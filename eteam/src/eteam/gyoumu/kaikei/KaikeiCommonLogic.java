package eteam.gyoumu.kaikei;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.Env;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.ShiwakeConst;
import eteam.common.EteamConst.yosanCheckKikan;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_KBN;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.SHIHARAI_HOUHOU;
import eteam.common.EteamNaibuCodeSetting.SHIYOU_FLG;
import eteam.common.EteamSettingInfo;
import eteam.common.GamenKoumokuSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.database.abstractdao.BumonMasterAbstractDao;
import eteam.database.abstractdao.KamokuEdabanZandakaAbstractDao;
import eteam.database.abstractdao.KamokuMasterAbstractDao;
import eteam.database.abstractdao.ShouninJoukyouAbstractDao;
import eteam.database.dao.BumonMasterDao;
import eteam.database.dao.KamokuMasterDao;
import eteam.database.dao.KiShouhizeiSettingDao;
import eteam.database.dao.ShouninJoukyouDao;
import eteam.database.dto.KiShouhizeiSetting;
import eteam.database.dto.ShiwakePatternMaster;
import eteam.gyoumu.user.User;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import eteam.gyoumu.yosanshikkoukanri.YosanShikkouKanriCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 会計共通Logic
 */
public class KaikeiCommonLogic extends EteamAbstractLogic {

//＜定数＞
	/** 交際費エラー */
	final int KOUSAI_ERROR_CD_ERROR = -1;
	/** 交際費警告 */
	final int KOUSAI_ERROR_CD_KEIKOKU = -2;
	/** 交際費システムエラー */
	final int KOUSAI_ERROR_CD_SYSERROR = -999;
	/** 交際費チェックタイプ：超過 */
	final int KOUSAI_CHECK_TYPE_CHOUKA = 1;
	/** 交際費チェックタイプ：不足 */
	final int KOUSAI_CHECK_TYPE_FUSOKU = 2;

	/** 仕訳チェックデータ */
	@Getter @Setter @ToString
	public class ShiwakeCheckData{
		/** 仕訳枝番号名 */
		public String shiwakeEdaNoNm;
		/** 仕訳枝番号 */
		public String shiwakeEdaNo;
		/** 取引先名 */
		public String torihikisakiNm;
		/** 取引先コード */
		public String torihikisakiCd;
		/** 勘定科目名 */
		public String kamokuNm;
		/** 勘定科目コード */
		public String kamokuCd;
		/** 勘定科目枝番名 */
		public String kamokuEdabanNm;
		/** 勘定科目枝番コード */
		public String kamokuEdabanCd;
		/** 負担部門名 */
		public String futanBumonNm;
		/** 負担部門コード */
		public String futanBumonCd;
		/** 課税区分名 */
		public String kazeiKbnNm;
		/** 課税区分 */
		public String kazeiKbn;
		/** 分離区分名 */
		public String bunriKbnNm;
		/** 分離区分 */
		public String bunriKbn;
		/** 仕入区分名 */
		public String shiireKbnNm;
		/** 仕入区分 */
		public String shiireKbn;
		/** HF1名 */
		public String hf1Nm;
		/** HF1コード */
		public String hf1Cd;
		/** HF2名 */
		public String hf2Nm;
		/** HF2コード */
		public String hf2Cd;
		/** HF3名 */
		public String hf3Nm;
		/** HF3コード */
		public String hf3Cd;
		/** HF4名 */
		public String hf4Nm;
		/** HF4コード */
		public String hf4Cd;
		/** HF5名 */
		public String hf5Nm;
		/** HF5コード */
		public String hf5Cd;
		/** HF6名 */
		public String hf6Nm;
		/** HF6コード */
		public String hf6Cd;
		/** HF7名 */
		public String hf7Nm;
		/** HF7コード */
		public String hf7Cd;
		/** HF8名 */
		public String hf8Nm;
		/** HF8コード */
		public String hf8Cd;
		/** HF9名 */
		public String hf9Nm;
		/** HF9コード */
		public String hf9Cd;
		/** HF10名 */
		public String hf10Nm;
		/** HF10コード */
		public String hf10Cd;
		/** UF1名 */
		public String uf1Nm;
		/** UF1コード */
		public String uf1Cd;
		/** UF2名 */
		public String uf2Nm;
		/** UF2コード */
		public String uf2Cd;
		/** UF3名 */
		public String uf3Nm;
		/** UF3コード */
		public String uf3Cd;
		/** UF4名 */
		public String uf4Nm;
		/** UF4コード */
		public String uf4Cd;
		/** UF5名 */
		public String uf5Nm;
		/** UF5コード */
		public String uf5Cd;
		/** UF6名 */
		public String uf6Nm;
		/** UF6コード */
		public String uf6Cd;
		/** UF7名 */
		public String uf7Nm;
		/** UF7コード */
		public String uf7Cd;
		/** UF8名 */
		public String uf8Nm;
		/** UF8コード */
		public String uf8Cd;
		/** UF9名 */
		public String uf9Nm;
		/** UF9コード */
		public String uf9Cd;
		/** UF10名 */
		public String uf10Nm;
		/** UF10コード */
		public String uf10Cd;
		/** UF(固定値)1コード */
		public String ufKotei1Cd;
		/** UF(固定値)1名 */
		public String ufKotei1Nm;
		/** UF(固定値)2コード */
		public String ufKotei2Cd;
		/** UF(固定値)2名 */
		public String ufKotei2Nm;
		/** UF(固定値)3コード */
		public String ufKotei3Cd;
		/** UF(固定値)3名 */
		public String ufKotei3Nm;
		/** UF(固定値)4コード */
		public String ufKotei4Cd;
		/** UF(固定値)4名 */
		public String ufKotei4Nm;
		/** UF(固定値)5コード */
		public String ufKotei5Cd;
		/** UF(固定値)5名 */
		public String ufKotei5Nm;
		/** UF(固定値)6コード */
		public String ufKotei6Cd;
		/** UF(固定値)6名 */
		public String ufKotei6Nm;
		/** UF(固定値)7コード */
		public String ufKotei7Cd;
		/** UF(固定値)7名 */
		public String ufKotei7Nm;
		/** UF(固定値)8コード */
		public String ufKotei8Cd;
		/** UF(固定値)8名 */
		public String ufKotei8Nm;
		/** UF(固定値)9コード */
		public String ufKotei9Cd;
		/** UF(固定値)9名 */
		public String ufKotei9Nm;
		/** UF(固定値)10コード */
		public String ufKotei10Cd;
		/** UF(固定値)10名 */
		public String ufKotei10Nm;
		/** プロジェクト名 */
		public String projectNm;
		/** プロジェクトコード */
		public String projectCd;
		/** セグメント名 */
		public String segmentNm;
		/** セグメントコード */
		public String segmentCd;
		/** 財務枝番連携フラグ */
		public String zaimuEdabanRenkei;
	}

	/** システム管理SELECT */
	SystemKanriCategoryLogic systemLg;
	/** マスター管理SELECT */
	MasterKanriCategoryLogic masterLg;
	/** 会計SELECT */
	KaikeiCategoryLogic kaikeiLg;
	/** 部門ユーザーSELECT */
	BumonUserKanriCategoryLogic bumonUserLg;
	/** ワークフローSELECT */
	WorkflowCategoryLogic workflowLg;
	/** ワークフローイベント */
	WorkflowEventControlLogic workflowEventLg;
	/** 承認状況Dao */
	ShouninJoukyouAbstractDao shouninJoukyouDao;
	/** 消費税設定Dao */
	KiShouhizeiSettingDao kiShouhizeiSettingDao;
	/** 科目マスタDao */
	KamokuMasterAbstractDao kamokuMasterDao;
	/** 科目枝番残高マスタ */
	KamokuEdabanZandakaAbstractDao edabanZandaka;
	/** 部門マスタDao */
	BumonMasterAbstractDao bumonMasterDao;

	@Override
	public void init(EteamConnection _connection) {
		super.init(_connection);
		systemLg = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		masterLg = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		kaikeiLg = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
		bumonUserLg = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		workflowLg = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		workflowEventLg = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		this.shouninJoukyouDao = EteamContainer.getComponent(ShouninJoukyouDao.class, connection);
		kiShouhizeiSettingDao = EteamContainer.getComponent(KiShouhizeiSettingDao.class, connection);
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		edabanZandaka =  EteamContainer.getComponent(KamokuEdabanZandakaAbstractDao.class, connection);
		bumonMasterDao = EteamContainer.getComponent(BumonMasterDao.class, connection);
	}

	/**
	 * 個人精算支払日であるかチェックする。
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param date 入力日付(yyyy/mm/dd)
	 * @param name 項目名
	 * @param errorList エラーリスト
	 */
	public void kojinSeisanShiharaibiCheck(Date date, String name, List<String> errorList) {
		GMap hizukeControl = masterLg.findKojinSeisanShiharaibi(date);
		if (!(
			null != hizukeControl
		)) {
			errorList.add(name + "[" + EteamCommon.formatDate(date) + "]は個人精算支払日ではありません。");
		}
	}

	/**
	 * ユーザーの役職コードを取得
	 * ※これ過去データ見る時に結構落ちるので更新モード以外で使ってはだめ。
	 * @param userId ユーザーID
	 * @param denpyouId 伝票ID
	 * @return 役職コード
	 */
	public String getKihyouYakushokuCd(String userId, String denpyouId) {

		String userIdTmp = userId;

		// ユーザーIDがブランクかつ起票者がいる場合、起票者の役職コード
		GMap kihyouUser = findKihyouUser(denpyouId);
		if(isEmpty(userId) && kihyouUser != null && kihyouUser.get("user_id") != null){
			userIdTmp = kihyouUser.get("user_id").toString();
		}

		GMap user = bumonUserLg.selectUserInfo(userIdTmp);
		String shainNo = (String)user.get("shain_no");
		GMap shain = masterLg.findShain(shainNo);
		if (null == shain) {
			throw new RuntimeException("社員マスターなし");
		}
		return (String)shain.get("yakushoku_cd");
	}

	/**
	 * 課税区分の課税グループが税抜であるかどうかを判定する
	 * @param kazeiKbn 課税区分
	 * @return 該当するならtrue
	 */
	public boolean kazeiKbnIsZeinukiGroup(String kazeiKbn) {
		GMap tmp = systemLg.findNaibuCdSetting("kazei_kbn", kazeiKbn);
		if(tmp == null) {
			//支払依頼申請専用の課税区分(課込仕入・非課税仕入)のコードも参照
			tmp = systemLg.findNaibuCdSetting("kazei_kbn_shiharaiirai", kazeiKbn);
		}
		String kazeiKbnGroup = (String)tmp.get("option1");
		return EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEINUKI.equals(kazeiKbnGroup);
	}

	/**
	 * 課税区分の課税グループが税込であるかどうかを判定する
	 * @param kazeiKbn 課税区分
	 * @return 該当するならtrue
	 */
	public boolean kazeiKbnIsZeikomiGroup(String kazeiKbn) {
		GMap tmp = systemLg.findNaibuCdSetting("kazei_kbn", kazeiKbn);
		if(tmp == null) {
			//支払依頼申請専用の課税区分(課込仕入・非課税仕入)のコードも参照
			tmp = systemLg.findNaibuCdSetting("kazei_kbn_shiharaiirai", kazeiKbn);
		}
		String kazeiKbnGroup = (String)tmp.get("option1");
		return EteamNaibuCodeSetting.KAZEI_KBN_GROUP.ZEIKOMI.equals(kazeiKbnGroup);
	}

	/**
	 * 計上日の入力状態・モード(0:非表示、1:入力可(手入力)、2:表示、3:入力可(プルダウン))を判定する
	 * @param hasseiShugi true:発生主義 false:現金主義
	 * @param shinseishaKeijoubiInput 計上日を申請者が入力するか否か、する場合がtrue
	 * @param denpyouId 伝票ID
	 * @param user ユーザー情報
	 * @param enableInput 入力可能か否か
	 * @param NyuryokuMode 入力モード(1:手入力 2:プルダウン)
	 * @return 入力状態
	 */
	public int judgeKeijouBiMode(boolean hasseiShugi, boolean shinseishaKeijoubiInput, String denpyouId, User user, boolean enableInput, String NyuryokuMode) {
		//発生主義の場合
		if(hasseiShugi) {
			if(shinseishaKeijoubiInput){
				//計上日を申請者が入力する設定の場合
				if (enableInput)
				{
					return NyuryokuMode.equals("1") ? 1 : 3;
				} else {
					return 2;
				}
			}else {
				//計上日を経理が入力する設定の場合
				GMap denpyou = workflowLg.selectDenpyou(denpyouId);
				//未起票/起票中は非表示
				if (denpyou == null || EteamNaibuCodeSetting.DENPYOU_JYOUTAI.KIHYOU_CHUU.equals(denpyou.get("denpyou_joutai")))
				{
					return 0;
				}
				//申請中
				if(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU.equals(denpyou.get("denpyou_joutai"))) {
					if (userIsKeiri(user))
					{
						return NyuryokuMode.equals("1") ? 1 : 3;
					} else {
						return 2;
					}
				}
				//承認済
				if (EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SYOUNIN_ZUMI.equals(denpyou.get("denpyou_joutai")))
				{
					return  2;
				}
			}
		}

		//現金主義または発生主義で上記ステータスに該当しない場合は非表示固定
		return 0;
	}

	/**
	 * 計上日の値を設定に沿ってセットする。
	 * @param hasseiShugi true:発生主義 false:現金主義
	 * @param keijouBiMode 計上日の入力状態・モード(0:非表示、1:入力可(手入力)、2:表示、3:入力可(プルダウン))
	 * @param keijoubi 計上日(入力値)
	 * @param keijoubiDefaultSettei 計上日デフォルト設定
	 * @param NyuryokuMode 入力モード(1:手入力 2:プルダウン)
	 * @param denpyouKbn 伝票区分
	 * @param kijunbi 基準日(使用日or精算期間終了日)(入力値)
	 * @param errorList エラーリスト
	 * @param user ユーザー
	 * @return 設定に則った計上日
	 */
	public String setKeijoubi(boolean hasseiShugi, int keijouBiMode, String keijoubi ,String keijoubiDefaultSettei, String NyuryokuMode , String denpyouKbn, String kijunbi, List<String> errorList, User user) {
		if(hasseiShugi) {
			//ユーザーが入力する設定の場合は入力値をそのまま返す
			if (keijouBiMode == 1 || keijouBiMode == 3)
			{
				return keijoubi;
			}

			//設定に則りデフォルト値をセットする
			String keijoubiTmp = null;
			if (keijoubiDefaultSettei.equals("1")) {
				keijoubiTmp = kijunbi;
				if(NyuryokuMode.equals("2") && StringUtils.isNotEmpty(keijoubiTmp))keijoubiTmp = getkeijoubiPulldown(denpyouKbn, keijoubiTmp, errorList);
			}
			if (keijoubiDefaultSettei.equals("2")) {
				//本来は「登録時」または「申請者更新時(申請時)」のみであるべきかもしれないが、現状仕様としては経理以外の承認者更新で計上日が現在日付にリセットされる
				keijoubiTmp = new SimpleDateFormat("yyyy/MM/dd").format((new java.sql.Date(System.currentTimeMillis())));
				if(NyuryokuMode.equals("2"))keijoubiTmp = getkeijoubiPulldown(denpyouKbn, keijoubiTmp, errorList);
			}
			//申請者が入力・最終承認者の編集不可
			if(keijouBiMode == 2 && keijoubiDefaultSettei.equals("3") && userIsKeiri(user)) {
				return keijoubi;
			}
			//デフォルト値設定が「なし」且つ入力者が経理以外の場合はnull固定
			return keijoubiTmp;
		}

		//現金主義の場合はnull固定
		return null;
	}

	/**
	 * 計上日プルダウン入力時、基準日に一番近い未来日付(同日含む)を計上日リストから取得する。
	 * @param denpyouKbn 伝票区分
	 * @param kijunbi 基準日(使用日or精算期間終了日or本日日付)
	 * @param errorList エラーリスト
	 * @return 基準日に一番近い未来日付
	 */
	private String getkeijoubiPulldown(String denpyouKbn, String kijunbi, List<String> errorList){
		String keijoubi = null;
		//計上日(未来日付)取得
		List<String> keijoubiList = masterLg.loadKeijoubiList(denpyouKbn);
		for(String keijoobiTmp:keijoubiList) {
			if(kijunbi.compareTo(keijoobiTmp) <= 0) { //基準日＜=計上日リスト日付の場合
				keijoubi = keijoobiTmp;
				break;
			}
		}
		//未来日付が取得できない場合(恒常的にプルダウン入力で運用していれば起こりえないはずではあるが)
		if(keijoubi == null) errorList.add("計上日に指定できる日付が存在しません。");
		return keijoubi;
	}

