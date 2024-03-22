SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- ��ʍ��ڐ���
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

-- �}�X�^�[�Ǘ��ɐV�����}�X�^�[��ǉ�
\copy master_kanri_hansuu FROM '.\files\csv\master_kanri_hansuu_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_kanri_ichiran FROM '.\files\csv\master_kanri_ichiran_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- �}�X�^�[�捞����
DELETE FROM master_torikomi_ichiran_de3;
DELETE FROM master_torikomi_shousai_de3;
DELETE FROM master_torikomi_ichiran_sias;
DELETE FROM master_torikomi_shousai_sias;
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- ��ʌ�������
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--���[�U�[���e�[�u���J�����ǉ�
ALTER TABLE user_info RENAME TO user_info_old;
CREATE TABLE user_info
(
	user_id character varying(30) NOT NULL,
	shain_no character varying(15) NOT NULL,
	user_sei character varying(10) NOT NULL,
	user_mei character varying(10) NOT NULL,
	mail_address character varying(50) NOT NULL,
	yuukou_kigen_from DATE NOT NULL,
	Yuukou_Kigen_to DATE NOT NULL,
	touroku_user_id character varying(30) NOT NULL,
	touroku_time TIMESTAMP NOT NULL,
	koushin_user_id character varying(30) NOT NULL,
	koushin_time TIMESTAMP NOT NULL,
	pass_koushin_date DATE,
	Pass_failure_count INT DEFAULT 0 NOT NULL,
	pass_failure_time TIMESTAMP,
	tmp_lock_flg character varying(1) NOT NULL,
	tmp_lock_time TIMESTAMP,
	lock_flg character varying(1) DEFAULT '0' NOT NULL,
	lock_time TIMESTAMP,
	dairikihyou_flg character varying(1) NOT NULL,
	houjin_card_riyou_flag character varying(1) NOT NULL,
	houjin_card_shikibetsuyou_num character varying(20) NOT NULL,
	security_pattern character varying(4) NOT NULL,
	security_wfonly_flg character varying(1) NOT NULL,
	shounin_route_henkou_level character varying(1) NOT NULL,
	maruhi_kengen_flg character varying(1) NOT NULL,
	maruhi_kaijyo_flg character varying(1) NOT NULL,
	zaimu_kyoten_nyuryoku_only_flg character varying(1) default '0' not null,
	PRIMARY KEY (USER_ID)
) WITHOUT OIDS;
COMMENT ON TABLE user_info IS '���[�U�[���';
COMMENT ON COLUMN user_info.user_id IS '���[�U�[ID';
COMMENT ON COLUMN user_info.shain_no IS '�Ј��ԍ�';
COMMENT ON COLUMN user_info.user_sei IS '���[�U�[��';
COMMENT ON COLUMN user_info.user_mei IS '���[�U�[��';
COMMENT ON COLUMN user_info.mail_address IS '���[���A�h���X';
COMMENT ON COLUMN user_info.yuukou_kigen_from IS '�L�������J�n��';
COMMENT ON COLUMN user_info.yuukou_kigen_to IS '�L�������I����';
COMMENT ON COLUMN user_info.touroku_user_id IS '�o�^���[�U�[ID';
COMMENT ON COLUMN user_info.touroku_time IS '�o�^����';
COMMENT ON COLUMN user_info.koushin_user_id IS '�X�V���[�U�[ID';
COMMENT ON COLUMN user_info.koushin_time IS '�X�V����';
COMMENT ON COLUMN user_info.pass_koushin_date IS '�p�X���[�h�ύX��';
COMMENT ON COLUMN user_info.pass_failure_count IS '�p�X���[�h����';
COMMENT ON COLUMN user_info.pass_failure_time IS '�p�X���[�h��莞��';
COMMENT ON COLUMN user_info.tmp_lock_flg IS '�A�J�E���g�ꎞ���b�N�t���O';
COMMENT ON COLUMN user_info.tmp_lock_time IS '�A�J�E���g�ꎞ���b�N����';
COMMENT ON COLUMN user_info.lock_flg IS '�A�J�E���g���b�N�t���O';
COMMENT ON COLUMN user_info.lock_time IS '�A�J�E���g���b�N����';
COMMENT ON COLUMN user_info.dairikihyou_flg IS '�㗝�N�[�\�t���O';
COMMENT ON COLUMN user_info.houjin_card_riyou_flag IS '�@�l�J�[�h���p';
COMMENT ON COLUMN user_info.houjin_card_shikibetsuyou_num IS '�@�l�J�[�h���ʗp�ԍ�';
COMMENT ON COLUMN user_info.security_pattern IS '�Z�L�����e�B�p�^�[��';
COMMENT ON COLUMN user_info.security_wfonly_flg IS '�Z�L�����e�B���[�N�t���[����t���O';
COMMENT ON COLUMN user_info.shounin_route_henkou_level IS '���F���[�g�ύX�������x��';
COMMENT ON COLUMN user_info.maruhi_kengen_flg IS '�}����ݒ茠��';
COMMENT ON COLUMN user_info.maruhi_kaijyo_flg IS '�}�����������';
COMMENT ON COLUMN user_info.zaimu_kyoten_nyuryoku_only_flg is '�������_���͂̂ݎg�p�t���O';
INSERT INTO user_info(
	user_id,
	shain_no,
	user_sei,
	user_mei,
	mail_address,
	yuukou_kigen_from,
	yuukou_kigen_to,
	touroku_user_id,
	touroku_time,
	koushin_user_id,
	koushin_time,
	pass_koushin_date,
	pass_failure_count,
	pass_failure_time,
	tmp_lock_flg,
	tmp_lock_time,
	lock_flg,
	lock_time,
	dairikihyou_flg,
	houjin_card_riyou_flag,
	houjin_card_shikibetsuyou_num,
	security_pattern,
	security_wfonly_flg ,
	shounin_route_henkou_level,
	maruhi_kengen_flg,
	maruhi_kaijyo_flg,
	zaimu_kyoten_nyuryoku_only_flg
)
SELECT
	user_id,
	shain_no,
	user_sei,
	user_mei,
	mail_address,
	yuukou_kigen_from,
	yuukou_kigen_to,
	touroku_user_id,
	touroku_time,
	koushin_user_id,
	koushin_time,
	pass_koushin_date,
	pass_failure_count,
	pass_failure_time,
	tmp_lock_flg,
	tmp_lock_time,
	lock_flg,
	lock_time,
	dairikihyou_flg,
	houjin_card_riyou_flag,
	houjin_card_shikibetsuyou_num,
	security_pattern,
	security_wfonly_flg ,
	shounin_route_henkou_level,
	maruhi_kengen_flg,
	maruhi_kengen_flg,
	0 --�������_���͂̂ݎg�p�t���O
FROM user_info_old;
DROP TABLE user_info_old;

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
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint bumon_furikae_PKEY primary key (denpyou_id)
);
comment on table bumon_furikae is '����U�֓`�[';
comment on column bumon_furikae.denpyou_id is '�`�[ID';
comment on column bumon_furikae.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
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
comment on column bumon_furikae.touroku_user_id is '�o�^���[�U�[ID';
comment on column bumon_furikae.touroku_time is '�o�^����';
comment on column bumon_furikae.koushin_user_id is '�X�V���[�U�[ID';
comment on column bumon_furikae.koushin_time is '�X�V����';

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
  , gyou_kugiri character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint bumon_furikae_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table bumon_furikae_meisai is '����U�֓`�[����';
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
comment on column bumon_furikae_meisai.gyou_kugiri is '�s��؂�';
comment on column bumon_furikae_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column bumon_furikae_meisai.touroku_time is '�o�^����';
comment on column bumon_furikae_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column bumon_furikae_meisai.koushin_time is '�X�V����';

create table bumon_furikae_setting (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , taishaku_tekiyou_flg character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint bumon_furikae_setting_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no)
);
comment on table bumon_furikae_setting is '����U�֐ݒ�';
comment on column bumon_furikae_setting.denpyou_kbn is '�`�[�敪';
comment on column bumon_furikae_setting.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column bumon_furikae_setting.taishaku_tekiyou_flg is '�ݎؓE�v�t���O';
comment on column bumon_furikae_setting.touroku_user_id is '�o�^���[�U�[ID';
comment on column bumon_furikae_setting.touroku_time is '�o�^����';
comment on column bumon_furikae_setting.koushin_user_id is '�X�V���[�U�[ID';
comment on column bumon_furikae_setting.koushin_time is '�X�V����';

create table edaban_zandaka (
  kamoku_gaibu_cd character varying(8) not null
  , kamoku_edaban_cd character varying(12) not null
  , kamoku_naibu_cd character varying(15) not null
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , edaban_name character varying(20) not null
  , kessanki_bangou smallint not null
  , chouhyou_shaturyoku_no smallint
  , taishaku_zokusei smallint not null
  , zandaka_kari00 numeric(19) not null
  , zandaka_kashi00 numeric(19) not null
  , zandaka_kari01 numeric(19) not null
  , zandaka_kashi01 numeric(19) not null
  , zandaka_kari02 numeric(19) not null
  , zandaka_kashi02 numeric(19) not null
  , zandaka_kari03 numeric(19) not null
  , zandaka_kashi03 numeric(19) not null
  , zandaka_kari03_shu numeric(19) not null
  , zandaka_kashi03_shu numeric(19) not null
  , zandaka_kari04 numeric(19) not null
  , zandaka_kashi04 numeric(19) not null
  , zandaka_kari05 numeric(19) not null
  , zandaka_kashi05 numeric(19) not null
  , zandaka_kari06 numeric(19) not null
  , zandaka_kashi06 numeric(19) not null
  , zandaka_kari06_shu numeric(19) not null
  , zandaka_kashi06_shu numeric(19) not null
  , zandaka_kari07 numeric(19) not null
  , zandaka_kashi07 numeric(19) not null
  , zandaka_kari08 numeric(19) not null
  , zandaka_kashi08 numeric(19) not null
  , zandaka_kari09 numeric(19) not null
  , zandaka_kashi09 numeric(19) not null
  , zandaka_kari09_shu numeric(19) not null
  , zandaka_kashi09_shu numeric(19) not null
  , zandaka_kari10 numeric(19) not null
  , zandaka_kashi10 numeric(19) not null
  , zandaka_kari11 numeric(19) not null
  , zandaka_kashi11 numeric(19) not null
  , zandaka_kari12 numeric(19) not null
  , zandaka_kashi12 numeric(19) not null
  , zandaka_kari12_shu numeric(19) not null
  , zandaka_kashi12_shu numeric(19) not null
  , constraint edaban_zandaka_PKEY primary key (kamoku_gaibu_cd,kamoku_edaban_cd,kessanki_bangou)
);
comment on table edaban_zandaka is '�}�Ԏc��';
comment on column edaban_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column edaban_zandaka.kamoku_edaban_cd is '�Ȗڎ}�ԃR�[�h';
comment on column edaban_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column edaban_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column edaban_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column edaban_zandaka.edaban_name is '�}�Ԗ�';
comment on column edaban_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column edaban_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column edaban_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column edaban_zandaka.zandaka_kari00 is '����c��(�ؕ�)';
comment on column edaban_zandaka.zandaka_kashi00 is '����c��(�ݕ�)';
comment on column edaban_zandaka.zandaka_kari01 is '1�����(��)';
comment on column edaban_zandaka.zandaka_kashi01 is '1�����(��)';
comment on column edaban_zandaka.zandaka_kari02 is '2�����(��)';
comment on column edaban_zandaka.zandaka_kashi02 is '2�����(��)';
comment on column edaban_zandaka.zandaka_kari03 is '3�����(��)';
comment on column edaban_zandaka.zandaka_kashi03 is '3�����(��)';
comment on column edaban_zandaka.zandaka_kari03_shu is '3����ڏC��(��)';
comment on column edaban_zandaka.zandaka_kashi03_shu is '3����ڏC��(��)';
comment on column edaban_zandaka.zandaka_kari04 is '4�����(��)';
comment on column edaban_zandaka.zandaka_kashi04 is '4�����(��)';
comment on column edaban_zandaka.zandaka_kari05 is '5�����(��)';
comment on column edaban_zandaka.zandaka_kashi05 is '5�����(��)';
comment on column edaban_zandaka.zandaka_kari06 is '6�����(��)';
comment on column edaban_zandaka.zandaka_kashi06 is '6�����(��)';
comment on column edaban_zandaka.zandaka_kari06_shu is '6����ڏC��(��)';
comment on column edaban_zandaka.zandaka_kashi06_shu is '6����ڏC��(��)';
comment on column edaban_zandaka.zandaka_kari07 is '7�����(��)';
comment on column edaban_zandaka.zandaka_kashi07 is '7�����(��)';
comment on column edaban_zandaka.zandaka_kari08 is '8�����(��)';
comment on column edaban_zandaka.zandaka_kashi08 is '8�����(��)';
comment on column edaban_zandaka.zandaka_kari09 is '9�����(��)';
comment on column edaban_zandaka.zandaka_kashi09 is '9�����(��)';
comment on column edaban_zandaka.zandaka_kari09_shu is '9����ڏC��(��)';
comment on column edaban_zandaka.zandaka_kashi09_shu is '9����ڏC��(��)';
comment on column edaban_zandaka.zandaka_kari10 is '10�����(��)';
comment on column edaban_zandaka.zandaka_kashi10 is '10�����(��)';
comment on column edaban_zandaka.zandaka_kari11 is '11�����(��)';
comment on column edaban_zandaka.zandaka_kashi11 is '11�����(��)';
comment on column edaban_zandaka.zandaka_kari12 is '12�����(��)';
comment on column edaban_zandaka.zandaka_kashi12 is '12�����(��)';
comment on column edaban_zandaka.zandaka_kari12_shu is '12����ڏC��(��)';
comment on column edaban_zandaka.zandaka_kashi12_shu is '12����ڏC��(��)';

create table kamoku_zandaka (
  kamoku_gaibu_cd character varying(8) not null
  , kamoku_naibu_cd character varying(15) not null
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , kessanki_bangou smallint not null
  , chouhyou_shaturyoku_no smallint
  , taishaku_zokusei smallint not null
  , zandaka_kari00 numeric(19) not null
  , zandaka_kashi00 numeric(19) not null
  , zandaka_kari01 numeric(19) not null
  , zandaka_kashi01 numeric(19) not null
  , zandaka_kari02 numeric(19) not null
  , zandaka_kashi02 numeric(19) not null
  , zandaka_kari03 numeric(19) not null
  , zandaka_kashi03 numeric(19) not null
  , zandaka_kari03_shu numeric(19) not null
  , zandaka_kashi03_shu numeric(19) not null
  , zandaka_kari04 numeric(19) not null
  , zandaka_kashi04 numeric(19) not null
  , zandaka_kari05 numeric(19) not null
  , zandaka_kashi05 numeric(19) not null
  , zandaka_kari06 numeric(19) not null
  , zandaka_kashi06 numeric(19) not null
  , zandaka_kari06_shu numeric(19) not null
  , zandaka_kashi06_shu numeric(19) not null
  , zandaka_kari07 numeric(19) not null
  , zandaka_kashi07 numeric(19) not null
  , zandaka_kari08 numeric(19) not null
  , zandaka_kashi08 numeric(19) not null
  , zandaka_kari09 numeric(19) not null
  , zandaka_kashi09 numeric(19) not null
  , zandaka_kari09_shu numeric(19) not null
  , zandaka_kashi09_shu numeric(19) not null
  , zandaka_kari10 numeric(19) not null
  , zandaka_kashi10 numeric(19) not null
  , zandaka_kari11 numeric(19) not null
  , zandaka_kashi11 numeric(19) not null
  , zandaka_kari12 numeric(19) not null
  , zandaka_kashi12 numeric(19) not null
  , zandaka_kari12_shu numeric(19) not null
  , zandaka_kashi12_shu numeric(19) not null
  , constraint kamoku_zandaka_PKEY primary key (kamoku_gaibu_cd,kessanki_bangou)
);
comment on table kamoku_zandaka is '�Ȗڎc��';
comment on column kamoku_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column kamoku_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column kamoku_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column kamoku_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column kamoku_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column kamoku_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column kamoku_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column kamoku_zandaka.zandaka_kari00 is '����c��(�ؕ�)';
comment on column kamoku_zandaka.zandaka_kashi00 is '����c��(�ݕ�)';
comment on column kamoku_zandaka.zandaka_kari01 is '1�����(��)';
comment on column kamoku_zandaka.zandaka_kashi01 is '1�����(��)';
comment on column kamoku_zandaka.zandaka_kari02 is '2�����(��)';
comment on column kamoku_zandaka.zandaka_kashi02 is '2�����(��)';
comment on column kamoku_zandaka.zandaka_kari03 is '3�����(��)';
comment on column kamoku_zandaka.zandaka_kashi03 is '3�����(��)';
comment on column kamoku_zandaka.zandaka_kari03_shu is '3����ڏC��(��)';
comment on column kamoku_zandaka.zandaka_kashi03_shu is '3����ڏC��(��)';
comment on column kamoku_zandaka.zandaka_kari04 is '4�����(��)';
comment on column kamoku_zandaka.zandaka_kashi04 is '4�����(��)';
comment on column kamoku_zandaka.zandaka_kari05 is '5�����(��)';
comment on column kamoku_zandaka.zandaka_kashi05 is '5�����(��)';
comment on column kamoku_zandaka.zandaka_kari06 is '6�����(��)';
comment on column kamoku_zandaka.zandaka_kashi06 is '6�����(��)';
comment on column kamoku_zandaka.zandaka_kari06_shu is '6����ڏC��(��)';
comment on column kamoku_zandaka.zandaka_kashi06_shu is '6����ڏC��(��)';
comment on column kamoku_zandaka.zandaka_kari07 is '7�����(��)';
comment on column kamoku_zandaka.zandaka_kashi07 is '7�����(��)';
comment on column kamoku_zandaka.zandaka_kari08 is '8�����(��)';
comment on column kamoku_zandaka.zandaka_kashi08 is '8�����(��)';
comment on column kamoku_zandaka.zandaka_kari09 is '9�����(��)';
comment on column kamoku_zandaka.zandaka_kashi09 is '9�����(��)';
comment on column kamoku_zandaka.zandaka_kari09_shu is '9����ڏC��(��)';
comment on column kamoku_zandaka.zandaka_kashi09_shu is '9����ڏC��(��)';
comment on column kamoku_zandaka.zandaka_kari10 is '10�����(��)';
comment on column kamoku_zandaka.zandaka_kashi10 is '10�����(��)';
comment on column kamoku_zandaka.zandaka_kari11 is '11�����(��)';
comment on column kamoku_zandaka.zandaka_kashi11 is '11�����(��)';
comment on column kamoku_zandaka.zandaka_kari12 is '12�����(��)';
comment on column kamoku_zandaka.zandaka_kashi12 is '12�����(��)';
comment on column kamoku_zandaka.zandaka_kari12_shu is '12����ڏC��(��)';
comment on column kamoku_zandaka.zandaka_kashi12_shu is '12����ڏC��(��)';

create table ki_shouhizei_setting (
  kesn smallint not null
  , shiire_zeigaku_anbun_flg smallint not null
  , shouhizei_kbn smallint not null
  , hasuu_shori_flg smallint not null
  , zeigaku_keisan_flg smallint not null
  , shouhizeitaishou_minyuryoku_flg smallint not null
  , constraint ki_shouhizei_setting_PKEY primary key (kesn)
);
comment on table ki_shouhizei_setting is '�i���ʁj����Őݒ�';
comment on column ki_shouhizei_setting.kesn is '�������Z��';
comment on column ki_shouhizei_setting.shiire_zeigaku_anbun_flg is '�d���Ŋz���t���O';
comment on column ki_shouhizei_setting.shouhizei_kbn is '����ŋ敪';
comment on column ki_shouhizei_setting.hasuu_shori_flg is '�[�������t���O';
comment on column ki_shouhizei_setting.zeigaku_keisan_flg is '�Ŋz�v�Z�t���O';
comment on column ki_shouhizei_setting.shouhizeitaishou_minyuryoku_flg is '����őΏۉȖږ����̓t���O';

create table shiwake_pattern_master_zaimu_kyoten (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , shiwake_edano integer not null
  , delete_flg character varying(1) default '0' not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , bunrui1 character varying(20) not null
  , bunrui2 character varying(20) not null
  , bunrui3 character varying(20) not null
  , torihiki_name character varying(20) not null
  , tekiyou_flg character varying(1) not null
  , tekiyou character varying(20) not null
  , nyushukkin_flg character varying(1) not null
  , hyouji_jun integer not null
  , shain_cd_renkei_flg character varying(1) not null
  , tenpu_file_hissu_flg character varying(1) not null
  , shiwake_taishougai_flg character varying(1) not null
  , fusen_flg character varying(1) not null
  , fusen_color character varying(1) not null
  , zaimu_kyoten_nyuryoku_pattern_no_bumon_furikae character varying(6) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , header_futan_bumon_cd character varying not null
  , header_torihikisaki_cd character varying not null
  , header_kamoku_cd character varying not null
  , header_kamoku_edaban_cd character varying not null
  , header_project_cd character varying not null
  , header_segment_cd character varying(8) not null
  , header_uf1_cd character varying(20) not null
  , header_uf2_cd character varying(20) not null
  , header_uf3_cd character varying(20) not null
  , header_uf4_cd character varying(20) not null
  , header_uf5_cd character varying(20) not null
  , header_uf6_cd character varying(20) not null
  , header_uf7_cd character varying(20) not null
  , header_uf8_cd character varying(20) not null
  , header_uf9_cd character varying(20) not null
  , header_uf10_cd character varying(20) not null
  , header_uf_kotei1_cd character varying(20) not null
  , header_uf_kotei2_cd character varying(20) not null
  , header_uf_kotei3_cd character varying(20) not null
  , header_uf_kotei4_cd character varying(20) not null
  , header_uf_kotei5_cd character varying(20) not null
  , header_uf_kotei6_cd character varying(20) not null
  , header_uf_kotei7_cd character varying(20) not null
  , header_uf_kotei8_cd character varying(20) not null
  , header_uf_kotei9_cd character varying(20) not null
  , header_uf_kotei10_cd character varying(20) not null
  , header_kazei_kbn character varying not null
  , header_zeiritsu character varying(10) not null
  , header_keigen_zeiritsu_kbn character varying(1) not null
  , header_bunri_kbn character varying(1) not null
  , header_kobetsu_kbn character varying(1) not null
  , kari_futan_bumon_cd character varying not null
  , kari_kamoku_cd character varying not null
  , kari_kamoku_edaban_cd character varying not null
  , kari_torihikisaki_cd character varying not null
  , kari_project_cd character varying not null
  , kari_segment_cd character varying(8) not null
  , kari_uf1_cd character varying(20) not null
  , kari_uf2_cd character varying(20) not null
  , kari_uf3_cd character varying(20) not null
  , kari_uf4_cd character varying(20) not null
  , kari_uf5_cd character varying(20) not null
  , kari_uf6_cd character varying(20) not null
  , kari_uf7_cd character varying(20) not null
  , kari_uf8_cd character varying(20) not null
  , kari_uf9_cd character varying(20) not null
  , kari_uf10_cd character varying(20) not null
  , kari_uf_kotei1_cd character varying(20) not null
  , kari_uf_kotei2_cd character varying(20) not null
  , kari_uf_kotei3_cd character varying(20) not null
  , kari_uf_kotei4_cd character varying(20) not null
  , kari_uf_kotei5_cd character varying(20) not null
  , kari_uf_kotei6_cd character varying(20) not null
  , kari_uf_kotei7_cd character varying(20) not null
  , kari_uf_kotei8_cd character varying(20) not null
  , kari_uf_kotei9_cd character varying(20) not null
  , kari_uf_kotei10_cd character varying(20) not null
  , kari_kazei_kbn character varying not null
  , kari_zeiritsu character varying(10) not null
  , kari_keigen_zeiritsu_kbn character varying(1) not null
  , kari_bunri_kbn character varying(1) not null
  , kari_kobetsu_kbn character varying(1) not null
  , kashi_futan_bumon_cd character varying not null
  , kashi_kamoku_cd character varying not null
  , kashi_kamoku_edaban_cd character varying not null
  , kashi_torihikisaki_cd character varying not null
  , kashi_project_cd character varying not null
  , kashi_segment_cd character varying(8) not null
  , kashi_uf1_cd character varying(20) not null
  , kashi_uf2_cd character varying(20) not null
  , kashi_uf3_cd character varying(20) not null
  , kashi_uf4_cd character varying(20) not null
  , kashi_uf5_cd character varying(20) not null
  , kashi_uf6_cd character varying(20) not null
  , kashi_uf7_cd character varying(20) not null
  , kashi_uf8_cd character varying(20) not null
  , kashi_uf9_cd character varying(20) not null
  , kashi_uf10_cd character varying(20) not null
  , kashi_uf_kotei1_cd character varying(20) not null
  , kashi_uf_kotei2_cd character varying(20) not null
  , kashi_uf_kotei3_cd character varying(20) not null
  , kashi_uf_kotei4_cd character varying(20) not null
  , kashi_uf_kotei5_cd character varying(20) not null
  , kashi_uf_kotei6_cd character varying(20) not null
  , kashi_uf_kotei7_cd character varying(20) not null
  , kashi_uf_kotei8_cd character varying(20) not null
  , kashi_uf_kotei9_cd character varying(20) not null
  , kashi_uf_kotei10_cd character varying(20) not null
  , kashi_kazei_kbn character varying not null
  , kashi_zeiritsu character varying(10) not null
  , kashi_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_bunri_kbn character varying(1) not null
  , kashi_kobetsu_kbn character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shiwake_pattern_master_zaimu_kyoten_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,shiwake_edano)
);
comment on table shiwake_pattern_master_zaimu_kyoten is '�d��p�^�[���}�X�^�[�i�������_���́j';
comment on column shiwake_pattern_master_zaimu_kyoten.denpyou_kbn is '�`�[�敪';
comment on column shiwake_pattern_master_zaimu_kyoten.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column shiwake_pattern_master_zaimu_kyoten.shiwake_edano is '�d��}�ԍ�';
comment on column shiwake_pattern_master_zaimu_kyoten.delete_flg is '�폜�t���O';
comment on column shiwake_pattern_master_zaimu_kyoten.yuukou_kigen_from is '�L�������J�n��';
comment on column shiwake_pattern_master_zaimu_kyoten.yuukou_kigen_to is '�L�������I����';
comment on column shiwake_pattern_master_zaimu_kyoten.bunrui1 is '����1';
comment on column shiwake_pattern_master_zaimu_kyoten.bunrui2 is '����2';
comment on column shiwake_pattern_master_zaimu_kyoten.bunrui3 is '����3';
comment on column shiwake_pattern_master_zaimu_kyoten.torihiki_name is '�����';
comment on column shiwake_pattern_master_zaimu_kyoten.tekiyou_flg is '�E�v�t���O';
comment on column shiwake_pattern_master_zaimu_kyoten.tekiyou is '�E�v';
comment on column shiwake_pattern_master_zaimu_kyoten.nyushukkin_flg is '���o���t���O';
comment on column shiwake_pattern_master_zaimu_kyoten.hyouji_jun is '�\����';
comment on column shiwake_pattern_master_zaimu_kyoten.shain_cd_renkei_flg is '�Ј��R�[�h�A�g�t���O';
comment on column shiwake_pattern_master_zaimu_kyoten.tenpu_file_hissu_flg is '�Y�t�t�@�C���K�{�t���O';
comment on column shiwake_pattern_master_zaimu_kyoten.shiwake_taishougai_flg is '�d��ΏۊO�t���O';
comment on column shiwake_pattern_master_zaimu_kyoten.fusen_flg is '�tⳃt���O';
comment on column shiwake_pattern_master_zaimu_kyoten.fusen_color is '�tⳃJ���[';
comment on column shiwake_pattern_master_zaimu_kyoten.zaimu_kyoten_nyuryoku_pattern_no_bumon_furikae is '����U�փp�^�[��No';
comment on column shiwake_pattern_master_zaimu_kyoten.zaimu_kyoten_nyuryoku_haifu_pattern_no is '�z���p�^�[��No';
comment on column shiwake_pattern_master_zaimu_kyoten.header_futan_bumon_cd is '�w�b�_�[���S����R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_torihikisaki_cd is '�w�b�_�[�����R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_kamoku_cd is '�w�b�_�[�ȖڃR�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_kamoku_edaban_cd is '�w�b�_�[�Ȗڎ}�ԃR�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_project_cd is '�w�b�_�[�v���W�F�N�g�R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_segment_cd is '�w�b�_�[�Z�O�����g�R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf1_cd is '�w�b�_�[UF1�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf2_cd is '�w�b�_�[UF2�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf3_cd is '�w�b�_�[UF3�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf4_cd is '�w�b�_�[UF4�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf5_cd is '�w�b�_�[UF5�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf6_cd is '�w�b�_�[UF6�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf7_cd is '�w�b�_�[UF7�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf8_cd is '�w�b�_�[UF8�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf9_cd is '�w�b�_�[UF9�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf10_cd is '�w�b�_�[UF10�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei1_cd is '�w�b�_�[UF1�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei2_cd is '�w�b�_�[UF2�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei3_cd is '�w�b�_�[UF3�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei4_cd is '�w�b�_�[UF4�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei5_cd is '�w�b�_�[UF5�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei6_cd is '�w�b�_�[UF6�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei7_cd is '�w�b�_�[UF7�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei8_cd is '�w�b�_�[UF8�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei9_cd is '�w�b�_�[UF9�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei10_cd is '�w�b�_�[UF10�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_kazei_kbn is '�w�b�_�[�ېŋ敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_zeiritsu is '�w�b�_�[����ŗ��i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_keigen_zeiritsu_kbn is '�w�b�_�[�y���ŗ��敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_bunri_kbn is '�w�b�_�[�����敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.header_kobetsu_kbn is '�w�b�_�[�ʋ敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_futan_bumon_cd is '�ؕ����S����R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_kamoku_cd is '�ؕ��ȖڃR�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_torihikisaki_cd is '�ؕ������R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_project_cd is '�ؕ��v���W�F�N�g�R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_segment_cd is '�ؕ��Z�O�����g�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf1_cd is '�ؕ�UF1�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf2_cd is '�ؕ�UF2�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf3_cd is '�ؕ�UF3�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf4_cd is '�ؕ�UF4�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf5_cd is '�ؕ�UF5�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf6_cd is '�ؕ�UF6�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf7_cd is '�ؕ�UF7�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf8_cd is '�ؕ�UF8�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf9_cd is '�ؕ�UF9�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf10_cd is '�ؕ�UF10�R�[�h';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei1_cd is '�ؕ�UF1�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei2_cd is '�ؕ�UF2�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei3_cd is '�ؕ�UF3�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei4_cd is '�ؕ�UF4�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei5_cd is '�ؕ�UF5�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei6_cd is '�ؕ�UF6�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei7_cd is '�ؕ�UF7�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei8_cd is '�ؕ�UF8�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei9_cd is '�ؕ�UF9�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei10_cd is '�ؕ�UF10�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_kazei_kbn is '�ؕ��ېŋ敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_zeiritsu is '�ؕ�����ŗ��i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_keigen_zeiritsu_kbn is '�ؕ��y���ŗ��敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_bunri_kbn is '�ؕ������敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_kobetsu_kbn is '�ؕ��ʋ敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_futan_bumon_cd is '�ݕ����S����R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_torihikisaki_cd is '�ݕ������R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_project_cd is '�ݕ��v���W�F�N�g�R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_segment_cd is '�ݕ��Z�O�����g�R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf1_cd is '�ݕ�UF1�R�[�h�P';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf2_cd is '�ݕ�UF2�R�[�h�P';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf3_cd is '�ݕ�UF3�R�[�h�P';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf4_cd is '�ݕ�UF4�R�[�h�P';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf5_cd is '�ݕ�UF5�R�[�h�P';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf6_cd is '�ݕ�UF6�R�[�h�P';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf7_cd is '�ݕ�UF7�R�[�h�P';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf8_cd is '�ݕ�UF8�R�[�h�P';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf9_cd is '�ݕ�UF9�R�[�h�P';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf10_cd is '�ݕ�UF10�R�[�h�P';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei1_cd is '�ݕ�UF1�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei2_cd is '�ݕ�UF2�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei3_cd is '�ݕ�UF3�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei4_cd is '�ݕ�UF4�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei5_cd is '�ݕ�UF5�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei6_cd is '�ݕ�UF6�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei7_cd is '�ݕ�UF7�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei8_cd is '�ݕ�UF8�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei9_cd is '�ݕ�UF9�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei10_cd is '�ݕ�UF10�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_kazei_kbn is '�ݕ��ېŋ敪�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_zeiritsu is '�ݕ�����ŗ��i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_keigen_zeiritsu_kbn is '�ݕ��y���ŗ��敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_bunri_kbn is '�ݕ������敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_kobetsu_kbn is '�ݕ��ʋ敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master_zaimu_kyoten.touroku_user_id is '�o�^���[�U�[ID';
comment on column shiwake_pattern_master_zaimu_kyoten.touroku_time is '�o�^����';
comment on column shiwake_pattern_master_zaimu_kyoten.koushin_user_id is '�X�V���[�U�[ID';
comment on column shiwake_pattern_master_zaimu_kyoten.koushin_time is '�X�V����';

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
  , kaishi_zandaka numeric(15)
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
comment on column suitouchou_setting.kaishi_zandaka is '�J�n�c��';
comment on column suitouchou_setting.denpyou_no_tani_flg is '�`�[�ԍ��P�ʃt���O';
comment on column suitouchou_setting.touroku_user_id is '�o�^���[�U�[ID';
comment on column suitouchou_setting.touroku_time is '�o�^����';
comment on column suitouchou_setting.koushin_user_id is '�X�V���[�U�[ID';
comment on column suitouchou_setting.koushin_time is '�X�V����';

create table zaimu_kyoten_nyuryoku_bumon_security (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , futan_bumon_cd character varying(8) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_bumon_security_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,futan_bumon_cd)
);
comment on table zaimu_kyoten_nyuryoku_bumon_security is '�������_���͕���Z�L�����e�B';
comment on column zaimu_kyoten_nyuryoku_bumon_security.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_bumon_security.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_bumon_security.futan_bumon_cd is '���S����R�[�h';
comment on column zaimu_kyoten_nyuryoku_bumon_security.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_bumon_security.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_bumon_security.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_bumon_security.koushin_time is '�X�V����';

create table zaimu_kyoten_nyuryoku_denpyou_no (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , denpyou_serial_no_start bigint not null
  , denpyou_serial_no_end bigint not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_denpyou_no_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,yuukou_kigen_to)
);
comment on table zaimu_kyoten_nyuryoku_denpyou_no is '�������_���`�[�ԍ��ݒ�';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.yuukou_kigen_from is '�L�������J�n��';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.yuukou_kigen_to is '�L�������I����';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.denpyou_serial_no_start is '�J�n�ԍ��i�`�[�ԍ��j';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.denpyou_serial_no_end is '�I���ԍ��i�`�[�ԍ��j';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.koushin_time is '�X�V����';

create table zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , sequence_val integer not null
  , constraint zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,yuukou_kigen_to)
);
comment on table zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban is '�������_���`�[�ԍ��̔�';
comment on column zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban.yuukou_kigen_from is '�L�������J�n��';
comment on column zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban.yuukou_kigen_to is '�L�������I����';
comment on column zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban.sequence_val is '�V�[�P���X�l';

create table zaimu_kyoten_nyuryoku_ichiran (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , zaimu_kyoten_nyuryoku_pattern_name character varying(20) not null
  , naiyou character varying(160) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , denpyou_print_flg character varying(1) not null
  , shounin_jyoukyou_print_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_ichiran_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no)
);
comment on table zaimu_kyoten_nyuryoku_ichiran is '�������_���͈ꗗ';
comment on column zaimu_kyoten_nyuryoku_ichiran.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_ichiran.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_ichiran.zaimu_kyoten_nyuryoku_pattern_name is '�������_���̓p�^�[������';
comment on column zaimu_kyoten_nyuryoku_ichiran.naiyou is '���e�i�`�[�j';
comment on column zaimu_kyoten_nyuryoku_ichiran.yuukou_kigen_from is '�L�������J�n��';
comment on column zaimu_kyoten_nyuryoku_ichiran.yuukou_kigen_to is '�L�������I����';
comment on column zaimu_kyoten_nyuryoku_ichiran.denpyou_print_flg is '�\�������[�o�̓t���O';
comment on column zaimu_kyoten_nyuryoku_ichiran.shounin_jyoukyou_print_flg is '���F�󋵗�����t���O';
comment on column zaimu_kyoten_nyuryoku_ichiran.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_ichiran.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_ichiran.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_ichiran.koushin_time is '�X�V����';

create table zaimu_kyoten_nyuryoku_kamoku_security (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , kamoku_gaibu_cd character varying(6) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_kamoku_security_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,kamoku_gaibu_cd)
);
comment on table zaimu_kyoten_nyuryoku_kamoku_security is '�������_���͉ȖڃZ�L�����e�B';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.koushin_time is '�X�V����';

create table zaimu_kyoten_nyuryoku_shounin_route (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , edano integer not null
  , user_id character varying(30) not null
  , dairi_shounin_user_id character varying(30) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_shounin_route_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,edano)
);
comment on table zaimu_kyoten_nyuryoku_shounin_route is '�������_���͏��F���[�g';
comment on column zaimu_kyoten_nyuryoku_shounin_route.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_shounin_route.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_shounin_route.edano is '�}�ԍ�';
comment on column zaimu_kyoten_nyuryoku_shounin_route.user_id is '���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.dairi_shounin_user_id is '�㗝���F�҃��[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_shounin_route.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.koushin_time is '�X�V����';

create table zaimu_kyoten_nyuryoku_tsukekaemoto (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , moto_denpyou_kbn character varying(4) not null
  , moto_zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_tsukekaemoto_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,moto_denpyou_kbn,moto_zaimu_kyoten_nyuryoku_pattern_no)
);
comment on table zaimu_kyoten_nyuryoku_tsukekaemoto is '�������_���͕t�֌�';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.moto_denpyou_kbn is '�t�֌��`�[�敪';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.moto_zaimu_kyoten_nyuryoku_pattern_no is '�t�֌��������_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.koushin_time is '�X�V����';

create table zaimu_kyoten_nyuryoku_user_info (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , user_id character varying(30) not null
  , open21_user_id character varying(4) not null
  , nyuryoku_flg character varying(1) not null
  , shiwake_shusei_kengen_flg character varying(1) default '0' not null
  , bumon_tsukekae_kengen_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_user_info_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,user_id)
);
comment on table zaimu_kyoten_nyuryoku_user_info is '�������_���̓��[�U�[';
comment on column zaimu_kyoten_nyuryoku_user_info.denpyou_kbn is '�`�[�敪';
comment on column zaimu_kyoten_nyuryoku_user_info.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column zaimu_kyoten_nyuryoku_user_info.user_id is '���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_user_info.open21_user_id is 'OPEN21���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_user_info.nyuryoku_flg is '���͎҃t���O';
comment on column zaimu_kyoten_nyuryoku_user_info.shiwake_shusei_kengen_flg is '�d��C�������t���O';
comment on column zaimu_kyoten_nyuryoku_user_info.bumon_tsukekae_kengen_flg is '����t�֌����t���O';
comment on column zaimu_kyoten_nyuryoku_user_info.touroku_user_id is '�o�^���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_user_info.touroku_time is '�o�^����';
comment on column zaimu_kyoten_nyuryoku_user_info.koushin_user_id is '�X�V���[�U�[ID';
comment on column zaimu_kyoten_nyuryoku_user_info.koushin_time is '�X�V����';


-- ���a���o�[���e�[�u��

create table genyokin_suitouchou (
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
  , constraint genyokin_suitouchou_PKEY primary key (denpyou_id)
);

create table genyokin_suitouchou_meisai (
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
  , constraint genyokin_suitouchou_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

comment on table genyokin_suitouchou is '���a���o�[��';
comment on column genyokin_suitouchou.denpyou_id is '�`�[ID';
comment on column genyokin_suitouchou.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column genyokin_suitouchou.denpyou_date is '�`�[���t';
comment on column genyokin_suitouchou.nyukin_goukei is '�������v';
comment on column genyokin_suitouchou.shukkin_goukei is '�o�����v';
comment on column genyokin_suitouchou.toujitsu_zandaka is '�����c��';
comment on column genyokin_suitouchou.suitou_kamoku_cd is '�o�[�ȖڃR�[�h';
comment on column genyokin_suitouchou.suitou_kamoku_name is '�o�[�Ȗږ�';
comment on column genyokin_suitouchou.suitou_kamoku_edaban_cd is '�o�[�Ȗڎ}�ԃR�[�h';
comment on column genyokin_suitouchou.suitou_kamoku_edaban_name is '�o�[�Ȗڎ}�Ԗ�';
comment on column genyokin_suitouchou.suitou_futan_bumon_cd is '�o�[���S����R�[�h';
comment on column genyokin_suitouchou.suitou_futan_bumon_name is '�o�[���S���喼';
comment on column genyokin_suitouchou.bikou is '���l';
comment on column genyokin_suitouchou.touroku_user_id is '�o�^���[�U�[ID';
comment on column genyokin_suitouchou.touroku_time is '�o�^����';
comment on column genyokin_suitouchou.koushin_user_id is '�X�V���[�U�[ID';
comment on column genyokin_suitouchou.koushin_time is '�X�V����';

comment on table genyokin_suitouchou_meisai is '���a���o�[������';
comment on column genyokin_suitouchou_meisai.denpyou_id is '�`�[ID';
comment on column genyokin_suitouchou_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column genyokin_suitouchou_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column genyokin_suitouchou_meisai.denpyou_date is '�`�[���t';
comment on column genyokin_suitouchou_meisai.serial_no is '�V���A���ԍ�';
comment on column genyokin_suitouchou_meisai.torihiki_name is '�����';
comment on column genyokin_suitouchou_meisai.tekiyou is '�E�v';
comment on column genyokin_suitouchou_meisai.nyukin_goukei is '����';
comment on column genyokin_suitouchou_meisai.shukkin_goukei is '�o��';
comment on column genyokin_suitouchou_meisai.aite_kazei_kbn is '�ېŋ敪';
comment on column genyokin_suitouchou_meisai.aite_zeiritsu is '�ŗ�';
comment on column genyokin_suitouchou_meisai.aite_keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column genyokin_suitouchou_meisai.fusen_color is '�tⳃJ���[';
comment on column genyokin_suitouchou_meisai.meisai_touroku_user_id is '���דo�^���[�U�[ID';
comment on column genyokin_suitouchou_meisai.meisai_touroku_shain_no is '���דo�^���[�U�[�Ј�No';
comment on column genyokin_suitouchou_meisai.meisai_touroku_user_sei is '���דo�^���[�U�[��';
comment on column genyokin_suitouchou_meisai.meisai_touroku_user_mei is '���דo�^���[�U�[��';
comment on column genyokin_suitouchou_meisai.tenpu_file_edano is '�Y�t�t�@�C���}��';
comment on column genyokin_suitouchou_meisai.shiwake_serial_no is '�d��f�[�^�V���A��No';
comment on column genyokin_suitouchou_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column genyokin_suitouchou_meisai.touroku_time is '�o�^����';
comment on column genyokin_suitouchou_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column genyokin_suitouchou_meisai.koushin_time is '�X�V����';


-- �`�[�ꗗ�������_
create table denpyou_ichiran_kyoten (
  denpyou_id character varying(19) not null
  , denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , serial_no bigint
  , denpyou_joutai character varying(2) not null
  , name character varying not null
  , denpyou_shubetsu_url character varying(240) not null
  , touroku_time timestamp without time zone
  , koushin_time timestamp without time zone
  , shouninbi timestamp without time zone
  , bumon_cd character varying(8) not null
  , bumon_full_name character varying not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , all_cnt bigint
  , cur_cnt bigint
  , zan_cnt bigint
  , gen_bumon_full_name character varying not null
  , gen_user_id character varying not null
  , gen_user_full_name character varying not null
  , gen_gyoumu_role_id character varying not null
  , gen_gyoumu_role_name character varying not null
  , gen_name character varying not null
  , shiwake_status character varying not null
  , kingaku numeric(15)
  , denpyou_date date
  , constraint denpyou_ichiran_kyoten_PKEY primary key (denpyou_id)
);

comment on table denpyou_ichiran_kyoten is '�`�[�ꗗ(�������_)';
comment on column denpyou_ichiran_kyoten.denpyou_id is '�`�[ID';
comment on column denpyou_ichiran_kyoten.denpyou_kbn is '�`�[�敪';
comment on column denpyou_ichiran_kyoten.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column denpyou_ichiran_kyoten.serial_no is '�V���A���ԍ�';
comment on column denpyou_ichiran_kyoten.denpyou_joutai is '�`�[���';
comment on column denpyou_ichiran_kyoten.name is '�X�e�[�^�X';
comment on column denpyou_ichiran_kyoten.denpyou_shubetsu_url is '�`�[���URL';
comment on column denpyou_ichiran_kyoten.touroku_time is '�o�^����';
comment on column denpyou_ichiran_kyoten.koushin_time is '�X�V����';
comment on column denpyou_ichiran_kyoten.shouninbi is '���F��';
comment on column denpyou_ichiran_kyoten.bumon_cd is '����R�[�h';
comment on column denpyou_ichiran_kyoten.bumon_full_name is '����t����';
comment on column denpyou_ichiran_kyoten.user_id is '���[�U�[ID';
comment on column denpyou_ichiran_kyoten.user_full_name is '���[�U�[�t����';
comment on column denpyou_ichiran_kyoten.all_cnt is '�S���F�l���J�E���g';
comment on column denpyou_ichiran_kyoten.cur_cnt is '���F�ϐl���J�E���g';
comment on column denpyou_ichiran_kyoten.zan_cnt is '�c�菳�F�l���J�E���g';
comment on column denpyou_ichiran_kyoten.gen_bumon_full_name is '���ݏ��F�ҕ���t����';
comment on column denpyou_ichiran_kyoten.gen_user_id is '���ݏ��F�҃��[�U�[ID���X�g';
comment on column denpyou_ichiran_kyoten.gen_user_full_name is '���ݏ��F�҃��[�U�[�t����';
comment on column denpyou_ichiran_kyoten.gen_gyoumu_role_id is '���ݏ��F�ҋƖ����[��ID���X�g';
comment on column denpyou_ichiran_kyoten.gen_gyoumu_role_name is '���ݏ��F�ҋƖ����[����';
comment on column denpyou_ichiran_kyoten.gen_name is '���ݏ��F�Җ���';
comment on column denpyou_ichiran_kyoten.shiwake_status is '�d��f�[�^�쐬�X�e�[�^�X';
comment on column denpyou_ichiran_kyoten.kingaku is '���z';
comment on column denpyou_ichiran_kyoten.denpyou_date is '�`�[���t';


-- �`�[��ʈꗗ(�������_)
create table denpyou_shubetsu_ichiran_kyoten (
  denpyou_kbn character varying(4) not null
  , denpyou_shubetsu character varying(20) not null
  , denpyou_print_shubetsu character varying(20)
  , hyouji_jun integer not null
  , gyoumu_shubetsu character varying(20) not null
  , naiyou character varying(160) not null
  , denpyou_shubetsu_url character varying(240) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , kanren_sentaku_flg character varying(1) not null
  , kanren_hyouji_flg character varying(1) not null
  , denpyou_print_flg character varying(1) not null
  , shounin_jyoukyou_print_flg character varying(1) default '0' not null
  , shinsei_shori_kengen_name character varying(6) not null
  , shiiresaki_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint denpyou_shubetsu_ichiran_kyoten_PKEY primary key (denpyou_kbn)
);

comment on table denpyou_shubetsu_ichiran_kyoten is '�`�[��ʈꗗ(�������_)';
comment on column denpyou_shubetsu_ichiran_kyoten.denpyou_kbn is '�`�[�敪';
comment on column denpyou_shubetsu_ichiran_kyoten.denpyou_shubetsu is '�`�[���';
comment on column denpyou_shubetsu_ichiran_kyoten.denpyou_print_shubetsu is '�`�[��ʁi���[�j';
comment on column denpyou_shubetsu_ichiran_kyoten.hyouji_jun is '�\����';
comment on column denpyou_shubetsu_ichiran_kyoten.gyoumu_shubetsu is '�Ɩ����';
comment on column denpyou_shubetsu_ichiran_kyoten.naiyou is '���e�i�`�[�j';
comment on column denpyou_shubetsu_ichiran_kyoten.denpyou_shubetsu_url is '�`�[���URL';
comment on column denpyou_shubetsu_ichiran_kyoten.yuukou_kigen_from is '�L�������J�n��';
comment on column denpyou_shubetsu_ichiran_kyoten.yuukou_kigen_to is '�L�������I����';
comment on column denpyou_shubetsu_ichiran_kyoten.kanren_sentaku_flg is '�֘A�`�[�I���t���O';
comment on column denpyou_shubetsu_ichiran_kyoten.kanren_hyouji_flg is '�֘A�`�[���͗��\���t���O';
comment on column denpyou_shubetsu_ichiran_kyoten.denpyou_print_flg is '�\�������[�o�̓t���O';
comment on column denpyou_shubetsu_ichiran_kyoten.shounin_jyoukyou_print_flg is '���F�󋵗�����t���O';
comment on column denpyou_shubetsu_ichiran_kyoten.shinsei_shori_kengen_name is '�\������������';
comment on column denpyou_shubetsu_ichiran_kyoten.shiiresaki_flg is '�d����t���O';
comment on column denpyou_shubetsu_ichiran_kyoten.touroku_user_id is '�o�^���[�U�[ID';
comment on column denpyou_shubetsu_ichiran_kyoten.touroku_time is '�o�^����';
comment on column denpyou_shubetsu_ichiran_kyoten.koushin_user_id is '�X�V���[�U�[ID';
comment on column denpyou_shubetsu_ichiran_kyoten.koushin_time is '�X�V����';

\copy denpyou_shubetsu_ichiran_kyoten FROM '.\files\csv\denpyou_shubetsu_ichiran_kyoten.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--�d��֘A
create table teikei_shiwake (
  user_id character varying(30) not null
  , teikei_shiwake_pattern_no integer not null
  , teikei_shiwake_pattern_name character varying(60) not null
  , kari_kingaku_goukei numeric(15)
  , kashi_kingaku_goukei numeric(15)
  , denpyou_date date
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
  , denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint teikei_shiwake_PKEY primary key (user_id,teikei_shiwake_pattern_no)
);

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
  , gyou_kugiri character varying(1) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint teikei_shiwake_meisai_PKEY primary key (user_id,teikei_shiwake_pattern_no,teikei_shiwake_edano)
);

create table zaimu_kyoten_shiwake_de3 (
  serial_no bigserial not null
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
  serial_no bigserial not null
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

comment on table teikei_shiwake is '��^�d��';
comment on column teikei_shiwake.user_id is '���[�U�[ID';
comment on column teikei_shiwake.teikei_shiwake_pattern_no is '��^�d��p�^�[���ԍ�';
comment on column teikei_shiwake.teikei_shiwake_pattern_name is '��^�d��p�^�[����';
comment on column teikei_shiwake.kari_kingaku_goukei is '�ؕ����z���v';
comment on column teikei_shiwake.kashi_kingaku_goukei is '�ݕ����z���v';
comment on column teikei_shiwake.denpyou_date is '�`�[���t';
comment on column teikei_shiwake.hf1_cd is 'HF1�R�[�h';
comment on column teikei_shiwake.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column teikei_shiwake.hf2_cd is 'HF2�R�[�h';
comment on column teikei_shiwake.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column teikei_shiwake.hf3_cd is 'HF3�R�[�h';
comment on column teikei_shiwake.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column teikei_shiwake.hf4_cd is 'HF4�R�[�h';
comment on column teikei_shiwake.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column teikei_shiwake.hf5_cd is 'HF5�R�[�h';
comment on column teikei_shiwake.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column teikei_shiwake.hf6_cd is 'HF6�R�[�h';
comment on column teikei_shiwake.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column teikei_shiwake.hf7_cd is 'HF7�R�[�h';
comment on column teikei_shiwake.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column teikei_shiwake.hf8_cd is 'HF8�R�[�h';
comment on column teikei_shiwake.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column teikei_shiwake.hf9_cd is 'HF9�R�[�h';
comment on column teikei_shiwake.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column teikei_shiwake.hf10_cd is 'HF10�R�[�h';
comment on column teikei_shiwake.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column teikei_shiwake.bikou is '���l';
comment on column teikei_shiwake.denpyou_kbn is '�`�[�敪';
comment on column teikei_shiwake.zaimu_kyoten_nyuryoku_pattern_no is '�������_���̓p�^�[��No';
comment on column teikei_shiwake.touroku_user_id is '�o�^���[�U�[ID';
comment on column teikei_shiwake.touroku_time is '�o�^����';
comment on column teikei_shiwake.koushin_user_id is '�X�V���[�U�[ID';
comment on column teikei_shiwake.koushin_time is '�X�V����';

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
comment on column teikei_shiwake_meisai.gyou_kugiri is '�s��؂�';
comment on column teikei_shiwake_meisai.zaimu_kyoten_nyuryoku_haifu_pattern_no is '�������_���͔z���p�^�[��No';
comment on column teikei_shiwake_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column teikei_shiwake_meisai.touroku_time is '�o�^����';
comment on column teikei_shiwake_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column teikei_shiwake_meisai.koushin_time is '�X�V����';

comment on table zaimu_kyoten_shiwake_de3 is '�������_�d�󒊏o(de3)';
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

comment on table zaimu_kyoten_shiwake_sias is '�������_�d�󒊏o(SIAS)';
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


-- ���F���[�g
create table shounin_route_kyoten (
  denpyou_id character varying(19) not null
  , edano integer not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , dairi_shounin_user_id character varying(30) not null
  , dairi_shounin_user_full_name character varying(50) not null
  , genzai_flg character varying(1) not null
  , joukyou_edano integer
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shounin_route_kyoten_PKEY primary key (denpyou_id,edano)
);
comment on table shounin_route_kyoten is '���F���[�g(�������_)';
comment on column shounin_route_kyoten.denpyou_id is '�`�[ID';
comment on column shounin_route_kyoten.edano is '�}�ԍ�';
comment on column shounin_route_kyoten.user_id is '���[�U�[ID';
comment on column shounin_route_kyoten.user_full_name is '���[�U�[�t����';
comment on column shounin_route_kyoten.dairi_shounin_user_id is '�㗝���F�҃��[�U�[ID';
comment on column shounin_route_kyoten.dairi_shounin_user_full_name is '�㗝���F�҃��[�U�[�t����';
comment on column shounin_route_kyoten.genzai_flg is '���݃t���O';
comment on column shounin_route_kyoten.joukyou_edano is '���F�󋵎}��';
comment on column shounin_route_kyoten.touroku_user_id is '�o�^���[�U�[ID';
comment on column shounin_route_kyoten.touroku_time is '�o�^����';
comment on column shounin_route_kyoten.koushin_user_id is '�X�V���[�U�[ID';
comment on column shounin_route_kyoten.koushin_time is '�X�V����';


-- ���F��
create table shounin_joukyou_kyoten (
  denpyou_id character varying(19) not null
  , edano integer not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , joukyou_cd character varying(1) not null
  , joukyou character varying not null
  , comment character varying not null
  , shounin_route_edano integer
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shounin_joukyou_kyoten_PKEY primary key (denpyou_id,edano)
);

comment on table shounin_joukyou_kyoten is '���F��(�������_)';
comment on column shounin_joukyou_kyoten.denpyou_id is '�`�[ID';
comment on column shounin_joukyou_kyoten.edano is '�}�ԍ�';
comment on column shounin_joukyou_kyoten.user_id is '���[�U�[ID';
comment on column shounin_joukyou_kyoten.user_full_name is '���[�U�[�t����';
comment on column shounin_joukyou_kyoten.joukyou_cd is '�󋵃R�[�h';
comment on column shounin_joukyou_kyoten.joukyou is '��';
comment on column shounin_joukyou_kyoten.comment is '�R�����g';
comment on column shounin_joukyou_kyoten.shounin_route_edano is '���F���[�g�}�ԍ�';
comment on column shounin_joukyou_kyoten.touroku_user_id is '�o�^���[�U�[ID';
comment on column shounin_joukyou_kyoten.touroku_time is '�o�^����';
comment on column shounin_joukyou_kyoten.koushin_user_id is '�X�V���[�U�[ID';
comment on column shounin_joukyou_kyoten.koushin_time is '�X�V����';


commit;
