package eteam.base;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.postgresql.Driver;

import com.opensymphony.xwork2.ActionContext;
import com.sybase.jdbc4.jdbc.SybDriver;

import eteam.base.exception.EteamDBConnectException;
import eteam.base.exception.EteamSQLException;
import eteam.base.exception.EteamSQLExceptionHandler;
import eteam.common.EteamCommon;
import eteam.common.EteamConst;
import eteam.common.EteamSettingInfo;
import eteam.common.EteamSettingInfo.Key;
import eteam.common.open21.Open21Env;

/**
 * データアクセス用部品
 */
public class EteamConnection implements AutoCloseable {

	/** マスタ取込向けselect処理呼出し時のフェッチサイズ */
	public static final int DB_FETCH_SIZE = 1000;

	/** コネクション */
	protected Connection connection;
	/** フェッチ時データ保持用Statement */
	protected Statement fetchsStatement;
	/** フェッチ時データ保持用ResultSet */
	protected ResultSet fetchResultSet;
	/** このコネクションがみるスキーマ名 */
	protected String schema;

	/**
	 * コネクション渡し
	 * @param _connection コネクション
	 */
	public void init(Connection _connection) {
		this.connection = _connection;
	}

	/**
	 * コネクション渡し
	 * @param _connection コネクション
	 * @param _schema スキーマ(OPEN21等のコネクションならnullでよい。eteamコネクションなら渡して)
	 */
	public void init(Connection _connection, String _schema) {
		this.connection = _connection;
		this.schema = _schema;
	}

	/**
	 * コネクトする。
	 * 通常はコネクションプールからの取得。
	 * System.setProperty("isUt")を呼ばれている場合は、ローカルホストの固定値から取得する。
	 * @return コネクション
	 */
	public static EteamConnection connect() {
		if (null != ActionContext.getContext()) {
			return connect4Web();
		} else {
			return connect4Bat();
		}
	}

