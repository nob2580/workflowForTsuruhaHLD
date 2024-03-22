package eteam.gyoumu.masterkanri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.database.dao.KiHeishuMasterKyotenDao;
import eteam.database.dto.KiHeishuMasterKyoten;

/**
 * マスターデータチェック機能Logic
 * 複数Actionで利用するマスタ関連サービスクラス
 * 主にチェック処理を内包
 */
public class MasterDataServiceLogic extends EteamAbstractLogic {
	
	/**
	 * マスタ毎個別のチェックを行う。
	 * @param checkList データリスト(Map<String,String>形式リスト)
	 * @param masterIdTmp  マスタID
	 * @return エラーある場合はリスト
	 */
	public List<String> kobetsuCheck(List<GMap> checkList, String masterIdTmp){
		
		List<String> errorList = new ArrayList<String>();

		//起案番号簿マスタ
		if (masterIdTmp.equals("kian_bangou_bo")){
			List<String> errList = checkBangouBo(checkList);
			if (!errList.isEmpty()) {
				errorList.addAll(errList);
			}
		}
		
		//金融機関マスタ
		if (masterIdTmp.equals("kinyuukikan")) {
			return checkKinyuukikan(checkList);
		}

		//交通手段マスタ
		if (masterIdTmp.equals("koutsuu_shudan_master")) {
			return checkKoutsuuShudanMaster(checkList);
		}

		//日当等マスタ
		if (masterIdTmp.equals("nittou_nado_master")) {
			return checkNittouNadoMaster(checkList);
		}

		//海外交通手段マスタ
		if (masterIdTmp.equals("kaigai_koutsuu_shudan_master")) {
			return checkKaigaiKoutsuuShudanMaster(checkList);
		}

		//海外日当等マスタ
		if (masterIdTmp.equals("kaigai_nittou_nado_master")){
			return checkKaigaiNittouNadoMaster(checkList);
		}

		//振込元口座マスタ
		if (masterIdTmp.equals("moto_kouza")){
			List<String> errList = checkMotoKouza(checkList);
			if (!errList.isEmpty()) {
				errorList.addAll(errList);
			}
		}
		
		//振込元口座（支払依頼）マスタ
		if (masterIdTmp.equals("moto_kouza_shiharaiirai")){
			List<String> errList = checkMotoKouzaShiharaiirai(checkList);
			if (!errList.isEmpty()) {
				errorList.addAll(errList);
			}
		}
		
		//社員口座マスタ
		if (masterIdTmp.equals("shain_kouza")){
			List<String> errList = checkShainKouza(checkList);
			if (!errList.isEmpty()) {
				errorList.addAll(errList);
			}
		}
		
		//幣種別レートマスター(拠点)
		if (masterIdTmp.equals("rate_master_kyoten")) {
			List<String> errList = checkRateMasterKyoten(checkList);
			if (!errList.isEmpty()) {
				errorList.addAll(errList);
			}
		}
		
		//消費税率マスタ
		if (masterIdTmp.equals("shouhizeiritsu")){
			List<String> errList = checkShouhizeiritsu(checkList);
			if (!errList.isEmpty()) {
				errorList.addAll(errList);
			}
		}
		
		return errorList;
	}

	/**
	 * マスターから名称を取り直す
	 * @param input 入力内容
	 * @param masterId 対象マスターId
	 */
	public void reloadMaster(List<GMap> input, String masterId) {
		if(masterId.equals("kaigai_nittou_nado_master")) {
			reloadHeishuMaster(input);
		}
	}
	
	/**
	 * 幣種マスターから名称等を取り直す
	 * @param input 入力内容
	 */
	protected void reloadHeishuMaster(List<GMap> input) {
		MasterKanriCategoryLogic logic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		for (GMap row : input) {
			GMap heishuMap = logic.findHeishuCd(row.get("heishu_cd"));
			if(null!=heishuMap) row.put("currency_unit", heishuMap.get("currency_unit"));
		}
	}
	
