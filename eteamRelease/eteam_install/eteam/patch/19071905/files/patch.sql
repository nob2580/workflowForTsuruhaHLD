SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- 連絡票_0908_貸借科目に外貨科目が入力された場合のみ、外貨関連項目を入力できるようにする
-- マスター取込制御
DELETE FROM master_torikomi_ichiran_de3;
DELETE FROM master_torikomi_shousai_de3;
DELETE FROM master_torikomi_ichiran_sias;
DELETE FROM master_torikomi_shousai_sias;
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- 連絡票_0908_貸借科目に外貨科目が入力された場合のみ、外貨関連項目を入力できるようにする
-- 科目マスター
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
comment on table kamoku_master is '科目マスター';
comment on column kamoku_master.kamoku_gaibu_cd is '科目外部コード';
comment on column kamoku_master.kamoku_naibu_cd is '科目内部コード';
comment on column kamoku_master.kamoku_name_ryakushiki is '科目名（略式）';
comment on column kamoku_master.kamoku_name_seishiki is '科目名（正式）';
comment on column kamoku_master.kessanki_bangou is '決算期番号';
comment on column kamoku_master.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column kamoku_master.taishaku_zokusei is '貸借属性';
comment on column kamoku_master.kamoku_group_kbn is '科目グループ区分';
comment on column kamoku_master.kamoku_group_bangou is '科目グループ番号';
comment on column kamoku_master.shori_group is '処理グループ';
comment on column kamoku_master.taikakingaku_nyuuryoku_flg is '対価金額入力フラグ';
comment on column kamoku_master.kazei_kbn is '課税区分';
comment on column kamoku_master.bunri_kbn is '分離区分';
comment on column kamoku_master.shiire_kbn is '仕入区分';
comment on column kamoku_master.gyousha_kbn is '業種区分';
comment on column kamoku_master.zeiritsu_kbn is '税率区分';
comment on column kamoku_master.tokusha_hyouji_kbn is '特殊表示区分';
comment on column kamoku_master.edaban_minyuuryoku_check is '枝番未入力チェック';
comment on column kamoku_master.torihikisaki_minyuuryoku_check is '取引先未入力チェック';
comment on column kamoku_master.bumon_minyuuryoku_check is '部門未入力チェック';
comment on column kamoku_master.bumon_edaban_flg is '部門科目枝番使用フラグ';
comment on column kamoku_master.segment_minyuuryoku_check is 'セグメント未入力チェック';
comment on column kamoku_master.project_minyuuryoku_check is 'プロジェクト未入力チェック';
comment on column kamoku_master.uf1_minyuuryoku_check is 'ユニバーサルフィールド１未入力チェック';
comment on column kamoku_master.uf2_minyuuryoku_check is 'ユニバーサルフィールド２未入力チェック';
comment on column kamoku_master.uf3_minyuuryoku_check is 'ユニバーサルフィールド３未入力チェック';
comment on column kamoku_master.uf4_minyuuryoku_check is 'ユニバーサルフィールド４未入力チェック';
comment on column kamoku_master.uf5_minyuuryoku_check is 'ユニバーサルフィールド５未入力チェック';
comment on column kamoku_master.uf6_minyuuryoku_check is 'ユニバーサルフィールド６未入力チェック';
comment on column kamoku_master.uf7_minyuuryoku_check is 'ユニバーサルフィールド７未入力チェック';
comment on column kamoku_master.uf8_minyuuryoku_check is 'ユニバーサルフィールド８未入力チェック';
comment on column kamoku_master.uf9_minyuuryoku_check is 'ユニバーサルフィールド９未入力チェック';
comment on column kamoku_master.uf10_minyuuryoku_check is 'ユニバーサルフィールド１０未入力チェック';
comment on column kamoku_master.uf_kotei1_minyuuryoku_check is 'ユニバーサルフィールド１未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei2_minyuuryoku_check is 'ユニバーサルフィールド２未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei3_minyuuryoku_check is 'ユニバーサルフィールド３未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei4_minyuuryoku_check is 'ユニバーサルフィールド４未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei5_minyuuryoku_check is 'ユニバーサルフィールド５未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei6_minyuuryoku_check is 'ユニバーサルフィールド６未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei7_minyuuryoku_check is 'ユニバーサルフィールド７未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei8_minyuuryoku_check is 'ユニバーサルフィールド８未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei9_minyuuryoku_check is 'ユニバーサルフィールド９未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei10_minyuuryoku_check is 'ユニバーサルフィールド１０未入力チェック(固定値)';
comment on column kamoku_master.kouji_minyuuryoku_check is '工事未入力チェック';
comment on column kamoku_master.kousha_minyuuryoku_check is '工種未入力チェック';
comment on column kamoku_master.tekiyou_cd_minyuuryoku_check is '摘要コード未入力チェック';
comment on column kamoku_master.bumon_torhikisaki_kamoku_flg is '部門取引先科目使用フラグ';
comment on column kamoku_master.bumon_torihikisaki_edaban_shiyou_flg is '部門取引先科目枝番使用フラグ';
comment on column kamoku_master.torihikisaki_kamoku_edaban_flg is '取引先科目枝番使用フラグ';
comment on column kamoku_master.segment_torihikisaki_kamoku_flg is 'セグメント取引先科目使用フラグ';
comment on column kamoku_master.karikanjou_keshikomi_no_flg is '仮勘定消込No使用フラグ';
comment on column kamoku_master.gaika_flg is '外貨使用フラグ';
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


-- WF一般オプションはパッチによるUPDATE後ではONとする
UPDATE setting_info SET setting_val ='1' WHERE setting_name = 'ippan_option';


commit;
