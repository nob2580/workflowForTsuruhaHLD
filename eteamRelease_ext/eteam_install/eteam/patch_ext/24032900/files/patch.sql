SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--JXúÂXú}X^[e[uì¬
create table if not exists kaitenbi_heitenbi_master (
  kesn smallint not null
  , futan_bumon_cd varchar(8) not null
  , stymd date 
  , edymd date 
  , constraint kaitenbi_heitenbi_master_PKEY primary key (kesn,futan_bumon_cd)
);

--UæâsExte[uì¬
create table if not exists torihikisaki_furikomisaki_ext(
  torihikisaki_cd varchar(12) not null
  , ginkou_id smallint not null
  , kouza_meiginin_seishiki varchar(44) not null
  , furikomi_kbn varchar(1) not null
  , tesuuryou_futan_kbn smallint not null
  , constraint torihikisaki_furikomisaki_ext_PKEY primary key (torihikisaki_cd, ginkou_id)
);

--x¥ËExte[uì¬
create table if not exists shiharai_irai_ext(
  denpyou_id varchar(19) not null
  , furikomi_ginkou_id smallint not null
  , constraint shiharai_irai_ext_PKEY primary key (denpyou_id)
);

--JXúÂXú}X^[e[uRg
comment on table kaitenbi_heitenbi_master is 'JXúÂXú}X^[';
comment on column kaitenbi_heitenbi_master.kesn is 'àZú';
comment on column kaitenbi_heitenbi_master.futan_bumon_cd is 'SåR[h';
comment on column kaitenbi_heitenbi_master.stymd is 'JXú';
comment on column kaitenbi_heitenbi_master.edymd is 'ÂXú';

--UæâsExte[uRg
comment on table torihikisaki_furikomisaki_ext is 'æøæUæExt';
comment on column torihikisaki_furikomisaki_ext.torihikisaki_cd is 'æøæR[h';
comment on column torihikisaki_furikomisaki_ext.ginkou_id is 'âsID';
comment on column torihikisaki_furikomisaki_ext.kouza_meiginin_seishiki is 'ûÀ¼`l';
comment on column torihikisaki_furikomisaki_ext.furikomi_kbn is 'Uæª';
comment on column torihikisaki_furikomisaki_ext.tesuuryou_futan_kbn is 'è¿Sæª';

--x¥ËExte[uRg
comment on table shiharai_irai_ext is 'x¥ËExt';
comment on column shiharai_irai_ext.denpyou_id is '`[ID';
comment on column shiharai_irai_ext.furikomi_ginkou_id is 'UâsID';

-- csvÅÇÁ·éÆ« }X^[æêÇÁ
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_kanri_ichiran FROM '.\files\csv\master_kanri_ichiran_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_kanri_hansuu FROM '.\files\csv\master_kanri_hansuu_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;