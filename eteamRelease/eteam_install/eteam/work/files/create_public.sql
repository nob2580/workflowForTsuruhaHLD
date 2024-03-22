SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

-- ‘SŠp•¶š‚ğ‘S‚Ä”¼Šp•¶š‚ÉAƒnƒCƒtƒ“—Ş‚ğ‘S‚Äu-v‚É•ÏŠ·AƒAƒ‹ƒtƒ@ƒxƒbƒg‚Í¬•¶š‚É“ˆê
drop function if exists unify_kana_width(text);
create function unify_kana_width(chg_text text) returns text as $$
declare
    res_text text := trim(chg_text);
    arr_zenkaku_daku_kanas text[] := array['ƒ”','ƒK','ƒM','ƒO','ƒQ','ƒS','ƒU','ƒW','ƒY','ƒ[','ƒ]','ƒ_','ƒa','ƒd','ƒf','ƒh','ƒo','ƒr','ƒu','ƒx','ƒ{','ƒp','ƒs','ƒv','ƒy','ƒ|','[','|'];
    arr_hankaku_daku_kanas text[] := array['³Ş','¶Ş','·Ş','¸Ş','¹Ş','ºŞ','»Ş','¼Ş','½Ş','¾Ş','¿Ş','ÀŞ','ÁŞ','ÂŞ','ÃŞ','ÄŞ','ÊŞ','ËŞ','ÌŞ','ÍŞ','ÎŞ','Êß','Ëß','Ìß','Íß','Îß','-', '-' ];
    text_zenkaku_kanas text := 'ƒAƒCƒEƒGƒIƒJƒLƒNƒPƒRƒTƒVƒXƒZƒ\ƒ^ƒ`ƒcƒeƒgƒiƒjƒkƒlƒmƒnƒqƒtƒwƒzƒ}ƒ~ƒ€ƒƒ‚ƒ„ƒ†ƒˆƒ‰ƒŠƒ‹ƒŒƒƒƒ’ƒ“ƒ@ƒBƒDƒFƒHƒbƒƒƒ…ƒ‡';
    text_hankaku_kanas text := '±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏĞÑÒÓÔÕÖ×ØÙÚÛÜ¦İ§¨©ª«¯¬­®';
    text_zenkaku_alphanum text := 'ABCDEFGHIJKLMNOPQRSTUVWXYZ‚`‚a‚b‚c‚d‚e‚f‚g‚h‚i‚j‚k‚l‚m‚n‚o‚p‚q‚r‚s‚t‚u‚v‚w‚x‚y‚‚‚‚ƒ‚„‚…‚†‚‡‚ˆ‚‰‚Š‚‹‚Œ‚‚‚‚‚‘‚’‚“‚”‚•‚–‚—‚˜‚™‚š‚O‚P‚Q‚R‚S‚T‚U‚V‚W‚X';
    text_hankaku_alphanum text := 'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz0123456789';
    text_zenkaku_kigou text := 'JKijopmnDC^[|°@';
    text_hankaku_kigou text := 'Şß(){}[].,/--- ';
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

-- ƒ†[ƒUƒZƒbƒVƒ‡ƒ“
create table if not exists public.user_session (
  jsession_id character varying(32) not null
  , schema character varying(32) not null
  , data bytea not null
  , time timestamp(6) without time zone not null
  , primary key (jsession_id,schema)
);
