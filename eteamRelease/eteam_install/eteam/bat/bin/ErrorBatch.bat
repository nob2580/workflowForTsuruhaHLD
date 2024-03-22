@echo off

set isError=true
echo [%1] %ERRORLEVEL%:エラーが発生しました。
eventcreate /T ERROR /ID 200 /L APPLICATION /SO eteam /D "[%1]が異常終了しました。"

exit /b


