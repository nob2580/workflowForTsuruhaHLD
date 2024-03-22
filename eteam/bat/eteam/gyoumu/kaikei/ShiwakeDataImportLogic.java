package eteam.gyoumu.kaikei;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamIO;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.SHIWAKE_STATUS;
import eteam.common.SaibanLogic;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.KaikeiCategoryLogic;
import eteam.common.select.KaniTodokeCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.kaikei.open21.Open21CSetData;
import eteam.gyoumu.kaikei.open21.Open21LinkData;
import eteam.gyoumu.kaikei.open21.Open21ShiwakeData;
import eteam.gyoumu.kanitodoke.KanitodokeXlsLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 仕訳データインポートロジック
 */
public class ShiwakeDataImportLogic extends EteamAbstractLogic  {
	
	/** bmp拡張子 */
	protected static final String EXTENSION_BMP = "bmp";
	/** jpeg拡張子 */
	protected static final String EXTENSION_JPEG = "jpeg";
	/** jpg拡張子 */
	protected static final String EXTENSION_JPG = "jpg";
	/** png拡張子 */
	protected static final String EXTENSION_PNG = "png";
	/** doc拡張子 */
	protected static final String EXTENSION_DOC = "doc";
	/** docx拡張子 */
	protected static final String EXTENSION_DOCX = "docx";
	/** DB内DOCDOCX拡張子コード名 */
	protected static final String EXTENSION_SETTING_DOCDOCX = "DOCDOCX";
	/** xls拡張子 */
	protected static final String EXTENSION_XLS = "xls";
	/** xlsx拡張子 */
	protected static final String EXTENSION_XLSX = "xlsx";
	/** DB内XLSXLSX拡張子コード名 */
	protected static final String EXTENSION_SETTING_XLSXLSX = "XLSXLSX";
	/** 添付ファイル格納先フォルダ名前半部 */
	protected static final String IMP_APPENDFILE_FOLDER = "ImpFile_";
	
	/** SIASリンク情報件数最大値 */
	protected static final long LINK_COUNT_MAX = 9999;
	/** リンク名のサイズ(byte) */
	protected static final int LINKNAME_MAXBYTE = 40;

	/** 一時フォルダパス */
	protected String path;
	/** リンク情報利用有無 */
	protected boolean useLink;
	
	/** WF SELECT*/
	WorkflowCategoryLogic wf;
	/** 採番 */
	SaibanLogic saibanLogic;
	/** 会計カテゴリロジック */
	KaikeiCategoryLogic kaikeiLogic;

	@Override
	public void init(EteamConnection c) {
		super.init(c);
		path = setting.op21MparamCsvPath() + "\\" + EteamCommon.getContextSchemaName();
		useLink = "1".equals(setting.tenpuFileRenkei());
		wf = EteamContainer.getComponent(WorkflowCategoryLogic.class, c);
		saibanLogic = EteamContainer.getComponent(SaibanLogic.class, c);
		kaikeiLogic = EteamContainer.getComponent(KaikeiCategoryLogic.class, c);
	}

	/**
	 * 全伝票の仕訳データを取得します。
	 * @return 全ての仕訳データ
	 */
	public List<GMap> loadShiwakeData(String invoiceFlg) {
		boolean isWfLevelCommit = EteamConst.SHIWAKE_COMMIT_TYPE.DENPYOU_ID.equals(setting.shiwakeSakuseiErrorAction());
		Map<String, Boolean> isWfLevelRenkeiMap = getWfLevelRenkeiMap();

		List<GMap> retList = new ArrayList<GMap>();
		for (Entry<String, Boolean> entry : isWfLevelRenkeiMap.entrySet()) {
			List<GMap> tmpList = null;
			String denpyouKbn = entry.getKey();
			
			// 伝票単位コミットの場合
			if (isWfLevelCommit) {
				//type2,4
				if (entry.getValue()) {
					//type2 : 伝票種別毎申請伝票毎
					tmpList = loadShiwakeDataType2(denpyouKbn, invoiceFlg);
				} else {
					//type4 : 伝票種別毎同一伝票日付毎
					tmpList = loadShiwakeDataType4(denpyouKbn, invoiceFlg);
				}
			} else { // エラーが一つでもあればその伝票種別は連携しない場合
				if (entry.getValue()) {
					//type1 : 伝票種別毎申請伝票毎
					tmpList = loadShiwakeDataType1(denpyouKbn, invoiceFlg);
				} else {
					//type3 : 伝票種別毎同一伝票日付毎
					tmpList = loadShiwakeDataType3(denpyouKbn, invoiceFlg);
				}
			}
			retList.addAll(tmpList);
		}
		return retList;
	}

