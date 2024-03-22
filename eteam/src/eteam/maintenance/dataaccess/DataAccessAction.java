package eteam.maintenance.dataaccess;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.EteamFormatter;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.base.exception.EteamAccessDeniedException;
import eteam.base.exception.EteamDataNotFoundException;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.ContentType;
import eteam.common.EteamConst.Encoding;
import eteam.common.EteamConst.TableName;
import eteam.common.EteamFileLogic;
import eteam.common.EteamNaibuCodeSetting.BUHIN_FORMAT;
import eteam.common.select.DataAccessCategoryLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * データアクセス一覧画面Action
 */
@Getter @Setter @ToString
public class DataAccessAction  extends EteamAbstractAction {

    //＜定数＞
    /** ＝ */
    protected static final String EQUAL = "1";
    /** ≠ */
    protected static final String NOT_EQUAL = "2";
    /** ＞＝= */
    protected static final String DAINARI_EQUAL = "3";
    /** ＜＝ */
    protected static final String SHOUNARI_EQUAL = "4";
    
    //＜画面入力＞
    /** テーブル名(物理) */
    String tableName;
    /** ページ番号 */
    String pageNo = "1";
    
    /** カラム１ */
    String column1;
    /** カラム２ */
    String column2;
    /** カラム３ */
    String column3;
    
    /** 条件符号１ */
    String condition1;
    /** 条件符号２ */
    String condition2;
    /** 条件符号３ */
    String condition3;
    
    /** 条件値１ */
    String whereVal1;
    /** 条件値２ */
    String whereVal2;
    /** 条件値３ */
    String whereVal3;

    //＜画面入力以外＞
    /** テーブル名(論理) */
    String tableComment;
    /** カラムリスト */
    List<GMap> columnList;
    /** プライマリキーリスト */
    List<GMap> pkColumnList;
    /** データHTML文字列 */
    String listData;

    /** データ件数 */
    long totalCount;
    /** 全ページ数 */
    long totalPage;
    /** ページング処理 link押下時のURL */
    String pagingLink;

    /** コネクション */
    EteamConnection connection;
    /** データアクセスカテゴリロジック */
     DataAccessCategoryLogic dataAccessCategoryLogic;
     /** ログ */
     static EteamLogger log = EteamLogger.getLogger(DataAccessAction.class);

    @Override
    protected void formatCheck() {
        //DataAccessCategoryLogic#loadTableListの抽出対象外の条件と同期
        if (TableName.PASSWORD.equals(tableName)) throw new EteamAccessDeniedException();
        checkString(tableName, 1, 60,"テーブル名",false);
        checkString(column1, 1, 60,"条件列１",false);
        checkNumberRange(condition1, 1, 9,"条件符号１",false);
        checkString(whereVal1, 1, 100,"条件値１",false);
        checkString(column2, 1, 60,"条件列２",false);
        checkNumberRange(condition2, 1, 9,"条件符号２",false);
        checkString(whereVal2, 1, 100,"条件値２",false);
        checkString(column3, 1, 60,"条件列３",false);
        checkNumberRange(condition3, 1, 9,"条件符号３",false);
        checkString(whereVal3, 1, 100,"条件値３",false);
    }

    @Override
    protected void hissuCheck(int eventNum) {
        String[][] list = {
            //項目						EVENT1～の必須フラグ(0:チェック無し、1:入力エラーチェック、2:不正リクエストチェック(表示イベントのキー項目))
            {tableName, "テーブル名"	,"2"}, //KEY
        };
        hissuCheckCommon(list, eventNum);
    }

    /**
     * データ表示
     * @return 処理結果
     */
    public String init() {
        connection = EteamConnection.connect();
        try {
            
            initParts();
            
            // 入力チェック
            hissuCheck(1);
            
            // フォーマットチェック
            formatCheck();

            if (!errorList.isEmpty()) {
                throw new EteamDataNotFoundException();
            }

            // テーブル情報を出力する
            return OutputTableInfo(false);
            
        } finally {
            connection.close();
        }
    }

