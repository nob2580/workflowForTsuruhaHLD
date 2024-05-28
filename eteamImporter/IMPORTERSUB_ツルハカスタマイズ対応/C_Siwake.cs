using System.Runtime.InteropServices;

namespace IMPORTERSUB
{
    public interface ISiwake
    {
        int    DYMD          { get; set; }  // 伝票日付
        short  SEIRI         { get; set; }  // 整理月フラグ
        string DCNO          { get; set; }  // 伝票番号
        int    KYMD          { get; set; }  // 起票年月日
        string KBMN          { get; set; }  // 起票部門コード
        string KUSR          { get; set; }  // 起票者コード
        int    SGNO          { get; set; }  // 承認グループNo.
        string HF1           { get; set; }  // ヘッダーフィールド１
        string HF2           { get; set; }  // ヘッダーフィールド２
        string HF3           { get; set; }  // ヘッダーフィールド３
        string HF4           { get; set; }  // ヘッダーフィールド４
        string HF5           { get; set; }  // ヘッダーフィールド５
        string HF6           { get; set; }  // ヘッダーフィールド６
        string HF7           { get; set; }  // ヘッダーフィールド７
        string HF8           { get; set; }  // ヘッダーフィールド８
        string HF9           { get; set; }  // ヘッダーフィールド９
        string HF10          { get; set; }  // ヘッダーフィールド１０
        string RBMN          { get; set; }  // 借方部門コード
        string RTOR          { get; set; }  // 借方取引先コード
        string RKMK          { get; set; }  // 借方科目コード
        string REDA          { get; set; }  // 借方枝番コード
        string RKOJ          { get; set; }  // 借方工事コード
        string RKOS          { get; set; }  // 借方工種コード
        string RPRJ          { get; set; }  // 借方プロジェクト
        string RSEG          { get; set; }  // 借方セグメントコード
        string RDM1          { get; set; }  // 借方ユニバーサル１
        string RDM2          { get; set; }  // 借方ユニバーサル２
        string RDM3          { get; set; }  // 借方ユニバーサル３
        string RDM4          { get; set; }  // 借方ユニバーサル４
        string RDM5          { get; set; }  // 借方ユニバーサル５
        string RDM6          { get; set; }  // 借方ユニバーサル６
        string RDM7          { get; set; }  // 借方ユニバーサル７
        string RDM8          { get; set; }  // 借方ユニバーサル８
        string RDM9          { get; set; }  // 借方ユニバーサル９
        string RDM10         { get; set; }  // 借方ユニバーサル１０
        string RDM11         { get; set; }  // 借方ユニバーサル１１
        string RDM12         { get; set; }  // 借方ユニバーサル１２
        string RDM13         { get; set; }  // 借方ユニバーサル１３
        string RDM14         { get; set; }  // 借方ユニバーサル１４
        string RDM15         { get; set; }  // 借方ユニバーサル１５
        string RDM16         { get; set; }  // 借方ユニバーサル１６
        string RDM17         { get; set; }  // 借方ユニバーサル１７
        string RDM18         { get; set; }  // 借方ユニバーサル１８
        string RDM19         { get; set; }  // 借方ユニバーサル１９
        string RDM20         { get; set; }  // 借方ユニバーサル２０
        string RRIT          { get; set; }  // 借方税率
        string RZKB          { get; set; }  // 借方課税区分
        string RGYO          { get; set; }  // 借方業種区分
        string RSRE          { get; set; }  // 借方仕入区分
        string RTKY          { get; set; }  // 借方摘要
        string RTNO          { get; set; }  // 借方摘要コード
        string SBMN          { get; set; }  // 貸方部門コード
        string STOR          { get; set; }  // 貸方取引先コード
        string SKMK          { get; set; }  // 貸方科目コード
        string SEDA          { get; set; }  // 貸方枝番コード
        string SKOJ          { get; set; }  // 貸方工事コード
        string SKOS          { get; set; }  // 貸方工種コード
        string SPRJ          { get; set; }  // 貸方プロジェクト
        string SSEG          { get; set; }  // 貸方セグメントコード
        string SDM1          { get; set; }  // 貸方ユニバーサル１
        string SDM2          { get; set; }  // 貸方ユニバーサル２
        string SDM3          { get; set; }  // 貸方ユニバーサル３
        string SDM4          { get; set; }  // 貸方ユニバーサル４
        string SDM5          { get; set; }  // 貸方ユニバーサル５
        string SDM6          { get; set; }  // 貸方ユニバーサル６
        string SDM7          { get; set; }  // 貸方ユニバーサル７
        string SDM8          { get; set; }  // 貸方ユニバーサル８
        string SDM9          { get; set; }  // 貸方ユニバーサル９
        string SDM10         { get; set; }  // 貸方ユニバーサル１０
        string SDM11         { get; set; }  // 貸方ユニバーサル１１
        string SDM12         { get; set; }  // 貸方ユニバーサル１２
        string SDM13         { get; set; }  // 貸方ユニバーサル１３
        string SDM14         { get; set; }  // 貸方ユニバーサル１４
        string SDM15         { get; set; }  // 貸方ユニバーサル１５
        string SDM16         { get; set; }  // 貸方ユニバーサル１６
        string SDM17         { get; set; }  // 貸方ユニバーサル１７
        string SDM18         { get; set; }  // 貸方ユニバーサル１８
        string SDM19         { get; set; }  // 貸方ユニバーサル１９
        string SDM20         { get; set; }  // 貸方ユニバーサル２０
        string SRIT          { get; set; }  // 貸方税率
        string SZKB          { get; set; }  // 貸方課税区分
        string SGYO          { get; set; }  // 貸方業種区分
        string SSRE          { get; set; }  // 貸方仕入区分
        string STKY          { get; set; }  // 貸方摘要
        string STNO          { get; set; }  // 貸方摘要コード
        string ZKMK          { get; set; }  // 消費税対象科目コード
        string ZRIT          { get; set; }  // 消費税対象科目税率
        string ZZKB          { get; set; }  // 消費税対象科目課税区分
        string ZGYO          { get; set; }  // 消費税対象科目業種区分
        string ZSRE          { get; set; }  // 消費税対象科目仕入区分
        double EXVL          { get; set; }  // 対価金額
        double VALU          { get; set; }  // 金額
        int    SYMD          { get; set; }  // 支払日
        string SKBN          { get; set; }  // 支払区分
        int    SKIZ          { get; set; }  // 支払期日
        int    UYMD          { get; set; }  // 回収日
        string UKBN          { get; set; }  // 入金区分
        int    UKIZ          { get; set; }  // 回収期日
        string DKEC          { get; set; }  // 消込コード
        string FUSR          { get; set; }  // 入力者コード
        short  FSEN          { get; set; }  // 付箋番号
        short  TKFLG         { get; set; }  // 貸借別摘要フラグ
        string BUNRI         { get; set; }  // 分離区分
        string HEIC          { get; set; }  // 幣種
        string RATE          { get; set; }  // レート
        string GEXVL         { get; set; }  // 外貨対価金額
        string GVALU         { get; set; }  // 外貨金額

