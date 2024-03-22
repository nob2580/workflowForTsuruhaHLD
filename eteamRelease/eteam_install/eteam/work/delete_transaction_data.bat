@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem スキーマ名入力
set /P SCHEMA_NAME="スキーマ名を入力してください: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
SET /P ANSWER="スキーマ(%SCHEMA_NAME%)のトランザクションデータを削除してよろしいですか(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1


rem トランザクションデータ削除
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.denpyou WHERE denpyou_kbn BETWEEN 'A001' AND 'A999'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.denpyou WHERE denpyou_kbn BETWEEN 'B001' AND 'B999'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.teiki_jouhou"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.houjin_card_import"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.houjin_card_jouhou"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.ic_card"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA_NAME%.ic_card_rireki"
call c:\eteam\bat\bin\OldDataDelete.bat %SCHEMA_NAME% 0


pause
