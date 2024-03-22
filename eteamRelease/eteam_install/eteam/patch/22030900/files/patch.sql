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


--内部コード設定
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
-- Web拠点入力の内部コードは洗い替え対象外
DELETE FROM naibu_cd_setting 
WHERE naibu_cd_name NOT IN ('bunri_kbn','shiire_kbn','kazei_kbn_kyoten_furikae','shiwake_pattern_denpyou_kbn_kyoten','shiwake_pattern_setting_kbn_kyoten','fusen_color');
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- 支払依頼申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- 請求書払申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

commit;
