SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- 0703_海外日当対応 設定情報データ変更 ※hyouji_jun900以降はカスタマイズ領域
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

-- 0703_海外日当対応 画面項目制御
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

-- 0703_海外日当対応 海外日当等マスター
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
comment on table kaigai_nittou_nado_master is '海外用日当等マスター';
comment on column kaigai_nittou_nado_master.shubetsu1 is '種別１';
comment on column kaigai_nittou_nado_master.shubetsu2 is '種別２';
comment on column kaigai_nittou_nado_master.yakushoku_cd is '役職コード';
comment on column kaigai_nittou_nado_master.tanka is '単価（邦貨）';
comment on column kaigai_nittou_nado_master.heishu_cd is '幣種コード';
comment on column kaigai_nittou_nado_master.currency_unit is '通貨単位';
comment on column kaigai_nittou_nado_master.tanka_gaika is '単価（外貨）';
comment on column kaigai_nittou_nado_master.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column kaigai_nittou_nado_master.nittou_shukuhakuhi_flg is '日当・宿泊費フラグ';
comment on column kaigai_nittou_nado_master.zei_kubun is '税区分';
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

-- 軽減税率対応 内部コード
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
DROP TABLE naibu_cd_setting_tmp;

-- 軽減税率対応 仕訳パターン設定
CREATE TABLE shiwake_pattern_setting_tmp AS SELECT * FROM shiwake_pattern_setting WHERE 1 = 2;
\copy shiwake_pattern_setting_tmp FROM '.\files\csv\shiwake_pattern_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM shiwake_pattern_setting_tmp WHERE denpyou_kbn='A003' AND (SELECT COUNT(*) FROM shiwake_pattern_setting WHERE denpyou_kbn='A003') = 0;
DELETE FROM shiwake_pattern_setting_tmp WHERE denpyou_kbn='A013' AND (SELECT COUNT(*) FROM shiwake_pattern_setting WHERE denpyou_kbn='A013') = 0;
DELETE FROM shiwake_pattern_setting;
INSERT INTO shiwake_pattern_setting SELECT * FROM shiwake_pattern_setting_tmp;
DROP TABLE shiwake_pattern_setting_tmp;

--軽減税率対応 仕訳パターン変数設定
DELETE FROM shiwake_pattern_var_setting;
\copy shiwake_pattern_var_setting FROM '.\files\csv\shiwake_pattern_var_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- 軽減税率対応 仕訳パターンマスター
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
comment on table shiwake_pattern_master is '仕訳パターンマスター';
comment on column shiwake_pattern_master.denpyou_kbn is '伝票区分';
comment on column shiwake_pattern_master.shiwake_edano is '仕訳枝番号';
comment on column shiwake_pattern_master.delete_flg is '削除フラグ';
comment on column shiwake_pattern_master.yuukou_kigen_from is '有効期限開始日';
comment on column shiwake_pattern_master.yuukou_kigen_to is '有効期限終了日';
comment on column shiwake_pattern_master.bunrui1 is '分類1';
comment on column shiwake_pattern_master.bunrui2 is '分類2';
comment on column shiwake_pattern_master.bunrui3 is '分類3';
comment on column shiwake_pattern_master.torihiki_name is '取引名';
comment on column shiwake_pattern_master.tekiyou_flg is '摘要フラグ';
comment on column shiwake_pattern_master.tekiyou is '摘要';
comment on column shiwake_pattern_master.default_hyouji_flg is 'デフォルト表示フラグ';
comment on column shiwake_pattern_master.kousaihi_hyouji_flg is '交際費表示フラグ';
comment on column shiwake_pattern_master.kake_flg is '掛けフラグ';
comment on column shiwake_pattern_master.hyouji_jun is '表示順';
comment on column shiwake_pattern_master.shain_cd_renkei_flg is '社員コード連携フラグ';
comment on column shiwake_pattern_master.kari_futan_bumon_cd is '借方負担部門コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_kamoku_cd is '借方科目コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_kamoku_edaban_cd is '借方科目枝番コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_torihikisaki_cd is '借方取引先コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_project_cd is '借方プロジェクトコード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_segment_cd is '借方セグメントコード';
comment on column shiwake_pattern_master.kari_uf1_cd is '借方UF1コード';
comment on column shiwake_pattern_master.kari_uf2_cd is '借方UF2コード';
comment on column shiwake_pattern_master.kari_uf3_cd is '借方UF3コード';
comment on column shiwake_pattern_master.kari_uf4_cd is '借方UF4コード';
comment on column shiwake_pattern_master.kari_uf5_cd is '借方UF5コード';
comment on column shiwake_pattern_master.kari_uf6_cd is '借方UF6コード';
comment on column shiwake_pattern_master.kari_uf7_cd is '借方UF7コード';
comment on column shiwake_pattern_master.kari_uf8_cd is '借方UF8コード';
comment on column shiwake_pattern_master.kari_uf9_cd is '借方UF9コード';
comment on column shiwake_pattern_master.kari_uf10_cd is '借方UF10コード';
comment on column shiwake_pattern_master.kari_uf_kotei1_cd is '借方UF1コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei2_cd is '借方UF2コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei3_cd is '借方UF3コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei4_cd is '借方UF4コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei5_cd is '借方UF5コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei6_cd is '借方UF6コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei7_cd is '借方UF7コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei8_cd is '借方UF8コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei9_cd is '借方UF9コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei10_cd is '借方UF10コード（固定）';
comment on column shiwake_pattern_master.kari_kazei_kbn is '借方課税区分（仕訳パターン）';
comment on column shiwake_pattern_master.kari_zeiritsu is '借方消費税率（仕訳パターン）';
comment on column shiwake_pattern_master.kari_keigen_zeiritsu_kbn is '借方軽減税率区分（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd1 is '貸方負担部門コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd1 is '貸方科目コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd1 is '貸方科目枝番コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd1 is '貸方取引先コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd1 is '貸方プロジェクトコード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd1 is '貸方セグメントコード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd1 is '貸方UF1コード１';
comment on column shiwake_pattern_master.kashi_uf2_cd1 is '貸方UF2コード１';
comment on column shiwake_pattern_master.kashi_uf3_cd1 is '貸方UF3コード１';
comment on column shiwake_pattern_master.kashi_uf4_cd1 is '貸方UF4コード１';
comment on column shiwake_pattern_master.kashi_uf5_cd1 is '貸方UF5コード１';
comment on column shiwake_pattern_master.kashi_uf6_cd1 is '貸方UF6コード１';
comment on column shiwake_pattern_master.kashi_uf7_cd1 is '貸方UF7コード１';
comment on column shiwake_pattern_master.kashi_uf8_cd1 is '貸方UF8コード１';
comment on column shiwake_pattern_master.kashi_uf9_cd1 is '貸方UF9コード１';
comment on column shiwake_pattern_master.kashi_uf10_cd1 is '貸方UF10コード１';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd1 is '貸方UF1コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd1 is '貸方UF2コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd1 is '貸方UF3コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd1 is '貸方UF4コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd1 is '貸方UF5コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd1 is '貸方UF6コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd1 is '貸方UF7コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd1 is '貸方UF8コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd1 is '貸方UF9コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd1 is '貸方UF10コード１（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn1 is '貸方課税区分１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd2 is '貸方負担部門コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd2 is '貸方取引先コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd2 is '貸方科目コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd2 is '貸方科目枝番コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd2 is '貸方プロジェクトコード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd2 is '貸方セグメントコード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd2 is '貸方UF1コード２';
comment on column shiwake_pattern_master.kashi_uf2_cd2 is '貸方UF2コード２';
comment on column shiwake_pattern_master.kashi_uf3_cd2 is '貸方UF3コード２';
comment on column shiwake_pattern_master.kashi_uf4_cd2 is '貸方UF4コード２';
comment on column shiwake_pattern_master.kashi_uf5_cd2 is '貸方UF5コード２';
comment on column shiwake_pattern_master.kashi_uf6_cd2 is '貸方UF6コード２';
comment on column shiwake_pattern_master.kashi_uf7_cd2 is '貸方UF7コード２';
comment on column shiwake_pattern_master.kashi_uf8_cd2 is '貸方UF8コード２';
comment on column shiwake_pattern_master.kashi_uf9_cd2 is '貸方UF9コード２';
comment on column shiwake_pattern_master.kashi_uf10_cd2 is '貸方UF10コード２';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd2 is '貸方UF1コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd2 is '貸方UF2コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd2 is '貸方UF3コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd2 is '貸方UF4コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd2 is '貸方UF5コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd2 is '貸方UF6コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd2 is '貸方UF7コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd2 is '貸方UF8コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd2 is '貸方UF9コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd2 is '貸方UF10コード２（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn2 is '貸方課税区分２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd3 is '貸方負担部門コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd3 is '貸方取引先コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd3 is '貸方科目コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd3 is '貸方科目枝番コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd3 is '貸方プロジェクトコード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd3 is '貸方セグメントコード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd3 is '貸方UF1コード３';
comment on column shiwake_pattern_master.kashi_uf2_cd3 is '貸方UF2コード３';
comment on column shiwake_pattern_master.kashi_uf3_cd3 is '貸方UF3コード３';
comment on column shiwake_pattern_master.kashi_uf4_cd3 is '貸方UF4コード３';
comment on column shiwake_pattern_master.kashi_uf5_cd3 is '貸方UF5コード３';
comment on column shiwake_pattern_master.kashi_uf6_cd3 is '貸方UF6コード３';
comment on column shiwake_pattern_master.kashi_uf7_cd3 is '貸方UF7コード３';
comment on column shiwake_pattern_master.kashi_uf8_cd3 is '貸方UF8コード３';
comment on column shiwake_pattern_master.kashi_uf9_cd3 is '貸方UF9コード３';
comment on column shiwake_pattern_master.kashi_uf10_cd3 is '貸方UF10コード３';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd3 is '貸方UF1コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd3 is '貸方UF2コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd3 is '貸方UF3コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd3 is '貸方UF4コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd3 is '貸方UF5コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd3 is '貸方UF6コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd3 is '貸方UF7コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd3 is '貸方UF8コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd3 is '貸方UF9コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd3 is '貸方UF10コード３（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn3 is '貸方課税区分３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd4 is '貸方負担部門コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd4 is '貸方取引先コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd4 is '貸方科目コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd4 is '貸方科目枝番コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd4 is '貸方プロジェクトコード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd4 is '貸方セグメントコード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd4 is '貸方UF1コード４';
comment on column shiwake_pattern_master.kashi_uf2_cd4 is '貸方UF2コード４';
comment on column shiwake_pattern_master.kashi_uf3_cd4 is '貸方UF3コード４';
comment on column shiwake_pattern_master.kashi_uf4_cd4 is '貸方UF4コード４';
comment on column shiwake_pattern_master.kashi_uf5_cd4 is '貸方UF5コード４';
comment on column shiwake_pattern_master.kashi_uf6_cd4 is '貸方UF6コード４';
comment on column shiwake_pattern_master.kashi_uf7_cd4 is '貸方UF7コード４';
comment on column shiwake_pattern_master.kashi_uf8_cd4 is '貸方UF8コード４';
comment on column shiwake_pattern_master.kashi_uf9_cd4 is '貸方UF9コード４';
comment on column shiwake_pattern_master.kashi_uf10_cd4 is '貸方UF10コード４';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd4 is '貸方UF1コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd4 is '貸方UF2コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd4 is '貸方UF3コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd4 is '貸方UF4コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd4 is '貸方UF5コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd4 is '貸方UF6コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd4 is '貸方UF7コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd4 is '貸方UF8コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd4 is '貸方UF9コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd4 is '貸方UF10コード４（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn4 is '貸方課税区分４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd5 is '貸方負担部門コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd5 is '貸方取引先コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd5 is '貸方科目コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd5 is '貸方科目枝番コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd5 is '貸方プロジェクトコード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd5 is '貸方セグメントコード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd5 is '貸方UF1コード５';
comment on column shiwake_pattern_master.kashi_uf2_cd5 is '貸方UF2コード５';
comment on column shiwake_pattern_master.kashi_uf3_cd5 is '貸方UF3コード５';
comment on column shiwake_pattern_master.kashi_uf4_cd5 is '貸方UF4コード５';
comment on column shiwake_pattern_master.kashi_uf5_cd5 is '貸方UF5コード５';
comment on column shiwake_pattern_master.kashi_uf6_cd5 is '貸方UF6コード５';
comment on column shiwake_pattern_master.kashi_uf7_cd5 is '貸方UF7コード５';
comment on column shiwake_pattern_master.kashi_uf8_cd5 is '貸方UF8コード５';
comment on column shiwake_pattern_master.kashi_uf9_cd5 is '貸方UF9コード５';
comment on column shiwake_pattern_master.kashi_uf10_cd5 is '貸方UF10コード５';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd5 is '貸方UF1コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd5 is '貸方UF2コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd5 is '貸方UF3コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd5 is '貸方UF4コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd5 is '貸方UF5コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd5 is '貸方UF6コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd5 is '貸方UF7コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd5 is '貸方UF8コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd5 is '貸方UF9コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd5 is '貸方UF10コード５（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn5 is '貸方課税区分５（仕訳パターン）';
comment on column shiwake_pattern_master.touroku_user_id is '登録ユーザーID';
comment on column shiwake_pattern_master.touroku_time is '登録日時';
comment on column shiwake_pattern_master.koushin_user_id is '更新ユーザーID';
comment on column shiwake_pattern_master.koushin_time is '更新日時';
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

