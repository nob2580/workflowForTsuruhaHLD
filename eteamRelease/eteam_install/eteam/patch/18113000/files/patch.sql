SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


--  開発計画_17 0639 設定情報データ変更 ※hyouji_jun900以降はカスタマイズ領域
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

-- 画面権限制御の追加
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- 開発計画_17 内部コード設定
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- 支払依頼申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- 請求書払申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

-- 0639 画面項目制御の修正
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

-- 0651,0653 『支払依頼書』となっている表示を『支払依頼申請』に修正
UPDATE gamen_kengen_seigyo 
SET 
	gamen_name = '支払依頼申請'
WHERE gamen_id = 'ShiharaiIrai';

UPDATE denpyou_shubetsu_ichiran 
SET 
	denpyou_shubetsu = '支払依頼申請'
WHERE denpyou_shubetsu = '支払依頼書'
AND denpyou_kbn = 'A013';

UPDATE denpyou_shubetsu_ichiran 
SET 
	denpyou_karibarai_nashi_shubetsu = '支払依頼申請'
WHERE denpyou_karibarai_nashi_shubetsu = '支払依頼書'
AND denpyou_kbn = 'A013';

UPDATE denpyou_shubetsu_ichiran 
SET 
	denpyou_print_shubetsu = '支払依頼申請'
WHERE denpyou_print_shubetsu = '支払依頼書'
AND denpyou_kbn = 'A013';

UPDATE denpyou_shubetsu_ichiran 
SET 
	denpyou_print_karibarai_nashi_shubetsu = '支払依頼申請'
WHERE denpyou_print_karibarai_nashi_shubetsu = '支払依頼書'
AND denpyou_kbn = 'A013';

-- 駅マスターデータ更新
DELETE FROM eki_master;
\copy eki_master FROM '.\files\csv\eki_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--バス路線名マスター 新規作成
CREATE TABLE BUS_LINE_MASTER
(
	LINE_CD VARCHAR(6) NOT NULL,
	LINE_NAME VARCHAR NOT NULL,
	PRIMARY KEY (LINE_CD)
) WITHOUT OIDS;

COMMENT ON TABLE BUS_LINE_MASTER IS 'バス路線名マスター';
COMMENT ON COLUMN BUS_LINE_MASTER.LINE_CD IS 'バス路線コード';
COMMENT ON COLUMN BUS_LINE_MASTER.LINE_NAME IS '路線名';

-- バス路線名マスターデータ更新
DELETE FROM BUS_LINE_MASTER;
\copy BUS_LINE_MASTER FROM '.\files\csv\bus_line_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--ICカード利用履歴 定義更新
ALTER TABLE IC_CARD_RIREKI RENAME TO IC_CARD_RIREKI_OLD;
CREATE TABLE IC_CARD_RIREKI
(
	IC_CARD_NO VARCHAR(16) NOT NULL,
	IC_CARD_SEQUENCE_NO VARCHAR(10) NOT NULL,
	IC_CARD_RIYOUBI DATE NOT NULL,
	TANMATU_CD VARCHAR(6) NOT NULL,
	LINE_CD_FROM VARCHAR(6) NOT NULL,
	LINE_NAME_FROM VARCHAR NOT NULL,
	EKI_CD_FROM VARCHAR(6) NOT NULL,
	EKI_NAME_FROM VARCHAR NOT NULL,
	LINE_CD_TO VARCHAR(6) NOT NULL,
	LINE_NAME_TO VARCHAR NOT NULL,
	EKI_CD_TO VARCHAR(6) NOT NULL,
	EKI_NAME_TO VARCHAR NOT NULL,
	KINGAKU DECIMAL(15) NOT NULL,
	USER_ID VARCHAR(30) NOT NULL,
	JYOGAI_FLG VARCHAR(1) NOT NULL,
	PRIMARY KEY (IC_CARD_NO, IC_CARD_SEQUENCE_NO)
) WITHOUT OIDS;
COMMENT ON TABLE IC_CARD_RIREKI IS 'ICカード利用履歴';
COMMENT ON COLUMN IC_CARD_RIREKI.IC_CARD_NO IS 'ICカード番号';
COMMENT ON COLUMN IC_CARD_RIREKI.IC_CARD_SEQUENCE_NO IS 'ICカードシーケンス番号';
COMMENT ON COLUMN IC_CARD_RIREKI.IC_CARD_RIYOUBI IS 'ICカード利用日';
COMMENT ON COLUMN IC_CARD_RIREKI.TANMATU_CD IS '端末種別';
COMMENT ON COLUMN IC_CARD_RIREKI.LINE_CD_FROM IS '路線コード（FROM）';
COMMENT ON COLUMN IC_CARD_RIREKI.LINE_NAME_FROM IS '路線名（FROM）';
COMMENT ON COLUMN IC_CARD_RIREKI.EKI_CD_FROM IS '駅コード（FROM）';
COMMENT ON COLUMN IC_CARD_RIREKI.EKI_NAME_FROM IS '駅名（FROM）';
COMMENT ON COLUMN IC_CARD_RIREKI.LINE_CD_TO IS '路線コード（TO）';
COMMENT ON COLUMN IC_CARD_RIREKI.LINE_NAME_TO IS '路線名（TO）';
COMMENT ON COLUMN IC_CARD_RIREKI.EKI_CD_TO IS '駅コード（TO）';
COMMENT ON COLUMN IC_CARD_RIREKI.EKI_NAME_TO IS '駅名（TO）';
COMMENT ON COLUMN IC_CARD_RIREKI.KINGAKU IS '金額';
COMMENT ON COLUMN IC_CARD_RIREKI.USER_ID IS 'ユーザーID';
COMMENT ON COLUMN IC_CARD_RIREKI.JYOGAI_FLG IS '除外フラグ';
INSERT INTO IC_CARD_RIREKI
SELECT
    IC_CARD_NO,
    IC_CARD_SEQUENCE_NO,
    IC_CARD_RIYOUBI,
    '', --TANMATU_CD
    LINE_CD_FROM,
    LINE_NAME_FROM,
    EKI_CD_FROM,
    EKI_NAME_FROM,
    LINE_CD_TO,
    LINE_NAME_TO,
    EKI_CD_TO,
    EKI_NAME_TO,
    KINGAKU,
    USER_ID,
    '0' --JYOGAI_FLG
