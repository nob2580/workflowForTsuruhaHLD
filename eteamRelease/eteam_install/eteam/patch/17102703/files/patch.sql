SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


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


-- 画面権限制御の更新
DELETE from gamen_kengen_seigyo;
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- テーブル和名の更新
COMMENT ON TABLE KANI_TODOKE IS '届出ジェネレーター';
COMMENT ON TABLE KANI_TODOKE_CHECKBOX IS '届出ジェネレーター項目チェックボックス';
COMMENT ON TABLE KANI_TODOKE_KOUMOKU IS '届出ジェネレーター項目定義';
COMMENT ON TABLE KANI_TODOKE_LIST_KO IS '届出ジェネレーター項目リスト子';
COMMENT ON TABLE KANI_TODOKE_LIST_OYA IS '届出ジェネレーター項目リスト親';
COMMENT ON TABLE KANI_TODOKE_MASTER IS '届出ジェネレーター項目マスター';
COMMENT ON TABLE KANI_TODOKE_MEISAI IS '届出ジェネレーター明細';
COMMENT ON TABLE KANI_TODOKE_META IS '届出ジェネレーターメタ';
COMMENT ON TABLE KANI_TODOKE_SELECT_ITEM IS '届出ジェネレーター選択項目';
COMMENT ON TABLE KANI_TODOKE_SUMMARY IS '届出ジェネレーターサマリ';
COMMENT ON TABLE KANI_TODOKE_TEXT IS '届出ジェネレーター項目テキスト';
COMMENT ON TABLE KANI_TODOKE_TEXTAREA IS '届出ジェネレーター項目テキストエリア';
COMMENT ON TABLE KANI_TODOKE_VERSION IS '届出ジェネレーターバージョン';

-- マスター管理一覧のマスター名更新
UPDATE master_kanri_ichiran SET master_name = '届出ジェネレーター選択項目' WHERE master_id = 'kani_todoke_select_item';


commit;