using System;
using System.Data;
using System.Windows.Forms;

namespace IMPORTERSUB
{
    internal class DBSUB
    {
        public static int  Kesi_Status = 0;
        public static bool bGSWKCHK    = false;  // 外貨換算仕訳用フラグ
        public static bool siasInvoice = false;  // インボイス対応版

        public Logger mLogger = null;

        // 消込システムの使用チェック
// 2020/03/06 Ver02.20.01.9901 PostgreSQL対応 --->
//        public static int Kesi_Check(IDbConnection cdb, IDbConnection kdb, string ccod, bool P_IsOracle)
//        {
//            int kesn = 0;
//            int ckei = 0;

//            return Kesi_Status = Kesi_LogicChk(kdb, CheckSubsys(cdb, ccod, 110, P_IsOracle), ref kesn, ref ckei, P_IsOracle);
//        }
        public static int Kesi_Check(IDbConnection cdb, IDbConnection kdb, string ccod, bool P_IsPostgre)
        {
            int kesn = 0;
            int ckei = 0;

            return Kesi_Status = Kesi_LogicChk(kdb, CheckSubsys(cdb, ccod, 110, P_IsPostgre), ref kesn, ref ckei, P_IsPostgre);
        }
// <--- 2020/03/06 Ver02.20.01.9901 PostgreSQL対応

        /// <summary>
        /// 消込システムの使用の論理チェック
        /// </summary>
        /// <param name="hSql"   > 会社DBへの接続                            </param>
        /// <param name="nSubsys"> サブシステムの登録有無(1:あり 1以外:なし) </param>
        /// <param name="nKesn"  > 開始決算期                                </param>
        /// <param name="nCkei"  > 開始経過月                                </param>
        /// <returns>              0:未使用  1:準備中(サブシステム登録有＆初期未設定)
        ///                        2:使用   -1:SQLエラー  -11:共通DB設定異常 </returns>
// 2020/03/06 Ver02.20.01.9901 PostgreSQL対応 --->
//        public static int Kesi_LogicChk(IDbConnection hSql, int nSubsys, ref int nKesn, ref int nCkei, bool P_IsOracle)
//        {
//            //テーブルチェックを廃止
//            switch (Kesi_StartChk(hSql, ref nKesn, ref nCkei, P_IsOracle))
        public static int Kesi_LogicChk(IDbConnection hSql, int nSubsys, ref int nKesn, ref int nCkei, bool P_IsPostgre)
        {
            //テーブルチェックを廃止
            switch (Kesi_StartChk(hSql, ref nKesn, ref nCkei, P_IsPostgre))
// <--- 2020/03/06 Ver02.20.01.9901 PostgreSQL対応

            {
                case -1:
                    return -2;
                case 1:
                    if (nSubsys == 1) { return 2;   }
                    else              { return -12; }
                default:
                    if (nSubsys == 1) { return 1; }
                    else              { return 0; }
            }
        }

        /// <summary>
        /// 消込システムの開始設定チェック
        /// </summary>
        /// <param name="hSql" > 会社DBへの接続 </param>
        /// <param name="nKesn"> 開始決算期     </param>
        /// <param name="nCkei"> 開始経過月     </param>
        /// <returns>  1:Ok 0:NG -1:SQLエラー </returns>
// 2020/03/06 Ver02.20.01.9901 PostgreSQL対応 --->
//        public static int Kesi_StartChk(IDbConnection hSql, ref int nKesn, ref int nCkei, bool P_IsOracle)
        public static int Kesi_StartChk(IDbConnection hSql, ref int nKesn, ref int nCkei, bool P_IsPostgre)
// <--- 2020/03/06 Ver02.20.01.9901 PostgreSQL対応

