@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0


rem �o�L���[�������̓t������Ȃ��ق���


rem # ���O�o�̓p�X
set LOG=..\logs\vacuum.log

rem # ���s�����o��
echo ---------- >> %LOG%
echo �����J�n���� >> %LOG%
date /t >> %LOG%
time /t >> %LOG%


rem # ���@�L���[��
set PGCLIENTENCODING=sjis
vacuumdb -z -h localhost -U eteam -d eteam

rem # ���s���G���[����
if %ERRORLEVEL%==0 (
 call NormalEndBatch.bat vacuumBat
 goto VACUUM_OK
)

rem # �G���[��
call ErrorBatch.bat vacuumBat
echo DB�œK�����ɃG���[���������܂����B >> %LOG%
echo �G���[�ԍ��F%errorlevel% >> %LOG%
net helpmsg %errorlevel% >> %LOG%


rem # ���펞
:VACUUM_OK
echo �����I������ >> %LOG%
time /t >> %LOG%
echo. >> %LOG%
exit /b 0


