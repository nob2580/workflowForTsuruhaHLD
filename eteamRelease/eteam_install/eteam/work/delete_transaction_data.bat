@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem �X�L�[�}������
set /P SCHEMA_NAME="�X�L�[�}������͂��Ă�������: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
SET /P ANSWER="�X�L�[�}(%SCHEMA_NAME%)�̃g�����U�N�V�����f�[�^���폜���Ă�낵���ł���(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1


rem �g�����U�N�V�����f�[�^�폜
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.denpyou WHERE denpyou_kbn BETWEEN 'A001' AND 'A999'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.denpyou WHERE denpyou_kbn BETWEEN 'B001' AND 'B999'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.teiki_jouhou"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.houjin_card_import"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.houjin_card_jouhou"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.ic_card"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.ic_card_rireki"
call c:\eteam\bat\bin\OldDataDelete.bat %SCHEMA_NAME% 0


pause
