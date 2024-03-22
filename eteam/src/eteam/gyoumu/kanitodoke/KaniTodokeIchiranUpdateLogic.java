package eteam.gyoumu.kanitodoke;

import java.util.ArrayList;
import java.util.List;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.KaniTodokeCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * ユーザー定義届書一覧参照用テーブル登録・更新用ロジック
 */
@Getter @Setter @ToString
public class KaniTodokeIchiranUpdateLogic extends EteamAbstractLogic {

	
	/**
	 * 全伝票IDを取得。
	 * @return 全伝票IDリスト
	 */
	public List<GMap> loadAllDenpyouId(){
		String sql = "SELECT denpyou_id FROM denpyou WHERE denpyou_kbn BETWEEN 'B001' AND 'B999'";
		return connection.load(sql);
	}
	
	/**
	 * 全伝票ID数を取得。
	 * @return 全伝票ID数
	 */
	public long countAllDenpyouId(){
		String sql = "SELECT count(denpyou_id) AS cnt FROM denpyou WHERE denpyou_kbn BETWEEN 'B001' AND 'B999'";
		GMap mp = connection.find(sql);
		long cnt = (null == mp.get("cnt"))? 0 : (long)mp.get("cnt");
		return cnt;
	}
	
	/**
	 * 指定した伝票区分の全伝票IDを取得。
	 * @param denpyouKbn 伝票区分
	 * @return 伝票IDリスト
	 */
	public List<GMap> loadAllDenpyouId(String denpyouKbn){
		String sql = "SELECT denpyou_id FROM denpyou WHERE denpyou_kbn = ?";
		return connection.load(sql, denpyouKbn);
	}
	
	/**
	 * 明細数を取得
	 * @param denpyouId 伝票ID
	 * @return 明細数
	 */
	public int countMeisai(String denpyouId){
		String sql = "SELECT MAX(denpyou_edano) AS cnt FROM kani_todoke_meisai WHERE denpyou_id = ?";
		GMap mp = connection.find(sql, denpyouId);
		int cnt = (null == mp.get("cnt"))? 0 : (int)mp.get("cnt");
		return cnt;
	}
	
	/**
	 * 指定した伝票IDのデータをユーザー定義届書一覧テーブルから削除する。
	 * @param denpyouId 伝票番号
	 */
	public void deleteKaniTodokeIchiran(String denpyouId){
		String sqlupd = "DELETE FROM kani_todoke_ichiran WHERE denpyou_id = ? ";
		connection.update(sqlupd,denpyouId);
	}

	/**
	 * 指定した伝票IDのデータをユーザー定義届書明細一覧テーブルから削除する。
	 * @param denpyouId 伝票番号
	 */
	public void deleteKaniTodokeMeisaiIchiran(String denpyouId){
		String sqlupd = "DELETE FROM kani_todoke_meisai_ichiran WHERE denpyou_id = ? ";
		connection.update(sqlupd,denpyouId);
	}
	
