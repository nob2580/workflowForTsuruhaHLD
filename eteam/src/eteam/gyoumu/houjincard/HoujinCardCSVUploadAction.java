package eteam.gyoumu.houjincard;

import java.io.File;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamIllegalRequestException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 法人カードデータインポート画面Action
 */
@Getter @Setter @ToString
public class HoujinCardCSVUploadAction extends EteamAbstractAction {

//＜定数＞
//＜画面入力＞
	/** ファイルオブジェクト */
	protected File uploadFile;
	/** ファイル名(uploadFileに付随) */
	protected String uploadFileFileName;
	/** 対象カード */
	protected String cardShubetsu;


//＜部品＞
	/** 法人カード情報用ロジック */
	HoujinCardLogic hcLogic;
	/** カード種別のDropDownList */
	List<GMap> cardList;
	/** 起案番号終了ドメイン */
	String[] cardShubetsuDomain;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		//TODO 対象カードのドメインをチェック？
	}

	@Override
	protected void hissuCheck(int eventNum) {
		super.checkHissu(uploadFile, "インポートファイル");
		super.checkHissu(cardShubetsu, "対象カード");
	}


//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()){
			session.remove(Sessionkey.HOUJINCARD_CSV);
			SystemKanriCategoryLogic common = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			setConnection(connection);
			// naibu_cd_settingテーブルからデータを取得（カード種別プルダウンリストの値）
			cardList = common.loadNaibuCdSetting("houjin_card_shubetsu");
			cardShubetsu = hcLogic.getCardShubtsu(super.getUser().getLoginUserId());

			return "success";
		}
	}

	/**
	 * アップロードイベント
	 * @return ResultName
	 */
	public String upload(){
		try(EteamConnection connection = EteamConnection.connect()){
			setConnection(connection);

			// naibu_cd_settingテーブルからデータを取得（カード種別プルダウンリストの値）
			SystemKanriCategoryLogic common = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			cardList = common.loadNaibuCdSetting("houjin_card_shubetsu");
			cardShubetsuDomain = EteamCommon.mapList2Arr(cardList, "naibu_cd");

			//カード種別を保存する
			hcLogic.insertCardShubetsu(super.getUser().getLoginUserId(), cardShubetsu);
			connection.commit(); // アップロードの成否に関わらず対象カードは保存しておく。

			// 入力チェック
			hissuCheck(2);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			EteamCommon.uploadFileSizeCheck();
			if (! errorList.isEmpty())
			{
				return "error";
			}

			//法人カード種別ごとの処理実施
			HoujinCardCSVUploadLogic hcuLogic = HoujinCardCSVUploadLogic.getInstance(cardShubetsu,connection);
			hcuLogic.uploadFileFileName = this.uploadFileFileName;

			//各ファイル読み取り
			hcuLogic.read(uploadFile,this.errorList);
			if (! errorList.isEmpty())
			{
				return "error";
			}

			//登録用情報チェック
			fileContentCheck(hcuLogic.getSeigyoData());
			if (! errorList.isEmpty())
			{
				return "error";
			}

			//セッション情報作成
			HoujinCardCSVUploadSessionInfo sessionInfo = new HoujinCardCSVUploadSessionInfo(this.uploadFileFileName,hcuLogic.getSeigyoData(),this.errorList,cardShubetsu,hcuLogic.getHyoujiHeader(),hcuLogic.getHyoujiData());
			if (! errorList.isEmpty())
			{
				return "error";
			}

			//処理結果をセションに保存します。
			session.put(Sessionkey.HOUJINCARD_CSV, sessionInfo);

			// 戻り値を返す
			return "success";
		}
	}


	/**
	 * コネクション設定を行います。
	 * @param connection DBコネクション
	 */
	protected void setConnection(EteamConnection connection) {
		hcLogic = EteamContainer.getComponent(HoujinCardLogic.class, connection);
	}


	/**
	 * CSVファイル情報をチェックします。
	 * @param rirekiList CSVファイル情報
	 */
	protected void fileContentCheck(List<HoujinCardCSVUploadInfoBase> rirekiList) {

		//必須項目チェック
		for (int i = 0; i < rirekiList.size(); i++) {
			//フォーマットに関わらず必須項目があればOKとする
			HoujinCardCSVUploadInfoBase rireki = rirekiList.get(i);
			String ip = "#" + rireki.getNumber() +":";
			checkHissu(rireki.getCardShubetsu(), ip + "カード種別");
			checkHissu(rireki.getCardBangou(), ip + "カード番号");
			checkHissu(rireki.getRiyoubi(), ip + "利用日");
			checkHissu(rireki.getBushoCd(), ip + "部署コード");
			checkHissu(rireki.getShainNo(), ip + "社員No");
			checkHissu(rireki.getShiyousha(), ip + "使用者");
			checkHissu(rireki.getKingaku(), ip + "金額");
			checkHissu(rireki.getKameiten(), ip + "加盟店");
			checkHissu(rireki.getGyoushuCd(), ip + "業種コード");
		}
		//形式のチェック
		for (int i = 0; i < rirekiList.size(); i++) {
			HoujinCardCSVUploadInfoBase rireki = rirekiList.get(i);
			String ip = "#" + rireki.getNumber() +":";

			// 項目							//項目名			//キー項目？
			checkString (rireki.getCardBangou(), 1, 16, ip + "カード番号", false);
			checkString (rireki.getBushoCd(), 1, 15, ip + "部署コード", false);
			checkString (rireki.getShainNo(), 1, 20, ip + "社員No", false);  //20220620 maxを20に変更
			checkString (rireki.getShiyousha(), 1, 30, ip + "使用者", false);
			checkString (rireki.getKameiten(), 1, 60, ip + "加盟店", false);
			checkString (rireki.getGyoushuCd(), 1, 15, ip + "業種コード", false);

			//カード種別はドメインでチェック
			checkDomain(rireki.getCardShubetsu(), cardShubetsuDomain, "カード種別", false);
			//利用日 日付型への変換が可能か(yyyyMMdd)
			checkYYYYMMDD (rireki.getRiyoubi(), ip + "利用日", false);
			//金額 BigDecimal型への変換が可能か
			checkMoney (rireki.getKingaku(), ip + "金額");
		}

	}


	/**
	 * 金額形式チェックして、不正ならエラーメッセージ詰める。
	 * @param s 入力値
	 * @param name 項目名(エラーメッセージ用)
	 */
	protected void checkMoney(String s, String name) {
		try{
			super.checkKingakuMinus(s, name, true);
		}catch(EteamIllegalRequestException e){
			errorList.add(name + "は数値に変換できる値を入力してください。");
		}
	}

}
