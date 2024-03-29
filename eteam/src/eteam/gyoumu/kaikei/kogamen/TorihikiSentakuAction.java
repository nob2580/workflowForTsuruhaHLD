package eteam.gyoumu.kaikei.kogamen;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamConst;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.dao.BumonMasterDao;
import eteam.gyoumu.kaikei.KaikeiCommonLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 取引選択ダイアログAction
 */
@Getter @Setter @ToString
public class TorihikiSentakuAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
	//必ずある
	/** 伝票区分(表示パラメータ) */
	String denpyouKbn;
	//登録済ならある
	/** 伝票ID(表示パラメータ) */
	String denpyouId;

	//起票部門コード（プルダウン）の値 ※経費立替精算の時は使用者のuser_idから引き直して上書く
	/** 部門コード(表示パラメータ) */
	String bumonCd;
	/** 代表負担部門コード(表示パラメータ) */
	String daihyouFutanBumonCd;

	//AJAX getNameの時のみ
	/** 仕訳枝番号(表示パラメータ) */
	int shiwakeEdaNo;

	//経費立替精算・旅費精算・旅費仮払申請・海外旅費精算・海外旅費仮払申請の時、使用者のuser_idが渡される ※パラメータ渡されなければ起票者のuser_idで上書く
	/** 使用者ユーザーID(表示パラメータ---なければセッションの申請者本人) */
	String userId;

//＜画面入力以外＞
	/** 取引リスト */
	List<GMap> list;
	/** ユーザー情報 */
	GMap usrInfo;
	/** 社員コード */
	String shainCd;

//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(denpyouKbn, 4, 4,  "伝票区分", true); // KEY
		checkString(denpyouId, 19, 19, "伝票ID", true); // KEY
		checkString(bumonCd, 1, 8,  "部門コード", true); // KEY
		checkString(daihyouFutanBumonCd, 1, 8,  "代表負担部門コード", true); // KEY
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] itemList = {
			//項目						EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
			{denpyouKbn ,"伝票区分"		,"2"}, // KEY
			{bumonCd ,"部門コード"	,"2"}, // KEY
		};
		hissuCheckCommon(itemList, eventNum);
	}