    /**
     * 検索イベント
     * @return 処理結果
     */
    public String kensaku(){
        connection = EteamConnection.connect();
        try {
            
            initParts();
            
            // 入力チェック
            hissuCheck(1);
            
            // フォーマットチェック
            formatCheck();

            if (!errorList.isEmpty()) {
                throw new EteamDataNotFoundException();
            }

            // テーブル情報を出力する
            return OutputTableInfo(false);
            
        } finally {
            connection.close();
        }
    }

    /**
     * CSVダウンロード処理
     * @return 処理結果
     */
    public String csvOutput() {
        connection = EteamConnection.connect();
        try {
            
            initParts();
    
            // 入力チェック
            hissuCheck(1);
            
            // フォーマットチェック
            formatCheck();
            
            if (!errorList.isEmpty()) {
                throw new EteamDataNotFoundException();
            }
            
            // テーブル情報を出力する
            return OutputTableInfo(true);

        } finally {
            connection.close();
        }
    }

    /**
     * 初期化処理
     */
    protected void initParts() {
        dataAccessCategoryLogic = EteamContainer.getComponent(DataAccessCategoryLogic.class, connection);
    }
    
    /** 
     * テーブル情報を出力する
     * @param csvFlg CSV出力フラグ
     * @return 成否
     */
    protected String OutputTableInfo(boolean csvFlg){
        
        String schemaName = EteamCommon.getContextSchemaName();

        GMap mapTableComment = dataAccessCategoryLogic.findTableComment(tableName, schemaName);

        if (mapTableComment == null || mapTableComment.isEmpty()) {
            throw new EteamDataNotFoundException();
        }
        
        tableComment = mapTableComment.getString("table_comment");

        // カラム名の取得 --------------------------------------------------------
        columnList = dataAccessCategoryLogic.loadTableColumnName(tableName, schemaName);

        StringBuilder colNm = new StringBuilder();

        int colCnt = 0;
        for (GMap columnMap : columnList) {
            if (colCnt > 0) {
                colNm.append(",");
            }
            switch(columnMap.getString("data_type")) {
            case "bytea":
                colNm.append("'-' AS "+columnMap.getString("column_name"));
                break;
            default:
                colNm.append(columnMap.getString("column_name"));
                break;
            }
            colCnt++;
        }
        // ------------------------------------------------------------------------
        
        // プライマリキーの取得----------------------------------------------------
        pkColumnList = dataAccessCategoryLogic.loadPrymaryKeyColumn(tableName, schemaName);

        StringBuilder colPk = new StringBuilder();
        int pkColCnt = 0;
        for (GMap pkColumnMap : pkColumnList) {

            if (pkColCnt > 0) {
                colPk.append(",");
            }
            colPk.append(pkColumnMap.getString("column_name"));
            pkColCnt++;
        }
        // ------------------------------------------------------------------------
        
        // 初期表示以外は、検索条件で絞込検索する
        StringBuilder sbWhere = new StringBuilder();
        List<Object> lstWhereParam = new ArrayList<Object>();
        
        SetWhere("条件１", sbWhere, lstWhereParam, schemaName, column1, condition1, whereVal1);
        SetWhere("条件２", sbWhere, lstWhereParam, schemaName, column2, condition2, whereVal2);
        SetWhere("条件３", sbWhere, lstWhereParam, schemaName, column3, condition3, whereVal3);
        
        if (!errorList.isEmpty()) {
            return "error";
        }
        
        String where = sbWhere.toString();
        Object[] whereParam = lstWhereParam.toArray();

        // テーブルのデータ件数を取得
        totalCount = dataAccessCategoryLogic.findDataCount(tableName, where, whereParam);

        // 設定情報の取得（１ページ表示件数）
        int intPageMax = Integer.parseInt(setting.recordNumPerPage());

        // 総ページ数の計算
        totalPage = EteamCommon.calcTotalPageNum(intPageMax, totalCount); 
        if (totalPage == 0) {
            totalPage = 1;
        }
        
        if (csvFlg) {
            // CSV作成
            createCsv(colNm.toString(), colPk.toString(), intPageMax, where, whereParam);

        } else {
            // 表示ページ番号が総ページ数より大きかったら、総ページ数を表示ページ番号にする。
            if(isEmpty(pageNo) || "0".equals(pageNo)){
                pageNo = "1";
            } else if(Integer.parseInt(pageNo) > totalPage) {
                pageNo = String.valueOf(totalPage);
            }
            int intPageNo = Integer.parseInt(pageNo);

            //検索条件のURLを作成
            String urlParam="";
            try {
                urlParam = "&column1=" + URLEncoder.encode((column1 == null ? "" : column1.toString()),"UTF-8")
                          + "&condition1=" + URLEncoder.encode((condition1 == null ? "" : condition1.toString()),"UTF-8")
                         + "&whereVal1=" + URLEncoder.encode((whereVal1 == null ? "" : whereVal1.toString()),"UTF-8")
                         + "&column2=" + URLEncoder.encode((column2 == null ? "" : column2.toString()),"UTF-8")
                          + "&condition2=" + URLEncoder.encode((condition2 == null ? "" : condition2.toString()),"UTF-8")
                         + "&whereVal2=" + URLEncoder.encode((whereVal2 == null ? "" : whereVal2.toString()),"UTF-8")
                         + "&column3=" + URLEncoder.encode((column3 == null ? "" : column3.toString()),"UTF-8")
                          + "&condition3=" + URLEncoder.encode((condition3 == null ? "" : condition3.toString()),"UTF-8")
                         + "&whereVal3=" + URLEncoder.encode((whereVal3 == null ? "" : whereVal3.toString()),"UTF-8");

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            
            // ページングリンクURLを設定
            pagingLink = "data_access?tableName=" + tableName + urlParam + "&";

            // HTML作成
            createHtml(colNm.toString(), colPk.toString(), intPageNo, intPageMax, where, whereParam);
        }
        
        return "success";
    }
    
    /**
     * Where句を設定する
     * @param lableName ラベル名
     * @param sbWhere where句
     * @param paramList パラメータリスト
     * @param schemaName スキーマ名
     * @param column 検索列
     * @param condition 検索条件
     * @param whereVal 検索値
     */
    protected void SetWhere(String lableName, StringBuilder sbWhere, List<Object> paramList, String schemaName, String column, String condition, String whereVal) {
        
        List<GMap> listMap = dataAccessCategoryLogic.loadTableColumnName(tableName, schemaName, column);

        if (listMap == null || listMap.size() == 0) {
            return;
        }
        
        if (StringUtils.isNotEmpty(column) && StringUtils.isNotEmpty(condition) && StringUtils.isNotEmpty(whereVal)) {
            
            GMap map = listMap.get(0);
            Object mapMaxLen = null;
            
            String dataFormat = map.get("data_format").toString();
            String dataType = map.get("data_type").toString();
            boolean isArray = map.get("is_array");
            String conditionMark = "";
            int errCnt = errorList.size();

            switch (condition){
            case EQUAL:
                conditionMark = "="; 
                break;
            case NOT_EQUAL:
                conditionMark = "!=";
                break;
            case DAINARI_EQUAL:
                conditionMark = isArray? "<=" : ">=";
                break;
            case SHOUNARI_EQUAL:
                conditionMark = isArray? ">=" : "<=";
                break;
            }
            
            switch (dataFormat){
            case BUHIN_FORMAT.NUMBER:

                // 形式チェック
                switch (dataType){
                case "bigint":
                    checkNumber(whereVal, 1, 18, lableName, false);
                    break;
                case "integer":
                    checkNumber(whereVal, 1, 9, lableName, false);
                    break;
                case "smallint":
                    checkNumber(whereVal, 1, 4, lableName, false);
                    break;
                case "numeric":
                    mapMaxLen = map.get("numeric_precision");
                    if (mapMaxLen != null && !mapMaxLen.toString().equals("")) {
                        checkNumber(whereVal, 1, Integer.parseInt(mapMaxLen.toString()), lableName, false);
                    }else {
                        checkNumber(whereVal, lableName, false);
                    }
                    break;
                }
                
                // 形式チェックエラーがない場合、条件を追加する
                if (errCnt == errorList.size()) {
            
                    if(isArray) {
                        sbWhere.append(" AND ? " + conditionMark + " ANY(" + column + ")");
                    }else {
                        sbWhere.append(" AND " + column + " " + conditionMark + " ?");
                    }
                    
                    switch (dataType){
                    case "bigint":
                        paramList.add(Long.parseLong(whereVal));
                        break;
                    case "integer":
                        paramList.add(Integer.parseInt(whereVal));
                        break;
                    case "smallint":
                        paramList.add(Short.parseShort(whereVal));
                        break;
                    case "numeric":
                        paramList.add(Float.parseFloat(whereVal));
                        break;
                    }
                }

                break;
                
            case BUHIN_FORMAT.DATE:
                
                // 形式チェック
                checkDate(whereVal, lableName, false);
                
                // 形式チェックエラーがない場合、条件を追加する
                if (errCnt == errorList.size()) {

                    switch (dataType){
                    case "date":
                        sbWhere.append(" AND " + column + " " + conditionMark + " TO_DATE(?, 'YYYY/MM/DD')");
                        paramList.add(whereVal);
                        break;
                        
                    case "timestamp":
                    case "timestamp without time zone":
                        
                        String fmDate = whereVal + " 00:00:00";
                        String toDate = whereVal + " 23:59:59";
                        
                        switch (condition){
                        case EQUAL:
                            sbWhere.append(" AND " + column + " BETWEEN" + " TO_TIMESTAMP(?, 'YYYY/MM/DD hh24:mi:ss') AND TO_TIMESTAMP(?, 'YYYY/MM/DD hh24:mi:ss')");
                            paramList.add(fmDate);
                            paramList.add(toDate);
                            break;
                        case NOT_EQUAL:
                            sbWhere.append(" AND " + column + " NOT BETWEEN" + " TO_TIMESTAMP(?, 'YYYY/MM/DD hh24:mi:ss') AND TO_TIMESTAMP(?, 'YYYY/MM/DD hh24:mi:ss')");
                            paramList.add(fmDate);
                            paramList.add(toDate);
                            break;
                        case DAINARI_EQUAL:
                            sbWhere.append(" AND " + column + " >= TO_TIMESTAMP(?, 'YYYY/MM/DD hh24:mi:ss')");
                            paramList.add(fmDate);
                            break;
                        case SHOUNARI_EQUAL:
                            sbWhere.append(" AND " + column + " <= TO_TIMESTAMP(?, 'YYYY/MM/DD hh24:mi:ss')");
                            paramList.add(toDate);
                            break;
                        }
                        
                        break;
                    }
                }
                
                break;
                
            case BUHIN_FORMAT.STRING:
                
                // 形式チェック
                mapMaxLen = map.get("character_maximum_length");
                if (mapMaxLen != null && !mapMaxLen.toString().equals("")) {
                    checkString(whereVal, 1, Integer.parseInt(mapMaxLen.toString()), lableName, false);
                }
                
                // 形式チェックエラーがない場合、条件を追加する
                if (errCnt == errorList.size()) {
                    if(isArray) {
                        sbWhere.append(" AND ? " + conditionMark + " ANY(" + column + ")");
                    }else {
                        sbWhere.append(" AND " + column + " " + conditionMark + " ?");
                    }
                    paramList.add(whereVal);
                }
                
                break;
                
            default:
                
                switch (dataType){
                case "bytea":
                    errorList.add(lableName + "は画像ファイルなどの圧縮データのため、条件に含めることができません。");
                    break;
                }
                
                break;
            }
        }
    }
    
    /**
     * HTML文字列を作成する
     * @param colNm カラム名カンマ区切り
     * @param colPk カラム名カンマ区切り(PKのみ)
     * @param intPageNo ページ番号
     * @param intPageMax １ページ最大表示件数
     * @param where WHERE句
     * @param whereParam WHERE句のパラメータ
     */
    protected void createHtml(String colNm, String colPk, int intPageNo, int intPageMax, String  where, Object[] whereParam){
        List<GMap> dataList = dataAccessCategoryLogic.loadTableData(tableName, colNm.toString(), colPk.toString(), intPageNo, intPageMax, where, whereParam);

        StringBuilder builder = new StringBuilder();
        builder.append("<table class='table-bordered table-condensed'>");
        builder.append("<thead>");

        builder.append("<tr>");
        // ヘッダ部(和名)
        for (GMap map : columnList) {

            if (map.get("column_comment") != null) {
                builder.append("<th>" + EteamFormatter.htmlEscape(map.getString("column_comment")) + "</th>");
            } else {
                builder.append("<th></th>");
            }
        }
        builder.append("</tr>");
        builder.append("<tr>");
        // ヘッダ部(物理名)
        for (GMap map : columnList) {
            builder.append("<th>" + n2b(map.getString("column_name")) + "</th>");
        }
        builder.append("</tr>");
        builder.append("</thead>");
        // BODY部
        builder.append("<tbody>");
        for (GMap map : dataList) {
            builder.append("<tr>");
            for (GMap colMap : columnList) {

                builder.append("<td>");
                String col = colMap.getString("column_name");
                builder.append(EteamFormatter.htmlEscapeBr(map.getString(col)));
                builder.append("</td>");
            }
            builder.append("</tr>");
        }
        builder.append("</tbody>");
        builder.append("</table>");
        listData = builder.toString();
    }
    
    /**
     * CSVファイルを作成す
     * @param colNm カラム名カンマ区切り
     * @param colPk カラム名カンマ区切り(PKのみ)
     * @param intPageMax １ページ最大表示件数
     * @param where WHERE句
     * @param whereParam WHERE句のパラメータ
     */
    protected void createCsv(String colNm, String colPk, int intPageMax, String  where, Object[] whereParam) {
        int browserCode = EteamCommon.getBrowserCode(ServletActionContext.getRequest());
        HttpServletResponse response = ServletActionContext.getResponse();
        PrintWriter writer = null;

        try {
            response.setContentType(ContentType.EXCEL);
            response.setHeader("Content-Disposition", EteamCommon.contentDisposition(browserCode, true, tableName + ".csv"));
            response.setCharacterEncoding(Encoding.MS932);
            writer = response.getWriter();
        
            // 1行目：カラム名(物理)、2行目：カラム名(和名)のリストを作る
            List<Object> colWa = new ArrayList<Object>();
            List<Object> colBu = new ArrayList<Object>();
            for (GMap columnMap : columnList) {
                colWa.add(columnMap.getString("column_comment"));
                colBu.add(columnMap.getString("column_name"));
            }
            
            EteamFileLogic.outputCsvRecord(writer, colWa);
            EteamFileLogic.outputCsvRecord(writer, colBu);
            
            // 3行目以降：データ部のリストを作る
            int tmpReadSize = EteamConnection.DB_FETCH_SIZE;
            int tmpMaxPage = (int)(totalCount / tmpReadSize + (totalCount % tmpReadSize == 0 ? 0 : 1));
            for(int tmpPageNo = 1; tmpPageNo <= tmpMaxPage; tmpPageNo++) {
                List<GMap> dataList = dataAccessCategoryLogic.loadTableData(tableName, colNm.toString(), colPk.toString(), tmpPageNo, tmpReadSize, where, whereParam);
                for(GMap dataMap : dataList) {
                    List<Object> csvRecord = new ArrayList<Object>();
                    for (GMap colMap : columnList) {
                        String col = colMap.getString("column_name");
                        if("numeric".equals(colMap.get("data_type")) && dataMap.get(col) != null) {
                            // ただのnumericとnumeric配列の場合とで、処理を分ける。
                            // numeric配列の場合はそのまま出力（項目区切りのカンマがあるため）
                            if(dataMap.get(col).toString().indexOf("{") < 0) {
                                csvRecord.add(new DecimalFormat("#,##0.#####").format(dataMap.get(col)));
                            } else {
                                csvRecord.add(dataMap.get(col));
                            }
                        } else {
                            csvRecord.add(dataMap.get(col));
                        }
                    }
                    EteamFileLogic.outputCsvRecord(writer, csvRecord);
                }
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(writer != null) {
                writer.close();
            }
        }
    }
}
