package eteam.gyoumu.kihyounavi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import org.apache.commons.collections.Factory;
//import org.apache.commons.collections.ListUtils;
import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.RegAccess;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.kanitodoke.KaniTodokeLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票管理画面Action
 */
@Getter @Setter @ToString
public class DenpyouKanriAction extends EteamAbstractAction {

//＜定数＞
	/** 有効期限終了日省略時 */
	static final String YUUKOU_KIGEN_TO_DEFAULT = "9999/12/31";

//＜画面入力＞
	/** 伝票区分 */
	String[] denpyouKbn;
	/** 伝票種別 */
	String[] denpyouShubetsu;
	/** 伝票種別 （仮払なし）*/
	String[] denpyouKaribaraiNashiShubetsu;
	/** 伝票種別 （印刷）*/
	String[] denpyouPrintShubetsu;
	/** 伝票種別 （印刷・仮払なし）*/
	String[] denpyouPrintKaribaraiNashiShubetsu;
	/** 業務種別 */
	String[] gyoumuShubetsu;
	/** 内容 */
	String[] naiyou;
	/** 有効期限開始日 */
	String[] yuukouKigenFrom;
	/** 有効期限終了日 */
	String[] yuukouKigenTo;
	/** 関連伝票選択フラグ */
	String[] kanrenSentakuFlg;
	/** 関連伝票入力欄表示フラグ */
	String[] kanrenHyoujiFlg;
	/** 申請時帳票出力フラグ */
	String[] denpyouPrintFlg;
	/** 起案番号運用フラグ */
	String[] kianBangouUnyouFlg;
	/** 予算執行対象 */
	String[] yosanShikkouTaishou;
	/** ルート判定金額 */
	String[] routeHanteiKingaku;
	/** ルート取引毎設定フラグ */
	String[] routeTorihikiFlg;
	/** 承認状況欄印刷フラグ */
	String[] shouninJyoukyouPrintFlg;
	/** 申請者の処理権限名*/
	String[] shorikengen;
	/** 背景色 */
	String[] bgColor;
	/** 予算執行管理オプション */
	String yosanShikkouOption;
	/** 仕入先フラグ */
	String[] shiiresakiFlg;
	
//＜画面入力以外＞
	/** 予算執行対象プルダウンリスト（仮払申請向け） */
	List<GMap> yosanShikkouTaishouKaribaraiList;
	/** 予算執行対象プルダウンリスト（精算伝票向け） */
	List<GMap> yosanShikkouTaishouSeisanList;
	/** 予算執行対象プルダウンリスト（ユーザー定義届書向け） */
	List<GMap> yosanShikkouTaishouKanitodokeList;
	/** ルート判定金額プルダウンリスト */
	List<GMap> routeHanteiKingakuList;
	/** 起案番号運用（活性制御） */
	String[] kianBangouUnyouSeigyo;
	/** 予算執行対象（活性制御） */
	String[] yosanShikkouTaishouSeigyo;
	/** ルート判定金額（活性制御） */
	String[] routeHanteiKingakuSeigyo;
	/** ルート判定金額自体表示 */
	boolean routeHanteiDisp;

//＜部品＞
	/** 伝票管理Logic */
	DenpyouKanriLogic myLogic;
	/** システム管理ロジック */
	SystemKanriCategoryLogic sysLogic;
	/** 起票ナビカテゴリー内のSelect文を集約したLogic */
	KihyouNaviCategoryLogic kihyouNaviLogic;
	/** ワークフローイベント制御Logic */
	WorkflowEventControlLogic wfEventLogic;
	/** ユーザー定義届書Logic */
	KaniTodokeLogic kaniTodokeLogic;

//＜入力チェック＞
	@Override
	protected void formatCheck(){
		if (denpyouKbn != null)
		{
			for(int i = 0; i < denpyouKbn.length ; i++){
				int ip = i + 1;
				checkString(denpyouKbn[i], 1, 4, "伝票区分："		+ ip + "行目", true);
				checkString(gyoumuShubetsu[i], 1, 20, "業務種別："		+ ip + "行目", false);
				checkString(naiyou[i], 1, 160, "内容："			+ ip + "行目", false);
				checkDate(yuukouKigenFrom[i], "有効期限開始日："	+ ip + "行目", false);
				checkDate(yuukouKigenTo[i], "有効期限終了日："	+ ip + "行目", false);
				checkString(shorikengen[i], 1, 6, "申請者の処理権限名："	+ ip + "行目", false);
				//管理画面なのでチェックボックスとプルダウンの改竄checkまでは未実装
			}
		}
	}

