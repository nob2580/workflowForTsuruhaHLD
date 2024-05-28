using System;
using System.Data;
using System.Runtime.InteropServices;

namespace IMPORTERSUB
{
    public interface ISetData
    {
        void SetSiwake(ref C_Siwake valcls);         // 仕訳情報
        void SetLink(ref C_Siwake_Link valcls);      // リンク情報
        void SetAiocrText(ref C_Aiocr_Text valcls);  // AI-OCR
    }

    // ここで付けられている属性はVBA上でコード補完を行うために必要です
    [ComVisible(true), ClassInterface(ClassInterfaceType.None)]
    public class C_SetData : ISetData
    {
        #region COM GUID
        // これらの GUID は、このクラスおよびその COM インターフェイスの COM ID を 
        // 指定します。この値を変更すると、 
        // 既存のクライアントはクラスにアクセスできなくなります。
        public const string ClassId     = "CC96F096-C397-4F83-8321-3CE7816EAC38";
        public const string InterfaceId = "B9C7DFB2-220D-4BB2-9F9F-4C29828D2837";
        public const string EventsId    = "3866A7BC-A1A5-4FEC-BB57-564A02F80D95";
        #endregion

        #region データテーブル
        private DataTable M_Dt;        // 仕訳保存用テーブル
        private DataTable M_Dt_Link;   // リンク情報テーブル
        private DataTable M_Dt_AIOCR;  // AI-OCRテーブル
        #endregion

