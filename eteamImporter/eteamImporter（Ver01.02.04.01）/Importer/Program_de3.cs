using Importer.Util;
using IMPORTERSUB;
using System;
using System.Collections.Generic;
using System.IO;

namespace Importer
{
    class Program
    {
        #region Const
        private const int IMP_SUBSIWAKE_M = 1;
        private const int IMP_SUBSIWAKE_K_M = 2;
        #endregion

        #region Enum
        private enum ReturnCode
        {
            /// <summary>
            /// システム例外
            /// </summary>
            SystemError = -999,
            /// <summary>
            /// 処理モードエラー
            /// </summary>
            ProcModeError = -100,
            /// <summary>
            /// 起動引数数エラー
            /// </summary>
            ArgsNumError = -101,
            /// <summary>
            /// 引数変換エラー
            /// </summary>
            ArgsConvertError = -102,
            /// <summary>
            /// ファイル存在エラー
            /// </summary>
            FileNotExistError = -103,
            /// <summary>
            /// CSV読み込みエラー
            /// </summary>
            CsvReadError = -104,
            /// <summary>
            /// データなしエラー
            /// </summary>
            NoDataError = -105,
            /// <summary>
            /// データ個数エラー
            /// </summary>
            DataNumError = -106,
            /// <summary>
            /// データ変換エラー
            /// </summary>
            DataConvertError = -107,
            /// <summary>
            /// 呼び出しインポータ相違エラー
            /// </summary>
            CallImporterError = -108
        }
        #endregion

        #region Struct
        private class ImportParam
        {
            public string    CCOD;          // 会社コード
            public int       PrcFlg;        // 処理区分(0：データチェック＆取込・1：データチェックのみ)
            public int       DFUK;          // 伝票形式(0：単一形式・1：複合形式(手入力諸口)・2：複合形式(自動諸口))
            public int       LogFlg;        // 不良データログ(0：ログ不要・1：ログ要・11：ログ要＆不良データがあった場合取り込まない)
            public C_SetData Siwake;        // メモリークラス（仕訳構造体）
            public string    LogPath;       // ログ用ファイルパス
            public string    LogFname;      // ログ用ファイル名称    ※ 拡張子は「.LOG」固定
            public int       RNo;           // レイアウトNo(0：de3レイアウト・1：SIASレイアウト(インボイス非対応)・2：SIASインボイス対応レイアウト)
            public int       RUCOD;         // 起動ユーザー
            public int       SKUBUN;        // 仕訳区分(21：人事給与・41：ワークフロー・51：BtoB連携・61：AT-OCR)
            public int       IJPT = 0;      // 伝票入力パターン
            public int       Kanzan = 0;    // 邦貨換算フラグ(0：外貨から邦貨換算しない・1：外貨から邦貨換算する)
            public string    Kakutei = "";  // 入力確定日            ※ 部署入出力処理用（フォーマット：YYYY/MM/DD）
            public int       Keigen;        // 税率の扱い(0：税率8%指定の場合通常税率8%・1：税率8％指定の場合軽減税率8%・2：軽減税率区分の値で判別)
        }
        #endregion

