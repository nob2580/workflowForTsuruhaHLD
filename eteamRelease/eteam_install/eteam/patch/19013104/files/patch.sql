SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--定期情報イントラ版用定義更新
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
comment on table teiki_jouhou is '定期券情報';
comment on column teiki_jouhou.user_id is 'ユーザーID';
comment on column teiki_jouhou.shiyou_kaishibi is '使用開始日';
comment on column teiki_jouhou.shiyou_shuuryoubi is '使用終了日';
comment on column teiki_jouhou.intra_teiki_kukan is 'イントラ版定期区間';
comment on column teiki_jouhou.intra_restoreroute is 'イントラ版方向性付き定期経路文字列';
comment on column teiki_jouhou.web_teiki_kukan is '定期区間情報';
comment on column teiki_jouhou.web_teiki_serialize_data is '定期区間シリアライズデータ';
comment on column teiki_jouhou.touroku_user_id is '登録ユーザーID';
comment on column teiki_jouhou.touroku_time is '登録日時';
comment on column teiki_jouhou.koushin_user_id is '更新ユーザーID';
comment on column teiki_jouhou.koushin_time is '更新日時';
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