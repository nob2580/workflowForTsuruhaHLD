SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- �ݒ���f�[�^�ύX ��hyouji_jun900�ȍ~�̓J�X�^�}�C�Y�̈�
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
UPDATE setting_info SET setting_val='0' WHERE setting_name='shiharaiirai_keijou_seigen';


-- ��ʍ��ڐ���̍폜(�x���˗����֘A)
DELETE FROM gamen_koumoku_seigyo WHERE denpyou_kbn = 'A013';


-- �����R�[�h�̍폜(�x���˗����֘A) ���I�v�V�����p���R�[�h�̓p�b�`�O�ɂȂ���΍폜
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;


-- �`�[��ʂ̍폜(�x���˗����֘A)
DELETE FROM denpyou_shubetsu_ichiran WHERE denpyou_kbn = 'A013';


-- �}�X�^�[�Ǘ��̍폜(�x���˗����֘A)
DELETE FROM master_kanri_ichiran WHERE master_id='moto_kouza_shiharaiirai';
DELETE FROM master_kanri_hansuu WHERE master_id='moto_kouza_shiharaiirai';


commit;