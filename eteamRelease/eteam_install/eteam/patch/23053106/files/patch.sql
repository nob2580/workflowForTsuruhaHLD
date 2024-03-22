SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


--ì‡ïîÉRÅ[Éhê›íË
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting WHERE naibu_cd_name NOT IN ('kazei_kbn_kyoten_furikae', 'shiwake_pattern_denpyou_kbn_kyoten', 'fusen_color', 'ebunsho_shubetsu_kyoten');
\copy naibu_cd_setting FROM '..\..\work\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DROP TABLE naibu_cd_setting_tmp;


commit;
