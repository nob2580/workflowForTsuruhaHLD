SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- テーブル和名の更新
COMMENT ON TABLE KANI_TODOKE IS '届書ジェネレータ';
COMMENT ON TABLE KANI_TODOKE_CHECKBOX IS '届書ジェネレータ項目チェックボックス';
COMMENT ON TABLE KANI_TODOKE_KOUMOKU IS '届書ジェネレータ項目定義';
COMMENT ON TABLE KANI_TODOKE_LIST_KO IS '届書ジェネレータ項目リスト子';
COMMENT ON TABLE KANI_TODOKE_LIST_OYA IS '届書ジェネレータ項目リスト親';
COMMENT ON TABLE KANI_TODOKE_MASTER IS '届書ジェネレータ項目マスター';
COMMENT ON TABLE KANI_TODOKE_MEISAI IS '届書ジェネレータ明細';
COMMENT ON TABLE KANI_TODOKE_META IS '届書ジェネレータメタ';
COMMENT ON TABLE KANI_TODOKE_SELECT_ITEM IS '届書ジェネレータ選択項目';
COMMENT ON TABLE KANI_TODOKE_SUMMARY IS '届書ジェネレータサマリ';
COMMENT ON TABLE KANI_TODOKE_TEXT IS '届書ジェネレータ項目テキスト';
COMMENT ON TABLE KANI_TODOKE_TEXTAREA IS '届書ジェネレータ項目テキストエリア';
COMMENT ON TABLE KANI_TODOKE_VERSION IS '届書ジェネレータバージョン';

-- マスター管理一覧のマスター名更新
UPDATE master_kanri_ichiran SET master_name = '届書ジェネレータ選択項目' WHERE master_id = 'kani_todoke_select_item';


commit;