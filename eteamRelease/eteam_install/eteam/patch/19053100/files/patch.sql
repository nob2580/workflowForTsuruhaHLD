SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--連絡票_0666_WEB印刷ボタンの表示制御 内部コード設定更新
--連絡票_0715 財務枝番コード連携対応も同時に実施
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- 支払依頼申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- 請求書払申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

--連絡票_0666_WEB印刷ボタンの表示制御 機能制御追加
\copy kinou_seigyo FROM '.\files\csv\kinou_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--連絡票_0715 財務枝番コード連携対応 仕訳パターン設定
CREATE TABLE shiwake_pattern_setting_tmp AS SELECT * FROM shiwake_pattern_setting WHERE 1 = 2;
\copy shiwake_pattern_setting_tmp FROM '.\files\csv\shiwake_pattern_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM shiwake_pattern_setting_tmp WHERE denpyou_kbn='A003' AND (SELECT COUNT(*) FROM shiwake_pattern_setting WHERE denpyou_kbn='A003') = 0;
DELETE FROM shiwake_pattern_setting_tmp WHERE denpyou_kbn='A013' AND (SELECT COUNT(*) FROM shiwake_pattern_setting WHERE denpyou_kbn='A013') = 0;
DELETE FROM shiwake_pattern_setting;
INSERT INTO shiwake_pattern_setting SELECT * FROM shiwake_pattern_setting_tmp;
DROP TABLE shiwake_pattern_setting_tmp;

--連絡票_0715 財務枝番コード連携対応 仕訳パターンマスタテーブル更新
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
  , edaban_renkei_flg character varying(1) not null
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
);

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
comment on column shiwake_pattern_master.edaban_renkei_flg is '財務枝番コード連携フラグ';
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
   ,'' -- edaban_renkei_flg
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
   ,kari_zeiritsu
   ,kari_keigen_zeiritsu_kbn
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
UPDATE shiwake_pattern_master SET edaban_renkei_flg = '0' WHERE denpyou_kbn = 'A004' OR denpyou_kbn = 'A010' OR denpyou_kbn = 'A011';
DROP TABLE shiwake_pattern_master_old;

--連絡票_0715 財務枝番コード連携対応 交通手段マスタテーブル更新
ALTER TABLE koutsuu_shudan_master RENAME TO koutsuu_shudan_master_old;
create table koutsuu_shudan_master (
  sort_jun character(3) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , primary key (sort_jun,koutsuu_shudan)
);
comment on table koutsuu_shudan_master is '国内用交通手段マスター';
comment on column koutsuu_shudan_master.sort_jun is '並び順';
comment on column koutsuu_shudan_master.koutsuu_shudan is '交通手段';
comment on column koutsuu_shudan_master.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column koutsuu_shudan_master.zei_kubun is '税区分';
comment on column koutsuu_shudan_master.edaban is '枝番コード';
INSERT INTO koutsuu_shudan_master
SELECT
  sort_jun
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , zei_kubun
  ,'' -- edaban
FROM koutsuu_shudan_master_old;
DROP TABLE koutsuu_shudan_master_old;

--連絡票_0715 財務枝番コード連携対応 海外交通手段マスタテーブル更新
ALTER TABLE kaigai_koutsuu_shudan_master RENAME TO kaigai_koutsuu_shudan_master_old;
create table kaigai_koutsuu_shudan_master (
  sort_jun character(3) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , primary key (sort_jun,koutsuu_shudan)
);
comment on table kaigai_koutsuu_shudan_master is '海外用交通手段マスター';
comment on column kaigai_koutsuu_shudan_master.sort_jun is '並び順';
comment on column kaigai_koutsuu_shudan_master.koutsuu_shudan is '交通手段';
comment on column kaigai_koutsuu_shudan_master.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column kaigai_koutsuu_shudan_master.zei_kubun is '税区分';
comment on column kaigai_koutsuu_shudan_master.edaban is '枝番コード';
INSERT INTO kaigai_koutsuu_shudan_master
SELECT
  sort_jun
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , zei_kubun
  ,'' -- edaban
FROM kaigai_koutsuu_shudan_master_old;
DROP TABLE kaigai_koutsuu_shudan_master_old;

--連絡票_0715 財務枝番コード連携対応 日当等マスタテーブル更新
ALTER TABLE nittou_nado_master RENAME TO nittou_nado_master_old;
create table nittou_nado_master (
  shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , yakushoku_cd character varying(10) not null
  , tanka numeric(15)
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , nittou_shukuhakuhi_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , primary key (shubetsu1,shubetsu2,yakushoku_cd)
);
comment on table nittou_nado_master is '国内用日当等マスター';
comment on column nittou_nado_master.shubetsu1 is '種別１';
comment on column nittou_nado_master.shubetsu2 is '種別２';
comment on column nittou_nado_master.yakushoku_cd is '役職コード';
comment on column nittou_nado_master.tanka is '単価';
comment on column nittou_nado_master.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column nittou_nado_master.nittou_shukuhakuhi_flg is '日当・宿泊費フラグ';
comment on column nittou_nado_master.zei_kubun is '税区分';
comment on column nittou_nado_master.edaban is '枝番コード';
INSERT INTO nittou_nado_master
SELECT
  shubetsu1
  , shubetsu2
  , yakushoku_cd
  , tanka
  , shouhyou_shorui_hissu_flg
  , nittou_shukuhakuhi_flg
  , zei_kubun
  ,'' -- edaban
