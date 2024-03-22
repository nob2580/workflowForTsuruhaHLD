@echo off

set isError=false
echo [%1] %ERRORLEVEL%:正常終了しました。
eventcreate /T INFORMATION /ID 200 /L APPLICATION /SO eteam /D "[%1]が正常終了しました。"

exit /b


