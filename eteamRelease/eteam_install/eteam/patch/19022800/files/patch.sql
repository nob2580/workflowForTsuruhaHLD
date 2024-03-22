SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- 0703_�C�O�����Ή� �ݒ���f�[�^�ύX ��hyouji_jun900�ȍ~�̓J�X�^�}�C�Y�̈�
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

-- 0703_�C�O�����Ή� ��ʍ��ڐ���
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

-- 0703_�C�O�����Ή� �C�O�������}�X�^�[
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
INSERT INTO kaigai_nittou_nado_master
SELECT
    shubetsu1
   ,shubetsu2
   ,yakushoku_cd
   ,tanka
   ,''		--heishu_cd
   ,''		--currency_unit
   ,null	--tanka_gaika
   ,shouhyou_shorui_hissu_flg
   ,nittou_shukuhakuhi_flg
   ,zei_kubun
FROM kaigai_nittou_nado_master_old;
DROP TABLE kaigai_nittou_nado_master_old;

-- �y���ŗ��Ή� �����R�[�h
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
DROP TABLE naibu_cd_setting_tmp;

-- �y���ŗ��Ή� �d��p�^�[���ݒ�
CREATE TABLE shiwake_pattern_setting_tmp AS SELECT * FROM shiwake_pattern_setting WHERE 1 = 2;
\copy shiwake_pattern_setting_tmp FROM '.\files\csv\shiwake_pattern_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM shiwake_pattern_setting_tmp WHERE denpyou_kbn='A003' AND (SELECT COUNT(*) FROM shiwake_pattern_setting WHERE denpyou_kbn='A003') = 0;
DELETE FROM shiwake_pattern_setting_tmp WHERE denpyou_kbn='A013' AND (SELECT COUNT(*) FROM shiwake_pattern_setting WHERE denpyou_kbn='A013') = 0;
DELETE FROM shiwake_pattern_setting;
INSERT INTO shiwake_pattern_setting SELECT * FROM shiwake_pattern_setting_tmp;
DROP TABLE shiwake_pattern_setting_tmp;

--�y���ŗ��Ή� �d��p�^�[���ϐ��ݒ�
DELETE FROM shiwake_pattern_var_setting;
\copy shiwake_pattern_var_setting FROM '.\files\csv\shiwake_pattern_var_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- �y���ŗ��Ή� �d��p�^�[���}�X�^�[
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
) WITHOUT OIDS;
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
   ,'' --kari_zeiritsu
   ,'' --kari_keigen_zeiritsu_kbn
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
UPDATE shiwake_pattern_master SET kari_zeiritsu = '<ZEIRITSU>' WHERE denpyou_kbn = 'A001' OR denpyou_kbn = 'A003' OR denpyou_kbn = 'A009' OR denpyou_kbn = 'A013';
DROP TABLE shiwake_pattern_master_old;

-- ����ŗ��}�X�^�[ ��`�X�V
ALTER TABLE shouhizeiritsu RENAME TO shouhizeiritsu_old;
CREATE TABLE shouhizeiritsu(
sort_jun character(3) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , hasuu_keisan_kbn character varying(1)
  , yuukou_kigen_from date
  , yuukou_kigen_to date
  , primary key (sort_jun,zeiritsu)
) WITHOUT OIDS;
comment on table shouhizeiritsu is '����ŗ�';
comment on column shouhizeiritsu.sort_jun is '���я�';
comment on column shouhizeiritsu.zeiritsu is '�ŗ�';
comment on column shouhizeiritsu.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column shouhizeiritsu.hasuu_keisan_kbn is '�[���v�Z�敪';
comment on column shouhizeiritsu.yuukou_kigen_from is '�L�������J�n��';
comment on column shouhizeiritsu.yuukou_kigen_to is '�L�������I����';

INSERT INTO shouhizeiritsu
SELECT
  sort_jun
  , zeiritsu
  , '0' --keigen_zeiritsu_kbn
  , hasuu_keisan_kbn
  , yuukou_kigen_from
  , yuukou_kigen_to
FROM shouhizeiritsu_old;
DROP TABLE shouhizeiritsu_old;

-- �y���ŗ��敪 �}�X�^�[�Ǘ��Ő��i����ŗ��j
-- 0703_�C�O�����Ή� �}�X�^�[�Ǘ��Ő��i�C�O�������}�X�^�[�j
UPDATE master_kanri_hansuu SET delete_flg = '1' WHERE master_id = 'shouhizeiritsu' OR master_id = 'kaigai_nittou_nado_master';
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
   'shouhizeiritsu'		--����ŗ�
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='shouhizeiritsu')
 , '0'
 , '����ŗ�_patch.csv'
 , length(convert_to(E'���я�,�ŗ�,�y���ŗ��敪,�[���v�Z�敪,�L�������J�n��,�L�������I����\r\nsort_jun,zeiritsu,keigen_zeiritsu_kbn,hasuu_keisan_kbn,yuukou_kigen_from,yuukou_kigen_to\r\nvarchar(3),decimal(3),varchar(1),varchar(1),date,date\r\n1,1,,,,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || zeiritsu || ',' || keigen_zeiritsu_kbn || ',' || hasuu_keisan_kbn || ',' || replace(yuukou_kigen_from::text,'-'::text,'/'::text) || ',' || replace(yuukou_kigen_to::text,'-'::text,'/'::text) FROM shouhizeiritsu), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'���я�,�ŗ�,�y���ŗ��敪,�[���v�Z�敪,�L�������J�n��,�L�������I����\r\nsort_jun,zeiritsu,keigen_zeiritsu_kbn,hasuu_keisan_kbn,yuukou_kigen_from,yuukou_kigen_to\r\nvarchar(3),decimal(3),varchar(1),varchar(1),date,date\r\n1,1,,,,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || zeiritsu || ',' || keigen_zeiritsu_kbn || ',' || hasuu_keisan_kbn || ',' || replace(yuukou_kigen_from::text,'-'::text,'/'::text) || ',' || replace(yuukou_kigen_to::text,'-'::text,'/'::text) FROM shouhizeiritsu), E'\r\n') || E'\r\n','sjis')
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
 , length(convert_to(E'��ʂP,��ʂQ,��E�R�[�h,�P���i�M�݁j,����R�[�h,�ʉݒP��,�P���i�O�݁j,�؜ߏ��ޕK�{�t���O,�����E�h����t���O,�ŋ敪\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,heishu_cd,currency_unit,tanka_gaika,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(4),varchar(20),decimal,varchar(1),varchar(1),varchar(1)\r\n1,1,1,,2,2,,2,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || heishu_cd || ',' || currency_unit || ',' || COALESCE(tanka_gaika::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun FROM kaigai_nittou_nado_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'��ʂP,��ʂQ,��E�R�[�h,�P���i�M�݁j,����R�[�h,�ʉݒP��,�P���i�O�݁j,�؜ߏ��ޕK�{�t���O,�����E�h����t���O,�ŋ敪\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,heishu_cd,currency_unit,tanka_gaika,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(4),varchar(20),decimal,varchar(1),varchar(1),varchar(1)\r\n1,1,1,,2,2,,2,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || heishu_cd || ',' || currency_unit || ',' || COALESCE(tanka_gaika::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun FROM kaigai_nittou_nado_master), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
 );


-- �U�֓`�[�e�[�u�� ��`�X�V
ALTER TABLE furikae RENAME TO furikae_old;
CREATE TABLE furikae (
  denpyou_id character varying(19) not null
  , denpyou_date date not null
  , shouhyou_shorui_flg character varying(1) not null
  , kingaku numeric(15) not null
  , hontai_kingaku numeric(15) not null
  , shouhizeigaku numeric(15)
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , tekiyou character varying(60) not null
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
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kari_torihikisaki_cd character varying(12) not null
  , kari_torihikisaki_name_ryakushiki character varying(20) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , kashi_torihikisaki_cd character varying(12) not null
  , kashi_torihikisaki_name_ryakushiki character varying(20) not null
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
  , kari_project_cd character varying(12) not null
  , kari_project_name character varying(20) not null
  , kari_segment_cd character varying(8) not null
  , kari_segment_name_ryakushiki character varying(20) not null
  , kashi_project_cd character varying(12) not null
  , kashi_project_name character varying(20) not null
  , kashi_segment_cd character varying(8) not null
  , kashi_segment_name_ryakushiki character varying(20) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id)
) WITHOUT OIDS;
comment on table furikae is '�U��';
comment on column furikae.denpyou_id is '�`�[ID';
comment on column furikae.denpyou_date is '�`�[���t';
comment on column furikae.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column furikae.kingaku is '���z';
comment on column furikae.hontai_kingaku is '�{�̋��z';
comment on column furikae.shouhizeigaku is '����Ŋz';
comment on column furikae.zeiritsu is '�ŗ�';
comment on column furikae.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column furikae.tekiyou is '�E�v';
comment on column furikae.hf1_cd is 'HF1�R�[�h';
comment on column furikae.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column furikae.hf2_cd is 'HF2�R�[�h';
comment on column furikae.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column furikae.hf3_cd is 'HF3�R�[�h';
comment on column furikae.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column furikae.hf4_cd is 'HF4�R�[�h';
comment on column furikae.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column furikae.hf5_cd is 'HF5�R�[�h';
comment on column furikae.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column furikae.hf6_cd is 'HF6�R�[�h';
comment on column furikae.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column furikae.hf7_cd is 'HF7�R�[�h';
comment on column furikae.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column furikae.hf8_cd is 'HF8�R�[�h';
comment on column furikae.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column furikae.hf9_cd is 'HF9�R�[�h';
comment on column furikae.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column furikae.hf10_cd is 'HF10�R�[�h';
comment on column furikae.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column furikae.bikou is '���l�i��v�`�[�j';
comment on column furikae.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column furikae.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column furikae.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column furikae.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column furikae.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column furikae.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column furikae.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column furikae.kari_torihikisaki_cd is '�ؕ������R�[�h';
comment on column furikae.kari_torihikisaki_name_ryakushiki is '�ؕ�����於�i�����j';
comment on column furikae.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column furikae.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column furikae.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column furikae.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column furikae.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column furikae.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column furikae.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column furikae.kashi_torihikisaki_cd is '�ݕ������R�[�h';
comment on column furikae.kashi_torihikisaki_name_ryakushiki is '�ݕ�����於�i�����j';
comment on column furikae.kari_uf1_cd is '�ؕ�UF1�R�[�h';
comment on column furikae.kari_uf1_name_ryakushiki is '�ؕ�UF1���i�����j';
comment on column furikae.kari_uf2_cd is '�ؕ�UF2�R�[�h';
comment on column furikae.kari_uf2_name_ryakushiki is '�ؕ�UF2���i�����j';
comment on column furikae.kari_uf3_cd is '�ؕ�UF3�R�[�h';
comment on column furikae.kari_uf3_name_ryakushiki is '�ؕ�UF3���i�����j';
comment on column furikae.kari_uf4_cd is '�ؕ�UF4�R�[�h';
comment on column furikae.kari_uf4_name_ryakushiki is '�ؕ�UF4���i�����j';
comment on column furikae.kari_uf5_cd is '�ؕ�UF5�R�[�h';
comment on column furikae.kari_uf5_name_ryakushiki is '�ؕ�UF5���i�����j';
comment on column furikae.kari_uf6_cd is '�ؕ�UF6�R�[�h';
comment on column furikae.kari_uf6_name_ryakushiki is '�ؕ�UF6���i�����j';
comment on column furikae.kari_uf7_cd is '�ؕ�UF7�R�[�h';
comment on column furikae.kari_uf7_name_ryakushiki is '�ؕ�UF7���i�����j';
comment on column furikae.kari_uf8_cd is '�ؕ�UF8�R�[�h';
comment on column furikae.kari_uf8_name_ryakushiki is '�ؕ�UF8���i�����j';
comment on column furikae.kari_uf9_cd is '�ؕ�UF9�R�[�h';
comment on column furikae.kari_uf9_name_ryakushiki is '�ؕ�UF9���i�����j';
comment on column furikae.kari_uf10_cd is '�ؕ�UF10�R�[�h';
comment on column furikae.kari_uf10_name_ryakushiki is '�ؕ�UF10���i�����j';
comment on column furikae.kashi_uf1_cd is '�ݕ�UF1�R�[�h';
comment on column furikae.kashi_uf1_name_ryakushiki is '�ݕ�UF1���i�����j';
comment on column furikae.kashi_uf2_cd is '�ݕ�UF2�R�[�h';
comment on column furikae.kashi_uf2_name_ryakushiki is '�ݕ�UF2���i�����j';
comment on column furikae.kashi_uf3_cd is '�ݕ�UF3�R�[�h';
comment on column furikae.kashi_uf3_name_ryakushiki is '�ݕ�UF3���i�����j';
comment on column furikae.kashi_uf4_cd is '�ݕ�UF4�R�[�h';
comment on column furikae.kashi_uf4_name_ryakushiki is '�ݕ�UF4���i�����j';
comment on column furikae.kashi_uf5_cd is '�ݕ�UF5�R�[�h';
comment on column furikae.kashi_uf5_name_ryakushiki is '�ݕ�UF5���i�����j';
comment on column furikae.kashi_uf6_cd is '�ݕ�UF6�R�[�h';
comment on column furikae.kashi_uf6_name_ryakushiki is '�ݕ�UF6���i�����j';
comment on column furikae.kashi_uf7_cd is '�ݕ�UF7�R�[�h';
comment on column furikae.kashi_uf7_name_ryakushiki is '�ݕ�UF7���i�����j';
comment on column furikae.kashi_uf8_cd is '�ݕ�UF8�R�[�h';
comment on column furikae.kashi_uf8_name_ryakushiki is '�ݕ�UF8���i�����j';
comment on column furikae.kashi_uf9_cd is '�ݕ�UF9�R�[�h';
comment on column furikae.kashi_uf9_name_ryakushiki is '�ݕ�UF9���i�����j';
comment on column furikae.kashi_uf10_cd is '�ݕ�UF10�R�[�h';
comment on column furikae.kashi_uf10_name_ryakushiki is '�ݕ�UF10���i�����j';
comment on column furikae.kari_project_cd is '�ؕ��v���W�F�N�g�R�[�h';
comment on column furikae.kari_project_name is '�ؕ��v���W�F�N�g��';
comment on column furikae.kari_segment_cd is '�ؕ��Z�O�����g�R�[�h';
comment on column furikae.kari_segment_name_ryakushiki is '�ؕ��Z�O�����g���i�����j';
comment on column furikae.kashi_project_cd is '�ݕ��v���W�F�N�g�R�[�h';
comment on column furikae.kashi_project_name is '�ݕ��v���W�F�N�g��';
comment on column furikae.kashi_segment_cd is '�ݕ��Z�O�����g�R�[�h';
comment on column furikae.kashi_segment_name_ryakushiki is '�ݕ��Z�O�����g���i�����j';
comment on column furikae.touroku_user_id is '�o�^���[�U�[ID';
comment on column furikae.touroku_time is '�o�^����';
comment on column furikae.koushin_user_id is '�X�V���[�U�[ID';
comment on column furikae.koushin_time is '�X�V����';

INSERT INTO furikae
SELECT
  denpyou_id
  , denpyou_date
  , shouhyou_shorui_flg
  , kingaku
  , hontai_kingaku
  , shouhizeigaku
  , zeiritsu
  , '0' --keigen_zeiritsu_kbn
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
FROM furikae_old;
DROP TABLE furikae_old;

-- �����t�֓`�[ ��`�ύX
ALTER TABLE tsukekae RENAME TO tsukekae_old;
CREATE TABLE tsukekae (
  denpyou_id character varying(19) not null
  , denpyou_date date not null
  , shouhyou_shorui_flg character varying(1) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , kingaku_goukei numeric(15) not null
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
  , hosoku character varying(240) not null
  , tsukekae_kbn character varying(1) not null
  , moto_kamoku_cd character varying(8) not null
  , moto_kamoku_name character varying(22) not null
  , moto_kamoku_edaban_cd character varying(12) not null
  , moto_kamoku_edaban_name character varying(20) not null
  , moto_futan_bumon_cd character varying(8) not null
  , moto_futan_bumon_name character varying(20) not null
  , moto_torihikisaki_cd character varying(12) not null
  , moto_torihikisaki_name_ryakushiki character varying(20) not null
  , moto_kazei_kbn character varying(3) not null
  , moto_uf1_cd character varying(20) not null
  , moto_uf1_name_ryakushiki character varying(20) not null
  , moto_uf2_cd character varying(20) not null
  , moto_uf2_name_ryakushiki character varying(20) not null
  , moto_uf3_cd character varying(20) not null
  , moto_uf3_name_ryakushiki character varying(20) not null
  , moto_uf4_cd character varying(20) not null
  , moto_uf4_name_ryakushiki character varying(20) not null
  , moto_uf5_cd character varying(20) not null
  , moto_uf5_name_ryakushiki character varying(20) not null
  , moto_uf6_cd character varying(20) not null
  , moto_uf6_name_ryakushiki character varying(20) not null
  , moto_uf7_cd character varying(20) not null
  , moto_uf7_name_ryakushiki character varying(20) not null
  , moto_uf8_cd character varying(20) not null
  , moto_uf8_name_ryakushiki character varying(20) not null
  , moto_uf9_cd character varying(20) not null
  , moto_uf9_name_ryakushiki character varying(20) not null
  , moto_uf10_cd character varying(20) not null
  , moto_uf10_name_ryakushiki character varying(20) not null
  , moto_project_cd character varying(12) not null
  , moto_project_name character varying(20) not null
  , moto_segment_cd character varying(8) not null
  , moto_segment_name_ryakushiki character varying(20) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id)
) WITHOUT OIDS;
comment on table tsukekae is '�t��';
comment on column tsukekae.denpyou_id is '�`�[ID';
comment on column tsukekae.denpyou_date is '�`�[���t';
comment on column tsukekae.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column tsukekae.zeiritsu is '�ŗ�';
comment on column tsukekae.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column tsukekae.kingaku_goukei is '���z���v';
comment on column tsukekae.hf1_cd is 'HF1�R�[�h';
comment on column tsukekae.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column tsukekae.hf2_cd is 'HF2�R�[�h';
comment on column tsukekae.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column tsukekae.hf3_cd is 'HF3�R�[�h';
comment on column tsukekae.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column tsukekae.hf4_cd is 'HF4�R�[�h';
comment on column tsukekae.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column tsukekae.hf5_cd is 'HF5�R�[�h';
comment on column tsukekae.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column tsukekae.hf6_cd is 'HF6�R�[�h';
comment on column tsukekae.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column tsukekae.hf7_cd is 'HF7�R�[�h';
comment on column tsukekae.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column tsukekae.hf8_cd is 'HF8�R�[�h';
comment on column tsukekae.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column tsukekae.hf9_cd is 'HF9�R�[�h';
comment on column tsukekae.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column tsukekae.hf10_cd is 'HF10�R�[�h';
comment on column tsukekae.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column tsukekae.hosoku is '�⑫';
comment on column tsukekae.tsukekae_kbn is '�t�֋敪';
comment on column tsukekae.moto_kamoku_cd is '�t�֌��ȖڃR�[�h';
comment on column tsukekae.moto_kamoku_name is '�t�֌��Ȗږ�';
comment on column tsukekae.moto_kamoku_edaban_cd is '�t�֌��Ȗڎ}�ԃR�[�h';
comment on column tsukekae.moto_kamoku_edaban_name is '�t�֌��Ȗڎ}�Ԗ�';
comment on column tsukekae.moto_futan_bumon_cd is '�t�֌����S����R�[�h';
comment on column tsukekae.moto_futan_bumon_name is '�t�֌����S���喼';
comment on column tsukekae.moto_torihikisaki_cd is '�t�֌������R�[�h';
comment on column tsukekae.moto_torihikisaki_name_ryakushiki is '�t�֌�����於�i�����j';
comment on column tsukekae.moto_kazei_kbn is '�t�֌��ېŋ敪';
comment on column tsukekae.moto_uf1_cd is '�t�֌�UF1�R�[�h';
comment on column tsukekae.moto_uf1_name_ryakushiki is '�t�֌�UF1���i�����j';
comment on column tsukekae.moto_uf2_cd is '�t�֌�UF2�R�[�h';
comment on column tsukekae.moto_uf2_name_ryakushiki is '�t�֌�UF2���i�����j';
comment on column tsukekae.moto_uf3_cd is '�t�֌�UF3�R�[�h';
comment on column tsukekae.moto_uf3_name_ryakushiki is '�t�֌�UF3���i�����j';
comment on column tsukekae.moto_uf4_cd is '�t�֌�UF4�R�[�h';
comment on column tsukekae.moto_uf4_name_ryakushiki is '�t�֌�UF4���i�����j';
comment on column tsukekae.moto_uf5_cd is '�t�֌�UF5�R�[�h';
comment on column tsukekae.moto_uf5_name_ryakushiki is '�t�֌�UF5���i�����j';
comment on column tsukekae.moto_uf6_cd is '�t�֌�UF6�R�[�h';
comment on column tsukekae.moto_uf6_name_ryakushiki is '�t�֌�UF6���i�����j';
comment on column tsukekae.moto_uf7_cd is '�t�֌�UF7�R�[�h';
comment on column tsukekae.moto_uf7_name_ryakushiki is '�t�֌�UF7���i�����j';
comment on column tsukekae.moto_uf8_cd is '�t�֌�UF8�R�[�h';
comment on column tsukekae.moto_uf8_name_ryakushiki is '�t�֌�UF8���i�����j';
comment on column tsukekae.moto_uf9_cd is '�t�֌�UF9�R�[�h';
comment on column tsukekae.moto_uf9_name_ryakushiki is '�t�֌�UF9���i�����j';
comment on column tsukekae.moto_uf10_cd is '�t�֌�UF10�R�[�h';
comment on column tsukekae.moto_uf10_name_ryakushiki is '�t�֌�UF10���i�����j';
comment on column tsukekae.moto_project_cd is '�t�֌��v���W�F�N�g�R�[�h';
comment on column tsukekae.moto_project_name is '�t�֌��v���W�F�N�g��';
comment on column tsukekae.moto_segment_cd is '�t�֌��Z�O�����g�R�[�h';
comment on column tsukekae.moto_segment_name_ryakushiki is '�t�֌��Z�O�����g���i�����j';
comment on column tsukekae.touroku_user_id is '�o�^���[�U�[ID';
comment on column tsukekae.touroku_time is '�o�^����';
comment on column tsukekae.koushin_user_id is '�X�V���[�U�[ID';
comment on column tsukekae.koushin_time is '�X�V����';

INSERT INTO tsukekae
SELECT
  denpyou_id
  , denpyou_date
  , shouhyou_shorui_flg
  , zeiritsu
  , '0' --keigen_zeiritsu_kbn
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
FROM tsukekae_old;
DROP TABLE tsukekae_old;

-- �y���ŗ��Ή� �x���˗�����
ALTER TABLE shiharai_irai_meisai RENAME TO shiharai_irai_meisai_old;
create table shiharai_irai_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , shiharai_kingaku numeric(15) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
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
  , primary key (denpyou_id,denpyou_edano)
) WITHOUT OIDS;
comment on table shiharai_irai_meisai is '�x���˗�����';
comment on column shiharai_irai_meisai.denpyou_id is '�`�[ID';
comment on column shiharai_irai_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column shiharai_irai_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column shiharai_irai_meisai.torihiki_name is '�����';
comment on column shiharai_irai_meisai.tekiyou is '�E�v';
comment on column shiharai_irai_meisai.shiharai_kingaku is '�x�����z';
comment on column shiharai_irai_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column shiharai_irai_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column shiharai_irai_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column shiharai_irai_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column shiharai_irai_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column shiharai_irai_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column shiharai_irai_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column shiharai_irai_meisai.zeiritsu is '�ŗ�';
comment on column shiharai_irai_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column shiharai_irai_meisai.uf1_cd is 'UF1�R�[�h';
comment on column shiharai_irai_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column shiharai_irai_meisai.uf2_cd is 'UF2�R�[�h';
comment on column shiharai_irai_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column shiharai_irai_meisai.uf3_cd is 'UF3�R�[�h';
comment on column shiharai_irai_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column shiharai_irai_meisai.uf4_cd is 'UF4�R�[�h';
comment on column shiharai_irai_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column shiharai_irai_meisai.uf5_cd is 'UF5�R�[�h';
comment on column shiharai_irai_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column shiharai_irai_meisai.uf6_cd is 'UF6�R�[�h';
comment on column shiharai_irai_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column shiharai_irai_meisai.uf7_cd is 'UF7�R�[�h';
comment on column shiharai_irai_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column shiharai_irai_meisai.uf8_cd is 'UF8�R�[�h';
comment on column shiharai_irai_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column shiharai_irai_meisai.uf9_cd is 'UF9�R�[�h';
comment on column shiharai_irai_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column shiharai_irai_meisai.uf10_cd is 'UF10�R�[�h';
comment on column shiharai_irai_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column shiharai_irai_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column shiharai_irai_meisai.project_name is '�v���W�F�N�g��';
comment on column shiharai_irai_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column shiharai_irai_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column shiharai_irai_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column shiharai_irai_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column shiharai_irai_meisai.touroku_time is '�o�^����';
comment on column shiharai_irai_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column shiharai_irai_meisai.koushin_time is '�X�V����';
INSERT INTO shiharai_irai_meisai
SELECT
	 denpyou_id
	,denpyou_edano
	,shiwake_edano
	,torihiki_name
	,tekiyou
	,shiharai_kingaku
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,zeiritsu
	,'0' --keigen_zeiritsu_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM shiharai_irai_meisai_old;
DROP TABLE shiharai_irai_meisai_old;


-- �y���ŗ��Ή� �ʋΒ���\��
ALTER TABLE tsuukinteiki RENAME TO tsuukinteiki_old;
create table tsuukinteiki (
  denpyou_id character varying(19) not null
  , shiyou_kikan_kbn character varying(2) not null
  , shiyou_kaishibi date not null
  , shiyou_shuuryoubi date not null
  , jyousha_kukan character varying not null
  , teiki_serialize_data character varying not null
  , shiharaibi date
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , kingaku numeric(15) not null
  , tenyuuryoku_flg character varying(1) not null
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
  , shiwake_edano integer not null
  , torihiki_name character varying(20) not null
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
  , primary key (denpyou_id)
);
comment on table tsuukinteiki is '�ʋΒ��';
comment on column tsuukinteiki.denpyou_id is '�`�[ID';
comment on column tsuukinteiki.shiyou_kikan_kbn is '�g�p���ԋ敪';
comment on column tsuukinteiki.shiyou_kaishibi is '�g�p�J�n��';
comment on column tsuukinteiki.shiyou_shuuryoubi is '�g�p�I����';
comment on column tsuukinteiki.jyousha_kukan is '��ԋ��';
comment on column tsuukinteiki.teiki_serialize_data is '�����ԃV���A���C�Y�f�[�^';
comment on column tsuukinteiki.shiharaibi is '�x����';
comment on column tsuukinteiki.tekiyou is '�E�v';
comment on column tsuukinteiki.zeiritsu is '�ŗ�';
comment on column tsuukinteiki.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column tsuukinteiki.kingaku is '���z';
comment on column tsuukinteiki.tenyuuryoku_flg is '����̓t���O';
comment on column tsuukinteiki.hf1_cd is 'HF1�R�[�h';
comment on column tsuukinteiki.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column tsuukinteiki.hf2_cd is 'HF2�R�[�h';
comment on column tsuukinteiki.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column tsuukinteiki.hf3_cd is 'HF3�R�[�h';
comment on column tsuukinteiki.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column tsuukinteiki.hf4_cd is 'HF4�R�[�h';
comment on column tsuukinteiki.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column tsuukinteiki.hf5_cd is 'HF5�R�[�h';
comment on column tsuukinteiki.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column tsuukinteiki.hf6_cd is 'HF6�R�[�h';
comment on column tsuukinteiki.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column tsuukinteiki.hf7_cd is 'HF7�R�[�h';
comment on column tsuukinteiki.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column tsuukinteiki.hf8_cd is 'HF8�R�[�h';
comment on column tsuukinteiki.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column tsuukinteiki.hf9_cd is 'HF9�R�[�h';
comment on column tsuukinteiki.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column tsuukinteiki.hf10_cd is 'HF10�R�[�h';
comment on column tsuukinteiki.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column tsuukinteiki.shiwake_edano is '�d��}�ԍ�';
comment on column tsuukinteiki.torihiki_name is '�����';
comment on column tsuukinteiki.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column tsuukinteiki.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column tsuukinteiki.torihikisaki_cd is '�����R�[�h';
comment on column tsuukinteiki.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column tsuukinteiki.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column tsuukinteiki.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column tsuukinteiki.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column tsuukinteiki.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column tsuukinteiki.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column tsuukinteiki.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column tsuukinteiki.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column tsuukinteiki.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column tsuukinteiki.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column tsuukinteiki.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column tsuukinteiki.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column tsuukinteiki.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column tsuukinteiki.uf1_cd is 'UF1�R�[�h';
comment on column tsuukinteiki.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column tsuukinteiki.uf2_cd is 'UF2�R�[�h';
comment on column tsuukinteiki.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column tsuukinteiki.uf3_cd is 'UF3�R�[�h';
comment on column tsuukinteiki.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column tsuukinteiki.uf4_cd is 'UF4�R�[�h';
comment on column tsuukinteiki.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column tsuukinteiki.uf5_cd is 'UF5�R�[�h';
comment on column tsuukinteiki.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column tsuukinteiki.uf6_cd is 'UF6�R�[�h';
comment on column tsuukinteiki.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column tsuukinteiki.uf7_cd is 'UF7�R�[�h';
comment on column tsuukinteiki.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column tsuukinteiki.uf8_cd is 'UF8�R�[�h';
comment on column tsuukinteiki.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column tsuukinteiki.uf9_cd is 'UF9�R�[�h';
comment on column tsuukinteiki.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column tsuukinteiki.uf10_cd is 'UF10�R�[�h';
comment on column tsuukinteiki.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column tsuukinteiki.project_cd is '�v���W�F�N�g�R�[�h';
comment on column tsuukinteiki.project_name is '�v���W�F�N�g��';
comment on column tsuukinteiki.segment_cd is '�Z�O�����g�R�[�h';
comment on column tsuukinteiki.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column tsuukinteiki.tekiyou_cd is '�E�v�R�[�h';
comment on column tsuukinteiki.touroku_user_id is '�o�^���[�U�[ID';
comment on column tsuukinteiki.touroku_time is '�o�^����';
comment on column tsuukinteiki.koushin_user_id is '�X�V���[�U�[ID';
comment on column tsuukinteiki.koushin_time is '�X�V����';
INSERT INTO tsuukinteiki
SELECT
	 denpyou_id
	,shiyou_kikan_kbn
	,shiyou_kaishibi
	,shiyou_shuuryoubi
	,jyousha_kukan
	,teiki_serialize_data
	,shiharaibi
	,tekiyou
	,zeiritsu
	,'0'	--�y���ŗ��敪
	,kingaku
	,tenyuuryoku_flg
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
	,shiwake_edano
	,torihiki_name
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM tsuukinteiki_old;
DROP TABLE tsuukinteiki_old;

-- 0703_�C�O�p�����̊O�ݑΉ�
ALTER TABLE kaigai_ryohi_karibarai RENAME TO kaigai_ryohi_karibarai_old;
create table kaigai_ryohi_karibarai (
  denpyou_id character varying(19) not null
  , karibarai_on character varying(1) default '0' not null
  , dairiflg character varying(1) not null
  , user_id character varying(30)
  , shain_no character varying(15) not null
  , user_sei character varying(10) not null
  , user_mei character varying(10) not null
  , houmonsaki character varying(120) not null
  , mokuteki character varying(240) not null
  , seisankikan_from date
  , seisankikan_from_hour character varying(2)
  , seisankikan_from_min character varying(2)
  , seisankikan_to date
  , seisankikan_to_hour character varying(2)
  , seisankikan_to_min character varying(2)
  , shiharaibi date
  , shiharaikiboubi date
  , shiharaihouhou character varying(1) not null
  , tekiyou character varying(60) not null
  , kingaku numeric(15) not null
  , karibarai_kingaku numeric(15)
  , sashihiki_num numeric(2)
  , sashihiki_tanka numeric(6)
  , sashihiki_num_kaigai numeric(2)
  , sashihiki_tanka_kaigai numeric(6)
  , sashihiki_heishu_cd_kaigai character varying(4) not null
  , sashihiki_rate_kaigai numeric(11,5)
  , sashihiki_tanka_kaigai_gaika numeric(8,2)
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
  , hosoku character varying(240) not null
  , shiwake_edano integer
  , torihiki_name character varying(20) not null
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
  , seisan_kanryoubi date
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id)
);
comment on table kaigai_ryohi_karibarai is '�C�O�����';
comment on column kaigai_ryohi_karibarai.denpyou_id is '�`�[ID';
comment on column kaigai_ryohi_karibarai.karibarai_on is '�����\���t���O';
comment on column kaigai_ryohi_karibarai.dairiflg is '�㗝�t���O';
comment on column kaigai_ryohi_karibarai.user_id is '���[�U�[ID';
comment on column kaigai_ryohi_karibarai.shain_no is '�Ј��ԍ�';
comment on column kaigai_ryohi_karibarai.user_sei is '���[�U�[��';
comment on column kaigai_ryohi_karibarai.user_mei is '���[�U�[��';
comment on column kaigai_ryohi_karibarai.houmonsaki is '�K���';
comment on column kaigai_ryohi_karibarai.mokuteki is '�ړI';
comment on column kaigai_ryohi_karibarai.seisankikan_from is '���Z���ԊJ�n��';
comment on column kaigai_ryohi_karibarai.seisankikan_from_hour is '���Z���ԊJ�n�����i���j';
comment on column kaigai_ryohi_karibarai.seisankikan_from_min is '���Z���ԊJ�n�����i���j';
comment on column kaigai_ryohi_karibarai.seisankikan_to is '���Z���ԏI����';
comment on column kaigai_ryohi_karibarai.seisankikan_to_hour is '���Z���ԏI�������i���j';
comment on column kaigai_ryohi_karibarai.seisankikan_to_min is '���Z���ԏI�������i���j';
comment on column kaigai_ryohi_karibarai.shiharaibi is '�x����';
comment on column kaigai_ryohi_karibarai.shiharaikiboubi is '�x����]��';
comment on column kaigai_ryohi_karibarai.shiharaihouhou is '�x�����@';
comment on column kaigai_ryohi_karibarai.tekiyou is '�E�v';
comment on column kaigai_ryohi_karibarai.kingaku is '���z';
comment on column kaigai_ryohi_karibarai.karibarai_kingaku is '�������z';
comment on column kaigai_ryohi_karibarai.sashihiki_num is '������';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka is '�����P��';
comment on column kaigai_ryohi_karibarai.sashihiki_num_kaigai is '�����񐔁i�C�O�j';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka_kaigai is '�����P���i�C�O�j';
comment on column kaigai_ryohi_karibarai.sashihiki_heishu_cd_kaigai is '��������R�[�h�i�C�O�j';
comment on column kaigai_ryohi_karibarai.sashihiki_rate_kaigai is '�������[�g�i�C�O�j';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka_kaigai_gaika is '�����P���i�C�O�j�O��';
comment on column kaigai_ryohi_karibarai.hf1_cd is 'HF1�R�[�h';
comment on column kaigai_ryohi_karibarai.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column kaigai_ryohi_karibarai.hf2_cd is 'HF2�R�[�h';
comment on column kaigai_ryohi_karibarai.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column kaigai_ryohi_karibarai.hf3_cd is 'HF3�R�[�h';
comment on column kaigai_ryohi_karibarai.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column kaigai_ryohi_karibarai.hf4_cd is 'HF4�R�[�h';
comment on column kaigai_ryohi_karibarai.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column kaigai_ryohi_karibarai.hf5_cd is 'HF5�R�[�h';
comment on column kaigai_ryohi_karibarai.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column kaigai_ryohi_karibarai.hf6_cd is 'HF6�R�[�h';
comment on column kaigai_ryohi_karibarai.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column kaigai_ryohi_karibarai.hf7_cd is 'HF7�R�[�h';
comment on column kaigai_ryohi_karibarai.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column kaigai_ryohi_karibarai.hf8_cd is 'HF8�R�[�h';
comment on column kaigai_ryohi_karibarai.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column kaigai_ryohi_karibarai.hf9_cd is 'HF9�R�[�h';
comment on column kaigai_ryohi_karibarai.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column kaigai_ryohi_karibarai.hf10_cd is 'HF10�R�[�h';
comment on column kaigai_ryohi_karibarai.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column kaigai_ryohi_karibarai.hosoku is '�⑫';
comment on column kaigai_ryohi_karibarai.shiwake_edano is '�d��}�ԍ�';
comment on column kaigai_ryohi_karibarai.torihiki_name is '�����';
comment on column kaigai_ryohi_karibarai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column kaigai_ryohi_karibarai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column kaigai_ryohi_karibarai.torihikisaki_cd is '�����R�[�h';
comment on column kaigai_ryohi_karibarai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column kaigai_ryohi_karibarai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column kaigai_ryohi_karibarai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column kaigai_ryohi_karibarai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohi_karibarai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohi_karibarai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column kaigai_ryohi_karibarai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column kaigai_ryohi_karibarai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohi_karibarai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column kaigai_ryohi_karibarai.uf1_cd is 'UF1�R�[�h';
comment on column kaigai_ryohi_karibarai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column kaigai_ryohi_karibarai.uf2_cd is 'UF2�R�[�h';
comment on column kaigai_ryohi_karibarai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column kaigai_ryohi_karibarai.uf3_cd is 'UF3�R�[�h';
comment on column kaigai_ryohi_karibarai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column kaigai_ryohi_karibarai.uf4_cd is 'UF4�R�[�h';
comment on column kaigai_ryohi_karibarai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column kaigai_ryohi_karibarai.uf5_cd is 'UF5�R�[�h';
comment on column kaigai_ryohi_karibarai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column kaigai_ryohi_karibarai.uf6_cd is 'UF6�R�[�h';
comment on column kaigai_ryohi_karibarai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column kaigai_ryohi_karibarai.uf7_cd is 'UF7�R�[�h';
comment on column kaigai_ryohi_karibarai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column kaigai_ryohi_karibarai.uf8_cd is 'UF8�R�[�h';
comment on column kaigai_ryohi_karibarai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column kaigai_ryohi_karibarai.uf9_cd is 'UF9�R�[�h';
comment on column kaigai_ryohi_karibarai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column kaigai_ryohi_karibarai.uf10_cd is 'UF10�R�[�h';
comment on column kaigai_ryohi_karibarai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column kaigai_ryohi_karibarai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column kaigai_ryohi_karibarai.project_name is '�v���W�F�N�g��';
comment on column kaigai_ryohi_karibarai.segment_cd is '�Z�O�����g�R�[�h';
comment on column kaigai_ryohi_karibarai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column kaigai_ryohi_karibarai.tekiyou_cd is '�E�v�R�[�h';
comment on column kaigai_ryohi_karibarai.seisan_kanryoubi is '���Z������';
comment on column kaigai_ryohi_karibarai.touroku_user_id is '�o�^���[�U�[ID';
comment on column kaigai_ryohi_karibarai.touroku_time is '�o�^����';
comment on column kaigai_ryohi_karibarai.koushin_user_id is '�X�V���[�U�[ID';
comment on column kaigai_ryohi_karibarai.koushin_time is '�X�V����';
INSERT INTO kaigai_ryohi_karibarai
SELECT
	 denpyou_id
	,karibarai_on
	,dairiflg
	,user_id
	,shain_no
	,user_sei
	,user_mei
	,houmonsaki
	,mokuteki
	,seisankikan_from
	,seisankikan_from_hour
	,seisankikan_from_min
	,seisankikan_to
	,seisankikan_to_hour
	,seisankikan_to_min
	,shiharaibi
	,shiharaikiboubi
	,shiharaihouhou
	,tekiyou
	,kingaku
	,karibarai_kingaku
	,sashihiki_num
	,sashihiki_tanka
	,sashihiki_num_kaigai
	,sashihiki_tanka_kaigai
	,''		--��������R�[�h�i�C�O�j
	,null	--�������[�g�i�C�O�j
	,null	--�����P���i�C�O�j�O��
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
	,hosoku
	,shiwake_edano
	,torihiki_name
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,seisan_kanryoubi
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM kaigai_ryohi_karibarai_old;
DROP TABLE kaigai_ryohi_karibarai_old;


-- �y���ŗ��Ή� �C�O����Z�A0703_�C�O�p�����̊O�ݑΉ�
ALTER TABLE kaigai_ryohiseisan RENAME TO kaigai_ryohiseisan_old;
create table kaigai_ryohiseisan (
  denpyou_id character varying(19) not null
  , karibarai_denpyou_id character varying(19) not null
  , karibarai_on character varying(1) not null
  , karibarai_mishiyou_flg character varying(1) default '0' not null
  , shucchou_chuushi_flg character varying(1) default '0' not null
  , dairiflg character varying(1) not null
  , user_id character varying(30) not null
  , shain_no character varying(15) not null
  , user_sei character varying(10) not null
  , user_mei character varying(10) not null
  , houmonsaki character varying(120) not null
  , mokuteki character varying(240) not null
  , seisankikan_from date
  , seisankikan_from_hour character varying(2)
  , seisankikan_from_min character varying(2)
  , seisankikan_to date
  , seisankikan_to_hour character varying(2)
  , seisankikan_to_min character varying(2)
  , keijoubi date
  , shiharaibi date
  , shiharaikiboubi date
  , shiharaihouhou character varying(1) not null
  , tekiyou character varying(60) not null
  , kaigai_tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , goukei_kingaku numeric(15) not null
  , houjin_card_riyou_kingaku numeric(15) not null
  , kaisha_tehai_kingaku numeric(15) not null
  , sashihiki_shikyuu_kingaku numeric(15) not null
  , sashihiki_num numeric(2)
  , sashihiki_tanka numeric(6)
  , sashihiki_num_kaigai numeric(2)
  , sashihiki_tanka_kaigai numeric(6)
  , sashihiki_heishu_cd_kaigai character varying(4) not null
  , sashihiki_rate_kaigai numeric(11,5)
  , sashihiki_tanka_kaigai_gaika numeric(8,2)
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
  , hosoku character varying(240) not null
  , shiwake_edano integer
  , torihiki_name character varying(20) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , ryohi_kazei_flg character varying(1) not null
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
  , kaigai_shiwake_edano integer
  , kaigai_torihiki_name character varying(20) not null
  , kaigai_kari_futan_bumon_cd character varying(8) not null
  , kaigai_kari_futan_bumon_name character varying(20) not null
  , kaigai_torihikisaki_cd character varying(12) not null
  , kaigai_torihikisaki_name_ryakushiki character varying(20) not null
  , kaigai_kari_kamoku_cd character varying(6) not null
  , kaigai_kari_kamoku_name character varying(22) not null
  , kaigai_kari_kamoku_edaban_cd character varying(12) not null
  , kaigai_kari_kamoku_edaban_name character varying(20) not null
  , kaigai_kari_kazei_kbn character varying(3) not null
  , kaigai_kazei_flg character varying(1) not null
  , kaigai_kashi_futan_bumon_cd character varying(8) not null
  , kaigai_kashi_futan_bumon_name character varying(20) not null
  , kaigai_kashi_kamoku_cd character varying(6) not null
  , kaigai_kashi_kamoku_name character varying(22) not null
  , kaigai_kashi_kamoku_edaban_cd character varying(12) not null
  , kaigai_kashi_kamoku_edaban_name character varying(20) not null
  , kaigai_kashi_kazei_kbn character varying(3) not null
  , kaigai_uf1_cd character varying(20) not null
  , kaigai_uf1_name_ryakushiki character varying(20) not null
  , kaigai_uf2_cd character varying(20) not null
  , kaigai_uf2_name_ryakushiki character varying(20) not null
  , kaigai_uf3_cd character varying(20) not null
  , kaigai_uf3_name_ryakushiki character varying(20) not null
  , kaigai_uf4_cd character varying(20) not null
  , kaigai_uf4_name_ryakushiki character varying(20) not null
  , kaigai_uf5_cd character varying(20) not null
  , kaigai_uf5_name_ryakushiki character varying(20) not null
  , kaigai_uf6_cd character varying(20) not null
  , kaigai_uf6_name_ryakushiki character varying(20) not null
  , kaigai_uf7_cd character varying(20) not null
  , kaigai_uf7_name_ryakushiki character varying(20) not null
  , kaigai_uf8_cd character varying(20) not null
  , kaigai_uf8_name_ryakushiki character varying(20) not null
  , kaigai_uf9_cd character varying(20) not null
  , kaigai_uf9_name_ryakushiki character varying(20) not null
  , kaigai_uf10_cd character varying(20) not null
  , kaigai_uf10_name_ryakushiki character varying(20) not null
  , kaigai_project_cd character varying(12) not null
  , kaigai_project_name character varying(20) not null
  , kaigai_segment_cd character varying(8) not null
  , kaigai_segment_name_ryakushiki character varying(20) not null
  , kaigai_tekiyou_cd character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id)
);
comment on table kaigai_ryohiseisan is '�C�O����Z';
comment on column kaigai_ryohiseisan.denpyou_id is '�`�[ID';
comment on column kaigai_ryohiseisan.karibarai_denpyou_id is '�����`�[ID';
comment on column kaigai_ryohiseisan.karibarai_on is '�����\���t���O';
comment on column kaigai_ryohiseisan.karibarai_mishiyou_flg is '���������g�p�t���O';
comment on column kaigai_ryohiseisan.shucchou_chuushi_flg is '�o�����~�t���O';
comment on column kaigai_ryohiseisan.dairiflg is '�㗝�t���O';
comment on column kaigai_ryohiseisan.user_id is '���[�U�[ID';
comment on column kaigai_ryohiseisan.shain_no is '�Ј��ԍ�';
comment on column kaigai_ryohiseisan.user_sei is '���[�U�[��';
comment on column kaigai_ryohiseisan.user_mei is '���[�U�[��';
comment on column kaigai_ryohiseisan.houmonsaki is '�K���';
comment on column kaigai_ryohiseisan.mokuteki is '�ړI';
comment on column kaigai_ryohiseisan.seisankikan_from is '���Z���ԊJ�n��';
comment on column kaigai_ryohiseisan.seisankikan_from_hour is '���Z���ԊJ�n�����i���j';
comment on column kaigai_ryohiseisan.seisankikan_from_min is '���Z���ԊJ�n�����i���j';
comment on column kaigai_ryohiseisan.seisankikan_to is '���Z���ԏI����';
comment on column kaigai_ryohiseisan.seisankikan_to_hour is '���Z���ԏI�������i���j';
comment on column kaigai_ryohiseisan.seisankikan_to_min is '���Z���ԏI�������i���j';
comment on column kaigai_ryohiseisan.keijoubi is '�v���';
comment on column kaigai_ryohiseisan.shiharaibi is '�x����';
comment on column kaigai_ryohiseisan.shiharaikiboubi is '�x����]��';
comment on column kaigai_ryohiseisan.shiharaihouhou is '�x�����@';
comment on column kaigai_ryohiseisan.tekiyou is '�E�v';
comment on column kaigai_ryohiseisan.kaigai_tekiyou is '�E�v�i�C�O�j';
comment on column kaigai_ryohiseisan.zeiritsu is '�ŗ�';
comment on column kaigai_ryohiseisan.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column kaigai_ryohiseisan.goukei_kingaku is '���v���z';
comment on column kaigai_ryohiseisan.houjin_card_riyou_kingaku is '���@�l�J�[�h���p���v';
comment on column kaigai_ryohiseisan.kaisha_tehai_kingaku is '��Ў�z���v';
comment on column kaigai_ryohiseisan.sashihiki_shikyuu_kingaku is '�����x�����z';
comment on column kaigai_ryohiseisan.sashihiki_num is '������';
comment on column kaigai_ryohiseisan.sashihiki_tanka is '�����P��';
comment on column kaigai_ryohiseisan.sashihiki_num_kaigai is '�����񐔁i�C�O�j';
comment on column kaigai_ryohiseisan.sashihiki_tanka_kaigai is '�����P���i�C�O�j';
comment on column kaigai_ryohiseisan.sashihiki_heishu_cd_kaigai is '��������R�[�h�i�C�O�j';
comment on column kaigai_ryohiseisan.sashihiki_rate_kaigai is '�������[�g�i�C�O�j';
comment on column kaigai_ryohiseisan.sashihiki_tanka_kaigai_gaika is '�����P���i�C�O�j�O��';
comment on column kaigai_ryohiseisan.hf1_cd is 'HF1�R�[�h';
comment on column kaigai_ryohiseisan.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column kaigai_ryohiseisan.hf2_cd is 'HF2�R�[�h';
comment on column kaigai_ryohiseisan.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column kaigai_ryohiseisan.hf3_cd is 'HF3�R�[�h';
comment on column kaigai_ryohiseisan.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column kaigai_ryohiseisan.hf4_cd is 'HF4�R�[�h';
comment on column kaigai_ryohiseisan.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column kaigai_ryohiseisan.hf5_cd is 'HF5�R�[�h';
comment on column kaigai_ryohiseisan.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column kaigai_ryohiseisan.hf6_cd is 'HF6�R�[�h';
comment on column kaigai_ryohiseisan.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column kaigai_ryohiseisan.hf7_cd is 'HF7�R�[�h';
comment on column kaigai_ryohiseisan.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column kaigai_ryohiseisan.hf8_cd is 'HF8�R�[�h';
comment on column kaigai_ryohiseisan.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column kaigai_ryohiseisan.hf9_cd is 'HF9�R�[�h';
comment on column kaigai_ryohiseisan.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column kaigai_ryohiseisan.hf10_cd is 'HF10�R�[�h';
comment on column kaigai_ryohiseisan.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column kaigai_ryohiseisan.hosoku is '�⑫';
comment on column kaigai_ryohiseisan.shiwake_edano is '�d��}�ԍ�';
comment on column kaigai_ryohiseisan.torihiki_name is '�����';
comment on column kaigai_ryohiseisan.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column kaigai_ryohiseisan.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column kaigai_ryohiseisan.torihikisaki_cd is '�����R�[�h';
comment on column kaigai_ryohiseisan.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column kaigai_ryohiseisan.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column kaigai_ryohiseisan.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column kaigai_ryohiseisan.ryohi_kazei_flg is '����ېŃt���O';
comment on column kaigai_ryohiseisan.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column kaigai_ryohiseisan.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column kaigai_ryohiseisan.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column kaigai_ryohiseisan.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column kaigai_ryohiseisan.uf1_cd is 'UF1�R�[�h';
comment on column kaigai_ryohiseisan.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column kaigai_ryohiseisan.uf2_cd is 'UF2�R�[�h';
comment on column kaigai_ryohiseisan.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column kaigai_ryohiseisan.uf3_cd is 'UF3�R�[�h';
comment on column kaigai_ryohiseisan.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column kaigai_ryohiseisan.uf4_cd is 'UF4�R�[�h';
comment on column kaigai_ryohiseisan.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column kaigai_ryohiseisan.uf5_cd is 'UF5�R�[�h';
comment on column kaigai_ryohiseisan.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column kaigai_ryohiseisan.uf6_cd is 'UF6�R�[�h';
comment on column kaigai_ryohiseisan.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column kaigai_ryohiseisan.uf7_cd is 'UF7�R�[�h';
comment on column kaigai_ryohiseisan.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column kaigai_ryohiseisan.uf8_cd is 'UF8�R�[�h';
comment on column kaigai_ryohiseisan.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column kaigai_ryohiseisan.uf9_cd is 'UF9�R�[�h';
comment on column kaigai_ryohiseisan.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column kaigai_ryohiseisan.uf10_cd is 'UF10�R�[�h';
comment on column kaigai_ryohiseisan.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column kaigai_ryohiseisan.project_cd is '�v���W�F�N�g�R�[�h';
comment on column kaigai_ryohiseisan.project_name is '�v���W�F�N�g��';
comment on column kaigai_ryohiseisan.segment_cd is '�Z�O�����g�R�[�h';
comment on column kaigai_ryohiseisan.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column kaigai_ryohiseisan.tekiyou_cd is '�E�v�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_shiwake_edano is '�C�O�d��}�ԍ�';
comment on column kaigai_ryohiseisan.kaigai_torihiki_name is '�C�O�����';
comment on column kaigai_ryohiseisan.kaigai_kari_futan_bumon_cd is '�C�O�ؕ����S����R�[�h';
comment on column kaigai_ryohiseisan.kaigai_kari_futan_bumon_name is '�C�O�ؕ����S���喼';
comment on column kaigai_ryohiseisan.kaigai_torihikisaki_cd is '�C�O�����R�[�h';
comment on column kaigai_ryohiseisan.kaigai_torihikisaki_name_ryakushiki is '�C�O����於�i�����j';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_cd is '�C�O�ؕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_name is '�C�O�ؕ��Ȗږ�';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_edaban_cd is '�C�O�ؕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_edaban_name is '�C�O�ؕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan.kaigai_kari_kazei_kbn is '�C�O�ؕ��ېŋ敪';
comment on column kaigai_ryohiseisan.kaigai_kazei_flg is '�C�O�ېŃt���O';
comment on column kaigai_ryohiseisan.kaigai_kashi_futan_bumon_cd is '�C�O�ݕ����S����R�[�h';
comment on column kaigai_ryohiseisan.kaigai_kashi_futan_bumon_name is '�C�O�ݕ����S���喼';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_cd is '�C�O�ݕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_name is '�C�O�ݕ��Ȗږ�';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_edaban_cd is '�C�O�ݕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_edaban_name is '�C�O�ݕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan.kaigai_kashi_kazei_kbn is '�C�O�ݕ��ېŋ敪';
comment on column kaigai_ryohiseisan.kaigai_uf1_cd is '�C�OUF1�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf1_name_ryakushiki is '�C�OUF1���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf2_cd is '�C�OUF2�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf2_name_ryakushiki is '�C�OUF2���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf3_cd is '�C�OUF3�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf3_name_ryakushiki is '�C�OUF3���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf4_cd is '�C�OUF4�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf4_name_ryakushiki is '�C�OUF4���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf5_cd is '�C�OUF5�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf5_name_ryakushiki is '�C�OUF5���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf6_cd is '�C�OUF6�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf6_name_ryakushiki is '�C�OUF6���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf7_cd is '�C�OUF7�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf7_name_ryakushiki is '�C�OUF7���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf8_cd is '�C�OUF8�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf8_name_ryakushiki is '�C�OUF8���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf9_cd is '�C�OUF9�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf9_name_ryakushiki is '�C�OUF9���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf10_cd is '�C�OUF10�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf10_name_ryakushiki is '�C�OUF10���i�����j';
comment on column kaigai_ryohiseisan.kaigai_project_cd is '�C�O�v���W�F�N�g�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_project_name is '�C�O�v���W�F�N�g��';
comment on column kaigai_ryohiseisan.kaigai_segment_cd is '�C�O�Z�O�����g�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_segment_name_ryakushiki is '�C�O�Z�O�����g���i�����j';
comment on column kaigai_ryohiseisan.kaigai_tekiyou_cd is '�C�O�E�v�R�[�h';
comment on column kaigai_ryohiseisan.touroku_user_id is '�o�^���[�U�[ID';
comment on column kaigai_ryohiseisan.touroku_time is '�o�^����';
comment on column kaigai_ryohiseisan.koushin_user_id is '�X�V���[�U�[ID';
comment on column kaigai_ryohiseisan.koushin_time is '�X�V����';
INSERT INTO kaigai_ryohiseisan
SELECT
	 denpyou_id
	,karibarai_denpyou_id
	,karibarai_on
	,karibarai_mishiyou_flg
	,shucchou_chuushi_flg
	,dairiflg
	,user_id
	,shain_no
	,user_sei
	,user_mei
	,houmonsaki
	,mokuteki
	,seisankikan_from
	,seisankikan_from_hour
	,seisankikan_from_min
	,seisankikan_to
	,seisankikan_to_hour
	,seisankikan_to_min
	,keijoubi
	,shiharaibi
	,shiharaikiboubi
	,shiharaihouhou
	,tekiyou
	,kaigai_tekiyou
	,zeiritsu
	,'0'	--�y���ŗ��敪
	,goukei_kingaku
	,houjin_card_riyou_kingaku
	,kaisha_tehai_kingaku
	,sashihiki_shikyuu_kingaku
	,sashihiki_num
	,sashihiki_tanka
	,sashihiki_num_kaigai
	,sashihiki_tanka_kaigai
	,''		--��������R�[�h�i�C�O�j
	,null	--�������[�g�i�C�O�j
	,null	--�����P���i�C�O�j�O��
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
	,hosoku
	,shiwake_edano
	,torihiki_name
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,ryohi_kazei_flg
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,kaigai_shiwake_edano
	,kaigai_torihiki_name
	,kaigai_kari_futan_bumon_cd
	,kaigai_kari_futan_bumon_name
	,kaigai_torihikisaki_cd
	,kaigai_torihikisaki_name_ryakushiki
	,kaigai_kari_kamoku_cd
	,kaigai_kari_kamoku_name
	,kaigai_kari_kamoku_edaban_cd
	,kaigai_kari_kamoku_edaban_name
	,kaigai_kari_kazei_kbn
	,kaigai_kazei_flg
	,kaigai_kashi_futan_bumon_cd
	,kaigai_kashi_futan_bumon_name
	,kaigai_kashi_kamoku_cd
	,kaigai_kashi_kamoku_name
	,kaigai_kashi_kamoku_edaban_cd
	,kaigai_kashi_kamoku_edaban_name
	,kaigai_kashi_kazei_kbn
	,kaigai_uf1_cd
	,kaigai_uf1_name_ryakushiki
	,kaigai_uf2_cd
	,kaigai_uf2_name_ryakushiki
	,kaigai_uf3_cd
	,kaigai_uf3_name_ryakushiki
	,kaigai_uf4_cd
	,kaigai_uf4_name_ryakushiki
	,kaigai_uf5_cd
	,kaigai_uf5_name_ryakushiki
	,kaigai_uf6_cd
	,kaigai_uf6_name_ryakushiki
	,kaigai_uf7_cd
	,kaigai_uf7_name_ryakushiki
	,kaigai_uf8_cd
	,kaigai_uf8_name_ryakushiki
	,kaigai_uf9_cd
	,kaigai_uf9_name_ryakushiki
	,kaigai_uf10_cd
	,kaigai_uf10_name_ryakushiki
	,kaigai_project_cd
	,kaigai_project_name
	,kaigai_segment_cd
	,kaigai_segment_name_ryakushiki
	,kaigai_tekiyou_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM kaigai_ryohiseisan_old;
DROP TABLE kaigai_ryohiseisan_old;


-- �y���ŗ��Ή� ��ʔ�Z
ALTER TABLE koutsuuhiseisan RENAME TO koutsuuhiseisan_old;
create table koutsuuhiseisan (
  denpyou_id character varying(19) not null
  , mokuteki character varying(240) not null
  , seisankikan_from date
  , seisankikan_from_hour character varying(2)
  , seisankikan_from_min character varying(2)
  , seisankikan_to date
  , seisankikan_to_hour character varying(2)
  , seisankikan_to_min character varying(2)
  , keijoubi date
  , shiharaibi date
  , shiharaikiboubi date
  , shiharaihouhou character varying(1) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , goukei_kingaku numeric(15) not null
  , houjin_card_riyou_kingaku numeric(15) not null
  , kaisha_tehai_kingaku numeric(15) not null
  , sashihiki_shikyuu_kingaku numeric(15) not null
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
  , hosoku character varying(240) not null
  , shiwake_edano integer
  , torihiki_name character varying(20) not null
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
  , primary key (denpyou_id)
);
comment on table koutsuuhiseisan is '��ʔ�Z';
comment on column koutsuuhiseisan.denpyou_id is '�`�[ID';
comment on column koutsuuhiseisan.mokuteki is '�ړI';
comment on column koutsuuhiseisan.seisankikan_from is '���Z���ԊJ�n��';
comment on column koutsuuhiseisan.seisankikan_from_hour is '���Z���ԊJ�n�����i���j';
comment on column koutsuuhiseisan.seisankikan_from_min is '���Z���ԊJ�n�����i���j';
comment on column koutsuuhiseisan.seisankikan_to is '���Z���ԏI����';
comment on column koutsuuhiseisan.seisankikan_to_hour is '���Z���ԏI�������i���j';
comment on column koutsuuhiseisan.seisankikan_to_min is '���Z���ԏI�������i���j';
comment on column koutsuuhiseisan.keijoubi is '�v���';
comment on column koutsuuhiseisan.shiharaibi is '�x����';
comment on column koutsuuhiseisan.shiharaikiboubi is '�x����]��';
comment on column koutsuuhiseisan.shiharaihouhou is '�x�����@';
comment on column koutsuuhiseisan.tekiyou is '�E�v';
comment on column koutsuuhiseisan.zeiritsu is '�ŗ�';
comment on column koutsuuhiseisan.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column koutsuuhiseisan.goukei_kingaku is '���v���z';
comment on column koutsuuhiseisan.houjin_card_riyou_kingaku is '���@�l�J�[�h���p���v';
comment on column koutsuuhiseisan.kaisha_tehai_kingaku is '��Ў�z���v';
comment on column koutsuuhiseisan.sashihiki_shikyuu_kingaku is '�����x�����z';
comment on column koutsuuhiseisan.hf1_cd is 'HF1�R�[�h';
comment on column koutsuuhiseisan.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column koutsuuhiseisan.hf2_cd is 'HF2�R�[�h';
comment on column koutsuuhiseisan.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column koutsuuhiseisan.hf3_cd is 'HF3�R�[�h';
comment on column koutsuuhiseisan.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column koutsuuhiseisan.hf4_cd is 'HF4�R�[�h';
comment on column koutsuuhiseisan.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column koutsuuhiseisan.hf5_cd is 'HF5�R�[�h';
comment on column koutsuuhiseisan.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column koutsuuhiseisan.hf6_cd is 'HF6�R�[�h';
comment on column koutsuuhiseisan.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column koutsuuhiseisan.hf7_cd is 'HF7�R�[�h';
comment on column koutsuuhiseisan.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column koutsuuhiseisan.hf8_cd is 'HF8�R�[�h';
comment on column koutsuuhiseisan.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column koutsuuhiseisan.hf9_cd is 'HF9�R�[�h';
comment on column koutsuuhiseisan.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column koutsuuhiseisan.hf10_cd is 'HF10�R�[�h';
comment on column koutsuuhiseisan.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column koutsuuhiseisan.hosoku is '�⑫';
comment on column koutsuuhiseisan.shiwake_edano is '�d��}�ԍ�';
comment on column koutsuuhiseisan.torihiki_name is '�����';
comment on column koutsuuhiseisan.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column koutsuuhiseisan.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column koutsuuhiseisan.torihikisaki_cd is '�����R�[�h';
comment on column koutsuuhiseisan.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column koutsuuhiseisan.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column koutsuuhiseisan.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column koutsuuhiseisan.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column koutsuuhiseisan.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column koutsuuhiseisan.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column koutsuuhiseisan.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column koutsuuhiseisan.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column koutsuuhiseisan.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column koutsuuhiseisan.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column koutsuuhiseisan.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column koutsuuhiseisan.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column koutsuuhiseisan.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column koutsuuhiseisan.uf1_cd is 'UF1�R�[�h';
comment on column koutsuuhiseisan.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column koutsuuhiseisan.uf2_cd is 'UF2�R�[�h';
comment on column koutsuuhiseisan.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column koutsuuhiseisan.uf3_cd is 'UF3�R�[�h';
comment on column koutsuuhiseisan.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column koutsuuhiseisan.uf4_cd is 'UF4�R�[�h';
comment on column koutsuuhiseisan.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column koutsuuhiseisan.uf5_cd is 'UF5�R�[�h';
comment on column koutsuuhiseisan.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column koutsuuhiseisan.uf6_cd is 'UF6�R�[�h';
comment on column koutsuuhiseisan.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column koutsuuhiseisan.uf7_cd is 'UF7�R�[�h';
comment on column koutsuuhiseisan.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column koutsuuhiseisan.uf8_cd is 'UF8�R�[�h';
comment on column koutsuuhiseisan.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column koutsuuhiseisan.uf9_cd is 'UF9�R�[�h';
comment on column koutsuuhiseisan.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column koutsuuhiseisan.uf10_cd is 'UF10�R�[�h';
comment on column koutsuuhiseisan.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column koutsuuhiseisan.project_cd is '�v���W�F�N�g�R�[�h';
comment on column koutsuuhiseisan.project_name is '�v���W�F�N�g��';
comment on column koutsuuhiseisan.segment_cd is '�Z�O�����g�R�[�h';
comment on column koutsuuhiseisan.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column koutsuuhiseisan.tekiyou_cd is '�E�v�R�[�h';
comment on column koutsuuhiseisan.touroku_user_id is '�o�^���[�U�[ID';
comment on column koutsuuhiseisan.touroku_time is '�o�^����';
comment on column koutsuuhiseisan.koushin_user_id is '�X�V���[�U�[ID';
comment on column koutsuuhiseisan.koushin_time is '�X�V����';
INSERT INTO koutsuuhiseisan
SELECT
	 denpyou_id
	,mokuteki
	,seisankikan_from
	,seisankikan_from_hour
	,seisankikan_from_min
	,seisankikan_to
	,seisankikan_to_hour
	,seisankikan_to_min
	,keijoubi
	,shiharaibi
	,shiharaikiboubi
	,shiharaihouhou
	,tekiyou
	,zeiritsu
	,'0'	--�y���ŗ��敪
	,goukei_kingaku
	,houjin_card_riyou_kingaku
	,kaisha_tehai_kingaku
	,sashihiki_shikyuu_kingaku
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
	,hosoku
	,shiwake_edano
	,torihiki_name
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM koutsuuhiseisan_old;
DROP TABLE koutsuuhiseisan_old;


-- �y���ŗ��Ή� �C�O������o���
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
  , kousaihi_shousai character varying(240)
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
  , primary key (denpyou_id,denpyou_edano,kaigai_flg)
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
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
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
	,denpyou_edano
	,kaigai_flg
	,shiwake_edano
	,shiyoubi
	,shouhyou_shorui_flg
	,torihiki_name
	,tekiyou
	,zeiritsu
	,'0'	--�y���ŗ��敪
	,kazei_flg
	,shiharai_kingaku
	,hontai_kingaku
	,shouhizeigaku
	,kousaihi_shousai_hyouji_flg
	,kousaihi_shousai
	,heishu_cd
	,rate
	,gaika
	,currency_unit
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM kaigai_ryohi_karibarai_keihi_meisai_old;
DROP TABLE kaigai_ryohi_karibarai_keihi_meisai_old;


-- �y���ŗ��Ή� �C�O����Z�o���
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
  , kousaihi_shousai character varying(240)
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
  , primary key (denpyou_id,denpyou_edano,kaigai_flg)
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
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
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
	,denpyou_edano
	,kaigai_flg
	,shiwake_edano
	,shiyoubi
	,shouhyou_shorui_flg
	,torihiki_name
	,tekiyou
	,zeiritsu
	,'0'	--�y���ŗ��敪
	,kazei_flg
	,shiharai_kingaku
	,hontai_kingaku
	,shouhizeigaku
	,houjin_card_riyou_flg
	,kaisha_tehai_flg
	,kousaihi_shousai_hyouji_flg
	,kousaihi_shousai
	,heishu_cd
	,rate
	,gaika
	,currency_unit
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,himoduke_card_meisai
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM kaigai_ryohiseisan_keihi_meisai_old;
DROP TABLE kaigai_ryohiseisan_keihi_meisai_old;


-- �y���ŗ��Ή� ����Z�o���
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
  , kousaihi_shousai character varying(240)
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
  , primary key (denpyou_id,denpyou_edano)
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
comment on column ryohiseisan_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
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
	,denpyou_edano
	,shiwake_edano
	,shiyoubi
	,shouhyou_shorui_flg
	,torihiki_name
	,tekiyou
	,zeiritsu
	,'0'	--�y���ŗ��敪
	,shiharai_kingaku
	,hontai_kingaku
	,shouhizeigaku
	,houjin_card_riyou_flg
	,kaisha_tehai_flg
	,kousaihi_shousai_hyouji_flg
	,kousaihi_shousai
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,himoduke_card_meisai
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM ryohiseisan_keihi_meisai_old;
DROP TABLE ryohiseisan_keihi_meisai_old;


-- �y���ŗ��Ή� ����Z
ALTER TABLE ryohiseisan RENAME TO ryohiseisan_old;
create table ryohiseisan (
  denpyou_id character varying(19) not null
  , karibarai_denpyou_id character varying(19) not null
  , karibarai_on character varying(1) not null
  , karibarai_mishiyou_flg character varying(1) default '0' not null
  , shucchou_chuushi_flg character varying(1) default '0' not null
  , dairiflg character varying(1) not null
  , user_id character varying(30) not null
  , shain_no character varying(15) not null
  , user_sei character varying(10) not null
  , user_mei character varying(10) not null
  , houmonsaki character varying(120) not null
  , mokuteki character varying(240) not null
  , seisankikan_from date
  , seisankikan_from_hour character varying(2)
  , seisankikan_from_min character varying(2)
  , seisankikan_to date
  , seisankikan_to_hour character varying(2)
  , seisankikan_to_min character varying(2)
  , keijoubi date
  , shiharaibi date
  , shiharaikiboubi date
  , shiharaihouhou character varying(1) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , goukei_kingaku numeric(15) not null
  , houjin_card_riyou_kingaku numeric(15) not null
  , kaisha_tehai_kingaku numeric(15) not null
  , sashihiki_shikyuu_kingaku numeric(15) not null
  , sashihiki_num numeric(2)
  , sashihiki_tanka numeric(6)
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
  , hosoku character varying(240) not null
  , shiwake_edano integer
  , torihiki_name character varying(20) not null
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
  , primary key (denpyou_id)
);
comment on table ryohiseisan is '����Z';
comment on column ryohiseisan.denpyou_id is '�`�[ID';
comment on column ryohiseisan.karibarai_denpyou_id is '�����`�[ID';
comment on column ryohiseisan.karibarai_on is '�����\���t���O';
comment on column ryohiseisan.karibarai_mishiyou_flg is '���������g�p�t���O';
comment on column ryohiseisan.shucchou_chuushi_flg is '�o�����~�t���O';
comment on column ryohiseisan.dairiflg is '�㗝�t���O';
comment on column ryohiseisan.user_id is '���[�U�[ID';
comment on column ryohiseisan.shain_no is '�Ј��ԍ�';
comment on column ryohiseisan.user_sei is '���[�U�[��';
comment on column ryohiseisan.user_mei is '���[�U�[��';
comment on column ryohiseisan.houmonsaki is '�K���';
comment on column ryohiseisan.mokuteki is '�ړI';
comment on column ryohiseisan.seisankikan_from is '���Z���ԊJ�n��';
comment on column ryohiseisan.seisankikan_from_hour is '���Z���ԊJ�n�����i���j';
comment on column ryohiseisan.seisankikan_from_min is '���Z���ԊJ�n�����i���j';
comment on column ryohiseisan.seisankikan_to is '���Z���ԏI����';
comment on column ryohiseisan.seisankikan_to_hour is '���Z���ԏI�������i���j';
comment on column ryohiseisan.seisankikan_to_min is '���Z���ԏI�������i���j';
comment on column ryohiseisan.keijoubi is '�v���';
comment on column ryohiseisan.shiharaibi is '�x����';
comment on column ryohiseisan.shiharaikiboubi is '�x����]��';
comment on column ryohiseisan.shiharaihouhou is '�x�����@';
comment on column ryohiseisan.tekiyou is '�E�v';
comment on column ryohiseisan.zeiritsu is '�ŗ�';
comment on column ryohiseisan.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column ryohiseisan.goukei_kingaku is '���v���z';
comment on column ryohiseisan.houjin_card_riyou_kingaku is '���@�l�J�[�h���p���v';
comment on column ryohiseisan.kaisha_tehai_kingaku is '��Ў�z���v';
comment on column ryohiseisan.sashihiki_shikyuu_kingaku is '�����x�����z';
comment on column ryohiseisan.sashihiki_num is '������';
comment on column ryohiseisan.sashihiki_tanka is '�����P��';
comment on column ryohiseisan.hf1_cd is 'HF1�R�[�h';
comment on column ryohiseisan.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column ryohiseisan.hf2_cd is 'HF2�R�[�h';
comment on column ryohiseisan.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column ryohiseisan.hf3_cd is 'HF3�R�[�h';
comment on column ryohiseisan.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column ryohiseisan.hf4_cd is 'HF4�R�[�h';
comment on column ryohiseisan.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column ryohiseisan.hf5_cd is 'HF5�R�[�h';
comment on column ryohiseisan.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column ryohiseisan.hf6_cd is 'HF6�R�[�h';
comment on column ryohiseisan.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column ryohiseisan.hf7_cd is 'HF7�R�[�h';
comment on column ryohiseisan.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column ryohiseisan.hf8_cd is 'HF8�R�[�h';
comment on column ryohiseisan.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column ryohiseisan.hf9_cd is 'HF9�R�[�h';
comment on column ryohiseisan.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column ryohiseisan.hf10_cd is 'HF10�R�[�h';
comment on column ryohiseisan.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column ryohiseisan.hosoku is '�⑫';
comment on column ryohiseisan.shiwake_edano is '�d��}�ԍ�';
comment on column ryohiseisan.torihiki_name is '�����';
comment on column ryohiseisan.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column ryohiseisan.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column ryohiseisan.torihikisaki_cd is '�����R�[�h';
comment on column ryohiseisan.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column ryohiseisan.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column ryohiseisan.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column ryohiseisan.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column ryohiseisan.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column ryohiseisan.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column ryohiseisan.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column ryohiseisan.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column ryohiseisan.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column ryohiseisan.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column ryohiseisan.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column ryohiseisan.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column ryohiseisan.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column ryohiseisan.uf1_cd is 'UF1�R�[�h';
comment on column ryohiseisan.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column ryohiseisan.uf2_cd is 'UF2�R�[�h';
comment on column ryohiseisan.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column ryohiseisan.uf3_cd is 'UF3�R�[�h';
comment on column ryohiseisan.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column ryohiseisan.uf4_cd is 'UF4�R�[�h';
comment on column ryohiseisan.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column ryohiseisan.uf5_cd is 'UF5�R�[�h';
comment on column ryohiseisan.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column ryohiseisan.uf6_cd is 'UF6�R�[�h';
comment on column ryohiseisan.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column ryohiseisan.uf7_cd is 'UF7�R�[�h';
comment on column ryohiseisan.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column ryohiseisan.uf8_cd is 'UF8�R�[�h';
comment on column ryohiseisan.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column ryohiseisan.uf9_cd is 'UF9�R�[�h';
comment on column ryohiseisan.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column ryohiseisan.uf10_cd is 'UF10�R�[�h';
comment on column ryohiseisan.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column ryohiseisan.project_cd is '�v���W�F�N�g�R�[�h';
comment on column ryohiseisan.project_name is '�v���W�F�N�g��';
comment on column ryohiseisan.segment_cd is '�Z�O�����g�R�[�h';
comment on column ryohiseisan.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column ryohiseisan.tekiyou_cd is '�E�v�R�[�h';
comment on column ryohiseisan.touroku_user_id is '�o�^���[�U�[ID';
comment on column ryohiseisan.touroku_time is '�o�^����';
comment on column ryohiseisan.koushin_user_id is '�X�V���[�U�[ID';
comment on column ryohiseisan.koushin_time is '�X�V����';
INSERT INTO ryohiseisan
SELECT
	 denpyou_id
	,karibarai_denpyou_id
	,karibarai_on
	,karibarai_mishiyou_flg
	,shucchou_chuushi_flg
	,dairiflg
	,user_id
	,shain_no
	,user_sei
	,user_mei
	,houmonsaki
	,mokuteki
	,seisankikan_from
	,seisankikan_from_hour
	,seisankikan_from_min
	,seisankikan_to
	,seisankikan_to_hour
	,seisankikan_to_min
	,keijoubi
	,shiharaibi
	,shiharaikiboubi
	,shiharaihouhou
	,tekiyou
	,zeiritsu
	,'0'	--�y���ŗ��敪
	,goukei_kingaku
	,houjin_card_riyou_kingaku
	,kaisha_tehai_kingaku
	,sashihiki_shikyuu_kingaku
	,sashihiki_num
	,sashihiki_tanka
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
	,hosoku
	,shiwake_edano
	,torihiki_name
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM ryohiseisan_old;
DROP TABLE ryohiseisan_old;


-- �y���ŗ��Ή� ������o���
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
  , kousaihi_shousai character varying(240)
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
  , primary key (denpyou_id,denpyou_edano)
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
INSERT INTO ryohi_karibarai_keihi_meisai
SELECT
	 denpyou_id
	,denpyou_edano
	,shiwake_edano
	,shiyoubi
	,shouhyou_shorui_flg
	,torihiki_name
	,tekiyou
	,zeiritsu
	,'0'	--�y���ŗ��敪
	,shiharai_kingaku
	,hontai_kingaku
	,shouhizeigaku
	,kousaihi_shousai_hyouji_flg
	,kousaihi_shousai
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM ryohi_karibarai_keihi_meisai_old;
DROP TABLE ryohi_karibarai_keihi_meisai_old;


--- �y���ŗ��Ή� ��������������
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
  , kousaihi_shousai character varying(240)
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
  , primary key (denpyou_id,denpyou_edano)
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
comment on column seikyuushobarai_meisai.kousaihi_shousai is '���۔�ڍ�';
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
	,denpyou_edano
	,shiwake_edano
	,torihiki_name
	,tekiyou
	,zeiritsu
	,'0'	--�y���ŗ��敪
	,shiharai_kingaku
	,hontai_kingaku
	,shouhizeigaku
	,kousaihi_shousai_hyouji_flg
	,kousaihi_shousai
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,furikomisaki_jouhou
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM seikyuushobarai_meisai_old;
DROP TABLE seikyuushobarai_meisai_old;


--- �y���ŗ��Ή� �o��Z����
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
  , kousaihi_shousai character varying(240)
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
  , primary key (denpyou_id,denpyou_edano)
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
comment on column keihiseisan_meisai.kousaihi_shousai is '���۔�ڍ�';
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
	,denpyou_edano
	,shiwake_edano
	,user_id
	,shain_no
	,user_sei
	,user_mei
	,shiyoubi
	,shouhyou_shorui_flg
	,torihiki_name
	,tekiyou
	,zeiritsu
	,'0'	--�y���ŗ��敪
	,shiharai_kingaku
	,hontai_kingaku
	,shouhizeigaku
	,houjin_card_riyou_flg
	,kaisha_tehai_flg
	,kousaihi_shousai_hyouji_flg
	,kousaihi_shousai
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,himoduke_card_meisai
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM keihiseisan_meisai_old;
DROP TABLE keihiseisan_meisai_old;


--- �y���ŗ��Ή� ������������
ALTER TABLE jidouhikiotoshi_meisai RENAME TO jidouhikiotoshi_meisai_old;
create table jidouhikiotoshi_meisai (
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
  , primary key (denpyou_id,denpyou_edano)
);
comment on table jidouhikiotoshi_meisai is '������������';
comment on column jidouhikiotoshi_meisai.denpyou_id is '�`�[ID';
comment on column jidouhikiotoshi_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column jidouhikiotoshi_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column jidouhikiotoshi_meisai.torihiki_name is '�����';
comment on column jidouhikiotoshi_meisai.tekiyou is '�E�v';
comment on column jidouhikiotoshi_meisai.zeiritsu is '�ŗ�';
comment on column jidouhikiotoshi_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column jidouhikiotoshi_meisai.shiharai_kingaku is '�x�����z';
comment on column jidouhikiotoshi_meisai.hontai_kingaku is '�{�̋��z';
comment on column jidouhikiotoshi_meisai.shouhizeigaku is '����Ŋz';
comment on column jidouhikiotoshi_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column jidouhikiotoshi_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column jidouhikiotoshi_meisai.torihikisaki_cd is '�����R�[�h';
comment on column jidouhikiotoshi_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column jidouhikiotoshi_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column jidouhikiotoshi_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column jidouhikiotoshi_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column jidouhikiotoshi_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column jidouhikiotoshi_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column jidouhikiotoshi_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column jidouhikiotoshi_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column jidouhikiotoshi_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column jidouhikiotoshi_meisai.uf1_cd is 'UF1�R�[�h';
comment on column jidouhikiotoshi_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column jidouhikiotoshi_meisai.uf2_cd is 'UF2�R�[�h';
comment on column jidouhikiotoshi_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column jidouhikiotoshi_meisai.uf3_cd is 'UF3�R�[�h';
comment on column jidouhikiotoshi_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column jidouhikiotoshi_meisai.uf4_cd is 'UF4�R�[�h';
comment on column jidouhikiotoshi_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column jidouhikiotoshi_meisai.uf5_cd is 'UF5�R�[�h';
comment on column jidouhikiotoshi_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column jidouhikiotoshi_meisai.uf6_cd is 'UF6�R�[�h';
comment on column jidouhikiotoshi_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column jidouhikiotoshi_meisai.uf7_cd is 'UF7�R�[�h';
comment on column jidouhikiotoshi_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column jidouhikiotoshi_meisai.uf8_cd is 'UF8�R�[�h';
comment on column jidouhikiotoshi_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column jidouhikiotoshi_meisai.uf9_cd is 'UF9�R�[�h';
comment on column jidouhikiotoshi_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column jidouhikiotoshi_meisai.uf10_cd is 'UF10�R�[�h';
comment on column jidouhikiotoshi_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column jidouhikiotoshi_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column jidouhikiotoshi_meisai.project_name is '�v���W�F�N�g��';
comment on column jidouhikiotoshi_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column jidouhikiotoshi_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column jidouhikiotoshi_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column jidouhikiotoshi_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column jidouhikiotoshi_meisai.touroku_time is '�o�^����';
comment on column jidouhikiotoshi_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column jidouhikiotoshi_meisai.koushin_time is '�X�V����';
INSERT INTO jidouhikiotoshi_meisai
SELECT
	 denpyou_id
	,denpyou_edano
	,shiwake_edano
	,torihiki_name
	,tekiyou
	,zeiritsu
	,'0'	--�y���ŗ��敪
	,shiharai_kingaku
	,hontai_kingaku
	,shouhizeigaku
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,torihikisaki_cd
	,torihikisaki_name_ryakushiki
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_kazei_kbn
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_kazei_kbn
	,uf1_cd
	,uf1_name_ryakushiki
	,uf2_cd
	,uf2_name_ryakushiki
	,uf3_cd
	,uf3_name_ryakushiki
	,uf4_cd
	,uf4_name_ryakushiki
	,uf5_cd
	,uf5_name_ryakushiki
	,uf6_cd
	,uf6_name_ryakushiki
	,uf7_cd
	,uf7_name_ryakushiki
	,uf8_cd
	,uf8_name_ryakushiki
	,uf9_cd
	,uf9_name_ryakushiki
	,uf10_cd
	,uf10_name_ryakushiki
	,project_cd
	,project_name
	,segment_cd
	,segment_name_ryakushiki
	,tekiyou_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM jidouhikiotoshi_meisai_old;
DROP TABLE jidouhikiotoshi_meisai_old;


--�y���ŗ��Ή� �d�󒊏o�iSIAS�j��`�ύX
ALTER TABLE shiwake_sias RENAME TO shiwake_sias_old;
create table shiwake_sias (
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
  , primary key (serial_no)
) WITHOUT OIDS;
comment on table shiwake_sias is '�d�󒊏o(SIAS)';
comment on column shiwake_sias.serial_no is '�V���A���ԍ�';
comment on column shiwake_sias.denpyou_id is '�`�[ID';
comment on column shiwake_sias.shiwake_status is '�d�󒊏o���';
comment on column shiwake_sias.touroku_time is '�o�^����';
comment on column shiwake_sias.koushin_time is '�X�V����';
comment on column shiwake_sias.dymd is '�i�I�[�v���Q�P�j�`�[���t';
comment on column shiwake_sias.seiri is '�i�I�[�v���Q�P�j�������t���O';
comment on column shiwake_sias.dcno is '�i�I�[�v���Q�P�j�`�[�ԍ�';
comment on column shiwake_sias.kymd is '�i�I�[�v���Q�P�j�N�[�N����';
comment on column shiwake_sias.kbmn is '�i�I�[�v���Q�P�j�N�[����R�[�h';
comment on column shiwake_sias.kusr is '�i�I�[�v���Q�P�j�N�[�҃R�[�h';
comment on column shiwake_sias.sgno is '�i�I�[�v���Q�P�j���F�O���[�vNo.';
comment on column shiwake_sias.hf1 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�P';
comment on column shiwake_sias.hf2 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�Q';
comment on column shiwake_sias.hf3 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�R';
comment on column shiwake_sias.hf4 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�S';
comment on column shiwake_sias.hf5 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�T';
comment on column shiwake_sias.hf6 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�U';
comment on column shiwake_sias.hf7 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�V';
comment on column shiwake_sias.hf8 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�W';
comment on column shiwake_sias.hf9 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�X';
comment on column shiwake_sias.hf10 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�P�O';
comment on column shiwake_sias.rbmn is '�i�I�[�v���Q�P�j�ؕ��@����R�[�h';
comment on column shiwake_sias.rtor is '�i�I�[�v���Q�P�j�ؕ��@�����R�[�h';
comment on column shiwake_sias.rkmk is '�i�I�[�v���Q�P�j�ؕ��@�ȖڃR�[�h';
comment on column shiwake_sias.reda is '�i�I�[�v���Q�P�j�ؕ��@�}�ԃR�[�h';
comment on column shiwake_sias.rkoj is '�i�I�[�v���Q�P�j�ؕ��@�H���R�[�h';
comment on column shiwake_sias.rkos is '�i�I�[�v���Q�P�j�ؕ��@�H��R�[�h';
comment on column shiwake_sias.rprj is '�i�I�[�v���Q�P�j�ؕ��@�v���W�F�N�g�R�[�h';
comment on column shiwake_sias.rseg is '�i�I�[�v���Q�P�j�ؕ��@�Z�O�����g�R�[�h';
comment on column shiwake_sias.rdm1 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P';
comment on column shiwake_sias.rdm2 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�Q';
comment on column shiwake_sias.rdm3 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�R';
comment on column shiwake_sias.rdm4 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�S';
comment on column shiwake_sias.rdm5 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�T';
comment on column shiwake_sias.rdm6 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�U';
comment on column shiwake_sias.rdm7 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�V';
comment on column shiwake_sias.rdm8 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�W';
comment on column shiwake_sias.rdm9 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�X';
comment on column shiwake_sias.rdm10 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�O';
comment on column shiwake_sias.rdm11 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�P';
comment on column shiwake_sias.rdm12 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�Q';
comment on column shiwake_sias.rdm13 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�R';
comment on column shiwake_sias.rdm14 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�S';
comment on column shiwake_sias.rdm15 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�T';
comment on column shiwake_sias.rdm16 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�U';
comment on column shiwake_sias.rdm17 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�V';
comment on column shiwake_sias.rdm18 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�W';
comment on column shiwake_sias.rdm19 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�X';
comment on column shiwake_sias.rdm20 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�Q�O';
comment on column shiwake_sias.rrit is '�i�I�[�v���Q�P�j�ؕ��@�ŗ�';
comment on column shiwake_sias.rkeigen is '�i�I�[�v���Q�P�j�ؕ��@�y���ŗ��敪';
comment on column shiwake_sias.rzkb is '�i�I�[�v���Q�P�j�ؕ��@�ېŋ敪';
comment on column shiwake_sias.rgyo is '�i�I�[�v���Q�P�j�ؕ��@�Ǝ�敪';
comment on column shiwake_sias.rsre is '�i�I�[�v���Q�P�j�ؕ��@�d���敪';
comment on column shiwake_sias.rtky is '�i�I�[�v���Q�P�j�ؕ��@�E�v';
comment on column shiwake_sias.rtno is '�i�I�[�v���Q�P�j�ؕ��@�E�v�R�[�h';
comment on column shiwake_sias.sbmn is '�i�I�[�v���Q�P�j�ݕ��@����R�[�h';
comment on column shiwake_sias.stor is '�i�I�[�v���Q�P�j�ݕ��@�����R�[�h';
comment on column shiwake_sias.skmk is '�i�I�[�v���Q�P�j�ݕ��@�ȖڃR�[�h';
comment on column shiwake_sias.seda is '�i�I�[�v���Q�P�j�ݕ��@�}�ԃR�[�h';
comment on column shiwake_sias.skoj is '�i�I�[�v���Q�P�j�ݕ��@�H���R�[�h';
comment on column shiwake_sias.skos is '�i�I�[�v���Q�P�j�ݕ��@�H��R�[�h';
comment on column shiwake_sias.sprj is '�i�I�[�v���Q�P�j�ݕ��@�v���W�F�N�g�R�[�h';
comment on column shiwake_sias.sseg is '�i�I�[�v���Q�P�j�ݕ��@�Z�O�����g�R�[�h';
comment on column shiwake_sias.sdm1 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P';
comment on column shiwake_sias.sdm2 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�Q';
comment on column shiwake_sias.sdm3 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�R';
comment on column shiwake_sias.sdm4 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�S';
comment on column shiwake_sias.sdm5 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�T';
comment on column shiwake_sias.sdm6 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�U';
comment on column shiwake_sias.sdm7 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�V';
comment on column shiwake_sias.sdm8 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�W';
comment on column shiwake_sias.sdm9 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�X';
comment on column shiwake_sias.sdm10 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�O';
comment on column shiwake_sias.sdm11 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�P';
comment on column shiwake_sias.sdm12 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�Q';
comment on column shiwake_sias.sdm13 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�R';
comment on column shiwake_sias.sdm14 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�S';
comment on column shiwake_sias.sdm15 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�T';
comment on column shiwake_sias.sdm16 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�U';
comment on column shiwake_sias.sdm17 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�V';
comment on column shiwake_sias.sdm18 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�W';
comment on column shiwake_sias.sdm19 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�X';
comment on column shiwake_sias.sdm20 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�Q�O';
comment on column shiwake_sias.srit is '�i�I�[�v���Q�P�j�ݕ��@�ŗ�';
comment on column shiwake_sias.skeigen is '�i�I�[�v���Q�P�j�ݕ��@�y���ŗ��敪';
comment on column shiwake_sias.szkb is '�i�I�[�v���Q�P�j�ݕ��@�ېŋ敪';
comment on column shiwake_sias.sgyo is '�i�I�[�v���Q�P�j�ݕ��@�Ǝ�敪';
comment on column shiwake_sias.ssre is '�i�I�[�v���Q�P�j�ݕ��@�d���敪';
comment on column shiwake_sias.stky is '�i�I�[�v���Q�P�j�ݕ��@�E�v';
comment on column shiwake_sias.stno is '�i�I�[�v���Q�P�j�ݕ��@�E�v�R�[�h';
comment on column shiwake_sias.zkmk is '�i�I�[�v���Q�P�j����őΏۉȖڃR�[�h';
comment on column shiwake_sias.zrit is '�i�I�[�v���Q�P�j����őΏۉȖڐŗ�';
comment on column shiwake_sias.zkeigen is '�i�I�[�v���Q�P�j����őΏۉȖځ@�y���ŗ��敪';
comment on column shiwake_sias.zzkb is '�i�I�[�v���Q�P�j����őΏۉȖځ@�ېŋ敪';
comment on column shiwake_sias.zgyo is '�i�I�[�v���Q�P�j����őΏۉȖځ@�Ǝ�敪';
comment on column shiwake_sias.zsre is '�i�I�[�v���Q�P�j����őΏۉȖځ@�d���敪';
comment on column shiwake_sias.exvl is '�i�I�[�v���Q�P�j�Ή����z';
comment on column shiwake_sias.valu is '�i�I�[�v���Q�P�j���z';
comment on column shiwake_sias.symd is '�i�I�[�v���Q�P�j�x����';
comment on column shiwake_sias.skbn is '�i�I�[�v���Q�P�j�x���敪';
comment on column shiwake_sias.skiz is '�i�I�[�v���Q�P�j�x������';
comment on column shiwake_sias.uymd is '�i�I�[�v���Q�P�j�����';
comment on column shiwake_sias.ukbn is '�i�I�[�v���Q�P�j�����敪';
comment on column shiwake_sias.ukiz is '�i�I�[�v���Q�P�j�������';
comment on column shiwake_sias.dkec is '�i�I�[�v���Q�P�j�����R�[�h';
comment on column shiwake_sias.fusr is '�i�I�[�v���Q�P�j���͎҃R�[�h';
comment on column shiwake_sias.fsen is '�i�I�[�v���Q�P�j�tⳔԍ�';
comment on column shiwake_sias.tkflg is '�i�I�[�v���Q�P�j�ݎؕʓE�v�t���O';
comment on column shiwake_sias.bunri is '�i�I�[�v���Q�P�j�����敪';
comment on column shiwake_sias.heic is '�i�I�[�v���Q�P�j����';
comment on column shiwake_sias.rate is '�i�I�[�v���Q�P�j���[�g';
comment on column shiwake_sias.gexvl is '�i�I�[�v���Q�P�j�O�ݑΉ����z';
comment on column shiwake_sias.gvalu is '�i�I�[�v���Q�P�j�O�݋��z';
comment on column shiwake_sias.gsep is '�i�I�[�v���Q�P�j�s��؂�';

INSERT INTO shiwake_sias
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
  , '' --rkeigen
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
  , '' --skeigen
  , szkb
  , sgyo
  , ssre
  , stky
  , stno
  , zkmk
  , zrit
  , '' --zkeigen
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
FROM shiwake_sias_old;
UPDATE shiwake_sias SET rkeigen = '0' WHERE rrit IS NOT NULL;
UPDATE shiwake_sias SET skeigen = '0' WHERE srit IS NOT NULL;
DROP TABLE shiwake_sias_old;

--�y���ŗ��Ή� �d�󒊏o�ide3�j��`�ύX
ALTER TABLE shiwake_de3 RENAME TO shiwake_de3_old;
create table shiwake_de3 (
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
  , primary key (serial_no)
) WITHOUT OIDS;
comment on table shiwake_de3 is '�d�󒊏o(de3)';
comment on column shiwake_de3.serial_no is '�V���A���ԍ�';
comment on column shiwake_de3.denpyou_id is '�`�[ID';
comment on column shiwake_de3.shiwake_status is '�d�󒊏o���';
comment on column shiwake_de3.touroku_time is '�o�^����';
comment on column shiwake_de3.koushin_time is '�X�V����';
comment on column shiwake_de3.dymd is '�i�I�[�v���Q�P�j�`�[���t';
comment on column shiwake_de3.seiri is '�i�I�[�v���Q�P�j�������t���O';
comment on column shiwake_de3.dcno is '�i�I�[�v���Q�P�j�`�[�ԍ�';
comment on column shiwake_de3.rbmn is '�i�I�[�v���Q�P�j�ؕ��@����R�[�h';
comment on column shiwake_de3.rtor is '�i�I�[�v���Q�P�j�ؕ��@�����R�[�h';
comment on column shiwake_de3.rkmk is '�i�I�[�v���Q�P�j�ؕ��@�ȖڃR�[�h';
comment on column shiwake_de3.reda is '�i�I�[�v���Q�P�j�ؕ��@�}�ԃR�[�h';
comment on column shiwake_de3.rkoj is '�i�I�[�v���Q�P�j�ؕ��@�H���R�[�h';
comment on column shiwake_de3.rkos is '�i�I�[�v���Q�P�j�ؕ��@�H��R�[�h';
comment on column shiwake_de3.rprj is '�i�I�[�v���Q�P�j�ؕ��@�v���W�F�N�g�R�[�h';
comment on column shiwake_de3.rseg is '�i�I�[�v���Q�P�j�ؕ��@�Z�O�����g�R�[�h';
comment on column shiwake_de3.rdm1 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P';
comment on column shiwake_de3.rdm2 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�Q';
comment on column shiwake_de3.rdm3 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�R';
comment on column shiwake_de3.tky is '�i�I�[�v���Q�P�j�E�v';
comment on column shiwake_de3.tno is '�i�I�[�v���Q�P�j�E�v�R�[�h';
comment on column shiwake_de3.sbmn is '�i�I�[�v���Q�P�j�ݕ��@����R�[�h';
comment on column shiwake_de3.stor is '�i�I�[�v���Q�P�j�ݕ��@�����R�[�h';
comment on column shiwake_de3.skmk is '�i�I�[�v���Q�P�j�ݕ��@�ȖڃR�[�h';
comment on column shiwake_de3.seda is '�i�I�[�v���Q�P�j�ݕ��@�}�ԃR�[�h';
comment on column shiwake_de3.skoj is '�i�I�[�v���Q�P�j�ݕ��@�H���R�[�h';
comment on column shiwake_de3.skos is '�i�I�[�v���Q�P�j�ݕ��@�H��R�[�h';
comment on column shiwake_de3.sprj is '�i�I�[�v���Q�P�j�ݕ��@�v���W�F�N�g�R�[�h';
comment on column shiwake_de3.sseg is '�i�I�[�v���Q�P�j�ݕ��@�Z�O�����g�R�[�h';
comment on column shiwake_de3.sdm1 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P';
comment on column shiwake_de3.sdm2 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�Q';
comment on column shiwake_de3.sdm3 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�R';
comment on column shiwake_de3.exvl is '�i�I�[�v���Q�P�j�Ή����z';
comment on column shiwake_de3.valu is '�i�I�[�v���Q�P�j���z';
comment on column shiwake_de3.zkmk is '�i�I�[�v���Q�P�j����őΏۉȖڃR�[�h';
comment on column shiwake_de3.zrit is '�i�I�[�v���Q�P�j����őΏۉȖڐŗ�';
comment on column shiwake_de3.zkeigen is '�i�I�[�v���Q�P�j����őΏۉȖځ@�y���ŗ��敪';
comment on column shiwake_de3.zzkb is '�i�I�[�v���Q�P�j����őΏۉȖځ@�ېŋ敪';
comment on column shiwake_de3.zgyo is '�i�I�[�v���Q�P�j����őΏۉȖځ@�Ǝ�敪';
comment on column shiwake_de3.zsre is '�i�I�[�v���Q�P�j����őΏۉȖځ@�d���敪';
comment on column shiwake_de3.rrit is '�i�I�[�v���Q�P�j�ؕ��@�ŗ�';
comment on column shiwake_de3.rkeigen is '�i�I�[�v���Q�P�j�ؕ��@�y���ŗ��敪';
comment on column shiwake_de3.srit is '�i�I�[�v���Q�P�j�ݕ��@�ŗ�';
comment on column shiwake_de3.skeigen is '�i�I�[�v���Q�P�j�ݕ��@�y���ŗ��敪';
comment on column shiwake_de3.rzkb is '�i�I�[�v���Q�P�j�ؕ��@�ېŋ敪';
comment on column shiwake_de3.rgyo is '�i�I�[�v���Q�P�j�ؕ��@�Ǝ�敪';
comment on column shiwake_de3.rsre is '�i�I�[�v���Q�P�j�ؕ��@�d���敪';
comment on column shiwake_de3.szkb is '�i�I�[�v���Q�P�j�ݕ��@�ېŋ敪';
comment on column shiwake_de3.sgyo is '�i�I�[�v���Q�P�j�ݕ��@�Ǝ�敪';
comment on column shiwake_de3.ssre is '�i�I�[�v���Q�P�j�ݕ��@�d���敪';
comment on column shiwake_de3.symd is '�i�I�[�v���Q�P�j�x����';
comment on column shiwake_de3.skbn is '�i�I�[�v���Q�P�j�x���敪';
comment on column shiwake_de3.skiz is '�i�I�[�v���Q�P�j�x������';
comment on column shiwake_de3.uymd is '�i�I�[�v���Q�P�j�����';
comment on column shiwake_de3.ukbn is '�i�I�[�v���Q�P�j�����敪';
comment on column shiwake_de3.ukiz is '�i�I�[�v���Q�P�j�������';
comment on column shiwake_de3.sten is '�i�I�[�v���Q�P�j�X���t���O';
comment on column shiwake_de3.dkec is '�i�I�[�v���Q�P�j�����R�[�h';
comment on column shiwake_de3.kymd is '�i�I�[�v���Q�P�j�N�[�N����';
comment on column shiwake_de3.kbmn is '�i�I�[�v���Q�P�j�N�[����R�[�h';
comment on column shiwake_de3.kusr is '�i�I�[�v���Q�P�j�N�[�҃R�[�h';
comment on column shiwake_de3.fusr is '�i�I�[�v���Q�P�j���͎҃R�[�h';
comment on column shiwake_de3.fsen is '�i�I�[�v���Q�P�j�tⳔԍ�';
comment on column shiwake_de3.sgno is '�i�I�[�v���Q�P�j���F�O���[�vNo.';
comment on column shiwake_de3.bunri is '�i�I�[�v���Q�P�j�����敪';
comment on column shiwake_de3.rate is '�i�I�[�v���Q�P�j���[�g';
comment on column shiwake_de3.gexvl is '�i�I�[�v���Q�P�j�O�ݑΉ����z';
comment on column shiwake_de3.gvalu is '�i�I�[�v���Q�P�j�O�݋��z';
comment on column shiwake_de3.gsep is '�i�I�[�v���Q�P�j�s��؂�';

INSERT INTO shiwake_de3
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
  , '' --zkeigen
  , zzkb
  , zgyo
  , zsre
  , rrit
  , '' --rkeigen
  , srit
  , '' --skeigen
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
FROM shiwake_de3_old;
UPDATE shiwake_de3 SET rkeigen = '0' WHERE rrit IS NOT NULL;
UPDATE shiwake_de3 SET skeigen = '0' WHERE srit IS NOT NULL;
DROP TABLE shiwake_de3_old;

--�A���[0604_�����t�֓`�[ �t�֋敪�f�t�H���g�l�Ή� �V�K�e�[�u���ǉ�
create table user_default_value (
  kbn character varying(50) not null
  , user_id character varying(30) not null
  , default_value character varying(1) not null
  , primary key (kbn,user_id)
)WITHOUT OIDS;
comment on table user_default_value is '���[�U�[�ʃf�t�H���g�l';
comment on column user_default_value.kbn is '�敪';
comment on column user_default_value.user_id is '���[�U�[ID';
comment on column user_default_value.default_value is '�f�t�H���g�l';

--�A���[0528 XML�`���̑S��t�H�[�}�b�g�ւ̑Ή� ��Џ��J�����ǉ�
ALTER TABLE kaisha_info RENAME TO kaisha_info_old;
create table kaisha_info (
  kessanki_bangou smallint not null
  , hf1_shiyou_flg smallint not null
  , hf1_hissu_flg character varying(1) not null
  , hf1_name character varying not null
  , hf2_shiyou_flg smallint not null
  , hf2_hissu_flg character varying(1) not null
  , hf2_name character varying not null
  , hf3_shiyou_flg smallint not null
  , hf3_hissu_flg character varying(1) not null
  , hf3_name character varying not null
  , hf4_shiyou_flg smallint not null
  , hf4_hissu_flg character varying(1) not null
  , hf4_name character varying not null
  , hf5_shiyou_flg smallint not null
  , hf5_hissu_flg character varying(1) not null
  , hf5_name character varying not null
  , hf6_shiyou_flg smallint not null
  , hf6_hissu_flg character varying(1) not null
  , hf6_name character varying not null
  , hf7_shiyou_flg smallint not null
  , hf7_hissu_flg character varying(1) not null
  , hf7_name character varying not null
  , hf8_shiyou_flg smallint not null
  , hf8_hissu_flg character varying(1) not null
  , hf8_name character varying not null
  , hf9_shiyou_flg smallint not null
  , hf9_hissu_flg character varying(1) not null
  , hf9_name character varying not null
  , hf10_shiyou_flg smallint not null
  , hf10_hissu_flg character varying(1) not null
  , hf10_name character varying not null
  , uf1_shiyou_flg smallint not null
  , uf1_name character varying not null
  , uf2_shiyou_flg smallint not null
  , uf2_name character varying not null
  , uf3_shiyou_flg smallint not null
  , uf3_name character varying not null
  , uf4_shiyou_flg smallint not null
  , uf4_name character varying not null
  , uf5_shiyou_flg smallint not null
  , uf5_name character varying not null
  , uf6_shiyou_flg smallint not null
  , uf6_name character varying not null
  , uf7_shiyou_flg smallint not null
  , uf7_name character varying not null
  , uf8_shiyou_flg smallint not null
  , uf8_name character varying not null
  , uf9_shiyou_flg smallint not null
  , uf9_name character varying not null
  , uf10_shiyou_flg smallint not null
  , uf10_name character varying not null
  , uf_kotei1_shiyou_flg smallint not null
  , uf_kotei1_name character varying not null
  , uf_kotei2_shiyou_flg smallint not null
  , uf_kotei2_name character varying not null
  , uf_kotei3_shiyou_flg smallint not null
  , uf_kotei3_name character varying not null
  , uf_kotei4_shiyou_flg smallint not null
  , uf_kotei4_name character varying not null
  , uf_kotei5_shiyou_flg smallint not null
  , uf_kotei5_name character varying not null
  , uf_kotei6_shiyou_flg smallint not null
  , uf_kotei6_name character varying not null
  , uf_kotei7_shiyou_flg smallint not null
  , uf_kotei7_name character varying not null
  , uf_kotei8_shiyou_flg smallint not null
  , uf_kotei8_name character varying not null
  , uf_kotei9_shiyou_flg smallint not null
  , uf_kotei9_name character varying not null
  , uf_kotei10_shiyou_flg smallint not null
  , uf_kotei10_name character varying not null
  , pjcd_shiyou_flg smallint not null
  , sgcd_shiyou_flg smallint not null
  , saimu_shiyou_flg character varying(1) not null
  , kamoku_cd_type smallint not null
  , kamoku_edaban_cd_type smallint not null
  , futan_bumon_cd_type smallint not null
  , torihikisaki_cd_type smallint not null
  , segment_cd_type smallint not null
  , houjin_bangou character varying(13) not null
);

comment on table kaisha_info is '��Џ��';
comment on column kaisha_info.kessanki_bangou is '���Z���ԍ�';
comment on column kaisha_info.hf1_shiyou_flg is 'HF1�g�p�t���O';
comment on column kaisha_info.hf1_hissu_flg is 'HF1�K�{�t���O';
comment on column kaisha_info.hf1_name is 'HF1��';
comment on column kaisha_info.hf2_shiyou_flg is 'HF2�g�p�t���O';
comment on column kaisha_info.hf2_hissu_flg is 'HF2�K�{�t���O';
comment on column kaisha_info.hf2_name is 'HF2��';
comment on column kaisha_info.hf3_shiyou_flg is 'HF3�g�p�t���O';
comment on column kaisha_info.hf3_hissu_flg is 'HF3�K�{�t���O';
comment on column kaisha_info.hf3_name is 'HF3��';
comment on column kaisha_info.hf4_shiyou_flg is 'HF4�g�p�t���O';
comment on column kaisha_info.hf4_hissu_flg is 'HF4�K�{�t���O';
comment on column kaisha_info.hf4_name is 'HF4��';
comment on column kaisha_info.hf5_shiyou_flg is 'HF5�g�p�t���O';
comment on column kaisha_info.hf5_hissu_flg is 'HF5�K�{�t���O';
comment on column kaisha_info.hf5_name is 'HF5��';
comment on column kaisha_info.hf6_shiyou_flg is 'HF6�g�p�t���O';
comment on column kaisha_info.hf6_hissu_flg is 'HF6�K�{�t���O';
comment on column kaisha_info.hf6_name is 'HF6��';
comment on column kaisha_info.hf7_shiyou_flg is 'HF7�g�p�t���O';
comment on column kaisha_info.hf7_hissu_flg is 'HF7�K�{�t���O';
comment on column kaisha_info.hf7_name is 'HF7��';
comment on column kaisha_info.hf8_shiyou_flg is 'HF8�g�p�t���O';
comment on column kaisha_info.hf8_hissu_flg is 'HF8�K�{�t���O';
comment on column kaisha_info.hf8_name is 'HF8��';
comment on column kaisha_info.hf9_shiyou_flg is 'HF9�g�p�t���O';
comment on column kaisha_info.hf9_hissu_flg is 'HF9�K�{�t���O';
comment on column kaisha_info.hf9_name is 'HF9��';
comment on column kaisha_info.hf10_shiyou_flg is 'HF10�g�p�t���O';
comment on column kaisha_info.hf10_hissu_flg is 'HF10�K�{�t���O';
comment on column kaisha_info.hf10_name is 'HF10��';
comment on column kaisha_info.uf1_shiyou_flg is 'UF1�g�p�t���O';
comment on column kaisha_info.uf1_name is 'UF1��';
comment on column kaisha_info.uf2_shiyou_flg is 'UF2�g�p�t���O';
comment on column kaisha_info.uf2_name is 'UF2��';
comment on column kaisha_info.uf3_shiyou_flg is 'UF3�g�p�t���O';
comment on column kaisha_info.uf3_name is 'UF3��';
comment on column kaisha_info.uf4_shiyou_flg is 'UF4�g�p�t���O';
comment on column kaisha_info.uf4_name is 'UF4��';
comment on column kaisha_info.uf5_shiyou_flg is 'UF5�g�p�t���O';
comment on column kaisha_info.uf5_name is 'UF5��';
comment on column kaisha_info.uf6_shiyou_flg is 'UF6�g�p�t���O';
comment on column kaisha_info.uf6_name is 'UF6��';
comment on column kaisha_info.uf7_shiyou_flg is 'UF7�g�p�t���O';
comment on column kaisha_info.uf7_name is 'UF7��';
comment on column kaisha_info.uf8_shiyou_flg is 'UF8�g�p�t���O';
comment on column kaisha_info.uf8_name is 'UF8��';
comment on column kaisha_info.uf9_shiyou_flg is 'UF9�g�p�t���O';
comment on column kaisha_info.uf9_name is 'UF9��';
comment on column kaisha_info.uf10_shiyou_flg is 'UF10�g�p�t���O';
comment on column kaisha_info.uf10_name is 'UF10��';
comment on column kaisha_info.uf_kotei1_shiyou_flg is 'UF1�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei1_name is 'UF1��(�Œ�l)';
comment on column kaisha_info.uf_kotei2_shiyou_flg is 'UF2�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei2_name is 'UF2��(�Œ�l)';
comment on column kaisha_info.uf_kotei3_shiyou_flg is 'UF3�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei3_name is 'UF3��(�Œ�l)';
comment on column kaisha_info.uf_kotei4_shiyou_flg is 'UF4�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei4_name is 'UF4��(�Œ�l)';
comment on column kaisha_info.uf_kotei5_shiyou_flg is 'UF5�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei5_name is 'UF5��(�Œ�l)';
comment on column kaisha_info.uf_kotei6_shiyou_flg is 'UF6�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei6_name is 'UF6��(�Œ�l)';
comment on column kaisha_info.uf_kotei7_shiyou_flg is 'UF7�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei7_name is 'UF7��(�Œ�l)';
comment on column kaisha_info.uf_kotei8_shiyou_flg is 'UF8�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei8_name is 'UF8��(�Œ�l)';
comment on column kaisha_info.uf_kotei9_shiyou_flg is 'UF9�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei9_name is 'UF9��(�Œ�l)';
comment on column kaisha_info.uf_kotei10_shiyou_flg is 'UF10�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei10_name is 'UF10��(�Œ�l)';
comment on column kaisha_info.pjcd_shiyou_flg is '�v���W�F�N�g�R�[�h�g�p�t���O';
comment on column kaisha_info.sgcd_shiyou_flg is '�Z�O�����g�R�[�h�g�p�t���O';
comment on column kaisha_info.saimu_shiyou_flg is '���g�p�t���O';
comment on column kaisha_info.kamoku_cd_type is '�ȖڃR�[�h�^�C�v';
comment on column kaisha_info.kamoku_edaban_cd_type is '�Ȗڎ}�ԃR�[�h�^�C�v';
comment on column kaisha_info.futan_bumon_cd_type is '���S����R�[�h�^�C�v';
comment on column kaisha_info.torihikisaki_cd_type is '�����R�[�h�^�C�v';
comment on column kaisha_info.segment_cd_type is '�Z�O�����g�R�[�h�^�C�v';
comment on column kaisha_info.houjin_bangou is '�@�l�ԍ�';

INSERT INTO kaisha_info
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
  , saimu_shiyou_flg
  , kamoku_cd_type
  , kamoku_edaban_cd_type
  , futan_bumon_cd_type
  , torihikisaki_cd_type
  , segment_cd_type
  , ''
FROM kaisha_info_old;
DROP TABLE kaisha_info_old;

DELETE FROM master_torikomi_ichiran_de3;
DELETE FROM master_torikomi_shousai_de3;
DELETE FROM master_torikomi_ichiran_sias;
DELETE FROM master_torikomi_shousai_sias;
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;
