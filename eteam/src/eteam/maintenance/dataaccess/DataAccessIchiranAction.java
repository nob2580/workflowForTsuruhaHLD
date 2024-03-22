package eteam.maintenance.dataaccess;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.select.DataAccessCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * データアクセス一覧画面Action
 */
@Getter @Setter @ToString
public class DataAccessIchiranAction  extends EteamAbstractAction {

	/** テーブルリスト */
	List<GMap> tableList;

	@Override
	protected void formatCheck() {
	}

	@Override
	protected void hissuCheck(int eventNum) {
	}

	/**
	 * データ表示
	 * @return 処理結果
	 */
	public String init() {

		try(EteamConnection connection = EteamConnection.connect()) {

			String schemaName = EteamCommon.getContextSchemaName();

			DataAccessCategoryLogic dail = EteamContainer.getComponent(DataAccessCategoryLogic.class, connection);
			tableList = dail.loadTableList(schemaName);

			return "success";

		}
	}
}
