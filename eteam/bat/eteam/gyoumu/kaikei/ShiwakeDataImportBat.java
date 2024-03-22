package eteam.gyoumu.kaikei;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.base.exception.EteamShiwakeErrorException;
import eteam.common.Env;
import eteam.common.EteamCommon;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_KBN;
import eteam.common.open21.Open21Env;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.kaikei.ShiwakeDataImportLogic.DcnoList;
import eteam.gyoumu.kaikei.common.BatchKaikeiCommonLogic;
import eteam.gyoumu.kaikei.open21.Open21ShiwakeData;
import eteam.gyoumu.kaikei.open21.Open21ShiwakeImportLogic;
import eteam.gyoumu.tsuuchi.DenpyouIchiranUpdateLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;

/**
 * 仕訳データインポートバッチ
 */
public class ShiwakeDataImportBat extends EteamAbstractBat {
	
	//＜定数＞
	
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(ShiwakeDataImportBat.class);
	
	/** 仕訳ロジック */
	protected ShiwakeDataImportLogic shiwakeLogic;
	/** バッチ会計ロジック */
	protected BatchKaikeiCommonLogic common;
	/** システム管理ロジック */
	protected SystemKanriCategoryLogic sysl;
	/** ワークフローイベント */
	protected WorkflowEventControlLogic wfEvLg;
	/** ワークフローSELECT */
	protected WorkflowCategoryLogic wfCtLg;
	/** 一時フォルダパス */
	protected File tmpFolder;
	/** １バッチ内で伝票番号処理済かどうか */
	protected Map<String, Boolean> denpyouIdZumi = new HashMap<String, Boolean>();
	/** 伝票一覧テーブルロジック */
	protected DenpyouIchiranUpdateLogic diLogic;

	/**
	 * 初期化を行います。
	 * @param connection コネクション
	 */
	protected void initialize(EteamConnection connection) {
		shiwakeLogic = EteamContainer.getComponent(ShiwakeDataImportLogic.class, connection);
		common = EteamContainer.getComponent(BatchKaikeiCommonLogic.class, connection);
		sysl = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
		wfEvLg = EteamContainer.getComponent(WorkflowEventControlLogic.class, connection);
		wfCtLg = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
		diLogic = EteamContainer.getComponent(DenpyouIchiranUpdateLogic.class, connection);
		tmpFolder = new File(setting.op21MparamCsvPath() + "\\" + EteamCommon.getContextSchemaName());
	}

	@Override
	public String getBatchName() {
		return "仕訳インポート";
	}

	@Override
	public String getCountName() {
		return "仕訳レコード数";
	}
	
