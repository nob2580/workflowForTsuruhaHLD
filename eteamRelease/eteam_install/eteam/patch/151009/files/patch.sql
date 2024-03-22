SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- helpカラムサイズ拡張
ALTER TABLE help ALTER COLUMN help_rich_text TYPE VARCHAR(50000);

-- informationカラムサイズ拡張
ALTER TABLE information ALTER COLUMN tsuuchi_naiyou TYPE VARCHAR(3000);

-- gamen_kengen_seigyoテーブルレコード追加
DELETE FROM gamen_kengen_seigyo WHERE gamen_id IN ('HiDaikouUserSentaku','DaikoushaShitei','ShinseichuuDenpyouIchiran');
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- kinou_seigyoテーブルレコード追加
DELETE FROM kinou_seigyo WHERE kinou_seigyo_cd IN ('KD','ST','SS');
\copy kinou_seigyo FROM '.\files\csv\kinou_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- naibu_cd_settingテーブルレコード追加
DELETE FROM naibu_cd_setting WHERE (naibu_cd_name = 'kinou_seigyo_cd' AND naibu_cd IN ('KD','ST','SS')) OR (naibu_cd_name = 'kanren_denpyou');
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- setting_infoテーブルレコード追加
CREATE TABLE setting_info_tmp AS SELECT * FROM setting_info;
DELETE FROM setting_info;
\copy setting_info FROM '.\files\csv\setting_info.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE setting_info new SET setting_val = (
	SELECT setting_val
	FROM setting_info_tmp tmp
	WHERE tmp.setting_name = new.setting_name
) WHERE new.setting_name IN (
	SELECT setting_name FROM setting_info_tmp
);
DROP TABLE setting_info_tmp;

-- password移行
ALTER TABLE password ALTER COLUMN password TYPE VARCHAR;
UPDATE password p1 SET password = (SELECT public.pgp_sym_encrypt(password,'eteam') from password p2 where p1.user_id = p2.user_id );


commit;
