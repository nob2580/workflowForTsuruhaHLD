SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 0751_「幣種別レートマスター」の適用開始日時が正しくない形式に変換されている
DELETE FROM  master_kanri_hansuu WHERE master_id='rate_master' AND delete_flg='0';
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
  , length(convert_to(E'幣種コード,適用開始日時,適用レート,適用レート(予備),適用レート(予備),適用レート(予備),初期値使用可否\r\nheishu_cd,start_date,rate,rate1,rate2,rate3,availability_flg\r\nvarchar(4),date,decimal,decimal,decimal,decimal,smallint\r\n1,2,,,,,2\r\n' || ARRAY_TO_STRING(ARRAY (SELECT heishu_cd || ',' || TO_CHAR(start_date, 'YYYY/MM/DD') || ',' || COALESCE(rate::TEXT, '') || ',' || COALESCE(rate1::TEXT, '') || ',' || COALESCE(rate2::TEXT, '') || ',' || COALESCE(rate3::TEXT, '') || ',' || availability_flg FROM rate_master ORDER BY heishu_cd), E'\r\n'), 'sjis'))
  , 'application/vnd.ms-excel'
  , convert_to(E'幣種コード,適用開始日時,適用レート,適用レート(予備),適用レート(予備),適用レート(予備),初期値使用可否\r\nheishu_cd,start_date,rate,rate1,rate2,rate3,availability_flg\r\nvarchar(4),date,decimal,decimal,decimal,decimal,smallint\r\n1,2,,,,,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT heishu_cd || ',' || TO_CHAR(start_date, 'YYYY/MM/DD') || ',' || COALESCE(rate::TEXT, '') || ',' || COALESCE(rate1::TEXT, '') || ',' || COALESCE(rate2::TEXT, '') || ',' || COALESCE(rate3::TEXT, '') || ',' || availability_flg FROM rate_master ORDER BY heishu_cd), E'\r\n'), 'sjis')
  ,'patch'
  , current_timestamp
  , 'patch'
  , current_timestamp
);

-- ICSP連絡票_0529_改正消費税_財務マスターの状態に応じたインポーターサブ切替
DELETE FROM master_torikomi_ichiran_de3;
DELETE FROM master_torikomi_shousai_de3;
DELETE FROM master_torikomi_ichiran_sias;
DELETE FROM master_torikomi_shousai_sias;
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- ICSP連絡票_0529_改正消費税_財務マスターの状態に応じたインポーターサブ切替
ALTER TABLE kaisha_info RENAME TO kaisha_info_tmp;
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
  , keigen_umu_flg character varying(1) not null
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
comment on column kaisha_info.keigen_umu_flg is '軽減有無フラグ';
INSERT INTO kaisha_info
SELECT
	 kessanki_bangou
	,hf1_shiyou_flg
	,hf1_hissu_flg
	,hf1_name
	,hf2_shiyou_flg
	,hf2_hissu_flg
	,hf2_name
	,hf3_shiyou_flg
	,hf3_hissu_flg
	,hf3_name
	,hf4_shiyou_flg
	,hf4_hissu_flg
	,hf4_name
	,hf5_shiyou_flg
	,hf5_hissu_flg
	,hf5_name
	,hf6_shiyou_flg
	,hf6_hissu_flg
	,hf6_name
	,hf7_shiyou_flg
	,hf7_hissu_flg
	,hf7_name
	,hf8_shiyou_flg
	,hf8_hissu_flg
	,hf8_name
	,hf9_shiyou_flg
	,hf9_hissu_flg
	,hf9_name
	,hf10_shiyou_flg
	,hf10_hissu_flg
	,hf10_name
	,uf1_shiyou_flg
	,uf1_name
	,uf2_shiyou_flg
	,uf2_name
	,uf3_shiyou_flg
	,uf3_name
	,uf4_shiyou_flg
	,uf4_name
	,uf5_shiyou_flg
	,uf5_name
	,uf6_shiyou_flg
	,uf6_name
	,uf7_shiyou_flg
	,uf7_name
	,uf8_shiyou_flg
	,uf8_name
	,uf9_shiyou_flg
	,uf9_name
	,uf10_shiyou_flg
	,uf10_name
	,uf_kotei1_shiyou_flg
	,uf_kotei1_name
	,uf_kotei2_shiyou_flg
	,uf_kotei2_name
	,uf_kotei3_shiyou_flg
	,uf_kotei3_name
	,uf_kotei4_shiyou_flg
	,uf_kotei4_name
	,uf_kotei5_shiyou_flg
	,uf_kotei5_name
	,uf_kotei6_shiyou_flg
	,uf_kotei6_name
	,uf_kotei7_shiyou_flg
	,uf_kotei7_name
	,uf_kotei8_shiyou_flg
	,uf_kotei8_name
	,uf_kotei9_shiyou_flg
	,uf_kotei9_name
	,uf_kotei10_shiyou_flg
	,uf_kotei10_name
	,pjcd_shiyou_flg
	,sgcd_shiyou_flg
	,saimu_shiyou_flg
	,kamoku_cd_type
	,kamoku_edaban_cd_type
	,futan_bumon_cd_type
	,torihikisaki_cd_type
	,segment_cd_type
	,houjin_bangou
	,'0'		--keigen_umu_flg
FROM kaisha_info_tmp;
DROP TABLE kaisha_info_tmp;

commit;
