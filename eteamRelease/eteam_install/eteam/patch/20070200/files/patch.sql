SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--web拠点入力分離作業 batch_logテーブルにバッチ区分を追加
ALTER TABLE batch_log RENAME TO batch_log_old;
create table batch_log (
  serial_no bigserial not null
  , start_time timestamp without time zone not null
  , end_time timestamp without time zone
  , batch_name character varying(50) not null
  , batch_status character varying(1) not null
  , count_name character varying(20) not null
  , count integer
  , batch_kbn character varying(1) not null
  , primary key (serial_no)
)WITHOUT OIDS;
comment on table batch_log is 'バッチログ';
comment on column batch_log.serial_no is 'シリアル番号';
comment on column batch_log.start_time is '開始日時';
comment on column batch_log.end_time is '終了日時';
comment on column batch_log.batch_name is 'バッチ名';
comment on column batch_log.batch_status is 'バッチステータス';
comment on column batch_log.count_name is '件数名';
comment on column batch_log.count is '件数';
comment on column batch_log.batch_kbn is 'バッチ区分';
INSERT INTO batch_log 
SELECT
  serial_no
  , start_time
  , end_time
  , batch_name
  , batch_status
  , count_name
  , count
  , '1' --batch_kbn
FROM batch_log_old; 
DROP TABLE batch_log_old;

-- batch_log.serial_noのシーケンスの再設定必要
DROP SEQUENCE IF EXISTS batch_log_serial_no_seq CASCADE;
DROP SEQUENCE IF EXISTS batch_log_serial_no_seq1 CASCADE;
CREATE SEQUENCE batch_log_serial_no_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE batch_log_serial_no_seq OWNER TO eteam;
ALTER SEQUENCE batch_log_serial_no_seq OWNED BY batch_log.serial_no;
ALTER TABLE ONLY batch_log ALTER COLUMN serial_no SET DEFAULT nextval('batch_log_serial_no_seq'::regclass);
SELECT setval('batch_log_serial_no_seq',(SELECT MAX(serial_no) FROM batch_log));


--web拠点入力分離作業 kyoten側に移したテーブル削除
DROP TABLE kamoku_zandaka;
DROP TABLE edaban_zandaka;
DROP TABLE ki_shouhizei_setting;
DROP TABLE bumon_furikae;
DROP TABLE bumon_furikae_meisai;
DROP TABLE bumon_furikae_setting;
DROP TABLE bumon_tsukekae_control;
DROP TABLE suitouchou;
DROP TABLE suitouchou_meisai;
DROP TABLE suitouchou_setting;
DROP TABLE denpyou_ichiran_kyoten;
DROP TABLE denpyou_ichiran_kyoten_meisai;
DROP TABLE denpyou_serial_no_saiban_kyoten;
DROP TABLE denpyou_shubetsu_ichiran_kyoten;
DROP TABLE user_tenpu_file;
DROP TABLE meisai_tenpu_file_himoduke;
DROP TABLE shiwake_pattern_master_zaimu_kyoten;
DROP TABLE shounin_joukyou_kyoten;
DROP TABLE shounin_route_kyoten;
DROP TABLE teikei_shiwake;
DROP TABLE teikei_shiwake_meisai;
DROP TABLE zaimu_kyoten_nyuryoku_bumon_security;
DROP TABLE zaimu_kyoten_nyuryoku_haifu_bumon;
DROP TABLE zaimu_kyoten_nyuryoku_haifu_kamoku;
DROP TABLE zaimu_kyoten_nyuryoku_haifu_pattern;
DROP TABLE zaimu_kyoten_nyuryoku_ichiran;
DROP TABLE zaimu_kyoten_nyuryoku_kamoku_security;
DROP TABLE zaimu_kyoten_nyuryoku_nyuryokusha;
DROP TABLE zaimu_kyoten_nyuryoku_shiwake_serial_no_saiban;
DROP TABLE zaimu_kyoten_nyuryoku_shounin_route;
DROP TABLE zaimu_kyoten_nyuryoku_tsukekaemoto;
DROP TABLE zaimu_kyoten_nyuryoku_user_info;
DROP TABLE zaimu_kyoten_shiwake_de3;
DROP TABLE zaimu_kyoten_shiwake_sias;
DROP TABLE bumon_tsukekaesaki;

--web拠点入力分離作業 kyoten側の初期設定CSVに移したレコードを削除
--画面権限制御
DELETE
FROM
  gamen_kengen_seigyo 
