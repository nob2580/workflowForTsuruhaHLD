package eteam.gyoumu.kaikei;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_STATUS;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.gyoumu.tsuuchi.DenpyouIchiranUpdateLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * 仕訳データ変更Actionクラス
 *
 */
@Getter @Setter @ToString
public class ShiwakeDataDe3HenkouAction extends EteamAbstractAction{
	
	//＜定数＞
	/** 仕訳抽出状態(内部コード名) */
	static final String SHIWAKE_STATUS_NM ="shiwake_status";
	
	//＜画面入力＞
	/** 伝票ID */
	String denpyouId;
	/** シリアル番号 */
	String serialNo;
	/** 仕訳抽出状態 */
	String shiwakeThushutuJyoutai;
	/** 伝票日付 */
	String denpyouDate;
	/** 整理月フラグ */
	String seiriTukiFlg;
	/** 伝票番号 */
	String denpyouNo;
	/** 借方　部門コード */
	String kariBumonCd;
	/** 借方　部門名 */
	String kariBumonName;
	/** 借方　取引先コード */
	String kariTorihikisakiCd;
	/** 借方　取引先名 */
	String kariTorihikisakiName;
	/** 借方　科目コード */
	String kariKamokuCd;
	/** 借方　科目名 */
	String kariKamokuName;
	/** 借方　枝番コード */
	String kariEdabanCd;
	/** 借方　枝番名 */
	String kariEdabanName;
	/** 借方　工事コード */
	String kariKoujiCd;
	/** 借方　工種コード */
	String kariKoushuCd;
	/** 借方　プロジェクトコード */
	String kariProjectCd;
	/** 借方　プロジェクト名 */
	String kariProjectName;
	/** 借方　セグメントコード */
	String kariSegmentCd;
	/** 借方　セグメント名 */
	String kariSegmentName;
	/** 借方　ユニバーサルフィールド１ */
	String kariUf1Cd;
	/** 借方　ユニバーサルフィールド１名 */
	String kariUf1Name;
	/** 借方　ユニバーサルフィールド２ */
	String kariUf2Cd;
	/** 借方　ユニバーサルフィールド２名 */
	String kariUf2Name;
	/** 借方　ユニバーサルフィールド３ */
	String kariUf3Cd;
	/** 借方　ユニバーサルフィールド３名 */
	String kariUf3Name;
	/** 摘要 */
	String tekiyou;
	/** 摘要コード */
	String tekiyouCd;
	/** 貸方　部門コード */
	String kashiBumonCd;
	/** 貸方　部門名 */
	String kashiBumonName;
	/** 貸方　取引先コード */
	String kashiTorihikisakiCd;
	/** 貸方　取引先名 */
	String kashiTorihikisakiName;
	/** 貸方　科目コード */
	String kashiKamokuCd;
	/** 貸方　科目名 */
	String kashiKamokuName;
	/** 貸方　枝番コード */
	String kashiEdabanCd;
	/** 貸方　枝番名 */
	String kashiEdabanName;
	/** 貸方　工事コード */
	String kashiKoujiCd;
	/** 貸方　工種コード */
	String kashiKoushuCd;
	/** 貸方　プロジェクトコード */
	String kashiProjectCd;
	/** 貸方　プロジェクト名 */
	String kashiProjectName;
	/** 貸方　セグメントコード */
	String kashiSegmentCd;
	/** 貸方　セグメント名 */
	String kashiSegmentName;
	/** 貸方　ユニバーサルフィールド１ */
	String kashiUf1Cd;
	/** 貸方　ユニバーサルフィールド１名 */
	String kashiUf1Name;
	/** 貸方　ユニバーサルフィールド２ */
	String kashiUf2Cd;
	/** 貸方　ユニバーサルフィールド２名 */
	String kashiUf2Name;
	/** 貸方　ユニバーサルフィールド３ */
	String kashiUf3Cd;
	/** 貸方　ユニバーサルフィールド３名 */
	String kashiUf3Name;
	/** 対価金額 */
	String taikaKingaku;
	/** 金額 */
	String kingaku;
	/** 消費税対象科目コード */
	String shouhizeiTaishoKamokuCd;
	/** 消費税対象科目税率 */
	String shouhizeiTaishoKamokuZeiritsu;
	/** 消費税対象科目　軽減税率区分 */
	String shouhizeiTaishoKeigenZeiritsuKbn;
	/** 消費税対象科目　課税区分 */
	String shouhizeiTaishoKamokuKazeiKbn;
	/** 消費税対象科目　業種区分 */
	String shouhizeiTaishoKamokuGyoushuKbn;
	/** 消費税対象科目　仕入区分 */
	String shouhizeiTaishoKamokuShiireKbn;
	/** 借方　税率 */
	String kariZeiritsu;
	/** 借方　軽減税率区分 */
	String kariKeigenZeiritsuKbn;
	/** 貸方　税率 */
	String kashiZeiritsu;
	/** 貸方　軽減税率区分 */
	String kashiKeigenZeiritsuKbn;
	/** 借方　課税区分 */
	String kariKazeiKbn;
	/** 借方　業種区分 */
	String kariGyoushuKbn;
	/** 借方　仕入区分 */
	String kariShiireKbn;
	/** 貸方　課税区分 */
	String kashiKazeiKbn;
	/** 貸方　業種区分 */
	String kashiGyoushuKbn;
	/** 貸方　仕入区分 */
	String kashiShiireKbn;
	/** 支払日 */
	String shiharaibi;
	/** 支払区分 */
	String shiharaiKbn;
	/** 支払期日 */
	String shiharaiKijitu;
	/** 回収日 */
	String kaishubi;
	/** 入金区分 */
	String nyukinKbn;
	/** 回収期日 */
	String kaishuKijitu;
	/** 店券フラグ */
	String tenkenFlg;
	/** 消込コード */
	String keshikomiCd;
	/** 起票年月日 */
	String kihyouNengappi;
	/** 起票部門コード */
	String kihyouBumonCd;
	/** 起票者コード */
	String kihyoushaCd;
	/** 入力者コード */
	String nyuryokushaCd;
	/** 付箋番号 */
	String husenNo;
	/** 承認グループNo. */
	String shouninGroupNo;
	/** 分離区分 */
	String bunriKbn;
	/** レート */
	String rate;
	/** 外貨対価金額 */
	String gaikaTaikaKingaku;
	/** 外貨金額 */
	String gaikaKingaku;
	/** 区切り */
	String kugiri;
	/** 借方 併用売上税額計算方式 */
	String kariUrizeikeisan;
	/** 貸方 併用売上税額計算方式 */
	String kashiUrizeikeisan;
	/** 税対象科目 併用売上税額計算方式 */
	String zeiUrizeikeisan;
	/** 借方 仕入税額控除経過措置割合 */
	String kariMenzeikeika;
	/** 貸方 仕入税額控除経過措置割合 */
	String kashiMenzeikeika;
	/** 税対象科目 仕入税額控除経過措置割合 */
	String zeiMenzeikeika;
	
