SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- ����ŏڍׂ̉�ʌ��������ǉ��i1���݂̂Ȃ̂Œ���INSERT�j
INSERT INTO gamen_kengen_seigyo(gamen_id, gamen_name, bumon_shozoku_riyoukanou_flg, system_kanri_riyoukanou_flg, workflow_riyoukanou_flg, kaishasettei_riyoukanou_flg, keirishori_riyoukanou_flg, kinou_seigyo_cd)
VALUES ('ManualSentaku','�}�j���A���I��','1','1','1','1','1','') ON CONFLICT DO NOTHING;

commit;
