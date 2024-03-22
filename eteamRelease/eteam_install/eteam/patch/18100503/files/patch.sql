SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- ‰æ–Ê€–Ú§Œä‚ÌC³
DELETE FROM gamen_kengen_seigyo WHERE gamen_id='SeikyuushoShime';
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';


commit;