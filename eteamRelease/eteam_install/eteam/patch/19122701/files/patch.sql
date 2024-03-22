SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- K-157
-- ファイル添付機能がOFFならファイル添付必須フラグを"0"にする
UPDATE shiwake_pattern_master_zaimu_kyoten set tenpu_file_hissu_flg = '0' 
WHERE '0' = (SELECT kinou_seigyo_kbn FROM kinou_seigyo WHERE kinou_seigyo_cd = 'TP');

commit;
