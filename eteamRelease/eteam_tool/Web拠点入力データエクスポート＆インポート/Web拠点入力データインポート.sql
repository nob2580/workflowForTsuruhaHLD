SET search_path TO :SCHEMA_NAME;

start transaction;
\copy kamoku_zandaka FROM '.\csv\kamoku_zandaka.csv' WITH CSV ENCODING 'SJIS';
\copy edaban_zandaka FROM '.\csv\edaban_zandaka.csv' WITH CSV ENCODING 'SJIS';
\copy ki_shouhizei_setting FROM '.\csv\ki_shouhizei_setting.csv' WITH CSV ENCODING 'SJIS';
\copy bumon_furikae FROM '.\csv\bumon_furikae.csv' WITH CSV ENCODING 'SJIS';
\copy bumon_furikae_meisai FROM '.\csv\bumon_furikae_meisai.csv' WITH CSV ENCODING 'SJIS';
\copy bumon_furikae_setting FROM '.\csv\bumon_furikae_setting.csv' WITH CSV ENCODING 'SJIS';
\copy bumon_tsukekae_control FROM '.\csv\bumon_tsukekae_control.csv' WITH CSV ENCODING 'SJIS';
\copy suitouchou FROM '.\csv\suitouchou.csv' WITH CSV ENCODING 'SJIS';
\copy suitouchou_meisai FROM '.\csv\suitouchou_meisai.csv' WITH CSV ENCODING 'SJIS';
\copy suitouchou_setting FROM '.\csv\suitouchou_setting.csv' WITH CSV ENCODING 'SJIS';
\copy denpyou_ichiran_kyoten FROM '.\csv\denpyou_ichiran_kyoten.csv' WITH CSV ENCODING 'SJIS';
\copy denpyou_ichiran_kyoten_meisai FROM '.\csv\denpyou_ichiran_kyoten_meisai.csv' WITH CSV ENCODING 'SJIS';
DELETE FROM denpyou_serial_no_saiban_kyoten;
\copy denpyou_serial_no_saiban_kyoten FROM '.\csv\denpyou_serial_no_saiban_kyoten.csv' WITH CSV ENCODING 'SJIS';
\copy user_tenpu_file FROM '.\csv\user_tenpu_file.csv' WITH CSV ENCODING 'SJIS';
\copy meisai_tenpu_file_himoduke FROM '.\csv\meisai_tenpu_file_himoduke.csv' WITH CSV ENCODING 'SJIS';
\copy shiwake_pattern_master_zaimu_kyoten FROM '.\csv\shiwake_pattern_master_zaimu_kyoten.csv' WITH CSV ENCODING 'SJIS';
\copy shounin_joukyou_kyoten FROM '.\csv\shounin_joukyou_kyoten.csv' WITH CSV ENCODING 'SJIS';
\copy shounin_route_kyoten FROM '.\csv\shounin_route_kyoten.csv' WITH CSV ENCODING 'SJIS';
\copy teikei_shiwake FROM '.\csv\teikei_shiwake.csv' WITH CSV ENCODING 'SJIS';
\copy teikei_shiwake_meisai FROM '.\csv\teikei_shiwake_meisai.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_nyuryoku_bumon_security FROM '.\csv\zaimu_kyoten_nyuryoku_bumon_security.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_nyuryoku_haifu_bumon FROM '.\csv\zaimu_kyoten_nyuryoku_haifu_bumon.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_nyuryoku_haifu_kamoku FROM '.\csv\zaimu_kyoten_nyuryoku_haifu_kamoku.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_nyuryoku_haifu_pattern FROM '.\csv\zaimu_kyoten_nyuryoku_haifu_pattern.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_nyuryoku_ichiran FROM '.\csv\zaimu_kyoten_nyuryoku_ichiran.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_nyuryoku_kamoku_security FROM '.\csv\zaimu_kyoten_nyuryoku_kamoku_security.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_nyuryoku_nyuryokusha FROM '.\csv\zaimu_kyoten_nyuryoku_nyuryokusha.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban FROM '.\csv\zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_nyuryoku_shounin_route FROM '.\csv\zaimu_kyoten_nyuryoku_shounin_route.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_nyuryoku_tsukekaemoto FROM '.\csv\zaimu_kyoten_nyuryoku_tsukekaemoto.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_nyuryoku_user_info FROM '.\csv\zaimu_kyoten_nyuryoku_user_info.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_shiwake_de3 FROM '.\csv\zaimu_kyoten_shiwake_de3.csv' WITH CSV ENCODING 'SJIS';
\copy zaimu_kyoten_shiwake_sias FROM '.\csv\zaimu_kyoten_shiwake_sias.csv' WITH CSV ENCODING 'SJIS';
\copy bumon_tsukekaesaki FROM '.\csv\bumon_tsukekaesaki.csv' WITH CSV ENCODING 'SJIS';

CREATE TABLE setting_info_tmp AS SELECT * FROM setting_info;
DELETE FROM setting_info_tmp;
\copy setting_info_tmp FROM '.\csv\setting_info.csv' WITH CSV ENCODING 'SJIS';
UPDATE setting_info new SET setting_val = (
 SELECT setting_val
 FROM setting_info_tmp tmp
 WHERE tmp.setting_name = new.setting_name
) WHERE new.setting_name IN (
 SELECT setting_name FROM setting_info_tmp
);
DROP TABLE setting_info_tmp;

CREATE TABLE gamen_koumoku_seigyo_tmp AS SELECT * FROM gamen_koumoku_seigyo;
DELETE FROM gamen_koumoku_seigyo_tmp;
\copy gamen_koumoku_seigyo_tmp FROM '.\csv\gamen_koumoku_seigyo.csv' WITH CSV ENCODING 'SJIS';
UPDATE gamen_koumoku_seigyo new SET
  koumoku_name = (SELECT koumoku_name FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp);
UPDATE gamen_koumoku_seigyo new SET
   hyouji_flg  = (SELECT hyouji_flg   FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
  ,hissu_flg   = (SELECT hissu_flg    FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE hyouji_seigyo_flg = '1');
UPDATE gamen_koumoku_seigyo new SET
  pdf_hyouji_flg = (SELECT pdf_hyouji_flg FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE pdf_hyouji_seigyo_flg = '1');
UPDATE gamen_koumoku_seigyo new SET
  code_output_flg = (SELECT code_output_flg FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE code_output_seigyo_flg = '1');
DROP TABLE gamen_koumoku_seigyo_tmp;

\copy denpyou FROM '.\csv\denpyou.csv' WITH CSV ENCODING 'SJIS';
\copy tsuuchi FROM '.\csv\tsuuchi.csv' WITH CSV ENCODING 'SJIS';

-- アップデート前に実行した仕訳インポート(拠点)のログがeteam側に表示されなくなるように更新する
UPDATE batch_log SET batch_kbn = '2' WHERE batch_name='仕訳インポート(拠点)';

commit;
