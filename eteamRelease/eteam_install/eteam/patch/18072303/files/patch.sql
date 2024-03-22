SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- Ýèîñf[^ÏX ¦hyouji_jun900È~ÍJX^}CYÌæ
CREATE TABLE setting_info_tmp AS SELECT * FROM setting_info;
DELETE FROM setting_info WHERE hyouji_jun < 900;
\copy setting_info FROM '.\files\csv\setting_info_tmp.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE setting_info new SET setting_val = (
 SELECT setting_val
 FROM setting_info_tmp tmp
 WHERE tmp.setting_name = new.setting_name
) WHERE new.setting_name IN (
 SELECT setting_name FROM setting_info_tmp
);
DROP TABLE setting_info_tmp;
UPDATE setting_info SET setting_val='0' WHERE setting_name='shiharaiirai_keijou_seigen';


-- æÊÚ§äÌí(x¥ËÖA)
DELETE FROM gamen_koumoku_seigyo WHERE denpyou_kbn = 'A013';


-- àR[hÌí(x¥ËÖA) ¦IvVpR[hÍpb`OÉÈ¯êÎí
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;


-- `[íÊÌí(x¥ËÖA)
DELETE FROM denpyou_shubetsu_ichiran WHERE denpyou_kbn = 'A013';


-- }X^[ÇÌí(x¥ËÖA)
DELETE FROM master_kanri_ichiran WHERE master_id='moto_kouza_shiharaiirai';
DELETE FROM master_kanri_hansuu WHERE master_id='moto_kouza_shiharaiirai';


commit;