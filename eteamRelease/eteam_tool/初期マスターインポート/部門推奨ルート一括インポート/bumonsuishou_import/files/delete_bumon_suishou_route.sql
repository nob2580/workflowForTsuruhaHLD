SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

delete from bumon_suishou_route_oya;
delete from bumon_suishou_route_ko;
commit;