WHERE
  gamen_id IN ( 
    'BumonFurikaeSettei'
    , 'SuitouchouSettei'
    , 'SuitouchouTorihikiIchiran'
    , 'SuitouchouTorihikiTsuika'
    , 'SuitouchouTorihikiHenkou'
    , 'SuitouchouPatternCopy'
    , 'ZaimuKyotenNyuryokuKihyou'
    , 'ZaimuKyotenNyuryokuKensaku'
    , 'ZaimuKyotenNyuryokuKihyoucyuuIchiran'
    , 'ZaimuKyotenNyuryokuShinseichuuIchiran'
    , 'ZaimuKyotenNyuryokuShouninmachiIchiran'
    , 'ZaimuKyotenNyuryokuIchiranItem'
    , 'ZaimuKyotenNyuryokuIchiranKensakuItem'
    , 'DenpyouKensakuKoumokuKyoutsuSettei'
    , 'BumonFurikae'
    , 'Suitouchou'
    , 'SuitouchouKakunin'
    , 'TeikeiShiwakeTouroku'
    , 'TeikeiShiwakeSentaku'
    , 'TeikeiShiwakeMeisai'
    , 'BumonFurikaeMeisai'
    , 'ZaimuKyotenNyuryokuKamokuSentaku'
    , 'ZaimuKyotenNyuryokuFutanBumonSentaku'
    , 'SuitouchouMeisai'
    , 'ZaimuKyotenShiwakeDataDe3Henkou'
    , 'ZaimuKyotenShiwakeDataSiasHenkou'
    , 'ZaimuKyotenNyuryokuPatternSentaku'
    , 'SuitouchouTorihikiSentaku'
    , 'BumonTsukekaeSentaku'
    , 'HaifuShousai'
    , 'HaifuPatternSentaku'
    , 'TenpuFileSentaku'
  );

--設定情報
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

--画面項目制御
DELETE FROM gamen_koumoku_seigyo WHERE denpyou_kbn LIKE 'Z%';

--内部コード設定
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- 支払依頼申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- 請求書払申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

--マスター管理一覧
DELETE 
FROM
  master_kanri_ichiran 
WHERE
  master_id IN ( 
    'edaban_zandaka'
    , 'kamoku_zandaka'
    , 'ki_shouhizei_setting'
  );

--マスター管理版数
DELETE 
FROM
  master_kanri_hansuu 
WHERE
  master_id IN ( 
    'edaban_zandaka'
    , 'kamoku_zandaka'
    , 'ki_shouhizei_setting'
  );

-- マスター取込制御
DELETE FROM master_torikomi_ichiran_de3;
DELETE FROM master_torikomi_shousai_de3;
DELETE FROM master_torikomi_ichiran_sias;
DELETE FROM master_torikomi_shousai_sias;
DELETE FROM master_torikomi_ichiran_mk2;
DELETE FROM master_torikomi_shousai_mk2;
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_mk2 FROM '.\files\csv\master_torikomi_ichiran_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_mk2 FROM '.\files\csv\master_torikomi_shousai_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--伝票テーブル、通知テーブル
DELETE FROM denpyou WHERE denpyou_kbn LIKE 'Z%';
DELETE FROM tsuuchi WHERE substr(denpyou_id, 8, 4) LIKE 'Z%';
--web拠点入力分離作業 kyoten側の初期設定CSVに移したレコードを削除 ここまで


-- ユーザセッション
create table if not exists public.user_session (
  jsession_id character varying(32) not null
  , schema character varying(32) not null
  , data bytea not null
  , time timestamp(6) without time zone not null
  , primary key (jsession_id,schema)
);

-- 連絡票_0961 予算執行対象が「実施起案」「支出起案」に設定時、起案番号運用フラグを「1」にする。
UPDATE denpyou_shubetsu_ichiran SET kianbangou_unyou_flg = '1' WHERE yosan_shikkou_taishou IN('A','B');

-- ICSP連絡票_0973_添付伝票の除外機能追加
create table tenpu_denpyou_jyogai (
  denpyou_id character varying(19) not null
  , constraint tenpu_denpyou_jyogai_PKEY primary key (denpyou_id)
);
comment on table tenpu_denpyou_jyogai is '添付伝票除外';
comment on column tenpu_denpyou_jyogai.denpyou_id is '伝票ID';


commit;
