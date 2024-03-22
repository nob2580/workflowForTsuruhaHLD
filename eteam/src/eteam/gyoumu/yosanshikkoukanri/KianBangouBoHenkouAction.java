package eteam.gyoumu.yosanshikkoukanri;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 起案番号簿変更画面Action
 */
@Getter @Setter @ToString
public class KianBangouBoHenkouAction extends EteamAbstractAction { 

//＜定数＞

//＜画面入力＞
	/** 所属部門コード */
	String bumonCd;
	/** 所属部門名 */
	String bumonName;
	/** 年度 */
	String nendo;
	/** 略号 */
	String ryakugou;
	/** 開始起案番号 */
	String kianbangouFrom;
	/** 終了起案番号 */
	String kianbangouTo;
	/** 区分内容 */
	String kbnNaiyou;
	/** 採番時表示 */
	String saibanjiHyouji;
	/** 検索時表示 */
	String kensakujiHyouji;

//＜画面入力以外＞

	/** デフォルト設定件数 */
	String defaultCnt = "0";

	/** 更新後のウィンドウクローズフラグ(onLoad) */
	boolean successful = false;

	//ドロップダウン等
	/** 起案番号簿表示のDropDownList */
	List<GMap> saibanjiHyoujiList;
	/** 伝票検索表示のDropDownList */
	List<GMap> kensakujiHyoujiList;

//部品
	/** 起案番号簿登録変更ロジック */
	protected KianBangouBoHenkouLogic myLogic;
	/** システム管理ロジック */
	protected SystemKanriCategoryLogic sysLogic;
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(KianbangouBoIchiranAction.class);

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		// 処理なし
	}

	@Override
	protected void hissuCheck(int eventNum) {
		// 処理なし
	}

//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {

		// 起案番号簿変更Logic
		myLogic = EteamContainer.getComponent(KianBangouBoHenkouLogic.class, connection);
		// システムカテゴリー内のSelect文を集約したLogic
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
	}

	/**
	 * 共通制御処理<br>
	 * 検索イベントやエラー表示時用に画面の共通制御を行う。<br>
	 */
	protected void displaySeigyo() {

		/*
		 * プルダウンリストの要素を取得する。
		 */

		// 採番時表示
		saibanjiHyoujiList = sysLogic.loadNaibuCdSetting("kian_bangou_bo_sentaku_hyouji");

		// 検索時表示
		kensakujiHyoujiList = sysLogic.loadNaibuCdSetting("denpyou_kensaku_hyouji");
	}

//＜イベント＞
	/**
	 * 変更初期表示イベント
	 * 起案番号簿から同一キーデータを取得して画面表示する。
	 * 
	 * @return 処理結果
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()){
			this.initParts(connection);

			// 共通制御処理
			this.displaySeigyo();

			/*
			 * 1.入力チェック
			 */
			// なし

			/*
			 * 2.データ存在チェック
			 */
			int fromNum = Integer.valueOf(this.kianbangouFrom).intValue();

			// 起案番号簿からレコードを取得する。
			GMap maptExistData = myLogic.findKianbangouBoByPk(this.bumonCd, this.nendo, this.ryakugou, fromNum);
			if (null == maptExistData){
				super.errorList.add("対象の起案番号簿データは既に削除されている、もしくは存在しません。");
				return "error";
			}

			/*
			 * 3.アクセス可能チェック なし
			 */
			// なし

			/*
			 * 4.処理 
			 */

			// 画面項目へ移送する。

			// 所属部門コード
			this.bumonCd = maptExistData.get("bumon_cd").toString();
			// 所属部門名
			this.bumonName = maptExistData.get("bumon_name").toString();
			// 年度
			this.nendo = maptExistData.get("nendo").toString();
			// 略号
			this.ryakugou = maptExistData.get("ryakugou").toString();
			// 開始起案番号
			this.kianbangouFrom = maptExistData.get("kian_bangou_from").toString();
			// 終了起案番号
			this.kianbangouTo = maptExistData.get("kian_bangou_to").toString();
			// 区分内容
			this.kbnNaiyou = maptExistData.get("kbn_naiyou").toString();
			// 採番時表示
			this.saibanjiHyouji = maptExistData.get("kianbangou_bo_sentaku_hyouji_flg").toString();
			// 検索時表示
			this.kensakujiHyouji = maptExistData.get("denpyou_kensaku_hyouji_flg").toString();

			// 5.戻り値を返す
			return "success";

		}
	}
	/**
	 * 更新イベント
	 * 起案番号簿の同一キーデータを更新する。<br>
	 * 
	 * @return 処理結果
	 */
	public String koushin(){
		try(EteamConnection connection = EteamConnection.connect()){
			this.initParts(connection);

			// 共通制御処理
			this.displaySeigyo();

			/*
			 * 1.入力チェック
			 */
			// なし

			/*
			 * 2.データ存在チェック
			 */
			int fromNum = Integer.valueOf(this.kianbangouFrom).intValue();

			// 起案番号簿からレコードを取得する。
			GMap maptExistData = myLogic.findKianbangouBoByPk(this.bumonCd, this.nendo, this.ryakugou, fromNum);
			if (null == maptExistData){
				super.errorList.add("対象の起案番号簿データは既に削除されている、もしくは存在しません。");
				return "error";
			}

			// 採番時表示欄がデフォルト以外の指定時チェック
			if (!EteamNaibuCodeSetting.KIAN_BANGOU_BO_SENTAKU_HYOUJI.DEFAULT.equals(this.saibanjiHyouji)){
				List<GMap> lstDefaultData = this.myLogic.findKianbangouBoByDefault(this.bumonCd, this.nendo, this.ryakugou, fromNum, false);
				if (0 == lstDefaultData.size()){
					super.errorList.add("同一所属部門で採番時表示にデフォルト指定したデータが存在しません。デフォルト設定で登録してください。");
					return "error";
				}
			}

			// 採番時表示欄がデフォルト指定時の処理
			else{
				// 同一所属部門で自身以外にデフォルト設定したレコードが存在する場合、
				// 起案番号簿選択表示フラグ値を変更する。
				List<GMap> lstDefaultData = this.myLogic.findKianbangouBoByDefault(this.bumonCd, this.nendo, this.ryakugou, fromNum, true);
				for (GMap aMap : lstDefaultData){
					String updBumonCd = aMap.get("bumon_cd").toString();
					String updNendo = aMap.get("nendo").toString();
					String updRyakugou = aMap.get("ryakugou").toString();
					int updFromNum = ((Integer)aMap.get("kian_bangou_from")).intValue();
					String updKianHyoujiFlg = EteamNaibuCodeSetting.KIAN_BANGOU_BO_SENTAKU_HYOUJI.YES;
					String updDenpyouHyoujiFlg = aMap.get("denpyou_kensaku_hyouji_flg").toString();
					this.myLogic.updateKianbangouBoForFlg(updBumonCd, updNendo, updRyakugou, updFromNum, updKianHyoujiFlg, updDenpyouHyoujiFlg);
				}
			}

			/*
			 * 3.アクセス可能チェック なし
			 */
			// なし

			/*
			 * 4.処理 
			 */

			// 起案番号管理簿のレコードを更新する。
			this.myLogic.updateKianbangouBoForFlg(this.bumonCd, this.nendo, this.ryakugou, fromNum, this.saibanjiHyouji, this.kensakujiHyouji);
			connection.commit();

			// 5.戻り値を返す
			this.successful = true;
			return "success";

		}
	}
}