SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

delete from shiwake_pattern_master;
commit;
