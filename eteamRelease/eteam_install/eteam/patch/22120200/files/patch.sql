SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- denpypu_ichiran‚Ì¸ZŠúŠÔ‚Ì•â[
UPDATE denpyou_ichiran SET seisankikan_from = k.seisankikan_from, seisankikan_to = k.seisankikan_to FROM koutsuuhiseisan k WHERE denpyou_ichiran.denpyou_id = k.denpyou_id;

commit;
