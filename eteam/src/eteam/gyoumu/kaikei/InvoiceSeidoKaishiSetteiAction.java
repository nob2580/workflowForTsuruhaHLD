package eteam.gyoumu.kaikei;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.database.abstractdao.InvoiceStartAbstractDao;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * インボイス制度開始設定画面Action
 */
@Getter @Setter @ToString
public class InvoiceSeidoKaishiSetteiAction extends EteamAbstractAction {
	
//＜定数＞
	/** インボイス開始フラグ */
	String invoiceStartFlg;
	
//＜イベント＞
	
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()){
			InvoiceStartAbstractDao myLogic = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);
			
			//インボイス開始テーブルに値がない、またはインボイス開始フラグの値が「0」の場合"0"
			//インボイス開始フラグが「1」の場合"1"
			invoiceStartFlg = myLogic.exists() ? myLogic.find().invoiceFlg : "0";
			
			//戻り値を返す
			return "success";
		}
	}
	
	/**
	 * 登録イベント
	 * @return ResultName
	 */
	public String touroku() {
		
		try(EteamConnection connection = EteamConnection.connect()){
			InvoiceStartAbstractDao myLogic = EteamContainer.getComponent(InvoiceStartAbstractDao.class, connection);
			
			// インボイス開始テーブルへ登録する。
			if(myLogic.exists()) {
				myLogic.update(getUser().getTourokuOrKoushinUserId());
			}else {
				myLogic.insert(getUser().getTourokuOrKoushinUserId());
			}
			connection.commit();
			
			// 戻り値を返す
			return "success";
		}
	}

	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {}

}
