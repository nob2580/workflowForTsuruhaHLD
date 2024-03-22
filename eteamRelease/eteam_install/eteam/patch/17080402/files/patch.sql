SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 海外用交通手段マスター
CREATE TABLE KAIGAI_KOUTSUU_SHUDAN_MASTER
(
	SORT_JUN CHAR(3) NOT NULL,
	KOUTSUU_SHUDAN VARCHAR(10) NOT NULL,
	SHOUHYOU_SHORUI_HISSU_FLG VARCHAR(1) NOT NULL,
	ZEI_KUBUN VARCHAR(1),
	PRIMARY KEY (SORT_JUN, KOUTSUU_SHUDAN)
) WITHOUT OIDS;
COMMENT ON TABLE KAIGAI_KOUTSUU_SHUDAN_MASTER IS '海外用交通手段マスター';
COMMENT ON COLUMN KAIGAI_KOUTSUU_SHUDAN_MASTER.SORT_JUN IS '並び順';
COMMENT ON COLUMN KAIGAI_KOUTSUU_SHUDAN_MASTER.KOUTSUU_SHUDAN IS '交通手段';
COMMENT ON COLUMN KAIGAI_KOUTSUU_SHUDAN_MASTER.SHOUHYOU_SHORUI_HISSU_FLG IS '証憑書類必須フラグ';
COMMENT ON COLUMN KAIGAI_KOUTSUU_SHUDAN_MASTER.ZEI_KUBUN IS '税区分';

-- 海外用日当等マスター
ALTER TABLE KAIGAI_NITTOU_NADO_MASTER RENAME TO KAIGAI_NITTOU_NADO_MASTER_OLD;
CREATE TABLE KAIGAI_NITTOU_NADO_MASTER
(
	SHUBETSU1 VARCHAR(20) NOT NULL,
	SHUBETSU2 VARCHAR(20) NOT NULL,
	YAKUSHOKU_CD VARCHAR(10) NOT NULL,
	TANKA DECIMAL(15),
	SHOUHYOU_SHORUI_HISSU_FLG VARCHAR(1) NOT NULL,
	NITTOU_SHUKUHAKUHI_FLG VARCHAR(1) NOT NULL,
	ZEI_KUBUN VARCHAR(1),
	PRIMARY KEY (SHUBETSU1, SHUBETSU2, YAKUSHOKU_CD)
) WITHOUT OIDS;
COMMENT ON TABLE KAIGAI_NITTOU_NADO_MASTER IS '海外用日当等マスター';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.SHUBETSU1 IS '種別１';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.SHUBETSU2 IS '種別２';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.YAKUSHOKU_CD IS '役職コード';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.TANKA IS '単価';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.SHOUHYOU_SHORUI_HISSU_FLG IS '証憑書類必須フラグ';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.NITTOU_SHUKUHAKUHI_FLG IS '日当・宿泊費フラグ';
COMMENT ON COLUMN KAIGAI_NITTOU_NADO_MASTER.ZEI_KUBUN IS '税区分';
INSERT INTO KAIGAI_NITTOU_NADO_MASTER(
	SHUBETSU1,
	SHUBETSU2,
	YAKUSHOKU_CD,
	TANKA,
	SHOUHYOU_SHORUI_HISSU_FLG,
	NITTOU_SHUKUHAKUHI_FLG,
	ZEI_KUBUN
)
SELECT
	SHUBETSU1,
	SHUBETSU2,
	YAKUSHOKU_CD,
	TANKA,
	SHOUHYOU_SHORUI_HISSU_FLG,
	NITTOU_SHUKUHAKUHI_FLG,
	''
FROM KAIGAI_NITTOU_NADO_MASTER_OLD;
DROP TABLE KAIGAI_NITTOU_NADO_MASTER_OLD;

-- 国内用交通手段マスター
ALTER TABLE KOUTSUU_SHUDAN_MASTER RENAME TO KOUTSUU_SHUDAN_MASTER_OLD;
CREATE TABLE KOUTSUU_SHUDAN_MASTER
(
	SORT_JUN CHAR(3) NOT NULL,
	KOUTSUU_SHUDAN VARCHAR(10) NOT NULL,
	SHOUHYOU_SHORUI_HISSU_FLG VARCHAR(1) NOT NULL,
	ZEI_KUBUN VARCHAR(1),
	PRIMARY KEY (SORT_JUN, KOUTSUU_SHUDAN)
) WITHOUT OIDS;
COMMENT ON TABLE KOUTSUU_SHUDAN_MASTER IS '国内用交通手段マスター';
COMMENT ON COLUMN KOUTSUU_SHUDAN_MASTER.SORT_JUN IS '並び順';
COMMENT ON COLUMN KOUTSUU_SHUDAN_MASTER.KOUTSUU_SHUDAN IS '交通手段';
COMMENT ON COLUMN KOUTSUU_SHUDAN_MASTER.SHOUHYOU_SHORUI_HISSU_FLG IS '証憑書類必須フラグ';
COMMENT ON COLUMN KOUTSUU_SHUDAN_MASTER.ZEI_KUBUN IS '税区分';
INSERT INTO KOUTSUU_SHUDAN_MASTER(
	SORT_JUN,
	KOUTSUU_SHUDAN,
	SHOUHYOU_SHORUI_HISSU_FLG,
	ZEI_KUBUN
)
SELECT
	SORT_JUN,
	KOUTSUU_SHUDAN,
	SHOUHYOU_SHORUI_HISSU_FLG,
	''
FROM KOUTSUU_SHUDAN_MASTER_OLD;
DROP TABLE KOUTSUU_SHUDAN_MASTER_OLD;


