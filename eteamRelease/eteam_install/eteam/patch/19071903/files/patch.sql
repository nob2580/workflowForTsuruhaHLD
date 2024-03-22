SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- 画面権限制御
DELETE FROM gamen_kengen_seigyo WHERE gamen_id LIKE 'Genyokin%';
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
	-- 「財務拠点入力検索」を「拠点入力一覧」に、「財務拠点入力パターン選択」を「拠点入力パターン選択」変更
	UPDATE gamen_kengen_seigyo SET gamen_name = '拠点入力一覧' WHERE gamen_name = '財務拠点入力検索';
	UPDATE gamen_kengen_seigyo SET gamen_name = '拠点入力パターン選択' WHERE gamen_name = '財務拠点入力パターン選択';

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

--内部コード ※オプション用レコードはパッチ前になければ削除
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- 支払依頼申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- 請求書払申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

-- 設定情報データ変更 ※hyouji_jun900以降はカスタマイズ領域
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
	--OEPN21連携のvalをOPEN21連携にコピーする（財務拠点入力対応）
	UPDATE setting_info new SET setting_val = (
	 SELECT setting_val
	 FROM setting_info_tmp tmp 
	 WHERE tmp.setting_name = replace(new.setting_name, '_kyoten', ''))
	WHERE new.setting_name LIKE '%_kyoten'; 
DROP TABLE setting_info_tmp;

-- 伝票種別一覧（財務拠点）
DELETE FROM denpyou_shubetsu_ichiran_kyoten;
\copy denpyou_shubetsu_ichiran_kyoten FROM '.\files\csv\denpyou_shubetsu_ichiran_kyoten.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- 現預金出納帳、現預金出納帳明細
-- genyokin_suitouchou→suitouchouに統一するため変更
create table suitouchou (
  denpyou_id character varying(19) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , denpyou_date date not null
  , nyukin_goukei numeric(15) not null
  , shukkin_goukei numeric(15) not null
  , toujitsu_zandaka numeric(15) not null
  , suitou_kamoku_cd character varying(6) not null
  , suitou_kamoku_name character varying(22) not null
  , suitou_kamoku_edaban_cd character varying(12) not null
  , suitou_kamoku_edaban_name character varying(20) not null
  , suitou_futan_bumon_cd character varying(8) not null
  , suitou_futan_bumon_name character varying(20) not null
  , bikou character varying(40) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint suitouchou_PKEY primary key (denpyou_id)
);

create table suitouchou_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , denpyou_date date not null
  , serial_no bigint not null
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , nyukin_goukei numeric(15)
  , shukkin_goukei numeric(15)
  , aite_kazei_kbn character varying(3) not null
  , aite_zeiritsu numeric(3) not null
  , aite_keigen_zeiritsu_kbn character varying(1) not null
  , fusen_color character varying(1) not null
  , meisai_touroku_user_id character varying(30) not null
  , meisai_touroku_shain_no character varying(15) not null
  , meisai_touroku_user_sei character varying(10) not null
  , meisai_touroku_user_mei character varying(10) not null
  , tenpu_file_edano character varying(40) not null
  , shiwake_serial_no bigserial not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint suitouchou_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

comment on table suitouchou is '現預金出納帳';
comment on column suitouchou.denpyou_id is '伝票ID';
comment on column suitouchou.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column suitouchou.denpyou_date is '伝票日付';
comment on column suitouchou.nyukin_goukei is '入金合計';
comment on column suitouchou.shukkin_goukei is '出金合計';
comment on column suitouchou.toujitsu_zandaka is '当日残高';
comment on column suitouchou.suitou_kamoku_cd is '出納科目コード';
comment on column suitouchou.suitou_kamoku_name is '出納科目名';
comment on column suitouchou.suitou_kamoku_edaban_cd is '出納科目枝番コード';
comment on column suitouchou.suitou_kamoku_edaban_name is '出納科目枝番名';
comment on column suitouchou.suitou_futan_bumon_cd is '出納負担部門コード';
comment on column suitouchou.suitou_futan_bumon_name is '出納負担部門名';
comment on column suitouchou.bikou is '備考';
comment on column suitouchou.touroku_user_id is '登録ユーザーID';
comment on column suitouchou.touroku_time is '登録日時';
comment on column suitouchou.koushin_user_id is '更新ユーザーID';
comment on column suitouchou.koushin_time is '更新日時';

comment on table suitouchou_meisai is '現預金出納帳明細';
comment on column suitouchou_meisai.denpyou_id is '伝票ID';
comment on column suitouchou_meisai.denpyou_edano is '伝票枝番号';
comment on column suitouchou_meisai.shiwake_edano is '仕訳枝番号';
comment on column suitouchou_meisai.denpyou_date is '伝票日付';
comment on column suitouchou_meisai.serial_no is 'シリアル番号';
comment on column suitouchou_meisai.torihiki_name is '取引名';
comment on column suitouchou_meisai.tekiyou is '摘要';
comment on column suitouchou_meisai.nyukin_goukei is '入金';
comment on column suitouchou_meisai.shukkin_goukei is '出金';
comment on column suitouchou_meisai.aite_kazei_kbn is '課税区分';
comment on column suitouchou_meisai.aite_zeiritsu is '税率';
comment on column suitouchou_meisai.aite_keigen_zeiritsu_kbn is '軽減税率区分';
comment on column suitouchou_meisai.fusen_color is '付箋カラー';
comment on column suitouchou_meisai.meisai_touroku_user_id is '明細登録ユーザーID';
comment on column suitouchou_meisai.meisai_touroku_shain_no is '明細登録ユーザー社員No';
comment on column suitouchou_meisai.meisai_touroku_user_sei is '明細登録ユーザー姓';
comment on column suitouchou_meisai.meisai_touroku_user_mei is '明細登録ユーザー名';
comment on column suitouchou_meisai.tenpu_file_edano is '添付ファイル枝番';
comment on column suitouchou_meisai.shiwake_serial_no is '仕訳データシリアルNo';
comment on column suitouchou_meisai.touroku_user_id is '登録ユーザーID';
comment on column suitouchou_meisai.touroku_time is '登録日時';
comment on column suitouchou_meisai.koushin_user_id is '更新ユーザーID';
comment on column suitouchou_meisai.koushin_time is '更新日時';

INSERT INTO suitouchou SELECT * FROM genyokin_suitouchou;
DROP TABLE genyokin_suitouchou;
INSERT INTO suitouchou_meisai SELECT * FROM genyokin_suitouchou_meisai;
DROP TABLE genyokin_suitouchou_meisai;

-- 配賦（パターン、科目、部門）
create table zaimu_kyoten_nyuryoku_haifu_pattern (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_name character varying(20) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_haifu_pattern_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,zaimu_kyoten_nyuryoku_haifu_pattern_no)
);
comment on table zaimu_kyoten_nyuryoku_haifu_pattern is '拠点入力配賦パターン';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.zaimu_kyoten_nyuryoku_haifu_pattern_no is '配賦パターンNo';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.zaimu_kyoten_nyuryoku_haifu_pattern_name is '配賦パターン名';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_haifu_pattern.koushin_time is '更新日時';

create table zaimu_kyoten_nyuryoku_haifu_bumon (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , futan_bumon_cd character varying(8) not null
  , haifu_rate numeric(7,4) not null
  , sagaku_flg character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp(6) without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp(6) without time zone not null
  , constraint zaimu_kyoten_nyuryoku_haifu_bumon_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,zaimu_kyoten_nyuryoku_haifu_pattern_no,yuukou_kigen_from,futan_bumon_cd)
);
comment on table zaimu_kyoten_nyuryoku_haifu_bumon is '拠点入力配賦部門';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.zaimu_kyoten_nyuryoku_haifu_pattern_no is '拠点入力配賦パターンNo';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.yuukou_kigen_from is '有効期限開始日';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.yuukou_kigen_to is '有効期限終了日';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.futan_bumon_cd is '負担部門コード';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.haifu_rate is '配賦率';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.sagaku_flg is '差額フラグ';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_haifu_bumon.koushin_time is '更新日時';

create table zaimu_kyoten_nyuryoku_haifu_kamoku (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , kamoku_gaibu_cd character varying(8) not null
  , kamoku_edaban_cd character varying(12) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp(6) without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp(6) without time zone not null
  , constraint zaimu_kyoten_nyuryoku_haifu_kamoku_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,zaimu_kyoten_nyuryoku_haifu_pattern_no,yuukou_kigen_from,kamoku_gaibu_cd,kamoku_edaban_cd)
);
comment on table zaimu_kyoten_nyuryoku_haifu_kamoku is '拠点入力配賦科目';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.zaimu_kyoten_nyuryoku_haifu_pattern_no is '拠点入力配賦パターンNo';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.yuukou_kigen_from is '有効期限開始日';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.yuukou_kigen_to is '有効期限終了日';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.kamoku_gaibu_cd is '科目外部コード';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.kamoku_edaban_cd is '科目枝番コード';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_haifu_kamoku.koushin_time is '更新日時';

-- 仕訳シリアル番号採番（財務拠点入力）
create table zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban (
  sequence_val bigint
);
comment on table zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban is '拠点入力仕訳シリアル番号採番';
comment on column zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban.sequence_val is 'シーケンス値';
INSERT INTO zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban 
VALUES (CASE 
			WHEN (SELECT count(*) FROM zaimu_kyoten_shiwake_de3) > 0 THEN (SELECT MAX(serial_no) FROM zaimu_kyoten_shiwake_de3)
			WHEN (SELECT count(*) FROM zaimu_kyoten_shiwake_sias) > 0 THEN (SELECT MAX(serial_no) FROM zaimu_kyoten_shiwake_sias)
			ELSE 0 END);

-- 部門振替
ALTER TABLE bumon_furikae DROP CONSTRAINT IF EXISTS bumon_furikae_PKEY;
ALTER TABLE bumon_furikae RENAME TO bumon_furikae_old;
create table bumon_furikae (
  denpyou_id character varying(19) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , kari_kingaku_goukei numeric(15) not null
  , kashi_kingaku_goukei numeric(15) not null
  , denpyou_date date not null
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
  , teikei_shiwake_user_id character varying(30) not null
  , teikei_shiwake_pattern_no Integer
  , bumon_tsukekae_flg character varying(1) not null
  , bumon_tsukekae_moto_serial_no bigint
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint bumon_furikae_PKEY primary key (denpyou_id)
);
comment on table bumon_furikae is '振替伝票(拠点)';
comment on column bumon_furikae.denpyou_id is '伝票ID';
comment on column bumon_furikae.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column bumon_furikae.kari_kingaku_goukei is '借方金額合計';
comment on column bumon_furikae.kashi_kingaku_goukei is '貸方金額合計';
comment on column bumon_furikae.denpyou_date is '伝票日付';
comment on column bumon_furikae.hf1_cd is 'HF1コード';
comment on column bumon_furikae.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column bumon_furikae.hf2_cd is 'HF2コード';
comment on column bumon_furikae.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column bumon_furikae.hf3_cd is 'HF3コード';
comment on column bumon_furikae.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column bumon_furikae.hf4_cd is 'HF4コード';
comment on column bumon_furikae.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column bumon_furikae.hf5_cd is 'HF5コード';
comment on column bumon_furikae.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column bumon_furikae.hf6_cd is 'HF6コード';
comment on column bumon_furikae.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column bumon_furikae.hf7_cd is 'HF7コード';
comment on column bumon_furikae.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column bumon_furikae.hf8_cd is 'HF8コード';
comment on column bumon_furikae.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column bumon_furikae.hf9_cd is 'HF9コード';
comment on column bumon_furikae.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column bumon_furikae.hf10_cd is 'HF10コード';
comment on column bumon_furikae.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column bumon_furikae.bikou is '備考';
comment on column bumon_furikae.teikei_shiwake_user_id is '定型仕訳ユーザーID';
comment on column bumon_furikae.teikei_shiwake_pattern_no is '定型仕訳パターンNo';
comment on column bumon_furikae.bumon_tsukekae_flg is '部門付替フラグ';
comment on column bumon_furikae.bumon_tsukekae_moto_serial_no is '部門付替元シリアル番号';
comment on column bumon_furikae.touroku_user_id is '登録ユーザーID';
comment on column bumon_furikae.touroku_time is '登録日時';
comment on column bumon_furikae.koushin_user_id is '更新ユーザーID';
comment on column bumon_furikae.koushin_time is '更新日時';
INSERT INTO bumon_furikae
SELECT
	 denpyou_id
	,zaimu_kyoten_nyuryoku_pattern_no
	,kari_kingaku_goukei
	,kashi_kingaku_goukei
	,denpyou_date
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
	,bikou
	,teikei_shiwake_user_id
	,teikei_shiwake_pattern_no
	,'0'      --bumon_tsukekae_flg
	,0    --bumon_tsukekae_moto_serial_no
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM bumon_furikae_old;
DROP TABLE bumon_furikae_old;

-- 部門振替明細
ALTER TABLE bumon_furikae_meisai DROP CONSTRAINT IF EXISTS bumon_furikae_meisai_PKEY;
ALTER TABLE bumon_furikae_meisai RENAME TO bumon_furikae_meisai_old;
create table bumon_furikae_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , kari_tekiyou character varying(60) not null
  , kari_kingaku numeric(15) not null
  , kari_shouhizeigaku numeric(15)
  , kari_zeiritsu numeric(3) not null
  , kari_keigen_zeiritsu_kbn character varying(1) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , kari_torihikisaki_cd character varying(12) not null
  , kari_torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kari_bunri_kbn character varying(1) not null
  , kari_kobetsu_kbn character varying(1) not null
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
  , kari_project_cd character varying(12) not null
  , kari_project_name character varying(20) not null
  , kari_segment_cd character varying(8) not null
  , kari_segment_name character varying(20) not null
  , kari_taika numeric(15)
  , kari_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kari_shouhizeitaishou_kamoku_name character varying(22) not null
  , kari_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kari_shouhizeitaishou_zeiritsu numeric(3)
  , kari_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kari_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kari_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kari_heishu_cd character varying(4) not null
  , kari_rate numeric(11,5)
  , kari_gaika numeric(22,5)
  , kari_gaika_shouhizeigaku numeric(22,5)
  , kari_gaika_taika numeric(22,5)
  , kari_teikei_shiwake_edano integer
  , kari_shiwake_serial_no bigint
  , kashi_tekiyou character varying(60) not null
  , kashi_kingaku numeric(15) not null
  , kashi_shouhizeigaku numeric(15)
  , kashi_zeiritsu numeric(3) not null
  , kashi_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_torihikisaki_cd character varying(12) not null
  , kashi_torihikisaki_name_ryakushiki character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , kashi_bunri_kbn character varying(1) not null
  , kashi_kobetsu_kbn character varying(1) not null
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
  , kashi_project_cd character varying(12) not null
  , kashi_project_name character varying(20) not null
  , kashi_segment_cd character varying(8) not null
  , kashi_segment_name character varying(20) not null
  , kashi_taika numeric(15)
  , kashi_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kashi_shouhizeitaishou_kamoku_name character varying(22) not null
  , kashi_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kashi_shouhizeitaishou_zeiritsu numeric(3)
  , kashi_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kashi_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kashi_heishu_cd character varying(4) not null
  , kashi_rate numeric(11,5)
  , kashi_gaika numeric(22,5)
  , kashi_gaika_shouhizeigaku numeric(22,5)
  , kashi_gaika_taika numeric(22,5)
  , kashi_teikei_shiwake_edano integer
  , kashi_shiwake_serial_no bigint
  , gyou_kugiri character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint bumon_furikae_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table bumon_furikae_meisai is '振替伝票明細(拠点)';
