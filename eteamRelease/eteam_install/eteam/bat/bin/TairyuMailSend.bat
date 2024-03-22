@echo off
cd /d %~dp0
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem スキーマ単位に処理
if "%1" NEQ "" goto UNIT
for /f %%L in (tenant_list.txt) do (call TairyuMailSend.bat %%L >> c:\eteam\bat\logs\%~n0_%DATE:~-10,4%%DATE:~-5,2%%DATE:~-2%_%%L.txt 2>> c:\eteam\bat\logs\%~n0_%DATE:~-10,4%%DATE:~-5,2%%DATE:~-2%_%%LError.txt)
exit /b 0


:UNIT
rem 滞留メール配信バッチ
echo 滞留メール配信バッチ 開始 スキーマ[%1]
set isError=false


rem 滞留メール（要処理通知等）を配信する
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.gyoumu.workflow.TairyuMailSendBat %1
if %ERRORLEVEL%==0 call NormalEndBatch.bat TairyuMailSend
if %ERRORLEVEL%==1 call ErrorBatch.bat TairyuMailSend


rem Subでエラーが出ていればエラー、そうでなければ正常
if %isError%==true  exit /b 1
exit /b 0