        // ↓ de3専用 ↓
        string TKY           { get; set; }  // 摘要
        string TNO           { get; set; }  // 摘要コード
        short  STEN          { get; set; }  // 店券フラグ
        // ↑ de3専用 ↑

        // 行区切り対応
        string GSEP          { get; set; }  // 行区切り
        // *-リンク情報
        int    LNO           { get; set; }  // リンク情報
        // 軽減税率区分
        int    RKEIGEN       { get; set; }  // 借方　軽減税率区分
        int    SKEIGEN       { get; set; }  // 貸方　軽減税率区分
        int    ZKEIGEN       { get; set; }  // 税科　軽減税率区分

// Ver02.26.01 インボイス対応
        // 売上税額計算方式
        short  RURIZEIKEISAN { get; set; }
        short  SURIZEIKEISAN { get; set; }
        short  ZURIZEIKEISAN { get; set; }
        // 仕入税額控除経過措置割合
        short  RMENZEIKEIKA  { get; set; }
        short  SMENZEIKEIKA  { get; set; }
        short  ZMENZEIKEIKA  { get; set; }

        // メンバ変数のクリア
        void Clear();
    }

    // ここで付けられている属性はVBA上でコード補完を行うために必要です
    [ComVisible(true), ClassInterface(ClassInterfaceType.None)]
    public class C_Siwake : ISiwake
    {
        #region COM GUID
        // これらの GUID は、このクラスおよびその COM インターフェイスの COM ID を 
        // 指定します。この値を変更すると、 
        // 既存のクライアントはクラスにアクセスできなくなります。
        public const string ClassId = "655CA529-9512-4926-98FA-B5477136099F";
        public const string InterfaceId = "6EC84590-93C9-4116-8E10-9C947D32F090";
        public const string EventsId = "179AEED9-E467-442A-80EB-17FB17131A87";
        #endregion

