package eteam.gyoumu.masterkanri;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.RegAccess;
import eteam.common.select.MasterKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * マスターデータ一覧画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class MasterDataIchiranAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜画面入力以外＞
	/** マスターデータ一覧 */
	List<GMap> masterDataList;

//＜入力チェック＞※入力項目が無いので使用しない。
	@Override protected void formatCheck() {}
	@Override protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		//1.入力チェック
		try(EteamConnection connection = EteamConnection.connect()){
			MasterKanriCategoryLogic masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
			
			//2.データ存在チェック
			//3.アクセス可能チェック
			// なし
			
			//4.処理
			
			// ヴァージョンが最新のマスターのリストを取得
			masterDataList = masterLogic.selectMasterDataList();
			if(masterDataList.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}
			
			//WF本体オフなら一部項目除外
			if(!RegAccess.checkEnableKeihiSeisan()) {
				List<String> jyogaiList = new ArrayList<String>();
				jyogaiList.add("furikomi_bi_rule_hi"); //振込日ルール（日付）
				jyogaiList.add("furikomi_bi_rule_youbi"); //振込日ルール（曜日）
				jyogaiList.add("kaigai_koutsuu_shudan_master"); //海外交通手段マスター
				jyogaiList.add("kaigai_nittou_nado_master"); //海外用日当等マスター
				jyogaiList.add("kani_todoke_select_item"); //届出ジェネレータ選択項目
				jyogaiList.add("kinyuukikan"); //金融機関
				jyogaiList.add("open21_kinyuukikan"); //金融機関
				jyogaiList.add("koutsuu_shudan_master"); //国内用交通手段マスター
				jyogaiList.add("moto_kouza"); //振込元口座
				jyogaiList.add("moto_kouza_shiharaiirai"); //振込元口座（支払依頼）
				jyogaiList.add("nittou_nado_master"); //国内用日当等マスター
				jyogaiList.add("shain_kouza"); //社員口座
				
				Iterator<GMap> itr = masterDataList.iterator();
				while(itr.hasNext()){
					GMap data = itr.next();
					if (jyogaiList.contains(data.get("master_id"))) {
						itr.remove();
					}
				}
			}

			// 表示形式変換
			for(GMap map : masterDataList) {
				map.put("koushin_time", formatTime(map.get("koushin_time")));
			}
			
			//5.戻り値を返す
			return "success";
		}
	}
}