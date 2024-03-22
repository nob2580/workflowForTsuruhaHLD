package eteam.gyoumu.tsuuchi;

import java.util.ArrayList;
import java.util.List;

import eteam.base.GMap;
import lombok.Getter;
import lombok.Setter;

/**
 * @author j_matsumoto
 *
 */
@Getter @Setter
public abstract class AbstractDenpyouIchiranActionStrategy
{
	/** 追加カラムのリスト */
	public List<String> additionalColumnNameList = new ArrayList<String>();
	
	/** 追加キーのリスト */
	public List<String> additionalKeyList = new ArrayList<String>();
	
	/** CSV用のデータマップ */
	public GMap mapForCsv;
	
	/** JSP用のデータマップ */
	public GMap mapForJsp;
	
	/**
	 * 追加カラム名について、加工が必要な場合に加工する。既定値は無加工のリスト。
	 * @return 加工後のCSV列名リスト
	 */
	public List<String> getProcessedCsvColumnNameList()
	{
		return this.additionalColumnNameList;
	}
	
	/**
	 * 追加キーデータについて、加工が必要な場合に加工する（csv用）。既定値は無加工のリスト。
	 * @return 加工後のCSV用追加データリスト
	 */
	public List<Object> getProcessedCsvMap()
	{
		List<Object> csvRecord = new ArrayList<Object>();
		
		for(String key : this.additionalKeyList)
		{
			csvRecord.add(mapForCsv.get(key));
		}
		
		return csvRecord;
	}
	
	/**
	 * 追加キーデータについて、加工が必要な場合に加工する（JSP用）。既定値は無加工のリスト。
	 */
	public void getProcessedJspMap()
	{
	}
}
