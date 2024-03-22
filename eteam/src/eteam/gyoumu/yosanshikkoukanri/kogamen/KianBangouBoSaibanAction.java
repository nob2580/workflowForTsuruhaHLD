package eteam.gyoumu.yosanshikkoukanri.kogamen;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.gyoumu.yosanshikkoukanri.KianBangouBoHenkouLogic;
import eteam.gyoumu.yosanshikkoukanri.KianbangouBoIchiranLogic;
import eteam.gyoumu.yosanshikkoukanri.YosanShikkouKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 起案番号採番ダイアログAction
 */
@Getter @Setter @ToString
public class KianBangouBoSaibanAction extends EteamAbstractAction {

//＜定数＞
	/** 採番ボタン押下時に引き渡す複数情報のセパレータ文字 */
	protected final String SEPARATOR_STRING = "@";

//＜画面入力＞
	/** 起票部門 */
	String kensakuBumonCd;
	/** 選択チェックボックス */
	String sentaku;
	/** 伝票ID */
	String denpyouId;
	
//＜画面入力以外＞

	// 採番ボタン押下時の選択情報
	/** 起案番号簿選択ダイアログ選択値（部門コード） */
	String sentakuBumonCd;
	/** 起案番号簿選択ダイアログ選択値（年度） */
	String sentakuNendo;
	/** 起案番号簿選択ダイアログ選択値（略号） */
	String sentakuRyakugou;
	/** 起案番号簿選択ダイアログ選択値（開始起案番号） */
	String sentakuKianbangouFrom;

//表示
	/** 検索結果リスト */
	List<GMap> list;

//＜制御＞
	/** 入力チェックエラー有無 */
	boolean isError = false;

	/** 部門・ユーザー管理カテゴリー内のSelect文を集約したLogic */
	protected BumonUserKanriCategoryLogic bumonUserKanriCategoryLogic;
	/** 通知カテゴリー内のSelect文を集約したLogic */
	protected TsuuchiCategoryLogic tsuuchiLogic;
	/** 起案番号簿変更Logic */
	protected KianBangouBoHenkouLogic kianBangouBoHenkouLogic;
	/** 起案番号簿一覧ロジック */
	protected KianbangouBoIchiranLogic kianbangouBoIchiranLogic;
	/** 予算執行カテゴリロジック */
	protected YosanShikkouKanriCategoryLogic yosanShikkouKanriLogic;
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(KianBangouBoSaibanAction.class);

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		// 処理なし
	}
	
	@Override
	protected void hissuCheck(int eventNum) {
		// 処理なし
	}

	/**
	 * 部品の初期化
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		// 部門・ユーザー管理カテゴリー内のSelect文を集約したLogic
		bumonUserKanriCategoryLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		// 通知カテゴリー内のSelect文を集約したLogic
		tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
		// 起案番号簿変更Logic
		kianBangouBoHenkouLogic = EteamContainer.getComponent(KianBangouBoHenkouLogic.class, connection);
		// 起案番号簿一覧Logic
		kianbangouBoIchiranLogic = EteamContainer.getComponent(KianbangouBoIchiranLogic.class, connection);
		// 予算執行カテゴリロジック
		yosanShikkouKanriLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, connection);
		
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * 
	 * @return 処理結果
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);

			/*
			 * 1.入力チェック
			 */
			// なし
			
			/*
			 * 2.データ存在チェック
			 */
			// なし

			/*
			 * 3.アクセス可能チェック なし
			 */
			// なし

			/*
			 * 4.処理 
			 */

			// 検索条件を指定する。
			KianbangouBoIchiranLogic.SearchCondition cond = new KianbangouBoIchiranLogic.SearchCondition();
			// ログインユーザ情報
			cond.loginUser = super.getUser();
			// 採番時表示コード（表示対象指定）
			cond.saibanjiHyouji = new String[] {EteamNaibuCodeSetting.KIAN_BANGOU_BO_SENTAKU_HYOUJI.YES
											   ,EteamNaibuCodeSetting.KIAN_BANGOU_BO_SENTAKU_HYOUJI.DEFAULT};
			// 起票部門
			cond.kihyouBumonCd = kensakuBumonCd;
			
			// 呼出元判別区分(ソート順設定用)
			cond.yobidashiHanbetsu = 1;
			
