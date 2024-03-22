SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS zeigaku_fix_flg character varying(1) default 0 not null;
comment on column kaigai_ryohiseisan_meisai.zeigaku_fix_flg is '�Ŋz�C���t���O';
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS zeigaku_fix_flg character varying(1) default 0 not null;
comment on column koutsuuhiseisan_meisai.zeigaku_fix_flg is '�Ŋz�C���t���O';
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS zeigaku_fix_flg character varying(1) default 0 not null;
comment on column ryohiseisan_meisai.zeigaku_fix_flg is '�Ŋz�C���t���O';


commit;
