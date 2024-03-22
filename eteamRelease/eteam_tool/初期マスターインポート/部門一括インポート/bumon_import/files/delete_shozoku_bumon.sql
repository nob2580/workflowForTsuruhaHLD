SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

delete from shozoku_bumon where bumon_cd <> '0000';
commit;