	//＜画面制御情報＞
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** プロジェクトコード表示 */
	String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
	/** セグメントコード表示 */
	String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
	
	//＜画面入力以外＞
	/** シリアル番号のリスト */
	List<GMap> serialNoList;
	/** 戻るリンクURL */
	String backDenpyouUrl;
	/** 仕訳抽出状態コード */
	String shiwakeStatusCd;
	//＜部品＞
	/** コネクション */
	EteamConnection connection;
	/** 仕訳データ変更Logicクラス */
	ShiwakeDataDe3HenkouLogic shiwakeDataHenkouLogic;
	/** ワークフローイベント制御ロジッククラス */
	WorkflowEventControlLogic wfEventLogic;
	

	//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkHankakuEiSuuHaihun(denpyouId, "伝票ID", true);
		checkString(denpyouId, 1, 19, "伝票ID", true);
		checkNumber(serialNo, 1, 8, "シリアル番号", true);
		checkDate(denpyouDate, "伝票日付", false);
		checkString(seiriTukiFlg, 1, 1, "整理月フラグ", false);
		checkString(denpyouNo, 1, 8, "伝票番号", false);
		checkString(kariBumonCd, 1, 8, "借方　部門コード", false);
		checkString(kariTorihikisakiCd, 1, 12, "借方　取引先コード", false);
		checkString(kariKamokuCd, 1, 6, "借方　科目コード", false);
		checkString(kariEdabanCd, 1, 12, "借方　枝番コード", false);
		checkString(kariKoujiCd, 1, 10, "借方　工事コード", false);
		checkString(kariKoushuCd, 1, 6, "借方　工種コード", false);
		checkString(kariProjectCd, 1, 12, "借方　プロジェクトコード", false);
		checkString(kariSegmentCd, 1, 8, "借方　セグメントコード", false);
		checkString(kariUf1Cd, 1, 20, "借方　ユニバーサルフィールド１", false);
		checkString(kariUf2Cd, 1, 20, "借方　ユニバーサルフィールド２", false);
		checkString(kariUf3Cd, 1, 20, "借方　ユニバーサルフィールド３", false);
		checkString(tekiyou, 1, 60, "摘要", false);
		checkString(tekiyouCd, 1, 4, "摘要コード", false);
		checkString(kashiBumonCd, 1, 8, "貸方　部門コード", false);
		checkString(kashiTorihikisakiCd, 1, 12, "貸方　取引先コード", false);
		checkString(kashiKamokuCd, 1, 6, "貸方　科目コード", false);
		checkString(kashiEdabanCd, 1, 12, "貸方　枝番コード", false);
		checkString(kashiKoujiCd, 1, 10, "貸方　工事コード", false);
		checkString(kashiKoushuCd, 1, 6, "貸方　工種コード", false);
		checkString(kashiProjectCd, 1, 12, "貸方　プロジェクトコード", false);
		checkString(kashiSegmentCd, 1, 8, "貸方　セグメントコード", false);
		checkString(kashiUf1Cd, 1, 20, "貸方　ユニバーサルフィールド１", false);
		checkString(kashiUf2Cd, 1, 20, "貸方　ユニバーサルフィールド２", false);
		checkString(kashiUf3Cd, 1, 20, "貸方　ユニバーサルフィールド３", false);
		checkKingakuOver0(taikaKingaku, "対価金額", false);
		checkKingakuMinus(kingaku, "金額", false);
		checkString(shouhizeiTaishoKamokuCd, 1, 6, "消費税対象科目コード", false);
		checkNumber(shouhizeiTaishoKamokuZeiritsu, 1, 3, "消費税対象科目税率", false);
		checkDomain(shouhizeiTaishoKeigenZeiritsuKbn, EteamConst.Domain.FLG, "消費税対象科目　軽減税率区分", false);
		checkString(shouhizeiTaishoKamokuKazeiKbn, 1, 3, "消費税対象科目　課税区分", false);
		checkString(shouhizeiTaishoKamokuGyoushuKbn, 1, 1, "消費税対象科目　業種区分", false);
		checkString(shouhizeiTaishoKamokuShiireKbn, 1, 1, "消費税対象科目　仕入区分", false);
		checkNumber(kariZeiritsu, 1, 3, "借方　税率", false);
		checkDomain(kariKeigenZeiritsuKbn, EteamConst.Domain.FLG, "借方　軽減税率区分", false);
		checkNumber(kashiZeiritsu, 1, 3, "貸方　税率", false);
		checkDomain(kashiKeigenZeiritsuKbn, EteamConst.Domain.FLG, "貸方　軽減税率区分", false);
		checkString(kariKazeiKbn, 1, 3, "借方　課税区分", false);
		checkString(kariGyoushuKbn, 1, 1, "借方　業種区分", false);
		checkString(kariShiireKbn, 1, 1, "借方　仕入区分", false);
		checkString(kashiKazeiKbn, 1, 3, "貸方　課税区分", false);
		checkString(kashiGyoushuKbn, 1, 1, "貸方　業種区分", false);
		checkString(kashiShiireKbn, 1, 1, "貸方　仕入区分", false);
		//画面上とチェック順合わせるならココカラ
		checkString(kariUrizeikeisan, 1, 1, "借方　併用売上税額計算方式", false);
		checkString(kashiUrizeikeisan, 1, 1, "貸方　併用売上税額計算方式", false);
		checkString(zeiUrizeikeisan, 1, 1, "消費税対象科目　併用売上税額計算方式", false);
		checkString(kariMenzeikeika, 1, 1, "借方　仕入税額控除経過措置割合", false);
		checkString(kashiMenzeikeika, 1, 1, "貸方　仕入税額控除経過措置割合", false);
		checkString(zeiMenzeikeika, 1, 1, "消費税対象科目　仕入税額控除経過措置割合", false);
		//ココマデ
		checkDate(shiharaibi, "支払日", false);
		checkString(shiharaiKbn, 1, 2, "支払区分", false);
		checkDate(shiharaiKijitu, "支払期日", false);
		checkDate(kaishubi, "回収日", false);
		checkString(nyukinKbn, 1, 2, "入金区分", false);
		checkDate(kaishuKijitu, "回収期日", false);
		checkString(tenkenFlg, 1, 1, "店券フラグ", false);
		checkString(keshikomiCd, 1, 12, "消込コード", false);
		checkDate(kihyouNengappi, "起票年月日", false);
		checkString(kihyouBumonCd, 1, 8, "起票部門コード", false);
		checkString(kihyoushaCd, 1, 12, "起票者コード", false);
		checkString(nyuryokushaCd, 1, 4, "入力者コード", false);
		checkString(husenNo, 1, 2, "付箋番号", false);
		checkString(shouninGroupNo, 1, 4, "承認グループNo.", false);
		checkString(bunriKbn, 1, 1, "分離区分", false);
		checkString(rate, 1, 12, "レート", false);
		checkKingakuOver0(gaikaTaikaKingaku, "外貨対価金額", false);
		checkKingakuOver0(gaikaKingaku, "外貨金額", false);
		checkString(kugiri, 1, 1, "区切り", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
		//項目				 		EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
		String[][] list = {
				{denpyouId, "伝票ID", "2", "2", "2" },
				{serialNo, "シリアル番号", "0", "2", "2" },
		};
		hissuCheckCommon(list, eventNum);
	}
	
