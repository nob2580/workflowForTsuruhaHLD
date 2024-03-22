SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 伝票テーブル.シリアル番号データ属性変更
alter table denpyou alter column serial_no type bigint;

commit;
