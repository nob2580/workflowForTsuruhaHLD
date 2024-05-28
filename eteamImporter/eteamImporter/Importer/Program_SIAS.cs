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
        private const int    IMP_SUBSIWAKE_M       = 1;
        private const int    IMP_SUBSIWAKE_K_M     = 2;
        private const string IMP_APPENDFILE_FOLDER = "ImpFile_";    // 添付ファイル格納先フォルダ名前半部
        private const string IMP_LINKDATA_CSV      = "linkData_";   // リンク情報格納ファイル名前半部
        private const string EXTENSION_CSV         = "csv";         // 拡張子CSV
        private const string EXTENSION_PDF         = "pdf";         // 拡張子PDF
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
            CallImporterError = -108,
            /// <summary>
            /// 伝票添付ファイル存在エラー
            /// </summary>
            ImpFolderExistsError = -109,
            /// <summary>
            /// リンク情報CSVファイル存在エラー
            /// </summary>
            LinkCsvExistsError = -110
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
// ▼ ツルハ様カスタマイズ ▼
            public string    SYBMNCD = "";  // 消費税科目固定部門コード
// ▲ ツルハ様カスタマイズ ▲
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
            trace("Importer_SIAS起動しました。 param = " + String.Join(", ", args) + ". プロセスタイプ = " + (IntPtr.Size == 4 ? "32bit" : "64bit"));
            int rtn = 0;

            Import      imp    = null;
            ImportParam impPrm = null;

            try
            {
// ▼ ツルハ様カスタマイズ ▼
//              if (args.Length != 16)
                if (args.Length != 17)
// ▲ ツルハ様カスタマイズ ▲
                {
                    int errCd = (int)ReturnCode.ArgsNumError;
                    throw new Exception(errCd.ToString());
                }
                int procMode = StringUtil.StringToInt(args[0]);

                imp    = new Import();
                impPrm = GetImportParam(args);

                // 日次処理への転記
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
// ▼ ツルハ様カスタマイズ ▼
//                      + impPrm.Keigen);
                        + impPrm.Keigen + ", "
                        + impPrm.SYBMNCD);
// ▲ ツルハ様カスタマイズ ▲

                    rtn = imp.Imp_SubSiwake_M(
                        impPrm.CCOD,
                        impPrm.PrcFlg,
                        impPrm.DFUK,
                        impPrm.LogFlg,
                        ref    impPrm.Siwake,
                        impPrm.LogPath,
                        impPrm.LogFname,
                        impPrm.RNo,
                        impPrm.RUCOD,
                        impPrm.SKUBUN,
                        impPrm.IJPT,
                        impPrm.Kanzan,
// ▼ ツルハ様カスタマイズ ▼
//                      impPrm.Keigen);
                        impPrm.Keigen,
                        impPrm.SYBMNCD);
// ▲ ツルハ様カスタマイズ ▲
                    #endregion
                }
                // 部署入出力処理への転記
                else if (procMode == IMP_SUBSIWAKE_K_M)
                {
                    #region IMP_SUBSIWAKE_K_M
                    trace("IMPORTER呼び出し(SIWAKE_K_M)"
                        + impPrm.CCOD     + ", "
                        + impPrm.PrcFlg   + ", "
                        + impPrm.DFUK     + ", "
                        + impPrm.LogFlg   + ", "
                        + impPrm.Siwake   + ", "
                        + impPrm.LogPath  + ", "
                        + impPrm.LogFname + ", "
                        + impPrm.RNo      + ", "
                        + impPrm.RUCOD    + ", "
                        + impPrm.SKUBUN   + ", "
                        + impPrm.IJPT     + ", "
                        + impPrm.Kanzan   + ", "
                        + impPrm.Kakutei  + ", "
// ▼ ツルハ様カスタマイズ ▼
//                      + impPrm.Keigen);
                        + impPrm.Keigen + ", "
                        + impPrm.SYBMNCD);
// ▲ ツルハ様カスタマイズ ▲
                    rtn = imp.Imp_SubSiwake_K_M(
                        impPrm.CCOD,
                        impPrm.PrcFlg,
                        impPrm.DFUK,
                        impPrm.LogFlg,
                        ref    impPrm.Siwake,
                        impPrm.LogPath,
                        impPrm.LogFname,
                        impPrm.RNo,
                        impPrm.RUCOD,
                        impPrm.SKUBUN,
                        impPrm.IJPT,
                        impPrm.Kanzan,
                        impPrm.Kakutei,
// ▼ ツルハ様カスタマイズ ▼
//                      impPrm.Keigen);
                        impPrm.Keigen,
                        impPrm.SYBMNCD);
// ▲ ツルハ様カスタマイズ ▲
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
                    string fname = String.Empty;
                    string path  = String.Empty;

                    if (impPrm != null)
                    {
                        fname = Path.GetFileNameWithoutExtension(impPrm.LogFname);
                        path  = impPrm.LogPath;
                    }

                    fname += "Error.log";

                    if (path.Length == 0)
                    {
                        path = Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location);
                    }

                    string filePatch = System.IO.Path.Combine(path, fname);
                    string text      = String.Format("{0}{1}{2}{3}{4}{5}", DateTime.Now, Environment.NewLine, ex.Message, Environment.NewLine, ex.StackTrace, Environment.NewLine);

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
                    impParam.Kakutei = String.Empty;
                }
                else
                {
                    impParam.Kakutei = args[14];
                }

                impParam.Keigen = Convert.ToInt32(args[15]);
