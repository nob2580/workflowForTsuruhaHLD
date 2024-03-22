using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Win32;

namespace auth
{
    class auth
    {
        static void Main(string[] args)
        {
            if (args.Length != 2) return;

            string val = @"c:\eteam\3161pdf";

            String[] strX;
            strX = new String[6]{
                "Def",
                "Et",
                "auth",
                "Pwd719",
                "WorkFlow",
                "2015" };
            if (args[0] == strX[1] && args[1] == strX[3])
            {
                String str = EDOCUMENTLIB.Atl.GetString(val);
                String str2 = EDOCUMENTLIB.Atl.ErrorMessage;
                System.Console.WriteLine(str);
            }
        }
    }
}
