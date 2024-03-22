SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 添付ファイル名に『,(半角カンマ)』がある場合『、(読点)』に変換
UPDATE tenpu_file SET file_name = REPLACE(file_name, ',' , '、' );

commit;