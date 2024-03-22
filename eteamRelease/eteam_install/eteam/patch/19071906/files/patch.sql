SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

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
DROP TABLE setting_info_tmp;

-- ログ・拠点入力のデータ保存日数初期値はdata_hozon_nissuuの元値に合わせる
UPDATE setting_info set setting_val = (SELECT setting_val FROM setting_info WHERE setting_name ='data_hozon_nissuu') WHERE setting_name = 'data_hozon_nissuu_log';
UPDATE setting_info set setting_val = (SELECT setting_val FROM setting_info WHERE setting_name ='data_hozon_nissuu') WHERE setting_name = 'data_hozon_nissuu_kyoten';

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


-- 連絡票K-104
ALTER TABLE bumon_furikae_meisai ALTER COLUMN kari_kingaku DROP NOT NULL;
ALTER TABLE bumon_furikae_meisai ALTER COLUMN kari_zeiritsu DROP NOT NULL;
ALTER TABLE bumon_furikae_meisai ALTER COLUMN kashi_kingaku DROP NOT NULL;
ALTER TABLE bumon_furikae_meisai ALTER COLUMN kashi_zeiritsu DROP NOT NULL;
ALTER TABLE teikei_shiwake_meisai ALTER COLUMN kari_kingaku DROP NOT NULL;
ALTER TABLE teikei_shiwake_meisai ALTER COLUMN kari_zeiritsu DROP NOT NULL;
ALTER TABLE teikei_shiwake_meisai ALTER COLUMN kashi_kingaku DROP NOT NULL;
ALTER TABLE teikei_shiwake_meisai ALTER COLUMN kashi_zeiritsu DROP NOT NULL;
UPDATE bumon_furikae_meisai SET kari_kingaku = null WHERE kari_kingaku = '0';
UPDATE bumon_furikae_meisai SET kari_zeiritsu = null WHERE kari_zeiritsu = '0';
UPDATE bumon_furikae_meisai SET kashi_kingaku = null WHERE kashi_kingaku = '0';
UPDATE bumon_furikae_meisai SET kashi_zeiritsu = null WHERE kashi_zeiritsu = '0';
UPDATE teikei_shiwake_meisai SET kari_kingaku = null WHERE kari_kingaku = '0';
UPDATE teikei_shiwake_meisai SET kari_zeiritsu = null WHERE kari_zeiritsu = '0';
UPDATE teikei_shiwake_meisai SET kashi_kingaku = null WHERE kashi_kingaku = '0';
UPDATE teikei_shiwake_meisai SET kashi_zeiritsu = null WHERE kashi_zeiritsu = '0';


--ガソリン代対応 国内交通手段マスター
ALTER TABLE koutsuu_shudan_master RENAME TO koutsuu_shudan_master_tmp;
create table koutsuu_shudan_master (
  sort_jun character(3) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , suuryou_nyuryoku_type character varying(1) not null
  , tanka numeric(15, 3)
  , suuryou_kigou character varying(5)
  , primary key (sort_jun,koutsuu_shudan)
) WITHOUT OIDS;
comment on table koutsuu_shudan_master is '国内用交通手段マスター';
comment on column koutsuu_shudan_master.sort_jun is '並び順';
comment on column koutsuu_shudan_master.koutsuu_shudan is '交通手段';
comment on column koutsuu_shudan_master.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column koutsuu_shudan_master.zei_kubun is '税区分';
comment on column koutsuu_shudan_master.edaban is '枝番コード';
comment on column koutsuu_shudan_master.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column koutsuu_shudan_master.tanka is '単価';
comment on column koutsuu_shudan_master.suuryou_kigou is '数量記号';
INSERT INTO koutsuu_shudan_master(
	sort_jun,
	koutsuu_shudan,
	shouhyou_shorui_hissu_flg,
	zei_kubun,
	edaban,
	suuryou_nyuryoku_type,
	tanka,
	suuryou_kigou
)
SELECT
	sort_jun,
	koutsuu_shudan,
	shouhyou_shorui_hissu_flg,
	zei_kubun,
	edaban,
	'0', --suuryou_nyuryoku_type
	null, --tanka
 	'' --suuryou_kigou
FROM koutsuu_shudan_master_tmp;
DROP TABLE koutsuu_shudan_master_tmp;