	/**
	 * 初期表示
	 * @return ResultName
	 */
	public String init(){
		
		// 入力チェック
		formatCheck();
		hissuCheck(1);
		
		connection = EteamConnection.connect();

		try{
			// 色々読み込み
			displaySeigyo();
			
			// 仕訳データ検索処理
			GMap shiwakeData = shiwakeDataHenkouLogic.selectShiwakeData(Integer.parseInt(serialNo), denpyouId);
			
			// 0件の場合
			if(shiwakeData == null){
				throw new EteamDataNotFoundException();
			}
			
			// フィールドに検索結果をつめる
			shiwakeStatusCd = (String) shiwakeData.get("shiwake_status");
			denpyouDate =  formatDate(shiwakeData.get("dymd"));
			seiriTukiFlg = (String) shiwakeData.get("seiri");
			denpyouNo = (String) shiwakeData.get("dcno");
			kariBumonCd = (String) shiwakeData.get("rbmn");
			kariBumonName = (String) shiwakeData.get("kari_bumon_name");
			kariTorihikisakiCd = (String) shiwakeData.get("rtor");
			kariTorihikisakiName = (String) shiwakeData.get("kari_torihikisaki_name");
			kariKamokuCd = (String) shiwakeData.get("rkmk");
			kariKamokuName = (String) shiwakeData.get("kari_kamoku_name");
			kariEdabanCd = (String) shiwakeData.get("reda");
			kariEdabanName = (String) shiwakeData.get("kari_edaban_name");
			kariKoujiCd = (String) shiwakeData.get("rkoj");
			kariKoushuCd = (String) shiwakeData.get("rkos");
			kariProjectCd = (String) shiwakeData.get("rprj");
			kariProjectName = (String) shiwakeData.get("kari_project_name");
			kariSegmentCd = (String) shiwakeData.get("rseg");
			kariSegmentName = (String) shiwakeData.get("kari_segment_name");
			kariUf1Cd = (String) shiwakeData.get("rdm1");
			kariUf1Name = (String) shiwakeData.get("kari_uf1_name");
			kariUf2Cd = (String) shiwakeData.get("rdm2");
			kariUf2Name = (String) shiwakeData.get("kari_uf2_name");
			kariUf3Cd = (String) shiwakeData.get("rdm3");
			kariUf3Name = (String) shiwakeData.get("kari_uf3_name");
			tekiyou = (String) shiwakeData.get("tky");
			tekiyouCd = (String) shiwakeData.get("tno");
			kashiBumonCd = (String) shiwakeData.get("sbmn");
			kashiBumonName = (String) shiwakeData.get("kashi_bumon_name");
			kashiTorihikisakiCd = (String) shiwakeData.get("stor");
			kashiTorihikisakiName = (String) shiwakeData.get("kashi_torihikisaki_name");
			kashiKamokuCd = (String) shiwakeData.get("skmk");
			kashiKamokuName = (String) shiwakeData.get("kashi_kamoku_name");
			kashiEdabanCd = (String) shiwakeData.get("seda");
			kashiEdabanName = (String) shiwakeData.get("kashi_edaban_name");
			kashiKoujiCd = (String) shiwakeData.get("skoj");
			kashiKoushuCd = (String) shiwakeData.get("skos");
			kashiProjectCd = (String) shiwakeData.get("sprj");
			kashiProjectName = (String) shiwakeData.get("kashi_project_name");
			kashiSegmentCd = (String) shiwakeData.get("sseg");
			kashiSegmentName = (String) shiwakeData.get("kashi_segment_name");
			kashiUf1Cd = (String) shiwakeData.get("sdm1");
			kashiUf1Name = (String) shiwakeData.get("kashi_uf1_name");
			kashiUf2Cd = (String) shiwakeData.get("sdm2");
			kashiUf2Name = (String) shiwakeData.get("kashi_uf2_name");
			kashiUf3Cd = (String) shiwakeData.get("sdm3");
			kashiUf3Name = (String) shiwakeData.get("kashi_uf3_name");
			taikaKingaku = formatMoney(shiwakeData.get("exvl"));
			kingaku = formatMoney(shiwakeData.get("valu"));
			shouhizeiTaishoKamokuCd = (String) shiwakeData.get("zkmk");
			shouhizeiTaishoKamokuZeiritsu = formatMoney(shiwakeData.get("zrit"));
			shouhizeiTaishoKeigenZeiritsuKbn = shiwakeData.get("zkeigen");
			shouhizeiTaishoKamokuKazeiKbn = (String) shiwakeData.get("zzkb");
			shouhizeiTaishoKamokuGyoushuKbn = (String) shiwakeData.get("zgyo");
			shouhizeiTaishoKamokuShiireKbn = (String) shiwakeData.get("zsre");
			kariZeiritsu = formatMoney(shiwakeData.get("rrit"));
			kariKeigenZeiritsuKbn = shiwakeData.get("rkeigen");
			kashiZeiritsu = formatMoney(shiwakeData.get("srit"));
			kashiKeigenZeiritsuKbn = shiwakeData.get("skeigen");
			kariKazeiKbn = (String) shiwakeData.get("rzkb");
			kariGyoushuKbn = (String) shiwakeData.get("rgyo");
			kariShiireKbn = (String) shiwakeData.get("rsre");
			kashiKazeiKbn = (String) shiwakeData.get("szkb");
			kashiGyoushuKbn = (String) shiwakeData.get("sgyo");
			kashiShiireKbn = (String) shiwakeData.get("ssre");
			shiharaibi = formatDate(shiwakeData.get("symd"));
			shiharaiKbn = (String) shiwakeData.get("skbn");
			shiharaiKijitu = formatDate(shiwakeData.get("skiz"));
			kaishubi = formatDate(shiwakeData.get("uymd"));
			nyukinKbn = (String) shiwakeData.get("ukbn");
			kaishuKijitu = formatDate(shiwakeData.get("ukiz"));
			tenkenFlg = (String) shiwakeData.get("sten");
			keshikomiCd = (String) shiwakeData.get("dkec");
			kihyouNengappi =formatDate(shiwakeData.get("kymd"));
			kihyouBumonCd = (String) shiwakeData.get("kbmn");
			kihyoushaCd = (String) shiwakeData.get("kusr");
			nyuryokushaCd = (String) shiwakeData.get("fusr");
			husenNo = (String) shiwakeData.get("fsen");
			shouninGroupNo = (String) shiwakeData.get("sgno");
			bunriKbn = (String) shiwakeData.get("bunri");
			rate = (String) shiwakeData.get("rate");
			gaikaTaikaKingaku =  (String) shiwakeData.get("gexvl");
			gaikaKingaku =  (String) shiwakeData.get("gvalu");
			kugiri =  (String) shiwakeData.get("gsep");
			
			kariUrizeikeisan = (String) shiwakeData.get("rurizeikeisan");
			kashiUrizeikeisan = (String) shiwakeData.get("surizeikeisan");
			zeiUrizeikeisan = (String) shiwakeData.get("zurizeikeisan");
			kariMenzeikeika = (String) shiwakeData.get("rmenzeikeika");
			kashiMenzeikeika = (String) shiwakeData.get("smenzeikeika");
			zeiMenzeikeika = (String) shiwakeData.get("zmenzeikeika");
			
			return "success";
		}finally{
			connection.close();
		}
	}
	
