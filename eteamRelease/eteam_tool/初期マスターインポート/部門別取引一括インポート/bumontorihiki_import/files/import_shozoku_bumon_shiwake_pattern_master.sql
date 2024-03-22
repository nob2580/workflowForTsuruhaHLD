SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

\copy shozoku_bumon_shiwake_pattern_master FROM '.\files\csv\shozoku_bumon_shiwake_pattern_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';
commit;