--ガソリン代対応 マスター管理版数の国内交通手段マスターを更新
UPDATE master_kanri_hansuu SET delete_flg = '1' WHERE master_id = 'koutsuu_shudan_master';
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
 , length(convert_to(E'並び順,交通手段,証憑書類必須フラグ,税区分,枝番コード,数量入力タイプ,単価,数量記号\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun,edaban,suuryou_nyuryoku_type,tanka,suuryou_kigou\r\nvarchar(3),varchar(10),varchar(1),varchar(1),varchar(12),varchar(1),decimal(15),varchar(5)\r\n1,1,2,,2,2,,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun || ',' || edaban || ',' || '0' || ',' || ',' FROM koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'並び順,交通手段,証憑書類必須フラグ,税区分,枝番コード,数量入力タイプ,単価,数量記号\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun,edaban,suuryou_nyuryoku_type,tanka,suuryou_kigou\r\nvarchar(3),varchar(10),varchar(1),varchar(1),varchar(12),varchar(1),decimal(15),varchar(5)\r\n1,1,2,,2,2,,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun || ',' || edaban || ',' || '0' || ',' || ',' FROM koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
);

--ガソリン代対応 交通費精算明細
ALTER TABLE koutsuuhiseisan_meisai RENAME TO koutsuuhiseisan_meisai_tmp;
create table koutsuuhiseisan_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , kikan_from date not null
  , shubetsu_cd character varying(1) not null
  , shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , ryoushuusho_seikyuusho_tou_flg character(1) not null
  , naiyou character varying(512) not null
  , bikou character varying not null
  , oufuku_flg character varying(1) not null
  , houjin_card_riyou_flg character varying(1) not null
  , kaisha_tehai_flg character varying(1) not null
  , jidounyuuryoku_flg character varying(1) not null
  , tanka numeric(15, 3) not null
  , suuryou_nyuryoku_type character varying(1) not null
  , suuryou numeric(15, 2)
  , suuryou_kigou character varying(5)
  , meisai_kingaku numeric(15) not null
  , ic_card_no character varying(16) not null
  , ic_card_sequence_no character varying(10) not null
  , himoduke_card_meisai bigint
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id,denpyou_edano)
)WITHOUT OIDS;
comment on table koutsuuhiseisan_meisai is '交通費精算明細';
comment on column koutsuuhiseisan_meisai.denpyou_id is '伝票ID';
comment on column koutsuuhiseisan_meisai.denpyou_edano is '伝票枝番号';
comment on column koutsuuhiseisan_meisai.kikan_from is '期間開始日';
comment on column koutsuuhiseisan_meisai.shubetsu_cd is '種別コード';
comment on column koutsuuhiseisan_meisai.shubetsu1 is '種別１';
comment on column koutsuuhiseisan_meisai.shubetsu2 is '種別２';
comment on column koutsuuhiseisan_meisai.koutsuu_shudan is '交通手段';
comment on column koutsuuhiseisan_meisai.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column koutsuuhiseisan_meisai.ryoushuusho_seikyuusho_tou_flg is '領収書・請求書等フラグ';
comment on column koutsuuhiseisan_meisai.naiyou is '内容（旅費精算）';
comment on column koutsuuhiseisan_meisai.bikou is '備考（旅費精算）';
comment on column koutsuuhiseisan_meisai.oufuku_flg is '往復フラグ';
comment on column koutsuuhiseisan_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column koutsuuhiseisan_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column koutsuuhiseisan_meisai.jidounyuuryoku_flg is '自動入力フラグ';
comment on column koutsuuhiseisan_meisai.tanka is '単価';
comment on column koutsuuhiseisan_meisai.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column koutsuuhiseisan_meisai.suuryou is '数量';
comment on column koutsuuhiseisan_meisai.suuryou_kigou is '数量記号';
comment on column koutsuuhiseisan_meisai.meisai_kingaku is '明細金額';
comment on column koutsuuhiseisan_meisai.ic_card_no is 'ICカード番号';
comment on column koutsuuhiseisan_meisai.ic_card_sequence_no is 'ICカードシーケンス番号';
comment on column koutsuuhiseisan_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column koutsuuhiseisan_meisai.touroku_user_id is '登録ユーザーID';
comment on column koutsuuhiseisan_meisai.touroku_time is '登録日時';
comment on column koutsuuhiseisan_meisai.koushin_user_id is '更新ユーザーID';
comment on column koutsuuhiseisan_meisai.koushin_time is '更新日時';
INSERT INTO koutsuuhiseisan_meisai(
	denpyou_id
  , denpyou_edano
  , kikan_from
  , shubetsu_cd
  , shubetsu1
  , shubetsu2
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , ryoushuusho_seikyuusho_tou_flg
  , naiyou
  , bikou
  , oufuku_flg
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , jidounyuuryoku_flg
  , tanka
  , suuryou_nyuryoku_type
  , suuryou
  , suuryou_kigou
  , meisai_kingaku
  , ic_card_no
  , ic_card_sequence_no
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
)SELECT
  	denpyou_id
  , denpyou_edano
  , kikan_from
  , shubetsu_cd
  , shubetsu1
  , shubetsu2
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , ryoushuusho_seikyuusho_tou_flg
  , naiyou
  , bikou
  , oufuku_flg
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , jidounyuuryoku_flg
  , tanka
  , '0' --suuryou_nyuryoku_type
  , null --suuryou
  , '' --suuryou_kigou
  , meisai_kingaku
  , ic_card_no
  , ic_card_sequence_no
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM koutsuuhiseisan_meisai_tmp;
DROP TABLE koutsuuhiseisan_meisai_tmp;