comment on column bumon_furikae_meisai.denpyou_id is '伝票ID';
comment on column bumon_furikae_meisai.denpyou_edano is '伝票枝番号';
comment on column bumon_furikae_meisai.kari_tekiyou is '借方摘要';
comment on column bumon_furikae_meisai.kari_kingaku is '借方金額';
comment on column bumon_furikae_meisai.kari_shouhizeigaku is '借方消費税額';
comment on column bumon_furikae_meisai.kari_zeiritsu is '借方税率';
comment on column bumon_furikae_meisai.kari_keigen_zeiritsu_kbn is '借方軽減税率区分';
comment on column bumon_furikae_meisai.kari_kamoku_cd is '借方科目コード';
comment on column bumon_furikae_meisai.kari_kamoku_name is '借方科目名';
comment on column bumon_furikae_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column bumon_furikae_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column bumon_furikae_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column bumon_furikae_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column bumon_furikae_meisai.kari_torihikisaki_cd is '借方取引先コード';
comment on column bumon_furikae_meisai.kari_torihikisaki_name_ryakushiki is '借方取引先名（略式）';
comment on column bumon_furikae_meisai.kari_kazei_kbn is '借方課税区分';
comment on column bumon_furikae_meisai.kari_bunri_kbn is '借方分離区分';
comment on column bumon_furikae_meisai.kari_kobetsu_kbn is '借方個別区分';
comment on column bumon_furikae_meisai.kari_uf1_cd is '借方UF1コード';
comment on column bumon_furikae_meisai.kari_uf1_name_ryakushiki is '借方UF1名（略式）';
comment on column bumon_furikae_meisai.kari_uf2_cd is '借方UF2コード';
comment on column bumon_furikae_meisai.kari_uf2_name_ryakushiki is '借方UF2名（略式）';
comment on column bumon_furikae_meisai.kari_uf3_cd is '借方UF3コード';
comment on column bumon_furikae_meisai.kari_uf3_name_ryakushiki is '借方UF3名（略式）';
comment on column bumon_furikae_meisai.kari_uf4_cd is '借方UF4コード';
comment on column bumon_furikae_meisai.kari_uf4_name_ryakushiki is '借方UF4名（略式）';
comment on column bumon_furikae_meisai.kari_uf5_cd is '借方UF5コード';
comment on column bumon_furikae_meisai.kari_uf5_name_ryakushiki is '借方UF5名（略式）';
comment on column bumon_furikae_meisai.kari_uf6_cd is '借方UF6コード';
comment on column bumon_furikae_meisai.kari_uf6_name_ryakushiki is '借方UF6名（略式）';
comment on column bumon_furikae_meisai.kari_uf7_cd is '借方UF7コード';
comment on column bumon_furikae_meisai.kari_uf7_name_ryakushiki is '借方UF7名（略式）';
comment on column bumon_furikae_meisai.kari_uf8_cd is '借方UF8コード';
comment on column bumon_furikae_meisai.kari_uf8_name_ryakushiki is '借方UF8名（略式）';
comment on column bumon_furikae_meisai.kari_uf9_cd is '借方UF9コード';
comment on column bumon_furikae_meisai.kari_uf9_name_ryakushiki is '借方UF9名（略式）';
comment on column bumon_furikae_meisai.kari_uf10_cd is '借方UF10コード';
comment on column bumon_furikae_meisai.kari_uf10_name_ryakushiki is '借方UF10名（略式）';
comment on column bumon_furikae_meisai.kari_project_cd is '借方プロジェクトコード';
comment on column bumon_furikae_meisai.kari_project_name is '借方プロジェクト名';
comment on column bumon_furikae_meisai.kari_segment_cd is '借方セグメントコード';
comment on column bumon_furikae_meisai.kari_segment_name is '借方セグメント名';
comment on column bumon_furikae_meisai.kari_taika is '借方対価';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_kamoku_cd is '借方消費税対象科目コード';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_kamoku_name is '借方消費税対象科目名称';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_kazei_kbn is '借方消費税対象課税区分';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_zeiritsu is '借方消費税対象税率';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_keigen_zeiritsu_kbn is '借方消費税対象軽減税率区分';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_bunri_kbn is '借方消費税対象分離区分';
comment on column bumon_furikae_meisai.kari_shouhizeitaishou_kobetsu_kbn is '借方消費税対象個別区分';
comment on column bumon_furikae_meisai.kari_heishu_cd is '借方幣種コード';
comment on column bumon_furikae_meisai.kari_rate is '借方レート';
comment on column bumon_furikae_meisai.kari_gaika is '借方外貨金額';
comment on column bumon_furikae_meisai.kari_gaika_shouhizeigaku is '借方外貨消費税額';
comment on column bumon_furikae_meisai.kari_gaika_taika is '借方外貨対価';
comment on column bumon_furikae_meisai.kari_teikei_shiwake_edano is '借方定型仕訳枝番号';
comment on column bumon_furikae_meisai.kari_shiwake_serial_no is '借方仕訳データシリアルNo';
comment on column bumon_furikae_meisai.kashi_tekiyou is '貸方摘要';
comment on column bumon_furikae_meisai.kashi_kingaku is '貸方金額';
comment on column bumon_furikae_meisai.kashi_shouhizeigaku is '貸方消費税額';
comment on column bumon_furikae_meisai.kashi_zeiritsu is '貸方税率';
comment on column bumon_furikae_meisai.kashi_keigen_zeiritsu_kbn is '貸方軽減税率区分';
comment on column bumon_furikae_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column bumon_furikae_meisai.kashi_kamoku_name is '貸方科目名';
comment on column bumon_furikae_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column bumon_furikae_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column bumon_furikae_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column bumon_furikae_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column bumon_furikae_meisai.kashi_torihikisaki_cd is '貸方取引先コード';
comment on column bumon_furikae_meisai.kashi_torihikisaki_name_ryakushiki is '貸方取引先名（略式）';
comment on column bumon_furikae_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column bumon_furikae_meisai.kashi_bunri_kbn is '貸方分離区分';
comment on column bumon_furikae_meisai.kashi_kobetsu_kbn is '貸方個別区分';
comment on column bumon_furikae_meisai.kashi_uf1_cd is '貸方UF1コード';
comment on column bumon_furikae_meisai.kashi_uf1_name_ryakushiki is '貸方UF1名（略式）';
comment on column bumon_furikae_meisai.kashi_uf2_cd is '貸方UF2コード';
comment on column bumon_furikae_meisai.kashi_uf2_name_ryakushiki is '貸方UF2名（略式）';
comment on column bumon_furikae_meisai.kashi_uf3_cd is '貸方UF3コード';
comment on column bumon_furikae_meisai.kashi_uf3_name_ryakushiki is '貸方UF3名（略式）';
comment on column bumon_furikae_meisai.kashi_uf4_cd is '貸方UF4コード';
comment on column bumon_furikae_meisai.kashi_uf4_name_ryakushiki is '貸方UF4名（略式）';
comment on column bumon_furikae_meisai.kashi_uf5_cd is '貸方UF5コード';
comment on column bumon_furikae_meisai.kashi_uf5_name_ryakushiki is '貸方UF5名（略式）';
comment on column bumon_furikae_meisai.kashi_uf6_cd is '貸方UF6コード';
comment on column bumon_furikae_meisai.kashi_uf6_name_ryakushiki is '貸方UF6名（略式）';
comment on column bumon_furikae_meisai.kashi_uf7_cd is '貸方UF7コード';
comment on column bumon_furikae_meisai.kashi_uf7_name_ryakushiki is '貸方UF7名（略式）';
comment on column bumon_furikae_meisai.kashi_uf8_cd is '貸方UF8コード';
comment on column bumon_furikae_meisai.kashi_uf8_name_ryakushiki is '貸方UF8名（略式）';
comment on column bumon_furikae_meisai.kashi_uf9_cd is '貸方UF9コード';
comment on column bumon_furikae_meisai.kashi_uf9_name_ryakushiki is '貸方UF9名（略式）';
comment on column bumon_furikae_meisai.kashi_uf10_cd is '貸方UF10コード';
comment on column bumon_furikae_meisai.kashi_uf10_name_ryakushiki is '貸方UF10名（略式）';
comment on column bumon_furikae_meisai.kashi_project_cd is '貸方プロジェクトコード';
comment on column bumon_furikae_meisai.kashi_project_name is '貸方プロジェクト名';
comment on column bumon_furikae_meisai.kashi_segment_cd is '貸方セグメントコード';
comment on column bumon_furikae_meisai.kashi_segment_name is '貸方セグメント名';
comment on column bumon_furikae_meisai.kashi_taika is '貸方対価';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_kamoku_cd is '貸方消費税対象科目コード';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_kamoku_name is '貸方消費税対象科目名称';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_kazei_kbn is '貸方消費税対象課税区分';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_zeiritsu is '貸方消費税対象税率';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_keigen_zeiritsu_kbn is '貸方消費税対象軽減税率区分';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_bunri_kbn is '貸方消費税対象分離区分';
comment on column bumon_furikae_meisai.kashi_shouhizeitaishou_kobetsu_kbn is '貸方消費税対象個別区分';
comment on column bumon_furikae_meisai.kashi_heishu_cd is '貸方幣種コード';
comment on column bumon_furikae_meisai.kashi_rate is '貸方レート';
comment on column bumon_furikae_meisai.kashi_gaika is '貸方外貨金額';
comment on column bumon_furikae_meisai.kashi_gaika_shouhizeigaku is '貸方外貨消費税額';
comment on column bumon_furikae_meisai.kashi_gaika_taika is '貸方外貨対価';
comment on column bumon_furikae_meisai.kashi_teikei_shiwake_edano is '貸方定型仕訳枝番号';
comment on column bumon_furikae_meisai.kashi_shiwake_serial_no is '貸方仕訳データシリアルNo';
comment on column bumon_furikae_meisai.gyou_kugiri is '行区切り';
comment on column bumon_furikae_meisai.touroku_user_id is '登録ユーザーID';
comment on column bumon_furikae_meisai.touroku_time is '登録日時';
comment on column bumon_furikae_meisai.koushin_user_id is '更新ユーザーID';
comment on column bumon_furikae_meisai.koushin_time is '更新日時';
INSERT INTO bumon_furikae_meisai
SELECT
	denpyou_id
	,denpyou_edano
	,kari_tekiyou
	,kari_kingaku
	,kari_shouhizeigaku
	,kari_zeiritsu
	,kari_keigen_zeiritsu_kbn
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,kari_torihikisaki_cd
	,kari_torihikisaki_name_ryakushiki
	,kari_kazei_kbn
	,kari_bunri_kbn
	,kari_kobetsu_kbn
	,kari_uf1_cd
	,kari_uf1_name_ryakushiki
	,kari_uf2_cd
	,kari_uf2_name_ryakushiki
	,kari_uf3_cd
	,kari_uf3_name_ryakushiki
	,kari_uf4_cd
	,kari_uf4_name_ryakushiki
	,kari_uf5_cd
	,kari_uf5_name_ryakushiki
	,kari_uf6_cd
	,kari_uf6_name_ryakushiki
	,kari_uf7_cd
	,kari_uf7_name_ryakushiki
	,kari_uf8_cd
	,kari_uf8_name_ryakushiki
	,kari_uf9_cd
	,kari_uf9_name_ryakushiki
	,kari_uf10_cd
	,kari_uf10_name_ryakushiki
	,kari_project_cd
	,kari_project_name
	,kari_segment_cd
	,kari_segment_name
	,kari_taika
	,kari_shouhizeitaishou_kamoku_cd
	,kari_shouhizeitaishou_kamoku_name
	,kari_shouhizeitaishou_kazei_kbn
	,kari_shouhizeitaishou_zeiritsu
	,kari_shouhizeitaishou_keigen_zeiritsu_kbn
	,kari_shouhizeitaishou_bunri_kbn
	,kari_shouhizeitaishou_kobetsu_kbn
	,kari_heishu_cd
	,kari_rate
	,kari_gaika
	,kari_gaika_shouhizeigaku
	,kari_gaika_taika
	,0   --kari_teikei_shiwake_edano
	,0   --kari_shiwake_serial_no
	,kashi_tekiyou
	,kashi_kingaku
	,kashi_shouhizeigaku
	,kashi_zeiritsu
	,kashi_keigen_zeiritsu_kbn
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_torihikisaki_cd
	,kashi_torihikisaki_name_ryakushiki
	,kashi_kazei_kbn
	,kashi_bunri_kbn
	,kashi_kobetsu_kbn
	,kashi_uf1_cd
	,kashi_uf1_name_ryakushiki
	,kashi_uf2_cd
	,kashi_uf2_name_ryakushiki
	,kashi_uf3_cd
	,kashi_uf3_name_ryakushiki
	,kashi_uf4_cd
	,kashi_uf4_name_ryakushiki
	,kashi_uf5_cd
	,kashi_uf5_name_ryakushiki
	,kashi_uf6_cd
	,kashi_uf6_name_ryakushiki
	,kashi_uf7_cd
	,kashi_uf7_name_ryakushiki
	,kashi_uf8_cd
	,kashi_uf8_name_ryakushiki
	,kashi_uf9_cd
	,kashi_uf9_name_ryakushiki
	,kashi_uf10_cd
	,kashi_uf10_name_ryakushiki
	,kashi_project_cd
	,kashi_project_name
	,kashi_segment_cd
	,kashi_segment_name
	,kashi_taika
	,kashi_shouhizeitaishou_kamoku_cd
	,kashi_shouhizeitaishou_kamoku_name
	,kashi_shouhizeitaishou_kazei_kbn
	,kashi_shouhizeitaishou_zeiritsu
	,kashi_shouhizeitaishou_keigen_zeiritsu_kbn
	,kashi_shouhizeitaishou_bunri_kbn
	,kashi_shouhizeitaishou_kobetsu_kbn
	,kashi_heishu_cd
	,kashi_rate
	,kashi_gaika
	,kashi_gaika_shouhizeigaku
	,kashi_gaika_taika
	,0    --kashi_teikei_shiwake_edano
	,0    --kashi_shiwake_serial_no
	,gyou_kugiri
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM bumon_furikae_meisai_old;
DROP TABLE bumon_furikae_meisai_old;

