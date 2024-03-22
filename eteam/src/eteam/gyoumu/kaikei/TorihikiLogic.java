package eteam.gyoumu.kaikei;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KAZEI_KBN_GROUP;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_ITEM;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIYOU_FLG;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShiwakePatternSettingAbstractDao;
import eteam.database.abstractdao.ShiwakePatternVarSettingAbstractDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiwakePatternSettingDao;
import eteam.database.dao.ShiwakePatternVarSettingDao;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiwakePatternVarSetting;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 取引仕訳管理機能Logic
 */
public class TorihikiLogic extends EteamAbstractLogic {

	/** 仕訳変数名（入力される） */
	protected static List<String> SHIWAKE_VAR_NAME_LIST;
	/** 仕訳変数名（入力される） */
	protected static List<String> SHIWAKE_VAR_LIST;
	
//＜部品＞
	/** 会計　SELECT */
	KaikeiCategoryLogic kaikeiLogic;
	/** 起票ナビカテゴリロジック */
	KihyouNaviCategoryLogic kihyouNaviLogic;
	/** マスターカテゴリー */
	MasterKanriCategoryLogic masterLg;
	/** 仕訳パターンVar設定Dao */
	ShiwakePatternVarSettingAbstractDao shiwakePatternVarSettingDao;
	/** 仕訳パターン設定Dao */
	ShiwakePatternSettingAbstractDao shiwakePatternSettingDao;

	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, _connection);
		kihyouNaviLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, _connection);
		masterLg = EteamContainer.getComponent(MasterKanriCategoryLogic.class, _connection);
		this.shiwakePatternVarSettingDao = EteamContainer.getComponent(ShiwakePatternVarSettingDao.class, connection);
		this.shiwakePatternSettingDao = EteamContainer.getComponent(ShiwakePatternSettingDao.class, connection);
	}
	
	/**
	 * 取引仕訳データを登録。参照先はもうない（+長すぎて邪魔な）ので形だけ残して親メソッドは実質無効化
	 * @param dataMap 登録データ
	 * @param shiwakeEdano 仕訳枝番：自動採番ならnullを渡す
	 * @return 登録結果
	 */
	@Deprecated
	public int insert(GMap dataMap, Integer shiwakeEdano) {
		return 0;
	}

	/**
	 * 取引仕訳データを更新。参照先はもうない（+長すぎて邪魔な）ので形だけ残して親メソッドは実質無効化
	 * @param dataMap  更新データ
	 * @return 更新結果
	 */
	@Deprecated
	public int update(GMap dataMap) {
		return 0;
	}

	/**
	 * 仕訳パターンマスターテーブルに存在するかチェック
	 * @param denpyouKbn String 伝票区分
	 * @param shiwakeEdano int 仕訳枝番
	 * @return 存在すればtrue
	 */
	@Deprecated
	public boolean existsShiwakeEdano(String denpyouKbn, int shiwakeEdano) {
		final String sql = "SELECT COUNT(*) count FROM shiwake_pattern_master WHERE denpyou_kbn = ? AND shiwake_edano = ?";
		long count = (long)connection.find(sql, denpyouKbn, shiwakeEdano).get("count");
		return (count > 0);
	}
	
	/**
	 * 仕訳パターンマスターテーブルのデータを全て削除する。
	 * @return 削除レコード数
	 */
	@Deprecated
	public long deleteAllShiwakePatternMaster(){
		
		int count = 0;
		count += connection.update("DELETE FROM shiwake_pattern_master ");
		
		return count;
	}

	/**
	 * 所属部門仕訳パターンマスターテーブルから削除
	 * @param bumonCd  String 所属部門コード
	 * @param denpyouKbn  String 伝票区分
	 * @param torihikiNm  String 取引名
	 * @return 削除結果
	 */
	public int deleteShozokuBumonShiwakeInfo(String bumonCd, String denpyouKbn, String torihikiNm) {
		String sql = 
				"DELETE FROM  "
				+ "      shozoku_bumon_shiwake_pattern_master A  "
				+ " WHERE   "
				+ "      A.bumon_cd = ?  "
				+ " AND  EXISTS  "
				+ "    ( "
				+ "        SELECT *  "
				+ "          FROM shiwake_pattern_master B "
				+ "         WHERE  "
				+ "                B.denpyou_kbn   = A.denpyou_kbn "
				+ "           AND  B.shiwake_edano = A.shiwake_edano "
				+ "           AND  B.denpyou_kbn   = ? ";
		
		List<Object> params = new ArrayList<Object>();
		
		params.add(bumonCd);
		params.add(denpyouKbn);
		
		if(null != torihikiNm && !"".equals(torihikiNm.trim())) {
			sql = sql + "     AND  B.torihiki_name LIKE ?";
			params.add(new StringBuilder().append("%").append(torihikiNm.trim()).append("%").toString());
		}
		
		sql = sql + "    ) ";

		return connection.update(sql, params.toArray());
	}
	
	
	/**
	 * 所属部門仕訳パターンマスターテーブルから削除(仕訳枝番号を使用)
	 * @param bumonCd  String 所属部門コード
	 * @param denpyouKbn  String 伝票区分
	 * @param shiwakeEdano  int 仕訳枝番号
	 * @return 削除結果
	 */
	@Deprecated
	public int deleteShozokuBumonShiwakeEdano(String bumonCd, String denpyouKbn, int shiwakeEdano) {
		String sql = 
				"DELETE FROM  "
				+ "      shozoku_bumon_shiwake_pattern_master A  "
				+ " WHERE  A.bumon_cd = ?  "
				+ " AND    A.denpyou_kbn = ?  "
				+ " AND    A.shiwake_edano = ?  ";
		
		List<Object> params = new ArrayList<Object>();
		
		params.add(bumonCd);
		params.add(denpyouKbn);
		params.add(shiwakeEdano);

		return connection.update(sql, params.toArray());
	}
	
	/**
	 * 所属部門仕訳パターンマスターテーブルのデータを全て削除する。
	 * @return 削除レコード数
	 */
	@Deprecated
	public long deleteAllShozokuBumonShiwake(){
		
		int count = 0;
		count += connection.update("DELETE FROM shozoku_bumon_shiwake_pattern_master ");
		
		return count;
	}

	/**
	 * 所属部門仕訳パターンマスターテーブルに登録
	 * @param bumonCd  String 所属部門コード
	 * @param denpyouKbn  String 伝票区分
	 * @param shiwakeEdano  int 仕訳枝番
	 * @param userId  String ユーザーID
	 * @return 登録結果
	 */
	@Deprecated
	public int insertShozokuBumonShiwakeInfo(String bumonCd, String denpyouKbn, int shiwakeEdano, String userId) {
		final String sql = "INSERT INTO "
				+ "shozoku_bumon_shiwake_pattern_master "
				+ "("
				+ " bumon_cd, "
				+ " denpyou_kbn, "
				+ " shiwake_edano, "
				+ " touroku_user_id, "
				+ " touroku_time, "
				+ " koushin_user_id, "
				+ " koushin_time "
				+ ") VALUES ("
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " ?,"
				+ " current_timestamp,"
				+ " ?,"
				+ " current_timestamp"
				+ ")";
		return connection.update(sql, bumonCd, denpyouKbn, shiwakeEdano, userId, userId);
	}

	/**
	 * 仕訳パターンマスターで最新の摘要フラグを取得する
	 * @return 摘要フラグ
	 */
	@Deprecated
	public String selLatestShiwakePatternMaster() {
		final String sql ="SELECT "
						+ "  tekiyou_flg "
						+ "FROM "
						+ "  shiwake_pattern_master "
						+ "ORDER BY "
						+ "  koushin_time DESC "
						+ "LIMIT "
						+ "  1 ";
		GMap ret = connection.find(sql);
		if (ret == null || ret.isEmpty()) {
			return "0";
		} else {
			return (String)ret.get("tekiyou_flg");
		}
	}

	/**
	 * 仕訳パターン変数チェック。
	 * 入力文言を該当項目の変数として許容するかどうかチェックする。
	 * 許容しないのであれば、エラーメッセージを詰める。
	 * @param nm 項目名
	 * @param cd 項目値
	 * @param shiwakeVarList 仕訳パターン変数リスト
	 * @param errorList エラーリスト
	 */
	public void shiwakeVarCheck(String nm, String cd, List<GMap> shiwakeVarList, List<String> errorList) {
		
		if (! isHensuu(cd))
		{
			return;
		}
		
		for (GMap shiwakeVarMap : shiwakeVarList) {
			
			if(cd.equals(shiwakeVarMap.get("shiwake_pattern_var_name").toString()))
			{
				return;
			}
		}
			
		errorList.add(nm + "には、" + cd + "を入力することができません。");
	}
	
	/**
	 * 仕訳パターン変数名を変数値に変換する
	 * @param nm 変数名
	 * @param shiwakeVarList 仕訳パターン変数リスト
	 * @return 変数値
	 */
	public String ConvertShiwakeVarNm2Cd(String nm, List<GMap> shiwakeVarList) {
		
		for (GMap shiwakeVarMap : shiwakeVarList) {
			
			if(nm.equals(shiwakeVarMap.get("shiwake_pattern_var_name").toString()))
			{
				return shiwakeVarMap.get("shiwake_pattern_var").toString();
			}
		}
		return nm;
	}
	
	/**
	 * 仕訳パターン変数値を変数名に変換する
	 * @param cd 変数値
	 * @param shiwakeVarList 仕訳パターン変数リスト
	 * @return 変数名
	 */
	public String ConvertShiwakeVarCd2Nm(String cd, List<GMap> shiwakeVarList) {
		
		for (GMap shiwakeVarMap : shiwakeVarList) {
			
			if(cd.equals(shiwakeVarMap.get("shiwake_pattern_var").toString()))
			{
				return shiwakeVarMap.get("shiwake_pattern_var_name").toString();
			}
		}
		return cd;
	}

	/**
	 * 項目が表示対象であるか調べる
	 * @param denpyouKbn 伝票区分
	 * @param shiwakePatternSettingKbn 仕訳パターン設定区分
	 * @param shiwakePatternSettingItem 仕訳パターン設定項目
	 * @return 表示対象ならtrue
	 */
	public boolean judgeHyouji(String denpyouKbn, String shiwakePatternSettingKbn, String shiwakePatternSettingItem) {
		var record = this.shiwakePatternSettingDao.find(denpyouKbn, shiwakePatternSettingKbn, shiwakePatternSettingItem);
		return null != record && "1".equals(record.hyoujiFlg);
	}

	/**
	 * 仕訳変数名（shiwake_pattern_var_setting.shiwake_pattern_var_name）であるかチェックする
	 * @param s 文字列
	 * @return 変数である
	 */
	public boolean isHensuu(String s) {
		if (SHIWAKE_VAR_NAME_LIST == null) {
			this.shiwakePatternVarSettingDao = EteamContainer.getComponent(ShiwakePatternVarSettingDao.class, connection);
			List<ShiwakePatternVarSetting> varList = shiwakePatternVarSettingDao.load();
			SHIWAKE_VAR_NAME_LIST = new ArrayList<>(varList.size());
			for (var varSetting : varList) {
				SHIWAKE_VAR_NAME_LIST.add(varSetting.shiwakePatternVarName);
			}
		}
		return SHIWAKE_VAR_NAME_LIST.contains(s);
	}

	/**
	 * 仕訳パターン変数（shiwake_pattern_var_setting.shiwake_pattern_var）であるかチェックする
	 * @param s 文字列
	 * @return 変数である
	 */
	public boolean isHensuuVar(String s) {
		if (SHIWAKE_VAR_LIST == null) {
			this.shiwakePatternVarSettingDao = EteamContainer.getComponent(ShiwakePatternVarSettingDao.class, connection);
			List<ShiwakePatternVarSetting> varList = shiwakePatternVarSettingDao.load();
			SHIWAKE_VAR_LIST = new ArrayList<>(varList.size());
			for (var varSetting : varList) {
				SHIWAKE_VAR_LIST.add(varSetting.shiwakePatternVar);
			}
		}
		return SHIWAKE_VAR_LIST.contains(s);
	}
	/**
	 * 取引（仕訳）追加/変更画面の入力制御情報を作成する。
	 * shiwake_pattern_settingテーブルの設定に基いて各種項目の表示有無を返す。
	 * @param denpyouKbn 伝票区分
	 * @return 入力制御情報
	 */
	public ShiwakePatternInputSeigyo inputSeigyo(String denpyouKbn) {
		SystemKanriCategoryLogic sysCateLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		
		// 伝票区分による仕訳作成方法判定
		String shiwakeSakusei = "3";
		switch (denpyouKbn) { 
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
				shiwakeSakusei = setting.shiwakeSakuseiHouhouA001();
				break;
			case DENPYOU_KBN.RYOHI_SEISAN:
				shiwakeSakusei = setting.shiwakeSakuseiHouhouA004();
				break;
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			case "A901":
				shiwakeSakusei = setting.shiwakeSakuseiHouhouA011();
				break;
			case DENPYOU_KBN.KOUTSUUHI_SEISAN:
				shiwakeSakusei = setting.shiwakeSakuseiHouhouA010();
				break;
			case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
				shiwakeSakusei = setting.shiwakeSakuseiHouhouA009();
				break;
			case DENPYOU_KBN.SIHARAIIRAI:
				shiwakeSakusei = "2";
		}
		
		// カスタマイズ用
		shiwakeSakusei = shiwakeSakusei_Ext(denpyouKbn, shiwakeSakusei);
				
		ShiwakePatternInputSeigyo inputSeigyo = new ShiwakePatternInputSeigyo();
		inputSeigyo.shiwakeVarList = shiwakePatternVarSettingDao.loadOrderByHyoujiJun().stream().map(ShiwakePatternVarSetting::getMap).collect(Collectors.toList());
		inputSeigyo.shiwakeVarListKariBumon = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
		inputSeigyo.shiwakeVarListKariKamoku = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
		inputSeigyo.shiwakeVarListKariEdano = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
		inputSeigyo.shiwakeVarListKariTorihiki = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
		inputSeigyo.shiwakeVarListKariProject = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
		inputSeigyo.shiwakeVarListKariSegment = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
		inputSeigyo.shiwakeVarListKariUf1 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
		inputSeigyo.shiwakeVarListKariUf2 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
		inputSeigyo.shiwakeVarListKariUf3 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
		inputSeigyo.shiwakeVarListKariUf4 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
		inputSeigyo.shiwakeVarListKariUf5 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
		inputSeigyo.shiwakeVarListKariUf6 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
		inputSeigyo.shiwakeVarListKariUf7 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
		inputSeigyo.shiwakeVarListKariUf8 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
		inputSeigyo.shiwakeVarListKariUf9 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
		inputSeigyo.shiwakeVarListKariUf10 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF10);
		inputSeigyo.shiwakeVarListKashi1Bumon = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
		inputSeigyo.shiwakeVarListKashi1Kamoku = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
		inputSeigyo.shiwakeVarListKashi1Edano = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
		inputSeigyo.shiwakeVarListKashi1Torihiki= kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
		inputSeigyo.shiwakeVarListKashi1Project = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
		inputSeigyo.shiwakeVarListKashi1Segment = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
		inputSeigyo.shiwakeVarListKashi1Uf1 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
		inputSeigyo.shiwakeVarListKashi1Uf2 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
		inputSeigyo.shiwakeVarListKashi1Uf3 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
		inputSeigyo.shiwakeVarListKashi1Uf4 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
		inputSeigyo.shiwakeVarListKashi1Uf5 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
		inputSeigyo.shiwakeVarListKashi1Uf6 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
		inputSeigyo.shiwakeVarListKashi1Uf7 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
		inputSeigyo.shiwakeVarListKashi1Uf8 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
		inputSeigyo.shiwakeVarListKashi1Uf9 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
		inputSeigyo.shiwakeVarListKashi1Uf10 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF10);
		inputSeigyo.shiwakeVarListKashi2Bumon = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
		inputSeigyo.shiwakeVarListKashi2Kamoku = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
		inputSeigyo.shiwakeVarListKashi2Edano = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
		inputSeigyo.shiwakeVarListKashi2Torihiki= kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
		inputSeigyo.shiwakeVarListKashi2Project = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
		inputSeigyo.shiwakeVarListKashi2Segment = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
		inputSeigyo.shiwakeVarListKashi2Uf1 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
		inputSeigyo.shiwakeVarListKashi2Uf2 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
		inputSeigyo.shiwakeVarListKashi2Uf3 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
		inputSeigyo.shiwakeVarListKashi2Uf4 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
		inputSeigyo.shiwakeVarListKashi2Uf4 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
		inputSeigyo.shiwakeVarListKashi2Uf5 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
		inputSeigyo.shiwakeVarListKashi2Uf6 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
		inputSeigyo.shiwakeVarListKashi2Uf7 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
		inputSeigyo.shiwakeVarListKashi2Uf8 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
		inputSeigyo.shiwakeVarListKashi2Uf9 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
		inputSeigyo.shiwakeVarListKashi2Uf10 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF10);
		inputSeigyo.shiwakeVarListKashi3Bumon = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
		inputSeigyo.shiwakeVarListKashi3Kamoku = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
		inputSeigyo.shiwakeVarListKashi3Edano = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
		inputSeigyo.shiwakeVarListKashi3Torihiki= kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
		inputSeigyo.shiwakeVarListKashi3Project = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
		inputSeigyo.shiwakeVarListKashi3Segment = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
		inputSeigyo.shiwakeVarListKashi3Uf1 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
		inputSeigyo.shiwakeVarListKashi3Uf2 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
		inputSeigyo.shiwakeVarListKashi3Uf3 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
		inputSeigyo.shiwakeVarListKashi3Uf4 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
		inputSeigyo.shiwakeVarListKashi3Uf5 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
		inputSeigyo.shiwakeVarListKashi3Uf6 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
		inputSeigyo.shiwakeVarListKashi3Uf7 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
		inputSeigyo.shiwakeVarListKashi3Uf8 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
		inputSeigyo.shiwakeVarListKashi3Uf9 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
		inputSeigyo.shiwakeVarListKashi3Uf10 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF10);
		inputSeigyo.shiwakeVarListKashi4Bumon = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
		inputSeigyo.shiwakeVarListKashi4Kamoku = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
		inputSeigyo.shiwakeVarListKashi4Edano = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
		inputSeigyo.shiwakeVarListKashi4Torihiki= kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
		inputSeigyo.shiwakeVarListKashi4Project = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
		inputSeigyo.shiwakeVarListKashi4Segment = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
		inputSeigyo.shiwakeVarListKashi4Uf1 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
		inputSeigyo.shiwakeVarListKashi4Uf2 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
		inputSeigyo.shiwakeVarListKashi4Uf3 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
		inputSeigyo.shiwakeVarListKashi4Uf4 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
		inputSeigyo.shiwakeVarListKashi4Uf5 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
		inputSeigyo.shiwakeVarListKashi4Uf6 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
		inputSeigyo.shiwakeVarListKashi4Uf7 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
		inputSeigyo.shiwakeVarListKashi4Uf8 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
		inputSeigyo.shiwakeVarListKashi4Uf9 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
		inputSeigyo.shiwakeVarListKashi4Uf10 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF10);
		inputSeigyo.shiwakeVarListKashi5Bumon = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
		inputSeigyo.shiwakeVarListKashi5Kamoku = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
		inputSeigyo.shiwakeVarListKashi5Edano = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
		inputSeigyo.shiwakeVarListKashi5Torihiki= kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
		inputSeigyo.shiwakeVarListKashi5Project = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
		inputSeigyo.shiwakeVarListKashi5Segment = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
		inputSeigyo.shiwakeVarListKashi5Uf1 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
		inputSeigyo.shiwakeVarListKashi5Uf2 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
		inputSeigyo.shiwakeVarListKashi5Uf3 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
		inputSeigyo.shiwakeVarListKashi5Uf4 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
		inputSeigyo.shiwakeVarListKashi5Uf5 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
		inputSeigyo.shiwakeVarListKashi5Uf6 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
		inputSeigyo.shiwakeVarListKashi5Uf7 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
		inputSeigyo.shiwakeVarListKashi5Uf8 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
		inputSeigyo.shiwakeVarListKashi5Uf9 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
		inputSeigyo.shiwakeVarListKashi5Uf10 = kaikeiLogic.selectShiwakeVar(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF10);

		inputSeigyo.defaultHyouji = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.TORIHIKI_NAIYOU, SHIWAKE_PATTERN_SETTING_ITEM.DEFAULT_HYOUJI);
		inputSeigyo.kousaihiHyouji = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.TORIHIKI_NAIYOU, SHIWAKE_PATTERN_SETTING_ITEM.KOUSAIHI);
		inputSeigyo.kakeHyouji = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.TORIHIKI_NAIYOU, SHIWAKE_PATTERN_SETTING_ITEM.KAKE);
		inputSeigyo.shainCdRenkeiHyouji= judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.TORIHIKI_NAIYOU, SHIWAKE_PATTERN_SETTING_ITEM.SHAIN);
		inputSeigyo.zaimuEdabanRenkeiHyouji= judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.TORIHIKI_NAIYOU, SHIWAKE_PATTERN_SETTING_ITEM.ZAIMU_EDABAN);
		
		inputSeigyo.kariBumon = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
		inputSeigyo.kariKamoku = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
		inputSeigyo.kariKamokuEda = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
		inputSeigyo.kariTorihiki = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
		inputSeigyo.kariProject = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
		inputSeigyo.kariSegment = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
		inputSeigyo.kariUf1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
		inputSeigyo.kariUf2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
		inputSeigyo.kariUf3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
		inputSeigyo.kariUf4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
		inputSeigyo.kariUf5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
		inputSeigyo.kariUf6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
		inputSeigyo.kariUf7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
		inputSeigyo.kariUf8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
		inputSeigyo.kariUf9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
		inputSeigyo.kariUf10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF10);
		inputSeigyo.kariUfKotei1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI1);
		inputSeigyo.kariUfKotei2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI2);
		inputSeigyo.kariUfKotei3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI3);
		inputSeigyo.kariUfKotei4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI4);
		inputSeigyo.kariUfKotei5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI5);
		inputSeigyo.kariUfKotei6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI6);
		inputSeigyo.kariUfKotei7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI7);
		inputSeigyo.kariUfKotei8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI8);
		inputSeigyo.kariUfKotei9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI9);
		inputSeigyo.kariUfKotei10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI10);
		inputSeigyo.kariKazeiKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
		inputSeigyo.kariZeiritsu = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.ZEIRITSU);
		inputSeigyo.kariSetsumei = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
		inputSeigyo.kariBunriKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);
		inputSeigyo.kariShiireKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);

		inputSeigyo.kashi1Bumon = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
		inputSeigyo.kashi1Kamoku = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
		inputSeigyo.kashi1KamokuEda = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
		inputSeigyo.kashi1Torihiki = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
		inputSeigyo.kashi1Project = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
		inputSeigyo.kashi1Segment = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
		inputSeigyo.kashi1Uf1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
		inputSeigyo.kashi1Uf2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
		inputSeigyo.kashi1Uf3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
		inputSeigyo.kashi1Uf4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
		inputSeigyo.kashi1Uf5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
		inputSeigyo.kashi1Uf6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
		inputSeigyo.kashi1Uf7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
		inputSeigyo.kashi1Uf8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
		inputSeigyo.kashi1Uf9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
		inputSeigyo.kashi1Uf10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF10);
		inputSeigyo.kashi1UfKotei1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI1);
		inputSeigyo.kashi1UfKotei2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI2);
		inputSeigyo.kashi1UfKotei3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI3);
		inputSeigyo.kashi1UfKotei4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI4);
		inputSeigyo.kashi1UfKotei5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI5);
		inputSeigyo.kashi1UfKotei6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI6);
		inputSeigyo.kashi1UfKotei7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI7);
		inputSeigyo.kashi1UfKotei8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI8);
		inputSeigyo.kashi1UfKotei9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI9);
		inputSeigyo.kashi1UfKotei10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI10);
		inputSeigyo.kashi1KazeiKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
		inputSeigyo.kashi1Setsumei = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
		inputSeigyo.kashi1BunriKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);
		inputSeigyo.kashi1ShiireKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);
		
		// そもそもbooleanは初期値falseなので、特定条件が共通するなら、それを満たした時だけ代入すればよい
		// 貸方2
		if((!DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU.equals(denpyouKbn) || !"3".equals(shiwakeSakusei)))
		{
			inputSeigyo.kashi2Bumon = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
			inputSeigyo.kashi2Kamoku = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
			inputSeigyo.kashi2KamokuEda = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
			inputSeigyo.kashi2Torihiki = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
			inputSeigyo.kashi2Project = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
			inputSeigyo.kashi2Segment = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
			inputSeigyo.kashi2Uf1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
			inputSeigyo.kashi2Uf2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
			inputSeigyo.kashi2Uf3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
			inputSeigyo.kashi2Uf4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
			inputSeigyo.kashi2Uf5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
			inputSeigyo.kashi2Uf6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
			inputSeigyo.kashi2Uf7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
			inputSeigyo.kashi2Uf8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
			inputSeigyo.kashi2Uf9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
			inputSeigyo.kashi2Uf10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF10);
			inputSeigyo.kashi2UfKotei1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI1);
			inputSeigyo.kashi2UfKotei2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI2);
			inputSeigyo.kashi2UfKotei3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI3);
			inputSeigyo.kashi2UfKotei4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI4);
			inputSeigyo.kashi2UfKotei5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI5);
			inputSeigyo.kashi2UfKotei6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI6);
			inputSeigyo.kashi2UfKotei7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI7);
			inputSeigyo.kashi2UfKotei8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI8);
			inputSeigyo.kashi2UfKotei9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI9);
			inputSeigyo.kashi2UfKotei10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI10);
			inputSeigyo.kashi2KazeiKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			inputSeigyo.kashi2Setsumei = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
			inputSeigyo.kashi2BunriKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);
			inputSeigyo.kashi2ShiireKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);
		}
		
		// 貸方3
		if(!"3".equals(shiwakeSakusei))
		{
			inputSeigyo.kashi3Bumon = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
			inputSeigyo.kashi3Kamoku = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
			inputSeigyo.kashi3KamokuEda = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
			inputSeigyo.kashi3Torihiki = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
			inputSeigyo.kashi3Project = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
			inputSeigyo.kashi3Segment = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
			inputSeigyo.kashi3Uf1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
			inputSeigyo.kashi3Uf2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
			inputSeigyo.kashi3Uf3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
			inputSeigyo.kashi3Uf4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
			inputSeigyo.kashi3Uf5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
			inputSeigyo.kashi3Uf6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
			inputSeigyo.kashi3Uf7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
			inputSeigyo.kashi3Uf8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
			inputSeigyo.kashi3Uf9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
			inputSeigyo.kashi3Uf10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF10);
			inputSeigyo.kashi3UfKotei1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI1);
			inputSeigyo.kashi3UfKotei2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI2);
			inputSeigyo.kashi3UfKotei3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI3);
			inputSeigyo.kashi3UfKotei4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI4);
			inputSeigyo.kashi3UfKotei5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI5);
			inputSeigyo.kashi3UfKotei6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI6);
			inputSeigyo.kashi3UfKotei7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI7);
			inputSeigyo.kashi3UfKotei8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI8);
			inputSeigyo.kashi3UfKotei9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI9);
			inputSeigyo.kashi3UfKotei10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI10);
			inputSeigyo.kashi3KazeiKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			inputSeigyo.kashi3Setsumei = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
			inputSeigyo.kashi3BunriKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);
			inputSeigyo.kashi3ShiireKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);
		}
		

		
		// 法人カード・会社手配の使用可否
		boolean isFlgTargetDenpyou = List.of(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, "A901", DENPYOU_KBN.RYOHI_SEISAN, DENPYOU_KBN.KAIGAI_RYOHI_SEISAN, DENPYOU_KBN.KOUTSUUHI_SEISAN).contains(denpyouKbn); 
		boolean houjinCardFlag = isFlgTargetDenpyou && sysCateLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD);
		
		// 貸方4
		if(houjinCardFlag)
		{
			inputSeigyo.kashi4Bumon = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
			inputSeigyo.kashi4Kamoku = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
			inputSeigyo.kashi4KamokuEda = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
			inputSeigyo.kashi4Torihiki = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
			inputSeigyo.kashi4Project = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
			inputSeigyo.kashi4Segment = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
			inputSeigyo.kashi4Uf1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
			inputSeigyo.kashi4Uf2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
			inputSeigyo.kashi4Uf3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
			inputSeigyo.kashi4Uf4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
			inputSeigyo.kashi4Uf5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
			inputSeigyo.kashi4Uf6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
			inputSeigyo.kashi4Uf7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
			inputSeigyo.kashi4Uf8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
			inputSeigyo.kashi4Uf9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
			inputSeigyo.kashi4Uf10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF10);
			inputSeigyo.kashi4UfKotei1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI1);
			inputSeigyo.kashi4UfKotei2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI2);
			inputSeigyo.kashi4UfKotei3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI3);
			inputSeigyo.kashi4UfKotei4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI4);
			inputSeigyo.kashi4UfKotei5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI5);
			inputSeigyo.kashi4UfKotei6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI6);
			inputSeigyo.kashi4UfKotei7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI7);
			inputSeigyo.kashi4UfKotei8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI8);
			inputSeigyo.kashi4UfKotei9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI9);
			inputSeigyo.kashi4UfKotei10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI10);
			inputSeigyo.kashi4KazeiKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			inputSeigyo.kashi4Setsumei = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
			inputSeigyo.kashi4BunriKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);
			inputSeigyo.kashi4ShiireKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);
		}


		boolean kaishaTehaiFlag = isFlgTargetDenpyou && sysCateLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI);
		
		// 貸方5
		if(kaishaTehaiFlag)
		{
			inputSeigyo.kashi5Bumon = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.BUMON);
			inputSeigyo.kashi5Kamoku = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKU);
			inputSeigyo.kashi5KamokuEda = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.KAMOKUEDABAN);
			inputSeigyo.kashi5Torihiki = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.TORIHIKI);
			inputSeigyo.kashi5Project = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.PROJECT);
			inputSeigyo.kashi5Segment = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG).equals(SHIYOU_FLG.SHIYOU_SURU) && judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.SEGMENT);
			inputSeigyo.kashi5Uf1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF1);
			inputSeigyo.kashi5Uf2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF2);
			inputSeigyo.kashi5Uf3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF3);
			inputSeigyo.kashi5Uf4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF4);
			inputSeigyo.kashi5Uf5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF5);
			inputSeigyo.kashi5Uf6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF6);
			inputSeigyo.kashi5Uf7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF7);
			inputSeigyo.kashi5Uf8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF8);
			inputSeigyo.kashi5Uf9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF9);
			inputSeigyo.kashi5Uf10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF10);
			inputSeigyo.kashi5UfKotei1 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI1);
			inputSeigyo.kashi5UfKotei2 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI2);
			inputSeigyo.kashi5UfKotei3 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI3);
			inputSeigyo.kashi5UfKotei4 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI4);
			inputSeigyo.kashi5UfKotei5 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI5);
			inputSeigyo.kashi5UfKotei6 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI6);
			inputSeigyo.kashi5UfKotei7 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI7);
			inputSeigyo.kashi5UfKotei8 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI8);
			inputSeigyo.kashi5UfKotei9 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI9);
			inputSeigyo.kashi5UfKotei10 = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.UF_KOTEI10);
			inputSeigyo.kashi5KazeiKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.KAZEIKBN);
			inputSeigyo.kashi5Setsumei = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
			inputSeigyo.kashi5BunriKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.BUNRIKBN);
			inputSeigyo.kashi5ShiireKbn = judgeHyouji(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5, SHIWAKE_PATTERN_SETTING_ITEM.KOBETSUKBN);
		}

		inputSeigyo.kariSetsumeiStr = kaikeiLogic.findShiwakePatternDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KARIKATA, SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
		inputSeigyo.kashiSetsumei1Str = kaikeiLogic.findShiwakePatternDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1,SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
		inputSeigyo.kashiSetsumei2Str = kaikeiLogic.findShiwakePatternDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2,SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
		inputSeigyo.kashiSetsumei3Str = kaikeiLogic.findShiwakePatternDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3,SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
		inputSeigyo.kashiSetsumei4Str = kaikeiLogic.findShiwakePatternDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4,SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
		inputSeigyo.kashiSetsumei5Str = kaikeiLogic.findShiwakePatternDefaultValue(denpyouKbn, SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5,SHIWAKE_PATTERN_SETTING_ITEM.SETSUMEI);
		return inputSeigyo;
	}

	/**
	 * 取引一覧から貸方コードと科目名の一覧を作成する。
	 * 
	 * @param torihikiMap 取引一覧
	 * @return 貸方科目一覧
	 */
	public String makeKashiKamokuView(Map<String , Object> torihikiMap){
		Map<String , Object> dataMap = new LinkedHashMap<String , Object>();

		String kashiKamokuCd1 = torihikiMap.get("kashi_kamoku_cd1").toString();
		String kashiKamokuCd2 = torihikiMap.get("kashi_kamoku_cd2").toString();
		String kashiKamokuCd3 = torihikiMap.get("kashi_kamoku_cd3").toString();
		String kashiKamokuCd4 = torihikiMap.get("kashi_kamoku_cd4").toString();
		String kashiKamokuCd5 = torihikiMap.get("kashi_kamoku_cd5").toString();
		
		if(!kashiKamokuCd1.equals("")) dataMap.put("kashi_kata1", kashiKamokuCd1 + "：" + (torihikiMap.get("kashi_kamoku_name1") != null ? torihikiMap.get("kashi_kamoku_name1").toString() : ""));
		if(!kashiKamokuCd2.equals("")) dataMap.put("kashi_kata2", kashiKamokuCd2 + "：" + (torihikiMap.get("kashi_kamoku_name2") != null ? torihikiMap.get("kashi_kamoku_name2").toString() : ""));
		if(!kashiKamokuCd3.equals("")) dataMap.put("kashi_kata3", kashiKamokuCd3 + "：" + (torihikiMap.get("kashi_kamoku_name3") != null ? torihikiMap.get("kashi_kamoku_name3").toString() : ""));
		if(!kashiKamokuCd4.equals("")) dataMap.put("kashi_kata4", kashiKamokuCd4 + "：" + (torihikiMap.get("kashi_kamoku_name4") != null ? torihikiMap.get("kashi_kamoku_name4").toString() : ""));
		if(!kashiKamokuCd5.equals("")) dataMap.put("kashi_kata5", kashiKamokuCd5 + "：" + (torihikiMap.get("kashi_kamoku_name5") != null ? torihikiMap.get("kashi_kamoku_name5").toString() : ""));
		
		String retStr = "";
		for(Map.Entry<String, Object> entry : dataMap.entrySet()) {
			if (!retStr.equals(""))
			{
				retStr += "\r\n";
			}
			retStr += entry.getValue();
		}
		
		return retStr;
	}

	/**
	 * 各種項目入力制御
	 */
	@Getter @Setter @ToString
	public class ShiwakePatternInputSeigyo {
		/** 仕訳パターン変数List */
		List<GMap> shiwakeVarList;
		/** 仕訳パターン変数List(借方・負担部門) */
		List<GMap> shiwakeVarListKariBumon;
		/** 仕訳パターン変数List(借方・勘定科目) */
		List<GMap> shiwakeVarListKariKamoku;
		/** 仕訳パターン変数List(借方・勘定科目枝番) */
		List<GMap> shiwakeVarListKariEdano;
		/** 仕訳パターン変数List(借方・取引先) */
		List<GMap> shiwakeVarListKariTorihiki;
		/** 仕訳パターン変数List(借方・プロジェクト) */
		List<GMap> shiwakeVarListKariProject;
		/** 仕訳パターン変数List(借方・セグメント) */
		List<GMap> shiwakeVarListKariSegment;
		/** 仕訳パターン変数List(借方・UF1) */
		List<GMap> shiwakeVarListKariUf1;
		/** 仕訳パターン変数List(借方・UF2) */
		List<GMap> shiwakeVarListKariUf2;
		/** 仕訳パターン変数List(借方・UF3) */
		List<GMap> shiwakeVarListKariUf3;
		/** 仕訳パターン変数List(借方・UF4) */
		List<GMap> shiwakeVarListKariUf4;
		/** 仕訳パターン変数List(借方・UF5) */
		List<GMap> shiwakeVarListKariUf5;
		/** 仕訳パターン変数List(借方・UF6) */
		List<GMap> shiwakeVarListKariUf6;
		/** 仕訳パターン変数List(借方・UF7) */
		List<GMap> shiwakeVarListKariUf7;
		/** 仕訳パターン変数List(借方・UF8) */
		List<GMap> shiwakeVarListKariUf8;
		/** 仕訳パターン変数List(借方・UF9) */
		List<GMap> shiwakeVarListKariUf9;
		/** 仕訳パターン変数List(借方・UF10) */
		List<GMap> shiwakeVarListKariUf10;
		/** 仕訳パターン変数List(貸方1・負担部門) */
		List<GMap> shiwakeVarListKashi1Bumon;
		/** 仕訳パターン変数List(貸方1・勘定科目) */
		List<GMap> shiwakeVarListKashi1Kamoku;
		/** 仕訳パターン変数List(貸方1・勘定科目枝番) */
		List<GMap> shiwakeVarListKashi1Edano;
		/** 仕訳パターン変数List(貸方1・取引先) */
		List<GMap> shiwakeVarListKashi1Torihiki;
		/** 仕訳パターン変数List(貸方1・プロジェクト) */
		List<GMap> shiwakeVarListKashi1Project;
		/** 仕訳パターン変数List(貸方1・セグメント) */
		List<GMap> shiwakeVarListKashi1Segment;
		/** 仕訳パターン変数List(貸方1・UF1) */
		List<GMap> shiwakeVarListKashi1Uf1;
		/** 仕訳パターン変数List(貸方1・UF2) */
		List<GMap> shiwakeVarListKashi1Uf2;
		/** 仕訳パターン変数List(貸方1・UF3) */
		List<GMap> shiwakeVarListKashi1Uf3;
		/** 仕訳パターン変数List(貸方1・UF4) */
		List<GMap> shiwakeVarListKashi1Uf4;
		/** 仕訳パターン変数List(貸方1・UF5) */
		List<GMap> shiwakeVarListKashi1Uf5;
		/** 仕訳パターン変数List(貸方1・UF6) */
		List<GMap> shiwakeVarListKashi1Uf6;
		/** 仕訳パターン変数List(貸方1・UF7) */
		List<GMap> shiwakeVarListKashi1Uf7;
		/** 仕訳パターン変数List(貸方1・UF8) */
		List<GMap> shiwakeVarListKashi1Uf8;
		/** 仕訳パターン変数List(貸方1・UF9) */
		List<GMap> shiwakeVarListKashi1Uf9;
		/** 仕訳パターン変数List(貸方1・UF10) */
		List<GMap> shiwakeVarListKashi1Uf10;
		
		/** 仕訳パターン変数List(貸方2・負担部門) */
		List<GMap> shiwakeVarListKashi2Bumon;
		/** 仕訳パターン変数List(貸方2・勘定科目) */
		List<GMap> shiwakeVarListKashi2Kamoku;
		/** 仕訳パターン変数List(貸方2・勘定科目枝番) */
		List<GMap> shiwakeVarListKashi2Edano;
		/** 仕訳パターン変数List(貸方2・取引先) */
		List<GMap> shiwakeVarListKashi2Torihiki;
		/** 仕訳パターン変数List(貸方2・プロジェクト) */
		List<GMap> shiwakeVarListKashi2Project;
		/** 仕訳パターン変数List(貸方2・セグメント) */
		List<GMap> shiwakeVarListKashi2Segment;
		/** 仕訳パターン変数List(貸方2・UF1) */
		List<GMap> shiwakeVarListKashi2Uf1;
		/** 仕訳パターン変数List(貸方2・UF2) */
		List<GMap> shiwakeVarListKashi2Uf2;
		/** 仕訳パターン変数List(貸方2・UF3) */
		List<GMap> shiwakeVarListKashi2Uf3;
		/** 仕訳パターン変数List(貸方2・UF4) */
		List<GMap> shiwakeVarListKashi2Uf4;
		/** 仕訳パターン変数List(貸方2・UF5) */
		List<GMap> shiwakeVarListKashi2Uf5;
		/** 仕訳パターン変数List(貸方2・UF6) */
		List<GMap> shiwakeVarListKashi2Uf6;
		/** 仕訳パターン変数List(貸方2・UF7) */
		List<GMap> shiwakeVarListKashi2Uf7;
		/** 仕訳パターン変数List(貸方2・UF8) */
		List<GMap> shiwakeVarListKashi2Uf8;
		/** 仕訳パターン変数List(貸方2・UF9) */
		List<GMap> shiwakeVarListKashi2Uf9;
		/** 仕訳パターン変数List(貸方2・UF10) */
		List<GMap> shiwakeVarListKashi2Uf10;
		
		/** 仕訳パターン変数List(貸方3・負担部門) */
		List<GMap> shiwakeVarListKashi3Bumon;
		/** 仕訳パターン変数List(貸方3・勘定科目) */
		List<GMap> shiwakeVarListKashi3Kamoku;
		/** 仕訳パターン変数List(貸方3・勘定科目枝番) */
		List<GMap> shiwakeVarListKashi3Edano;
		/** 仕訳パターン変数List(貸方3・取引先) */
		List<GMap> shiwakeVarListKashi3Torihiki;
		/** 仕訳パターン変数List(貸方3・プロジェクト) */
		List<GMap> shiwakeVarListKashi3Project;
		/** 仕訳パターン変数List(貸方3・セグメント) */
		List<GMap> shiwakeVarListKashi3Segment;
		/** 仕訳パターン変数List(貸方3・UF1) */
		List<GMap> shiwakeVarListKashi3Uf1;
		/** 仕訳パターン変数List(貸方3・UF2) */
		List<GMap> shiwakeVarListKashi3Uf2;
		/** 仕訳パターン変数List(貸方3・UF3) */
		List<GMap> shiwakeVarListKashi3Uf3;
		/** 仕訳パターン変数List(貸方3・UF4) */
		List<GMap> shiwakeVarListKashi3Uf4;
		/** 仕訳パターン変数List(貸方3・UF5) */
		List<GMap> shiwakeVarListKashi3Uf5;
		/** 仕訳パターン変数List(貸方3・UF6) */
		List<GMap> shiwakeVarListKashi3Uf6;
		/** 仕訳パターン変数List(貸方3・UF7) */
		List<GMap> shiwakeVarListKashi3Uf7;
		/** 仕訳パターン変数List(貸方3・UF8) */
		List<GMap> shiwakeVarListKashi3Uf8;
		/** 仕訳パターン変数List(貸方3・UF9) */
		List<GMap> shiwakeVarListKashi3Uf9;
		/** 仕訳パターン変数List(貸方3・UF10) */
		List<GMap> shiwakeVarListKashi3Uf10;
		/** 仕訳パターン変数List(貸方4・負担部門) */
		List<GMap> shiwakeVarListKashi4Bumon;
		/** 仕訳パターン変数List(貸方4・勘定科目) */
		List<GMap> shiwakeVarListKashi4Kamoku;
		/** 仕訳パターン変数List(貸方4・勘定科目枝番) */
		List<GMap> shiwakeVarListKashi4Edano;
		/** 仕訳パターン変数List(貸方4・取引先) */
		List<GMap> shiwakeVarListKashi4Torihiki;
		/** 仕訳パターン変数List(貸方4・プロジェクト) */
		List<GMap> shiwakeVarListKashi4Project;
		/** 仕訳パターン変数List(貸方4・セグメント) */
		List<GMap> shiwakeVarListKashi4Segment;
		/** 仕訳パターン変数List(貸方4・UF1) */
		List<GMap> shiwakeVarListKashi4Uf1;
		/** 仕訳パターン変数List(貸方4・UF2) */
		List<GMap> shiwakeVarListKashi4Uf2;
		/** 仕訳パターン変数List(貸方4・UF3) */
		List<GMap> shiwakeVarListKashi4Uf3;
		/** 仕訳パターン変数List(貸方4・UF4) */
		List<GMap> shiwakeVarListKashi4Uf4;
		/** 仕訳パターン変数List(貸方4・UF5) */
		List<GMap> shiwakeVarListKashi4Uf5;
		/** 仕訳パターン変数List(貸方4・UF6) */
		List<GMap> shiwakeVarListKashi4Uf6;
		/** 仕訳パターン変数List(貸方4・UF7) */
		List<GMap> shiwakeVarListKashi4Uf7;
		/** 仕訳パターン変数List(貸方4・UF8) */
		List<GMap> shiwakeVarListKashi4Uf8;
		/** 仕訳パターン変数List(貸方4・UF9) */
		List<GMap> shiwakeVarListKashi4Uf9;
		/** 仕訳パターン変数List(貸方4・UF10) */
		List<GMap> shiwakeVarListKashi4Uf10;
		
		/** 仕訳パターン変数List(貸方5・負担部門) */
		List<GMap> shiwakeVarListKashi5Bumon;
		/** 仕訳パターン変数List(貸方5・勘定科目) */
		List<GMap> shiwakeVarListKashi5Kamoku;
		/** 仕訳パターン変数List(貸方5・勘定科目枝番) */
		List<GMap> shiwakeVarListKashi5Edano;
		/** 仕訳パターン変数List(貸方5・取引先) */
		List<GMap> shiwakeVarListKashi5Torihiki;
		/** 仕訳パターン変数List(貸方5・プロジェクト) */
		List<GMap> shiwakeVarListKashi5Project;
		/** 仕訳パターン変数List(貸方5・セグメント) */
		List<GMap> shiwakeVarListKashi5Segment;
		/** 仕訳パターン変数List(貸方5・UF1) */
		List<GMap> shiwakeVarListKashi5Uf1;
		/** 仕訳パターン変数List(貸方5・UF2) */
		List<GMap> shiwakeVarListKashi5Uf2;
		/** 仕訳パターン変数List(貸方5・UF3) */
		List<GMap> shiwakeVarListKashi5Uf3;
		/** 仕訳パターン変数List(貸方5・UF4) */
		List<GMap> shiwakeVarListKashi5Uf4;
		/** 仕訳パターン変数List(貸方5・UF5) */
		List<GMap> shiwakeVarListKashi5Uf5;
		/** 仕訳パターン変数List(貸方5・UF6) */
		List<GMap> shiwakeVarListKashi5Uf6;
		/** 仕訳パターン変数List(貸方5・UF7) */
		List<GMap> shiwakeVarListKashi5Uf7;
		/** 仕訳パターン変数List(貸方5・UF8) */
		List<GMap> shiwakeVarListKashi5Uf8;
		/** 仕訳パターン変数List(貸方5・UF9) */
		List<GMap> shiwakeVarListKashi5Uf9;
		/** 仕訳パターン変数List(貸方5・UF10) */
		List<GMap> shiwakeVarListKashi5Uf10;
		
		/** デフォルト表示 */
		boolean defaultHyouji;
		/** 交際費表示 */
		boolean kousaihiHyouji;
		/** 掛け表示 */
		boolean kakeHyouji;
		/** 社員コード連携表示 */
		boolean shainCdRenkeiHyouji;
		/** 財務枝番コード連携表示 */
		boolean zaimuEdabanRenkeiHyouji;
		
		/** 借方　負担部門　入力可否 */
		boolean kariBumon;
		/** 借方　科目　入力可否 */
		boolean kariKamoku;
		/** 借方　科目枝番　入力可否 */
		boolean kariKamokuEda;
		/** 借方　取引　入力可否 */
		boolean kariTorihiki;
		/** 借方　プロジェクト　入力可否 */
		boolean kariProject;
		/** 借方　セグメント　入力可否 */
		boolean kariSegment;
		/** 借方　UF1　入力可否 */
		boolean kariUf1;
		/** 借方　UF2　入力可否 */
		boolean kariUf2;
		/** 借方　UF3　入力可否 */
		boolean kariUf3;
		/** 借方　UF4　入力可否 */
		boolean kariUf4;
		/** 借方　UF5　入力可否 */
		boolean kariUf5;
		/** 借方　UF6　入力可否 */
		boolean kariUf6;
		/** 借方　UF7　入力可否 */
		boolean kariUf7;
		/** 借方　UF8　入力可否 */
		boolean kariUf8;
		/** 借方　UF9　入力可否 */
		boolean kariUf9;
		/** 借方　UF10　入力可否 */
		boolean kariUf10;
		/** 借方　UFKotei1　入力可否 */
		boolean kariUfKotei1;
		/** 借方　UFKotei2　入力可否 */
		boolean kariUfKotei2;
		/** 借方　UFKotei3　入力可否 */
		boolean kariUfKotei3;
		/** 借方　UFKotei4　入力可否 */
		boolean kariUfKotei4;
		/** 借方　UFKotei5　入力可否 */
		boolean kariUfKotei5;
		/** 借方　UFKotei6　入力可否 */
		boolean kariUfKotei6;
		/** 借方　UFKotei7　入力可否 */
		boolean kariUfKotei7;
		/** 借方　UFKotei8　入力可否 */
		boolean kariUfKotei8;
		/** 借方　UFKotei9　入力可否 */
		boolean kariUfKotei9;
		/** 借方　UFKotei10　入力可否 */
		boolean kariUfKotei10;
		/** 借方　課税区分　入力可否 */
		boolean kariKazeiKbn;
		/** 借方　消費税率　入力可否 */
		boolean kariZeiritsu;
		/** 借方　説明 */
		boolean kariSetsumei;
		/** 借方　分離区分　入力可否 */
		boolean kariBunriKbn;
		/** 借方　仕入区分　入力可否 */
		boolean kariShiireKbn;

		/** 貸方１　負担部門　入力可否 */
		boolean kashi1Bumon;
		/** 貸方１　科目　入力可否 */
		boolean kashi1Kamoku;
		/** 貸方１　科目枝番　入力可否 */
		boolean kashi1KamokuEda;
		/** 貸方１　取引　入力可否 */
		boolean kashi1Torihiki;
		/** 貸方１　プロジェクト　入力可否 */
		boolean kashi1Project;
		/** 貸方１　セグメント　入力可否 */
		boolean kashi1Segment;
		/** 貸方１　UF1　入力可否 */
		boolean kashi1Uf1;
		/** 貸方１　UF2　入力可否 */
		boolean kashi1Uf2;
		/** 貸方１　UF3　入力可否 */
		boolean kashi1Uf3;
		/** 貸方１　UF4　入力可否 */
		boolean kashi1Uf4;
		/** 貸方１　UF5　入力可否 */
		boolean kashi1Uf5;
		/** 貸方１　UF6　入力可否 */
		boolean kashi1Uf6;
		/** 貸方１　UF7　入力可否 */
		boolean kashi1Uf7;
		/** 貸方１　UF8　入力可否 */
		boolean kashi1Uf8;
		/** 貸方１　UF9　入力可否 */
		boolean kashi1Uf9;
		/** 貸方１　UF10　入力可否 */
		boolean kashi1Uf10;
		/** 貸方１　UFKotei1　入力可否 */
		boolean kashi1UfKotei1;
		/** 貸方１　UFKotei2　入力可否 */
		boolean kashi1UfKotei2;
		/** 貸方１　UFKotei3　入力可否 */
		boolean kashi1UfKotei3;
		/** 貸方１　UFKotei4　入力可否 */
		boolean kashi1UfKotei4;
		/** 貸方１　UFKotei5　入力可否 */
		boolean kashi1UfKotei5;
		/** 貸方１　UFKotei6　入力可否 */
		boolean kashi1UfKotei6;
		/** 貸方１　UFKotei7　入力可否 */
		boolean kashi1UfKotei7;
		/** 貸方１　UFKotei8　入力可否 */
		boolean kashi1UfKotei8;
		/** 貸方１　UFKotei9　入力可否 */
		boolean kashi1UfKotei9;
		/** 貸方１　UFKotei10　入力可否 */
		boolean kashi1UfKotei10;
		/** 貸方１　課税区分　入力可否 */
		boolean kashi1KazeiKbn;
		/** 貸方１　説明 */
		boolean kashi1Setsumei;
		/** 貸方1　分離区分　入力可否 */
		boolean kashi1BunriKbn;
		/** 貸方1　仕入区分　入力可否 */
		boolean kashi1ShiireKbn;

		/** 貸方２　負担部門　入力可否 */
		boolean kashi2Bumon;
		/** 貸方２　科目　入力可否 */
		boolean kashi2Kamoku;
		/** 貸方２　科目枝番　入力可否 */
		boolean kashi2KamokuEda;
		/** 貸方２　取引　入力可否 */
		boolean kashi2Torihiki;
		/** 貸方２　プロジェクト　入力可否 */
		boolean kashi2Project;
		/** 貸方２　セグメント　入力可否 */
		boolean kashi2Segment;
		/** 貸方２　UF1　入力可否 */
		boolean kashi2Uf1;
		/** 貸方２　UF2　入力可否 */
		boolean kashi2Uf2;
		/** 貸方２　UF3　入力可否 */
		boolean kashi2Uf3;
		/** 貸方２　UF4　入力可否 */
		boolean kashi2Uf4;
		/** 貸方２　UF5　入力可否 */
		boolean kashi2Uf5;
		/** 貸方２　UF6　入力可否 */
		boolean kashi2Uf6;
		/** 貸方２　UF7　入力可否 */
		boolean kashi2Uf7;
		/** 貸方２　UF8　入力可否 */
		boolean kashi2Uf8;
		/** 貸方２　UF9　入力可否 */
		boolean kashi2Uf9;
		/** 貸方２　UF10　入力可否 */
		boolean kashi2Uf10;
		/** 貸方２　UFKotei1　入力可否 */
		boolean kashi2UfKotei1;
		/** 貸方２　UFKotei2　入力可否 */
		boolean kashi2UfKotei2;
		/** 貸方２　UFKotei3　入力可否 */
		boolean kashi2UfKotei3;
		/** 貸方２　UFKotei4　入力可否 */
		boolean kashi2UfKotei4;
		/** 貸方２　UFKotei5　入力可否 */
		boolean kashi2UfKotei5;
		/** 貸方２　UFKotei6　入力可否 */
		boolean kashi2UfKotei6;
		/** 貸方２　UFKotei7　入力可否 */
		boolean kashi2UfKotei7;
		/** 貸方２　UFKotei8　入力可否 */
		boolean kashi2UfKotei8;
		/** 貸方２　UFKotei9　入力可否 */
		boolean kashi2UfKotei9;
		/** 貸方２　UFKotei10　入力可否 */
		boolean kashi2UfKotei10;
		/** 貸方２　課税区分　入力可否 */
		boolean kashi2KazeiKbn;
		/** 貸方２　説明 */
		boolean kashi2Setsumei;
		/** 貸方2　分離区分　入力可否 */
		boolean kashi2BunriKbn;
		/** 貸方2　仕入区分　入力可否 */
		boolean kashi2ShiireKbn;


		/** 貸方３　負担部門　入力可否 */
		boolean kashi3Bumon;
		/** 貸方３　科目　入力可否 */
		boolean kashi3Kamoku;
		/** 貸方３　科目枝番　入力可否 */
		boolean kashi3KamokuEda;
		/** 貸方３　取引　入力可否 */
		boolean kashi3Torihiki;
		/** 貸方３　プロジェクト　入力可否 */
		boolean kashi3Project;
		/** 貸方３　セグメント　入力可否 */
		boolean kashi3Segment;
		/** 貸方３　UF1　入力可否 */
		boolean kashi3Uf1;
		/** 貸方３　UF2　入力可否 */
		boolean kashi3Uf2;
		/** 貸方３　UF3　入力可否 */
		boolean kashi3Uf3;
		/** 貸方３　UF4　入力可否 */
		boolean kashi3Uf4;
		/** 貸方３　UF5　入力可否 */
		boolean kashi3Uf5;
		/** 貸方３　UF6　入力可否 */
		boolean kashi3Uf6;
		/** 貸方３　UF7　入力可否 */
		boolean kashi3Uf7;
		/** 貸方３　UF8　入力可否 */
		boolean kashi3Uf8;
		/** 貸方３　UF9　入力可否 */
		boolean kashi3Uf9;
		/** 貸方３　UF10　入力可否 */
		boolean kashi3Uf10;
		/** 貸方３　UFKotei1　入力可否 */
		boolean kashi3UfKotei1;
		/** 貸方３　UFKotei2　入力可否 */
		boolean kashi3UfKotei2;
		/** 貸方３　UFKotei3　入力可否 */
		boolean kashi3UfKotei3;
		/** 貸方３　UFKotei4　入力可否 */
		boolean kashi3UfKotei4;
		/** 貸方３　UFKotei5　入力可否 */
		boolean kashi3UfKotei5;
		/** 貸方３　UFKotei6　入力可否 */
		boolean kashi3UfKotei6;
		/** 貸方３　UFKotei7　入力可否 */
		boolean kashi3UfKotei7;
		/** 貸方３　UFKotei8　入力可否 */
		boolean kashi3UfKotei8;
		/** 貸方３　UFKotei9　入力可否 */
		boolean kashi3UfKotei9;
		/** 貸方３　UFKotei10　入力可否 */
		boolean kashi3UfKotei10;
		/** 貸方３　課税区分　入力可否 */
		boolean kashi3KazeiKbn;
		/** 貸方３　説明 */
		boolean kashi3Setsumei;
		/** 貸方3　分離区分　入力可否 */
		boolean kashi3BunriKbn;
		/** 貸方3　仕入区分　入力可否 */
		boolean kashi3ShiireKbn;

		/** 貸方４　負担部門　入力可否 */
		boolean kashi4Bumon;
		/** 貸方４　科目　入力可否 */
		boolean kashi4Kamoku;
		/** 貸方４　科目枝番　入力可否 */
		boolean kashi4KamokuEda;
		/** 貸方４　取引　入力可否 */
		boolean kashi4Torihiki;
		/** 貸方４　プロジェクト　入力可否 */
		boolean kashi4Project;
		/** 貸方４　セグメント　入力可否 */
		boolean kashi4Segment;
		/** 貸方４　UF1　入力可否 */
		boolean kashi4Uf1;
		/** 貸方４　UF2　入力可否 */
		boolean kashi4Uf2;
		/** 貸方４　UF3　入力可否 */
		boolean kashi4Uf3;
		/** 貸方４　UF4　入力可否 */
		boolean kashi4Uf4;
		/** 貸方４　UF5　入力可否 */
		boolean kashi4Uf5;
		/** 貸方４　UF6　入力可否 */
		boolean kashi4Uf6;
		/** 貸方４　UF7　入力可否 */
		boolean kashi4Uf7;
		/** 貸方４　UF8　入力可否 */
		boolean kashi4Uf8;
		/** 貸方４　UF9　入力可否 */
		boolean kashi4Uf9;
		/** 貸方４　UF10　入力可否 */
		boolean kashi4Uf10;
		/** 貸方４　UFKotei1　入力可否 */
		boolean kashi4UfKotei1;
		/** 貸方４　UFKotei2　入力可否 */
		boolean kashi4UfKotei2;
		/** 貸方４　UFKotei3　入力可否 */
		boolean kashi4UfKotei3;
		/** 貸方４　UFKotei4　入力可否 */
		boolean kashi4UfKotei4;
		/** 貸方４　UFKotei5　入力可否 */
		boolean kashi4UfKotei5;
		/** 貸方４　UFKotei6　入力可否 */
		boolean kashi4UfKotei6;
		/** 貸方４　UFKotei7　入力可否 */
		boolean kashi4UfKotei7;
		/** 貸方４　UFKotei8　入力可否 */
		boolean kashi4UfKotei8;
		/** 貸方４　UFKotei9　入力可否 */
		boolean kashi4UfKotei9;
		/** 貸方４　UFKotei10　入力可否 */
		boolean kashi4UfKotei10;
		/** 貸方４　課税区分　入力可否 */
		boolean kashi4KazeiKbn;
		/** 貸方４　説明 */
		boolean kashi4Setsumei;
		/** 貸方4　分離区分　入力可否 */
		boolean kashi4BunriKbn;
		/** 貸方4　仕入区分　入力可否 */
		boolean kashi4ShiireKbn;

		/** 貸方５　負担部門　入力可否 */
		boolean kashi5Bumon;
		/** 貸方５　科目　入力可否 */
		boolean kashi5Kamoku;
		/** 貸方５　科目枝番　入力可否 */
		boolean kashi5KamokuEda;
		/** 貸方５　取引　入力可否 */
		boolean kashi5Torihiki;
		/** 貸方５　プロジェクト　入力可否 */
		boolean kashi5Project;
		/** 貸方５　セグメント　入力可否 */
		boolean kashi5Segment;
		/** 貸方５　UF1　入力可否 */
		boolean kashi5Uf1;
		/** 貸方５　UF2　入力可否 */
		boolean kashi5Uf2;
		/** 貸方５　UF3　入力可否 */
		boolean kashi5Uf3;
		/** 貸方５　UF4　入力可否 */
		boolean kashi5Uf4;
		/** 貸方５　UF5　入力可否 */
		boolean kashi5Uf5;
		/** 貸方５　UF6　入力可否 */
		boolean kashi5Uf6;
		/** 貸方５　UF7　入力可否 */
		boolean kashi5Uf7;
		/** 貸方５　UF8　入力可否 */
		boolean kashi5Uf8;
		/** 貸方５　UF9　入力可否 */
		boolean kashi5Uf9;
		/** 貸方５　UF10　入力可否 */
		boolean kashi5Uf10;
		/** 貸方５　UFKotei1　入力可否 */
		boolean kashi5UfKotei1;
		/** 貸方５　UFKotei2　入力可否 */
		boolean kashi5UfKotei2;
		/** 貸方５　UFKotei3　入力可否 */
		boolean kashi5UfKotei3;
		/** 貸方５　UFKotei4　入力可否 */
		boolean kashi5UfKotei4;
		/** 貸方５　UFKotei5　入力可否 */
		boolean kashi5UfKotei5;
		/** 貸方５　UFKotei6　入力可否 */
		boolean kashi5UfKotei6;
		/** 貸方５　UFKotei7　入力可否 */
		boolean kashi5UfKotei7;
		/** 貸方５　UFKotei8　入力可否 */
		boolean kashi5UfKotei8;
		/** 貸方５　UFKotei9　入力可否 */
		boolean kashi5UfKotei9;
		/** 貸方５　UFKotei10　入力可否 */
		boolean kashi5UfKotei10;
		/** 貸方５　課税区分　入力可否 */
		boolean kashi5KazeiKbn;
		/** 貸方５　説明 */
		boolean kashi5Setsumei;
		/** 貸方5　分離区分　入力可否 */
		boolean kashi5BunriKbn;
		/** 貸方5　仕入区分　入力可否 */
		boolean kashi5ShiireKbn;


		/** (借方)説明 */
		String kariSetsumeiStr;
		/** (貸方1)説明 */
		String kashiSetsumei1Str;
		/** (貸方2)説明 */
		String kashiSetsumei2Str;
		/** (貸方3)説明 */
		String kashiSetsumei3Str;
		/** (貸方4)説明 */
		String kashiSetsumei4Str;
		/** (貸方5)説明 */
		String kashiSetsumei5Str;
	}
	
	/**
	 * カスタマイズ用、仕訳作成判定
	 * @param denpyouKbn 伝票区分
	 * @param shiwakeSakusei 仕訳作成区分
	 * @return 仕訳作成区分
	 */
	protected String shiwakeSakusei_Ext(String denpyouKbn, String shiwakeSakusei) {
		return shiwakeSakusei;
	}
	
	/**
	 * UF欄をブランクにする
	 * @param no UFの番号(1～10)
	 * @param denpyouKbn 伝票区分
	 * @param userId ログインユーザーID
	 */
	@Deprecated
	public void clearUf(Integer no, String denpyouKbn, String userId){
		String sql =	"UPDATE shiwake_pattern_master SET "
					+ "  kari_uf:NO_cd='', kashi_uf:NO_cd1='', kashi_uf:NO_cd2='', kashi_uf:NO_cd3='', kashi_uf:NO_cd4='', kashi_uf:NO_cd5='', "
					+ "  koushin_user_id=?, koushin_time = current_timestamp ";
		sql = sql.replaceAll(":NO", Integer.toString(no));
		if(!isEmpty(denpyouKbn)){
			sql = sql + "WHERE denpyou_kbn='" + denpyouKbn + "' ";
		}
		connection.update(sql, userId);
	}
	

	/**
	 * UF欄(固定値)をブランクにする
	 * @param no UFの番号(1～10)
	 * @param denpyouKbn 伝票区分
	 * @param userId ログインユーザーID
	 */
	@Deprecated
	public void clearUfKotei(Integer no, String denpyouKbn, String userId){
		String sql =	"UPDATE shiwake_pattern_master SET "
					+ "  kari_uf_kotei:NO_cd='', kashi_uf_kotei:NO_cd1='', kashi_uf_kotei:NO_cd2='', kashi_uf_kotei:NO_cd3='', kashi_uf_kotei:NO_cd4='', kashi_uf_kotei:NO_cd5='', "
					+ "  koushin_user_id=?, koushin_time = current_timestamp ";
		sql = sql.replaceAll(":NO", Integer.toString(no));
		if(!isEmpty(denpyouKbn)){
			sql = sql + "WHERE denpyou_kbn='" + denpyouKbn + "' ";
		}
		connection.update(sql, userId);
	}
	
	/**
	 * 課税区分が課税グループに属するか判定
	 * @param kazeiKbn 課税区分
	 * @return boolean 課税グループが税込みでない場合はtrue
	 */
	public boolean isNotKazeiGroup(String kazeiKbn) {
		// 課税区分(kazei_kbn)のレコードの課税区分グループ（オプション１）を取得。
		NaibuCdSettingAbstractDao naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		NaibuCdSetting kazeiKbnGroup = naibuCdSettingDao.find("kazei_kbn", kazeiKbn);
		//支払依頼申請書の場合はkazei_kbn_shiharaiiraiの課税区分が入力されている場合がある
		if(kazeiKbnGroup == null) {
			kazeiKbnGroup = naibuCdSettingDao.find("kazei_kbn_shiharaiirai", kazeiKbn);
		}
		
		// 課税区分グループが税込（"1"）の時、false
		return kazeiKbnGroup == null || !KAZEI_KBN_GROUP.ZEIKOMI.equals(kazeiKbnGroup.option1);
		
	}
	
	/**
	 * denpyou_shubetsu_ichiran.shiiresaki_flgが1の場合、取引先が仕入先であるかチェック
	 * @param errorList エラーリスト
	 * @param label 取引先コードのエラー文言用名称
	 * @param torihikisakiCd 取引先コード
	 * @param denpyouKbn 伝票区分
	 */
	public void checkShiiresaki(List<String> errorList, String label, String torihikisakiCd, String denpyouKbn) {
		//支払依頼申請は常に一見先取引先を許容する
		if (DENPYOU_KBN.SIHARAIIRAI.equals(denpyouKbn) && setting.ichigenCd().equals(torihikisakiCd))
		{
			return;
		} 
		
		if(! isEmpty(torihikisakiCd)) {
			String denpyouKbn4DenpyouKanri = denpyouKbn;
			if(denpyouKbn4DenpyouKanri.equals("A901")){
				denpyouKbn4DenpyouKanri = "A001";
			}
			GMap denpyouKanri = kihyouNaviLogic.findDenpyouKanri(denpyouKbn4DenpyouKanri);
			boolean shiiresakiFlg = denpyouKanri.get("shiiresaki_flg").equals("1");
			if(shiiresakiFlg) {
				if(masterLg.findTorihikisakiJouhou(torihikisakiCd, true) == null) {
					errorList.add(label + "に仕入先ではない取引先を入力することはできません。");
				}
			}
		}
	}
}
