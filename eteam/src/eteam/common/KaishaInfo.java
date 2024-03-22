package eteam.common;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.util.ThreadLocalUtil;

/**
 * 設定情報テーブルの情報保持用
 * 都度SELECTするより多少パフォーマンスが上がるのと、connectしないと触れないのが無駄なので。
 */
public class KaishaInfo {

	/** ログ */
	protected static EteamLogger log = EteamLogger.getLogger(KaishaInfo.class);

	/**
	 * 設定情報テーブルのカラム名
	 */
	public class ColumnName {
		/** HF1使用フラグ */
		public static final String HF1_SHIYOU_FLG = "hf1_shiyou_flg";
		/** HF1必須フラグ */
		public static final String HF1_HISSU_FLG = "hf1_hissu_flg";
		/** HF1名 */
		public static final String HF1_NAME = "hf1_name";
		/** HF2使用フラグ */
		public static final String HF2_SHIYOU_FLG = "hf2_shiyou_flg";
		/** HF2必須フラグ */
		public static final String HF2_HISSU_FLG = "hf2_hissu_flg";
		/** HF2名 */
		public static final String HF2_NAME = "hf2_name";
		/** HF3使用フラグ */
		public static final String HF3_SHIYOU_FLG = "hf3_shiyou_flg";
		/** HF3必須フラグ */
		public static final String HF3_HISSU_FLG = "hf3_hissu_flg";
		/** HF3名 */
		public static final String HF3_NAME = "hf3_name";
		/** HF4使用フラグ */
		public static final String HF4_SHIYOU_FLG = "hf4_shiyou_flg";
		/** HF4必須フラグ */
		public static final String HF4_HISSU_FLG = "hf4_hissu_flg";
		/** HF4名 */
		public static final String HF4_NAME = "hf4_name";
		/** HF5使用フラグ */
		public static final String HF5_SHIYOU_FLG = "hf5_shiyou_flg";
		/** HF5必須フラグ */
		public static final String HF5_HISSU_FLG = "hf5_hissu_flg";
		/** HF5名 */
		public static final String HF5_NAME = "hf5_name";
		/** HF6使用フラグ */
		public static final String HF6_SHIYOU_FLG = "hf6_shiyou_flg";
		/** HF6必須フラグ */
		public static final String HF6_HISSU_FLG = "hf6_hissu_flg";
		/** HF6名 */
		public static final String HF6_NAME = "hf6_name";
		/** HF7使用フラグ */
		public static final String HF7_SHIYOU_FLG = "hf7_shiyou_flg";
		/** HF7必須フラグ */
		public static final String HF7_HISSU_FLG = "hf7_hissu_flg";
		/** HF7名 */
		public static final String HF7_NAME = "hf7_name";
		/** HF8使用フラグ */
		public static final String HF8_SHIYOU_FLG = "hf8_shiyou_flg";
		/** HF8必須フラグ */
		public static final String HF8_HISSU_FLG = "hf8_hissu_flg";
		/** HF8名 */
		public static final String HF8_NAME = "hf8_name";
		/** HF9使用フラグ */
		public static final String HF9_SHIYOU_FLG = "hf9_shiyou_flg";
		/** HF9必須フラグ */
		public static final String HF9_HISSU_FLG = "hf9_hissu_flg";
		/** HF9名 */
		public static final String HF9_NAME = "hf9_name";
		/** HF10使用フラグ */
		public static final String HF10_SHIYOU_FLG = "hf10_shiyou_flg";
		/** HF10必須フラグ */
		public static final String HF10_HISSU_FLG = "hf10_hissu_flg";
		/** HF10名 */
		public static final String HF10_NAME = "hf10_name";
		/** UF1使用フラグ */
		public static final String UF1_SHIYOU_FLG = "uf1_shiyou_flg";
		/** UF1名 */
		public static final String UF1_NAME = "uf1_name";
		/** UF2使用フラグ */
		public static final String UF2_SHIYOU_FLG = "uf2_shiyou_flg";
		/** UF2名 */
		public static final String UF2_NAME = "uf2_name";
		/** UF3使用フラグ */
		public static final String UF3_SHIYOU_FLG = "uf3_shiyou_flg";
		/** UF3名 */
		public static final String UF3_NAME = "uf3_name";
		/** UF4使用フラグ */
		public static final String UF4_SHIYOU_FLG = "uf4_shiyou_flg";
		/** UF4名 */
		public static final String UF4_NAME = "uf4_name";
		/** UF5使用フラグ */
		public static final String UF5_SHIYOU_FLG = "uf5_shiyou_flg";
		/** UF5名 */
		public static final String UF5_NAME = "uf5_name";
		/** UF6使用フラグ */
		public static final String UF6_SHIYOU_FLG = "uf6_shiyou_flg";
		/** UF6名 */
		public static final String UF6_NAME = "uf6_name";
		/** UF7使用フラグ */
		public static final String UF7_SHIYOU_FLG = "uf7_shiyou_flg";
		/** UF7名 */
		public static final String UF7_NAME = "uf7_name";
		/** UF8使用フラグ */
		public static final String UF8_SHIYOU_FLG = "uf8_shiyou_flg";
		/** UF8名 */
		public static final String UF8_NAME = "uf8_name";
		/** UF9使用フラグ */
		public static final String UF9_SHIYOU_FLG = "uf9_shiyou_flg";
		/** UF9名 */
		public static final String UF9_NAME = "uf9_name";
		/** UF10使用フラグ */
		public static final String UF10_SHIYOU_FLG = "uf10_shiyou_flg";
		/** UF10名 */
		public static final String UF10_NAME = "uf10_name";
		/** プロジェクトコード使用フラグ */
		public static final String PJ_SHIYOU_FLG = "pjcd_shiyou_flg";
		/** セグメントコード使用フラグ */
		public static final String SEGMENT_SHIYOU_FLG = "sgcd_shiyou_flg";
		/** 債務使用フラグ */
		public static final String SAIMU_SHIYOU_FLG = "saimu_shiyou_flg";
		/** UF(固定値)1使用フラグ */
		public static final String UF_KOTEI1_SHIYOU_FLG = "uf_kotei1_shiyou_flg";
		/** UF(固定値)1名 */
		public static final String UF_KOTEI1_NAME = "uf_kotei1_name";
		/** UF(固定値)2使用フラグ */
		public static final String UF_KOTEI2_SHIYOU_FLG = "uf_kotei2_shiyou_flg";
		/** UF(固定値)2名 */
		public static final String UF_KOTEI2_NAME = "uf_kotei2_name";
		/** UF(固定値)3使用フラグ */
		public static final String UF_KOTEI3_SHIYOU_FLG = "uf_kotei3_shiyou_flg";
		/** UF(固定値)3名 */
		public static final String UF_KOTEI3_NAME = "uf_kotei3_name";
		/** UF(固定値)4使用フラグ */
		public static final String UF_KOTEI4_SHIYOU_FLG = "uf_kotei4_shiyou_flg";
		/** UF(固定値)4名 */
		public static final String UF_KOTEI4_NAME = "uf_kotei4_name";
		/** UF(固定値)5使用フラグ */
		public static final String UF_KOTEI5_SHIYOU_FLG = "uf_kotei5_shiyou_flg";
		/** UF(固定値)5名 */
		public static final String UF_KOTEI5_NAME = "uf_kotei5_name";
		/** UF(固定値)6使用フラグ */
		public static final String UF_KOTEI6_SHIYOU_FLG = "uf_kotei6_shiyou_flg";
		/** UF(固定値)6名 */
		public static final String UF_KOTEI6_NAME = "uf_kotei6_name";
		/** UF(固定値)7使用フラグ */
		public static final String UF_KOTEI7_SHIYOU_FLG = "uf_kotei7_shiyou_flg";
		/** UF(固定値)7名 */
		public static final String UF_KOTEI7_NAME = "uf_kotei7_name";
		/** UF(固定値)8使用フラグ */
		public static final String UF_KOTEI8_SHIYOU_FLG = "uf_kotei8_shiyou_flg";
		/** UF(固定値)8名 */
		public static final String UF_KOTEI8_NAME = "uf_kotei8_name";
		/** UF(固定値)9使用フラグ */
		public static final String UF_KOTEI9_SHIYOU_FLG = "uf_kotei9_shiyou_flg";
		/** UF(固定値)9名 */
		public static final String UF_KOTEI9_NAME = "uf_kotei9_name";
		/** UF(固定値)10使用フラグ */
		public static final String UF_KOTEI10_SHIYOU_FLG = "uf_kotei10_shiyou_flg";
		/** UF(固定値)10名 */
		public static final String UF_KOTEI10_NAME = "uf_kotei10_name";
		/** 科目コードタイプ */
		public static final String KAMOKU_CD_TYPE = "kamoku_cd_type";
		/** 枝番コードタイプ */
		public static final String KAMOKU_EDABAN_CD_TYPE = "kamoku_edaban_cd_type";
		/** 部門コードタイプ */
		public static final String FUTAN_BUMON_CD_TYPE = "futan_bumon_cd_type";
		/** 取引先コードタイプ */
		public static final String TORIHIKISAKI_CD_TYPE = "torihikisaki_cd_type";
		/** セグメントコードタイプ */
		public static final String SEGMENT_CD_TYPE = "segment_cd_type";
		/** 法人番号 */
		public static final String HOUJIN_BANGOU = "houjin_bangou";
		/** 軽減有無フラグ */
		public static final String KEIGEN_UMU_FLG = "keigen_umu_flg";
	}

	/**
	 * 会社情報をスキーマ単位でメモリ上に読込
	 * @param kaishaInfoMap 会社情報
	 */
	protected static void reload(GMap kaishaInfoMap) {
		try(EteamConnection connection = EteamConnection.connect()) {
			SystemKanriCategoryLogic lg = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			kaishaInfoMap.putAll(lg.findKaishaInfo());
		}
	}
	
	/**
	 * カラム値取得。
	 * @param columnName カラム名
	 * @return カラム値
	 */
	public static String getKaishaInfo(String columnName) {
		GMap kaishaInfoMap = ThreadLocalUtil.get("kaishaInfoMap");
		if(kaishaInfoMap.isEmpty()) {
			//スレッドローカルの初回のみ読み込み実施
			reload(kaishaInfoMap);
		}
		
		return kaishaInfoMap.getString(columnName);
	}
}
