SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;


-- 法人カードインポート
CREATE TABLE HOUJIN_CARD_IMPORT
(
	USER_ID VARCHAR(30) NOT NULL,
	HOUJIN_CARD_SHUBETSU VARCHAR(1) NOT NULL,
	PRIMARY KEY (USER_ID)
) WITHOUT OIDS;
COMMENT ON TABLE HOUJIN_CARD_IMPORT IS '法人カードインポート';
COMMENT ON COLUMN HOUJIN_CARD_IMPORT.USER_ID IS 'ユーザーID';
COMMENT ON COLUMN HOUJIN_CARD_IMPORT.HOUJIN_CARD_SHUBETSU IS '法人カード種別';

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