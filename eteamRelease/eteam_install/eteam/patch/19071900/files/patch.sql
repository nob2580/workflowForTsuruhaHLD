SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 画面項目制御
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

-- マスター管理に新しいマスターを追加
\copy master_kanri_hansuu FROM '.\files\csv\master_kanri_hansuu_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_kanri_ichiran FROM '.\files\csv\master_kanri_ichiran_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- マスター取込制御
DELETE FROM master_torikomi_ichiran_de3;
DELETE FROM master_torikomi_shousai_de3;
DELETE FROM master_torikomi_ichiran_sias;
DELETE FROM master_torikomi_shousai_sias;
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- 画面権限制御
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--ユーザー情報テーブルカラム追加
ALTER TABLE user_info RENAME TO user_info_old;
CREATE TABLE user_info
(
	user_id character varying(30) NOT NULL,
	shain_no character varying(15) NOT NULL,
	user_sei character varying(10) NOT NULL,
	user_mei character varying(10) NOT NULL,
	mail_address character varying(50) NOT NULL,
	yuukou_kigen_from DATE NOT NULL,
	Yuukou_Kigen_to DATE NOT NULL,
	touroku_user_id character varying(30) NOT NULL,
	touroku_time TIMESTAMP NOT NULL,
	koushin_user_id character varying(30) NOT NULL,
	koushin_time TIMESTAMP NOT NULL,
	pass_koushin_date DATE,
	Pass_failure_count INT DEFAULT 0 NOT NULL,
	pass_failure_time TIMESTAMP,
	tmp_lock_flg character varying(1) NOT NULL,
	tmp_lock_time TIMESTAMP,
	lock_flg character varying(1) DEFAULT '0' NOT NULL,
	lock_time TIMESTAMP,
	dairikihyou_flg character varying(1) NOT NULL,
	houjin_card_riyou_flag character varying(1) NOT NULL,
	houjin_card_shikibetsuyou_num character varying(20) NOT NULL,
	security_pattern character varying(4) NOT NULL,
	security_wfonly_flg character varying(1) NOT NULL,
	shounin_route_henkou_level character varying(1) NOT NULL,
	maruhi_kengen_flg character varying(1) NOT NULL,
	maruhi_kaijyo_flg character varying(1) NOT NULL,
	zaimu_kyoten_nyuryoku_only_flg character varying(1) default '0' not null,
	PRIMARY KEY (USER_ID)
) WITHOUT OIDS;
COMMENT ON TABLE user_info IS 'ユーザー情報';
COMMENT ON COLUMN user_info.user_id IS 'ユーザーID';
COMMENT ON COLUMN user_info.shain_no IS '社員番号';
COMMENT ON COLUMN user_info.user_sei IS 'ユーザー姓';
COMMENT ON COLUMN user_info.user_mei IS 'ユーザー名';
COMMENT ON COLUMN user_info.mail_address IS 'メールアドレス';
COMMENT ON COLUMN user_info.yuukou_kigen_from IS '有効期限開始日';
COMMENT ON COLUMN user_info.yuukou_kigen_to IS '有効期限終了日';
COMMENT ON COLUMN user_info.touroku_user_id IS '登録ユーザーID';
COMMENT ON COLUMN user_info.touroku_time IS '登録日時';
COMMENT ON COLUMN user_info.koushin_user_id IS '更新ユーザーID';
COMMENT ON COLUMN user_info.koushin_time IS '更新日時';
COMMENT ON COLUMN user_info.pass_koushin_date IS 'パスワード変更日';
COMMENT ON COLUMN user_info.pass_failure_count IS 'パスワード誤り回数';
COMMENT ON COLUMN user_info.pass_failure_time IS 'パスワード誤り時間';
COMMENT ON COLUMN user_info.tmp_lock_flg IS 'アカウント一時ロックフラグ';
COMMENT ON COLUMN user_info.tmp_lock_time IS 'アカウント一時ロック時間';
COMMENT ON COLUMN user_info.lock_flg IS 'アカウントロックフラグ';
COMMENT ON COLUMN user_info.lock_time IS 'アカウントロック時間';
COMMENT ON COLUMN user_info.dairikihyou_flg IS '代理起票可能フラグ';
COMMENT ON COLUMN user_info.houjin_card_riyou_flag IS '法人カード利用';
COMMENT ON COLUMN user_info.houjin_card_shikibetsuyou_num IS '法人カード識別用番号';
COMMENT ON COLUMN user_info.security_pattern IS 'セキュリティパターン';
COMMENT ON COLUMN user_info.security_wfonly_flg IS 'セキュリティワークフロー限定フラグ';
COMMENT ON COLUMN user_info.shounin_route_henkou_level IS '承認ルート変更権限レベル';
COMMENT ON COLUMN user_info.maruhi_kengen_flg IS 'マル秘設定権限';
COMMENT ON COLUMN user_info.maruhi_kaijyo_flg IS 'マル秘解除権限';
COMMENT ON COLUMN user_info.zaimu_kyoten_nyuryoku_only_flg is '財務拠点入力のみ使用フラグ';
INSERT INTO user_info(
	user_id,
	shain_no,
	user_sei,
	user_mei,
	mail_address,
	yuukou_kigen_from,
	yuukou_kigen_to,
	touroku_user_id,
	touroku_time,
	koushin_user_id,
	koushin_time,
	pass_koushin_date,
	pass_failure_count,
	pass_failure_time,
	tmp_lock_flg,
	tmp_lock_time,
	lock_flg,
	lock_time,
	dairikihyou_flg,
	houjin_card_riyou_flag,
	houjin_card_shikibetsuyou_num,
	security_pattern,
	security_wfonly_flg ,
	shounin_route_henkou_level,
	maruhi_kengen_flg,
	maruhi_kaijyo_flg,
	zaimu_kyoten_nyuryoku_only_flg
)
SELECT
	user_id,
	shain_no,
	user_sei,
	user_mei,
	mail_address,
	yuukou_kigen_from,
	yuukou_kigen_to,
	touroku_user_id,
	touroku_time,
	koushin_user_id,
	koushin_time,
	pass_koushin_date,
	pass_failure_count,
	pass_failure_time,
	tmp_lock_flg,
	tmp_lock_time,
	lock_flg,
	lock_time,
	dairikihyou_flg,
	houjin_card_riyou_flag,
	houjin_card_shikibetsuyou_num,
	security_pattern,
	security_wfonly_flg ,
	shounin_route_henkou_level,
	maruhi_kengen_flg,
	maruhi_kengen_flg,
	0 --財務拠点入力のみ使用フラグ
