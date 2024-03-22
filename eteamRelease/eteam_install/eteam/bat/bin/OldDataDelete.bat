@echo off
cd /d %~dp0
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem スキーマ単位に処理
if "%1" NEQ "" goto UNIT
for /f %%L in (tenant_list.txt) do (call OldDataDelete.bat %%L >> c:\eteam\bat\logs\%~n0_%DATE:~-10,4%%DATE:~-5,2%%DATE:~-2%_%%L.txt 2>> c:\eteam\bat\logs\%~n0_%DATE:~-10,4%%DATE:~-5,2%%DATE:~-2%_%%LError.txt)
exit /b 0


:UNIT
rem 過去データ削除バッチ
echo 過去データ削除バッチ 開始 スキーマ[%1]
set isError=false


rem 古いデータを削除する
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.common.datamaintenance.OldDataDeleteBat %1 %2
if %ERRORLEVEL%==0 call NormalEndBatch.bat OldDataDeleteBat
if %ERRORLEVEL%==1 call ErrorBatch.bat OldDataDeleteBat


rem Subでエラーが出ていればエラー、そうでなければ正常
if %isError%==true  exit /b 1
exit /b 0


