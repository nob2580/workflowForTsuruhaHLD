SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- �ݒ���f�[�^�ύX ��hyouji_jun900�ȍ~�̓J�X�^�}�C�Y�̈�
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

-- �v���V�[�W���ǉ�(Ver 17.08.04.06 patch�Ɠ�������)
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


-- �`�[�N�ĕR
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
COMMENT ON TABLE DENPYOU_KIAN_HIMOZUKE IS '�`�[�N�ĕR�t';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.BUMON_CD IS '����R�[�h';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.NENDO IS '�N�x';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RYAKUGOU IS '����';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_BANGOU_FROM IS '�J�n�N�Ĕԍ�';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_BANGOU IS '�N�Ĕԍ�';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_SYURYO_FLG IS '�N�ďI���t���O';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_SYURYO_BI IS '�N�ďI����';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_DENPYOU IS '�N�ē`�[';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.KIAN_DENPYOU_KBN IS '�N�ē`�[�敪';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.JISSHI_NENDO IS '���{�N�x';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.JISSHI_KIAN_BANGOU IS '���{�N�Ĕԍ�';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.SHISHUTSU_NENDO IS '�x�o�N�x';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.SHISHUTSU_KIAN_BANGOU IS '�x�o�N�Ĕԍ�';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RINGI_KINGAKU IS '�g�c���z';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RINGI_KINGAKU_HIKITSUGIMOTO_DENPYOU IS '�g�c���z���p�����`�[';
COMMENT ON COLUMN DENPYOU_KIAN_HIMOZUKE.RINGI_KINGAKU_CHOUKA_COMMENT IS '�g�c���z���߃R�����g';

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


-- ��ʌ�������̒ǉ�
DELETE from gamen_kengen_seigyo;
\copy gamen_kengen_seigyo FROM '.\files\csv\gamen_kengen_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';


-- �}�X�^�[���Ԏ捞�ꗗ�E�ڍׁiDE3�j
DELETE FROM master_torikomi_term_ichiran_de3;
\copy master_torikomi_term_ichiran_de3 FROM '.\files\csv\master_torikomi_term_ichiran_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM master_torikomi_term_shousai_de3;
\copy master_torikomi_term_shousai_de3 FROM '.\files\csv\master_torikomi_term_shousai_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';


commit;