SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �Y�t�t�@�C������UTF-8�̃m�[�u���[�N�X�y�[�X(&nbsp)������ꍇ�ʏ피�p�X�y�[�X�ɕϊ�
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xc2a0', 'utf8'), CONVERT_FROM('\x20', 'utf8'));

commit;