	/**
	 * 指定した伝票IDのデータをユーザー定義届書一覧テーブルに登録する。(共通表示部分)
	 * @param denpyouId 伝票番号
	 */
	public void insertKaniTodokeIchiran(String denpyouId){
		//コネクションのコミットは本メソッドの外側で実行すること
		
		String sql = "SELECT * FROM denpyou WHERE denpyou_id = ?";
		GMap map = connection.find(sql, denpyouId);
		if(map == null)return; //伝票個別テーブルにデータが存在してdenpyouにデータが存在しない場合があるので念のため確認

		KaniTodokeIchiranData kd = new KaniTodokeIchiranData();
		
		String sqlupd = "";
		List<Object> paramsUpd = new ArrayList<Object>();
		paramsUpd.add(denpyouId);
		paramsUpd.add(kd.shinsei01);
		paramsUpd.add(kd.shinsei02);
		paramsUpd.add(kd.shinsei03);
		paramsUpd.add(kd.shinsei04);
		paramsUpd.add(kd.shinsei05);
		paramsUpd.add(kd.shinsei06);
		paramsUpd.add(kd.shinsei07);
		paramsUpd.add(kd.shinsei08);
		paramsUpd.add(kd.shinsei09);
		paramsUpd.add(kd.shinsei10);
		paramsUpd.add(kd.shinsei11);
		paramsUpd.add(kd.shinsei12);
		paramsUpd.add(kd.shinsei13);
		paramsUpd.add(kd.shinsei14);
		paramsUpd.add(kd.shinsei15);
		paramsUpd.add(kd.shinsei16);
		paramsUpd.add(kd.shinsei17);
		paramsUpd.add(kd.shinsei18);
		paramsUpd.add(kd.shinsei19);
		paramsUpd.add(kd.shinsei20);
		paramsUpd.add(kd.shinsei21);
		paramsUpd.add(kd.shinsei22);
		paramsUpd.add(kd.shinsei23);
		paramsUpd.add(kd.shinsei24);
		paramsUpd.add(kd.shinsei25);
		paramsUpd.add(kd.shinsei26);
		paramsUpd.add(kd.shinsei27);
		paramsUpd.add(kd.shinsei28);
		paramsUpd.add(kd.shinsei29);
		paramsUpd.add(kd.shinsei30);
		//insert
		sqlupd = ""
				+ "INSERT INTO kani_todoke_ichiran( "
			    + " denpyou_id, "
			    + " shinsei01, "
			    + " shinsei02, "
			    + " shinsei03, "
			    + " shinsei04, "
			    + " shinsei05, "
			    + " shinsei06, "
			    + " shinsei07, "
			    + " shinsei08, "
			    + " shinsei09, "
			    + " shinsei10, "
			    + " shinsei11, "
			    + " shinsei12, "
			    + " shinsei13, "
			    + " shinsei14, "
			    + " shinsei15, "
			    + " shinsei16, "
			    + " shinsei17, "
			    + " shinsei18, "
			    + " shinsei19, "
			    + " shinsei20, "
			    + " shinsei21, "
			    + " shinsei22, "
			    + " shinsei23, "
			    + " shinsei24, "
			    + " shinsei25, "
			    + " shinsei26, "
			    + " shinsei27, "
			    + " shinsei28, "
			    + " shinsei29, "
			    + " shinsei30 "
			    + ") "
			    + "VALUES( "
			    + " ?,?,?,?,?,?,?,?,?,?, "
			    + " ?,?,?,?,?,?,?,?,?,?, "
			    + " ?,?,?,?,?,?,?,?,?,?, "
			    + " ? "
			    + ") ";
		connection.update(sqlupd, paramsUpd.toArray());
	}
	