FROM user_info_old;
DROP TABLE user_info_old;

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
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint bumon_furikae_PKEY primary key (denpyou_id)
);
comment on table bumon_furikae is '部門振替伝票';
comment on column bumon_furikae.denpyou_id is '伝票ID';
comment on column bumon_furikae.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
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
comment on column bumon_furikae.touroku_user_id is '登録ユーザーID';
comment on column bumon_furikae.touroku_time is '登録日時';
comment on column bumon_furikae.koushin_user_id is '更新ユーザーID';
comment on column bumon_furikae.koushin_time is '更新日時';

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
  , gyou_kugiri character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint bumon_furikae_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table bumon_furikae_meisai is '部門振替伝票明細';
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
comment on column bumon_furikae_meisai.gyou_kugiri is '行区切り';
comment on column bumon_furikae_meisai.touroku_user_id is '登録ユーザーID';
comment on column bumon_furikae_meisai.touroku_time is '登録日時';
comment on column bumon_furikae_meisai.koushin_user_id is '更新ユーザーID';
comment on column bumon_furikae_meisai.koushin_time is '更新日時';

create table bumon_furikae_setting (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , taishaku_tekiyou_flg character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint bumon_furikae_setting_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no)
);
comment on table bumon_furikae_setting is '部門振替設定';
comment on column bumon_furikae_setting.denpyou_kbn is '伝票区分';
comment on column bumon_furikae_setting.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column bumon_furikae_setting.taishaku_tekiyou_flg is '貸借摘要フラグ';
comment on column bumon_furikae_setting.touroku_user_id is '登録ユーザーID';
comment on column bumon_furikae_setting.touroku_time is '登録日時';
comment on column bumon_furikae_setting.koushin_user_id is '更新ユーザーID';
comment on column bumon_furikae_setting.koushin_time is '更新日時';

create table edaban_zandaka (
  kamoku_gaibu_cd character varying(8) not null
  , kamoku_edaban_cd character varying(12) not null
  , kamoku_naibu_cd character varying(15) not null
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , edaban_name character varying(20) not null
  , kessanki_bangou smallint not null
  , chouhyou_shaturyoku_no smallint
  , taishaku_zokusei smallint not null
  , zandaka_kari00 numeric(19) not null
  , zandaka_kashi00 numeric(19) not null
  , zandaka_kari01 numeric(19) not null
  , zandaka_kashi01 numeric(19) not null
  , zandaka_kari02 numeric(19) not null
  , zandaka_kashi02 numeric(19) not null
  , zandaka_kari03 numeric(19) not null
  , zandaka_kashi03 numeric(19) not null
  , zandaka_kari03_shu numeric(19) not null
  , zandaka_kashi03_shu numeric(19) not null
  , zandaka_kari04 numeric(19) not null
  , zandaka_kashi04 numeric(19) not null
  , zandaka_kari05 numeric(19) not null
  , zandaka_kashi05 numeric(19) not null
  , zandaka_kari06 numeric(19) not null
  , zandaka_kashi06 numeric(19) not null
  , zandaka_kari06_shu numeric(19) not null
  , zandaka_kashi06_shu numeric(19) not null
  , zandaka_kari07 numeric(19) not null
  , zandaka_kashi07 numeric(19) not null
  , zandaka_kari08 numeric(19) not null
  , zandaka_kashi08 numeric(19) not null
  , zandaka_kari09 numeric(19) not null
  , zandaka_kashi09 numeric(19) not null
  , zandaka_kari09_shu numeric(19) not null
  , zandaka_kashi09_shu numeric(19) not null
  , zandaka_kari10 numeric(19) not null
  , zandaka_kashi10 numeric(19) not null
  , zandaka_kari11 numeric(19) not null
  , zandaka_kashi11 numeric(19) not null
  , zandaka_kari12 numeric(19) not null
  , zandaka_kashi12 numeric(19) not null
  , zandaka_kari12_shu numeric(19) not null
  , zandaka_kashi12_shu numeric(19) not null
  , constraint edaban_zandaka_PKEY primary key (kamoku_gaibu_cd,kamoku_edaban_cd,kessanki_bangou)
);
comment on table edaban_zandaka is '枝番残高';
comment on column edaban_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column edaban_zandaka.kamoku_edaban_cd is '科目枝番コード';
comment on column edaban_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column edaban_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column edaban_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column edaban_zandaka.edaban_name is '枝番名';
comment on column edaban_zandaka.kessanki_bangou is '決算期番号';
comment on column edaban_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column edaban_zandaka.taishaku_zokusei is '貸借属性';
comment on column edaban_zandaka.zandaka_kari00 is '期首残高(借方)';
comment on column edaban_zandaka.zandaka_kashi00 is '期首残高(貸方)';
comment on column edaban_zandaka.zandaka_kari01 is '1ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi01 is '1ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari02 is '2ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi02 is '2ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari03 is '3ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi03 is '3ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari03_shu is '3ｹ月目修正(借)';
comment on column edaban_zandaka.zandaka_kashi03_shu is '3ｹ月目修正(貸)';
comment on column edaban_zandaka.zandaka_kari04 is '4ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi04 is '4ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari05 is '5ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi05 is '5ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari06 is '6ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi06 is '6ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari06_shu is '6ｹ月目修正(借)';
comment on column edaban_zandaka.zandaka_kashi06_shu is '6ｹ月目修正(貸)';
comment on column edaban_zandaka.zandaka_kari07 is '7ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi07 is '7ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari08 is '8ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi08 is '8ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari09 is '9ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi09 is '9ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari09_shu is '9ｹ月目修正(借)';
comment on column edaban_zandaka.zandaka_kashi09_shu is '9ｹ月目修正(貸)';
comment on column edaban_zandaka.zandaka_kari10 is '10ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi10 is '10ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari11 is '11ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi11 is '11ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari12 is '12ｹ月目(借)';
comment on column edaban_zandaka.zandaka_kashi12 is '12ｹ月目(貸)';
comment on column edaban_zandaka.zandaka_kari12_shu is '12ｹ月目修正(借)';
comment on column edaban_zandaka.zandaka_kashi12_shu is '12ｹ月目修正(貸)';

