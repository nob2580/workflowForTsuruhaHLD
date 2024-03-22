SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- �w�}�X�^�[�f�[�^�X�V
DROP TABLE eki_master;
create table eki_master (
  region_cd character varying(3) not null
  , line_cd character varying(3) not null
  , eki_cd character varying(3) not null
  , line_name character varying not null
  , eki_name character varying not null
  , constraint eki_master_PKEY primary key (region_cd,line_cd,eki_cd)
);
comment on table eki_master is '�w�}�X�^�[';
comment on column eki_master.region_cd is '�n��R�[�h';
comment on column eki_master.line_cd is '�H���R�[�h';
comment on column eki_master.eki_cd is '�w�R�[�h';
comment on column eki_master.line_name is '�H����';
comment on column eki_master.eki_name is '�w��';
\copy eki_master FROM '.\files\csv\eki_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';


commit;
