@echo off

set isError=true
echo [%1] %ERRORLEVEL%:�G���[���������܂����B
eventcreate /T ERROR /ID 200 /L APPLICATION /SO eteam /D "[%1]���ُ�I�����܂����B"

exit /b