create table kamoku_zandaka (
  kamoku_gaibu_cd character varying(8) not null
  , kamoku_naibu_cd character varying(15) not null
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , kessanki_bangou smallint not null
  , chouhyou_shaturyoku_no smallint
  , taishaku_zokusei smallint not null
  , zandaka_kari00 numeric(19) not null
  , zandaka_kashi00 numeric(19) not null
  , zandaka_kari01 numeric(19) not null
  , zandaka_kashi01 numeric(19) not null
  , zandaka_kari02 numeric(19) not null
  , zandaka_kashi02 numeric(19) not null
  , zandaka_kari03 numeric(19) not null
  , zandaka_kashi03 numeric(19) not null
  , zandaka_kari03_shu numeric(19) not null
  , zandaka_kashi03_shu numeric(19) not null
  , zandaka_kari04 numeric(19) not null
  , zandaka_kashi04 numeric(19) not null
  , zandaka_kari05 numeric(19) not null
  , zandaka_kashi05 numeric(19) not null
  , zandaka_kari06 numeric(19) not null
  , zandaka_kashi06 numeric(19) not null
  , zandaka_kari06_shu numeric(19) not null
  , zandaka_kashi06_shu numeric(19) not null
  , zandaka_kari07 numeric(19) not null
  , zandaka_kashi07 numeric(19) not null
  , zandaka_kari08 numeric(19) not null
  , zandaka_kashi08 numeric(19) not null
  , zandaka_kari09 numeric(19) not null
  , zandaka_kashi09 numeric(19) not null
  , zandaka_kari09_shu numeric(19) not null
  , zandaka_kashi09_shu numeric(19) not null
  , zandaka_kari10 numeric(19) not null
  , zandaka_kashi10 numeric(19) not null
  , zandaka_kari11 numeric(19) not null
  , zandaka_kashi11 numeric(19) not null
  , zandaka_kari12 numeric(19) not null
  , zandaka_kashi12 numeric(19) not null
  , zandaka_kari12_shu numeric(19) not null
  , zandaka_kashi12_shu numeric(19) not null
  , constraint kamoku_zandaka_PKEY primary key (kamoku_gaibu_cd,kessanki_bangou)
);
comment on table kamoku_zandaka is '科目残高';
comment on column kamoku_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column kamoku_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column kamoku_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column kamoku_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column kamoku_zandaka.kessanki_bangou is '決算期番号';
comment on column kamoku_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column kamoku_zandaka.taishaku_zokusei is '貸借属性';
comment on column kamoku_zandaka.zandaka_kari00 is '期首残高(借方)';
comment on column kamoku_zandaka.zandaka_kashi00 is '期首残高(貸方)';
comment on column kamoku_zandaka.zandaka_kari01 is '1ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi01 is '1ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari02 is '2ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi02 is '2ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari03 is '3ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi03 is '3ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari03_shu is '3ｹ月目修正(借)';
comment on column kamoku_zandaka.zandaka_kashi03_shu is '3ｹ月目修正(貸)';
comment on column kamoku_zandaka.zandaka_kari04 is '4ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi04 is '4ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari05 is '5ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi05 is '5ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari06 is '6ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi06 is '6ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari06_shu is '6ｹ月目修正(借)';
comment on column kamoku_zandaka.zandaka_kashi06_shu is '6ｹ月目修正(貸)';
comment on column kamoku_zandaka.zandaka_kari07 is '7ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi07 is '7ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari08 is '8ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi08 is '8ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari09 is '9ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi09 is '9ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari09_shu is '9ｹ月目修正(借)';
comment on column kamoku_zandaka.zandaka_kashi09_shu is '9ｹ月目修正(貸)';
comment on column kamoku_zandaka.zandaka_kari10 is '10ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi10 is '10ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari11 is '11ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi11 is '11ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari12 is '12ｹ月目(借)';
comment on column kamoku_zandaka.zandaka_kashi12 is '12ｹ月目(貸)';
comment on column kamoku_zandaka.zandaka_kari12_shu is '12ｹ月目修正(借)';
comment on column kamoku_zandaka.zandaka_kashi12_shu is '12ｹ月目修正(貸)';

create table ki_shouhizei_setting (
  kesn smallint not null
  , shiire_zeigaku_anbun_flg smallint not null
  , shouhizei_kbn smallint not null
  , hasuu_shori_flg smallint not null
  , zeigaku_keisan_flg smallint not null
  , shouhizeitaishou_minyuryoku_flg smallint not null
  , constraint ki_shouhizei_setting_PKEY primary key (kesn)
);
comment on table ki_shouhizei_setting is '（期別）消費税設定';
comment on column ki_shouhizei_setting.kesn is '内部決算期';
comment on column ki_shouhizei_setting.shiire_zeigaku_anbun_flg is '仕入税額按分フラグ';
comment on column ki_shouhizei_setting.shouhizei_kbn is '消費税区分';
comment on column ki_shouhizei_setting.hasuu_shori_flg is '端数処理フラグ';
comment on column ki_shouhizei_setting.zeigaku_keisan_flg is '税額計算フラグ';
comment on column ki_shouhizei_setting.shouhizeitaishou_minyuryoku_flg is '消費税対象科目未入力フラグ';

