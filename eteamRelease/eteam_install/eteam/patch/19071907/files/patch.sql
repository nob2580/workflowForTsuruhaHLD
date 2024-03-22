SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- �A���[_0911_�c���̑ݕ�11�̎捞���Ԉ���Ă���
DELETE FROM master_torikomi_shousai_de3;
DELETE FROM master_torikomi_shousai_sias;
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- �A���[_K-105
--�����R�[�h ���I�v�V�����p���R�[�h�̓p�b�`�O�ɂȂ���΍폜
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- �x���˗��\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- ���������\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

--LT�s�
--���_�z���Ȗڃe�[�u���A���_�z���Ȗڕ���e�[�u���A���_���͎҃e�[�u���̓o�^���ԁA�X�V���Ԃ̌^�ύX(ver19071903,19071906�ł̏C���R��)
ALTER TABLE zaimu_kyoten_nyuryoku_haifu_kamoku RENAME TO zaimu_kyoten_nyuryoku_haifu_kamoku_tmp;
create table zaimu_kyoten_nyuryoku_haifu_kamoku (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , kamoku_gaibu_cd character varying(8) not null
  , kamoku_edaban_cd character varying(12) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,zaimu_kyoten_nyuryoku_haifu_pattern_no,yuukou_kigen_from,kamoku_gaibu_cd,kamoku_edaban_cd)
)WITHOUT OIDS;
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
INSERT INTO zaimu_kyoten_nyuryoku_haifu_kamoku(
	denpyou_kbn
  , zaimu_kyoten_nyuryoku_pattern_no
  , zaimu_kyoten_nyuryoku_haifu_pattern_no
  , yuukou_kigen_from
  , yuukou_kigen_to
  , kamoku_gaibu_cd
  , kamoku_edaban_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
)SELECT
  	denpyou_kbn
  , zaimu_kyoten_nyuryoku_pattern_no
  , zaimu_kyoten_nyuryoku_haifu_pattern_no
  , yuukou_kigen_from
  , yuukou_kigen_to
  , kamoku_gaibu_cd
  , kamoku_edaban_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM zaimu_kyoten_nyuryoku_haifu_kamoku_tmp;
DROP TABLE zaimu_kyoten_nyuryoku_haifu_kamoku_tmp;

ALTER TABLE zaimu_kyoten_nyuryoku_haifu_bumon RENAME TO zaimu_kyoten_nyuryoku_haifu_bumon_tmp;
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
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,zaimu_kyoten_nyuryoku_haifu_pattern_no,yuukou_kigen_from,futan_bumon_cd)
)WITHOUT OIDS;
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
INSERT INTO zaimu_kyoten_nyuryoku_haifu_bumon(
  	denpyou_kbn
  , zaimu_kyoten_nyuryoku_pattern_no
  , zaimu_kyoten_nyuryoku_haifu_pattern_no
  , yuukou_kigen_from
  , yuukou_kigen_to
  , futan_bumon_cd
  , haifu_rate
  , sagaku_flg
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
)SELECT
  	denpyou_kbn
  , zaimu_kyoten_nyuryoku_pattern_no
  , zaimu_kyoten_nyuryoku_haifu_pattern_no
  , yuukou_kigen_from
  , yuukou_kigen_to
  , futan_bumon_cd
  , haifu_rate
  , sagaku_flg
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM zaimu_kyoten_nyuryoku_haifu_bumon_tmp;
DROP TABLE zaimu_kyoten_nyuryoku_haifu_bumon_tmp;

ALTER TABLE zaimu_kyoten_nyuryoku_nyuryokusha RENAME TO zaimu_kyoten_nyuryoku_nyuryokusha_tmp;
create table zaimu_kyoten_nyuryoku_nyuryokusha (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , user_id character varying(30) not null
  , bumon_tsukekae_kengen_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,user_id)
)WITHOUT OIDS;
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
  , zaimu_kyoten_nyuryoku_pattern_no
  , yuukou_kigen_from
  , yuukou_kigen_to
  , user_id
  , bumon_tsukekae_kengen_flg
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
)SELECT
	denpyou_kbn
  , zaimu_kyoten_nyuryoku_pattern_no
  , yuukou_kigen_from
  , yuukou_kigen_to
  , user_id
  , bumon_tsukekae_kengen_flg
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM zaimu_kyoten_nyuryoku_nyuryokusha_tmp;
DROP TABLE zaimu_kyoten_nyuryoku_nyuryokusha_tmp;

commit;
