SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �ݒ���f�[�^�ύX ��hyouji_jun900�ȍ~�̓J�X�^�}�C�Y�̈�A1000�Ԉȍ~�͋��_���͌����̈�
CREATE TABLE setting_info_tmp AS SELECT * FROM setting_info;
DELETE FROM setting_info WHERE hyouji_jun < 900;
\copy setting_info FROM '.\files\csv\setting_info_tmp.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE setting_info new SET setting_val = (
 SELECT setting_val
 FROM setting_info_tmp tmp
 WHERE tmp.setting_name = new.setting_name
) WHERE new.setting_name IN (
 SELECT setting_name FROM setting_info_tmp
);
DROP TABLE setting_info_tmp;

-- ebunsho_file�e�[�u����`�X�V
ALTER TABLE ebunsho_file DROP CONSTRAINT IF EXISTS ebunsho_file_PKEY;
ALTER TABLE ebunsho_file RENAME TO ebunsho_file_old;
create table ebunsho_file (
  denpyou_id character varying(19) not null
  , edano integer not null
  , ebunsho_no character varying(19) not null
  , binary_data bytea not null
  , denshitorihiki_flg character varying(1) not null
  , tsfuyo_flg character varying(1) not null
  , touroku_user_id character varying(30)
  , touroku_time timestamp without time zone
  , constraint ebunsho_file_PKEY primary key (denpyou_id,edano)
);

comment on table ebunsho_file is 'e�����t�@�C��';
comment on column ebunsho_file.denpyou_id is '�`�[ID';
comment on column ebunsho_file.edano is '�}�ԍ�';
comment on column ebunsho_file.ebunsho_no is 'e�����ԍ�';
comment on column ebunsho_file.binary_data is '�o�C�i���[�f�[�^';
comment on column ebunsho_file.denshitorihiki_flg is '�d�q����t���O';
comment on column ebunsho_file.tsfuyo_flg is '�^�C���X�^���v�t�^�t���O';
comment on column ebunsho_file.touroku_user_id is '�o�^���[�U�[ID';
comment on column ebunsho_file.touroku_time is '�o�^����';

INSERT INTO ebunsho_file
SELECT
  denpyou_id
  , edano
  , ebunsho_no
  , binary_data
  , '0' -- denshitorihiki_flg
  , '0' -- tsfuyo_flg
  , touroku_user_id
  , touroku_time
FROM ebunsho_file_old;
DROP TABLE ebunsho_file_old;

alter table ebunsho_file add constraint ebunsho_file_ebunsho_no_key
  unique (ebunsho_no) ;

-- �o�X�H�����}�X�^�[�f�[�^�X�V
DELETE FROM BUS_LINE_MASTER;
\copy BUS_LINE_MASTER FROM '.\files\csv\bus_line_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;