--   承認済みの伝票は、借方仕訳シリアル番号と貸方仕訳シリアル番号を採番する
WITH GET_NUM AS(
	SELECT row_number() OVER (ORDER BY m.denpyou_id, m.denpyou_edano) * 2 as num, m.* 
	FROM bumon_furikae_meisai m
	INNER JOIN denpyou d ON d.denpyou_id = m.denpyou_id
	WHERE d.denpyou_joutai = '30'
)
UPDATE bumon_furikae_meisai mm
SET kari_shiwake_serial_no = (SELECT (SELECT sequence_val + h.num -1 FROM zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban) 
										FROM bumon_furikae_meisai m
										INNER JOIN GET_NUM h ON h.denpyou_id = m.denpyou_id AND h.denpyou_edano = m.denpyou_edano 
										WHERE m.denpyou_id = mm.denpyou_id AND m.denpyou_edano = mm.denpyou_edano)
	,kashi_shiwake_serial_no = (SELECT (SELECT sequence_val + h.num FROM zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban) 
										 FROM bumon_furikae_meisai m
										 INNER JOIN GET_NUM h ON h.denpyou_id = m.denpyou_id AND h.denpyou_edano = m.denpyou_edano 
										 WHERE m.denpyou_id = mm.denpyou_id AND m.denpyou_edano = mm.denpyou_edano)
WHERE mm.denpyou_id IN (SELECT denpyou_id FROM denpyou WHERE denpyou_joutai = '30');
--   採番テーブルのシーケンス番号も上記の更新に合わせて変える
UPDATE zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban 
SET sequence_val = sequence_val + (SELECT count(*) * 2 
									FROM bumon_furikae_meisai m
									INNER JOIN denpyou d ON d.denpyou_id = m.denpyou_id 
									WHERE d.denpyou_joutai = '30');


-- 振替部門設定
ALTER TABLE bumon_furikae_setting DROP CONSTRAINT IF EXISTS bumon_furikae_setting_PKEY;
ALTER TABLE bumon_furikae_setting RENAME TO bumon_furikae_setting_old;
create table bumon_furikae_setting (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , taishaku_tekiyou_flg character varying(1) not null
  , bumon_tsukekae_shiyou_flg character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint bumon_furikae_setting_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no)
);
comment on table bumon_furikae_setting is '部門振替設定';
comment on column bumon_furikae_setting.denpyou_kbn is '伝票区分';
comment on column bumon_furikae_setting.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column bumon_furikae_setting.taishaku_tekiyou_flg is '貸借摘要フラグ';
comment on column bumon_furikae_setting.bumon_tsukekae_shiyou_flg is '部門付替使用フラグ';
comment on column bumon_furikae_setting.touroku_user_id is '登録ユーザーID';
comment on column bumon_furikae_setting.touroku_time is '登録日時';
comment on column bumon_furikae_setting.koushin_user_id is '更新ユーザーID';
comment on column bumon_furikae_setting.koushin_time is '更新日時';
INSERT INTO bumon_furikae_setting
SELECT
	denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,taishaku_tekiyou_flg
	,'0'   --bumon_tsukekae_shiyou_flg
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM bumon_furikae_setting_old;
DROP TABLE bumon_furikae_setting_old;

-- 定型仕訳明細
ALTER TABLE teikei_shiwake_meisai DROP CONSTRAINT IF EXISTS teikei_shiwake_meisai_PKEY;
ALTER TABLE teikei_shiwake_meisai RENAME TO teikei_shiwake_meisai_old;
create table teikei_shiwake_meisai (
  user_id character varying(30) not null
  , teikei_shiwake_pattern_no integer not null
  , teikei_shiwake_edano integer not null
  , kari_tekiyou character varying(60) not null
  , kari_kingaku numeric(15) not null
  , kari_shouhizeigaku numeric(15)
  , kari_zeiritsu numeric(3) not null
  , kari_keigen_zeiritsu_kbn character varying(1) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , kari_torihikisaki_cd character varying(12) not null
  , kari_torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kari_bunri_kbn character varying(1) not null
  , kari_kobetsu_kbn character varying(1) not null
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
  , kari_project_cd character varying(12) not null
  , kari_project_name character varying(20) not null
  , kari_segment_cd character varying(8) not null
  , kari_segment_name character varying(20) not null
  , kari_taika numeric(15)
  , kari_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kari_shouhizeitaishou_kamoku_name character varying(22) not null
  , kari_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kari_shouhizeitaishou_zeiritsu numeric(3)
  , kari_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kari_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kari_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kari_heishu_cd character varying(1) not null
  , kari_rate numeric(11,5)
  , kari_gaika numeric(22,5)
  , kari_gaika_shouhizeigaku numeric(22,5)
  , kari_gaika_taika numeric(22,5)
  , kari_zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , kashi_tekiyou character varying(60) not null
  , kashi_kingaku numeric(15) not null
  , kashi_shouhizeigaku numeric(15)
  , kashi_zeiritsu numeric(3) not null
  , kashi_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_torihikisaki_cd character varying(12) not null
  , kashi_torihikisaki_name_ryakushiki character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , kashi_bunri_kbn character varying(1) not null
  , kashi_kobetsu_kbn character varying(1) not null
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
  , kashi_project_cd character varying(12) not null
  , kashi_project_name character varying(20) not null
  , kashi_segment_cd character varying(8) not null
  , kashi_segment_name character varying(20) not null
  , kashi_taika numeric(15)
  , kashi_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kashi_shouhizeitaishou_kamoku_name character varying(22) not null
  , kashi_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kashi_shouhizeitaishou_zeiritsu numeric(3)
  , kashi_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kashi_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kashi_heishu_cd character varying(1) not null
  , kashi_rate numeric(11,5)
  , kashi_gaika numeric(22,5)
  , kashi_gaika_shouhizeigaku numeric(22,5)
  , kashi_gaika_taika numeric(22,5)
  , kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , gyou_kugiri character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint teikei_shiwake_meisai_PKEY primary key (user_id,teikei_shiwake_pattern_no,teikei_shiwake_edano)
);
comment on table teikei_shiwake_meisai is '定型仕訳明細';
comment on column teikei_shiwake_meisai.user_id is 'ユーザーID';
comment on column teikei_shiwake_meisai.teikei_shiwake_pattern_no is '定型仕訳パターン番号';
comment on column teikei_shiwake_meisai.teikei_shiwake_edano is '定型仕訳枝番号';
comment on column teikei_shiwake_meisai.kari_tekiyou is '借方摘要';
comment on column teikei_shiwake_meisai.kari_kingaku is '借方金額';
comment on column teikei_shiwake_meisai.kari_shouhizeigaku is '借方消費税額';
comment on column teikei_shiwake_meisai.kari_zeiritsu is '借方税率';
comment on column teikei_shiwake_meisai.kari_keigen_zeiritsu_kbn is '借方軽減税率区分';
comment on column teikei_shiwake_meisai.kari_kamoku_cd is '借方科目コード';
comment on column teikei_shiwake_meisai.kari_kamoku_name is '借方科目名';
comment on column teikei_shiwake_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column teikei_shiwake_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column teikei_shiwake_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column teikei_shiwake_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column teikei_shiwake_meisai.kari_torihikisaki_cd is '借方取引先コード';
comment on column teikei_shiwake_meisai.kari_torihikisaki_name_ryakushiki is '借方取引先名（略式）';
comment on column teikei_shiwake_meisai.kari_kazei_kbn is '借方課税区分';
comment on column teikei_shiwake_meisai.kari_bunri_kbn is '借方分離区分';
comment on column teikei_shiwake_meisai.kari_kobetsu_kbn is '借方個別区分';
comment on column teikei_shiwake_meisai.kari_uf1_cd is '借方UF1コード';
comment on column teikei_shiwake_meisai.kari_uf1_name_ryakushiki is '借方UF1名（略式）';
comment on column teikei_shiwake_meisai.kari_uf2_cd is '借方UF2コード';
comment on column teikei_shiwake_meisai.kari_uf2_name_ryakushiki is '借方UF2名（略式）';
comment on column teikei_shiwake_meisai.kari_uf3_cd is '借方UF3コード';
comment on column teikei_shiwake_meisai.kari_uf3_name_ryakushiki is '借方UF3名（略式）';
comment on column teikei_shiwake_meisai.kari_uf4_cd is '借方UF4コード';
comment on column teikei_shiwake_meisai.kari_uf4_name_ryakushiki is '借方UF4名（略式）';
comment on column teikei_shiwake_meisai.kari_uf5_cd is '借方UF5コード';
comment on column teikei_shiwake_meisai.kari_uf5_name_ryakushiki is '借方UF5名（略式）';
comment on column teikei_shiwake_meisai.kari_uf6_cd is '借方UF6コード';
comment on column teikei_shiwake_meisai.kari_uf6_name_ryakushiki is '借方UF6名（略式）';
comment on column teikei_shiwake_meisai.kari_uf7_cd is '借方UF7コード';
comment on column teikei_shiwake_meisai.kari_uf7_name_ryakushiki is '借方UF7名（略式）';
comment on column teikei_shiwake_meisai.kari_uf8_cd is '借方UF8コード';
comment on column teikei_shiwake_meisai.kari_uf8_name_ryakushiki is '借方UF8名（略式）';
comment on column teikei_shiwake_meisai.kari_uf9_cd is '借方UF9コード';
comment on column teikei_shiwake_meisai.kari_uf9_name_ryakushiki is '借方UF9名（略式）';
comment on column teikei_shiwake_meisai.kari_uf10_cd is '借方UF10コード';
comment on column teikei_shiwake_meisai.kari_uf10_name_ryakushiki is '借方UF10名（略式）';
comment on column teikei_shiwake_meisai.kari_project_cd is '借方プロジェクトコード';
comment on column teikei_shiwake_meisai.kari_project_name is '借方プロジェクト名';
comment on column teikei_shiwake_meisai.kari_segment_cd is '借方セグメントコード';
comment on column teikei_shiwake_meisai.kari_segment_name is '借方セグメント名';
comment on column teikei_shiwake_meisai.kari_taika is '借方対価';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kamoku_cd is '借方消費税対象科目コード';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kamoku_name is '借方消費税対象科目名称';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kazei_kbn is '借方消費税対象課税区分';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_zeiritsu is '借方消費税対象税率';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_keigen_zeiritsu_kbn is '借方消費税対象軽減税率区分';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_bunri_kbn is '借方消費税対象分離区分';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kobetsu_kbn is '借方消費税対象個別区分';
comment on column teikei_shiwake_meisai.kari_heishu_cd is '借方幣種コード';
comment on column teikei_shiwake_meisai.kari_rate is '借方レート';
comment on column teikei_shiwake_meisai.kari_gaika is '借方外貨金額';
comment on column teikei_shiwake_meisai.kari_gaika_shouhizeigaku is '借方外貨消費税額';
comment on column teikei_shiwake_meisai.kari_gaika_taika is '借方外貨対価';
comment on column teikei_shiwake_meisai.kari_zaimu_kyoten_nyuryoku_haifu_pattern_no is '借方拠点入力配賦パターンNo';
comment on column teikei_shiwake_meisai.kashi_tekiyou is '貸方摘要';
comment on column teikei_shiwake_meisai.kashi_kingaku is '貸方金額';
comment on column teikei_shiwake_meisai.kashi_shouhizeigaku is '貸方消費税額';
comment on column teikei_shiwake_meisai.kashi_zeiritsu is '貸方税率';
comment on column teikei_shiwake_meisai.kashi_keigen_zeiritsu_kbn is '貸方軽減税率区分';
comment on column teikei_shiwake_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column teikei_shiwake_meisai.kashi_kamoku_name is '貸方科目名';
comment on column teikei_shiwake_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column teikei_shiwake_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column teikei_shiwake_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column teikei_shiwake_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column teikei_shiwake_meisai.kashi_torihikisaki_cd is '貸方取引先コード';
comment on column teikei_shiwake_meisai.kashi_torihikisaki_name_ryakushiki is '貸方取引先名（略式）';
comment on column teikei_shiwake_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column teikei_shiwake_meisai.kashi_bunri_kbn is '貸方分離区分';
comment on column teikei_shiwake_meisai.kashi_kobetsu_kbn is '貸方個別区分';
comment on column teikei_shiwake_meisai.kashi_uf1_cd is '貸方UF1コード';
comment on column teikei_shiwake_meisai.kashi_uf1_name_ryakushiki is '貸方UF1名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf2_cd is '貸方UF2コード';
comment on column teikei_shiwake_meisai.kashi_uf2_name_ryakushiki is '貸方UF2名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf3_cd is '貸方UF3コード';
comment on column teikei_shiwake_meisai.kashi_uf3_name_ryakushiki is '貸方UF3名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf4_cd is '貸方UF4コード';
comment on column teikei_shiwake_meisai.kashi_uf4_name_ryakushiki is '貸方UF4名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf5_cd is '貸方UF5コード';
comment on column teikei_shiwake_meisai.kashi_uf5_name_ryakushiki is '貸方UF5名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf6_cd is '貸方UF6コード';
comment on column teikei_shiwake_meisai.kashi_uf6_name_ryakushiki is '貸方UF6名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf7_cd is '貸方UF7コード';
comment on column teikei_shiwake_meisai.kashi_uf7_name_ryakushiki is '貸方UF7名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf8_cd is '貸方UF8コード';
comment on column teikei_shiwake_meisai.kashi_uf8_name_ryakushiki is '貸方UF8名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf9_cd is '貸方UF9コード';
comment on column teikei_shiwake_meisai.kashi_uf9_name_ryakushiki is '貸方UF9名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf10_cd is '貸方UF10コード';
comment on column teikei_shiwake_meisai.kashi_uf10_name_ryakushiki is '貸方UF10名（略式）';
comment on column teikei_shiwake_meisai.kashi_project_cd is '貸方プロジェクトコード';
comment on column teikei_shiwake_meisai.kashi_project_name is '貸方プロジェクト名';
comment on column teikei_shiwake_meisai.kashi_segment_cd is '貸方セグメントコード';
comment on column teikei_shiwake_meisai.kashi_segment_name is '貸方セグメント名';
comment on column teikei_shiwake_meisai.kashi_taika is '貸方対価';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kamoku_cd is '貸方消費税対象科目コード';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kamoku_name is '貸方消費税対象科目名称';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kazei_kbn is '貸方消費税対象課税区分';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_zeiritsu is '貸方消費税対象税率';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_keigen_zeiritsu_kbn is '貸方消費税対象軽減税率区分';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_bunri_kbn is '貸方消費税対象分離区分';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kobetsu_kbn is '貸方消費税対象個別区分';
comment on column teikei_shiwake_meisai.kashi_heishu_cd is '貸方幣種コード';
comment on column teikei_shiwake_meisai.kashi_rate is '貸方レート';
comment on column teikei_shiwake_meisai.kashi_gaika is '貸方外貨金額';
comment on column teikei_shiwake_meisai.kashi_gaika_shouhizeigaku is '貸方外貨消費税額';
comment on column teikei_shiwake_meisai.kashi_gaika_taika is '貸方外貨対価';
comment on column teikei_shiwake_meisai.kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no is '貸方拠点入力配賦パターンNo';
comment on column teikei_shiwake_meisai.gyou_kugiri is '行区切り';
comment on column teikei_shiwake_meisai.touroku_user_id is '登録ユーザーID';
comment on column teikei_shiwake_meisai.touroku_time is '登録日時';
comment on column teikei_shiwake_meisai.koushin_user_id is '更新ユーザーID';
comment on column teikei_shiwake_meisai.koushin_time is '更新日時';
INSERT INTO teikei_shiwake_meisai
SELECT
	 user_id
	,teikei_shiwake_pattern_no
	,teikei_shiwake_edano
	,kari_tekiyou
	,kari_kingaku
	,kari_shouhizeigaku
	,kari_zeiritsu
	,kari_keigen_zeiritsu_kbn
	,kari_kamoku_cd
	,kari_kamoku_name
	,kari_kamoku_edaban_cd
	,kari_kamoku_edaban_name
	,kari_futan_bumon_cd
	,kari_futan_bumon_name
	,kari_torihikisaki_cd
	,kari_torihikisaki_name_ryakushiki
	,kari_kazei_kbn
	,kari_bunri_kbn
	,kari_kobetsu_kbn
	,kari_uf1_cd
	,kari_uf1_name_ryakushiki
	,kari_uf2_cd
	,kari_uf2_name_ryakushiki
	,kari_uf3_cd
	,kari_uf3_name_ryakushiki
	,kari_uf4_cd
	,kari_uf4_name_ryakushiki
	,kari_uf5_cd
	,kari_uf5_name_ryakushiki
	,kari_uf6_cd
	,kari_uf6_name_ryakushiki
	,kari_uf7_cd
	,kari_uf7_name_ryakushiki
	,kari_uf8_cd
	,kari_uf8_name_ryakushiki
	,kari_uf9_cd
	,kari_uf9_name_ryakushiki
	,kari_uf10_cd
	,kari_uf10_name_ryakushiki
	,kari_project_cd
	,kari_project_name
	,kari_segment_cd
	,kari_segment_name
	,kari_taika
	,kari_shouhizeitaishou_kamoku_cd
	,kari_shouhizeitaishou_kamoku_name
	,kari_shouhizeitaishou_kazei_kbn
	,kari_shouhizeitaishou_zeiritsu
	,kari_shouhizeitaishou_keigen_zeiritsu_kbn
	,kari_shouhizeitaishou_bunri_kbn
	,kari_shouhizeitaishou_kobetsu_kbn
	,kari_heishu_cd
	,kari_rate
	,kari_gaika
	,kari_gaika_shouhizeigaku
	,kari_gaika_taika
	,''   --kari_zaimu_kyoten_nyuryoku_haifu_pattern_no
	,kashi_tekiyou
	,kashi_kingaku
	,kashi_shouhizeigaku
	,kashi_zeiritsu
	,kashi_keigen_zeiritsu_kbn
	,kashi_kamoku_cd
	,kashi_kamoku_name
	,kashi_kamoku_edaban_cd
	,kashi_kamoku_edaban_name
	,kashi_futan_bumon_cd
	,kashi_futan_bumon_name
	,kashi_torihikisaki_cd
	,kashi_torihikisaki_name_ryakushiki
	,kashi_kazei_kbn
	,kashi_bunri_kbn
	,kashi_kobetsu_kbn
	,kashi_uf1_cd
	,kashi_uf1_name_ryakushiki
	,kashi_uf2_cd
	,kashi_uf2_name_ryakushiki
	,kashi_uf3_cd
	,kashi_uf3_name_ryakushiki
	,kashi_uf4_cd
	,kashi_uf4_name_ryakushiki
	,kashi_uf5_cd
	,kashi_uf5_name_ryakushiki
	,kashi_uf6_cd
	,kashi_uf6_name_ryakushiki
	,kashi_uf7_cd
	,kashi_uf7_name_ryakushiki
	,kashi_uf8_cd
	,kashi_uf8_name_ryakushiki
	,kashi_uf9_cd
	,kashi_uf9_name_ryakushiki
	,kashi_uf10_cd
	,kashi_uf10_name_ryakushiki
	,kashi_project_cd
	,kashi_project_name
	,kashi_segment_cd
	,kashi_segment_name
	,kashi_taika
	,kashi_shouhizeitaishou_kamoku_cd
	,kashi_shouhizeitaishou_kamoku_name
	,kashi_shouhizeitaishou_kazei_kbn
	,kashi_shouhizeitaishou_zeiritsu
	,kashi_shouhizeitaishou_keigen_zeiritsu_kbn
	,kashi_shouhizeitaishou_bunri_kbn
	,kashi_shouhizeitaishou_kobetsu_kbn
	,kashi_heishu_cd
	,kashi_rate
	,kashi_gaika
	,kashi_gaika_shouhizeigaku
	,kashi_gaika_taika
	,''   --kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no
	,gyou_kugiri
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM teikei_shiwake_meisai_old;
DROP TABLE teikei_shiwake_meisai_old;