        {
            string      L_SqlString = null;
            IDbCommand  L_Command   = null;
            IDataReader L_Reader    = null;

            try
            {
                L_SqlString           = "select SKESN,SCKEI from KSVOL";
// 2020/03/06 Ver02.20.01.9901 PostgreSQL対応 --->
//                if (P_IsOracle)
//                {
//                    OleDbConnection oSql = (OleDbConnection)hSql;
//                    L_Command = new System.Data.OleDb.OleDbCommand(L_SqlString, oSql);
//                }
//                else
//                {
//                    SqlConnection sSql = (SqlConnection)hSql;
//                    L_Command = new System.Data.SqlClient.SqlCommand(L_SqlString, sSql);
//                }
                L_Command             = hSql.CreateCommand();
                L_Command.CommandText = L_SqlString;
// <--- 2020/03/06 Ver02.20.01.9901 PostgreSQL対応
                L_Reader              = L_Command.ExecuteReader();

                if (L_Reader.Read())
                {
                    nKesn = Convert.ToInt32(L_Reader[0]);
                    nCkei = Convert.ToInt32(L_Reader[1]);
                    L_Reader.Close();
                    return 1;
                }
                L_Reader.Close();
                return 0;
            }
            catch (Exception ex)
            {
//			      Interaction.MsgBox(ex.Message, MsgBoxStyle.Exclamation);
///                MessageBox.Show(ex.Message, "", MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
                return -1;
            }
        }

        /// <summary>
        /// Prj312　処理を変更
        /// サブシステムの使用可否をチェックする
        /// </summary>
        /// <param name="db"         > 共通DBへの接続  </param>
        /// <param name="ccod"       > 会社コード      </param>
        /// <param name="subid"      > サブシステムID  </param>
        /// <param name="P_IsPostgre"> DBEngine        </param>
        /// <returns> 使用可能な場合:1                 </returns>
        /// <remarks> 指定された会社で指定されたサブシステムが
        ///           使用可能である場合1を返します。
        ///           指定されたサブシステムIDが存在しない場合、
        ///           または指定された会社で許可されていない場合
        ///           は0を返します。
        /// </remarks>
        public static int CheckSubsys(IDbConnection db, string ccod, int subid, bool P_IsPostgre)  // <--- 2020/03/06 Ver02.20.01.9901 PostgreSQL対応
        {
            int functionReturnValue = 0;

            IDbCommand L_Cmd    = null;
            object     L_Result = null;

            L_Cmd             = db.CreateCommand();
            L_Cmd.CommandText = "select S.SUBNM from KSUBSYS K, SUBSYS S where K.CCOD = :p and K.SUBID = :p and K.SUBID = S.SUBID";

            IMPORTERSUB.Import.AddParameter_FNC(ref L_Cmd, "@CCOD", DbType.String, ccod);
            IMPORTERSUB.Import.AddParameter_FNC(ref L_Cmd, "@SUBID", DbType.Int32, subid);
            IMPORTERSUB.Import.ReplacePlaceHolder_FNC(ref L_Cmd);
            L_Result = L_Cmd.ExecuteScalar();

            if (L_Result == null) { functionReturnValue = 0; }
            else                  { functionReturnValue = 1; }

            return functionReturnValue;
        }

        //外貨換算仕訳用のチェック（DBHISにVERS=119があるかどうかで判断）
        public static bool CheckGSWKCHK(IDbConnection db, bool P_IsPostgre)                        // <--- 2020/03/06 Ver02.20.01.9901 PostgreSQL対応
        {
            bool functionReturnValue = false;

            IDbCommand L_Cmd    = null;
            object     L_Result = null;
            int        L_DBVER  = 119;

            try
            {
                L_Cmd = db.CreateCommand();
                L_Cmd.CommandText = "select VERS from DBHIS where VERS = " + L_DBVER + "";
                IMPORTERSUB.Import.ReplacePlaceHolder_FNC(ref L_Cmd);
                L_Result = L_Cmd.ExecuteScalar();

                // テーブルの存在チェック
                if (L_Result == null) { functionReturnValue = false; }
                else                  { functionReturnValue = true;  }

                bGSWKCHK = functionReturnValue;
            }
            catch (Exception ex)
            {
//			      Interaction.MsgBox(ex.Message, MsgBoxStyle.Exclamation);
///                MessageBox.Show(ex.Message, "", MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
                  //
                  functionReturnValue = false;
            }
            return functionReturnValue;
        }