	/**
	 * 指定した伝票IDのデータをユーザー定義届書一覧テーブルに登録する。(共通表示部分)
	 * @param denpyouId 伝票番号
	 */
	public void insertKaniTodokeMeisaiIchiran(String denpyouId){
		//コネクションのコミットは本メソッドの外側で実行すること
		
		String sql = "SELECT * FROM denpyou WHERE denpyou_id = ?";
		GMap map = connection.find(sql, denpyouId);
		if(map == null)return; //伝票個別テーブルにデータが存在してdenpyouにデータが存在しない場合があるので念のため確認
		
		int cnt = this.countMeisai(denpyouId);
		int i = 0 ;
		while(i < cnt){
			
			KaniTodokeMeisaiIchiranData kmd = new KaniTodokeMeisaiIchiranData();
			
			String sqlupd = "";
			List<Object> paramsUpd = new ArrayList<Object>();
			paramsUpd.add(denpyouId);
			paramsUpd.add(++i);
			paramsUpd.add(kmd.meisai01);
			paramsUpd.add(kmd.meisai02);
			paramsUpd.add(kmd.meisai03);
			paramsUpd.add(kmd.meisai04);
			paramsUpd.add(kmd.meisai05);
			paramsUpd.add(kmd.meisai06);
			paramsUpd.add(kmd.meisai07);
			paramsUpd.add(kmd.meisai08);
			paramsUpd.add(kmd.meisai09);
			paramsUpd.add(kmd.meisai10);
			paramsUpd.add(kmd.meisai11);
			paramsUpd.add(kmd.meisai12);
			paramsUpd.add(kmd.meisai13);
			paramsUpd.add(kmd.meisai14);
			paramsUpd.add(kmd.meisai15);
			paramsUpd.add(kmd.meisai16);
			paramsUpd.add(kmd.meisai17);
			paramsUpd.add(kmd.meisai18);
			paramsUpd.add(kmd.meisai19);
			paramsUpd.add(kmd.meisai20);
			paramsUpd.add(kmd.meisai21);
			paramsUpd.add(kmd.meisai22);
			paramsUpd.add(kmd.meisai23);
			paramsUpd.add(kmd.meisai24);
			paramsUpd.add(kmd.meisai25);
			paramsUpd.add(kmd.meisai26);
			paramsUpd.add(kmd.meisai27);
			paramsUpd.add(kmd.meisai28);
			paramsUpd.add(kmd.meisai29);
			paramsUpd.add(kmd.meisai30);
			//insert
			sqlupd = ""
					+ "INSERT INTO kani_todoke_meisai_ichiran( "
				    + " denpyou_id, "
				    + " denpyou_edano, "
				    + " meisai01, "
				    + " meisai02, "
				    + " meisai03, "
				    + " meisai04, "
				    + " meisai05, "
				    + " meisai06, "
				    + " meisai07, "
				    + " meisai08, "
				    + " meisai09, "
				    + " meisai10, "
				    + " meisai11, "
				    + " meisai12, "
				    + " meisai13, "
				    + " meisai14, "
				    + " meisai15, "
				    + " meisai16, "
				    + " meisai17, "
				    + " meisai18, "
				    + " meisai19, "
				    + " meisai20, "
				    + " meisai21, "
				    + " meisai22, "
				    + " meisai23, "
				    + " meisai24, "
				    + " meisai25, "
				    + " meisai26, "
				    + " meisai27, "
				    + " meisai28, "
				    + " meisai29, "
				    + " meisai30 "
				    + ") "
				    + "VALUES( "
				    + " ?,?,?,?,?,?,?,?,?,?, "
				    + " ?,?,?,?,?,?,?,?,?,?, "
				    + " ?,?,?,?,?,?,?,?,?,?, "
				    + " ?,? "
				    + ") ";
			connection.update(sqlupd, paramsUpd.toArray());
		}
	}