	/**
	 * 起案番号簿の単一行チェック
	 * @param map 起案番号簿
	 * @return エラーある場合はリスト
	 */
	public List<String> checkBangouBoSingle(GMap map){
		List<String> errorList = new ArrayList<String>();
		//起案番号簿なら起案番号開始・終了の逆転チェック
		if(Integer.parseInt(map.get("kian_bangou_to")) < Integer.parseInt(map.get("kian_bangou_from")) ){
			errorList.add("開始起案番号は終了起案番号以下の値を指定してください。");
		}
		return errorList;
	}

	/**
	 * 渡された起案番号簿リストについてデータ整合性・重複チェックを実施します。
	 * @param checkList 起案番号簿リスト(bumon_cd,nendo,ryakugou,kian_bangou_from,kian_bangou_toがあればよい)
	 * @return エラーある場合はリスト
	 */
	protected List<String> checkBangouBo(List<GMap> checkList){
		List<String> errorList = new ArrayList<String>();
		
		// 起案番号開始・終了の逆転チェック
		for(GMap map : checkList){
			if(Integer.parseInt(map.get("kian_bangou_to")) < Integer.parseInt(map.get("kian_bangou_from")) ){
				errorList.add("["+ map.get("bumon_cd") +"]["+ map.get("nendo") +"]["+ map.get("ryakugou") +"]：開始起案番号は終了起案番号以下の値を指定してください。");
			}
		}
		if(errorList.size() > 0){
			return errorList;
		}
		
		LinkedHashSet<Map<String, String>> bumonHashSet = new LinkedHashSet<Map<String, String>>();
		LinkedHashSet<Map<String, String>> bumonSubHashSet = new LinkedHashSet<Map<String, String>>();
		
		// 同一部門コード・年度・略号の起案番号範囲重複チェック
		for(GMap map : checkList){
			Map<String, String> bumonMap = new HashMap<String, String>();
			bumonMap.put("bumon_cd",               map.get("bumon_cd"));
			bumonMap.put("nendo",                  map.get("nendo"));
			bumonMap.put("ryakugou",               map.get("ryakugou"));
			Map<String, String> bumonSubMap = new HashMap<String, String>();
			bumonSubMap.put("bumon_cd",            map.get("bumon_cd"));
			bumonSubMap.put("nendo",               map.get("nendo"));
			bumonSubMap.put("ryakugou",            map.get("ryakugou"));
			bumonSubMap.put("kian_bangou_from",    map.get("kian_bangou_from"));
			bumonSubMap.put("kian_bangou_to",      map.get("kian_bangou_to"));
			if(bumonHashSet.contains(bumonMap)) {
				for (Map<String,String>chkmap : bumonSubHashSet) {
					if (chkmap.get("bumon_cd").equals(bumonSubMap.get("bumon_cd")) && 
						chkmap.get("nendo").equals(bumonSubMap.get("nendo")) && 
						chkmap.get("ryakugou").equals(bumonSubMap.get("ryakugou"))){
						if(  !( 
								Integer.parseInt(bumonSubMap.get("kian_bangou_from")) > Integer.parseInt(chkmap.get("kian_bangou_to"))  
							 || Integer.parseInt(chkmap.get("kian_bangou_from")) > Integer.parseInt(bumonSubMap.get("kian_bangou_to"))  
							 ) ){
									errorList.add("["+ map.get("bumon_cd") +"]["+ map.get("nendo") +"]["+ map.get("ryakugou") +"]：同一部門コード・年度・略号の起案番号範囲が重複しています。");
									break;
						}
					}
				}
			}
			bumonHashSet.add(bumonMap);
			bumonSubHashSet.add(bumonSubMap);
		}
		return errorList;
	}
	
