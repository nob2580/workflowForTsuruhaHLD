@echo off
rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd "%~dp0"
setlocal

rem �ȉ� ��������----------------------------

rem �����o�[�W�����擾
reg query "HKEY_LOCAL_MACHINE\SOFTWARE\�i���j�h�b�r�p�[�g�i�[�Y\Prj312" /v "WFSerial" /reg:64> nul 2>&1
if %ERRORLEVEL% == 0 (set ZAIMUVER=SIAS) else (set ZAIMUVER=de3)


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
echo �c���n�z�[���f�B���O�X�l�J�X�^�}�C�Y [%1] Start

rem �e�[�u���ύX
psql -U eteam -d eteam -f .\files\patch.sql -v SCHEMA_NAME=%1 -v ZAIMU_VER=%ZAIMUVER%

echo �c���n�z�[���f�B���O�X�l�J�X�^�}�C�Y [%1] End
pause
endlocal