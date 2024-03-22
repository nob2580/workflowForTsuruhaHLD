SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

\copy shozoku_bumon FROM '.\files\csv\shozoku_bumon.csv' WITH CSV header ENCODING 'SHIFT-JIS';
commit;
