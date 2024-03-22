package eteam.gyoumu.userjouhoukanri;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamFileLogic;
import eteam.common.RegAccess;
import eteam.common.select.BumonUserKanriCategoryLogic;
import eteam.common.select.WorkflowCategoryLogic;
import eteam.gyoumu.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * ユーザー（CSVアップロード）画面Action
 */
@Getter @Setter @ToString
public class IkkatsuTourokuUserAction extends EteamAbstractAction {

//＜定数＞
	/** データ列数（ユーザー情報）*/
	static final int DATACOLUMN_NUM_USER_INFO = 15;
	/** データ列数（所属部門割り当て）*/
	static final int DATACOLUMN_NUM_SHOZOKU_BUMON_WARIATE = 7;
	/** CSVファイル名（ユーザー情報） */
	static final String CSV_FILENAME_USER_INFO = "user_info.csv";
	/** CSVファイル名（所属部門割り当て） */
	static final String CSV_FILENAME_SHOZOKU_BUMON_WARIATE = "shozoku_bumon_wariate.csv";

//＜画面入力＞
	/** ファイルオブジェクト（ユーザー情報） */
	protected File uploadFileUserInfo;
	/** ファイルオブジェクト（所属部門割り当て） */
	protected File uploadFileShozokuBumonWariate;
	/** ファイル名(uploadFileに付随：ユーザー情報) */
	protected String uploadFileUserInfoFileName;
	/** ファイル名(uploadFileに付随：所属部門割り当て) */
	protected String uploadFileShozokuBumonWariateFileName;

	//CSV出力用
	/** ダウンロードファイル用 */
	InputStream inputStream;
	/** コンテンツタイプ */
	protected String contentType;
	/** コンテンツディスポジション */
	protected String contentDisposition;
	/** EXCELファイルの長さ */
	long contentLength;

//＜部品＞
	/** マスターデータ管理ロジック */
	UserJouhouKanriLogic myLogic;
	/** 部門ユーザ管理カテゴリロジック */
	BumonUserKanriCategoryLogic bumonUserLogic;
	/** ワークフローロジック */
	WorkflowCategoryLogic wfLogic;

//＜入力チェック＞
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {
		super.checkHissu(uploadFileUserInfo, "CSVファイル（ユーザー情報）");
		super.checkHissu(uploadFileShozokuBumonWariate, "CSVファイル（所属部門割り当て）");
	}

	/**
	 * ファイル（ユーザー情報）のフォーマットチェックを行います。
	 * @param dataList CSV１行ずつのテキスト
	 */
	protected void fileFormatCheckUserInfo(List<String[]> dataList) {
		int datacolumnNum = DATACOLUMN_NUM_USER_INFO;
		if (RegAccess.checkEnableZaimuKyotenOption())
		{
			datacolumnNum++;
		}
		//データ行の項目数のチェック
		for(int i = 0; i < dataList.size() ; i++){
			String[] rowData = dataList.get(i);
			if(rowData.length != datacolumnNum){
				errorList.add((i + 1) + "行目：ユーザー情報データの項目数が正しくありません。(正常:" + datacolumnNum +"項目、現在:" + rowData.length + "項目)");
			}
		}
		if (! errorList.isEmpty())
		{
			return;
		}
	}

	/**
	 * ファイル（所属部門割り当て）のフォーマットチェックを行います。
	 * @param dataList CSV１行ずつのテキスト
	 */
	protected void fileFormatCheckShozokuBumonWariate(List<String[]> dataList) {
		int datacolumnNum = DATACOLUMN_NUM_SHOZOKU_BUMON_WARIATE;
		//データ行の項目数のチェック
		for(int i = 0; i < dataList.size() ; i++){
			String[] rowData = dataList.get(i);
			if(rowData.length != datacolumnNum){
				errorList.add((i + 1) + "行目：所属部門割り当てデータの項目数が正しくありません。(正常:" + datacolumnNum +"項目、現在:" + rowData.length + "項目)");
			}
		}
		if (! errorList.isEmpty())
		{
			return;
		}
	}

