package eteam.gyoumu.workflow;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamXls;
import eteam.base.EteamXlsFmt;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.JOUKYOU;
import eteam.common.EteamSettingInfo;
import eteam.common.select.KaniTodokeCategoryLogic;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.user.User;
import eteam.gyoumu.user.UserLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic.WfSeigyo;
import eteam.gyoumu.yosanshikkoukanri.YosanShikkouKanriCategoryLogic;
import jxl.Cell;
import jxl.format.Colour;
import jxl.write.WritableWorkbook;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

/**
 * 各伝票共通欄
 */
public class WorkflowXlsLogic extends EteamAbstractLogic{
	
	// 定義
	//TODO:文言は固定ではなくなる
	/** 申請 */
	public static final String SHINSEI = "申請";
	/** 代行申請 */
	public static final String DAIKOU_SHINSEI = "代行申請";
	/** 承認 */
	public static final String SHOUNIN = "承認";
	/** 代行承認 */
	public static final String DAIKOU_SHOUNIN = "代行承認";
	/** 先決承認 */
	public static final String JOUISENKETSU = "先決承認";
	/** 印影最大人数 */
	public static final int MAX_USER = 8;

	/** 押印枠数 出力形式 */
	protected String ouinFormat = setting.ouinFormat();
	/** 押印枠数 表示内容 */
	protected String ouinNaiyou = setting.ouinNaiyou();
	/** 印影（画像）表示 */
	protected boolean ineiFlg = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.INEI).equals("0")? false : true;
	/** 印鑑サイズ */
	protected String inkanSize = setting.ouinSize();
	
	/**
	 * @param excel 編集中EXCEL
	 * @param denpyouId 伝票ID
	 * @param sokyuuFlg 遡及日適用フラグ
	 */
	public void makeExcel(EteamXls excel, String denpyouId, boolean sokyuuFlg) {
		
		// ---------------------------------------
		// オブジェクトNEW
		// ---------------------------------------
		SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		WorkflowCategoryLogic workflowLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		WorkflowEventControlLogic wfConLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		KaniTodokeCategoryLogic kaniLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);
		KihyouNaviCategoryLogic kihyouLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		
		// ---------------------------------------
		// データ取得
		// ---------------------------------------
		GMap denpyou = workflowLogic.selectDenpyou(denpyouId); //denpyouテーブル
		List<GMap>		routeList = workflowLogic.selectShouninRoute(denpyouId); //├(1:N)   shounin_route
		GMap shinseiRoute = workflowLogic.selectKihyoushaData(denpyouId); //├(1:1)   shounin_route
		GMap shinseiRireki = workflowLogic.selectShinseiRireki(denpyouId); //├(1:0/1) shounin_joukyou
		List<GMap>		jouykouList = workflowLogic.selectShouninJoukyou(denpyouId);
		List<GMap>		sousaList = workflowLogic.loadSousaRireki(denpyouId);
		GMap ringiInfo = wfConLogic.loadRingiKingakuData(denpyouId);
		String joukyouCd = (String)denpyou.get("denpyou_joutai");
		String joukyouName = (String)sysLogic.findNaibuCdSetting("denpyou_jyoutai", joukyouCd).get("name");
		
		// 伝票番号の表示非表示制御
		Map<String, Boolean> isWfLevelRenkeiMap = EteamCommon.getWfLevelRenkeiMap();
		String denpyouKbn = denpyouId.substring(7, 11);
		Boolean flag = isWfLevelRenkeiMap.get(denpyouKbn);
		
		// ---------------------------------------
		// テンプレートEXCEL開く
		// ---------------------------------------
		WritableWorkbook book = excel.getBook();
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		GMap denpyouMap = kihyouLogic.findDenpyouKanri(denpyouKbn);

		//■伝票管理に設定された伝票のみ、起案番号の出力
		if (denpyouMap.get("kianbangou_unyou_flg").equals("1")) {
			//TODO:画面とロジックを部品化する(メンテ漏れそうでこわいんで)
			GMap map = wfConLogic.selectDenpyouKianHimozuke(denpyouId);
			if (!map.isEmpty()) {
				String kianBangou = "";
				if (denpyouMap.get("yosan_shikkou_taishou").equals(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN)) {
					kianBangou = (String)map.get("shishutsu_kian_bangou");
				} else if (denpyouMap.get("yosan_shikkou_taishou").equals(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI)) {
					kianBangou = "";
				} else {
					kianBangou = (String)map.get("jisshi_kian_bangou");
				}
				excel.write("kian_no", kianBangou);
				excel.selectSheet(0);
			}
		}
		
		//■承認状況
		excel.write("joukyou", "（" + joukyouName + "）");
		
		//添付ファイル名記載
		if (setting.filenameOut().equals("1")) {
			// Ver22.12.02.10 selectTenpuFile → selectTenpuFileImportに変更(1e文書複数明細で複数行出力されてしまうため) #4118対応
			List<GMap> tenpuFileList = workflowLogic.selectTenpuFileForImport(denpyouId);
			int tenpuCol = excel.getCell("tenpu_file_name").getColumn();
			int tenpuRow = excel.getCell("tenpu_file_name").getRow();
			int row = tenpuRow;
			for (GMap map : tenpuFileList) {
				String fileName = (String)map.get("file_name");
				if (map.get("ebunsho_no") != null) {
					fileName += "(" + (String)map.get("ebunsho_no") + ".pdf)";
				}
				
				// 長すぎるファイル名は改行してしまう
				var fileNameArray = this.splitStringByByteLength(fileName);
				for(int i = 0; i < fileNameArray.length; i++)
				{
					excel.write(tenpuCol, tenpuRow, fileNameArray[i]);
					excel.copyRow(tenpuRow, ++tenpuRow, 1, 1, true);
					excel.write(tenpuCol, tenpuRow, "");
				}
			}
			if (tenpuRow != row) {
				excel.getSheet().removeRow(tenpuRow);
			}
		} else {
			excel.hideRow(excel.getCell("tenpu_file_title").getRow());
			excel.hideRow(excel.getCell("tenpu_file_name").getRow());
		}
		
		//■印鑑
		routeList = trimRouteListNini(routeList); //印鑑用に承認処理必須のユーザーのみにする
		int rowW = 0; //Wのときrowの差分用
		int wakuOnly = 0; //1～8のとき承認者印字なし用
		// 印鑑のサイズ指定で値が異なる変数をセットする
		String inkanName = "inkan_S";
		int rowLength = 5;
		int inkanCol = 2;
		int inkanRow = 3;
		int inkanImageRow = -5;
		if(inkanSize.equals("1")){
			inkanName = "inkan_L";
			rowLength = 6;
			inkanCol = 3;
			inkanRow = 4;
			inkanImageRow = 1;
		}
		
		if(ouinFormat.equals("X")){ //押印枠を申請者+承認者の数だけ印字。ただし最大8枠まで。
			setInkanWakuPatternX(book, excel, denpyouId, sokyuuFlg, routeList, inkanName, inkanCol, inkanRow, inkanImageRow);
			
		}else if(ouinFormat.equals("W")){ //押印枠を合議・申請者+承認者の数だけ印字。
			rowW = setInkanWakuPatternW(book, excel, denpyouId, sokyuuFlg, routeList, inkanName, rowLength, inkanCol, inkanRow, inkanImageRow);
			
		}else{ //1～8：承認者印字なしの枠を指定個数分表示
			wakuOnly = 1;
			if (ouinFormat.equals("0") == false) {
				setInkanWakuPatternNum(book, excel, inkanCol, inkanRow);
			}
		}
		
		//いらない印鑑行を削除
		if(!ouinFormat.equals("0")){ //押印枠非表示
			for(int x=0; x<6-wakuOnly; x++){
				excel.getSheet().removeRow(book.findCellByName("gougi_name_small").getRow() + 5 + rowW + wakuOnly);
			}
		}
		if(ouinFormat.equals("0") || !inkanSize.equals("2")){ //印鑑小
			for(int x=0; x<6-wakuOnly; x++){
				excel.getSheet().removeRow(book.findCellByName("gougi_name_small").getRow() + rowW + wakuOnly);
			}
		} else {
			if (wakuOnly > 0) {
				excel.getSheet().removeRow(book.findCellByName("inkan_S").getRow());
			}
		}
		if(ouinFormat.equals("0") || !inkanSize.equals("1")){ //印鑑大
			for(int x=0; x<6; x++){
				excel.getSheet().removeRow(book.findCellByName("gougi_name").getRow());
			}
		} else {
			if (wakuOnly > 0) {
				excel.getSheet().removeRow(book.findCellByName("inkan_L").getRow());
			}
		}
		
		//QRテキスト作成
		String denpyouShubetsuUrl = (String)workflowLogic.selectDenpyouShubetu(denpyouKbn).get("denpyou_shubetsu_url");
		String url = sysLogic.findSettingValOnlyInfo(EteamSettingInfo.Key.SHOTODOKE_URL) + denpyouShubetsuUrl + "?denpyouId=" + denpyouId + "&denpyouKbn=" + denpyouKbn;
		if (wfConLogic.isKaniTodoke(denpyouKbn)) {
			int version = kaniLogic.findVersion(denpyouId);
			if(version >= 1) {
				url += "&version=" + version;
			}
		}
		
		//伝票管理に設定された伝票のみ、２シート目、承認状況・操作履歴の出力
		if (denpyouMap.get("shounin_jyoukyou_print_flg").equals("1")) {
			excel.selectSheet(1);
			
			//操作履歴
			int count = 1;
			int baseRow = book.findCellByName("sousa_koushin_time").getRow();
			for (GMap joukyou : sousaList) {
				setSousaRireki(excel, baseRow, count++, joukyou);
			}
			excel.getSheet().removeRow(baseRow);

			//承認状況
			count = 1;
			baseRow = book.findCellByName("koushin_time").getRow();
			int edano = 1;
			for (GMap joukyou : jouykouList) {
				if (edano != 1 && ! joukyou.get("isGougi").equals(jouykouList.get(edano - 2).get("isGougi"))) {
					setShouninJoukyouEmpty(excel, baseRow, count++);
				}
				if(! (boolean)joukyou.get("isGougi")) {
					setShouninJoukyou(excel, baseRow, count++, joukyou);
				} else {
					List<GMap> gougiOya = (List<GMap>) joukyou.get("gougiOya");
					for (GMap oya : gougiOya) {
						setShouninJoukyouGougiHeader(excel, baseRow, count++, oya);
						List<GMap> gougiKo = (List<GMap>) oya.get("gougiKo");
						for (GMap ko : gougiKo) {
							setShouninJoukyou(excel, baseRow, count++, ko);
						}
					}
				}
				edano++;
			}
			excel.getSheet().removeRow(baseRow);
			excel.selectSheet(0);
		} else {
			excel.getBook().removeSheet(1);
		}
		
		//■ヘッダー
		excel.write("denpyou_id", denpyouId);
		this.setKihyoushaAndDate(excel, sokyuuFlg, shinseiRoute, joukyouCd, shinseiRireki, routeList, denpyouKbn);
		excel.write("denpyou_no", flag == null || flag.booleanValue() ? String.format("%1$08d", denpyou.getObject("serial_no")) : "");
		excel.write("denpyou_id", denpyouId);
		if(null == ringiInfo || null == ringiInfo.get("ringi_kingaku")){
			excel.hideRow(excel.getCell("ringi_kingaku_title").getRow());
		}else{
			String ringiKingakuName = ringiInfo.get("ringiKingakuName").toString();
			excel.write("ringi_kingaku_title", ringiKingakuName);
			excel.write("ringi_kingaku", EteamXlsFmt.formatMoney(ringiInfo.get("ringi_kingaku")) + "円");
			excel.write("ringi_kingaku_zandaka_title", ringiKingakuName + "残高");
			excel.write("ringi_kingaku_zandaka", EteamXlsFmt.formatMoney(ringiInfo.get("ringiKingakuZandaka")) + "円");
		}
		excel.write("shozoku_bumon_cd", (String)shinseiRoute.get("bumon_cd") + " " );
		excel.write("shozoku_bumon_name", trimBumonName((String)shinseiRoute.get("bumon_full_name")));
		excel.writeImage ("brcode", 5, 7, QRCode.from(url).to(ImageType.PNG).stream().toByteArray());
		
		if(denpyouKbn.startsWith("A")){
			//通常伝票
			setMaruhi(excel, denpyouId, "denpyou_shubetsu");
		}else if(denpyouKbn.startsWith("B")){
			//ユーザー定義届書伝票
			setMaruhi(excel, denpyouId, "denpyoumei");
		}
	}
	
	/**
	 * @param excel 編集中EXCEL
	 * @param denpyouId 伝票ID
	 */
	public void makeExcelBottom(EteamXls excel, String denpyouId) {
		
		// ---------------------------------------
		// オブジェクトNEW
		// ---------------------------------------
		YosanShikkouKanriCategoryLogic yosanLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, connection);
		WorkflowEventControlLogic wfecLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		UserLogic lc = EteamContainer.getComponent(UserLogic.class, connection);
		
		// ---------------------------------------
		// データ取得
		// ---------------------------------------
		String yosanComment = yosanLogic.findComment(denpyouId);
		
		// ---------------------------------------
		// テンプレートEXCEL開く
		// ---------------------------------------
		WritableWorkbook book = excel.getBook();
		
		// ---------------------------------------
		//EXCEL編集
		// ---------------------------------------
		
		//■予算・起案チェックコメント
		if (setting.yosanKianCommnent().equals("1")) {
			@SuppressWarnings("deprecation")
			User user = getUser();
			if (isEmpty(user)) {
				user = lc.makeSessionUser("admin");
			}
			WfSeigyo wf = wfecLogic.judgeWfSeigyo(user, denpyouId, null);
			if (wf.kianCheck) {
				excel.write("yosan_kian_comment_title", "■起案チェックコメント");
			} else if (wf.yosanCheck) {
				excel.write("yosan_kian_comment_title", "■予算チェックコメント");
			}
			if ((wf.kianCheck | wf.yosanCheck) && !isEmpty(yosanComment)) {
				Cell commentCell = book.findCellByName("yosan_kian_comment");
				int commentRow = commentCell.getRow();
				int commentCol = commentCell.getColumn();
				//高さ調整：補足は１行４７文字以内で計算。一行ずつインサート。
				List<String> commentList = excel.splitLine(yosanComment, 94);
				if (1 < commentList.size()) {
					excel.copyRow(commentRow, commentRow + 1, 1, commentList.size() - 1, true);
				}
				int rowCount = 0;
				for (String hosokuStr : commentList) {
					excel.write(commentCol, commentRow + rowCount++, hosokuStr);
				}
				//枠線をセットする。
				excel.encloseInBorder(commentRow, commentCol, commentList.size());
			} else {
				excel.hideRow(excel.getCell("yosan_kian_comment_title").getRow());
				excel.hideRow(excel.getCell("yosan_kian_comment").getRow());
			}
		}else{
			excel.hideRow(excel.getCell("yosan_kian_comment_title").getRow());
			excel.hideRow(excel.getCell("yosan_kian_comment").getRow());
		}
	}
	
	/**
	 * 承認ルートリスト内の承認処理必須以外のを取り除く
	 * @param routeList 承認ルートリスト(元)
	 * @return 承認ルートリスト(trim後)
	 */
	protected List<GMap> trimRouteListNini(List<GMap> routeList) {
		List<GMap> neoRouteList = new ArrayList<>();
		for(GMap route : routeList){
			if((int)route.get("edano") == 1){
				neoRouteList.add(route);
			} else if (route.get("saishu_shounin_flg").equals("1")) {
				neoRouteList.add(route);
			} else{
				if(!(boolean)route.get("isGougi")){
					if(route.get("shounin_hissu_flg").equals("1")){
						neoRouteList.add(route);
					}
				}else{
					boolean gougiHissu = false;
					@SuppressWarnings("unchecked")
					ArrayList<GMap> gougiOyaList = (ArrayList<GMap>)((ArrayList<GMap>)route.get("gougiOya")).clone();
					for(int j = gougiOyaList.size() - 1; j >= 0; j--){
						GMap gougiOya = gougiOyaList.get(j);
						boolean oyaHissu = false;
						@SuppressWarnings("unchecked")
						ArrayList<GMap> gougiKoList = (ArrayList<GMap>)gougiOya.get("gougiKo");
						for(int k = gougiKoList.size() - 1; k >= 0; k--){
							GMap gougiKo = gougiKoList.get(k);
							if(gougiKo.get("shounin_hissu_flg").equals("1")){
								oyaHissu = true; 
							}else{
								gougiKoList.remove(k);
							}
						}
						if(oyaHissu){
							gougiHissu = true;
						}else{
							gougiOyaList.remove(j);
						}
					}
					if(gougiHissu){
						neoRouteList.add(route);
					}
				}
			}
		}
		return neoRouteList;
	}

	/**
	 * 引数を比較し、最大値を返すメソッド
	 * 
	 * @param arr 整数
	 * @return 最大値
	 */
	public int findMaxInt(int... arr) {

		int max = 0;
		for (int i = 0; i< arr.length;i++){
			max = Math.max(max, arr[i]);
		}
		return max;
	}
	
	/**
	 * 部門名のトリム
	 * @param s 部門名
	 * @return 部門名
	 */
	protected String trimBumonName(String s) {
		return s;
	}
	
	/**
	 * 名前を探索し、代行者であるときにtrueを返す。
	 * 
	 * @param fullName 名前
	 * @return 代行者であるかどうか
	 */
	protected boolean isDaikousha(String fullName) {
		Pattern p = Pattern.compile("\\(代行者:.+?\\)");
        Matcher m = p.matcher(fullName);
		if(m.find()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 名前を整形する。
	 * 代行者である場合、代行者名を返す。
	 * そうでなければそのままで返す。
	 * 
	 * @param fullName 名前
	 * @return 整形された名前。
	 */
	protected String findFullName(String fullName) {
		Pattern p = Pattern.compile("\\(代行者:.+?\\)");
        Matcher m = p.matcher(fullName);
		if(m.find()) {
			return m.group(0).substring(5, m.group().length() - 1);
		} else {
			return fullName;
		}
	}
	
	/**
	 * @param fileName ファイル名
	 * @return 90バイトごとに分割された配列
	 */
	protected String[] splitStringByByteLength(String fileName) {
	    Charset cs = Charset.forName("SHIFT-JIS"); // ExcelなのでSHIFT-JISで多分OKのはず（アラビア語のファイル名とか入れられたら分からないけど、UTFだと日本語が３バイトになってしまうはずなので）
	    CharsetEncoder coder = cs.newEncoder();
	    ByteBuffer out = ByteBuffer.allocate(90);  // output buffer of required size
	    CharBuffer in = CharBuffer.wrap(fileName);
	    List<String> ss = new ArrayList<>();            // a list to store the chunks
	    int pos = 0;
	    while(true) {
	        CoderResult cr = coder.encode(in, out, true); // try to encode as much as possible
	        int newpos = fileName.length() - in.length();
	        String s = fileName.substring(pos, newpos);
	        ss.add(s);                                  // add what has been encoded to the list
	        pos = newpos;                               // store new input position
	        out.rewind();                               // and rewind output buffer
	        if (! cr.isOverflow()) {
	            break;                                  // everything has been encoded
	        }
	    }
	    return ss.toArray(new String[0]);
	}
	
	/**
	 * 印影に表示させるリストを作成する。
	 * 
	 * @param denpyouId 伝票ID
	 * @param routeList 承認ルート
	 * @param sokyuuFlg 遡及日適用フラグ
	 * @return 印影に追加するリスト
	 */
	protected List<GMap> setInkanList(String denpyouId, List<GMap> routeList, boolean sokyuuFlg) {
		
		// ---------------------------------------
		// オブジェクトNEW
		// ---------------------------------------
		WorkflowCategoryLogic workflowLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		List<GMap> result = new ArrayList<GMap>();
		
		for (int i = 0; i < routeList.size(); i++) {
			GMap route = routeList.get(i);
			GMap map = new GMap();
			//紐づけされていればmapに格納する
			if (
					(route.get("gougi_edano") == null && route.get("joukyou_edano") != null && !(boolean)route.get("isGougi")) || 
					(route.get("gougi_edano") != null && route.get("joukyou_edano") != null)
			) {
				GMap joukyou = workflowLogic.selectSaishinShouninJoukyou(denpyouId, (int)route.get("joukyou_edano"));
				String joukyouCd = (String)joukyou.get("joukyou_cd");//上位先決で飛ばされても承認/否認のコードが入る
				String joukyouMongon = (String)joukyou.get("joukyou"); //上位先決で飛ばされたら空のはず
				if(joukyouCd.equals("1") || (joukyouCd.equals(JOUKYOU.SHOUNIN) && !joukyouMongon.isEmpty())){//申請 or 承認の承認状況のみ
					if (isDaikousha((String)joukyou.get("user_full_name"))) {
						joukyouMongon = "代行" + joukyouMongon;
					}
					map.put("waku_mongon", route.get("user_full_name"));
					map.put("inkan_top", joukyouMongon);
					map.put("inkan_name", findFullName((String)joukyou.get("user_full_name")));
					map.put("inkan_bottom", sokyuuFlg ? "" : new SimpleDateFormat("yyyy.MM.dd").format(joukyou.get("koushin_time")));
				}
			}
			result.add(map);
		}
		
		return result;
	}
	
	/**
	 * @param route 承認ルートマップ
	 * @param excel 編集中EXCEL
	 * @param inkanName セルの名前
	 * @param col 座標（行)
	 * @param row 座標（列）
	 */
	protected void outputOuinsha(GMap route, EteamXls excel, String inkanName, int col, int row) {
		String hyoujiNaiyou = "";
		switch(ouinNaiyou){
		case "1":		// 役割（業務ロールユーザーの場合、業務ロール名）
			hyoujiNaiyou = (StringUtils.isNotEmpty((String)route.get("gyoumu_role_name")) ? (String)route.get("gyoumu_role_name") : (String)route.get("bumon_role_name"));
			break;
			
		case "2":		// ユーザー名（業務ロールユーザーの場合、業務ロール名）
			hyoujiNaiyou = (StringUtils.isNotEmpty((String)route.get("gyoumu_role_name")) ? (String)route.get("gyoumu_role_name") : (String)route.get("user_full_name"));
			break;
			
		case "3":		// 処理権限名
			hyoujiNaiyou = (String)route.get("shounin_shori_kengen_name");
			break;
		}
		excel.write(inkanName, col, row, hyoujiNaiyou);
	}
	
	/**
	 * @param inkan 印影画像データ（バイト）
	 * @param excel 編集中EXCEL
	 * @param inkanName セルの名前
	 * @param col 座標（行)
	 * @param row 座標（列）
	 * @param inkanCol 印鑑サイズ（幅）
	 * @param inkanRow 印鑑サイズ（縦）
	 */
	protected void outputInei(byte[] inkan, EteamXls excel, String inkanName, int col, int row, int inkanCol, int inkanRow) {
		if (inkan != null) {
			excel.writeImage (inkanName, col, row, inkanCol, inkanRow, inkan);
		}
	}
	
	/**
	 * @param book テンプレートExcel
	 * @param excel 編集中のExcel
	 * @param inkanCol 座標（行）
	 * @param inkanRow 座標（列）
	 */
	protected void setInkanWakuPatternNum(WritableWorkbook book, EteamXls excel, int inkanCol, int inkanRow) {

		// 印鑑のサイズ指定で値が異なる変数をセットする
		Cell base = (inkanSize.equals("1"))? book.findCellByName("inkan_L") : book.findCellByName("inkan_S");
		for (int i = 0; i < Integer.parseInt(ouinFormat); i++) {
			excel.copy(base.getColumn(), base.getRow(), base.getColumn() + inkanCol, base.getRow() + inkanRow, base.getColumn() - inkanCol * i, base.getRow());
		}
		
		// 押印者の出力・印影追加はなし
		
	}
	
	/**
	 * 
	 * 押印枠タイプXの処理
	 * 
	 * @param book テンプレートEXCEL
	 * @param excel 編集中EXCEL
	 * @param denpyouId 伝票ID
	 * @param sokyuuFlg 遡及日適用フラグ
	 * @param routeList 承認ルートリスト
	 * @param inkanName 印鑑セル名
	 * @param inkanCol 印鑑サイズ（列）
	 * @param inkanRow 印鑑サイズ（行）
	 * @param inkanImageRow 印鑑イメージ用行
	 */
	protected void setInkanWakuPatternX(WritableWorkbook book, EteamXls excel, String denpyouId, boolean sokyuuFlg, List<GMap> routeList, String inkanName, int inkanCol, int inkanRow, int inkanImageRow) {
		
		// ---------------------------------------
		// オブジェクトNEW
		// ---------------------------------------
		InkanLogic inkanLogic = EteamContainer.getComponent(InkanLogic.class);
		List<GMap> inkanList = setInkanList(denpyouId, routeList, sokyuuFlg);
		
		int gougiCnt = 0;
		//押印枠印字
		for (int i = 0; i < routeList.size(); i++) {
			GMap route = routeList.get(i);
			if (0 < routeList.size() && i + gougiCnt < MAX_USER) {
				if (route.get("isGougi").equals(true)) {
					@SuppressWarnings("unchecked")
					List<GMap> gougiOya = (List<GMap>) route.get("gougiOya");
					for (GMap oya : gougiOya) {
						@SuppressWarnings("unchecked")
						List<GMap> gougiKo = (List<GMap>) oya.get("gougiKo");
						// TODO 印鑑リストの構造は合議リストと承認状況の紐付けの持ち方によって修正必須
						List<GMap> inkanKoList = setInkanList(denpyouId, gougiKo, sokyuuFlg);
						for (int j = 0; j < gougiKo.size(); j++) {
							if (i + gougiCnt < MAX_USER) {
								Cell inkanCell = book.findCellByName(inkanName);
								excel.copy(inkanCell.getColumn(), inkanCell.getRow(), inkanCell.getColumn() + inkanCol, inkanCell.getRow() + inkanRow, inkanCell.getColumn() - inkanCol * (i + gougiCnt), inkanCell.getRow());

								// 押印者の出力
								if((ouinNaiyou.equals("0"))==false){
									outputOuinsha(gougiKo.get(j), excel, inkanName, -inkanCol * (i + gougiCnt), 0);
								}
								
								// 印影追加
								if(ineiFlg){
									if(!inkanKoList.isEmpty()) {
										outputInei(inkanLogic.make(inkanKoList.get(j)), excel, inkanName, -inkanCol * (i + gougiCnt), inkanImageRow, inkanCol, inkanRow);
									}
								}
								gougiCnt++;
							}
						}
					}
					gougiCnt--;
				} else {
					Cell inkanCell = book.findCellByName(inkanName);
					excel.copy(inkanCell.getColumn(), inkanCell.getRow(), inkanCell.getColumn() + inkanCol, inkanCell.getRow() + inkanRow, inkanCell.getColumn() - inkanCol * (i + gougiCnt), inkanCell.getRow());
					
					// 押印者の出力
					if((ouinNaiyou.equals("0"))==false){
						outputOuinsha(route, excel, inkanName, -inkanCol * (i + gougiCnt), 0);
					}
					
					// 印影追加
					if(ineiFlg){
						if(!inkanList.isEmpty()) {
							outputInei(inkanLogic.make(inkanList.get(i)), excel, inkanName, -inkanCol * (i + gougiCnt), inkanImageRow, inkanCol, inkanRow);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * 押印枠タイプWの処理
	 * 
	 * @param book テンプレートEXCEL
	 * @param excel 編集中EXCEL
	 * @param denpyouId 伝票ID
	 * @param sokyuuFlg 遡及日適用フラグ
	 * @param routeList 承認ルートリスト
	 * @param inkanName 印鑑セル名
	 * @param rowLength 印鑑エリア行
	 * @param inkanCol 印鑑サイズ（列）
	 * @param inkanRow 印鑑サイズ（行）
	 * @param inkanImageRow 印鑑イメージ用行
	 * @return 印鑑行合計
	 */
	protected int setInkanWakuPatternW(WritableWorkbook book, EteamXls excel, String denpyouId, boolean sokyuuFlg, List<GMap> routeList, String inkanName, int rowLength, int inkanCol, int inkanRow, int inkanImageRow) {
		
		// ---------------------------------------
		// オブジェクトNEW
		// ---------------------------------------
		InkanLogic inkanLogic = EteamContainer.getComponent(InkanLogic.class);
		List<GMap> inkanList = setInkanList(denpyouId, routeList, sokyuuFlg);
		
		// 変数をセットする
		String gougiCount = "gougi_count_small";
		String gougiName = "gougi_name_small";
		if(inkanSize.equals("1")){
			gougiCount = "gougi_count";
			gougiName = "gougi_name";
		}
		
		//押印枠行の追加
		int originalRow = book.findCellByName(inkanName).getRow()-1;
		int copyRow = book.findCellByName(inkanName).getRow()-1;
		int cnt = 0;
		boolean isGougiPrev = false;
		for (int routeCnt = 0; routeCnt < routeList.size(); routeCnt++) {
			GMap route = routeList.get(routeCnt);
			if (cnt >= MAX_USER) {
				cnt = 0;
				copyRow += rowLength;
				excel.copyRow(originalRow, copyRow, rowLength, 1, true);
			}
			if (0 < routeList.size()) {
				if (route.get("isGougi").equals(true)) {
					cnt = 0;
					isGougiPrev = false;
					copyRow += rowLength;
					excel.copyRow(originalRow, copyRow, rowLength, 1, true);
					@SuppressWarnings("unchecked")
					List<GMap> gougiOya = (List<GMap>) route.get("gougiOya");
					for (GMap oya : gougiOya) {
						cnt = 0;
						if (isGougiPrev) {
							copyRow += rowLength;
							excel.copyRow(originalRow, copyRow, rowLength, 1, true);
						}
						isGougiPrev = true;
						@SuppressWarnings("unchecked")
						List<GMap> gougiKo = (List<GMap>) oya.get("gougiKo");
						for (int i = 0; i < gougiKo.size() / MAX_USER; i++) {
							cnt = 0;
							copyRow += rowLength;
							excel.copyRow(originalRow, copyRow, rowLength, 1, true);
						}
					}
				} else {
					//前回合議の行だった場合、行を追加
					if (isGougiPrev) {
						cnt = 0;
						isGougiPrev = false;
						copyRow += rowLength;
						excel.copyRow(originalRow, copyRow, rowLength, 1, true);
					}
					cnt++;
				}
			}
		}
		//押印枠印字
		int row = 0;
		int col = 0;
		int gougiCnt = 1;
		boolean isGougiPrev2 = false;
		for (int i = 0; i < routeList.size(); i++) {
			GMap route = routeList.get(i);
			if (col >= MAX_USER) {
				col = 0;
				row += rowLength;
			}
			if (0 < routeList.size()) {
				if (route.get("isGougi").equals(true)) {
					col = 0;
					isGougiPrev2 = false;
					row += rowLength;
					@SuppressWarnings("unchecked")
					List<GMap> gougiOya = (List<GMap>) route.get("gougiOya");
					excel.write(gougiCount, 0, row, "合議(" + gougiCnt++ + ")");
					for (GMap oya : gougiOya) {
						if (isGougiPrev2) {
							row += rowLength;
						}
						col = 0;
						isGougiPrev2 = true;
						excel.write(gougiName, 0, row, (String)oya.get("gougi_name"));
						@SuppressWarnings("unchecked")
						List<GMap> gougiKo = (List<GMap>) oya.get("gougiKo");
						// TODO 印鑑リストの構造は合議リストと承認状況の紐付けの持ち方によって修正必須
						List<GMap> inkanKoList = setInkanList(denpyouId, gougiKo, sokyuuFlg);
						for (int j = 0; j < gougiKo.size(); j++) {
							if (col >= MAX_USER) {
								col = 0;
								row += rowLength;
							}
							Cell inkanCell = book.findCellByName(inkanName);
							excel.copy(inkanCell.getColumn(), inkanCell.getRow(), inkanCell.getColumn() + inkanCol, inkanCell.getRow() + inkanRow, inkanCell.getColumn() - inkanCol * col, inkanCell.getRow() + row);
							
							// 押印者の出力
							if((ouinNaiyou.equals("0"))==false){
								outputOuinsha(gougiKo.get(j), excel, inkanName, -inkanCol * col, row);
							}
							
							// 印影追加
							if(ineiFlg){
								if(!inkanKoList.isEmpty()) {
									outputInei(inkanLogic.make(inkanKoList.get(j)), excel, inkanName, -inkanCol * col, row + inkanImageRow, inkanCol, inkanRow);
								}
							}
							col++;
						}
					}
				} else {
					//前回合議の行だった場合、行を追加
					if (isGougiPrev2) {
						col = 0;
						isGougiPrev2 = false;
						row += rowLength;
					}
					Cell inkanCell = book.findCellByName(inkanName);
					excel.copy(inkanCell.getColumn(), inkanCell.getRow(), inkanCell.getColumn() + inkanCol, inkanCell.getRow() + inkanRow, inkanCell.getColumn() - inkanCol * col, inkanCell.getRow() + row);

					// 押印者の出力
					if((ouinNaiyou.equals("0"))==false){
						outputOuinsha(route, excel, inkanName, -inkanCol * col, row);
					}
					
					// 印影追加
					if(ineiFlg){
						if(!inkanList.isEmpty()) {
							outputInei(inkanLogic.make(inkanList.get(i)), excel, inkanName, -inkanCol * col, row + inkanImageRow, inkanCol, inkanRow);
						}
					}
					col++;
				}
			}
		}
		return row;
	}

	/**
	 * 承認状況の値を格納する
	 * @param excel 編集中EXCEL
	 * @param baseRow デフォルトの行番号
	 * @param count 何行目？(1～)
	 */
	protected void setShouninJoukyouEmpty(EteamXls excel, int baseRow, int count) {
		excel.copyRow(baseRow, baseRow + count, 1, 1, true);
	}

	/**
	 * 承認状況の値を格納する
	 * @param excel 編集中EXCEL
	 * @param baseRow デフォルトの行番号
	 * @param count 何行目？(1～)
	 * @param gougiOya 合議親レコード
	 */
	protected void setShouninJoukyouGougiHeader(EteamXls excel, int baseRow, int count, GMap gougiOya) {
		excel.copyRow(baseRow, baseRow + count, 1, 1, true);
		
		excel.write("koushin_time"		, 0, count, "合議");
		excel.write("bumon_full_name"	, 0, count, gougiOya.get("gougi_name"));
	}

	/**
	 * 承認状況の値を格納する
	 * @param excel 編集中EXCEL
	 * @param baseRow デフォルトの行番号
	 * @param count 何行目？(1～)
	 * @param joukyou 承認ルート or 合議子のレコード(JOIN 承認状況)
	 */
	protected void setShouninJoukyou(EteamXls excel, int baseRow, int count, GMap joukyou) {
		excel.copyRow(baseRow, baseRow + count, 1, 1, true);
		
		String genzaiFlg = joukyou.get("gougi_genzai_flg") != null ? joukyou.get("gougi_genzai_flg") : joukyou.get("genzai_flg");
		if (genzaiFlg.equals("1")) {
			excel.writeBackgroundColor("koushin_time"		, 0, count, Colour.YELLOW);
			excel.writeBackgroundColor("bumon_full_name"	, 0, count, Colour.YELLOW);
			excel.writeBackgroundColor("user_full_name"		, 0, count, Colour.YELLOW);
			excel.writeBackgroundColor("bumon_role_name"	, 0, count, Colour.YELLOW);
			excel.writeBackgroundColor("shori_kengen"		, 0, count, Colour.YELLOW);
			excel.writeBackgroundColor("shounin_joukyou"	, 0, count, Colour.YELLOW);
			excel.writeBackgroundColor("comment"			, 0, count, Colour.YELLOW);
		}
		
		excel.write("koushin_time"		, 0, count, EteamXlsFmt.formatTime(joukyou.get("koushin_time")));
		excel.write("bumon_full_name"	, 0, count, joukyou.get("bumon_full_name"));
		excel.write("user_full_name"	, 0, count, joukyou.get("user_full_name"));
		excel.write("bumon_role_name"	, 0, count, isEmpty(joukyou.get("gyoumu_role_name")) ? joukyou.get("bumon_role_name") : joukyou.get("gyoumu_role_name"));
		excel.write("shori_kengen"		, 0, count, joukyou.get("shounin_shori_kengen_name"));
		excel.write("shounin_joukyou"	, 0, count, joukyou.get("joukyou"));
		excel.write("comment"			, 0, count, joukyou.get("comment"));
		
		int num = 1;
		String comment = joukyou.get("comment");
		if (!isEmpty(comment)) {
			String[] line = comment.split("\n");
			for (int i = 0; i < line.length; i++) {
				num += excel.lineCount(line[i], 22, 1);
			}
		}
		excel.setHeight(baseRow + count, num, 1);
	}

	/**
	 * 
	 * 承認状況の値を格納する
	 * @param excel 編集中EXCEL
	 * @param baseRow デフォルトの行番号
	 * @param count 何行目？(1～)
	 * @param joukyou 承認状況レコード
	 */
	protected void setSousaRireki(EteamXls excel, int baseRow, int count, GMap joukyou) {
		excel.copyRow(baseRow, baseRow + count, 1, 1, true);
		
		excel.write("sousa_koushin_time"	, 0, count, EteamXlsFmt.formatTime(joukyou.get("koushin_time")));
		excel.write("sousa_bumon_full_name"	, 0, count, joukyou.get("bumon_full_name"));
		excel.write("sousa_user_full_name"	, 0, count, joukyou.get("user_full_name"));
		excel.write("sousa_bumon_role_name"	, 0, count, isEmpty(joukyou.get("gyoumu_role_name")) ? joukyou.get("bumon_role_name") : joukyou.get("gyoumu_role_name"));
		excel.write("sousa_shori_kengen"	, 0, count, joukyou.get("shounin_shori_kengen_name"));
		excel.write("sousa_shounin_joukyou"	, 0, count, joukyou.get("joukyou"));
		excel.write("sousa_comment"			, 0, count, joukyou.get("comment"));
		
		int num = 1;
		String comment = joukyou.get("comment");
		if (!isEmpty(comment)) {
			String[] line = comment.split("\n");
			for (int i = 0; i < line.length; i++) {
				num += excel.lineCount(line[i], 22, 1);
			}
		}
		excel.setHeight(baseRow + count, num, 1);
	}
	
	/**
	 * 伝票にマル秘フラグが設定されている場合、タイトル左隣部にマル秘指定の表記を追加
	 * @param excel     追加先Excel
	 * @param denpyouId 伝票ID
	 * @param cellName  タイトル指定のセル名
	 */
	public void setMaruhi(EteamXls excel, String denpyouId, String cellName) {
		WorkflowEventControlLogic wfConLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		boolean maruhiFlg = wfConLogic.isMaruhiFlg(denpyouId);
		if(maruhiFlg){
			excel.writeWithColor(cellName, -1, 0, "㊙", jxl.format.Colour.RED);
		}
	}
	
	/**
	 * カスタマイズ用。起票者名取得処理。
	 * @param shinseiRoute 申請ルート
	 * @return 起票者ユーザー名
	 */
	protected String getKihyoushaUserName(GMap shinseiRoute) {
		return (String)shinseiRoute.get("user_full_name");
	}
	
	protected void setKihyoushaAndDate(EteamXls excel, boolean sokyuuFlg, GMap shinseiRoute, String joukyouCd, GMap shinseiRireki, List<GMap> routeList, String denpyouKbn) {
		if (sokyuuFlg) {
			excel.write("shinsei_bi_title", "起票者");
			excel.write("shinsei_bi", this.getKihyoushaUserName(shinseiRoute));
			excel.hideRow(excel.getCell("kihyou_bi").getRow());
		} else {
			excel.write("shinsei_bi", (! DENPYOU_JYOUTAI.KIHYOU_CHUU.equals(joukyouCd) && null != shinseiRireki) ? EteamXlsFmt.formatDate(shinseiRireki.get("koushin_time")) : "");
			excel.write("kihyou_bi", new SimpleDateFormat("yyyy/MM/dd").format(routeList.get(0).get("touroku_time")));
			excel.write("kihyou_user_name", this.getKihyoushaUserName(shinseiRoute));
		}
	}
}