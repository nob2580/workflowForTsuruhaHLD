SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--�A���[_0666_WEB����{�^���̕\������ �����R�[�h�ݒ�X�V
--�A���[_0715 �����}�ԃR�[�h�A�g�Ή��������Ɏ��{
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- �x���˗��\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- ���������\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

--�A���[_0666_WEB����{�^���̕\������ �@�\����ǉ�
\copy kinou_seigyo FROM '.\files\csv\kinou_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--�A���[_0715 �����}�ԃR�[�h�A�g�Ή� �d��p�^�[���ݒ�
CREATE TABLE shiwake_pattern_setting_tmp AS SELECT * FROM shiwake_pattern_setting WHERE 1 = 2;
\copy shiwake_pattern_setting_tmp FROM '.\files\csv\shiwake_pattern_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM shiwake_pattern_setting_tmp WHERE denpyou_kbn='A003' AND (SELECT COUNT(*) FROM shiwake_pattern_setting WHERE denpyou_kbn='A003') = 0;
DELETE FROM shiwake_pattern_setting_tmp WHERE denpyou_kbn='A013' AND (SELECT COUNT(*) FROM shiwake_pattern_setting WHERE denpyou_kbn='A013') = 0;
DELETE FROM shiwake_pattern_setting;
INSERT INTO shiwake_pattern_setting SELECT * FROM shiwake_pattern_setting_tmp;
DROP TABLE shiwake_pattern_setting_tmp;

--�A���[_0715 �����}�ԃR�[�h�A�g�Ή� �d��p�^�[���}�X�^�e�[�u���X�V
ALTER TABLE shiwake_pattern_master RENAME TO shiwake_pattern_master_old;
create table shiwake_pattern_master (
  denpyou_kbn character varying(4) not null
  , shiwake_edano integer not null
  , delete_flg character varying(1) default '0' not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , bunrui1 character varying(20) not null
  , bunrui2 character varying(20) not null
  , bunrui3 character varying(20) not null
  , torihiki_name character varying(20) not null
  , tekiyou_flg character varying(1) not null
  , tekiyou character varying(20)
  , default_hyouji_flg character varying(1) not null
  , kousaihi_hyouji_flg character varying(1) not null
  , kake_flg character varying(1) not null
  , hyouji_jun integer not null
  , shain_cd_renkei_flg character varying(1) not null
  , edaban_renkei_flg character varying(1) not null
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
  , kashi_futan_bumon_cd1 character varying not null
  , kashi_kamoku_cd1 character varying not null
  , kashi_kamoku_edaban_cd1 character varying not null
  , kashi_torihikisaki_cd1 character varying not null
  , kashi_project_cd1 character varying not null
  , kashi_segment_cd1 character varying(8) not null
  , kashi_uf1_cd1 character varying(20) not null
  , kashi_uf2_cd1 character varying(20) not null
  , kashi_uf3_cd1 character varying(20) not null
  , kashi_uf4_cd1 character varying(20) not null
  , kashi_uf5_cd1 character varying(20) not null
  , kashi_uf6_cd1 character varying(20) not null
  , kashi_uf7_cd1 character varying(20) not null
  , kashi_uf8_cd1 character varying(20) not null
  , kashi_uf9_cd1 character varying(20) not null
  , kashi_uf10_cd1 character varying(20) not null
  , kashi_uf_kotei1_cd1 character varying(20) not null
  , kashi_uf_kotei2_cd1 character varying(20) not null
  , kashi_uf_kotei3_cd1 character varying(20) not null
  , kashi_uf_kotei4_cd1 character varying(20) not null
  , kashi_uf_kotei5_cd1 character varying(20) not null
  , kashi_uf_kotei6_cd1 character varying(20) not null
  , kashi_uf_kotei7_cd1 character varying(20) not null
  , kashi_uf_kotei8_cd1 character varying(20) not null
  , kashi_uf_kotei9_cd1 character varying(20) not null
  , kashi_uf_kotei10_cd1 character varying(20) not null
  , kashi_kazei_kbn1 character varying not null
  , kashi_futan_bumon_cd2 character varying not null
  , kashi_torihikisaki_cd2 character varying not null
  , kashi_kamoku_cd2 character varying not null
  , kashi_kamoku_edaban_cd2 character varying not null
  , kashi_project_cd2 character varying not null
  , kashi_segment_cd2 character varying(8) not null
  , kashi_uf1_cd2 character varying(20) not null
  , kashi_uf2_cd2 character varying(20) not null
  , kashi_uf3_cd2 character varying(20) not null
  , kashi_uf4_cd2 character varying(20) not null
  , kashi_uf5_cd2 character varying(20) not null
  , kashi_uf6_cd2 character varying(20) not null
  , kashi_uf7_cd2 character varying(20) not null
  , kashi_uf8_cd2 character varying(20) not null
  , kashi_uf9_cd2 character varying(20) not null
  , kashi_uf10_cd2 character varying(20) not null
  , kashi_uf_kotei1_cd2 character varying(20) not null
  , kashi_uf_kotei2_cd2 character varying(20) not null
  , kashi_uf_kotei3_cd2 character varying(20) not null
  , kashi_uf_kotei4_cd2 character varying(20) not null
  , kashi_uf_kotei5_cd2 character varying(20) not null
  , kashi_uf_kotei6_cd2 character varying(20) not null
  , kashi_uf_kotei7_cd2 character varying(20) not null
  , kashi_uf_kotei8_cd2 character varying(20) not null
  , kashi_uf_kotei9_cd2 character varying(20) not null
  , kashi_uf_kotei10_cd2 character varying(20) not null
  , kashi_kazei_kbn2 character varying not null
  , kashi_futan_bumon_cd3 character varying not null
  , kashi_torihikisaki_cd3 character varying not null
  , kashi_kamoku_cd3 character varying not null
  , kashi_kamoku_edaban_cd3 character varying not null
  , kashi_project_cd3 character varying not null
  , kashi_segment_cd3 character varying(8) not null
  , kashi_uf1_cd3 character varying(20) not null
  , kashi_uf2_cd3 character varying(20) not null
  , kashi_uf3_cd3 character varying(20) not null
  , kashi_uf4_cd3 character varying(20) not null
  , kashi_uf5_cd3 character varying(20) not null
  , kashi_uf6_cd3 character varying(20) not null
  , kashi_uf7_cd3 character varying(20) not null
  , kashi_uf8_cd3 character varying(20) not null
  , kashi_uf9_cd3 character varying(20) not null
  , kashi_uf10_cd3 character varying(20) not null
  , kashi_uf_kotei1_cd3 character varying(20) not null
  , kashi_uf_kotei2_cd3 character varying(20) not null
  , kashi_uf_kotei3_cd3 character varying(20) not null
  , kashi_uf_kotei4_cd3 character varying(20) not null
  , kashi_uf_kotei5_cd3 character varying(20) not null
  , kashi_uf_kotei6_cd3 character varying(20) not null
  , kashi_uf_kotei7_cd3 character varying(20) not null
  , kashi_uf_kotei8_cd3 character varying(20) not null
  , kashi_uf_kotei9_cd3 character varying(20) not null
  , kashi_uf_kotei10_cd3 character varying(20) not null
  , kashi_kazei_kbn3 character varying not null
  , kashi_futan_bumon_cd4 character varying not null
  , kashi_torihikisaki_cd4 character varying not null
  , kashi_kamoku_cd4 character varying not null
  , kashi_kamoku_edaban_cd4 character varying not null
  , kashi_project_cd4 character varying not null
  , kashi_segment_cd4 character varying(8) not null
  , kashi_uf1_cd4 character varying(20) not null
  , kashi_uf2_cd4 character varying(20) not null
  , kashi_uf3_cd4 character varying(20) not null
  , kashi_uf4_cd4 character varying(20) not null
  , kashi_uf5_cd4 character varying(20) not null
  , kashi_uf6_cd4 character varying(20) not null
  , kashi_uf7_cd4 character varying(20) not null
  , kashi_uf8_cd4 character varying(20) not null
  , kashi_uf9_cd4 character varying(20) not null
  , kashi_uf10_cd4 character varying(20) not null
  , kashi_uf_kotei1_cd4 character varying(20) not null
  , kashi_uf_kotei2_cd4 character varying(20) not null
  , kashi_uf_kotei3_cd4 character varying(20) not null
  , kashi_uf_kotei4_cd4 character varying(20) not null
  , kashi_uf_kotei5_cd4 character varying(20) not null
  , kashi_uf_kotei6_cd4 character varying(20) not null
  , kashi_uf_kotei7_cd4 character varying(20) not null
  , kashi_uf_kotei8_cd4 character varying(20) not null
  , kashi_uf_kotei9_cd4 character varying(20) not null
  , kashi_uf_kotei10_cd4 character varying(20) not null
  , kashi_kazei_kbn4 character varying not null
  , kashi_futan_bumon_cd5 character varying not null
  , kashi_torihikisaki_cd5 character varying not null
  , kashi_kamoku_cd5 character varying not null
  , kashi_kamoku_edaban_cd5 character varying not null
  , kashi_project_cd5 character varying not null
  , kashi_segment_cd5 character varying(8) not null
  , kashi_uf1_cd5 character varying(20) not null
  , kashi_uf2_cd5 character varying(20) not null
  , kashi_uf3_cd5 character varying(20) not null
  , kashi_uf4_cd5 character varying(20) not null
  , kashi_uf5_cd5 character varying(20) not null
  , kashi_uf6_cd5 character varying(20) not null
  , kashi_uf7_cd5 character varying(20) not null
  , kashi_uf8_cd5 character varying(20) not null
  , kashi_uf9_cd5 character varying(20) not null
  , kashi_uf10_cd5 character varying(20) not null
  , kashi_uf_kotei1_cd5 character varying(20) not null
  , kashi_uf_kotei2_cd5 character varying(20) not null
  , kashi_uf_kotei3_cd5 character varying(20) not null
  , kashi_uf_kotei4_cd5 character varying(20) not null
  , kashi_uf_kotei5_cd5 character varying(20) not null
  , kashi_uf_kotei6_cd5 character varying(20) not null
  , kashi_uf_kotei7_cd5 character varying(20) not null
  , kashi_uf_kotei8_cd5 character varying(20) not null
  , kashi_uf_kotei9_cd5 character varying(20) not null
  , kashi_uf_kotei10_cd5 character varying(20) not null
  , kashi_kazei_kbn5 character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_kbn,shiwake_edano)
);

comment on table shiwake_pattern_master is '�d��p�^�[���}�X�^�[';
comment on column shiwake_pattern_master.denpyou_kbn is '�`�[�敪';
comment on column shiwake_pattern_master.shiwake_edano is '�d��}�ԍ�';
comment on column shiwake_pattern_master.delete_flg is '�폜�t���O';
comment on column shiwake_pattern_master.yuukou_kigen_from is '�L�������J�n��';
comment on column shiwake_pattern_master.yuukou_kigen_to is '�L�������I����';
comment on column shiwake_pattern_master.bunrui1 is '����1';
comment on column shiwake_pattern_master.bunrui2 is '����2';
comment on column shiwake_pattern_master.bunrui3 is '����3';
comment on column shiwake_pattern_master.torihiki_name is '�����';
comment on column shiwake_pattern_master.tekiyou_flg is '�E�v�t���O';
comment on column shiwake_pattern_master.tekiyou is '�E�v';
comment on column shiwake_pattern_master.default_hyouji_flg is '�f�t�H���g�\���t���O';
comment on column shiwake_pattern_master.kousaihi_hyouji_flg is '���۔�\���t���O';
comment on column shiwake_pattern_master.kake_flg is '�|���t���O';
comment on column shiwake_pattern_master.hyouji_jun is '�\����';
comment on column shiwake_pattern_master.shain_cd_renkei_flg is '�Ј��R�[�h�A�g�t���O';
comment on column shiwake_pattern_master.edaban_renkei_flg is '�����}�ԃR�[�h�A�g�t���O';
comment on column shiwake_pattern_master.kari_futan_bumon_cd is '�ؕ����S����R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_kamoku_cd is '�ؕ��ȖڃR�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_torihikisaki_cd is '�ؕ������R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_project_cd is '�ؕ��v���W�F�N�g�R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_segment_cd is '�ؕ��Z�O�����g�R�[�h';
comment on column shiwake_pattern_master.kari_uf1_cd is '�ؕ�UF1�R�[�h';
comment on column shiwake_pattern_master.kari_uf2_cd is '�ؕ�UF2�R�[�h';
comment on column shiwake_pattern_master.kari_uf3_cd is '�ؕ�UF3�R�[�h';
comment on column shiwake_pattern_master.kari_uf4_cd is '�ؕ�UF4�R�[�h';
comment on column shiwake_pattern_master.kari_uf5_cd is '�ؕ�UF5�R�[�h';
comment on column shiwake_pattern_master.kari_uf6_cd is '�ؕ�UF6�R�[�h';
comment on column shiwake_pattern_master.kari_uf7_cd is '�ؕ�UF7�R�[�h';
comment on column shiwake_pattern_master.kari_uf8_cd is '�ؕ�UF8�R�[�h';
comment on column shiwake_pattern_master.kari_uf9_cd is '�ؕ�UF9�R�[�h';
comment on column shiwake_pattern_master.kari_uf10_cd is '�ؕ�UF10�R�[�h';
comment on column shiwake_pattern_master.kari_uf_kotei1_cd is '�ؕ�UF1�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei2_cd is '�ؕ�UF2�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei3_cd is '�ؕ�UF3�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei4_cd is '�ؕ�UF4�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei5_cd is '�ؕ�UF5�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei6_cd is '�ؕ�UF6�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei7_cd is '�ؕ�UF7�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei8_cd is '�ؕ�UF8�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei9_cd is '�ؕ�UF9�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei10_cd is '�ؕ�UF10�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_kazei_kbn is '�ؕ��ېŋ敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_zeiritsu is '�ؕ�����ŗ��i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_keigen_zeiritsu_kbn is '�ؕ��y���ŗ��敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd1 is '�ݕ����S����R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_cd1 is '�ݕ��ȖڃR�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd1 is '�ݕ��Ȗڎ}�ԃR�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd1 is '�ݕ������R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_project_cd1 is '�ݕ��v���W�F�N�g�R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_segment_cd1 is '�ݕ��Z�O�����g�R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_uf1_cd1 is '�ݕ�UF1�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf2_cd1 is '�ݕ�UF2�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf3_cd1 is '�ݕ�UF3�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf4_cd1 is '�ݕ�UF4�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf5_cd1 is '�ݕ�UF5�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf6_cd1 is '�ݕ�UF6�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf7_cd1 is '�ݕ�UF7�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf8_cd1 is '�ݕ�UF8�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf9_cd1 is '�ݕ�UF9�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf10_cd1 is '�ݕ�UF10�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd1 is '�ݕ�UF1�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd1 is '�ݕ�UF2�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd1 is '�ݕ�UF3�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd1 is '�ݕ�UF4�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd1 is '�ݕ�UF5�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd1 is '�ݕ�UF6�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd1 is '�ݕ�UF7�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd1 is '�ݕ�UF8�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd1 is '�ݕ�UF9�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd1 is '�ݕ�UF10�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_kazei_kbn1 is '�ݕ��ېŋ敪�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd2 is '�ݕ����S����R�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd2 is '�ݕ������R�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_cd2 is '�ݕ��ȖڃR�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd2 is '�ݕ��Ȗڎ}�ԃR�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_project_cd2 is '�ݕ��v���W�F�N�g�R�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_segment_cd2 is '�ݕ��Z�O�����g�R�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_uf1_cd2 is '�ݕ�UF1�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf2_cd2 is '�ݕ�UF2�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf3_cd2 is '�ݕ�UF3�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf4_cd2 is '�ݕ�UF4�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf5_cd2 is '�ݕ�UF5�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf6_cd2 is '�ݕ�UF6�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf7_cd2 is '�ݕ�UF7�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf8_cd2 is '�ݕ�UF8�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf9_cd2 is '�ݕ�UF9�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf10_cd2 is '�ݕ�UF10�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd2 is '�ݕ�UF1�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd2 is '�ݕ�UF2�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd2 is '�ݕ�UF3�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd2 is '�ݕ�UF4�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd2 is '�ݕ�UF5�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd2 is '�ݕ�UF6�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd2 is '�ݕ�UF7�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd2 is '�ݕ�UF8�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd2 is '�ݕ�UF9�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd2 is '�ݕ�UF10�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_kazei_kbn2 is '�ݕ��ېŋ敪�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd3 is '�ݕ����S����R�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd3 is '�ݕ������R�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_cd3 is '�ݕ��ȖڃR�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd3 is '�ݕ��Ȗڎ}�ԃR�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_project_cd3 is '�ݕ��v���W�F�N�g�R�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_segment_cd3 is '�ݕ��Z�O�����g�R�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_uf1_cd3 is '�ݕ�UF1�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf2_cd3 is '�ݕ�UF2�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf3_cd3 is '�ݕ�UF3�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf4_cd3 is '�ݕ�UF4�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf5_cd3 is '�ݕ�UF5�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf6_cd3 is '�ݕ�UF6�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf7_cd3 is '�ݕ�UF7�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf8_cd3 is '�ݕ�UF8�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf9_cd3 is '�ݕ�UF9�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf10_cd3 is '�ݕ�UF10�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd3 is '�ݕ�UF1�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd3 is '�ݕ�UF2�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd3 is '�ݕ�UF3�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd3 is '�ݕ�UF4�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd3 is '�ݕ�UF5�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd3 is '�ݕ�UF6�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd3 is '�ݕ�UF7�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd3 is '�ݕ�UF8�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd3 is '�ݕ�UF9�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd3 is '�ݕ�UF10�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_kazei_kbn3 is '�ݕ��ېŋ敪�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd4 is '�ݕ����S����R�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd4 is '�ݕ������R�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_cd4 is '�ݕ��ȖڃR�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd4 is '�ݕ��Ȗڎ}�ԃR�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_project_cd4 is '�ݕ��v���W�F�N�g�R�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_segment_cd4 is '�ݕ��Z�O�����g�R�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_uf1_cd4 is '�ݕ�UF1�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf2_cd4 is '�ݕ�UF2�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf3_cd4 is '�ݕ�UF3�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf4_cd4 is '�ݕ�UF4�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf5_cd4 is '�ݕ�UF5�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf6_cd4 is '�ݕ�UF6�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf7_cd4 is '�ݕ�UF7�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf8_cd4 is '�ݕ�UF8�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf9_cd4 is '�ݕ�UF9�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf10_cd4 is '�ݕ�UF10�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd4 is '�ݕ�UF1�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd4 is '�ݕ�UF2�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd4 is '�ݕ�UF3�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd4 is '�ݕ�UF4�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd4 is '�ݕ�UF5�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd4 is '�ݕ�UF6�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd4 is '�ݕ�UF7�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd4 is '�ݕ�UF8�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd4 is '�ݕ�UF9�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd4 is '�ݕ�UF10�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_kazei_kbn4 is '�ݕ��ېŋ敪�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd5 is '�ݕ����S����R�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd5 is '�ݕ������R�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_cd5 is '�ݕ��ȖڃR�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd5 is '�ݕ��Ȗڎ}�ԃR�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_project_cd5 is '�ݕ��v���W�F�N�g�R�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_segment_cd5 is '�ݕ��Z�O�����g�R�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_uf1_cd5 is '�ݕ�UF1�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf2_cd5 is '�ݕ�UF2�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf3_cd5 is '�ݕ�UF3�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf4_cd5 is '�ݕ�UF4�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf5_cd5 is '�ݕ�UF5�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf6_cd5 is '�ݕ�UF6�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf7_cd5 is '�ݕ�UF7�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf8_cd5 is '�ݕ�UF8�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf9_cd5 is '�ݕ�UF9�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf10_cd5 is '�ݕ�UF10�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd5 is '�ݕ�UF1�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd5 is '�ݕ�UF2�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd5 is '�ݕ�UF3�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd5 is '�ݕ�UF4�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd5 is '�ݕ�UF5�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd5 is '�ݕ�UF6�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd5 is '�ݕ�UF7�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd5 is '�ݕ�UF8�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd5 is '�ݕ�UF9�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd5 is '�ݕ�UF10�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_kazei_kbn5 is '�ݕ��ېŋ敪�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.touroku_user_id is '�o�^���[�U�[ID';
comment on column shiwake_pattern_master.touroku_time is '�o�^����';
comment on column shiwake_pattern_master.koushin_user_id is '�X�V���[�U�[ID';
comment on column shiwake_pattern_master.koushin_time is '�X�V����';
INSERT INTO shiwake_pattern_master
SELECT
    denpyou_kbn
   ,shiwake_edano
   ,delete_flg
   ,yuukou_kigen_from
   ,yuukou_kigen_to
   ,bunrui1
   ,bunrui2
   ,bunrui3
   ,torihiki_name
   ,tekiyou_flg
   ,tekiyou
   ,default_hyouji_flg
   ,kousaihi_hyouji_flg
   ,kake_flg
   ,hyouji_jun
   ,shain_cd_renkei_flg
   ,'' -- edaban_renkei_flg
   ,kari_futan_bumon_cd
   ,kari_kamoku_cd
   ,kari_kamoku_edaban_cd
   ,kari_torihikisaki_cd
   ,kari_project_cd
   ,kari_segment_cd
   ,kari_uf1_cd
   ,kari_uf2_cd
   ,kari_uf3_cd
   ,kari_uf4_cd
   ,kari_uf5_cd
   ,kari_uf6_cd
   ,kari_uf7_cd
   ,kari_uf8_cd
   ,kari_uf9_cd
   ,kari_uf10_cd
   ,kari_uf_kotei1_cd
   ,kari_uf_kotei2_cd
   ,kari_uf_kotei3_cd
   ,kari_uf_kotei4_cd
   ,kari_uf_kotei5_cd
   ,kari_uf_kotei6_cd
   ,kari_uf_kotei7_cd
   ,kari_uf_kotei8_cd
   ,kari_uf_kotei9_cd
   ,kari_uf_kotei10_cd
   ,kari_kazei_kbn
   ,kari_zeiritsu
   ,kari_keigen_zeiritsu_kbn
   ,kashi_futan_bumon_cd1
   ,kashi_kamoku_cd1
   ,kashi_kamoku_edaban_cd1
   ,kashi_torihikisaki_cd1
   ,kashi_project_cd1
   ,kashi_segment_cd1
   ,kashi_uf1_cd1
   ,kashi_uf2_cd1
   ,kashi_uf3_cd1
   ,kashi_uf4_cd1
   ,kashi_uf5_cd1
   ,kashi_uf6_cd1
   ,kashi_uf7_cd1
   ,kashi_uf8_cd1
   ,kashi_uf9_cd1
   ,kashi_uf10_cd1
   ,kashi_uf_kotei1_cd1
   ,kashi_uf_kotei2_cd1
   ,kashi_uf_kotei3_cd1
   ,kashi_uf_kotei4_cd1
   ,kashi_uf_kotei5_cd1
   ,kashi_uf_kotei6_cd1
   ,kashi_uf_kotei7_cd1
   ,kashi_uf_kotei8_cd1
   ,kashi_uf_kotei9_cd1
   ,kashi_uf_kotei10_cd1
   ,kashi_kazei_kbn1
   ,kashi_futan_bumon_cd2
   ,kashi_torihikisaki_cd2
   ,kashi_kamoku_cd2
   ,kashi_kamoku_edaban_cd2
   ,kashi_project_cd2
   ,kashi_segment_cd2
   ,kashi_uf1_cd2
   ,kashi_uf2_cd2
   ,kashi_uf3_cd2
   ,kashi_uf4_cd2
   ,kashi_uf5_cd2
   ,kashi_uf6_cd2
   ,kashi_uf7_cd2
   ,kashi_uf8_cd2
   ,kashi_uf9_cd2
   ,kashi_uf10_cd2
   ,kashi_uf_kotei1_cd2
   ,kashi_uf_kotei2_cd2
   ,kashi_uf_kotei3_cd2
   ,kashi_uf_kotei4_cd2
   ,kashi_uf_kotei5_cd2
   ,kashi_uf_kotei6_cd2
   ,kashi_uf_kotei7_cd2
   ,kashi_uf_kotei8_cd2
   ,kashi_uf_kotei9_cd2
   ,kashi_uf_kotei10_cd2
   ,kashi_kazei_kbn2
   ,kashi_futan_bumon_cd3
   ,kashi_torihikisaki_cd3
   ,kashi_kamoku_cd3
   ,kashi_kamoku_edaban_cd3
   ,kashi_project_cd3
   ,kashi_segment_cd3
   ,kashi_uf1_cd3
   ,kashi_uf2_cd3
   ,kashi_uf3_cd3
   ,kashi_uf4_cd3
   ,kashi_uf5_cd3
   ,kashi_uf6_cd3
   ,kashi_uf7_cd3
   ,kashi_uf8_cd3
   ,kashi_uf9_cd3
   ,kashi_uf10_cd3
   ,kashi_uf_kotei1_cd3
   ,kashi_uf_kotei2_cd3
   ,kashi_uf_kotei3_cd3
   ,kashi_uf_kotei4_cd3
   ,kashi_uf_kotei5_cd3
   ,kashi_uf_kotei6_cd3
   ,kashi_uf_kotei7_cd3
   ,kashi_uf_kotei8_cd3
   ,kashi_uf_kotei9_cd3
   ,kashi_uf_kotei10_cd3
   ,kashi_kazei_kbn3
   ,kashi_futan_bumon_cd4
   ,kashi_torihikisaki_cd4
   ,kashi_kamoku_cd4
   ,kashi_kamoku_edaban_cd4
   ,kashi_project_cd4
   ,kashi_segment_cd4
   ,kashi_uf1_cd4
   ,kashi_uf2_cd4
   ,kashi_uf3_cd4
   ,kashi_uf4_cd4
   ,kashi_uf5_cd4
   ,kashi_uf6_cd4
   ,kashi_uf7_cd4
   ,kashi_uf8_cd4
   ,kashi_uf9_cd4
   ,kashi_uf10_cd4
   ,kashi_uf_kotei1_cd4
   ,kashi_uf_kotei2_cd4
   ,kashi_uf_kotei3_cd4
   ,kashi_uf_kotei4_cd4
   ,kashi_uf_kotei5_cd4
   ,kashi_uf_kotei6_cd4
   ,kashi_uf_kotei7_cd4
   ,kashi_uf_kotei8_cd4
   ,kashi_uf_kotei9_cd4
   ,kashi_uf_kotei10_cd4
   ,kashi_kazei_kbn4
   ,kashi_futan_bumon_cd5
   ,kashi_torihikisaki_cd5
   ,kashi_kamoku_cd5
   ,kashi_kamoku_edaban_cd5
   ,kashi_project_cd5
   ,kashi_segment_cd5
   ,kashi_uf1_cd5
   ,kashi_uf2_cd5
   ,kashi_uf3_cd5
   ,kashi_uf4_cd5
   ,kashi_uf5_cd5
   ,kashi_uf6_cd5
   ,kashi_uf7_cd5
   ,kashi_uf8_cd5
   ,kashi_uf9_cd5
   ,kashi_uf10_cd5
   ,kashi_uf_kotei1_cd5
   ,kashi_uf_kotei2_cd5
   ,kashi_uf_kotei3_cd5
   ,kashi_uf_kotei4_cd5
   ,kashi_uf_kotei5_cd5
   ,kashi_uf_kotei6_cd5
   ,kashi_uf_kotei7_cd5
   ,kashi_uf_kotei8_cd5
   ,kashi_uf_kotei9_cd5
   ,kashi_uf_kotei10_cd5
   ,kashi_kazei_kbn5
   ,touroku_user_id
   ,touroku_time
   ,koushin_user_id
   ,koushin_time
FROM shiwake_pattern_master_old;
UPDATE shiwake_pattern_master SET edaban_renkei_flg = '0' WHERE denpyou_kbn = 'A004' OR denpyou_kbn = 'A010' OR denpyou_kbn = 'A011';
DROP TABLE shiwake_pattern_master_old;

--�A���[_0715 �����}�ԃR�[�h�A�g�Ή� ��ʎ�i�}�X�^�e�[�u���X�V
ALTER TABLE koutsuu_shudan_master RENAME TO koutsuu_shudan_master_old;
create table koutsuu_shudan_master (
  sort_jun character(3) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , primary key (sort_jun,koutsuu_shudan)
);
comment on table koutsuu_shudan_master is '�����p��ʎ�i�}�X�^�[';
comment on column koutsuu_shudan_master.sort_jun is '���я�';
comment on column koutsuu_shudan_master.koutsuu_shudan is '��ʎ�i';
comment on column koutsuu_shudan_master.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column koutsuu_shudan_master.zei_kubun is '�ŋ敪';
comment on column koutsuu_shudan_master.edaban is '�}�ԃR�[�h';
INSERT INTO koutsuu_shudan_master
SELECT
  sort_jun
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , zei_kubun
  ,'' -- edaban
FROM koutsuu_shudan_master_old;
DROP TABLE koutsuu_shudan_master_old;

--�A���[_0715 �����}�ԃR�[�h�A�g�Ή� �C�O��ʎ�i�}�X�^�e�[�u���X�V
ALTER TABLE kaigai_koutsuu_shudan_master RENAME TO kaigai_koutsuu_shudan_master_old;
create table kaigai_koutsuu_shudan_master (
  sort_jun character(3) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , primary key (sort_jun,koutsuu_shudan)
);
comment on table kaigai_koutsuu_shudan_master is '�C�O�p��ʎ�i�}�X�^�[';
comment on column kaigai_koutsuu_shudan_master.sort_jun is '���я�';
comment on column kaigai_koutsuu_shudan_master.koutsuu_shudan is '��ʎ�i';
comment on column kaigai_koutsuu_shudan_master.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column kaigai_koutsuu_shudan_master.zei_kubun is '�ŋ敪';
comment on column kaigai_koutsuu_shudan_master.edaban is '�}�ԃR�[�h';
INSERT INTO kaigai_koutsuu_shudan_master
SELECT
  sort_jun
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , zei_kubun
  ,'' -- edaban
FROM kaigai_koutsuu_shudan_master_old;
DROP TABLE kaigai_koutsuu_shudan_master_old;

--�A���[_0715 �����}�ԃR�[�h�A�g�Ή� �������}�X�^�e�[�u���X�V
ALTER TABLE nittou_nado_master RENAME TO nittou_nado_master_old;
create table nittou_nado_master (
  shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , yakushoku_cd character varying(10) not null
  , tanka numeric(15)
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , nittou_shukuhakuhi_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , primary key (shubetsu1,shubetsu2,yakushoku_cd)
);
comment on table nittou_nado_master is '�����p�������}�X�^�[';
comment on column nittou_nado_master.shubetsu1 is '��ʂP';
comment on column nittou_nado_master.shubetsu2 is '��ʂQ';
comment on column nittou_nado_master.yakushoku_cd is '��E�R�[�h';
comment on column nittou_nado_master.tanka is '�P��';
comment on column nittou_nado_master.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column nittou_nado_master.nittou_shukuhakuhi_flg is '�����E�h����t���O';
comment on column nittou_nado_master.zei_kubun is '�ŋ敪';
comment on column nittou_nado_master.edaban is '�}�ԃR�[�h';
INSERT INTO nittou_nado_master
SELECT
  shubetsu1
  , shubetsu2
  , yakushoku_cd
  , tanka
  , shouhyou_shorui_hissu_flg
  , nittou_shukuhakuhi_flg
  , zei_kubun
  ,'' -- edaban
FROM nittou_nado_master_old;
DROP TABLE nittou_nado_master_old;

--�A���[_0715 �����}�ԃR�[�h�A�g�Ή� �C�O�������}�X�^�e�[�u���X�V
ALTER TABLE kaigai_nittou_nado_master RENAME TO kaigai_nittou_nado_master_old;
create table kaigai_nittou_nado_master (
  shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , yakushoku_cd character varying(10) not null
  , tanka numeric(15)
  , heishu_cd character varying(4) not null
  , currency_unit character varying(20) not null
  , tanka_gaika numeric(19,2)
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , nittou_shukuhakuhi_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , primary key (shubetsu1,shubetsu2,yakushoku_cd)
);
comment on table kaigai_nittou_nado_master is '�C�O�p�������}�X�^�[';
comment on column kaigai_nittou_nado_master.shubetsu1 is '��ʂP';
comment on column kaigai_nittou_nado_master.shubetsu2 is '��ʂQ';
comment on column kaigai_nittou_nado_master.yakushoku_cd is '��E�R�[�h';
comment on column kaigai_nittou_nado_master.tanka is '�P���i�M�݁j';
comment on column kaigai_nittou_nado_master.heishu_cd is '����R�[�h';
comment on column kaigai_nittou_nado_master.currency_unit is '�ʉݒP��';
comment on column kaigai_nittou_nado_master.tanka_gaika is '�P���i�O�݁j';
comment on column kaigai_nittou_nado_master.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column kaigai_nittou_nado_master.nittou_shukuhakuhi_flg is '�����E�h����t���O';
comment on column kaigai_nittou_nado_master.zei_kubun is '�ŋ敪';
comment on column kaigai_nittou_nado_master.edaban is '�}�ԃR�[�h';
INSERT INTO kaigai_nittou_nado_master
SELECT
  shubetsu1
  , shubetsu2
  , yakushoku_cd
  , tanka
  , heishu_cd
  , currency_unit
  , tanka_gaika
  , shouhyou_shorui_hissu_flg
  , nittou_shukuhakuhi_flg
  , zei_kubun
  ,'' -- edaban
FROM kaigai_nittou_nado_master_old;
DROP TABLE kaigai_nittou_nado_master_old;


-- �A���[_0715 �����}�ԃR�[�h�A�g�Ή�  �}�X�^�[�Ǘ��Ő��i����(�C�O)��ʔ�E�������}�X�^�[�j
UPDATE master_kanri_hansuu SET delete_flg = '1' WHERE master_id = 'nittou_nado_master' OR master_id = 'kaigai_nittou_nado_master' OR master_id = 'koutsuu_shudan_master' OR master_id = 'kaigai_koutsuu_shudan_master';

INSERT INTO master_kanri_hansuu 
(
  master_id
  , version
  , delete_flg
  , file_name
  , file_size
  , content_type
  , binary_data
  , touroku_user_id 
  , touroku_time 
  , koushin_user_id 
  , koushin_time 
) VALUES ( 
   'nittou_nado_master'		--�����������}�X�^�[
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='nittou_nado_master')
 , '0'
 , '����������_patch.csv'
 , length(convert_to(E'��ʂP,��ʂQ,��E�R�[�h,�P��,�؜ߏ��ޕK�{�t���O,�����E�h����t���O,�ŋ敪,�}�ԃR�[�h\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun,edaban\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(1),varchar(1),varchar(1),varchar(12)\r\n1,1,1,,2,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun || ',' || edaban FROM nittou_nado_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'��ʂP,��ʂQ,��E�R�[�h,�P��,�؜ߏ��ޕK�{�t���O,�����E�h����t���O,�ŋ敪,�}�ԃR�[�h\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun,edaban\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(1),varchar(1),varchar(1),varchar(12)\r\n1,1,1,,2,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun || ',' || edaban FROM nittou_nado_master), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
 );

INSERT INTO master_kanri_hansuu 
(
  master_id
  , version
  , delete_flg
  , file_name
  , file_size
  , content_type
  , binary_data
  , touroku_user_id 
  , touroku_time 
  , koushin_user_id 
  , koushin_time 
) VALUES ( 
   'kaigai_nittou_nado_master'		--�C�O�������}�X�^�[
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='kaigai_nittou_nado_master')
 , '0'
 , '�C�O������_patch.csv'
 , length(convert_to(E'��ʂP,��ʂQ,��E�R�[�h,�P���i�M�݁j,����R�[�h,�ʉݒP��,�P���i�O�݁j,�؜ߏ��ޕK�{�t���O,�����E�h����t���O,�ŋ敪,�}�ԃR�[�h\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,heishu_cd,currency_unit,tanka_gaika,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun,edaban\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(4),varchar(20),decimal,varchar(1),varchar(1),varchar(1),varchar(12)\r\n1,1,1,,2,2,,2,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || heishu_cd || ',' || currency_unit || ',' || COALESCE(tanka_gaika::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun || ',' || edaban FROM kaigai_nittou_nado_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'��ʂP,��ʂQ,��E�R�[�h,�P���i�M�݁j,����R�[�h,�ʉݒP��,�P���i�O�݁j,�؜ߏ��ޕK�{�t���O,�����E�h����t���O,�ŋ敪,�}�ԃR�[�h\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,heishu_cd,currency_unit,tanka_gaika,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun,edaban\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(4),varchar(20),decimal,varchar(1),varchar(1),varchar(1),varchar(12)\r\n1,1,1,,2,2,,2,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || heishu_cd || ',' || currency_unit || ',' || COALESCE(tanka_gaika::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun || ',' || edaban FROM kaigai_nittou_nado_master), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
 );

INSERT INTO master_kanri_hansuu 
(
  master_id
  , version
  , delete_flg
  , file_name
  , file_size
  , content_type
  , binary_data
  , touroku_user_id 
  , touroku_time 
  , koushin_user_id 
  , koushin_time 
) VALUES ( 
   'koutsuu_shudan_master'		--������ʎ�i�}�X�^�[
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='koutsuu_shudan_master')
 , '0'
 , '������ʎ�i_patch.csv'
 , length(convert_to(E'���я�,��ʎ�i,�؜ߏ��ޕK�{�t���O,�ŋ敪,�}�ԃR�[�h\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun,edaban\r\nvarchar(3),varchar(10),varchar(1),varchar(1),varchar(12)\r\n1,1,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun || ',' || edaban FROM koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'���я�,��ʎ�i,�؜ߏ��ޕK�{�t���O,�ŋ敪,�}�ԃR�[�h\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun,edaban\r\nvarchar(3),varchar(10),varchar(1),varchar(1),varchar(12)\r\n1,1,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun || ',' || edaban FROM koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
 );

INSERT INTO master_kanri_hansuu 
(
  master_id
  , version
  , delete_flg
  , file_name
  , file_size
  , content_type
  , binary_data
  , touroku_user_id 
  , touroku_time 
  , koushin_user_id 
  , koushin_time 
) VALUES ( 
   'kaigai_koutsuu_shudan_master'		--�C�O��ʎ�i�}�X�^�[
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='kaigai_koutsuu_shudan_master')
 , '0'
 , '�C�O��ʎ�i_patch.csv'
 , length(convert_to(E'���я�,��ʎ�i,�؜ߏ��ޕK�{�t���O,�ŋ敪,�}�ԃR�[�h\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun,edaban\r\nvarchar(3),varchar(10),varchar(1),varchar(1),varchar(12)\r\n1,1,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun || ',' || edaban FROM kaigai_koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'���я�,��ʎ�i,�؜ߏ��ޕK�{�t���O,�ŋ敪,�}�ԃR�[�h\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun,edaban\r\nvarchar(3),varchar(10),varchar(1),varchar(1),varchar(12)\r\n1,1,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun || ',' || edaban FROM kaigai_koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
 );

-- 0710_�����������\���Ǝx���˗��\���̎x�����ꊇ�o�^�Ή� �ݒ��`�ǉ� ��hyouji_jun900�ȍ~�̓J�X�^�}�C�Y�̈�
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

commit;