	/**
	 * 交通手段マスタの単一行チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @return エラーリスト
	 */
	public List<String> checkKoutsuuShudanMaster(GMap input, String msg) {
		List<String> errorList = new ArrayList<String>();
		checkEdabanCd(input,msg,errorList);
		checkZeiKbn(input,msg,errorList);
		checkSuuryouNyuryokuType(input,msg,errorList);
		return errorList;
	}
	/**
	 * 交通手段マスタの全行チェック
	 * @param list 入力内容
	 * @return エラーリスト
	 */
	public List<String> checkKoutsuuShudanMaster(List<GMap> list) {
		List<String> errorList = new ArrayList<String>();
		for(GMap input : list) {
			String msg = String.format("[%s][%s]:", input.getString("sort_jun"), input.get("koutsuu_shudan"));
			errorList.addAll(checkKoutsuuShudanMaster(input, msg));
		}
		return errorList;
	}
	
	/**
	 * 日当等マスタの単一行チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @return エラーリスト
	 */
	public List<String> checkNittouNadoMaster(GMap input, String msg) {
		List<String> errorList = new ArrayList<String>();
		checkEdabanCd(input,msg,errorList);
		checkZeiKbn(input,msg,errorList);
		return errorList;
	}
	/**
	 * 日当等マスタの全行チェック
	 * @param list 入力内容
	 * @return エラーリスト
	 */
	public List<String> checkNittouNadoMaster(List<GMap> list) {
		List<String> errorList = new ArrayList<String>();
		for(GMap input : list) {
			String msg = String.format("[%s][%s][%s]:", input.get("shubetsu1"), input.get("shubetsu2"), input.get("yakushoku_cd"));
			errorList.addAll(checkNittouNadoMaster(input, msg));
		}
		return errorList;
	}
	
	/**
	 * 海外交通手段マスタの単一行チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @return エラーリスト
	 */
	public List<String> checkKaigaiKoutsuuShudanMaster(GMap input, String msg) {
		List<String> errorList = new ArrayList<String>();
		checkEdabanCd(input,msg,errorList);
		checkZeiKbn(input,msg,errorList);
		return errorList;
	}
	/**
	 * 海外交通手段マスタの全行チェック
	 * @param list 入力内容
	 * @return エラーリスト
	 */
	public List<String> checkKaigaiKoutsuuShudanMaster(List<GMap> list) {
		List<String> errorList = new ArrayList<String>();
		for(GMap input : list) {
			String msg = String.format("[%s][%s]:", input.getString("sort_jun"), input.get("koutsuu_shudan"));
			errorList.addAll(checkKaigaiKoutsuuShudanMaster(input, msg));
		}
		return errorList;
	}
	
	/**
	 * 海外日当等マスタの単一行チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @return エラーリスト
	 */
	public List<String> checkKaigaiNittouNadoMaster(GMap input, String msg) {
		List<String> errorList = new ArrayList<String>();
		String heishuCd = input.getString("heishu_cd");
		String tankaGaika = input.getString("tanka_gaika");
		if(0==heishuCd.length() && 0!=tankaGaika.length()) {
			errorList.add(msg + "単価（外貨）を登録する場合は幣種コードの登録も必要です。");
		}
		if( !"2".equals(input.get("nittou_shukuhakuhi_flg")) ) {
			if(0 != heishuCd.length() || 0 != tankaGaika.length()) {
				errorList.add(msg + "日当でない場合は幣種コード、通貨単位、単価（外貨）の登録はできません。");
			}
		}
		checkZeiKbn(input,msg,errorList);
		checkEdabanCd(input,msg,errorList);
		
		return errorList;
		
	}
	/**
	 * 海外日当等マスタの全行チェック
	 * @param checkList 入力内容
	 * @return エラーリスト
	 */
	public List<String> checkKaigaiNittouNadoMaster(List<GMap> checkList){
		List<String> errorList = new ArrayList<String>();
		MasterKanriCategoryLogic logic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		
		for(GMap map : checkList) {
			String msg = "[" + map.get("shubetsu1") +"][" + map.get("shubetsu2") + "][" + map.get("yakushoku_cd") + "]：";
			List<String> errList = checkKaigaiNittouNadoMaster(map, msg);
			if (!errList.isEmpty()) errorList.addAll(errList);

			String heishuCd = map.get("heishu_cd");
			if (isEmpty(heishuCd))
			{
				continue;
			}
			if(null== logic.findHeishuCd(heishuCd)) {
				errorList.add(msg + "幣種コードが登録されていません。先に幣種マスターに幣種コードを登録してください。");
			}
		}
		return errorList;
	}
	
	
	/**
	 * 金融機関マスタの単一行チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @return エラーリスト
	 */
	public List<String> checkKinyuukikan(GMap input, String msg) {
		List<String> errorList = new ArrayList<String>();
		String kinyuukikanCd  = input.getString("kinyuukikan_cd");
		String kinyuukikanShitenCd  = input.getString("kinyuukikan_shiten_cd");
		hankakuKetasuuCheck(kinyuukikanCd, msg + "金融機関コード", 4, errorList);
		hankakuKetasuuCheck(kinyuukikanShitenCd,msg + "金融機関支店コード", 3, errorList);
		return errorList;
	}
	