--ガソリン代対応 旅費仮払明細
ALTER TABLE ryohi_karibarai_meisai RENAME TO ryohi_karibarai_meisai_tmp;
create table ryohi_karibarai_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , kikan_from date not null
  , kikan_to date
  , kyuujitsu_nissuu numeric
  , shubetsu_cd character varying(1) not null
  , shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , naiyou character varying(512) not null
  , bikou character varying not null
  , oufuku_flg character varying(1) not null
  , jidounyuuryoku_flg character varying(1) not null
  , nissuu numeric
  , tanka numeric(15, 3) not null
  , suuryou_nyuryoku_type character varying(1) not null
  , suuryou numeric(15, 2)
  , suuryou_kigou character varying(5)
  , meisai_kingaku numeric(15) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id,denpyou_edano)
)WITHOUT OIDS;
comment on table ryohi_karibarai_meisai is '旅費仮払明細';
comment on column ryohi_karibarai_meisai.denpyou_id is '伝票ID';
comment on column ryohi_karibarai_meisai.denpyou_edano is '伝票枝番号';
comment on column ryohi_karibarai_meisai.kikan_from is '期間開始日';
comment on column ryohi_karibarai_meisai.kikan_to is '期間終了日';
comment on column ryohi_karibarai_meisai.kyuujitsu_nissuu is '休日日数';
comment on column ryohi_karibarai_meisai.shubetsu_cd is '種別コード';
comment on column ryohi_karibarai_meisai.shubetsu1 is '種別１';
comment on column ryohi_karibarai_meisai.shubetsu2 is '種別２';
comment on column ryohi_karibarai_meisai.koutsuu_shudan is '交通手段';
comment on column ryohi_karibarai_meisai.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column ryohi_karibarai_meisai.naiyou is '内容（旅費精算）';
comment on column ryohi_karibarai_meisai.bikou is '備考（旅費精算）';
comment on column ryohi_karibarai_meisai.oufuku_flg is '往復フラグ';
comment on column ryohi_karibarai_meisai.jidounyuuryoku_flg is '自動入力フラグ';
comment on column ryohi_karibarai_meisai.nissuu is '日数';
comment on column ryohi_karibarai_meisai.tanka is '単価';
comment on column ryohi_karibarai_meisai.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column ryohi_karibarai_meisai.suuryou is '数量';
comment on column ryohi_karibarai_meisai.suuryou_kigou is '数量記号';
comment on column ryohi_karibarai_meisai.meisai_kingaku is '明細金額';
comment on column ryohi_karibarai_meisai.touroku_user_id is '登録ユーザーID';
comment on column ryohi_karibarai_meisai.touroku_time is '登録日時';
comment on column ryohi_karibarai_meisai.koushin_user_id is '更新ユーザーID';
comment on column ryohi_karibarai_meisai.koushin_time is '更新日時';
INSERT INTO ryohi_karibarai_meisai(
	denpyou_id
  , denpyou_edano
  , kikan_from
  , kikan_to
  , kyuujitsu_nissuu
  , shubetsu_cd
  , shubetsu1
  , shubetsu2
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , naiyou
  , bikou
  , oufuku_flg
  , jidounyuuryoku_flg
  , nissuu
  , tanka
  , suuryou_nyuryoku_type
  , suuryou
  , suuryou_kigou
  , meisai_kingaku
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
)SELECT
  	denpyou_id
  , denpyou_edano
  , kikan_from
  , kikan_to
  , kyuujitsu_nissuu
  , shubetsu_cd
  , shubetsu1
  , shubetsu2
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , naiyou
  , bikou
  , oufuku_flg
  , jidounyuuryoku_flg
  , nissuu
  , tanka
  , '0' --suuryou_nyuryoku_type
  , null --suuryou
  , '' --suuryou_kigou
  , meisai_kingaku
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM ryohi_karibarai_meisai_tmp;
DROP TABLE ryohi_karibarai_meisai_tmp;

