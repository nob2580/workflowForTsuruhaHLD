SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- �ݒ���f�[�^�ύX ��hyouji_jun900�ȍ~�̓J�X�^�}�C�Y�̈�
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

--485_�`�[�`���E���̓p�^�[����
--�V�K�ǉ����ڕ��̓`�[�`���̐ݒ�����p��
UPDATE setting_info SET setting_val = (SELECT setting_val FROM setting_info WHERE setting_name = 'op21mparam_denpyou_nyuuryoku_pattern') 
 WHERE setting_name LIKE 'op21mparam_denpyou_nyuuryoku_pattern_A%'
   AND setting_name <>   'op21mparam_denpyou_nyuuryoku_pattern_A001'
   AND setting_name <>   'op21mparam_denpyou_nyuuryoku_pattern_A004'
   AND setting_name <>   'op21mparam_denpyou_nyuuryoku_pattern_A011';
--�V�K�ǉ����ڕ��̓`�[���̓p�^�[���̐ݒ�����p��
UPDATE setting_info SET setting_val = (SELECT setting_val FROM setting_info WHERE setting_name = 'op21mparam_denpyou_keishiki') 
 WHERE setting_name LIKE 'op21mparam_denpyou_keishiki_A%'
   AND setting_name <>   'op21mparam_denpyou_keishiki_A001'
   AND setting_name <>   'op21mparam_denpyou_keishiki_A004'
   AND setting_name <>   'op21mparam_denpyou_keishiki_A011';

--573_�抷�ē����� ��������UPDATE�ł͘A������ۂ��߁A�����l�������@�\�ɂ��킹�ĕύX
UPDATE setting_info SET setting_val = '2' WHERE setting_name = 'ekispert_teikikukan_haneisaki';


--574_�P��`���̉����c���d��쐬 ���@�[�W�����A�b�v�O��ō쐬�����d��f�[�^�ɍ��ق��łȂ��悤�ݒ��ύX
UPDATE setting_info SET setting_val='1'
 WHERE (setting_name = 'zankin_shiwake_sakusei_umu_A001' OR setting_name = 'zankin_shiwake_hi_A001')
   AND '0' = (SELECT setting_val FROM setting_info WHERE setting_name = 'op21mparam_denpyou_keishiki_A001');
UPDATE setting_info SET setting_val='1'
 WHERE (setting_name = 'zankin_shiwake_sakusei_umu_A004' OR setting_name = 'zankin_shiwake_hi_A004')
   AND '0' = (SELECT setting_val FROM setting_info WHERE setting_name = 'op21mparam_denpyou_keishiki_A004');
UPDATE setting_info SET setting_val='1'
 WHERE (setting_name = 'zankin_shiwake_sakusei_umu_A011' OR setting_name = 'zankin_shiwake_hi_A011')
   AND '0' = (SELECT setting_val FROM setting_info WHERE setting_name = 'op21mparam_denpyou_keishiki_A011');


-- ��ʍ��ڐ���̏C��
CREATE TABLE gamen_koumoku_seigyo_tmp AS SELECT * FROM gamen_koumoku_seigyo;
DELETE FROM gamen_koumoku_seigyo WHERE denpyou_kbn LIKE 'A%';
\copy gamen_koumoku_seigyo FROM '.\files\csv\gamen_koumoku_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE gamen_koumoku_seigyo new SET
  koumoku_name = (SELECT koumoku_name FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
  ,hyouji_flg  = (SELECT hyouji_flg   FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
  ,hissu_flg   = (SELECT hissu_flg    FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
  ,pdf_hyouji_flg = (SELECT pdf_hyouji_flg FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
  ,code_output_flg = (SELECT code_output_flg FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp);
DROP TABLE gamen_koumoku_seigyo_tmp;

-- �}�X�^�[�Ǘ��̐U�������[��(���t)�e�[�u���������C��
delete from master_torikomi_ichiran_sias
where master_id = 'kaisha_info' or master_id = 'kamoku_master';

delete from master_torikomi_shousai_sias
where master_id = 'kaisha_info' or master_id = 'kamoku_master';

-- �}�X�^�[�Ǘ��̐U�������[��(�j��)�e�[�u��������ǉ�
\copy master_kanri_hansuu FROM '.\files\csv\master_kanri_hansuu_tmp_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_kanri_ichiran FROM '.\files\csv\master_kanri_ichiran_tmp_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM master_torikomi_ichiran_de3;
DELETE FROM master_torikomi_shousai_de3;
DELETE FROM master_torikomi_ichiran_sias;
DELETE FROM master_torikomi_shousai_sias;
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- ��ʌ�������̒ǉ�
-- ��ʌ�������̍X�V
DELETE from gamen_kengen_seigyo;
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- �����R�[�h ���I�v�V�����p���R�[�h�̓p�b�`�O�ɂȂ���΍폜
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

-- �d��p�^�[���ϐ��ݒ�
\copy shiwake_pattern_var_setting FROM '.\files\csv\shiwake_pattern_var_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- shiwake_pattern_setting ����ւ�
CREATE TABLE shiwake_pattern_setting_tmp AS SELECT * FROM shiwake_pattern_setting WHERE 1 = 2;
\copy shiwake_pattern_setting_tmp FROM '.\files\csv\shiwake_pattern_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM shiwake_pattern_setting_tmp WHERE denpyou_kbn='A003' AND (SELECT COUNT(*) FROM shiwake_pattern_setting WHERE denpyou_kbn='A003') = 0;
DELETE FROM shiwake_pattern_setting;
INSERT INTO shiwake_pattern_setting SELECT * FROM shiwake_pattern_setting_tmp;
DROP TABLE shiwake_pattern_setting_tmp;

-- �d��p�^�[���}�X�^�[
ALTER TABLE SHIWAKE_PATTERN_MASTER RENAME TO SHIWAKE_PATTERN_MASTER_OLD;
CREATE TABLE SHIWAKE_PATTERN_MASTER
(
	DENPYOU_KBN VARCHAR(4) NOT NULL,
	SHIWAKE_EDANO INT NOT NULL,
	DELETE_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
	YUUKOU_KIGEN_FROM DATE NOT NULL,
	YUUKOU_KIGEN_TO DATE NOT NULL,
	BUNRUI1 VARCHAR(20) NOT NULL,
	BUNRUI2 VARCHAR(20) NOT NULL,
	BUNRUI3 VARCHAR(20) NOT NULL,
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	TEKIYOU_FLG VARCHAR(1) NOT NULL,
	TEKIYOU VARCHAR(20),
	DEFAULT_HYOUJI_FLG VARCHAR(1) NOT NULL,
	KOUSAIHI_HYOUJI_FLG VARCHAR(1) NOT NULL,
	KAKE_FLG VARCHAR(1) NOT NULL,
	HYOUJI_JUN INT NOT NULL,
	SHAIN_CD_RENKEI_FLG VARCHAR(1) NOT NULL,
	KARI_FUTAN_BUMON_CD VARCHAR NOT NULL,
	KARI_KAMOKU_CD VARCHAR NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR NOT NULL,
	KARI_TORIHIKISAKI_CD VARCHAR NOT NULL,
	KARI_PROJECT_CD VARCHAR NOT NULL,
	KARI_SEGMENT_CD VARCHAR(8) NOT NULL,
	KARI_UF1_CD VARCHAR(20) NOT NULL,
	KARI_UF2_CD VARCHAR(20) NOT NULL,
	KARI_UF3_CD VARCHAR(20) NOT NULL,
	KARI_UF4_CD VARCHAR(20) NOT NULL,
	KARI_UF5_CD VARCHAR(20) NOT NULL,
	KARI_UF6_CD VARCHAR(20) NOT NULL,
	KARI_UF7_CD VARCHAR(20) NOT NULL,
	KARI_UF8_CD VARCHAR(20) NOT NULL,
	KARI_UF9_CD VARCHAR(20) NOT NULL,
	KARI_UF10_CD VARCHAR(20) NOT NULL,
	KARI_UF_KOTEI1_CD VARCHAR(20) NOT NULL,
	KARI_UF_KOTEI2_CD VARCHAR(20) NOT NULL,
	KARI_UF_KOTEI3_CD VARCHAR(20) NOT NULL,
	KARI_UF_KOTEI4_CD VARCHAR(20) NOT NULL,
	KARI_UF_KOTEI5_CD VARCHAR(20) NOT NULL,
	KARI_UF_KOTEI6_CD VARCHAR(20) NOT NULL,
	KARI_UF_KOTEI7_CD VARCHAR(20) NOT NULL,
	KARI_UF_KOTEI8_CD VARCHAR(20) NOT NULL,
	KARI_UF_KOTEI9_CD VARCHAR(20) NOT NULL,
	KARI_UF_KOTEI10_CD VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR NOT NULL,
	KASHI_FUTAN_BUMON_CD1 VARCHAR NOT NULL,
	KASHI_KAMOKU_CD1 VARCHAR NOT NULL,
	KASHI_KAMOKU_EDABAN_CD1 VARCHAR NOT NULL,
	KASHI_TORIHIKISAKI_CD1 VARCHAR NOT NULL,
	KASHI_PROJECT_CD1 VARCHAR NOT NULL,
	KASHI_SEGMENT_CD1 VARCHAR(8) NOT NULL,
	KASHI_UF1_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF2_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF3_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF4_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF5_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF6_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF7_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF8_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF9_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF10_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI1_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI2_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI3_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI4_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI5_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI6_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI7_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI8_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI9_CD1 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI10_CD1 VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN1 VARCHAR NOT NULL,
	KASHI_FUTAN_BUMON_CD2 VARCHAR NOT NULL,
	KASHI_TORIHIKISAKI_CD2 VARCHAR NOT NULL,
	KASHI_KAMOKU_CD2 VARCHAR NOT NULL,
	KASHI_KAMOKU_EDABAN_CD2 VARCHAR NOT NULL,
	KASHI_PROJECT_CD2 VARCHAR NOT NULL,
	KASHI_SEGMENT_CD2 VARCHAR(8) NOT NULL,
	KASHI_UF1_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF2_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF3_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF4_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF5_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF6_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF7_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF8_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF9_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF10_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI1_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI2_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI3_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI4_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI5_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI6_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI7_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI8_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI9_CD2 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI10_CD2 VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN2 VARCHAR NOT NULL,
	KASHI_FUTAN_BUMON_CD3 VARCHAR NOT NULL,
	KASHI_TORIHIKISAKI_CD3 VARCHAR NOT NULL,
	KASHI_KAMOKU_CD3 VARCHAR NOT NULL,
	KASHI_KAMOKU_EDABAN_CD3 VARCHAR NOT NULL,
	KASHI_PROJECT_CD3 VARCHAR NOT NULL,
	KASHI_SEGMENT_CD3 VARCHAR(8) NOT NULL,
	KASHI_UF1_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF2_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF3_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF4_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF5_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF6_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF7_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF8_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF9_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF10_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI1_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI2_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI3_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI4_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI5_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI6_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI7_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI8_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI9_CD3 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI10_CD3 VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN3 VARCHAR NOT NULL,
	KASHI_FUTAN_BUMON_CD4 VARCHAR NOT NULL,
	KASHI_TORIHIKISAKI_CD4 VARCHAR NOT NULL,
	KASHI_KAMOKU_CD4 VARCHAR NOT NULL,
	KASHI_KAMOKU_EDABAN_CD4 VARCHAR NOT NULL,
	KASHI_PROJECT_CD4 VARCHAR NOT NULL,
	KASHI_SEGMENT_CD4 VARCHAR(8) NOT NULL,
	KASHI_UF1_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF2_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF3_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF4_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF5_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF6_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF7_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF8_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF9_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF10_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI1_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI2_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI3_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI4_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI5_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI6_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI7_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI8_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI9_CD4 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI10_CD4 VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN4 VARCHAR NOT NULL,
	KASHI_FUTAN_BUMON_CD5 VARCHAR NOT NULL,
	KASHI_TORIHIKISAKI_CD5 VARCHAR NOT NULL,
	KASHI_KAMOKU_CD5 VARCHAR NOT NULL,
	KASHI_KAMOKU_EDABAN_CD5 VARCHAR NOT NULL,
	KASHI_PROJECT_CD5 VARCHAR NOT NULL,
	KASHI_SEGMENT_CD5 VARCHAR(8) NOT NULL,
	KASHI_UF1_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF2_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF3_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF4_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF5_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF6_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF7_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF8_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF9_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF10_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI1_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI2_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI3_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI4_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI5_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI6_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI7_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI8_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI9_CD5 VARCHAR(20) NOT NULL,
	KASHI_UF_KOTEI10_CD5 VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN5 VARCHAR NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_KBN, SHIWAKE_EDANO)
) WITHOUT OIDS;
COMMENT ON TABLE SHIWAKE_PATTERN_MASTER IS '�d��p�^�[���}�X�^�[';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.DENPYOU_KBN IS '�`�[�敪';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.DELETE_FLG IS '�폜�t���O';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.YUUKOU_KIGEN_FROM IS '�L�������J�n��';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.YUUKOU_KIGEN_TO IS '�L�������I����';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.BUNRUI1 IS '����1';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.BUNRUI2 IS '����2';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.BUNRUI3 IS '����3';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.TEKIYOU_FLG IS '�E�v�t���O';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.TEKIYOU IS '�E�v';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.DEFAULT_HYOUJI_FLG IS '�f�t�H���g�\���t���O';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KOUSAIHI_HYOUJI_FLG IS '���۔�\���t���O';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KAKE_FLG IS '�|���t���O';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.HYOUJI_JUN IS '�\����';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.SHAIN_CD_RENKEI_FLG IS '�Ј��R�[�h�A�g�t���O';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_TORIHIKISAKI_CD IS '�ؕ������R�[�h�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_PROJECT_CD IS '�ؕ��v���W�F�N�g�R�[�h�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_SEGMENT_CD IS '�ؕ��Z�O�����g�R�[�h';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF1_CD IS '�ؕ�UF1�R�[�h';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF2_CD IS '�ؕ�UF2�R�[�h';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF3_CD IS '�ؕ�UF3�R�[�h';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF4_CD IS '�ؕ�UF4�R�[�h';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF5_CD IS '�ؕ�UF5�R�[�h';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF6_CD IS '�ؕ�UF6�R�[�h';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF7_CD IS '�ؕ�UF7�R�[�h';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF8_CD IS '�ؕ�UF8�R�[�h';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF9_CD IS '�ؕ�UF9�R�[�h';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF10_CD IS '�ؕ�UF10�R�[�h';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF_KOTEI1_CD IS '�ؕ�UF1�R�[�h�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF_KOTEI2_CD IS '�ؕ�UF2�R�[�h�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF_KOTEI3_CD IS '�ؕ�UF3�R�[�h�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF_KOTEI4_CD IS '�ؕ�UF4�R�[�h�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF_KOTEI5_CD IS '�ؕ�UF5�R�[�h�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF_KOTEI6_CD IS '�ؕ�UF6�R�[�h�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF_KOTEI7_CD IS '�ؕ�UF7�R�[�h�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF_KOTEI8_CD IS '�ؕ�UF8�R�[�h�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF_KOTEI9_CD IS '�ؕ�UF9�R�[�h�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_UF_KOTEI10_CD IS '�ؕ�UF10�R�[�h�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_FUTAN_BUMON_CD1 IS '�ݕ����S����R�[�h�P�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAMOKU_CD1 IS '�ݕ��ȖڃR�[�h�P�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAMOKU_EDABAN_CD1 IS '�ݕ��Ȗڎ}�ԃR�[�h�P�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_TORIHIKISAKI_CD1 IS '�ݕ������R�[�h�P�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_PROJECT_CD1 IS '�ݕ��v���W�F�N�g�R�[�h�P�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_SEGMENT_CD1 IS '�ݕ��Z�O�����g�R�[�h�P�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF1_CD1 IS '�ݕ�UF1�R�[�h�P';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF2_CD1 IS '�ݕ�UF2�R�[�h�P';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF3_CD1 IS '�ݕ�UF3�R�[�h�P';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF4_CD1 IS '�ݕ�UF4�R�[�h�P';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF5_CD1 IS '�ݕ�UF5�R�[�h�P';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF6_CD1 IS '�ݕ�UF6�R�[�h�P';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF7_CD1 IS '�ݕ�UF7�R�[�h�P';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF8_CD1 IS '�ݕ�UF8�R�[�h�P';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF9_CD1 IS '�ݕ�UF9�R�[�h�P';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF10_CD1 IS '�ݕ�UF10�R�[�h�P';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI1_CD1 IS '�ݕ�UF1�R�[�h�P�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI2_CD1 IS '�ݕ�UF2�R�[�h�P�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI3_CD1 IS '�ݕ�UF3�R�[�h�P�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI4_CD1 IS '�ݕ�UF4�R�[�h�P�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI5_CD1 IS '�ݕ�UF5�R�[�h�P�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI6_CD1 IS '�ݕ�UF6�R�[�h�P�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI7_CD1 IS '�ݕ�UF7�R�[�h�P�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI8_CD1 IS '�ݕ�UF8�R�[�h�P�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI9_CD1 IS '�ݕ�UF9�R�[�h�P�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI10_CD1 IS '�ݕ�UF10�R�[�h�P�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAZEI_KBN1 IS '�ݕ��ېŋ敪�P�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_FUTAN_BUMON_CD2 IS '�ݕ����S����R�[�h�Q�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_TORIHIKISAKI_CD2 IS '�ݕ������R�[�h�Q�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAMOKU_CD2 IS '�ݕ��ȖڃR�[�h�Q�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAMOKU_EDABAN_CD2 IS '�ݕ��Ȗڎ}�ԃR�[�h�Q�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_PROJECT_CD2 IS '�ݕ��v���W�F�N�g�R�[�h�Q�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_SEGMENT_CD2 IS '�ݕ��Z�O�����g�R�[�h�Q�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF1_CD2 IS '�ݕ�UF1�R�[�h�Q';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF2_CD2 IS '�ݕ�UF2�R�[�h�Q';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF3_CD2 IS '�ݕ�UF3�R�[�h�Q';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF4_CD2 IS '�ݕ�UF4�R�[�h�Q';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF5_CD2 IS '�ݕ�UF5�R�[�h�Q';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF6_CD2 IS '�ݕ�UF6�R�[�h�Q';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF7_CD2 IS '�ݕ�UF7�R�[�h�Q';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF8_CD2 IS '�ݕ�UF8�R�[�h�Q';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF9_CD2 IS '�ݕ�UF9�R�[�h�Q';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF10_CD2 IS '�ݕ�UF10�R�[�h�Q';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI1_CD2 IS '�ݕ�UF1�R�[�h�Q�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI2_CD2 IS '�ݕ�UF2�R�[�h�Q�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI3_CD2 IS '�ݕ�UF3�R�[�h�Q�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI4_CD2 IS '�ݕ�UF4�R�[�h�Q�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI5_CD2 IS '�ݕ�UF5�R�[�h�Q�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI6_CD2 IS '�ݕ�UF6�R�[�h�Q�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI7_CD2 IS '�ݕ�UF7�R�[�h�Q�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI8_CD2 IS '�ݕ�UF8�R�[�h�Q�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI9_CD2 IS '�ݕ�UF9�R�[�h�Q�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI10_CD2 IS '�ݕ�UF10�R�[�h�Q�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAZEI_KBN2 IS '�ݕ��ېŋ敪�Q�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_FUTAN_BUMON_CD3 IS '�ݕ����S����R�[�h�R�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_TORIHIKISAKI_CD3 IS '�ݕ������R�[�h�R�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAMOKU_CD3 IS '�ݕ��ȖڃR�[�h�R�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAMOKU_EDABAN_CD3 IS '�ݕ��Ȗڎ}�ԃR�[�h�R�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_PROJECT_CD3 IS '�ݕ��v���W�F�N�g�R�[�h�R�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_SEGMENT_CD3 IS '�ݕ��Z�O�����g�R�[�h�R�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF1_CD3 IS '�ݕ�UF1�R�[�h�R';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF2_CD3 IS '�ݕ�UF2�R�[�h�R';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF3_CD3 IS '�ݕ�UF3�R�[�h�R';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF4_CD3 IS '�ݕ�UF4�R�[�h�R';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF5_CD3 IS '�ݕ�UF5�R�[�h�R';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF6_CD3 IS '�ݕ�UF6�R�[�h�R';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF7_CD3 IS '�ݕ�UF7�R�[�h�R';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF8_CD3 IS '�ݕ�UF8�R�[�h�R';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF9_CD3 IS '�ݕ�UF9�R�[�h�R';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF10_CD3 IS '�ݕ�UF10�R�[�h�R';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI1_CD3 IS '�ݕ�UF1�R�[�h�R�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI2_CD3 IS '�ݕ�UF2�R�[�h�R�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI3_CD3 IS '�ݕ�UF3�R�[�h�R�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI4_CD3 IS '�ݕ�UF4�R�[�h�R�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI5_CD3 IS '�ݕ�UF5�R�[�h�R�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI6_CD3 IS '�ݕ�UF6�R�[�h�R�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI7_CD3 IS '�ݕ�UF7�R�[�h�R�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI8_CD3 IS '�ݕ�UF8�R�[�h�R�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI9_CD3 IS '�ݕ�UF9�R�[�h�R�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI10_CD3 IS '�ݕ�UF10�R�[�h�R�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAZEI_KBN3 IS '�ݕ��ېŋ敪�R�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_FUTAN_BUMON_CD4 IS '�ݕ����S����R�[�h�S�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_TORIHIKISAKI_CD4 IS '�ݕ������R�[�h�S�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAMOKU_CD4 IS '�ݕ��ȖڃR�[�h�S�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAMOKU_EDABAN_CD4 IS '�ݕ��Ȗڎ}�ԃR�[�h�S�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_PROJECT_CD4 IS '�ݕ��v���W�F�N�g�R�[�h�S�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_SEGMENT_CD4 IS '�ݕ��Z�O�����g�R�[�h�S�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF1_CD4 IS '�ݕ�UF1�R�[�h�S';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF2_CD4 IS '�ݕ�UF2�R�[�h�S';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF3_CD4 IS '�ݕ�UF3�R�[�h�S';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF4_CD4 IS '�ݕ�UF4�R�[�h�S';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF5_CD4 IS '�ݕ�UF5�R�[�h�S';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF6_CD4 IS '�ݕ�UF6�R�[�h�S';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF7_CD4 IS '�ݕ�UF7�R�[�h�S';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF8_CD4 IS '�ݕ�UF8�R�[�h�S';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF9_CD4 IS '�ݕ�UF9�R�[�h�S';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF10_CD4 IS '�ݕ�UF10�R�[�h�S';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI1_CD4 IS '�ݕ�UF1�R�[�h�S�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI2_CD4 IS '�ݕ�UF2�R�[�h�S�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI3_CD4 IS '�ݕ�UF3�R�[�h�S�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI4_CD4 IS '�ݕ�UF4�R�[�h�S�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI5_CD4 IS '�ݕ�UF5�R�[�h�S�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI6_CD4 IS '�ݕ�UF6�R�[�h�S�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI7_CD4 IS '�ݕ�UF7�R�[�h�S�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI8_CD4 IS '�ݕ�UF8�R�[�h�S�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI9_CD4 IS '�ݕ�UF9�R�[�h�S�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI10_CD4 IS '�ݕ�UF10�R�[�h�S�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAZEI_KBN4 IS '�ݕ��ېŋ敪�S�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_FUTAN_BUMON_CD5 IS '�ݕ����S����R�[�h�T�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_TORIHIKISAKI_CD5 IS '�ݕ������R�[�h�T�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAMOKU_CD5 IS '�ݕ��ȖڃR�[�h�T�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAMOKU_EDABAN_CD5 IS '�ݕ��Ȗڎ}�ԃR�[�h�T�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_PROJECT_CD5 IS '�ݕ��v���W�F�N�g�R�[�h�T�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_SEGMENT_CD5 IS '�ݕ��Z�O�����g�R�[�h�T�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF1_CD5 IS '�ݕ�UF1�R�[�h�T';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF2_CD5 IS '�ݕ�UF2�R�[�h�T';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF3_CD5 IS '�ݕ�UF3�R�[�h�T';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF4_CD5 IS '�ݕ�UF4�R�[�h�T';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF5_CD5 IS '�ݕ�UF5�R�[�h�T';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF6_CD5 IS '�ݕ�UF6�R�[�h�T';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF7_CD5 IS '�ݕ�UF7�R�[�h�T';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF8_CD5 IS '�ݕ�UF8�R�[�h�T';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF9_CD5 IS '�ݕ�UF9�R�[�h�T';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF10_CD5 IS '�ݕ�UF10�R�[�h�T';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI1_CD5 IS '�ݕ�UF1�R�[�h�T�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI2_CD5 IS '�ݕ�UF2�R�[�h�T�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI3_CD5 IS '�ݕ�UF3�R�[�h�T�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI4_CD5 IS '�ݕ�UF4�R�[�h�T�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI5_CD5 IS '�ݕ�UF5�R�[�h�T�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI6_CD5 IS '�ݕ�UF6�R�[�h�T�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI7_CD5 IS '�ݕ�UF7�R�[�h�T�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI8_CD5 IS '�ݕ�UF8�R�[�h�T�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI9_CD5 IS '�ݕ�UF9�R�[�h�T�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_UF_KOTEI10_CD5 IS '�ݕ�UF10�R�[�h�T�i�Œ�j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KASHI_KAZEI_KBN5 IS '�ݕ��ېŋ敪�T�i�d��p�^�[���j';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN SHIWAKE_PATTERN_MASTER.KOUSHIN_TIME IS '�X�V����';

INSERT 
INTO shiwake_pattern_master( 
  denpyou_kbn
  , shiwake_edano
  , delete_flg
  , yuukou_kigen_from
  , yuukou_kigen_to
  , bunrui1
  , bunrui2
  , bunrui3
  , torihiki_name
  , tekiyou_flg
  , tekiyou
  , default_hyouji_flg
  , kousaihi_hyouji_flg
  , kake_flg
  , hyouji_jun
  , shain_cd_renkei_flg
  , kari_futan_bumon_cd
  , kari_kamoku_cd
  , kari_kamoku_edaban_cd
  , kari_torihikisaki_cd
  , kari_project_cd
  , kari_segment_cd
  , kari_uf1_cd
  , kari_uf2_cd
  , kari_uf3_cd
  , kari_uf4_cd
  , kari_uf5_cd
  , kari_uf6_cd
  , kari_uf7_cd
  , kari_uf8_cd
  , kari_uf9_cd
  , kari_uf10_cd
  , kari_uf_kotei1_cd
  , kari_uf_kotei2_cd
  , kari_uf_kotei3_cd
  , kari_uf_kotei4_cd
  , kari_uf_kotei5_cd
  , kari_uf_kotei6_cd
  , kari_uf_kotei7_cd
  , kari_uf_kotei8_cd
  , kari_uf_kotei9_cd
  , kari_uf_kotei10_cd
  , kari_kazei_kbn
  , kashi_futan_bumon_cd1
  , kashi_kamoku_cd1
  , kashi_kamoku_edaban_cd1
  , kashi_torihikisaki_cd1
  , kashi_project_cd1
  , kashi_segment_cd1
  , kashi_uf1_cd1
  , kashi_uf2_cd1
  , kashi_uf3_cd1
  , kashi_uf4_cd1
  , kashi_uf5_cd1
  , kashi_uf6_cd1
  , kashi_uf7_cd1
  , kashi_uf8_cd1
  , kashi_uf9_cd1
  , kashi_uf10_cd1
  , kashi_uf_kotei1_cd1
  , kashi_uf_kotei2_cd1
  , kashi_uf_kotei3_cd1
  , kashi_uf_kotei4_cd1
  , kashi_uf_kotei5_cd1
  , kashi_uf_kotei6_cd1
  , kashi_uf_kotei7_cd1
  , kashi_uf_kotei8_cd1
  , kashi_uf_kotei9_cd1
  , kashi_uf_kotei10_cd1
  , kashi_kazei_kbn1
  , kashi_futan_bumon_cd2
  , kashi_torihikisaki_cd2
  , kashi_kamoku_cd2
  , kashi_kamoku_edaban_cd2
  , kashi_project_cd2
  , kashi_segment_cd2
  , kashi_uf1_cd2
  , kashi_uf2_cd2
  , kashi_uf3_cd2
  , kashi_uf4_cd2
  , kashi_uf5_cd2
  , kashi_uf6_cd2
  , kashi_uf7_cd2
  , kashi_uf8_cd2
  , kashi_uf9_cd2
  , kashi_uf10_cd2
  , kashi_uf_kotei1_cd2
  , kashi_uf_kotei2_cd2
  , kashi_uf_kotei3_cd2
  , kashi_uf_kotei4_cd2
  , kashi_uf_kotei5_cd2
  , kashi_uf_kotei6_cd2
  , kashi_uf_kotei7_cd2
  , kashi_uf_kotei8_cd2
  , kashi_uf_kotei9_cd2
  , kashi_uf_kotei10_cd2
  , kashi_kazei_kbn2
  , kashi_futan_bumon_cd3
  , kashi_torihikisaki_cd3
  , kashi_kamoku_cd3
  , kashi_kamoku_edaban_cd3
  , kashi_project_cd3
  , kashi_segment_cd3
  , kashi_uf1_cd3
  , kashi_uf2_cd3
  , kashi_uf3_cd3
  , kashi_uf4_cd3
  , kashi_uf5_cd3
  , kashi_uf6_cd3
  , kashi_uf7_cd3
  , kashi_uf8_cd3
  , kashi_uf9_cd3
  , kashi_uf10_cd3
  , kashi_uf_kotei1_cd3
  , kashi_uf_kotei2_cd3
  , kashi_uf_kotei3_cd3
  , kashi_uf_kotei4_cd3
  , kashi_uf_kotei5_cd3
  , kashi_uf_kotei6_cd3
  , kashi_uf_kotei7_cd3
  , kashi_uf_kotei8_cd3
  , kashi_uf_kotei9_cd3
  , kashi_uf_kotei10_cd3
  , kashi_kazei_kbn3
  , kashi_futan_bumon_cd4
  , kashi_torihikisaki_cd4
  , kashi_kamoku_cd4
  , kashi_kamoku_edaban_cd4
  , kashi_project_cd4
  , kashi_segment_cd4
  , kashi_uf1_cd4
  , kashi_uf2_cd4
  , kashi_uf3_cd4
  , kashi_uf4_cd4
  , kashi_uf5_cd4
  , kashi_uf6_cd4
  , kashi_uf7_cd4
  , kashi_uf8_cd4
  , kashi_uf9_cd4
  , kashi_uf10_cd4
  , kashi_uf_kotei1_cd4
  , kashi_uf_kotei2_cd4
  , kashi_uf_kotei3_cd4
  , kashi_uf_kotei4_cd4
  , kashi_uf_kotei5_cd4
  , kashi_uf_kotei6_cd4
  , kashi_uf_kotei7_cd4
  , kashi_uf_kotei8_cd4
  , kashi_uf_kotei9_cd4
  , kashi_uf_kotei10_cd4
  , kashi_kazei_kbn4
  , kashi_futan_bumon_cd5
  , kashi_torihikisaki_cd5
  , kashi_kamoku_cd5
  , kashi_kamoku_edaban_cd5
  , kashi_project_cd5
  , kashi_segment_cd5
  , kashi_uf1_cd5
  , kashi_uf2_cd5
  , kashi_uf3_cd5
  , kashi_uf4_cd5
  , kashi_uf5_cd5
  , kashi_uf6_cd5
  , kashi_uf7_cd5
  , kashi_uf8_cd5
  , kashi_uf9_cd5
  , kashi_uf10_cd5
  , kashi_uf_kotei1_cd5
  , kashi_uf_kotei2_cd5
  , kashi_uf_kotei3_cd5
  , kashi_uf_kotei4_cd5
  , kashi_uf_kotei5_cd5
  , kashi_uf_kotei6_cd5
  , kashi_uf_kotei7_cd5
  , kashi_uf_kotei8_cd5
  , kashi_uf_kotei9_cd5
  , kashi_uf_kotei10_cd5
  , kashi_kazei_kbn5
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_kbn
  , shiwake_edano
  , delete_flg
  , yuukou_kigen_from
  , yuukou_kigen_to
  , bunrui1
  , bunrui2
  , bunrui3
  , torihiki_name
  , tekiyou_flg
  , tekiyou
  , default_hyouji_flg
  , kousaihi_hyouji_flg
  , kake_flg
  , hyouji_jun
  , shain_cd_renkei_flg
  , kari_futan_bumon_cd
  , kari_kamoku_cd
  , kari_kamoku_edaban_cd
  , kari_torihikisaki_cd
  , kari_project_cd
  , '' -- kari_segment_cd
  , kari_uf1_cd
  , kari_uf2_cd
  , kari_uf3_cd
  , '' -- kari_uf4_cd
  , '' -- kari_uf5_cd
  , '' -- kari_uf6_cd
  , '' -- kari_uf7_cd
  , '' -- kari_uf8_cd
  , '' -- kari_uf9_cd
  , '' -- kari_uf10_cd
  , '' -- kari_uf_kotei1_cd
  , '' -- kari_uf_kotei2_cd
  , '' -- kari_uf_kotei3_cd
  , '' -- kari_uf_kotei4_cd
  , '' -- kari_uf_kotei5_cd
  , '' -- kari_uf_kotei6_cd
  , '' -- kari_uf_kotei7_cd
  , '' -- kari_uf_kotei8_cd
  , '' -- kari_uf_kotei9_cd
  , '' -- kari_uf_kotei10_cd
  , kari_kazei_kbn
  , kashi_futan_bumon_cd1
  , kashi_kamoku_cd1
  , kashi_kamoku_edaban_cd1
  , kashi_torihikisaki_cd1
  , '' -- kashi_project_cd1
  , '' -- kashi_segment_cd1
  , kashi_uf1_cd1
  , kashi_uf2_cd1
  , kashi_uf3_cd1
  , '' -- kashi_uf4_cd1
  , '' -- kashi_uf5_cd1
  , '' -- kashi_uf6_cd1
  , '' -- kashi_uf7_cd1
  , '' -- kashi_uf8_cd1
  , '' -- kashi_uf9_cd1
  , '' -- kashi_uf10_cd1
  , '' -- kashi_uf_kotei1_cd1
  , '' -- kashi_uf_kotei2_cd1
  , '' -- kashi_uf_kotei3_cd1
  , '' -- kashi_uf_kotei4_cd1
  , '' -- kashi_uf_kotei5_cd1
  , '' -- kashi_uf_kotei6_cd1
  , '' -- kashi_uf_kotei7_cd1
  , '' -- kashi_uf_kotei8_cd1
  , '' -- kashi_uf_kotei9_cd1
  , '' -- kashi_uf_kotei10_cd1
  , kashi_kazei_kbn1
  , kashi_futan_bumon_cd2
  , kashi_torihikisaki_cd2
  , kashi_kamoku_cd2
  , kashi_kamoku_edaban_cd2
  , '' -- kashi_project_cd2
  , '' -- kashi_segment_cd2
  , kashi_uf1_cd2
  , kashi_uf2_cd2
  , kashi_uf3_cd2
  , '' -- kashi_uf4_cd2
  , '' -- kashi_uf5_cd2
  , '' -- kashi_uf6_cd2
  , '' -- kashi_uf7_cd2
  , '' -- kashi_uf8_cd2
  , '' -- kashi_uf9_cd2
  , '' -- kashi_uf10_cd2
  , '' -- kashi_uf_kotei1_cd2
  , '' -- kashi_uf_kotei2_cd2
  , '' -- kashi_uf_kotei3_cd2
  , '' -- kashi_uf_kotei4_cd2
  , '' -- kashi_uf_kotei5_cd2
  , '' -- kashi_uf_kotei6_cd2
  , '' -- kashi_uf_kotei7_cd2
  , '' -- kashi_uf_kotei8_cd2
  , '' -- kashi_uf_kotei9_cd2
  , '' -- kashi_uf_kotei10_cd2
  , kashi_kazei_kbn2
  , kashi_futan_bumon_cd3
  , kashi_torihikisaki_cd3
  , kashi_kamoku_cd3
  , kashi_kamoku_edaban_cd3
  , '' -- kashi_project_cd3
  , '' -- kashi_segment_cd3
  , kashi_uf1_cd3
  , kashi_uf2_cd3
  , kashi_uf3_cd3
  , '' -- kashi_uf4_cd3
  , '' -- kashi_uf5_cd3
  , '' -- kashi_uf6_cd3
  , '' -- kashi_uf7_cd3
  , '' -- kashi_uf8_cd3
  , '' -- kashi_uf9_cd3
  , '' -- kashi_uf10_cd3
  , '' -- kashi_uf_kotei1_cd3
  , '' -- kashi_uf_kotei2_cd3
  , '' -- kashi_uf_kotei3_cd3
  , '' -- kashi_uf_kotei4_cd3
  , '' -- kashi_uf_kotei5_cd3
  , '' -- kashi_uf_kotei6_cd3
  , '' -- kashi_uf_kotei7_cd3
  , '' -- kashi_uf_kotei8_cd3
  , '' -- kashi_uf_kotei9_cd3
  , '' -- kashi_uf_kotei10_cd3
  , kashi_kazei_kbn3
  , kashi_futan_bumon_cd4
  , kashi_torihikisaki_cd4
  , kashi_kamoku_cd4
  , kashi_kamoku_edaban_cd4
  , '' -- kashi_project_cd4
  , '' -- kashi_segment_cd4
  , kashi_uf1_cd4
  , kashi_uf2_cd4
  , kashi_uf3_cd4
  , '' -- kashi_uf4_cd4
  , '' -- kashi_uf5_cd4
  , '' -- kashi_uf6_cd4
  , '' -- kashi_uf7_cd4
  , '' -- kashi_uf8_cd4
  , '' -- kashi_uf9_cd4
  , '' -- kashi_uf10_cd4
  , '' -- kashi_uf_kotei1_cd4
  , '' -- kashi_uf_kotei2_cd4
  , '' -- kashi_uf_kotei3_cd4
  , '' -- kashi_uf_kotei4_cd4
  , '' -- kashi_uf_kotei5_cd4
  , '' -- kashi_uf_kotei6_cd4
  , '' -- kashi_uf_kotei7_cd4
  , '' -- kashi_uf_kotei8_cd4
  , '' -- kashi_uf_kotei9_cd4
  , '' -- kashi_uf_kotei10_cd4
  , kashi_kazei_kbn4
  , kashi_futan_bumon_cd5
  , kashi_torihikisaki_cd5
  , kashi_kamoku_cd5
  , kashi_kamoku_edaban_cd5
  , '' -- kashi_project_cd5
  , '' -- kashi_segment_cd5
  , kashi_uf1_cd5
  , kashi_uf2_cd5
  , kashi_uf3_cd5
  , '' -- kashi_uf4_cd5
  , '' -- kashi_uf5_cd5
  , '' -- kashi_uf6_cd5
  , '' -- kashi_uf7_cd5
  , '' -- kashi_uf8_cd5
  , '' -- kashi_uf9_cd5
  , '' -- kashi_uf10_cd5
  , '' -- kashi_uf_kotei1_cd5
  , '' -- kashi_uf_kotei2_cd5
  , '' -- kashi_uf_kotei3_cd5
  , '' -- kashi_uf_kotei4_cd5
  , '' -- kashi_uf_kotei5_cd5
  , '' -- kashi_uf_kotei6_cd5
  , '' -- kashi_uf_kotei7_cd5
  , '' -- kashi_uf_kotei8_cd5
  , '' -- kashi_uf_kotei9_cd5
  , '' -- kashi_uf_kotei10_cd5
  , kashi_kazei_kbn5
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  shiwake_pattern_master_old;
DROP TABLE shiwake_pattern_master_old;

-- ��Џ��
ALTER TABLE KAISHA_INFO RENAME TO KAISHA_INFO_OLD;
CREATE TABLE KAISHA_INFO
(
	KESSANKI_BANGOU SMALLINT NOT NULL,
	HF1_SHIYOU_FLG SMALLINT NOT NULL,
	HF1_HISSU_FLG VARCHAR(1) NOT NULL,
	HF1_NAME VARCHAR NOT NULL,
	HF2_SHIYOU_FLG SMALLINT NOT NULL,
	HF2_HISSU_FLG VARCHAR(1) NOT NULL,
	HF2_NAME VARCHAR NOT NULL,
	HF3_SHIYOU_FLG SMALLINT NOT NULL,
	HF3_HISSU_FLG VARCHAR(1) NOT NULL,
	HF3_NAME VARCHAR NOT NULL,
	HF4_SHIYOU_FLG SMALLINT NOT NULL,
	HF4_HISSU_FLG VARCHAR(1) NOT NULL,
	HF4_NAME VARCHAR NOT NULL,
	HF5_SHIYOU_FLG SMALLINT NOT NULL,
	HF5_HISSU_FLG VARCHAR(1) NOT NULL,
	HF5_NAME VARCHAR NOT NULL,
	HF6_SHIYOU_FLG SMALLINT NOT NULL,
	HF6_HISSU_FLG VARCHAR(1) NOT NULL,
	HF6_NAME VARCHAR NOT NULL,
	HF7_SHIYOU_FLG SMALLINT NOT NULL,
	HF7_HISSU_FLG VARCHAR(1) NOT NULL,
	HF7_NAME VARCHAR NOT NULL,
	HF8_SHIYOU_FLG SMALLINT NOT NULL,
	HF8_HISSU_FLG VARCHAR(1) NOT NULL,
	HF8_NAME VARCHAR NOT NULL,
	HF9_SHIYOU_FLG SMALLINT NOT NULL,
	HF9_HISSU_FLG VARCHAR(1) NOT NULL,
	HF9_NAME VARCHAR NOT NULL,
	HF10_SHIYOU_FLG SMALLINT NOT NULL,
	HF10_HISSU_FLG VARCHAR(1) NOT NULL,
	HF10_NAME VARCHAR NOT NULL,
	UF1_SHIYOU_FLG SMALLINT NOT NULL,
	UF1_NAME VARCHAR NOT NULL,
	UF2_SHIYOU_FLG SMALLINT NOT NULL,
	UF2_NAME VARCHAR NOT NULL,
	UF3_SHIYOU_FLG SMALLINT NOT NULL,
	UF3_NAME VARCHAR NOT NULL,
	UF4_SHIYOU_FLG SMALLINT NOT NULL,
	UF4_NAME VARCHAR NOT NULL,
	UF5_SHIYOU_FLG SMALLINT NOT NULL,
	UF5_NAME VARCHAR NOT NULL,
	UF6_SHIYOU_FLG SMALLINT NOT NULL,
	UF6_NAME VARCHAR NOT NULL,
	UF7_SHIYOU_FLG SMALLINT NOT NULL,
	UF7_NAME VARCHAR NOT NULL,
	UF8_SHIYOU_FLG SMALLINT NOT NULL,
	UF8_NAME VARCHAR NOT NULL,
	UF9_SHIYOU_FLG SMALLINT NOT NULL,
	UF9_NAME VARCHAR NOT NULL,
	UF10_SHIYOU_FLG SMALLINT NOT NULL,
	UF10_NAME VARCHAR NOT NULL,
	UF_KOTEI1_SHIYOU_FLG SMALLINT NOT NULL,
	UF_KOTEI1_NAME VARCHAR NOT NULL,
	UF_KOTEI2_SHIYOU_FLG SMALLINT NOT NULL,
	UF_KOTEI2_NAME VARCHAR NOT NULL,
	UF_KOTEI3_SHIYOU_FLG SMALLINT NOT NULL,
	UF_KOTEI3_NAME VARCHAR NOT NULL,
	UF_KOTEI4_SHIYOU_FLG SMALLINT NOT NULL,
	UF_KOTEI4_NAME VARCHAR NOT NULL,
	UF_KOTEI5_SHIYOU_FLG SMALLINT NOT NULL,
	UF_KOTEI5_NAME VARCHAR NOT NULL,
	UF_KOTEI6_SHIYOU_FLG SMALLINT NOT NULL,
	UF_KOTEI6_NAME VARCHAR NOT NULL,
	UF_KOTEI7_SHIYOU_FLG SMALLINT NOT NULL,
	UF_KOTEI7_NAME VARCHAR NOT NULL,
	UF_KOTEI8_SHIYOU_FLG SMALLINT NOT NULL,
	UF_KOTEI8_NAME VARCHAR NOT NULL,
	UF_KOTEI9_SHIYOU_FLG SMALLINT NOT NULL,
	UF_KOTEI9_NAME VARCHAR NOT NULL,
	UF_KOTEI10_SHIYOU_FLG SMALLINT NOT NULL,
	UF_KOTEI10_NAME VARCHAR NOT NULL,
	PJCD_SHIYOU_FLG SMALLINT NOT NULL,
	SGCD_SHIYOU_FLG SMALLINT NOT NULL,
	SAIMU_SHIYOU_FLG VARCHAR(1) NOT NULL
) WITHOUT OIDS;
COMMENT ON TABLE KAISHA_INFO IS '��Џ��';
COMMENT ON COLUMN KAISHA_INFO.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN KAISHA_INFO.HF1_SHIYOU_FLG IS 'HF1�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF1_HISSU_FLG IS 'HF1�K�{�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF1_NAME IS 'HF1��';
COMMENT ON COLUMN KAISHA_INFO.HF2_SHIYOU_FLG IS 'HF2�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF2_HISSU_FLG IS 'HF2�K�{�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF2_NAME IS 'HF2��';
COMMENT ON COLUMN KAISHA_INFO.HF3_SHIYOU_FLG IS 'HF3�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF3_HISSU_FLG IS 'HF3�K�{�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF3_NAME IS 'HF3��';
COMMENT ON COLUMN KAISHA_INFO.HF4_SHIYOU_FLG IS 'HF4�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF4_HISSU_FLG IS 'HF4�K�{�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF4_NAME IS 'HF4��';
COMMENT ON COLUMN KAISHA_INFO.HF5_SHIYOU_FLG IS 'HF5�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF5_HISSU_FLG IS 'HF5�K�{�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF5_NAME IS 'HF5��';
COMMENT ON COLUMN KAISHA_INFO.HF6_SHIYOU_FLG IS 'HF6�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF6_HISSU_FLG IS 'HF6�K�{�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF6_NAME IS 'HF6��';
COMMENT ON COLUMN KAISHA_INFO.HF7_SHIYOU_FLG IS 'HF7�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF7_HISSU_FLG IS 'HF7�K�{�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF7_NAME IS 'HF7��';
COMMENT ON COLUMN KAISHA_INFO.HF8_SHIYOU_FLG IS 'HF8�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF8_HISSU_FLG IS 'HF8�K�{�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF8_NAME IS 'HF8��';
COMMENT ON COLUMN KAISHA_INFO.HF9_SHIYOU_FLG IS 'HF9�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF9_HISSU_FLG IS 'HF9�K�{�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF9_NAME IS 'HF9��';
COMMENT ON COLUMN KAISHA_INFO.HF10_SHIYOU_FLG IS 'HF10�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF10_HISSU_FLG IS 'HF10�K�{�t���O';
COMMENT ON COLUMN KAISHA_INFO.HF10_NAME IS 'HF10��';
COMMENT ON COLUMN KAISHA_INFO.UF1_SHIYOU_FLG IS 'UF1�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.UF1_NAME IS 'UF1��';
COMMENT ON COLUMN KAISHA_INFO.UF2_SHIYOU_FLG IS 'UF2�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.UF2_NAME IS 'UF2��';
COMMENT ON COLUMN KAISHA_INFO.UF3_SHIYOU_FLG IS 'UF3�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.UF3_NAME IS 'UF3��';
COMMENT ON COLUMN KAISHA_INFO.UF4_SHIYOU_FLG IS 'UF4�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.UF4_NAME IS 'UF4��';
COMMENT ON COLUMN KAISHA_INFO.UF5_SHIYOU_FLG IS 'UF5�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.UF5_NAME IS 'UF5��';
COMMENT ON COLUMN KAISHA_INFO.UF6_SHIYOU_FLG IS 'UF6�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.UF6_NAME IS 'UF6��';
COMMENT ON COLUMN KAISHA_INFO.UF7_SHIYOU_FLG IS 'UF7�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.UF7_NAME IS 'UF7��';
COMMENT ON COLUMN KAISHA_INFO.UF8_SHIYOU_FLG IS 'UF8�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.UF8_NAME IS 'UF8��';
COMMENT ON COLUMN KAISHA_INFO.UF9_SHIYOU_FLG IS 'UF9�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.UF9_NAME IS 'UF9��';
COMMENT ON COLUMN KAISHA_INFO.UF10_SHIYOU_FLG IS 'UF10�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.UF10_NAME IS 'UF10��';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI1_SHIYOU_FLG IS 'UF1�g�p�t���O(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI1_NAME IS 'UF1��(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI2_SHIYOU_FLG IS 'UF2�g�p�t���O(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI2_NAME IS 'UF2��(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI3_SHIYOU_FLG IS 'UF3�g�p�t���O(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI3_NAME IS 'UF3��(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI4_SHIYOU_FLG IS 'UF4�g�p�t���O(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI4_NAME IS 'UF4��(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI5_SHIYOU_FLG IS 'UF5�g�p�t���O(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI5_NAME IS 'UF5��(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI6_SHIYOU_FLG IS 'UF6�g�p�t���O(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI6_NAME IS 'UF6��(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI7_SHIYOU_FLG IS 'UF7�g�p�t���O(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI7_NAME IS 'UF7��(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI8_SHIYOU_FLG IS 'UF8�g�p�t���O(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI8_NAME IS 'UF8��(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI9_SHIYOU_FLG IS 'UF9�g�p�t���O(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI9_NAME IS 'UF9��(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI10_SHIYOU_FLG IS 'UF10�g�p�t���O(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.UF_KOTEI10_NAME IS 'UF10��(�Œ�l)';
COMMENT ON COLUMN KAISHA_INFO.PJCD_SHIYOU_FLG IS '�v���W�F�N�g�R�[�h�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.SGCD_SHIYOU_FLG IS '�Z�O�����g�R�[�h�g�p�t���O';
COMMENT ON COLUMN KAISHA_INFO.SAIMU_SHIYOU_FLG IS '���g�p�t���O';
INSERT 
INTO kaisha_info( 
  kessanki_bangou
  , hf1_shiyou_flg
  , hf1_hissu_flg
  , hf1_name
  , hf2_shiyou_flg
  , hf2_hissu_flg
  , hf2_name
  , hf3_shiyou_flg
  , hf3_hissu_flg
  , hf3_name
  , hf4_shiyou_flg
  , hf4_hissu_flg
  , hf4_name
  , hf5_shiyou_flg
  , hf5_hissu_flg
  , hf5_name
  , hf6_shiyou_flg
  , hf6_hissu_flg
  , hf6_name
  , hf7_shiyou_flg
  , hf7_hissu_flg
  , hf7_name
  , hf8_shiyou_flg
  , hf8_hissu_flg
  , hf8_name
  , hf9_shiyou_flg
  , hf9_hissu_flg
  , hf9_name
  , hf10_shiyou_flg
  , hf10_hissu_flg
  , hf10_name
  , uf1_shiyou_flg
  , uf1_name
  , uf2_shiyou_flg
  , uf2_name
  , uf3_shiyou_flg
  , uf3_name
  , uf4_shiyou_flg
  , uf4_name
  , uf5_shiyou_flg
  , uf5_name
  , uf6_shiyou_flg
  , uf6_name
  , uf7_shiyou_flg
  , uf7_name
  , uf8_shiyou_flg
  , uf8_name
  , uf9_shiyou_flg
  , uf9_name
  , uf10_shiyou_flg
  , uf10_name
  , uf_kotei1_shiyou_flg
  , uf_kotei1_name
  , uf_kotei2_shiyou_flg
  , uf_kotei2_name
  , uf_kotei3_shiyou_flg
  , uf_kotei3_name
  , uf_kotei4_shiyou_flg
  , uf_kotei4_name
  , uf_kotei5_shiyou_flg
  , uf_kotei5_name
  , uf_kotei6_shiyou_flg
  , uf_kotei6_name
  , uf_kotei7_shiyou_flg
  , uf_kotei7_name
  , uf_kotei8_shiyou_flg
  , uf_kotei8_name
  , uf_kotei9_shiyou_flg
  , uf_kotei9_name
  , uf_kotei10_shiyou_flg
  , uf_kotei10_name
  , pjcd_shiyou_flg
  , sgcd_shiyou_flg
  , SAIMU_SHIYOU_FLG
) 
SELECT
    kessanki_bangou
  , hf1_shiyou_flg
  , hf1_hissu_flg
  , hf1_name
  , hf2_shiyou_flg
  , hf2_hissu_flg
  , hf2_name
  , hf3_shiyou_flg
  , hf3_hissu_flg
  , hf3_name
  , 0 --   , hf4_shiyou_flg
  , 0 --   , hf4_hissu_flg
  , '' --   , hf4_name
  , 0 --   , hf5_shiyou_flg
  , 0 --   , hf5_hissu_flg
  , '' --   , hf5_name
  , 0 --   , hf6_shiyou_flg
  , 0 --   , hf6_hissu_flg
  , '' --   , hf6_name
  , 0 --   , hf7_shiyou_flg
  , 0 --   , hf7_hissu_flg
  , '' --   , hf7_name
  , 0 --   , hf8_shiyou_flg
  , 0 --   , hf8_hissu_flg
  , '' --   , hf8_name
  , 0 --   , hf9_shiyou_flg
  , 0 --   , hf9_hissu_flg
  , '' --   , hf9_name
  , 0 --   , hf10_shiyou_flg
  , 0 --   , hf10_hissu_flg
  , '' --   , hf10_name
  , uf1_shiyou_flg
  , uf1_name
  , uf2_shiyou_flg
  , uf2_name
  , uf3_shiyou_flg
  , uf3_name
  , 0 --   , uf4_shiyou_flg
  , '' --   , uf4_name
  , 00 --   , uf5_shiyou_flg
  , '' --   , uf5_name
  , 0 --   , uf6_shiyou_flg
  , '' --   , uf6_name
  , 0 --   , uf7_shiyou_flg
  , '' --   , uf7_name
  , 0 --   , uf8_shiyou_flg
  , '' --   , uf8_name
  , 0 --   , uf9_shiyou_flg
  , '' --   , uf9_name
  , 0 --   , uf10_shiyou_flg
  , '' --   , uf10_name
  , 0 --   , uf_kotei1_shiyou_flg
  , '' --   , uf_kotei1_name
  , 0 --   , uf_kotei2_shiyou_flg
  , '' --   , uf_kotei2_name
  , 0 --   , uf_kotei3_shiyou_flg
  , '' --   , uf_kotei3_name
  , 0 --   , uf_kotei4_shiyou_flg
  , '' --   , uf_kotei4_name
  , 0 --   , uf_kotei5_shiyou_flg
  , '' --   , uf_kotei5_name
  , 0 --   , uf_kotei6_shiyou_flg
  , '' --   , uf_kotei6_name
  , 0 --   , uf_kotei7_shiyou_flg
  , '' --   , uf_kotei7_name
  , 0 --   , uf_kotei8_shiyou_flg
  , '' --   , uf_kotei8_name
  , 0 --   , uf_kotei9_shiyou_flg
  , '' --   , uf_kotei9_name
  , 0 --   , uf_kotei10_shiyou_flg
  , '' --   , uf_kotei10_name
  , pjcd_shiyou_flg
  , 0 --   , sgcd_shiyou_flg 
  , '0' --SAIMU_SHIYOU_FLG
FROM
  kaisha_info_old; 
DROP TABLE kaisha_info_old;

-- �Ȗڃ}�X�^�[�C��
ALTER TABLE KAMOKU_MASTER RENAME TO KAMOKU_MASTER_OLD;
CREATE TABLE KAMOKU_MASTER
(
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22) NOT NULL,
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	TAISHAKU_ZOKUSEI SMALLINT,
	KAMOKU_GROUP_KBN SMALLINT,
	KAMOKU_GROUP_BANGOU SMALLINT,
	SHORI_GROUP SMALLINT,
	TAIKAKINGAKU_NYUURYOKU_FLG SMALLINT,
	KAZEI_KBN SMALLINT,
	BUNRI_KBN SMALLINT,
	SHIIRE_KBN SMALLINT,
	GYOUSHA_KBN SMALLINT,
	ZEIRITSU_KBN SMALLINT,
	TOKUSHA_HYOUJI_KBN SMALLINT,
	EDABAN_MINYUURYOKU_CHECK SMALLINT,
	TORIHIKISAKI_MINYUURYOKU_CHECK SMALLINT,
	BUMON_MINYUURYOKU_CHECK SMALLINT,
	BUMON_EDABAN_FLG SMALLINT,
	SEGMENT_MINYUURYOKU_CHECK SMALLINT,
	PROJECT_MINYUURYOKU_CHECK SMALLINT,
	UF1_MINYUURYOKU_CHECK SMALLINT,
	UF2_MINYUURYOKU_CHECK SMALLINT,
	UF3_MINYUURYOKU_CHECK SMALLINT,
	UF4_MINYUURYOKU_CHECK SMALLINT,
	UF5_MINYUURYOKU_CHECK SMALLINT,
	UF6_MINYUURYOKU_CHECK SMALLINT,
	UF7_MINYUURYOKU_CHECK SMALLINT,
	UF8_MINYUURYOKU_CHECK SMALLINT,
	UF9_MINYUURYOKU_CHECK SMALLINT,
	UF10_MINYUURYOKU_CHECK SMALLINT,
	UF_KOTEI1_MINYUURYOKU_CHECK SMALLINT,
	UF_KOTEI2_MINYUURYOKU_CHECK SMALLINT,
	UF_KOTEI3_MINYUURYOKU_CHECK SMALLINT,
	UF_KOTEI4_MINYUURYOKU_CHECK SMALLINT,
	UF_KOTEI5_MINYUURYOKU_CHECK SMALLINT,
	UF_KOTEI6_MINYUURYOKU_CHECK SMALLINT,
	UF_KOTEI7_MINYUURYOKU_CHECK SMALLINT,
	UF_KOTEI8_MINYUURYOKU_CHECK SMALLINT,
	UF_KOTEI9_MINYUURYOKU_CHECK SMALLINT,
	UF_KOTEI10_MINYUURYOKU_CHECK SMALLINT,
	KOUJI_MINYUURYOKU_CHECK SMALLINT,
	KOUSHA_MINYUURYOKU_CHECK SMALLINT,
	TEKIYOU_CD_MINYUURYOKU_CHECK SMALLINT,
	BUMON_TORHIKISAKI_KAMOKU_FLG SMALLINT,
	BUMON_TORIHIKISAKI_EDABAN_SHIYOU_FLG SMALLINT,
	TORIHIKISAKI_KAMOKU_EDABAN_FLG SMALLINT,
	SEGMENT_TORIHIKISAKI_KAMOKU_FLG SMALLINT,
	KARIKANJOU_KESHIKOMI_NO_FLG SMALLINT,
	PRIMARY KEY (KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE KAMOKU_MASTER IS '�Ȗڃ}�X�^�[';
COMMENT ON COLUMN KAMOKU_MASTER.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN KAMOKU_MASTER.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN KAMOKU_MASTER.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN KAMOKU_MASTER.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN KAMOKU_MASTER.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN KAMOKU_MASTER.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN KAMOKU_MASTER.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN KAMOKU_MASTER.KAMOKU_GROUP_KBN IS '�ȖڃO���[�v�敪';
COMMENT ON COLUMN KAMOKU_MASTER.KAMOKU_GROUP_BANGOU IS '�ȖڃO���[�v�ԍ�';
COMMENT ON COLUMN KAMOKU_MASTER.SHORI_GROUP IS '�����O���[�v';
COMMENT ON COLUMN KAMOKU_MASTER.TAIKAKINGAKU_NYUURYOKU_FLG IS '�Ή����z���̓t���O';
COMMENT ON COLUMN KAMOKU_MASTER.KAZEI_KBN IS '�ېŋ敪';
COMMENT ON COLUMN KAMOKU_MASTER.BUNRI_KBN IS '�����敪';
COMMENT ON COLUMN KAMOKU_MASTER.SHIIRE_KBN IS '�d���敪';
COMMENT ON COLUMN KAMOKU_MASTER.GYOUSHA_KBN IS '�Ǝ�敪';
COMMENT ON COLUMN KAMOKU_MASTER.ZEIRITSU_KBN IS '�ŗ��敪';
COMMENT ON COLUMN KAMOKU_MASTER.TOKUSHA_HYOUJI_KBN IS '����\���敪';
COMMENT ON COLUMN KAMOKU_MASTER.EDABAN_MINYUURYOKU_CHECK IS '�}�Ԗ����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.TORIHIKISAKI_MINYUURYOKU_CHECK IS '����斢���̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.BUMON_MINYUURYOKU_CHECK IS '���喢���̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.BUMON_EDABAN_FLG IS '����Ȗڎ}�Ԏg�p�t���O';
COMMENT ON COLUMN KAMOKU_MASTER.SEGMENT_MINYUURYOKU_CHECK IS '�Z�O�����g�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.PROJECT_MINYUURYOKU_CHECK IS '�v���W�F�N�g�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.UF1_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�P�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.UF2_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�Q�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.UF3_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�R�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.UF4_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�S�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.UF5_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�T�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.UF6_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�U�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.UF7_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�V�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.UF8_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�W�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.UF9_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�X�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.UF10_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�P�O�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.UF_KOTEI1_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�P�����̓`�F�b�N(�Œ�l)';
COMMENT ON COLUMN KAMOKU_MASTER.UF_KOTEI2_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�Q�����̓`�F�b�N(�Œ�l)';
COMMENT ON COLUMN KAMOKU_MASTER.UF_KOTEI3_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�R�����̓`�F�b�N(�Œ�l)';
COMMENT ON COLUMN KAMOKU_MASTER.UF_KOTEI4_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�S�����̓`�F�b�N(�Œ�l)';
COMMENT ON COLUMN KAMOKU_MASTER.UF_KOTEI5_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�T�����̓`�F�b�N(�Œ�l)';
COMMENT ON COLUMN KAMOKU_MASTER.UF_KOTEI6_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�U�����̓`�F�b�N(�Œ�l)';
COMMENT ON COLUMN KAMOKU_MASTER.UF_KOTEI7_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�V�����̓`�F�b�N(�Œ�l)';
COMMENT ON COLUMN KAMOKU_MASTER.UF_KOTEI8_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�W�����̓`�F�b�N(�Œ�l)';
COMMENT ON COLUMN KAMOKU_MASTER.UF_KOTEI9_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�X�����̓`�F�b�N(�Œ�l)';
COMMENT ON COLUMN KAMOKU_MASTER.UF_KOTEI10_MINYUURYOKU_CHECK IS '���j�o�[�T���t�B�[���h�P�O�����̓`�F�b�N(�Œ�l)';
COMMENT ON COLUMN KAMOKU_MASTER.KOUJI_MINYUURYOKU_CHECK IS '�H�������̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.KOUSHA_MINYUURYOKU_CHECK IS '�H�햢���̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.TEKIYOU_CD_MINYUURYOKU_CHECK IS '�E�v�R�[�h�����̓`�F�b�N';
COMMENT ON COLUMN KAMOKU_MASTER.BUMON_TORHIKISAKI_KAMOKU_FLG IS '��������Ȗڎg�p�t���O';
COMMENT ON COLUMN KAMOKU_MASTER.BUMON_TORIHIKISAKI_EDABAN_SHIYOU_FLG IS '��������Ȗڎ}�Ԏg�p�t���O';
COMMENT ON COLUMN KAMOKU_MASTER.TORIHIKISAKI_KAMOKU_EDABAN_FLG IS '�����Ȗڎ}�Ԏg�p�t���O';
COMMENT ON COLUMN KAMOKU_MASTER.SEGMENT_TORIHIKISAKI_KAMOKU_FLG IS '�Z�O�����g�����Ȗڎg�p�t���O';
COMMENT ON COLUMN KAMOKU_MASTER.KARIKANJOU_KESHIKOMI_NO_FLG IS '���������No�g�p�t���O';
INSERT 
INTO kamoku_master( 
  kamoku_gaibu_cd
  , kamoku_naibu_cd
  , kamoku_name_ryakushiki
  , kamoku_name_seishiki
  , kessanki_bangou
  , chouhyou_shaturyoku_no
  , taishaku_zokusei
  , kamoku_group_kbn
  , kamoku_group_bangou
  , shori_group
  , taikakingaku_nyuuryoku_flg
  , kazei_kbn
  , bunri_kbn
  , shiire_kbn
  , gyousha_kbn
  , zeiritsu_kbn
  , tokusha_hyouji_kbn
  , edaban_minyuuryoku_check
  , torihikisaki_minyuuryoku_check
  , bumon_minyuuryoku_check
  , bumon_edaban_flg
  , segment_minyuuryoku_check
  , project_minyuuryoku_check
  , uf1_minyuuryoku_check
  , uf2_minyuuryoku_check
  , uf3_minyuuryoku_check
  , uf4_minyuuryoku_check
  , uf5_minyuuryoku_check
  , uf6_minyuuryoku_check
  , uf7_minyuuryoku_check
  , uf8_minyuuryoku_check
  , uf9_minyuuryoku_check
  , uf10_minyuuryoku_check
  , uf_kotei1_minyuuryoku_check
  , uf_kotei2_minyuuryoku_check
  , uf_kotei3_minyuuryoku_check
  , uf_kotei4_minyuuryoku_check
  , uf_kotei5_minyuuryoku_check
  , uf_kotei6_minyuuryoku_check
  , uf_kotei7_minyuuryoku_check
  , uf_kotei8_minyuuryoku_check
  , uf_kotei9_minyuuryoku_check
  , uf_kotei10_minyuuryoku_check
  , kouji_minyuuryoku_check
  , kousha_minyuuryoku_check
  , tekiyou_cd_minyuuryoku_check
  , bumon_torhikisaki_kamoku_flg
  , bumon_torihikisaki_edaban_shiyou_flg
  , torihikisaki_kamoku_edaban_flg
  , segment_torihikisaki_kamoku_flg
  , karikanjou_keshikomi_no_flg
) 
SELECT
    kamoku_gaibu_cd
  , kamoku_naibu_cd
  , kamoku_name_ryakushiki
  , kamoku_name_seishiki
  , kessanki_bangou
  , chouhyou_shaturyoku_no
  , taishaku_zokusei
  , kamoku_group_kbn
  , kamoku_group_bangou
  , shori_group
  , taikakingaku_nyuuryoku_flg
  , kazei_kbn
  , bunri_kbn
  , shiire_kbn
  , gyousha_kbn
  , zeiritsu_kbn
  , tokusha_hyouji_kbn
  , edaban_minyuuryoku_check
  , torihikisaki_minyuuryoku_check
  , bumon_minyuuryoku_check
  , bumon_edaban_flg
  , segment_minyuuryoku_check
  , project_minyuuryoku_check
  , uf1_minyuuryoku_check
  , uf2_minyuuryoku_check
  , uf3_minyuuryoku_check
  , 0 --   , uf4_minyuuryoku_check
  , 0 --   , uf5_minyuuryoku_check
  , 0 --   , uf6_minyuuryoku_check
  , 0 --   , uf7_minyuuryoku_check
  , 0 --   , uf8_minyuuryoku_check
  , 0 --   , uf9_minyuuryoku_check
  , 0 --   , uf10_minyuuryoku_check
  , 0 --   , uf_kotei1_minyuuryoku_check
  , 0 --   , uf_kotei2_minyuuryoku_check
  , 0 --   , uf_kotei3_minyuuryoku_check
  , 0 --   , uf_kotei4_minyuuryoku_check
  , 0 --   , uf_kotei5_minyuuryoku_check
  , 0 --   , uf_kotei6_minyuuryoku_check
  , 0 --   , uf_kotei7_minyuuryoku_check
  , 0 --   , uf_kotei8_minyuuryoku_check
  , 0 --   , uf_kotei9_minyuuryoku_check
  , 0 --   , uf_kotei10_minyuuryoku_check
  , kouji_minyuuryoku_check
  , kousha_minyuuryoku_check
  , tekiyou_cd_minyuuryoku_check
  , bumon_torhikisaki_kamoku_flg
  , bumon_torihikisaki_edaban_shiyou_flg
  , torihikisaki_kamoku_edaban_flg
  , segment_torihikisaki_kamoku_flg
  , karikanjou_keshikomi_no_flg 
FROM
  kamoku_master_old;
DROP TABLE kamoku_master_old;

-- �Z�O�����g�}�X�^�[�쐬
CREATE TABLE SEGMENT_MASTER
(
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SEGMENT_NAME_SEISHIKI VARCHAR(60) NOT NULL,
	PRIMARY KEY (SEGMENT_CD)
) WITHOUT OIDS;
COMMENT ON TABLE SEGMENT_MASTER IS '�Z�O�����g�}�X�^�[';
COMMENT ON COLUMN SEGMENT_MASTER.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN SEGMENT_MASTER.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN SEGMENT_MASTER.SEGMENT_NAME_SEISHIKI IS '�Z�O�����g���i�����j';

CREATE TABLE SEGMENT_KAMOKU_ZANDAKA
(
	SEGMENT_CD VARCHAR(8) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TORIHIKISAKI_NAME_SEISHIKI VARCHAR(60) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22) NOT NULL,
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (SEGMENT_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE SEGMENT_KAMOKU_ZANDAKA IS '�Z�O�����g�Ȗڎc��';
COMMENT ON COLUMN SEGMENT_KAMOKU_ZANDAKA.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN SEGMENT_KAMOKU_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN SEGMENT_KAMOKU_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN SEGMENT_KAMOKU_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN SEGMENT_KAMOKU_ZANDAKA.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN SEGMENT_KAMOKU_ZANDAKA.TORIHIKISAKI_NAME_SEISHIKI IS '����於�i�����j';
COMMENT ON COLUMN SEGMENT_KAMOKU_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN SEGMENT_KAMOKU_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN SEGMENT_KAMOKU_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN SEGMENT_KAMOKU_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN SEGMENT_KAMOKU_ZANDAKA.KISHU_ZANDAKA IS '����c��';


-- �w�b�_�[�t�B�[���h�ꗗ�S�`�P�O�̒ǉ�
CREATE TABLE HF4_ICHIRAN
(
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (HF4_CD)
) WITHOUT OIDS;
COMMENT ON TABLE HF4_ICHIRAN IS '�w�b�_�t�B�[���h�S�ꗗ';
COMMENT ON COLUMN HF4_ICHIRAN.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN HF4_ICHIRAN.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN HF4_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE HF5_ICHIRAN
(
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (HF5_CD)
) WITHOUT OIDS;
COMMENT ON TABLE HF5_ICHIRAN IS '�w�b�_�t�B�[���h�T�ꗗ';
COMMENT ON COLUMN HF5_ICHIRAN.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN HF5_ICHIRAN.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN HF5_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE HF6_ICHIRAN
(
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (HF6_CD)
) WITHOUT OIDS;
COMMENT ON TABLE HF6_ICHIRAN IS '�w�b�_�t�B�[���h�U�ꗗ';
COMMENT ON COLUMN HF6_ICHIRAN.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN HF6_ICHIRAN.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN HF6_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE HF7_ICHIRAN
(
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (HF7_CD)
) WITHOUT OIDS;
COMMENT ON TABLE HF7_ICHIRAN IS '�w�b�_�t�B�[���h�V�ꗗ';
COMMENT ON COLUMN HF7_ICHIRAN.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN HF7_ICHIRAN.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN HF7_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE HF8_ICHIRAN
(
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (HF8_CD)
) WITHOUT OIDS;
COMMENT ON TABLE HF8_ICHIRAN IS '�w�b�_�t�B�[���h�W�ꗗ';
COMMENT ON COLUMN HF8_ICHIRAN.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN HF8_ICHIRAN.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN HF8_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE HF9_ICHIRAN
(
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (HF9_CD)
) WITHOUT OIDS;
COMMENT ON TABLE HF9_ICHIRAN IS '�w�b�_�t�B�[���h�X�ꗗ';
COMMENT ON COLUMN HF9_ICHIRAN.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN HF9_ICHIRAN.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN HF9_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE HF10_ICHIRAN
(
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (HF10_CD)
) WITHOUT OIDS;
COMMENT ON TABLE HF10_ICHIRAN IS '�w�b�_�t�B�[���h�P�O�ꗗ';
COMMENT ON COLUMN HF10_ICHIRAN.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN HF10_ICHIRAN.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN HF10_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';


-- ���j�o�[�T���t�B�[���h�ꗗ�y�юc���S�`�P�O�̒ǉ�
CREATE TABLE UF4_ICHIRAN
(
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF4_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF4_ICHIRAN IS '���j�o�[�T���t�B�[���h�S�ꗗ';
COMMENT ON COLUMN UF4_ICHIRAN.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN UF4_ICHIRAN.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN UF4_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF4_ZANDAKA
(
	UF4_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF4_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF4_ZANDAKA IS '���j�o�[�T���t�B�[���h�S�c��';
COMMENT ON COLUMN UF4_ZANDAKA.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN UF4_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF4_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF4_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF4_ZANDAKA.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN UF4_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF4_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF4_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF4_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF4_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF5_ICHIRAN
(
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF5_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF5_ICHIRAN IS '���j�o�[�T���t�B�[���h�T�ꗗ';
COMMENT ON COLUMN UF5_ICHIRAN.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN UF5_ICHIRAN.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN UF5_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF5_ZANDAKA
(
	UF5_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF5_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF5_ZANDAKA IS '���j�o�[�T���t�B�[���h�T�c��';
COMMENT ON COLUMN UF5_ZANDAKA.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN UF5_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF5_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF5_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF5_ZANDAKA.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN UF5_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF5_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF5_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF5_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF5_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF6_ICHIRAN
(
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF6_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF6_ICHIRAN IS '���j�o�[�T���t�B�[���h�U�ꗗ';
COMMENT ON COLUMN UF6_ICHIRAN.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN UF6_ICHIRAN.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN UF6_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF6_ZANDAKA
(
	UF6_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF6_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF6_ZANDAKA IS '���j�o�[�T���t�B�[���h�U�c��';
COMMENT ON COLUMN UF6_ZANDAKA.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN UF6_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF6_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF6_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF6_ZANDAKA.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN UF6_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF6_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF6_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF6_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF6_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF7_ICHIRAN
(
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF7_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF7_ICHIRAN IS '���j�o�[�T���t�B�[���h�V�ꗗ';
COMMENT ON COLUMN UF7_ICHIRAN.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN UF7_ICHIRAN.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN UF7_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF7_ZANDAKA
(
	UF7_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF7_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF7_ZANDAKA IS '���j�o�[�T���t�B�[���h�V�c��';
COMMENT ON COLUMN UF7_ZANDAKA.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN UF7_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF7_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF7_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF7_ZANDAKA.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN UF7_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF7_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF7_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF7_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF7_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF8_ICHIRAN
(
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF8_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF8_ICHIRAN IS '���j�o�[�T���t�B�[���h�W�ꗗ';
COMMENT ON COLUMN UF8_ICHIRAN.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN UF8_ICHIRAN.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN UF8_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF8_ZANDAKA
(
	UF8_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF8_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF8_ZANDAKA IS '���j�o�[�T���t�B�[���h�W�c��';
COMMENT ON COLUMN UF8_ZANDAKA.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN UF8_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF8_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF8_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF8_ZANDAKA.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN UF8_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF8_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF8_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF8_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF8_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF9_ICHIRAN
(
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF9_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF9_ICHIRAN IS '���j�o�[�T���t�B�[���h�X�ꗗ';
COMMENT ON COLUMN UF9_ICHIRAN.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN UF9_ICHIRAN.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN UF9_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF9_ZANDAKA
(
	UF9_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF9_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF9_ZANDAKA IS '���j�o�[�T���t�B�[���h�X�c��';
COMMENT ON COLUMN UF9_ZANDAKA.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN UF9_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF9_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF9_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF9_ZANDAKA.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN UF9_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF9_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF9_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF9_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF9_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF10_ICHIRAN
(
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF10_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF10_ICHIRAN IS '���j�o�[�T���t�B�[���h�P�O�ꗗ';
COMMENT ON COLUMN UF10_ICHIRAN.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN UF10_ICHIRAN.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN UF10_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';


CREATE TABLE UF10_ZANDAKA
(
	UF10_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF10_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF10_ZANDAKA IS '���j�o�[�T���t�B�[���h�P�O�c��';
COMMENT ON COLUMN UF10_ZANDAKA.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN UF10_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF10_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF10_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF10_ZANDAKA.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN UF10_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF10_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF10_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF10_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF10_ZANDAKA.KISHU_ZANDAKA IS '����c��';


-- ���j�o�[�T���t�B�[���h�i�Œ�l�j�ꗗ�y�юc���P�`�P�O�̒ǉ�
CREATE TABLE UF_KOTEI1_ICHIRAN
(
	UF_KOTEI1_CD VARCHAR(20) NOT NULL,
	UF_KOTEI1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF_KOTEI1_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI1_ICHIRAN IS '���j�o�[�T���t�B�[���h�P�ꗗ�i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI1_ICHIRAN.UF_KOTEI1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN UF_KOTEI1_ICHIRAN.UF_KOTEI1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN UF_KOTEI1_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF_KOTEI1_ZANDAKA
(
	UF_KOTEI1_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF_KOTEI1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22) NOT NULL,
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF_KOTEI1_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI1_ZANDAKA IS '���j�o�[�T���t�B�[���h�P�c���i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI1_ZANDAKA.UF_KOTEI1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN UF_KOTEI1_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF_KOTEI1_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF_KOTEI1_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF_KOTEI1_ZANDAKA.UF_KOTEI1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN UF_KOTEI1_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF_KOTEI1_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI1_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI1_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF_KOTEI1_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF_KOTEI2_ICHIRAN
(
	UF_KOTEI2_CD VARCHAR(20) NOT NULL,
	UF_KOTEI2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF_KOTEI2_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI2_ICHIRAN IS '���j�o�[�T���t�B�[���h�Q�ꗗ�i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI2_ICHIRAN.UF_KOTEI2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN UF_KOTEI2_ICHIRAN.UF_KOTEI2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN UF_KOTEI2_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF_KOTEI2_ZANDAKA
(
	UF_KOTEI2_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF_KOTEI2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22) NOT NULL,
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF_KOTEI2_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI2_ZANDAKA IS '���j�o�[�T���t�B�[���h�Q�c���i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI2_ZANDAKA.UF_KOTEI2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN UF_KOTEI2_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF_KOTEI2_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF_KOTEI2_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF_KOTEI2_ZANDAKA.UF_KOTEI2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN UF_KOTEI2_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF_KOTEI2_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI2_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI2_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF_KOTEI2_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF_KOTEI3_ICHIRAN
(
	UF_KOTEI3_CD VARCHAR(20) NOT NULL,
	UF_KOTEI3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF_KOTEI3_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI3_ICHIRAN IS '���j�o�[�T���t�B�[���h�R�ꗗ�i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI3_ICHIRAN.UF_KOTEI3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN UF_KOTEI3_ICHIRAN.UF_KOTEI3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN UF_KOTEI3_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF_KOTEI3_ZANDAKA
(
	UF_KOTEI3_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF_KOTEI3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22) NOT NULL,
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF_KOTEI3_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI3_ZANDAKA IS '���j�o�[�T���t�B�[���h�R�c���i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI3_ZANDAKA.UF_KOTEI3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN UF_KOTEI3_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF_KOTEI3_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF_KOTEI3_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF_KOTEI3_ZANDAKA.UF_KOTEI3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN UF_KOTEI3_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF_KOTEI3_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI3_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI3_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF_KOTEI3_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF_KOTEI4_ICHIRAN
(
	UF_KOTEI4_CD VARCHAR(20) NOT NULL,
	UF_KOTEI4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF_KOTEI4_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI4_ICHIRAN IS '���j�o�[�T���t�B�[���h�S�ꗗ�i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI4_ICHIRAN.UF_KOTEI4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN UF_KOTEI4_ICHIRAN.UF_KOTEI4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN UF_KOTEI4_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF_KOTEI4_ZANDAKA
(
	UF_KOTEI4_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF_KOTEI4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF_KOTEI4_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI4_ZANDAKA IS '���j�o�[�T���t�B�[���h�S�c���i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI4_ZANDAKA.UF_KOTEI4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN UF_KOTEI4_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF_KOTEI4_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF_KOTEI4_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF_KOTEI4_ZANDAKA.UF_KOTEI4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN UF_KOTEI4_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF_KOTEI4_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI4_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI4_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF_KOTEI4_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF_KOTEI5_ICHIRAN
(
	UF_KOTEI5_CD VARCHAR(20) NOT NULL,
	UF_KOTEI5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF_KOTEI5_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI5_ICHIRAN IS '���j�o�[�T���t�B�[���h�T�ꗗ�i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI5_ICHIRAN.UF_KOTEI5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN UF_KOTEI5_ICHIRAN.UF_KOTEI5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN UF_KOTEI5_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF_KOTEI5_ZANDAKA
(
	UF_KOTEI5_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF_KOTEI5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF_KOTEI5_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI5_ZANDAKA IS '���j�o�[�T���t�B�[���h�T�c���i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI5_ZANDAKA.UF_KOTEI5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN UF_KOTEI5_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF_KOTEI5_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF_KOTEI5_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF_KOTEI5_ZANDAKA.UF_KOTEI5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN UF_KOTEI5_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF_KOTEI5_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI5_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI5_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF_KOTEI5_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF_KOTEI6_ICHIRAN
(
	UF_KOTEI6_CD VARCHAR(20) NOT NULL,
	UF_KOTEI6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF_KOTEI6_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI6_ICHIRAN IS '���j�o�[�T���t�B�[���h�U�ꗗ�i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI6_ICHIRAN.UF_KOTEI6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN UF_KOTEI6_ICHIRAN.UF_KOTEI6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN UF_KOTEI6_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF_KOTEI6_ZANDAKA
(
	UF_KOTEI6_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF_KOTEI6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF_KOTEI6_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI6_ZANDAKA IS '���j�o�[�T���t�B�[���h�U�c���i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI6_ZANDAKA.UF_KOTEI6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN UF_KOTEI6_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF_KOTEI6_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF_KOTEI6_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF_KOTEI6_ZANDAKA.UF_KOTEI6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN UF_KOTEI6_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF_KOTEI6_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI6_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI6_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF_KOTEI6_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF_KOTEI7_ICHIRAN
(
	UF_KOTEI7_CD VARCHAR(20) NOT NULL,
	UF_KOTEI7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF_KOTEI7_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI7_ICHIRAN IS '���j�o�[�T���t�B�[���h�V�ꗗ�i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI7_ICHIRAN.UF_KOTEI7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN UF_KOTEI7_ICHIRAN.UF_KOTEI7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN UF_KOTEI7_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF_KOTEI7_ZANDAKA
(
	UF_KOTEI7_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF_KOTEI7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF_KOTEI7_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI7_ZANDAKA IS '���j�o�[�T���t�B�[���h�V�c���i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI7_ZANDAKA.UF_KOTEI7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN UF_KOTEI7_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF_KOTEI7_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF_KOTEI7_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF_KOTEI7_ZANDAKA.UF_KOTEI7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN UF_KOTEI7_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF_KOTEI7_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI7_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI7_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF_KOTEI7_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF_KOTEI8_ICHIRAN
(
	UF_KOTEI8_CD VARCHAR(20) NOT NULL,
	UF_KOTEI8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF_KOTEI8_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI8_ICHIRAN IS '���j�o�[�T���t�B�[���h�W�ꗗ�i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI8_ICHIRAN.UF_KOTEI8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN UF_KOTEI8_ICHIRAN.UF_KOTEI8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN UF_KOTEI8_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF_KOTEI8_ZANDAKA
(
	UF_KOTEI8_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF_KOTEI8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF_KOTEI8_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI8_ZANDAKA IS '���j�o�[�T���t�B�[���h�W�c���i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI8_ZANDAKA.UF_KOTEI8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN UF_KOTEI8_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF_KOTEI8_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF_KOTEI8_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF_KOTEI8_ZANDAKA.UF_KOTEI8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN UF_KOTEI8_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF_KOTEI8_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI8_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI8_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF_KOTEI8_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF_KOTEI9_ICHIRAN
(
	UF_KOTEI9_CD VARCHAR(20) NOT NULL,
	UF_KOTEI9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF_KOTEI9_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI9_ICHIRAN IS '���j�o�[�T���t�B�[���h�X�ꗗ�i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI9_ICHIRAN.UF_KOTEI9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN UF_KOTEI9_ICHIRAN.UF_KOTEI9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN UF_KOTEI9_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF_KOTEI9_ZANDAKA
(
	UF_KOTEI9_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF_KOTEI9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF_KOTEI9_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI9_ZANDAKA IS '���j�o�[�T���t�B�[���h�X�c���i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI9_ZANDAKA.UF_KOTEI9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN UF_KOTEI9_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF_KOTEI9_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF_KOTEI9_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF_KOTEI9_ZANDAKA.UF_KOTEI9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN UF_KOTEI9_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF_KOTEI9_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI9_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI9_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF_KOTEI9_ZANDAKA.KISHU_ZANDAKA IS '����c��';

CREATE TABLE UF_KOTEI10_ICHIRAN
(
	UF_KOTEI10_CD VARCHAR(20) NOT NULL,
	UF_KOTEI10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	PRIMARY KEY (UF_KOTEI10_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI10_ICHIRAN IS '���j�o�[�T���t�B�[���h�P�O�ꗗ�i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI10_ICHIRAN.UF_KOTEI10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN UF_KOTEI10_ICHIRAN.UF_KOTEI10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN UF_KOTEI10_ICHIRAN.KESSANKI_BANGOU IS '���Z���ԍ�';

CREATE TABLE UF_KOTEI10_ZANDAKA
(
	UF_KOTEI10_CD VARCHAR(20) NOT NULL,
	KAMOKU_GAIBU_CD VARCHAR(8) NOT NULL,
	KESSANKI_BANGOU SMALLINT,
	KAMOKU_NAIBU_CD VARCHAR(15) NOT NULL,
	UF_KOTEI10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	CHOUHYOU_SHATURYOKU_NO SMALLINT,
	KAMOKU_NAME_RYAKUSHIKI VARCHAR(22),
	KAMOKU_NAME_SEISHIKI VARCHAR(40) NOT NULL,
	TAISHAKU_ZOKUSEI SMALLINT NOT NULL,
	KISHU_ZANDAKA DECIMAL(19,0) NOT NULL,
	PRIMARY KEY (UF_KOTEI10_CD, KAMOKU_GAIBU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE UF_KOTEI10_ZANDAKA IS '���j�o�[�T���t�B�[���h�P�O�c���i�Œ�l�j';
COMMENT ON COLUMN UF_KOTEI10_ZANDAKA.UF_KOTEI10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN UF_KOTEI10_ZANDAKA.KAMOKU_GAIBU_CD IS '�ȖڊO���R�[�h';
COMMENT ON COLUMN UF_KOTEI10_ZANDAKA.KESSANKI_BANGOU IS '���Z���ԍ�';
COMMENT ON COLUMN UF_KOTEI10_ZANDAKA.KAMOKU_NAIBU_CD IS '�Ȗړ����R�[�h';
COMMENT ON COLUMN UF_KOTEI10_ZANDAKA.UF_KOTEI10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN UF_KOTEI10_ZANDAKA.CHOUHYOU_SHATURYOKU_NO IS '���[�o�͏��ԍ�';
COMMENT ON COLUMN UF_KOTEI10_ZANDAKA.KAMOKU_NAME_RYAKUSHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI10_ZANDAKA.KAMOKU_NAME_SEISHIKI IS '�Ȗږ��i�����j';
COMMENT ON COLUMN UF_KOTEI10_ZANDAKA.TAISHAKU_ZOKUSEI IS '�ݎؑ���';
COMMENT ON COLUMN UF_KOTEI10_ZANDAKA.KISHU_ZANDAKA IS '����c��';

-- �o��֐��Z
ALTER TABLE KEIHISEISAN RENAME TO KEIHISEISAN_OLD;
CREATE TABLE KEIHISEISAN
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	KARIBARAI_DENPYOU_ID VARCHAR(19) NOT NULL,
	KARIBARAI_ON VARCHAR(1),
	KARIBARAI_MISHIYOU_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
	DAIRIFLG VARCHAR(1) DEFAULT '0' NOT NULL,
	KEIJOUBI DATE,
	SHIHARAIBI DATE,
	SHIHARAIKIBOUBI DATE,
	SHIHARAIHOUHOU VARCHAR(1) NOT NULL,
	HONTAI_KINGAKU_GOUKEI DECIMAL(15) NOT NULL,
	SHOUHIZEIGAKU_GOUKEI DECIMAL(15) NOT NULL,
	SHIHARAI_KINGAKU_GOUKEI DECIMAL(15) NOT NULL,
	HOUJIN_CARD_RIYOU_KINGAKU DECIMAL(15) NOT NULL,
	KAISHA_TEHAI_KINGAKU DECIMAL(15) NOT NULL,
	SASHIHIKI_SHIKYUU_KINGAKU DECIMAL(15) NOT NULL,
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HOSOKU VARCHAR(240) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE KEIHISEISAN IS '�o��Z';
COMMENT ON COLUMN KEIHISEISAN.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN KEIHISEISAN.KARIBARAI_DENPYOU_ID IS '�����`�[ID';
COMMENT ON COLUMN KEIHISEISAN.KARIBARAI_ON IS '�����\���t���O';
COMMENT ON COLUMN KEIHISEISAN.KARIBARAI_MISHIYOU_FLG IS '���������g�p�t���O';
COMMENT ON COLUMN KEIHISEISAN.DAIRIFLG IS '�㗝�t���O';
COMMENT ON COLUMN KEIHISEISAN.KEIJOUBI IS '�v���';
COMMENT ON COLUMN KEIHISEISAN.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN KEIHISEISAN.SHIHARAIKIBOUBI IS '�x����]��';
COMMENT ON COLUMN KEIHISEISAN.SHIHARAIHOUHOU IS '�x�����@';
COMMENT ON COLUMN KEIHISEISAN.HONTAI_KINGAKU_GOUKEI IS '�{�̋��z���v';
COMMENT ON COLUMN KEIHISEISAN.SHOUHIZEIGAKU_GOUKEI IS '����Ŋz���v';
COMMENT ON COLUMN KEIHISEISAN.SHIHARAI_KINGAKU_GOUKEI IS '�x�����z���v';
COMMENT ON COLUMN KEIHISEISAN.HOUJIN_CARD_RIYOU_KINGAKU IS '���@�l�J�[�h���p���v';
COMMENT ON COLUMN KEIHISEISAN.KAISHA_TEHAI_KINGAKU IS '��Ў�z���v';
COMMENT ON COLUMN KEIHISEISAN.SASHIHIKI_SHIKYUU_KINGAKU IS '�����x�����z';
COMMENT ON COLUMN KEIHISEISAN.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN KEIHISEISAN.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN KEIHISEISAN.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN KEIHISEISAN.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN KEIHISEISAN.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN KEIHISEISAN.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN KEIHISEISAN.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN KEIHISEISAN.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN KEIHISEISAN.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN KEIHISEISAN.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN KEIHISEISAN.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN KEIHISEISAN.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN KEIHISEISAN.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN KEIHISEISAN.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN KEIHISEISAN.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN KEIHISEISAN.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN KEIHISEISAN.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN KEIHISEISAN.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN KEIHISEISAN.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN KEIHISEISAN.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN KEIHISEISAN.HOSOKU IS '�⑫';
COMMENT ON COLUMN KEIHISEISAN.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN KEIHISEISAN.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN KEIHISEISAN.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN KEIHISEISAN.KOUSHIN_TIME IS '�X�V����';

INSERT 
INTO keihiseisan( 
  denpyou_id
  , karibarai_denpyou_id
  , karibarai_on
  , karibarai_mishiyou_flg
  , dairiflg
  , keijoubi
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , hontai_kingaku_goukei
  , shouhizeigaku_goukei
  , shiharai_kingaku_goukei
  , houjin_card_riyou_kingaku
  , kaisha_tehai_kingaku
  , sashihiki_shikyuu_kingaku
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , hosoku
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , karibarai_denpyou_id
  , karibarai_on
  , karibarai_mishiyou_flg
  , dairiflg
  , keijoubi
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , hontai_kingaku_goukei
  , shouhizeigaku_goukei
  , shiharai_kingaku_goukei
  , houjin_card_riyou_kingaku
  , kaisha_tehai_kingaku
  , sashihiki_shikyuu_kingaku
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , hosoku
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  keihiseisan_old;
DROP TABLE keihiseisan_old;

-- �o��֖���
ALTER TABLE KEIHISEISAN_MEISAI RENAME TO KEIHISEISAN_MEISAI_OLD;
CREATE TABLE KEIHISEISAN_MEISAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	DENPYOU_EDANO INT NOT NULL,
	SHIWAKE_EDANO INT NOT NULL,
	USER_ID VARCHAR(30) NOT NULL,
	SHAIN_NO VARCHAR(15) NOT NULL,
	USER_SEI VARCHAR(10) NOT NULL,
	USER_MEI VARCHAR(10) NOT NULL,
	SHIYOUBI DATE,
	SHOUHYOU_SHORUI_FLG VARCHAR(1),
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	SHIHARAI_KINGAKU DECIMAL(15) NOT NULL,
	HONTAI_KINGAKU DECIMAL(15),
	SHOUHIZEIGAKU DECIMAL(15),
	HOUJIN_CARD_RIYOU_FLG VARCHAR(1) NOT NULL,
	KAISHA_TEHAI_FLG VARCHAR(1) NOT NULL,
	KOUSAIHI_SHOUSAI_HYOUJI_FLG VARCHAR(1) NOT NULL,
	KOUSAIHI_SHOUSAI VARCHAR(240),
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	HIMODUKE_CARD_MEISAI BIGINT,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID, DENPYOU_EDANO)
) WITHOUT OIDS;
COMMENT ON TABLE KEIHISEISAN_MEISAI IS '�o��Z����';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.DENPYOU_EDANO IS '�`�[�}�ԍ�';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.USER_ID IS '���[�U�[ID';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.SHAIN_NO IS '�Ј��ԍ�';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.USER_SEI IS '���[�U�[��';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.USER_MEI IS '���[�U�[��';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.SHIYOUBI IS '�g�p��';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.SHOUHYOU_SHORUI_FLG IS '�؜ߏ��ރt���O';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.SHIHARAI_KINGAKU IS '�x�����z';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.HONTAI_KINGAKU IS '�{�̋��z';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.SHOUHIZEIGAKU IS '����Ŋz';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.HOUJIN_CARD_RIYOU_FLG IS '�@�l�J�[�h���p�t���O';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KAISHA_TEHAI_FLG IS '��Ў�z�t���O';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KOUSAIHI_SHOUSAI_HYOUJI_FLG IS '���۔�ڍו\���t���O';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KOUSAIHI_SHOUSAI IS '���۔�ڍ�';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.HIMODUKE_CARD_MEISAI IS '�R�t���J�[�h����';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN KEIHISEISAN_MEISAI.KOUSHIN_TIME IS '�X�V����';

INSERT 
INTO keihiseisan_meisai( 
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , user_id
  , shain_no
  , user_sei
  , user_mei
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , denpyou_edano
  , shiwake_edano
  , user_id
  , shain_no
  , user_sei
  , user_mei
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , project_cd
  , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  keihiseisan_meisai_old;
DROP TABLE keihiseisan_meisai_old;

-- �����\��
ALTER TABLE KARIBARAI RENAME TO KARIBARAI_OLD;
CREATE TABLE KARIBARAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	SEISAN_YOTEIBI DATE,
	SEISAN_KANRYOUBI DATE,
	SHIHARAIBI DATE,
	KARIBARAI_ON VARCHAR(1),
	SHIHARAIKIBOUBI DATE,
	SHIHARAIHOUHOU VARCHAR(1) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	KINGAKU DECIMAL(15),
	KARIBARAI_KINGAKU DECIMAL(15),
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HOSOKU VARCHAR(240) NOT NULL,
	SHIWAKE_EDANO INT,
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE KARIBARAI IS '����';
COMMENT ON COLUMN KARIBARAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN KARIBARAI.SEISAN_YOTEIBI IS '���Z�\���';
COMMENT ON COLUMN KARIBARAI.SEISAN_KANRYOUBI IS '���Z������';
COMMENT ON COLUMN KARIBARAI.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN KARIBARAI.KARIBARAI_ON IS '�����\���t���O';
COMMENT ON COLUMN KARIBARAI.SHIHARAIKIBOUBI IS '�x����]��';
COMMENT ON COLUMN KARIBARAI.SHIHARAIHOUHOU IS '�x�����@';
COMMENT ON COLUMN KARIBARAI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN KARIBARAI.KINGAKU IS '���z';
COMMENT ON COLUMN KARIBARAI.KARIBARAI_KINGAKU IS '�������z';
COMMENT ON COLUMN KARIBARAI.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN KARIBARAI.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN KARIBARAI.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN KARIBARAI.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN KARIBARAI.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN KARIBARAI.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN KARIBARAI.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN KARIBARAI.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN KARIBARAI.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN KARIBARAI.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN KARIBARAI.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN KARIBARAI.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN KARIBARAI.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN KARIBARAI.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN KARIBARAI.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN KARIBARAI.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN KARIBARAI.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN KARIBARAI.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN KARIBARAI.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN KARIBARAI.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN KARIBARAI.HOSOKU IS '�⑫';
COMMENT ON COLUMN KARIBARAI.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN KARIBARAI.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN KARIBARAI.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN KARIBARAI.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN KARIBARAI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN KARIBARAI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN KARIBARAI.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN KARIBARAI.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN KARIBARAI.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KARIBARAI.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KARIBARAI.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN KARIBARAI.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN KARIBARAI.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN KARIBARAI.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN KARIBARAI.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN KARIBARAI.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KARIBARAI.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KARIBARAI.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN KARIBARAI.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN KARIBARAI.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN KARIBARAI.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN KARIBARAI.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN KARIBARAI.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN KARIBARAI.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN KARIBARAI.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN KARIBARAI.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN KARIBARAI.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN KARIBARAI.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN KARIBARAI.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN KARIBARAI.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN KARIBARAI.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN KARIBARAI.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN KARIBARAI.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN KARIBARAI.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN KARIBARAI.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN KARIBARAI.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN KARIBARAI.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN KARIBARAI.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN KARIBARAI.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN KARIBARAI.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN KARIBARAI.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN KARIBARAI.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN KARIBARAI.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN KARIBARAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN KARIBARAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN KARIBARAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN KARIBARAI.KOUSHIN_TIME IS '�X�V����';

INSERT 
INTO karibarai( 
  denpyou_id
  , seisan_yoteibi
  , seisan_kanryoubi
  , shiharaibi
  , karibarai_on
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , kingaku
  , karibarai_kingaku
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , seisan_yoteibi
  , seisan_kanryoubi
  , shiharaibi
  , karibarai_on
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , kingaku
  , karibarai_kingaku
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , '' --   , torihikisaki_cd
  , '' --   , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , '' --   , project_cd
  , '' --   , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  karibarai_old;
DROP TABLE karibarai_old;

-- �����������\��
ALTER TABLE SEIKYUUSHOBARAI RENAME TO SEIKYUUSHOBARAI_OLD;
CREATE TABLE SEIKYUUSHOBARAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	KEIJOUBI DATE,
	SHIHARAI_KIGEN DATE,
	SHIHARAIBI DATE,
	MASREF_FLG VARCHAR(1) NOT NULL,
	SHOUHYOU_SHORUI_FLG VARCHAR(1) NOT NULL,
	KAKE_FLG VARCHAR(1) NOT NULL,
	HONTAI_KINGAKU_GOUKEI DECIMAL(15) NOT NULL,
	SHOUHIZEIGAKU_GOUKEI DECIMAL(15) NOT NULL,
	SHIHARAI_KINGAKU_GOUKEI DECIMAL(15) NOT NULL,
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HOSOKU VARCHAR(240) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE SEIKYUUSHOBARAI IS '����������';
COMMENT ON COLUMN SEIKYUUSHOBARAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN SEIKYUUSHOBARAI.KEIJOUBI IS '�v���';
COMMENT ON COLUMN SEIKYUUSHOBARAI.SHIHARAI_KIGEN IS '�x������';
COMMENT ON COLUMN SEIKYUUSHOBARAI.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN SEIKYUUSHOBARAI.MASREF_FLG IS '�}�X�^�[�Q�ƃt���O';
COMMENT ON COLUMN SEIKYUUSHOBARAI.SHOUHYOU_SHORUI_FLG IS '�؜ߏ��ރt���O';
COMMENT ON COLUMN SEIKYUUSHOBARAI.KAKE_FLG IS '�|���t���O';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HONTAI_KINGAKU_GOUKEI IS '�{�̋��z���v';
COMMENT ON COLUMN SEIKYUUSHOBARAI.SHOUHIZEIGAKU_GOUKEI IS '����Ŋz���v';
COMMENT ON COLUMN SEIKYUUSHOBARAI.SHIHARAI_KINGAKU_GOUKEI IS '�x�����z���v';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI.HOSOKU IS '�⑫';
COMMENT ON COLUMN SEIKYUUSHOBARAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN SEIKYUUSHOBARAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN SEIKYUUSHOBARAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN SEIKYUUSHOBARAI.KOUSHIN_TIME IS '�X�V����';

INSERT 
INTO seikyuushobarai( 
  denpyou_id
  , keijoubi
  , shiharai_kigen
  , shiharaibi
  , masref_flg
  , shouhyou_shorui_flg
  , kake_flg
  , hontai_kingaku_goukei
  , shouhizeigaku_goukei
  , shiharai_kingaku_goukei
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , hosoku
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , keijoubi
  , shiharai_kigen
  , shiharaibi
  , masref_flg
  , shouhyou_shorui_flg
  , kake_flg
  , hontai_kingaku_goukei
  , shouhizeigaku_goukei
  , shiharai_kingaku_goukei
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , '' --   , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , hosoku
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  seikyuushobarai_old;
DROP TABLE seikyuushobarai_old;

-- ��������������
ALTER TABLE SEIKYUUSHOBARAI_MEISAI RENAME TO SEIKYUUSHOBARAI_MEISAI_OLD;
CREATE TABLE SEIKYUUSHOBARAI_MEISAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	DENPYOU_EDANO INT NOT NULL,
	SHIWAKE_EDANO INT NOT NULL,
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	SHIHARAI_KINGAKU DECIMAL(15) NOT NULL,
	HONTAI_KINGAKU DECIMAL(15),
	SHOUHIZEIGAKU DECIMAL(15),
	KOUSAIHI_SHOUSAI_HYOUJI_FLG VARCHAR(1) NOT NULL,
	KOUSAIHI_SHOUSAI VARCHAR(240),
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	FURIKOMISAKI_JOUHOU VARCHAR(240) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID, DENPYOU_EDANO)
) WITHOUT OIDS;
COMMENT ON TABLE SEIKYUUSHOBARAI_MEISAI IS '��������������';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.DENPYOU_EDANO IS '�`�[�}�ԍ�';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.SHIHARAI_KINGAKU IS '�x�����z';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.HONTAI_KINGAKU IS '�{�̋��z';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.SHOUHIZEIGAKU IS '����Ŋz';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KOUSAIHI_SHOUSAI_HYOUJI_FLG IS '���۔�ڍו\���t���O';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KOUSAIHI_SHOUSAI IS '���۔�ڍ�';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.FURIKOMISAKI_JOUHOU IS '�U������';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN SEIKYUUSHOBARAI_MEISAI.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO seikyuushobarai_meisai( 
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , torihiki_name
  , tekiyou
  , zeiritsu
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , furikomisaki_jouhou
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , denpyou_edano
  , shiwake_edano
  , torihiki_name
  , tekiyou
  , zeiritsu
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , furikomisaki_jouhou
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki-- 
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , project_cd
  , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  seikyuushobarai_meisai_old;
DROP TABLE seikyuushobarai_meisai_old;

-- ������\��
ALTER TABLE RYOHI_KARIBARAI RENAME TO RYOHI_KARIBARAI_OLD;
CREATE TABLE RYOHI_KARIBARAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	KARIBARAI_ON VARCHAR(1) DEFAULT '0' NOT NULL,
	DAIRIFLG VARCHAR(1) NOT NULL,
	USER_ID VARCHAR(30),
	SHAIN_NO VARCHAR(15) NOT NULL,
	USER_SEI VARCHAR(10) NOT NULL,
	USER_MEI VARCHAR(10) NOT NULL,
	HOUMONSAKI VARCHAR(120) NOT NULL,
	MOKUTEKI VARCHAR(240) NOT NULL,
	SEISANKIKAN_FROM DATE,
	SEISANKIKAN_FROM_HOUR VARCHAR(2),
	SEISANKIKAN_FROM_MIN VARCHAR(2),
	SEISANKIKAN_TO DATE,
	SEISANKIKAN_TO_HOUR VARCHAR(2),
	SEISANKIKAN_TO_MIN VARCHAR(2),
	SHIHARAIBI DATE,
	SHIHARAIKIBOUBI DATE,
	SHIHARAIHOUHOU VARCHAR(1) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	KINGAKU DECIMAL(15) NOT NULL,
	KARIBARAI_KINGAKU DECIMAL(15),
	SASHIHIKI_NUM NUMERIC(2),
	SASHIHIKI_TANKA DECIMAL(6),
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HOSOKU VARCHAR(240) NOT NULL,
	SHIWAKE_EDANO INT,
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	SEISAN_KANRYOUBI DATE,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE RYOHI_KARIBARAI IS '�����';
COMMENT ON COLUMN RYOHI_KARIBARAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN RYOHI_KARIBARAI.KARIBARAI_ON IS '�����\���t���O';
COMMENT ON COLUMN RYOHI_KARIBARAI.DAIRIFLG IS '�㗝�t���O';
COMMENT ON COLUMN RYOHI_KARIBARAI.USER_ID IS '���[�U�[ID';
COMMENT ON COLUMN RYOHI_KARIBARAI.SHAIN_NO IS '�Ј��ԍ�';
COMMENT ON COLUMN RYOHI_KARIBARAI.USER_SEI IS '���[�U�[��';
COMMENT ON COLUMN RYOHI_KARIBARAI.USER_MEI IS '���[�U�[��';
COMMENT ON COLUMN RYOHI_KARIBARAI.HOUMONSAKI IS '�K���';
COMMENT ON COLUMN RYOHI_KARIBARAI.MOKUTEKI IS '�ړI';
COMMENT ON COLUMN RYOHI_KARIBARAI.SEISANKIKAN_FROM IS '���Z���ԊJ�n��';
COMMENT ON COLUMN RYOHI_KARIBARAI.SEISANKIKAN_FROM_HOUR IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN RYOHI_KARIBARAI.SEISANKIKAN_FROM_MIN IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN RYOHI_KARIBARAI.SEISANKIKAN_TO IS '���Z���ԏI����';
COMMENT ON COLUMN RYOHI_KARIBARAI.SEISANKIKAN_TO_HOUR IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN RYOHI_KARIBARAI.SEISANKIKAN_TO_MIN IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN RYOHI_KARIBARAI.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN RYOHI_KARIBARAI.SHIHARAIKIBOUBI IS '�x����]��';
COMMENT ON COLUMN RYOHI_KARIBARAI.SHIHARAIHOUHOU IS '�x�����@';
COMMENT ON COLUMN RYOHI_KARIBARAI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN RYOHI_KARIBARAI.KINGAKU IS '���z';
COMMENT ON COLUMN RYOHI_KARIBARAI.KARIBARAI_KINGAKU IS '�������z';
COMMENT ON COLUMN RYOHI_KARIBARAI.SASHIHIKI_NUM IS '������';
COMMENT ON COLUMN RYOHI_KARIBARAI.SASHIHIKI_TANKA IS '�����P��';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.HOSOKU IS '�⑫';
COMMENT ON COLUMN RYOHI_KARIBARAI.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN RYOHI_KARIBARAI.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN RYOHI_KARIBARAI.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN RYOHI_KARIBARAI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN RYOHI_KARIBARAI.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN RYOHI_KARIBARAI.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN RYOHI_KARIBARAI.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN RYOHI_KARIBARAI.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN RYOHI_KARIBARAI.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN RYOHI_KARIBARAI.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN RYOHI_KARIBARAI.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN RYOHI_KARIBARAI.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI.SEISAN_KANRYOUBI IS '���Z������';
COMMENT ON COLUMN RYOHI_KARIBARAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN RYOHI_KARIBARAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN RYOHI_KARIBARAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN RYOHI_KARIBARAI.KOUSHIN_TIME IS '�X�V����';

INSERT 
INTO ryohi_karibarai( 
  denpyou_id
  , karibarai_on
  , dairiflg
  , user_id
  , shain_no
  , user_sei
  , user_mei
  , houmonsaki
  , mokuteki
  , seisankikan_from
  , seisankikan_from_hour
  , seisankikan_from_min
  , seisankikan_to
  , seisankikan_to_hour
  , seisankikan_to_min
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , kingaku
  , karibarai_kingaku
  , sashihiki_num
  , sashihiki_tanka
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , SEISAN_KANRYOUBI
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , karibarai_on
  , dairiflg
  , user_id
  , shain_no
  , user_sei
  , user_mei
  , houmonsaki
  , mokuteki
  , seisankikan_from
  , seisankikan_from_hour
  , seisankikan_from_min
  , seisankikan_to
  , seisankikan_to_hour
  , seisankikan_to_min
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , kingaku
  , karibarai_kingaku
  , sashihiki_num
  , sashihiki_tanka
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , '' --   , torihikisaki_cd
  , '' --   , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , '' --   , project_cd
  , '' --   , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , null --SEISAN_KANRYOUBI
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  ryohi_karibarai_old;
DROP TABLE ryohi_karibarai_old;

-- ������o���
ALTER TABLE RYOHI_KARIBARAI_KEIHI_MEISAI RENAME TO RYOHI_KARIBARAI_KEIHI_MEISAI_OLD;
CREATE TABLE RYOHI_KARIBARAI_KEIHI_MEISAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	DENPYOU_EDANO INT NOT NULL,
	SHIWAKE_EDANO INT NOT NULL,
	SHIYOUBI DATE,
	SHOUHYOU_SHORUI_FLG VARCHAR(1),
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	SHIHARAI_KINGAKU DECIMAL(15) NOT NULL,
	HONTAI_KINGAKU DECIMAL(15),
	SHOUHIZEIGAKU DECIMAL(15),
	KOUSAIHI_SHOUSAI_HYOUJI_FLG VARCHAR(1) NOT NULL,
	KOUSAIHI_SHOUSAI VARCHAR(240),
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID, DENPYOU_EDANO)
) WITHOUT OIDS;
COMMENT ON COLUMN RYOHI_KARIBARAI_KEIHI_MEISAI.KOUSHIN_TIME IS '�X�V����';
COMMENT ON TABLE RYOHI_KARIBARAI_MEISAI IS '���������';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.DENPYOU_EDANO IS '�`�[�}�ԍ�';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.KIKAN_FROM IS '���ԊJ�n��';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.KIKAN_TO IS '���ԏI����';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.KYUUJITSU_NISSUU IS '�x������';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.SHUBETSU_CD IS '��ʃR�[�h';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.SHUBETSU1 IS '��ʂP';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.SHUBETSU2 IS '��ʂQ';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.KOUTSUU_SHUDAN IS '��ʎ�i';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.SHOUHYOU_SHORUI_HISSU_FLG IS '�؜ߏ��ޕK�{�t���O';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.NAIYOU IS '���e�i����Z�j';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.BIKOU IS '���l�i����Z�j';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.OUFUKU_FLG IS '�����t���O';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.JIDOUNYUURYOKU_FLG IS '�������̓t���O';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.NISSUU IS '����';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.TANKA IS '�P��';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.MEISAI_KINGAKU IS '���׋��z';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN RYOHI_KARIBARAI_MEISAI.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO ryohi_karibarai_keihi_meisai( 
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , denpyou_edano
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , project_cd
  , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  ryohi_karibarai_keihi_meisai_old;
DROP TABLE ryohi_karibarai_keihi_meisai_old;

-- ����Z
ALTER TABLE RYOHISEISAN RENAME TO RYOHISEISAN_OLD;
CREATE TABLE RYOHISEISAN
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	KARIBARAI_DENPYOU_ID VARCHAR(19) NOT NULL,
	KARIBARAI_ON VARCHAR(1) NOT NULL,
	KARIBARAI_MISHIYOU_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
	SHUCCHOU_CHUUSHI_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
	DAIRIFLG VARCHAR(1) NOT NULL,
	USER_ID VARCHAR(30) NOT NULL,
	SHAIN_NO VARCHAR(15) NOT NULL,
	USER_SEI VARCHAR(10) NOT NULL,
	USER_MEI VARCHAR(10) NOT NULL,
	HOUMONSAKI VARCHAR(120) NOT NULL,
	MOKUTEKI VARCHAR(240) NOT NULL,
	SEISANKIKAN_FROM DATE,
	SEISANKIKAN_FROM_HOUR VARCHAR(2),
	SEISANKIKAN_FROM_MIN VARCHAR(2),
	SEISANKIKAN_TO DATE,
	SEISANKIKAN_TO_HOUR VARCHAR(2),
	SEISANKIKAN_TO_MIN VARCHAR(2),
	KEIJOUBI DATE,
	SHIHARAIBI DATE,
	SHIHARAIKIBOUBI DATE,
	SHIHARAIHOUHOU VARCHAR(1) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	GOUKEI_KINGAKU DECIMAL(15) NOT NULL,
	HOUJIN_CARD_RIYOU_KINGAKU DECIMAL(15) NOT NULL,
	KAISHA_TEHAI_KINGAKU DECIMAL(15) NOT NULL,
	SASHIHIKI_SHIKYUU_KINGAKU DECIMAL(15) NOT NULL,
	SASHIHIKI_NUM NUMERIC(2),
	SASHIHIKI_TANKA DECIMAL(6),
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HOSOKU VARCHAR(240) NOT NULL,
	SHIWAKE_EDANO INT,
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE RYOHISEISAN IS '����Z';
COMMENT ON COLUMN RYOHISEISAN.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN RYOHISEISAN.KARIBARAI_DENPYOU_ID IS '�����`�[ID';
COMMENT ON COLUMN RYOHISEISAN.KARIBARAI_ON IS '�����\���t���O';
COMMENT ON COLUMN RYOHISEISAN.KARIBARAI_MISHIYOU_FLG IS '���������g�p�t���O';
COMMENT ON COLUMN RYOHISEISAN.SHUCCHOU_CHUUSHI_FLG IS '�o�����~�t���O';
COMMENT ON COLUMN RYOHISEISAN.DAIRIFLG IS '�㗝�t���O';
COMMENT ON COLUMN RYOHISEISAN.USER_ID IS '���[�U�[ID';
COMMENT ON COLUMN RYOHISEISAN.SHAIN_NO IS '�Ј��ԍ�';
COMMENT ON COLUMN RYOHISEISAN.USER_SEI IS '���[�U�[��';
COMMENT ON COLUMN RYOHISEISAN.USER_MEI IS '���[�U�[��';
COMMENT ON COLUMN RYOHISEISAN.HOUMONSAKI IS '�K���';
COMMENT ON COLUMN RYOHISEISAN.MOKUTEKI IS '�ړI';
COMMENT ON COLUMN RYOHISEISAN.SEISANKIKAN_FROM IS '���Z���ԊJ�n��';
COMMENT ON COLUMN RYOHISEISAN.SEISANKIKAN_FROM_HOUR IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN RYOHISEISAN.SEISANKIKAN_FROM_MIN IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN RYOHISEISAN.SEISANKIKAN_TO IS '���Z���ԏI����';
COMMENT ON COLUMN RYOHISEISAN.SEISANKIKAN_TO_HOUR IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN RYOHISEISAN.SEISANKIKAN_TO_MIN IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN RYOHISEISAN.KEIJOUBI IS '�v���';
COMMENT ON COLUMN RYOHISEISAN.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN RYOHISEISAN.SHIHARAIKIBOUBI IS '�x����]��';
COMMENT ON COLUMN RYOHISEISAN.SHIHARAIHOUHOU IS '�x�����@';
COMMENT ON COLUMN RYOHISEISAN.TEKIYOU IS '�E�v';
COMMENT ON COLUMN RYOHISEISAN.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN RYOHISEISAN.GOUKEI_KINGAKU IS '���v���z';
COMMENT ON COLUMN RYOHISEISAN.HOUJIN_CARD_RIYOU_KINGAKU IS '���@�l�J�[�h���p���v';
COMMENT ON COLUMN RYOHISEISAN.KAISHA_TEHAI_KINGAKU IS '��Ў�z���v';
COMMENT ON COLUMN RYOHISEISAN.SASHIHIKI_SHIKYUU_KINGAKU IS '�����x�����z';
COMMENT ON COLUMN RYOHISEISAN.SASHIHIKI_NUM IS '������';
COMMENT ON COLUMN RYOHISEISAN.SASHIHIKI_TANKA IS '�����P��';
COMMENT ON COLUMN RYOHISEISAN.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN RYOHISEISAN.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN RYOHISEISAN.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN RYOHISEISAN.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN RYOHISEISAN.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN RYOHISEISAN.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN RYOHISEISAN.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN RYOHISEISAN.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN RYOHISEISAN.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN RYOHISEISAN.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN RYOHISEISAN.HOSOKU IS '�⑫';
COMMENT ON COLUMN RYOHISEISAN.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN RYOHISEISAN.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN RYOHISEISAN.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN RYOHISEISAN.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN RYOHISEISAN.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN RYOHISEISAN.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN RYOHISEISAN.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN RYOHISEISAN.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN RYOHISEISAN.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN RYOHISEISAN.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN RYOHISEISAN.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN RYOHISEISAN.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN RYOHISEISAN.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN RYOHISEISAN.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN RYOHISEISAN.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN RYOHISEISAN.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN RYOHISEISAN.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN RYOHISEISAN.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN RYOHISEISAN.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN RYOHISEISAN.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN RYOHISEISAN.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN RYOHISEISAN.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN RYOHISEISAN.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN RYOHISEISAN.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN RYOHISEISAN.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN RYOHISEISAN.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN RYOHISEISAN.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN RYOHISEISAN.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN RYOHISEISAN.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN RYOHISEISAN.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN RYOHISEISAN.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN RYOHISEISAN.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN RYOHISEISAN.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN RYOHISEISAN.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN RYOHISEISAN.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO ryohiseisan( 
  denpyou_id
  , karibarai_denpyou_id
  , karibarai_on
  , karibarai_mishiyou_flg
  , SHUCCHOU_CHUUSHI_FLG
  , dairiflg
  , user_id
  , shain_no
  , user_sei
  , user_mei
  , houmonsaki
  , mokuteki
  , seisankikan_from
  , seisankikan_from_hour
  , seisankikan_from_min
  , seisankikan_to
  , seisankikan_to_hour
  , seisankikan_to_min
  , keijoubi
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , zeiritsu
  , goukei_kingaku
  , houjin_card_riyou_kingaku
  , kaisha_tehai_kingaku
  , sashihiki_shikyuu_kingaku
  , sashihiki_num
  , sashihiki_tanka
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , karibarai_denpyou_id
  , karibarai_on
  , karibarai_mishiyou_flg
  , '0' --SHUCCHOU_CHUUSHI_FLG
  , dairiflg
  , user_id
  , shain_no
  , user_sei
  , user_mei
  , houmonsaki
  , mokuteki
  , seisankikan_from
  , seisankikan_from_hour
  , seisankikan_from_min
  , seisankikan_to
  , seisankikan_to_hour
  , seisankikan_to_min
  , keijoubi
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , zeiritsu
  , goukei_kingaku
  , houjin_card_riyou_kingaku
  , kaisha_tehai_kingaku
  , sashihiki_shikyuu_kingaku
  , sashihiki_num
  , sashihiki_tanka
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , '' --   , torihikisaki_cd
  , '' --   , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , project_cd
  , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  ryohiseisan_old;
DROP TABLE ryohiseisan_old;

-- ����Z�o���
ALTER TABLE RYOHISEISAN_KEIHI_MEISAI RENAME TO RYOHISEISAN_KEIHI_MEISAI_OLD;
CREATE TABLE RYOHISEISAN_KEIHI_MEISAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	DENPYOU_EDANO INT NOT NULL,
	SHIWAKE_EDANO INT NOT NULL,
	SHIYOUBI DATE,
	SHOUHYOU_SHORUI_FLG VARCHAR(1),
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	SHIHARAI_KINGAKU DECIMAL(15) NOT NULL,
	HONTAI_KINGAKU DECIMAL(15),
	SHOUHIZEIGAKU DECIMAL(15),
	HOUJIN_CARD_RIYOU_FLG VARCHAR(1) NOT NULL,
	KAISHA_TEHAI_FLG VARCHAR(1) NOT NULL,
	KOUSAIHI_SHOUSAI_HYOUJI_FLG VARCHAR(1) NOT NULL,
	KOUSAIHI_SHOUSAI VARCHAR(240),
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	HIMODUKE_CARD_MEISAI BIGINT,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID, DENPYOU_EDANO)
) WITHOUT OIDS;
COMMENT ON TABLE RYOHISEISAN_KEIHI_MEISAI IS '����Z�o���';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.DENPYOU_EDANO IS '�`�[�}�ԍ�';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.SHIYOUBI IS '�g�p��';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.SHOUHYOU_SHORUI_FLG IS '�؜ߏ��ރt���O';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.SHIHARAI_KINGAKU IS '�x�����z';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.HONTAI_KINGAKU IS '�{�̋��z';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.SHOUHIZEIGAKU IS '����Ŋz';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.HOUJIN_CARD_RIYOU_FLG IS '�@�l�J�[�h���p�t���O';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KAISHA_TEHAI_FLG IS '��Ў�z�t���O';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KOUSAIHI_SHOUSAI_HYOUJI_FLG IS '���۔�ڍו\���t���O';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KOUSAIHI_SHOUSAI IS '���۔�ڍ�';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.HIMODUKE_CARD_MEISAI IS '�R�t���J�[�h����';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN RYOHISEISAN_KEIHI_MEISAI.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO ryohiseisan_keihi_meisai( 
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , denpyou_edano
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , project_cd
  , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  ryohiseisan_keihi_meisai_old;
DROP TABLE ryohiseisan_keihi_meisai_old;

-- �C�O������\��
ALTER TABLE KAIGAI_RYOHI_KARIBARAI RENAME TO KAIGAI_RYOHI_KARIBARAI_OLD;
CREATE TABLE KAIGAI_RYOHI_KARIBARAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	KARIBARAI_ON VARCHAR(1) DEFAULT '0' NOT NULL,
	DAIRIFLG VARCHAR(1) NOT NULL,
	USER_ID VARCHAR(30),
	SHAIN_NO VARCHAR(15) NOT NULL,
	USER_SEI VARCHAR(10) NOT NULL,
	USER_MEI VARCHAR(10) NOT NULL,
	HOUMONSAKI VARCHAR(120) NOT NULL,
	MOKUTEKI VARCHAR(240) NOT NULL,
	SEISANKIKAN_FROM DATE,
	SEISANKIKAN_FROM_HOUR VARCHAR(2),
	SEISANKIKAN_FROM_MIN VARCHAR(2),
	SEISANKIKAN_TO DATE,
	SEISANKIKAN_TO_HOUR VARCHAR(2),
	SEISANKIKAN_TO_MIN VARCHAR(2),
	SHIHARAIBI DATE,
	SHIHARAIKIBOUBI DATE,
	SHIHARAIHOUHOU VARCHAR(1) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	KINGAKU DECIMAL(15) NOT NULL,
	KARIBARAI_KINGAKU DECIMAL(15),
	SASHIHIKI_NUM NUMERIC(2),
	SASHIHIKI_TANKA DECIMAL(6),
	SASHIHIKI_NUM_KAIGAI NUMERIC(2),
	SASHIHIKI_TANKA_KAIGAI DECIMAL(6),
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HOSOKU VARCHAR(240) NOT NULL,
	SHIWAKE_EDANO INT,
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	SEISAN_KANRYOUBI DATE,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE KAIGAI_RYOHI_KARIBARAI IS '�C�O�����';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KARIBARAI_ON IS '�����\���t���O';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.DAIRIFLG IS '�㗝�t���O';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.USER_ID IS '���[�U�[ID';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SHAIN_NO IS '�Ј��ԍ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.USER_SEI IS '���[�U�[��';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.USER_MEI IS '���[�U�[��';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HOUMONSAKI IS '�K���';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.MOKUTEKI IS '�ړI';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SEISANKIKAN_FROM IS '���Z���ԊJ�n��';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SEISANKIKAN_FROM_HOUR IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SEISANKIKAN_FROM_MIN IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SEISANKIKAN_TO IS '���Z���ԏI����';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SEISANKIKAN_TO_HOUR IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SEISANKIKAN_TO_MIN IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SHIHARAIKIBOUBI IS '�x����]��';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SHIHARAIHOUHOU IS '�x�����@';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KINGAKU IS '���z';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KARIBARAI_KINGAKU IS '�������z';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SASHIHIKI_NUM IS '������';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SASHIHIKI_TANKA IS '�����P��';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SASHIHIKI_NUM_KAIGAI IS '�����񐔁i�C�O�j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SASHIHIKI_TANKA_KAIGAI IS '�����P���i�C�O�j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.HOSOKU IS '�⑫';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.SEISAN_KANRYOUBI IS '���Z������';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO kaigai_ryohi_karibarai( 
  denpyou_id
  , karibarai_on
  , dairiflg
  , user_id
  , shain_no
  , user_sei
  , user_mei
  , houmonsaki
  , mokuteki
  , seisankikan_from
  , seisankikan_from_hour
  , seisankikan_from_min
  , seisankikan_to
  , seisankikan_to_hour
  , seisankikan_to_min
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , kingaku
  , karibarai_kingaku
  , sashihiki_num
  , sashihiki_tanka
  , sashihiki_num_kaigai
  , sashihiki_tanka_kaigai
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , SEISAN_KANRYOUBI
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , karibarai_on
  , dairiflg
  , user_id
  , shain_no
  , user_sei
  , user_mei
  , houmonsaki
  , mokuteki
  , seisankikan_from
  , seisankikan_from_hour
  , seisankikan_from_min
  , seisankikan_to
  , seisankikan_to_hour
  , seisankikan_to_min
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , kingaku
  , karibarai_kingaku
  , sashihiki_num
  , sashihiki_tanka
  , sashihiki_num_kaigai
  , sashihiki_tanka_kaigai
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , '' --   , torihikisaki_cd
  , '' --   , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , '' --   , project_cd
  , '' --   , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , null --SEISAN_KANRYOUBI
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  kaigai_ryohi_karibarai_old;
DROP TABLE kaigai_ryohi_karibarai_old;

-- �C�O������o���
ALTER TABLE KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI RENAME TO KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI_OLD;
CREATE TABLE KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	DENPYOU_EDANO INT NOT NULL,
	KAIGAI_FLG VARCHAR(1) NOT NULL,
	SHIWAKE_EDANO INT NOT NULL,
	SHIYOUBI DATE,
	SHOUHYOU_SHORUI_FLG VARCHAR(1),
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	KAZEI_FLG VARCHAR(1) NOT NULL,
	SHIHARAI_KINGAKU DECIMAL(15) NOT NULL,
	HONTAI_KINGAKU DECIMAL(15),
	SHOUHIZEIGAKU DECIMAL(15),
	KOUSAIHI_SHOUSAI_HYOUJI_FLG VARCHAR(1) NOT NULL,
	KOUSAIHI_SHOUSAI VARCHAR(240),
	HEISHU_CD VARCHAR(4),
	RATE DECIMAL(11,5),
	GAIKA DECIMAL(19,2),
	CURRENCY_UNIT VARCHAR(20),
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID, DENPYOU_EDANO, KAIGAI_FLG)
) WITHOUT OIDS;
COMMENT ON TABLE KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI IS '�C�O������o���';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.DENPYOU_EDANO IS '�`�[�}�ԍ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KAIGAI_FLG IS '�C�O�t���O';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.SHIYOUBI IS '�g�p��';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.SHOUHYOU_SHORUI_FLG IS '�؜ߏ��ރt���O';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KAZEI_FLG IS '�ېŃt���O';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.SHIHARAI_KINGAKU IS '�x�����z';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.HONTAI_KINGAKU IS '�{�̋��z';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.SHOUHIZEIGAKU IS '����Ŋz';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KOUSAIHI_SHOUSAI_HYOUJI_FLG IS '���۔�ڍו\���t���O';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KOUSAIHI_SHOUSAI IS '���۔�ڍ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.HEISHU_CD IS '����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.RATE IS '���[�g';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.GAIKA IS '�O�݋��z';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.CURRENCY_UNIT IS '�ʉݒP��';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN KAIGAI_RYOHI_KARIBARAI_KEIHI_MEISAI.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO kaigai_ryohi_karibarai_keihi_meisai( 
  denpyou_id
  , denpyou_edano
  , kaigai_flg
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , kazei_flg
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , heishu_cd
  , rate
  , gaika
  , currency_unit
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , denpyou_edano
  , kaigai_flg
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , kazei_flg
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , heishu_cd
  , rate
  , gaika
  , currency_unit
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , project_cd
  , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  kaigai_ryohi_karibarai_keihi_meisai_old;
DROP TABLE kaigai_ryohi_karibarai_keihi_meisai_old;

-- �C�O����Z
ALTER TABLE KAIGAI_RYOHISEISAN RENAME TO KAIGAI_RYOHISEISAN_OLD;
CREATE TABLE KAIGAI_RYOHISEISAN
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	KARIBARAI_DENPYOU_ID VARCHAR(19) NOT NULL,
	KARIBARAI_ON VARCHAR(1) NOT NULL,
	KARIBARAI_MISHIYOU_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
	SHUCCHOU_CHUUSHI_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
	DAIRIFLG VARCHAR(1) NOT NULL,
	USER_ID VARCHAR(30) NOT NULL,
	SHAIN_NO VARCHAR(15) NOT NULL,
	USER_SEI VARCHAR(10) NOT NULL,
	USER_MEI VARCHAR(10) NOT NULL,
	HOUMONSAKI VARCHAR(120) NOT NULL,
	MOKUTEKI VARCHAR(240) NOT NULL,
	SEISANKIKAN_FROM DATE,
	SEISANKIKAN_FROM_HOUR VARCHAR(2),
	SEISANKIKAN_FROM_MIN VARCHAR(2),
	SEISANKIKAN_TO DATE,
	SEISANKIKAN_TO_HOUR VARCHAR(2),
	SEISANKIKAN_TO_MIN VARCHAR(2),
	KEIJOUBI DATE,
	SHIHARAIBI DATE,
	SHIHARAIKIBOUBI DATE,
	SHIHARAIHOUHOU VARCHAR(1) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	KAIGAI_TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	GOUKEI_KINGAKU DECIMAL(15) NOT NULL,
	HOUJIN_CARD_RIYOU_KINGAKU DECIMAL(15) NOT NULL,
	KAISHA_TEHAI_KINGAKU DECIMAL(15) NOT NULL,
	SASHIHIKI_SHIKYUU_KINGAKU DECIMAL(15) NOT NULL,
	SASHIHIKI_NUM NUMERIC(2),
	SASHIHIKI_TANKA DECIMAL(6),
	SASHIHIKI_NUM_KAIGAI NUMERIC(2),
	SASHIHIKI_TANKA_KAIGAI DECIMAL(6),
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HOSOKU VARCHAR(240) NOT NULL,
	SHIWAKE_EDANO INT,
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	RYOHI_KAZEI_FLG VARCHAR(1) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	KAIGAI_SHIWAKE_EDANO INT,
	KAIGAI_TORIHIKI_NAME VARCHAR(20) NOT NULL,
	KAIGAI_KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KAIGAI_KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KAIGAI_TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	KAIGAI_TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KAIGAI_KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KAIGAI_KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KAIGAI_KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KAIGAI_KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KAIGAI_KAZEI_FLG VARCHAR(1) NOT NULL,
	KAIGAI_KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KAIGAI_KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KAIGAI_KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KAIGAI_KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KAIGAI_KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KAIGAI_KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KAIGAI_KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KAIGAI_UF1_CD VARCHAR(20) NOT NULL,
	KAIGAI_UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_UF2_CD VARCHAR(20) NOT NULL,
	KAIGAI_UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_UF3_CD VARCHAR(20) NOT NULL,
	KAIGAI_UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_UF4_CD VARCHAR(20) NOT NULL,
	KAIGAI_UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_UF5_CD VARCHAR(20) NOT NULL,
	KAIGAI_UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_UF6_CD VARCHAR(20) NOT NULL,
	KAIGAI_UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_UF7_CD VARCHAR(20) NOT NULL,
	KAIGAI_UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_UF8_CD VARCHAR(20) NOT NULL,
	KAIGAI_UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_UF9_CD VARCHAR(20) NOT NULL,
	KAIGAI_UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_UF10_CD VARCHAR(20) NOT NULL,
	KAIGAI_UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_PROJECT_CD VARCHAR(12) NOT NULL,
	KAIGAI_PROJECT_NAME VARCHAR(20) NOT NULL,
	KAIGAI_SEGMENT_CD VARCHAR(8) NOT NULL,
	KAIGAI_SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KAIGAI_TEKIYOU_CD VARCHAR(4) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE KAIGAI_RYOHISEISAN IS '�C�O����Z';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KARIBARAI_DENPYOU_ID IS '�����`�[ID';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KARIBARAI_ON IS '�����\���t���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KARIBARAI_MISHIYOU_FLG IS '���������g�p�t���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SHUCCHOU_CHUUSHI_FLG IS '�o�����~�t���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.DAIRIFLG IS '�㗝�t���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.USER_ID IS '���[�U�[ID';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SHAIN_NO IS '�Ј��ԍ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.USER_SEI IS '���[�U�[��';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.USER_MEI IS '���[�U�[��';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HOUMONSAKI IS '�K���';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.MOKUTEKI IS '�ړI';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SEISANKIKAN_FROM IS '���Z���ԊJ�n��';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SEISANKIKAN_FROM_HOUR IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SEISANKIKAN_FROM_MIN IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SEISANKIKAN_TO IS '���Z���ԏI����';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SEISANKIKAN_TO_HOUR IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SEISANKIKAN_TO_MIN IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KEIJOUBI IS '�v���';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SHIHARAIKIBOUBI IS '�x����]��';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SHIHARAIHOUHOU IS '�x�����@';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.TEKIYOU IS '�E�v';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_TEKIYOU IS '�E�v�i�C�O�j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.GOUKEI_KINGAKU IS '���v���z';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HOUJIN_CARD_RIYOU_KINGAKU IS '���@�l�J�[�h���p���v';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAISHA_TEHAI_KINGAKU IS '��Ў�z���v';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SASHIHIKI_SHIKYUU_KINGAKU IS '�����x�����z';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SASHIHIKI_NUM IS '������';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SASHIHIKI_TANKA IS '�����P��';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SASHIHIKI_NUM_KAIGAI IS '�����񐔁i�C�O�j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SASHIHIKI_TANKA_KAIGAI IS '�����P���i�C�O�j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.HOSOKU IS '�⑫';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.RYOHI_KAZEI_FLG IS '����ېŃt���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_SHIWAKE_EDANO IS '�C�O�d��}�ԍ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_TORIHIKI_NAME IS '�C�O�����';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KARI_FUTAN_BUMON_CD IS '�C�O�ؕ����S����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KARI_FUTAN_BUMON_NAME IS '�C�O�ؕ����S���喼';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_TORIHIKISAKI_CD IS '�C�O�����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_TORIHIKISAKI_NAME_RYAKUSHIKI IS '�C�O����於�i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KARI_KAMOKU_CD IS '�C�O�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KARI_KAMOKU_NAME IS '�C�O�ؕ��Ȗږ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KARI_KAMOKU_EDABAN_CD IS '�C�O�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KARI_KAMOKU_EDABAN_NAME IS '�C�O�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KARI_KAZEI_KBN IS '�C�O�ؕ��ېŋ敪';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KAZEI_FLG IS '�C�O�ېŃt���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KASHI_FUTAN_BUMON_CD IS '�C�O�ݕ����S����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KASHI_FUTAN_BUMON_NAME IS '�C�O�ݕ����S���喼';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KASHI_KAMOKU_CD IS '�C�O�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KASHI_KAMOKU_NAME IS '�C�O�ݕ��Ȗږ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KASHI_KAMOKU_EDABAN_CD IS '�C�O�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KASHI_KAMOKU_EDABAN_NAME IS '�C�O�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_KASHI_KAZEI_KBN IS '�C�O�ݕ��ېŋ敪';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF1_CD IS '�C�OUF1�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF1_NAME_RYAKUSHIKI IS '�C�OUF1���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF2_CD IS '�C�OUF2�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF2_NAME_RYAKUSHIKI IS '�C�OUF2���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF3_CD IS '�C�OUF3�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF3_NAME_RYAKUSHIKI IS '�C�OUF3���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF4_CD IS '�C�OUF4�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF4_NAME_RYAKUSHIKI IS '�C�OUF4���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF5_CD IS '�C�OUF5�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF5_NAME_RYAKUSHIKI IS '�C�OUF5���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF6_CD IS '�C�OUF6�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF6_NAME_RYAKUSHIKI IS '�C�OUF6���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF7_CD IS '�C�OUF7�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF7_NAME_RYAKUSHIKI IS '�C�OUF7���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF8_CD IS '�C�OUF8�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF8_NAME_RYAKUSHIKI IS '�C�OUF8���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF9_CD IS '�C�OUF9�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF9_NAME_RYAKUSHIKI IS '�C�OUF9���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF10_CD IS '�C�OUF10�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_UF10_NAME_RYAKUSHIKI IS '�C�OUF10���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_PROJECT_CD IS '�C�O�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_PROJECT_NAME IS '�C�O�v���W�F�N�g��';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_SEGMENT_CD IS '�C�O�Z�O�����g�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_SEGMENT_NAME_RYAKUSHIKI IS '�C�O�Z�O�����g���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KAIGAI_TEKIYOU_CD IS '�C�O�E�v�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO kaigai_ryohiseisan( 
  denpyou_id
  , karibarai_denpyou_id
  , karibarai_on
  , karibarai_mishiyou_flg
  , SHUCCHOU_CHUUSHI_FLG
  , dairiflg
  , user_id
  , shain_no
  , user_sei
  , user_mei
  , houmonsaki
  , mokuteki
  , seisankikan_from
  , seisankikan_from_hour
  , seisankikan_from_min
  , seisankikan_to
  , seisankikan_to_hour
  , seisankikan_to_min
  , keijoubi
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , kaigai_tekiyou
  , zeiritsu
  , goukei_kingaku
  , houjin_card_riyou_kingaku
  , kaisha_tehai_kingaku
  , sashihiki_shikyuu_kingaku
  , sashihiki_num
  , sashihiki_tanka
  , sashihiki_num_kaigai
  , sashihiki_tanka_kaigai
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , ryohi_kazei_flg
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , kaigai_shiwake_edano
  , kaigai_torihiki_name
  , kaigai_kari_futan_bumon_cd
  , kaigai_kari_futan_bumon_name
  , kaigai_torihikisaki_cd
  , kaigai_torihikisaki_name_ryakushiki
  , kaigai_kari_kamoku_cd
  , kaigai_kari_kamoku_name
  , kaigai_kari_kamoku_edaban_cd
  , kaigai_kari_kamoku_edaban_name
  , kaigai_kari_kazei_kbn
  , kaigai_kazei_flg
  , kaigai_kashi_futan_bumon_cd
  , kaigai_kashi_futan_bumon_name
  , kaigai_kashi_kamoku_cd
  , kaigai_kashi_kamoku_name
  , kaigai_kashi_kamoku_edaban_cd
  , kaigai_kashi_kamoku_edaban_name
  , kaigai_kashi_kazei_kbn
  , kaigai_uf1_cd
  , kaigai_uf1_name_ryakushiki
  , kaigai_uf2_cd
  , kaigai_uf2_name_ryakushiki
  , kaigai_uf3_cd
  , kaigai_uf3_name_ryakushiki
  , kaigai_uf4_cd
  , kaigai_uf4_name_ryakushiki
  , kaigai_uf5_cd
  , kaigai_uf5_name_ryakushiki
  , kaigai_uf6_cd
  , kaigai_uf6_name_ryakushiki
  , kaigai_uf7_cd
  , kaigai_uf7_name_ryakushiki
  , kaigai_uf8_cd
  , kaigai_uf8_name_ryakushiki
  , kaigai_uf9_cd
  , kaigai_uf9_name_ryakushiki
  , kaigai_uf10_cd
  , kaigai_uf10_name_ryakushiki
  , kaigai_project_cd
  , kaigai_project_name
  , kaigai_segment_cd
  , kaigai_segment_name_ryakushiki
  , kaigai_tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , karibarai_denpyou_id
  , karibarai_on
  , karibarai_mishiyou_flg
  , '0' --SHUCCHOU_CHUUSHI_FLG
  , dairiflg
  , user_id
  , shain_no
  , user_sei
  , user_mei
  , houmonsaki
  , mokuteki
  , seisankikan_from
  , seisankikan_from_hour
  , seisankikan_from_min
  , seisankikan_to
  , seisankikan_to_hour
  , seisankikan_to_min
  , keijoubi
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , kaigai_tekiyou
  , zeiritsu
  , goukei_kingaku
  , houjin_card_riyou_kingaku
  , kaisha_tehai_kingaku
  , sashihiki_shikyuu_kingaku
  , sashihiki_num
  , sashihiki_tanka
  , sashihiki_num_kaigai
  , sashihiki_tanka_kaigai
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , '' --   , torihikisaki_cd
  , '' --   , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , ryohi_kazei_flg
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , project_cd
  , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , kaigai_shiwake_edano
  , kaigai_torihiki_name
  , kaigai_kari_futan_bumon_cd
  , kaigai_kari_futan_bumon_name
  , '' --   , kaigai_torihikisaki_cd
  , '' --   , kaigai_torihikisaki_name_ryakushiki
  , kaigai_kari_kamoku_cd
  , kaigai_kari_kamoku_name
  , kaigai_kari_kamoku_edaban_cd
  , kaigai_kari_kamoku_edaban_name
  , kaigai_kari_kazei_kbn
  , kaigai_kazei_flg
  , kaigai_kashi_futan_bumon_cd
  , kaigai_kashi_futan_bumon_name
  , kaigai_kashi_kamoku_cd
  , kaigai_kashi_kamoku_name
  , kaigai_kashi_kamoku_edaban_cd
  , kaigai_kashi_kamoku_edaban_name
  , kaigai_kashi_kazei_kbn
  , kaigai_uf1_cd
  , kaigai_uf1_name_ryakushiki
  , kaigai_uf2_cd
  , kaigai_uf2_name_ryakushiki
  , kaigai_uf3_cd
  , kaigai_uf3_name_ryakushiki
  , '' --   , kaigai_uf4_cd
  , '' --   , kaigai_uf4_name_ryakushiki
  , '' --   , kaigai_uf5_cd
  , '' --   , kaigai_uf5_name_ryakushiki
  , '' --   , kaigai_uf6_cd
  , '' --   , kaigai_uf6_name_ryakushiki
  , '' --   , kaigai_uf7_cd
  , '' --   , kaigai_uf7_name_ryakushiki
  , '' --   , kaigai_uf8_cd
  , '' --   , kaigai_uf8_name_ryakushiki
  , '' --   , kaigai_uf9_cd
  , '' --   , kaigai_uf9_name_ryakushiki
  , '' --   , kaigai_uf10_cd
  , '' --   , kaigai_uf10_name_ryakushiki
  , kaigai_project_cd
  , kaigai_project_name
  , '' --   , kaigai_segment_cd
  , '' --   , kaigai_segment_name_ryakushiki
  , kaigai_tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  kaigai_ryohiseisan_old;
DROP TABLE kaigai_ryohiseisan_old;

-- �C�O����o���
ALTER TABLE KAIGAI_RYOHISEISAN_KEIHI_MEISAI RENAME TO KAIGAI_RYOHISEISAN_KEIHI_MEISAI_OLD;
CREATE TABLE KAIGAI_RYOHISEISAN_KEIHI_MEISAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	DENPYOU_EDANO INT NOT NULL,
	KAIGAI_FLG VARCHAR(1) NOT NULL,
	SHIWAKE_EDANO INT NOT NULL,
	SHIYOUBI DATE,
	SHOUHYOU_SHORUI_FLG VARCHAR(1),
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	KAZEI_FLG VARCHAR(1) NOT NULL,
	SHIHARAI_KINGAKU DECIMAL(15) NOT NULL,
	HONTAI_KINGAKU DECIMAL(15),
	SHOUHIZEIGAKU DECIMAL(15),
	HOUJIN_CARD_RIYOU_FLG VARCHAR(1) NOT NULL,
	KAISHA_TEHAI_FLG VARCHAR(1) NOT NULL,
	KOUSAIHI_SHOUSAI_HYOUJI_FLG VARCHAR(1) NOT NULL,
	KOUSAIHI_SHOUSAI VARCHAR(240),
	HEISHU_CD VARCHAR(4),
	RATE DECIMAL(11,5),
	GAIKA DECIMAL(19,2),
	CURRENCY_UNIT VARCHAR(20),
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	HIMODUKE_CARD_MEISAI BIGINT,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID, DENPYOU_EDANO, KAIGAI_FLG)
) WITHOUT OIDS;
COMMENT ON TABLE KAIGAI_RYOHISEISAN_KEIHI_MEISAI IS '�C�O����Z�o���';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.DENPYOU_EDANO IS '�`�[�}�ԍ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KAIGAI_FLG IS '�C�O�t���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.SHIYOUBI IS '�g�p��';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.SHOUHYOU_SHORUI_FLG IS '�؜ߏ��ރt���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KAZEI_FLG IS '�ېŃt���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.SHIHARAI_KINGAKU IS '�x�����z';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.HONTAI_KINGAKU IS '�{�̋��z';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.SHOUHIZEIGAKU IS '����Ŋz';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.HOUJIN_CARD_RIYOU_FLG IS '�@�l�J�[�h���p�t���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KAISHA_TEHAI_FLG IS '��Ў�z�t���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KOUSAIHI_SHOUSAI_HYOUJI_FLG IS '���۔�ڍו\���t���O';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KOUSAIHI_SHOUSAI IS '���۔�ڍ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.HEISHU_CD IS '����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.RATE IS '���[�g';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.GAIKA IS '�O�݋��z';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.CURRENCY_UNIT IS '�ʉݒP��';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.HIMODUKE_CARD_MEISAI IS '�R�t���J�[�h����';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN KAIGAI_RYOHISEISAN_KEIHI_MEISAI.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO kaigai_ryohiseisan_keihi_meisai( 
  denpyou_id
  , denpyou_edano
  , kaigai_flg
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , kazei_flg
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , heishu_cd
  , rate
  , gaika
  , currency_unit
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , denpyou_edano
  , kaigai_flg
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , kazei_flg
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , heishu_cd
  , rate
  , gaika
  , currency_unit
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , project_cd
  , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  kaigai_ryohiseisan_keihi_meisai_old;
DROP TABLE kaigai_ryohiseisan_keihi_meisai_old;

-- �ʋΒ��
ALTER TABLE TSUUKINTEIKI RENAME TO TSUUKINTEIKI_OLD;
CREATE TABLE TSUUKINTEIKI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	SHIYOU_KIKAN_KBN VARCHAR(2) NOT NULL,
	SHIYOU_KAISHIBI DATE NOT NULL,
	SHIYOU_SHUURYOUBI DATE NOT NULL,
	JYOUSHA_KUKAN VARCHAR NOT NULL,
	SHIHARAIBI DATE,
	TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	KINGAKU DECIMAL(15) NOT NULL,
	TENYUURYOKU_FLG VARCHAR(1) NOT NULL,
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SHIWAKE_EDANO INT NOT NULL,
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE TSUUKINTEIKI IS '�ʋΒ��';
COMMENT ON COLUMN TSUUKINTEIKI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN TSUUKINTEIKI.SHIYOU_KIKAN_KBN IS '�g�p���ԋ敪';
COMMENT ON COLUMN TSUUKINTEIKI.SHIYOU_KAISHIBI IS '�g�p�J�n��';
COMMENT ON COLUMN TSUUKINTEIKI.SHIYOU_SHUURYOUBI IS '�g�p�I����';
COMMENT ON COLUMN TSUUKINTEIKI.JYOUSHA_KUKAN IS '��ԋ��';
COMMENT ON COLUMN TSUUKINTEIKI.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN TSUUKINTEIKI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN TSUUKINTEIKI.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN TSUUKINTEIKI.KINGAKU IS '���z';
COMMENT ON COLUMN TSUUKINTEIKI.TENYUURYOKU_FLG IS '����̓t���O';
COMMENT ON COLUMN TSUUKINTEIKI.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN TSUUKINTEIKI.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN TSUUKINTEIKI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN TSUUKINTEIKI.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN TSUUKINTEIKI.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN TSUUKINTEIKI.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN TSUUKINTEIKI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN TSUUKINTEIKI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN TSUUKINTEIKI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN TSUUKINTEIKI.KOUSHIN_TIME IS '�X�V����';

INSERT 
INTO tsuukinteiki( 
  denpyou_id
  , shiyou_kikan_kbn
  , shiyou_kaishibi
  , shiyou_shuuryoubi
  , jyousha_kukan
  , shiharaibi
  , tekiyou
  , zeiritsu
  , kingaku
  , tenyuuryoku_flg
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , shiyou_kikan_kbn
  , shiyou_kaishibi
  , shiyou_shuuryoubi
  , jyousha_kukan
  , shiharaibi
  , tekiyou
  , zeiritsu
  , kingaku
  , tenyuuryoku_flg
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , '' --   , torihikisaki_cd
  , '' --   , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , '' --   , project_cd
  , '' --   , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  tsuukinteiki_old;
DROP TABLE tsuukinteiki_old;

-- �U�֐\��
ALTER TABLE FURIKAE RENAME TO FURIKAE_OLD;
CREATE TABLE FURIKAE
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	DENPYOU_DATE DATE NOT NULL,
	SHOUHYOU_SHORUI_FLG VARCHAR(1) NOT NULL,
	KINGAKU DECIMAL(15) NOT NULL,
	HONTAI_KINGAKU DECIMAL(15) NOT NULL,
	SHOUHIZEIGAKU DECIMAL(15),
	ZEIRITSU DECIMAL(3) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	BIKOU VARCHAR(40) NOT NULL,
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KARI_TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	KARI_TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	KASHI_TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_UF1_CD VARCHAR(20) NOT NULL,
	KARI_UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_UF2_CD VARCHAR(20) NOT NULL,
	KARI_UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_UF3_CD VARCHAR(20) NOT NULL,
	KARI_UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_UF4_CD VARCHAR(20) NOT NULL,
	KARI_UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_UF5_CD VARCHAR(20) NOT NULL,
	KARI_UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_UF6_CD VARCHAR(20) NOT NULL,
	KARI_UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_UF7_CD VARCHAR(20) NOT NULL,
	KARI_UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_UF8_CD VARCHAR(20) NOT NULL,
	KARI_UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_UF9_CD VARCHAR(20) NOT NULL,
	KARI_UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_UF10_CD VARCHAR(20) NOT NULL,
	KARI_UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_UF1_CD VARCHAR(20) NOT NULL,
	KASHI_UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_UF2_CD VARCHAR(20) NOT NULL,
	KASHI_UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_UF3_CD VARCHAR(20) NOT NULL,
	KASHI_UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_UF4_CD VARCHAR(20) NOT NULL,
	KASHI_UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_UF5_CD VARCHAR(20) NOT NULL,
	KASHI_UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_UF6_CD VARCHAR(20) NOT NULL,
	KASHI_UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_UF7_CD VARCHAR(20) NOT NULL,
	KASHI_UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_UF8_CD VARCHAR(20) NOT NULL,
	KASHI_UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_UF9_CD VARCHAR(20) NOT NULL,
	KASHI_UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_UF10_CD VARCHAR(20) NOT NULL,
	KASHI_UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_PROJECT_CD VARCHAR(12) NOT NULL,
	KARI_PROJECT_NAME VARCHAR(20) NOT NULL,
	KARI_SEGMENT_CD VARCHAR(8) NOT NULL,
	KARI_SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KASHI_PROJECT_CD VARCHAR(12) NOT NULL,
	KASHI_PROJECT_NAME VARCHAR(20) NOT NULL,
	KASHI_SEGMENT_CD VARCHAR(8) NOT NULL,
	KASHI_SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE FURIKAE IS '�U��';
COMMENT ON COLUMN FURIKAE.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN FURIKAE.DENPYOU_DATE IS '�`�[���t';
COMMENT ON COLUMN FURIKAE.SHOUHYOU_SHORUI_FLG IS '�؜ߏ��ރt���O';
COMMENT ON COLUMN FURIKAE.KINGAKU IS '���z';
COMMENT ON COLUMN FURIKAE.HONTAI_KINGAKU IS '�{�̋��z';
COMMENT ON COLUMN FURIKAE.SHOUHIZEIGAKU IS '����Ŋz';
COMMENT ON COLUMN FURIKAE.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN FURIKAE.TEKIYOU IS '�E�v';
COMMENT ON COLUMN FURIKAE.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN FURIKAE.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN FURIKAE.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN FURIKAE.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN FURIKAE.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN FURIKAE.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN FURIKAE.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN FURIKAE.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN FURIKAE.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN FURIKAE.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN FURIKAE.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN FURIKAE.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN FURIKAE.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN FURIKAE.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN FURIKAE.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN FURIKAE.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN FURIKAE.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN FURIKAE.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN FURIKAE.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN FURIKAE.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN FURIKAE.BIKOU IS '���l�i��v�`�[�j';
COMMENT ON COLUMN FURIKAE.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN FURIKAE.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN FURIKAE.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN FURIKAE.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN FURIKAE.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN FURIKAE.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN FURIKAE.KARI_TORIHIKISAKI_CD IS '�ؕ������R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_TORIHIKISAKI_NAME_RYAKUSHIKI IS '�ؕ�����於�i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN FURIKAE.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN FURIKAE.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN FURIKAE.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN FURIKAE.KASHI_TORIHIKISAKI_CD IS '�ݕ������R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_TORIHIKISAKI_NAME_RYAKUSHIKI IS '�ݕ�����於�i�����j';
COMMENT ON COLUMN FURIKAE.KARI_UF1_CD IS '�ؕ�UF1�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_UF1_NAME_RYAKUSHIKI IS '�ؕ�UF1���i�����j';
COMMENT ON COLUMN FURIKAE.KARI_UF2_CD IS '�ؕ�UF2�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_UF2_NAME_RYAKUSHIKI IS '�ؕ�UF2���i�����j';
COMMENT ON COLUMN FURIKAE.KARI_UF3_CD IS '�ؕ�UF3�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_UF3_NAME_RYAKUSHIKI IS '�ؕ�UF3���i�����j';
COMMENT ON COLUMN FURIKAE.KARI_UF4_CD IS '�ؕ�UF4�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_UF4_NAME_RYAKUSHIKI IS '�ؕ�UF4���i�����j';
COMMENT ON COLUMN FURIKAE.KARI_UF5_CD IS '�ؕ�UF5�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_UF5_NAME_RYAKUSHIKI IS '�ؕ�UF5���i�����j';
COMMENT ON COLUMN FURIKAE.KARI_UF6_CD IS '�ؕ�UF6�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_UF6_NAME_RYAKUSHIKI IS '�ؕ�UF6���i�����j';
COMMENT ON COLUMN FURIKAE.KARI_UF7_CD IS '�ؕ�UF7�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_UF7_NAME_RYAKUSHIKI IS '�ؕ�UF7���i�����j';
COMMENT ON COLUMN FURIKAE.KARI_UF8_CD IS '�ؕ�UF8�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_UF8_NAME_RYAKUSHIKI IS '�ؕ�UF8���i�����j';
COMMENT ON COLUMN FURIKAE.KARI_UF9_CD IS '�ؕ�UF9�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_UF9_NAME_RYAKUSHIKI IS '�ؕ�UF9���i�����j';
COMMENT ON COLUMN FURIKAE.KARI_UF10_CD IS '�ؕ�UF10�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_UF10_NAME_RYAKUSHIKI IS '�ؕ�UF10���i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_UF1_CD IS '�ݕ�UF1�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_UF1_NAME_RYAKUSHIKI IS '�ݕ�UF1���i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_UF2_CD IS '�ݕ�UF2�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_UF2_NAME_RYAKUSHIKI IS '�ݕ�UF2���i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_UF3_CD IS '�ݕ�UF3�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_UF3_NAME_RYAKUSHIKI IS '�ݕ�UF3���i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_UF4_CD IS '�ݕ�UF4�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_UF4_NAME_RYAKUSHIKI IS '�ݕ�UF4���i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_UF5_CD IS '�ݕ�UF5�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_UF5_NAME_RYAKUSHIKI IS '�ݕ�UF5���i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_UF6_CD IS '�ݕ�UF6�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_UF6_NAME_RYAKUSHIKI IS '�ݕ�UF6���i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_UF7_CD IS '�ݕ�UF7�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_UF7_NAME_RYAKUSHIKI IS '�ݕ�UF7���i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_UF8_CD IS '�ݕ�UF8�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_UF8_NAME_RYAKUSHIKI IS '�ݕ�UF8���i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_UF9_CD IS '�ݕ�UF9�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_UF9_NAME_RYAKUSHIKI IS '�ݕ�UF9���i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_UF10_CD IS '�ݕ�UF10�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_UF10_NAME_RYAKUSHIKI IS '�ݕ�UF10���i�����j';
COMMENT ON COLUMN FURIKAE.KARI_PROJECT_CD IS '�ؕ��v���W�F�N�g�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_PROJECT_NAME IS '�ؕ��v���W�F�N�g��';
COMMENT ON COLUMN FURIKAE.KARI_SEGMENT_CD IS '�ؕ��Z�O�����g�R�[�h';
COMMENT ON COLUMN FURIKAE.KARI_SEGMENT_NAME_RYAKUSHIKI IS '�ؕ��Z�O�����g���i�����j';
COMMENT ON COLUMN FURIKAE.KASHI_PROJECT_CD IS '�ݕ��v���W�F�N�g�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_PROJECT_NAME IS '�ݕ��v���W�F�N�g��';
COMMENT ON COLUMN FURIKAE.KASHI_SEGMENT_CD IS '�ݕ��Z�O�����g�R�[�h';
COMMENT ON COLUMN FURIKAE.KASHI_SEGMENT_NAME_RYAKUSHIKI IS '�ݕ��Z�O�����g���i�����j';
COMMENT ON COLUMN FURIKAE.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN FURIKAE.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN FURIKAE.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN FURIKAE.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO furikae( 
  denpyou_id
  , denpyou_date
  , shouhyou_shorui_flg
  , kingaku
  , hontai_kingaku
  , shouhizeigaku
  , zeiritsu
  , tekiyou
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , bikou
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kari_torihikisaki_cd
  , kari_torihikisaki_name_ryakushiki
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , kashi_torihikisaki_cd
  , kashi_torihikisaki_name_ryakushiki
  , kari_uf1_cd
  , kari_uf1_name_ryakushiki
  , kari_uf2_cd
  , kari_uf2_name_ryakushiki
  , kari_uf3_cd
  , kari_uf3_name_ryakushiki
  , kari_uf4_cd
  , kari_uf4_name_ryakushiki
  , kari_uf5_cd
  , kari_uf5_name_ryakushiki
  , kari_uf6_cd
  , kari_uf6_name_ryakushiki
  , kari_uf7_cd
  , kari_uf7_name_ryakushiki
  , kari_uf8_cd
  , kari_uf8_name_ryakushiki
  , kari_uf9_cd
  , kari_uf9_name_ryakushiki
  , kari_uf10_cd
  , kari_uf10_name_ryakushiki
  , kashi_uf1_cd
  , kashi_uf1_name_ryakushiki
  , kashi_uf2_cd
  , kashi_uf2_name_ryakushiki
  , kashi_uf3_cd
  , kashi_uf3_name_ryakushiki
  , kashi_uf4_cd
  , kashi_uf4_name_ryakushiki
  , kashi_uf5_cd
  , kashi_uf5_name_ryakushiki
  , kashi_uf6_cd
  , kashi_uf6_name_ryakushiki
  , kashi_uf7_cd
  , kashi_uf7_name_ryakushiki
  , kashi_uf8_cd
  , kashi_uf8_name_ryakushiki
  , kashi_uf9_cd
  , kashi_uf9_name_ryakushiki
  , kashi_uf10_cd
  , kashi_uf10_name_ryakushiki
  , kari_project_cd
  , kari_project_name
  , kari_segment_cd
  , kari_segment_name_ryakushiki
  , kashi_project_cd
  , kashi_project_name
  , kashi_segment_cd
  , kashi_segment_name_ryakushiki
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , denpyou_date
  , shouhyou_shorui_flg
  , kingaku
  , hontai_kingaku
  , shouhizeigaku
  , zeiritsu
  , tekiyou
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , bikou
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kari_torihikisaki_cd
  , kari_torihikisaki_name_ryakushiki
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , kashi_torihikisaki_cd
  , kashi_torihikisaki_name_ryakushiki
  , kari_uf1_cd
  , kari_uf1_name_ryakushiki
  , kari_uf2_cd
  , kari_uf2_name_ryakushiki
  , kari_uf3_cd
  , kari_uf3_name_ryakushiki
  , '' --   , kari_uf4_cd
  , '' --   , kari_uf4_name_ryakushiki
  , '' --   , kari_uf5_cd
  , '' --   , kari_uf5_name_ryakushiki
  , '' --   , kari_uf6_cd
  , '' --   , kari_uf6_name_ryakushiki
  , '' --   , kari_uf7_cd
  , '' --   , kari_uf7_name_ryakushiki
  , '' --   , kari_uf8_cd
  , '' --   , kari_uf8_name_ryakushiki
  , '' --   , kari_uf9_cd
  , '' --   , kari_uf9_name_ryakushiki
  , '' --   , kari_uf10_cd
  , '' --   , kari_uf10_name_ryakushiki
  , kashi_uf1_cd
  , kashi_uf1_name_ryakushiki
  , kashi_uf2_cd
  , kashi_uf2_name_ryakushiki
  , kashi_uf3_cd
  , kashi_uf3_name_ryakushiki
  , '' --   , kashi_uf4_cd
  , '' --   , kashi_uf4_name_ryakushiki
  , '' --   , kashi_uf5_cd
  , '' --   , kashi_uf5_name_ryakushiki
  , '' --   , kashi_uf6_cd
  , '' --   , kashi_uf6_name_ryakushiki
  , '' --   , kashi_uf7_cd
  , '' --   , kashi_uf7_name_ryakushiki
  , '' --   , kashi_uf8_cd
  , '' --   , kashi_uf8_name_ryakushiki
  , '' --   , kashi_uf9_cd
  , '' --   , kashi_uf9_name_ryakushiki
  , '' --   , kashi_uf10_cd
  , '' --   , kashi_uf10_name_ryakushiki
  , kari_project_cd
  , kari_project_name
  , '' --   , kari_segment_cd
  , '' --   , kari_segment_name_ryakushiki
  , kashi_project_cd
  , kashi_project_name
  , '' --   , kashi_segment_cd
  , '' --   , kashi_segment_name_ryakushiki
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  furikae_old;

-- �����t�֐\��
ALTER TABLE TSUKEKAE RENAME TO TSUKEKAE_OLD;
CREATE TABLE TSUKEKAE
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	DENPYOU_DATE DATE NOT NULL,
	SHOUHYOU_SHORUI_FLG VARCHAR(1) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	KINGAKU_GOUKEI DECIMAL(15) NOT NULL,
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HOSOKU VARCHAR(240) NOT NULL,
	TSUKEKAE_KBN VARCHAR(1) NOT NULL,
	MOTO_KAMOKU_CD VARCHAR(8) NOT NULL,
	MOTO_KAMOKU_NAME VARCHAR(22) NOT NULL,
	MOTO_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	MOTO_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	MOTO_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	MOTO_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	MOTO_TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	MOTO_TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	MOTO_KAZEI_KBN VARCHAR(3) NOT NULL,
	MOTO_UF1_CD VARCHAR(20) NOT NULL,
	MOTO_UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	MOTO_UF2_CD VARCHAR(20) NOT NULL,
	MOTO_UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	MOTO_UF3_CD VARCHAR(20) NOT NULL,
	MOTO_UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	MOTO_UF4_CD VARCHAR(20) NOT NULL,
	MOTO_UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	MOTO_UF5_CD VARCHAR(20) NOT NULL,
	MOTO_UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	MOTO_UF6_CD VARCHAR(20) NOT NULL,
	MOTO_UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	MOTO_UF7_CD VARCHAR(20) NOT NULL,
	MOTO_UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	MOTO_UF8_CD VARCHAR(20) NOT NULL,
	MOTO_UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	MOTO_UF9_CD VARCHAR(20) NOT NULL,
	MOTO_UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	MOTO_UF10_CD VARCHAR(20) NOT NULL,
	MOTO_UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	MOTO_PROJECT_CD VARCHAR(12) NOT NULL,
	MOTO_PROJECT_NAME VARCHAR(20) NOT NULL,
	MOTO_SEGMENT_CD VARCHAR(8) NOT NULL,
	MOTO_SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE TSUKEKAE IS '�t��';
COMMENT ON COLUMN TSUKEKAE.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN TSUKEKAE.DENPYOU_DATE IS '�`�[���t';
COMMENT ON COLUMN TSUKEKAE.SHOUHYOU_SHORUI_FLG IS '�؜ߏ��ރt���O';
COMMENT ON COLUMN TSUKEKAE.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN TSUKEKAE.KINGAKU_GOUKEI IS '���z���v';
COMMENT ON COLUMN TSUKEKAE.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN TSUKEKAE.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN TSUKEKAE.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN TSUKEKAE.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN TSUKEKAE.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN TSUKEKAE.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN TSUKEKAE.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN TSUKEKAE.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN TSUKEKAE.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN TSUKEKAE.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN TSUKEKAE.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN TSUKEKAE.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN TSUKEKAE.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN TSUKEKAE.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN TSUKEKAE.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN TSUKEKAE.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN TSUKEKAE.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN TSUKEKAE.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN TSUKEKAE.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN TSUKEKAE.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN TSUKEKAE.HOSOKU IS '�⑫';
COMMENT ON COLUMN TSUKEKAE.TSUKEKAE_KBN IS '�t�֋敪';
COMMENT ON COLUMN TSUKEKAE.MOTO_KAMOKU_CD IS '�t�֌��ȖڃR�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_KAMOKU_NAME IS '�t�֌��Ȗږ�';
COMMENT ON COLUMN TSUKEKAE.MOTO_KAMOKU_EDABAN_CD IS '�t�֌��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_KAMOKU_EDABAN_NAME IS '�t�֌��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN TSUKEKAE.MOTO_FUTAN_BUMON_CD IS '�t�֌����S����R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_FUTAN_BUMON_NAME IS '�t�֌����S���喼';
COMMENT ON COLUMN TSUKEKAE.MOTO_TORIHIKISAKI_CD IS '�t�֌������R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_TORIHIKISAKI_NAME_RYAKUSHIKI IS '�t�֌�����於�i�����j';
COMMENT ON COLUMN TSUKEKAE.MOTO_KAZEI_KBN IS '�t�֌��ېŋ敪';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF1_CD IS '�t�֌�UF1�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF1_NAME_RYAKUSHIKI IS '�t�֌�UF1���i�����j';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF2_CD IS '�t�֌�UF2�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF2_NAME_RYAKUSHIKI IS '�t�֌�UF2���i�����j';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF3_CD IS '�t�֌�UF3�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF3_NAME_RYAKUSHIKI IS '�t�֌�UF3���i�����j';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF4_CD IS '�t�֌�UF4�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF4_NAME_RYAKUSHIKI IS '�t�֌�UF4���i�����j';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF5_CD IS '�t�֌�UF5�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF5_NAME_RYAKUSHIKI IS '�t�֌�UF5���i�����j';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF6_CD IS '�t�֌�UF6�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF6_NAME_RYAKUSHIKI IS '�t�֌�UF6���i�����j';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF7_CD IS '�t�֌�UF7�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF7_NAME_RYAKUSHIKI IS '�t�֌�UF7���i�����j';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF8_CD IS '�t�֌�UF8�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF8_NAME_RYAKUSHIKI IS '�t�֌�UF8���i�����j';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF9_CD IS '�t�֌�UF9�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF9_NAME_RYAKUSHIKI IS '�t�֌�UF9���i�����j';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF10_CD IS '�t�֌�UF10�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_UF10_NAME_RYAKUSHIKI IS '�t�֌�UF10���i�����j';
COMMENT ON COLUMN TSUKEKAE.MOTO_PROJECT_CD IS '�t�֌��v���W�F�N�g�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_PROJECT_NAME IS '�t�֌��v���W�F�N�g��';
COMMENT ON COLUMN TSUKEKAE.MOTO_SEGMENT_CD IS '�t�֌��Z�O�����g�R�[�h';
COMMENT ON COLUMN TSUKEKAE.MOTO_SEGMENT_NAME_RYAKUSHIKI IS '�t�֌��Z�O�����g���i�����j';
COMMENT ON COLUMN TSUKEKAE.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN TSUKEKAE.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN TSUKEKAE.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN TSUKEKAE.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO tsukekae( 
  denpyou_id
  , denpyou_date
  , shouhyou_shorui_flg
  , zeiritsu
  , kingaku_goukei
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , hosoku
  , tsukekae_kbn
  , moto_kamoku_cd
  , moto_kamoku_name
  , moto_kamoku_edaban_cd
  , moto_kamoku_edaban_name
  , moto_futan_bumon_cd
  , moto_futan_bumon_name
  , moto_torihikisaki_cd
  , moto_torihikisaki_name_ryakushiki
  , moto_kazei_kbn
  , moto_uf1_cd
  , moto_uf1_name_ryakushiki
  , moto_uf2_cd
  , moto_uf2_name_ryakushiki
  , moto_uf3_cd
  , moto_uf3_name_ryakushiki
  , moto_uf4_cd
  , moto_uf4_name_ryakushiki
  , moto_uf5_cd
  , moto_uf5_name_ryakushiki
  , moto_uf6_cd
  , moto_uf6_name_ryakushiki
  , moto_uf7_cd
  , moto_uf7_name_ryakushiki
  , moto_uf8_cd
  , moto_uf8_name_ryakushiki
  , moto_uf9_cd
  , moto_uf9_name_ryakushiki
  , moto_uf10_cd
  , moto_uf10_name_ryakushiki
  , moto_project_cd
  , moto_project_name
  , moto_segment_cd
  , moto_segment_name_ryakushiki
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , denpyou_date
  , shouhyou_shorui_flg
  , zeiritsu
  , kingaku_goukei
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , hosoku
  , tsukekae_kbn
  , moto_kamoku_cd
  , moto_kamoku_name
  , moto_kamoku_edaban_cd
  , moto_kamoku_edaban_name
  , moto_futan_bumon_cd
  , moto_futan_bumon_name
  , moto_torihikisaki_cd
  , moto_torihikisaki_name_ryakushiki
  , moto_kazei_kbn
  , moto_uf1_cd
  , moto_uf1_name_ryakushiki
  , moto_uf2_cd
  , moto_uf2_name_ryakushiki
  , moto_uf3_cd
  , moto_uf3_name_ryakushiki
  , '' --   , moto_uf4_cd
  , '' --   , moto_uf4_name_ryakushiki
  , '' --   , moto_uf5_cd
  , '' --   , moto_uf5_name_ryakushiki
  , '' --   , moto_uf6_cd
  , '' --   , moto_uf6_name_ryakushiki
  , '' --   , moto_uf7_cd
  , '' --   , moto_uf7_name_ryakushiki
  , '' --   , moto_uf8_cd
  , '' --   , moto_uf8_name_ryakushiki
  , '' --   , moto_uf9_cd
  , '' --   , moto_uf9_name_ryakushiki
  , '' --   , moto_uf10_cd
  , '' --   , moto_uf10_name_ryakushiki
  , moto_project_cd
  , moto_project_name
  , '' --   , moto_segment_cd
  , '' --   , moto_segment_name_ryakushiki
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  tsukekae_old;
DROP TABLE tsukekae_old;

-- �����t�֖���
ALTER TABLE TSUKEKAE_MEISAI RENAME TO TSUKEKAE_MEISAI_OLD;
CREATE TABLE TSUKEKAE_MEISAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	DENPYOU_EDANO INT NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	KINGAKU DECIMAL(15) NOT NULL,
	HONTAI_KINGAKU DECIMAL(15),
	SHOUHIZEIGAKU DECIMAL(15),
	BIKOU VARCHAR(40) NOT NULL,
	SAKI_KAMOKU_CD VARCHAR(6) NOT NULL,
	SAKI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	SAKI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	SAKI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	SAKI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	SAKI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	SAKI_TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	SAKI_TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SAKI_KAZEI_KBN VARCHAR(3) NOT NULL,
	SAKI_UF1_CD VARCHAR(20) NOT NULL,
	SAKI_UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SAKI_UF2_CD VARCHAR(20) NOT NULL,
	SAKI_UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SAKI_UF3_CD VARCHAR(20) NOT NULL,
	SAKI_UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SAKI_UF4_CD VARCHAR(20) NOT NULL,
	SAKI_UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SAKI_UF5_CD VARCHAR(20) NOT NULL,
	SAKI_UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SAKI_UF6_CD VARCHAR(20) NOT NULL,
	SAKI_UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SAKI_UF7_CD VARCHAR(20) NOT NULL,
	SAKI_UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SAKI_UF8_CD VARCHAR(20) NOT NULL,
	SAKI_UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SAKI_UF9_CD VARCHAR(20) NOT NULL,
	SAKI_UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SAKI_UF10_CD VARCHAR(20) NOT NULL,
	SAKI_UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	SAKI_PROJECT_CD VARCHAR(12) NOT NULL,
	SAKI_PROJECT_NAME VARCHAR(20) NOT NULL,
	SAKI_SEGMENT_CD VARCHAR(8) NOT NULL,
	SAKI_SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID, DENPYOU_EDANO)
) WITHOUT OIDS;
COMMENT ON TABLE TSUKEKAE_MEISAI IS '�t�֖���';
COMMENT ON COLUMN TSUKEKAE_MEISAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN TSUKEKAE_MEISAI.DENPYOU_EDANO IS '�`�[�}�ԍ�';
COMMENT ON COLUMN TSUKEKAE_MEISAI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN TSUKEKAE_MEISAI.KINGAKU IS '���z';
COMMENT ON COLUMN TSUKEKAE_MEISAI.HONTAI_KINGAKU IS '�{�̋��z';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SHOUHIZEIGAKU IS '����Ŋz';
COMMENT ON COLUMN TSUKEKAE_MEISAI.BIKOU IS '���l�i��v�`�[�j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_KAMOKU_CD IS '�t�֐�ȖڃR�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_KAMOKU_NAME IS '�t�֐�Ȗږ�';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_KAMOKU_EDABAN_CD IS '�t�֐�Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_KAMOKU_EDABAN_NAME IS '�t�֐�Ȗڎ}�Ԗ�';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_FUTAN_BUMON_CD IS '�t�֐敉�S����R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_FUTAN_BUMON_NAME IS '�t�֐敉�S���喼';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_TORIHIKISAKI_CD IS '�t�֐�����R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_TORIHIKISAKI_NAME_RYAKUSHIKI IS '�t�֐����於�i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_KAZEI_KBN IS '�t�֐�ېŋ敪';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF1_CD IS '�t�֐�UF1�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF1_NAME_RYAKUSHIKI IS '�t�֐�UF1���i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF2_CD IS '�t�֐�UF2�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF2_NAME_RYAKUSHIKI IS '�t�֐�UF2���i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF3_CD IS '�t�֐�UF3�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF3_NAME_RYAKUSHIKI IS '�t�֐�UF3���i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF4_CD IS '�t�֐�UF4�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF4_NAME_RYAKUSHIKI IS '�t�֐�UF4���i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF5_CD IS '�t�֐�UF5�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF5_NAME_RYAKUSHIKI IS '�t�֐�UF5���i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF6_CD IS '�t�֐�UF6�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF6_NAME_RYAKUSHIKI IS '�t�֐�UF6���i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF7_CD IS '�t�֐�UF7�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF7_NAME_RYAKUSHIKI IS '�t�֐�UF7���i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF8_CD IS '�t�֐�UF8�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF8_NAME_RYAKUSHIKI IS '�t�֐�UF8���i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF9_CD IS '�t�֐�UF9�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF9_NAME_RYAKUSHIKI IS '�t�֐�UF9���i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF10_CD IS '�t�֐�UF10�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_UF10_NAME_RYAKUSHIKI IS '�t�֐�UF10���i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_PROJECT_CD IS '�t�֐�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_PROJECT_NAME IS '�t�֐�v���W�F�N�g��';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_SEGMENT_CD IS '�t�֐�Z�O�����g�R�[�h';
COMMENT ON COLUMN TSUKEKAE_MEISAI.SAKI_SEGMENT_NAME_RYAKUSHIKI IS '�t�֐�Z�O�����g���i�����j';
COMMENT ON COLUMN TSUKEKAE_MEISAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN TSUKEKAE_MEISAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN TSUKEKAE_MEISAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN TSUKEKAE_MEISAI.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO tsukekae_meisai( 
  denpyou_id
  , denpyou_edano
  , tekiyou
  , kingaku
  , hontai_kingaku
  , shouhizeigaku
  , bikou
  , saki_kamoku_cd
  , saki_kamoku_name
  , saki_kamoku_edaban_cd
  , saki_kamoku_edaban_name
  , saki_futan_bumon_cd
  , saki_futan_bumon_name
  , saki_torihikisaki_cd
  , saki_torihikisaki_name_ryakushiki
  , saki_kazei_kbn
  , saki_uf1_cd
  , saki_uf1_name_ryakushiki
  , saki_uf2_cd
  , saki_uf2_name_ryakushiki
  , saki_uf3_cd
  , saki_uf3_name_ryakushiki
  , saki_uf4_cd
  , saki_uf4_name_ryakushiki
  , saki_uf5_cd
  , saki_uf5_name_ryakushiki
  , saki_uf6_cd
  , saki_uf6_name_ryakushiki
  , saki_uf7_cd
  , saki_uf7_name_ryakushiki
  , saki_uf8_cd
  , saki_uf8_name_ryakushiki
  , saki_uf9_cd
  , saki_uf9_name_ryakushiki
  , saki_uf10_cd
  , saki_uf10_name_ryakushiki
  , saki_project_cd
  , saki_project_name
  , saki_segment_cd
  , saki_segment_name_ryakushiki
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , denpyou_edano
  , tekiyou
  , kingaku
  , hontai_kingaku
  , shouhizeigaku
  , bikou
  , saki_kamoku_cd
  , saki_kamoku_name
  , saki_kamoku_edaban_cd
  , saki_kamoku_edaban_name
  , saki_futan_bumon_cd
  , saki_futan_bumon_name
  , saki_torihikisaki_cd
  , saki_torihikisaki_name_ryakushiki
  , saki_kazei_kbn
  , saki_uf1_cd
  , saki_uf1_name_ryakushiki
  , saki_uf2_cd
  , saki_uf2_name_ryakushiki
  , saki_uf3_cd
  , saki_uf3_name_ryakushiki
  , '' --   , saki_uf4_cd
  , '' --   , saki_uf4_name_ryakushiki
  , '' --   , saki_uf5_cd
  , '' --   , saki_uf5_name_ryakushiki
  , '' --   , saki_uf6_cd
  , '' --   , saki_uf6_name_ryakushiki
  , '' --   , saki_uf7_cd
  , '' --   , saki_uf7_name_ryakushiki
  , '' --   , saki_uf8_cd
  , '' --   , saki_uf8_name_ryakushiki
  , '' --   , saki_uf9_cd
  , '' --   , saki_uf9_name_ryakushiki
  , '' --   , saki_uf10_cd
  , '' --   , saki_uf10_name_ryakushiki
  , saki_project_cd
  , saki_project_name
  , '' --   , saki_segment_cd
  , '' --   , saki_segment_name_ryakushiki
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  tsukekae_meisai_old;
DROP TABLE tsukekae_meisai_old;

-- ��������
ALTER TABLE JIDOUHIKIOTOSHI RENAME TO JIDOUHIKIOTOSHI_OLD;
CREATE TABLE JIDOUHIKIOTOSHI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	KEIJOUBI DATE,
	HIKIOTOSHIBI DATE NOT NULL,
	HONTAI_KINGAKU_GOUKEI DECIMAL(15) NOT NULL,
	SHOUHIZEIGAKU_GOUKEI DECIMAL(15) NOT NULL,
	SHIHARAI_KINGAKU_GOUKEI DECIMAL(15) NOT NULL,
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HOSOKU VARCHAR(240) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE JIDOUHIKIOTOSHI IS '��������';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.KEIJOUBI IS '�v���';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HIKIOTOSHIBI IS '������';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HONTAI_KINGAKU_GOUKEI IS '�{�̋��z���v';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.SHOUHIZEIGAKU_GOUKEI IS '����Ŋz���v';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.SHIHARAI_KINGAKU_GOUKEI IS '�x�����z���v';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.HOSOKU IS '�⑫';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN JIDOUHIKIOTOSHI.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO jidouhikiotoshi( 
  denpyou_id
  , keijoubi
  , hikiotoshibi
  , hontai_kingaku_goukei
  , shouhizeigaku_goukei
  , shiharai_kingaku_goukei
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , hosoku
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , keijoubi
  , hikiotoshibi
  , hontai_kingaku_goukei
  , shouhizeigaku_goukei
  , shiharai_kingaku_goukei
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , hosoku
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  jidouhikiotoshi_old;
DROP TABLE jidouhikiotoshi_old;

-- ������������
ALTER TABLE JIDOUHIKIOTOSHI_MEISAI RENAME TO JIDOUHIKIOTOSHI_MEISAI_OLD;
CREATE TABLE JIDOUHIKIOTOSHI_MEISAI
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	DENPYOU_EDANO INT NOT NULL,
	SHIWAKE_EDANO INT NOT NULL,
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	SHIHARAI_KINGAKU DECIMAL(15) NOT NULL,
	HONTAI_KINGAKU DECIMAL(15),
	SHOUHIZEIGAKU DECIMAL(15),
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID, DENPYOU_EDANO)
) WITHOUT OIDS;
COMMENT ON TABLE JIDOUHIKIOTOSHI_MEISAI IS '������������';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.DENPYOU_EDANO IS '�`�[�}�ԍ�';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.SHIHARAI_KINGAKU IS '�x�����z';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.HONTAI_KINGAKU IS '�{�̋��z';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.SHOUHIZEIGAKU IS '����Ŋz';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN JIDOUHIKIOTOSHI_MEISAI.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO jidouhikiotoshi_meisai( 
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , torihiki_name
  , tekiyou
  , zeiritsu
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , denpyou_edano
  , shiwake_edano
  , torihiki_name
  , tekiyou
  , zeiritsu
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , project_cd
  , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  jidouhikiotoshi_meisai_old;
DROP TABLE jidouhikiotoshi_meisai_old;

-- ��ʔ�Z
ALTER TABLE KOUTSUUHISEISAN RENAME TO KOUTSUUHISEISAN_OLD;
CREATE TABLE KOUTSUUHISEISAN
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	MOKUTEKI VARCHAR(240) NOT NULL,
	SEISANKIKAN_FROM DATE,
	SEISANKIKAN_FROM_HOUR VARCHAR(2),
	SEISANKIKAN_FROM_MIN VARCHAR(2),
	SEISANKIKAN_TO DATE,
	SEISANKIKAN_TO_HOUR VARCHAR(2),
	SEISANKIKAN_TO_MIN VARCHAR(2),
	KEIJOUBI DATE,
	SHIHARAIBI DATE,
	SHIHARAIKIBOUBI DATE,
	SHIHARAIHOUHOU VARCHAR(1) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	GOUKEI_KINGAKU DECIMAL(15) NOT NULL,
	HOUJIN_CARD_RIYOU_KINGAKU DECIMAL(15) NOT NULL,
	KAISHA_TEHAI_KINGAKU DECIMAL(15) NOT NULL,
	SASHIHIKI_SHIKYUU_KINGAKU DECIMAL(15) NOT NULL,
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF4_CD VARCHAR(20) NOT NULL,
	HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF5_CD VARCHAR(20) NOT NULL,
	HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF6_CD VARCHAR(20) NOT NULL,
	HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF7_CD VARCHAR(20) NOT NULL,
	HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF8_CD VARCHAR(20) NOT NULL,
	HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF9_CD VARCHAR(20) NOT NULL,
	HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF10_CD VARCHAR(20) NOT NULL,
	HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HOSOKU VARCHAR(240) NOT NULL,
	SHIWAKE_EDANO INT,
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF4_CD VARCHAR(20) NOT NULL,
	UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF5_CD VARCHAR(20) NOT NULL,
	UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF6_CD VARCHAR(20) NOT NULL,
	UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF7_CD VARCHAR(20) NOT NULL,
	UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF8_CD VARCHAR(20) NOT NULL,
	UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF9_CD VARCHAR(20) NOT NULL,
	UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF10_CD VARCHAR(20) NOT NULL,
	UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	SEGMENT_CD VARCHAR(8) NOT NULL,
	SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE KOUTSUUHISEISAN IS '��ʔ�Z';
COMMENT ON COLUMN KOUTSUUHISEISAN.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN KOUTSUUHISEISAN.MOKUTEKI IS '�ړI';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_FROM IS '���Z���ԊJ�n��';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_FROM_HOUR IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_FROM_MIN IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_TO IS '���Z���ԏI����';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_TO_HOUR IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_TO_MIN IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN KOUTSUUHISEISAN.KEIJOUBI IS '�v���';
COMMENT ON COLUMN KOUTSUUHISEISAN.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN KOUTSUUHISEISAN.SHIHARAIKIBOUBI IS '�x����]��';
COMMENT ON COLUMN KOUTSUUHISEISAN.SHIHARAIHOUHOU IS '�x�����@';
COMMENT ON COLUMN KOUTSUUHISEISAN.TEKIYOU IS '�E�v';
COMMENT ON COLUMN KOUTSUUHISEISAN.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.GOUKEI_KINGAKU IS '���v���z';
COMMENT ON COLUMN KOUTSUUHISEISAN.HOUJIN_CARD_RIYOU_KINGAKU IS '���@�l�J�[�h���p���v';
COMMENT ON COLUMN KOUTSUUHISEISAN.KAISHA_TEHAI_KINGAKU IS '��Ў�z���v';
COMMENT ON COLUMN KOUTSUUHISEISAN.SASHIHIKI_SHIKYUU_KINGAKU IS '�����x�����z';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HOSOKU IS '�⑫';
COMMENT ON COLUMN KOUTSUUHISEISAN.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN KOUTSUUHISEISAN.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN KOUTSUUHISEISAN.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN KOUTSUUHISEISAN.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN KOUTSUUHISEISAN.KOUSHIN_TIME IS '�X�V����';
INSERT 
INTO koutsuuhiseisan( 
  denpyou_id
  , mokuteki
  , seisankikan_from
  , seisankikan_from_hour
  , seisankikan_from_min
  , seisankikan_to
  , seisankikan_to_hour
  , seisankikan_to_min
  , keijoubi
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , zeiritsu
  , goukei_kingaku
  , houjin_card_riyou_kingaku
  , kaisha_tehai_kingaku
  , sashihiki_shikyuu_kingaku
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , hf4_cd
  , hf4_name_ryakushiki
  , hf5_cd
  , hf5_name_ryakushiki
  , hf6_cd
  , hf6_name_ryakushiki
  , hf7_cd
  , hf7_name_ryakushiki
  , hf8_cd
  , hf8_name_ryakushiki
  , hf9_cd
  , hf9_name_ryakushiki
  , hf10_cd
  , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
    denpyou_id
  , mokuteki
  , seisankikan_from
  , seisankikan_from_hour
  , seisankikan_from_min
  , seisankikan_to
  , seisankikan_to_hour
  , seisankikan_to_min
  , keijoubi
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , tekiyou
  , zeiritsu
  , goukei_kingaku
  , houjin_card_riyou_kingaku
  , kaisha_tehai_kingaku
  , sashihiki_shikyuu_kingaku
  , hf1_cd
  , hf1_name_ryakushiki
  , hf2_cd
  , hf2_name_ryakushiki
  , hf3_cd
  , hf3_name_ryakushiki
  , '' --   , hf4_cd
  , '' --   , hf4_name_ryakushiki
  , '' --   , hf5_cd
  , '' --   , hf5_name_ryakushiki
  , '' --   , hf6_cd
  , '' --   , hf6_name_ryakushiki
  , '' --   , hf7_cd
  , '' --   , hf7_name_ryakushiki
  , '' --   , hf8_cd
  , '' --   , hf8_name_ryakushiki
  , '' --   , hf9_cd
  , '' --   , hf9_name_ryakushiki
  , '' --   , hf10_cd
  , '' --   , hf10_name_ryakushiki
  , hosoku
  , shiwake_edano
  , torihiki_name
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , '' --   , torihikisaki_cd
  , '' --   , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , '' --   , uf4_cd
  , '' --   , uf4_name_ryakushiki
  , '' --   , uf5_cd
  , '' --   , uf5_name_ryakushiki
  , '' --   , uf6_cd
  , '' --   , uf6_name_ryakushiki
  , '' --   , uf7_cd
  , '' --   , uf7_name_ryakushiki
  , '' --   , uf8_cd
  , '' --   , uf8_name_ryakushiki
  , '' --   , uf9_cd
  , '' --   , uf9_name_ryakushiki
  , '' --   , uf10_cd
  , '' --   , uf10_name_ryakushiki
  , project_cd
  , project_name
  , '' --   , segment_cd
  , '' --   , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  koutsuuhiseisan_old;
DROP TABLE koutsuuhiseisan_old;


--0474_�����\�����Z�������J�����ւ̃f�[�^�o�^
--�������F�������̃f�[�^��o�^
--�����\��
UPDATE karibarai kr 
SET seisan_kanryoubi = sjt.ttdate 
FROM keihiseisan ks 
 INNER JOIN denpyou dp ON ks.denpyou_id = dp.denpyou_id AND dp.denpyou_joutai = '30' 
 INNER JOIN (SELECT sj.denpyou_id, CAST(sj.touroku_time AS DATE) AS ttdate FROM shounin_joukyou sj 
             INNER JOIN (SELECT denpyou_id, MAX(edano) AS maxeda FROM shounin_joukyou WHERE joukyou_cd = '4' GROUP BY denpyou_id) sb 
                         ON sj.denpyou_id = sb.denpyou_id AND sj.edano = sb.maxeda) sjt 
             ON dp.denpyou_id = sjt.denpyou_id 
WHERE kr.denpyou_id = ks.karibarai_denpyou_id;

--������\��
UPDATE ryohi_karibarai kr 
SET seisan_kanryoubi = sjt.ttdate 
FROM ryohiseisan ks 
 INNER JOIN denpyou dp ON ks.denpyou_id = dp.denpyou_id AND dp.denpyou_joutai = '30' 
 INNER JOIN (SELECT sj.denpyou_id, CAST(sj.touroku_time AS DATE) AS ttdate FROM shounin_joukyou sj 
             INNER JOIN (SELECT denpyou_id, MAX(edano) AS maxeda FROM shounin_joukyou WHERE joukyou_cd = '4' GROUP BY denpyou_id) sb 
                         ON sj.denpyou_id = sb.denpyou_id AND sj.edano = sb.maxeda) sjt 
             ON dp.denpyou_id = sjt.denpyou_id 
WHERE kr.denpyou_id = ks.karibarai_denpyou_id;

--�C�O������\��
UPDATE kaigai_ryohi_karibarai kr 
SET seisan_kanryoubi = sjt.ttdate 
FROM kaigai_ryohiseisan ks 
 INNER JOIN denpyou dp ON ks.denpyou_id = dp.denpyou_id AND dp.denpyou_joutai = '30' 
 INNER JOIN (SELECT sj.denpyou_id, CAST(sj.touroku_time AS DATE) AS ttdate FROM shounin_joukyou sj 
             INNER JOIN (SELECT denpyou_id, MAX(edano) AS maxeda FROM shounin_joukyou WHERE joukyou_cd = '4' GROUP BY denpyou_id) sb 
                         ON sj.denpyou_id = sb.denpyou_id AND sj.edano = sb.maxeda) sjt 
             ON dp.denpyou_id = sjt.denpyou_id 
WHERE kr.denpyou_id = ks.karibarai_denpyou_id;


--508_���t���̓J�����_�[���i�̓y���j���F�ς�
CREATE TABLE SHUKUJITSU_MASTER
(
	SHUKUJITSU DATE NOT NULL,
	SHUKUJITSU_NAME VARCHAR(30) NOT NULL,
	PRIMARY KEY (SHUKUJITSU)
) WITHOUT OIDS;
COMMENT ON TABLE SHUKUJITSU_MASTER IS '�j���}�X�^�[';
COMMENT ON COLUMN SHUKUJITSU_MASTER.SHUKUJITSU IS '�j��';
COMMENT ON COLUMN SHUKUJITSU_MASTER.SHUKUJITSU_NAME IS '�j����';


--�x���˗��֘A
CREATE TABLE MOTO_KOUZA_SHIHARAIIRAI
(
	MOTO_KINYUUKIKAN_CD VARCHAR(4) NOT NULL,
	MOTO_KINYUUKIKAN_SHITEN_CD VARCHAR(3) NOT NULL,
	MOTO_YOKINSHUBETSU VARCHAR(1) NOT NULL,
	MOTO_KOUZA_BANGOU VARCHAR(7) NOT NULL,
	MOTO_KINYUUKIKAN_NAME_HANKANA VARCHAR(15) NOT NULL,
	MOTO_KINYUUKIKAN_SHITEN_NAME_HANKANA VARCHAR(15) NOT NULL,
	SHUBETSU_CD VARCHAR(2) NOT NULL,
	CD_KBN VARCHAR(1) NOT NULL,
	KAISHA_CD VARCHAR(10) NOT NULL,
	KAISHA_NAME_HANKANA VARCHAR(40) NOT NULL,
	SHINKI_CD VARCHAR(1) NOT NULL,
	FURIKOMI_KBN VARCHAR(1) NOT NULL,
	PRIMARY KEY (MOTO_KINYUUKIKAN_CD, MOTO_KINYUUKIKAN_SHITEN_CD, MOTO_YOKINSHUBETSU, MOTO_KOUZA_BANGOU)
) WITHOUT OIDS;
COMMENT ON TABLE MOTO_KOUZA_SHIHARAIIRAI IS '�U���������i�x���˗��j';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.MOTO_KINYUUKIKAN_CD IS '�U�������Z�@�փR�[�h';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.MOTO_KINYUUKIKAN_SHITEN_CD IS '�U�������Z�@�֎x�X�R�[�h';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.MOTO_YOKINSHUBETSU IS '�U�����a�����';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.MOTO_KOUZA_BANGOU IS '�U���������ԍ�';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.MOTO_KINYUUKIKAN_NAME_HANKANA IS '�U�������Z�@�֖��i���p�J�i�j';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.MOTO_KINYUUKIKAN_SHITEN_NAME_HANKANA IS '�U�������Z�@�֎x�X���i���p�J�i�j';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.SHUBETSU_CD IS '��ʃR�[�h';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.CD_KBN IS '�R�[�h�敪';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.KAISHA_CD IS '��ЃR�[�h';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.KAISHA_NAME_HANKANA IS '��Ж��i���p�J�i�j';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.SHINKI_CD IS '�V�K�R�[�h';
COMMENT ON COLUMN MOTO_KOUZA_SHIHARAIIRAI.FURIKOMI_KBN IS '�U���敪';

CREATE TABLE OPEN21_KINYUUKIKAN
(
	KINYUUKIKAN_CD VARCHAR(4) NOT NULL,
	KINYUUKIKAN_SHITEN_CD VARCHAR(3) NOT NULL,
	KINYUUKIKAN_NAME_HANKANA VARCHAR(15) NOT NULL,
	KINYUUKIKAN_NAME_KANA VARCHAR(30) NOT NULL,
	SHITEN_NAME_HANKANA VARCHAR(15) NOT NULL,
	SHITEN_NAME_KANA VARCHAR(30) NOT NULL,
	PRIMARY KEY (KINYUUKIKAN_CD, KINYUUKIKAN_SHITEN_CD)
) WITHOUT OIDS;
COMMENT ON TABLE OPEN21_KINYUUKIKAN IS '���Z�@��';
COMMENT ON COLUMN OPEN21_KINYUUKIKAN.KINYUUKIKAN_CD IS '���Z�@�փR�[�h';
COMMENT ON COLUMN OPEN21_KINYUUKIKAN.KINYUUKIKAN_SHITEN_CD IS '���Z�@�֎x�X�R�[�h';
COMMENT ON COLUMN OPEN21_KINYUUKIKAN.KINYUUKIKAN_NAME_HANKANA IS '���Z�@�֖��i���p�J�i�j';
COMMENT ON COLUMN OPEN21_KINYUUKIKAN.KINYUUKIKAN_NAME_KANA IS '���Z�@�֖�';
COMMENT ON COLUMN OPEN21_KINYUUKIKAN.SHITEN_NAME_HANKANA IS '�x�X���i���p�J�i�j';
COMMENT ON COLUMN OPEN21_KINYUUKIKAN.SHITEN_NAME_KANA IS '�x�X��';


ALTER TABLE torihikisaki_master RENAME TO torihikisaki_master_tmp;
CREATE TABLE TORIHIKISAKI_MASTER
(
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	TORIHIKISAKI_NAME_SEISHIKI VARCHAR(60) NOT NULL,
	TORIHIKISAKI_NAME_HANKANA VARCHAR(4) NOT NULL,
	PRIMARY KEY (TORIHIKISAKI_CD)
) WITHOUT OIDS;
COMMENT ON TABLE TORIHIKISAKI_MASTER IS '�����}�X�^�[';
COMMENT ON COLUMN TORIHIKISAKI_MASTER.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN TORIHIKISAKI_MASTER.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN TORIHIKISAKI_MASTER.TORIHIKISAKI_NAME_SEISHIKI IS '����於�i�����j';
COMMENT ON COLUMN TORIHIKISAKI_MASTER.TORIHIKISAKI_NAME_HANKANA IS '����於�i���p�J�i�j';
INSERT INTO torihikisaki_master
SELECT
	*
	, '' --TORIHIKISAKI_NAME_HANKANA ����於�i���p�J�i�j
FROM torihikisaki_master_tmp;
DROP TABLE torihikisaki_master_tmp;

ALTER TABLE torihikisaki RENAME TO torihikisaki_tmp;
CREATE TABLE TORIHIKISAKI
(
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	YUUBIN_BANGOU VARCHAR(7) NOT NULL,
	JUUSHO1 VARCHAR(40) NOT NULL,
	JUUSHO2 VARCHAR(40) NOT NULL,
	TELNO VARCHAR(20) NOT NULL,
	FAXNO VARCHAR(20) NOT NULL,
	KOUZA_MEIGININ VARCHAR(44) NOT NULL,
	KOUZA_MEIGININ_FURIGANA VARCHAR(40) NOT NULL,
	SHIHARAI_SHUBETSU SMALLINT,
	YOKIN_SHUBETSU VARCHAR(1) NOT NULL,
	KOUZA_BANGOU VARCHAR(7) NOT NULL,
	TESUURYOU_FUTAN_KBN SMALLINT,
	FURIKOMI_KBN VARCHAR(1) NOT NULL,
	FURIKOMI_GINKOU_CD VARCHAR(4) NOT NULL,
	FURIKOMI_GINKOU_SHITEN_CD VARCHAR(3) NOT NULL,
	SHIHARAIBI SMALLINT,
	SHIHARAI_KIJITSU SMALLINT,
	YAKUJOU_KINGAKU DECIMAL(15),
	TORIHIKISAKI_NAME_HANKANA VARCHAR(4) NOT NULL,
	SBUSYO VARCHAR NOT NULL,
	STANTO VARCHAR NOT NULL,
	KEICD SMALLINT,
	NAYOSE SMALLINT NOT NULL,
	F_SETUIN SMALLINT,
	STAN VARCHAR NOT NULL,
	SBCOD VARCHAR NOT NULL,
	SKICD VARCHAR NOT NULL,
	F_SOUFU SMALLINT,
	ANNAI SMALLINT,
	TSOKBN SMALLINT,
	F_SHITU SMALLINT,
	CDM2 VARCHAR NOT NULL,
	DM1 VARCHAR NOT NULL,
	DM2 SMALLINT,
	DM3 INT,
	GENDO DECIMAL(15),
	PRIMARY KEY (TORIHIKISAKI_CD)
) WITHOUT OIDS;
COMMENT ON TABLE TORIHIKISAKI IS '�����';
COMMENT ON COLUMN TORIHIKISAKI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN TORIHIKISAKI.YUUBIN_BANGOU IS '�X�֔ԍ�';
COMMENT ON COLUMN TORIHIKISAKI.JUUSHO1 IS '�Z���P';
COMMENT ON COLUMN TORIHIKISAKI.JUUSHO2 IS '�Z���Q';
COMMENT ON COLUMN TORIHIKISAKI.TELNO IS '�d�b�ԍ�';
COMMENT ON COLUMN TORIHIKISAKI.FAXNO IS 'FAX�ԍ�';
COMMENT ON COLUMN TORIHIKISAKI.KOUZA_MEIGININ IS '�������`�l';
COMMENT ON COLUMN TORIHIKISAKI.KOUZA_MEIGININ_FURIGANA IS '�������`�l�ӂ艼��';
COMMENT ON COLUMN TORIHIKISAKI.SHIHARAI_SHUBETSU IS '�x�����';
COMMENT ON COLUMN TORIHIKISAKI.YOKIN_SHUBETSU IS '�a�����';
COMMENT ON COLUMN TORIHIKISAKI.KOUZA_BANGOU IS '�����ԍ�';
COMMENT ON COLUMN TORIHIKISAKI.TESUURYOU_FUTAN_KBN IS '�萔�����S�敪';
COMMENT ON COLUMN TORIHIKISAKI.FURIKOMI_KBN IS '�U���敪';
COMMENT ON COLUMN TORIHIKISAKI.FURIKOMI_GINKOU_CD IS '�U����s�R�[�h';
COMMENT ON COLUMN TORIHIKISAKI.FURIKOMI_GINKOU_SHITEN_CD IS '�U����s�x�X�R�[�h';
COMMENT ON COLUMN TORIHIKISAKI.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN TORIHIKISAKI.SHIHARAI_KIJITSU IS '�x������';
COMMENT ON COLUMN TORIHIKISAKI.YAKUJOU_KINGAKU IS '�����z';
COMMENT ON COLUMN TORIHIKISAKI.TORIHIKISAKI_NAME_HANKANA IS '����於�i���p�J�i�j';
COMMENT ON COLUMN TORIHIKISAKI.SBUSYO IS '����敔��';
COMMENT ON COLUMN TORIHIKISAKI.STANTO IS '�����S����';
COMMENT ON COLUMN TORIHIKISAKI.KEICD IS '�h��';
COMMENT ON COLUMN TORIHIKISAKI.NAYOSE IS '���񂹃t���O';
COMMENT ON COLUMN TORIHIKISAKI.F_SETUIN IS '�߈���s�t���O';
COMMENT ON COLUMN TORIHIKISAKI.STAN IS '��S����';
COMMENT ON COLUMN TORIHIKISAKI.SBCOD IS '����R�[�h';
COMMENT ON COLUMN TORIHIKISAKI.SKICD IS '�ȖڃR�[�h';
COMMENT ON COLUMN TORIHIKISAKI.F_SOUFU IS '���t�ē�';
COMMENT ON COLUMN TORIHIKISAKI.ANNAI IS '�ē���';
COMMENT ON COLUMN TORIHIKISAKI.TSOKBN IS '�������S�敪';
COMMENT ON COLUMN TORIHIKISAKI.F_SHITU IS '�x���ʒm';
COMMENT ON COLUMN TORIHIKISAKI.CDM2 IS '�d����ԍ�';
COMMENT ON COLUMN TORIHIKISAKI.DM1 IS '�⏕�R�[�h�P';
COMMENT ON COLUMN TORIHIKISAKI.DM2 IS '�⏕�R�[�h�Q';
COMMENT ON COLUMN TORIHIKISAKI.DM3 IS '�⏕�R�[�h�R';
COMMENT ON COLUMN TORIHIKISAKI.GENDO IS '���S���x�z';
INSERT INTO torihikisaki 
SELECT
	*
	,''   --TORIHIKISAKI.TORIHIKISAKI_NAME_HANKANA ����於�i���p�J�i�j
	,''   --TORIHIKISAKI.SBUSYO ����敔��
	,''   --TORIHIKISAKI.STANTO �����S����
	,null --TORIHIKISAKI.KEICD �h��
	,0    --TORIHIKISAKI.NAYOSE ���񂹃t���O
	,0    --TORIHIKISAKI.F_SETUIN �߈���s�t���O
	,''   --TORIHIKISAKI.STAN ��S����
	,''   --TORIHIKISAKI.SBCOD ����R�[�h
	,''   --TORIHIKISAKI.SKICD �ȖڃR�[�h
	,0    --TORIHIKISAKI.F_SOUFU ���t�ē�
	,0    --TORIHIKISAKI.ANNAI �ē���
	,0    --TORIHIKISAKI.TSOKBN �������S�敪
	,0    --TORIHIKISAKI.F_SHITU �x���ʒm
	,''   --TORIHIKISAKI.CDM2 �d����ԍ�
	,''   --TORIHIKISAKI.DM1 �⏕�R�[�h�P
	,null --TORIHIKISAKI.DM2 �⏕�R�[�h�Q
	,null --TORIHIKISAKI.DM3 �⏕�R�[�h�R
	,null --TORIHIKISAKI.GENDO ���S���x�z
FROM torihikisaki_tmp;
DROP TABLE torihikisaki_tmp;

CREATE TABLE TORIHIKISAKI_FURIKOMISAKI
(
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	GINKOU_ID SMALLINT NOT NULL,
	KINYUUKIKAN_CD VARCHAR(4) NOT NULL,
	KINYUUKIKAN_SHITEN_CD VARCHAR(3) NOT NULL,
	YOKIN_SHUBETSU VARCHAR(1) NOT NULL,
	KOUZA_BANGOU VARCHAR(7) NOT NULL,
	KOUZA_MEIGININ VARCHAR(60) NOT NULL,
	PRIMARY KEY (TORIHIKISAKI_CD, GINKOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE TORIHIKISAKI_FURIKOMISAKI IS '�����U����';
COMMENT ON COLUMN TORIHIKISAKI_FURIKOMISAKI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN TORIHIKISAKI_FURIKOMISAKI.GINKOU_ID IS '��sID';
COMMENT ON COLUMN TORIHIKISAKI_FURIKOMISAKI.KINYUUKIKAN_CD IS '���Z�@�փR�[�h';
COMMENT ON COLUMN TORIHIKISAKI_FURIKOMISAKI.KINYUUKIKAN_SHITEN_CD IS '���Z�@�֎x�X�R�[�h';
COMMENT ON COLUMN TORIHIKISAKI_FURIKOMISAKI.YOKIN_SHUBETSU IS '�a�����';
COMMENT ON COLUMN TORIHIKISAKI_FURIKOMISAKI.KOUZA_BANGOU IS '�����ԍ�';
COMMENT ON COLUMN TORIHIKISAKI_FURIKOMISAKI.KOUZA_MEIGININ IS '�������`�l';

CREATE TABLE TORIHIKISAKI_HOJO
(
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	DM1 VARCHAR NOT NULL,
	DM2 SMALLINT NOT NULL,
	DM3 INT NOT NULL,
	STFLG SMALLINT NOT NULL,
	PRIMARY KEY (TORIHIKISAKI_CD)
) WITHOUT OIDS;
COMMENT ON TABLE TORIHIKISAKI_HOJO IS '�����⏕';
COMMENT ON COLUMN TORIHIKISAKI_HOJO.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN TORIHIKISAKI_HOJO.DM1 IS '�⏕�R�[�h1';
COMMENT ON COLUMN TORIHIKISAKI_HOJO.DM2 IS '�⏕�R�[�h2';
COMMENT ON COLUMN TORIHIKISAKI_HOJO.DM3 IS '�⏕�R�[�h3';
COMMENT ON COLUMN TORIHIKISAKI_HOJO.STFLG IS '�����~';

CREATE TABLE TORIHIKISAKI_SHIHARAIHOUHOU
(
	TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
	SHIHARAI_ID SMALLINT NOT NULL,
	SHIMEBI SMALLINT NOT NULL,
	SHIHARAIBI_MM SMALLINT NOT NULL,
	SHIHARAIBI_DD SMALLINT NOT NULL,
	SHIHARAI_KBN VARCHAR(2) NOT NULL,
	SHIHARAIKIJITSU_MM SMALLINT NOT NULL,
	SHIHARAIKIJITSU_DD SMALLINT NOT NULL,
	HARAI_H SMALLINT NOT NULL,
	KIJITU_H SMALLINT NOT NULL,
	SHIHARAI_HOUHOU SMALLINT NOT NULL,
	PRIMARY KEY (TORIHIKISAKI_CD, SHIHARAI_ID)
) WITHOUT OIDS;
COMMENT ON TABLE TORIHIKISAKI_SHIHARAIHOUHOU IS '�����x�����@';
COMMENT ON COLUMN TORIHIKISAKI_SHIHARAIHOUHOU.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN TORIHIKISAKI_SHIHARAIHOUHOU.SHIHARAI_ID IS '�x��ID';
COMMENT ON COLUMN TORIHIKISAKI_SHIHARAIHOUHOU.SHIMEBI IS '����';
COMMENT ON COLUMN TORIHIKISAKI_SHIHARAIHOUHOU.SHIHARAIBI_MM IS '�x�����iMM�j';
COMMENT ON COLUMN TORIHIKISAKI_SHIHARAIHOUHOU.SHIHARAIBI_DD IS '�x�����iDD�j';
COMMENT ON COLUMN TORIHIKISAKI_SHIHARAIHOUHOU.SHIHARAI_KBN IS '�x���敪';
COMMENT ON COLUMN TORIHIKISAKI_SHIHARAIHOUHOU.SHIHARAIKIJITSU_MM IS '�x�������iMM�j';
COMMENT ON COLUMN TORIHIKISAKI_SHIHARAIHOUHOU.SHIHARAIKIJITSU_DD IS '�x�������iDD�j';
COMMENT ON COLUMN TORIHIKISAKI_SHIHARAIHOUHOU.HARAI_H IS '�x���␳(�x����)';
COMMENT ON COLUMN TORIHIKISAKI_SHIHARAIHOUHOU.KIJITU_H IS '�x���␳(�x������)';
COMMENT ON COLUMN TORIHIKISAKI_SHIHARAIHOUHOU.SHIHARAI_HOUHOU IS '�x�����@';

\copy denpyou_shubetsu_ichiran FROM '.\files\csv\denpyou_shubetsu_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE denpyou_shubetsu_ichiran SET hyouji_jun=(SELECT MAX(hyouji_jun)+1 FROM denpyou_shubetsu_ichiran WHERE denpyou_kbn<>'A013') WHERE denpyou_kbn='A013';
UPDATE denpyou_shubetsu_ichiran SET yuukou_kigen_to = current_date - integer '1' WHERE denpyou_kbn='A013';

CREATE TABLE SHIHARAI_IRAI
(
    DENPYOU_ID VARCHAR(19) NOT NULL,
    KEIJOUBI DATE NOT NULL,
    YOTEIBI DATE NOT NULL,
    SHIHARAIBI DATE,
    SHIHARAI_KIJITSU DATE,
    TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
    TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    ICHIGENSAKI_TORIHIKISAKI_NAME VARCHAR NOT NULL,
    EDI VARCHAR(20) NOT NULL,
    SHIHARAI_GOUKEI DECIMAL(15) NOT NULL,
    SOUSAI_GOUKEI DECIMAL(15) NOT NULL,
    SASHIHIKI_SHIHARAI DECIMAL(15) NOT NULL,
    MANEKIN_GENSEN DECIMAL(15) NOT NULL,
    HF1_CD VARCHAR(20) NOT NULL,
    HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF2_CD VARCHAR(20) NOT NULL,
    HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF3_CD VARCHAR(20) NOT NULL,
    HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF4_CD VARCHAR(20) NOT NULL,
    HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF5_CD VARCHAR(20) NOT NULL,
    HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF6_CD VARCHAR(20) NOT NULL,
    HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF7_CD VARCHAR(20) NOT NULL,
    HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF8_CD VARCHAR(20) NOT NULL,
    HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF9_CD VARCHAR(20) NOT NULL,
    HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF10_CD VARCHAR(20) NOT NULL,
    HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    SHIHARAI_HOUHOU VARCHAR(1) NOT NULL,
    SHIHARAI_SHUBETSU VARCHAR(1) NOT NULL,
    FURIKOMI_GINKOU_CD VARCHAR(4) NOT NULL,
    FURIKOMI_GINKOU_NAME VARCHAR(30) NOT NULL,
    FURIKOMI_GINKOU_SHITEN_CD VARCHAR(3) NOT NULL,
    FURIKOMI_GINKOU_SHITEN_NAME VARCHAR(30) NOT NULL,
    YOKIN_SHUBETSU VARCHAR(1) NOT NULL,
    KOUZA_BANGOU VARCHAR(7) NOT NULL,
    KOUZA_MEIGININ VARCHAR(60) NOT NULL,
    TESUURYOU VARCHAR(1) NOT NULL,
    HOSOKU VARCHAR(240) NOT NULL,
    GYAKU_SHIWAKE_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
    SHUTSURYOKU_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
    CSV_UPLOAD_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
    HASSEI_SHUBETSU VARCHAR DEFAULT '�o��' NOT NULL,
    SAIMU_MADE_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
    FB_MADE_FLG VARCHAR(1) DEFAULT '0' NOT NULL,
    TOUROKU_USER_ID VARCHAR(30) NOT NULL,
    TOUROKU_TIME TIMESTAMP NOT NULL,
    KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
    KOUSHIN_TIME TIMESTAMP NOT NULL,
    PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE SHIHARAI_IRAI IS '�x���˗�';
COMMENT ON COLUMN SHIHARAI_IRAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN SHIHARAI_IRAI.KEIJOUBI IS '�v���';
COMMENT ON COLUMN SHIHARAI_IRAI.YOTEIBI IS '�\���';
COMMENT ON COLUMN SHIHARAI_IRAI.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN SHIHARAI_IRAI.SHIHARAI_KIJITSU IS '�x������';
COMMENT ON COLUMN SHIHARAI_IRAI.TORIHIKISAKI_CD IS '�����R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '����於�i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI.ICHIGENSAKI_TORIHIKISAKI_NAME IS '�ꌩ�����於';
COMMENT ON COLUMN SHIHARAI_IRAI.EDI IS 'EDI';
COMMENT ON COLUMN SHIHARAI_IRAI.SHIHARAI_GOUKEI IS '�x�����v';
COMMENT ON COLUMN SHIHARAI_IRAI.SOUSAI_GOUKEI IS '���E���v';
COMMENT ON COLUMN SHIHARAI_IRAI.SASHIHIKI_SHIHARAI IS '�����x���z';
COMMENT ON COLUMN SHIHARAI_IRAI.MANEKIN_GENSEN IS '�}�l�L������';
COMMENT ON COLUMN SHIHARAI_IRAI.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI.HF4_CD IS 'HF4�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.HF4_NAME_RYAKUSHIKI IS 'HF4���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI.HF5_CD IS 'HF5�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.HF5_NAME_RYAKUSHIKI IS 'HF5���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI.HF6_CD IS 'HF6�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.HF6_NAME_RYAKUSHIKI IS 'HF6���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI.HF7_CD IS 'HF7�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.HF7_NAME_RYAKUSHIKI IS 'HF7���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI.HF8_CD IS 'HF8�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.HF8_NAME_RYAKUSHIKI IS 'HF8���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI.HF9_CD IS 'HF9�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.HF9_NAME_RYAKUSHIKI IS 'HF9���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI.HF10_CD IS 'HF10�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.HF10_NAME_RYAKUSHIKI IS 'HF10���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI.SHIHARAI_HOUHOU IS '�x�����@';
COMMENT ON COLUMN SHIHARAI_IRAI.SHIHARAI_SHUBETSU IS '�x�����';
COMMENT ON COLUMN SHIHARAI_IRAI.FURIKOMI_GINKOU_CD IS '�U����s�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.FURIKOMI_GINKOU_NAME IS '�U����s����';
COMMENT ON COLUMN SHIHARAI_IRAI.FURIKOMI_GINKOU_SHITEN_CD IS '�U����s�x�X�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI.FURIKOMI_GINKOU_SHITEN_NAME IS '�U����s�x�X����';
COMMENT ON COLUMN SHIHARAI_IRAI.YOKIN_SHUBETSU IS '�a�����';
COMMENT ON COLUMN SHIHARAI_IRAI.KOUZA_BANGOU IS '�����ԍ�';
COMMENT ON COLUMN SHIHARAI_IRAI.KOUZA_MEIGININ IS '�������`�l';
COMMENT ON COLUMN SHIHARAI_IRAI.TESUURYOU IS '�萔��';
COMMENT ON COLUMN SHIHARAI_IRAI.HOSOKU IS '�⑫';
COMMENT ON COLUMN SHIHARAI_IRAI.GYAKU_SHIWAKE_FLG IS '�t�d��t���O';
COMMENT ON COLUMN SHIHARAI_IRAI.SHUTSURYOKU_FLG IS '�o�̓t���O';
COMMENT ON COLUMN SHIHARAI_IRAI.CSV_UPLOAD_FLG IS 'CSV�A�b�v���[�h�t���O';
COMMENT ON COLUMN SHIHARAI_IRAI.HASSEI_SHUBETSU IS '�������';
COMMENT ON COLUMN SHIHARAI_IRAI.SAIMU_MADE_FLG IS '���x���f�[�^�쐬�σt���O';
COMMENT ON COLUMN SHIHARAI_IRAI.FB_MADE_FLG IS 'FB�f�[�^�쐬�σt���O';
COMMENT ON COLUMN SHIHARAI_IRAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN SHIHARAI_IRAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN SHIHARAI_IRAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN SHIHARAI_IRAI.KOUSHIN_TIME IS '�X�V����';

CREATE TABLE SHIHARAI_IRAI_MEISAI
(
    DENPYOU_ID VARCHAR(19) NOT NULL,
    DENPYOU_EDANO INT NOT NULL,
    SHIWAKE_EDANO INT NOT NULL,
    TORIHIKI_NAME VARCHAR(20) NOT NULL,
    TEKIYOU VARCHAR(60) NOT NULL,
    SHIHARAI_KINGAKU DECIMAL(15) NOT NULL,
    KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
    KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
    KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
    KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
    KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
    KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
    KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
    ZEIRITSU DECIMAL(3) NOT NULL,
    UF1_CD VARCHAR(20) NOT NULL,
    UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF2_CD VARCHAR(20) NOT NULL,
    UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF3_CD VARCHAR(20) NOT NULL,
    UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF4_CD VARCHAR(20) NOT NULL,
    UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF5_CD VARCHAR(20) NOT NULL,
    UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF6_CD VARCHAR(20) NOT NULL,
    UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF7_CD VARCHAR(20) NOT NULL,
    UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF8_CD VARCHAR(20) NOT NULL,
    UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF9_CD VARCHAR(20) NOT NULL,
    UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF10_CD VARCHAR(20) NOT NULL,
    UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    PROJECT_CD VARCHAR(12) NOT NULL,
    PROJECT_NAME VARCHAR(20) NOT NULL,
    SEGMENT_CD VARCHAR(8) NOT NULL,
    SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    TEKIYOU_CD VARCHAR(4) NOT NULL,
    TOUROKU_USER_ID VARCHAR(30) NOT NULL,
    TOUROKU_TIME TIMESTAMP NOT NULL,
    KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
    KOUSHIN_TIME TIMESTAMP NOT NULL,
    PRIMARY KEY (DENPYOU_ID, DENPYOU_EDANO)
) WITHOUT OIDS;
COMMENT ON TABLE SHIHARAI_IRAI_MEISAI IS '�x���˗�����';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.DENPYOU_EDANO IS '�`�[�}�ԍ�';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.TEKIYOU IS '�E�v';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.SHIHARAI_KINGAKU IS '�x�����z';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF4_CD IS 'UF4�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF4_NAME_RYAKUSHIKI IS 'UF4���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF5_CD IS 'UF5�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF5_NAME_RYAKUSHIKI IS 'UF5���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF6_CD IS 'UF6�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF6_NAME_RYAKUSHIKI IS 'UF6���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF7_CD IS 'UF7�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF7_NAME_RYAKUSHIKI IS 'UF7���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF8_CD IS 'UF8�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF8_NAME_RYAKUSHIKI IS 'UF8���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF9_CD IS 'UF9�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF9_NAME_RYAKUSHIKI IS 'UF9���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF10_CD IS 'UF10�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.UF10_NAME_RYAKUSHIKI IS 'UF10���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.SEGMENT_CD IS '�Z�O�����g�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.SEGMENT_NAME_RYAKUSHIKI IS '�Z�O�����g���i�����j';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN SHIHARAI_IRAI_MEISAI.KOUSHIN_TIME IS '�X�V����';


commit;