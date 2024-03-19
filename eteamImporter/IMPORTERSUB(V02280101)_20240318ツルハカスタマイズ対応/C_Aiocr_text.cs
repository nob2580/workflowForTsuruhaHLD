using System.Runtime.InteropServices;

namespace IMPORTERSUB
{
    public interface IAIOCR
    {
        string EDOC    { get; set; }  // e文書番号
        string ITMID   { get; set; }  // 項目ID
        short ITMODR   { get; set; }  // 順序
        string OCRTEXT { get; set; }  // テキスト

        void Clear();
    }

    // ここで付けられている属性はVBA上でコード補完を行うために必要です
    [ComVisible(true), ClassInterface(ClassInterfaceType.None)]
    public class C_Aiocr_Text : IAIOCR
    {
        #region COM GUID
        // AI-OCRでのみ使用するので、GUIDの固定はしません。
        #endregion

        private string M_EDOC;     // e文書番号
        private string M_ITMID;    // 項目ID
        private short  M_ITMODR;   // 順序
        private string M_OCRTEXT;  // テキスト

        /// <summary>
        /// メンバ変数のクリア
        /// </summary>
        public void Clear()
        {
            M_EDOC    = "";
            M_ITMID   = "";
            M_ITMODR  = 0;
            M_OCRTEXT = "";
        }

        public string EDOC
        {
            get { return M_EDOC; }
            set { M_EDOC = value; }
        }

        public string ITMID
        {
            get { return M_ITMID; }
            set { M_ITMID = value; }
        }

        public short ITMODR
        {
            get { return M_ITMODR; }
            set { M_ITMODR = value; }
        }

        public string OCRTEXT
        {
            get { return M_OCRTEXT; }
            set { M_OCRTEXT = value; }
        }
    }
}