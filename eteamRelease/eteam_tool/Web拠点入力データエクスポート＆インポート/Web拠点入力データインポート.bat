@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem 必要情報の入力
set /P SCHEMA_NAME="スキーマ名を入力してください: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1

SET /P ANSWER="スキーマ(%SCHEMA_NAME%)でよろしいですか(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1

rem 退避したデータ戻す
cd .\csv\%SCHEMA_NAME%
copy *.csv .\..\
cd .\..\..\
psql -U eteam -d eteam -f Web拠点入力データインポート.sql -v SCHEMA_NAME=%SCHEMA_NAME%
del .\csv\*.csv

pause