	/**
	 * 更新処理
	 * @return ResultName
	 */
	public String koushin(){
		
		// 入力チェック
		formatCheck();
		hissuCheck(2);
		
		connection = EteamConnection.connect();

		try{
			// 色々読み込み
			displaySeigyo();
			
			// エラーがあったら遷移
			if (0 < errorList.size())
			{
				return "error";
			}

			wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
			DenpyouIchiranUpdateLogic diLogic = EteamContainer.getComponent(DenpyouIchiranUpdateLogic.class, connection);
			
			// 更新前のデータをDBから取得
			GMap shiwakeData = shiwakeDataHenkouLogic.selectShiwakeData(Integer.parseInt(serialNo), denpyouId);
			
			// 画面から入力されたデータとDBのデータを比較し、承認状況用のコメントを作成
			String comment = createComment(shiwakeData);
			
			// 更新データ
			GMap dataMap = new GMap();
			dataMap.put("serial_no", Integer.parseInt(serialNo));
			dataMap.put("denpyou_id", denpyouId);
			dataMap.put("dymd", toDate(denpyouDate));
			dataMap.put("seiri", seiriTukiFlg);
			dataMap.put("dcno", denpyouNo);
			dataMap.put("rbmn", kariBumonCd);
			dataMap.put("rtor", kariTorihikisakiCd);
			dataMap.put("rkmk", kariKamokuCd);
			dataMap.put("reda", kariEdabanCd);
			dataMap.put("rkoj", kariKoujiCd);
			dataMap.put("rkos", kariKoushuCd);
			dataMap.put("rprj", kariProjectCd);
			dataMap.put("rseg", kariSegmentCd);
			dataMap.put("rdm1", kariUf1Cd);
			dataMap.put("rdm2", kariUf2Cd);
			dataMap.put("rdm3", kariUf3Cd);
			dataMap.put("tky", tekiyou);
			dataMap.put("tno", tekiyouCd);
			dataMap.put("sbmn", kashiBumonCd);
			dataMap.put("stor", kashiTorihikisakiCd);
			dataMap.put("skmk", kashiKamokuCd);
			dataMap.put("seda", kashiEdabanCd);
			dataMap.put("skoj", kashiKoujiCd);
			dataMap.put("skos", kashiKoushuCd);
			dataMap.put("sprj", kashiProjectCd);
			dataMap.put("sseg", kashiSegmentCd);
			dataMap.put("sdm1", kashiUf1Cd);
			dataMap.put("sdm2", kashiUf2Cd);
			dataMap.put("sdm3", kashiUf3Cd);
			dataMap.put("exvl", toDecimal(taikaKingaku));
			dataMap.put("valu", toDecimal(kingaku));
			dataMap.put("zkmk", shouhizeiTaishoKamokuCd);
			dataMap.put("zrit", toDecimal(shouhizeiTaishoKamokuZeiritsu));
			dataMap.put("zkeigen", shouhizeiTaishoKeigenZeiritsuKbn);
			dataMap.put("zzkb", shouhizeiTaishoKamokuKazeiKbn);
			dataMap.put("zgyo", shouhizeiTaishoKamokuGyoushuKbn);
			dataMap.put("zsre", shouhizeiTaishoKamokuShiireKbn);
			dataMap.put("rrit", toDecimal(kariZeiritsu));
			dataMap.put("rkeigen", kariKeigenZeiritsuKbn);
			dataMap.put("srit", toDecimal(kashiZeiritsu));
			dataMap.put("skeigen", kashiKeigenZeiritsuKbn);
			dataMap.put("rzkb", kariKazeiKbn);
			dataMap.put("rgyo", kariGyoushuKbn);
			dataMap.put("rsre", kariShiireKbn);
			dataMap.put("szkb", kashiKazeiKbn);
			dataMap.put("sgyo", kashiGyoushuKbn);
			dataMap.put("ssre", kashiShiireKbn);
			dataMap.put("symd", toDate(shiharaibi));
			dataMap.put("skbn", shiharaiKbn);
			dataMap.put("skiz", toDate(shiharaiKijitu));
			dataMap.put("uymd", toDate(kaishubi));
			dataMap.put("ukbn", nyukinKbn);
			dataMap.put("ukiz", toDate(kaishuKijitu));
			dataMap.put("sten", tenkenFlg);
			dataMap.put("dkec", keshikomiCd);
			dataMap.put("kymd", toDate(kihyouNengappi));
			dataMap.put("kbmn", kihyouBumonCd);
			dataMap.put("kusr", kihyoushaCd);
			dataMap.put("fusr", nyuryokushaCd);
			dataMap.put("fsen", husenNo);
			dataMap.put("sgno", shouninGroupNo);
			dataMap.put("bunri", bunriKbn);
			dataMap.put("rate", rate);
			dataMap.put("gexvl", gaikaTaikaKingaku.replaceAll(",", ""));
			dataMap.put("gvalu", gaikaKingaku.replaceAll(",", ""));
			dataMap.put("gsep", kugiri);
			
			dataMap.put("rurizeikeisan", kariUrizeikeisan);
			dataMap.put("surizeikeisan", kashiUrizeikeisan);
			dataMap.put("zurizeikeisan", zeiUrizeikeisan);
			dataMap.put("rmenzeikeika", kariMenzeikeika);
			dataMap.put("smenzeikeika", kashiMenzeikeika);
			dataMap.put("zmenzeikeika", zeiMenzeikeika);
			
			// 更新処理
			shiwakeDataHenkouLogic.updateShiwakeData(dataMap);
			
			// 承認状況登録処理
			wfEventLogic.insertShouninJoukyouRoleKengen(denpyouId, getUser(), "仕訳データ変更", comment);
			
			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);

			// コミット
			connection.commit();
			
		}finally{
			connection.close();
		}
		
