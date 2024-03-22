SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- �d����t���O�ǉ�
ALTER TABLE denpyou_shubetsu_ichiran RENAME TO denpyou_shubetsu_ichiran_old;
create table denpyou_shubetsu_ichiran (
  denpyou_kbn character varying(4) not null
  , version integer default 0 not null
  , denpyou_shubetsu character varying(20) not null
  , denpyou_karibarai_nashi_shubetsu character varying(20)
  , denpyou_print_shubetsu character varying(20)
  , denpyou_print_karibarai_nashi_shubetsu character varying(20)
  , hyouji_jun integer not null
  , gyoumu_shubetsu character varying(20) not null
  , naiyou character varying(160) not null
  , denpyou_shubetsu_url character varying(240) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , kanren_sentaku_flg character varying(1) not null
  , kanren_hyouji_flg character varying(1) not null
  , denpyou_print_flg character varying(1) not null
  , kianbangou_unyou_flg character varying(1) default '0' not null
  , yosan_shikkou_taishou character varying(1) default 'X' not null
  , route_hantei_kingaku character varying(1) default '0' not null
  , route_torihiki_flg character varying(1) default '0' not null
  , shounin_jyoukyou_print_flg character varying(1) default '0' not null
  , shinsei_shori_kengen_name character varying(6) not null
  , shiiresaki_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_kbn)
);
comment on table denpyou_shubetsu_ichiran is '�`�[��ʈꗗ';
comment on column denpyou_shubetsu_ichiran.denpyou_kbn is '�`�[�敪';
comment on column denpyou_shubetsu_ichiran.version is '�o�[�W����';
comment on column denpyou_shubetsu_ichiran.denpyou_shubetsu is '�`�[���';
comment on column denpyou_shubetsu_ichiran.denpyou_karibarai_nashi_shubetsu is '�`�[��ʁi�����Ȃ��j';
comment on column denpyou_shubetsu_ichiran.denpyou_print_shubetsu is '�`�[��ʁi���[�j';
comment on column denpyou_shubetsu_ichiran.denpyou_print_karibarai_nashi_shubetsu is '�`�[��ʁi���[�E�����Ȃ��j';
comment on column denpyou_shubetsu_ichiran.hyouji_jun is '�\����';
comment on column denpyou_shubetsu_ichiran.gyoumu_shubetsu is '�Ɩ����';
comment on column denpyou_shubetsu_ichiran.naiyou is '���e�i�`�[�j';
comment on column denpyou_shubetsu_ichiran.denpyou_shubetsu_url is '�`�[���URL';
comment on column denpyou_shubetsu_ichiran.yuukou_kigen_from is '�L�������J�n��';
comment on column denpyou_shubetsu_ichiran.yuukou_kigen_to is '�L�������I����';
comment on column denpyou_shubetsu_ichiran.kanren_sentaku_flg is '�֘A�`�[�I���t���O';
comment on column denpyou_shubetsu_ichiran.kanren_hyouji_flg is '�֘A�`�[���͗��\���t���O';
comment on column denpyou_shubetsu_ichiran.denpyou_print_flg is '�\�������[�o�̓t���O';
comment on column denpyou_shubetsu_ichiran.kianbangou_unyou_flg is '�N�Ĕԍ��^�p�t���O';
comment on column denpyou_shubetsu_ichiran.yosan_shikkou_taishou is '�\�Z���s�Ώ�';
comment on column denpyou_shubetsu_ichiran.route_hantei_kingaku is '���[�g������z';
comment on column denpyou_shubetsu_ichiran.route_torihiki_flg is '���[�g������ݒ�t���O';
comment on column denpyou_shubetsu_ichiran.shounin_jyoukyou_print_flg is '���F�󋵗�����t���O';
comment on column denpyou_shubetsu_ichiran.shinsei_shori_kengen_name is '�\������������';
comment on column denpyou_shubetsu_ichiran.shiiresaki_flg is '�d����t���O';
comment on column denpyou_shubetsu_ichiran.touroku_user_id is '�o�^���[�U�[ID';
comment on column denpyou_shubetsu_ichiran.touroku_time is '�o�^����';
comment on column denpyou_shubetsu_ichiran.koushin_user_id is '�X�V���[�U�[ID';
comment on column denpyou_shubetsu_ichiran.koushin_time is '�X�V����';
INSERT INTO denpyou_shubetsu_ichiran
SELECT
  denpyou_kbn
  , version
  , denpyou_shubetsu
  , denpyou_karibarai_nashi_shubetsu
  , denpyou_print_shubetsu
  , denpyou_print_karibarai_nashi_shubetsu
  , hyouji_jun
  , gyoumu_shubetsu
  , naiyou
  , denpyou_shubetsu_url
  , yuukou_kigen_from
  , yuukou_kigen_to
  , kanren_sentaku_flg
  , kanren_hyouji_flg
  , denpyou_print_flg
  , kianbangou_unyou_flg
  , yosan_shikkou_taishou
  , route_hantei_kingaku
  , route_torihiki_flg
  , shounin_jyoukyou_print_flg
  , shinsei_shori_kengen_name
  , '0' --shiiresaki_flg
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time   
FROM denpyou_shubetsu_ichiran_old;
DROP TABLE denpyou_shubetsu_ichiran_old;


commit;