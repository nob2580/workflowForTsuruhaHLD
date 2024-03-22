package eteam.gyoumu.tsuuchi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.RegAccess;
import lombok.Getter;
import lombok.Setter;

/**
 * 表示項目を表すBeanです。
 */
@Getter @Setter
public class DenpyouDisplayItem implements Serializable {

	/** 項番 */
	protected int index;
	/** 名称 */
	protected String name;
	/** 表示するかどうか */
	protected boolean isDisplay;
	/** 表示文言 */
	protected String label;

	/**
	 * コンストラクタです。
	 * @param index 項番
	 * @param name 項目名
	 * @param isDisplay 表示フラグ
	 * @param label 表示文言
	 */
	public DenpyouDisplayItem(int index, String name, boolean isDisplay, String label) {
		this.index = index;
		this.name = name;
		this.isDisplay = isDisplay;
		this.label = label;
	}

	/**
	 * 伝票一覧結果表示項目のデフォルト値リストを返します。
	 * @return 表示項目のリスト
	 */
	public static List<DenpyouDisplayItem> getDefaultDisplayItemList() {
		List<DenpyouDisplayItem> itemTypeList = new ArrayList<>();
		int ii = 0;
		itemTypeList.add(new DenpyouDisplayItem(ii++, "joutai"						, true ,"状態"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "id"							, true ,"伝票ID"));
		if(RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption())){
			itemTypeList.add(new DenpyouDisplayItem(ii++, "jisshi_kian_bangou"			, true ,"実施起案番号"));
			itemTypeList.add(new DenpyouDisplayItem(ii++, "shishutsu_kian_bangou"		, true ,"支出起案番号"));
			itemTypeList.add(new DenpyouDisplayItem(ii++, "yosan_shikkou_taishou"		, true ,"予算執行伝票種別"));
			itemTypeList.add(new DenpyouDisplayItem(ii++, "yosan_check_nengetsu"		, false ,"予算執行対象月"));

		}
		itemTypeList.add(new DenpyouDisplayItem(ii++, "serialNo"					, false ,"伝票番号"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "shubetsu"					, true ,"伝票種別"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "kanitodoke_kenmei"			, false ,EteamSettingInfo.getSettingInfo(Key.USER_TEIGI_TODOKE_KENSAKU_KENMEI)));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "kanitodoke_naiyou"			, false ,EteamSettingInfo.getSettingInfo(Key.USER_TEIGI_TODOKE_KENSAKU_NAIYOU)));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "user_name"					, false ,"使用者"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "seisankikan"					, false ,"精算期間"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "houmonsaki"					, false ,"出張先・訪問先"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "mokuteki"					, false ,"目的"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "kingaku"						, true ,"金額"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "sashihiki_shikyuu_kingaku"	, false ,"差引支給金額"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "shiharaiKiboubi"				, true ,"支払希望日\r\n支払期限"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "shouninbi"					, false ,"最終承認日"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "shiharaiBi"					, true ,"支払日"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "shiharaihouhou"				, false ,"支払方法"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "shiwakeKeijoubi"				, false ,"計上日"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "keijoubi"					, false ,"使用日"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "torihikisaki"				, true ,"取引先"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "seisan_yoteibi"				, false ,"精算予定日"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "karibarai_denpyou_id"		, false ,"仮払伝票ID"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "kihyou"						, true ,"起票"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "shoyuu"						, false ,"所有"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "hosoku", false, "補足"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "zeiritsu", false, "消費税率"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "jigyousha_kbn", false, "事業者区分"));
		itemTypeList.add(new DenpyouDisplayItem(ii++, "zeinuki_kingaku", false, "税抜金額"));
		return itemTypeList;
	}

	/**
	 * 変更可能な項目かどうかを返します。
	 * @return 結果
	 */
	public boolean isEditable() {
		if (name.equals("id"))
		{
			return false;
		}
		return true;
	}
}
