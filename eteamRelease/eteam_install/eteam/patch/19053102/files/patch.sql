SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- ICSP˜A—•[_0782 OPEN21‚©‚ç•‰’S•”–å‰È–Ú—\Z‚ªæ“¾‚³‚ê‚È‚¢
DELETE FROM master_torikomi_ichiran_de3;
DELETE FROM master_torikomi_ichiran_sias;
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;
