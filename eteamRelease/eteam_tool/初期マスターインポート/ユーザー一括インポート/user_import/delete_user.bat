@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd "%~dp0"
setlocal

rem スキーマ名入力
set /P SCHEMA_NAME="スキーマ名を入力してください: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
SET /P ANSWER="スキーマ(%SCHEMA_NAME%)に対してユーザー等情報を削除してよろしいですか(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1

rem テーブル変更
psql -U eteam -d eteam -f .\files\delete_user.sql -v SCHEMA_NAME=%SCHEMA_NAME%

pause