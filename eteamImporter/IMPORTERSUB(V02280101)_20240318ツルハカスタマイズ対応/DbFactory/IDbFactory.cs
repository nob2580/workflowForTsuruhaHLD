using System.Data;
using System.Data.Common;

namespace IMPORTERSUB.DbFactory
{
    internal interface IDbFactory
	{
		IDbConnection CreateAndOpenDbConnection(string databaseName, int userCode);

		IDbDataAdapter CreateDbDataAdapter();

		IDbDataAdapter CreateDbDataAdapterAndSelectCommand(IDbConnection connection, string commandText);

		bool IsLockTimeoutError(DbException exception);
	}
}
