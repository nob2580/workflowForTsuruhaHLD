package eteam.gyoumu.tsuuchi;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.BUHIN_FORMAT;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.kaikei.FurikaeDenpyouAction;
import eteam.gyoumu.kaikei.JidouHikiotoshiDenpyouAction;
import eteam.gyoumu.kaikei.KaigaiRyohiKaribaraiShinseiAction;
import eteam.gyoumu.kaikei.KaigaiRyohiSeisanAction;
import eteam.gyoumu.kaikei.KaribaraiShinseiAction;
import eteam.gyoumu.kaikei.KeihiTatekaeSeisanAction;
import eteam.gyoumu.kaikei.KoutsuuhiSeisanAction;
import eteam.gyoumu.kaikei.RyohiKaribaraiShinseiAction;
import eteam.gyoumu.kaikei.RyohiSeisanAction;
import eteam.gyoumu.kaikei.SeikyuushoBaraiAction;
import eteam.gyoumu.kaikei.ShiharaiIraiAction;
import eteam.gyoumu.kaikei.SougouTsukekaeDenpyouAction;
import eteam.gyoumu.kaikei.TsuukinTeikiShinseiAction;
import eteam.gyoumu.kanitodoke.KaniTodokeAction;
import eteam.gyoumu.workflow.WorkflowEventControl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票一覧画面Logic
 */
public class DenpyouIchiranLogic extends EteamAbstractLogic {
		
	/**
	 * 各テーブルの計上日を更新する。
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @param keijoubi 計上日
	 * @param tourokuOrKoushinUserId ユーザーID
	 * @return 処理件数
	 */
	public int updateKeijoubi(
			String denpyouKbn,
			String denpyouId,
			Date keijoubi,
			String tourokuOrKoushinUserId
	) {
		String tableName = denpyouKbn2TableName(denpyouKbn);
		final String sql =
				"UPDATE " + tableName
			+ " SET keijoubi = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, keijoubi, tourokuOrKoushinUserId, denpyouId);
	}

	/**
	 * 各テーブルの支払日を更新する。
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @param shiharaibi 支払日
	 * @param tourokuOrKoushinUserId ユーザーID
	 * @return 処理件数
	 */
	public int updateShiharaibi(String denpyouKbn, String denpyouId, Date shiharaibi, String tourokuOrKoushinUserId) {
		String tableName = denpyouKbn2TableName(denpyouKbn);
		final String sql =
				"UPDATE " + tableName
			+ " SET shiharaibi = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, shiharaibi, tourokuOrKoushinUserId, denpyouId);
	}

	/**
	 * 請求書払い申請の支払日を更新する。
	 * @param denpyouId  伝票ID
	 * @param shiharaibi 支払日
	 * @param masrefFlg  マスター参照フラグ
	 * @param userId     ユーザーID
	 * @return 処理件数
	 */
	public int updateShiharaibi(
			String denpyouId,
			Date shiharaibi,
			String masrefFlg,
			String userId
	) {
		final String sql =
				"UPDATE seikyuushobarai "
			+   "SET shiharaibi = ?, masref_flg = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, shiharaibi, masrefFlg, userId, denpyouId);
	}

	/**
	 * 支払依頼申請の支払予定日を更新する。
	 * @param denpyouId 伝票ID
	 * @param shiharaiYoteibi 支払予定日
	 * @param tourokuOrKoushinUserId ユーザーID
	 * @return 処理件数
	 */
	public int updateYoteibi(String denpyouId, Date shiharaiYoteibi, String tourokuOrKoushinUserId) {
		final String sql =
				"UPDATE shiharai_irai "
			+ " SET yoteibi = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, shiharaiYoteibi, tourokuOrKoushinUserId, denpyouId);
	}

