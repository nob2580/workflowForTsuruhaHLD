package eteam.common.access;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.common.EteamSendMailLogic;

/**
 * メール送信テスト
 *
 */
public class EteamSendMailLogicTest {

	/**
	 * メール送信テスト
	 * TOのみ設定
	 */
	@Test
	public void Test01() {

		try(EteamConnection connection = EteamConnection.connect()) {
			EteamSendMailLogic logic = EteamContainer.getComponent(EteamSendMailLogic.class, connection);
			
			String subject = "ＪＡＶＡ メール！";
			String text = "　ようこそ。\n\n" + "　これはＪＡＶＡのテストメールです。\n"
					+ "　あなたはＪＡＶＡメールを受信しました。\n";
	
			// "TO"の設定
			List<String> to = new ArrayList<String>();
	// to.add("eteam_sample@outlook.jp");
			to.add("eteam_takahashi@yahoo.co.jp");
			// "CC"の設定
			List<String> cc = null;
	
			// "BCC"の設定
			List<String> bcc = null;
	
			// メールの送信
			boolean ret = false;
			ret = logic.execute(subject, text, to, cc, bcc);
			assertEquals(true, ret);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			fail();
		}

	}
	
	/**
	 * メール送信テスト(宛先なし)
	 */
	@Test
	public void Test02() {

		try(EteamConnection connection = EteamConnection.connect()) {
			EteamSendMailLogic logic = EteamContainer.getComponent(EteamSendMailLogic.class, connection);
			
			String subject = "失敗メール";
			String text = "　送信失敗";
			
			// "TO"の設定
			List<String> to = new ArrayList<String>();
	
			
			// "CC"の設定
			List<String> cc = new ArrayList<String>();
	
	
			// "BCC"の設定
			List<String> bcc = new ArrayList<String>();
	
			// メールの送信
			boolean ret = false;
			ret = logic.execute(subject, text, to, cc, bcc);
		} catch (Exception e) {

			assertEquals(false, ret);
			return;
		}
		fail();
	}
	
	/**
	 * メール送信テスト(cc,bccの確認)
	 */
	@Test
	public void Test03() {

		try(EteamConnection connection = EteamConnection.connect()) {
			EteamSendMailLogic logic = EteamContainer.getComponent(EteamSendMailLogic.class, connection);
			
			String subject = "JAVAメールテスト03";
			String text = "cc,bccの送信確認";
			
			// "TO"の設定
			List<String> to = new ArrayList<String>();
			to.add("eteam_sample@outlook.jp");
			
			// "CC"の設定
			List<String> cc = new ArrayList<String>();
			cc.add("eteam_sample@outlook.jp");
	
			// "BCC"の設定
			List<String> bcc = new ArrayList<String>();
			bcc.add("eteam_sample@outlook.jp");
			
			// メールの送信
			boolean ret = false;
			ret = logic.execute(subject, text, to, cc, bcc);
			assertEquals(true, ret);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			fail();
		}
		
	}
}
