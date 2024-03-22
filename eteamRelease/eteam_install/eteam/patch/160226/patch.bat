@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd "%~dp0"
setlocal

set PATCH_VER=160226

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

rem ���F���[�g�f�[�^�X�V
psql -U eteam -d eteam -f .\files\shounin_edano_update.sql -v SCHEMA_NAME=%1 > c:\eteam\tmp\shounin_edano_update_%1.log 2>&1

rem �A�b�v�f�[�g���s�f�[�^���X�g�o�́A���X�g�ێ��p�ꎞ�e�[�u���폜
psql -U eteam -d eteam -t -c "SELECT * FROM %1.update_fail_tmp ORDER BY denpyou_id" > c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt
psql -U eteam -d eteam -t -c "DROP TABLE IF EXISTS %1.update_fail_tmp"
for %%F in (c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt) do (
   rem ���s�R�[�h�̂ݏo�͂���Ă���ꍇ���l������2�o�C�g�ȉ�
   if %%~zF leq 2 (
     if exist c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt del c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt
   ) else (
     echo [%1 ���F���[�g�A�b�v�f�[�g���s�f�[�^���X�g]
     echo -------------------------------------
     type c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt
     echo ���X�g��[c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt]�ɂ��o�͂���Ă��܂��B
     pause
   )
)

rem ���@�[�W�����o�^
psql -U eteam -d eteam -c "UPDATE %1.version SET version='%PATCH_VER%'";

echo Version Up[%PATCH_VER%][%1] End

endlocal
