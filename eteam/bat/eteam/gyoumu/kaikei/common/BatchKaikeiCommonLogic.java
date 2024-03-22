package eteam.gyoumu.kaikei.common;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.FB_STATUS;
import eteam.common.EteamNaibuCodeSetting.KAZEI_KBN;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.RYOHISEISAN_SYUBETSU;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.KamokuMasterAbstractDao;
import eteam.database.abstractdao.KiShouhizeiSettingAbstractDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dto.KiShouhizeiSetting;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.kaikei.ShiwakeDataImportLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;


/**
 * 会計共通処理（バッチ）
 */
public class BatchKaikeiCommonLogic extends EteamAbstractLogic {
	
	/** ユーザー名 */
	static final String USER = "<USER>";
	/** 摘要 */
	static final String TEKIYOU = "<TEKIYOU>";
	/** 使用日 */
	static final String SHIYOUBI = "<SHIYOUBI>";
	/** 精算期間開始日	 */
	static final String SEISANSTART = "<SEISANSTART>";
	/** 精算期間終了日	 */
	static final String SEISANEND = "<SEISANEND>";
	/** 取引名 */
	static final String TORIHIKI = "<TORIHIKI>";
	/** 取引先名 */
	static final String TORIHIKISAKI = "<TORIHIKISAKI>";
	/** 使用者 */
	static final String SHIYOUSHA = "<SHIYOUSHA>";
	/** 交際費人数 */
	static final String KOUSAINUM = "<KOUSAI_NUM>";
	/** 交際費１人あたり金額 */
	static final String KOUSAIGAKU = "<KOUSAI_GAKU>";
	/** 支払先名 */
	static final String SHIHARAISAKI = "<SHIHARAISAKI>";
//ダイショー用
	/** 退去日	 */
	static final String TAIKYOBI = "<TAIKYOBI>";
	/** 入居日	 */
	static final String NYUUKYOBI = "<NYUUKYOBI>";
	/** 出発日	 */
	static final String SHUPPATSUBI = "<SHUPPATSUBI>";
	/** 帰着日	 */
	static final String KICHAKUBI = "<KICHAKUBI>";

	/** マスターカテゴリ */
	MasterKanriCategoryLogic masterLogic; 
	/** 消費税設定Dao */
	KiShouhizeiSettingAbstractDao kiShouhizeiSettingDao;
	/** 科目マスタDao */
	KamokuMasterAbstractDao kamokuMasterDao;
	/** 会計共通ロジック */
	KaikeiCommonLogic commonLogic;
	/** 仕訳データインポートロジック */
	ShiwakeDataImportLogic shiwakeDataImportLogic;

