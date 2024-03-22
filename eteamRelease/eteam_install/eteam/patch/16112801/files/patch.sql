SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- 科目マスターに仮勘定消込No仕様フラグ追加
ALTER TABLE kamoku_master ADD COLUMN karikanjou_keshikomi_no_flg SMALLINT;
COMMENT ON COLUMN kamoku_master.karikanjou_keshikomi_no_flg IS '仮勘定消込No使用フラグ';
UPDATE kamoku_master SET karikanjou_keshikomi_no_flg = 0;


-- マスター取込に科目マスターの仮勘定消込No仕様フラグ追加
DELETE FROM MASTER_TORIKOMI_ICHIRAN_DE3;
\copy MASTER_TORIKOMI_ICHIRAN_DE3  FROM '.\files\csv\master_torikomi_ichiran_de3.csv'  WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM MASTER_TORIKOMI_SHOUSAI_DE3;
\copy MASTER_TORIKOMI_SHOUSAI_DE3  FROM '.\files\csv\master_torikomi_shousai_de3.csv'  WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM MASTER_TORIKOMI_ICHIRAN_SIAS;
\copy MASTER_TORIKOMI_ICHIRAN_SIAS FROM '.\files\csv\master_torikomi_ichiran_sias.csv'       WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM MASTER_TORIKOMI_SHOUSAI_SIAS;
\copy MASTER_TORIKOMI_SHOUSAI_SIAS FROM '.\files\csv\master_torikomi_shousai_sias.csv'       WITH CSV header ENCODING 'SHIFT-JIS';


commit;
