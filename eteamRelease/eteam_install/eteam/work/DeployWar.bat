rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0


rem ��
cd c:\eteam\work\files
rmdir /s /q eteam
mkdir eteam
cd eteam
"C:\Program Files\Java\jdk-11\bin\jar" xvf ../eteam.war
cd ..

rem server.xml�̃|�[�g�ԍ���������(WF_POSTGRES_PORT�ɂ�set_pgport�Ŏ擾�����l���Z�b�g�����)
set WF_BEFPORT=5432
set ORG_FOLD=C:\Program Files\Apache Software Foundation\Tomcat 9.0\conf
copy "%ORG_FOLD%\server.xml" c:\eteam\tmp\
set INP_FILE=c:\eteam\tmp\server.xml
set OUT_FILE=c:\eteam\tmp\server.xml.tmp
set WF_BEFADR=localhost:%WF_BEFPORT%
set WF_CHGADR=localhost:%WF_POSTGRES_PORT%
if exist %OUT_FILE% del /Q %OUT_FILE%
setlocal enabledelayedexpansion
for /f "delims=" %%a in (%INP_FILE%) do (
set line=%%a
echo !line:%WF_BEFADR%=%WF_CHGADR%!>>%OUT_FILE%
)
del /Q %INP_FILE%
copy /Y %OUT_FILE% "%ORG_FOLD%\server.xml"
del /Q %OUT_FILE%


rem �ÓI�t�@�C���f�v���C
rmdir /s /q C:\Apache24\htdocs\eteam
mkdir C:\Apache24\htdocs\eteam\static
xcopy /D /E /R /Y /I eteam\static C:\Apache24\htdocs\eteam\static
mkdir C:\Apache24\htdocs\eteam\static_ext
if exist eteam\static_ext (xcopy /D /E /R /Y /I eteam\static_ext C:\Apache24\htdocs\eteam\static_ext)


rem WAR�f�v���C
rmdir /s /q "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\eteam"
xcopy /D /E /R /Y /I eteam "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\eteam"


pause
