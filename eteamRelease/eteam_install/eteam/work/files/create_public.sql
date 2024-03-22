SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

-- �S�p������S�Ĕ��p�����ɁA�n�C�t���ނ�S�āu-�v�ɕϊ��A�A���t�@�x�b�g�͏������ɓ���
drop function if exists unify_kana_width(text);
create function unify_kana_width(chg_text text) returns text as $$
declare
    res_text text := trim(chg_text);
    arr_zenkaku_daku_kanas text[] := array['��','�K','�M','�O','�Q','�S','�U','�W','�Y','�[','�]','�_','�a','�d','�f','�h','�o','�r','�u','�x','�{','�p','�s','�v','�y','�|','�[','�|'];
    arr_hankaku_daku_kanas text[] := array['��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','��','-', '-' ];
    text_zenkaku_kanas text := '�A�C�E�G�I�J�L�N�P�R�T�V�X�Z�\�^�`�c�e�g�i�j�k�l�m�n�q�t�w�z�}�~�����������������������������@�B�D�F�H�b������';
    text_hankaku_kanas text := '�������������������������������������������ܦݧ��������';
    text_zenkaku_alphanum text := 'ABCDEFGHIJKLMNOPQRSTUVWXYZ�`�a�b�c�d�e�f�g�h�i�j�k�l�m�n�o�p�q�r�s�t�u�v�w�x�y�����������������������������������������������������O�P�Q�R�S�T�U�V�W�X';
    text_hankaku_alphanum text := 'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz0123456789';
    text_zenkaku_kigou text := '�J�K�i�j�o�p�m�n�D�C�^�[�|��@';
    text_hankaku_kigou text := '��(){}[].,/--- ';
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

-- ���[�U�Z�b�V����
create table if not exists public.user_session (
  jsession_id character varying(32) not null
  , schema character varying(32) not null
  , data bytea not null
  , time timestamp(6) without time zone not null
  , primary key (jsession_id,schema)
);
