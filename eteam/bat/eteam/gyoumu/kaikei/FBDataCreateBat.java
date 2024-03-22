package eteam.gyoumu.kaikei;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import eteam.base.EteamAbstractBat;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_CD;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_KBN;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.kaikei.common.FBData;
import lombok.Getter;
import lombok.Setter;

/**
 * FBデータ作成バッチ
 * @author takahashi_ryousuke
 *
 */
public class FBDataCreateBat extends EteamAbstractBat {

//＜定数＞
	/** 処理成功 */
	protected static final int SHORI_SUCCESS = 0;
	/** 処理失敗 */
	protected static final int SHORI_FAILED = 1;
	/** 処理続行 */
	protected static final int SHORI_CONTINUE = 2;
	
	/** 郵貯フォーマット(固定長)*/
	protected static final String FORMAT_YUUCHO = "FY";
	/** 全銀フォーマット(固定長形式)*/
	protected static final String FORMAT_ZENGIN_KOTEI = "ZK";
	/** 全銀フォーマット(XML形式)*/
	protected static final String FORMAT_ZENGIN_XML = "ZX";
	
	/** Ustrdタグ区切り文字数 */
	protected static final int USTRD_CUT = 76;

//＜呼び出し元との授受用途＞
	/** 個人精算支払日 */
	@Setter
	protected Date kojinSeisanShiharaibi;
	/** 画面へのレスポンスである */
	@Setter
	protected boolean gamenResponse;
	/** FBデータ(振込口座単位） */
	@Getter
	protected List<ByteArrayOutputStream> bstList = new ArrayList<>();
	/** FBデータファイル名リスト */
	@Getter
	protected List<String> fnameList = new ArrayList<>();
	
	//このオブジェクトに格納される情報
	//List
	//　├String（FBデータファイルパス１）
	//　└String（FBデータファイルパス１）

//＜クラス内グローバル：FBデータの原型を詰めていく＞
	//このオブジェクトに格納される情報
	//List
	//　├Map（FBデータ１）
	//　│　├key=header, value=ヘッダー情報List←getHeaderList()で作ったやつ
	//　│　│　　　　　　　　　　　├0:データ種別
	//　│　│　　　　　　　　　　　├1:種別コード
	//　│　│　　　　　　　　　　　├...以下略
	//　│　├key=data, value=データ情報List
	//　│　├key=trailer, value=トレーラー情報List
	//　│　└key=end, value=エンドレコード情報リスト
	//　└Map（FBデータ２）
	/** FBデータ出力ファイルパス */
	protected List<String> fpathList = new ArrayList<String>();
	/** FBファイル情報マップリスト */
	protected List<GMap> fbMapList;

//＜部品＞
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(FBDataCreateBat.class);
	/** FBデータ作成ロジック */
	protected FBDataCreateLogic fbdcl;
	/** システム管理カテゴリSELECTロジック */
	protected SystemKanriCategoryLogic sysl;

	@Override
	public String getBatchName() {
		return "FBデータ作成";
	}

	@Override
	public String getCountName() {
		return "FBデータファイル";
	}

