SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--������C���g���ŗp��`�X�V
alter table teiki_jouhou rename to teiki_jouhou_old;
create table teiki_jouhou (
  user_id character varying(30) not null
  , shiyou_kaishibi date not null
  , shiyou_shuuryoubi date not null
  , intra_teiki_kukan character varying
  , intra_restoreroute character varying not null
  , web_teiki_kukan character varying
  , web_teiki_serialize_data character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (user_id,shiyou_kaishibi,shiyou_shuuryoubi)
);
comment on table teiki_jouhou is '��������';
comment on column teiki_jouhou.user_id is '���[�U�[ID';
comment on column teiki_jouhou.shiyou_kaishibi is '�g�p�J�n��';
comment on column teiki_jouhou.shiyou_shuuryoubi is '�g�p�I����';
comment on column teiki_jouhou.intra_teiki_kukan is '�C���g���Œ�����';
comment on column teiki_jouhou.intra_restoreroute is '�C���g���ŕ������t������o�H������';
comment on column teiki_jouhou.web_teiki_kukan is '�����ԏ��';
comment on column teiki_jouhou.web_teiki_serialize_data is '�����ԃV���A���C�Y�f�[�^';
comment on column teiki_jouhou.touroku_user_id is '�o�^���[�U�[ID';
comment on column teiki_jouhou.touroku_time is '�o�^����';
comment on column teiki_jouhou.koushin_user_id is '�X�V���[�U�[ID';
comment on column teiki_jouhou.koushin_time is '�X�V����';
INSERT INTO teiki_jouhou
SELECT 
  user_id
  , shiyou_kaishibi
  , shiyou_shuuryoubi
  , null
  , ''
  , web_teiki_kukan
  , web_teiki_serialize_data
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM teiki_jouhou_old;
drop table teiki_jouhou_old;


commit;