create table shiwake_pattern_master_zaimu_kyoten (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , shiwake_edano integer not null
  , delete_flg character varying(1) default '0' not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , bunrui1 character varying(20) not null
  , bunrui2 character varying(20) not null
  , bunrui3 character varying(20) not null
  , torihiki_name character varying(20) not null
  , tekiyou_flg character varying(1) not null
  , tekiyou character varying(20) not null
  , nyushukkin_flg character varying(1) not null
  , hyouji_jun integer not null
  , shain_cd_renkei_flg character varying(1) not null
  , tenpu_file_hissu_flg character varying(1) not null
  , shiwake_taishougai_flg character varying(1) not null
  , fusen_flg character varying(1) not null
  , fusen_color character varying(1) not null
  , zaimu_kyoten_nyuryoku_pattern_no_bumon_furikae character varying(6) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , header_futan_bumon_cd character varying not null
  , header_torihikisaki_cd character varying not null
  , header_kamoku_cd character varying not null
  , header_kamoku_edaban_cd character varying not null
  , header_project_cd character varying not null
  , header_segment_cd character varying(8) not null
  , header_uf1_cd character varying(20) not null
  , header_uf2_cd character varying(20) not null
  , header_uf3_cd character varying(20) not null
  , header_uf4_cd character varying(20) not null
  , header_uf5_cd character varying(20) not null
  , header_uf6_cd character varying(20) not null
  , header_uf7_cd character varying(20) not null
  , header_uf8_cd character varying(20) not null
  , header_uf9_cd character varying(20) not null
  , header_uf10_cd character varying(20) not null
  , header_uf_kotei1_cd character varying(20) not null
  , header_uf_kotei2_cd character varying(20) not null
  , header_uf_kotei3_cd character varying(20) not null
  , header_uf_kotei4_cd character varying(20) not null
  , header_uf_kotei5_cd character varying(20) not null
  , header_uf_kotei6_cd character varying(20) not null
  , header_uf_kotei7_cd character varying(20) not null
  , header_uf_kotei8_cd character varying(20) not null
  , header_uf_kotei9_cd character varying(20) not null
  , header_uf_kotei10_cd character varying(20) not null
  , header_kazei_kbn character varying not null
  , header_zeiritsu character varying(10) not null
  , header_keigen_zeiritsu_kbn character varying(1) not null
  , header_bunri_kbn character varying(1) not null
  , header_kobetsu_kbn character varying(1) not null
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
  , kari_bunri_kbn character varying(1) not null
  , kari_kobetsu_kbn character varying(1) not null
  , kashi_futan_bumon_cd character varying not null
  , kashi_kamoku_cd character varying not null
  , kashi_kamoku_edaban_cd character varying not null
  , kashi_torihikisaki_cd character varying not null
  , kashi_project_cd character varying not null
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
  , kashi_uf_kotei1_cd character varying(20) not null
  , kashi_uf_kotei2_cd character varying(20) not null
  , kashi_uf_kotei3_cd character varying(20) not null
  , kashi_uf_kotei4_cd character varying(20) not null
  , kashi_uf_kotei5_cd character varying(20) not null
  , kashi_uf_kotei6_cd character varying(20) not null
  , kashi_uf_kotei7_cd character varying(20) not null
  , kashi_uf_kotei8_cd character varying(20) not null
  , kashi_uf_kotei9_cd character varying(20) not null
  , kashi_uf_kotei10_cd character varying(20) not null
  , kashi_kazei_kbn character varying not null
  , kashi_zeiritsu character varying(10) not null
  , kashi_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_bunri_kbn character varying(1) not null
  , kashi_kobetsu_kbn character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shiwake_pattern_master_zaimu_kyoten_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,shiwake_edano)
);
comment on table shiwake_pattern_master_zaimu_kyoten is '仕訳パターンマスター（財務拠点入力）';
comment on column shiwake_pattern_master_zaimu_kyoten.denpyou_kbn is '伝票区分';
comment on column shiwake_pattern_master_zaimu_kyoten.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column shiwake_pattern_master_zaimu_kyoten.shiwake_edano is '仕訳枝番号';
comment on column shiwake_pattern_master_zaimu_kyoten.delete_flg is '削除フラグ';
comment on column shiwake_pattern_master_zaimu_kyoten.yuukou_kigen_from is '有効期限開始日';
comment on column shiwake_pattern_master_zaimu_kyoten.yuukou_kigen_to is '有効期限終了日';
comment on column shiwake_pattern_master_zaimu_kyoten.bunrui1 is '分類1';
comment on column shiwake_pattern_master_zaimu_kyoten.bunrui2 is '分類2';
comment on column shiwake_pattern_master_zaimu_kyoten.bunrui3 is '分類3';
comment on column shiwake_pattern_master_zaimu_kyoten.torihiki_name is '取引名';
comment on column shiwake_pattern_master_zaimu_kyoten.tekiyou_flg is '摘要フラグ';
comment on column shiwake_pattern_master_zaimu_kyoten.tekiyou is '摘要';
comment on column shiwake_pattern_master_zaimu_kyoten.nyushukkin_flg is '入出金フラグ';
comment on column shiwake_pattern_master_zaimu_kyoten.hyouji_jun is '表示順';
comment on column shiwake_pattern_master_zaimu_kyoten.shain_cd_renkei_flg is '社員コード連携フラグ';
comment on column shiwake_pattern_master_zaimu_kyoten.tenpu_file_hissu_flg is '添付ファイル必須フラグ';
comment on column shiwake_pattern_master_zaimu_kyoten.shiwake_taishougai_flg is '仕訳対象外フラグ';
comment on column shiwake_pattern_master_zaimu_kyoten.fusen_flg is '付箋フラグ';
comment on column shiwake_pattern_master_zaimu_kyoten.fusen_color is '付箋カラー';
comment on column shiwake_pattern_master_zaimu_kyoten.zaimu_kyoten_nyuryoku_pattern_no_bumon_furikae is '部門振替パターンNo';
comment on column shiwake_pattern_master_zaimu_kyoten.zaimu_kyoten_nyuryoku_haifu_pattern_no is '配賦パターンNo';
comment on column shiwake_pattern_master_zaimu_kyoten.header_futan_bumon_cd is 'ヘッダー負担部門コード（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_torihikisaki_cd is 'ヘッダー取引先コード（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_kamoku_cd is 'ヘッダー科目コード（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_kamoku_edaban_cd is 'ヘッダー科目枝番コード（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_project_cd is 'ヘッダープロジェクトコード（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_segment_cd is 'ヘッダーセグメントコード（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf1_cd is 'ヘッダーUF1コード';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf2_cd is 'ヘッダーUF2コード';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf3_cd is 'ヘッダーUF3コード';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf4_cd is 'ヘッダーUF4コード';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf5_cd is 'ヘッダーUF5コード';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf6_cd is 'ヘッダーUF6コード';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf7_cd is 'ヘッダーUF7コード';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf8_cd is 'ヘッダーUF8コード';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf9_cd is 'ヘッダーUF9コード';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf10_cd is 'ヘッダーUF10コード';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei1_cd is 'ヘッダーUF1コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei2_cd is 'ヘッダーUF2コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei3_cd is 'ヘッダーUF3コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei4_cd is 'ヘッダーUF4コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei5_cd is 'ヘッダーUF5コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei6_cd is 'ヘッダーUF6コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei7_cd is 'ヘッダーUF7コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei8_cd is 'ヘッダーUF8コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei9_cd is 'ヘッダーUF9コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_uf_kotei10_cd is 'ヘッダーUF10コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_kazei_kbn is 'ヘッダー課税区分（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_zeiritsu is 'ヘッダー消費税率（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_keigen_zeiritsu_kbn is 'ヘッダー軽減税率区分（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_bunri_kbn is 'ヘッダー分離区分（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.header_kobetsu_kbn is 'ヘッダー個別区分（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_futan_bumon_cd is '借方負担部門コード（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_kamoku_cd is '借方科目コード（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_kamoku_edaban_cd is '借方科目枝番コード（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_torihikisaki_cd is '借方取引先コード（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_project_cd is '借方プロジェクトコード（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_segment_cd is '借方セグメントコード';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf1_cd is '借方UF1コード';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf2_cd is '借方UF2コード';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf3_cd is '借方UF3コード';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf4_cd is '借方UF4コード';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf5_cd is '借方UF5コード';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf6_cd is '借方UF6コード';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf7_cd is '借方UF7コード';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf8_cd is '借方UF8コード';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf9_cd is '借方UF9コード';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf10_cd is '借方UF10コード';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei1_cd is '借方UF1コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei2_cd is '借方UF2コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei3_cd is '借方UF3コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei4_cd is '借方UF4コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei5_cd is '借方UF5コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei6_cd is '借方UF6コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei7_cd is '借方UF7コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei8_cd is '借方UF8コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei9_cd is '借方UF9コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_uf_kotei10_cd is '借方UF10コード（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_kazei_kbn is '借方課税区分（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_zeiritsu is '借方消費税率（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_keigen_zeiritsu_kbn is '借方軽減税率区分（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_bunri_kbn is '借方分離区分（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kari_kobetsu_kbn is '借方個別区分（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_futan_bumon_cd is '貸方負担部門コード１（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_kamoku_cd is '貸方科目コード１（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_kamoku_edaban_cd is '貸方科目枝番コード１（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_torihikisaki_cd is '貸方取引先コード１（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_project_cd is '貸方プロジェクトコード１（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_segment_cd is '貸方セグメントコード１（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf1_cd is '貸方UF1コード１';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf2_cd is '貸方UF2コード１';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf3_cd is '貸方UF3コード１';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf4_cd is '貸方UF4コード１';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf5_cd is '貸方UF5コード１';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf6_cd is '貸方UF6コード１';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf7_cd is '貸方UF7コード１';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf8_cd is '貸方UF8コード１';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf9_cd is '貸方UF9コード１';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf10_cd is '貸方UF10コード１';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei1_cd is '貸方UF1コード１（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei2_cd is '貸方UF2コード１（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei3_cd is '貸方UF3コード１（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei4_cd is '貸方UF4コード１（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei5_cd is '貸方UF5コード１（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei6_cd is '貸方UF6コード１（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei7_cd is '貸方UF7コード１（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei8_cd is '貸方UF8コード１（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei9_cd is '貸方UF9コード１（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_uf_kotei10_cd is '貸方UF10コード１（固定）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_kazei_kbn is '貸方課税区分１（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_zeiritsu is '貸方消費税率（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_keigen_zeiritsu_kbn is '貸方軽減税率区分（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_bunri_kbn is '貸方分離区分（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.kashi_kobetsu_kbn is '貸方個別区分（仕訳パターン）';
comment on column shiwake_pattern_master_zaimu_kyoten.touroku_user_id is '登録ユーザーID';
comment on column shiwake_pattern_master_zaimu_kyoten.touroku_time is '登録日時';
comment on column shiwake_pattern_master_zaimu_kyoten.koushin_user_id is '更新ユーザーID';
comment on column shiwake_pattern_master_zaimu_kyoten.koushin_time is '更新日時';

