rem ######################################################################################
rem オプションインストール
rem 第1引数 seikyuushobarai：請求書払い、shiharaiirai：支払依頼、kanitodoke：ユーザー定義届書、yosan：予算オプション、keihiseisan：経費精算(WF本体)オプション
rem 第2引数 スキーマ名
rem 第3引数 1：インストール、0：アンインストール、9：レジストリによる自動判定
rem			A：予算A
rem ######################################################################################

@echo off

rem ワークフロー使用PostgreSQLのポート・binパス取得
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

IF "%1" EQU "" (
    echo 第1引数が設定されていません。
    GOTO ERROR
)
IF "%2" EQU "" (
    echo 第2引数が設定されていません。
    GOTO ERROR
)
IF "%3" EQU "" (
    echo 第3引数が設定されていません。
    GOTO ERROR
)

SET KBN=%1
SET SCHEMA=%2
SET MODE=%3

echo.
echo 第1引数=%KBN%
echo 第2引数=%SCHEMA%
echo 第3引数=%MODE%
echo.

SETLOCAL enabledelayedexpansion

if !MODE! == 9 (
    rem 違法コピーかどうか確認する
    eteamOptionInstallCheck "illegalcopy"
    if !ERRORLEVEL! == 0 (
        echo インストール環境に問題があります。弊社担当者までお問合せをお願い致します。
        GOTO ERROR
    ) else if !ERRORLEVEL! == 1 (
        rem レジストリによるオプションの有効判定
        eteamOptionInstallCheck !KBN!
        if !ERRORLEVEL! == 1 (
            SET MODE=!ERRORLEVEL!
        ) else if !ERRORLEVEL! == 0 (
            SET MODE=!ERRORLEVEL!
        ) else (
            echo エラーコード：!ERRORLEVEL!
            GOTO ERROR
        )
    ) else if !ERRORLEVEL! == -104 (
        echo OPEN21-SIASが未導入のため、オプションのインストールをスキップします。（エラーコード：!ERRORLEVEL!）
        GOTO ERROR
    ) else (
        echo インストール環境に問題があります。弊社担当者までお問合せをお願い致します。（エラーコード：!ERRORLEVEL!）
        GOTO ERROR
    )
)

rem 請求書払いオプション
if !KBN! == seikyuushobarai (
    if !MODE! == 1 (
        GOTO INSTALL_SEIKYUUSHOBARAI
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_SEIKYUUSHOBARAI
    ) else (
        echo インストールモードが不正です。（0：ｱﾝｲﾝｽﾄｰﾙ, 1：ｲﾝｽﾄｰﾙ, 9：ﾚｼﾞｽﾄﾘによるｵﾌﾟｼｮﾝの有効判定でのｲﾝｽﾄｰﾙ）
        GOTO ERROR
    )
)

rem 支払依頼オプション
if !KBN! == shiharaiirai (
    if !MODE! == 1 (
        GOTO INSTALL_SHIHARAIIRAI
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_SHIHARAIIRAI
    ) else (
        echo インストールモードが不正です。（0：ｱﾝｲﾝｽﾄｰﾙ, 1：ｲﾝｽﾄｰﾙ, 9：ﾚｼﾞｽﾄﾘによるｵﾌﾟｼｮﾝの有効判定でのｲﾝｽﾄｰﾙ）
        GOTO ERROR
    )
)

rem ユーザー定義届書オプション
if !KBN! == kanitodoke (
    if !MODE! == 1 (
        GOTO INSTALL_KANITODOKE
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_KANITODOKE
    ) else (
        echo インストールモードが不正です。（0：ｱﾝｲﾝｽﾄｰﾙ, 1：ｲﾝｽﾄｰﾙ, 9：ﾚｼﾞｽﾄﾘによるｵﾌﾟｼｮﾝの有効判定でのｲﾝｽﾄｰﾙ）
        GOTO ERROR
    )
)

rem 予算オプション
if !KBN! == yosan (
    if !MODE! == A (
        GOTO INSTALL_YOSAN_A
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_YOSAN
    ) else (
        echo インストールモードが不正です。（0：ｱﾝｲﾝｽﾄｰﾙ, A：予算A ｲﾝｽﾄｰﾙ）
        GOTO ERROR
    )
)

rem Web拠点入力オプション
if !KBN! == kyoten (
    if !MODE! == 1 (
        GOTO INSTALL_KYOTEN
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_KYOTEN
    ) else (
        echo インストールモードが不正です。（0：ｱﾝｲﾝｽﾄｰﾙ, 1：ｲﾝｽﾄｰﾙ, 9：ﾚｼﾞｽﾄﾘによるｵﾌﾟｼｮﾝの有効判定でのｲﾝｽﾄｰﾙ）
        GOTO ERROR
    )
)

