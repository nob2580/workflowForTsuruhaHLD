@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem # ���O�o�̓p�X
set LOG=..\logs\backup.log

rem # �o�b�N�A�b�v�o�͐�
set DUMPDIR=C:\eteam\dbbackup

rem # �o�b�N�A�b�v�ۊǊ���
set SAVE_DAYS=-3

rem # �t�@�C�������쐬����
set YYYYMMDD=date
set HHMMSS_TMP=%time: =0%
set HHMMSS=%HHMMSS_TMP:~0,2%%HHMMSS_TMP:~3,2%%HHMMSS_TMP:~6,2%
set FILE_NAME=%date:~-10,4%%date:~-5,2%%date:~-2,2%%HHMMSS%_eteamDb.dump

rem # ���s�����o��
echo ---------- >> %LOG%
echo �����J�n���� >> %LOG%
date /t >> %LOG%
time /t >> %LOG%
echo �t�@�C����:%FILE_NAME% >> %LOG%


rem # �Â��_���v�̍폜
forfiles /P %DUMPDIR% /M *.dump /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG% 2>&1


rem # �_���v
pg_dump -h localhost -U eteam -d eteam -E utf8 -F c -b -f %DUMPDIR%\%FILE_NAME% >> %LOG%


rem # ���s���G���[����
if %errorlevel%==0 (
 call NormalEndBatch.bat backupBat
 goto DUMP_OK
)

rem # �G���[��
call ErrorBatch.bat backupBat
echo �o�b�N�A�b�v���s���ɃG���[���������܂����B >> %LOG%
echo �G���[�ԍ��F%errorlevel% >> %LOG%
net helpmsg %errorlevel% >> %LOG%


rem # ���펞
:DUMP_OK
echo �����I������ >> %LOG%
time /t >> %LOG%
echo. >> %LOG%
exit /b 0


