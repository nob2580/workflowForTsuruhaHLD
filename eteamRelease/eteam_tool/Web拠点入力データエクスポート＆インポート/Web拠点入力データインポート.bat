@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem �K�v���̓���
set /P SCHEMA_NAME="�X�L�[�}������͂��Ă�������: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1

SET /P ANSWER="�X�L�[�}(%SCHEMA_NAME%)�ł�낵���ł���(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1

rem �ޔ������f�[�^�߂�
cd .\csv\%SCHEMA_NAME%
copy *.csv .\..\
cd .\..\..\
psql -U eteam -d eteam -f Web���_���̓f�[�^�C���|�[�g.sql -v SCHEMA_NAME=%SCHEMA_NAME%
del .\csv\*.csv

pause