rem 経費精算(WF本体)オプション
if !KBN! == keihiseisan (
    if !MODE! == 1 (
        GOTO INSTALL_KEIHISEISAN
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_KEIHISEISAN
    ) else (
        echo インストールモードが不正です。（0：ｱﾝｲﾝｽﾄｰﾙ, 1：ｲﾝｽﾄｰﾙ, 9：ﾚｼﾞｽﾄﾘによるｵﾌﾟｼｮﾝの有効判定でのｲﾝｽﾄｰﾙ）
        GOTO ERROR
    )
)

ENDLOCAL

:ERROR
exit /b 1

:INSTALL_SEIKYUUSHOBARAI

echo 請求書払いオプションのインストール
psql -U eteam -d eteam -c "\copy %SCHEMA%.denpyou_shubetsu_ichiran FROM 'files/csv/denpyou_shubetsu_ichiran_A003.csv' WITH CSV header"
psql -U eteam -d eteam -c "\copy %SCHEMA%.naibu_cd_setting FROM 'files/csv/naibu_cd_setting_A003.csv' WITH CSV header"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.gamen_kengen_seigyo WHERE gamen_id LIKE 'Seikyuusho%%'"
psql -U eteam -d eteam -c "\copy %SCHEMA%.gamen_kengen_seigyo FROM 'files/csv/gamen_kengen_seigyo_A003.csv' WITH CSV header"
echo.
exit /b 0

:UNINSTALL_SEIKYUUSHOBARAI

echo 請求書払いオプションのアンインストール
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.denpyou_shubetsu_ichiran WHERE denpyou_kbn = 'A003'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.naibu_cd_setting WHERE naibu_cd_name = 'denpyou_kbn' AND naibu_cd = 'A003'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.naibu_cd_setting WHERE naibu_cd_name = 'shiwake_pattern_denpyou_kbn' AND naibu_cd = 'A003'"
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.gamen_kengen_seigyo SET bumon_shozoku_riyoukanou_flg = '0', system_kanri_riyoukanou_flg = '0', workflow_riyoukanou_flg = '0', kaishasettei_riyoukanou_flg = '0', keirishori_riyoukanou_flg = '0' WHERE gamen_id LIKE 'Seikyuusho%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.seikyuushobarai"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.seikyuushobarai_meisai"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.denpyou WHERE denpyou_id LIKE '%%A003%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kanren_denpyou WHERE denpyou_id LIKE '%%A003%%' OR kanren_denpyou LIKE '%%A003%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.tenpu_file WHERE denpyou_id LIKE '%%A003%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.shiwake_sias WHERE denpyou_id LIKE '%%A003%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.shiwake_de3 WHERE denpyou_id LIKE '%%A003%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.tsuuchi WHERE denpyou_id LIKE '%%A003%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.jishou_dpkbn_kanren WHERE denpyou_kbn = 'A003'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.bookmark WHERE denpyou_kbn = 'A003'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.bumon_suishou_route_ko WHERE denpyou_kbn = 'A003'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.bumon_suishou_route_oya WHERE denpyou_kbn = 'A003'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.saishuu_syounin_route_ko WHERE denpyou_kbn = 'A003'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.saishuu_syounin_route_oya WHERE denpyou_kbn = 'A003'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.shiwake_pattern_master WHERE denpyou_kbn = 'A003'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.shozoku_bumon_shiwake_pattern_master WHERE denpyou_kbn = 'A003'"
echo.
exit /b 0

:INSTALL_SHIHARAIIRAI

echo 支払依頼オプションのインストール
psql -U eteam -d eteam -c "\copy %SCHEMA%.denpyou_shubetsu_ichiran FROM 'files/csv/denpyou_shubetsu_ichiran_A013.csv' WITH CSV header"
psql -U eteam -d eteam -c "\copy %SCHEMA%.naibu_cd_setting FROM 'files/csv/naibu_cd_setting_A013.csv' WITH CSV header"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.gamen_kengen_seigyo WHERE gamen_id LIKE 'Shiharai%%'"
psql -U eteam -d eteam -c "\copy %SCHEMA%.gamen_kengen_seigyo FROM 'files/csv/gamen_kengen_seigyo_A013.csv' WITH CSV header"
echo.
exit /b 0

:UNINSTALL_SHIHARAIIRAI

