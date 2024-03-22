package eteam.gyoumu.masterkanri;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamNaibuCodeSetting.KINOU_SEIGYO_KBN;
import eteam.common.select.MasterKanriCategoryLogic;
import eteam.common.select.PostgreSQLSystemCatalogsLogic;
import eteam.database.dao.KaniTodokeListKoDao;
import eteam.database.dao.KaniTodokeSelectItemDao;
import eteam.database.dto.KaniTodokeSelectItem;
import eteam.gyoumu.kanitodoke.KaniTodokeLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * マスターデータ変更画面Action
 * @author tashiro_yuuta
 */
@Getter @Setter @ToString
public class MasterDataHenkouAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞
    /** マスターID */
    String masterId;
    /** マスター名 */
    String masterName;
    
    /** ファイルオブジェクト */
    File uploadFile;

//＜確認イベントでは画面入力(アップロードファイル情報)、登録イベントではワーク用＞
    /** ファイル名 */
    String uploadFileFileName;
    /** ファイルコンテンツタイプ */
    String uploadFileContentType;
    
//＜画面入力以外＞
    /** ファイルサイズ */
    long uploadFileSize;
    /** ファイルデータ */
    byte[] uploadFileData;
    /** 論理名リスト */
    List<String> logicalNameList  = new ArrayList<String>();
    /** 物理名リスト */
    List<String> physicalNameList = new ArrayList<String>();
    /** データ型リスト */
    List<String> dataTypeList  = new ArrayList<String>();
    /** プライマリーキー区分リスト */
    List<String> primaryKeyKbnList = new ArrayList<String>();
    /** データリスト */
    List<List<String>> dataList   = new ArrayList<List<String>>();
    /** 現行カラムメタリスト */
    List<GMap> currentColumnList;
    /** 現行カラムデータリスト */
    List<List<String>> currentDataList = new ArrayList<List<String>>();

//＜部品＞
    /** マスターデータ管理ロジック */
    protected MasterDataKanriLogic myLogic;
    /** マスター管理カテゴリロジック */
    protected MasterKanriCategoryLogic masterKanriLogic;
    /** ポスグレシステム管理カテゴリロジック */
    protected PostgreSQLSystemCatalogsLogic postgreSystemLogic;
    /** マスターデータサービスロジック */
    protected MasterDataServiceLogic service;
//＜入力チェック＞
    @Override protected void formatCheck() {
        checkString(masterId,    1, 50,   "マスターID",    true);  // KEY
        checkString(masterName,  1, 50,   "マスター名",    false);
    }

    @Override
    protected void hissuCheck(int eventNum) {
        String[][] list = {
            //項目						EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
            {masterId ,"マスターID"	,"2", "2", "2", },
            {masterName ,"マスター名"	,"0", "1", "1", },
        };
        hissuCheckCommon(list, eventNum);
    }

    /**
     * ファイル入力チェックを行います。
     */
    protected void fileCheck() {
        if(uploadFile == null) {
            errorList.add("CSVファイルを選択してください。");
        } else {
            if(uploadFile.length() == 0) {
                errorList.add("ファイルサイズが０バイトです。");
            }
        }
    }