	@Override
	public int mainProc() {
		try(EteamConnection connection = EteamConnection.connect()) {
			init(connection);

			//機能OFFであれば何もせず終わり
			GMap kinouSeigyo = sysl.findKinouSeigyo(KINOU_SEIGYO_CD.FBDATA_SAKUSEI);
			if(! KINOU_SEIGYO_KBN.YUUKOU.equals(kinouSeigyo.get("kinou_seigyo_kbn"))) {
				log.info("FB機能がOFFなので終了します。");
				return SHORI_SUCCESS;
			}
			
			
			
			
			fbMapList = new ArrayList<GMap>();
			int ret = 1;

			// 個人精算支払日の取得。（外部から個人精算支払日指定されている場合はそれを使うので取得せず）
			//→日付データがなければ異常終了。
			//→FBデータ作成日でなければ正常終了。
			if (null == kojinSeisanShiharaibi) {
				ret = readKojinSeisanShiharaiDate();
				if (ret != SHORI_CONTINUE) {
					return ret;
				}
			}
			
			// ヘッダレコード(振込元)の取得
			// 本日作成分の対象振込がなければ正常終了。
			List<GMap> headerList = fbdcl.loadHeadRecord(kojinSeisanShiharaibi);
			if (headerList.size() == 0) {
				log.info("本日作成分の対象振込がありません。");
				return SHORI_SUCCESS;
			}
			
			boolean soejiUmFlg = false;
			boolean nextRecordUmFlg = false;
			int soeji = 0;
			// データレコードの取得。以下の変数にセット
			// 出力ファイル名、パス→fnameList、fpathList
			// 出力データ→fbMapList
			List<GMap> dataList = null;
			for (int i = 0 ; i < headerList.size() ; i++){
				GMap headerMap = headerList.get(i);
				FBData fbData = new FBData();
// fbData.setShubetsuCd(String.valueOf(headerMap.get("shubetsu_cd")));
// fbData.setCdKbn(String.valueOf(headerMap.get("cd_kbn")));
				fbData.setFurikomiDate((Date)headerMap.get("furikomi_date"));
// fbData.setKaishaCd(String.valueOf(headerMap.get("kaisha_cd")));
				fbData.setMotoKinyuukikanCd(headerMap.getString("moto_kinyuukikan_cd"));
				fbData.setMotoKinyuukikanShitenCd(headerMap.getString("moto_kinyuukikan_shiten_cd"));
				fbData.setMotoYokinShumokuCd(headerMap.getString("moto_yokin_shumoku_cd"));
				fbData.setMotoKouzaBangou(headerMap.getString("moto_kouza_bangou"));
				
				// データレコードの取得
				dataList = fbdcl.loadDataRecord(fbData);

				// トレーラレコードの取得
// GMap trailerMap = fbdcl.loadTrailerRecord(fbData);
				GMap trailerMap = fbdcl.makeTrailerRecord(dataList);
				
				
				// 振込元金融機関コードと振込元金融機関支店コードを取得
				String nowMotokinyuukikan = fbData.getMotoKinyuukikanCd() + fbData.getMotoKinyuukikanShitenCd();
				// 次ヘッダーレコードの振込元金融機関コードと振込元金融機関支店コードを取得
				String nextMotokinyuukikan = "";
				if(i < (headerList.size() - 1)){
					GMap nextHeaderMap = headerList.get(i + 1);
					nextMotokinyuukikan = nextHeaderMap.getString("moto_kinyuukikan_cd") + nextHeaderMap.getString("moto_kinyuukikan_shiten_cd");
				}
				
				// 振込元金融機関コードと振込元金融機関支店コードが次ヘッダレコードと等しい場合
				if(nextMotokinyuukikan.length() != 0 && nowMotokinyuukikan.equals(nextMotokinyuukikan)){
					soejiUmFlg = true;
					nextRecordUmFlg = true;
				}else{
					soejiUmFlg = (nextRecordUmFlg)? true : false ;
					nextRecordUmFlg = false;
				}
				
				// ファイル名を決定
				String fname = (soejiUmFlg)?
						MessageFormat.format(setting.outputFileNmFbdata(), new SimpleDateFormat("yyyyMMdd").format(kojinSeisanShiharaibi), nowMotokinyuukikan + "_" + String.valueOf(++soeji)):
						MessageFormat.format(setting.outputFileNmFbdata(), new SimpleDateFormat("yyyyMMdd").format(kojinSeisanShiharaibi), nowMotokinyuukikan);
				String fpath = setting.outputFilePathFbdata() + File.separator + fname; 
				fnameList.add(fname);
				fpathList.add(fpath);
				
				// ファイル名添え字の初期化
				if(!nextRecordUmFlg){
					soeji = 0;
				}
				
				// CSVファイル情報の生成
				GMap fbMap = createCsvDataInfo(headerMap, dataList, trailerMap);
				fbMapList.add(fbMap);
				
			}
			
			
			//ファイル作成
			makeFBFile();

			// 承認状況に「FBデータ作成」の履歴を登録
			int count = fbdcl.insertFBDataSakuseiSyouninJoukyou(kojinSeisanShiharaibi);
			log.info("「FBデータ作成」の承認状況登録件数：" + count);
			
			// 仕訳抽出状態更新
			count = fbdcl.updateFBStatus(kojinSeisanShiharaibi);
			log.info("FB抽出状態更新件数：" + count);
			
			// 伝票一覧テーブルのFBデータ作成フラグ更新
			fbdcl.updateDenpyouIchiranFBStatus(kojinSeisanShiharaibi);

			// 後処理
			if (dataList != null) {
				afterMainExt(connection, dataList);
			}
			
			//コミット
			connection.commit();
			setCount(fpathList.size());
			return SHORI_SUCCESS;
		}catch (Throwable e) {
			log.error("エラー発生", e);
			return SHORI_FAILED;
		}
	}

	/**
	 * mainの後処理
	 * @param connection コネクション
	 * @param dataList データリスト
	 */
	protected void afterMainExt(EteamConnection connection, List<GMap> dataList) {}