-- 部門付替制御
create table bumon_tsukekae_control (
  serial_no bigint not null
  , bumon_tsukekae_taishou_flg character varying(1) not null
  , bumon_tsukekae_moto_kbn character varying(1) not null
  , jyogai_flg character varying(1) not null
  , constraint bumon_tsukekae_control_PKEY primary key (serial_no)
);
comment on table bumon_tsukekae_control is '拠点部門付替制御';
comment on column bumon_tsukekae_control.serial_no is 'シリアル番号';
comment on column bumon_tsukekae_control.bumon_tsukekae_taishou_flg is '部門付替対象フラグ';
comment on column bumon_tsukekae_control.bumon_tsukekae_moto_kbn is '部門付替元区分';
comment on column bumon_tsukekae_control.jyogai_flg is '除外フラグ';

-- 拠点入力者一覧
create table zaimu_kyoten_nyuryoku_nyuryokusha (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , user_id character varying(30) not null
  , bumon_tsukekae_kengen_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp(6) without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp(6) without time zone not null
  , constraint zaimu_kyoten_nyuryoku_nyuryokusha_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,user_id)
);

comment on table zaimu_kyoten_nyuryoku_nyuryokusha is '拠点入力者';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.yuukou_kigen_from is '有効期限開始日';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.yuukou_kigen_to is '有効期限終了日';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.user_id is 'ユーザーID';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.bumon_tsukekae_kengen_flg is '部門付替権限フラグ';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_nyuryokusha.koushin_time is '更新日時';

INSERT INTO zaimu_kyoten_nyuryoku_nyuryokusha(
	denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,yuukou_kigen_from
	,yuukou_kigen_to
	,user_id
	,bumon_tsukekae_kengen_flg
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
)SELECT
	 denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,'1999-01-01' --yuukou_kigen_from
	,'9999-12-31' --yuukou_kigen_to
	,user_id
	,bumon_tsukekae_kengen_flg
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM zaimu_kyoten_nyuryoku_user_info
WHERE nyuryoku_flg = '1';

-- 期間をパターンのそれに合わせる
UPDATE zaimu_kyoten_nyuryoku_nyuryokusha AS nyuryokusha 
SET
  yuukou_kigen_from = ichiran.yuukou_kigen_from
  , yuukou_kigen_to = ichiran.yuukou_kigen_to 
FROM
  zaimu_kyoten_nyuryoku_ichiran AS ichiran 
WHERE
  nyuryokusha.denpyou_kbn = ichiran.denpyou_kbn 
  AND nyuryokusha.zaimu_kyoten_nyuryoku_pattern_no = ichiran.zaimu_kyoten_nyuryoku_pattern_no;

-- 財務拠点入力ユーザーカラム削除
ALTER TABLE zaimu_kyoten_nyuryoku_user_info DROP COLUMN bumon_tsukekae_kengen_flg;
ALTER TABLE zaimu_kyoten_nyuryoku_user_info DROP COLUMN nyuryoku_flg;

-- 財務拠点入力科目セキュリティ
ALTER TABLE zaimu_kyoten_nyuryoku_kamoku_security DROP CONSTRAINT IF EXISTS zaimu_kyoten_nyuryoku_kamoku_security_PKEY;
ALTER TABLE zaimu_kyoten_nyuryoku_kamoku_security RENAME TO zaimu_kyoten_nyuryoku_kamoku_security_old;
create table zaimu_kyoten_nyuryoku_kamoku_security (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , kamoku_gaibu_cd character varying(6) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_kamoku_security_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,kamoku_gaibu_cd)
);

comment on table zaimu_kyoten_nyuryoku_kamoku_security is '拠点入力科目セキュリティ';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.yuukou_kigen_from is '有効期限開始日';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.yuukou_kigen_to is '有効期限終了日';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.kamoku_gaibu_cd is '科目外部コード';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.koushin_time is '更新日時';

INSERT INTO zaimu_kyoten_nyuryoku_kamoku_security(
	denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,yuukou_kigen_from
	,yuukou_kigen_to
	,kamoku_gaibu_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
)SELECT
	 denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,'1999-01-01' --yuukou_kigen_from
	,'9999-12-31' --yuukou_kigen_to
	,kamoku_gaibu_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM zaimu_kyoten_nyuryoku_kamoku_security_old;
DROP TABLE zaimu_kyoten_nyuryoku_kamoku_security_old;

-- 期間をパターンのそれに合わせる
UPDATE zaimu_kyoten_nyuryoku_kamoku_security AS kamoku 
SET
  yuukou_kigen_from = ichiran.yuukou_kigen_from
  , yuukou_kigen_to = ichiran.yuukou_kigen_to 
FROM
  zaimu_kyoten_nyuryoku_ichiran AS ichiran 
WHERE
  kamoku.denpyou_kbn = ichiran.denpyou_kbn 
  AND kamoku.zaimu_kyoten_nyuryoku_pattern_no = ichiran.zaimu_kyoten_nyuryoku_pattern_no;

-- 財務拠点入力部門セキュリティ
ALTER TABLE zaimu_kyoten_nyuryoku_bumon_security DROP CONSTRAINT IF EXISTS zaimu_kyoten_nyuryoku_bumon_security_PKEY;
ALTER TABLE zaimu_kyoten_nyuryoku_bumon_security RENAME TO zaimu_kyoten_nyuryoku_bumon_security_old;
create table zaimu_kyoten_nyuryoku_bumon_security (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , futan_bumon_cd character varying(8) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_bumon_security_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,futan_bumon_cd)
);

comment on table zaimu_kyoten_nyuryoku_bumon_security is '拠点入力部門セキュリティ';
comment on column zaimu_kyoten_nyuryoku_bumon_security.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_bumon_security.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_bumon_security.yuukou_kigen_from is '有効期限開始日';
comment on column zaimu_kyoten_nyuryoku_bumon_security.yuukou_kigen_to is '有効期限終了日';
comment on column zaimu_kyoten_nyuryoku_bumon_security.futan_bumon_cd is '負担部門コード';
comment on column zaimu_kyoten_nyuryoku_bumon_security.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_bumon_security.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_bumon_security.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_bumon_security.koushin_time is '更新日時';

INSERT INTO zaimu_kyoten_nyuryoku_bumon_security(
	denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,yuukou_kigen_from
	,yuukou_kigen_to
	,futan_bumon_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
)SELECT
	 denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,'1999-01-01' --yuukou_kigen_from
	,'9999-12-31' --yuukou_kigen_to
	,futan_bumon_cd
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM zaimu_kyoten_nyuryoku_bumon_security_old;
DROP TABLE zaimu_kyoten_nyuryoku_bumon_security_old;

-- 期間をパターンのそれに合わせる
UPDATE zaimu_kyoten_nyuryoku_bumon_security AS bumon 
SET
  yuukou_kigen_from = ichiran.yuukou_kigen_from
  , yuukou_kigen_to = ichiran.yuukou_kigen_to 
FROM
  zaimu_kyoten_nyuryoku_ichiran AS ichiran 
WHERE
  bumon.denpyou_kbn = ichiran.denpyou_kbn 
  AND bumon.zaimu_kyoten_nyuryoku_pattern_no = ichiran.zaimu_kyoten_nyuryoku_pattern_no;