--ガソリン代対応 旅費精算明細
ALTER TABLE ryohiseisan_meisai RENAME TO ryohiseisan_meisai_tmp;
create table ryohiseisan_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , kikan_from date not null
  , kikan_to date
  , kyuujitsu_nissuu numeric
  , shubetsu_cd character varying(1) not null
  , shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , ryoushuusho_seikyuusho_tou_flg character(1) not null
  , naiyou character varying(512) not null
  , bikou character varying not null
  , oufuku_flg character varying(1) not null
  , houjin_card_riyou_flg character varying(1) not null
  , kaisha_tehai_flg character varying(1) not null
  , jidounyuuryoku_flg character varying(1) not null
  , nissuu numeric
  , tanka numeric(15, 3) not null
  , suuryou_nyuryoku_type character varying(1) not null
  , suuryou numeric(15, 2)
  , suuryou_kigou character varying(5)
  , meisai_kingaku numeric(15) not null
  , ic_card_no character varying(16) not null
  , ic_card_sequence_no character varying(10) not null
  , himoduke_card_meisai bigint
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id,denpyou_edano)
)WITHOUT OIDS;
comment on table ryohiseisan_meisai is '旅費精算明細';
comment on column ryohiseisan_meisai.denpyou_id is '伝票ID';
comment on column ryohiseisan_meisai.denpyou_edano is '伝票枝番号';
comment on column ryohiseisan_meisai.kikan_from is '期間開始日';
comment on column ryohiseisan_meisai.kikan_to is '期間終了日';
comment on column ryohiseisan_meisai.kyuujitsu_nissuu is '休日日数';
comment on column ryohiseisan_meisai.shubetsu_cd is '種別コード';
comment on column ryohiseisan_meisai.shubetsu1 is '種別１';
comment on column ryohiseisan_meisai.shubetsu2 is '種別２';
comment on column ryohiseisan_meisai.koutsuu_shudan is '交通手段';
comment on column ryohiseisan_meisai.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column ryohiseisan_meisai.ryoushuusho_seikyuusho_tou_flg is '領収書・請求書等フラグ';
comment on column ryohiseisan_meisai.naiyou is '内容（旅費精算）';
comment on column ryohiseisan_meisai.bikou is '備考（旅費精算）';
comment on column ryohiseisan_meisai.oufuku_flg is '往復フラグ';
comment on column ryohiseisan_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column ryohiseisan_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column ryohiseisan_meisai.jidounyuuryoku_flg is '自動入力フラグ';
comment on column ryohiseisan_meisai.nissuu is '日数';
comment on column ryohiseisan_meisai.tanka is '単価';
comment on column ryohiseisan_meisai.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column ryohiseisan_meisai.suuryou is '数量';
comment on column ryohiseisan_meisai.suuryou_kigou is '数量記号';
comment on column ryohiseisan_meisai.meisai_kingaku is '明細金額';
comment on column ryohiseisan_meisai.ic_card_no is 'ICカード番号';
comment on column ryohiseisan_meisai.ic_card_sequence_no is 'ICカードシーケンス番号';
comment on column ryohiseisan_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column ryohiseisan_meisai.touroku_user_id is '登録ユーザーID';
comment on column ryohiseisan_meisai.touroku_time is '登録日時';
comment on column ryohiseisan_meisai.koushin_user_id is '更新ユーザーID';
comment on column ryohiseisan_meisai.koushin_time is '更新日時';
INSERT INTO ryohiseisan_meisai(
	denpyou_id
  , denpyou_edano
  , kikan_from
  , kikan_to
  , shubetsu_cd
  , shubetsu1
  , shubetsu2
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , ryoushuusho_seikyuusho_tou_flg
  , naiyou
  , bikou
  , oufuku_flg
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , jidounyuuryoku_flg
  , tanka
  , suuryou_nyuryoku_type
  , suuryou
  , suuryou_kigou
  , meisai_kingaku
  , ic_card_no
  , ic_card_sequence_no
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
)SELECT
  	denpyou_id
  , denpyou_edano
  , kikan_from
  , kikan_to
  , shubetsu_cd
  , shubetsu1
  , shubetsu2
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , ryoushuusho_seikyuusho_tou_flg
  , naiyou
  , bikou
  , oufuku_flg
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , jidounyuuryoku_flg
  , tanka
  , '0' --suuryou_nyuryoku_type
  , null --suuryou
  , '' --suuryou_kigou
  , meisai_kingaku
  , ic_card_no
  , ic_card_sequence_no
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM ryohiseisan_meisai_tmp;
DROP TABLE ryohiseisan_meisai_tmp;

