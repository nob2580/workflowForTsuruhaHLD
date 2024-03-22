SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- 設定情報データ変更 ※hyouji_jun900以降はカスタマイズ領域
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

-- プロシージャ追加(Ver 17.08.04.06 patchと同じこと)
-- 全角文字を全て半角文字に、ハイフン類を全て「-」に変換、アルファベットは小文字に統一
drop function if exists unify_kana_width(text);
create function unify_kana_width(chg_text text) returns text as $$
declare
    res_text text := trim(chg_text);
    arr_zenkaku_daku_kanas text[] := array['ヴ','ガ','ギ','グ','ゲ','ゴ','ザ','ジ','ズ','ゼ','ゾ','ダ','ヂ','ヅ','デ','ド','バ','ビ','ブ','ベ','ボ','パ','ピ','プ','ペ','ポ','ー','－'];
    arr_hankaku_daku_kanas text[] := array['ｳﾞ','ｶﾞ','ｷﾞ','ｸﾞ','ｹﾞ','ｺﾞ','ｻﾞ','ｼﾞ','ｽﾞ','ｾﾞ','ｿﾞ','ﾀﾞ','ﾁﾞ','ﾂﾞ','ﾃﾞ','ﾄﾞ','ﾊﾞ','ﾋﾞ','ﾌﾞ','ﾍﾞ','ﾎﾞ','ﾊﾟ','ﾋﾟ','ﾌﾟ','ﾍﾟ','ﾎﾟ','-', '-' ];
    text_zenkaku_kanas text := 'アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲンァィゥェォッャュョ';
    text_hankaku_kanas text := 'ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜｦﾝｧｨｩｪｫｯｬｭｮ';
    text_zenkaku_alphanum text := 'ABCDEFGHIJKLMNOPQRSTUVWXYZＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ０１２３４５６７８９';
    text_hankaku_alphanum text := 'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz0123456789';
    text_zenkaku_kigou text := '゛゜（）｛｝［］．，／ー－ｰ　';
    text_hankaku_kigou text := 'ﾞﾟ(){}[].,/--- ';
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


-- 伝票起案紐
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
COMMENT ON TABLE DENPYOU_KIAN_HIMOZUKE IS '伝票起案紐付';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.DENPYOU_ID IS '伝票ID';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.BUMON_CD IS '部門コード';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.NENDO IS '年度';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RYAKUGOU IS '略号';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_BANGOU_FROM IS '開始起案番号';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_BANGOU IS '起案番号';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_SYURYO_FLG IS '起案終了フラグ';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_SYURYO_BI IS '起案終了日';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_DENPYOU IS '起案伝票';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_DENPYOU_KBN IS '起案伝票区分';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.JISSHI_NENDO IS '実施年度';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.JISSHI_KIAN_BANGOU IS '実施起案番号';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.SHISHUTSU_NENDO IS '支出年度';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.SHISHUTSU_KIAN_BANGOU IS '支出起案番号';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RINGI_KINGAKU IS '稟議金額';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RINGI_KINGAKU_HIKITSUGIMOTO_DENPYOU IS '稟議金額引継ぎ元伝票';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RINGI_KINGAKU_CHOUKA_COMMENT IS '稟議金額超過コメント';

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


-- 画面権限制御の追加
DELETE from gamen_kengen_seigyo;
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- マスター期間取込一覧・詳細（DE3）
DELETE FROM master_torikomi_term_ichiran_de3;
\copy master_torikomi_term_ichiran_de3 FROM '.\files\csv\master_torikomi_term_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM master_torikomi_term_shousai_de3;
\copy master_torikomi_term_shousai_de3 FROM '.\files\csv\master_torikomi_term_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';


commit;