--拠点承認ルート
ALTER TABLE zaimu_kyoten_nyuryoku_shounin_route DROP CONSTRAINT IF EXISTS zaimu_kyoten_nyuryoku_shounin_route_PKEY;
ALTER TABLE zaimu_kyoten_nyuryoku_shounin_route RENAME TO zaimu_kyoten_nyuryoku_shounin_route_old;
create table zaimu_kyoten_nyuryoku_shounin_route (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , edano integer not null
  , user_id character varying(30) not null
  , dairi_shounin_user_id character varying(30) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_shounin_route_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,edano)
);

comment on table zaimu_kyoten_nyuryoku_shounin_route is '拠点入力承認ルート';
comment on column zaimu_kyoten_nyuryoku_shounin_route.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_shounin_route.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_shounin_route.yuukou_kigen_from is '有効期限開始日';
comment on column zaimu_kyoten_nyuryoku_shounin_route.yuukou_kigen_to is '有効期限終了日';
comment on column zaimu_kyoten_nyuryoku_shounin_route.edano is '枝番号';
comment on column zaimu_kyoten_nyuryoku_shounin_route.user_id is 'ユーザーID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.dairi_shounin_user_id is '代理承認者ユーザーID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_shounin_route.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.koushin_time is '更新日時';

INSERT INTO zaimu_kyoten_nyuryoku_shounin_route(
	denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,yuukou_kigen_from
	,yuukou_kigen_to
	,edano
	,user_id
	,dairi_shounin_user_id
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
)SELECT
	 denpyou_kbn
	,zaimu_kyoten_nyuryoku_pattern_no
	,'1999-01-01' --yuukou_kigen_from
	,'9999-12-31' --yuukou_kigen_to
	,edano
	,user_id
	,dairi_shounin_user_id
	,touroku_user_id
	,touroku_time
	,koushin_user_id
	,koushin_time
FROM zaimu_kyoten_nyuryoku_shounin_route_old;
DROP TABLE zaimu_kyoten_nyuryoku_shounin_route_old;

-- 期間をパターンのそれに合わせる
UPDATE zaimu_kyoten_nyuryoku_shounin_route AS shounin 
SET
  yuukou_kigen_from = ichiran.yuukou_kigen_from
  , yuukou_kigen_to = ichiran.yuukou_kigen_to 
FROM
  zaimu_kyoten_nyuryoku_ichiran AS ichiran 
WHERE
  shounin.denpyou_kbn = ichiran.denpyou_kbn 
  AND shounin.zaimu_kyoten_nyuryoku_pattern_no = ichiran.zaimu_kyoten_nyuryoku_pattern_no;

-- 幣種コードのデータ長が足りなかった。
ALTER TABLE teikei_shiwake_meisai DROP CONSTRAINT IF EXISTS teikei_shiwake_meisai_PKEY;
ALTER TABLE teikei_shiwake_meisai RENAME TO teikei_shiwake_meisai_old;
create table teikei_shiwake_meisai (
  user_id character varying(30) not null
  , teikei_shiwake_pattern_no integer not null
  , teikei_shiwake_edano integer not null
  , kari_tekiyou character varying(60) not null
  , kari_kingaku numeric(15) not null
  , kari_shouhizeigaku numeric(15)
  , kari_zeiritsu numeric(3) not null
  , kari_keigen_zeiritsu_kbn character varying(1) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , kari_torihikisaki_cd character varying(12) not null
  , kari_torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kari_bunri_kbn character varying(1) not null
  , kari_kobetsu_kbn character varying(1) not null
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
  , kari_project_cd character varying(12) not null
  , kari_project_name character varying(20) not null
  , kari_segment_cd character varying(8) not null
  , kari_segment_name character varying(20) not null
  , kari_taika numeric(15)
  , kari_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kari_shouhizeitaishou_kamoku_name character varying(22) not null
  , kari_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kari_shouhizeitaishou_zeiritsu numeric(3)
  , kari_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kari_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kari_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kari_heishu_cd character varying(4) not null
  , kari_rate numeric(11,5)
  , kari_gaika numeric(22,5)
  , kari_gaika_shouhizeigaku numeric(22,5)
  , kari_gaika_taika numeric(22,5)
  , kari_zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , kashi_tekiyou character varying(60) not null
  , kashi_kingaku numeric(15) not null
  , kashi_shouhizeigaku numeric(15)
  , kashi_zeiritsu numeric(3) not null
  , kashi_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_torihikisaki_cd character varying(12) not null
  , kashi_torihikisaki_name_ryakushiki character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , kashi_bunri_kbn character varying(1) not null
  , kashi_kobetsu_kbn character varying(1) not null
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
  , kashi_project_cd character varying(12) not null
  , kashi_project_name character varying(20) not null
  , kashi_segment_cd character varying(8) not null
  , kashi_segment_name character varying(20) not null
  , kashi_taika numeric(15)
  , kashi_shouhizeitaishou_kamoku_cd character varying(6) not null
  , kashi_shouhizeitaishou_kamoku_name character varying(22) not null
  , kashi_shouhizeitaishou_kazei_kbn character varying(3) not null
  , kashi_shouhizeitaishou_zeiritsu numeric(3)
  , kashi_shouhizeitaishou_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_shouhizeitaishou_bunri_kbn character varying(1) not null
  , kashi_shouhizeitaishou_kobetsu_kbn character varying(1) not null
  , kashi_heishu_cd character varying(4) not null
  , kashi_rate numeric(11,5)
  , kashi_gaika numeric(22,5)
  , kashi_gaika_shouhizeigaku numeric(22,5)
  , kashi_gaika_taika numeric(22,5)
  , kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , gyou_kugiri character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint teikei_shiwake_meisai_PKEY primary key (user_id,teikei_shiwake_pattern_no,teikei_shiwake_edano)
);
comment on table teikei_shiwake_meisai is '定型仕訳明細';
comment on column teikei_shiwake_meisai.user_id is 'ユーザーID';
comment on column teikei_shiwake_meisai.teikei_shiwake_pattern_no is '定型仕訳パターン番号';
comment on column teikei_shiwake_meisai.teikei_shiwake_edano is '定型仕訳枝番号';
comment on column teikei_shiwake_meisai.kari_tekiyou is '借方摘要';
comment on column teikei_shiwake_meisai.kari_kingaku is '借方金額';
comment on column teikei_shiwake_meisai.kari_shouhizeigaku is '借方消費税額';
comment on column teikei_shiwake_meisai.kari_zeiritsu is '借方税率';
comment on column teikei_shiwake_meisai.kari_keigen_zeiritsu_kbn is '借方軽減税率区分';
comment on column teikei_shiwake_meisai.kari_kamoku_cd is '借方科目コード';
comment on column teikei_shiwake_meisai.kari_kamoku_name is '借方科目名';
comment on column teikei_shiwake_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column teikei_shiwake_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column teikei_shiwake_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column teikei_shiwake_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column teikei_shiwake_meisai.kari_torihikisaki_cd is '借方取引先コード';
comment on column teikei_shiwake_meisai.kari_torihikisaki_name_ryakushiki is '借方取引先名（略式）';
comment on column teikei_shiwake_meisai.kari_kazei_kbn is '借方課税区分';
comment on column teikei_shiwake_meisai.kari_bunri_kbn is '借方分離区分';
comment on column teikei_shiwake_meisai.kari_kobetsu_kbn is '借方個別区分';
comment on column teikei_shiwake_meisai.kari_uf1_cd is '借方UF1コード';
comment on column teikei_shiwake_meisai.kari_uf1_name_ryakushiki is '借方UF1名（略式）';
comment on column teikei_shiwake_meisai.kari_uf2_cd is '借方UF2コード';
comment on column teikei_shiwake_meisai.kari_uf2_name_ryakushiki is '借方UF2名（略式）';
comment on column teikei_shiwake_meisai.kari_uf3_cd is '借方UF3コード';
comment on column teikei_shiwake_meisai.kari_uf3_name_ryakushiki is '借方UF3名（略式）';
comment on column teikei_shiwake_meisai.kari_uf4_cd is '借方UF4コード';
comment on column teikei_shiwake_meisai.kari_uf4_name_ryakushiki is '借方UF4名（略式）';
comment on column teikei_shiwake_meisai.kari_uf5_cd is '借方UF5コード';
comment on column teikei_shiwake_meisai.kari_uf5_name_ryakushiki is '借方UF5名（略式）';
comment on column teikei_shiwake_meisai.kari_uf6_cd is '借方UF6コード';
comment on column teikei_shiwake_meisai.kari_uf6_name_ryakushiki is '借方UF6名（略式）';
comment on column teikei_shiwake_meisai.kari_uf7_cd is '借方UF7コード';
comment on column teikei_shiwake_meisai.kari_uf7_name_ryakushiki is '借方UF7名（略式）';
comment on column teikei_shiwake_meisai.kari_uf8_cd is '借方UF8コード';
comment on column teikei_shiwake_meisai.kari_uf8_name_ryakushiki is '借方UF8名（略式）';
comment on column teikei_shiwake_meisai.kari_uf9_cd is '借方UF9コード';
comment on column teikei_shiwake_meisai.kari_uf9_name_ryakushiki is '借方UF9名（略式）';
comment on column teikei_shiwake_meisai.kari_uf10_cd is '借方UF10コード';
comment on column teikei_shiwake_meisai.kari_uf10_name_ryakushiki is '借方UF10名（略式）';
comment on column teikei_shiwake_meisai.kari_project_cd is '借方プロジェクトコード';
comment on column teikei_shiwake_meisai.kari_project_name is '借方プロジェクト名';
comment on column teikei_shiwake_meisai.kari_segment_cd is '借方セグメントコード';
comment on column teikei_shiwake_meisai.kari_segment_name is '借方セグメント名';
comment on column teikei_shiwake_meisai.kari_taika is '借方対価';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kamoku_cd is '借方消費税対象科目コード';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kamoku_name is '借方消費税対象科目名称';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kazei_kbn is '借方消費税対象課税区分';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_zeiritsu is '借方消費税対象税率';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_keigen_zeiritsu_kbn is '借方消費税対象軽減税率区分';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_bunri_kbn is '借方消費税対象分離区分';
comment on column teikei_shiwake_meisai.kari_shouhizeitaishou_kobetsu_kbn is '借方消費税対象個別区分';
comment on column teikei_shiwake_meisai.kari_heishu_cd is '借方幣種コード';
comment on column teikei_shiwake_meisai.kari_rate is '借方レート';
comment on column teikei_shiwake_meisai.kari_gaika is '借方外貨金額';
comment on column teikei_shiwake_meisai.kari_gaika_shouhizeigaku is '借方外貨消費税額';
comment on column teikei_shiwake_meisai.kari_gaika_taika is '借方外貨対価';
comment on column teikei_shiwake_meisai.kari_zaimu_kyoten_nyuryoku_haifu_pattern_no is '借方拠点入力配賦パターンNo';
comment on column teikei_shiwake_meisai.kashi_tekiyou is '貸方摘要';
comment on column teikei_shiwake_meisai.kashi_kingaku is '貸方金額';
comment on column teikei_shiwake_meisai.kashi_shouhizeigaku is '貸方消費税額';
comment on column teikei_shiwake_meisai.kashi_zeiritsu is '貸方税率';
comment on column teikei_shiwake_meisai.kashi_keigen_zeiritsu_kbn is '貸方軽減税率区分';
comment on column teikei_shiwake_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column teikei_shiwake_meisai.kashi_kamoku_name is '貸方科目名';
comment on column teikei_shiwake_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column teikei_shiwake_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column teikei_shiwake_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column teikei_shiwake_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column teikei_shiwake_meisai.kashi_torihikisaki_cd is '貸方取引先コード';
comment on column teikei_shiwake_meisai.kashi_torihikisaki_name_ryakushiki is '貸方取引先名（略式）';
comment on column teikei_shiwake_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column teikei_shiwake_meisai.kashi_bunri_kbn is '貸方分離区分';
comment on column teikei_shiwake_meisai.kashi_kobetsu_kbn is '貸方個別区分';
comment on column teikei_shiwake_meisai.kashi_uf1_cd is '貸方UF1コード';
comment on column teikei_shiwake_meisai.kashi_uf1_name_ryakushiki is '貸方UF1名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf2_cd is '貸方UF2コード';
comment on column teikei_shiwake_meisai.kashi_uf2_name_ryakushiki is '貸方UF2名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf3_cd is '貸方UF3コード';
comment on column teikei_shiwake_meisai.kashi_uf3_name_ryakushiki is '貸方UF3名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf4_cd is '貸方UF4コード';
comment on column teikei_shiwake_meisai.kashi_uf4_name_ryakushiki is '貸方UF4名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf5_cd is '貸方UF5コード';
comment on column teikei_shiwake_meisai.kashi_uf5_name_ryakushiki is '貸方UF5名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf6_cd is '貸方UF6コード';
comment on column teikei_shiwake_meisai.kashi_uf6_name_ryakushiki is '貸方UF6名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf7_cd is '貸方UF7コード';
comment on column teikei_shiwake_meisai.kashi_uf7_name_ryakushiki is '貸方UF7名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf8_cd is '貸方UF8コード';
comment on column teikei_shiwake_meisai.kashi_uf8_name_ryakushiki is '貸方UF8名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf9_cd is '貸方UF9コード';
comment on column teikei_shiwake_meisai.kashi_uf9_name_ryakushiki is '貸方UF9名（略式）';
comment on column teikei_shiwake_meisai.kashi_uf10_cd is '貸方UF10コード';
comment on column teikei_shiwake_meisai.kashi_uf10_name_ryakushiki is '貸方UF10名（略式）';
comment on column teikei_shiwake_meisai.kashi_project_cd is '貸方プロジェクトコード';
comment on column teikei_shiwake_meisai.kashi_project_name is '貸方プロジェクト名';
comment on column teikei_shiwake_meisai.kashi_segment_cd is '貸方セグメントコード';
comment on column teikei_shiwake_meisai.kashi_segment_name is '貸方セグメント名';
comment on column teikei_shiwake_meisai.kashi_taika is '貸方対価';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kamoku_cd is '貸方消費税対象科目コード';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kamoku_name is '貸方消費税対象科目名称';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kazei_kbn is '貸方消費税対象課税区分';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_zeiritsu is '貸方消費税対象税率';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_keigen_zeiritsu_kbn is '貸方消費税対象軽減税率区分';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_bunri_kbn is '貸方消費税対象分離区分';
comment on column teikei_shiwake_meisai.kashi_shouhizeitaishou_kobetsu_kbn is '貸方消費税対象個別区分';
comment on column teikei_shiwake_meisai.kashi_heishu_cd is '貸方幣種コード';
comment on column teikei_shiwake_meisai.kashi_rate is '貸方レート';
comment on column teikei_shiwake_meisai.kashi_gaika is '貸方外貨金額';
comment on column teikei_shiwake_meisai.kashi_gaika_shouhizeigaku is '貸方外貨消費税額';
comment on column teikei_shiwake_meisai.kashi_gaika_taika is '貸方外貨対価';
comment on column teikei_shiwake_meisai.kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no is '貸方拠点入力配賦パターンNo';
comment on column teikei_shiwake_meisai.gyou_kugiri is '行区切り';
comment on column teikei_shiwake_meisai.touroku_user_id is '登録ユーザーID';
comment on column teikei_shiwake_meisai.touroku_time is '登録日時';
comment on column teikei_shiwake_meisai.koushin_user_id is '更新ユーザーID';
comment on column teikei_shiwake_meisai.koushin_time is '更新日時';
INSERT INTO teikei_shiwake_meisai(
		 user_id
		,teikei_shiwake_pattern_no
		,teikei_shiwake_edano
		,kari_tekiyou
		,kari_kingaku
		,kari_shouhizeigaku
		,kari_zeiritsu
		,kari_keigen_zeiritsu_kbn
		,kari_kamoku_cd
		,kari_kamoku_name
		,kari_kamoku_edaban_cd
		,kari_kamoku_edaban_name
		,kari_futan_bumon_cd
		,kari_futan_bumon_name
		,kari_torihikisaki_cd
		,kari_torihikisaki_name_ryakushiki
		,kari_kazei_kbn
		,kari_bunri_kbn
		,kari_kobetsu_kbn
		,kari_uf1_cd
		,kari_uf1_name_ryakushiki
		,kari_uf2_cd
		,kari_uf2_name_ryakushiki
		,kari_uf3_cd
		,kari_uf3_name_ryakushiki
		,kari_uf4_cd
		,kari_uf4_name_ryakushiki
		,kari_uf5_cd
		,kari_uf5_name_ryakushiki
		,kari_uf6_cd
		,kari_uf6_name_ryakushiki
		,kari_uf7_cd
		,kari_uf7_name_ryakushiki
		,kari_uf8_cd
		,kari_uf8_name_ryakushiki
		,kari_uf9_cd
		,kari_uf9_name_ryakushiki
		,kari_uf10_cd
		,kari_uf10_name_ryakushiki
		,kari_project_cd
		,kari_project_name
		,kari_segment_cd
		,kari_segment_name
		,kari_taika
		,kari_shouhizeitaishou_kamoku_cd
		,kari_shouhizeitaishou_kamoku_name
		,kari_shouhizeitaishou_kazei_kbn
		,kari_shouhizeitaishou_zeiritsu
		,kari_shouhizeitaishou_keigen_zeiritsu_kbn
		,kari_shouhizeitaishou_bunri_kbn
		,kari_shouhizeitaishou_kobetsu_kbn
		,kari_heishu_cd
		,kari_rate
		,kari_gaika
		,kari_gaika_shouhizeigaku
		,kari_gaika_taika
		,kari_zaimu_kyoten_nyuryoku_haifu_pattern_no
		,kashi_tekiyou
		,kashi_kingaku
		,kashi_shouhizeigaku
		,kashi_zeiritsu
		,kashi_keigen_zeiritsu_kbn
		,kashi_kamoku_cd
		,kashi_kamoku_name
		,kashi_kamoku_edaban_cd
		,kashi_kamoku_edaban_name
		,kashi_futan_bumon_cd
		,kashi_futan_bumon_name
		,kashi_torihikisaki_cd
		,kashi_torihikisaki_name_ryakushiki
		,kashi_kazei_kbn
		,kashi_bunri_kbn
		,kashi_kobetsu_kbn
		,kashi_uf1_cd
		,kashi_uf1_name_ryakushiki
		,kashi_uf2_cd
		,kashi_uf2_name_ryakushiki
		,kashi_uf3_cd
		,kashi_uf3_name_ryakushiki
		,kashi_uf4_cd
		,kashi_uf4_name_ryakushiki
		,kashi_uf5_cd
		,kashi_uf5_name_ryakushiki
		,kashi_uf6_cd
		,kashi_uf6_name_ryakushiki
		,kashi_uf7_cd
		,kashi_uf7_name_ryakushiki
		,kashi_uf8_cd
		,kashi_uf8_name_ryakushiki
		,kashi_uf9_cd
		,kashi_uf9_name_ryakushiki
		,kashi_uf10_cd
		,kashi_uf10_name_ryakushiki
		,kashi_project_cd
		,kashi_project_name
		,kashi_segment_cd
		,kashi_segment_name
		,kashi_taika
		,kashi_shouhizeitaishou_kamoku_cd
		,kashi_shouhizeitaishou_kamoku_name
		,kashi_shouhizeitaishou_kazei_kbn
		,kashi_shouhizeitaishou_zeiritsu
		,kashi_shouhizeitaishou_keigen_zeiritsu_kbn
		,kashi_shouhizeitaishou_bunri_kbn
		,kashi_shouhizeitaishou_kobetsu_kbn
		,kashi_heishu_cd
		,kashi_rate
		,kashi_gaika
		,kashi_gaika_shouhizeigaku
		,kashi_gaika_taika
		,kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no
		,gyou_kugiri
		,touroku_user_id
		,touroku_time
		,koushin_user_id
		,koushin_time
)SELECT
		 user_id
		,teikei_shiwake_pattern_no
		,teikei_shiwake_edano
		,kari_tekiyou
		,kari_kingaku
		,kari_shouhizeigaku
		,kari_zeiritsu
		,kari_keigen_zeiritsu_kbn
		,kari_kamoku_cd
		,kari_kamoku_name
		,kari_kamoku_edaban_cd
		,kari_kamoku_edaban_name
		,kari_futan_bumon_cd
		,kari_futan_bumon_name
		,kari_torihikisaki_cd
		,kari_torihikisaki_name_ryakushiki
		,kari_kazei_kbn
		,kari_bunri_kbn
		,kari_kobetsu_kbn
		,kari_uf1_cd
		,kari_uf1_name_ryakushiki
		,kari_uf2_cd
		,kari_uf2_name_ryakushiki
		,kari_uf3_cd
		,kari_uf3_name_ryakushiki
		,kari_uf4_cd
		,kari_uf4_name_ryakushiki
		,kari_uf5_cd
		,kari_uf5_name_ryakushiki
		,kari_uf6_cd
		,kari_uf6_name_ryakushiki
		,kari_uf7_cd
		,kari_uf7_name_ryakushiki
		,kari_uf8_cd
		,kari_uf8_name_ryakushiki
		,kari_uf9_cd
		,kari_uf9_name_ryakushiki
		,kari_uf10_cd
		,kari_uf10_name_ryakushiki
		,kari_project_cd
		,kari_project_name
		,kari_segment_cd
		,kari_segment_name
		,kari_taika
		,kari_shouhizeitaishou_kamoku_cd
		,kari_shouhizeitaishou_kamoku_name
		,kari_shouhizeitaishou_kazei_kbn
		,kari_shouhizeitaishou_zeiritsu
		,kari_shouhizeitaishou_keigen_zeiritsu_kbn
		,kari_shouhizeitaishou_bunri_kbn
		,kari_shouhizeitaishou_kobetsu_kbn
		,kari_heishu_cd
		,kari_rate
		,kari_gaika
		,kari_gaika_shouhizeigaku
		,kari_gaika_taika
		,kari_zaimu_kyoten_nyuryoku_haifu_pattern_no
		,kashi_tekiyou
		,kashi_kingaku
		,kashi_shouhizeigaku
		,kashi_zeiritsu
		,kashi_keigen_zeiritsu_kbn
		,kashi_kamoku_cd
		,kashi_kamoku_name
		,kashi_kamoku_edaban_cd
		,kashi_kamoku_edaban_name
		,kashi_futan_bumon_cd
		,kashi_futan_bumon_name
		,kashi_torihikisaki_cd
		,kashi_torihikisaki_name_ryakushiki
		,kashi_kazei_kbn
		,kashi_bunri_kbn
		,kashi_kobetsu_kbn
		,kashi_uf1_cd
		,kashi_uf1_name_ryakushiki
		,kashi_uf2_cd
		,kashi_uf2_name_ryakushiki
		,kashi_uf3_cd
		,kashi_uf3_name_ryakushiki
		,kashi_uf4_cd
		,kashi_uf4_name_ryakushiki
		,kashi_uf5_cd
		,kashi_uf5_name_ryakushiki
		,kashi_uf6_cd
		,kashi_uf6_name_ryakushiki
		,kashi_uf7_cd
		,kashi_uf7_name_ryakushiki
		,kashi_uf8_cd
		,kashi_uf8_name_ryakushiki
		,kashi_uf9_cd
		,kashi_uf9_name_ryakushiki
		,kashi_uf10_cd
		,kashi_uf10_name_ryakushiki
		,kashi_project_cd
		,kashi_project_name
		,kashi_segment_cd
		,kashi_segment_name
		,kashi_taika
		,kashi_shouhizeitaishou_kamoku_cd
		,kashi_shouhizeitaishou_kamoku_name
		,kashi_shouhizeitaishou_kazei_kbn
		,kashi_shouhizeitaishou_zeiritsu
		,kashi_shouhizeitaishou_keigen_zeiritsu_kbn
		,kashi_shouhizeitaishou_bunri_kbn
		,kashi_shouhizeitaishou_kobetsu_kbn
		,kashi_heishu_cd
		,kashi_rate
		,kashi_gaika
		,kashi_gaika_shouhizeigaku
		,kashi_gaika_taika
		,kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no
		,gyou_kugiri
		,touroku_user_id
		,touroku_time
		,koushin_user_id
		,koushin_time
