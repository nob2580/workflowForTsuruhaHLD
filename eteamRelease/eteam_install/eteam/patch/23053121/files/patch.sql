SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--マスター取り込み一覧関係
DELETE FROM master_torikomi_ichiran_de3 WHERE master_id = 'torihikisaki';
DELETE FROM master_torikomi_ichiran_sias WHERE master_id = 'torihikisaki';
DELETE FROM master_torikomi_ichiran_mk2 WHERE master_id = 'torihikisaki';
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_mk2 FROM '.\files\csv\master_torikomi_ichiran_mk2_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;