        // SIASのインボイス対応版チェック（DBHISにVERS=159があるかどうかで判断）
        public static bool CheckInvoice(IDbConnection db, bool P_IsPostgre)                        // <--- 2020/03/06 Ver02.20.01.9901 PostgreSQL対応
        {
            bool functionReturnValue = false;

            IDbCommand L_Cmd = null;
            object L_Result = null;
            int L_DBVER = 159;

            try
            {
                L_Cmd = db.CreateCommand();
                L_Cmd.CommandText = "select VERS from DBHIS where VERS = " + L_DBVER + "";
                IMPORTERSUB.Import.ReplacePlaceHolder_FNC(ref L_Cmd);
                L_Result = L_Cmd.ExecuteScalar();

                // テーブルの存在チェック
                if (L_Result == null) { functionReturnValue = false; }
                else { functionReturnValue = true; }

                siasInvoice = functionReturnValue;
            }
            catch (Exception ex)
            {
                functionReturnValue = false;
            }
            return functionReturnValue;
        }

        #region Ver02.20.01.99(Postgre対応) (コメントアウト)
        //public static int? Kesi_Status = null;
        ////*-01.07.01　外貨換算仕訳用フラグ（テーブルの存在有無とZDATA/SJDATのカラムの存在有無で使用）
        //public static bool bGSWKCHK = false;
        ////-*01.07.01

        //public static int Kesi_Check(IDbConnection cdb, IDbConnection kdb, string ccod, bool P_IsPostgre)
        //{
        //    // <--- MDCR_WWMM PostgreSQL版対応
        //    int kesn = 0;
        //    int ckei = 0;

        //    Kesi_Status = Kesi_LogicChk(kdb, CheckSubsys(cdb, ccod, 110, P_IsPostgre), ref kesn, ref ckei, P_IsPostgre);
        //    // <--- MDCR_WWMM PostgreSQL版対応
        //}
        ///// <summary>
        ///// 消込システムの使用の論理チェック
        ///// </summary>
        ///// <param name="hSql">会社DBへの接続</param>
        ///// <param name="nSubsys">サブシステムの登録有無(1:あり 1以外:なし)</param>
        ///// <param name="nKesn">開始決算期</param>
        ///// <param name="nCkei">開始経過月</param>
        ///// <returns>0:未使用 1:使用準備中(サブシステム登録あり＆初期設定未了) 2:使用 -1:SQLエラー -11:共通DB設定異常</returns>
        ///// <remarks>DBSUB_Kesi_LogicChk</remarks>
        //public static int Kesi_LogicChk(IDbConnection hSql, int nSubsys, ref int nKesn, ref int nCkei, bool P_IsPostgre)
        //{
        //    // <--- MDCR_WWMM PostgreSQL版対応
        //    //Prj312--->
        //    //テーブルチェックを廃止
        //    switch (Kesi_StartChk(hSql, ref nKesn, ref nCkei, P_IsPostgre))
        //    {
        //        // <--- MDCR_WWMM PostgreSQL版対応
        //        case -1:
        //            return -2;
        //        case 1:
        //            if (nSubsys == 1)
        //            {
        //                return 2;
        //            }
        //            else
        //            {
        //                return -12;
        //            }
        //            break;
        //        default:
        //            if (nSubsys == 1)
        //            {
        //                return 1;
        //            }
        //            else
        //            {
        //                return 0;
        //            }
        //            break;
        //    }
        //}