--ガソリン代対応 海外旅費仮払明細
ALTER TABLE kaigai_ryohi_karibarai_meisai RENAME TO kaigai_ryohi_karibarai_meisai_tmp;
create table kaigai_ryohi_karibarai_meisai (
  denpyou_id character varying(19) not null
  , kaigai_flg character varying(1) not null
  , denpyou_edano integer not null
  , kikan_from date not null
  , kikan_to date
  , kyuujitsu_nissuu numeric
  , shubetsu_cd character varying(1) not null
  , shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , naiyou character varying(512) not null
  , bikou character varying not null
  , oufuku_flg character varying(1) not null
  , jidounyuuryoku_flg character varying(1) not null
  , nissuu numeric
  , heishu_cd character varying(4)
  , rate numeric(11, 5)
  , gaika numeric(19, 2)
  , currency_unit character varying(20)
  , tanka numeric(15, 3) not null
  , suuryou_nyuryoku_type character varying(1) not null
  , suuryou numeric(15, 2)
  , suuryou_kigou character varying(5)
  , meisai_kingaku numeric(15) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id,kaigai_flg,denpyou_edano)
)WITHOUT OIDS;
comment on table kaigai_ryohi_karibarai_meisai is '海外旅費仮払明細';
comment on column kaigai_ryohi_karibarai_meisai.denpyou_id is '伝票ID';
comment on column kaigai_ryohi_karibarai_meisai.kaigai_flg is '海外フラグ';
comment on column kaigai_ryohi_karibarai_meisai.denpyou_edano is '伝票枝番号';
comment on column kaigai_ryohi_karibarai_meisai.kikan_from is '期間開始日';
comment on column kaigai_ryohi_karibarai_meisai.kikan_to is '期間終了日';
comment on column kaigai_ryohi_karibarai_meisai.kyuujitsu_nissuu is '休日日数';
comment on column kaigai_ryohi_karibarai_meisai.shubetsu_cd is '種別コード';
comment on column kaigai_ryohi_karibarai_meisai.shubetsu1 is '種別１';
comment on column kaigai_ryohi_karibarai_meisai.shubetsu2 is '種別２';
comment on column kaigai_ryohi_karibarai_meisai.koutsuu_shudan is '交通手段';
comment on column kaigai_ryohi_karibarai_meisai.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column kaigai_ryohi_karibarai_meisai.naiyou is '内容（旅費精算）';
comment on column kaigai_ryohi_karibarai_meisai.bikou is '備考（旅費精算）';
comment on column kaigai_ryohi_karibarai_meisai.oufuku_flg is '往復フラグ';
comment on column kaigai_ryohi_karibarai_meisai.jidounyuuryoku_flg is '自動入力フラグ';
comment on column kaigai_ryohi_karibarai_meisai.nissuu is '日数';
comment on column kaigai_ryohi_karibarai_meisai.heishu_cd is '幣種コード';
comment on column kaigai_ryohi_karibarai_meisai.rate is 'レート';
comment on column kaigai_ryohi_karibarai_meisai.gaika is '外貨金額';
comment on column kaigai_ryohi_karibarai_meisai.currency_unit is '通貨単位';
comment on column kaigai_ryohi_karibarai_meisai.tanka is '単価';
comment on column kaigai_ryohi_karibarai_meisai.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column kaigai_ryohi_karibarai_meisai.suuryou is '数量';
comment on column kaigai_ryohi_karibarai_meisai.suuryou_kigou is '数量記号';
comment on column kaigai_ryohi_karibarai_meisai.meisai_kingaku is '明細金額';
comment on column kaigai_ryohi_karibarai_meisai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohi_karibarai_meisai.touroku_time is '登録日時';
comment on column kaigai_ryohi_karibarai_meisai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohi_karibarai_meisai.koushin_time is '更新日時';
INSERT INTO kaigai_ryohi_karibarai_meisai(
	denpyou_id
  , kaigai_flg
  , denpyou_edano
  , kikan_from
  , kikan_to
  , shubetsu_cd
  , shubetsu1
  , shubetsu2
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , naiyou
  , bikou
  , oufuku_flg
  , jidounyuuryoku_flg
  , nissuu
  , heishu_cd
  , rate
  , gaika
  , currency_unit
  , tanka
  , suuryou_nyuryoku_type
  , suuryou
  , suuryou_kigou
  , meisai_kingaku
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
)SELECT
  	denpyou_id
  , kaigai_flg
  , denpyou_edano
  , kikan_from
  , kikan_to
  , shubetsu_cd
  , shubetsu1
  , shubetsu2
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , naiyou
  , bikou
  , oufuku_flg
  , jidounyuuryoku_flg
  , nissuu
  , heishu_cd
  , rate
  , gaika
  , currency_unit
  , tanka
  , '0' --suuryou_nyuryoku_type
  , null --suuryou
  , '' --suuryou_kigou
  , meisai_kingaku
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM kaigai_ryohi_karibarai_meisai_tmp;
DROP TABLE kaigai_ryohi_karibarai_meisai_tmp;

