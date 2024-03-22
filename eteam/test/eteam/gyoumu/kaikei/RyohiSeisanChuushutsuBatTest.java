package eteam.gyoumu.kaikei;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.EteamThreadMap;
import eteam.common.EteamConst.SYSTEM_PROP;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamSettingInfo.Key;
import eteam.gyoumu.system.SystemKanriLogic;

/**
 * 旅費精算申請の仕訳テスト
 */
public class RyohiSeisanChuushutsuBatTest {

	//実行時切替
	/** ログインユーザー */
	protected static final String USER = "saito_mariko";
	/** 業務ロールID(経理承認) */
	protected static final String GYOUMU_ROLE = "00009";
	
	/** URL */
	protected static final String URL = "C:\\eteam\\tmp\\";
	/** CSVのファイル数 */
	protected static final int CSV_NUMS = 4; //TODO: 暫定 必要に応じ変更
	
	/** 明細数 */
	protected static final int MEISAI_NUMS = 2; //TODO: 暫定 必要に応じ変更
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(KeihiTatekaeSeisanChuushutsuBatTest.class);
	
	/**
	 * main
	 * @param args 引数
	 */
	public static void main(String[] args) {

		//--------------------
		//前処理
		//--------------------
		// UT用のログ出力プロパティを読み込みます。
		PropertyConfigurator.configure(log.getClass().getResourceAsStream("/batlog4j.properties"));

		// スキーマ指定
		if (1 != args.length) {
			throw new IllegalArgumentException("パラメータにスキーマ名が指定されていません。");
		}
		Map<String, String> threadMap = EteamThreadMap.get();
		threadMap.put(SYSTEM_PROP.SCHEMA, args[0]);

		// プロパティの設定
		System.setProperty("ut_user", USER);

		//--------------------
		// CSVからテストデータを読み込む
		//--------------------
		List<Map<String, Object>> testDataList = readCsvFiles();
		int caseNo = 1;

		//--------------------
		//テストデータのCSV1行ずつ起票～承認
		//--------------------
		for(Map<String, Object> testData : testDataList){
			//--------------------
			// 設定変更
			//--------------------
			try(EteamConnection connection = EteamConnection.connect()){
				SystemKanriLogic sLogic = EteamContainer.getComponent(SystemKanriLogic.class, connection);

				sLogic.updateSettingInfo(Key.OP21MPARAM_DENPYOU_KEISHIKI_A004 , (String)testData.get(Key.OP21MPARAM_DENPYOU_KEISHIKI_A004));
				sLogic.updateSettingInfo(Key.SHIWAKE_SAKUSEI_HOUHOU_A004 , (String)testData.get(Key.SHIWAKE_SAKUSEI_HOUHOU_A004));
				sLogic.updateSettingInfo(Key.ZANKIN_SHIWAKE_SAKUSEI_UMU_A004 , (String)testData.get(Key.ZANKIN_SHIWAKE_SAKUSEI_UMU_A004));
				connection.commit();
			}

			//--------------------
			// 旅費仮払申請の作成
			//--------------------
			RyohiKaribaraiShinseiAction ryohiKaribaraiAction = null;
			if(testData.get("ryohikaribarai") != null){
				// 一般ユーザーでログイン
				System.setProperty("ut_gyoumu_role", "");
				//新規起票
				ryohiKaribaraiAction = new RyohiKaribaraiShinseiAction();
				ryohiKaribaraiAction.setDenpyouKbn(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI);
				if(!ryohiKaribaraiAction.init().equals("success")){
					System.out.println(ryohiKaribaraiAction.getErrorList());
					throw new RuntimeException();
				}
				
				//CSVデータを入力
				copyInputdata((RyohiKaribaraiShinseiAction)testData.get("ryohikaribarai"), ryohiKaribaraiAction);
				
				//CSVで仮払の入力があるなら
				if (!ryohiKaribaraiAction.getShiharaihouhou().equals("")){
		
					//登録して申請
					if(!ryohiKaribaraiAction.touroku().equals("success")){
						System.out.println(ryohiKaribaraiAction.getErrorList());
						throw new RuntimeException();
					}
					if(!ryohiKaribaraiAction.shinsei().equals("success")){
						System.out.println(ryohiKaribaraiAction.getErrorList());
						throw new RuntimeException();
					}
		
					// 経理ユーザーでログイン
					System.setProperty("ut_gyoumu_role", GYOUMU_ROLE);
					//支払日更新＆承認
	// karibaraiAction.setShiharaiBi(SHIHARAIBI);
					if(!ryohiKaribaraiAction.koushin().equals("success")){
						System.out.println(ryohiKaribaraiAction.getErrorList());
						throw new RuntimeException();
					}
					if(!ryohiKaribaraiAction.shounin().equals("success")){
						System.out.println(ryohiKaribaraiAction.getErrorList());
						throw new RuntimeException();
					}
				}
			}

			//--------------------
			// 旅費精算の作成
			//--------------------
			// 一般ユーザーでログイン
			System.setProperty("ut_gyoumu_role", "");
			//新規起票
			RyohiSeisanAction seisanAction = new RyohiSeisanAction();
			seisanAction.setDenpyouKbn(DENPYOU_KBN.RYOHI_SEISAN);
			if(!seisanAction.init().equals("success")){
				System.out.println(seisanAction.getErrorList());
				throw new RuntimeException();
			}

			//CSVで仮払の入力があるなら選択
			if (ryohiKaribaraiAction != null){
				seisanAction.setKaribaraiDenpyouId(ryohiKaribaraiAction.getDenpyouId());
				seisanAction.setKaribaraiTekiyou(ryohiKaribaraiAction.getTekiyouRyohi());
				seisanAction.setKaribaraiKingaku(ryohiKaribaraiAction.getKingaku());
				seisanAction.setKaribaraiOn("1");
			}else{
				seisanAction.setKaribaraiDenpyouId("");
				seisanAction.setKaribaraiOn("0");
			}
			
			//CSVデータを入力
			copyInputData((RyohiSeisanAction)testData.get("ryohiseisan"), seisanAction);

			//登録して申請
			if(!seisanAction.touroku().equals("success")){
				System.out.println(seisanAction.getErrorList());
				throw new RuntimeException();
			}
			if(!seisanAction.shinsei().equals("success")){
				System.out.println(seisanAction.getErrorList());
				throw new RuntimeException();
			}

			// 経理ユーザーでログイン
			System.setProperty("ut_gyoumu_role", GYOUMU_ROLE);

			//計上日＆支払日更新＆承認
// seisanAction.setKeijoubi(KEIJOUBI);
// seisanAction.setShiharaibi(SHIHARAIBI);
			if(seisanAction.getShiwakeEdaNo().length == 0){
			//仮払未使用の伝票で配列エラーになるためブランク使用日追加
				String[] tmp = {""};
				seisanAction.setShiyoubi(tmp);
			}
			if(!seisanAction.koushin().equals("success")){
				System.out.println(seisanAction.getErrorList());
				throw new RuntimeException();
			}
			if(!seisanAction.shounin().equals("success")){
				System.out.println(seisanAction.getErrorList());
				throw new RuntimeException();
			}
			
			
			//--------------------
			// 仕訳の作成
			//--------------------
			
			
			//TODO 有効化
			// 会社設定情報リロードのためスリープ
			try{Thread.sleep(SYSTEM_PROP.ONE_THREAD_MS + 1);}catch(InterruptedException e){}
			System.out.println("[" + (caseNo++) + "]");
			RyohiSeisanChuushutsuBat bat = EteamContainer.getComponent(RyohiSeisanChuushutsuBat.class);
			if(bat.mainProc() == 1){
				throw new RuntimeException();
			};
			//TODO 出力整理 不要ならDB参照ツールなどでCSVを直接出力させる
			//outputShiwake(seisanAction.getDenpyouId(),"" + (caseNo++) );
			
		}


	}

