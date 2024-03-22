SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 法人カード利用履歴テーブル
ALTER TABLE houjin_card_jouhou ALTER COLUMN shain_bangou TYPE character varying(20);

commit;
