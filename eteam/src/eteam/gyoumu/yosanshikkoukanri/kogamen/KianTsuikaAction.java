package eteam.gyoumu.yosanshikkoukanri.kogamen;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.gyoumu.kihyounavi.DenpyouKanriLogic;
import eteam.gyoumu.yosanshikkoukanri.KianBangouBoHenkouLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 起案追加ダイアログAction
 */
@Getter @Setter @ToString
public class KianTsuikaAction extends EteamAbstractAction {

//＜定数＞
	/** 採番ボタン押下時に引き渡す複数情報のセパレータ文字 */
	protected final String SEPARATOR_STRING = "@";

//＜画面入力＞
	/** [検索条件]起案番号簿 */
	String kensakuKianbangouBo;
	/** [検索条件]起案番号 */
	String kensakuKianbangou;
	/** [検索条件]件名 */
	String kensakuKenmei;
	/** [検索条件]伝票種別 */
	String kensakuDenpyouShubetsu;
	/** [検索条件]起案日(from) */
	String kensakuKianBiFrom;
	/** [検索条件]起案日(to) */
	String kensakuKianBiTo;
	/** [検索条件]最終承認日(from) */
	String kensakuSaishuuShouninBiFrom;
	/** [検索条件]最終承認日(to) */
	String kensakuSaishuuShouninBiTo;
	/** [検索条件]起票部門（非表示） */
	String kensakuBumonCd;
	/** 選択チェックボックス */
	String sentaku;
	
//＜画面入力以外＞
	/** 伝票の伝票区分 */
	String denpyouKbn;
	/** 伝票区分から求めた予算執行対象 */
	String yosanShikkouTaishou;

	//ドロップダウン等
	/** 起案番号簿のDropDownList */
	List<GMap> kianbangouBoList;
	/** 伝票種別のDropDownList */
	List<GMap> kensakuDenpyouShubetsuList;

//表示
	/** 検索結果リスト */
	List<GMap> list;

//＜制御＞
	/** 起案番号簿変更Logic */
	protected KianBangouBoHenkouLogic kianBangouBoHenkouLogic;
	/** 通知カテゴリー内のSelect文を集約したLogic */
	protected TsuuchiCategoryLogic tsuuchiLogic;
	/** 伝票管理Logic */
	protected DenpyouKanriLogic denpyouKanriLogic;
	/** 起案追加ロジック */
	protected KianTsuikaLogic myLogic;
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(KianTsuikaAction.class);

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		// 子画面選択およびプルダウンリスト以外の入力項目をチェック対象とする。
		checkNumberRange3(this.kensakuKianbangou, 1, EteamConst.kianBangou.MAX_VALUE, "起案番号", false);
		checkDate(this.kensakuKianBiFrom, "起案日(from)", false);
		checkDate(this.kensakuKianBiTo, "起案日(to)", false);
		checkDate(this.kensakuSaishuuShouninBiFrom, "最終承認日(from)", false);
		checkDate(this.kensakuSaishuuShouninBiTo, "最終承認日(to)", false);
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
		// 起案番号簿変更Logic
		kianBangouBoHenkouLogic = EteamContainer.getComponent(KianBangouBoHenkouLogic.class, connection);
		// 通知カテゴリー内のSelect文を集約したLogic
		tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
		// 伝票管理Logic
		denpyouKanriLogic = EteamContainer.getComponent(DenpyouKanriLogic.class, connection);
		// 起案追加Logic
		myLogic = EteamContainer.getComponent(KianTsuikaLogic.class, connection);
	}
	
	/**
	 * 共通制御処理<br>
	 * 検索イベントやエラー表示時用に画面の共通制御を行う。<br>
	 */
	protected void displaySeigyo() {

		/*
		 * プルダウンリストの要素を取得する。
		 */

		// 起案番号簿
		this.kianbangouBoList = this.myLogic.loadKianbangouBoList(super.getUser(), kensakuBumonCd);
		// 伝票種別
		this.kensakuDenpyouShubetsuList = tsuuchiLogic.loadDenpyouShubetsu();
	}
	
