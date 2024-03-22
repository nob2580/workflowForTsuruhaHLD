@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem 必要情報の入力
set /P SCHEMA_NAME="スキーマ名を入力してください: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1

SET /P ANSWER="スキーマ(%SCHEMA_NAME%)でよろしいですか(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1


rem csvファイルにデータを退避
psql -U eteam -d eteam -f Web拠点入力データエクスポート.sql -v SCHEMA_NAME=%SCHEMA_NAME%
cd .\csv
mkdir %SCHEMA_NAME%
copy *.csv .\%SCHEMA_NAME%
del *.csv

pause
