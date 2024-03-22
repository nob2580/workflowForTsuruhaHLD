SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- ��ʌ�������
DELETE FROM gamen_kengen_seigyo WHERE gamen_id LIKE 'Genyokin%';
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';


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

--�A���[No.878 
--�����R�[�h ���I�v�V�����p���R�[�h�̓p�b�`�O�ɂȂ���΍폜
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- �x���˗��\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- ���������\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

--�`�[�ꗗ
DROP TABLE denpyou_ichiran_kyoten;
create table denpyou_ichiran_kyoten (
  denpyou_id character varying(19) not null
  , denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , serial_no bigint
  , denpyou_joutai character varying(2) not null
  , name character varying not null
  , denpyou_shubetsu_url character varying(240) not null
  , touroku_time timestamp(6) without time zone
  , koushin_time timestamp(6) without time zone
  , shouninbi timestamp(6) without time zone
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
  , gen_name character varying not null
  , shiwake_status character varying not null
  , kari_kingaku_goukei numeric(15) not null
  , kashi_kingaku_goukei numeric(15) not null
  , denpyou_date date not null
  , hf1_cd character varying(20) not null
  , hf2_cd character varying(20) not null
  , hf3_cd character varying(20) not null
  , hf4_cd character varying(20) not null
  , hf5_cd character varying(20) not null
  , hf6_cd character varying(20) not null
  , hf7_cd character varying(20) not null
  , hf8_cd character varying(20) not null
  , hf9_cd character varying(20) not null
  , hf10_cd character varying(20) not null
  , bumon_tsukekae_kekka_flg character varying(1) not null
  , constraint denpyou_ichiran_kyoten_PKEY primary key (denpyou_id)
);
comment on table denpyou_ichiran_kyoten is '�`�[�ꗗ(���_)';
comment on column denpyou_ichiran_kyoten.denpyou_id is '�`�[ID';
comment on column denpyou_ichiran_kyoten.denpyou_kbn is '�`�[�敪';
comment on column denpyou_ichiran_kyoten.zaimu_kyoten_nyuryoku_pattern_no is '���_���̓p�^�[��No';
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
comment on column denpyou_ichiran_kyoten.gen_name is '���ݏ��F�Җ���';
comment on column denpyou_ichiran_kyoten.shiwake_status is '�d��f�[�^�쐬�X�e�[�^�X';
comment on column denpyou_ichiran_kyoten.kari_kingaku_goukei is '�ؕ����z���v(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.kashi_kingaku_goukei is '�ݕ����z���v(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.denpyou_date is '�`�[���t(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.hf1_cd is 'HF1�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.hf2_cd is 'HF2�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.hf3_cd is 'HF3�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.hf4_cd is 'HF4�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.hf5_cd is 'HF5�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.hf6_cd is 'HF6�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.hf7_cd is 'HF7�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.hf8_cd is 'HF8�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.hf9_cd is 'HF9�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.hf10_cd is 'HF10�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten.bumon_tsukekae_kekka_flg is '����t�֌��ʃt���O(�ꗗ�����p)';

create table denpyou_ichiran_kyoten_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , meisai_serial_no bigint
  , nyushukkin_kbn character varying(1) not null
  , shiwake_edano integer
  , torihiki_name character varying(20) not null
  , bumon_tsukekae_taishou_flg character varying(1) not null
  , bumon_tsukekae_shori_flg character varying(1) not null
  , shiwake_taishougai_flg character varying(1) not null
  , tenpu_file_flg character varying(1) not null
  , ebunsho_flg character varying(1) not null
  , fusen_flg character varying(1) not null
  , fusen_color character varying(1) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_torihikisaki_cd character varying(12) not null
  , kari_torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kingaku numeric(15)
  , kari_shouhizeigaku numeric(15)
  , kari_taika numeric(15)
  , kari_tekiyou character varying(60) not null
  , kari_kazei_kbn character varying(3) not null
  , kari_zeiritsu numeric(3)
  , kari_keigen_zeiritsu_kbn character varying(1) not null
  , kari_bunri_kbn character varying(1) not null
  , kari_kobetsu_kbn character varying(1) not null
  , kari_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kari_project_cd character varying(12) not null
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
  , kari_heishu_cd character varying(4) not null
  , kari_rate numeric(11, 5)
  , kari_gaika numeric(22, 5)
  , kari_gaika_shouhizeigaku numeric(22, 5)
  , kari_gaika_taika numeric(22, 5)
  , kashi_futan_bumon_cd character varying not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_torihikisaki_cd character varying not null
  , kashi_torihikisaki_name_ryakushiki character varying(20) not null
  , kashi_kingaku numeric(15)
  , kashi_shouhizeigaku numeric(15)
  , kashi_taika numeric(15)
  , kashi_tekiyou character varying(60) not null
  , kashi_kazei_kbn character varying(3) not null
  , kashi_zeiritsu numeric(3)
  , kashi_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_bunri_kbn character varying(1) not null
  , kashi_kobetsu_kbn character varying(1) not null
  , kashi_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kashi_project_cd character varying(12) not null
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
  , kashi_heishu_cd character varying(20) not null
  , kashi_rate numeric(11, 5)
  , kashi_gaika numeric(22, 5)
  , kashi_gaika_shouhizeigaku numeric(22, 5)
  , kashi_gaika_taika numeric(22, 5)
  , constraint denpyou_ichiran_kyoten_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table denpyou_ichiran_kyoten_meisai is '�`�[�ꗗ(���_)����';
comment on column denpyou_ichiran_kyoten_meisai.denpyou_id is '�`�[ID';
comment on column denpyou_ichiran_kyoten_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column denpyou_ichiran_kyoten_meisai.meisai_serial_no is '���׃V���A��No';
comment on column denpyou_ichiran_kyoten_meisai.nyushukkin_kbn is '���o���敪(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.shiwake_edano is '�d��}�ԍ�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.torihiki_name is '�����(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.bumon_tsukekae_taishou_flg is '����t�֑Ώۃt���O(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.bumon_tsukekae_shori_flg is '����t�֏����t���O(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.shiwake_taishougai_flg is '�d��ΏۊO�t���O(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.tenpu_file_flg is '�Y�t�t�@�C���t���O(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.ebunsho_flg is 'e�����Ώۃt���O(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.fusen_flg is '�tⳃt���O(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.fusen_color is '�tⳃJ���[(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_futan_bumon_name is '�ؕ����S���喼(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kamoku_name is '�ؕ��Ȗږ�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_torihikisaki_cd is '�ؕ������R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_torihikisaki_name_ryakushiki is '�ؕ�����於�i�����j(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kingaku is '�ؕ����z(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_shouhizeigaku is '�ؕ�����Ŋz(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_taika is '�ؕ��Ή�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_tekiyou is '�ؕ��E�v(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_zeiritsu is '�ؕ��ŗ�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_keigen_zeiritsu_kbn is '�ؕ��y���ŗ��敪(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_bunri_kbn is '�ؕ������敪(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kobetsu_kbn is '�ؕ��ʋ敪(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_shouhizeitaishou_kamoku_cd is '�ؕ��őΏۉȖ�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_project_cd is '�ؕ��v���W�F�N�g�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_segment_cd is '�ؕ��Z�O�����g�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf1_cd is '�ؕ�UF1�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf2_cd is '�ؕ�UF2�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf3_cd is '�ؕ�UF3�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf4_cd is '�ؕ�UF4�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf5_cd is '�ؕ�UF5�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf6_cd is '�ؕ�UF6�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf7_cd is '�ؕ�UF7�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf8_cd is '�ؕ�UF8�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf9_cd is '�ؕ�UF9�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf10_cd is '�ؕ�UF10�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_heishu_cd is '�ؕ�����R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_rate is '�ؕ����[�g(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_gaika is '�ؕ��O�݋��z(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_gaika_shouhizeigaku is '�ؕ��O�ݏ���Ŋz(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kari_gaika_taika is '�ؕ��O�ݑΉ�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_futan_bumon_name is '�ݕ����S���喼(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_torihikisaki_cd is '�ݕ������R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_torihikisaki_name_ryakushiki is '�ݕ�����於�i�����j(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kingaku is '�ݕ����z(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_shouhizeigaku is '�ݕ�����Ŋz(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_taika is '�ݕ��Ή�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_tekiyou is '�ݕ��E�v(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_zeiritsu is '�ݕ��ŗ�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_keigen_zeiritsu_kbn is '�ݕ��y���ŗ��敪(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_bunri_kbn is '�ݕ������敪(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kobetsu_kbn is '�ݕ��ʋ敪(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_shouhizeitaishou_kamoku_cd is '�ݕ��őΏۉȖ�(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_project_cd is '�ݕ��v���W�F�N�g�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_segment_cd is '�ݕ��Z�O�����g�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf1_cd is '�ݕ�UF1�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf2_cd is '�ݕ�UF2�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf3_cd is '�ݕ�UF3�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf4_cd is '�ݕ�UF4�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf5_cd is '�ݕ�UF5�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf6_cd is '�ݕ�UF6�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf7_cd is '�ݕ�UF7�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf8_cd is '�ݕ�UF8�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf9_cd is '�ݕ�UF9�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf10_cd is '�ݕ�UF10�R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_heishu_cd is '�ݕ�����R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_rate is '�ݕ����[�g(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_gaika is '�ݕ��O�݋��z(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_gaika_shouhizeigaku is '�ݕ��O�ݏ���Ŋz(�ꗗ�����p)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_gaika_taika is '�ݕ��O�ݑΉ�(�ꗗ�����p)';


commit;