        #region Main
        /// <summary>
        /// メインメソッド
        /// </summary>
        /// <param name="args">
        /// 処理モード
        /// 会社コード
        /// 処理区分
        /// 伝票形式
        /// 不良データログ
        /// CSVパス
        /// CSVファイル名
        /// ログ用パス
        /// ログファイル名
        /// レイアウトNo
        /// 起動ユーザー
        /// 仕訳区分
        /// 伝票入力パターン
        /// 邦貨換算フラグ
        /// 入力確定日
        /// 税率の扱い
        /// </param>
        /// <returns>リターンコード（個別エラー番号）</returns>
        /// 2015-04-15 m-naka .net3.5にプロジェクト設定変更。参照設定から4.5固有名前空間を削除し、伴ってusingを一部削除。デバッグ出力を追加。
        static int Main(string[] args)
        {
            trace("Importer_de3起動しました。 param = " + String.Join(", ", args) + ". プロセスタイプ = " + (IntPtr.Size == 4 ? "32bit" : "64bit"));
            int rtn = 0;

            Import      imp    = null;
            ImportParam impPrm = null;

            try
            {
                if (args.Length != 16)
                {
                    int errCd = (int)ReturnCode.ArgsNumError;
                    throw new Exception(errCd.ToString());
                }
                int procMode = StringUtil.StringToInt(args[0]);

                imp    = new Import();
                impPrm = GetImportParam(args);

                if (procMode == IMP_SUBSIWAKE_M)
                {
                    #region IMP_SUBSIWAKE_M
                    trace("IMPORTER呼び出し(SIWAKE_M) = "
                        + impPrm.CCOD + ", "
                        + impPrm.PrcFlg + ", "
                        + impPrm.DFUK + ", "
                        + impPrm.LogFlg + ", "
                        + impPrm.Siwake + ", "
                        + impPrm.LogPath + ", "
                        + impPrm.LogFname + ", "
                        + impPrm.RNo + ", "
                        + impPrm.RUCOD + ", "
                        + impPrm.SKUBUN + ", "
                        + impPrm.IJPT + ", "
                        + impPrm.Kanzan + ", "
                        + impPrm.Keigen);
                    rtn = imp.Imp_SubSiwake_M(
                        impPrm.CCOD,
                        impPrm.PrcFlg,
                        impPrm.DFUK,
                        impPrm.LogFlg,
                        ref impPrm.Siwake,
                        impPrm.LogPath,
                        impPrm.LogFname,
                        impPrm.RNo,
                        impPrm.RUCOD,
                        impPrm.SKUBUN,
                        impPrm.IJPT,
                        impPrm.Kanzan,
                        impPrm.Keigen);
                    #endregion
                }
                else if (procMode == IMP_SUBSIWAKE_K_M)
                {
                    #region IMP_SUBSIWAKE_K_M
                    trace("IMPORTER呼び出し(SIWAKE_K_M)"
                        + impPrm.CCOD + ", "
                        + impPrm.PrcFlg + ", "
                        + impPrm.DFUK + ", "
                        + impPrm.LogFlg + ", "
                        + impPrm.Siwake + ", "
                        + impPrm.LogPath + ", "
                        + impPrm.LogFname + ", "
                        + impPrm.RNo + ", "
                        + impPrm.RUCOD + ", "
                        + impPrm.SKUBUN + ", "
                        + impPrm.IJPT + ", "
                        + impPrm.Kanzan + ", "
                        + impPrm.Kakutei + ", "
                        + impPrm.Keigen);
                    rtn = imp.Imp_SubSiwake_K_M(
                        impPrm.CCOD,
                        impPrm.PrcFlg,
                        impPrm.DFUK,
                        impPrm.LogFlg,
                        ref impPrm.Siwake,
                        impPrm.LogPath,
                        impPrm.LogFname,
                        impPrm.RNo,
                        impPrm.RUCOD,
                        impPrm.SKUBUN,
                        impPrm.IJPT,
                        impPrm.Kanzan,
                        impPrm.Kakutei,
                        impPrm.Keigen);
                    #endregion
                }
                else
                {
                    int errCd = (int)ReturnCode.ProcModeError;
                    throw new Exception(errCd.ToString());
                }
            }
            catch (Exception ex)
            {
                trace("例外を検知しました。" + ex.ToString());
                if (CheckUtil.IsNumeric(ex.Message))
                {
                    rtn = Convert.ToInt32(ex.Message);
                }
                else
                {
                    rtn = (int)ReturnCode.SystemError;

                    #region エラーログを出力する
                    string fname = string.Empty;
                    string path = string.Empty;

                    if (impPrm != null)
                    {
                        fname = Path.GetFileNameWithoutExtension(impPrm.LogFname);
                        path = impPrm.LogPath;
                    }

                    fname += "Error.log";

                    if (path.Length == 0)
                    {
                        path = Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location);
                    }

                    string filePatch = System.IO.Path.Combine(path, fname);
                    string text = string.Format("{0}{1}{2}{3}{4}{5}", DateTime.Now, Environment.NewLine, ex.Message, Environment.NewLine, ex.StackTrace, Environment.NewLine);

                    // ファイル作成
                    FileUtil.CreateFile(filePatch, text, System.Text.Encoding.GetEncoding("shift_jis"));
                    #endregion
                }
            }
            if (rtn >= 1) rtn = 1;
            trace("Importer終了しました。rtn = " + rtn);
            return rtn;
        }
        #endregion

