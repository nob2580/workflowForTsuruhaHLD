SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �A���[_0939_�݌v���s���ɍs�폜�ō폜�ς݂̖��׍s�f�[�^�����o�����
-- �}�X�^�[�捞����
DELETE FROM master_torikomi_ichiran_sias;
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;