create table suitouchou_setting (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , zandaka_type character varying(1) not null
  , futan_bumon_cd character varying(8) not null
  , futan_bumon_name character varying(20) not null
  , kamoku_gaibu_cd character varying(6) not null
  , kamoku_name character varying(22) not null
  , kamoku_edaban_cd character varying(12) not null
  , kamoku_edaban_name character varying(20) not null
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
  , kaishi_date date
  , kaishi_zandaka numeric(15)
  , denpyou_no_tani_flg character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint suitouchou_setting_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no)
);
comment on table suitouchou_setting is '出納帳設定';
comment on column suitouchou_setting.denpyou_kbn is '伝票区分';
comment on column suitouchou_setting.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column suitouchou_setting.zandaka_type is '残高タイプ';
comment on column suitouchou_setting.futan_bumon_cd is '負担部門コード';
comment on column suitouchou_setting.futan_bumon_name is '負担部門名';
comment on column suitouchou_setting.kamoku_gaibu_cd is '科目外部コード';
comment on column suitouchou_setting.kamoku_name is '科目名';
comment on column suitouchou_setting.kamoku_edaban_cd is '科目枝番コード';
comment on column suitouchou_setting.kamoku_edaban_name is '科目枝番名';
comment on column suitouchou_setting.hf1_cd is 'HF1コード';
comment on column suitouchou_setting.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column suitouchou_setting.hf2_cd is 'HF2コード';
comment on column suitouchou_setting.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column suitouchou_setting.hf3_cd is 'HF3コード';
comment on column suitouchou_setting.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column suitouchou_setting.hf4_cd is 'HF4コード';
comment on column suitouchou_setting.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column suitouchou_setting.hf5_cd is 'HF5コード';
comment on column suitouchou_setting.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column suitouchou_setting.hf6_cd is 'HF6コード';
comment on column suitouchou_setting.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column suitouchou_setting.hf7_cd is 'HF7コード';
comment on column suitouchou_setting.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column suitouchou_setting.hf8_cd is 'HF8コード';
comment on column suitouchou_setting.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column suitouchou_setting.hf9_cd is 'HF9コード';
comment on column suitouchou_setting.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column suitouchou_setting.hf10_cd is 'HF10コード';
comment on column suitouchou_setting.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column suitouchou_setting.kaishi_date is '開始年月日';
comment on column suitouchou_setting.kaishi_zandaka is '開始残高';
comment on column suitouchou_setting.denpyou_no_tani_flg is '伝票番号単位フラグ';
comment on column suitouchou_setting.touroku_user_id is '登録ユーザーID';
comment on column suitouchou_setting.touroku_time is '登録日時';
comment on column suitouchou_setting.koushin_user_id is '更新ユーザーID';
comment on column suitouchou_setting.koushin_time is '更新日時';

create table zaimu_kyoten_nyuryoku_bumon_security (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , futan_bumon_cd character varying(8) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_bumon_security_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,futan_bumon_cd)
);
comment on table zaimu_kyoten_nyuryoku_bumon_security is '財務拠点入力部門セキュリティ';
comment on column zaimu_kyoten_nyuryoku_bumon_security.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_bumon_security.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_bumon_security.futan_bumon_cd is '負担部門コード';
comment on column zaimu_kyoten_nyuryoku_bumon_security.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_bumon_security.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_bumon_security.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_bumon_security.koushin_time is '更新日時';

create table zaimu_kyoten_nyuryoku_denpyou_no (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , denpyou_serial_no_start bigint not null
  , denpyou_serial_no_end bigint not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_denpyou_no_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,yuukou_kigen_to)
);
comment on table zaimu_kyoten_nyuryoku_denpyou_no is '財務拠点入伝票番号設定';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.yuukou_kigen_from is '有効期限開始日';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.yuukou_kigen_to is '有効期限終了日';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.denpyou_serial_no_start is '開始番号（伝票番号）';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.denpyou_serial_no_end is '終了番号（伝票番号）';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_denpyou_no.koushin_time is '更新日時';

create table zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , sequence_val integer not null
  , constraint zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,yuukou_kigen_from,yuukou_kigen_to)
);
comment on table zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban is '財務拠点入伝票番号採番';
comment on column zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban.yuukou_kigen_from is '有効期限開始日';
comment on column zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban.yuukou_kigen_to is '有効期限終了日';
comment on column zaimu_kyoten_nyuryoku_denpyou_serial_no_saiban.sequence_val is 'シーケンス値';

