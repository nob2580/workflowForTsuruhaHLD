SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �A���[No.925 �@�l�J�[�h���p�ꗗ���O�@�\�̒ǉ�
-- �A���[No.803_�����q��ʂ̂�1�y�[�W�\�������ʕێ�
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

-- �A���[No.983 �����R�[�h�ݒ� ���i�`���u���z(�����_����)�v�폜
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- �x���˗��\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- ���������\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

-- ����挟���q��ʂ̕\�����������l��record_num_per_page�̌��l�ɍ��킹��
UPDATE setting_info set setting_val = (SELECT setting_val FROM setting_info WHERE setting_name ='record_num_per_page') WHERE setting_name = 'record_num_per_page_torihikisaki';

-- �@�l�J�[�h���e�[�u���X�V
ALTER TABLE houjin_card_jouhou DROP CONSTRAINT IF EXISTS houjin_card_jouhou_PKEY;
ALTER TABLE houjin_card_jouhou RENAME TO houjin_card_jouhou_old;
create table houjin_card_jouhou (
  card_jouhou_id bigserial not null
  , card_shubetsu character varying(3) not null
  , torikomi_denpyou_id character varying(19) not null
  , busho_cd character varying(15) not null
  , shain_bangou character varying(16) not null
  , shiyousha character varying(30) not null
  , riyoubi date not null
  , kingaku numeric(15) not null
  , card_bangou character varying(16) not null
  , kameiten character varying(60) not null
  , gyoushu_cd character varying(15) not null
  , jyogai_flg character varying(1) not null
  , jyogai_riyuu character varying(60) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint houjin_card_jouhou_PKEY primary key (card_jouhou_id)
);
comment on table houjin_card_jouhou is '�@�l�J�[�h�g�p�������';
comment on column houjin_card_jouhou.card_jouhou_id is '�J�[�h���ID';
comment on column houjin_card_jouhou.card_shubetsu is '�J�[�h��ʃR�[�h';
comment on column houjin_card_jouhou.torikomi_denpyou_id is '�捞��`�[ID';
comment on column houjin_card_jouhou.busho_cd is '�����R�[�h';
comment on column houjin_card_jouhou.shain_bangou is '�Ј��ԍ�';
comment on column houjin_card_jouhou.shiyousha is '�g�p��';
comment on column houjin_card_jouhou.riyoubi is '���p��';
comment on column houjin_card_jouhou.kingaku is '���z';
comment on column houjin_card_jouhou.card_bangou is '�J�[�h�ԍ�';
comment on column houjin_card_jouhou.kameiten is '�����X';
comment on column houjin_card_jouhou.gyoushu_cd is '�Ǝ�R�[�h';
comment on column houjin_card_jouhou.jyogai_flg is '���O�t���O';
comment on column houjin_card_jouhou.jyogai_riyuu is '���O���R';
comment on column houjin_card_jouhou.touroku_user_id is '�o�^���[�U�[ID';
comment on column houjin_card_jouhou.touroku_time is '�o�^����';
comment on column houjin_card_jouhou.koushin_user_id is '�X�V���[�U�[ID';
comment on column houjin_card_jouhou.koushin_time is '�X�V����';
INSERT INTO houjin_card_jouhou
SELECT
card_jouhou_id
  , card_shubetsu
  , torikomi_denpyou_id
  , busho_cd
  , shain_bangou
  , shiyousha
  , riyoubi
  , kingaku
  , card_bangou
  , kameiten
  , gyoushu_cd
  , '0' -- jyogai_flg
  , '' -- jyogai_riyuu
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM houjin_card_jouhou_old;
DROP TABLE houjin_card_jouhou_old;


commit;
