SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- �e�[�u���a���̍X�V
COMMENT ON TABLE KANI_TODOKE IS '�͏��W�F�l���[�^';
COMMENT ON TABLE KANI_TODOKE_CHECKBOX IS '�͏��W�F�l���[�^���ڃ`�F�b�N�{�b�N�X';
COMMENT ON TABLE KANI_TODOKE_KOUMOKU IS '�͏��W�F�l���[�^���ڒ�`';
COMMENT ON TABLE KANI_TODOKE_LIST_KO IS '�͏��W�F�l���[�^���ڃ��X�g�q';
COMMENT ON TABLE KANI_TODOKE_LIST_OYA IS '�͏��W�F�l���[�^���ڃ��X�g�e';
COMMENT ON TABLE KANI_TODOKE_MASTER IS '�͏��W�F�l���[�^���ڃ}�X�^�[';
COMMENT ON TABLE KANI_TODOKE_MEISAI IS '�͏��W�F�l���[�^����';
COMMENT ON TABLE KANI_TODOKE_META IS '�͏��W�F�l���[�^���^';
COMMENT ON TABLE KANI_TODOKE_SELECT_ITEM IS '�͏��W�F�l���[�^�I������';
COMMENT ON TABLE KANI_TODOKE_SUMMARY IS '�͏��W�F�l���[�^�T�}��';
COMMENT ON TABLE KANI_TODOKE_TEXT IS '�͏��W�F�l���[�^���ڃe�L�X�g';
COMMENT ON TABLE KANI_TODOKE_TEXTAREA IS '�͏��W�F�l���[�^���ڃe�L�X�g�G���A';
COMMENT ON TABLE KANI_TODOKE_VERSION IS '�͏��W�F�l���[�^�o�[�W����';

-- �}�X�^�[�Ǘ��ꗗ�̃}�X�^�[���X�V
UPDATE master_kanri_ichiran SET master_name = '�͏��W�F�l���[�^�I������' WHERE master_id = 'kani_todoke_select_item';


commit;