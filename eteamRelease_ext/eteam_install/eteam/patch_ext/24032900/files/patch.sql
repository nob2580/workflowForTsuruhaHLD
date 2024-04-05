SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--�J�X���X���}�X�^�[�e�[�u���쐬
create table if not exists kaitenbi_heitenbi_master (
  kesn smallint not null
  , futan_bumon_cd varchar(8) not null
  , stymd date 
  , edymd date 
  , constraint kaitenbi_heitenbi_master_PKEY primary key (kesn,futan_bumon_cd)
);

--�U�����sExt�e�[�u���쐬
create table if not exists torihikisaki_furikomisaki_ext(
  torihikisaki_cd varchar(12) not null
  , ginkou_id smallint not null
  , kouza_meiginin_seishiki varchar(44) not null
  , furikomi_kbn varchar(1) not null
  , tesuuryou_futan_kbn smallint not null
  , constraint torihikisaki_furikomisaki_ext_PKEY primary key (torihikisaki_cd, ginkou_id)
);

--�x���˗�Ext�e�[�u���쐬
create table if not exists shiharai_irai_ext(
  denpyou_id varchar(19) not null
  , furikomi_ginkou_id smallint not null
  , constraint shiharai_irai_ext_PKEY primary key (denpyou_id)
);

--�J�X���X���}�X�^�[�e�[�u���R�����g
comment on table kaitenbi_heitenbi_master is '�J�X���X���}�X�^�[';
comment on column kaitenbi_heitenbi_master.kesn is '�������Z��';
comment on column kaitenbi_heitenbi_master.futan_bumon_cd is '���S����R�[�h';
comment on column kaitenbi_heitenbi_master.stymd is '�J�X��';
comment on column kaitenbi_heitenbi_master.edymd is '�X��';

--�U�����sExt�e�[�u���R�����g
comment on table torihikisaki_furikomisaki_ext is '�����U����Ext';
comment on column torihikisaki_furikomisaki_ext.torihikisaki_cd is '�����R�[�h';
comment on column torihikisaki_furikomisaki_ext.ginkou_id is '��sID';
comment on column torihikisaki_furikomisaki_ext.kouza_meiginin_seishiki is '�������`�l';
comment on column torihikisaki_furikomisaki_ext.furikomi_kbn is '�U���敪';
comment on column torihikisaki_furikomisaki_ext.tesuuryou_futan_kbn is '�萔�����S�敪';

--�x���˗�Ext�e�[�u���R�����g
comment on table shiharai_irai_ext is '�x���˗�Ext';
comment on column shiharai_irai_ext.denpyou_id is '�`�[ID';
comment on column shiharai_irai_ext.furikomi_ginkou_id is '�U����sID';

-- csv�Œǉ�����Ƃ� �}�X�^�[�捞�ꗗ�ǉ�
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_kanri_ichiran FROM '.\files\csv\master_kanri_ichiran_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_kanri_hansuu FROM '.\files\csv\master_kanri_hansuu_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;