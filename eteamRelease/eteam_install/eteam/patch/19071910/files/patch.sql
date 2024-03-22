SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 連絡票_0939_累計執行高に行削除で削除済みの明細行データが抽出される
-- マスター取込制御
DELETE FROM master_torikomi_ichiran_sias;
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;