FROM IC_CARD_RIREKI_OLD;
DROP TABLE IC_CARD_RIREKI_OLD;

--e文書データ 定義更新
alter table ebunsho_data rename to ebunsho_data_old;
create table ebunsho_data (
  ebunsho_no character varying(19) not null
  , ebunsho_edano integer not null
  , ebunsho_shubetsu integer not null
  , ebunsho_nengappi date not null
  , ebunsho_kingaku numeric(15) not null
  , ebunsho_hakkousha character varying(20) not null
  , ebunsho_hinmei character varying(50) not null
  , primary key (ebunsho_no,ebunsho_edano)
) WITHOUT OIDS;
comment on table ebunsho_data is 'e文書データ';
comment on column ebunsho_data.ebunsho_no is 'e文書番号';
comment on column ebunsho_data.ebunsho_edano is 'e文書枝番号';
comment on column ebunsho_data.ebunsho_shubetsu is 'e文書種別';
comment on column ebunsho_data.ebunsho_nengappi is 'e文書年月日';
comment on column ebunsho_data.ebunsho_kingaku is 'e文書金額';
comment on column ebunsho_data.ebunsho_hakkousha is 'e文書発行者';
comment on column ebunsho_data.ebunsho_hinmei is 'e文書品名';

INSERT INTO ebunsho_data
SELECT 
  ebunsho_no
  , ebunsho_edano
  , ebunsho_shubetsu
  , ebunsho_nengappi
  , ebunsho_kingaku
  , ebunsho_hakkousha
  , '' --ebunsho_hinmei
FROM ebunsho_data_old;
DROP TABLE ebunsho_data_old;


-- 0639 支払依頼 定義更新
alter table shiharai_irai rename to shiharai_irai_old;

create table shiharai_irai (
  denpyou_id character varying(19) not null
  , keijoubi date not null
  , yoteibi date not null
  , shiharaibi date
  , shiharai_kijitsu date
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , ichigensaki_torihikisaki_name character varying not null
  , edi character varying(20) not null
  , shiharai_goukei numeric(15) not null
  , sousai_goukei numeric(15) not null
  , sashihiki_shiharai numeric(15) not null
  , manekin_gensen numeric(15) not null
  , shouhyou_shorui_flg character varying(1) not null
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
  , shiharai_houhou character varying(1) not null
  , shiharai_shubetsu character varying(1) not null
  , furikomi_ginkou_cd character varying(4) not null
  , furikomi_ginkou_name character varying(30) not null
  , furikomi_ginkou_shiten_cd character varying(3) not null
  , furikomi_ginkou_shiten_name character varying(30) not null
  , yokin_shubetsu character varying(1) not null
  , kouza_bangou character varying(7) not null
  , kouza_meiginin character varying(60) not null
  , tesuuryou character varying(1) not null
  , hosoku character varying(240) not null
  , gyaku_shiwake_flg character varying(1) default '0' not null
  , shutsuryoku_flg character varying(1) default '0' not null
  , csv_upload_flg character varying(1) default '0' not null
  , hassei_shubetsu character varying default '経費' not null
  , saimu_made_flg character varying(1) default '0' not null
  , fb_made_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id)
) WITHOUT OIDS;

comment on table shiharai_irai is '支払依頼';
comment on column shiharai_irai.denpyou_id is '伝票ID';
comment on column shiharai_irai.keijoubi is '計上日';
comment on column shiharai_irai.yoteibi is '予定日';
comment on column shiharai_irai.shiharaibi is '支払日';
comment on column shiharai_irai.shiharai_kijitsu is '支払期日';
comment on column shiharai_irai.torihikisaki_cd is '取引先コード';
comment on column shiharai_irai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column shiharai_irai.ichigensaki_torihikisaki_name is '一見先取引先名';
comment on column shiharai_irai.edi is 'EDI';
comment on column shiharai_irai.shiharai_goukei is '支払合計';
comment on column shiharai_irai.sousai_goukei is '相殺合計';
comment on column shiharai_irai.sashihiki_shiharai is '差引支払額';
comment on column shiharai_irai.manekin_gensen is '控除項目';
comment on column shiharai_irai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column shiharai_irai.hf1_cd is 'HF1コード';
comment on column shiharai_irai.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column shiharai_irai.hf2_cd is 'HF2コード';
comment on column shiharai_irai.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column shiharai_irai.hf3_cd is 'HF3コード';
comment on column shiharai_irai.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column shiharai_irai.hf4_cd is 'HF4コード';
comment on column shiharai_irai.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column shiharai_irai.hf5_cd is 'HF5コード';
comment on column shiharai_irai.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column shiharai_irai.hf6_cd is 'HF6コード';
comment on column shiharai_irai.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column shiharai_irai.hf7_cd is 'HF7コード';
comment on column shiharai_irai.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column shiharai_irai.hf8_cd is 'HF8コード';
comment on column shiharai_irai.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column shiharai_irai.hf9_cd is 'HF9コード';
comment on column shiharai_irai.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column shiharai_irai.hf10_cd is 'HF10コード';
comment on column shiharai_irai.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column shiharai_irai.shiharai_houhou is '支払方法';
comment on column shiharai_irai.shiharai_shubetsu is '支払種別';
comment on column shiharai_irai.furikomi_ginkou_cd is '振込銀行コード';
comment on column shiharai_irai.furikomi_ginkou_name is '振込銀行名称';
comment on column shiharai_irai.furikomi_ginkou_shiten_cd is '振込銀行支店コード';
comment on column shiharai_irai.furikomi_ginkou_shiten_name is '振込銀行支店名称';
comment on column shiharai_irai.yokin_shubetsu is '預金種別';
comment on column shiharai_irai.kouza_bangou is '口座番号';
comment on column shiharai_irai.kouza_meiginin is '口座名義人';
comment on column shiharai_irai.tesuuryou is '手数料';
comment on column shiharai_irai.hosoku is '補足';
comment on column shiharai_irai.gyaku_shiwake_flg is '逆仕訳フラグ';
comment on column shiharai_irai.shutsuryoku_flg is '出力フラグ';
comment on column shiharai_irai.csv_upload_flg is 'CSVアップロードフラグ';
comment on column shiharai_irai.hassei_shubetsu is '発生種別';
comment on column shiharai_irai.saimu_made_flg is '債務支払データ作成済フラグ';
comment on column shiharai_irai.fb_made_flg is 'FBデータ作成済フラグ';
comment on column shiharai_irai.touroku_user_id is '登録ユーザーID';
comment on column shiharai_irai.touroku_time is '登録日時';
comment on column shiharai_irai.koushin_user_id is '更新ユーザーID';
comment on column shiharai_irai.koushin_time is '更新日時';

