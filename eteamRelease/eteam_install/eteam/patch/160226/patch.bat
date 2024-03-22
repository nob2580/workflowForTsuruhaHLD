@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd "%~dp0"
setlocal

set PATCH_VER=160226

rem スキーマ単位に処理
if "%1" NEQ "" goto UNIT
psql -U eteam -d eteam -t -c "SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT LIKE 'pg_%%' AND nspname NOT LIKE 'information_%%' AND nspname <> 'public'" > schema_list.txt
for /f %%L in (schema_list.txt) do (call patch.bat %%L)
del schema_list.txt
exit /b 0

:UNIT
echo Version Up[%PATCH_VER%][%1] Start

rem テーブル変更
psql -U eteam -d eteam -f .\files\patch.sql -v SCHEMA_NAME=%1

rem 承認ルートデータ更新
psql -U eteam -d eteam -f .\files\shounin_edano_update.sql -v SCHEMA_NAME=%1 > c:\eteam\tmp\shounin_edano_update_%1.log 2>&1

rem アップデート失敗データリスト出力、リスト保持用一時テーブル削除
psql -U eteam -d eteam -t -c "SELECT * FROM %1.update_fail_tmp ORDER BY denpyou_id" > c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt
psql -U eteam -d eteam -t -c "DROP TABLE IF EXISTS %1.update_fail_tmp"
for %%F in (c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt) do (
   rem 改行コードのみ出力されている場合を考慮して2バイト以下
   if %%~zF leq 2 (
     if exist c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt del c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt
   ) else (
     echo [%1 承認ルートアップデート失敗データリスト]
     echo -------------------------------------
     type c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt
     echo リストは[c:\eteam\tmp\shounin_edano_update_fail_list_%1.txt]にも出力されています。
     pause
   )
)

rem ヴァージョン登録
psql -U eteam -d eteam -c "UPDATE %1.version SET version='%PATCH_VER%'";

echo Version Up[%PATCH_VER%][%1] End

endlocal
