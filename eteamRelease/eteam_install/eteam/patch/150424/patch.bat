@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd "%~dp0"

rem スキーマ単位に処理
if "%1" NEQ "" goto UNIT
psql -U eteam -d eteam -t -c "SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT LIKE 'pg_%%' AND nspname NOT LIKE 'information_%%' AND nspname <> 'public'" > schema_list.txt
for /f %%L in (schema_list.txt) do (call patch.bat %%L)
del schema_list.txt
exit /b 0


:UNIT
echo Version Up[150424][%1] Start

rem 設定情報追加
psql -U eteam -d eteam -c "CREATE TABLE %1.setting_info_old AS SELECT setting_name,setting_name_wa,setting_val,category,hyouji_jun,editable_flg,hissu_flg,format_regex,description FROM %1.SETTING_INFO"
psql -U eteam -d eteam -c "DELETE FROM %1.setting_info"
psql -U eteam -d eteam -c "\copy %1.setting_info FROM 'files/csv/setting_info.csv' WITH CSV header ENCODING 'SJIS'"
psql -U eteam -d eteam -c "UPDATE %1.SETTING_INFO N SET SETTING_VAL = (SELECT SETTING_VAL FROM %1.SETTING_INFO_OLD O WHERE O.SETTING_NAME = N.SETTING_NAME) WHERE N.SETTING_NAME IN (SELECT SETTING_NAME FROM %1.SETTING_INFO_OLD)"
psql -U eteam -d eteam -c "DROP TABLE %1.SETTING_INFO_OLD"

rem テーブル作成
psql -U eteam -d eteam -f .\files\patch.sql -v SCHEMA_NAME=%1

echo Version Up[150424][%1] End