INSERT INTO shiharai_irai
SELECT 
  denpyou_id
  , keijoubi
  , yoteibi
  , shiharaibi
  , shiharai_kijitsu
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , ichigensaki_torihikisaki_name
  , edi
  , shiharai_goukei
  , sousai_goukei
  , sashihiki_shiharai
  , manekin_gensen
  , '0' --shouhyou_shorui_flg
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
  , shiharai_houhou
  , shiharai_shubetsu
  , furikomi_ginkou_cd
  , furikomi_ginkou_name
  , furikomi_ginkou_shiten_cd
  , furikomi_ginkou_shiten_name
  , yokin_shubetsu
  , kouza_bangou
  , kouza_meiginin
  , tesuuryou
  , hosoku
  , gyaku_shiwake_flg
  , shutsuryoku_flg
  , csv_upload_flg
  , hassei_shubetsu
  , saimu_made_flg
  , fb_made_flg
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM shiharai_irai_old;
DROP TABLE shiharai_irai_old;

-- 0485 予算執行処理年月
create table yosan_shikkou_shori_nengetsu (
  from_nengetsu character varying(6) not null
  , to_nengetsu character varying(6) not null
  , primary key (from_nengetsu,to_nengetsu)
);
comment on table yosan_shikkou_shori_nengetsu is '予算執行処理年月';
comment on column yosan_shikkou_shori_nengetsu.from_nengetsu is '開始年月';
comment on column yosan_shikkou_shori_nengetsu.to_nengetsu is '終了年月';

-- 0485 執行状況一覧情報
create table shikkou_joukyou_ichiran_jouhou (
  user_id character varying(30) not null
  , yosan_tani character varying(1) not null
  , primary key (user_id)
);
comment on table shikkou_joukyou_ichiran_jouhou is '執行状況一覧情報';
comment on column shikkou_joukyou_ichiran_jouhou.user_id is 'ユーザーID';
comment on column shikkou_joukyou_ichiran_jouhou.yosan_tani is '予算単位';

-- 0485 伝票
ALTER TABLE denpyou RENAME TO denpyou_old;
create table denpyou (
  denpyou_id character varying(19) not null
  , denpyou_kbn character varying(4) not null
  , denpyou_joutai character varying(2) not null
  , sanshou_denpyou_id character varying(19) not null
  , daihyou_futan_bumon_cd character varying(8) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , serial_no bigint not null
  , chuushutsu_zumi_flg character varying(1) default '0' not null
  , shounin_route_henkou_flg character varying(1) default '0' not null
  , maruhi_flg character varying(1) default '0' not null
  , yosan_check_nengetsu character varying(6) default '' not null
  , primary key (denpyou_id)
);
comment on table denpyou is '伝票';
comment on column denpyou.denpyou_id is '伝票ID';
comment on column denpyou.denpyou_kbn is '伝票区分';
comment on column denpyou.denpyou_joutai is '伝票状態';
comment on column denpyou.sanshou_denpyou_id is '参照伝票ID';
comment on column denpyou.daihyou_futan_bumon_cd is '代表負担部門コード';
comment on column denpyou.touroku_user_id is '登録ユーザーID';
comment on column denpyou.touroku_time is '登録日時';
comment on column denpyou.koushin_user_id is '更新ユーザーID';
comment on column denpyou.koushin_time is '更新日時';
comment on column denpyou.serial_no is 'シリアル番号';
comment on column denpyou.chuushutsu_zumi_flg is '抽出済フラグ';
comment on column denpyou.shounin_route_henkou_flg is '承認ルート変更フラグ';
comment on column denpyou.maruhi_flg is 'マル秘文書フラグ';
comment on column denpyou.yosan_check_nengetsu is '予算執行対象月';
INSERT INTO denpyou
SELECT
    denpyou_id
  , denpyou_kbn
  , denpyou_joutai
  , sanshou_denpyou_id
  , daihyou_futan_bumon_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
  , serial_no
  , chuushutsu_zumi_flg
  , shounin_route_henkou_flg
  , maruhi_flg
  , ''   -- yosan_check_nengetsu
FROM denpyou_old;
DROP TABLE denpyou_old;