        // テーブルレイアウト
        public C_SetData()
        {
            M_Dt = new DataTable("ZDATA");

            // テーブルレイアウトの作成
            M_Dt.Columns.Add("DYMD",    Type.GetType("System.Int32"));   // 伝票日付
            M_Dt.Columns.Add("SEIRI",   Type.GetType("System.Int16"));   // 整理月フラグ
            M_Dt.Columns.Add("DCNO",    Type.GetType("System.String"));  // 伝票番号
            M_Dt.Columns.Add("KYMD",    Type.GetType("System.Int32"));   // 起票年月日
            M_Dt.Columns.Add("KBMN",    Type.GetType("System.String"));  // 起票部門コード
            M_Dt.Columns.Add("KUSR",    Type.GetType("System.String"));  // 起票者コード
            M_Dt.Columns.Add("SGNO",    Type.GetType("System.Int32"));   // 承認グループNo
            M_Dt.Columns.Add("HF1",     Type.GetType("System.String"));  // ヘッダーフィールド１
            M_Dt.Columns.Add("HF2",     Type.GetType("System.String"));  // ヘッダーフィールド２
            M_Dt.Columns.Add("HF3",     Type.GetType("System.String"));  // ヘッダーフィールド３
            M_Dt.Columns.Add("HF4",     Type.GetType("System.String"));  // ヘッダーフィールド４
            M_Dt.Columns.Add("HF5",     Type.GetType("System.String"));  // ヘッダーフィールド５
            M_Dt.Columns.Add("HF6",     Type.GetType("System.String"));  // ヘッダーフィールド６
            M_Dt.Columns.Add("HF7",     Type.GetType("System.String"));  // ヘッダーフィールド７
            M_Dt.Columns.Add("HF8",     Type.GetType("System.String"));  // ヘッダーフィールド８
            M_Dt.Columns.Add("HF9",     Type.GetType("System.String"));  // ヘッダーフィールド９
            M_Dt.Columns.Add("HF10",    Type.GetType("System.String"));  // ヘッダーフィールド10
            M_Dt.Columns.Add("RBMN",    Type.GetType("System.String"));  // 借方部門コード
            M_Dt.Columns.Add("RTOR",    Type.GetType("System.String"));  // 借方取引先コード
            M_Dt.Columns.Add("RKMK",    Type.GetType("System.String"));  // 借方科目コード
            M_Dt.Columns.Add("REDA",    Type.GetType("System.String"));  // 借方枝番コード
            M_Dt.Columns.Add("RKOJ",    Type.GetType("System.String"));  // 借方工事コード
            M_Dt.Columns.Add("RKOS",    Type.GetType("System.String"));  // 借方工種コード
            M_Dt.Columns.Add("RPRJ",    Type.GetType("System.String"));  // 借方プロジェクトコード
            M_Dt.Columns.Add("RSEG",    Type.GetType("System.String"));  // 借方セグメントコード
            M_Dt.Columns.Add("RDM1",    Type.GetType("System.String"));  // 借方ユニバーサル１
            M_Dt.Columns.Add("RDM2",    Type.GetType("System.String"));  // 借方ユニバーサル２
            M_Dt.Columns.Add("RDM3",    Type.GetType("System.String"));  // 借方ユニバーサル３
            M_Dt.Columns.Add("RDM4",    Type.GetType("System.String"));  // 借方ユニバーサル４
            M_Dt.Columns.Add("RDM5",    Type.GetType("System.String"));  // 借方ユニバーサル５
            M_Dt.Columns.Add("RDM6",    Type.GetType("System.String"));  // 借方ユニバーサル６
            M_Dt.Columns.Add("RDM7",    Type.GetType("System.String"));  // 借方ユニバーサル７
            M_Dt.Columns.Add("RDM8",    Type.GetType("System.String"));  // 借方ユニバーサル８
            M_Dt.Columns.Add("RDM9",    Type.GetType("System.String"));  // 借方ユニバーサル９
            M_Dt.Columns.Add("RDM10",   Type.GetType("System.String"));  // 借方ユニバーサル10
            M_Dt.Columns.Add("RDM11",   Type.GetType("System.String"));  // 借方ユニバーサル11
            M_Dt.Columns.Add("RDM12",   Type.GetType("System.String"));  // 借方ユニバーサル12
            M_Dt.Columns.Add("RDM13",   Type.GetType("System.String"));  // 借方ユニバーサル13
            M_Dt.Columns.Add("RDM14",   Type.GetType("System.String"));  // 借方ユニバーサル14
            M_Dt.Columns.Add("RDM15",   Type.GetType("System.String"));  // 借方ユニバーサル15
            M_Dt.Columns.Add("RDM16",   Type.GetType("System.String"));  // 借方ユニバーサル16
            M_Dt.Columns.Add("RDM17",   Type.GetType("System.String"));  // 借方ユニバーサル17
            M_Dt.Columns.Add("RDM18",   Type.GetType("System.String"));  // 借方ユニバーサル18
            M_Dt.Columns.Add("RDM19",   Type.GetType("System.String"));  // 借方ユニバーサル19
            M_Dt.Columns.Add("RDM20",   Type.GetType("System.String"));  // 借方ユニバーサル20
            M_Dt.Columns.Add("RRIT",    Type.GetType("System.String"));  // 借方税率
            M_Dt.Columns.Add("RZKB",    Type.GetType("System.String"));  // 借方課税区分
            M_Dt.Columns.Add("RGYO",    Type.GetType("System.String"));  // 借方業種区分
            M_Dt.Columns.Add("RSRE",    Type.GetType("System.String"));  // 借方仕入区分
            M_Dt.Columns.Add("RTKY",    Type.GetType("System.String"));  // 借方摘要
            M_Dt.Columns.Add("RTNO",    Type.GetType("System.String"));  // 借方摘要コード
            M_Dt.Columns.Add("SBMN",    Type.GetType("System.String"));  // 貸方部門コード
            M_Dt.Columns.Add("STOR",    Type.GetType("System.String"));  // 貸方取引先コード
            M_Dt.Columns.Add("SKMK",    Type.GetType("System.String"));  // 貸方科目コード
            M_Dt.Columns.Add("SEDA",    Type.GetType("System.String"));  // 貸方枝番コード
            M_Dt.Columns.Add("SKOJ",    Type.GetType("System.String"));  // 貸方工事コード
            M_Dt.Columns.Add("SKOS",    Type.GetType("System.String"));  // 貸方工種コード
            M_Dt.Columns.Add("SPRJ",    Type.GetType("System.String"));  // 貸方プロジェクトコード
            M_Dt.Columns.Add("SSEG",    Type.GetType("System.String"));  // 貸方セグメントコード
            M_Dt.Columns.Add("SDM1",    Type.GetType("System.String"));  // 貸方ユニバーサル１
            M_Dt.Columns.Add("SDM2",    Type.GetType("System.String"));  // 貸方ユニバーサル２
            M_Dt.Columns.Add("SDM3",    Type.GetType("System.String"));  // 貸方ユニバーサル３
            M_Dt.Columns.Add("SDM4",    Type.GetType("System.String"));  // 貸方ユニバーサル４
            M_Dt.Columns.Add("SDM5",    Type.GetType("System.String"));  // 貸方ユニバーサル５
            M_Dt.Columns.Add("SDM6",    Type.GetType("System.String"));  // 貸方ユニバーサル６
            M_Dt.Columns.Add("SDM7",    Type.GetType("System.String"));  // 貸方ユニバーサル７
            M_Dt.Columns.Add("SDM8",    Type.GetType("System.String"));  // 貸方ユニバーサル８
            M_Dt.Columns.Add("SDM9",    Type.GetType("System.String"));  // 貸方ユニバーサル９
            M_Dt.Columns.Add("SDM10",   Type.GetType("System.String"));  // 貸方ユニバーサル10
            M_Dt.Columns.Add("SDM11",   Type.GetType("System.String"));  // 貸方ユニバーサル11
            M_Dt.Columns.Add("SDM12",   Type.GetType("System.String"));  // 貸方ユニバーサル12
            M_Dt.Columns.Add("SDM13",   Type.GetType("System.String"));  // 貸方ユニバーサル13
            M_Dt.Columns.Add("SDM14",   Type.GetType("System.String"));  // 貸方ユニバーサル14
            M_Dt.Columns.Add("SDM15",   Type.GetType("System.String"));  // 貸方ユニバーサル15
            M_Dt.Columns.Add("SDM16",   Type.GetType("System.String"));  // 貸方ユニバーサル16
            M_Dt.Columns.Add("SDM17",   Type.GetType("System.String"));  // 貸方ユニバーサル17
            M_Dt.Columns.Add("SDM18",   Type.GetType("System.String"));  // 貸方ユニバーサル18
            M_Dt.Columns.Add("SDM19",   Type.GetType("System.String"));  // 貸方ユニバーサル19
            M_Dt.Columns.Add("SDM20",   Type.GetType("System.String"));  // 貸方ユニバーサル20
            M_Dt.Columns.Add("SRIT",    Type.GetType("System.String"));  // 貸方税率
            M_Dt.Columns.Add("SZKB",    Type.GetType("System.String"));  // 貸方課税区分
            M_Dt.Columns.Add("SGYO",    Type.GetType("System.String"));  // 貸方業種区分
            M_Dt.Columns.Add("SSRE",    Type.GetType("System.String"));  // 貸方仕入区分
            M_Dt.Columns.Add("STKY",    Type.GetType("System.String"));  // 貸方摘要
            M_Dt.Columns.Add("STNO",    Type.GetType("System.String"));  // 貸方摘要コード
            M_Dt.Columns.Add("ZKMK",    Type.GetType("System.String"));  // (消)科目コード
            M_Dt.Columns.Add("ZRIT",    Type.GetType("System.String"));  // (消)税率
            M_Dt.Columns.Add("ZZKB",    Type.GetType("System.String"));  // (消)課税区分
            M_Dt.Columns.Add("ZGYO",    Type.GetType("System.String"));  // (消)業種区分
            M_Dt.Columns.Add("ZSRE",    Type.GetType("System.String"));  // (消)仕入区分
            M_Dt.Columns.Add("EXVL",    Type.GetType("System.Double"));  // 対価金額
            M_Dt.Columns.Add("VALU",    Type.GetType("System.Double"));  // 金額
            M_Dt.Columns.Add("SYMD",    Type.GetType("System.Int32"));   // 支払日
            M_Dt.Columns.Add("SKBN",    Type.GetType("System.String"));  // 支払区分
            M_Dt.Columns.Add("SKIZ",    Type.GetType("System.Int32"));   // 支払期日
            M_Dt.Columns.Add("UYMD",    Type.GetType("System.Int32"));   // 回収日
            M_Dt.Columns.Add("UKBN",    Type.GetType("System.String"));  // 入金区分
            M_Dt.Columns.Add("UKIZ",    Type.GetType("System.Int32"));   // 回収期日
            M_Dt.Columns.Add("DKEC",    Type.GetType("System.String"));  // 消込コード
            M_Dt.Columns.Add("FUSR",    Type.GetType("System.String"));  // 入力者コード
            M_Dt.Columns.Add("FSEN",    Type.GetType("System.Int16"));   // 付箋番号
            M_Dt.Columns.Add("TKFLG",   Type.GetType("System.Int16"));   // 貸借別摘要フラグ
            M_Dt.Columns.Add("BUNRI",   Type.GetType("System.String"));  // 分離区分
            M_Dt.Columns.Add("HEIC",    Type.GetType("System.String"));  // 幣種
            M_Dt.Columns.Add("RATE",    Type.GetType("System.String"));  // レート
            M_Dt.Columns.Add("GEXVL",   Type.GetType("System.String"));  // 外貨対価金額
            M_Dt.Columns.Add("GVALU",   Type.GetType("System.String"));  // 外貨金額

            // ↓ de3のみ ↓
            M_Dt.Columns.Add("TKY",     Type.GetType("System.String"));  // 摘要
            M_Dt.Columns.Add("TNO",     Type.GetType("System.String"));  // 摘要コード
            M_Dt.Columns.Add("STEN",    Type.GetType("System.Int16"));   // 店券フラグ
            // ↑ de3のみ ↑

            M_Dt.Columns.Add("GSEP",    Type.GetType("System.String"));  // 行区切り
            M_Dt.Columns.Add("LNO",     Type.GetType("System.Int32"));   // リンクNo
            M_Dt.Columns.Add("RKEIGEN", Type.GetType("System.Int16"));   // 借方 軽減税率区分
            M_Dt.Columns.Add("SKEIGEN", Type.GetType("System.Int16"));   // 貸方 軽減税率区分
            M_Dt.Columns.Add("ZKEIGEN", Type.GetType("System.Int16"));   // (消) 軽減税率区分

// Ver02.26.01 インボイス対応 --->
            M_Dt.Columns.Add("RURIZEIKEISAN", Type.GetType("System.Int16"));  // 借方　売上税額計算方式
            M_Dt.Columns.Add("SURIZEIKEISAN", Type.GetType("System.Int16"));  // 貸方　売上税額計算方式
            M_Dt.Columns.Add("ZURIZEIKEISAN", Type.GetType("System.Int16"));  // (消)　売上税額計算方式
            M_Dt.Columns.Add("RMENZEIKEIKA",  Type.GetType("System.Int16"));  // 借方　仕入税額控除経過措置割合
            M_Dt.Columns.Add("SMENZEIKEIKA",  Type.GetType("System.Int16"));  // 貸方　仕入税額控除経過措置割合
            M_Dt.Columns.Add("ZMENZEIKEIKA",  Type.GetType("System.Int16"));  // (消)　仕入税額控除経過措置割合
// <--- Ver02.26.01 インボイス対応

            // *-リンク情報
            M_Dt_Link = new DataTable("DLINK");

            M_Dt_Link.Columns.Add("LNO",      Type.GetType("System.Int32"));   // リンクNo
            M_Dt_Link.Columns.Add("LNAM",     Type.GetType("System.String"));  // リンク名称
            M_Dt_Link.Columns.Add("FLG1",     Type.GetType("System.Int16"));   // 形式
            M_Dt_Link.Columns.Add("LINK",     Type.GetType("System.String"));  // リンク先
            M_Dt_Link.Columns.Add("EDOC",     Type.GetType("System.String"));  // e文書番号
            M_Dt_Link.Columns.Add("NUSR",     Type.GetType("System.String"));  // 申請者名称
            M_Dt_Link.Columns.Add("SUSR",     Type.GetType("System.String"));  // 最終承認者名称
            M_Dt_Link.Columns.Add("SYUBETSU", Type.GetType("System.Int16"));   // 書類種別
            M_Dt_Link.Columns.Add("SYMD",     Type.GetType("System.Int32"));   // 日付
            M_Dt_Link.Columns.Add("SVALU",    Type.GetType("System.Int64"));   // 金額
            M_Dt_Link.Columns.Add("STRNAM",   Type.GetType("System.String"));  // 発行者名称
            M_Dt_Link.Columns.Add("HINMEI",   Type.GetType("System.String"));  // 品名
            M_Dt_Link.Columns.Add("BIKO",     Type.GetType("System.String"));  // 備考

            // *- AI-OCR
            M_Dt_AIOCR = new DataTable("AIOCRTEXT");
            var _with3 = M_Dt_AIOCR.Columns;
            M_Dt_AIOCR.Columns.Add("EDOC",    Type.GetType("System.String"));  // e文書番号
            M_Dt_AIOCR.Columns.Add("ITMID",   Type.GetType("System.String"));  // 項目ID
            M_Dt_AIOCR.Columns.Add("ITMODR",  Type.GetType("System.Int16"));   // 順序
            M_Dt_AIOCR.Columns.Add("OCRTEXT", Type.GetType("System.String"));  // テキスト
        }

//	      protected override void Finalize()
//        {
//            base.Finalize();
//            M_Dt.Dispose();
//            M_Dt_Link.Dispose();
//            M_Dt_AIOCR.Dispose();
//        }
        // デストラクター
        ~C_SetData()
        {
        }

