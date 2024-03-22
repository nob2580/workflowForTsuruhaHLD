SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--  ICカード利用履歴テーブルに処理内容カラム、残高カラム、全バイト配列カラムを追加
ALTER TABLE ic_card_rireki ADD COLUMN IF NOT EXISTS shori_cd character varying(6) NOT NULL default '';
comment on column ic_card_rireki.shori_cd is '処理内容';
ALTER TABLE ic_card_rireki ADD COLUMN IF NOT EXISTS balance numeric(15) NOT NULL default 0;
comment on column ic_card_rireki.balance is '残高';
ALTER TABLE ic_card_rireki ADD COLUMN IF NOT EXISTS all_byte character varying NOT NULL default '';
comment on column ic_card_rireki.all_byte is '全バイト配列';

commit;
