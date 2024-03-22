SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- 0711_領収書・請求書等デフォルト値設定 設定情報データ変更 ※hyouji_jun900以降はカスタマイズ領域
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


--連絡票665_インフォメーションの表示順序を変更 テーブル追加
create table information_sort (
  info_id character varying(14) not null
  , hyouji_jun integer
  , primary key (info_id)
)WITHOUT OIDS;
comment on table information_sort is 'インフォメーション順序';
comment on column information_sort.info_id is 'インフォメーションID';
comment on column information_sort.hyouji_jun is '表示順';


-- 連絡票_0729_計上日入力制限
CREATE TABLE hizuke_control_tmp AS SELECT * FROM hizuke_control;
DROP TABLE hizuke_control;
create table hizuke_control (
  system_kanri_date date not null
  , fb_data_sakuseibi_flg character varying(1) not null
  , kojinseisan_shiharaibi date
  , kinyuukikan_eigyoubi_flg character varying(1) not null
  , toujitsu_kbn_flg character varying(1) not null
  , keijoubi_flg character varying(1) default '0' not null
  , constraint hizuke_control_PKEY primary key (system_kanri_date)
);
comment on table hizuke_control is '日付コントロール';
comment on column hizuke_control.system_kanri_date is 'システム管理日付';
comment on column hizuke_control.fb_data_sakuseibi_flg is 'FBデータ作成日フラグ';
comment on column hizuke_control.kojinseisan_shiharaibi is '個人精算支払日';
comment on column hizuke_control.kinyuukikan_eigyoubi_flg is '金融機関営業日フラグ';
comment on column hizuke_control.toujitsu_kbn_flg is '当日区分フラグ';
comment on column hizuke_control.keijoubi_flg is '計上日フラグ';
INSERT INTO hizuke_control
SELECT
  system_kanri_date
  , fb_data_sakuseibi_flg
  , kojinseisan_shiharaibi
  , kinyuukikan_eigyoubi_flg
  , toujitsu_kbn_flg
  , '0' --keijoubi_flg
FROM hizuke_control_tmp;
DROP TABLE hizuke_control_tmp;