	/**
	 * ユーザー定義届書一覧テーブルの指定した伝票IDのデータを更新する。(共通表示部分)
	 * @param denpyouId 伝票番号
	 */
	public void updateKaniTodokeIchiran(String denpyouId){
		String sql = "";
		
		KaniTodokeIchiranData kd = new KaniTodokeIchiranData();

		sql = "SELECT * FROM denpyou WHERE denpyou_id = ?";
		GMap map = connection.find(sql, denpyouId);
		if(map == null)return; //伝票個別テーブルにデータが存在してdenpyouにデータが存在しない場合があるので念のため確認
		
		sql = "";
		map = this.getKanitodokeIchiranDat(denpyouId);
		if(map == null)return; //表示用のデータが存在しない場合があるので念のため確認
		
		//各種表示用データを登録
		kd.shinsei01 = map.get("shinsei01") == null ? "" : (String)map.get("shinsei01");
		kd.shinsei02 = map.get("shinsei02") == null ? "" : (String)map.get("shinsei02");
		kd.shinsei03 = map.get("shinsei03") == null ? "" : (String)map.get("shinsei03");
		kd.shinsei04 = map.get("shinsei04") == null ? "" : (String)map.get("shinsei04");
		kd.shinsei05 = map.get("shinsei05") == null ? "" : (String)map.get("shinsei05");
		kd.shinsei06 = map.get("shinsei06") == null ? "" : (String)map.get("shinsei06");
		kd.shinsei07 = map.get("shinsei07") == null ? "" : (String)map.get("shinsei07");
		kd.shinsei08 = map.get("shinsei08") == null ? "" : (String)map.get("shinsei08");
		kd.shinsei09 = map.get("shinsei09") == null ? "" : (String)map.get("shinsei09");
		kd.shinsei10 = map.get("shinsei10") == null ? "" : (String)map.get("shinsei10");
		kd.shinsei11 = map.get("shinsei11") == null ? "" : (String)map.get("shinsei11");
		kd.shinsei12 = map.get("shinsei12") == null ? "" : (String)map.get("shinsei12");
		kd.shinsei13 = map.get("shinsei13") == null ? "" : (String)map.get("shinsei13");
		kd.shinsei14 = map.get("shinsei14") == null ? "" : (String)map.get("shinsei14");
		kd.shinsei15 = map.get("shinsei15") == null ? "" : (String)map.get("shinsei15");
		kd.shinsei16 = map.get("shinsei16") == null ? "" : (String)map.get("shinsei16");
		kd.shinsei17 = map.get("shinsei17") == null ? "" : (String)map.get("shinsei17");
		kd.shinsei18 = map.get("shinsei18") == null ? "" : (String)map.get("shinsei18");
		kd.shinsei19 = map.get("shinsei19") == null ? "" : (String)map.get("shinsei19");
		kd.shinsei20 = map.get("shinsei20") == null ? "" : (String)map.get("shinsei20");
		kd.shinsei21 = map.get("shinsei21") == null ? "" : (String)map.get("shinsei21");
		kd.shinsei22 = map.get("shinsei22") == null ? "" : (String)map.get("shinsei22");
		kd.shinsei23 = map.get("shinsei23") == null ? "" : (String)map.get("shinsei23");
		kd.shinsei24 = map.get("shinsei24") == null ? "" : (String)map.get("shinsei24");
		kd.shinsei25 = map.get("shinsei25") == null ? "" : (String)map.get("shinsei25");
		kd.shinsei26 = map.get("shinsei26") == null ? "" : (String)map.get("shinsei26");
		kd.shinsei27 = map.get("shinsei27") == null ? "" : (String)map.get("shinsei27");
		kd.shinsei28 = map.get("shinsei28") == null ? "" : (String)map.get("shinsei28");
		kd.shinsei29 = map.get("shinsei29") == null ? "" : (String)map.get("shinsei29");
		kd.shinsei30 = map.get("shinsei30") == null ? "" : (String)map.get("shinsei30");
		
		String sqlupd = "";
		List<Object> paramsUpd = new ArrayList<Object>();
		paramsUpd.add(kd.shinsei01);
		paramsUpd.add(kd.shinsei02);
		paramsUpd.add(kd.shinsei03);
		paramsUpd.add(kd.shinsei04);
		paramsUpd.add(kd.shinsei05);
		paramsUpd.add(kd.shinsei06);
		paramsUpd.add(kd.shinsei07);
		paramsUpd.add(kd.shinsei08);
		paramsUpd.add(kd.shinsei09);
		paramsUpd.add(kd.shinsei10);
		paramsUpd.add(kd.shinsei11);
		paramsUpd.add(kd.shinsei12);
		paramsUpd.add(kd.shinsei13);
		paramsUpd.add(kd.shinsei14);
		paramsUpd.add(kd.shinsei15);
		paramsUpd.add(kd.shinsei16);
		paramsUpd.add(kd.shinsei17);
		paramsUpd.add(kd.shinsei18);
		paramsUpd.add(kd.shinsei19);
		paramsUpd.add(kd.shinsei20);
		paramsUpd.add(kd.shinsei21);
		paramsUpd.add(kd.shinsei22);
		paramsUpd.add(kd.shinsei23);
		paramsUpd.add(kd.shinsei24);
		paramsUpd.add(kd.shinsei25);
		paramsUpd.add(kd.shinsei26);
		paramsUpd.add(kd.shinsei27);
		paramsUpd.add(kd.shinsei28);
		paramsUpd.add(kd.shinsei29);
		paramsUpd.add(kd.shinsei30);
		paramsUpd.add(denpyouId);
		//update
		sqlupd = ""
				+ "UPDATE kani_todoke_ichiran SET "
			    + " shinsei01 = ? ,"
			    + " shinsei02 = ? ,"
			    + " shinsei03 = ? ,"
			    + " shinsei04 = ? ,"
			    + " shinsei05 = ? ,"
			    + " shinsei06 = ? ,"
			    + " shinsei07 = ? ,"
			    + " shinsei08 = ? ,"
			    + " shinsei09 = ? ,"
			    + " shinsei10 = ? ,"
			    + " shinsei11 = ? ,"
			    + " shinsei12 = ? ,"
			    + " shinsei13 = ? ,"
			    + " shinsei14 = ? ,"
			    + " shinsei15 = ? ,"
			    + " shinsei16 = ? ,"
			    + " shinsei17 = ? ,"
			    + " shinsei18 = ? ,"
			    + " shinsei19 = ? ,"
			    + " shinsei20 = ? ,"
			    + " shinsei21 = ? ,"
			    + " shinsei22 = ? ,"
			    + " shinsei23 = ? ,"
			    + " shinsei24 = ? ,"
			    + " shinsei25 = ? ,"
			    + " shinsei26 = ? ,"
			    + " shinsei27 = ? ,"
			    + " shinsei28 = ? ,"
			    + " shinsei29 = ? ,"
			    + " shinsei30 = ? "
			    + " WHERE denpyou_id = ? ";
		connection.update(sqlupd, paramsUpd.toArray());
	}
	
