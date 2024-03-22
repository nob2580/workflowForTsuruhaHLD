@echo off
cd /d %~dp0
set classpath=C:\eteam\bat\bin\eteam.jar;c:\eteam\bat\lib\*


rem スキーマ単位に処理
if "%1" NEQ "" goto UNIT
for /f %%L in (tenant_list.txt) do (call MasterTorikomi.bat %%L >> c:\eteam\bat\logs\%~n0_%DATE:~-10,4%%DATE:~-5,2%%DATE:~-2%_%%L.txt 2>> c:\eteam\bat\logs\%~n0_%DATE:~-10,4%%DATE:~-5,2%%DATE:~-2%_%%LError.txt)
exit /b 0


:UNIT
rem マスター取込バッチ
echo マスター取込バッチ 開始 スキーマ[%1]
set isError=false


rem OPEN21からマスターデータを取込み
java --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED eteam.gyoumu.masterkanri.MasterTorikomiBat %1
if %ERRORLEVEL%==0 call NormalEndBatch.bat MasterTorikomiBat
if %ERRORLEVEL%==1 call ErrorBatch.bat MasterTorikomiBat


rem Subでエラーが出ていればエラー、そうでなければ正常
if %isError%==true  exit /b 1
exit /b 0


