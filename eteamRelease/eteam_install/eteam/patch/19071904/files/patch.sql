SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- 画面権限制御
DELETE FROM gamen_kengen_seigyo WHERE gamen_id LIKE 'Genyokin%';
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- 画面項目制御
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

--連絡票No.878 
--内部コード ※オプション用レコードはパッチ前になければ削除
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- 支払依頼申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- 請求書払申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

--伝票一覧
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
comment on table denpyou_ichiran_kyoten is '伝票一覧(拠点)';
comment on column denpyou_ichiran_kyoten.denpyou_id is '伝票ID';
comment on column denpyou_ichiran_kyoten.denpyou_kbn is '伝票区分';
comment on column denpyou_ichiran_kyoten.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column denpyou_ichiran_kyoten.serial_no is 'シリアル番号';
comment on column denpyou_ichiran_kyoten.denpyou_joutai is '伝票状態';
comment on column denpyou_ichiran_kyoten.name is 'ステータス';
comment on column denpyou_ichiran_kyoten.denpyou_shubetsu_url is '伝票種別URL';
comment on column denpyou_ichiran_kyoten.touroku_time is '登録日時';
comment on column denpyou_ichiran_kyoten.koushin_time is '更新日時';
comment on column denpyou_ichiran_kyoten.shouninbi is '承認日';
comment on column denpyou_ichiran_kyoten.bumon_cd is '部門コード';
comment on column denpyou_ichiran_kyoten.bumon_full_name is '部門フル名';
comment on column denpyou_ichiran_kyoten.user_id is 'ユーザーID';
comment on column denpyou_ichiran_kyoten.user_full_name is 'ユーザーフル名';
comment on column denpyou_ichiran_kyoten.all_cnt is '全承認人数カウント';
comment on column denpyou_ichiran_kyoten.cur_cnt is '承認済人数カウント';
comment on column denpyou_ichiran_kyoten.zan_cnt is '残り承認人数カウント';
comment on column denpyou_ichiran_kyoten.gen_bumon_full_name is '現在承認者部門フル名';
comment on column denpyou_ichiran_kyoten.gen_user_id is '現在承認者ユーザーIDリスト';
comment on column denpyou_ichiran_kyoten.gen_user_full_name is '現在承認者ユーザーフル名';
comment on column denpyou_ichiran_kyoten.gen_name is '現在承認者名称';
comment on column denpyou_ichiran_kyoten.shiwake_status is '仕訳データ作成ステータス';
comment on column denpyou_ichiran_kyoten.kari_kingaku_goukei is '借方金額合計(一覧検索用)';
comment on column denpyou_ichiran_kyoten.kashi_kingaku_goukei is '貸方金額合計(一覧検索用)';
comment on column denpyou_ichiran_kyoten.denpyou_date is '伝票日付(一覧検索用)';
comment on column denpyou_ichiran_kyoten.hf1_cd is 'HF1コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten.hf2_cd is 'HF2コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten.hf3_cd is 'HF3コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten.hf4_cd is 'HF4コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten.hf5_cd is 'HF5コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten.hf6_cd is 'HF6コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten.hf7_cd is 'HF7コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten.hf8_cd is 'HF8コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten.hf9_cd is 'HF9コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten.hf10_cd is 'HF10コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten.bumon_tsukekae_kekka_flg is '部門付替結果フラグ(一覧検索用)';

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
comment on table denpyou_ichiran_kyoten_meisai is '伝票一覧(拠点)明細';
comment on column denpyou_ichiran_kyoten_meisai.denpyou_id is '伝票ID';
comment on column denpyou_ichiran_kyoten_meisai.denpyou_edano is '伝票枝番号';
comment on column denpyou_ichiran_kyoten_meisai.meisai_serial_no is '明細シリアルNo';
comment on column denpyou_ichiran_kyoten_meisai.nyushukkin_kbn is '入出金区分(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.shiwake_edano is '仕訳枝番号(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.torihiki_name is '取引名(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.bumon_tsukekae_taishou_flg is '部門付替対象フラグ(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.bumon_tsukekae_shori_flg is '部門付替処理フラグ(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.shiwake_taishougai_flg is '仕訳対象外フラグ(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.tenpu_file_flg is '添付ファイルフラグ(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.ebunsho_flg is 'e文書対象フラグ(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.fusen_flg is '付箋フラグ(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.fusen_color is '付箋カラー(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_futan_bumon_cd is '借方負担部門コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_futan_bumon_name is '借方負担部門名(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kamoku_cd is '借方科目コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kamoku_name is '借方科目名(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kamoku_edaban_name is '借方科目枝番名(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_torihikisaki_cd is '借方取引先コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_torihikisaki_name_ryakushiki is '借方取引先名（略式）(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kingaku is '借方金額(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_shouhizeigaku is '借方消費税額(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_taika is '借方対価(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_tekiyou is '借方摘要(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kazei_kbn is '借方課税区分(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_zeiritsu is '借方税率(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_keigen_zeiritsu_kbn is '借方軽減税率区分(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_bunri_kbn is '借方分離区分(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_kobetsu_kbn is '借方個別区分(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_shouhizeitaishou_kamoku_cd is '借方税対象科目(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_project_cd is '借方プロジェクトコード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_segment_cd is '借方セグメントコード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf1_cd is '借方UF1コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf2_cd is '借方UF2コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf3_cd is '借方UF3コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf4_cd is '借方UF4コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf5_cd is '借方UF5コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf6_cd is '借方UF6コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf7_cd is '借方UF7コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf8_cd is '借方UF8コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf9_cd is '借方UF9コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_uf10_cd is '借方UF10コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_heishu_cd is '借方幣種コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_rate is '借方レート(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_gaika is '借方外貨金額(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_gaika_shouhizeigaku is '借方外貨消費税額(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kari_gaika_taika is '借方外貨対価(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_futan_bumon_cd is '貸方負担部門コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_futan_bumon_name is '貸方負担部門名(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kamoku_cd is '貸方科目コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kamoku_name is '貸方科目名(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_torihikisaki_cd is '貸方取引先コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_torihikisaki_name_ryakushiki is '貸方取引先名（略式）(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kingaku is '貸方金額(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_shouhizeigaku is '貸方消費税額(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_taika is '貸方対価(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_tekiyou is '貸方摘要(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kazei_kbn is '貸方課税区分(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_zeiritsu is '貸方税率(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_keigen_zeiritsu_kbn is '貸方軽減税率区分(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_bunri_kbn is '貸方分離区分(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_kobetsu_kbn is '貸方個別区分(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_shouhizeitaishou_kamoku_cd is '貸方税対象科目(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_project_cd is '貸方プロジェクトコード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_segment_cd is '貸方セグメントコード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf1_cd is '貸方UF1コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf2_cd is '貸方UF2コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf3_cd is '貸方UF3コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf4_cd is '貸方UF4コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf5_cd is '貸方UF5コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf6_cd is '貸方UF6コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf7_cd is '貸方UF7コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf8_cd is '貸方UF8コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf9_cd is '貸方UF9コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_uf10_cd is '貸方UF10コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_heishu_cd is '貸方幣種コード(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_rate is '貸方レート(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_gaika is '貸方外貨金額(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_gaika_shouhizeigaku is '貸方外貨消費税額(一覧検索用)';
comment on column denpyou_ichiran_kyoten_meisai.kashi_gaika_taika is '貸方外貨対価(一覧検索用)';


commit;
