SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 添付ファイル名にUTF-8のノーブレークスペース(&nbsp)がある場合通常半角スペースに変換
UPDATE tenpu_file SET file_name = REPLACE(file_name, CONVERT_FROM('\xc2a0', 'utf8'), CONVERT_FROM('\x20', 'utf8'));

commit;