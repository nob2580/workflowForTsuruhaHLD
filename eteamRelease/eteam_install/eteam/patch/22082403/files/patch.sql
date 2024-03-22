SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--画面項目制御の変更（交通費精算の単価を表示・必須固定に。帳票は非表示固定の現状維持）
UPDATE gamen_koumoku_seigyo SET hyouji_flg = '1', hissu_flg = '1', hyouji_seigyo_flg = '0', pdf_hyouji_flg ='0', pdf_hyouji_seigyo_flg = '0' WHERE denpyou_kbn = 'A010' AND koumoku_id = 'tanka';

commit;
