SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 連絡票No.707 SIAS-MK2対応
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

comment on table master_torikomi_ichiran_mk2 is 'マスター取込一覧(SIAS_mk2)';
comment on column master_torikomi_ichiran_mk2.master_id is 'マスターID';
comment on column master_torikomi_ichiran_mk2.master_name is 'マスター名';
comment on column master_torikomi_ichiran_mk2.op_master_id is 'OPEN21マスターID';
comment on column master_torikomi_ichiran_mk2.op_master_name is 'OPEN21マスター名';
comment on column master_torikomi_ichiran_mk2.torikomi_kahi_flg is '取込可否フラグ';

comment on table master_torikomi_shousai_mk2 is 'マスター取込詳細(SIAS_mk2)';
comment on column master_torikomi_shousai_mk2.master_id is 'マスターID';
comment on column master_torikomi_shousai_mk2.et_column_id is 'eTeamカラムID';
comment on column master_torikomi_shousai_mk2.et_column_name is 'eTeamカラム名';
comment on column master_torikomi_shousai_mk2.et_data_type is 'eTeamデータ型';
comment on column master_torikomi_shousai_mk2.op_colume_id is 'OPEN21カラムID';
comment on column master_torikomi_shousai_mk2.op_column_name is 'OPEN21カラム名';
comment on column master_torikomi_shousai_mk2.op_data_type is 'OPEN21データ型';
comment on column master_torikomi_shousai_mk2.entry_order is '登録順';
comment on column master_torikomi_shousai_mk2.pk_flg is 'プライマリーキーフラグ';

comment on table master_torikomi_term_ichiran_mk2 is 'マスター取込期間一覧(SIAS_mk2)';
comment on column master_torikomi_term_ichiran_mk2.master_id is 'マスターID';
comment on column master_torikomi_term_ichiran_mk2.master_name is 'マスター名';
comment on column master_torikomi_term_ichiran_mk2.op_master_id is 'OPEN21マスターID';
comment on column master_torikomi_term_ichiran_mk2.op_master_name is 'OPEN21マスター名';
comment on column master_torikomi_term_ichiran_mk2.torikomi_kahi_flg is '取込可否フラグ';

comment on table master_torikomi_term_shousai_mk2 is 'マスター取込期間詳細(SIAS_mk2)';
comment on column master_torikomi_term_shousai_mk2.master_id is 'マスターID';
comment on column master_torikomi_term_shousai_mk2.et_column_id is 'eTeamカラムID';
comment on column master_torikomi_term_shousai_mk2.et_column_name is 'eTeamカラム名';
comment on column master_torikomi_term_shousai_mk2.et_data_type is 'eTeamデータ型';
comment on column master_torikomi_term_shousai_mk2.op_colume_id is 'OPEN21カラムID';
comment on column master_torikomi_term_shousai_mk2.op_column_name is 'OPEN21カラム名';
comment on column master_torikomi_term_shousai_mk2.op_data_type is 'OPEN21データ型';
comment on column master_torikomi_term_shousai_mk2.entry_order is '登録順';
comment on column master_torikomi_term_shousai_mk2.pk_flg is 'プライマリーキーフラグ';

--インポート用SQL初期データ登録
\copy master_torikomi_ichiran_mk2 FROM '.\files\csv\master_torikomi_ichiran_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_mk2 FROM '.\files\csv\master_torikomi_shousai_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_term_ichiran_mk2 FROM '.\files\csv\master_torikomi_term_ichiran_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_term_shousai_mk2 FROM '.\files\csv\master_torikomi_term_shousai_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- 連絡票No.957 「出張先・訪問先」入力可能文字数拡張
ALTER TABLE denpyou_ichiran ALTER COLUMN houmonsaki TYPE character varying(200);
ALTER TABLE ryohiseisan ALTER COLUMN houmonsaki TYPE character varying(200);
ALTER TABLE ryohi_karibarai ALTER COLUMN houmonsaki TYPE character varying(200);
ALTER TABLE kaigai_ryohiseisan ALTER COLUMN houmonsaki TYPE character varying(200);
ALTER TABLE kaigai_ryohi_karibarai ALTER COLUMN houmonsaki TYPE character varying(200);

commit;
