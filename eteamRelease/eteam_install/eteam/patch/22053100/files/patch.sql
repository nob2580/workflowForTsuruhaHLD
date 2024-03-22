SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �ݒ���f�[�^�ύX ��hyouji_jun900�ȍ~�̓J�X�^�}�C�Y�̈�A1000�Ԉȍ~�͋��_���͌����̈�
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

--�����R�[�h�ݒ�
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
-- Web���_���͂̓����R�[�h�͐􂢑ւ��ΏۊO
DELETE FROM naibu_cd_setting
WHERE naibu_cd_name NOT IN ('bunri_kbn','shiire_kbn','kazei_kbn_kyoten_furikae','shiwake_pattern_denpyou_kbn_kyoten','shiwake_pattern_setting_kbn_kyoten','fusen_color');
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- �x���˗��\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- ���������\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

-- kinou_seigyo�ւ̃f�[�^�ǉ�
INSERT INTO kinou_seigyo VALUES ('SD', '1') ON CONFLICT DO NOTHING;

-- gamen_koumoku_seigyo�}�X�^�[�f�[�^�ɍ��ڒǉ�
-- �ς̐ݒ肪�����Ă���̂Ńo�b�N�A�b�v������Đ􂢑ւ��Ƃ�������肭�ǂ������͎��Ȃ��ŃV���v���ɒǉ��f�[�^��insert�����݂�
INSERT INTO gamen_koumoku_seigyo (denpyou_kbn,koumoku_id,default_koumoku_name,koumoku_name,hyouji_flg,hissu_flg,hyouji_seigyo_flg,pdf_hyouji_flg,pdf_hyouji_seigyo_flg,code_output_flg,code_output_seigyo_flg,hyouji_jun)
VALUES ('A009','shouhyou_shorui_flg','�̎����E��������','�̎����E��������','1','1','1','0','0','0','0','2') ON CONFLICT DO NOTHING;

-- ���������`�[�ւ̃J�����ǉ�
-- �J�����w�肪�K�v�ȏ����͎w��ς݁A����select��dto�ւ̒l�̕R�Â����J�������L�[�ł���Ă��邽�߁A�P���ǉ��Ŗ��Ȃ�
ALTER TABLE jidouhikiotoshi ADD COLUMN IF NOT EXISTS shouhyou_shorui_flg character varying(1) DEFAULT '0' not null;
comment on column jidouhikiotoshi.shouhyou_shorui_flg is '�؜ߏ��ރt���O';

-- shiwake_pattern_setting�}�X�^�[�f�[�^�X�V
DELETE FROM SHIWAKE_PATTERN_SETTING;
\copy SHIWAKE_PATTERN_SETTING FROM '.\files\csv\shiwake_pattern_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- shiwake_pattern_var_setting�}�X�^�[�f�[�^�X�V
DELETE FROM SHIWAKE_PATTERN_VAR_SETTING;
\copy SHIWAKE_PATTERN_VAR_SETTING FROM '.\files\csv\shiwake_pattern_var_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- �Ј������ւ̃J�����ǉ�
-- �J�����w�肪�K�v�ȏ����͎w��ς݁A����select��dto�ւ̒l�̕R�Â����J�������L�[�ł���Ă��邽�߁A�P���ǉ��Ŗ��Ȃ�
ALTER TABLE shain_kouza ADD COLUMN IF NOT EXISTS zaimu_edaban_cd character varying(12) DEFAULT '' not null;
comment on column shain_kouza.zaimu_edaban_cd is '�����}�ԃR�[�h';

-- �v���W�F�N�g�}�X�^�[�ւ̃J�����ǉ�
-- �J�����w�肪�K�v�ȏ����͎w��ς݁A����select��dto�ւ̒l�̕R�Â����J�������L�[�ł���Ă��邽�߁A�P���ǉ��Ŗ��Ȃ�
ALTER TABLE project_master ADD COLUMN IF NOT EXISTS shuuryou_kbn smallint DEFAULT 0 NOT NULL;
comment on column project_master.shuuryou_kbn is '�I���敪';

-- e���������̍��ڔC�Ӊ�
ALTER TABLE ebunsho_data ALTER COLUMN ebunsho_nengappi DROP NOT NULL;
ALTER TABLE ebunsho_data ALTER COLUMN ebunsho_kingaku DROP NOT NULL;
ALTER TABLE ebunsho_data ALTER COLUMN ebunsho_hakkousha DROP NOT NULL;

-- e�����������ڗp�}�X�^�[�e�[�u���̍쐬
create table if not exists ebunsho_kensaku (
  doc_type smallint not null
  , item_no smallint not null
  , nini_flg smallint DEFAULT 0 not null
  , constraint ebunsho_kensaku_PKEY primary key (doc_type,item_no)
);

comment on table ebunsho_kensaku is 'e���������ݒ�';
comment on column ebunsho_kensaku.doc_type is '���ގ��';
comment on column ebunsho_kensaku.item_no is '����';
comment on column ebunsho_kensaku.nini_flg is '�C�Ӄt���O';

-- �����l�̑}��
INSERT INTO ebunsho_kensaku VALUES (0,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (0,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (0,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (1,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (1,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (1,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (2,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (2,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (2,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (3,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (3,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (3,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (4,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (4,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (4,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (5,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (5,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (5,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (6,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (6,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (6,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (7,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (7,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (7,6,0) ON CONFLICT DO NOTHING;

--�}�X�^�[��荞�݈ꗗ�֌W
DELETE FROM master_torikomi_ichiran_de3 WHERE master_id = 'project_master';
DELETE FROM master_torikomi_ichiran_sias WHERE master_id = 'project_master' OR master_id = 'ebunsho_kensaku' OR master_id = 'extension_setting';
DELETE FROM master_torikomi_ichiran_mk2 WHERE master_id = 'project_master' OR master_id = 'ebunsho_kensaku' OR master_id = 'extension_setting';
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_patch_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_patch_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_mk2 FROM '.\files\csv\master_torikomi_ichiran_patch_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--�}�X�^�[��荞�ݏڍ׊֌W
--�f�[�^�ǉ��݂̂�OK
INSERT INTO master_torikomi_shousai_de3(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('project_master','shuuryou_kbn','�I���敪','smallint','ENDKU','�����敪','smallint',3,'0') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_sias(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('project_master','shuuryou_kbn','�I���敪','smallint','ENDKU','�����敪','smallint',3,'0') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_mk2(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('project_master','shuuryou_kbn','�I���敪','smallint','ENDKU','�����敪','smallint',3,'0') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_sias(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','doc_type','���ގ��','smallint','KEYNM2','�j�������Q','char',1,'1') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_mk2(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','doc_type','���ގ��','smallint','KEYNM2','�j�������Q','char',1,'1') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_sias(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','item_no','����','smallint','KEYNO','�j�������m��','smallint',2,'1') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_mk2(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','item_no','����','smallint','KEYNO','�j�������m��','smallint',2,'1') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_sias(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','nini_flg','�C�Ӄt���O','smallint','IDATA','���l�f�[�^','smallint',3,'0') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_mk2(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','nini_flg','�C�Ӄt���O','smallint','IDATA','���l�f�[�^','smallint',3,'0') ON CONFLICT DO NOTHING;

--e���������ݒ�֌W�̃}�X�^�[�Ǘ��ꗗ�y�у}�X�^�[�Ǘ��Ő��̃f�[�^�ǉ�
--��荞�݂̎��s��SIAS/mk2�݂̂ɍi�邪�A�e�[�u���Ɗ���l��de3�łł��p�ӂ��Ă����i�K�{�Œ�̎����ŋ�ʂ���̂���������j�̂ŁA�Ő���ꗗ�����ʂƂ���
INSERT INTO master_kanri_ichiran VALUES ('ebunsho_kensaku','e���������ݒ�','0','default','1999-01-01 00:00:00','default','1999-01-01 00:00:00') ON CONFLICT DO NOTHING;
INSERT INTO master_kanri_hansuu VALUES ('ebunsho_kensaku',1,'0','',0,'','','default','1999-01-01 00:00:00','default','1999-01-01 00:00:00') ON CONFLICT DO NOTHING;

--�}�X�^�[�Ǘ��Ő��̎Ј������}�X�^�[���X�V
UPDATE master_kanri_hansuu SET delete_flg = '1' WHERE master_id = 'shain_kouza';
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
   'shain_kouza'		--�Ј������}�X�^�[
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='shain_kouza')
 , '0'
 , '�Ј�����_patch.csv'
 , length(convert_to(E'�Ј��ԍ�,�U������Z�@�֋�s�R�[�h,�U�����s�x�X�R�[�h,�U����a�����,�U��������ԍ�,�U����������`����,�U����������`�i���p�J�i�j,�U�������Z�@�փR�[�h,�U�������Z�@�֎x�X�R�[�h,�U�����a�����,�U���������ԍ�,�����}�ԃR�[�h\r\nshain_no,saki_kinyuukikan_cd,saki_ginkou_shiten_cd,saki_yokin_shabetsu,saki_kouza_bangou,saki_kouza_meigi_kanji,saki_kouza_meigi_kana,moto_kinyuukikan_cd,moto_kinyuukikan_shiten_cd,moto_yokinshubetsu,moto_kouza_bangou,zaimu_edaban_cd\r\nvarchar(15),varchar(4),varchar(3),varchar(1),varchar(7),varchar(30),varchar(30),varchar(4),varchar(3),varchar(1),varchar(7),varchar(12)\r\n1,2,2,2,2,2,2,2,2,2,2,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shain_no || ',' || saki_kinyuukikan_cd || ',' || saki_ginkou_shiten_cd || ',' || saki_yokin_shabetsu || ',' || saki_kouza_bangou || ',' || saki_kouza_meigi_kanji || ',' || saki_kouza_meigi_kana || ',' || moto_kinyuukikan_cd || ',' || moto_kinyuukikan_shiten_cd || ',' || moto_yokinshubetsu || ',' || moto_kouza_bangou || ',' || '' FROM shain_kouza), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'�Ј��ԍ�,�U������Z�@�֋�s�R�[�h,�U�����s�x�X�R�[�h,�U����a�����,�U��������ԍ�,�U����������`����,�U����������`�i���p�J�i�j,�U�������Z�@�փR�[�h,�U�������Z�@�֎x�X�R�[�h,�U�����a�����,�U���������ԍ�,�����}�ԃR�[�h\r\nshain_no,saki_kinyuukikan_cd,saki_ginkou_shiten_cd,saki_yokin_shabetsu,saki_kouza_bangou,saki_kouza_meigi_kanji,saki_kouza_meigi_kana,moto_kinyuukikan_cd,moto_kinyuukikan_shiten_cd,moto_yokinshubetsu,moto_kouza_bangou,zaimu_edaban_cd\r\nvarchar(15),varchar(4),varchar(3),varchar(1),varchar(7),varchar(30),varchar(30),varchar(4),varchar(3),varchar(1),varchar(7),varchar(12)\r\n1,2,2,2,2,2,2,2,2,2,2,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shain_no || ',' || saki_kinyuukikan_cd || ',' || saki_ginkou_shiten_cd || ',' || saki_yokin_shabetsu || ',' || saki_kouza_bangou || ',' || saki_kouza_meigi_kanji || ',' || saki_kouza_meigi_kana || ',' || moto_kinyuukikan_cd || ',' || moto_kinyuukikan_shiten_cd || ',' || moto_yokinshubetsu || ',' || moto_kouza_bangou || ',' || '' FROM shain_kouza), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
);

--houjin_card_import�e�[�u����`�X�V
ALTER TABLE houjin_card_import RENAME TO houjin_card_import_old;
create table houjin_card_import (
  user_id character varying(30) not null PRIMARY KEY
  , houjin_card_shubetsu character varying(3) not null
);

comment on table houjin_card_import is '�@�l�J�[�h�C���|�[�g';
comment on column houjin_card_import.user_id is '���[�U�[ID';
comment on column houjin_card_import.houjin_card_shubetsu is '�@�l�J�[�h���';

INSERT INTO houjin_card_import
SELECT
  user_id
  , houjin_card_shubetsu
FROM houjin_card_import_old;
DROP TABLE houjin_card_import_old;

-- �w�}�X�^�[�f�[�^�X�V
DELETE FROM eki_master;
\copy eki_master FROM '.\files\csv\eki_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';


commit;
