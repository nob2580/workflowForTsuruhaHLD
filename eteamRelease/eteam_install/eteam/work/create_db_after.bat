@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem publicスキーマを追加し、拡張機能を適用
psql -U postgres -W -d eteam -f .\files\create_db_after.sql


pause
