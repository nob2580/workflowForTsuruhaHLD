SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

delete from shozoku_bumon_shiwake_pattern_master;
commit;
