@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem # ログ出力パス
set LOG=..\logs\backup.log

rem # バックアップ出力先
set DUMPDIR=C:\eteam\dbbackup

rem # バックアップ保管期限
set SAVE_DAYS=-3

rem # ファイル名を作成する
set YYYYMMDD=date
set HHMMSS_TMP=%time: =0%
set HHMMSS=%HHMMSS_TMP:~0,2%%HHMMSS_TMP:~3,2%%HHMMSS_TMP:~6,2%
set FILE_NAME=%date:~-10,4%%date:~-5,2%%date:~-2,2%%HHMMSS%_eteamDb.dump

rem # 実行日時出力
echo ---------- >> %LOG%
echo 処理開始時刻 >> %LOG%
date /t >> %LOG%
time /t >> %LOG%
echo ファイル名:%FILE_NAME% >> %LOG%


rem # 古いダンプの削除
forfiles /P %DUMPDIR% /M *.dump /D %SAVE_DAYS% /C "cmd /c del @path" >> %LOG% 2>&1


rem # ダンプ
pg_dump -h localhost -U eteam -d eteam -E utf8 -F c -b -f %DUMPDIR%\%FILE_NAME% >> %LOG%


rem # 実行時エラー判定
if %errorlevel%==0 (
 call NormalEndBatch.bat backupBat
 goto DUMP_OK
)

rem # エラー時
call ErrorBatch.bat backupBat
echo バックアップ実行時にエラーが発生しました。 >> %LOG%
echo エラー番号：%errorlevel% >> %LOG%
net helpmsg %errorlevel% >> %LOG%


rem # 正常時
:DUMP_OK
echo 処理終了時刻 >> %LOG%
time /t >> %LOG%
echo. >> %LOG%
exit /b 0


