package eteam.gyoumu.yosanshikkoukanri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.select.SystemKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 起案番号簿一覧画面Action
 */
@Getter @Setter @ToString
public class KianbangouBoIchiranAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	/** [検索条件]所属部門コード */
	String kensakuBumonCd="";
	/** [検索条件]所属部門名 */
	String kensakuBumonName="";
	/** [検索条件]年度 */
	String kensakuNendo = "";
	/** [検索条件]区分内容 */
	String kensakuKbnNaiyou = "";
	/** [検索条件]略号 */
	String kensakuRyakugou = "";
	/** [検索条件]採番時表示 */
	String kensakuSaibanjiHyouji = "";
	/** [検索条件]検索時表示 */
	String kensakuKensakujiHyouji = "";

	/** 複製部門コード */
	String bumonCd="";
	/** 複製年度 */
	String nendo = "";
	/** 複製略号 */
	String ryakugou = "";
	/** 複製開始起案番号 */
	String kianbangouFrom = "";
	/** 複製年度 */
	String copyNendo = "";
	
	/** 再表示用URL */
	String urlParam = "";

//＜画面入力以外＞
	//ドロップダウン等
	/** 起案番号簿表示のDropDownList */
	List<GMap> kensakuSaibanjiHyoujiList;
	/** 伝票検索表示のDropDownList */
	List<GMap> kensakuKensakujiHyoujiList;

//表示
	/** 検索結果リスト */
	List<GMap> list;

//部品
	/** 起案番号簿一覧ロジック */
	protected KianbangouBoIchiranLogic myLogic;
	/** システム管理ロジック */
	protected SystemKanriCategoryLogic sysLogic;
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(KianbangouBoIchiranAction.class);

//＜入力チェック＞
	@Override protected void formatCheck() {
		checkNumber(kensakuNendo, 1, 4, "年度", false);
	}

	@Override protected void hissuCheck(int eventNum) {}

//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {

		// 起案番号簿一覧Logic
		myLogic = EteamContainer.getComponent(KianbangouBoIchiranLogic.class, connection);
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

		// 起案番号簿表示
		kensakuSaibanjiHyoujiList = sysLogic.loadNaibuCdSetting("kian_bangou_bo_sentaku_hyouji");

		// 伝票検索表示
		kensakuKensakujiHyoujiList = sysLogic.loadNaibuCdSetting("denpyou_kensaku_hyouji");
	}

//＜イベント＞
	/**
	 * 検索イベント
	 * @return 処理結果
	 */
	public String kensaku(){
		try(EteamConnection connection = EteamConnection.connect()){
			this.initParts(connection);

			// 共通制御処理
			this.displaySeigyo();

			/*
			 * 1.入力チェック
			 */
			this.formatCheck();
			this.hissuCheck(2);
			if(! super.errorList.isEmpty()){
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

			this.searchCondSet();
			if(this.list.isEmpty()){
				super.errorList.add("検索結果が0件でした。");
				return "error";
			}

			// 5.戻り値を返す
			return "success";

		}
	}

	/**
	 * 複製イベント
	 * @return 処理結果
	 */
	public String fukusei(){
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
			// なし

			/*
			 * 3.アクセス可能チェック なし
			 */
			// なし

			/*
			 * 4.処理 
			 */
			boolean isError = false;
			int iKianbangouFrom = Integer.valueOf(this.kianbangouFrom).intValue();

			// コピー元となる起案番号簿レコードを取得する。
			GMap mapKianbangouBo = this.myLogic.findKianbangouBo(this.bumonCd, this.nendo, this.ryakugou, iKianbangouFrom);
			if (null == mapKianbangouBo){
				super.errorList.add("対象の起案番号簿データは既に削除されている、もしくは存在しません。");
				isError = true;
			}

			// コピー先となる起案番号簿レコードを取得する。
			if (!isError){
				GMap mapTmp = this.myLogic.findKianbangouBo(this.bumonCd, this.copyNendo, this.ryakugou, iKianbangouFrom);
				if (null != mapTmp){
					super.errorList.add("複製する起案番号簿データは既に存在しています。");
					isError = true;
				}
			}

			//検索条件のURLを作成
			try {
				urlParam = "kensakuBumonCd=" + URLEncoder.encode((kensakuBumonCd == null ? "" : kensakuBumonCd.toString()),"UTF-8")
					 	 + "&kensakuBumonName=" + URLEncoder.encode((kensakuBumonName == null ? "" : kensakuBumonName.toString()),"UTF-8")
						 + "&kensakuNendo=" + URLEncoder.encode((kensakuNendo == null ? "" : kensakuNendo.toString()),"UTF-8")
						 + "&kensakuRyakugou=" + URLEncoder.encode((kensakuRyakugou == null ? "" : kensakuRyakugou.toString()),"UTF-8")
					 	 + "&kensakuKbnNaiyou=" + URLEncoder.encode((kensakuKbnNaiyou == null ? "" : kensakuKbnNaiyou.toString()),"UTF-8")
						 + "&kensakuSaibanjiHyouji=" + URLEncoder.encode((kensakuSaibanjiHyouji == null ? "" : kensakuSaibanjiHyouji.toString()),"UTF-8")
						 + "&kensakuKensakujiHyouji=" + URLEncoder.encode((kensakuKensakujiHyouji == null ? "" : kensakuKensakujiHyouji.toString()),"UTF-8");

			} catch (UnsupportedEncodingException e) {
			    throw new RuntimeException(e);
			}
			// 起案番号簿を複製する。
			if (!isError){
				this.myLogic.copyKianbangouBo(this.bumonCd, this.nendo, this.ryakugou, iKianbangouFrom, this.copyNendo);
				connection.commit();
				return "success";
			}else{
				//エラー時の条件で再表示
				this.searchCondSet();
				return "error";
			}

		}
	}
	
	/**
	 * 検索条件の設定・検索を実行する。
	 */
	protected void searchCondSet() {
		// 画面で指定された検索条件を格納する。
		KianbangouBoIchiranLogic.SearchCondition cond = new KianbangouBoIchiranLogic.SearchCondition();
		// ログインユーザ情報
		cond.loginUser = super.getUser();
		// 所属部門コード
		if (!isEmpty(this.kensakuBumonCd)){
			cond.bumonCd = this.kensakuBumonCd;
		}
		// 年度
		if (!isEmpty(this.kensakuNendo)){
			cond.nendo = this.kensakuNendo;
		}
		// 略号
		if (!isEmpty(this.kensakuRyakugou)){
			cond.ryakugou = this.kensakuRyakugou;
		}
		// 区分内容
		if (!isEmpty(this.kensakuKbnNaiyou)){
			cond.kbnNaiyou = this.kensakuKbnNaiyou;
		}
		// 採番時表示コード
		if (!isEmpty(this.kensakuSaibanjiHyouji)){
			cond.saibanjiHyouji = new String[] {this.kensakuSaibanjiHyouji};
		}
		// 検索時表示コード
		if (!isEmpty(this.kensakuKensakujiHyouji)){
			cond.kensakuHyouji = this.kensakuKensakujiHyouji;
		}
		
		// 呼出元判別区分(ソート順設定用)
		cond.yobidashiHanbetsu = 2;
		
log.debug("検索条件=" + cond.toDebugString());

		// 検索を行う。
		this.list = this.myLogic.kensaku(cond);
	}
}
