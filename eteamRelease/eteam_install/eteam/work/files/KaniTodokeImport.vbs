'--------------------
'������
'--------------------
Set sh = WScript.CreateObject("Shell.Application")
Set fso = WScript.CreateObject("Scripting.FileSystemObject")
Set ws = CreateObject("WScript.Shell")
count = 0

'--------------------
'�X�L�[�}����͂���
'--------------------
schemaName = InputBox("�X�L�[�}������͂��Ă��������B" , "�X�L�[�}��")
If schemaName = "" Then
	MsgBox "�I�����܂�"
	WScript.Quit -9
End If

'--------------------
'�P�ꂩ�t�H���_��I��
'--------------------
tani = InputBox("�C���|�[�g�P�ʂ�I�����Ă��������B" & vbLf & "1:�t�@�C���P�́B2:�t�H���_�ꊇ�B", "�C���|�[�g�P��")
If tani = "�P" Then
	tani = "1"
ElseIf tani = "�Q" Then
	tani = "2"
End If
If Not(tani = "1" Or tani = "2") Then
	MsgBox "�I�����܂�"
	WScript.Quit -9
End If

'--------------------
'�t�@�C���܂��̓t�H���_��I��
'--------------------
If tani = "1" Then
	'�t�@�C���I������
	file = SelectFile
	If file = "" Then
		MsgBox "�I�����܂�"
		WScript.Quit -9
	End If
	If LCase(fso.GetExtensionName(file)) <> "json" Then
		MsgBox "JSON�t�@�C���ȊO�̓C���|�[�g�ł��܂���B"
		WScript.Quit -9
	End If
ElseIf tani = "2" Then
	file = SelectFolder()
	If file = "" Then
		MsgBox "�I�����܂�"
		WScript.Quit -9
	End If
End If

'--------------------
'�m�F����
'--------------------
usersJudge = MsgBox("�C���|�[�g���܂��B" & vbLf & "�@�X�L�[�}���F" & schemaName & vbLf & "�@�t�@�C��(�t�H���_)�F" & file, vbOKCancel)
If usersJudge <> vbOK Then
	MsgBox "�I�����܂�"
	WScript.Quit -9
End If

'--------------------
'���s����
'--------------------
retCd = Import(schemaName, file)
If int(retCd) <> 0 Then
	WScript.Quit -1
End If

'--------------------
'���킾����
'--------------------
WScript.Quit 0


'�ȉ��͋��ʏ���


'--------------------
'�t�@�C���I������
'@return	�t�@�C���p�X�B���I���Ȃ�u�����N
'--------------------
Function SelectFile()
  Set Ie = CreateObject("InternetExplorer.Application")

  ' �ϐ��錾
  FilePath = "" ' �t�@�C���p�X

  ' IE�I�u�W�F�N�g��p��
  Ie.Navigate( "about:blank" )
  Ie.document.getElementsByTagName("BODY")(0).innerHTML = _
    "<INPUT id=FilePath type=file><TEXTAREA id=Text></TEXTAREA>"
  Ie.document.getElementById("FilePath").click

  ' �I���t�@�C���p�X�i�[
  FilePath = Ie.document.getElementById("FilePath").value

  ' �I���t�@�C���p�X�`�F�b�N
  If FilePath = "" then
    SelectFile = ""
    Exit Function
  end If

  ' IE8�̏ꍇ�̓Z�L�����e�B�̊֌W��A
  ' �uC:\fakepath�v�ƂȂ�̂ŁA�ʏ����擾
  If InStr( FilePath, ":\fakepath\" ) = 2 Then
    ' �R�s�[&�y�[�X�g�p�萔�p��
    Const OLECMDID_COPY = 12
    Const OLECMDID_PASTE = 13
    Const OLECMDID_SELECTALL = 17 '(&H11)
    Const OLECMDEXECOPT_DODEFAULT = 0
    ' FilePath�̓��e���R�s�[���AText�փy�[�X�g
    Ie.Document.all.FilePath.focus
    Ie.ExecWB OLECMDID_SELECTALL,OLECMDEXECOPT_DODEFAULT
    Ie.ExecWB OLECMDID_COPY,OLECMDEXECOPT_DODEFAULT
    Ie.Document.all.Text.focus
    Ie.ExecWB OLECMDID_PASTE,OLECMDEXECOPT_DODEFAULT
    FilePath = Ie.Document.all.Text.value
  End If

  ' Ie�j��
  Ie.Quit
  Set Ie = Nothing

  ' �ŏI�I�Ƀt�@�C���p�X�擾
  SelectFile = FilePath
End Function

'--------------------
'�t�H���_�I������
'@return	�t�H���_�p�X�B���I���Ȃ�u�����N
'--------------------
Function SelectFolder()
	Set objFolder = sh.BrowseForFolder(0,"�t�H���_��I�����ĉ�����",&1)
	If objFolder Is Nothing Then
		SelectFolder = ""
	Else
		SelectFolder = objFolder.Self.path
	End If
End Function

'--------------------
'�C���|�[�g����
'--------------------
Function Import(s, f)
	cmd = "cmd /c java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED -cp C:\\eteam\\bat\\bin\\eteam.jar;c:\\eteam\\bat\\lib\* eteam.gyoumu.kanitodoke.KaniTodokeImportBat """ & s & """ """ & f & """"
	Set oExec = ws.Exec(cmd)
	execOut = oExec.StdOut.ReadAll
	execErr = oExec.StdErr.ReadAll
	retCd = oExec.ExitCode 
	If retCd = 0 Then
		MsgBox "����ɃC���|�[�g���܂����B" & vbLf & execOut & vbLf & execErr
		cmd = "c:\\eteam\\bat\\bin\\NormalEndBatch.bat KaniTodokeImportBat"
		ws.Run cmd, 1, true 
	Else
		MsgBox "�C���|�[�g���ɃG���[�����B" & vbLf & execOut & vbLf & execErr
		cmd = "c:\\eteam\\bat\\bin\\ErrorBatch.bat KaniTodokeImportBat"
		ws.Run cmd, 1, true
	End If
	Import = retCd
End Function
