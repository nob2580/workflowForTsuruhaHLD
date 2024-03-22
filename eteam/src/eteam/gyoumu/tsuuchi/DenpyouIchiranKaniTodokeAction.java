package eteam.gyoumu.tsuuchi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamFileLogic;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.BUHIN_FORMAT;
import eteam.common.select.KaniTodokeCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic.KaniJouken;
import eteam.gyoumu.kanitodoke.KaniTodokeLogic;
import eteam.gyoumu.kihyounavi.DenpyouKanriLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票一覧画面Action
 */
@Getter @Setter @ToString
public class DenpyouIchiranKaniTodokeAction extends DenpyouIchiranAction {

//＜定数＞
	/** list_item_control.nameで簡易用一覧に出すもの */
	final static String[] KANI_VIEW_ITEMNAME = {
		 "joutai"
		,"id"
		,"serialNo"
		,"shubetsu"
		,"shouninbi"
		,"kihyou"
		,"shoyuu"
	};

//＜画面入力＞
	/** 集計(起票部門) */
	String shuukeiKihyouBumon;
	/** 集計(起票者) */
	String shuukeiKihyousha;
	
	/** 簡易届項目(本体1) */
	String[] shinsei01 = {"",""};
	/** 簡易届項目(本体2) */
	String[] shinsei02 = {"",""};
	/** 簡易届項目(本体3) */
	String[] shinsei03 = {"",""};
	/** 簡易届項目(本体4) */
	String[] shinsei04 = {"",""};
	/** 簡易届項目(本体5) */
	String[] shinsei05 = {"",""};
	/** 簡易届項目(本体6) */
	String[] shinsei06 = {"",""};
	/** 簡易届項目(本体7) */
	String[] shinsei07 = {"",""};
	/** 簡易届項目(本体8) */
	String[] shinsei08 = {"",""};
	/** 簡易届項目(本体9) */
	String[] shinsei09 = {"",""};
	/** 簡易届項目(本体10) */
	String[] shinsei10 = {"",""};
	/** 簡易届項目(本体11) */
	String[] shinsei11 = {"",""};
	/** 簡易届項目(本体12) */
	String[] shinsei12 = {"",""};
	/** 簡易届項目(本体13) */
	String[] shinsei13 = {"",""};
	/** 簡易届項目(本体14) */
	String[] shinsei14 = {"",""};
	/** 簡易届項目(本体15) */
	String[] shinsei15 = {"",""};
	/** 簡易届項目(本体16) */
	String[] shinsei16 = {"",""};
	/** 簡易届項目(本体17) */
	String[] shinsei17 = {"",""};
	/** 簡易届項目(本体18) */
	String[] shinsei18 = {"",""};
	/** 簡易届項目(本体19) */
	String[] shinsei19 = {"",""};
	/** 簡易届項目(本体20) */
	String[] shinsei20 = {"",""};
	/** 簡易届項目(本体21) */
	String[] shinsei21 = {"",""};
	/** 簡易届項目(本体22) */
	String[] shinsei22 = {"",""};
	/** 簡易届項目(本体23) */
	String[] shinsei23 = {"",""};
	/** 簡易届項目(本体24) */
	String[] shinsei24 = {"",""};
	/** 簡易届項目(本体25) */
	String[] shinsei25 = {"",""};
	/** 簡易届項目(本体26) */
	String[] shinsei26 = {"",""};
	/** 簡易届項目(本体27) */
	String[] shinsei27 = {"",""};
	/** 簡易届項目(本体28) */
	String[] shinsei28 = {"",""};
	/** 簡易届項目(本体29) */
	String[] shinsei29 = {"",""};
	/** 簡易届項目(本体30) */
	String[] shinsei30 = {"",""};
	/** 簡易届項目(明細1) */
	String[] meisai01 = {"",""};
	/** 簡易届項目(明細2) */
	String[] meisai02 = {"",""};
	/** 簡易届項目(明細3) */
	String[] meisai03 = {"",""};
	/** 簡易届項目(明細4) */
	String[] meisai04 = {"",""};
	/** 簡易届項目(明細5) */
	String[] meisai05 = {"",""};
	/** 簡易届項目(明細6) */
	String[] meisai06 = {"",""};
	/** 簡易届項目(明細7) */
	String[] meisai07 = {"",""};
	/** 簡易届項目(明細8) */
	String[] meisai08 = {"",""};
	/** 簡易届項目(明細9) */
	String[] meisai09 = {"",""};
	/** 簡易届項目(明細10) */
	String[] meisai10 = {"",""};
	/** 簡易届項目(明細11) */
	String[] meisai11 = {"",""};
	/** 簡易届項目(明細12) */
	String[] meisai12 = {"",""};
	/** 簡易届項目(明細13) */
	String[] meisai13 = {"",""};
	/** 簡易届項目(明細14) */
	String[] meisai14 = {"",""};
	/** 簡易届項目(明細15) */
	String[] meisai15 = {"",""};
	/** 簡易届項目(明細16) */
	String[] meisai16 = {"",""};
	/** 簡易届項目(明細17) */
	String[] meisai17 = {"",""};
	/** 簡易届項目(明細18) */
	String[] meisai18 = {"",""};
	/** 簡易届項目(明細19) */
	String[] meisai19 = {"",""};
	/** 簡易届項目(明細20) */
	String[] meisai20 = {"",""};
	/** 簡易届項目(明細21) */
	String[] meisai21 = {"",""};
	/** 簡易届項目(明細22) */
	String[] meisai22 = {"",""};
	/** 簡易届項目(明細23) */
	String[] meisai23 = {"",""};
	/** 簡易届項目(明細24) */
	String[] meisai24 = {"",""};
	/** 簡易届項目(明細25) */
	String[] meisai25 = {"",""};
	/** 簡易届項目(明細26) */
	String[] meisai26 = {"",""};
	/** 簡易届項目(明細27) */
	String[] meisai27 = {"",""};
	/** 簡易届項目(明細28) */
	String[] meisai28 = {"",""};
	/** 簡易届項目(明細29) */
	String[] meisai29 = {"",""};
	/** 簡易届項目(明細30) */
	String[] meisai30 = {"",""};


//＜画面入力以外＞
	//ドロップダウン等
	
	/** 簡易届申請部項目フォーマットリスト */
	List<GMap> shinseiLayoutList = null;
	/** 簡易届明細部項目フォーマットリスト */
	List<GMap> meisaiLayoutList = null;
	/** 簡易届申請部選択肢リスト */
	List<GMap> shinseiOptionList = null;
	/** 簡易届明細部選択肢リスト */
	List<GMap> meisaiOptionList = null;
	
	/** 簡易届列 和名リスト */
	List<String> wameiList;
	/** 簡易届列 カラム名リスト */
	List<String> columnList;
	/** 簡易届列 TYPE(text,textareaとか) */
	List<String> typeList;
	/** 簡易届列 フォーマット(string,numberとかtextに対する詳細) */
	List<String> formatList;
	/** 簡易届列 小数点以下桁数リスト */
	List<String> decPointList;
	/** 時刻一覧リスト */
	List<GMap> jikokuList;
	/** 簡易届専用 */
	boolean kanitodokeFlag = true;
	
