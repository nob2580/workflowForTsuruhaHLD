SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 連絡票No.990 「精算金額との差額」必須チェックの無効化
UPDATE gamen_koumoku_seigyo SET hissu_flg = '0' WHERE koumoku_id = 'karibarai_kingaku_sagaku' AND denpyou_kbn = 'A001';
UPDATE gamen_koumoku_seigyo SET hissu_flg = '0' WHERE koumoku_id = 'karibarai_kingaku_sagaku' AND denpyou_kbn = 'A004';
UPDATE gamen_koumoku_seigyo SET hissu_flg = '0' WHERE koumoku_id = 'karibarai_kingaku_sagaku' AND denpyou_kbn = 'A011';


commit;