	/**
	 * 支払日のチェックを行う。
	 * 最終承認者または経理担当者の場合のみチェックを行う。それ以外の場合は何もせずに終了。
	 * 以下チェック内容
	 * ＜必ず＞
	 * ・支払日が未入力であればエラー。
	 * ・計上日を渡している場合、計上日<=支払日でなければエラー。
	 * ・支払日が未来日でなければエラー。
	 *
	 * ＜支払方法=振込の場合のみ＞※現状振込固定
	 * ・支払日が日付コントロールマスターに登録された個人精算日でなければエラー。
	 * ・支払日が本日より後でなければエラー。
	 *
	 * ＜支払方法=請求書払い申請の場合のみ＞
	 * ・支払日が本日より後でなければエラー。
	 *
	 * @param denpyouId 伝票ID
	 * @param shiharaiBi 支払日
	 * @param keijouBi 計上日（仮払なら不要、精算なら経費発生日をわたす）
	 * @param keijouBiName 計上日に該当する項目名（エラーメッセージ用）
	 * @param shiharaiHouhou 支払方法(画面上なければnullを渡す→振込扱い)
	 * @param user セッション・ユーザー
	 * @param errorList エラーリスト
	 * @param keijoubiTourokuFlg 0:エラーメッセージとして支払日が先、1:エラーメッセージとして計上日が先
	 *								※実態として、個別伝票から呼び出す場合は0、伝票一覧から支払日一括登録の場合は0、伝票一覧から計上日一括登録の場合は1
	 */
	public void checkShiharaiBi(String denpyouId, Date shiharaiBi, Date keijouBi, String keijouBiName, String shiharaiHouhou, User user, List<String> errorList, String keijoubiTourokuFlg) {

		//最終承認者 or 経理担当者であるか判定
		if (!(isKeiriOrLastShounin(denpyouId,user)))
		{
			return;
		}

		//最終承認者 or 経理担当者であれば支払日のチェックを行う
		if(null == shiharaiBi) {
			errorList.add("支払日が未登録です。支払日を登録してください。");
			return;
		}
		//計上日（があれば）<=支払日でなければエラー
		if(null != keijouBi && shiharaiBi.compareTo(keijouBi) < 0) {
			if(keijoubiTourokuFlg.equals("0")) {
				errorList.add("支払日は" + keijouBiName + "以降の日付を指定してください。");
			}else if(keijoubiTourokuFlg.equals("1")) {
				errorList.add(keijouBiName +"は支払日以前の日付を指定してください。");
			}
			return;
		}
		// 未来日でなければエラー。
		java.util.Date truncDay = DateUtils.truncate(new Date(System.currentTimeMillis()), Calendar.DAY_OF_MONTH);
		if (SHIHARAI_HOUHOU.FURIKOMI.equals(shiharaiHouhou) || DENPYOU_KBN.SEIKYUUSHO_BARAI.equals(shiharaiHouhou)) {
			//振込 / 請求書払い申請の場合のチェック：本日 < 支払日であるべき
			if(shiharaiBi.compareTo(truncDay) <= 0) {
				errorList.add("支払日は本日より後の日付を指定してください。");
				return;
			}
		} else {
			//現金の場合のチェック：本日 <= 支払日であるべき
			if(shiharaiBi.compareTo(truncDay) < 0) {
				errorList.add("支払日は本日以降の日付を指定してください。");
				return;
			}
		}

		// 日付コントロールマスターに登録された個人精算日でなければエラー。
		if (SHIHARAI_HOUHOU.FURIKOMI.equals(shiharaiHouhou)) {
			kojinSeisanShiharaibiCheck(shiharaiBi, "支払日", errorList);
			return;
		}
	}

	/**
	 * 各種伝票画面から相関チェックを呼び出す際、こちらで呼び出し元フラグを設定し渡してあげる。
	 * 各種伝票画面からの呼び出しのため、呼び出し元フラグは"0"となる。
	 *
	 * @param denpyouId 伝票ID
	 * @param shiharaiBi 支払日
	 * @param keijouBi 計上日（仮払なら不要、精算なら経費発生日をわたす）
	 * @param keijouBiName 計上日に該当する項目名（エラーメッセージ用）
	 * @param shiharaiHouhou 支払方法(画面上なければnullを渡す→振込扱い)
	 * @param user セッション・ユーザー
	 * @param errorList エラーリスト
	 */
	public void checkShiharaiBi(String denpyouId, Date shiharaiBi, Date keijouBi, String keijouBiName, String shiharaiHouhou, User user, List<String> errorList) {
		checkShiharaiBi(denpyouId, shiharaiBi, keijouBi, keijouBiName, shiharaiHouhou, user, errorList, "0");
	}

	/**
	 * 引落日のチェックを行う。
	 * 計上日を渡している場合、計上日<=引落日でなければエラー。
	 *
	 * @param hikiotoshiBi 引落日
	 * @param keijouBi 計上日
	 * @param errorList エラーリスト
	 */
	public void checkHikiotoshiBi(Date hikiotoshiBi, Date keijouBi, List<String> errorList) {
		//計上日（があれば）<=支払日でなければエラー
		if(null != keijouBi && hikiotoshiBi.compareTo(keijouBi) < 0) {
			errorList.add("引落日は計上日以降の日付を指定してください。");
			return;
		}
	}


	/**
	 * 引落日のチェックを行う。
	 * 計上日を渡している場合、計上日<=引落日でなければエラー。
	 * ※連絡票対応No.763(自動引落伝票 伝票一覧での計上日一括更新時のメッセージ変更)
	 *   のためのメッセージ文言変更のみで、実施処理はcheckHikiotoshiBiと変化なし
	 *
	 * @param hikiotoshiBi 引落日
	 * @param keijouBi 計上日
	 * @param errorList エラーリスト
	 */
	public void checkHikiotoshiBiForJidouHikiotoshi(Date hikiotoshiBi, Date keijouBi, List<String> errorList) {
		//計上日（があれば）<=支払日でなければエラー
		if(null != keijouBi && hikiotoshiBi.compareTo(keijouBi) < 0) {
			errorList.add("計上日は引落日以前の日付を指定してください。");
			return;
		}
	}

