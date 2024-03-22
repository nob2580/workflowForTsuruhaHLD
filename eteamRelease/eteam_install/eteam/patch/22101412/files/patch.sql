SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--  IC�J�[�h���p�����e�[�u���ɏ������e�J�����A�c���J�����A�S�o�C�g�z��J������ǉ�
ALTER TABLE ic_card_rireki ADD COLUMN IF NOT EXISTS shori_cd character varying(6) NOT NULL default '';
comment on column ic_card_rireki.shori_cd is '�������e';
ALTER TABLE ic_card_rireki ADD COLUMN IF NOT EXISTS balance numeric(15) NOT NULL default 0;
comment on column ic_card_rireki.balance is '�c��';
ALTER TABLE ic_card_rireki ADD COLUMN IF NOT EXISTS all_byte character varying NOT NULL default '';
comment on column ic_card_rireki.all_byte is '�S�o�C�g�z��';

commit;