FROM nittou_nado_master_old;
DROP TABLE nittou_nado_master_old;

--連絡票_0715 財務枝番コード連携対応 海外日当等マスタテーブル更新
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
  , edaban character varying(12) not null
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
comment on column kaigai_nittou_nado_master.edaban is '枝番コード';
INSERT INTO kaigai_nittou_nado_master
SELECT
  shubetsu1
  , shubetsu2
  , yakushoku_cd
  , tanka
  , heishu_cd
  , currency_unit
  , tanka_gaika
  , shouhyou_shorui_hissu_flg
  , nittou_shukuhakuhi_flg
  , zei_kubun
  ,'' -- edaban
FROM kaigai_nittou_nado_master_old;
DROP TABLE kaigai_nittou_nado_master_old;


-- 連絡票_0715 財務枝番コード連携対応  マスター管理版数（国内(海外)交通費・日当等マスター）
UPDATE master_kanri_hansuu SET delete_flg = '1' WHERE master_id = 'nittou_nado_master' OR master_id = 'kaigai_nittou_nado_master' OR master_id = 'koutsuu_shudan_master' OR master_id = 'kaigai_koutsuu_shudan_master';

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
   'nittou_nado_master'		--国内日当等マスター
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='nittou_nado_master')
 , '0'
 , '国内日当等_patch.csv'
 , length(convert_to(E'種別１,種別２,役職コード,単価,証憑書類必須フラグ,日当・宿泊費フラグ,税区分,枝番コード\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun,edaban\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(1),varchar(1),varchar(1),varchar(12)\r\n1,1,1,,2,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun || ',' || edaban FROM nittou_nado_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'種別１,種別２,役職コード,単価,証憑書類必須フラグ,日当・宿泊費フラグ,税区分,枝番コード\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun,edaban\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(1),varchar(1),varchar(1),varchar(12)\r\n1,1,1,,2,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun || ',' || edaban FROM nittou_nado_master), E'\r\n') || E'\r\n','sjis')
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
 , length(convert_to(E'種別１,種別２,役職コード,単価（邦貨）,幣種コード,通貨単位,単価（外貨）,証憑書類必須フラグ,日当・宿泊費フラグ,税区分,枝番コード\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,heishu_cd,currency_unit,tanka_gaika,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun,edaban\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(4),varchar(20),decimal,varchar(1),varchar(1),varchar(1),varchar(12)\r\n1,1,1,,2,2,,2,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || heishu_cd || ',' || currency_unit || ',' || COALESCE(tanka_gaika::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun || ',' || edaban FROM kaigai_nittou_nado_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'種別１,種別２,役職コード,単価（邦貨）,幣種コード,通貨単位,単価（外貨）,証憑書類必須フラグ,日当・宿泊費フラグ,税区分,枝番コード\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,heishu_cd,currency_unit,tanka_gaika,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun,edaban\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(4),varchar(20),decimal,varchar(1),varchar(1),varchar(1),varchar(12)\r\n1,1,1,,2,2,,2,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || heishu_cd || ',' || currency_unit || ',' || COALESCE(tanka_gaika::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun || ',' || edaban FROM kaigai_nittou_nado_master), E'\r\n') || E'\r\n','sjis')
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
   'koutsuu_shudan_master'		--国内交通手段マスター
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='koutsuu_shudan_master')
 , '0'
 , '国内交通手段_patch.csv'
 , length(convert_to(E'並び順,交通手段,証憑書類必須フラグ,税区分,枝番コード\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun,edaban\r\nvarchar(3),varchar(10),varchar(1),varchar(1),varchar(12)\r\n1,1,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun || ',' || edaban FROM koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'並び順,交通手段,証憑書類必須フラグ,税区分,枝番コード\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun,edaban\r\nvarchar(3),varchar(10),varchar(1),varchar(1),varchar(12)\r\n1,1,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun || ',' || edaban FROM koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis')
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
   'kaigai_koutsuu_shudan_master'		--海外交通手段マスター
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='kaigai_koutsuu_shudan_master')
 , '0'
 , '海外交通手段_patch.csv'
 , length(convert_to(E'並び順,交通手段,証憑書類必須フラグ,税区分,枝番コード\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun,edaban\r\nvarchar(3),varchar(10),varchar(1),varchar(1),varchar(12)\r\n1,1,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun || ',' || edaban FROM kaigai_koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'並び順,交通手段,証憑書類必須フラグ,税区分,枝番コード\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun,edaban\r\nvarchar(3),varchar(10),varchar(1),varchar(1),varchar(12)\r\n1,1,2,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun || ',' || edaban FROM kaigai_koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
 );

-- 0710_請求書払い申請と支払依頼申請の支払日一括登録対応 設定定義追加 ※hyouji_jun900以降はカスタマイズ領域
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

commit;