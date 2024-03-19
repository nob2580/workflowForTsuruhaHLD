using System.Data;
using System.Data.Common;

using IMPORTERSUB;

namespace IMPORTERSUB.DbFactory
{
	internal abstract class AbstractDbFactory : IDbFactory
	{
		public AbstractDbFactory(RegistryInfo registryInfo)
		{
			this.RegistryInfo = registryInfo;
		}

		protected RegistryInfo RegistryInfo { get; set; }

		public IDbConnection CreateAndOpenDbConnection(string databaseName, int userCode)
		{
			IDbConnection connection = this.CreateDbConnectionWithConnectionString(databaseName);
			connection.Open();
			this.SetLockTimeOut(connection);
			this.SetUserIdToConnectionInfo(connection, userCode);
			return connection;
		}

		public IDbDataAdapter CreateDbDataAdapterAndSelectCommand(IDbConnection connection, string commandText)
		{
			IDbDataAdapter adapter = this.CreateDbDataAdapter();
			adapter.SelectCommand = connection.CreateCommand();
			adapter.SelectCommand.CommandText = commandText;
			return adapter;
		}

		public abstract bool IsLockTimeoutError(DbException exception);

		public abstract IDbDataAdapter CreateDbDataAdapter();

		protected abstract IDbConnection CreateDbConnectionWithConnectionString(string databaseName);

		protected abstract void SetUserIdToConnectionInfo(IDbConnection connection, int userCode);

		protected abstract void SetLockTimeOut(IDbConnection connection);

		protected string GetPassWord()
		{
			string s1 = DB_DmyStr(1);
			string s2 = DB_DmyStr(2);
			string s3 = DB_DmyStr(3);
			string s4 = DB_DmyStr(4);
			string pass = "";
			pass += s1.Substring( 8, 1);
			pass += s1.Substring( 2, 1);
			pass += s1.Substring(18, 1);
			pass += s3.Substring( 8, 1);
			pass += s1.Substring(18, 1);
			pass += s1.Substring(19, 1);
			pass += s1.Substring( 3, 1);
			pass += s3.Substring( 8, 1);
			pass += s1.Substring(18, 1);
			pass += s1.Substring( 8, 1);
			pass += s1.Substring( 0, 1);
			pass += s1.Substring(18, 1);

			return pass;
		}

		private string DB_DmyStr(int no)
		{
			if (no == 1)
			{
				return "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			}
			if (no == 2)
			{
				return "abcdefghijklmnopqrstuvwxyz";
			}
			if (no == 3)
			{
				return "!#$%&:()_~|+-*-.;@, ";
			}
			if (no == 4)
			{
				return "1234567890";
			}
			return "";
		}
	}
}
