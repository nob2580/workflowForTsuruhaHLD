SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 残高の貸方11に貸方10が入っていた問題
DELETE FROM master_torikomi_shousai_de3;
DELETE FROM master_torikomi_shousai_sias;
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;