-- 0485 伝票一覧
ALTER TABLE denpyou_ichiran RENAME TO denpyou_ichiran_old;
create table denpyou_ichiran (
  denpyou_id character varying(19) not null
  , name character varying not null
  , denpyou_kbn character varying(4)
  , jisshi_kian_bangou character varying(15) not null
  , shishutsu_kian_bangou character varying(15) not null
  , yosan_shikkou_taishou character varying not null
  , yosan_check_nengetsu character varying(6) not null
  , serial_no bigint
  , denpyou_shubetsu_url character varying(240) not null
  , touroku_time timestamp without time zone
  , bumon_full_name character varying not null
  , user_full_name character varying(50) not null
  , user_id character varying(30) not null
  , denpyou_joutai character varying(2) not null
  , koushin_time timestamp without time zone
  , shouninbi timestamp without time zone
  , maruhi_flg character varying(1) not null
  , all_cnt bigint
  , cur_cnt bigint
  , zan_cnt bigint
  , gen_bumon_full_name character varying not null
  , gen_user_full_name character varying not null
  , gen_gyoumu_role_name character varying not null
  , gen_name character varying not null
  , version integer
  , kingaku numeric(15)
  , gaika character varying not null
  , houjin_kingaku numeric(15)
  , tehai_kingaku numeric(15)
  , torihikisaki1 character varying not null
  , shiharaibi date
  , shiharaikiboubi date
  , shiharaihouhou character varying not null
  , sashihiki_shikyuu_kingaku numeric(15)
  , keijoubi date
  , shiwakekeijoubi date
  , seisan_yoteibi date
  , karibarai_denpyou_id character varying(19) not null
  , houmonsaki character varying(120) not null
  , mokuteki character varying(240) not null
  , kenmei character varying not null
  , naiyou character varying not null
  , user_sei character varying(10) not null
  , user_mei character varying(10) not null
  , seisankikan_from date
  , seisankikan_to date
  , gen_user_id character varying not null
  , gen_gyoumu_role_id character varying not null
  , kian_bangou_unyou_flg character varying(1) not null
  , yosan_shikkou_taishou_cd character varying not null
  , kian_syuryou_flg character varying not null
  , kari_futan_bumon_cd character varying not null
  , kari_futan_bumon_name character varying not null
  , kari_kamoku_cd character varying not null
  , kari_kamoku_name character varying not null
  , kari_torihikisaki_cd character varying not null
  , kari_torihikisaki_name character varying not null
  , kashi_futan_bumon_cd character varying not null
  , kashi_futan_bumon_name character varying not null
  , kashi_kamoku_cd character varying not null
  , kashi_kamoku_name character varying not null
  , kashi_torihikisaki_cd character varying not null
  , kashi_torihikisaki_name character varying not null
  , tekiyou character varying not null
  , houjin_card_use character varying(1) not null
  , kaisha_tehai_use character varying(1) not null
  , ryoushuusho_exist character varying not null
  , miseisan_karibarai_exist character varying(1) not null
  , miseisan_ukagai_exist character varying(1) not null
  , shiwake_status character varying not null
  , fb_status character varying not null
  , jisshi_nendo character varying(4) not null
  , shishutsu_nendo character varying(4) not null
  , bumon_cd character varying(8) not null
  , kian_bangou_input character varying(1) not null
  , primary key (denpyou_id)
);
comment on table denpyou_ichiran is '伝票一覧';
comment on column denpyou_ichiran.denpyou_id is '伝票ID';
comment on column denpyou_ichiran.name is 'ステータス';
comment on column denpyou_ichiran.denpyou_kbn is '伝票区分';
comment on column denpyou_ichiran.jisshi_kian_bangou is '実施起案番号';
comment on column denpyou_ichiran.shishutsu_kian_bangou is '支出起案番号';
comment on column denpyou_ichiran.yosan_shikkou_taishou is '予算執行対象';
comment on column denpyou_ichiran.yosan_check_nengetsu is '予算執行対象月';
comment on column denpyou_ichiran.serial_no is 'シリアル番号';
comment on column denpyou_ichiran.denpyou_shubetsu_url is '伝票種別URL';
comment on column denpyou_ichiran.touroku_time is '登録日時';
comment on column denpyou_ichiran.bumon_full_name is '部門フル名';
comment on column denpyou_ichiran.user_full_name is 'ユーザーフル名';
comment on column denpyou_ichiran.user_id is 'ユーザーID';
comment on column denpyou_ichiran.denpyou_joutai is '伝票状態';
comment on column denpyou_ichiran.koushin_time is '更新日時';
comment on column denpyou_ichiran.shouninbi is '承認日';
comment on column denpyou_ichiran.maruhi_flg is 'マル秘文書フラグ';
comment on column denpyou_ichiran.all_cnt is '全承認人数カウント';
comment on column denpyou_ichiran.cur_cnt is '承認済人数カウント';
comment on column denpyou_ichiran.zan_cnt is '残り承認人数カウント';
comment on column denpyou_ichiran.gen_bumon_full_name is '現在承認者部門フル名';
comment on column denpyou_ichiran.gen_user_full_name is '現在承認者ユーザーフル名';
comment on column denpyou_ichiran.gen_gyoumu_role_name is '現在承認者業務ロール名';
comment on column denpyou_ichiran.gen_name is '現在承認者名称';
comment on column denpyou_ichiran.version is 'バージョン';
comment on column denpyou_ichiran.kingaku is '金額';
comment on column denpyou_ichiran.gaika is '外貨';
comment on column denpyou_ichiran.houjin_kingaku is '法人カード払金額';
comment on column denpyou_ichiran.tehai_kingaku is '会社手配金額';
comment on column denpyou_ichiran.torihikisaki1 is '取引先1';
comment on column denpyou_ichiran.shiharaibi is '支払日';
comment on column denpyou_ichiran.shiharaikiboubi is '支払希望日';
comment on column denpyou_ichiran.shiharaihouhou is '支払方法';
comment on column denpyou_ichiran.sashihiki_shikyuu_kingaku is '差引支給金額';
comment on column denpyou_ichiran.keijoubi is '計上日';
comment on column denpyou_ichiran.shiwakekeijoubi is '仕訳計上日';
comment on column denpyou_ichiran.seisan_yoteibi is '精算予定日';
comment on column denpyou_ichiran.karibarai_denpyou_id is '仮払伝票ID';
comment on column denpyou_ichiran.houmonsaki is '訪問先';
comment on column denpyou_ichiran.mokuteki is '目的';
comment on column denpyou_ichiran.kenmei is '件名';
comment on column denpyou_ichiran.naiyou is '内容';
comment on column denpyou_ichiran.user_sei is 'ユーザー姓';
comment on column denpyou_ichiran.user_mei is 'ユーザー名';
comment on column denpyou_ichiran.seisankikan_from is '精算期間開始日';
comment on column denpyou_ichiran.seisankikan_to is '精算期間終了日';
comment on column denpyou_ichiran.gen_user_id is '現在承認者ユーザーIDリスト';
comment on column denpyou_ichiran.gen_gyoumu_role_id is '現在承認者業務ロールIDリスト';
comment on column denpyou_ichiran.kian_bangou_unyou_flg is '起案番号運用フラグ';
comment on column denpyou_ichiran.yosan_shikkou_taishou_cd is '予算執行対象コード';
comment on column denpyou_ichiran.kian_syuryou_flg is '起案終了フラグ';
comment on column denpyou_ichiran.kari_futan_bumon_cd is '借方負担部門コード(一覧検索用)';
comment on column denpyou_ichiran.kari_futan_bumon_name is '借方負担部門名(一覧検索用)';
comment on column denpyou_ichiran.kari_kamoku_cd is '借方科目コード(一覧検索用)';
comment on column denpyou_ichiran.kari_kamoku_name is '借方科目名(一覧検索用)';
comment on column denpyou_ichiran.kari_torihikisaki_cd is '借方取引先コード(一覧検索用)';
comment on column denpyou_ichiran.kari_torihikisaki_name is '借方取引先名(一覧検索用)';
comment on column denpyou_ichiran.kashi_futan_bumon_cd is '貸方負担部門コード(一覧検索用)';
comment on column denpyou_ichiran.kashi_futan_bumon_name is '貸方負担部門名(一覧検索用)';
comment on column denpyou_ichiran.kashi_kamoku_cd is '貸方科目コード(一覧検索用)';
comment on column denpyou_ichiran.kashi_kamoku_name is '貸方科目名(一覧検索用)';
comment on column denpyou_ichiran.kashi_torihikisaki_cd is '貸方取引先コード(一覧検索用)';
comment on column denpyou_ichiran.kashi_torihikisaki_name is '貸方取引先名(一覧検索用)';
comment on column denpyou_ichiran.tekiyou is '摘要(一覧検索用)';
comment on column denpyou_ichiran.houjin_card_use is '法人カード使用フラグ';
comment on column denpyou_ichiran.kaisha_tehai_use is '会社手配使用フラグ';
comment on column denpyou_ichiran.ryoushuusho_exist is '領収書フラグ';
comment on column denpyou_ichiran.miseisan_karibarai_exist is '未精算仮払伝票フラグ';
comment on column denpyou_ichiran.miseisan_ukagai_exist is '未精算伺い伝票フラグ';
comment on column denpyou_ichiran.shiwake_status is '仕訳データ作成ステータス';
comment on column denpyou_ichiran.fb_status is 'FBデータ作成ステータス';
comment on column denpyou_ichiran.jisshi_nendo is '実施年度';
comment on column denpyou_ichiran.shishutsu_nendo is '支出年度';
comment on column denpyou_ichiran.bumon_cd is '部門コード';
comment on column denpyou_ichiran.kian_bangou_input is '起案番号採番フラグ';
INSERT INTO denpyou_ichiran
SELECT
    denpyou_id
  , name
  , denpyou_kbn
  , jisshi_kian_bangou
  , shishutsu_kian_bangou
  , yosan_shikkou_taishou
  , ''   -- yosan_check_nengetsu
  , serial_no
  , denpyou_shubetsu_url
  , touroku_time
  , bumon_full_name
  , user_full_name
  , user_id
  , denpyou_joutai
  , koushin_time
  , shouninbi
  , maruhi_flg
  , all_cnt
  , cur_cnt
  , zan_cnt
  , gen_bumon_full_name
  , gen_user_full_name
  , gen_gyoumu_role_name
  , gen_name
  , version
  , kingaku
  , gaika
  , houjin_kingaku
  , tehai_kingaku
  , torihikisaki1
  , shiharaibi
  , shiharaikiboubi
  , shiharaihouhou
  , sashihiki_shikyuu_kingaku
  , keijoubi
  , shiwakekeijoubi
  , seisan_yoteibi
  , karibarai_denpyou_id
  , houmonsaki
  , mokuteki
  , kenmei
  , naiyou
  , user_sei
  , user_mei
  , seisankikan_from
  , seisankikan_to
  , gen_user_id
  , gen_gyoumu_role_id
  , kian_bangou_unyou_flg
  , yosan_shikkou_taishou_cd
  , kian_syuryou_flg
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_torihikisaki_cd
  , kari_torihikisaki_name
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_torihikisaki_cd
  , kashi_torihikisaki_name
  , tekiyou
  , houjin_card_use
  , kaisha_tehai_use
  , ryoushuusho_exist
  , miseisan_karibarai_exist
  , miseisan_ukagai_exist
  , shiwake_status
  , fb_status
  , jisshi_nendo
  , shishutsu_nendo
  , bumon_cd
  , kian_bangou_input