	/**
	 * 初期化
	 * @param _connection コネクション
	 */
	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, _connection);
		kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		commonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		shiwakeDataImportLogic = EteamContainer.getComponent(ShiwakeDataImportLogic.class, connection);
	}
	
	/**
	 * 起票者情報の取得
	 * @param denpyouId 伝票ID
	 * @return 起票者情報
	 */
	public GMap findKihyouUser(String denpyouId) {
		final String sql =
				"SELECT "
			+ "  s.user_id "
			+ "  ,s.user_full_name "
			+ "  ,u.shain_no "
			+ "  ,u.houjin_card_shikibetsuyou_num "
			+ "FROM shounin_route s "
			+ "LEFT OUTER JOIN user_info u ON "
			+ "  s.user_id = u.user_id "
			+ "WHERE "
			+ "  s.denpyou_id = ? "
			+ "  AND s.edano = 1 ";
		return connection.find(sql, denpyouId);
	}

	/**
	 * 起票者情報の取得
	 * @param userId 承認ルートのユーザID
	 * @return 起票者情報
	 */
	public GMap findKihyouShainKouza(String userId) {
		final String sql =
				"SELECT "
			+ "  m.shubetsu_cd "
			+ "  ,m.cd_kbn "
			+ "  ,m.kaisha_cd "
			+ "  ,m.kaisha_name_hankana "
			+ "  ,s.moto_kinyuukikan_cd "
			+ "  ,m.moto_kinyuukikan_name_hankana "
			+ "  ,s.moto_kinyuukikan_shiten_cd "
			+ "  ,m.moto_kinyuukikan_shiten_name_hankana "
			+ "  ,s.moto_yokinshubetsu "
			+ "  ,s.moto_kouza_bangou "
			+ "  ,s.saki_kinyuukikan_cd "
			+ "  ,kn.kinyuukikan_name_hankana "
			+ "  ,s.saki_ginkou_shiten_cd "
			+ "  ,kn.shiten_name_hankana "
			+ "  ,s.saki_yokin_shabetsu "
			+ "  ,s.saki_kouza_bangou "
			+ "  ,s.saki_kouza_meigi_kana "
			+ "  ,m.shinki_cd "
			+ "  ,s.shain_no "
			+ "  ,m.furikomi_kbn "
			+ "FROM user_info u "
			+ "INNER JOIN shain_kouza s ON "
			+ "  u.shain_no = s.shain_no "
			+ "INNER JOIN moto_kouza m ON "
			+ "  s.moto_kinyuukikan_cd = m.moto_kinyuukikan_cd "
			+ "  AND s.moto_kinyuukikan_shiten_cd = m.moto_kinyuukikan_shiten_cd "
			+ "  AND s.moto_yokinshubetsu = m.moto_yokinshubetsu "
			+ "  AND s.moto_kouza_bangou = m.moto_kouza_bangou "
			+ "INNER JOIN kinyuukikan kn ON "
			+ "  s.saki_kinyuukikan_cd = kn.kinyuukikan_cd "
			+ "  AND s.saki_ginkou_shiten_cd = kn.kinyuukikan_shiten_cd "
			+ "WHERE "
			+ "  u.user_id = ? ";
		return connection.find(sql, userId);
	}
	
	/**
	 * 仕訳摘要内容決定
	 * 設定を元に、各変数に正しい文字を代入し、文字列作成。
	 * 30文字を超えてるかチェックし、30文字以上の場合makeTKYに変数(文字列)を渡し、
	 * 戻り値を変数(文字列)に上書きし各伝票の仕訳摘要に返す。
	 * 30文字以下の場合、そのまま変数(文字列)を各伝票の仕訳摘要に返す。
	 * @param denpyouKbn        伝票区分
	 * @param shinseiData 伝票内容
	 * @param meisai 呼び出し元伝票明細レコード or 明細使うか否かの引数
	 * @param shiwakeHanteiFlg  仕訳判定フラグ(0:普通の仕訳、1:仮払相殺、2:仮払未使用、3:仮払残金)
	 * @return 仕訳摘要内容
	 */
	public String shiwakeTekiyou(String denpyouKbn, GMap shinseiData, GMap meisai, String shiwakeHanteiFlg){
		if(denpyouKbn.equals("A901")){
			denpyouKbn = DENPYOU_KBN.KEIHI_TATEKAE_SEISAN;
		}
		
		// ---------------------------------------
		//会社設定 摘要情報取得
		// ---------------------------------------
		String stReplace   = "";
		//Map結合 hontai = shinseiData + meisai
		Map<String, Object>hontai = new LinkedHashMap<>();
		hontai.putAll(shinseiData);
		if(meisai!=null){
			hontai.putAll(meisai);
		}
		
		BatchKaikeiCommonLogic common;
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		GMap kihyouUser = common.findKihyouUser((String)hontai.get("denpyou_id"));

		String shiwakeTekiyou = readShiwakeTekiyouSetting(denpyouKbn, shiwakeHanteiFlg);

		//1つづつ置換
		//<USER>置換
		if(kihyouUser.get("user_full_name")!=null){
			stReplace = (String)kihyouUser.get("user_full_name");
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(USER, stReplace);
		
		//<SHIYOUBI>置換
		if(hontai.get("shiyoubi")!=null){
			String DATE_PATTERN ="MM/dd";
			stReplace = new SimpleDateFormat(DATE_PATTERN).format((Date) hontai.get("shiyoubi"));
		}else{
			stReplace   = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(SHIYOUBI, stReplace);
		
		//<SEISANSTART>置換
		if(hontai.get("seisankikan_from")!=null){
			String DATE_PATTERN ="MM/dd";
			stReplace = new SimpleDateFormat(DATE_PATTERN).format((Date)hontai.get("seisankikan_from"));
		}else{
			stReplace   = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(SEISANSTART, stReplace);
		
		//<SEISANEND>置換
		if(hontai.get("seisankikan_to")!=null){
			String DATE_PATTERN ="MM/dd";
			stReplace = new SimpleDateFormat(DATE_PATTERN).format((Date)hontai.get("seisankikan_to"));
		}else{
			stReplace   = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(SEISANEND, stReplace);
		
		//<TAIKYOBI>置換
		if(hontai.get("taikyobi")!=null){
			String DATE_PATTERN ="MM/dd";
			stReplace = new SimpleDateFormat(DATE_PATTERN).format((Date)hontai.get("taikyobi"));
		}else{
			stReplace   = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(TAIKYOBI, stReplace);
		
		//<NYUUKYOBI>置換
		if(hontai.get("nyuukyobi")!=null){
			String DATE_PATTERN ="MM/dd";
			stReplace = new SimpleDateFormat(DATE_PATTERN).format((Date)hontai.get("nyuukyobi"));
		}else{
			stReplace   = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(NYUUKYOBI, stReplace);
		
		//<SHUPPATSUBI>置換
		if(hontai.get("shuppatsubi")!=null){
			String DATE_PATTERN ="MM/dd";
			stReplace = new SimpleDateFormat(DATE_PATTERN).format((Date)hontai.get("shuppatsubi"));
		}else{
			stReplace   = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(SHUPPATSUBI, stReplace);
		
		//<KICHAKUBI>置換
		if(hontai.get("kichakubi")!=null){
			String DATE_PATTERN ="MM/dd";
			stReplace = new SimpleDateFormat(DATE_PATTERN).format((Date)hontai.get("kichakubi"));
		}else{
			stReplace   = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(KICHAKUBI, stReplace);
		
		//<TORIHIKI>置換
		if(hontai.get("torihiki_name")!=null){
			if(!denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN)){
				stReplace = (String)hontai.get("torihiki_name");
			}else{
				//海外旅費精算の場合海外フラグを参照
				if( hontai.get("kaigaiTekiyouFlg")!=null ){
					stReplace = (String)hontai.get("kaigai_torihiki_name");
				}else{
					stReplace = (String)hontai.get("torihiki_name");
				}
			}
		}else{
			stReplace   = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(TORIHIKI, stReplace);
		
		//<TORIHIKISAKI>置換
		if(hontai.get("torihikisaki_name_ryakushiki")!=null){
			stReplace = (String)hontai.get("torihikisaki_name_ryakushiki");
		}else{
			stReplace   = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(TORIHIKISAKI, stReplace);
		
		//<SHIYOUSHA>置換
		if(hontai.get("shain_no")!=null){
			stReplace = (String)hontai.get("user_sei") + "　" + (String)hontai.get("user_mei");
		}else if(kihyouUser.get("user_full_name")!=null){
			stReplace = (String)kihyouUser.get("user_full_name");
		}else{
			stReplace   = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(SHIYOUSHA, stReplace);
		
		//<TEKIYOU>置換
		if(hontai.get("tekiyou")!=null){
			if(!denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN)){
				stReplace = (String)hontai.get("tekiyou");
			}else{
				//海外旅費精算の場合海外フラグを参照
				if( hontai.get("kaigaiTekiyouFlg")!=null ){
					stReplace = (String)hontai.get("kaigai_tekiyou");
				}else{
					stReplace = (String)hontai.get("tekiyou");
				}
			}
		}else{
			stReplace   = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(TEKIYOU, stReplace);
		
		//<KOUSAI_NUM>置換
		if(hontai.get("kousaihi_ninzuu")!=null){
			stReplace = ((Integer)hontai.get("kousaihi_ninzuu")).toString() + "名";
		}else {
			stReplace = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(KOUSAINUM, stReplace);
		
		//<KOUSAI_GAKU>置換
		if(hontai.get("kousaihi_hitori_kingaku")!=null){
			stReplace = "一人当たり" + ( new DecimalFormat("#,###").format(hontai.get("kousaihi_hitori_kingaku")) ) + "円";
		}else {
			stReplace = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(KOUSAIGAKU, stReplace);
		
		//<SHIHARAISAKI>置換
		if(hontai.get("shiharaisaki_name")!=null){
			stReplace = hontai.get("shiharaisaki_name").toString();
		}else{
			stReplace = "";
		}
		shiwakeTekiyou = shiwakeTekiyou.replace(SHIHARAISAKI, stReplace);
		
		return shiwakeTekiyou;
	}
	
	/**
	 * 会社設定から仕訳摘要の設定文言を取得	
	 * @param denpyouKbn 伝票区分
	 * @param shiwakeHanteiFlg 仕訳判定フラグ	
	 * @return 仕訳の摘要設定文言
	 */
	protected String readShiwakeTekiyouSetting(String denpyouKbn, String shiwakeHanteiFlg) {
		String shiwakeTekiyou = "";
		if(shiwakeHanteiFlg.equals("0")){
			switch(denpyouKbn){
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA001();
						 								break;
			case DENPYOU_KBN.KARIBARAI_SHINSEI   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA002();
														break;
			case DENPYOU_KBN.SEIKYUUSHO_BARAI   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA003();
														break;
			case DENPYOU_KBN.RYOHI_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA004();
														break;
			case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI  : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA005();
														break;
			case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA006();
														break;
			case DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU  : shiwakeTekiyou =  TEKIYOU;
														break;
			case DENPYOU_KBN.FURIKAE_DENPYOU   : shiwakeTekiyou =  TEKIYOU;
														break;
			case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA009();
														break;
			case DENPYOU_KBN.KOUTSUUHI_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA010();
														break;
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA011();
														break;
			case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI  : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA012();
														break;
			case DENPYOU_KBN.SIHARAIIRAI   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA013();
														break;
			}
		}else if(shiwakeHanteiFlg.equals("1")){
			switch(denpyouKbn){
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA0011();
														break;
			case DENPYOU_KBN.RYOHI_SEISAN   : shiwakeTekiyou  = EteamSettingInfo.getSettingInfo(Key.SHIWAKE_TEKIYOU_NAIYOU_A004_1);
														break;
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA0111();
														break;
			}
		}else if(shiwakeHanteiFlg.equals("2")){
			switch(denpyouKbn){
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA0012();
														break;
			case DENPYOU_KBN.RYOHI_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA0042();
														break;
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA0112();
														break;
			}
		}else if(shiwakeHanteiFlg.equals("3")){
			switch(denpyouKbn){
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA0013();
														break;
			case DENPYOU_KBN.RYOHI_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA0043();
														break;
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN   : shiwakeTekiyou  = setting.shiwakeTekiyouNaiyouA0113();
														break;
			}
		}
		return shiwakeTekiyou;
	}

	/**
	 * （オープン２１）摘要 文字列の作成
	 * 30バイト以内になるように編集する。
	 * @param tekiyou 伝票画面上に入力された摘要
	 * @return 編集後の摘要
	 */
	public String cutTekiyou(String tekiyou) {
		String tky = null;
		int tekiyouLength= commonLogic.tekiyouCheck(Open21Env.getVersion().toString());
		//60→(de3)60 / (SIAS)120
		if(tekiyouLength < EteamCommon.getByteLength(tekiyou)){
			tky = EteamCommon.byteCut(tekiyou, tekiyouLength);
		} else {
			tky = tekiyou;
		}

		return tky;
	}
	
	/**
	 * オープン２１へのインポート用に課税区分の変換（頭0除く）を行う。ただし、"000"は"0"とする。
	 * @param kazeiKbn 課税区分(3桁)
	 * @return 頭0を除いた課税区分
	 */
	public String convKazekKbn(String kazeiKbn) {
		return "000".equals(kazeiKbn) ? "0" : kazeiKbn.replaceFirst("^0+", "");
	}

	/**
	 * 仕訳パターンマスター情報の取得
	 * @param denpyouKbn 伝票区分
	 * @param shiwakeEdano 仕訳枝番号
	 * @return 仕訳パターンマスター情報
	 */
	public GMap findShiwakePatternInfo(String denpyouKbn, String shiwakeEdano) {
		final String sql =" SELECT * FROM shiwake_pattern_master WHERE denpyou_kbn = ? AND shiwake_edano = ? AND delete_flg = '0'";
		return connection.find(sql, denpyouKbn, Integer.parseInt(shiwakeEdano));
	}

	/**
	 * 仮払情報の取得
	 * @param denpyouId 伝票ID
	 * @return 仮払情報
	 */
	public GMap findKaribaraiInfo(String denpyouId) {
		final String sql = "SELECT M.denpyou_kbn, M.daihyou_futan_bumon_cd, L.* "
				 + "      ,R.kashi_kamoku_cd2 AS kashi_kamoku_cd2"
				 + "      ,R.kashi_kamoku_edaban_cd2 AS kashi_kamoku_edaban_cd2"
				 + "      ,R.kashi_futan_bumon_cd2 AS kashi_futan_bumon_cd2"
				 + "      ,R.kashi_kazei_kbn2 AS kashi_kazei_kbn2"
				 + "  FROM karibarai L "
				 + " INNER JOIN denpyou M "
				 + "    ON L.denpyou_id = M.denpyou_id "
				 + "  LEFT OUTER JOIN shiwake_pattern_master R "
				 + "    ON M.denpyou_kbn   = R.denpyou_kbn "
				 + "   AND L.shiwake_edano = R.shiwake_edano "
				 + " WHERE L.denpyou_id = ?";
		return connection.find(sql, denpyouId);
	}

	/**
	 * 旅費仮払テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	public GMap findRyouhiKaribarai(String denpyouId){
		final String sql = "SELECT M.denpyou_kbn, M.daihyou_futan_bumon_cd, L.* "
				 + "      ,R.kashi_kamoku_cd2 AS kashi_kamoku_cd2 "
				 + "      ,R.kashi_kamoku_edaban_cd2 AS kashi_kamoku_edaban_cd2 "
				 + "      ,R.kashi_futan_bumon_cd2 AS kashi_futan_bumon_cd2"
				 + "      ,R.kashi_kazei_kbn2 AS kashi_kazei_kbn2"
				 + "  FROM ryohi_karibarai L "
				 + " INNER JOIN denpyou M "
				 + "    ON L.denpyou_id = M.denpyou_id "
				 + "  LEFT OUTER JOIN shiwake_pattern_master R "
				 + "    ON M.denpyou_kbn   = R.denpyou_kbn "
				 + "   AND L.shiwake_edano = R.shiwake_edano "
				 + " WHERE L.denpyou_id = ? ";
		return connection.find(sql, denpyouId);
	}

	/**
	 * 海外旅費仮払テーブル検索する。データがなければnull。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
	public GMap findKaigaiRyouhiKaribarai(String denpyouId){
		final String sql = "SELECT M.denpyou_kbn, M.daihyou_futan_bumon_cd, L.* "
				 + "      ,R.kashi_kamoku_cd2 AS kashi_kamoku_cd2 "
				 + "      ,R.kashi_kamoku_edaban_cd2 AS kashi_kamoku_edaban_cd2 "
				 + "      ,R.kashi_futan_bumon_cd2 AS kashi_futan_bumon_cd2"
				 + "      ,R.kashi_kazei_kbn2 AS kashi_kazei_kbn2"
				 + "  FROM kaigai_ryohi_karibarai L "
				 + " INNER JOIN denpyou M "
				 + "    ON L.denpyou_id = M.denpyou_id "
				 + "  LEFT OUTER JOIN shiwake_pattern_master R "
				 + "    ON M.denpyou_kbn   = R.denpyou_kbn "
				 + "   AND L.shiwake_edano = R.shiwake_edano "
				 + " WHERE L.denpyou_id = ? ";
		return connection.find(sql, denpyouId);
	}
	
	/**
	 * 分離区分の値の取得
	 * @param kazeiKbn 課税区分
	 * @return String 分離区分
	 */
	public String getBUNRI(String kazeiKbn) {
/* 当初分離区分課税区分によると考えていたが、未設定で仕訳作成後にOPEN21で設定する運用となった。
		String bunri = "0";
		
		// 課税区分(kazei_kbn)のレコードの課税区分グループ（オプション１）を取得。
		SystemKanriCategoryLogic systemLg = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		GMap kazeiKbnGroup = systemLg.findNaibuCdSetting("kazei_kbn", kazeiKbn);

		// 課税区分グループが税込（"1"）の時、"1"を設定
		if (kazeiKbnGroup != null && KAZEI_KBN_GROUP.ZEIKOMI.equals(kazeiKbnGroup.get("option1"))) {
			bunri = "1";
		}
		
		return bunri;
*/
		return "";
	}

	/**
	 * 伝票番号(シリアル番号)を+1する
	 * @param serialNo シリアル番号(8桁)
	 * @return シリアル番号+1
	 */
	public String serialNo(Object serialNo) {
		return (String.format("%1$08d",(Integer.parseInt(serialNo.toString()))));
	}

	/**
	 * 伝票番号(シリアル番号)を+1する
	 * @param serialNo シリアル番号(8桁)
	 * @return シリアル番号+1
	 */
	public String serialNoPlus(long serialNo) {
		return (String.format("%1$08d",(serialNo + 1)));
	}

	/**
	 * 伝票番号(シリアル番号)を+1する
	 * @param serialNo シリアル番号(8桁)
	 * @return シリアル番号+1
	 */
	public String serialNoPlus(Object serialNo) {
		return serialNoPlus(Integer.parseInt(serialNo.toString()));
	}
	
	/**
	 * 科目に紐づく分離区分の判定
	 * @param kamokuCd 科目コード
	 * @return 分離区分が0だったらfalse/それ以外なら(1のはず)true
	 */
	public boolean  judgeBunriKbn(String kamokuCd) {
		MasterKanriCategoryLogic masterCommonLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		GMap kamokuMap = masterCommonLogic.findKamokuMaster(kamokuCd);
		return ! (0 == (Integer)kamokuMap.get("bunri_kbn"));
	}
	
	/**
	 * 課税区分が「税抜」「税込」の課税区分グループかの判定し、
	 * 「税抜」「税込」なら税率、それ以外ならnullを返却
	 * @param kazeiKbn 課税区分
	 * @param zeiritsu 税率
	 * @return true :「税抜」「税込」 false:それ以外
	 */
	public BigDecimal judgeShiwakeZeiritsu(String kazeiKbn, BigDecimal zeiritsu) {
		
		BigDecimal rtn = null;
		if(isZeinukiOrZeikomiGroup(kazeiKbn)) {
			rtn = zeiritsu;
		}
		
		return rtn;
	}

	/**
	 * 課税区分が「税抜」「税込」の課税区分グループかの判定し、
	 * 「税抜」「税込」なら軽減税率区分、それ以外ならブランクを返却
	 * @param kazeiKbn 課税区分
	 * @param KeigenZeiritsuKbn 軽減税率区分
	 * @return true :「税抜」「税込」 false:それ以外
	 */
	public String judgeShiwakeZeiritsuKbn(String kazeiKbn, String KeigenZeiritsuKbn) {
		
		String rtn = "";
		if(isZeinukiOrZeikomiGroup(kazeiKbn)) {
			rtn = KeigenZeiritsuKbn;
		}
		
		return rtn;
	}

	/**
	 * judgeShiwakeZeiritsu及びjudgeShiwakeZeiritsuKbnの条件を共通メソッド化
	 * @param kazeiKbn 課税区分
	 * @return 条件
	 */
	protected boolean isZeinukiOrZeikomiGroup(String kazeiKbn) {
		KaikeiCommonLogic kaikeiCommonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		boolean kazeiKbnIsZeinukiGroup = kaikeiCommonLg.kazeiKbnIsZeinukiGroup(kazeiKbn);
		boolean kazeiKbnIsZeikomiGroup = kaikeiCommonLg.kazeiKbnIsZeikomiGroup(kazeiKbn);
		return kazeiKbnIsZeinukiGroup || kazeiKbnIsZeikomiGroup;
	}
	
	/**
	 * FBデータを作成する。
	 * @param denpyouId 伝票ID
	 * @param furikomiBi 振込日
	 * @param kingaku 金額
	 * @param userId ユーザーID
	 * @param shainKouza 社員講座情報
	 * @return 振込日
	 */
	public Date makeFBData(String denpyouId, Date furikomiBi, BigDecimal kingaku, String userId, GMap shainKouza) {
		FBLogic fbLogic = EteamContainer.getComponent(FBLogic.class, connection);

		FBData fbData = new FBData();
		fbData.setDenpyouId(denpyouId); // 伝票ID
		fbData.setUserId(userId); // ユーザーID
		fbData.setFbStatus(FB_STATUS.CHUUSHUTSU); // FB抽出状態
		fbData.setShubetsuCd((String)shainKouza.get("shubetsu_cd")); // 種別コード
		fbData.setCdKbn((String)shainKouza.get("cd_kbn")); // コード区分
		fbData.setKaishaCd((String)shainKouza.get("kaisha_cd")); // 会社コード
		fbData.setKaishaNameHankana((String)shainKouza.get("kaisha_name_hankana")); // 会社名（半角カナ）
		fbData.setFurikomiDate(furikomiBi); // 振込日
		fbData.setMotoKinyuukikanCd((String)shainKouza.get("moto_kinyuukikan_cd")); // 振込元金融機関コード
		fbData.setMotoKinyuukikanNameHankana((String)shainKouza.get("moto_kinyuukikan_name_hankana")); // 振込元金融機関名（半角カナ）
		fbData.setMotoKinyuukikanShitenCd((String)shainKouza.get("moto_kinyuukikan_shiten_cd")); // 振込元金融機関支店コード
		fbData.setMotoKinyuukikanShitenNameHankana((String)shainKouza.get("moto_kinyuukikan_shiten_name_hankana")); // 振込元金融機関支店名（半角カナ）
		fbData.setMotoYokinShumokuCd((String)shainKouza.get("moto_yokinshubetsu")); // 振込元預金種目コード
		fbData.setMotoKouzaBangou((String)shainKouza.get("moto_kouza_bangou")); // 振込元口座番号
		fbData.setSakiKinyuukikanCd((String)shainKouza.get("saki_kinyuukikan_cd")); // 振込先金融機関銀行コード
		fbData.setSakiKinyuukikanNameHankana((String)shainKouza.get("kinyuukikan_name_hankana")); // 振込先金融機関名（半角カナ）
		fbData.setSakiKinyuukikanShitenCd((String)shainKouza.get("saki_ginkou_shiten_cd")); // 振込先金融機関支店コード
		fbData.setSakiKinyuukikanShitenNameHankana((String)shainKouza.get("shiten_name_hankana")); // 振込先金融機関支店名（半角カナ）
		fbData.setSakiYokinShumokuCd((String)shainKouza.get("saki_yokin_shabetsu")); // 振込先預金種目コード
		fbData.setSakiKouzaBangou((String)shainKouza.get("saki_kouza_bangou")); // 振込先口座番号
		fbData.setSakiKouzaMeigiKana((String)shainKouza.get("saki_kouza_meigi_kana")); // 振込先口座名義（カナ）
		fbData.setKingaku(kingaku); // 金額
		fbData.setShinkiCd((String)shainKouza.get("shinki_cd")); // 新規コード
		String shainNo = (String)shainKouza.get("shain_no"); // 顧客コード１
		if (StringUtils.isNotEmpty(shainNo) && shainNo.length() > 10) {
			fbData.setKokyakuCd1(shainNo.substring(0,10));
		} else {
			fbData.setKokyakuCd1(shainNo);
		}
		fbData.setFurikomiKbn((String)shainKouza.get("furikomi_kbn")); // 振込区分
		fbLogic.insert(fbData);
		return furikomiBi;
	}

	/**
	 * 伝票テーブルの抽出済フラグをONにする
	 * @param denpyouId 伝票ID
	 */
	public void updateChuushutsuZumi(String denpyouId) {
		final String sql = "UPDATE denpyou SET chuushutsu_zumi_flg = '1', koushin_user_id = 'batch', koushin_time = current_timestamp WHERE denpyou_id = ?";
		connection.update(sql, denpyouId);
	}

	/**
	 * 伝票仕訳の行区切りを設定します。
	 * ※伝票番号＆伝票日付別に末尾の仕訳のみ区切り「1」を付ける。
	 * @param denpyouId 伝票ID
	 * @param invoiceFlg インボイスフラグ
	 * @return 結果
	 */
	public int updateShiwakeGSEPByDate(String denpyouId,String invoiceFlg) {
		String denpyouKbn = denpyouId.substring(7, 11);
		String joinStr = shiwakeDataImportLogic.makeJoinStringByKbn(denpyouKbn);
		final String sql = 
				  "update "
				+ ( (Open21Env.getVersion() == Version.DE3 ) ? "shiwake_de3 s ":"shiwake_sias s ")
				+ "set "
				+ "  gsep = '1' "
				+ "from "
				+ "( "
				+ "  select s.denpyou_id, dymd, max(serial_no) as serial_no "
				+ ( (Open21Env.getVersion() == Version.DE3 ) ? "from shiwake_de3 s ":"from shiwake_sias s ")
				+ joinStr
				+ ( (joinStr.length() > 0) ? " WHERE kbn.invoice_denpyou = '"+ invoiceFlg + "' " :"")
				+ "  group by "
				+ "    s.denpyou_id, dymd "
				+ ") sl "
				+ "where "
				+ "  s.denpyou_id = sl.denpyou_id and "
				+ "  s.serial_no = sl.serial_no and "
				+ "  s.denpyou_id = ? ";
		return connection.update(sql, denpyouId);
	}

	/**
	 * 伝票仕訳の伝票番号を設定します。
	 * ※伝票種別＆伝票日付別に先頭の仕訳に伝票番号を合わせる。
	 * @param denpyouId 伝票ID
	 * @param invoiceFlg インボイスフラグ
	 * @return 結果
	 */
	public int updateShiwakeDCNOByDate(String denpyouId,String invoiceFlg) {
		String denpyouKbn = denpyouId.substring(7, 11);
		String joinStr = shiwakeDataImportLogic.makeJoinStringByKbn(denpyouKbn);
		final String sql = 
				  "update "
				+ ( (Open21Env.getVersion() == Version.DE3 ) ? "shiwake_de3 s ":"shiwake_sias s ")
				+ "set "
				+ "  dcno = sl.dcno  "
				+ "from  "
				+ "(  "
				+ "  select substring(s.denpyou_id, 8, 4) as denpyou_kbn, dymd, min(dcno) as dcno  "
				+ ( (Open21Env.getVersion() == Version.DE3 ) ? "from shiwake_de3 s ":"from shiwake_sias s ")
				+ joinStr
				+ "  where "
				+ "    shiwake_status='0' "
				+( (joinStr.length() > 0) ? " AND kbn.invoice_denpyou = '"+ invoiceFlg + "' " :"")
				+ "  group by "
				+ "    substring(s.denpyou_id, 8, 4), dymd  "
				+ ") sl "
				+ "where  "
				+ "  substring(s.denpyou_id, 8, 4) = sl.denpyou_kbn and  "
				+ "  s.dymd = sl.dymd and "
				+ "  s.denpyou_id = ? ";
		return connection.update(sql, denpyouId);
	}

	/**
	 * 承認履歴にデータ抽出の履歴を残す
	 * @param denpyouId 伝票ID
	 * @param fbDataMade FBデータ作成有無
	 */
	public void insertSyouninJoukyou(String denpyouId, boolean fbDataMade) {
		WorkflowEventControlLogic wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		wfEventLogic.insertShouninJoukyouBatch(denpyouId, "", "データ抽出", fbDataMade ? "仕訳データ抽出、FBデータ抽出" : "仕訳データ抽出");
	}

	/**
	 * 仕訳作成形式を判定<br>
	 * キー：EteamConst.SHIWAKE_SAKUSEI_TYPE.FUKUGOU　true→複合形式/false→単一形式<br>
	 * 　　　EteamConst.SHIWAKE_SAKUSEI_TYPE.HASSEI　 true→発生主義/false→現金主義
	 * @param  houhouKey 仕訳作成方法を取得するキー
	 * @param  shiharaibi 支払日
	 * @param  keijoubi 計上日
	 * @param  keishikiKey 仕訳形式を取得するキー（なければnull）
	 * @return 結果
	 */
	public Map<String, Boolean> getShiwakeSakuseiKeishikiInfoMap(String houhouKey, Date shiharaibi, Date keijoubi, String keishikiKey) {
		Map<String, Boolean> sakuseiKeishikiInfo = new HashMap<String, Boolean>();
		sakuseiKeishikiInfo.put(EteamConst.SHIWAKE_SAKUSEI_TYPE.FUKUGOU, false);
		sakuseiKeishikiInfo.put(EteamConst.SHIWAKE_SAKUSEI_TYPE.HASSEI, false);
		
		// 単一形式か複合形式か
		if(keishikiKey != null) {
			sakuseiKeishikiInfo.put(EteamConst.SHIWAKE_SAKUSEI_TYPE.FUKUGOU, "0".equals(EteamSettingInfo.getSettingInfo(keishikiKey)) ? false : true);
		}
		
		// 発生主義か現金主義か
		String houhou = EteamSettingInfo.getSettingInfo(houhouKey);
		if(("1".equals(houhou) && (keijoubi == null || keijoubi.compareTo(shiharaibi) != 0))|| "2".equals(houhou)) {
			sakuseiKeishikiInfo.put(EteamConst.SHIWAKE_SAKUSEI_TYPE.HASSEI, true);
		}
		
		return sakuseiKeishikiInfo;
	}
	
	/**
	 * 課税区分を仕訳作成用の課税区分に変換する
	 * @param kazeiKbn 課税区分
	 * @param kaigaiFlg 海外フラグ（true:海外、false:国内）
	 * @param shubetsuCd 種別コード（1:交通費、2:その他）
	 * @param koutsuuShudan 交通手段
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２
	 * @param yakushokuCd 役職コード
	 * @return 課税区分（仕訳作成用）
	 */
	public String cnvZeiKbn(String kazeiKbn, boolean kaigaiFlg, String shubetsuCd, String koutsuuShudan, String shubetsu1, String shubetsu2, String yakushokuCd){
		MasterKanriCategoryLogic masterCommonLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		String zeiKbn = "";
		String tmp = "";
		
		if(RYOHISEISAN_SYUBETSU.KOUTSUUHI.equals(shubetsuCd)){
			// 海外交通費の場合
			if(kaigaiFlg){
				zeiKbn = masterCommonLogic.findKaigaiZeiKubun(koutsuuShudan);
			// 国内交通費の場合
			}else{
				zeiKbn = masterCommonLogic.findZeiKubun(koutsuuShudan);
			}
		}else{
			// 海外宿泊費等の場合
			if(kaigaiFlg){
				zeiKbn = masterCommonLogic.findKaigaiNittouZeiKubun(shubetsu1, shubetsu2, yakushokuCd);
			// 国内宿泊費等の場合
			}else{
				zeiKbn = masterCommonLogic.findNittouZeiKubun(shubetsu1, shubetsu2, yakushokuCd);
			}
		}
		
		// 各マスターから取得した税区分を対応する課税区分のコードに変換
		if(!zeiKbn.isEmpty()){
			switch(zeiKbn){
			case "0":
				tmp = KAZEI_KBN.MISETTEI; //未設定
				break;
			case "1":
				tmp = KAZEI_KBN.TAISHOUGAI; //対象外
				break;
			case "2":
				tmp = KAZEI_KBN.ZEIKOMI; //税込
				break;
			default:
				tmp = "00".concat(zeiKbn); //免税または非課税
			}
		}
		
		return ("".equals(zeiKbn))? kazeiKbn : tmp;
	}
	
	/**
	 * 日当等明細の日当宿泊費フラグ(0:その他、1:宿泊費、2:日当)
	 * @param kaigaiFlg 海外フラグ
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２
	 * @param yakushokuCd 役職コード
	 * @return 日当宿泊費フラグ
	 */
	public String findNittouShukuhakuhiFlg(boolean kaigaiFlg, String shubetsu1, String shubetsu2, String yakushokuCd){
		MasterKanriCategoryLogic masterCommonLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		GMap nittou = null;
		String nittouShukuhakuhiFlg = "0";
		
		if (kaigaiFlg) {
			nittou = masterCommonLogic.loadKaigaiNittouKingaku(shubetsu1, shubetsu2, yakushokuCd);
		} else {
			nittou = masterCommonLogic.loadNittouKingakuAndFlgAndKbn(shubetsu1, shubetsu2, yakushokuCd);
		}
		if (nittou != null) {
			nittouShukuhakuhiFlg = (String)nittou.get("nittou_shukuhakuhi_flg");
		}

		return nittouShukuhakuhiFlg;
	}
	
	/**
	 * 明細が日当かどうかを判定
	 * @param kaigaiFlg 海外フラグ
	 * @param shubetsu1 種別１
	 * @param shubetsu2 種別２
	 * @param yakushokuCd 役職コード
	 * @return true（日当）:false（日当以外）
	 */
	public boolean isNittou(boolean kaigaiFlg, String shubetsu1, String shubetsu2, String yakushokuCd){
		String nittouShukuhakuhiFlg = findNittouShukuhakuhiFlg(kaigaiFlg, shubetsu1, shubetsu2, yakushokuCd);
		return "2".equals(nittouShukuhakuhiFlg)? true : false;
	}
	
	/**
	 * 指定伝票区分で起案番号を運用する設定であるか判別
	 * @param denpyouKbn 伝票区分
	 * @return 伝票種別一覧情報
	 */
	boolean judgeKianbangouUnyouFlg(String denpyouKbn) {
		boolean blnRes = false;
		final String sql =
				"SELECT kianbangou_unyou_flg "
			+ "FROM denpyou_shubetsu_ichiran d "
			+ "WHERE "
			+ "  d.denpyou_kbn = ? ";
		GMap tmpMap = connection.find(sql, denpyouKbn);
		if(tmpMap != null){
			if ( "1".equals(tmpMap.get("kianbangou_unyou_flg")) )
			{
				blnRes = true;
			}
		}
			
		return blnRes;
	}
	
	/**
	 * 伝票起案紐付情報の取得
	 * @param denpyouId 伝票ID
	 * @return 伝票起案紐付情報
	 */
	public GMap findKianHimozuke(String denpyouId) {
		final String sql =
				"SELECT * "
			+ "FROM denpyou_kian_himozuke k "
			+ "WHERE "
			+ "  k.denpyou_id = ? ";
		return connection.find(sql, denpyouId);
	}
	
	/**
	 * バッチ不良ログファイル紐づけの設定
	 * @param serialNo シリアルNo
	 * @param edaban 枝番
	 * @param fileName ファイル名
	 * @return 結果
	 */
	public int insertBatchErrorLogHimoduke(long serialNo, int edaban, String fileName) {
		final String sql = "INSERT INTO batch_log_invalid_log_himoduke VALUES (?, ?, ?); ";
		return connection.update(sql, serialNo, edaban, fileName);
	}
	
	/**
	 * 不良データログの設定
	 * @param fileName ファイル名
	 * @param gyouNo 行No
	 * @param koumokuNo 項目No
	 * @param koumokuName 項目名称
	 * @param invalidValue 不正な値
	 * @param errorNaiyou エラー内容
	 * @return 結果
	 */
	public int insertInvalidFileLog(String fileName, int gyouNo, int koumokuNo, String koumokuName, String invalidValue, String errorNaiyou) {
		final String sql = "INSERT INTO batch_log_invalid_file_log VALUES (?, ?, ?, ?, ?, ?); ";
		return connection.update(sql, fileName, gyouNo, koumokuNo, koumokuName, invalidValue, errorNaiyou);
	}
	
	/**
	 * 不良伝票ログの設定
	 * @param fileName ファイル名
	 * @param denpyouStartGyou 伝票開始行
	 * @param denpyouEndGyou 伝票終了行
	 * @param denpyouDate 伝票日付
	 * @param denpyouBangou 伝票番号
	 * @param taishakuSagakuKingaku 貸借差額金額
	 * @param gaiyou 概要
	 * @param naiyou 内容
	 * @return 結果
	 */
	public int insertInvalidDenpyouLog(String fileName, int denpyouStartGyou, int denpyouEndGyou, Date denpyouDate, String denpyouBangou, BigDecimal taishakuSagakuKingaku, String gaiyou, String naiyou) {
		final String sql = "INSERT INTO batch_log_invalid_denpyou_log VALUES (?, ?, ?, ?, ?, ?, ?, ?); ";
		return connection.update(sql, fileName, denpyouStartGyou, denpyouEndGyou, denpyouDate, denpyouBangou, taishakuSagakuKingaku, gaiyou, naiyou);
	}
	
	/**
	 * 貸借を同じくする仕訳を諸口マージする
	 * @param shiwakeList 仕訳リスト
	 * @param kari 借方をマージする
	 * @param kashi 貸方をマージする
	 * @return マージ後の仕訳リスト
	 */
	public List<ShiwakeData> mergeShiwake(List<ShiwakeData> shiwakeList, boolean kari, boolean kashi) {
		CommonChuushutsuLogic comChu = EteamContainer.getComponent(CommonChuushutsuLogic.class, connection);

		if(shiwakeList == null || shiwakeList.isEmpty()) return new ArrayList<>();
		ShiwakeData s;

		List<ShiwakeData> kariList;
		List<ShiwakeData> kashiList;

		//借方／諸口にする
		kariList = new ArrayList<>();
		for(int i = 0; i < shiwakeList.size(); i++){
			s = shiwakeList.get(i).clone();
			s.setKashi(comChu.makeShokuchi(), null, null, null);
			s.getCom().setSYMD(null);
			kariList.add(s);
		}
		//諸口/貸方にする
		kashiList = new ArrayList<>();
		for(int i = 0; i < shiwakeList.size(); i++){
			s = shiwakeList.get(i).clone();
			s.setKari(comChu.makeShokuchi(), null, null);
			String kashiKazei = s.getS().getZKB();
			if(kashiKazei == null || !List.of("1", "2","11","12","13","14").contains(kashiKazei)) {
				s.getCom().setBUNRI("");
			}
			s.setZKMK(""); //元が消費税仕訳の場合、貸方は集約したいので税対象科目コードを空文字に置き換え
			kashiList.add(s);
		}
		
		//借方を同じくする仕訳をグループ化して、
		//グループ毎の先頭に金額合計して
		//グループ毎の先頭以外は消す
		if(kari){
			for(int i = 0; i < kariList.size(); i++){
				s = kariList.get(i);
				for(int j = kariList.size()-1; i < j; j--){
					ShiwakeData s2 = kariList.get(j);
					if(s.getR().equals(s2.getR())){
						s.getCom().setVALU(s.getCom().getVALU().add(s2.getCom().getVALU()));
						kariList.remove(j);
					}
				}
			}
		}
		
		//貸方を同じくする仕訳をグループ化して、
		//グループ毎の先頭に金額合計して
		//グループ毎の先頭以外は消す
		if(kashi){
			for(int i = 0; i < kashiList.size(); i++){
				s = kashiList.get(i);
				for(int j = kashiList.size()-1; i < j; j--){
					ShiwakeData s2 = kashiList.get(j);
					if(s.getS().equals(s2.getS())){
						s.getCom().setVALU(s.getCom().getVALU().add(s2.getCom().getVALU()));
						kashiList.remove(j);
					}
				}
			}
		}

		List<ShiwakeData> ret = new ArrayList<>();
		ret.addAll(kariList);
		ret.addAll(kashiList);
		return ret;
	}
	
	
	/**
	 * 貸借を同じくする仕訳をマージする(諸口分割はしない)
	 * @param shiwakeList 仕訳リスト
	 * @param kari 借方をマージする
	 * @param kashi 貸方をマージする
	 * @return マージ後の仕訳リスト
	 */
	public List<ShiwakeData> mergeShiwakeWithoutBunkatsu(List<ShiwakeData> shiwakeList, boolean kari, boolean kashi) {
		//諸口分割なしのマージでは借方・貸方の同時マージはさせない
		if(kari == true && kashi == true) throw new RuntimeException("貸借両方true指定はNG");

		if(shiwakeList == null || shiwakeList.isEmpty()) return new ArrayList<>();
		ShiwakeData s;

		//借方を同じくする仕訳をグループ化して、
		//グループ毎の先頭に金額合計して
		//グループ毎の先頭以外は消す
		if(kari){
			for(int i = 0; i < shiwakeList.size(); i++){
				s = shiwakeList.get(i);
				for(int j = shiwakeList.size()-1; i < j; j--){
					ShiwakeData s2 = shiwakeList.get(j);
					if(s.getR().equals(s2.getR())){
						s.getCom().setVALU(s.getCom().getVALU().add(s2.getCom().getVALU()));
						shiwakeList.remove(j);
					}
				}
			}
		}
		
		//貸方を同じくする仕訳をグループ化して、
		//グループ毎の先頭に金額合計して
		//グループ毎の先頭以外は消す
		if(kashi){
			for(int i = 0; i < shiwakeList.size(); i++){
				s = shiwakeList.get(i);
				for(int j = shiwakeList.size()-1; i < j; j--){
					ShiwakeData s2 = shiwakeList.get(j);
					if(s.getS().equals(s2.getS())){
						s.getCom().setVALU(s.getCom().getVALU().add(s2.getCom().getVALU()));
						shiwakeList.remove(j);
					}
				}
			}
		}
		return shiwakeList;
	}

	/**
	 * 交通費や日当等の明細にマスタ毎の枝番号をセットする
	 * @param meisai 明細レコード
	 * @param yakushokuCd 起票者役職コード
	 */
	public void setMasterEdaban(GMap meisai, String yakushokuCd) {
		boolean kaigaiFlg = "1".equals(meisai.get("kaigai_flg"));
		String shubetsuCd = meisai.get("shubetsu_cd"); if(shubetsuCd == null) shubetsuCd = "1";
		String koutsuuShudan = meisai.get("koutsuu_shudan");
		String shubetsu1 = meisai.get("shubetsu1");
		String shubetsu2 = meisai.get("shubetsu2");
		// 借方科目枝番を上書き
		if(kaigaiFlg){
			if(shubetsuCd.equals("1")) {
				meisai.put("kari_kamoku_edaban_cd", masterLogic.findKaigaiKoutsuuShudanEdaban(koutsuuShudan));
			}else {
				meisai.put("kari_kamoku_edaban_cd", masterLogic.findKaigaiNittouEdaban(shubetsu1,shubetsu2,yakushokuCd));
			}
		}else {
			if(shubetsuCd.equals("1")) {
				meisai.put("kari_kamoku_edaban_cd", masterLogic.findKoutsuuShudanEdaban(koutsuuShudan));
			}else {
				meisai.put("kari_kamoku_edaban_cd", masterLogic.findNittouEdaban(shubetsu1,shubetsu2,yakushokuCd));
			}
		}
	}

	/**
	 * 交通費や日当等の明細にマスタ毎の枝番号をセットする
	 * @param meisaiList 明細レコード
	 * @param yakushokuCd 起票者役職コード
	 */
	public void setMasterEdaban(List<GMap> meisaiList, String yakushokuCd) {
		for(GMap meisai : meisaiList) setMasterEdaban(meisai, yakushokuCd);
	}

	/**
	 * 借枝番と支払方法(C,K)が一致するやつを金額合算する
	 * @param orgList 交通費や日当等のリスト
	 * @return 交通費や日当等のリスト（合算後）
	 */
	public List<GMap> mergeRyohiMeisai(List<GMap> orgList) {
		List<GMap> newList = new ArrayList<>();

		//元リスト１つずつ処理
		for(GMap orgMap : orgList) {
			String orgEdaban = orgMap.get("kari_kamoku_edaban_cd");
			if(orgEdaban == null) {
				orgEdaban = "";
			}
			String orgShiharai = "";
			if (orgMap.get("houjin_card_riyou_flg").equals("1"))
			{
				orgShiharai = "C";
			}
			if (orgMap.get("kaisha_tehai_flg").equals("1"))
			{
				orgShiharai = "K";
			}
			String orgJigyousha = orgMap.get("jigyousha_kbn");//(String)は必要になる？
			if(orgJigyousha == null) {
				orgJigyousha = "";
			}

			GMap tmpMap = null;
			for(GMap newMap : newList) {
				String newEdaban = newMap.get("kari_kamoku_edaban_cd");
				if(newEdaban == null) {
					newEdaban = "";
				}
				String newShiharai = "";
				if (newMap.get("houjin_card_riyou_flg").equals("1"))
				{
					newShiharai = "C";
				}
				if (newMap.get("kaisha_tehai_flg").equals("1"))
				{
					newShiharai = "K";
				}
				String newJigyousha = newMap.get("jigyousha_kbn");
				if(newJigyousha == null) {
					newJigyousha = "";
				}
				if(orgEdaban.equals(newEdaban) && orgShiharai.equals(newShiharai) && orgJigyousha.equals(newJigyousha)) {
					tmpMap = newMap;
					break;
				}
			}
			//後リストにまだ同じ借枝番と支払方法の明細がなければそのまま入れる
			if(tmpMap == null) {
				newList.add(orgMap);
			//後リストに借枝番と支払方法が同じやつがあったらそいつに金額寄せる
			//消費税額も集計
			}else {
				BigDecimal meisaiKingaku = tmpMap.get("meisai_kingaku");
				BigDecimal zeinukiKingaku = tmpMap.get("zeinuki_kingaku");
				BigDecimal shouhizeigaku = tmpMap.get("shouhizeigaku");
				meisaiKingaku = meisaiKingaku.add(orgMap.get("meisai_kingaku"));
				zeinukiKingaku = zeinukiKingaku.add(orgMap.get("zeinuki_kingaku"));
				shouhizeigaku = shouhizeigaku.add(orgMap.get("shouhizeigaku"));
				tmpMap.put("meisai_kingaku", meisaiKingaku);
				tmpMap.put("zeinuki_kingaku", zeinukiKingaku);
				tmpMap.put("shouhizeigaku", shouhizeigaku);
			}
		}
		return newList;
	}
	
	/**
	 * 明細金額から差引金額を減算する。
	 * @param meisaiKingaku 明細金額
	 * @param sashihikiKingaku 差引金額
	 * @return 結果リスト（[0]:明細金額、[1]:差引金額）
	 */
	public List<BigDecimal> subtractSasihikiKingaku(BigDecimal meisaiKingaku, BigDecimal sashihikiKingaku){
		//差引金額 >= 旅費明細金額の場合
		if(sashihikiKingaku.doubleValue() >= meisaiKingaku.doubleValue()){
			sashihikiKingaku = sashihikiKingaku.subtract(meisaiKingaku);
			meisaiKingaku = BigDecimal.ZERO; //旅費明細金額を0に
		}else{
			meisaiKingaku  = meisaiKingaku.subtract(sashihikiKingaku);
			sashihikiKingaku =BigDecimal.ZERO; //差引金額を0に
		}
		
		List<BigDecimal> ret = new ArrayList<BigDecimal>();
		ret.add(meisaiKingaku);
		ret.add(sashihikiKingaku);
		return ret;
	}
	
	
	/**
	 * 摘要オーバー注記文言を返す。
	 * @return 注記文言
	 */
	public String getTekiyouChuuki() {
		SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class,connection);
// sysLogic.init(connection);
		if(sysLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.CHUUKI_PREVIEW)) {
			return "\r\n" + EteamConst.Chuuki.CHUUKI_EXCEL;
		}
		return "";
	}
	
	/**
	 * 消費税科目の外部コードを取得する。税抜かの判別条件はメソッドの外側でやること。
	 * @param denpyou 伝票
	 * @return 消費税科目外部コード
	 */
	public String getZeiKamokuGaibuCd(GMap denpyou)
	{
		KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);
		return getZeiKamokuGaibuCd(denpyou,dto);
		
	}
	
	/**
	 * 消費税科目の外部コードを取得する。
	 * @param denpyou 伝票
	 * @param dto 消費税設定DTO
	 * @return 消費税科目外部コード
	 */
	public String getZeiKamokuGaibuCd(GMap denpyou, KiShouhizeiSetting dto)
	{
		String taishaku = "kari";
		if("002".equals(denpyou.get("kari_kazei_kbn")) || "013".equals(denpyou.get("kari_kazei_kbn")) ||"014".equals(denpyou.get("kari_kazei_kbn"))) {
			taishaku = "kari";
		}
		else if("002".equals(denpyou.get("kashi_kazei_kbn")) || "013".equals(denpyou.get("kashi_kazei_kbn")) ||"014".equals(denpyou.get("kashi_kazei_kbn"))) {
			taishaku = "kashi";
		}
		var shoriGrpMp = kamokuMasterDao.find(denpyou.get(taishaku+"_kamoku_cd"));
		if (shoriGrpMp == null)
		{
			return null;
		}
		var shoriGroup = shoriGrpMp.shoriGroup;
		if (shoriGroup == null)
		{
			return null;
		}
		//科目マスターに登録されているレコードの中で、処理グループがnullになることは普通に使ってたらあり得ない
// GMap kamokuMap = masterCommonLogic.findKamokuMaster((String)meisai.get("kari_kamoku_cd"));
// String shoriGrp = kamokuMap.get("shori_group") == null ? null : kamokuMap.get("shori_group").toString();
		String taishakuKazeiKbn = taishaku+"_kazei_kbn";
		var kamokuNaibuCd = List.of(2, 8).contains(shoriGroup)
			? denpyou.get(taishakuKazeiKbn).equals("013")
				? dto.haraisyouhizeiShisan
				: dto.ukeshouhizeiShisan
			: List.of(3, 4, 9).contains(shoriGroup) || (shoriGroup == 7 && !denpyou.get(taishakuKazeiKbn).equals("013"))
				? dto.ukeshouhizeiUriage
				: List.of(5, 10).contains(shoriGroup) || shoriGroup == 7 // 課税区分条件は既に
					? dto.haraishouhizeiShiire
					: dto.haraishouhizeiKeihi;
		//上記以外の処理グループでは消費税仕訳が作成されることが無い
		return kamokuMasterDao.findByKamokuNaibuCd(kamokuNaibuCd).kamokuGaibuCd;
	}

	/**
	 * 消費税対象科目の部門・取引先・枝番・プロジェクト・セグメント・UF1～3を消費税設定テーブルの設定に基づいてセットする
	 * <br> 消費税仕訳の作成時に使用してください。消費税仕訳かどうかの判定は行いません
	 * @param shiwakeDataRS 仕訳
	 */
	public void setZeitaishouKoumoku(ShiwakeDataRS shiwakeDataRS) {
		KiShouhizeiSetting dto = kiShouhizeiSettingDao.findByDate(null);
		setZeitaishouKoumoku(shiwakeDataRS,dto);
	}
	
	/**
	 * 消費税対象科目の部門・取引先・枝番・プロジェクト・セグメント・UF1～3を消費税設定テーブルの設定で、1なら元のまま、1でないなら空にする
	 * <br> 消費税仕訳の作成時に使用してください。消費税仕訳かどうかの判定は行いません
	 * @param shiwakeDataRS 仕訳
	 * @param dto 消費税設定テーブル
	 */
	public void setZeitaishouKoumoku(ShiwakeDataRS shiwakeDataRS, KiShouhizeiSetting dto) {
		shiwakeDataRS.BMN = dto.shouhizeiBumon == 1 ? shiwakeDataRS.BMN : "";
		shiwakeDataRS.TOR = dto.shouhizeiTorihikisaki == 1 ? shiwakeDataRS.TOR : "";
		shiwakeDataRS.EDA = dto.shouhizeiEdaban == 1 ? shiwakeDataRS.EDA : "";
		shiwakeDataRS.PRJ = dto.shouhizeiProject == 1 ? shiwakeDataRS.PRJ : "";
		shiwakeDataRS.SEG = dto.shouhizeiSegment == 1 ? shiwakeDataRS.SEG : "";
		shiwakeDataRS.DM1 = dto.shouhizeiUf1 == 1 ? shiwakeDataRS.DM1 : "";
		shiwakeDataRS.DM2 = dto.shouhizeiUf2 == 1 ? shiwakeDataRS.DM2 : "";
		shiwakeDataRS.DM3 = dto.shouhizeiUf3 == 1 ? shiwakeDataRS.DM3 : "";
	}
	
	/**
	 * 分離区分が対象外・nullの場合は適切な値に変換する　※分離区分が貸借で分かれていない場合使用可
	 * @param meisai 明細（伝票）
	 * @return 分離区分
	 */
	public String getBunriKbnForShiwake(GMap meisai) {
		return getBunriKbnForShiwake(meisai,(String)meisai.get("bunri_kbn"),(String)meisai.get("kari_kazei_kbn"));
	}
	/**
	 * 分離区分が対象外・nullの場合は適切な値に変換する
	 * @param meisai 明細（伝票）
	 * @param bunrikbn 分離区分
	 * @param kazeikbn 課税区分
	 * @return 分離区分（変換後）
	 */
	public String getBunriKbnForShiwake(GMap meisai,String bunrikbn,String kazeikbn) {
		String bunri = bunrikbn;
		
		if(bunri == null) {
			bunri = "1".equals(meisai.get("invoice_denpyou")) ? "" : "0";
		}
		
		if("9".equals(bunri)) {
			bunri = "0";
		}
		//課税区分が未設定""なら分離区分も""にする
		if("".equals(kazeikbn)) {
			bunri = "";
		}
		
		return bunri;
	}
}
