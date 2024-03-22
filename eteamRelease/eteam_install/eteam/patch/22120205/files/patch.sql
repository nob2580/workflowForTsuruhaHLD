SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- kinou_seigyoÇ÷ÇÃÉfÅ[É^í«â¡
INSERT INTO kinou_seigyo VALUES ('CP', '1') ON CONFLICT DO NOTHING;


commit;