echo 支払依頼オプションのアンインストール
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.denpyou_shubetsu_ichiran WHERE denpyou_kbn = 'A013'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.naibu_cd_setting WHERE naibu_cd_name = 'denpyou_kbn' AND naibu_cd = 'A013'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.naibu_cd_setting WHERE naibu_cd_name = 'shiwake_pattern_denpyou_kbn' AND naibu_cd = 'A013'"
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.gamen_kengen_seigyo SET bumon_shozoku_riyoukanou_flg = '0', system_kanri_riyoukanou_flg = '0', workflow_riyoukanou_flg = '0', kaishasettei_riyoukanou_flg = '0', keirishori_riyoukanou_flg = '0' WHERE gamen_id LIKE 'Shiharai%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.shiharai_irai"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.shiharai_irai_meisai"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.denpyou WHERE denpyou_id LIKE '%%A013%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kanren_denpyou WHERE denpyou_id LIKE '%%A013%%' OR kanren_denpyou LIKE '%%A013%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.tenpu_file WHERE denpyou_id LIKE '%%A013%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.shiwake_sias WHERE denpyou_id LIKE '%%A013%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.shiwake_de3 WHERE denpyou_id LIKE '%%A013%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.tsuuchi WHERE denpyou_id LIKE '%%A013%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.jishou_dpkbn_kanren WHERE denpyou_kbn = 'A013'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.bookmark WHERE denpyou_kbn = 'A013'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.bumon_suishou_route_ko WHERE denpyou_kbn = 'A013'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.bumon_suishou_route_oya WHERE denpyou_kbn = 'A013'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.saishuu_syounin_route_ko WHERE denpyou_kbn = 'A013'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.saishuu_syounin_route_oya WHERE denpyou_kbn = 'A013'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.shiwake_pattern_master WHERE denpyou_kbn = 'A013'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.shozoku_bumon_shiwake_pattern_master WHERE denpyou_kbn = 'A013'"
echo.
exit /b 0

:INSTALL_KANITODOKE

echo ユーザー定義届書オプションのインストール
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.gamen_kengen_seigyo WHERE gamen_id LIKE 'Kani%%'"
psql -U eteam -d eteam -c "\copy %SCHEMA%.gamen_kengen_seigyo FROM 'files/csv/gamen_kengen_seigyo_B.csv' WITH CSV header"
exit /b 0

:UNINSTALL_KANITODOKE

echo ユーザー定義届書オプションのアンインストール
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.denpyou_shubetsu_ichiran WHERE denpyou_kbn like '%%B%%'"
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.gamen_kengen_seigyo SET bumon_shozoku_riyoukanou_flg = '0', system_kanri_riyoukanou_flg = '0', workflow_riyoukanou_flg = '0', kaishasettei_riyoukanou_flg = '0', keirishori_riyoukanou_flg = '0' WHERE gamen_id LIKE 'Kani%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke_checkbox"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke_list_ko"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke_list_oya"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke_master"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke_text"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke_textarea"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke_koumoku"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke_meisai"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke_version"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke_meta"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kani_todoke_select_item"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.denpyou WHERE denpyou_id LIKE '%%B%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.kanren_denpyou WHERE denpyou_id LIKE '%%B%%' OR kanren_denpyou LIKE '%%B%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.tenpu_file WHERE denpyou_id LIKE '%%B%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.tsuuchi WHERE denpyou_id LIKE '%%B%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.jishou_dpkbn_kanren WHERE denpyou_kbn LIKE '%%B%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.bookmark WHERE denpyou_kbn LIKE '%%B%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.bumon_suishou_route_ko WHERE denpyou_kbn LIKE '%%B%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.bumon_suishou_route_oya WHERE denpyou_kbn LIKE '%%B%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.saishuu_syounin_route_ko WHERE denpyou_kbn LIKE '%%B%%'"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.saishuu_syounin_route_oya WHERE denpyou_kbn LIKE '%%B%%'"
echo.
exit /b 0

:INSTALL_YOSAN_A

echo 予算オプションのインストール
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = 'A' WHERE setting_name = 'yosan_option'"

echo.
exit /b 0

:UNINSTALL_YOSAN

echo 予算オプションのアンインストール
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = '0' WHERE setting_name = 'yosan_option'"

echo.
exit /b 0

:INSTALL_KYOTEN

echo Web拠点入力オプションのインストール
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = '1' WHERE setting_name = 'kyoten_option'"

echo.
exit /b 0

:UNINSTALL_KYOTEN

echo Web拠点入力オプションのアンインストール
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = '0' WHERE setting_name = 'kyoten_option'"

echo.
exit /b 0

:INSTALL_KEIHISEISAN

echo 経費精算(WF本体)オプションのインストール
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = '1' WHERE setting_name = 'ippan_option'"

echo.
exit /b 0

:UNINSTALL_KEIHISEISAN

echo 経費精算(WF本体)オプションのアンインストール
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = '0' WHERE setting_name = 'ippan_option'"

echo.
exit /b 0