FROM teikei_shiwake_meisai_old;
DROP TABLE teikei_shiwake_meisai_old;

--承認ルートに部門を追加
ALTER TABLE shounin_route_kyoten RENAME TO shounin_route_kyoten_tmp;
create table shounin_route_kyoten (
  denpyou_id character varying(19) not null
  , edano integer not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , bumon_full_name character varying not null
  , dairi_shounin_user_id character varying(30) not null
  , dairi_shounin_user_full_name character varying(50) not null
  , genzai_flg character varying(1) not null
  , joukyou_edano integer
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , PRIMARY KEY (denpyou_id,edano)
);
comment on table shounin_route_kyoten is '承認ルート(拠点)';
comment on column shounin_route_kyoten.denpyou_id is '伝票ID';
comment on column shounin_route_kyoten.edano is '枝番号';
comment on column shounin_route_kyoten.user_id is 'ユーザーID';
comment on column shounin_route_kyoten.user_full_name is 'ユーザーフル名';
comment on column shounin_route_kyoten.bumon_full_name is '部門フル名';
comment on column shounin_route_kyoten.dairi_shounin_user_id is '代理承認者ユーザーID';
comment on column shounin_route_kyoten.dairi_shounin_user_full_name is '代理承認者ユーザーフル名';
comment on column shounin_route_kyoten.genzai_flg is '現在フラグ';
comment on column shounin_route_kyoten.joukyou_edano is '承認状況枝番';
comment on column shounin_route_kyoten.touroku_user_id is '登録ユーザーID';
comment on column shounin_route_kyoten.touroku_time is '登録日時';
comment on column shounin_route_kyoten.koushin_user_id is '更新ユーザーID';
comment on column shounin_route_kyoten.koushin_time is '更新日時';
INSERT INTO shounin_route_kyoten
SELECT
  denpyou_id
  , edano 
  , user_id 
  , user_full_name 
  , '' --bumon_full_name 
  , dairi_shounin_user_id 
  , dairi_shounin_user_full_name 
  , genzai_flg 
  , joukyou_edano 
  , touroku_user_id 
  , touroku_time 
  , koushin_user_id 
  , koushin_time 
FROM shounin_route_kyoten_tmp;
DROP table shounin_route_kyoten_tmp CASCADE;

--テーブル和名変更(連絡票No.835より)
comment on table denpyou_ichiran_kyoten is '伝票一覧(拠点)';
comment on table denpyou_shubetsu_ichiran_kyoten is '伝票種別一覧(拠点)';
comment on table shounin_joukyou_kyoten is '承認状況(拠点)';
comment on table shounin_route_kyoten is '承認ルート(拠点)';
comment on table zaimu_kyoten_nyuryoku_ichiran is '拠点入力一覧';
comment on table zaimu_kyoten_nyuryoku_shounin_route is '拠点入力承認ルート';
comment on table zaimu_kyoten_nyuryoku_tsukekaemoto is '拠点入力付替元';
comment on table zaimu_kyoten_nyuryoku_user_info is '拠点入力ユーザー';
comment on table zaimu_kyoten_shiwake_de3 is '拠点仕訳抽出(de3)';
comment on table zaimu_kyoten_shiwake_sias is '拠点仕訳抽出(SIAS)';
comment on table zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban is '拠点入力仕訳シリアル番号採番';
comment on table denpyou_serial_no_saiban_kyoten is '伝票番号採番(拠点)';
comment on table shiwake_pattern_master_zaimu_kyoten is '仕訳パターンマスター(拠点)';
comment on table bumon_furikae_setting IS '振替伝票設定(拠点)';

--カラム和名変更(連絡票No.835より)
comment on column denpyou_ichiran_kyoten.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column shiwake_pattern_master_zaimu_kyoten.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column suitouchou_setting.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column teikei_shiwake.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column user_info.zaimu_kyoten_nyuryoku_only_flg is '拠点入力のみ使用フラグ';
comment on column zaimu_kyoten_nyuryoku_ichiran.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_ichiran.zaimu_kyoten_nyuryoku_pattern_name is '拠点入力パターン名称';
comment on column zaimu_kyoten_nyuryoku_shounin_route.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.moto_zaimu_kyoten_nyuryoku_pattern_no is '付替元拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_user_info.zaimu_kyoten_nyuryoku_pattern_no is '拠点入力パターンNo';
comment on column teikei_shiwake_meisai.kari_zaimu_kyoten_nyuryoku_haifu_pattern_no IS '借方拠点入力配賦パターンNo';
comment on column teikei_shiwake_meisai.kashi_zaimu_kyoten_nyuryoku_haifu_pattern_no IS '貸方拠点入力配賦パターンNo';

