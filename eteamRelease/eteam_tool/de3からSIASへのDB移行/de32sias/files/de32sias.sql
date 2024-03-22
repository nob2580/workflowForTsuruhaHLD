SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 設定情報データ 変更
CREATE TABLE setting_info_tmp AS SELECT * FROM setting_info;
DELETE FROM setting_info;
\copy setting_info FROM 'c:\eteam\work\files\csv\setting_info_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE setting_info new SET setting_val = (
 SELECT setting_val
 FROM setting_info_tmp tmp
 WHERE tmp.setting_name = new.setting_name
)
WHERE
  new.setting_name IN (SELECT setting_name FROM setting_info_tmp) AND
  new.editable_flg <> '0';
DROP TABLE setting_info_tmp;

-- マスター管理初期レコード 追加
CREATE TABLE master_kanri_hansuu_diff AS SELECT * FROM master_kanri_hansuu WHERE 1 = 2;
\copy master_kanri_hansuu_diff FROM 'C:/eteam/work/files/csv/master_kanri_hansuu_sias.csv' WITH CSV header;
INSERT INTO master_kanri_hansuu (SELECT * FROM master_kanri_hansuu_diff diff WHERE diff.master_id NOT IN (SELECT master_id FROM master_kanri_hansuu));
DROP TABLE master_kanri_hansuu_diff;

CREATE TABLE master_kanri_ichiran_diff AS SELECT * FROM master_kanri_ichiran WHERE 1 = 2;
\copy master_kanri_ichiran_diff FROM 'C:/eteam/work/files/csv/master_kanri_ichiran_sias.csv' WITH CSV header;
INSERT INTO master_kanri_ichiran (SELECT * FROM master_kanri_ichiran_diff diff WHERE diff.master_id NOT IN (SELECT master_id FROM master_kanri_ichiran));
DROP TABLE master_kanri_ichiran_diff;

-- 未転記の仕訳は転記対象外
UPDATE shiwake_de3 SET shiwake_status='9', koushin_time = current_timestamp WHERE shiwake_status='0';

commit;
