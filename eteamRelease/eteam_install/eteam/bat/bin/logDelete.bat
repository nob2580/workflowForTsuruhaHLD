@echo off
cd /d %~dp0

rem 削除バッチ出力ログ
set LOG_FILE=..\logs\logDelete.log

rem apacheログディレクトリ
set APACHE_DIR=C:\Apache24\logs
rem tomcatログディレクトリ
set TOMCAT_DIR="C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs"
rem log4jWebログディレクトリ
set LOG4J_WEB_DIR=..\..\web\logs
rem log4jBatログディレクトリ
set LOG4J_BAT_DIR=..\..\bat\logs

rem ログファイル保持日数
set SAVE_DAYS=-60

rem # 実行日時出力
echo ---------- >> %LOG_FILE%
echo 処理開始時刻 >> %LOG_FILE%
date /t >> %LOG_FILE%
time /t >> %LOG_FILE%


rem apacheログの削除
forfiles /P %APACHE_DIR% /M *.log /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1
forfiles /P %APACHE_DIR% /M *.txt /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1

rem tomcatログの削除
forfiles /P %TOMCAT_DIR% /M *.log /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1
forfiles /P %TOMCAT_DIR% /M *.txt /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1

rem LOG4JWebログの削除
forfiles /P %LOG4J_WEB_DIR%  /M *.* /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1

rem LOG4JBatログの削除
forfiles /P %LOG4J_BAT_DIR%  /M *.* /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG_FILE% 2>&1


echo 処理終了時刻 >> %LOG_FILE%
time /t >> %LOG_FILE%
echo. >> %LOG_FILE%
exit /b 0