	@Override
	protected void hissuCheck(int eventNum) {
		if (denpyouKbn != null)
		{
			for(int i = 0; i < denpyouKbn.length ; i++){
				int ip = i + 1;
				String[][] list1 = {
					//項目													必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
					{denpyouKbn[i], "伝票区分："		+ ip + "行目"	,"0", "2", },
					{gyoumuShubetsu[i], "業務種別："		+ ip + "行目"	,"0", "1", },
					{naiyou[i], "内容："			+ ip + "行目"	,"0", "1", },
					{yuukouKigenFrom[i],"有効期限開始日："	+ ip + "行目"	,"0", "1", },
					{shorikengen[i], "申請者の処理権限名："	+ ip + "行目"	,"0", "0", },
					//管理画面なのでチェックボックスとプルダウンの改竄checkまでは未実装
				};
				hissuCheckCommon(list1, eventNum);
			}
		}
	}

	/**
	 * 相関チェック<br>
	 * <ul>
	 * <li>有効期限の共通チェック</li>
	 * <li>予算執行対象組み合わせチェック（予算執行管理オプション有効時）</li>
	 * <li>ルート判定金額項目存在チェック（予算執行管理オプション有効時）</li>
	 * <li>予算執行対象変更チェック（予算執行管理オプション有効時）</li>
	 * </ul>
	 */
	protected void soukanCheck() {
		if(denpyouKbn != null){
			for(int i = 0; i < denpyouKbn.length ; i++){
				String gyo = "行" + (i+1) + "：";

				//有効期限終了日が省略されている場合
				String wkYuukouKigenTo = yuukouKigenTo[i];
				if(wkYuukouKigenTo == null || wkYuukouKigenTo.equals("")){
					wkYuukouKigenTo = YUUKOU_KIGEN_TO_DEFAULT;
				}

				/*
				 * 有効期限の共通チェック
				 */
				List<Map<String, String>> dateCheck = EteamCommon.kigenCheck(yuukouKigenFrom[i], wkYuukouKigenTo);
				for(Map<String, String> map :dateCheck){
					// 開始日と終了日の整合性チェックのみエラーとする。
					if("2".equals(map.get("errorCode"))){
						errorList.add(gyo + map.get("errorMassage"));
					}
				}

				/*
				 * 予算執行対象組み合わせチェック
				 * 予算執行管理オプションが有効のときに実施する。
				 */
				if (RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(yosanShikkouOption)){
					List<Map<String, String>> lstError = myLogic.chkPatternYosanShikkouTaishou(denpyouKbn[i], yosanShikkouTaishou[i], kianBangouUnyouFlg[i]);
					if (0 < lstError.size()){
						errorList.add(gyo + lstError.get(0).get("errorMassage"));
					}
				}

				/*
				 * ルート判定金額項目存在チェック
				 * 予算執行管理オプションが有効のときに実施する。
				 */
				if (RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(yosanShikkouOption)){
					List<Map<String, String>> lstError = myLogic.chkExistRouteHanteiKingaku(denpyouKbn[i], routeHanteiKingaku[i]);
					if (0 < lstError.size()){
						errorList.add(gyo + lstError.get(0).get("errorMassage"));
					}
				}

				/*
				 * 予算執行対象変更チェック
				 * 予算執行管理オプションが有効のときに実施する。
				 */
				if (RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(yosanShikkouOption)){
					List<Map<String, String>> lstError = myLogic.chkExistYosanShikkouTaishou(denpyouKbn[i], yosanShikkouTaishou[i]);
					if (0 < lstError.size()){
						errorList.add(gyo + lstError.get(0).get("errorMassage"));
					}
				}
			}
		}
	}

//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		// 伝票管理Logic
		myLogic = EteamContainer.getComponent(DenpyouKanriLogic.class, connection);
		// システムカテゴリー内のSelect文を集約したLogic
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		// 起票ナビカテゴリー内のSelect文を集約したLogic
		kihyouNaviLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		// ワークフローイベント制御Logic
		wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		// ユーザー定義届書Logic
		kaniTodokeLogic = EteamContainer.getComponent(KaniTodokeLogic.class, connection);
	}

	/**
	 * 共通制御処理<br>
	 * 画面表示イベントや登録等イベントのエラー表示時用に画面の共通制御を行う。<br>
	 * 
	 * @param connection コネクション
	 * @param dpkbnArray 伝票区分配列
	 */
	public void displaySeigyo(EteamConnection connection, String[] dpkbnArray) {

		/*
		 * 予算執行対象プルダウンリスト
		 */
		// 予算執行管理オプションが「予算執行管理Aあり」の場合に実施する。
		if (RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(yosanShikkouOption)){

			// 予算執行対象プルダウンリストを取得
			List<GMap> yosanShikkouTaishouList = sysLogic.loadNaibuCdSetting("yosan_shikkou_taishou");
			
			yosanShikkouTaishouKaribaraiList = new ArrayList<GMap>();
			yosanShikkouTaishouKanitodokeList = new ArrayList<GMap>();
			yosanShikkouTaishouSeisanList = new ArrayList<GMap>();
			for(GMap map : yosanShikkouTaishouList){
				switch(map.get("naibu_cd").toString()){
				case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN:
					yosanShikkouTaishouKaribaraiList.add(map); //仮払申請向け
					yosanShikkouTaishouKanitodokeList.add(map); //ユーザー定義届書向け
					break;
					
				case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN:
					yosanShikkouTaishouKanitodokeList.add(map); //ユーザー定義届書向け
					break;
					
				case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI:
					yosanShikkouTaishouSeisanList.add(map); //精算伝票向け
					break;
					
				default:
					//対象外はすべてのリストで必要
					yosanShikkouTaishouKaribaraiList.add(map);
					yosanShikkouTaishouKanitodokeList.add(map);
					yosanShikkouTaishouSeisanList.add(map);
					break;
				}
			}

			// 予算執行管理の対象項目に対して活性／非活性情報を設定
			kianBangouUnyouSeigyo = new String[dpkbnArray.length];
			yosanShikkouTaishouSeigyo = new String[dpkbnArray.length];
			for (int i = 0; i < dpkbnArray.length; i++) {
				kianBangouUnyouSeigyo[i] = myLogic.isActiveKianBangouUnyou(dpkbnArray[i]);
				yosanShikkouTaishouSeigyo[i] = myLogic.isActiveYosanShikkouTaishou(dpkbnArray[i]);
			}
		}

		/*
		 * ルート判定金額プルダウンリスト
		 */

		// リストを取得
		routeHanteiKingakuList = sysLogic.loadNaibuCdSetting("route_hantei_kingaku");

		// 活性／非活性情報を設定
		routeHanteiKingakuSeigyo = new String[dpkbnArray.length];
		for (int i = 0; i < dpkbnArray.length; i++) {
			routeHanteiKingakuSeigyo[i] = myLogic.isActiveRoueHanteiKingaku(dpkbnArray[i]);
			if (routeHanteiKingakuSeigyo[i].equals("1"))
			{
				routeHanteiDisp = true;
			}
		}
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){

		// 1.入力チェック
		formatCheck();
		hissuCheck(1);

		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);

			// 予算執行管理オプションを取得する。
			yosanShikkouOption = RegAccess.checkEnableYosanShikkouOption();

			// 2.データ存在チェック
			List<GMap> list = kihyouNaviLogic.loadDenpyouKanri();
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			// 3.アクセス可能チェック なし

			// 4.処理
			// 入出力項目にマッピング
			denpyouKbn = new String[list.size()];
			denpyouShubetsu = new String[list.size()];
			denpyouKaribaraiNashiShubetsu = new String[list.size()];
			denpyouPrintShubetsu = new String[list.size()];
			denpyouPrintKaribaraiNashiShubetsu = new String[list.size()];
			gyoumuShubetsu = new String[list.size()];
			naiyou = new String[list.size()];
			yuukouKigenFrom = new String[list.size()];
			yuukouKigenTo = new String[list.size()];
			kanrenSentakuFlg = new String[list.size()];
			kanrenHyoujiFlg = new String[list.size()];
			denpyouPrintFlg = new String[list.size()];
			// 予算執行管理オプションが「予算執行管理Aあり」の場合に設定
			if (RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(yosanShikkouOption)){
				kianBangouUnyouFlg = new String[list.size()];
				yosanShikkouTaishou = new String[list.size()];
			}
			routeHanteiKingaku = new String[list.size()];
			routeTorihikiFlg = new String[list.size()];
			shouninJyoukyouPrintFlg = new String[list.size()];
			shorikengen = new String[list.size()];
			shiiresakiFlg = new String[list.size()];
			bgColor = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				GMap map = list.get(i);
				denpyouKbn[i] = (String)map.get("denpyou_kbn");
				denpyouShubetsu[i] = (String)map.get("denpyou_shubetsu");
				denpyouKaribaraiNashiShubetsu[i] = (String)map.get("denpyou_karibarai_nashi_shubetsu");
				denpyouPrintShubetsu[i] = (String)map.get("denpyou_print_shubetsu");
				denpyouPrintKaribaraiNashiShubetsu[i] = (String)map.get("denpyou_print_karibarai_nashi_shubetsu");
				gyoumuShubetsu[i] = (String)map.get("gyoumu_shubetsu");
				naiyou[i] = (String)map.get("naiyou");
				yuukouKigenFrom[i] = formatDate(map.get("yuukou_kigen_from"));
				yuukouKigenTo[i] = formatDate(map.get("yuukou_kigen_to"));
				kanrenSentakuFlg[i] = (String)map.get("kanren_sentaku_flg");
				kanrenHyoujiFlg[i]  					= (String)map.get("kanren_hyouji_flg");
				denpyouPrintFlg[i]  					= (String)map.get("denpyou_print_flg");
				// 予算執行管理オプションが「予算執行管理Aあり」の場合に設定
				if (RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(yosanShikkouOption)){
					kianBangouUnyouFlg[i] = (String)map.get("kianbangou_unyou_flg");
					yosanShikkouTaishou[i] = (String)map.get("yosan_shikkou_taishou");
				}
				routeHanteiKingaku[i] = (String)map.get("route_hantei_kingaku");
				routeTorihikiFlg[i] = (String)map.get("route_torihiki_flg");
				shouninJyoukyouPrintFlg[i] = (String)map.get("shounin_jyoukyou_print_flg");
				shorikengen[i] = (String)map.get("shinsei_shori_kengen_name");
				shiiresakiFlg[i] = (String)map.get("shiiresaki_flg");
				bgColor[i] = EteamCommon.bkColorSettei(yuukouKigenFrom[i], yuukouKigenTo[i]);
			}
			// 共通制御
			displaySeigyo(connection, denpyouKbn);

			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 更新イベント
	 * @return 処理結果
	 */
	public String kousin(){

		try(EteamConnection connection = EteamConnection.connect()){
			initParts(connection);

			// 共通制御
			displaySeigyo(connection, denpyouKbn);

			// 1.入力チェック
			formatCheck();
			hissuCheck(2);
			soukanCheck();
			if (! errorList.isEmpty())
			{
				return "error";
			}

			// 予算執行管理オプションが「予算執行管理Aあり」以外の場合に設定
			if (!RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(yosanShikkouOption)){
				kianBangouUnyouFlg = new String[gyoumuShubetsu.length];
				yosanShikkouTaishou = new String[gyoumuShubetsu.length];
				for (int j = 0; j < kianBangouUnyouFlg.length; j++){
					kianBangouUnyouFlg[j] = "0";
					yosanShikkouTaishou[j] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI;
				}
			}

			if(denpyouKbn != null){
				for(int i=0 ; i < denpyouKbn.length ; i++){

					// 2.データ存在チェック
					GMap data = kihyouNaviLogic.findDenpyouKanri(denpyouKbn[i]);
					if(null == data) throw new EteamDataNotFoundException();

					// 3.アクセス可能チェック なし

					// 4.処理
					
					//有効期限終了日が省略されている場合
					String wkYuukouKigenTo = yuukouKigenTo[i];
					if(isEmpty(wkYuukouKigenTo)){
						wkYuukouKigenTo = YUUKOU_KIGEN_TO_DEFAULT;
					}

					// 伝票種別一覧を更新する。
					myLogic.update(
						i + 1, 
						gyoumuShubetsu[i], 
						denpyouShubetsu[i],
						denpyouKaribaraiNashiShubetsu[i],
						denpyouPrintShubetsu[i],
						denpyouPrintKaribaraiNashiShubetsu[i],
						naiyou[i], 
						toDate(yuukouKigenFrom[i]),
						toDate(wkYuukouKigenTo),
						kanrenSentakuFlg[i],
						kanrenHyoujiFlg[i],
						denpyouPrintFlg[i],
						kianBangouUnyouFlg[i],
						yosanShikkouTaishou[i],
						routeHanteiKingaku[i],
						routeTorihikiFlg[i],
						shouninJyoukyouPrintFlg[i],
						shorikengen[i],
						shiiresakiFlg[i],
						getUser().getTourokuOrKoushinUserId(),
						denpyouKbn[i]);

					// 伝票がユーザー定義届書の場合、ユーザー定義届書内容を更新する
					if (wfEventLogic.isKaniTodoke(denpyouKbn[i])){
						kaniTodokeLogic.updateKaniTodokeNaiyou(denpyouKbn[i], naiyou[i], getUser().getTourokuOrKoushinUserId());
					}

					connection.commit();
				}
			}
			return "success";
		}
	}
}
