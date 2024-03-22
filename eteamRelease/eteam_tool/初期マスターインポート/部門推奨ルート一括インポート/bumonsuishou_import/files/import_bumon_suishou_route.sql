SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

\copy bumon_suishou_route_oya FROM '.\files\csv\bumon_suishou_route_oya.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy bumon_suishou_route_ko  FROM '.\files\csv\bumon_suishou_route_ko.csv'  WITH CSV header ENCODING 'SHIFT-JIS';
commit;
