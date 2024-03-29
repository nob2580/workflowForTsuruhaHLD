@echo off


rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd "%~dp0"

rem WEB停止 
call ..\work\stop.bat
cd "%~dp0"

rem timeout 10 /nobreak >null
echo 設定内容を確認中。少々このままでお待ちください！
timeout 30 /nobreak >null

rem 全スキーマ名取得・スキーマ単位ループ
"c:\program files\postgresql\11\bin\psql.exe" -U eteam -d eteam -t -c "SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT LIKE 'pg_%%' AND nspname NOT LIKE 'information_%%' AND nspname <> 'public'" > schema_list_upd.txt
for /f %%L in (schema_list_upd.txt) do (
    call :SCHEMAUPDATE %%L
)
rem スキーマ名リストファイル削除
del schema_list_upd.txt
pause


rem JARファイルの最新化

rem ①古いJARは削除

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

rem ②SQL Anywhere モジュールコピー
copy /Y "C:\Program Files\ICSP\OPENde3\Sybase\SQL Anywhere 12\Java\jodbc.jar" "C:\eteam\bat\lib\jodbc.jar"

rem ③JARをTomcatLibへコピー
copy /Y "c:\eteam\bat\lib\*" "C:\Program Files\Apache Software Foundation\Tomcat 9.0\lib"

rem 古いファイルを削除
rmdir /s /q "C:\eteam\bat\bin\bef" > NUL 2>&1
rmdir /s /q "C:\eteam\bat\bin\mk2" > NUL 2>&1


rem WEBデプロイ 開始
call ..\work\DeployWar.bat


cd "%~dp0"
call ..\work\start.bat
cd "%~dp0"
exit /b 0


:SCHEMAUPDATE
rem 引数でスキーマ名が指定されているか確認
if "%1" EQU "" (
	echo スキーマ名取得失敗
	exit /b 1
)
rem スキーマ単位の現V取得 現Vより後のパッチ適用
set VERSION=
for /f "DELIMS=" %%A IN ('psql -U eteam -d eteam -t -c "SELECT version FROM %1.version"') do call :VERSIONTRIMSET %%A
if NOT DEFINED VERSION (
    echo バージョン判別でエラー
    exit /b 1
)
echo 対象スキーマ[%1]
echo 現在ヴァージョン[%VERSION%]
for /f "DELIMS=" %%A IN ('dir /A:d /B .') do (
	rem アップデートパッチをスキーマ指定で呼び出し
	rem if "%VERSION%" LSS "%%A" echo 【%%A\patch.bat %1】を実行
	if "%VERSION%" LSS "%%A" call %%A\patch.bat %1
	cd "%~dp0"
)
rem 伝票一覧データ登録処理
call c:\eteam\bat\bin\createDenpyouList.bat %1
rem ユーザー定義届書一覧データ登録処理
call c:\eteam\bat\bin\createKaniTodokeList.bat %1
exit /b 0

:VERSIONTRIMSET
rem psqlで取得した各スキーマヴァージョン番号のTrim
set VERSION=%1
exit /b 0

