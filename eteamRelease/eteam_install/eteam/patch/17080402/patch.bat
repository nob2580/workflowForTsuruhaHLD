@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd "%~dp0"
setlocal



rem 以下 初期処理----------------------------

rem ヴァージョン定義 ◆◆◆要確認◆◆◆
set PATCH_VER=17080402

rem 財務バージョン取得
reg query "HKEY_LOCAL_MACHINE\SOFTWARE\（株）ＩＣＳパートナーズ\Prj312" /v "WFSerial" /reg:64> nul 2>&1
if %ERRORLEVEL% == 0 (set ZAIMUVER=SIAS) else (set ZAIMUVER=de3)



rem 以下 スキーマ単位振り分け----------------

if "%1" NEQ "" goto UNIT

rem スキーマ単位ループ
psql -U eteam -d eteam -t -c "SELECT nspname FROM pg_catalog.pg_namespace WHERE nspname NOT LIKE 'pg_%%' AND nspname NOT LIKE 'information_%%' AND nspname <> 'public'" > schema_list.txt
for /f %%L in (schema_list.txt) do (call patch.bat %%L)

rem スキーマファイル削除
del schema_list.txt
exit /b 0



rem 以下 スキーマ単位処理--------------------
:UNIT
echo Version Up[%PATCH_VER%][%1] Start

rem テーブル変更
psql -U eteam -d eteam -f .\files\patch.sql -v SCHEMA_NAME=%1 -v ZAIMU_VER=%ZAIMUVER%

rem ヴァージョン登録
psql -U eteam -d eteam -c "UPDATE %1.version SET version='%PATCH_VER%'";

echo Version Up[%PATCH_VER%][%1] End



endlocal
