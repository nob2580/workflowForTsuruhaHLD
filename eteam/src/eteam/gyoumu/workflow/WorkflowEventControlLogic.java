package eteam.gyoumu.workflow;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.struts2.ServletActionContext;

import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamConst;
import eteam.common.EteamConst.UserId;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_JYOUTAI;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.JOUKYOU;
import eteam.common.EteamNaibuCodeSetting.KIHON_MODEL_CD;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.MAIL_TSUUCHI_KBN;
import eteam.common.EteamNaibuCodeSetting.SHOUNIN_NINZUU_CD;
import eteam.common.EteamNaibuCodeSetting.SHOUNIN_ROUTE_HENKOU_LEVEL;
import eteam.common.EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU;
import eteam.common.EteamSettingInfo;
import eteam.common.RegAccess;
import eteam.common.select.KaniTodokeCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.TsuuchiCategoryLogic;
import eteam.database.dao.ShouninRouteDao;
import eteam.database.dto.EbunshoFile;
import eteam.database.dto.TenpuFile;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import eteam.gyoumu.kihyounavi.DenpyouKanriLogic;
import eteam.gyoumu.user.User;
import eteam.gyoumu.yosanshikkoukanri.kogamen.KianTsuikaLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ワークフローイベント制御Logic(ワークフローのロジックが長くなったので各種イベント管理のにサブに移している)
 */
public class WorkflowEventControlLogic extends WorkflowEventControlLogicBase {

	/**
	 * 起案終了に関する定数クラス
	 */
	public class kianShuuryou {
		/** コメント（設定） */
		public static final String SETTEI = "起案終了の設定";
		/** コメント（解除） */
		public static final String KAIJO  = "起案終了の解除";
		/** コメント（状況） */
		public static final String JOUKYOU = "起案終了";
	}

	/**
	 * WF操作判定結果
	 */
	@Getter @Setter @ToString
	public class WfSeigyo {
		//WFのデータ
		/** 伝票ID */ String denpyouId;
		/** 伝票区分 */ String denpyouKbn;
		/** 参照伝票ID */ String sanshouDenpyouId;
		/** 伝票 */ GMap denpyou;
		/** 伝票状態 */ String denpyouJoutai = DENPYOU_JYOUTAI.MI_KIHYOU;
		/** マル秘フラグ */ boolean maruhiFlg;
		/** 承認ルート */ List<GMap>	routeList;//承認ルート + 承認ルート合議親 + 子のJOIN状態
		/** 承認ルート(起票者) */ GMap kihyouRoute;
		/** 承認ルート(ユーザー処理対象) */ GMap procRoute; //承認ルート OR 承認ルート合議子
		/** 承認権限(ユーザー処理対象) */ GMap procRouteKengen;
		/** 承認権限(ユーザー処理対象)が閲覧者である */ boolean etsuransha;
		/** 承認権限(ユーザー処理対象)の承認文言 */ String shouninMongon;
		/** ヴァージョン */ Integer version;

		//ユーザーの判定
		/** 起票者である */ boolean kihyousha;
		/** 現在承認者である */ boolean curShouninsha;
		/** 経理権限 */ boolean keiri;
		/** 他者の参照起票である */ boolean othersSanshouKihyou;

		//操作可否判定
		/** 登録 */ boolean touroku;
		/** 更新 */ boolean koushin;
		/** 承認ルート登録 */ boolean shouninRouteTouroku;
		/** マル秘設定 */ boolean maruhiSettei;
		/** マル秘解除 */ boolean maruhiKaijyo;
		/** 申請 */ boolean shinsei;
		/** 取下げ */ boolean torisage;
		/** 取戻し */ boolean torimodoshi;
		/** 承認 */ boolean shounin;
		/** 否認可否フラグ */ boolean hinin;
		/** 差戻し可否フラグ */ boolean sashimodoshi;
		/** 参照起票 */ boolean sanshouKihyou;
		/** ファイル添付 */ boolean fileTenpuRef;
		/** ファイル添付 */ boolean fileTenpuUpd;
		/** WEB印刷表示 */ boolean webInsatsu;
		/** e文書 */ boolean ebunsho;
		/** 仕訳データ変更(null/"de3"/"sias") */ String shiwakeDataHenkou;
		/** 起案確認 */ boolean kianCheck;
		/** 予算確認 */ boolean yosanCheck;
		/** 操作履歴表示 */ boolean sousaRirekiView;
		/** 承認解除可否 */ boolean shouninKaijo;
		/** 最終承認者編集可否 */ boolean denpyouHenkouKengen;
		/** 計上日の編集可否 */ boolean enableDenpyouHenkouKengenKeijou;
		/**	消費税額修正可否 */ String  				zeigakuShusei;
		/**	入力方式の初期値 */ boolean nyuryokuDefault;
		/**	入力方式の変更可否 */ boolean nyuryokuhenkou;
		/**	事業者区分の変更可否 */ boolean jigyoushaKbnHenkou;
		/**	インボイス制度開始設定 */ String invoiceStartCheck;
		/**	税額端数処理 */ String ZeigakuHasuuShoriFlg;
	}

	/**
	 * WFの各種処理可否判定
	 * @param user ユーザー
	 * @param denpyouId 伝票ID
	 * @param sanshouDenpyouId 参照起票伝票ID(必ずnullで渡して...)
	 * @return 判定結果
	 * @throws EteamAccessDeniedException アクセス不可能
	 * @throws EteamDataNotFoundException データなし（伝票 or 参照起票伝票）
	 */
	public WfSeigyo judgeWfSeigyo(User user, String denpyouId, String sanshouDenpyouId) {
		return judgeWfSeigyo(user, denpyouId.substring(7, 11), denpyouId, sanshouDenpyouId);
	}

