package eteam.gyoumu.kaikei.kogamen;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Domain;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.open21.Open21Env;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.NaibuCdSettingAbstractDao;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.abstractdao.ShouhizeiritsuAbstractDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.NaibuCdSettingDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import eteam.database.dao.ShouhizeiritsuDao;
import eteam.database.dto.KamokuMaster;
import eteam.database.dto.NaibuCdSetting;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.database.dto.Shouhizeiritsu;
import eteam.gyoumu.houjincard.HoujinCardLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.kaikei.KaikeiCommonLogic.InputEnableInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票明細追加ダイアログAction
 */
@Getter @Setter @ToString
public class DenpyouMeisaiAction extends EteamAbstractAction {

	//＜定数＞

	//＜画面入力＞
		/** 伝票区分 */
		String denpyouId;
		/** 伝票区分 */
		String denpyouKbn;

		/** インデックス */
		String index;
		/** 最大インデックス */
		String maxIndex;
		/** 仮払あり・なし（金額0許容なら1、それ以外は0またはブランク） */
		String zeroEnabled;

		/** 出張区分 */
		String kaigaiFlg;
		/** 使用者ID */
		String userId;
		/** 使用者名前 */
		String userName;
		/** 社員番号 */
		String shainNo;
		/** 使用日 */
		String shiyoubi;
		/** 証憑書類フラグ */
		String shouhyouShorui;
		/** 仕訳枝番号 */
		String shiwakeEdaNo;
		/** 取引名 */
		String torihikiName;
		/** 勘定科目コード */
		String kamokuCd;
		/** 勘定科目名 */
		String kamokuName;
		/** 勘定科目枝番コード */
		String kamokuEdabanCd;
		/** 勘定科目枝番名 */
		String kamokuEdabanName;
		/** 負担部門コード */
		String futanBumonCd;
		/** 負担部門名 */
		String futanBumonName;
		/** 取引先コード */
		String torihikisakiCd;
		/** 取引先名 */
		String torihikisakiName;
		/** 振込先 */
		String furikomisakiJouhou;
		/** プロジェクトコード */
		String projectCd;
		/** プロジェクト名 */
		String projectName;
		/** セグメントコード */
		String segmentCd;
		/** セグメント名 */
		String segmentName;
		/** ユニバーサルフィールド１コード */
		String uf1Cd;
		/** ユニバーサルフィールド１名 */
		String uf1Name;
		/** ユニバーサルフィールド２コード */
		String uf2Cd;
		/** ユニバーサルフィールド２名 */
		String uf2Name;
		/** ユニバーサルフィールド３コード */
		String uf3Cd;
		/** ユニバーサルフィールド３名 */
		String uf3Name;
		/** ユニバーサルフィールド４コード */
		String uf4Cd;
		/** ユニバーサルフィールド４名 */
		String uf4Name;
		/** ユニバーサルフィールド５コード */
		String uf5Cd;
		/** ユニバーサルフィールド５名 */
		String uf5Name;
		/** ユニバーサルフィールド６コード */
		String uf6Cd;
		/** ユニバーサルフィールド６名 */
		String uf6Name;
		/** ユニバーサルフィールド７コード */
		String uf7Cd;
		/** ユニバーサルフィールド７名 */
		String uf7Name;
		/** ユニバーサルフィールド８コード */
		String uf8Cd;
		/** ユニバーサルフィールド８名 */
		String uf8Name;
		/** ユニバーサルフィールド９コード */
		String uf9Cd;
		/** ユニバーサルフィールド９名 */
		String uf9Name;
		/** ユニバーサルフィールド１０コード */
		String uf10Cd;
		/** ユニバーサルフィールド１０名 */
		String uf10Name;
		/** 課税区分 */
		String kazeiKbn;
		/** 税率 */
		String zeiritsu;
		/** 軽減税率区分 */
		String keigenZeiritsuKbn;
		/** 幣種 */
		String heishu;
		/** レート */
		String rate;
		/** 外貨 */
		String gaika;
		/** 支払金額 */
		String shiharaiKingaku;
		/** 本体金額 */
		String hontaiKingaku;
		/** 消費税額 */
		String shouhizeigaku;
		/** 摘要 */
		String tekiyou;
		/** 交際費表示フラグ */
		String kousaihiHyoujiFlg;
		/** 人数項目表示フラグ */
		String ninzuuRiyouFlg;
		/** 交際費詳細 */
		String kousaihiShousai;
		/** 交際費人数 */
		String kousaihiNinzuu;
		/** 交際費一人あたり金額 */
		String kousaihiHitoriKingaku;
		/** 法人カード利用 */
		String houjinCardFlgKeihi;
		/** 会社手配 */
		String kaishaTehaiFlgKeihi;
		/** 法人カード履歴紐付番号(経費) */
		String himodukeCardMeisaiKeihi;
		
		/** 支払先名 */
		String shiharaisakiName;
		/** 分離区分 */ 
		String bunriKbn;
		/** 仕入区分 */
		String kariShiireKbn;
		
		/** インボイス伝票フラグ */
		String invoiceDenpyou;
		/** 処理グループ */
		String shoriGroup;


	//＜画面入力以外＞