	/**
	 * CSVファイル情報（ユーザー情報）をチェックします。
	 * @param userInfoList CSVファイル情報
	 */
	protected void fileContentCheckUserInfo(List<IkkatsuTourokuUserCSVUploadInfoUserInfo> userInfoList) {

		//必須項目チェック
		for (int i = 0; i < userInfoList.size(); i++) {
			IkkatsuTourokuUserCSVUploadInfoUserInfo userInfo = userInfoList.get(i);
			String ip = "#" + userInfo.getNumber() +":";
			checkHissu(userInfo.getUserId(), "ユーザー情報：" + ip + "ユーザーID");
			checkHissu(userInfo.getShainNo(), "ユーザー情報：" + ip + "社員番号");
			checkHissu(userInfo.getUserSei(), "ユーザー情報：" + ip + "ユーザー姓");
			checkHissu(userInfo.getUserMei(), "ユーザー情報：" + ip + "ユーザー名");
			checkHissu(userInfo.getYuukouKigenFrom(), "ユーザー情報：" + ip + "有効期限開始日");
			checkHissu(userInfo.getYuukouKigenTo(), "ユーザー情報：" + ip + "有効期限終了日");
			checkHissu(userInfo.getDairikihyouFlg(), "ユーザー情報：" + ip + "代理起票フラグ");
			checkHissu(userInfo.getHoujinCardRiyouFlag(), "ユーザー情報：" + ip + "法人カード利用");
			checkHissu(userInfo.getSecurityWfonlyFlg(), "ユーザー情報：" + ip + "セキュリティワークフロー限定フラグ");
			checkHissu(userInfo.getShouninRouteHenkouLevel(), "ユーザー情報：" + ip + "承認ルート変更権限レベル");
			checkHissu(userInfo.getMaruhiKengenFlg(), "ユーザー情報：" + ip + "マル秘設定権限");
			checkHissu(userInfo.getMaruhiKaijyoFlg(), "ユーザー情報：" + ip + "マル秘解除権限");
			if (RegAccess.checkEnableZaimuKyotenOption()) {
				checkHissu(userInfo.getZaimuKyotenNyuryokuOnlyFlg(), "ユーザー情報：" + ip + "拠点入力のみ使用");
			}
		}
		if (0 < errorList.size())
		{
			return;
		}

		//形式のチェック
		for (int i = 0; i < userInfoList.size(); i++) {
			IkkatsuTourokuUserCSVUploadInfoUserInfo userInfo = userInfoList.get(i);
			String ip = "#" + Integer.toString(i+1) + ":";
			checkDate(userInfo.getYuukouKigenFrom(), "ユーザー情報：" + ip + "有効期限開始日"		, false);
			checkDate(userInfo.getYuukouKigenTo(), "ユーザー情報：" + ip + "有効期限終了日"		, false);
			checkNumberRange(userInfo.getDairikihyouFlg(), 0, 1, "ユーザー情報：" + ip + "代理起票可能フラグ", false);
			checkNumberRange(userInfo.getHoujinCardRiyouFlag(), 0, 1, "ユーザー情報：" + ip + "法人カード利用", false);
			checkNumberRange(userInfo.getSecurityWfonlyFlg(), 0, 1, "ユーザー情報：" + ip + "セキュリティワークフロー限定フラグ", false);
			checkNumberRange(userInfo.getShouninRouteHenkouLevel(), 0, 4, "ユーザー情報：" + ip + "承認ルート変更権限レベル", false);
			checkNumberRange(userInfo.getMaruhiKengenFlg(), 0, 1, "ユーザー情報：" + ip + "マル秘設定権限", false);
			checkNumberRange(userInfo.getMaruhiKaijyoFlg(), 0, 1, "ユーザー情報：" + ip + "マル秘解除権限", false);
			if (RegAccess.checkEnableZaimuKyotenOption()) {
				checkNumberRange(userInfo.getZaimuKyotenNyuryokuOnlyFlg(), 0, 1,"ユーザー情報：" + ip + "拠点入力のみ使用", false);
			}
		}
		if (0 < errorList.size())
		{
			return;
		}

		//重複チェック
		ArrayList<String> userIdArray      = new ArrayList<String>();
		for (int i = 0; i < userInfoList.size(); i++) {
			IkkatsuTourokuUserCSVUploadInfoUserInfo userInfo = userInfoList.get(i);
			userIdArray.add(userInfo.getUserId());
		}
		for (int i = 0; i < userInfoList.size(); i++) {
			String ip = "#" + Integer.toString(i+1) + ":";
			IkkatsuTourokuUserCSVUploadInfoUserInfo userInfo = userInfoList.get(i);
			int idxFstUserId      = userIdArray.indexOf(userInfo.getUserId());
			int idxLstUserId      = userIdArray.lastIndexOf(userInfo.getUserId());

			if(idxFstUserId      != idxLstUserId)      errorList.add( "ユーザー情報：" + ip + "ユーザーIDが重複しています。");
		}
		if (0 < errorList.size())
		{
			return;
		}
	}

