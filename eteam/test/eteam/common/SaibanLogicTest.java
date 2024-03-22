package eteam.common;

import org.junit.Test;

import eteam.base.EteamContainer;
import eteam.common.SaibanLogic;

/**
 * 採番テスト
 */
public class SaibanLogicTest {

	/**
	 * test
	 */
	@Test
	public void saibanInfoId() {
		System.setProperty("isUt", "1");
		try {
			SaibanLogic logic = EteamContainer.getComponent(SaibanLogic.class);
			System.out.println(logic.saibanInfoId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * test
	 */
	@Test
	public void saibanDenpyouId() {
		System.setProperty("isUt", "1");
		try {
			SaibanLogic logic = EteamContainer.getComponent(SaibanLogic.class);
			System.out.println(logic.saibanDenpyouId("AA"));
			System.out.println(logic.saibanDenpyouId("AA"));
			System.out.println(logic.saibanDenpyouId("BB"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