--現預金出納帳明細テーブル定義変更
ALTER TABLE suitouchou_meisai DROP CONSTRAINT IF EXISTS suitouchou_meisai_PKEY;
ALTER TABLE suitouchou_meisai RENAME TO suitouchou_meisai_old;

create table suitouchou_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , serial_no bigint not null
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , nyukin_goukei numeric(15)
  , shukkin_goukei numeric(15)
  , aite_kazei_kbn character varying(3) not null
  , aite_zeiritsu numeric(3) not null
  , aite_keigen_zeiritsu_kbn character varying(1) not null
  , fusen_color character varying(1) not null
  , meisai_touroku_user_id character varying(30) not null
  , meisai_touroku_shain_no character varying(15) not null
  , meisai_touroku_user_sei character varying(10) not null
  , meisai_touroku_user_mei character varying(10) not null
  , shiwake_serial_no bigint not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint suitouchou_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

comment on table suitouchou_meisai is '現預金出納帳明細';
comment on column suitouchou_meisai.denpyou_id is '伝票ID';
comment on column suitouchou_meisai.denpyou_edano is '伝票枝番号';
comment on column suitouchou_meisai.shiwake_edano is '仕訳枝番号';
comment on column suitouchou_meisai.serial_no is 'シリアル番号';
comment on column suitouchou_meisai.torihiki_name is '取引名';
comment on column suitouchou_meisai.tekiyou is '摘要';
comment on column suitouchou_meisai.nyukin_goukei is '入金';
comment on column suitouchou_meisai.shukkin_goukei is '出金';
comment on column suitouchou_meisai.aite_kazei_kbn is '課税区分';
comment on column suitouchou_meisai.aite_zeiritsu is '税率';
comment on column suitouchou_meisai.aite_keigen_zeiritsu_kbn is '軽減税率区分';
comment on column suitouchou_meisai.fusen_color is '付箋カラー';
comment on column suitouchou_meisai.meisai_touroku_user_id is '明細登録ユーザーID';
comment on column suitouchou_meisai.meisai_touroku_shain_no is '明細登録ユーザー社員No';
comment on column suitouchou_meisai.meisai_touroku_user_sei is '明細登録ユーザー姓';
comment on column suitouchou_meisai.meisai_touroku_user_mei is '明細登録ユーザー名';
comment on column suitouchou_meisai.shiwake_serial_no is '仕訳データシリアルNo';
comment on column suitouchou_meisai.touroku_user_id is '登録ユーザーID';
comment on column suitouchou_meisai.touroku_time is '登録日時';
comment on column suitouchou_meisai.koushin_user_id is '更新ユーザーID';
comment on column suitouchou_meisai.koushin_time is '更新日時';


INSERT INTO suitouchou_meisai
SELECT
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , serial_no
  , torihiki_name
  , tekiyou
  , nyukin_goukei
  , shukkin_goukei
  , aite_kazei_kbn
  , aite_zeiritsu
  , aite_keigen_zeiritsu_kbn
  , fusen_color
  , meisai_touroku_user_id
  , meisai_touroku_shain_no
  , meisai_touroku_user_sei
  , meisai_touroku_user_mei
  , shiwake_serial_no
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM suitouchou_meisai_old;
DROP TABLE suitouchou_meisai_old;

--添付ファイル明細化
create table meisai_tenpu_file_himoduke (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , edano integer not null
  , tenpu_file_serial_no bigint not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint meisai_tenpu_file_himoduke_PKEY primary key (denpyou_id,denpyou_edano,edano)
);

create table user_tenpu_file (
  tenpu_file_serial_no bigserial not null
  , file_name character varying not null
  , file_size bigint not null
  , content_type character varying not null
  , binary_data bytea not null
  , ebunsho_no character varying(19)
  , ebunsho_binary_data bytea
  , ebunsho_touroku_time timestamp without time zone
  , koushin_time timestamp without time zone not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , constraint user_tenpu_file_PKEY primary key (tenpu_file_serial_no)
);

alter table user_tenpu_file add constraint user_tenpu_file_ebunsho_no_key
  unique (ebunsho_no) ;

comment on table meisai_tenpu_file_himoduke is '明細添付ファイル紐づけ';
comment on column meisai_tenpu_file_himoduke.denpyou_id is '伝票ID';
comment on column meisai_tenpu_file_himoduke.denpyou_edano is '伝票枝番号';
comment on column meisai_tenpu_file_himoduke.edano is '枝番号';
comment on column meisai_tenpu_file_himoduke.tenpu_file_serial_no is '添付ファイルシリアルNo';
comment on column meisai_tenpu_file_himoduke.touroku_user_id is '登録ユーザーID';
comment on column meisai_tenpu_file_himoduke.touroku_time is '登録日時';
comment on column meisai_tenpu_file_himoduke.koushin_user_id is '更新ユーザーID';
comment on column meisai_tenpu_file_himoduke.koushin_time is '更新日時';

comment on table user_tenpu_file is 'ユーザー別添付ファイル';
comment on column user_tenpu_file.tenpu_file_serial_no is '添付ファイルシリアルNo';
comment on column user_tenpu_file.file_name is 'ファイル名';
comment on column user_tenpu_file.file_size is 'ファイルサイズ';
comment on column user_tenpu_file.content_type is 'コンテンツタイプ';
comment on column user_tenpu_file.binary_data is 'バイナリーデータ';
comment on column user_tenpu_file.ebunsho_no is 'e文書番号';
comment on column user_tenpu_file.ebunsho_binary_data is 'e文書バイナリーデータ';
comment on column user_tenpu_file.ebunsho_touroku_time is 'e文書登録日時';
comment on column user_tenpu_file.koushin_time is '更新日時';
comment on column user_tenpu_file.touroku_user_id is '登録ユーザーID';
comment on column user_tenpu_file.touroku_time is '登録日時';
comment on column user_tenpu_file.koushin_user_id is '更新ユーザーID';

--仕訳テーブル定義変更
ALTER TABLE zaimu_kyoten_shiwake_de3 DROP CONSTRAINT IF EXISTS zaimu_kyoten_shiwake_de3_PKEY;
ALTER TABLE zaimu_kyoten_shiwake_sias DROP CONSTRAINT IF EXISTS zaimu_kyoten_shiwake_sias_PKEY;
ALTER TABLE zaimu_kyoten_shiwake_de3 RENAME TO zaimu_kyoten_shiwake_de3_old;
ALTER TABLE zaimu_kyoten_shiwake_sias RENAME TO zaimu_kyoten_shiwake_sias_old;

create table zaimu_kyoten_shiwake_de3 (
  serial_no bigint not null
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
  , constraint zaimu_kyoten_shiwake_de3_PKEY primary key (serial_no)
);

create table zaimu_kyoten_shiwake_sias (
  serial_no bigint not null
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
  , constraint zaimu_kyoten_shiwake_sias_PKEY primary key (serial_no)
);

comment on table zaimu_kyoten_shiwake_de3 is '拠点仕訳抽出(de3)';
comment on column zaimu_kyoten_shiwake_de3.serial_no is 'シリアル番号';
comment on column zaimu_kyoten_shiwake_de3.denpyou_id is '伝票ID';
comment on column zaimu_kyoten_shiwake_de3.shiwake_status is '仕訳抽出状態';
comment on column zaimu_kyoten_shiwake_de3.touroku_time is '登録日時';
comment on column zaimu_kyoten_shiwake_de3.koushin_time is '更新日時';
comment on column zaimu_kyoten_shiwake_de3.dymd is '（オープン２１）伝票日付';
comment on column zaimu_kyoten_shiwake_de3.seiri is '（オープン２１）整理月フラグ';
comment on column zaimu_kyoten_shiwake_de3.dcno is '（オープン２１）伝票番号';
comment on column zaimu_kyoten_shiwake_de3.rbmn is '（オープン２１）借方　部門コード';
comment on column zaimu_kyoten_shiwake_de3.rtor is '（オープン２１）借方　取引先コード';
comment on column zaimu_kyoten_shiwake_de3.rkmk is '（オープン２１）借方　科目コード';
comment on column zaimu_kyoten_shiwake_de3.reda is '（オープン２１）借方　枝番コード';
comment on column zaimu_kyoten_shiwake_de3.rkoj is '（オープン２１）借方　工事コード';
comment on column zaimu_kyoten_shiwake_de3.rkos is '（オープン２１）借方　工種コード';
comment on column zaimu_kyoten_shiwake_de3.rprj is '（オープン２１）借方　プロジェクトコード';
comment on column zaimu_kyoten_shiwake_de3.rseg is '（オープン２１）借方　セグメントコード';
comment on column zaimu_kyoten_shiwake_de3.rdm1 is '（オープン２１）借方　ユニバーサルフィールド１';
comment on column zaimu_kyoten_shiwake_de3.rdm2 is '（オープン２１）借方　ユニバーサルフィールド２';
comment on column zaimu_kyoten_shiwake_de3.rdm3 is '（オープン２１）借方　ユニバーサルフィールド３';
comment on column zaimu_kyoten_shiwake_de3.tky is '（オープン２１）摘要';
comment on column zaimu_kyoten_shiwake_de3.tno is '（オープン２１）摘要コード';
comment on column zaimu_kyoten_shiwake_de3.sbmn is '（オープン２１）貸方　部門コード';
comment on column zaimu_kyoten_shiwake_de3.stor is '（オープン２１）貸方　取引先コード';
comment on column zaimu_kyoten_shiwake_de3.skmk is '（オープン２１）貸方　科目コード';
comment on column zaimu_kyoten_shiwake_de3.seda is '（オープン２１）貸方　枝番コード';
comment on column zaimu_kyoten_shiwake_de3.skoj is '（オープン２１）貸方　工事コード';
comment on column zaimu_kyoten_shiwake_de3.skos is '（オープン２１）貸方　工種コード';
comment on column zaimu_kyoten_shiwake_de3.sprj is '（オープン２１）貸方　プロジェクトコード';
comment on column zaimu_kyoten_shiwake_de3.sseg is '（オープン２１）貸方　セグメントコード';
comment on column zaimu_kyoten_shiwake_de3.sdm1 is '（オープン２１）貸方　ユニバーサルフィールド１';
comment on column zaimu_kyoten_shiwake_de3.sdm2 is '（オープン２１）貸方　ユニバーサルフィールド２';
comment on column zaimu_kyoten_shiwake_de3.sdm3 is '（オープン２１）貸方　ユニバーサルフィールド３';
comment on column zaimu_kyoten_shiwake_de3.exvl is '（オープン２１）対価金額';
comment on column zaimu_kyoten_shiwake_de3.valu is '（オープン２１）金額';
comment on column zaimu_kyoten_shiwake_de3.zkmk is '（オープン２１）消費税対象科目コード';
comment on column zaimu_kyoten_shiwake_de3.zrit is '（オープン２１）消費税対象科目税率';
comment on column zaimu_kyoten_shiwake_de3.zkeigen is '（オープン２１）消費税対象科目　軽減税率区分';
comment on column zaimu_kyoten_shiwake_de3.zzkb is '（オープン２１）消費税対象科目　課税区分';
comment on column zaimu_kyoten_shiwake_de3.zgyo is '（オープン２１）消費税対象科目　業種区分';
comment on column zaimu_kyoten_shiwake_de3.zsre is '（オープン２１）消費税対象科目　仕入区分';
comment on column zaimu_kyoten_shiwake_de3.rrit is '（オープン２１）借方　税率';
comment on column zaimu_kyoten_shiwake_de3.rkeigen is '（オープン２１）借方　軽減税率区分';
comment on column zaimu_kyoten_shiwake_de3.srit is '（オープン２１）貸方　税率';
comment on column zaimu_kyoten_shiwake_de3.skeigen is '（オープン２１）貸方　軽減税率区分';
comment on column zaimu_kyoten_shiwake_de3.rzkb is '（オープン２１）借方　課税区分';
comment on column zaimu_kyoten_shiwake_de3.rgyo is '（オープン２１）借方　業種区分';
comment on column zaimu_kyoten_shiwake_de3.rsre is '（オープン２１）借方　仕入区分';
comment on column zaimu_kyoten_shiwake_de3.szkb is '（オープン２１）貸方　課税区分';
comment on column zaimu_kyoten_shiwake_de3.sgyo is '（オープン２１）貸方　業種区分';
comment on column zaimu_kyoten_shiwake_de3.ssre is '（オープン２１）貸方　仕入区分';
comment on column zaimu_kyoten_shiwake_de3.symd is '（オープン２１）支払日';
comment on column zaimu_kyoten_shiwake_de3.skbn is '（オープン２１）支払区分';
comment on column zaimu_kyoten_shiwake_de3.skiz is '（オープン２１）支払期日';
comment on column zaimu_kyoten_shiwake_de3.uymd is '（オープン２１）回収日';
comment on column zaimu_kyoten_shiwake_de3.ukbn is '（オープン２１）入金区分';
comment on column zaimu_kyoten_shiwake_de3.ukiz is '（オープン２１）回収期日';
comment on column zaimu_kyoten_shiwake_de3.sten is '（オープン２１）店券フラグ';
comment on column zaimu_kyoten_shiwake_de3.dkec is '（オープン２１）消込コード';
comment on column zaimu_kyoten_shiwake_de3.kymd is '（オープン２１）起票年月日';
comment on column zaimu_kyoten_shiwake_de3.kbmn is '（オープン２１）起票部門コード';
comment on column zaimu_kyoten_shiwake_de3.kusr is '（オープン２１）起票者コード';
comment on column zaimu_kyoten_shiwake_de3.fusr is '（オープン２１）入力者コード';
comment on column zaimu_kyoten_shiwake_de3.fsen is '（オープン２１）付箋番号';
comment on column zaimu_kyoten_shiwake_de3.sgno is '（オープン２１）承認グループNo.';
comment on column zaimu_kyoten_shiwake_de3.bunri is '（オープン２１）分離区分';
comment on column zaimu_kyoten_shiwake_de3.rate is '（オープン２１）レート';
comment on column zaimu_kyoten_shiwake_de3.gexvl is '（オープン２１）外貨対価金額';
comment on column zaimu_kyoten_shiwake_de3.gvalu is '（オープン２１）外貨金額';
comment on column zaimu_kyoten_shiwake_de3.gsep is '（オープン２１）行区切り';