FROM denpyou_ichiran_old;
DROP TABLE denpyou_ichiran_old;

-- UTF-8でShift-JIS化できない文字がある場合Shift-JISの対応文字に変換
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xe3809c', 'utf8'), '～');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xe28096', 'utf8'), '∥');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xe28892', 'utf8'), '－');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xc2a2', 'utf8'), '￠');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xc2a3', 'utf8'), '￡');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xc2ac', 'utf8'), '￢');
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xe28094', 'utf8'), '―');

-- LT環境DB構造の差分対応
DROP TABLE IF EXISTS furikae_old;

comment on table ryohi_karibarai_keihi_meisai is '旅費仮払経費明細';
comment on column ryohi_karibarai_keihi_meisai.denpyou_id is '伝票ID';
comment on column ryohi_karibarai_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column ryohi_karibarai_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column ryohi_karibarai_keihi_meisai.shiyoubi is '使用日';
comment on column ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column ryohi_karibarai_keihi_meisai.torihiki_name is '取引名';
comment on column ryohi_karibarai_keihi_meisai.tekiyou is '摘要';
comment on column ryohi_karibarai_keihi_meisai.zeiritsu is '税率';
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

COMMENT ON TABLE furikomi_bi_rule_hi IS '振込日ルール(日付)';






-- 以下Ver 18.07.23.09からのマージ

-- 定期情報に定期区間シリアライズデータを追加
CREATE FUNCTION seri(s TEXT, uid TEXT, dt DATE) RETURNS TEXT AS $$
DECLARE
    cnt integer;