        // 仕訳情報
        public void SetSiwake(ref C_Siwake valcls)
        {
            DataRow L_Row = null;
            L_Row = M_Dt.NewRow();

            // クラスで受け取った値をそのままテーブルに保存
            L_Row["DYMD"]          = valcls.DYMD;
            L_Row["SEIRI"]         = valcls.SEIRI;
            L_Row["DCNO"]          = valcls.DCNO;
            L_Row["KYMD"]          = valcls.KYMD;
            L_Row["KBMN"]          = valcls.KBMN;
            L_Row["KUSR"]          = valcls.KUSR;
            L_Row["SGNO"]          = valcls.SGNO;
            L_Row["HF1"]           = valcls.HF1;
            L_Row["HF2"]           = valcls.HF2;
            L_Row["HF3"]           = valcls.HF3;
            L_Row["HF4"]           = valcls.HF4;
            L_Row["HF5"]           = valcls.HF5;
            L_Row["HF6"]           = valcls.HF6;
            L_Row["HF7"]           = valcls.HF7;
            L_Row["HF8"]           = valcls.HF8;
            L_Row["HF9"]           = valcls.HF9;
            L_Row["HF10"]          = valcls.HF10;
            L_Row["RBMN"]          = valcls.RBMN;
            L_Row["RTOR"]          = valcls.RTOR;
            L_Row["RKMK"]          = valcls.RKMK;
            L_Row["REDA"]          = valcls.REDA;
            L_Row["RKOJ"]          = valcls.RKOJ;
            L_Row["RKOS"]          = valcls.RKOS;
            L_Row["RPRJ"]          = valcls.RPRJ;
            L_Row["RSEG"]          = valcls.RSEG;
            L_Row["RDM1"]          = valcls.RDM1;
            L_Row["RDM2"]          = valcls.RDM2;
            L_Row["RDM3"]          = valcls.RDM3;
            L_Row["RDM4"]          = valcls.RDM4;
            L_Row["RDM5"]          = valcls.RDM5;
            L_Row["RDM6"]          = valcls.RDM6;
            L_Row["RDM7"]          = valcls.RDM7;
            L_Row["RDM8"]          = valcls.RDM8;
            L_Row["RDM9"]          = valcls.RDM9;
            L_Row["RDM10"]         = valcls.RDM10;
            L_Row["RDM11"]         = valcls.RDM11;
            L_Row["RDM12"]         = valcls.RDM12;
            L_Row["RDM13"]         = valcls.RDM13;
            L_Row["RDM14"]         = valcls.RDM14;
            L_Row["RDM15"]         = valcls.RDM15;
            L_Row["RDM16"]         = valcls.RDM16;
            L_Row["RDM17"]         = valcls.RDM17;
            L_Row["RDM18"]         = valcls.RDM18;
            L_Row["RDM19"]         = valcls.RDM19;
            L_Row["RDM20"]         = valcls.RDM20;
            L_Row["RRIT"]          = valcls.RRIT;
            L_Row["RZKB"]          = valcls.RZKB;
            L_Row["RGYO"]          = valcls.RGYO;
            L_Row["RSRE"]          = valcls.RSRE;
            L_Row["RTKY"]          = valcls.RTKY;
            L_Row["RTNO"]          = valcls.RTNO;
            L_Row["SBMN"]          = valcls.SBMN;
            L_Row["STOR"]          = valcls.STOR;
            L_Row["SKMK"]          = valcls.SKMK;
            L_Row["SEDA"]          = valcls.SEDA;
            L_Row["SKOJ"]          = valcls.SKOJ;
            L_Row["SKOS"]          = valcls.SKOS;
            L_Row["SPRJ"]          = valcls.SPRJ;
            L_Row["SSEG"]          = valcls.SSEG;
            L_Row["SDM1"]          = valcls.SDM1;
            L_Row["SDM2"]          = valcls.SDM2;
            L_Row["SDM3"]          = valcls.SDM3;
            L_Row["SDM4"]          = valcls.SDM4;
            L_Row["SDM5"]          = valcls.SDM5;
            L_Row["SDM6"]          = valcls.SDM6;
            L_Row["SDM7"]          = valcls.SDM7;
            L_Row["SDM8"]          = valcls.SDM8;
            L_Row["SDM9"]          = valcls.SDM9;
            L_Row["SDM10"]         = valcls.SDM10;
            L_Row["SDM11"]         = valcls.SDM11;
            L_Row["SDM12"]         = valcls.SDM12;
            L_Row["SDM13"]         = valcls.SDM13;
            L_Row["SDM14"]         = valcls.SDM14;
            L_Row["SDM15"]         = valcls.SDM15;
            L_Row["SDM16"]         = valcls.SDM16;
            L_Row["SDM17"]         = valcls.SDM17;
            L_Row["SDM18"]         = valcls.SDM18;
            L_Row["SDM19"]         = valcls.SDM19;
            L_Row["SDM20"]         = valcls.SDM20;
            L_Row["SRIT"]          = valcls.SRIT;
            L_Row["SZKB"]          = valcls.SZKB;
            L_Row["SGYO"]          = valcls.SGYO;
            L_Row["SSRE"]          = valcls.SSRE;
            L_Row["STKY"]          = valcls.STKY;
            L_Row["STNO"]          = valcls.STNO;
            L_Row["ZKMK"]          = valcls.ZKMK;
            L_Row["ZRIT"]          = valcls.ZRIT;
            L_Row["ZZKB"]          = valcls.ZZKB;
            L_Row["ZGYO"]          = valcls.ZGYO;
            L_Row["ZSRE"]          = valcls.ZSRE;
            L_Row["EXVL"]          = valcls.EXVL;
            L_Row["VALU"]          = valcls.VALU;
            L_Row["SYMD"]          = valcls.SYMD;
            L_Row["SKBN"]          = valcls.SKBN;
            L_Row["SKIZ"]          = valcls.SKIZ;
            L_Row["UYMD"]          = valcls.UYMD;
            L_Row["UKBN"]          = valcls.UKBN;
            L_Row["UKIZ"]          = valcls.UKIZ;
            L_Row["DKEC"]          = valcls.DKEC;
            L_Row["FUSR"]          = valcls.FUSR;
            L_Row["FSEN"]          = valcls.FSEN;
            L_Row["TKFLG"]         = valcls.TKFLG;
            L_Row["BUNRI"]         = valcls.BUNRI;
            L_Row["HEIC"]          = valcls.HEIC;
            L_Row["RATE"]          = valcls.RATE;
            L_Row["GEXVL"]         = valcls.GEXVL;
            L_Row["GVALU"]         = valcls.GVALU;
            // ↓ de3専用 ↓
            L_Row["TKY"]           = valcls.TKY;
            L_Row["TNO"]           = valcls.TNO;
            L_Row["STEN"]          = valcls.STEN;
            // ↑ de3専用 ↑
            L_Row["GSEP"]          = valcls.GSEP;
            L_Row["LNO"]           = valcls.LNO;
            L_Row["RKEIGEN"]       = valcls.RKEIGEN;
            L_Row["SKEIGEN"]       = valcls.SKEIGEN;
            L_Row["ZKEIGEN"]       = valcls.ZKEIGEN;

            L_Row["RURIZEIKEISAN"] = valcls.RURIZEIKEISAN;
            L_Row["SURIZEIKEISAN"] = valcls.SURIZEIKEISAN;
            L_Row["ZURIZEIKEISAN"] = valcls.ZURIZEIKEISAN;
            L_Row["RMENZEIKEIKA"]  = valcls.RMENZEIKEIKA;
            L_Row["SMENZEIKEIKA"]  = valcls.SMENZEIKEIKA;
            L_Row["ZMENZEIKEIKA"]  = valcls.ZMENZEIKEIKA;

            M_Dt.Rows.Add(L_Row);
        }