	/**
	 * CSVファイル情報（所属部門割り当て）をチェックします。
	 * @param shozokuBumonWariateList CSVファイル情報
	 */
	protected void fileContentCheckShozokuBumonWariate(List<IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate> shozokuBumonWariateList) {

		//必須項目チェック
		for (int i = 0; i < shozokuBumonWariateList.size(); i++) {
			IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate shozokuBumonWariate = shozokuBumonWariateList.get(i);
			String ip = "#" + shozokuBumonWariate.getNumber() +":";
			checkHissu(shozokuBumonWariate.getBumonCd(), "所属部門割り当て：" + ip + "部門コード");
			checkHissu(shozokuBumonWariate.getBumonRoleId(), "所属部門割り当て：" + ip + "部門ロールID");
			checkHissu(shozokuBumonWariate.getUserId(), "所属部門割り当て：" + ip + "ユーザーID");

			if(this.setting.userDaihyoufutanbumonMinyuuryokuCheck().equals("1"))
			{
				this.checkHissu(shozokuBumonWariate.getDaihyouFutanBumonCd(), "所属部門割り当て：" + ip + "代表負担部門");
			}

			checkHissu(shozokuBumonWariate.getYuukouKigenFrom(), "所属部門割り当て：" + ip + "有効期限開始日");
			checkHissu(shozokuBumonWariate.getYuukouKigenTo(), "所属部門割り当て：" + ip + "有効期限終了日");
			checkHissu(shozokuBumonWariate.getHyoujiJun(), "所属部門割り当て：" + ip + "表示順");
		}
		if (0 < errorList.size())
		{
			return;
		}

		//形式のチェック
		for (int i = 0; i < shozokuBumonWariateList.size(); i++) {
			IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate shozokuBumonWariate = shozokuBumonWariateList.get(i);
			String ip = "#" + Integer.toString(i+1) + ":";
			checkDate(shozokuBumonWariate.getYuukouKigenFrom(), "所属部門割り当て：" + ip + "有効期限開始日"		, false);
			checkDate(shozokuBumonWariate.getYuukouKigenTo(), "所属部門割り当て：" + ip + "有効期限終了日"		, false);
		}
		if (0 < errorList.size())
		{
			return;
		}
	}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		session.remove(Sessionkey.USER_CSV);
		return "success";
	}

	/**
	 * アップロードイベント
	 * @return ResultName
	 */
	public String upload(){
		try(EteamConnection connection = EteamConnection.connect()){
			// 入力チェック
			hissuCheck(2);
			if (! errorList.isEmpty())
			{
				return "error";
			}
			// ファイルサイズエラーチェック
			EteamCommon.uploadFileSizeCheck();
			//ファイル（ユーザ情報）をテキストに分解
			List<String[]> dataUserInfoList = fileDataReadUserInfo();

			//ファイル（ユーザ情報）をテキストに分解
			List<String[]> dataShozokuBumonWariateList = fileDataReadShozokuBumonWariate();

			//ファイル（ユーザー情報）全体のチェック。列数が規定数であるか
			fileFormatCheckUserInfo(dataUserInfoList);
			if (! errorList.isEmpty())
			{
				return "error";
			}

			//ファイル（所属部門割り当て）全体のチェック。列数が規定数であるか
			fileFormatCheckShozokuBumonWariate(dataShozokuBumonWariateList);
			if (! errorList.isEmpty())
			{
				return "error";
			}


			//CSVファイル情報（ユーザー情報）を作成します。
			List<IkkatsuTourokuUserCSVUploadInfoUserInfo> userInfoList = makeCsvFileInfoUserInfo(dataUserInfoList);

			//CSVファイル情報（所属部門割り当て）を作成します。
			List<IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate> shozokuBumonWariateList = makeCsvFileInfoShozokuBumonWariate(dataShozokuBumonWariateList);

			//CSVファイル情報（ユーザー情報）をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			fileContentCheckUserInfo(userInfoList);

			//CSVファイル情報（所属部門割り当て）をチェックします。
			//※ここでエラーが出ても画面遷移する。（登録ボタンは出さない）
			fileContentCheckShozokuBumonWariate(shozokuBumonWariateList);

			//処理結果をセションに保存します。（ユーザー情報）
			IkkatsuTourokuUserCSVUploadSessionInfo sessionInfo = new IkkatsuTourokuUserCSVUploadSessionInfo(uploadFileUserInfoFileName, userInfoList, uploadFileShozokuBumonWariateFileName, shozokuBumonWariateList ,errorList);

			session.put(Sessionkey.USER_CSV, sessionInfo);

			// 戻り値を返す
			return "success";
		}
	}

	/**
	 * 一括削除イベント
	 * @return ResultName
	 */
	public String sakujo(){
		try(EteamConnection connection = EteamConnection.connect()){
			UserJouhouAction myUserDeleteAction;
			setConnection(connection);

			//ユーザーの一括削除
			List<GMap> userList = bumonUserLogic.loadUserIchiran();
			for(GMap map : userList) {
				myUserDeleteAction = new UserJouhouAction();
				myUserDeleteAction.setGaibuConnection(connection);
				myUserDeleteAction.setGaibuYobidashiFlag(true);
				myUserDeleteAction.setUserId(map.get("user_id").toString());
				myUserDeleteAction.setLoginUserInfo((User)session.get(Sessionkey.USER));
				myUserDeleteAction.sakujo();
				for(String errorStr : myUserDeleteAction.getErrorList()) {
					errorList.add( "ユーザー情報 #" + map.get("user_id").toString() + ":" + errorStr);
				}
			}
			if(errorList.isEmpty()) {
				connection.commit();
			}
			else {
				connection.rollback();
			}

			// 戻り値を返す
			return "success";
		}
	}

	/**
	 * ダウンロードイベント（ユーザー情報）
	 * @return ResultName
	 */
	public String downloadUserInfo(){
		try(EteamConnection connection = EteamConnection.connect()){
			setConnection(connection);

			//--------------------
			//データ取得
			//--------------------
			List<GMap> userList;
			userList = bumonUserLogic.loadUserIchiran();

			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();

			// カラム名
			List<Object> colRecord = new ArrayList<Object>();
			colRecord.add("ユーザーID");
			colRecord.add("社員番号");
			colRecord.add("ユーザー姓");
			colRecord.add("ユーザー名");
			colRecord.add("メールアドレス");
			colRecord.add("有効期限開始日");
			colRecord.add("有効期限終了日");
			colRecord.add("代理起票可能フラグ");
			colRecord.add("法人カード利用");
			colRecord.add("法人カード識別用番号");
			colRecord.add("セキュリティパターン");
			colRecord.add("セキュリティワークフロー限定フラグ");
			colRecord.add("承認ルート変更権限レベル");
			colRecord.add("マル秘登録権限");
			colRecord.add("マル秘解除権限");
			if (RegAccess.checkEnableZaimuKyotenOption()) {
				colRecord.add("Web拠点のみ使用");
			}
			csvRecords.add(colRecord);

			// データ
			for (GMap map : userList) {
				List<Object> dataRecord = new ArrayList<Object>();
				dataRecord.add(map.get("user_id"));
				dataRecord.add(map.get("shain_no"));
				dataRecord.add(map.get("user_sei"));
				dataRecord.add(map.get("user_mei"));
				dataRecord.add(map.get("mail_address"));
				dataRecord.add(formatDate(map.get("yuukou_kigen_from")));
				dataRecord.add(formatDate(map.get("yuukou_kigen_to")));
				dataRecord.add(map.get("dairikihyou_flg"));
				dataRecord.add(map.get("houjin_card_riyou_flag"));
				dataRecord.add(map.get("houjin_card_shikibetsuyou_num"));
				dataRecord.add(map.get("security_pattern"));
				dataRecord.add(map.get("security_wfonly_flg"));
				dataRecord.add(map.get("shounin_route_henkou_level"));
				dataRecord.add(map.get("maruhi_kengen_flg"));
				dataRecord.add(map.get("maruhi_kaijyo_flg"));
				if (RegAccess.checkEnableZaimuKyotenOption()) {
					dataRecord.add(map.get("zaimu_kyoten_nyuryoku_only_flg"));
				}
				csvRecords.add(dataRecord);
			}

			// CSVファイルデータを作る
			try {
				//CSVデータ作成
				ByteArrayOutputStream objBos = new ByteArrayOutputStream();
				EteamFileLogic.outputCsv(objBos, csvRecords);

				//コンテンツタイプ設定
				contentType = ContentType.EXCEL;
				int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
				contentDisposition = EteamCommon.contentDisposition(browserCode, true, CSV_FILENAME_USER_INFO);
				inputStream = new ByteArrayInputStream(objBos.toByteArray());
				contentLength = objBos.size();
			} catch (IOException e) {
			   throw new RuntimeException(e);
			}
		}
		return "success";
	}

	/**
	 * ダウンロードイベント（所属部門割り当て）
	 * @return ResultName
	 */
	public String downloadShozokuBumonWariate(){
		try(EteamConnection connection = EteamConnection.connect()){
			setConnection(connection);

			//--------------------
			//データ取得
			//--------------------
			List<GMap> shozokuBumonWariateList;
			shozokuBumonWariateList = bumonUserLogic.loadShozokuBumonWariateIchiran();

			// CSVを表現した２次元配列
			List<List<Object>> csvRecords = new ArrayList<List<Object>>();

			// カラム名
			List<Object> colRecord = new ArrayList<Object>();
			colRecord.add("部門コード");
			colRecord.add("部門ロールID");
			colRecord.add("ユーザーID");
			colRecord.add("代表負担部門コード");
			colRecord.add("有効期限開始日");
			colRecord.add("有効期限終了日");
			colRecord.add("表示順");
			csvRecords.add(colRecord);

			// データ
			for (GMap map : shozokuBumonWariateList) {
				List<Object> dataRecord = new ArrayList<Object>();
				dataRecord.add(map.get("bumon_cd"));
				dataRecord.add(map.get("bumon_role_id"));
				dataRecord.add(map.get("user_id"));
				dataRecord.add(map.get("daihyou_futan_bumon_cd"));
				dataRecord.add(formatDate(map.get("yuukou_kigen_from")));
				dataRecord.add(formatDate(map.get("yuukou_kigen_to")));
				dataRecord.add(map.get("hyouji_jun"));
				csvRecords.add(dataRecord);
			}

			// CSVファイルデータを作る
			try {
				//CSVデータ作成
				ByteArrayOutputStream objBos = new ByteArrayOutputStream();
				EteamFileLogic.outputCsv(objBos, csvRecords);

				//コンテンツタイプ設定
				contentType = ContentType.EXCEL;
				int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
				contentDisposition = EteamCommon.contentDisposition(browserCode, true, CSV_FILENAME_SHOZOKU_BUMON_WARIATE);
				inputStream = new ByteArrayInputStream(objBos.toByteArray());
				contentLength = objBos.size();
			} catch (IOException e) {
			   throw new RuntimeException(e);
			}
		}
		return "success";
	}

	/**
	 * コネクション設定を行います。
	 * @param connection DBコネクション
	 */