log.debug("検索条件=" + cond.toDebugString());

			// 初期検索を実施する。
			this.list = this.kianbangouBoIchiranLogic.kensaku(cond);

			// 選択用にユニークキーを追加する。
			boolean isSettingDefault = false;
			for (GMap aMap : this.list){
				StringBuilder sbKey = new StringBuilder();
				sbKey.append(aMap.get("bumonCd").toString()).append("@");
				sbKey.append(aMap.get("nendo").toString()).append("@");
				sbKey.append(aMap.get("ryakugou").toString()).append("@");
				sbKey.append(aMap.get("kianbangouFrom").toString());
				aMap.put("sentakuKey", sbKey.toString());

				// デフォルト選択を指定する。
				if (!isError){
					// エラーでの再表示ではないときに実施する。
					if (!isSettingDefault){
						// 起案番号簿選択表示フラグを取得する。
						String flag = aMap.get("kianbangou_bo_sentaku_hyouji_flg").toString();
						// フラグが "デフォルト" 指定の場合、これを使用する。
						if (EteamNaibuCodeSetting.KIAN_BANGOU_BO_SENTAKU_HYOUJI.DEFAULT.equals(flag)){
							this.sentaku = aMap.get("sentakuKey").toString();
							isSettingDefault = true;
						}
					}
				}
			}
			this.isError = false;

			// 5.戻り値を返す
			return "success";

		}
	}
	
	/**
	 * 採番イベント
	 * @return 処理結果
	 */
	public String saiban(){
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);

			/*
			 * 1.入力チェック
			 */
			if (sentaku == null || sentaku.isEmpty()) {
				// エラー時の再表示用に一覧を再検索する。
				this.isError = true;
				init();

				errorList.add("起案番号簿を1つ以上選択してください。");
				return "error";
			}
			
			// 選択情報をセパレータ文字で要素分解する。
			String[] sentakuArrays = sentaku.split(this.SEPARATOR_STRING);
			String pBumonCd = sentakuArrays[0];
			String pNendo = sentakuArrays[1];
			String pRyakugou = sentakuArrays[2];
			int pKianbangouFrom = Integer.valueOf(sentakuArrays[3]);
			
			// 予算執行対象月との相関チェック
			if(isNotEmpty(denpyouId)) {
				String yosanCheckNengetsu = tsuuchiLogic.findDenpyou(denpyouId).getString("yosan_check_nengetsu");
				if(isNotEmpty(yosanCheckNengetsu)) {
					
					// 予算執行対象月が属する年度の開始日
					Date fromDate = yosanShikkouKanriLogic.retYosanCheckNengetsuFromDate(yosanCheckNengetsu);
					SimpleDateFormat format = new SimpleDateFormat("YYYY");
					
					if (false == pNendo.equals(format.format(fromDate))) {
						errorList.add("選択された起案番号簿の年度が予算執行対象月と異なります。");
						
						// 再表示用に一覧を再検索
						this.isError = true;
						init();
						
						return "error";
					}
				}
			}

			/*
			 * 2.データ存在チェック
			 */
			// なし

			/*
			 * 3.アクセス可能チェック なし
			 */
			// なし

			/*
			 * 4.処理 
			 */


			// 採番可能かをチェックする。
			// ・起案番号部署管理から終了起案番号を取得
			// ・起案番号管理から起案番号を取得
			// ・起案番号と終了起案番号が同じ場合は発番不可のためエラーにする
			GMap mapBo = this.kianBangouBoHenkouLogic.findKianbangouBoByPk(pBumonCd, pNendo, pRyakugou, pKianbangouFrom);
			int kianbangouTo = (int) mapBo.get("kian_bangou_to");
			GMap mapSaiban = this.kianBangouBoHenkouLogic.findKianbangouSaibanByPk(pBumonCd, pNendo, pRyakugou, pKianbangouFrom);
			if (null != mapSaiban){
				int kianbangouLast = (int) mapSaiban.get("kian_bangou_last");
				if (kianbangouLast == kianbangouTo){
					this.isError = true;
					init();

					errorList.add("選択した番号簿は最終番号まで採番済みの為、採番できません。");
					return "error";
				}
			}

			// 起案番号簿選択ダイアログ選択値（部門コード）
			this.sentakuBumonCd = pBumonCd;
			// 起案番号簿選択ダイアログ選択値（年度）
			this.sentakuNendo = pNendo;
			// 起案番号簿選択ダイアログ選択値（略号）
			this.sentakuRyakugou = pRyakugou;
			// 起案番号簿選択ダイアログ選択値（開始起案番号）
			this.sentakuKianbangouFrom = String.valueOf(pKianbangouFrom);

			// 5.戻り値を返す
			return "success";

		}
	}
}
