SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �ݒ���ɒǉ�
\copy setting_info FROM '.\files\csv\setting_info_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;
