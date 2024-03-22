package eteam.base.intercepter;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.common.BatchLogic;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.BATCH_STATUS;

/**
 * Batクラス全般のインターセプタ。
 */
public class EteamBatIntercepter extends AbstractInterceptor {

	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(EteamBatIntercepter.class);

	/**
	 * ロジック実行前後のログ出力インターセプト
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try(EteamConnection logConnection = EteamConnection.connect();
				EteamConnection haitaConnection = EteamConnection.connect();) {
			BatchLogic logLogic = EteamContainer.getComponent(BatchLogic.class, logConnection);
			BatchLogic haitaLogic = EteamContainer.getComponent(BatchLogic.class, haitaConnection);
			EteamAbstractBat bat = (EteamAbstractBat)invocation.getThis();
			String process = getTargetClass(invocation).getSimpleName() + "#" + invocation.getMethod().getName();
			String schema = EteamCommon.getBatSchemaName();

			//開始ログ
	        log.info(process + "バッチ　開始" + "(スキーマ:" + schema + ")");

			//排他取得
			haitaLogic.lock();

	        //開始バッチログ
			long serialNo = logLogic.startLog(bat.getBatchName(), bat.getCountName());
			logConnection.commit();
			if(invocation.getThis() instanceof EteamAbstractBat){//ifは絶対通るはず
				((EteamAbstractBat)invocation.getThis()).setSerialNo(serialNo);
			}

			//実行
			Object ret = invocation.proceed();

			//終了バッチログ
			String batchStatus = BATCH_STATUS.SHORICHUU;
			switch (String.valueOf(ret)) {
			case "0":
				batchStatus = BATCH_STATUS.SEIJOU;
				break;
			case "1":
				batchStatus = BATCH_STATUS.IJOU;
				break;
			}
			logLogic.endLog(serialNo, batchStatus, bat.getCount());
			logConnection.commit();
			
	        //排他解除
			haitaLogic.unlock();

			//終了ログ
			log.info(process + "バッチ　処理件数[" + bat.getCount() + "]");
			log.info(process + "バッチ　終了[" + ret + "]");
			return ret;
		}
	}
}