	/**
	 * 渡されたリストについてデータ整合性・重複チェックを実施します。
	 * @param checkList 金融機関リスト
	 * @return エラーある場合はリスト
	 */
	protected List<String> checkKinyuukikan(List<GMap> checkList){
		List<String> errorList = new ArrayList<String>();
		for(GMap map : checkList){
			String msg = "[" + map.get("kinyuukikan_cd") +"][" + map.get("kinyuukikan_shiten_cd") + "]：";
			List<String> errList = checkKinyuukikan(map,msg);
			if (!errList.isEmpty()) errorList.addAll(errList);
		}
		return errorList;
	}
	
	/**
	 * 振込元口座マスタの単一行チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @return エラーリスト
	 */
	public List<String> checkMotoKouza(GMap input, String msg) {
		List<String> errorList = new ArrayList<String>();
		String kinyuukikanCd  = input.getString("moto_kinyuukikan_cd");
		String kinyuukikanShitenCd  = input.getString("moto_kinyuukikan_shiten_cd");
		String kouzaBangou  = input.getString("moto_kouza_bangou");
		hankakuKetasuuCheck(kinyuukikanCd, msg + "振込元金融機関コード", 4, errorList);
		hankakuKetasuuCheck(kinyuukikanShitenCd,msg + "振込元金融機関支店コード", 3, errorList);
		hankakuKetasuuCheck(kouzaBangou, msg + "振込元口座番号", 7, errorList);
		return errorList;
	}
	
	/**
	 * 渡されたリストについてデータ整合性・重複チェックを実施します。
	 * @param checkList 振込元口座リスト
	 * @return エラーある場合はリスト
	 */
	protected List<String> checkMotoKouza(List<GMap> checkList){
		
		MasterKanriCategoryLogic logic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		List<GMap> kinyuukikan = logic.loadKinyuukikan();
		List<String> errorList = new ArrayList<String>();
		for(GMap map : checkList){
			String msg = "["+ map.get("moto_kinyuukikan_cd") +"]["+ map.get("moto_kinyuukikan_shiten_cd") +"]：";
			List<String> errList = checkMotoKouza(map,msg);
			if (!errList.isEmpty()) errorList.addAll(errList);
			if (!kinyuukikan.stream().anyMatch(k -> k.get("kinyuukikan_cd").equals(map.get("moto_kinyuukikan_cd"))
					&& k.get("kinyuukikan_shiten_cd").equals(map.get("moto_kinyuukikan_shiten_cd")))){
				errorList.add(msg + "振込元金融機関コードと振込元金融機関支店コードの組み合わせは金融機関に存在しません。"); 
			};
		}
		return errorList;
	}
	
