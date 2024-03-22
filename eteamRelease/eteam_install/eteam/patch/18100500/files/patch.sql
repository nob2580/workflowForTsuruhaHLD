SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �w�}�X�^�[�f�[�^�X�V
DELETE FROM eki_master;
\copy eki_master FROM '.\files\csv\eki_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- �ȉ��͎x���˗��̕���(Ver 18.07.23.01�̑Ή���Ver 18.07.23.03�ň�U����������)

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

-- �����R�[�h ���I�v�V�����p���R�[�h�̓p�b�`�O�ɂȂ���΍폜
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

-- �`�[���
\copy denpyou_shubetsu_ichiran FROM '.\files\csv\denpyou_shubetsu_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE denpyou_shubetsu_ichiran SET hyouji_jun=(SELECT MAX(hyouji_jun)+1 FROM denpyou_shubetsu_ichiran WHERE denpyou_kbn<>'A013') WHERE denpyou_kbn='A013';
UPDATE denpyou_shubetsu_ichiran SET yuukou_kigen_to = current_date - integer '1' WHERE denpyou_kbn='A013';

-- �}�X�^�[�Ǘ��̐U��������(�x���˗�)�e�[�u��������ǉ�
\copy master_kanri_hansuu FROM '.\files\csv\master_kanri_hansuu_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_kanri_ichiran FROM '.\files\csv\master_kanri_ichiran_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- �ȉ��͎x���˗��Ή�

-- ��Џ��ɃR�[�h�^�C�v���������� ���p�b�`�ł̓R�[�h�^�C�v=1�ɂ��邪�A�}�X�^�[�捞�ɂ�萳�����l�ɂȂ�
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
    SAIMU_SHIYOU_FLG VARCHAR(1) NOT NULL,
    KAMOKU_CD_TYPE SMALLINT NOT NULL,
    KAMOKU_EDABAN_CD_TYPE SMALLINT NOT NULL,
    FUTAN_BUMON_CD_TYPE SMALLINT NOT NULL,
    TORIHIKISAKI_CD_TYPE SMALLINT NOT NULL,
    SEGMENT_CD_TYPE SMALLINT NOT NULL
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
COMMENT ON COLUMN KAISHA_INFO.KAMOKU_CD_TYPE IS '�ȖڃR�[�h�^�C�v';
COMMENT ON COLUMN KAISHA_INFO.KAMOKU_EDABAN_CD_TYPE IS '�Ȗڎ}�ԃR�[�h�^�C�v';
COMMENT ON COLUMN KAISHA_INFO.FUTAN_BUMON_CD_TYPE IS '���S����R�[�h�^�C�v';
COMMENT ON COLUMN KAISHA_INFO.TORIHIKISAKI_CD_TYPE IS '�����R�[�h�^�C�v';
COMMENT ON COLUMN KAISHA_INFO.SEGMENT_CD_TYPE IS '�Z�O�����g�R�[�h�^�C�v';
INSERT INTO KAISHA_INFO
SELECT
     KESSANKI_BANGOU
    ,HF1_SHIYOU_FLG
    ,HF1_HISSU_FLG
    ,HF1_NAME
    ,HF2_SHIYOU_FLG
    ,HF2_HISSU_FLG
    ,HF2_NAME
    ,HF3_SHIYOU_FLG
    ,HF3_HISSU_FLG
    ,HF3_NAME
    ,HF4_SHIYOU_FLG
    ,HF4_HISSU_FLG
    ,HF4_NAME
    ,HF5_SHIYOU_FLG
    ,HF5_HISSU_FLG
    ,HF5_NAME
    ,HF6_SHIYOU_FLG
    ,HF6_HISSU_FLG
    ,HF6_NAME
    ,HF7_SHIYOU_FLG
    ,HF7_HISSU_FLG
    ,HF7_NAME
    ,HF8_SHIYOU_FLG
    ,HF8_HISSU_FLG
    ,HF8_NAME
    ,HF9_SHIYOU_FLG
    ,HF9_HISSU_FLG
    ,HF9_NAME
    ,HF10_SHIYOU_FLG
    ,HF10_HISSU_FLG
    ,HF10_NAME
    ,UF1_SHIYOU_FLG
    ,UF1_NAME
    ,UF2_SHIYOU_FLG
    ,UF2_NAME
    ,UF3_SHIYOU_FLG
    ,UF3_NAME
    ,UF4_SHIYOU_FLG
    ,UF4_NAME
    ,UF5_SHIYOU_FLG
    ,UF5_NAME
    ,UF6_SHIYOU_FLG
    ,UF6_NAME
    ,UF7_SHIYOU_FLG
    ,UF7_NAME
    ,UF8_SHIYOU_FLG
    ,UF8_NAME
    ,UF9_SHIYOU_FLG
    ,UF9_NAME
    ,UF10_SHIYOU_FLG
    ,UF10_NAME
    ,UF_KOTEI1_SHIYOU_FLG
    ,UF_KOTEI1_NAME
    ,UF_KOTEI2_SHIYOU_FLG
    ,UF_KOTEI2_NAME
    ,UF_KOTEI3_SHIYOU_FLG
    ,UF_KOTEI3_NAME
    ,UF_KOTEI4_SHIYOU_FLG
    ,UF_KOTEI4_NAME
    ,UF_KOTEI5_SHIYOU_FLG
    ,UF_KOTEI5_NAME
    ,UF_KOTEI6_SHIYOU_FLG
    ,UF_KOTEI6_NAME
    ,UF_KOTEI7_SHIYOU_FLG
    ,UF_KOTEI7_NAME
    ,UF_KOTEI8_SHIYOU_FLG
    ,UF_KOTEI8_NAME
    ,UF_KOTEI9_SHIYOU_FLG
    ,UF_KOTEI9_NAME
    ,UF_KOTEI10_SHIYOU_FLG
    ,UF_KOTEI10_NAME
    ,PJCD_SHIYOU_FLG
    ,SGCD_SHIYOU_FLG
    ,SAIMU_SHIYOU_FLG
    ,1--KAMOKU_CD_TYPE
    ,1--KAMOKU_EDABAN_CD_TYPE
    ,1--FUTAN_BUMON_CD_TYPE
    ,1--TORIHIKISAKI_CD_TYPE
    ,1--SEGMENT_CD_TYPE
FROM KAISHA_INFO_OLD;
DROP TABLE KAISHA_INFO_OLD;

-- �x���˗����̘a���ύX
COMMENT ON COLUMN SHIHARAI_IRAI.MANEKIN_GENSEN IS '�T������';

-- �}�X�^�[�捞����
DELETE FROM master_torikomi_ichiran_de3;
DELETE FROM master_torikomi_shousai_de3;
DELETE FROM master_torikomi_ichiran_sias;
DELETE FROM master_torikomi_shousai_sias;
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';


commit;