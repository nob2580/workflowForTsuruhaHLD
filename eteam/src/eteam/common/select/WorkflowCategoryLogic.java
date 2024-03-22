package eteam.common.select;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;

/**
 * ワークフローカテゴリー内のSelect文を集約したLogic
 */
public class WorkflowCategoryLogic extends EteamAbstractLogic {

	/** カスタマイズ用Strategyクラス */
	@Getter @Setter
	protected AbstractWorkflowCategoryLogicCustomStrategy customStrategy;
	
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */
/* ・INSERT文・UPDATE文は個別Logicに記載してください。  */
/* ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ * ～ */

/*  伝票(denpyou) */
	
	/**
	 * 「伝票」テーブルロック、伝票IDのレコードをロックする。
	 * @param  denpyou_id  String 伝票ID
	 * @return 検索結果
	 */
	public GMap selectDenpyouForUpdate(String denpyou_id) {
		return connection.find("SELECT * FROM denpyou WHERE denpyou_id = ? FOR UPDATE", denpyou_id);
	}
	
	/**
	 * 伝票IDをキーに「伝票」からデータを取得する。データがなければnull。
	 * @param  denpyou_id  String 伝票ID
	 * @return 検索結果 1件データ
	 */
	public GMap selectDenpyou(String denpyou_id) {
		String sql;
		if (Open21Env.getVersion() == Version.DE3 ) {
			sql = "SELECT denpyou.*, COALESCE(kani_todoke.version,0) AS version , ARRAY_TO_STRING(ARRAY(SELECT DISTINCT dcno FROM shiwake_de3 s WHERE s.denpyou_id = denpyou.denpyou_id ORDER BY dcno), ' ') AS dcno FROM denpyou LEFT OUTER JOIN (SELECT DISTINCT denpyou_id, version FROM kani_todoke) kani_todoke ON denpyou.denpyou_id = kani_todoke.denpyou_id WHERE denpyou.denpyou_id = ?;";
		} else {
			sql = "SELECT denpyou.*, COALESCE(kani_todoke.version,0) AS version , ARRAY_TO_STRING(ARRAY(SELECT DISTINCT dcno FROM shiwake_sias s WHERE s.denpyou_id = denpyou.denpyou_id ORDER BY dcno), ' ') AS dcno FROM denpyou LEFT OUTER JOIN (SELECT DISTINCT denpyou_id, version FROM kani_todoke) kani_todoke ON denpyou.denpyou_id = kani_todoke.denpyou_id WHERE denpyou.denpyou_id = ?;";
		}
		
		return connection.find(sql, denpyou_id);
	}
	
/* 伝票種別一覧(denpyou_shubetsu_ichiran) */
	
	/**
	 * 伝票区分をキーに「伝票種別一覧」からデータを取得する。データがなければnull。
	 * @param  denpyou_kbn  String 伝票区分
	 * @return 検索結果 1件データ
	 */
    @Deprecated
	public GMap selectDenpyouShubetu(String denpyou_kbn) {
		final String sql = "SELECT denpyou_kbn, denpyou_shubetsu, denpyou_karibarai_nashi_shubetsu, denpyou_print_shubetsu, denpyou_print_karibarai_nashi_shubetsu, denpyou_shubetsu_url, denpyou_print_flg FROM denpyou_shubetsu_ichiran WHERE denpyou_kbn = ?;";
		return connection.find(sql, denpyou_kbn);
	}
	
/* 承認ルート(shounin_route) */
	
	/**
	 * 「承認ルート」テーブルロック、伝票IDのレコードをロックする。
	 * @param  denpyouId 伝票ID
	 * @return 検索結果
	 */
    @Deprecated
	public GMap selectShouninRouteForUpdate(String denpyouId) {
		final String sql = "SELECT denpyou_id, edano, user_id, user_full_name, bumon_cd, bumon_full_name, bumon_role_id, bumon_role_name, gyoumu_role_id, gyoumu_role_name, genzai_flg, saishu_shounin_flg FROM shounin_route WHERE denpyou_id = ? FOR UPDATE";
		return connection.find(sql, denpyouId);
	}
	
