SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 設定情報に追加
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- 会社切替設定テーブル
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

-- URL情報テーブル
create table url_info (
 denpyou_id character varying(19) not null
 ,edano integer not null
 ,url character varying not null
 ,constraint url_info_PKEY primary key (denpyou_id,edano,url)
 );
comment on table kaisha_kirikae_settei is '会社切替設定';
comment on column kaisha_kirikae_settei.user_id is 'ユーザーID';
comment on column kaisha_kirikae_settei.scheme_cd is 'スキーマコード';
comment on column kaisha_kirikae_settei.yuukou_kigen_from is '有効期限開始日';
comment on column kaisha_kirikae_settei.yuukou_kigen_to is '有効期限終了日';
comment on column kaisha_kirikae_settei.hyouji_jun is '表示順';
comment on column kaisha_kirikae_settei.touroku_user_id is '登録ユーザーID';
comment on column kaisha_kirikae_settei.touroku_time is '登録日時';
comment on column kaisha_kirikae_settei.koushin_user_id is '更新ユーザーID';
comment on column kaisha_kirikae_settei.koushin_time is '更新日時';

comment on table url_info is 'URL情報';
comment on column url_info.denpyou_id is '伝票ID';
comment on column url_info.edano is '枝番号';
comment on column url_info.url is 'URL';
commit;

