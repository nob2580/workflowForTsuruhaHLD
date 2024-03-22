@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem DB初期化
psql -U postgres -W -f .\files\create_db.sql


rem 設定
setx PGPASSWORD 1QAZxsw2 -m


pause
