SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- マスター取込期間一覧
DELETE FROM master_torikomi_term_ichiran_de3;
\copy master_torikomi_term_ichiran_de3 FROM '.\files\csv\master_torikomi_term_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM master_torikomi_term_ichiran_sias;
\copy master_torikomi_term_ichiran_sias FROM '.\files\csv\master_torikomi_term_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';


commit;