        private int    M_DYMD;     // 伝票日付      西暦：［19980101］～［99991231］ 必須項目
        private short  M_SEIRI;    // 整理月フラグ  0:通常月 1:整理月（四半期、中間、決算に関係なし）0、1以外は不可です。（値をセットしない状態は通常月です）
        private string M_DCNO;     // 伝票番号
        private int    M_KYMD;     // 起票年月日
        private string M_KBMN;     // 起票部門コード
        private string M_KUSR;     // 起票者コード
        private int    M_SGNO;     // 承認グループNo.
        private string M_HF1;      // ヘッダーフィールド１
        private string M_HF2;      // ヘッダーフィールド２
        private string M_HF3;      // ヘッダーフィールド３
        private string M_HF4;      // ヘッダーフィールド４
        private string M_HF5;      // ヘッダーフィールド５
        private string M_HF6;      // ヘッダーフィールド６
        private string M_HF7;      // ヘッダーフィールド７
        private string M_HF8;      // ヘッダーフィールド８
        private string M_HF9;      // ヘッダーフィールド９
        private string M_HF10;     // ヘッダーフィールド１０
        private string M_RBMN;     // 借方部門コード
        private string M_RTOR;     // 借方取引先コード
        private string M_RKMK;     // 借方科目コード
        private string M_REDA;     // 借方枝番コード
        private string M_RKOJ;     // 借方工事コード
        private string M_RKOS;     // 借方工種コード
        private string M_RPRJ;     // 借方プロジェクト
        private string M_RSEG;     // 借方セグメントコード
        private string M_RDM1;     // 借方ユニバーサル１
        private string M_RDM2;     // 借方ユニバーサル２
        private string M_RDM3;     // 借方ユニバーサル３
        private string M_RDM4;     // 借方ユニバーサル４
        private string M_RDM5;     // 借方ユニバーサル５
        private string M_RDM6;     // 借方ユニバーサル６
        private string M_RDM7;     // 借方ユニバーサル７
        private string M_RDM8;     // 借方ユニバーサル８
        private string M_RDM9;     // 借方ユニバーサル９
        private string M_RDM10;    // 借方ユニバーサル１０
        private string M_RDM11;    // 借方ユニバーサル１１
        private string M_RDM12;    // 借方ユニバーサル１２
        private string M_RDM13;    // 借方ユニバーサル１３
        private string M_RDM14;    // 借方ユニバーサル１４
        private string M_RDM15;    // 借方ユニバーサル１５
        private string M_RDM16;    // 借方ユニバーサル１６
        private string M_RDM17;    // 借方ユニバーサル１７
        private string M_RDM18;    // 借方ユニバーサル１８
        private string M_RDM19;    // 借方ユニバーサル１９
        private string M_RDM20;    // 借方ユニバーサル２０
        private string M_RRIT;     // 借方税率
        private string M_RZKB;     // 借方課税区分
        private string M_RGYO;     // 借方業種区分
        private string M_RSRE;     // 借方仕入区分
        private string M_RTKY;     // 借方摘要
        private string M_RTNO;     // 借方摘要コード
        private string M_SBMN;     // 貸方部門コード
        private string M_STOR;     // 貸方取引先コード
        private string M_SKMK;     // 貸方科目コード
        private string M_SEDA;     // 貸方枝番コード
        private string M_SKOJ;     // 貸方工事コード
        private string M_SKOS;     // 貸方工種コード
        private string M_SPRJ;     // 貸方プロジェクト
        private string M_SSEG;     // 貸方セグメントコード
        private string M_SDM1;     // 貸方ユニバーサル１
        private string M_SDM2;     // 貸方ユニバーサル２
        private string M_SDM3;     // 貸方ユニバーサル３
        private string M_SDM4;     // 貸方ユニバーサル４
        private string M_SDM5;     // 貸方ユニバーサル５
        private string M_SDM6;     // 貸方ユニバーサル６
        private string M_SDM7;     // 貸方ユニバーサル７
        private string M_SDM8;     // 貸方ユニバーサル８
        private string M_SDM9;     // 貸方ユニバーサル９
        private string M_SDM10;    // 貸方ユニバーサル１０
        private string M_SDM11;    // 貸方ユニバーサル１１
        private string M_SDM12;    // 貸方ユニバーサル１２
        private string M_SDM13;    // 貸方ユニバーサル１３
        private string M_SDM14;    // 貸方ユニバーサル１４
        private string M_SDM15;    // 貸方ユニバーサル１５
        private string M_SDM16;    // 貸方ユニバーサル１６
        private string M_SDM17;    // 貸方ユニバーサル１７
        private string M_SDM18;    // 貸方ユニバーサル１８
        private string M_SDM19;    // 貸方ユニバーサル１９
        private string M_SDM20;    // 貸方ユニバーサル２０
        private string M_SRIT;     // 貸方税率
        private string M_SZKB;     // 貸方課税区分
        private string M_SGYO;     // 貸方業種区分
        private string M_SSRE;     // 貸方仕入区分
        private string M_STKY;     // 貸方摘要
        private string M_STNO;     // 貸方摘要コード
        private string M_ZKMK;     // 消費税対象科目コード
        private string M_ZRIT;     // 消費税対象科目税率
        private string M_ZZKB;     // 消費税対象科目課税区分
        private string M_ZGYO;     // 消費税対象科目業種区分
        private string M_ZSRE;     // 消費税対象科目仕入区分
        private double M_EXVL;     // 対価金額
        private double M_VALU;     // 金額
        private int    M_SYMD;     // 支払日
        private string M_SKBN;     // 支払区分
        private int    M_SKIZ;     // 支払期日
        private int    M_UYMD;     // 回収日
        private string M_UKBN;     // 入金区分
        private int    M_UKIZ;     // 回収期日
        private string M_DKEC;     // 消込コード
        private string M_FUSR;     // 入力者コード
        private short  M_FSEN;     // 付箋番号
        private short  M_TKFLG;    // 貸借別摘要フラグ
        private string M_BUNRI;    // 分離区分
        private string M_HEIC;     // 幣種
        private string M_RATE;     // レート
        private string M_GEXVL;    // 外貨対価金額
        private string M_GVALU;    // 外貨金額