	/** ルート判定金額・件名・内容　カラム名 */
	String[] kaniKoumokuItemName;

//＜画面入力以外＞
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(DenpyouIchiranKaniTodokeAction.class);
	/** 簡易届検索 */
	KaniTodokeCategoryLogic  kaniTodokeLogic;
	/** ユーザー定義届書作成　SELECT */
	KaniTodokeLogic kaniTodokeLogicSelect;
	/** 伝票管理 */
	DenpyouKanriLogic denpyouKanriLogic;

//＜入力チェック＞
	//TODO: 親クラスのチェック＋アルファで
	@Override protected void formatCheck() {
		
		//共通部チェック
		super.formatCheck();
		//簡易届申請部チェック
		for(int i = 0 ; i < shinseiLayoutList.size() ; i++ ){
			GMap mp = shinseiLayoutList.get(i);
			if(mp.get("buhin_format") == null){continue;};
			String name = mp.get("item_name").toString();
			String type = mp.get("buhin_format").toString();
			String chkStrFrom = "";
			String chkStrTo = "";
			switch(name){
			case "shinsei01":chkStrFrom = shinsei01[0]; if(shinsei01.length == 2){chkStrTo = shinsei01[1];}; break;
			case "shinsei02":chkStrFrom = shinsei02[0]; if(shinsei02.length == 2){chkStrTo = shinsei02[1];}; break;
			case "shinsei03":chkStrFrom = shinsei03[0]; if(shinsei03.length == 2){chkStrTo = shinsei03[1];}; break;
			case "shinsei04":chkStrFrom = shinsei04[0]; if(shinsei04.length == 2){chkStrTo = shinsei04[1];}; break;
			case "shinsei05":chkStrFrom = shinsei05[0]; if(shinsei05.length == 2){chkStrTo = shinsei05[1];}; break;
			case "shinsei06":chkStrFrom = shinsei06[0]; if(shinsei06.length == 2){chkStrTo = shinsei06[1];}; break;
			case "shinsei07":chkStrFrom = shinsei07[0]; if(shinsei07.length == 2){chkStrTo = shinsei07[1];}; break;
			case "shinsei08":chkStrFrom = shinsei08[0]; if(shinsei08.length == 2){chkStrTo = shinsei08[1];}; break;
			case "shinsei09":chkStrFrom = shinsei09[0]; if(shinsei09.length == 2){chkStrTo = shinsei09[1];}; break;
			case "shinsei10":chkStrFrom = shinsei10[0]; if(shinsei10.length == 2){chkStrTo = shinsei10[1];}; break;
			case "shinsei11":chkStrFrom = shinsei11[0]; if(shinsei11.length == 2){chkStrTo = shinsei11[1];}; break;
			case "shinsei12":chkStrFrom = shinsei12[0]; if(shinsei12.length == 2){chkStrTo = shinsei12[1];}; break;
			case "shinsei13":chkStrFrom = shinsei13[0]; if(shinsei13.length == 2){chkStrTo = shinsei13[1];}; break;
			case "shinsei14":chkStrFrom = shinsei14[0]; if(shinsei14.length == 2){chkStrTo = shinsei14[1];}; break;
			case "shinsei15":chkStrFrom = shinsei15[0]; if(shinsei15.length == 2){chkStrTo = shinsei15[1];}; break;
			case "shinsei16":chkStrFrom = shinsei16[0]; if(shinsei16.length == 2){chkStrTo = shinsei16[1];}; break;
			case "shinsei17":chkStrFrom = shinsei17[0]; if(shinsei17.length == 2){chkStrTo = shinsei17[1];}; break;
			case "shinsei18":chkStrFrom = shinsei18[0]; if(shinsei18.length == 2){chkStrTo = shinsei18[1];}; break;
			case "shinsei19":chkStrFrom = shinsei19[0]; if(shinsei19.length == 2){chkStrTo = shinsei19[1];}; break;
			case "shinsei20":chkStrFrom = shinsei20[0]; if(shinsei20.length == 2){chkStrTo = shinsei20[1];}; break;
			case "shinsei21":chkStrFrom = shinsei21[0]; if(shinsei21.length == 2){chkStrTo = shinsei21[1];}; break;
			case "shinsei22":chkStrFrom = shinsei22[0]; if(shinsei22.length == 2){chkStrTo = shinsei22[1];}; break;
			case "shinsei23":chkStrFrom = shinsei23[0]; if(shinsei23.length == 2){chkStrTo = shinsei23[1];}; break;
			case "shinsei24":chkStrFrom = shinsei24[0]; if(shinsei24.length == 2){chkStrTo = shinsei24[1];}; break;
			case "shinsei25":chkStrFrom = shinsei25[0]; if(shinsei25.length == 2){chkStrTo = shinsei25[1];}; break;
			case "shinsei26":chkStrFrom = shinsei26[0]; if(shinsei26.length == 2){chkStrTo = shinsei26[1];}; break;
			case "shinsei27":chkStrFrom = shinsei27[0]; if(shinsei27.length == 2){chkStrTo = shinsei27[1];}; break;
			case "shinsei28":chkStrFrom = shinsei28[0]; if(shinsei28.length == 2){chkStrTo = shinsei28[1];}; break;
			case "shinsei29":chkStrFrom = shinsei29[0]; if(shinsei29.length == 2){chkStrTo = shinsei29[1];}; break;
			case "shinsei30":chkStrFrom = shinsei30[0]; if(shinsei30.length == 2){chkStrTo = shinsei30[1];}; break;
			default:continue;
			}
			
			switch(type){
			case BUHIN_FORMAT.STRING :
				checkString(chkStrFrom,0,(int)mp.get("max_length"),mp.get("label_name").toString(),false);
				break;
			case BUHIN_FORMAT.NUMBER :
				checkNumberRangeDecimalPoint(chkStrFrom,((BigDecimal)mp.get("min_value")).doubleValue(),((BigDecimal)mp.get("max_value")).doubleValue(),Integer.parseInt((String)mp.get("decimal_point")),mp.get("label_name").toString(),false);
				checkNumberRangeDecimalPoint(chkStrTo  ,((BigDecimal)mp.get("min_value")).doubleValue(),((BigDecimal)mp.get("max_value")).doubleValue(),Integer.parseInt((String)mp.get("decimal_point")),mp.get("label_name").toString(),false);
				break;
			case BUHIN_FORMAT.DATE   :
				checkDate(chkStrFrom,mp.get("label_name").toString(),false);
				checkDate(chkStrTo  ,mp.get("label_name").toString(),false);
				break;
			case BUHIN_FORMAT.MONEY  :
				checkKingaku(chkStrFrom,((BigDecimal)mp.get("min_value")).longValue(),((BigDecimal)mp.get("max_value")).longValue(),mp.get("label_name").toString(),false);
				checkKingaku(chkStrTo  ,((BigDecimal)mp.get("min_value")).longValue(),((BigDecimal)mp.get("max_value")).longValue(),mp.get("label_name").toString(),false);
				break;
			case BUHIN_FORMAT.TIME   :
				checkTime(chkStrFrom,mp.get("label_name").toString(),false);
				checkTime(chkStrTo  ,mp.get("label_name").toString(),false);
				break;
			default:
				break;
			}
		}
		//簡易届明細部チェック
		for(int i = 0 ; i < meisaiLayoutList.size() ; i++ ){
			GMap mp = meisaiLayoutList.get(i);
			if(mp.get("buhin_format") == null){continue;};
			String name = mp.get("item_name").toString();
			String type = mp.get("buhin_format").toString();
			String chkStrFrom = "";
			String chkStrTo = "";
			switch(name){
			case "meisai01":chkStrFrom = meisai01[0]; if(meisai01.length == 2){chkStrTo = meisai01[1];}; break;
			case "meisai02":chkStrFrom = meisai02[0]; if(meisai02.length == 2){chkStrTo = meisai02[1];}; break;
			case "meisai03":chkStrFrom = meisai03[0]; if(meisai03.length == 2){chkStrTo = meisai03[1];}; break;
			case "meisai04":chkStrFrom = meisai04[0]; if(meisai04.length == 2){chkStrTo = meisai04[1];}; break;
			case "meisai05":chkStrFrom = meisai05[0]; if(meisai05.length == 2){chkStrTo = meisai05[1];}; break;
			case "meisai06":chkStrFrom = meisai06[0]; if(meisai06.length == 2){chkStrTo = meisai06[1];}; break;
			case "meisai07":chkStrFrom = meisai07[0]; if(meisai07.length == 2){chkStrTo = meisai07[1];}; break;
			case "meisai08":chkStrFrom = meisai08[0]; if(meisai08.length == 2){chkStrTo = meisai08[1];}; break;
			case "meisai09":chkStrFrom = meisai09[0]; if(meisai09.length == 2){chkStrTo = meisai09[1];}; break;
			case "meisai10":chkStrFrom = meisai10[0]; if(meisai10.length == 2){chkStrTo = meisai10[1];}; break;
			case "meisai11":chkStrFrom = meisai11[0]; if(meisai11.length == 2){chkStrTo = meisai11[1];}; break;
			case "meisai12":chkStrFrom = meisai12[0]; if(meisai12.length == 2){chkStrTo = meisai12[1];}; break;
			case "meisai13":chkStrFrom = meisai13[0]; if(meisai13.length == 2){chkStrTo = meisai13[1];}; break;
			case "meisai14":chkStrFrom = meisai14[0]; if(meisai14.length == 2){chkStrTo = meisai14[1];}; break;
			case "meisai15":chkStrFrom = meisai15[0]; if(meisai15.length == 2){chkStrTo = meisai15[1];}; break;
			case "meisai16":chkStrFrom = meisai16[0]; if(meisai16.length == 2){chkStrTo = meisai16[1];}; break;
			case "meisai17":chkStrFrom = meisai17[0]; if(meisai17.length == 2){chkStrTo = meisai17[1];}; break;
			case "meisai18":chkStrFrom = meisai18[0]; if(meisai18.length == 2){chkStrTo = meisai18[1];}; break;
			case "meisai19":chkStrFrom = meisai19[0]; if(meisai19.length == 2){chkStrTo = meisai19[1];}; break;
			case "meisai20":chkStrFrom = meisai20[0]; if(meisai20.length == 2){chkStrTo = meisai20[1];}; break;
			case "meisai21":chkStrFrom = meisai21[0]; if(meisai21.length == 2){chkStrTo = meisai21[1];}; break;
			case "meisai22":chkStrFrom = meisai22[0]; if(meisai22.length == 2){chkStrTo = meisai22[1];}; break;
			case "meisai23":chkStrFrom = meisai23[0]; if(meisai23.length == 2){chkStrTo = meisai23[1];}; break;
			case "meisai24":chkStrFrom = meisai24[0]; if(meisai24.length == 2){chkStrTo = meisai24[1];}; break;
			case "meisai25":chkStrFrom = meisai25[0]; if(meisai25.length == 2){chkStrTo = meisai25[1];}; break;
			case "meisai26":chkStrFrom = meisai26[0]; if(meisai26.length == 2){chkStrTo = meisai26[1];}; break;
			case "meisai27":chkStrFrom = meisai27[0]; if(meisai27.length == 2){chkStrTo = meisai27[1];}; break;
			case "meisai28":chkStrFrom = meisai28[0]; if(meisai28.length == 2){chkStrTo = meisai28[1];}; break;
			case "meisai29":chkStrFrom = meisai29[0]; if(meisai29.length == 2){chkStrTo = meisai29[1];}; break;
			case "meisai30":chkStrFrom = meisai30[0]; if(meisai30.length == 2){chkStrTo = meisai30[1];}; break;
			default:continue;
			}
			
			switch(type){
			case BUHIN_FORMAT.STRING :
				checkString(chkStrFrom,0,(int)mp.get("max_length"),mp.get("label_name").toString(),false);
				break;
			case BUHIN_FORMAT.NUMBER :
				checkNumberRangeDecimalPoint(chkStrFrom,((BigDecimal)mp.get("min_value")).doubleValue(),((BigDecimal)mp.get("max_value")).doubleValue(),Integer.parseInt((String)mp.get("decimal_point")),mp.get("label_name").toString(),false);
				checkNumberRangeDecimalPoint(chkStrTo  ,((BigDecimal)mp.get("min_value")).doubleValue(),((BigDecimal)mp.get("max_value")).doubleValue(),Integer.parseInt((String)mp.get("decimal_point")),mp.get("label_name").toString(),false);
				break;
			case BUHIN_FORMAT.DATE   :
				checkDate(chkStrFrom,mp.get("label_name").toString(),false);
				checkDate(chkStrTo  ,mp.get("label_name").toString(),false);
				break;
			case BUHIN_FORMAT.MONEY  :
				checkKingaku(chkStrFrom,((BigDecimal)mp.get("min_value")).longValue(),((BigDecimal)mp.get("max_value")).longValue(),mp.get("label_name").toString(),false);
				checkKingaku(chkStrTo  ,((BigDecimal)mp.get("min_value")).longValue(),((BigDecimal)mp.get("max_value")).longValue(),mp.get("label_name").toString(),false);
				break;
			case BUHIN_FORMAT.TIME   :
				checkTime(chkStrFrom,mp.get("label_name").toString(),false);
				checkTime(chkStrTo  ,mp.get("label_name").toString(),false);
				break;
			default:
				break;
			}
		}
		
	}

