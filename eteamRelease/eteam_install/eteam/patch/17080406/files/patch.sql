SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- vV[WÇÁ
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

commit;