        // ↓ de3レイアウト用 ↓ 
        private string M_TKY;      // 摘要        
        private string M_TNO;      // 摘要コード
        private short  M_STEN;     // 店券フラグ
        // ↑ de3レイアウト用 ↑

        private string M_GSEP;     // 行区切り
        private int    M_LNO;      // リンクNo
        private int    M_RKEIGEN;  // (借)軽減税率区分
        private int    M_SKEIGEN;  // (貸)軽減税率区分
        private int    M_ZKEIGEN;  // (税)軽減税率区分

        // Ver02.26.01 インボイス対応 --->
        private short  M_RURIZEIKEISAN;  // (借)売上税額計算方式
        private short  M_SURIZEIKEISAN;  // (貸)売上税額計算方式
        private short  M_ZURIZEIKEISAN;  // (税)売上税額計算方式
        private short  M_RMENZEIKEIKA;   // (借)仕入税額控除経過措置割合
        private short  M_SMENZEIKEIKA;   // (貸)仕入税額控除経過措置割合
        private short  M_ZMENZEIKEIKA;   // (税)仕入税額控除経過措置割合
        // <--- Ver02.26.01 インボイス対応

        #region メンバ変数のクリア
        /// <summary>
        /// メンバ変数のクリア
        /// </summary>
        public void Clear()
        {
            M_DYMD          = 0;
            M_SEIRI         = 0;
            M_DCNO          = "";
            M_KYMD          = 0;
            M_KBMN          = "";
            M_KUSR          = "";
            M_SGNO          = 0;
            M_RBMN          = "";
            M_RTOR          = "";
            M_RKMK          = "";
            M_REDA          = "";
            M_RKOJ          = "";
            M_RKOS          = "";
            M_RPRJ          = "";
            M_RSEG          = "";
            M_RDM1          = "";
            M_RDM2          = "";
            M_RDM3          = "";
            M_RDM4          = "";
            M_RDM5          = "";
            M_RDM6          = "";
            M_RDM7          = "";
            M_RDM8          = "";
            M_RDM9          = "";
            M_RDM10         = "";
            M_RDM11         = "";
            M_RDM12         = "";
            M_RDM13         = "";
            M_RDM14         = "";
            M_RDM15         = "";
            M_RDM16         = "";
            M_RDM17         = "";
            M_RDM18         = "";
            M_RDM19         = "";
            M_RDM20         = "";
            M_RRIT          = "";
            M_RZKB          = "";
            M_RGYO          = "";
            M_RSRE          = "";
            M_RTKY          = "";
            M_RTNO          = "";
            M_SBMN          = "";
            M_STOR          = "";
            M_SKMK          = "";
            M_SEDA          = "";
            M_SKOJ          = "";
            M_SKOS          = "";
            M_SPRJ          = "";
            M_SSEG          = "";
            M_SDM1          = "";
            M_SDM2          = "";
            M_SDM3          = "";
            M_SDM4          = "";
            M_SDM5          = "";
            M_SDM6          = "";
            M_SDM7          = "";
            M_SDM8          = "";
            M_SDM9          = "";
            M_SDM10         = "";
            M_SDM11         = "";
            M_SDM12         = "";
            M_SDM13         = "";
            M_SDM14         = "";
            M_SDM15         = "";
            M_SDM16         = "";
            M_SDM17         = "";
            M_SDM18         = "";
            M_SDM19         = "";
            M_SDM20         = "";
            M_SRIT          = "";
            M_SZKB          = "";
            M_SGYO          = "";
            M_SSRE          = "";
            M_STKY          = "";
            M_STNO          = "";
            M_ZKMK          = "";
            M_ZRIT          = "";
            M_ZZKB          = "";
            M_ZGYO          = "";
            M_ZSRE          = "";
            M_EXVL          = 0;
            M_VALU          = 0;
            M_SYMD          = 0;
            M_SKBN          = "";
            M_SKIZ          = 0;
            M_UYMD          = 0;
            M_UKBN          = "";
            M_UKIZ          = 0;
            M_DKEC          = "";
            M_FUSR          = "";
            M_FSEN          = 0;
            M_TKFLG         = 0;
            M_BUNRI         = "";
            M_HEIC          = "";
            M_RATE          = "";
            M_GEXVL         = "";
            M_GVALU         = "";

            // ↓ de3専用 ↓
            M_TKY           = "";
            M_TNO           = "";
            M_STEN          = 0;
            // ↑ de3専用 ↑

            //行区切り対応
            M_GSEP          = "";
            //*-リンク情報
            M_LNO           = 0;
            // 軽減税率＆10％対応
            M_RKEIGEN       = 0;
            M_SKEIGEN       = 0;
            M_ZKEIGEN       = 0;

            // Ver02.26.01 インボイス対応
            M_RURIZEIKEISAN = 0;
            M_SURIZEIKEISAN = 0;
            M_ZURIZEIKEISAN = 0;
            M_RMENZEIKEIKA  = 0;
            M_SMENZEIKEIKA  = 0;
            M_ZMENZEIKEIKA  = 0;
        }
        #endregion