	/**
	 * WFの各種処理可否判定
	 * @param user ユーザー
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID。未起票ならnull、参照起票ならnull。
	 * @param sanshouDenpyouId 参照起票伝票ID。参照起票以外ならnull。
	 * @return 判定結果
	 * @throws EteamAccessDeniedException アクセス不可能
	 * @throws EteamDataNotFoundException データなし（伝票 or 参照起票伝票）
	 */
	public WfSeigyo judgeWfSeigyo(User user, String denpyouKbn, String denpyouId, String sanshouDenpyouId) {
		KaikeiCommonLogic kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, super.connection);
		KaniTodokeCategoryLogic kaniLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, super.connection);
		DenpyouKanriLogic dkl = EteamContainer.getComponent(DenpyouKanriLogic.class, connection);

		WfSeigyo w = new WfSeigyo();
		w.denpyouId = denpyouId;
		w.denpyouKbn = denpyouKbn;
		w.sanshouDenpyouId = sanshouDenpyouId;

		//伝票情報取得
		if (StringUtils.isNotEmpty(denpyouId)) {
			w.denpyou = wfLogic.selectDenpyou(denpyouId);
			if(w.denpyou == null) throw new EteamDataNotFoundException();
			w.denpyouJoutai = w.denpyou.get("denpyou_joutai").toString();
			w.maruhiFlg = w.denpyou.get("maruhi_flg").equals("1");
			w.routeList = wfLogic.selectShouninRoute(denpyouId);
			w.kihyouRoute = w.routeList.get(0);
			w.procRoute = wfLogic.findProcRoute(w.routeList, user);
			if(isKaniTodoke(denpyouKbn)){
				w.version = kaniLogic.findVersion(denpyouId);
			}
		}

		//アクセス権限チェック
		if (! judgeAccess(user, w)){
			// 合議設定SQLのあり・なしで差分を取り、差分があったら合議部署の最終決裁後の閲覧制限によるエラー
			if (setting.etsuranSeigenGougi().equals("0") && judgeAccessGougiSetteiNashi(user, w)) {
				throw new EteamAccessDeniedException("最終決裁されているので、閲覧できません。");
			} else {
				throw new EteamAccessDeniedException();
			}
		}

		// 起票時、伝票種別が有効期限切れであった場合はエラー
		if(StringUtils.isEmpty(denpyouId)) checkDenpyouShubetsuYuukouKigen(w);

		//該当ユーザーが承認ルート上にいるかチェック
		String sql = "SELECT COUNT(*) count FROM denpyou d INNER JOIN shounin_route r ON (r.denpyou_id,r.edano)=(d.denpyou_id,1) WHERE d.denpyou_id=? ";
		List<Object> params = new ArrayList<>();
		params.add(denpyouId);
		if(user.getGyoumuRoleId() == null){
			//承認ルートに自分がいる または 自分が被代理起票者
			sql += (" AND(");
			sql += ("       (EXISTS (SELECT * FROM shounin_route tmp_r WHERE  tmp_r.denpyou_id = d.denpyou_id AND  tmp_r.user_id=? ))");
			sql += ("    OR (EXISTS (SELECT * FROM shounin_route_gougi_ko tmp_gk WHERE tmp_gk.denpyou_id = d.denpyou_id AND tmp_gk.user_id=? ))");
			sql += (" ) ");
			params.add(user.getSeigyoUserId());
			params.add(user.getSeigyoUserId());
		}
		boolean existOnRoute = ((long)connection.find(sql, params.toArray()).get("count") > 0);

		//マル秘文書制御
		if(w.maruhiFlg){
			boolean sanshoEnable = false;
			//「adminユーザー」「承認ルート上のユーザー」「業務ロールかつログイン中ユーザーがマル秘文書参照権限あり」なら参照可能
			if(        UserId.ADMIN.equals(user.getLoginUserId())
				|| (   isEmpty(user.getGyoumuRoleId()) && existOnRoute )
				|| ( !(isEmpty(user.getGyoumuRoleId())) && user.isMaruhiKengenFlg() )
				){
				sanshoEnable = true;
			}
			//「経理処理ロールで、下に参照権限のある支出依頼起案が添付されている場合」も参照可能
			if( !sanshoEnable && kaikeiCommonLogic.userIsKeiri(user) && setting.keiriMaruhiRef().equals("1") ){
				List<String> bottomIdList = wfLogic.loadBottomKianDenpyouId(w.denpyouId);
				for(String bottomId : bottomIdList){
					try{
						judgeWfSeigyo(user, bottomId.substring(7,11), bottomId, null);
						String yosanShikkouTaishouCd = dkl.getYosanShikkouTaishou(bottomId.substring(7,11));
						if(yosanShikkouTaishouCd.equals(YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI)){
							sanshoEnable = true;
							break;
						}
					}catch(EteamAccessDeniedException e){}
				}
			}
			if(!sanshoEnable){
				throw new EteamAccessDeniedException("マル秘の為、参照できません。", true);
			}
		}

		//ユーザー判定
		w.kihyousha = judgeKihyousha(user, w);
		w.curShouninsha = judgeCurShouninsha(user, w);
		w.keiri = kaikeiCommonLogic.userIsKeiri(user);
		if(w.curShouninsha){
			if(w.procRoute.get("shounin_shori_kengen_no") != null){
				w.procRouteKengen = wfLogic.findShouninShoriKengen((long)w.procRoute.get("shounin_shori_kengen_no"));
				w.shouninMongon = (String)w.procRouteKengen.get("shounin_mongon");
				w.etsuransha = w.procRoute.get("kihon_model_cd") != null && w.procRoute.get("kihon_model_cd").equals(KIHON_MODEL_CD.ETSURAN);
			}else{
				w.shouninMongon = "承認";
			}
		}

		//WF操作判定
		w.touroku = judgeTouroku(user, w);
		w.koushin = judgeKoushin(user ,w);
		w.shouninRouteTouroku = judgeShoninRouteTouroku(user, w);
		w.shinsei = judgeShinsei(user, w);
		w.torisage = judgeTorisage(user, w);
		w.torimodoshi = judgeTorimodoshi(user, w);
		w.shounin = judgeShounin(user, w);
		w.hinin = judgeHinin(user, w);
		w.sashimodoshi = judgeSashimodoshi(user, w);
		w.sanshouKihyou = judgeSanshouKihyou(user, w);
		/*202207 ここに承認解除が入る*/
		w.shouninKaijo = judgeShouninKaijo(user, w);
		w.denpyouHenkouKengen = judgeDenpyouHenkouKengen(user, w);
		w.enableDenpyouHenkouKengenKeijou = judgeEnableDenpyouHenkouKengenKeijou(user, w);
		w.zeigakuShusei = judgeZeigakuShusei(denpyouKbn);
		w.nyuryokuDefault = judgeNyuryokuDefault(denpyouKbn);
		w.nyuryokuhenkou = judgeNyuryokuHoushiki(denpyouKbn);
		w.jigyoushaKbnHenkou = judgeJigyoushaKbnHenkou(denpyouKbn);
		w.invoiceStartCheck = judgeinvoiceStart();
		w.ZeigakuHasuuShoriFlg = judgeHasuuKeisan();

		//承認関連

		//仕訳の紐付き判定
		w.shiwakeDataHenkou = judgeShiwakeDataHenkou(w);

		//添付ファイル
		w.fileTenpuRef = judgeFileTenpuRef(user, w);
		w.fileTenpuUpd = judgeFileTenpuUpd(user, w);
		w.ebunsho = isUseEbunsho(denpyouKbn);

		//WEB印刷表示
		w.webInsatsu = judgeWebInsatsu(user, w);

		//予算執行対象判定
		w.kianCheck = judgeKian(w, user);
		w.yosanCheck = judgeYosan(w, user);

		//マル秘
		w.maruhiSettei = judgeMaruhiSettei(user, w, true,  existOnRoute);
		w.maruhiKaijyo = judgeMaruhiSettei(user, w, false, existOnRoute);

		//操作履歴
		w.sousaRirekiView = systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.SOUSA_RIREKI);
		return w;
	}

	/**
	 * 参照権限の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 参照権限
	 */
	protected boolean judgeAccess(User user, WfSeigyo w) {
		TsuuchiCategoryLogic tLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);

		boolean access = false;
		//未起票状態での操作
		if (StringUtils.isEmpty(w.denpyouId)) {
			//一般ユーザーで有効所属部門がある場合のみ
			if (null == user.getGyoumuRoleId() && 0 < user.getBumonCd().length) {
				access = true;
			}
			//ただし参照起票なら元の起票者の場合のみ
			if (StringUtils.isNotEmpty(w.sanshouDenpyouId)) {
				GMap sanshouKihyousha = wfLogic.selectKihyoushaData(w.sanshouDenpyouId);
				if (sanshouKihyousha == null) throw new EteamDataNotFoundException();//これにひっかかることはないはず
				String orgKihyouUserId = sanshouKihyousha.get("user_id").toString();
				if( ! user.getSeigyoUserId().equals(orgKihyouUserId)) {
					access = false;
					//設定により自部門他ユーザの参照起票が可能
					if(setting.shozokuBumonDataKyoyu().equals("1")) {
						if(judgeShozokuBumonKyoyu(user, w.sanshouDenpyouId)) {
							access = true;
						}
					}
				}
			}
		//起票済状態での操作
		} else {
			String sql = "SELECT COUNT(*) count FROM denpyou d INNER JOIN shounin_route r ON (r.denpyou_id,r.edano)=(d.denpyou_id,1) WHERE d.denpyou_id=? ";
			List<Object> params = new ArrayList<>();
			params.add(w.denpyouId);
			sql += tLogic.makeAccessWhere(params, user, "d", "r", true);
			if((long)connection.find(sql, params.toArray()).get("count") >= 1){
				access = true;
			}
		}
		return access;
	}

	/**
	 * 参照権限の判定　(合議部署の最終決裁後の閲覧制限設定なし)
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 参照権限
	 */
	protected boolean judgeAccessGougiSetteiNashi(User user, WfSeigyo w) {
		TsuuchiCategoryLogic tLogic = EteamContainer.getComponent(TsuuchiCategoryLogic.class, connection);

		boolean access = false;
		//未起票状態での操作
		if (StringUtils.isEmpty(w.denpyouId)) {
			//一般ユーザーで有効所属部門がある場合のみ
			if (null == user.getGyoumuRoleId() && 0 < user.getBumonCd().length) {
				access = false;
			}
			//ただし参照起票なら元の起票者の場合のみ
			if (StringUtils.isNotEmpty(w.sanshouDenpyouId)) {
				GMap sanshouKihyousha = wfLogic.selectKihyoushaData(w.sanshouDenpyouId);
				if (sanshouKihyousha == null) throw new EteamDataNotFoundException();
				String orgKihyouUserId = sanshouKihyousha.get("user_id").toString();
				if( ! user.getSeigyoUserId().equals(orgKihyouUserId)) {
					access = false;
				}
			}
		//起票済状態での操作
		} else {
			String sql = "SELECT COUNT(*) count FROM denpyou d INNER JOIN shounin_route r ON (r.denpyou_id,r.edano)=(d.denpyou_id,1) WHERE d.denpyou_id=? ";
			List<Object> params = new ArrayList<>();
			params.add(w.denpyouId);
			sql += tLogic.makeAccessWhere(params, user, "d", "r", false);
			if((long)connection.find(sql, params.toArray()).get("count") >= 1){
				access = true;
			}
		}
		return access;
	}

	/**
	 * 起票者の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 起票者である
	 */
	protected boolean judgeKihyousha(User user, WfSeigyo w) {
		ShouninRouteDao sr = EteamContainer.getComponent(ShouninRouteDao.class, connection);

		//未起票状態での操作
		if (StringUtils.isEmpty(w.denpyouId)) {
			//一般ユーザーで有効所属部門がある場合のみ
			if (null == user.getGyoumuRoleId() && 0 < user.getBumonCd().length) {
				if(!isEmpty(w.sanshouDenpyouId)) {
					if(!sr.find(w.sanshouDenpyouId, 1).userId.equals(user.getSeigyoUserId())) {
						w.othersSanshouKihyou = true;
					}
				}
				return true;
			}
		//起票済状態での操作
		} else {
			//業務ロールでログインしている
			if (StringUtils.isNotEmpty(user.getGyoumuRoleId())) {
			//一般ユーザーでログインしている
			} else {
				//起票者判定
				if (user.getSeigyoUserId().equals(w.kihyouRoute.get("user_id"))) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 現在承認可能者の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 現在承認可能者である
	 */
	protected boolean judgeCurShouninsha(User user, WfSeigyo w) {
		//起票済・申請中かつ、自己承認可能又はログインユーザーが登録した伝票ではない
		//もしくはロールとしてのログインであること
		return !isEmpty(w.denpyouId)
				&& DENPYOU_JYOUTAI.SHINSEI_CHUU.equals(w.denpyouJoutai)
				&& w.procRoute != null
				&& (!user.getLoginUserId().equals(w.kihyouRoute.get("touroku_user_id"))
						|| this.systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.DAIKOUSHA_JIKO_SHOUNIN)
						|| StringUtils.isNotEmpty(user.getGyoumuRoleId()));
	}
	/**
	 * 登録可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeTouroku(User user, WfSeigyo w) {
		return
				w.denpyouJoutai.equals(DENPYOU_JYOUTAI.MI_KIHYOU) &&
				w.kihyousha;
	}
	/**
	 * 更新可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeKoushin(User user, WfSeigyo w) {
		if(w.denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU) && w.kihyousha){
			return true;
		}
		if(
			w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SHINSEI_CHUU) &&
			w.curShouninsha &&
			(w.procRoute.get("isJoui") != null || "1".equals(w.procRoute.get("genzai_flg")) || "1".equals(w.procRoute.get("gougi_genzai_flg"))) &&
			(!isEmpty(user.getGyoumuRoleId()) || (w.procRoute.get("henkou_flg").equals("1")))
		){
			return true;
		}
		return false;
	}
	/**
	 * 承認ルート登録可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeShoninRouteTouroku(User user, WfSeigyo w) {
		if(user.getShouninRouteHenkouLevel().compareTo(SHOUNIN_ROUTE_HENKOU_LEVEL.DISABLED) > 0){
			switch(w.denpyouJoutai){
			case DENPYOU_JYOUTAI.KIHYOU_CHUU:
				if(systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.SHOUNIN_ROUTE_HENKOU_SHINSEISHA)){
					//起票者として変更可能
					if(w.kihyousha){
						return true;
					}
				}
				break;
			case DENPYOU_JYOUTAI.SHINSEI_CHUU:
				if(systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.SHOUNIN_ROUTE_HENKOU_SHOUNINSHA)){
					//承認者として変更可能
					if(w.curShouninsha){
						//ただし後伺いユーザなら変更不可
						if( w.procRoute != null && w.procRoute.get("isAtoukagai") != null && (boolean)(w.procRoute.get("isAtoukagai")) == true ){
							return false;
						}else{
							return true;
						}
					}
					//現在承認者の上位承認者として変更可能
					if(wfLogic.findJouiRoute(w.routeList, user) != null){
						return true;
					}
				}
				break;
			}
		}
		return false;
	}

	/**
	 * 申請可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeShinsei(User user, WfSeigyo w) {
		return
			w.denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU) &&
			w.kihyousha &&
			(1 < w.routeList.size());
	}
	/**
	 * 取下げ可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeTorisage(User user, WfSeigyo w) {
		return
			w.denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU) &&
			w.kihyousha &&
			systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.WORKFLOW_TORISAGE);
	}
	/**
	 * 取戻し可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeTorimodoshi(User user, WfSeigyo w) {
		if(
			w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SHINSEI_CHUU) &&
			w.kihyousha && systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.WORKFLOW_TORIMODOSHI)){
			if(systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.SHOUNINGO_TORIMODOSHI)){
				return true;
			}
			GMap curRoute = wfLogic.findShouninRouteCur(w.denpyouId);
			int curEdaNo = (curRoute == null) ? 0 : (int)curRoute.get("edano");
			if(curEdaNo == 2){
				return true;
			}
		}
		return false;
	}
	/**
	 * 承認可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeShounin(User user, WfSeigyo w) {
		return
			w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SHINSEI_CHUU) &&
			w.curShouninsha;
	}
	/**
	 * 更新可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeHinin(User user, WfSeigyo w) {
		return
			w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SHINSEI_CHUU) &&
			w.curShouninsha &&
			(w.procRoute.get("isJoui") != null || "1".equals(w.procRoute.get("genzai_flg")) || "1".equals(w.procRoute.get("gougi_genzai_flg"))) &&
			(isEmpty(w.procRoute.get("kihon_model_cd")) || w.procRoute.get("kihon_model_cd").equals(KIHON_MODEL_CD.SHOUNI)) &&
			systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.WORKFLOW_HININ);
	}
	/**
	 * 差戻し可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeSashimodoshi(User user, WfSeigyo w) {
		return
			w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SHINSEI_CHUU) &&
			w.curShouninsha &&
			(w.procRoute.get("isJoui") != null || "1".equals(w.procRoute.get("genzai_flg")) || "1".equals(w.procRoute.get("gougi_genzai_flg"))) &&
			(isEmpty(w.procRoute.get("kihon_model_cd")) || w.procRoute.get("kihon_model_cd").equals(KIHON_MODEL_CD.SHOUNI)) &&
			systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.WORKFLOW_SASHIMODOSHI);
	}
	/**
	 * 参照起票可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeSanshouKihyou(User user, WfSeigyo w) {
		//伝票区分をキーに現在日付時点で有効な伝票種別を検索する。ヒットしなければ参照起票ボタンは表示しない。
		GMap denpyouShubetsu = kihyouLogic.findValidDenpyouKanri(w.denpyouKbn);
		if (denpyouShubetsu == null)
		{
			return false;
		}
		return
			!w.denpyouJoutai.equals(DENPYOU_JYOUTAI.MI_KIHYOU) &&
			(
				(w.kihyousha) ||
				//設定により自部門他ユーザの参照起票が可能
				(setting.shozokuBumonDataKyoyu().equals("1") && judgeShozokuBumonKyoyu(user, w.denpyouId)));
	}
	
	/*202207 115594*/
	/**
	 * 承認解除可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可否
	 */
	protected boolean judgeShouninKaijo(User user, WfSeigyo w) {
		if (w.denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU))
		{
			return false;
		}
		GMap mp = findSaishuShouninSha(w.denpyouId);
		if (mp == null)
		{
			return false;
		}
		String roleId = mp.get("gyoumu_role_id").toString();
		if (user.getGyoumuRoleId() == null)
		{
			return false;
		}
		boolean a = 
				user.getGyoumuRoleId().equals(roleId) &&
				(w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SYOUNIN_ZUMI) || (w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SHINSEI_CHUU) && mp.get("joukyou_edano") != null)) &&
				setting.saishuushouninKaijoFlg().equals("1");
// boolean a = 
// user.getGyoumuRoleId().equals(roleId) &&
// w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SYOUNIN_ZUMI) &&
// setting.saishuushouninKaijoFlg().equals("1") &&
// !wfLogic.loadShiwakezumi(w.denpyouId) &&
// !wfLogic.loadKianDenpyouHImoduki(w.denpyouId) &&
// !wfLogic.loadTaDenpyouHImoduki(w.denpyouId) &&
// !wfLogic.loadKaribaraiSeisanHImoduki(w.denpyouId);
		return a;
	}
	/**
	 * 最終承認者の編集可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return true:修正不可　false:修正可（伝票状態に従う）
	 */
	protected boolean judgeDenpyouHenkouKengen(User user, WfSeigyo w) {
		// 起票中なら関係なし　承認済みも関係なし
		if(w.denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU) || w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SYOUNIN_ZUMI)) {
			return false;
		}
		//TODO 最終承認フラグをチェックするようになって、このmpは必要なくなる？　
		GMap mp = findSaishuShouninSha(w.denpyouId); // 元々簡易届だけ分けていたところ、調べたら意味がなかったので統一
		//最終承認者・注記文言設定には二人以上登録されているかもしれない
		if (user.getGyoumuRoleId() == null)
		{
			return false;
		}
		//最終承認者リスト確認のため、ログインユーザーのロールIDの有無の確認を先にしておきたい　20220927
		boolean chek = false;
		List<GMap> routeSaishuuShouninshaList = findSaishuShouninShaFlg(w.denpyouId);
		for(GMap map : routeSaishuuShouninshaList) {
			String rlid = map.get("gyoumu_role_id").toString();
			//承認者ルートの最終承認フラグが立ってる業務ロールと自分のロールが一致したら不可
			chek = user.getGyoumuRoleId().equals(rlid);
			if (chek)
			{
				break;
			}
		}
		
		return mp != null // 最終承認者が存在し（＝承認ルートが登録されており、かつ起票中以降としてDBに登録されている伝票で）
				&& setting.denpyouHenkouFlg().equals("1") // 会社設定で最終承認者が変更不可で
