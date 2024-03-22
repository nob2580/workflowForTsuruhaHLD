SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- ICSP�A���[_0998_���۔�l����������z�̕s�g�p�ݒ�ǉ�
ALTER TABLE shiwake_pattern_master DROP CONSTRAINT IF EXISTS shiwake_pattern_master_PKEY;
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
  , kousaihi_ninzuu_riyou_flg character varying(1) not null
  , kousaihi_kijun_gaku numeric(6)
  , kousaihi_check_houhou character varying(1) not null
  , kousaihi_check_result character varying(1) not null
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
  , constraint shiwake_pattern_master_PKEY primary key (denpyou_kbn,shiwake_edano)
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
comment on column shiwake_pattern_master.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column shiwake_pattern_master.kousaihi_kijun_gaku is '���۔��z';
comment on column shiwake_pattern_master.kousaihi_check_houhou is '���۔�`�F�b�N���@';
comment on column shiwake_pattern_master.kousaihi_check_result is '���۔�`�F�b�N��o�^����';
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
  , kousaihi_hyouji_flg -- kousaihi_ninzuu_riyou_flg
  , kousaihi_kijun_gaku
  , kousaihi_check_houhou
  , kousaihi_check_result
  , kake_flg
  , hyouji_jun
  , shain_cd_renkei_flg
  , edaban_renkei_flg
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
  , kari_zeiritsu
  , kari_keigen_zeiritsu_kbn
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
FROM shiwake_pattern_master_old;
DROP TABLE shiwake_pattern_master_old;

ALTER TABLE keihiseisan_meisai DROP CONSTRAINT IF EXISTS keihiseisan_meisai_PKEY;
ALTER TABLE keihiseisan_meisai RENAME TO keihiseisan_meisai_old;
create table keihiseisan_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , user_id character varying(30) not null
  , shain_no character varying(15) not null
  , user_sei character varying(10) not null
  , user_mei character varying(10) not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , houjin_card_riyou_flg character varying(1) not null
  , kaisha_tehai_flg character varying(1) not null
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_ninzuu_riyou_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , himoduke_card_meisai bigint
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint keihiseisan_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table keihiseisan_meisai is '�o��Z����';
comment on column keihiseisan_meisai.denpyou_id is '�`�[ID';
comment on column keihiseisan_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column keihiseisan_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column keihiseisan_meisai.user_id is '���[�U�[ID';
comment on column keihiseisan_meisai.shain_no is '�Ј��ԍ�';
comment on column keihiseisan_meisai.user_sei is '���[�U�[��';
comment on column keihiseisan_meisai.user_mei is '���[�U�[��';
comment on column keihiseisan_meisai.shiyoubi is '�g�p��';
comment on column keihiseisan_meisai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column keihiseisan_meisai.torihiki_name is '�����';
comment on column keihiseisan_meisai.tekiyou is '�E�v';
comment on column keihiseisan_meisai.zeiritsu is '�ŗ�';
comment on column keihiseisan_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column keihiseisan_meisai.shiharai_kingaku is '�x�����z';
comment on column keihiseisan_meisai.hontai_kingaku is '�{�̋��z';
comment on column keihiseisan_meisai.shouhizeigaku is '����Ŋz';
comment on column keihiseisan_meisai.houjin_card_riyou_flg is '�@�l�J�[�h���p�t���O';
comment on column keihiseisan_meisai.kaisha_tehai_flg is '��Ў�z�t���O';
comment on column keihiseisan_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column keihiseisan_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column keihiseisan_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column keihiseisan_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column keihiseisan_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
comment on column keihiseisan_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column keihiseisan_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column keihiseisan_meisai.torihikisaki_cd is '�����R�[�h';
comment on column keihiseisan_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column keihiseisan_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column keihiseisan_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column keihiseisan_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column keihiseisan_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column keihiseisan_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column keihiseisan_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column keihiseisan_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column keihiseisan_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column keihiseisan_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column keihiseisan_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column keihiseisan_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column keihiseisan_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column keihiseisan_meisai.uf1_cd is 'UF1�R�[�h';
comment on column keihiseisan_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column keihiseisan_meisai.uf2_cd is 'UF2�R�[�h';
comment on column keihiseisan_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column keihiseisan_meisai.uf3_cd is 'UF3�R�[�h';
comment on column keihiseisan_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column keihiseisan_meisai.uf4_cd is 'UF4�R�[�h';
comment on column keihiseisan_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column keihiseisan_meisai.uf5_cd is 'UF5�R�[�h';
comment on column keihiseisan_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column keihiseisan_meisai.uf6_cd is 'UF6�R�[�h';
comment on column keihiseisan_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column keihiseisan_meisai.uf7_cd is 'UF7�R�[�h';
comment on column keihiseisan_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column keihiseisan_meisai.uf8_cd is 'UF8�R�[�h';
comment on column keihiseisan_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column keihiseisan_meisai.uf9_cd is 'UF9�R�[�h';
comment on column keihiseisan_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column keihiseisan_meisai.uf10_cd is 'UF10�R�[�h';
comment on column keihiseisan_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column keihiseisan_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column keihiseisan_meisai.project_name is '�v���W�F�N�g��';
comment on column keihiseisan_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column keihiseisan_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column keihiseisan_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column keihiseisan_meisai.himoduke_card_meisai is '�R�t���J�[�h����';
comment on column keihiseisan_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column keihiseisan_meisai.touroku_time is '�o�^����';
comment on column keihiseisan_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column keihiseisan_meisai.koushin_time is '�X�V����';
INSERT INTO keihiseisan_meisai
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
  , keigen_zeiritsu_kbn
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai_hyouji_flg -- kousaihi_ninzuu_riyou_flg
  , kousaihi_shousai
  , kousaihi_ninzuu
  , kousaihi_hitori_kingaku
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
FROM keihiseisan_meisai_old;
DROP TABLE keihiseisan_meisai_old;

ALTER TABLE seikyuushobarai_meisai DROP CONSTRAINT IF EXISTS seikyuushobarai_meisai_PKEY;
ALTER TABLE seikyuushobarai_meisai RENAME TO seikyuushobarai_meisai_old;
create table seikyuushobarai_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_ninzuu_riyou_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , furikomisaki_jouhou character varying(240) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint seikyuushobarai_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table seikyuushobarai_meisai is '��������������';
comment on column seikyuushobarai_meisai.denpyou_id is '�`�[ID';
comment on column seikyuushobarai_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column seikyuushobarai_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column seikyuushobarai_meisai.torihiki_name is '�����';
comment on column seikyuushobarai_meisai.tekiyou is '�E�v';
comment on column seikyuushobarai_meisai.zeiritsu is '�ŗ�';
comment on column seikyuushobarai_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column seikyuushobarai_meisai.shiharai_kingaku is '�x�����z';
comment on column seikyuushobarai_meisai.hontai_kingaku is '�{�̋��z';
comment on column seikyuushobarai_meisai.shouhizeigaku is '����Ŋz';
comment on column seikyuushobarai_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column seikyuushobarai_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column seikyuushobarai_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column seikyuushobarai_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column seikyuushobarai_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
comment on column seikyuushobarai_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column seikyuushobarai_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column seikyuushobarai_meisai.torihikisaki_cd is '�����R�[�h';
comment on column seikyuushobarai_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column seikyuushobarai_meisai.furikomisaki_jouhou is '�U������';
comment on column seikyuushobarai_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column seikyuushobarai_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column seikyuushobarai_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column seikyuushobarai_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column seikyuushobarai_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column seikyuushobarai_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column seikyuushobarai_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column seikyuushobarai_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column seikyuushobarai_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column seikyuushobarai_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column seikyuushobarai_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column seikyuushobarai_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column seikyuushobarai_meisai.uf1_cd is 'UF1�R�[�h';
comment on column seikyuushobarai_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column seikyuushobarai_meisai.uf2_cd is 'UF2�R�[�h';
comment on column seikyuushobarai_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column seikyuushobarai_meisai.uf3_cd is 'UF3�R�[�h';
comment on column seikyuushobarai_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column seikyuushobarai_meisai.uf4_cd is 'UF4�R�[�h';
comment on column seikyuushobarai_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column seikyuushobarai_meisai.uf5_cd is 'UF5�R�[�h';
comment on column seikyuushobarai_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column seikyuushobarai_meisai.uf6_cd is 'UF6�R�[�h';
comment on column seikyuushobarai_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column seikyuushobarai_meisai.uf7_cd is 'UF7�R�[�h';
comment on column seikyuushobarai_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column seikyuushobarai_meisai.uf8_cd is 'UF8�R�[�h';
comment on column seikyuushobarai_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column seikyuushobarai_meisai.uf9_cd is 'UF9�R�[�h';
comment on column seikyuushobarai_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column seikyuushobarai_meisai.uf10_cd is 'UF10�R�[�h';
comment on column seikyuushobarai_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column seikyuushobarai_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column seikyuushobarai_meisai.project_name is '�v���W�F�N�g��';
comment on column seikyuushobarai_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column seikyuushobarai_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column seikyuushobarai_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column seikyuushobarai_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column seikyuushobarai_meisai.touroku_time is '�o�^����';
comment on column seikyuushobarai_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column seikyuushobarai_meisai.koushin_time is '�X�V����';
INSERT INTO seikyuushobarai_meisai
SELECT
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , torihiki_name
  , tekiyou
  , zeiritsu
  , keigen_zeiritsu_kbn
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai_hyouji_flg -- kousaihi_ninzuu_riyou_flg
  , kousaihi_shousai
  , kousaihi_ninzuu
  , kousaihi_hitori_kingaku
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
FROM seikyuushobarai_meisai_old;
DROP TABLE seikyuushobarai_meisai_old;

ALTER TABLE ryohi_karibarai_keihi_meisai DROP CONSTRAINT IF EXISTS ryohi_karibarai_keihi_meisai_PKEY;
ALTER TABLE ryohi_karibarai_keihi_meisai RENAME TO ryohi_karibarai_keihi_meisai_old;
create table ryohi_karibarai_keihi_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_ninzuu_riyou_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint ryohi_karibarai_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table ryohi_karibarai_keihi_meisai is '������o���';
comment on column ryohi_karibarai_keihi_meisai.denpyou_id is '�`�[ID';
comment on column ryohi_karibarai_keihi_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column ryohi_karibarai_keihi_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column ryohi_karibarai_keihi_meisai.shiyoubi is '�g�p��';
comment on column ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column ryohi_karibarai_keihi_meisai.torihiki_name is '�����';
comment on column ryohi_karibarai_keihi_meisai.tekiyou is '�E�v';
comment on column ryohi_karibarai_keihi_meisai.zeiritsu is '�ŗ�';
comment on column ryohi_karibarai_keihi_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column ryohi_karibarai_keihi_meisai.shiharai_kingaku is '�x�����z';
comment on column ryohi_karibarai_keihi_meisai.hontai_kingaku is '�{�̋��z';
comment on column ryohi_karibarai_keihi_meisai.shouhizeigaku is '����Ŋz';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
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
INSERT INTO ryohi_karibarai_keihi_meisai
SELECT
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , keigen_zeiritsu_kbn
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai_hyouji_flg -- kousaihi_ninzuu_riyou_flg
  , kousaihi_shousai
  , kousaihi_ninzuu
  , kousaihi_hitori_kingaku
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
FROM ryohi_karibarai_keihi_meisai_old;
DROP TABLE ryohi_karibarai_keihi_meisai_old;

ALTER TABLE ryohiseisan_keihi_meisai DROP CONSTRAINT IF EXISTS ryohiseisan_keihi_meisai_PKEY;
ALTER TABLE ryohiseisan_keihi_meisai RENAME TO ryohiseisan_keihi_meisai_old;
create table ryohiseisan_keihi_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , houjin_card_riyou_flg character varying(1) not null
  , kaisha_tehai_flg character varying(1) not null
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_ninzuu_riyou_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , himoduke_card_meisai bigint
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint ryohiseisan_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table ryohiseisan_keihi_meisai is '����Z�o���';
comment on column ryohiseisan_keihi_meisai.denpyou_id is '�`�[ID';
comment on column ryohiseisan_keihi_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column ryohiseisan_keihi_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column ryohiseisan_keihi_meisai.shiyoubi is '�g�p��';
comment on column ryohiseisan_keihi_meisai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column ryohiseisan_keihi_meisai.torihiki_name is '�����';
comment on column ryohiseisan_keihi_meisai.tekiyou is '�E�v';
comment on column ryohiseisan_keihi_meisai.zeiritsu is '�ŗ�';
comment on column ryohiseisan_keihi_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column ryohiseisan_keihi_meisai.shiharai_kingaku is '�x�����z';
comment on column ryohiseisan_keihi_meisai.hontai_kingaku is '�{�̋��z';
comment on column ryohiseisan_keihi_meisai.shouhizeigaku is '����Ŋz';
comment on column ryohiseisan_keihi_meisai.houjin_card_riyou_flg is '�@�l�J�[�h���p�t���O';
comment on column ryohiseisan_keihi_meisai.kaisha_tehai_flg is '��Ў�z�t���O';
comment on column ryohiseisan_keihi_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column ryohiseisan_keihi_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column ryohiseisan_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column ryohiseisan_keihi_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column ryohiseisan_keihi_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
comment on column ryohiseisan_keihi_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column ryohiseisan_keihi_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column ryohiseisan_keihi_meisai.torihikisaki_cd is '�����R�[�h';
comment on column ryohiseisan_keihi_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column ryohiseisan_keihi_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column ryohiseisan_keihi_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column ryohiseisan_keihi_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column ryohiseisan_keihi_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column ryohiseisan_keihi_meisai.uf1_cd is 'UF1�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column ryohiseisan_keihi_meisai.uf2_cd is 'UF2�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column ryohiseisan_keihi_meisai.uf3_cd is 'UF3�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column ryohiseisan_keihi_meisai.uf4_cd is 'UF4�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column ryohiseisan_keihi_meisai.uf5_cd is 'UF5�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column ryohiseisan_keihi_meisai.uf6_cd is 'UF6�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column ryohiseisan_keihi_meisai.uf7_cd is 'UF7�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column ryohiseisan_keihi_meisai.uf8_cd is 'UF8�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column ryohiseisan_keihi_meisai.uf9_cd is 'UF9�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column ryohiseisan_keihi_meisai.uf10_cd is 'UF10�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column ryohiseisan_keihi_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column ryohiseisan_keihi_meisai.project_name is '�v���W�F�N�g��';
comment on column ryohiseisan_keihi_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column ryohiseisan_keihi_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column ryohiseisan_keihi_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column ryohiseisan_keihi_meisai.himoduke_card_meisai is '�R�t���J�[�h����';
comment on column ryohiseisan_keihi_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column ryohiseisan_keihi_meisai.touroku_time is '�o�^����';
comment on column ryohiseisan_keihi_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column ryohiseisan_keihi_meisai.koushin_time is '�X�V����';
INSERT INTO ryohiseisan_keihi_meisai
SELECT
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , keigen_zeiritsu_kbn
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai_hyouji_flg -- kousaihi_ninzuu_riyou_flg
  , kousaihi_shousai
  , kousaihi_ninzuu
  , kousaihi_hitori_kingaku
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
FROM ryohiseisan_keihi_meisai_old;
DROP TABLE ryohiseisan_keihi_meisai_old;

ALTER TABLE kaigai_ryohi_karibarai_keihi_meisai DROP CONSTRAINT IF EXISTS kaigai_ryohi_karibarai_keihi_meisai_PKEY;
ALTER TABLE kaigai_ryohi_karibarai_keihi_meisai RENAME TO kaigai_ryohi_karibarai_keihi_meisai_old;
create table kaigai_ryohi_karibarai_keihi_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , kaigai_flg character varying(1) not null
  , shiwake_edano integer not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , kazei_flg character varying(1) not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_ninzuu_riyou_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , heishu_cd character varying(4)
  , rate numeric(11, 5)
  , gaika numeric(19, 2)
  , currency_unit character varying(20)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kaigai_ryohi_karibarai_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano,kaigai_flg)
);
comment on table kaigai_ryohi_karibarai_keihi_meisai is '�C�O������o���';
comment on column kaigai_ryohi_karibarai_keihi_meisai.denpyou_id is '�`�[ID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kaigai_flg is '�C�O�t���O';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiyoubi is '�g�p��';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihiki_name is '�����';
comment on column kaigai_ryohi_karibarai_keihi_meisai.tekiyou is '�E�v';
comment on column kaigai_ryohi_karibarai_keihi_meisai.zeiritsu is '�ŗ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kazei_flg is '�ېŃt���O';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiharai_kingaku is '�x�����z';
comment on column kaigai_ryohi_karibarai_keihi_meisai.hontai_kingaku is '�{�̋��z';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shouhizeigaku is '����Ŋz';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
comment on column kaigai_ryohi_karibarai_keihi_meisai.heishu_cd is '����R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.rate is '���[�g';
comment on column kaigai_ryohi_karibarai_keihi_meisai.gaika is '�O�݋��z';
comment on column kaigai_ryohi_karibarai_keihi_meisai.currency_unit is '�ʉݒP��';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihikisaki_cd is '�����R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf1_cd is 'UF1�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf2_cd is 'UF2�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf3_cd is 'UF3�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf4_cd is 'UF4�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf5_cd is 'UF5�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf6_cd is 'UF6�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf7_cd is 'UF7�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf8_cd is 'UF8�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf9_cd is 'UF9�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf10_cd is 'UF10�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.project_name is '�v���W�F�N�g��';
comment on column kaigai_ryohi_karibarai_keihi_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.touroku_time is '�o�^����';
comment on column kaigai_ryohi_karibarai_keihi_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.koushin_time is '�X�V����';
INSERT INTO kaigai_ryohi_karibarai_keihi_meisai
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
  , keigen_zeiritsu_kbn
  , kazei_flg
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai_hyouji_flg -- kousaihi_ninzuu_riyou_flg
  , kousaihi_shousai
  , kousaihi_ninzuu
  , kousaihi_hitori_kingaku
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
FROM kaigai_ryohi_karibarai_keihi_meisai_old;
DROP TABLE kaigai_ryohi_karibarai_keihi_meisai_old;

ALTER TABLE kaigai_ryohiseisan_keihi_meisai DROP CONSTRAINT IF EXISTS kaigai_ryohiseisan_keihi_meisai_PKEY;
ALTER TABLE kaigai_ryohiseisan_keihi_meisai RENAME TO kaigai_ryohiseisan_keihi_meisai_old;
create table kaigai_ryohiseisan_keihi_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , kaigai_flg character varying(1) not null
  , shiwake_edano integer not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , kazei_flg character varying(1) not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , houjin_card_riyou_flg character varying(1) not null
  , kaisha_tehai_flg character varying(1) not null
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_ninzuu_riyou_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , heishu_cd character varying(4)
  , rate numeric(11, 5)
  , gaika numeric(19, 2)
  , currency_unit character varying(20)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , himoduke_card_meisai bigint
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kaigai_ryohiseisan_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano,kaigai_flg)
);
comment on table kaigai_ryohiseisan_keihi_meisai is '�C�O����Z�o���';
comment on column kaigai_ryohiseisan_keihi_meisai.denpyou_id is '�`�[ID';
comment on column kaigai_ryohiseisan_keihi_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kaigai_flg is '�C�O�t���O';
comment on column kaigai_ryohiseisan_keihi_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column kaigai_ryohiseisan_keihi_meisai.shiyoubi is '�g�p��';
comment on column kaigai_ryohiseisan_keihi_meisai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column kaigai_ryohiseisan_keihi_meisai.torihiki_name is '�����';
comment on column kaigai_ryohiseisan_keihi_meisai.tekiyou is '�E�v';
comment on column kaigai_ryohiseisan_keihi_meisai.zeiritsu is '�ŗ�';
comment on column kaigai_ryohiseisan_keihi_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column kaigai_ryohiseisan_keihi_meisai.kazei_flg is '�ېŃt���O';
comment on column kaigai_ryohiseisan_keihi_meisai.shiharai_kingaku is '�x�����z';
comment on column kaigai_ryohiseisan_keihi_meisai.hontai_kingaku is '�{�̋��z';
comment on column kaigai_ryohiseisan_keihi_meisai.shouhizeigaku is '����Ŋz';
comment on column kaigai_ryohiseisan_keihi_meisai.houjin_card_riyou_flg is '�@�l�J�[�h���p�t���O';
comment on column kaigai_ryohiseisan_keihi_meisai.kaisha_tehai_flg is '��Ў�z�t���O';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
comment on column kaigai_ryohiseisan_keihi_meisai.heishu_cd is '����R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.rate is '���[�g';
comment on column kaigai_ryohiseisan_keihi_meisai.gaika is '�O�݋��z';
comment on column kaigai_ryohiseisan_keihi_meisai.currency_unit is '�ʉݒP��';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column kaigai_ryohiseisan_keihi_meisai.torihikisaki_cd is '�����R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column kaigai_ryohiseisan_keihi_meisai.uf1_cd is 'UF1�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf2_cd is 'UF2�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf3_cd is 'UF3�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf4_cd is 'UF4�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf5_cd is 'UF5�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf6_cd is 'UF6�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf7_cd is 'UF7�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf8_cd is 'UF8�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf9_cd is 'UF9�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf10_cd is 'UF10�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.project_name is '�v���W�F�N�g��';
comment on column kaigai_ryohiseisan_keihi_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.himoduke_card_meisai is '�R�t���J�[�h����';
comment on column kaigai_ryohiseisan_keihi_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column kaigai_ryohiseisan_keihi_meisai.touroku_time is '�o�^����';
comment on column kaigai_ryohiseisan_keihi_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column kaigai_ryohiseisan_keihi_meisai.koushin_time is '�X�V����';
INSERT INTO kaigai_ryohiseisan_keihi_meisai
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
  , keigen_zeiritsu_kbn
  , kazei_flg
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai_hyouji_flg -- kousaihi_ninzuu_riyou_flg
  , kousaihi_shousai
  , kousaihi_ninzuu
  , kousaihi_hitori_kingaku
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
FROM kaigai_ryohiseisan_keihi_meisai_old;
DROP TABLE kaigai_ryohiseisan_keihi_meisai_old;


commit;
