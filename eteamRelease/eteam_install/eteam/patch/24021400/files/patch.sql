SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 消費税詳細の画面権限制御を追加（1件のみなので直接INSERT）
INSERT INTO gamen_kengen_seigyo(gamen_id, gamen_name, bumon_shozoku_riyoukanou_flg, system_kanri_riyoukanou_flg, workflow_riyoukanou_flg, kaishasettei_riyoukanou_flg, keirishori_riyoukanou_flg, kinou_seigyo_cd)
VALUES ('ManualSentaku','マニュアル選択','1','1','1','1','1','') ON CONFLICT DO NOTHING;

commit;
