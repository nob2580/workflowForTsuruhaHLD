package eteam.common.select;

import java.util.Map;

public class PostgreSQLSystemCatalogsExtLogic extends PostgreSQLSystemCatalogsLogic {
	//カスタマイズここから
		/**
		 * スキーマ名から会社名を取得
		 * （もし会社名が無かった場合スキーマ名を返す）
		 * @param scheme スキーマ名
		 * @return スキーマ名と会社名が入ったマップ
		 */
		public String findSchemeName(String scheme) {
			String sql = "SELECT setting_val FROM " + scheme + ".setting_info WHERE setting_name='hyouji_kaisha_num'";
			Map<String, Object> settingMap = connection.find(sql);
			String schemeName = scheme;
			if(settingMap != null && !isEmpty(settingMap.get("setting_val"))){
				schemeName = (String)settingMap.get("setting_val");
			}
			return schemeName;
		}
	//カスタマイズここまで
}