        #region 各項目プロパティ
        public int DYMD
        {
            get { return M_DYMD; }
            set { M_DYMD = value; }
        }

        public short SEIRI
        {
            get { return M_SEIRI; }
            set { M_SEIRI = value; }
        }

        public string DCNO
        {
            get { return M_DCNO; }
            set { M_DCNO = value; }
        }

        public int KYMD
        {
            get { return M_KYMD; }
            set { M_KYMD = value; }
        }

        public string KBMN
        {
            get { return M_KBMN; }
            set { M_KBMN = value; }
        }

        public string KUSR
        {
            get { return M_KUSR; }
            set { M_KUSR = value; }
        }

        public int SGNO
        {
            get { return M_SGNO; }
            set { M_SGNO = value; }
        }

        public string HF1
        {
            get { return M_HF1; }
            set { M_HF1 = value; }
        }

        public string HF2
        {
            get { return M_HF2; }
            set { M_HF2 = value; }
        }

        public string HF3
        {
            get { return M_HF3; }
            set { M_HF3 = value; }
        }

        public string HF4
        {
            get { return M_HF4; }
            set { M_HF4 = value; }
        }

        public string HF5
        {
            get { return M_HF5; }
            set { M_HF5 = value; }
        }

        public string HF6
        {
            get { return M_HF6; }
            set { M_HF6 = value; }
        }

