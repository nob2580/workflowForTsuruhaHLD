package eteam.base;

import java.util.List;

import eteam.common.EteamConst;
import eteam.common.EteamSettingInfo;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.common.ShiwakeData;
import eteam.gyoumu.tsuuchi.DenpyouIchiranUpdateLogic;
import lombok.Setter;

/**
 * 会計バッチの基底。
 *
 */
public abstract class EteamAbstractKaikeiBat extends EteamAbstractBat {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamAbstractKaikeiBat.class);

	/** バッチ会計共通ロジック */
	protected BatchKaikeiCommonLogic common;
	/** 伝票一覧更新ロジック */
	protected DenpyouIchiranUpdateLogic diLogic;
	
	/** コネクション */
	@Setter
	protected EteamConnection connection;
	/** メモリ上だけで仕訳作るモード(伝票一覧用) */
	@Setter
	protected boolean onMemory;
	
	//定数
	/** 100：対象外　(消費税仕訳の課税区分) */
	protected static final String KAZEI_KBN_SHOUHIZEI_SHIWAKE = "100";
	// この定数をメンテする際は KeihiTatekaeSeisanChuushutsuLogic の定数もメンテすること

	/**
	 * 仕訳単位を取得するためのキーを返します。
	 * @return キー文字列
	 */
	protected abstract String getIdLevelRenkeiKey();

	/**
	 * 仕訳を作成して返す。
	 * ※伝票一覧用にメモリ上で作るだけなので、何かをアップデートする前にreturnすべし。
	 * @param denpyouId 伝票ID
	 * @return 仕訳リスト
	 */
	public abstract List<ShiwakeData> makeShiwake(String denpyouId);

	/**
	 * 伝票の仕訳抽出を行います。
	 * @param denpyou 伝票情報
	 * @return 仕訳リスト
	 */
	protected abstract List<ShiwakeData> denpyouChuushutsu(GMap denpyou);

	/**
	 * 抽出をします。
	 * @param denpyouList 対象伝票情報
	 * @return 結果コード
	 */
	protected int chuushutsuShiwake(List<GMap> denpyouList) {
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		diLogic = EteamContainer.getComponent(DenpyouIchiranUpdateLogic.class, connection);

		boolean isWfLevelCommit = isWfLevelCommit();
		boolean isWfLevelRenkei = isWfLevelRenkei();

		//伝票単位に処理
		int _count = 0;
		for (GMap denpyou : denpyouList) {
			try {
				String denpyouId = (String)denpyou.get("denpyou_id");
				connection.setSearchPath();
				denpyouChuushutsu(denpyou);
				//伝票一覧データ更新
				diLogic.updateDenpyouIchiran(denpyouId);
				if (!isWfLevelRenkei) {
					// type3,4
					common.updateShiwakeGSEPByDate(denpyouId,(String)denpyou.get("invoice_denpyou"));
					common.updateShiwakeDCNOByDate(denpyouId,(String)denpyou.get("invoice_denpyou"));
				}
				if (isWfLevelCommit) {
					// type2,4
					connection.commit();
				}
				_count++;
			} catch (Exception e) {
				if (!isWfLevelCommit) {
					throw e;
				}
				// type1,3
				connection.rollback();
				log.warn("仕訳作成中に一つの伝票でエラー発生 ID = " + (String)denpyou.get("denpyou_id"), e);
			}
		}

		connection.commit();
		setCount(_count);
		if (_count != denpyouList.size()) {
			return 1;
		}
		return 0;
	}

	/**
	 * 仕訳コミット単位がID単位かどうか
	 * @return ID単位の時true
	 */
	protected boolean isWfLevelCommit() {
		return EteamConst.SHIWAKE_COMMIT_TYPE.DENPYOU_ID.equals(setting.shiwakeSakuseiErrorAction());
	}

	/**
	 * 仕訳連携単位がID単位かどうか
	 * @return ID単位の時true
	 */
	protected boolean isWfLevelRenkei() {
		return EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(EteamSettingInfo.getSettingInfo(getIdLevelRenkeiKey()));
	}
}
