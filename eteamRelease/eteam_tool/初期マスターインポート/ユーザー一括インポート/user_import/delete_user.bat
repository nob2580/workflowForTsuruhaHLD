@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd "%~dp0"
setlocal

rem �X�L�[�}������
set /P SCHEMA_NAME="�X�L�[�}������͂��Ă�������: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
SET /P ANSWER="�X�L�[�}(%SCHEMA_NAME%)�ɑ΂��ă��[�U�[�������폜���Ă�낵���ł���(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1

rem �e�[�u���ύX
psql -U eteam -d eteam -f .\files\delete_user.sql -v SCHEMA_NAME=%SCHEMA_NAME%

pause