	/**
	 * c:\eteam\tmp配下のA004_1.csv～A004_4.csvを読み込む。
	 * @return ファイル×行×列
	 */
	protected static List<Map<String, Object>> readCsvFiles() {
		List<Map<String, Object>> ret = new ArrayList<>();
		try {
			for (int i = 0; i < CSV_NUMS; i++) {
				//CSVファイルを読み込む
	            FileInputStream input = new FileInputStream(URL + "A004_" + (i+1) + ".csv");  
	            InputStreamReader stream = new InputStreamReader(input,"SJIS");
	            BufferedReader br = new BufferedReader(stream);
	
				//ファイル終わり行まで処理
				int lineCount = 0;
				while(true){
					String line = br.readLine();
					if (line == null)
					{
						break;
					}
					if (++lineCount > 20)
					{
						break;
					} //TODO: 暫定 必要に応じ変更
					String[] columns = line.split(",",-1);
					
					Map<String, Object> testMap = new HashMap<>();
					int col = 0;
					
					//CSVから設定に
					testMap.put(Key.OP21MPARAM_DENPYOU_KEISHIKI_A004, columns[col++]);
					testMap.put(Key.SHIWAKE_SAKUSEI_HOUHOU_A004, columns[col++]);
					testMap.put(Key.ZANKIN_SHIWAKE_SAKUSEI_UMU_A004, columns[col++]);
					
					//CSVから旅費仮払アクションへ
					//本体部
					RyohiKaribaraiShinseiAction ryohiKaribaraiAction = new RyohiKaribaraiShinseiAction();
					ryohiKaribaraiAction.setUserIdRyohi(columns[col++]);
					ryohiKaribaraiAction.setUserNameRyohi(columns[col++]);
					ryohiKaribaraiAction.setShainNoRyohi(columns[col++]);
					ryohiKaribaraiAction.setKaribaraiOn(columns[col++]);
					ryohiKaribaraiAction.setDairiFlg(columns[col++]);
					ryohiKaribaraiAction.setHoumonsaki(columns[col++]);
					ryohiKaribaraiAction.setMokuteki(columns[col++]);
					ryohiKaribaraiAction.setSeisankikanFrom(columns[col++]);
					ryohiKaribaraiAction.setSeisankikanTo(columns[col++]);
					ryohiKaribaraiAction.setShiwakeEdaNoRyohi(columns[col++]);
					ryohiKaribaraiAction.setTorihikiNameRyohi(columns[col++]);
					ryohiKaribaraiAction.setKamokuCdRyohi(columns[col++]);
					ryohiKaribaraiAction.setKamokuNameRyohi(columns[col++]);
					ryohiKaribaraiAction.setKamokuEdabanCdRyohi(columns[col++]);
					ryohiKaribaraiAction.setKamokuEdabanNameRyohi(columns[col++]);
					ryohiKaribaraiAction.setFutanBumonCdRyohi(columns[col++]);
					ryohiKaribaraiAction.setFutanBumonNameRyohi(columns[col++]);
					ryohiKaribaraiAction.setHf1Cd(columns[col++]);
					ryohiKaribaraiAction.setHf1Name(columns[col++]);
					ryohiKaribaraiAction.setHf2Cd(columns[col++]);
					ryohiKaribaraiAction.setHf2Name(columns[col++]);
					ryohiKaribaraiAction.setHf3Cd(columns[col++]);
					ryohiKaribaraiAction.setHf3Name(columns[col++]);
					ryohiKaribaraiAction.setUf1CdRyohi(columns[col++]);
					ryohiKaribaraiAction.setUf1NameRyohi(columns[col++]);
					ryohiKaribaraiAction.setUf2CdRyohi(columns[col++]);
					ryohiKaribaraiAction.setUf2NameRyohi(columns[col++]);
					ryohiKaribaraiAction.setUf3CdRyohi(columns[col++]);
					ryohiKaribaraiAction.setUf3NameRyohi(columns[col++]);
					ryohiKaribaraiAction.setTekiyouRyohi(columns[col++]);
					ryohiKaribaraiAction.setShiharaiKiboubi(columns[col++]);
					ryohiKaribaraiAction.setShiharaihouhou(columns[col++]);//1:現金 2:振込
					ryohiKaribaraiAction.setShiharaibi(columns[col++]);
					ryohiKaribaraiAction.setHosoku(columns[col++]);
					ryohiKaribaraiAction.setKingaku(columns[col++]);
					ryohiKaribaraiAction.setKaribaraiKingaku(columns[col++]);
					
					
					
					
					
					
					//交通費・日当
					List<String> shubetsuCd =  new ArrayList<>();
					List<String> shubetsu1 =  new ArrayList<>();
					List<String> shubetsu2 =  new ArrayList<>();
					List<String> kikanFrom =  new ArrayList<>();
					List<String> kikanTo =  new ArrayList<>();
					List<String> kyuujitsuNissuu =  new ArrayList<>();
					List<String> shouhyouShoruiHissuFlg =  new ArrayList<>();
					List<String> koutsuuShudan =  new ArrayList<>();
					List<String> naiyou =  new ArrayList<>();
					List<String> bikou =  new ArrayList<>();
					List<String> oufukuFlg =  new ArrayList<>();
					List<String> jidounyuuryokuFlg =  new ArrayList<>();
					List<String> nissuu =  new ArrayList<>();
					List<String> tanka =  new ArrayList<>();
					List<String> meisaiKingaku =  new ArrayList<>();
					for(int kk = 0; kk < MEISAI_NUMS ; kk++ ){
						//データ無ければスキップ
						if(columns[col].equals("")){
							col += 15;
							continue;
						}
						kikanFrom .add(columns[col++]);
						kikanTo .add(columns[col++]);
						kyuujitsuNissuu .add(columns[col++]);
						shubetsuCd .add(columns[col++]);
						shubetsu1 .add(columns[col++]);
						shubetsu2 .add(columns[col++]);
						koutsuuShudan .add(columns[col++]);
						shouhyouShoruiHissuFlg .add(columns[col++]);
						naiyou .add(columns[col++]);
						bikou .add(columns[col++]);
						oufukuFlg .add(columns[col++]);
						jidounyuuryokuFlg .add(columns[col++]);
						nissuu .add(columns[col++]);
						tanka .add(columns[col++]);
						meisaiKingaku .add(columns[col++]);
						
						
					}
					ryohiKaribaraiAction.setKikanFrom(kikanFrom.toArray(new String[0]));
					ryohiKaribaraiAction.setKikanTo(kikanTo.toArray(new String[0]));
					ryohiKaribaraiAction.setKyuujitsuNissuu(kyuujitsuNissuu.toArray(new String[0]));
					ryohiKaribaraiAction.setShubetsuCd(shubetsuCd.toArray(new String[0]));
					ryohiKaribaraiAction.setShubetsu1(shubetsu1.toArray(new String[0]));
					ryohiKaribaraiAction.setShubetsu2(shubetsu2.toArray(new String[0]));
					ryohiKaribaraiAction.setKoutsuuShudan(koutsuuShudan.toArray(new String[0]));
					ryohiKaribaraiAction.setNaiyou(naiyou.toArray(new String[0]));
					ryohiKaribaraiAction.setBikou(bikou.toArray(new String[0]));
					ryohiKaribaraiAction.setOufukuFlg(oufukuFlg.toArray(new String[0]));
					ryohiKaribaraiAction.setJidounyuuryokuFlg(jidounyuuryokuFlg.toArray(new String[0]));
					ryohiKaribaraiAction.setNissuu(nissuu.toArray(new String[0]));
					ryohiKaribaraiAction.setTanka(tanka.toArray(new String[0]));
					ryohiKaribaraiAction.setMeisaiKingaku(meisaiKingaku.toArray(new String[0]));
					
					ryohiKaribaraiAction.setShouhyouShoruiHissuFlg(shouhyouShoruiHissuFlg.toArray(new String[0]));
					
					//その他経費
					List<String> shiwakeEdaNo =  new ArrayList<>();
					List<String> torihikiName =  new ArrayList<>();
					List<String> kamokuCd =  new ArrayList<>();
					List<String> kamokuName =  new ArrayList<>();
					List<String> kamokuEdabanCd =  new ArrayList<>();
					List<String> kamokuEdabanName =  new ArrayList<>();
					List<String> futanBumonCd =  new ArrayList<>();
					List<String> futanBumonName =  new ArrayList<>();
					List<String> torihikisakiCd =  new ArrayList<>();
					List<String> torihikisakiName =  new ArrayList<>();
					List<String> projectCd =  new ArrayList<>();
					List<String> projectName =  new ArrayList<>();
					List<String> uf1Cd =  new ArrayList<>();
					List<String> uf1Name =  new ArrayList<>();
					List<String> uf2Cd =  new ArrayList<>();
					List<String> uf2Name =  new ArrayList<>();
					List<String> uf3Cd =  new ArrayList<>();
					List<String> uf3Name =  new ArrayList<>();
					List<String> kazeiKbn =  new ArrayList<>();
					List<String> zeiritsu =  new ArrayList<>();
					List<String> shiyoubi =  new ArrayList<>();
					List<String> shouhyouShorui =  new ArrayList<>();
					List<String> shiharaiKingaku =  new ArrayList<>();
					List<String> hontaiKingaku =  new ArrayList<>();
					List<String> shouhizeigaku =  new ArrayList<>();
					List<String> tekiyou =  new ArrayList<>();
					List<String> shiwakeTekiyou =  new ArrayList<>();
					List<String> chuuki2 =  new ArrayList<>();
					List<String> kousaihiHyoujiFlg =  new ArrayList<>();
					List<String> kousaihiShousai =  new ArrayList<>();
					for(int kk = 0; kk < MEISAI_NUMS ; kk++ ){
						//データ無ければスキップ
						if(columns[col].equals("")){
							col += 26;
							continue;
						}
						shiyoubi .add(columns[col++]);
						shouhyouShorui .add(columns[col++]);
						shiwakeEdaNo .add(columns[col++]);
						torihikiName .add(columns[col++]);
						kamokuCd .add(columns[col++]);
						kamokuName .add(columns[col++]);
						kamokuEdabanCd .add(columns[col++]);
						kamokuEdabanName .add(columns[col++]);
						futanBumonCd .add(columns[col++]);
						futanBumonName .add(columns[col++]);
						projectCd .add(columns[col++]);
						projectName .add(columns[col++]);
						torihikisakiCd .add(columns[col++]);
						torihikisakiName .add(columns[col++]);
						uf1Cd .add(columns[col++]);
						uf1Name .add(columns[col++]);
						uf2Cd .add(columns[col++]);
						uf2Name .add(columns[col++]);
						uf3Cd .add(columns[col++]);
						uf3Name .add(columns[col++]);
						kazeiKbn .add(columns[col++]);
						zeiritsu .add(columns[col++]);
						shiharaiKingaku .add(columns[col++]);
						tekiyou .add(columns[col++]);
						kousaihiShousai .add(columns[col++]);
						kousaihiHyoujiFlg .add(columns[col++]);
						
						//下記2項目は空欄でよい
						hontaiKingaku.add("");
						shouhizeigaku.add("");
						
					}
					ryohiKaribaraiAction.setShiyoubi(shiyoubi.toArray(new String[0]));
					ryohiKaribaraiAction.setShouhyouShorui(shouhyouShorui.toArray(new String[0]));
					ryohiKaribaraiAction.setShiwakeEdaNo(shiwakeEdaNo.toArray(new String[0]));
					ryohiKaribaraiAction.setTorihikiName(torihikiName.toArray(new String[0]));
					ryohiKaribaraiAction.setKamokuCd(kamokuCd.toArray(new String[0]));
					ryohiKaribaraiAction.setKamokuName(kamokuName.toArray(new String[0]));
					ryohiKaribaraiAction.setKamokuEdabanCd(kamokuEdabanCd.toArray(new String[0]));
					ryohiKaribaraiAction.setKamokuEdabanName(kamokuEdabanName.toArray(new String[0]));
					ryohiKaribaraiAction.setFutanBumonCd(futanBumonCd.toArray(new String[0]));
					ryohiKaribaraiAction.setFutanBumonName(futanBumonName.toArray(new String[0]));
					ryohiKaribaraiAction.setProjectCd(projectCd.toArray(new String[0]));
					ryohiKaribaraiAction.setProjectName(projectName.toArray(new String[0]));
					ryohiKaribaraiAction.setTorihikisakiCd(torihikisakiCd.toArray(new String[0]));
					ryohiKaribaraiAction.setTorihikisakiName(torihikisakiName.toArray(new String[0]));
					ryohiKaribaraiAction.setUf1Cd(uf1Cd.toArray(new String[0]));
					ryohiKaribaraiAction.setUf1Name(uf1Name.toArray(new String[0]));
					ryohiKaribaraiAction.setUf2Cd(uf2Cd.toArray(new String[0]));
					ryohiKaribaraiAction.setUf2Name(uf2Name.toArray(new String[0]));
					ryohiKaribaraiAction.setUf3Cd(uf3Cd.toArray(new String[0]));
					ryohiKaribaraiAction.setUf3Name(uf3Name.toArray(new String[0]));
					ryohiKaribaraiAction.setKazeiKbn(kazeiKbn.toArray(new String[0]));
					ryohiKaribaraiAction.setZeiritsu(zeiritsu.toArray(new String[0]));
					ryohiKaribaraiAction.setShiharaiKingaku(shiharaiKingaku.toArray(new String[0]));
					ryohiKaribaraiAction.setTekiyou(tekiyou.toArray(new String[0]));
					ryohiKaribaraiAction.setKousaihiShousai(kousaihiShousai.toArray(new String[0]));
					ryohiKaribaraiAction.setKousaihiHyoujiFlg(kousaihiHyoujiFlg.toArray(new String[0]));
					
					ryohiKaribaraiAction.setHontaiKingaku(hontaiKingaku.toArray(new String[0]));
					ryohiKaribaraiAction.setShouhizeigaku(shouhizeigaku.toArray(new String[0]));

					if(!ryohiKaribaraiAction.getShiharaihouhou().equals("")){
						testMap.put("ryohikaribarai", ryohiKaribaraiAction);
					}
					
					//CSVから旅費精算アクションへ
					RyohiSeisanAction ryohiSeisanAction = new RyohiSeisanAction();
					//本体部
					ryohiSeisanAction.setUserIdRyohi(columns[col++]);
					ryohiSeisanAction.setUserNameRyohi(columns[col++]);
					ryohiSeisanAction.setShainNoRyohi(columns[col++]);
					ryohiSeisanAction.setKaribaraiMishiyouFlg(columns[col++]);
					ryohiSeisanAction.setDairiFlg(columns[col++]);
					ryohiSeisanAction.setHoumonsaki(columns[col++]);
					ryohiSeisanAction.setMokuteki(columns[col++]);
					ryohiSeisanAction.setShiharaihouhou(columns[col++]);//1:現金 2:振込
					ryohiSeisanAction.setShiharaiKiboubi(columns[col++]);
					ryohiSeisanAction.setSeisankikanFrom(columns[col++]);
					ryohiSeisanAction.setSeisankikanTo(columns[col++]);
					ryohiSeisanAction.setZeiritsuRyohi(columns[col++]);
					ryohiSeisanAction.setKingaku(columns[col++]);
					
					ryohiSeisanAction.setShiwakeEdaNoRyohi(columns[col++]);
					ryohiSeisanAction.setTorihikiNameRyohi(columns[col++]);
					ryohiSeisanAction.setKamokuCdRyohi(columns[col++]);
					ryohiSeisanAction.setKamokuNameRyohi(columns[col++]);
					ryohiSeisanAction.setKamokuEdabanCdRyohi(columns[col++]);
					ryohiSeisanAction.setKamokuEdabanNameRyohi(columns[col++]);
					ryohiSeisanAction.setFutanBumonCdRyohi(columns[col++]);
					ryohiSeisanAction.setFutanBumonNameRyohi(columns[col++]);
					ryohiSeisanAction.setProjectCdRyohi(columns[col++]);
					ryohiSeisanAction.setProjectNameRyohi(columns[col++]);
					
					ryohiSeisanAction.setHoujinCardRiyouGoukei(columns[col++]);
					ryohiSeisanAction.setKaishaTehaiGoukei(columns[col++]);
					ryohiSeisanAction.setSashihikiShikyuuKingaku(columns[col++]);
					
					
					ryohiSeisanAction.setTekiyouRyohi(columns[col++]);
					ryohiSeisanAction.setShiharaibi(columns[col++]);
					ryohiSeisanAction.setKeijoubi(columns[col++]);
					ryohiSeisanAction.setHosoku(columns[col++]);
					
					ryohiSeisanAction.setHf1Cd(columns[col++]);
					ryohiSeisanAction.setHf1Name(columns[col++]);
					ryohiSeisanAction.setHf2Cd(columns[col++]);
					ryohiSeisanAction.setHf2Name(columns[col++]);
					ryohiSeisanAction.setHf3Cd(columns[col++]);
					ryohiSeisanAction.setHf3Name(columns[col++]);
					ryohiSeisanAction.setUf1CdRyohi(columns[col++]);
					ryohiSeisanAction.setUf1NameRyohi(columns[col++]);
					ryohiSeisanAction.setUf2CdRyohi(columns[col++]);
					ryohiSeisanAction.setUf2NameRyohi(columns[col++]);
					ryohiSeisanAction.setUf3CdRyohi(columns[col++]);
					ryohiSeisanAction.setUf3NameRyohi(columns[col++]);
					
					
					
					
					
					
					//交通費・日当
					kikanFrom.clear();
					kikanTo.clear();
					kyuujitsuNissuu.clear();
					shubetsuCd.clear();
					shubetsu1.clear();
					shubetsu2.clear();
					koutsuuShudan.clear();
					shouhyouShoruiHissuFlg.clear();
					List<String> ryoushuushoSeikyuushoTouFlg =  new ArrayList<>();
					naiyou.clear();
					bikou.clear();
					oufukuFlg.clear();
					List<String> houjinCardFlgRyohi =  new ArrayList<>();
					List<String> kaishaTehaiFlgRyohi =  new ArrayList<>();
					jidounyuuryokuFlg.clear();
					nissuu.clear();
					tanka.clear();
					meisaiKingaku.clear();
					for(int kk = 0; kk < MEISAI_NUMS ; kk++ ){
						//データ無ければスキップ
						if(columns[col].equals("")){
							col += 18;
							continue;
						}
						kikanFrom .add(columns[col++]);
						kikanTo .add(columns[col++]);
						kyuujitsuNissuu .add(columns[col++]);
						shubetsuCd .add(columns[col++]);
						shubetsu1 .add(columns[col++]);
						shubetsu2 .add(columns[col++]);
						koutsuuShudan .add(columns[col++]);
						shouhyouShoruiHissuFlg.add(columns[col++]);
						ryoushuushoSeikyuushoTouFlg.add(columns[col++]);
						naiyou .add(columns[col++]);
						bikou .add(columns[col++]);
						oufukuFlg .add(columns[col++]);
						houjinCardFlgRyohi.add(columns[col++]);
						kaishaTehaiFlgRyohi.add(columns[col++]);
						jidounyuuryokuFlg .add(columns[col++]);
						nissuu .add(columns[col++]);
						tanka .add(columns[col++]);
						meisaiKingaku .add(columns[col++]);
					}
					ryohiSeisanAction.setKikanFrom(kikanFrom.toArray(new String[0]));
					ryohiSeisanAction.setKikanTo(kikanTo.toArray(new String[0]));
					ryohiSeisanAction.setKyuujitsuNissuu(kyuujitsuNissuu.toArray(new String[0]));
					ryohiSeisanAction.setShubetsuCd(shubetsuCd.toArray(new String[0]));
					ryohiSeisanAction.setShubetsu1(shubetsu1.toArray(new String[0]));
					ryohiSeisanAction.setShubetsu2(shubetsu2.toArray(new String[0]));
					ryohiSeisanAction.setKoutsuuShudan(koutsuuShudan.toArray(new String[0]));
					ryohiSeisanAction.setShouhyouShoruiHissuFlg(shouhyouShoruiHissuFlg.toArray(new String[0]));
					ryohiSeisanAction.setRyoushuushoSeikyuushoTouFlg(ryoushuushoSeikyuushoTouFlg.toArray(new String[0]));
					ryohiSeisanAction.setNaiyou(naiyou.toArray(new String[0]));
					ryohiSeisanAction.setBikou(bikou.toArray(new String[0]));
					ryohiSeisanAction.setOufukuFlg(oufukuFlg.toArray(new String[0]));
					ryohiSeisanAction.setHoujinCardFlgRyohi(houjinCardFlgRyohi.toArray(new String[0]));
					ryohiSeisanAction.setKaishaTehaiFlgRyohi(kaishaTehaiFlgRyohi.toArray(new String[0]));
					ryohiSeisanAction.setJidounyuuryokuFlg(jidounyuuryokuFlg.toArray(new String[0]));
					ryohiSeisanAction.setNissuu(nissuu.toArray(new String[0]));
					ryohiSeisanAction.setTanka(tanka.toArray(new String[0]));
					ryohiSeisanAction.setMeisaiKingaku(meisaiKingaku.toArray(new String[0]));
					
					
					
					//その他経費
					shiyoubi.clear();
					shouhyouShorui.clear();
					shiwakeEdaNo.clear();
					torihikiName.clear();
					kamokuCd.clear();
					kamokuName.clear();
					kamokuEdabanCd.clear();
					kamokuEdabanName.clear();
					futanBumonCd.clear();
					futanBumonName.clear();
					torihikisakiCd.clear();
					torihikisakiName.clear();
					projectCd.clear();
					projectName.clear();
					uf1Cd.clear();
					uf1Name.clear();
					uf2Cd.clear();
					uf2Name.clear();
					uf3Cd.clear();
					uf3Name.clear();
					kazeiKbn.clear();
					zeiritsu.clear();
					List<String> houjinCardFlgKeihi =  new ArrayList<>();
					List<String> kaishaTehaiFlgKeihi =  new ArrayList<>();
					shiharaiKingaku.clear();
					tekiyou.clear();
					kousaihiShousai.clear();
					kousaihiHyoujiFlg.clear();
					
					hontaiKingaku.clear();
					shouhizeigaku.clear();
					for(int kk = 0; kk < MEISAI_NUMS ; kk++ ){
						//データ無ければスキップ
						if(columns[col].equals("")){
							col += 28;
							continue;
						}
						shiyoubi .add(columns[col++]);
						shouhyouShorui .add(columns[col++]);
						shiwakeEdaNo .add(columns[col++]);
						torihikiName .add(columns[col++]);
						kamokuCd .add(columns[col++]);
						kamokuName .add(columns[col++]);
						kamokuEdabanCd .add(columns[col++]);
						kamokuEdabanName .add(columns[col++]);
						futanBumonCd .add(columns[col++]);
						futanBumonName .add(columns[col++]);
						torihikisakiCd .add(columns[col++]);
						torihikisakiName .add(columns[col++]);
						projectCd .add(columns[col++]);
						projectName .add(columns[col++]);
						uf1Cd .add(columns[col++]);
						uf1Name .add(columns[col++]);
						uf2Cd .add(columns[col++]);
						uf2Name .add(columns[col++]);
						uf3Cd .add(columns[col++]);
						uf3Name .add(columns[col++]);
						kazeiKbn .add(columns[col++]);
						zeiritsu .add(columns[col++]);
						houjinCardFlgKeihi .add(columns[col++]);
						kaishaTehaiFlgKeihi .add(columns[col++]);
						shiharaiKingaku .add(columns[col++]);
						tekiyou .add(columns[col++]);
						kousaihiShousai .add(columns[col++]);
						kousaihiHyoujiFlg .add(columns[col++]);
						
						//下記2項目は空欄でよい
						hontaiKingaku.add("");
						shouhizeigaku.add("");
					}
					ryohiSeisanAction.setShiyoubi(shiyoubi.toArray(new String[0]));
					ryohiSeisanAction.setShouhyouShorui(shouhyouShorui.toArray(new String[0]));
					ryohiSeisanAction.setShiwakeEdaNo(shiwakeEdaNo.toArray(new String[0]));
					ryohiSeisanAction.setTorihikiName(torihikiName.toArray(new String[0]));
					ryohiSeisanAction.setKamokuCd(kamokuCd.toArray(new String[0]));
					ryohiSeisanAction.setKamokuName(kamokuName.toArray(new String[0]));
					ryohiSeisanAction.setKamokuEdabanCd(kamokuEdabanCd.toArray(new String[0]));
					ryohiSeisanAction.setKamokuEdabanName(kamokuEdabanName.toArray(new String[0]));
					ryohiSeisanAction.setFutanBumonCd(futanBumonCd.toArray(new String[0]));
					ryohiSeisanAction.setFutanBumonName(futanBumonName.toArray(new String[0]));
					ryohiSeisanAction.setTorihikisakiCd(torihikisakiCd.toArray(new String[0]));
					ryohiSeisanAction.setTorihikisakiName(torihikisakiName.toArray(new String[0]));
					ryohiSeisanAction.setProjectCd(projectCd.toArray(new String[0]));
					ryohiSeisanAction.setProjectName(projectName.toArray(new String[0]));
					ryohiSeisanAction.setUf1Cd(uf1Cd.toArray(new String[0]));
					ryohiSeisanAction.setUf1Name(uf1Name.toArray(new String[0]));
					ryohiSeisanAction.setUf2Cd(uf2Cd.toArray(new String[0]));
					ryohiSeisanAction.setUf2Name(uf2Name.toArray(new String[0]));
					ryohiSeisanAction.setUf3Cd(uf3Cd.toArray(new String[0]));
					ryohiSeisanAction.setUf3Name(uf3Name.toArray(new String[0]));
					ryohiSeisanAction.setKazeiKbn(kazeiKbn.toArray(new String[0]));
					ryohiSeisanAction.setZeiritsu(zeiritsu.toArray(new String[0]));
					ryohiSeisanAction.setHoujinCardFlgKeihi(houjinCardFlgKeihi.toArray(new String[0]));
					ryohiSeisanAction.setKaishaTehaiFlgKeihi(kaishaTehaiFlgKeihi.toArray(new String[0]));
					ryohiSeisanAction.setShiharaiKingaku(shiharaiKingaku.toArray(new String[0]));
					ryohiSeisanAction.setTekiyou(tekiyou.toArray(new String[0]));
					ryohiSeisanAction.setKousaihiShousai(kousaihiShousai.toArray(new String[0]));
					ryohiSeisanAction.setKousaihiHyoujiFlg(kousaihiHyoujiFlg.toArray(new String[0]));
					
					ryohiSeisanAction.setHontaiKingaku(hontaiKingaku.toArray(new String[0]));
					ryohiSeisanAction.setShouhizeigaku(shouhizeigaku.toArray(new String[0]));
					
					
					testMap.put("ryohiseisan", ryohiSeisanAction);
					ret.add(testMap);
				}
				br.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return ret;
	}

	/**
	 * Actionの入力コピー
	 * @param from FROM
	 * @param to TO
	 */
	protected static void copyInputdata(RyohiKaribaraiShinseiAction from, RyohiKaribaraiShinseiAction to) {
		to.setUserIdRyohi(from.getUserIdRyohi());
		to.setUserNameRyohi(from.getUserNameRyohi());
		to.setShainNoRyohi(from.getShainNoRyohi());
		to.setKaribaraiOn(from.getKaribaraiOn());
		to.setDairiFlg(from.getDairiFlg());
		to.setHoumonsaki(from.getHoumonsaki());
		to.setMokuteki(from.getMokuteki());
		to.setSeisankikanFrom(from.getSeisankikanFrom());
		to.setSeisankikanTo(from.getSeisankikanTo());
		to.setShiwakeEdaNoRyohi(from.getShiwakeEdaNoRyohi());
		to.setTorihikiNameRyohi(from.getTorihikiNameRyohi());
		to.setKamokuCdRyohi(from.getKamokuCdRyohi());
		to.setKamokuNameRyohi(from.getKamokuNameRyohi());
		to.setKamokuEdabanCdRyohi(from.getKamokuEdabanCdRyohi());
		to.setKamokuEdabanNameRyohi(from.getKamokuEdabanNameRyohi());
		to.setFutanBumonCdRyohi(from.getFutanBumonCdRyohi());
		to.setFutanBumonNameRyohi(from.getFutanBumonNameRyohi());
		to.setHf1Cd(from.getHf1Cd());
		to.setHf1Name(from.getHf1Name());
		to.setHf2Cd(from.getHf2Cd());
		to.setHf2Name(from.getHf2Name());
		to.setHf3Cd(from.getHf3Cd());
		to.setHf3Name(from.getHf3Name());
		to.setUf1CdRyohi(from.getUf1CdRyohi());
		to.setUf1NameRyohi(from.getUf1NameRyohi());
		to.setUf2CdRyohi(from.getUf2CdRyohi());
		to.setUf2NameRyohi(from.getUf2NameRyohi());
		to.setUf3CdRyohi(from.getUf3CdRyohi());
		to.setUf3NameRyohi(from.getUf3NameRyohi());
		to.setTekiyouRyohi(from.getTekiyouRyohi());
		to.setShiharaiKiboubi(from.getShiharaiKiboubi());
		to.setShiharaihouhou(from.getShiharaihouhou());
		to.setShiharaibi(from.getShiharaibi());
		to.setKingaku(from.getKingaku());
		to.setKaribaraiKingaku(from.getKaribaraiKingaku());
		to.setKikanFrom(from.getKikanFrom());
		to.setKikanTo(from.getKikanTo());
		to.setKyuujitsuNissuu(from.getKyuujitsuNissuu());
		to.setShubetsuCd(from.getShubetsuCd());
		to.setShubetsu1(from.getShubetsu1());
		to.setShubetsu2(from.getShubetsu2());
		to.setKoutsuuShudan(from.getKoutsuuShudan());
		to.setNaiyou(from.getNaiyou());
		to.setBikou(from.getBikou());
		to.setOufukuFlg(from.getOufukuFlg());
		to.setJidounyuuryokuFlg(from.getJidounyuuryokuFlg());
		to.setNissuu(from.getNissuu());
		to.setTanka(from.getTanka());
		to.setMeisaiKingaku(from.getMeisaiKingaku());
		to.setShiyoubi(from.getShiyoubi());
		to.setShouhyouShorui(from.getShouhyouShorui());
		to.setShiwakeEdaNo(from.getShiwakeEdaNo());
		to.setTorihikiName(from.getTorihikiName());
		to.setKamokuCd(from.getKamokuCd());
		to.setKamokuName(from.getKamokuName());
		to.setKamokuEdabanCd(from.getKamokuEdabanCd());
		to.setKamokuEdabanName(from.getKamokuEdabanName());
		to.setFutanBumonCd(from.getFutanBumonCd());
		to.setFutanBumonName(from.getFutanBumonName());
		to.setProjectCd(from.getProjectCd());
		to.setProjectName(from.getProjectName());
		to.setTorihikisakiCd(from.getTorihikisakiCd());
		to.setTorihikisakiName(from.getTorihikisakiName());
		to.setUf1Cd(from.getUf1Cd());
		to.setUf1Name(from.getUf1Name());
		to.setUf2Cd(from.getUf2Cd());
		to.setUf2Name(from.getUf2Name());
		to.setUf3Cd(from.getUf3Cd());
		to.setUf3Name(from.getUf3Name());
		to.setKazeiKbn(from.getKazeiKbn());
		to.setZeiritsu(from.getZeiritsu());
		to.setShiharaiKingaku(from.getShiharaiKingaku());
		to.setTekiyou(from.getTekiyou());
		to.setKousaihiShousai(from.getKousaihiShousai());
		to.setKousaihiHyoujiFlg(from.getKousaihiHyoujiFlg());
		
		to.setHosoku(from.getHosoku());
		to.setHontaiKingaku(from.getHontaiKingaku());
		to.setShouhizeigaku(from.getShouhizeigaku());
		to.setShouhyouShoruiHissuFlg(from.getShouhyouShoruiHissuFlg());
	}

	/**
	 * Actionの入力コピー
	 * @param from FROM
	 * @param to TO
	 */
	protected static void copyInputData(RyohiSeisanAction from, RyohiSeisanAction to) {
		to.setUserIdRyohi(from.getUserIdRyohi());
		to.setUserNameRyohi(from.getUserNameRyohi());
		to.setShainNoRyohi(from.getShainNoRyohi());
		to.setKaribaraiMishiyouFlg(from.getKaribaraiMishiyouFlg());
		to.setDairiFlg(from.getDairiFlg());
		to.setHoumonsaki(from.getHoumonsaki());
		to.setMokuteki(from.getMokuteki());
		to.setShiharaihouhou(from.getShiharaihouhou());
		to.setShiharaiKiboubi(from.getShiharaiKiboubi());
		to.setSeisankikanFrom(from.getSeisankikanFrom());
		to.setSeisankikanTo(from.getSeisankikanTo());
		to.setZeiritsuRyohi(from.getZeiritsuRyohi());
		to.setKingaku(from.getKingaku());
		to.setShiwakeEdaNoRyohi(from.getShiwakeEdaNoRyohi());
		to.setTorihikiNameRyohi(from.getTorihikiNameRyohi());
		to.setKamokuCdRyohi(from.getKamokuCdRyohi());
		to.setKamokuNameRyohi(from.getKamokuNameRyohi());
		to.setKamokuEdabanCdRyohi(from.getKamokuEdabanCdRyohi());
		to.setKamokuEdabanNameRyohi(from.getKamokuEdabanNameRyohi());
		to.setFutanBumonCdRyohi(from.getFutanBumonCdRyohi());
		to.setFutanBumonNameRyohi(from.getFutanBumonNameRyohi());
		to.setProjectCdRyohi(from.getProjectCdRyohi());
		to.setProjectNameRyohi(from.getProjectNameRyohi());
		to.setHoujinCardRiyouGoukei(from.getHoujinCardRiyouGoukei());
		to.setKaishaTehaiGoukei(from.getKaishaTehaiGoukei());
		to.setSashihikiShikyuuKingaku(from.getSashihikiShikyuuKingaku());
		to.setTekiyouRyohi(from.getTekiyouRyohi());
		to.setShiharaibi(from.getShiharaibi());
		to.setKeijoubi(from.getKeijoubi());
		to.setHosoku(from.getHosoku());
		to.setHf1Cd(from.getHf1Cd());
		to.setHf1Name(from.getHf1Name());
		to.setHf2Cd(from.getHf2Cd());
		to.setHf2Name(from.getHf2Name());
		to.setHf3Cd(from.getHf3Cd());
		to.setHf3Name(from.getHf3Name());
		to.setUf1CdRyohi(from.getUf1CdRyohi());
		to.setUf1NameRyohi(from.getUf1NameRyohi());
		to.setUf2CdRyohi(from.getUf2CdRyohi());
		to.setUf2NameRyohi(from.getUf2NameRyohi());
		to.setUf3CdRyohi(from.getUf3CdRyohi());
		to.setUf3NameRyohi(from.getUf3NameRyohi());
		to.setKikanFrom(from.getKikanFrom());
		to.setKikanTo(from.getKikanTo());
		to.setKyuujitsuNissuu(from.getKyuujitsuNissuu());
		to.setShubetsuCd(from.getShubetsuCd());
		to.setShubetsu1(from.getShubetsu1());
		to.setShubetsu2(from.getShubetsu2());
		to.setKoutsuuShudan(from.getKoutsuuShudan());
		to.setShouhyouShoruiHissuFlg(from.getShouhyouShoruiHissuFlg());
		to.setRyoushuushoSeikyuushoTouFlg(from.getRyoushuushoSeikyuushoTouFlg());
		to.setNaiyou(from.getNaiyou());
		to.setBikou(from.getBikou());
		to.setOufukuFlg(from.getOufukuFlg());
		to.setHoujinCardFlgRyohi(from.getHoujinCardFlgRyohi());
		to.setKaishaTehaiFlgRyohi(from.getKaishaTehaiFlgRyohi());
		to.setJidounyuuryokuFlg(from.getJidounyuuryokuFlg());
		to.setNissuu(from.getNissuu());
		to.setTanka(from.getTanka());
		to.setMeisaiKingaku(from.getMeisaiKingaku());
		to.setShiyoubi(from.getShiyoubi());
		to.setShouhyouShorui(from.getShouhyouShorui());
		to.setShiwakeEdaNo(from.getShiwakeEdaNo());
		to.setTorihikiName(from.getTorihikiName());
		to.setKamokuCd(from.getKamokuCd());
		to.setKamokuName(from.getKamokuName());
		to.setKamokuEdabanCd(from.getKamokuEdabanCd());
		to.setKamokuEdabanName(from.getKamokuEdabanName());
		to.setFutanBumonCd(from.getFutanBumonCd());
		to.setFutanBumonName(from.getFutanBumonName());
		to.setTorihikisakiCd(from.getTorihikisakiCd());
		to.setTorihikisakiName(from.getTorihikisakiName());
		to.setProjectCd(from.getProjectCd());
		to.setProjectName(from.getProjectName());
		to.setUf1Cd(from.getUf1Cd());
		to.setUf1Name(from.getUf1Name());
		to.setUf2Cd(from.getUf2Cd());
		to.setUf2Name(from.getUf2Name());
		to.setUf3Cd(from.getUf3Cd());
		to.setUf3Name(from.getUf3Name());
		to.setKazeiKbn(from.getKazeiKbn());
		to.setZeiritsu(from.getZeiritsu());
		to.setHoujinCardFlgKeihi(from.getHoujinCardFlgKeihi());
		to.setKaishaTehaiFlgKeihi(from.getKaishaTehaiFlgKeihi());
		to.setShiharaiKingaku(from.getShiharaiKingaku());
		to.setTekiyou(from.getTekiyou());
		to.setKousaihiShousai(from.getKousaihiShousai());
		to.setKousaihiHyoujiFlg(from.getKousaihiHyoujiFlg());
		
		to.setHontaiKingaku(from.getHontaiKingaku());
		to.setShouhizeigaku(from.getShouhizeigaku());

	}
	
// /**
//  *
//  * @param denpyouId 伝票ID
//  * @param str 出力文言
//  */
// protected static void outputShiwake (String denpyouId, String str) {
// 
// //TODO 必要なら作成
// //TODO 1ファイルでまとめて出すようにする 比較しにくくなる項目を省くか見やすくする
// 
// try {
// FileWriter fw = new FileWriter("C:\\eteam\\tmp\\test_" + "No" + str + ".csv", false);
// PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
//
// //データの取得
// List<Map<String, Object>> dataList = selectShiwakeData(denpyouId);
//
// pw.println("serial_no"
// +",denpyou_id"
// +",shiwake_status"
// +",touroku_time"
// +",koushin_time"
// +",dymd"
// +",seiri"
// +",dcno"
// +",kymd"
// +",kbmn"
// +",kusr"
// +",sgno"
// +",hf1"
// +",hf2"
// +",hf3"
// +",hf4"
// +",hf5"
// +",hf6"
// +",hf7"
// +",hf8"
// +",hf9"
// +",hf10"
// +",rbmn"
// +",rtor"
// +",rkmk"
// +",reda"
// +",rkoj"
// +",rkos"
// +",rprj"
// +",rseg"
// +",rdm1"
// +",rdm2"
// +",rdm3"
// +",rdm4"
// +",rdm5"
// +",rdm6"
// +",rdm7"
// +",rdm8"
// +",rdm9"
// +",rdm10"
// +",rdm11"
// +",rdm12"
// +",rdm13"
// +",rdm14"
// +",rdm15"
// +",rdm16"
// +",rdm17"
// +",rdm18"
// +",rdm19"
// +",rdm20"
// +",rrit"
// +",rzkb"
// +",rgyo"
// +",rsre"
// +",rtky"
// +",rtno"
// +",sbmn"
// +",stor"
// +",skmk"
// +",seda"
// +",skoj"
// +",skos"
// +",sprj"
// +",sseg"
// +",sdm1"
// +",sdm2"
// +",sdm3"
// +",sdm4"
// +",sdm5"
// +",sdm6"
// +",sdm7"
// +",sdm8"
// +",sdm9"
// +",sdm10"
// +",sdm11"
// +",sdm12"
// +",sdm13"
// +",sdm14"
// +",sdm15"
// +",sdm16"
// +",sdm17"
// +",sdm18"
// +",sdm19"
// +",sdm20"
// +",srit"
// +",szkb"
// +",sgyo"
// +",ssre"
// +",stky"
// +",stno"
// +",zkmk"
// +",zrit"
// +",zzkb"
// +",zgyo"
// +",zsre"
// +",exvl"
// +",valu"
// +",symd"
// +",skbn"
// +",skiz"
// +",uymd"
// +",ukbn"
// +",ukiz"
// +",dkec"
// +",fusr"
// +",fsen"
// +",tkflg"
// +",bunri"
// +",heic"
// +",rate"
// +",gexvl"
// +",gvalu"
// +",gsep"
// +",hf1_name"
// +",hf2_name"
// +",hf3_name"
// +",kari_bumon_name"
// +",kari_torihikisaki_name"
// +",kari_kamoku_name"
// +",kari_edaban_name"
// +",kari_project_name"
// +",kari_uf1_name"
// +",kari_uf2_name"
// +",kari_uf3_name"
// +",kashi_bumon_name"
// +",kashi_torihikisaki_name"
// +",kashi_kamoku_name"
// +",kashi_edaban_name"
// +",kashi_project_name"
// +",kashi_uf1_name"
// +",kashi_uf2_name"
// +",kashi_uf3_name");
//
// for (Map<String, Object> map : dataList) {
// pw.print(map.get("serial_no"));
// pw.print(",");
// pw.print(map.get("denpyou_id"));
// pw.print(",");
// pw.print(map.get("shiwake_status"));
// pw.print(",");
// pw.print(map.get("touroku_time"));
// pw.print(",");
// pw.print(map.get("koushin_time"));
// pw.print(",");
// pw.print(map.get("dymd"));
// pw.print(",");
// pw.print(map.get("seiri"));
// pw.print(",");
// pw.print(map.get("dcno"));
// pw.print(",");
// pw.print(map.get("kymd"));
// pw.print(",");
// pw.print(map.get("kbmn"));
// pw.print(",");
// pw.print(map.get("kusr"));
// pw.print(",");
// pw.print(map.get("sgno"));
// pw.print(",");
// pw.print(map.get("hf1"));
// pw.print(",");
// pw.print(map.get("hf2"));
// pw.print(",");
// pw.print(map.get("hf3"));
// pw.print(",");
// pw.print(map.get("hf4"));
// pw.print(",");
// pw.print(map.get("hf5"));
// pw.print(",");
// pw.print(map.get("hf6"));
// pw.print(",");
// pw.print(map.get("hf7"));
// pw.print(",");
// pw.print(map.get("hf8"));
// pw.print(",");
// pw.print(map.get("hf9"));
// pw.print(",");
// pw.print(map.get("hf10"));
// pw.print(",");
// pw.print(map.get("rbmn"));
// pw.print(",");
// pw.print(map.get("rtor"));
// pw.print(",");
// pw.print(map.get("rkmk"));
// pw.print(",");
// pw.print(map.get("reda"));
// pw.print(",");
// pw.print(map.get("rkoj"));
// pw.print(",");
// pw.print(map.get("rkos"));
// pw.print(",");
// pw.print(map.get("rprj"));
// pw.print(",");
// pw.print(map.get("rseg"));
// pw.print(",");
// pw.print(map.get("rdm1"));
// pw.print(",");
// pw.print(map.get("rdm2"));
// pw.print(",");
// pw.print(map.get("rdm3"));
// pw.print(",");
// pw.print(map.get("rdm4"));
// pw.print(",");
// pw.print(map.get("rdm5"));
// pw.print(",");
// pw.print(map.get("rdm6"));
// pw.print(",");
// pw.print(map.get("rdm7"));
// pw.print(",");
// pw.print(map.get("rdm8"));
// pw.print(",");
// pw.print(map.get("rdm9"));
// pw.print(",");
// pw.print(map.get("rdm10"));
// pw.print(",");
// pw.print(map.get("rdm11"));
// pw.print(",");
// pw.print(map.get("rdm12"));
// pw.print(",");
// pw.print(map.get("rdm13"));
// pw.print(",");
// pw.print(map.get("rdm14"));
// pw.print(",");
// pw.print(map.get("rdm15"));
// pw.print(",");
// pw.print(map.get("rdm16"));
// pw.print(",");
// pw.print(map.get("rdm17"));
// pw.print(",");
// pw.print(map.get("rdm18"));
// pw.print(",");
// pw.print(map.get("rdm19"));
// pw.print(",");
// pw.print(map.get("rdm20"));
// pw.print(",");
// pw.print(map.get("rrit"));
// pw.print(",");
// pw.print(map.get("rzkb"));
// pw.print(",");
// pw.print(map.get("rgyo"));
// pw.print(",");
// pw.print(map.get("rsre"));
// pw.print(",");
// pw.print(map.get("rtky"));
// pw.print(",");
// pw.print(map.get("rtno"));
// pw.print(",");
// pw.print(map.get("sbmn"));
// pw.print(",");
// pw.print(map.get("stor"));
// pw.print(",");
// pw.print(map.get("skmk"));
// pw.print(",");
// pw.print(map.get("seda"));
// pw.print(",");
// pw.print(map.get("skoj"));
// pw.print(",");
// pw.print(map.get("skos"));
// pw.print(",");
// pw.print(map.get("sprj"));
// pw.print(",");
// pw.print(map.get("sseg"));
// pw.print(",");
// pw.print(map.get("sdm1"));
// pw.print(",");
// pw.print(map.get("sdm2"));
// pw.print(",");
// pw.print(map.get("sdm3"));
// pw.print(",");
// pw.print(map.get("sdm4"));
// pw.print(",");
// pw.print(map.get("sdm5"));
// pw.print(",");
// pw.print(map.get("sdm6"));
// pw.print(",");
// pw.print(map.get("sdm7"));
// pw.print(",");
// pw.print(map.get("sdm8"));
// pw.print(",");
// pw.print(map.get("sdm9"));
// pw.print(",");
// pw.print(map.get("sdm10"));
// pw.print(",");
// pw.print(map.get("sdm11"));
// pw.print(",");
// pw.print(map.get("sdm12"));
// pw.print(",");
// pw.print(map.get("sdm13"));
// pw.print(",");
// pw.print(map.get("sdm14"));
// pw.print(",");
// pw.print(map.get("sdm15"));
// pw.print(",");
// pw.print(map.get("sdm16"));
// pw.print(",");
// pw.print(map.get("sdm17"));
// pw.print(",");
// pw.print(map.get("sdm18"));
// pw.print(",");
// pw.print(map.get("sdm19"));
// pw.print(",");
// pw.print(map.get("sdm20"));
// pw.print(",");
// pw.print(map.get("srit"));
// pw.print(",");
// pw.print(map.get("szkb"));
// pw.print(",");
// pw.print(map.get("sgyo"));
// pw.print(",");
// pw.print(map.get("ssre"));
// pw.print(",");
// pw.print(map.get("stky"));
// pw.print(",");
// pw.print(map.get("stno"));
// pw.print(",");
// pw.print(map.get("zkmk"));
// pw.print(",");
// pw.print(map.get("zrit"));
// pw.print(",");
// pw.print(map.get("zzkb"));
// pw.print(",");
// pw.print(map.get("zgyo"));
// pw.print(",");
// pw.print(map.get("zsre"));
// pw.print(",");
// pw.print(map.get("exvl"));
// pw.print(",");
// pw.print(map.get("valu"));
// pw.print(",");
// pw.print(map.get("symd"));
// pw.print(",");
// pw.print(map.get("skbn"));
// pw.print(",");
// pw.print(map.get("skiz"));
// pw.print(",");
// pw.print(map.get("uymd"));
// pw.print(",");
// pw.print(map.get("ukbn"));
// pw.print(",");
// pw.print(map.get("ukiz"));
// pw.print(",");
// pw.print(map.get("dkec"));
// pw.print(",");
// pw.print(map.get("fusr"));
// pw.print(",");
// pw.print(map.get("fsen"));
// pw.print(",");
// pw.print(map.get("tkflg"));
// pw.print(",");
// pw.print(map.get("bunri"));
// pw.print(",");
// pw.print(map.get("heic"));
// pw.print(",");
// pw.print(map.get("rate"));
// pw.print(",");
// pw.print(map.get("gexvl"));
// pw.print(",");
// pw.print(map.get("gvalu"));
// pw.print(",");
// pw.print(map.get("gsep"));
// pw.print(",");
// pw.print(map.get("hf1_name"));
// pw.print(",");
// pw.print(map.get("hf2_name"));
// pw.print(",");
// pw.print(map.get("hf3_name"));
// pw.print(",");
// pw.print(map.get("kari_bumon_name"));
// pw.print(",");
// pw.print(map.get("kari_torihikisaki_name"));
// pw.print(",");
// pw.print(map.get("kari_kamoku_name"));
// pw.print(",");
// pw.print(map.get("kari_edaban_name"));
// pw.print(",");
// pw.print(map.get("kari_project_name"));
// pw.print(",");
// pw.print(map.get("kari_uf1_name"));
// pw.print(",");
// pw.print(map.get("kari_uf2_name"));
// pw.print(",");
// pw.print(map.get("kari_uf3_name"));
// pw.print(",");
// pw.print(map.get("kashi_bumon_name"));
// pw.print(",");
// pw.print(map.get("kashi_torihikisaki_name"));
// pw.print(",");
// pw.print(map.get("kashi_kamoku_name"));
// pw.print(",");
// pw.print(map.get("kashi_edaban_name"));
// pw.print(",");
// pw.print(map.get("kashi_project_name"));
// pw.print(",");
// pw.print(map.get("kashi_uf1_name"));
// pw.print(",");
// pw.print(map.get("kashi_uf2_name"));
// pw.print(",");
// pw.print(map.get("kashi_uf3_name"));
// pw.println();
// }
//
// pw.close();
//
// } catch (IOException ex) {
// //例外時処理
// ex.printStackTrace();
// }
// }
//
// /**
//  * 伝票IDをキーに仕訳データを取得する
//  * @param denpyouId 伝票ID
//  * @return 検索結果(複数件)
//  */
// protected static List<Map<String, Object>> selectShiwakeData(String denpyouId){
// try(EteamConnection connection = EteamConnection.connect()){
// String sql = " SELECT * "
// + "  ,:HF1_NAME AS hf1_name"
// + "  ,:HF2_NAME AS hf2_name"
// + "  ,:HF3_NAME AS hf3_name"
// + "  ,rbm.futan_bumon_name AS kari_bumon_name"
// + "  ,rto.torihikisaki_name_ryakushiki AS kari_torihikisaki_name"
// + "  ,rkm.kamoku_name_ryakushiki AS kari_kamoku_name"
// + "  ,red.edaban_name AS kari_edaban_name"
// + "  ,rpj.project_name AS kari_project_name"
// + "  ,:R_UF1_NAME AS kari_uf1_name"
// + "  ,:R_UF2_NAME AS kari_uf2_name"
// + "  ,:R_UF3_NAME AS kari_uf3_name"
// + "  ,sbm.futan_bumon_name AS kashi_bumon_name"
// + "  ,sto.torihikisaki_name_ryakushiki AS kashi_torihikisaki_name"
// + "  ,skm.kamoku_name_ryakushiki AS kashi_kamoku_name"
// + "  ,sed.edaban_name AS kashi_edaban_name"
// + "  ,spj.project_name AS kashi_project_name"
// + "  ,:S_UF1_NAME AS kashi_uf1_name"
// + "  ,:S_UF2_NAME AS kashi_uf2_name"
// + "  ,:S_UF3_NAME AS kashi_uf3_name"
// + " FROM"
// + "  shiwake_sias s"
// + " :HF1_ICHIRAN"
// + " :HF2_ICHIRAN"
// + " :HF3_ICHIRAN"
// + " LEFT OUTER JOIN bumon_master rbm ON s.rbmn = rbm.futan_bumon_cd"
// + " LEFT OUTER JOIN torihikisaki_master rto ON s.rtor = rto.torihikisaki_cd"
// + " LEFT OUTER JOIN kamoku_master rkm ON s.rkmk = rkm.kamoku_gaibu_cd"
// + " LEFT OUTER JOIN kamoku_edaban_zandaka red ON s.rkmk = red.kamoku_gaibu_cd AND s.reda = red.kamoku_edaban_cd"
// + " LEFT OUTER JOIN project_master rpj ON s.rprj = rpj.project_cd"
// + " :R_UF1_ICHIRAN"
// + " :R_UF2_ICHIRAN"
// + " :R_UF3_ICHIRAN"
// + " LEFT OUTER JOIN bumon_master sbm ON s.sbmn = sbm.futan_bumon_cd"
// + " LEFT OUTER JOIN torihikisaki_master sto ON s.stor = sto.torihikisaki_cd"
// + " LEFT OUTER JOIN kamoku_master skm ON s.skmk = skm.kamoku_gaibu_cd"
// + " LEFT OUTER JOIN kamoku_edaban_zandaka sed ON s.skmk = sed.kamoku_gaibu_cd AND s.seda = sed.kamoku_edaban_cd"
// + " LEFT OUTER JOIN project_master spj ON s.sprj = spj.project_cd"
// + " :S_UF1_ICHIRAN"
// + " :S_UF2_ICHIRAN"
// + " :S_UF3_ICHIRAN"
// + " WHERE"
// + " denpyou_id= ?";
// 
// if (Integer.parseInt(EteamSettingInfo.getSettingInfo(Key.HF1_MAPPING)) == 0) {
// sql = sql.replaceAll(":HF1_NAME", "''");
// sql = sql.replaceAll(":HF1_ICHIRAN", "");
// 
// } else {
// sql = sql.replaceAll(":HF1_NAME", "h1.hf1_name_ryakushiki");
// sql = sql.replaceAll(":HF1_ICHIRAN", "LEFT OUTER JOIN hf1_ichiran h1 ON s.hf" + EteamSettingInfo.getSettingInfo(Key.HF1_MAPPING) + " = h1.hf1_cd");
// }
// 
// if (Integer.parseInt(EteamSettingInfo.getSettingInfo(Key.HF2_MAPPING)) == 0) {
// sql = sql.replaceAll(":HF2_NAME", "''");
// sql = sql.replaceAll(":HF2_ICHIRAN", "");
// 
// } else {
// sql = sql.replaceAll(":HF2_NAME", "h2.hf2_name_ryakushiki");
// sql = sql.replaceAll(":HF2_ICHIRAN", "LEFT OUTER JOIN hf2_ichiran h2 ON s.hf" + EteamSettingInfo.getSettingInfo(Key.HF2_MAPPING) + " = h2.hf2_cd");
// }
// 
// if (Integer.parseInt(EteamSettingInfo.getSettingInfo(Key.HF3_MAPPING)) == 0) {
// sql = sql.replaceAll(":HF3_NAME", "''");
// sql = sql.replaceAll(":HF3_ICHIRAN", "");
// 
// } else {
// sql = sql.replaceAll(":HF3_NAME", "h3.hf3_name_ryakushiki");
// sql = sql.replaceAll(":HF3_ICHIRAN", "LEFT OUTER JOIN hf3_ichiran h3 ON s.hf" + EteamSettingInfo.getSettingInfo(Key.HF3_MAPPING) + " = h3.hf3_cd");
// }
// 
// if (Integer.parseInt(EteamSettingInfo.getSettingInfo(Key.UF1_MAPPING)) == 0) {
// sql = sql.replaceAll(":R_UF1_NAME", "''");
// sql = sql.replaceAll(":S_UF1_NAME", "''");
// sql = sql.replaceAll(":R_UF1_ICHIRAN", "");
// sql = sql.replaceAll(":S_UF1_ICHIRAN", "");
// 
// } else {
// sql = sql.replaceAll(":R_UF1_NAME", "rd1.uf1_name_ryakushiki");
// sql = sql.replaceAll(":S_UF1_NAME", "sd1.uf1_name_ryakushiki");
// sql = sql.replaceAll(":R_UF1_ICHIRAN", "LEFT OUTER JOIN uf1_ichiran rd1 ON s.rdm" + EteamSettingInfo.getSettingInfo(Key.UF1_MAPPING) + " = rd1.uf1_cd");
// sql = sql.replaceAll(":S_UF1_ICHIRAN", "LEFT OUTER JOIN uf1_ichiran sd1 ON s.sdm" + EteamSettingInfo.getSettingInfo(Key.UF1_MAPPING) + " = sd1.uf1_cd");
// }
// 
// if (Integer.parseInt(EteamSettingInfo.getSettingInfo(Key.UF2_MAPPING)) == 0) {
// sql = sql.replaceAll(":R_UF2_NAME", "''");
// sql = sql.replaceAll(":S_UF2_NAME", "''");
// sql = sql.replaceAll(":R_UF2_ICHIRAN", "");
// sql = sql.replaceAll(":S_UF2_ICHIRAN", "");
// 
// } else {
// sql = sql.replaceAll(":R_UF2_NAME", "rd2.uf2_name_ryakushiki");
// sql = sql.replaceAll(":S_UF2_NAME", "sd2.uf2_name_ryakushiki");
// sql = sql.replaceAll(":R_UF2_ICHIRAN", "LEFT OUTER JOIN uf2_ichiran rd2 ON s.rdm" + EteamSettingInfo.getSettingInfo(Key.UF2_MAPPING) + " = rd2.uf2_cd");
// sql = sql.replaceAll(":S_UF2_ICHIRAN", "LEFT OUTER JOIN uf2_ichiran sd2 ON s.sdm" + EteamSettingInfo.getSettingInfo(Key.UF2_MAPPING) + " = sd2.uf2_cd");
// }
// 
// if (Integer.parseInt(EteamSettingInfo.getSettingInfo(Key.UF3_MAPPING)) == 0) {
// sql = sql.replaceAll(":R_UF3_NAME", "''");
// sql = sql.replaceAll(":S_UF3_NAME", "''");
// sql = sql.replaceAll(":R_UF3_ICHIRAN", "");
// sql = sql.replaceAll(":S_UF3_ICHIRAN", "");
// 
// } else {
// sql = sql.replaceAll(":R_UF3_NAME", "rd3.uf3_name_ryakushiki");
// sql = sql.replaceAll(":S_UF3_NAME", "sd3.uf3_name_ryakushiki");
// sql = sql.replaceAll(":R_UF3_ICHIRAN", "LEFT OUTER JOIN uf3_ichiran rd3 ON s.rdm" + EteamSettingInfo.getSettingInfo(Key.UF3_MAPPING) + " = rd3.uf3_cd");
// sql = sql.replaceAll(":S_UF3_ICHIRAN", "LEFT OUTER JOIN uf3_ichiran sd3 ON s.sdm" + EteamSettingInfo.getSettingInfo(Key.UF3_MAPPING) + " = sd3.uf3_cd");
// }
// 
// List<Map<String, Object>> list = connection.load(sql, denpyouId);
// }
//
// return list;
// }
}
