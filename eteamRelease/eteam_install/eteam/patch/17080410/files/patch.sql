SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �Y�t�t�@�C�����Ɂw,(���p�J���})�x������ꍇ�w�A(�Ǔ_)�x�ɕϊ�
UPDATE tenpu_file SET file_name = REPLACE(file_name, ',' , '�A' );

commit;