        public string HF7
        {
            get { return M_HF7; }
            set { M_HF7 = value; }
        }

        public string HF8
        {
            get { return M_HF8; }
            set { M_HF8 = value; }
        }

        public string HF9
        {
            get { return M_HF9; }
            set { M_HF9 = value; }
        }

        public string HF10
        {
            get { return M_HF10; }
            set { M_HF10 = value; }
        }

        public string RBMN
        {
            get { return M_RBMN; }
            set { M_RBMN = value; }
        }

        public string RTOR
        {
            get { return M_RTOR; }
            set { M_RTOR = value; }
        }

        public string RKMK
        {
            get { return M_RKMK; }
            set { M_RKMK = value; }
        }

        public string REDA
        {
            get { return M_REDA; }
            set { M_REDA = value; }
        }

        public string RKOJ
        {
            get { return M_RKOJ; }
            set { M_RKOJ = value; }
        }

        public string RKOS
        {
            get { return M_RKOS; }
            set { M_RKOS = value; }
        }

        public string RPRJ
        {
            get { return M_RPRJ; }
            set { M_RPRJ = value; }
        }

        public string RSEG
        {
            get { return M_RSEG; }
            set { M_RSEG = value; }
        }

        public string RDM1
        {
            get { return M_RDM1; }
            set { M_RDM1 = value; }
        }

        public string RDM2
        {
            get { return M_RDM2; }
            set { M_RDM2 = value; }
        }

        public string RDM3
        {
            get { return M_RDM3; }
            set { M_RDM3 = value; }
        }

        public string RDM4
        {
            get { return M_RDM4; }
            set { M_RDM4 = value; }
        }

        public string RDM5
        {
            get { return M_RDM5; }
            set { M_RDM5 = value; }
        }

        public string RDM6
        {
            get { return M_RDM6; }
            set { M_RDM6 = value; }
        }

        public string RDM7
        {
            get { return M_RDM7; }
            set { M_RDM7 = value; }
        }

        public string RDM8
        {
            get { return M_RDM8; }
            set { M_RDM8 = value; }
        }

        public string RDM9
        {
            get { return M_RDM9; }
            set { M_RDM9 = value; }
        }

        public string RDM10
        {
            get { return M_RDM10; }
            set { M_RDM10 = value; }
        }

        public string RDM11
        {
            get { return M_RDM11; }
            set { M_RDM11 = value; }
        }

        public string RDM12
        {
            get { return M_RDM12; }
            set { M_RDM12 = value; }
        }

        public string RDM13
        {
            get { return M_RDM13; }
            set { M_RDM13 = value; }
        }

        public string RDM14
        {
            get { return M_RDM14; }
            set { M_RDM14 = value; }
        }

        public string RDM15
        {
            get { return M_RDM15; }
            set { M_RDM15 = value; }
        }

        public string RDM16
        {
            get { return M_RDM16; }
            set { M_RDM16 = value; }
        }

        public string RDM17
        {
            get { return M_RDM17; }
            set { M_RDM17 = value; }
        }

        public string RDM18
        {
            get { return M_RDM18; }
            set { M_RDM18 = value; }
        }

        public string RDM19
        {
            get { return M_RDM19; }
            set { M_RDM19 = value; }
        }

        public string RDM20
        {
            get { return M_RDM20; }
            set { M_RDM20 = value; }
        }

        public string RRIT
        {
            get { return M_RRIT; }
            set { M_RRIT = value; }
        }

        public string RZKB
        {
            get { return M_RZKB; }
            set { M_RZKB = value; }
        }