	/**
	 * 伝票IDをキーに「承認ルート」からデータを取得する。
	 * @param  denpyouId 伝票ID
	 * @return 検索結果
	 */
	public List<GMap> selectShouninRoute(String denpyouId) {
		//この構造で返す
		//承認ルート			ret (denpyou_id,edano)
		//└承認ルート合議親	ret.gougiOya (denpyou_id,edano,gougi_edano)
		//　└承認ルート合議子	ret.gougiOya.gougiKo (denpyou_id,edano,gougi_edaedano)
		final String sql1 = "SELECT "
						+ "   r.* "
						+ "  ,k.shounin_mongon "
						+ "  ,ARRAY_TO_STRING(ARRAY(SELECT gougi_edano FROM shounin_route_gougi_oya goya WHERE (goya.denpyou_id,goya.edano)=(r.denpyou_id,r.edano)),',') AS gougi_edano_array "
						+ "  ,(SELECT COUNT(*) FROM shounin_joukyou j WHERE (j.denpyou_id,j.edano)=(r.denpyou_id,r.joukyou_edano)) > 0 AS done "
						+ "FROM shounin_route r "
						+ "LEFT OUTER JOIN shounin_shori_kengen k USING(shounin_shori_kengen_no) "
						+ "WHERE "
						+ "  r.denpyou_id = ? "
						+ "ORDER BY "
						+ "  r.edano ASC";
		final String sql2 = "SELECT * FROM shounin_route_gougi_oya WHERE (denpyou_id,edano,gougi_edano)=(?,?,?)";
		final String sql3 = "SELECT "
						+ "  r.* "
						+ "  ,k.shounin_mongon "
						+ "  ,(SELECT COUNT(*) FROM shounin_joukyou j WHERE (j.denpyou_id,j.edano)=(r.denpyou_id,r.joukyou_edano)) > 0 AS done "
						+ "FROM shounin_route_gougi_ko r "
						+ "LEFT OUTER JOIN shounin_shori_kengen k USING(shounin_shori_kengen_no) "
						+ "WHERE "
						+ "  (denpyou_id,edano,gougi_edano)=(?,?,?) "
						+ "ORDER BY "
						+ "  gougi_edaedano ASC ";
		
		//承認ルート取得
		List<GMap> routeList = connection.load(sql1, denpyouId);
		
		//承認ルート単位
		for(int i = 0; i < routeList.size(); i++){
			GMap route = routeList.get(i);
			route.put("koushin_time", null); //便宜上邪魔だったんで
			int edano = (int)route.get("edano");
			
			//承認ルートに合議親リスト(空)を付けておく
			List<GMap> gougiOyaList = new ArrayList<>();
			route.put("gougiOya", gougiOyaList);

			//合議でない
			if(isEmpty(route.get("gougi_edano_array"))){
				route.put("isGougi", Boolean.FALSE);
			//合議である
			}else{
				route.put("isGougi", Boolean.TRUE);
				String[] gougiEdanoArray = ((String)route.get("gougi_edano_array")).split(",");
				//合議親単位
				for(String gougiEdano : gougiEdanoArray){
					//合議親レコードを追加
					GMap gougiOya = connection.find(sql2, denpyouId, edano, Integer.parseInt(gougiEdano));
					gougiOyaList.add(gougiOya);
					//合議子コードを追加
					List<GMap> gougiKoList = connection.load(sql3, denpyouId, edano, Integer.parseInt(gougiEdano));
					gougiOya.put("gougiKo", gougiKoList);
				}
			}
			//合議/合議以外というブロック単位の先頭識別
			route.put("groupHead", i == 0 || ! route.get("isGougi").equals(routeList.get(i-1).get("isGougi")));
		}
		return routeList;
	}
	/**
	 * 合議子を返す
	 * @param routeList 承認ルートリスt
	 * @param edano 枝番号
	 * @param gougiEdano 合議枝番号
	 * @return 承認ルート合議子
	 */
	public List<GMap> getGougiKoList(List<GMap> routeList, int edano, int gougiEdano){
		GMap route = routeList.get(edano-1);
		List<GMap>		gougiOyaList = (List<GMap>)route.get("gougiOya");
		GMap gougiOya = gougiOyaList.get(gougiEdano-1);
		List<GMap>		gougiKoList = (List<GMap>)gougiOya.get("gougiKo");
		return gougiKoList;
	}
	/**
	 * 合議子を返す
	 * @param routeList 承認ルートリスt
	 * @param edano 枝番号
	 * @param gougiEdano 合議枝番号
	 * @param gougiEdaedano 合議枝枝番号
	 * @return 承認ルート合議子
	 */
	public GMap getGougiKo(List<GMap> routeList, int edano, int gougiEdano, int gougiEdaedano){
		List<GMap>		gougiKoList = getGougiKoList(routeList, edano, gougiEdano);
		GMap gougiKo = gougiKoList.get(gougiEdaedano-1);
		return gougiKo;
	}
	/**
	 * 処理対象の承認ルートを取得
	 * @param denpyouId 伝票ID
	 * @param user ユーザー
	 * @return 承認ルート なければnull
	 */
	public GMap findProcRoute(String denpyouId, User user) {
		List<GMap> routeList = selectShouninRoute(denpyouId);
		return findProcRoute(routeList, user);
	}
	/**
	 * 処理対象の承認ルートを取得
	 * @param  routeList 承認ルート
	 * @param  user ユーザー
	 * @return 承認ルート なければnull
	 */
	public GMap findProcRoute(List<GMap> routeList, User user) {
		SystemKanriCategoryLogic sysLg = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		
		//承認ルート + 承認ルート合議親 + 承認ルート合議子をなめてgenzai_flg=1がログインユーザーならそれ
		for(int i = 0; i < routeList.size(); i++){
			GMap route = routeList.get(i);
			if(route.get("genzai_flg").equals("1")){
				//普通の承認ルート
				if(!(boolean)route.get("isGougi")){
					if(user.getGyoumuRoleId()==null && user.getSeigyoUserId().equals(route.get("user_id"))){
						return route;
					}else if(user.getGyoumuRoleId()!=null && user.getGyoumuRoleId().equals(route.get("gyoumu_role_id"))){
						return route;
					}
				//合議の承認ルート
				}else{
					@SuppressWarnings("unchecked")
					List<GMap> gougiOyaList = (List<GMap>)route.get("gougiOya"); 
					for(int j = 0; j < gougiOyaList.size(); j++){
						GMap gougiOya = gougiOyaList.get(j);
						@SuppressWarnings("unchecked")
						List<GMap> gougiKoList = (List<GMap>)gougiOya.get("gougiKo");
						for(int z = 0; z < gougiKoList.size(); z++){
							GMap gougiKo = gougiKoList.get(z);
							if(gougiKo.get("gougi_genzai_flg").equals("1") && user.getGyoumuRoleId()==null && user.getSeigyoUserId().equals(gougiKo.get("user_id"))){
								return gougiKo;
							}
						}
					}
				}
			}
		}
		
		//承認ルート + 承認ルート合議親 + 承認ルート合議子をなめてgenzai_flg=1の位置が承認権=NOになっていて、その後にログインユーザーがいたらそれ・・・承認権NOを飛ばす
		for(int i = 0; i < routeList.size(); i++){
			GMap route = routeList.get(i);
			if(route.get("genzai_flg").equals("1")){
				//普通の承認ルート
				if(!(boolean)route.get("isGougi")){
					if(route.get("shounin_shori_kengen_no") != null && route.get("shounin_ken_flg").equals("0")){
						//i～iiが同じグループになるように
						int ii = i;
						for(;ii < routeList.size(); ii++){
							if(ii == routeList.size() - 1 || (boolean)routeList.get(ii+1).get("groupHead")){
								break;
							}
						}
						//i～iiの未処理の承認ルートを追加
						for(int x = i; x <= ii; x++){
							GMap tmpRoute = routeList.get(x);
							if(user.getGyoumuRoleId()==null && user.getSeigyoUserId().equals(tmpRoute.get("user_id"))){
								return tmpRoute;
							}else if(user.getGyoumuRoleId()!=null && user.getGyoumuRoleId().equals(tmpRoute.get("gyoumu_role_id"))){
								return tmpRoute;
							}
						}
					}
				//合議の承認ルート
				}else{
					@SuppressWarnings("unchecked")
					List<GMap> gougiOyaList = (List<GMap>)route.get("gougiOya"); 
					for(int j = 0; j < gougiOyaList.size(); j++){
						GMap gougiOya = gougiOyaList.get(j);
						@SuppressWarnings("unchecked")
						List<GMap> gougiKoList = (List<GMap>)gougiOya.get("gougiKo");
						for(int z = 0; z < gougiKoList.size(); z++){
							GMap gougiKo = gougiKoList.get(z);
							if(gougiKo.get("gougi_genzai_flg").equals("1")){
								GMap shoriKengen = findShouninShoriKengen((long)gougiKo.get("shounin_shori_kengen_no"));
								if(shoriKengen.get("shounin_ken_flg").equals("0")){
									int zz = z;
									for(;zz < gougiKoList.size(); zz++){
										GMap tmpRoute = gougiKoList.get(zz);
										if(user.getGyoumuRoleId()==null && user.getSeigyoUserId().equals(tmpRoute.get("user_id"))){
											return tmpRoute;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		//承認ルート + 承認ルート合議親 + 承認ルート合議子をなめてgenzai_flg=1の位置より前に、ログインユーザーがいて未処理ならそれ・・・後伺い等
		int iGenzai = routeList.size() - 1;//genzai_flgがどこにもなければ便宜上MAXにしておく
		for(int i = 0; i < routeList.size(); i++){
			if (routeList.get(i).get("genzai_flg").equals("1"))
			{
				iGenzai = i;
			}
		}
		for(int i = 0; i <= iGenzai; i++){
			GMap route = routeList.get(i);
			
			//普通の承認ルート
			if(!(boolean)route.get("isGougi")){
				if(route.get("shounin_shori_kengen_no") != null){
					if(
						(user.getGyoumuRoleId() == null && user.getSeigyoUserId().equals(route.get("user_id"))) ||
						(user.getGyoumuRoleId() != null && user.getGyoumuRoleId().equals(route.get("gyoumu_role_id")))
					){
						if(route.get("joukyou_edano") == null){
							route.put("isAtoukagai", Boolean.TRUE);
							return route;
						}
					}
				}
			//合議の承認ルート
			}else{
				@SuppressWarnings("unchecked")
				List<GMap> gougiOyaList = (List<GMap>)route.get("gougiOya");
				for(GMap gougiOya : gougiOyaList){
					@SuppressWarnings("unchecked")
					List<GMap> gougiKoList = (List<GMap>)gougiOya.get("gougiKo");
					int jGenzai = gougiKoList.size() - 1;//genzai_flgがどこにもなければ便宜上MAXにしておく
					for(int j = 0; j < gougiKoList.size(); j++){
						if (gougiKoList.get(j).get("gougi_genzai_flg").equals("1"))
						{
							jGenzai = j;
						}
					}
					for(int j = 0; j <= jGenzai; j++){
						GMap gougiKo = gougiKoList.get(j);
						if(
							(user.getGyoumuRoleId() == null && user.getSeigyoUserId().equals(gougiKo.get("user_id"))) ||
							(user.getGyoumuRoleId() != null && user.getGyoumuRoleId().equals(gougiKo.get("gyoumu_role_id")))
						){
							if(gougiKo.get("joukyou_edano") == null){
								gougiKo.put("isAtoukagai", Boolean.TRUE);
								return gougiKo;
							}
						}
					}
				}
			}
		}
		
		//承認ルート + 承認ルート合議親 + 承認ルート合議子をなめてgenzai_flg=1の位置より後に、ログインユーザーがいて未処理かつ承認権ありなら、そいつは上位承認者
		//上位承認者は合議飛ばしと合議内はなくて、一般承認者内だけ
		if(sysLg.judgeKinouSeigyoON(KINOU_SEIGYO_CD.JOUI_SENKETSU_SHOUNIN)){
			//現在フラグ位置確認
			iGenzai = routeList.size();//genzai_flgがどこにもなければ便宜上MAX+1にしておく
			for(int i = 0; i < routeList.size(); i++){
				if (routeList.get(i).get("genzai_flg").equals("1"))
				{
					iGenzai = i;
				}
			}
			//現在フラグ位置が一般承認者なら、上位に飛ばせる可能性あり
			if(iGenzai < routeList.size() && !(boolean)routeList.get(iGenzai).get("isGougi")){
				for(int i = iGenzai + 1; i <= routeList.size() - 1; i++){
					GMap route = routeList.get(i);
					//合議になったんで終わり(合議は飛ばせない)
					if((boolean)route.get("isGougi")){
						break;
					}
					//普通の承認ルートで承認権ありOR業務ロールならそいつは上位承認者
					if(route.get("saishu_shounin_flg").equals("1") || route.get("shounin_ken_flg").equals("1")){
						if(
							(user.getGyoumuRoleId() == null && user.getSeigyoUserId().equals(route.get("user_id"))) ||
							(user.getGyoumuRoleId() != null && user.getGyoumuRoleId().equals(route.get("gyoumu_role_id")))
						){
							if(route.get("joukyou_edano") == null){
								route.put("isJoui", Boolean.TRUE);
								return route;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * ユーザーが現在承認者よりも上位者であるか調べる
	 * @param routeList 承認ルートリスト
	 * @param user ユーザー
	 * @return 上位者である
	 */
	public GMap findJouiRoute(List<GMap> routeList, User user) {
		//現在承認者の上位承認者として変更可能
		for(GMap tmpRoute : routeList){
			if(!(boolean)tmpRoute.get("isGougi")){
				if(tmpRoute.get("genzai_flg").equals("1")){
					GMap tmp = findJouiRoute(tmpRoute, routeList, user);
					if (tmp != null)
					{
						return tmp;
					}
				}
			}else{
				@SuppressWarnings("unchecked")
				List<GMap> gougiOyaList = (List<GMap>)tmpRoute.get("gougiOya");
				for(GMap tmpGougiOya : gougiOyaList){
					@SuppressWarnings("unchecked")
					List<GMap> gougiKoList = (List<GMap>)tmpGougiOya.get("gougiKo");
					for(GMap tmpGougiKo : gougiKoList){
						if(tmpGougiKo.get("gougi_genzai_flg").equals("1")){
							GMap tmp = findJouiRoute(tmpGougiKo, routeList, user);
							if (tmp != null)
							{
								return tmp;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * ユーザーが現在承認者よりも上位者であるか調べる
	 * @param kijunRoute 基準の承認ルート or 承認ルート合議子
	 * @param routeList 承認ルートリスト
	 * @param user ユーザー
	 * @return 上位者である
	 */
	public GMap findJouiRoute(GMap kijunRoute, List<GMap> routeList, User user) {
		int kijunEdano = (int)kijunRoute.get("edano");

		//基準が承認ルート
		if(kijunRoute.get("gougi_edano") == null){
			for(GMap tmpRoute : routeList){
				if(!(boolean)tmpRoute.get("isGougi")){
					if(
						(user.getGyoumuRoleId() == null && user.getSeigyoUserId().equals(tmpRoute.get("user_id"))) ||
						(user.getGyoumuRoleId() != null && user.getGyoumuRoleId().equals(tmpRoute.get("gyoumu_role_id")))
					){
						//基準より後の承認ルートに該当ユーザーがいたので上位者である
						if(kijunEdano < (int)tmpRoute.get("edano")){
							return tmpRoute;
						}
					}
				}else{
					@SuppressWarnings("unchecked")
					List<GMap> gougiOyaList = (List<GMap>)tmpRoute.get("gougiOya");
					for(GMap tmpGougiOya : gougiOyaList){
						@SuppressWarnings("unchecked")
						List<GMap> gougiKoList = (List<GMap>)tmpGougiOya.get("gougiKo");
						for(GMap tmpGougiKo : gougiKoList){
							if(
								(user.getGyoumuRoleId() == null && user.getSeigyoUserId().equals(tmpGougiKo.get("user_id")))
							){
								//基準より後の承認ルート合議子に該当ユーザーがいたので上位者である
								if(kijunEdano < (int)tmpRoute.get("edano")){
									return tmpGougiKo;
								}
							}
						}
					}
				}
			}
		//基準が承認ルート合議子
		}else{
			int kijunGougiEdano = (int)kijunRoute.get("gougi_edano");
			int kijunGougiEdaedano = (int)kijunRoute.get("gougi_edaedano");
			
			for(GMap tmpRoute : routeList){
				if(!(boolean)tmpRoute.get("isGougi")){
					if(
						(user.getGyoumuRoleId() == null && user.getSeigyoUserId().equals(tmpRoute.get("user_id"))) ||
						(user.getGyoumuRoleId() != null && user.getGyoumuRoleId().equals(tmpRoute.get("gyoumu_role_id")))
					){
						//基準より後の承認ルートに該当ユーザーがいたので上位者である
						if(kijunEdano < (int)tmpRoute.get("edano")){
							return tmpRoute;
						}
					}
				}else{
					@SuppressWarnings("unchecked")
					List<GMap> gougiOyaList = (List<GMap>)tmpRoute.get("gougiOya");
					for(GMap tmpGougiOya : gougiOyaList){
						@SuppressWarnings("unchecked")
						List<GMap> gougiKoList = (List<GMap>)tmpGougiOya.get("gougiKo");
						for(GMap tmpGougiKo : gougiKoList){
							if(
								(user.getGyoumuRoleId() == null && user.getSeigyoUserId().equals(tmpGougiKo.get("user_id")))
							){
								//基準より後の承認ルート合議子に該当ユーザーがいたので上位者である
								if(kijunEdano < (int)tmpRoute.get("edano")){
									return tmpGougiKo;
								}
								if(kijunEdano == (int)tmpRoute.get("edano")){
									if(kijunGougiEdano == (int)tmpGougiKo.get("gougi_edano") && kijunGougiEdaedano < (int)tmpGougiKo.get("gougi_edaedano")){
										//合議枝々番が基準より後にあっても同一部門・役職の可能性があるためチェック
										String checkBumonCd = tmpGougiKo.get("bumon_cd").toString();
										String checkBumonRoleId = tmpGougiKo.get("bumon_role_id").toString();
										String genzaiBumonCd = kijunRoute.get("bumon_cd").toString();
										String genzaiBumonRoleId = kijunRoute.get("bumon_role_id").toString();
										
										boolean isSameRole = (checkBumonCd.equals(genzaiBumonCd) && checkBumonRoleId.equals(genzaiBumonRoleId));
										//同一部門・役職でなければ上位者である
										if(!isSameRole){
											return tmpGougiKo;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 伝票IDをキーに「承認ルート」からデータを取得する。(合議用データも含め取得)
	 * @param  denpyouId 伝票ID
	 * @return 検索結果
	 */
	public List<GMap> selectShouninRouteGougiList(String denpyouId) {
		final String sql = "select s.denpyou_id, "
				+  "       s.edano, "
				+  "       COALESCE(sk.user_id,s.user_id) as user_id, "
				+  "       COALESCE(sk.user_full_name,s.user_full_name) as user_full_name, "
				+  "       COALESCE(sk.bumon_cd, s.bumon_cd) as bumon_cd, "
				+  "       COALESCE(sk.bumon_full_name, s.bumon_full_name) as bumon_full_name, "
				+  "       COALESCE(sk.bumon_role_id,s.bumon_role_id) as bumon_role_id, "
				+  "       COALESCE(sk.bumon_role_name,s.bumon_role_name) as bumon_role_name, "
				+  "       s.gyoumu_role_id, "
				+  "       s.gyoumu_role_name, "
				+  "       so.gougi_pattern_no, "
				+  "       so.gougi_name, "
				+  "       so.gougi_edano, "
				+  "       COALESCE(sk.shounin_shori_kengen_no, s.shounin_shori_kengen_no) as shounin_shori_kengen_no,"
				+  "       sk.shounin_ninzuu_cd, "
				+  "       sk.shounin_ninzuu_hiritsu, "
				+  "       sk.gougi_genzai_flg, "
				+  "       sk.gouginai_group, "
				+  "       sk.gougi_edaedano, "
				+  "       sk.joukyou_edano as gougi_joukyou_edano, "
				+  "       s.shounin_shori_kengen_name, "
				+  "       s.genzai_flg, "
				+  "       s.saishu_shounin_flg, "
				+  "       s.joukyou_edano, "
				+  "       s.touroku_time "
				+  " from shounin_route s "
				+  " left outer join shounin_route_gougi_oya so "
				+  " on s.denpyou_id = so.denpyou_id and s.edano = so.edano "
				+  " left outer join shounin_route_gougi_ko sk "
				+  " on so.denpyou_id = sk.denpyou_id and so.edano = sk.edano and so.gougi_edano = sk.gougi_edano "
				+  " left outer join user_info ui "
				+  " on sk.user_id = ui.user_id "
				+  " where s.denpyou_id = ? "
				+  " ORDER BY s.edano, sk.gougi_edano, sk.gougi_edaedano ASC";
		return connection.load(sql, denpyouId);
	}
	
	/**
	 * 伝票IDをキーに「承認ルート合議親」からデータを取得する。
	 * @param  denpyouId 伝票ID
	 * @return 検索結果
	 */
    @Deprecated
	public List<GMap> selectShouninRouteGougiOya(String denpyouId) {
		final String sql = "SELECT * FROM shounin_route_gougi_oya WHERE denpyou_id = ? ORDER BY edano,gougi_edano ";
		return connection.load(sql, denpyouId);
	}

	/**
	 * 伝票IDをキーに「承認ルート合議子」からデータを取得する。
	 * @param  denpyouId 伝票ID
	 * @return 検索結果
	 */
    @Deprecated
	public List<GMap> selectShouninRouteGougiKo(String denpyouId) {
		final String sql = "SELECT * FROM shounin_route_gougi_ko WHERE denpyou_id = ? ORDER BY edano,gougi_edano,gougi_edaedano ";
		return connection.load(sql, denpyouId);
	}
	
	/**
	 * 伝票IDをキーに「承認ルート合議子」からデータを取得する。(枝番、合議枝番も指定)
	 * @param  denpyouId 伝票ID
	 * @param  edano 枝番
	 * @param  gougiEdano 合議枝番
	 * @return 検索結果
	 */
    @Deprecated
	public List<GMap> selectShouninRouteGougiKoWithEdano(String denpyouId,int edano,int gougiEdano) {
		final String sql = "SELECT * FROM shounin_route_gougi_ko WHERE denpyou_id = ? AND edano = ? AND gougi_edano = ? ORDER BY edano,gougi_edano,gougi_edaedano ";
		return connection.load(sql, denpyouId, edano, gougiEdano);
	}

	/**
	 * 現在作業者「承認ルート」を取得する。
	 * @param  denpyouId 伝票ID
	 * @return 検索結果
	 */
    @Deprecated
	public GMap findShouninRouteCur(String denpyouId) {
		final String sql = "SELECT * FROM shounin_route WHERE denpyou_id = ? AND genzai_flg = '1'";
		return connection.find(sql, denpyouId);
	}
	
	/**
	 * 伝票IDをキーに「承認ルート」から起票者のデータを取得する。
	 * @param  denpyou_id  String 伝票ID
	 * @return 検索結果 1件データ
	 */
    @Deprecated
	public GMap selectKihyoushaData(String denpyou_id) {
		final String sql = "SELECT * FROM shounin_route WHERE denpyou_id = ? AND edano = 1 ";
		return connection.find(sql, denpyou_id);
	}
	
	/**
	 * 該当伝票承認ルートの合議内ユーザーであり、かつ承認ルート上に単一で承認者として登録されていない
	 * 承認ルート合議子データを取得する。
	 * @param denpyouId 伝票ID
	 * @param user ユーザー
	 * @return 承認ルート合議子
	 */
	public GMap findRouteGougiShouninsha(String denpyouId, User user){
		final String sql = "SELECT * FROM shounin_route_gougi_ko "
				+ " WHERE denpyou_id = ? "
				+ " AND user_id = ? "
				+ " AND user_id NOT IN (SELECT user_id from shounin_route WHERE denpyou_id = ?) "
				+ " LIMIT 1 ";
		return connection.find(sql, denpyouId, user.getSeigyoUserId(), denpyouId);
	}

	/**
	 * 該当ユーザーが起票者で、未完了（起票中、申請中、）の伝票があるか調べる
	 * @param userId ユーザーID
	 * @return あるならtrue
	 */
	public boolean existsMikanryouDenpyou(String userId) {
		final String sql  =
				"SELECT COUNT(*) count FROM denpyou WHERE "
			+ "  (denpyou_id IN (SELECT denpyou_id FROM shounin_route WHERE user_id = ? AND edano = 1)) AND "
			+ "  ("
			+ "    (denpyou_joutai IN (?,?)) OR "
			+ "    (denpyou_joutai = ? AND denpyou_kbn LIKE 'A%' AND chuushutsu_zumi_flg = '0') "
			+ "  ) ";
		GMap record = connection.find(sql, userId, DENPYOU_JYOUTAI.KIHYOU_CHUU, DENPYOU_JYOUTAI.SHINSEI_CHUU, DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
		return ((long)record.get("count")) > 0;
	}
	
	/**
	 * 現在作業者となっているユーザーが承認ルートに登場する回数を取得する
	 * @param denpyouId 伝票ID
	 * @param user ユーザー
	 * @return 承認ルート登場回数
	 */
    @Deprecated
	public int countExistShouninsha(String denpyouId, User user){
		final String sql = "SELECT count(*) as count FROM shounin_route WHERE denpyou_id = ? AND user_id = ?";
		GMap result = connection.find(sql, denpyouId, user.getSeigyoUserId());
		return (null==result)? 0 : Integer.parseInt(result.get("count").toString());
	}

	/**
	 * 現在作業者となっているユーザーが承認ルート合議子に登場する回数を取得する
	 * @param denpyouId 伝票ID
	 * @param user ユーザー
	 * @return 承認ルート合議子登場回数
	 */
    @Deprecated
	public int countExistShouninshaGougiKo(String denpyouId, User user){
		final String sql = "SELECT count(*) as count FROM shounin_route_gougi_ko WHERE denpyou_id = ? AND user_id = ?";
		GMap result = connection.find(sql, denpyouId, user.getSeigyoUserId());
		return (null==result)? 0 : Integer.parseInt(result.get("count").toString());
	}
	

/* 承認状況(shounin_joukyou) */
	/**
	 * 操作履歴を取得
	 * @param denpyouId 伝票ID
	 * @return 承認状況
	 */
    @Deprecated
	public List<GMap> loadSousaRireki(String denpyouId) {
		final String sql = "SELECT * FROM shounin_joukyou WHERE denpyou_id = ? ORDER BY edano";
		return connection.load(sql, denpyouId);
	}

	/**
	 * 伝票IDをキーに「承認状況」からデータを取得する。
	 * @param  denpyouId  String 伝票ID
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectShouninJoukyou(String denpyouId) {
		final String sql_just = "SELECT * FROM shounin_joukyou j WHERE denpyou_id=? AND edano=?";

		//承認ルート + 承認ルート合議親 + 承認ルート合議子を取得して
		GMap denpyou = selectDenpyou(denpyouId);
		List<GMap> routeList = selectShouninRoute(denpyouId);

		//取下・否認で終わった場合は処理済ユーザのみを残す
		boolean minashi = denpyou.get("denpyou_joutai").equals(DENPYOU_JYOUTAI.TORISAGE_ZUMI) || denpyou.get("denpyou_joutai").equals(DENPYOU_JYOUTAI.HININ_ZUMI);

		List<GMap> ret = new ArrayList<>();
		for(int i = 0; i < routeList.size(); i++){
			GMap route = routeList.get(i);
			//承認ルート（ユーザ）の場合：
			//　　処理済なら処理コメントを紐付けて出す
			//　　未処理なら処理コメントを紐付けずに出す　ただし、取下・否認なら出さない
			if(!(boolean)route.get("isGougi")){
				if(route.get("joukyou_edano") != null){
					route.putAll(connection.find(sql_just, denpyouId, route.get("joukyou_edano")));
				}
				if(route.get("joukyou_edano") != null || !minashi){
					ret.add(route);
				}
			
			//承認ルート（合議）の場合：
			}else if((boolean)route.get("isGougi")){
				List<GMap> oyaList = route.get("gougiOya");
				for(int j = 0; j < oyaList.size(); j++){
					GMap oya = oyaList.get(j);
					List<GMap> koList = oya.get("gougiKo");
					List<GMap> koListNeo = new ArrayList<>();

					//合議子が処理済なら処理コメントを紐付けて出す
					//　　　　未処理なら処理コメントを紐付けずに出す　ただし、取下・否認なら出さない
					for(int z = 0; z < koList.size(); z++){
						GMap ko = koList.get(z);
						if(ko.get("joukyou_edano") != null){
							ko.putAll(connection.find(sql_just, denpyouId, ko.get("joukyou_edano")));
						}
						if(ko.get("joukyou_edano") != null || !minashi){
							koListNeo.add(ko);
						}
					}
					if(!koListNeo.isEmpty()){
						oya.put("gougiKo", koListNeo);
					}else{
						oya.put("gougiKo", null);
					}
				}
				//出すべき合議子がないなら合議親も出さないし、出すべき合議親がないならその承認ルート自体出さない
				oyaList.removeIf(oya -> oya.get("gougiKo") == null);
				if(!oyaList.isEmpty()){
					ret.add(route);
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * 伝票IDをキーに「承認状況」からデータを取得する。
	 * @param  denpyou_id  String 伝票ID
	 * @return 検索結果 リスト
	 */
	public GMap selectShinseiRireki(String denpyou_id) {
		final String sql = "SELECT sj.denpyou_id, "
						+  "sj.edano, "
						+  "sj.user_id, "
						+  "sj.user_full_name, "
						+  "sj.bumon_cd, "
						+  "sj.bumon_full_name, "
						+  "sj.bumon_role_id, "
						+  "sj.bumon_role_name, "
						+  "sj.gyoumu_role_id, "
						+  "sj.gyoumu_role_name, "
						+  "sj.joukyou, "
						+  "sj.comment, "
						+  "sj.koushin_time, "
						+  "ui.shain_no "
						+  "FROM shounin_joukyou sj "
						+  "LEFT OUTER JOIN user_info ui ON sj.user_id = ui.user_id "
						+  "WHERE denpyou_id = ? AND joukyou_cd='1' "
						+  "ORDER BY edano DESC "
						+  "LIMIT 1;";
		return connection.find(sql, denpyou_id);
	}
	
	/**
	 * 伝票IDをキーに「承認状況」から最終承認ユーザ名・社員番号を取得する。
	 * @param  denpyouId  String 伝票ID
	 * @return 検索結果 リスト
	 */
	public GMap selectSaishuShouninUserData(String denpyouId){
		final String sql = "SELECT sj.user_full_name,"
				+ " ui.shain_no"
				+ " FROM shounin_joukyou sj"
				+ " LEFT OUTER JOIN user_info ui ON sj.user_id = ui.user_id"
				+ " WHERE denpyou_id = ?"
				+ " AND joukyou_cd='4' ORDER BY edano DESC LIMIT 1;";
		return connection.find(sql, denpyouId);

	}
	
	/**
	 * 伝票IDと枝番をキーに最新の承認状況を取得する。
	 * 
	 * @param denpyou_id 伝票ID
	 * @param edano 枝番
	 * @return 最新の承認状況（１レコード）
	 */
    @Deprecated
	public GMap selectSaishinShouninJoukyou(String denpyou_id, int edano) {
		final String sql = "SELECT user_full_name, koushin_time, joukyou_cd, joukyou FROM shounin_joukyou WHERE denpyou_id = ? AND edano = ?";
		return connection.find(sql, denpyou_id, edano);
	}
	
	/**
	 * 伝票IDをキーに最新の申請日付を取得する。
	 * 
	 * @param denpyou_id 伝票ID
	 * @return 最新の申請日付（１レコード）
	 */
    @Deprecated
	public GMap selectShinseiDate(String denpyou_id) {
		final String sql = "SELECT denpyou_id, koushin_time FROM shounin_joukyou "
						+  " WHERE denpyou_id = ? AND joukyou_cd = '1' "
						+  " ORDER BY koushin_time DESC LIMIT 1; ";
		return connection.find(sql, denpyou_id);
	}
	
/* 任意メモ(nini_comment) */
	
	/**
	 * 伝票IDをキーに「任意メモ」からデータを取得する。
	 * @param  denpyou_id  String 伝票ID
	 * @return 検索結果 リスト
	 */
    @Deprecated
	public List<GMap> selectNiniComment(String denpyou_id) {
		final String sql = "SELECT denpyou_id, edano, user_id, user_full_name, comment, koushin_time "
				+  "FROM nini_comment "
				+  "WHERE denpyou_id = ? "
				+  "ORDER BY edano ASC;";
		return connection.load(sql, denpyou_id);
	}
	
/* 添付ファイル(tenpu_file) */
	/**
	 * 伝票IDをキーに「添付ファイル」からデータを取得する。(バイナリデータなし)
	 * @param  denpyou_id  String 伝票ID
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectTenpuFileForImport(String denpyou_id) {
		final String sql =
				"SELECT t.denpyou_id, t.edano, t.file_name, t.file_size, t.content_type, e.ebunsho_no, e.denshitorihiki_flg, e.tsfuyo_flg "
				+  "FROM tenpu_file t "
				+  "	LEFT OUTER JOIN ebunsho_file e ON (t.denpyou_id, t.edano) = (e.denpyou_id, e.edano) "				
				+  "WHERE t.denpyou_id = ? "
				+  "ORDER BY t.edano ASC;";
		return connection.load(sql, denpyou_id);
	}
	
	/**
	 * 伝票IDをキーに「添付ファイル」からデータを取得する。(バイナリデータなし)
	 * @param  denpyou_id  String 伝票ID
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectTenpuFile(String denpyou_id) {
		final String sql =
				
// Ver22.05.31.01 e文書関連の操作履歴 *-				
//    "SELECT t.denpyou_id, t.edano, t.file_name, t.file_size, t.content_type, e.ebunsho_no, e.denshitorihiki_flg, e.tsfuyo_flg "
// +  "FROM tenpu_file t "
// +  "LEFT OUTER JOIN ebunsho_file e ON (t.denpyou_id, t.edano) = (e.denpyou_id, e.edano) "
				"SELECT t.denpyou_id, t.edano, t.file_name, t.file_size, t.content_type, e.ebunsho_no, e.denshitorihiki_flg, e.tsfuyo_flg, "
				+  "d.ebunsho_edano, d.ebunsho_shubetsu, d.ebunsho_nengappi, d.ebunsho_kingaku, d.ebunsho_hakkousha, d.ebunsho_hinmei "
				+  "FROM tenpu_file t "
				+  "LEFT OUTER JOIN ebunsho_file e ON (t.denpyou_id, t.edano) = (e.denpyou_id, e.edano) "
				+  "LEFT OUTER JOIN ebunsho_data d ON (e.ebunsho_no) = (d.ebunsho_no) "
// -*				
				
				+  "WHERE t.denpyou_id = ? "
				+  "ORDER BY t.edano ASC;";
		return connection.load(sql, denpyou_id);
	}
	
	/**
	 * 伝票IDをキーに「添付ファイル」からデータを取得する。(バイナリデータあり)
	 * @param  denpyou_id  String 伝票ID
	 * @return 検索結果 リスト
	 */
    @Deprecated
	public List<GMap> selectTenpuFileBD(String denpyou_id) {
		final String sql = "SELECT " + 
				"  denpyou_id " + 
				"  , TO_CHAR(edano, 'FM999') edano " + 
				"  , file_name " + 
				"  , binary_data  " + 
				"FROM " + 
				"  tenpu_file " + 
				"WHERE denpyou_id = ?  " + 
				"ORDER BY edano ASC ";
		return connection.load(sql, denpyou_id);
	}
	
	/**
	 * 伝票IDをキーに「添付ファイル」からデータを取得する。データがなければnull。
	 * @param  denpyou_id  String 伝票ID
	 * @param  edano       String 枝番
	 * @return 検索結果 1件データ
	 */
    @Deprecated
	public GMap tenpuFileFind(String denpyou_id, int edano) {
		final String sql = "SELECT denpyou_id, edano, file_name, file_size, content_type, binary_data "
				+  "FROM tenpu_file "
				+  "WHERE denpyou_id = ? AND edano = ? "
				+  "ORDER BY edano ASC;";
		return connection.find(sql, denpyou_id, edano);
	}
	
	/**
	 * 伝票IDをキーに「添付ファイル」添付済みファイルの合計容量を算出して取得する。
	 * @param  denpyou_id  String 伝票ID
	 * @return 伝票ID添付済みファイルの合計容量
	 */
    @Deprecated
	public long findTenpuFileSize(String denpyou_id) {
		final String sql = "SELECT denpyou_id, SUM(file_size) as total_size "
				+  "FROM tenpu_file "
				+  "WHERE denpyou_id = ? "
				+  "GROUP BY denpyou_id;";
		GMap tmpMap = connection.find(sql, denpyou_id);
		long totalSize = 0;
		if (tmpMap != null) totalSize = ((BigDecimal)tmpMap.get("total_size")).longValue();
		return totalSize;
	}

	/**
	 * 複数の伝票ID（カンマ区切り）をキーに「添付ファイル」の合計数を取得する。
	 * @param  denpyouIds  String 伝票ID
	 * @return 添付ファイル数
	 */
    @Deprecated
	public long selectTenpuFileNum(String denpyouIds) {
		final String sql = "SELECT COUNT(*) FROM tenpu_file WHERE denpyou_id IN (" + denpyouIds + ")";
		GMap result = connection.find(sql); 
		return (long)result.get("count");
	}

	/**
	 * 複数の伝票ID（カンマ区切り）をキーに重複した「添付ファイル」のファイル名を取得する。
	 * @param  denpyouIds  String 伝票ID
	 * @return 検索結果
	 */
    @Deprecated
	public ArrayList<String> selectDuplicateTenpuFileName(String denpyouIds) {
		final String sql = "SELECT file_name FROM tenpu_file WHERE denpyou_id IN (" + denpyouIds + ") "
				+ "GROUP BY file_name HAVING COUNT(file_name) > 1";
		List<GMap> result = connection.load(sql); 
		ArrayList<String> tenpuList = new ArrayList<String>();
		for (GMap map : result) {
			tenpuList.add((String)map.get("file_name"));
		}
		return tenpuList;
	}
	
	/**
	 * 伝票IDをキーに「添付ファイル」を削除する。
	 * @param  denpyou_id  String 伝票ID
	 * @param  edano       String 枝番
	 * @return 検索結果
	 */
    @Deprecated
	public int tenpuFileDelete(String denpyou_id, int edano) {
		return connection.update("DELETE FROM tenpu_file WHERE denpyou_id = ? AND edano = ? ", denpyou_id, edano);
	}
	
	/**
	 * ebunsho_dataから「e文書ファイル」情報を削除する。（タイムスタンプ再付与対象のファイル向け）
	 * @param  ebunshoNo       String 枝番
	 * @return 検索結果
	 */
    @Deprecated
	public int tenpuEbunshoDataDelete(String ebunshoNo) {
		return connection.update("DELETE FROM ebunsho_data WHERE ebunsho_no = ? ", ebunshoNo);
	}
	/**
	 * ebunsho_fileから「e文書ファイル」情報を削除する。（タイムスタンプ再付与対象のファイル向け）
	 * @param  denpyou_id  String 伝票ID
	 * @param  ebunshoNo       String 枝番
	 * @return 検索結果
	 */
    @Deprecated
	public int tenpuEbunshoFileDelete(String denpyou_id, String ebunshoNo) {
		return connection.update("DELETE FROM ebunsho_file WHERE denpyou_id = ? AND ebunsho_no = ? ", denpyou_id, ebunshoNo);
	}
	
/* e文書ファイル(ebunsho_file) */
/* e文書データ(ebunsho_data) */
	/**
	 * 伝票IDをキーに「e文書ファイル」から
	 * 添付ファイル枝番、e文書番号を取得する。
	 * @param  denpyouId  String 伝票ID
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectTenpufileListWithEbunshoNo(String denpyouId) {
		final String sql = "select"
						+ " t.denpyou_id,"
						+ " t.edano,"
						+ " t.file_name,"
						+ " eb.ebunsho_no,"
						+ " eb.denshitorihiki_flg,"
						+ " eb.tsfuyo_flg,"
						+ " eb.touroku_time"
						+ " from tenpu_file t "
						+ " LEFT OUTER JOIN "
						+ " (select"
						+ " ef.denpyou_id,"
						+ " ef.ebunsho_no,"
						+ " ef.edano,"
						+ " ef.denshitorihiki_flg,"
						+ " ef.tsfuyo_flg,"
						+ " ef.touroku_time"
						+ " from ebunsho_file ef "
						+ " WHERE ef.denpyou_id = ? ) eb"
						+ " ON  t.denpyou_id = eb.denpyou_id and t.edano = eb.edano"
						+ " WHERE t.denpyou_id = ? "
						+ " ORDER BY t.denpyou_id,t.edano;";
				return connection.load(sql, denpyouId, denpyouId);
	}
	
	/**
	 * e文書番号をキーに「e文書ファイル」からデータを取得する。データがなければnull。
	 * @param  ebunshoNo String e文書番号
	 * @return 検索結果 1件データ
	 */
    @Deprecated
	public GMap findTenpuEbunshoFile(String ebunshoNo) {
		final String sql = "SELECT denpyou_id, edano, ebunsho_no, binary_data, touroku_user_id, touroku_time "
				+  "FROM ebunsho_file "
				+  "WHERE ebunsho_no = ?;";
		return connection.find(sql, ebunshoNo);
	}
	
	/**
	 * e文書番号をキーに「e文書データ」からe文書情報を取得する。
	 * @param  ebunshoNo  String e文書番号
	 * @return 検索結果 リスト
	 */
    @Deprecated
	public List<GMap> selectEbunshoDatalist(String ebunshoNo) {
		final String sql = "select * from ebunsho_data where ebunsho_no = ? order by ebunsho_edano;";
		return connection.load(sql, ebunshoNo);
	}

	
	/**
	 * 伝票IDをキーにe文書ファイルの数を取得する。
	 * @param  denpyouId  String 伝票ID
	 * @return 該当伝票のe文書ファイル数
	 */
    @Deprecated
	public long findEbunshoCount(String denpyouId) {
		final String sql = "SELECT COUNT(*) FROM ebunsho_file WHERE denpyou_id = '" + denpyouId + "';";
		GMap result = connection.find(sql); 
		long count = (long)result.get("count");
		return count;
	}
	
	/**
	 * 登録済e文書番号を返す
	 * @param denpyouId 伝票ID
	 * @param edano 枝番号
	 * @return e文書番号。未登録ならnull。
	 */
    @Deprecated
	public String findEbunshoNo(String denpyouId, Integer edano) {
		final String sql = "SELECT ebunsho_no FROM ebunsho_file WHERE denpyou_id = ? and edano = ?";
		GMap result = connection.find(sql, denpyouId, edano); 
		return (result == null) ? null : (String)result.get("ebunsho_no");
	}
	
	/**
	 * 指定した伝票IDに添付されているe文書でタイムスタンプの付与されていないファイル数を返す。
	 * @param denpyouId String 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @return 指定したe文書番号が既に作成されているか
	 */
    @Deprecated
	public long findEbunshoTimestampNotexistCount(String denpyouId, String denpyouKbn) {
		String sql = "";
		sql = "SELECT COUNT(*) FROM ebunsho_file WHERE denpyou_id = '" + denpyouId + "' and touroku_time IS NULL;";
		GMap result = connection.find(sql); 
		long count = (long)result.get("count");
		return count;
	}
	
/* 部門推奨ルート親(bumon_suishou_route_oya) */
/* 部門推奨ルート子(bumon_suishou_route_ko) */
	/**
	 * 伝票区分、部門コード、申請金額をキーに以下のテーブルを連結しデータを取得する。
	 * 「部門推奨ルート親・子」の有効期限内のレコードを取得する。
	 * @param  denpyou_kbn 伝票区分
	 * @param  bumon_cd    部門コード
	 * @param  kingaku     申請金額
	 * @param  date        基準日
	 * @return 検索結果 リスト
	 */
	protected List<GMap> selectBumonSuishouRoute(String denpyou_kbn, String bumon_cd, BigDecimal kingaku, Date date) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();
		//枝々番号、承認処理権限番号、合議パターン番号も取得
		sql.append("SELECT ");
		sql.append("  o.bumon_cd, ");
		sql.append("  k.bumon_role_id, k.edaedano as route_edaedano, k.shounin_shori_kengen_no, k.gougi_pattern_no, k.gougi_edano ");
		sql.append("FROM bumon_suishou_route_oya o ");
		sql.append("LEFT OUTER JOIN bumon_suishou_route_ko k USING(bumon_cd,denpyou_kbn,edano) ");
		sql.append("WHERE o.denpyou_kbn = ? AND o.bumon_cd = ? AND o.default_flg = '1'");
		param.add(denpyou_kbn);
		param.add(bumon_cd);
		
		if (kingaku != null) {
			sql.append(" AND ? BETWEEN o.kingaku_from AND o.kingaku_to ");
			param.add(kingaku);
		} else {
			sql.append(" AND o.kingaku_from IS NULL AND o.kingaku_to IS NULL ");
		}
		sql.append(" AND ? BETWEEN o.yuukou_kigen_from AND o.yuukou_kigen_to ");
		param.add(date);
		sql.append("ORDER BY k.edaedano ASC");
		return connection.load(sql.toString(), param.toArray());
	}
	
	/**
	 * 伝票区分、部門コード、申請金額をキーに以下のテーブルを連結しデータを取得する。
	 * 「部門推奨ルート親・子」の有効期限内のレコードを取得する。
	 * @param  denpyou_kbn 伝票区分
	 * @param  bumon_cd    部門コード
	 * @param  kingaku     申請金額
	 * @param  date        基準日
	 * @param shiwakeEdaNo 仕訳枝番号
	 * @return 検索結果 リスト
	 */
	protected List<GMap> selectBumonSuishouTorihikiRoute(String denpyou_kbn, String bumon_cd, BigDecimal kingaku, Date date, Integer shiwakeEdaNo) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();
		//枝々番号、承認処理権限番号、合議パターン番号も取得
		sql.append("SELECT ");
		sql.append("  o.bumon_cd, ");
		sql.append("  k.bumon_role_id, k.edaedano as route_edaedano, k.shounin_shori_kengen_no, k.gougi_pattern_no, k.gougi_edano ");
		sql.append("FROM bumon_suishou_route_oya o ");
		sql.append("LEFT OUTER JOIN bumon_suishou_route_ko k USING(bumon_cd,denpyou_kbn,edano) ");
		sql.append("WHERE o.denpyou_kbn = ? AND o.bumon_cd = ? AND o.default_flg = '0'");
		param.add(denpyou_kbn);
		param.add(bumon_cd);
		
		sql.append(" AND ? = ANY(o.shiwake_edano) ");
		param.add(shiwakeEdaNo);
		
		if (kingaku != null) {
			sql.append(" AND ? BETWEEN o.kingaku_from AND o.kingaku_to ");
			param.add(kingaku);
		} else {
			sql.append(" AND o.kingaku_from IS NULL AND o.kingaku_to IS NULL ");
		}
		sql.append(" AND ? BETWEEN o.yuukou_kigen_from AND o.yuukou_kigen_to ");
		param.add(date);
		sql.append("ORDER BY k.edaedano ASC");
		return connection.load(sql.toString(), param.toArray());
	}

	
	/**
	 * 伝票区分、部門コード、枝番をキーに以下のテーブルを連結しデータを取得する。
	 * 「部門推奨ルート親・子」のレコードを取得する。
	 * @param  denpyou_kbn 伝票区分
	 * @param  bumon_cd    部門コード
	 * @param  edano       枝番
	 * @return 検索結果 Map
	 */
	public GMap selectBumonSuishouRouteEdano(String denpyou_kbn, String bumon_cd, String edano) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();
		//枝々番号、承認処理権限番号、合議パターン番号も取得
		sql.append("SELECT ");
		sql.append("  o.bumon_cd, ");
		sql.append("  k.bumon_role_id, k.edaedano as route_edaedano, k.shounin_shori_kengen_no, k.gougi_pattern_no, k.gougi_edano ");
		sql.append("FROM bumon_suishou_route_oya o ");
		sql.append("LEFT OUTER JOIN bumon_suishou_route_ko k USING(bumon_cd,denpyou_kbn,edano) ");
		sql.append("WHERE o.denpyou_kbn = ? AND o.bumon_cd = ? AND o.edano = ? ");
		param.add(denpyou_kbn);
		param.add(bumon_cd);
		param.add(Integer.parseInt(edano));
		sql.append("ORDER BY k.edaedano ASC");
		return connection.find(sql.toString(), param.toArray());
	}
	
	/**
	 * 部門推奨ルート(親)全レコードを取得
	 * @return 部門推奨ルートリスト(親)
	 */
    @Deprecated
	public List<GMap> loadBumonSuishouRouteOya(){
		final String colList = "" 
				+ "  denpyou_kbn "
				+ " ,default_flg "
				+ " ,shiwake_edano "
				+ " ,bumon_cd "
				+ " ,edano "
				+ " ,kingaku_from "
				+ " ,kingaku_to "
				+ " ,yuukou_kigen_from "
				+ " ,yuukou_kigen_to ";
		final String sql = "SELECT " + colList +" FROM bumon_suishou_route_oya ORDER BY "+ colList;
		return connection.load(sql);
	}
	
	/**
	 * 部門推奨ルート(子)全レコードを取得
	 * @return 部門推奨ルートリスト(子)
	 */
    @Deprecated
	public List<GMap> loadBumonSuishouRouteKo(){
		final String colList = "" 
				+ "  denpyou_kbn "
				+ " ,bumon_cd "
				+ " ,edano "
				+ " ,edaedano "
				+ " ,bumon_role_id "
				+ " ,shounin_shori_kengen_no "
				+ " ,gougi_pattern_no "
				+ " ,gougi_edano ";
		final String sql = "SELECT " + colList +" FROM bumon_suishou_route_ko ORDER BY "+ colList;
		return connection.load(sql);
	}

	/**
	 * 
	 * @param denpyouKbn 伝票区分
	 * @param kihyouBumonCd 起票部門コード
	 * @param kingaku 金額
	 * @param seigyoUserId 制御ユーザーID
	 * @param kijunbi 基準日
	 * @param shiwakeEdaNo 仕訳枝番号
	 * @return ユーザーリスト
	 */
	public List<GMap> loadBumonSuishouRouteUser(String denpyouKbn, String kihyouBumonCd, BigDecimal kingaku, String seigyoUserId, Date kijunbi, String shiwakeEdaNo) {
		BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		KihyouNaviCategoryLogic navi = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		List<GMap> ret = new ArrayList<>();
		
		//指定された所属部門以上に対する部門推奨ルート親１×部門推奨ルート子Ｎを取得。見つからなければ所属部門を親に遡って探す
		String kensakuBumonCd = kihyouBumonCd;
		List<GMap> suishouRouteList = new ArrayList<GMap>();
		if(navi.findDenpyouKanri(denpyouKbn).get("route_torihiki_flg").toString().equals(EteamConst.routeTorihiki.OK)){
			if(StringUtils.isNotEmpty(shiwakeEdaNo)){
				while(StringUtils.isNotEmpty(kensakuBumonCd)) {
					suishouRouteList = selectBumonSuishouTorihikiRoute(denpyouKbn, kensakuBumonCd, kingaku, kijunbi, Integer.parseInt(shiwakeEdaNo));
					if (suishouRouteList.size() > 0)
					{
						break;
					}
					GMap tmpMap = bumonUserLogic.selectValidShozokuBumon(kensakuBumonCd,kijunbi);
					if(tmpMap != null){
						kensakuBumonCd = tmpMap.get("oya_bumon_cd").toString();
					}else{
						break;
					}
				}
			}
		}
		kensakuBumonCd = kihyouBumonCd;
		if(suishouRouteList.size() == 0){
			while(StringUtils.isNotEmpty(kensakuBumonCd)) {
				suishouRouteList = selectBumonSuishouRoute(denpyouKbn, kensakuBumonCd, kingaku, kijunbi);
				if (suishouRouteList.size() > 0)
				{
					break;
				}
				GMap tmpMap = bumonUserLogic.selectValidShozokuBumon(kensakuBumonCd,kijunbi);
				if(tmpMap != null){
					kensakuBumonCd = tmpMap.get("oya_bumon_cd").toString();
				}else{
					break;
				}
			}
		}
		
		//部門推奨ルート子にユーザー OR 合議を紐つける
		for(GMap route : suishouRouteList) {
			//部門推奨ルート子に該当するユーザーを紐つける(下位部門から探して1人だけ)
			if(route.get("gougi_pattern_no") == null ){
				kensakuBumonCd = kihyouBumonCd;
				while(StringUtils.isNotEmpty(kensakuBumonCd)){
					List<GMap> suishouRouteUserList = bumonUserLogic.shouninUserSerach(kensakuBumonCd, route.get("bumon_role_id").toString(), seigyoUserId, kijunbi);
					if (suishouRouteUserList.size() > 0) {
						GMap routeUser = new GMap();
						routeUser.putAll(route);
						routeUser.putAll(suishouRouteUserList.get(0));
						ret.add(routeUser);
						break;
					}
					GMap tmpMap = bumonUserLogic.selectValidShozokuBumon(kensakuBumonCd,kijunbi);
					if(tmpMap != null){
						kensakuBumonCd = tmpMap.get("oya_bumon_cd").toString();
					}else{
						break;
					}
				}
			//部門推奨ルート子に該当する合議を紐つける
			}else{
				//部門推奨ルート子(合議)に紐付くユーザー全員
				List<GMap> gougiPtnList = selectGougiPattern((Long)route.get("gougi_pattern_no"));
				for(GMap gMap : gougiPtnList) {
					List<GMap> suishouRouteUserGougiList = bumonUserLogic.shouninUserSerach(gMap.get("bumon_cd").toString(), gMap.get("bumon_role_id").toString(), seigyoUserId, kijunbi);
					for(GMap tmpMap : suishouRouteUserGougiList) {
						GMap routeUser = new GMap();
						routeUser.putAll(route);
						routeUser.putAll(gMap);
						routeUser.putAll(tmpMap);
						ret.add(routeUser);
					}
				}
			}
		}
		return ret;
	}
	
	
	/**
	 * 指定ID伝票の承認ルートリスト(承認者・合議単位)を取得
	 * @param denpyouId 伝票ID
	 * @return 承認ルートリスト(承認者・合議単位)
	 */
	protected List<GMap> selectShouninRouteWithGougi(String denpyouId){
		final String sql = "SELECT sr.denpyou_id, "
				+ "       sr.edano, "
				+ "       sr.user_id, "
				+ "       sr.bumon_cd, "
				+ "       sr.bumon_role_id, "
				+ "       sr.shounin_shori_kengen_no, "
				+ "       go.gougi_pattern_no, "
				+ "       go.gougi_edano "
				+ "  FROM shounin_route sr "
				+ "  LEFT OUTER JOIN shounin_route_gougi_oya go "
				+ "  ON sr.denpyou_id = go.denpyou_id "
				+ "  AND sr.edano = go.edano "
				+ " WHERE sr.saishu_shounin_flg <> '1' "
				+ "  AND sr.edano <> 1 "
				+ "  AND sr.denpyou_id = ? "
				+ " ORDER BY sr.denpyou_id,sr.edano,go.gougi_edano ";
		return connection.load(sql, denpyouId);
	}
	
	
	/**
	 * 指定ユーザ・金額・伝票区分でのデフォルト承認ルートリスト(承認者・合議単位)を取得
	 * @param denpyouKbn 伝票区分
	 * @param kihyouBumonCd 起票部門コード
	 * @param kingaku 金額
	 * @param seigyoUserId 制御ユーザーID
	 * @param kijunbi 基準日
	 * @param shiwakeEdaNo 仕訳枝番号
	 * @return 承認ルートリスト(承認者・合議単位)
	 */
	protected List<GMap> selectDefaultShouninRoute(String denpyouKbn, String kihyouBumonCd, BigDecimal kingaku, String seigyoUserId, Date kijunbi, String shiwakeEdaNo){
		BumonUserKanriCategoryLogic bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		KihyouNaviCategoryLogic navi = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
		
		//承認者・合議単位で部門推奨ルートデータを取得し、業務ロールにユーザ割り当て
		List<GMap> suishouList = new ArrayList<>();
		String kensakuBumonCd = kihyouBumonCd;
		List<GMap> suishouRouteList = new ArrayList<GMap>();
		if(navi.findDenpyouKanri(denpyouKbn).get("route_torihiki_flg").toString().equals(EteamConst.routeTorihiki.OK)){
			if(StringUtils.isNotEmpty(shiwakeEdaNo)){
				while(suishouRouteList.size() == 0 && StringUtils.isNotEmpty(kensakuBumonCd)) {
					suishouRouteList = selectBumonSuishouTorihikiRoute(denpyouKbn, kensakuBumonCd, kingaku, kijunbi, Integer.parseInt(shiwakeEdaNo));
					GMap tmpMap = bumonUserLogic.selectValidShozokuBumon(kensakuBumonCd,kijunbi);
					if(tmpMap != null){
						kensakuBumonCd = tmpMap.get("oya_bumon_cd").toString();
					}else{
						break;
					}
				}
			}
		}
		kensakuBumonCd = kihyouBumonCd;
		while(suishouRouteList.size() == 0 && StringUtils.isNotEmpty(kensakuBumonCd)) {
			suishouRouteList = selectBumonSuishouRoute(denpyouKbn, kensakuBumonCd, kingaku, kijunbi);
			GMap tmpMap = bumonUserLogic.selectValidShozokuBumon(kensakuBumonCd,kijunbi);
			if(tmpMap != null){
				kensakuBumonCd = tmpMap.get("oya_bumon_cd").toString();
			}else{
				break;
			}
		}
		for(GMap map : suishouRouteList) {
			kensakuBumonCd = kihyouBumonCd;
			List<GMap> suishouRouteUserList = new ArrayList<GMap>();
			while(suishouRouteUserList.size() == 0 && StringUtils.isNotEmpty(kensakuBumonCd)) {
				suishouRouteUserList = bumonUserLogic.shouninUserSerach(kensakuBumonCd, map.get("bumon_role_id").toString(), seigyoUserId, kijunbi);
				GMap tmpMap = bumonUserLogic.selectValidShozokuBumon(kensakuBumonCd,kijunbi);
				if(tmpMap != null){
					kensakuBumonCd = tmpMap.get("oya_bumon_cd").toString();
				}else{
					break;
				}
			}
			GMap tmpMap = new GMap();
			tmpMap.put("bumon_cd",map.get("bumon_cd"));
			tmpMap.put("bumon_role_id",map.get("bumon_role_id"));
			tmpMap.put("route_edaedano",map.get("route_edaedano"));
			tmpMap.put("shounin_shori_kengen_no",map.get("shounin_shori_kengen_no"));
			tmpMap.put("gougi_pattern_no",map.get("gougi_pattern_no"));
			tmpMap.put("gougi_edano",map.get("gougi_edano"));
			if (suishouRouteUserList.size() > 0) {
				GMap usrMap = suishouRouteUserList.get(0);
				tmpMap.put("user_id",usrMap.get("user_id"));
			}else{
				tmpMap.put("user_id","");
			}
			suishouList.add(tmpMap);
		}
		
		return suishouList;
	}

	/**
	 * 登録されている承認ルートがデフォルトの承認ルートと一致するか確認
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param kingaku 金額
	 * @param kijunbi 基準日
	 * @param shiwakeEdaNo 仕訳枝番号
	 * @return 一致するならtrue
	 */
	public boolean shouninRouteIsDefault(String denpyouId, String denpyouKbn, BigDecimal kingaku, Date kijunbi, String shiwakeEdaNo) {
		
		GMap kihyoushaMap = selectKihyoushaData(denpyouId);
		List<GMap> suishouList = selectDefaultShouninRoute(denpyouKbn,(String)kihyoushaMap.get("bumon_cd"),kingaku,(String)kihyoushaMap.get("user_id"), kijunbi, shiwakeEdaNo);
		List<GMap> currentList = selectShouninRouteWithGougi(denpyouId);
		
		//大元の承認ルートが同じならデフォルトとみなす(ユーザ・合議が1単位)
		if ( suishouList.size() != currentList.size() )
		{
			 return false;
		}
		for (int i = 0; i < currentList.size(); i++) {
			if (! suishouList.get(i).get("user_id").equals(currentList.get(i).get("user_id"))) {
				return false;
			}
			if (! suishouList.get(i).get("bumon_role_id").equals(currentList.get(i).get("bumon_role_id"))) {
				return false;
			}
			
			String gptna = suishouList.get(i).get("gougi_pattern_no") != null ? suishouList.get(i).get("gougi_pattern_no").toString() : "";
			String gptnb = currentList.get(i).get("gougi_pattern_no") != null ? currentList.get(i).get("gougi_pattern_no").toString() : "";
			String gedaa = suishouList.get(i).get("gougi_edano") != null ? suishouList.get(i).get("gougi_edano").toString() : "";
			String gedab = currentList.get(i).get("gougi_edano") != null ? currentList.get(i).get("gougi_edano").toString() : "";
			
			if (!gptna.equals(gptnb)) {
				return false;
			}
			if (!gedaa.equals(gedab)) {
				return false;
			}
			
			//合議の場合、合議内承認者に指定されている承認権限が該当合議テンプレートと一致していない場合は変更有とみなす
			if( !(gptnb.isEmpty()) ){
				int  eda  = Integer.parseInt(currentList.get(i).get("edano").toString());
				long gptn = Integer.parseInt(gptnb);
				int  geda = Integer.parseInt(gedab);
				List<GMap> ptnList = selectGougiPattern(gptn);
				List<GMap> gkList = selectShouninRouteGougiKoWithEdano(denpyouId,eda,geda);
				
				//bumon_cd,bumon_role_id,shounin_shori_kengen_noの組み合わせが順序どおりであるかチェック
				//該当役職がいない等の原因で合議テンプレートにいるユーザーが現在ルートに含まれていない可能性も考慮
				int ptnInd = 0;
				String bcdp = ptnList.get(ptnInd).get("bumon_cd").toString();
				String brip = ptnList.get(ptnInd).get("bumon_role_id").toString();
				String sskp = ptnList.get(ptnInd).get("shounin_shori_kengen_no").toString();
				
				for(int j = 0; j < gkList.size(); j++) {
					String bcdg = gkList.get(j).get("bumon_cd").toString();
					String brig = gkList.get(j).get("bumon_role_id").toString();
					String sskg = gkList.get(j).get("shounin_shori_kengen_no").toString();
					while( !(bcdg.equals(bcdp) && brig.equals(brip) && sskg.equals(sskp)) ){
						ptnInd++;
						if(ptnInd >= ptnList.size()){
							return false;
						}
						bcdp = ptnList.get(ptnInd).get("bumon_cd").toString();
						brip = ptnList.get(ptnInd).get("bumon_role_id").toString();
						sskp = ptnList.get(ptnInd).get("shounin_shori_kengen_no").toString();
					}
				}
			}
			
			if ( (gptna.isEmpty() && gptnb.isEmpty()) && ! suishouList.get(i).get("shounin_shori_kengen_no").equals(currentList.get(i).get("shounin_shori_kengen_no"))) {
				return false;
			}

		}
		return true;
	}
	
/* 合議パターン親(gougi_pattern_oya) */
/* 合議パターン子(gougi_pattern_ko) */
	/**
	 * 合議パターン番号をキーに「合議パターン親」「合議パターン子」から合議パターン情報を取得する。データがなければnull。
	 * @param  gougiPtnNo 合議パターン番号
	 * @return 検索結果 リスト
	 */
	public List<GMap> selectGougiPattern(Long gougiPtnNo) {
		final String sql = "SELECT go.gougi_pattern_no,  "
				+  "       go.gougi_name, "
				+  "       gk.edano, "
				+  "       gk.bumon_cd, "
				+  "       gk.bumon_role_id, "
				+  "       gk.shounin_shori_kengen_no, "
				+  "       gk.shounin_ninzuu_cd, "
				+  "       gk.shounin_ninzuu_hiritsu "
				+  "FROM gougi_pattern_oya go LEFT OUTER JOIN gougi_pattern_ko gk ON go.gougi_pattern_no = gk.gougi_pattern_no "
				+  "WHERE go.gougi_pattern_no = ? "
				+  "     ORDER BY gk.edano ";
		return connection.load(sql, gougiPtnNo);
	}
	
/* 最終承認ルート親(saishuu_syounin_route_oya) */
/* 最終承認ルート子(saishuu_syounin_route_ko) */

	/**
	 * 伝票区分をキーに「最終承認ルート親」から注記文言を取得する。データがなければnull。
	 * @param  denpyou_kbn 伝票区分
	 * @return 検索結果 1件データ
	 */
    @Deprecated
	public GMap selectChuukiMongon(String denpyou_kbn) {
		final String sql = "SELECT denpyou_kbn, edano, chuuki_mongon, yuukou_kigen_from, yuukou_kigen_to "
						+  "FROM   saishuu_syounin_route_oya "
						+  "WHERE  denpyou_kbn = ? AND current_date BETWEEN yuukou_kigen_from AND yuukou_kigen_TO ";

		return connection.find(sql, denpyou_kbn);
	}

	/**
	 * 伝票区分をキーに「最終承認ルート子」から最終承認ルートを取得する。
	 * @param  denpyou_kbn 伝票区分
	 * @param  edano       最終承認ルート親の枝番
	 * @return 検索結果 リスト
	 */
    @Deprecated
	public List<GMap> selectSaishuuSyouninRoute(String denpyou_kbn, int edano) {
		final String sql = "SELECT ssr.denpyou_kbn, ssr.edano, ssr.gyoumu_role_id, ssr.saishuu_shounin_shori_kengen_name, gr.gyoumu_role_name "
						+  "FROM saishuu_syounin_route_ko ssr, gyoumu_role gr "
						+  "WHERE denpyou_kbn = ? AND ssr.edano = ? AND ssr.gyoumu_role_id = gr.gyoumu_role_id "
						+  "ORDER BY edaedano ASC;";
		return connection.load(sql, denpyou_kbn, edano);
	}
	
	/**
	 * 伝票区分と業務ロールIDをキーに「最終承認ルート子」から処理権限名を取得する。
	 * @param  denpyou_kbn     伝票区分
	 * @param  gyoumu_role_id  業務ロールID
	 * @return 処理権限名
	 */
    @Deprecated
	public String findSaiSaishuuShouninShoriKengen(String denpyou_kbn, String gyoumu_role_id) {
		if (null==gyoumu_role_id)
		{
			return "";
		}
		final String sql = "SELECT saishuu_shounin_shori_kengen_name FROM saishuu_syounin_route_ko WHERE denpyou_kbn = ? AND gyoumu_role_id = ?";
		GMap record = connection.find(sql, denpyou_kbn, gyoumu_role_id);
		return record == null ? "" : (String)record.get("saishuu_shounin_shori_kengen_name");
	}

/* 承認処理権限 */
	/**
	 * 承認処理権限レコード取得
	 * @param shouninShoriKengenNo 承認処理権限番号
	 * @return 承認処理権限レコード
	 */
    @Deprecated
	public GMap findShouninShoriKengen(long shouninShoriKengenNo){
		final String sql = "SELECT * FROM shounin_shori_kengen WHERE shounin_shori_kengen_no=?";
		return connection.find(sql, shouninShoriKengenNo);
	}
	/**
	 * 承認文言取得
	 * @param shouninShoriKengenNo 承認処理権限番号
	 * @return 承認文言
	 */
	public String findShouninShoriMongon(Long shouninShoriKengenNo){
		if (shouninShoriKengenNo == null)
		{
			return "";
		}
		GMap record = findShouninShoriKengen(shouninShoriKengenNo);
		return record == null ? "" : (String)record.get("shounin_mongon");
	}
	/**
	 * 承認処理権限名取得
	 * @param shouninShoriKengenNo 承認処理権限番号
	 * @return 承認処理権限名
	 */
	public String findShouninShoriKengenName(Long shouninShoriKengenNo){
		if (shouninShoriKengenNo == null)
		{
			return "";
		}
		GMap record = findShouninShoriKengen(shouninShoriKengenNo);
		return record == null ? "" : (String)record.get("shounin_shori_kengen_name");
	}

/* 関連伝票 */
	/**
	 * 関連伝票入力欄表示フラグが有効であるか判定する。
	 * @param denpyouKbn 伝票区分
	 * @return 有効であればtrue
	 */
    @Deprecated
	public boolean isUsableKanrenDenpyou (String denpyouKbn) {
		final String sql =   "SELECT kanren_hyouji_flg "
							+ "FROM   denpyou_shubetsu_ichiran "
							+ "WHERE  denpyou_kbn = ?;";
		GMap ret = connection.find(sql, denpyouKbn);
		return ((String)ret.get("kanren_hyouji_flg")).equals("1") ? true : false;
	}

	/**
	 * 伝票IDに紐づく関連伝票を取得する。
	 * @param denpyouId 伝票ID
	 * @return 関連伝票数
	 */
	public List<GMap> loadKanrenDenpyou (String denpyouId) {
		final String sql = "SELECT "
						+ "  kan.kanren_denpyou as denpyou_id "
						+ "  , dsi.denpyou_shubetsu "
						+ "  , dsi.denpyou_kbn "
						+ "  , COALESCE(kani.version, 0) as version "
						+ "  , dsi.denpyou_shubetsu_url "
						+ "  , kan.kanren_denpyou_kihyoubi as touroku_time "
						+ "  , kan.kanren_denpyou_shouninbi as shounin_time "
						+ "  , (SELECT count(*) FROM denpyou_kian_himozuke WHERE denpyou_id = kan.denpyou_id AND ringi_kingaku_hikitsugimoto_denpyou = kan.kanren_denpyou) AS ringi_kingaku_hikitsugi_flg "
						+ "FROM "
						+ "  kanren_denpyou kan "
						+ "  INNER JOIN denpyou_shubetsu_ichiran dsi "
						+ "    ON kan.kanren_denpyou_kbn = dsi.denpyou_kbn "
						+ "  LEFT OUTER JOIN ( "
						+ "    SELECT DISTINCT "
						+ "      denpyou_id "
						+ "      , version "
						+ "    FROM "
						+ "      kani_todoke "
						+ "  ) kani "
						+ "    ON kan.kanren_denpyou = kani.denpyou_id "
						+ "WHERE "
						+ "  kan.denpyou_id = ? "
						+ "ORDER BY "
						+ " kan.kanren_denpyou ";


		return connection.load(sql, denpyouId);
	}

	/**
	 * 関連伝票を検索する。
	 * @param userId 登録ユーザーID
	 * @param denpyouId 伝票ID
	 * @param denpyouShubetsu 伝票種別
	 * @param shouninFrom 承認日From
	 * @param shouninTo 承認日To
	 * @param jyogaiDataHyoujiFlg 除外データ表示フラグ
	 * @return 検索結果
	 */
	public List<GMap> loadEnableKanrenDenpyou(String userId, String denpyouId, String denpyouShubetsu, String shouninFrom, String shouninTo, String jyogaiDataHyoujiFlg) {
		String sql = "SELECT "
					+ "  d.denpyou_kbn "
					+ "  , dsi.denpyou_shubetsu_url "
					+ "  , COALESCE(kani.version, 0) as version "
					+ "  , dsi.denpyou_shubetsu "
					+ "  , d.denpyou_kbn "
					+ "  , d.denpyou_id "
					+ "  , s.touroku_time "
					+ "  , sj.touroku_time as shounin_time "
					+ "FROM "
					+ "  denpyou d "
					+ "  INNER JOIN shounin_route s "
					+ "    ON d.denpyou_id = s.denpyou_id "
					+ "    AND s.edano = 1 "
					+ "  INNER JOIN shounin_joukyou sj "
					+ "    ON sj.denpyou_id = d.denpyou_id "
					+ "    AND sj.joukyou_cd = '4' "
					+ "    AND sj.edano = ( "
					+ "      SELECT "
					+ "        MAX(edano) "
					+ "      FROM "
					+ "        shounin_joukyou "
					+ "      WHERE "
					+ "        denpyou_id = d.denpyou_id "
					+ "        AND joukyou_cd = '4' "
					+ "    ) "
					+ "  INNER JOIN denpyou_shubetsu_ichiran dsi "
					+ "    ON d.denpyou_kbn = dsi.denpyou_kbn "
					+ "  LEFT OUTER JOIN ( "
					+ "    SELECT DISTINCT "
					+ "      denpyou_id "
					+ "      , version "
					+ "    FROM "
					+ "      kani_todoke "
					+ "  ) kani "
					+ "    ON d.denpyou_id = kani.denpyou_id "
					+ "WHERE "
					+ "  d.denpyou_joutai = ? "
					+ "  AND s.user_id = ? "
					+ "  AND d.denpyou_kbn IN ( "
					+ "    SELECT "
					+ "      denpyou_kbn "
					+ "    FROM "
					+ "      denpyou_shubetsu_ichiran "
					+ "    WHERE "
					+ "      kanren_sentaku_flg = '1' "
					+ "  ) "
					+ "  :DENPYOUID "
					+ "  :DENPYOUSHUBETSU "
					+ "  :SHOUNINFROM "
					+ "  :SHOUNINTO "
					+ "  :JYOGAIFLG "
					+ "ORDER BY "
					+ "  sj.touroku_time DESC; ";
		
		if (denpyouId == null || denpyouId.isEmpty()) {
			sql = sql.replaceAll(":DENPYOUID", "");
		} else {
			sql = sql.replaceAll(":DENPYOUID", " AND d.denpyou_id = '" + denpyouId + "' ");
		}
		
		if (denpyouShubetsu == null || denpyouShubetsu.isEmpty()) {
			sql = sql.replaceAll(":DENPYOUSHUBETSU", "");
		} else {
			sql = sql.replaceAll(":DENPYOUSHUBETSU", " AND dsi.denpyou_kbn = '" + denpyouShubetsu + "' ");
		}
		
		if (shouninFrom == null || shouninFrom.isEmpty()) {
			sql = sql.replaceAll(":SHOUNINFROM", "");
		} else {
			sql = sql.replaceAll(":SHOUNINFROM", " AND sj.touroku_time > '"+ shouninFrom  +"'");
		}
		
		if ((shouninTo == null || shouninTo.isEmpty())) {
			sql = sql.replaceAll(":SHOUNINTO", "");
		} else {
			sql = sql.replaceAll(":SHOUNINTO", " AND sj.touroku_time < (CAST('"+ shouninTo  +"' AS DATE) + 1)");
		}
		
		if (!("1".equals(jyogaiDataHyoujiFlg)) ) {
			sql = sql.replaceAll(":JYOGAIFLG", " AND d.denpyou_id NOT IN (SELECT denpyou_id FROM tenpu_denpyou_jyogai)");
		} else {
			sql = sql.replaceAll(":JYOGAIFLG", " AND d.denpyou_id IN (SELECT denpyou_id FROM tenpu_denpyou_jyogai)");
		}
		
		// SQL分のカスタマイズが必要な場合はここで行う
		if(this.customStrategy != null)
		{
			this.customStrategy.currentSql = sql;
			sql = this.customStrategy.getCustomizeLoadEnableKanrenDenpyouSql();
		}
		
		return connection.load(sql, EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SYOUNIN_ZUMI, userId);
	}

/* 伝票起案紐付 */
	/**
	 * 伝票IDをキーに「伝票起案紐付」から支出起案・実施起案伝票IDを取得する。データがなければnull。
	 * @param  denpyou_id  String 伝票ID
	 * @return 検索結果 1件データ
	 */
    @Deprecated
	public GMap selectDenpyouKianId(String denpyou_id) {
		final String sql = "SELECT kian_denpyou FROM denpyou_kian_himozuke WHERE denpyou_id = ?;";
		return connection.find(sql, denpyou_id);
	}

	/**
	 * 起案添付元から起案添付先の伝票ID(複数)を取得
	 * @param denpyouId 伝票ID
	 * @return 起案添付先の伝票ID(複数)
	 */
	public List<String> loadBottomKianDenpyouId(String denpyouId){
		List<String> list = new ArrayList<>();
		loadBottomKianDenpyouId(denpyouId, list);
		return list;
	}
	/**
	 * 起案添付元から起案添付先の伝票ID(複数)を取得
	 * @param denpyouId 伝票ID
	 * @param list 起案添付先の伝票ID(複数)
	 */
	public void loadBottomKianDenpyouId(String denpyouId, List<String> list){
		final String sql = "SELECT denpyou_id FROM denpyou_kian_himozuke WHERE kian_denpyou = ?";
		List<GMap> tmpBottmoList = connection.load(sql, denpyouId);
		for(GMap tmpBottmoMap : tmpBottmoList){
			String tmpBottomId = (String)tmpBottmoMap.get("denpyou_id");
			list.add(tmpBottomId);
			loadBottomKianDenpyouId(tmpBottomId, list);
		}
	}
	
	/**
	 * 指定した伝票IDの稟議金額引継ぎ元を取得する。
	 * @param denpyouId 伝票ID
	 * @return 稟議金額引継ぎ元伝票ID
	 */
    @Deprecated
	protected String findRingiMotoDenpyouId(String denpyouId){
		String sql = "SELECT ringi_kingaku_hikitsugimoto_denpyou FROM denpyou_kian_himozuke WHERE denpyou_id = ?";
		GMap ret = connection.find(sql, denpyouId);
		return (null != ret)? ret.get("ringi_kingaku_hikitsugimoto_denpyou").toString() : "";
	}
	
	/**
	 * 指定した伝票IDの大本となる稟議金額引継ぎ元を取得する。
	 * @param ret 結果マップ
	 * @param denpyouId 伝票ID
	 * @return 結果マップ
	 */
	public GMap findRingiMotoDenpyouId(GMap ret, String denpyouId){
		String parDenpyouId = denpyouId;
		String motoDenpyouId;
		while(ret.isEmpty()){
			motoDenpyouId = findRingiMotoDenpyouId(parDenpyouId);
			if(!"".equals(motoDenpyouId)){
				parDenpyouId = motoDenpyouId;
			}else{
				// 探索終わり
				ret.put("ringi_kingaku_hikitsugimoto_denpyou_base", parDenpyouId);
			}
		}
		return ret;
	}
	
	/**
	 * 指定した伝票から稟議金額の引継いだ伝票の情報を取得する。
	 * @param denpyouId 伝票ID
	 * @return 検索結果
	 */
    @Deprecated
	protected List<GMap> loadRingiSakiDenpyouId(String denpyouId){
		String sql = "SELECT denpyou_id FROM denpyou_kian_himozuke WHERE ringi_kingaku_hikitsugimoto_denpyou = ?";
		return connection.load(sql, denpyouId);
	}
	
	/**
	 * 指定した伝票から稟議金額を引き継いだ全伝票の情報を取得する。
	 * @param ret 結果マップ
	 * @param denpyouId 伝票ID
	 * @return 結果マップ
	 */
	public List<GMap> loadRingiSakiDenpyouId(List<GMap> ret, String denpyouId){
		
		String parDenpyouId = denpyouId;
		List<GMap> hikitsugiDenpyouList =  loadRingiSakiDenpyouId(parDenpyouId);
		
		for(GMap map :hikitsugiDenpyouList){
			map.put("ringi_kingaku_hikitsugimoto_denpyou", parDenpyouId);
			ret.add(map);
			
			ret = loadRingiSakiDenpyouId(ret, map.get("denpyou_id").toString());
		}
		
		return ret;
	}
	
	/**
	 * 予算執行項目テキストデータ ラベル名称取得<br>
	 * ユーザー定義届書申請から申請エリアに設定されているテキスト形式の予算執行項目のラベル名称を取得する。
	 * 
	 * @param denpyouId 伝票ID
	 * @param yosanShikkouId 予算執行項目ID
	 * @return 予算執行項目のラベル名称
	 */
	public String findKaniTodokeLabelName(String denpyouId, String yosanShikkouId){
		String result = "";
		
		// MainSql
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("SELECT")
				 .append("  C.label_name")
				 .append(" FROM kani_todoke AS A")
				 .append(" INNER JOIN kani_todoke_koumoku AS B ON (")
				 .append("      B.denpyou_kbn = A.denpyou_kbn")
				 .append("  AND B.version = A.version")
				 .append("  AND B.item_name = A.item_name")
				 .append(" )")
				 .append(" INNER JOIN kani_todoke_text AS C ON (")
				 .append("      C.denpyou_kbn = A.denpyou_kbn")
				 .append("  AND C.version = A.version")
				 .append("  AND C.area_kbn = B.area_kbn")
				 .append("  AND C.item_name = B.item_name")
				 .append(" )")
				 .append(" WHERE")
				 .append("      A.denpyou_id = ?")
				 .append("  AND B.yosan_shikkou_koumoku_id = ?");
		
		//検索実施
		GMap mapResult = connection.find(sbMainSql.toString(), denpyouId, yosanShikkouId);
		
		// 戻り値取得
		if (null != mapResult){
			result = (String)mapResult.get("label_name");
		}
		
		return result;
	}
	
	/**
	 * 予算執行項目データ取得<br>
	 * ユーザー定義届書申請から申請エリアに設定されている予算執行項目のデータを取得する。
	 * 
	 * @param denpyouId 伝票ID
	 * @param yosanShikkouId 予算執行項目ID
	 * @return 予算執行項目に対するデータ値
	 */
	public String getKaniTodokeYosanShikkouColumnData(String denpyouId, String yosanShikkouId){
		String result = "";

		// MainSQL
		StringBuilder sbMainSql = new StringBuilder();
		sbMainSql.append("SELECT")
				 .append("  B.value1")
				 .append(" FROM denpyou AS A")
				 .append(" INNER JOIN kani_todoke AS B ON B.denpyou_id = A.denpyou_id")
				 .append(" INNER JOIN kani_todoke_koumoku AS C ON (")
				 .append("      C.denpyou_kbn = B.denpyou_kbn")
				 .append("  AND C.version = B.version")
				 .append("  AND C.item_name = B.item_name")
				 .append(" )")
				 .append(" WHERE")
				 .append("      A.denpyou_id = ?")
				 .append("  AND C.yosan_shikkou_koumoku_id = ?");

		// 検索実施
		GMap mapResult = connection.find(sbMainSql.toString(), denpyouId, yosanShikkouId);

		// 戻り値取得
		if (null != mapResult){
			result = (String)mapResult.get("value1");
		}

		return result;
	}

	/**
	 * 承認ルートの差分を取る、差がなければnullを返す
	 * @param bfRouteList 変更前
	 * @param afRouteList 変更後
	 * @return 差分表現
	 */
	public String diffRouteList(List<GMap> bfRouteList, List<GMap> afRouteList) {
	try{
		//件数違う
		if(bfRouteList.size() != afRouteList.size()){
			return "承認ルートを変更しました\r\n変更前:" + makeRouteListStr(bfRouteList) + "\r\n変更後:" + makeRouteListStr(afRouteList);
		}
		
		List<String> msgList = new ArrayList<>();
		
		//承認ルート単位で違う
		boolean routeDefferent = false;
		for(int i = 0; i < bfRouteList.size(); i++){
			GMap bfRoute = bfRouteList.get(i);
			GMap afRoute = afRouteList.get(i);
			if(!bfRoute.get("isGougi").equals(afRoute.get("isGougi"))){
				routeDefferent = true;
				break;
			}else{
				if(!(boolean)bfRoute.get("isGougi")){
					if(!sameRoute(bfRoute, afRoute)){
						routeDefferent = true;
						break;
					}
				}
			}
		}
		if(routeDefferent){
			msgList.add("承認ルートを変更しました\r\n変更前:" + makeRouteListStr(bfRouteList) + "\r\n変更後:" + makeRouteListStr(afRouteList));
		}
		
		//合議単位で違う
		for(int i = 0; i < bfRouteList.size(); i++){
			GMap bfRoute = bfRouteList.get(i);
			GMap afRoute = afRouteList.get(i);
			if((boolean)bfRoute.get("isGougi") && (boolean)afRoute.get("isGougi")){
				List<GMap> bfGougiOyaLsit = (List<GMap>)bfRoute.get("gougiOya");
				List<GMap> afGougiOyaLsit = (List<GMap>)afRoute.get("gougiOya");
				if(bfGougiOyaLsit.size() != afGougiOyaLsit.size()){
					msgList.add("合議" + (i+1) + "を変更しました\r\n変更前:" + makeGougiOyaStr(bfGougiOyaLsit) + "\r\n変更後:" + makeGougiOyaStr(afGougiOyaLsit));
				}else{
					for(int j = 0; j < bfGougiOyaLsit.size(); j++){
						GMap bfGougiOya = bfGougiOyaLsit.get(j);
						GMap afGougiOya = afGougiOyaLsit.get(j);
						if(!bfGougiOya.get("gougi_pattern_no").equals(afGougiOya.get("gougi_pattern_no"))){
							msgList.add("合議" + (i+1) + "を変更しました\r\n変更前:" + makeGougiOyaStr(bfGougiOyaLsit) + "\r\n変更後:" + makeGougiOyaStr(afGougiOyaLsit));
							break;
						}
					}
				}
			}
		}
		if(!msgList.isEmpty()){
			return String.join("\r\n", msgList);
		}
		
		//合議内で違う
		for(int i = 0; i < bfRouteList.size(); i++){
			GMap bfRoute = bfRouteList.get(i);
			GMap afRoute = afRouteList.get(i);
			if((boolean)bfRoute.get("isGougi") && (boolean)afRoute.get("isGougi")){
				List<GMap> bfGougiOyaLsit = (List<GMap>)bfRoute.get("gougiOya");
				List<GMap> afGougiOyaLsit = (List<GMap>)afRoute.get("gougiOya");
				for(int j = 0; j < bfGougiOyaLsit.size(); j++){
					GMap bfGougiOya = bfGougiOyaLsit.get(j);
					GMap afGougiOya = afGougiOyaLsit.get(j);
					List<GMap> bfGougiKoList = (List<GMap>)bfGougiOya.get("gougiKo");
					List<GMap> afGougiKoList = (List<GMap>)afGougiOya.get("gougiKo");
					if(bfGougiKoList.size() != afGougiKoList.size()){
						msgList.add("合議" + (i+1) + "-" + (j+1) + "を変更しました\r\n変更前:" + makeGougiKoStr(bfGougiKoList) + "\r\n変更後:" + makeGougiKoStr(afGougiKoList));
						break;
					}else{
						for(int k = 0; k < bfGougiKoList.size(); k++){
							GMap bfGougiKo = bfGougiKoList.get(k);
							GMap afGougiKo = afGougiKoList.get(k);
							if(!sameGougiKo(bfGougiKo, afGougiKo)){
								msgList.add("合議" + (i+1) + "-" + (j+1) + "を変更しました\r\n変更前:" + makeGougiKoStr(bfGougiKoList) + "\r\n変更後:" + makeGougiKoStr(afGougiKoList));
								break;
							}
						}
					}
				}
			}
		}
		if(!msgList.isEmpty()){
			return String.join("\r\n", msgList);
		}
		
		return null;
	}catch(Exception e){
		EteamLogger.getLogger(getClass()).error("承認ルート差分取得時のエラー", e);
		return "";
	}
	}
	/**
	 * 承認ルートを文字列表現
	 * @param routeList 承認ルートリスト
	 * @return 文字列表現
	 */
	protected String makeRouteListStr(List<GMap> routeList){
		StringBuffer buf = new StringBuffer();
		for(GMap route : routeList){
			if(!(boolean)route.get("isGougi")){
				if(isEmpty(route.get("gyoumu_role_id"))){
					buf.append(route.get("user_full_name").toString()).append("(").append(route.get("bumon_role_name").toString()).append(isEmpty(route.get("shounin_shori_kengen_name")) ? "" : ":" + route.get("shounin_shori_kengen_name")).append(")").append("、");
				}else{
					buf.append(route.get("gyoumu_role_name").toString()).append(isEmpty(route.get("shounin_shori_kengen_name").toString()) ? "" : "(" + route.get("shounin_shori_kengen_name") + ")").append("、");
				}
			}else{
				buf.append("合議").append("、");
			}
		}
		if(buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}
	/**
	 * 承認ルート合議親を文字列表現
	 * @param gougiOyaLsit 承認ルート合議親リスト
	 * @return 文字列表現
	 */
	protected String makeGougiOyaStr(List<GMap> gougiOyaLsit) {
		StringBuffer buf = new StringBuffer();
		for(GMap gougiOya : gougiOyaLsit){
			buf.append(gougiOya.get("gougi_name").toString()).append("、");
			
		}
		if(buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}
	/**
	 * 承認ルート合議子を文字列表現
	 * @param gougiKoList 承認ルート合議親リスト
	 * @return 文字列表現
	 */
	protected String makeGougiKoStr(List<GMap> gougiKoList) {
		StringBuffer buf = new StringBuffer();
		for(GMap gougiKo : gougiKoList){
			buf.append(gougiKo.get("user_full_name") + "(" + gougiKo.get("bumon_role_name")  + ":" + gougiKo.get("shounin_shori_kengen_name") + ")").append("、");
		}
		if(buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}
	/**
	 * 承認ルートが同じ
	 * @param a 承認ルート
	 * @param b 承認ルート
	 * @return 同じ
	 */
	protected boolean sameRoute(GMap a, GMap b){
		if (!a.get("user_id").equals(b.get("user_id")))
		{
			return false;
		}
		if (!a.get("gyoumu_role_id").equals(b.get("gyoumu_role_id")))
		{
			return false;
		}
		if (a.get("shounin_shori_kengen_no") == null && b.get("shounin_shori_kengen_no") != null)
		{
			return false;
		}
		if (a.get("shounin_shori_kengen_no") != null && !a.get("shounin_shori_kengen_no").equals(b.get("shounin_shori_kengen_no")))
		{
			return false;
		}
		return true;
	}
	/**
	 * 承認ルート合議子が同じ
	 * @param a 承認ルート合議子
	 * @param b 承認ルート合議子
	 * @return 同じ
	 */
	protected boolean sameGougiKo(GMap a, GMap b){
		if (!a.get("user_id").equals(b.get("user_id")))
		{
			return false;
		}
		if (a.get("shounin_shori_kengen_no") == null && b.get("shounin_shori_kengen_no") != null)
		{
			return false;
		}
		if (a.get("shounin_shori_kengen_no") != null && !a.get("shounin_shori_kengen_no").equals(b.get("shounin_shori_kengen_no")))
		{
			return false;
		}
		return true;
	}
	
	//202207 追加
	/**
	 * 既に添付伝票として他の伝票に紐付けされているか
	 * @param denpyouId 伝票ID
	 * @return 紐付状態
	 */
    @Deprecated
	public boolean loadTaDenpyouHImoduki(String denpyouId) {
		final String sql = "SELECT * FROM kanren_denpyou kd WHERE kd.kanren_denpyou = ?;";
		return connection.load(sql, denpyouId).size() == 0 ? false : true;
	}
	/**
	 * 既に仮払伝票として紐付けされているか
	 * @param denpyouId 伝票ID
	 * @return 紐付状態
	 */
    @Deprecated
	public boolean loadKaribaraiSeisanHImoduki(String denpyouId) {
		final String sql = "SELECT * FROM denpyou_ichiran di WHERE di.karibarai_denpyou_id = ?;";
		return connection.load(sql, denpyouId).size() == 0 ? false : true;
	}
	/**
	 * 起案伝票として既に紐付けされているか
	 * @param denpyouId 伝票ID
	 * @return 紐付状態
	 */
    @Deprecated
	public boolean loadKianDenpyouHImoduki(String denpyouId) {
		final String sql = "SELECT dkh.denpyou_id FROM denpyou_kian_himozuke dkh WHERE dkh.kian_denpyou = ?;";
		return connection.load(sql, denpyouId).size() == 0 ? false : true;
	}
	/**
	 * 仕訳連携済みか確認する
	 * @param denpyouId 伝票ID
	 * @return 連携状態
	 */
    @Deprecated
	public boolean loadShiwakezumi(String denpyouId) {
		final String sql = "SELECT sj.denpyou_id FROM shounin_joukyou sj WHERE (sj.joukyou = 'OPEN21転記' OR sj.joukyou = 'データ抽出') AND sj.denpyou_id = ?;";
		return connection.load(sql, denpyouId).size() == 0 ? false : true;
	}
	/**
	 * 伝票の最新承認状況を取得
	 * @param denpyouId 伝票ID
	 * @return　伝票状態
	 */
	public GMap findDenpyouJoukyou(String denpyouId) {
		final String sql = "SELECT * FROM shounin_joukyou sj WHERE sj.denpyou_id = ? AND sj.edano = (SELECT MAX(ssj.edano) FROM shounin_joukyou ssj WHERE ssj.denpyou_id = ?)";
		return connection.find(sql, denpyouId ,denpyouId);
// return mp == null ? "" : mp.get("joukyou").toString();
	}
}
