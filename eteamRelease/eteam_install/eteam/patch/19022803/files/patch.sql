SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 0765_XML���b�Z�[�W�P�ʂ̎��ʔԍ��̐ݒ�l�ύX�i�A���_�[�X�R�A�𔼊p�X�y�[�X�Ɂj ��hyouji_jun900�ȍ~�̓J�X�^�}�C�Y�̈�
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
UPDATE setting_info SET setting_val = '{0} {1} {2}' WHERE setting_name = 'xml_message_shikibetsu_no'; --0765�Őݒ�l�݂̂�ύX���Ă��邽��

commit;
