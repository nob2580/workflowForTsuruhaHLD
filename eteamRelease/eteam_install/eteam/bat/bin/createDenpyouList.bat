@echo off
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem スキーマが指定されていなければエラーとする
if "%1" NEQ "" goto UNIT
echo スキーマ名が指定されていません。
exit /b 1

:UNIT
rem 伝票一覧テーブルデータ登録バッチ
echo 伝票一覧データ作成 開始 スキーマ[%1]

rem 伝票一覧テーブルデータ登録バッチを実行
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.gyoumu.workflow.DenpyouIchiranTableCreateBat %1

echo 伝票一覧データ作成 終了 スキーマ[%1]

if %ERRORLEVEL%==0 exit /b 0
if %ERRORLEVEL%==1 exit /b 1