	/**
	 * 伝票作成単位のマップを取得する。
	 * @return 結果
	 */
	public Map<String, Boolean> getWfLevelRenkeiMap() {
		Map<String, Boolean> isWfLevelRenkeiMap = new HashMap<String, Boolean>();
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.KEIHI_TATEKAE_SEISAN , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA001()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.KARIBARAI_SHINSEI , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA002()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.SEIKYUUSHO_BARAI , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA003()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA004()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA005()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA006()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.FURIKAE_DENPYOU , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA007()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA008()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA009()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.KOUTSUUHI_SEISAN , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA010()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA011()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA012()));
		isWfLevelRenkeiMap.put(EteamNaibuCodeSetting.DENPYOU_KBN.SIHARAIIRAI , EteamConst.SHIWAKE_RENKEI_TYPE.DENPYOU_ID.equals(setting.denpyouSakuseiTaniA013()));
		return isWfLevelRenkeiMap;
	}

	/**
	 * 仕訳抽出データの抽出
	 * @param denpyouKbn 伝票区分
	 * @return 仕訳抽出データ
	 */
	protected List<GMap> loadShiwakeDataType1(String denpyouKbn, String invoiceFlg) {
		String joinStr = makeJoinStringByKbn(denpyouKbn);
		String joinWhereStr = makeJoinStringByInvoiceFlg(denpyouKbn,invoiceFlg);
		final String sql =
				 "SELECT "
				+ "  cast(substring(s.denpyou_id, 8, 4) as varchar(4)) as __group, "
				+ "  s.* "
				+ ( (Open21Env.getVersion() == Version.DE3 ) ? "FROM shiwake_de3 s ":"FROM shiwake_sias s ")
				+ joinStr
				+ "WHERE "
				+ "  shiwake_status = '0'  "
				+ ("1".equals(setting.shiwakeSakuseiSakihiduke()) ? "" : "  AND dymd <= current_date ")
				+ "  AND cast(substring(s.denpyou_id, 8, 4) as varchar(4)) = ? "
				+ joinWhereStr
				+ "ORDER BY "
				+ "  cast(substring(s.denpyou_id, 8, 4) as varchar(4)), "
				+ "  dcno, "
				+ "  s.denpyou_id, "
				+ "  serial_no ASC ";
		return connection.load(sql, denpyouKbn);
	}

	/**
	 * 仕訳抽出データの抽出
	 * @param denpyouKbn 伝票区分
	 * @return 仕訳抽出データ
	 */
	protected List<GMap> loadShiwakeDataType2(String denpyouKbn,String invoiceFlg) {
		String joinStr = makeJoinStringByKbn(denpyouKbn);
		String joinWhereStr = makeJoinStringByInvoiceFlg(denpyouKbn,invoiceFlg);
		final String sql =
				 "SELECT "
				+ "  s.denpyou_id as __group, "
				+ "  s.* "
				+ ( (Open21Env.getVersion() == Version.DE3 ) ? "FROM shiwake_de3 s ":"FROM shiwake_sias s ")
				+ joinStr
				+ "WHERE "
				+ "  shiwake_status = '0'  "
				+ ("1".equals(setting.shiwakeSakuseiSakihiduke()) ? "" : "  AND dymd <= current_date ")
				+ "  AND cast(substring(s.denpyou_id, 8, 4) as varchar(4)) = ? "
				+ joinWhereStr
				+ "ORDER BY "
				+ "  s.denpyou_id, "
				+ "  dcno, "
				+ "  serial_no ASC ";
		return connection.load(sql, denpyouKbn);
	}

	/**
	 * 仕訳抽出データの抽出
	 * @param denpyouKbn 伝票区分
	 * @return 仕訳抽出データ
	 */
	protected List<GMap> loadShiwakeDataType3(String denpyouKbn, String invoiceFlg) {
		String joinStr = makeJoinStringByKbn(denpyouKbn);
		String joinWhereStr = makeJoinStringByInvoiceFlg(denpyouKbn,invoiceFlg);
		final String sql =
				 "SELECT "
				+ "  cast(substring(s.denpyou_id, 8, 4) as varchar(4)) as __group, "
				+ "  s.* "
				+ ( (Open21Env.getVersion() == Version.DE3 ) ? "FROM shiwake_de3 s ":"FROM shiwake_sias s ")
				+ joinStr
				+ "WHERE "
				+ "  shiwake_status = '0'  "
				+ ("1".equals(setting.shiwakeSakuseiSakihiduke()) ? "" : "  AND dymd <= current_date ")
				+ "  AND cast(substring(s.denpyou_id, 8, 4) as varchar(4)) = ? "
				+ joinWhereStr
				+ "ORDER BY "
				+ "  cast(substring(s.denpyou_id, 8, 4) as varchar(4)), "
				+ "  dcno, "
				+ "  s.denpyou_id, "
				+ "  dymd, "
				+ "  serial_no ASC ";
		return connection.load(sql, denpyouKbn);
	}

	/**
	 * 仕訳抽出データの抽出
	 * @param denpyouKbn 伝票区分
	 * @return 仕訳抽出データ
	 */
	protected List<GMap> loadShiwakeDataType4(String denpyouKbn, String invoiceFlg) {
		String joinStr = makeJoinStringByKbn(denpyouKbn);
		String joinWhereStr = makeJoinStringByInvoiceFlg(denpyouKbn,invoiceFlg);
		final String sql =
				 "SELECT "
				+ "  cast(substring(s.denpyou_id, 8, 4) as varchar(4)) || '-' || dymd as __group, "
				+ "  s.* "
				+ ( (Open21Env.getVersion() == Version.DE3 ) ? "FROM shiwake_de3 s ":"FROM shiwake_sias s ")
				+ joinStr
				+ "WHERE "
				+ "  shiwake_status = '0'  "
				+ ("1".equals(setting.shiwakeSakuseiSakihiduke()) ? "" : "  AND dymd <= current_date ")
				+ "  AND cast(substring(s.denpyou_id, 8, 4) as varchar(4)) = ? "
				+ joinWhereStr
				+ "ORDER BY "
				+ "  cast(substring(s.denpyou_id, 8, 4) as varchar(4)), "
				+ "  dymd, "
				+ "  dcno, "
				+ "  s.denpyou_id, "
				+ "  serial_no ASC ";
		return connection.load(sql, denpyouKbn);
	}

	/**
	 * 仕訳データリストから、Open21連携用情報を作成します。
	 * @param dcList インポート単位の伝票番号-伝票ID-仕訳という構造のリスト
	 * @param invoiceFlg invoiceDenpyou
	 * @return Open21連携用情報
	 */
	public Open21ShiwakeData createOpen21ShiwakeData(DcnoList dcList, String invoiceFlg) {
		List<GMap> shiwakeDataList = dcList.getShiwakeList();

		// インポータ仕訳明細の生成
		List<Open21CSetData> shiwakeCSetList = new ArrayList<>();
		List<Open21LinkData> linkList = new ArrayList<>();
		String denpyouKbn = shiwakeDataList.get(0).get("denpyou_id").toString().substring(7, 11);

		//---------------------
		// 仕訳単位のセット(DE3)
		//---------------------
		if(Open21Env.getVersion() == Version.DE3 ){
			//shiwake_de3レコード→OPEN21 DLL用の形式に変換
			for (GMap detailData: shiwakeDataList) {
				shiwakeCSetList.add(makeShiwakeDetailData_DE3(detailData));
			}

		//---------------------
		// 仕訳単位のセット(SIAS)
		//---------------------
		}else if(Open21Env.getVersion() == Version.SIAS){
			//shiwake_siasレコード→OPEN21 DLL用の形式に変換
			List<Integer> linkNoSumi = new ArrayList<>();
			for (GMap detailData: shiwakeDataList) {
				//リンク番号は伝票番号の先頭仕訳のみに指定する
				Integer linkNo = dcList.get((String)detailData.get("dcno")).getLinkNo();
				if (linkNo != null) {
					if (! linkNoSumi.contains(linkNo)) {
						linkNoSumi.add(linkNo);
					} else {
						linkNo = null;
					}
				}
				shiwakeCSetList.add(makeShiwakeDetailData_SIAS(detailData, linkNo));
			}
			//ebunsho_dataレコード→OPEN21 DLL用の形式に変換
			for (Dcno dc : dcList) {
				if (dc.getLinkNo() != null) {
					linkList.addAll(makeLinkData(dc));
				}
			}
		}else{
			throw new RuntimeException("OPEN21バージョン取得異常");
		}

		//---------------------
		// 財務転記インポート単位のセット
		//---------------------
		Open21ShiwakeData shiwakeData = new Open21ShiwakeData();
		// 設定情報．設定名(ｵｰﾌﾟﾝ21ﾒﾓﾘ渡し引数(会社ｺｰﾄﾞ)) String
		shiwakeData.setCCOD(setting.op21MparamKaishaCd());
		// 設定情報．設定名(ｵｰﾌﾟﾝ21ﾒﾓﾘ渡し引数(処理区分)) Integer
		String prcFlg = setting.op21MparamShoriKbn();
		if (!StringUtils.isEmpty(prcFlg)) {
			shiwakeData.setPrcFlg(Integer.parseInt(prcFlg));
		}
		// 設定情報．設定名(ｵｰﾌﾟﾝ21ﾒﾓﾘ渡し引数(伝票形式)) Integer
		String dfuk = judgeDenpyouKeishiki(denpyouKbn);
		if (!StringUtils.isEmpty(dfuk)) {
			shiwakeData.setDFUK(Integer.parseInt(dfuk));
		}
		// 設定情報．設定名(ｵｰﾌﾟﾝ21ﾒﾓﾘ渡し引数(不良ﾃﾞｰﾀﾛｸﾞ)) Integer
		String logFlg = setting.op21MparamFuryouDataLog();
		if (!StringUtils.isEmpty(logFlg)) {
			shiwakeData.setLogFlg(Integer.parseInt(logFlg));
		}
		// 設定情報．設定名(ｵｰﾌﾟﾝ21ﾒﾓﾘ渡し引数(ﾛｸﾞ用ﾊﾟｽ)) String
		shiwakeData.setLogPath(setting.op21MparamLogPath());
		// 設定情報．設定名(ｵｰﾌﾟﾝ21ﾒﾓﾘ渡し引数(ﾛｸﾞﾌｧｲﾙ名)) String
		shiwakeData.setLogFname(setting.op21MparamLogFileNm());
		// 設定情報．設定名(ｵｰﾌﾟﾝ21ﾒﾓﾘ渡し引数(ﾚｲｱｳﾄNo)) Integer
		String rno = setting.op21MparamLayoutNo();
		if (!StringUtils.isEmpty(rno)) {
			shiwakeData.setRno(Integer.parseInt(rno));
			if(Open21Env.getVersion() == Version.DE3) {
				//SIASならインボイスフラグを確認して改めてrnoセット
				rno = "0".equals(invoiceFlg) ? "1" : "0";
				shiwakeData.setRno(Integer.parseInt(rno));
			}
			if(Open21Env.getVersion() == Version.SIAS) {
				//SIASならインボイスフラグを確認して改めてrnoセット
				rno = "0".equals(invoiceFlg) ? "2" : "1";
				shiwakeData.setRno(Integer.parseInt(rno));
			}
		}
		// 設定情報．設定名(ｵｰﾌﾟﾝ21ﾒﾓﾘ渡し引数(伝票入力ﾊﾟﾀｰﾝ)) Integer
		String ijpt = judgeNyuuryokuPattern(denpyouKbn);
		if (!StringUtils.isEmpty(ijpt)) {
			shiwakeData.setIJPT(Integer.parseInt(ijpt));
		}
		// 設定情報．設定名(ｵｰﾌﾟﾝ21ﾒﾓﾘ渡し引数(邦貨換算ﾌﾗｸﾞ)) Integer
		String kanzan = setting.op21MparamHoukaKanzanFlg();
		if (!StringUtils.isEmpty(kanzan)) {
			shiwakeData.setKanzan(Integer.parseInt(kanzan));
		}
		// 設定情報．設定名(ｵｰﾌﾟﾝ21ﾒﾓﾘ渡し引数(起動ﾕｰｻﾞｰ)) Integer
		String rucod = setting.op21MparamKidouUser();
		if (!StringUtils.isEmpty(rucod)) {
			shiwakeData.setRUCOD(Integer.parseInt(rucod));
		}
		// 設定情報．設定名(ｵｰﾌﾟﾝ21ﾒﾓﾘ渡し引数(仕訳区分)) Integer
		shiwakeData.setSKUBUN(EteamConst.OP21.SHIWAKE_KBN);

		// 入力確定日
		if ("1".equals(setting.shiwakeKbn())) {
			// 日次設定の場合
			shiwakeData.setKakutei("0");
		// 拡張設定の場合
		} else {
			// 「確定有無」を参照
			if ("1".equals(setting.kakuteiUmu())) {
				// 1(確定済にする)
				shiwakeData.setKakutei(new SimpleDateFormat("yyyy/MM/dd").format(new Date(System.currentTimeMillis())));
			}else{
				// 2(未確定データにする)
				shiwakeData.setKakutei("0");
			}
		}
		
		// 設定情報. 設定名(税率の扱い) Integer
		shiwakeData.setKeigen(2); //2固定（0：通常税率、1：軽減税率、2：軽減税率区分で判断）

		// 生成したインポータ仕訳明細 List
		shiwakeData.setSiwake(shiwakeCSetList);
		// 生成したe文書明細 List
		shiwakeData.setLink(linkList);
		return shiwakeData;
	}

	/**
	 * 仕訳レコードをインポート単位に整理
	 * 
	 * 以下のデータ構造のリストを作る
	 * インポート単位(DcnoList)
	 * └伝票番号(Dcno)
	 *   └伝票ID(Denpyou)
	 *     ├添付ファイル(e文書)・関連伝票(帳票、添付ファイル、支出起案、実施起案)・支出起案・実施起案
	 *     └仕訳
	 * 
	 * 引数の仕訳レコードは、伝票番号、伝票ID、serial_noの順が保たれるはず
	 * 
	 * @param shiwakeList 仕訳レコード
	 * @return インポート単位データ構造のリスト
	 */
	public List<DcnoList> createDcList (List<GMap> shiwakeList) {
		DcnoList dcnoList = new DcnoList();
		Map<String, Denpyou> dcnoDenpyouMap = new HashMap<String, Denpyou>();
		
		//--------------------
		//伝票番号
		//  └伝票ID
		//     ├添付ファイル(e文書)・関連伝票(帳票、添付ファイル、支出起案、実施起案）・支出起案・実施起案
		//     └仕訳
		//に構造化
		//--------------------
		for (GMap shiwake : shiwakeList) {
			String dcno = (String)shiwake.get("dcno");
			String denpyouId = (String)shiwake.get("denpyou_id");
			String dcnoDenpyouId = dcno + "-" + denpyouId;
			if (! dcnoList.contains(dcno)) {
				Dcno dcnoObj = new Dcno(dcno);
				dcnoList.add(dcnoObj);
			}
			Denpyou denpyou = dcnoDenpyouMap.get(dcnoDenpyouId);
			if (denpyou == null) {
				denpyou = new Denpyou(dcno, denpyouId, denpyouId, false);
				dcnoDenpyouMap.put(dcnoDenpyouId, denpyou);
				dcnoList.get(dcno).getDenpyouList().add(denpyou);
			}
			denpyou.getShiwakeList().add(shiwake);
		}
		
		//--------------------
		//伝票番号配下のリンク件数10000件以上なら伝票番号を分割する
		//仕訳の伝票番号も芋づる式に更新しておく
		//de3だとリンク件数が0なので分割されない
		//--------------------
		List<Dcno> newDcnoList = new ArrayList<Dcno>();
		for (Dcno orgDcObj : dcnoList) {
			long lnkCnt = orgDcObj.getLinkCount();
			if(lnkCnt > LINK_COUNT_MAX){
				newDcnoList.addAll(orgDcObj.division());
			}
		}
		//分割分も後ろにくっつけて１つの伝票番号リスト完成
		dcnoList.addAll(newDcnoList);

		//--------------------
		//インポート単位がリンク件数10000万件以内になるように分割
		//伝票番号1と伝票番号2を1回目、伝票番号2と伝票番号3を2回目、、、のように
		//de3だとリンク件数が0なので分割されない
		//--------------------
		List<DcnoList> ret = new ArrayList<>();
		DcnoList tmpDcnoList = new DcnoList();
		ret.add(tmpDcnoList);
		long linkCount = 0;
		for (Dcno d : dcnoList) {
			long tmpCount = d.getLinkCount();
			linkCount  += tmpCount;
			if (linkCount > LINK_COUNT_MAX) {
				tmpDcnoList = new DcnoList();
				ret.add(tmpDcnoList);
				linkCount = tmpCount;
			}
			tmpDcnoList.add(d);
		}

		//--------------------
		//伝票番号配下の伝票IDのどれかがリンク情報出力対象ならリンク番号を採番する
		//--------------------
		for (DcnoList dcnoSubList : ret) {
			int linkNoNext = 0;
			for (Dcno dcnoObj : dcnoSubList) {
				if (dcnoObj.getLinkCount() > 0) {
					dcnoObj.setLinkNo(++linkNoNext);
				}
			}
		}
		return ret;
	}

	/**
	 * 
	 * リンク番号(伝票番号)配下の伝票ID配下の添付ファイル(e文書)や関連伝票(帳票、添付ファイル)を、出力する。
	 * @param dcList インポート単位の伝票番号-伝票ID-仕訳という構造のリスト
	 */
	public void outLinkFiles(DcnoList dcList) {
		//リンク番号(伝票番号)単位ループ
		for (Dcno dc : dcList) {
			//リンク番号配下全ファイル出力
			if (dc.getLinkNo() != null) {
				//リンク番号が振られている(リンク出力するファイルあり)ので、リンク単位フォルダを作って、伝票番号配下のファイル全部出力
				String folder = path + "/" + IMP_APPENDFILE_FOLDER + dc.getLinkNo(); //c:\eteam\tmp\ImpFile_#
				new File(folder).mkdirs();
				dc.outFiles(folder);
			}
		}
	}

	/**
	 * 伝票番号リスト（インポート単位）
	 */
	@Getter @Setter @ToString
	public class DcnoList extends ArrayList<Dcno> {
		/**
		 * dcno登録済？
		 * @param dcno 伝票番号
		 * @return 登録済？
		 */
		public boolean contains(String dcno) {
			return get(dcno) != null;
		}
		
		/**
		 * 登録済dcno取得
		 * @param dcno 伝票番号
		 * @return 伝票番号OJB
		 */
		public Dcno get(String dcno) {
			for (int i = 0; i < size(); i++) {
				Dcno d = super.get(i);
				if (d.getDcno().equals(dcno)) {
					return d;
				}
			}
			return null;
		}
		
		/**
		 * 伝票番号リスト（インポート単位）配下の仕訳全部取得
		 * @return 仕訳全部
		 */
		public List<GMap> getShiwakeList() {
			List<GMap> ret = new ArrayList<>();
			for (Dcno dc : this) {
				for (Denpyou dp : dc.getDenpyouList()) {
					ret.addAll(dp.getShiwakeList());
				}
			}
			return ret;
		}
	}
	/**
	 * 伝票番号単位
	 */
	@Getter @Setter @ToString
	public class Dcno {
		/** 伝票番号 */
		String dcno;
		/** 伝票ID紐付け */
		List<Denpyou> denpyouList = new ArrayList<>();
		/** リンク番号 */
		Integer linkNo;
		
		/**
		 * 仕訳レコードの中から指定伝票番号配下の構造を取り出す
		 * @param dcno 伝票番号
		 */
		public Dcno(String dcno) {
			//自分自身の伝票番号覚えておく
			this.dcno = dcno;
			//配下の伝票は後で紐つける
		}
		
		/**
		 * 伝票番号配下の全伝票の添付ファイル(e文書)＋関連伝票(帳票と添付ファイル)を出力する
		 * @param folder 出力先フォルダ
		 */
		public void outFiles(String folder) {
			for (Denpyou denpyou : denpyouList) {
				//伝票ID配下ファイル出力
				denpyou.outFiles(folder);
			}
		}
		
		/**
		 * 伝票番号配下の全伝票のリンク数を取得
		 * @return long 合計リンク数
		 */
		public long getLinkCount(){
			long cnt = 0;
			for (Denpyou denpyou : denpyouList) {
				cnt += denpyou.getLinkCount();
			}
			return cnt;
		}
		
		/**
		 * 伝票番号配下のリンク情報件数をLINK_COUNT_MAXに収まるよう分割し、
		 * 分割後のデータ(自身を除く)を返却する
		 * @return List<Dcno> 分割後の追加するDcnoリスト
		 */
		public List<Dcno> division(){
			
			List<Dcno> addList = new ArrayList<Dcno>();
			Dcno addDcObj = null;
			long totalCnt = 0;
			String newDcno = dcno;
			
			//伝票毎のリンク数を逐次カウントしていく
			//①制限数を超過したら新規に転記用伝票番号を採番
			//②伝票データを新規伝票番号クラスに登録
			//新規伝票番号クラス側でもリンク数が超過したら更に①②を実行
			for (Denpyou denpyou : denpyouList) {
				long cnt = denpyou.getLinkCount();
				totalCnt += cnt;
				if( totalCnt > LINK_COUNT_MAX ){
					newDcno = String.format("%1$08d",(saibanLogic.saibanDenpyouSerialNo()));
					addDcObj = new Dcno(newDcno);
					addList.add(addDcObj);
					totalCnt = cnt;
				}
				if(addDcObj != null) addDcObj.getDenpyouList().add(denpyou);
			}
			
			//分割で移動した伝票データを自身から削除
			for(Dcno dcObj : addList){
				for(Denpyou dpObj : dcObj.getDenpyouList()){
					dpObj.setDcno(dcObj.getDcno());
					if(denpyouList.contains(dpObj)){
						denpyouList.remove(dpObj);
					}
				}
			}
			
			//分割先の仕訳の伝票番号更新
			for (Dcno dcObj : addList) {
				for (Denpyou denpyou : dcObj.getDenpyouList()) {
					for (GMap shiwake : denpyou.getShiwakeList()) {
						shiwake.replace("dcno", dcObj.getDcno());
						updateDcno((long)shiwake.get("serial_no"), dcObj.getDcno());
					}
				}
			}
			
			return addList;
		}
	}
	
	/**
	 * 伝票番号+伝票ID単位
	 * ぶら下がっている添付ファイル(e文書)と関連伝票は、リンク出力対象のみ
	 */
	@Getter @Setter @ToString
	public class Denpyou {
		/** 伝票番号 */
		String dcno;
		/** 伝票ID */
		String denpyouId;

		/** この伝票自体が帳票出力対象である */
		boolean chouhyou;
		/** リンク名称 */
		String linkName;
		/** ファイル名 */
		String fileName;

		/** 添付ファイル */
		List<Tenpu> tenpuList = new ArrayList<>();
		/** e文書 */
		List<Ebunsho> ebunshoList = new ArrayList<>();
		/** 関連伝票 */
		List<Denpyou> kanrenList = new ArrayList<>();
		/** 実施起案・支出起案 */
		List<Denpyou> kianList = new ArrayList<>();

		/** 仕訳 */
		List<GMap> shiwakeList = new ArrayList<>();
		
		/**
		 * 指定伝票番号・伝票IDのリンク出力情報を構築する。
		 * 以下の構造
		 * 
		 * 自分自身（転記用伝票番号+帳票）
		 *   ├自分自身の帳票
		 *   ├自分自身の添付ファイル1
		 *   ├自分自身の添付ファイル2
		 *   ├...
		 *   ├関連伝票1(帳票)
		 *   │  関連伝票1の添付ファイル1
		 *   │  関連伝票1の添付ファイル2
		 *   │  ...
		 *   │  関連伝票1の支出起案
		 *   │  関連伝票1の実施起案
		 *   │  
		 *   ├関連伝票2(帳票)
		 *   ├...
		 *   ├自分自身の支出起案
		 *   │  支出起案の関連伝票の添付ファイル1
		 *   │  支出起案の関連伝票の添付ファイル2
		 *   │  ...
		 *   │  支出起案の関連伝票の支出起案
		 *   │  支出起案の関連伝票の実施起案
		 *   │  
		 *   └自分自身の実施起案
		 *       実施起案の関連伝票の添付ファイル1
		 *       実施起案の関連伝票の添付ファイル2
		 *       ...
		 *       実施起案の関連伝票の支出起案
		 *       実施起案の関連伝票の実施起案
		 *   
		 * @param dcno 転記用伝票番号
		 * @param rootDenpyouId 伝票ID(仕訳と直接紐付く)
		 * @param denpyouId 伝票ID(関連伝票の場合、rootDenpyouIdの子孫)
		 * @param isKian 支出起案・実施起案として紐付された伝票であるか
		 */
		public Denpyou(String dcno, String rootDenpyouId, String denpyouId, boolean isKian) {
			boolean isRoot = rootDenpyouId.equals(denpyouId);
			
			//自分自身の情報セット
			setDenpyouId(denpyouId);
			setDcno(dcno);
			
			//de3ならリンク情報関係ないので終わり
			if (Open21Env.getVersion() == Version.DE3)
			{
				return;
			}
			
			//リンク情報使わないなら伝票配下の取得不要
			if (! useLink)
			{
				return;
			}
			
			//該当伝票帳票をリンク情報に出力するか判定
			// hoge.pdf決め打ちなので、実質常時true
			if (isEnableAppendFile("hoge.pdf")) {
				setChouhyou(true);
				setLinkName(EteamCommon.byteCut((String)wf.selectDenpyouShubetu(denpyouId.substring(7, 11)).get("denpyou_shubetsu"), LINKNAME_MAXBYTE));
				setFileName(isRoot ?
					 rootDenpyouId + ".pdf"
					:rootDenpyouId + "_SUB" + denpyouId + ".pdf");
			}
			
			//伝票配下の添付ファイル取ってくる
			//添付ファイルがe文書ならe文書としてリンク情報に渡す
			//e文書以外でリンク出力可能なら素ファイルとしてリンク情報に渡す
			List<GMap> appendList = wf.selectTenpuFileForImport(denpyouId);
			for( GMap append : appendList ) {
				String orgFileName = append.get("file_name").toString();
				// 対象外拡張子系はリストに入れない
				if(List.of("", "bat", "exe").contains(FilenameUtils.getExtension(orgFileName).toLowerCase())) {
					continue;
				}
				
				Integer edano = (Integer)append.get("edano");
				String ebunshoNo = (String)append.get("ebunsho_no");
				if (ebunshoNo != null) {
					String denshitorihikiFlg = (String)append.get("denshitorihiki_flg");
					String tsfuyoFlg = (String)append.get("tsfuyo_flg");
					ebunshoList.add(new Ebunsho(denpyouId, edano, orgFileName, ebunshoNo, denshitorihikiFlg, tsfuyoFlg));
				} else if (isEnableAppendFile(orgFileName)) {
					tenpuList.add(new Tenpu(rootDenpyouId, denpyouId, edano, orgFileName));
				}
			}
			
			//伝票配下の関連文書情報とってくる
			if (isRoot || isKian) {
				List<GMap> kanrenChildList = fetchKanrenChildList(denpyouId);
				for (GMap kanrenChild : kanrenChildList) {
					//関連伝票側のdcnoは指定しても使うことはない
					Denpyou kanrenDenpyou = new Denpyou("", rootDenpyouId, (String)kanrenChild.get("denpyou_id"), false);
					kanrenList.add(kanrenDenpyou);
				}
			}
			
			//伝票配下の支出起案・実施起案情報とってくる
			List<GMap> kianChildList = fetchKianList(denpyouId);
			for (GMap kianChild : kianChildList) {
				//支出起案・実施起案側のdcnoは指定しても使うことはない
				if(kianChild.get("kian_denpyou") != null && !((String)kianChild.get("kian_denpyou")).isEmpty() ){
					Denpyou kianDenpyou = new Denpyou("", denpyouId, (String)kianChild.get("kian_denpyou"), true);
					kianList.add(kianDenpyou);
				}
			}
		}

		/**
		 * 伝票配下のリンク情報を全てファイル出力
		 * 
		 * @param folder フォルダ
		 */
		public void outFiles(String folder) {
			if (isChouhyou()) outFile(folder);
			for (Ebunsho ebunsho : ebunshoList) ebunsho.outFile(folder);
			for (Tenpu tenpu : tenpuList) tenpu.outFile(folder);
			for (Denpyou kanren : kanrenList) kanren.outFiles(folder);
			for (Denpyou kian : kianList) kian.outFiles(folder);
		}
		
		/**
		 * 該当伝票番号・伝票IDのリンク数を取得
		 * @return long 合計リンク数
		 */
		public long getLinkCount(){
			long cnt = 0;
			if (isChouhyou())
			{
				cnt++;
			}
			cnt += tenpuList.size();
			for (Ebunsho ebunsho : getEbunshoList()) cnt += ebunsho.getEbunshoDataCount();
			for (Denpyou kanren : getKanrenList()) cnt += kanren.getLinkCount();
			for (Denpyou kian : getKianList()) cnt += kian.getLinkCount();
			return cnt;
		}
		
		/**
		 * 伝票の帳票をファイル出力
		 * 
		 * @param folder フォルダ
		 */
		public void outFile(String folder) {
			String denpyouKbn = denpyouId.substring(7, 11);

			//伝票個別Excelファイル作成
			ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
			
			switch(denpyouKbn) {
			case "A001":
				KeihiTatekaeSeisanXlsLogic keihitatekaeseisanxsLogic = EteamContainer.getComponent(KeihiTatekaeSeisanXlsLogic.class, connection);
				keihitatekaeseisanxsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A002":
				KaribaraiShinseiXlsLogic karibaraishinseixlsLogic = EteamContainer.getComponent(KaribaraiShinseiXlsLogic.class, connection);
				karibaraishinseixlsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A003":
				SeikyuushoBaraiXlsLogic seikyuushobaraixlsLogic = EteamContainer.getComponent(SeikyuushoBaraiXlsLogic.class, connection);
				seikyuushobaraixlsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A004":
				RyohiSeisanXlsLogic ryohiseisanxlsLogic = EteamContainer.getComponent(RyohiSeisanXlsLogic.class, connection);
				ryohiseisanxlsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A005":
				RyohikaribaraiShinseiXlsLogic ryohikaribaraishinseixlsLogic = EteamContainer.getComponent(RyohikaribaraiShinseiXlsLogic.class, connection);
				ryohikaribaraishinseixlsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A006":
				TsuukinteikiShinseiXlsLogic tsuukinteikishinseixlsLogic = EteamContainer.getComponent(TsuukinteikiShinseiXlsLogic.class, connection);
				tsuukinteikishinseixlsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A007":
				FurikaeDenpyouXlsLogic furikaedenpyouxlsLogic = EteamContainer.getComponent(FurikaeDenpyouXlsLogic.class, connection);
				furikaedenpyouxlsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A008":
				SougouTsukekaeDenpyouXlsLogic sougoutsukekaedenpyouxlsLogic = EteamContainer.getComponent(SougouTsukekaeDenpyouXlsLogic.class, connection);
				sougoutsukekaedenpyouxlsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A009":
				JidouHikiotoshiDenpyouXlsLogic jidouhikiotoshidenpyouxlsLogic = EteamContainer.getComponent(JidouHikiotoshiDenpyouXlsLogic.class, connection);
				jidouhikiotoshidenpyouxlsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A010":
				KoutsuuhiSeisanXlsLogic koutsuuhiseisanxlsLogic = EteamContainer.getComponent(KoutsuuhiSeisanXlsLogic.class, connection);
				koutsuuhiseisanxlsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A011":
				KaigaiRyohiSeisanXlsLogic kaigaiRyohiseisanxlsLogic = EteamContainer.getComponent(KaigaiRyohiSeisanXlsLogic.class, connection);
				kaigaiRyohiseisanxlsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A012":
				KaigaiRyohikaribaraiShinseiXlsLogic kaigaiRyohikaribaraishinseixlsLogic = EteamContainer.getComponent(KaigaiRyohikaribaraiShinseiXlsLogic.class, connection);
				kaigaiRyohikaribaraishinseixlsLogic.makeExcel(denpyouId, excelOut);
				break;
			case "A013":
				ShiharaiIraiXlsLogic shiharaiIraiXlsLogic = EteamContainer.getComponent(ShiharaiIraiXlsLogic.class, connection);
				shiharaiIraiXlsLogic.makeExcel(denpyouId, excelOut);
				break;
			}
			if(denpyouKbn.startsWith("B")) {
				KaniTodokeCategoryLogic kaniTodokeCategoryLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, connection);
				KanitodokeXlsLogic kanitodokexlsLogic = EteamContainer.getComponent(KanitodokeXlsLogic.class, connection);
				String version = String.valueOf(kaniTodokeCategoryLogic.findVersion(denpyouId));
				kanitodokexlsLogic.makeExcel(denpyouId, excelOut, version, denpyouKbn, false);
			}
			outFile_Ext(denpyouId, denpyouKbn, excelOut);
			
			//PDF変換
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
			Workbook workbook;
			try {
				workbook = new Workbook(new ByteArrayInputStream(excelOut.toByteArray()));
				workbook.save(pdfOut, SaveFormat.PDF);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			//ファイル出力
			EteamIO.writeFile(pdfOut.toByteArray(), folder + "/" + getFileName());
		}
	}
	
	/**
	 * カスタマイズ用 outFile
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param excelOut Excel帳票
	 */
	public void outFile_Ext(String denpyouId, String denpyouKbn, ByteArrayOutputStream excelOut) {
		
	}
	
	/**
	 * リンク情報出力対象としての添付ファイル(e文書)情報
	 */
	@Getter @Setter @ToString
	public class Tenpu {
		/** 伝票ID */
		String denpyouId;
		/** 枝番号 */
		Integer edaNo;
		
		/** リンク名称 */
		String linkName;
		/** ファイル名 */
		String fileName;
		
		/**
		 * new
		 * @param rootDenpyouId 伝票ID(仕訳と直接紐付く)
		 * @param denpyouId 伝票ID(関連伝票の場合、rootDenpyouIdの子孫)
		 * @param edaNo 枝番号
		 * @param orgFileName ファイル名
		 */
		public Tenpu(String rootDenpyouId, String denpyouId, Integer edaNo, String orgFileName) {
			setDenpyouId(denpyouId);
			setEdaNo(edaNo);

			setLinkName(EteamCommon.byteCut((String)wf.selectDenpyouShubetu(denpyouId.substring(7, 11)).get("denpyou_shubetsu") + "-" + EteamIO.getBase(orgFileName), LINKNAME_MAXBYTE));
			setFileName(rootDenpyouId.equals(denpyouId) ?
				 rootDenpyouId + "_" + orgFileName
				:rootDenpyouId + "_SUB" + denpyouId + "_" + orgFileName);
		}
		
		/**
		 * 添付ファイルををファイル出力
		 * 
		 * @param folder 出力先
		 */
		public void outFile(String folder) {
			byte[] fileData = (byte[])wf.tenpuFileFind(denpyouId, edaNo).get("binary_data");
			EteamIO.writeFile(fileData, folder + "/" + getFileName());
		}
	}

	/**
	 * e文書属性
	 */
	@Getter @Setter @ToString
	public class Ebunsho {
		/** 伝票ID */
		String denpyouId;
		/** 枝番号 */
		Integer edaNo;
		/** e文書番号 */
		String ebunshoNo;
		
		/** 電子取引フラグ */
		String denshitorihikiFlg;
		/** タイムスタンプ付与フラグ */
		String tsfuyoFlg;
		
		/** 申請者名称 */
		String shinseisha;
		/** 最終承認者名称 */
		String shouninsha;
		/** 書類種別 */
		List<String> shubetsu = new ArrayList<>();
		/** 書類日付 */
		List<String> date = new ArrayList<>();
		/** 書類金額 */
		List<String> kingaku = new ArrayList<>();
		/** 書類発行者名称 */
		List<String> hakkousha = new ArrayList<>();
		/** 書類品名 */
		List<String> hinmei = new ArrayList<>();
		
		/** リンク名称 */
		String linkName;
		/** ファイル名 */
		String fileName;
		/** 拡張子 */
		String extension;
		
		/**
		 * new
		 * @param denpyouId 伝票ID
		 * @param edaNo 枝番号
		 * @param orgFileName ファイル名
		 * @param ebunshoNo e文書番号
		 * @param denshitorihikiFlg 電子取引フラグ
		 * @param tsfuyoFlg タイムスタンプ付与フラグ
		 */
		public Ebunsho(String denpyouId, Integer edaNo, String orgFileName, String ebunshoNo, String denshitorihikiFlg, String tsfuyoFlg) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			
			setDenpyouId(denpyouId);
			setEdaNo(edaNo);
			setEbunshoNo(ebunshoNo);
			setDenshitorihikiFlg(denshitorihikiFlg);
			setTsfuyoFlg(tsfuyoFlg);
			
			GMap srDat = wf.selectShinseiRireki(denpyouId);
			GMap ssDat = wf.selectSaishuShouninUserData(denpyouId);
			setShinseisha(createShainDataString(srDat));
			setShouninsha(createShainDataString(ssDat));
			List<GMap> ebDataList = wf.selectEbunshoDatalist(ebunshoNo);
			for (GMap ebData : ebDataList) {
				shubetsu.add(ebData.get("ebunsho_shubetsu").toString());
				
				Object nengappi = ebData.get("ebunsho_nengappi");
                date.add(nengappi == null ? "" : sdf.format((nengappi)));
                Object ebKingaku = ebData.get("ebunsho_kingaku");
                kingaku.add(ebKingaku == null ? "" : ebKingaku.toString());
				
				hakkousha.add(ebData.get("ebunsho_hakkousha"));
				hinmei.add(ebData.get("ebunsho_hinmei"));
			}

			setLinkName(EteamCommon.byteCut((String)wf.selectDenpyouShubetu(denpyouId.substring(7, 11)).get("denpyou_shubetsu") + "-" + EteamIO.getBase(orgFileName), LINKNAME_MAXBYTE));
			
			setExtension(denshitorihikiFlg.equals(tsfuyoFlg) ? ".pdf" : ("." + FilenameUtils.getExtension(orgFileName)));
			setFileName(ebunshoNo + getExtension());
		}

		/**
		 * 添付ファイルををファイル出力
		 * 
		 * @param folder 出力先
		 */
		public void outFile(String folder) {
			byte[] fileData = (byte[])wf.findTenpuEbunshoFile(ebunshoNo).get("binary_data");
			EteamIO.writeFile(fileData, folder + "/" + ebunshoNo + getExtension());
		}
		
		/**
		 * 該当e文書に紐づくe文書明細数合計を取得
		 * 
		 * @return long e文書明細数合計
		 */
		public long getEbunshoDataCount(){
			final String sql =
					  "SELECT COUNT(*) as cnt "
					+ " FROM ebunsho_data "
					+ "WHERE "
					+ " ebunsho_no = ? ";
			GMap result = connection.find(sql, ebunshoNo); 
			long cnt = (long)result.get("cnt");
			return cnt;
		}
	}

	/**
	 * 「OPEN21転記済」の承認状況を作成
	 * @param denpyouId 伝票ID
	 * @return 登録件数
	 */
	public int insertOPEN21TenkiSyouninJoukyou(String denpyouId) {
		final String sql =
			"INSERT INTO shounin_joukyou VALUES( "
		+ "  ? "						//denpyou_id
		+ "  ,(SELECT COALESCE(MAX(edano + 1), 1) FROM shounin_joukyou tmp WHERE tmp.denpyou_id = ?) "	//edano
		+ "  ,'' "					//user_id
		+ "  ,'' "					//user_full_name
		+ "  ,'' "					//bumon_cd
		+ "  ,'' "					//bumon_full_name
		+ "  ,'' "					//bumon_role_id
		+ "  ,'' "					//bumon_role_name
		+ "  ,'' "					//gyoumu_role_id
		+ "  ,'' "					//gyoumu_role_name
		+ "  ,'' "					//joukyou_cd
		+ "  ,'OPEN21転記' "			//joukyou
		+ "  ,'' "					//comment
		+ "  ,NULL "					//shounin_route_edano
		+ "  ,NULL "					//shounin_route_gougi_edano
		+ "  ,NULL "					//shounin_route_gougi_edaedano
		+ "  ,NULL "					//shounin_shori_kengen_no
		+ "  ,'' "					//shounin_shori_kengen_name
		+ "  ,'batch' "				//touroku_user_id
		+ "  ,current_timestamp "		//touroku_time
		+ "  ,'batch' "				//koushin_user_id
		+ "  ,current_timestamp) "; //koushin_time
		return connection.update(sql, denpyouId, denpyouId);
	}

	/**
	 * 仕訳抽出状態の更新
	 * @param serialNo シリアル番号
	 * @return 更新結果
	 */
	public int updateShiwakeStatusSumi(long serialNo) {
		final String sql = 
				( (Open21Env.getVersion() == Version.DE3 ) ? "UPDATE shiwake_de3 SET ":"UPDATE shiwake_sias SET ")
			+ " shiwake_status = ?, "
			+ " koushin_time=current_timestamp  "
			+ "WHERE "
			+ "  shiwake_status = '0'  "
			+ "  AND serial_no = ? ";

		return connection.update(sql, SHIWAKE_STATUS.SHIWAKE_ZUMI, serialNo);
	}

	/**
	 * 伝票形式を取得する
	 * @param denpyouKbn 伝票区分
	 * @return 伝票形式
	 */
	protected String judgeDenpyouKeishiki(String denpyouKbn) {
		String dfuk = "";
		switch (denpyouKbn) {
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
				dfuk = setting.op21MparamDenpyouKeishikiA001();
				break;
			case DENPYOU_KBN.KARIBARAI_SHINSEI:
				dfuk = setting.op21MparamDenpyouKeishikiA002();
				break;
			case DENPYOU_KBN.SEIKYUUSHO_BARAI:
				dfuk = setting.op21MparamDenpyouKeishikiA003();
				break;
			case DENPYOU_KBN.RYOHI_SEISAN:
				dfuk = setting.op21MparamDenpyouKeishikiA004();
				break;
			case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
				dfuk = setting.op21MparamDenpyouKeishikiA005();
				break;
			case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI:
				dfuk = setting.op21MparamDenpyouKeishikiA006();
				break;
			case DENPYOU_KBN.FURIKAE_DENPYOU:
				dfuk = setting.op21MparamDenpyouKeishiki();
				break;
			case DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU:
				dfuk = setting.op21MparamDenpyouKeishiki();
				break;
			case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
				dfuk = setting.op21MparamDenpyouKeishikiA009();
				break;
			case DENPYOU_KBN.KOUTSUUHI_SEISAN:
				dfuk = setting.op21MparamDenpyouKeishikiA010();
				break;
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
				dfuk = setting.op21MparamDenpyouKeishikiA011();
				break;
			case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
				dfuk = setting.op21MparamDenpyouKeishikiA012();
				break;
			case DENPYOU_KBN.SIHARAIIRAI:
				dfuk = setting.op21MparamDenpyouKeishikiA013();
				break;
			default:
				dfuk = setting.op21MparamDenpyouKeishiki();
		}
		return dfuk;
	}

	/**
	 * 入力パターンを取得する
	 * @param denpyouKbn 伝票区分
	 * @return 入力パターン
	 */
	protected String judgeNyuuryokuPattern(String denpyouKbn) {
		String ijpt = "";
		switch (denpyouKbn) {
			case DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
				ijpt = setting.op21MparamDenpyouNyuuryokuPatternA001();
				break;
			case DENPYOU_KBN.KARIBARAI_SHINSEI:
				ijpt = setting.op21MparamDenpyouNyuuryokuPatternA002();
				break;
			case DENPYOU_KBN.SEIKYUUSHO_BARAI:
				ijpt = setting.op21MparamDenpyouNyuuryokuPatternA003();
				break;
			case DENPYOU_KBN.RYOHI_SEISAN:
				ijpt = setting.op21MparamDenpyouNyuuryokuPatternA004();
				break;
			case DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
				ijpt = setting.op21MparamDenpyouNyuuryokuPatternA005();
				break;
			case DENPYOU_KBN.TSUUKIN_TEIKI_SHINSEI:
				ijpt = setting.op21MparamDenpyouNyuuryokuPatternA006();
				break;
			case DENPYOU_KBN.FURIKAE_DENPYOU:
				ijpt = setting.op21MparamDenpyouNyuuryokuPattern();
				break;
			case DENPYOU_KBN.SOUGOU_TSUKEKAE_DENPYOU:
				ijpt = setting.op21MparamDenpyouNyuuryokuPattern();
				break;
			case DENPYOU_KBN.JIDOU_HIKIOTOSHI_DENPYOU:
				ijpt = setting.op21MparamDenpyouNyuuryokuPatternA009();
				break;
			case DENPYOU_KBN.KOUTSUUHI_SEISAN:
				ijpt = setting.op21MparamDenpyouNyuuryokuPatternA010();
				break;
			case DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
				ijpt = setting.op21MparamDenpyouNyuuryokuPatternA011();
				break;
			case DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
				ijpt = setting.op21MparamDenpyouNyuuryokuPatternA012();
				break;
			case DENPYOU_KBN.SIHARAIIRAI:
				ijpt = setting.op21MparamDenpyouNyuuryokuPatternA013();
				break;
			default:
				ijpt = setting.op21MparamDenpyouNyuuryokuPattern();
		}
		return ijpt;
	}
	
	/**
	 * shiwake_siasテーブルのdcnoを書き換え
	 * @param serialNo シリアル番号
	 * @param dcno 伝票番号
	 */
	protected void updateDcno(long serialNo, String dcno) {
		final String sql = "UPDATE shiwake_sias SET dcno = ? WHERE serial_no = ?";
		connection.update(sql, dcno, serialNo);
	}
	
