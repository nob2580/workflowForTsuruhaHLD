@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem 必要情報の入力
set /P SCHEMA_NAME="スキーマ名を入力してください: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1

set /P DT="基準日（問題のあるモジュールにアップデートした日）をYYYY-MM-DD形式で入力してください: "
if /i {%DT%} equ {} exit /b 1

SET /P ANSWER="スキーマ(%SCHEMA_NAME%)、基準日(%DT%)でよろしいですか(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1


rem SQLで調査
psql -U eteam -d eteam -c "SET SEARCH_PATH TO %SCHEMA_NAME%; SELECT DISTINCT denpyou_id FROM shiwake_sias s WHERE shiwake_status = '1' AND koushin_time >= '%DT%' AND EXISTS(SELECT * FROM ryohiseisan_meisai r WHERE r.denpyou_id = s.denpyou_id AND shubetsu_cd = '2' AND nissuu IS NULL) ORDER BY denpyou_id;"


pause