        public string RGYO
        {
            get { return M_RGYO; }
            set { M_RGYO = value; }
        }

        public string RSRE
        {
            get { return M_RSRE; }
            set { M_RSRE = value; }
        }

        public string RTKY
        {
            get { return M_RTKY; }
            set { M_RTKY = value; }
        }

        public string RTNO
        {
            get { return M_RTNO; }
            set { M_RTNO = value; }
        }

        public string SBMN
        {
            get { return M_SBMN; }
            set { M_SBMN = value; }
        }

        public string STOR
        {
            get { return M_STOR; }
            set { M_STOR = value; }
        }

        public string SKMK
        {
            get { return M_SKMK; }
            set { M_SKMK = value; }
        }

        public string SEDA
        {
            get { return M_SEDA; }
            set { M_SEDA = value; }
        }

        public string SKOJ
        {
            get { return M_SKOJ; }
            set { M_SKOJ = value; }
        }

        public string SKOS
        {
            get { return M_SKOS; }
            set { M_SKOS = value; }
        }

        public string SPRJ
        {
            get { return M_SPRJ; }
            set { M_SPRJ = value; }
        }

        public string SSEG
        {
            get { return M_SSEG; }
            set { M_SSEG = value; }
        }

        public string SDM1
        {
            get { return M_SDM1; }
            set { M_SDM1 = value; }
        }

        public string SDM2
        {
            get { return M_SDM2; }
            set { M_SDM2 = value; }
        }

        public string SDM3
        {
            get { return M_SDM3; }
            set { M_SDM3 = value; }
        }

        public string SDM4
        {
            get { return M_SDM4; }
            set { M_SDM4 = value; }
        }

        public string SDM5
        {
            get { return M_SDM5; }
            set { M_SDM5 = value; }
        }

        public string SDM6
        {
            get { return M_SDM6; }
            set { M_SDM6 = value; }
        }

        public string SDM7
        {
            get { return M_SDM7; }
            set { M_SDM7 = value; }
        }

        public string SDM8
        {
            get { return M_SDM8; }
            set { M_SDM8 = value; }
        }

        public string SDM9
        {
            get { return M_SDM9; }
            set { M_SDM9 = value; }
        }

        public string SDM10
        {
            get { return M_SDM10; }
            set { M_SDM10 = value; }
        }

        public string SDM11
        {
            get { return M_SDM11; }
            set { M_SDM11 = value; }
        }

        public string SDM12
        {
            get { return M_SDM12; }
            set { M_SDM12 = value; }
        }

        public string SDM13
        {
            get { return M_SDM13; }
            set { M_SDM13 = value; }
        }

        public string SDM14
        {
            get { return M_SDM14; }
            set { M_SDM14 = value; }
        }

        public string SDM15
        {
            get { return M_SDM15; }
            set { M_SDM15 = value; }
        }

        public string SDM16
        {
            get { return M_SDM16; }
            set { M_SDM16 = value; }
        }

        public string SDM17
        {
            get { return M_SDM17; }
            set { M_SDM17 = value; }
        }

        public string SDM18
        {
            get { return M_SDM18; }
            set { M_SDM18 = value; }
        }

        public string SDM19
        {
            get { return M_SDM19; }
            set { M_SDM19 = value; }
        }

        public string SDM20
        {
            get { return M_SDM20; }
            set { M_SDM20 = value; }
        }

        public string SRIT
        {
            get { return M_SRIT; }
            set { M_SRIT = value; }
        }

        public string SZKB
        {
            get { return M_SZKB; }
            set { M_SZKB = value; }
        }

        public string SGYO
        {
            get { return M_SGYO; }
            set { M_SGYO = value; }
        }

        public string SSRE
        {
            get { return M_SSRE; }
            set { M_SSRE = value; }
        }

        public string STKY
        {
            get { return M_STKY; }
            set { M_STKY = value; }
        }

        public string STNO
        {
            get { return M_STNO; }
            set { M_STNO = value; }
        }

        public string ZKMK
        {
            get { return M_ZKMK; }
            set { M_ZKMK = value; }
        }

        public string ZRIT
        {
            get { return M_ZRIT; }
            set { M_ZRIT = value; }
        }

