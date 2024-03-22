@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

rem 現V取得
for /f "DELIMS=" %%A IN ('java -cp "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\eteam\WEB-INF\classes" eteam.symbol.EteamSymbol') do set VERSION=%%A
echo 現在ヴァージョン[%VERSION%]

rem 財務バージョン取得
reg query "HKEY_LOCAL_MACHINE\SOFTWARE\（株）ＩＣＳパートナーズ\Prj312" /v "WFSerial" > nul 2>&1
if %ERRORLEVEL% == 0 (set ZAIMUVER=SIAS) else (set ZAIMUVER=de3)

rem スキーマ名入力
set /P SCHEMA_NAME="スキーマ名を入力してください: "
if /i {%SCHEMA_NAME%} equ {} exit /b 1
SET /P ANSWER="スキーマ(%SCHEMA_NAME%)を作成してよろしいですか(Y/N)?"
if /i {%ANSWER%} neq {y} exit /b 1


rem スキーマ作成
psql -U eteam -d eteam -c "create schema %SCHEMA_NAME%";


rem ユーザー作成
psql -U eteam -d eteam -c "alter schema %SCHEMA_NAME% owner to eteam;"

rem publicスキーマ設定
psql -U eteam -d eteam -f .\files\create_public.sql -v SCHEMA_NAME=%SCHEMA_NAME%

rem スキーマ内テーブル作成
psql -U eteam -d eteam -f .\files\eteam.sql -v SCHEMA_NAME=%SCHEMA_NAME%

rem 初期データインポート
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.denpyou_serial_no_saiban FROM 'files/csv/denpyou_serial_no_saiban.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.denpyou_shubetsu_ichiran FROM 'files/csv/denpyou_shubetsu_ichiran.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.gamen_kengen_seigyo FROM 'files/csv/gamen_kengen_seigyo.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.gamen_koumoku_seigyo FROM 'files/csv/gamen_koumoku_seigyo.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.gyoumu_role FROM 'files/csv/gyoumu_role.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.gyoumu_role_kinou_seigyo FROM 'files/csv/gyoumu_role_kinou_seigyo.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.gyoumu_role_wariate FROM 'files/csv/gyoumu_role_wariate.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.kaisha_info FROM 'files/csv/kaisha_info.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.kinou_seigyo FROM 'files/csv/kinou_seigyo.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.mail_settei FROM 'files/csv/mail_settei.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_kanri_hansuu FROM 'files/csv/master_kanri_hansuu_%ZAIMUVER%.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_kanri_ichiran FROM 'files/csv/master_kanri_ichiran_%ZAIMUVER%.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_ichiran_de3 FROM 'files/csv/master_torikomi_ichiran_de3.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_shousai_de3 FROM 'files/csv/master_torikomi_shousai_de3.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_ichiran_sias FROM 'files/csv/master_torikomi_ichiran_sias.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_shousai_sias FROM 'files/csv/master_torikomi_shousai_sias.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_ichiran_mk2 FROM 'files/csv/master_torikomi_ichiran_mk2.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_shousai_mk2 FROM 'files/csv/master_torikomi_shousai_mk2.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_term_ichiran_de3 FROM 'files/csv/master_torikomi_term_ichiran_de3.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_term_shousai_de3 FROM 'files/csv/master_torikomi_term_shousai_de3.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_term_ichiran_sias FROM 'files/csv/master_torikomi_term_ichiran_sias.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_term_shousai_sias FROM 'files/csv/master_torikomi_term_shousai_sias.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_term_ichiran_mk2 FROM 'files/csv/master_torikomi_term_ichiran_mk2.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.master_torikomi_term_shousai_mk2 FROM 'files/csv/master_torikomi_term_shousai_mk2.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.naibu_cd_setting FROM 'files/csv/naibu_cd_setting.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.password FROM 'files/csv/password.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.saiban_kanri FROM 'files/csv/saiban_kanri.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.setting_info FROM 'files/csv/setting_info_%ZAIMUVER%.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.shiwake_pattern_setting FROM 'files/csv/shiwake_pattern_setting.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.shiwake_pattern_var_setting FROM 'files/csv/shiwake_pattern_var_setting.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.shozoku_bumon FROM 'files/csv/shozoku_bumon.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.user_info FROM 'files/csv/user_info.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.shounin_shori_kengen FROM 'files/csv/shounin_shori_kengen.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.eki_master FROM 'files/csv/eki_master.csv' WITH CSV header";
psql -U eteam -d eteam -c "\copy %SCHEMA_NAME%.bus_line_master FROM 'files/csv/bus_line_master.csv' WITH CSV header";
psql -U eteam -d eteam -c "INSERT INTO %SCHEMA_NAME%.version VALUES('%VERSION%')";

if %ZAIMUVER% == SIAS (
rem オプションインストール（請求書払い）
call eteamOptionInstall.bat seikyuushobarai %SCHEMA_NAME% 0
call eteamOptionInstall.bat seikyuushobarai %SCHEMA_NAME% 9
rem オプションインストール（支払依頼）
call eteamOptionInstall.bat shiharaiirai %SCHEMA_NAME% 0
call eteamOptionInstall.bat shiharaiirai %SCHEMA_NAME% 9
rem オプションインストール（ユーザー定義届書）
call eteamOptionInstall.bat kanitodoke %SCHEMA_NAME% 0
call eteamOptionInstall.bat kanitodoke %SCHEMA_NAME% 9
)

if %ZAIMUVER% == de3 (
rem オプションアンインストール（請求書払い）
call eteamOptionInstall.bat seikyuushobarai %SCHEMA_NAME% 0
rem オプションアンインストール（支払依頼）
call eteamOptionInstall.bat shiharaiirai %SCHEMA_NAME% 0
rem オプションアンインストール（ユーザー定義届書）
call eteamOptionInstall.bat kanitodoke %SCHEMA_NAME% 0
rem オプションアンインストール（予算）
call eteamOptionInstall.bat yosan %SCHEMA_NAME% 0
rem オプションアンインストール（WEB拠点）
call eteamOptionInstall.bat kyoten %SCHEMA_NAME% 0
)
pause
