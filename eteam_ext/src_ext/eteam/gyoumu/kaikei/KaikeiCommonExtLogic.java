package eteam.gyoumu.kaikei;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import eteam.base.GMap;
import eteam.common.Env;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIYOU_FLG;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;

public class KaikeiCommonExtLogic extends KaikeiCommonLogic {
	
	/**
	 * 仕訳チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * 画面上存在する項目のみ渡すこと。
	 * @param denpyouKbn 伝票区分
	 * @param shiwakePatternSettingKbn 仕訳パターン設定区分
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param daihyouBumonCd 代表負担部門コード
	 * @param errorList エラーリスト
	 */
	@Override
	public void checkShiwake(
			String denpyouKbn,
			String shiwakePatternSettingKbn,
			ShiwakeCheckData shiwakeCheckData,
			String daihyouBumonCd,
			List<String> errorList) {
		if(Env.checkShiwakeIsDummy())return;

		//拡張 前処理
		String kamokuCd = shiwakeCheckData.getKamokuCd();
		beforeCheckShiwakeExt(shiwakeCheckData);

		//科目枝番連携フラグの再設定(各種申請画面から呼び出された時向け)
		if( shiwakeCheckData.zaimuEdabanRenkei == null && !isEmpty(denpyouKbn) && shiwakeCheckData.shiwakeEdaNo != null ) {
			String shiwakeEdanoCd = shiwakeCheckData.shiwakeEdaNo;
			GMap shiwakePattern = kaikeiLg.findShiwakePattern(denpyouKbn, Integer.parseInt(shiwakeEdanoCd));
			if(shiwakePattern != null && "1".equals(shiwakePattern.get("edaban_renkei_flg"))) {
				shiwakeCheckData.zaimuEdabanRenkei = "1";
			}
		}

		//勘定科目入力チェック
		//▼カスタマイズ
		this.checkKanjouKamoku(shiwakeCheckData, errorList, denpyouKbn);

		//負担部門入力チェック
		this.checkFutanBumon(shiwakeCheckData, errorList);

		//取引先入力チェック
		this.checkTorihikisaki(shiwakeCheckData, errorList);

		//プロジェクトチェック
		this.checkPJ(shiwakeCheckData, errorList);

		//セグメントチェック
		this.checkSegment(shiwakeCheckData, errorList);

		//課税区分入力チェック
		this.checkKazeiKbn(shiwakeCheckData, denpyouKbn, errorList);

		//HF1-10入力チェック
		this.checkHF("1", shiwakeCheckData.hf1Cd, shiwakeCheckData.hf1Nm, ColumnName.HF1_NAME, ColumnName.HF1_HISSU_FLG, ColumnName.HF1_SHIYOU_FLG, errorList);
		this.checkHF("2", shiwakeCheckData.hf2Cd, shiwakeCheckData.hf2Nm, ColumnName.HF2_NAME, ColumnName.HF2_HISSU_FLG, ColumnName.HF2_SHIYOU_FLG, errorList);
		this.checkHF("3", shiwakeCheckData.hf3Cd, shiwakeCheckData.hf3Nm, ColumnName.HF3_NAME, ColumnName.HF3_HISSU_FLG, ColumnName.HF3_SHIYOU_FLG, errorList);
		this.checkHF("4", shiwakeCheckData.hf4Cd, shiwakeCheckData.hf4Nm, ColumnName.HF4_NAME, ColumnName.HF4_HISSU_FLG, ColumnName.HF4_SHIYOU_FLG, errorList);
		this.checkHF("5", shiwakeCheckData.hf5Cd, shiwakeCheckData.hf5Nm, ColumnName.HF5_NAME, ColumnName.HF5_HISSU_FLG, ColumnName.HF5_SHIYOU_FLG, errorList);
		this.checkHF("6", shiwakeCheckData.hf6Cd, shiwakeCheckData.hf6Nm, ColumnName.HF6_NAME, ColumnName.HF6_HISSU_FLG, ColumnName.HF6_SHIYOU_FLG, errorList);
		this.checkHF("7", shiwakeCheckData.hf7Cd, shiwakeCheckData.hf7Nm, ColumnName.HF7_NAME, ColumnName.HF7_HISSU_FLG, ColumnName.HF7_SHIYOU_FLG, errorList);
		this.checkHF("8", shiwakeCheckData.hf8Cd, shiwakeCheckData.hf8Nm, ColumnName.HF8_NAME, ColumnName.HF8_HISSU_FLG, ColumnName.HF8_SHIYOU_FLG, errorList);
		this.checkHF("9", shiwakeCheckData.hf9Cd, shiwakeCheckData.hf9Nm, ColumnName.HF9_NAME, ColumnName.HF9_HISSU_FLG, ColumnName.HF9_SHIYOU_FLG, errorList);
		this.checkHF("10", shiwakeCheckData.hf10Cd, shiwakeCheckData.hf10Nm, ColumnName.HF10_NAME, ColumnName.HF10_HISSU_FLG, ColumnName.HF10_SHIYOU_FLG, errorList);

		//UF1-10入力チェック
		this.checkUF("1", shiwakeCheckData.uf1Cd, shiwakeCheckData.uf1Nm, ColumnName.UF1_SHIYOU_FLG, errorList);
		this.checkUF("2", shiwakeCheckData.uf2Cd, shiwakeCheckData.uf2Nm, ColumnName.UF2_SHIYOU_FLG, errorList);
		this.checkUF("3", shiwakeCheckData.uf3Cd, shiwakeCheckData.uf3Nm, ColumnName.UF3_SHIYOU_FLG, errorList);
		this.checkUF("4", shiwakeCheckData.uf4Cd, shiwakeCheckData.uf4Nm, ColumnName.UF4_SHIYOU_FLG, errorList);
		this.checkUF("5", shiwakeCheckData.uf5Cd, shiwakeCheckData.uf5Nm, ColumnName.UF5_SHIYOU_FLG, errorList);
		this.checkUF("6", shiwakeCheckData.uf6Cd, shiwakeCheckData.uf6Nm, ColumnName.UF6_SHIYOU_FLG, errorList);
		this.checkUF("7", shiwakeCheckData.uf7Cd, shiwakeCheckData.uf7Nm, ColumnName.UF7_SHIYOU_FLG, errorList);
		this.checkUF("8", shiwakeCheckData.uf8Cd, shiwakeCheckData.uf8Nm, ColumnName.UF8_SHIYOU_FLG, errorList);
		this.checkUF("9", shiwakeCheckData.uf9Cd, shiwakeCheckData.uf9Nm, ColumnName.UF9_SHIYOU_FLG, errorList);
		this.checkUF("10", shiwakeCheckData.uf10Cd, shiwakeCheckData.uf10Nm, ColumnName.UF10_SHIYOU_FLG, errorList);

		//UF固定1-10入力チェック
		this.checkUFKotei("1", shiwakeCheckData.ufKotei1Cd, shiwakeCheckData.ufKotei1Nm, ColumnName.UF_KOTEI1_SHIYOU_FLG, errorList);
		this.checkUFKotei("2", shiwakeCheckData.ufKotei2Cd, shiwakeCheckData.ufKotei2Nm, ColumnName.UF_KOTEI2_SHIYOU_FLG, errorList);
		this.checkUFKotei("3", shiwakeCheckData.ufKotei3Cd, shiwakeCheckData.ufKotei3Nm, ColumnName.UF_KOTEI3_SHIYOU_FLG, errorList);
		this.checkUFKotei("4", shiwakeCheckData.ufKotei4Cd, shiwakeCheckData.ufKotei4Nm, ColumnName.UF_KOTEI4_SHIYOU_FLG, errorList);
		this.checkUFKotei("5", shiwakeCheckData.ufKotei5Cd, shiwakeCheckData.ufKotei5Nm, ColumnName.UF_KOTEI5_SHIYOU_FLG, errorList);
		this.checkUFKotei("6", shiwakeCheckData.ufKotei6Cd, shiwakeCheckData.ufKotei6Nm, ColumnName.UF_KOTEI6_SHIYOU_FLG, errorList);
		this.checkUFKotei("7", shiwakeCheckData.ufKotei7Cd, shiwakeCheckData.ufKotei7Nm, ColumnName.UF_KOTEI7_SHIYOU_FLG, errorList);
		this.checkUFKotei("8", shiwakeCheckData.ufKotei8Cd, shiwakeCheckData.ufKotei8Nm, ColumnName.UF_KOTEI8_SHIYOU_FLG, errorList);
		this.checkUFKotei("9", shiwakeCheckData.ufKotei9Cd, shiwakeCheckData.ufKotei9Nm, ColumnName.UF_KOTEI9_SHIYOU_FLG, errorList);
		this.checkUFKotei("10", shiwakeCheckData.ufKotei10Cd, shiwakeCheckData.ufKotei10Nm, ColumnName.UF_KOTEI10_SHIYOU_FLG, errorList);

		//拡張 前処理の戻し
		shiwakeCheckData.kamokuCd = kamokuCd;

		//仕訳パターン入力チェック
		this.checkShiwakePattern(denpyouKbn, shiwakePatternSettingKbn, daihyouBumonCd, shiwakeCheckData, errorList);
	}
	
