@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0


rem バキューム処理はフルじゃないほうで


rem # ログ出力パス
set LOG=..\logs\vacuum.log

rem # 実行日時出力
echo ---------- >> %LOG%
echo 処理開始時刻 >> %LOG%
date /t >> %LOG%
time /t >> %LOG%


rem # ヴァキューム
set PGCLIENTENCODING=sjis
vacuumdb -z -h localhost -U eteam -d eteam

rem # 実行時エラー判定
if %ERRORLEVEL%==0 (
 call NormalEndBatch.bat vacuumBat
 goto VACUUM_OK
)

rem # エラー時
call ErrorBatch.bat vacuumBat
echo DB最適化時にエラーが発生しました。 >> %LOG%
echo エラー番号：%errorlevel% >> %LOG%
net helpmsg %errorlevel% >> %LOG%


rem # 正常時
:VACUUM_OK
echo 処理終了時刻 >> %LOG%
time /t >> %LOG%
echo. >> %LOG%
exit /b 0


