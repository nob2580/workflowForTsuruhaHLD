SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �}�X�^�[�捞�ꗗ�f�[�^�ύX
DELETE FROM master_torikomi_ichiran WHERE master_id = 'project_master';

\copy master_torikomi_ichiran FROM '.\files\csv\master_torikomi_ichiran_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;
