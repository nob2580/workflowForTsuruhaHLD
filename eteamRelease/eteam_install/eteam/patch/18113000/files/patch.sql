SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


--  �J���v��_17 0639 �ݒ���f�[�^�ύX ��hyouji_jun900�ȍ~�̓J�X�^�}�C�Y�̈�
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

-- ��ʌ�������̒ǉ�
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- �J���v��_17 �����R�[�h�ݒ�
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- �x���˗��\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- ���������\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

-- 0639 ��ʍ��ڐ���̏C��
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

-- 0651,0653 �w�x���˗����x�ƂȂ��Ă���\�����w�x���˗��\���x�ɏC��
UPDATE gamen_kengen_seigyo 
SET 
	gamen_name = '�x���˗��\��'
WHERE gamen_id = 'ShiharaiIrai';

UPDATE denpyou_shubetsu_ichiran 
SET 
	denpyou_shubetsu = '�x���˗��\��'
WHERE denpyou_shubetsu = '�x���˗���'
AND denpyou_kbn = 'A013';

UPDATE denpyou_shubetsu_ichiran 
SET 
	denpyou_karibarai_nashi_shubetsu = '�x���˗��\��'
WHERE denpyou_karibarai_nashi_shubetsu = '�x���˗���'
AND denpyou_kbn = 'A013';

UPDATE denpyou_shubetsu_ichiran 
SET 
	denpyou_print_shubetsu = '�x���˗��\��'
WHERE denpyou_print_shubetsu = '�x���˗���'
AND denpyou_kbn = 'A013';

UPDATE denpyou_shubetsu_ichiran 
SET 
	denpyou_print_karibarai_nashi_shubetsu = '�x���˗��\��'
WHERE denpyou_print_karibarai_nashi_shubetsu = '�x���˗���'
AND denpyou_kbn = 'A013';

-- �w�}�X�^�[�f�[�^�X�V
DELETE FROM eki_master;
\copy eki_master FROM '.\files\csv\eki_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--�o�X�H�����}�X�^�[ �V�K�쐬
CREATE TABLE BUS_LINE_MASTER
(
	LINE_CD VARCHAR(6) NOT NULL,
	LINE_NAME VARCHAR NOT NULL,
	PRIMARY KEY (LINE_CD)
) WITHOUT OIDS;

COMMENT ON TABLE BUS_LINE_MASTER IS '�o�X�H�����}�X�^�[';
COMMENT ON COLUMN BUS_LINE_MASTER.LINE_CD IS '�o�X�H���R�[�h';
COMMENT ON COLUMN BUS_LINE_MASTER.LINE_NAME IS '�H����';

-- �o�X�H�����}�X�^�[�f�[�^�X�V
DELETE FROM BUS_LINE_MASTER;
\copy BUS_LINE_MASTER FROM '.\files\csv\bus_line_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--IC�J�[�h���p���� ��`�X�V
ALTER TABLE IC_CARD_RIREKI RENAME TO IC_CARD_RIREKI_OLD;
CREATE TABLE IC_CARD_RIREKI
(
	IC_CARD_NO VARCHAR(16) NOT NULL,
	IC_CARD_SEQUENCE_NO VARCHAR(10) NOT NULL,
	IC_CARD_RIYOUBI DATE NOT NULL,
	TANMATU_CD VARCHAR(6) NOT NULL,
	LINE_CD_FROM VARCHAR(6) NOT NULL,
	LINE_NAME_FROM VARCHAR NOT NULL,
	EKI_CD_FROM VARCHAR(6) NOT NULL,
	EKI_NAME_FROM VARCHAR NOT NULL,
	LINE_CD_TO VARCHAR(6) NOT NULL,
	LINE_NAME_TO VARCHAR NOT NULL,
	EKI_CD_TO VARCHAR(6) NOT NULL,
	EKI_NAME_TO VARCHAR NOT NULL,
	KINGAKU DECIMAL(15) NOT NULL,
	USER_ID VARCHAR(30) NOT NULL,
	JYOGAI_FLG VARCHAR(1) NOT NULL,
	PRIMARY KEY (IC_CARD_NO, IC_CARD_SEQUENCE_NO)
) WITHOUT OIDS;
COMMENT ON TABLE IC_CARD_RIREKI IS 'IC�J�[�h���p����';
COMMENT ON COLUMN IC_CARD_RIREKI.IC_CARD_NO IS 'IC�J�[�h�ԍ�';
COMMENT ON COLUMN IC_CARD_RIREKI.IC_CARD_SEQUENCE_NO IS 'IC�J�[�h�V�[�P���X�ԍ�';
COMMENT ON COLUMN IC_CARD_RIREKI.IC_CARD_RIYOUBI IS 'IC�J�[�h���p��';
COMMENT ON COLUMN IC_CARD_RIREKI.TANMATU_CD IS '�[�����';
COMMENT ON COLUMN IC_CARD_RIREKI.LINE_CD_FROM IS '�H���R�[�h�iFROM�j';
COMMENT ON COLUMN IC_CARD_RIREKI.LINE_NAME_FROM IS '�H�����iFROM�j';
COMMENT ON COLUMN IC_CARD_RIREKI.EKI_CD_FROM IS '�w�R�[�h�iFROM�j';
COMMENT ON COLUMN IC_CARD_RIREKI.EKI_NAME_FROM IS '�w���iFROM�j';
COMMENT ON COLUMN IC_CARD_RIREKI.LINE_CD_TO IS '�H���R�[�h�iTO�j';
COMMENT ON COLUMN IC_CARD_RIREKI.LINE_NAME_TO IS '�H�����iTO�j';
COMMENT ON COLUMN IC_CARD_RIREKI.EKI_CD_TO IS '�w�R�[�h�iTO�j';
COMMENT ON COLUMN IC_CARD_RIREKI.EKI_NAME_TO IS '�w���iTO�j';
COMMENT ON COLUMN IC_CARD_RIREKI.KINGAKU IS '���z';
COMMENT ON COLUMN IC_CARD_RIREKI.USER_ID IS '���[�U�[ID';
COMMENT ON COLUMN IC_CARD_RIREKI.JYOGAI_FLG IS '���O�t���O';
INSERT INTO IC_CARD_RIREKI
SELECT
    IC_CARD_NO,
    IC_CARD_SEQUENCE_NO,
    IC_CARD_RIYOUBI,
    '', --TANMATU_CD
    LINE_CD_FROM,
    LINE_NAME_FROM,
    EKI_CD_FROM,
    EKI_NAME_FROM,
    LINE_CD_TO,
    LINE_NAME_TO,
    EKI_CD_TO,
    EKI_NAME_TO,
    KINGAKU,
    USER_ID,
    '0' --JYOGAI_FLG
FROM IC_CARD_RIREKI_OLD;
DROP TABLE IC_CARD_RIREKI_OLD;

--e�����f�[�^ ��`�X�V
alter table ebunsho_data rename to ebunsho_data_old;
create table ebunsho_data (
  ebunsho_no character varying(19) not null
  , ebunsho_edano integer not null
  , ebunsho_shubetsu integer not null
  , ebunsho_nengappi date not null
  , ebunsho_kingaku numeric(15) not null
  , ebunsho_hakkousha character varying(20) not null
  , ebunsho_hinmei character varying(50) not null
  , primary key (ebunsho_no,ebunsho_edano)
) WITHOUT OIDS;
comment on table ebunsho_data is 'e�����f�[�^';
comment on column ebunsho_data.ebunsho_no is 'e�����ԍ�';
comment on column ebunsho_data.ebunsho_edano is 'e�����}�ԍ�';
comment on column ebunsho_data.ebunsho_shubetsu is 'e�������';
comment on column ebunsho_data.ebunsho_nengappi is 'e�����N����';
comment on column ebunsho_data.ebunsho_kingaku is 'e�������z';
comment on column ebunsho_data.ebunsho_hakkousha is 'e�������s��';
comment on column ebunsho_data.ebunsho_hinmei is 'e�����i��';

INSERT INTO ebunsho_data
SELECT 
  ebunsho_no
  , ebunsho_edano
  , ebunsho_shubetsu
  , ebunsho_nengappi
  , ebunsho_kingaku
  , ebunsho_hakkousha
  , '' --ebunsho_hinmei
FROM ebunsho_data_old;
DROP TABLE ebunsho_data_old;


-- 0639 �x���˗� ��`�X�V
alter table shiharai_irai rename to shiharai_irai_old;

create table shiharai_irai (
  denpyou_id character varying(19) not null
  , keijoubi date not null
  , yoteibi date not null
  , shiharaibi date
  , shiharai_kijitsu date
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , ichigensaki_torihikisaki_name character varying not null
  , edi character varying(20) not null
  , shiharai_goukei numeric(15) not null
  , sousai_goukei numeric(15) not null
  , sashihiki_shiharai numeric(15) not null
  , manekin_gensen numeric(15) not null
  , shouhyou_shorui_flg character varying(1) not null
  , hf1_cd character varying(20) not null
  , hf1_name_ryakushiki character varying(20) not null
  , hf2_cd character varying(20) not null
  , hf2_name_ryakushiki character varying(20) not null
  , hf3_cd character varying(20) not null
  , hf3_name_ryakushiki character varying(20) not null
  , hf4_cd character varying(20) not null
  , hf4_name_ryakushiki character varying(20) not null
  , hf5_cd character varying(20) not null
  , hf5_name_ryakushiki character varying(20) not null
  , hf6_cd character varying(20) not null
  , hf6_name_ryakushiki character varying(20) not null
  , hf7_cd character varying(20) not null
  , hf7_name_ryakushiki character varying(20) not null
  , hf8_cd character varying(20) not null
  , hf8_name_ryakushiki character varying(20) not null
  , hf9_cd character varying(20) not null
  , hf9_name_ryakushiki character varying(20) not null
  , hf10_cd character varying(20) not null
  , hf10_name_ryakushiki character varying(20) not null
  , shiharai_houhou character varying(1) not null
  , shiharai_shubetsu character varying(1) not null
  , furikomi_ginkou_cd character varying(4) not null
  , furikomi_ginkou_name character varying(30) not null
  , furikomi_ginkou_shiten_cd character varying(3) not null
  , furikomi_ginkou_shiten_name character varying(30) not null
  , yokin_shubetsu character varying(1) not null
  , kouza_bangou character varying(7) not null
  , kouza_meiginin character varying(60) not null
  , tesuuryou character varying(1) not null
  , hosoku character varying(240) not null
  , gyaku_shiwake_flg character varying(1) default '0' not null
  , shutsuryoku_flg character varying(1) default '0' not null
  , csv_upload_flg character varying(1) default '0' not null
  , hassei_shubetsu character varying default '�o��' not null
  , saimu_made_flg character varying(1) default '0' not null
  , fb_made_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id)
) WITHOUT OIDS;

comment on table shiharai_irai is '�x���˗�';
comment on column shiharai_irai.denpyou_id is '�`�[ID';
comment on column shiharai_irai.keijoubi is '�v���';
comment on column shiharai_irai.yoteibi is '�\���';
comment on column shiharai_irai.shiharaibi is '�x����';
comment on column shiharai_irai.shiharai_kijitsu is '�x������';
comment on column shiharai_irai.torihikisaki_cd is '�����R�[�h';
comment on column shiharai_irai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column shiharai_irai.ichigensaki_torihikisaki_name is '�ꌩ�����於';
comment on column shiharai_irai.edi is 'EDI';
comment on column shiharai_irai.shiharai_goukei is '�x�����v';
comment on column shiharai_irai.sousai_goukei is '���E���v';
comment on column shiharai_irai.sashihiki_shiharai is '�����x���z';
comment on column shiharai_irai.manekin_gensen is '�T������';
comment on column shiharai_irai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column shiharai_irai.hf1_cd is 'HF1�R�[�h';
comment on column shiharai_irai.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column shiharai_irai.hf2_cd is 'HF2�R�[�h';
comment on column shiharai_irai.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column shiharai_irai.hf3_cd is 'HF3�R�[�h';
comment on column shiharai_irai.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column shiharai_irai.hf4_cd is 'HF4�R�[�h';
comment on column shiharai_irai.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column shiharai_irai.hf5_cd is 'HF5�R�[�h';
comment on column shiharai_irai.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column shiharai_irai.hf6_cd is 'HF6�R�[�h';
comment on column shiharai_irai.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column shiharai_irai.hf7_cd is 'HF7�R�[�h';
comment on column shiharai_irai.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column shiharai_irai.hf8_cd is 'HF8�R�[�h';
comment on column shiharai_irai.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column shiharai_irai.hf9_cd is 'HF9�R�[�h';
comment on column shiharai_irai.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column shiharai_irai.hf10_cd is 'HF10�R�[�h';
comment on column shiharai_irai.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column shiharai_irai.shiharai_houhou is '�x�����@';
comment on column shiharai_irai.shiharai_shubetsu is '�x�����';
comment on column shiharai_irai.furikomi_ginkou_cd is '�U����s�R�[�h';
comment on column shiharai_irai.furikomi_ginkou_name is '�U����s����';
comment on column shiharai_irai.furikomi_ginkou_shiten_cd is '�U����s�x�X�R�[�h';
comment on column shiharai_irai.furikomi_ginkou_shiten_name is '�U����s�x�X����';
comment on column shiharai_irai.yokin_shubetsu is '�a�����';
comment on column shiharai_irai.kouza_bangou is '�����ԍ�';
comment on column shiharai_irai.kouza_meiginin is '�������`�l';
comment on column shiharai_irai.tesuuryou is '�萔��';
comment on column shiharai_irai.hosoku is '�⑫';
comment on column shiharai_irai.gyaku_shiwake_flg is '�t�d��t���O';
comment on column shiharai_irai.shutsuryoku_flg is '�o�̓t���O';
comment on column shiharai_irai.csv_upload_flg is 'CSV�A�b�v���[�h�t���O';
comment on column shiharai_irai.hassei_shubetsu is '�������';
comment on column shiharai_irai.saimu_made_flg is '���x���f�[�^�쐬�σt���O';
comment on column shiharai_irai.fb_made_flg is 'FB�f�[�^�쐬�σt���O';
comment on column shiharai_irai.touroku_user_id is '�o�^���[�U�[ID';
comment on column shiharai_irai.touroku_time is '�o�^����';
comment on column shiharai_irai.koushin_user_id is '�X�V���[�U�[ID';
comment on column shiharai_irai.koushin_time is '�X�V����';

INSERT INTO shiharai_irai
SELECT 
  denpyou_id
  , keijoubi
  , yoteibi
  , shiharaibi
  , shiharai_kijitsu
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , ichigensaki_torihikisaki_name
  , edi
  , shiharai_goukei
  , sousai_goukei
  , sashihiki_shiharai
  , manekin_gensen
  , '0' --shouhyou_shorui_flg
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
  , shiharai_houhou
  , shiharai_shubetsu
  , furikomi_ginkou_cd
  , furikomi_ginkou_name
  , furikomi_ginkou_shiten_cd
  , furikomi_ginkou_shiten_name
  , yokin_shubetsu
  , kouza_bangou
  , kouza_meiginin
  , tesuuryou
  , hosoku
  , gyaku_shiwake_flg
  , shutsuryoku_flg
  , csv_upload_flg
  , hassei_shubetsu
  , saimu_made_flg
  , fb_made_flg
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM shiharai_irai_old;
DROP TABLE shiharai_irai_old;

-- 0485 �\�Z���s�����N��
create table yosan_shikkou_shori_nengetsu (
  from_nengetsu character varying(6) not null
  , to_nengetsu character varying(6) not null
  , primary key (from_nengetsu,to_nengetsu)
);
comment on table yosan_shikkou_shori_nengetsu is '�\�Z���s�����N��';
comment on column yosan_shikkou_shori_nengetsu.from_nengetsu is '�J�n�N��';
comment on column yosan_shikkou_shori_nengetsu.to_nengetsu is '�I���N��';

-- 0485 ���s�󋵈ꗗ���
create table shikkou_joukyou_ichiran_jouhou (
  user_id character varying(30) not null
  , yosan_tani character varying(1) not null
  , primary key (user_id)
);
comment on table shikkou_joukyou_ichiran_jouhou is '���s�󋵈ꗗ���';
comment on column shikkou_joukyou_ichiran_jouhou.user_id is '���[�U�[ID';
comment on column shikkou_joukyou_ichiran_jouhou.yosan_tani is '�\�Z�P��';

-- 0485 �`�[
ALTER TABLE denpyou RENAME TO denpyou_old;
create table denpyou (
  denpyou_id character varying(19) not null
  , denpyou_kbn character varying(4) not null
  , denpyou_joutai character varying(2) not null
  , sanshou_denpyou_id character varying(19) not null
  , daihyou_futan_bumon_cd character varying(8) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , serial_no bigint not null
  , chuushutsu_zumi_flg character varying(1) default '0' not null
  , shounin_route_henkou_flg character varying(1) default '0' not null
  , maruhi_flg character varying(1) default '0' not null
  , yosan_check_nengetsu character varying(6) default '' not null
  , primary key (denpyou_id)
);
comment on table denpyou is '�`�[';
comment on column denpyou.denpyou_id is '�`�[ID';
comment on column denpyou.denpyou_kbn is '�`�[�敪';
comment on column denpyou.denpyou_joutai is '�`�[���';
comment on column denpyou.sanshou_denpyou_id is '�Q�Ɠ`�[ID';
comment on column denpyou.daihyou_futan_bumon_cd is '��\���S����R�[�h';
comment on column denpyou.touroku_user_id is '�o�^���[�U�[ID';
comment on column denpyou.touroku_time is '�o�^����';
comment on column denpyou.koushin_user_id is '�X�V���[�U�[ID';
comment on column denpyou.koushin_time is '�X�V����';
comment on column denpyou.serial_no is '�V���A���ԍ�';
comment on column denpyou.chuushutsu_zumi_flg is '���o�σt���O';
comment on column denpyou.shounin_route_henkou_flg is '���F���[�g�ύX�t���O';
comment on column denpyou.maruhi_flg is '�}���镶���t���O';
comment on column denpyou.yosan_check_nengetsu is '�\�Z���s�Ώی�';
INSERT INTO denpyou
SELECT
    denpyou_id
  , denpyou_kbn
  , denpyou_joutai
  , sanshou_denpyou_id
  , daihyou_futan_bumon_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
  , serial_no
  , chuushutsu_zumi_flg
  , shounin_route_henkou_flg
  , maruhi_flg
  , ''   -- yosan_check_nengetsu
FROM denpyou_old;
DROP TABLE denpyou_old;

-- 0485 �`�[�ꗗ
ALTER TABLE denpyou_ichiran RENAME TO denpyou_ichiran_old;
create table denpyou_ichiran (
  denpyou_id character varying(19) not null
  , name character varying not null
  , denpyou_kbn character varying(4)
  , jisshi_kian_bangou character varying(15) not null
  , shishutsu_kian_bangou character varying(15) not null
  , yosan_shikkou_taishou character varying not null
  , yosan_check_nengetsu character varying(6) not null
  , serial_no bigint
  , denpyou_shubetsu_url character varying(240) not null
  , touroku_time timestamp without time zone
  , bumon_full_name character varying not null
  , user_full_name character varying(50) not null
  , user_id character varying(30) not null
  , denpyou_joutai character varying(2) not null
  , koushin_time timestamp without time zone
  , shouninbi timestamp without time zone
  , maruhi_flg character varying(1) not null
  , all_cnt bigint
  , cur_cnt bigint
  , zan_cnt bigint
  , gen_bumon_full_name character varying not null
  , gen_user_full_name character varying not null
  , gen_gyoumu_role_name character varying not null
  , gen_name character varying not null
  , version integer
  , kingaku numeric(15)
  , gaika character varying not null
  , houjin_kingaku numeric(15)
  , tehai_kingaku numeric(15)
  , torihikisaki1 character varying not null
  , shiharaibi date
  , shiharaikiboubi date
  , shiharaihouhou character varying not null
  , sashihiki_shikyuu_kingaku numeric(15)
  , keijoubi date
  , shiwakekeijoubi date
  , seisan_yoteibi date
  , karibarai_denpyou_id character varying(19) not null
  , houmonsaki character varying(120) not null
  , mokuteki character varying(240) not null
  , kenmei character varying not null
  , naiyou character varying not null
  , user_sei character varying(10) not null
  , user_mei character varying(10) not null
  , seisankikan_from date
  , seisankikan_to date
  , gen_user_id character varying not null
  , gen_gyoumu_role_id character varying not null
  , kian_bangou_unyou_flg character varying(1) not null
  , yosan_shikkou_taishou_cd character varying not null
  , kian_syuryou_flg character varying not null
  , kari_futan_bumon_cd character varying not null
  , kari_futan_bumon_name character varying not null
  , kari_kamoku_cd character varying not null
  , kari_kamoku_name character varying not null
  , kari_torihikisaki_cd character varying not null
  , kari_torihikisaki_name character varying not null
  , kashi_futan_bumon_cd character varying not null
  , kashi_futan_bumon_name character varying not null
  , kashi_kamoku_cd character varying not null
  , kashi_kamoku_name character varying not null
  , kashi_torihikisaki_cd character varying not null
  , kashi_torihikisaki_name character varying not null
  , tekiyou character varying not null
  , houjin_card_use character varying(1) not null
  , kaisha_tehai_use character varying(1) not null
  , ryoushuusho_exist character varying not null
  , miseisan_karibarai_exist character varying(1) not null
  , miseisan_ukagai_exist character varying(1) not null
  , shiwake_status character varying not null
  , fb_status character varying not null
  , jisshi_nendo character varying(4) not null
  , shishutsu_nendo character varying(4) not null
  , bumon_cd character varying(8) not null
  , kian_bangou_input character varying(1) not null
  , primary key (denpyou_id)
);
comment on table denpyou_ichiran is '�`�[�ꗗ';
comment on column denpyou_ichiran.denpyou_id is '�`�[ID';
comment on column denpyou_ichiran.name is '�X�e�[�^�X';
comment on column denpyou_ichiran.denpyou_kbn is '�`�[�敪';
comment on column denpyou_ichiran.jisshi_kian_bangou is '���{�N�Ĕԍ�';
comment on column denpyou_ichiran.shishutsu_kian_bangou is '�x�o�N�Ĕԍ�';
comment on column denpyou_ichiran.yosan_shikkou_taishou is '�\�Z���s�Ώ�';
comment on column denpyou_ichiran.yosan_check_nengetsu is '�\�Z���s�Ώی�';
comment on column denpyou_ichiran.serial_no is '�V���A���ԍ�';
comment on column denpyou_ichiran.denpyou_shubetsu_url is '�`�[���URL';
comment on column denpyou_ichiran.touroku_time is '�o�^����';
comment on column denpyou_ichiran.bumon_full_name is '����t����';
comment on column denpyou_ichiran.user_full_name is '���[�U�[�t����';
comment on column denpyou_ichiran.user_id is '���[�U�[ID';
comment on column denpyou_ichiran.denpyou_joutai is '�`�[���';
comment on column denpyou_ichiran.koushin_time is '�X�V����';
comment on column denpyou_ichiran.shouninbi is '���F��';
comment on column denpyou_ichiran.maruhi_flg is '�}���镶���t���O';
comment on column denpyou_ichiran.all_cnt is '�S���F�l���J�E���g';
comment on column denpyou_ichiran.cur_cnt is '���F�ϐl���J�E���g';
comment on column denpyou_ichiran.zan_cnt is '�c�菳�F�l���J�E���g';
comment on column denpyou_ichiran.gen_bumon_full_name is '���ݏ��F�ҕ���t����';
comment on column denpyou_ichiran.gen_user_full_name is '���ݏ��F�҃��[�U�[�t����';
comment on column denpyou_ichiran.gen_gyoumu_role_name is '���ݏ��F�ҋƖ����[����';
comment on column denpyou_ichiran.gen_name is '���ݏ��F�Җ���';
comment on column denpyou_ichiran.version is '�o�[�W����';
comment on column denpyou_ichiran.kingaku is '���z';
comment on column denpyou_ichiran.gaika is '�O��';
comment on column denpyou_ichiran.houjin_kingaku is '�@�l�J�[�h�����z';
comment on column denpyou_ichiran.tehai_kingaku is '��Ў�z���z';
comment on column denpyou_ichiran.torihikisaki1 is '�����1';
comment on column denpyou_ichiran.shiharaibi is '�x����';
comment on column denpyou_ichiran.shiharaikiboubi is '�x����]��';
comment on column denpyou_ichiran.shiharaihouhou is '�x�����@';
comment on column denpyou_ichiran.sashihiki_shikyuu_kingaku is '�����x�����z';
comment on column denpyou_ichiran.keijoubi is '�v���';
comment on column denpyou_ichiran.shiwakekeijoubi is '�d��v���';
comment on column denpyou_ichiran.seisan_yoteibi is '���Z�\���';
comment on column denpyou_ichiran.karibarai_denpyou_id is '�����`�[ID';
comment on column denpyou_ichiran.houmonsaki is '�K���';
comment on column denpyou_ichiran.mokuteki is '�ړI';
comment on column denpyou_ichiran.kenmei is '����';
comment on column denpyou_ichiran.naiyou is '���e';
comment on column denpyou_ichiran.user_sei is '���[�U�[��';
comment on column denpyou_ichiran.user_mei is '���[�U�[��';
comment on column denpyou_ichiran.seisankikan_from is '���Z���ԊJ�n��';
comment on column denpyou_ichiran.seisankikan_to is '���Z���ԏI����';
comment on column denpyou_ichiran.gen_user_id is '���ݏ��F�҃��[�U�[ID���X�g';
comment on column denpyou_ichiran.gen_gyoumu_role_id is '���ݏ��F�ҋƖ����[��ID���X�g';
comment on column denpyou_ichiran.kian_bangou_unyou_flg is '�N�Ĕԍ��^�p�t���O';
comment on column denpyou_ichiran.yosan_shikkou_taishou_cd is '�\�Z���s�ΏۃR�[�h';
comment on column denpyou_ichiran.kian_syuryou_flg is '�N�ďI���t���O';
comment on column denpyou_ichiran.kari_futan_bumon_cd is '�ؕ����S����R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kari_futan_bumon_name is '�ؕ����S���喼(�ꗗ�����p)';
comment on column denpyou_ichiran.kari_kamoku_cd is '�ؕ��ȖڃR�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kari_kamoku_name is '�ؕ��Ȗږ�(�ꗗ�����p)';
comment on column denpyou_ichiran.kari_torihikisaki_cd is '�ؕ������R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kari_torihikisaki_name is '�ؕ�����於(�ꗗ�����p)';
comment on column denpyou_ichiran.kashi_futan_bumon_cd is '�ݕ����S����R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kashi_futan_bumon_name is '�ݕ����S���喼(�ꗗ�����p)';
comment on column denpyou_ichiran.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kashi_kamoku_name is '�ݕ��Ȗږ�(�ꗗ�����p)';
comment on column denpyou_ichiran.kashi_torihikisaki_cd is '�ݕ������R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kashi_torihikisaki_name is '�ݕ�����於(�ꗗ�����p)';
comment on column denpyou_ichiran.tekiyou is '�E�v(�ꗗ�����p)';
comment on column denpyou_ichiran.houjin_card_use is '�@�l�J�[�h�g�p�t���O';
comment on column denpyou_ichiran.kaisha_tehai_use is '��Ў�z�g�p�t���O';
comment on column denpyou_ichiran.ryoushuusho_exist is '�̎����t���O';
comment on column denpyou_ichiran.miseisan_karibarai_exist is '�����Z�����`�[�t���O';
comment on column denpyou_ichiran.miseisan_ukagai_exist is '�����Z�f���`�[�t���O';
comment on column denpyou_ichiran.shiwake_status is '�d��f�[�^�쐬�X�e�[�^�X';
comment on column denpyou_ichiran.fb_status is 'FB�f�[�^�쐬�X�e�[�^�X';
comment on column denpyou_ichiran.jisshi_nendo is '���{�N�x';
comment on column denpyou_ichiran.shishutsu_nendo is '�x�o�N�x';
comment on column denpyou_ichiran.bumon_cd is '����R�[�h';
comment on column denpyou_ichiran.kian_bangou_input is '�N�Ĕԍ��̔ԃt���O';
INSERT INTO denpyou_ichiran
SELECT
    denpyou_id
  , name
  , denpyou_kbn
  , jisshi_kian_bangou
  , shishutsu_kian_bangou
  , yosan_shikkou_taishou
  , ''   -- yosan_check_nengetsu
  , serial_no
  , denpyou_shubetsu_url
  , touroku_time
  , bumon_full_name
  , user_full_name
  , user_id
  , denpyou_joutai
  , koushin_time
  , shouninbi
  , maruhi_flg
  , all_cnt
  , cur_cnt
  , zan_cnt
  , gen_bumon_full_name
  , gen_user_full_name
  , gen_gyoumu_role_name
  , gen_name
  , version
  , kingaku
  , gaika
  , houjin_kingaku
  , tehai_kingaku
  , torihikisaki1
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , sashihiki_shikyuu_kingaku
  , keijoubi
  , shiwakekeijoubi
  , seisan_yoteibi
  , karibarai_denpyou_id
  , houmonsaki
  , mokuteki
  , kenmei
  , naiyou
  , user_sei
  , user_mei
  , seisankikan_from
  , seisankikan_to
  , gen_user_id
  , gen_gyoumu_role_id
  , kian_bangou_unyou_flg
  , yosan_shikkou_taishou_cd
  , kian_syuryou_flg
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_torihikisaki_cd
  , kari_torihikisaki_name
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_torihikisaki_cd
  , kashi_torihikisaki_name
  , tekiyou
  , houjin_card_use
  , kaisha_tehai_use
  , ryoushuusho_exist
  , miseisan_karibarai_exist
  , miseisan_ukagai_exist
  , shiwake_status
  , fb_status
  , jisshi_nendo
  , shishutsu_nendo
  , bumon_cd
  , kian_bangou_input
FROM denpyou_ichiran_old;
DROP TABLE denpyou_ichiran_old;

-- UTF-8��Shift-JIS���ł��Ȃ�����������ꍇShift-JIS�̑Ή������ɕϊ�
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xe3809c', 'utf8'), '�`');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xe28096', 'utf8'), '�a');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xe28892', 'utf8'), '�|');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xc2a2', 'utf8'), '��');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xc2a3', 'utf8'), '��');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xc2ac', 'utf8'), '��');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xe28094', 'utf8'), '�\');

-- LT��DB�\���̍����Ή�
DROP TABLE IF EXISTS furikae_old;

comment on table ryohi_karibarai_keihi_meisai is '������o���';
comment on column ryohi_karibarai_keihi_meisai.denpyou_id is '�`�[ID';
comment on column ryohi_karibarai_keihi_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column ryohi_karibarai_keihi_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column ryohi_karibarai_keihi_meisai.shiyoubi is '�g�p��';
comment on column ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column ryohi_karibarai_keihi_meisai.torihiki_name is '�����';
comment on column ryohi_karibarai_keihi_meisai.tekiyou is '�E�v';
comment on column ryohi_karibarai_keihi_meisai.zeiritsu is '�ŗ�';
comment on column ryohi_karibarai_keihi_meisai.shiharai_kingaku is '�x�����z';
comment on column ryohi_karibarai_keihi_meisai.hontai_kingaku is '�{�̋��z';
comment on column ryohi_karibarai_keihi_meisai.shouhizeigaku is '����Ŋz';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column ryohi_karibarai_keihi_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column ryohi_karibarai_keihi_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column ryohi_karibarai_keihi_meisai.torihikisaki_cd is '�����R�[�h';
comment on column ryohi_karibarai_keihi_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column ryohi_karibarai_keihi_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column ryohi_karibarai_keihi_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column ryohi_karibarai_keihi_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column ryohi_karibarai_keihi_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column ryohi_karibarai_keihi_meisai.uf1_cd is 'UF1�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf2_cd is 'UF2�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf3_cd is 'UF3�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf4_cd is 'UF4�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf5_cd is 'UF5�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf6_cd is 'UF6�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf7_cd is 'UF7�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf8_cd is 'UF8�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf9_cd is 'UF9�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf10_cd is 'UF10�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column ryohi_karibarai_keihi_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.project_name is '�v���W�F�N�g��';
comment on column ryohi_karibarai_keihi_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column ryohi_karibarai_keihi_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column ryohi_karibarai_keihi_meisai.touroku_time is '�o�^����';
comment on column ryohi_karibarai_keihi_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column ryohi_karibarai_keihi_meisai.koushin_time is '�X�V����';

COMMENT ON TABLE furikomi_bi_rule_hi IS '�U�������[��(���t)';






-- �ȉ�Ver 18.07.23.09����̃}�[�W

-- ������ɒ����ԃV���A���C�Y�f�[�^��ǉ�
CREATE FUNCTION seri(s TEXT, uid TEXT, dt DATE) RETURNS TEXT AS $$
DECLARE
    cnt integer;
BEGIN
    cnt := (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema=s AND table_name='teiki_jouhou_tmp' AND column_name='web_teiki_serialize_data');
    IF cnt >= 1 THEN
        RETURN (SELECT web_teiki_serialize_data FROM teiki_jouhou_tmp WHERE user_id=uid AND shiyou_kaishibi=dt);
    ELSE
        RETURN '';
    END IF;
END;
$$ LANGUAGE plpgsql immutable;

ALTER TABLE TEIKI_JOUHOU RENAME TO TEIKI_JOUHOU_TMP;
CREATE TABLE TEIKI_JOUHOU
(
    USER_ID VARCHAR(30) NOT NULL,
    SHIYOU_KAISHIBI DATE NOT NULL,
    SHIYOU_SHUURYOUBI DATE NOT NULL,
    INTRA_EKI_NAME VARCHAR(200),
    INTRA_ROSEN VARCHAR(200),
    WEB_TEIKI_KUKAN VARCHAR,
    WEB_TEIKI_SERIALIZE_DATA VARCHAR NOT NULL,
    TOUROKU_USER_ID VARCHAR(30) NOT NULL,
    TOUROKU_TIME TIMESTAMP NOT NULL,
    KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
    KOUSHIN_TIME TIMESTAMP NOT NULL,
    PRIMARY KEY (USER_ID, SHIYOU_KAISHIBI, SHIYOU_SHUURYOUBI)
) WITHOUT OIDS;
COMMENT ON TABLE TEIKI_JOUHOU IS '��������';
COMMENT ON COLUMN TEIKI_JOUHOU.USER_ID IS '���[�U�[ID';
COMMENT ON COLUMN TEIKI_JOUHOU.SHIYOU_KAISHIBI IS '�g�p�J�n��';
COMMENT ON COLUMN TEIKI_JOUHOU.SHIYOU_SHUURYOUBI IS '�g�p�I����';
COMMENT ON COLUMN TEIKI_JOUHOU.INTRA_EKI_NAME IS '�����ԉw��';
COMMENT ON COLUMN TEIKI_JOUHOU.INTRA_ROSEN IS '�����ԘH����';
COMMENT ON COLUMN TEIKI_JOUHOU.WEB_TEIKI_KUKAN IS '�����ԏ��';
COMMENT ON COLUMN TEIKI_JOUHOU.WEB_TEIKI_SERIALIZE_DATA IS '�����ԃV���A���C�Y�f�[�^';
COMMENT ON COLUMN TEIKI_JOUHOU.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN TEIKI_JOUHOU.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN TEIKI_JOUHOU.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN TEIKI_JOUHOU.KOUSHIN_TIME IS '�X�V����';

INSERT INTO TEIKI_JOUHOU
SELECT
    USER_ID
    ,SHIYOU_KAISHIBI
    ,SHIYOU_SHUURYOUBI
    ,INTRA_EKI_NAME
    ,INTRA_ROSEN
    ,WEB_TEIKI_KUKAN
    ,seri(:'SCHEMA_NAME', USER_ID, SHIYOU_KAISHIBI) -- WEB_TEIKI_SERIALIZE_DATA
    ,TOUROKU_USER_ID
    ,TOUROKU_TIME
    ,KOUSHIN_USER_ID
    ,KOUSHIN_TIME
FROM TEIKI_JOUHOU_TMP;

DROP TABLE TEIKI_JOUHOU_TMP;
DROP FUNCTION seri(text, text, date);

-- �ʋΒ���ɒ����ԃV���A���C�Y�f�[�^��ǉ�
CREATE FUNCTION seri(s TEXT, did TEXT) RETURNS TEXT AS $$
DECLARE
    cnt integer;
BEGIN
    cnt := (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema=s AND table_name='tsuukinteiki_tmp' AND column_name='teiki_serialize_data');
    IF cnt >= 1 THEN
        RETURN (SELECT teiki_serialize_data FROM tsuukinteiki_tmp WHERE denpyou_id=did);
    ELSE
        RETURN '';
    END IF;
END;
$$ LANGUAGE plpgsql immutable;

ALTER TABLE TSUUKINTEIKI RENAME TO TSUUKINTEIKI_TMP;
CREATE TABLE TSUUKINTEIKI
(
    DENPYOU_ID VARCHAR(19) NOT NULL,
    SHIYOU_KIKAN_KBN VARCHAR(2) NOT NULL,
    SHIYOU_KAISHIBI DATE NOT NULL,
    SHIYOU_SHUURYOUBI DATE NOT NULL,
    JYOUSHA_KUKAN VARCHAR NOT NULL,
    TEIKI_SERIALIZE_DATA VARCHAR NOT NULL,
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
COMMENT ON COLUMN TSUUKINTEIKI.TEIKI_SERIALIZE_DATA IS '�����ԃV���A���C�Y�f�[�^';
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

INSERT INTO TSUUKINTEIKI
SELECT
    DENPYOU_ID
    ,SHIYOU_KIKAN_KBN
    ,SHIYOU_KAISHIBI
    ,SHIYOU_SHUURYOUBI
    ,JYOUSHA_KUKAN
    ,seri(:'SCHEMA_NAME', DENPYOU_ID) -- TEIKI_SERIALIZE_DATA
    ,SHIHARAIBI
    ,TEKIYOU
    ,ZEIRITSU
    ,KINGAKU
    ,TENYUURYOKU_FLG
    ,HF1_CD
    ,HF1_NAME_RYAKUSHIKI
    ,HF2_CD
    ,HF2_NAME_RYAKUSHIKI
    ,HF3_CD
    ,HF3_NAME_RYAKUSHIKI
    ,HF4_CD
    ,HF4_NAME_RYAKUSHIKI
    ,HF5_CD
    ,HF5_NAME_RYAKUSHIKI
    ,HF6_CD
    ,HF6_NAME_RYAKUSHIKI
    ,HF7_CD
    ,HF7_NAME_RYAKUSHIKI
    ,HF8_CD
    ,HF8_NAME_RYAKUSHIKI
    ,HF9_CD
    ,HF9_NAME_RYAKUSHIKI
    ,HF10_CD
    ,HF10_NAME_RYAKUSHIKI
    ,SHIWAKE_EDANO
    ,TORIHIKI_NAME
    ,KARI_FUTAN_BUMON_CD
    ,KARI_FUTAN_BUMON_NAME
    ,TORIHIKISAKI_CD
    ,TORIHIKISAKI_NAME_RYAKUSHIKI
    ,KARI_KAMOKU_CD
    ,KARI_KAMOKU_NAME
    ,KARI_KAMOKU_EDABAN_CD
    ,KARI_KAMOKU_EDABAN_NAME
    ,KARI_KAZEI_KBN
    ,KASHI_FUTAN_BUMON_CD
    ,KASHI_FUTAN_BUMON_NAME
    ,KASHI_KAMOKU_CD
    ,KASHI_KAMOKU_NAME
    ,KASHI_KAMOKU_EDABAN_CD
    ,KASHI_KAMOKU_EDABAN_NAME
    ,KASHI_KAZEI_KBN
    ,UF1_CD
    ,UF1_NAME_RYAKUSHIKI
    ,UF2_CD
    ,UF2_NAME_RYAKUSHIKI
    ,UF3_CD
    ,UF3_NAME_RYAKUSHIKI
    ,UF4_CD
    ,UF4_NAME_RYAKUSHIKI
    ,UF5_CD
    ,UF5_NAME_RYAKUSHIKI
    ,UF6_CD
    ,UF6_NAME_RYAKUSHIKI
    ,UF7_CD
    ,UF7_NAME_RYAKUSHIKI
    ,UF8_CD
    ,UF8_NAME_RYAKUSHIKI
    ,UF9_CD
    ,UF9_NAME_RYAKUSHIKI
    ,UF10_CD
    ,UF10_NAME_RYAKUSHIKI
    ,PROJECT_CD
    ,PROJECT_NAME
    ,SEGMENT_CD
    ,SEGMENT_NAME_RYAKUSHIKI
    ,TEKIYOU_CD
    ,TOUROKU_USER_ID
    ,TOUROKU_TIME
    ,KOUSHIN_USER_ID
    ,KOUSHIN_TIME
FROM TSUUKINTEIKI_TMP;

DROP TABLE TSUUKINTEIKI_TMP;
DROP FUNCTION seri(text, text);


commit;