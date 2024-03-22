SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 設定情報データ変更 ※hyouji_jun900以降はカスタマイズ領域、1000番以降は拠点入力向け領域
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

--内部コード設定
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
-- Web拠点入力の内部コードは洗い替え対象外
DELETE FROM naibu_cd_setting
WHERE naibu_cd_name NOT IN ('bunri_kbn','shiire_kbn','kazei_kbn_kyoten_furikae','shiwake_pattern_denpyou_kbn_kyoten','shiwake_pattern_setting_kbn_kyoten','fusen_color');
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- 支払依頼申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- 請求書払申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

-- kinou_seigyoへのデータ追加
INSERT INTO kinou_seigyo VALUES ('SD', '1') ON CONFLICT DO NOTHING;

-- gamen_koumoku_seigyoマスターデータに項目追加
-- 可変の設定が入っているのでバックアップを取って洗い替えといった回りくどい方式は取らないでシンプルに追加データのinsertを試みる
INSERT INTO gamen_koumoku_seigyo (denpyou_kbn,koumoku_id,default_koumoku_name,koumoku_name,hyouji_flg,hissu_flg,hyouji_seigyo_flg,pdf_hyouji_flg,pdf_hyouji_seigyo_flg,code_output_flg,code_output_seigyo_flg,hyouji_jun)
VALUES ('A009','shouhyou_shorui_flg','領収書・請求書等','領収書・請求書等','1','1','1','0','0','0','0','2') ON CONFLICT DO NOTHING;

-- 自動引落伝票へのカラム追加
-- カラム指定が必要な処理は指定済み、かつselectはdtoへの値の紐づけをカラム名キーでやっているため、単純追加で問題なし
ALTER TABLE jidouhikiotoshi ADD COLUMN IF NOT EXISTS shouhyou_shorui_flg character varying(1) DEFAULT '0' not null;
comment on column jidouhikiotoshi.shouhyou_shorui_flg is '証憑書類フラグ';

-- shiwake_pattern_settingマスターデータ更新
DELETE FROM SHIWAKE_PATTERN_SETTING;
\copy SHIWAKE_PATTERN_SETTING FROM '.\files\csv\shiwake_pattern_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- shiwake_pattern_var_settingマスターデータ更新
DELETE FROM SHIWAKE_PATTERN_VAR_SETTING;
\copy SHIWAKE_PATTERN_VAR_SETTING FROM '.\files\csv\shiwake_pattern_var_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- 社員口座へのカラム追加
-- カラム指定が必要な処理は指定済み、かつselectはdtoへの値の紐づけをカラム名キーでやっているため、単純追加で問題なし
ALTER TABLE shain_kouza ADD COLUMN IF NOT EXISTS zaimu_edaban_cd character varying(12) DEFAULT '' not null;
comment on column shain_kouza.zaimu_edaban_cd is '財務枝番コード';

-- プロジェクトマスターへのカラム追加
-- カラム指定が必要な処理は指定済み、かつselectはdtoへの値の紐づけをカラム名キーでやっているため、単純追加で問題なし
ALTER TABLE project_master ADD COLUMN IF NOT EXISTS shuuryou_kbn smallint DEFAULT 0 NOT NULL;
comment on column project_master.shuuryou_kbn is '終了区分';

-- e文書検索の項目任意化
ALTER TABLE ebunsho_data ALTER COLUMN ebunsho_nengappi DROP NOT NULL;
ALTER TABLE ebunsho_data ALTER COLUMN ebunsho_kingaku DROP NOT NULL;
ALTER TABLE ebunsho_data ALTER COLUMN ebunsho_hakkousha DROP NOT NULL;

-- e文書検索項目用マスターテーブルの作成
create table if not exists ebunsho_kensaku (
  doc_type smallint not null
  , item_no smallint not null
  , nini_flg smallint DEFAULT 0 not null
  , constraint ebunsho_kensaku_PKEY primary key (doc_type,item_no)
);