	@Override
	public int mainProc() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);

			List<DcnoList> dcnoListList;
			boolean hasError = false;
			int count = 0;
			//---------------------
			//機能OFFであれば何もせず終わり
			//---------------------
			GMap kinouSeigyo = sysl.findKinouSeigyo(KINOU_SEIGYO_CD.KAIKEI_RENKEI);
			if(! KINOU_SEIGYO_KBN.YUUKOU.equals(kinouSeigyo.get("kinou_seigyo_kbn"))) {
				log.info("会計連携機能がOFFなので終了します。");
				return 0;
			}

			//---------------------
			//財務転記対象レコード(shiwake_de3/shiwake_sias)を全て取得する。
			//---------------------
			List<GMap> baseShiwakeDataList = shiwakeLogic.loadShiwakeData("1"); //旧伝票レイアウト
			log.info("仕訳抽出データ総件数:" + baseShiwakeDataList.size());
			if (baseShiwakeDataList.size() != 0) {
// return 0;
			

// boolean hasError = false;
// int count = 0;

			//---------------------
			//取得したレコードをインポート単位に分割する。DcnoListがインポート単位のオブジェクト。
			//---------------------
// List<DcnoList> dcnoListList = new ArrayList<>();
			dcnoListList = new ArrayList<>();
			for (int fromIndex = 0, toIndex;
				 fromIndex < (toIndex = getRenkeiTargetIndex(baseShiwakeDataList, fromIndex));
				 fromIndex = toIndex) {
				//①伝票区分・伝票番号等のキー項目(__groupの仮想カラム)単位に分ける
				List<GMap> shiwakeDataList = baseShiwakeDataList.subList(fromIndex, toIndex);
				//②更にインポート単位内リンク件数が1万未満になるように分ける。
				//インポート単位(DcnoList)
				//  └伝票番号(Dcno)
				//    └伝票ID(Denpyou)
				//       ├添付ファイル(e文書)・関連伝票
				//       └仕訳
				dcnoListList.addAll(shiwakeLogic.createDcList(shiwakeDataList));
			}

			// どうも上の構造化までで種別順序が狂うようなので、データ連携と同じ順序にreorderする
			dcnoListList = dcnoListList.stream()
					.sorted(Comparator.comparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A002")) // 型指定がないと後続でエラーになるので明示しておく（以下同じ）
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A001"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A005"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A004"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A012"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A011"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A010"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A006"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A003"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A013"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A009"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A007"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A008"))
							.reversed())
					.collect(Collectors.toList());
			
			//---------------------
			//インポート単位処理
			//---------------------
			int i = 1;
			for (DcnoList dcnoList : dcnoListList) {
				//インポート単位
				try {
					//一時フォルダ内削除
					if(tmpFolder.exists()) {
						FileUtils.cleanDirectory(tmpFolder);
					}
					tmpFolder.mkdirs();
					
					//呼び出し単位でインポート
					connection.setSearchPath();
					tenki(connection, dcnoList, i++,"1");
					connection.commit();
					count += dcnoList.getShiwakeList().size();
				} catch (EteamShiwakeErrorException e) {
					hasError = true;
					connection.commit();
					log.error("Open21仕訳連携中にエラー発生", e);
				}  catch (RuntimeException e) {
					hasError = true;
					connection.rollback();
					log.error("Open21仕訳連携中にエラー発生", e);
				} finally{
					//一時フォルダ内削除
					//仕訳インポート一時フォルダ保持設定オンなら削除しない
					if ( (! Open21Env.readIsUt()) && !(Env.shiwakeTempFileKeep()) ) {
						if(tmpFolder.exists()) {
							FileUtils.deleteDirectory(tmpFolder);
						}
					}
				}
			}
			setCount(count);
			}
			// 旧伝票↑
			// インボイス伝票↓
			//---------------------
			//財務転記対象レコード(shiwake_de3/shiwake_sias)を全て取得する。
			//---------------------
			baseShiwakeDataList = shiwakeLogic.loadShiwakeData("0"); //インボイス伝票レイアウト
			log.info("仕訳抽出データ総件数:" + baseShiwakeDataList.size());
			if (baseShiwakeDataList.size() == 0) {
				return 0;
			}

