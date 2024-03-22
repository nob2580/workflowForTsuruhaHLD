SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 連絡票No.925 法人カード利用一覧除外機能の追加
-- 連絡票No.803_取引先子画面のみ1ページ表示件数個別保持
-- 設定情報データ変更 ※hyouji_jun900以降はカスタマイズ領域
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

-- 連絡票No.983 内部コード設定 部品形式「金額(小数点あり)」削除
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- 支払依頼申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- 請求書払申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

-- 取引先検索子画面の表示件数初期値はrecord_num_per_pageの元値に合わせる
UPDATE setting_info set setting_val = (SELECT setting_val FROM setting_info WHERE setting_name ='record_num_per_page') WHERE setting_name = 'record_num_per_page_torihikisaki';

-- 法人カード情報テーブル更新
ALTER TABLE houjin_card_jouhou DROP CONSTRAINT IF EXISTS houjin_card_jouhou_PKEY;
ALTER TABLE houjin_card_jouhou RENAME TO houjin_card_jouhou_old;
create table houjin_card_jouhou (
  card_jouhou_id bigserial not null
  , card_shubetsu character varying(3) not null
  , torikomi_denpyou_id character varying(19) not null
  , busho_cd character varying(15) not null
  , shain_bangou character varying(16) not null
  , shiyousha character varying(30) not null
  , riyoubi date not null
  , kingaku numeric(15) not null
  , card_bangou character varying(16) not null
  , kameiten character varying(60) not null
  , gyoushu_cd character varying(15) not null
  , jyogai_flg character varying(1) not null
  , jyogai_riyuu character varying(60) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint houjin_card_jouhou_PKEY primary key (card_jouhou_id)
);
comment on table houjin_card_jouhou is '法人カード使用履歴情報';
comment on column houjin_card_jouhou.card_jouhou_id is 'カード情報ID';
comment on column houjin_card_jouhou.card_shubetsu is 'カード種別コード';
comment on column houjin_card_jouhou.torikomi_denpyou_id is '取込先伝票ID';
comment on column houjin_card_jouhou.busho_cd is '部署コード';
comment on column houjin_card_jouhou.shain_bangou is '社員番号';
comment on column houjin_card_jouhou.shiyousha is '使用者';
comment on column houjin_card_jouhou.riyoubi is '利用日';
comment on column houjin_card_jouhou.kingaku is '金額';
comment on column houjin_card_jouhou.card_bangou is 'カード番号';
comment on column houjin_card_jouhou.kameiten is '加盟店';
comment on column houjin_card_jouhou.gyoushu_cd is '業種コード';
comment on column houjin_card_jouhou.jyogai_flg is '除外フラグ';
comment on column houjin_card_jouhou.jyogai_riyuu is '除外理由';
comment on column houjin_card_jouhou.touroku_user_id is '登録ユーザーID';
comment on column houjin_card_jouhou.touroku_time is '登録日時';
comment on column houjin_card_jouhou.koushin_user_id is '更新ユーザーID';
comment on column houjin_card_jouhou.koushin_time is '更新日時';
INSERT INTO houjin_card_jouhou
SELECT
card_jouhou_id
  , card_shubetsu
  , torikomi_denpyou_id
  , busho_cd
  , shain_bangou
  , shiyousha
  , riyoubi
  , kingaku
  , card_bangou
  , kameiten
  , gyoushu_cd
  , '0' -- jyogai_flg
  , '' -- jyogai_riyuu
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM houjin_card_jouhou_old;
DROP TABLE houjin_card_jouhou_old;


commit;