-- 国内用日当等マスター
ALTER TABLE NITTOU_NADO_MASTER RENAME TO NITTOU_NADO_MASTER_OLD;
CREATE TABLE NITTOU_NADO_MASTER
(
	SHUBETSU1 VARCHAR(20) NOT NULL,
	SHUBETSU2 VARCHAR(20) NOT NULL,
	YAKUSHOKU_CD VARCHAR(10) NOT NULL,
	TANKA DECIMAL(15),
	SHOUHYOU_SHORUI_HISSU_FLG VARCHAR(1) NOT NULL,
	NITTOU_SHUKUHAKUHI_FLG VARCHAR(1) NOT NULL,
	ZEI_KUBUN VARCHAR(1),
	PRIMARY KEY (SHUBETSU1, SHUBETSU2, YAKUSHOKU_CD)
) WITHOUT OIDS;

COMMENT ON TABLE NITTOU_NADO_MASTER IS '国内用日当等マスター';
COMMENT ON COLUMN NITTOU_NADO_MASTER.SHUBETSU1 IS '種別１';
COMMENT ON COLUMN NITTOU_NADO_MASTER.SHUBETSU2 IS '種別２';
COMMENT ON COLUMN NITTOU_NADO_MASTER.YAKUSHOKU_CD IS '役職コード';
COMMENT ON COLUMN NITTOU_NADO_MASTER.TANKA IS '単価';
COMMENT ON COLUMN NITTOU_NADO_MASTER.SHOUHYOU_SHORUI_HISSU_FLG IS '証憑書類必須フラグ';
COMMENT ON COLUMN NITTOU_NADO_MASTER.NITTOU_SHUKUHAKUHI_FLG IS '日当・宿泊費フラグ';
COMMENT ON COLUMN NITTOU_NADO_MASTER.ZEI_KUBUN IS '税区分';
INSERT INTO NITTOU_NADO_MASTER(
	SHUBETSU1,
	SHUBETSU2,
	YAKUSHOKU_CD,
	TANKA,
	SHOUHYOU_SHORUI_HISSU_FLG,
	NITTOU_SHUKUHAKUHI_FLG,
	ZEI_KUBUN
)
SELECT
	SHUBETSU1,
	SHUBETSU2,
	YAKUSHOKU_CD,
	TANKA,
	SHOUHYOU_SHORUI_HISSU_FLG,
	NITTOU_SHUKUHAKUHI_FLG,
	''
FROM NITTOU_NADO_MASTER_OLD;
DROP TABLE NITTOU_NADO_MASTER_OLD;

-- マスター管理一覧（海外用交通手段マスターの追加、国内用交通手段マスターの変更）
\copy MASTER_KANRI_ICHIRAN         FROM '.\files\csv\master_kanri_ichiran_patch.csv'         WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE MASTER_KANRI_ICHIRAN SET MASTER_NAME = '国内用交通手段マスター' WHERE MASTER_ID = 'koutsuu_shudan_master';

-- マスター管理版数（海外用日当等マスターは17.08.04.00版で追加済みなので削除＆追加、海外用交通手段マスターの追加）
DELETE FROM MASTER_KANRI_HANSUU WHERE MASTER_ID = 'kaigai_nittou_nado_master';
\copy MASTER_KANRI_HANSUU          FROM '.\files\csv\master_kanri_hansuu_patch.csv'          WITH CSV header ENCODING 'SHIFT-JIS';

-- マスター管理版数（国内交通手段マスターを追加）
UPDATE MASTER_KANRI_HANSUU SET DELETE_FLG = '1' WHERE MASTER_ID = 'koutsuu_shudan_master';
INSERT INTO MASTER_KANRI_HANSUU 
(
  MASTER_ID
  , VERSION
  , DELETE_FLG
  , FILE_NAME
  , FILE_SIZE
  , CONTENT_TYPE
  , BINARY_DATA
  , TOUROKU_USER_ID 
  , TOUROKU_TIME 
  , KOUSHIN_USER_ID 
  , KOUSHIN_TIME 
) VALUES (
   'koutsuu_shudan_master'
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='koutsuu_shudan_master')
 , '0'
 , '国内用交通手段マスター_patch.csv'
 , length(convert_to(E'並び順,交通手段,証憑書類必須フラグ,税区分\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun\r\nchar(3),varchar(10),varchar(1),varchar(1)\r\n1,1,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun FROM koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'並び順,交通手段,証憑書類必須フラグ,税区分\r\nsort_jun,koutsuu_shudan,shouhyou_shorui_hissu_flg,zei_kubun\r\nchar(3),varchar(10),varchar(1),varchar(1)\r\n1,1,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || koutsuu_shudan || ',' || shouhyou_shorui_hissu_flg || ',' || zei_kubun FROM koutsuu_shudan_master), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
);

-- マスター管理版数（国内日当等マスターは17.08.04.00版で追加済みなのでここでは更新）
UPDATE MASTER_KANRI_HANSUU
SET
   file_size = length(convert_to(E'種別１,種別２,役職コード,単価,証憑書類必須フラグ,日当・宿泊費フラグ,税区分\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(1),varchar(1),varchar(1)\r\n1,1,1,,2,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun FROM nittou_nado_master), E'\r\n') || E'\r\n','sjis') )
 , binary_data = convert_to(E'種別１,種別２,役職コード,単価,証憑書類必須フラグ,日当・宿泊費フラグ,税区分\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg,zei_kubun\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(1),varchar(1),varchar(1)\r\n1,1,1,,2,2,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg || ',' || zei_kubun FROM nittou_nado_master), E'\r\n') || E'\r\n','sjis')
WHERE
  MASTER_ID = 'nittou_nado_master' 
AND
  DELETE_FLG = '0'
;

DROP TABLE IF EXISTS naibu_cd_setting_tmp; 


commit;