		return "success";
	}

	/**
	 * 仕訳対象外更新処理
	 * @return ResultName
	 */
	public String shiwakeTaishougaiKoushin(){
		
		// 入力チェック
		formatCheck();
		hissuCheck(3);
		
		connection = EteamConnection.connect();
		
		try{
			
			// 色々読み込み
			displaySeigyo();
			
			// エラーがあったら遷移
			if (0 < errorList.size())
			{
				return "error";
			}

			wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
			DenpyouIchiranUpdateLogic diLogic = EteamContainer.getComponent(DenpyouIchiranUpdateLogic.class, connection);
			
			// 更新処理
			shiwakeDataHenkouLogic.updateShiwakeStatus(SHIWAKE_STATUS.SHIWAKE_TAISHOGAI, Integer.parseInt(serialNo));
			
			// 承認状況登録処理
			wfEventLogic.insertShouninJoukyouRoleKengen(denpyouId, getUser(), "仕訳対象外", "");
			
			//伝票一覧テーブル状態更新
			diLogic.updateDenpyouIchiran(denpyouId);
			
			// コミット
			connection.commit();
			
			return "success";
		}finally{
			connection.close();
		}
	}
	
	/**
	 * 入力値とDBデータを比較し、変更部分を状況欄用に編集する
	 * @param shiwakeData DBデータ
	 * @return 状況欄用コメント
	 */
	protected String createComment(GMap shiwakeData) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(compareData(formatDate(shiwakeData.get("dymd")), denpyouDate, "伝票日付"));
		sb.append(compareData((String) shiwakeData.get("seiri"), seiriTukiFlg, "整理月フラグ"));
		sb.append(compareData((String) shiwakeData.get("dcno"), denpyouNo, "伝票番号"));
		sb.append(compareData((String) shiwakeData.get("rbmn"), kariBumonCd, "借方　部門コード"));
		sb.append(compareData((String) shiwakeData.get("rtor"), kariTorihikisakiCd, "借方　取引先コード"));
		sb.append(compareData((String) shiwakeData.get("rkmk"), kariKamokuCd, "借方　科目コード"));
		sb.append(compareData((String) shiwakeData.get("reda"), kariEdabanCd, "借方　枝番コード"));
		sb.append(compareData((String) shiwakeData.get("rkoj"), kariKoujiCd, "借方　工事コード"));
		sb.append(compareData((String) shiwakeData.get("rkos"), kariKoushuCd, "借方　工種コード"));
		sb.append(compareData((String) shiwakeData.get("rprj"), kariProjectCd, "借方　プロジェクトコード"));
		sb.append(compareData((String) shiwakeData.get("rseg"), kariSegmentCd, "借方　セグメントコード"));
		sb.append(compareData((String) shiwakeData.get("rdm1"), kariUf1Cd, "借方　ユニバーサルフィールド１"));
		sb.append(compareData((String) shiwakeData.get("rdm2"), kariUf2Cd, "借方　ユニバーサルフィールド２"));
		sb.append(compareData((String) shiwakeData.get("rdm3"), kariUf3Cd, "借方　ユニバーサルフィールド３"));
		sb.append(compareData((String) shiwakeData.get("tky"), tekiyou, "摘要"));
		sb.append(compareData((String) shiwakeData.get("tno"), tekiyouCd, "摘要コード"));
		sb.append(compareData((String) shiwakeData.get("sbmn"), kashiBumonCd, "貸方　部門コード"));
		sb.append(compareData((String) shiwakeData.get("stor"), kashiTorihikisakiCd, "貸方　取引先コード"));
		sb.append(compareData((String) shiwakeData.get("skmk"), kashiKamokuCd, "貸方　科目コード"));
		sb.append(compareData((String) shiwakeData.get("seda"), kashiEdabanCd, "貸方　枝番コード"));
		sb.append(compareData((String) shiwakeData.get("skoj"), kashiKoujiCd, "貸方　工事コード"));
		sb.append(compareData((String) shiwakeData.get("skos"), kashiKoushuCd, "貸方　工種コード"));
		sb.append(compareData((String) shiwakeData.get("sprj"), kashiProjectCd, "貸方　プロジェクトコード"));
		sb.append(compareData((String) shiwakeData.get("sseg"), kashiSegmentCd, "貸方　セグメントコード"));
		sb.append(compareData((String) shiwakeData.get("sdm1"), kashiUf1Cd, "貸方　ユニバーサルフィールド１"));
		sb.append(compareData((String) shiwakeData.get("sdm2"), kashiUf2Cd, "貸方　ユニバーサルフィールド２"));
		sb.append(compareData((String) shiwakeData.get("sdm3"), kashiUf3Cd, "貸方　ユニバーサルフィールド３"));
		sb.append(compareData(formatMoney(shiwakeData.get("exvl")), taikaKingaku, "対価金額"));
		sb.append(compareData(formatMoney(shiwakeData.get("valu")), kingaku, "金額"));
		sb.append(compareData((String) shiwakeData.get("zkmk"), shouhizeiTaishoKamokuCd, "消費税対象科目コード"));
		sb.append(compareData(formatMoney(shiwakeData.get("zrit")), shouhizeiTaishoKamokuZeiritsu, "消費税対象科目税率"));
		sb.append(compareData(shiwakeData.get("zkeigen"), shouhizeiTaishoKeigenZeiritsuKbn, "消費税対象科目　軽減税率区分"));
		sb.append(compareData((String) shiwakeData.get("zzkb"), shouhizeiTaishoKamokuKazeiKbn, "消費税対象科目　課税区分"));
		sb.append(compareData((String) shiwakeData.get("zgyo"), shouhizeiTaishoKamokuGyoushuKbn, "消費税対象科目　業種区分"));
		sb.append(compareData((String) shiwakeData.get("zsre"), shouhizeiTaishoKamokuShiireKbn, "消費税対象科目　仕入区分"));
		sb.append(compareData(formatMoney(shiwakeData.get("rrit")), kariZeiritsu, "借方　税率"));
		sb.append(compareData((shiwakeData.get("rkeigen")), kariKeigenZeiritsuKbn, "借方　軽減税率区分"));
		sb.append(compareData(formatMoney(shiwakeData.get("srit")), kashiZeiritsu, "貸方　税率"));
		sb.append(compareData((shiwakeData.get("skeigen")), kashiKeigenZeiritsuKbn, "貸方　軽減税率区分"));
		sb.append(compareData((String) shiwakeData.get("rzkb"), kariKazeiKbn, "借方　課税区分"));
		sb.append(compareData((String) shiwakeData.get("rgyo"), kariGyoushuKbn, "借方　業種区分"));
		sb.append(compareData((String) shiwakeData.get("rsre"), kariShiireKbn, "借方　仕入区分"));
		sb.append(compareData((String) shiwakeData.get("szkb"), kashiKazeiKbn, "貸方　課税区分"));
		sb.append(compareData((String) shiwakeData.get("sgyo"), kashiGyoushuKbn, "貸方　業種区分"));
		sb.append(compareData((String) shiwakeData.get("ssre"), kashiShiireKbn, "貸方　仕入区分"));
		sb.append(compareData(formatDate(shiwakeData.get("symd")), shiharaibi, "支払日"));
		sb.append(compareData((String) shiwakeData.get("skbn"), shiharaiKbn, "支払区分"));
		sb.append(compareData(formatDate(shiwakeData.get("skiz")), shiharaiKijitu, "支払期日"));
		sb.append(compareData(formatDate(shiwakeData.get("uymd")), kaishubi, "回収日"));
		sb.append(compareData((String) shiwakeData.get("ukbn"), nyukinKbn, "入金区分"));
		sb.append(compareData(formatDate(shiwakeData.get("ukiz")), kaishuKijitu, "回収期日"));
		sb.append(compareData((String) shiwakeData.get("sten"), tenkenFlg, "店券フラグ"));
		sb.append(compareData((String) shiwakeData.get("dkec"), keshikomiCd, "消込コード"));
		sb.append(compareData(formatDate(shiwakeData.get("kymd")), kihyouNengappi, "起票年月日"));
		sb.append(compareData((String) shiwakeData.get("kbmn"), kihyouBumonCd, "起票部門コード"));
		sb.append(compareData((String) shiwakeData.get("kusr"), kihyoushaCd, "起票者コード"));
		sb.append(compareData((String) shiwakeData.get("fusr"), nyuryokushaCd, "入力者コード"));
		sb.append(compareData((String) shiwakeData.get("fsen"), husenNo, "付箋番号"));
		sb.append(compareData((String) shiwakeData.get("sgno"), shouninGroupNo, "承認グループNo."));
		sb.append(compareData((String) shiwakeData.get("bunri"), bunriKbn, "分離区分"));
		sb.append(compareData((String) shiwakeData.get("rate"), rate, "レート"));
		sb.append(compareData(formatMoneyForString((String) shiwakeData.get("gexvl")), gaikaTaikaKingaku, "外貨対価金額"));
		sb.append(compareData(formatMoneyForString((String) shiwakeData.get("gvalu")), gaikaKingaku, "外貨金額"));
		sb.append(compareData(formatMoneyForString((String) shiwakeData.get("gsep")), kugiri, "区切り"));
		
		//インボイス
		sb.append(compareData(formatMoneyForString((String) shiwakeData.get("rurizeikeisan")), kariUrizeikeisan, "借方　併用売上税額計算方式"));
		sb.append(compareData(formatMoneyForString((String) shiwakeData.get("rmenzeikeika")), kariMenzeikeika, "借方　仕入税額控除経過措置割合"));
		sb.append(compareData(formatMoneyForString((String) shiwakeData.get("surizeikeisan")), kashiUrizeikeisan, "貸方　併用売上税額計算方式"));
		sb.append(compareData(formatMoneyForString((String) shiwakeData.get("smenzeikeika")), kashiMenzeikeika, "貸方　仕入税額控除経過措置割合"));
		sb.append(compareData(formatMoneyForString((String) shiwakeData.get("zurizeikeisan")), zeiUrizeikeisan, "消費税対象科目 併用売上税額計算方式"));
		sb.append(compareData(formatMoneyForString((String) shiwakeData.get("zmenzeikeika")), zeiMenzeikeika, "消費税対象科目 仕入税額控除経過措置割合"));

		return sb.toString();
	}

	/**
	 * 変更前と変更後のデータを比較し、コメント文を出力する
	 * 
	 * @param dbData DB内のデータ(変更前データ)
	 * @param nyuryokuData 入力データ(変更後データ)
	 * @param colmnName 項目名
	 * @return コメント文
	 */
	protected String compareData(String dbData, String nyuryokuData, String colmnName){
		
		String comment = "";

		// データが変更されていたらコメント文を出力
		if(!dbData.equals(nyuryokuData)){
			
			// データが空orNULLの箇所に文言を追加
			if(isEmpty(dbData)){
				dbData = "データなし";
			}
			
			if(isEmpty(nyuryokuData)){
				nyuryokuData = "データなし";
			}
			
			comment = colmnName + ":" + dbData + "→" + nyuryokuData + "\r\n";
		}
		
		return comment;
	}
	
	/**
	 * String型の金額をフォーマットする
	 * @param data 変換対象データ
	 * @return フォーマットされた値
	 */
	protected String formatMoneyForString(String data) {
		
		// nullまたは空以外の時、フォーマットする
		if(!isEmpty(data)){
			return String.format("%,d", Long.parseLong(data));
		}
		return data;
	}
	
	/**
	 * 画面表示イベントやイベントエラー表示時用に、画面の共通制御処理を行う。
	 */
	protected void displaySeigyo(){
		
		shiwakeDataHenkouLogic = EteamContainer.getComponent(ShiwakeDataDe3HenkouLogic.class, connection);
		
		// リンク番号取得処理
		serialNoList =  shiwakeDataHenkouLogic.selectSerialNo(denpyouId);
		
		// データ0件の場合、エラーメッセージを表示する
		if(serialNoList.size() == 0){
			throw new EteamDataNotFoundException();
		}
		
		// 初期表示の場合、
		if(isEmpty(serialNo)){
			serialNo = serialNoList.get(0).get("serial_no").toString(); // シリアル番号最小値を取得
		}
		
		// 仕訳抽出状態の名称変換処理
		for(GMap map : serialNoList){
			GMap shiwakeStatus = shiwakeDataHenkouLogic.selectNaibuCdSetting
					(SHIWAKE_STATUS_NM, (String)map.get("shiwake_status"));
			// 取得した情報をmapに詰める
			map.put("shiwake_status_nm",shiwakeStatus.get("name"));
			// 現在表示中の仕訳ステータス
			if (serialNo.equals(map.get("serial_no").toString())) {
				shiwakeStatusCd = (String)map.get("shiwake_status");
			}
		}
		
		// 伝票IDから戻り先のURLを生成する。
		GMap map = shiwakeDataHenkouLogic.selectDenpyouInfo(denpyouId);
		backDenpyouUrl = map.get("denpyou_shubetsu_url")+ "?denpyouId=" + denpyouId + "&denpyouKbn=" + map.get("denpyou_kbn");
	}
}