//＜イベント＞
    /**
     * 初期表示イベント
     * @return ResultName
     */
    public String init(){
        //1.入力チェック
        hissuCheck(1);
        formatCheck();
        if(!errorList.isEmpty()){ return "error"; }
        
        try(EteamConnection connection = EteamConnection.connect()){
            setConnection(connection);

            //2.データ存在チェック なし

            //3.アクセス可能チェック
            masterName = enableCheckAndFindMasterName();

            //4.処理

            //5.戻り値を返す
            return "success";
        }
    }
    
    /**
     * 確認イベント
     * @return ResultName
     */
    public String kakunin() {
        //1.入力チェック
        hissuCheck(2);
        formatCheck();
        fileCheck();
        if(!errorList.isEmpty()){ return "error"; }
        
        // ファイルサイズエラーチェック
        EteamCommon.uploadFileSizeCheck();
        
        try(EteamConnection connection = EteamConnection.connect()){
            setConnection(connection);

            //2.データ存在チェック
            //3.アクセス可能チェック
            enableCheckAndFindMasterName();
            
            //4.処理

            // アップロードファイルのバイナリデータ化
            uploadFileData = super.toByte(uploadFile);
            uploadFileSize = uploadFileData.length;

            // バイナリデータからファイル内容を読み込む
            fileDataRead();

            // ファイルの形式チェック
            if(headerSizeCheck()) return "error";
            if(headerCheck() ) return "error";
            if(dataCheck() ) return "error";
            
            //No.811(ガソリン代精算パッケージ化)国内交通手段マスタの小数点対応
            if("koutsuu_shudan_master".equals(masterId)) {
                for(int i = 0; i < dataList.size(); i++) {
                    List<String> columnArray = dataList.get(i);
                    for(int j = 0; j < physicalNameList.size(); j++) {
                        if("tanka".equals(physicalNameList.get(j))) {
                            //チェック
                            String tanka = columnArray.get(j).replaceAll(",", "");;
                            String msg = "[" + columnArray.get(0) + "][" + columnArray.get(1) + "][" + columnArray.get(2) + "]：";
                            if(tanka.length() != 0) {
                                if (!tanka.matches("^\\d{1,15}+(\\.\\d{0,3})?$")) {
                                    errorList.add(msg + "単価の小数部は3桁の半角数字で入力してください。");
                                } else if (! GenericValidator.isDouble(tanka)) {
                                    errorList.add(msg + "単価は半角数字で入力してください。");
                                }
                            }
                        }
                    }
                }
            }
            
            //No.767(海外日当等マスターCSV取込時の小数点制限)暫定対応
            if("kaigai_nittou_nado_master".equals(masterId)) {
                for(int i = 0; i < dataList.size(); i++) {
                    List<String> columnArray = dataList.get(i);
                    for(int j = 0; j < physicalNameList.size(); j++) {
                        if("tanka_gaika".equals(physicalNameList.get(j))) {
                            //チェック
                            String tankaGaika = columnArray.get(j).replaceAll(",", "");;
                            String msg = "[" + columnArray.get(0) + "][" + columnArray.get(1) + "][" + columnArray.get(2) + "]：";
                            if(tankaGaika.length() != 0) {
                                if (!tankaGaika.matches("^\\d{1,17}+(\\.\\d{0,2})?$")) {
                                    errorList.add(msg + "単価（外貨）の小数部は2桁の半角数字で入力してください。");
                                } else if (! GenericValidator.isDouble(tankaGaika)) {
                                    errorList.add(msg + "単価（外貨）は半角数字で入力してください。");
                                }
                            }
                        }
                    }
                }
            }
            if(!errorList.isEmpty()) {
                return "error";
            }
            //幣種別レートマスターも対応
            if("rate_master".equals(masterId) || "rate_master_kyoten".equals(masterId)) {
                for(int i = 0; i < dataList.size(); i++) {
                    List<String> columnArray = dataList.get(i);
                    for(int j = 0; j < physicalNameList.size(); j++) {
                        if(physicalNameList.get(j).toString().indexOf("rate") >= 0) {
                            //チェック
                            String rate = columnArray.get(j).replaceAll(",", "");;
                            String msg = "[" + columnArray.get(0) + "]：";
                            if(rate.length() != 0) {
                                if (!rate.matches("^\\d{1,6}+(\\.\\d{0,5})?$")) {
                                    errorList.add(msg + "適用レート(" + physicalNameList.get(j).toString() + ")の小数部は5桁の半角数字で入力してください。");
                                } else if (! GenericValidator.isDouble(rate)) {
                                    errorList.add(msg + "適用レート(" + physicalNameList.get(j).toString() + ")は半角数字で入力してください。");
                                }
                            }
                        }
                    }
                }
            }
            if(!errorList.isEmpty()) {
                return "error";
            }
            
            //TODO 下記、各カラムデータのチェックを画面からのマスタデータ変更ロジックでのチェックに寄せる
            
            //金額フォーマットへの変換
            for(int i = 0; i < dataList.size(); i++) {
                List<String> columnArray = dataList.get(i);
                for(int j = 0; j < physicalNameList.size(); j++) {
                    String dataType = dataTypeList.get(j).replaceAll("\\u0020|\\u00A0", "").toUpperCase();
                    String data = columnArray.get(j);
                    if(dataType.matches(".*\\(.*\\).*")) {
                        if(dataType.startsWith("DECIMAL")) {
                            if(data.indexOf(".") > 0){
                                if(physicalNameList.get(j).toString().indexOf("rate") >= 0) {
                                    columnArray.set(j, formatDecimalNoComma(toDecimal(data), "rate"));
                                }
                                if(physicalNameList.get(j).toString().indexOf("gaika") >= 0) {
                                    columnArray.set(j, formatMoneyDecimal(toDecimal(data)));
                                }
                            }else{
                                columnArray.set(j, formatMoney(toDecimal(data)));
                            }
                        }
                    }
                }
            }
            
            
            // 現行のカラム名とコメント（和名）を取得
            currentColumnList = postgreSystemLogic.selectColumnInfo(EteamCommon.getContextSchemaName(), masterId);
            
            // 現行のデータを取得する。
            for(GMap dataMap : myLogic.selectData(masterId)) {
                List<String> columnValueList = new ArrayList<String>();
                for(GMap map : currentColumnList) {
                    String column   = map.get("column_name").toString();
                    String dataType = map.get("column_type").toString().toUpperCase();
                    String decimalLength = (map.get("column_decimal_length") == null)? null: map.get("column_decimal_length").toString();
                    Boolean isDecimal = (decimalLength != null && toInteger(decimalLength) > 0)? true : false;
                    Object dataObj = dataMap.get(column);
                    if(dataType.equals("NUMERIC")) {
                        if(isDecimal){
                            columnValueList.add((dataObj == null)? "" :dataObj.toString());
                        }else{
                            columnValueList.add(formatMoney(dataObj));
                        }
                    } else if(dataType.equals("DATE")) {
                        String datestr = "";
                        if(dataObj != null) {
                            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                            java.util.Date date = new java.util.Date(System.currentTimeMillis());
                            try {
                                date = sdFormat.parse(dataObj.toString());
                                datestr = formatDate(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        columnValueList.add(datestr);
                    } else if(dataType.equals("TIMESTAMP")) {
                        columnValueList.add(formatTime(dataObj));
                    } else {
                        if(dataObj == null) {
                            dataObj = "";
                        }
                        columnValueList.add(dataObj.toString());
                    }
                }
                currentDataList.add(columnValueList);
            }
            
            // 現在形式とアップロード形式の差異をチェック
            if(currentDiffCheck()) return "error";
            
            // CSVデータをList<Map<String, String>>形式に変換
            List<GMap> rowMapList = new ArrayList<GMap>();
            for(int i = 0; i < dataList.size(); i++) {
                List<String> columnArray = dataList.get(i);
                GMap rowMap = new GMap();
                for(int k = 0; k < physicalNameList.size(); k++) {
                    String column = physicalNameList.get(k);
                    rowMap.put(column, columnArray.get(k));
                }
                rowMapList.add(rowMap);
            }
            
            // マスタ毎項目の個別チェック
            List<String> errList = service.kobetsuCheck(rowMapList, masterId);
            if(errList.size() > 0){
                errorList.addAll(errList);
                return "error";
            }
            
            // セッションにファイルデータを格納する。
            sessionSetFileData();
            
            //5.戻り値を返す
            return "success";
        }
    }
    
    /**
     * 登録イベント
     * @return ResultName
     */
    public String touroku() {
        //1.入力チェック
        hissuCheck(3);
        formatCheck();
        if(!errorList.isEmpty()){ return "error"; }

        try(EteamConnection connection = EteamConnection.connect()){
            setConnection(connection);

            //2.データ存在チェック
            //3.アクセス可能チェック
            enableCheckAndFindMasterName();
            
            //4.処理
            
            // セッションからユーザー情報の取得
            String userId = getUser().getTourokuOrKoushinUserId();
            
            // セッションからファイルデータを取得
            sessionGetFileData();

            // ファイルデータを読み込む
            fileDataRead();
            
            //届出ジェネレータ選択項目ならマスタ変更内容をここでチェック、使用中項目マスタ削除されているならエラー
            //登録側データのチェックは既に済んでいる前提
        	List<KaniSelectItemDiff> diffList = null;
            if("kani_todoke_select_item".equals(masterId)) {
            	diffList = judgeKaniSelectItemDiff(connection);
            	checkKaniSelectItemDiff(connection, diffList);
            	if(!errorList.isEmpty()){ return "error"; }
        	}
            
            // マスター版数へ登録
            myLogic.updateMasterKanriHansuu(masterId, userId); // 現在有効なマスターを無効にする。
            myLogic.insertMasterKanriHansuu(masterId, "0", uploadFileFileName, uploadFileSize, uploadFileContentType, uploadFileData, userId);
            
            // 対象テーブルにロックを行います。
            myLogic.tableLock(masterId);
            
            // 全レコード削除
            myLogic.delete(masterId);
            
            // データを登録します。
            insertData();

            if("kani_todoke_select_item".equals(masterId)) {
            	updateKaniTodoke(connection, diffList);
            }
            
            connection.commit();
            
            // セッションからファイルデータを削除する。
            session.remove(masterId);
            
            //5.戻り値を返す
            return "success";
        }
        
    }

	/**
     * コネクション設定を行います。
     * @param connection DBコネクション
     */
    protected void setConnection(EteamConnection connection) {
        myLogic = EteamContainer.getComponent(MasterDataKanriLogic.class, connection);
        masterKanriLogic = EteamContainer.getComponent(MasterKanriCategoryLogic.class, connection);
        postgreSystemLogic = EteamContainer.getComponent(PostgreSQLSystemCatalogsLogic.class, connection);
        service = EteamContainer.getComponent(MasterDataServiceLogic.class, connection);
    }
    
    /**
     * データ存在チェックとアクセス権限チェックを行います。
     * マスターIDが存在しない、または変更不可なマスターであれば例外を投げる。
     * @return マスター名
     */
    protected String enableCheckAndFindMasterName() {
        GMap masterIchiranMap = masterKanriLogic.selectMasterKanriIchiran(masterId);
        if(masterIchiranMap == null) {
            throw new EteamDataNotFoundException();
        }
        if(masterIchiranMap.get("henkou_kahi_flg").equals(KINOU_SEIGYO_KBN.MUKOU)) {
            throw new EteamAccessDeniedException();
        }
        return (String)masterIchiranMap.get("master_name");
    }
    
    /**
     * ファイルデータを読み込みます。
     */
    protected void fileDataRead() {
        try {
            // ファイルデータを読み込みます。
            InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(uploadFileData), Encoding.MS932);
            BufferedReader inFile = new BufferedReader(in);
            String line;
            int lineCount = 1;
            while ((line = inFile.readLine()) != null) {
                String[] columnArray = line.split(",", -1);
                // １行目は論理名
                if (lineCount == 1) {
                    logicalNameList.addAll(Arrays.asList(columnArray));
                // ２行目は物理名
                } else if (lineCount == 2) {
                    physicalNameList.addAll(Arrays.asList(columnArray));
                // ３行目はデータ型
                } else if (lineCount == 3) {
                    dataTypeList.addAll(Arrays.asList(columnArray));
                // ４行目はプライマリーキー区分
                } else if (lineCount == 4) {
                    primaryKeyKbnList.addAll(Arrays.asList(columnArray));
                // ５行目以降はデータ
                } else {
                    dataList.add(new ArrayList<String>(Arrays.asList(columnArray)));
                }
                lineCount++;
            }
            inFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * ヘッダーサイズチェックを行います。
     * @return 【エラーが有る場合：TRUE】【エラーが無い場合：FALSE】
     */
    protected boolean headerSizeCheck() {
        if(logicalNameList.size() != physicalNameList.size()) {
            errorList.add("１行目：カラム論理名と２行目：カラム物理名の列数が異なります。");
        }
        if(logicalNameList.size() != dataTypeList.size()) {
            errorList.add("１行目：カラム論理名と３行目：データ型の列数が異なります。");
        }
        if(logicalNameList.size() != primaryKeyKbnList.size()) {
            errorList.add("１行目：カラム論理名と４行目：プライマリーキー設定の列数が異なります。");
        }
        return errorList.isEmpty() ? false : true;
    }
    
    /**
     * ヘッダーチェックを行います。
     * @return 【エラーが有る場合：TRUE】【エラーが無い場合：FALSE】
     */
    protected boolean headerCheck() {
        @SuppressWarnings("unused")
        boolean isFindPrimaryKey = false;
        
        List<String> tmpErrorList1 = new ArrayList<String>();
        List<String> tmpErrorList2 = new ArrayList<String>();
        List<String> tmpErrorList3 = new ArrayList<String>();
        
        for(int i = 0; physicalNameList.size() > i; i++) {
            int colNo = i + 1;
            
            // １行目：カラム論理名のチェック
            if(replaceSpaceDelete(logicalNameList.get(i)).length() == 0) {
                tmpErrorList1.add("1行目：" + colNo + "列目：空、もしくはスペースのみのカラムが存在します。");
            }
            
            // ２行目：カラム物理名のチェック
            String column = physicalNameList.get(i);
            if(replaceSpaceDelete(column).length() == 0) {
                tmpErrorList2.add("2行目：" + colNo + "列目：空、もしくはスペースのみのカラムが存在します。");
            }
            if(column.matches("^[^a-zA-Z].*")) {
                tmpErrorList2.add("2行目：" + colNo + "列目：カラムの先頭文字は「a-z」「A-Z」以外は使用できません。");
            }
            if(column.matches(".*[^a-zA-Z0-9_].*")) {
                tmpErrorList2.add("2行目：" + colNo + "列目：カラムは「a-z」「A-Z」「0-9」「_(アンダーバー)」以外は使用できません。");
            }
            if(column.length() > 63) {
                tmpErrorList2.add("2行目：" + colNo + "列目：カラムに使用できる文字は63バイトまでです。");
            }
            
            // ３行目：データ型のチェック
            String dataType = dataTypeList.get(i).replaceAll("\\u0020|\\u00A0", "").toUpperCase();
            if(replaceSpaceDelete(dataType).length() == 0) {
                tmpErrorList3.add("3行目：" + colNo + "列目：空、もしくはスペースのみのデータ型が存在します。");
            } else if(dataType.matches(".*（.*|.*）.*")) {
                tmpErrorList3.add("3行目：" + colNo + "列目：全角括弧が含まれています。");
            } else if(! dataTypeCheck(dataType)) {
                tmpErrorList3.add("3行目：" + colNo + "列目：使用不可のデータ型が設定されています。");
            } else if(dataType.matches(".*\\(.*\\).*")) {
                // 桁数指定が有る場合のチェック
                Pattern pattern = Pattern.compile("\\((.+?)\\)");
                Matcher matcher = pattern.matcher(dataType);
                while (matcher.find()) {
                    if(dataType.startsWith("VARCHAR")) {
                        int length = Integer.parseInt(matcher.group(1));
                        if (length > 10485760) {// PostgreSQL仕様
                            tmpErrorList3.add("3行目：" + colNo + "列目：VARCHAR型へ設定できる文字数は10485760までです。");
                        }
                    } else if(dataType.startsWith("DECIMAL")) {
                        String[] digitArray = matcher.group(1).split(";;;");
                        int integerDigit = Integer.parseInt(digitArray[0]);
                        if(integerDigit < 1 || integerDigit > 1000) {// PostgreSQL仕様
                            tmpErrorList3.add("3行目：" + colNo + "列目：DECIMAL型の有効桁数は1～1000までです。");
                        }
                        if (digitArray.length == 2) {
                            int decimalDigit = Integer.parseInt(digitArray[1]);
                            if(decimalDigit < 0 || decimalDigit > 1000) {// PostgreSQL仕様
                                tmpErrorList3.add("3行目：" + colNo + "列目：DECIMAL型の小数点以下桁数は1～1000までです。");
                            }
                        }
                    }
                }
            }
            
            // ４行目：プライマリーキーのチェック
            if(primaryKeyKbnList.get(i).equals("1")) {
                isFindPrimaryKey = true;
            }
        }
        
        errorList.addAll(tmpErrorList1);
        errorList.addAll(tmpErrorList2);
        errorList.addAll(tmpErrorList3);
        
        //PKがないのは問題にせず
// if (! isFindPrimaryKey) {
// errorList.add("4行目：プライマリーキーが設定されていません。");
// }
        
        return errorList.isEmpty() ? false : true;
    }
    
    /**
     * データ型チェック
     * @param dataType データ型
     * @return 【使用可能：TRUE】【使用不可：FALSE】
     */
    protected boolean dataTypeCheck(String dataType) {
        if(dataType.matches("VARCHAR")
        || dataType.matches("VARCHAR\\([0-9]+\\)$")
        || dataType.matches("INT")
        || dataType.matches("SMALLINT")
        || dataType.matches("DECIMAL")
        || dataType.matches("DECIMAL\\([0-9]+\\)$")
        || dataType.matches("DECIMAL\\([0-9]+;;;[0-9]+\\)$")
        || dataType.matches("DATE")
        || dataType.matches("TIMESTAMP")
        ) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * データチェック
     * @return 【エラーが有る場合：TRUE】【エラーが無い場合：FALSE】
     */
    protected boolean dataCheck() {

        //行単位ループ：カラム数チェック
        for(int i = 0; i < dataList.size(); i++) {
            List<String> columnArray = dataList.get(i);
            int rowNo = i + 5;
            if(logicalNameList.size() == physicalNameList.size() && columnArray.size() != logicalNameList.size()) {
                errorList.add((rowNo) + "行目のカラム数が不正です。");
            }
        }
        if (! errorList.isEmpty()) return true;

        //行単位ループ：NUT NULLチェック
        for(int i = 0; i < dataList.size(); i++) {
            List<String> columnArray = dataList.get(i);
            int rowNo = i + 5;

            //列単位のループ
            for(int j = 0; j < physicalNameList.size(); j++) {
                int colNo = j + 1;
                String dataType = dataTypeList.get(j).replaceAll("\\u0020|\\u00A0", "").toUpperCase();
                String primaryKeyKbn = primaryKeyKbnList.get(j);
                String data = columnArray.get(j);
                String physicalName = physicalNameList.get(j);
                
                // NULLチェック PKカラムなら空はNG、必須カラムなら空はNG(ただし税率区分以外のVARCHARは許容)
                if (
                    ("1".equals(primaryKeyKbn)) ||
                    (! dataType.startsWith("VARCHAR") && ("1".equals(primaryKeyKbn) || "2".equals(primaryKeyKbn))) ||
                    (dataType.startsWith("VARCHAR") && physicalName.equals("keigen_zeiritsu_kbn"))
                ){
                    if(isEmpty(data)) {
                        errorList.add(rowNo + "行目：" + colNo + "列目：必須項目が空です。");
                        continue;
                    }
                }

                // フォーマットチェック
                if(dataType.matches(".*\\(.*\\).*")) {
                    if(dataType.startsWith("VARCHAR")) {
                        Pattern pattern = Pattern.compile("\\((.+?)\\)");
                        Matcher matcher = pattern.matcher(dataType);
                        while(matcher.find()) {
                            int maxLength = Integer.parseInt(matcher.group(1));
                            checkString(data, 0, maxLength, rowNo + "行目：" + colNo + "列目：VARCHAR型", false);
                            
                            // 半角カナチェック
                            if (physicalName.endsWith(EteamConst.HankanaCheck.MATCH_COLUMN_STR1) || physicalName.equals(EteamConst.HankanaCheck.MATCH_COLUMN_STR2)) {
                                String regex = EteamConst.HankanaCheck.REGEX;
                                if (!"".equals(regex) && StringUtils.isNotEmpty(data) && !Pattern.compile("^" + regex + "$").matcher(data).find()) {
                                    errorList.add(rowNo + "行目：" + colNo + "列目：半角カナ(英数字記号含む)で入力してください。");
                                }
                            // 半角英数字チェック
                            }else if(physicalName.equals(EteamConst.HankakuCheck.MATCH_COLUMN_STR1)){
                                String regex = EteamConst.HankakuCheck.REGEX;
                                if (!"".equals(regex) && StringUtils.isNotEmpty(data) && !Pattern.compile("^" + regex + "$").matcher(data).find()) {
                                    errorList.add(rowNo + "行目：" + colNo + "列目：半角英数字で入力してください。");
                                }
                            }
                            
                            // 税率区分チェック（通常税率=0,軽減税率=1）
                            if(physicalName.equals("keigen_zeiritsu_kbn")) {
                                String regex = "^[0|1]$";
                                if (!"".equals(regex) && StringUtils.isNotEmpty(data) && !Pattern.compile(regex).matcher(data).find()) {
                                    errorList.add(rowNo + "行目：" + colNo + "列目：半角数字の0または1で入力してください。");
                                }
                            } 
                         // 税区分チェック（通常税率=0,軽減税率=1）
                            if(physicalName.equals("zei_kubun")) {
                                String regex = "^[0-4]$";
                                if (!"".equals(regex) && StringUtils.isNotEmpty(data) && !Pattern.compile(regex).matcher(data).find()) {
                                    errorList.add(rowNo + "行目：" + colNo + "列目：半角数字の「0-4」で入力してください。");
                                }
                            } 
                        }
                    } else if(dataType.startsWith("DECIMAL")) {
                        Pattern pattern = Pattern.compile("\\((.+?)\\)");
                        Matcher matcher = pattern.matcher(dataType);
                        while(matcher.find()) {
                            String[] digitArray = matcher.group(1).split(";;;");
                            int integerDigit = Integer.parseInt(digitArray[0]);
                            checkNumber(data.replaceAll("\\.", ""), 0, integerDigit, rowNo + "行目：" + colNo + "列目：DECIMAL型の数値", false);
                            
                            if (digitArray.length == 2) {
                                int decimalDigit = Integer.parseInt(digitArray[1]);
                                checkNumber(data.split("\\.")[0], 0, integerDigit - decimalDigit, rowNo + "行目：" + colNo + "列目：DECIMAL型の整数値", false);
                            }
                        }
                    }
                } else if(dataType.startsWith("INT")) {
                    checkNumberRange(data, 0, 2147483647, rowNo + "行目：" + colNo + "列目：INT型", false);
                    if ((physicalName.equals(EteamConst.kianBangou.MASTER_CHK_NUM_RENGE_COLUMN1) || physicalName.equals(EteamConst.kianBangou.MASTER_CHK_NUM_RENGE_COLUMN2))) {
                        checkNumberRange(data, 1, EteamConst.kianBangou.MAX_VALUE, rowNo + "行目：" + colNo + "列目", false);
                    }
                } else if(dataType.startsWith("SMALLINT")) {
                    checkNumberRange(data, 0, 32767, rowNo + "行目：" + colNo + "列目：SMALLINT型", false);
                    
                    // 数値チェック
                    if (physicalName.equals("kijun_weekday")) {
                        String regex = "^[1|2|3|4|5|6|7]$";
                        if (!"".equals(regex) && StringUtils.isNotEmpty(data) && !Pattern.compile("^" + regex + "$").matcher(data).find()) {
                            errorList.add(rowNo + "行目：" + colNo + "列目：1~7で入力してください。");
                        }
                    }else if (physicalName.equals("furikomi_weekday")) {
                        String regex = "^[1-9][1|2|3|4|5]$";
                        if (!"".equals(regex) && StringUtils.isNotEmpty(data) && !Pattern.compile("^" + regex + "$").matcher(data).find()) {
                            errorList.add(rowNo + "行目：" + colNo + "列目：一の位は1~5、十の位は1~9で入力してください。");
                        }
                    }
                } else if(dataType.startsWith("DATE") || dataType.startsWith("TIMESTAMP")) {
                    if (! "CURRENT_TIMESTAMP".equals(data.toUpperCase())) {
                        checkDate(data, rowNo + "行目：" + colNo + "列目：DATE型・TIMESTAMP型", false);
                    }
                }
            }
        }
        if (! errorList.isEmpty()) return true;

        //行単位ループ：重複チェック。PK項目が全部同じならエラー。
        HashSet<Map<String, String>> hashSet = new HashSet<Map<String, String>>();
        for(int i = 0; i < dataList.size(); i++) {
            List<String> columnArray = dataList.get(i);
            int rowNo = i + 5;

            //列単位のループ：PK1名=PK1値,PK2名=PK2値,,,のように現在行のPKリストを重複チェック用に作る
            Map<String, String> tempMap = new HashMap<String, String>();
            for(int j = 0; j < physicalNameList.size(); j++) {
                String primaryKeyKbn = primaryKeyKbnList.get(j);
                String data = columnArray.get(j);
                if("1".equals(primaryKeyKbn)) {
                    tempMap.put(physicalNameList.get(j), data);
                }
            }

            // 重複チェック
            if(hashSet.contains(tempMap)) {
                errorList.add(rowNo + "行目：キー項目が他の行と重複しています。");
            }

            //PKリストに追加
            hashSet.add(tempMap);
        }
        return errorList.isEmpty() ? false : true;
    }

    /**
     * 現在のテーブル形式とアップロードファイルの形式を比較する。
     * @return 【エラーが有る場合：TRUE】【エラーが無い場合：FALSE】
     */
    protected boolean currentDiffCheck() {
        String msg = "マスターデータの形式が前回と異なります。ファイルを確認してください。";

        // 現在のカラム名の数とファイルのカラム名の数を比較する
        if((currentColumnList.size()) != physicalNameList.size()) {
            errorList.add(msg);
            return true;
        }

        // 現行カラムとファイルカラムのカラム名、データ型を比較する
        for(int i = 0; i < currentColumnList.size(); i++) {
            GMap dataMap = currentColumnList.get(i);

            // 物理名比較
            String currentColumn = dataMap.get("column_name").toString();
            String fileColumn    = physicalNameList.get(i).toLowerCase();
            if(!currentColumn.equals(fileColumn)) {
                errorList.add(msg);
                return true;
            };

            // データ型比較
            String currentDataType = dataMap.get("column_type").toString();
            String fileDataType    = dataTypeList.get(i).toUpperCase();
            if(!fileDataType.startsWith(dataTypeHenkan(currentDataType))) {
                errorList.add(msg);
                return true;
            }
        }

        // 現行カラムとファイルカラムのプライマリーキー/NUT NULLを比較する
        List<String> currentPKList = postgreSystemLogic.selectPKColumn(EteamCommon.getContextSchemaName(), masterId);
        List<String> filePKList = new ArrayList<String>();

        for(int i = 0; i < primaryKeyKbnList.size(); i++) {
            // プライマリーキー設定のされているカラム名と現行のプライマリーキーが一致するかチェックする。
            if("1".equals(primaryKeyKbnList.get(i))) {
                filePKList.add(physicalNameList.get(i));
            }
        }

        // 現行テーブルのプライマリーキーとファイルのプライマリーキーの数をチェックする。
        if (currentPKList.size() != filePKList.size()) {
            errorList.add(msg);
            return true;
        } else {
            // 数が同じ場合は、カラム名をチェックする。
            for(int i = 0; i < currentPKList.size(); i++) {
                if(!currentPKList.get(i).equals(filePKList.get(i))) {
                    errorList.add(msg);
                    return true;
                }
                
            }
        }
        return false;
    }

    /**
     * データを登録します。
     */
    protected void insertData() {
        //全レコード×カラムの内容を入れる箱
        List<List<Object>> tourokuRecordList = new ArrayList<List<Object>>();
        
        //レコード単位のループ
        for(List<String> record : dataList) {

            //１レコードのカラム内容を入れる箱
            List<Object> tourokuColumnList = new ArrayList<Object>();

            //カラム単位のループ
            for(int i = 0; i < physicalNameList.size(); i++) {
                String dataType = dataTypeList.get(i).toUpperCase();
                
                if(dataType.startsWith("VARCHAR")) {
                    tourokuColumnList.add(record.get(i).replace(";;;", ","));//";;;"→","を表すメタ文字ということにした
                } else if(dataType.startsWith("INT") || dataType.startsWith("SMALLINT")) {
                    tourokuColumnList.add(Integer.parseInt(record.get(i)));
                } else if(dataType.startsWith("DECIMAL")) {
                    tourokuColumnList.add(toDecimal(record.get(i)));
                } else if(dataType.startsWith("DATE") || dataType.startsWith("TIMESTAMP")) {
                    if ("CURRENT_TIMESTAMP".equals(record.get(i).toUpperCase())) {
                        tourokuColumnList.add(record.get(i));
                    } else {
                        tourokuColumnList.add(toDate(record.get(i)));
                    }
                }
            }
            tourokuRecordList.add(tourokuColumnList);
        }

        //全レコード登録
        myLogic.insertData(masterId, tourokuRecordList);
        myLogic.afterUpdate(masterId);
    }
    
    /**
     * セッションにファイルデータを格納する。
     */
    protected void sessionSetFileData() {
        MasterData masterData = new MasterData();
        masterData.uploadFileFileName    = uploadFileFileName;
        masterData.uploadFileContentType = uploadFileContentType;
        masterData.uploadFileSize        = uploadFileSize;
        masterData.uploadFileData        = uploadFileData;
        session.put(masterId, masterData);
    }
    
    /**
     * セッションからファイルデータを取得する。
     */
    protected void sessionGetFileData() {
        MasterData masterData = (MasterData) session.get(masterId);
        // 通常ありえないが、ここでNULLポになったので対策しておく。
        if(masterData == null) {
            throw new RuntimeException();
        }
        uploadFileFileName    = masterData.uploadFileFileName;
        uploadFileSize        = masterData.uploadFileSize;
        uploadFileContentType = masterData.uploadFileContentType;
        uploadFileData        = masterData.uploadFileData;
    }
    
    /**
     * システム管理しているデータ型を登録時のデータ型へ変換を行う
     * PostgreSQLのシステムで管理しているデータ型と登録時に使用するデータ型が異なるため
     * @param dataType CSVファイルのデータ型
     * @return 変換後の型
     */
    protected String dataTypeHenkan(String dataType) {
        String result = "";
        switch (dataType) {
            case "varchar"  : result = "VARCHAR";   break;
            case "int2"     : result = "SMALLINT";  break;
            case "int4"     : result = "INT";       break;
            case "numeric"  : result = "DECIMAL";   break;
            case "date"     : result = "DATE";      break;
            case "timestamp": result = "TIMESTAMP"; break;
        }
        return result;
    }

    /**
     * 簡易届選択項目マスターの差異タイプ
     */
    enum KaniMasterDiffType{
    	/** 削除 */ DELETE,
    	/** 変更 */ CHANGE
    	//※追加は不要(追加する分にはいまのところここで何もする必用ない)
    }

    /**
     * 簡易届選択項目マスターの差異
     */
    class KaniSelectItemDiff{
    	/** 選択項目名 */ String selectItem;
    	/** 差異タイプ */ KaniMasterDiffType diffType;
    	/** 紐付き届出 */ Set<GMap> todokeList = new TreeSet<>();

    	/**
    	 * 初期化
    	 * @param selectItem 選択項目
    	 * @param diffType 差異タイプ
    	 */
    	KaniSelectItemDiff(String selectItem, KaniMasterDiffType diffType){
    		this.selectItem = selectItem;
    		this.diffType = diffType;
    	}
    }
    
    /**
     * kani_todoke_select_itemの変更前(DB)と変更後(画面/CSV入力)を比較して、変更点を返す
     * @param connection コネクション
     * @return 差異
     */
    protected List<KaniSelectItemDiff> judgeKaniSelectItemDiff(EteamConnection connection){
    	KaniTodokeSelectItemDao siDao = EteamContainer.getComponent(KaniTodokeSelectItemDao.class, connection);

    	List<KaniSelectItemDiff> ret = new ArrayList<>();
    	
    	//-----------------
    	//更新前データ
    	//-----------------
    	Map<String, List<KaniTodokeSelectItem>> before = siDao.load().stream().collect(Collectors.groupingBy(KaniTodokeSelectItem::getSelectItem));

    	//-----------------
    	//更新後データ
    	//-----------------
    	List<KaniTodokeSelectItem> tmpList = new ArrayList<>();
        for(List<String> record : dataList) {
        	KaniTodokeSelectItem tmp = new KaniTodokeSelectItem();
        	tmp.selectItem = record.get(0).replace(";;;", ",");
    		tmp.cd = record.get(1).replace(";;;", ",");
    		tmp.name = record.get(2).replace(";;;", ",");
    		tmpList.add(tmp);
        }
    	Map<String, List<KaniTodokeSelectItem>> after = tmpList.stream().collect(Collectors.groupingBy(KaniTodokeSelectItem::getSelectItem));

    	//-----------------
    	//select_item単位に削除、変更が行われれいるかチェック
    	//-----------------
    	for(String selectItem : before.keySet()) {
    		if(after.containsKey(selectItem)) {
    			List<KaniTodokeSelectItem> beforeItemList = before.get(selectItem);
    			List<KaniTodokeSelectItem> afterItemList = after.get(selectItem);
    			boolean exact = beforeItemList.size() == afterItemList.size();
    			if(exact) {
	    			for(KaniTodokeSelectItem beforeItem : beforeItemList) {
	    				if(!afterItemList.stream().anyMatch(afterItem -> afterItem.cd.equals(beforeItem.cd) && afterItem.name.equals(beforeItem.name))) {
	    					exact = false;
	    					break;
	    				}
	    			}
    			}
    			if(!exact) {
            		KaniSelectItemDiff diff = new KaniSelectItemDiff(selectItem, KaniMasterDiffType.CHANGE);
        			ret.add(diff);
    			}
    		}else {
        		KaniSelectItemDiff diff = new KaniSelectItemDiff(selectItem, KaniMasterDiffType.DELETE);
    			ret.add(diff);
    		}
    	}
    	
    	return ret;
    }
    
    /**
     * 簡易届選択項目の変更を許容するかどうか？許容しなければerrorListへ
     * @param connection コネクション
     * @param diffList 差異
     */
    protected void checkKaniSelectItemDiff(EteamConnection connection, List<KaniSelectItemDiff> diffList) {
    	KaniTodokeListKoDao listDao = EteamContainer.getComponent(KaniTodokeListKoDao.class, connection);

    	for(KaniSelectItemDiff diff :  diffList) {
    		if(diff.diffType == KaniMasterDiffType.DELETE) {
    			List<GMap> denpyouList = listDao.loadDenpyouKbnUsingSelectItem(diff.selectItem);
    			if(denpyouList.size() > 0) {
	    			List<String> denpyouStrList = denpyouList.stream().map(d -> d.get("denpyou_kbn") + ":" + d.get("denpyou_shubetsu")).collect(Collectors.toList());
	    			errorList.add("選択項目[" + diff.selectItem + "]は右記の届出で使用している為、削除できません。" + String.join("、", denpyouStrList));
    			}
    		}
    	}
    }
    
    /**
     * 簡易届選択項目の変更を簡易届メタデータに反映させる
     * @param connection コネクション
     * @param diffList 差異
     */
    protected void updateKaniTodoke(EteamConnection connection, List<KaniSelectItemDiff> diffList) {
		KaniTodokeLogic lg = EteamContainer.getComponent(KaniTodokeLogic.class, connection);
		KaniTodokeListKoDao listDao = EteamContainer.getComponent(KaniTodokeListKoDao.class, connection);
		
		//key = 伝票区分、value =(version, select_item[])という構造にする
		Map<String, GMap> todokedeMap = new HashMap<>();
		for(KaniSelectItemDiff diff : diffList) {
			List<GMap> denpyouKbnList = listDao.loadDenpyouKbnUsingSelectItem(diff.selectItem);
			for(GMap denpyouKbn : denpyouKbnList){
				GMap todokede = todokedeMap.get(denpyouKbn.get("denpyou_kbn"));
				if(todokede == null) {
					todokede = new GMap();
					todokede.put("version", denpyouKbn.get("version"));
					todokede.put("select_item", new ArrayList<String>());
					todokedeMap.put(denpyouKbn.get("denpyou_kbn"), todokede);
				}
				((List<String>)todokede.get("select_item")).add(diff.selectItem);
			}
		}
		
		//届出単位にversionを上げて簡易届選択項目の変更を反映させる
		for(String denpyouKbn : todokedeMap.keySet()) {
			GMap todokede = todokedeMap.get(denpyouKbn);
			lg.updateSelectItem(getUser(), denpyouKbn, todokede.get("version"), todokede.get("select_item"));
		}
	}
}
