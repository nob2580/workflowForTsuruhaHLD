@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem スキーマ名入力
set /P SCHEMA_NAME="スキーマ名を入力してください: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
SET /P ANSWER="スキーマ(%SCHEMA_NAME%)の起案番号採番データをすべて削除してよろしいですか(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1

rem 起案番号採番データ削除
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.kian_bangou_saiban"

pause