// ▼ ツルハ様カスタマイズ ▼
　              impParam.SYBMNCD = args[16];
// ▲ ツルハ様カスタマイズ ▲
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
// Ver01.02.01 インボイス対応(レイアウトNo.2追加) *-
//              if (RNo.Trim() != "1")
                if (!(RNo.Trim() == "1" || RNo.Trim() == "2"))
// -*
                {
                    //Rnoが 1または2 以外
                    int errCd = (int)ReturnCode.CallImporterError;
                    throw new Exception(errCd.ToString());
                }

                foreach (string[] row in rtn)
                {
                    if ((RNo.Trim() == "1" && row.Length != 112) || 
                        (RNo.Trim() == "2" && row.Length != 118)) {
                        // データ個数エラー
                        int errCd = (int)ReturnCode.DataNumError;
                        throw new Exception(errCd.ToString());
                    }

                    try
                    {
                        //SIASレイアウト向け
                        //現在は仕様書記載の順序に合わせている
                        shiwake.Clear();
                        shiwake.DYMD  = Convert.ToInt32(row[0]);
                        shiwake.SEIRI = Convert.ToInt16(row[1]);
                        shiwake.DCNO  = row[2];
                        shiwake.KYMD  = Convert.ToInt32(row[3]);
                        shiwake.KBMN  = row[4];
                        shiwake.KUSR  = row[5];
                        shiwake.SGNO  = Convert.ToInt32(row[6]);
                        shiwake.HF1   = row[7];
                        shiwake.HF2   = row[8];
                        shiwake.HF3   = row[9];
                        shiwake.HF4   = row[10];
                        shiwake.HF5   = row[11];
                        shiwake.HF6   = row[12];
                        shiwake.HF7   = row[13];
                        shiwake.HF8   = row[14];
                        shiwake.HF9   = row[15];
                        shiwake.HF10  = row[16];
                        shiwake.RBMN  = row[17];
                        shiwake.RTOR  = row[18];
                        shiwake.RKMK  = row[19];
                        shiwake.REDA  = row[20];
                        shiwake.RKOJ  = row[21];
                        shiwake.RKOS  = row[22];
                        shiwake.RPRJ  = row[23];
                        shiwake.RSEG  = row[24];
                        shiwake.RDM1  = row[25];
                        shiwake.RDM2  = row[26];
                        shiwake.RDM3  = row[27];
                        shiwake.RDM4  = row[28];
                        shiwake.RDM5  = row[29];
                        shiwake.RDM6  = row[30];
                        shiwake.RDM7  = row[31];
                        shiwake.RDM8  = row[32];
                        shiwake.RDM9  = row[33];
                        shiwake.RDM10 = row[34];
                        shiwake.RDM11 = row[35];
                        shiwake.RDM12 = row[36];
                        shiwake.RDM13 = row[37];
                        shiwake.RDM14 = row[38];
                        shiwake.RDM15 = row[39];
                        shiwake.RDM16 = row[40];
                        shiwake.RDM17 = row[41];
                        shiwake.RDM18 = row[42];
                        shiwake.RDM19 = row[43];
                        shiwake.RDM20 = row[44];
                        shiwake.RRIT  = row[45];
                        shiwake.RZKB  = row[46];
                        shiwake.RGYO  = row[47];

                        // 2022/11/11 Ver02.25.02 0 → " 対応
                        //shiwake.RSRE  = row[48];
                        shiwake.RSRE  = row[48] == "0" ? shiwake.RSRE = "" : shiwake.RSRE = row[48];

                        shiwake.RTKY  = row[49];
                        shiwake.RTNO  = row[50];
                        shiwake.SBMN  = row[51];
                        shiwake.STOR  = row[52];
                        shiwake.SKMK  = row[53];
                        shiwake.SEDA  = row[54];
                        shiwake.SKOJ  = row[55];
                        shiwake.SKOS  = row[56];
                        shiwake.SPRJ  = row[57];
                        shiwake.SSEG  = row[58];
                        shiwake.SDM1  = row[59];
                        shiwake.SDM2  = row[60];
                        shiwake.SDM3  = row[61];
                        shiwake.SDM4  = row[62];
                        shiwake.SDM5  = row[63];
                        shiwake.SDM6  = row[64];
                        shiwake.SDM7  = row[65];
                        shiwake.SDM8  = row[66];
                        shiwake.SDM9  = row[67];
                        shiwake.SDM10 = row[68];
                        shiwake.SDM11 = row[69];
                        shiwake.SDM12 = row[70];
                        shiwake.SDM13 = row[71];
                        shiwake.SDM14 = row[72];
                        shiwake.SDM15 = row[73];
                        shiwake.SDM16 = row[74];
                        shiwake.SDM17 = row[75];
                        shiwake.SDM18 = row[76];
                        shiwake.SDM19 = row[77];
                        shiwake.SDM20 = row[78];
                        shiwake.SRIT  = row[79];
                        shiwake.SZKB  = row[80];
                        shiwake.SGYO  = row[81];

                        // 2022/11/11 Ver02.25.02 　0 → " 対応
                        //shiwake.SSRE  = row[82];                        
                        shiwake.SSRE  = row[82] == "0" ? shiwake.SSRE = "" : shiwake.SSRE = row[82];

                        shiwake.STKY  = row[83];
                        shiwake.STNO  = row[84];
                        shiwake.ZKMK  = row[85];
                        shiwake.ZRIT  = row[86];
                        shiwake.ZZKB  = row[87];
                        shiwake.ZGYO  = row[88];

                        // 2022/11/11 Ver02.25.02 　0 → "" 対応
                        //shiwake.ZSRE  = row[89];
                        shiwake.ZSRE  = row[89] == "0" ? shiwake.ZSRE = "" : shiwake.ZSRE = row[89];

                        shiwake.EXVL  = Convert.ToDouble(row[90]);
                        shiwake.VALU  = Convert.ToDouble(row[91]);
                        shiwake.SYMD  = Convert.ToInt32(row[92]);
                        shiwake.SKBN  = row[93];
                        shiwake.SKIZ  = Convert.ToInt32(row[94]);
                        shiwake.UYMD  = Convert.ToInt32(row[95]);
                        shiwake.UKBN  = row[96];
                        shiwake.UKIZ  = Convert.ToInt32(row[97]);
                        shiwake.DKEC  = row[98];
                        shiwake.FUSR  = row[99];
                        shiwake.FSEN  = Convert.ToInt16(row[100]);
                        shiwake.TKFLG = Convert.ToInt16(row[101]);
                        shiwake.BUNRI = row[102];
                        shiwake.HEIC  = row[103];
                        shiwake.RATE  = row[104];
                        shiwake.GEXVL = row[105];
                        shiwake.GVALU = row[106];
                        // 2023/11/14 Ver01.02.04 半角スペース対応 *-
                        if (!String.IsNullOrEmpty(row[107].Trim()))
                        {
                            shiwake.RKEIGEN = Convert.ToInt16(row[107]);
                        }
                        if (!String.IsNullOrEmpty(row[108].Trim()))
                        {
                            shiwake.SKEIGEN = Convert.ToInt16(row[108]);
                        }
                        if (!String.IsNullOrEmpty(row[109].Trim()))
                        {
                            shiwake.ZKEIGEN = Convert.ToInt16(row[109]);
                        }
                        shiwake.GSEP = row[110];
                        shiwake.LNO  = Convert.ToInt32(row[111]);
                        // Ver01.02.01 インボイス対応(レイアウト追加) *-
                        // Ver01.02.02　旧レイアウト修正 *-
                        if (RNo == "2" && (shiwake.DYMD > 20230930))
                        {
                            if (!String.IsNullOrEmpty(row[112].Trim()))
                            {
                                shiwake.RURIZEIKEISAN = Convert.ToInt16(row[112]);
                            }
                            if (!String.IsNullOrEmpty(row[113].Trim()))
                            {
                                shiwake.SURIZEIKEISAN = Convert.ToInt16(row[113]);
                            }
                            if (!String.IsNullOrEmpty(row[114].Trim()))
                            {
                                shiwake.ZURIZEIKEISAN = Convert.ToInt16(row[114]);
                            }
                            if (!String.IsNullOrEmpty(row[115].Trim()))
                            {
                                shiwake.RMENZEIKEIKA = Convert.ToInt16(row[115]);
                            }
                            if (!String.IsNullOrEmpty(row[116].Trim()))
                            {
                                shiwake.SMENZEIKEIKA = Convert.ToInt16(row[116]);
                            }
                            if (!String.IsNullOrEmpty(row[117].Trim()))
                            {
                                shiwake.ZMENZEIKEIKA = Convert.ToInt16(row[117]);
                            }
                        }
                        // -* 2023/11/14 Ver01.02.04 半角スペース対応
    // -*
// -*
// Ver01.02.03 2023/10/01以前なら旧伝票 -*
                        else if (RNo == "2" && (shiwake.DYMD <= 20230930))
                        {
                            shiwake.RURIZEIKEISAN = 0;
                            shiwake.SURIZEIKEISAN = 0;
                            shiwake.ZURIZEIKEISAN = 0;
                            shiwake.RMENZEIKEIKA  = 0;
                            shiwake.SMENZEIKEIKA  = 0;
                            shiwake.ZMENZEIKEIKA  = 0;
                        }
// -*

                        if (shiwake.LNO != 0)
                        {
                            // ファイル取得先フォルダパス( c:\eteam\tmp\ImpFile_リンクNo )
                            string impFileFolderFull = System.IO.Path.Combine(path, IMP_APPENDFILE_FOLDER + shiwake.LNO.ToString());
                            // リンク情報CSVパス( c:\eteam\tmp\linkData_リンクNo )
                            string linkCsvPathFull   = System.IO.Path.Combine(path, IMP_LINKDATA_CSV + shiwake.LNO.ToString() + "." + EXTENSION_CSV);

                            // フォルダ・ファイルが存在しなければエラー
                            if (!System.IO.Directory.Exists(impFileFolderFull))
                            {
                                int errCd = (int)ReturnCode.ImpFolderExistsError;
                                throw new Exception(errCd.ToString());
                            }
                            if (!System.IO.File.Exists(linkCsvPathFull))
                            {
                                int errCd = (int)ReturnCode.LinkCsvExistsError;
                                throw new Exception(errCd.ToString());
                            }

                            // リンク情報ファイルからIMPORTERSUBへの引数に
                            List<string[]> linkStr = ReadCsvFile(linkCsvPathFull);
                            foreach (string[] linkRow in linkStr)
                            {
                                // データ個数エラー
                                if (linkRow.Length != 11)
                                {
                                    int errCd = (int)ReturnCode.DataNumError;
                                    throw new Exception(errCd.ToString());
                                }

                                //e文書向け情報をリストから取得・セット
                                C_Siwake_Link SLink = new C_Siwake_Link();
                                SLink.LNO  = shiwake.LNO;
                                SLink.LNAM = linkRow[0];                                            //リンク名称
                                SLink.FLG1 = Convert.ToInt16(linkRow[1]);                           //0:URL、2:通常ファイル、3:e文書(スキャナ)、4:e文書(電子取引・タイムスタンプあり)、5:e文書(電子取引・タイムスタンプ無し)
// Ver01.02.04.02 *-
//                              SLink.LINK = System.IO.Path.Combine(impFileFolderFull, linkRow[2]); //ファイルパス                  
                                if (SLink.FLG1 == 0)
                                {
                                    SLink.LINK = linkRow[2];
                                }
                                else
                                {
                                    SLink.LINK = System.IO.Path.Combine(impFileFolderFull, linkRow[2]);
                                }
// -*
                                //以下e文書専用
                                if (SLink.FLG1 == 3 || SLink.FLG1 == 4 || SLink.FLG1 == 5)
                                {
                                    SLink.EDOC      = linkRow[3];                                   //e文書番号
                                    SLink.NUSR      = linkRow[4];                                   //申請者名称
                                    SLink.SUSR      = linkRow[5];                                   //最終承認者名称
                                    SLink.SYUBETSU  = Convert.ToInt16(linkRow[6]);                  //書類種別
                                                                                                    
// 2022/05/13 Ver01.01.05 e文書検索項目任意化対応 --->                                              
//                                  SLink.SYMD     = Convert.ToInt32(linkRow[7]);                   
//                                  SLink.SVALU    = Convert.ToInt64(linkRow[8]);                   
                                    if (string.IsNullOrEmpty(linkRow[7]))                           //書類日付
                                    {                                                               
                                        SLink.SYMD  = null;                                         
                                    } else {                                                        
                                        SLink.SYMD  = Convert.ToInt32(linkRow[7]);                  
                                    }                                                               
                                    if (string.IsNullOrEmpty(linkRow[8]))                           //書類金額
                                    {                                                               
                                        SLink.SVALU = null;                                         
                                    } else {                                                        
                                        SLink.SVALU = Convert.ToInt64(linkRow[8]);                  
                                    }                                                               
// <--- 2022/05/13 Ver01.01.05 e文書検索項目任意化対応                                              
                                                                                                    
                                    SLink.STRNAM    = linkRow[9];                                   //書類発行者名称
                                    SLink.HINMEI    = linkRow[10];                                  //品名
                                }
                                data.SetLink(ref SLink);
                            }
                        }
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
