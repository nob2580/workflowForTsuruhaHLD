@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd "%~dp0"
setlocal


rem �ȉ� �X�L�[�}�P�ʐU�蕪��----------------

if "%1" NEQ "" goto UNIT

rem �X�L�[�}�P�ʃ��[�v
psql -U eteam -d eteam -t -c "SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT LIKE 'pg_%%' AND nspname NOT LIKE 'information_%%' AND nspname <> 'public'" > schema_list.txt
for /f %%L in (schema_list.txt) do (call shiwake_sequence_reset.bat %%L)

rem �X�L�[�}�t�@�C���폜
del schema_list.txt

pause
exit /b 0



rem �ȉ� �X�L�[�}�P�ʏ���--------------------
:UNIT
echo �d��V�[�P���X���Z�b�g[%1] Start

rem �e�[�u���ύX
psql -U eteam -d eteam -f .\files\shiwake_sequence_reset.sql -v SCHEMA_NAME=%1 -v ZAIMU_VER=%ZAIMUVER%

echo �d��V�[�P���X���Z�b�g[%1] End


endlocal