create table zaimu_kyoten_nyuryoku_ichiran (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , zaimu_kyoten_nyuryoku_pattern_name character varying(20) not null
  , naiyou character varying(160) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , denpyou_print_flg character varying(1) not null
  , shounin_jyoukyou_print_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_ichiran_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no)
);
comment on table zaimu_kyoten_nyuryoku_ichiran is '財務拠点入力一覧';
comment on column zaimu_kyoten_nyuryoku_ichiran.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_ichiran.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_ichiran.zaimu_kyoten_nyuryoku_pattern_name is '財務拠点入力パターン名称';
comment on column zaimu_kyoten_nyuryoku_ichiran.naiyou is '内容（伝票）';
comment on column zaimu_kyoten_nyuryoku_ichiran.yuukou_kigen_from is '有効期限開始日';
comment on column zaimu_kyoten_nyuryoku_ichiran.yuukou_kigen_to is '有効期限終了日';
comment on column zaimu_kyoten_nyuryoku_ichiran.denpyou_print_flg is '申請時帳票出力フラグ';
comment on column zaimu_kyoten_nyuryoku_ichiran.shounin_jyoukyou_print_flg is '承認状況欄印刷フラグ';
comment on column zaimu_kyoten_nyuryoku_ichiran.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_ichiran.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_ichiran.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_ichiran.koushin_time is '更新日時';

create table zaimu_kyoten_nyuryoku_kamoku_security (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , kamoku_gaibu_cd character varying(6) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_kamoku_security_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,kamoku_gaibu_cd)
);
comment on table zaimu_kyoten_nyuryoku_kamoku_security is '財務拠点入力科目セキュリティ';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.kamoku_gaibu_cd is '科目外部コード';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_kamoku_security.koushin_time is '更新日時';

create table zaimu_kyoten_nyuryoku_shounin_route (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , edano integer not null
  , user_id character varying(30) not null
  , dairi_shounin_user_id character varying(30) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_shounin_route_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,edano)
);
comment on table zaimu_kyoten_nyuryoku_shounin_route is '財務拠点入力承認ルート';
comment on column zaimu_kyoten_nyuryoku_shounin_route.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_shounin_route.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_shounin_route.edano is '枝番号';
comment on column zaimu_kyoten_nyuryoku_shounin_route.user_id is 'ユーザーID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.dairi_shounin_user_id is '代理承認者ユーザーID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_shounin_route.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_shounin_route.koushin_time is '更新日時';

create table zaimu_kyoten_nyuryoku_tsukekaemoto (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , moto_denpyou_kbn character varying(4) not null
  , moto_zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_tsukekaemoto_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,moto_denpyou_kbn,moto_zaimu_kyoten_nyuryoku_pattern_no)
);
comment on table zaimu_kyoten_nyuryoku_tsukekaemoto is '財務拠点入力付替元';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.moto_denpyou_kbn is '付替元伝票区分';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.moto_zaimu_kyoten_nyuryoku_pattern_no is '付替元財務拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_tsukekaemoto.koushin_time is '更新日時';

create table zaimu_kyoten_nyuryoku_user_info (
  denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , user_id character varying(30) not null
  , open21_user_id character varying(4) not null
  , nyuryoku_flg character varying(1) not null
  , shiwake_shusei_kengen_flg character varying(1) default '0' not null
  , bumon_tsukekae_kengen_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint zaimu_kyoten_nyuryoku_user_info_PKEY primary key (denpyou_kbn,zaimu_kyoten_nyuryoku_pattern_no,user_id)
);
comment on table zaimu_kyoten_nyuryoku_user_info is '財務拠点入力ユーザー';
comment on column zaimu_kyoten_nyuryoku_user_info.denpyou_kbn is '伝票区分';
comment on column zaimu_kyoten_nyuryoku_user_info.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column zaimu_kyoten_nyuryoku_user_info.user_id is 'ユーザーID';
comment on column zaimu_kyoten_nyuryoku_user_info.open21_user_id is 'OPEN21ユーザーID';
comment on column zaimu_kyoten_nyuryoku_user_info.nyuryoku_flg is '入力者フラグ';
comment on column zaimu_kyoten_nyuryoku_user_info.shiwake_shusei_kengen_flg is '仕訳修正権限フラグ';
comment on column zaimu_kyoten_nyuryoku_user_info.bumon_tsukekae_kengen_flg is '部門付替権限フラグ';
comment on column zaimu_kyoten_nyuryoku_user_info.touroku_user_id is '登録ユーザーID';
comment on column zaimu_kyoten_nyuryoku_user_info.touroku_time is '登録日時';
comment on column zaimu_kyoten_nyuryoku_user_info.koushin_user_id is '更新ユーザーID';
comment on column zaimu_kyoten_nyuryoku_user_info.koushin_time is '更新日時';


-- 現預金出納帳テーブル

create table genyokin_suitouchou (
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
  , constraint genyokin_suitouchou_PKEY primary key (denpyou_id)
);

