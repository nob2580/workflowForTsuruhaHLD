SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--��ʍ��ڐ���̕ύX�i��ʔ�Z�̒P����\���E�K�{�Œ�ɁB���[�͔�\���Œ�̌���ێ��j
UPDATE gamen_koumoku_seigyo SET hyouji_flg = '1', hissu_flg = '1', hyouji_seigyo_flg = '0', pdf_hyouji_flg ='0', pdf_hyouji_seigyo_flg = '0' WHERE denpyou_kbn = 'A010' AND koumoku_id = 'tanka';

commit;
