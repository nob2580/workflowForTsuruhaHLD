SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �C�O�p��ʎ�i�}�X�^�[
CREATE TABLE KAIGAI_KOUTSUU_SHUDAN_MASTER
(
	SORT_JUN CHAR(3) NOT NULL,
	KOUTSUU_SHUDAN VARCHAR(10) NOT NULL,
	SHOUHYOU_SHORUI_HISSU_FLG VARCHAR(1) NOT NULL,
	ZEI_KUBUN VARCHAR(1),
	PRIMARY KEY (SORT_JUN, KOUTSUU_SHUDAN)
) WITHOUT OIDS;
COMMENT ON TABLE KAIGAI_KOUTSUU_SHUDAN_MASTER IS '�C�O�p��ʎ�i�}�X�^�[';
COMMENT ON COLUMN KAIGAI_KOUTSUU_SHUDAN_MASTER.SORT_JUN IS '���я�';
COMMENT ON COLUMN KAIGAI_KOUTSUU_SHUDAN_MASTER.KOUTSUU_SHUDAN IS '��ʎ�i';
COMMENT ON COLUMN KAIGAI_KOUTSUU_SHUDAN_MASTER.SHOUHYOU_SHORUI_HISSU_FLG IS '�؜ߏ��ޕK�{�t���O';
COMMENT ON COLUMN KAIGAI_KOUTSUU_SHUDAN_MASTER.ZEI_KUBUN IS '�ŋ敪';

-- �C�O�p�������}�X�^�[
ALTER TABLE KAIGAI_NITTOU_NADO_MASTER RENAME TO KAIGAI_NITTOU_NADO_MASTER_OLD;
CREATE TABLE KAIGAI_NITTOU_NADO_MASTER
(
	SHUBETSU1 VARCHAR(20) NOT NULL,
	SHUBETSU2 VARCHAR(20) NOT NULL,
	YAKUSHOKU_CD VARCHAR(10) NOT NULL,
	TANKA DECIMAL(15),
	SHOUHYOU_SHORUI_HISSU_FLG VARCHAR(1) NOT NULL,
	NITTOU_SHUKUHAKUHI_FLG VARCHAR(1) NOT NULL,
	ZEI_KUBUN VARCHAR(1),
	PRIMARY KEY (SHUBETSU1, SHUBETSU2, YAKUSHOKU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE KAIGAI_NITTOU_NADO_MASTER IS '�C�O�p�������}�X�^�[';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.SHUBETSU1 IS '��ʂP';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.SHUBETSU2 IS '��ʂQ';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.YAKUSHOKU_CD IS '��E�R�[�h';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.TANKA IS '�P��';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.SHOUHYOU_SHORUI_HISSU_FLG IS '�؜ߏ��ޕK�{�t���O';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.NITTOU_SHUKUHAKUHI_FLG IS '�����E�h����t���O';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.ZEI_KUBUN IS '�ŋ敪';
INSERT INTO KAIGAI_NITTOU_NADO_MASTER(
	SHUBETSU1,
	SHUBETSU2,
	YAKUSHOKU_CD,
	TANKA,
	SHOUHYOU_SHORUI_HISSU_FLG,
	NITTOU_SHUKUHAKUHI_FLG,
	ZEI_KUBUN
)
SELECT
	SHUBETSU1,
	SHUBETSU2,
	YAKUSHOKU_CD,
	TANKA,
	SHOUHYOU_SHORUI_HISSU_FLG,
	NITTOU_SHUKUHAKUHI_FLG,
	''
FROM KAIGAI_NITTOU_NADO_MASTER_OLD;
DROP TABLE KAIGAI_NITTOU_NADO_MASTER_OLD;

-- �����p��ʎ�i�}�X�^�[
ALTER TABLE KOUTSUU_SHUDAN_MASTER RENAME TO KOUTSUU_SHUDAN_MASTER_OLD;
CREATE TABLE KOUTSUU_SHUDAN_MASTER
(
	SORT_JUN CHAR(3) NOT NULL,
	KOUTSUU_SHUDAN VARCHAR(10) NOT NULL,
	SHOUHYOU_SHORUI_HISSU_FLG VARCHAR(1) NOT NULL,
	ZEI_KUBUN VARCHAR(1),
	PRIMARY KEY (SORT_JUN, KOUTSUU_SHUDAN)
) WITHOUT OIDS;
COMMENT ON TABLE KOUTSUU_SHUDAN_MASTER IS '�����p��ʎ�i�}�X�^�[';
COMMENT ON COLUMN KOUTSUU_SHUDAN_MASTER.SORT_JUN IS '���я�';
COMMENT ON COLUMN KOUTSUU_SHUDAN_MASTER.KOUTSUU_SHUDAN IS '��ʎ�i';
COMMENT ON COLUMN KOUTSUU_SHUDAN_MASTER.SHOUHYOU_SHORUI_HISSU_FLG IS '�؜ߏ��ޕK�{�t���O';
COMMENT ON COLUMN KOUTSUU_SHUDAN_MASTER.ZEI_KUBUN IS '�ŋ敪';
INSERT INTO KOUTSUU_SHUDAN_MASTER(
	SORT_JUN,
	KOUTSUU_SHUDAN,
	SHOUHYOU_SHORUI_HISSU_FLG,
	ZEI_KUBUN
)
SELECT
	SORT_JUN,
	KOUTSUU_SHUDAN,
	SHOUHYOU_SHORUI_HISSU_FLG,
	''
FROM KOUTSUU_SHUDAN_MASTER_OLD;
DROP TABLE KOUTSUU_SHUDAN_MASTER_OLD;


-- �����p�������}�X�^�[
ALTER TABLE NITTOU_NADO_MASTER RENAME TO NITTOU_NADO_MASTER_OLD;
CREATE TABLE NITTOU_NADO_MASTER
(
	SHUBETSU1 VARCHAR(20) NOT NULL,
	SHUBETSU2 VARCHAR(20) NOT NULL,
	YAKUSHOKU_CD VARCHAR(10) NOT NULL,
	TANKA DECIMAL(15),
	SHOUHYOU_SHORUI_HISSU_FLG VARCHAR(1) NOT NULL,
	NITTOU_SHUKUHAKUHI_FLG VARCHAR(1) NOT NULL,
	ZEI_KUBUN VARCHAR(1),
	PRIMARY KEY (SHUBETSU1, SHUBETSU2, YAKUSHOKU_CD)
) WITHOUT OIDS;

COMMENT ON TABLE NITTOU_NADO_MASTER IS '�����p�������}�X�^�[';
COMMENT ON COLUMN NITTOU_NADO_MASTER.SHUBETSU1 IS '��ʂP';
COMMENT ON COLUMN NITTOU_NADO_MASTER.SHUBETSU2 IS '��ʂQ';
COMMENT ON COLUMN NITTOU_NADO_MASTER.YAKUSHOKU_CD IS '��E�R�[�h';
COMMENT ON COLUMN NITTOU_NADO_MASTER.TANKA IS '�P��';
COMMENT ON COLUMN NITTOU_NADO_MASTER.SHOUHYOU_SHORUI_HISSU_FLG IS '�؜ߏ��ޕK�{�t���O';
COMMENT ON COLUMN NITTOU_NADO_MASTER.NITTOU_SHUKUHAKUHI_FLG IS '�����E�h����t���O';
COMMENT ON COLUMN NITTOU_NADO_MASTER.ZEI_KUBUN IS '�ŋ敪';
INSERT INTO NITTOU_NADO_MASTER(
	SHUBETSU1,
	SHUBETSU2,
	YAKUSHOKU_CD,
	TANKA,
	SHOUHYOU_SHORUI_HISSU_FLG,
	NITTOU_SHUKUHAKUHI_FLG,
	ZEI_KUBUN
)
SELECT
	SHUBETSU1,
	SHUBETSU2,
	YAKUSHOKU_CD,
	TANKA,
	SHOUHYOU_SHORUI_HISSU_FLG,
	NITTOU_SHUKUHAKUHI_FLG,
	''
FROM NITTOU_NADO_MASTER_OLD;
DROP TABLE NITTOU_NADO_MASTER_OLD;

-- �}�X�^�[�Ǘ��ꗗ�i�C�O�p��ʎ�i�}�X�^�[�̒ǉ��A�����p��ʎ�i�}�X�^�[�̕ύX�j
\copy MASTER_KANRI_ICHIRAN         FROM '.\files\csv\master_kanri_ichiran_patch.csv'         WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE MASTER_KANRI_ICHIRAN SET MASTER_NAME = '�����p��ʎ�i�}�X�^�[' WHERE MASTER_ID = 'koutsuu_shudan_master';

-- �}�X�^�[�Ǘ��Ő��i�C�O�p�������}�X�^�[��17.08.04.00�łŒǉ��ς݂Ȃ̂ō폜���ǉ��A�C�O�p��ʎ�i�}�X�^�[�̒ǉ��j
DELETE FROM MASTER_KANRI_HANSUU WHERE MASTER_ID = 'kaigai_nittou_nado_master';
\copy MASTER_KANRI_HANSUU          FROM '.\files\csv\master_kanri_hansuu_patch.csv'          WITH CSV header ENCODING 'SHIFT-JIS';

-- �}�X�^�[�Ǘ��Ő��i������ʎ�i�}�X�^�[��ǉ��j
UPDATE MASTER_KANRI_HANSUU SET DELETE_FLG = '1' WHERE MASTER_ID = 'koutsuu_shudan_master';
INSERT INTO MASTER_KANRI_HANSUU 
(
  MASTER_ID
  , VERSION
  , DELETE_FLG
  , FILE_NAME
  , FILE_SIZE
  , CONTENT_TYPE
  , BINARY_DATA
  , TOUROKU_USER_ID 
  , TOUROKU_TIME 
  , KOUSHIN_USER_ID 
  , KOUSHIN_TIME 
) VALUES (
   'koutsuu_shudan_master'
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='koutsuu_shudan_master')
 , '0'
 , '�����p��ʎ�i�}�X�^�[_patch.csv'
 , length(convert_to(E'���я�,��ʎ�i,�؜ߏ��ޕK�{�t���O,�ŋ敪\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun\r\nchar(3),varchar(10),varchar(1),varchar(1)\r\n1,1,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun FROM koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'���я�,��ʎ�i,�؜ߏ��ޕK�{�t���O,�ŋ敪\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun\r\nchar(3),varchar(10),varchar(1),varchar(1)\r\n1,1,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun FROM koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
);

-- �}�X�^�[�Ǘ��Ő��i�����������}�X�^�[��17.08.04.00�łŒǉ��ς݂Ȃ̂ł����ł͍X�V�j
UPDATE MASTER_KANRI_HANSUU
SET
   file_size = length(convert_to(E'��ʂP,��ʂQ,��E�R�[�h,�P��,�؜ߏ��ޕK�{�t���O,�����E�h����t���O,�ŋ敪\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(1),varchar(1),varchar(1)\r\n1,1,1,,2,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun FROM nittou_nado_master), E'\r\n') || E'\r\n','sjis') )
 , binary_data = convert_to(E'��ʂP,��ʂQ,��E�R�[�h,�P��,�؜ߏ��ޕK�{�t���O,�����E�h����t���O,�ŋ敪\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(1),varchar(1),varchar(1)\r\n1,1,1,,2,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun FROM nittou_nado_master), E'\r\n') || E'\r\n','sjis')
WHERE
  MASTER_ID = 'nittou_nado_master' 
AND
  DELETE_FLG = '0'
;

DROP TABLE IF EXISTS naibu_cd_setting_tmp; 


commit;