comment on table zaimu_kyoten_shiwake_sias is '拠点仕訳抽出(SIAS)';
comment on column zaimu_kyoten_shiwake_sias.serial_no is 'シリアル番号';
comment on column zaimu_kyoten_shiwake_sias.denpyou_id is '伝票ID';
comment on column zaimu_kyoten_shiwake_sias.shiwake_status is '仕訳抽出状態';
comment on column zaimu_kyoten_shiwake_sias.touroku_time is '登録日時';
comment on column zaimu_kyoten_shiwake_sias.koushin_time is '更新日時';
comment on column zaimu_kyoten_shiwake_sias.dymd is '（オープン２１）伝票日付';
comment on column zaimu_kyoten_shiwake_sias.seiri is '（オープン２１）整理月フラグ';
comment on column zaimu_kyoten_shiwake_sias.dcno is '（オープン２１）伝票番号';
comment on column zaimu_kyoten_shiwake_sias.kymd is '（オープン２１）起票年月日';
comment on column zaimu_kyoten_shiwake_sias.kbmn is '（オープン２１）起票部門コード';
comment on column zaimu_kyoten_shiwake_sias.kusr is '（オープン２１）起票者コード';
comment on column zaimu_kyoten_shiwake_sias.sgno is '（オープン２１）承認グループNo.';
comment on column zaimu_kyoten_shiwake_sias.hf1 is '（オープン２１）ヘッダーフィールド１';
comment on column zaimu_kyoten_shiwake_sias.hf2 is '（オープン２１）ヘッダーフィールド２';
comment on column zaimu_kyoten_shiwake_sias.hf3 is '（オープン２１）ヘッダーフィールド３';
comment on column zaimu_kyoten_shiwake_sias.hf4 is '（オープン２１）ヘッダーフィールド４';
comment on column zaimu_kyoten_shiwake_sias.hf5 is '（オープン２１）ヘッダーフィールド５';
comment on column zaimu_kyoten_shiwake_sias.hf6 is '（オープン２１）ヘッダーフィールド６';
comment on column zaimu_kyoten_shiwake_sias.hf7 is '（オープン２１）ヘッダーフィールド７';
comment on column zaimu_kyoten_shiwake_sias.hf8 is '（オープン２１）ヘッダーフィールド８';
comment on column zaimu_kyoten_shiwake_sias.hf9 is '（オープン２１）ヘッダーフィールド９';
comment on column zaimu_kyoten_shiwake_sias.hf10 is '（オープン２１）ヘッダーフィールド１０';
comment on column zaimu_kyoten_shiwake_sias.rbmn is '（オープン２１）借方　部門コード';
comment on column zaimu_kyoten_shiwake_sias.rtor is '（オープン２１）借方　取引先コード';
comment on column zaimu_kyoten_shiwake_sias.rkmk is '（オープン２１）借方　科目コード';
comment on column zaimu_kyoten_shiwake_sias.reda is '（オープン２１）借方　枝番コード';
comment on column zaimu_kyoten_shiwake_sias.rkoj is '（オープン２１）借方　工事コード';
comment on column zaimu_kyoten_shiwake_sias.rkos is '（オープン２１）借方　工種コード';
comment on column zaimu_kyoten_shiwake_sias.rprj is '（オープン２１）借方　プロジェクトコード';
comment on column zaimu_kyoten_shiwake_sias.rseg is '（オープン２１）借方　セグメントコード';
comment on column zaimu_kyoten_shiwake_sias.rdm1 is '（オープン２１）借方　ユニバーサルフィールド１';
comment on column zaimu_kyoten_shiwake_sias.rdm2 is '（オープン２１）借方　ユニバーサルフィールド２';
comment on column zaimu_kyoten_shiwake_sias.rdm3 is '（オープン２１）借方　ユニバーサルフィールド３';
comment on column zaimu_kyoten_shiwake_sias.rdm4 is '（オープン２１）借方　ユニバーサルフィールド４';
comment on column zaimu_kyoten_shiwake_sias.rdm5 is '（オープン２１）借方　ユニバーサルフィールド５';
comment on column zaimu_kyoten_shiwake_sias.rdm6 is '（オープン２１）借方　ユニバーサルフィールド６';
comment on column zaimu_kyoten_shiwake_sias.rdm7 is '（オープン２１）借方　ユニバーサルフィールド７';
comment on column zaimu_kyoten_shiwake_sias.rdm8 is '（オープン２１）借方　ユニバーサルフィールド８';
comment on column zaimu_kyoten_shiwake_sias.rdm9 is '（オープン２１）借方　ユニバーサルフィールド９';
comment on column zaimu_kyoten_shiwake_sias.rdm10 is '（オープン２１）借方　ユニバーサルフィールド１０';
comment on column zaimu_kyoten_shiwake_sias.rdm11 is '（オープン２１）借方　ユニバーサルフィールド１１';
comment on column zaimu_kyoten_shiwake_sias.rdm12 is '（オープン２１）借方　ユニバーサルフィールド１２';
comment on column zaimu_kyoten_shiwake_sias.rdm13 is '（オープン２１）借方　ユニバーサルフィールド１３';
comment on column zaimu_kyoten_shiwake_sias.rdm14 is '（オープン２１）借方　ユニバーサルフィールド１４';
comment on column zaimu_kyoten_shiwake_sias.rdm15 is '（オープン２１）借方　ユニバーサルフィールド１５';
comment on column zaimu_kyoten_shiwake_sias.rdm16 is '（オープン２１）借方　ユニバーサルフィールド１６';
comment on column zaimu_kyoten_shiwake_sias.rdm17 is '（オープン２１）借方　ユニバーサルフィールド１７';
comment on column zaimu_kyoten_shiwake_sias.rdm18 is '（オープン２１）借方　ユニバーサルフィールド１８';
comment on column zaimu_kyoten_shiwake_sias.rdm19 is '（オープン２１）借方　ユニバーサルフィールド１９';
comment on column zaimu_kyoten_shiwake_sias.rdm20 is '（オープン２１）借方　ユニバーサルフィールド２０';
comment on column zaimu_kyoten_shiwake_sias.rrit is '（オープン２１）借方　税率';
comment on column zaimu_kyoten_shiwake_sias.rkeigen is '（オープン２１）借方　軽減税率区分';
comment on column zaimu_kyoten_shiwake_sias.rzkb is '（オープン２１）借方　課税区分';
comment on column zaimu_kyoten_shiwake_sias.rgyo is '（オープン２１）借方　業種区分';
comment on column zaimu_kyoten_shiwake_sias.rsre is '（オープン２１）借方　仕入区分';
comment on column zaimu_kyoten_shiwake_sias.rtky is '（オープン２１）借方　摘要';
comment on column zaimu_kyoten_shiwake_sias.rtno is '（オープン２１）借方　摘要コード';
comment on column zaimu_kyoten_shiwake_sias.sbmn is '（オープン２１）貸方　部門コード';
comment on column zaimu_kyoten_shiwake_sias.stor is '（オープン２１）貸方　取引先コード';
comment on column zaimu_kyoten_shiwake_sias.skmk is '（オープン２１）貸方　科目コード';
comment on column zaimu_kyoten_shiwake_sias.seda is '（オープン２１）貸方　枝番コード';
comment on column zaimu_kyoten_shiwake_sias.skoj is '（オープン２１）貸方　工事コード';
comment on column zaimu_kyoten_shiwake_sias.skos is '（オープン２１）貸方　工種コード';
comment on column zaimu_kyoten_shiwake_sias.sprj is '（オープン２１）貸方　プロジェクトコード';
comment on column zaimu_kyoten_shiwake_sias.sseg is '（オープン２１）貸方　セグメントコード';
comment on column zaimu_kyoten_shiwake_sias.sdm1 is '（オープン２１）貸方　ユニバーサルフィールド１';
comment on column zaimu_kyoten_shiwake_sias.sdm2 is '（オープン２１）貸方　ユニバーサルフィールド２';
comment on column zaimu_kyoten_shiwake_sias.sdm3 is '（オープン２１）貸方　ユニバーサルフィールド３';
comment on column zaimu_kyoten_shiwake_sias.sdm4 is '（オープン２１）貸方　ユニバーサルフィールド４';
comment on column zaimu_kyoten_shiwake_sias.sdm5 is '（オープン２１）貸方　ユニバーサルフィールド５';
comment on column zaimu_kyoten_shiwake_sias.sdm6 is '（オープン２１）貸方　ユニバーサルフィールド６';
comment on column zaimu_kyoten_shiwake_sias.sdm7 is '（オープン２１）貸方　ユニバーサルフィールド７';
comment on column zaimu_kyoten_shiwake_sias.sdm8 is '（オープン２１）貸方　ユニバーサルフィールド８';
comment on column zaimu_kyoten_shiwake_sias.sdm9 is '（オープン２１）貸方　ユニバーサルフィールド９';
comment on column zaimu_kyoten_shiwake_sias.sdm10 is '（オープン２１）貸方　ユニバーサルフィールド１０';
comment on column zaimu_kyoten_shiwake_sias.sdm11 is '（オープン２１）貸方　ユニバーサルフィールド１１';
comment on column zaimu_kyoten_shiwake_sias.sdm12 is '（オープン２１）貸方　ユニバーサルフィールド１２';
comment on column zaimu_kyoten_shiwake_sias.sdm13 is '（オープン２１）貸方　ユニバーサルフィールド１３';
comment on column zaimu_kyoten_shiwake_sias.sdm14 is '（オープン２１）貸方　ユニバーサルフィールド１４';
comment on column zaimu_kyoten_shiwake_sias.sdm15 is '（オープン２１）貸方　ユニバーサルフィールド１５';
comment on column zaimu_kyoten_shiwake_sias.sdm16 is '（オープン２１）貸方　ユニバーサルフィールド１６';
comment on column zaimu_kyoten_shiwake_sias.sdm17 is '（オープン２１）貸方　ユニバーサルフィールド１７';
comment on column zaimu_kyoten_shiwake_sias.sdm18 is '（オープン２１）貸方　ユニバーサルフィールド１８';
comment on column zaimu_kyoten_shiwake_sias.sdm19 is '（オープン２１）貸方　ユニバーサルフィールド１９';
comment on column zaimu_kyoten_shiwake_sias.sdm20 is '（オープン２１）貸方　ユニバーサルフィールド２０';
comment on column zaimu_kyoten_shiwake_sias.srit is '（オープン２１）貸方　税率';
comment on column zaimu_kyoten_shiwake_sias.skeigen is '（オープン２１）貸方　軽減税率区分';
comment on column zaimu_kyoten_shiwake_sias.szkb is '（オープン２１）貸方　課税区分';
comment on column zaimu_kyoten_shiwake_sias.sgyo is '（オープン２１）貸方　業種区分';
comment on column zaimu_kyoten_shiwake_sias.ssre is '（オープン２１）貸方　仕入区分';
comment on column zaimu_kyoten_shiwake_sias.stky is '（オープン２１）貸方　摘要';
comment on column zaimu_kyoten_shiwake_sias.stno is '（オープン２１）貸方　摘要コード';
comment on column zaimu_kyoten_shiwake_sias.zkmk is '（オープン２１）消費税対象科目コード';
comment on column zaimu_kyoten_shiwake_sias.zrit is '（オープン２１）消費税対象科目税率';
comment on column zaimu_kyoten_shiwake_sias.zkeigen is '（オープン２１）消費税対象科目　軽減税率区分';
comment on column zaimu_kyoten_shiwake_sias.zzkb is '（オープン２１）消費税対象科目　課税区分';
comment on column zaimu_kyoten_shiwake_sias.zgyo is '（オープン２１）消費税対象科目　業種区分';
comment on column zaimu_kyoten_shiwake_sias.zsre is '（オープン２１）消費税対象科目　仕入区分';
comment on column zaimu_kyoten_shiwake_sias.exvl is '（オープン２１）対価金額';
comment on column zaimu_kyoten_shiwake_sias.valu is '（オープン２１）金額';
comment on column zaimu_kyoten_shiwake_sias.symd is '（オープン２１）支払日';
comment on column zaimu_kyoten_shiwake_sias.skbn is '（オープン２１）支払区分';
comment on column zaimu_kyoten_shiwake_sias.skiz is '（オープン２１）支払期日';
comment on column zaimu_kyoten_shiwake_sias.uymd is '（オープン２１）回収日';
comment on column zaimu_kyoten_shiwake_sias.ukbn is '（オープン２１）入金区分';
comment on column zaimu_kyoten_shiwake_sias.ukiz is '（オープン２１）回収期日';
comment on column zaimu_kyoten_shiwake_sias.dkec is '（オープン２１）消込コード';
comment on column zaimu_kyoten_shiwake_sias.fusr is '（オープン２１）入力者コード';
comment on column zaimu_kyoten_shiwake_sias.fsen is '（オープン２１）付箋番号';
comment on column zaimu_kyoten_shiwake_sias.tkflg is '（オープン２１）貸借別摘要フラグ';
comment on column zaimu_kyoten_shiwake_sias.bunri is '（オープン２１）分離区分';
comment on column zaimu_kyoten_shiwake_sias.heic is '（オープン２１）幣種';
comment on column zaimu_kyoten_shiwake_sias.rate is '（オープン２１）レート';
comment on column zaimu_kyoten_shiwake_sias.gexvl is '（オープン２１）外貨対価金額';
comment on column zaimu_kyoten_shiwake_sias.gvalu is '（オープン２１）外貨金額';
comment on column zaimu_kyoten_shiwake_sias.gsep is '（オープン２１）行区切り';

INSERT INTO zaimu_kyoten_shiwake_de3
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
  , zkeigen
  , zzkb
  , zgyo
  , zsre
  , rrit
  , rkeigen
  , srit
  , skeigen
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
FROM
  zaimu_kyoten_shiwake_de3;
DROP TABLE zaimu_kyoten_shiwake_de3_old;

INSERT INTO zaimu_kyoten_shiwake_sias
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
  , rkeigen
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
  , skeigen
  , szkb
  , sgyo
  , ssre
  , stky
  , stno
  , zkmk
  , zrit
  , zkeigen
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
FROM
  zaimu_kyoten_shiwake_sias_old;
DROP TABLE zaimu_kyoten_shiwake_sias_old;

commit;