	/**
	 * ユーザー定義届書明細一覧テーブルの指定した伝票IDのデータを更新する。(共通表示部分)
	 * @param denpyouId 伝票番号
	 */
	public void updateKaniTodokeMeisaiIchiran(String denpyouId){
		String sql = "";
		
		KaniTodokeMeisaiIchiranData kmd = new KaniTodokeMeisaiIchiranData();

		sql = "SELECT * FROM denpyou WHERE denpyou_id = ?";
		GMap denpyouMap = connection.find(sql, denpyouId);
		if(denpyouMap == null)return; //伝票個別テーブルにデータが存在してdenpyouにデータが存在しない場合があるので念のため確認
		
		sql = "";
		List<GMap> mapList = this.getKanitodokeMeisaiIchiranData(denpyouId);
		if(mapList == null)return; //表示用のデータが存在しない場合があるので念のため確認
		
		int i = 0 ;
		for(GMap map : mapList){
			i++;
			//各種表示用データを登録
			kmd.meisai01 = map.get("meisai01") == null ? "" : (String)map.get("meisai01");
			kmd.meisai02 = map.get("meisai02") == null ? "" : (String)map.get("meisai02");
			kmd.meisai03 = map.get("meisai03") == null ? "" : (String)map.get("meisai03");
			kmd.meisai04 = map.get("meisai04") == null ? "" : (String)map.get("meisai04");
			kmd.meisai05 = map.get("meisai05") == null ? "" : (String)map.get("meisai05");
			kmd.meisai06 = map.get("meisai06") == null ? "" : (String)map.get("meisai06");
			kmd.meisai07 = map.get("meisai07") == null ? "" : (String)map.get("meisai07");
			kmd.meisai08 = map.get("meisai08") == null ? "" : (String)map.get("meisai08");
			kmd.meisai09 = map.get("meisai09") == null ? "" : (String)map.get("meisai09");
			kmd.meisai10 = map.get("meisai10") == null ? "" : (String)map.get("meisai10");
			kmd.meisai11 = map.get("meisai11") == null ? "" : (String)map.get("meisai11");
			kmd.meisai12 = map.get("meisai12") == null ? "" : (String)map.get("meisai12");
			kmd.meisai13 = map.get("meisai13") == null ? "" : (String)map.get("meisai13");
			kmd.meisai14 = map.get("meisai14") == null ? "" : (String)map.get("meisai14");
			kmd.meisai15 = map.get("meisai15") == null ? "" : (String)map.get("meisai15");
			kmd.meisai16 = map.get("meisai16") == null ? "" : (String)map.get("meisai16");
			kmd.meisai17 = map.get("meisai17") == null ? "" : (String)map.get("meisai17");
			kmd.meisai18 = map.get("meisai18") == null ? "" : (String)map.get("meisai18");
			kmd.meisai19 = map.get("meisai19") == null ? "" : (String)map.get("meisai19");
			kmd.meisai20 = map.get("meisai20") == null ? "" : (String)map.get("meisai20");
			kmd.meisai21 = map.get("meisai21") == null ? "" : (String)map.get("meisai21");
			kmd.meisai22 = map.get("meisai22") == null ? "" : (String)map.get("meisai22");
			kmd.meisai23 = map.get("meisai23") == null ? "" : (String)map.get("meisai23");
			kmd.meisai24 = map.get("meisai24") == null ? "" : (String)map.get("meisai24");
			kmd.meisai25 = map.get("meisai25") == null ? "" : (String)map.get("meisai25");
			kmd.meisai26 = map.get("meisai26") == null ? "" : (String)map.get("meisai26");
			kmd.meisai27 = map.get("meisai27") == null ? "" : (String)map.get("meisai27");
			kmd.meisai28 = map.get("meisai28") == null ? "" : (String)map.get("meisai28");
			kmd.meisai29 = map.get("meisai29") == null ? "" : (String)map.get("meisai29");
			kmd.meisai30 = map.get("meisai30") == null ? "" : (String)map.get("meisai30");
			
			String sqlupd = "";
			List<Object> paramsUpd = new ArrayList<Object>();
			paramsUpd.add(kmd.meisai01);
			paramsUpd.add(kmd.meisai02);
			paramsUpd.add(kmd.meisai03);
			paramsUpd.add(kmd.meisai04);
			paramsUpd.add(kmd.meisai05);
			paramsUpd.add(kmd.meisai06);
			paramsUpd.add(kmd.meisai07);
			paramsUpd.add(kmd.meisai08);
			paramsUpd.add(kmd.meisai09);
			paramsUpd.add(kmd.meisai10);
			paramsUpd.add(kmd.meisai11);
			paramsUpd.add(kmd.meisai12);
			paramsUpd.add(kmd.meisai13);
			paramsUpd.add(kmd.meisai14);
			paramsUpd.add(kmd.meisai15);
			paramsUpd.add(kmd.meisai16);
			paramsUpd.add(kmd.meisai17);
			paramsUpd.add(kmd.meisai18);
			paramsUpd.add(kmd.meisai19);
			paramsUpd.add(kmd.meisai20);
			paramsUpd.add(kmd.meisai21);
			paramsUpd.add(kmd.meisai22);
			paramsUpd.add(kmd.meisai23);
			paramsUpd.add(kmd.meisai24);
			paramsUpd.add(kmd.meisai25);
			paramsUpd.add(kmd.meisai26);
			paramsUpd.add(kmd.meisai27);
			paramsUpd.add(kmd.meisai28);
			paramsUpd.add(kmd.meisai29);
			paramsUpd.add(kmd.meisai30);
			paramsUpd.add(denpyouId);
			paramsUpd.add(i);
			//update
			sqlupd = ""
					+ "UPDATE kani_todoke_meisai_ichiran SET "
				    + " meisai01 = ? ,"
				    + " meisai02 = ? ,"
				    + " meisai03 = ? ,"
				    + " meisai04 = ? ,"
				    + " meisai05 = ? ,"
				    + " meisai06 = ? ,"
				    + " meisai07 = ? ,"
				    + " meisai08 = ? ,"
				    + " meisai09 = ? ,"
				    + " meisai10 = ? ,"
				    + " meisai11 = ? ,"
				    + " meisai12 = ? ,"
				    + " meisai13 = ? ,"
				    + " meisai14 = ? ,"
				    + " meisai15 = ? ,"
				    + " meisai16 = ? ,"
				    + " meisai17 = ? ,"
				    + " meisai18 = ? ,"
				    + " meisai19 = ? ,"
				    + " meisai20 = ? ,"
				    + " meisai21 = ? ,"
				    + " meisai22 = ? ,"
				    + " meisai23 = ? ,"
				    + " meisai24 = ? ,"
				    + " meisai25 = ? ,"
				    + " meisai26 = ? ,"
				    + " meisai27 = ? ,"
				    + " meisai28 = ? ,"
				    + " meisai29 = ? ,"
				    + " meisai30 = ? "
				    + " WHERE denpyou_id = ? AND denpyou_edano = ?";
			connection.update(sqlupd, paramsUpd.toArray());
		}
	}

