SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 設定情報データ変更 ※hyouji_jun900以降はカスタマイズ領域、1000番以降は拠点入力向け領域
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

-- ebunsho_fileテーブル定義更新
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

comment on table ebunsho_file is 'e文書ファイル';
comment on column ebunsho_file.denpyou_id is '伝票ID';
comment on column ebunsho_file.edano is '枝番号';
comment on column ebunsho_file.ebunsho_no is 'e文書番号';
comment on column ebunsho_file.binary_data is 'バイナリーデータ';
comment on column ebunsho_file.denshitorihiki_flg is '電子取引フラグ';
comment on column ebunsho_file.tsfuyo_flg is 'タイムスタンプ付与フラグ';
comment on column ebunsho_file.touroku_user_id is '登録ユーザーID';
comment on column ebunsho_file.touroku_time is '登録日時';

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

-- バス路線名マスターデータ更新
DELETE FROM BUS_LINE_MASTER;
\copy BUS_LINE_MASTER FROM '.\files\csv\bus_line_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;
