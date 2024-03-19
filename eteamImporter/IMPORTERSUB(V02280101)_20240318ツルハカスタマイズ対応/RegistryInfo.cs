
namespace IMPORTERSUB
{
    public class RegistryInfo
    {
        public bool IsLocalDbServer
		{
			// 1 = スタンドアロン, 3 = DBサーバー, 5 = DB/RDS同居
			get { return this.Type == "1" || this.Type == "3" || this.Type == "5"; }
		}

		public string Type		 { get; set; }
		public string ServerName { get; set; }
		public string ServerPort { get; set; }
    }
}
