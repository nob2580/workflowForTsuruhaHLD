SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �`�[�e�[�u��.�V���A���ԍ��f�[�^�����ύX
alter table denpyou alter column serial_no type bigint;

commit;