	/**
	 * 伝票内容を取得する
	 * @param denpyouId 伝票ID
	 * @return 伝票内容マップリスト
	 */
	protected GMap getKanitodokeIchiranDat(String denpyouId) {
		KaniTodokeCategoryLogic kaniTodokeLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);
		// 伝票区分
		String denpyouKbn = denpyouId.substring(7, 11);
		
		// 最新バージョン
		int lastVersion = kaniTodokeLogic.findMaxVersion(denpyouKbn);
		List<GMap> layoutList = kaniTodokeLogic.loadLayout("shinsei", denpyouKbn, String.valueOf(lastVersion));
		List<GMap> dataList = kaniTodokeLogic.loadSanshouData("shinsei", denpyouKbn, String.valueOf(lastVersion), denpyouId, false);
		
		GMap ret = new GMap();
		for(GMap data: dataList){
			Object itemName = data.get("item_name");
			for(GMap layout : layoutList){
				boolean isContinue = false;
				if(itemName.equals(layout.get("item_name"))){
					String val = (String)data.get("value");
					if(val != null){
						ret.put(itemName.toString(), val.toString());
					}
					isContinue = true;
					continue;
				}
				if (isContinue)
				{
					continue;
				}
			}
		}
		
		return ret;
	}

	/**
	 * 明細データを取得する
	 * @param denpyouId 伝票ID
	 * @return 伝票内容マップリスト
	 */
	protected List<GMap> getKanitodokeMeisaiIchiranData(String denpyouId) {
		KaniTodokeCategoryLogic kaniTodokeLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);
		// 伝票区分
		String denpyouKbn = denpyouId.substring(7, 11);
		
		// 最新バージョン
		int lastVersion = kaniTodokeLogic.findMaxVersion(denpyouKbn);
		List<GMap> layoutList = kaniTodokeLogic.loadLayout("meisai", denpyouKbn, String.valueOf(lastVersion));
		List<GMap> dataList = kaniTodokeLogic.loadSanshouData("meisai", denpyouKbn, String.valueOf(lastVersion), denpyouId, false);
		
		// 明細
		int maxDenpyouEdano = countMeisai(denpyouId);
		
		List<GMap> retList = new ArrayList<GMap>();
		for(int i=0; i < maxDenpyouEdano ; i++){
			GMap ret = new GMap();
			int genzaiEdano = i+1;
			
			for(GMap data: dataList){
				int denpyouEdano = Integer.parseInt(data.get("denpyou_edano").toString());
				if(denpyouEdano == genzaiEdano){
					Object itemName = data.get("item_name");
					
					for(GMap layout : layoutList){
						boolean isContinue = false;
						if(itemName.equals(layout.get("item_name"))){
							String val = (String)data.get("value");
							if(val != null){
								ret.put(itemName.toString(), val.toString());
							}
							isContinue = true;
							continue;
						}
						if (isContinue)
						{
							continue;
						}
					}
				}
			}
			retList.add(i, ret);
		}
		
		return retList;
	}
}
