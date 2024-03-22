package eteam.gyoumu.masterkanri.kogamen;

import java.util.Collection;
import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamDBConnectException;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.base.exception.EteamIllegalRequestException;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.PostgreSQLSystemCatalogsLogic;
import eteam.gyoumu.masterkanri.MasterColumnInfo;
import eteam.gyoumu.masterkanri.MasterColumnsInfo;
import eteam.gyoumu.masterkanri.MasterDataServiceLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * マスターデータ汎用編集ダイアログ
 */
@Getter @Setter @ToString
public class MasterDataEditDialogAction extends EteamAbstractAction {
	/** ロガー */
	protected static EteamLogger log = EteamLogger.getLogger(MasterDataEditDialogAction.class);

	/** マスターID */
	protected String masterId;
	/** PK文字列 */
	protected String pkstr;
	/** カラム名称 */
	protected String[] name;
	/** 値 */
	protected String[] value;

	/** 異常時の例外ページ */
	protected String exceptionPage;

	/** マスター名 */
	protected String masterName;

	/** マスターカラムリスト */
	protected MasterColumnsInfo masterColumnsInfo;
	/** ロジック */
	protected MasterKanriCategoryLogic masterLogic;
	/** posgresロジック */
	protected PostgreSQLSystemCatalogsLogic dbLogic;
	/** マスターデータサービスロジック */
	protected MasterDataServiceLogic service;
	@Override
	protected void formatCheck() {
		checkString(masterId,   1, 50,  "マスターID",     true); // KEY
	}

	@Override
	protected void hissuCheck(int eventNum) {
		String[][] list = {
				//項目								EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
				{masterId ,"マスターID"		,"2", }, // KEY
			};
		hissuCheckCommon(list, eventNum);
	}

	/**
	 * 初期処理・入力チェックを行います。
	 * @param connection コネクション
	 */
	protected void initialize(EteamConnection connection) {
		dbLogic = EteamContainer.getComponent(PostgreSQLSystemCatalogsLogic.class, connection);
		masterLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
		service  = EteamContainer.getComponent(MasterDataServiceLogic.class, connection);
		formatCheck();
		hissuCheck(1);
		if (!errorList.isEmpty()) {
			return;
		}

		// マスター情報の取得
		GMap master = masterLogic.selectMasterKanriIchiran(masterId);
		if (null == master) {
			// マスターが存在しない
			throw new EteamDataNotFoundException();
		}
		masterName = (String)master.get("master_name");

		// 変更可否
		if (!"1".equals(master.get("editable_flg"))) {
			// 変更不可エラー
			throw new EteamAccessDeniedException();
		}

		// 列情報取得
		masterColumnsInfo = dbLogic.getMasterColumnsInfo(masterId);

	}

	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);
			if (!errorList.isEmpty()) {
				return "success";
			}
		}
		catch (EteamDataNotFoundException e) {
			exceptionPage = "dataNotFoundError";
			log.warn("マスタ編集ダイアログで例外", e);
		}
		catch (EteamIllegalRequestException e) {
			exceptionPage = "illegalRequestError";
			log.warn("マスタ編集ダイアログで例外", e);
		}
		catch (EteamAccessDeniedException e) {
			exceptionPage = "accessDeniedError";
			log.warn("マスタ編集ダイアログで例外", e);
		}
		catch (EteamDBConnectException e) {
			exceptionPage = "dbConnectError";
			log.warn("マスタ編集ダイアログで例外", e);
		}
		catch (Exception e) {
			exceptionPage = "systemError";
			log.warn("マスタ編集ダイアログで例外", e);
		}

		return "success";
	}

	/**
	 * ダイアログの内容を確認します。
	 * @return 結果
	 */
	public String confirm() {
		try(EteamConnection connection = EteamConnection.connect()) {
			initialize(connection);
			if (!errorList.isEmpty()) {
				return "success";
			}

			// エスケープ文字列を解除しちゃう
			for (int ii = 0; ii < value.length; ii++) {
				value[ii] = value[ii].replaceAll(";;;", ",");
			}

			// マスター固有入力チェック
			GMap input = MasterColumnsInfo.convertNameValueMap(name, value);
			errorList.addAll(masterColumnsInfo.checkInput(input));
			if (!errorList.isEmpty())
			{
				return "success";
			}
			
			//入力内容の相関チェック
			//照会画面での更新時にもチェックするけど、そこだけでチェックだとエラー時にまた子画面表示→修正って手順が増えるからここにも同一のチェック入れておく。
			List<String> soukanCheckErrorList = soukanCheck(input);
			if(null != soukanCheckErrorList) errorList.addAll(soukanCheckErrorList);
			if (!errorList.isEmpty())
			{
				return "success";
			}
		
			// インデックスの生成
			pkstr = masterColumnsInfo.createPkString(value);
		}
		catch (EteamDataNotFoundException e) {
			exceptionPage = "dataNotFoundError";
			log.warn("マスタ編集ダイアログで例外", e);
		}
		catch (EteamIllegalRequestException e) {
			exceptionPage = "illegalRequestError";
			log.warn("マスタ編集ダイアログで例外", e);
		}
		catch (EteamAccessDeniedException e) {
			exceptionPage = "accessDeniedError";
			log.warn("マスタ編集ダイアログで例外", e);
		}
		catch (EteamDBConnectException e) {
			exceptionPage = "dbConnectError";
			log.warn("マスタ編集ダイアログで例外", e);
		}
		catch (Exception e) {
			exceptionPage = "systemError";
			log.warn("マスタ編集ダイアログで例外", e);
		}

		return "success";
	}

	/**
	 * カラムリストを取得します。
	 * @return カラムリスト
	 * @see eteam.gyoumu.masterkanri.MasterColumnsInfo#getColumnList()
	 */
	public Collection<MasterColumnInfo> getColumnList() {
		return masterColumnsInfo.getColumnList();
	}
	
	/**
	 * 相関チェック ※単一行で修正可能なチェックのみ
	 * @param input 入力内容
	 * @return エラーリスト
	 */
	protected List<String> soukanCheck(GMap input){
		if (masterId.equals("kian_bangou_bo"))return service.checkBangouBoSingle(input);
		if (masterId.equals("koutsuu_shudan_master"))return service.checkKoutsuuShudanMaster(input, "");
		if (masterId.equals("nittou_nado_master"))return service.checkNittouNadoMaster(input, "");
		if (masterId.equals("kaigai_koutsuu_shudan_master"))return service.checkKaigaiKoutsuuShudanMaster(input, "");
		if (masterId.equals("kaigai_nittou_nado_master"))return service.checkKaigaiNittouNadoMaster(input, "");
		if (masterId.equals("kinyuukikan"))return service.checkKinyuukikan(input, "");
		if (masterId.equals("moto_kouza"))return service.checkMotoKouza(input, "");
		if (masterId.equals("moto_kouza_shiharaiirai"))return service.checkMotoKouzaShiharaiirai(input, "");
		if (masterId.equals("shain_kouza"))return service.checkShainKouza(input, "");
		if (masterId.equals("shouhizeiritsu"))return service.checkShouhizeiritsu(input, "");
		return null;
	}
}