-- 消費税率マスター 定義更新
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
comment on table shouhizeiritsu is '消費税率';
comment on column shouhizeiritsu.sort_jun is '並び順';
comment on column shouhizeiritsu.zeiritsu is '税率';
comment on column shouhizeiritsu.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column shouhizeiritsu.hasuu_keisan_kbn is '端数計算区分';
comment on column shouhizeiritsu.yuukou_kigen_from is '有効期限開始日';
comment on column shouhizeiritsu.yuukou_kigen_to is '有効期限終了日';

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

-- 軽減税率区分 マスター管理版数（消費税率）
-- 0703_海外日当対応 マスター管理版数（海外日当等マスター）
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
   'shouhizeiritsu'		--消費税率
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='shouhizeiritsu')
 , '0'
 , '消費税率_patch.csv'
 , length(convert_to(E'並び順,税率,軽減税率区分,端数計算区分,有効期限開始日,有効期限終了日\r\nsort_jun,zeiritsu,keigen_zeiritsu_kbn,hasuu_keisan_kbn,yuukou_kigen_from,yuukou_kigen_to\r\nvarchar(3),decimal(3),varchar(1),varchar(1),date,date\r\n1,1,,,,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || zeiritsu || ',' || keigen_zeiritsu_kbn || ',' || hasuu_keisan_kbn || ',' || replace(yuukou_kigen_from::text,'-'::text,'/'::text) || ',' || replace(yuukou_kigen_to::text,'-'::text,'/'::text) FROM shouhizeiritsu), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'並び順,税率,軽減税率区分,端数計算区分,有効期限開始日,有効期限終了日\r\nsort_jun,zeiritsu,keigen_zeiritsu_kbn,hasuu_keisan_kbn,yuukou_kigen_from,yuukou_kigen_to\r\nvarchar(3),decimal(3),varchar(1),varchar(1),date,date\r\n1,1,,,,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || zeiritsu || ',' || keigen_zeiritsu_kbn || ',' || hasuu_keisan_kbn || ',' || replace(yuukou_kigen_from::text,'-'::text,'/'::text) || ',' || replace(yuukou_kigen_to::text,'-'::text,'/'::text) FROM shouhizeiritsu), E'\r\n') || E'\r\n','sjis')
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
   'kaigai_nittou_nado_master'		--海外日当等マスター
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='kaigai_nittou_nado_master')
 , '0'
 , '海外日当等_patch.csv'
 , length(convert_to(E'種別１,種別２,役職コード,単価（邦貨）,幣種コード,通貨単位,単価（外貨）,証憑書類必須フラグ,日当・宿泊費フラグ,税区分\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,heishu_cd,currency_unit,tanka_gaika,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(4),varchar(20),decimal,varchar(1),varchar(1),varchar(1)\r\n1,1,1,,2,2,,2,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || heishu_cd || ',' || currency_unit || ',' || COALESCE(tanka_gaika::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun FROM kaigai_nittou_nado_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'種別１,種別２,役職コード,単価（邦貨）,幣種コード,通貨単位,単価（外貨）,証憑書類必須フラグ,日当・宿泊費フラグ,税区分\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,heishu_cd,currency_unit,tanka_gaika,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(4),varchar(20),decimal,varchar(1),varchar(1),varchar(1)\r\n1,1,1,,2,2,,2,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || heishu_cd || ',' || currency_unit || ',' || COALESCE(tanka_gaika::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun FROM kaigai_nittou_nado_master), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
 );


-- 振替伝票テーブル 定義更新
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
comment on table furikae is '振替';
comment on column furikae.denpyou_id is '伝票ID';
comment on column furikae.denpyou_date is '伝票日付';
comment on column furikae.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column furikae.kingaku is '金額';
comment on column furikae.hontai_kingaku is '本体金額';
comment on column furikae.shouhizeigaku is '消費税額';
comment on column furikae.zeiritsu is '税率';
comment on column furikae.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column furikae.tekiyou is '摘要';
comment on column furikae.hf1_cd is 'HF1コード';
comment on column furikae.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column furikae.hf2_cd is 'HF2コード';
comment on column furikae.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column furikae.hf3_cd is 'HF3コード';
comment on column furikae.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column furikae.hf4_cd is 'HF4コード';
comment on column furikae.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column furikae.hf5_cd is 'HF5コード';
comment on column furikae.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column furikae.hf6_cd is 'HF6コード';
comment on column furikae.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column furikae.hf7_cd is 'HF7コード';
comment on column furikae.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column furikae.hf8_cd is 'HF8コード';
comment on column furikae.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column furikae.hf9_cd is 'HF9コード';
comment on column furikae.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column furikae.hf10_cd is 'HF10コード';
comment on column furikae.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column furikae.bikou is '備考（会計伝票）';
comment on column furikae.kari_futan_bumon_cd is '借方負担部門コード';
comment on column furikae.kari_futan_bumon_name is '借方負担部門名';
comment on column furikae.kari_kamoku_cd is '借方科目コード';
comment on column furikae.kari_kamoku_name is '借方科目名';
comment on column furikae.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column furikae.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column furikae.kari_kazei_kbn is '借方課税区分';
comment on column furikae.kari_torihikisaki_cd is '借方取引先コード';
comment on column furikae.kari_torihikisaki_name_ryakushiki is '借方取引先名（略式）';
comment on column furikae.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column furikae.kashi_futan_bumon_name is '貸方負担部門名';
comment on column furikae.kashi_kamoku_cd is '貸方科目コード';
comment on column furikae.kashi_kamoku_name is '貸方科目名';
comment on column furikae.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column furikae.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column furikae.kashi_kazei_kbn is '貸方課税区分';
comment on column furikae.kashi_torihikisaki_cd is '貸方取引先コード';
comment on column furikae.kashi_torihikisaki_name_ryakushiki is '貸方取引先名（略式）';
comment on column furikae.kari_uf1_cd is '借方UF1コード';
comment on column furikae.kari_uf1_name_ryakushiki is '借方UF1名（略式）';
comment on column furikae.kari_uf2_cd is '借方UF2コード';
comment on column furikae.kari_uf2_name_ryakushiki is '借方UF2名（略式）';
comment on column furikae.kari_uf3_cd is '借方UF3コード';
comment on column furikae.kari_uf3_name_ryakushiki is '借方UF3名（略式）';
comment on column furikae.kari_uf4_cd is '借方UF4コード';
comment on column furikae.kari_uf4_name_ryakushiki is '借方UF4名（略式）';
comment on column furikae.kari_uf5_cd is '借方UF5コード';
comment on column furikae.kari_uf5_name_ryakushiki is '借方UF5名（略式）';
comment on column furikae.kari_uf6_cd is '借方UF6コード';
comment on column furikae.kari_uf6_name_ryakushiki is '借方UF6名（略式）';
comment on column furikae.kari_uf7_cd is '借方UF7コード';
comment on column furikae.kari_uf7_name_ryakushiki is '借方UF7名（略式）';
comment on column furikae.kari_uf8_cd is '借方UF8コード';
comment on column furikae.kari_uf8_name_ryakushiki is '借方UF8名（略式）';
comment on column furikae.kari_uf9_cd is '借方UF9コード';
comment on column furikae.kari_uf9_name_ryakushiki is '借方UF9名（略式）';
comment on column furikae.kari_uf10_cd is '借方UF10コード';
comment on column furikae.kari_uf10_name_ryakushiki is '借方UF10名（略式）';
comment on column furikae.kashi_uf1_cd is '貸方UF1コード';
comment on column furikae.kashi_uf1_name_ryakushiki is '貸方UF1名（略式）';
comment on column furikae.kashi_uf2_cd is '貸方UF2コード';
comment on column furikae.kashi_uf2_name_ryakushiki is '貸方UF2名（略式）';
comment on column furikae.kashi_uf3_cd is '貸方UF3コード';
comment on column furikae.kashi_uf3_name_ryakushiki is '貸方UF3名（略式）';
comment on column furikae.kashi_uf4_cd is '貸方UF4コード';
comment on column furikae.kashi_uf4_name_ryakushiki is '貸方UF4名（略式）';
comment on column furikae.kashi_uf5_cd is '貸方UF5コード';
comment on column furikae.kashi_uf5_name_ryakushiki is '貸方UF5名（略式）';
comment on column furikae.kashi_uf6_cd is '貸方UF6コード';
comment on column furikae.kashi_uf6_name_ryakushiki is '貸方UF6名（略式）';
comment on column furikae.kashi_uf7_cd is '貸方UF7コード';
comment on column furikae.kashi_uf7_name_ryakushiki is '貸方UF7名（略式）';
comment on column furikae.kashi_uf8_cd is '貸方UF8コード';
comment on column furikae.kashi_uf8_name_ryakushiki is '貸方UF8名（略式）';
comment on column furikae.kashi_uf9_cd is '貸方UF9コード';
comment on column furikae.kashi_uf9_name_ryakushiki is '貸方UF9名（略式）';
comment on column furikae.kashi_uf10_cd is '貸方UF10コード';
comment on column furikae.kashi_uf10_name_ryakushiki is '貸方UF10名（略式）';
comment on column furikae.kari_project_cd is '借方プロジェクトコード';
comment on column furikae.kari_project_name is '借方プロジェクト名';
comment on column furikae.kari_segment_cd is '借方セグメントコード';
comment on column furikae.kari_segment_name_ryakushiki is '借方セグメント名（略式）';
comment on column furikae.kashi_project_cd is '貸方プロジェクトコード';
comment on column furikae.kashi_project_name is '貸方プロジェクト名';
comment on column furikae.kashi_segment_cd is '貸方セグメントコード';
comment on column furikae.kashi_segment_name_ryakushiki is '貸方セグメント名（略式）';
comment on column furikae.touroku_user_id is '登録ユーザーID';
comment on column furikae.touroku_time is '登録日時';
comment on column furikae.koushin_user_id is '更新ユーザーID';
comment on column furikae.koushin_time is '更新日時';

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

-- 総合付替伝票 定義変更
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
comment on table tsukekae is '付替';
comment on column tsukekae.denpyou_id is '伝票ID';
comment on column tsukekae.denpyou_date is '伝票日付';
comment on column tsukekae.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column tsukekae.zeiritsu is '税率';
comment on column tsukekae.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column tsukekae.kingaku_goukei is '金額合計';
comment on column tsukekae.hf1_cd is 'HF1コード';
comment on column tsukekae.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column tsukekae.hf2_cd is 'HF2コード';
comment on column tsukekae.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column tsukekae.hf3_cd is 'HF3コード';
comment on column tsukekae.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column tsukekae.hf4_cd is 'HF4コード';
comment on column tsukekae.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column tsukekae.hf5_cd is 'HF5コード';
comment on column tsukekae.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column tsukekae.hf6_cd is 'HF6コード';
comment on column tsukekae.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column tsukekae.hf7_cd is 'HF7コード';
comment on column tsukekae.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column tsukekae.hf8_cd is 'HF8コード';
comment on column tsukekae.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column tsukekae.hf9_cd is 'HF9コード';
comment on column tsukekae.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column tsukekae.hf10_cd is 'HF10コード';
comment on column tsukekae.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column tsukekae.hosoku is '補足';
comment on column tsukekae.tsukekae_kbn is '付替区分';
comment on column tsukekae.moto_kamoku_cd is '付替元科目コード';
comment on column tsukekae.moto_kamoku_name is '付替元科目名';
comment on column tsukekae.moto_kamoku_edaban_cd is '付替元科目枝番コード';
comment on column tsukekae.moto_kamoku_edaban_name is '付替元科目枝番名';
comment on column tsukekae.moto_futan_bumon_cd is '付替元負担部門コード';
comment on column tsukekae.moto_futan_bumon_name is '付替元負担部門名';
comment on column tsukekae.moto_torihikisaki_cd is '付替元取引先コード';
comment on column tsukekae.moto_torihikisaki_name_ryakushiki is '付替元取引先名（略式）';
comment on column tsukekae.moto_kazei_kbn is '付替元課税区分';
comment on column tsukekae.moto_uf1_cd is '付替元UF1コード';
comment on column tsukekae.moto_uf1_name_ryakushiki is '付替元UF1名（略式）';
comment on column tsukekae.moto_uf2_cd is '付替元UF2コード';
comment on column tsukekae.moto_uf2_name_ryakushiki is '付替元UF2名（略式）';
comment on column tsukekae.moto_uf3_cd is '付替元UF3コード';
comment on column tsukekae.moto_uf3_name_ryakushiki is '付替元UF3名（略式）';
comment on column tsukekae.moto_uf4_cd is '付替元UF4コード';
comment on column tsukekae.moto_uf4_name_ryakushiki is '付替元UF4名（略式）';
comment on column tsukekae.moto_uf5_cd is '付替元UF5コード';
comment on column tsukekae.moto_uf5_name_ryakushiki is '付替元UF5名（略式）';
comment on column tsukekae.moto_uf6_cd is '付替元UF6コード';
comment on column tsukekae.moto_uf6_name_ryakushiki is '付替元UF6名（略式）';
comment on column tsukekae.moto_uf7_cd is '付替元UF7コード';
comment on column tsukekae.moto_uf7_name_ryakushiki is '付替元UF7名（略式）';
comment on column tsukekae.moto_uf8_cd is '付替元UF8コード';
comment on column tsukekae.moto_uf8_name_ryakushiki is '付替元UF8名（略式）';
comment on column tsukekae.moto_uf9_cd is '付替元UF9コード';
comment on column tsukekae.moto_uf9_name_ryakushiki is '付替元UF9名（略式）';
comment on column tsukekae.moto_uf10_cd is '付替元UF10コード';
comment on column tsukekae.moto_uf10_name_ryakushiki is '付替元UF10名（略式）';
comment on column tsukekae.moto_project_cd is '付替元プロジェクトコード';
comment on column tsukekae.moto_project_name is '付替元プロジェクト名';
comment on column tsukekae.moto_segment_cd is '付替元セグメントコード';
comment on column tsukekae.moto_segment_name_ryakushiki is '付替元セグメント名（略式）';
comment on column tsukekae.touroku_user_id is '登録ユーザーID';
comment on column tsukekae.touroku_time is '登録日時';
comment on column tsukekae.koushin_user_id is '更新ユーザーID';
comment on column tsukekae.koushin_time is '更新日時';

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

-- 軽減税率対応 支払依頼明細
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
comment on table shiharai_irai_meisai is '支払依頼明細';
comment on column shiharai_irai_meisai.denpyou_id is '伝票ID';
comment on column shiharai_irai_meisai.denpyou_edano is '伝票枝番号';
comment on column shiharai_irai_meisai.shiwake_edano is '仕訳枝番号';
comment on column shiharai_irai_meisai.torihiki_name is '取引名';
comment on column shiharai_irai_meisai.tekiyou is '摘要';
comment on column shiharai_irai_meisai.shiharai_kingaku is '支払金額';
comment on column shiharai_irai_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column shiharai_irai_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column shiharai_irai_meisai.kari_kamoku_cd is '借方科目コード';
comment on column shiharai_irai_meisai.kari_kamoku_name is '借方科目名';
comment on column shiharai_irai_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column shiharai_irai_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column shiharai_irai_meisai.kari_kazei_kbn is '借方課税区分';
comment on column shiharai_irai_meisai.zeiritsu is '税率';
comment on column shiharai_irai_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column shiharai_irai_meisai.uf1_cd is 'UF1コード';
comment on column shiharai_irai_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column shiharai_irai_meisai.uf2_cd is 'UF2コード';
comment on column shiharai_irai_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column shiharai_irai_meisai.uf3_cd is 'UF3コード';
comment on column shiharai_irai_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column shiharai_irai_meisai.uf4_cd is 'UF4コード';
comment on column shiharai_irai_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column shiharai_irai_meisai.uf5_cd is 'UF5コード';
comment on column shiharai_irai_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column shiharai_irai_meisai.uf6_cd is 'UF6コード';
comment on column shiharai_irai_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column shiharai_irai_meisai.uf7_cd is 'UF7コード';
comment on column shiharai_irai_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column shiharai_irai_meisai.uf8_cd is 'UF8コード';
comment on column shiharai_irai_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column shiharai_irai_meisai.uf9_cd is 'UF9コード';
comment on column shiharai_irai_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column shiharai_irai_meisai.uf10_cd is 'UF10コード';
comment on column shiharai_irai_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column shiharai_irai_meisai.project_cd is 'プロジェクトコード';
comment on column shiharai_irai_meisai.project_name is 'プロジェクト名';
comment on column shiharai_irai_meisai.segment_cd is 'セグメントコード';
comment on column shiharai_irai_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column shiharai_irai_meisai.tekiyou_cd is '摘要コード';
comment on column shiharai_irai_meisai.touroku_user_id is '登録ユーザーID';
comment on column shiharai_irai_meisai.touroku_time is '登録日時';
comment on column shiharai_irai_meisai.koushin_user_id is '更新ユーザーID';
comment on column shiharai_irai_meisai.koushin_time is '更新日時';
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


-- 軽減税率対応 通勤定期申請
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
comment on table tsuukinteiki is '通勤定期';
comment on column tsuukinteiki.denpyou_id is '伝票ID';
comment on column tsuukinteiki.shiyou_kikan_kbn is '使用期間区分';
comment on column tsuukinteiki.shiyou_kaishibi is '使用開始日';
comment on column tsuukinteiki.shiyou_shuuryoubi is '使用終了日';
comment on column tsuukinteiki.jyousha_kukan is '乗車区間';
comment on column tsuukinteiki.teiki_serialize_data is '定期区間シリアライズデータ';
comment on column tsuukinteiki.shiharaibi is '支払日';
comment on column tsuukinteiki.tekiyou is '摘要';
comment on column tsuukinteiki.zeiritsu is '税率';
comment on column tsuukinteiki.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column tsuukinteiki.kingaku is '金額';
comment on column tsuukinteiki.tenyuuryoku_flg is '手入力フラグ';
comment on column tsuukinteiki.hf1_cd is 'HF1コード';
comment on column tsuukinteiki.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column tsuukinteiki.hf2_cd is 'HF2コード';
comment on column tsuukinteiki.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column tsuukinteiki.hf3_cd is 'HF3コード';
comment on column tsuukinteiki.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column tsuukinteiki.hf4_cd is 'HF4コード';
comment on column tsuukinteiki.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column tsuukinteiki.hf5_cd is 'HF5コード';
comment on column tsuukinteiki.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column tsuukinteiki.hf6_cd is 'HF6コード';
comment on column tsuukinteiki.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column tsuukinteiki.hf7_cd is 'HF7コード';
comment on column tsuukinteiki.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column tsuukinteiki.hf8_cd is 'HF8コード';
comment on column tsuukinteiki.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column tsuukinteiki.hf9_cd is 'HF9コード';
comment on column tsuukinteiki.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column tsuukinteiki.hf10_cd is 'HF10コード';
comment on column tsuukinteiki.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column tsuukinteiki.shiwake_edano is '仕訳枝番号';
comment on column tsuukinteiki.torihiki_name is '取引名';
comment on column tsuukinteiki.kari_futan_bumon_cd is '借方負担部門コード';
comment on column tsuukinteiki.kari_futan_bumon_name is '借方負担部門名';
comment on column tsuukinteiki.torihikisaki_cd is '取引先コード';
comment on column tsuukinteiki.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column tsuukinteiki.kari_kamoku_cd is '借方科目コード';
comment on column tsuukinteiki.kari_kamoku_name is '借方科目名';
comment on column tsuukinteiki.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column tsuukinteiki.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column tsuukinteiki.kari_kazei_kbn is '借方課税区分';
comment on column tsuukinteiki.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column tsuukinteiki.kashi_futan_bumon_name is '貸方負担部門名';
comment on column tsuukinteiki.kashi_kamoku_cd is '貸方科目コード';
comment on column tsuukinteiki.kashi_kamoku_name is '貸方科目名';
comment on column tsuukinteiki.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column tsuukinteiki.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column tsuukinteiki.kashi_kazei_kbn is '貸方課税区分';
comment on column tsuukinteiki.uf1_cd is 'UF1コード';
comment on column tsuukinteiki.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column tsuukinteiki.uf2_cd is 'UF2コード';
comment on column tsuukinteiki.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column tsuukinteiki.uf3_cd is 'UF3コード';
comment on column tsuukinteiki.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column tsuukinteiki.uf4_cd is 'UF4コード';
comment on column tsuukinteiki.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column tsuukinteiki.uf5_cd is 'UF5コード';
comment on column tsuukinteiki.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column tsuukinteiki.uf6_cd is 'UF6コード';
comment on column tsuukinteiki.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column tsuukinteiki.uf7_cd is 'UF7コード';
comment on column tsuukinteiki.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column tsuukinteiki.uf8_cd is 'UF8コード';
comment on column tsuukinteiki.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column tsuukinteiki.uf9_cd is 'UF9コード';
comment on column tsuukinteiki.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column tsuukinteiki.uf10_cd is 'UF10コード';
comment on column tsuukinteiki.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column tsuukinteiki.project_cd is 'プロジェクトコード';
comment on column tsuukinteiki.project_name is 'プロジェクト名';
comment on column tsuukinteiki.segment_cd is 'セグメントコード';
comment on column tsuukinteiki.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column tsuukinteiki.tekiyou_cd is '摘要コード';
comment on column tsuukinteiki.touroku_user_id is '登録ユーザーID';
comment on column tsuukinteiki.touroku_time is '登録日時';
comment on column tsuukinteiki.koushin_user_id is '更新ユーザーID';
comment on column tsuukinteiki.koushin_time is '更新日時';
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
	,'0'	--軽減税率区分
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

-- 0703_海外用日当の外貨対応
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
comment on table kaigai_ryohi_karibarai is '海外旅費仮払';
comment on column kaigai_ryohi_karibarai.denpyou_id is '伝票ID';
comment on column kaigai_ryohi_karibarai.karibarai_on is '仮払申請フラグ';
comment on column kaigai_ryohi_karibarai.dairiflg is '代理フラグ';
comment on column kaigai_ryohi_karibarai.user_id is 'ユーザーID';
comment on column kaigai_ryohi_karibarai.shain_no is '社員番号';
comment on column kaigai_ryohi_karibarai.user_sei is 'ユーザー姓';
comment on column kaigai_ryohi_karibarai.user_mei is 'ユーザー名';
comment on column kaigai_ryohi_karibarai.houmonsaki is '訪問先';
comment on column kaigai_ryohi_karibarai.mokuteki is '目的';
comment on column kaigai_ryohi_karibarai.seisankikan_from is '精算期間開始日';
comment on column kaigai_ryohi_karibarai.seisankikan_from_hour is '精算期間開始時刻（時）';
comment on column kaigai_ryohi_karibarai.seisankikan_from_min is '精算期間開始時刻（分）';
comment on column kaigai_ryohi_karibarai.seisankikan_to is '精算期間終了日';
comment on column kaigai_ryohi_karibarai.seisankikan_to_hour is '精算期間終了時刻（時）';
comment on column kaigai_ryohi_karibarai.seisankikan_to_min is '精算期間終了時刻（分）';
comment on column kaigai_ryohi_karibarai.shiharaibi is '支払日';
comment on column kaigai_ryohi_karibarai.shiharaikiboubi is '支払希望日';
comment on column kaigai_ryohi_karibarai.shiharaihouhou is '支払方法';
comment on column kaigai_ryohi_karibarai.tekiyou is '摘要';
comment on column kaigai_ryohi_karibarai.kingaku is '金額';
comment on column kaigai_ryohi_karibarai.karibarai_kingaku is '仮払金額';
comment on column kaigai_ryohi_karibarai.sashihiki_num is '差引回数';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka is '差引単価';
comment on column kaigai_ryohi_karibarai.sashihiki_num_kaigai is '差引回数（海外）';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka_kaigai is '差引単価（海外）';
comment on column kaigai_ryohi_karibarai.sashihiki_heishu_cd_kaigai is '差引幣種コード（海外）';
comment on column kaigai_ryohi_karibarai.sashihiki_rate_kaigai is '差引レート（海外）';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka_kaigai_gaika is '差引単価（海外）外貨';
comment on column kaigai_ryohi_karibarai.hf1_cd is 'HF1コード';
comment on column kaigai_ryohi_karibarai.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column kaigai_ryohi_karibarai.hf2_cd is 'HF2コード';
comment on column kaigai_ryohi_karibarai.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column kaigai_ryohi_karibarai.hf3_cd is 'HF3コード';
comment on column kaigai_ryohi_karibarai.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column kaigai_ryohi_karibarai.hf4_cd is 'HF4コード';
comment on column kaigai_ryohi_karibarai.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column kaigai_ryohi_karibarai.hf5_cd is 'HF5コード';
comment on column kaigai_ryohi_karibarai.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column kaigai_ryohi_karibarai.hf6_cd is 'HF6コード';
comment on column kaigai_ryohi_karibarai.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column kaigai_ryohi_karibarai.hf7_cd is 'HF7コード';
comment on column kaigai_ryohi_karibarai.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column kaigai_ryohi_karibarai.hf8_cd is 'HF8コード';
comment on column kaigai_ryohi_karibarai.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column kaigai_ryohi_karibarai.hf9_cd is 'HF9コード';
comment on column kaigai_ryohi_karibarai.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column kaigai_ryohi_karibarai.hf10_cd is 'HF10コード';
comment on column kaigai_ryohi_karibarai.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column kaigai_ryohi_karibarai.hosoku is '補足';
comment on column kaigai_ryohi_karibarai.shiwake_edano is '仕訳枝番号';
comment on column kaigai_ryohi_karibarai.torihiki_name is '取引名';
comment on column kaigai_ryohi_karibarai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column kaigai_ryohi_karibarai.kari_futan_bumon_name is '借方負担部門名';
comment on column kaigai_ryohi_karibarai.torihikisaki_cd is '取引先コード';
comment on column kaigai_ryohi_karibarai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column kaigai_ryohi_karibarai.kari_kamoku_cd is '借方科目コード';
comment on column kaigai_ryohi_karibarai.kari_kamoku_name is '借方科目名';
comment on column kaigai_ryohi_karibarai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column kaigai_ryohi_karibarai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column kaigai_ryohi_karibarai.kari_kazei_kbn is '借方課税区分';
comment on column kaigai_ryohi_karibarai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column kaigai_ryohi_karibarai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_cd is '貸方科目コード';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_name is '貸方科目名';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column kaigai_ryohi_karibarai.kashi_kazei_kbn is '貸方課税区分';
comment on column kaigai_ryohi_karibarai.uf1_cd is 'UF1コード';
comment on column kaigai_ryohi_karibarai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column kaigai_ryohi_karibarai.uf2_cd is 'UF2コード';
comment on column kaigai_ryohi_karibarai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column kaigai_ryohi_karibarai.uf3_cd is 'UF3コード';
comment on column kaigai_ryohi_karibarai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column kaigai_ryohi_karibarai.uf4_cd is 'UF4コード';
comment on column kaigai_ryohi_karibarai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column kaigai_ryohi_karibarai.uf5_cd is 'UF5コード';
comment on column kaigai_ryohi_karibarai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column kaigai_ryohi_karibarai.uf6_cd is 'UF6コード';
comment on column kaigai_ryohi_karibarai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column kaigai_ryohi_karibarai.uf7_cd is 'UF7コード';
comment on column kaigai_ryohi_karibarai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column kaigai_ryohi_karibarai.uf8_cd is 'UF8コード';
comment on column kaigai_ryohi_karibarai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column kaigai_ryohi_karibarai.uf9_cd is 'UF9コード';
comment on column kaigai_ryohi_karibarai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column kaigai_ryohi_karibarai.uf10_cd is 'UF10コード';
comment on column kaigai_ryohi_karibarai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column kaigai_ryohi_karibarai.project_cd is 'プロジェクトコード';
comment on column kaigai_ryohi_karibarai.project_name is 'プロジェクト名';
comment on column kaigai_ryohi_karibarai.segment_cd is 'セグメントコード';
comment on column kaigai_ryohi_karibarai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column kaigai_ryohi_karibarai.tekiyou_cd is '摘要コード';
comment on column kaigai_ryohi_karibarai.seisan_kanryoubi is '精算完了日';
comment on column kaigai_ryohi_karibarai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohi_karibarai.touroku_time is '登録日時';
comment on column kaigai_ryohi_karibarai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohi_karibarai.koushin_time is '更新日時';
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
	,''		--差引幣種コード（海外）
	,null	--差引レート（海外）
	,null	--差引単価（海外）外貨
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


-- 軽減税率対応 海外旅費精算、0703_海外用日当の外貨対応
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
comment on table kaigai_ryohiseisan is '海外旅費精算';
comment on column kaigai_ryohiseisan.denpyou_id is '伝票ID';
comment on column kaigai_ryohiseisan.karibarai_denpyou_id is '仮払伝票ID';
comment on column kaigai_ryohiseisan.karibarai_on is '仮払申請フラグ';
comment on column kaigai_ryohiseisan.karibarai_mishiyou_flg is '仮払金未使用フラグ';
comment on column kaigai_ryohiseisan.shucchou_chuushi_flg is '出張中止フラグ';
comment on column kaigai_ryohiseisan.dairiflg is '代理フラグ';
comment on column kaigai_ryohiseisan.user_id is 'ユーザーID';
comment on column kaigai_ryohiseisan.shain_no is '社員番号';
comment on column kaigai_ryohiseisan.user_sei is 'ユーザー姓';
comment on column kaigai_ryohiseisan.user_mei is 'ユーザー名';
comment on column kaigai_ryohiseisan.houmonsaki is '訪問先';
comment on column kaigai_ryohiseisan.mokuteki is '目的';
comment on column kaigai_ryohiseisan.seisankikan_from is '精算期間開始日';
comment on column kaigai_ryohiseisan.seisankikan_from_hour is '精算期間開始時刻（時）';
comment on column kaigai_ryohiseisan.seisankikan_from_min is '精算期間開始時刻（分）';
comment on column kaigai_ryohiseisan.seisankikan_to is '精算期間終了日';
comment on column kaigai_ryohiseisan.seisankikan_to_hour is '精算期間終了時刻（時）';
comment on column kaigai_ryohiseisan.seisankikan_to_min is '精算期間終了時刻（分）';
comment on column kaigai_ryohiseisan.keijoubi is '計上日';
comment on column kaigai_ryohiseisan.shiharaibi is '支払日';
comment on column kaigai_ryohiseisan.shiharaikiboubi is '支払希望日';
comment on column kaigai_ryohiseisan.shiharaihouhou is '支払方法';
comment on column kaigai_ryohiseisan.tekiyou is '摘要';
comment on column kaigai_ryohiseisan.kaigai_tekiyou is '摘要（海外）';
comment on column kaigai_ryohiseisan.zeiritsu is '税率';
comment on column kaigai_ryohiseisan.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column kaigai_ryohiseisan.goukei_kingaku is '合計金額';
comment on column kaigai_ryohiseisan.houjin_card_riyou_kingaku is '内法人カード利用合計';
comment on column kaigai_ryohiseisan.kaisha_tehai_kingaku is '会社手配合計';
comment on column kaigai_ryohiseisan.sashihiki_shikyuu_kingaku is '差引支給金額';
comment on column kaigai_ryohiseisan.sashihiki_num is '差引回数';
comment on column kaigai_ryohiseisan.sashihiki_tanka is '差引単価';
comment on column kaigai_ryohiseisan.sashihiki_num_kaigai is '差引回数（海外）';
comment on column kaigai_ryohiseisan.sashihiki_tanka_kaigai is '差引単価（海外）';
comment on column kaigai_ryohiseisan.sashihiki_heishu_cd_kaigai is '差引幣種コード（海外）';
comment on column kaigai_ryohiseisan.sashihiki_rate_kaigai is '差引レート（海外）';
comment on column kaigai_ryohiseisan.sashihiki_tanka_kaigai_gaika is '差引単価（海外）外貨';
comment on column kaigai_ryohiseisan.hf1_cd is 'HF1コード';
comment on column kaigai_ryohiseisan.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column kaigai_ryohiseisan.hf2_cd is 'HF2コード';
comment on column kaigai_ryohiseisan.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column kaigai_ryohiseisan.hf3_cd is 'HF3コード';
comment on column kaigai_ryohiseisan.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column kaigai_ryohiseisan.hf4_cd is 'HF4コード';
comment on column kaigai_ryohiseisan.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column kaigai_ryohiseisan.hf5_cd is 'HF5コード';
comment on column kaigai_ryohiseisan.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column kaigai_ryohiseisan.hf6_cd is 'HF6コード';
comment on column kaigai_ryohiseisan.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column kaigai_ryohiseisan.hf7_cd is 'HF7コード';
comment on column kaigai_ryohiseisan.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column kaigai_ryohiseisan.hf8_cd is 'HF8コード';
comment on column kaigai_ryohiseisan.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column kaigai_ryohiseisan.hf9_cd is 'HF9コード';
comment on column kaigai_ryohiseisan.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column kaigai_ryohiseisan.hf10_cd is 'HF10コード';
comment on column kaigai_ryohiseisan.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column kaigai_ryohiseisan.hosoku is '補足';
comment on column kaigai_ryohiseisan.shiwake_edano is '仕訳枝番号';
comment on column kaigai_ryohiseisan.torihiki_name is '取引名';
comment on column kaigai_ryohiseisan.kari_futan_bumon_cd is '借方負担部門コード';
comment on column kaigai_ryohiseisan.kari_futan_bumon_name is '借方負担部門名';
comment on column kaigai_ryohiseisan.torihikisaki_cd is '取引先コード';
comment on column kaigai_ryohiseisan.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column kaigai_ryohiseisan.kari_kamoku_cd is '借方科目コード';
comment on column kaigai_ryohiseisan.kari_kamoku_name is '借方科目名';
comment on column kaigai_ryohiseisan.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column kaigai_ryohiseisan.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column kaigai_ryohiseisan.kari_kazei_kbn is '借方課税区分';
comment on column kaigai_ryohiseisan.ryohi_kazei_flg is '旅費課税フラグ';
comment on column kaigai_ryohiseisan.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column kaigai_ryohiseisan.kashi_futan_bumon_name is '貸方負担部門名';
comment on column kaigai_ryohiseisan.kashi_kamoku_cd is '貸方科目コード';
comment on column kaigai_ryohiseisan.kashi_kamoku_name is '貸方科目名';
comment on column kaigai_ryohiseisan.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column kaigai_ryohiseisan.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column kaigai_ryohiseisan.kashi_kazei_kbn is '貸方課税区分';
comment on column kaigai_ryohiseisan.uf1_cd is 'UF1コード';
comment on column kaigai_ryohiseisan.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column kaigai_ryohiseisan.uf2_cd is 'UF2コード';
comment on column kaigai_ryohiseisan.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column kaigai_ryohiseisan.uf3_cd is 'UF3コード';
comment on column kaigai_ryohiseisan.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column kaigai_ryohiseisan.uf4_cd is 'UF4コード';
comment on column kaigai_ryohiseisan.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column kaigai_ryohiseisan.uf5_cd is 'UF5コード';
comment on column kaigai_ryohiseisan.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column kaigai_ryohiseisan.uf6_cd is 'UF6コード';
comment on column kaigai_ryohiseisan.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column kaigai_ryohiseisan.uf7_cd is 'UF7コード';
comment on column kaigai_ryohiseisan.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column kaigai_ryohiseisan.uf8_cd is 'UF8コード';
comment on column kaigai_ryohiseisan.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column kaigai_ryohiseisan.uf9_cd is 'UF9コード';
comment on column kaigai_ryohiseisan.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column kaigai_ryohiseisan.uf10_cd is 'UF10コード';
comment on column kaigai_ryohiseisan.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column kaigai_ryohiseisan.project_cd is 'プロジェクトコード';
comment on column kaigai_ryohiseisan.project_name is 'プロジェクト名';
comment on column kaigai_ryohiseisan.segment_cd is 'セグメントコード';
comment on column kaigai_ryohiseisan.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column kaigai_ryohiseisan.tekiyou_cd is '摘要コード';
comment on column kaigai_ryohiseisan.kaigai_shiwake_edano is '海外仕訳枝番号';
comment on column kaigai_ryohiseisan.kaigai_torihiki_name is '海外取引名';
comment on column kaigai_ryohiseisan.kaigai_kari_futan_bumon_cd is '海外借方負担部門コード';
comment on column kaigai_ryohiseisan.kaigai_kari_futan_bumon_name is '海外借方負担部門名';
comment on column kaigai_ryohiseisan.kaigai_torihikisaki_cd is '海外取引先コード';
comment on column kaigai_ryohiseisan.kaigai_torihikisaki_name_ryakushiki is '海外取引先名（略式）';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_cd is '海外借方科目コード';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_name is '海外借方科目名';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_edaban_cd is '海外借方科目枝番コード';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_edaban_name is '海外借方科目枝番名';
comment on column kaigai_ryohiseisan.kaigai_kari_kazei_kbn is '海外借方課税区分';
comment on column kaigai_ryohiseisan.kaigai_kazei_flg is '海外課税フラグ';
comment on column kaigai_ryohiseisan.kaigai_kashi_futan_bumon_cd is '海外貸方負担部門コード';
comment on column kaigai_ryohiseisan.kaigai_kashi_futan_bumon_name is '海外貸方負担部門名';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_cd is '海外貸方科目コード';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_name is '海外貸方科目名';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_edaban_cd is '海外貸方科目枝番コード';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_edaban_name is '海外貸方科目枝番名';
comment on column kaigai_ryohiseisan.kaigai_kashi_kazei_kbn is '海外貸方課税区分';
comment on column kaigai_ryohiseisan.kaigai_uf1_cd is '海外UF1コード';
comment on column kaigai_ryohiseisan.kaigai_uf1_name_ryakushiki is '海外UF1名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf2_cd is '海外UF2コード';
comment on column kaigai_ryohiseisan.kaigai_uf2_name_ryakushiki is '海外UF2名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf3_cd is '海外UF3コード';
comment on column kaigai_ryohiseisan.kaigai_uf3_name_ryakushiki is '海外UF3名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf4_cd is '海外UF4コード';
comment on column kaigai_ryohiseisan.kaigai_uf4_name_ryakushiki is '海外UF4名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf5_cd is '海外UF5コード';
comment on column kaigai_ryohiseisan.kaigai_uf5_name_ryakushiki is '海外UF5名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf6_cd is '海外UF6コード';
comment on column kaigai_ryohiseisan.kaigai_uf6_name_ryakushiki is '海外UF6名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf7_cd is '海外UF7コード';
comment on column kaigai_ryohiseisan.kaigai_uf7_name_ryakushiki is '海外UF7名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf8_cd is '海外UF8コード';
comment on column kaigai_ryohiseisan.kaigai_uf8_name_ryakushiki is '海外UF8名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf9_cd is '海外UF9コード';
comment on column kaigai_ryohiseisan.kaigai_uf9_name_ryakushiki is '海外UF9名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf10_cd is '海外UF10コード';
comment on column kaigai_ryohiseisan.kaigai_uf10_name_ryakushiki is '海外UF10名（略式）';
comment on column kaigai_ryohiseisan.kaigai_project_cd is '海外プロジェクトコード';
comment on column kaigai_ryohiseisan.kaigai_project_name is '海外プロジェクト名';
comment on column kaigai_ryohiseisan.kaigai_segment_cd is '海外セグメントコード';
comment on column kaigai_ryohiseisan.kaigai_segment_name_ryakushiki is '海外セグメント名（略式）';
comment on column kaigai_ryohiseisan.kaigai_tekiyou_cd is '海外摘要コード';
comment on column kaigai_ryohiseisan.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohiseisan.touroku_time is '登録日時';
comment on column kaigai_ryohiseisan.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohiseisan.koushin_time is '更新日時';
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
	,'0'	--軽減税率区分
	,goukei_kingaku
	,houjin_card_riyou_kingaku
	,kaisha_tehai_kingaku
	,sashihiki_shikyuu_kingaku
	,sashihiki_num
	,sashihiki_tanka
	,sashihiki_num_kaigai
	,sashihiki_tanka_kaigai
	,''		--差引幣種コード（海外）
	,null	--差引レート（海外）
	,null	--差引単価（海外）外貨
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


-- 軽減税率対応 交通費精算
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
comment on table koutsuuhiseisan is '交通費精算';
comment on column koutsuuhiseisan.denpyou_id is '伝票ID';
comment on column koutsuuhiseisan.mokuteki is '目的';
comment on column koutsuuhiseisan.seisankikan_from is '精算期間開始日';
comment on column koutsuuhiseisan.seisankikan_from_hour is '精算期間開始時刻（時）';
comment on column koutsuuhiseisan.seisankikan_from_min is '精算期間開始時刻（分）';
comment on column koutsuuhiseisan.seisankikan_to is '精算期間終了日';
comment on column koutsuuhiseisan.seisankikan_to_hour is '精算期間終了時刻（時）';
comment on column koutsuuhiseisan.seisankikan_to_min is '精算期間終了時刻（分）';
comment on column koutsuuhiseisan.keijoubi is '計上日';
comment on column koutsuuhiseisan.shiharaibi is '支払日';
comment on column koutsuuhiseisan.shiharaikiboubi is '支払希望日';
comment on column koutsuuhiseisan.shiharaihouhou is '支払方法';
comment on column koutsuuhiseisan.tekiyou is '摘要';
comment on column koutsuuhiseisan.zeiritsu is '税率';
comment on column koutsuuhiseisan.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column koutsuuhiseisan.goukei_kingaku is '合計金額';
comment on column koutsuuhiseisan.houjin_card_riyou_kingaku is '内法人カード利用合計';
comment on column koutsuuhiseisan.kaisha_tehai_kingaku is '会社手配合計';
comment on column koutsuuhiseisan.sashihiki_shikyuu_kingaku is '差引支給金額';
comment on column koutsuuhiseisan.hf1_cd is 'HF1コード';
comment on column koutsuuhiseisan.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column koutsuuhiseisan.hf2_cd is 'HF2コード';
comment on column koutsuuhiseisan.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column koutsuuhiseisan.hf3_cd is 'HF3コード';
comment on column koutsuuhiseisan.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column koutsuuhiseisan.hf4_cd is 'HF4コード';
comment on column koutsuuhiseisan.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column koutsuuhiseisan.hf5_cd is 'HF5コード';
comment on column koutsuuhiseisan.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column koutsuuhiseisan.hf6_cd is 'HF6コード';
comment on column koutsuuhiseisan.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column koutsuuhiseisan.hf7_cd is 'HF7コード';
comment on column koutsuuhiseisan.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column koutsuuhiseisan.hf8_cd is 'HF8コード';
comment on column koutsuuhiseisan.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column koutsuuhiseisan.hf9_cd is 'HF9コード';
comment on column koutsuuhiseisan.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column koutsuuhiseisan.hf10_cd is 'HF10コード';
comment on column koutsuuhiseisan.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column koutsuuhiseisan.hosoku is '補足';
comment on column koutsuuhiseisan.shiwake_edano is '仕訳枝番号';
comment on column koutsuuhiseisan.torihiki_name is '取引名';
comment on column koutsuuhiseisan.kari_futan_bumon_cd is '借方負担部門コード';
comment on column koutsuuhiseisan.kari_futan_bumon_name is '借方負担部門名';
comment on column koutsuuhiseisan.torihikisaki_cd is '取引先コード';
comment on column koutsuuhiseisan.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column koutsuuhiseisan.kari_kamoku_cd is '借方科目コード';
comment on column koutsuuhiseisan.kari_kamoku_name is '借方科目名';
comment on column koutsuuhiseisan.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column koutsuuhiseisan.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column koutsuuhiseisan.kari_kazei_kbn is '借方課税区分';
comment on column koutsuuhiseisan.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column koutsuuhiseisan.kashi_futan_bumon_name is '貸方負担部門名';
comment on column koutsuuhiseisan.kashi_kamoku_cd is '貸方科目コード';
comment on column koutsuuhiseisan.kashi_kamoku_name is '貸方科目名';
comment on column koutsuuhiseisan.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column koutsuuhiseisan.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column koutsuuhiseisan.kashi_kazei_kbn is '貸方課税区分';
comment on column koutsuuhiseisan.uf1_cd is 'UF1コード';
comment on column koutsuuhiseisan.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column koutsuuhiseisan.uf2_cd is 'UF2コード';
comment on column koutsuuhiseisan.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column koutsuuhiseisan.uf3_cd is 'UF3コード';
comment on column koutsuuhiseisan.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column koutsuuhiseisan.uf4_cd is 'UF4コード';
comment on column koutsuuhiseisan.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column koutsuuhiseisan.uf5_cd is 'UF5コード';
comment on column koutsuuhiseisan.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column koutsuuhiseisan.uf6_cd is 'UF6コード';
comment on column koutsuuhiseisan.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column koutsuuhiseisan.uf7_cd is 'UF7コード';
comment on column koutsuuhiseisan.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column koutsuuhiseisan.uf8_cd is 'UF8コード';
comment on column koutsuuhiseisan.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column koutsuuhiseisan.uf9_cd is 'UF9コード';
comment on column koutsuuhiseisan.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column koutsuuhiseisan.uf10_cd is 'UF10コード';
comment on column koutsuuhiseisan.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column koutsuuhiseisan.project_cd is 'プロジェクトコード';
comment on column koutsuuhiseisan.project_name is 'プロジェクト名';
comment on column koutsuuhiseisan.segment_cd is 'セグメントコード';
comment on column koutsuuhiseisan.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column koutsuuhiseisan.tekiyou_cd is '摘要コード';
comment on column koutsuuhiseisan.touroku_user_id is '登録ユーザーID';
comment on column koutsuuhiseisan.touroku_time is '登録日時';
comment on column koutsuuhiseisan.koushin_user_id is '更新ユーザーID';
comment on column koutsuuhiseisan.koushin_time is '更新日時';
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
	,'0'	--軽減税率区分
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


-- 軽減税率対応 海外旅費仮払経費明細
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
comment on table kaigai_ryohi_karibarai_keihi_meisai is '海外旅費仮払経費明細';
comment on column kaigai_ryohi_karibarai_keihi_meisai.denpyou_id is '伝票ID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kaigai_flg is '海外フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiyoubi is '使用日';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihiki_name is '取引名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.tekiyou is '摘要';
comment on column kaigai_ryohi_karibarai_keihi_meisai.zeiritsu is '税率';
comment on column kaigai_ryohi_karibarai_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kazei_flg is '課税フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.hontai_kingaku is '本体金額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shouhizeigaku is '消費税額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column kaigai_ryohi_karibarai_keihi_meisai.heishu_cd is '幣種コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.rate is 'レート';
comment on column kaigai_ryohi_karibarai_keihi_meisai.gaika is '外貨金額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.currency_unit is '通貨単位';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf1_cd is 'UF1コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf2_cd is 'UF2コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf3_cd is 'UF3コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf4_cd is 'UF4コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf5_cd is 'UF5コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf6_cd is 'UF6コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf7_cd is 'UF7コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf8_cd is 'UF8コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf9_cd is 'UF9コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf10_cd is 'UF10コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.project_name is 'プロジェクト名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.segment_cd is 'セグメントコード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.touroku_time is '登録日時';
comment on column kaigai_ryohi_karibarai_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.koushin_time is '更新日時';
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
	,'0'	--軽減税率区分
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


-- 軽減税率対応 海外旅費精算経費明細
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
comment on table kaigai_ryohiseisan_keihi_meisai is '海外旅費精算経費明細';
comment on column kaigai_ryohiseisan_keihi_meisai.denpyou_id is '伝票ID';
comment on column kaigai_ryohiseisan_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column kaigai_ryohiseisan_keihi_meisai.kaigai_flg is '海外フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column kaigai_ryohiseisan_keihi_meisai.shiyoubi is '使用日';
comment on column kaigai_ryohiseisan_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.torihiki_name is '取引名';
comment on column kaigai_ryohiseisan_keihi_meisai.tekiyou is '摘要';
comment on column kaigai_ryohiseisan_keihi_meisai.zeiritsu is '税率';
comment on column kaigai_ryohiseisan_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column kaigai_ryohiseisan_keihi_meisai.kazei_flg is '課税フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column kaigai_ryohiseisan_keihi_meisai.hontai_kingaku is '本体金額';
comment on column kaigai_ryohiseisan_keihi_meisai.shouhizeigaku is '消費税額';
comment on column kaigai_ryohiseisan_keihi_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column kaigai_ryohiseisan_keihi_meisai.heishu_cd is '幣種コード';
comment on column kaigai_ryohiseisan_keihi_meisai.rate is 'レート';
comment on column kaigai_ryohiseisan_keihi_meisai.gaika is '外貨金額';
comment on column kaigai_ryohiseisan_keihi_meisai.currency_unit is '通貨単位';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column kaigai_ryohiseisan_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column kaigai_ryohiseisan_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column kaigai_ryohiseisan_keihi_meisai.uf1_cd is 'UF1コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf2_cd is 'UF2コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf3_cd is 'UF3コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf4_cd is 'UF4コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf5_cd is 'UF5コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf6_cd is 'UF6コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf7_cd is 'UF7コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf8_cd is 'UF8コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf9_cd is 'UF9コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf10_cd is 'UF10コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column kaigai_ryohiseisan_keihi_meisai.project_name is 'プロジェクト名';
comment on column kaigai_ryohiseisan_keihi_meisai.segment_cd is 'セグメントコード';
comment on column kaigai_ryohiseisan_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column kaigai_ryohiseisan_keihi_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column kaigai_ryohiseisan_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohiseisan_keihi_meisai.touroku_time is '登録日時';
comment on column kaigai_ryohiseisan_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohiseisan_keihi_meisai.koushin_time is '更新日時';
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
	,'0'	--軽減税率区分
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


-- 軽減税率対応 旅費精算経費明細
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
comment on table ryohiseisan_keihi_meisai is '旅費精算経費明細';
comment on column ryohiseisan_keihi_meisai.denpyou_id is '伝票ID';
comment on column ryohiseisan_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column ryohiseisan_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column ryohiseisan_keihi_meisai.shiyoubi is '使用日';
comment on column ryohiseisan_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column ryohiseisan_keihi_meisai.torihiki_name is '取引名';
comment on column ryohiseisan_keihi_meisai.tekiyou is '摘要';
comment on column ryohiseisan_keihi_meisai.zeiritsu is '税率';
comment on column ryohiseisan_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column ryohiseisan_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column ryohiseisan_keihi_meisai.hontai_kingaku is '本体金額';
comment on column ryohiseisan_keihi_meisai.shouhizeigaku is '消費税額';
comment on column ryohiseisan_keihi_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column ryohiseisan_keihi_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column ryohiseisan_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column ryohiseisan_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column ryohiseisan_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column ryohiseisan_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column ryohiseisan_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column ryohiseisan_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column ryohiseisan_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column ryohiseisan_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column ryohiseisan_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column ryohiseisan_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column ryohiseisan_keihi_meisai.uf1_cd is 'UF1コード';
comment on column ryohiseisan_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column ryohiseisan_keihi_meisai.uf2_cd is 'UF2コード';
comment on column ryohiseisan_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column ryohiseisan_keihi_meisai.uf3_cd is 'UF3コード';
comment on column ryohiseisan_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column ryohiseisan_keihi_meisai.uf4_cd is 'UF4コード';
comment on column ryohiseisan_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column ryohiseisan_keihi_meisai.uf5_cd is 'UF5コード';
comment on column ryohiseisan_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column ryohiseisan_keihi_meisai.uf6_cd is 'UF6コード';
comment on column ryohiseisan_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column ryohiseisan_keihi_meisai.uf7_cd is 'UF7コード';
comment on column ryohiseisan_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column ryohiseisan_keihi_meisai.uf8_cd is 'UF8コード';
comment on column ryohiseisan_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column ryohiseisan_keihi_meisai.uf9_cd is 'UF9コード';
comment on column ryohiseisan_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column ryohiseisan_keihi_meisai.uf10_cd is 'UF10コード';
comment on column ryohiseisan_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column ryohiseisan_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column ryohiseisan_keihi_meisai.project_name is 'プロジェクト名';
comment on column ryohiseisan_keihi_meisai.segment_cd is 'セグメントコード';
comment on column ryohiseisan_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column ryohiseisan_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column ryohiseisan_keihi_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column ryohiseisan_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column ryohiseisan_keihi_meisai.touroku_time is '登録日時';
comment on column ryohiseisan_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column ryohiseisan_keihi_meisai.koushin_time is '更新日時';
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
	,'0'	--軽減税率区分
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


-- 軽減税率対応 旅費精算
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
comment on table ryohiseisan is '旅費精算';
comment on column ryohiseisan.denpyou_id is '伝票ID';
comment on column ryohiseisan.karibarai_denpyou_id is '仮払伝票ID';
comment on column ryohiseisan.karibarai_on is '仮払申請フラグ';
comment on column ryohiseisan.karibarai_mishiyou_flg is '仮払金未使用フラグ';
comment on column ryohiseisan.shucchou_chuushi_flg is '出張中止フラグ';
comment on column ryohiseisan.dairiflg is '代理フラグ';
comment on column ryohiseisan.user_id is 'ユーザーID';
comment on column ryohiseisan.shain_no is '社員番号';
comment on column ryohiseisan.user_sei is 'ユーザー姓';
comment on column ryohiseisan.user_mei is 'ユーザー名';
comment on column ryohiseisan.houmonsaki is '訪問先';
comment on column ryohiseisan.mokuteki is '目的';
comment on column ryohiseisan.seisankikan_from is '精算期間開始日';
comment on column ryohiseisan.seisankikan_from_hour is '精算期間開始時刻（時）';
comment on column ryohiseisan.seisankikan_from_min is '精算期間開始時刻（分）';
comment on column ryohiseisan.seisankikan_to is '精算期間終了日';
comment on column ryohiseisan.seisankikan_to_hour is '精算期間終了時刻（時）';
comment on column ryohiseisan.seisankikan_to_min is '精算期間終了時刻（分）';
comment on column ryohiseisan.keijoubi is '計上日';
comment on column ryohiseisan.shiharaibi is '支払日';
comment on column ryohiseisan.shiharaikiboubi is '支払希望日';
comment on column ryohiseisan.shiharaihouhou is '支払方法';
comment on column ryohiseisan.tekiyou is '摘要';
comment on column ryohiseisan.zeiritsu is '税率';
comment on column ryohiseisan.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column ryohiseisan.goukei_kingaku is '合計金額';
comment on column ryohiseisan.houjin_card_riyou_kingaku is '内法人カード利用合計';
comment on column ryohiseisan.kaisha_tehai_kingaku is '会社手配合計';
comment on column ryohiseisan.sashihiki_shikyuu_kingaku is '差引支給金額';
comment on column ryohiseisan.sashihiki_num is '差引回数';
comment on column ryohiseisan.sashihiki_tanka is '差引単価';
comment on column ryohiseisan.hf1_cd is 'HF1コード';
comment on column ryohiseisan.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column ryohiseisan.hf2_cd is 'HF2コード';
comment on column ryohiseisan.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column ryohiseisan.hf3_cd is 'HF3コード';
comment on column ryohiseisan.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column ryohiseisan.hf4_cd is 'HF4コード';
comment on column ryohiseisan.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column ryohiseisan.hf5_cd is 'HF5コード';
comment on column ryohiseisan.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column ryohiseisan.hf6_cd is 'HF6コード';
comment on column ryohiseisan.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column ryohiseisan.hf7_cd is 'HF7コード';
comment on column ryohiseisan.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column ryohiseisan.hf8_cd is 'HF8コード';
comment on column ryohiseisan.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column ryohiseisan.hf9_cd is 'HF9コード';
comment on column ryohiseisan.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column ryohiseisan.hf10_cd is 'HF10コード';
comment on column ryohiseisan.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column ryohiseisan.hosoku is '補足';
comment on column ryohiseisan.shiwake_edano is '仕訳枝番号';
comment on column ryohiseisan.torihiki_name is '取引名';
comment on column ryohiseisan.kari_futan_bumon_cd is '借方負担部門コード';
comment on column ryohiseisan.kari_futan_bumon_name is '借方負担部門名';
comment on column ryohiseisan.torihikisaki_cd is '取引先コード';
comment on column ryohiseisan.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column ryohiseisan.kari_kamoku_cd is '借方科目コード';
comment on column ryohiseisan.kari_kamoku_name is '借方科目名';
comment on column ryohiseisan.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column ryohiseisan.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column ryohiseisan.kari_kazei_kbn is '借方課税区分';
comment on column ryohiseisan.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column ryohiseisan.kashi_futan_bumon_name is '貸方負担部門名';
comment on column ryohiseisan.kashi_kamoku_cd is '貸方科目コード';
comment on column ryohiseisan.kashi_kamoku_name is '貸方科目名';
comment on column ryohiseisan.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column ryohiseisan.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column ryohiseisan.kashi_kazei_kbn is '貸方課税区分';
comment on column ryohiseisan.uf1_cd is 'UF1コード';
comment on column ryohiseisan.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column ryohiseisan.uf2_cd is 'UF2コード';
comment on column ryohiseisan.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column ryohiseisan.uf3_cd is 'UF3コード';
comment on column ryohiseisan.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column ryohiseisan.uf4_cd is 'UF4コード';
comment on column ryohiseisan.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column ryohiseisan.uf5_cd is 'UF5コード';
comment on column ryohiseisan.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column ryohiseisan.uf6_cd is 'UF6コード';
comment on column ryohiseisan.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column ryohiseisan.uf7_cd is 'UF7コード';
comment on column ryohiseisan.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column ryohiseisan.uf8_cd is 'UF8コード';
comment on column ryohiseisan.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column ryohiseisan.uf9_cd is 'UF9コード';
comment on column ryohiseisan.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column ryohiseisan.uf10_cd is 'UF10コード';
comment on column ryohiseisan.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column ryohiseisan.project_cd is 'プロジェクトコード';
comment on column ryohiseisan.project_name is 'プロジェクト名';
comment on column ryohiseisan.segment_cd is 'セグメントコード';
comment on column ryohiseisan.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column ryohiseisan.tekiyou_cd is '摘要コード';
comment on column ryohiseisan.touroku_user_id is '登録ユーザーID';
comment on column ryohiseisan.touroku_time is '登録日時';
comment on column ryohiseisan.koushin_user_id is '更新ユーザーID';
comment on column ryohiseisan.koushin_time is '更新日時';
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
	,'0'	--軽減税率区分
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


-- 軽減税率対応 旅費仮払経費明細
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
comment on table ryohi_karibarai_keihi_meisai is '旅費仮払経費明細';
comment on column ryohi_karibarai_keihi_meisai.denpyou_id is '伝票ID';
comment on column ryohi_karibarai_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column ryohi_karibarai_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column ryohi_karibarai_keihi_meisai.shiyoubi is '使用日';
comment on column ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column ryohi_karibarai_keihi_meisai.torihiki_name is '取引名';
comment on column ryohi_karibarai_keihi_meisai.tekiyou is '摘要';
comment on column ryohi_karibarai_keihi_meisai.zeiritsu is '税率';
comment on column ryohi_karibarai_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column ryohi_karibarai_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column ryohi_karibarai_keihi_meisai.hontai_kingaku is '本体金額';
comment on column ryohi_karibarai_keihi_meisai.shouhizeigaku is '消費税額';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column ryohi_karibarai_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column ryohi_karibarai_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column ryohi_karibarai_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column ryohi_karibarai_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column ryohi_karibarai_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column ryohi_karibarai_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column ryohi_karibarai_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column ryohi_karibarai_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column ryohi_karibarai_keihi_meisai.uf1_cd is 'UF1コード';
comment on column ryohi_karibarai_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf2_cd is 'UF2コード';
comment on column ryohi_karibarai_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf3_cd is 'UF3コード';
comment on column ryohi_karibarai_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf4_cd is 'UF4コード';
comment on column ryohi_karibarai_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf5_cd is 'UF5コード';
comment on column ryohi_karibarai_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf6_cd is 'UF6コード';
comment on column ryohi_karibarai_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf7_cd is 'UF7コード';
comment on column ryohi_karibarai_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf8_cd is 'UF8コード';
comment on column ryohi_karibarai_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf9_cd is 'UF9コード';
comment on column ryohi_karibarai_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf10_cd is 'UF10コード';
comment on column ryohi_karibarai_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column ryohi_karibarai_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column ryohi_karibarai_keihi_meisai.project_name is 'プロジェクト名';
comment on column ryohi_karibarai_keihi_meisai.segment_cd is 'セグメントコード';
comment on column ryohi_karibarai_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column ryohi_karibarai_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column ryohi_karibarai_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column ryohi_karibarai_keihi_meisai.touroku_time is '登録日時';
comment on column ryohi_karibarai_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column ryohi_karibarai_keihi_meisai.koushin_time is '更新日時';
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
	,'0'	--軽減税率区分
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


--- 軽減税率対応 請求書払い明細
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
comment on table seikyuushobarai_meisai is '請求書払い明細';
comment on column seikyuushobarai_meisai.denpyou_id is '伝票ID';
comment on column seikyuushobarai_meisai.denpyou_edano is '伝票枝番号';
comment on column seikyuushobarai_meisai.shiwake_edano is '仕訳枝番号';
comment on column seikyuushobarai_meisai.torihiki_name is '取引名';
comment on column seikyuushobarai_meisai.tekiyou is '摘要';
comment on column seikyuushobarai_meisai.zeiritsu is '税率';
comment on column seikyuushobarai_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column seikyuushobarai_meisai.shiharai_kingaku is '支払金額';
comment on column seikyuushobarai_meisai.hontai_kingaku is '本体金額';
comment on column seikyuushobarai_meisai.shouhizeigaku is '消費税額';
comment on column seikyuushobarai_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column seikyuushobarai_meisai.kousaihi_shousai is '交際費詳細';
comment on column seikyuushobarai_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column seikyuushobarai_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column seikyuushobarai_meisai.torihikisaki_cd is '取引先コード';
comment on column seikyuushobarai_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column seikyuushobarai_meisai.furikomisaki_jouhou is '振込先情報';
comment on column seikyuushobarai_meisai.kari_kamoku_cd is '借方科目コード';
comment on column seikyuushobarai_meisai.kari_kamoku_name is '借方科目名';
comment on column seikyuushobarai_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column seikyuushobarai_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column seikyuushobarai_meisai.kari_kazei_kbn is '借方課税区分';
comment on column seikyuushobarai_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column seikyuushobarai_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column seikyuushobarai_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column seikyuushobarai_meisai.kashi_kamoku_name is '貸方科目名';
comment on column seikyuushobarai_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column seikyuushobarai_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column seikyuushobarai_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column seikyuushobarai_meisai.uf1_cd is 'UF1コード';
comment on column seikyuushobarai_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column seikyuushobarai_meisai.uf2_cd is 'UF2コード';
comment on column seikyuushobarai_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column seikyuushobarai_meisai.uf3_cd is 'UF3コード';
comment on column seikyuushobarai_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column seikyuushobarai_meisai.uf4_cd is 'UF4コード';
comment on column seikyuushobarai_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column seikyuushobarai_meisai.uf5_cd is 'UF5コード';
comment on column seikyuushobarai_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column seikyuushobarai_meisai.uf6_cd is 'UF6コード';
comment on column seikyuushobarai_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column seikyuushobarai_meisai.uf7_cd is 'UF7コード';
comment on column seikyuushobarai_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column seikyuushobarai_meisai.uf8_cd is 'UF8コード';
comment on column seikyuushobarai_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column seikyuushobarai_meisai.uf9_cd is 'UF9コード';
comment on column seikyuushobarai_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column seikyuushobarai_meisai.uf10_cd is 'UF10コード';
comment on column seikyuushobarai_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column seikyuushobarai_meisai.project_cd is 'プロジェクトコード';
comment on column seikyuushobarai_meisai.project_name is 'プロジェクト名';
comment on column seikyuushobarai_meisai.segment_cd is 'セグメントコード';
comment on column seikyuushobarai_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column seikyuushobarai_meisai.tekiyou_cd is '摘要コード';
comment on column seikyuushobarai_meisai.touroku_user_id is '登録ユーザーID';
comment on column seikyuushobarai_meisai.touroku_time is '登録日時';
comment on column seikyuushobarai_meisai.koushin_user_id is '更新ユーザーID';
comment on column seikyuushobarai_meisai.koushin_time is '更新日時';
INSERT INTO seikyuushobarai_meisai
SELECT
	 denpyou_id
	,denpyou_edano
	,shiwake_edano
	,torihiki_name
	,tekiyou
	,zeiritsu
	,'0'	--軽減税率区分
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


--- 軽減税率対応 経費精算明細
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
comment on table keihiseisan_meisai is '経費精算明細';
comment on column keihiseisan_meisai.denpyou_id is '伝票ID';
comment on column keihiseisan_meisai.denpyou_edano is '伝票枝番号';
comment on column keihiseisan_meisai.shiwake_edano is '仕訳枝番号';
comment on column keihiseisan_meisai.user_id is 'ユーザーID';
comment on column keihiseisan_meisai.shain_no is '社員番号';
comment on column keihiseisan_meisai.user_sei is 'ユーザー姓';
comment on column keihiseisan_meisai.user_mei is 'ユーザー名';
comment on column keihiseisan_meisai.shiyoubi is '使用日';
comment on column keihiseisan_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column keihiseisan_meisai.torihiki_name is '取引名';
comment on column keihiseisan_meisai.tekiyou is '摘要';
comment on column keihiseisan_meisai.zeiritsu is '税率';
comment on column keihiseisan_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column keihiseisan_meisai.shiharai_kingaku is '支払金額';
comment on column keihiseisan_meisai.hontai_kingaku is '本体金額';
comment on column keihiseisan_meisai.shouhizeigaku is '消費税額';
comment on column keihiseisan_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column keihiseisan_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column keihiseisan_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column keihiseisan_meisai.kousaihi_shousai is '交際費詳細';
comment on column keihiseisan_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column keihiseisan_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column keihiseisan_meisai.torihikisaki_cd is '取引先コード';
comment on column keihiseisan_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column keihiseisan_meisai.kari_kamoku_cd is '借方科目コード';
comment on column keihiseisan_meisai.kari_kamoku_name is '借方科目名';
comment on column keihiseisan_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column keihiseisan_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column keihiseisan_meisai.kari_kazei_kbn is '借方課税区分';
comment on column keihiseisan_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column keihiseisan_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column keihiseisan_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column keihiseisan_meisai.kashi_kamoku_name is '貸方科目名';
comment on column keihiseisan_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column keihiseisan_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column keihiseisan_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column keihiseisan_meisai.uf1_cd is 'UF1コード';
comment on column keihiseisan_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column keihiseisan_meisai.uf2_cd is 'UF2コード';
comment on column keihiseisan_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column keihiseisan_meisai.uf3_cd is 'UF3コード';
comment on column keihiseisan_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column keihiseisan_meisai.uf4_cd is 'UF4コード';
comment on column keihiseisan_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column keihiseisan_meisai.uf5_cd is 'UF5コード';
comment on column keihiseisan_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column keihiseisan_meisai.uf6_cd is 'UF6コード';
comment on column keihiseisan_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column keihiseisan_meisai.uf7_cd is 'UF7コード';
comment on column keihiseisan_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column keihiseisan_meisai.uf8_cd is 'UF8コード';
comment on column keihiseisan_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column keihiseisan_meisai.uf9_cd is 'UF9コード';
comment on column keihiseisan_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column keihiseisan_meisai.uf10_cd is 'UF10コード';
comment on column keihiseisan_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column keihiseisan_meisai.project_cd is 'プロジェクトコード';
comment on column keihiseisan_meisai.project_name is 'プロジェクト名';
comment on column keihiseisan_meisai.segment_cd is 'セグメントコード';
comment on column keihiseisan_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column keihiseisan_meisai.tekiyou_cd is '摘要コード';
comment on column keihiseisan_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column keihiseisan_meisai.touroku_user_id is '登録ユーザーID';
comment on column keihiseisan_meisai.touroku_time is '登録日時';
comment on column keihiseisan_meisai.koushin_user_id is '更新ユーザーID';
comment on column keihiseisan_meisai.koushin_time is '更新日時';
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
	,'0'	--軽減税率区分
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


--- 軽減税率対応 自動引落明細
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
comment on table jidouhikiotoshi_meisai is '自動引落明細';
comment on column jidouhikiotoshi_meisai.denpyou_id is '伝票ID';
comment on column jidouhikiotoshi_meisai.denpyou_edano is '伝票枝番号';
comment on column jidouhikiotoshi_meisai.shiwake_edano is '仕訳枝番号';
comment on column jidouhikiotoshi_meisai.torihiki_name is '取引名';
comment on column jidouhikiotoshi_meisai.tekiyou is '摘要';
comment on column jidouhikiotoshi_meisai.zeiritsu is '税率';
comment on column jidouhikiotoshi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column jidouhikiotoshi_meisai.shiharai_kingaku is '支払金額';
comment on column jidouhikiotoshi_meisai.hontai_kingaku is '本体金額';
comment on column jidouhikiotoshi_meisai.shouhizeigaku is '消費税額';
comment on column jidouhikiotoshi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column jidouhikiotoshi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column jidouhikiotoshi_meisai.torihikisaki_cd is '取引先コード';
comment on column jidouhikiotoshi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column jidouhikiotoshi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column jidouhikiotoshi_meisai.kari_kamoku_name is '借方科目名';
comment on column jidouhikiotoshi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column jidouhikiotoshi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column jidouhikiotoshi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column jidouhikiotoshi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column jidouhikiotoshi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column jidouhikiotoshi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column jidouhikiotoshi_meisai.uf1_cd is 'UF1コード';
comment on column jidouhikiotoshi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column jidouhikiotoshi_meisai.uf2_cd is 'UF2コード';
comment on column jidouhikiotoshi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column jidouhikiotoshi_meisai.uf3_cd is 'UF3コード';
comment on column jidouhikiotoshi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column jidouhikiotoshi_meisai.uf4_cd is 'UF4コード';
comment on column jidouhikiotoshi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column jidouhikiotoshi_meisai.uf5_cd is 'UF5コード';
comment on column jidouhikiotoshi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column jidouhikiotoshi_meisai.uf6_cd is 'UF6コード';
comment on column jidouhikiotoshi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column jidouhikiotoshi_meisai.uf7_cd is 'UF7コード';
comment on column jidouhikiotoshi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column jidouhikiotoshi_meisai.uf8_cd is 'UF8コード';
comment on column jidouhikiotoshi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column jidouhikiotoshi_meisai.uf9_cd is 'UF9コード';
comment on column jidouhikiotoshi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column jidouhikiotoshi_meisai.uf10_cd is 'UF10コード';
comment on column jidouhikiotoshi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column jidouhikiotoshi_meisai.project_cd is 'プロジェクトコード';
comment on column jidouhikiotoshi_meisai.project_name is 'プロジェクト名';
comment on column jidouhikiotoshi_meisai.segment_cd is 'セグメントコード';
comment on column jidouhikiotoshi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column jidouhikiotoshi_meisai.tekiyou_cd is '摘要コード';
comment on column jidouhikiotoshi_meisai.touroku_user_id is '登録ユーザーID';
comment on column jidouhikiotoshi_meisai.touroku_time is '登録日時';
comment on column jidouhikiotoshi_meisai.koushin_user_id is '更新ユーザーID';
comment on column jidouhikiotoshi_meisai.koushin_time is '更新日時';
INSERT INTO jidouhikiotoshi_meisai
SELECT
	 denpyou_id
	,denpyou_edano
	,shiwake_edano
	,torihiki_name
	,tekiyou
	,zeiritsu
	,'0'	--軽減税率区分
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


--軽減税率対応 仕訳抽出（SIAS）定義変更
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
comment on table shiwake_sias is '仕訳抽出(SIAS)';
comment on column shiwake_sias.serial_no is 'シリアル番号';
comment on column shiwake_sias.denpyou_id is '伝票ID';
comment on column shiwake_sias.shiwake_status is '仕訳抽出状態';
comment on column shiwake_sias.touroku_time is '登録日時';
comment on column shiwake_sias.koushin_time is '更新日時';
comment on column shiwake_sias.dymd is '（オープン２１）伝票日付';
comment on column shiwake_sias.seiri is '（オープン２１）整理月フラグ';
comment on column shiwake_sias.dcno is '（オープン２１）伝票番号';
comment on column shiwake_sias.kymd is '（オープン２１）起票年月日';
comment on column shiwake_sias.kbmn is '（オープン２１）起票部門コード';
comment on column shiwake_sias.kusr is '（オープン２１）起票者コード';
comment on column shiwake_sias.sgno is '（オープン２１）承認グループNo.';
comment on column shiwake_sias.hf1 is '（オープン２１）ヘッダーフィールド１';
comment on column shiwake_sias.hf2 is '（オープン２１）ヘッダーフィールド２';
comment on column shiwake_sias.hf3 is '（オープン２１）ヘッダーフィールド３';
comment on column shiwake_sias.hf4 is '（オープン２１）ヘッダーフィールド４';
comment on column shiwake_sias.hf5 is '（オープン２１）ヘッダーフィールド５';
comment on column shiwake_sias.hf6 is '（オープン２１）ヘッダーフィールド６';
comment on column shiwake_sias.hf7 is '（オープン２１）ヘッダーフィールド７';
comment on column shiwake_sias.hf8 is '（オープン２１）ヘッダーフィールド８';
comment on column shiwake_sias.hf9 is '（オープン２１）ヘッダーフィールド９';
comment on column shiwake_sias.hf10 is '（オープン２１）ヘッダーフィールド１０';
comment on column shiwake_sias.rbmn is '（オープン２１）借方　部門コード';
comment on column shiwake_sias.rtor is '（オープン２１）借方　取引先コード';
comment on column shiwake_sias.rkmk is '（オープン２１）借方　科目コード';
comment on column shiwake_sias.reda is '（オープン２１）借方　枝番コード';
comment on column shiwake_sias.rkoj is '（オープン２１）借方　工事コード';
comment on column shiwake_sias.rkos is '（オープン２１）借方　工種コード';
comment on column shiwake_sias.rprj is '（オープン２１）借方　プロジェクトコード';
comment on column shiwake_sias.rseg is '（オープン２１）借方　セグメントコード';
comment on column shiwake_sias.rdm1 is '（オープン２１）借方　ユニバーサルフィールド１';
comment on column shiwake_sias.rdm2 is '（オープン２１）借方　ユニバーサルフィールド２';
comment on column shiwake_sias.rdm3 is '（オープン２１）借方　ユニバーサルフィールド３';
comment on column shiwake_sias.rdm4 is '（オープン２１）借方　ユニバーサルフィールド４';
comment on column shiwake_sias.rdm5 is '（オープン２１）借方　ユニバーサルフィールド５';
comment on column shiwake_sias.rdm6 is '（オープン２１）借方　ユニバーサルフィールド６';
comment on column shiwake_sias.rdm7 is '（オープン２１）借方　ユニバーサルフィールド７';
comment on column shiwake_sias.rdm8 is '（オープン２１）借方　ユニバーサルフィールド８';
comment on column shiwake_sias.rdm9 is '（オープン２１）借方　ユニバーサルフィールド９';
comment on column shiwake_sias.rdm10 is '（オープン２１）借方　ユニバーサルフィールド１０';
comment on column shiwake_sias.rdm11 is '（オープン２１）借方　ユニバーサルフィールド１１';
comment on column shiwake_sias.rdm12 is '（オープン２１）借方　ユニバーサルフィールド１２';
comment on column shiwake_sias.rdm13 is '（オープン２１）借方　ユニバーサルフィールド１３';
comment on column shiwake_sias.rdm14 is '（オープン２１）借方　ユニバーサルフィールド１４';
comment on column shiwake_sias.rdm15 is '（オープン２１）借方　ユニバーサルフィールド１５';
comment on column shiwake_sias.rdm16 is '（オープン２１）借方　ユニバーサルフィールド１６';
comment on column shiwake_sias.rdm17 is '（オープン２１）借方　ユニバーサルフィールド１７';
comment on column shiwake_sias.rdm18 is '（オープン２１）借方　ユニバーサルフィールド１８';
comment on column shiwake_sias.rdm19 is '（オープン２１）借方　ユニバーサルフィールド１９';
comment on column shiwake_sias.rdm20 is '（オープン２１）借方　ユニバーサルフィールド２０';
comment on column shiwake_sias.rrit is '（オープン２１）借方　税率';
comment on column shiwake_sias.rkeigen is '（オープン２１）借方　軽減税率区分';
comment on column shiwake_sias.rzkb is '（オープン２１）借方　課税区分';
comment on column shiwake_sias.rgyo is '（オープン２１）借方　業種区分';
comment on column shiwake_sias.rsre is '（オープン２１）借方　仕入区分';
comment on column shiwake_sias.rtky is '（オープン２１）借方　摘要';
comment on column shiwake_sias.rtno is '（オープン２１）借方　摘要コード';
comment on column shiwake_sias.sbmn is '（オープン２１）貸方　部門コード';
comment on column shiwake_sias.stor is '（オープン２１）貸方　取引先コード';
comment on column shiwake_sias.skmk is '（オープン２１）貸方　科目コード';
comment on column shiwake_sias.seda is '（オープン２１）貸方　枝番コード';
comment on column shiwake_sias.skoj is '（オープン２１）貸方　工事コード';
comment on column shiwake_sias.skos is '（オープン２１）貸方　工種コード';
comment on column shiwake_sias.sprj is '（オープン２１）貸方　プロジェクトコード';
comment on column shiwake_sias.sseg is '（オープン２１）貸方　セグメントコード';
comment on column shiwake_sias.sdm1 is '（オープン２１）貸方　ユニバーサルフィールド１';
comment on column shiwake_sias.sdm2 is '（オープン２１）貸方　ユニバーサルフィールド２';
comment on column shiwake_sias.sdm3 is '（オープン２１）貸方　ユニバーサルフィールド３';
comment on column shiwake_sias.sdm4 is '（オープン２１）貸方　ユニバーサルフィールド４';
comment on column shiwake_sias.sdm5 is '（オープン２１）貸方　ユニバーサルフィールド５';
comment on column shiwake_sias.sdm6 is '（オープン２１）貸方　ユニバーサルフィールド６';
comment on column shiwake_sias.sdm7 is '（オープン２１）貸方　ユニバーサルフィールド７';
comment on column shiwake_sias.sdm8 is '（オープン２１）貸方　ユニバーサルフィールド８';
comment on column shiwake_sias.sdm9 is '（オープン２１）貸方　ユニバーサルフィールド９';
comment on column shiwake_sias.sdm10 is '（オープン２１）貸方　ユニバーサルフィールド１０';
comment on column shiwake_sias.sdm11 is '（オープン２１）貸方　ユニバーサルフィールド１１';
comment on column shiwake_sias.sdm12 is '（オープン２１）貸方　ユニバーサルフィールド１２';
comment on column shiwake_sias.sdm13 is '（オープン２１）貸方　ユニバーサルフィールド１３';
comment on column shiwake_sias.sdm14 is '（オープン２１）貸方　ユニバーサルフィールド１４';
comment on column shiwake_sias.sdm15 is '（オープン２１）貸方　ユニバーサルフィールド１５';
comment on column shiwake_sias.sdm16 is '（オープン２１）貸方　ユニバーサルフィールド１６';
comment on column shiwake_sias.sdm17 is '（オープン２１）貸方　ユニバーサルフィールド１７';
comment on column shiwake_sias.sdm18 is '（オープン２１）貸方　ユニバーサルフィールド１８';
comment on column shiwake_sias.sdm19 is '（オープン２１）貸方　ユニバーサルフィールド１９';
comment on column shiwake_sias.sdm20 is '（オープン２１）貸方　ユニバーサルフィールド２０';
comment on column shiwake_sias.srit is '（オープン２１）貸方　税率';
comment on column shiwake_sias.skeigen is '（オープン２１）貸方　軽減税率区分';
comment on column shiwake_sias.szkb is '（オープン２１）貸方　課税区分';
comment on column shiwake_sias.sgyo is '（オープン２１）貸方　業種区分';
comment on column shiwake_sias.ssre is '（オープン２１）貸方　仕入区分';
comment on column shiwake_sias.stky is '（オープン２１）貸方　摘要';
comment on column shiwake_sias.stno is '（オープン２１）貸方　摘要コード';
comment on column shiwake_sias.zkmk is '（オープン２１）消費税対象科目コード';
comment on column shiwake_sias.zrit is '（オープン２１）消費税対象科目税率';
comment on column shiwake_sias.zkeigen is '（オープン２１）消費税対象科目　軽減税率区分';
comment on column shiwake_sias.zzkb is '（オープン２１）消費税対象科目　課税区分';
comment on column shiwake_sias.zgyo is '（オープン２１）消費税対象科目　業種区分';
comment on column shiwake_sias.zsre is '（オープン２１）消費税対象科目　仕入区分';
comment on column shiwake_sias.exvl is '（オープン２１）対価金額';
comment on column shiwake_sias.valu is '（オープン２１）金額';
comment on column shiwake_sias.symd is '（オープン２１）支払日';
comment on column shiwake_sias.skbn is '（オープン２１）支払区分';
comment on column shiwake_sias.skiz is '（オープン２１）支払期日';
comment on column shiwake_sias.uymd is '（オープン２１）回収日';
comment on column shiwake_sias.ukbn is '（オープン２１）入金区分';
comment on column shiwake_sias.ukiz is '（オープン２１）回収期日';
comment on column shiwake_sias.dkec is '（オープン２１）消込コード';
comment on column shiwake_sias.fusr is '（オープン２１）入力者コード';
comment on column shiwake_sias.fsen is '（オープン２１）付箋番号';
comment on column shiwake_sias.tkflg is '（オープン２１）貸借別摘要フラグ';
comment on column shiwake_sias.bunri is '（オープン２１）分離区分';
comment on column shiwake_sias.heic is '（オープン２１）幣種';
comment on column shiwake_sias.rate is '（オープン２１）レート';
comment on column shiwake_sias.gexvl is '（オープン２１）外貨対価金額';
comment on column shiwake_sias.gvalu is '（オープン２１）外貨金額';
comment on column shiwake_sias.gsep is '（オープン２１）行区切り';

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

--軽減税率対応 仕訳抽出（de3）定義変更
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
comment on table shiwake_de3 is '仕訳抽出(de3)';
comment on column shiwake_de3.serial_no is 'シリアル番号';
comment on column shiwake_de3.denpyou_id is '伝票ID';
comment on column shiwake_de3.shiwake_status is '仕訳抽出状態';
comment on column shiwake_de3.touroku_time is '登録日時';
comment on column shiwake_de3.koushin_time is '更新日時';
comment on column shiwake_de3.dymd is '（オープン２１）伝票日付';
comment on column shiwake_de3.seiri is '（オープン２１）整理月フラグ';
comment on column shiwake_de3.dcno is '（オープン２１）伝票番号';
comment on column shiwake_de3.rbmn is '（オープン２１）借方　部門コード';
comment on column shiwake_de3.rtor is '（オープン２１）借方　取引先コード';
comment on column shiwake_de3.rkmk is '（オープン２１）借方　科目コード';
comment on column shiwake_de3.reda is '（オープン２１）借方　枝番コード';
comment on column shiwake_de3.rkoj is '（オープン２１）借方　工事コード';
comment on column shiwake_de3.rkos is '（オープン２１）借方　工種コード';
comment on column shiwake_de3.rprj is '（オープン２１）借方　プロジェクトコード';
comment on column shiwake_de3.rseg is '（オープン２１）借方　セグメントコード';
comment on column shiwake_de3.rdm1 is '（オープン２１）借方　ユニバーサルフィールド１';
comment on column shiwake_de3.rdm2 is '（オープン２１）借方　ユニバーサルフィールド２';
comment on column shiwake_de3.rdm3 is '（オープン２１）借方　ユニバーサルフィールド３';
comment on column shiwake_de3.tky is '（オープン２１）摘要';
comment on column shiwake_de3.tno is '（オープン２１）摘要コード';
comment on column shiwake_de3.sbmn is '（オープン２１）貸方　部門コード';
comment on column shiwake_de3.stor is '（オープン２１）貸方　取引先コード';
comment on column shiwake_de3.skmk is '（オープン２１）貸方　科目コード';
comment on column shiwake_de3.seda is '（オープン２１）貸方　枝番コード';
comment on column shiwake_de3.skoj is '（オープン２１）貸方　工事コード';
comment on column shiwake_de3.skos is '（オープン２１）貸方　工種コード';
comment on column shiwake_de3.sprj is '（オープン２１）貸方　プロジェクトコード';
comment on column shiwake_de3.sseg is '（オープン２１）貸方　セグメントコード';
comment on column shiwake_de3.sdm1 is '（オープン２１）貸方　ユニバーサルフィールド１';
comment on column shiwake_de3.sdm2 is '（オープン２１）貸方　ユニバーサルフィールド２';
comment on column shiwake_de3.sdm3 is '（オープン２１）貸方　ユニバーサルフィールド３';
comment on column shiwake_de3.exvl is '（オープン２１）対価金額';
comment on column shiwake_de3.valu is '（オープン２１）金額';
comment on column shiwake_de3.zkmk is '（オープン２１）消費税対象科目コード';
comment on column shiwake_de3.zrit is '（オープン２１）消費税対象科目税率';
comment on column shiwake_de3.zkeigen is '（オープン２１）消費税対象科目　軽減税率区分';
comment on column shiwake_de3.zzkb is '（オープン２１）消費税対象科目　課税区分';
comment on column shiwake_de3.zgyo is '（オープン２１）消費税対象科目　業種区分';
comment on column shiwake_de3.zsre is '（オープン２１）消費税対象科目　仕入区分';
comment on column shiwake_de3.rrit is '（オープン２１）借方　税率';
comment on column shiwake_de3.rkeigen is '（オープン２１）借方　軽減税率区分';
comment on column shiwake_de3.srit is '（オープン２１）貸方　税率';
comment on column shiwake_de3.skeigen is '（オープン２１）貸方　軽減税率区分';
comment on column shiwake_de3.rzkb is '（オープン２１）借方　課税区分';
comment on column shiwake_de3.rgyo is '（オープン２１）借方　業種区分';
comment on column shiwake_de3.rsre is '（オープン２１）借方　仕入区分';
comment on column shiwake_de3.szkb is '（オープン２１）貸方　課税区分';
comment on column shiwake_de3.sgyo is '（オープン２１）貸方　業種区分';
comment on column shiwake_de3.ssre is '（オープン２１）貸方　仕入区分';
comment on column shiwake_de3.symd is '（オープン２１）支払日';
comment on column shiwake_de3.skbn is '（オープン２１）支払区分';
comment on column shiwake_de3.skiz is '（オープン２１）支払期日';
comment on column shiwake_de3.uymd is '（オープン２１）回収日';
comment on column shiwake_de3.ukbn is '（オープン２１）入金区分';
comment on column shiwake_de3.ukiz is '（オープン２１）回収期日';
comment on column shiwake_de3.sten is '（オープン２１）店券フラグ';
comment on column shiwake_de3.dkec is '（オープン２１）消込コード';
comment on column shiwake_de3.kymd is '（オープン２１）起票年月日';
comment on column shiwake_de3.kbmn is '（オープン２１）起票部門コード';
comment on column shiwake_de3.kusr is '（オープン２１）起票者コード';
comment on column shiwake_de3.fusr is '（オープン２１）入力者コード';
comment on column shiwake_de3.fsen is '（オープン２１）付箋番号';
comment on column shiwake_de3.sgno is '（オープン２１）承認グループNo.';
comment on column shiwake_de3.bunri is '（オープン２１）分離区分';
comment on column shiwake_de3.rate is '（オープン２１）レート';
comment on column shiwake_de3.gexvl is '（オープン２１）外貨対価金額';
comment on column shiwake_de3.gvalu is '（オープン２１）外貨金額';
comment on column shiwake_de3.gsep is '（オープン２１）行区切り';

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

--連絡票0604_総合付替伝票 付替区分デフォルト値対応 新規テーブル追加
create table user_default_value (
  kbn character varying(50) not null
  , user_id character varying(30) not null
  , default_value character varying(1) not null
  , primary key (kbn,user_id)
)WITHOUT OIDS;
comment on table user_default_value is 'ユーザー別デフォルト値';
comment on column user_default_value.kbn is '区分';
comment on column user_default_value.user_id is 'ユーザーID';
comment on column user_default_value.default_value is 'デフォルト値';

--連絡票0528 XML形式の全銀フォーマットへの対応 会社情報カラム追加
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

comment on table kaisha_info is '会社情報';
comment on column kaisha_info.kessanki_bangou is '決算期番号';
comment on column kaisha_info.hf1_shiyou_flg is 'HF1使用フラグ';
comment on column kaisha_info.hf1_hissu_flg is 'HF1必須フラグ';
comment on column kaisha_info.hf1_name is 'HF1名';
comment on column kaisha_info.hf2_shiyou_flg is 'HF2使用フラグ';
comment on column kaisha_info.hf2_hissu_flg is 'HF2必須フラグ';
comment on column kaisha_info.hf2_name is 'HF2名';
comment on column kaisha_info.hf3_shiyou_flg is 'HF3使用フラグ';
comment on column kaisha_info.hf3_hissu_flg is 'HF3必須フラグ';
comment on column kaisha_info.hf3_name is 'HF3名';
comment on column kaisha_info.hf4_shiyou_flg is 'HF4使用フラグ';
comment on column kaisha_info.hf4_hissu_flg is 'HF4必須フラグ';
comment on column kaisha_info.hf4_name is 'HF4名';
comment on column kaisha_info.hf5_shiyou_flg is 'HF5使用フラグ';
comment on column kaisha_info.hf5_hissu_flg is 'HF5必須フラグ';
comment on column kaisha_info.hf5_name is 'HF5名';
comment on column kaisha_info.hf6_shiyou_flg is 'HF6使用フラグ';
comment on column kaisha_info.hf6_hissu_flg is 'HF6必須フラグ';
comment on column kaisha_info.hf6_name is 'HF6名';
comment on column kaisha_info.hf7_shiyou_flg is 'HF7使用フラグ';
comment on column kaisha_info.hf7_hissu_flg is 'HF7必須フラグ';
comment on column kaisha_info.hf7_name is 'HF7名';
comment on column kaisha_info.hf8_shiyou_flg is 'HF8使用フラグ';
comment on column kaisha_info.hf8_hissu_flg is 'HF8必須フラグ';
comment on column kaisha_info.hf8_name is 'HF8名';
comment on column kaisha_info.hf9_shiyou_flg is 'HF9使用フラグ';
comment on column kaisha_info.hf9_hissu_flg is 'HF9必須フラグ';
comment on column kaisha_info.hf9_name is 'HF9名';
comment on column kaisha_info.hf10_shiyou_flg is 'HF10使用フラグ';
comment on column kaisha_info.hf10_hissu_flg is 'HF10必須フラグ';
comment on column kaisha_info.hf10_name is 'HF10名';
comment on column kaisha_info.uf1_shiyou_flg is 'UF1使用フラグ';
comment on column kaisha_info.uf1_name is 'UF1名';
comment on column kaisha_info.uf2_shiyou_flg is 'UF2使用フラグ';
comment on column kaisha_info.uf2_name is 'UF2名';
comment on column kaisha_info.uf3_shiyou_flg is 'UF3使用フラグ';
comment on column kaisha_info.uf3_name is 'UF3名';
comment on column kaisha_info.uf4_shiyou_flg is 'UF4使用フラグ';
comment on column kaisha_info.uf4_name is 'UF4名';
comment on column kaisha_info.uf5_shiyou_flg is 'UF5使用フラグ';
comment on column kaisha_info.uf5_name is 'UF5名';
comment on column kaisha_info.uf6_shiyou_flg is 'UF6使用フラグ';
comment on column kaisha_info.uf6_name is 'UF6名';
comment on column kaisha_info.uf7_shiyou_flg is 'UF7使用フラグ';
comment on column kaisha_info.uf7_name is 'UF7名';
comment on column kaisha_info.uf8_shiyou_flg is 'UF8使用フラグ';
comment on column kaisha_info.uf8_name is 'UF8名';
comment on column kaisha_info.uf9_shiyou_flg is 'UF9使用フラグ';
comment on column kaisha_info.uf9_name is 'UF9名';
comment on column kaisha_info.uf10_shiyou_flg is 'UF10使用フラグ';
comment on column kaisha_info.uf10_name is 'UF10名';
comment on column kaisha_info.uf_kotei1_shiyou_flg is 'UF1使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei1_name is 'UF1名(固定値)';
comment on column kaisha_info.uf_kotei2_shiyou_flg is 'UF2使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei2_name is 'UF2名(固定値)';
comment on column kaisha_info.uf_kotei3_shiyou_flg is 'UF3使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei3_name is 'UF3名(固定値)';
comment on column kaisha_info.uf_kotei4_shiyou_flg is 'UF4使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei4_name is 'UF4名(固定値)';
comment on column kaisha_info.uf_kotei5_shiyou_flg is 'UF5使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei5_name is 'UF5名(固定値)';
comment on column kaisha_info.uf_kotei6_shiyou_flg is 'UF6使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei6_name is 'UF6名(固定値)';
comment on column kaisha_info.uf_kotei7_shiyou_flg is 'UF7使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei7_name is 'UF7名(固定値)';
comment on column kaisha_info.uf_kotei8_shiyou_flg is 'UF8使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei8_name is 'UF8名(固定値)';
comment on column kaisha_info.uf_kotei9_shiyou_flg is 'UF9使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei9_name is 'UF9名(固定値)';
comment on column kaisha_info.uf_kotei10_shiyou_flg is 'UF10使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei10_name is 'UF10名(固定値)';
comment on column kaisha_info.pjcd_shiyou_flg is 'プロジェクトコード使用フラグ';
comment on column kaisha_info.sgcd_shiyou_flg is 'セグメントコード使用フラグ';
comment on column kaisha_info.saimu_shiyou_flg is '債務使用フラグ';
comment on column kaisha_info.kamoku_cd_type is '科目コードタイプ';
comment on column kaisha_info.kamoku_edaban_cd_type is '科目枝番コードタイプ';
comment on column kaisha_info.futan_bumon_cd_type is '負担部門コードタイプ';
comment on column kaisha_info.torihikisaki_cd_type is '取引先コードタイプ';
comment on column kaisha_info.segment_cd_type is 'セグメントコードタイプ';
comment on column kaisha_info.houjin_bangou is '法人番号';

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