		//画面制御情報
		/** HF・UF制御クラス */
		HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
		/** 画面項目制御クラス(経費明細) */
		GamenKoumokuSeigyo ks1;
		/** 社員コード連携エリア */
		String shainCdRenkeiArea = EteamSettingInfo.getShainCdRenkeiSaki();
		/** 入力モード */
		boolean enableInput;
		/** 勘定科目枝番選択ボタン押下可否 */
		boolean kamokuEdabanEnable;
		/** 負担部門選択ボタン */
		boolean futanBumonEnable;
		/** 取引先選択ボタン */
		boolean torihikisakiEnable;
		/** プロジェクトコード表示 */
		String pjShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
		/** プロジェクト選択ボタン */
		boolean projectEnable;
		/** セグメントコード表示 */
		String segmentShiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
		/** セグメント選択ボタン */
		boolean segmentEnable;
		/** 交際費表示 */
		boolean kousaihiEnable;
		/** 人数項目表示 */
		boolean ninzuuEnable;
		/** UF1ボタン押下可否 */
		boolean uf1Enable;
		/** UF2ボタン押下可否 */
		boolean uf2Enable;
		/** UF3ボタン押下可否 */
		boolean uf3Enable;
		/** UF4ボタン押下可否 */
		boolean uf4Enable;
		/** UF5ボタン押下可否 */
		boolean uf5Enable;
		/** UF6ボタン押下可否 */
		boolean uf6Enable;
		/** UF7ボタン押下可否 */
		boolean uf7Enable;
		/** UF8ボタン押下可否 */
		boolean uf8Enable;
		/** UF9ボタン押下可否 */
		boolean uf9Enable;
		/** UF10ボタン押下可否 */
		boolean uf10Enable;
		/** 消費税率変更可否 */
		boolean zeiritsuEnable;
		/** 旅費仮払でないかどうか */
		boolean isNotRyohikaribarai;
		/** 経費立替精算であるかどうか */
		boolean isKeihiTatekaeSeisan;
		/** 海外旅費の伝票区分（そうでないときは空） */
		String kaigaiDenpyouKbn;
		/** 法人カード利用表示制御 */
		boolean enableHoujinCard;
		/** 会社手配表示制御 */
		boolean enableKaishaTehai;
		/** 外貨表示制御 */
		boolean enableGaika;
		/** 邦貨換算端数処理方法 */
		String houkaKansanHansuu = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.HOUKA_KANSAN_HASUU);

		/** 法人カード利用明細の日付変更制御 */
		boolean houjinCardDateEnabled = setting.houjinCardDateEnabled().equals("1");

		//プルダウン等の候補値
		/** 消費税率DropDownList */
		List<Shouhizeiritsu> zeiritsuList;
		/** 消費税率ドメイン */
		String[] zeiritsuDomain;
		/** 海外旅費精算用 税率リスト項目総数 */
		int zeiritsuListCount;
		/** 課税区分DropDownList */
		List<GMap> kazeiKbnList;
		/** 課税区分ドメイン */
		String[] kazeiKbnDomain;
		/** 分離区分ドメイン */
		String[] bunriKbnDomain;
		/** 分離区分DropDownList */
		List<GMap> bunriKbnList;
		/** 仕入区分ドメイン */
		String[] shiireKbnDomain;
		/** 仕入区分DropDownList */
		List<GMap> shiireKbnList;
		/** 事業者区分ドメイン */
		String[] jigyoushaKbnDomain;
		/** 事業者区分DropDownList */
		List<GMap> jigyoushaKbnList;
		/** 入力方式ドメイン */
		String[] nyuryokuHoushikiDomain;
		/** 入力方式DropDownList */
		List<GMap> nyuryokuHoushikiList;
		/** インボイス伝票ドメイン */
		String[] invoiceDenpyouDomain;
		/** インボイス伝票DropDownList */
		List<NaibuCdSetting> invoiceDenpyouList;
		/**	税込or税抜ならtrue */
		boolean kazeiKbnCheck;

	//＜部品＞
		/** システム管理ロジック */
		SystemKanriCategoryLogic sysLogic;
		/** 会計SELECT */
		KaikeiCategoryLogic kaikeiLogic;
		/** 会計共通ロジック */
		KaikeiCommonLogic commonLg;
		/** 部門ユーザー管理ロジック */
		BumonUserKanriCategoryLogic bumonUsrLogic;
		/** マスターSELECT */
		MasterKanriCategoryLogic masterLogic;
		/** 法人カード情報用ロジック */
		HoujinCardLogic hcLogic;
		/** 税率Dao */
		ShouhizeiritsuAbstractDao zeiritsuDao;
		/** 科目マスタDao */
		KamokuMasterDao  kamokuMasterDao;
		/** 内部コード設定Dao */
		NaibuCdSettingAbstractDao naibuCdSettingDao;
		/** 仕訳パターンマスターDao */
		ShiwakePatternMasterAbstractDao shiwakePatternMasterDao;

	//＜入力チェック＞
	@Override
	protected void formatCheck() {
		// 項目									//項目名													//キー項目？
		checkString (denpyouKbn, 4, 4, "伝票区分", true);
		checkNumber (index, 1, 3, "インデックス", false);
		checkNumber (maxIndex, 1, 3, "最大インデックス", false);
		if(denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN)) {
			if (isKeihiTatekaeSeisan) {
				//姓名の間に空白が入るので21文字
				checkString (userName, 1, 21, ks1.userName.getName() + "名", false);
				checkString (shainNo,  1, 15, ks1.userName.getName() + "社員番号", false);
			}
			checkDate (shiyoubi, ks1.shiyoubi.getName(), false);
			if (isNotRyohikaribarai) {
				checkDomain (shouhyouShorui, EteamConst.Domain.FLG,ks1.shouhyouShoruiFlg.getName(), false);
				checkDomain (houjinCardFlgKeihi, Domain.FLG, ks1.houjinCardRiyou.getName(), false);
				checkDomain (kaishaTehaiFlgKeihi, Domain.FLG, ks1.kaishaTehai.getName(), false);
			}
		}
		checkNumber (shiwakeEdaNo, 1, 10, ks1.torihiki.getName() + "コード", false);
		checkString (torihikiName, 1, 20, ks1.torihiki.getName() + "名", false);
		checkString (kamokuCd, 1, 6, ks1.kamoku.getName() + "コード", false);
		checkString (kamokuName, 1, 22, ks1.kamoku.getName() + "名", false);
		checkString (kamokuEdabanCd, 1, 12, ks1.kamokuEdaban.getName() + "コード", false);
		checkString (kamokuEdabanName, 1, 20, ks1.kamokuEdaban.getName() + "名", false);
		checkString (futanBumonCd, 1, 8, ks1.futanBumon.getName() + "コード", false);
		checkString (futanBumonName, 1, 20, ks1.futanBumon.getName() + "名", false);
		checkString (torihikisakiCd, 1, 12, ks1.torihikisaki.getName() + "コード", false);
		checkString (torihikisakiName, 1, 20, ks1.torihikisaki.getName() + "名", false);
		checkString (projectCd, 1, 12, ks1.project.getName() + "コード", false);
		checkString (projectName, 1, 20, ks1.project.getName() + "名", false);
		checkString (segmentCd, 1, 8, ks1.segment.getName() + "コード", false);
		checkString (segmentName, 1, 20, ks1.segment.getName() + "名", false);
		checkString (uf1Cd, 1, 20, hfUfSeigyo.getUf1Name(), false);
		checkString (uf1Name, 1, 20, hfUfSeigyo.getUf1Name(), false);
		checkString (uf2Cd, 1, 20, hfUfSeigyo.getUf2Name(), false);
		checkString (uf2Name, 1, 20, hfUfSeigyo.getUf2Name(), false);
		checkString (uf3Cd, 1, 20, hfUfSeigyo.getUf3Name(), false);
		checkString (uf3Name, 1, 20, hfUfSeigyo.getUf3Name(), false);
		checkString (uf4Cd, 1, 20, hfUfSeigyo.getUf4Name(), false);
		checkString (uf4Name, 1, 20, hfUfSeigyo.getUf4Name(), false);
		checkString (uf5Cd, 1, 20, hfUfSeigyo.getUf5Name(), false);
		checkString (uf5Name, 1, 20, hfUfSeigyo.getUf5Name(), false);
		checkString (uf6Cd, 1, 20, hfUfSeigyo.getUf6Name(), false);
		checkString (uf6Name, 1, 20, hfUfSeigyo.getUf6Name(), false);
		checkString (uf7Cd, 1, 20, hfUfSeigyo.getUf7Name(), false);
		checkString (uf7Name, 1, 20, hfUfSeigyo.getUf7Name(), false);
		checkString (uf8Cd, 1, 20, hfUfSeigyo.getUf8Name(), false);
		checkString (uf8Name, 1, 20, hfUfSeigyo.getUf8Name(), false);
		checkString (uf9Cd, 1, 20, hfUfSeigyo.getUf9Name(), false);
		checkString (uf9Name, 1, 20, hfUfSeigyo.getUf9Name(), false);
		checkString (uf10Cd, 1, 20, hfUfSeigyo.getUf10Name(), false);
		checkString (uf10Name, 1, 20, hfUfSeigyo.getUf10Name(), false);
		checkDomain (kazeiKbn, kazeiKbnDomain, ks1.kazeiKbn.getName(), false);
		if(kazeiKbnCheck) {
			checkDomain (zeiritsu, zeiritsuDomain, ks1.zeiritsu.getName(), false);
			checkDomain (keigenZeiritsuKbn, Domain.FLG, "軽減税率区分", false);
		}
		checkDomain (bunriKbn, bunriKbnDomain, ks1.bunriKbn.getName(), false);
		checkDomain (kariShiireKbn, shiireKbnDomain, ks1.shiireKbn.getName(), false);
		if (zeroEnabled != null && zeroEnabled.equals("1")) {
			checkKingakuOver0 (shiharaiKingaku, ks1.shiharaiKingaku.getName(), false);
		} else {
			checkKingakuOver1 (shiharaiKingaku, ks1.shiharaiKingaku.getName(), false);
		}
		if (!kaigaiDenpyouKbn.equals("")) {
			checkString (heishu,1 ,4 , ks1.heishu.getName() ,false);
			checkKingakuDecimalMoreThan0(rate, ks1.rate.getName() ,false);
			checkKingakuDecimalOver1 (gaika, ks1.gaika.getName() ,false);
		}
		checkKingakuOver0 (hontaiKingaku, "本体金額", false);
		checkKingakuOver0 (shouhizeigaku, "消費税額", false);
		checkString (tekiyou, 1, commonLg.tekiyouCheck(Open21Env.getVersion().toString()), ks1.tekiyou.getName(), false);
		checkSJIS (tekiyou, ks1.tekiyou.getName());
		checkString (kousaihiShousai, 1, 240, kousaihiEnable ? ks1.kousaihiShousai.getName() : "", false);
		checkNumberOver1 (kousaihiNinzuu, 1, 6, (kousaihiEnable && ninzuuEnable) ? ks1.kousaihiShousai.getName() + "人数" : "", false);
		checkKingakuOver0 (kousaihiHitoriKingaku, (kousaihiEnable && ninzuuEnable) ? ks1.kousaihiShousai.getName() + "一人当たりの金額" : "", false);
	}

	@Override
	protected void hissuCheck(int eventNum) {
// int shoriGroup = kamokuMasterDao.find(kamokuCd).shoriGroup;
// GMap invoiceKoumoku = commonLg.getFromKamoku(invoiceDenpyou,shoriGroup,shouhizeikbn,kazeiKbn,shiireZeiAnbun);
		//一応使用可能 まだ使っていないのでCO　DenpyouMeisaiActionからKaikeiCommonLogicに移動

		switch (denpyouKbn) {
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
			if (isKeihiTatekaeSeisan) {
				//項目						項目名 										必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				hissuCheckCommon(new String[][]{
					{userName, ks1.userName.getName() + "名", ks1.userName.getHissuFlgS(), "0"},
					{shainNo, ks1.userName.getName() + "社員番号", ks1.userName.getHissuFlgS(), "0"},
				}, eventNum);
			}
			hissuCheckCommon(new String[][]{
				{shiyoubi, ks1.shiyoubi.getName(), ks1.shiyoubi.getHissuFlgS(), "0"},
				{shiwakeEdaNo, ks1.torihiki.getName() + "コード", ks1.torihiki.getHissuFlgS(), "0"},
				{torihikiName, ks1.torihiki.getName() + "名", ks1.torihiki.getHissuFlgS(), "0"},
				{kamokuCd, ks1.kamoku.getName() + "コード", ks1.kamoku.getHissuFlgS(), "0"},
				{kamokuName, ks1.kamoku.getName() + "名", ks1.kamoku.getHissuFlgS(), "0"},
				{kamokuEdabanCd, ks1.kamokuEdaban.getName()+ "コード", ks1.kamokuEdaban.getHissuFlgS(), "0"},
				{kamokuEdabanName, ks1.kamokuEdaban.getName()+ "名", ks1.kamokuEdaban.getHissuFlgS(), "0"},
				{futanBumonCd, ks1.futanBumon.getName() + "コード：", ks1.futanBumon.getHissuFlgS()},
				{futanBumonName, ks1.futanBumon.getName() + "名：", ks1.futanBumon.getHissuFlgS()},
				{torihikisakiCd, ks1.torihikisaki.getName()+ "コード", ks1.torihikisaki.getHissuFlgS()},
				{torihikisakiName, ks1.torihikisaki.getName()+ "名", ks1.torihikisaki.getHissuFlgS()},
				{projectCd, ks1.project.getName() + "コード", !pjShiyouFlg.equals("0") && ks1.project.getHyoujiFlg() ? ks1.project.getHissuFlgS() : "0"},
				{projectName, ks1.project.getName() + "名", !pjShiyouFlg.equals("0") && ks1.project.getHyoujiFlg() ? ks1.project.getHissuFlgS() : "0"},
				{segmentCd, ks1.segment.getName() + "コード", !segmentShiyouFlg.equals("0") && ks1.segment.getHyoujiFlg() ? ks1.segment.getHissuFlgS() : "0"},
				{segmentName, ks1.segment.getName() + "名", !segmentShiyouFlg.equals("0") && ks1.segment.getHyoujiFlg() ? ks1.segment.getHissuFlgS() : "0"},
				{shiharaiKingaku, ks1.shiharaiKingaku.getName(), ks1.shiharaiKingaku.getHissuFlgS(), "0"},
				{tekiyou, ks1.tekiyou.getName(), ks1.tekiyou.getHissuFlgS(), "0"},
				{kousaihiShousai, ks1.kousaihiShousai.getName(), kousaihiEnable ? ks1.kousaihiShousai.getHissuFlgS() : "0", "0"},
				{kousaihiNinzuu, ks1.kousaihiShousai.getName() + "人数", (kousaihiEnable && ninzuuEnable) ? "1" : "0", "0"},
				{kousaihiHitoriKingaku, ks1.kousaihiShousai.getName() + "一人当たりの金額", (kousaihiEnable && ninzuuEnable) ? "1" : "0", "0"},
				{denpyouKbn, "伝票区分", "2", "0"},
				//{shiharaisakiName, ks1.shiharaisakiName.getName(), ks1.shiharaisakiName.getHissuFlgS(), "0"}, 経費立替は現時点で支払先未対応なのでCO
			}, eventNum);
			if (isNotRyohikaribarai) {
				hissuCheckCommon(new String[][]{
					{shouhyouShorui, ks1.shouhyouShoruiFlg.getName(), ks1.shouhyouShoruiFlg.getHissuFlgS(), "0"},
					{houjinCardFlgKeihi, ks1.houjinCardRiyou.getName(), "0",ks1.houjinCardRiyou.getHissuFlgS()},
					{kaishaTehaiFlgKeihi, ks1.kaishaTehai.getName(), "0",ks1.kaishaTehai.getHissuFlgS()},
				}, eventNum);
			}
			if (!kaigaiDenpyouKbn.equals("")) {
				hissuCheckCommon(new String[][]{
					{heishu, ks1.heishu.getName(), "0",ks1.heishu.getHissuFlgS()},
					{rate, ks1.rate.getName(), "0",ks1.rate.getHissuFlgS()},
					{gaika, ks1.gaika.getName(), "0",ks1.gaika.getHissuFlgS()},
				}, eventNum);
			}
			break;
		case DENPYOU_KBN.SEIKYUUSHO_BARAI:
			hissuCheckCommon(new String[][]{
				//項目						項目名 										必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{shiwakeEdaNo, ks1.torihiki.getName() + "コード", ks1.torihiki.getHissuFlgS(), "0"},
				{torihikiName, ks1.torihiki.getName() + "名", ks1.torihiki.getHissuFlgS(), "0"},
				{kamokuCd, ks1.kamoku.getName() + "コード", ks1.kamoku.getHissuFlgS(), "0"},
				{kamokuName, ks1.kamoku.getName() + "名", ks1.kamoku.getHissuFlgS(), "0"},
				{futanBumonCd, ks1.futanBumon.getName() + "コード：", ks1.futanBumon.getHissuFlgS()},
				{futanBumonName, ks1.futanBumon.getName() + "名：", ks1.futanBumon.getHissuFlgS()},
				{torihikisakiCd, ks1.torihikisaki.getName()+ "コード", ks1.torihikisaki.getHissuFlgS()},
				{torihikisakiName, ks1.torihikisaki.getName()+ "名", ks1.torihikisaki.getHissuFlgS()},
				{furikomisakiJouhou, ks1.furikomisakiJouhou.getName(), ks1.furikomisakiJouhou.getHissuFlgS(), "0"},
				{projectCd, ks1.project.getName() + "コード", !pjShiyouFlg.equals("0") && ks1.project.getHyoujiFlg() ? ks1.project.getHissuFlgS() : "0"},
				{projectName, ks1.project.getName() + "名", !pjShiyouFlg.equals("0") && ks1.project.getHyoujiFlg() ? ks1.project.getHissuFlgS() : "0"},
				{segmentCd, ks1.segment.getName() + "コード", !segmentShiyouFlg.equals("0") && ks1.segment.getHyoujiFlg() ? ks1.segment.getHissuFlgS() : "0"},
				{segmentName, ks1.segment.getName() + "名", !segmentShiyouFlg.equals("0") && ks1.segment.getHyoujiFlg() ? ks1.segment.getHissuFlgS() : "0"},
				{shiharaiKingaku, ks1.shiharaiKingaku.getName(), ks1.shiharaiKingaku.getHissuFlgS(), "0"},
				{tekiyou, ks1.tekiyou.getName(), ks1.tekiyou.getHissuFlgS(), "0"},
				{kousaihiShousai, ks1.kousaihiShousai.getName(), kousaihiEnable ? ks1.kousaihiShousai.getHissuFlgS() : "0", "0"},
				{kousaihiNinzuu, ks1.kousaihiShousai.getName() + "人数", (kousaihiEnable && ninzuuEnable) ? "1" : "0", "0"},
				{kousaihiHitoriKingaku, ks1.kousaihiShousai.getName() + "一人当たりの金額", (kousaihiEnable && ninzuuEnable) ? "1" : "0", "0"},
				{denpyouKbn, "伝票区分", "2", "0"},
			}, eventNum);
			break;
		case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
			hissuCheckCommon(new String[][]{
				//項目						項目名 										必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{shiwakeEdaNo, ks1.torihiki.getName() + "コード", ks1.torihiki.getHissuFlgS(), "0"},
				{torihikiName, ks1.torihiki.getName() + "名", ks1.torihiki.getHissuFlgS(), "0"},
				{kamokuCd, ks1.kamoku.getName() + "コード", ks1.kamoku.getHissuFlgS(), "0"},
				{kamokuName, ks1.kamoku.getName() + "名", ks1.kamoku.getHissuFlgS(), "0"},
				{futanBumonCd, ks1.futanBumon.getName() + "コード：", ks1.futanBumon.getHissuFlgS()},
				{futanBumonName, ks1.futanBumon.getName() + "名：", ks1.futanBumon.getHissuFlgS()},
				{torihikisakiCd, ks1.torihikisaki.getName()+ "コード", ks1.torihikisaki.getHissuFlgS()},
				{torihikisakiName, ks1.torihikisaki.getName()+ "名", ks1.torihikisaki.getHissuFlgS()},
				{projectCd, ks1.project.getName() + "コード", !pjShiyouFlg.equals("0") && ks1.project.getHyoujiFlg() ? ks1.project.getHissuFlgS() : "0"},
				{projectName, ks1.project.getName() + "名", !pjShiyouFlg.equals("0") && ks1.project.getHyoujiFlg() ? ks1.project.getHissuFlgS() : "0"},
				{segmentCd, ks1.segment.getName() + "コード", !segmentShiyouFlg.equals("0") && ks1.segment.getHyoujiFlg() ? ks1.segment.getHissuFlgS() : "0"},
				{segmentName, ks1.segment.getName() + "名", !segmentShiyouFlg.equals("0") && ks1.segment.getHyoujiFlg() ? ks1.segment.getHissuFlgS() : "0"},
				{shiharaiKingaku, ks1.shiharaiKingaku.getName(), ks1.shiharaiKingaku.getHissuFlgS(), "0"},
				{tekiyou, ks1.tekiyou.getName(), ks1.tekiyou.getHissuFlgS(), "0"},
				{denpyouKbn, "伝票区分", "2", "0"},
				/* 課税区分・消費税率・分離区分・仕入区分
				 * 科目の処理グループを取得してからhissuCheck
				 * 課税区分　「未設定」はどうする 自動引落伝票以外にも必須チェック必要そうな感じ
				 * TODO 画面側の表示非表示制御が解決していないためまだコメントアウト (String)mp.get("kazeiHissu") */
// {kazeiKbn, ks1.kazeiKbn.getName(), (String)invoiceKoumoku.get("kazeiHissu"),"0"},
// {zeiritsu, ks1.zeiritsu.getName(), (String)invoiceKoumoku.get("zeiHissu"),"0"},
// {bunriKbn, ks1.bunriKbn.getName(), (String)invoiceKoumoku.get("bunriHissu"),"0"},
// {kariShiireKbn, ks1.shiireKbn.getName(), (String)invoiceKoumoku.get("shiireHissu"),"0"},
				
			}, eventNum);
			break;
		}

		if ( !hfUfSeigyo.getUf1ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf1Cd ,hfUfSeigyo.getUf1Name() + "コード"		,ks1.uf1.getHissuFlgS()},
			}, eventNum);
		}

		if ( !hfUfSeigyo.getUf2ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf2Cd ,hfUfSeigyo.getUf2Name() + "コード"		,ks1.uf2.getHissuFlgS()},
			}, eventNum);
		}

		if ( !hfUfSeigyo.getUf3ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf3Cd ,hfUfSeigyo.getUf3Name() + "コード"		,ks1.uf3.getHissuFlgS()},
			}, eventNum);
		}

		if ( !hfUfSeigyo.getUf4ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf4Cd ,hfUfSeigyo.getUf4Name() + "コード"		,ks1.uf4.getHissuFlgS()},
			}, eventNum);
		}

		if ( !hfUfSeigyo.getUf5ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf5Cd ,hfUfSeigyo.getUf5Name() + "コード"		,ks1.uf5.getHissuFlgS()},
			}, eventNum);
		}

		if ( !hfUfSeigyo.getUf6ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf6Cd ,hfUfSeigyo.getUf6Name() + "コード"		,ks1.uf6.getHissuFlgS()},
			}, eventNum);
		}

		if ( !hfUfSeigyo.getUf7ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf7Cd ,hfUfSeigyo.getUf7Name() + "コード"		,ks1.uf7.getHissuFlgS()},
			}, eventNum);
		}

		if ( !hfUfSeigyo.getUf8ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf8Cd ,hfUfSeigyo.getUf8Name() + "コード"		,ks1.uf8.getHissuFlgS()},
			}, eventNum);
		}

		if ( !hfUfSeigyo.getUf9ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf9Cd ,hfUfSeigyo.getUf9Name() + "コード"		,ks1.uf9.getHissuFlgS()},
			}, eventNum);
		}

		if ( !hfUfSeigyo.getUf10ShiyouFlg().equals("0")) {
			hissuCheckCommon(new String[][]{
				{uf10Cd ,hfUfSeigyo.getUf10Name() + "コード"		,ks1.uf10.getHissuFlgS()},
			}, eventNum);
		}

		hissuCheckCommon(new String[][]{
			{zeiritsu ,ks1.zeiritsu.getName(), kazeiKbnCheck ? ks1.zeiritsu.getHissuFlgS() : "0", "0"},
			{keigenZeiritsuKbn ,"軽減税率区分", kazeiKbnCheck ? ks1.zeiritsu.getHissuFlgS() : "0", "0"},
		}, eventNum);
	}

	//＜内部処理＞
	/**
	 * 初期化処理
	 * @param connection コネクション
	 */
	protected void initParts(EteamConnection connection) {
		sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		commonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		bumonUsrLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		hcLogic = EteamContainer.getComponent(HoujinCardLogic.class, connection);
		this.zeiritsuDao = EteamContainer.getComponent(ShouhizeiritsuDao.class, connection);
		this.shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);
		this.naibuCdSettingDao = EteamContainer.getComponent(NaibuCdSettingDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
	}

	/**
	 * 証憑書類フラグのデフォルト制御
	 */
	protected void setShouhyouShoruiFlg() {

		// 初回(null)の場合のみ設定
		if (shouhyouShorui != null)
		{
			return;
		}
		switch(denpyouKbn){
		//旅費精算、旅費仮払、海外旅費精算、海外旅費仮払も経費立替の内容で制御している
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:

			//証憑書類フラグがnullの場合、経費明細の画面項目制御を適用
			if (ks1.shouhyouShoruiFlg == null)
			{
				return;
			}

			//項目非表示の場合はセットしない。
			if (false==ks1.shouhyouShoruiFlg.getHyoujiFlg())
			{
				return;
			}

			// 経費明細はA001の定義で制御
			shouhyouShorui = setting.shouhyouShoruiDefaultA001();

			break;
		default:
			// shouHyouShoruiなしor別エリア
			break;
		}
	}

	/**
	 * 画面表示イベントや登録等イベントのエラー表示時用に、画面の共通制御処理を行う。
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {

		//プルダウンのリストを取得s
		zeiritsuList = this.zeiritsuDao.load();
		zeiritsuDomain = zeiritsuList.stream().map(item -> item.zeiritsu.toString()).collect(Collectors.toList()).toArray(new String[0]);
		kazeiKbnList = naibuCdSettingDao.loadByCdNameOrderByPositiveHyoujiJun("kazei_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		kazeiKbnDomain = EteamCommon.mapList2Arr(kazeiKbnList, "naibu_cd");
		bunriKbnList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("bunri_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		bunriKbnDomain = EteamCommon.mapList2Arr(bunriKbnList, "naibu_cd");
		shiireKbnList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("shiire_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		shiireKbnDomain = EteamCommon.mapList2Arr(shiireKbnList, "naibu_cd");
		jigyoushaKbnList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("jigyousha_kbn").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		jigyoushaKbnDomain = EteamCommon.mapList2Arr(jigyoushaKbnList, "naibu_cd");
		nyuryokuHoushikiList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("nyuryoku_flg").stream().map(NaibuCdSetting::getMap).collect(Collectors.toList());
		nyuryokuHoushikiDomain = EteamCommon.mapList2Arr(bunriKbnList, "naibu_cd");
		invoiceDenpyouList = naibuCdSettingDao.loadByCdNameOrderByHyoujiJun("invoice_denpyou");
		invoiceDenpyouDomain = invoiceDenpyouList.stream().map(NaibuCdSetting::getNaibuCd).collect(Collectors.toList()).toArray(new String[0]);
		
		//海外旅費精算 その他経費(海外)にて税率リスト表示・非表示制御に使用
		zeiritsuListCount = zeiritsuList.size();
		
		kamokuEdabanEnable = false;
		futanBumonEnable = false;
		torihikisakiEnable = false;
		projectEnable = false;
		segmentEnable = false;
		kousaihiEnable = false;
		ninzuuEnable = false;
		uf1Enable = false;
		uf2Enable = false;
		uf3Enable = false;
		uf4Enable = false;
		uf5Enable = false;
		uf6Enable = false;
		uf7Enable = false;
		uf8Enable = false;
		uf9Enable = false;
		uf10Enable = false;
		zeiritsuEnable = false;
		kazeiKbnCheck = false;
		
		//課税区分の制御
		if (isNotEmpty(kamokuCd)) {
			KamokuMaster kmk = kamokuMasterDao.find(kamokuCd);
			if (kmk != null) {
				shoriGroup = kmk.shoriGroup.toString();
			}
		}
		
		//20231027 法人カード日付の変更について 以下経費精算でも変更制御が係るようにしていたが一旦CO
		//if(!List.of(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN, DENPYOU_KBN.RYOHI_SEISAN,DENPYOU_KBN.KAIGAI_RYOHI_SEISAN).contains(denpyouKbn)){		
		// 出張旅費精算、海外出張旅費精算でなければ、法人カード利用明細の日付変更制御はfalseで固定。
		if(!List.of(DENPYOU_KBN.RYOHI_SEISAN,DENPYOU_KBN.KAIGAI_RYOHI_SEISAN).contains(denpyouKbn)){
			houjinCardDateEnabled = false;
		}

		//旅費精算・旅費仮払申請のとき経費立替申請の伝票区分を使用する
		//ただし旅費仮払申請では、証憑と法人カードは表示しない
		isNotRyohikaribarai = !(denpyouKbn.equals(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI));
		kaigaiDenpyouKbn = denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI) ? denpyouKbn : "";
		isKeihiTatekaeSeisan = denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN);

		// 一部伝票は経費立替申請の内容で制御する
		denpyouKbn = denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) ||
					denpyouKbn.equals(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI) ||
					denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) ||
					denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI) ? DENPYOU_KBN.KEIHI_TATEKAE_SEISAN : denpyouKbn;

		// denpyouKbnに応じて制御クラスを生成
		ks1 = new GamenKoumokuSeigyo(denpyouKbn);

		//税率の初期設定
		if (zeiritsu == null ) {
			if(isNotEmpty(shiwakeEdaNo)) {
				zeiritsu = "0";
			}else {
				GMap initZeiritsu = masterLogic.findValidShouhizeiritsuMap();
				zeiritsu = initZeiritsu.get("zeiritsu").toString();
				keigenZeiritsuKbn = initZeiritsu.get("keigen_zeiritsu_kbn");
			}
		}

		if (enableInput) {
			//明細単位に仕訳パターンによる制御
			if (isNotEmpty(shiwakeEdaNo)) {
				//初期表示時点や未登録 or では取引選択されていないので各ボタン押下不能なまま。
				//仕訳枝番号がある = 取引選択されている状態では、仕訳パターンをみて、ボタン押下可能にする。
				//海外旅費・海外旅費仮払かつ海外フラグが１のとき、海外用の経費仕訳を取得する
				String denpyouKbnKeihi = denpyouKbn;
				if (!kaigaiDenpyouKbn.equals("") && kaigaiFlg.equals("1")) {
					denpyouKbnKeihi = "A901";
				}
				ShiwakePatternMaster shiwakePattern = this.shiwakePatternMasterDao.find(denpyouKbnKeihi, Integer.parseInt(shiwakeEdaNo));
				InputEnableInfo info = commonLg.judgeInputEnable(shiwakePattern.map);
				kamokuEdabanEnable = info.kamokuEdabanEnable;
				futanBumonEnable = info.futanBumonEnable;
				torihikisakiEnable = info.torihikisakiEnable;
				kousaihiEnable = info.kousaihiEnable;
				ninzuuEnable = info.ninzuuEnable;
				projectEnable = info.projectEnable;
				segmentEnable = info.segmentEnable;
				uf1Enable = info.uf1Enable;
				uf2Enable = info.uf2Enable;
				uf3Enable = info.uf3Enable;
				uf4Enable = info.uf4Enable;
				uf5Enable = info.uf5Enable;
				uf6Enable = info.uf6Enable;
				uf7Enable = info.uf7Enable;
				uf8Enable = info.uf8Enable;
				uf9Enable = info.uf9Enable;
				uf10Enable = info.uf10Enable;
				zeiritsuEnable = info.zeiritsuEnable;
			}
		} else {
			kousaihiEnable = "1".equals(kousaihiHyoujiFlg);
			ninzuuEnable = "1".equals(ninzuuRiyouFlg);
		}

		// 以下は画面項目制御テーブルより優先されるロジック
		if (!kaigaiDenpyouKbn.equals("")) {
			// 海外伝票は経費立替精算で制御だが以下置き換える
			var ksKaigai = new GamenKoumokuSeigyo(kaigaiDenpyouKbn);
			ks1.shucchouKbn = ksKaigai.shucchouKbn;
			ks1.heishu = ksKaigai.heishu;
			ks1.rate = ksKaigai.rate;
			ks1.gaika = ksKaigai.gaika;
		}

		if (!isKeihiTatekaeSeisan) {
			// 使用者は経費立替精算でのみ表示
			ks1.userName = null;
		}
		if (!isNotRyohikaribarai) {
			// 旅費仮払伝票は経費立替精算で制御だが以下未使用
			ks1.shouhyouShoruiFlg = null;
			ks1.houjinCardRiyou = null;
			ks1.kaishaTehai = null;
		}

		if(denpyouKbn.equals(DENPYOU_KBN.SEIKYUUSHO_BARAI)
			|| denpyouKbn.equals(DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU)) {
			// この証憑書類フラグは明細部でなく申請部の領収書・請求書等に対する制御だから
			ks1.shouhyouShoruiFlg = null;
		}

		//使用者=起票者をセット。
		//代理起票の場合は
		//経費立替精算：ScriptのsetDialogMeisaiInfoで消しちゃう
		//旅費精算系　：通らない（userIdを申請画面側で取得しているため）
		//追加ではなく変更・参照の場合は、setDialogMeisaiInfoで一覧情報(meisaiInfo)で上書いちゃう
		//なので、通常起票の追加の時、以外はこのコードは意味ない
		if (isEmpty(userId) && isKeihiTatekaeSeisan) {
			//ログインユーザー
			userId   = getUser().getSeigyoUserId();
			userName = getUser().getDisplayUserNameShort();//userInfo.getDisplayUserName();
			shainNo  = getUser().getShainNo();

			// 起票ユーザーID
			if (StringUtils.isNotEmpty(denpyouId)) {
				GMap kihyouUser = commonLg.findKihyouUser(denpyouId);
				if(kihyouUser != null && kihyouUser.get("user_id") != null){
					userId = kihyouUser.get("user_id").toString();
					GMap kihyouUserInfo = bumonUsrLogic.selectUserInfo(userId);
					userName = kihyouUserInfo == null ? "" : kihyouUserInfo.get("user_sei") + "　" + kihyouUserInfo.get("user_mei");
					shainNo = (kihyouUserInfo == null) ? "" : (String)kihyouUserInfo.get("shain_no");
				}
			}
		}

		// 法人カードの表示可否
		// 経費立替精算：代理起票のときは画面側で新規追加時は非表示にする
		// 旅費精算系　：代理起票のときは申請画面のユーザーにより新規追加時は表示制御
		GMap userInfo = bumonUsrLogic.selectUserInfo(userId);
		if (userInfo == null) {
			enableHoujinCard = false;
		} else {
			enableHoujinCard = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.HOUJIN_CARD) &&
							   userInfo.get("houjin_card_riyou_flag").equals("1") &&
							   (denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN));
		}
		// 会社手配の表示可否
		enableKaishaTehai = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.KAISHA_TEHAI) &&
							(denpyouKbn.equals(DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) || denpyouKbn.equals(DENPYOU_KBN.KEIHI_TATEKAE_SEISAN));
		// 外貨等の表示可否
		if (!kaigaiDenpyouKbn.equals("")) {
			enableGaika = sysLogic.judgeKinouSeigyoON(EteamNaibuCodeSetting.KINOU_SEIGYO_CD.GAIKA);
		} else {
			enableGaika = true;
		}
		if (!isEmpty(kazeiKbn)){
			kazeiKbnCheck = List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn);
		}
		
	}
	

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);
			displaySeigyo(connection);
			setShouhyouShoruiFlg();
		}

		return "success";
	}

	/**
	 * ダイアログ確定
	 * @return 結果
	 */
	public String confirm() {

		try(EteamConnection connection = EteamConnection.connect()) {
			initParts(connection);
			displaySeigyo(connection);

			formatCheck();
			hissuCheck(1);

			//法人カード使用履歴と紐づく明細の場合、指定使用者のカード番号が履歴のカード番号と一致するかチェック
			if( (!isEmpty(himodukeCardMeisaiKeihi)) && (!isEmpty(userId)) ){
				if(!hcLogic.isValidUserForCardMeisai(himodukeCardMeisaiKeihi, userId)){
					errorList.add("法人カード使用履歴のカード番号と[" + userId + "]の法人カード識別用番号が一致しません。");
				}
			}

			if (!errorList.isEmpty()) {
				return "error";
			}
		}

		return "success";
	}
}