        #region Private Method

        /// <summary>
        /// トレース用出力を行います。
        /// </summary>
        /// <param name="msg">出力メッセージ</param>
        private static void trace(string msg)
        {
            string tracemsg = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.fff") + " " + msg;
            System.Diagnostics.Trace.WriteLine(tracemsg);
        }

        #region GetImportParam インポートパラメータの取得
        /// <summary>
        /// インポートパラメータの取得
        /// </summary>
        /// <param name="args">コマンドライン引数</param>
        /// <returns>インポートパラメータ</returns>
        private static ImportParam GetImportParam(string[] args)
        {
            ImportParam impParam = new ImportParam();

            C_SetData siwake = GetSiwake(args[5], args[6], args[9]);

            try
            {
                impParam.CCOD     = args[1];
                impParam.PrcFlg   = Convert.ToInt32(args[2]);
                impParam.DFUK     = Convert.ToInt32(args[3]);
                impParam.LogFlg   = Convert.ToInt32(args[4]);
                impParam.Siwake   = siwake;
                impParam.LogPath  = args[7];
                impParam.LogFname = args[8];
                impParam.RNo      = Convert.ToInt32(args[9]);
                impParam.RUCOD    = Convert.ToInt32(args[10]);
                impParam.SKUBUN   = Convert.ToInt32(args[11]);
                impParam.IJPT     = Convert.ToInt32(args[12]);
                impParam.Kanzan   = Convert.ToInt32(args[13]);

                if (args[14] == "0")
                {
                    impParam.Kakutei = string.Empty;
                }
                else
                {
                    impParam.Kakutei = args[14];
                }

                impParam.Keigen = Convert.ToInt32(args[15]);
            }
            catch
            {
                int errCd = (int)ReturnCode.ArgsConvertError;
                throw new Exception(errCd.ToString());
            }

            return impParam;
        }
        #endregion

