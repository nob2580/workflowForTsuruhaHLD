@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd "%~dp0"
setlocal

rem �X�L�[�}�P�ʂɏ���
if "%1" NEQ "" goto UNIT
psql -U eteam -d eteam -t -c "SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT LIKE 'pg_%%' AND nspname NOT LIKE 'information_%%' AND nspname <> 'public'" > schema_list.txt
for /f %%L in (schema_list.txt) do (call de32sias.bat %%L)
del schema_list.txt
exit /b 0

:UNIT
echo de3 to SIAS[%1] Start

rem �e�[�u���ύX
psql -U eteam -d eteam -f .\files\de32sias.sql -v SCHEMA_NAME=%1

endlocal
