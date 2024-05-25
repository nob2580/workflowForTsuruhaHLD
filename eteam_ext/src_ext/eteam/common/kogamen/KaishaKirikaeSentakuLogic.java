package eteam.common.kogamen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.common.select.PostgreSQLSystemCatalogsExtLogic;
import eteam.common.select.PostgreSQLSystemCatalogsLogic;

/**
 * 会社切替選択ダイアログLogic
 */
public class KaishaKirikaeSentakuLogic extends EteamAbstractLogic {

	/**
	 * 会社切替一覧取得<br>
	 * 会社切替からデータを取得する。
	 * 
	 * @return 会社切替リスト
	 */
	public List<Map<String, Object>> loadKaishaKirikaeList(){
		PostgreSQLSystemCatalogsLogic pgLog = EteamContainer.getComponent(PostgreSQLSystemCatalogsLogic.class);
		pgLog.init(connection);

		List<Map<String, Object>> retList = new ArrayList<>();
		List<String> schemeList = pgLog.loadAllSchemaName();
		for(String scheme : schemeList){
			String schemeName = ((PostgreSQLSystemCatalogsExtLogic)pgLog).findSchemeName(scheme);
			Map<String, Object> retMap = new HashMap<>();
			retMap.put("scheme_cd", scheme);
			retMap.put("scheme_name", schemeName);
			retList.add(retMap);
		}
		return retList;
	}
}