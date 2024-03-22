package eteam.base;

import eteam.common.EteamSettingInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * バッチクラスの親
 */
@Getter @Setter @ToString
public abstract class EteamAbstractBat {

	/** 初期化 */
	public EteamAbstractBat()
	{
		setting = new EteamSettingInfo();
	}
	
	/** 設定情報 */
	protected EteamSettingInfo setting;
	
	//処理結果の授受
	/** バッチ名 */
	String batchName;
	/** 件数名 */
	String countName;
	/** 件数 */
	int count = 0;
	/** シリアル番号 */
	long serialNo;
	
	/**
	 * バッチ名を返す
	 * @return バッチ名
	 */
	public abstract String getBatchName();

	/**
	 * 件数名を返す
	 * @return バッチ名
	 */
	public abstract String getCountName();

	/**
	 * バッチ処理
	 * @return 0:正常　1:異常
	 */
	public abstract int mainProc();
}
