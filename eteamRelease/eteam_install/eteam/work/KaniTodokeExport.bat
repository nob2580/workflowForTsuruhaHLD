@echo off
cd /d %~dp0
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem 条件
echo "ユーザー定義届書定義のエクスポートを行います。"
set /P SCHEMA_NAME="処理対象のスキーマ名を入力してください: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
set /P TARGET="対象の伝票区分を4桁で指定してください。全伝票の場合は ALL と指定してください: "
if /i {%TARGET%} equ {} exit /b 1
SET /P ANSWER="スキーマ名(%SCHEMA_NAME%)、対象伝票(%TARGET%)で処理します。よろしいですか？(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1


rem 届出定義エクスポート
echo 届出定義エクスポートバッチ 開始 スキーマ[%SCHEMA_NAME%] 対象伝票[%TARGET%]
set isError=false
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.gyoumu.kanitodoke.KaniTodokeExportBat %SCHEMA_NAME% %TARGET%
if %ERRORLEVEL%==0 call ..\bat\bin\NormalEndBatch.bat KaniTodokeExportBat
if %ERRORLEVEL%==1 call ..\bat\bin\ErrorBatch.bat KaniTodokeExportBat

pause
if %isError%==true  exit /b 1
exit /b 0

