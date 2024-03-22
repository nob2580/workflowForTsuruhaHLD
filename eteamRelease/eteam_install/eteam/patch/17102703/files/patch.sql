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


-- ��ʌ�������̍X�V
DELETE from gamen_kengen_seigyo;
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- �e�[�u���a���̍X�V
COMMENT ON TABLE KANI_TODOKE IS '�͏o�W�F�l���[�^�[';
COMMENT ON TABLE KANI_TODOKE_CHECKBOX IS '�͏o�W�F�l���[�^�[���ڃ`�F�b�N�{�b�N�X';
COMMENT ON TABLE KANI_TODOKE_KOUMOKU IS '�͏o�W�F�l���[�^�[���ڒ�`';
COMMENT ON TABLE KANI_TODOKE_LIST_KO IS '�͏o�W�F�l���[�^�[���ڃ��X�g�q';
COMMENT ON TABLE KANI_TODOKE_LIST_OYA IS '�͏o�W�F�l���[�^�[���ڃ��X�g�e';
COMMENT ON TABLE KANI_TODOKE_MASTER IS '�͏o�W�F�l���[�^�[���ڃ}�X�^�[';
COMMENT ON TABLE KANI_TODOKE_MEISAI IS '�͏o�W�F�l���[�^�[����';
COMMENT ON TABLE KANI_TODOKE_META IS '�͏o�W�F�l���[�^�[���^';
COMMENT ON TABLE KANI_TODOKE_SELECT_ITEM IS '�͏o�W�F�l���[�^�[�I������';
COMMENT ON TABLE KANI_TODOKE_SUMMARY IS '�͏o�W�F�l���[�^�[�T�}��';
COMMENT ON TABLE KANI_TODOKE_TEXT IS '�͏o�W�F�l���[�^�[���ڃe�L�X�g';
COMMENT ON TABLE KANI_TODOKE_TEXTAREA IS '�͏o�W�F�l���[�^�[���ڃe�L�X�g�G���A';
COMMENT ON TABLE KANI_TODOKE_VERSION IS '�͏o�W�F�l���[�^�[�o�[�W����';

-- �}�X�^�[�Ǘ��ꗗ�̃}�X�^�[���X�V
UPDATE master_kanri_ichiran SET master_name = '�͏o�W�F�l���[�^�[�I������' WHERE master_id = 'kani_todoke_select_item';


commit;