	/**
	 * 支払依頼テーブルの計上日・支払日・支払期日を更新する。
	 * @param denpyouId 伝票ID
	 * @param keijoubi 計上日
	 * @param shiharaibi 支払日
	 * @param shiharaiKijitsu 支払期日
	 * @param yoteibi 支払予定日
	 * @param tourokuOrKoushinUserId ユーザーID
	 * @return 処理件数
	 */
	public int updateShiharaiiraiDate(
			String denpyouId,
			Date keijoubi,
			Date shiharaibi,
			Date shiharaiKijitsu,
			Date yoteibi,
			String tourokuOrKoushinUserId
	) {
		final String sql =
				"UPDATE shiharai_irai "
			+ " SET keijoubi = ?, shiharaibi = ?, shiharai_kijitsu = ?, yoteibi = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, keijoubi, shiharaibi, shiharaiKijitsu, yoteibi, tourokuOrKoushinUserId, denpyouId);
	}

	
	/** 計上日・支払日情報 */
	@Getter @Setter @ToString
	public class KeijouShiharai {
		/** 支払日 */
		Date shiharaibi;
		/** 計上日 */
		Date keijoubi;
		/** 計上日名 */
		String keijoubiName;
		/** 支払方法 */
		String shiharaiHouhou;
		/** 想定外伝票区分かどうか*/
		boolean souteigaiDenpyouKbn;
	}

	/**
	 * 
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return 計上日・支払日情報
	 */
	public KeijouShiharai findKeijouShiharai(String denpyouKbn, String denpyouId) {
		TsuuchiCategoryLogic tl = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);

		KeijouShiharai ret = new KeijouShiharai();
		GMap denpyouRecord = null;
		BigDecimal sashihikiShikyuuKingaku;
		String shiwakeInfo = null;
		boolean souteigaiDenpyouKbn = false;
		
