@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd "%~dp0"
setlocal

set PATCH_VER=160311

rem �X�L�[�}�P�ʂɏ���
if "%1" NEQ "" goto UNIT
psql -U eteam -d eteam -t -c "SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT LIKE 'pg_%%' AND nspname NOT LIKE 'information_%%' AND nspname <> 'public'" > schema_list.txt
for /f %%L in (schema_list.txt) do (call patch.bat %%L)
del schema_list.txt
exit /b 0

:UNIT
echo Version Up[%PATCH_VER%][%1] Start

rem �e�[�u���ύX
psql -U eteam -d eteam -f .\files\patch.sql -v SCHEMA_NAME=%1

rem ���@�[�W�����o�^
psql -U eteam -d eteam -c "UPDATE %1.version SET version='%PATCH_VER%'";

echo Version Up[%PATCH_VER%][%1] End

endlocal