	/**
	 * 振込元口座（支払依頼）マスタの単一行チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @return エラーリスト
	 */
	public List<String> checkMotoKouzaShiharaiirai(GMap input, String msg) {
		List<String> errorList = new ArrayList<String>();
		String kinyuukikanCd  = input.getString("moto_kinyuukikan_cd");
		String kinyuukikanShitenCd  = input.getString("moto_kinyuukikan_shiten_cd");
		String kouzaBangou  = input.getString("moto_kouza_bangou");
		hankakuKetasuuCheck(kinyuukikanCd, msg + "振込元金融機関コード", 4, errorList);
		hankakuKetasuuCheck(kinyuukikanShitenCd,msg + "振込元金融機関支店コード", 3, errorList);
		hankakuKetasuuCheck(kouzaBangou, msg + "振込元口座番号", 7, errorList);
		return errorList;
	}
	
	/**
	 * 渡されたリストについてデータ整合性・重複チェックを実施します。
	 * @param checkList 振込元口座（支払依頼）リスト
	 * @return エラーある場合はリスト
	 */
	protected List<String> checkMotoKouzaShiharaiirai(List<GMap> checkList){
		
		MasterKanriCategoryLogic logic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		List<GMap> kinyuukikan = logic.loadOpen21Kinyuukikan();
		List<String> errorList = new ArrayList<String>();
		for(GMap map : checkList){
			String msg = "["+ map.get("moto_kinyuukikan_cd") +"]["+ map.get("moto_kinyuukikan_shiten_cd") +"]：";
			List<String> errList = checkMotoKouzaShiharaiirai(map,msg);
			if (!errList.isEmpty()) errorList.addAll(errList);
			if (!kinyuukikan.stream().anyMatch(k -> k.get("kinyuukikan_cd").equals(map.get("moto_kinyuukikan_cd"))
					&& k.get("kinyuukikan_shiten_cd").equals(map.get("moto_kinyuukikan_shiten_cd")))){
				errorList.add(msg + "振込元金融機関コードと振込元金融機関支店コードの組み合わせは金融機関(open21_kinnyuukikan)に存在しません。");
			};
		}
		return errorList;
	}
	
	/**
	 * 社員口座マスタの単一行チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @return エラーリスト
	 */
	public List<String> checkShainKouza(GMap input, String msg) {
		List<String> errorList = new ArrayList<String>();
		String sakiKinyuukikanCd  = input.getString("saki_kinyuukikan_cd");
		String sakiKinyuukikanShitenCd  = input.getString("saki_ginkou_shiten_cd");
		String sakiKouzaBangou  = input.getString("saki_kouza_bangou");
		String motoKinyuukikanCd  = input.getString("moto_kinyuukikan_cd");
		String motoKinyuukikanShitenCd  = input.getString("moto_kinyuukikan_shiten_cd");
		String motoKouzaBangou  = input.getString("moto_kouza_bangou");
		hankakuKetasuuCheck(sakiKinyuukikanCd, msg + "振込先金融機関銀行コード", 4, errorList);
		hankakuKetasuuCheck(sakiKinyuukikanShitenCd,msg + "振込先銀行支店コード", 3, errorList);
		hankakuKetasuuCheck(sakiKouzaBangou, msg + "振込先口座番号", 7, errorList);
		hankakuKetasuuCheck(motoKinyuukikanCd, msg + "振込元金融機関コード", 4, errorList);
		hankakuKetasuuCheck(motoKinyuukikanShitenCd,msg + "振込元金融機関支店コード", 3, errorList);
		hankakuKetasuuCheck(motoKouzaBangou, msg + "振込元口座番号", 7, errorList);
		return errorList;
	}
	