// && user.getGyoumuRoleId() != null // ロールIDがnullではなく
// && user.getGyoumuRoleId().equals(mp.get("gyoumu_role_id")); // 最終承認者のロールIDである
				&& chek;
	}
	/**
	 * 計上日の修正可否　※judgeDenpyouHenkouKengenとtrue/falseの意味が逆になっている
	 * @param user ユーザー
	 * @param w WF制御
	 * @return true:修正可　false:修正不可
	 */
	public boolean judgeEnableDenpyouHenkouKengenKeijou(User user, WfSeigyo w) {
		if (!w.denpyouHenkouKengen)
		{
			return true;
		}
		boolean hasseiShugi;
		boolean shinseishaKeijoubiInput;
// boolean enableInput = w.touroku || w.koushin;
		boolean enableInput = judgeDenpyouHenkouKengen(user,w);
		if (!enableInput)
		{
			enableInput = w.touroku || w.koushin;
		}
		KaikeiCommonLogic kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, super.connection);
		switch(w.denpyouKbn) {
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
			hasseiShugi = !"3".equals(setting.shiwakeSakuseiHouhouA001());
			shinseishaKeijoubiInput = setting.keijoubiDefaultA001().equals("3");
			break;
		case DENPYOU_KBN.RYOHI_SEISAN:
			hasseiShugi = !"3".equals(setting.shiwakeSakuseiHouhouA004());
			shinseishaKeijoubiInput = setting.keijoubiDefaultA004().equals("3");
			//EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.KEIJOUBI_DEFAULT_A004).equals("3");
			break;
		case DENPYOU_KBN.KOUTSUUHI_SEISAN:
			hasseiShugi = !"3".equals(setting.shiwakeSakuseiHouhouA010());
			shinseishaKeijoubiInput = setting.keijoubiDefaultA010().equals("3");
			break;
		case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
			hasseiShugi = !"3".equals(setting.shiwakeSakuseiHouhouA009());
			shinseishaKeijoubiInput = setting.jidouhikiKeijouNyuurohyku().equals("1");
			break;
		case DENPYOU_KBN.KARIBARAI_SHINSEI:
		case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
		case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
		case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI:
			return false;
		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			hasseiShugi = !"3".equals(setting.shiwakeSakuseiHouhouA011());
			shinseishaKeijoubiInput = setting.keijoubiDefaultA011().equals("3");
			break;
		case DENPYOU_KBN.SEIKYUUSHO_BARAI:
			int keijouBiMode;
			int shiharaiBiMode = kaikeiCommonLogic.judgeShiharaiBiMode(w.denpyouId, user);

			if(setting.seikyuuKeijouNyuurohyku().equals("1")) {
				keijouBiMode = 2;
				if (!enableInput) {
					keijouBiMode = 1;
				}
			}else {
				keijouBiMode = shiharaiBiMode;
			}
			return keijouBiMode == 1 ? true : false;
		case DENPYOU_KBN.SIHARAIIRAI:
			return true;
		case DENPYOU_KBN.FURIKAE_DENPYOU:
		case DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU:
			return false;
		default:
			return true;
		}
		int mode = kaikeiCommonLogic.judgeKeijouBiMode(hasseiShugi, shinseishaKeijoubiInput, w.denpyouId, user, !enableInput, setting.keijouNyuuryoku());
		if(mode == 0 || mode == 2) {
			return false;
		}else {
			return true;
		}
		//if(mode == 1 || mode == 3) return true;
	}
	
	/**
	 * 添付ファイル参照可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeFileTenpuRef(User user, WfSeigyo w) {
		//機能制御(ファイル添付)が有効なら添付欄利用可能
		//ただしe文書が有効ならば機能制御がOFFでもその伝票区分に限り添付欄利用可能
		if (systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.FILE_TENPU) || w.ebunsho) {
			return true;
		}
		//既にデータがあるなら参照だけは許容
		if (! isEmpty(w.denpyouId) && wfLogic.selectTenpuFile(w.denpyouId).size() > 0) {
			return true;
		}
		return false;
	}
	/**
	 * 添付ファイル更新可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeFileTenpuUpd(User user, WfSeigyo w) {
		return
			(w.touroku || w.koushin) &&
			w.fileTenpuRef;
	}
	/**
	 * WEB印刷ボタン表示可否の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeWebInsatsu(User user, WfSeigyo w){
		return systemLogic.judgeKinouSeigyoON(KINOU_SEIGYO_CD.WEB_INSATSU_BUTTON);
	}
	/**
	 * 仕訳データ変更の可否判定
	 * @param w WF制御
	 * @return de3(可:de3画面)、sias(可:SIAS画面)、null(不可)
	 */
	protected String judgeShiwakeDataHenkou(WfSeigyo w) {
		if (w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SYOUNIN_ZUMI) && w.denpyou.get("chuushutsu_zumi_flg").equals("1") && w.keiri) {
			if (kaikeiLogic.hasShiwakeDe3(w.denpyouId)) {
				return "de3";
			} else if (kaikeiLogic.hasShiwakeSias(w.denpyouId)) {
				return "sias";
			}
		}
		return null;
	}
	/**
	 * 起案チェック可否の判定<br>
	 * 下記の条件を満たす場合にチェック可と判断する。<br>
	 * <ul>
	 * <li>予算執行オプションが 予算執行管理Aあり(A) である。
	 * <li>伝票の「予算執行対象」が 支出起案である。</li>
	 * <li>もしくは、伝票の「予算執行対象」が 支出依頼 でかつ、起案伝票を添付している（起案伝票を紐付けている）。</li>
	 * <li>伝票の状態が 起票中 もしくは 申請中である。</li>
	 * <li>現作業者が合議メンバー以外である。</li>
	 * <li>ただし該当ユーザが経理の業務ロールを持つユーザである場合は合議内のみのメンバーでも参照可能とする。</li>
	 * </ul>
	 *
	 * @param w WF制御
	 * @param user ユーザー
	 * @return 可
	 */
	protected boolean judgeKian(WfSeigyo w, User user) {
		boolean isKianCheckTaishou = false;

		// 会社設定の「合議部署の予算確認設定」により合議メンバーの参照可否を設定する。
		boolean isNotGougi = true;
		if ("0".equals(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.GOUGI_YOSAN_KAKUNIN))){
			// 0 の場合、現作業者が合議内のみのメンバー以外であれば true を指定する。
			isNotGougi = (null == wfLogic.findRouteGougiShouninsha(w.denpyouId, user));

			//ただし該当ユーザが経理の業務ロールを持つユーザである場合は合議内のみのメンバーでも参照可能とする
			if(isNotGougi == false){
				if(bumonUserLogic.haveGyoumuRole(user,EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_CD.KEIRI_SHORI)){
					isNotGougi = true;
				}
			}

		}

		// 予算執行オプションを取得する。
		boolean yosannShikkouAOPOn = RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption());
		// 伝票の予算執行対象を取得する。
		GMap denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(w.denpyouKbn);
		String yosanShikkouTaishou = denpyouShubetsuMap.get("yosan_shikkou_taishou").toString();
		// 伝票起案紐付けレコードを取得する。
		GMap denpyouKianMap = selectDenpyouKianHimozuke(w.denpyouId);

		if (null != denpyouKianMap){
			// 伝票の「予算執行対象」が 支出依頼 でかつ、起案伝票を添付していないか確認する。
			// true:支出依頼で実施起案番号の設定あり false:それ以外
			boolean isShishutsuIraiKianAri = false;
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)){
				isShishutsuIraiKianAri = !super.isEmpty(denpyouKianMap.get("jisshi_kian_bangou"));
			}

			// 起案確認の対象伝票かどうか判別する。
			//   予算執行オプションが 予算執行管理Aあり(A)
			//   伝票が 支出起案 もしくは、伝票が 支出依頼 でかつ、実施起案番号の設定ある
			if (yosannShikkouAOPOn
			 && (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(yosanShikkouTaishou) || isShishutsuIraiKianAri)) {
				isKianCheckTaishou = true;
			}
		}

		// 伝票が未起票以外でかつ、起案確認の対象伝票かつ、現作業者が合議メンバー以外の場合に true を戻す。
		return
			(w.denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU) || w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SHINSEI_CHUU) ||
			 w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SYOUNIN_ZUMI) || w.denpyouJoutai.equals(DENPYOU_JYOUTAI.HININ_ZUMI) || w.denpyouJoutai.equals(DENPYOU_JYOUTAI.TORISAGE_ZUMI)) &&
			isKianCheckTaishou &&
			isNotGougi;
	}

	/**
	 * 予算チェック可否の判定<br>
	 * 下記の条件を満たす場合にチェック可と判断する。<br>
	 * <ul>
	 * <li>予算執行オプションが 予算執行管理Aあり(A) である。
	 * <li>伝票の「予算執行対象」が 実施起案である。</li>
	 * <li>もしくは、伝票の「予算執行対象」が 支出依頼 でかつ、起案伝票を添付していない（起案伝票を紐付けていない）。</li>
	 * <li>伝票の状態が 起票中 もしくは 申請中である。</li>
	 * <li>現作業者が合議メンバー以外である。</li>
	 * <li>ただし該当ユーザが経理の業務ロールを持つユーザである場合は合議内のみのメンバーでも参照可能とする。</li>
	 * </ul>
	 *
	 * @param user ユーザー
	 * @param w WF制御
	 * @return 可
	 */
	protected boolean judgeYosan(WfSeigyo w, User user) {
		boolean isYosanCheckTaishou = false;

		// 会社設定の「合議部署の予算確認設定」により合議メンバーの参照可否を設定する。
		boolean isNotGougi = true;
		if ("0".equals(EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.GOUGI_YOSAN_KAKUNIN))){
			// 0 の場合、現作業者が合議内のみのメンバー以外であれば true を指定する。
			isNotGougi = (null == wfLogic.findRouteGougiShouninsha(w.denpyouId, user));

			//ただし該当ユーザが経理の業務ロールを持つユーザである場合は合議内のみのメンバーでも参照可能とする
			if(isNotGougi == false){
				if(bumonUserLogic.haveGyoumuRole(user,EteamNaibuCodeSetting.GYOUMU_ROLE_KINOU_SEIGYO_CD.KEIRI_SHORI)){
					isNotGougi = true;
				}
			}

		}

		// 予算執行オプションを取得する。
		boolean yosannShikkouAOPOn = RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(RegAccess.checkEnableYosanShikkouOption());
		// 伝票の予算執行対象を取得する。
		GMap denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(w.denpyouKbn);
		String yosanShikkouTaishou = denpyouShubetsuMap.get("yosan_shikkou_taishou").toString();
		// 伝票起案紐付けレコードを取得する。
		GMap denpyouKianMap = selectDenpyouKianHimozuke(w.denpyouId);

		if (null != denpyouKianMap){
			// 伝票の「予算執行対象」が 支出依頼 でかつ、起案伝票を添付していないか確認する。
			// true:支出依頼で実施起案番号の設定あり false:それ以外
			boolean isShishutsuIraiKianNashi = false;
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)){
				isShishutsuIraiKianNashi = super.isEmpty(denpyouKianMap.get("jisshi_kian_bangou"));
			}

			// 起案確認の対象伝票かどうか判定判別する
			//   予算執行オプションが 予算執行管理Aあり(A)
			//   伝票が 実施起案 もしくは、伝票が 支出依頼 でかつ、起案伝票を添付していない
			if (yosannShikkouAOPOn
			 && (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(yosanShikkouTaishou) || isShishutsuIraiKianNashi)) {
				isYosanCheckTaishou = true;
			}
		}

		// 伝票が未起票以外でかつ、起案確認の対象伝票かつ、現作業者が合議メンバー以外の場合に true を戻す。
		return
			(w.denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU) || w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SHINSEI_CHUU) ||
			 w.denpyouJoutai.equals(DENPYOU_JYOUTAI.SYOUNIN_ZUMI) || w.denpyouJoutai.equals(DENPYOU_JYOUTAI.HININ_ZUMI) || w.denpyouJoutai.equals(DENPYOU_JYOUTAI.TORISAGE_ZUMI)) &&
			isYosanCheckTaishou &&
			isNotGougi;
	}

	/**
	 * マル秘設定の判定
	 * @param user ユーザー
	 * @param w WF制御
	 * @param isMaruhiSet true:マル秘設定 false:マル秘解除
	 * @param existOnRoute 該当ユーザーが承認ルート上に存在するか
	 * @return 可
	 */
	protected boolean judgeMaruhiSettei(User user, WfSeigyo w, boolean isMaruhiSet, boolean existOnRoute) {

		//未起票状態ならfalse
		if (w.denpyouJoutai.equals(DENPYOU_JYOUTAI.MI_KIHYOU))
		{
			return false;
		}

		//ユーザーにマル秘設定・解除権限が無ければfalse
		if (  (isMaruhiSet) && !(user.isMaruhiSetteiFlg()))
		{
			return false;
		}
		if ( !(isMaruhiSet) && !(user.isMaruhiKaijyoFlg()))
		{
			return false;
		}

		//伝票がマル秘設定時にマル秘設定しようとするか、マル秘未設定でマル秘解除しようとするならfalse
		if (w.maruhiFlg == isMaruhiSet)
		{
			return false;
		}

		if(!isEmpty(w.denpyouId) ){
			//承認ルート上にいなければfalse
			if (!existOnRoute)
			{
				return false;
			}
			// 最終決裁者(業務ロール以外(一般ユーザーとして)最後の承認者)による決裁後であれば変更不可とする
			//memo 最終承認者でもマル秘設定ができたのは、上位先決しようとした時だけ　順番に承認ルート回ってきていたらマル秘設定はできない　→承認解除後表示されない
			//　　TSRさんのソースとしては正しいので、仕様としてどうなってるか確認　
			//　　元Sqlのcountが0のとき、最終決裁者に紐図いている状況枝番の履歴が上位先決承認かそうでないかの確認Sqlが必要
			String sql = "     SELECT COUNT(*) count FROM shounin_route WHERE"
						+"       denpyou_id= ? "
						+"       AND edano>=(SELECT MAX(edano) FROM shounin_route WHERE denpyou_id= ? AND genzai_flg='1')"
						+"       AND saishu_shounin_flg<>'1' ";
			if((long)connection.find(sql,w.denpyouId,w.denpyouId).get("count") == 0){
				return false;
			}
		}

		// 会社設定でマル秘設定変更が承認権がある時に制限されているかチェック
		String seigen = setting.maruhiSetteiShouninSeigen();
		if(seigen.equals("1")){
			if(w.shinsei){
				return true;
			}
			if(w.shounin){
				if(("1").equals(w.procRoute.get("shounin_ken_flg"))){
					return true;
				}
				if(("1").equals(w.procRoute.get("saishu_shounin_flg"))){
					return true;
				}
			}
			//申請時または承認権のある承認時でなければfalse
			return false;
		}


		return true;
	}

	/**
	 * 申請処理
	 * @param w 伝票
	 * @param user ユーザー
	 * @param comment コメント
	 */
	public void shinsei(WfSeigyo w, User user, String comment) {
		//処理前の承認ルート
		List<GMap> routeList = wfLogic.selectShouninRoute(w.denpyouId);

		//処理対象の承認ルートを取得
		GMap procRoute = wfLogic.findProcRoute(routeList, user);
		if(!procRoute.get("edano").equals(Integer.valueOf(1))) throw new RuntimeException("申請時、edanoが1ではない");

		//現在フラグを進める
		List<GMap>[] tmp = nextRoute(user, w.denpyouId, routeList, procRoute);
		List<GMap> nextRouteList = tmp[0];
		List<GMap> skippedRouteList = tmp[1];

		//承認状況登録、承認状況枝番を承認ルートに紐付け
		int joukyouEdano = insertShouninJoukyou(w.denpyouId, user, procRoute, JOUKYOU.SHINSEI, null, comment);
		updateJoukyouEdano(w.denpyouId, "AND edano=1", joukyouEdano, user.getTourokuOrKoushinUserId());

		//伝票状態を更新/
		updateDenpyou(w.denpyouId, DENPYOU_JYOUTAI.SHINSEI_CHUU, user.getLoginUserId());

		//メール
		List<String> userIdList = new ArrayList<>();
		List<String> gougiUserIdList = new ArrayList<>();
		for(GMap nextRoute : nextRouteList){
			if(!isEmpty(nextRoute.get("user_id"))){
				String shoriKengenName = isEmpty(nextRoute.get("shounin_shori_kengen_name")) ? "承認者" : nextRoute.get("shounin_shori_kengen_name");
				userIdList.add(nextRoute.get("user_id") + "\n" + shoriKengenName);
			}
			if(nextRoute.get("isGougi") != null && (boolean)nextRoute.get("isGougi")){
				List<GMap> oyaGougiList = (List<GMap>)nextRoute.get("gougiOya");
				for(GMap oyaGougi : oyaGougiList){
					List<GMap> koGougiList = (List<GMap>)oyaGougi.get("gougiKo");
					for(GMap koGougi : koGougiList){
						if(koGougi.get("user_id") != null){
							String shoriKengenName = isEmpty(koGougi.get("shounin_shori_kengen_name")) ? "承認者" : koGougi.get("shounin_shori_kengen_name");
							gougiUserIdList.add(koGougi.get("user_id") + "\n" + shoriKengenName);
						}
					}
				}
			}
		}
		List<String> skippedIdList = new ArrayList<>();
		for(GMap skeppedRoute : skippedRouteList){
			//通常ルートスキップ分
			if(skeppedRoute.get("user_id") != null){
				String shoriKengenName = isEmpty(skeppedRoute.get("shounin_shori_kengen_name")) ? "承認者" : skeppedRoute.get("shounin_shori_kengen_name");
				skippedIdList.add(skeppedRoute.get("user_id") + "\n" + shoriKengenName);
			}
			//合議内ルートスキップ分
			if(skeppedRoute.get("isGougi") != null && (boolean)skeppedRoute.get("isGougi")){
				List<GMap> oyaGougiList = (List<GMap>)skeppedRoute.get("gougiOya");
				for(GMap oyaGougi : oyaGougiList){
					List<GMap> koGougiList = (List<GMap>)oyaGougi.get("gougiKo");
					for(GMap koGougi : koGougiList){
						if(koGougi.get("user_id") != null){
							String shoriKengenName = isEmpty(koGougi.get("shounin_shori_kengen_name")) ? "承認者" : koGougi.get("shounin_shori_kengen_name");
							skippedIdList.add(koGougi.get("user_id") + "\n" + shoriKengenName);
						}
					}
				}
			}
		}
		sendRealTimeMail(w.denpyouId, w.version, userIdList, MAIL_TSUUCHI_KBN.SHOUNIN_MACHI, user);
		sendRealTimeMail(w.denpyouId, w.version, gougiUserIdList, MAIL_TSUUCHI_KBN.GOUGISHOUNIN_MACHI, user);
		sendRealTimeMail(w.denpyouId, w.version, skippedIdList, MAIL_TSUUCHI_KBN.SHOUNIN_MACHI, user);
	}

	/**
	 * 取戻し処理
	 * @param w 伝票
	 * @param user ログインユーザー
	 * @param comment コメント
	 */
	public void torimodoshi(WfSeigyo w, User user, String comment) {
		// 作業者（=起票者）の承認ルートレコード取得
		GMap shouninRoute = wfLogic.selectKihyoushaData(w.denpyouId);

		//承認状況登録
		insertShouninJoukyou(w.denpyouId, user, shouninRoute, JOUKYOU.TORIMODOSHI, null, comment);

		//作業者変更
		updateGenzaiFlg(w.denpyouId, "AND genzai_flg='1'", "0", user.getTourokuOrKoushinUserId());
		updateGenzaiFlg(w.denpyouId, "AND edano=1", "1", user.getTourokuOrKoushinUserId());
		updateGenzaiFlgGougi(w.denpyouId, "", "0");

		//承認状況紐付け消去
		updateJoukyouEdano(w.denpyouId, "", null, user.getTourokuOrKoushinUserId());
		updateJoukyouEdanoGougi(w.denpyouId, "", null);

		//伝票状態を更新/
		updateDenpyou(w.denpyouId, DENPYOU_JYOUTAI.KIHYOU_CHUU, user.getLoginUserId());

	}

	/**
	 * 取下げ処理
	 * @param w 伝票
	 * @param user ログインユーザー
	 * @param comment コメント
	 */
	public void torisage(WfSeigyo w, User user, String comment) {
		// 作業者（=起票者）の承認ルートレコード取得
		GMap shouninRoute = wfLogic.selectKihyoushaData(w.denpyouId);

		//承認状況登録
		int joukyouEdano = insertShouninJoukyou(w.denpyouId, user, shouninRoute, JOUKYOU.TORISAGE, null, comment);

		//作業者変更
		updateGenzaiFlg(w.denpyouId, "AND edano=1", "0", user.getTourokuOrKoushinUserId());

		//承認状況紐付け
		updateJoukyouEdano(w.denpyouId, "AND edano=1", joukyouEdano, user.getTourokuOrKoushinUserId());

		//伝票状態を更新/
		updateDenpyou(w.denpyouId, DENPYOU_JYOUTAI.TORISAGE_ZUMI, user.getLoginUserId());
	}

	/**
	 * 承認処理
	 * @param w 伝票
	 * @param user ログインユーザー
	 * @param comment コメン
	 * @return 最終承認である
	 */
	public boolean shounin(WfSeigyo w, User user, String comment) {
		//処理前の承認ルート
		List<GMap> routeList = wfLogic.selectShouninRoute(w.denpyouId);

		//処理対象の承認ルートを取得
		GMap procRoute = wfLogic.findProcRoute(routeList, user);

		//処理対象の承認ルートに対する承認文言を取得
		String shouninMongon = "承認";
		if(isEmpty(procRoute.get("gyoumu_role_id"))){
			shouninMongon = wfLogic.findShouninShoriMongon((long)procRoute.get("shounin_shori_kengen_no"));
		}

		//処理前承認者
		GMap skippedRoute = wfLogic.findShouninRouteCur(w.denpyouId);

		//現在フラグを進める 承認権があり　かつ　(現在フラグONまたは上位先決)の場合のみ 承認権がなかったり後追い承認は現在フラグ動かさない
		List<GMap> nextRouteList = null;
		List<GMap> skippedRouteList = null;
		if(!"0".equals(procRoute.get("shounin_ken_flg")) && (procRoute.get("isJoui") != null || "1".equals(procRoute.get("genzai_flg")) || "1".equals(procRoute.get("gougi_genzai_flg")))){
			List<GMap>[] tmp =  nextRoute(user, w.denpyouId, routeList, procRoute);
			nextRouteList = tmp[0];
			skippedRouteList = tmp[1];
		}

		//上位先決承認なら飛ばした履歴出す
		if(procRoute.get("isJoui") != null){
			int preCurEdano = (int)skippedRoute.get("edano");
			updateGenzaiFlg(w.denpyouId, "AND edano=" + preCurEdano, "0", user.getLoginUserId());
			for(int i = preCurEdano; i < (int)procRoute.get("edano"); i++){
				GMap route = routeList.get(i - 1);
				if(route.get("saishu_shounin_flg").equals("1") || route.get("shounin_ken_flg").equals("1")){
					int joukyouEdano = insertShouninJoukyou(w.denpyouId, route, JOUKYOU.SHOUNIN, "", "上位先決承認済", user.getLoginUserId());
					updateJoukyouEdano(w.denpyouId, "AND edano= " + (int)route.get("edano"), joukyouEdano, user.getTourokuOrKoushinUserId());
				}
			}
		}

		//承認状況登録、承認状況枝番を承認ルートに紐付け
		int joukyouEdano = insertShouninJoukyou(w.denpyouId, user, procRoute, JOUKYOU.SHOUNIN, shouninMongon, comment);
		if(procRoute.get("gougi_edano") == null){
			updateJoukyouEdano(w.denpyouId, "AND edano= " + (int)procRoute.get("edano"), joukyouEdano, user.getTourokuOrKoushinUserId());
		}else{
			updateJoukyouEdanoGougi(w.denpyouId, "AND edano=" + (int)procRoute.get("edano") + " AND gougi_edano=" + (int)procRoute.get("gougi_edano") + " AND gougi_edaedano=" + (int)procRoute.get("gougi_edaedano"), joukyouEdano);
		}

		//承認ルート、承認ルート合議子で承認必須＆未処理なユーザーがいなければ終わり
		List<GMap> newRouteList = wfLogic.selectShouninRoute(w.denpyouId);
		boolean finished = allFinished(newRouteList);
		if(finished){
			updateDenpyou(w.denpyouId, DENPYOU_JYOUTAI.SYOUNIN_ZUMI, user.getLoginUserId());
		}

		//メール（途中）
		List<String> userIdList = new ArrayList<>();
		List<String> gougiUserIdList = new ArrayList<>();
		if(nextRouteList != null) for(GMap nextRoute : nextRouteList){
			if(!isEmpty(nextRoute.get("user_id"))){
				String shoriKengenName = isEmpty(nextRoute.get("shounin_shori_kengen_name")) ? "承認" : nextRoute.get("shounin_shori_kengen_name");
				userIdList.add(nextRoute.get("user_id") + "\n" + shoriKengenName);
			}else if(nextRoute.get("isGougi") != null && (boolean)nextRoute.get("isGougi")){
				List<GMap> oyaGougiList = (List<GMap>)nextRoute.get("gougiOya");
				for(GMap oyaGougi : oyaGougiList){
					List<GMap> koGougiList = (List<GMap>)oyaGougi.get("gougiKo");
					for(GMap koGougi : koGougiList){
						if(koGougi.get("user_id") != null){
							String shoriKengenName = isEmpty(koGougi.get("shounin_shori_kengen_name")) ? "承認者" : koGougi.get("shounin_shori_kengen_name");
							gougiUserIdList.add(koGougi.get("user_id") + "\n" + shoriKengenName);
						}
					}
				}
			}
		}
		List<String> skippedIdList = new ArrayList<>();
		if(skippedRouteList != null && ! finished) for(GMap skeppedRoute : skippedRouteList){
			//通常ルートスキップ分
			if(skeppedRoute.get("user_id") != null){
				String shoriKengenName = isEmpty(skeppedRoute.get("shounin_shori_kengen_name")) ? "承認" : skeppedRoute.get("shounin_shori_kengen_name");
				skippedIdList.add(skeppedRoute.get("user_id") + "\n" + shoriKengenName);
			}
			//合議内ルートスキップ分
			if(skeppedRoute.get("isGougi") != null && (boolean)skeppedRoute.get("isGougi")){
				List<GMap> oyaGougiList = (List<GMap>)skeppedRoute.get("gougiOya");
				for(GMap oyaGougi : oyaGougiList){
					List<GMap> koGougiList = (List<GMap>)oyaGougi.get("gougiKo");
					for(GMap koGougi : koGougiList){
						if(koGougi.get("user_id") != null){
							String shoriKengenName = isEmpty(koGougi.get("shounin_shori_kengen_name")) ? "承認" : koGougi.get("shounin_shori_kengen_name");
							skippedIdList.add(koGougi.get("user_id") + "\n" + shoriKengenName);
						}
					}
				}
			}
		}
		sendRealTimeMail(w.denpyouId, w.version, userIdList, MAIL_TSUUCHI_KBN.SHOUNIN_MACHI, user);
		sendRealTimeMail(w.denpyouId, w.version, gougiUserIdList, MAIL_TSUUCHI_KBN.GOUGISHOUNIN_MACHI, user);
		sendRealTimeMail(w.denpyouId, w.version, skippedIdList, MAIL_TSUUCHI_KBN.SHOUNIN_MACHI, user);

		//メール（完了）
		if(finished){
			List<String> jiUserIdList = new ArrayList<>();
			List<String> kanrenUserIdList = new ArrayList<>();
			for(GMap route : w.routeList){
				if((int)route.get("edano") == 1){
					jiUserIdList.add((String)route.get("user_id"));
				}else{
					if(!isEmpty(route.get("user_id"))){
						kanrenUserIdList.add((String)route.get("user_id"));
					}else if(route.get("isGougi") != null && (boolean)route.get("isGougi")){
						List<GMap> oyaGougiList = (List<GMap>)route.get("gougiOya");
						for(GMap oyaGougi : oyaGougiList){
							List<GMap> koGougiList = (List<GMap>)oyaGougi.get("gougiKo");
							for(GMap koGougi : koGougiList){
								kanrenUserIdList.add((String)koGougi.get("user_id"));
							}
						}
					}
				}
			}
			sendRealTimeMail(w.denpyouId, w.version, jiUserIdList, MAIL_TSUUCHI_KBN.JI_DENPYOU_SHOUNIN, user);
			sendRealTimeMail(w.denpyouId, w.version, kanrenUserIdList, MAIL_TSUUCHI_KBN.KANREN_DENPYOU_SHOUNIN, user);
			jiUserIdList.addAll(kanrenUserIdList);
			tsuutiTouroku(w.denpyouId, jiUserIdList, user);
		}

		return finished;
	}

	/**
	 * 否認処理
	 * @param w 伝票
	 * @param user ログインユーザー
	 * @param comment コメント
	 */
	public void hinin(WfSeigyo w, User user, String comment) {
		//作業者変更
		Integer preCurEdano = updateGenzaiFlg(w.denpyouId, "AND genzai_flg= '1'", "0", user.getTourokuOrKoushinUserId());
		updateGenzaiFlgGougi(w.denpyouId, "AND gougi_genzai_flg= '1'", "0");

		//上位先決否認なら飛ばした履歴出す
		if(w.procRoute.get("isJoui") != null){
			for(int i = preCurEdano; i < (int)w.procRoute.get("edano"); i++){
				GMap route = w.routeList.get(i - 1);
				if(route.get("saishu_shounin_flg").equals("1") || route.get("shounin_ken_flg").equals("1")){
					int joukyouEdano = insertShouninJoukyou(w.denpyouId, route, JOUKYOU.HININ, "", "上位先決否認済", user.getLoginUserId());
					updateJoukyouEdano(w.denpyouId, "AND edano= " + (int)route.get("edano"), joukyouEdano, user.getTourokuOrKoushinUserId());
				}
			}
		}

		//承認状況登録
		//承認状況登録、承認状況枝番を承認ルートに紐付け
		int joukyouEdano = insertShouninJoukyou(w.denpyouId, user, w.procRoute, JOUKYOU.HININ, null, comment);
		if(w.procRoute.get("gougi_edano") == null){
			updateJoukyouEdano(w.denpyouId, "AND edano= " + (int)w.procRoute.get("edano"), joukyouEdano, user.getTourokuOrKoushinUserId());
		}else{
			updateJoukyouEdanoGougi(w.denpyouId, "AND edano=" + (int)w.procRoute.get("edano") + " AND gougi_edano=" + (int)w.procRoute.get("gougi_edano") + " AND gougi_edaedano=" + (int)w.procRoute.get("gougi_edaedano"), joukyouEdano);
		}

		//伝票状態を更新/
		updateDenpyou(w.denpyouId, DENPYOU_JYOUTAI.HININ_ZUMI, user.getLoginUserId());

		//メール
		List<String> jiUserIdList = new ArrayList<>();
		List<String> kanrenUserIdList = new ArrayList<>();
		for(GMap route : w.routeList){
			if((int)route.get("edano") == 1){
				jiUserIdList.add((String)route.get("user_id"));
			}else{
				if(!isEmpty(route.get("user_id"))){
					kanrenUserIdList.add((String)route.get("user_id"));
				}else if(route.get("isGougi") != null && (boolean)route.get("isGougi")){
					List<GMap> oyaGougiList = (List<GMap>)route.get("gougiOya");
					for(GMap oyaGougi : oyaGougiList){
						List<GMap> koGougiList = (List<GMap>)oyaGougi.get("gougiKo");
						for(GMap koGougi : koGougiList){
							kanrenUserIdList.add((String)koGougi.get("user_id"));
						}
					}
				}
			}
		}
		sendRealTimeMail(w.denpyouId, w.version, jiUserIdList, MAIL_TSUUCHI_KBN.JI_DENPYOU_HININ, user);
		sendRealTimeMail(w.denpyouId, w.version, kanrenUserIdList, MAIL_TSUUCHI_KBN.KANREN_DENPYOU_HININ, user);
		jiUserIdList.addAll(kanrenUserIdList);
		tsuutiTouroku(w.denpyouId, jiUserIdList, user);
	}

	/**
	 * 差戻し処理
	 * @param w 伝票
	 * @param user ログインユーザー
	 * @param edano 差戻し先枝番
	 * @param gougiEdano 差戻し先合議枝番
	 * @param gougiEdaedano 差戻し先合議枝枝番
	 * @param comment コメント
	 */
	public void sashimodoshi(WfSeigyo w, User user, Integer edano, Integer gougiEdano, Integer gougiEdaedano, String comment) {
		List<GMap> nextRouteList = new ArrayList<>();
		GMap sakiRoute = w.routeList.get(edano-1);

		//処理するユーザー（業務ロール）の承認ルート
		if ((int)w.procRoute.get("edano") < edano) throw new EteamAccessDeniedException();

		//承認状況登録
		insertShouninJoukyou(w.denpyouId, user, w.procRoute, JOUKYOU.SASHIMODOSHI, null, comment);

		//普通の承認ルートへ
		if(gougiEdano == null && !(boolean)sakiRoute.get("isGougi")){
			//作業者OFF
			updateGenzaiFlg(w.denpyouId, "AND edano > " + edano, "0", user.getLoginUserId());
			updateGenzaiFlgGougi(w.denpyouId, "AND edano > " + edano, "0");
			updateJoukyouEdano(w.denpyouId, "AND edano >= " + edano, null, user.getLoginUserId());
			updateJoukyouEdanoGougi(w.denpyouId, "AND edano >= " + edano, null);
			//作業者ON
			updateGenzaiFlg(w.denpyouId, "AND edano=" + edano, "1", user.getLoginUserId());
			nextRouteList.add(w.routeList.get(edano-1));
		//合議へ
		}else if(gougiEdano == null && (boolean)sakiRoute.get("isGougi")){
			//作業者OFF
			updateGenzaiFlg(w.denpyouId, "AND edano > " + edano, "0", user.getLoginUserId());
			updateGenzaiFlgGougi(w.denpyouId, "AND edano > " + edano, "0");
			updateJoukyouEdano(w.denpyouId, "AND edano >= " + edano, null, user.getLoginUserId());
			updateJoukyouEdanoGougi(w.denpyouId, "AND edano >= " + edano, null);
			//作業者ON
			updateGenzaiFlg(w.denpyouId, "AND edano=" + edano, "1", user.getLoginUserId());
			nextRouteList.add(sakiRoute);

			//次の承認ルート合議子(承認権あり)があるか調べる
			List<GMap> nextGougiOyaList = (List<GMap>)sakiRoute.get("gougiOya");
			for(GMap nextGougiOya : nextGougiOyaList){
				List<GMap> gougiKoList = ((List<GMap>)nextGougiOya.get("gougiKo"));
				GMap nextGougiKo =null;
				for(int k = 0; k < gougiKoList.size(); k++){
					GMap tmpGougiKo = gougiKoList.get(k);
					if(tmpGougiKo.get("shounin_ken_flg").equals("1")){
						nextGougiKo = tmpGougiKo;
						break;
					}
				}

				//次の承認ルート合議子(承認権あり)があればONにする
				if(nextGougiKo != null){
					updateGenzaiFlgGougi(w.denpyouId, "AND edano=" + edano + " AND gougi_edaedano=" + nextGougiKo.get("gougi_edaedano"), "1");
					nextRouteList.add(nextGougiKo);
					//ついでに同じ役職者もONにする・・
					for(int kk = (int)nextGougiKo.get("gougi_edaedano"); kk < gougiKoList.size(); kk++){
						GMap tmpGougiKo = gougiKoList.get(kk);
						if(sameRoute(nextGougiKo, tmpGougiKo)){
							updateGenzaiFlgGougi(w.denpyouId, "AND edano=" + edano + " AND gougi_edaedano=" + tmpGougiKo.get("gougi_edaedano"), "1");
							nextRouteList.add(tmpGougiKo);
						}else{
							break;
						}
					}
				}
			}
		//合議内承認者へ
		}else if(gougiEdano != null){
			List<GMap>	gougiKoList = wfLogic.getGougiKoList(w.routeList, edano, gougiEdano);
			GMap nextGougiKo = wfLogic.getGougiKo(w.routeList, edano, gougiEdano, gougiEdaedano);
			//作業者OFF
			updateGenzaiFlgGougi(w.denpyouId, "AND edano=" + edano + " AND gougi_edano=" + gougiEdano + " AND gougi_edaedano>" + gougiEdaedano, "0");
			updateJoukyouEdanoGougi(w.denpyouId, "AND edano=" + edano + " AND gougi_edano=" + gougiEdano + " AND gougi_edaedano>" + gougiEdaedano, null);
			//作業者ON
			updateGenzaiFlgGougi(w.denpyouId, "AND edano=" + edano + "AND gougi_edano=" + gougiEdano + " AND gougi_edaedano=" + gougiEdaedano, "1");
			updateJoukyouEdanoGougi(w.denpyouId, "AND edano=" + edano + " AND gougi_edano=" + gougiEdano + " AND gougi_edaedano=" + gougiEdaedano, null);
			nextRouteList.add(nextGougiKo);
			//ついでに同じ役職者もONにする
			int khani[] = getGougiKoDouretsuHani(gougiKoList, nextGougiKo);
			if (khani != null)
			{
				for(int kk = khani[0]; kk <= khani[1]; kk++){
					GMap tmpGougiKo = gougiKoList.get(kk);
					updateGenzaiFlgGougi(w.denpyouId, "AND edano=" + edano + " AND gougi_edano=" + gougiEdano + " AND gougi_edaedano=" + (kk+1), "1");
					updateJoukyouEdanoGougi(w.denpyouId, "AND edano=" + edano + " AND gougi_edano=" + gougiEdano + " AND gougi_edaedano=" + (kk+1), null);
					nextRouteList.add(tmpGougiKo);
				}
			}
		}

		//メール
		List<String> userIdList = new ArrayList<>();
		for(GMap nextRoute : nextRouteList){
			if(!isEmpty(nextRoute.get("user_id"))){
				userIdList.add((String)nextRoute.get("user_id"));
			}
		}
		sendRealTimeMail(w.denpyouId, w.version, userIdList, MAIL_TSUUCHI_KBN.JI_DENPYOU_SASHIMODOSHI, user);

		//伝票状態を更新/
		updateDenpyou(w.denpyouId, (1 == edano) ? DENPYOU_JYOUTAI.KIHYOU_CHUU : DENPYOU_JYOUTAI.SHINSEI_CHUU, user.getLoginUserId());
	}

	/** 
	 * 最終承認の解除処理
	 * @param w 伝票
	 * @param gyoumuRoleId 業務ロールID
	 * @param user ログインユーザー
	 * @param comment コメント
	 */
	public void shouninKaijo(WfSeigyo w, String gyoumuRoleId, User user, String comment) {
		GMap saishuuMp = findSaishuShouninSha(w.denpyouId);
		String roleId = saishuuMp.get("gyoumu_role_id").toString();
		if(!roleId.equals(gyoumuRoleId)) {
			throw new EteamAccessDeniedException();
		}
		
		int saishuShouninEdano = saishuuMp.get("edano");
		
		insertShouninJoukyouShouninKaijo(w.denpyouId, user, gyoumuRoleId, saishuuMp.get("gyoumu_role_name"), JOUKYOU.SHOUNINKAIJO, null, comment, saishuShouninEdano);
		
		//20220930 最終承認者の承認を解除するのみ　上位承認を解除して戻すこともしない　　最終承認者の行を再度黄色インバースにする
		updateJoukyouEdano(w.denpyouId, "AND edano = " + saishuShouninEdano, null, user.getLoginUserId());
		updateGenzaiFlg(w.denpyouId, "AND edano = " + saishuShouninEdano, "1", user.getLoginUserId());
		
		//伝票状態を更新
		updateDenpyou(w.denpyouId, DENPYOU_JYOUTAI.SHINSEI_CHUU, user.getLoginUserId());
	}

	/**
	 * 承認ルートの処理権限名（申請者、最終承認者）を最新の登録内容に変更する
	 * @param denpyouKbn 伝票区分
	 * @param routeList 承認ルート
	 */
	protected void resetShorikengen(String denpyouKbn, List<GMap> routeList){
		for(GMap map : routeList){
			if(null == map.get("shounin_shori_kengen_no")){
				if(isEmpty(map.get("gyoumu_role_id"))){
					//現在の申請者の承認処理権限名を取得しセット
					map.put("shounin_shori_kengen_name", kihyouLogic.findDenpyouKanri(denpyouKbn).get("shinsei_shori_kengen_name").toString());
				}else{
					//現在の最終承認者の処理権限名を取得してセット
					map.put("shounin_shori_kengen_name", wfLogic.findSaiSaishuuShouninShoriKengen(denpyouKbn, map.get("gyoumu_role_id").toString()));
				}
			}
		}
	}

	/**
	 * 承認ルートを進める（現在フラグの変更）
	 * @param user ユーザー
	 * @param denpyouId 伝票ID
	 * @param routeList 承認ルート ＋ 合議親 ＋ 合議子(ALL)
	 * @param procRoute 処理対象の承認ルート OR 合議子(ALL)
	 * @return [0]=現在フラグ遷移先(承認ルート OR 合議子) [1]=飛ばされた(承認ルート OR 合議子)
	 */
	protected List<GMap>[] nextRoute(User user, String denpyouId, List<GMap> routeList, GMap procRoute) {
		List<GMap> nextList = new ArrayList<>();
		List<GMap> skippedList = new ArrayList<>();

		int edano = (int)procRoute.get("edano");
		GMap curRoute = routeList.get(edano - 1);
		boolean gougiFinish = false;

		//【承認ルート合議を処理した時】
		if((boolean)curRoute.get("isGougi")){
			int gougiEdano = (int)procRoute.get("gougi_edano");
			int gougiEdaedano = (int)procRoute.get("gougi_edaedano");
			List<GMap> curGougiOyaList = (List<GMap>)curRoute.get("gougiOya");
			GMap curGougiOya = curGougiOyaList.get(gougiEdano - 1);
			List<GMap> curGougiKoList = (List<GMap>)curGougiOya.get("gougiKo");
			GMap curGougiKo = curGougiKoList.get(gougiEdaedano - 1);

			//処理対象承認ルート合議子をOFFにして
			curGougiKo.put("gougi_genzai_flg_pre", "1");
			curGougiKo.put("gougi_genzai_flg", "0");
			curGougiKo.put("joukyou_edano", "dummy");//gougiKoFinished用

			//承認ルート合議子が同列者との兼合いも含めて終わった？
			boolean sameFinished = gougiKoFinished(routeList, curGougiKo);
			if(sameFinished){
				//ついでに同じ役職者も条件によりOFF
				int[] khani = getGougiKoDouretsuHani(curGougiKoList, curGougiKo);
				if(khani != null){
					for(int i = khani[0]; i <= khani[1]; i++){
						GMap tmpGougiKo = curGougiKoList.get(i);
						if(tmpGougiKo.get("gougi_genzai_flg").equals("1")){
							tmpGougiKo.put("gougi_genzai_flg_pre", "1");
							tmpGougiKo.put("gougi_genzai_flg", "0");
						}
					}
				}
				//次の承認ルート(承認権あり)があるか調べる
				GMap nextGougiKo = null;
				for(int kk = (khani == null ? gougiEdaedano : khani[1] + 1); kk < curGougiKoList.size(); kk++){
					GMap tmpGougiKo = curGougiKoList.get(kk);
					if(hasShouninKen(tmpGougiKo)){
						nextGougiKo = tmpGougiKo;
						break;
					}else{
						skippedList.add(tmpGougiKo);
					}
				}
				//次の承認ルート合議子(承認権あり)があればONにする
				if(nextGougiKo != null){
					nextGougiKo.put("gougi_genzai_flg", "1");
					nextList.add(nextGougiKo);
					//ついでに同じ役職者もONにする
					int[] nextKhani = getGougiKoDouretsuHani(curGougiKoList, nextGougiKo);
					if (nextKhani != null)
					{
						for(int kk = nextKhani[0] + 1;  kk <= nextKhani[1]; kk++){
							GMap tmpGougiKo = curGougiKoList.get(kk);
							tmpGougiKo.put("gougi_genzai_flg", "1");
							nextList.add(tmpGougiKo);
						}
					}
				}
			}
			//合議が終わって承認ルートを進めてよいか判定
			gougiFinish = true;
			for(GMap gougiOya : curGougiOyaList){
				List<GMap> gougiKoList = (List<GMap>)gougiOya.get("gougiKo");
				for(GMap gougiKo : gougiKoList){
					if(gougiKo.get("gougi_genzai_flg").equals("1")){
						gougiFinish = false;
					}
				}
			}
		}

		//【合議でない承認ルートを処理した】※承認ルート合議子を処理した際、その合議全体が終わった時も
		if(!(boolean)curRoute.get("isGougi") || gougiFinish){

			//処理対象承認ルートをOFFにして
			curRoute.put("genzai_flg_pre", "1");
			curRoute.put("genzai_flg", "0");

			//次の承認ルート(承認権あり)があるか調べる
			GMap nextRoute = null;
			for(int i = edano; i < routeList.size(); i++){
				GMap tmpRoute = routeList.get(i);
				if(hasShouninKen(tmpRoute)){
					nextRoute = tmpRoute;
					break;
				}else{
					skippedList.add(tmpRoute);
				}
			}

			//次の承認ルート(承認権あり)があればONにする
			if(nextRoute != null){
				nextRoute.put("genzai_flg", "1");
				nextList.add(nextRoute);

				//次の承認ルートが合議なら配下の承認ルート合議子の先頭(承認権あり)があればONにする
				List<GMap> nextGougiOyaList = (List<GMap>)nextRoute.get("gougiOya");
				for(GMap nextGougiOya : nextGougiOyaList){
					List<GMap> gougiKoList = ((List<GMap>)nextGougiOya.get("gougiKo"));
					for(int k = 0; k < gougiKoList.size(); k++){
						GMap gougiKo = gougiKoList.get(k);
						if(hasShouninKen(gougiKo)){

							//次の承認ルート合議子(承認権あり)があればONにする
							gougiKo.put("gougi_genzai_flg", "1");
							nextList.add(gougiKo);

							//ついでに同じ役職者もON
							int[] khani = getGougiKoDouretsuHani(gougiKoList, gougiKo);
							if (khani != null)
							{
								for(int i = khani[0] + 1; i <= khani[1]; i++){
									GMap gougiKoTonari = gougiKoList.get(i);
									gougiKoTonari.put("gougi_genzai_flg", "1");
									nextList.add(gougiKoTonari);
								}
							}
							break;
						}
					}
				}
			}
		}

		//--------------------
		//上げ下げされた現在フラグについて更新
		//--------------------
		//承認ルート単位
		for(GMap route : routeList){
			if(route.get("genzai_flg").equals("1")){
				updateGenzaiFlg(denpyouId, "AND edano=" + route.get("edano"), "1", user.getTourokuOrKoushinUserId());
			}else if("1".equals(route.get("genzai_flg_pre"))){
				updateGenzaiFlg(denpyouId, "AND edano=" + route.get("edano"), "0", user.getTourokuOrKoushinUserId());
			}
			List<GMap> gougiOyaList = (List<GMap>)route.get("gougiOya");
			for(int j = 0; j < gougiOyaList.size(); j++){
				GMap gougiOya = gougiOyaList.get(j);
				List<GMap> gougiKoList = (List<GMap>)gougiOya.get("gougiKo");
				for(int z = 0; z < gougiKoList.size(); z++){
					GMap gougiKo = gougiKoList.get(z);
					if(gougiKo.get("gougi_genzai_flg").equals("1")){
						updateGenzaiFlgGougi(denpyouId, "AND edano=" + (int)route.get("edano") + " AND gougi_edano=" + (int)gougiOya.get("gougi_edano") + " AND gougi_edaedano=" + (int)gougiKo.get("gougi_edaedano"), "1");
					}
					if("1".equals(gougiKo.get("gougi_genzai_flg_pre"))){
						updateGenzaiFlgGougi(denpyouId, "AND edano=" + (int)route.get("edano") + " AND gougi_edano=" + (int)gougiOya.get("gougi_edano") + " AND gougi_edaedano=" + (int)gougiKo.get("gougi_edaedano"), "0");
					}
				}
			}
		}
		return new List[]{nextList, skippedList};
	}
	/**
	 * 承認ルート合議子が同じ部門／役職（つまり同じgougi_pattern_koから二人以上とれたってこと)である
	 * @param a 承認ルート合議子A
	 * @param b 承認ルート合議子B
	 * @return 同じ
	 */
	protected boolean sameRoute(GMap a, GMap b){
		return (a.get("bumon_cd").equals(b.get("bumon_cd")) && a.get("bumon_role_id").equals(b.get("bumon_role_id")));
	}
	/**
	 * 通常承認ルートが処理済？
	 * @param route 承認ルート(合議でない)
	 * @return 処理済
	 */
	protected boolean normalRouteFinished(GMap route){
		return !((route.get("saishu_shounin_flg").equals("1") || route.get("shounin_hissu_flg").equals("1")) && route.get("joukyou_edano") == null);
	}
	/**
	 * 合議子が処理済？(単体判定)
	 * @param gougiKo 承認ルート合議子
	 * @return 処理済
	 */
	protected boolean gougiKoFinished(GMap gougiKo){
		return !(gougiKo.get("shounin_hissu_flg").equals("1") && gougiKo.get("joukyou_edano") == null);
	}
	/**
	 * 合議子が処理済？(同列判定込み)
	 * @param routeList 承認ルートリスト
	 * @param gougiKo 承認ルート合議子
	 * @return 処理済
	 */
	public boolean gougiKoFinished(List<GMap> routeList, GMap gougiKo){
		int edano = (int)gougiKo.get("edano");
		int gougiEdano = (int)gougiKo.get("gougi_edano");
		GMap route = routeList.get(edano-1);
		List<GMap> gougiOyaList = (List<GMap>)route.get("gougiOya");
		GMap gougiOya = gougiOyaList.get(gougiEdano-1);
		List<GMap> gougiKoList = (List<GMap>)gougiOya.get("gougiKo");

		int[] khani = getGougiKoDouretsuHani(gougiKoList, gougiKo);

		//同列者がいない場合
		if(khani == null){
			return gougiKoFinished(gougiKo);
		//同列者がいる場合
		}else{
			int kstart = khani[0], kend = khani[1];

			//同列内で処理済かどうか判定
			boolean sameFinished = false;
			switch((String)gougiKo.get("shounin_ninzuu_cd")){
			case SHOUNIN_NINZUU_CD.ZENIN:
				boolean mishoriAri = false;
				for(int kk = kstart; kk <= kend; kk++){
					GMap tmpGougiKo = gougiKoList.get(kk);
					if(!gougiKoFinished(tmpGougiKo)){
						mishoriAri = true;
					}
				}
				sameFinished = !mishoriAri;
				break;
			case SHOUNIN_NINZUU_CD.HITORI:
				for(int kk = kstart; kk <= kend; kk++){
					GMap tmpGougiKo = gougiKoList.get(kk);
					if(gougiKoFinished(tmpGougiKo)){
						sameFinished = true;
					}
				}
				break;
			case SHOUNIN_NINZUU_CD.HIRITSU:
				int hiritsu = (int)gougiKo.get("shounin_ninzuu_hiritsu");
				int count = 0;
				for(int kk = kstart; kk <= kend; kk++){
					GMap tmpGougiKo = gougiKoList.get(kk);
					if(gougiKoFinished(tmpGougiKo)){
						count++;
					}
				}
				if((double)count / (kend-kstart+1) * 100>= hiritsu){
					sameFinished = true;
				}
				break;
			}
			return sameFinished;
		}
	}

	/**
	 * 現在作業者が最終承認者であるかチェックする。
	 * @param denpyouId 伝票ID
	 * @param user ログインユーザー
	 * @return 検索結果 最終承認者である。
	 */
	public boolean isLastShounin(String denpyouId, User user) {
		List<GMap> routeList = wfLogic.selectShouninRoute(denpyouId);
		GMap procRoute = wfLogic.findProcRoute(routeList, user);
		//現在作業者が枝番1なのは申請者なので例外的にはずす
		if ((int)procRoute.get("edano") == 1)
		{
			return false;
		}
		//仮にこのユーザで処理したら最終承認済になるか？っていう
		procRoute.put("joukyou_edano", 999);//処理対象者の状況枝番を入れて仮想処理状態に
		for(int i = 0; i+1 < (int)procRoute.get("edano"); i++){
			if(!(boolean)routeList.get(i).get("isGougi")){
				routeList.get(i).put("joukyou_edano", 999);//上位先決がありえるので処理対象者より前は仮想処理状態に
			}
		}
		return allFinished(routeList);
	}

	/**
	 * 全承認ルートが処理済？
	 * @param routeList 承認ルートリスト
	 * @return 処理済み
	 */
	protected boolean allFinished(List<GMap> routeList) {
		for(GMap route : routeList){
			if(!(boolean)route.get("isGougi")){
				if(!normalRouteFinished(route)){
					return false;
				}
			}else{
				List<GMap> gougiOyaList = (List<GMap>)route.get("gougiOya");
				for(GMap gougiOya : gougiOyaList){
					List<GMap> gougiKoList = (List<GMap>)gougiOya.get("gougiKo");
					for(GMap gougiKo : gougiKoList){
						if(!gougiKoFinished(routeList, gougiKo)){
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	/**
	 * 合議内同列者の範囲を返す
	 * @param gougiKoList 承認ルート合議子リスト
	 * @param gougiKo 承認ルート合議子
	 * @return gougiKoと同列の範囲INDEX [0]from [1]to
	 */
	protected int[] getGougiKoDouretsuHani(List<GMap> gougiKoList, GMap gougiKo){
		int k = (int)gougiKo.get("gougi_edaedano") - 1;
		int kstart = k, kend = k;

		//同列者がいる場合
		if((k+1 < gougiKoList.size() && sameRoute(gougiKo, gougiKoList.get(k+1))) || (k-1 >= 0 && k-1 < gougiKoList.size() && sameRoute(gougiKo, gougiKoList.get(k-1)))){
			//同じ役職者のgougiKo内インデックス：kstart～kendを決定
			for(kend = k+1; kend < gougiKoList.size(); kend++){
				GMap tmpGougiKo = gougiKoList.get(kend);
				if(!sameRoute(gougiKo, tmpGougiKo)){
					kend--;
					break;
				}
			}
			if (kend == gougiKoList.size())
			{
				kend = gougiKoList.size() - 1;
			}
			for(kstart = k-1; kstart >= 0; kstart--){
				GMap tmpGougiKo = gougiKoList.get(kstart);
				if(!sameRoute(gougiKo, tmpGougiKo)){
					kstart++;
					break;
				}
			}
			if (kstart == -1)
			{
				kstart = 0;
			}

			return new int[]{kstart, kend};
		//同列者がいない場合
		}else{
			return null;
		}
	}
	/**
	 * 承認権の有無をチェック
	 * @param route 承認ルート OR 承認ルート合議子
	 * @return 承認権あり
	 */
	public boolean hasShouninKen(GMap route) {
		if(route.get("gougi_edano") == null){
			if(!(boolean)route.get("isGougi")){
				if(route.get("saishu_shounin_flg").equals("1") || route.get("shounin_ken_flg").equals("1")){
					return true;
				}
			}else{
				List<GMap> gougiOyaList = (List<GMap>)route.get("gougiOya");
				for(GMap gougiOya : gougiOyaList){
					List<GMap> gougiKoList = (List<GMap>)gougiOya.get("gougiKo");
					for(GMap gougiKo : gougiKoList){
						if(hasShouninKen(gougiKo)){
							return true;
						}
					}
				}
			}
		}else{
			if(route.get("shounin_ken_flg").equals("1")){
				return true;
			}
		}
		return false;
	}

	/**
	 * 差戻し先のユーザー/合議のリスト
	 * @param w 伝票
	 * @return 差戻し先
	 */
	public List<GMap> makeSashimodosiSakiList(WfSeigyo w) {
		List<GMap> sashimodoshiSakiList = new ArrayList<>();
		List<GMap> routeList = (ArrayList<GMap>)((ArrayList<GMap>)w.routeList).clone();

		//自分より前の承認ルートで承認権限あるやつ
		for(int i = 0; i < (int)w.procRoute.get("edano")-1; i++){
			GMap route = routeList.get(i);
			if(i == 0){
				sashimodoshiSakiList.add(route);
			}else{
				if(hasShouninKen(route)){
					//合議として承認権限もつ（合議内の誰かが承認権限を持つ）ので合議自体を差戻し先に追加
					if((boolean)route.get("isGougi")){
						sashimodoshiSakiList.add(route);
						String gougiName = "";
						List<GMap> gougiOyaList = (List<GMap>)route.get("gougiOya");
						for(GMap gougiOya : gougiOyaList){
							if(!gougiName.isEmpty()){
								gougiName += "\r\n";
							}
							gougiName += gougiOya.get("gougi_name");
						}
						route.put("user_full_name", "");
						route.put("bumon_full_name", gougiName);
						route.put("bumon_roll_name", "");
					//一般承認者として承認権限をもっていて上位先決されてもいないので差戻し先に追加
					}else{
						if(route.get("joukyou_edano") != null){
							GMap joukyouMap = wfLogic.selectSaishinShouninJoukyou(w.denpyouId, (int)route.get("joukyou_edano"));
							String joukyouCd = (String)joukyouMap.get("joukyou_cd");//上位先決で飛ばされても承認/否認のコードが入る
							String joukyou = (String)joukyouMap.get("joukyou"); //上位先決で飛ばされたら空のはず
							if((joukyouCd.equals(JOUKYOU.SHOUNIN) || joukyouCd.equals(JOUKYOU.HININ)) && !joukyou.isEmpty()){
								sashimodoshiSakiList.add(route);
							}
						}
					}
				}
			}
		}
		//合議承認の場合のみ）合議内で自分より前で承認権限あるやつ
		if(w.procRoute.get("gougi_edano") != null){
			int edano = (int)w.procRoute.get("edano");
			int gougiEdano = (int)w.procRoute.get("gougi_edano");
			int gougiEdaedano = (int)w.procRoute.get("gougi_edaedano");
			List<GMap> gougiOyaList = (List<GMap>)routeList.get(edano-1).get("gougiOya");
			List<GMap> gougiKoList = (List<GMap>)gougiOyaList.get(gougiEdano-1).get("gougiKo");
			int iend = gougiEdaedano-2;
			for(; iend >= 0; iend--){
				if(!sameRoute(w.procRoute, gougiKoList.get(iend))){
					break;
				}
			}
			for(int i = 0; i <= iend; i++){
				GMap gougiKo = gougiKoList.get(i);
				if(hasShouninKen(gougiKo) && gougiKo.get("joukyou_edano") != null){
					if(i > 0 && sameRoute(gougiKo, gougiKoList.get(i-1))){
						GMap sasimodosiSaki = sashimodoshiSakiList.get(sashimodoshiSakiList.size()-1);
						sasimodosiSaki.put("user_full_name", sasimodosiSaki.get("user_full_name") + "\r\n" + gougiKo.get("user_full_name"));
					}else{
						sashimodoshiSakiList.add(gougiKo);
					}
				}
			}
		}
		return sashimodoshiSakiList;
	}

	/**
	 * 起案終了／起案終了解除の対象とする仮払伝票IDを取得する処理を実装。
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @return 仮払伝票ID（処理対象がない場合はnull）
	 */
	public String getKianShuuryouKaribaraiDenpyouId(String denpyouId, String denpyouKbn) {
		String result = null;
		GMap denpyouShubetsuMap = null;
		GMap seisan = null;
		switch(denpyouKbn){
		case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
			denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(DENPYOU_KBN.KARIBARAI_SHINSEI);
			seisan = kaikeiLogic.findKeihiSeisan(denpyouId);
			break;
		case DENPYOU_KBN.RYOHI_SEISAN:
			denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI);
			seisan = kaikeiLogic.findRyohiSeisan(denpyouId);
			break;
		case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
			denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI);
			seisan = kaikeiLogic.findKaigaiRyohiSeisan(denpyouId);
			break;
		default:
			break;
		}

		if(null != denpyouShubetsuMap){
			if(YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(denpyouShubetsuMap.get("yosan_shikkou_taishou").toString())){

				// 仮払金未使用
				if("1".equals(seisan.get("karibarai_mishiyou_flg").toString())){
					// 仮払伝票IDを返却
					result = seisan.get("karibarai_denpyou_id").toString();
				}

				// 出張中止(国内・海外旅費精算の場合)
				if(DENPYOU_KBN.RYOHI_SEISAN.equals(denpyouKbn) || DENPYOU_KBN.KAIGAI_RYOHI_SEISAN.equals(denpyouKbn)){
					if("1".equals(seisan.get("shucchou_chuushi_flg").toString())){
						// 仮払伝票IDを返却
						result = seisan.get("karibarai_denpyou_id").toString();
					}
				}
			}
		}

		return result;
	}

	/**
	 * 申請前に起案終了していたかどうか判定する
	 * @param denpyouId 伝票ID
	 * @return 申請前の起案終了フラグ=1の場合はtrue
	 */
	public boolean isKianShuuryouBeforeShinsei(String denpyouId) {
		final String sql = "SELECT"
						 + " CASE comment WHEN null THEN FALSE ELSE comment='" + kianShuuryou.SETTEI + "' END AS result "
						 + "FROM"
						 + " shounin_joukyou "
						//精算申請時の起案終了でも承認状況追加されるため、申請の直前の起案終了操作はmax(edano-1)となる
						 + "WHERE edano = (SELECT MAX(edano-1) FROM shounin_joukyou WHERE joukyou='" + kianShuuryou.JOUKYOU + "' AND denpyou_id= ? )"
						 + "  AND denpyou_id = ?";
		return (boolean)connection.find(sql, denpyouId, denpyouId).get("result");
	}

	/**
	 * （期別）決算期に設定されたすべての年月を取得して月のリストMapにします。
	 * key = 月末の年月(yyyy/MM)、valがyyyy年MM月
	 * @return 年月リスト
	 */
	public List<GMap> createKiKesnNengetsuList() {

		MasterKanriCategoryLogic masterCommonLogic = (MasterKanriCategoryLogic)EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		SimpleDateFormat formatKey = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat formatVal = new SimpleDateFormat("yyyy年MM月");

		// （期別）決算期に設定されたすべての年月を取得
		List<GMap> ret = masterCommonLogic.loadKesnToDateAll();

		// フォーマットを設定
		for (Map<String, Object> month : ret) {
			month.put("key", formatKey.format(month.get("to_date")));
			month.put("val", formatVal.format(month.get("to_date")));
		}

		return ret;
	}

	/**
	 * 伝票の起票部門がユーザ所属配下部門である故の参照権限を持つ
	 * @param user ユーザ
	 * @param denpyouId 伝票ID
	 * @return 判定結果
	 */
	public boolean judgeShozokuBumonKyoyu(User user, String denpyouId) {
		GMap sanshouKihyousha = wfLogic.selectKihyoushaData(denpyouId);

		//「所属部門＆配下部門のデータ共有する・しない」オプション設定値が1(する)の場合
        //該当ユーザの所属部門・配下部門により起票された伝票は
        //該当ユーザが承認ルート上に存在しない場合も参照可能とする
        List<String> lstBumon = new ArrayList<String>();

        // ログインユーザに紐づく所属部門の件数分、配下部門のリストを取得する。
        if(user.getBumonCd() != null) for (String aBumonCd : user.getBumonCd()){
            // まず、自所属部門をリストに追加
            lstBumon.add(aBumonCd);
            // 自所属部門を親とする子部門を取得して、リストに追加
            List<GMap> lstNestBumon = bumonUserLogic.selectBumonTreeStructure(aBumonCd, true);
            for (GMap aMap : lstNestBumon){
                lstBumon.add(aMap.get("bumon_cd").toString());
            }
        }

        //一致確認
        if(lstBumon.contains(sanshouKihyousha.get("bumon_cd"))) {
        	return true;
        }
		return false;
	}
	/**
	 * 添付ファイルのチェックを行う
	 * @param denpyouKbn 伝票区分
	 * @param uploadFileFileName 添付ファイル名
	 * @param errorList エラーリスト
	 */
	public void checkTenpuFile(String denpyouKbn, String[] uploadFileFileName, List<String> errorList) {
		return;
	}

	/**
	 * 伝票起案紐付に初期レコードを登録する。
	 * @param denpyouId 伝票ID
	 * @param kianDenpyouId 起案伝票ID配列
	 * @param kianDenpyouKbn 起案伝票区分配列
	 * @param kianHimodukeFlg 起案紐づけフラグ
	 */
	protected void tourokuDenpyouKianHimozuke(
			String denpyouId, String[] kianDenpyouId, String[] kianDenpyouKbn,
			String kianHimodukeFlg){
		deleteDenpyouKianHimozuke(denpyouId);
		insertDenpyouKianHimozuke(denpyouId, kianDenpyouId[0], kianDenpyouKbn[0]);
	}

	/**
	 * 伝票起案紐付レコードを更新する。
	 * @param denpyouJoutai 伝票状態
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param kianDenpyouId 起案伝票ID配列
	 * @param kianDenpyouKbn 起案伝票区分配列
	 * @param kianHimodukeFlg 起案紐づけフラグ
	 */
	protected void koushinDenpyouKianHimozuke(
			String denpyouJoutai, String denpyouId, String denpyouKbn,
			String[] kianDenpyouId, String[] kianDenpyouKbn, String kianHimodukeFlg){
		/*
		 * 伝票状態により更新方法を変える
		 */
		if (denpyouJoutai.equals(DENPYOU_JYOUTAI.KIHYOU_CHUU)) {
			// 起票中の場合は、初期レコードを登録する。
			tourokuDenpyouKianHimozuke(denpyouId, kianDenpyouId, kianDenpyouKbn, kianHimodukeFlg);
		}
		if (denpyouJoutai.equals(DENPYOU_JYOUTAI.SHINSEI_CHUU)) {
			// 申請中の場合は、レコードを更新する。
			updateDenpyouKianHimozuke(denpyouId, denpyouKbn, kianDenpyouId[0], kianDenpyouKbn[0]);
		}
	}

	/**
	 * 起案伝票に関するチェック
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @param kianHimodukeFlg 起案紐づけフラグ
	 * @param kianDenpyouId 起案伝票ID配列
	 * @param errorList エラーリスト
	 */
	protected void kianDenpyouCheck(
			String denpyouKbn, String denpyouId, String kianHimodukeFlg,
			String[] kianDenpyouId, List<String> errorList) {
		boolean isCheck = true;
		/*
		 * 起案伝票の添付必須チェックを行う。
		 */

		// 伝票の「予算執行対象」を取得する。
		String yosanShikkouTaishouCd = denpyouKanriLogic.getYosanShikkouTaishou(denpyouKbn);

		// 支出起案および支出依頼の場合にチェックする。
		switch (yosanShikkouTaishouCd){
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI:
			// 支出依頼の場合、

			// 伝票起案情報を取得する。
			GMap mapDenpyouKianHimozuke = selectDenpyouKianHimozuke(denpyouId);
			if (null == mapDenpyouKianHimozuke){
				// 伝票状態が未起票であれば、紐付けフラグを参照してチェックを制御する。
				if ("0".equals(kianHimodukeFlg)) {
					// 起案と紐付けない場合はチェックしない
					isCheck = false;
				}
			}else{
				if (isEmpty(mapDenpyouKianHimozuke.get("jisshi_kian_bangou"))){
					// 伝票の伝票起案情報に実施起案番号の設定がない場合は起案引継しないため
					// チェックは実施しない
					isCheck = false;
				}
			}
			break;

		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN:
			// 支出起案
			break;

		default:
			isCheck = false;
			break;
		}

		// 起案伝票を添付したか確認する。
		if (isCheck){
			if (isEmpty(kianDenpyouId[0])){
				errorList.add("起案伝票が添付されていません。");
			}
			
			//202208 tuika
			if (!isEmpty(kianDenpyouId[0])){
				for(int i = 0;i < kianDenpyouId.length;i++) {
					if(!kianDenpyouId[i].equals("")) {
						GMap forDenpyouJoutai = wfLogic.selectDenpyou(kianDenpyouId[i]);
						if(forDenpyouJoutai == null) {
							errorList.add("起案伝票ID[" + kianDenpyouId[i]+"]は存在しません。"); 
						}else {
							String joutai = forDenpyouJoutai.get("denpyou_joutai").toString(); 
							if(!joutai.equals(DENPYOU_JYOUTAI.SYOUNIN_ZUMI)) { 
								errorList.add(kianDenpyouId[i]+"は承認解除されているため、起案伝票として選択できません。"); 
							}
						}
					}
				}
			}
			
		}
	}

	/**
	 * 起案添付セクション表示フラグの判定（0:表示しない 1:表示する）
	 * @param yosanShikkouTaishouCd 予算執行対象コード
	 * @param denpyouId 伝票ID
	 * @param existJisshiKianBangou 実施起案番号が存在するかどうか
	 * @return 起案添付セクション表示フラグ（0:表示しない 1:表示する）
	 */
	protected String judgeIsDispKiantenpuSection(String yosanShikkouTaishouCd, String denpyouId, boolean existJisshiKianBangou) {
		switch (yosanShikkouTaishouCd){
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN:
			// 支出起案
			return "1";
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI:
			// 支出依頼（確認ダイアログで表示可能になる）の場合、
			// 伝票に実施起案番号が引き継がれている場合に表示する。
			if (existJisshiKianBangou)
			{
				return "1";
			}
			break;
		default:
			// 実施起案、対象外
			break;
		}
		return "0";
	}

	/**
	 * 起案伝票紐付け確認フラグの判定（0:紐付け確認しない 1:紐付け確認する）
	 * @param yosanShikkouTaishouCd 予算執行対象コード
	 * @param denpyouId 伝票ID
	 * @return 起案伝票紐付け確認フラグ（0:紐付け確認しない 1:紐付け確認する）
	 */
	protected String judgeKianHimozukeFlg(String yosanShikkouTaishouCd, String denpyouId) {
		// 伝票の予算執行対象が支出依頼の場合、
		// 伝票状態が未起票であれば起案番号を引き継ぐ確認ダイアログを表示させる。
		// （画面側で伝票状態が未起票であるか確認して実施）
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishouCd))
		{
			return "1";
		}
		return "0";
	}

	/**
	 * 起案番号簿選択ダイアログ表示フラグの判定（0:表示しない 1:表示する）
	 * @param yosanShikkouTaishouCd 予算執行対象コード
	 * @return 起案番号簿選択ダイアログ表示フラグ（0:表示しない 1:表示する）
	 */
	protected String judgeIsDispKianbangouBoDialog(String yosanShikkouTaishouCd) {
		switch (yosanShikkouTaishouCd){
		// 実施起案
		// 支出起案
		// 対象外
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN:
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN:
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI:
			return "1";

		// 支出依頼
		default:
			return "0";
		}
	}

	/**
	 * 起案番号項目表示フラグの判定（0:表示しない 1:表示する 2:アンカー表示のみ）
	 * @param yosanShikkouTaishouCd 予算執行対象コード
	 * @param notExistJisshiKianBangou 実施起案番号が存在しないかどうか
	 * @return 起案番号項目表示フラグ（0:表示しない 1:表示する 2:アンカー表示のみ）
	 */
	protected String judgeIsDispKianbangou(String yosanShikkouTaishouCd, boolean notExistJisshiKianBangou) {
		// 予算執行対象により表示形式を変更する
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishouCd)){
			if (notExistJisshiKianBangou){
				// 伝票の伝票起案情報に実施起案番号の設定がない場合は起案引継しないため
				// 起案番号項目を表示しない
				return "0";
			}else {
				return "2";

			}
		}else{
			// 実施起案、支出起案、対象外
			return "1";
		}
	}

	/**
	 * 起案番号運用判定<br>
	 * 対象伝票が起案番号運用の対象かを判定する。
	 * <ul type="1">
	 * <li>予算執行管理オプションが
	 * <br>・「予算執行管理Aあり」の場合は 2 を確認
	 * <br>・上記以外の場合は対象外で確定</li>
	 * <li>伝票の「起案番号運用フラグ」が
	 * <br>・ 1(Checkbox=ON) の場合は一旦対象とする
	 * <li>伝票の「予算執行対象」が
	 * <br>・対象外以外でかつ、「起案番号運用フラグ」が0(Checkbox=OFF) なら対象で確定
	 * <br>・上記以外は対象外で確定</li>
	 * </ul>
	 * @param denpyouKbn 伝票区分
	 * @return true:対象 false:対象外
	 */
	protected boolean isUsableKianBangou(String denpyouKbn) {
		boolean result = false;

		/*
		 * 予算執行管理オプションを確認する。
		 */
		String optionCd = RegAccess.checkEnableYosanShikkouOption();
		if (RegAccess.YOSAN_SHIKKOU_OP.A_OPTION.equals(optionCd)){
			// 「予算執行管理Aあり」は対象

			/*
			 * 伝票の「起案番号運用フラグ」を確認する。
			 */
			String kianBangouUnyouCd = denpyouKanriLogic.getKianBangouUnyou(denpyouKbn);
			if ("1".equals(kianBangouUnyouCd)){
				// する（Checkbox=ON）は対象
				result = true;
			}

			/*
			 * 伝票の「予算執行対象」を確認する。
			 */
			// 起案運用しない場合、対象外以外なら対象とする。
			if (!result){
				String yosanShikkouTaishouCd = denpyouKanriLogic.getYosanShikkouTaishou(denpyouKbn);
				if (!EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI.equals(yosanShikkouTaishouCd)){
					result = true;
				}
			}
		}

		return (result);
	}
	/**
	 * 表示用の起案番号文字列を取得する
	 * @param yosanShikkouTaishouCd 予算執行対象種別コード
	 * @param mapDenpyouKianHimozuke 伝票起案紐づけレコード
	 * @return 起案番号の表示内容
	 */
	protected String getHyoujikianBangou(String yosanShikkouTaishouCd, GMap mapDenpyouKianHimozuke) {
		switch (yosanShikkouTaishouCd){
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN:
			// 実施起案
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI:
			// 対象外
			if (null != mapDenpyouKianHimozuke.get("jisshi_kian_bangou")){
				return mapDenpyouKianHimozuke.get("jisshi_kian_bangou").toString();
			}
			break;
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN:
			// 支出起案
			if (null != mapDenpyouKianHimozuke.get("shishutsu_kian_bangou")){
				return mapDenpyouKianHimozuke.get("shishutsu_kian_bangou").toString();
			}
			break;
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI:
			// 支出依頼
			if (null != mapDenpyouKianHimozuke.get("jisshi_kian_bangou") || null != mapDenpyouKianHimozuke.get("shishutsu_kian_bangou")){
				return " ";
			}
			break;
		default:
			break;
		}
		return "";
	}

	/**
	 * 起案終了済み・未済かどうかを判定する
	 * @param yosanShikkouTaishouCd 予算執行対象種別コード
	 * @param usr ユーザー情報
	 * @param kihyouUserId 起票ユーザーID
	 * @param mapDenpyouKianHimozuke 伝票起案紐づけ情報
	 * @return 起案終了未済→"0"、済→"1"、そもそも起案終了対象外→ブランク
	 */
	protected String judgeKianShuuryou(String yosanShikkouTaishouCd, User usr, String kihyouUserId, GMap mapDenpyouKianHimozuke) {
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishouCd))
		{
			return "";
		}
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI.equals(yosanShikkouTaishouCd))
		{
			return "";
		}

		KaikeiCommonLogic kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		if (kaikeiCommonLogic.userIsKeiri(usr)){

			if (null == mapDenpyouKianHimozuke){
				// 伝票起案情報レコードが存在しない場合（未起票）は起案終了設定を可能にする。
				return "0";
			}else{
				return mapDenpyouKianHimozuke.get("kian_syuryo_flg").toString();
			}

		}else if (usr.getLoginUserId().equals(kihyouUserId)){
			// ログインユーザが起案者の場合

			if (null == mapDenpyouKianHimozuke){
				// 伝票起案情報レコードが存在しない場合（未起票）は起案終了設定を可能にする。
				return "0";
			}else{
				return mapDenpyouKianHimozuke.get("kian_syuryo_flg").toString();
			}
		}
		return "";
	}

	/**
	 * 起案終了／起案終了解除を表示するか判定する
	 * @param yosanShikkouTaishouCd 予算執行対象種別コード
	 * @param usr ユーザー情報
	 * @param kihyouUserId 起票ユーザーID
	 * @param isKaribaraiMishiyouSeisanZumi 仮払未使用で精算済みならtrue
	 * @param kianShuuryouFlg 起案終了フラグ
	 * @return 起案終了表示→"1"、非表示→"0"
	 */
	protected String judgeDispKianShuuryou(String yosanShikkouTaishouCd, User usr, String kihyouUserId, boolean isKaribaraiMishiyouSeisanZumi, String kianShuuryouFlg) {
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishouCd))
		{
			return "0";
		}
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI.equals(yosanShikkouTaishouCd))
		{
			return "0";
		}

		KaikeiCommonLogic kaikeiCommonLogic = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
		if (kaikeiCommonLogic.userIsKeiri(usr)){
			// ログインユーザが経理権限でログインしている場合、起案終了を表示する。
			// 但し、仮払金未使用で精算済みの仮払申請は表示しない。
			return isKaribaraiMishiyouSeisanZumi? "0" : "1";


		}else if (usr.getLoginUserId().equals(kihyouUserId)){
			// ログインユーザが起案者の場合
			// 起案終了未済であれば起案終了を表示する。
			if ("0".equals(kianShuuryouFlg)){
				return "1";
			}
		}
		return "0";

	}

	/**
	 * 予算チェック関連情報の表示制御判定
	 * @param denpyouKbn 伝票区分
	 * @return 予算チェックsecsion表示→true、非表示→false
	 */
	protected boolean isDispYosanCheckSection(String denpyouKbn) {
		// 予算チェック期間が通年の場合はfalse
		if(EteamConst.yosanCheckKikan.YEAR.equals(setting.yosanCheckKikan())) {
			return false;
		}

		// 伝票の「予算執行対象」を取得する。
		String yosanShikkouTaishouCd = denpyouKanriLogic.getYosanShikkouTaishou(denpyouKbn);

		// 判定
		switch (yosanShikkouTaishouCd){
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN:
		case EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI:
			// 実施起案
			// 支出依頼
			return true;

		default:
			// 支出起案
			// 対象外		---予算執行オプション=Aでない場合は全伝票が対象外なのでここに該当する。
			return false;
		}
	}

	/**
	 * 起案追加できる伝票情報をリスト型で取得
	 * @param denpyouKbn 伝票区分
	 * @param kihyouBumonCd 起票部門コード
	 * @return 伝票情報
	 */
	protected List<GMap> loadKianTsuikaDenpyou(String denpyouKbn, String kihyouBumonCd){

		// 伝票の「予算執行対象」を取得する。
		String yosanShikkouTaishouCd = denpyouKanriLogic.getYosanShikkouTaishou(denpyouKbn);

		// 検索条件を指定する。
		KianTsuikaLogic.SearchCondition cond = new KianTsuikaLogic.SearchCondition();

		// 予算執行対象
		if (!isEmpty(yosanShikkouTaishouCd)){
			cond.yosanShikkouTaishou = yosanShikkouTaishouCd;
		}

		// 起案部門
		cond.kihyouBumonCd = kihyouBumonCd;

		return this.kianTsuikaLogic.kensaku(cond);
	}

	/**
	 * 起票時、伝票種別の有効期限をチェックする。
	 * 有効期限外であればエラー
	 * @param w WF制御
	 */
	protected void checkDenpyouShubetsuYuukouKigen(WfSeigyo w) {
		// 有効期限内の伝票種別が見つかれば、問題ないため処理終了
		GMap denpyouShubetsu = kihyouLogic.findValidDenpyouKanri(w.denpyouKbn);
		if (denpyouShubetsu != null)
		{
			return;
		}

		// 伝票種別が有効期限切れであった場合のエラー
		denpyouShubetsu = kihyouLogic.findDenpyouKanri(w.denpyouKbn);
		if(denpyouShubetsu != null) {
			// 新規起票時
			if(StringUtils.isEmpty(w.sanshouDenpyouId)) throw new EteamAccessDeniedException("伝票種別「"+denpyouShubetsu.get("denpyou_shubetsu")+"」は有効期限外の為、起票ができません。");
			// 参照起票時
			if(StringUtils.isNotEmpty(w.sanshouDenpyouId)) throw new EteamAccessDeniedException("伝票種別「"+denpyouShubetsu.get("denpyou_shubetsu")+"」は有効期限外の為、参照起票ができません。");
		}else {
			// 伝票種別名が取得できない→起票時点では不正値(メッセージが伝票種別ではなく伝票区分であるのは、パラメータは後者のため)
			throw new EteamAccessDeniedException("伝票区分の送信値が不正です。");
		}
	}
	
	/** 
	* WorkflowEventControlのExt継承は各伝票に対して効果がないので、ExtLogicのDIで解決できるこっちに置く。
	* @param workflowEventControl Controlクラス
	*/
	protected void loadTenpuFilePreviewInternal(WorkflowEventControl workflowEventControl) {
		String fileNameSuffix = "_" + workflowEventControl.getSchemaName() + "_" + workflowEventControl.loginUserId + "_" + workflowEventControl.denpyouId + "_preview"; // 将来的なUploadの一時保存と被らせたくないのでpreviewである旨明記
		
		File tmpFolder = new File("C:\\Apache24\\htdocs\\eteam\\static\\tmp");
		
		// 一時ファイル保存用フォルダーが存在するなら最終アクセスから1週間以上経過している自分自身用の一時ファイルを削除
		// 自分の一時ファイル全て→複数タブで開かれると開いているタブ用の一時ファイルが消える、
		// 自分の今の伝票のみ→一時ファイルのジャンクが増え続ける
		// 両者のトレードオフで仮に「1週間」とする
		// 期間設定を変更する場合、Windws Updateを正常にかけている限りでは、ここの値は最大でも1か月+バッファがあればその間に1度はOS再起動しているはずなので十分と思われる
		if(tmpFolder.exists() && tmpFolder.listFiles() != null)
		{
			String fileNameUserPart = "_" + workflowEventControl.getSchemaName() + "_" + workflowEventControl.loginUserId;
			for(File file : tmpFolder.listFiles())
			{
				String targetFileName = file.getName().replace(" ","_").replace("+","_").replace("&", "and").replace("=", "eq");// プラスなどのクエリ用文字は対策
				if(targetFileName.contains(fileNameUserPart) && targetFileName.contains("_preview"))
				{
					Path filePath = Paths.get(file.getAbsolutePath());
					BasicFileAttributes attrs;
					try
					{
						attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
						FileTime time = attrs.lastAccessTime();
						
						if(Instant.now().getEpochSecond() - time.toInstant().getEpochSecond() > 86400 * 7) // 1day=86400secs
						{
							Files.delete(filePath);
						};
					}
					catch (Exception e)
					{
						// e.printStackTrace(); デバッグで必要なら使用
						workflowEventControl.errorList.add("一時ファイルの読み込みに失敗しました。");
						return;
					}
				}
			}
		}
		else
		{
			tmpFolder.mkdirs();
		}
		
		// ファイルの読み込みと一時保存先への保存
		var tenpuFileList = workflowEventControl.tenpuFileDao.load(workflowEventControl.denpyouId);
		var ebunshoList = workflowEventControl.ebunshoFileDao.load(workflowEventControl.denpyouId);

		var fileNameList = new ArrayList<String>();
		var fileExtensionList = new ArrayList<String>();

		for(TenpuFile tenpuFile : tenpuFileList)
		{
			var tenpuFileNameForPreview = tenpuFile.fileName.replace(" ","_").replace("+","_").replace("&", "and").replace("=", "eq"); // 半角スペースを含むファイルについて、リンクに直すとNBSPに勝手に置き換わってしまうのでスペース自体を回避 半角プラスについても追加
			var splitFileName = tenpuFileNameForPreview.split("\\.");
			var extension = splitFileName[splitFileName.length - 1];

			tenpuFileNameForPreview = tenpuFileNameForPreview.substring(0, tenpuFileNameForPreview.length() - extension.length() - (splitFileName.length > 1 ? 1 : 0)) + "_" + extension + fileNameSuffix;
			fileNameList.add(tenpuFileNameForPreview);

			Optional<EbunshoFile> ebunsho = ebunshoList.stream().filter(item -> item.edano == tenpuFile.edano).findFirst();

			var binaryData = ebunsho.isEmpty() ? tenpuFile.binaryData : ebunsho.get().binaryData;

			extension = (ebunsho.isEmpty() || !ebunsho.get().denshitorihikiFlg.equals(ebunsho.get().tsfuyoFlg)) ? extension : "pdf";
			fileExtensionList.add(extension);

			var fullFilePath = tmpFolder.getAbsolutePath() + "\\" + tenpuFileNameForPreview + "." + extension;
			
			try(OutputStream outputStream = new FileOutputStream(new File(fullFilePath)))
			{
				StopWatch stopWatch = new StopWatch();
				stopWatch.start();
				workflowEventControl.inputStream = new ByteArrayInputStream(binaryData);
				byte[] buffer = workflowEventControl.inputStream.readAllBytes(); // バッファとして使用するバイト配列を定義
				outputStream.write(buffer); // 読み込んだバイト数だけをoutputStreamに書き出す
				workflowEventControl.inputStream.close();
				log.info("ファイル処理（共通高速化）： " + (stopWatch.getTime()/1000f) + " 秒。");
				stopWatch.stop();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		// 確定したリストの格納
		workflowEventControl.tenpuFileName = fileNameList.toArray(new String[fileNameList.size()]);
		workflowEventControl.tenpuFileExtension = fileExtensionList.toArray(new String[fileExtensionList.size()]);

		// 絶対パス用に必要なプロパティのセット
		HttpServletRequest request = ServletActionContext.getRequest();
		workflowEventControl.scheme = request.getScheme(); // "http" または "https"
		var serverName = request.getServerName(); // ホスト名
		var serverPort = request.getServerPort(); // ポート番号

		workflowEventControl.host = serverName + ":" + serverPort; // "location.host"に相当

	}
}