//＜イベント＞
	/**
	 * @return 処理結果
	 */
	public String init() {

		//1.入力チェック
		formatCheck();
		hissuCheck(1);
		//errorならば既に例外発生のはず

		//2.データ存在チェック
		//3.アクセス可能チェック
		//なし

		// 取引リスト取得
		return getTorihikiList();
	}

	/**
	 * 名称取得イベント
	 * @return 処理結果
	 */
	public String getName() {

		String ret = getTorihikiList();

		try {

			String[] dataAry = new String[53];

			for (int i = 0; i < dataAry.length; i++) {
				dataAry[i] = "";
			}

			if (("success").equals(ret)) {

				setName();

				for (GMap record : list) {

					int no = Integer.parseInt(o2s(record.get("shiwake_edano")));
					int m = 0;

					if (no == shiwakeEdaNo) {
						dataAry[m++] = o2s(record.get("torihiki_name")); // 取引名
						dataAry[m++] = o2s(record.get("shiwake_edano")); // 仕訳枝番号
						dataAry[m++] = o2s(record.get("kari_kamoku_cd")); // 科目コード
						dataAry[m++] = o2s(record.get("kamoku_name_ryakushiki")); // 科目名
						dataAry[m++] = o2s(record.get("kari_kamoku_edaban_cd")); // 科目枝番号
						dataAry[m++] = o2s(record.get("edaban_name")); // 科目枝番号名
						dataAry[m++] = o2s(record.get("kari_futan_bumon_cd")); // 負担部門コード
						dataAry[m++] = o2s(record.get("futan_bumon_name")); // 負担部門名
						dataAry[m++] = o2s(record.get("daihyouFutanBumonFlg")); // 代表負担部門フラグ
						dataAry[m++] = o2s(record.get("kari_torihikisaki_cd")); // 取引先コード
						dataAry[m++] = o2s(record.get("torihikisaki_name_ryakushiki")); // 取引先名
						dataAry[m++] = o2s(record.get("kari_project_cd")); // プロジェクトコード
						dataAry[m++] = o2s(record.get("project_name")); // プロジェクト名
						dataAry[m++] = o2s(record.get("kari_segment_cd")); // セグメントコード
						dataAry[m++] = o2s(record.get("segment_name_ryakushiki")); // セグメント名
						dataAry[m++] = o2s(record.get("furikomisaki")); // 振込先
						dataAry[m++] = o2s(record.get("kari_kazei_kbn")); // 課税区分
						dataAry[m++] = o2s(record.get("kousaihi_hyouji_flg")); // 交際費表示フラグ
						dataAry[m++] = o2s(record.get("kousaihi_ninzuu_riyou_flg")); // 人数項目表示フラグ
						dataAry[m++] = o2s(record.get("kake_flg")); // 掛けフラグ
						dataAry[m++] = o2s(record.get("shain_cd_renkei_flg")); // 社員コード連携フラグ
						dataAry[m++] = shainCd; // 社員コード
						dataAry[m++] = o2s(record.get("tekiyou_flg")); // 摘要フラグ
						dataAry[m++] = o2s(record.get("tekiyou")); // 摘要
						dataAry[m++] = o2s(record.get("shori_group")); // (科目)処理グループ
						dataAry[m++] = o2s(record.get("kazei_flg")); // 課税フラグ 1:課税 0:非課税
						dataAry[m++] = o2s(record.get("kazei_flg_kamoku")); // 科目課税フラグ 1:課税 0:非課税
						dataAry[m++] = o2s(record.get("kari_uf1_cd")); // UF1コード
						dataAry[m++] = o2s(record.get("kari_uf1_name_ryakushiki")); // UF1名
						dataAry[m++] = o2s(record.get("kari_uf2_cd")); // UF2コード
						dataAry[m++] = o2s(record.get("kari_uf2_name_ryakushiki")); // UF2名
						dataAry[m++] = o2s(record.get("kari_uf3_cd")); // UF3コード
						dataAry[m++] = o2s(record.get("kari_uf3_name_ryakushiki")); // UF3名
						dataAry[m++] = o2s(record.get("kari_uf4_cd"));
						dataAry[m++] = o2s(record.get("kari_uf4_name_ryakushiki"));
						dataAry[m++] = o2s(record.get("kari_uf5_cd"));
						dataAry[m++] = o2s(record.get("kari_uf5_name_ryakushiki"));
						dataAry[m++] = o2s(record.get("kari_uf6_cd"));
						dataAry[m++] = o2s(record.get("kari_uf6_name_ryakushiki"));
						dataAry[m++] = o2s(record.get("kari_uf7_cd"));
						dataAry[m++] = o2s(record.get("kari_uf7_name_ryakushiki"));
						dataAry[m++] = o2s(record.get("kari_uf8_cd"));
						dataAry[m++] = o2s(record.get("kari_uf8_name_ryakushiki"));
						dataAry[m++] = o2s(record.get("kari_uf9_cd"));
						dataAry[m++] = o2s(record.get("kari_uf9_name_ryakushiki"));
						dataAry[m++] = o2s(record.get("kari_uf10_cd"));
						dataAry[m++] = o2s(record.get("kari_uf10_name_ryakushiki"));
						dataAry[m++] = o2s(record.get("kari_zeiritsu"));
						dataAry[m++] = o2s(record.get("kari_keigen_zeiritsu_kbn"));
						dataAry[m++] = o2s(record.get("kari_bunri_kbn")); //分離区分
						dataAry[m++] = o2s(record.get("kari_shiire_kbn")); //仕入区分
						dataAry[m++] = o2s(record.get("old_kari_kazei_kbn")); //旧課税区分
						dataAry[m++] = o2s(record.get("old_kazei_flg")); // 課税フラグ 1:課税 0:非課税
						break;
					}
				}
			}

			// 配列を改行区切りで文字列化
			String data = String.join("\t", dataAry);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/plain; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(data);
			out.flush();
			out.close();

		} catch (IOException ex) {
		} finally {
		}

		return "success";
	}

	/**
	 * ヌルはブランクへ、それ以外ならそのまま。
	 * @param s 変換前
	 * @return 変換後
	 */
	protected String o2s(Object s) {
		return (null == s) ? "" : s.toString();
	}

	/**
	 * リスト取得
	 * @return 処理結果
	 */
	protected String getTorihikiList() {
		try(EteamConnection connection = EteamConnection.connect()) {
			KaikeiCategoryLogic lg = EteamContainer.getComponent(KaikeiCategoryLogic.class, connection);
			KaikeiCommonLogic commonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
			BumonUserKanriCategoryLogic bumonUsrLg = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);

			// -----------------------------------
			// 該当明細の起票者情報を決定
			// -----------------------------------

			// 経費立替精算・旅費精算・旅費仮払申請・海外旅費精算・海外旅費仮払申請の代理起票なら、明細単位に選択されたユーザーIDを元に部門情報を取る
			// ※※しかし、起票者=使用者の場合は通常起票と同じく起票部門の情報を取得する※※
			String kihyouUserId = isEmpty(denpyouId) ? getUser().getSeigyoUserId() : commonLg.findKihyouUser(denpyouId).get("user_id");
			if (! isEmpty(userId) && !(kihyouUserId.equals(userId))) {
				GMap userBumon = bumonUsrLg.selectValidShozokuBumonRole(userId).get(0);
				bumonCd = (String)userBumon.get("bumon_cd");
				daihyouFutanBumonCd = (String)userBumon.get("daihyou_futan_bumon_cd");

			// それ以外なら、伝票共通ヘッダー部の起票部門から渡されたパラメータの部門情報のまま
			} else {
				// 未起票なら起票者ユーザーID
				if (StringUtils.isEmpty(denpyouId)) {
					userId = getUser().getSeigyoUserId();
				// 起票済なら承認ルートのユーザーIDを取ってくる
				} else {
					GMap kihyouUser = commonLg.findKihyouUser(denpyouId);
					userId = kihyouUser.get("user_id").toString();
				}
			}

			// -----------------------------------
			//仕訳パターンマスターから伝票区分に紐付く取引取得
			list = lg.loadTorihiki(denpyouKbn, bumonCd);
			// -----------------------------------
			if(list.isEmpty()){
				errorList.add("検索結果が0件でした。");
				return "error";
			}

			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * 取得したリストに名称付与
	 */
	protected void setName()
	{
		try(EteamConnection connection = EteamConnection.connect()) {

			KaikeiCommonLogic commonLg = EteamContainer.getComponent(KaikeiCommonLogic.class, connection);
			MasterKanriCategoryLogic masterLg = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
			BumonUserKanriCategoryLogic bumonUsrLg = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
			SystemKanriCategoryLogic sysLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
			BumonMasterDao bumonMasterDao = EteamContainer.getComponent(BumonMasterDao.class, connection);

			//代表負担部門名
			String daihyouFutanBumonName = masterLg.findFutanBumonName(daihyouFutanBumonCd);
			//代表負担部門に紐づいた仕入区分
			var bumonMasterDto = bumonMasterDao.find(daihyouFutanBumonCd);
			String daihyouFutanBumonShiireKbn = bumonMasterDto == null ||  bumonMasterDto.shiireKbn == null ? "" : bumonMasterDto.shiireKbn.toString();
			
			// 社員コード
			usrInfo = bumonUsrLg.selectUserInfo(userId);
			shainCd = (String)usrInfo.get("shain_no");

			// -----------------------------------
			//取引単位に変数変換等
			// -----------------------------------
			for (GMap record : list) {

				// 仕訳枝番号の合致するレコードのみ処理
				int no = Integer.parseInt(o2s(record.get("shiwake_edano")));
				if (no != shiwakeEdaNo) {
					continue;
				}

				boolean isDaihyouBumon = EteamConst.ShiwakeConst.DAIHYOUBUMON.equals(record.get("kari_futan_bumon_cd"));
				boolean isSyokiDaihyou = EteamConst.ShiwakeConst.SYOKIDAIHYOU.equals(record.get("kari_futan_bumon_cd"));

				//借方負担部門コード=<DAIHYOUBUMON>もしくは<SHOKIDAIHYOU>は変換が必要
				if (isDaihyouBumon || isSyokiDaihyou) {
					record.put("kari_futan_bumon_cd", daihyouFutanBumonCd);
					record.put("futan_bumon_name", daihyouFutanBumonName);
					if(!isEmpty(daihyouFutanBumonShiireKbn)) {
						record.put("kari_shiire_kbn", daihyouFutanBumonShiireKbn);
					}
				}

				record.put("daihyouFutanBumonFlg", isSyokiDaihyou ? EteamConst.ShiwakeConst.SYOKIDAIHYOU : "");

				// 取引先は借・貸1-5のどれかが「任意取引先」なら、それを採用。「任意取引先」以外の場合は借方を採用
				String torihikisakiCd = (String)record.get("kari_torihikisaki_cd");
				if (EteamConst.ShiwakeConst.TORIHIKI.equals(record.get("kashi_torihikisaki_cd1"))) torihikisakiCd = (String)record.get("kashi_torihikisaki_cd1");
				if (EteamConst.ShiwakeConst.TORIHIKI.equals(record.get("kashi_torihikisaki_cd2"))) torihikisakiCd = (String)record.get("kashi_torihikisaki_cd2");
				if (EteamConst.ShiwakeConst.TORIHIKI.equals(record.get("kashi_torihikisaki_cd3"))) torihikisakiCd = (String)record.get("kashi_torihikisaki_cd3");
				if (EteamConst.ShiwakeConst.TORIHIKI.equals(record.get("kashi_torihikisaki_cd4"))) torihikisakiCd = (String)record.get("kashi_torihikisaki_cd4");
				if (EteamConst.ShiwakeConst.TORIHIKI.equals(record.get("kashi_torihikisaki_cd5"))) torihikisakiCd = (String)record.get("kashi_torihikisaki_cd5");
				record.put("kari_torihikisaki_cd", torihikisakiCd);
				// 取引先コードを元に振込先情報の取得
				if(!EteamConst.ShiwakeConst.TORIHIKI.equals(record.get("kari_torihikisaki_cd")) && !"".equals(record.get("kari_torihikisaki_cd"))) {
					record.put("torihikisaki_name_ryakushiki", masterLg.findTorihikisakiName(torihikisakiCd));
					record.put("furikomisaki", commonLg.furikomisakiSakusei(torihikisakiCd));
				}

				// プロジェクトは借・貸1-5のどれかが「任意プロジェクト」なら、それを採用。「任意プロジェクト」以外の場合は借方を採用
				String projectCd = (String)record.get("kari_project_cd");
				if (EteamConst.ShiwakeConst.PROJECT.equals(record.get("kashi_project_cd1"))) projectCd = (String)record.get("kashi_project_cd1");
				if (EteamConst.ShiwakeConst.PROJECT.equals(record.get("kashi_project_cd2"))) projectCd = (String)record.get("kashi_project_cd2");
				if (EteamConst.ShiwakeConst.PROJECT.equals(record.get("kashi_project_cd3"))) projectCd = (String)record.get("kashi_project_cd3");
				if (EteamConst.ShiwakeConst.PROJECT.equals(record.get("kashi_project_cd4"))) projectCd = (String)record.get("kashi_project_cd4");
				if (EteamConst.ShiwakeConst.PROJECT.equals(record.get("kashi_project_cd5"))) projectCd = (String)record.get("kashi_project_cd5");
				record.put("kari_project_cd", projectCd);
				// プロジェクトコードを元に名称を取得
				if(!EteamConst.ShiwakeConst.PROJECT.equals(record.get("kari_project_cd")) && !"".equals(record.get("kari_project_cd"))) {
					record.put("project_name", masterLg.findProjectName(projectCd));
				}

				// セグメントは借・貸1-5のどれかが「任意セグメント」なら、それを採用。「任意セグメント」以外の場合は借方を採用
				String segmentCd = (String)record.get("kari_segment_cd");
				if (EteamConst.ShiwakeConst.SEGMENT.equals(record.get("kashi_segment_cd1"))) segmentCd = (String)record.get("kashi_segment_cd1");
				if (EteamConst.ShiwakeConst.SEGMENT.equals(record.get("kashi_segment_cd2"))) segmentCd = (String)record.get("kashi_segment_cd2");
				if (EteamConst.ShiwakeConst.SEGMENT.equals(record.get("kashi_segment_cd3"))) segmentCd = (String)record.get("kashi_segment_cd3");
				if (EteamConst.ShiwakeConst.SEGMENT.equals(record.get("kashi_segment_cd4"))) segmentCd = (String)record.get("kashi_segment_cd4");
				if (EteamConst.ShiwakeConst.SEGMENT.equals(record.get("kashi_segment_cd5"))) segmentCd = (String)record.get("kashi_segment_cd5");
				record.put("kari_segment_cd", segmentCd);
				// セグメントコードを元に名称を取得
				if(!EteamConst.ShiwakeConst.SEGMENT.equals(record.get("kari_segment_cd")) && !"".equals(record.get("kari_segment_cd"))) {
					record.put("segment_name_ryakushiki", masterLg.findSegmentName(segmentCd));
				}

				// UF1は借・貸1-5のどれかが「任意UF」なら、それを採用。「任意UF」以外の場合は借方を採用
				String uf1Cd = (String)record.get("kari_uf1_cd");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf1_cd1"))) uf1Cd = (String)record.get("kashi_uf1_cd1");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf1_cd2"))) uf1Cd = (String)record.get("kashi_uf1_cd2");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf1_cd3"))) uf1Cd = (String)record.get("kashi_uf1_cd3");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf1_cd4"))) uf1Cd = (String)record.get("kashi_uf1_cd4");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf1_cd5"))) uf1Cd = (String)record.get("kashi_uf1_cd5");
				record.put("kari_uf1_cd", uf1Cd);
				// UF1コードを元にUF1名を取得
				if(!EteamConst.ShiwakeConst.UF.equals(record.get("kari_uf1_cd")) && !"".equals(record.get("kari_uf1_cd"))) {
					record.put("kari_uf1_name_ryakushiki", masterLg.findUfName("1", uf1Cd));
				}
				// UF2は借・貸1-5のどれかが「任意UF」なら、それを採用。「任意UF」以外の場合は借方を採用
				String uf2Cd = (String)record.get("kari_uf2_cd");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf2_cd1"))) uf2Cd = (String)record.get("kashi_uf2_cd1");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf2_cd2"))) uf2Cd = (String)record.get("kashi_uf2_cd2");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf2_cd3"))) uf2Cd = (String)record.get("kashi_uf2_cd3");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf2_cd4"))) uf2Cd = (String)record.get("kashi_uf2_cd4");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf2_cd5"))) uf2Cd = (String)record.get("kashi_uf2_cd5");
				record.put("kari_uf2_cd", uf2Cd);
				// UF2コードを元にUF2名を取得
				if(!EteamConst.ShiwakeConst.UF.equals(record.get("kari_uf2_cd")) && !"".equals(record.get("kari_uf2_cd"))) {
					record.put("kari_uf2_name_ryakushiki", masterLg.findUfName("2", uf2Cd));
				}
				// UF3は借・貸1-5のどれかが「任意UF」なら、それを採用。「任意UF」以外の場合は借方を採用
				String uf3Cd = (String)record.get("kari_uf3_cd");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf3_cd1"))) uf3Cd = (String)record.get("kashi_uf3_cd1");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf3_cd2"))) uf3Cd = (String)record.get("kashi_uf3_cd2");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf3_cd3"))) uf3Cd = (String)record.get("kashi_uf3_cd3");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf3_cd4"))) uf3Cd = (String)record.get("kashi_uf3_cd4");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf3_cd5"))) uf3Cd = (String)record.get("kashi_uf3_cd5");
				record.put("kari_uf3_cd", uf3Cd);
				// UF3コードを元にUF3名を取得
				if(!EteamConst.ShiwakeConst.UF.equals(record.get("kari_uf3_cd")) && !"".equals(record.get("kari_uf3_cd"))) {
					record.put("kari_uf3_name_ryakushiki", masterLg.findUfName("3", uf3Cd));
				}

				// UF4は借・貸1-5のどれかが「任意UF」なら、それを採用。「任意UF」以外の場合は借方を採用
				String uf4Cd = (String)record.get("kari_uf4_cd");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf4_cd1"))) uf4Cd = (String)record.get("kashi_uf4_cd1");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf4_cd2"))) uf4Cd = (String)record.get("kashi_uf4_cd2");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf4_cd3"))) uf4Cd = (String)record.get("kashi_uf4_cd3");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf4_cd4"))) uf4Cd = (String)record.get("kashi_uf4_cd4");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf4_cd5"))) uf4Cd = (String)record.get("kashi_uf4_cd5");
				record.put("kari_uf4_cd", uf4Cd);
				// UF4コードを元にUF4名を取得
				if(!EteamConst.ShiwakeConst.UF.equals(record.get("kari_uf4_cd")) && !"".equals(record.get("kari_uf4_cd"))) {
					record.put("kari_uf4_name_ryakushiki", masterLg.findUfName("4", uf4Cd));
				}

				// UF5は借・貸1-5のどれかが「任意UF」なら、それを採用。「任意UF」以外の場合は借方を採用
				String uf5Cd = (String)record.get("kari_uf5_cd");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf5_cd1"))) uf5Cd = (String)record.get("kashi_uf5_cd1");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf5_cd2"))) uf5Cd = (String)record.get("kashi_uf5_cd2");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf5_cd3"))) uf5Cd = (String)record.get("kashi_uf5_cd3");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf5_cd4"))) uf5Cd = (String)record.get("kashi_uf5_cd4");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf5_cd5"))) uf5Cd = (String)record.get("kashi_uf5_cd5");
				record.put("kari_uf5_cd", uf5Cd);
				// UF5コードを元にUF5名を取得
				if(!EteamConst.ShiwakeConst.UF.equals(record.get("kari_uf5_cd")) && !"".equals(record.get("kari_uf5_cd"))) {
					record.put("kari_uf5_name_ryakushiki", masterLg.findUfName("5", uf5Cd));
				}

				// UF6は借・貸1-5のどれかが「任意UF」なら、それを採用。「任意UF」以外の場合は借方を採用
				String uf6Cd = (String)record.get("kari_uf6_cd");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf6_cd1"))) uf6Cd = (String)record.get("kashi_uf6_cd1");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf6_cd2"))) uf6Cd = (String)record.get("kashi_uf6_cd2");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf6_cd3"))) uf6Cd = (String)record.get("kashi_uf6_cd3");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf6_cd4"))) uf6Cd = (String)record.get("kashi_uf6_cd4");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf6_cd5"))) uf6Cd = (String)record.get("kashi_uf6_cd5");
				record.put("kari_uf6_cd", uf6Cd);
				// UF6コードを元にUF6名を取得
				if(!EteamConst.ShiwakeConst.UF.equals(record.get("kari_uf6_cd")) && !"".equals(record.get("kari_uf6_cd"))) {
					record.put("kari_uf6_name_ryakushiki", masterLg.findUfName("6", uf6Cd));
				}

				// UF7は借・貸1-5のどれかが「任意UF」なら、それを採用。「任意UF」以外の場合は借方を採用
				String uf7Cd = (String)record.get("kari_uf7_cd");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf7_cd1"))) uf7Cd = (String)record.get("kashi_uf7_cd1");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf7_cd2"))) uf7Cd = (String)record.get("kashi_uf7_cd2");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf7_cd3"))) uf7Cd = (String)record.get("kashi_uf7_cd3");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf7_cd4"))) uf7Cd = (String)record.get("kashi_uf7_cd4");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf7_cd5"))) uf7Cd = (String)record.get("kashi_uf7_cd5");
				record.put("kari_uf7_cd", uf7Cd);
				// UF7コードを元にUF7名を取得
				if(!EteamConst.ShiwakeConst.UF.equals(record.get("kari_uf7_cd")) && !"".equals(record.get("kari_uf7_cd"))) {
					record.put("kari_uf7_name_ryakushiki", masterLg.findUfName("7", uf7Cd));
				}

				// UF8は借・貸1-5のどれかが「任意UF」なら、それを採用。「任意UF」以外の場合は借方を採用
				String uf8Cd = (String)record.get("kari_uf8_cd");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf8_cd1"))) uf8Cd = (String)record.get("kashi_uf8_cd1");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf8_cd2"))) uf8Cd = (String)record.get("kashi_uf8_cd2");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf8_cd3"))) uf8Cd = (String)record.get("kashi_uf8_cd3");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf8_cd4"))) uf8Cd = (String)record.get("kashi_uf8_cd4");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf8_cd5"))) uf8Cd = (String)record.get("kashi_uf8_cd5");
				record.put("kari_uf8_cd", uf8Cd);
				// UF8コードを元にUF8名を取得
				if(!EteamConst.ShiwakeConst.UF.equals(record.get("kari_uf8_cd")) && !"".equals(record.get("kari_uf8_cd"))) {
					record.put("kari_uf8_name_ryakushiki", masterLg.findUfName("8", uf8Cd));
				}

				// UF9は借・貸1-5のどれかが「任意UF」なら、それを採用。「任意UF」以外の場合は借方を採用
				String uf9Cd = (String)record.get("kari_uf9_cd");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf9_cd1"))) uf9Cd = (String)record.get("kashi_uf9_cd1");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf9_cd2"))) uf9Cd = (String)record.get("kashi_uf9_cd2");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf9_cd3"))) uf9Cd = (String)record.get("kashi_uf9_cd3");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf9_cd4"))) uf9Cd = (String)record.get("kashi_uf9_cd4");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf9_cd5"))) uf9Cd = (String)record.get("kashi_uf9_cd5");
				record.put("kari_uf9_cd", uf9Cd);
				// UF9コードを元にUF9名を取得
				if(!EteamConst.ShiwakeConst.UF.equals(record.get("kari_uf9_cd")) && !"".equals(record.get("kari_uf9_cd"))) {
					record.put("kari_uf9_name_ryakushiki", masterLg.findUfName("9", uf9Cd));
				}

				// UF10は借・貸1-5のどれかが「任意UF」なら、それを採用。「任意UF」以外の場合は借方を採用
				String uf10Cd = (String)record.get("kari_uf10_cd");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf10_cd1"))) uf10Cd = (String)record.get("kashi_uf10_cd1");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf10_cd2"))) uf10Cd = (String)record.get("kashi_uf10_cd2");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf10_cd3"))) uf10Cd = (String)record.get("kashi_uf10_cd3");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf10_cd4"))) uf10Cd = (String)record.get("kashi_uf10_cd4");
				if (EteamConst.ShiwakeConst.UF.equals(record.get("kashi_uf10_cd5"))) uf10Cd = (String)record.get("kashi_uf10_cd5");
				record.put("kari_uf10_cd", uf10Cd);
				// UF10コードを元にUF10名を取得
				if(!EteamConst.ShiwakeConst.UF.equals(record.get("kari_uf10_cd")) && !"".equals(record.get("kari_uf10_cd"))) {
					record.put("kari_uf10_name_ryakushiki", masterLg.findUfName("10", uf10Cd));
				}

				// -----------------------------------
				//取引単位に課税フラグをセット
				// -----------------------------------
				String kazeiKbn = (String)record.get("kari_kazei_kbn");
				String kamokuCd = (String)record.get("kari_kamoku_cd");
				//旧課税区分を取得・セット
				String oldKazeiKbn = (String)record.get("old_kari_kazei_kbn");
				
				//借方課税区分に対する課税フラグ
				record.put("kazei_flg", sysLogic.judgeKazeiFlg(kazeiKbn, kamokuCd));
				//科目マスター.課税区分に対する課税フラグ
				record.put("kazei_flg_kamoku", sysLogic.judgeKazeiFlg(EteamNaibuCodeSetting.KAZEI_KBN.MISETTEI, kamokuCd));
				
				//旧借方課税区分に対する課税フラグ
				record.put("old_kazei_flg", sysLogic.judgeKazeiFlg(oldKazeiKbn, kamokuCd));
			}
		}
	}
}