	/**
	 * コネクトする（実際はプールから貸し出す）。
	 * コネクションにはオートコミット設定を行う。
	 * またデフォルトスキーマをセット。スキーマ名はサーブレットコンテキストのパラメータから取得。
	 * @return コネクション
	 */
	protected static EteamConnection connect4Web(){
		try {
			String schema = EteamCommon.getContextSchemaName();

			InitialContext initCon = new InitialContext();
			DataSource dataSource = (DataSource)initCon.lookup(EteamConst.Env.DS_ENV);
			Connection connection = dataSource.getConnection();

			EteamConnection ret = EteamContainer.getComponent(EteamConnection.class);
			ret.init(connection, schema);

			ret.beginTransaction();
			return ret;
		} catch (SQLException e) {
			throw new EteamDBConnectException(e);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * バッチ用のセレクト。
	 * @return コネクション
	 */
	protected static EteamConnection connect4Bat(){
		try {
			String schema = EteamCommon.getBatSchemaName();

			Properties prop = new Properties();
			String url = "jdbc:postgresql://localhost:" + Open21Env.getPortWF() + "/eteam"; //デフォルトポートは5432
			String user = "eteam";
			String pass = "1QAZxsw2";

			prop.setProperty("user", user);
			prop.setProperty("username", user); //不要？
			prop.setProperty("pass", pass); //不要？
			prop.setProperty("password", pass);
			Connection connection = new Driver().connect(url, prop);

			EteamConnection ret = EteamContainer.getComponent(EteamConnection.class);
			ret.init(connection, schema);

			ret.beginTransaction();
			return ret;
		} catch (SQLException e) {
			throw new EteamDBConnectException(e);
		}
	}

	/**
	 * バッチ用のセレクト。
	 * @return コネクション
	 */
	protected static EteamConnection connect4NoTrx(){
		try {
			Properties prop = new Properties();
			String url = "jdbc:postgresql://localhost:" + Open21Env.getPortWF() + "/eteam"; //デフォルトポートは5432
			String user = "eteam";
			String pass = "1QAZxsw2";

			prop.setProperty("user", user);
			prop.setProperty("username", user); //不要？
			prop.setProperty("pass", pass); //不要？
			prop.setProperty("password", pass);
			Connection connection = new Driver().connect(url, prop);

			EteamConnection ret = EteamContainer.getComponent(EteamConnection.class);
			ret.init(connection);
			return ret;
		} catch (SQLException e) {
			throw new EteamDBConnectException(e);
		}
	}

	/**
	 * トランザクションを開始
	 */
	public void beginTransaction() {
		try {
			connection.setAutoCommit(false);
			Statement s = connection.createStatement();
			s.execute("set search_path to " + schema);
			s.close();
		} catch (SQLException e) {
			throw new EteamDBConnectException(e);
		}
	}

	/**
	 * OPEN21の債務管理ユーザーを使用しDBとコネクトする。
	 * @return コネクション
	 */
	public static EteamConnection connect4De3Type4(){

		try {
			String host = Open21Env.getHost4DE3();
			SybDriver sybDriver = (SybDriver)Class.forName("com.sybase.jdbc4.jdbc.SybDriver").getDeclaredConstructor().newInstance();
			String database = "DE3Z" + EteamSettingInfo.getSettingInfo(Key.OP21MPARAM_KAISHA_CD);
			Properties prop = new Properties();
			prop.setProperty("user"		, "REFRE0001");
			prop.setProperty("password"	, "ICSP_LVL1");
			prop.setProperty("DATABASE", database);
			Connection connection = sybDriver.connect("jdbc:sybase:Tds:" + host + "?ServiceName=" + database, prop);
			EteamConnection ret = EteamContainer.getComponent(EteamConnection.class);
			ret.init(connection);
			return ret;
		} catch (SQLException e) {
			throw new EteamDBConnectException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * OPEN21の債務管理ユーザーを使用しDBとコネクトする。（共通DB用）
	 * @return コネクション
	 */
	public static EteamConnection connect4De3Type4Common(){

		try {
			String host = Open21Env.getHost4DE3();
			SybDriver sybDriver = (SybDriver)Class.forName("com.sybase.jdbc4.jdbc.SybDriver").getDeclaredConstructor().newInstance();
			String database = "DE3C9999";
			Properties prop = new Properties();
			prop.setProperty("user"		, "REFRE0001");
			prop.setProperty("password"	, "ICSP_LVL1");
			prop.setProperty("DATABASE", database);
			Connection connection = sybDriver.connect("jdbc:sybase:Tds:" + host + "?ServiceName=" + database, prop);
			EteamConnection ret = EteamContainer.getComponent(EteamConnection.class);
			ret.init(connection);
			return ret;
		} catch (SQLException e) {
			throw new EteamDBConnectException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * SIASマスター取込向けコネクタ
	 * @return コネクション
	 */
	public static EteamConnection connect4SIAS(){
		try {
			String dsn = "ICSP_312Z" + EteamSettingInfo.getSettingInfo(Key.OP21MPARAM_KAISHA_CD);
			String host = Open21Env.getHost4SIAS();
			String url = "jdbc:sqlserver://" + host +  ";user=ICSP_312REFRE0001;password=ICS_VIEW_SIAS;database=" + dsn + ";";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection connection=DriverManager.getConnection(url);
			connection.setAutoCommit(false);
			connection.createStatement();
			EteamConnection ret = EteamContainer.getComponent(EteamConnection.class);
			ret.init(connection);
			return ret;
		} catch (SQLException e) {
			throw new EteamDBConnectException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * SIASマスター取込向けコネクタ(債権債務DB用)
	 * @return コネクション 債権債務DBへの接続に失敗した場合はnull
	 */
	public static EteamConnection connect4SIASsaiken(){
		try {
			String dsn = "ICSP_312RVT" + EteamSettingInfo.getSettingInfo(Key.OP21MPARAM_KAISHA_CD);
			String host = Open21Env.getHost4SIAS();
			String url = "jdbc:sqlserver://" + host +  ";user=ICSP_312REFRE0001;password=ICS_VIEW_SIAS;database=" + dsn + ";";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection connection=DriverManager.getConnection(url);
			connection.setAutoCommit(false);
			connection.createStatement();
			EteamConnection ret = EteamContainer.getComponent(EteamConnection.class);
			ret.init(connection);
			return ret;

		// 債権債務DBへの接続に失敗した場合はnullを返却
		} catch (SQLException e) {
			return null;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * SIASマスター取込向けコネクタ（共通DB用）
	 * @return コネクション
	 */
	public static EteamConnection connect4SIASCommon(){
		try {
			String dsn = "ICSP_312C9999";
			String host = Open21Env.getHost4SIAS();
			String url = "jdbc:sqlserver://" + host +  ";user=ICSP_312REFRE0001;password=ICS_VIEW_SIAS;database=" + dsn + ";";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection connection=DriverManager.getConnection(url);
			connection.setAutoCommit(false);
			connection.createStatement();
			EteamConnection ret = EteamContainer.getComponent(EteamConnection.class);
			ret.init(connection);
			return ret;
		} catch (SQLException e) {
			throw new EteamDBConnectException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}



	/**
	 * SIASマスター取込(PostgreSQL版)向けコネクタ
	 * @return コネクション
	 */
	public static EteamConnection connect4SIASMK2(){
		try {
			String dsn = "icsp_312z" + EteamSettingInfo.getSettingInfo(Key.OP21MPARAM_KAISHA_CD);
			String host = Open21Env.getHost4SIAS();
			String url = "jdbc:postgresql://" + host + "/" + dsn;

			Properties prop = new Properties();
			prop.setProperty("user", "icsp_312refre0001");
			prop.setProperty("password", "ICS_VIEW_SIAS");
			Connection connection = new Driver().connect(url, prop);

			connection.setAutoCommit(false);
			connection.createStatement();
			EteamConnection ret = EteamContainer.getComponent(EteamConnection.class);
			ret.init(connection);
			return ret;
		} catch (SQLException e) {
			throw new EteamDBConnectException(e);
		}
	}

	/**
	 * SIASマスター取込(PostgreSQL版)向けコネクタ(債権債務DB用)
	 * @return コネクション 債権債務DBへの接続に失敗した場合はnull
	 */
	public static EteamConnection connect4SIASMK2saiken(){
		try {
			String dsn = "icsp_312rvt" + EteamSettingInfo.getSettingInfo(Key.OP21MPARAM_KAISHA_CD);
			String host = Open21Env.getHost4SIAS();
			String url = "jdbc:postgresql://" + host + "/" + dsn;

			Properties prop = new Properties();
			prop.setProperty("user", "icsp_312refre0001");
			prop.setProperty("password", "ICS_VIEW_SIAS");
			Connection connection = new Driver().connect(url, prop);

			connection.setAutoCommit(false);
			connection.createStatement();
			EteamConnection ret = EteamContainer.getComponent(EteamConnection.class);
			ret.init(connection);
			return ret;

		// 債権債務DBへの接続に失敗した場合はnullを返却
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * SIASマスター取込(PostgreSQL版)向けコネクタ（共通DB用）
	 * @return コネクション
	 */
	public static EteamConnection connect4SIASMK2Common(){
		try {
			String dsn = "icsp_312c9999";
			String host = Open21Env.getHost4SIAS();
			String url = "jdbc:postgresql://" + host + "/" + dsn;

			Properties prop = new Properties();
			prop.setProperty("user", "icsp_312refre0001");
			prop.setProperty("password", "ICS_VIEW_SIAS");
			Connection connection = new Driver().connect(url, prop);

			connection.setAutoCommit(false);
			connection.createStatement();
			EteamConnection ret = EteamContainer.getComponent(EteamConnection.class);
			ret.init(connection);
			return ret;
		} catch (SQLException e) {
			throw new EteamDBConnectException(e);
		}
	}

	/**
	 * コミットする
	 */
	public void commit(){
		try {
			connection.commit();
		} catch(SQLException e) {
			throw new EteamSQLException(e);
		}
	}

	/**
	 * ロールバックする
	 */
	public void rollback(){
		try{
			CloseFetchResultSet();
			connection.rollback();
		} catch(SQLException e) {
			throw EteamSQLExceptionHandler.makeException(e);
		}
	}

	/**
	 * クローズする
	 */
	@Override //AutoCloseable
	public void close(){
		try {
			CloseFetchResultSet();
			connection.close();
		} catch(SQLException e) {
			throw EteamSQLExceptionHandler.makeException(e);
		}
	}

	/**
	 * 検索する。1レコード目を返す。検索結果が0レコードならnullを返し、2レコードあったら例外。
	 * @param sql SQL
	 * @param params パラメータ
	 * @return 検索結果
	 */
	public GMap find(String sql, Object... params){
		try {
			QueryRunner q = new QueryRunner();
			return q.query(connection, sql, new EteamMapResultSetHandler(), params);
		} catch(SQLException e) {
			throw EteamSQLExceptionHandler.makeException(e);
		}
	}

	/**
	 * 検索する。検索結果が0件ならサイズ0のリストを返す。
	 * 返却するハッシュを適正化するため、高速になります。
	 * @param sql SQL
	 * @param params パラメータ
	 * @return 検索結果
	 */
	public List<GMap> load(String sql, Object... params){
		try {
			QueryRunner q = new QueryRunner();
			return q.query(connection, sql, new EteamListMapResultSetHandler(), params);
		} catch(SQLException e) {
			throw EteamSQLExceptionHandler.makeException(e);
		}
	}
	/**
	 * テーブルの存在チェックをします。
	 * @param tableName テーブル名
	 * @param isPostgre PostgreSQLか
	 * @return true:存在する。 false:存在しない
	 */
	public boolean tableSonzaiCheck(String tableName, boolean isPostgre) {
		if(isPostgre)
		{
			String sql = "SELECT relname FROM pg_class WHERE relkind = 'r' AND relname = ? ";
			return this.find(sql, tableName) == null ? false : true;
		}
		
		String sql = "IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES"
				+ "  WHERE TABLE_TYPE='BASE TABLE' AND TABLE_NAME= ?)"
				+ "  SELECT 'TRUE' AS res ELSE SELECT 'FALSE' AS res;";
		return this.find(sql, tableName).getString("res").toLowerCase().equals("true");
	}
	
	/**
	 * マスタ取込向け検索。ResultSetを返す。
	 * @param sqlSearch SQL
	 * @return 検索結果ResultSet
	 */
	public ResultSet loadFetchResultSet(String sqlSearch){
		try {
			//既に保持用データ存在する場合は初期化
			CloseFetchResultSet();

			fetchsStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			fetchsStatement.setFetchSize(DB_FETCH_SIZE);

			fetchResultSet = fetchsStatement.executeQuery(sqlSearch);

			return fetchResultSet;

		} catch(SQLException e) {
			throw EteamSQLExceptionHandler.makeException(e);
		}
	}

	/**
	 * フェッチ時保持データをクリアし、クローズする。
	 */
	public void CloseFetchResultSet(){
		if(fetchResultSet != null){
			try {
				fetchResultSet.close();
			} catch (SQLException e) {
				throw EteamSQLExceptionHandler.makeException(e);
			}
		}
		if(fetchsStatement != null){
			try {
				fetchsStatement.close();
			} catch (SQLException e) {
				throw EteamSQLExceptionHandler.makeException(e);
			}
		}
	}

	/**
	 * 更新する。(INSERT, UPDATE, DELETE)
	 * @param sql SQL
	 * @param params パラメータ
	 * @return 更新件数
	 */
	public int update(String sql, Object... params) {
		try {
			QueryRunner q = new QueryRunner();
			return q.update(connection, sql, params);
		} catch(SQLException e) {
			throw EteamSQLExceptionHandler.makeException(e);
		}
	}



	/**
	 * search_pathの設定を行います。
	 */
	public void setSearchPath(){
		String schema = EteamCommon.getBatSchemaName();
		Statement s;
		try {
			s = connection.createStatement();
			s.execute("set search_path to " + schema);
			s.close();
		} catch (SQLException e) {
			throw new EteamDBConnectException(e);
		}
	}

    /**
	 * 数値配列をSQLパラメータ代入用のArrayにして返却する。
     * @param obj 変換対象配列
     * @return Array化されたinteger配列
     */
    public Array getIntegerArray(Object[] obj){
        try {
                return this.connection.createArrayOf("integer", obj);
        } catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }
	/**
	 * String配列をSQLパラメータ代入用のArrayにして返却する。
	 * @param str 変換対象配列
	 * @return Array化されたString配列
	 */
	public Array getDecimalArray(BigDecimal[] str){
		try {
			return this.connection.createArrayOf("decimal", str);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
    }
	/**
	 * String配列をSQLパラメータ代入用のArrayにして返却する。
	 * @param str 変換対象配列
	 * @return Array化されたString配列
	 */
	public Array getArray(String[] str){
		try {
			return this.connection.createArrayOf("varchar", str);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
    }
	/**
	 * String配列をSQLパラメータ代入用のArrayにして返却する。
	 * @param set 変換対象配列
	 * @return Array化されたString配列
	 */
	public Array getArray(Set<String> set){
		return getArray(set.toArray(new String[0]));
    }
	/**
	 * String配列をSQLパラメータ代入用のArrayにして返却する。
	 * @param set 変換対象配列
	 * @return Array化されたString配列
	 */
	public Array getDecimalArray(Set<BigDecimal> set){
		return getDecimalArray(set.toArray(new BigDecimal[0]));
    }
}
