@echo off
cd /d %~dp0

rem �폜�o�b�`�o�̓��O
set LOG_FILE=..\logs\logDelete.log

rem apache���O�f�B���N�g��
set APACHE_DIR=C:\Apache24\logs
rem tomcat���O�f�B���N�g��
set TOMCAT_DIR="C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs"
rem log4jWeb���O�f�B���N�g��
set LOG4J_WEB_DIR=..\..\web\logs
rem log4jBat���O�f�B���N�g��
set LOG4J_BAT_DIR=..\..\bat\logs

rem ���O�t�@�C���ێ�����
set SAVE_DAYS=-60

rem # ���s�����o��
echo ---------- >> %LOG_FILE%
echo �����J�n���� >> %LOG_FILE%
date /t >> %LOG_FILE%
time /t >> %LOG_FILE%


rem apache���O�̍폜
forfiles /P %APACHE_DIR% /M *.log /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1
forfiles /P %APACHE_DIR% /M *.txt /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1

rem tomcat���O�̍폜
forfiles /P %TOMCAT_DIR% /M *.log /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1
forfiles /P %TOMCAT_DIR% /M *.txt /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1

rem LOG4JWeb���O�̍폜
forfiles /P %LOG4J_WEB_DIR%  /M *.* /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1

rem LOG4JBat���O�̍폜
forfiles /P %LOG4J_BAT_DIR%  /M *.* /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1


echo �����I������ >> %LOG_FILE%
time /t >> %LOG_FILE%
echo. >> %LOG_FILE%
exit /b 0


