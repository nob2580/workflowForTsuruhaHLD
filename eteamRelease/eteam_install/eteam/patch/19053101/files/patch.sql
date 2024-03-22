SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �s��F�y���ŗ��Ή�
-- �V�[�P���X���ݒl�̍Đݒ����B���̃V�[�P���X�i�O�̈דY�����Ȃ��Ƃ����2�j�������Ă���č쐬����B
DROP SEQUENCE IF EXISTS shiwake_sias_serial_no_seq CASCADE;
DROP SEQUENCE IF EXISTS shiwake_sias_serial_no_seq1 CASCADE;
CREATE SEQUENCE shiwake_sias_serial_no_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE shiwake_sias_serial_no_seq OWNER TO eteam;
ALTER SEQUENCE shiwake_sias_serial_no_seq OWNED BY shiwake_sias.serial_no;
ALTER TABLE ONLY shiwake_sias ALTER COLUMN serial_no SET DEFAULT nextval('shiwake_sias_serial_no_seq'::regclass);
SELECT setval('shiwake_sias_serial_no_seq',(SELECT MAX(serial_no) FROM shiwake_sias));


DROP SEQUENCE IF EXISTS shiwake_de3_serial_no_seq CASCADE;
DROP SEQUENCE IF EXISTS shiwake_de3_serial_no_seq1 CASCADE;
CREATE SEQUENCE shiwake_de3_serial_no_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE shiwake_de3_serial_no_seq OWNER TO eteam;
ALTER SEQUENCE shiwake_de3_serial_no_seq OWNED BY shiwake_de3.serial_no;
ALTER TABLE ONLY shiwake_de3 ALTER COLUMN serial_no SET DEFAULT nextval('shiwake_de3_serial_no_seq'::regclass);
SELECT setval('shiwake_de3_serial_no_seq',(SELECT MAX(serial_no) FROM shiwake_de3));

commit;