UPDATE master_kanri_hansuu SET delete_flg='1' WHERE master_id='hizuke_control';
INSERT INTO master_kanri_hansuu( 
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
) 
VALUES ( 
  'hizuke_control'
  ,(SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id = 'hizuke_control') 
  , '0'
  , '日付コントロールマスター_patch.csv'
  , length(convert_to(E'システム管理日,FBデータ作成日フラグ,個人精算支払日,金融機関営業日フラグ,当日区分フラグ,計上日フラグ\r\nsystem_kanri_date,fb_data_sakuseibi_flg,kojinseisan_shiharaibi,kinyuukikan_eigyoubi_flg,toujitsu_kbn_flg,keijoubi_flg\r\ndate,varchar(1),date,varchar(1),varchar(1),varchar(1)\r\n1,2,,2,2,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT replace(system_kanri_date::text,'-'::text,'/'::text) || ',' || fb_data_sakuseibi_flg || ',' || COALESCE(replace(kojinseisan_shiharaibi::text,'-'::text,'/'::text),'') || ',' || kinyuukikan_eigyoubi_flg || ',' || toujitsu_kbn_flg || ',' || keijoubi_flg FROM hizuke_control ORDER BY system_kanri_date), E'\r\n'), 'sjis'))
  , 'application/vnd.ms-excel'
  , convert_to(E'システム管理日,FBデータ作成日フラグ,個人精算支払日,金融機関営業日フラグ,当日区分フラグ,計上日フラグ\r\nsystem_kanri_date,fb_data_sakuseibi_flg,kojinseisan_shiharaibi,kinyuukikan_eigyoubi_flg,toujitsu_kbn_flg,keijoubi_flg\r\ndate,varchar(1),date,varchar(1),varchar(1),varchar(1)\r\n1,2,,2,2,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT replace(system_kanri_date::text,'-'::text,'/'::text) || ',' || fb_data_sakuseibi_flg || ',' || COALESCE(replace(kojinseisan_shiharaibi::text,'-'::text,'/'::text),'') || ',' || kinyuukikan_eigyoubi_flg || ',' || toujitsu_kbn_flg || ',' || keijoubi_flg FROM hizuke_control ORDER BY system_kanri_date), E'\r\n'), 'sjis')
  ,'patch'
  , current_timestamp
  , 'patch'
  , current_timestamp
);

\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- 連絡票_0730_伝票一覧改良
ALTER TABLE denpyou_ichiran DROP CONSTRAINT IF EXISTS denpyou_ichiran_PKEY;
ALTER TABLE denpyou_ichiran RENAME TO denpyou_ichiran_tmp;

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
  , futan_bumon_cd character varying[]
  , kari_futan_bumon_cd character varying[]
  , kari_kamoku_cd character varying[]
  , kari_kamoku_edaban_cd character varying[]
  , kari_torihikisaki_cd character varying[]
  , kashi_futan_bumon_cd character varying[]
  , kashi_kamoku_cd character varying[]
  , kashi_kamoku_edaban_cd character varying[]
  , kashi_torihikisaki_cd character varying[]
  , meisai_kingaku numeric(15)[]
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
  , constraint denpyou_ichiran_PKEY primary key (denpyou_id)
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
comment on column denpyou_ichiran.futan_bumon_cd is '負担部門コード(一覧検索用)';
comment on column denpyou_ichiran.kari_futan_bumon_cd is '借方負担部門コード(一覧検索用)';
comment on column denpyou_ichiran.kari_kamoku_cd is '借方科目コード(一覧検索用)';
comment on column denpyou_ichiran.kari_kamoku_edaban_cd is '借方科目枝番コード(一覧検索用)';
comment on column denpyou_ichiran.kari_torihikisaki_cd is '借方取引先コード(一覧検索用)';
comment on column denpyou_ichiran.kashi_futan_bumon_cd is '貸方負担部門コード(一覧検索用)';
comment on column denpyou_ichiran.kashi_kamoku_cd is '貸方科目コード(一覧検索用)';
comment on column denpyou_ichiran.kashi_kamoku_edaban_cd is '貸方科目枝番コード(一覧検索用)';
comment on column denpyou_ichiran.kashi_torihikisaki_cd is '貸方取引先コード(一覧検索用)';
comment on column denpyou_ichiran.meisai_kingaku is '明細金額(一覧検索用)';
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
    ,name
    ,denpyou_kbn
    ,jisshi_kian_bangou
    ,shishutsu_kian_bangou
    ,yosan_shikkou_taishou
    ,yosan_check_nengetsu
    ,serial_no
    ,denpyou_shubetsu_url
    ,touroku_time
    ,bumon_full_name
    ,user_full_name
    ,user_id
    ,denpyou_joutai
    ,koushin_time
    ,shouninbi
    ,maruhi_flg
    ,all_cnt
    ,cur_cnt
    ,zan_cnt
    ,gen_bumon_full_name
    ,gen_user_full_name
    ,gen_gyoumu_role_name
    ,gen_name
    ,version
    ,kingaku
    ,gaika
    ,houjin_kingaku
    ,tehai_kingaku
    ,torihikisaki1
    ,shiharaibi
    ,shiharaikiboubi
    ,shiharaihouhou
    ,sashihiki_shikyuu_kingaku
    ,keijoubi
    ,shiwakekeijoubi
    ,seisan_yoteibi
    ,karibarai_denpyou_id
    ,houmonsaki
    ,mokuteki
    ,kenmei
    ,naiyou
    ,user_sei
    ,user_mei
    ,seisankikan_from
    ,seisankikan_to
    ,gen_user_id
    ,gen_gyoumu_role_id
    ,kian_bangou_unyou_flg
    ,yosan_shikkou_taishou_cd
    ,kian_syuryou_flg
    ,null--futan_bumon_cd
    ,null--kari_futan_bumon_cd
    ,null--kari_kamoku_cd
    ,null--kari_kamoku_edaban_cd
    ,null--kari_torihikisaki_cd
    ,null--kashi_futan_bumon_cd
    ,null--kashi_kamoku_cd
    ,null--kashi_kamoku_edaban_cd
    ,null--kashi_torihikisaki_cd
    ,null--meisai_kingaku
    ,tekiyou
    ,houjin_card_use
    ,kaisha_tehai_use
    ,ryoushuusho_exist
    ,miseisan_karibarai_exist
    ,miseisan_ukagai_exist
    ,shiwake_status
    ,fb_status
    ,jisshi_nendo
    ,shishutsu_nendo
    ,bumon_cd
    ,kian_bangou_input
FROM denpyou_ichiran_tmp;

DROP TABLE denpyou_ichiran_tmp;

-- 0742_「幣種別レートマスター」で同一幣種コードのレートを複数登録可能
ALTER TABLE rate_master RENAME TO rate_master_tmp;
create table rate_master (
  heishu_cd character varying(4) not null
  , start_date date not null
  , rate numeric(11, 5)
  , rate1 numeric(11, 5)
  , rate2 numeric(11, 5)
  , rate3 numeric(11, 5)
  , availability_flg smallint default 1 not null
  , primary key (heishu_cd)
);
comment on table rate_master is '幣種別レートマスター';
comment on column rate_master.heishu_cd is '幣種コード';
comment on column rate_master.start_date is '適用開始日時';
comment on column rate_master.rate is '適用レート';
comment on column rate_master.rate1 is '適用レート(予備)';
comment on column rate_master.rate2 is '適用レート(予備)';
comment on column rate_master.rate3 is '適用レート(予備)';
comment on column rate_master.availability_flg is '初期値使用可否';
INSERT INTO rate_master
SELECT
  heishu_cd
  , start_date
  , rate
  , rate1
  , rate2
  , rate3
  , availability_flg
FROM (SELECT * FROM rate_master_tmp
                 -- ①適用開始日がSQL実行日より前で一番日付が新しいやつ
                WHERE (heishu_cd, start_date) IN (SELECT heishu_cd, MAX(start_date) FROM rate_master_tmp WHERE start_date <= current_date GROUP BY heishu_cd)) tmp;

INSERT INTO rate_master
SELECT
  heishu_cd
  , start_date
  , rate
  , rate1
  , rate2
  , rate3
  , availability_flg
FROM (SELECT * FROM rate_master_tmp
                -- ②適用開始日が未来だけどその中で一番日付が小さいやつ
                -- （未来日のレートのみ1件登録されている場合や、複数件登録あるけど全部未来日の場合を考慮）
                WHERE (heishu_cd, start_date) IN (SELECT heishu_cd, MIN(start_date) FROM rate_master_tmp WHERE start_date > current_date GROUP BY heishu_cd)
                  AND heishu_cd NOT IN (SELECT heishu_cd FROM rate_master)) tmp;

DROP TABLE rate_master_tmp;

-- 0742_「幣種別レートマスター」で同一幣種コードのレートを複数登録可能
UPDATE master_kanri_hansuu SET delete_flg='1' WHERE master_id='rate_master';
INSERT INTO master_kanri_hansuu( 
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
) 
VALUES ( 
  'rate_master'
  ,(SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id = 'rate_master') 
  , '0'
  , '幣種別レートマスター_patch.csv'
  , length(convert_to(E'幣種コード,適用開始日時,適用レート,適用レート(予備),適用レート(予備),適用レート(予備),初期値使用可否\r\nheishu_cd,start_date,rate,rate1,rate2,rate3,availability_flg\r\nvarchar(4),date,decimal,decimal,decimal,decimal,smallint\r\n1,2,,,,,2\r\n' || ARRAY_TO_STRING(ARRAY (SELECT heishu_cd || ',' || start_date || ',' || COALESCE(rate::TEXT, '') || ',' || COALESCE(rate1::TEXT, '') || ',' || COALESCE(rate2::TEXT, '') || ',' || COALESCE(rate3::TEXT, '') || ',' || availability_flg FROM rate_master ORDER BY heishu_cd), E'\r\n'), 'sjis'))
  , 'application/vnd.ms-excel'
  , convert_to(E'幣種コード,適用開始日時,適用レート,適用レート(予備),適用レート(予備),適用レート(予備),初期値使用可否\r\nheishu_cd,start_date,rate,rate1,rate2,rate3,availability_flg\r\nvarchar(4),date,decimal,decimal,decimal,decimal,smallint\r\n1,2,,,,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT heishu_cd || ',' || start_date || ',' || COALESCE(rate::TEXT, '') || ',' || COALESCE(rate1::TEXT, '') || ',' || COALESCE(rate2::TEXT, '') || ',' || COALESCE(rate3::TEXT, '') || ',' || availability_flg FROM rate_master ORDER BY heishu_cd), E'\r\n'), 'sjis')
  ,'patch'
  , current_timestamp
  , 'patch'
  , current_timestamp
);


commit;