create table genyokin_suitouchou_meisai (
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
  , constraint genyokin_suitouchou_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

comment on table genyokin_suitouchou is '現預金出納帳';
comment on column genyokin_suitouchou.denpyou_id is '伝票ID';
comment on column genyokin_suitouchou.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column genyokin_suitouchou.denpyou_date is '伝票日付';
comment on column genyokin_suitouchou.nyukin_goukei is '入金合計';
comment on column genyokin_suitouchou.shukkin_goukei is '出金合計';
comment on column genyokin_suitouchou.toujitsu_zandaka is '当日残高';
comment on column genyokin_suitouchou.suitou_kamoku_cd is '出納科目コード';
comment on column genyokin_suitouchou.suitou_kamoku_name is '出納科目名';
comment on column genyokin_suitouchou.suitou_kamoku_edaban_cd is '出納科目枝番コード';
comment on column genyokin_suitouchou.suitou_kamoku_edaban_name is '出納科目枝番名';
comment on column genyokin_suitouchou.suitou_futan_bumon_cd is '出納負担部門コード';
comment on column genyokin_suitouchou.suitou_futan_bumon_name is '出納負担部門名';
comment on column genyokin_suitouchou.bikou is '備考';
comment on column genyokin_suitouchou.touroku_user_id is '登録ユーザーID';
comment on column genyokin_suitouchou.touroku_time is '登録日時';
comment on column genyokin_suitouchou.koushin_user_id is '更新ユーザーID';
comment on column genyokin_suitouchou.koushin_time is '更新日時';

comment on table genyokin_suitouchou_meisai is '現預金出納帳明細';
comment on column genyokin_suitouchou_meisai.denpyou_id is '伝票ID';
comment on column genyokin_suitouchou_meisai.denpyou_edano is '伝票枝番号';
comment on column genyokin_suitouchou_meisai.shiwake_edano is '仕訳枝番号';
comment on column genyokin_suitouchou_meisai.denpyou_date is '伝票日付';
comment on column genyokin_suitouchou_meisai.serial_no is 'シリアル番号';
comment on column genyokin_suitouchou_meisai.torihiki_name is '取引名';
comment on column genyokin_suitouchou_meisai.tekiyou is '摘要';
comment on column genyokin_suitouchou_meisai.nyukin_goukei is '入金';
comment on column genyokin_suitouchou_meisai.shukkin_goukei is '出金';
comment on column genyokin_suitouchou_meisai.aite_kazei_kbn is '課税区分';
comment on column genyokin_suitouchou_meisai.aite_zeiritsu is '税率';
comment on column genyokin_suitouchou_meisai.aite_keigen_zeiritsu_kbn is '軽減税率区分';
comment on column genyokin_suitouchou_meisai.fusen_color is '付箋カラー';
comment on column genyokin_suitouchou_meisai.meisai_touroku_user_id is '明細登録ユーザーID';
comment on column genyokin_suitouchou_meisai.meisai_touroku_shain_no is '明細登録ユーザー社員No';
comment on column genyokin_suitouchou_meisai.meisai_touroku_user_sei is '明細登録ユーザー姓';
comment on column genyokin_suitouchou_meisai.meisai_touroku_user_mei is '明細登録ユーザー名';
comment on column genyokin_suitouchou_meisai.tenpu_file_edano is '添付ファイル枝番';
comment on column genyokin_suitouchou_meisai.shiwake_serial_no is '仕訳データシリアルNo';
comment on column genyokin_suitouchou_meisai.touroku_user_id is '登録ユーザーID';
comment on column genyokin_suitouchou_meisai.touroku_time is '登録日時';
comment on column genyokin_suitouchou_meisai.koushin_user_id is '更新ユーザーID';
comment on column genyokin_suitouchou_meisai.koushin_time is '更新日時';


-- 伝票一覧財務拠点
create table denpyou_ichiran_kyoten (
  denpyou_id character varying(19) not null
  , denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , serial_no bigint
  , denpyou_joutai character varying(2) not null
  , name character varying not null
  , denpyou_shubetsu_url character varying(240) not null
  , touroku_time timestamp without time zone
  , koushin_time timestamp without time zone
  , shouninbi timestamp without time zone
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
  , gen_gyoumu_role_id character varying not null
  , gen_gyoumu_role_name character varying not null
  , gen_name character varying not null
  , shiwake_status character varying not null
  , kingaku numeric(15)
  , denpyou_date date
  , constraint denpyou_ichiran_kyoten_PKEY primary key (denpyou_id)
);

comment on table denpyou_ichiran_kyoten is '伝票一覧(財務拠点)';
comment on column denpyou_ichiran_kyoten.denpyou_id is '伝票ID';
comment on column denpyou_ichiran_kyoten.denpyou_kbn is '伝票区分';
comment on column denpyou_ichiran_kyoten.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
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
comment on column denpyou_ichiran_kyoten.gen_gyoumu_role_id is '現在承認者業務ロールIDリスト';
comment on column denpyou_ichiran_kyoten.gen_gyoumu_role_name is '現在承認者業務ロール名';
comment on column denpyou_ichiran_kyoten.gen_name is '現在承認者名称';
comment on column denpyou_ichiran_kyoten.shiwake_status is '仕訳データ作成ステータス';
comment on column denpyou_ichiran_kyoten.kingaku is '金額';
comment on column denpyou_ichiran_kyoten.denpyou_date is '伝票日付';


-- 伝票種別一覧(財務拠点)
create table denpyou_shubetsu_ichiran_kyoten (
  denpyou_kbn character varying(4) not null
  , denpyou_shubetsu character varying(20) not null
  , denpyou_print_shubetsu character varying(20)
  , hyouji_jun integer not null
  , gyoumu_shubetsu character varying(20) not null
  , naiyou character varying(160) not null
  , denpyou_shubetsu_url character varying(240) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , kanren_sentaku_flg character varying(1) not null
  , kanren_hyouji_flg character varying(1) not null
  , denpyou_print_flg character varying(1) not null
  , shounin_jyoukyou_print_flg character varying(1) default '0' not null
  , shinsei_shori_kengen_name character varying(6) not null
  , shiiresaki_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint denpyou_shubetsu_ichiran_kyoten_PKEY primary key (denpyou_kbn)
);

comment on table denpyou_shubetsu_ichiran_kyoten is '伝票種別一覧(財務拠点)';
comment on column denpyou_shubetsu_ichiran_kyoten.denpyou_kbn is '伝票区分';
comment on column denpyou_shubetsu_ichiran_kyoten.denpyou_shubetsu is '伝票種別';
comment on column denpyou_shubetsu_ichiran_kyoten.denpyou_print_shubetsu is '伝票種別（帳票）';
comment on column denpyou_shubetsu_ichiran_kyoten.hyouji_jun is '表示順';
comment on column denpyou_shubetsu_ichiran_kyoten.gyoumu_shubetsu is '業務種別';
comment on column denpyou_shubetsu_ichiran_kyoten.naiyou is '内容（伝票）';
comment on column denpyou_shubetsu_ichiran_kyoten.denpyou_shubetsu_url is '伝票種別URL';
comment on column denpyou_shubetsu_ichiran_kyoten.yuukou_kigen_from is '有効期限開始日';
comment on column denpyou_shubetsu_ichiran_kyoten.yuukou_kigen_to is '有効期限終了日';
comment on column denpyou_shubetsu_ichiran_kyoten.kanren_sentaku_flg is '関連伝票選択フラグ';
comment on column denpyou_shubetsu_ichiran_kyoten.kanren_hyouji_flg is '関連伝票入力欄表示フラグ';
comment on column denpyou_shubetsu_ichiran_kyoten.denpyou_print_flg is '申請時帳票出力フラグ';
comment on column denpyou_shubetsu_ichiran_kyoten.shounin_jyoukyou_print_flg is '承認状況欄印刷フラグ';
comment on column denpyou_shubetsu_ichiran_kyoten.shinsei_shori_kengen_name is '申請処理権限名';
comment on column denpyou_shubetsu_ichiran_kyoten.shiiresaki_flg is '仕入先フラグ';
comment on column denpyou_shubetsu_ichiran_kyoten.touroku_user_id is '登録ユーザーID';
comment on column denpyou_shubetsu_ichiran_kyoten.touroku_time is '登録日時';
comment on column denpyou_shubetsu_ichiran_kyoten.koushin_user_id is '更新ユーザーID';
comment on column denpyou_shubetsu_ichiran_kyoten.koushin_time is '更新日時';

\copy denpyou_shubetsu_ichiran_kyoten FROM '.\files\csv\denpyou_shubetsu_ichiran_kyoten.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--仕訳関連
create table teikei_shiwake (
  user_id character varying(30) not null
  , teikei_shiwake_pattern_no integer not null
  , teikei_shiwake_pattern_name character varying(60) not null
  , kari_kingaku_goukei numeric(15)
  , kashi_kingaku_goukei numeric(15)
  , denpyou_date date
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
  , denpyou_kbn character varying(4) not null
  , zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint teikei_shiwake_PKEY primary key (user_id,teikei_shiwake_pattern_no)
);

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
  , gyou_kugiri character varying(1) not null
  , zaimu_kyoten_nyuryoku_haifu_pattern_no character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp not null
  , constraint teikei_shiwake_meisai_PKEY primary key (user_id,teikei_shiwake_pattern_no,teikei_shiwake_edano)
);

