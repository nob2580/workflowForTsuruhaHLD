SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

\copy shiwake_pattern_master FROM '.\files\csv\shiwake_pattern_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';
commit;
