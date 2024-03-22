rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0


rem 実行
"C:\Program Files\PostgreSQL\9.3\scripts\runpsql.bat"


pause
