@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem �X�L�[�}������
set /P SCHEMA_NAME="�X�L�[�}������͂��Ă�������: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
SET /P ANSWER="�X�L�[�}(%SCHEMA_NAME%)���폜���Ă�낵���ł���(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1


rem �X�L�[�}�폜
psql -h localhost -U eteam -d eteam -c "drop schema %SCHEMA_NAME% CASCADE";


pause
