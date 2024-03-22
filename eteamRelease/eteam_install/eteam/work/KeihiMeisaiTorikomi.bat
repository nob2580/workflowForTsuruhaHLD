@echo off
cd /d %~dp0
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem 条件
echo "経費明細データ更新を行います。"
set /P SCHEMA_NAME="処理対象のスキーマ名を入力してください: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
set /P TARGET_MONTH="取得する開始月をYYYYMM形式で入力してください: "
if /i {%TARGET_MONTH%} equ {} exit /b 1
SET /P ANSWER="スキーマ名(%SCHEMA_NAME%)、開始月(%TARGET_MONTH%)以降を処理します。よろしいですか？(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1


rem 経費明細データ更新(初期インポート)バッチ
echo 経費明細データ更新(初期インポート)バッチ 開始 スキーマ[%SCHEMA_NAME%] 開始月[%TARGET_MONTH%]
set isError=false


rem 経費明細データ更新
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.gyoumu.masterkanri.MasterTorikomiTermBat %SCHEMA_NAME% %TARGET_MONTH%
if %ERRORLEVEL%==0 call ..\bat\bin\NormalEndBatch.bat MasterTorikomiTermBat
if %ERRORLEVEL%==1 call ..\bat\bin\ErrorBatch.bat MasterTorikomiTermBat


pause
if %isError%==true  exit /b 1
exit /b 0