// boolean hasError = false;
// int count = 0;

			//---------------------
			//取得したレコードをインポート単位に分割する。DcnoListがインポート単位のオブジェクト。
			//---------------------
			dcnoListList = new ArrayList<>();
			for (int fromIndex = 0, toIndex;
				 fromIndex < (toIndex = getRenkeiTargetIndex(baseShiwakeDataList, fromIndex));
				 fromIndex = toIndex) {
				//①伝票区分・伝票番号等のキー項目(__groupの仮想カラム)単位に分ける
				List<GMap> shiwakeDataList = baseShiwakeDataList.subList(fromIndex, toIndex);
				//②更にインポート単位内リンク件数が1万未満になるように分ける。
				//インポート単位(DcnoList)
				//  └伝票番号(Dcno)
				//    └伝票ID(Denpyou)
				//       ├添付ファイル(e文書)・関連伝票
				//       └仕訳
				dcnoListList.addAll(shiwakeLogic.createDcList(shiwakeDataList));
			}

			// どうも上の構造化までで種別順序が狂うようなので、データ連携と同じ順序にreorderする
			dcnoListList = dcnoListList.stream()
					.sorted(Comparator.comparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A002")) // 型指定がないと後続でエラーになるので明示しておく（以下同じ）
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A001"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A005"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A004"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A012"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A011"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A010"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A006"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A003"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A013"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A009"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A007"))
							.thenComparing((DcnoList list) -> list.get(0).denpyouList.get(0).denpyouId.contains("A008"))
							.reversed())
					.collect(Collectors.toList());
			
			//---------------------
			//インポート単位処理
			//---------------------
			int i = 1;
			for (DcnoList dcnoList : dcnoListList) {
				//インポート単位
				try {
					//一時フォルダ内削除
					if(tmpFolder.exists()) {
						FileUtils.cleanDirectory(tmpFolder);
					}
					tmpFolder.mkdirs();
					
					//呼び出し単位でインポート
					connection.setSearchPath();
					tenki(connection, dcnoList, i++,"0");
					connection.commit();
					count += dcnoList.getShiwakeList().size();
				} catch (EteamShiwakeErrorException e) {
					hasError = true;
					connection.commit();
					log.error("Open21仕訳連携中にエラー発生", e);
				}  catch (RuntimeException e) {
					hasError = true;
					connection.rollback();
					log.error("Open21仕訳連携中にエラー発生", e);
				} finally{
					//一時フォルダ内削除
					//仕訳インポート一時フォルダ保持設定オンなら削除しない
					if ( (! Open21Env.readIsUt()) && !(Env.shiwakeTempFileKeep()) ) {
						if(tmpFolder.exists()) {
							FileUtils.deleteDirectory(tmpFolder);
						}
					}
				}
			}
			setCount(count); //このカウントは旧と新の合計
    		return hasError ? 1 : 0;
		} catch (Throwable e) {
			log.error("エラー発生", e);
			return 1;
		}
	}

	/**
	 * 仕訳レコードを伝票区分・伝票番号等のキー項目(__groupの仮想カラム)単位に分ける
	 * @param baseShiwakeDataList 全仕訳データリスト
	 * @param from 次の処理開始Index
	 * @return 処理対象Indexの最後 + 1
	 */
	protected int getRenkeiTargetIndex(List<GMap> baseShiwakeDataList, int from) {
		int count = 0;
		String prev = null;
		for (ListIterator<GMap> ite = baseShiwakeDataList.listIterator(from); ite.hasNext(); ) {
			GMap map = ite.next();
			String curr = (String)map.get("__group");
			if (null == prev || StringUtils.equals(prev, curr)) {
				prev = curr;
				count++;
			}
		}
		return from + count;
	}
	
	/**
	 * インポート単位処理
	 * @param connection コネクション
	 * @param dcnoList インポート単位の伝票番号リスト
	 * @param edaban 枝番
	 */
	protected void tenki(EteamConnection connection, DcnoList dcnoList, int edaban,String invoiceFlg) {
		List<GMap> shiwakeDataList = dcnoList.getShiwakeList();
		log.info("仕訳インポート開始 件数：" + shiwakeDataList.size());

		//伝票番号（リンク番号）毎のフォルダ「C:\eteam\tmp\ImpFile_X(Xはリンク番号」配下に伝票番号配下→伝票ID配下→添付ファイル(e文書)・関連伝票を出力
		shiwakeLogic.outLinkFiles(dcnoList);
		
		// インポータ送信内容決定
		Open21ShiwakeData shiwakeData = shiwakeLogic.createOpen21ShiwakeData(dcnoList,invoiceFlg);
		//現状レイアウト(Rno)は仕訳行単位ではなく、インポータ仕訳明細 Listにつき一つ
		// →ココに来るまでに旧とinvoiceで分けておく（loadShiwakeData()でデータをloadする時点で）
		shiwakeData.setLogFname(shiwakeData.getLogFname().substring(0, shiwakeData.getLogFname().length() -4));
		shiwakeData.setLogFname(shiwakeData.getLogFname() + "_" + EteamCommon.getContextSchemaName());
		shiwakeData.setLogFname(shiwakeData.getLogFname() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".log");

		//財務転記(OPEN21インポーターDLL呼び出し)
		int retCode;
		if ("1".equals(setting.shiwakeKbn())) {
			// 日次設定の場合
			retCode = Open21ShiwakeImportLogic.importShiwakeM(shiwakeData);
		} else {
			// 拡張設定の場合
			retCode = Open21ShiwakeImportLogic.importShiwakeKM(shiwakeData);
		}
		if (retCode <= 0) {
			try {
				// エラーログをDBに保存
				String fileName = shiwakeData.getLogFname();
				String filePath = shiwakeData.getLogPath();
				// バッチ不良ログファイル紐づけ(batch_log_invalid_log_himoduke)
				common.insertBatchErrorLogHimoduke(getSerialNo(), edaban, fileName.substring(0,fileName.length()-4));
				
				// 不良データログ(batch_log_invalid_file_log)
				File invalidFile = new File(filePath, fileName);
				if(invalidFile.exists()) {
					BufferedReader invalidFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(invalidFile),"SJIS"));
					String invalidFileData;
					while ((invalidFileData = invalidFileReader.readLine()) != null) {
						String[] d = invalidFileData.split(",");
						if (retCode != -56) {
							common.insertInvalidFileLog(fileName.substring(0,fileName.length()-4), Integer.parseInt(d[0]), Integer.parseInt(d[1]), d[2], d[3], d[4]);
						}else {
							//-56(リンク情報エラー)のみ３項目目と４項目目を繋げる
							common.insertInvalidFileLog(fileName.substring(0,fileName.length()-4), Integer.parseInt(d[0]), Integer.parseInt(d[1]), d[2]+"："+d[3], d[4], d[5]);
						}
					}
					invalidFileReader.close();
				}
				
				// 不良伝票ログ(batch_log_invalid_denpyou_log)
				File invalidDenpyou = new File(filePath, fileName.substring(0,fileName.length()-4)+"_2.log");
				if(invalidDenpyou.exists()) {
					BufferedReader invalidDenpyouReader = new BufferedReader(new InputStreamReader(new FileInputStream(invalidDenpyou),"SJIS"));
					String invalidDenpyouData;
					while ((invalidDenpyouData = invalidDenpyouReader.readLine()) != null) {
						String[] d = invalidDenpyouData.split(",");
						common.insertInvalidDenpyouLog(fileName.substring(0,fileName.length()-4)
								, Integer.parseInt(d[0])
								, Integer.parseInt(d[1])
								, new Date(new SimpleDateFormat("yyyyMMdd").parse(d[2]).getTime())
								, d[3]
								, new BigDecimal(d[4].replaceAll(",", ""))
								, d[5]
								, d[6]);
					}
					invalidDenpyouReader.close();
				}
				
				//取得対象エラーログが両方とも存在しない場合、不良データログにエラーメッセージのみ登録
				if( (!invalidFile.exists()) && (!invalidDenpyou.exists()) ) {
					common.insertInvalidFileLog(fileName.substring(0,fileName.length()-4), 0, 0, "-", "-", Open21ShiwakeImportLogic.getErrorMessage(retCode));
				}
				
			} catch (Exception e) {
				log.error("不良データログをDBに保存時にエラー", e);
			}
			// 個別エラー番号をエラーログに出力(全て仕訳エラーとして扱う)
			throw new EteamShiwakeErrorException(Open21ShiwakeImportLogic.getErrorMessage(retCode));
		}

		// 仕訳抽出状態更新
		for (GMap map : shiwakeDataList) {
			long serialNo = (long)map.get("serial_no");
			shiwakeLogic.updateShiwakeStatusSumi(serialNo);
		}
		
		//WFの更新を残す
		for (GMap map : shiwakeDataList) {
			String denpyouId = (String)map.get("denpyou_id");
			if (! denpyouIdZumi.containsKey(denpyouId)) {
				// 承認状況に「OPEN21転記」の履歴を登録
				shiwakeLogic.insertOPEN21TenkiSyouninJoukyou(denpyouId);
				//伝票一覧テーブル状態更新(詳細部分のみ)
				diLogic.updateDenpyouIchiran(denpyouId);
				denpyouIdZumi.put(denpyouId, Boolean.TRUE);
			}
		}
		log.info("仕訳インポート終了");
	}
}
