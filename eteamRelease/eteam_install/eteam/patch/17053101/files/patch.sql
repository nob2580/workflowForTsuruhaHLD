SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;


-- ��ʍ��ڐ���̖��̕ύX
UPDATE gamen_koumoku_seigyo 
SET
  default_koumoku_name = '�@�l�J�[�h���p���v'
  , koumoku_name = '�@�l�J�[�h���p���v' 
WHERE
  koumoku_id = 'uchi_houjin_card_riyou_goukei';
  
-- ��ʍ��ڐ���̏C��
CREATE TABLE gamen_koumoku_seigyo_tmp AS SELECT * FROM gamen_koumoku_seigyo;
DELETE FROM gamen_koumoku_seigyo WHERE denpyou_kbn LIKE 'A%';
\copy gamen_koumoku_seigyo FROM '.\files\csv\gamen_koumoku_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';
UPDATE gamen_koumoku_seigyo new SET
  koumoku_name = (SELECT koumoku_name FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
  ,hyouji_flg  = (SELECT hyouji_flg   FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
  ,hissu_flg   = (SELECT hissu_flg    FROM gamen_koumoku_seigyo_tmp tmp WHERE (tmp.denpyou_kbn,tmp.koumoku_id) = (new.denpyou_kbn,new.koumoku_id))
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp);
DROP TABLE gamen_koumoku_seigyo_tmp;

-- �ݒ���f�[�^�ύX
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

--��ʔ�Z�ɍ����x�����z��ǉ�
ALTER TABLE KOUTSUUHISEISAN RENAME TO KOUTSUUHISEISAN_OLD;
CREATE TABLE KOUTSUUHISEISAN
(
	DENPYOU_ID VARCHAR(19) NOT NULL,
	MOKUTEKI VARCHAR(240) NOT NULL,
	SEISANKIKAN_FROM DATE,
	SEISANKIKAN_FROM_HOUR VARCHAR(2),
	SEISANKIKAN_FROM_MIN VARCHAR(2),
	SEISANKIKAN_TO DATE,
	SEISANKIKAN_TO_HOUR VARCHAR(2),
	SEISANKIKAN_TO_MIN VARCHAR(2),
	KEIJOUBI DATE,
	SHIHARAIBI DATE,
	SHIHARAIKIBOUBI DATE,
	SHIHARAIHOUHOU VARCHAR(1) NOT NULL,
	TEKIYOU VARCHAR(60) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	GOUKEI_KINGAKU DECIMAL(15) NOT NULL,
	HOUJIN_CARD_RIYOU_KINGAKU DECIMAL(15) NOT NULL,
	SASHIHIKI_SHIKYUU_KINGAKU DECIMAL(15) NOT NULL,
	HF1_CD VARCHAR(20) NOT NULL,
	HF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF2_CD VARCHAR(20) NOT NULL,
	HF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HF3_CD VARCHAR(20) NOT NULL,
	HF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	HOSOKU VARCHAR(240) NOT NULL,
	SHIWAKE_EDANO INT,
	TORIHIKI_NAME VARCHAR(20) NOT NULL,
	KARI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KARI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KARI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KARI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KARI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KARI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KARI_KAZEI_KBN VARCHAR(3) NOT NULL,
	KASHI_FUTAN_BUMON_CD VARCHAR(8) NOT NULL,
	KASHI_FUTAN_BUMON_NAME VARCHAR(20) NOT NULL,
	KASHI_KAMOKU_CD VARCHAR(6) NOT NULL,
	KASHI_KAMOKU_NAME VARCHAR(22) NOT NULL,
	KASHI_KAMOKU_EDABAN_CD VARCHAR(12) NOT NULL,
	KASHI_KAMOKU_EDABAN_NAME VARCHAR(20) NOT NULL,
	KASHI_KAZEI_KBN VARCHAR(3) NOT NULL,
	UF1_CD VARCHAR(20) NOT NULL,
	UF1_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF2_CD VARCHAR(20) NOT NULL,
	UF2_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	UF3_CD VARCHAR(20) NOT NULL,
	UF3_NAME_RYAKUSHIKI VARCHAR(20) NOT NULL,
	PROJECT_CD VARCHAR(12) NOT NULL,
	PROJECT_NAME VARCHAR(20) NOT NULL,
	TEKIYOU_CD VARCHAR(4) NOT NULL,
	TOUROKU_USER_ID VARCHAR(30) NOT NULL,
	TOUROKU_TIME TIMESTAMP NOT NULL,
	KOUSHIN_USER_ID VARCHAR(30) NOT NULL,
	KOUSHIN_TIME TIMESTAMP NOT NULL,
	PRIMARY KEY (DENPYOU_ID)
) WITHOUT OIDS;

COMMENT ON TABLE KOUTSUUHISEISAN IS '��ʔ�Z';
COMMENT ON COLUMN KOUTSUUHISEISAN.DENPYOU_ID IS '�`�[ID';
COMMENT ON COLUMN KOUTSUUHISEISAN.MOKUTEKI IS '�ړI';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_FROM IS '���Z���ԊJ�n��';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_FROM_HOUR IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_FROM_MIN IS '���Z���ԊJ�n�����i���j';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_TO IS '���Z���ԏI����';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_TO_HOUR IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN KOUTSUUHISEISAN.SEISANKIKAN_TO_MIN IS '���Z���ԏI�������i���j';
COMMENT ON COLUMN KOUTSUUHISEISAN.KEIJOUBI IS '�v���';
COMMENT ON COLUMN KOUTSUUHISEISAN.SHIHARAIBI IS '�x����';
COMMENT ON COLUMN KOUTSUUHISEISAN.SHIHARAIKIBOUBI IS '�x����]��';
COMMENT ON COLUMN KOUTSUUHISEISAN.SHIHARAIHOUHOU IS '�x�����@';
COMMENT ON COLUMN KOUTSUUHISEISAN.TEKIYOU IS '�E�v';
COMMENT ON COLUMN KOUTSUUHISEISAN.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.GOUKEI_KINGAKU IS '���v���z';
COMMENT ON COLUMN KOUTSUUHISEISAN.HOUJIN_CARD_RIYOU_KINGAKU IS '���@�l�J�[�h���p���v';
COMMENT ON COLUMN KOUTSUUHISEISAN.SASHIHIKI_SHIKYUU_KINGAKU IS '�����x�����z';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF1_CD IS 'HF1�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF1_NAME_RYAKUSHIKI IS 'HF1���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF2_CD IS 'HF2�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF2_NAME_RYAKUSHIKI IS 'HF2���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF3_CD IS 'HF3�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.HF3_NAME_RYAKUSHIKI IS 'HF3���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.HOSOKU IS '�⑫';
COMMENT ON COLUMN KOUTSUUHISEISAN.SHIWAKE_EDANO IS '�d��}�ԍ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.TORIHIKI_NAME IS '�����';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_FUTAN_BUMON_CD IS '�ؕ����S����R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_FUTAN_BUMON_NAME IS '�ؕ����S���喼';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_KAMOKU_CD IS '�ؕ��ȖڃR�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_KAMOKU_NAME IS '�ؕ��Ȗږ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_KAMOKU_EDABAN_CD IS '�ؕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_KAMOKU_EDABAN_NAME IS '�ؕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.KARI_KAZEI_KBN IS '�ؕ��ېŋ敪';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_FUTAN_BUMON_CD IS '�ݕ����S����R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_FUTAN_BUMON_NAME IS '�ݕ����S���喼';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_KAMOKU_CD IS '�ݕ��ȖڃR�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_KAMOKU_NAME IS '�ݕ��Ȗږ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_KAMOKU_EDABAN_CD IS '�ݕ��Ȗڎ}�ԃR�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_KAMOKU_EDABAN_NAME IS '�ݕ��Ȗڎ}�Ԗ�';
COMMENT ON COLUMN KOUTSUUHISEISAN.KASHI_KAZEI_KBN IS '�ݕ��ېŋ敪';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF1_CD IS 'UF1�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF1_NAME_RYAKUSHIKI IS 'UF1���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF2_CD IS 'UF2�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF2_NAME_RYAKUSHIKI IS 'UF2���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF3_CD IS 'UF3�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.UF3_NAME_RYAKUSHIKI IS 'UF3���i�����j';
COMMENT ON COLUMN KOUTSUUHISEISAN.PROJECT_CD IS '�v���W�F�N�g�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.PROJECT_NAME IS '�v���W�F�N�g��';
COMMENT ON COLUMN KOUTSUUHISEISAN.TEKIYOU_CD IS '�E�v�R�[�h';
COMMENT ON COLUMN KOUTSUUHISEISAN.TOUROKU_USER_ID IS '�o�^���[�U�[ID';
COMMENT ON COLUMN KOUTSUUHISEISAN.TOUROKU_TIME IS '�o�^����';
COMMENT ON COLUMN KOUTSUUHISEISAN.KOUSHIN_USER_ID IS '�X�V���[�U�[ID';
COMMENT ON COLUMN KOUTSUUHISEISAN.KOUSHIN_TIME IS '�X�V����';

INSERT
INTO KOUTSUUHISEISAN(
	DENPYOU_ID,
	MOKUTEKI,
	SEISANKIKAN_FROM,
	SEISANKIKAN_FROM_HOUR,
	SEISANKIKAN_FROM_MIN,
	SEISANKIKAN_TO,
	SEISANKIKAN_TO_HOUR,
	SEISANKIKAN_TO_MIN,
	KEIJOUBI,
	SHIHARAIBI,
	SHIHARAIKIBOUBI,
	SHIHARAIHOUHOU,
	TEKIYOU,
	ZEIRITSU,
	GOUKEI_KINGAKU,
	HOUJIN_CARD_RIYOU_KINGAKU,
	SASHIHIKI_SHIKYUU_KINGAKU,
	HF1_CD,
	HF1_NAME_RYAKUSHIKI,
	HF2_CD,
	HF2_NAME_RYAKUSHIKI,
	HF3_CD,
	HF3_NAME_RYAKUSHIKI,
	HOSOKU,
	SHIWAKE_EDANO,
	TORIHIKI_NAME,
	KARI_FUTAN_BUMON_CD,
	KARI_FUTAN_BUMON_NAME,
	KARI_KAMOKU_CD,
	KARI_KAMOKU_NAME,
	KARI_KAMOKU_EDABAN_CD,
	KARI_KAMOKU_EDABAN_NAME,
	KARI_KAZEI_KBN,
	KASHI_FUTAN_BUMON_CD,
	KASHI_FUTAN_BUMON_NAME,
	KASHI_KAMOKU_CD,
	KASHI_KAMOKU_NAME,
	KASHI_KAMOKU_EDABAN_CD,
	KASHI_KAMOKU_EDABAN_NAME,
	KASHI_KAZEI_KBN,
	UF1_CD,
	UF1_NAME_RYAKUSHIKI,
	UF2_CD,
	UF2_NAME_RYAKUSHIKI,
	UF3_CD,
	UF3_NAME_RYAKUSHIKI,
	PROJECT_CD,
	PROJECT_NAME,
	TEKIYOU_CD,
	TOUROKU_USER_ID,
	TOUROKU_TIME,
	KOUSHIN_USER_ID,
	KOUSHIN_TIME
)
SELECT
DENPYOU_ID,
	MOKUTEKI,
	SEISANKIKAN_FROM,
	SEISANKIKAN_FROM_HOUR,
	SEISANKIKAN_FROM_MIN,
	SEISANKIKAN_TO,
	SEISANKIKAN_TO_HOUR,
	SEISANKIKAN_TO_MIN,
	KEIJOUBI,
	SHIHARAIBI,
	SHIHARAIKIBOUBI,
	SHIHARAIHOUHOU,
	TEKIYOU,
	ZEIRITSU,
	GOUKEI_KINGAKU,
	HOUJIN_CARD_RIYOU_KINGAKU,
	GOUKEI_KINGAKU-HOUJIN_CARD_RIYOU_KINGAKU,
	HF1_CD,
	HF1_NAME_RYAKUSHIKI,
	HF2_CD,
	HF2_NAME_RYAKUSHIKI,
	HF3_CD,
	HF3_NAME_RYAKUSHIKI,
	HOSOKU,
	SHIWAKE_EDANO,
	TORIHIKI_NAME,
	KARI_FUTAN_BUMON_CD,
	KARI_FUTAN_BUMON_NAME,
	KARI_KAMOKU_CD,
	KARI_KAMOKU_NAME,
	KARI_KAMOKU_EDABAN_CD,
	KARI_KAMOKU_EDABAN_NAME,
	KARI_KAZEI_KBN,
	KASHI_FUTAN_BUMON_CD,
	KASHI_FUTAN_BUMON_NAME,
	KASHI_KAMOKU_CD,
	KASHI_KAMOKU_NAME,
	KASHI_KAMOKU_EDABAN_CD,
	KASHI_KAMOKU_EDABAN_NAME,
	KASHI_KAZEI_KBN,
	UF1_CD,
	UF1_NAME_RYAKUSHIKI,
	UF2_CD,
	UF2_NAME_RYAKUSHIKI,
	UF3_CD,
	UF3_NAME_RYAKUSHIKI,
	PROJECT_CD,
	PROJECT_NAME,
	TEKIYOU_CD,
	TOUROKU_USER_ID,
	TOUROKU_TIME,
	KOUSHIN_USER_ID,
	KOUSHIN_TIME
FROM
	KOUTSUUHISEISAN_OLD;

DROP TABLE KOUTSUUHISEISAN_OLD;


-- �}�X�^�[�捞�pSQL�̍X�V(���� �����⏕�R�[�h�̑Ή�)
DELETE FROM MASTER_TORIKOMI_ICHIRAN_DE3;
\copy MASTER_TORIKOMI_ICHIRAN_DE3  FROM '.\files\csv\master_torikomi_ichiran_de3.csv'  WITH CSV header ENCODING 'SHIFT-JIS';
DELETE FROM MASTER_TORIKOMI_ICHIRAN_SIAS;
\copy MASTER_TORIKOMI_ICHIRAN_SIAS FROM '.\files\csv\master_torikomi_ichiran_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- ����ŗ��ɕ��я���ǉ�
ALTER TABLE SHOUHIZEIRITSU RENAME TO SHOUHIZEIRITSU_OLD;
CREATE TABLE SHOUHIZEIRITSU
(
	SORT_JUN CHAR(3) NOT NULL,
	ZEIRITSU DECIMAL(3) NOT NULL,
	HASUU_KEISAN_KBN VARCHAR(1),
	YUUKOU_KIGEN_FROM DATE,
	YUUKOU_KIGEN_TO DATE,
	PRIMARY KEY (SORT_JUN, ZEIRITSU)
) WITHOUT OIDS;
COMMENT ON TABLE SHOUHIZEIRITSU IS '����ŗ�';
COMMENT ON COLUMN SHOUHIZEIRITSU.SORT_JUN IS '���я�';
COMMENT ON COLUMN SHOUHIZEIRITSU.ZEIRITSU IS '�ŗ�';
COMMENT ON COLUMN SHOUHIZEIRITSU.HASUU_KEISAN_KBN IS '�[���v�Z�敪';
COMMENT ON COLUMN SHOUHIZEIRITSU.YUUKOU_KIGEN_FROM IS '�L�������J�n��';
COMMENT ON COLUMN SHOUHIZEIRITSU.YUUKOU_KIGEN_TO IS '�L�������I����';

INSERT
INTO SHOUHIZEIRITSU(
	SORT_JUN,
	ZEIRITSU,
	HASUU_KEISAN_KBN,
	YUUKOU_KIGEN_FROM,
	YUUKOU_KIGEN_TO
)
SELECT 
	LPAD( ROW_NUMBER() OVER()::TEXT, 3, '0'::TEXT),
	ZEIRITSU,
	HASUU_KEISAN_KBN,
	YUUKOU_KIGEN_FROM,
	YUUKOU_KIGEN_TO
FROM
	SHOUHIZEIRITSU_OLD
ORDER BY ZEIRITSU;

DROP TABLE SHOUHIZEIRITSU_OLD;

-- ����ŗ��̃}�X�^�[�Ǘ��Ő��̒l��ύX
-- �S�ĐV������蒼����INSERT
UPDATE master_kanri_hansuu SET delete_flg = '1' WHERE master_id = 'shouhizeiritsu';
INSERT INTO master_kanri_hansuu 
(
  master_id
  , version
  , delete_flg
  , file_name
  , file_size
  , content_type
  , binary_data
  , touroku_user_id 
  , touroku_time 
  , koushin_user_id 
  , koushin_time 
)
SELECT 
   'shouhizeiritsu' as master_id
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='shouhizeiritsu') as version
 , '0' as delete_flg
 , '����ŗ�_patch.csv' as file_name
 , length(convert_to(E'���я�,�ŗ�,�[���v�Z�敪,�L�������J�n��,�L�������I����\r\nsort_jun,zeiritsu,hasuu_keisan_kbn,yuukou_kigen_from,yuukou_kigen_to\r\nvarchar(3),decimal(3),varchar(1),date,date\r\n1,1,,,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || zeiritsu || ',' || hasuu_keisan_kbn || ',' || replace(yuukou_kigen_from::text,'-'::text,'/'::text) || ',' || replace(yuukou_kigen_to::text,'-'::text,'/'::text) FROM shouhizeiritsu), E'\r\n') || E'\r\n','sjis') ) as file_size
 , 'application/vnd.ms-excel' as content_type
 , convert_to(E'���я�,�ŗ�,�[���v�Z�敪,�L�������J�n��,�L�������I����\r\nsort_jun,zeiritsu,hasuu_keisan_kbn,yuukou_kigen_from,yuukou_kigen_to\r\nvarchar(3),decimal(3),varchar(1),date,date\r\n1,1,,,\r\n' || ARRAY_TO_STRING(ARRAY(SELECT sort_jun || ',' || zeiritsu || ',' || hasuu_keisan_kbn || ',' || replace(yuukou_kigen_from::text,'-'::text,'/'::text) || ',' || replace(yuukou_kigen_to::text,'-'::text,'/'::text) FROM shouhizeiritsu), E'\r\n') || E'\r\n','sjis') as binary_data
 , 'patch' as koushin_time
 , current_timestamp as touroku_user_id
 , 'patch' as touroku_time
 , current_timestamp as koushin_user_id
 ;

commit;