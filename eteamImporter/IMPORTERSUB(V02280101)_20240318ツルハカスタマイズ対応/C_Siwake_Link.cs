using System.Runtime.InteropServices;

namespace IMPORTERSUB
{
    public interface ISiwakeLink
    {
        int    LNO      { get; set; }  // リンクNo
        string LNAM     { get; set; }  // リンク名称
        short  FLG1     { get; set; }  // 形式
        string LINK     { get; set; }  // リンク先
        string EDOC     { get; set; }  // e文書番号
        string NUSR     { get; set; }  // 申請者
        string SUSR     { get; set; }  // 最終承認者
        short  SYUBETSU { get; set; }  // 書類種別
        // 仮対応
        //int    SYMD     { get; set; }  // 日付
        //long   SVALU    { get; set; }  // 金額
        int?   SYMD { get; set; }  // 日付
        long?  SVALU { get; set; }  // 金額
        string STRNAM   { get; set; }  // 発行者名称
        string HINMEI   { get; set; }  // 品名
        string BIKO     { get; set; }  // 備考(AI-OCRでのみ使用)

        // メンバ変数のクリア
        void Clear();
    }

    // ここで付けられている属性はVBA上でコード補完を行うために必要です
    [ComVisible(true), ClassInterface(ClassInterfaceType.None)]
    public class C_Siwake_Link : ISiwakeLink
    {
        #region COM GUID
        // これらの GUID は、このクラスおよびその COM インターフェイスの COM ID を 
        // 指定します。この値を変更すると、 
        // 既存のクライアントはクラスにアクセスできなくなります。
        public const string ClassId = "FCB8FB0F-793E-4F95-9974-A5E6C428ACC1";
        public const string InterfaceId = "47B959D1-24D4-453C-A861-98BE85756711";
        public const string EventsId = "CDCE3045-817F-4BC0-81E1-9E93DF3A9E73";
        #endregion

        // メンバ変数
        private int    M_LNO;       // リンクNo
        private string M_LNAM;      // リンク名称
        private short  M_FLG1;      // 形式
        private string M_LINK;      // リンク先
        private string M_EDOC;      // e文書番号
        private string M_NUSR;      // 申請者名称
        private string M_SUSR;      // 最終承認者名称
        private short  M_SYUBETSU;  // 書類種別
        // 仮対応
        //private int    M_SYMD;      // 日付
        //private long   M_SVALU;     // 金額
        private int?   M_SYMD;      // 日付
        private long?  M_SVALU;     // 金額
        private string M_STRNAM;    // 発行者名称

// 2021/10/14 Ver02.22.13 令和３年改正電帳法対応(コメント追記のみ) --->
        private string M_HINMEI;    // 品名(備考にセット)
// <--- 2021/10/14 Ver02.22.13 令和３年改正電帳法対応(コメント追記のみ)

        private string M_BIKO;      // 備考(AI-OCRでのみ使用)

        /// <summary>
        /// メンバ変数のクリア
        /// </summary>
        public void Clear()
        {
            M_LNO      = 0;
            M_LNAM     = "";
            M_FLG1     = 0;
            M_LINK     = "";
            M_EDOC     = "";
            M_SYUBETSU = 0;
            M_SYMD     = 0;
            M_SVALU    = 0;
            M_STRNAM   = "";
            M_NUSR     = "";
            M_SUSR     = "";
            M_HINMEI   = "";
            M_BIKO     = "";
        }

        #region 各項目プロパティ
        public int LNO
        {
            get { return M_LNO; }
            set { M_LNO = value; }
        }

        public string LNAM
        {
            get { return M_LNAM; }
            set { M_LNAM = value; }
        }

        public short FLG1
        {
            get { return M_FLG1; }
            set { M_FLG1 = value; }
        }

        public string LINK
        {
            get { return M_LINK; }
            set { M_LINK = value; }
        }

        public string EDOC
        {
            get { return M_EDOC; }
            set { M_EDOC = value; }
        }

        public short SYUBETSU
        {
            get { return M_SYUBETSU; }
            set { M_SYUBETSU = value; }
        }

        // 仮対応
        //public int SYMD
        public int? SYMD
        {
            get { return M_SYMD; }
            set { M_SYMD = value; }
        }

        // 仮対応
        //public long SVALU
        public long? SVALU
        {
            get { return M_SVALU; }
            set { M_SVALU = value; }
        }

        public string STRNAM
        {
            get { return M_STRNAM; }
            set { M_STRNAM = value; }
        }

        public string NUSR
        {
            get { return M_NUSR; }
            set { M_NUSR = value; }
        }

        public string SUSR
        {
            get { return M_SUSR; }
            set { M_SUSR = value; }
        }

        public string HINMEI
        {
            get { return M_HINMEI; }
            set { M_HINMEI = value; }
        }

        public string BIKO
        {
            get { return M_BIKO; }
            set { M_BIKO = value; }
        }
        #endregion
    }
}