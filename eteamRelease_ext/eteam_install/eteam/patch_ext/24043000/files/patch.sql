SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �ݒ���ɒǉ�
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- ��Аؑ֐ݒ�e�[�u��
create table kaisha_kirikae_settei (
  user_id character varying(30) not null
  , scheme_cd character varying(30) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , hyouji_jun integer not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp(6) without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp(6) without time zone not null
  , constraint kaisha_kirikae_settei_PKEY primary key (user_id,scheme_cd,yuukou_kigen_from)
);

-- URL���e�[�u��
create table url_info (
 denpyou_id character varying(19) not null
 ,edano integer not null
 ,url character varying not null
 ,constraint url_info_PKEY primary key (denpyou_id,edano,url)
 );
comment on table kaisha_kirikae_settei is '��Аؑ֐ݒ�';
comment on column kaisha_kirikae_settei.user_id is '���[�U�[ID';
comment on column kaisha_kirikae_settei.scheme_cd is '�X�L�[�}�R�[�h';
comment on column kaisha_kirikae_settei.yuukou_kigen_from is '�L�������J�n��';
comment on column kaisha_kirikae_settei.yuukou_kigen_to is '�L�������I����';
comment on column kaisha_kirikae_settei.hyouji_jun is '�\����';
comment on column kaisha_kirikae_settei.touroku_user_id is '�o�^���[�U�[ID';
comment on column kaisha_kirikae_settei.touroku_time is '�o�^����';
comment on column kaisha_kirikae_settei.koushin_user_id is '�X�V���[�U�[ID';
comment on column kaisha_kirikae_settei.koushin_time is '�X�V����';

comment on table url_info is 'URL���';
comment on column url_info.denpyou_id is '�`�[ID';
comment on column url_info.edano is '�}�ԍ�';
comment on column url_info.url is 'URL';
commit;

