SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 画面項目制御
CREATE TABLE gamen_koumoku_seigyo_tmp AS SELECT * FROM gamen_koumoku_seigyo;
DELETE FROM gamen_koumoku_seigyo WHERE denpyou_kbn LIKE 'A%';
\copy gamen_koumoku_seigyo FROM '..\..\work\files\csv\gamen_koumoku_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE gamen_koumoku_seigyo new SET
  koumoku_name = (SELECT koumoku_name FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp);
UPDATE gamen_koumoku_seigyo new SET
   hyouji_flg  = (SELECT hyouji_flg   FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
  ,hissu_flg   = (SELECT hissu_flg    FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE hyouji_seigyo_flg = '1');
UPDATE gamen_koumoku_seigyo new SET
  pdf_hyouji_flg = (SELECT pdf_hyouji_flg FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE pdf_hyouji_seigyo_flg = '1');
UPDATE gamen_koumoku_seigyo new SET
  code_output_flg = (SELECT code_output_flg FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE code_output_seigyo_flg = '1');
-- ▼▼K-133 ここから
-- アップデート前に項目値がデフォルトから未変更の項目は、項目名をデフォルト項目名と同じにする
UPDATE gamen_koumoku_seigyo new SET
  koumoku_name = default_koumoku_name
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE default_koumoku_name != '' AND default_koumoku_name = koumoku_name AND denpyou_kbn = 'Z001');
-- ▲▲ここまで
DROP TABLE gamen_koumoku_seigyo_tmp;

-- 交通費精算明細テーブルに早フラグ、安フラグ、楽フラグを追加
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS haya_flg character varying(1) NOT NULL default '0';
comment on column koutsuuhiseisan_meisai.haya_flg is '早フラグ';
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS yasu_flg character varying(1) NOT NULL default '0';
comment on column koutsuuhiseisan_meisai.yasu_flg is '安フラグ';
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS raku_flg character varying(1) NOT NULL default '0';
comment on column koutsuuhiseisan_meisai.raku_flg is '楽フラグ';

-- 海外旅費精算明細テーブルに早フラグ、安フラグ、楽フラグを追加
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS haya_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohiseisan_meisai.haya_flg is '早フラグ';
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS yasu_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohiseisan_meisai.yasu_flg is '安フラグ';
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS raku_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohiseisan_meisai.raku_flg is '楽フラグ';


-- 海外旅費仮払明細テーブルに早フラグ、安フラグ、楽フラグを追加
ALTER TABLE kaigai_ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS haya_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohi_karibarai_meisai.haya_flg is '早フラグ';
ALTER TABLE kaigai_ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS yasu_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohi_karibarai_meisai.yasu_flg is '安フラグ';
ALTER TABLE kaigai_ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS raku_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohi_karibarai_meisai.raku_flg is '楽フラグ';

-- 旅費精算明細テーブルに早フラグ、安フラグ、楽フラグを追加
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS haya_flg character varying(1) NOT NULL default '0';
comment on column ryohiseisan_meisai.haya_flg is '早フラグ';
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS yasu_flg character varying(1) NOT NULL default '0';
comment on column ryohiseisan_meisai.yasu_flg is '安フラグ';
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS raku_flg character varying(1) NOT NULL default '0';
comment on column ryohiseisan_meisai.raku_flg is '楽フラグ';


-- 旅費仮払明細テーブルに早フラグ、安フラグ、楽フラグを追加
ALTER TABLE ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS haya_flg character varying(1) NOT NULL default '0';
comment on column ryohi_karibarai_meisai.haya_flg is '早フラグ';
ALTER TABLE ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS yasu_flg character varying(1) NOT NULL default '0';
comment on column ryohi_karibarai_meisai.yasu_flg is '安フラグ';
ALTER TABLE ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS raku_flg character varying(1) NOT NULL default '0';
comment on column ryohi_karibarai_meisai.raku_flg is '楽フラグ';

commit;
