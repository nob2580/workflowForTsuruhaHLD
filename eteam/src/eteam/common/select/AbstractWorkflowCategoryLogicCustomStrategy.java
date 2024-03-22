package eteam.common.select;

import lombok.Getter;
import lombok.Setter;

/**
 * @author j_matsumoto
 *
 */
public abstract class AbstractWorkflowCategoryLogicCustomStrategy
{
	/**
	 * 現在のSQL
	 */
	@Getter @Setter
	protected String currentSql;
	
	/**
	 * @return loadEnableKanrenDenpyou用のカスタマイズ後SQL
	 */
	public String getCustomizeLoadEnableKanrenDenpyouSql()
	{
		return currentSql;
	}
}