BEGIN
    cnt := (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema=s AND table_name='teiki_jouhou_tmp' AND column_name='web_teiki_serialize_data');
    IF cnt >= 1 THEN
        RETURN (SELECT web_teiki_serialize_data FROM teiki_jouhou_tmp WHERE user_id=uid AND shiyou_kaishibi=dt);
    ELSE
        RETURN '';
    END IF;
END;
$$ LANGUAGE plpgsql immutable;

ALTER TABLE TEIKI_JOUHOU RENAME TO TEIKI_JOUHOU_TMP;
CREATE TABLE TEIKI_JOUHOU
(
    USER_ID VARCHAR(30) NOT NULL,
    SHIYOU_KAISHIBI DATE NOT NULL,
    SHIYOU_SHUURYOUBI DATE NOT NULL,
    INTRA_EKI_NAME VARCHAR(200),
    INTRA_ROSEN VARCHAR(200),
    WEB_TEIKI_KUKAN VARCHAR,
    WEB_TEIKI_SERIALIZE_DATA VARCHAR NOT NULL,
    TOUROKU_USER_ID VARCHAR(30) NOT NULL,
    TOUROKU_TIME TIMESTAMP NOT NULL,
    KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
    KOUSHIN_TIME TIMESTAMP NOT NULL,
    PRIMARY KEY (USER_ID, SHIYOU_KAISHIBI, SHIYOU_SHUURYOUBI)
) WITHOUT OIDS;
COMMENT ON TABLE TEIKI_JOUHOU IS '定期券情報';
COMMENT ON COLUMN TEIKI_JOUHOU.USER_ID IS 'ユーザーID';
COMMENT ON COLUMN TEIKI_JOUHOU.SHIYOU_KAISHIBI IS '使用開始日';
COMMENT ON COLUMN TEIKI_JOUHOU.SHIYOU_SHUURYOUBI IS '使用終了日';
COMMENT ON COLUMN TEIKI_JOUHOU.INTRA_EKI_NAME IS '定期区間駅名';
COMMENT ON COLUMN TEIKI_JOUHOU.INTRA_ROSEN IS '定期区間路線名';
COMMENT ON COLUMN TEIKI_JOUHOU.WEB_TEIKI_KUKAN IS '定期区間情報';
COMMENT ON COLUMN TEIKI_JOUHOU.WEB_TEIKI_SERIALIZE_DATA IS '定期区間シリアライズデータ';
COMMENT ON COLUMN TEIKI_JOUHOU.TOUROKU_USER_ID IS '登録ユーザーID';
COMMENT ON COLUMN TEIKI_JOUHOU.TOUROKU_TIME IS '登録日時';
COMMENT ON COLUMN TEIKI_JOUHOU.KOUSHIN_USER_ID IS '更新ユーザーID';
COMMENT ON COLUMN TEIKI_JOUHOU.KOUSHIN_TIME IS '更新日時';

INSERT INTO TEIKI_JOUHOU
SELECT
    USER_ID
    ,SHIYOU_KAISHIBI
    ,SHIYOU_SHUURYOUBI
    ,INTRA_EKI_NAME
    ,INTRA_ROSEN
    ,WEB_TEIKI_KUKAN
    ,seri(:'SCHEMA_NAME', USER_ID, SHIYOU_KAISHIBI) -- WEB_TEIKI_SERIALIZE_DATA
    ,TOUROKU_USER_ID
    ,TOUROKU_TIME
    ,KOUSHIN_USER_ID
    ,KOUSHIN_TIME
FROM TEIKI_JOUHOU_TMP;

DROP TABLE TEIKI_JOUHOU_TMP;
DROP FUNCTION seri(text, text, date);

-- 通勤定期に定期区間シリアライズデータを追加
CREATE FUNCTION seri(s TEXT, did TEXT) RETURNS TEXT AS $$
DECLARE
    cnt integer;
BEGIN
    cnt := (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema=s AND table_name='tsuukinteiki_tmp' AND column_name='teiki_serialize_data');
    IF cnt >= 1 THEN
        RETURN (SELECT teiki_serialize_data FROM tsuukinteiki_tmp WHERE denpyou_id=did);
    ELSE
        RETURN '';
    END IF;
END;
$$ LANGUAGE plpgsql immutable;