	/**
	 * 渡されたリストについてデータ整合性・重複チェックを実施します。
	 * @param checkList 社員口座リスト
	 * @return エラーある場合はリスト
	 */
	protected List<String> checkShainKouza(List<GMap> checkList){
		
		MasterKanriCategoryLogic logic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		List<GMap> kinyuukikan = logic.loadKinyuukikan();
		List<GMap> motoKouza = logic.loadMotoKouza();
		List<String> errorList = new ArrayList<String>();
		for(GMap map : checkList){
			String msg = "["+ map.get("shain_no") +"]：";
			List<String> errList = checkShainKouza(map,msg);
			if (!errList.isEmpty()) errorList.addAll(errList);
			if (logic.findShain(map.get("shain_no")) == null){
				errorList.add(msg + "社員番号は社員に存在しません。"); 
			};
			
			if (!kinyuukikan.stream().anyMatch(k -> k.get("kinyuukikan_cd").equals(map.get("saki_kinyuukikan_cd"))
					&& k.get("kinyuukikan_shiten_cd").equals(map.get("saki_ginkou_shiten_cd")))){
				errorList.add("["+ map.get("saki_kinyuukikan_cd") +"]["+ map.get("saki_ginkou_shiten_cd") +"]：振込先金融機関銀行コードと振込先銀行支店コードの組み合わせは金融機関に存在しません。"); 
			};
			
			if (!motoKouza.stream().anyMatch(k -> k.get("moto_kinyuukikan_cd").equals(map.get("moto_kinyuukikan_cd"))
					&& k.get("moto_kinyuukikan_shiten_cd").equals(map.get("moto_kinyuukikan_shiten_cd"))
					&& k.get("moto_yokinshubetsu").equals(map.get("moto_yokinshubetsu"))
					&& k.get("moto_kouza_bangou").equals(map.get("moto_kouza_bangou")))){
				errorList.add("["+ map.get("moto_kinyuukikan_cd") +"]["+ map.get("moto_kinyuukikan_shiten_cd") +"]["+ map.get("moto_yokinshubetsu") +"]["+ map.get("moto_kouza_bangou") +"]：振込元金融機関コード・振込元金融機関支店コード・振込元預金種別・振込元口座番号の組み合わせは振込元口座に存在しません。"); 
			};
		}
		return errorList;
	}
	
	
	/**
	 * 消費税率マスタの単一行チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @return エラーリスト
	 */
	public List<String> checkShouhizeiritsu(GMap input, String msg) {
		List<String> errorList = new ArrayList<String>();
		checkHasuuKeisanKbn(input,msg,errorList);
		return errorList;
	}
	/**
	 * 消費税率マスタの全行チェック
	 * @param list 入力内容
	 * @return エラーリスト
	 */
	public List<String> checkShouhizeiritsu(List<GMap> list) {
		List<String> errorList = new ArrayList<String>();
		for(GMap input : list) {
			String msg = String.format("[%s][%s]:", input.get("sort_jun"), input.get("zeiritsu"));
			errorList.addAll(checkShouhizeiritsu(input, msg));
		}
		return errorList;
	}
	
	/**
	 * 渡されたリストについてデータ整合性・重複チェックを実施します。
	 * @param checkList 幣種別レートマスター(拠点)リスト
	 * @return エラーある場合はリスト
	 */
	protected List<String> checkRateMasterKyoten(List<GMap> checkList){
		
		KiHeishuMasterKyotenDao heishuKyotenDao = EteamContainer.getComponent(KiHeishuMasterKyotenDao.class, connection);
		List<KiHeishuMasterKyoten> heishuList = heishuKyotenDao.load();
		List<String> errorList = new ArrayList<String>();
		for(GMap map : checkList){
			String msg = "["+ map.get("heishu_cd") +"]：";
			if( !heishuList.stream().anyMatch(dto -> dto.getHeishuCd().equals(map.get("heishu_cd"))) ){
				errorList.add(msg + "幣種コードは（期別）幣種マスター(拠点)に存在しません。");
			}
		}
		return errorList;
	}
	
	/**
	 * 枝番コードチェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @param errorList エラーリスト
	 */
	protected void checkEdabanCd(GMap input, String msg, List<String> errorList) {
		String edaban = input.getString("edaban");
		if(0 != edaban.length()) {
			if(12 < edaban.length() || edaban.matches(".*[^0-9A-Z-/].*")){
				errorList.add(msg + "枝番コードは、半角英数字「Ａ～Ｚ(大文字)」、「0～9」、「-(ハイフン)、/(スラッシュ)」12文字以内で登録してください。");
			}
		}
	}
	
