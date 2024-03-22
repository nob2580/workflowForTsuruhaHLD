@echo off
cd /d %~dp0


cscript files\KaniTodokeImport.vbs
exit /b %ERRORLEVEL%