// 判定不要になったためコメントアウト
//  /**
//  * 伝票番号が伝票ID内先頭かどうか調べる（発生主義なら計上側が先頭、現金主義なら基本的に先頭しかない）
//  * @param dcno       伝票番号
//  * @param denpyouId  伝票ID
//  * @return 先頭ならtrue
//  */
// protected boolean dcnoIsFirstOfDenpyou(String dcno, String denpyouId){
// final String sql1 = "SELECT MIN(dymd) dymd FROM shiwake_sias WHERE denpyou_id = ?";
// final String sql2 = "SELECT dymd FROM shiwake_sias WHERE denpyou_id = ? AND dcno = ?";
// GMap res1 = connection.find(sql1, denpyouId); 
// GMap res2 = connection.find(sql2, denpyouId, dcno); 
// return res1.get("dymd").equals(res2.get("dymd"));
// }
	
	/**
	 * 拡張子設定extension_settingテーブルを参照し、指定拡張子コードがインポート対象であるかを判別する
	 * @param  extension  String 拡張子コード(extension_cdの値)
	 * @return true:取得対象 false:取得対象ではない
	 */
	protected boolean isEnableExtension(String extension) {
		final String sql = "SELECT COUNT(*) FROM extension_setting "
				+  "WHERE UPPER(extension_cd) = UPPER('" + extension + "') AND extension_flg = '1';";
		GMap result = connection.find(sql); 
		long count = (long)result.get("count");
		return (count > 0) ? true : false;
	}
	
	/**
	 * 指定したファイル名がインポート対象のファイルとなるか判別します。(拡張子なしのファイルは非取込対象)
	 * @param fileName 対象のファイル名
	 * @return true:取込対象 false:取込対象でない
	 */
	protected boolean isEnableAppendFile(String fileName) {
		boolean result = false;
		String extStr = EteamIO.getExtension(fileName);
		// 設定不問で取得対象となる拡張子(bmp/jpeg/jpg/png)ならtrue
		if( (EXTENSION_BMP.equalsIgnoreCase(extStr))
				||	(EXTENSION_JPEG.equalsIgnoreCase(extStr))
				||	(EXTENSION_JPG.equalsIgnoreCase(extStr))
				||	(EXTENSION_PNG.equalsIgnoreCase(extStr)) ){
			result = true;
			
		// 拡張子が「doc」「docx」なら「DOCDOCX」としてフラグ取得
		}else if( (EXTENSION_DOC.equalsIgnoreCase(extStr))
				||	(EXTENSION_DOCX.equalsIgnoreCase(extStr)) ){
			result = isEnableExtension(EXTENSION_SETTING_DOCDOCX);
			
		// 拡張子が「xls」「xlsx」なら「XLSXLSX」としてフラグ取得
		}else if( (EXTENSION_XLS.equalsIgnoreCase(extStr))
				||	(EXTENSION_XLSX.equalsIgnoreCase(extStr)) ){
			result = isEnableExtension(EXTENSION_SETTING_XLSXLSX);
			
		// 上記以外なら直接拡張子を用いて拡張子使用フラグ取得
		}else{
			result = isEnableExtension(extStr);
		}
		return result;
	}
	
	
	/**
	 * 関連伝票に紐づく関連伝票を再帰的に取得する。
	 * @param denpyouId 関連伝票ID
	 * @return 関連伝票リスト
	 */
	protected List<GMap> fetchKanrenChildList (String denpyouId) {
		List<GMap> retList = new ArrayList<GMap>();
		List<GMap> childList = wf.loadKanrenDenpyou(denpyouId);
		
		for (GMap childMap : childList) {
			retList.add(childMap);
			List<GMap> list = fetchKanrenChildList((String)childMap.get("denpyou_id"));
			if (list != null && !list.isEmpty()) {
				retList.addAll(list);
			}
		}
		
		return retList;
	}
	
	
	/**
	 * 伝票に紐づく支出起案・実施起案を取得する。
	 * @param denpyouId 関連伝票ID
	 * @return 支出起案・実施起案リスト
	 */
	protected List<GMap> fetchKianList (String denpyouId) {
		List<GMap> retList = new ArrayList<GMap>();
		GMap retMap = wf.selectDenpyouKianId(denpyouId);
		if(retMap != null){
			if(retMap.get("kian_denpyou") != null && !((String)retMap.get("kian_denpyou")).isEmpty()){
				retList.add(retMap);
			}
		}
		
		return retList;
	}
	
	
	/**
	 * 仕訳抽出明細データの転記(DE3版)
	 * @param detailData 仕訳抽出データ
	 * @return  Open21の仕訳抽出明細データ
	 */
	protected Open21CSetData makeShiwakeDetailData_DE3(GMap detailData) {
		Open21CSetData cSetData = new Open21CSetData();

		cSetData.setDYMD(formatDate(detailData.get("dymd"))); // 伝票日付(年月日(yyyymmdd)にフォーマット変換)
		cSetData.setSEIRI(formatString(detailData.get("seiri"))); // 整理月ﾌﾗｸﾞ(0：通常月、1：整理月（四半期、中間、決算に関係なし）、0、1 以外は不可。)
		cSetData.setDCNO(formatString(detailData.get("dcno"))); // 伝票番号(値を設定しない場合は“”)
		cSetData.setRBMN(formatString(detailData.get("rbmn"))); // 借方 部門ｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setRTOR(formatString(detailData.get("rtor"))); // 借方 取引先ｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setRKMK(formatString(detailData.get("rkmk"))); // 借方 科目ｺｰﾄﾞ(必須項目)
		cSetData.setREDA(formatString(detailData.get("reda"))); // 借方 枝番ｺｰﾄﾞ(値を設定しない場合は“”)

		cSetData.setRKOJ(formatString(detailData.get("rkoj"))); // 借方 工事ｺｰﾄﾞ
		cSetData.setRKOS(formatString(detailData.get("rkos"))); // 借方 工種ｺｰﾄﾞ
		cSetData.setRPRJ(formatString(detailData.get("rprj"))); // 借方 ﾌﾟﾛｼﾞｪｸﾄｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setRSEG(formatString(detailData.get("rseg"))); // 借方 ｾｸﾞﾒﾝﾄｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setRDM1(formatString(detailData.get("rdm1"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ1
		cSetData.setRDM2(formatString(detailData.get("rdm2"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ2
		cSetData.setRDM3(formatString(detailData.get("rdm3"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ3
		cSetData.setTKY(formatString(detailData.get("tky"))); // 摘要
		cSetData.setTNO(formatString(detailData.get("tno"))); // 摘要ｺｰﾄﾞ(値を設定しない場合は“”)
		
		cSetData.setSBMN(formatString(detailData.get("sbmn"))); // 貸方 部門ｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setSTOR(formatString(detailData.get("stor"))); // 貸方 取引先ｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setSKMK(formatString(detailData.get("skmk"))); // 貸方 科目ｺｰﾄﾞ(必須項目)
		cSetData.setSEDA(formatString(detailData.get("seda"))); // 貸方 枝番ｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setSKOJ(formatString(detailData.get("skoj"))); // 貸方 工事ｺｰﾄﾞ
		cSetData.setSKOS(formatString(detailData.get("skos"))); // 貸方 工種ｺｰﾄﾞ
		cSetData.setSPRJ(formatString(detailData.get("sprj"))); // 貸方 ﾌﾟﾛｼﾞｪｸﾄｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setSSEG(formatString(detailData.get("sseg"))); // 貸方 ｾｸﾞﾒﾝﾄｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setSDM1(formatString(detailData.get("sdm1"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ1
		cSetData.setSDM2(formatString(detailData.get("sdm2"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ2
		cSetData.setSDM3(formatString(detailData.get("sdm3"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ3
		cSetData.setEXVL(formatNum(detailData.get("exvl"))); // 対価金額
		cSetData.setVALU(formatNum(detailData.get("valu"))); // 金額
		
		cSetData.setZKMK(formatString(detailData.get("zkmk"))); // 消費税対象科目ｺｰﾄﾞ
		cSetData.setZRIT(formatString(detailData.get("zrit"))); // 消費税対象科目税率
		cSetData.setZZKB(formatString(detailData.get("zzkb"))); // 消費税対象科目 課税区分
		cSetData.setZGYO(formatString(detailData.get("zgyo"))); // 消費税対象科目 業種区分
		cSetData.setZSRE(formatString(detailData.get("zsre"))); // 消費税対象科目 仕入区分
		cSetData.setZKEIGEN(formatString(detailData.get("zkeigen"))); // 消費税対象科目 軽減税率区分
		cSetData.setRRIT(formatString(detailData.get("rrit"))); // 借方 税率
		cSetData.setRKEIGEN(formatString(detailData.get("rkeigen"))); // 借方 軽減税率区分
		cSetData.setSRIT(formatString(detailData.get("srit"))); // 貸方 税率
		cSetData.setSKEIGEN(formatString(detailData.get("skeigen"))); //貸方 軽減税率区分
		cSetData.setRZKB(formatString(detailData.get("rzkb"))); // 借方 課税区分
		cSetData.setRGYO(formatString(detailData.get("rgyo"))); // 借方 業種区分
		cSetData.setRSRE(formatString(detailData.get("rsre"))); // 借方 仕入区分
		cSetData.setSZKB(formatString(detailData.get("szkb"))); // 貸方 課税区分
		cSetData.setSGYO(formatString(detailData.get("sgyo"))); // 貸方 業種区分
		cSetData.setSSRE(formatString(detailData.get("ssre"))); // 貸方 仕入区分
		cSetData.setSYMD(formatDate(detailData.get("symd"))); // 支払日
		cSetData.setSKBN(formatString(detailData.get("skbn"))); // 支払区分
		cSetData.setSKIZ(formatDate(detailData.get("skiz"))); // 支払期日
		cSetData.setUYMD(formatDate(detailData.get("uymd"))); // 回収日
		cSetData.setUKBN(formatString(detailData.get("ukbn"))); // 入金区分
		cSetData.setUKIZ(formatDate(detailData.get("ukiz"))); // 回収期日
		cSetData.setSTEN(formatString(detailData.get("sten"))); // 店券ﾌﾗｸﾞ
		cSetData.setDKEC(formatString(detailData.get("dkec"))); // 消込ｺｰﾄﾞ
		cSetData.setKYMD(formatDate(detailData.get("kymd"))); // 起票年月日
		cSetData.setKBMN(formatString(detailData.get("kbmn"))); // 起票部門ｺｰﾄﾞ
		cSetData.setKUSR(formatString(detailData.get("kusr"))); // 起票者ｺｰﾄﾞ
		cSetData.setFUSR(formatString(detailData.get("fusr"))); // 入力者ｺｰﾄﾞ
		cSetData.setFSEN(formatString(detailData.get("fsen"))); // 付箋番号
		cSetData.setSGNO(formatString(detailData.get("sgno"))); // 承認ｸﾞﾙｰﾌﾟNo.
		cSetData.setBUNRI(formatString(detailData.get("bunri"))); // 分離区分
		cSetData.setGSEP(formatString(detailData.get("gsep"))); // 行区切り
		
		cSetData.setRURIZEIKEISAN(formatString(detailData.get("rurizeikeisan"))); // 借方　　　　併用売上税額計算方式
		cSetData.setSURIZEIKEISAN(formatString(detailData.get("surizeikeisan"))); // 貸方　　　　併用売上税額計算方式
		cSetData.setZURIZEIKEISAN(formatString(detailData.get("zurizeikeisan"))); // 税対象科目　併用売上税額計算方式
		cSetData.setRMENZEIKEIKA(formatString(detailData.get("rmenzeikeika"))); // 借方　　　　仕入税額控除経過措置割合
		cSetData.setSMENZEIKEIKA(formatString(detailData.get("smenzeikeika"))); // 貸方　　　　仕入税額控除経過措置割合
		cSetData.setZMENZEIKEIKA(formatString(detailData.get("zmenzeikeika"))); // 税対象科目　仕入税額控除経過措置割合

		return cSetData;
	}
	

	/**
	 * 仕訳抽出明細データの転記(SIAS版)
	 * @param detailData 仕訳抽出データ
	 * @param addLNo 添付ファイルリンク番号(リンク情報なければnull)
	 * @return  Open21の仕訳抽出明細データ
	 */
	protected Open21CSetData makeShiwakeDetailData_SIAS(GMap detailData, Integer addLNo) {
		Open21CSetData cSetData = new Open21CSetData();

		cSetData.setDYMD(formatDate(detailData.get("dymd"))); // 伝票日付(年月日(yyyymmdd)にフォーマット変換)
		cSetData.setSEIRI(formatString(detailData.get("seiri"))); // 整理月ﾌﾗｸﾞ(0：通常月、1：整理月（四半期、中間、決算に関係なし）、0、1 以外は不可。)
		cSetData.setDCNO(formatString(detailData.get("dcno"))); // 伝票番号(値を設定しない場合は“”)
		cSetData.setKYMD(formatDate(detailData.get("kymd"))); // 起票年月日
		cSetData.setKBMN(formatString(detailData.get("kbmn"))); // 起票部門ｺｰﾄﾞ
		cSetData.setKUSR(formatString(detailData.get("kusr"))); // 起票者ｺｰﾄﾞ
		cSetData.setSGNO(formatString(detailData.get("sgno"))); // 承認ｸﾞﾙｰﾌﾟNo.

		cSetData.setHF1(formatString(detailData.get("hf1"))); // ﾍｯﾀﾞｰﾌｨｰﾙﾄﾞ1
		cSetData.setHF2(formatString(detailData.get("hf2"))); // ﾍｯﾀﾞｰﾌｨｰﾙﾄﾞ2
		cSetData.setHF3(formatString(detailData.get("hf3"))); // ﾍｯﾀﾞｰﾌｨｰﾙﾄﾞ3
		cSetData.setHF4(formatString(detailData.get("hf4"))); // ﾍｯﾀﾞｰﾌｨｰﾙﾄﾞ4
		cSetData.setHF5(formatString(detailData.get("hf5"))); // ﾍｯﾀﾞｰﾌｨｰﾙﾄﾞ5
		cSetData.setHF6(formatString(detailData.get("hf6"))); // ﾍｯﾀﾞｰﾌｨｰﾙﾄﾞ6
		cSetData.setHF7(formatString(detailData.get("hf7"))); // ﾍｯﾀﾞｰﾌｨｰﾙﾄﾞ7
		cSetData.setHF8(formatString(detailData.get("hf8"))); // ﾍｯﾀﾞｰﾌｨｰﾙﾄﾞ8
		cSetData.setHF9(formatString(detailData.get("hf9"))); // ﾍｯﾀﾞｰﾌｨｰﾙﾄﾞ9
		cSetData.setHF10(formatString(detailData.get("hf10"))); // ﾍｯﾀﾞｰﾌｨｰﾙﾄﾞ10

		cSetData.setRBMN(formatString(detailData.get("rbmn"))); // 借方 部門ｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setRTOR(formatString(detailData.get("rtor"))); // 借方 取引先ｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setRKMK(formatString(detailData.get("rkmk"))); // 借方 科目ｺｰﾄﾞ(必須項目)
		cSetData.setREDA(formatString(detailData.get("reda"))); // 借方 枝番ｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setRKOJ(formatString(detailData.get("rkoj"))); // 借方 工事ｺｰﾄﾞ
		cSetData.setRKOS(formatString(detailData.get("rkos"))); // 借方 工種ｺｰﾄﾞ
		cSetData.setRPRJ(formatString(detailData.get("rprj"))); // 借方 ﾌﾟﾛｼﾞｪｸﾄｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setRSEG(formatString(detailData.get("rseg"))); // 借方 ｾｸﾞﾒﾝﾄｺｰﾄﾞ(値を設定しない場合は“”)

		cSetData.setRDM1(formatString(detailData.get("rdm1"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ1
		cSetData.setRDM2(formatString(detailData.get("rdm2"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ2
		cSetData.setRDM3(formatString(detailData.get("rdm3"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ3
		cSetData.setRDM4(formatString(detailData.get("rdm4"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ4
		cSetData.setRDM5(formatString(detailData.get("rdm5"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ5
		cSetData.setRDM6(formatString(detailData.get("rdm6"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ6
		cSetData.setRDM7(formatString(detailData.get("rdm7"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ7
		cSetData.setRDM8(formatString(detailData.get("rdm8"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ8
		cSetData.setRDM9(formatString(detailData.get("rdm9"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ9
		cSetData.setRDM10(formatString(detailData.get("rdm10"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ10
		cSetData.setRDM11(formatString(detailData.get("rdm11"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ11
		cSetData.setRDM12(formatString(detailData.get("rdm12"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ12
		cSetData.setRDM13(formatString(detailData.get("rdm13"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ13
		cSetData.setRDM14(formatString(detailData.get("rdm14"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ14
		cSetData.setRDM15(formatString(detailData.get("rdm15"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ15
		cSetData.setRDM16(formatString(detailData.get("rdm16"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ16
		cSetData.setRDM17(formatString(detailData.get("rdm17"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ17
		cSetData.setRDM18(formatString(detailData.get("rdm18"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ18
		cSetData.setRDM19(formatString(detailData.get("rdm19"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ19
		cSetData.setRDM20(formatString(detailData.get("rdm20"))); // 借方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ20

		cSetData.setRRIT(formatString(detailData.get("rrit"))); // 借方 税率
		cSetData.setRKEIGEN(formatString(detailData.get("rkeigen"))); // 借方 軽減税率区分		
		cSetData.setRZKB(formatString(detailData.get("rzkb"))); // 借方 課税区分
		cSetData.setRGYO(formatString(detailData.get("rgyo"))); // 借方 業種区分
		cSetData.setRSRE(formatString(detailData.get("rsre"))); // 借方 仕入区分
		cSetData.setRTKY(formatString(detailData.get("rtky")));
		cSetData.setRTNO(formatString(detailData.get("rtno"))); // 借方 摘要ｺｰﾄﾞ(値を設定しない場合は“”)

		cSetData.setSBMN(formatString(detailData.get("sbmn"))); // 貸方 部門ｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setSTOR(formatString(detailData.get("stor"))); // 貸方 取引先ｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setSKMK(formatString(detailData.get("skmk"))); // 貸方 科目ｺｰﾄﾞ(必須項目)
		cSetData.setSEDA(formatString(detailData.get("seda"))); // 貸方 枝番ｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setSKOJ(formatString(detailData.get("skoj"))); // 貸方 工事ｺｰﾄﾞ
		cSetData.setSKOS(formatString(detailData.get("skos"))); // 貸方 工種ｺｰﾄﾞ
		cSetData.setSPRJ(formatString(detailData.get("sprj"))); // 貸方 ﾌﾟﾛｼﾞｪｸﾄｺｰﾄﾞ(値を設定しない場合は“”)
		cSetData.setSSEG(formatString(detailData.get("sseg"))); // 貸方 ｾｸﾞﾒﾝﾄｺｰﾄﾞ(値を設定しない場合は“”)

		cSetData.setSDM1(formatString(detailData.get("sdm1"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ1
		cSetData.setSDM2(formatString(detailData.get("sdm2"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ2
		cSetData.setSDM3(formatString(detailData.get("sdm3"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ3
		cSetData.setSDM4(formatString(detailData.get("sdm4"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ4
		cSetData.setSDM5(formatString(detailData.get("sdm5"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ5
		cSetData.setSDM6(formatString(detailData.get("sdm6"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ6
		cSetData.setSDM7(formatString(detailData.get("sdm7"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ7
		cSetData.setSDM8(formatString(detailData.get("sdm8"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ8
		cSetData.setSDM9(formatString(detailData.get("sdm9"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ9
		cSetData.setSDM10(formatString(detailData.get("sdm10"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ10
		cSetData.setSDM11(formatString(detailData.get("sdm11"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ11
		cSetData.setSDM12(formatString(detailData.get("sdm12"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ12
		cSetData.setSDM13(formatString(detailData.get("sdm13"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ13
		cSetData.setSDM14(formatString(detailData.get("sdm14"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ14
		cSetData.setSDM15(formatString(detailData.get("sdm15"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ15
		cSetData.setSDM16(formatString(detailData.get("sdm16"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ16
		cSetData.setSDM17(formatString(detailData.get("sdm17"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ17
		cSetData.setSDM18(formatString(detailData.get("sdm18"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ18
		cSetData.setSDM19(formatString(detailData.get("sdm19"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ19
		cSetData.setSDM20(formatString(detailData.get("sdm20"))); // 貸方 ﾕﾆﾊﾞｰｻﾙﾌｨｰﾙﾄﾞ20

		cSetData.setSRIT(formatString(detailData.get("srit"))); // 貸方 税率
		cSetData.setSKEIGEN(formatString(detailData.get("skeigen"))); // 貸方 軽減税率区分
		cSetData.setSZKB(formatString(detailData.get("szkb"))); // 貸方 課税区分
		cSetData.setSGYO(formatString(detailData.get("sgyo"))); // 貸方 業種区分
		cSetData.setSSRE(formatString(detailData.get("ssre"))); // 貸方 仕入区分
		cSetData.setSTKY(formatString(detailData.get("stky"))); // 貸方 摘要
		cSetData.setSTNO(formatString(detailData.get("stno"))); // 貸方 摘要ｺｰﾄﾞ(値を設定しない場合は“”)

		cSetData.setZKMK(formatString(detailData.get("zkmk"))); // 消費税対象科目ｺｰﾄﾞ
		cSetData.setZRIT(formatString(detailData.get("zrit"))); // 消費税対象科目税率
		cSetData.setZZKB(formatString(detailData.get("zzkb"))); // 消費税対象科目 課税区分
		cSetData.setZGYO(formatString(detailData.get("zgyo"))); // 消費税対象科目 業種区分
		cSetData.setZSRE(formatString(detailData.get("zsre"))); // 消費税対象科目 仕入区分
		cSetData.setZKEIGEN(formatString(detailData.get("zkeigen"))); // 消費税対象科目 軽減税率区分

		cSetData.setEXVL(formatNum(detailData.get("exvl"))); // 対価金額
		cSetData.setVALU(formatNum(detailData.get("valu"))); // 金額
		cSetData.setSYMD(formatDate(detailData.get("symd"))); // 支払日
		cSetData.setSKBN(formatString(detailData.get("skbn"))); // 支払区分
		cSetData.setSKIZ(formatDate(detailData.get("skiz"))); // 支払期日
		cSetData.setUYMD(formatDate(detailData.get("uymd"))); // 回収日
		cSetData.setUKBN(formatString(detailData.get("ukbn"))); // 入金区分
		cSetData.setUKIZ(formatDate(detailData.get("ukiz"))); // 回収期日
		cSetData.setDKEC(formatString(detailData.get("dkec"))); // 消込ｺｰﾄﾞ
		cSetData.setFUSR(formatString(detailData.get("fusr"))); // 入力者ｺｰﾄﾞ
		cSetData.setFSEN(formatString(detailData.get("fsen"))); // 付箋番号
		cSetData.setTKFLG(formatString(detailData.get("tkflg"))); // 貸借別摘要ﾌﾗｸﾞ
		cSetData.setBUNRI(formatString(detailData.get("bunri"))); // 分離区分
		cSetData.setHEIC(formatString(detailData.get("heic"))); // 幣種

		cSetData.setGSEP(formatString(detailData.get("gsep"))); // 行区切り

		cSetData.setLNO(formatString(null == addLNo ? 0 : addLNo)); // リンクＮｏ 引数として指定された番号を設定(添付ファイルが元伝票に無い場合は0)

		cSetData.setRATE(formatString(detailData.get("rate"))); // ﾚｰﾄ
		cSetData.setGEXVL(formatString(detailData.get("gexvl"))); // 外貨対価金額
		cSetData.setGVALU(formatString(detailData.get("gvalu"))); // 外貨金額

		cSetData.setRURIZEIKEISAN(formatString(detailData.get("rurizeikeisan"))); // 借方　　　　併用売上税額計算方式
		cSetData.setSURIZEIKEISAN(formatString(detailData.get("surizeikeisan"))); // 貸方　　　　併用売上税額計算方式
		cSetData.setZURIZEIKEISAN(formatString(detailData.get("zurizeikeisan"))); // 税対象科目　併用売上税額計算方式
		cSetData.setRMENZEIKEIKA(formatString(detailData.get("rmenzeikeika"))); // 借方　　　　仕入税額控除経過措置割合
		cSetData.setSMENZEIKEIKA(formatString(detailData.get("smenzeikeika"))); // 貸方　　　　仕入税額控除経過措置割合
		cSetData.setZMENZEIKEIKA(formatString(detailData.get("zmenzeikeika"))); // 税対象科目　仕入税額控除経過措置割合

		return cSetData;
	}
	
	/**
	 * Importer.exeに渡すリンク情報を作る
	 * @param dc 伝票番号
	 * @return 伝票番号配下の全リンク情報
	 */
	protected List<Open21LinkData> makeLinkData(Dcno dc) {
		List<Open21LinkData> ret = new ArrayList<>();
		for (Denpyou denpyou : dc.getDenpyouList()) {
			//伝票本体
			if (denpyou.isChouhyou()) {
				ret.add(new Open21LinkData(dc.getLinkNo(), denpyou.getLinkName(), denpyou.getFileName()));
			}
			//e文書
			for (Ebunsho eb : denpyou.getEbunshoList()) {
				for (int i = 0; i < eb.getShubetsu().size(); i++) {
					ret.add(new Open21LinkData(dc.getLinkNo(), eb.getLinkName(), eb.getFileName(), eb.getEbunshoNo(), eb.getDenshitorihikiFlg(), eb.getTsfuyoFlg(), eb.getShinseisha(), eb.getShouninsha(), eb.getShubetsu().get(i), eb.getDate().get(i), eb.getKingaku().get(i), eb.getHakkousha().get(i), eb.getHinmei().get(i)));
				}
			}
			//添付ファイル
			for (Tenpu tp : denpyou.getTenpuList()) {
				ret.add(new Open21LinkData(dc.getLinkNo(), tp.getLinkName(), tp.getFileName()));
			}
			
			//関連文書
			addKanrenLinkData(dc, denpyou, ret);
			
			//支出起案・実施起案
			addKianLinkData(dc, denpyou, ret);
			
		}
		return ret;
	}
	
	/**
	 * 関連文書のリンク情報を作成し指定リスト内に格納
	 * @param dc 伝票番号
	 * @param dnp 関連文書を持つ伝票クラス
	 * @param orgList リンク情報リスト
	 */
	protected void addKanrenLinkData(Dcno dc, Denpyou dnp, List<Open21LinkData> orgList){
		//関連文書
		for (Denpyou kan : dnp.getKanrenList()) {
			if (kan.isChouhyou()) {
				orgList.add(new Open21LinkData(dc.getLinkNo(), kan.getLinkName(), kan.getFileName()));
			}
			//関連文書のe文書
			for (Ebunsho eb : kan.getEbunshoList()) {
				for (int i = 0; i < eb.getShubetsu().size(); i++) {
					orgList.add(new Open21LinkData(dc.getLinkNo(), eb.getLinkName(), eb.getFileName(), eb.getEbunshoNo(), eb.getDenshitorihikiFlg(), eb.getTsfuyoFlg(), eb.getShinseisha(), eb.getShouninsha(), eb.getShubetsu().get(i), eb.getDate().get(i), eb.getKingaku().get(i), eb.getHakkousha().get(i), eb.getHinmei().get(i)));
				}
			}
			//関連文書の添付ファイル
			for (Tenpu tp : kan.getTenpuList()) {
				orgList.add(new Open21LinkData(dc.getLinkNo(), tp.getLinkName(), tp.getFileName()));
			}
			//関連文書の支出起案・実施起案
			addKianLinkData(dc, kan, orgList);
		}
	}
	
	
	/**
	 * 支出起案・実施起案のリンク情報を作成し指定リスト内に格納
	 * @param dc 伝票番号
	 * @param dnp 支出起案・実施起案を持つ伝票クラス
	 * @param orgList リンク情報リスト
	 */
	protected void addKianLinkData(Dcno dc, Denpyou dnp, List<Open21LinkData> orgList){
		//支出起案・実施起案
		for (Denpyou kia : dnp.getKianList()) {
			if (kia.isChouhyou()) {
				orgList.add(new Open21LinkData(dc.getLinkNo(), kia.getLinkName(), kia.getFileName()));
			}
			//支出起案・実施起案 のe文書
			for (Ebunsho eb : kia.getEbunshoList()) {
				for (int i = 0; i < eb.getShubetsu().size(); i++) {
					orgList.add(new Open21LinkData(dc.getLinkNo(), eb.getLinkName(), eb.getFileName(), eb.getEbunshoNo(), eb.getDenshitorihikiFlg(), eb.getTsfuyoFlg(), eb.getShinseisha(), eb.getShouninsha(), eb.getShubetsu().get(i), eb.getDate().get(i), eb.getKingaku().get(i), eb.getHakkousha().get(i), eb.getHinmei().get(i)));
				}
			}
			//支出起案・実施起案 の添付ファイル
			for (Tenpu tp : kia.getTenpuList()) {
				orgList.add(new Open21LinkData(dc.getLinkNo(), tp.getLinkName(), tp.getFileName()));
			}
			
			//支出起案・実施起案 の関連文書
			addKanrenLinkData(dc, kia, orgList);
			
			//実施起案(存在する場合)
			addKianLinkData(dc, kia, orgList);
		}
	}
	

	/**
	 * 数値のフォーマット変換。
	 * @param num 変換前
	 * @return 変換後
	 */
	protected BigDecimal formatNum(Object num) {
		if (null == num) {
			return new BigDecimal(0);
		} else if (num instanceof BigDecimal) {
			return (BigDecimal) num;
		} else {
			throw new InvalidParameterException("BigDecimal以外禁止.num:" + num);
		}
	}
	
	/**
	 * 文字列のフォーマット変換。
	 * @param str 変換前
	 * @return 変換後
	 */
	protected String formatString(Object str) {
		
		if (null == str) {
			return "";
		} else if (str instanceof String) {
			return (String) str;
		} else {
			return String.valueOf(str);
		}
	}
	
	/**
	 * 日付型のフォーマット変換。
	 * @param inDate 変換前
	 * @return 変換後
	 */
	protected Date formatDate(Object inDate){
		
		if (null == inDate) {
			return null;
		}
		if (inDate instanceof Date) {
			return (Date) inDate;
		} else {
			throw new InvalidParameterException("Date以外禁止.inDate:" + inDate);
		}
	}
	
	/**
	 * 引数の社員情報Mapを元に、インポート時申請者・承認者登録用の文字列を作成します。
	 * @param datMap 社員情報Map
	 * @return 登録用文字列
	 */
	@SuppressWarnings("cast")
	protected String createShainDataString(GMap datMap){
		String fullName = isEmpty((String)datMap.get("user_full_name")) ? "" : (String)datMap.get("user_full_name");
		String shainNo = isEmpty((String)datMap.get("shain_no")) ? "" : (String)datMap.get("shain_no");
		return fullName + ":" + shainNo;
	}
	
	/**
	 * 伝票区分と伝票IDからinvoiceDenpyouを取得する
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @return 1:旧伝票　0:インボイス対応
	 */
	public String getInvoiceDenpyou(String denpyouId, String denpyouKbn) {
		// 1:インボイス前　0:インボイス後
		String ret = "1";
		GMap mp;
		//TODO わざとDeprecatedなkaikeiLogic.find伝票()を使用している　インボイス後にDao使用など整備してください
		//dao部品が増えるから　Daoの継承元とかDTOのインターフェースの元とか
		switch(denpyouKbn) {
		case "A001":
			mp = kaikeiLogic.findKeihiSeisan(denpyouId);
			break;
		case "A003":
			mp = kaikeiLogic.findSeikyuushobarai(denpyouId);
			break;
		case "A004":
			mp = kaikeiLogic.findRyohiSeisan(denpyouId);
			break;
		case "A006":
			mp = kaikeiLogic.findTsuukinnteiki(denpyouId);
			break;
		case "A007":
			mp = kaikeiLogic.findFurikae(denpyouId);
			break;
		case "A008":
			mp = kaikeiLogic.findTsukekae(denpyouId);
			break;
		case "A009":
			mp = kaikeiLogic.findJidouhikiotoshi(denpyouId);
			break;
		case "A010":
			mp = kaikeiLogic.findKoutsuuhiSeisan(denpyouId);
			break;
		case "A011":
			mp = kaikeiLogic.findKaigaiRyohiSeisan(denpyouId);
			break;
		case "A013":
			mp = kaikeiLogic.findShiharaiIrai(denpyouId);
			break;
		default:
			mp = null;
		}
		if(mp != null) {
			ret = (String)mp.get("invoice_denpyou");
		}
		
		return ret;
	}
	
	/**
	 * 伝票区分からJOIN文を作成する
	 * @param denpyouKbn 伝票区分
	 * @return SQL文字列
	 */
	public String makeJoinStringByKbn(String denpyouKbn) {
		String sqlStr = "";
		switch(denpyouKbn) {
		case "A001":
			sqlStr = " LEFT OUTER JOIN keihiseisan kbn ON kbn.denpyou_id = s.denpyou_id ";
			break;
		case "A003":
			sqlStr = " LEFT OUTER JOIN seikyuushobarai kbn ON kbn.denpyou_id = s.denpyou_id ";
			break;
		case "A004":
			sqlStr = " LEFT OUTER JOIN ryohiseisan kbn ON kbn.denpyou_id = s.denpyou_id ";
			break;
		case "A006":
			sqlStr = " LEFT OUTER JOIN tsuukinteiki kbn ON kbn.denpyou_id = s.denpyou_id ";
			break;
		case "A007":
			sqlStr = " LEFT OUTER JOIN furikae kbn ON kbn.denpyou_id = s.denpyou_id ";
			break;
		case "A008":
			sqlStr = " LEFT OUTER JOIN tsukekae kbn ON kbn.denpyou_id = s.denpyou_id ";
			break;
		case "A009":
			sqlStr = " LEFT OUTER JOIN jidouhikiotoshi kbn ON kbn.denpyou_id = s.denpyou_id ";
			break;
		case "A010":
			sqlStr = " LEFT OUTER JOIN koutsuuhiseisan kbn ON kbn.denpyou_id = s.denpyou_id ";
			break;
		case "A011":
			sqlStr = " LEFT OUTER JOIN kaigai_ryohiseisan kbn ON kbn.denpyou_id = s.denpyou_id ";
			break;
		case "A013":
			sqlStr = " LEFT OUTER JOIN shiharai_irai kbn ON kbn.denpyou_id = s.denpyou_id ";
			break;
		default:
			sqlStr = "";
		}
		
		return sqlStr;
	}
	
	/**
	 * 伝票区分とインボイスフラグからWhere条件を作成する
	 * @param denpyouKbn 伝票区分
	 * @param invoiceFlg インボイスフラグ
	 * @return SQL文字列
	 */
	public String makeJoinStringByInvoiceFlg(String denpyouKbn, String invoiceFlg) {
		String sqlStr = "";
		if(List.of("A001", "A003", "A004", "A006", "A007", "A008", "A009", "A010", "A011", "A013").contains(denpyouKbn)) {
			if("0".equals(invoiceFlg)) {
				//PKG23.05.31.20以前では旅費仮払申請未使用の伝票はinvoice_denpyouが空文字で登録されてしまっていたため
				sqlStr = " AND ( kbn.invoice_denpyou = '"+ invoiceFlg + "' OR kbn.invoice_denpyou = '' ) ";
			}else {
				sqlStr = " AND kbn.invoice_denpyou = '"+ invoiceFlg + "' ";
			}
		}
		return sqlStr;
	}
}