        internal DataTable GetSiwakeDt()
        {
            return M_Dt;
        }

        // リンク情報
        public void SetLink(ref C_Siwake_Link valcls)
        {
            DataRow L_Row = null;
            L_Row = M_Dt_Link.NewRow();

            // クラスで受け取った値をそのままテーブルに保存
            L_Row["LNO"]      = valcls.LNO;
            L_Row["LNAM"]     = valcls.LNAM;
            L_Row["FLG1"]     = valcls.FLG1;
            L_Row["LINK"]     = valcls.LINK;
            L_Row["EDOC"]     = valcls.EDOC;
            L_Row["NUSR"]     = valcls.NUSR;
            L_Row["SUSR"]     = valcls.SUSR;
            L_Row["SYUBETSU"] = valcls.SYUBETSU;
            // 仮対応
            //L_Row["SYMD"]     = valcls.SYMD;
            //L_Row["SVALU"]    = valcls.SVALU;
            if(valcls.SYMD == null)
            {
                L_Row["SYMD"] = DBNull.Value;
            }
            else
            {
                L_Row["SYMD"] = valcls.SYMD;
            }
            if (valcls.SVALU == null)
            {
                L_Row["SVALU"] = DBNull.Value;
            }
            else
            {
                L_Row["SVALU"] = valcls.SVALU;
            }

            L_Row["STRNAM"]   = valcls.STRNAM;
            L_Row["HINMEI"]   = valcls.HINMEI;
            L_Row["BIKO"]     = valcls.BIKO;

            M_Dt_Link.Rows.Add(L_Row);
        }

        internal DataTable GetLinkDt()
        {
            return M_Dt_Link;
        }

        // AI-OCR
        public void SetAiocrText(ref C_Aiocr_Text valcls)
        {
            DataRow L_Row = null;
            L_Row = M_Dt_AIOCR.NewRow();

            // クラスで受け取った値をそのままテーブルに保存
            L_Row["EDOC"]    = valcls.EDOC;
            L_Row["ITMID"]   = valcls.ITMID;
            L_Row["ITMODR"]  = valcls.ITMODR;
            L_Row["OCRTEXT"] = valcls.OCRTEXT;

            M_Dt_AIOCR.Rows.Add(L_Row);
        }

        internal DataTable GetAIOCRDt()
        {
            return M_Dt_AIOCR;
        }
    }
}