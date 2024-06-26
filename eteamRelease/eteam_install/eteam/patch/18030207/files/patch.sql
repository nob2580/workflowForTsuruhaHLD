SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- àR[h ¦IvVpR[hÍpb`OÉÈ¯êÎí
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

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

-- ob`¼ÌXV
UPDATE batch_log SET batch_name = (CASE batch_name
	WHEN '·ï¼¥\¿o'	THEN	'o£f¢\¿i¼¥\¿jo'
	WHEN '·ï¸Zo'		THEN	'o£·ï¸Zi¼¥¸Zjo'
	WHEN '¼¥\¿o'		THEN	'oïf¢\¿i¼¥\¿jo'
	ELSE							batch_name
END);


commit;