	/**
	 * 部品初期化
	 * @param connection コネクション
	 */
	protected void init(EteamConnection connection) {
		fbdcl = EteamContainer.getComponent(FBDataCreateLogic.class, connection);
		sysl = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);
	}
	
	/**
	 * 個人精算支払日取得
	 * @return 0:処理成功 1:処理失敗 2:処理続行
	 */
	protected int readKojinSeisanShiharaiDate() {
		
		/** FBデータ作成日フラグ */
		String fbDataSakuseiFlg;
		
		
		List<GMap> list = fbdcl.loadHizukeControl();
		
		if (list.size() == 0) {
			log.info("当日区分 = 1のレコードが存在しません。");
			return SHORI_SUCCESS;
		}
		
		for (GMap map : list) {
			// FBデータ作成日フラグ
			fbDataSakuseiFlg = map.getString("fb_data_sakuseibi_flg");
			// 個人精算支払日
			kojinSeisanShiharaibi =(Date) map.get("kojinseisan_shiharaibi");
			
			if (! "1".equals(fbDataSakuseiFlg)) {
				log.info("FBデータ作成日ではありません。");
				return SHORI_SUCCESS;
			}
			
		}
		return SHORI_CONTINUE;
	}

	/**
	 * CSVファイル情報を生成する
	 * @param headMap ヘッダレコード
	 * @param dataList データレコード
	 * @param trailerMap トレイラレコード
	 * @return FBデータマップ
	 */
	protected GMap createCsvDataInfo(GMap headMap , List<GMap>dataList , GMap trailerMap) {

		// 振込元金融機関コード
		String kikanCd = headMap.getString("moto_kinyuukikan_cd");
		
		GMap retMap = new GMap();

		if (setting.kinyukikanCdYuucho().equals(kikanCd)) {
			// 郵貯FBデータの作成
			retMap.put("format", FORMAT_YUUCHO);
			// ヘッダレコード
			ArrayList<String> headerRecList = getHeaderList(headMap);
			retMap.put("header", headerRecList);
			
			// データレコード
			ArrayList<ArrayList<String>> dataRecList = getDataYList(headMap,dataList);
			retMap.put("data", dataRecList);
			
			// トレーラレコード
			ArrayList<String> trailerRecList = getTrailList(headMap,trailerMap);
			retMap.put("trailer", trailerRecList);
			
			// エンドレコード
			ArrayList<String> endRecList = getEndList();
			retMap.put("end", endRecList);
		} else {
			// 全銀FBデータの作成
			
			if("0".equals(setting.zenginFurikomiKeishiki())) {
				//固定長形式
				retMap.put("format", FORMAT_ZENGIN_KOTEI);
				// ヘッダレコード
				ArrayList<String> headerRecList = getHeaderList(headMap);
				retMap.put("header", headerRecList);
				
				// データレコード
				ArrayList<ArrayList<String>> dataRecList = getDataZList(headMap,dataList);
				retMap.put("data", dataRecList);
				
				// トレーラレコード
				ArrayList<String> trailerRecList = getTrailList(headMap,trailerMap);
				retMap.put("trailer", trailerRecList);
				
				// エンドレコード
				ArrayList<String> endRecList = getEndList();
				retMap.put("end", endRecList);
			}else {
				//XML形式
				retMap.put("format", FORMAT_ZENGIN_XML);
				// ヘッダレコード(ZEDI用)
				ArrayList<String> headerRecList = getHeaderListXml(headMap);
				retMap.put("header", headerRecList);
				
				// データレコード(ZEDI用)
				ArrayList<ArrayList<String>> dataRecList = getDataZListXml(headMap,dataList);
				retMap.put("data", dataRecList);
				
				// トレーラレコード(ZEDI用)
				ArrayList<String> trailerRecList = getTrailListXml(headMap,trailerMap);
				retMap.put("trailer", trailerRecList);
			}
			
		}
		return retMap;
	}
	
	/**
	 * ヘッダリストを返却する
	 * @param headMap ヘッダレコード
	 * @return ヘッダリスト
	 */
	protected ArrayList<String> getHeaderList(GMap headMap) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("1"); // データ区分(1)
		list.add(valuePad(headMap.getString("shubetsu_cd"),2)); // 種別コード(2)
		list.add(valuePad(headMap.getString("cd_kbn"),1)); // コード区分(1)
		list.add(valuePad(headMap.getString("kaisha_cd"),10)); // 委託者コード(10)
		list.add(rightPad(headMap.getString("kaisha_name_hankana"), 40)); // 委託者名(40)桁埋めする
		list.add(toMMdd(((Date)headMap.get("furikomi_date")))); // 取組日(4)mmdd変換
		list.add(valuePad(headMap.getString("moto_kinyuukikan_cd"),4)); // 仕向金融機関番号(4)
		list.add(rightPad(headMap.getString("moto_kinyuukikan_name_hankana"), 15)); // 仕向金融機関名(15)桁埋め
		list.add(valuePad(headMap.getString("moto_kinyuukikan_shiten_cd"),3)); // 仕向支店番号(3)
		list.add(rightPad(headMap.getString("moto_kinyuukikan_shiten_name_hankana"), 15)); // 仕向支店名(15)桁埋め
		list.add(valuePad(headMap.getString("moto_yokin_shumoku_cd"),1)); // 預金種目(依頼人)(1)
		list.add(valuePad(headMap.getString("moto_kouza_bangou"),7)); // 口座番号(依頼人)(7)
		list.add(rightPad("", 17)); // ダミー(17)桁埋め
		return list;
	}
	
	/**
	 * データリスト(全銀)を返却する
	 * @param headMap ヘッダレコード
	 * @param dataList データレコード
	 * @return データリスト
	 */
	protected ArrayList<ArrayList<String>> getDataZList(GMap headMap, List<GMap> dataList) {
		boolean useShainCd = setting.shainCd2KokyakuCd1().equals("1");
		ArrayList<ArrayList<String>> rowList = new ArrayList<ArrayList<String>>();
		for ( int i = 0 ; i < dataList.size() ; i++ ){
			ArrayList<String> list = new ArrayList<String>();
			list.add("2"); //データ区分(1)
			list.add(valuePad(dataList.get(i).getString("saki_kinyuukikan_cd"),4)); //被仕向金融機関番号(4)
			list.add(rightPad(dataList.get(i).getString("saki_kinyuukikan_name_hankana"),15)); //被仕向金融機関名(15)
			list.add(valuePad(dataList.get(i).getString("saki_kinyuukikan_shiten_cd"),3)); //被仕向支店番号(3)
			list.add(rightPad(dataList.get(i).getString("saki_kinyuukikan_shiten_name_hankana"),15)); //被仕向支店名(15)
			list.add(rightPad("",4)); //手形交換所番号(4)
			list.add(valuePad(dataList.get(i).getString("saki_yokin_shumoku_cd"),1)); //預金種目(1)
			list.add(valuePad(dataList.get(i).getString("saki_kouza_bangou"),7)); //口座番号(7)
			list.add(rightPad(dataList.get(i).getString("saki_kouza_meigi_kana"),30)); //受取人名(30)
			list.add(valuePad(dataList.get(i).getString("kingaku"),10)); //振込金額(10)
			list.add(valuePad(dataList.get(i).getString("shinki_cd"),1)); //新規コード(1)
			list.add(valuePad(useShainCd ? dataList.get(i).getString("kokyaku_cd1") : "",20)); //顧客コード１・２(20)
			list.add(valuePad(dataList.get(i).getString("furikomi_kbn"),1)); //振込区分(1)
			list.add(dataList.get(i).containsKey("ediFlg") ? "Y" : " "); //識別表示(1)
			list.add(rightPad("",7)); //ダミー(7)
			rowList.add(list);
		}
		return rowList;
	}
	
	/**
	 * データリスト(郵貯)を返却する
	 * @param headMap ヘッダレコード
	 * @param dataList データレコード
	 * @return データリスト
	 */
	protected ArrayList<ArrayList<String>> getDataYList(GMap headMap, List<GMap> dataList) {
		boolean useShainCd = setting.shainCd2KokyakuCd1().equals("1");
		ArrayList<ArrayList<String>> rowList = new ArrayList<ArrayList<String>>();
		for ( int i = 0 ; i < dataList.size() ; i++ ){
			ArrayList<String> list = new ArrayList<String>();
			list.add("2"); //データ区分(1)
			list.add(valuePad(dataList.get(i).getString("saki_kinyuukikan_cd"),4)); //引落銀行番号(4)
			list.add(rightPad(dataList.get(i).getString("saki_kinyuukikan_name_hankana"),15)); //引落銀行名(15)
			list.add(valuePad(dataList.get(i).getString("saki_kinyuukikan_shiten_cd"),3)); //通帳記号[引落支店番号](3)
			list.add(rightPad(dataList.get(i).getString("saki_kinyuukikan_shiten_name_hankana"),15)); //予備[引落支店名](15)
			list.add(rightPad("",4)); //ダミー(4)
			list.add(valuePad(dataList.get(i).getString("saki_yokin_shumoku_cd"),1)); //予備[引落預金種類](1)
			list.add(valuePad(dataList.get(i).getString("saki_kouza_bangou"),7)); //通帳番号[引落口座番号](7)
			list.add(rightPad(dataList.get(i).getString("saki_kouza_meigi_kana"),30)); //貯金者名[預金者名](30)
			list.add(valuePad(dataList.get(i).getString("kingaku"),10)); //引落金額(10)
			list.add(valuePad(dataList.get(i).getString("shinki_cd"),1)); //新規コード(1)
			list.add(valuePad(useShainCd ? dataList.get(i).getString("kokyaku_cd1") : "",20)); //顧客番号(20)
			list.add(valuePad(dataList.get(i).getString("furikomi_kbn"),1)); //振込結果コード(1)
			list.add(rightPad("",8)); //ダミー(8)
			rowList.add(list);
		}
		return rowList;
	}
	
	/**
	 * トレイラリストを返却する
	 * @param headMap レッダレコード
	 * @param trailerMap トレイラレコード
	 * @return トレイラリスト
	 */
	protected ArrayList<String> getTrailList(GMap headMap, GMap trailerMap) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("8"); //データ区分(1)
		list.add(valuePad(trailerMap.getString("goukei_kensuu"),6)); //合計件数(6)
		list.add(valuePad(trailerMap.getString("goukei_kingaku"),12)); //合計金額(12)
		list.add(rightPad("",101)); //ダミー(101)
		return list;
	}
	
	/**
	 * エンドリストを返却する
	 * @return エンドリスト
	 */
	protected ArrayList<String> getEndList() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("9"); //データ区分(1)
		list.add(rightPad("",119)); //ダミー(119)
		return list;
	}
	
	
	
	/**
	 * ヘッダリストを返却する(ZEDI向け用に調整)
	 * @param headMap ヘッダレコード
	 * @return ヘッダリスト
	 */
	protected ArrayList<String> getHeaderListXml(GMap headMap) {
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("1"); // データ区分(1)
		list.add(headMap.getString("shubetsu_cd")); // 種別コード(2)
		list.add(headMap.getString("cd_kbn")); // コード区分(1)
		list.add(headMap.getString("kaisha_cd")); // 委託者コード(10)
		list.add(headMap.getString("kaisha_name_hankana")); // 委託者名(40)
		list.add(date.format((Date)headMap.get("furikomi_date"))); // 取組日(4)yyyy-MM-dd変換
		list.add(headMap.getString("moto_kinyuukikan_cd")); // 仕向金融機関番号(4)
		list.add(headMap.getString("moto_kinyuukikan_name_hankana")); // 仕向金融機関名(15)
		list.add(headMap.getString("moto_kinyuukikan_shiten_cd")); // 仕向支店番号(3)
		list.add(headMap.getString("moto_kinyuukikan_shiten_name_hankana")); // 仕向支店名(15)
		list.add(headMap.getString("moto_yokin_shumoku_cd")); // 預金種目(依頼人)(1)
		list.add(headMap.getString("moto_kouza_bangou")); // 口座番号(依頼人)(7)
		list.add(rightPad("", 17)); // ダミー(17)桁埋め
		return list;
	}
	
	/**
	 * データリスト(全銀)を返却する(ZEDI向け用に調整)
	 * @param headMap ヘッダレコード
	 * @param dataList データレコード
	 * @return データリスト
	 */
	protected ArrayList<ArrayList<String>> getDataZListXml(GMap headMap, List<GMap> dataList) {
		boolean useShainCd = setting.shainCd2KokyakuCd1().equals("1");
		ArrayList<ArrayList<String>> rowList = new ArrayList<ArrayList<String>>();
		for ( int i = 0 ; i < dataList.size() ; i++ ){
			ArrayList<String> list = new ArrayList<String>();
			list.add("2"); //データ区分(1)
			list.add(dataList.get(i).getString("saki_kinyuukikan_cd")); //被仕向金融機関番号(4)
			list.add(dataList.get(i).getString("saki_kinyuukikan_name_hankana")); //被仕向金融機関名(15)
			list.add(dataList.get(i).getString("saki_kinyuukikan_shiten_cd")); //被仕向支店番号(3)
			list.add(dataList.get(i).getString("saki_kinyuukikan_shiten_name_hankana")); //被仕向支店名(15)
			list.add(""); //手形交換所番号(4)
			list.add(dataList.get(i).getString("saki_yokin_shumoku_cd")); //預金種目(1)
			list.add(dataList.get(i).getString("saki_kouza_bangou")); //口座番号(7)
			list.add(dataList.get(i).getString("saki_kouza_meigi_kana")); //受取人名(30)
			list.add(dataList.get(i).getString("kingaku")); //振込金額(10)
			list.add(valuePad(dataList.get(i).getString("shinki_cd"),1)); //新規コード(1)
			list.add(useShainCd ? dataList.get(i).getString("kokyaku_cd1") : ""); //顧客コード１・２(20)
			list.add(dataList.get(i).getString("furikomi_kbn")); //振込区分(1)
			list.add((dataList.get(i).containsKey("ediFlg") ? "Y" : " ") + ":       :                 "); //固定長形式のD15（識別表示）の値 + 半角コロン + 固定長形式D16（ダミー）の値 + 半角コロン + 固定長形式のH13（ダミー）の値
			list.add(dataList.get(i).getString("denpyou_id")); //伝票ID(伝票種別の判別用)
			rowList.add(list);
		}
		return rowList;
	}
	
	
	/**
	 * トレイラリストを返却する(ZEDI向け用に調整)
	 * @param headMap レッダレコード
	 * @param trailerMap トレイラレコード
	 * @return トレイラリスト
	 */
	protected ArrayList<String> getTrailListXml(GMap headMap, GMap trailerMap) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("8"); //データ区分(1)
		list.add(trailerMap.getString("goukei_kensuu")); //合計件数(6)
		list.add(trailerMap.getString("goukei_kingaku")); //合計金額(12)
		list.add(rightPad("",101)); //ダミー(101)
		return list;
	}
	
	/**
	 * yyyy/MM/ddからMM/ddへ。nullやブランクはnullへ。
	 * @param yyyy_mm_dd 変換前
	 * @return 変換後
	 */
	protected String toMMdd(Date yyyy_mm_dd){
		if (yyyy_mm_dd == null) {
			return null;
		} else {
			String str = null;
			SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
			date.applyPattern("MMdd");
			date.setLenient(false);
			str = date.format(yyyy_mm_dd);
			return str;
		}
	}
	
	/**
	 * 指定の桁数で空白埋めをする
	 * @param str 文字列
	 * @param size 最大桁すう
	 * @return 変換後文字列
	 */
	protected String rightPad(String str, int size) {
	    StringBuilder sb = new StringBuilder();
	 
	    sb.append(str);
	 
	    for (int i = str.length(); i < size; i++) {
	        sb.append(" ");
	    }
	 
	    return sb.toString();
	}
	
	
	/**
	 * 指定の桁数で0埋めをする
	 * @param str 文字列
	 * @param size 最大桁数
	 * @return 変換後桁数
	 */
	protected String valuePad(String str , int size){
		while (str.length() < size) {
			str = "0" + str;
		}
		
		if ( str.length() > size ){
			throw new RuntimeException("サイズオーバーです");
		}
		return str;
	}
	
	/**
	 * CSVファイル情報の生成可否の判断
	 */
	@SuppressWarnings("unchecked")
	protected void makeFBFile(){
		OutputStream ops = null;
		try {
			//件数分ファイル出力
			for (int i = 0; i < fpathList.size() ; i++){
				PrintWriter pw;
				GMap map = fbMapList.get(i);
				
				//TODO 夜間バッチとしての実行時の動作確認
				//TODO 郵貯フォーマットも含めて出力した際の動作確認
				//出力用 Object作成
				//画面からの実行時はZIP化してレスポンスで返す。夜間バッチの場合は指定のパスにファイルを作る。
				if (gamenResponse) {
					ByteArrayOutputStream bst = new ByteArrayOutputStream();
					ops = bst;
					bstList.add(bst);
					OutputStreamWriter osw = new OutputStreamWriter(bst, Encoding.MS932);
					pw = new PrintWriter(osw);
				} else {
					FileOutputStream fos = new FileOutputStream(fpathList.get(i));
					ops = fos;
					OutputStreamWriter osw = new OutputStreamWriter(fos, Encoding.MS932);
					pw = new PrintWriter(osw);
				}
				
				
				
				if(!(FORMAT_ZENGIN_XML.equals(map.get("format")))){

					//ヘッダーレコード(項目数) Write
					List<String> headerList = (List<String>) map.get("header");
					for (int j = 0 ; j < headerList.size() ; j++){
						pw.write(headerList.get(j));
					}
					pw.println();
					
					//データレコード(データ数) Write
					ArrayList<ArrayList<String>> dataList = (ArrayList<ArrayList<String>>) map.get("data");
					for (int k = 0 ; k < dataList.size() ; k++){
						//データレコード(項目数)
						for (int l = 0 ; l < dataList.get(k).size() ; l++) {
							dataList.get(k).get(l);
							pw.write(dataList.get(k).get(l));
						}
						pw.println();
					}
					
					//トレイラレコード(項目数)
					List<String> trailerList = (List<String>) map.get("trailer");
					for ( int m = 0 ; m < trailerList.size() ; m++){
						pw.write(trailerList.get(m));
					}
					pw.println();
					
					//エンドレコード(項目数)
					List<String> endList =(List<String>) map.get("end");
					for ( int n = 0 ; n < endList.size() ; n++){
						pw.write(endList.get(n));
					}
					pw.println();
				}else {
					//全銀データをXMLフォーマットで出力
					try {
						makeFBXml(map,ops);
					}catch(Exception e) {
						pw.close();
						throw new RuntimeException(e);
					}
				}
				pw.close();

			}
		} catch (IOException e){
			throw new RuntimeException(e);
		} finally {
			try {
				if(ops != null)ops.close();
			}catch (IOException e){
				throw new RuntimeException(e);
			}
		}
		return;
	}
	
	/**
	 * 渡されたデータをZEDIのXML形式で指定ストリームに出力
	 * @param map 出力用データ
	 * @param ops 出力用ストリーム
	 */
	protected void makeFBXml(GMap map,OutputStream ops) {
		
		try {
			ZenginXmlClass.CstmrCdtTrfInitnClass.GrpHdrClass gs = new ZenginXmlClass.CstmrCdtTrfInitnClass.GrpHdrClass();
			
			//GrpHdrクラス
			List<ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass> pml = new ArrayList<ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass>();
			
			List<String> hdMap = map.get("header");
			List<List<String>> dtMapLst = map.get("data");
			List<String> trMap = map.get("trailer");

			//会社設定に指定した内容を出力 {0}を振込日（yyyymmdd）、{1}を振込元金融機関コード+支店コード、{2}を振込元口座番号で置換
			String msgIdStr = setting.xmlMessageShikibetsuNo();
			msgIdStr = MessageFormat.format(setting.xmlMessageShikibetsuNo(), hdMap.get(5).replace("-",""), hdMap.get(6) + hdMap.get(8), hdMap.get(11));
			gs.setMsgId(msgIdStr); 
			
			//ファイル作成日時	形式：“YYYY-MM-DDThh:mm:ss”(Tは固定文字)
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss"); //現在日時を取得してフォーマット調整
			String dateTimeStr =sdf.format(new Date(System.currentTimeMillis()));
			gs.setCreDtTm(dateTimeStr);
			
			//支払い情報の繰り返し数
			int nbOfTxsCnt = 1; //※現状ではXML内で支払い情報は1件のみのため固定
			String nbOfTxsStr = Integer.toString(nbOfTxsCnt);
			gs.setNbOfTxs(nbOfTxsStr);
			ArrayList<String> dmyLst = new ArrayList<String>();
			dmyLst.add("");
			gs.setInitgPty(dmyLst); //空要素タグ<InitgPty/>として出力
			
			//PmtInfクラス
			JAXBContext jc = JAXBContext.newInstance(ZenginXmlClass.class);
			
			ZenginXmlClass ts = new ZenginXmlClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass tr = new ZenginXmlClass.CstmrCdtTrfInitnClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass pm = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass();
			
			pm.setPmtInfId("1"); //固定値「1」
			pm.setPmtMtd("TRF"); //固定値「TRF」
			pm.setNbOfTxs(trMap.get(1)); //T2の値を出力(合計件数)
			pm.setCtrlSum(trMap.get(2)); //T3の値を出力(合計金額)
			
			ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.PmtTpInfClass pt = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.PmtTpInfClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.PmtTpInfClass.CtgyPurpClass ct = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.PmtTpInfClass.CtgyPurpClass();
			
			ct.Cd = "OTHR"; //固定値「OTHR」
			pt.setCtgyPurp(ct);
			pm.setPmtTpInf(pt);
			
			pm.setReqdExctnDt(hdMap.get(5)); //H6(取組日)の値を「YYYY-MM-DD」形式で出力
			
			ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.DbtrClass dbt = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.DbtrClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.IdClass id = new ZenginXmlClass.CstmrCdtTrfInitnClass.IdClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.OrgIdClass oi = new ZenginXmlClass.CstmrCdtTrfInitnClass.OrgIdClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass ocA = new ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass ocB = new ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass();
			List<ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass> otls = new ArrayList<ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass>();
			ZenginXmlClass.CstmrCdtTrfInitnClass.SchmeNmClass scA = new ZenginXmlClass.CstmrCdtTrfInitnClass.SchmeNmClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.SchmeNmClass scB = new ZenginXmlClass.CstmrCdtTrfInitnClass.SchmeNmClass();
			ocA.Id = hdMap.get(3); //H4の値を出力
			scA.Cd = "BANK"; //固定値「BANK」
			ocA.SchmeNm = scA;
			
			otls.add(ocA);
			
			//OPEN21にてマスタ取込実施したマイナンバー値を取得・出力させる
			String hjbStr = KaishaInfo.getKaishaInfo(ColumnName.HOUJIN_BANGOU);
			if((!"null".equals(hjbStr)) && (!hjbStr.isEmpty())) { //設定値nullの場合文字列「null」が返却されるらしいため念のため比較
				ocB.Id = hjbStr; //会社設定法人マイナンバーの値を出力
				scB.Cd = "TXID"; //固定値「TXID」だが法人マイナンバーがブランクの場合はタグごと出力させない
				ocB.SchmeNm = scB;
				otls.add(ocB);
			}
			oi.Othr = otls;
			id.OrgId = oi;
			dbt.setId(id);
			pm.setDbtr(dbt);
			
			
			ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.DbtrAcctClass dbta = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.DbtrAcctClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.IdClass ida = new ZenginXmlClass.CstmrCdtTrfInitnClass.IdClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.TpClass tpa = new ZenginXmlClass.CstmrCdtTrfInitnClass.TpClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass ociA = new ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass();
			
			ociA.Id = hdMap.get(11); //H12の値を出力
			//Tp要素は振込依頼人預金種別がブランクならタグごと出力しない
			if(!(hdMap.get(10).isEmpty())){
				ida.Othr = ociA;
				tpa.Prtry = hdMap.get(10); //H11の値を出力
				dbta.setTp(tpa);
			}
			dbta.setId(ida);
			
			pm.setDbtrAcct(dbta);
			
			ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.DbtrAgtClass dbtg = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.DbtrAgtClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.FinInstnIdClass fii = new ZenginXmlClass.CstmrCdtTrfInitnClass.FinInstnIdClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.BrnchIdClass bri = new ZenginXmlClass.CstmrCdtTrfInitnClass.BrnchIdClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.ClrSysMmbIdClass clr = new ZenginXmlClass.CstmrCdtTrfInitnClass.ClrSysMmbIdClass();
			ZenginXmlClass.CstmrCdtTrfInitnClass.ClrSysIdClass clri = new ZenginXmlClass.CstmrCdtTrfInitnClass.ClrSysIdClass();
			
			clri.Cd = "JPZGN"; //固定値「JPZGN」
			clr.ClrSysId = clri;
			clr.MmbId = hdMap.get(6); //H7の値を出力
			fii.ClrSysMmbId = clr;
			fii.Nm = hdMap.get(7); //H8の値を出力
			bri.Id = hdMap.get(8); //H9の値を出力
			bri.Nm = hdMap.get(9); //H10の値を出力
			dbtg.setFinInstnId(fii);
			dbtg.setBrnchId(bri);
			
			pm.setDbtrAgt(dbtg);
			
			
			ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.UltmtDbtrClass ud = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.UltmtDbtrClass();
			ud.Nm = hdMap.get(4); //H5の値を出力
			
			pm.setUltmtDbtr(ud);
			
			
			//振込先明細をCdtTrfTxInfでループさせる
			List<ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass> cml = new ArrayList<ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass>();
			int recCnt = 1;
			for(List<String> dtMap : dtMapLst) {
				
				ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass cm = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass();
				
				ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.PmtIdClass pmt = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.PmtIdClass();
				pmt.EndToEndId = Integer.toString(recCnt++); //明細通番(データレコード出力順に1から採番)
				cm.setPmtId(pmt);
				
				ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.AmtClass am = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.AmtClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.AmtClass.InstdAmtClass im = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.AmtClass.InstdAmtClass();
				im.setInstdAmt(dtMap.get(9)); //D10の値を出力
				im.setCcy("JPY"); //固定値「JPY」
				am.setIAmtCls(im);
				cm.setAmt(am);
				
				
				ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.CdtrAgtClass cda = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.CdtrAgtClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.FinInstnIdClass fin = new ZenginXmlClass.CstmrCdtTrfInitnClass.FinInstnIdClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.ClrSysMmbIdClass clsm = new ZenginXmlClass.CstmrCdtTrfInitnClass.ClrSysMmbIdClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass coth = new ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.BrnchIdClass cbri = new ZenginXmlClass.CstmrCdtTrfInitnClass.BrnchIdClass();
				
				clsm.MmbId = dtMap.get(1); //D2の値を出力
				fin.ClrSysMmbId = clsm;
				fin.Nm = dtMap.get(2); //D3の値を出力
				//手形交換所番号がブランクならタグごと出力しない
				//※現状の動作では必ずブランクになる？
				if(!(dtMap.get(5).isEmpty())) {
					coth.Id = dtMap.get(5); //D6の値を出力
					fin.Othr = coth;
				}
				cbri.Id = dtMap.get(3); //D4の値を出力
				cbri.Nm = dtMap.get(4); //D5の値を出力
				cda.FinInstnId = fin;
				cda.BrnchId = cbri;
				cm.setCdtrAgt(cda);
				
				ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.CdtrClass cdtr = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.CdtrClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.IdClass cdtid = new ZenginXmlClass.CstmrCdtTrfInitnClass.IdClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.OrgIdClass ctdorg = new ZenginXmlClass.CstmrCdtTrfInitnClass.OrgIdClass();
				List<ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass> ctdol = new ArrayList<ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass>();
				//ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass othrA = new ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass othrB = new ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass();
				//ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass othrC = new ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass();
				//ZenginXmlClass.CstmrCdtTrfInitnClass.SchmeNmClass otscA = new ZenginXmlClass.CstmrCdtTrfInitnClass.SchmeNmClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.SchmeNmClass otscB = new ZenginXmlClass.CstmrCdtTrfInitnClass.SchmeNmClass();
				//ZenginXmlClass.CstmrCdtTrfInitnClass.SchmeNmClass otscC = new ZenginXmlClass.CstmrCdtTrfInitnClass.SchmeNmClass();
				
				cdtr.Nm = dtMap.get(8); //D9の値を出力
				//otscA.Cd = "oac"; //タグごと出力させない
				//othrA.Id = "oia";
				//othrA.SchmeNm = otscA;
				//ctdol.add(othrA);
				
				
				//D12-D13の値だが、伝票種別=支払依頼書以外で、会社情報-顧客コード1への社員コード反映=1（する）なら、社員コードの10桁目までを設定する。
				//EDI情報は金融EDI情報に設定するため、上記以外の場合はブランク。
				if(!(dtMap.get(11).isEmpty())) {
					if( !(DENPYOU_KBN.SIHARAIIRAI.equals(dtMap.get(14).substring(7, 11))) ) {
						othrB.Id = dtMap.get(11); 
						otscB.Prtry ="Customer Code1"; //固定値「Customer Code1」
						othrB.SchmeNm = otscB;
						ctdol.add(othrB);
						ctdorg.Othr = ctdol;
						cdtid.OrgId = ctdorg;
						cdtr.Id = cdtid;
					}else {
						othrB.Id = "";
					}
				}
				cm.setCdtr(cdtr);
				
				
				ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.CdtrAcctClass cdac = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.CdtrAcctClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.IdClass cdaid = new ZenginXmlClass.CstmrCdtTrfInitnClass.IdClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass otcd = new ZenginXmlClass.CstmrCdtTrfInitnClass.OthrClass();
				ZenginXmlClass.CstmrCdtTrfInitnClass.TpClass tpcd = new ZenginXmlClass.CstmrCdtTrfInitnClass.TpClass();
				
				otcd.Id = dtMap.get(7); //D8の値を出力
				cdaid.Othr = otcd;
				cdac.Id = cdaid;
				tpcd.Prtry = dtMap.get(6); //D7の値を出力
				cdac.Tp = tpcd;
				cm.setCdtrAcct(cdac);
				
				ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.InstrForCdtrAgtClass inc = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.InstrForCdtrAgtClass();
				inc.InstrInf = dtMap.get(12); //D14の値を出力
				cm.setInstrForCdtrAgt(inc);
				
				//固定長形式のD15（識別表示）の値 + 半角コロン + 固定長形式D16（ダミー）の値 + 半角コロン + 固定長形式のH13（ダミー）の値
				//具体的には、以下のいずれか。
				//『Y:       :                 』←支払依頼書でEDIに入力した場合
				//『 :       :                 』←上記以外
				cm.setInstrForDbtrAgt(dtMap.get(13));
				
				ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.PurpClass pp = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.PurpClass();
				pp.Prtry = dtMap.get(10); //D11の値を出力
				cm.setPurp(pp);
				
				ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.RmtInfClass rm = new ZenginXmlClass.CstmrCdtTrfInitnClass.PmtInfClass.CdtTrfTxInfClass.RmtInfClass();
				List<String> usrtdList = new ArrayList<String>();
				//D12-D13の値だが、伝票種別=支払依頼書で、会社情報-顧客コード1への社員コード反映=1（する）なら、申請画面で入力したEDIの値を設定する。
				//伝票種別が違うか、EDIがブランクならタグごと出力させない。
				//※支払依頼に限り、D12-13にはEDIの値が転記されている
				if( (DENPYOU_KBN.SIHARAIIRAI.equals(dtMap.get(14).substring(7, 11))) ) {
					String ediStr = dtMap.get(11);
					if(!ediStr.isEmpty()) {
						Charset chs = StandardCharsets.UTF_8;
						//S-ZEDIで出力されているタグを追加し、base64エンコード実施
						ediStr = "<EDIInf1><Content>" + ediStr + "</Content></EDIInf1>";
						String ediStrEnc = Base64.getEncoder().encodeToString(ediStr.getBytes(chs));
						usrtdList.add("MIME-Version: 1.0");
						usrtdList.add("Content-Type: text/xml");
						usrtdList.add("Content-Transfer-Encoding: base64");
						
						//76文字(USTRD_CUT)ごとに区切る
						String ediStrTmp = ediStrEnc;
						while(ediStrTmp.length() > USTRD_CUT) {
							String ediStrCut = ediStrTmp.substring(0,USTRD_CUT);
							usrtdList.add(ediStrCut);
							ediStrTmp = ediStrTmp.substring(USTRD_CUT);
						}
						usrtdList.add(ediStrTmp);
						rm.Ustrd = usrtdList;
						cm.setRmtInf(rm);
					}
				}
				cml.add(cm);
				
				
			}
			pm.setCdtTrfTxInf(cml);
			pml.add(pm);
				
			tr.setGrpHdr(gs);
			tr.setPmtInf(pml);
				
			ts.setCstmrCdtTrfInitn(tr);
			
			
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
			//念のためエンコード指定
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			//xml定義とインデントずれ対処のためTransformerを利用
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(ts, stringWriter);
			Transformer transformer;
			try {
				
				//TODO 必要なプロパティだけ使うよう整理
				
				transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.METHOD, "xml");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
				transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
				transformer.transform(new StreamSource(new StringReader(stringWriter.toString())),new StreamResult(ops));
			} catch (TransformerException e) {
				throw new RuntimeException(e);
			} catch (TransformerFactoryConfigurationError e) {
				throw new RuntimeException(e);
			}
			
			
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
