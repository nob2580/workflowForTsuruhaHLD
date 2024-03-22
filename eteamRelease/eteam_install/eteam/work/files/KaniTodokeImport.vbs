'--------------------
'初期化
'--------------------
Set sh = WScript.CreateObject("Shell.Application")
Set fso = WScript.CreateObject("Scripting.FileSystemObject")
Set ws = CreateObject("WScript.Shell")
count = 0

'--------------------
'スキーマを入力する
'--------------------
schemaName = InputBox("スキーマ名を入力してください。" , "スキーマ名")
If schemaName = "" Then
	MsgBox "終了します"
	WScript.Quit -9
End If

'--------------------
'単一かフォルダを選択
'--------------------
tani = InputBox("インポート単位を選択してください。" & vbLf & "1:ファイル単体。2:フォルダ一括。", "インポート単位")
If tani = "１" Then
	tani = "1"
ElseIf tani = "２" Then
	tani = "2"
End If
If Not(tani = "1" Or tani = "2") Then
	MsgBox "終了します"
	WScript.Quit -9
End If

'--------------------
'ファイルまたはフォルダを選択
'--------------------
If tani = "1" Then
	'ファイル選択して
	file = SelectFile
	If file = "" Then
		MsgBox "終了します"
		WScript.Quit -9
	End If
	If LCase(fso.GetExtensionName(file)) <> "json" Then
		MsgBox "JSONファイル以外はインポートできません。"
		WScript.Quit -9
	End If
ElseIf tani = "2" Then
	file = SelectFolder()
	If file = "" Then
		MsgBox "終了します"
		WScript.Quit -9
	End If
End If

'--------------------
'確認して
'--------------------
usersJudge = MsgBox("インポートします。" & vbLf & "　スキーマ名：" & schemaName & vbLf & "　ファイル(フォルダ)：" & file, vbOKCancel)
If usersJudge <> vbOK Then
	MsgBox "終了します"
	WScript.Quit -9
End If

'--------------------
'実行する
'--------------------
retCd = Import(schemaName, file)
If int(retCd) <> 0 Then
	WScript.Quit -1
End If

'--------------------
'正常だった
'--------------------
WScript.Quit 0


'以下は共通処理


'--------------------
'ファイル選択する
'@return	ファイルパス。未選択ならブランク
'--------------------
Function SelectFile()
  Set Ie = CreateObject("InternetExplorer.Application")

  ' 変数宣言
  FilePath = "" ' ファイルパス

  ' IEオブジェクトを用意
  Ie.Navigate( "about:blank" )
  Ie.document.getElementsByTagName("BODY")(0).innerHTML = _
    "<INPUT id=FilePath type=file><TEXTAREA id=Text></TEXTAREA>"
  Ie.document.getElementById("FilePath").click

  ' 選択ファイルパス格納
  FilePath = Ie.document.getElementById("FilePath").value

  ' 選択ファイルパスチェック
  If FilePath = "" then
    SelectFile = ""
    Exit Function
  end If

  ' IE8の場合はセキュリティの関係上、
  ' 「C:\fakepath」となるので、別処理取得
  If InStr( FilePath, ":\fakepath\" ) = 2 Then
    ' コピー&ペースト用定数用意
    Const OLECMDID_COPY = 12
    Const OLECMDID_PASTE = 13
    Const OLECMDID_SELECTALL = 17 '(&H11)
    Const OLECMDEXECOPT_DODEFAULT = 0
    ' FilePathの内容をコピーし、Textへペースト
    Ie.Document.all.FilePath.focus
    Ie.ExecWB OLECMDID_SELECTALL,OLECMDEXECOPT_DODEFAULT
    Ie.ExecWB OLECMDID_COPY,OLECMDEXECOPT_DODEFAULT
    Ie.Document.all.Text.focus
    Ie.ExecWB OLECMDID_PASTE,OLECMDEXECOPT_DODEFAULT
    FilePath = Ie.Document.all.Text.value
  End If

  ' Ie破棄
  Ie.Quit
  Set Ie = Nothing

  ' 最終的にファイルパス取得
  SelectFile = FilePath
End Function

'--------------------
'フォルダ選択する
'@return	フォルダパス。未選択ならブランク
'--------------------
Function SelectFolder()
	Set objFolder = sh.BrowseForFolder(0,"フォルダを選択して下さい",&1)
	If objFolder Is Nothing Then
		SelectFolder = ""
	Else
		SelectFolder = objFolder.Self.path
	End If
End Function

'--------------------
'インポートする
'--------------------
Function Import(s, f)
	cmd = "cmd /c java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED -cp C:\\eteam\\bat\\bin\\eteam.jar;c:\\eteam\\bat\\lib\* eteam.gyoumu.kanitodoke.KaniTodokeImportBat """ & s & """ """ & f & """"
	Set oExec = ws.Exec(cmd)
	execOut = oExec.StdOut.ReadAll
	execErr = oExec.StdErr.ReadAll
	retCd = oExec.ExitCode 
	If retCd = 0 Then
		MsgBox "正常にインポートしました。" & vbLf & execOut & vbLf & execErr
		cmd = "c:\\eteam\\bat\\bin\\NormalEndBatch.bat KaniTodokeImportBat"
		ws.Run cmd, 1, true 
	Else
		MsgBox "インポート中にエラー発生。" & vbLf & execOut & vbLf & execErr
		cmd = "c:\\eteam\\bat\\bin\\ErrorBatch.bat KaniTodokeImportBat"
		ws.Run cmd, 1, true
	End If
	Import = retCd
End Function
