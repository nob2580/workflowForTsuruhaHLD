package eteam.common.access;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamConnection;
import eteam.common.EteamCommon;

public class Hoge {
	public static void main(String[] argv) {
		//TODO:環境情報はプロパティー化したいね
		EteamConnection connection = EteamConnection.connect();
// GamenKengenSeigyoLogic accLg = EteamContainer.getComponent(GamenKengenSeigyoLogic.class, connection);
// accLg.judgeAccess("hoge", null);
		
// EventLogLogic eventLogic = EteamContainer.getComponent(EventLogLogic.class);
// long serialNo = eventLogic.startLog("", "Hoge", "execute");
// eventLogic.endLog(serialNo, "success");
// BumonUserKanriCategoryLogic lg = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
// lg.userSerach("", "", false);
// lg.userSerach("!\"#$%&'()zxcvbnm,./'", "", false);
		List<String> errorList = new ArrayList<String>();
		errorList.addAll(new ArrayList<String>());
		errorList.addAll(EteamCommon.passwordCheck("a "));
		errorList.addAll(new ArrayList<String>());
	}
}
