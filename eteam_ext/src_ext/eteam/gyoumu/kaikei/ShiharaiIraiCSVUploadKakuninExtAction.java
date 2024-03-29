package eteam.gyoumu.kaikei;

import java.util.List;

import eteam.common.HfUfSeigyo;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.gyoumu.kaikei.ShiharaiIraiCSVUploadSessionInfo.UploadStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 支払依頼（CSVアップロード確認）画面Action
 */
@Getter @Setter @ToString
public class ShiharaiIraiCSVUploadKakuninExtAction extends ShiharaiIraiCSVUploadKakuninAction {
	/**
	 * 
	 * @param sessionInfo セッションのオブジェクト
	 * @param contextSchemaName スキーマ名
	 */
	protected void startThread(ShiharaiIraiCSVUploadSessionInfo sessionInfo, String contextSchemaName) {
		ShiharaiIraiCSVUploadExtThread myThread = new ShiharaiIraiCSVUploadExtThread(session, sessionInfo, contextSchemaName);
		myThread.start();
	}
}
