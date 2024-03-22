SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


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


--- shiwakeテーブルにカラム追加
ALTER TABLE shiwake ADD COLUMN GSEP VARCHAR(1);
UPDATE shiwake SET GSEP = '';
ALTER TABLE shiwake ALTER COLUMN GSEP SET NOT NULL;
COMMENT ON COLUMN SHIWAKE.GSEP IS '（オープン２１）行区切り';


commit;
