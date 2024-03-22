SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �A���[No.707 SIAS-MK2�Ή�
create table master_torikomi_ichiran_mk2 (
  master_id character varying(50) not null
  , master_name character varying(50) not null
  , op_master_id character varying not null
  , op_master_name character varying not null
  , torikomi_kahi_flg character varying(1) not null
  , constraint master_torikomi_ichiran_mk2_PKEY primary key (master_id)
);

create table master_torikomi_shousai_mk2 (
  master_id character varying(50) not null
  , et_column_id character varying not null
  , et_column_name character varying not null
  , et_data_type character varying not null
  , op_colume_id character varying not null
  , op_column_name character varying not null
  , op_data_type character varying not null
  , entry_order integer not null
  , pk_flg character varying(1) not null
  , constraint master_torikomi_shousai_mk2_PKEY primary key (master_id,et_column_id)
);

create table master_torikomi_term_ichiran_mk2 (
  master_id character varying(50) not null
  , master_name character varying(50) not null
  , op_master_id character varying not null
  , op_master_name character varying not null
  , torikomi_kahi_flg character varying(1) not null
  , constraint master_torikomi_term_ichiran_mk2_PKEY primary key (master_id)
);

create table master_torikomi_term_shousai_mk2 (
  master_id character varying(50) not null
  , et_column_id character varying not null
  , et_column_name character varying not null
  , et_data_type character varying not null
  , op_colume_id character varying not null
  , op_column_name character varying not null
  , op_data_type character varying not null
  , entry_order integer not null
  , pk_flg character varying(1) not null
  , constraint master_torikomi_term_shousai_mk2_PKEY primary key (master_id,et_column_id)
);

comment on table master_torikomi_ichiran_mk2 is '�}�X�^�[�捞�ꗗ(SIAS_mk2)';
comment on column master_torikomi_ichiran_mk2.master_id is '�}�X�^�[ID';
comment on column master_torikomi_ichiran_mk2.master_name is '�}�X�^�[��';
comment on column master_torikomi_ichiran_mk2.op_master_id is 'OPEN21�}�X�^�[ID';
comment on column master_torikomi_ichiran_mk2.op_master_name is 'OPEN21�}�X�^�[��';
comment on column master_torikomi_ichiran_mk2.torikomi_kahi_flg is '�捞�ۃt���O';

comment on table master_torikomi_shousai_mk2 is '�}�X�^�[�捞�ڍ�(SIAS_mk2)';
comment on column master_torikomi_shousai_mk2.master_id is '�}�X�^�[ID';
comment on column master_torikomi_shousai_mk2.et_column_id is 'eTeam�J����ID';
comment on column master_torikomi_shousai_mk2.et_column_name is 'eTeam�J������';
comment on column master_torikomi_shousai_mk2.et_data_type is 'eTeam�f�[�^�^';
comment on column master_torikomi_shousai_mk2.op_colume_id is 'OPEN21�J����ID';
comment on column master_torikomi_shousai_mk2.op_column_name is 'OPEN21�J������';
comment on column master_torikomi_shousai_mk2.op_data_type is 'OPEN21�f�[�^�^';
comment on column master_torikomi_shousai_mk2.entry_order is '�o�^��';
comment on column master_torikomi_shousai_mk2.pk_flg is '�v���C�}���[�L�[�t���O';

comment on table master_torikomi_term_ichiran_mk2 is '�}�X�^�[�捞���Ԉꗗ(SIAS_mk2)';
comment on column master_torikomi_term_ichiran_mk2.master_id is '�}�X�^�[ID';
comment on column master_torikomi_term_ichiran_mk2.master_name is '�}�X�^�[��';
comment on column master_torikomi_term_ichiran_mk2.op_master_id is 'OPEN21�}�X�^�[ID';
comment on column master_torikomi_term_ichiran_mk2.op_master_name is 'OPEN21�}�X�^�[��';
comment on column master_torikomi_term_ichiran_mk2.torikomi_kahi_flg is '�捞�ۃt���O';

comment on table master_torikomi_term_shousai_mk2 is '�}�X�^�[�捞���ԏڍ�(SIAS_mk2)';
comment on column master_torikomi_term_shousai_mk2.master_id is '�}�X�^�[ID';
comment on column master_torikomi_term_shousai_mk2.et_column_id is 'eTeam�J����ID';
comment on column master_torikomi_term_shousai_mk2.et_column_name is 'eTeam�J������';
comment on column master_torikomi_term_shousai_mk2.et_data_type is 'eTeam�f�[�^�^';
comment on column master_torikomi_term_shousai_mk2.op_colume_id is 'OPEN21�J����ID';
comment on column master_torikomi_term_shousai_mk2.op_column_name is 'OPEN21�J������';
comment on column master_torikomi_term_shousai_mk2.op_data_type is 'OPEN21�f�[�^�^';
comment on column master_torikomi_term_shousai_mk2.entry_order is '�o�^��';
comment on column master_torikomi_term_shousai_mk2.pk_flg is '�v���C�}���[�L�[�t���O';

--�C���|�[�g�pSQL�����f�[�^�o�^
\copy master_torikomi_ichiran_mk2 FROM '.\files\csv\master_torikomi_ichiran_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_mk2 FROM '.\files\csv\master_torikomi_shousai_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_term_ichiran_mk2 FROM '.\files\csv\master_torikomi_term_ichiran_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_term_shousai_mk2 FROM '.\files\csv\master_torikomi_term_shousai_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- �A���[No.957 �u�o����E�K���v���͉\�������g��
ALTER TABLE denpyou_ichiran ALTER COLUMN houmonsaki TYPE character varying(200);
ALTER TABLE ryohiseisan ALTER COLUMN houmonsaki TYPE character varying(200);
ALTER TABLE ryohi_karibarai ALTER COLUMN houmonsaki TYPE character varying(200);
ALTER TABLE kaigai_ryohiseisan ALTER COLUMN houmonsaki TYPE character varying(200);
ALTER TABLE kaigai_ryohi_karibarai ALTER COLUMN houmonsaki TYPE character varying(200);

commit;
