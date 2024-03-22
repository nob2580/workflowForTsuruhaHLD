package eteam.common;

import eteam.common.EteamNaibuCodeSetting.SHIYOU_FLG;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import lombok.Getter;
import lombok.ToString;

/**
 * HF/UFの項目名・表示有無・使用フラグ取得用クラス。
 *
 */
@Getter @ToString
public class HfUfSeigyo {
	/** HF1表示 */
	String hf1ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.HF1_SHIYOU_FLG);
	/** HF1項目名 */
	String hf1Name = KaishaInfo.getKaishaInfo(ColumnName.HF1_NAME);
	/** HF2表示 */
	String hf2ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.HF2_SHIYOU_FLG);
	/** HF2項目名 */
	String hf2Name = KaishaInfo.getKaishaInfo(ColumnName.HF2_NAME);
	/** HF3表示 */
	String hf3ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.HF3_SHIYOU_FLG);
	/** HF3項目名 */
	String hf3Name = KaishaInfo.getKaishaInfo(ColumnName.HF3_NAME);
	/** HF4表示 */
	String hf4ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.HF4_SHIYOU_FLG);
	/** HF4項目名 */
	String hf4Name = KaishaInfo.getKaishaInfo(ColumnName.HF4_NAME);
	/** HF5表示 */
	String hf5ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.HF5_SHIYOU_FLG);
	/** HF5項目名 */
	String hf5Name = KaishaInfo.getKaishaInfo(ColumnName.HF5_NAME);
	/** HF6表示 */
	String hf6ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.HF6_SHIYOU_FLG);
	/** HF6項目名 */
	String hf6Name = KaishaInfo.getKaishaInfo(ColumnName.HF6_NAME);
	/** HF7表示 */
	String hf7ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.HF7_SHIYOU_FLG);
	/** HF7項目名 */
	String hf7Name = KaishaInfo.getKaishaInfo(ColumnName.HF7_NAME);
	/** HF8表示 */
	String hf8ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.HF8_SHIYOU_FLG);
	/** HF8項目名 */
	String hf8Name = KaishaInfo.getKaishaInfo(ColumnName.HF8_NAME);
	/** HF9表示 */
	String hf9ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.HF9_SHIYOU_FLG);
	/** HF9項目名 */
	String hf9Name = KaishaInfo.getKaishaInfo(ColumnName.HF9_NAME);
	/** HF10表示 */
	String hf10ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.HF10_SHIYOU_FLG);
	/** HF10項目名 */
	String hf10Name = KaishaInfo.getKaishaInfo(ColumnName.HF10_NAME);
	/** UF1表示 */
	String uf1ShiyouFlg = ("UF1".equals(EteamSettingInfo.getSettingInfo(Key.UF1_DENPYOU_ID_HANEI)) && Open21Env.getVersion() == Version.DE3) ? SHIYOU_FLG.SHIYOU_SHINAI :  KaishaInfo.getKaishaInfo(ColumnName.UF1_SHIYOU_FLG);
	/** UF1項目名 */
	String uf1Name = KaishaInfo.getKaishaInfo(ColumnName.UF1_NAME);
	/** UF2表示 */
	String uf2ShiyouFlg = ("UF2".equals(EteamSettingInfo.getSettingInfo(Key.UF1_DENPYOU_ID_HANEI)) && Open21Env.getVersion() == Version.DE3) ? SHIYOU_FLG.SHIYOU_SHINAI :  KaishaInfo.getKaishaInfo(ColumnName.UF2_SHIYOU_FLG);
	/** UF2項目名 */
	String uf2Name = KaishaInfo.getKaishaInfo(ColumnName.UF2_NAME);
	/** UF3表示 */
	String uf3ShiyouFlg = ("UF3".equals(EteamSettingInfo.getSettingInfo(Key.UF1_DENPYOU_ID_HANEI)) && Open21Env.getVersion() == Version.DE3) ? SHIYOU_FLG.SHIYOU_SHINAI :  KaishaInfo.getKaishaInfo(ColumnName.UF3_SHIYOU_FLG);
	/** UF3項目名 */
	String uf3Name = KaishaInfo.getKaishaInfo(ColumnName.UF3_NAME);
	/** UF4表示 */
	String uf4ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF4_SHIYOU_FLG);
	/** UF4項目名 */
	String uf4Name = KaishaInfo.getKaishaInfo(ColumnName.UF4_NAME);
	/** UF5表示 */
	String uf5ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF5_SHIYOU_FLG);
	/** UF5項目名 */
	String uf5Name = KaishaInfo.getKaishaInfo(ColumnName.UF5_NAME);
	/** UF6表示 */
	String uf6ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF6_SHIYOU_FLG);
	/** UF6項目名 */
	String uf6Name = KaishaInfo.getKaishaInfo(ColumnName.UF6_NAME);
	/** UF7表示 */
	String uf7ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF7_SHIYOU_FLG);
	/** UF7項目名 */
	String uf7Name = KaishaInfo.getKaishaInfo(ColumnName.UF7_NAME);
	/** UF8表示 */
	String uf8ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF8_SHIYOU_FLG);
	/** UF8項目名 */
	String uf8Name = KaishaInfo.getKaishaInfo(ColumnName.UF8_NAME);
	/** UF9表示 */
	String uf9ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF9_SHIYOU_FLG);
	/** UF9項目名 */
	String uf9Name = KaishaInfo.getKaishaInfo(ColumnName.UF9_NAME);
	/** UF10表示 */
	String uf10ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF10_SHIYOU_FLG);
	/** UF10項目名 */
	String uf10Name = KaishaInfo.getKaishaInfo(ColumnName.UF10_NAME);

	/** UF(固定値)1表示 */
	String ufKotei1ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI1_SHIYOU_FLG);
	/** UF(固定値)1項目名 */
	String ufKotei1Name = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI1_NAME);
	/** UF(固定値)2表示 */
	String ufKotei2ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI2_SHIYOU_FLG);
	/** UF(固定値)2項目名 */
	String ufKotei2Name = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI2_NAME);
	/** UF(固定値)3表示 */
	String ufKotei3ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI3_SHIYOU_FLG);
	/** UF(固定値)3項目名 */
	String ufKotei3Name = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI3_NAME);
	/** UF(固定値)4表示 */
	String ufKotei4ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI4_SHIYOU_FLG);
	/** UF(固定値)4項目名 */
	String ufKotei4Name = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI4_NAME);
	/** UF(固定値)5表示 */
	String ufKotei5ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI5_SHIYOU_FLG);
	/** UF(固定値)5項目名 */
	String ufKotei5Name = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI5_NAME);
	/** UF(固定値)6表示 */
	String ufKotei6ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI6_SHIYOU_FLG);
	/** UF(固定値)6項目名 */
	String ufKotei6Name = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI6_NAME);
	/** UF(固定値)7表示 */
	String ufKotei7ShiyouFlg =KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI7_SHIYOU_FLG);
	/** UF(固定値)7項目名 */
	String ufKotei7Name = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI7_NAME);
	/** UF(固定値)8表示 */
	String ufKotei8ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI8_SHIYOU_FLG);
	/** UF(固定値)8項目名 */
	String ufKotei8Name = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI8_NAME);
	/** UF(固定値)9表示 */
	String ufKotei9ShiyouFlg =KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI9_SHIYOU_FLG);
	/** UF(固定値)9項目名 */
	String ufKotei9Name = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI9_NAME);
	/** UF(固定値)10表示 */
	String ufKotei10ShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI10_SHIYOU_FLG);
	/** UF(固定値)10項目名 */
	String ufKotei10Name = KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI10_NAME);
}
