SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- テーブル和名の更新
COMMENT ON TABLE KANI_TODOKE IS '届出ジェネレータ';
COMMENT ON TABLE KANI_TODOKE_CHECKBOX IS '届出ジェネレータ項目チェックボックス';
COMMENT ON TABLE KANI_TODOKE_KOUMOKU IS '届出ジェネレータ項目定義';
COMMENT ON TABLE KANI_TODOKE_LIST_KO IS '届出ジェネレータ項目リスト子';
COMMENT ON TABLE KANI_TODOKE_LIST_OYA IS '届出ジェネレータ項目リスト親';
COMMENT ON TABLE KANI_TODOKE_MASTER IS '届出ジェネレータ項目マスター';
COMMENT ON TABLE KANI_TODOKE_MEISAI IS '届出ジェネレータ明細';
COMMENT ON TABLE KANI_TODOKE_META IS '届出ジェネレータメタ';
COMMENT ON TABLE KANI_TODOKE_SELECT_ITEM IS '届出ジェネレータ選択項目';
COMMENT ON TABLE KANI_TODOKE_SUMMARY IS '届出ジェネレータサマリ';
COMMENT ON TABLE KANI_TODOKE_TEXT IS '届出ジェネレータ項目テキスト';
COMMENT ON TABLE KANI_TODOKE_TEXTAREA IS '届出ジェネレータ項目テキストエリア';
COMMENT ON TABLE KANI_TODOKE_VERSION IS '届出ジェネレータバージョン';

-- マスター管理一覧のマスター名更新
UPDATE master_kanri_ichiran SET master_name = '届出ジェネレータ選択項目' WHERE master_id = 'kani_todoke_select_item';


commit;