--ガソリン代対応 海外旅費精算明細
ALTER TABLE kaigai_ryohiseisan_meisai RENAME TO kaigai_ryohiseisan_meisai_tmp;
create table kaigai_ryohiseisan_meisai (
  denpyou_id character varying(19) not null
  , kaigai_flg character varying(1) not null
  , denpyou_edano integer not null
  , kikan_from date not null
  , kikan_to date
  , kyuujitsu_nissuu numeric
  , shubetsu_cd character varying(1) not null
  , shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , ryoushuusho_seikyuusho_tou_flg character(1) not null
  , naiyou character varying(512) not null
  , bikou character varying not null
  , oufuku_flg character varying(1) not null
  , houjin_card_riyou_flg character varying(1) not null
  , kaisha_tehai_flg character varying(1) not null
  , jidounyuuryoku_flg character varying(1) not null
  , nissuu numeric
  , heishu_cd character varying(4)
  , rate numeric(11, 5)
  , gaika numeric(19, 2)
  , currency_unit character varying(20)
  , tanka numeric(15, 3) not null
  , suuryou_nyuryoku_type character varying(1) not null
  , suuryou numeric(15, 2)
  , suuryou_kigou character varying(5)
  , meisai_kingaku numeric(15) not null
  , zei_kubun character varying(1)
  , kazei_flg character varying(1)
  , ic_card_no character varying(16) not null
  , ic_card_sequence_no character varying(10) not null
  , himoduke_card_meisai bigint
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id,kaigai_flg,denpyou_edano)
)WITHOUT OIDS;
comment on table kaigai_ryohiseisan_meisai is '海外旅費精算明細';
comment on column kaigai_ryohiseisan_meisai.denpyou_id is '伝票ID';
comment on column kaigai_ryohiseisan_meisai.kaigai_flg is '海外フラグ';
comment on column kaigai_ryohiseisan_meisai.denpyou_edano is '伝票枝番号';
comment on column kaigai_ryohiseisan_meisai.kikan_from is '期間開始日';
comment on column kaigai_ryohiseisan_meisai.kikan_to is '期間終了日';
comment on column kaigai_ryohiseisan_meisai.kyuujitsu_nissuu is '休日日数';
comment on column kaigai_ryohiseisan_meisai.shubetsu_cd is '種別コード';
comment on column kaigai_ryohiseisan_meisai.shubetsu1 is '種別１';
comment on column kaigai_ryohiseisan_meisai.shubetsu2 is '種別２';
comment on column kaigai_ryohiseisan_meisai.koutsuu_shudan is '交通手段';
comment on column kaigai_ryohiseisan_meisai.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column kaigai_ryohiseisan_meisai.ryoushuusho_seikyuusho_tou_flg is '領収書・請求書等フラグ';
comment on column kaigai_ryohiseisan_meisai.naiyou is '内容（旅費精算）';
comment on column kaigai_ryohiseisan_meisai.bikou is '備考（旅費精算）';
comment on column kaigai_ryohiseisan_meisai.oufuku_flg is '往復フラグ';
comment on column kaigai_ryohiseisan_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column kaigai_ryohiseisan_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column kaigai_ryohiseisan_meisai.jidounyuuryoku_flg is '自動入力フラグ';
comment on column kaigai_ryohiseisan_meisai.nissuu is '日数';
comment on column kaigai_ryohiseisan_meisai.heishu_cd is '幣種コード';
comment on column kaigai_ryohiseisan_meisai.rate is 'レート';
comment on column kaigai_ryohiseisan_meisai.gaika is '外貨金額';
comment on column kaigai_ryohiseisan_meisai.currency_unit is '通貨単位';
comment on column kaigai_ryohiseisan_meisai.tanka is '単価';
comment on column kaigai_ryohiseisan_meisai.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column kaigai_ryohiseisan_meisai.suuryou is '数量';
comment on column kaigai_ryohiseisan_meisai.suuryou_kigou is '数量記号';
comment on column kaigai_ryohiseisan_meisai.meisai_kingaku is '明細金額';
comment on column kaigai_ryohiseisan_meisai.zei_kubun is '税区分';
comment on column kaigai_ryohiseisan_meisai.kazei_flg is '課税フラグ';
comment on column kaigai_ryohiseisan_meisai.ic_card_no is 'ICカード番号';
comment on column kaigai_ryohiseisan_meisai.ic_card_sequence_no is 'ICカードシーケンス番号';
comment on column kaigai_ryohiseisan_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column kaigai_ryohiseisan_meisai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohiseisan_meisai.touroku_time is '登録日時';
comment on column kaigai_ryohiseisan_meisai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohiseisan_meisai.koushin_time is '更新日時';
INSERT INTO kaigai_ryohiseisan_meisai(
	denpyou_id
  , kaigai_flg
  , denpyou_edano
  , kikan_from
  , kikan_to
  , kyuujitsu_nissuu
  , shubetsu_cd
  , shubetsu1
  , shubetsu2
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , ryoushuusho_seikyuusho_tou_flg
  , naiyou
  , bikou
  , oufuku_flg
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , jidounyuuryoku_flg
  , nissuu
  , heishu_cd
  , rate
  , gaika
  , currency_unit
  , tanka
  , suuryou_nyuryoku_type
  , suuryou
  , suuryou_kigou
  , meisai_kingaku
  , zei_kubun
  , kazei_flg
  , ic_card_no
  , ic_card_sequence_no
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
)SELECT
  	denpyou_id
  , kaigai_flg
  , denpyou_edano
  , kikan_from
  , kikan_to
  , kyuujitsu_nissuu
  , shubetsu_cd
  , shubetsu1
  , shubetsu2
  , koutsuu_shudan
  , shouhyou_shorui_hissu_flg
  , ryoushuusho_seikyuusho_tou_flg
  , naiyou
  , bikou
  , oufuku_flg
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , jidounyuuryoku_flg
  , nissuu
  , heishu_cd
  , rate
  , gaika
  , currency_unit
  , tanka
  , '0' --suuryou_nyuryoku_type
  , null --suuryou
  , '' --suuryou_kigou
  , meisai_kingaku
  , zei_kubun
  , kazei_flg
  , ic_card_no
  , ic_card_sequence_no
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM kaigai_ryohiseisan_meisai_tmp;
DROP TABLE kaigai_ryohiseisan_meisai_tmp;

--連絡票K-85 部門付替先テーブル追加
CREATE TABLE bumon_tsukekaesaki (
  serial_no bigint not null
  , saki_denpyou_kbn character varying(4) not null
  , saki_zaimu_kyoten_nyuryoku_pattern_no character varying(6) not null
  , primary key (serial_no,saki_denpyou_kbn,saki_zaimu_kyoten_nyuryoku_pattern_no)
)WITHOUT OIDS;
comment on table bumon_tsukekaesaki is '拠点部門付替先';
comment on column bumon_tsukekaesaki.serial_no is 'シリアル番号';
comment on column bumon_tsukekaesaki.saki_denpyou_kbn is '付替先伝票区分';
comment on column bumon_tsukekaesaki.saki_zaimu_kyoten_nyuryoku_pattern_no is '付替先拠点入力パターンNo';

commit;