create table zaimu_kyoten_shiwake_de3 (
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
  , constraint zaimu_kyoten_shiwake_de3_PKEY primary key (serial_no)
);

create table zaimu_kyoten_shiwake_sias (
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
  , constraint zaimu_kyoten_shiwake_sias_PKEY primary key (serial_no)
);

comment on table teikei_shiwake is '定型仕訳';
comment on column teikei_shiwake.user_id is 'ユーザーID';
comment on column teikei_shiwake.teikei_shiwake_pattern_no is '定型仕訳パターン番号';
comment on column teikei_shiwake.teikei_shiwake_pattern_name is '定型仕訳パターン名';
comment on column teikei_shiwake.kari_kingaku_goukei is '借方金額合計';
comment on column teikei_shiwake.kashi_kingaku_goukei is '貸方金額合計';
comment on column teikei_shiwake.denpyou_date is '伝票日付';
comment on column teikei_shiwake.hf1_cd is 'HF1コード';
comment on column teikei_shiwake.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column teikei_shiwake.hf2_cd is 'HF2コード';
comment on column teikei_shiwake.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column teikei_shiwake.hf3_cd is 'HF3コード';
comment on column teikei_shiwake.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column teikei_shiwake.hf4_cd is 'HF4コード';
comment on column teikei_shiwake.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column teikei_shiwake.hf5_cd is 'HF5コード';
comment on column teikei_shiwake.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column teikei_shiwake.hf6_cd is 'HF6コード';
comment on column teikei_shiwake.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column teikei_shiwake.hf7_cd is 'HF7コード';
comment on column teikei_shiwake.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column teikei_shiwake.hf8_cd is 'HF8コード';
comment on column teikei_shiwake.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column teikei_shiwake.hf9_cd is 'HF9コード';
comment on column teikei_shiwake.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column teikei_shiwake.hf10_cd is 'HF10コード';
comment on column teikei_shiwake.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column teikei_shiwake.bikou is '備考';
comment on column teikei_shiwake.denpyou_kbn is '伝票区分';
comment on column teikei_shiwake.zaimu_kyoten_nyuryoku_pattern_no is '財務拠点入力パターンNo';
comment on column teikei_shiwake.touroku_user_id is '登録ユーザーID';
comment on column teikei_shiwake.touroku_time is '登録日時';
comment on column teikei_shiwake.koushin_user_id is '更新ユーザーID';
comment on column teikei_shiwake.koushin_time is '更新日時';

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
comment on column teikei_shiwake_meisai.gyou_kugiri is '行区切り';
comment on column teikei_shiwake_meisai.zaimu_kyoten_nyuryoku_haifu_pattern_no is '財務拠点入力配賦パターンNo';
comment on column teikei_shiwake_meisai.touroku_user_id is '登録ユーザーID';
comment on column teikei_shiwake_meisai.touroku_time is '登録日時';
comment on column teikei_shiwake_meisai.koushin_user_id is '更新ユーザーID';
comment on column teikei_shiwake_meisai.koushin_time is '更新日時';

comment on table zaimu_kyoten_shiwake_de3 is '財務拠点仕訳抽出(de3)';
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

comment on table zaimu_kyoten_shiwake_sias is '財務拠点仕訳抽出(SIAS)';
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


-- 承認ルート
create table shounin_route_kyoten (
  denpyou_id character varying(19) not null
  , edano integer not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , dairi_shounin_user_id character varying(30) not null
  , dairi_shounin_user_full_name character varying(50) not null
  , genzai_flg character varying(1) not null
  , joukyou_edano integer
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shounin_route_kyoten_PKEY primary key (denpyou_id,edano)
);
comment on table shounin_route_kyoten is '承認ルート(財務拠点)';
comment on column shounin_route_kyoten.denpyou_id is '伝票ID';
comment on column shounin_route_kyoten.edano is '枝番号';
comment on column shounin_route_kyoten.user_id is 'ユーザーID';
comment on column shounin_route_kyoten.user_full_name is 'ユーザーフル名';
comment on column shounin_route_kyoten.dairi_shounin_user_id is '代理承認者ユーザーID';
comment on column shounin_route_kyoten.dairi_shounin_user_full_name is '代理承認者ユーザーフル名';
comment on column shounin_route_kyoten.genzai_flg is '現在フラグ';
comment on column shounin_route_kyoten.joukyou_edano is '承認状況枝番';
comment on column shounin_route_kyoten.touroku_user_id is '登録ユーザーID';
comment on column shounin_route_kyoten.touroku_time is '登録日時';
comment on column shounin_route_kyoten.koushin_user_id is '更新ユーザーID';
comment on column shounin_route_kyoten.koushin_time is '更新日時';


-- 承認状況
create table shounin_joukyou_kyoten (
  denpyou_id character varying(19) not null
  , edano integer not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , joukyou_cd character varying(1) not null
  , joukyou character varying not null
  , comment character varying not null
  , shounin_route_edano integer
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shounin_joukyou_kyoten_PKEY primary key (denpyou_id,edano)
);

comment on table shounin_joukyou_kyoten is '承認状況(財務拠点)';
comment on column shounin_joukyou_kyoten.denpyou_id is '伝票ID';
comment on column shounin_joukyou_kyoten.edano is '枝番号';
comment on column shounin_joukyou_kyoten.user_id is 'ユーザーID';
comment on column shounin_joukyou_kyoten.user_full_name is 'ユーザーフル名';
comment on column shounin_joukyou_kyoten.joukyou_cd is '状況コード';
comment on column shounin_joukyou_kyoten.joukyou is '状況';
comment on column shounin_joukyou_kyoten.comment is 'コメント';
comment on column shounin_joukyou_kyoten.shounin_route_edano is '承認ルート枝番号';
comment on column shounin_joukyou_kyoten.touroku_user_id is '登録ユーザーID';
comment on column shounin_joukyou_kyoten.touroku_time is '登録日時';
comment on column shounin_joukyou_kyoten.koushin_user_id is '更新ユーザーID';
comment on column shounin_joukyou_kyoten.koushin_time is '更新日時';


commit;
