SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- �e�[�u���a���̍X�V
COMMENT ON TABLE KANI_TODOKE IS '�͏o�W�F�l���[�^';
COMMENT ON TABLE KANI_TODOKE_CHECKBOX IS '�͏o�W�F�l���[�^���ڃ`�F�b�N�{�b�N�X';
COMMENT ON TABLE KANI_TODOKE_KOUMOKU IS '�͏o�W�F�l���[�^���ڒ�`';
COMMENT ON TABLE KANI_TODOKE_LIST_KO IS '�͏o�W�F�l���[�^���ڃ��X�g�q';
COMMENT ON TABLE KANI_TODOKE_LIST_OYA IS '�͏o�W�F�l���[�^���ڃ��X�g�e';
COMMENT ON TABLE KANI_TODOKE_MASTER IS '�͏o�W�F�l���[�^���ڃ}�X�^�[';
COMMENT ON TABLE KANI_TODOKE_MEISAI IS '�͏o�W�F�l���[�^����';
COMMENT ON TABLE KANI_TODOKE_META IS '�͏o�W�F�l���[�^���^';
COMMENT ON TABLE KANI_TODOKE_SELECT_ITEM IS '�͏o�W�F�l���[�^�I������';
COMMENT ON TABLE KANI_TODOKE_SUMMARY IS '�͏o�W�F�l���[�^�T�}��';
COMMENT ON TABLE KANI_TODOKE_TEXT IS '�͏o�W�F�l���[�^���ڃe�L�X�g';
COMMENT ON TABLE KANI_TODOKE_TEXTAREA IS '�͏o�W�F�l���[�^���ڃe�L�X�g�G���A';
COMMENT ON TABLE KANI_TODOKE_VERSION IS '�͏o�W�F�l���[�^�o�[�W����';

-- �}�X�^�[�Ǘ��ꗗ�̃}�X�^�[���X�V
UPDATE master_kanri_ichiran SET master_name = '�͏o�W�F�l���[�^�I������' WHERE master_id = 'kani_todoke_select_item';


commit;