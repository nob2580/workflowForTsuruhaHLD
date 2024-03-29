@echo off
cd /d %~dp0

rem 既に設定済フラグ環境変数があるならそのまま終了(PATHの重複設定を防ぐ)
if defined ETEAM_TMP_SET_PGPORT (exit /b)

rem ポート設定値が存在するなら該当値をPGPORTに設定
reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Services\postgresql-x64-11" /v "Port" > nul 2>&1
if %ERRORLEVEL% == 0 (for /F "TOKENS=1,2,*" %%A in ('reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Services\postgresql-x64-11" /v "Port"') do if "%%A"=="Port" SET /A WF_POSTGRES_PORT=%%C) else (SET /A WF_POSTGRES_PORT=5432)
if defined WF_POSTGRES_PORT (SET PGPORT=%WF_POSTGRES_PORT%)

rem 諸届利用側PostgreSQLのpath指定
reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Installations\postgresql-x64-11" /v "Base Directory" > nul 2>&1
if %ERRORLEVEL% == 0 (for /F "TOKENS=1,2,3,*" %%A in ('reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Installations\postgresql-x64-11" /v "Base Directory"') do if "%%A %%B"=="Base Directory" SET WF_POSTGRES_BASEDIR=%%D)
if defined WF_POSTGRES_BASEDIR (SET "PATH=%WF_POSTGRES_BASEDIR%\bin;%PATH%")

rem 設定済フラグ環境変数登録して終了
SET ETEAM_TMP_SET_PGPORT=TRUE
