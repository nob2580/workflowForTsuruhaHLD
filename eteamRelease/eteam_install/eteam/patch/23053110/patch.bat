@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd "%~dp0"
setlocal



rem �ȉ� ��������----------------------------

rem ���@�[�W������`(��ʃt�H���_yymmdd##��PATCH_VER��)
set PATCH_VER=%cd%
for /F "DELIMS=" %%A IN ("%PATCH_VER%") DO SET PATCH_VER=%%~nxA

rem �����o�[�W�����擾
reg query "HKEY_LOCAL_MACHINE\SOFTWARE\�i���j�h�b�r�p�[�g�i�[�Y\Prj312" /v "WFSerial" /reg:64> nul 2>&1
if %ERRORLEVEL% == 0 (set ZAIMUVER=SIAS) else (set ZAIMUVER=de3)

rem github�̋�t�H���_�����΍�
set "folder=.\files\csv"

if not exist %folder% (
  mkdir %folder%
  echo created folder: %folder%
)

rem ��Аݒ�X�V��(setting_info_XX.csv������ꍇ) ����SQL�p�Ƀt�@�C�������l�[��
if "%ZAIMUVER%" EQU "SIAS" (
    if exist ..\..\work\files\csv\setting_info_sias.csv copy /Y ..\..\work\files\csv\setting_info_sias.csv				.\files\csv\setting_info_tmp.csv
) else if "%ZAIMUVER%" EQU "de3" (
    if exist ..\..\work\files\csv\setting_info_de3.csv copy /Y ..\..\work\files\csv\setting_info_de3.csv				.\files\csv\setting_info_tmp.csv
) else (
    echo �o�[�W�������ʂŃG���[
    exit /b 1
)

rem �ȉ� �X�L�[�}�P�ʐU�蕪��----------------

if "%1" NEQ "" goto UNIT

rem �X�L�[�}�P�ʃ��[�v
psql -U eteam -d eteam -t -c "SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT LIKE 'pg_%%' AND nspname NOT LIKE 'information_%%' AND nspname <> 'public'" > schema_list.txt
for /f %%L in (schema_list.txt) do (call patch.bat %%L)

rem �X�L�[�}�t�@�C���폜
del schema_list.txt

exit /b 0



rem �ȉ� �X�L�[�}�P�ʏ���--------------------
:UNIT
echo Version Up[%PATCH_VER%][%1] Start

rem �e�[�u���ύX
psql -U eteam -d eteam -f .\files\patch.sql -v SCHEMA_NAME=%1 -v ZAIMU_VER=%ZAIMUVER%

rem ���@�[�W�����o�^
psql -U eteam -d eteam -c "UPDATE %1.version SET version='%PATCH_VER%'";

echo Version Up[%PATCH_VER%][%1] End

endlocal