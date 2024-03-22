SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- ��ʍ��ڐ���
CREATE TABLE gamen_koumoku_seigyo_tmp AS SELECT * FROM gamen_koumoku_seigyo;
DELETE FROM gamen_koumoku_seigyo WHERE denpyou_kbn LIKE 'A%';
\copy gamen_koumoku_seigyo FROM '..\..\work\files\csv\gamen_koumoku_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE gamen_koumoku_seigyo new SET
  koumoku_name = (SELECT koumoku_name FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp);
UPDATE gamen_koumoku_seigyo new SET
   hyouji_flg  = (SELECT hyouji_flg   FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
  ,hissu_flg   = (SELECT hissu_flg    FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE hyouji_seigyo_flg = '1');
UPDATE gamen_koumoku_seigyo new SET
  pdf_hyouji_flg = (SELECT pdf_hyouji_flg FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE pdf_hyouji_seigyo_flg = '1');
UPDATE gamen_koumoku_seigyo new SET
  code_output_flg = (SELECT code_output_flg FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE code_output_seigyo_flg = '1');
-- ����K-133 ��������
-- �A�b�v�f�[�g�O�ɍ��ڒl���f�t�H���g���疢�ύX�̍��ڂ́A���ږ����f�t�H���g���ږ��Ɠ����ɂ���
UPDATE gamen_koumoku_seigyo new SET
  koumoku_name = default_koumoku_name
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE default_koumoku_name != '' AND default_koumoku_name = koumoku_name AND denpyou_kbn = 'Z001');
-- ���������܂�
DROP TABLE gamen_koumoku_seigyo_tmp;

-- ��ʔ�Z���׃e�[�u���ɑ��t���O�A���t���O�A�y�t���O��ǉ�
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS haya_flg character varying(1) NOT NULL default '0';
comment on column koutsuuhiseisan_meisai.haya_flg is '���t���O';
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS yasu_flg character varying(1) NOT NULL default '0';
comment on column koutsuuhiseisan_meisai.yasu_flg is '���t���O';
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS raku_flg character varying(1) NOT NULL default '0';
comment on column koutsuuhiseisan_meisai.raku_flg is '�y�t���O';

-- �C�O����Z���׃e�[�u���ɑ��t���O�A���t���O�A�y�t���O��ǉ�
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS haya_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohiseisan_meisai.haya_flg is '���t���O';
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS yasu_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohiseisan_meisai.yasu_flg is '���t���O';
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS raku_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohiseisan_meisai.raku_flg is '�y�t���O';


-- �C�O��������׃e�[�u���ɑ��t���O�A���t���O�A�y�t���O��ǉ�
ALTER TABLE kaigai_ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS haya_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohi_karibarai_meisai.haya_flg is '���t���O';
ALTER TABLE kaigai_ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS yasu_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohi_karibarai_meisai.yasu_flg is '���t���O';
ALTER TABLE kaigai_ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS raku_flg character varying(1) NOT NULL default '0';
comment on column kaigai_ryohi_karibarai_meisai.raku_flg is '�y�t���O';

-- ����Z���׃e�[�u���ɑ��t���O�A���t���O�A�y�t���O��ǉ�
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS haya_flg character varying(1) NOT NULL default '0';
comment on column ryohiseisan_meisai.haya_flg is '���t���O';
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS yasu_flg character varying(1) NOT NULL default '0';
comment on column ryohiseisan_meisai.yasu_flg is '���t���O';
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS raku_flg character varying(1) NOT NULL default '0';
comment on column ryohiseisan_meisai.raku_flg is '�y�t���O';


-- ��������׃e�[�u���ɑ��t���O�A���t���O�A�y�t���O��ǉ�
ALTER TABLE ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS haya_flg character varying(1) NOT NULL default '0';
comment on column ryohi_karibarai_meisai.haya_flg is '���t���O';
ALTER TABLE ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS yasu_flg character varying(1) NOT NULL default '0';
comment on column ryohi_karibarai_meisai.yasu_flg is '���t���O';
ALTER TABLE ryohi_karibarai_meisai ADD COLUMN IF NOT EXISTS raku_flg character varying(1) NOT NULL default '0';
comment on column ryohi_karibarai_meisai.raku_flg is '�y�t���O';

commit;