	/**
	 * ユーザーが経理処理権限を持つか判定する
	 * 以下の場合、経理担当者と判定。
	 * ・ユーザーが業務ロールでログインしている
	 * ・かつ、その業務ロールに経理処理権限が付いている（業務ロール_機能制御テーブル）
	 * @param user セッション・ユーザー
	 * @return 経理担当者であればtrue
	 */
	public boolean userIsKeiri(User user) {
		String loginGyoumuRoleId = user.getGyoumuRoleId();
		if(StringUtils.isNotEmpty(loginGyoumuRoleId)) {
			GMap gyoumu = bumonUserLg.selectGyoumuRoleKinouSeigyo(loginGyoumuRoleId, GYOUMU_ROLE_KINOU_SEIGYO_CD.KEIRI_SHORI);
			if(null != gyoumu && GYOUMU_ROLE_KINOU_SEIGYO_KBN.YUUKOU.equals(gyoumu.get("gyoumu_role_kinou_seigyo_kbn"))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 支払日表示/登録可能であるか判定する。
	 * 以下の場合、支払日登録可能と判定。
	 * ・伝票が申請中
	 * ・かつ、ログインユーザーが経理処理権限を持つ。
	 * 以下の場合、支払日表示可能と判定。
	 * ・伝票が申請中または承認済
	 * 上記以外は非表示
	 * @param denpyouId 伝票ID
	 * @param user セッション・ユーザー
	 * @return 0:非表示、1:入力可、2:表示、3:仮払ありに切り替わったときに表示
	 */
	public int judgeShiharaiBiMode(String denpyouId, User user) {
		GMap denpyou = workflowLg.selectDenpyou(denpyouId);

		// 毎回not null指定するなら最初に返した方がシンプル
		if(denpyou == null)
		{
			return 0;
		}

		// 仮払申請系のロジック再編
		var karibaraiList = 
				List.of(EteamNaibuCodeSetting.DENPYOU_KBN.KARIBARAI_SHINSEI,
				EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI,
				EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI);
		
		if(karibaraiList.contains(denpyou.get("denpyou_kbn")))
		{
			TsuuchiCategoryLogic lg = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);
			
			int index = karibaraiList.indexOf(denpyou.get("denpyou_kbn"));
			GMap map = index == 0
					? lg.findKaribaraKeiri((String)denpyou.get("denpyou_id"))
					: index == 1
						? lg.findRyohiKaribaraiKeiri((String)denpyou.get("denpyou_id"))
						: lg.findKaigaiRyohiKaribaraiKeiri((String)denpyou.get("denpyou_id"));
			
			if (map.get("karibarai_on").equals("0"))
			{
				return userIsKeiri(user) ? 3 : 0; // 経理ユーザーなら3、それ以外なら0
			}
		}

		// 三項演算子で残りの条件を再編
		return EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU.equals(denpyou.get("denpyou_joutai")) && userIsKeiri(user)
				? 1
				: EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SYOUNIN_ZUMI.equals(denpyou.get("denpyou_joutai"))
				|| EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU.equals(denpyou.get("denpyou_joutai"))
						? 2
						: 0;
	}

	/**
	 * 該当伝票について、指定ユーザーが経理権限、または最終承認者であるか判別します。
	 * @param denpyouId 伝票ID
	 * @param user セッション・ユーザー
	 * @return boolean 指定ユーザーが経理権限、または最終承認者のどちらかであるか
	 */
	public boolean isKeiriOrLastShounin(String denpyouId, User user) {
		boolean result = false;
		String loginGyoumuRoleId = user.getGyoumuRoleId();

		//最終承認者 or 経理担当者であるか判定(承認ルート最初の場合は除外)
		if(StringUtils.isEmpty(loginGyoumuRoleId)) {
			if(workflowEventLg.isLastShounin(denpyouId, user)) {
				result = true; // 最終承認者
			}
		}else{
			if(userIsKeiri(user)) {
				result = true; // 経理担当者
			}else if(workflowEventLg.isLastShounin(denpyouId, user)) {
				result = true; // 最終承認者
			}
		}
		return result;
	}

	/**
	 * 承認状況に支払日登録の履歴を残す。
	 * @param denpyouId 伝票ID
	 * @param user セッション・ユーザー
	 * @param shiharaiBi 支払日
	 */
	public void insertShiharaiBiTourokuRireki(String denpyouId, User user, String shiharaiBi) {
		workflowEventLg.insertShouninJoukyou(denpyouId, user, workflowLg.findProcRoute(denpyouId, user), "", "支払日登録", shiharaiBi);
	}

	/**
	 * 承認状況に計上日更新の履歴を残す。
	 * @param denpyouId 伝票ID
	 * @param user セッション・ユーザー
	 * @param keijouBi 計上日
	 */
	public void insertKeijouBiKoushinRireki(String denpyouId, User user, String keijouBi) {
		workflowEventLg.insertShouninJoukyou(denpyouId, user, workflowLg.findProcRoute(denpyouId, user), "", "計上日更新", keijouBi);
	}

	/**
	 * 社員口座チェック中継メソッド
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * 伝票単位のチェックはこちらを参照
	 * @param userId ユーザーID
	 * @param shiharaiHouhou 支払方法
	 * @param errorList エラーリスト
	 */
	public void checkShainKouza(String userId, String shiharaiHouhou, List<String> errorList) {
		checkShainKouza(userId, shiharaiHouhou, errorList, "");
	}

	/**
	 * 社員口座チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * 明細単位のチェックはこちらを直参照
	 * @param userId ユーザーID
	 * @param shiharaiHouhou 支払方法
	 * @param errorList エラーリスト
	 * @param prefix 接頭語(〇行目：)
	 */
	public void checkShainKouza(String userId, String shiharaiHouhou, List<String> errorList, String prefix) {
		Boolean isCheck = false;
		if(null == shiharaiHouhou || shiharaiHouhou.equals(EteamNaibuCodeSetting.SHIHARAI_HOUHOU.FURIKOMI)) {
			isCheck = true;
		}

		if (null != userId && isCheck) {
			if (!masterLg.existsShainKouza(userId)) {
				errorList.add(prefix + "ユーザーID[" + userId + "]の社員口座が存在しません。");
			}
		}
	}

	/**
	 * 消費税率チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param kijyunBi 基準日
	 * @param zeiritsu 税率
	 * @param keigenZeiritsuKbn 軽減税率区分
	 * @param errorList エラーリスト
	 */
	public void checkZeiritsu(BigDecimal zeiritsu, String keigenZeiritsuKbn, Date kijyunBi, List<String> errorList) {
		checkZeiritsu(zeiritsu, keigenZeiritsuKbn, kijyunBi, errorList, "");
	}

	/**
	 * 消費税率チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param kazeiKbn 課税区分
	 * @param kijyunBi 基準日
	 * @param zeiritsu 税率
	 * @param keigenZeiritsuKbn 軽減税率区分
	 * @param errorList エラーリスト
	 * @param itemNamePrefix 項目名prefix
	 */
	public void checkZeiritsu(String kazeiKbn, BigDecimal zeiritsu, String keigenZeiritsuKbn, Date kijyunBi, List<String> errorList, String itemNamePrefix) {
		if(kazeiKbnIsZeikomiGroup(kazeiKbn) || kazeiKbnIsZeinukiGroup(kazeiKbn)) {
			//基準日が入力されている場合は「存在チェック」「基準日有効チェック」、されてなければ「存在チェック」のみ
			checkZeiritsu(zeiritsu, keigenZeiritsuKbn, kijyunBi, errorList, itemNamePrefix);
		}else {
			//課税区分グループ「その他」の場合は「存在チェック」のみ固定
			checkZeiritsu(zeiritsu, keigenZeiritsuKbn, null, errorList, itemNamePrefix);
		}
	}

	/**
	 * 消費税率チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param kijyunBi 基準日
	 * @param zeiritsu 税率
	 * @param keigenZeiritsuKbn 軽減税率区分
	 * @param errorList エラーリスト
	 * @param itemNamePrefix 項目名prefix
	 */
	public void checkZeiritsu(BigDecimal zeiritsu, String keigenZeiritsuKbn, Date kijyunBi, List<String> errorList, String itemNamePrefix) {
		String itemName = itemNamePrefix + "消費税率";
		if (null != zeiritsu) {

			//消費税率 存在チェック
			if (!masterLg.existsZeritsu(zeiritsu, keigenZeiritsuKbn)) {
				errorList.add(itemName + "が存在しません。");
			} else {
				if (null != kijyunBi) {

					//基準日有効チェック
					if (!masterLg.existsYuukouZeritsu(zeiritsu, keigenZeiritsuKbn, kijyunBi)) {
						errorList.add(itemName + "は日付[" + new SimpleDateFormat("yyyy/MM/dd").format(kijyunBi) + "]で有効ではありません。");
					}
				}
			}
		}
	}

	//mode=エラー・警告共通
	//  複数要素(エラー・警告あれば)
	//    [errCd]-1(KOUSAI_ERROR_CD_ERROR)　-2(KOUSAI_ERROR_CD_KEIKOKU)
	//    [errMsg]明細用の警告メッセージ　X行目：明細エラーメッセージ
	//    [index]明細番号0～
	//mode=警告のみ
	//  最後の要素(警告あれば　超過・不足それぞれで作成)
	//    [keikokuMsg]"※一人当たりの金額が・・・(1行目、3行目)" ヘッダ部表示用文言
	/**
	 * 交際費として登録された明細において
	 * 一人あたり金額が指定取引の交際費基準を満たしているかチェックし、チェック結果とエラーメッセージを返却します。
	 * @param denpyouKbn 伝票区分
	 * @param shiwakeEdanoCd 仕訳枝番コード(String)
	 * @param kousaihiHitoriKingaku 交際費一人当たり金額(String)
	 * @param kaigaiFlg 海外フラグ(海外旅費精算用)
	 * @param mode チェックモード(-1:エラー -2:警告)
	 * @return エラーまたは警告メッセージリスト(警告モードの場合は上部表示用文字列含む)
	 */
	public List<GMap> checkKousaihiKijun(String denpyouKbn, String[] shiwakeEdanoCd, String[] kousaihiHitoriKingaku, String[] kaigaiFlg, int mode){
		List<GMap> retList = new ArrayList<GMap>();
		if (shiwakeEdanoCd == null || shiwakeEdanoCd.length == 0)
		{
			return retList;
		}
		String overList = "";
		String lessList = "";
		//渡された明細データ分チェック
		for(int i = 0; i < shiwakeEdanoCd.length ; i++) {
			String lineStr = Integer.toString(i+1) + "行目";
			String chkKbn = denpyouKbn;
			if(kaigaiFlg != null) {
				chkKbn = kaigaiFlg[i].equals("1") ? "A901" : denpyouKbn;
			}
			GMap mp = checkKousaihiKijunKobetsu(EteamCommon.toDecimal(kousaihiHitoriKingaku[i]), chkKbn, shiwakeEdanoCd[i]);
			//modeに関わらずチェック結果はretListに格納
			if(mp != null) {
				if(mp.get("errCd").equals(KOUSAI_ERROR_CD_KEIKOKU) && mode == KOUSAI_ERROR_CD_KEIKOKU) {
					//警告：ヘッダ部警告メッセージ用に行番号取得・整形
					if((int)mp.get("chkType") == KOUSAI_CHECK_TYPE_CHOUKA) {
						if(isEmpty(overList)) {
							overList = lineStr;
						}else {
							overList = overList + "、" + lineStr;
						}
					}else {
						if(isEmpty(lessList)) {
							lessList = lineStr;
						}else {
							lessList = lessList + "、" + lineStr;
						}
					}
				}else if(mp.get("errCd").equals(KOUSAI_ERROR_CD_ERROR) && mode == KOUSAI_ERROR_CD_ERROR){
					//エラー：エラーメッセージ用にmpの中身を書き換え
					mp.put("errMsg", lineStr + "：" + mp.get("errMsg"));
				}
				mp.put("index", i);
				retList.add(mp);
			}
		}
		//画面上部(ヘッダ部)表示用警告メッセージ作成 超過・不足それぞれで作成
		if(!isEmpty(overList)) {
			GMap mp = new GMap();
			mp.put("keikokuMsg","※一人当たりの金額が基準額を超過している明細行があります。（" + overList + "）");
			retList.add(mp);
		}
		if(!isEmpty(lessList)) {
			GMap mp = new GMap();
			mp.put("keikokuMsg","※一人当たりの金額が基準額を下回っている明細行があります。（" + lessList + "）");
			retList.add(mp);
		}
		return retList;
	}
	
	/**
	 * @param errorList エラーリスト
	 * @param ks 画面項目制御
	 * @param shoriGroup 処理グループ
	 * @param shouhizeiKbn 消費税区分
	 * @param shiireZeigakuAnbun 仕入税額按分
	 * @param kazeiKbn 課税区分
	 * @param bunriKbn 分離区分
	 * @param shiireKbn 仕入区分
	 * @param i 明細行（配列番号。本体側の場合は負の値を設定すること）
	 */
	public void checkKbnConsistency(List<String> errorList, GamenKoumokuSeigyo ks, String shoriGroup, int shouhizeiKbn, int shiireZeigakuAnbun, String kazeiKbn, String bunriKbn, String shiireKbn, int i) {
		String meisaiRowHeader = i < 0 ? "" : ("：" + (i + 1) + "行目");
	    // 1. kazeiKbnの整合性確認
	    if (!isValidKazeiKbn(shoriGroup, kazeiKbn, shouhizeiKbn)) {
	    	errorList.add(ks.getKazeiKbn().getName() + meisaiRowHeader + "「" + kazeiKbn + "」は処理グループ「" + shoriGroup + "」で使用不可です。");
	    }

	    // 2. bunriKbnの整合性確認
	    if (!isValidBunriKbn(shoriGroup, kazeiKbn, bunriKbn, shouhizeiKbn)) {
	    	errorList.add(ks.getBunriKbn().getName() + meisaiRowHeader + "「" + bunriKbn + "」は処理グループ「" + shoriGroup + "」・課税区分「" + kazeiKbn + "」で使用不可です。");
	    }

	    // 3. shiireKbnの整合性確認
	    if (!isValidShiireKbn(shoriGroup, kazeiKbn, shiireKbn, shiireZeigakuAnbun)) {
	    	errorList.add(ks.getShiireKbn().getName() + meisaiRowHeader + "「" + shiireKbn + "」は処理グループ「" + shoriGroup + "」・課税区分「" + kazeiKbn + "」で使用不可です。");
	    }
	}
	
	/**
	 * @param shoriGroup 処理グループ
	 * @param kazeiKbn 課税区分
	 * @param shouhizeiKbn 消費税区分
	 * @return 有効か否か
	 */
	private boolean isValidKazeiKbn(String shoriGroup, String kazeiKbn, int shouhizeiKbn) {
	    List<String> validKazeiKbnOptions = new ArrayList<>();
	    
	    if (Arrays.asList("3", "4", "5", "6", "9", "10").contains(shoriGroup)) {
	        validKazeiKbnOptions.addAll(Arrays.asList("000", "001", "003", "004"));
	        if(Arrays.asList(1, 2).contains(shouhizeiKbn)) {
	            validKazeiKbnOptions.add("002");
	        }
	    } else if (Arrays.asList("2", "7", "8").contains(shoriGroup)) {
	        validKazeiKbnOptions.addAll(Arrays.asList("000", "003", "011", "012", "041", "042"));
	        if(Arrays.asList(1, 2).contains(shouhizeiKbn)) {
	            validKazeiKbnOptions.addAll(Arrays.asList("013", "014"));
	        }
	        if (shoriGroup.equals("7")) {
	            validKazeiKbnOptions.add("004");
	        }
	    } else {
	        validKazeiKbnOptions.add("100");
	    }
	    
	    return validKazeiKbnOptions.contains(kazeiKbn);
	}

	/**
	 * @param shoriGroup 処理グループ
	 * @param kazeiKbn 課税区分
	 * @param bunriKbn 分離区分
	 * @param shouhizeiKbn 消費税区分
	 * @return 有効か否か
	 */
	public boolean isValidBunriKbn(String shoriGroup, String kazeiKbn, String bunriKbn, int shouhizeiKbn) {
	    if (Arrays.asList(1, 2).contains(shouhizeiKbn)) {
	        if (Arrays.asList("002", "013", "014").contains(kazeiKbn)) {
	            return bunriKbn.equals("0");
	        } else if(Arrays.asList("001", "011", "012").contains(kazeiKbn)) {
	        	if(shouhizeiKbn == 1) {
		            return bunriKbn.equals("1");
	        	}
	            return Arrays.asList("0", "1", "2").contains(bunriKbn);
	        }
	    }
	    return bunriKbn.equals("9");
	}
	
	/**
	 * @param shoriGroup 処理グループ
	 * @param kazeiKbn 課税区分
	 * @param shiireKbn 仕入区分
	 * @param shiireZeigakuAnbun 仕入税額按分
	 * @return 有効か否か
	 */
	public boolean isValidShiireKbn(String shoriGroup, String kazeiKbn, String shiireKbn, int shiireZeigakuAnbun) {
	    if (shiireZeigakuAnbun == 1 && Arrays.asList("2", "5", "6", "7", "8", "10").contains(shoriGroup) && Arrays.asList("001", "002", "011", "013").contains(kazeiKbn)) {
	        return Arrays.asList("1", "2", "3").contains(shiireKbn);
	    }
	    return shiireKbn.equals("0");
	}

	/**
	 * checkKousaihiKijunの個別チェック処理
	 * ※checkKousaihiKijunのモードによらず、各明細のチェック結果は格納する
	 * @param kousaihiHitoriKingaku 交際費一人あたり金額
	 * @param denpyouKbn 伝票区分
	 * @param shiwakeEdanoCd 仕訳枝番コード
	 * @return 結果GMap {(errCd -1:エラー -2:警告 -999:その他システムエラー),(errMsg エラーメッセージ),(chkType 1:超過 2:不足)}
	 */
	protected GMap checkKousaihiKijunKobetsu(BigDecimal kousaihiHitoriKingaku, String denpyouKbn, String shiwakeEdanoCd) {
		GMap mp = new GMap();
		GMap shiwakePattern = kaikeiLg.findShiwakePattern(denpyouKbn, Integer.parseInt(shiwakeEdanoCd));
		if(shiwakePattern == null) {
			mp.put("errCd", KOUSAI_ERROR_CD_SYSERROR);
			mp.put("errMsg", "伝票種別[" + denpyouKbn + "]の仕訳パターン[" + shiwakeEdanoCd + "]が見つかりません。");
			return mp;
		}
		//交際費表示対象かつチェック対象ならチェック実施
		if( "1".equals(shiwakePattern.get("kousaihi_hyouji_flg")) && !("0".equals(shiwakePattern.get("kousaihi_check_houhou"))) ) {
			BigDecimal kijunGaku = shiwakePattern.get("kousaihi_kijun_gaku");
			if (kousaihiHitoriKingaku == null)
			{
				return null;
			} //途中で設定を変えると、トランザクションデータには交際費一人あたり金額がないけど、取引は交際費対象になりえる・・・警告は出しようがないので出さない。未申請データであれば申請時に一人あたり金額の未入力エラーになるはず。
			if("1".equals(shiwakePattern.get("kousaihi_check_houhou"))) {
				//1.基準値超過かチェック
				if(kousaihiHitoriKingaku.compareTo(kijunGaku) > 0 ) {
					mp.put("chkType", KOUSAI_CHECK_TYPE_CHOUKA);
					mp.put("errMsg", "※一人当たりの金額が基準額" + kijunGaku.toString() + "円を超過しています。");
					if("1".equals(shiwakePattern.get("kousaihi_check_result"))){
						mp.put("errCd", KOUSAI_ERROR_CD_KEIKOKU); //警告
					}else {
						mp.put("errCd", KOUSAI_ERROR_CD_ERROR); //エラー
						mp.put("errMsg", ((String)mp.get("errMsg")).replace("※",""));
					}
				}
			}else{
				//2.基準値以下かチェック
				if(kousaihiHitoriKingaku.compareTo(kijunGaku) <= 0 ) {
					mp.put("chkType", KOUSAI_CHECK_TYPE_FUSOKU);
					mp.put("errMsg", "※一人当たりの金額が基準額" + kijunGaku.toString() + "円を下回っています。");
					if("1".equals(shiwakePattern.get("kousaihi_check_result"))){
						mp.put("errCd", KOUSAI_ERROR_CD_KEIKOKU); //警告
					}else {
						mp.put("errCd", KOUSAI_ERROR_CD_ERROR); //エラー
						mp.put("errMsg", ((String)mp.get("errMsg")).replace("※",""));
					}
				}
			}
		}
		return mp.isEmpty() ? null : mp;
	}

	/**
	 * 仕訳チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * 画面上存在する項目のみ渡すこと。
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param errorList エラーリスト
	 */
	public void checkShiwake(
			ShiwakeCheckData shiwakeCheckData,
			List<String> errorList) {
		checkShiwake(null, null, shiwakeCheckData, null, errorList);
	}
	
	/**
	 * 仕訳チェック(拡張用)
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * 画面上存在する項目のみ渡すこと。
	 * @param denpyouKbn 伝票区分
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param errorList エラーリスト
	 */
	public void checkShiwake(
			String denpyouKbn,
			ShiwakeCheckData shiwakeCheckData,
			List<String> errorList) {
		checkShiwake(denpyouKbn, null, shiwakeCheckData, null, errorList);
	}

	/**
	 * 仕訳チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * 画面上存在する項目のみ渡すこと。
	 * @param denpyouKbn 伝票区分
	 * @param shiwakePatternSettingKbn 仕訳パターン設定区分
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param daihyouBumonCd 代表負担部門コード
	 * @param errorList エラーリスト
	 */
	public void checkShiwake(
			String denpyouKbn,
			String shiwakePatternSettingKbn,
			ShiwakeCheckData shiwakeCheckData,
			String daihyouBumonCd,
			List<String> errorList) {
		if(Env.checkShiwakeIsDummy())return;

		//拡張 前処理
		String kamokuCd = shiwakeCheckData.getKamokuCd();
		beforeCheckShiwakeExt(shiwakeCheckData);

		//科目枝番連携フラグの再設定(各種申請画面から呼び出された時向け)
		if( shiwakeCheckData.zaimuEdabanRenkei == null && !isEmpty(denpyouKbn) && shiwakeCheckData.shiwakeEdaNo != null ) {
			String shiwakeEdanoCd = shiwakeCheckData.shiwakeEdaNo;
			GMap shiwakePattern = kaikeiLg.findShiwakePattern(denpyouKbn, Integer.parseInt(shiwakeEdanoCd));
			if(shiwakePattern != null && "1".equals(shiwakePattern.get("edaban_renkei_flg"))) {
				shiwakeCheckData.zaimuEdabanRenkei = "1";
			}
		}

		//勘定科目入力チェック
		this.checkKanjouKamoku(shiwakeCheckData, errorList);

		//負担部門入力チェック
		this.checkFutanBumon(shiwakeCheckData, errorList);

		//取引先入力チェック
		this.checkTorihikisaki(shiwakeCheckData, errorList);

		//プロジェクトチェック
		this.checkPJ(shiwakeCheckData, errorList);

		//セグメントチェック
		this.checkSegment(shiwakeCheckData, errorList);

		//課税区分入力チェック
		this.checkKazeiKbn(shiwakeCheckData, denpyouKbn, errorList);

		//HF1-10入力チェック
		this.checkHF("1", shiwakeCheckData.hf1Cd, shiwakeCheckData.hf1Nm, ColumnName.HF1_NAME, ColumnName.HF1_HISSU_FLG, ColumnName.HF1_SHIYOU_FLG, errorList);
		this.checkHF("2", shiwakeCheckData.hf2Cd, shiwakeCheckData.hf2Nm, ColumnName.HF2_NAME, ColumnName.HF2_HISSU_FLG, ColumnName.HF2_SHIYOU_FLG, errorList);
		this.checkHF("3", shiwakeCheckData.hf3Cd, shiwakeCheckData.hf3Nm, ColumnName.HF3_NAME, ColumnName.HF3_HISSU_FLG, ColumnName.HF3_SHIYOU_FLG, errorList);
		this.checkHF("4", shiwakeCheckData.hf4Cd, shiwakeCheckData.hf4Nm, ColumnName.HF4_NAME, ColumnName.HF4_HISSU_FLG, ColumnName.HF4_SHIYOU_FLG, errorList);
		this.checkHF("5", shiwakeCheckData.hf5Cd, shiwakeCheckData.hf5Nm, ColumnName.HF5_NAME, ColumnName.HF5_HISSU_FLG, ColumnName.HF5_SHIYOU_FLG, errorList);
		this.checkHF("6", shiwakeCheckData.hf6Cd, shiwakeCheckData.hf6Nm, ColumnName.HF6_NAME, ColumnName.HF6_HISSU_FLG, ColumnName.HF6_SHIYOU_FLG, errorList);
		this.checkHF("7", shiwakeCheckData.hf7Cd, shiwakeCheckData.hf7Nm, ColumnName.HF7_NAME, ColumnName.HF7_HISSU_FLG, ColumnName.HF7_SHIYOU_FLG, errorList);
		this.checkHF("8", shiwakeCheckData.hf8Cd, shiwakeCheckData.hf8Nm, ColumnName.HF8_NAME, ColumnName.HF8_HISSU_FLG, ColumnName.HF8_SHIYOU_FLG, errorList);
		this.checkHF("9", shiwakeCheckData.hf9Cd, shiwakeCheckData.hf9Nm, ColumnName.HF9_NAME, ColumnName.HF9_HISSU_FLG, ColumnName.HF9_SHIYOU_FLG, errorList);
		this.checkHF("10", shiwakeCheckData.hf10Cd, shiwakeCheckData.hf10Nm, ColumnName.HF10_NAME, ColumnName.HF10_HISSU_FLG, ColumnName.HF10_SHIYOU_FLG, errorList);

		//UF1-10入力チェック
		this.checkUF("1", shiwakeCheckData.uf1Cd, shiwakeCheckData.uf1Nm, ColumnName.UF1_SHIYOU_FLG, errorList);
		this.checkUF("2", shiwakeCheckData.uf2Cd, shiwakeCheckData.uf2Nm, ColumnName.UF2_SHIYOU_FLG, errorList);
		this.checkUF("3", shiwakeCheckData.uf3Cd, shiwakeCheckData.uf3Nm, ColumnName.UF3_SHIYOU_FLG, errorList);
		this.checkUF("4", shiwakeCheckData.uf4Cd, shiwakeCheckData.uf4Nm, ColumnName.UF4_SHIYOU_FLG, errorList);
		this.checkUF("5", shiwakeCheckData.uf5Cd, shiwakeCheckData.uf5Nm, ColumnName.UF5_SHIYOU_FLG, errorList);
		this.checkUF("6", shiwakeCheckData.uf6Cd, shiwakeCheckData.uf6Nm, ColumnName.UF6_SHIYOU_FLG, errorList);
		this.checkUF("7", shiwakeCheckData.uf7Cd, shiwakeCheckData.uf7Nm, ColumnName.UF7_SHIYOU_FLG, errorList);
		this.checkUF("8", shiwakeCheckData.uf8Cd, shiwakeCheckData.uf8Nm, ColumnName.UF8_SHIYOU_FLG, errorList);
		this.checkUF("9", shiwakeCheckData.uf9Cd, shiwakeCheckData.uf9Nm, ColumnName.UF9_SHIYOU_FLG, errorList);
		this.checkUF("10", shiwakeCheckData.uf10Cd, shiwakeCheckData.uf10Nm, ColumnName.UF10_SHIYOU_FLG, errorList);

		//UF固定1-10入力チェック
		this.checkUFKotei("1", shiwakeCheckData.ufKotei1Cd, shiwakeCheckData.ufKotei1Nm, ColumnName.UF_KOTEI1_SHIYOU_FLG, errorList);
		this.checkUFKotei("2", shiwakeCheckData.ufKotei2Cd, shiwakeCheckData.ufKotei2Nm, ColumnName.UF_KOTEI2_SHIYOU_FLG, errorList);
		this.checkUFKotei("3", shiwakeCheckData.ufKotei3Cd, shiwakeCheckData.ufKotei3Nm, ColumnName.UF_KOTEI3_SHIYOU_FLG, errorList);
		this.checkUFKotei("4", shiwakeCheckData.ufKotei4Cd, shiwakeCheckData.ufKotei4Nm, ColumnName.UF_KOTEI4_SHIYOU_FLG, errorList);
		this.checkUFKotei("5", shiwakeCheckData.ufKotei5Cd, shiwakeCheckData.ufKotei5Nm, ColumnName.UF_KOTEI5_SHIYOU_FLG, errorList);
		this.checkUFKotei("6", shiwakeCheckData.ufKotei6Cd, shiwakeCheckData.ufKotei6Nm, ColumnName.UF_KOTEI6_SHIYOU_FLG, errorList);
		this.checkUFKotei("7", shiwakeCheckData.ufKotei7Cd, shiwakeCheckData.ufKotei7Nm, ColumnName.UF_KOTEI7_SHIYOU_FLG, errorList);
		this.checkUFKotei("8", shiwakeCheckData.ufKotei8Cd, shiwakeCheckData.ufKotei8Nm, ColumnName.UF_KOTEI8_SHIYOU_FLG, errorList);
		this.checkUFKotei("9", shiwakeCheckData.ufKotei9Cd, shiwakeCheckData.ufKotei9Nm, ColumnName.UF_KOTEI9_SHIYOU_FLG, errorList);
		this.checkUFKotei("10", shiwakeCheckData.ufKotei10Cd, shiwakeCheckData.ufKotei10Nm, ColumnName.UF_KOTEI10_SHIYOU_FLG, errorList);

		//拡張 前処理の戻し
		shiwakeCheckData.kamokuCd = kamokuCd;

		//仕訳パターン入力チェック
		this.checkShiwakePattern(denpyouKbn, shiwakePatternSettingKbn, daihyouBumonCd, shiwakeCheckData, errorList);
	}

	/**
	 * 拡張用メソッド checkShiwake前処理
	 * @param shiwakeCheckData 仕訳
	 */
	protected void beforeCheckShiwakeExt(ShiwakeCheckData shiwakeCheckData) {}

	/**
	 * マップ情報から振込先を作成して返却します。
	 * @param torihikisakiCd 取引先コード
	 * @return 振込先を表す文字列
	 */
	public String furikomisakiSakusei(String torihikisakiCd) {
		if (StringUtils.isEmpty(torihikisakiCd))
		{
			return "";
		}

		MasterKanriCategoryLogic masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		GMap map = masterKanriLogic.findTorihikisakiJouhou(torihikisakiCd, false);
		if(map == null) {
			return "";
		}

		// フォーマット変換
		StringBuilder furikomisaki = new StringBuilder();
		furikomisaki.append("金融機関:").append(n2b(map.get("kinyuukikan_name_kana"))).append("　");
		furikomisaki.append("支店:").append(n2b(map.get("shiten_name_kana"))).append("　");
		furikomisaki.append("預金種別:").append(mojiretsuHenkan("yokin_shubetsu", map.get("yokin_shubetsu"))).append("　");
		furikomisaki.append("口座番号:").append(n2b(map.get("kouza_bangou"))).append("　");
		furikomisaki.append("口座名義人:").append(n2b(map.get("kouza_meiginin")));
		return furikomisaki.toString();
	}
	//※※※同期とること↑↓※※※
	/**
	 * マップ情報から振込先を作成して返却します。
	 * @param map 取引先
	 * @return 振込先を表す文字列
	 */
	public String furikomisakiSakusei(GMap map) {
		// フォーマット変換
		StringBuilder furikomisaki = new StringBuilder();
		furikomisaki.append("金融機関:").append(n2b(map.get("kinyuukikan_name_kana"))).append("　");
		furikomisaki.append("支店:").append(n2b(map.get("shiten_name_kana"))).append("　");
		furikomisaki.append("預金種別:").append(n2b(map.get("yokin_shubetsu_name"))).append("　");
		furikomisaki.append("口座番号:").append(n2b(map.get("kouza_bangou"))).append("　");
		furikomisaki.append("口座名義人:").append(n2b(map.get("kouza_meiginin")));
		return furikomisaki.toString();
	}

	/**
	 * 支払方法のプルダウンリストを作成して返却します。
	 * @param shiharaiHouhouShubetsu 支払方法（伝票種別）※設定情報より
	 * @param houhou                 選択済みの支払方法
	 * @return 支払方法リスト
	 */
	public List<GMap> makeShiharaiHouhouList(String shiharaiHouhouShubetsu, String houhou) {
		// 設定情報から伝票種別ごとの支払方法を取得する
		String shiharaiHouhouSetting = EteamSettingInfo.getSettingInfo(shiharaiHouhouShubetsu);
		String[] shiharaiHouhouArray = shiharaiHouhouSetting.split(",");
		List<GMap> shiharaihouhouListTmp = systemLg.loadNaibuCdSetting("shiharai_houhou");

		List<GMap> shiharaihouhouList = new ArrayList<GMap>();
		// 設定情報の支払方法の登録順（1,2）or（2,1）を基準にリストを作成
		for(String shiharaiHouhou : shiharaiHouhouArray) {
			for(GMap mapShiharaiHouhou : shiharaihouhouListTmp) {
				String naibu_cd = mapShiharaiHouhou.get("naibu_cd").toString();
				if (naibu_cd.equals(shiharaiHouhou)) {
					shiharaihouhouList.add(mapShiharaiHouhou);
					break;
				}
			}
		}

		// 伝票で選択した支払方法が会社設定で設定した支払方法にない場合、選択済の支払方法もリストにいれる
		if(!StringUtils.isEmpty(houhou)) {
			if(!Arrays.asList(shiharaiHouhouArray).contains(houhou)){
				for(GMap map : shiharaihouhouListTmp) {
					if(houhou.equals(map.get("naibu_cd").toString())) {
						shiharaihouhouList.add(map);
						break;
					}
				}
	        }
		}

		return shiharaihouhouList;
	}

	/**
	 * 内部コード設定から名前を取得します。
	 * @param naibuCdName 内部コード名
	 * @param naibuCd     内部コード
	 * @return 名前
	 */
	protected String mojiretsuHenkan(String naibuCdName, Object naibuCd) {
		if (null == naibuCd)
		{
			return "";
		}
		String cdStr = String.valueOf(naibuCd);
		if (StringUtils.isEmpty(cdStr))
		{
			return "";
		}
		GMap map = systemLg.findNaibuCdSetting(naibuCdName, cdStr);
		if (null == map)
		{
			return "";
		}
		return map.get("name").toString();
	}

	/**
	 * 科目-各項目未入力チェック
	 * 科目マスターの未入力チェックを参照し、枝番、部門、取引先等の入力が必須かチェックする。
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param shiwakeCheckData  仕訳チェックデータ
	 * @param errorList エラーリスト
	 */
	protected void checkKamokuMinyuuryoku(ShiwakeCheckData shiwakeCheckData, List<String> errorList) {

		if(StringUtils.isEmpty(shiwakeCheckData.kamokuCd))
		{
			return;
		}
		// 科目マスターよりレコードを取得
		GMap kamokuJouhou = masterLg.findKamokuMaster(shiwakeCheckData.kamokuCd);

		// 各未入力フラグを取得
		Boolean edaNoCheck = 1 == (Integer)kamokuJouhou.get("edaban_minyuuryoku_check"); // 枝番未入力チェック
		Boolean torihikisakiCheck = 1 == (Integer)kamokuJouhou.get("torihikisaki_minyuuryoku_check"); // 取引先未入力チェック
		Boolean bumonCheck = 1 == (Integer)kamokuJouhou.get("bumon_minyuuryoku_check"); // 部門未入力チェック
		Boolean uf1Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF1_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("uf1_minyuuryoku_check"));
		Boolean uf2Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF2_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("uf2_minyuuryoku_check"));
		Boolean uf3Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF3_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("uf3_minyuuryoku_check"));
		Boolean uf4Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF4_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("uf4_minyuuryoku_check"));
		Boolean uf5Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF5_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("uf5_minyuuryoku_check"));
		Boolean uf6Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF6_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("uf6_minyuuryoku_check"));
		Boolean uf7Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF7_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("uf7_minyuuryoku_check"));
		Boolean uf8Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF8_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("uf8_minyuuryoku_check"));
		Boolean uf9Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF9_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("uf9_minyuuryoku_check"));
		Boolean uf10Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF10_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("uf10_minyuuryoku_check"));
		Boolean ufKotei1Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI1_SHIYOU_FLG)))
				&&(1 == (Integer)kamokuJouhou.get("uf_kotei1_minyuuryoku_check"));
		Boolean ufKotei2Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI2_SHIYOU_FLG)))
				&&(1 == (Integer)kamokuJouhou.get("uf_kotei2_minyuuryoku_check"));
		Boolean ufKotei3Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI3_SHIYOU_FLG)))
				&&(1 == (Integer)kamokuJouhou.get("uf_kotei3_minyuuryoku_check"));
		Boolean ufKotei4Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI4_SHIYOU_FLG)))
				&&(1 == (Integer)kamokuJouhou.get("uf_kotei4_minyuuryoku_check"));
		Boolean ufKotei5Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI5_SHIYOU_FLG)))
				&&(1 == (Integer)kamokuJouhou.get("uf_kotei5_minyuuryoku_check"));
		Boolean ufKotei6Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI6_SHIYOU_FLG)))
				&&(1 == (Integer)kamokuJouhou.get("uf_kotei6_minyuuryoku_check"));
		Boolean ufKotei7Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI7_SHIYOU_FLG)))
				&&(1 == (Integer)kamokuJouhou.get("uf_kotei7_minyuuryoku_check"));
		Boolean ufKotei8Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI8_SHIYOU_FLG)))
				&&(1 == (Integer)kamokuJouhou.get("uf_kotei8_minyuuryoku_check"));
		Boolean ufKotei9Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI9_SHIYOU_FLG)))
				&&(1 == (Integer)kamokuJouhou.get("uf_kotei9_minyuuryoku_check"));
		Boolean ufKotei10Check = (! SHIYOU_FLG.SHIYOU_SHINAI.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI10_SHIYOU_FLG)))
				&&(1 == (Integer)kamokuJouhou.get("uf_kotei10_minyuuryoku_check"));
		Boolean pjCheck =  (! "0".equals(KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("project_minyuuryoku_check"));
		Boolean segmentCheck =  (! "0".equals(KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG)))
									&&(1 == (Integer)kamokuJouhou.get("segment_minyuuryoku_check"));

		// エラーメッセージ
		String formatMsgHissu = shiwakeCheckData.kamokuNm + "[" + shiwakeCheckData.kamokuCd + "]は%sの入力が必須です。";

		// 枝番チェック(科目枝番連携フラグ1(ON)の場合はスキップ)
		if (edaNoCheck && "".equals(shiwakeCheckData.kamokuEdabanCd) && !("1".equals(shiwakeCheckData.zaimuEdabanRenkei)) ) {
			errorList.add(String.format(formatMsgHissu, shiwakeCheckData.kamokuEdabanNm));
		}
		// 取引先チェック
		if(torihikisakiCheck && "".equals(shiwakeCheckData.torihikisakiCd)) {
			errorList.add(String.format(formatMsgHissu, shiwakeCheckData.torihikisakiNm));
		}
		// 部門チェック
		if(bumonCheck && "".equals(shiwakeCheckData.futanBumonCd)) {
			errorList.add(String.format(formatMsgHissu, shiwakeCheckData.futanBumonNm));
		}

		// ユニバーサルフィールド1-10チェック
		if(uf1Check && "".equals(shiwakeCheckData.uf1Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf1Nm));
		if(uf2Check && "".equals(shiwakeCheckData.uf2Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf2Nm));
		if(uf3Check && "".equals(shiwakeCheckData.uf3Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf3Nm));
		if(uf4Check && "".equals(shiwakeCheckData.uf4Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf4Nm));
		if(uf5Check && "".equals(shiwakeCheckData.uf5Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf5Nm));
		if(uf6Check && "".equals(shiwakeCheckData.uf6Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf6Nm));
		if(uf7Check && "".equals(shiwakeCheckData.uf7Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf7Nm));
		if(uf8Check && "".equals(shiwakeCheckData.uf8Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf8Nm));
		if(uf9Check && "".equals(shiwakeCheckData.uf9Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf9Nm));
		if(uf10Check && "".equals(shiwakeCheckData.uf10Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf10Nm));

		// ユニバーサルフィールド(固定値)1-10チェック
		if(ufKotei1Check && "".equals(shiwakeCheckData.ufKotei1Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.ufKotei1Nm));
		if(ufKotei2Check && "".equals(shiwakeCheckData.ufKotei2Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.ufKotei2Nm));
		if(ufKotei3Check && "".equals(shiwakeCheckData.ufKotei3Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.ufKotei3Nm));
		if(ufKotei4Check && "".equals(shiwakeCheckData.ufKotei4Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.ufKotei4Nm));
		if(ufKotei5Check && "".equals(shiwakeCheckData.ufKotei5Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.ufKotei5Nm));
		if(ufKotei6Check && "".equals(shiwakeCheckData.ufKotei6Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.ufKotei6Nm));
		if(ufKotei7Check && "".equals(shiwakeCheckData.ufKotei7Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.ufKotei7Nm));
		if(ufKotei8Check && "".equals(shiwakeCheckData.ufKotei8Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.ufKotei8Nm));
		if(ufKotei9Check && "".equals(shiwakeCheckData.ufKotei9Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.ufKotei9Nm));
		if(ufKotei10Check && "".equals(shiwakeCheckData.ufKotei10Cd)) errorList.add(String.format(formatMsgHissu, shiwakeCheckData.ufKotei10Nm));

		// プロジェクトコードチェック
		if(pjCheck && "".equals(shiwakeCheckData.projectCd)) {
			errorList.add(String.format(formatMsgHissu, shiwakeCheckData.projectNm));
		}
		// セグメントコードチェック
		if(segmentCheck && "".equals(shiwakeCheckData.segmentCd)) {
			errorList.add(String.format(formatMsgHissu, shiwakeCheckData.segmentNm));
		}
	}

	//--------------------
	//protectedメソッド
	//--------------------

	/**
	 * 勘定科目入力チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param errorList エラーリスト
	 */
	protected void checkKanjouKamoku(ShiwakeCheckData shiwakeCheckData, List<String> errorList) {

		if(null == shiwakeCheckData.kamokuCd)
		{
			return;
		}

		// UF使用フラグ
		boolean kamokuUf1RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF1_SHIYOU_FLG));
		boolean kamokuUf2RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF2_SHIYOU_FLG));
		boolean kamokuUf3RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF3_SHIYOU_FLG));
		boolean kamokuUf4RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF4_SHIYOU_FLG));
		boolean kamokuUf5RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF5_SHIYOU_FLG));
		boolean kamokuUf6RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF6_SHIYOU_FLG));
		boolean kamokuUf7RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF7_SHIYOU_FLG));
		boolean kamokuUf8RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF8_SHIYOU_FLG));
		boolean kamokuUf9RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF9_SHIYOU_FLG));
		boolean kamokuUf10RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF10_SHIYOU_FLG));

		// UF(固定)使用フラグ
		boolean kamokuUfKotei1RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI1_SHIYOU_FLG));
		boolean kamokuUfKotei2RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI2_SHIYOU_FLG));
		boolean kamokuUfKotei3RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI3_SHIYOU_FLG));
		boolean kamokuUfKotei4RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI4_SHIYOU_FLG));
		boolean kamokuUfKotei5RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI5_SHIYOU_FLG));
		boolean kamokuUfKotei6RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI6_SHIYOU_FLG));
		boolean kamokuUfKotei7RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI7_SHIYOU_FLG));
		boolean kamokuUfKotei8RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI8_SHIYOU_FLG));
		boolean kamokuUfKotei9RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI9_SHIYOU_FLG));
		boolean kamokuUfKotei10RelationCheckFlg = SHIYOU_FLG.ZANDAKA.equals(KaishaInfo.getKaishaInfo(ColumnName.UF_KOTEI10_SHIYOU_FLG));

		if (StringUtils.isNotEmpty(shiwakeCheckData.kamokuCd)) {

			String kamokuNm = shiwakeCheckData.kamokuNm;
			String kamokuCd = shiwakeCheckData.kamokuCd;

			//勘定科目 存在チェック
			if (null == masterLg.findKamokuName(kamokuCd)) {
				errorList.add(kamokuNm + "[" + kamokuCd + "]は存在しません。");
			} else {
				// 科目-各項目未入力チェック
				this.checkKamokuMinyuuryoku(shiwakeCheckData, errorList);

				// 勘定科目紐付チェック
				String formatMsg = kamokuNm + "[" + kamokuCd + "]と%s[%s]が紐づきません。";

				// 勘定科目-取引先の紐付チェック
				if (null != shiwakeCheckData.torihikisakiCd) {
					String checkNm = shiwakeCheckData.torihikisakiNm;
					String checkCd = shiwakeCheckData.torihikisakiCd;
					if(StringUtils.isNotEmpty(checkCd)) {
						if (!masterLg.existsTorihikisakiKamokuZandaka(checkCd, kamokuCd)) {
							errorList.add(String.format(formatMsg,checkNm,checkCd));
						}
					}
				}

				// 勘定科目-勘定科目枝番の紐付チェック
				if (null != shiwakeCheckData.kamokuEdabanCd) {
					String checkNm = shiwakeCheckData.kamokuEdabanNm;
					String checkCd = shiwakeCheckData.kamokuEdabanCd;
					if(StringUtils.isNotEmpty(checkCd)) {
						if (!masterLg.existsKamokuEdabanZandaka(kamokuCd, checkCd)) {
							errorList.add(String.format(formatMsg,checkNm,checkCd));
						}
					}
				}

				// 勘定科目-負担部門の紐付チェック
				if (null != shiwakeCheckData.futanBumonCd) {
					String checkNm = shiwakeCheckData.futanBumonNm;
					String checkCd = shiwakeCheckData.futanBumonCd;
					if(StringUtils.isNotEmpty(checkCd)) {
						if (!masterLg.existsFutanBumonKamokuZandaka(checkCd, kamokuCd)) {
							errorList.add(String.format(formatMsg,checkNm,checkCd));
						}
					}
				}

				// 勘定科目-セグメントの紐付チェック
				if (null != shiwakeCheckData.segmentCd) {
					String checkNm = shiwakeCheckData.segmentNm;
					String checkCd = shiwakeCheckData.segmentCd;
					if(StringUtils.isNotEmpty(checkCd)) {
						if (!masterLg.existsSegmentKamokuZandaka(checkCd, kamokuCd)) {
							errorList.add(String.format(formatMsg,checkNm,checkCd));
						}
					}
				}

				// 勘定科目-UF1-10の紐付チェック
				if (kamokuUf1RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf1Cd)
						&& !masterLg.existsUFZandaka("1", shiwakeCheckData.uf1Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf1Nm, shiwakeCheckData.uf1Cd));
				}
				if (kamokuUf2RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf2Cd)
						&& !masterLg.existsUFZandaka("2", shiwakeCheckData.uf2Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf2Nm, shiwakeCheckData.uf2Cd));
				}
				if (kamokuUf3RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf3Cd)
						&& !masterLg.existsUFZandaka("3", shiwakeCheckData.uf3Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf3Nm, shiwakeCheckData.uf3Cd));
				}
				if (kamokuUf4RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf4Cd)
						&& !masterLg.existsUFZandaka("4", shiwakeCheckData.uf4Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf4Nm, shiwakeCheckData.uf4Cd));
				}
				if (kamokuUf5RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf5Cd)
						&& !masterLg.existsUFZandaka("5", shiwakeCheckData.uf5Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf5Nm, shiwakeCheckData.uf5Cd));
				}
				if (kamokuUf6RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf6Cd)
						&& !masterLg.existsUFZandaka("6", shiwakeCheckData.uf6Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf6Nm, shiwakeCheckData.uf6Cd));
				}
				if (kamokuUf7RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf7Cd)
						&& !masterLg.existsUFZandaka("7", shiwakeCheckData.uf7Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf7Nm, shiwakeCheckData.uf7Cd));
				}
				if (kamokuUf8RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf8Cd)
						&& !masterLg.existsUFZandaka("8", shiwakeCheckData.uf8Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf8Nm, shiwakeCheckData.uf8Cd));
				}
				if (kamokuUf9RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf9Cd)
						&& !masterLg.existsUFZandaka("9", shiwakeCheckData.uf9Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf9Nm, shiwakeCheckData.uf9Cd));
				}
				if (kamokuUf10RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.uf10Cd)
						&& !masterLg.existsUFZandaka("10", shiwakeCheckData.uf10Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.uf10Nm, shiwakeCheckData.uf10Cd));
				}

				// 勘定科目-UF(固定値)1-10の紐付チェック
				if (kamokuUfKotei1RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei1Cd)
						&& !masterLg.existsUFKoteiZandaka("1", shiwakeCheckData.ufKotei1Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei1Nm, shiwakeCheckData.ufKotei1Cd));
				}
				if (kamokuUfKotei2RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei2Cd)
						&& !masterLg.existsUFKoteiZandaka("2", shiwakeCheckData.ufKotei2Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei2Nm, shiwakeCheckData.ufKotei2Cd));
				}
				if (kamokuUfKotei3RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei3Cd)
						&& !masterLg.existsUFKoteiZandaka("3", shiwakeCheckData.ufKotei3Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei3Nm, shiwakeCheckData.ufKotei3Cd));
				}
				if (kamokuUfKotei4RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei4Cd)
						&& !masterLg.existsUFKoteiZandaka("4", shiwakeCheckData.ufKotei4Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei4Nm, shiwakeCheckData.ufKotei4Cd));
				}
				if (kamokuUfKotei5RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei5Cd)
						&& !masterLg.existsUFKoteiZandaka("5", shiwakeCheckData.ufKotei5Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei5Nm, shiwakeCheckData.ufKotei5Cd));
				}
				if (kamokuUfKotei6RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei6Cd)
						&& !masterLg.existsUFKoteiZandaka("6", shiwakeCheckData.ufKotei6Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei6Nm, shiwakeCheckData.ufKotei6Cd));
				}
				if (kamokuUfKotei7RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei7Cd)
						&& !masterLg.existsUFKoteiZandaka("7", shiwakeCheckData.ufKotei7Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei7Nm, shiwakeCheckData.ufKotei7Cd));
				}
				if (kamokuUfKotei8RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei8Cd)
						&& !masterLg.existsUFKoteiZandaka("8", shiwakeCheckData.ufKotei8Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei8Nm, shiwakeCheckData.ufKotei8Cd));
				}
				if (kamokuUfKotei9RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei9Cd)
						&& !masterLg.existsUFKoteiZandaka("9", shiwakeCheckData.ufKotei9Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei9Nm, shiwakeCheckData.ufKotei9Cd));
				}
				if (kamokuUfKotei10RelationCheckFlg && StringUtils.isNotEmpty(shiwakeCheckData.ufKotei10Cd)
						&& !masterLg.existsUFKoteiZandaka("10", shiwakeCheckData.ufKotei10Cd, kamokuCd)) {
					errorList.add(String.format(formatMsg, shiwakeCheckData.ufKotei10Nm, shiwakeCheckData.ufKotei10Cd));
				}
			}
		}
	}

	/**
	 * 負担部門入力チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param errorList エラーリスト
	 */
	protected void checkFutanBumon(ShiwakeCheckData shiwakeCheckData, List<String> errorList) {

		if(null == shiwakeCheckData.futanBumonCd)
		{
			return;
		}

		if (StringUtils.isNotEmpty(shiwakeCheckData.futanBumonCd) && StringUtils.isEmpty(masterLg.findFutanBumonName(shiwakeCheckData.futanBumonCd))) {
			errorList.add(shiwakeCheckData.futanBumonNm + "[" + shiwakeCheckData.futanBumonCd + "]は存在しません。");
		}
	}

	/**
	 * 取引先入力チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param errorList エラーリスト
	 */
	public void checkTorihikisaki(ShiwakeCheckData shiwakeCheckData, List<String> errorList) {

		if(null == shiwakeCheckData.torihikisakiCd)
		{
			return;
		}

		if (StringUtils.isNotEmpty(shiwakeCheckData.torihikisakiCd) && StringUtils.isEmpty(masterLg.findTorihikisakiName(shiwakeCheckData.torihikisakiCd))) {
			errorList.add(shiwakeCheckData.torihikisakiNm + "[" + shiwakeCheckData.torihikisakiCd + "]は存在しません。");
		}
	}

	/**
	 * 課税区分入力チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param denpyouKbn 伝票区分
	 * @param errorList エラーリスト
	 */
	protected void checkKazeiKbn(ShiwakeCheckData shiwakeCheckData, String denpyouKbn, List<String> errorList) {

		if(null == shiwakeCheckData.kazeiKbn)
		{
			return;
		}

		if (StringUtils.isNotEmpty(shiwakeCheckData.kazeiKbn)) {

			List<GMap> kazeiKbnList = systemLg.loadNaibuCdSetting("kazei_kbn");
			if(!StringUtils.isEmpty(denpyouKbn) && denpyouKbn.equals(DENPYOU_KBN.SIHARAIIRAI)){
				kazeiKbnList.addAll(systemLg.loadNaibuCdSetting("kazei_kbn_shiharaiirai"));
			}

			for (GMap map : kazeiKbnList) {

				String naibuCd = (String)map.get("naibu_cd");

				if(naibuCd.equals(shiwakeCheckData.kazeiKbn))
				{
					return;
				}
			}

			errorList.add(shiwakeCheckData.kazeiKbnNm + "[" + shiwakeCheckData.kazeiKbn + "]は存在しません。");
		}
	}

	/**
	 * プロジェクト入力チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param errorList エラーリスト
	 */
	protected void checkPJ(ShiwakeCheckData shiwakeCheckData, List<String> errorList) {
		if(null == shiwakeCheckData.projectCd) {
			return;
		}

		String shiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.PJ_SHIYOU_FLG);
		String projectCd = shiwakeCheckData.projectCd;
		
		if (
			("1".equals(shiyouFlg))
			&& StringUtils.isNotEmpty(projectCd)
			&& StringUtils.isEmpty(masterLg.findProjectName(projectCd,
					true))) {
			errorList.add(shiwakeCheckData.projectNm + "[" + projectCd + "]は"
					+ (StringUtils.isEmpty(masterLg.findProjectName(projectCd, false))
						? "存在しません。"
						: "終了区分が「終了」であるため、使用できません。"));
		}
	}

	/**
	 * セグメント入力チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param errorList エラーリスト
	 */
	protected void checkSegment(ShiwakeCheckData shiwakeCheckData, List<String> errorList) {
		if(null == shiwakeCheckData.segmentCd) {
			return;
		}

		String shiyouFlg = KaishaInfo.getKaishaInfo(ColumnName.SEGMENT_SHIYOU_FLG);
		if (
			("1".equals(shiyouFlg))
			&& StringUtils.isNotEmpty(shiwakeCheckData.segmentCd)
			&& StringUtils.isEmpty(masterLg.findSegmentName(shiwakeCheckData.segmentCd))) {
			errorList.add(shiwakeCheckData.segmentNm + "[" + shiwakeCheckData.segmentCd + "]は存在しません。");
		}
	}

	/**
	 * HF入力チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param num HF番号
	 * @param hfCd HFコード
	 * @param hfNm HF名
	 * @param hfName 設定情報テーブルのカラムHF名
	 * @param hfHissuFlg 設定情報テーブルのカラムHF必須フラグ
	 * @param hfShiyouFlg 設定情報テーブルのカラムHF使用フラグ
	 * @param errorList エラーリスト
	 */
	protected void checkHF(String num, String hfCd, String hfNm, String hfName, String hfHissuFlg, String hfShiyouFlg, List<String> errorList) {

		if(null == hfCd) {
			return;
		}

		String hissuFlg = KaishaInfo.getKaishaInfo(hfHissuFlg);
		if("1".equals(hissuFlg)){
			if(StringUtils.isEmpty(hfCd)){
				errorList.add( KaishaInfo.getKaishaInfo(hfName) + "が入力されていません。" );
				return;
			}
		}

		String shiyouFlg = KaishaInfo.getKaishaInfo(hfShiyouFlg);
		if (
			(SHIYOU_FLG.MASTER.equals(shiyouFlg) || SHIYOU_FLG.ZANDAKA.equals(shiyouFlg))
			&& StringUtils.isNotEmpty(hfCd)
			&& StringUtils.isEmpty(masterLg.findHfName(num, hfCd))) {
			errorList.add(hfNm + "[" + hfCd + "]は存在しません。");
		}
	}

	/**
	 * UF入力チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param num UF番号
	 * @param ufCd UFコード
	 * @param ufNm UF名
	 * @param ufShiyouFlg 設定情報テーブルのカラムUF使用フラグ
	 * @param errorList エラーリスト
	 */
	protected void checkUF(String num, String ufCd, String ufNm, String ufShiyouFlg, List<String> errorList) {
		if(null == ufCd) {
			return;
		}

		String shiyouFlg = KaishaInfo.getKaishaInfo(ufShiyouFlg);
		if (
			(SHIYOU_FLG.MASTER.equals(shiyouFlg) || SHIYOU_FLG.ZANDAKA.equals(shiyouFlg))
			&& StringUtils.isNotEmpty(ufCd)
			&& StringUtils.isEmpty(masterLg.findUfName(num, ufCd))) {
			errorList.add(ufNm + "[" + ufCd + "]は存在しません。");
		}
	}

	/**
	 * UF固定入力チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param num UF固定番号
	 * @param ufKoteiCd UF固定コード
	 * @param ufKoteiNm UF固定名
	 * @param ufShiyouFlg 設定情報テーブルのカラムUF使用フラグ
	 * @param errorList エラーリスト
	 */
	protected void checkUFKotei(String num, String ufKoteiCd, String ufKoteiNm, String ufShiyouFlg, List<String> errorList) {
		if(null == ufKoteiCd) {
			return;
		}

		String shiyouFlg = KaishaInfo.getKaishaInfo(ufShiyouFlg);
		if (
			(SHIYOU_FLG.MASTER.equals(shiyouFlg) || SHIYOU_FLG.ZANDAKA.equals(shiyouFlg))
			&& StringUtils.isNotEmpty(ufKoteiCd)
			&& StringUtils.isEmpty(masterLg.findUfKoteiName(num, ufKoteiCd))) {
			errorList.add(ufKoteiNm + "[" + ufKoteiCd + "]は存在しません。");
		}
	}

	/**
	 * 仕訳パターン入力チェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param denpyouKbn 伝票区分
	 * @param shiwakePatternSettingKbn 仕訳パターン設定区分
	 * @param daihyouBumonCd 代表部門
	 * @param shiwakeCheckData 仕訳チェックデータ
	 * @param errorList エラーリスト
	 */
	protected void checkShiwakePattern(String denpyouKbn, String shiwakePatternSettingKbn, String daihyouBumonCd, ShiwakeCheckData shiwakeCheckData, List<String> errorList) {
		if(StringUtils.isEmpty(denpyouKbn) || null == shiwakeCheckData.shiwakeEdaNo) {
			return;
		}

		String shiwakeEdanoNm = shiwakeCheckData.shiwakeEdaNoNm;
		String shiwakeEdanoCd = shiwakeCheckData.shiwakeEdaNo;

		//仕訳パターンの取得
		GMap shiwakePatten = kaikeiLg.findShiwakePattern(denpyouKbn, Integer.parseInt(shiwakeEdanoCd));

		//仕訳パターンの存在チェック
		if (null == shiwakePatten) {
			errorList.add("伝票区分[" + denpyouKbn + "]の" + shiwakeEdanoNm + "[" + shiwakeEdanoCd + "]は存在しません。");

		} else {
			if(shiwakePatten.get("delete_flg").equals("1")) {
				errorList.add("伝票区分[" + denpyouKbn + "]の" + shiwakeEdanoNm + "[" + shiwakeEdanoCd + "]は削除済です。");
				return;

			}
			if(! EteamCommon.between(EteamCommon.today(), shiwakePatten.get("yuukou_kigen_from"), shiwakePatten.get("yuukou_kigen_to"))) {
				errorList.add("伝票区分[" + denpyouKbn + "]の" + shiwakeEdanoNm + "[" + shiwakeEdanoCd + "]は有効期限範囲外です。");
				return;
			}

			// メッセージフォーマット
			String formatMsgBlank = "%sを空白にしてください。";
			String formatMsgHissu = "%sを入力してください。";
			String formatMsgShiteiCd = "%sは[%s]を入力してください。";

			//仕訳パターン項目の取得
			Map<String, String> shiwakePattenKoumoku = this.getShiwakePattenKoumoku(shiwakePatternSettingKbn);

			//仕訳パターン整合性チェック(取引先)
			if (null != shiwakeCheckData.torihikisakiCd) {
				String checkNm = shiwakeCheckData.torihikisakiNm;
				String checkCd = shiwakeCheckData.torihikisakiCd;
				String koumoku = shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.TORIHIKISAKI);
				String pattern = (String)shiwakePatten.get(koumoku);

				switch (pattern) {
					case "":
						// 仕訳パターンが「空白」を指定している場合
						if ("".equals(checkCd) == false) {
							errorList.add(String.format(formatMsgBlank,checkNm));
						}
						break;
					case EteamConst.ShiwakeConst.TORIHIKI:
						// 仕訳パターンが「任意コード」を指定している場合
						if ("".equals(checkCd) == true) {
							errorList.add(String.format(formatMsgHissu,checkNm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」を指定している場合
						if (!pattern.equals(checkCd)) {
							errorList.add(String.format(formatMsgShiteiCd,checkNm,pattern));
						}
				}
			}

			//仕訳パターン整合性チェック(勘定科目)
			if (null != shiwakeCheckData.kamokuCd) {
				String checkNm = shiwakeCheckData.kamokuNm;
				String checkCd = shiwakeCheckData.kamokuCd;
				String koumoku = shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.KAMOKU);
				String pattern = (String)shiwakePatten.get(koumoku);

				switch (pattern) {
					case EteamConst.ShiwakeConst.KAMOKU:
						// 仕訳パターンが「任意コード」を指定している場合
						if ("".equals(checkCd) == true) {
							errorList.add(String.format(formatMsgHissu,checkNm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」を指定している場合
						if (!pattern.equals(checkCd)) {
							errorList.add(String.format(formatMsgShiteiCd,checkNm,pattern));
						}
				}
			}

			//仕訳パターン整合性チェック(勘定科目枝番)
			if (null != shiwakeCheckData.kamokuEdabanCd) {
				String checkNm = shiwakeCheckData.kamokuEdabanNm;
				String checkCd = shiwakeCheckData.kamokuEdabanCd;
				String koumoku = shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.KAMOKUEDANO);
				String pattern = (String)shiwakePatten.get(koumoku);

				switch (pattern) {
					case "":
						// 仕訳パターンが「空白」を指定している場合
						if ("".equals(checkCd) == false) {
							errorList.add(String.format(formatMsgBlank,checkNm));
						}
						break;
					case EteamConst.ShiwakeConst.EDABAN:
						// 仕訳パターンが「任意コード」を指定している場合
						if ("".equals(checkCd) == true) {
							errorList.add(String.format(formatMsgHissu,checkNm));
						}
						break;
					case MasterKanriCategoryLogic.ZAIMU:
						// 仕訳パターンが「財務枝番コード」を指定している場合
						// チェックなし
						break;
					default:
						// 仕訳パターンが「固定コード」を指定している場合
						if (!pattern.equals(checkCd)) {
							errorList.add(String.format(formatMsgShiteiCd,checkNm,pattern));
						}
				}
			}

			//仕訳パターン整合性チェック(負担部門)
			if (null != shiwakeCheckData.futanBumonCd) {
				String checkNm = shiwakeCheckData.futanBumonNm;
				String checkCd = shiwakeCheckData.futanBumonCd;
				String koumoku = shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.FUTANBUMON);
				String pattern = (String)shiwakePatten.get(koumoku);

				switch (pattern) {
					case EteamConst.ShiwakeConst.FUTAN:
					case EteamConst.ShiwakeConst.SYOKIDAIHYOU:
						// 仕訳パターンが「任意コード」又は「初期代表」を指定している場合
						if ("".equals(checkCd) == true) {
							errorList.add(String.format(formatMsgHissu,checkNm));
						}
						break;
					case EteamConst.ShiwakeConst.DAIHYOUBUMON:
						if(null != daihyouBumonCd) {
							// 仕訳パターンが「代表部門コード」を指定している場合
							if (!daihyouBumonCd.equals(checkCd)) {
								errorList.add(String.format(formatMsgShiteiCd,checkNm,daihyouBumonCd));
							}
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(checkCd)) {
							errorList.add(String.format(formatMsgShiteiCd,checkNm,pattern));
						}
				}
			}

			//仕訳パターン整合性チェック(プロジェクト)
			if (null != shiwakeCheckData.projectCd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.PROJECT));
				switch (pattern) {
					case EteamConst.ShiwakeConst.PROJECT:
						// 仕訳パターンが「任意コード」を指定している場合
						if ("".equals(shiwakeCheckData.projectCd)) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.projectNm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.projectCd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.projectNm, pattern));
						}
				}
			}

			//仕訳パターン整合性チェック(セグメント)
			if (null != shiwakeCheckData.segmentCd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.SEGMENT));
				switch (pattern) {
					case EteamConst.ShiwakeConst.SEGMENT:
						// 仕訳パターンが「任意コード」を指定している場合
						if ("".equals(shiwakeCheckData.segmentCd)) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.segmentNm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.segmentCd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.segmentNm, pattern));
						}
				}
			}

			if (null != shiwakeCheckData.uf1Cd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF1));
				switch (pattern) {
					case EteamConst.ShiwakeConst.UF:
						// 仕訳パターンが「任意コード」を指定していてUFがマスター化されている場合
						if ("".equals(shiwakeCheckData.uf1Cd) == true && Integer.parseInt(KaishaInfo.getKaishaInfo(ColumnName.UF1_SHIYOU_FLG)) >= 2) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf1Nm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.uf1Cd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.uf1Nm, pattern));
						}
				}
			}

			if (null != shiwakeCheckData.uf2Cd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF2));
				switch (pattern) {
					case EteamConst.ShiwakeConst.UF:
						// 仕訳パターンが「任意コード」を指定していてUFがマスター化されている場合
						if ("".equals(shiwakeCheckData.uf2Cd) == true && Integer.parseInt(KaishaInfo.getKaishaInfo(ColumnName.UF2_SHIYOU_FLG)) >= 2) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf2Nm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.uf2Cd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.uf2Nm, pattern));
						}
				}
			}

			if (null != shiwakeCheckData.uf3Cd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF3));
				switch (pattern) {
					case EteamConst.ShiwakeConst.UF:
						// 仕訳パターンが「任意コード」を指定していてUFがマスター化されている場合
						if ("".equals(shiwakeCheckData.uf3Cd) == true && Integer.parseInt(KaishaInfo.getKaishaInfo(ColumnName.UF3_SHIYOU_FLG)) >= 2) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf3Nm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.uf3Cd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.uf3Nm, pattern));
						}
				}
			}

			if (null != shiwakeCheckData.uf4Cd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF4));
				switch (pattern) {
					case EteamConst.ShiwakeConst.UF:
						// 仕訳パターンが「任意コード」を指定していてUFがマスター化されている場合
						if ("".equals(shiwakeCheckData.uf4Cd) == true && Integer.parseInt(KaishaInfo.getKaishaInfo(ColumnName.UF4_SHIYOU_FLG)) >= 2) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf4Nm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.uf4Cd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.uf4Nm, pattern));
						}
				}
			}

			if (null != shiwakeCheckData.uf5Cd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF5));
				switch (pattern) {
					case EteamConst.ShiwakeConst.UF:
						// 仕訳パターンが「任意コード」を指定していてUFがマスター化されている場合
						if ("".equals(shiwakeCheckData.uf5Cd) == true && Integer.parseInt(KaishaInfo.getKaishaInfo(ColumnName.UF5_SHIYOU_FLG)) >= 2) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf5Nm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.uf5Cd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.uf5Nm, pattern));
						}
				}
			}

			if (null != shiwakeCheckData.uf6Cd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF6));
				switch (pattern) {
					case EteamConst.ShiwakeConst.UF:
						// 仕訳パターンが「任意コード」を指定していてUFがマスター化されている場合
						if ("".equals(shiwakeCheckData.uf6Cd) == true && Integer.parseInt(KaishaInfo.getKaishaInfo(ColumnName.UF6_SHIYOU_FLG)) >= 2) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf6Nm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.uf6Cd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.uf6Nm, pattern));
						}
				}
			}

			if (null != shiwakeCheckData.uf7Cd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF7));
				switch (pattern) {
					case EteamConst.ShiwakeConst.UF:
						// 仕訳パターンが「任意コード」を指定していてUFがマスター化されている場合
						if ("".equals(shiwakeCheckData.uf7Cd) == true && Integer.parseInt(KaishaInfo.getKaishaInfo(ColumnName.UF7_SHIYOU_FLG)) >= 2) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf7Nm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.uf7Cd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.uf7Nm, pattern));
						}
				}
			}

			if (null != shiwakeCheckData.uf8Cd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF8));
				switch (pattern) {
					case EteamConst.ShiwakeConst.UF:
						// 仕訳パターンが「任意コード」を指定していてUFがマスター化されている場合
						if ("".equals(shiwakeCheckData.uf8Cd) == true && Integer.parseInt(KaishaInfo.getKaishaInfo(ColumnName.UF8_SHIYOU_FLG)) >= 2) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf8Nm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.uf8Cd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.uf8Nm, pattern));
						}
				}
			}

			if (null != shiwakeCheckData.uf9Cd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF9));
				switch (pattern) {
					case EteamConst.ShiwakeConst.UF:
						// 仕訳パターンが「任意コード」を指定していてUFがマスター化されている場合
						if ("".equals(shiwakeCheckData.uf9Cd) == true && Integer.parseInt(KaishaInfo.getKaishaInfo(ColumnName.UF9_SHIYOU_FLG)) >= 2) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf9Nm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.uf9Cd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.uf9Nm, pattern));
						}
				}
			}

			if (null != shiwakeCheckData.uf10Cd) {
				String pattern = (String)shiwakePatten.get(shiwakePattenKoumoku.get(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF10));
				switch (pattern) {
					case EteamConst.ShiwakeConst.UF:
						// 仕訳パターンが「任意コード」を指定していてUFがマスター化されている場合
						if ("".equals(shiwakeCheckData.uf10Cd) == true && Integer.parseInt(KaishaInfo.getKaishaInfo(ColumnName.UF10_SHIYOU_FLG)) >= 2) {
							errorList.add(String.format(formatMsgHissu, shiwakeCheckData.uf10Nm));
						}
						break;
					default:
						// 仕訳パターンが「固定コード」 or ブランクを指定している場合
						if (!pattern.equals(shiwakeCheckData.uf10Cd)) {
							errorList.add(String.format(formatMsgShiteiCd, shiwakeCheckData.uf10Nm, pattern));
						}
				}
			}
		}
	}


	/**
	 * 仕訳パターン項目の取得
	 * @param shiwakePatternSettingKbn 仕訳パターン設定区分
	 * @return 仕訳パターン項目
	 */
	protected Map<String, String> getShiwakePattenKoumoku(String shiwakePatternSettingKbn){
		Map<String, String> rtn = new HashMap<String, String>();
		switch (shiwakePatternSettingKbn) {
			case EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KARIKATA:
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.TORIHIKISAKI, "kari_torihikisaki_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.KAMOKU, "kari_kamoku_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.KAMOKUEDANO, "kari_kamoku_edaban_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.FUTANBUMON, "kari_futan_bumon_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.PROJECT, "kari_project_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.SEGMENT, "kari_segment_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF1, "kari_uf1_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF2, "kari_uf2_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF3, "kari_uf3_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF4, "kari_uf4_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF5, "kari_uf5_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF6, "kari_uf6_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF7, "kari_uf7_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF8, "kari_uf8_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF9, "kari_uf9_cd");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF10, "kari_uf10_cd");
				break;
			case EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA1:
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.TORIHIKISAKI, "kashi_torihikisaki_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.KAMOKU, "kashi_kamoku_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.KAMOKUEDANO, "kashi_kamoku_edaban_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.FUTANBUMON, "kashi_futan_bumon_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.PROJECT, "kashi_project_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.SEGMENT, "kashi_segment_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF1, "kashi_uf1_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF2, "kashi_uf2_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF3, "kashi_uf3_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF4, "kashi_uf4_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF5, "kashi_uf5_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF6, "kashi_uf6_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF7, "kashi_uf7_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF8, "kashi_uf8_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF9, "kashi_uf9_cd1");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF10, "kashi_uf10_cd1");

				break;
			case EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA2:
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.TORIHIKISAKI, "kashi_torihikisaki_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.KAMOKU, "kashi_kamoku_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.KAMOKUEDANO, "kashi_kamoku_edaban_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.FUTANBUMON, "kashi_futan_bumon_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.PROJECT, "kashi_project_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.SEGMENT, "kashi_segment_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF1, "kashi_uf1_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF2, "kashi_uf2_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF3, "kashi_uf3_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF4, "kashi_uf4_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF5, "kashi_uf5_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF6, "kashi_uf6_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF7, "kashi_uf7_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF8, "kashi_uf8_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF9, "kashi_uf9_cd2");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF10, "kashi_uf10_cd2");
				break;
			case EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA3:
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.TORIHIKISAKI, "kashi_torihikisaki_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.KAMOKU, "kashi_kamoku_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.KAMOKUEDANO, "kashi_kamoku_edaban_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.FUTANBUMON, "kashi_futan_bumon_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.PROJECT, "kashi_project_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.SEGMENT, "kashi_segment_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF1, "kashi_uf1_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF2, "kashi_uf2_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF3, "kashi_uf3_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF4, "kashi_uf4_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF5, "kashi_uf5_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF6, "kashi_uf6_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF7, "kashi_uf7_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF8, "kashi_uf8_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF9, "kashi_uf9_cd3");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF10, "kashi_uf10_cd3");
				break;
			case EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA4:
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.TORIHIKISAKI, "kashi_torihikisaki_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.PROJECT, "kashi_project_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.SEGMENT, "kashi_segment_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF1, "kashi_uf1_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF2, "kashi_uf2_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF3, "kashi_uf3_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF4, "kashi_uf4_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF5, "kashi_uf5_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF6, "kashi_uf6_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF7, "kashi_uf7_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF8, "kashi_uf8_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF9, "kashi_uf9_cd4");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF10, "kashi_uf10_cd4");
				break;
			case EteamNaibuCodeSetting.SHIWAKE_PATTERN_SETTING_KBN.KASHIKATA5:
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.TORIHIKISAKI, "kashi_torihikisaki_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.PROJECT, "kashi_project_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.SEGMENT, "kashi_segment_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF1, "kashi_uf1_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF2, "kashi_uf2_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF3, "kashi_uf3_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF4, "kashi_uf4_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF5, "kashi_uf5_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF6, "kashi_uf6_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF7, "kashi_uf7_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF8, "kashi_uf8_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF9, "kashi_uf9_cd5");
				rtn.put(EteamNaibuCodeSetting.KAIKEI_KOUMOKU.UF10, "kashi_uf10_cd5");
				break;
		}
		return rtn;
	}

	/**
	 * 起票者情報の取得
	 * @param denpyouId 伝票ID
	 * @return 起票者情報
	 */
	public GMap findKihyouUser(String denpyouId) {
		final String sql =
				"SELECT * FROM shounin_route s "
			+ "WHERE "
			+ "  s.denpyou_id = ? "
			+ "  AND s.edano = 1 ";
		return connection.find(sql, denpyouId);
	}

	/**
	 * 振込日ルールから振込日を決定する。
	 * ルール上、基準日が見つからなければnullを返す。
	 *
	 * 当メソッドが返した日付を支払日(振込)に設定する。
	 * ただし、当メソッドがnullを返した場合は支払日を変更しない。
	 *
	 * @param kijunDate 基準日(計上日等)
	 * @return 振込日
	 */
	public Date decideFurikomiDateHi(java.util.Date kijunDate) {
		//基準日が渡されていないので振込日も決定できず
		if (kijunDate == null)
		{
			return null;
		}

		//振込日ルールの、基準日(1~31)に対する振込日(1~30)を取得
		int i_kijunDate = (int)DateUtils.getFragmentInDays(kijunDate, Calendar.MONTH);
		Integer i_furikomiDate = masterLg.findFurikomiBiRuleHi(i_kijunDate);

		//振込日ルールが見つからないので振込日を決定できず
		if (i_furikomiDate == null)
		{
			return null;
		}

		//振込ルールから振込日を決定する
		@SuppressWarnings("deprecation")
		int year = kijunDate.getYear(); //※year-1900、2017年なら117
		@SuppressWarnings("deprecation")
		int month = kijunDate.getMonth(); //※0～11、1月なら0
		@SuppressWarnings("deprecation")
		int date = kijunDate.getDate(); //※1～31
		if (i_furikomiDate < i_kijunDate) {
			//振込日＜基準日なので、基準日翌月の振込日。
			month++;
			date = i_furikomiDate;
		} else {
			//基準日<=振込日なので、基準日同月の振込日
			date = i_furikomiDate;
		}
		if (month > 11) { //※11は12月のこと
			month = 0; //0は1月のこと
			year++;
		}

		//振込ルール通りに算出した振込日が存在しない日だったら(31日とかうるう年とか)翌月の1日に
		if (! dateExists(String.format("%1$04d", year + 1900) + String.format("%1$02d", month + 1) + String.format("%1$02d", date))) {
			month++;
			date = 1;
			if (month > 11) { //※11は12月のこと
				month = 0; //0は1月のこと
				year++;
			}
		}

		//振込日ルールで判定した日付以降で個人精算支払日に登録された日を探す
		Date ret = EteamCommon.toDate(String.format("%1$04d", year + 1900) + "/" + String.format("%1$02d", month + 1) + "/" + String.format("%1$02d", date));
		return masterLg.findKojinSeisanShiharaibiAfter(new Date(ret.getTime()));
	}

	/**
	 * 振込曜日ルールから振込日を決定する。
	 * ルール上、基準日が見つからなければnullを返す。
	 *
	 * 当メソッドが返した日付を支払日(振込)に設定する。
	 * ただし、当メソッドがnullを返した場合は支払日を変更しない。
	 *
	 * @param kijunDate 基準日(計上日等)
	 * @return 振込日
	 */
	public Date decideFurikomiDateYoubi(java.util.Date kijunDate) {
		//基準日が渡されていないので振込日も決定できず
		if (kijunDate == null)
		{
			return null;
		}

		//振込曜日ルールの、基準曜日(1~7)に対する曜日(1~7)を取得
		Calendar cal = Calendar.getInstance();
		cal.setTime(kijunDate);
		int i_kijunWeekday = convertWeekday(cal.get(Calendar.DAY_OF_WEEK));
		Integer i_furikomiWeekday = masterLg.findFurikomiBiRuleYoubi(i_kijunWeekday);

		//振込曜日ルールが見つからないので振込日を決定できず
		if (i_furikomiWeekday == null)
		{
			return null;
		}
		//振込日が本日or過去日になるので決定できず→本来おかしなことであるが設定通りのことなのでOKとしてコメントアウトしとく
// if (i_furikomiWeekday > 10 && i_furikomiWeekday < 20 && i_furikomiWeekday%10 <= i_kijunWeekday)
// {
// return null;
// }

		//振込ルールから振込日を決定する
		for (int i = 0; i < (i_furikomiWeekday/10) - 1 ; i++) {
			cal.add(Calendar.DATE, 7);
		}
		cal.add(Calendar.DATE, (i_furikomiWeekday%10) - i_kijunWeekday);

		//振込日ルールで判定した日付以降で個人精算支払日に登録された日を探す
		return masterLg.findKojinSeisanShiharaibiAfter(new Date(cal.getTimeInMillis()));
	}


	/**
	 * 摘要オーバー注記文言を返す。
	 * @return 注記文言
	 */
	public String getTekiyouChuuki() {
		if(systemLg.judgeKinouSeigyoON(KINOU_SEIGYO_CD.CHUUKI_PREVIEW)) {
			return "※財務転記時にカットされる文言があります。";
		}
		return "";
	}
	/**
	 * 摘要オーバー注記文言(ピンポイント）を返す。
	 * @param cuttedTekiyou カット後の摘要文言
	 * @return 注記文言
	 */
	public String getTekiyouChuukiMeisai(String cuttedTekiyou) {
		if(systemLg.judgeKinouSeigyoON(KINOU_SEIGYO_CD.CHUUKI_PREVIEW)) {
			return "※財務転記時にカットされます。財務転記される文言は「"+ cuttedTekiyou + "」";
		}
		return "";
	}

	/**
	 * 存在する日付か判定
	 * @param yyyymmdd 日付(yyyyMMdd)
	 * @return 存在するならtrue
	 */
	protected boolean dateExists(String yyyymmdd) {
		try {
			DateUtils.parseDateStrictly(yyyymmdd, "yyyyMMdd");
		    return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 仕訳パターンに照らして負担部門コードの変換を行う
	 * @param bumonCd 画面で入力された部門コード(空か固定コードか、初期で任意)
	 * @param shiwakePattern 仕訳パターンの部門コード(空か固定コードか初期で任意、<FUTAN>、<DAIHYOUBUMON>、<SYOKIDAIHYOU>)
	 * @param daihyouBumonCd 代表部門コード
	 * @return 変換後負担部門
	 */
	public String convFutanBumon(String bumonCd, String shiwakePattern, String daihyouBumonCd) {
		bumonCd = n2b(bumonCd);
		switch (shiwakePattern) {
			case EteamConst.ShiwakeConst.FUTAN:
				//何もしない(画面入力のまま)
				return bumonCd;
			case EteamConst.ShiwakeConst.DAIHYOUBUMON:
				//代表部門取得方法
				return daihyouBumonCd;
			case EteamConst.ShiwakeConst.SYOKIDAIHYOU:
				//初期代表。勝手に代表部門に置き換わっては困るので画面入力を見て、空ならば代表負担部門をセットする
				return isEmpty(bumonCd) ? daihyouBumonCd : bumonCd;
			default:
				//固定コード値 or Blanc
				return shiwakePattern;
		}
	}

	/**
	 * 仕訳パターンに照らして取引先コードの変換を行う
	 * @param torihikisakiCd 画面で入力された取引先コード(空か固定コード)
	 * @param shiwakePattern 仕訳パターンの取引先コード(空か固定コード、<TORIHIKI>)
	 * @return 変換後取引先コード
	 */
	public String convTorihikisaki(String torihikisakiCd, String shiwakePattern){
		torihikisakiCd = n2b(torihikisakiCd);
		switch (shiwakePattern) {
			case ShiwakeConst.TORIHIKI:
				//何もしない(画面入力のまま)
				return torihikisakiCd;
			default:
				//固定コード値 or Blanc
				return shiwakePattern;
		}
	}

	/**
	 * 仕訳パターンに照らしてプロジェクトコードの変換を行う
	 * @param projectCd 画面で入力されたプロジェクトコード(空か固定コード)
	 * @param shiwakePattern 仕訳パターンのプロジェクトコード(空か固定コード、<PROJECT>)
	 * @return 変換後取引先コード
	 */
	public String convProject(String projectCd, String shiwakePattern){
		projectCd = n2b(projectCd);
		switch (shiwakePattern) {
			case ShiwakeConst.PROJECT:
				//何もしない(画面入力のまま)
				return projectCd;
			default:
				//固定コード値 or Blanc
				return shiwakePattern;
		}
	}

	/**
	 * 仕訳パターンに照らしてセグメントコードの変換を行う
	 * @param segmentCd 画面で入力されたセグメントコード(空か固定コード)
	 * @param shiwakePattern 仕訳パターンのセグメントコード(空か固定コード、<SG>)
	 * @return 変換後取引先コード
	 */
	public String convSegment(String segmentCd, String shiwakePattern){
		segmentCd = n2b(segmentCd);
		switch (shiwakePattern) {
			case ShiwakeConst.SEGMENT:
				//何もしない(画面入力のまま)
				return segmentCd;
			default:
				//固定コード値 or Blanc
				return shiwakePattern;
		}
	}

	/**
	 * 仕訳パターンに照らしてUFコードの変換を行う
	 * @param ufCd 画面で入力されたUFコード(空か固定コード)
	 * @param shiwakePattern 仕訳パターンのUFコード(空か固定コード、<UF>)
	 * @return 変換後UF
	 */
	public String convUf(String ufCd, String shiwakePattern) {
		ufCd = n2b(ufCd);
		switch (shiwakePattern) {
			case EteamConst.ShiwakeConst.UF:
				//何もしない(画面入力のまま)
				return ufCd;
			default:
				//固定コード値 or Blanc
				return shiwakePattern;
		}
	}

	/**
	 * 仕訳パターンに照らして消費税率の変換を行う
	 * @param zeiritsu 画面で入力された消費税率
	 * @param keigenZeiritsuKbn 画面で入力された消費税率の軽減税率
	 * @param shiwakePatternZeiritsu 仕訳パターンの消費税率
	 * @param shiwakePatternKeigenZeiritsuKbn 仕訳パターンの軽減税率区分
	 * @return [0]変換後の消費税率、[1]変換後の軽減税率
	 */
	public String[] convZeiritsu(String zeiritsu, String keigenZeiritsuKbn, String shiwakePatternZeiritsu, String shiwakePatternKeigenZeiritsuKbn){
		switch (shiwakePatternZeiritsu) {
		case EteamConst.ShiwakeConst.ZEIRITSU:
		case "":	// 消費税率非表示の取引の場合
			//何もしない(画面入力のまま)
			return new String[] {zeiritsu, keigenZeiritsuKbn};
		default:
			//固定コード値
			return new String[] {shiwakePatternZeiritsu, shiwakePatternKeigenZeiritsuKbn};
		}
	}

	/**
	 * ユーザーの役職コードを取得
	 * @param userId ユーザーID
	 * @return 役職コード
	 */
	public String getYakushokuCd(String userId){
		GMap user = bumonUserLg.selectUserInfo(userId);
		String shainNo = (null == user)? "" :(String)user.get("shain_no");
		GMap shain = masterLg.findShain(shainNo);
		return (null != shain)? (String)shain.get("yakushoku_cd") : "";
	}


	/**
	 * 指定された負担部門が起票者or起票部門のセキュリティパターンで使用できる部門かチェック
	 * エラーがあれば、エラーリストにメッセージを詰める。
	 * @param futanBumonCd 負担部門コード
	 * @param futanBumonNm 負担部門項目名
	 * @param user セッションユーザー
	 * @param denpyouId 伝票ID
	 * @param kihyouBumonCd 起票部門コード
	 * @param errorList エラーリスト
	 * @return セキュリティパターンで使用できる部門かどうか
	 */
	public boolean checkFutanBumonWithSecurity(String futanBumonCd, String futanBumonNm, User user, String denpyouId, String kihyouBumonCd, List<String> errorList) {

		// 「負担部門選択時に集計部門による制限を行う」オプションが無効ならチェックしない
		if ( !("1".equals(setting.futanBumonShukeiFilter())) )
		{
			return true;
		}

		// 負担部門コードが入力なしならチェックしない
		if (futanBumonCd == null || futanBumonCd.isEmpty())
		{
			return true;
		}

		// 起票者or起票部門のセキュリティパターンリスト取得
		String userId;
		if(denpyouId == null || denpyouId.isEmpty()){
			userId = user.getSeigyoUserId();
		}else{
			//TODO FutanBumonSentakuAction,SyuukeiBumonSentakuAction,KaikeiCommonLogicで共通化しておく
			GMap map = workflowLg.selectKihyoushaData(denpyouId);
			if(map != null && map.get("user_id") != null){
				userId = (String)map.get("user_id");
			}else{
				userId = user.getSeigyoUserId();
			}
		}
		List<Integer> spList = bumonUserLg.selectSecurityPatternList(userId,kihyouBumonCd);
		List<Integer> kiList = getKiList(denpyouId);

		//指定負担部門が該当セキュリティパターンで使用できなければエラー
		if(masterLg.findBumonMasterByCdAndSptn(futanBumonCd, kiList, spList) == null){
			errorList.add(futanBumonNm + "[" + futanBumonCd + "]は起票ユーザーで利用できません。");
			return false;
		}
		return true;
	}

	/**
	 * 使用する決算期番号のリストを取得
	 * @param denpyouId 伝票ID
	 * @return 決算期番号リスト
	 */
	protected List<Integer> getKiList(String denpyouId){

		List<Integer> kbList = new ArrayList<Integer>();
		int intKes = masterLg.findKessankiBangou(EteamCommon.toDate(this.getKiDate(denpyouId)));
		//伝票系
		//(申請前) 当日日付(クライアントPC日付)に該当するkiと、その-1(翌期分)のkiに該当する内部決算期
		//(申請後) 該当伝票の申請日に該当するkiと、その-1(翌期分)のkiに該当する内部決算期
		if(intKes >= 1){kbList.add(intKes - 1);}
		if(intKes >= 0){kbList.add(intKes);}

		return kbList;

	}
	
	/**
	 * 期取得用日付を伝票IDから取得
	 * @param denpyouId 伝票ID
	 * @return 期取得用日付文字列
	 */
	public String getKiDate(String denpyouId)
	{
		// 伝票IDがない場合、当日日付
		if(isEmpty(denpyouId)){
			return EteamCommon.formatDate(new Date(System.currentTimeMillis()));
		}
		
		// 伝票IDがある場合、伝票が存在してかつ申請後なら申請日
		GMap dpmap = workflowLg.selectDenpyou(denpyouId);
		if(dpmap != null && dpmap.get("denpyou_joutai") != null
			&& !List.of(DENPYOU_JYOUTAI.MI_KIHYOU, DENPYOU_JYOUTAI.KIHYOU_CHUU).contains(dpmap.get("denpyou_joutai")))
		{
			return EteamCommon.formatDate(this.shouninJoukyouDao.selectShinseiDate(denpyouId));
		}
		
		// 上記以外なら当日日付
		return EteamCommon.formatDate(new Date(System.currentTimeMillis()));
	}

	/**
	 * @param kiDate 期取得用日付
	 * @return 決算期番号
	 */
	public int getKessankiBangou(String kiDate)
	{
		return masterLg.findKessankiBangou(EteamCommon.toDate(kiDate));
	}
	
	/**
	 * 曜日を本システム用に変換
	 * @param weekday 曜日
	 * @return 変換された曜日
	 */
	protected int convertWeekday (int weekday) {
		return --weekday == 0 ? 7 : weekday;
	}

	/**
	 * 海外分の明細の種別が日当かどうか判断する
	 * @param shubetsu1 種別1
	 * @param shubetsu2 種別2
	 * @param yakushokuCd 役職コード
	 * @return 日当の場合=true、それ以外=false
	 *
	 */
	public boolean isNittouKaigai(String shubetsu1, String shubetsu2, String yakushokuCd){
		GMap nittou = masterLg.loadKaigaiNittouKingaku(shubetsu1, shubetsu2, yakushokuCd);
		return (null != nittou && "2".equals(nittou.get("nittou_shukuhakuhi_flg")))? true : false;
	}

	/**
	 * 国内分の明細の種別が日当かどうか判断する
	 * @param shubetsu1 種別1
	 * @param shubetsu2 種別2
	 * @param yakushokuCd 役職コード
	 * @return 日当の場合=true、それ以外=false
	 *
	 */
	public boolean isNittou(String shubetsu1, String shubetsu2, String yakushokuCd){
		GMap nittou = masterLg.loadNittouKingakuAndFlgAndKbn(shubetsu1, shubetsu2, yakushokuCd);
		return (null != nittou && "2".equals(nittou.get("nittou_shukuhakuhi_flg")))? true : false;
	}

	/**
	 * 伝票状態が申請中より後かどうか判断する
	 * @param denpyouID 伝票ID
	 * @return 未起票・起票中=false、申請中・承認済・取下済み=true
	 */
	public boolean isAfterShinsei(String denpyouID){
		boolean ret = false;
		GMap denpyou = workflowLg.selectDenpyou(denpyouID);
		String joutai = denpyou.get("denpyou_joutai").toString();
		if(!(DENPYOU_JYOUTAI.MI_KIHYOU.equals(joutai)) && !(DENPYOU_JYOUTAI.KIHYOU_CHUU.equals(joutai))){
			ret = true;
		}
		return ret;
	}

	/**
	 * 有効な別伝票に起案添付済みかどうか判定する
	 * @param oyaDenpyouId 伝票ID
	 * @param KianDenpyouId 起案伝票ID
	 * @return 有効な別伝票に起案添付済みの場合はtrue
	 */
	public boolean isKaribaraiKianTenpuZumi(String oyaDenpyouId, String KianDenpyouId) {
		boolean result = false;
		if(KianDenpyouId.isEmpty()){
			result = false;
		}else if(0 < kaikeiLg.countKianTenpuZumi(oyaDenpyouId, KianDenpyouId)){
			result = true;
		}
		return result;
	}

	/**
	 * 項目の入力可否情報保持クラス
	 */
	public class InputEnableInfo
	{
		/** 科目枝番入力可 */
		public boolean kamokuEdabanEnable;
		/** 負担部門入力可 */
		public boolean futanBumonEnable;
		/** 取引先入力可 */
		public boolean torihikisakiEnable;
		/** プロジェクト入力可 */
		public boolean projectEnable;
		/** セグメント入力可 */
		public boolean segmentEnable;
		/** 交際費入力可 */
		public boolean kousaihiEnable;
		/** 人数項目表示 */
		public boolean ninzuuEnable;
		/** UF1入力可 */
		public boolean uf1Enable;
		/** UF2入力可 */
		public boolean uf2Enable;
		/** UF3入力可 */
		public boolean uf3Enable;
		/** UF4入力可 */
		public boolean uf4Enable;
		/** UF5入力可 */
		public boolean uf5Enable;
		/** UF6入力可 */
		public boolean uf6Enable;
		/** UF7入力可 */
		public boolean uf7Enable;
		/** UF8入力可 */
		public boolean uf8Enable;
		/** UF9入力可 */
		public boolean uf9Enable;
		/** UF10入力可 */
		public boolean uf10Enable;
		/** 消費税率入力可 */
		public boolean zeiritsuEnable;
	}
	
	/**
	 * 取引内容による項目の入力可否を判定する（dtoからマップを取得）
	 * @param shiwakePattern 仕訳パターンマスタレコード
	 * @return 入力可否クラス
	 *
	 */
	public InputEnableInfo judgeInputEnable(ShiwakePatternMaster shiwakePattern)
	{
		return this.judgeInputEnable(shiwakePattern.map);
	}

	/**
	 * 取引内容による項目の入力可否を判定する
	 * @param shiwakePattern 仕訳パターンマスタレコード
	 * @return 入力可否クラス
	 *
	 */
	public InputEnableInfo judgeInputEnable(GMap shiwakePattern)
	{
		var ret = new InputEnableInfo();
		ret.kamokuEdabanEnable = EteamConst.ShiwakeConst.EDABAN.equals(shiwakePattern.get("kari_kamoku_edaban_cd"));
		ret.futanBumonEnable = EteamConst.ShiwakeConst.FUTAN.equals(shiwakePattern.get("kari_futan_bumon_cd"))
				|| EteamConst.ShiwakeConst.SYOKIDAIHYOU.equals(shiwakePattern.get("kari_futan_bumon_cd"));
		ret.kousaihiEnable = "1".equals(shiwakePattern.get("kousaihi_hyouji_flg"));
		ret.ninzuuEnable = "1".equals(shiwakePattern.get("kousaihi_ninzuu_riyou_flg"));

		// 借方・貸方1-5の何れかが任意であれば入力可能
		if (EteamConst.ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kari_torihikisaki_cd"))
				|| EteamConst.ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd1"))
				|| EteamConst.ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd2"))
				|| EteamConst.ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd3"))
				|| EteamConst.ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd4"))
				|| EteamConst.ShiwakeConst.TORIHIKI.equals(shiwakePattern.get("kashi_torihikisaki_cd5"))) {
			ret.torihikisakiEnable = true;
		}

		if (EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kari_project_cd"))
				|| EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd1"))
				|| EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd2"))
				|| EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd3"))
				|| EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd4"))
				|| EteamConst.ShiwakeConst.PROJECT.equals(shiwakePattern.get("kashi_project_cd5"))) {
			ret.projectEnable = true;
		}

		if (EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kari_segment_cd"))
				|| EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd1"))
				|| EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd2"))
				|| EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd3"))
				|| EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd4"))
				|| EteamConst.ShiwakeConst.SEGMENT.equals(shiwakePattern.get("kashi_segment_cd5"))) {
			ret.segmentEnable = true;
		}

		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf1_cd"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf1_cd1"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf1_cd2"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf1_cd3"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf1_cd4"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf1_cd5"))) {
			ret.uf1Enable = true;
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf2_cd"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf2_cd1"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf2_cd2"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf2_cd3"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf2_cd4"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf2_cd5"))) {
			ret.uf2Enable = true;
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf3_cd"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf3_cd1"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf3_cd2"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf3_cd3"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf3_cd4"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf3_cd5"))) {
			ret.uf3Enable = true;
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf4_cd"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf4_cd1"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf4_cd2"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf4_cd3"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf4_cd4"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf4_cd5"))) {
			ret.uf4Enable = true;
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf5_cd"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf5_cd1"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf5_cd2"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf5_cd3"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf5_cd4"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf5_cd5"))) {
			ret.uf5Enable = true;
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf6_cd"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf6_cd1"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf6_cd2"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf6_cd3"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf6_cd4"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf6_cd5"))) {
			ret.uf6Enable = true;
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf7_cd"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf7_cd1"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf7_cd2"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf7_cd3"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf7_cd4"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf7_cd5"))) {
			ret.uf7Enable = true;
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf8_cd"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf8_cd1"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf8_cd2"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf8_cd3"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf8_cd4"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf8_cd5"))) {
			ret.uf8Enable = true;
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf9_cd"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf9_cd1"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf9_cd2"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf9_cd3"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf9_cd4"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf9_cd5"))) {
			ret.uf9Enable = true;
		}
		if (EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kari_uf10_cd"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf10_cd1"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf10_cd2"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf10_cd3"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf10_cd4"))
				|| EteamConst.ShiwakeConst.UF.equals(shiwakePattern.get("kashi_uf10_cd5"))) {
			ret.uf10Enable = true;
		}
		if (EteamConst.ShiwakeConst.ZEIRITSU.equals(shiwakePattern.get("kari_zeiritsu"))) {
			ret.zeiritsuEnable = true;
		}

		return ret;
	}

	/**
	 * 予算執行科目かどうか判定する
	 * @param denpyouId 伝票ID
	 * @param kamokuCdSet 科目コード
	 * @param nendo 年度
	 * @return 予算執行科目が1つ以上含まれればtrue
	 */
	public boolean isYosanShikkouKamoku(String denpyouId, HashSet<String> kamokuCdSet, String nendo){

		WorkflowEventControlLogic wfEvntLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, super.connection);
		MasterKanriCategoryLogic masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		YosanShikkouKanriCategoryLogic yosanLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, connection);
		TsuuchiCategoryLogic tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);

		// 決算期を取得
		Map<String, Object> mapHimozuke = wfEvntLogic.selectDenpyouKianHimozuke(denpyouId);
		String jisshiNendo = "";
		if(false == nendo.isEmpty()){
			jisshiNendo = nendo;
		}else if(null != mapHimozuke){
			if(null != mapHimozuke.get("jisshi_nendo")){ jisshiNendo = mapHimozuke.get("jisshi_nendo").toString(); }
		}
		String yosanCheckNengetsu = tsuuchiLogic.findDenpyou(denpyouId).getString("yosan_check_nengetsu");
		boolean isRuikei     = yosanCheckKikan.TO_TAISHOUZUKI.equals(setting.yosanCheckKikan());
		int kesn = masterLogic.findKesn((isRuikei)? yosanLogic.retYosanCheckNengetsuFromDate(yosanCheckNengetsu) : yosanLogic.retTargetDate(jisshiNendo));

		// セキュリティパターンを取得
		int sptn = Integer.parseInt(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.YOSAN_SECURITY_PATTERN));

		// 科目コードを1つずつ確認
		for(String kamokuCd : kamokuCdSet){
			if(yosanLogic.existKamokuSecurity(kesn, sptn, kamokuCd)){
				return false;
			}
		}

		return true;
	}

	/**
	 * 集計部門が予算執行対象部門かどうか判定する
	 * @param denpyouId 伝票ID
	 * @param bumonCdSet 部門コード
	 * @param nendo 年度
	 * @return 予算執行対象の集計部門が1つ以上含まれればtrue
	 */
	public boolean isYosanShikkouBumon(String denpyouId, HashSet<String> bumonCdSet, String nendo){

		WorkflowEventControlLogic wfEvntLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, super.connection);
		MasterKanriCategoryLogic masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		YosanShikkouKanriCategoryLogic yosanLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, connection);
		TsuuchiCategoryLogic tsuuchiLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);

		// 決算期を取得
		Map<String, Object> mapHimozuke = wfEvntLogic.selectDenpyouKianHimozuke(denpyouId);
		String jisshiNendo = "";
		if(false == nendo.isEmpty()){
			jisshiNendo = nendo;
		}else if(null != mapHimozuke){
			if(null != mapHimozuke.get("jisshi_nendo")){ jisshiNendo = mapHimozuke.get("jisshi_nendo").toString(); }
		}
		String yosanCheckNengetsu = tsuuchiLogic.findDenpyou(denpyouId).getString("yosan_check_nengetsu");
		boolean isRuikei     = yosanCheckKikan.TO_TAISHOUZUKI.equals(setting.yosanCheckKikan());
		int kesn = masterLogic.findKesn((isRuikei)? yosanLogic.retYosanCheckNengetsuFromDate(yosanCheckNengetsu) : yosanLogic.retTargetDate(jisshiNendo));

		// セキュリティパターンを取得
		int sptn = Integer.parseInt(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.YOSAN_SECURITY_PATTERN));

		// 科目コードを1つずつ確認
		for(String bumonCd : bumonCdSet){
			if(yosanLogic.existBumonSecurity(kesn, sptn, bumonCd)){
				return false;
			}
		}

		return true;
	}

	/**
	 * 単価（外貨）の計算
	 * @param gaika 外貨
	 * @param rate レート
	 * @return 外貨単価（小数点以下は邦貨換算端数で処理）
	 */
	public BigDecimal calSashihikiTankaKaigai(BigDecimal gaika, BigDecimal rate) {
		if (null==gaika)
		{
			return null;
		}
		if (null==rate)
		{
			return gaika;
		}
		BigDecimal tankaDecimal = gaika.multiply(rate);
		String houkaKanzanHasuu = setting.houkaKansanHasuu();
		if("0".equals(houkaKanzanHasuu)) return tankaDecimal.setScale(0, RoundingMode.DOWN); //切り上げ
		if("1".equals(houkaKanzanHasuu)) return tankaDecimal.setScale(0, RoundingMode.UP); //切り捨て
		return tankaDecimal.setScale(0, RoundingMode.HALF_UP); //四捨五入
	}

	/** 定期区間の文字列 */ protected static final String KUKAN  = "【定期区間】";
	/** 定期区間の注釈 */ protected static final String KUKAN_WARN  = "※参照元起票者の定期区間のままです";
	/** 交通費明細を含む伝票区分 */ protected static final String[] DENPYOU_KBN_KOUTSUUHI  = {DENPYOU_KBN.KOUTSUUHI_SEISAN, DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI, DENPYOU_KBN.RYOHI_SEISAN, DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI, DENPYOU_KBN.KAIGAI_RYOHI_SEISAN};
	/** 海外日当を含む伝票区分 */ protected static final String[] DENPYOU_KBN_NITTOU  = {DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI, DENPYOU_KBN.RYOHI_SEISAN, DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI, DENPYOU_KBN.KAIGAI_RYOHI_SEISAN};
	/** 本体の使用者を含む伝票区分 */ protected static final String[] DENPYOU_KBN_HONTAISIYOUSHA = {DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI, DENPYOU_KBN.RYOHI_SEISAN, DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI, DENPYOU_KBN.KAIGAI_RYOHI_SEISAN};
	/** 明細の使用者を含む伝票区分 */ protected static final String[] DENPYOU_KBN_MEISAISIYOUSHA = {DENPYOU_KBN.KEIHI_TATEKAE_SEISAN};

	/**
	 * 他人の参照起票時の微調整
	 * @param dairiFlg 代理フラグ
	 * @param user ユーザ
	 * @param denpyouKbn 伝票区分
	 * @param hontai 本体
	 */
	public void adjustmentTashaCopy(String dairiFlg, User user, String denpyouKbn, GMap hontai) {
		if(!"1".equals(dairiFlg)) {
			adjustmentShiyousha(user, denpyouKbn, hontai);
		}
	}

	/**
	 * 他人の参照起票時の微調整
	 * @param dairiFlg 代理フラグ
	 * @param user ユーザ
	 * @param denpyouKbn 伝票区分
	 * @param meisaiList 明細
	 */
	public void adjustmentTashaCopy(String dairiFlg, User user, String denpyouKbn, List<GMap> meisaiList) {
		if(!"1".equals(dairiFlg)) {
			adjustmentTashaCopy_Koutsuuhi(denpyouKbn, meisaiList);
			adjustmentTashaCopy_Nittou(user, denpyouKbn, meisaiList);
			adjustmentShiyousha(user, denpyouKbn, meisaiList);
		}
	}

	/**
	 * 他人の参照起票時の微調整(交通費明細)
	 * @param denpyouKbn 伝票区分
	 * @param meisaiList 明細
	 */
	protected void adjustmentTashaCopy_Koutsuuhi(String denpyouKbn, List<GMap> meisaiList) {
		if(ArrayUtils.contains(DENPYOU_KBN_KOUTSUUHI, denpyouKbn)) {
			for(GMap meisai : meisaiList) {
				String shubetsuCd = meisai.get("shubetsu_cd");
				String naiyou = meisai.get("naiyou");
				String bikou = meisai.get("bikou");
				if(shubetsuCd == null || shubetsuCd.equals("1")) {
					if(naiyou.contains(KUKAN)) {
						naiyou = appendTeikikukanSanshouKihyouWarning(naiyou);
						meisai.put("naiyou", naiyou);
					}
					if(bikou.contains(KUKAN)) {
						bikou = appendTeikikukanSanshouKihyouWarning(bikou);
						meisai.put("bikou", bikou);
					}
				}
			}
		}
	}

	/**
	 * 他人の参照起票時の微調整(日当等明細)
	 * @param user ユーザ
	 * @param denpyouKbn 伝票区分
	 * @param meisaiList 明細
	 */
	protected void adjustmentTashaCopy_Nittou(User user, String denpyouKbn, List<GMap> meisaiList) {
		MasterKanriCategoryLogic mas = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);

		if(ArrayUtils.contains(DENPYOU_KBN_NITTOU, denpyouKbn)) {
			for(int i = meisaiList.size() - 1; i >= 0; i--) {
				GMap meisai = meisaiList.get(i);
				String shubetsuCd = meisai.get("shubetsu_cd");
				String kaigaiFlg = meisai.get("kaigai_flg");
				String shubetsu1 = meisai.get("shubetsu1");
				String shubetsu2 = meisai.get("shubetsu2");
				BigDecimal tanka = meisai.get("tanka");
				BigDecimal nissuu = meisai.get("nissuu");
				if(shubetsuCd.equals("2")) {
					GMap nittouMap = null;
					if(kaigaiFlg == null || !kaigaiFlg.equals("1")) {
						nittouMap = mas.loadNittouKingakuAndFlgAndKbn(shubetsu1, shubetsu2, getKihyouYakushokuCd(user.getSeigyoUserId(), null));
					}else {
						nittouMap = mas.loadKaigaiNittouKingaku(shubetsu1, shubetsu2, getKihyouYakushokuCd(user.getSeigyoUserId(), null));
					}
					if(nittouMap == null) {
						meisaiList.remove(i);
					}else {
						tanka = nittouMap.get("tanka");
						if(tanka != null) {
							BigDecimal kingaku = tanka.multiply(nissuu).setScale(0, RoundingMode.CEILING);
							meisai.put("tanka", tanka);
							meisai.put("meisai_kingaku", kingaku);
						}
					}
				}
			}
		}
	}

	/**
	 * 定期区間文字列に注記を足す
	 * @param org 元の文字列
	 * @return 注釈を足した文字列
	 */
	protected String appendTeikikukanSanshouKihyouWarning(String org) {
		String[] strs = org.replace("\r\n", "\n").split("\n");
		for(int i = 0; i < strs.length; i++) {
			if(strs[i].contains(KUKAN) && !strs[i].contains(KUKAN_WARN)) {
				strs[i] = strs[i] + "　" + KUKAN_WARN;
				break;
			}
		}
		return String.join("\r\n", strs);
	}

	/**
	 * 他人の参照起票時の微調整(使用者明細)
	 * @param user ユーザ
	 * @param denpyouKbn 伝票区分
	 * @param hontai 本体
	 */
	protected void adjustmentShiyousha(User user, String denpyouKbn, GMap hontai) {
		if(ArrayUtils.contains(DENPYOU_KBN_HONTAISIYOUSHA, denpyouKbn)) {
			hontai.put("user_id", user.getSeigyoUserId());
			hontai.put("shain_no", user.getShainNo());
			hontai.put("user_sei", user.getUserSei());
			hontai.put("user_mei", user.getUserMei());
		}
	}

	/**
	 * 他人の参照起票時の微調整(使用者明細)
	 * @param user ユーザ
	 * @param denpyouKbn 伝票区分
	 * @param meisaiList 明細
	 */
	protected void adjustmentShiyousha(User user, String denpyouKbn, List<GMap> meisaiList) {
		if(ArrayUtils.contains(DENPYOU_KBN_MEISAISIYOUSHA, denpyouKbn)) {
			for(GMap meisai : meisaiList) {
				meisai.put("user_id", user.getSeigyoUserId());
				meisai.put("shain_no", user.getShainNo());
				meisai.put("user_sei", user.getUserSei());
				meisai.put("user_mei", user.getUserMei());
			}
		}
	}
	
	/**
	 * 科目の処理グループ、消費税区分、按分法、課税区分から
	 * 課税区分・消費税率・分離区分・仕入区分の必須フラグを取得する
	 * </br>※(期別)消費税設定から消費税区分と按分フラグが必
	 * @param invoiceFlg インボイスフラグ
	 * @param shoriGrp 処理グループ
	 * @param shouhizeikbn 消費税区分
	 * @param kazeiKbn 課税区分
	 * @param shiireZeiAnbun 仕入税額按分フラグ
	 * @return index　0:課税区分　1：消費税率　2：分離区分　3：仕入区分
	 */
	public GMap getInvoiceKanrenDataFromKamoku(String invoiceFlg, int shoriGrp, int shouhizeikbn, String kazeiKbn, int shiireZeiAnbun) {
		GMap mp = new GMap();
		mp.put("kazeiHissu", "0");
		mp.put("zeiHissu", "0");
		mp.put("bunriHissu", "0");
		mp.put("shiireHissu", "0");
		
		if ("1".equals(invoiceFlg))
		{
			return mp;
		}
		//旧伝票なら必須チェックなし
		
		int shoriGroup = shoriGrp;

		String kazeiHissu = (shoriGroup == 1 || shoriGroup == 21 || shoriGroup == 22) ? "0" : "1";
		String zeiHissu = (shoriGroup == 21 || shoriGroup == 22 || (0 == shouhizeikbn && List.of("1", "11", "12").contains(kazeiKbn)) || List.of("1", "2", "11", "12", "13", "14").contains(kazeiKbn)) ? "1" : "0";
		String bunriHissu = (0 != shouhizeikbn && List.of("1", "2", "11", "12", "13", "14").contains(kazeiKbn)) ? "1" : "0";
		String shiireHissu = (0== shiireZeiAnbun && List.of(2, 5, 6, 7, 8, 10).contains(shoriGroup)) ? "1" : "0";
		
		mp.put("kazeiHissu",kazeiHissu);
		mp.put("zeiHissu",zeiHissu);
		mp.put("bunriHissu",bunriHissu);
		mp.put("shiireHissu",shiireHissu);
		
		return mp;
	}
	
	/**
	 * 入力方式に従って金額計算する ※請求書払い・支払依頼申請csv一括登録専用
	 * @param nyuryokuHoushiki 入力方式
	 * @param kingaku 金額 
	 * @param zeiritsu 税率
	 * @param jigyoushaKbn 事業者区分
	 * @param dto 期別消費税設定DTO
	 * @param shouhizeigaku 消費税額
	 * @return   計算結果GMap
	 */
	public GMap calcKingakuAndTaxForIkkatsu(String nyuryokuHoushiki, String kingaku, int zeiritsu, String jigyoushaKbn, KiShouhizeiSetting dto, String shouhizeigaku, String kazeiKbn) {
		
		int shiirezeigakuKeikasothi = dto.shiirezeigakuKeikasothi;
		int shiireZeigakuKeisan = dto.shiirezeigakuKeisan;
		String hasuuShoriFlg = Integer.toString(dto.hasuuShoriFlg);
		if(shiireZeigakuKeisan == 1 && "1".equals(hasuuShoriFlg)) {
			hasuuShoriFlg = setting.zeigakuHasuuHenkanFlg(); //会社設定から
		}
		//課税区分が税込系or税抜系以外の場合は強制的に消費税額を0円とする
		boolean kazeiTaishou = List.of("001", "002", "011", "012", "013", "014").contains(kazeiKbn);
		
		GMap mp = new GMap();
		
		BigDecimal jigyoushaKbnNum = (1 == shiirezeigakuKeikasothi || "0".equals(jigyoushaKbn))
				? new BigDecimal("1")
				: "1".equals(jigyoushaKbn)
					? new BigDecimal("0.8")
					: new BigDecimal("0.5"); // 掛け算なのでデフォは1
		BigDecimal bdKingaku = new BigDecimal(kingaku);
		BigDecimal bdZeiritsu = new BigDecimal(zeiritsu);
		BigDecimal bdBunbo = new BigDecimal(100 + zeiritsu);
		
		if("0".equals(nyuryokuHoushiki)) {
			//税込
			//支払金額から税抜金額と消費税額を計算する
			BigDecimal taxbase = bdKingaku.multiply(bdZeiritsu).divide(bdBunbo,10,RoundingMode.HALF_UP).multiply(jigyoushaKbnNum);
			BigDecimal tax = kazeiTaishou ? processShouhizeiFraction(hasuuShoriFlg, taxbase, true): BigDecimal.ZERO;
			BigDecimal hontai = bdKingaku.subtract(tax);
			
			mp.put("shiharaiKingaku",bdKingaku);
			mp.put("hontaiKingaku",hontai);
			mp.put("shouhizeigaku",tax);
		}else {
			//税抜
			//入力した税抜金額と入力した消費税額を足し算して支払金額を出す
			//措置フラグにかかわらず、csv一括登録では必ず消費税額が入力されるものとする
			BigDecimal tax = kazeiTaishou ? new BigDecimal(shouhizeigaku) : BigDecimal.ZERO;
			BigDecimal shiharai = bdKingaku.add(tax);
			mp.put("shiharaiKingaku",shiharai);
			mp.put("hontaiKingaku",bdKingaku);
			mp.put("shouhizeigaku",tax);
		}
		//mapに税込税抜消費税全部putすることで、呼出元側配列セットで入力方式のif文を不要にする
		return mp;
	}
	
	/** 消費税設定テーブルの端数処理フラグに従って端数計算する
	 * 0：切り捨て　1：切り上げ　2：四捨五入 
	 * @param hasuuKeisanFlg 端数計算フラグ（消費税設定テーブル）
	 * @param kingaku 金額
	 * @param isShouhizei 消費税計算か？
	 * @return 計算結果
	 */
	@Deprecated
	public double processFraction(String hasuuKeisanFlg, double kingaku, boolean isShouhizei) {
		if(!isShouhizei) {
			if("0".equals(hasuuKeisanFlg)) {
				hasuuKeisanFlg = "1";
			}
			else if("1".equals(hasuuKeisanFlg)) {
				hasuuKeisanFlg = "0";
			}
		}
		double keisango = 0;
		if("0".equals(hasuuKeisanFlg)) {
			keisango = Math.floor(kingaku);
		}
		if("1".equals(hasuuKeisanFlg)) {
			var kiriage = (kingaku - Math.floor(kingaku) < 0.1) ? Math.floor(kingaku) : Math.ceil(kingaku);
			keisango = kiriage;
		}
		if("2".equals(hasuuKeisanFlg)) {
			keisango = Math.round(kingaku);
		}
		return keisango;
	}
	
	/** 消費税設定テーブルの端数処理フラグに従って端数計算する
	 * 0：切り捨て　1：切り上げ　2：四捨五入 
	 * @param hasuuKeisanFlg 端数計算フラグ（消費税設定テーブル）
	 * @param kingaku 金額
	 * @param isShouhizei 消費税計算か？
	 * @return 計算結果
	 */
	public BigDecimal processShouhizeiFraction(String hasuuKeisanFlg, BigDecimal kingaku, boolean isShouhizei) {
		if(!isShouhizei) {
			if("0".equals(hasuuKeisanFlg)) {
				hasuuKeisanFlg = "1";
			}
			else if("1".equals(hasuuKeisanFlg)) {
				hasuuKeisanFlg = "0";
			}
		}
		BigDecimal ten = new BigDecimal("10");
		BigDecimal dotfive = new BigDecimal("0.5");
		BigDecimal keisango = kingaku.abs().multiply(ten).setScale(0, RoundingMode.DOWN);
		keisango = keisango.divide(ten);
		if("0".equals(hasuuKeisanFlg)) {
			if(BigDecimal.ZERO.compareTo(keisango) != 0){
				keisango = keisango.subtract(dotfive);
			}
		}
		if("1".equals(hasuuKeisanFlg)) {
			if(BigDecimal.ZERO.compareTo(keisango) != 0){
				keisango = keisango.add(new BigDecimal("0.4"));
			}
		}
		if(BigDecimal.ZERO.compareTo(kingaku) <= 0){
			keisango = keisango.add(dotfive).multiply(new BigDecimal(keisango.signum())).setScale(0, RoundingMode.DOWN);
		}else{
			keisango = keisango.add(dotfive).multiply(new BigDecimal(keisango.signum())).setScale(0, RoundingMode.DOWN).multiply(new BigDecimal("-1"));
		}
		return keisango;
	}
	
	/**
	 * 摘要バイト数チェック
	 * SIASの場合120、de3の場合60
	 * @param version バージョン（SIAS or de3）
	 * @return 摘要バイト数制限
	 */
	public int tekiyouCheck(String version) {
		return version.equals("SIAS") ? 120 : 60;
	}
	
	/**
	 * 枝番分離区分の取得
	 * @param kamokuCd 科目コード
	 * @param kamokuEdabanCd 科目枝番コード
	 * @param bunriKbn 科目及び伝票登録時点での分離区分コード
	 * @param kazeiKbn 課税区分
	 * @return 分離区分
	 */
	public String edabanBunriCheck(String kamokuCd,String kamokuEdabanCd, String bunriKbn, String kazeiKbn) {
		String bunri = bunriKbn;
		//分離区分設定不可の課税区分の場合には空白
		if(!List.of("001","002","011","012","013","014").contains(kazeiKbn)) {
			bunri = "9";
		}
		//枝番コードが空白でない場合、枝番マスタから分離区分を取得
		else if(!kamokuEdabanCd.equals("")) {
			edabanZandaka =  EteamContainer.getComponent(KamokuEdabanZandakaAbstractDao.class, connection);
			if(edabanZandaka.find(kamokuCd, kamokuEdabanCd).getBunriKbn() != null) {
				bunri = edabanZandaka.find(kamokuCd, kamokuEdabanCd).getBunriKbn().toString();
			}
		}
		return bunri;
	}
	
	/**
	 * 部門仕入の取得
	 * @param kamokuCd 科目コード
	 * @param futanBumonCd 負担部門コード
	 * @param kazeiKbn 課税区分
	 * @param shiireKbn 仕入区分
	 * @param shiireZeiAnbun 仕入税按分
	 * @return 分離区分
	 */
	public String bumonShiireCheck(String kamokuCd, String futanBumonCd, String kazeiKbn, String shiireKbn, int shiireZeiAnbun) {
		kamokuMasterDao = EteamContainer.getComponent(KamokuMasterDao.class, connection);
		bumonMasterDao = EteamContainer.getComponent(BumonMasterDao.class, connection);
		String shiire = shiireKbn;
		// 負担部門が存在する場合、部門仕入を確認
		if(futanBumonCd != null && !futanBumonCd.equals("")) {
			// 一旦仮払い伝票で登録されている負担部門の仕入区分をセット
			if(this.bumonMasterDao.find(futanBumonCd).shiireKbn != null) {
				shiire = this.bumonMasterDao.find(futanBumonCd).shiireKbn.toString();
			}
			// 処理グループ&課税区分、仕入税額按分を考慮し、使用不可なら0に戻す
			var	shoriGroup = this.kamokuMasterDao.find(kamokuCd).shoriGroup;
			if(!isValidShiireKbn(shoriGroup.toString(), kazeiKbn, shiire, shiireZeiAnbun)) {
				shiire = "0";
			}
		}
		return shiire;
	}
	
	//仕入税額控除経過措置割合が仕訳連携に必要なため、jspとxlsにある仮払い消費税のロジック必要になる？
}
