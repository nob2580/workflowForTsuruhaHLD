SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �@�l�J�[�h���p�����e�[�u��
ALTER TABLE houjin_card_jouhou ALTER COLUMN shain_bangou TYPE character varying(20);

commit;
