@echo off


rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd "%~dp0"

rem WEB��~ 
call ..\work\stop.bat
cd "%~dp0"

rem timeout 10 /nobreak >null
echo �ݒ���e���m�F���B���X���̂܂܂ł��҂����������I
timeout 30 /nobreak >null

rem �S�X�L�[�}���擾�E�X�L�[�}�P�ʃ��[�v
"c:\program files\postgresql\11\bin\psql.exe" -U eteam -d eteam -t -c "SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT LIKE 'pg_%%' AND nspname NOT LIKE 'information_%%' AND nspname <> 'public'" > schema_list_upd.txt
for /f %%L in (schema_list_upd.txt) do (
    call :SCHEMAUPDATE %%L
)
rem �X�L�[�}�����X�g�t�@�C���폜
del schema_list_upd.txt
pause


rem JAR�t�@�C���̍ŐV��

rem �@�Â�JAR�͍폜

DEL /F "C:\eteam\bat\lib\commons-dbutils-1.5.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-fileupload-1.3.1.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-io-2.2.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-lang3-3.1.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-logging-1.1.3.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-validator-1.3.1.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\freemarker-2.3.19.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\ognl-3.0.6.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-convention-plugin-2.3.16.3.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-core-2.3.16.3.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\taglibs-standard-compat-1.2.1.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\taglibs-standard-impl-1.2.1.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\taglibs-standard-jstlel-1.2.1.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\taglibs-standard-spec-1.2.1.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\xwork-core-2.3.16.3.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-convention-plugin-2.3.30.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-core-2.3.30.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\xwork-core-2.3.30.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-convention-plugin-2.3.31.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-core-2.3.31.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\xwork-core-2.3.31.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\javassist-3.11.0.GA.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\s2-extension-2.4.47.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\s2-framework-2.4.47.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\s2-tiger-2.4.47.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-dbutils-1.6.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-fileupload-1.3.2.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-io-2.5.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-lang3-3.2.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-validator-1.5.1.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\freemarker-2.3.22.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\ognl-3.0.19.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\pdfbox-2.0.1.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-convention-plugin-2.3.32.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-core-2.3.32.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\xwork-core-2.3.32.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\freemarker-2.3.26-incubating.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\lombok-1.2.6.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\ognl-3.0.21.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-convention-plugin-2.3.35.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-core-2.3.35.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\xwork-core-2.3.35.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\sqljdbc4.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\asm-3.3.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\asm-commons-3.3.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\asm-tree-3.3.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-fileupload-1.3.3.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\commons-lang3-3.8.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\log4j-api-2.10.0.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\log4j-api-2.12.1.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\ognl-3.1.15.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-convention-plugin-2.5.17.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-core-2.5.17.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\freemarker-2.3.28.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\ognl-3.1.28.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\log4j-api-2.16.0.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\log4j-api-2.17.0.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-convention-plugin-2.5.26.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-core-2.5.26.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\freemarker-2.3.30.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-convention-plugin-2.5.29.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\struts2-core-2.5.29.jar" > NUL 2>&1
DEL /F "C:\eteam\bat\lib\postgresql-9.3-1101.jdbc4.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-dbutils-1.5.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-fileupload-1.3.1.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-io-2.2.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-lang3-3.1.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-logging-1.1.3.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-validator-1.3.1.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\freemarker-2.3.19.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\ognl-3.0.6.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-convention-plugin-2.3.16.3.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-core-2.3.16.3.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\taglibs-standard-compat-1.2.1.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\taglibs-standard-impl-1.2.1.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\taglibs-standard-jstlel-1.2.1.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\taglibs-standard-spec-1.2.1.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\xwork-core-2.3.16.3.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-convention-plugin-2.3.30.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-core-2.3.30.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\xwork-core-2.3.30.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-convention-plugin-2.3.31.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-core-2.3.31.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\xwork-core-2.3.31.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\javassist-3.11.0.GA.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\s2-extension-2.4.47.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\s2-framework-2.4.47.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\s2-tiger-2.4.47.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-dbutils-1.6.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-fileupload-1.3.2.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-io-2.5.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-lang3-3.2.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-validator-1.5.1.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\freemarker-2.3.22.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\ognl-3.0.19.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\pdfbox-2.0.1.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-convention-plugin-2.3.32.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-core-2.3.32.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\xwork-core-2.3.32.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\freemarker-2.3.26-incubating.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\lombok-1.2.6.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\ognl-3.0.21.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-convention-plugin-2.3.35.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-core-2.3.35.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\xwork-core-2.3.35.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\sqljdbc4.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\asm-3.3.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\asm-commons-3.3.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\asm-tree-3.3.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-fileupload-1.3.3.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\commons-lang3-3.8.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\log4j-api-2.10.0.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\log4j-api-2.12.1.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\ognl-3.1.15.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-convention-plugin-2.5.17.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-core-2.5.17.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\freemarker-2.3.28.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\ognl-3.1.28.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\log4j-api-2.16.0.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\log4j-api-2.17.0.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-convention-plugin-2.5.26.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-core-2.5.26.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\freemarker-2.3.30.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-convention-plugin-2.5.29.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\struts2-core-2.5.29.jar" > NUL 2>&1
DEL /F "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib\postgresql-9.3-1101.jdbc4.jar" > NUL 2>&1

rem �ASQL Anywhere ���W���[���R�s�[
copy /Y "C:\Program Files\ICSP\OPENde3\Sybase\SQL Anywhere 12\Java\jodbc.jar" "C:\eteam\bat\lib\jodbc.jar"

rem �BJAR��TomcatLib�փR�s�[
copy /Y "c:\eteam\bat\lib\*" "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib"

rem �Â��t�@�C�����폜
rmdir /s /q "C:\eteam\bat\bin\bef" > NUL 2>&1
rmdir /s /q "C:\eteam\bat\bin\mk2" > NUL 2>&1


rem WEB�f�v���C �J�n
call ..\work\DeployWar.bat


cd "%~dp0"
call ..\work\start.bat
cd "%~dp0"
exit /b 0


:SCHEMAUPDATE
rem �����ŃX�L�[�}�����w�肳��Ă��邩�m�F
if "%1" EQU "" (
	echo �X�L�[�}���擾���s
	exit /b 1
)
rem �X�L�[�}�P�ʂ̌�V�擾 ��V����̃p�b�`�K�p
set VERSION=
for /f "DELIMS=" %%A IN ('psql -U eteam -d eteam -t -c "SELECT version FROM %1.version"') do call :VERSIONTRIMSET %%A
if NOT DEFINED VERSION (
    echo �o�[�W�������ʂŃG���[
    exit /b 1
)
echo �ΏۃX�L�[�}[%1]
echo ���݃��@�[�W����[%VERSION%]
for /f "DELIMS=" %%A IN ('dir /A:d /B .') do (
	rem �A�b�v�f�[�g�p�b�`���X�L�[�}�w��ŌĂяo��
	rem if "%VERSION%" LSS "%%A" echo �y%%A\patch.bat %1�z�����s
	if "%VERSION%" LSS "%%A" call %%A\patch.bat %1
	cd "%~dp0"
)
rem �`�[�ꗗ�f�[�^�o�^����
call c:\eteam\bat\bin\createDenpyouList.bat %1
rem ���[�U�[��`�͏��ꗗ�f�[�^�o�^����
call c:\eteam\bat\bin\createKaniTodokeList.bat %1
exit /b 0

:VERSIONTRIMSET
rem psql�Ŏ擾�����e�X�L�[�}���@�[�W�����ԍ���Trim
set VERSION=%1
exit /b 0

