SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 駅マスターデータ更新
DELETE FROM eki_master;
\copy eki_master FROM '.\files\csv\eki_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';


commit;