        #region GetSiwake 仕訳データの取得
        /// <summary>
        /// 仕訳データの取得
        /// </summary>
        /// <param name="path">CSVファイルパス</param>
        /// <param name="fname">CSVファイル名</param>
        /// <returns>仕訳データ</returns>
        private static C_SetData GetSiwake(string path, string fname, string RNo)
        {
            C_SetData data    = new C_SetData();
            C_Siwake  shiwake = new C_Siwake();

            string filePatch = System.IO.Path.Combine(path, fname);

            if (System.IO.File.Exists(filePatch))
            {
                List<string[]> rtn = ReadCsvFile(filePatch);

                if (rtn.Count == 0)
                {
                    // データなしエラー
                    int errCd = (int)ReturnCode.NoDataError;
                    throw new Exception(errCd.ToString());
                }

                // Ver01.02.01 インボイス対応
                //if (RNo.Trim() != "0")
                if (RNo.Trim() != "0" && RNo.Trim() != "1")
                {
                    //Rnoが 0 または 1 以外
                    int errCd = (int)ReturnCode.CallImporterError;
                    throw new Exception(errCd.ToString());
                }


                foreach (string[] row in rtn)
                {
                    // Ver01.02.01 インボイス対応
                    //if (row.Length != 64)
                    if ((RNo.Trim() == "0" && row.Length != 64) || 
                        (RNo.Trim() == "1" && row.Length != 70))
                    {
                        // データ個数エラー
                        int errCd = (int)ReturnCode.DataNumError;
                        throw new Exception(errCd.ToString());
                    }

                    try
                    {
                        //de3レイアウト
                        shiwake.Clear();
                        shiwake.DYMD  = Convert.ToInt32(row[0]);
                        shiwake.SEIRI = Convert.ToInt16(row[1]);
                        shiwake.DCNO  = row[2];
                        shiwake.RBMN  = row[3];
                        shiwake.RTOR  = row[4];
                        shiwake.RKMK  = row[5];
                        shiwake.REDA  = row[6];
                        shiwake.RKOJ  = row[7];
                        shiwake.RKOS  = row[8];
                        shiwake.RPRJ  = row[9];
                        shiwake.RSEG  = row[10];
                        shiwake.RDM1  = row[11];
                        shiwake.RDM2  = row[12];
                        shiwake.RDM3  = row[13];
                        shiwake.TKY   = row[14];
                        shiwake.TNO   = row[15];
                        shiwake.SBMN  = row[16];
                        shiwake.STOR  = row[17];
                        shiwake.SKMK  = row[18];
                        shiwake.SEDA  = row[19];
                        shiwake.SKOJ  = row[20];
                        shiwake.SKOS  = row[21];
                        shiwake.SPRJ  = row[22];
                        shiwake.SSEG  = row[23];
                        shiwake.SDM1  = row[24];
                        shiwake.SDM2  = row[25];
                        shiwake.SDM3  = row[26];
                        shiwake.EXVL  = Convert.ToDouble(row[27]);
                        shiwake.VALU  = Convert.ToDouble(row[28]);
                        shiwake.ZKMK  = row[29];
                        shiwake.ZRIT  = row[30];
                        shiwake.ZZKB  = row[31];
                        shiwake.ZGYO  = row[32];

                        // 2022/11/11 Ver01.01.07   0 → "" 対応
                        //shiwake.ZSRE = row[33];
                        shiwake.ZSRE  = row[33] == "0" ? shiwake.ZSRE = "" : shiwake.ZSRE = row[33]; 

                        shiwake.RRIT  = row[34];
                        shiwake.SRIT  = row[35];
                        shiwake.RZKB  = row[36];
                        shiwake.RGYO  = row[37];

                        // 2022/11/11 Ver01.01.07   0 → "" 対応
                        //shiwake.RSRE = row[38];
                        shiwake.RSRE  = row[38] == "0" ? shiwake.RSRE = "" : shiwake.RSRE = row[38];

                        shiwake.SZKB  = row[39];
                        shiwake.SGYO  = row[40];

                        // 2022/11/11 Ver01.01.07   0 → "" 対応
                        //shiwake.SSRE = row[41];
                        shiwake.SSRE  = row[41] == "0" ? shiwake.SSRE = "" : shiwake.SSRE = row[41];

                        shiwake.SYMD  = Convert.ToInt32(row[42]);
                        shiwake.SKBN  = row[43];
                        shiwake.SKIZ  = Convert.ToInt32(row[44]);
                        shiwake.UYMD  = Convert.ToInt32(row[45]);
                        shiwake.UKBN  = row[46];
                        shiwake.UKIZ  = Convert.ToInt32(row[47]);
                        shiwake.STEN  = Convert.ToInt16(row[48]);
                        shiwake.DKEC  = row[49];
                        shiwake.KYMD  = Convert.ToInt32(row[50]);
                        shiwake.KBMN  = row[51];
                        shiwake.KUSR  = row[52];
                        shiwake.FUSR  = row[53];
                        shiwake.FSEN  = Convert.ToInt16(row[54]);
                        shiwake.SGNO  = Convert.ToInt32(row[55]);
                        shiwake.BUNRI = row[56];
                        shiwake.RATE  = row[57];
                        shiwake.GEXVL = row[58];
                        shiwake.GVALU = row[59];
                        // 2023/11/14 Ver01.02.04 半角スペース対応 *-
                        if (!String.IsNullOrEmpty(row[60].Trim()))
                        {
                            shiwake.RKEIGEN = Convert.ToInt16(row[60]);
                        }
                        if (!String.IsNullOrEmpty(row[61].Trim()))
                        {
                            shiwake.SKEIGEN = Convert.ToInt16(row[61]);
                        }
                        if (!String.IsNullOrEmpty(row[62].Trim()))
                        {
                            shiwake.ZKEIGEN = Convert.ToInt16(row[62]);
                        }
                        shiwake.GSEP = row[63];

                        // Ver01.02.01 インボイス対応 *-
                        if (RNo.Trim() == "1" && shiwake.DYMD > 20230930)
                        {
                            if (!String.IsNullOrEmpty(row[64].Trim()))
                            {
                                shiwake.RURIZEIKEISAN = Convert.ToInt16(row[64]);
                            }
                            if (!String.IsNullOrEmpty(row[65].Trim()))
                            {
                                shiwake.SURIZEIKEISAN = Convert.ToInt16(row[65]);
                            }
                            if (!String.IsNullOrEmpty(row[66].Trim()))
                            {
                                shiwake.ZURIZEIKEISAN = Convert.ToInt16(row[66]);
                            }
                            if (!String.IsNullOrEmpty(row[67].Trim()))
                            {
                                shiwake.RMENZEIKEIKA = Convert.ToInt16(row[67]);
                            }
                            if (!String.IsNullOrEmpty(row[68].Trim()))
                            {
                                shiwake.SMENZEIKEIKA = Convert.ToInt16(row[68]);
                            }
                            if (!String.IsNullOrEmpty(row[69].Trim()))
                            {
                                shiwake.ZMENZEIKEIKA = Convert.ToInt16(row[69]);
                            }
                        }
                        // -* 2023/11/14 Ver01.02.04 半角スペース対応
                        else
                        {
                            shiwake.RURIZEIKEISAN = 0;
                            shiwake.SURIZEIKEISAN = 0;
                            shiwake.ZURIZEIKEISAN = 0;
                            shiwake.RMENZEIKEIKA  = 0;
                            shiwake.SMENZEIKEIKA  = 0;
                            shiwake.ZMENZEIKEIKA  = 0;
                        }
                        // -*
                    }
                    catch
                    {
                        // データ変換エラー
                        int errCd = (int)ReturnCode.DataConvertError;
                        throw new Exception(errCd.ToString());
                    }

                    data.SetSiwake(ref shiwake);
                }
            }
            else
            {
                // ファイルなし
                int errCd = (int)ReturnCode.FileNotExistError;
                throw new Exception(errCd.ToString());
            }

            return data;
        }
        #endregion

        #region ReadCsvFile CSVファイルの読み込み
        /// <summary>
        /// CSVファイルの読み込み
        /// </summary>
        /// <param name="filePatch">ファイルパス</param>
        /// <returns>CSVデータ</returns>
        private static List<string[]> ReadCsvFile(string filePatch)
        {
            List<string[]> rtn = new List<string[]>();

            try
            {
                // csvファイルを開く
                using (var sr = new System.IO.StreamReader(filePatch, System.Text.Encoding.GetEncoding("shift_jis")))
                {
                    // ストリームの末尾まで繰り返す
                    while (!sr.EndOfStream)
                    {
                        // ファイルから一行読み込む
                        var line = sr.ReadLine();

                        // 読み込んだ一行をカンマ毎に分けて配列に格納する
                        string[] values = line.Split('\t');

                        rtn.Add(values);
                    }
                }
            }
            catch
            {
                // CSV読み込みエラー
                int errCd = (int)ReturnCode.CsvReadError;
                throw new Exception(errCd.ToString());
            }

            return rtn;
        }
        #endregion

        #endregion
    }
}
