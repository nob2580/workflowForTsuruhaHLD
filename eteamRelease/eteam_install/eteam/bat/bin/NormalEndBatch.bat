@echo off

set isError=false
echo [%1] %ERRORLEVEL%:����I�����܂����B
eventcreate /T INFORMATION /ID 200 /L APPLICATION /SO eteam /D "[%1]������I�����܂����B"

exit /b


