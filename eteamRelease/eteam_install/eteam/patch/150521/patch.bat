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
echo Version Up[150521][%1] Start

rem 会計系テーブルカラム追加

rem テーブルバックアップ
psql -U eteam -d eteam -c "ALTER TABLE %1.karibarai RENAME TO karibarai_old"
psql -U eteam -d eteam -c "ALTER TABLE %1.keihiseisan RENAME TO keihiseisan_old"
psql -U eteam -d eteam -c "ALTER TABLE %1.ryohi_karibarai RENAME TO ryohi_karibarai_old"
psql -U eteam -d eteam -c "ALTER TABLE %1.ryohiseisan RENAME TO ryohiseisan_old"
	
rem テーブル作成※会計系とヴァージョン
psql -U eteam -d eteam -f .\files\patch.sql -v SCHEMA_NAME=%1
	
rem データ移行
psql -U eteam -d eteam -c "INSERT INTO %1.karibarai (DENPYOU_ID, SEISAN_YOTEIBI, SEISAN_KANRYOUBI, SHIHARAIBI, SHIHARAIHOUHOU, TEKIYOU, KINGAKU, HOSOKU, SHIWAKE_EDANO, TORIHIKI_NAME, KARI_FUTAN_BUMON_CD, KARI_FUTAN_BUMON_NAME, KARI_KAMOKU_CD, KARI_KAMOKU_NAME, KARI_KAMOKU_EDABAN_CD, KARI_KAMOKU_EDABAN_NAME, KARI_KAZEI_KBN, KASHI_FUTAN_BUMON_CD, KASHI_FUTAN_BUMON_NAME, KASHI_KAMOKU_CD, KASHI_KAMOKU_NAME, KASHI_KAMOKU_EDABAN_CD, KASHI_KAMOKU_EDABAN_NAME, KASHI_KAZEI_KBN, UF1_CD, UF1_NAME_RYAKUSHIKI, UF2_CD, UF2_NAME_RYAKUSHIKI, UF3_CD, UF3_NAME_RYAKUSHIKI, TOUROKU_USER_ID, TOUROKU_TIME, KOUSHIN_USER_ID, KOUSHIN_TIME) SELECT * FROM %1.karibarai_old"
psql -U eteam -d eteam -c "INSERT INTO %1.keihiseisan (DENPYOU_ID, KARIBARAI_DENPYOU_ID, KARIBARAI_MISHIYOU_FLG, SHIYOUBI, SHIHARAIBI, SHIHARAIHOUHOU, SHOUHYOU_SHORUI_FLG, TORIHIKISAKI_CD, TORIHIKISAKI_NAME_RYAKUSHIKI, ZEIRITSU, HONTAI_KINGAKU_GOUKEI, SHOUHIZEIGAKU_GOUKEI, SHIHARAI_KINGAKU_GOUKEI, SASHIHIKI_SHIKYUU_KINGAKU, HOSOKU, TOUROKU_USER_ID, TOUROKU_TIME, KOUSHIN_USER_ID, KOUSHIN_TIME) SELECT * FROM %1.keihiseisan_old"
psql -U eteam -d eteam -c "INSERT INTO %1.ryohi_karibarai (DENPYOU_ID, MOKUTEKI, KIKAN_FROM, SHUPPATSU_KBN, KIKAN_TO, KICHAKU_KBN, SEISAN_YOTEIBI, SEISAN_KANRYOUBI, SHIHARAIBI, SHIHARAIHOUHOU, TEKIYOU, KINGAKU, SHIWAKE_EDANO, TORIHIKI_NAME, KARI_FUTAN_BUMON_CD, KARI_FUTAN_BUMON_NAME, KARI_KAMOKU_CD, KARI_KAMOKU_NAME, KARI_KAMOKU_EDABAN_CD, KARI_KAMOKU_EDABAN_NAME, KARI_KAZEI_KBN, KASHI_FUTAN_BUMON_CD, KASHI_FUTAN_BUMON_NAME, KASHI_KAMOKU_CD, KASHI_KAMOKU_NAME, KASHI_KAMOKU_EDABAN_CD, KASHI_KAMOKU_EDABAN_NAME, KASHI_KAZEI_KBN, UF1_CD, UF1_NAME_RYAKUSHIKI, UF2_CD, UF2_NAME_RYAKUSHIKI, UF3_CD, UF3_NAME_RYAKUSHIKI, TOUROKU_USER_ID, TOUROKU_TIME, KOUSHIN_USER_ID, KOUSHIN_TIME) SELECT * FROM %1.ryohi_karibarai_old"
psql -U eteam -d eteam -c "INSERT INTO %1.ryohiseisan (DENPYOU_ID, KARIBARAI_DENPYOU_ID, KARIBARAI_MISHIYOU_FLG, HOUMONSAKI, MOKUTEKI, SEISANKIKAN_FROM, SEISANKIKAN_TO, SHIHARAIBI, SHIHARAIHOUHOU, TEKIYOU, ZEIRITSU, GOUKEI_KINGAKU, SASHIHIKI_SHIKYUU_KINGAKU, HOSOKU, SHIWAKE_EDANO, TORIHIKI_NAME, KARI_FUTAN_BUMON_CD, KARI_FUTAN_BUMON_NAME, KARI_KAMOKU_CD, KARI_KAMOKU_NAME, KARI_KAMOKU_EDABAN_CD, KARI_KAMOKU_EDABAN_NAME, KARI_KAZEI_KBN, KASHI_FUTAN_BUMON_CD, KASHI_FUTAN_BUMON_NAME, KASHI_KAMOKU_CD, KASHI_KAMOKU_NAME, KASHI_KAMOKU_EDABAN_CD, KASHI_KAMOKU_EDABAN_NAME, KASHI_KAZEI_KBN, UF1_CD, UF1_NAME_RYAKUSHIKI, UF2_CD, UF2_NAME_RYAKUSHIKI, UF3_CD, UF3_NAME_RYAKUSHIKI, TOUROKU_USER_ID, TOUROKU_TIME, KOUSHIN_USER_ID, KOUSHIN_TIME) SELECT * FROM %1.ryohiseisan_old"

rem 元テーブルを削除
psql -U eteam -d eteam -c "DROP TABLE %1.karibarai_old"
psql -U eteam -d eteam -c "DROP TABLE %1.keihiseisan_old"
psql -U eteam -d eteam -c "DROP TABLE %1.ryohi_karibarai_old"
psql -U eteam -d eteam -c "DROP TABLE %1.ryohiseisan_old"


rem バックアップコメント移行

rem 個別スキーマからテキストファイルへ移行
for /f "usebackq tokens=*" %%i in (`psql -U eteam -d eteam -c "select setting_val from %1.setting_info where setting_name = 'dbdump_url'" -t -A -F " "`) do @set BACKUP_PATH=%%i
psql -U eteam -d eteam -c "SELECT file_name,comment FROM %1.backup_comment" -t -A -F " " >> %BACKUP_PATH%\backup_comment.txt

rem 個別スキーマからテーブル削除
psql -U eteam -d eteam -c "DROP TABLE %1.backup_comment"


rem ヴァージョン登録
psql -U eteam -d eteam -c "INSERT INTO %1.version VALUES('150521')";

echo Version Up[150521][%1] End
