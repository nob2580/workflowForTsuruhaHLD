SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--開店日閉店日マスターテーブル作成
create table if not exists kaitenbi_heitenbi_master (
  kesn smallint not null
  , futan_bumon_cd varchar(8) not null
  , stymd date 
  , edymd date 
  , constraint kaitenbi_heitenbi_master_PKEY primary key (kesn,futan_bumon_cd)
);

--振込先銀行Extテーブル作成
create table if not exists torihikisaki_furikomisaki_ext(
  torihikisaki_cd varchar(12) not null
  , ginkou_id smallint not null
  , kouza_meiginin_seishiki varchar(44) not null
  , furikomi_kbn varchar(1) not null
  , tesuuryou_futan_kbn smallint not null
  , constraint torihikisaki_furikomisaki_ext_PKEY primary key (torihikisaki_cd, ginkou_id)
);

--支払依頼Extテーブル作成
create table if not exists shiharai_irai_ext(
  denpyou_id varchar(19) not null
  , furikomi_ginkou_id smallint not null
  , constraint shiharai_irai_ext_PKEY primary key (denpyou_id)
);

--開店日閉店日マスターテーブルコメント
comment on table kaitenbi_heitenbi_master is '開店日閉店日マスター';
comment on column kaitenbi_heitenbi_master.kesn is '内部決算期';
comment on column kaitenbi_heitenbi_master.futan_bumon_cd is '負担部門コード';
comment on column kaitenbi_heitenbi_master.stymd is '開店日';
comment on column kaitenbi_heitenbi_master.edymd is '閉店日';

--振込先銀行Extテーブルコメント
comment on table torihikisaki_furikomisaki_ext is '取引先振込先Ext';
comment on column torihikisaki_furikomisaki_ext.torihikisaki_cd is '取引先コード';
comment on column torihikisaki_furikomisaki_ext.ginkou_id is '銀行ID';
comment on column torihikisaki_furikomisaki_ext.kouza_meiginin_seishiki is '口座名義人';
comment on column torihikisaki_furikomisaki_ext.furikomi_kbn is '振込区分';
comment on column torihikisaki_furikomisaki_ext.tesuuryou_futan_kbn is '手数料負担区分';

--支払依頼Extテーブルコメント
comment on table shiharai_irai_ext is '支払依頼Ext';
comment on column shiharai_irai_ext.denpyou_id is '伝票ID';
comment on column shiharai_irai_ext.furikomi_ginkou_id is '振込銀行ID';

-- csvで追加するとき マスター取込一覧追加
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_kanri_ichiran FROM '.\files\csv\master_kanri_ichiran_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_kanri_hansuu FROM '.\files\csv\master_kanri_hansuu_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_ext.csv' WITH CSV header ENCODING 'SHIFT-JIS';

commit;