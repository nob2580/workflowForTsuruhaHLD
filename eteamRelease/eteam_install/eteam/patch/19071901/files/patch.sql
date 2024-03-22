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

-- �o�[���ݒ�
ALTER TABLE suitouchou_setting DROP CONSTRAINT IF EXISTS suitouchou_setting_PKEY;
ALTER TABLE suitouchou_setting RENAME TO suitouchou_setting_old;
create table suitouchou_setting (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , zandaka_type character varying(1) not null
  , futan_bumon_cd character varying(8) not null
  , futan_bumon_name character varying(20) not null
  , kamoku_gaibu_cd character varying(6) not null
  , kamoku_name character varying(22) not null
  , kamoku_edaban_cd character varying(12) not null
  , kamoku_edaban_name character varying(20) not null
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
  , kaishi_date date
  , kaishi_kesn integer
  , kaishi_zandaka numeric(15)
  , kaishi_zandaka_torikomi_zumi_flg character varying(1) not null
  , denpyou_no_tani_flg character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint suitouchou_setting_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no)
);
comment on table suitouchou_setting is '�o�[���ݒ�';
comment on column suitouchou_setting.denpyou_kbn is '�`�[�敪';
comment on column suitouchou_setting.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column suitouchou_setting.zandaka_type is '�c���^�C�v';
comment on column suitouchou_setting.futan_bumon_cd is '���S����R�[�h';
comment on column suitouchou_setting.futan_bumon_name is '���S���喼';
comment on column suitouchou_setting.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column suitouchou_setting.kamoku_name is '�Ȗږ�';
comment on column suitouchou_setting.kamoku_edaban_cd is '�Ȗڎ}�ԃR�[�h';
comment on column suitouchou_setting.kamoku_edaban_name is '�Ȗڎ}�Ԗ�';
comment on column suitouchou_setting.hf1_cd is 'HF1�R�[�h';
comment on column suitouchou_setting.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column suitouchou_setting.hf2_cd is 'HF2�R�[�h';
comment on column suitouchou_setting.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column suitouchou_setting.hf3_cd is 'HF3�R�[�h';
comment on column suitouchou_setting.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column suitouchou_setting.hf4_cd is 'HF4�R�[�h';
comment on column suitouchou_setting.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column suitouchou_setting.hf5_cd is 'HF5�R�[�h';
comment on column suitouchou_setting.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column suitouchou_setting.hf6_cd is 'HF6�R�[�h';
comment on column suitouchou_setting.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column suitouchou_setting.hf7_cd is 'HF7�R�[�h';
comment on column suitouchou_setting.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column suitouchou_setting.hf8_cd is 'HF8�R�[�h';
comment on column suitouchou_setting.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column suitouchou_setting.hf9_cd is 'HF9�R�[�h';
comment on column suitouchou_setting.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column suitouchou_setting.hf10_cd is 'HF10�R�[�h';
comment on column suitouchou_setting.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column suitouchou_setting.kaishi_date is '�J�n�N����';
comment on column suitouchou_setting.kaishi_kesn is '�J�n�������Z��';
comment on column suitouchou_setting.kaishi_zandaka is '�J�n�c��';
comment on column suitouchou_setting.kaishi_zandaka_torikomi_zumi_flg is '�J�n�c���捞�σt���O';
comment on column suitouchou_setting.denpyou_no_tani_flg is '�`�[�ԍ��P�ʃt���O';
comment on column suitouchou_setting.touroku_user_id is '�o�^���[�U�[ID';
comment on column suitouchou_setting.touroku_time is '�o�^����';
comment on column suitouchou_setting.koushin_user_id is '�X�V���[�U�[ID';
comment on column suitouchou_setting.koushin_time is '�X�V����';
INSERT INTO suitouchou_setting
SELECT
	 denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,zandaka_type
	,futan_bumon_cd
	,futan_bumon_name
	,kamoku_gaibu_cd
	,kamoku_name
	,kamoku_edaban_cd
	,kamoku_edaban_name
	,hf1_cd
	,hf1_name_ryakushiki
	,hf2_cd
	,hf2_name_ryakushiki
	,hf3_cd
	,hf3_name_ryakushiki
	,hf4_cd
	,hf4_name_ryakushiki
	,hf5_cd
	,hf5_name_ryakushiki
	,hf6_cd
	,hf6_name_ryakushiki
	,hf7_cd
	,hf7_name_ryakushiki
	,hf8_cd
	,hf8_name_ryakushiki
	,hf9_cd
	,hf9_name_ryakushiki
	,hf10_cd
	,hf10_name_ryakushiki
	,kaishi_date
	,null   -- kaishi_kesn
	,kaishi_zandaka
	,'0'   -- kaishi_zandaka_torikomi_zumi_flg
	,denpyou_no_tani_flg
	,touroku_user_id	
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM suitouchou_setting_old;
DROP TABLE suitouchou_setting_old;
UPDATE suitouchou_setting ss 
SET kaishi_kesn = (SELECT kesn FROM ki_kesn kk WHERE ss.kaishi_date BETWEEN from_date AND to_date);

-- �V�����������_�p�̓`�[�ԍ��̔ԃe�[�u���B
create table denpyou_serial_no_saiban_kyoten (
  denpyou_kbn character varying(4) not null
  , sequence_val integer not null
  , max_value integer not null
  , min_value integer not null
  , constraint denpyou_serial_no_saiban_kyoten_PKEY primary key (denpyou_kbn)
);
comment on table denpyou_serial_no_saiban_kyoten is '�`�[�ԍ��̔�(�������_)';
comment on column denpyou_serial_no_saiban_kyoten.denpyou_kbn is '�`�[�敪';
comment on column denpyou_serial_no_saiban_kyoten.sequence_val is '�V�[�P���X�l';
comment on column denpyou_serial_no_saiban_kyoten.max_value is '�ő�l';
comment on column denpyou_serial_no_saiban_kyoten.min_value is '�ŏ��l';
\copy denpyou_serial_no_saiban_kyoten FROM '.\files\csv\denpyou_serial_no_saiban_kyoten.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- �U�֓`�[�ݒ�Əo�[���ݒ�̌ʂ̓`�[�ԍ��t�ݒ�͕s�v�ɂȂ����̂ō폜
DROP TABLE zaimu_kyoten_nyuryoku_denpyou_no;
-- �������_�p�̓`�[�ԍ��̔ԃe�[�u�����L�[�l���ς��̂ł܂���ƍ폜
DROP TABLE zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban;


commit;