	/**
	 * 数量入力タイプの必須チェック及び形式チェック、数量入力タイプによる単価・数量記号の相関チェック及び単価の形式チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @param errorList エラーリスト
	 */
	protected void checkSuuryouNyuryokuType(GMap input, String msg, List<String> errorList) {
		String suuryouNyuryokuType = input.getString("suuryou_nyuryoku_type");
		String tanka = input.getString("tanka");
		String suuryouKigou = input.getString("suuryou_kigou");
		
		//数量入力タイプの必須チェック及び形式チェック(値の範囲はMasterColumnInfoのcheckInputでは見ていないので)
		if(StringUtils.isEmpty(suuryouNyuryokuType) || (suuryouNyuryokuType.length() != 1 || !suuryouNyuryokuType.matches("[0-2]"))) {
			errorList.add(msg + "数量入力タイプを半角数字「0～2」1文字で登録してください。");
		}else {
			//数量入力タイプによる単価・数量記号の相関チェック
			if(suuryouNyuryokuType.equals("0") && (StringUtils.isNotEmpty(tanka) || StringUtils.isNotEmpty(suuryouKigou))) {
				errorList.add(msg + "数量入力タイプが「0」の場合、単価と数量記号は登録できません。");
			}else if(suuryouNyuryokuType.equals("1")){
				if (StringUtils.isEmpty(tanka) || StringUtils.isEmpty(suuryouKigou)) {
					errorList.add(msg + "数量入力タイプが「1」の場合、単価と数量記号を登録してください。");
				//単価の形式チェック(値の範囲はMasterColumnInfoのcheckInputでは見ていないので) 
				}else if(StringUtils.isNotEmpty(tanka) && (Double.parseDouble(tanka) < 0.001 || 999999999999.999 < Double.parseDouble(tanka))) {
					errorList.add(msg + "単価は、半角数字0.001～999999999999.999の範囲で登録してください。");
				}
			}else if(suuryouNyuryokuType.equals("2")) {
				if(StringUtils.isEmpty(suuryouKigou)) {
					errorList.add(msg + "数量入力タイプが「2」の場合、数量記号を登録してください。");
				}
				if(StringUtils.isNotEmpty(tanka)) {
					errorList.add(msg + "数量入力タイプが「2」の場合、単価は登録できません。");
				}
			}
		}
	}
	
	/**
	 * 数量入力タイプの必須チェック及び形式チェック、数量入力タイプによる単価・数量記号の相関チェック及び単価の形式チェック
	 * @param value  入力内容
	 * @param msg  識別用メッセージ
	 * @param keta  桁数
	 * @param errorList エラーリスト
	 */
	protected void hankakuKetasuuCheck(String value, String msg, int keta, List<String> errorList) {
		String regex = "[0-9]{" + Integer.toString(keta) + "}";
		if(!value.matches(regex)){
			errorList.add(msg + "は半角数字" + keta + "桁で入力してください。");
		}
	}
	
	/**
	 * 端数計算区分チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @param errorList エラーリスト
	 */
	protected void checkHasuuKeisanKbn(GMap input, String msg, List<String> errorList) {
	String hasuuKeisanKbn = input.getString("hasuu_keisan_kbn");
		if(!"3".equals(hasuuKeisanKbn)){
			errorList.add(msg + "端数計算区分は「3」を指定してください。");
		}
	}
	
	/**
	 * 税区分チェック
	 * @param input 入力内容
	 * @param msg 識別用メッセージ
	 * @param errorList エラーリスト
	 */
	protected void checkZeiKbn(GMap input, String msg, List<String> errorList) {
		String zeiKbn = input.getString("zei_kubun");
		String regex = "^[0-4]$";
		if (!"".equals(regex) && StringUtils.isNotEmpty(zeiKbn) && !Pattern.compile(regex).matcher(zeiKbn).find()) {
			errorList.add(msg + "税区分は半角数字「0～4」で登録してください。");
		}
	}
	
}