ALTER TABLE TSUUKINTEIKI RENAME TO TSUUKINTEIKI_TMP;
CREATE TABLE TSUUKINTEIKI
(
    DENPYOU_ID VARCHAR(19) NOT NULL,
    SHIYOU_KIKAN_KBN VARCHAR(2) NOT NULL,
    SHIYOU_KAISHIBI DATE NOT NULL,
    SHIYOU_SHUURYOUBI DATE NOT NULL,
    JYOUSHA_KUKAN VARCHAR NOT NULL,
    TEIKI_SERIALIZE_DATA VARCHAR NOT NULL,
    SHIHARAIBI DATE,
    TEKIYOU VARCHAR(60) NOT NULL,
    ZEIRITSU DECIMAL(3) NOT NULL,
    KINGAKU DECIMAL(15) NOT NULL,
    TENYUURYOKU_FLG VARCHAR(1) NOT NULL,
    HF1_CD VARCHAR(20) NOT NULL,
    HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF2_CD VARCHAR(20) NOT NULL,
    HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF3_CD VARCHAR(20) NOT NULL,
    HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF4_CD VARCHAR(20) NOT NULL,
    HF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF5_CD VARCHAR(20) NOT NULL,
    HF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF6_CD VARCHAR(20) NOT NULL,
    HF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF7_CD VARCHAR(20) NOT NULL,
    HF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF8_CD VARCHAR(20) NOT NULL,
    HF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF9_CD VARCHAR(20) NOT NULL,
    HF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    HF10_CD VARCHAR(20) NOT NULL,
    HF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    SHIWAKE_EDANO INT NOT NULL,
    TORIHIKI_NAME VARCHAR(20) NOT NULL,
    KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
    KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
    TORIHIKISAKI_CD VARCHAR(12) NOT NULL,
    TORIHIKISAKI_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
    KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
    KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
    KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
    KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
    KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
    KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
    KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
    KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
    KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
    KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
    KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
    UF1_CD VARCHAR(20) NOT NULL,
    UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF2_CD VARCHAR(20) NOT NULL,
    UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF3_CD VARCHAR(20) NOT NULL,
    UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF4_CD VARCHAR(20) NOT NULL,
    UF4_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF5_CD VARCHAR(20) NOT NULL,
    UF5_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF6_CD VARCHAR(20) NOT NULL,
    UF6_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF7_CD VARCHAR(20) NOT NULL,
    UF7_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF8_CD VARCHAR(20) NOT NULL,
    UF8_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF9_CD VARCHAR(20) NOT NULL,
    UF9_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    UF10_CD VARCHAR(20) NOT NULL,
    UF10_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    PROJECT_CD VARCHAR(12) NOT NULL,
    PROJECT_NAME VARCHAR(20) NOT NULL,
    SEGMENT_CD VARCHAR(8) NOT NULL,
    SEGMENT_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
    TEKIYOU_CD VARCHAR(4) NOT NULL,
    TOUROKU_USER_ID VARCHAR(30) NOT NULL,
    TOUROKU_TIME TIMESTAMP NOT NULL,
    KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
    KOUSHIN_TIME TIMESTAMP NOT NULL,
    PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE TSUUKINTEIKI IS '通勤定期';
COMMENT ON COLUMN TSUUKINTEIKI.DENPYOU_ID IS '伝票ID';
COMMENT ON COLUMN TSUUKINTEIKI.SHIYOU_KIKAN_KBN IS '使用期間区分';
COMMENT ON COLUMN TSUUKINTEIKI.SHIYOU_KAISHIBI IS '使用開始日';
COMMENT ON COLUMN TSUUKINTEIKI.SHIYOU_SHUURYOUBI IS '使用終了日';
COMMENT ON COLUMN TSUUKINTEIKI.JYOUSHA_KUKAN IS '乗車区間';
COMMENT ON COLUMN TSUUKINTEIKI.TEIKI_SERIALIZE_DATA IS '定期区間シリアライズデータ';
COMMENT ON COLUMN TSUUKINTEIKI.SHIHARAIBI IS '支払日';
COMMENT ON COLUMN TSUUKINTEIKI.TEKIYOU IS '摘要';
COMMENT ON COLUMN TSUUKINTEIKI.ZEIRITSU IS '税率';
COMMENT ON COLUMN TSUUKINTEIKI.KINGAKU IS '金額';
COMMENT ON COLUMN TSUUKINTEIKI.TENYUURYOKU_FLG IS '手入力フラグ';
COMMENT ON COLUMN TSUUKINTEIKI.HF1_CD IS 'HF1コード';
COMMENT ON COLUMN TSUUKINTEIKI.HF1_NAME_RYAKUSHIKI IS 'HF1名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.HF2_CD IS 'HF2コード';
COMMENT ON COLUMN TSUUKINTEIKI.HF2_NAME_RYAKUSHIKI IS 'HF2名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.HF3_CD IS 'HF3コード';
COMMENT ON COLUMN TSUUKINTEIKI.HF3_NAME_RYAKUSHIKI IS 'HF3名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.HF4_CD IS 'HF4コード';
COMMENT ON COLUMN TSUUKINTEIKI.HF4_NAME_RYAKUSHIKI IS 'HF4名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.HF5_CD IS 'HF5コード';
COMMENT ON COLUMN TSUUKINTEIKI.HF5_NAME_RYAKUSHIKI IS 'HF5名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.HF6_CD IS 'HF6コード';
COMMENT ON COLUMN TSUUKINTEIKI.HF6_NAME_RYAKUSHIKI IS 'HF6名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.HF7_CD IS 'HF7コード';
COMMENT ON COLUMN TSUUKINTEIKI.HF7_NAME_RYAKUSHIKI IS 'HF7名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.HF8_CD IS 'HF8コード';
COMMENT ON COLUMN TSUUKINTEIKI.HF8_NAME_RYAKUSHIKI IS 'HF8名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.HF9_CD IS 'HF9コード';
COMMENT ON COLUMN TSUUKINTEIKI.HF9_NAME_RYAKUSHIKI IS 'HF9名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.HF10_CD IS 'HF10コード';
COMMENT ON COLUMN TSUUKINTEIKI.HF10_NAME_RYAKUSHIKI IS 'HF10名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.SHIWAKE_EDANO IS '仕訳枝番号';
COMMENT ON COLUMN TSUUKINTEIKI.TORIHIKI_NAME IS '取引名';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_FUTAN_BUMON_CD IS '借方負担部門コード';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_FUTAN_BUMON_NAME IS '借方負担部門名';
COMMENT ON COLUMN TSUUKINTEIKI.TORIHIKISAKI_CD IS '取引先コード';
COMMENT ON COLUMN TSUUKINTEIKI.TORIHIKISAKI_NAME_RYAKUSHIKI IS '取引先名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_KAMOKU_CD IS '借方科目コード';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_KAMOKU_NAME IS '借方科目名';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_KAMOKU_EDABAN_CD IS '借方科目枝番コード';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_KAMOKU_EDABAN_NAME IS '借方科目枝番名';
COMMENT ON COLUMN TSUUKINTEIKI.KARI_KAZEI_KBN IS '借方課税区分';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_FUTAN_BUMON_CD IS '貸方負担部門コード';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_FUTAN_BUMON_NAME IS '貸方負担部門名';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_KAMOKU_CD IS '貸方科目コード';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_KAMOKU_NAME IS '貸方科目名';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_KAMOKU_EDABAN_CD IS '貸方科目枝番コード';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_KAMOKU_EDABAN_NAME IS '貸方科目枝番名';
COMMENT ON COLUMN TSUUKINTEIKI.KASHI_KAZEI_KBN IS '貸方課税区分';
COMMENT ON COLUMN TSUUKINTEIKI.UF1_CD IS 'UF1コード';
COMMENT ON COLUMN TSUUKINTEIKI.UF1_NAME_RYAKUSHIKI IS 'UF1名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.UF2_CD IS 'UF2コード';
COMMENT ON COLUMN TSUUKINTEIKI.UF2_NAME_RYAKUSHIKI IS 'UF2名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.UF3_CD IS 'UF3コード';
COMMENT ON COLUMN TSUUKINTEIKI.UF3_NAME_RYAKUSHIKI IS 'UF3名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.UF4_CD IS 'UF4コード';
COMMENT ON COLUMN TSUUKINTEIKI.UF4_NAME_RYAKUSHIKI IS 'UF4名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.UF5_CD IS 'UF5コード';
COMMENT ON COLUMN TSUUKINTEIKI.UF5_NAME_RYAKUSHIKI IS 'UF5名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.UF6_CD IS 'UF6コード';
COMMENT ON COLUMN TSUUKINTEIKI.UF6_NAME_RYAKUSHIKI IS 'UF6名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.UF7_CD IS 'UF7コード';
COMMENT ON COLUMN TSUUKINTEIKI.UF7_NAME_RYAKUSHIKI IS 'UF7名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.UF8_CD IS 'UF8コード';
COMMENT ON COLUMN TSUUKINTEIKI.UF8_NAME_RYAKUSHIKI IS 'UF8名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.UF9_CD IS 'UF9コード';
COMMENT ON COLUMN TSUUKINTEIKI.UF9_NAME_RYAKUSHIKI IS 'UF9名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.UF10_CD IS 'UF10コード';
COMMENT ON COLUMN TSUUKINTEIKI.UF10_NAME_RYAKUSHIKI IS 'UF10名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.PROJECT_CD IS 'プロジェクトコード';
COMMENT ON COLUMN TSUUKINTEIKI.PROJECT_NAME IS 'プロジェクト名';
COMMENT ON COLUMN TSUUKINTEIKI.SEGMENT_CD IS 'セグメントコード';
COMMENT ON COLUMN TSUUKINTEIKI.SEGMENT_NAME_RYAKUSHIKI IS 'セグメント名（略式）';
COMMENT ON COLUMN TSUUKINTEIKI.TEKIYOU_CD IS '摘要コード';
COMMENT ON COLUMN TSUUKINTEIKI.TOUROKU_USER_ID IS '登録ユーザーID';
COMMENT ON COLUMN TSUUKINTEIKI.TOUROKU_TIME IS '登録日時';
COMMENT ON COLUMN TSUUKINTEIKI.KOUSHIN_USER_ID IS '更新ユーザーID';
COMMENT ON COLUMN TSUUKINTEIKI.KOUSHIN_TIME IS '更新日時';

INSERT INTO TSUUKINTEIKI
SELECT
    DENPYOU_ID
    ,SHIYOU_KIKAN_KBN
    ,SHIYOU_KAISHIBI
    ,SHIYOU_SHUURYOUBI
    ,JYOUSHA_KUKAN
    ,seri(:'SCHEMA_NAME', DENPYOU_ID) -- TEIKI_SERIALIZE_DATA
    ,SHIHARAIBI
    ,TEKIYOU
    ,ZEIRITSU
    ,KINGAKU
    ,TENYUURYOKU_FLG
    ,HF1_CD
    ,HF1_NAME_RYAKUSHIKI
    ,HF2_CD
    ,HF2_NAME_RYAKUSHIKI
    ,HF3_CD
    ,HF3_NAME_RYAKUSHIKI
    ,HF4_CD
    ,HF4_NAME_RYAKUSHIKI
    ,HF5_CD
    ,HF5_NAME_RYAKUSHIKI
    ,HF6_CD
    ,HF6_NAME_RYAKUSHIKI
    ,HF7_CD
    ,HF7_NAME_RYAKUSHIKI
    ,HF8_CD
    ,HF8_NAME_RYAKUSHIKI
    ,HF9_CD
    ,HF9_NAME_RYAKUSHIKI
    ,HF10_CD
    ,HF10_NAME_RYAKUSHIKI
    ,SHIWAKE_EDANO
    ,TORIHIKI_NAME
    ,KARI_FUTAN_BUMON_CD
    ,KARI_FUTAN_BUMON_NAME
    ,TORIHIKISAKI_CD
    ,TORIHIKISAKI_NAME_RYAKUSHIKI
    ,KARI_KAMOKU_CD
    ,KARI_KAMOKU_NAME
    ,KARI_KAMOKU_EDABAN_CD
    ,KARI_KAMOKU_EDABAN_NAME
    ,KARI_KAZEI_KBN
    ,KASHI_FUTAN_BUMON_CD
    ,KASHI_FUTAN_BUMON_NAME
    ,KASHI_KAMOKU_CD
    ,KASHI_KAMOKU_NAME
    ,KASHI_KAMOKU_EDABAN_CD
    ,KASHI_KAMOKU_EDABAN_NAME
    ,KASHI_KAZEI_KBN
    ,UF1_CD
    ,UF1_NAME_RYAKUSHIKI
    ,UF2_CD
    ,UF2_NAME_RYAKUSHIKI
    ,UF3_CD
    ,UF3_NAME_RYAKUSHIKI
    ,UF4_CD
    ,UF4_NAME_RYAKUSHIKI
    ,UF5_CD
    ,UF5_NAME_RYAKUSHIKI
    ,UF6_CD
    ,UF6_NAME_RYAKUSHIKI
    ,UF7_CD
    ,UF7_NAME_RYAKUSHIKI
    ,UF8_CD
    ,UF8_NAME_RYAKUSHIKI
    ,UF9_CD
    ,UF9_NAME_RYAKUSHIKI
    ,UF10_CD
    ,UF10_NAME_RYAKUSHIKI
    ,PROJECT_CD
    ,PROJECT_NAME
    ,SEGMENT_CD
    ,SEGMENT_NAME_RYAKUSHIKI
    ,TEKIYOU_CD
    ,TOUROKU_USER_ID
    ,TOUROKU_TIME
    ,KOUSHIN_USER_ID
    ,KOUSHIN_TIME
FROM TSUUKINTEIKI_TMP;

DROP TABLE TSUUKINTEIKI_TMP;
DROP FUNCTION seri(text, text);


commit;