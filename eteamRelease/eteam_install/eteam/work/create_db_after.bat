@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem public�X�L�[�}��ǉ����A�g���@�\��K�p
psql -U postgres -W -d eteam -f .\files\create_db_after.sql


pause
