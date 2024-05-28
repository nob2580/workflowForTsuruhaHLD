using System.Reflection;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;

// アセンブリに関する一般情報は以下の属性セットをとおして制御されます。
// アセンブリに関連付けられている情報を変更するには、
// これらの属性値を変更してください。
[assembly: AssemblyTitle("Importer.exe")]
[assembly: AssemblyDescription("IMPORTERSUB.dll用WFからの呼び出しexeプログラム")]
[assembly: AssemblyConfiguration("")]
[assembly: AssemblyCompany("株式会社ＩＣＳパートナーズ")]
[assembly: AssemblyProduct("OPEN21 SIAS")]
[assembly: AssemblyCopyright("Copyright© 2015 株式会社ＩＣＳパートナーズ")]
[assembly: AssemblyTrademark("OPEN21®")]
[assembly: AssemblyCulture("")]

// ComVisible を false に設定すると、その型はこのアセンブリ内で COM コンポーネントから 
// 参照不可能になります。COM からこのアセンブリ内の型にアクセスする場合は、 
// その型の ComVisible 属性を true に設定してください。
[assembly: ComVisible(false)]

// このプロジェクトが COM に公開される場合、次の GUID が typelib の ID になります
[assembly: Guid("e007b921-7f5a-40dc-930c-97de51df869b")]

// アセンブリのバージョン情報は次の 4 つの値で構成されています:
//
//      メジャー バージョン
//      マイナー バージョン
//      ビルド番号
//      Revision
//
[assembly: AssemblyVersion("1.0.0.0")]
[assembly: AssemblyFileVersion("01.02.04.03")]

#region 変更履歴
// 01.01.05     2022/05/13      e文書検索項目（年月日・金額・発行者名称）の任意化対応
// 01.01.06     2022/11/11      仕入区分 0:無し → "" 対応       #117291対応
// 01.02.01     2023/05/08      インボイス対応版
// 01.02.02     2023/05/23      インボイス対応版不具合修正（旧レイアウト(RNo=1)のレイアウト修正）
// 01.02.03     2023/06/19      インボイス伝票で2023/10/01以前の伝票の場合、旧伝票とする対応
// 01.02.04     2023/11/14      軽減税率の半角スペース変換エラー対応　#122753
// 01.02.04.01  2024/03/29      自動分離した仮払消費税・仮受消費税科目に固定部門を付加する対応＝起動パラメータ１項目追加　←　ツルハホールディングス様用カスタマイズ
// 01.02.04.02  2024/05/03      外部URL連携(カスタマイズ)時、URLにファイルパスが連結されてしまう仕様漏れ対応
// 01.02.04.03  2024/05/28      当期に部門科目がなく、翌期に部門科目があり、当期の伝票連携時にプライマリキーエラー　→　期ごとに登録するように修正
#endregion