	//TODO: 親クラスのチェック＋アルファで
	@Override protected void hissuCheck(int eventNum) {}

//＜初期化＞
	/**
	 * DB接続を初期化する。
	 * @param connection コネクション
	 */
	@Override protected void initConnection(EteamConnection connection){
		super.initConnection(connection);
		kaniTodokeLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);
		kaniTodokeLogic.layoutKoteiNashi = true;
		denpyouKanriLogic = EteamContainer.getComponent(DenpyouKanriLogic.class, connection);
		kaniTodokeLogicSelect = EteamContainer.getComponent(KaniTodokeLogic.class, connection);
		// 内部コード取得
		SystemKanriCategoryLogic sysLc = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		jikokuList = sysLc.loadNaibuCdSetting("jikoku_ichiran");
	}
	
	/**
	 * 画面パーツを作成する。
	 */
	@Override protected void makeParts(){
		//簡易届申請のレイアウト情報
		int maxversion = kaniTodokeLogic.findMaxVersion(kensakuDenpyouShubetsu);
		shinseiLayoutList = kaniTodokeLogic.loadLayout("shinsei", kensakuDenpyouShubetsu, String.valueOf(maxversion));
		meisaiLayoutList = kaniTodokeLogic.loadLayout("meisai", kensakuDenpyouShubetsu, String.valueOf(maxversion));
		shinseiOptionList = kaniTodokeLogic.loadOption("shinsei", kensakuDenpyouShubetsu, String.valueOf(maxversion));
		meisaiOptionList = kaniTodokeLogic.loadOption("meisai", kensakuDenpyouShubetsu, String.valueOf(maxversion));
		List<String>[] tmp = kaniTodokeLogic.getLayoutInfo(kensakuDenpyouShubetsu);
		columnList = tmp[0];
		wameiList = tmp[1];
		typeList = tmp[3];
		formatList = tmp[2];
		decPointList= tmp[4];
		//カラム名もここで一緒に取得
		kaniKoumokuItemName = getKaniKoumokuItemName(kensakuDenpyouShubetsu);
		
		//空のないプルダウン（必須項目）は空Mapを作成。最初にブランクを表示させたいため、先頭に追加。
		shinseiOptionList.addAll(0, setEmptyMapOfPullDown(shinseiLayoutList, shinseiOptionList));
		meisaiOptionList.addAll(0, setEmptyMapOfPullDown(meisaiLayoutList, meisaiOptionList));
		
		//共通部分
		super.makeParts();
		
		//list_item_controlから簡易届用一覧に出さないやつを消す（親共通部分の上書き）
		for(DenpyouDisplayItem item : itemList){
			if(!ArrayUtils.contains(KANI_VIEW_ITEMNAME, item.getName())){
				item.setDisplay(false);
			}
		}

		//再処理用のURLを作る（親共通部分の上書き）
		String urlParam = makeUrlParam();
		beforeSelect= "pageNo=" + pageNo + "&sortKbn=" + sortKbn + urlParam;
	}

	
