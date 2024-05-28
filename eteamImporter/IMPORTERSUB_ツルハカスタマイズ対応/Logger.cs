using System;
using System.IO;
using System.Text;

namespace IMPORTERSUB
{
    internal class Logger
    {
        private static readonly string       mDateTimeFormat   = "yyyy/MM/dd HH:mm:ss.fff ";
        //--- 2021/02/09 追加
        private static          string       mDateFormat = "yyyyMMddHHmmssfff";

        private static          string       mFilePath         = "";
        private static          StreamWriter streamWriter      = null;
        private static          Encoding     encoding          = null;
        private static          Logger       singletonInstance = null;

        public enum Level
        {
            INFO,
            OK,
            NG,
            ERROR
        }

        private Logger() { }

        public static Logger GetInstance(string filePath)
        {
            if (singletonInstance == null)
            {
                singletonInstance = new Logger();
                // 文字コード指定
                encoding = Encoding.GetEncoding("Shift_JIS");
                // ファイルを開く
                // 2021/02/09 変更
                //---mFilePath = @"" + filePath + @"\IMPORTERSUB.LOG";
                //---streamWriter = new StreamWriter(mFilePath, false, encoding);
                mFilePath = @"" + filePath + @"\IMPORTERSUB" + @"_" + DateTime.Now.ToString(mDateFormat) + @".LOG";
                streamWriter = new StreamWriter(mFilePath, false, encoding);
            }
            return Logger.singletonInstance;
        }

        public void LogWrite(Level level, string logText)
        {
            // ログテキスト内容
            string   sLogContent = string.Format("{0} {1} {2}", DateTime.Now.ToString(mDateTimeFormat), (level.ToString()).PadRight(6 - (Encoding.GetEncoding("Shift_JIS").GetByteCount(level.ToString()) - level.ToString().Length)), logText);

            // 開いていない場合、フィルを開く
            if (streamWriter.BaseStream == null)
            {
                //---
                //---streamWriter = new StreamWriter(mFilePath, false, encoding);
                //streamWriter = new StreamWriter(mFilePath, false, encoding);
                streamWriter = new StreamWriter(mFilePath, true, encoding);
            }

            try
            {
                // テキストの書き込み
                streamWriter.WriteLine(sLogContent);
            }
            catch
            {
                LogClose();
            }
        }

        public void LogClose()
        {
            streamWriter.Close();
        }
    }
}