	/**
	 * 勘定科目入力チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param errorList エラーリスト
	 * @param denpyouKbn 伝票区分
	 */
	protected void checkKanjouKamoku(ShiwakeCheckData shiwakeCheckData, List<String> errorList, String denpyouKbn) {

		if(null == shiwakeCheckData.kamokuCd)
		{
			return;
		}

		// UF使用フラグ
		boolean kamokuUf1RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF1_SHIYOU_FLG));
		boolean kamokuUf2RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF2_SHIYOU_FLG));
		boolean kamokuUf3RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF3_SHIYOU_FLG));
		boolean kamokuUf4RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF4_SHIYOU_FLG));
		boolean kamokuUf5RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF5_SHIYOU_FLG));
		boolean kamokuUf6RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF6_SHIYOU_FLG));
		boolean kamokuUf7RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF7_SHIYOU_FLG));
		boolean kamokuUf8RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF8_SHIYOU_FLG));
		boolean kamokuUf9RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF9_SHIYOU_FLG));
		boolean kamokuUf10RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF10_SHIYOU_FLG));

		// UF(固定)使用フラグ
		boolean kamokuUfKotei1RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI1_SHIYOU_FLG));
		boolean kamokuUfKotei2RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI2_SHIYOU_FLG));
		boolean kamokuUfKotei3RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI3_SHIYOU_FLG));
		boolean kamokuUfKotei4RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI4_SHIYOU_FLG));
		boolean kamokuUfKotei5RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI5_SHIYOU_FLG));
		boolean kamokuUfKotei6RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI6_SHIYOU_FLG));
		boolean kamokuUfKotei7RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI7_SHIYOU_FLG));
		boolean kamokuUfKotei8RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI8_SHIYOU_FLG));
		boolean kamokuUfKotei9RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI9_SHIYOU_FLG));
		boolean kamokuUfKotei10RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI10_SHIYOU_FLG));

		if (StringUtils.isNotEmpty(shiwakeCheckData.kamokuCd)) {

			String kamokuNm = shiwakeCheckData.kamokuNm;
			String kamokuCd = shiwakeCheckData.kamokuCd;

			//勘定科目 存在チェック
			if (null == masterLg.findKamokuName(kamokuCd)) {
				errorList.add(kamokuNm + "[" + kamokuCd + "]は存在しません。");
			} else {
				// 科目-各項目未入力チェック
				this.checkKamokuMinyuuryoku(shiwakeCheckData, errorList);

				// 勘定科目紐付チェック
				String formatMsg = kamokuNm + "[" + kamokuCd + "]と%s[%s]が紐づきません。";

				//▼カスタマイズ 部門科目・科目取引先の紐付チェックを行わない伝票区分
				String[] DENPYOU_KBN_HIMODUKI_NO_CHECK = {DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, DENPYOU_KBN.RYOHI_SEISAN, DENPYOU_KBN.SIHARAIIRAI};
				// 勘定科目-取引先の紐付チェック
				if (null != shiwakeCheckData.torihikisakiCd) {
					String checkNm = shiwakeCheckData.torihikisakiNm;
					String checkCd = shiwakeCheckData.torihikisakiCd;
					//▼カスタマイズ A001・A004・A013では科目取引先紐付チェック不要
					if(StringUtils.isNotEmpty(checkCd) 
					&& (StringUtils.isNotEmpty(denpyouKbn) && !ArrayUtils.contains(DENPYOU_KBN_HIMODUKI_NO_CHECK, denpyouKbn))) {
						if (!masterLg.existsTorihikisakiKamokuZandaka(checkCd, kamokuCd)) {
							errorList.add(String.format(formatMsg,checkNm,checkCd));
						}
					}
				}

				// 勘定科目-勘定科目枝番の紐付チェック
				if (null != shiwakeCheckData.kamokuEdabanCd) {
					String checkNm = shiwakeCheckData.kamokuEdabanNm;
					String checkCd = shiwakeCheckData.kamokuEdabanCd;
					if(StringUtils.isNotEmpty(checkCd)) {
						if (!masterLg.existsKamokuEdabanZandaka(kamokuCd, checkCd)) {
							errorList.add(String.format(formatMsg,checkNm,checkCd));
						}
					}
				}

				// 勘定科目-負担部門の紐付チェック
				if (null != shiwakeCheckData.futanBumonCd) {
					String checkNm = shiwakeCheckData.futanBumonNm;
					String checkCd = shiwakeCheckData.futanBumonCd;
					//▼カスタマイズ A001・A004・A013では部門科目紐付チェック不要
					if(StringUtils.isNotEmpty(checkCd) 
					&& (StringUtils.isNotEmpty(denpyouKbn) && !ArrayUtils.contains(DENPYOU_KBN_HIMODUKI_NO_CHECK, denpyouKbn))) {
						if (!masterLg.existsFutanBumonKamokuZandaka(checkCd, kamokuCd)) {
							errorList.add(String.format(formatMsg,checkNm,checkCd));
						}
					}
				}

				// 勘定科目-セグメントの紐付チェック
				if (null != shiwakeCheckData.segmentCd) {
					String checkNm = shiwakeCheckData.segmentNm;
					String checkCd = shiwakeCheckData.segmentCd;
					if(StringUtils.isNotEmpty(checkCd)) {
						if (!masterLg.existsSegmentKamokuZandaka(checkCd, kamokuCd)) {
							errorList.add(String.format(formatMsg,checkNm,checkCd));
						}
					}
				}

				// 勘定科目-UF1-10の紐付チェック
				if (kamokuUf1RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf1Cd)
						&& !masterLg.existsUFZandaka("1", shiwakeCheckData.uf1Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf1Nm, shiwakeCheckData.uf1Cd));
				}
				if (kamokuUf2RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf2Cd)
						&& !masterLg.existsUFZandaka("2", shiwakeCheckData.uf2Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf2Nm, shiwakeCheckData.uf2Cd));
				}
				if (kamokuUf3RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf3Cd)
						&& !masterLg.existsUFZandaka("3", shiwakeCheckData.uf3Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf3Nm, shiwakeCheckData.uf3Cd));
				}
				if (kamokuUf4RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf4Cd)
						&& !masterLg.existsUFZandaka("4", shiwakeCheckData.uf4Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf4Nm, shiwakeCheckData.uf4Cd));
				}
				if (kamokuUf5RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf5Cd)
						&& !masterLg.existsUFZandaka("5", shiwakeCheckData.uf5Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf5Nm, shiwakeCheckData.uf5Cd));
				}
				if (kamokuUf6RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf6Cd)
						&& !masterLg.existsUFZandaka("6", shiwakeCheckData.uf6Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf6Nm, shiwakeCheckData.uf6Cd));
				}
				if (kamokuUf7RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf7Cd)
						&& !masterLg.existsUFZandaka("7", shiwakeCheckData.uf7Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf7Nm, shiwakeCheckData.uf7Cd));
				}
				if (kamokuUf8RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf8Cd)
						&& !masterLg.existsUFZandaka("8", shiwakeCheckData.uf8Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf8Nm, shiwakeCheckData.uf8Cd));
				}
				if (kamokuUf9RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf9Cd)
						&& !masterLg.existsUFZandaka("9", shiwakeCheckData.uf9Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf9Nm, shiwakeCheckData.uf9Cd));
				}
				if (kamokuUf10RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf10Cd)
						&& !masterLg.existsUFZandaka("10", shiwakeCheckData.uf10Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf10Nm, shiwakeCheckData.uf10Cd));
				}

				// 勘定科目-UF(固定値)1-10の紐付チェック
				if (kamokuUfKotei1RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei1Cd)
						&& !masterLg.existsUFKoteiZandaka("1", shiwakeCheckData.ufKotei1Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei1Nm, shiwakeCheckData.ufKotei1Cd));
				}
				if (kamokuUfKotei2RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei2Cd)
						&& !masterLg.existsUFKoteiZandaka("2", shiwakeCheckData.ufKotei2Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei2Nm, shiwakeCheckData.ufKotei2Cd));
				}
				if (kamokuUfKotei3RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei3Cd)
						&& !masterLg.existsUFKoteiZandaka("3", shiwakeCheckData.ufKotei3Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei3Nm, shiwakeCheckData.ufKotei3Cd));
				}
				if (kamokuUfKotei4RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei4Cd)
						&& !masterLg.existsUFKoteiZandaka("4", shiwakeCheckData.ufKotei4Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei4Nm, shiwakeCheckData.ufKotei4Cd));
				}
				if (kamokuUfKotei5RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei5Cd)
						&& !masterLg.existsUFKoteiZandaka("5", shiwakeCheckData.ufKotei5Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei5Nm, shiwakeCheckData.ufKotei5Cd));
				}
				if (kamokuUfKotei6RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei6Cd)
						&& !masterLg.existsUFKoteiZandaka("6", shiwakeCheckData.ufKotei6Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei6Nm, shiwakeCheckData.ufKotei6Cd));
				}
				if (kamokuUfKotei7RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei7Cd)
						&& !masterLg.existsUFKoteiZandaka("7", shiwakeCheckData.ufKotei7Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei7Nm, shiwakeCheckData.ufKotei7Cd));
				}
				if (kamokuUfKotei8RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei8Cd)
						&& !masterLg.existsUFKoteiZandaka("8", shiwakeCheckData.ufKotei8Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei8Nm, shiwakeCheckData.ufKotei8Cd));
				}
				if (kamokuUfKotei9RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei9Cd)
						&& !masterLg.existsUFKoteiZandaka("9", shiwakeCheckData.ufKotei9Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei9Nm, shiwakeCheckData.ufKotei9Cd));
				}
				if (kamokuUfKotei10RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei10Cd)
						&& !masterLg.existsUFKoteiZandaka("10", shiwakeCheckData.ufKotei10Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei10Nm, shiwakeCheckData.ufKotei10Cd));
				}
			}
		}
	}
}