        public string ZZKB
        {
            get { return M_ZZKB; }
            set { M_ZZKB = value; }
        }

        public string ZGYO
        {
            get { return M_ZGYO; }
            set { M_ZGYO = value; }
        }

        public string ZSRE
        {
            get { return M_ZSRE; }
            set { M_ZSRE = value; }
        }

        public double EXVL
        {
            get { return M_EXVL; }
            set { M_EXVL = value; }
        }

        public double VALU
        {
            get { return M_VALU; }
            set { M_VALU = value; }
        }

        public int SYMD
        {
            get { return M_SYMD; }
            set { M_SYMD = value; }
        }

        public string SKBN
        {
            get { return M_SKBN; }
            set { M_SKBN = value; }
        }

        public int SKIZ
        {
            get { return M_SKIZ; }
            set { M_SKIZ = value; }
        }

        public int UYMD
        {
            get { return M_UYMD; }
            set { M_UYMD = value; }
        }

        public string UKBN
        {
            get { return M_UKBN; }
            set { M_UKBN = value; }
        }

        public int UKIZ
        {
            get { return M_UKIZ; }
            set { M_UKIZ = value; }
        }

        public string DKEC
        {
            get { return M_DKEC; }
            set { M_DKEC = value; }
        }

        public string FUSR
        {
            get { return M_FUSR; }
            set { M_FUSR = value; }
        }

        public short FSEN
        {
            get { return M_FSEN; }
            set { M_FSEN = value; }
        }

        public short TKFLG
        {
            get { return M_TKFLG; }
            set { M_TKFLG = value; }
        }

        public string BUNRI
        {
            get { return M_BUNRI; }
            set { M_BUNRI = value; }
        }

        public string HEIC
        {
            get { return M_HEIC; }
            set { M_HEIC = value; }
        }

        public string RATE
        {
            get { return M_RATE; }
            set { M_RATE = value; }
        }

        public string GEXVL
        {
            get { return M_GEXVL; }
            set { M_GEXVL = value; }
        }

        public string GVALU
        {
            get { return M_GVALU; }
            set { M_GVALU = value; }
        }

        // ↓ de3専用 ↓
        public string TKY
        {
            get { return M_TKY; }
            set { M_TKY = value; }
        }

        public string TNO
        {
            get { return M_TNO; }
            set { M_TNO = value; }
        }

        public short STEN
        {
            get { return M_STEN; }
            set { M_STEN = value; }
        }
        // ↑ de3専用 ↑

        //行区切り対応
        public string GSEP
        {
            get { return M_GSEP; }
            set { M_GSEP = value; }
        }

        //*-リンク情報
        public int LNO
        {
            get { return M_LNO; }
            set { M_LNO = value; }
        }

        // 軽減税率＆10％対応
        public int RKEIGEN
        {
            get { return M_RKEIGEN; }
            set { M_RKEIGEN = value; }
        }

        public int SKEIGEN
        {
            get { return M_SKEIGEN; }
            set { M_SKEIGEN = value; }
        }

        public int ZKEIGEN
        {
            get { return M_ZKEIGEN; }
            set { M_ZKEIGEN = value; }
        }

        // Ver02.26.01 インボイス対応
        public short RURIZEIKEISAN
        {
            get { return M_RURIZEIKEISAN; }
            set { M_RURIZEIKEISAN = value; }
        }
        public short SURIZEIKEISAN
        {
            get { return M_SURIZEIKEISAN; }
            set { M_SURIZEIKEISAN = value; }
        }
        public short ZURIZEIKEISAN
        {
            get { return M_ZURIZEIKEISAN; }
            set { M_ZURIZEIKEISAN = value; }
        }
        public short RMENZEIKEIKA
        {
            get { return M_RMENZEIKEIKA; }
            set { M_RMENZEIKEIKA = value; }
        }
        public short SMENZEIKEIKA
        {
            get { return M_SMENZEIKEIKA; }
            set { M_SMENZEIKEIKA = value; }
        }
        public short ZMENZEIKEIKA
        {
            get { return M_ZMENZEIKEIKA; }
            set { M_ZMENZEIKEIKA = value; }
        }
        #endregion
    }
}
