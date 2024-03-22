SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

delete from user_info where user_id <> 'admin';
delete from shozoku_bumon_wariate where user_id <> 'admin';

commit;
