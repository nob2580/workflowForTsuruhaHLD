SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- �A���[_0908_�ݎ؉ȖڂɊO�݉Ȗڂ����͂��ꂽ�ꍇ�̂݁A�O�݊֘A���ڂ���͂ł���悤�ɂ���
-- �}�X�^�[�捞����
DELETE FROM master_torikomi_ichiran_de3;
DELETE FROM master_torikomi_shousai_de3;
DELETE FROM master_torikomi_ichiran_sias;
DELETE FROM master_torikomi_shousai_sias;
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- �A���[_0908_�ݎ؉ȖڂɊO�݉Ȗڂ����͂��ꂽ�ꍇ�̂݁A�O�݊֘A���ڂ���͂ł���悤�ɂ���
-- �Ȗڃ}�X�^�[
ALTER TABLE kamoku_master DROP CONSTRAINT IF EXISTS kamoku_master_PKEY;
ALTER TABLE kamoku_master RENAME TO kamoku_master_old;
create table kamoku_master (
  kamoku_gaibu_cd character varying(8) not null
  , kamoku_naibu_cd character varying(15) not null
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , kessanki_bangou smallint
  , chouhyou_shaturyoku_no smallint
  , taishaku_zokusei smallint
  , kamoku_group_kbn smallint
  , kamoku_group_bangou smallint
  , shori_group smallint
  , taikakingaku_nyuuryoku_flg smallint
  , kazei_kbn smallint
  , bunri_kbn smallint
  , shiire_kbn smallint
  , gyousha_kbn smallint
  , zeiritsu_kbn smallint
  , tokusha_hyouji_kbn smallint
  , edaban_minyuuryoku_check smallint
  , torihikisaki_minyuuryoku_check smallint
  , bumon_minyuuryoku_check smallint
  , bumon_edaban_flg smallint
  , segment_minyuuryoku_check smallint
  , project_minyuuryoku_check smallint
  , uf1_minyuuryoku_check smallint
  , uf2_minyuuryoku_check smallint
  , uf3_minyuuryoku_check smallint
  , uf4_minyuuryoku_check smallint
  , uf5_minyuuryoku_check smallint
  , uf6_minyuuryoku_check smallint
  , uf7_minyuuryoku_check smallint
  , uf8_minyuuryoku_check smallint
  , uf9_minyuuryoku_check smallint
  , uf10_minyuuryoku_check smallint
  , uf_kotei1_minyuuryoku_check smallint
  , uf_kotei2_minyuuryoku_check smallint
  , uf_kotei3_minyuuryoku_check smallint
  , uf_kotei4_minyuuryoku_check smallint
  , uf_kotei5_minyuuryoku_check smallint
  , uf_kotei6_minyuuryoku_check smallint
  , uf_kotei7_minyuuryoku_check smallint
  , uf_kotei8_minyuuryoku_check smallint
  , uf_kotei9_minyuuryoku_check smallint
  , uf_kotei10_minyuuryoku_check smallint
  , kouji_minyuuryoku_check smallint
  , kousha_minyuuryoku_check smallint
  , tekiyou_cd_minyuuryoku_check smallint
  , bumon_torhikisaki_kamoku_flg smallint
  , bumon_torihikisaki_edaban_shiyou_flg smallint
  , torihikisaki_kamoku_edaban_flg smallint
  , segment_torihikisaki_kamoku_flg smallint
  , karikanjou_keshikomi_no_flg smallint
  , gaika_flg smallint
  , constraint kamoku_master_PKEY primary key (kamoku_gaibu_cd)
);
comment on table kamoku_master is '�Ȗڃ}�X�^�[';
comment on column kamoku_master.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column kamoku_master.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column kamoku_master.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column kamoku_master.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column kamoku_master.kessanki_bangou is '���Z���ԍ�';
comment on column kamoku_master.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column kamoku_master.taishaku_zokusei is '�ݎؑ���';
comment on column kamoku_master.kamoku_group_kbn is '�ȖڃO���[�v�敪';
comment on column kamoku_master.kamoku_group_bangou is '�ȖڃO���[�v�ԍ�';
comment on column kamoku_master.shori_group is '�����O���[�v';
comment on column kamoku_master.taikakingaku_nyuuryoku_flg is '�Ή����z���̓t���O';
comment on column kamoku_master.kazei_kbn is '�ېŋ敪';
comment on column kamoku_master.bunri_kbn is '�����敪';
comment on column kamoku_master.shiire_kbn is '�d���敪';
comment on column kamoku_master.gyousha_kbn is '�Ǝ�敪';
comment on column kamoku_master.zeiritsu_kbn is '�ŗ��敪';
comment on column kamoku_master.tokusha_hyouji_kbn is '����\���敪';
comment on column kamoku_master.edaban_minyuuryoku_check is '�}�Ԗ����̓`�F�b�N';
comment on column kamoku_master.torihikisaki_minyuuryoku_check is '����斢���̓`�F�b�N';
comment on column kamoku_master.bumon_minyuuryoku_check is '���喢���̓`�F�b�N';
comment on column kamoku_master.bumon_edaban_flg is '����Ȗڎ}�Ԏg�p�t���O';
comment on column kamoku_master.segment_minyuuryoku_check is '�Z�O�����g�����̓`�F�b�N';
comment on column kamoku_master.project_minyuuryoku_check is '�v���W�F�N�g�����̓`�F�b�N';
comment on column kamoku_master.uf1_minyuuryoku_check is '���j�o�[�T���t�B�[���h�P�����̓`�F�b�N';
comment on column kamoku_master.uf2_minyuuryoku_check is '���j�o�[�T���t�B�[���h�Q�����̓`�F�b�N';
comment on column kamoku_master.uf3_minyuuryoku_check is '���j�o�[�T���t�B�[���h�R�����̓`�F�b�N';
comment on column kamoku_master.uf4_minyuuryoku_check is '���j�o�[�T���t�B�[���h�S�����̓`�F�b�N';
comment on column kamoku_master.uf5_minyuuryoku_check is '���j�o�[�T���t�B�[���h�T�����̓`�F�b�N';
comment on column kamoku_master.uf6_minyuuryoku_check is '���j�o�[�T���t�B�[���h�U�����̓`�F�b�N';
comment on column kamoku_master.uf7_minyuuryoku_check is '���j�o�[�T���t�B�[���h�V�����̓`�F�b�N';
comment on column kamoku_master.uf8_minyuuryoku_check is '���j�o�[�T���t�B�[���h�W�����̓`�F�b�N';
comment on column kamoku_master.uf9_minyuuryoku_check is '���j�o�[�T���t�B�[���h�X�����̓`�F�b�N';
comment on column kamoku_master.uf10_minyuuryoku_check is '���j�o�[�T���t�B�[���h�P�O�����̓`�F�b�N';
comment on column kamoku_master.uf_kotei1_minyuuryoku_check is '���j�o�[�T���t�B�[���h�P�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei2_minyuuryoku_check is '���j�o�[�T���t�B�[���h�Q�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei3_minyuuryoku_check is '���j�o�[�T���t�B�[���h�R�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei4_minyuuryoku_check is '���j�o�[�T���t�B�[���h�S�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei5_minyuuryoku_check is '���j�o�[�T���t�B�[���h�T�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei6_minyuuryoku_check is '���j�o�[�T���t�B�[���h�U�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei7_minyuuryoku_check is '���j�o�[�T���t�B�[���h�V�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei8_minyuuryoku_check is '���j�o�[�T���t�B�[���h�W�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei9_minyuuryoku_check is '���j�o�[�T���t�B�[���h�X�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei10_minyuuryoku_check is '���j�o�[�T���t�B�[���h�P�O�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.kouji_minyuuryoku_check is '�H�������̓`�F�b�N';
comment on column kamoku_master.kousha_minyuuryoku_check is '�H�햢���̓`�F�b�N';
comment on column kamoku_master.tekiyou_cd_minyuuryoku_check is '�E�v�R�[�h�����̓`�F�b�N';
comment on column kamoku_master.bumon_torhikisaki_kamoku_flg is '��������Ȗڎg�p�t���O';
comment on column kamoku_master.bumon_torihikisaki_edaban_shiyou_flg is '��������Ȗڎ}�Ԏg�p�t���O';
comment on column kamoku_master.torihikisaki_kamoku_edaban_flg is '�����Ȗڎ}�Ԏg�p�t���O';
comment on column kamoku_master.segment_torihikisaki_kamoku_flg is '�Z�O�����g�����Ȗڎg�p�t���O';
comment on column kamoku_master.karikanjou_keshikomi_no_flg is '���������No�g�p�t���O';
comment on column kamoku_master.gaika_flg is '�O�ݎg�p�t���O';
INSERT INTO kamoku_master
SELECT
	 kamoku_gaibu_cd
	,kamoku_naibu_cd
	,kamoku_name_ryakushiki
	,kamoku_name_seishiki
	,kessanki_bangou
	,chouhyou_shaturyoku_no
	,taishaku_zokusei
	,kamoku_group_kbn
	,kamoku_group_bangou
	,shori_group
	,taikakingaku_nyuuryoku_flg
	,kazei_kbn
	,bunri_kbn
	,shiire_kbn
	,gyousha_kbn
	,zeiritsu_kbn
	,tokusha_hyouji_kbn
	,edaban_minyuuryoku_check
	,torihikisaki_minyuuryoku_check
	,bumon_minyuuryoku_check
	,bumon_edaban_flg
	,segment_minyuuryoku_check
	,project_minyuuryoku_check
	,uf1_minyuuryoku_check
	,uf2_minyuuryoku_check
	,uf3_minyuuryoku_check
	,uf4_minyuuryoku_check
	,uf5_minyuuryoku_check
	,uf6_minyuuryoku_check
	,uf7_minyuuryoku_check
	,uf8_minyuuryoku_check
	,uf9_minyuuryoku_check
	,uf10_minyuuryoku_check
	,uf_kotei1_minyuuryoku_check
	,uf_kotei2_minyuuryoku_check
	,uf_kotei3_minyuuryoku_check
	,uf_kotei4_minyuuryoku_check
	,uf_kotei5_minyuuryoku_check
	,uf_kotei6_minyuuryoku_check
	,uf_kotei7_minyuuryoku_check
	,uf_kotei8_minyuuryoku_check
	,uf_kotei9_minyuuryoku_check
	,uf_kotei10_minyuuryoku_check
	,kouji_minyuuryoku_check
	,kousha_minyuuryoku_check
	,tekiyou_cd_minyuuryoku_check
	,bumon_torhikisaki_kamoku_flg
	,bumon_torihikisaki_edaban_shiyou_flg
	,torihikisaki_kamoku_edaban_flg
	,segment_torihikisaki_kamoku_flg
	,karikanjou_keshikomi_no_flg
	,0   --gaika_flg
FROM kamoku_master_old;
DROP TABLE kamoku_master_old;


-- WF��ʃI�v�V�����̓p�b�`�ɂ��UPDATE��ł�ON�Ƃ���
UPDATE setting_info SET setting_val ='1' WHERE setting_name = 'ippan_option';


commit;