comment on table ebunsho_kensaku is 'e文書検索設定';
comment on column ebunsho_kensaku.doc_type is '書類種別';
comment on column ebunsho_kensaku.item_no is '項目';
comment on column ebunsho_kensaku.nini_flg is '任意フラグ';

-- 初期値の挿入
INSERT INTO ebunsho_kensaku VALUES (0,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (0,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (0,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (1,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (1,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (1,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (2,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (2,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (2,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (3,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (3,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (3,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (4,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (4,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (4,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (5,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (5,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (5,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (6,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (6,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (6,6,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (7,4,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (7,5,0) ON CONFLICT DO NOTHING;
INSERT INTO ebunsho_kensaku VALUES (7,6,0) ON CONFLICT DO NOTHING;

--マスター取り込み一覧関係
DELETE FROM master_torikomi_ichiran_de3 WHERE master_id = 'project_master';
DELETE FROM master_torikomi_ichiran_sias WHERE master_id = 'project_master' OR master_id = 'ebunsho_kensaku' OR master_id = 'extension_setting';
DELETE FROM master_torikomi_ichiran_mk2 WHERE master_id = 'project_master' OR master_id = 'ebunsho_kensaku' OR master_id = 'extension_setting';
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_patch_de3.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_patch_sias.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_mk2 FROM '.\files\csv\master_torikomi_ichiran_patch_mk2.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--マスター取り込み詳細関係
--データ追加のみでOK
INSERT INTO master_torikomi_shousai_de3(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('project_master','shuuryou_kbn','終了区分','smallint','ENDKU','完了区分','smallint',3,'0') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_sias(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('project_master','shuuryou_kbn','終了区分','smallint','ENDKU','完了区分','smallint',3,'0') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_mk2(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('project_master','shuuryou_kbn','終了区分','smallint','ENDKU','完了区分','smallint',3,'0') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_sias(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','doc_type','書類種別','smallint','KEYNM2','Ｋｅｙ名２','char',1,'1') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_mk2(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','doc_type','書類種別','smallint','KEYNM2','Ｋｅｙ名２','char',1,'1') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_sias(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','item_no','項目','smallint','KEYNO','Ｋｅｙ名Ｎｏ','smallint',2,'1') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_mk2(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','item_no','項目','smallint','KEYNO','Ｋｅｙ名Ｎｏ','smallint',2,'1') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_sias(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','nini_flg','任意フラグ','smallint','IDATA','数値データ','smallint',3,'0') ON CONFLICT DO NOTHING;
INSERT INTO master_torikomi_shousai_mk2(
	master_id, et_column_id, et_column_name, et_data_type, op_colume_id, op_column_name, op_data_type, entry_order, pk_flg)
	VALUES ('ebunsho_kensaku','nini_flg','任意フラグ','smallint','IDATA','数値データ','smallint',3,'0') ON CONFLICT DO NOTHING;

--e文書検索設定関係のマスター管理一覧及びマスター管理版数のデータ追加
--取り込みの実行はSIAS/mk2のみに絞るが、テーブルと既定値はde3版でも用意しておく（必須固定の実装で区別するのを避けたい）ので、版数や一覧も共通とする
INSERT INTO master_kanri_ichiran VALUES ('ebunsho_kensaku','e文書検索設定','0','default','1999-01-01 00:00:00','default','1999-01-01 00:00:00') ON CONFLICT DO NOTHING;
INSERT INTO master_kanri_hansuu VALUES ('ebunsho_kensaku',1,'0','',0,'','','default','1999-01-01 00:00:00','default','1999-01-01 00:00:00') ON CONFLICT DO NOTHING;

--マスター管理版数の社員口座マスターを更新
UPDATE master_kanri_hansuu SET delete_flg = '1' WHERE master_id = 'shain_kouza';
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
) VALUES (
   'shain_kouza'		--社員口座マスター
 , (SELECT MAX(version) + 1 FROM master_kanri_hansuu WHERE master_id ='shain_kouza')
 , '0'
 , '社員口座_patch.csv'
 , length(convert_to(E'社員番号,振込先金融機関銀行コード,振込先銀行支店コード,振込先預金種別,振込先口座番号,振込先口座名義漢字,振込先口座名義（半角カナ）,振込元金融機関コード,振込元金融機関支店コード,振込元預金種別,振込元口座番号,財務枝番コード\r\nshain_no,saki_kinyuukikan_cd,saki_ginkou_shiten_cd,saki_yokin_shabetsu,saki_kouza_bangou,saki_kouza_meigi_kanji,saki_kouza_meigi_kana,moto_kinyuukikan_cd,moto_kinyuukikan_shiten_cd,moto_yokinshubetsu,moto_kouza_bangou,zaimu_edaban_cd\r\nvarchar(15),varchar(4),varchar(3),varchar(1),varchar(7),varchar(30),varchar(30),varchar(4),varchar(3),varchar(1),varchar(7),varchar(12)\r\n1,2,2,2,2,2,2,2,2,2,2,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shain_no || ',' || saki_kinyuukikan_cd || ',' || saki_ginkou_shiten_cd || ',' || saki_yokin_shabetsu || ',' || saki_kouza_bangou || ',' || saki_kouza_meigi_kanji || ',' || saki_kouza_meigi_kana || ',' || moto_kinyuukikan_cd || ',' || moto_kinyuukikan_shiten_cd || ',' || moto_yokinshubetsu || ',' || moto_kouza_bangou || ',' || '' FROM shain_kouza), E'\r\n') || E'\r\n','sjis') )
 , 'application/vnd.ms-excel'
 , convert_to(E'社員番号,振込先金融機関銀行コード,振込先銀行支店コード,振込先預金種別,振込先口座番号,振込先口座名義漢字,振込先口座名義（半角カナ）,振込元金融機関コード,振込元金融機関支店コード,振込元預金種別,振込元口座番号,財務枝番コード\r\nshain_no,saki_kinyuukikan_cd,saki_ginkou_shiten_cd,saki_yokin_shabetsu,saki_kouza_bangou,saki_kouza_meigi_kanji,saki_kouza_meigi_kana,moto_kinyuukikan_cd,moto_kinyuukikan_shiten_cd,moto_yokinshubetsu,moto_kouza_bangou,zaimu_edaban_cd\r\nvarchar(15),varchar(4),varchar(3),varchar(1),varchar(7),varchar(30),varchar(30),varchar(4),varchar(3),varchar(1),varchar(7),varchar(12)\r\n1,2,2,2,2,2,2,2,2,2,2,2\r\n' || ARRAY_TO_STRING(ARRAY(SELECT shain_no || ',' || saki_kinyuukikan_cd || ',' || saki_ginkou_shiten_cd || ',' || saki_yokin_shabetsu || ',' || saki_kouza_bangou || ',' || saki_kouza_meigi_kanji || ',' || saki_kouza_meigi_kana || ',' || moto_kinyuukikan_cd || ',' || moto_kinyuukikan_shiten_cd || ',' || moto_yokinshubetsu || ',' || moto_kouza_bangou || ',' || '' FROM shain_kouza), E'\r\n') || E'\r\n','sjis')
 , 'patch'
 , current_timestamp
 , 'patch'
 , current_timestamp
);

--houjin_card_importテーブル定義更新
ALTER TABLE houjin_card_import RENAME TO houjin_card_import_old;
create table houjin_card_import (
  user_id character varying(30) not null PRIMARY KEY
  , houjin_card_shubetsu character varying(3) not null
);

comment on table houjin_card_import is '法人カードインポート';
comment on column houjin_card_import.user_id is 'ユーザーID';
comment on column houjin_card_import.houjin_card_shubetsu is '法人カード種別';

INSERT INTO houjin_card_import
SELECT
  user_id
  , houjin_card_shubetsu
FROM houjin_card_import_old;
DROP TABLE houjin_card_import_old;

-- 駅マスターデータ更新
DELETE FROM eki_master;
\copy eki_master FROM '.\files\csv\eki_master.csv' WITH CSV header ENCODING 'SHIFT-JIS';


commit;
