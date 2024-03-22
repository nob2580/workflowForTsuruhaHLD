SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- Ýèîñf[^ÏX ¦hyouji_jun900È~ÍJX^}CYÌæ
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

-- vV[WÇÁ(Ver 17.08.04.06 patchÆ¯¶±Æ)
-- Sp¶ðSÄ¼p¶ÉAnCtÞðSÄu-vÉÏ·AAt@xbgÍ¬¶Éê
drop function if exists unify_kana_width(text);
create function unify_kana_width(chg_text text) returns text as $$
declare
    res_text text := trim(chg_text);
    arr_zenkaku_daku_kanas text[] := array['','K','M','O','Q','S','U','W','Y','[',']','_','a','d','f','h','o','r','u','x','{','p','s','v','y','|','[','|'];
    arr_hankaku_daku_kanas text[] := array['³Þ','¶Þ','·Þ','¸Þ','¹Þ','ºÞ','»Þ','¼Þ','½Þ','¾Þ','¿Þ','ÀÞ','ÁÞ','ÂÞ','ÃÞ','ÄÞ','ÊÞ','ËÞ','ÌÞ','ÍÞ','ÎÞ','Êß','Ëß','Ìß','Íß','Îß','-', '-' ];
    text_zenkaku_kanas text := 'ACEGIJLNPRTVXZ\^`cegijklmnqtwz}~@BDFHb';
    text_hankaku_kanas text := '±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜ¦Ý§¨©ª«¯¬­®';
    text_zenkaku_alphanum text := 'ABCDEFGHIJKLMNOPQRSTUVWXYZ`abcdefghijklmnopqrstuvwxyOPQRSTUVWX';
    text_hankaku_alphanum text := 'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz0123456789';
    text_zenkaku_kigou text := 'JKijopmnDC^[|°@';
    text_hankaku_kigou text := 'Þß(){}[].,/--- ';
begin
    if res_text is null or res_text = '' then
        return res_text;
    end if;
    for i in 1..array_length(arr_zenkaku_daku_kanas, 1) loop
        res_text := replace(res_text, arr_zenkaku_daku_kanas[i], arr_hankaku_daku_kanas[i]);
    end loop;
    res_text := translate(res_text, text_zenkaku_kanas || text_zenkaku_alphanum || text_zenkaku_kigou,
                                    text_hankaku_kanas || text_hankaku_alphanum || text_hankaku_kigou);  
    return res_text;
end;
$$ language plpgsql immutable
;


-- `[NÄR
ALTER TABLE DENPYOU_KIAN_HIMOZUKE RENAME TO DENPYOU_KIAN_HIMOZUKE_OLD;
CREATE TABLE DENPYOU_KIAN_HIMOZUKE
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	BUMON_CD VARCHAR(8),
	NENDO VARCHAR(4),
	RYAKUGOU VARCHAR(7),
	KIAN_BANGOU_FROM SMALLINT,
	KIAN_BANGOU SMALLINT,
	KIAN_SYURYO_FLG VARCHAR(1),
	KIAN_SYURYO_BI DATE,
	KIAN_DENPYOU VARCHAR(19),
	KIAN_DENPYOU_KBN VARCHAR(4),
	JISSHI_NENDO VARCHAR(4),
	JISSHI_KIAN_BANGOU VARCHAR(13),
	SHISHUTSU_NENDO VARCHAR(4),
	SHISHUTSU_KIAN_BANGOU VARCHAR(13),
	RINGI_KINGAKU DECIMAL(15),
	RINGI_KINGAKU_HIKITSUGIMOTO_DENPYOU VARCHAR(19) NOT NULL,
	RINGI_KINGAKU_CHOUKA_COMMENT VARCHAR(240) NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;
COMMENT ON TABLE DENPYOU_KIAN_HIMOZUKE IS '`[NÄRt';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.DENPYOU_ID IS '`[ID';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.BUMON_CD IS 'åR[h';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.NENDO IS 'Nx';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RYAKUGOU IS 'ª';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_BANGOU_FROM IS 'JnNÄÔ';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_BANGOU IS 'NÄÔ';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_SYURYO_FLG IS 'NÄI¹tO';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_SYURYO_BI IS 'NÄI¹ú';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_DENPYOU IS 'NÄ`[';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_DENPYOU_KBN IS 'NÄ`[æª';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.JISSHI_NENDO IS 'À{Nx';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.JISSHI_KIAN_BANGOU IS 'À{NÄÔ';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.SHISHUTSU_NENDO IS 'xoNx';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.SHISHUTSU_KIAN_BANGOU IS 'xoNÄÔ';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RINGI_KINGAKU IS 'âgcàz';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RINGI_KINGAKU_HIKITSUGIMOTO_DENPYOU IS 'âgcàzøp¬³`[';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RINGI_KINGAKU_CHOUKA_COMMENT IS 'âgcàz´ßRg';

INSERT 
INTO DENPYOU_KIAN_HIMOZUKE
SELECT 
	DENPYOU_ID,
	BUMON_CD,
	NENDO,
	RYAKUGOU,
	KIAN_BANGOU_FROM,
	KIAN_BANGOU,
	KIAN_SYURYO_FLG,
	KIAN_SYURYO_BI,
	KIAN_DENPYOU,
	KIAN_DENPYOU_KBN,
	JISSHI_NENDO,
	JISSHI_KIAN_BANGOU,
	SHISHUTSU_NENDO,
	SHISHUTSU_KIAN_BANGOU,
	RINGI_KINGAKU,
	'',
	''
FROM DENPYOU_KIAN_HIMOZUKE_OLD;

DROP TABLE DENPYOU_KIAN_HIMOZUKE_OLD;


-- æÊ À§äÌÇÁ
DELETE from gamen_kengen_seigyo;
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- }X^[úÔæêEÚ×iDE3j
DELETE FROM master_torikomi_term_ichiran_de3;
\copy master_torikomi_term_ichiran_de3 FROM '.\files\csv\master_torikomi_term_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM master_torikomi_term_shousai_de3;
\copy master_torikomi_term_shousai_de3 FROM '.\files\csv\master_torikomi_term_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';


commit;