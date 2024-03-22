SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- ��ʌ�������
DELETE FROM gamen_kengen_seigyo WHERE gamen_id LIKE 'Genyokin%';
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
	-- �u�������_���͌����v���u���_���͈ꗗ�v�ɁA�u�������_���̓p�^�[���I���v���u���_���̓p�^�[���I���v�ύX
	UPDATE gamen_kengen_seigyo SET gamen_name = '���_���͈ꗗ' WHERE gamen_name = '�������_���͌���';
	UPDATE gamen_kengen_seigyo SET gamen_name = '���_���̓p�^�[���I��' WHERE gamen_name = '�������_���̓p�^�[���I��';

-- ��ʍ��ڐ���
CREATE TABLE gamen_koumoku_seigyo_tmp AS SELECT * FROM gamen_koumoku_seigyo;
DELETE FROM gamen_koumoku_seigyo WHERE denpyou_kbn LIKE 'A%'OR denpyou_kbn LIKE 'Z%';
\copy gamen_koumoku_seigyo FROM '.\files\csv\gamen_koumoku_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE gamen_koumoku_seigyo new SET
  koumoku_name = (SELECT koumoku_name FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp);
UPDATE gamen_koumoku_seigyo new SET
   hyouji_flg  = (SELECT hyouji_flg   FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
  ,hissu_flg   = (SELECT hissu_flg    FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE hyouji_seigyo_flg = '1');
UPDATE gamen_koumoku_seigyo new SET
  pdf_hyouji_flg = (SELECT pdf_hyouji_flg FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE pdf_hyouji_seigyo_flg = '1');
UPDATE gamen_koumoku_seigyo new SET
  code_output_flg = (SELECT code_output_flg FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE code_output_seigyo_flg = '1');
DROP TABLE gamen_koumoku_seigyo_tmp;

--�����R�[�h ���I�v�V�����p���R�[�h�̓p�b�`�O�ɂȂ���΍폜
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- �x���˗��\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- ���������\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

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
	--OEPN21�A�g��val��OPEN21�A�g�ɃR�s�[����i�������_���͑Ή��j
	UPDATE setting_info new SET setting_val = (
	 SELECT setting_val
	 FROM setting_info_tmp tmp 
	 WHERE tmp.setting_name = replace(new.setting_name, '_kyoten', ''))
	WHERE new.setting_name LIKE '%_kyoten'; 
DROP TABLE setting_info_tmp;

-- �`�[��ʈꗗ�i�������_�j
DELETE FROM denpyou_shubetsu_ichiran_kyoten;
\copy denpyou_shubetsu_ichiran_kyoten FROM '.\files\csv\denpyou_shubetsu_ichiran_kyoten.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- ���a���o�[���A���a���o�[������
-- genyokin_suitouchou��suitouchou�ɓ��ꂷ�邽�ߕύX
create table suitouchou (
  denpyou_id character varying(19) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , denpyou_date date not null
  , nyukin_goukei numeric(15) not null
  , shukkin_goukei numeric(15) not null
  , toujitsu_zandaka numeric(15) not null
  , suitou_kamoku_cd character varying(6) not null
  , suitou_kamoku_name character varying(22) not null
  , suitou_kamoku_edaban_cd character varying(12) not null
  , suitou_kamoku_edaban_name character varying(20) not null
  , suitou_futan_bumon_cd character varying(8) not null
  , suitou_futan_bumon_name character varying(20) not null
  , bikou character varying(40) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint suitouchou_PKEY primary key (denpyou_id)
);

create table suitouchou_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , denpyou_date date not null
  , serial_no bigint not null
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , nyukin_goukei numeric(15)
  , shukkin_goukei numeric(15)
  , aite_kazei_kbn character varying(3) not null
  , aite_zeiritsu numeric(3) not null
  , aite_keigen_zeiritsu_kbn character varying(1) not null
  , fusen_color character varying(1) not null
  , meisai_touroku_user_id character varying(30) not null
  , meisai_touroku_shain_no character varying(15) not null
  , meisai_touroku_user_sei character varying(10) not null
  , meisai_touroku_user_mei character varying(10) not null
  , tenpu_file_edano character varying(40) not null
  , shiwake_serial_no bigserial not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint suitouchou_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

comment on table suitouchou is '���a���o�[��';
comment on column suitouchou.denpyou_id is '�`�[ID';
comment on column suitouchou.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column suitouchou.denpyou_date is '�`�[���t';
comment on column suitouchou.nyukin_goukei is '�������v';
comment on column suitouchou.shukkin_goukei is '�o�����v';
comment on column suitouchou.toujitsu_zandaka is '�����c��';
comment on column suitouchou.suitou_kamoku_cd is '�o�[�ȖڃR�[�h';
comment on column suitouchou.suitou_kamoku_name is '�o�[�Ȗږ�';
comment on column suitouchou.suitou_kamoku_edaban_cd is '�o�[�Ȗڎ}�ԃR�[�h';
comment on column suitouchou.suitou_kamoku_edaban_name is '�o�[�Ȗڎ}�Ԗ�';
comment on column suitouchou.suitou_futan_bumon_cd is '�o�[���S����R�[�h';
comment on column suitouchou.suitou_futan_bumon_name is '�o�[���S���喼';
comment on column suitouchou.bikou is '���l';
comment on column suitouchou.touroku_user_id is '�o�^���[�U�[ID';
comment on column suitouchou.touroku_time is '�o�^����';
comment on column suitouchou.koushin_user_id is '�X�V���[�U�[ID';
comment on column suitouchou.koushin_time is '�X�V����';

comment on table suitouchou_meisai is '���a���o�[������';
comment on column suitouchou_meisai.denpyou_id is '�`�[ID';
comment on column suitouchou_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column suitouchou_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column suitouchou_meisai.denpyou_date is '�`�[���t';
comment on column suitouchou_meisai.serial_no is '�V���A���ԍ�';
comment on column suitouchou_meisai.torihiki_name is '�����';
comment on column suitouchou_meisai.tekiyou is '�E�v';
comment on column suitouchou_meisai.nyukin_goukei is '����';
comment on column suitouchou_meisai.shukkin_goukei is '�o��';
comment on column suitouchou_meisai.aite_kazei_kbn is '�ېŋ敪';
comment on column suitouchou_meisai.aite_zeiritsu is '�ŗ�';
comment on column suitouchou_meisai.aite_keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column suitouchou_meisai.fusen_color is '�tⳃJ���[';
comment on column suitouchou_meisai.meisai_touroku_user_id is '���דo�^���[�U�[ID';
comment on column suitouchou_meisai.meisai_touroku_shain_no is '���דo�^���[�U�[�Ј�No';
comment on column suitouchou_meisai.meisai_touroku_user_sei is '���דo�^���[�U�[��';
comment on column suitouchou_meisai.meisai_touroku_user_mei is '���דo�^���[�U�[��';
comment on column suitouchou_meisai.tenpu_file_edano is '�Y�t�t�@�C���}��';
comment on column suitouchou_meisai.shiwake_serial_no is '�d��f�[�^�V���A��No';
comment on column suitouchou_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column suitouchou_meisai.touroku_time is '�o�^����';
comment on column suitouchou_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column suitouchou_meisai.koushin_time is '�X�V����';

INSERT INTO suitouchou SELECT * FROM genyokin_suitouchou;
DROP TABLE genyokin_suitouchou;
INSERT INTO suitouchou_meisai SELECT * FROM genyokin_suitouchou_meisai;
DROP TABLE genyokin_suitouchou_meisai;

-- �z���i�p�^�[���A�ȖځA����j
create table zaimu_kyoten_nyuryoku_haifu_pattern (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_name character varying(20) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_haifu_pattern_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,zaimu_kyoten_nyuryoku_haifu_pattern_no)
);
comment on table zaimu_kyoten_nyuryoku_haifu_pattern is '���_���͔z���p�^�[��';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.zaimu_kyoten_nyuryoku_haifu_pattern_no is '�z���p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.zaimu_kyoten_nyuryoku_haifu_pattern_name is '�z���p�^�[����';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.koushin_time is '�X�V����';

create table zaimu_kyoten_nyuryoku_haifu_bumon (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , futan_bumon_cd character varying(8) not null
  , haifu_rate numeric(7,4) not null
  , sagaku_flg character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp(6) without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp(6) without time zone not null
  , constraint zaimu_kyoten_nyuryoku_haifu_bumon_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,zaimu_kyoten_nyuryoku_haifu_pattern_no,yuukou_kigen_from,futan_bumon_cd)
);
comment on table zaimu_kyoten_nyuryoku_haifu_bumon is '���_���͔z������';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.zaimu_kyoten_nyuryoku_haifu_pattern_no is '���_���͔z���p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.yuukou_kigen_from is '�L�������J�n��';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.yuukou_kigen_to is '�L�������I����';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.futan_bumon_cd is '���S����R�[�h';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.haifu_rate is '�z����';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.sagaku_flg is '���z�t���O';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.koushin_time is '�X�V����';

create table zaimu_kyoten_nyuryoku_haifu_kamoku (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , kamoku_gaibu_cd character varying(8) not null
  , kamoku_edaban_cd character varying(12) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp(6) without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp(6) without time zone not null
  , constraint zaimu_kyoten_nyuryoku_haifu_kamoku_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,zaimu_kyoten_nyuryoku_haifu_pattern_no,yuukou_kigen_from,kamoku_gaibu_cd,kamoku_edaban_cd)
);
comment on table zaimu_kyoten_nyuryoku_haifu_kamoku is '���_���͔z���Ȗ�';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.zaimu_kyoten_nyuryoku_haifu_pattern_no is '���_���͔z���p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.yuukou_kigen_from is '�L�������J�n��';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.yuukou_kigen_to is '�L�������I����';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.kamoku_edaban_cd is '�Ȗڎ}�ԃR�[�h';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.koushin_time is '�X�V����';

-- �d��V���A���ԍ��̔ԁi�������_���́j
create table zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban (
  sequence_val bigint
);
comment on table zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban is '���_���͎d��V���A���ԍ��̔�';
comment on column zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban.sequence_val is '�V�[�P���X�l';
INSERT INTO zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban 
VALUES (CASE 
			WHEN (SELECT count(*) FROM zaimu_kyoten_shiwake_de3) > 0 THEN (SELECT MAX(serial_no) FROM zaimu_kyoten_shiwake_de3)
			WHEN (SELECT count(*) FROM zaimu_kyoten_shiwake_sias) > 0 THEN (SELECT MAX(serial_no) FROM zaimu_kyoten_shiwake_sias)
			ELSE 0 END);

-- ����U��
ALTER TABLE bumon_furikae DROP CONSTRAINT IF EXISTS bumon_furikae_PKEY;
ALTER TABLE bumon_furikae RENAME TO bumon_furikae_old;
create table bumon_furikae (
  denpyou_id character varying(19) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , kari_kingaku_goukei numeric(15) not null
  , kashi_kingaku_goukei numeric(15) not null
  , denpyou_date date not null
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
  , bikou character varying(40) not null
  , teikei_shiwake_user_id character varying(30) not null
  , teikei_shiwake_pattern_no Integer
  , bumon_tsukekae_flg character varying(1) not null
  , bumon_tsukekae_moto_serial_no bigint
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint bumon_furikae_PKEY primary key (denpyou_id)
);
comment on table bumon_furikae is '�U�֓`�[(���_)';
comment on column bumon_furikae.denpyou_id is '�`�[ID';
comment on column bumon_furikae.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column bumon_furikae.kari_kingaku_goukei is '�ؕ����z���v';
comment on column bumon_furikae.kashi_kingaku_goukei is '�ݕ����z���v';
comment on column bumon_furikae.denpyou_date is '�`�[���t';
comment on column bumon_furikae.hf1_cd is 'HF1�R�[�h';
comment on column bumon_furikae.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column bumon_furikae.hf2_cd is 'HF2�R�[�h';
comment on column bumon_furikae.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column bumon_furikae.hf3_cd is 'HF3�R�[�h';
comment on column bumon_furikae.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column bumon_furikae.hf4_cd is 'HF4�R�[�h';
comment on column bumon_furikae.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column bumon_furikae.hf5_cd is 'HF5�R�[�h';
comment on column bumon_furikae.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column bumon_furikae.hf6_cd is 'HF6�R�[�h';
comment on column bumon_furikae.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column bumon_furikae.hf7_cd is 'HF7�R�[�h';
comment on column bumon_furikae.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column bumon_furikae.hf8_cd is 'HF8�R�[�h';
comment on column bumon_furikae.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column bumon_furikae.hf9_cd is 'HF9�R�[�h';
comment on column bumon_furikae.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column bumon_furikae.hf10_cd is 'HF10�R�[�h';
comment on column bumon_furikae.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column bumon_furikae.bikou is '���l';
comment on column bumon_furikae.teikei_shiwake_user_id is '��^�d�󃆁[�U�[ID';
comment on column bumon_furikae.teikei_shiwake_pattern_no is '��^�d��p�^�[��No';
comment on column bumon_furikae.bumon_tsukekae_flg is '����t�փt���O';
comment on column bumon_furikae.bumon_tsukekae_moto_serial_no is '����t�֌��V���A���ԍ�';
comment on column bumon_furikae.touroku_user_id is '�o�^���[�U�[ID';
comment on column bumon_furikae.touroku_time is '�o�^����';
comment on column bumon_furikae.koushin_user_id is '�X�V���[�U�[ID';
comment on column bumon_furikae.koushin_time is '�X�V����';
INSERT INTO bumon_furikae
SELECT
	 denpyou_id
	,zaimu_kyoten_nyuryoku_pattern_no
	,kari_kingaku_goukei
	,kashi_kingaku_goukei
	,denpyou_date
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
	,bikou
	,teikei_shiwake_user_id
	,teikei_shiwake_pattern_no
	,'0'      --bumon_tsukekae_flg
	,0    --bumon_tsukekae_moto_serial_no
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM bumon_furikae_old;
DROP TABLE bumon_furikae_old;

-- ����U�֖���
ALTER TABLE bumon_furikae_meisai DROP CONSTRAINT IF EXISTS bumon_furikae_meisai_PKEY;
ALTER TABLE bumon_furikae_meisai RENAME TO bumon_furikae_meisai_old;
create table bumon_furikae_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , kari_tekiyou character varying(60) not null
  , kari_kingaku numeric(15) not null
  , kari_shouhizeigaku numeric(15)
  , kari_zeiritsu numeric(3) not null
  , kari_keigen_zeiritsu_kbn character varying(1) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , kari_torihikisaki_cd character varying(12) not null
  , kari_torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kari_bunri_kbn character varying(1) not null
  , kari_kobetsu_kbn character varying(1) not null
  , kari_uf1_cd character varying(20) not null
  , kari_uf1_name_ryakushiki character varying(20) not null
  , kari_uf2_cd character varying(20) not null
  , kari_uf2_name_ryakushiki character varying(20) not null
  , kari_uf3_cd character varying(20) not null
  , kari_uf3_name_ryakushiki character varying(20) not null
  , kari_uf4_cd character varying(20) not null
  , kari_uf4_name_ryakushiki character varying(20) not null
  , kari_uf5_cd character varying(20) not null
  , kari_uf5_name_ryakushiki character varying(20) not null
  , kari_uf6_cd character varying(20) not null
  , kari_uf6_name_ryakushiki character varying(20) not null
  , kari_uf7_cd character varying(20) not null
  , kari_uf7_name_ryakushiki character varying(20) not null
  , kari_uf8_cd character varying(20) not null
  , kari_uf8_name_ryakushiki character varying(20) not null
  , kari_uf9_cd character varying(20) not null
  , kari_uf9_name_ryakushiki character varying(20) not null
  , kari_uf10_cd character varying(20) not null
  , kari_uf10_name_ryakushiki character varying(20) not null
  , kari_project_cd character varying(12) not null
  , kari_project_name character varying(20) not null
  , kari_segment_cd character varying(8) not null
  , kari_segment_name character varying(20) not null
  , kari_taika numeric(15)
  , kari_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kari_shouhizeitaishou_kamoku_name character varying(22) not null
  , kari_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kari_shouhizeitaishou_zeiritsu numeric(3)
  , kari_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kari_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kari_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kari_heishu_cd character varying(4) not null
  , kari_rate numeric(11,5)
  , kari_gaika numeric(22,5)
  , kari_gaika_shouhizeigaku numeric(22,5)
  , kari_gaika_taika numeric(22,5)
  , kari_teikei_shiwake_edano integer
  , kari_shiwake_serial_no bigint
  , kashi_tekiyou character varying(60) not null
  , kashi_kingaku numeric(15) not null
  , kashi_shouhizeigaku numeric(15)
  , kashi_zeiritsu numeric(3) not null
  , kashi_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_torihikisaki_cd character varying(12) not null
  , kashi_torihikisaki_name_ryakushiki character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , kashi_bunri_kbn character varying(1) not null
  , kashi_kobetsu_kbn character varying(1) not null
  , kashi_uf1_cd character varying(20) not null
  , kashi_uf1_name_ryakushiki character varying(20) not null
  , kashi_uf2_cd character varying(20) not null
  , kashi_uf2_name_ryakushiki character varying(20) not null
  , kashi_uf3_cd character varying(20) not null
  , kashi_uf3_name_ryakushiki character varying(20) not null
  , kashi_uf4_cd character varying(20) not null
  , kashi_uf4_name_ryakushiki character varying(20) not null
  , kashi_uf5_cd character varying(20) not null
  , kashi_uf5_name_ryakushiki character varying(20) not null
  , kashi_uf6_cd character varying(20) not null
  , kashi_uf6_name_ryakushiki character varying(20) not null
  , kashi_uf7_cd character varying(20) not null
  , kashi_uf7_name_ryakushiki character varying(20) not null
  , kashi_uf8_cd character varying(20) not null
  , kashi_uf8_name_ryakushiki character varying(20) not null
  , kashi_uf9_cd character varying(20) not null
  , kashi_uf9_name_ryakushiki character varying(20) not null
  , kashi_uf10_cd character varying(20) not null
  , kashi_uf10_name_ryakushiki character varying(20) not null
  , kashi_project_cd character varying(12) not null
  , kashi_project_name character varying(20) not null
  , kashi_segment_cd character varying(8) not null
  , kashi_segment_name character varying(20) not null
  , kashi_taika numeric(15)
  , kashi_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kashi_shouhizeitaishou_kamoku_name character varying(22) not null
  , kashi_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kashi_shouhizeitaishou_zeiritsu numeric(3)
  , kashi_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kashi_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kashi_heishu_cd character varying(4) not null
  , kashi_rate numeric(11,5)
  , kashi_gaika numeric(22,5)
  , kashi_gaika_shouhizeigaku numeric(22,5)
  , kashi_gaika_taika numeric(22,5)
  , kashi_teikei_shiwake_edano integer
  , kashi_shiwake_serial_no bigint
  , gyou_kugiri character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint bumon_furikae_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table bumon_furikae_meisai is '�U�֓`�[����(���_)';
comment on column bumon_furikae_meisai.denpyou_id is '�`�[ID';
comment on column bumon_furikae_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column bumon_furikae_meisai.kari_tekiyou is '�ؕ��E�v';
comment on column bumon_furikae_meisai.kari_kingaku is '�ؕ����z';
comment on column bumon_furikae_meisai.kari_shouhizeigaku is '�ؕ�����Ŋz';
comment on column bumon_furikae_meisai.kari_zeiritsu is '�ؕ��ŗ�';
comment on column bumon_furikae_meisai.kari_keigen_zeiritsu_kbn is '�ؕ��y���ŗ��敪';
comment on column bumon_furikae_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column bumon_furikae_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column bumon_furikae_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column bumon_furikae_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column bumon_furikae_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column bumon_furikae_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column bumon_furikae_meisai.kari_torihikisaki_cd is '�ؕ������R�[�h';
comment on column bumon_furikae_meisai.kari_torihikisaki_name_ryakushiki is '�ؕ�����於�i�����j';
comment on column bumon_furikae_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column bumon_furikae_meisai.kari_bunri_kbn is '�ؕ������敪';
comment on column bumon_furikae_meisai.kari_kobetsu_kbn is '�ؕ��ʋ敪';
comment on column bumon_furikae_meisai.kari_uf1_cd is '�ؕ�UF1�R�[�h';
comment on column bumon_furikae_meisai.kari_uf1_name_ryakushiki is '�ؕ�UF1���i�����j';
comment on column bumon_furikae_meisai.kari_uf2_cd is '�ؕ�UF2�R�[�h';
comment on column bumon_furikae_meisai.kari_uf2_name_ryakushiki is '�ؕ�UF2���i�����j';
comment on column bumon_furikae_meisai.kari_uf3_cd is '�ؕ�UF3�R�[�h';
comment on column bumon_furikae_meisai.kari_uf3_name_ryakushiki is '�ؕ�UF3���i�����j';
comment on column bumon_furikae_meisai.kari_uf4_cd is '�ؕ�UF4�R�[�h';
comment on column bumon_furikae_meisai.kari_uf4_name_ryakushiki is '�ؕ�UF4���i�����j';
comment on column bumon_furikae_meisai.kari_uf5_cd is '�ؕ�UF5�R�[�h';
comment on column bumon_furikae_meisai.kari_uf5_name_ryakushiki is '�ؕ�UF5���i�����j';
comment on column bumon_furikae_meisai.kari_uf6_cd is '�ؕ�UF6�R�[�h';
comment on column bumon_furikae_meisai.kari_uf6_name_ryakushiki is '�ؕ�UF6���i�����j';
comment on column bumon_furikae_meisai.kari_uf7_cd is '�ؕ�UF7�R�[�h';
comment on column bumon_furikae_meisai.kari_uf7_name_ryakushiki is '�ؕ�UF7���i�����j';
comment on column bumon_furikae_meisai.kari_uf8_cd is '�ؕ�UF8�R�[�h';
comment on column bumon_furikae_meisai.kari_uf8_name_ryakushiki is '�ؕ�UF8���i�����j';
comment on column bumon_furikae_meisai.kari_uf9_cd is '�ؕ�UF9�R�[�h';
comment on column bumon_furikae_meisai.kari_uf9_name_ryakushiki is '�ؕ�UF9���i�����j';
comment on column bumon_furikae_meisai.kari_uf10_cd is '�ؕ�UF10�R�[�h';
comment on column bumon_furikae_meisai.kari_uf10_name_ryakushiki is '�ؕ�UF10���i�����j';
comment on column bumon_furikae_meisai.kari_project_cd is '�ؕ��v���W�F�N�g�R�[�h';
comment on column bumon_furikae_meisai.kari_project_name is '�ؕ��v���W�F�N�g��';
comment on column bumon_furikae_meisai.kari_segment_cd is '�ؕ��Z�O�����g�R�[�h';
comment on column bumon_furikae_meisai.kari_segment_name is '�ؕ��Z�O�����g��';
comment on column bumon_furikae_meisai.kari_taika is '�ؕ��Ή�';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_kamoku_cd is '�ؕ�����őΏۉȖڃR�[�h';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_kamoku_name is '�ؕ�����őΏۉȖږ���';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_kazei_kbn is '�ؕ�����őΏۉېŋ敪';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_zeiritsu is '�ؕ�����őΏېŗ�';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_keigen_zeiritsu_kbn is '�ؕ�����őΏیy���ŗ��敪';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_bunri_kbn is '�ؕ�����őΏە����敪';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_kobetsu_kbn is '�ؕ�����őΏیʋ敪';
comment on column bumon_furikae_meisai.kari_heishu_cd is '�ؕ�����R�[�h';
comment on column bumon_furikae_meisai.kari_rate is '�ؕ����[�g';
comment on column bumon_furikae_meisai.kari_gaika is '�ؕ��O�݋��z';
comment on column bumon_furikae_meisai.kari_gaika_shouhizeigaku is '�ؕ��O�ݏ���Ŋz';
comment on column bumon_furikae_meisai.kari_gaika_taika is '�ؕ��O�ݑΉ�';
comment on column bumon_furikae_meisai.kari_teikei_shiwake_edano is '�ؕ���^�d��}�ԍ�';
comment on column bumon_furikae_meisai.kari_shiwake_serial_no is '�ؕ��d��f�[�^�V���A��No';
comment on column bumon_furikae_meisai.kashi_tekiyou is '�ݕ��E�v';
comment on column bumon_furikae_meisai.kashi_kingaku is '�ݕ����z';
comment on column bumon_furikae_meisai.kashi_shouhizeigaku is '�ݕ�����Ŋz';
comment on column bumon_furikae_meisai.kashi_zeiritsu is '�ݕ��ŗ�';
comment on column bumon_furikae_meisai.kashi_keigen_zeiritsu_kbn is '�ݕ��y���ŗ��敪';
comment on column bumon_furikae_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column bumon_furikae_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column bumon_furikae_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column bumon_furikae_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column bumon_furikae_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column bumon_furikae_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column bumon_furikae_meisai.kashi_torihikisaki_cd is '�ݕ������R�[�h';
comment on column bumon_furikae_meisai.kashi_torihikisaki_name_ryakushiki is '�ݕ�����於�i�����j';
comment on column bumon_furikae_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column bumon_furikae_meisai.kashi_bunri_kbn is '�ݕ������敪';
comment on column bumon_furikae_meisai.kashi_kobetsu_kbn is '�ݕ��ʋ敪';
comment on column bumon_furikae_meisai.kashi_uf1_cd is '�ݕ�UF1�R�[�h';
comment on column bumon_furikae_meisai.kashi_uf1_name_ryakushiki is '�ݕ�UF1���i�����j';
comment on column bumon_furikae_meisai.kashi_uf2_cd is '�ݕ�UF2�R�[�h';
comment on column bumon_furikae_meisai.kashi_uf2_name_ryakushiki is '�ݕ�UF2���i�����j';
comment on column bumon_furikae_meisai.kashi_uf3_cd is '�ݕ�UF3�R�[�h';
comment on column bumon_furikae_meisai.kashi_uf3_name_ryakushiki is '�ݕ�UF3���i�����j';
comment on column bumon_furikae_meisai.kashi_uf4_cd is '�ݕ�UF4�R�[�h';
comment on column bumon_furikae_meisai.kashi_uf4_name_ryakushiki is '�ݕ�UF4���i�����j';
comment on column bumon_furikae_meisai.kashi_uf5_cd is '�ݕ�UF5�R�[�h';
comment on column bumon_furikae_meisai.kashi_uf5_name_ryakushiki is '�ݕ�UF5���i�����j';
comment on column bumon_furikae_meisai.kashi_uf6_cd is '�ݕ�UF6�R�[�h';
comment on column bumon_furikae_meisai.kashi_uf6_name_ryakushiki is '�ݕ�UF6���i�����j';
comment on column bumon_furikae_meisai.kashi_uf7_cd is '�ݕ�UF7�R�[�h';
comment on column bumon_furikae_meisai.kashi_uf7_name_ryakushiki is '�ݕ�UF7���i�����j';
comment on column bumon_furikae_meisai.kashi_uf8_cd is '�ݕ�UF8�R�[�h';
comment on column bumon_furikae_meisai.kashi_uf8_name_ryakushiki is '�ݕ�UF8���i�����j';
comment on column bumon_furikae_meisai.kashi_uf9_cd is '�ݕ�UF9�R�[�h';
comment on column bumon_furikae_meisai.kashi_uf9_name_ryakushiki is '�ݕ�UF9���i�����j';
comment on column bumon_furikae_meisai.kashi_uf10_cd is '�ݕ�UF10�R�[�h';
comment on column bumon_furikae_meisai.kashi_uf10_name_ryakushiki is '�ݕ�UF10���i�����j';
comment on column bumon_furikae_meisai.kashi_project_cd is '�ݕ��v���W�F�N�g�R�[�h';
comment on column bumon_furikae_meisai.kashi_project_name is '�ݕ��v���W�F�N�g��';
comment on column bumon_furikae_meisai.kashi_segment_cd is '�ݕ��Z�O�����g�R�[�h';
comment on column bumon_furikae_meisai.kashi_segment_name is '�ݕ��Z�O�����g��';
comment on column bumon_furikae_meisai.kashi_taika is '�ݕ��Ή�';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_kamoku_cd is '�ݕ�����őΏۉȖڃR�[�h';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_kamoku_name is '�ݕ�����őΏۉȖږ���';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_kazei_kbn is '�ݕ�����őΏۉېŋ敪';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_zeiritsu is '�ݕ�����őΏېŗ�';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_keigen_zeiritsu_kbn is '�ݕ�����őΏیy���ŗ��敪';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_bunri_kbn is '�ݕ�����őΏە����敪';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_kobetsu_kbn is '�ݕ�����őΏیʋ敪';
comment on column bumon_furikae_meisai.kashi_heishu_cd is '�ݕ�����R�[�h';
comment on column bumon_furikae_meisai.kashi_rate is '�ݕ����[�g';
comment on column bumon_furikae_meisai.kashi_gaika is '�ݕ��O�݋��z';
comment on column bumon_furikae_meisai.kashi_gaika_shouhizeigaku is '�ݕ��O�ݏ���Ŋz';
comment on column bumon_furikae_meisai.kashi_gaika_taika is '�ݕ��O�ݑΉ�';
comment on column bumon_furikae_meisai.kashi_teikei_shiwake_edano is '�ݕ���^�d��}�ԍ�';
comment on column bumon_furikae_meisai.kashi_shiwake_serial_no is '�ݕ��d��f�[�^�V���A��No';
comment on column bumon_furikae_meisai.gyou_kugiri is '�s��؂�';
comment on column bumon_furikae_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column bumon_furikae_meisai.touroku_time is '�o�^����';
comment on column bumon_furikae_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column bumon_furikae_meisai.koushin_time is '�X�V����';
INSERT INTO bumon_furikae_meisai
SELECT
	denpyou_id
	,denpyou_edano
	,kari_tekiyou
	,kari_kingaku
	,kari_shouhizeigaku
	,kari_zeiritsu
	,kari_keigen_zeiritsu_kbn
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,kari_torihikisaki_cd
	,kari_torihikisaki_name_ryakushiki
	,kari_kazei_kbn
	,kari_bunri_kbn
	,kari_kobetsu_kbn
	,kari_uf1_cd
	,kari_uf1_name_ryakushiki
	,kari_uf2_cd
	,kari_uf2_name_ryakushiki
	,kari_uf3_cd
	,kari_uf3_name_ryakushiki
	,kari_uf4_cd
	,kari_uf4_name_ryakushiki
	,kari_uf5_cd
	,kari_uf5_name_ryakushiki
	,kari_uf6_cd
	,kari_uf6_name_ryakushiki
	,kari_uf7_cd
	,kari_uf7_name_ryakushiki
	,kari_uf8_cd
	,kari_uf8_name_ryakushiki
	,kari_uf9_cd
	,kari_uf9_name_ryakushiki
	,kari_uf10_cd
	,kari_uf10_name_ryakushiki
	,kari_project_cd
	,kari_project_name
	,kari_segment_cd
	,kari_segment_name
	,kari_taika
	,kari_shouhizeitaishou_kamoku_cd
	,kari_shouhizeitaishou_kamoku_name
	,kari_shouhizeitaishou_kazei_kbn
	,kari_shouhizeitaishou_zeiritsu
	,kari_shouhizeitaishou_keigen_zeiritsu_kbn
	,kari_shouhizeitaishou_bunri_kbn
	,kari_shouhizeitaishou_kobetsu_kbn
	,kari_heishu_cd
	,kari_rate
	,kari_gaika
	,kari_gaika_shouhizeigaku
	,kari_gaika_taika
	,0   --kari_teikei_shiwake_edano
	,0   --kari_shiwake_serial_no
	,kashi_tekiyou
	,kashi_kingaku
	,kashi_shouhizeigaku
	,kashi_zeiritsu
	,kashi_keigen_zeiritsu_kbn
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_torihikisaki_cd
	,kashi_torihikisaki_name_ryakushiki
	,kashi_kazei_kbn
	,kashi_bunri_kbn
	,kashi_kobetsu_kbn
	,kashi_uf1_cd
	,kashi_uf1_name_ryakushiki
	,kashi_uf2_cd
	,kashi_uf2_name_ryakushiki
	,kashi_uf3_cd
	,kashi_uf3_name_ryakushiki
	,kashi_uf4_cd
	,kashi_uf4_name_ryakushiki
	,kashi_uf5_cd
	,kashi_uf5_name_ryakushiki
	,kashi_uf6_cd
	,kashi_uf6_name_ryakushiki
	,kashi_uf7_cd
	,kashi_uf7_name_ryakushiki
	,kashi_uf8_cd
	,kashi_uf8_name_ryakushiki
	,kashi_uf9_cd
	,kashi_uf9_name_ryakushiki
	,kashi_uf10_cd
	,kashi_uf10_name_ryakushiki
	,kashi_project_cd
	,kashi_project_name
	,kashi_segment_cd
	,kashi_segment_name
	,kashi_taika
	,kashi_shouhizeitaishou_kamoku_cd
	,kashi_shouhizeitaishou_kamoku_name
	,kashi_shouhizeitaishou_kazei_kbn
	,kashi_shouhizeitaishou_zeiritsu
	,kashi_shouhizeitaishou_keigen_zeiritsu_kbn
	,kashi_shouhizeitaishou_bunri_kbn
	,kashi_shouhizeitaishou_kobetsu_kbn
	,kashi_heishu_cd
	,kashi_rate
	,kashi_gaika
	,kashi_gaika_shouhizeigaku
	,kashi_gaika_taika
	,0    --kashi_teikei_shiwake_edano
	,0    --kashi_shiwake_serial_no
	,gyou_kugiri
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM bumon_furikae_meisai_old;
DROP TABLE bumon_furikae_meisai_old;

--   ���F�ς݂̓`�[�́A�ؕ��d��V���A���ԍ��Ƒݕ��d��V���A���ԍ����̔Ԃ���
WITH GET_NUM AS(
	SELECT row_number() OVER (ORDER BY m.denpyou_id, m.denpyou_edano) * 2 as num, m.* 
	FROM bumon_furikae_meisai m
	INNER JOIN denpyou d ON d.denpyou_id = m.denpyou_id
	WHERE d.denpyou_joutai = '30'
)
UPDATE bumon_furikae_meisai mm
SET kari_shiwake_serial_no = (SELECT (SELECT sequence_val + h.num -1 FROM zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban) 
										FROM bumon_furikae_meisai m
										INNER JOIN GET_NUM h ON h.denpyou_id = m.denpyou_id AND h.denpyou_edano = m.denpyou_edano 
										WHERE m.denpyou_id = mm.denpyou_id AND m.denpyou_edano = mm.denpyou_edano)
	,kashi_shiwake_serial_no = (SELECT (SELECT sequence_val + h.num FROM zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban) 
										 FROM bumon_furikae_meisai m
										 INNER JOIN GET_NUM h ON h.denpyou_id = m.denpyou_id AND h.denpyou_edano = m.denpyou_edano 
										 WHERE m.denpyou_id = mm.denpyou_id AND m.denpyou_edano = mm.denpyou_edano)
WHERE mm.denpyou_id IN (SELECT denpyou_id FROM denpyou WHERE denpyou_joutai = '30');
--   �̔ԃe�[�u���̃V�[�P���X�ԍ�����L�̍X�V�ɍ��킹�ĕς���
UPDATE zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban 
SET sequence_val = sequence_val + (SELECT count(*) * 2 
									FROM bumon_furikae_meisai m
									INNER JOIN denpyou d ON d.denpyou_id = m.denpyou_id 
									WHERE d.denpyou_joutai = '30');


-- �U�֕���ݒ�
ALTER TABLE bumon_furikae_setting DROP CONSTRAINT IF EXISTS bumon_furikae_setting_PKEY;
ALTER TABLE bumon_furikae_setting RENAME TO bumon_furikae_setting_old;
create table bumon_furikae_setting (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , taishaku_tekiyou_flg character varying(1) not null
  , bumon_tsukekae_shiyou_flg character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint bumon_furikae_setting_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no)
);
comment on table bumon_furikae_setting is '����U�֐ݒ�';
comment on column bumon_furikae_setting.denpyou_kbn is '�`�[�敪';
comment on column bumon_furikae_setting.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column bumon_furikae_setting.taishaku_tekiyou_flg is '�ݎؓE�v�t���O';
comment on column bumon_furikae_setting.bumon_tsukekae_shiyou_flg is '����t�֎g�p�t���O';
comment on column bumon_furikae_setting.touroku_user_id is '�o�^���[�U�[ID';
comment on column bumon_furikae_setting.touroku_time is '�o�^����';
comment on column bumon_furikae_setting.koushin_user_id is '�X�V���[�U�[ID';
comment on column bumon_furikae_setting.koushin_time is '�X�V����';
INSERT INTO bumon_furikae_setting
SELECT
	denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,taishaku_tekiyou_flg
	,'0'   --bumon_tsukekae_shiyou_flg
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM bumon_furikae_setting_old;
DROP TABLE bumon_furikae_setting_old;

-- ��^�d�󖾍�
ALTER TABLE teikei_shiwake_meisai DROP CONSTRAINT IF EXISTS teikei_shiwake_meisai_PKEY;
ALTER TABLE teikei_shiwake_meisai RENAME TO teikei_shiwake_meisai_old;
create table teikei_shiwake_meisai (
  user_id character varying(30) not null
  , teikei_shiwake_pattern_no integer not null
  , teikei_shiwake_edano integer not null
  , kari_tekiyou character varying(60) not null
  , kari_kingaku numeric(15) not null
  , kari_shouhizeigaku numeric(15)
  , kari_zeiritsu numeric(3) not null
  , kari_keigen_zeiritsu_kbn character varying(1) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , kari_torihikisaki_cd character varying(12) not null
  , kari_torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kari_bunri_kbn character varying(1) not null
  , kari_kobetsu_kbn character varying(1) not null
  , kari_uf1_cd character varying(20) not null
  , kari_uf1_name_ryakushiki character varying(20) not null
  , kari_uf2_cd character varying(20) not null
  , kari_uf2_name_ryakushiki character varying(20) not null
  , kari_uf3_cd character varying(20) not null
  , kari_uf3_name_ryakushiki character varying(20) not null
  , kari_uf4_cd character varying(20) not null
  , kari_uf4_name_ryakushiki character varying(20) not null
  , kari_uf5_cd character varying(20) not null
  , kari_uf5_name_ryakushiki character varying(20) not null
  , kari_uf6_cd character varying(20) not null
  , kari_uf6_name_ryakushiki character varying(20) not null
  , kari_uf7_cd character varying(20) not null
  , kari_uf7_name_ryakushiki character varying(20) not null
  , kari_uf8_cd character varying(20) not null
  , kari_uf8_name_ryakushiki character varying(20) not null
  , kari_uf9_cd character varying(20) not null
  , kari_uf9_name_ryakushiki character varying(20) not null
  , kari_uf10_cd character varying(20) not null
  , kari_uf10_name_ryakushiki character varying(20) not null
  , kari_project_cd character varying(12) not null
  , kari_project_name character varying(20) not null
  , kari_segment_cd character varying(8) not null
  , kari_segment_name character varying(20) not null
  , kari_taika numeric(15)
  , kari_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kari_shouhizeitaishou_kamoku_name character varying(22) not null
  , kari_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kari_shouhizeitaishou_zeiritsu numeric(3)
  , kari_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kari_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kari_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kari_heishu_cd character varying(1) not null
  , kari_rate numeric(11,5)
  , kari_gaika numeric(22,5)
  , kari_gaika_shouhizeigaku numeric(22,5)
  , kari_gaika_taika numeric(22,5)
  , kari_zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , kashi_tekiyou character varying(60) not null
  , kashi_kingaku numeric(15) not null
  , kashi_shouhizeigaku numeric(15)
  , kashi_zeiritsu numeric(3) not null
  , kashi_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_torihikisaki_cd character varying(12) not null
  , kashi_torihikisaki_name_ryakushiki character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , kashi_bunri_kbn character varying(1) not null
  , kashi_kobetsu_kbn character varying(1) not null
  , kashi_uf1_cd character varying(20) not null
  , kashi_uf1_name_ryakushiki character varying(20) not null
  , kashi_uf2_cd character varying(20) not null
  , kashi_uf2_name_ryakushiki character varying(20) not null
  , kashi_uf3_cd character varying(20) not null
  , kashi_uf3_name_ryakushiki character varying(20) not null
  , kashi_uf4_cd character varying(20) not null
  , kashi_uf4_name_ryakushiki character varying(20) not null
  , kashi_uf5_cd character varying(20) not null
  , kashi_uf5_name_ryakushiki character varying(20) not null
  , kashi_uf6_cd character varying(20) not null
  , kashi_uf6_name_ryakushiki character varying(20) not null
  , kashi_uf7_cd character varying(20) not null
  , kashi_uf7_name_ryakushiki character varying(20) not null
  , kashi_uf8_cd character varying(20) not null
  , kashi_uf8_name_ryakushiki character varying(20) not null
  , kashi_uf9_cd character varying(20) not null
  , kashi_uf9_name_ryakushiki character varying(20) not null
  , kashi_uf10_cd character varying(20) not null
  , kashi_uf10_name_ryakushiki character varying(20) not null
  , kashi_project_cd character varying(12) not null
  , kashi_project_name character varying(20) not null
  , kashi_segment_cd character varying(8) not null
  , kashi_segment_name character varying(20) not null
  , kashi_taika numeric(15)
  , kashi_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kashi_shouhizeitaishou_kamoku_name character varying(22) not null
  , kashi_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kashi_shouhizeitaishou_zeiritsu numeric(3)
  , kashi_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kashi_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kashi_heishu_cd character varying(1) not null
  , kashi_rate numeric(11,5)
  , kashi_gaika numeric(22,5)
  , kashi_gaika_shouhizeigaku numeric(22,5)
  , kashi_gaika_taika numeric(22,5)
  , kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , gyou_kugiri character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint teikei_shiwake_meisai_PKEY primary key (user_id,teikei_shiwake_pattern_no,teikei_shiwake_edano)
);
comment on table teikei_shiwake_meisai is '��^�d�󖾍�';
comment on column teikei_shiwake_meisai.user_id is '���[�U�[ID';
comment on column teikei_shiwake_meisai.teikei_shiwake_pattern_no is '��^�d��p�^�[���ԍ�';
comment on column teikei_shiwake_meisai.teikei_shiwake_edano is '��^�d��}�ԍ�';
comment on column teikei_shiwake_meisai.kari_tekiyou is '�ؕ��E�v';
comment on column teikei_shiwake_meisai.kari_kingaku is '�ؕ����z';
comment on column teikei_shiwake_meisai.kari_shouhizeigaku is '�ؕ�����Ŋz';
comment on column teikei_shiwake_meisai.kari_zeiritsu is '�ؕ��ŗ�';
comment on column teikei_shiwake_meisai.kari_keigen_zeiritsu_kbn is '�ؕ��y���ŗ��敪';
comment on column teikei_shiwake_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column teikei_shiwake_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column teikei_shiwake_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column teikei_shiwake_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column teikei_shiwake_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column teikei_shiwake_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column teikei_shiwake_meisai.kari_torihikisaki_cd is '�ؕ������R�[�h';
comment on column teikei_shiwake_meisai.kari_torihikisaki_name_ryakushiki is '�ؕ�����於�i�����j';
comment on column teikei_shiwake_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column teikei_shiwake_meisai.kari_bunri_kbn is '�ؕ������敪';
comment on column teikei_shiwake_meisai.kari_kobetsu_kbn is '�ؕ��ʋ敪';
comment on column teikei_shiwake_meisai.kari_uf1_cd is '�ؕ�UF1�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf1_name_ryakushiki is '�ؕ�UF1���i�����j';
comment on column teikei_shiwake_meisai.kari_uf2_cd is '�ؕ�UF2�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf2_name_ryakushiki is '�ؕ�UF2���i�����j';
comment on column teikei_shiwake_meisai.kari_uf3_cd is '�ؕ�UF3�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf3_name_ryakushiki is '�ؕ�UF3���i�����j';
comment on column teikei_shiwake_meisai.kari_uf4_cd is '�ؕ�UF4�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf4_name_ryakushiki is '�ؕ�UF4���i�����j';
comment on column teikei_shiwake_meisai.kari_uf5_cd is '�ؕ�UF5�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf5_name_ryakushiki is '�ؕ�UF5���i�����j';
comment on column teikei_shiwake_meisai.kari_uf6_cd is '�ؕ�UF6�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf6_name_ryakushiki is '�ؕ�UF6���i�����j';
comment on column teikei_shiwake_meisai.kari_uf7_cd is '�ؕ�UF7�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf7_name_ryakushiki is '�ؕ�UF7���i�����j';
comment on column teikei_shiwake_meisai.kari_uf8_cd is '�ؕ�UF8�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf8_name_ryakushiki is '�ؕ�UF8���i�����j';
comment on column teikei_shiwake_meisai.kari_uf9_cd is '�ؕ�UF9�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf9_name_ryakushiki is '�ؕ�UF9���i�����j';
comment on column teikei_shiwake_meisai.kari_uf10_cd is '�ؕ�UF10�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf10_name_ryakushiki is '�ؕ�UF10���i�����j';
comment on column teikei_shiwake_meisai.kari_project_cd is '�ؕ��v���W�F�N�g�R�[�h';
comment on column teikei_shiwake_meisai.kari_project_name is '�ؕ��v���W�F�N�g��';
comment on column teikei_shiwake_meisai.kari_segment_cd is '�ؕ��Z�O�����g�R�[�h';
comment on column teikei_shiwake_meisai.kari_segment_name is '�ؕ��Z�O�����g��';
comment on column teikei_shiwake_meisai.kari_taika is '�ؕ��Ή�';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kamoku_cd is '�ؕ�����őΏۉȖڃR�[�h';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kamoku_name is '�ؕ�����őΏۉȖږ���';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kazei_kbn is '�ؕ�����őΏۉېŋ敪';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_zeiritsu is '�ؕ�����őΏېŗ�';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_keigen_zeiritsu_kbn is '�ؕ�����őΏیy���ŗ��敪';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_bunri_kbn is '�ؕ�����őΏە����敪';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kobetsu_kbn is '�ؕ�����őΏیʋ敪';
comment on column teikei_shiwake_meisai.kari_heishu_cd is '�ؕ�����R�[�h';
comment on column teikei_shiwake_meisai.kari_rate is '�ؕ����[�g';
comment on column teikei_shiwake_meisai.kari_gaika is '�ؕ��O�݋��z';
comment on column teikei_shiwake_meisai.kari_gaika_shouhizeigaku is '�ؕ��O�ݏ���Ŋz';
comment on column teikei_shiwake_meisai.kari_gaika_taika is '�ؕ��O�ݑΉ�';
comment on column teikei_shiwake_meisai.kari_zaimu_kyoten_nyuryoku_haifu_pattern_no is '�ؕ����_���͔z���p�^�[��No';
comment on column teikei_shiwake_meisai.kashi_tekiyou is '�ݕ��E�v';
comment on column teikei_shiwake_meisai.kashi_kingaku is '�ݕ����z';
comment on column teikei_shiwake_meisai.kashi_shouhizeigaku is '�ݕ�����Ŋz';
comment on column teikei_shiwake_meisai.kashi_zeiritsu is '�ݕ��ŗ�';
comment on column teikei_shiwake_meisai.kashi_keigen_zeiritsu_kbn is '�ݕ��y���ŗ��敪';
comment on column teikei_shiwake_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column teikei_shiwake_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column teikei_shiwake_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column teikei_shiwake_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column teikei_shiwake_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column teikei_shiwake_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column teikei_shiwake_meisai.kashi_torihikisaki_cd is '�ݕ������R�[�h';
comment on column teikei_shiwake_meisai.kashi_torihikisaki_name_ryakushiki is '�ݕ�����於�i�����j';
comment on column teikei_shiwake_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column teikei_shiwake_meisai.kashi_bunri_kbn is '�ݕ������敪';
comment on column teikei_shiwake_meisai.kashi_kobetsu_kbn is '�ݕ��ʋ敪';
comment on column teikei_shiwake_meisai.kashi_uf1_cd is '�ݕ�UF1�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf1_name_ryakushiki is '�ݕ�UF1���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf2_cd is '�ݕ�UF2�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf2_name_ryakushiki is '�ݕ�UF2���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf3_cd is '�ݕ�UF3�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf3_name_ryakushiki is '�ݕ�UF3���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf4_cd is '�ݕ�UF4�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf4_name_ryakushiki is '�ݕ�UF4���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf5_cd is '�ݕ�UF5�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf5_name_ryakushiki is '�ݕ�UF5���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf6_cd is '�ݕ�UF6�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf6_name_ryakushiki is '�ݕ�UF6���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf7_cd is '�ݕ�UF7�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf7_name_ryakushiki is '�ݕ�UF7���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf8_cd is '�ݕ�UF8�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf8_name_ryakushiki is '�ݕ�UF8���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf9_cd is '�ݕ�UF9�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf9_name_ryakushiki is '�ݕ�UF9���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf10_cd is '�ݕ�UF10�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf10_name_ryakushiki is '�ݕ�UF10���i�����j';
comment on column teikei_shiwake_meisai.kashi_project_cd is '�ݕ��v���W�F�N�g�R�[�h';
comment on column teikei_shiwake_meisai.kashi_project_name is '�ݕ��v���W�F�N�g��';
comment on column teikei_shiwake_meisai.kashi_segment_cd is '�ݕ��Z�O�����g�R�[�h';
comment on column teikei_shiwake_meisai.kashi_segment_name is '�ݕ��Z�O�����g��';
comment on column teikei_shiwake_meisai.kashi_taika is '�ݕ��Ή�';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kamoku_cd is '�ݕ�����őΏۉȖڃR�[�h';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kamoku_name is '�ݕ�����őΏۉȖږ���';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kazei_kbn is '�ݕ�����őΏۉېŋ敪';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_zeiritsu is '�ݕ�����őΏېŗ�';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_keigen_zeiritsu_kbn is '�ݕ�����őΏیy���ŗ��敪';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_bunri_kbn is '�ݕ�����őΏە����敪';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kobetsu_kbn is '�ݕ�����őΏیʋ敪';
comment on column teikei_shiwake_meisai.kashi_heishu_cd is '�ݕ�����R�[�h';
comment on column teikei_shiwake_meisai.kashi_rate is '�ݕ����[�g';
comment on column teikei_shiwake_meisai.kashi_gaika is '�ݕ��O�݋��z';
comment on column teikei_shiwake_meisai.kashi_gaika_shouhizeigaku is '�ݕ��O�ݏ���Ŋz';
comment on column teikei_shiwake_meisai.kashi_gaika_taika is '�ݕ��O�ݑΉ�';
comment on column teikei_shiwake_meisai.kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no is '�ݕ����_���͔z���p�^�[��No';
comment on column teikei_shiwake_meisai.gyou_kugiri is '�s��؂�';
comment on column teikei_shiwake_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column teikei_shiwake_meisai.touroku_time is '�o�^����';
comment on column teikei_shiwake_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column teikei_shiwake_meisai.koushin_time is '�X�V����';
INSERT INTO teikei_shiwake_meisai
SELECT
	 user_id
	,teikei_shiwake_pattern_no
	,teikei_shiwake_edano
	,kari_tekiyou
	,kari_kingaku
	,kari_shouhizeigaku
	,kari_zeiritsu
	,kari_keigen_zeiritsu_kbn
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,kari_torihikisaki_cd
	,kari_torihikisaki_name_ryakushiki
	,kari_kazei_kbn
	,kari_bunri_kbn
	,kari_kobetsu_kbn
	,kari_uf1_cd
	,kari_uf1_name_ryakushiki
	,kari_uf2_cd
	,kari_uf2_name_ryakushiki
	,kari_uf3_cd
	,kari_uf3_name_ryakushiki
	,kari_uf4_cd
	,kari_uf4_name_ryakushiki
	,kari_uf5_cd
	,kari_uf5_name_ryakushiki
	,kari_uf6_cd
	,kari_uf6_name_ryakushiki
	,kari_uf7_cd
	,kari_uf7_name_ryakushiki
	,kari_uf8_cd
	,kari_uf8_name_ryakushiki
	,kari_uf9_cd
	,kari_uf9_name_ryakushiki
	,kari_uf10_cd
	,kari_uf10_name_ryakushiki
	,kari_project_cd
	,kari_project_name
	,kari_segment_cd
	,kari_segment_name
	,kari_taika
	,kari_shouhizeitaishou_kamoku_cd
	,kari_shouhizeitaishou_kamoku_name
	,kari_shouhizeitaishou_kazei_kbn
	,kari_shouhizeitaishou_zeiritsu
	,kari_shouhizeitaishou_keigen_zeiritsu_kbn
	,kari_shouhizeitaishou_bunri_kbn
	,kari_shouhizeitaishou_kobetsu_kbn
	,kari_heishu_cd
	,kari_rate
	,kari_gaika
	,kari_gaika_shouhizeigaku
	,kari_gaika_taika
	,''   --kari_zaimu_kyoten_nyuryoku_haifu_pattern_no
	,kashi_tekiyou
	,kashi_kingaku
	,kashi_shouhizeigaku
	,kashi_zeiritsu
	,kashi_keigen_zeiritsu_kbn
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_torihikisaki_cd
	,kashi_torihikisaki_name_ryakushiki
	,kashi_kazei_kbn
	,kashi_bunri_kbn
	,kashi_kobetsu_kbn
	,kashi_uf1_cd
	,kashi_uf1_name_ryakushiki
	,kashi_uf2_cd
	,kashi_uf2_name_ryakushiki
	,kashi_uf3_cd
	,kashi_uf3_name_ryakushiki
	,kashi_uf4_cd
	,kashi_uf4_name_ryakushiki
	,kashi_uf5_cd
	,kashi_uf5_name_ryakushiki
	,kashi_uf6_cd
	,kashi_uf6_name_ryakushiki
	,kashi_uf7_cd
	,kashi_uf7_name_ryakushiki
	,kashi_uf8_cd
	,kashi_uf8_name_ryakushiki
	,kashi_uf9_cd
	,kashi_uf9_name_ryakushiki
	,kashi_uf10_cd
	,kashi_uf10_name_ryakushiki
	,kashi_project_cd
	,kashi_project_name
	,kashi_segment_cd
	,kashi_segment_name
	,kashi_taika
	,kashi_shouhizeitaishou_kamoku_cd
	,kashi_shouhizeitaishou_kamoku_name
	,kashi_shouhizeitaishou_kazei_kbn
	,kashi_shouhizeitaishou_zeiritsu
	,kashi_shouhizeitaishou_keigen_zeiritsu_kbn
	,kashi_shouhizeitaishou_bunri_kbn
	,kashi_shouhizeitaishou_kobetsu_kbn
	,kashi_heishu_cd
	,kashi_rate
	,kashi_gaika
	,kashi_gaika_shouhizeigaku
	,kashi_gaika_taika
	,''   --kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no
	,gyou_kugiri
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM teikei_shiwake_meisai_old;
DROP TABLE teikei_shiwake_meisai_old;

-- ����t�֐���
create table bumon_tsukekae_control (
  serial_no bigint not null
  , bumon_tsukekae_taishou_flg character varying(1) not null
  , bumon_tsukekae_moto_kbn character varying(1) not null
  , jyogai_flg character varying(1) not null
  , constraint bumon_tsukekae_control_PKEY primary key (serial_no)
);
comment on table bumon_tsukekae_control is '���_����t�֐���';
comment on column bumon_tsukekae_control.serial_no is '�V���A���ԍ�';
comment on column bumon_tsukekae_control.bumon_tsukekae_taishou_flg is '����t�֑Ώۃt���O';
comment on column bumon_tsukekae_control.bumon_tsukekae_moto_kbn is '����t�֌��敪';
comment on column bumon_tsukekae_control.jyogai_flg is '���O�t���O';

-- ���_���͎҈ꗗ
create table zaimu_kyoten_nyuryoku_nyuryokusha (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , user_id character varying(30) not null
  , bumon_tsukekae_kengen_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp(6) without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp(6) without time zone not null
  , constraint zaimu_kyoten_nyuryoku_nyuryokusha_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,user_id)
);

comment on table zaimu_kyoten_nyuryoku_nyuryokusha is '���_���͎�';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.yuukou_kigen_from is '�L�������J�n��';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.yuukou_kigen_to is '�L�������I����';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.user_id is '���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.bumon_tsukekae_kengen_flg is '����t�֌����t���O';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.koushin_time is '�X�V����';

INSERT INTO zaimu_kyoten_nyuryoku_nyuryokusha(
	denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,yuukou_kigen_from
	,yuukou_kigen_to
	,user_id
	,bumon_tsukekae_kengen_flg
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
)SELECT
	 denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,'1999-01-01' --yuukou_kigen_from
	,'9999-12-31' --yuukou_kigen_to
	,user_id
	,bumon_tsukekae_kengen_flg
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM zaimu_kyoten_nyuryoku_user_info
WHERE nyuryoku_flg = '1';

-- ���Ԃ��p�^�[���̂���ɍ��킹��
UPDATE zaimu_kyoten_nyuryoku_nyuryokusha AS nyuryokusha 
SET
  yuukou_kigen_from = ichiran.yuukou_kigen_from
  , yuukou_kigen_to = ichiran.yuukou_kigen_to 
FROM
  zaimu_kyoten_nyuryoku_ichiran AS ichiran 
WHERE
  nyuryokusha.denpyou_kbn = ichiran.denpyou_kbn 
  AND nyuryokusha.zaimu_kyoten_nyuryoku_pattern_no = ichiran.zaimu_kyoten_nyuryoku_pattern_no;

-- �������_���̓��[�U�[�J�����폜
ALTER TABLE zaimu_kyoten_nyuryoku_user_info DROP COLUMN bumon_tsukekae_kengen_flg;
ALTER TABLE zaimu_kyoten_nyuryoku_user_info DROP COLUMN nyuryoku_flg;

-- �������_���͉ȖڃZ�L�����e�B
ALTER TABLE zaimu_kyoten_nyuryoku_kamoku_security DROP CONSTRAINT IF EXISTS zaimu_kyoten_nyuryoku_kamoku_security_PKEY;
ALTER TABLE zaimu_kyoten_nyuryoku_kamoku_security RENAME TO zaimu_kyoten_nyuryoku_kamoku_security_old;
create table zaimu_kyoten_nyuryoku_kamoku_security (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , kamoku_gaibu_cd character varying(6) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_kamoku_security_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,kamoku_gaibu_cd)
);

comment on table zaimu_kyoten_nyuryoku_kamoku_security is '���_���͉ȖڃZ�L�����e�B';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.yuukou_kigen_from is '�L�������J�n��';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.yuukou_kigen_to is '�L�������I����';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.koushin_time is '�X�V����';

INSERT INTO zaimu_kyoten_nyuryoku_kamoku_security(
	denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,yuukou_kigen_from
	,yuukou_kigen_to
	,kamoku_gaibu_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
)SELECT
	 denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,'1999-01-01' --yuukou_kigen_from
	,'9999-12-31' --yuukou_kigen_to
	,kamoku_gaibu_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM zaimu_kyoten_nyuryoku_kamoku_security_old;
DROP TABLE zaimu_kyoten_nyuryoku_kamoku_security_old;

-- ���Ԃ��p�^�[���̂���ɍ��킹��
UPDATE zaimu_kyoten_nyuryoku_kamoku_security AS kamoku 
SET
  yuukou_kigen_from = ichiran.yuukou_kigen_from
  , yuukou_kigen_to = ichiran.yuukou_kigen_to 
FROM
  zaimu_kyoten_nyuryoku_ichiran AS ichiran 
WHERE
  kamoku.denpyou_kbn = ichiran.denpyou_kbn 
  AND kamoku.zaimu_kyoten_nyuryoku_pattern_no = ichiran.zaimu_kyoten_nyuryoku_pattern_no;

-- �������_���͕���Z�L�����e�B
ALTER TABLE zaimu_kyoten_nyuryoku_bumon_security DROP CONSTRAINT IF EXISTS zaimu_kyoten_nyuryoku_bumon_security_PKEY;
ALTER TABLE zaimu_kyoten_nyuryoku_bumon_security RENAME TO zaimu_kyoten_nyuryoku_bumon_security_old;
create table zaimu_kyoten_nyuryoku_bumon_security (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , futan_bumon_cd character varying(8) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_bumon_security_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,futan_bumon_cd)
);

comment on table zaimu_kyoten_nyuryoku_bumon_security is '���_���͕���Z�L�����e�B';
comment on column zaimu_kyoten_nyuryoku_bumon_security.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_bumon_security.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_bumon_security.yuukou_kigen_from is '�L�������J�n��';
comment on column zaimu_kyoten_nyuryoku_bumon_security.yuukou_kigen_to is '�L�������I����';
comment on column zaimu_kyoten_nyuryoku_bumon_security.futan_bumon_cd is '���S����R�[�h';
comment on column zaimu_kyoten_nyuryoku_bumon_security.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_bumon_security.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_bumon_security.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_bumon_security.koushin_time is '�X�V����';

INSERT INTO zaimu_kyoten_nyuryoku_bumon_security(
	denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,yuukou_kigen_from
	,yuukou_kigen_to
	,futan_bumon_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
)SELECT
	 denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,'1999-01-01' --yuukou_kigen_from
	,'9999-12-31' --yuukou_kigen_to
	,futan_bumon_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM zaimu_kyoten_nyuryoku_bumon_security_old;
DROP TABLE zaimu_kyoten_nyuryoku_bumon_security_old;

-- ���Ԃ��p�^�[���̂���ɍ��킹��
UPDATE zaimu_kyoten_nyuryoku_bumon_security AS bumon 
SET
  yuukou_kigen_from = ichiran.yuukou_kigen_from
  , yuukou_kigen_to = ichiran.yuukou_kigen_to 
FROM
  zaimu_kyoten_nyuryoku_ichiran AS ichiran 
WHERE
  bumon.denpyou_kbn = ichiran.denpyou_kbn 
  AND bumon.zaimu_kyoten_nyuryoku_pattern_no = ichiran.zaimu_kyoten_nyuryoku_pattern_no;

--���_���F���[�g
ALTER TABLE zaimu_kyoten_nyuryoku_shounin_route DROP CONSTRAINT IF EXISTS zaimu_kyoten_nyuryoku_shounin_route_PKEY;
ALTER TABLE zaimu_kyoten_nyuryoku_shounin_route RENAME TO zaimu_kyoten_nyuryoku_shounin_route_old;
create table zaimu_kyoten_nyuryoku_shounin_route (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , edano integer not null
  , user_id character varying(30) not null
  , dairi_shounin_user_id character varying(30) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_shounin_route_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,edano)
);

comment on table zaimu_kyoten_nyuryoku_shounin_route is '���_���͏��F���[�g';
comment on column zaimu_kyoten_nyuryoku_shounin_route.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_shounin_route.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_shounin_route.yuukou_kigen_from is '�L�������J�n��';
comment on column zaimu_kyoten_nyuryoku_shounin_route.yuukou_kigen_to is '�L�������I����';
comment on column zaimu_kyoten_nyuryoku_shounin_route.edano is '�}�ԍ�';
comment on column zaimu_kyoten_nyuryoku_shounin_route.user_id is '���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.dairi_shounin_user_id is '�㗝���F�҃��[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_shounin_route.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.koushin_time is '�X�V����';

INSERT INTO zaimu_kyoten_nyuryoku_shounin_route(
	denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,yuukou_kigen_from
	,yuukou_kigen_to
	,edano
	,user_id
	,dairi_shounin_user_id
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
)SELECT
	 denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,'1999-01-01' --yuukou_kigen_from
	,'9999-12-31' --yuukou_kigen_to
	,edano
	,user_id
	,dairi_shounin_user_id
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM zaimu_kyoten_nyuryoku_shounin_route_old;
DROP TABLE zaimu_kyoten_nyuryoku_shounin_route_old;

-- ���Ԃ��p�^�[���̂���ɍ��킹��
UPDATE zaimu_kyoten_nyuryoku_shounin_route AS shounin 
SET
  yuukou_kigen_from = ichiran.yuukou_kigen_from
  , yuukou_kigen_to = ichiran.yuukou_kigen_to 
FROM
  zaimu_kyoten_nyuryoku_ichiran AS ichiran 
WHERE
  shounin.denpyou_kbn = ichiran.denpyou_kbn 
  AND shounin.zaimu_kyoten_nyuryoku_pattern_no = ichiran.zaimu_kyoten_nyuryoku_pattern_no;

-- ����R�[�h�̃f�[�^��������Ȃ������B
ALTER TABLE teikei_shiwake_meisai DROP CONSTRAINT IF EXISTS teikei_shiwake_meisai_PKEY;
ALTER TABLE teikei_shiwake_meisai RENAME TO teikei_shiwake_meisai_old;
create table teikei_shiwake_meisai (
  user_id character varying(30) not null
  , teikei_shiwake_pattern_no integer not null
  , teikei_shiwake_edano integer not null
  , kari_tekiyou character varying(60) not null
  , kari_kingaku numeric(15) not null
  , kari_shouhizeigaku numeric(15)
  , kari_zeiritsu numeric(3) not null
  , kari_keigen_zeiritsu_kbn character varying(1) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , kari_torihikisaki_cd character varying(12) not null
  , kari_torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kari_bunri_kbn character varying(1) not null
  , kari_kobetsu_kbn character varying(1) not null
  , kari_uf1_cd character varying(20) not null
  , kari_uf1_name_ryakushiki character varying(20) not null
  , kari_uf2_cd character varying(20) not null
  , kari_uf2_name_ryakushiki character varying(20) not null
  , kari_uf3_cd character varying(20) not null
  , kari_uf3_name_ryakushiki character varying(20) not null
  , kari_uf4_cd character varying(20) not null
  , kari_uf4_name_ryakushiki character varying(20) not null
  , kari_uf5_cd character varying(20) not null
  , kari_uf5_name_ryakushiki character varying(20) not null
  , kari_uf6_cd character varying(20) not null
  , kari_uf6_name_ryakushiki character varying(20) not null
  , kari_uf7_cd character varying(20) not null
  , kari_uf7_name_ryakushiki character varying(20) not null
  , kari_uf8_cd character varying(20) not null
  , kari_uf8_name_ryakushiki character varying(20) not null
  , kari_uf9_cd character varying(20) not null
  , kari_uf9_name_ryakushiki character varying(20) not null
  , kari_uf10_cd character varying(20) not null
  , kari_uf10_name_ryakushiki character varying(20) not null
  , kari_project_cd character varying(12) not null
  , kari_project_name character varying(20) not null
  , kari_segment_cd character varying(8) not null
  , kari_segment_name character varying(20) not null
  , kari_taika numeric(15)
  , kari_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kari_shouhizeitaishou_kamoku_name character varying(22) not null
  , kari_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kari_shouhizeitaishou_zeiritsu numeric(3)
  , kari_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kari_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kari_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kari_heishu_cd character varying(4) not null
  , kari_rate numeric(11,5)
  , kari_gaika numeric(22,5)
  , kari_gaika_shouhizeigaku numeric(22,5)
  , kari_gaika_taika numeric(22,5)
  , kari_zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , kashi_tekiyou character varying(60) not null
  , kashi_kingaku numeric(15) not null
  , kashi_shouhizeigaku numeric(15)
  , kashi_zeiritsu numeric(3) not null
  , kashi_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_torihikisaki_cd character varying(12) not null
  , kashi_torihikisaki_name_ryakushiki character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , kashi_bunri_kbn character varying(1) not null
  , kashi_kobetsu_kbn character varying(1) not null
  , kashi_uf1_cd character varying(20) not null
  , kashi_uf1_name_ryakushiki character varying(20) not null
  , kashi_uf2_cd character varying(20) not null
  , kashi_uf2_name_ryakushiki character varying(20) not null
  , kashi_uf3_cd character varying(20) not null
  , kashi_uf3_name_ryakushiki character varying(20) not null
  , kashi_uf4_cd character varying(20) not null
  , kashi_uf4_name_ryakushiki character varying(20) not null
  , kashi_uf5_cd character varying(20) not null
  , kashi_uf5_name_ryakushiki character varying(20) not null
  , kashi_uf6_cd character varying(20) not null
  , kashi_uf6_name_ryakushiki character varying(20) not null
  , kashi_uf7_cd character varying(20) not null
  , kashi_uf7_name_ryakushiki character varying(20) not null
  , kashi_uf8_cd character varying(20) not null
  , kashi_uf8_name_ryakushiki character varying(20) not null
  , kashi_uf9_cd character varying(20) not null
  , kashi_uf9_name_ryakushiki character varying(20) not null
  , kashi_uf10_cd character varying(20) not null
  , kashi_uf10_name_ryakushiki character varying(20) not null
  , kashi_project_cd character varying(12) not null
  , kashi_project_name character varying(20) not null
  , kashi_segment_cd character varying(8) not null
  , kashi_segment_name character varying(20) not null
  , kashi_taika numeric(15)
  , kashi_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kashi_shouhizeitaishou_kamoku_name character varying(22) not null
  , kashi_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kashi_shouhizeitaishou_zeiritsu numeric(3)
  , kashi_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kashi_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kashi_heishu_cd character varying(4) not null
  , kashi_rate numeric(11,5)
  , kashi_gaika numeric(22,5)
  , kashi_gaika_shouhizeigaku numeric(22,5)
  , kashi_gaika_taika numeric(22,5)
  , kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , gyou_kugiri character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint teikei_shiwake_meisai_PKEY primary key (user_id,teikei_shiwake_pattern_no,teikei_shiwake_edano)
);
comment on table teikei_shiwake_meisai is '��^�d�󖾍�';
comment on column teikei_shiwake_meisai.user_id is '���[�U�[ID';
comment on column teikei_shiwake_meisai.teikei_shiwake_pattern_no is '��^�d��p�^�[���ԍ�';
comment on column teikei_shiwake_meisai.teikei_shiwake_edano is '��^�d��}�ԍ�';
comment on column teikei_shiwake_meisai.kari_tekiyou is '�ؕ��E�v';
comment on column teikei_shiwake_meisai.kari_kingaku is '�ؕ����z';
comment on column teikei_shiwake_meisai.kari_shouhizeigaku is '�ؕ�����Ŋz';
comment on column teikei_shiwake_meisai.kari_zeiritsu is '�ؕ��ŗ�';
comment on column teikei_shiwake_meisai.kari_keigen_zeiritsu_kbn is '�ؕ��y���ŗ��敪';
comment on column teikei_shiwake_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column teikei_shiwake_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column teikei_shiwake_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column teikei_shiwake_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column teikei_shiwake_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column teikei_shiwake_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column teikei_shiwake_meisai.kari_torihikisaki_cd is '�ؕ������R�[�h';
comment on column teikei_shiwake_meisai.kari_torihikisaki_name_ryakushiki is '�ؕ�����於�i�����j';
comment on column teikei_shiwake_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column teikei_shiwake_meisai.kari_bunri_kbn is '�ؕ������敪';
comment on column teikei_shiwake_meisai.kari_kobetsu_kbn is '�ؕ��ʋ敪';
comment on column teikei_shiwake_meisai.kari_uf1_cd is '�ؕ�UF1�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf1_name_ryakushiki is '�ؕ�UF1���i�����j';
comment on column teikei_shiwake_meisai.kari_uf2_cd is '�ؕ�UF2�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf2_name_ryakushiki is '�ؕ�UF2���i�����j';
comment on column teikei_shiwake_meisai.kari_uf3_cd is '�ؕ�UF3�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf3_name_ryakushiki is '�ؕ�UF3���i�����j';
comment on column teikei_shiwake_meisai.kari_uf4_cd is '�ؕ�UF4�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf4_name_ryakushiki is '�ؕ�UF4���i�����j';
comment on column teikei_shiwake_meisai.kari_uf5_cd is '�ؕ�UF5�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf5_name_ryakushiki is '�ؕ�UF5���i�����j';
comment on column teikei_shiwake_meisai.kari_uf6_cd is '�ؕ�UF6�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf6_name_ryakushiki is '�ؕ�UF6���i�����j';
comment on column teikei_shiwake_meisai.kari_uf7_cd is '�ؕ�UF7�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf7_name_ryakushiki is '�ؕ�UF7���i�����j';
comment on column teikei_shiwake_meisai.kari_uf8_cd is '�ؕ�UF8�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf8_name_ryakushiki is '�ؕ�UF8���i�����j';
comment on column teikei_shiwake_meisai.kari_uf9_cd is '�ؕ�UF9�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf9_name_ryakushiki is '�ؕ�UF9���i�����j';
comment on column teikei_shiwake_meisai.kari_uf10_cd is '�ؕ�UF10�R�[�h';
comment on column teikei_shiwake_meisai.kari_uf10_name_ryakushiki is '�ؕ�UF10���i�����j';
comment on column teikei_shiwake_meisai.kari_project_cd is '�ؕ��v���W�F�N�g�R�[�h';
comment on column teikei_shiwake_meisai.kari_project_name is '�ؕ��v���W�F�N�g��';
comment on column teikei_shiwake_meisai.kari_segment_cd is '�ؕ��Z�O�����g�R�[�h';
comment on column teikei_shiwake_meisai.kari_segment_name is '�ؕ��Z�O�����g��';
comment on column teikei_shiwake_meisai.kari_taika is '�ؕ��Ή�';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kamoku_cd is '�ؕ�����őΏۉȖڃR�[�h';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kamoku_name is '�ؕ�����őΏۉȖږ���';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kazei_kbn is '�ؕ�����őΏۉېŋ敪';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_zeiritsu is '�ؕ�����őΏېŗ�';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_keigen_zeiritsu_kbn is '�ؕ�����őΏیy���ŗ��敪';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_bunri_kbn is '�ؕ�����őΏە����敪';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kobetsu_kbn is '�ؕ�����őΏیʋ敪';
comment on column teikei_shiwake_meisai.kari_heishu_cd is '�ؕ�����R�[�h';
comment on column teikei_shiwake_meisai.kari_rate is '�ؕ����[�g';
comment on column teikei_shiwake_meisai.kari_gaika is '�ؕ��O�݋��z';
comment on column teikei_shiwake_meisai.kari_gaika_shouhizeigaku is '�ؕ��O�ݏ���Ŋz';
comment on column teikei_shiwake_meisai.kari_gaika_taika is '�ؕ��O�ݑΉ�';
comment on column teikei_shiwake_meisai.kari_zaimu_kyoten_nyuryoku_haifu_pattern_no is '�ؕ����_���͔z���p�^�[��No';
comment on column teikei_shiwake_meisai.kashi_tekiyou is '�ݕ��E�v';
comment on column teikei_shiwake_meisai.kashi_kingaku is '�ݕ����z';
comment on column teikei_shiwake_meisai.kashi_shouhizeigaku is '�ݕ�����Ŋz';
comment on column teikei_shiwake_meisai.kashi_zeiritsu is '�ݕ��ŗ�';
comment on column teikei_shiwake_meisai.kashi_keigen_zeiritsu_kbn is '�ݕ��y���ŗ��敪';
comment on column teikei_shiwake_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column teikei_shiwake_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column teikei_shiwake_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column teikei_shiwake_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column teikei_shiwake_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column teikei_shiwake_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column teikei_shiwake_meisai.kashi_torihikisaki_cd is '�ݕ������R�[�h';
comment on column teikei_shiwake_meisai.kashi_torihikisaki_name_ryakushiki is '�ݕ�����於�i�����j';
comment on column teikei_shiwake_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column teikei_shiwake_meisai.kashi_bunri_kbn is '�ݕ������敪';
comment on column teikei_shiwake_meisai.kashi_kobetsu_kbn is '�ݕ��ʋ敪';
comment on column teikei_shiwake_meisai.kashi_uf1_cd is '�ݕ�UF1�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf1_name_ryakushiki is '�ݕ�UF1���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf2_cd is '�ݕ�UF2�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf2_name_ryakushiki is '�ݕ�UF2���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf3_cd is '�ݕ�UF3�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf3_name_ryakushiki is '�ݕ�UF3���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf4_cd is '�ݕ�UF4�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf4_name_ryakushiki is '�ݕ�UF4���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf5_cd is '�ݕ�UF5�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf5_name_ryakushiki is '�ݕ�UF5���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf6_cd is '�ݕ�UF6�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf6_name_ryakushiki is '�ݕ�UF6���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf7_cd is '�ݕ�UF7�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf7_name_ryakushiki is '�ݕ�UF7���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf8_cd is '�ݕ�UF8�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf8_name_ryakushiki is '�ݕ�UF8���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf9_cd is '�ݕ�UF9�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf9_name_ryakushiki is '�ݕ�UF9���i�����j';
comment on column teikei_shiwake_meisai.kashi_uf10_cd is '�ݕ�UF10�R�[�h';
comment on column teikei_shiwake_meisai.kashi_uf10_name_ryakushiki is '�ݕ�UF10���i�����j';
comment on column teikei_shiwake_meisai.kashi_project_cd is '�ݕ��v���W�F�N�g�R�[�h';
comment on column teikei_shiwake_meisai.kashi_project_name is '�ݕ��v���W�F�N�g��';
comment on column teikei_shiwake_meisai.kashi_segment_cd is '�ݕ��Z�O�����g�R�[�h';
comment on column teikei_shiwake_meisai.kashi_segment_name is '�ݕ��Z�O�����g��';
comment on column teikei_shiwake_meisai.kashi_taika is '�ݕ��Ή�';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kamoku_cd is '�ݕ�����őΏۉȖڃR�[�h';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kamoku_name is '�ݕ�����őΏۉȖږ���';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kazei_kbn is '�ݕ�����őΏۉېŋ敪';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_zeiritsu is '�ݕ�����őΏېŗ�';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_keigen_zeiritsu_kbn is '�ݕ�����őΏیy���ŗ��敪';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_bunri_kbn is '�ݕ�����őΏە����敪';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kobetsu_kbn is '�ݕ�����őΏیʋ敪';
comment on column teikei_shiwake_meisai.kashi_heishu_cd is '�ݕ�����R�[�h';
comment on column teikei_shiwake_meisai.kashi_rate is '�ݕ����[�g';
comment on column teikei_shiwake_meisai.kashi_gaika is '�ݕ��O�݋��z';
comment on column teikei_shiwake_meisai.kashi_gaika_shouhizeigaku is '�ݕ��O�ݏ���Ŋz';
comment on column teikei_shiwake_meisai.kashi_gaika_taika is '�ݕ��O�ݑΉ�';
comment on column teikei_shiwake_meisai.kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no is '�ݕ����_���͔z���p�^�[��No';
comment on column teikei_shiwake_meisai.gyou_kugiri is '�s��؂�';
comment on column teikei_shiwake_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column teikei_shiwake_meisai.touroku_time is '�o�^����';
comment on column teikei_shiwake_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column teikei_shiwake_meisai.koushin_time is '�X�V����';
INSERT INTO teikei_shiwake_meisai(
		 user_id
		,teikei_shiwake_pattern_no
		,teikei_shiwake_edano
		,kari_tekiyou
		,kari_kingaku
		,kari_shouhizeigaku
		,kari_zeiritsu
		,kari_keigen_zeiritsu_kbn
		,kari_kamoku_cd
		,kari_kamoku_name
		,kari_kamoku_edaban_cd
		,kari_kamoku_edaban_name
		,kari_futan_bumon_cd
		,kari_futan_bumon_name
		,kari_torihikisaki_cd
		,kari_torihikisaki_name_ryakushiki
		,kari_kazei_kbn
		,kari_bunri_kbn
		,kari_kobetsu_kbn
		,kari_uf1_cd
		,kari_uf1_name_ryakushiki
		,kari_uf2_cd
		,kari_uf2_name_ryakushiki
		,kari_uf3_cd
		,kari_uf3_name_ryakushiki
		,kari_uf4_cd
		,kari_uf4_name_ryakushiki
		,kari_uf5_cd
		,kari_uf5_name_ryakushiki
		,kari_uf6_cd
		,kari_uf6_name_ryakushiki
		,kari_uf7_cd
		,kari_uf7_name_ryakushiki
		,kari_uf8_cd
		,kari_uf8_name_ryakushiki
		,kari_uf9_cd
		,kari_uf9_name_ryakushiki
		,kari_uf10_cd
		,kari_uf10_name_ryakushiki
		,kari_project_cd
		,kari_project_name
		,kari_segment_cd
		,kari_segment_name
		,kari_taika
		,kari_shouhizeitaishou_kamoku_cd
		,kari_shouhizeitaishou_kamoku_name
		,kari_shouhizeitaishou_kazei_kbn
		,kari_shouhizeitaishou_zeiritsu
		,kari_shouhizeitaishou_keigen_zeiritsu_kbn
		,kari_shouhizeitaishou_bunri_kbn
		,kari_shouhizeitaishou_kobetsu_kbn
		,kari_heishu_cd
		,kari_rate
		,kari_gaika
		,kari_gaika_shouhizeigaku
		,kari_gaika_taika
		,kari_zaimu_kyoten_nyuryoku_haifu_pattern_no
		,kashi_tekiyou
		,kashi_kingaku
		,kashi_shouhizeigaku
		,kashi_zeiritsu
		,kashi_keigen_zeiritsu_kbn
		,kashi_kamoku_cd
		,kashi_kamoku_name
		,kashi_kamoku_edaban_cd
		,kashi_kamoku_edaban_name
		,kashi_futan_bumon_cd
		,kashi_futan_bumon_name
		,kashi_torihikisaki_cd
		,kashi_torihikisaki_name_ryakushiki
		,kashi_kazei_kbn
		,kashi_bunri_kbn
		,kashi_kobetsu_kbn
		,kashi_uf1_cd
		,kashi_uf1_name_ryakushiki
		,kashi_uf2_cd
		,kashi_uf2_name_ryakushiki
		,kashi_uf3_cd
		,kashi_uf3_name_ryakushiki
		,kashi_uf4_cd
		,kashi_uf4_name_ryakushiki
		,kashi_uf5_cd
		,kashi_uf5_name_ryakushiki
		,kashi_uf6_cd
		,kashi_uf6_name_ryakushiki
		,kashi_uf7_cd
		,kashi_uf7_name_ryakushiki
		,kashi_uf8_cd
		,kashi_uf8_name_ryakushiki
		,kashi_uf9_cd
		,kashi_uf9_name_ryakushiki
		,kashi_uf10_cd
		,kashi_uf10_name_ryakushiki
		,kashi_project_cd
		,kashi_project_name
		,kashi_segment_cd
		,kashi_segment_name
		,kashi_taika
		,kashi_shouhizeitaishou_kamoku_cd
		,kashi_shouhizeitaishou_kamoku_name
		,kashi_shouhizeitaishou_kazei_kbn
		,kashi_shouhizeitaishou_zeiritsu
		,kashi_shouhizeitaishou_keigen_zeiritsu_kbn
		,kashi_shouhizeitaishou_bunri_kbn
		,kashi_shouhizeitaishou_kobetsu_kbn
		,kashi_heishu_cd
		,kashi_rate
		,kashi_gaika
		,kashi_gaika_shouhizeigaku
		,kashi_gaika_taika
		,kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no
		,gyou_kugiri
		,touroku_user_id
		,touroku_time
		,koushin_user_id
		,koushin_time
)SELECT
		 user_id
		,teikei_shiwake_pattern_no
		,teikei_shiwake_edano
		,kari_tekiyou
		,kari_kingaku
		,kari_shouhizeigaku
		,kari_zeiritsu
		,kari_keigen_zeiritsu_kbn
		,kari_kamoku_cd
		,kari_kamoku_name
		,kari_kamoku_edaban_cd
		,kari_kamoku_edaban_name
		,kari_futan_bumon_cd
		,kari_futan_bumon_name
		,kari_torihikisaki_cd
		,kari_torihikisaki_name_ryakushiki
		,kari_kazei_kbn
		,kari_bunri_kbn
		,kari_kobetsu_kbn
		,kari_uf1_cd
		,kari_uf1_name_ryakushiki
		,kari_uf2_cd
		,kari_uf2_name_ryakushiki
		,kari_uf3_cd
		,kari_uf3_name_ryakushiki
		,kari_uf4_cd
		,kari_uf4_name_ryakushiki
		,kari_uf5_cd
		,kari_uf5_name_ryakushiki
		,kari_uf6_cd
		,kari_uf6_name_ryakushiki
		,kari_uf7_cd
		,kari_uf7_name_ryakushiki
		,kari_uf8_cd
		,kari_uf8_name_ryakushiki
		,kari_uf9_cd
		,kari_uf9_name_ryakushiki
		,kari_uf10_cd
		,kari_uf10_name_ryakushiki
		,kari_project_cd
		,kari_project_name
		,kari_segment_cd
		,kari_segment_name
		,kari_taika
		,kari_shouhizeitaishou_kamoku_cd
		,kari_shouhizeitaishou_kamoku_name
		,kari_shouhizeitaishou_kazei_kbn
		,kari_shouhizeitaishou_zeiritsu
		,kari_shouhizeitaishou_keigen_zeiritsu_kbn
		,kari_shouhizeitaishou_bunri_kbn
		,kari_shouhizeitaishou_kobetsu_kbn
		,kari_heishu_cd
		,kari_rate
		,kari_gaika
		,kari_gaika_shouhizeigaku
		,kari_gaika_taika
		,kari_zaimu_kyoten_nyuryoku_haifu_pattern_no
		,kashi_tekiyou
		,kashi_kingaku
		,kashi_shouhizeigaku
		,kashi_zeiritsu
		,kashi_keigen_zeiritsu_kbn
		,kashi_kamoku_cd
		,kashi_kamoku_name
		,kashi_kamoku_edaban_cd
		,kashi_kamoku_edaban_name
		,kashi_futan_bumon_cd
		,kashi_futan_bumon_name
		,kashi_torihikisaki_cd
		,kashi_torihikisaki_name_ryakushiki
		,kashi_kazei_kbn
		,kashi_bunri_kbn
		,kashi_kobetsu_kbn
		,kashi_uf1_cd
		,kashi_uf1_name_ryakushiki
		,kashi_uf2_cd
		,kashi_uf2_name_ryakushiki
		,kashi_uf3_cd
		,kashi_uf3_name_ryakushiki
		,kashi_uf4_cd
		,kashi_uf4_name_ryakushiki
		,kashi_uf5_cd
		,kashi_uf5_name_ryakushiki
		,kashi_uf6_cd
		,kashi_uf6_name_ryakushiki
		,kashi_uf7_cd
		,kashi_uf7_name_ryakushiki
		,kashi_uf8_cd
		,kashi_uf8_name_ryakushiki
		,kashi_uf9_cd
		,kashi_uf9_name_ryakushiki
		,kashi_uf10_cd
		,kashi_uf10_name_ryakushiki
		,kashi_project_cd
		,kashi_project_name
		,kashi_segment_cd
		,kashi_segment_name
		,kashi_taika
		,kashi_shouhizeitaishou_kamoku_cd
		,kashi_shouhizeitaishou_kamoku_name
		,kashi_shouhizeitaishou_kazei_kbn
		,kashi_shouhizeitaishou_zeiritsu
		,kashi_shouhizeitaishou_keigen_zeiritsu_kbn
		,kashi_shouhizeitaishou_bunri_kbn
		,kashi_shouhizeitaishou_kobetsu_kbn
		,kashi_heishu_cd
		,kashi_rate
		,kashi_gaika
		,kashi_gaika_shouhizeigaku
		,kashi_gaika_taika
		,kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no
		,gyou_kugiri
		,touroku_user_id
		,touroku_time
		,koushin_user_id
		,koushin_time
FROM teikei_shiwake_meisai_old;
DROP TABLE teikei_shiwake_meisai_old;

--���F���[�g�ɕ����ǉ�
ALTER TABLE shounin_route_kyoten RENAME TO shounin_route_kyoten_tmp;
create table shounin_route_kyoten (
  denpyou_id character varying(19) not null
  , edano integer not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , bumon_full_name character varying not null
  , dairi_shounin_user_id character varying(30) not null
  , dairi_shounin_user_full_name character varying(50) not null
  , genzai_flg character varying(1) not null
  , joukyou_edano integer
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , PRIMARY KEY (denpyou_id,edano)
);
comment on table shounin_route_kyoten is '���F���[�g(���_)';
comment on column shounin_route_kyoten.denpyou_id is '�`�[ID';
comment on column shounin_route_kyoten.edano is '�}�ԍ�';
comment on column shounin_route_kyoten.user_id is '���[�U�[ID';
comment on column shounin_route_kyoten.user_full_name is '���[�U�[�t����';
comment on column shounin_route_kyoten.bumon_full_name is '����t����';
comment on column shounin_route_kyoten.dairi_shounin_user_id is '�㗝���F�҃��[�U�[ID';
comment on column shounin_route_kyoten.dairi_shounin_user_full_name is '�㗝���F�҃��[�U�[�t����';
comment on column shounin_route_kyoten.genzai_flg is '���݃t���O';
comment on column shounin_route_kyoten.joukyou_edano is '���F�󋵎}��';
comment on column shounin_route_kyoten.touroku_user_id is '�o�^���[�U�[ID';
comment on column shounin_route_kyoten.touroku_time is '�o�^����';
comment on column shounin_route_kyoten.koushin_user_id is '�X�V���[�U�[ID';
comment on column shounin_route_kyoten.koushin_time is '�X�V����';
INSERT INTO shounin_route_kyoten
SELECT
  denpyou_id
  , edano 
  , user_id 
  , user_full_name 
  , '' --bumon_full_name 
  , dairi_shounin_user_id 
  , dairi_shounin_user_full_name 
  , genzai_flg 
  , joukyou_edano 
  , touroku_user_id 
  , touroku_time 
  , koushin_user_id 
  , koushin_time 
FROM shounin_route_kyoten_tmp;
DROP table shounin_route_kyoten_tmp CASCADE;

--�e�[�u���a���ύX(�A���[No.835���)
comment on table denpyou_ichiran_kyoten is '�`�[�ꗗ(���_)';
comment on table denpyou_shubetsu_ichiran_kyoten is '�`�[��ʈꗗ(���_)';
comment on table shounin_joukyou_kyoten is '���F��(���_)';
comment on table shounin_route_kyoten is '���F���[�g(���_)';
comment on table zaimu_kyoten_nyuryoku_ichiran is '���_���͈ꗗ';
comment on table zaimu_kyoten_nyuryoku_shounin_route is '���_���͏��F���[�g';
comment on table zaimu_kyoten_nyuryoku_tsukekaemoto is '���_���͕t�֌�';
comment on table zaimu_kyoten_nyuryoku_user_info is '���_���̓��[�U�[';
comment on table zaimu_kyoten_shiwake_de3 is '���_�d�󒊏o(de3)';
comment on table zaimu_kyoten_shiwake_sias is '���_�d�󒊏o(SIAS)';
comment on table zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban is '���_���͎d��V���A���ԍ��̔�';
comment on table denpyou_serial_no_saiban_kyoten is '�`�[�ԍ��̔�(���_)';
comment on table shiwake_pattern_master_zaimu_kyoten is '�d��p�^�[���}�X�^�[(���_)';
comment on table bumon_furikae_setting IS '�U�֓`�[�ݒ�(���_)';

--�J�����a���ύX(�A���[No.835���)
comment on column denpyou_ichiran_kyoten.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column shiwake_pattern_master_zaimu_kyoten.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column suitouchou_setting.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column teikei_shiwake.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column user_info.zaimu_kyoten_nyuryoku_only_flg is '���_���͂̂ݎg�p�t���O';
comment on column zaimu_kyoten_nyuryoku_ichiran.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_ichiran.zaimu_kyoten_nyuryoku_pattern_name is '���_���̓p�^�[������';
comment on column zaimu_kyoten_nyuryoku_shounin_route.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.moto_zaimu_kyoten_nyuryoku_pattern_no is '�t�֌����_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_user_info.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
comment on column teikei_shiwake_meisai.kari_zaimu_kyoten_nyuryoku_haifu_pattern_no IS '�ؕ����_���͔z���p�^�[��No';
comment on column teikei_shiwake_meisai.kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no IS '�ݕ����_���͔z���p�^�[��No';

--���a���o�[�����׃e�[�u����`�ύX
ALTER TABLE suitouchou_meisai DROP CONSTRAINT IF EXISTS suitouchou_meisai_PKEY;
ALTER TABLE suitouchou_meisai RENAME TO suitouchou_meisai_old;

create table suitouchou_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , serial_no bigint not null
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , nyukin_goukei numeric(15)
  , shukkin_goukei numeric(15)
  , aite_kazei_kbn character varying(3) not null
  , aite_zeiritsu numeric(3) not null
  , aite_keigen_zeiritsu_kbn character varying(1) not null
  , fusen_color character varying(1) not null
  , meisai_touroku_user_id character varying(30) not null
  , meisai_touroku_shain_no character varying(15) not null
  , meisai_touroku_user_sei character varying(10) not null
  , meisai_touroku_user_mei character varying(10) not null
  , shiwake_serial_no bigint not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint suitouchou_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

comment on table suitouchou_meisai is '���a���o�[������';
comment on column suitouchou_meisai.denpyou_id is '�`�[ID';
comment on column suitouchou_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column suitouchou_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column suitouchou_meisai.serial_no is '�V���A���ԍ�';
comment on column suitouchou_meisai.torihiki_name is '�����';
comment on column suitouchou_meisai.tekiyou is '�E�v';
comment on column suitouchou_meisai.nyukin_goukei is '����';
comment on column suitouchou_meisai.shukkin_goukei is '�o��';
comment on column suitouchou_meisai.aite_kazei_kbn is '�ېŋ敪';
comment on column suitouchou_meisai.aite_zeiritsu is '�ŗ�';
comment on column suitouchou_meisai.aite_keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column suitouchou_meisai.fusen_color is '�tⳃJ���[';
comment on column suitouchou_meisai.meisai_touroku_user_id is '���דo�^���[�U�[ID';
comment on column suitouchou_meisai.meisai_touroku_shain_no is '���דo�^���[�U�[�Ј�No';
comment on column suitouchou_meisai.meisai_touroku_user_sei is '���דo�^���[�U�[��';
comment on column suitouchou_meisai.meisai_touroku_user_mei is '���דo�^���[�U�[��';
comment on column suitouchou_meisai.shiwake_serial_no is '�d��f�[�^�V���A��No';
comment on column suitouchou_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column suitouchou_meisai.touroku_time is '�o�^����';
comment on column suitouchou_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column suitouchou_meisai.koushin_time is '�X�V����';


INSERT INTO suitouchou_meisai
SELECT
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , serial_no
  , torihiki_name
  , tekiyou
  , nyukin_goukei
  , shukkin_goukei
  , aite_kazei_kbn
  , aite_zeiritsu
  , aite_keigen_zeiritsu_kbn
  , fusen_color
  , meisai_touroku_user_id
  , meisai_touroku_shain_no
  , meisai_touroku_user_sei
  , meisai_touroku_user_mei
  , shiwake_serial_no
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM suitouchou_meisai_old;
DROP TABLE suitouchou_meisai_old;

--�Y�t�t�@�C�����׉�
create table meisai_tenpu_file_himoduke (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , edano integer not null
  , tenpu_file_serial_no bigint not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint meisai_tenpu_file_himoduke_PKEY primary key (denpyou_id,denpyou_edano,edano)
);

create table user_tenpu_file (
  tenpu_file_serial_no bigserial not null
  , file_name character varying not null
  , file_size bigint not null
  , content_type character varying not null
  , binary_data bytea not null
  , ebunsho_no character varying(19)
  , ebunsho_binary_data bytea
  , ebunsho_touroku_time timestamp without time zone
  , koushin_time timestamp without time zone not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , constraint user_tenpu_file_PKEY primary key (tenpu_file_serial_no)
);

alter table user_tenpu_file add constraint user_tenpu_file_ebunsho_no_key
  unique (ebunsho_no) ;

comment on table meisai_tenpu_file_himoduke is '���דY�t�t�@�C���R�Â�';
comment on column meisai_tenpu_file_himoduke.denpyou_id is '�`�[ID';
comment on column meisai_tenpu_file_himoduke.denpyou_edano is '�`�[�}�ԍ�';
comment on column meisai_tenpu_file_himoduke.edano is '�}�ԍ�';
comment on column meisai_tenpu_file_himoduke.tenpu_file_serial_no is '�Y�t�t�@�C���V���A��No';
comment on column meisai_tenpu_file_himoduke.touroku_user_id is '�o�^���[�U�[ID';
comment on column meisai_tenpu_file_himoduke.touroku_time is '�o�^����';
comment on column meisai_tenpu_file_himoduke.koushin_user_id is '�X�V���[�U�[ID';
comment on column meisai_tenpu_file_himoduke.koushin_time is '�X�V����';

comment on table user_tenpu_file is '���[�U�[�ʓY�t�t�@�C��';
comment on column user_tenpu_file.tenpu_file_serial_no is '�Y�t�t�@�C���V���A��No';
comment on column user_tenpu_file.file_name is '�t�@�C����';
comment on column user_tenpu_file.file_size is '�t�@�C���T�C�Y';
comment on column user_tenpu_file.content_type is '�R���e���c�^�C�v';
comment on column user_tenpu_file.binary_data is '�o�C�i���[�f�[�^';
comment on column user_tenpu_file.ebunsho_no is 'e�����ԍ�';
comment on column user_tenpu_file.ebunsho_binary_data is 'e�����o�C�i���[�f�[�^';
comment on column user_tenpu_file.ebunsho_touroku_time is 'e�����o�^����';
comment on column user_tenpu_file.koushin_time is '�X�V����';
comment on column user_tenpu_file.touroku_user_id is '�o�^���[�U�[ID';
comment on column user_tenpu_file.touroku_time is '�o�^����';
comment on column user_tenpu_file.koushin_user_id is '�X�V���[�U�[ID';

--�d��e�[�u����`�ύX
ALTER TABLE zaimu_kyoten_shiwake_de3 DROP CONSTRAINT IF EXISTS zaimu_kyoten_shiwake_de3_PKEY;
ALTER TABLE zaimu_kyoten_shiwake_sias DROP CONSTRAINT IF EXISTS zaimu_kyoten_shiwake_sias_PKEY;
ALTER TABLE zaimu_kyoten_shiwake_de3 RENAME TO zaimu_kyoten_shiwake_de3_old;
ALTER TABLE zaimu_kyoten_shiwake_sias RENAME TO zaimu_kyoten_shiwake_sias_old;

create table zaimu_kyoten_shiwake_de3 (
  serial_no bigint not null
  , denpyou_id character varying(19) not null
  , shiwake_status character varying(1) not null
  , touroku_time timestamp without time zone
  , koushin_time timestamp without time zone
  , dymd date
  , seiri character varying(1) not null
  , dcno character varying(8) not null
  , rbmn character varying(8) not null
  , rtor character varying(12) not null
  , rkmk character varying(6) not null
  , reda character varying(12) not null
  , rkoj character varying(10) not null
  , rkos character varying(6) not null
  , rprj character varying(12) not null
  , rseg character varying(8) not null
  , rdm1 character varying(20) not null
  , rdm2 character varying(20) not null
  , rdm3 character varying(20) not null
  , tky character varying(60) not null
  , tno character varying(4) not null
  , sbmn character varying(8) not null
  , stor character varying(12) not null
  , skmk character varying(6) not null
  , seda character varying(12) not null
  , skoj character varying(10) not null
  , skos character varying(6) not null
  , sprj character varying(12) not null
  , sseg character varying(8) not null
  , sdm1 character varying(20) not null
  , sdm2 character varying(20) not null
  , sdm3 character varying(20) not null
  , exvl numeric(15)
  , valu numeric(15)
  , zkmk character varying(6) not null
  , zrit numeric(3)
  , zkeigen character varying(1) not null
  , zzkb character varying(3) not null
  , zgyo character varying(1) not null
  , zsre character varying(1) not null
  , rrit numeric(3)
  , rkeigen character varying(1) not null
  , srit numeric(3)
  , skeigen character varying(1) not null
  , rzkb character varying(3) not null
  , rgyo character varying(1) not null
  , rsre character varying(1) not null
  , szkb character varying(3) not null
  , sgyo character varying(1) not null
  , ssre character varying(1) not null
  , symd date
  , skbn character varying(2) not null
  , skiz date
  , uymd date
  , ukbn character varying(2) not null
  , ukiz date
  , sten character varying(1) not null
  , dkec character varying(12) not null
  , kymd date
  , kbmn character varying(8) not null
  , kusr character varying(12) not null
  , fusr character varying(4) not null
  , fsen character varying(2) not null
  , sgno character varying(4) not null
  , bunri character varying(1) not null
  , rate character varying(12) not null
  , gexvl character varying(14) not null
  , gvalu character varying(14) not null
  , gsep character varying(1) not null
  , constraint zaimu_kyoten_shiwake_de3_PKEY primary key (serial_no)
);

create table zaimu_kyoten_shiwake_sias (
  serial_no bigint not null
  , denpyou_id character varying(19) not null
  , shiwake_status character varying(1) not null
  , touroku_time timestamp without time zone
  , koushin_time timestamp without time zone
  , dymd date
  , seiri character varying(1) not null
  , dcno character varying(8) not null
  , kymd date
  , kbmn character varying(8) not null
  , kusr character varying(12) not null
  , sgno character varying(4) not null
  , hf1 character varying(20) not null
  , hf2 character varying(20) not null
  , hf3 character varying(20) not null
  , hf4 character varying(20) not null
  , hf5 character varying(20) not null
  , hf6 character varying(20) not null
  , hf7 character varying(20) not null
  , hf8 character varying(20) not null
  , hf9 character varying(20) not null
  , hf10 character varying(20) not null
  , rbmn character varying(8) not null
  , rtor character varying(12) not null
  , rkmk character varying(6) not null
  , reda character varying(12) not null
  , rkoj character varying(10) not null
  , rkos character varying(6) not null
  , rprj character varying(12) not null
  , rseg character varying(8) not null
  , rdm1 character varying(20) not null
  , rdm2 character varying(20) not null
  , rdm3 character varying(20) not null
  , rdm4 character varying(20) not null
  , rdm5 character varying(20) not null
  , rdm6 character varying(20) not null
  , rdm7 character varying(20) not null
  , rdm8 character varying(20) not null
  , rdm9 character varying(20) not null
  , rdm10 character varying(20) not null
  , rdm11 character varying(20) not null
  , rdm12 character varying(20) not null
  , rdm13 character varying(20) not null
  , rdm14 character varying(20) not null
  , rdm15 character varying(20) not null
  , rdm16 character varying(20) not null
  , rdm17 character varying(20) not null
  , rdm18 character varying(20) not null
  , rdm19 character varying(20) not null
  , rdm20 character varying(20) not null
  , rrit numeric(3)
  , rkeigen character varying(1) not null
  , rzkb character varying(3) not null
  , rgyo character varying(1) not null
  , rsre character varying(1) not null
  , rtky character varying(60) not null
  , rtno character varying(4) not null
  , sbmn character varying(8) not null
  , stor character varying(12) not null
  , skmk character varying(6) not null
  , seda character varying(12) not null
  , skoj character varying(10) not null
  , skos character varying(6) not null
  , sprj character varying(12) not null
  , sseg character varying(8) not null
  , sdm1 character varying(20) not null
  , sdm2 character varying(20) not null
  , sdm3 character varying(20) not null
  , sdm4 character varying(20) not null
  , sdm5 character varying(20) not null
  , sdm6 character varying(20) not null
  , sdm7 character varying(20) not null
  , sdm8 character varying(20) not null
  , sdm9 character varying(20) not null
  , sdm10 character varying(20) not null
  , sdm11 character varying(20) not null
  , sdm12 character varying(20) not null
  , sdm13 character varying(20) not null
  , sdm14 character varying(20) not null
  , sdm15 character varying(20) not null
  , sdm16 character varying(20) not null
  , sdm17 character varying(20) not null
  , sdm18 character varying(20) not null
  , sdm19 character varying(20) not null
  , sdm20 character varying(20) not null
  , srit numeric(3)
  , skeigen character varying(1) not null
  , szkb character varying(3) not null
  , sgyo character varying(1) not null
  , ssre character varying(1) not null
  , stky character varying(60) not null
  , stno character varying(4) not null
  , zkmk character varying(6) not null
  , zrit numeric(3)
  , zkeigen character varying(1) not null
  , zzkb character varying(3) not null
  , zgyo character varying(1) not null
  , zsre character varying(1) not null
  , exvl numeric(15)
  , valu numeric(15)
  , symd date
  , skbn character varying(2) not null
  , skiz date
  , uymd date
  , ukbn character varying(2) not null
  , ukiz date
  , dkec character varying(12) not null
  , fusr character varying(4) not null
  , fsen character varying(2) not null
  , tkflg character varying(1) not null
  , bunri character varying(1) not null
  , heic character varying(4) not null
  , rate character varying(12) not null
  , gexvl character varying(14) not null
  , gvalu character varying(14) not null
  , gsep character varying(1) not null
  , constraint zaimu_kyoten_shiwake_sias_PKEY primary key (serial_no)
);

comment on table zaimu_kyoten_shiwake_de3 is '���_�d�󒊏o(de3)';
comment on column zaimu_kyoten_shiwake_de3.serial_no is '�V���A���ԍ�';
comment on column zaimu_kyoten_shiwake_de3.denpyou_id is '�`�[ID';
comment on column zaimu_kyoten_shiwake_de3.shiwake_status is '�d�󒊏o���';
comment on column zaimu_kyoten_shiwake_de3.touroku_time is '�o�^����';
comment on column zaimu_kyoten_shiwake_de3.koushin_time is '�X�V����';
comment on column zaimu_kyoten_shiwake_de3.dymd is '�i�I�[�v���Q�P�j�`�[���t';
comment on column zaimu_kyoten_shiwake_de3.seiri is '�i�I�[�v���Q�P�j�������t���O';
comment on column zaimu_kyoten_shiwake_de3.dcno is '�i�I�[�v���Q�P�j�`�[�ԍ�';
comment on column zaimu_kyoten_shiwake_de3.rbmn is '�i�I�[�v���Q�P�j�ؕ��@����R�[�h';
comment on column zaimu_kyoten_shiwake_de3.rtor is '�i�I�[�v���Q�P�j�ؕ��@�����R�[�h';
comment on column zaimu_kyoten_shiwake_de3.rkmk is '�i�I�[�v���Q�P�j�ؕ��@�ȖڃR�[�h';
comment on column zaimu_kyoten_shiwake_de3.reda is '�i�I�[�v���Q�P�j�ؕ��@�}�ԃR�[�h';
comment on column zaimu_kyoten_shiwake_de3.rkoj is '�i�I�[�v���Q�P�j�ؕ��@�H���R�[�h';
comment on column zaimu_kyoten_shiwake_de3.rkos is '�i�I�[�v���Q�P�j�ؕ��@�H��R�[�h';
comment on column zaimu_kyoten_shiwake_de3.rprj is '�i�I�[�v���Q�P�j�ؕ��@�v���W�F�N�g�R�[�h';
comment on column zaimu_kyoten_shiwake_de3.rseg is '�i�I�[�v���Q�P�j�ؕ��@�Z�O�����g�R�[�h';
comment on column zaimu_kyoten_shiwake_de3.rdm1 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P';
comment on column zaimu_kyoten_shiwake_de3.rdm2 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�Q';
comment on column zaimu_kyoten_shiwake_de3.rdm3 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�R';
comment on column zaimu_kyoten_shiwake_de3.tky is '�i�I�[�v���Q�P�j�E�v';
comment on column zaimu_kyoten_shiwake_de3.tno is '�i�I�[�v���Q�P�j�E�v�R�[�h';
comment on column zaimu_kyoten_shiwake_de3.sbmn is '�i�I�[�v���Q�P�j�ݕ��@����R�[�h';
comment on column zaimu_kyoten_shiwake_de3.stor is '�i�I�[�v���Q�P�j�ݕ��@�����R�[�h';
comment on column zaimu_kyoten_shiwake_de3.skmk is '�i�I�[�v���Q�P�j�ݕ��@�ȖڃR�[�h';
comment on column zaimu_kyoten_shiwake_de3.seda is '�i�I�[�v���Q�P�j�ݕ��@�}�ԃR�[�h';
comment on column zaimu_kyoten_shiwake_de3.skoj is '�i�I�[�v���Q�P�j�ݕ��@�H���R�[�h';
comment on column zaimu_kyoten_shiwake_de3.skos is '�i�I�[�v���Q�P�j�ݕ��@�H��R�[�h';
comment on column zaimu_kyoten_shiwake_de3.sprj is '�i�I�[�v���Q�P�j�ݕ��@�v���W�F�N�g�R�[�h';
comment on column zaimu_kyoten_shiwake_de3.sseg is '�i�I�[�v���Q�P�j�ݕ��@�Z�O�����g�R�[�h';
comment on column zaimu_kyoten_shiwake_de3.sdm1 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P';
comment on column zaimu_kyoten_shiwake_de3.sdm2 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�Q';
comment on column zaimu_kyoten_shiwake_de3.sdm3 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�R';
comment on column zaimu_kyoten_shiwake_de3.exvl is '�i�I�[�v���Q�P�j�Ή����z';
comment on column zaimu_kyoten_shiwake_de3.valu is '�i�I�[�v���Q�P�j���z';
comment on column zaimu_kyoten_shiwake_de3.zkmk is '�i�I�[�v���Q�P�j����őΏۉȖڃR�[�h';
comment on column zaimu_kyoten_shiwake_de3.zrit is '�i�I�[�v���Q�P�j����őΏۉȖڐŗ�';
comment on column zaimu_kyoten_shiwake_de3.zkeigen is '�i�I�[�v���Q�P�j����őΏۉȖځ@�y���ŗ��敪';
comment on column zaimu_kyoten_shiwake_de3.zzkb is '�i�I�[�v���Q�P�j����őΏۉȖځ@�ېŋ敪';
comment on column zaimu_kyoten_shiwake_de3.zgyo is '�i�I�[�v���Q�P�j����őΏۉȖځ@�Ǝ�敪';
comment on column zaimu_kyoten_shiwake_de3.zsre is '�i�I�[�v���Q�P�j����őΏۉȖځ@�d���敪';
comment on column zaimu_kyoten_shiwake_de3.rrit is '�i�I�[�v���Q�P�j�ؕ��@�ŗ�';
comment on column zaimu_kyoten_shiwake_de3.rkeigen is '�i�I�[�v���Q�P�j�ؕ��@�y���ŗ��敪';
comment on column zaimu_kyoten_shiwake_de3.srit is '�i�I�[�v���Q�P�j�ݕ��@�ŗ�';
comment on column zaimu_kyoten_shiwake_de3.skeigen is '�i�I�[�v���Q�P�j�ݕ��@�y���ŗ��敪';
comment on column zaimu_kyoten_shiwake_de3.rzkb is '�i�I�[�v���Q�P�j�ؕ��@�ېŋ敪';
comment on column zaimu_kyoten_shiwake_de3.rgyo is '�i�I�[�v���Q�P�j�ؕ��@�Ǝ�敪';
comment on column zaimu_kyoten_shiwake_de3.rsre is '�i�I�[�v���Q�P�j�ؕ��@�d���敪';
comment on column zaimu_kyoten_shiwake_de3.szkb is '�i�I�[�v���Q�P�j�ݕ��@�ېŋ敪';
comment on column zaimu_kyoten_shiwake_de3.sgyo is '�i�I�[�v���Q�P�j�ݕ��@�Ǝ�敪';
comment on column zaimu_kyoten_shiwake_de3.ssre is '�i�I�[�v���Q�P�j�ݕ��@�d���敪';
comment on column zaimu_kyoten_shiwake_de3.symd is '�i�I�[�v���Q�P�j�x����';
comment on column zaimu_kyoten_shiwake_de3.skbn is '�i�I�[�v���Q�P�j�x���敪';
comment on column zaimu_kyoten_shiwake_de3.skiz is '�i�I�[�v���Q�P�j�x������';
comment on column zaimu_kyoten_shiwake_de3.uymd is '�i�I�[�v���Q�P�j�����';
comment on column zaimu_kyoten_shiwake_de3.ukbn is '�i�I�[�v���Q�P�j�����敪';
comment on column zaimu_kyoten_shiwake_de3.ukiz is '�i�I�[�v���Q�P�j�������';
comment on column zaimu_kyoten_shiwake_de3.sten is '�i�I�[�v���Q�P�j�X���t���O';
comment on column zaimu_kyoten_shiwake_de3.dkec is '�i�I�[�v���Q�P�j�����R�[�h';
comment on column zaimu_kyoten_shiwake_de3.kymd is '�i�I�[�v���Q�P�j�N�[�N����';
comment on column zaimu_kyoten_shiwake_de3.kbmn is '�i�I�[�v���Q�P�j�N�[����R�[�h';
comment on column zaimu_kyoten_shiwake_de3.kusr is '�i�I�[�v���Q�P�j�N�[�҃R�[�h';
comment on column zaimu_kyoten_shiwake_de3.fusr is '�i�I�[�v���Q�P�j���͎҃R�[�h';
comment on column zaimu_kyoten_shiwake_de3.fsen is '�i�I�[�v���Q�P�j�tⳔԍ�';
comment on column zaimu_kyoten_shiwake_de3.sgno is '�i�I�[�v���Q�P�j���F�O���[�vNo.';
comment on column zaimu_kyoten_shiwake_de3.bunri is '�i�I�[�v���Q�P�j�����敪';
comment on column zaimu_kyoten_shiwake_de3.rate is '�i�I�[�v���Q�P�j���[�g';
comment on column zaimu_kyoten_shiwake_de3.gexvl is '�i�I�[�v���Q�P�j�O�ݑΉ����z';
comment on column zaimu_kyoten_shiwake_de3.gvalu is '�i�I�[�v���Q�P�j�O�݋��z';
comment on column zaimu_kyoten_shiwake_de3.gsep is '�i�I�[�v���Q�P�j�s��؂�';

comment on table zaimu_kyoten_shiwake_sias is '���_�d�󒊏o(SIAS)';
comment on column zaimu_kyoten_shiwake_sias.serial_no is '�V���A���ԍ�';
comment on column zaimu_kyoten_shiwake_sias.denpyou_id is '�`�[ID';
comment on column zaimu_kyoten_shiwake_sias.shiwake_status is '�d�󒊏o���';
comment on column zaimu_kyoten_shiwake_sias.touroku_time is '�o�^����';
comment on column zaimu_kyoten_shiwake_sias.koushin_time is '�X�V����';
comment on column zaimu_kyoten_shiwake_sias.dymd is '�i�I�[�v���Q�P�j�`�[���t';
comment on column zaimu_kyoten_shiwake_sias.seiri is '�i�I�[�v���Q�P�j�������t���O';
comment on column zaimu_kyoten_shiwake_sias.dcno is '�i�I�[�v���Q�P�j�`�[�ԍ�';
comment on column zaimu_kyoten_shiwake_sias.kymd is '�i�I�[�v���Q�P�j�N�[�N����';
comment on column zaimu_kyoten_shiwake_sias.kbmn is '�i�I�[�v���Q�P�j�N�[����R�[�h';
comment on column zaimu_kyoten_shiwake_sias.kusr is '�i�I�[�v���Q�P�j�N�[�҃R�[�h';
comment on column zaimu_kyoten_shiwake_sias.sgno is '�i�I�[�v���Q�P�j���F�O���[�vNo.';
comment on column zaimu_kyoten_shiwake_sias.hf1 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�P';
comment on column zaimu_kyoten_shiwake_sias.hf2 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�Q';
comment on column zaimu_kyoten_shiwake_sias.hf3 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�R';
comment on column zaimu_kyoten_shiwake_sias.hf4 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�S';
comment on column zaimu_kyoten_shiwake_sias.hf5 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�T';
comment on column zaimu_kyoten_shiwake_sias.hf6 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�U';
comment on column zaimu_kyoten_shiwake_sias.hf7 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�V';
comment on column zaimu_kyoten_shiwake_sias.hf8 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�W';
comment on column zaimu_kyoten_shiwake_sias.hf9 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�X';
comment on column zaimu_kyoten_shiwake_sias.hf10 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�P�O';
comment on column zaimu_kyoten_shiwake_sias.rbmn is '�i�I�[�v���Q�P�j�ؕ��@����R�[�h';
comment on column zaimu_kyoten_shiwake_sias.rtor is '�i�I�[�v���Q�P�j�ؕ��@�����R�[�h';
comment on column zaimu_kyoten_shiwake_sias.rkmk is '�i�I�[�v���Q�P�j�ؕ��@�ȖڃR�[�h';
comment on column zaimu_kyoten_shiwake_sias.reda is '�i�I�[�v���Q�P�j�ؕ��@�}�ԃR�[�h';
comment on column zaimu_kyoten_shiwake_sias.rkoj is '�i�I�[�v���Q�P�j�ؕ��@�H���R�[�h';
comment on column zaimu_kyoten_shiwake_sias.rkos is '�i�I�[�v���Q�P�j�ؕ��@�H��R�[�h';
comment on column zaimu_kyoten_shiwake_sias.rprj is '�i�I�[�v���Q�P�j�ؕ��@�v���W�F�N�g�R�[�h';
comment on column zaimu_kyoten_shiwake_sias.rseg is '�i�I�[�v���Q�P�j�ؕ��@�Z�O�����g�R�[�h';
comment on column zaimu_kyoten_shiwake_sias.rdm1 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P';
comment on column zaimu_kyoten_shiwake_sias.rdm2 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�Q';
comment on column zaimu_kyoten_shiwake_sias.rdm3 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�R';
comment on column zaimu_kyoten_shiwake_sias.rdm4 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�S';
comment on column zaimu_kyoten_shiwake_sias.rdm5 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�T';
comment on column zaimu_kyoten_shiwake_sias.rdm6 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�U';
comment on column zaimu_kyoten_shiwake_sias.rdm7 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�V';
comment on column zaimu_kyoten_shiwake_sias.rdm8 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�W';
comment on column zaimu_kyoten_shiwake_sias.rdm9 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�X';
comment on column zaimu_kyoten_shiwake_sias.rdm10 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�O';
comment on column zaimu_kyoten_shiwake_sias.rdm11 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�P';
comment on column zaimu_kyoten_shiwake_sias.rdm12 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�Q';
comment on column zaimu_kyoten_shiwake_sias.rdm13 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�R';
comment on column zaimu_kyoten_shiwake_sias.rdm14 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�S';
comment on column zaimu_kyoten_shiwake_sias.rdm15 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�T';
comment on column zaimu_kyoten_shiwake_sias.rdm16 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�U';
comment on column zaimu_kyoten_shiwake_sias.rdm17 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�V';
comment on column zaimu_kyoten_shiwake_sias.rdm18 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�W';
comment on column zaimu_kyoten_shiwake_sias.rdm19 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�X';
comment on column zaimu_kyoten_shiwake_sias.rdm20 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�Q�O';
comment on column zaimu_kyoten_shiwake_sias.rrit is '�i�I�[�v���Q�P�j�ؕ��@�ŗ�';
comment on column zaimu_kyoten_shiwake_sias.rkeigen is '�i�I�[�v���Q�P�j�ؕ��@�y���ŗ��敪';
comment on column zaimu_kyoten_shiwake_sias.rzkb is '�i�I�[�v���Q�P�j�ؕ��@�ېŋ敪';
comment on column zaimu_kyoten_shiwake_sias.rgyo is '�i�I�[�v���Q�P�j�ؕ��@�Ǝ�敪';
comment on column zaimu_kyoten_shiwake_sias.rsre is '�i�I�[�v���Q�P�j�ؕ��@�d���敪';
comment on column zaimu_kyoten_shiwake_sias.rtky is '�i�I�[�v���Q�P�j�ؕ��@�E�v';
comment on column zaimu_kyoten_shiwake_sias.rtno is '�i�I�[�v���Q�P�j�ؕ��@�E�v�R�[�h';
comment on column zaimu_kyoten_shiwake_sias.sbmn is '�i�I�[�v���Q�P�j�ݕ��@����R�[�h';
comment on column zaimu_kyoten_shiwake_sias.stor is '�i�I�[�v���Q�P�j�ݕ��@�����R�[�h';
comment on column zaimu_kyoten_shiwake_sias.skmk is '�i�I�[�v���Q�P�j�ݕ��@�ȖڃR�[�h';
comment on column zaimu_kyoten_shiwake_sias.seda is '�i�I�[�v���Q�P�j�ݕ��@�}�ԃR�[�h';
comment on column zaimu_kyoten_shiwake_sias.skoj is '�i�I�[�v���Q�P�j�ݕ��@�H���R�[�h';
comment on column zaimu_kyoten_shiwake_sias.skos is '�i�I�[�v���Q�P�j�ݕ��@�H��R�[�h';
comment on column zaimu_kyoten_shiwake_sias.sprj is '�i�I�[�v���Q�P�j�ݕ��@�v���W�F�N�g�R�[�h';
comment on column zaimu_kyoten_shiwake_sias.sseg is '�i�I�[�v���Q�P�j�ݕ��@�Z�O�����g�R�[�h';
comment on column zaimu_kyoten_shiwake_sias.sdm1 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P';
comment on column zaimu_kyoten_shiwake_sias.sdm2 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�Q';
comment on column zaimu_kyoten_shiwake_sias.sdm3 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�R';
comment on column zaimu_kyoten_shiwake_sias.sdm4 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�S';
comment on column zaimu_kyoten_shiwake_sias.sdm5 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�T';
comment on column zaimu_kyoten_shiwake_sias.sdm6 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�U';
comment on column zaimu_kyoten_shiwake_sias.sdm7 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�V';
comment on column zaimu_kyoten_shiwake_sias.sdm8 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�W';
comment on column zaimu_kyoten_shiwake_sias.sdm9 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�X';
comment on column zaimu_kyoten_shiwake_sias.sdm10 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�O';
comment on column zaimu_kyoten_shiwake_sias.sdm11 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�P';
comment on column zaimu_kyoten_shiwake_sias.sdm12 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�Q';
comment on column zaimu_kyoten_shiwake_sias.sdm13 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�R';
comment on column zaimu_kyoten_shiwake_sias.sdm14 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�S';
comment on column zaimu_kyoten_shiwake_sias.sdm15 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�T';
comment on column zaimu_kyoten_shiwake_sias.sdm16 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�U';
comment on column zaimu_kyoten_shiwake_sias.sdm17 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�V';
comment on column zaimu_kyoten_shiwake_sias.sdm18 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�W';
comment on column zaimu_kyoten_shiwake_sias.sdm19 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�X';
comment on column zaimu_kyoten_shiwake_sias.sdm20 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�Q�O';
comment on column zaimu_kyoten_shiwake_sias.srit is '�i�I�[�v���Q�P�j�ݕ��@�ŗ�';
comment on column zaimu_kyoten_shiwake_sias.skeigen is '�i�I�[�v���Q�P�j�ݕ��@�y���ŗ��敪';
comment on column zaimu_kyoten_shiwake_sias.szkb is '�i�I�[�v���Q�P�j�ݕ��@�ېŋ敪';
comment on column zaimu_kyoten_shiwake_sias.sgyo is '�i�I�[�v���Q�P�j�ݕ��@�Ǝ�敪';
comment on column zaimu_kyoten_shiwake_sias.ssre is '�i�I�[�v���Q�P�j�ݕ��@�d���敪';
comment on column zaimu_kyoten_shiwake_sias.stky is '�i�I�[�v���Q�P�j�ݕ��@�E�v';
comment on column zaimu_kyoten_shiwake_sias.stno is '�i�I�[�v���Q�P�j�ݕ��@�E�v�R�[�h';
comment on column zaimu_kyoten_shiwake_sias.zkmk is '�i�I�[�v���Q�P�j����őΏۉȖڃR�[�h';
comment on column zaimu_kyoten_shiwake_sias.zrit is '�i�I�[�v���Q�P�j����őΏۉȖڐŗ�';
comment on column zaimu_kyoten_shiwake_sias.zkeigen is '�i�I�[�v���Q�P�j����őΏۉȖځ@�y���ŗ��敪';
comment on column zaimu_kyoten_shiwake_sias.zzkb is '�i�I�[�v���Q�P�j����őΏۉȖځ@�ېŋ敪';
comment on column zaimu_kyoten_shiwake_sias.zgyo is '�i�I�[�v���Q�P�j����őΏۉȖځ@�Ǝ�敪';
comment on column zaimu_kyoten_shiwake_sias.zsre is '�i�I�[�v���Q�P�j����őΏۉȖځ@�d���敪';
comment on column zaimu_kyoten_shiwake_sias.exvl is '�i�I�[�v���Q�P�j�Ή����z';
comment on column zaimu_kyoten_shiwake_sias.valu is '�i�I�[�v���Q�P�j���z';
comment on column zaimu_kyoten_shiwake_sias.symd is '�i�I�[�v���Q�P�j�x����';
comment on column zaimu_kyoten_shiwake_sias.skbn is '�i�I�[�v���Q�P�j�x���敪';
comment on column zaimu_kyoten_shiwake_sias.skiz is '�i�I�[�v���Q�P�j�x������';
comment on column zaimu_kyoten_shiwake_sias.uymd is '�i�I�[�v���Q�P�j�����';
comment on column zaimu_kyoten_shiwake_sias.ukbn is '�i�I�[�v���Q�P�j�����敪';
comment on column zaimu_kyoten_shiwake_sias.ukiz is '�i�I�[�v���Q�P�j�������';
comment on column zaimu_kyoten_shiwake_sias.dkec is '�i�I�[�v���Q�P�j�����R�[�h';
comment on column zaimu_kyoten_shiwake_sias.fusr is '�i�I�[�v���Q�P�j���͎҃R�[�h';
comment on column zaimu_kyoten_shiwake_sias.fsen is '�i�I�[�v���Q�P�j�tⳔԍ�';
comment on column zaimu_kyoten_shiwake_sias.tkflg is '�i�I�[�v���Q�P�j�ݎؕʓE�v�t���O';
comment on column zaimu_kyoten_shiwake_sias.bunri is '�i�I�[�v���Q�P�j�����敪';
comment on column zaimu_kyoten_shiwake_sias.heic is '�i�I�[�v���Q�P�j����';
comment on column zaimu_kyoten_shiwake_sias.rate is '�i�I�[�v���Q�P�j���[�g';
comment on column zaimu_kyoten_shiwake_sias.gexvl is '�i�I�[�v���Q�P�j�O�ݑΉ����z';
comment on column zaimu_kyoten_shiwake_sias.gvalu is '�i�I�[�v���Q�P�j�O�݋��z';
comment on column zaimu_kyoten_shiwake_sias.gsep is '�i�I�[�v���Q�P�j�s��؂�';

INSERT INTO zaimu_kyoten_shiwake_de3
SELECT
  serial_no
  , denpyou_id
  , shiwake_status
  , touroku_time
  , koushin_time
  , dymd
  , seiri
  , dcno
  , rbmn
  , rtor
  , rkmk
  , reda
  , rkoj
  , rkos
  , rprj
  , rseg
  , rdm1
  , rdm2
  , rdm3
  , tky
  , tno
  , sbmn
  , stor
  , skmk
  , seda
  , skoj
  , skos
  , sprj
  , sseg
  , sdm1
  , sdm2
  , sdm3
  , exvl
  , valu
  , zkmk
  , zrit
  , zkeigen
  , zzkb
  , zgyo
  , zsre
  , rrit
  , rkeigen
  , srit
  , skeigen
  , rzkb
  , rgyo
  , rsre
  , szkb
  , sgyo
  , ssre
  , symd
  , skbn
  , skiz
  , uymd
  , ukbn
  , ukiz
  , sten
  , dkec
  , kymd
  , kbmn
  , kusr
  , fusr
  , fsen
  , sgno
  , bunri
  , rate
  , gexvl
  , gvalu
  , gsep 
FROM
  zaimu_kyoten_shiwake_de3;
DROP TABLE zaimu_kyoten_shiwake_de3_old;

INSERT INTO zaimu_kyoten_shiwake_sias
SELECT
  serial_no
  , denpyou_id
  , shiwake_status
  , touroku_time
  , koushin_time
  , dymd
  , seiri
  , dcno
  , kymd
  , kbmn
  , kusr
  , sgno
  , hf1
  , hf2
  , hf3
  , hf4
  , hf5
  , hf6
  , hf7
  , hf8
  , hf9
  , hf10
  , rbmn
  , rtor
  , rkmk
  , reda
  , rkoj
  , rkos
  , rprj
  , rseg
  , rdm1
  , rdm2
  , rdm3
  , rdm4
  , rdm5
  , rdm6
  , rdm7
  , rdm8
  , rdm9
  , rdm10
  , rdm11
  , rdm12
  , rdm13
  , rdm14
  , rdm15
  , rdm16
  , rdm17
  , rdm18
  , rdm19
  , rdm20
  , rrit
  , rkeigen
  , rzkb
  , rgyo
  , rsre
  , rtky
  , rtno
  , sbmn
  , stor
  , skmk
  , seda
  , skoj
  , skos
  , sprj
  , sseg
  , sdm1
  , sdm2
  , sdm3
  , sdm4
  , sdm5
  , sdm6
  , sdm7
  , sdm8
  , sdm9
  , sdm10
  , sdm11
  , sdm12
  , sdm13
  , sdm14
  , sdm15
  , sdm16
  , sdm17
  , sdm18
  , sdm19
  , sdm20
  , srit
  , skeigen
  , szkb
  , sgyo
  , ssre
  , stky
  , stno
  , zkmk
  , zrit
  , zkeigen
  , zzkb
  , zgyo
  , zsre
  , exvl
  , valu
  , symd
  , skbn
  , skiz
  , uymd
  , ukbn
  , ukiz
  , dkec
  , fusr
  , fsen
  , tkflg
  , bunri
  , heic
  , rate
  , gexvl
  , gvalu
  , gsep 
FROM
  zaimu_kyoten_shiwake_sias_old;
DROP TABLE zaimu_kyoten_shiwake_sias_old;

commit;
