@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem �K�v���̓���
set /P SCHEMA_NAME="�X�L�[�}������͂��Ă�������: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1

set /P DT="����i���̂��郂�W���[���ɃA�b�v�f�[�g�������j��YYYY-MM-DD�`���œ��͂��Ă�������: "
if /i {%DT%} equ {} exit /b 1

SET /P ANSWER="�X�L�[�}(%SCHEMA_NAME%)�A���(%DT%)�ł�낵���ł���(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1


rem SQL�Œ���
psql -U eteam -d eteam -c "SET SEARCH_PATH TO %SCHEMA_NAME%; SELECT DISTINCT denpyou_id FROM shiwake_sias s WHERE shiwake_status = '1' AND koushin_time >= '%DT%' AND EXISTS(SELECT * FROM ryohiseisan_meisai r WHERE r.denpyou_id = s.denpyou_id AND shubetsu_cd = '2' AND nissuu IS NULL) ORDER BY denpyou_id;"


pause
