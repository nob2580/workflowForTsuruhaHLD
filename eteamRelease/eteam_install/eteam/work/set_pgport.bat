@echo off
cd /d %~dp0

rem ���ɐݒ�σt���O���ϐ�������Ȃ炻�̂܂܏I��(PATH�̏d���ݒ��h��)
rem if defined ETEAM_TMP_SET_PGPORT (exit /b)

rem �|�[�g�ݒ�l�����݂���Ȃ�Y���l��PGPORT�ɐݒ�
reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Services\postgresql-x64-15" /v "Port" /reg:64 > nul 2>&1
if %ERRORLEVEL% == 1 goto :Pos11
if %ERRORLEVEL% == 0 (for /F "TOKENS=1,2,*" %%A in ('reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Services\postgresql-x64-15" /v "Port" /reg:64') do if "%%A"=="Port" SET /A WF_POSTGRES_PORT=%%C) else (SET /A WF_POSTGRES_PORT=5432)
if defined WF_POSTGRES_PORT (SET /A PGPORT=%WF_POSTGRES_PORT%)

rem ���͗��p��PostgreSQL��path�w��
reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Installations\postgresql-x64-15" /v "Base Directory" /reg:64 > nul 2>&1
if %ERRORLEVEL% == 0 (for /F "TOKENS=1,2,3,*" %%A in ('reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Installations\postgresql-x64-15" /v "Base Directory" /reg:64') do if "%%A %%B"=="Base Directory" SET WF_POSTGRES_BASEDIR=%%D)
if defined WF_POSTGRES_BASEDIR (SET "PATH=%WF_POSTGRES_BASEDIR%\bin;%PATH%")

rem �ݒ�σt���O���ϐ��o�^���ďI��
SET ETEAM_TMP_SET_PGPORT=TRUE
exit /b


:Pos11
rem �|�[�g�ݒ�l�����݂���Ȃ�Y���l��PGPORT�ɐݒ�
reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Services\postgresql-x64-11" /v "Port" /reg:64 > nul 2>&1
if %ERRORLEVEL% == 0 (for /F "TOKENS=1,2,*" %%A in ('reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Services\postgresql-x64-11" /v "Port" /reg:64') do if "%%A"=="Port" SET /A WF_POSTGRES_PORT=%%C) else (SET /A WF_POSTGRES_PORT=5432)
if defined WF_POSTGRES_PORT (SET /A PGPORT=%WF_POSTGRES_PORT%)

rem ���͗��p��PostgreSQL��path�w��
reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Installations\postgresql-x64-11" /v "Base Directory" /reg:64 > nul 2>&1
if %ERRORLEVEL% == 0 (for /F "TOKENS=1,2,3,*" %%A in ('reg query "HKEY_LOCAL_MACHINE\SOFTWARE\PostgreSQL\Installations\postgresql-x64-11" /v "Base Directory" /reg:64') do if "%%A %%B"=="Base Directory" SET WF_POSTGRES_BASEDIR=%%D)
if defined WF_POSTGRES_BASEDIR (SET "PATH=%WF_POSTGRES_BASEDIR%\bin;%PATH%")

rem �ݒ�σt���O���ϐ��o�^���ďI��
SET ETEAM_TMP_SET_PGPORT=TRUE