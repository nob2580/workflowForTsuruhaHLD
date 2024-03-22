SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


--Ver 19.07.19.06‚ÌNo.811‘Î‰‚Å‚Ìè“–
UPDATE ryohiseisan_meisai SET
	nissuu = TRUNC((meisai_kingaku / tanka) / 0.5, 0) * 0.5 --0.5’PˆÊØ‚èÌ‚Ä
WHERE
	shubetsu_cd = '2' AND
	nissuu IS NULL;

UPDATE ryohiseisan_meisai SET
	nissuu = TRUNC(nissuu, 1) --¬”“_1Œ…‚Ü‚Å•Û
WHERE
	NOT(nissuu IS NULL);

UPDATE ryohiseisan_meisai SET
	nissuu = TRUNC(nissuu, 0) --¬”“_0Œ…‚Ü‚Å•Û
WHERE
	NOT(nissuu IS NULL) AND nissuu % 1 = 0;

UPDATE ryohiseisan_meisai SET	
	kyuujitsu_nissuu = kikan_to - kikan_from + 1 - nissuu
WHERE	
	shubetsu_cd = '2' AND
	kyuujitsu_nissuu IS NULL AND
	(SELECT hyouji_flg FROM gamen_koumoku_seigyo WHERE denpyou_kbn = 'A004' AND koumoku_id = 'kyuujitsu_nissuu') = '1';
	
UPDATE kaigai_ryohi_karibarai_meisai SET	
	kyuujitsu_nissuu = kikan_to - kikan_from + 1 - nissuu
WHERE	
	shubetsu_cd = '2' AND
	kyuujitsu_nissuu IS NULL AND
	(SELECT hyouji_flg FROM gamen_koumoku_seigyo WHERE denpyou_kbn = 'A012' AND koumoku_id = 'kyuujitsu_nissuu') = '1';


commit;
