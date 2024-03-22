SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- ‰æ–Ê€–Ú§Œä‚ÌC³
UPDATE gamen_koumoku_seigyo SET
  koumoku_name = default_koumoku_name
WHERE
  denpyou_kbn = 'A004' AND koumoku_id = 'karibarai_sentaku';

UPDATE gamen_koumoku_seigyo SET
  hyouji_flg = '0',
  hissu_flg = '0'
WHERE
  koumoku_id = 'kyuujitsu_nissuu';

commit;
