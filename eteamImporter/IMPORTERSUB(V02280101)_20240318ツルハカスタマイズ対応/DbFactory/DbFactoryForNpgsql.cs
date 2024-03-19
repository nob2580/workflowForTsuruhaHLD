using System.Data;
using System.Data.Common;
using Npgsql;

namespace IMPORTERSUB.DbFactory
{
    internal class DbFactoryForNpgsql : AbstractDbFactory
	{
	
		public DbFactoryForNpgsql(RegistryInfo registryInfo) : base(registryInfo)
		{
		}
	
		protected override void SetUserIdToConnectionInfo(IDbConnection connection, int userCode)
		{
		}
	
		public override IDbDataAdapter CreateDbDataAdapter()
		{
			return new NpgsqlDataAdapter();
		}
	
		public override bool IsLockTimeoutError(DbException exception)
		{
			NpgsqlException ex = (NpgsqlException)exception;
			return ex.Message == "55P03";  // ロックは使用できません
		}
	
		protected override IDbConnection CreateDbConnectionWithConnectionString(string databaseName)
		{
			return new NpgsqlConnection(this.CreateConnectionString(databaseName));
		}
	
		protected override void SetLockTimeOut(IDbConnection connection)
		{
			string     commandText = "SET LOCK_TIMEOUT = 30000 ";
			IDbCommand command     = connection.CreateCommand();
			command.CommandText    = commandText;
			command.ExecuteNonQuery();
		}
	
		private string CreateConnectionString(string databaseName)
		{
			databaseName = databaseName.ToLower();
			string user  = "icsp_312iuser";
			string pass  = this.GetPassWord();
			return "Server = " + this.RegistryInfo.ServerName + "; DataBase = " + databaseName + "; User ID = " + user + "; Password = " + pass + "; Port = " + this.RegistryInfo.ServerPort + "; Pooling = False;";
		}
	}
}