		//計上日型
		switch(denpyouKbn){
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
			denpyouRecord = tl.findKeihiSeisanKeiri(denpyouId);
			sashihikiShikyuuKingaku = (BigDecimal)denpyouRecord.get("sashihiki_shikyuu_kingaku");
			shiwakeInfo = setting.shiwakeSakuseiHouhouA001();
			ret.shiharaibi = (Date)denpyouRecord.get("shiharaibi");
			//差引支給金額が0以下なら（つまり本当に振込があるなら）支払方法と関係なく当日支払日許容とする。
			ret.shiharaiHouhou = (String)denpyouRecord.get("shiharaihouhou");
			ret.shiharaiHouhou = (0 < sashihikiShikyuuKingaku.compareTo(BigDecimal.ZERO)) ? ret.shiharaiHouhou : SHIHARAI_HOUHOU.GENKIN;
			//現金主義なら使用日を計上日代わりに
			ret.keijoubi = !"3".equals(shiwakeInfo) ? (Date)denpyouRecord.get("keijoubi") : (Date)denpyouRecord.get("shiyoubi");
			ret.keijoubiName = !"3".equals(shiwakeInfo) ? "計上日" : "使用日";
			break;

		case DENPYOU_KBN.RYOHI_SEISAN:
			denpyouRecord = tl.findRyohiSeisanKeiri(denpyouId);
			sashihikiShikyuuKingaku = (BigDecimal)denpyouRecord.get("sashihiki_shikyuu_kingaku");
			shiwakeInfo = setting.shiwakeSakuseiHouhouA004();
			ret.shiharaibi = (Date)denpyouRecord.get("shiharaibi");
			//差引支給金額が0以下なら（つまり本当に振込があるなら）支払方法と関係なく当日支払日許容とする。
			ret.shiharaiHouhou = (String)denpyouRecord.get("shiharaihouhou");
			ret.shiharaiHouhou = (0 < sashihikiShikyuuKingaku.compareTo(BigDecimal.ZERO)) ? ret.shiharaiHouhou : SHIHARAI_HOUHOU.GENKIN;
			//現金主義なら精算期間終了日を計上日代わりに
			ret.keijoubi = !"3".equals(shiwakeInfo) ? (Date)denpyouRecord.get("keijoubi") : (Date)denpyouRecord.get("seisankikan_to");
			ret.keijoubiName = !"3".equals(shiwakeInfo) ? "計上日" : "精算期間終了日";
			break;

		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			denpyouRecord = tl.findKaigaiRyohiSeisanKeiri(denpyouId);
			sashihikiShikyuuKingaku = (BigDecimal)denpyouRecord.get("sashihiki_shikyuu_kingaku");
			shiwakeInfo = setting.shiwakeSakuseiHouhouA011();
			ret.shiharaibi = (Date)denpyouRecord.get("shiharaibi");
			//差引支給金額が0以下なら（つまり本当に振込があるなら）支払方法と関係なく当日支払日許容とする。
			ret.shiharaiHouhou = (String)denpyouRecord.get("shiharaihouhou");
			ret.shiharaiHouhou = (0 < sashihikiShikyuuKingaku.compareTo(BigDecimal.ZERO)) ? ret.shiharaiHouhou : SHIHARAI_HOUHOU.GENKIN;
			//現金主義なら精算期間終了日を計上日代わりに
			ret.keijoubi = !"3".equals(shiwakeInfo) ? (Date)denpyouRecord.get("keijoubi") : (Date)denpyouRecord.get("seisankikan_to");
			ret.keijoubiName = !"3".equals(shiwakeInfo) ? "計上日" : "精算期間終了日";
			break;

		case DENPYOU_KBN.KOUTSUUHI_SEISAN:
			denpyouRecord = tl.findKoutsuuhiSeisanKeiri(denpyouId);
			shiwakeInfo = setting.shiwakeSakuseiHouhouA010();
			ret.shiharaibi = (Date)denpyouRecord.get("shiharaibi");
			ret.shiharaiHouhou = (String)denpyouRecord.get("shiharaihouhou");
			//現金主義なら精算期間終了日を計上日代わりに
			ret.keijoubi = !"3".equals(shiwakeInfo) ? (Date)denpyouRecord.get("keijoubi") : (Date)denpyouRecord.get("seisankikan_to");
			ret.keijoubiName = !"3".equals(shiwakeInfo) ?  "計上日" : "精算期間終了日";
			break;

		case DENPYOU_KBN.KARIBARAI_SHINSEI:
			denpyouRecord = tl.findKaribaraKeiri(denpyouId);
			ret.shiharaibi = (Date)denpyouRecord.get("shiharaibi");
			ret.shiharaiHouhou = (String)denpyouRecord.get("shiharaihouhou");
			break;

		case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
			denpyouRecord = tl.findRyohiKaribaraiKeiri(denpyouId);
			ret.shiharaibi = (Date)denpyouRecord.get("shiharaibi");
			ret.shiharaiHouhou = (String)denpyouRecord.get("shiharaihouhou");
			break;

		case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
			denpyouRecord = tl.findKaigaiRyohiKaribaraiKeiri(denpyouId);
			ret.shiharaibi = (Date)denpyouRecord.get("shiharaibi");
			ret.shiharaiHouhou = (String)denpyouRecord.get("shiharaihouhou");
			break;

		case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI:
			denpyouRecord = tl.findTsuukinTeiki(denpyouId);
			ret.shiharaibi = (Date)denpyouRecord.get("shiharaibi");
			ret.shiharaiHouhou = SHIHARAI_HOUHOU.FURIKOMI;
			break;

		case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
			denpyouRecord = tl.findJidouhikiotoshi(denpyouId);
			ret.shiharaibi = (Date)denpyouRecord.get("hikiotoshibi");
			break;

		case DENPYOU_KBN.SEIKYUUSHO_BARAI:
			denpyouRecord = tl.findSeikyuushoBarai(denpyouId);
			ret.shiharaibi = (Date)denpyouRecord.get("shiharaibi");
			ret.keijoubi = (Date)denpyouRecord.get("keijoubi");
			ret.keijoubiName = "計上日";
			ret.shiharaiHouhou = DENPYOU_KBN.SEIKYUUSHO_BARAI;
			break;

		case DENPYOU_KBN.SIHARAIIRAI:
			denpyouRecord = tl.findShiharaiirai(denpyouId);
			ret.keijoubi = (Date)denpyouRecord.get("keijoubi");
			ret.keijoubiName = "計上日";
			break;

		default:
			//カスタマイズ用
			findKeijouShiharaiExt(ret, denpyouId, denpyouKbn, denpyouRecord, shiwakeInfo,souteigaiDenpyouKbn);
			if(ret.souteigaiDenpyouKbn){
				throw new InvalidParameterException("想定外伝票区分");
			}
		}
		return ret;
	}
	
	/**
	 * カスタマイズ用
	 * @param ret 計上日・支払日情報
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @param denpyouRecord 伝票レコード
	 * @param shiwakeInfo   仕訳情報
	 * @param souteigaiDenpyouKbn 想定外伝票区分かどうか
	 * @return ret
	 */
		protected KeijouShiharai findKeijouShiharaiExt(KeijouShiharai ret,String denpyouId,String denpyouKbn, GMap denpyouRecord,String shiwakeInfo, boolean souteigaiDenpyouKbn){
			ret.souteigaiDenpyouKbn = true;
			 return ret;
		}
	
	/**
	 * 
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @return 各種WFアクション
	 */
	public WorkflowEventControl makeWfAction(String denpyouId, String denpyouKbn) {
		WorkflowCategoryLogic lg = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);

		//伝票テーブル取得
		GMap denpyou = lg.selectDenpyou(denpyouId);
		String version = ((Integer)denpyou.get("version")).toString();
		
		WorkflowEventControl ret = null;
		
		switch(denpyouKbn) {
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN :ret = new KeihiTatekaeSeisanAction(); break;//A001
		case DENPYOU_KBN.KARIBARAI_SHINSEI :ret = new KaribaraiShinseiAction(); break;//A002
		case DENPYOU_KBN.SEIKYUUSHO_BARAI :ret = new SeikyuushoBaraiAction(); break;//A003
		case DENPYOU_KBN.RYOHI_SEISAN :ret = new RyohiSeisanAction(); break;//A004
		case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI :ret = new RyohiKaribaraiShinseiAction(); break;//A005
		case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI :ret = new TsuukinTeikiShinseiAction();  	break;//A006
		case DENPYOU_KBN.FURIKAE_DENPYOU :ret = new FurikaeDenpyouAction(); break;//A007
		case DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU :ret = new SougouTsukekaeDenpyouAction(); break;//A008
		case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU :ret = new JidouHikiotoshiDenpyouAction(); break;//A009
		case DENPYOU_KBN.KOUTSUUHI_SEISAN :ret = new KoutsuuhiSeisanAction(); break;//A010
		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN :ret = new KaigaiRyohiSeisanAction(); break;//A011
		case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI :ret = new KaigaiRyohiKaribaraiShinseiAction(); break;//A012
		case DENPYOU_KBN.SIHARAIIRAI :ret = new ShiharaiIraiAction(); break;//A013
		default :
			//カスタマイズ用
			ret = makeWfExtAction(ret, denpyouId, denpyouKbn);
			//ユーザー定義届書
			if(ret == null){
				ret = new KaniTodokeAction();//B###
			}
		}
		ret.setDenpyouId(denpyouId);
		ret.setDenpyouKbn(denpyouKbn);
		ret.setVersion(version);
		return ret;
	}

	/**
	 * カスタマイズ用
	 * @param ret  ワークフローイベント制御クラス
	 * @param denpyouId  伝票ID
	 * @param denpyouKbn 伝票区分
	 * @return ret
	 */
	protected WorkflowEventControl makeWfExtAction(WorkflowEventControl ret, String denpyouId, String denpyouKbn) {
		return ret;
	}
	
	/**
	 * 伝票区分に対する業務テーブル名を返す
	 * @param denpyouKbn 伝票区分
	 * @return 業務テーブル名
	 */
	protected String denpyouKbn2TableName(String denpyouKbn) {
		
		String ret = null;
		
		switch(denpyouKbn){ 
		case DENPYOU_KBN.KARIBARAI_SHINSEI :ret = "karibarai"; break;
		case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI :ret = "ryohi_karibarai"; break;
		case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI :ret = "kaigai_ryohi_karibarai";break;
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN :ret = "keihiseisan"; break;
		case DENPYOU_KBN.RYOHI_SEISAN :ret = "ryohiseisan"; break;
		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN :ret = "kaigai_ryohiseisan"; break;
		case DENPYOU_KBN.KOUTSUUHI_SEISAN :ret = "koutsuuhiseisan"; break;
		case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI :ret = "tsuukinteiki"; break;
		case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU :ret = "jidouhikiotoshi"; break;
		case DENPYOU_KBN.SEIKYUUSHO_BARAI :ret = "seikyuushobarai"; break;
		default :
		//カスタマイズ用
		ret = denpyouKbn2ExtTableName(denpyouKbn,ret);
			if (ret == null){
			throw new InvalidParameterException("想定外伝票区分:" + denpyouKbn);
			}
		}
		return ret;
	}
	
	/**
	 * 伝票区分に対する業務テーブル名を返す（カスタマイズ用）
	 * @param denpyouKbn 伝票区分
	 * @param ret          	テーブル名
	 * @return 業務テーブル名
	 */
	protected  String denpyouKbn2ExtTableName(String denpyouKbn, String ret) { 
		return ret;
	}

	/**
	 * ユーザー定義届書の集計を行う
	 * @param dataList SELECT結果のリスト
	 * @param columnList dataListに対するカラム名
	 * @param formatList dataListに対するカラム別フォーマット
	 * @param decPointList dataListに対する小数点以下桁数
	 * @param sortKbnShuukei 集計区分
	 * @return dataListに集計レコードを差し込んだもの
	 */
	public List<GMap> kaniTodokeShuukei(List<GMap> dataList, List<String> columnList, List<String> formatList, List<String> decPointList, String sortKbnShuukei){
		List<GMap> retList = new ArrayList<>();

		//ユーザー・部門のどっちで？（どちらも？）集計するか
		boolean userShuukei = false;
		boolean bumonShuukei = false;
		switch(sortKbnShuukei){
		case TsuuchiCategoryLogic.SORT_KBN_ALL:
			userShuukei = true;
			bumonShuukei = true;
			break;
		case TsuuchiCategoryLogic.SORT_KBN_BUMON:
			bumonShuukei = true;
			break;
		case TsuuchiCategoryLogic.SORT_KBN_USER:
			userShuukei = true;
			break;
		}
		
		//元リストから１件ずつみていく。オリジナルレコードはそのまま結果リストに入れて、集計レコードを結果リストに追加。
		int bumonStart = 0;
		int userStart = 0;
		for(int i = 0; i < dataList.size(); i++){
			GMap data = dataList.get(i);
			retList.add(data);

			//ユーザ・部門の区切りレコードを入れるか判定する
			boolean bumonKugiri = bumonShuukei && (i == dataList.size() - 1 || !same(data.get("kihyouBumonCd"), dataList.get(i+1).get("kihyouBumonCd")));
			boolean userKugiri = userShuukei && (i == dataList.size() - 1 || !same(data.get("shain_no"), dataList.get(i+1).get("shain_no")));
			if (userShuukei && bumonKugiri)
			{
				userKugiri = true;
			}
			
			//ユーザーの区切りで集計レコード入れる
			if(userKugiri){
				retList.add(makeShuukei(dataList.subList(userStart, i + 1), 1, columnList, formatList, decPointList));
				userStart = i + 1;
			}
			
			//部門の区切りで集計レコード入れる
			if(bumonKugiri){
				retList.add(makeShuukei(dataList.subList(bumonStart, i + 1), 2, columnList, formatList, decPointList));
				bumonStart = i + 1;
			}
		}
		return retList;
	}

	/**
	 * SELECT結果リストに対する集計レコードを作る
	 * @param dataList SELECT結果リスト
	 * @param kbn 1:ユーザー集計、2:部門集計
	 * @param columnList dataListに対するカラム名
	 * @param formatList dataListに対するカラム別フォーマット
	 * @param decPointList dataListに対する小数点以下桁数
	 * @return 集計レコード
	 */
	protected GMap makeShuukei(List<GMap> dataList, int kbn, List<String> columnList, List<String> formatList, List<String> decPointList) {
		GMap ret = new GMap();
		ret.put("syuukei", "1");
		GMap data1 = dataList.get(0);
		
		//簡易届列については、形式が「数値」「金額」のいずれか
		for(int i = 0; i < columnList.size(); i++){
			String column = columnList.get(i);
			String format = formatList.get(i);
			String decPoint = decPointList.get(i);
			Object sumObj = "";
			Map<Object, Boolean> didMap = new HashMap<Object, Boolean>(); //明細が複数あると本体部分は重複して出てくるので重複防止用

			if(format != null) switch(format){
				case BUHIN_FORMAT.NUMBER:
					double sumVal = 0;
					for(GMap data : dataList){
						if (column.startsWith("shinsei") && didMap.containsKey(data.get("denpyou_id")))
						{
							continue;
						} //✫これBUHIN_FORMAT毎にやってね
						didMap.put(data.get("denpyou_id"), Boolean.TRUE);
						
						if(!isEmpty(data.get(column))){
							String columnData = (String)data.get(column);
							if(GenericValidator.isDouble(columnData)) sumVal += Double.parseDouble(columnData);
						}
					}
					if(isEmpty(decPoint) || decPoint.equals("0")){
						sumObj = new BigDecimal(sumVal).toPlainString();
					}else{
						sumObj = String.format("%1$.0" + decPoint + "f", sumVal);
					}
					break;
				case BUHIN_FORMAT.MONEY:
					sumVal = 0;
					for(GMap data : dataList){
						if (column.startsWith("shinsei") && didMap.containsKey(data.get("denpyou_id")))
						{
							continue;
						} //✫これBUHIN_FORMAT毎にやってね
						didMap.put(data.get("denpyou_id"), Boolean.TRUE);
						
						if(!isEmpty(data.get(column))){
							String columnData = ((String)data.get(column)).replace(",", "");
							if(GenericValidator.isDouble(columnData)) sumVal += Double.parseDouble(columnData);
						}
					}
					sumObj = new BigDecimal(sumVal);//CSV出力側でフォーマット
					break;
				case BUHIN_FORMAT.TIME:
					sumVal = 0;
					for(GMap data : dataList){
						if (column.startsWith("shinsei") && didMap.containsKey(data.get("denpyou_id")))
						{
							continue;
						} //✫これBUHIN_FORMAT毎にやってね
						didMap.put(data.get("denpyou_id"), Boolean.TRUE);
						
						if(!isEmpty(data.get(column))){
							String[] hm = ((String)data.get(column)).split(":");
							if(2 == hm.length && GenericValidator.isInt(hm[0]) && GenericValidator.isInt(hm[1])){
								sumVal += Integer.parseInt(hm[0])*60 + Integer.parseInt(hm[1]);
							}
						}
					}
					sumObj = String.format("%02d:%02d", (int)sumVal/60, (int)sumVal%60);
					break;
			}
			ret.put(column, sumObj);
		}
		
		//共通列については、部門・ユーザーだけ出す
		if(kbn == 1){
			ret.put("name", "合計");
			ret.put("kihyouBumonCd"		, data1.get("kihyouBumonCd"));
			ret.put("bumon_full_name"	, data1.get("bumon_full_name"));
			ret.put("shain_no"			, data1.get("shain_no"));
			ret.put("user_full_name"	, data1.get("user_full_name"));
		}else{
			ret.put("name", "起票部門合計");
			ret.put("kihyouBumonCd"		, data1.get("kihyouBumonCd"));
			ret.put("bumon_full_name"	, data1.get("bumon_full_name"));
		}
		return ret;
	}

	/**
	 * オブジェクト比較
	 * @param object1 １
	 * @param object2 ２
	 * @return １と２が一致している？
	 */
	protected boolean same(Object object1, Object object2) {
		return (object1 == null) ? (object2 == null) : object1.equals(object2);
	}
}
