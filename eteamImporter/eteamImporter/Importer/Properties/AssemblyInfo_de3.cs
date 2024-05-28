using System.Reflection;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;

// アセンブリに関する一般情報は以下の属性セットをとおして制御されます。
// アセンブリに関連付けられている情報を変更するには、
// これらの属性値を変更してください。
[assembly: AssemblyTitle("Importer_de3")]
[assembly: AssemblyDescription("")]
[assembly: AssemblyConfiguration("")]
[assembly: AssemblyCompany("Microsoft")]
[assembly: AssemblyProduct("Importer_de3")]
[assembly: AssemblyCopyright("Copyright © Microsoft 2014")]
[assembly: AssemblyTrademark("")]
[assembly: AssemblyCulture("")]

// ComVisible を false に設定すると、その型はこのアセンブリ内で COM コンポーネントから 
// 参照不可能になります。COM からこのアセンブリ内の型にアクセスする場合は、
// その型の ComVisible 属性を true に設定してください。
[assembly: ComVisible(false)]

// 次の GUID は、このプロジェクトが COM に公開される場合の、typelib の ID です
[assembly: Guid("c455698e-35d2-468d-bd3f-cc9af9786adb")]

// アセンブリのバージョン情報は、以下の 4 つの値で構成されています:
//
//      Major Version
//      Minor Version 
//      Build Number
//      Revision
//
// すべての値を指定するか、下のように '*' を使ってビルドおよびリビジョン番号を 
// 既定値にすることができます:
// [assembly: AssemblyVersion("1.0.*")]
[assembly: AssemblyVersion("1.0.0.0")]
[assembly: AssemblyFileVersion("01.02.04")]

#region de3変更履歴
// 2022/11/11   Ver01.01.06     仕入区分 0:無し → "" 対応       #117291対応
// 2022/11/24   Ver01.01.07     Ver01.01.06対応にて、仕入区分・入金区分の　0　→　""（空白）　変換で変換エラー対応
// 2023/05/15   Ver01.02.01     令和５年インボイス対応
// 2023/06/19   Ver01.02.02     伝票日付が2023/10/01以前の場合、旧伝票として連携
// 2023/07/11   Ver01.02.03     旧レイアウトの考慮もれ
// 2023/11/14   Ver01.02.04     軽減税率の半角スペース変換エラー対応　#122753
#endregion
