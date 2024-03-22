@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd "%~dp0"
setlocal


rem 以下 スキーマ単位振り分け----------------

if "%1" NEQ "" goto UNIT

rem スキーマ単位ループ
psql -U eteam -d eteam -t -c "SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT LIKE 'pg_%%' AND nspname NOT LIKE 'information_%%' AND nspname <> 'public'" > schema_list.txt
for /f %%L in (schema_list.txt) do (call shiwake_sequence_reset.bat %%L)

rem スキーマファイル削除
del schema_list.txt

pause
exit /b 0



rem 以下 スキーマ単位処理--------------------
:UNIT
echo 仕訳シーケンスリセット[%1] Start

rem テーブル変更
psql -U eteam -d eteam -f .\files\shiwake_sequence_reset.sql -v SCHEMA_NAME=%1 -v ZAIMU_VER=%ZAIMUVER%

echo 仕訳シーケンスリセット[%1] End


endlocal