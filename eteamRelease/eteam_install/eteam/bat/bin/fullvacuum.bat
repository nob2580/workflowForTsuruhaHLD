@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0


rem �t���N���[���A�b�v�����s����B
rem �����Ɏ��Ԃ������邽�߁A���X�̃����e�i���X��DB�œK���o�b�`���g�p����


rem # ���O�o�̓p�X
set LOG=..\logs\fullvacuum.log

rem # ���s�����o��
echo ---------- >> %LOG%
echo �����J�n���� >> %LOG%
date /t >> %LOG%
time /t >> %LOG%


rem # ���@�L���[��
set PGCLIENTENCODING=sjis
vacuumdb -z -f -h localhost -U eteam -d eteam

rem # ���s���G���[����
if %ERRORLEVEL%==0 (
 call NormalEndBatch.bat fullvacuumBat
 goto VACUUM_FULL_OK
)

rem # �G���[��
call ErrorBatch.bat fullvacuumBat
echo DB�T�[�o�[�N���[���A�b�v���ɃG���[���������܂����B >> %LOG%
echo �G���[�ԍ��F%errorlevel% >> %LOG%
net helpmsg %errorlevel% >> %LOG%


rem # ���펞
:VACUUM_FULL_OK
echo �����I������ >> %LOG%
time /t >> %LOG%
echo. >> %LOG%
exit /b 0


