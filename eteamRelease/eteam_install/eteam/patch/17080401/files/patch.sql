
SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 国内用日当等マスター管理版数のfile_size、binary_dateを変更
UPDATE
  master_kanri_hansuu
SET
  file_size = length(convert_to(E'種別１,種別２,役職コード,単価,証憑書類必須フラグ,日当・宿泊費フラグ\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(1),varchar(1)\r\n1,1,1,,2,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg FROM nittou_nado_master), E'\r\n') || E'\r\n','sjis') )
 ,binary_data = convert_to(E'種別１,種別２,役職コード,単価,証憑書類必須フラグ,日当・宿泊費フラグ\r\nshubetsu1,shubetsu2,yakushoku_cd,tanka,shouhyou_shorui_hissu_flg,nittou_shukuhakuhi_flg\r\nvarchar(20),varchar(20),varchar(10),decimal(15),varchar(1),varchar(1)\r\n1,1,1,,2,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shubetsu1 || ',' || shubetsu2 || ',' || yakushoku_cd || ',' || COALESCE(tanka::TEXT, '') || ',' || shouhyou_shorui_hissu_flg || ',' || nittou_shukuhakuhi_flg FROM nittou_nado_master), E'\r\n') || E'\r\n','sjis')
WHERE
  master_id = 'nittou_nado_master' 
AND
  delete_flg = '0'
;

commit;