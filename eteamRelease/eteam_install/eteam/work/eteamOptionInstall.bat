rem ######################################################################################
rem �I�v�V�����C���X�g�[��
rem ��1���� seikyuushobarai�F�����������Ashiharaiirai�F�x���˗��Akanitodoke�F���[�U�[��`�͏��Ayosan�F�\�Z�I�v�V�����Akeihiseisan�F�o��Z(WF�{��)�I�v�V����
rem ��2���� �X�L�[�}��
rem ��3���� 1�F�C���X�g�[���A0�F�A���C���X�g�[���A9�F���W�X�g���ɂ�鎩������
rem			A�F�\�ZA
rem ######################################################################################

@echo off

rem ���[�N�t���[�g�pPostgreSQL�̃|�[�g�Ebin�p�X�擾
call C:\eteam\work\set_pgport.bat
cd /d %~dp0

IF "%1" EQU "" (
    echo ��1�������ݒ肳��Ă��܂���B
    GOTO ERROR
)
IF "%2" EQU "" (
    echo ��2�������ݒ肳��Ă��܂���B
    GOTO ERROR
)
IF "%3" EQU "" (
    echo ��3�������ݒ肳��Ă��܂���B
    GOTO ERROR
)

SET KBN=%1
SET SCHEMA=%2
SET MODE=%3

echo.
echo ��1����=%KBN%
echo ��2����=%SCHEMA%
echo ��3����=%MODE%
echo.

SETLOCAL enabledelayedexpansion

if !MODE! == 9 (
    rem ��@�R�s�[���ǂ����m�F����
    eteamOptionInstallCheck "illegalcopy"
    if !ERRORLEVEL! == 0 (
        echo �C���X�g�[�����ɖ�肪����܂��B���ВS���҂܂ł��⍇�������肢�v���܂��B
        GOTO ERROR
    ) else if !ERRORLEVEL! == 1 (
        rem ���W�X�g���ɂ��I�v�V�����̗L������
        eteamOptionInstallCheck !KBN!
        if !ERRORLEVEL! == 1 (
            SET MODE=!ERRORLEVEL!
        ) else if !ERRORLEVEL! == 0 (
            SET MODE=!ERRORLEVEL!
        ) else (
            echo �G���[�R�[�h�F!ERRORLEVEL!
            GOTO ERROR
        )
    ) else if !ERRORLEVEL! == -104 (
        echo OPEN21-SIAS���������̂��߁A�I�v�V�����̃C���X�g�[�����X�L�b�v���܂��B�i�G���[�R�[�h�F!ERRORLEVEL!�j
        GOTO ERROR
    ) else (
        echo �C���X�g�[�����ɖ�肪����܂��B���ВS���҂܂ł��⍇�������肢�v���܂��B�i�G���[�R�[�h�F!ERRORLEVEL!�j
        GOTO ERROR
    )
)

rem �����������I�v�V����
if !KBN! == seikyuushobarai (
    if !MODE! == 1 (
        GOTO INSTALL_SEIKYUUSHOBARAI
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_SEIKYUUSHOBARAI
    ) else (
        echo �C���X�g�[�����[�h���s���ł��B�i0�F�ݲݽİ�, 1�F�ݽİ�, 9�Fڼ޽�؂ɂ���߼�݂̗L������ł̲ݽİفj
        GOTO ERROR
    )
)

rem �x���˗��I�v�V����
if !KBN! == shiharaiirai (
    if !MODE! == 1 (
        GOTO INSTALL_SHIHARAIIRAI
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_SHIHARAIIRAI
    ) else (
        echo �C���X�g�[�����[�h���s���ł��B�i0�F�ݲݽİ�, 1�F�ݽİ�, 9�Fڼ޽�؂ɂ���߼�݂̗L������ł̲ݽİفj
        GOTO ERROR
    )
)

rem ���[�U�[��`�͏��I�v�V����
if !KBN! == kanitodoke (
    if !MODE! == 1 (
        GOTO INSTALL_KANITODOKE
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_KANITODOKE
    ) else (
        echo �C���X�g�[�����[�h���s���ł��B�i0�F�ݲݽİ�, 1�F�ݽİ�, 9�Fڼ޽�؂ɂ���߼�݂̗L������ł̲ݽİفj
        GOTO ERROR
    )
)

rem �\�Z�I�v�V����
if !KBN! == yosan (
    if !MODE! == A (
        GOTO INSTALL_YOSAN_A
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_YOSAN
    ) else (
        echo �C���X�g�[�����[�h���s���ł��B�i0�F�ݲݽİ�, A�F�\�ZA �ݽİفj
        GOTO ERROR
    )
)

rem Web���_���̓I�v�V����
if !KBN! == kyoten (
    if !MODE! == 1 (
        GOTO INSTALL_KYOTEN
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_KYOTEN
    ) else (
        echo �C���X�g�[�����[�h���s���ł��B�i0�F�ݲݽİ�, 1�F�ݽİ�, 9�Fڼ޽�؂ɂ���߼�݂̗L������ł̲ݽİفj
        GOTO ERROR
    )
)

rem �o��Z(WF�{��)�I�v�V����
if !KBN! == keihiseisan (
    if !MODE! == 1 (
        GOTO INSTALL_KEIHISEISAN
    ) else if !MODE! == 0 (
        GOTO UNINSTALL_KEIHISEISAN
    ) else (
        echo �C���X�g�[�����[�h���s���ł��B�i0�F�ݲݽİ�, 1�F�ݽİ�, 9�Fڼ޽�؂ɂ���߼�݂̗L������ł̲ݽİفj
        GOTO ERROR
    )
)

ENDLOCAL

:ERROR
exit /b 1

:INSTALL_SEIKYUUSHOBARAI

echo �����������I�v�V�����̃C���X�g�[��
psql -U eteam -d eteam -c "\copy %SCHEMA%.denpyou_shubetsu_ichiran FROM 'files/csv/denpyou_shubetsu_ichiran_A003.csv' WITH CSV header"
psql -U eteam -d eteam -c "\copy %SCHEMA%.naibu_cd_setting FROM 'files/csv/naibu_cd_setting_A003.csv' WITH CSV header"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.gamen_kengen_seigyo WHERE gamen_id LIKE 'Seikyuusho%%'"
psql -U eteam -d eteam -c "\copy %SCHEMA%.gamen_kengen_seigyo FROM 'files/csv/gamen_kengen_seigyo_A003.csv' WITH CSV header"
echo.
exit /b 0

:UNINSTALL_SEIKYUUSHOBARAI

echo �����������I�v�V�����̃A���C���X�g�[��
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

echo �x���˗��I�v�V�����̃C���X�g�[��
psql -U eteam -d eteam -c "\copy %SCHEMA%.denpyou_shubetsu_ichiran FROM 'files/csv/denpyou_shubetsu_ichiran_A013.csv' WITH CSV header"
psql -U eteam -d eteam -c "\copy %SCHEMA%.naibu_cd_setting FROM 'files/csv/naibu_cd_setting_A013.csv' WITH CSV header"
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.gamen_kengen_seigyo WHERE gamen_id LIKE 'Shiharai%%'"
psql -U eteam -d eteam -c "\copy %SCHEMA%.gamen_kengen_seigyo FROM 'files/csv/gamen_kengen_seigyo_A013.csv' WITH CSV header"
echo.
exit /b 0

:UNINSTALL_SHIHARAIIRAI

echo �x���˗��I�v�V�����̃A���C���X�g�[��
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

echo ���[�U�[��`�͏��I�v�V�����̃C���X�g�[��
psql -U eteam -d eteam -c "DELETE FROM %SCHEMA%.gamen_kengen_seigyo WHERE gamen_id LIKE 'Kani%%'"
psql -U eteam -d eteam -c "\copy %SCHEMA%.gamen_kengen_seigyo FROM 'files/csv/gamen_kengen_seigyo_B.csv' WITH CSV header"
exit /b 0

:UNINSTALL_KANITODOKE

echo ���[�U�[��`�͏��I�v�V�����̃A���C���X�g�[��
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

echo �\�Z�I�v�V�����̃C���X�g�[��
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = 'A' WHERE setting_name = 'yosan_option'"

echo.
exit /b 0

:UNINSTALL_YOSAN

echo �\�Z�I�v�V�����̃A���C���X�g�[��
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = '0' WHERE setting_name = 'yosan_option'"

echo.
exit /b 0

:INSTALL_KYOTEN

echo Web���_���̓I�v�V�����̃C���X�g�[��
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = '1' WHERE setting_name = 'kyoten_option'"

echo.
exit /b 0

:UNINSTALL_KYOTEN

echo Web���_���̓I�v�V�����̃A���C���X�g�[��
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = '0' WHERE setting_name = 'kyoten_option'"

echo.
exit /b 0

:INSTALL_KEIHISEISAN

echo �o��Z(WF�{��)�I�v�V�����̃C���X�g�[��
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = '1' WHERE setting_name = 'ippan_option'"

echo.
exit /b 0

:UNINSTALL_KEIHISEISAN

echo �o��Z(WF�{��)�I�v�V�����̃A���C���X�g�[��
psql -U eteam -d eteam -c "UPDATE %SCHEMA%.setting_info SET setting_val = '0' WHERE setting_name = 'ippan_option'"

echo.
exit /b 0