//＜イベント＞
	/**
	 * 検索イベント
	 * @return 処理結果
	 */
	@Override public String kensaku(){
		//メニューの件数リンクからとんだ時に予算関連の入力を無効化するように
		HttpServletRequest request = ServletActionContext.getRequest();
		if(request.getParameter("kensakuDenpyouId") == null){
			kianBangouInput = "2";
			kianBangouEnd = "2";
			kianBangouUnyou = "";
			kianBangouNoShishutsuIrai = "";
		}
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);
			
			//画面部品の作成
			makeParts();
			
			// 1.入力チェック
			formatCheck();
			hissuCheck(2);
			if(! errorList.isEmpty()){
				return "error";
			}
			this.soukanCheck();
			if(! errorList.isEmpty()){
				return "error";
			}

			// 2.データ存在チェック
			
			// 3.アクセス可能チェック なし
			// 4.処理 
			

			// クエリパラメーターの作成
			var queryParameter = this.createQueryParameter();

			// データの全件数取得
			totalCount = tsuuchiLogic.findDenpyouIchiranKensakuCountKani(queryParameter, makeKaniJouken());
			
			// 1ページ最大表示件数を取得 
			int pagemax = Integer.parseInt(setting.recordNumPerPage());

			// 総ページ数の計算
			totalPage = EteamCommon.calcTotalPageNum(pagemax, totalCount);
			if(totalPage == 0){
				totalPage = 1;
			}
			
			// 表示ページ番号が総ページ数より大きかったら、総ページ数を表示ページ番号にする。
			if(isEmpty(pageNo) || "0".equals(pageNo)){
				pageNo = "1";
			} else if(Integer.parseInt(pageNo) > totalPage) {
				pageNo = String.valueOf(totalPage);
			}
			
			// ページングリンクURLを設定
			String urlParam = makeUrlParam();
			pagingLink = "denpyou_ichiran_kensaku_kanitodoke?sortKbn="+sortKbn+ urlParam+"&";
			
			list = tsuuchiLogic.loadDenpyouIchiranKensakuKani(
					  false
					, queryParameter
					, makeKaniJouken()
					, Integer.parseInt(pageNo)
					, pagemax
					, sortKbn);
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			//フォーマット
			formatList4JSP(kaniKoumokuItemName); //簡易届項目は一覧テーブル格納時点でフォーマット済なので共通項目のみでOK
			formatList4JSPKaniTodoke();//一部右寄せ必要なのでクラス指定用処理追加
			
			//5.戻り値を返す
			return "success";
		}
	}


	/**
	 * CSV出力イベント
	 * @return 処理結果
	 */
	@Override public String csvOutput(){
		PrintWriter writer = null;
		try(EteamConnection connection = EteamConnection.connect()){
			initConnection(connection);

			//画面部品の作成
			makeParts();

			// 1.入力チェック
			formatCheck();
			hissuCheck(4);
			if(! errorList.isEmpty()){
				reKensaku();
				return "error";
			}
			this.soukanCheck();
			if(! errorList.isEmpty()){
				reKensaku();
				return "error";
			}

			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理 

			//--------------------------
			//一覧取得
			//--------------------------
			// クエリパラメーターの作成
			var queryParameter = this.createQueryParameter();
			
			//チェックボックスの選択状態により、集計区分（両方・部門・ユーザー・なし)を判定
			String sortKbnShuukei= null;
			if("1".equals(shuukeiKihyouBumon)){
				sortKbnShuukei = shuukeiKihyouBumon.equals(shuukeiKihyousha)? TsuuchiCategoryLogic.SORT_KBN_ALL : TsuuchiCategoryLogic.SORT_KBN_BUMON;
			}else{
				if ("1".equals(shuukeiKihyousha))
				{
					sortKbnShuukei = TsuuchiCategoryLogic.SORT_KBN_USER;
				}
			}
			
			//SELECTする
			list = tsuuchiLogic.loadDenpyouIchiranKensakuKani(
					  true
					, queryParameter
					, makeKaniJouken()
					, 0 //ページ指定なし
					, 0
					, (null==sortKbnShuukei)? sortKbn : sortKbnShuukei);
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				reKensaku();
				return "error";
			}

			//--------------------------
			//取得した一覧に集計行を差し込み
			//--------------------------
			if(null != sortKbnShuukei){
				list = denpyouIchiranLg.kaniTodokeShuukei(list, columnList, formatList, decPointList, sortKbnShuukei);
			}

			//--------------------------
			//CSV作る
			//--------------------------
			//ContentType判定
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());

			// CSVファイルデータを作る
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(ContentType.EXCEL);
		    response.setHeader("Content-Disposition", EteamCommon.contentDisposition(browserCode, true, CSV_FILENAME));
		    response.setCharacterEncoding(Encoding.MS932);
		    writer = response.getWriter();
			
			// 1行目：カラム名(和名)のリストを作る
			List<Object> colName = new ArrayList<Object>();
			colName.add("状態");
			colName.add("伝票ID");
			colName.add("伝票番号");
			colName.add("伝票種別");
			colName.add("最終承認日");
			colName.add("起票日");
			colName.add("起票者部門コード");
			colName.add("起票者部門");
			colName.add("起票者社員番号");
			colName.add("起票者名");
			colName.add("所有者部門");
			colName.add("所有者名");
			for(String wamei : wameiList){
				colName.add(wamei);
			}
			EteamFileLogic.outputCsvRecord(writer, colName);
			
			// 2行目以降：データ部のリストを作る
			String maruhiHyoujiFlg = setting.maruhiHyoujiSeigyoFlg();
			for (GMap map : list) {
				List<Object> csvRecord = new ArrayList<>();
				if(map.containsKey("syuukei")){
					csvRecord.add(map.get("name"));
					csvRecord.add(""); //denpyou_id
					csvRecord.add(""); //serial_no
					csvRecord.add(""); //denpyou_shubetsu
					csvRecord.add(""); //shouninbi
					csvRecord.add(""); //touroku_time
					csvRecord.add(map.get("kihyouBumonCd"));
					csvRecord.add(map.get("bumon_full_name"));
					csvRecord.add(map.get("shain_no"));
					csvRecord.add(map.get("user_full_name"));
					csvRecord.add(""); //gen_bumon_full_name
					csvRecord.add(""); //gen_name
				}else{
					csvRecord.add(map.get("name") + "(" + map.get("cur_cnt") + "/" + map.get("all_cnt") + ")");
					csvRecord.add(map.get("denpyou_id"));
					csvRecord.add(String.format("%1$08d", map.getObject("serial_no"))); // 伝票番号
					csvRecord.add(map.get("denpyou_shubetsu"));
					csvRecord.add(map.get("shouninbi"));
					csvRecord.add(map.get("touroku_time"));
					csvRecord.add(map.get("kihyouBumonCd"));
					csvRecord.add(map.get("bumon_full_name"));
					csvRecord.add(map.get("shain_no"));
					csvRecord.add(map.get("user_full_name"));
					csvRecord.add(map.get("gen_bumon_full_name"));
					csvRecord.add(map.get("gen_name"));
				}
				for(String column : columnList){
					var columnItem = map.get(column);
					if("1".equals(map.get("maruhi_flg"))) {
						//formatList4JSPと同じことする　andとorでこんがらがったのでネストとりあえずこのまま　スッキリさせたい
						//金額に一致しないとき、
						if(!column.equals(kaniKoumokuItemName[0])) {
							//会社設定が0　または　件名と内容に一致しない場合
							if("0".equals(maruhiHyoujiFlg) || !(column.equals(kaniKoumokuItemName[1])||column.equals(kaniKoumokuItemName[2]))) {
								columnItem = "";
							}
						}
					}
// csvRecord.add(map.get(column)); //一覧テーブル上でフォーマットされているのでそのまま出す
					csvRecord.add(columnItem);
				}
				EteamFileLogic.outputCsvRecord(writer, csvRecord);
			}
			//5.戻り値を返す
			return "success";
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}finally{
			if(writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * URLに対するGETパラメータ文字列を作る
	 * @return GETパラメータ文字列
	 */
	@Override protected String makeUrlParam() {
		try {
			String urlParam = super.makeUrlParam();
			if(columnList.contains("shinsei01")){
				urlParam = urlParam.concat("&shinsei01=" + URLEncoder.encode(shinsei01[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei01=" + (shinsei01.length == 2 ? URLEncoder.encode(shinsei01[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei02")){
				urlParam = urlParam.concat("&shinsei02=" + URLEncoder.encode(shinsei02[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei02=" + (shinsei02.length == 2 ? URLEncoder.encode(shinsei02[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei03")){
				urlParam = urlParam.concat("&shinsei03=" + URLEncoder.encode(shinsei03[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei03=" + (shinsei03.length == 2 ? URLEncoder.encode(shinsei03[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei04")){
				urlParam = urlParam.concat("&shinsei04=" + URLEncoder.encode(shinsei04[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei04=" + (shinsei04.length == 2 ? URLEncoder.encode(shinsei04[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei05")){
				urlParam = urlParam.concat("&shinsei05=" + URLEncoder.encode(shinsei05[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei05=" + (shinsei05.length == 2 ? URLEncoder.encode(shinsei05[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei06")){
				urlParam = urlParam.concat("&shinsei06=" + URLEncoder.encode(shinsei06[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei06=" + (shinsei06.length == 2 ? URLEncoder.encode(shinsei06[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei07")){
				urlParam = urlParam.concat("&shinsei07=" + URLEncoder.encode(shinsei07[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei07=" + (shinsei07.length == 2 ? URLEncoder.encode(shinsei07[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei08")){
				urlParam = urlParam.concat("&shinsei08=" + URLEncoder.encode(shinsei08[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei08=" + (shinsei08.length == 2 ? URLEncoder.encode(shinsei08[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei09")){
				urlParam = urlParam.concat("&shinsei09=" + URLEncoder.encode(shinsei09[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei09=" + (shinsei09.length == 2 ? URLEncoder.encode(shinsei09[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei10")){
				urlParam = urlParam.concat("&shinsei10=" + URLEncoder.encode(shinsei10[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei10=" + (shinsei10.length == 2 ? URLEncoder.encode(shinsei10[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei11")){
				urlParam = urlParam.concat("&shinsei11=" + URLEncoder.encode(shinsei11[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei11=" + (shinsei11.length == 2 ? URLEncoder.encode(shinsei11[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei12")){
				urlParam = urlParam.concat("&shinsei12=" + URLEncoder.encode(shinsei12[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei12=" + (shinsei12.length == 2 ? URLEncoder.encode(shinsei12[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei13")){
				urlParam = urlParam.concat("&shinsei13=" + URLEncoder.encode(shinsei13[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei13=" + (shinsei13.length == 2 ? URLEncoder.encode(shinsei13[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei14")){
				urlParam = urlParam.concat("&shinsei14=" + URLEncoder.encode(shinsei14[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei14=" + (shinsei14.length == 2 ? URLEncoder.encode(shinsei14[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei15")){
				urlParam = urlParam.concat("&shinsei15=" + URLEncoder.encode(shinsei15[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei15=" + (shinsei15.length == 2 ? URLEncoder.encode(shinsei15[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei16")){
				urlParam = urlParam.concat("&shinsei16=" + URLEncoder.encode(shinsei16[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei16=" + (shinsei16.length == 2 ? URLEncoder.encode(shinsei16[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei17")){
				urlParam = urlParam.concat("&shinsei17=" + URLEncoder.encode(shinsei17[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei17=" + (shinsei17.length == 2 ? URLEncoder.encode(shinsei17[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei18")){
				urlParam = urlParam.concat("&shinsei18=" + URLEncoder.encode(shinsei18[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei18=" + (shinsei18.length == 2 ? URLEncoder.encode(shinsei18[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei19")){
				urlParam = urlParam.concat("&shinsei19=" + URLEncoder.encode(shinsei19[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei19=" + (shinsei19.length == 2 ? URLEncoder.encode(shinsei19[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei20")){
				urlParam = urlParam.concat("&shinsei20=" + URLEncoder.encode(shinsei20[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei20=" + (shinsei20.length == 2 ? URLEncoder.encode(shinsei20[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei21")){
				urlParam = urlParam.concat("&shinsei21=" + URLEncoder.encode(shinsei21[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei21=" + (shinsei21.length == 2 ? URLEncoder.encode(shinsei21[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei22")){
				urlParam = urlParam.concat("&shinsei22=" + URLEncoder.encode(shinsei22[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei22=" + (shinsei22.length == 2 ? URLEncoder.encode(shinsei22[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei23")){
				urlParam = urlParam.concat("&shinsei23=" + URLEncoder.encode(shinsei23[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei23=" + (shinsei23.length == 2 ? URLEncoder.encode(shinsei23[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei24")){
				urlParam = urlParam.concat("&shinsei24=" + URLEncoder.encode(shinsei24[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei24=" + (shinsei24.length == 2 ? URLEncoder.encode(shinsei24[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei25")){
				urlParam = urlParam.concat("&shinsei25=" + URLEncoder.encode(shinsei25[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei25=" + (shinsei25.length == 2 ? URLEncoder.encode(shinsei25[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei26")){
				urlParam = urlParam.concat("&shinsei26=" + URLEncoder.encode(shinsei26[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei26=" + (shinsei26.length == 2 ? URLEncoder.encode(shinsei26[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei27")){
				urlParam = urlParam.concat("&shinsei27=" + URLEncoder.encode(shinsei27[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei27=" + (shinsei27.length == 2 ? URLEncoder.encode(shinsei27[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei28")){
				urlParam = urlParam.concat("&shinsei28=" + URLEncoder.encode(shinsei28[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei28=" + (shinsei28.length == 2 ? URLEncoder.encode(shinsei28[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei29")){
				urlParam = urlParam.concat("&shinsei29=" + URLEncoder.encode(shinsei29[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei29=" + (shinsei29.length == 2 ? URLEncoder.encode(shinsei29[1],"UTF-8") : "" ));
			}
			if(columnList.contains("shinsei30")){
				urlParam = urlParam.concat("&shinsei30=" + URLEncoder.encode(shinsei30[0],"UTF-8"));
				urlParam = urlParam.concat("&shinsei30=" + (shinsei30.length == 2 ? URLEncoder.encode(shinsei30[1],"UTF-8") : "" ));
			}

			if(columnList.contains("meisai01")){
				urlParam = urlParam.concat("&meisai01=" + URLEncoder.encode(meisai01[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai01=" + (meisai01.length == 2 ? URLEncoder.encode(meisai01[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai02")){
				urlParam = urlParam.concat("&meisai02=" + URLEncoder.encode(meisai02[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai02=" + (meisai02.length == 2 ? URLEncoder.encode(meisai02[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai03")){
				urlParam = urlParam.concat("&meisai03=" + URLEncoder.encode(meisai03[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai03=" + (meisai03.length == 2 ? URLEncoder.encode(meisai03[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai04")){
				urlParam = urlParam.concat("&meisai04=" + URLEncoder.encode(meisai04[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai04=" + (meisai04.length == 2 ? URLEncoder.encode(meisai04[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai05")){
				urlParam = urlParam.concat("&meisai05=" + URLEncoder.encode(meisai05[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai05=" + (meisai05.length == 2 ? URLEncoder.encode(meisai05[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai06")){
				urlParam = urlParam.concat("&meisai06=" + URLEncoder.encode(meisai06[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai06=" + (meisai06.length == 2 ? URLEncoder.encode(meisai06[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai07")){
				urlParam = urlParam.concat("&meisai07=" + URLEncoder.encode(meisai07[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai07=" + (meisai07.length == 2 ? URLEncoder.encode(meisai07[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai08")){
				urlParam = urlParam.concat("&meisai08=" + URLEncoder.encode(meisai08[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai08=" + (meisai08.length == 2 ? URLEncoder.encode(meisai08[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai09")){
				urlParam = urlParam.concat("&meisai09=" + URLEncoder.encode(meisai09[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai09=" + (meisai09.length == 2 ? URLEncoder.encode(meisai09[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai10")){
				urlParam = urlParam.concat("&meisai10=" + URLEncoder.encode(meisai10[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai10=" + (meisai10.length == 2 ? URLEncoder.encode(meisai10[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai11")){
				urlParam = urlParam.concat("&meisai11=" + URLEncoder.encode(meisai11[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai11=" + (meisai11.length == 2 ? URLEncoder.encode(meisai11[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai12")){
				urlParam = urlParam.concat("&meisai12=" + URLEncoder.encode(meisai12[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai12=" + (meisai12.length == 2 ? URLEncoder.encode(meisai12[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai13")){
				urlParam = urlParam.concat("&meisai13=" + URLEncoder.encode(meisai13[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai13=" + (meisai13.length == 2 ? URLEncoder.encode(meisai13[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai14")){
				urlParam = urlParam.concat("&meisai14=" + URLEncoder.encode(meisai14[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai14=" + (meisai14.length == 2 ? URLEncoder.encode(meisai14[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai15")){
				urlParam = urlParam.concat("&meisai15=" + URLEncoder.encode(meisai15[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai15=" + (meisai15.length == 2 ? URLEncoder.encode(meisai15[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai16")){
				urlParam = urlParam.concat("&meisai16=" + URLEncoder.encode(meisai16[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai16=" + (meisai16.length == 2 ? URLEncoder.encode(meisai16[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai17")){
				urlParam = urlParam.concat("&meisai17=" + URLEncoder.encode(meisai17[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai17=" + (meisai17.length == 2 ? URLEncoder.encode(meisai17[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai18")){
				urlParam = urlParam.concat("&meisai18=" + URLEncoder.encode(meisai18[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai18=" + (meisai18.length == 2 ? URLEncoder.encode(meisai18[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai19")){
				urlParam = urlParam.concat("&meisai19=" + URLEncoder.encode(meisai19[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai19=" + (meisai19.length == 2 ? URLEncoder.encode(meisai19[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai20")){
				urlParam = urlParam.concat("&meisai20=" + URLEncoder.encode(meisai20[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai20=" + (meisai20.length == 2 ? URLEncoder.encode(meisai20[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai21")){
				urlParam = urlParam.concat("&meisai21=" + URLEncoder.encode(meisai21[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai21=" + (meisai21.length == 2 ? URLEncoder.encode(meisai21[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai22")){
				urlParam = urlParam.concat("&meisai22=" + URLEncoder.encode(meisai22[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai22=" + (meisai22.length == 2 ? URLEncoder.encode(meisai22[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai23")){
				urlParam = urlParam.concat("&meisai23=" + URLEncoder.encode(meisai23[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai23=" + (meisai23.length == 2 ? URLEncoder.encode(meisai23[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai24")){
				urlParam = urlParam.concat("&meisai24=" + URLEncoder.encode(meisai24[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai24=" + (meisai24.length == 2 ? URLEncoder.encode(meisai24[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai25")){
				urlParam = urlParam.concat("&meisai25=" + URLEncoder.encode(meisai25[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai25=" + (meisai25.length == 2 ? URLEncoder.encode(meisai25[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai26")){
				urlParam = urlParam.concat("&meisai26=" + URLEncoder.encode(meisai26[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai26=" + (meisai26.length == 2 ? URLEncoder.encode(meisai26[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai27")){
				urlParam = urlParam.concat("&meisai27=" + URLEncoder.encode(meisai27[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai27=" + (meisai27.length == 2 ? URLEncoder.encode(meisai27[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai28")){
				urlParam = urlParam.concat("&meisai28=" + URLEncoder.encode(meisai28[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai28=" + (meisai28.length == 2 ? URLEncoder.encode(meisai28[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai29")){
				urlParam = urlParam.concat("&meisai29=" + URLEncoder.encode(meisai29[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai29=" + (meisai29.length == 2 ? URLEncoder.encode(meisai29[1],"UTF-8") : "" ));
			}
			if(columnList.contains("meisai30")){
				urlParam = urlParam.concat("&meisai30=" + URLEncoder.encode(meisai30[0],"UTF-8"));
				urlParam = urlParam.concat("&meisai30=" + (meisai30.length == 2 ? URLEncoder.encode(meisai30[1],"UTF-8") : "" ));
			}


			return urlParam;
		} catch (UnsupportedEncodingException e) {
		    throw new RuntimeException(e);
		}
	}
	
	
	

	/**
	 * ファイルダウンロードイベント
	 * @return 処理結果
	 */
	@Override public String fileDownload(){

		try(EteamConnection connection = EteamConnection.connect()){
			
			initConnection(connection);

			//画面部品の作成
			makeParts();
			
			//設定情報テーブルから最大ダウンロードファイル数を取得
			int maxDownloadNum = Integer.parseInt(setting.fileNumForDownload());
			
			// 1.入力チェック
			formatCheck();
			hissuCheck(5);
			if(! errorList.isEmpty()){
				reKensaku();
				return "error";
			}
			this.soukanCheck();
			if(! errorList.isEmpty()){
				reKensaku();
				return "error";
			}

			// 2.データ存在チェック
			// 3.アクセス可能チェック なし

			// 4.処理 
			// クエリパラメーターの作成
			var queryParameter = this.createQueryParameter();
			
			//検索条件から伝票を検索する。
			list = tsuuchiLogic.loadDenpyouIchiranKensakuKani(
					  false
					, queryParameter
					, makeKaniJouken()
					, 0 //ページ指定なし
					, 0
					, ""); //標準ソート
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				reKensaku();
				return "error";
			}

			//カンマ区切りの検索用伝票IDリストを作成する。
			StringBuffer denpyouIdList = new StringBuffer();
			int i=0;
			for (GMap map : list) {
				if(i != 0){
					denpyouIdList.append(",");
				}
				denpyouIdList.append("'" + map.get("denpyou_id") +"'"); //伝票ID
				i++;
			}

			//添付ファイル数を取得する。
			long fileNum = wfLogic.selectTenpuFileNum(denpyouIdList.toString());

			if(fileNum < 1){ //添付ファイルなし
				errorList.add("添付ファイルがありません。");
				reKensaku();
				return "error";
			}
			
			if(fileNum > maxDownloadNum){ //最大ダウンロードファイル数より大きい。
				errorList.add("添付ファイル数が最大ダウンロード数（" + maxDownloadNum + "）を超えています。");
				reKensaku();
				return "error";
			}
			
			//重複した添付ファイル名リストを取得する。
			ArrayList<String> duplicateFileList = wfLogic.selectDuplicateTenpuFileName(denpyouIdList.toString());

			//ZIPの作成
			try {
				
				//ZIP出力ストリームの設定
				ByteArrayOutputStream bArray1 = new ByteArrayOutputStream(); 
				ZipOutputStream objZos = new ZipOutputStream(bArray1); 
				objZos.setEncoding(Encoding.MS932);
				
				//伝票ID毎に添付ファイルを処理する。
				for (GMap map : list) {

					String wkDenpyouId = (String)map.get("denpyou_id"); //伝票ID
					
					//指定した伝票IDの添付ファイルを取得する。
					List<GMap> tenpuList = wfLogic.selectTenpuFileBD(wkDenpyouId);
					for (GMap tenpu : tenpuList) {
						
						//ファイル名を作成する。（重複リストにある場合は名前を変える）
						String tenpuFileName = (String)tenpu.get("file_name");
						
						if(duplicateFileList.indexOf(tenpuFileName) != -1){
							//ファイル拡張子を取得
							int point = tenpuFileName.lastIndexOf(".");
						    if (point != -1) { //拡張子あり
						        tenpuFileName = tenpuFileName.substring(0, point) + "_" + wkDenpyouId + "-" + tenpu.get("edano") + "." + tenpuFileName.substring(point + 1);
						    }else{//拡張子なし
						        tenpuFileName = tenpuFileName + "_" + wkDenpyouId + "-" + tenpu.get("edano");
						    }
						}
						
						//ZIPエントリ
						ZipEntry objZe=new ZipEntry(tenpuFileName);
						objZe.setMethod(ZipOutputStream.DEFLATED);

						//ZIP出力ストリームに追加する。
						objZos.putNextEntry(objZe);
						objZos.write((byte[]) tenpu.get("binary_data"));
						
						//ZIPエントリクローズ
						objZos.closeEntry();
					}
				}

				//ZIP出力ストリームクローズ
				objZos.close();

				contentType = ContentType.ZIP;
				int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
				contentDisposition = EteamCommon.contentDisposition(browserCode, true, ZIP_FILENAME);
				
				ByteArrayInputStream bArray2 = new ByteArrayInputStream(bArray1.toByteArray()); 
				this.inputStream = bArray2;

			} catch (IOException e) {
			    throw new RuntimeException(e);
			}

			//5.戻り値を返す
			return "success";

		}
	}

	/**
	 * 更新処理エラー発生時、再検索用
	 */
	@Override protected void reKensaku() {
		List<String> tmpErrorList = errorList;
		errorList = new ArrayList<>();
		kensaku();
		errorList = tmpErrorList;
	}
	
	/**
	 * 簡易届検索条件を作る
	 * @return 簡易届検索条件
	 */
	KaniJouken makeKaniJouken(){
		KaniJouken kj = super.tsuuchiLogic.new KaniJouken();
		
		kj.columnList = this.columnList;
		kj.typeList = this.typeList;
		kj.formatList = this.formatList;
		
		kj.shinsei01=shinsei01;
		kj.shinsei02=shinsei02;
		kj.shinsei03=shinsei03;
		kj.shinsei04=shinsei04;
		kj.shinsei05=shinsei05;
		kj.shinsei06=shinsei06;
		kj.shinsei07=shinsei07;
		kj.shinsei08=shinsei08;
		kj.shinsei09=shinsei09;
		kj.shinsei10=shinsei10;
		kj.shinsei11=shinsei11;
		kj.shinsei12=shinsei12;
		kj.shinsei13=shinsei13;
		kj.shinsei14=shinsei14;
		kj.shinsei15=shinsei15;
		kj.shinsei16=shinsei16;
		kj.shinsei17=shinsei17;
		kj.shinsei18=shinsei18;
		kj.shinsei19=shinsei19;
		kj.shinsei20=shinsei20;
		kj.shinsei21=shinsei21;
		kj.shinsei22=shinsei22;
		kj.shinsei23=shinsei23;
		kj.shinsei24=shinsei24;
		kj.shinsei25=shinsei25;
		kj.shinsei26=shinsei26;
		kj.shinsei27=shinsei27;
		kj.shinsei28=shinsei28;
		kj.shinsei29=shinsei29;
		kj.shinsei30=shinsei30;

		kj.meisai01=meisai01;
		kj.meisai02=meisai02;
		kj.meisai03=meisai03;
		kj.meisai04=meisai04;
		kj.meisai05=meisai05;
		kj.meisai06=meisai06;
		kj.meisai07=meisai07;
		kj.meisai08=meisai08;
		kj.meisai09=meisai09;
		kj.meisai10=meisai10;
		kj.meisai11=meisai11;
		kj.meisai12=meisai12;
		kj.meisai13=meisai13;
		kj.meisai14=meisai14;
		kj.meisai15=meisai15;
		kj.meisai16=meisai16;
		kj.meisai17=meisai17;
		kj.meisai18=meisai18;
		kj.meisai19=meisai19;
		kj.meisai20=meisai20;
		kj.meisai21=meisai21;
		kj.meisai22=meisai22;
		kj.meisai23=meisai23;
		kj.meisai24=meisai24;
		kj.meisai25=meisai25;
		kj.meisai26=meisai26;
		kj.meisai27=meisai27;
		kj.meisai28=meisai28;
		kj.meisai29=meisai29;
		kj.meisai30=meisai30;
		
		kj.routeKingakuItem=kaniKoumokuItemName[0];
		kj.kenmeiItem=kaniKoumokuItemName[1];
		kj.naiyouShinseiItem=kaniKoumokuItemName[2];
		
		return kj;
	}
	/**
	 * データリストのフォーマット(画面、簡易届用)
	 */
	protected void formatList4JSPKaniTodoke() {
		// レコードの初期化
		for (GMap map : list) {
			for(int i = 0 ; i < columnList.size() ; i++){
				String clKey = columnList.get(i) + "Class";
				String clVal = "";
				String fmt = formatList.get(i);
				if(BUHIN_FORMAT.NUMBER.equals(fmt) || BUHIN_FORMAT.DATE.equals(fmt) || BUHIN_FORMAT.MONEY.equals(fmt) ){
					clVal = "text-r";
				}
				map.put(clKey,clVal);
			}
		}
	}
	
	/**
	 * 伝票検索では必須項目でもブランクでも検索がしたいため、<br>
	 * プルダウンにブランク指定のレコードがない場合、ブランク用のMapを新規作成し、強制的にListに追加。<br>
	 * 
	 * @param layoutList 簡易届項目フォーマットリスト
	 * @param optionList 簡易届選択肢リスト
	 * @return 追加する簡易届選択肢リスト
	 */
	protected List<GMap> setEmptyMapOfPullDown (List<GMap> layoutList, List<GMap> optionList) {
		List<GMap> relLayoutList = new ArrayList<GMap>(); 
		for (Map<String, Object> layout : layoutList) {
			for (Map<String, Object> option : optionList) {
				if (option.get("item_name").equals(layout.get("item_name")) 
					&& layout.get("buhin_type").equals("pulldown")
					// 表示順＝１のtextが空でない→必須であり、プルダウンに空レコードがない
					&& option.get("hyouji_jun").equals(1) 
					&& !option.get("text").equals("")
					) {
					GMap map = new GMap();
					map.put("area_kbn", option.get("area_kbn"));
					map.put("item_name", option.get("item_name"));
					map.put("hyouji_jun", 0);
					map.put("text", "");
					map.put("value", "");
					relLayoutList.add(map);
				}
			}
		}
		return relLayoutList;
	}
	/**
	 * ルート判定金額・件名・内容のカラム名を取得
	 * @param kbn 伝票区分
	 * @return 0:ルート判定金額 1:件名 2:内容
	 */
	protected String[] getKaniKoumokuItemName(String kbn) {
		String[] koumokuItemName = {"","",""};
		if (kbn.equals(""))
		{
			return null;
		}
		String version = String.valueOf(kaniTodokeLogic.findMaxVersion(kbn));
		
		String[] kingakuId = null;
		String routeHanteiKingakuCd = denpyouKanriLogic.getRouteHanteiKingaku(kbn);
		
		switch (routeHanteiKingakuCd){
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.RINGI_KINGAKU:
			kingakuId = new String[1];
			kingakuId[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.RINGI_KINGAKU;
			break;
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.SHISHUTSU_GOUKEI:
			kingakuId = new String[1];
			kingakuId[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_GOUKEI;
			break;
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.SHISHUTSU_SHUUNYUU:
			kingakuId = new String[2];
			kingakuId[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_GOUKEI;
			kingakuId[1] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_GOUKEI;
			break;
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.SHUUNYUU_GOUKEI:
			kingakuId = new String[1];
			kingakuId[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_GOUKEI;
			break;
		default:
			break;
		}
		String itemname = "";
		String kenmeiname = "";
		String naiyouname = "";
		// ルート判定金額が「分岐なし」以外の場合
		if (null != kingakuId){
			for (int i = 0; i < kingakuId.length; i++){
				itemname = kaniTodokeLogicSelect.getYosanShikkouItemName(kbn,version,kingakuId[i]);
				if (!itemname.equals(""))
				{
					break;
				}
			}
		}
		kenmeiname = kaniTodokeLogicSelect.getYosanShikkouItemName(kbn,version,"kenmei");
		naiyouname = kaniTodokeLogicSelect.getYosanShikkouItemName(kbn,version,"naiyou_shinsei");
		koumokuItemName[0] = itemname;
		koumokuItemName[1] = kenmeiname;
		koumokuItemName[2] = naiyouname;
		
		return koumokuItemName;
	}
	
	@Override
	protected DenpyouKensakuQueryParameter createQueryParameter()
	{
		// 簡易届ではいらない値をクリアする
		// JSPのスクリプト部分でで一部消しているらしいが、現時点で既に中途半端になっており、
		// それらを管理するのが面倒なのでシステマティックにjavaで除去
		this.tekiyou = "";
		this.kingakuFrom = "";
		this.kingakuTo = "";
		this.meisaiKingakuFrom = "";
		this.meisaiKingakuTo = "";
		this.queryZeiritsu = "";
		this.keigenZeiritsuKbn = "";
		this.houjinCardRiyou = "";
		this.kaishaTehai = "";
		this.shiharaiBiFrom = "";
		this.shiharaiBiTo = "";
		this.shiharaiKiboubiFrom = "";
		this.shiharaiKiboubiTo = "";
		this.keijoubiFrom = "";
		this.keijoubiTo = "";
		this.shiwakeKeijoubiFrom = "";
		this.shiwakeKeijoubiTo = "";
		this.karikataBumonCdFrom = "";
		this.karikataBumonCdTo = "";
		this.kashikataBumonCdFrom = "";
		this.kashikataBumonCdTo = "";
		this.karikataKamokuCdFrom = "";
		this.karikataKamokuCdTo = "";
		this.kashikataKamokuCdFrom = "";
		this.kashikataKamokuCdTo = "";
		this.karikataKamokuEdanoCdFrom = "";
		this.karikataKamokuEdanoCdTo = "";
		this.kashikataKamokuEdanoCdFrom = "";
		this.kashikataKamokuEdanoCdTo = "";
		this.karikataTorihikisakiCdFrom = "";
		this.karikataTorihikisakiCdTo = "";
		this.kashikataTorihikisakiCdFrom = "";
		this.kashikataTorihikisakiCdTo = "";
		this.bumonSentakuFlag = "";
		this.ryoushuushoSeikyuushoTou = "";
		this.miseisanKaribarai = "";
		this.miseisanUkagai = "";
		this.shiwakeStatus = "";
		this.fbStatus = "";
		this.bumonCdFrom = "";
		this.bumonCdTo = "";
		this.kanitodokeKenmei = "";
		this.kanitodokeNaiyou = "";
		
		return super.createQueryParameter();
	}
}
