using System.Data;
using System.Data.Common;
using System.Data.SqlClient;

namespace IMPORTERSUB.DbFactory
{
    internal class DbFactoryForSqlServer : AbstractDbFactory
	{	
		public DbFactoryForSqlServer(RegistryInfo registryInfo) : base(registryInfo)
		{
		}
	
		protected override void SetUserIdToConnectionInfo(IDbConnection connection, int userCode)
		{
			//if (string.IsNullOrEmpty(userCode.ToString()))
			//{
			//	return;
			//}

			string	   commandText = null;
			IDbCommand command	   = connection.CreateCommand();
			commandText			   = string.Format("SET CONTEXT_INFO {0}", userCode.ToString());
			command.CommandText    = commandText;
			command.ExecuteNonQuery();
		}
	
		public override IDbDataAdapter CreateDbDataAdapter()
		{
			return new SqlDataAdapter();
		}
	
		public override bool IsLockTimeoutError(DbException exception)
		{
			SqlException ex = (SqlException)exception;
			return ex.Number == 1222;
		}
	
		protected override IDbConnection CreateDbConnectionWithConnectionString(string databaseName)
		{
			return new SqlConnection(this.CreateConnectionString(databaseName));
		}
	
		protected override void SetLockTimeOut(IDbConnection connection)
		{
			//---> 2021/02/15
			//---string	   commandText = "SET LOCK_TIMEOUT 1000 ";
			string commandText	   = "SET LOCK_TIMEOUT 3000 ";
			IDbCommand command	   = connection.CreateCommand();
			command.CommandText	   = commandText;
			command.ExecuteNonQuery();
		}
	
		private string CreateConnectionString(string databaseName)
		{
			databaseName = databaseName.ToUpper();
			string user  = "ICSP_312IUSER";
			string pass  = this.GetPassWord();

			if (this.RegistryInfo.IsLocalDbServer)
			{
				return "Data Source = localhost\\ICSP;Initial Catalog = " + databaseName + ";Integrated Security = FALSE;User Id=" + user + ";Password=" + pass + ";";
			}
			else
			{
				string port = this.RegistryInfo.ServerPort == "1434" ? string.Empty : "," + RegistryInfo.ServerPort;
				return "Data Source = " + this.RegistryInfo.ServerName + "\\ICSP" + port + ";Initial Catalog = " + databaseName + ";Integrated Security = FALSE;User Id=" + user + ";Password=" + pass + ";";
			}
		}
	}
}