//＜イベント＞
	/**
	 * 初期表示イベント
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

			// 伝票区分
			if (!isEmpty(this.denpyouKbn)){
				// 伝票区分から予算執行対象を取得する。
				this.yosanShikkouTaishou = this.denpyouKanriLogic.getYosanShikkouTaishou(this.denpyouKbn);
			}

			// 初期検索を実施する。
			String result = this.kensaku();

			// 5.戻り値を返す
			return result;

		}
	}
	
	/**
	 * 検索イベント
	 * @return 処理結果
	 */
	public String kensaku(){
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);

			// 共通制御処理
			displaySeigyo();

			/*
			 * 1.入力チェック
			 */
			this.formatCheck();
			if (!errorList.isEmpty()) {
				return "error";
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

			// 検索条件を指定する。
			KianTsuikaLogic.SearchCondition cond = new KianTsuikaLogic.SearchCondition();
			// 予算執行対象
			if (!isEmpty(this.yosanShikkouTaishou)){
				cond.yosanShikkouTaishou = this.yosanShikkouTaishou;
			}
			// 起案番号簿
			if (!isEmpty(this.kensakuKianbangouBo)){
				String[] kianbangouBoKeys = this.kensakuKianbangouBo.split(SEPARATOR_STRING);
				cond.boBumonCd = kianbangouBoKeys[0];
				cond.boNendo = kianbangouBoKeys[1];
				cond.boRyakugou = kianbangouBoKeys[2];
				cond.boKianbangouFrom = Integer.valueOf(kianbangouBoKeys[3]);
			}
			// 起案番号
			if (!isEmpty(this.kensakuKianbangou)){
				cond.kianbangou = Integer.valueOf(this.kensakuKianbangou);
			}
			// 件名
			if (!isEmpty(this.kensakuKenmei)){
				cond.kenmei = this.kensakuKenmei;
			}
			// 伝票種別
			if (!isEmpty(this.kensakuDenpyouShubetsu)){
				cond.denpyouShubetsu = this.kensakuDenpyouShubetsu;
			}
			// 起案日(from)
			if (!isEmpty(this.kensakuKianBiFrom)){
				cond.kianBiFrom = super.toDate(this.kensakuKianBiFrom);
			}
			// 起案日(to)
			if (!isEmpty(this.kensakuKianBiTo)){
				cond.kianBiTo = super.toDate(this.kensakuKianBiTo);
			}
			// 最終承認日(from)
			if (!isEmpty(this.kensakuSaishuuShouninBiFrom)){
				cond.saishuuShouninBiFrom = super.toDate(this.kensakuSaishuuShouninBiFrom);
			}
			// 最終承認日(to)
			if (!isEmpty(this.kensakuSaishuuShouninBiTo)){
				cond.saishuuShouninBiTo = super.toDate(this.kensakuSaishuuShouninBiTo);
			}
			// 起案部門
			cond.kihyouBumonCd = kensakuBumonCd;
log.debug("検索条件=" + cond.toDebugString());

			// 検索を実施する。
			this.list = this.myLogic.kensaku(cond);
			if (this.list.isEmpty()) {
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			// 取得した検索結果を加工する。
			for (GMap aMap : this.list){
				// 起案番号は予算執行対象により対象カラムが異なるので切り替える。
				String resultYosanShikkouTaishou = aMap.get("yosan_shikkou_taishou").toString();
				if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(resultYosanShikkouTaishou)
				 || EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI.equals(resultYosanShikkouTaishou)){
					// 実施起案
					aMap.put("kianbangou", aMap.get("jisshi_kian_bangou"));
				}
				if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(resultYosanShikkouTaishou)){
					// 支出起案
					aMap.put("kianbangou", aMap.get("shishutsu_kian_bangou"));
				}
				// 起案日はyyyy/MM/dd形式に編集する。
				String strKihyouBi = super.formatDate(new Date(((Timestamp)aMap.get("kihyouBi")).getTime()));
				aMap.put("kihyouBi", strKihyouBi);
				// 最終承認日はyyyy/MM/dd形式に編集する。
				String strSaishuuShouninBi = super.formatDate(new Date(((Timestamp)aMap.get("saishuuShouninBi")).getTime()));
				aMap.put("saishuuShouninBi", strSaishuuShouninBi);

				// 取得したデータを整形する（起案追加されている伝票も対象）。
				this.myLogic.formatKianDenpyou(aMap);
			}

			// 5.戻り値を返す
			return "success";

		}
	}
	
	/**
	 * 採番イベント<br>
	 * 起案追加する伝票を選択して追加ボタンを押下したかチェックする。
	 * 
	 * @return 処理結果
	 */
	public String tsuika(){
		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);

			// 共通制御処理
			displaySeigyo();

			/*
			 * 1.入力チェック
			 */
			if (sentaku == null || sentaku.isEmpty()) {
				// エラー時の再表示用に一覧を再検索する。
				this.kensaku();

				errorList.add("添付伝票を1つ以上選択してください。");
				return "error";
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
			// なし

			// 5.戻り値を返す
			return "success";

		}
	}
}
