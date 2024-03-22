package eteam.gyoumu.masterkanri;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.base.exception.EteamIllegalRequestException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Encoding;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.PostgreSQLSystemCatalogsLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * マスターデータ照会画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class MasterDataShoukaiAction extends EteamAbstractAction {
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(MasterDataShoukaiAction.class);

//＜定数＞

//＜画面入力＞
	/** マスターID */
	String masterId;
	/** ヴァージョン(パラメータで渡されなければMAXを取得する)  */
	String version;
	/** 銀行コード */
	String ginkouCd;

//＜画面入力以外＞
	/** マスター名 */
	String masterName;
	/** 変更可否フラグ */
	String henkouKahiFlg;
	/** 編集可否フラグ */
	boolean editable = false;
	/** ファイル名 */
	String fileName;
	/** 銀行名 */
	String ginkouName;
	/** 検索か */
	String isSearch = "0";

	/** マスターデータ一覧 */
	List<GMap> masterDataVersionList;
	/** エラーコード */
	int errorCode = 0;
	/** エラーメッセージ */
	String errorMessage = "";

	/** 論理名リスト */
	List<String> logicalNameList  = new ArrayList<String>();
	/** 物理名リスト */
	List<String> physicalNameList = new ArrayList<String>();
	/** 列タイプリスト */
	String[] columnTypeList;
	/** 小数桁数リスト */
	boolean[] columnDecimalList;
	/** データリスト */
	List<List<String>> dataList   = new ArrayList<List<String>>();
	/** PKリスト */
	List<String> pkDataList = new ArrayList<String>();
	/** 表示可否リスト */
	List<Boolean> displayableList = new ArrayList<Boolean>();
	
	
	/** コンテンツタイプ */
	String contentType;
	/** コンテンツディスポジション */
	String contentDisposition;
	/** ダウンロードファイル用 */
	InputStream inputStream;

//＜部品＞
	/** マスターデータ管理ロジック */
	protected MasterDataKanriLogic kanriLogic;
	/** マスター管理カテゴリロジック */
	protected MasterKanriCategoryLogic masterKanriLogic;
	/** ポスグレシステム管理カテゴリロジック */
	protected PostgreSQLSystemCatalogsLogic dbLogic;
	/** マスターデータサービスロジック */
	protected MasterDataServiceLogic service;
	//＜入力チェック＞
	@Override
	protected void formatCheck() {
		checkString(masterId,   1, 50,  "マスターID",     true); // KEY
		checkNumber(version,    1,  4,  "ヴァージョン",   true); // KEY
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{masterId ,"マスターID"		,"2", "2", }, // KEY
				{version ,"ヴァージョン"		,"0", "2", }, // KEY
				{ginkouCd , "金融機関", this.isSearch, "0", } // 金融機関コード、更新時は万一空欄でもチェック対象にはしない。なお、検索時もこっちでエラーにしてしまうと他の項目まで表示されなくなるので、基本はこっちではなくjavascriptでチェック
			};
		hissuCheckCommon(list, eventNum);
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		
		//1.入力チェック
		formatCheck();
		hissuCheck(1);
		if(!errorList.isEmpty()){ return "error"; }
		
		try(EteamConnection connection = EteamConnection.connect()){
			setConnection(connection);
			
			//2.データ存在チェック

			// マスター管理版数に存在するかチェックします。
			GMap masterKanriHansuuMap = (isEmpty(version)) ? masterKanriLogic.findMasterKanriHansuuMaxV(masterId) : masterKanriLogic.findMasterKanriHansuu(masterId, Integer.parseInt(version));
			if(masterKanriHansuuMap == null) {
				throw new EteamDataNotFoundException();
			}
			
			//3.アクセス可能チェック
			// なし

			//4.処理
			
			// 表示用の値を取得します。
			version = masterKanriHansuuMap.get("version").toString();
			masterName = masterKanriHansuuMap.get("master_name").toString();
			henkouKahiFlg = masterKanriHansuuMap.get("henkou_kahi_flg").toString();
			fileName = masterKanriHansuuMap.get("file_name").toString();
			String editFlg = masterKanriHansuuMap.get("editable_flg").toString();

			String maxVersion = masterKanriLogic.findMasterKanriHansuuMaxV(masterId).get("version").toString();
			editable = StringUtils.equals(version, maxVersion) && "1".equals(editFlg);

			// ヴァージョンリストを取得、形式変換
			masterDataVersionList = masterKanriLogic.selectMasterDataVersionList(masterId);
			for(GMap map : masterDataVersionList) {
				map.put("koushin_time", formatTime(map.get("koushin_time")));
				map.put("bg_color", map.get("version").toString().equals(version) ? "wait-period-bgcolor" : map.get("delete_flg").toString().equals("0") ? "expiration-date-bgcolor" : "disabled-bgcolor");
			}

			// 列情報の取得
			MasterColumnsInfo masterColumnsInfo = dbLogic.getMasterColumnsInfo(masterId);
			
			columnTypeList = new String[masterColumnsInfo.size()];
			columnDecimalList = new boolean[masterColumnsInfo.size()];

			int jj = 0;
			for (MasterColumnInfo info : masterColumnsInfo.getColumnList()) {
				columnDecimalList[jj] = (info.getMaxDecimalLentgh() == null)? false : true ;
				columnTypeList[jj++] = info.getType();
			}
			
			// ファイルデータを読み込みます。
			byte[] fileData = (byte[]) masterKanriHansuuMap.get("binary_data");
			try {
				InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(fileData), Encoding.MS932);
				BufferedReader inFile = new BufferedReader(in);
				String line;
				int lineCount = 1;
				while ((line = inFile.readLine()) != null) {
					String[] str = line.split(",", masterColumnsInfo.size());
					
					// １行目は論理名
					if (lineCount == 1) {
						logicalNameList.addAll(Arrays.asList(str));
					// ２行目は物理名
					} else if (lineCount == 2) {
						physicalNameList.addAll(Arrays.asList(str));
					} 
					// ５行目以降はデータ
					if(lineCount > 4 && !line.isEmpty()) {
						// エスケープを戻して画面表示する
						for (int ii = 0; ii < str.length; ii++) {
							str[ii] = str[ii].replaceAll(";;;", ",");
						}
						List<String> data = new ArrayList<String>(Arrays.asList(str));
						
						// 22082401追加ロジック
						boolean isValidCode = !this.masterId.equals("kinyuukikan") || (isNotEmpty(this.ginkouCd) && this.ginkouCd.equals(str[0]));// 金融機関ではないか、金融機関でコードが検索条件と等しい場合
						if(isValidCode)
						{
							this.dataList.add(data); // 表示用データの取得元処理（22082400版以前から存在）
							this.pkDataList.add(masterColumnsInfo.createPkString(str));
							
							// 表示可否:条件整理（幣種マスターではないか、使用可能な幣種コード）
							this.displayableList.add(!masterId.equals("rate_master") || masterKanriLogic.isUsingHeishuCd(str[0]));
						}
					}
					lineCount++;
				}
				inFile.close();
				
				if (physicalNameList.isEmpty()) {
					// マスターが空の時、ヘッダ情報を取得追加する
					for (MasterColumnInfo info : masterColumnsInfo.getColumnList()) {
						physicalNameList.add(info.getName());
						logicalNameList.add(info.getDisplayName());
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			}
			
			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * マスターを更新します。
	 * @return 結果
	 */
	@SuppressWarnings({ "unchecked" })
	public String koushin() {
		HttpServletRequest request =  ServletActionContext.getRequest();
		try(EteamConnection connection = EteamConnection.connect()) {
			formatCheck();
			hissuCheck(2);
			if (!errorList.isEmpty()) {
				throw new EteamIllegalRequestException("基礎入力エラーです。");
			}

			String _count = request.getParameter("count");
			if (null == _count) {
				throw new EteamIllegalRequestException("Key項目が存在しません。");
			}
			int count = 0;
			try {
				count = Integer.parseInt(_count);
			}
			catch (Exception e) {
				throw new EteamIllegalRequestException("Key項目が不正。");
			}
			String[] types = request.getParameterValues("type");
			String[] names = request.getParameterValues("name");
			String[] keys = request.getParameterValues("key");
			if (count == 0 || types == null || names == null || keys == null ||
					count != types.length || count != keys.length) {
				throw new EteamIllegalRequestException("変更データ不正。");
			}

			List<GMap> rowMapList = new ArrayList<GMap>(types.length);
			for (int ii = 0; ii < types.length; ii++) {
				String[] values = request.getParameterValues("colv[" + ii + "]");
				GMap map = MasterColumnsInfo.convertNameValueMap(names, values);
				rowMapList.add(map);
			}

			setConnection(connection);
			
			// マスター情報の取得
			GMap master = masterKanriLogic.selectMasterKanriIchiran(masterId);
			if (null == master) {
				// マスターが存在しない
				throw new EteamIllegalRequestException("不正なマスターIDです。");
			}
			masterName = (String)master.get("master_name");

			// 変更可否
			if (!"1".equals(master.get("editable_flg"))) {
				// 変更不可エラー
				throw new EteamIllegalRequestException("変更不可マスターです。");
			}

			// バージョンチェック
			String maxVersion = masterKanriLogic.findMasterKanriHansuuMaxV(masterId).get("version").toString();
			if (!StringUtils.equals(maxVersion, version)) {
				errorMessage = "マスター情報が既に変更されています。最新情報に更新してから再度操作してください。";
				errorCode = 1;
				return "error";
			}

			// 列情報取得
			MasterColumnsInfo masterColumnsInfo = dbLogic.getMasterColumnsInfo(masterId);

			// 現行情報取得
			List<GMap> orgDataList = kanriLogic.selectData(masterId);
			// ハッシュにする
			Map<String, GMap> keyDataMap = new HashMap<String, GMap>(orgDataList.size());
			for (GMap data : orgDataList) {
				keyDataMap.put(masterColumnsInfo.createPkString(data), data);
			}
			// 差分適用
			for (int ii = 0; ii < types.length; ii++) {
				switch (types[ii]) {
				case "delete":
					if (!keyDataMap.containsKey(keys[ii])) {
						throw new EteamIllegalRequestException("削除キーがDB上に存在しません。key = " + keys[ii]);
					}
					keyDataMap.remove(keys[ii]);
					break;
				default:
					break;
				}
			}
			for (int ii = 0; ii < types.length; ii++) {
				switch (types[ii]) {
				case "add":
					keyDataMap.put(keys[ii], rowMapList.get(ii));
					break;
				case "update":
					if (!keyDataMap.containsKey(keys[ii])) {
						throw new EteamIllegalRequestException("更新キーがDB上に存在しません。key = " + keys[ii]);
					}
					keyDataMap.put(keys[ii], rowMapList.get(ii));
					break;
				default:
					break;
				}
			}
			// List化
			List<String> keyList = new ArrayList<String>(keyDataMap.keySet());
			Collections.sort(keyList);
			rowMapList = new ArrayList<GMap>(keyList.size());
			for (String key : keyList) {
				GMap map = keyDataMap.get(key);
				GMap tmp = new GMap(map.size());
				for (Entry<String, Object> entry : map.entrySet()) {
					String col = entry.getKey().toLowerCase();
					MasterColumnInfo colInfo = masterColumnsInfo.get(col);
					tmp.put(col, colInfo.convertObjectToString(entry.getValue()));
				}
				rowMapList.add(tmp);
			}
			
			// 入力チェック
			for (GMap map : rowMapList) {
				List<String> errList = masterColumnsInfo.checkInput(map);
				if (!errList.isEmpty()) {
					// 入力チェックは済んでいるはず
					throw new EteamIllegalRequestException("入力値が不正です。" + errList.toString());
				}
			}
			
			// 個別チェック
			List<String> errList = service.kobetsuCheck(rowMapList, masterId);
			if(errList.size() > 0){
				for(String err:errList){
					errorMessage += err + "\r\n";
				}
				errorCode = 1;
				return "error";
			}

			//マスターから名称等を取得する。
			service.reloadMaster(rowMapList, masterId);
			
			// ヘッダ情報の生成
			List<String[]> header = masterColumnsInfo.createCsvDisplayNamesLine();
			List<String[]> _dataList = new ArrayList<String[]>(rowMapList.size() + header.size());
			_dataList.addAll(header);
			for (GMap colMap : rowMapList) {
				_dataList.add(masterColumnsInfo.createCsvLine(colMap));
			}

			// セッションにファイル情報を格納
			byte[] data = masterColumnsInfo.createCsvByteData(_dataList);
			MasterData masterData = new MasterData();
			masterData.uploadFileFileName    = masterName + "_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".csv";
			masterData.uploadFileContentType = EteamConst.ContentType.EXCEL;
			masterData.uploadFileSize        = data.length;
			masterData.uploadFileData        = data;
			session.put(masterId, masterData);

			return "success";
		}
		catch (Exception e) {
			errorCode = -1;
			log.error("マスタ編集反映処理でエラーが発生しました。", e);

			return "error";
		}
	}

	/**
	 * ダウンロードイベント
	 * @return ResultName
	 */
	public String download() {
		//1.入力チェック
		formatCheck();
		hissuCheck(2);
		
		try(EteamConnection connection = EteamConnection.connect()){
			setConnection(connection);
			
			//2.データ存在チェック
			//3.アクセス可能チェック
			// なし

			//4.処理

			// ファイルデータ取得
			GMap masterKanriHansuuMap = masterKanriLogic.findMasterKanriHansuu(masterId, Integer.parseInt(version));
			fileName    = masterKanriHansuuMap.get("file_name").toString();
			byte[] fileData    = (byte[]) masterKanriHansuuMap.get("binary_data");
			
			// コンテンツタイプの設定を行います。
			contentType = masterKanriHansuuMap.get("content_type").toString();
			
			// コンテンツディスポジションの設定を行います。
			int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
			contentDisposition = EteamCommon.contentDisposition(browserCode, true, fileName);
			
			// ダウンロード用ファイルを作成します。
			// InputStreamを渡しておけば、後はStrutsがやってくれます。
			inputStream = new ByteArrayInputStream(fileData);
			
			//5.戻り値を返す
			return "success";
		}
	}
	
	/**
	 * ロジッククラスのインスタンス作成
	 * @param _connection DBコネクション
	 */
	protected void setConnection(EteamConnection _connection) {
		dbLogic = EteamContainer.getComponent(PostgreSQLSystemCatalogsLogic.class, _connection);
		masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, _connection);
		kanriLogic = EteamContainer.getComponent(MasterDataKanriLogic.class, _connection);
		service = EteamContainer.getComponent(MasterDataServiceLogic.class, _connection);
	}
}