        ///// <summary>
        ///// 消込システムの開始設定チェック
        ///// </summary>
        ///// <param name="hSql">会社DBへの接続</param>
        ///// <param name="nKesn">開始決算期</param>
        ///// <param name="nCkei">開始経過月</param>
        ///// <returns>1:Ok 0:NG -1:SQLエラー</returns>
        ///// <remarks></remarks>
        //public static int Kesi_StartChk(IDbConnection hSql, ref int nKesn, ref int nCkei, bool P_IsPostgre)
        //{
        //    // <--- MDCR_WWMM PostgreSQL版対応
        //    string      L_SqlString = null;
        //    IDbCommand  L_Command   = default(IDbCommand);
        //    IDataReader L_Reader    = default(IDataReader);
        //    try
        //    {
        //        L_SqlString           = "SELECT SKESN,SCKEI FROM KSVOL";
        //        L_Command             = hSql.CreateCommand();                
        //        L_Command.CommandText = L_SqlString;  // <--- MDCR_WWMM PostgreSQL版対応
        //        L_Reader              = L_Command.ExecuteReader();
        //        if (L_Reader.Read())
        //        {
        //            nKesn = Convert.ToInt32(L_Reader[0]);
        //            nCkei = Convert.ToInt32(L_Reader[1]);
        //            L_Reader.Close();
        //            return 1;
        //        }
        //        L_Reader.Close();
        //        return 0;
        //    }
        //    catch (Exception ex)
        //    {
        //        Interaction.MsgBox(ex.Message, MsgBoxStyle.Exclamation);
        //        return -1;
        //    }
        //}

        ///// <summary>
        ///// Prj312　処理を変更
        ///// サブシステムの使用可否をチェックする
        ///// </summary>
        ///// <param name="db">共通DBへの接続</param>
        ///// <param name="ccod">会社コード</param>
        ///// <param name="subid">サブシステムID</param>
        ///// <param name="P_IsPostgre">DBEngine</param>
        ///// <returns>使用可能な場合1</returns>
        ///// <remarks>指定された会社で指定されたサブシステムが使用可能である場合1を返します。
        ///// 指定されたサブシステムIDが存在しない場合、
        ///// 指定された会社で許可されていない場合は0を返します。
        ///// </remarks>
        //public static int CheckSubsys(IDbConnection db, string ccod, int subid, bool P_IsPostgre)
        //{
        //    int functionReturnValue = 0;
        //    // <--- MDCR_WWMM PostgreSQL版対応
        //    IDbCommand L_Cmd = default(IDbCommand);
        //    object L_Result = null;

        //    L_Cmd = db.CreateCommand;
        //    L_Cmd.CommandText = "SELECT S.SUBNM FROM KSUBSYS K, SUBSYS S WHERE K.CCOD = :p AND K.SUBID = :p AND K.SUBID = S.SUBID";
        //    IMPORTERSUB.Import.AddParameter_FNC(L_Cmd, "@CCOD", DbType.String, ccod);
        //    IMPORTERSUB.Import.AddParameter_FNC(L_Cmd, "@SUBID", DbType.Int32, subid);
        //    IMPORTERSUB.Import.ReplacePlaceHolder_FNC(L_Cmd);
        //    L_Result = L_Cmd.ExecuteScalar();
        //    if (L_Result == null)
        //    {
        //        functionReturnValue = 0;
        //    }
        //    else
        //    {
        //        functionReturnValue = 1;
        //    }
        //    return functionReturnValue;
        //}

        ////*-01.07.01
        ////外貨換算仕訳用のチェック（DBHISにVERS=119があるかどうかで判断）
        //public static bool CheckGSWKCHK(IDbConnection db, bool P_IsPostgre)
        //{
        //    bool functionReturnValue = false;
        //    // <--- MDCR_WWMM PostgreSQL版対応
        //    IDbCommand L_Cmd = default(IDbCommand);
        //    object L_Result = null;
        //    int L_DBVER = 119;

        //    try
        //    {
        //        L_Cmd = db.CreateCommand;
        //        L_Cmd.CommandText = "SELECT VERS FROM DBHIS WHERE VERS = " + L_DBVER + "";
        //        IMPORTERSUB.Import.ReplacePlaceHolder_FNC(L_Cmd);
        //        L_Result = L_Cmd.ExecuteScalar();
        //        //テーブルの存在チェック
        //        if (L_Result == null)
        //        {
        //            functionReturnValue = false;
        //        }
        //        else
        //        {
        //            functionReturnValue = true;
        //        }

        //        bGSWKCHK = functionReturnValue;

        //    }
        //    catch (Exception ex)
        //    {
        //        Interaction.MsgBox(ex.Message, MsgBoxStyle.Exclamation);
        //    }

        //    return functionReturnValue;
        //}
        // <---
        #endregion
    }
}
