@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem �X�L�[�}������
set /P SCHEMA_NAME="�X�L�[�}������͂��Ă�������: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
SET /P ANSWER="�X�L�[�}(%SCHEMA_NAME%)�̋N�Ĕԍ��̔ԃf�[�^�����ׂč폜���Ă�낵���ł���(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1

rem �N�Ĕԍ��̔ԃf�[�^�폜
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.kian_bangou_saiban"

pause
