SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- shiwake_pattern_setting “ü‚ê‘Ö‚¦
CREATE TABLE shiwake_pattern_setting_tmp AS SELECT * FROM shiwake_pattern_setting WHERE 1 = 2;
\copy shiwake_pattern_setting_tmp FROM '.\files\csv\shiwake_pattern_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM shiwake_pattern_setting_tmp WHERE denpyou_kbn='A003' AND (SELECT COUNT(*) FROM shiwake_pattern_setting WHERE denpyou_kbn='A003') = 0;
DELETE FROM shiwake_pattern_setting;
INSERT INTO shiwake_pattern_setting SELECT * FROM shiwake_pattern_setting_tmp;
DROP TABLE shiwake_pattern_setting_tmp;


commit;