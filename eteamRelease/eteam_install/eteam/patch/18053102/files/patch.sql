SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 画面項目制御のレコード差替
UPDATE gamen_koumoku_seigyo SET pdf_hyouji_seigyo_flg = '1' WHERE denpyou_kbn = 'A001' AND koumoku_id = 'kousaihi_shousai';
UPDATE gamen_koumoku_seigyo SET pdf_hyouji_seigyo_flg = '1' WHERE denpyou_kbn = 'A003' AND koumoku_id = 'kousaihi_shousai';

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

-- FBテーブルにuser_idカラムを追加
ALTER TABLE fb RENAME TO fb_old;

CREATE TABLE FB
(
	SERIAL_NO BIGSERIAL NOT NULL,
	DENPYOU_ID VARCHAR(19) NOT NULL,
	USER_ID VARCHAR(30) NOT NULL,
	FB_STATUS VARCHAR(1) NOT NULL,
	TOUROKU_TIME TIMESTAMP,
	KOUSHIN_TIME TIMESTAMP,
	SHUBETSU_CD VARCHAR(2) NOT NULL,
	CD_KBN VARCHAR(1) NOT NULL,
	KAISHA_CD VARCHAR(10) NOT NULL,
	KAISHA_NAME_HANKANA VARCHAR(40) NOT NULL,
	FURIKOMI_DATE DATE NOT NULL,
	MOTO_KINYUUKIKAN_CD VARCHAR(4) NOT NULL,
	MOTO_KINYUUKIKAN_NAME_HANKANA VARCHAR(15) NOT NULL,
	MOTO_KINYUUKIKAN_SHITEN_CD VARCHAR(3) NOT NULL,
	MOTO_KINYUUKIKAN_SHITEN_NAME_HANKANA VARCHAR(15) NOT NULL,
	MOTO_YOKIN_SHUMOKU_CD VARCHAR(1) NOT NULL,
	MOTO_KOUZA_BANGOU VARCHAR(7) NOT NULL,
	SAKI_KINYUUKIKAN_CD VARCHAR(4) NOT NULL,
	SAKI_KINYUUKIKAN_NAME_HANKANA VARCHAR(15) NOT NULL,
	SAKI_KINYUUKIKAN_SHITEN_CD VARCHAR(3) NOT NULL,
	SAKI_KINYUUKIKAN_SHITEN_NAME_HANKANA VARCHAR(15) NOT NULL,
	SAKI_YOKIN_SHUMOKU_CD VARCHAR(1) NOT NULL,
	SAKI_KOUZA_BANGOU VARCHAR(7) NOT NULL,
	SAKI_KOUZA_MEIGI_KANA VARCHAR(30) NOT NULL,
	KINGAKU DECIMAL(15) NOT NULL,
	SHINKI_CD VARCHAR(1) NOT NULL,
	KOKYAKU_CD1 VARCHAR(10) NOT NULL,
	FURIKOMI_KBN VARCHAR(1) NOT NULL,
	PRIMARY KEY (SERIAL_NO)
) WITHOUT OIDS;

COMMENT ON TABLE FB IS 'FB抽出';
COMMENT ON COLUMN FB.SERIAL_NO IS 'シリアル番号';
COMMENT ON COLUMN FB.DENPYOU_ID IS '伝票ID';
COMMENT ON COLUMN FB.USER_ID IS 'ユーザーID';
COMMENT ON COLUMN FB.FB_STATUS IS 'FB抽出状態';
COMMENT ON COLUMN FB.TOUROKU_TIME IS '登録日時';
COMMENT ON COLUMN FB.KOUSHIN_TIME IS '更新日時';
COMMENT ON COLUMN FB.SHUBETSU_CD IS '種別コード';
COMMENT ON COLUMN FB.CD_KBN IS 'コード区分';
COMMENT ON COLUMN FB.KAISHA_CD IS '会社コード';
COMMENT ON COLUMN FB.KAISHA_NAME_HANKANA IS '会社名（半角カナ）';
COMMENT ON COLUMN FB.FURIKOMI_DATE IS '振込日';
COMMENT ON COLUMN FB.MOTO_KINYUUKIKAN_CD IS '振込元金融機関コード';
COMMENT ON COLUMN FB.MOTO_KINYUUKIKAN_NAME_HANKANA IS '振込元金融機関名（半角カナ）';
COMMENT ON COLUMN FB.MOTO_KINYUUKIKAN_SHITEN_CD IS '振込元金融機関支店コード';
COMMENT ON COLUMN FB.MOTO_KINYUUKIKAN_SHITEN_NAME_HANKANA IS '振込元金融機関支店名（半角カナ）';
COMMENT ON COLUMN FB.MOTO_YOKIN_SHUMOKU_CD IS '振込元預金種目コード';
COMMENT ON COLUMN FB.MOTO_KOUZA_BANGOU IS '振込元口座番号';
COMMENT ON COLUMN FB.SAKI_KINYUUKIKAN_CD IS '振込先金融機関銀行コード';
COMMENT ON COLUMN FB.SAKI_KINYUUKIKAN_NAME_HANKANA IS '振込先金融機関名（半角カナ）';
COMMENT ON COLUMN FB.SAKI_KINYUUKIKAN_SHITEN_CD IS '振込先金融機関支店コード';
COMMENT ON COLUMN FB.SAKI_KINYUUKIKAN_SHITEN_NAME_HANKANA IS '振込先金融機関支店名（半角カナ）';
COMMENT ON COLUMN FB.SAKI_YOKIN_SHUMOKU_CD IS '振込先預金種目コード';
COMMENT ON COLUMN FB.SAKI_KOUZA_BANGOU IS '振込先口座番号';
COMMENT ON COLUMN FB.SAKI_KOUZA_MEIGI_KANA IS '振込先口座名義（半角カナ）';
COMMENT ON COLUMN FB.KINGAKU IS '金額';
COMMENT ON COLUMN FB.SHINKI_CD IS '新規コード';
COMMENT ON COLUMN FB.KOKYAKU_CD1 IS '顧客コード１';
COMMENT ON COLUMN FB.FURIKOMI_KBN IS '振込区分';

INSERT 
INTO fb
SELECT
    serial_no
  , denpyou_id
  , COALESCE((SELECT user_id FROM user_info WHERE shain_no = kokyaku_cd1), '')
  , fb_status
  , touroku_time
  , koushin_time
  , shubetsu_cd
  , cd_kbn
  , kaisha_cd
  , kaisha_name_hankana
  , furikomi_date
  , moto_kinyuukikan_cd
  , moto_kinyuukikan_name_hankana
  , moto_kinyuukikan_shiten_cd
  , moto_kinyuukikan_shiten_name_hankana
  , moto_yokin_shumoku_cd
  , moto_kouza_bangou
  , saki_kinyuukikan_cd
  , saki_kinyuukikan_name_hankana
  , saki_kinyuukikan_shiten_cd
  , saki_kinyuukikan_shiten_name_hankana
  , saki_yokin_shumoku_cd
  , saki_kouza_bangou
  , saki_kouza_meigi_kana
  , kingaku
  , shinki_cd
  , kokyaku_cd1
  , furikomi_kbn 
FROM
  fb_old;

DROP TABLE fb_old;

-- シーケンスの設定
SELECT setval('fb_serial_no_seq1',(SELECT MAX(serial_no) FROM fb));


commit;