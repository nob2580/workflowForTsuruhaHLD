@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem DB������
psql -U postgres -W -f .\files\create_db.sql


rem �ݒ�
setx PGPASSWORD 1QAZxsw2 -m


pause
