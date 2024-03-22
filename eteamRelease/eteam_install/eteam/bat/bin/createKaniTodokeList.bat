@echo off
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem スキーマが指定されていなければエラーとする
if "%1" NEQ "" goto UNIT
echo スキーマ名が指定されていません。
exit /b 1

:UNIT
rem ユーザー定義届書一覧テーブルデータ登録バッチ
echo ユーザー定義届書一覧データ作成 開始 スキーマ[%1]

rem ユーザー定義届書一覧テーブルデータ登録バッチを実行
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.gyoumu.kanitodoke.KaniTodokeIchiranTableCreateBat %1 

echo ユーザー定義届書一覧データ作成 終了 スキーマ[%1]

if %ERRORLEVEL%==0 exit /b 0
if %ERRORLEVEL%==1 exit /b 1
