SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- ���Z�@��
-- �e�[�u���o�b�N�A�b�v
ALTER TABLE KINYUUKIKAN RENAME TO KINYUUKIKAN_OLD;

-- �e�[�u���쐬
CREATE TABLE KINYUUKIKAN
(
	KINYUUKIKAN_CD VARCHAR(4) NOT NULL,
	KINYUUKIKAN_SHITEN_CD VARCHAR(3) NOT NULL,
	KINYUUKIKAN_NAME_HANKANA VARCHAR(15) NOT NULL,
	KINYUUKIKAN_NAME_KANA VARCHAR(30) NOT NULL,
	SHITEN_NAME_HANKANA VARCHAR(15) NOT NULL,
	SHITEN_NAME_KANA VARCHAR(30) NOT NULL,
	PRIMARY KEY (KINYUUKIKAN_CD, KINYUUKIKAN_SHITEN_CD)
) WITHOUT OIDS;

COMMENT ON TABLE KINYUUKIKAN IS '���Z�@��';
COMMENT ON COLUMN KINYUUKIKAN.KINYUUKIKAN_CD IS '���Z�@�փR�[�h';
COMMENT ON COLUMN KINYUUKIKAN.KINYUUKIKAN_SHITEN_CD IS '���Z�@�֎x�X�R�[�h';
COMMENT ON COLUMN KINYUUKIKAN.KINYUUKIKAN_NAME_HANKANA IS '���Z�@�֖��i���p�J�i�j';
COMMENT ON COLUMN KINYUUKIKAN.KINYUUKIKAN_NAME_KANA IS '���Z�@�֖�';
COMMENT ON COLUMN KINYUUKIKAN.SHITEN_NAME_HANKANA IS '�x�X���i���p�J�i�j';
COMMENT ON COLUMN KINYUUKIKAN.SHITEN_NAME_KANA IS '�x�X��';

-- �f�[�^�ڍs
INSERT INTO KINYUUKIKAN (
	KINYUUKIKAN_CD,
	KINYUUKIKAN_SHITEN_CD,
	KINYUUKIKAN_NAME_HANKANA,
	KINYUUKIKAN_NAME_KANA,
	SHITEN_NAME_HANKANA,
	SHITEN_NAME_KANA
)
SELECT 
	KINYUUKIKAN_CD,
	KINYUUKIKAN_SHITEN_CD,
	KINYUUKIKAN_NAME_HANKANA,
	KINYUUKIKAN_NAME_KANA,
	SHITEN_NAME_HANKANA,
	SHITEN_NAME_KANA
FROM KINYUUKIKAN_OLD;
DROP TABLE KINYUUKIKAN_OLD;

-- �}�X�^�[�Ǘ��Ő��̃o�C�i���f�[�^����ύX
-- ���Z�@�֖��E�x�X���́i�J�i�j���O��

CREATE TABLE tmp_table
(
	version integer,
	str text,
	str2 text
);
INSERT INTO tmp_table (
	version,
	str,
	str2
)
SELECT version, substr(convert_from(binary_data, 'sjis'),0,59),substr(convert_from(binary_data, 'sjis'),59) FROM master_kanri_hansuu WHERE master_id = 'kinyuukikan';

UPDATE tmp_table SET str ='���Z�@�փR�[�h,���Z�@�֎x�X�R�[�h,���Z�@�֖��i���p�J�i�j,���Z�@�֖�,�x�X���i���p�J�i�j,�x�X��'; 

UPDATE tmp_table SET str=str||str2;

UPDATE master_kanri_hansuu SET binary_data = (SELECT convert_to(str, 'sjis') FROM tmp_table WHERE version = master_kanri_hansuu.version) WHERE master_id = 'kinyuukikan' AND binary_data <> '';

DROP TABLE tmp_table;

-- �}�X�^�[�Ǘ��Ő��i��ʎ�i�}�X�^�[�j�̃o�C�i���f�[�^�ƃT�C�Y��ύX�FPK�Ƀ\�[�g����ǉ��A�t���O�͓��͕K�{(2)
UPDATE master_kanri_hansuu m
SET
  binary_data = (
    SELECT CONVERT_TO(REGEXP_REPLACE(CONVERT_FROM(binary_data, 'sjis'), ',1,', '1,1,2'), 'sjis')
    FROM master_kanri_hansuu tmp
    WHERE
      master_id = 'koutsuu_shudan_master' AND
      m.version = tmp.version),
  file_size = file_size + 2
WHERE master_id = 'koutsuu_shudan_master';

-- �}�X�^�[�Ǘ��Ő��i�������}�X�^�[�j�̃o�C�i���f�[�^�ƃT�C�Y��ύX�FPK�Ƀ\�[�g����ǉ��A�t���O�͓��͕K�{(2)
UPDATE master_kanri_hansuu m
SET
  binary_data = (
    SELECT CONVERT_TO(REGEXP_REPLACE(CONVERT_FROM(binary_data, 'sjis'), '1,1,1,,', '1,1,1,,2'), 'sjis')
    FROM master_kanri_hansuu tmp
    WHERE
      master_id = 'nittou_nado_master' AND
      m.version = tmp.version),
  file_size = file_size + 1
WHERE master_id = 'nittou_nado_master';

-- ��ʎ�i�}�X�^�[��PK�Ƀ\�[�g����ǉ�
ALTER TABLE koutsuu_shudan_master RENAME TO koutsuu_shudan_master_old;
CREATE TABLE KOUTSUU_SHUDAN_MASTER
(
	SORT_JUN CHAR(3) NOT NULL,
	KOUTSUU_SHUDAN VARCHAR(10) NOT NULL,
	SHOUHYOU_SHORUI_HISSU_FLG VARCHAR(1) NOT NULL,
	PRIMARY KEY (SORT_JUN, KOUTSUU_SHUDAN)
) WITHOUT OIDS;
COMMENT ON TABLE KOUTSUU_SHUDAN_MASTER IS '��ʎ�i�}�X�^�[';
COMMENT ON COLUMN KOUTSUU_SHUDAN_MASTER.SORT_JUN IS '���я�';
COMMENT ON COLUMN KOUTSUU_SHUDAN_MASTER.KOUTSUU_SHUDAN IS '��ʎ�i';
COMMENT ON COLUMN KOUTSUU_SHUDAN_MASTER.SHOUHYOU_SHORUI_HISSU_FLG IS '�؜ߏ��ޕK�{�t���O';
INSERT INTO koutsuu_shudan_master SELECT * FROM koutsuu_shudan_master_old;
DROP TABLE koutsuu_shudan_master_old;

-- �}�X�^�[�Ǘ��ꗗ �ύX�ۃt���O1����2
UPDATE master_kanri_ichiran SET henkou_kahi_flg = '2' WHERE henkou_kahi_flg = '1';

-- �����R�[�h�ݒ�f�[�^�ύX
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM naibu_cd_setting
	WHERE NOT EXISTS (SELECT naibu_cd_name FROM naibu_cd_setting_tmp WHERE naibu_cd_name = 'denpyou_kbn' AND naibu_cd = 'A003') 
	      AND naibu_cd_name = 'denpyou_kbn' AND naibu_cd = 'A003';
DELETE FROM naibu_cd_setting 
	WHERE NOT EXISTS (SELECT naibu_cd_name FROM naibu_cd_setting_tmp WHERE naibu_cd_name = 'shiwake_pattern_denpyou_kbn' AND naibu_cd = 'A003') 
	      AND naibu_cd_name = 'shiwake_pattern_denpyou_kbn' AND naibu_cd = 'A003';
DROP TABLE naibu_cd_setting_tmp;

-- ��ʌ�������f�[�^�ύX�i�����j
DELETE FROM gamen_kengen_seigyo WHERE gamen_id = 'BumonIchiran';
DELETE FROM gamen_kengen_seigyo WHERE gamen_id = 'BumonTsuika';
DELETE FROM gamen_kengen_seigyo WHERE gamen_id = 'BumonHenkou';
DELETE FROM gamen_kengen_seigyo WHERE gamen_id = 'MasterDataTsuika';
DELETE FROM gamen_kengen_seigyo WHERE gamen_id = 'MasterDataTsuikaKakunin';
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- �ݒ���f�[�^�ύX
CREATE TABLE setting_info_tmp AS SELECT * FROM setting_info;
DELETE FROM setting_info;
\copy setting_info FROM '.\files\csv\setting_info.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE setting_info new SET setting_val = (
 SELECT setting_val
 FROM setting_info_tmp tmp
 WHERE tmp.setting_name = new.setting_name
) WHERE new.setting_name IN (
 SELECT setting_name FROM setting_info_tmp
);
DROP TABLE setting_info_tmp;


-- ���F���[�g�̏��F�󋵎}�ԃJ�����ǉ�
ALTER TABLE shounin_route RENAME TO shounin_route_tmp;

-- �e�[�u���쐬
CREATE TABLE SHOUNIN_ROUTE
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	EDANO INT NOT NULL,
	USER_ID VARCHAR(30) NOT NULL,
	USER_FULL_NAME VARCHAR(50) NOT NULL,
	BUMON_CD VARCHAR(8) NOT NULL,
	BUMON_FULL_NAME VARCHAR NOT NULL,
	BUMON_ROLE_ID VARCHAR(5) NOT NULL,
	BUMON_ROLE_NAME VARCHAR NOT NULL,
	GYOUMU_ROLE_ID VARCHAR(5) NOT NULL,
	GYOUMU_ROLE_NAME VARCHAR NOT NULL,
	GENZAI_FLG VARCHAR(1) NOT NULL,
	SAISHU_SHOUNIN_FLG VARCHAR(1),
	JOUKYOU_EDANO INT,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID, EDANO)
) WITHOUT OIDS;

COMMENT ON TABLE SHOUNIN_ROUTE IS '���F���[�g';
COMMENT ON COLUMN SHOUNIN_ROUTE.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN SHOUNIN_ROUTE.EDANO IS '�}�ԍ�';
COMMENT ON COLUMN SHOUNIN_ROUTE.USER_ID IS '���[�U�[ID';
COMMENT ON COLUMN SHOUNIN_ROUTE.USER_FULL_NAME IS '���[�U�[�t����';
COMMENT ON COLUMN SHOUNIN_ROUTE.BUMON_CD IS '����R�[�h';
COMMENT ON COLUMN SHOUNIN_ROUTE.BUMON_FULL_NAME IS '����t����';
COMMENT ON COLUMN SHOUNIN_ROUTE.BUMON_ROLE_ID IS '���働�[��ID';
COMMENT ON COLUMN SHOUNIN_ROUTE.BUMON_ROLE_NAME IS '���働�[����';
COMMENT ON COLUMN SHOUNIN_ROUTE.GYOUMU_ROLE_ID IS '�Ɩ����[��ID';
COMMENT ON COLUMN SHOUNIN_ROUTE.GYOUMU_ROLE_NAME IS '�Ɩ����[����';
COMMENT ON COLUMN SHOUNIN_ROUTE.GENZAI_FLG IS '���݃t���O';
COMMENT ON COLUMN SHOUNIN_ROUTE.SAISHU_SHOUNIN_FLG IS '�ŏI���F�t���O';
COMMENT ON COLUMN SHOUNIN_ROUTE.JOUKYOU_EDANO IS '���F�󋵎}��';
COMMENT ON COLUMN SHOUNIN_ROUTE.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN SHOUNIN_ROUTE.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN SHOUNIN_ROUTE.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN SHOUNIN_ROUTE.KOUSHIN_TIME IS '�X�V����';

-- �f�[�^�ڍs
INSERT INTO SHOUNIN_ROUTE (
	DENPYOU_ID,
	EDANO,
	USER_ID,
	USER_FULL_NAME,
	BUMON_CD,
	BUMON_FULL_NAME,
	BUMON_ROLE_ID,
	BUMON_ROLE_NAME,
	GYOUMU_ROLE_ID,
	GYOUMU_ROLE_NAME,
	GENZAI_FLG,
	SAISHU_SHOUNIN_FLG,
	JOUKYOU_EDANO,
	TOUROKU_USER_ID,
	TOUROKU_TIME,
	KOUSHIN_USER_ID,
	KOUSHIN_TIME
)
SELECT 
	DENPYOU_ID,
	EDANO,
	USER_ID,
	USER_FULL_NAME,
	BUMON_CD,
	BUMON_FULL_NAME,
	BUMON_ROLE_ID,
	BUMON_ROLE_NAME,
	GYOUMU_ROLE_ID,
	GYOUMU_ROLE_NAME,
	GENZAI_FLG,
	SAISHU_SHOUNIN_FLG,
	NULL,
	TOUROKU_USER_ID,
	TOUROKU_TIME,
	KOUSHIN_USER_ID,
	KOUSHIN_TIME
FROM shounin_route_tmp;
DROP TABLE shounin_route_tmp;

-- �`�[�ԍ��̔ԃe�[�u���V�K�쐬
CREATE TABLE DENPYOU_SERIAL_NO_SAIBAN
(
	SEQUENCE_VAL INT NOT NULL,
	MAX_VALUE INT NOT NULL,
	MIN_VALUE INT NOT NULL
) WITHOUT OIDS;
COMMENT ON TABLE DENPYOU_SERIAL_NO_SAIBAN IS '�`�[�ԍ��̔�';
COMMENT ON COLUMN DENPYOU_SERIAL_NO_SAIBAN.SEQUENCE_VAL IS '�V�[�P���X�l';
COMMENT ON COLUMN DENPYOU_SERIAL_NO_SAIBAN.MAX_VALUE IS '�ő�l';
COMMENT ON COLUMN DENPYOU_SERIAL_NO_SAIBAN.MIN_VALUE IS '�ŏ��l';
-- �f�[�^����
insert into DENPYOU_SERIAL_NO_SAIBAN 
values ((select case when max(serial_no) is null then -1 else max(serial_no) end from denpyou where touroku_time = (select max(touroku_time) from denpyou)) 
	, to_number((select setting_val from setting_info where setting_name = 'denpyou_serial_no_end'), '99999999') - 1
	, to_number((select setting_val from setting_info where setting_name = 'denpyou_serial_no_start'), '99999999'));

-- �`�[�e�[�u���ύX
-- �e�[�u���o�b�N�A�b�v
ALTER TABLE DENPYOU RENAME TO DENPYOU_OLD;
CREATE TABLE DENPYOU
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	DENPYOU_KBN VARCHAR(4) NOT NULL,
	DENPYOU_JOUTAI VARCHAR(2) NOT NULL,
	SANSHOU_DENPYOU_ID VARCHAR(19) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	SERIAL_NO INT NOT NULL,
	CHUUSHUTSU_ZUMI_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE DENPYOU IS '�`�[';
COMMENT ON COLUMN DENPYOU.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN DENPYOU.DENPYOU_KBN IS '�`�[�敪';
COMMENT ON COLUMN DENPYOU.DENPYOU_JOUTAI IS '�`�[���';
COMMENT ON COLUMN DENPYOU.SANSHOU_DENPYOU_ID IS '�Q�Ɠ`�[ID';
COMMENT ON COLUMN DENPYOU.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN DENPYOU.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN DENPYOU.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN DENPYOU.KOUSHIN_TIME IS '�X�V����';
COMMENT ON COLUMN DENPYOU.SERIAL_NO IS '�V���A���ԍ�';
COMMENT ON COLUMN DENPYOU.CHUUSHUTSU_ZUMI_FLG IS '���o�σt���O';
COMMENT ON TABLE DENPYOU_ID_SAIBAN IS '�`�[ID�̔�';
COMMENT ON COLUMN DENPYOU_ID_SAIBAN.TOUROKU_DATE IS '�o�^��';
COMMENT ON COLUMN DENPYOU_ID_SAIBAN.DENPYOU_KBN IS '�`�[�敪';
COMMENT ON COLUMN DENPYOU_ID_SAIBAN.SEQUENCE_VAL IS '�V�[�P���X�l';

-- �`�[�e�[�u���f�[�^�ڍs
INSERT INTO DENPYOU SELECT * FROM DENPYOU_OLD;
DROP TABLE DENPYOU_OLD;

commit;
