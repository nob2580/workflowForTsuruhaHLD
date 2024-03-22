package eteam.gyoumu.houjincard;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Sessionkey;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 法人カードデータインポート（CSVアップロード確認）画面Action
 */
@Getter @Setter @ToString
public class HoujinCardCSVUploadKakuninAction extends EteamAbstractAction {

//＜定数＞
//＜画面入力＞
	/** 取込対象フラグ 0:取込対象外 1:取込対象*/
	String[] torikomiTaishouFlg;

//＜セッション＞
	/** CSVファイル名 */
	String csvFileName;
	/** CSVファイル情報（法人カード使用履歴）リスト ※登録用 */
	List<HoujinCardCSVUploadInfoBase> rirekiList;

	/** CSVファイル情報（法人カード使用履歴）表示用ヘッダリスト */
	List<String> hyoujiHeaderList;
	/** CSVファイル情報（法人カード使用履歴）表示用データリスト */
	List<String[]> hyoujiDataList;


//＜画面制御情報＞
	/** 処理済？ */
	boolean sumi;

	/** 対象カード */
	String cardShubetsu;

	/** 重複レコード存在時の項目表示フラグ */
	boolean duplicateHyoujiFlg = false;

//＜部品＞
	/** 法人カード情報用ロジック */
	HoujinCardLogic hcLogic;
	/** 重複レコードが存在する場合のアラートメッセージ */
	String alertMsg;


//＜入力チェック＞
	@Override
	protected void formatCheck() {
		for(int i = 0 ; i < torikomiTaishouFlg.length ; i++) {
			checkDomain(torikomiTaishouFlg[i], EteamConst.Domain.FLG, "取込対象:" + (i + 1) + "行目", false);
		}
	}

	@Override
	protected void hissuCheck(int eventNum) {
		for(int i = 0 ; i < torikomiTaishouFlg.length ; i++) {
			String[][] list = {
					//項目							項目名 						必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{torikomiTaishouFlg[i], "取込対象:" + (i + 1) + "行目", "1"},
			};
			hissuCheckCommon(list, eventNum);
		}
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		//セション情報を取得する。
		HoujinCardCSVUploadSessionInfo sessionInfo = (HoujinCardCSVUploadSessionInfo)session.get(Sessionkey.HOUJINCARD_CSV);
		if (null == sessionInfo) {
			errorList.add("インポートファイルのアップロードからやり直してください。");
			return "error";
		}
		csvFileName = sessionInfo.getFileName();
		rirekiList = sessionInfo.getRirekiList();
		errorList = sessionInfo.getErrorList();
		cardShubetsu = sessionInfo.getCardShubetsu();
		hyoujiHeaderList = sessionInfo.getHyoujiHeaderList();
		hyoujiDataList = sessionInfo.getHyoujiDataList();

		if(rirekiList == null || rirekiList.isEmpty()) {
			errorList.add("登録可能データがありません。");
			return "error";
		}

		// 全レコードで取込対象か否かのフラグを持つようにする(デフォルトは全て取込対象)
		torikomiTaishouFlg = new String[rirekiList.size()];
		Arrays.fill(torikomiTaishouFlg, "1");

		// 重複レコード存在時のアラート文、ヘッダーの表示判定
		for(HoujinCardCSVUploadInfoBase rireki : rirekiList) {
			if(rireki.duplicateFlg) {
				duplicateHyoujiFlg = true;
				alertMsg = "重複レコードがあります。重複レコードをインポート対象から外す場合は、取込対象のチェックを外してから登録ボタンを押してください。";
				break;
			}
		}

		// 戻り値を返す
		return "success";
	}

	/**
	 * 登録イベント
	 * @return ResultName
	 */
	public String touroku(){
		try(EteamConnection connection = EteamConnection.connect()) {
			setConnection(connection);
			//入力チェック
			formatCheck();
			hissuCheck(1);
			if (errorList.size() != 0)
			{
				return "error";
			}

			//セション情報を取得する。
			HoujinCardCSVUploadSessionInfo sessionInfo = (HoujinCardCSVUploadSessionInfo)session.get(Sessionkey.HOUJINCARD_CSV);
			if (null == sessionInfo) {
				errorList.add("インポートファイルのアップロードからやり直してください。");
				return "error";
			}
			csvFileName = sessionInfo.getFileName();
			rirekiList = sessionInfo.getRirekiList();
			cardShubetsu = sessionInfo.getCardShubetsu();
			hyoujiHeaderList = sessionInfo.getHyoujiHeaderList();
			hyoujiDataList = sessionInfo.getHyoujiDataList();

			if(rirekiList == null || rirekiList.isEmpty()) {
				errorList.add("登録可能データがありません。");
				return "error";
			}

			//登録予定データがDB重複しているかを先に確認
			Integer gyoCnt = 0;
			boolean[] dbDuplicateFlg = new boolean[rirekiList.size()];
			Arrays.fill(dbDuplicateFlg, false);
			for (HoujinCardCSVUploadInfoBase rireki : rirekiList) {
				if(torikomiTaishouFlg[gyoCnt].equals("1")) {
					String bushoCd = rireki.getBushoCd();
					String shainNo = rireki.getShainNo();
					String shiyousha = rireki.getShiyousha();
					String riyoubi = rireki.getRiyoubi();
					Date riyoubiDt = hcLogic.toDateyyyyMMdd(riyoubi);
					String kingaku = rireki.getKingaku();
					BigDecimal kingakuBd = new BigDecimal(kingaku);
					String cardBangou = rireki.getCardBangou();
					String kameiten = rireki.getKameiten();
					String gyoushuCd = rireki.getGyoushuCd();
					if(hcLogic.isDuplicate(cardShubetsu,bushoCd,shainNo,shiyousha,riyoubiDt,kingakuBd,cardBangou,kameiten,gyoushuCd)){
						dbDuplicateFlg[gyoCnt] = true;
					}
				}
				gyoCnt++;
			}

			gyoCnt = 0;
			for (HoujinCardCSVUploadInfoBase rireki : rirekiList) {
				rireki.setErrorFlg(false);
				rireki.setMitorikomiFlg(false);

				String bushoCd = rireki.getBushoCd();
				String shainNo = rireki.getShainNo();
				String shiyousha = rireki.getShiyousha();
				String riyoubi = rireki.getRiyoubi();
				Date riyoubiDt = hcLogic.toDateyyyyMMdd(riyoubi);
				String kingaku = rireki.getKingaku();
				BigDecimal kingakuBd = new BigDecimal(kingaku);
				String cardBangou = rireki.getCardBangou();
				String kameiten = rireki.getKameiten();
				String gyoushuCd = rireki.getGyoushuCd();
				if(torikomiTaishouFlg[gyoCnt].equals("1") && dbDuplicateFlg[gyoCnt] == false) {
					hcLogic.insert(cardShubetsu,bushoCd,shainNo,shiyousha,riyoubiDt,kingakuBd,cardBangou,kameiten,gyoushuCd,"0","",getUser().getTourokuOrKoushinUserId());
				}else{
					rireki.setMitorikomiFlg(true);
				}
				gyoCnt++;
			}

			sumi = true;
			session.remove(Sessionkey.HOUJINCARD_CSV);
			if(!errorList.isEmpty()) {
				connection.rollback();
				return "success";
			}else{
				connection.commit();
				return "success";
			}

		}
	}
	/**
	 * コネクション設定を行います。
	 * @param connection DBコネクション
	 */
	protected void setConnection(EteamConnection connection) {
		hcLogic = EteamContainer.getComponent(HoujinCardLogic.class, connection);
	}
}