protected void setConnection(EteamConnection connection) {
		myLogic = EteamContainer.getComponent(UserJouhouKanriLogic.class, connection);
		bumonUserLogic = EteamContainer.getComponent(BumonUserKanriCategoryLogic.class, connection);
		wfLogic = EteamContainer.getComponent(WorkflowCategoryLogic.class, connection);
}

	/**
	 * ファイルデータ（ユーザー情報）を読み込みます。
	 * @return ユーザー情報CSVの１行ずつのテキスト
	 */
	protected List<String[]> fileDataReadUserInfo() {
		List<String[]> dataList = new ArrayList<>();
		BufferedReader inFile = null;
		try {
			// ファイルデータを読み込みます。
			inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(uploadFileUserInfo)), Encoding.MS932));
			String line;
			while ((line = inFile.readLine()) != null) {
				String[] columnArray = EteamCommon.splitLineWithComma(line);
				dataList.add(columnArray);
			}
		} catch (IOException e) {
			throw new RuntimeException("ユーザー情報CSVファイル読込でエラー", e);
		} finally {
			if (null != inFile) {
				try {
					inFile.close();
				} catch (IOException e) {
					throw new RuntimeException("ユーザー情報CSVファイルクローズでエラー", e);
				}
			}
		}
		return dataList;
	}

	/**
	 * ファイルデータ（所属部門割り当て）を読み込みます。
	 * @return 所属部門割り当てCSVの１行ずつのテキスト
	 */
	protected List<String[]> fileDataReadShozokuBumonWariate() {
		List<String[]> dataList = new ArrayList<>();
		BufferedReader inFile = null;
		try {
			// ファイルデータを読み込みます。
			inFile = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(toByte(uploadFileShozokuBumonWariate)), Encoding.MS932));
			String line;
			while ((line = inFile.readLine()) != null) {
				String[] columnArray = EteamCommon.splitLineWithComma(line);
				dataList.add(columnArray);
			}
		} catch (IOException e) {
			throw new RuntimeException("所属部門割り当てCSVファイル読込でエラー", e);
		} finally {
			if (null != inFile) {
				try {
					inFile.close();
				} catch (IOException e) {
					throw new RuntimeException("所属部門割り当てCSVファイルクローズでエラー", e);
				}
			}
		}
		return dataList;
	}

	/**
	 * CSVファイル情報（ユーザー情報）を作成します。
	 * @param dataList CSV１行ずつのテキスト
	 * @return ユーザー情報リスト
	 */
	protected List<IkkatsuTourokuUserCSVUploadInfoUserInfo> makeCsvFileInfoUserInfo(List<String[]> dataList) {
		List<IkkatsuTourokuUserCSVUploadInfoUserInfo> userInfoList = new ArrayList<>();
		IkkatsuTourokuUserCSVUploadInfoUserInfo userInfo = null;
		for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
			String[] rowData = dataList.get(i);
			//CSVファイル情報（ユーザー情報）の設定
			userInfo = new IkkatsuTourokuUserCSVUploadInfoUserInfo();
			userInfoList.add(userInfo);
			userInfo.setNumber(Integer.toString(i));
			userInfo.setUserId(rowData[0]);
			userInfo.setShainNo(rowData[1]);
			userInfo.setUserSei(rowData[2]);
			userInfo.setUserMei(rowData[3]);
			userInfo.setMailAddress(rowData[4]);
			userInfo.setYuukouKigenFrom(rowData[5]);
			userInfo.setYuukouKigenTo(rowData[6]);
			userInfo.setDairikihyouFlg(rowData[7]);
			userInfo.setHoujinCardRiyouFlag(rowData[8]);
			userInfo.setHoujinCardShikibetsuyouNum(rowData[9]);
			userInfo.setSecurityPattern(rowData[10]);
			userInfo.setSecurityWfonlyFlg(rowData[11]);
			userInfo.setShouninRouteHenkouLevel(rowData[12]);
			userInfo.setMaruhiKengenFlg(rowData[13]);
			userInfo.setMaruhiKaijyoFlg(rowData[14]);
			if (RegAccess.checkEnableZaimuKyotenOption()) {
				userInfo.setZaimuKyotenNyuryokuOnlyFlg(rowData[15]);
			}
		}
		return userInfoList;
	}

	/**
	 * CSVファイル情報（所属部門割り当て）を作成します。
	 * @param dataList CSV１行ずつのテキスト
	 * @return ユーザー情報リスト
	 */
	protected List<IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate> makeCsvFileInfoShozokuBumonWariate(List<String[]> dataList) {
		List<IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate> shozokuBumonWariateList = new ArrayList<>();
		IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate shozokuBumonWariate = null;
		for(int i = 1 ; i < dataList.size() ; i++){ //ヘッダ読み飛ばし
			String[] rowData = dataList.get(i);
			//CSVファイル情報（所属部門割り当て）の設定
			shozokuBumonWariate = new IkkatsuTourokuUserCSVUploadInfoShozokuBumonWariate();
			shozokuBumonWariateList.add(shozokuBumonWariate);
			shozokuBumonWariate.setNumber(Integer.toString(i));
			shozokuBumonWariate.setBumonCd(rowData[0]);
			shozokuBumonWariate.setBumonRoleId(rowData[1]);
			shozokuBumonWariate.setUserId(rowData[2]);
			shozokuBumonWariate.setDaihyouFutanBumonCd(rowData[3]);
			shozokuBumonWariate.setYuukouKigenFrom(rowData[4]);
			shozokuBumonWariate.setYuukouKigenTo(rowData[5]);
			shozokuBumonWariate.setHyoujiJun(rowData[6]);
		}
		return shozokuBumonWariateList;
	}

}
