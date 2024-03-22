SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- ICSP連絡票_0804_駅すぱあと連携時に距離を反映
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

-- 画面項目制御
CREATE TABLE gamen_koumoku_seigyo_tmp AS SELECT * FROM gamen_koumoku_seigyo;
DELETE FROM gamen_koumoku_seigyo WHERE denpyou_kbn LIKE 'A%'OR denpyou_kbn LIKE 'Z%';
\copy gamen_koumoku_seigyo FROM '.\files\csv\gamen_koumoku_seigyo.csv' WITH CSV header ENCODING 'SHIFT-JIS';
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
-- ▼▼K-133 ここから
-- アップデート前に項目値がデフォルトから未変更の項目は、項目名をデフォルト項目名と同じにする
UPDATE gamen_koumoku_seigyo new SET
  koumoku_name = default_koumoku_name
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE default_koumoku_name != '' AND default_koumoku_name = koumoku_name AND denpyou_kbn = 'Z001');
-- ▲▲ここまで
DROP TABLE gamen_koumoku_seigyo_tmp;
-- 支払依頼申請セグメントの帳票出力フラグは0にしておく(旧バージョンで1になっている可能性があるため)
UPDATE gamen_koumoku_seigyo SET pdf_hyouji_flg = '0', pdf_hyouji_seigyo_flg = '0', code_output_flg = '0', code_output_seigyo_flg = '0' WHERE denpyou_kbn = 'A013' AND koumoku_id = 'segment_name_ryakushiki';

-- ICSP連絡票_0582_届出ジェネレータ選択項目の変更時、帳票へ自動反映してほしい
ALTER TABLE kani_todoke_list_ko DROP CONSTRAINT IF EXISTS kani_todoke_list_ko_PKEY;
ALTER TABLE kani_todoke_list_ko RENAME TO kani_todoke_list_ko_old;
create table kani_todoke_list_ko (
  denpyou_kbn character varying(4) not null
  , version integer not null
  , area_kbn character varying not null
  , item_name character varying not null
  , hyouji_jun integer not null
  , text character varying not null
  , value character varying not null
  , select_item character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_list_ko_PKEY primary key (denpyou_kbn,version,area_kbn,item_name,hyouji_jun)
);
comment on table kani_todoke_list_ko is '届出ジェネレータ項目リスト子';
comment on column kani_todoke_list_ko.denpyou_kbn is '伝票区分';
comment on column kani_todoke_list_ko.version is 'バージョン';
comment on column kani_todoke_list_ko.area_kbn is 'エリア区分';
comment on column kani_todoke_list_ko.item_name is '項目名';
comment on column kani_todoke_list_ko.hyouji_jun is '表示順';
comment on column kani_todoke_list_ko.text is 'テキスト';
comment on column kani_todoke_list_ko.value is '値';
comment on column kani_todoke_list_ko.select_item is '選択項目';
comment on column kani_todoke_list_ko.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke_list_ko.touroku_time is '登録日時';
comment on column kani_todoke_list_ko.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke_list_ko.koushin_time is '更新日時';
INSERT INTO kani_todoke_list_ko
SELECT
  denpyou_kbn
  , version
  , area_kbn
  , item_name
  , hyouji_jun
  , text
  , value
  , '' -- select_item
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM kani_todoke_list_ko_old;
DROP TABLE kani_todoke_list_ko_old;

WITH
  --とりあえず伝票区分毎のMAX VERSIONをとる。MAXのみが更新対象候補
  kb AS(
    SELECT
      denpyou_kbn,
      max(version) AS version 
    FROM kani_todoke_version 
    GROUP BY denpyou_kbn),

  --さらに、それに紐付くselect_itemとそのselect_itemがとり得るCD/CALUEの配列を取得※CD=BLANKは未選択に対応したものなので除外
  kani_item AS (
    SELECT 
      denpyou_kbn, 
      version, 
      area_kbn, 
      item_name,
      (SELECT ARRAY_AGG((value, text) ORDER BY value) AS VALUES FROM kani_todoke_list_ko WHERE (denpyou_kbn,version,area_kbn,item_name) = (kb.denpyou_kbn,kb.version,oya.area_kbn,oya.item_name) AND value <> '') AS values
    FROM kb 
    JOIN kani_todoke_list_oya oya 
    USING(denpyou_kbn, version)),

  --一方でマスタテーブルから、select_itemとCD/VALUEの配列を取得
  master_item AS(
    SELECT
      select_item,
      (SELECT ARRAY_AGG((cd, name) ORDER BY cd) AS VALUES FROM kani_todoke_select_item WHERE select_item = s.select_item)
    FROM kani_todoke_select_item s
    GROUP BY select_item),

  --CD/VALUEでJOINすることで、届出のメタ情報とマスターの項目名が紐付く。※配列比較便利すぎ・・・
  kani_master_join AS(
    SELECT
      kani_item.*,
      (SELECT select_item FROM master_item WHERE kani_item.values = master_item.values ORDER BY item_name LIMIT 1) AS select_item
    FROM kani_item)

--届出のメタ情報とマスターの項目名の紐付が取れたものについてはselect_itemを更新
UPDATE kani_todoke_list_ko ko SET 
  select_item = COALESCE(
    (SELECT select_item 
    FROM kani_master_join j 
    WHERE (ko.denpyou_kbn, ko.version, ko.area_kbn, ko.item_name) = (j.denpyou_kbn, j.version, j.area_kbn, j.item_name)), '');


-- ICSP連絡票_0905_交際費精算伝票の追加
ALTER TABLE keihiseisan_meisai DROP CONSTRAINT IF EXISTS keihiseisan_meisai_PKEY;
ALTER TABLE keihiseisan_meisai RENAME TO keihiseisan_meisai_old;
create table keihiseisan_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , user_id character varying(30) not null
  , shain_no character varying(15) not null
  , user_sei character varying(10) not null
  , user_mei character varying(10) not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , houjin_card_riyou_flg character varying(1) not null
  , kaisha_tehai_flg character varying(1) not null
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , himoduke_card_meisai bigint
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint keihiseisan_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table keihiseisan_meisai is '経費精算明細';
comment on column keihiseisan_meisai.denpyou_id is '伝票ID';
comment on column keihiseisan_meisai.denpyou_edano is '伝票枝番号';
comment on column keihiseisan_meisai.shiwake_edano is '仕訳枝番号';
comment on column keihiseisan_meisai.user_id is 'ユーザーID';
comment on column keihiseisan_meisai.shain_no is '社員番号';
comment on column keihiseisan_meisai.user_sei is 'ユーザー姓';
comment on column keihiseisan_meisai.user_mei is 'ユーザー名';
comment on column keihiseisan_meisai.shiyoubi is '使用日';
comment on column keihiseisan_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column keihiseisan_meisai.torihiki_name is '取引名';
comment on column keihiseisan_meisai.tekiyou is '摘要';
comment on column keihiseisan_meisai.zeiritsu is '税率';
comment on column keihiseisan_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column keihiseisan_meisai.shiharai_kingaku is '支払金額';
comment on column keihiseisan_meisai.hontai_kingaku is '本体金額';
comment on column keihiseisan_meisai.shouhizeigaku is '消費税額';
comment on column keihiseisan_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column keihiseisan_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column keihiseisan_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column keihiseisan_meisai.kousaihi_shousai is '交際費詳細';
comment on column keihiseisan_meisai.kousaihi_ninzuu is '交際費人数';
comment on column keihiseisan_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column keihiseisan_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column keihiseisan_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column keihiseisan_meisai.torihikisaki_cd is '取引先コード';
comment on column keihiseisan_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column keihiseisan_meisai.kari_kamoku_cd is '借方科目コード';
comment on column keihiseisan_meisai.kari_kamoku_name is '借方科目名';
comment on column keihiseisan_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column keihiseisan_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column keihiseisan_meisai.kari_kazei_kbn is '借方課税区分';
comment on column keihiseisan_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column keihiseisan_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column keihiseisan_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column keihiseisan_meisai.kashi_kamoku_name is '貸方科目名';
comment on column keihiseisan_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column keihiseisan_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column keihiseisan_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column keihiseisan_meisai.uf1_cd is 'UF1コード';
comment on column keihiseisan_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column keihiseisan_meisai.uf2_cd is 'UF2コード';
comment on column keihiseisan_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column keihiseisan_meisai.uf3_cd is 'UF3コード';
comment on column keihiseisan_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column keihiseisan_meisai.uf4_cd is 'UF4コード';
comment on column keihiseisan_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column keihiseisan_meisai.uf5_cd is 'UF5コード';
comment on column keihiseisan_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column keihiseisan_meisai.uf6_cd is 'UF6コード';
comment on column keihiseisan_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column keihiseisan_meisai.uf7_cd is 'UF7コード';
comment on column keihiseisan_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column keihiseisan_meisai.uf8_cd is 'UF8コード';
comment on column keihiseisan_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column keihiseisan_meisai.uf9_cd is 'UF9コード';
comment on column keihiseisan_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column keihiseisan_meisai.uf10_cd is 'UF10コード';
comment on column keihiseisan_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column keihiseisan_meisai.project_cd is 'プロジェクトコード';
comment on column keihiseisan_meisai.project_name is 'プロジェクト名';
comment on column keihiseisan_meisai.segment_cd is 'セグメントコード';
comment on column keihiseisan_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column keihiseisan_meisai.tekiyou_cd is '摘要コード';
comment on column keihiseisan_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column keihiseisan_meisai.touroku_user_id is '登録ユーザーID';
comment on column keihiseisan_meisai.touroku_time is '登録日時';
comment on column keihiseisan_meisai.koushin_user_id is '更新ユーザーID';
comment on column keihiseisan_meisai.koushin_time is '更新日時';
INSERT INTO keihiseisan_meisai
SELECT
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , user_id
  , shain_no
  , user_sei
  , user_mei
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , keigen_zeiritsu_kbn
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , null -- kousaihi_ninzuu
  , null -- kousaihi_hitori_kingaku
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM keihiseisan_meisai_old;
DROP TABLE keihiseisan_meisai_old;

ALTER TABLE seikyuushobarai_meisai DROP CONSTRAINT IF EXISTS seikyuushobarai_meisai_PKEY;
ALTER TABLE seikyuushobarai_meisai RENAME TO seikyuushobarai_meisai_old;
create table seikyuushobarai_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , furikomisaki_jouhou character varying(240) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint seikyuushobarai_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table seikyuushobarai_meisai is '請求書払い明細';
comment on column seikyuushobarai_meisai.denpyou_id is '伝票ID';
comment on column seikyuushobarai_meisai.denpyou_edano is '伝票枝番号';
comment on column seikyuushobarai_meisai.shiwake_edano is '仕訳枝番号';
comment on column seikyuushobarai_meisai.torihiki_name is '取引名';
comment on column seikyuushobarai_meisai.tekiyou is '摘要';
comment on column seikyuushobarai_meisai.zeiritsu is '税率';
comment on column seikyuushobarai_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column seikyuushobarai_meisai.shiharai_kingaku is '支払金額';
comment on column seikyuushobarai_meisai.hontai_kingaku is '本体金額';
comment on column seikyuushobarai_meisai.shouhizeigaku is '消費税額';
comment on column seikyuushobarai_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column seikyuushobarai_meisai.kousaihi_shousai is '交際費詳細';
comment on column seikyuushobarai_meisai.kousaihi_ninzuu is '交際費人数';
comment on column seikyuushobarai_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column seikyuushobarai_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column seikyuushobarai_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column seikyuushobarai_meisai.torihikisaki_cd is '取引先コード';
comment on column seikyuushobarai_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column seikyuushobarai_meisai.furikomisaki_jouhou is '振込先情報';
comment on column seikyuushobarai_meisai.kari_kamoku_cd is '借方科目コード';
comment on column seikyuushobarai_meisai.kari_kamoku_name is '借方科目名';
comment on column seikyuushobarai_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column seikyuushobarai_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column seikyuushobarai_meisai.kari_kazei_kbn is '借方課税区分';
comment on column seikyuushobarai_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column seikyuushobarai_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column seikyuushobarai_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column seikyuushobarai_meisai.kashi_kamoku_name is '貸方科目名';
comment on column seikyuushobarai_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column seikyuushobarai_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column seikyuushobarai_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column seikyuushobarai_meisai.uf1_cd is 'UF1コード';
comment on column seikyuushobarai_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column seikyuushobarai_meisai.uf2_cd is 'UF2コード';
comment on column seikyuushobarai_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column seikyuushobarai_meisai.uf3_cd is 'UF3コード';
comment on column seikyuushobarai_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column seikyuushobarai_meisai.uf4_cd is 'UF4コード';
comment on column seikyuushobarai_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column seikyuushobarai_meisai.uf5_cd is 'UF5コード';
comment on column seikyuushobarai_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column seikyuushobarai_meisai.uf6_cd is 'UF6コード';
comment on column seikyuushobarai_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column seikyuushobarai_meisai.uf7_cd is 'UF7コード';
comment on column seikyuushobarai_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column seikyuushobarai_meisai.uf8_cd is 'UF8コード';
comment on column seikyuushobarai_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column seikyuushobarai_meisai.uf9_cd is 'UF9コード';
comment on column seikyuushobarai_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column seikyuushobarai_meisai.uf10_cd is 'UF10コード';
comment on column seikyuushobarai_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column seikyuushobarai_meisai.project_cd is 'プロジェクトコード';
comment on column seikyuushobarai_meisai.project_name is 'プロジェクト名';
comment on column seikyuushobarai_meisai.segment_cd is 'セグメントコード';
comment on column seikyuushobarai_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column seikyuushobarai_meisai.tekiyou_cd is '摘要コード';
comment on column seikyuushobarai_meisai.touroku_user_id is '登録ユーザーID';
comment on column seikyuushobarai_meisai.touroku_time is '登録日時';
comment on column seikyuushobarai_meisai.koushin_user_id is '更新ユーザーID';
comment on column seikyuushobarai_meisai.koushin_time is '更新日時';
INSERT INTO seikyuushobarai_meisai
SELECT
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , torihiki_name
  , tekiyou
  , zeiritsu
  , keigen_zeiritsu_kbn
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , null -- kousaihi_ninzuu
  , null -- kousaihi_hitori_kingaku
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , furikomisaki_jouhou
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM seikyuushobarai_meisai_old;
DROP TABLE seikyuushobarai_meisai_old;


ALTER TABLE ryohi_karibarai_keihi_meisai DROP CONSTRAINT IF EXISTS ryohi_karibarai_keihi_meisai_PKEY;
ALTER TABLE ryohi_karibarai_keihi_meisai RENAME TO ryohi_karibarai_keihi_meisai_old;
create table ryohi_karibarai_keihi_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint ryohi_karibarai_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table ryohi_karibarai_keihi_meisai is '旅費仮払経費明細';
comment on column ryohi_karibarai_keihi_meisai.denpyou_id is '伝票ID';
comment on column ryohi_karibarai_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column ryohi_karibarai_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column ryohi_karibarai_keihi_meisai.shiyoubi is '使用日';
comment on column ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column ryohi_karibarai_keihi_meisai.torihiki_name is '取引名';
comment on column ryohi_karibarai_keihi_meisai.tekiyou is '摘要';
comment on column ryohi_karibarai_keihi_meisai.zeiritsu is '税率';
comment on column ryohi_karibarai_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column ryohi_karibarai_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column ryohi_karibarai_keihi_meisai.hontai_kingaku is '本体金額';
comment on column ryohi_karibarai_keihi_meisai.shouhizeigaku is '消費税額';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_ninzuu is '交際費人数';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column ryohi_karibarai_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column ryohi_karibarai_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column ryohi_karibarai_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column ryohi_karibarai_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column ryohi_karibarai_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column ryohi_karibarai_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column ryohi_karibarai_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column ryohi_karibarai_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column ryohi_karibarai_keihi_meisai.uf1_cd is 'UF1コード';
comment on column ryohi_karibarai_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf2_cd is 'UF2コード';
comment on column ryohi_karibarai_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf3_cd is 'UF3コード';
comment on column ryohi_karibarai_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf4_cd is 'UF4コード';
comment on column ryohi_karibarai_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf5_cd is 'UF5コード';
comment on column ryohi_karibarai_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf6_cd is 'UF6コード';
comment on column ryohi_karibarai_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf7_cd is 'UF7コード';
comment on column ryohi_karibarai_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf8_cd is 'UF8コード';
comment on column ryohi_karibarai_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf9_cd is 'UF9コード';
comment on column ryohi_karibarai_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf10_cd is 'UF10コード';
comment on column ryohi_karibarai_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column ryohi_karibarai_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column ryohi_karibarai_keihi_meisai.project_name is 'プロジェクト名';
comment on column ryohi_karibarai_keihi_meisai.segment_cd is 'セグメントコード';
comment on column ryohi_karibarai_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column ryohi_karibarai_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column ryohi_karibarai_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column ryohi_karibarai_keihi_meisai.touroku_time is '登録日時';
comment on column ryohi_karibarai_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column ryohi_karibarai_keihi_meisai.koushin_time is '更新日時';
INSERT INTO ryohi_karibarai_keihi_meisai
SELECT
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , keigen_zeiritsu_kbn
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , null -- kousaihi_ninzuu
  , null -- kousaihi_hitori_kingaku
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM ryohi_karibarai_keihi_meisai_old;
DROP TABLE ryohi_karibarai_keihi_meisai_old;

ALTER TABLE ryohiseisan_keihi_meisai DROP CONSTRAINT IF EXISTS ryohiseisan_keihi_meisai_PKEY;
ALTER TABLE ryohiseisan_keihi_meisai RENAME TO ryohiseisan_keihi_meisai_old;
create table ryohiseisan_keihi_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , houjin_card_riyou_flg character varying(1) not null
  , kaisha_tehai_flg character varying(1) not null
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , himoduke_card_meisai bigint
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint ryohiseisan_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);
comment on table ryohiseisan_keihi_meisai is '旅費精算経費明細';
comment on column ryohiseisan_keihi_meisai.denpyou_id is '伝票ID';
comment on column ryohiseisan_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column ryohiseisan_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column ryohiseisan_keihi_meisai.shiyoubi is '使用日';
comment on column ryohiseisan_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column ryohiseisan_keihi_meisai.torihiki_name is '取引名';
comment on column ryohiseisan_keihi_meisai.tekiyou is '摘要';
comment on column ryohiseisan_keihi_meisai.zeiritsu is '税率';
comment on column ryohiseisan_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column ryohiseisan_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column ryohiseisan_keihi_meisai.hontai_kingaku is '本体金額';
comment on column ryohiseisan_keihi_meisai.shouhizeigaku is '消費税額';
comment on column ryohiseisan_keihi_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column ryohiseisan_keihi_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column ryohiseisan_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column ryohiseisan_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column ryohiseisan_keihi_meisai.kousaihi_ninzuu is '交際費人数';
comment on column ryohiseisan_keihi_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column ryohiseisan_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column ryohiseisan_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column ryohiseisan_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column ryohiseisan_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column ryohiseisan_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column ryohiseisan_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column ryohiseisan_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column ryohiseisan_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column ryohiseisan_keihi_meisai.uf1_cd is 'UF1コード';
comment on column ryohiseisan_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column ryohiseisan_keihi_meisai.uf2_cd is 'UF2コード';
comment on column ryohiseisan_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column ryohiseisan_keihi_meisai.uf3_cd is 'UF3コード';
comment on column ryohiseisan_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column ryohiseisan_keihi_meisai.uf4_cd is 'UF4コード';
comment on column ryohiseisan_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column ryohiseisan_keihi_meisai.uf5_cd is 'UF5コード';
comment on column ryohiseisan_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column ryohiseisan_keihi_meisai.uf6_cd is 'UF6コード';
comment on column ryohiseisan_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column ryohiseisan_keihi_meisai.uf7_cd is 'UF7コード';
comment on column ryohiseisan_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column ryohiseisan_keihi_meisai.uf8_cd is 'UF8コード';
comment on column ryohiseisan_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column ryohiseisan_keihi_meisai.uf9_cd is 'UF9コード';
comment on column ryohiseisan_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column ryohiseisan_keihi_meisai.uf10_cd is 'UF10コード';
comment on column ryohiseisan_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column ryohiseisan_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column ryohiseisan_keihi_meisai.project_name is 'プロジェクト名';
comment on column ryohiseisan_keihi_meisai.segment_cd is 'セグメントコード';
comment on column ryohiseisan_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column ryohiseisan_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column ryohiseisan_keihi_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column ryohiseisan_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column ryohiseisan_keihi_meisai.touroku_time is '登録日時';
comment on column ryohiseisan_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column ryohiseisan_keihi_meisai.koushin_time is '更新日時';
INSERT INTO ryohiseisan_keihi_meisai
SELECT
  denpyou_id
  , denpyou_edano
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , keigen_zeiritsu_kbn
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , null -- kousaihi_ninzuu
  , null -- kousaihi_hitori_kingaku
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM ryohiseisan_keihi_meisai_old;
DROP TABLE ryohiseisan_keihi_meisai_old;


ALTER TABLE kaigai_ryohi_karibarai_keihi_meisai DROP CONSTRAINT IF EXISTS kaigai_ryohi_karibarai_keihi_meisai_PKEY;
ALTER TABLE kaigai_ryohi_karibarai_keihi_meisai RENAME TO kaigai_ryohi_karibarai_keihi_meisai_old;
create table kaigai_ryohi_karibarai_keihi_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , kaigai_flg character varying(1) not null
  , shiwake_edano integer not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , kazei_flg character varying(1) not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , heishu_cd character varying(4)
  , rate numeric(11, 5)
  , gaika numeric(19, 2)
  , currency_unit character varying(20)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kaigai_ryohi_karibarai_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano,kaigai_flg)
);
comment on table kaigai_ryohi_karibarai_keihi_meisai is '海外旅費仮払経費明細';
comment on column kaigai_ryohi_karibarai_keihi_meisai.denpyou_id is '伝票ID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kaigai_flg is '海外フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiyoubi is '使用日';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihiki_name is '取引名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.tekiyou is '摘要';
comment on column kaigai_ryohi_karibarai_keihi_meisai.zeiritsu is '税率';
comment on column kaigai_ryohi_karibarai_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kazei_flg is '課税フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.hontai_kingaku is '本体金額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shouhizeigaku is '消費税額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_ninzuu is '交際費人数';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.heishu_cd is '幣種コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.rate is 'レート';
comment on column kaigai_ryohi_karibarai_keihi_meisai.gaika is '外貨金額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.currency_unit is '通貨単位';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf1_cd is 'UF1コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf2_cd is 'UF2コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf3_cd is 'UF3コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf4_cd is 'UF4コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf5_cd is 'UF5コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf6_cd is 'UF6コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf7_cd is 'UF7コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf8_cd is 'UF8コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf9_cd is 'UF9コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf10_cd is 'UF10コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.project_name is 'プロジェクト名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.segment_cd is 'セグメントコード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.touroku_time is '登録日時';
comment on column kaigai_ryohi_karibarai_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.koushin_time is '更新日時';
INSERT INTO kaigai_ryohi_karibarai_keihi_meisai
SELECT
    denpyou_id
  , denpyou_edano
  , kaigai_flg
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , keigen_zeiritsu_kbn
  , kazei_flg
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , null -- kousaihi_ninzuu
  , null -- kousaihi_hitori_kingaku
  , heishu_cd
  , rate
  , gaika
  , currency_unit
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM kaigai_ryohi_karibarai_keihi_meisai_old;
DROP TABLE kaigai_ryohi_karibarai_keihi_meisai_old;



ALTER TABLE kaigai_ryohiseisan_keihi_meisai DROP CONSTRAINT IF EXISTS kaigai_ryohiseisan_keihi_meisai_PKEY;
ALTER TABLE kaigai_ryohiseisan_keihi_meisai RENAME TO kaigai_ryohiseisan_keihi_meisai_old;
create table kaigai_ryohiseisan_keihi_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , kaigai_flg character varying(1) not null
  , shiwake_edano integer not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(60) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , kazei_flg character varying(1) not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , houjin_card_riyou_flg character varying(1) not null
  , kaisha_tehai_flg character varying(1) not null
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , heishu_cd character varying(4)
  , rate numeric(11, 5)
  , gaika numeric(19, 2)
  , currency_unit character varying(20)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , himoduke_card_meisai bigint
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kaigai_ryohiseisan_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano,kaigai_flg)
);
comment on table kaigai_ryohiseisan_keihi_meisai is '海外旅費精算経費明細';
comment on column kaigai_ryohiseisan_keihi_meisai.denpyou_id is '伝票ID';
comment on column kaigai_ryohiseisan_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column kaigai_ryohiseisan_keihi_meisai.kaigai_flg is '海外フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column kaigai_ryohiseisan_keihi_meisai.shiyoubi is '使用日';
comment on column kaigai_ryohiseisan_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.torihiki_name is '取引名';
comment on column kaigai_ryohiseisan_keihi_meisai.tekiyou is '摘要';
comment on column kaigai_ryohiseisan_keihi_meisai.zeiritsu is '税率';
comment on column kaigai_ryohiseisan_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column kaigai_ryohiseisan_keihi_meisai.kazei_flg is '課税フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column kaigai_ryohiseisan_keihi_meisai.hontai_kingaku is '本体金額';
comment on column kaigai_ryohiseisan_keihi_meisai.shouhizeigaku is '消費税額';
comment on column kaigai_ryohiseisan_keihi_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_ninzuu is '交際費人数';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column kaigai_ryohiseisan_keihi_meisai.heishu_cd is '幣種コード';
comment on column kaigai_ryohiseisan_keihi_meisai.rate is 'レート';
comment on column kaigai_ryohiseisan_keihi_meisai.gaika is '外貨金額';
comment on column kaigai_ryohiseisan_keihi_meisai.currency_unit is '通貨単位';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column kaigai_ryohiseisan_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column kaigai_ryohiseisan_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column kaigai_ryohiseisan_keihi_meisai.uf1_cd is 'UF1コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf2_cd is 'UF2コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf3_cd is 'UF3コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf4_cd is 'UF4コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf5_cd is 'UF5コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf6_cd is 'UF6コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf7_cd is 'UF7コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf8_cd is 'UF8コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf9_cd is 'UF9コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf10_cd is 'UF10コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column kaigai_ryohiseisan_keihi_meisai.project_name is 'プロジェクト名';
comment on column kaigai_ryohiseisan_keihi_meisai.segment_cd is 'セグメントコード';
comment on column kaigai_ryohiseisan_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column kaigai_ryohiseisan_keihi_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column kaigai_ryohiseisan_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohiseisan_keihi_meisai.touroku_time is '登録日時';
comment on column kaigai_ryohiseisan_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohiseisan_keihi_meisai.koushin_time is '更新日時';
INSERT INTO kaigai_ryohiseisan_keihi_meisai
SELECT
  denpyou_id
  , denpyou_edano
  , kaigai_flg
  , shiwake_edano
  , shiyoubi
  , shouhyou_shorui_flg
  , torihiki_name
  , tekiyou
  , zeiritsu
  , keigen_zeiritsu_kbn
  , kazei_flg
  , shiharai_kingaku
  , hontai_kingaku
  , shouhizeigaku
  , houjin_card_riyou_flg
  , kaisha_tehai_flg
  , kousaihi_shousai_hyouji_flg
  , kousaihi_shousai
  , null -- kousaihi_ninzuu
  , null -- kousaihi_hitori_kingaku
  , heishu_cd
  , rate
  , gaika
  , currency_unit
  , kari_futan_bumon_cd
  , kari_futan_bumon_name
  , torihikisaki_cd
  , torihikisaki_name_ryakushiki
  , kari_kamoku_cd
  , kari_kamoku_name
  , kari_kamoku_edaban_cd
  , kari_kamoku_edaban_name
  , kari_kazei_kbn
  , kashi_futan_bumon_cd
  , kashi_futan_bumon_name
  , kashi_kamoku_cd
  , kashi_kamoku_name
  , kashi_kamoku_edaban_cd
  , kashi_kamoku_edaban_name
  , kashi_kazei_kbn
  , uf1_cd
  , uf1_name_ryakushiki
  , uf2_cd
  , uf2_name_ryakushiki
  , uf3_cd
  , uf3_name_ryakushiki
  , uf4_cd
  , uf4_name_ryakushiki
  , uf5_cd
  , uf5_name_ryakushiki
  , uf6_cd
  , uf6_name_ryakushiki
  , uf7_cd
  , uf7_name_ryakushiki
  , uf8_cd
  , uf8_name_ryakushiki
  , uf9_cd
  , uf9_name_ryakushiki
  , uf10_cd
  , uf10_name_ryakushiki
  , project_cd
  , project_name
  , segment_cd
  , segment_name_ryakushiki
  , tekiyou_cd
  , himoduke_card_meisai
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM kaigai_ryohiseisan_keihi_meisai_old;
DROP TABLE kaigai_ryohiseisan_keihi_meisai_old;




ALTER TABLE shiwake_pattern_master DROP CONSTRAINT IF EXISTS shiwake_pattern_master_PKEY;
ALTER TABLE shiwake_pattern_master RENAME TO shiwake_pattern_master_old;
create table shiwake_pattern_master (
  denpyou_kbn character varying(4) not null
  , shiwake_edano integer not null
  , delete_flg character varying(1) default '0' not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , bunrui1 character varying(20) not null
  , bunrui2 character varying(20) not null
  , bunrui3 character varying(20) not null
  , torihiki_name character varying(20) not null
  , tekiyou_flg character varying(1) not null
  , tekiyou character varying(20)
  , default_hyouji_flg character varying(1) not null
  , kousaihi_hyouji_flg character varying(1) not null
  , kousaihi_kijun_gaku numeric(6)
  , kousaihi_check_houhou character varying(1) not null
  , kousaihi_check_result character varying(1) not null
  , kake_flg character varying(1) not null
  , hyouji_jun integer not null
  , shain_cd_renkei_flg character varying(1) not null
  , edaban_renkei_flg character varying(1) not null
  , kari_futan_bumon_cd character varying not null
  , kari_kamoku_cd character varying not null
  , kari_kamoku_edaban_cd character varying not null
  , kari_torihikisaki_cd character varying not null
  , kari_project_cd character varying not null
  , kari_segment_cd character varying(8) not null
  , kari_uf1_cd character varying(20) not null
  , kari_uf2_cd character varying(20) not null
  , kari_uf3_cd character varying(20) not null
  , kari_uf4_cd character varying(20) not null
  , kari_uf5_cd character varying(20) not null
  , kari_uf6_cd character varying(20) not null
  , kari_uf7_cd character varying(20) not null
  , kari_uf8_cd character varying(20) not null
  , kari_uf9_cd character varying(20) not null
  , kari_uf10_cd character varying(20) not null
  , kari_uf_kotei1_cd character varying(20) not null
  , kari_uf_kotei2_cd character varying(20) not null
  , kari_uf_kotei3_cd character varying(20) not null
  , kari_uf_kotei4_cd character varying(20) not null
  , kari_uf_kotei5_cd character varying(20) not null
  , kari_uf_kotei6_cd character varying(20) not null
  , kari_uf_kotei7_cd character varying(20) not null
  , kari_uf_kotei8_cd character varying(20) not null
  , kari_uf_kotei9_cd character varying(20) not null
  , kari_uf_kotei10_cd character varying(20) not null
  , kari_kazei_kbn character varying not null
  , kari_zeiritsu character varying(10) not null
  , kari_keigen_zeiritsu_kbn character varying(1) not null
  , kashi_futan_bumon_cd1 character varying not null
  , kashi_kamoku_cd1 character varying not null
  , kashi_kamoku_edaban_cd1 character varying not null
  , kashi_torihikisaki_cd1 character varying not null
  , kashi_project_cd1 character varying not null
  , kashi_segment_cd1 character varying(8) not null
  , kashi_uf1_cd1 character varying(20) not null
  , kashi_uf2_cd1 character varying(20) not null
  , kashi_uf3_cd1 character varying(20) not null
  , kashi_uf4_cd1 character varying(20) not null
  , kashi_uf5_cd1 character varying(20) not null
  , kashi_uf6_cd1 character varying(20) not null
  , kashi_uf7_cd1 character varying(20) not null
  , kashi_uf8_cd1 character varying(20) not null
  , kashi_uf9_cd1 character varying(20) not null
  , kashi_uf10_cd1 character varying(20) not null
  , kashi_uf_kotei1_cd1 character varying(20) not null
  , kashi_uf_kotei2_cd1 character varying(20) not null
  , kashi_uf_kotei3_cd1 character varying(20) not null
  , kashi_uf_kotei4_cd1 character varying(20) not null
  , kashi_uf_kotei5_cd1 character varying(20) not null
  , kashi_uf_kotei6_cd1 character varying(20) not null
  , kashi_uf_kotei7_cd1 character varying(20) not null
  , kashi_uf_kotei8_cd1 character varying(20) not null
  , kashi_uf_kotei9_cd1 character varying(20) not null
  , kashi_uf_kotei10_cd1 character varying(20) not null
  , kashi_kazei_kbn1 character varying not null
  , kashi_futan_bumon_cd2 character varying not null
  , kashi_torihikisaki_cd2 character varying not null
  , kashi_kamoku_cd2 character varying not null
  , kashi_kamoku_edaban_cd2 character varying not null
  , kashi_project_cd2 character varying not null
  , kashi_segment_cd2 character varying(8) not null
  , kashi_uf1_cd2 character varying(20) not null
  , kashi_uf2_cd2 character varying(20) not null
  , kashi_uf3_cd2 character varying(20) not null
  , kashi_uf4_cd2 character varying(20) not null
  , kashi_uf5_cd2 character varying(20) not null
  , kashi_uf6_cd2 character varying(20) not null
  , kashi_uf7_cd2 character varying(20) not null
  , kashi_uf8_cd2 character varying(20) not null
  , kashi_uf9_cd2 character varying(20) not null
  , kashi_uf10_cd2 character varying(20) not null
  , kashi_uf_kotei1_cd2 character varying(20) not null
  , kashi_uf_kotei2_cd2 character varying(20) not null
  , kashi_uf_kotei3_cd2 character varying(20) not null
  , kashi_uf_kotei4_cd2 character varying(20) not null
  , kashi_uf_kotei5_cd2 character varying(20) not null
  , kashi_uf_kotei6_cd2 character varying(20) not null
  , kashi_uf_kotei7_cd2 character varying(20) not null
  , kashi_uf_kotei8_cd2 character varying(20) not null
  , kashi_uf_kotei9_cd2 character varying(20) not null
  , kashi_uf_kotei10_cd2 character varying(20) not null
  , kashi_kazei_kbn2 character varying not null
  , kashi_futan_bumon_cd3 character varying not null
  , kashi_torihikisaki_cd3 character varying not null
  , kashi_kamoku_cd3 character varying not null
  , kashi_kamoku_edaban_cd3 character varying not null
  , kashi_project_cd3 character varying not null
  , kashi_segment_cd3 character varying(8) not null
  , kashi_uf1_cd3 character varying(20) not null
  , kashi_uf2_cd3 character varying(20) not null
  , kashi_uf3_cd3 character varying(20) not null
  , kashi_uf4_cd3 character varying(20) not null
  , kashi_uf5_cd3 character varying(20) not null
  , kashi_uf6_cd3 character varying(20) not null
  , kashi_uf7_cd3 character varying(20) not null
  , kashi_uf8_cd3 character varying(20) not null
  , kashi_uf9_cd3 character varying(20) not null
  , kashi_uf10_cd3 character varying(20) not null
  , kashi_uf_kotei1_cd3 character varying(20) not null
  , kashi_uf_kotei2_cd3 character varying(20) not null
  , kashi_uf_kotei3_cd3 character varying(20) not null
  , kashi_uf_kotei4_cd3 character varying(20) not null
  , kashi_uf_kotei5_cd3 character varying(20) not null
  , kashi_uf_kotei6_cd3 character varying(20) not null
  , kashi_uf_kotei7_cd3 character varying(20) not null
  , kashi_uf_kotei8_cd3 character varying(20) not null
  , kashi_uf_kotei9_cd3 character varying(20) not null
  , kashi_uf_kotei10_cd3 character varying(20) not null
  , kashi_kazei_kbn3 character varying not null
  , kashi_futan_bumon_cd4 character varying not null
  , kashi_torihikisaki_cd4 character varying not null
  , kashi_kamoku_cd4 character varying not null
  , kashi_kamoku_edaban_cd4 character varying not null
  , kashi_project_cd4 character varying not null
  , kashi_segment_cd4 character varying(8) not null
  , kashi_uf1_cd4 character varying(20) not null
  , kashi_uf2_cd4 character varying(20) not null
  , kashi_uf3_cd4 character varying(20) not null
  , kashi_uf4_cd4 character varying(20) not null
  , kashi_uf5_cd4 character varying(20) not null
  , kashi_uf6_cd4 character varying(20) not null
  , kashi_uf7_cd4 character varying(20) not null
  , kashi_uf8_cd4 character varying(20) not null
  , kashi_uf9_cd4 character varying(20) not null
  , kashi_uf10_cd4 character varying(20) not null
  , kashi_uf_kotei1_cd4 character varying(20) not null
  , kashi_uf_kotei2_cd4 character varying(20) not null
  , kashi_uf_kotei3_cd4 character varying(20) not null
  , kashi_uf_kotei4_cd4 character varying(20) not null
  , kashi_uf_kotei5_cd4 character varying(20) not null
  , kashi_uf_kotei6_cd4 character varying(20) not null
  , kashi_uf_kotei7_cd4 character varying(20) not null
  , kashi_uf_kotei8_cd4 character varying(20) not null
  , kashi_uf_kotei9_cd4 character varying(20) not null
  , kashi_uf_kotei10_cd4 character varying(20) not null
  , kashi_kazei_kbn4 character varying not null
  , kashi_futan_bumon_cd5 character varying not null
  , kashi_torihikisaki_cd5 character varying not null
  , kashi_kamoku_cd5 character varying not null
  , kashi_kamoku_edaban_cd5 character varying not null
  , kashi_project_cd5 character varying not null
  , kashi_segment_cd5 character varying(8) not null
  , kashi_uf1_cd5 character varying(20) not null
  , kashi_uf2_cd5 character varying(20) not null
  , kashi_uf3_cd5 character varying(20) not null
  , kashi_uf4_cd5 character varying(20) not null
  , kashi_uf5_cd5 character varying(20) not null
  , kashi_uf6_cd5 character varying(20) not null
  , kashi_uf7_cd5 character varying(20) not null
  , kashi_uf8_cd5 character varying(20) not null
  , kashi_uf9_cd5 character varying(20) not null
  , kashi_uf10_cd5 character varying(20) not null
  , kashi_uf_kotei1_cd5 character varying(20) not null
  , kashi_uf_kotei2_cd5 character varying(20) not null
  , kashi_uf_kotei3_cd5 character varying(20) not null
  , kashi_uf_kotei4_cd5 character varying(20) not null
  , kashi_uf_kotei5_cd5 character varying(20) not null
  , kashi_uf_kotei6_cd5 character varying(20) not null
  , kashi_uf_kotei7_cd5 character varying(20) not null
  , kashi_uf_kotei8_cd5 character varying(20) not null
  , kashi_uf_kotei9_cd5 character varying(20) not null
  , kashi_uf_kotei10_cd5 character varying(20) not null
  , kashi_kazei_kbn5 character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shiwake_pattern_master_PKEY primary key (denpyou_kbn,shiwake_edano)
);
comment on table shiwake_pattern_master is '仕訳パターンマスター';
comment on column shiwake_pattern_master.denpyou_kbn is '伝票区分';
comment on column shiwake_pattern_master.shiwake_edano is '仕訳枝番号';
comment on column shiwake_pattern_master.delete_flg is '削除フラグ';
comment on column shiwake_pattern_master.yuukou_kigen_from is '有効期限開始日';
comment on column shiwake_pattern_master.yuukou_kigen_to is '有効期限終了日';
comment on column shiwake_pattern_master.bunrui1 is '分類1';
comment on column shiwake_pattern_master.bunrui2 is '分類2';
comment on column shiwake_pattern_master.bunrui3 is '分類3';
comment on column shiwake_pattern_master.torihiki_name is '取引名';
comment on column shiwake_pattern_master.tekiyou_flg is '摘要フラグ';
comment on column shiwake_pattern_master.tekiyou is '摘要';
comment on column shiwake_pattern_master.default_hyouji_flg is 'デフォルト表示フラグ';
comment on column shiwake_pattern_master.kousaihi_hyouji_flg is '交際費表示フラグ';
comment on column shiwake_pattern_master.kousaihi_kijun_gaku is '交際費基準額';
comment on column shiwake_pattern_master.kousaihi_check_houhou is '交際費チェック方法';
comment on column shiwake_pattern_master.kousaihi_check_result is '交際費チェック後登録許可';
comment on column shiwake_pattern_master.kake_flg is '掛けフラグ';
comment on column shiwake_pattern_master.hyouji_jun is '表示順';
comment on column shiwake_pattern_master.shain_cd_renkei_flg is '社員コード連携フラグ';
comment on column shiwake_pattern_master.edaban_renkei_flg is '財務枝番コード連携フラグ';
comment on column shiwake_pattern_master.kari_futan_bumon_cd is '借方負担部門コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_kamoku_cd is '借方科目コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_kamoku_edaban_cd is '借方科目枝番コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_torihikisaki_cd is '借方取引先コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_project_cd is '借方プロジェクトコード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_segment_cd is '借方セグメントコード';
comment on column shiwake_pattern_master.kari_uf1_cd is '借方UF1コード';
comment on column shiwake_pattern_master.kari_uf2_cd is '借方UF2コード';
comment on column shiwake_pattern_master.kari_uf3_cd is '借方UF3コード';
comment on column shiwake_pattern_master.kari_uf4_cd is '借方UF4コード';
comment on column shiwake_pattern_master.kari_uf5_cd is '借方UF5コード';
comment on column shiwake_pattern_master.kari_uf6_cd is '借方UF6コード';
comment on column shiwake_pattern_master.kari_uf7_cd is '借方UF7コード';
comment on column shiwake_pattern_master.kari_uf8_cd is '借方UF8コード';
comment on column shiwake_pattern_master.kari_uf9_cd is '借方UF9コード';
comment on column shiwake_pattern_master.kari_uf10_cd is '借方UF10コード';
comment on column shiwake_pattern_master.kari_uf_kotei1_cd is '借方UF1コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei2_cd is '借方UF2コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei3_cd is '借方UF3コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei4_cd is '借方UF4コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei5_cd is '借方UF5コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei6_cd is '借方UF6コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei7_cd is '借方UF7コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei8_cd is '借方UF8コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei9_cd is '借方UF9コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei10_cd is '借方UF10コード（固定）';
comment on column shiwake_pattern_master.kari_kazei_kbn is '借方課税区分（仕訳パターン）';
comment on column shiwake_pattern_master.kari_zeiritsu is '借方消費税率（仕訳パターン）';
comment on column shiwake_pattern_master.kari_keigen_zeiritsu_kbn is '借方軽減税率区分（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd1 is '貸方負担部門コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd1 is '貸方科目コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd1 is '貸方科目枝番コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd1 is '貸方取引先コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd1 is '貸方プロジェクトコード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd1 is '貸方セグメントコード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd1 is '貸方UF1コード１';
comment on column shiwake_pattern_master.kashi_uf2_cd1 is '貸方UF2コード１';
comment on column shiwake_pattern_master.kashi_uf3_cd1 is '貸方UF3コード１';
comment on column shiwake_pattern_master.kashi_uf4_cd1 is '貸方UF4コード１';
comment on column shiwake_pattern_master.kashi_uf5_cd1 is '貸方UF5コード１';
comment on column shiwake_pattern_master.kashi_uf6_cd1 is '貸方UF6コード１';
comment on column shiwake_pattern_master.kashi_uf7_cd1 is '貸方UF7コード１';
comment on column shiwake_pattern_master.kashi_uf8_cd1 is '貸方UF8コード１';
comment on column shiwake_pattern_master.kashi_uf9_cd1 is '貸方UF9コード１';
comment on column shiwake_pattern_master.kashi_uf10_cd1 is '貸方UF10コード１';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd1 is '貸方UF1コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd1 is '貸方UF2コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd1 is '貸方UF3コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd1 is '貸方UF4コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd1 is '貸方UF5コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd1 is '貸方UF6コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd1 is '貸方UF7コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd1 is '貸方UF8コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd1 is '貸方UF9コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd1 is '貸方UF10コード１（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn1 is '貸方課税区分１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd2 is '貸方負担部門コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd2 is '貸方取引先コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd2 is '貸方科目コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd2 is '貸方科目枝番コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd2 is '貸方プロジェクトコード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd2 is '貸方セグメントコード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd2 is '貸方UF1コード２';
comment on column shiwake_pattern_master.kashi_uf2_cd2 is '貸方UF2コード２';
comment on column shiwake_pattern_master.kashi_uf3_cd2 is '貸方UF3コード２';
comment on column shiwake_pattern_master.kashi_uf4_cd2 is '貸方UF4コード２';
comment on column shiwake_pattern_master.kashi_uf5_cd2 is '貸方UF5コード２';
comment on column shiwake_pattern_master.kashi_uf6_cd2 is '貸方UF6コード２';
comment on column shiwake_pattern_master.kashi_uf7_cd2 is '貸方UF7コード２';
comment on column shiwake_pattern_master.kashi_uf8_cd2 is '貸方UF8コード２';
comment on column shiwake_pattern_master.kashi_uf9_cd2 is '貸方UF9コード２';
comment on column shiwake_pattern_master.kashi_uf10_cd2 is '貸方UF10コード２';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd2 is '貸方UF1コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd2 is '貸方UF2コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd2 is '貸方UF3コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd2 is '貸方UF4コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd2 is '貸方UF5コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd2 is '貸方UF6コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd2 is '貸方UF7コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd2 is '貸方UF8コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd2 is '貸方UF9コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd2 is '貸方UF10コード２（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn2 is '貸方課税区分２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd3 is '貸方負担部門コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd3 is '貸方取引先コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd3 is '貸方科目コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd3 is '貸方科目枝番コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd3 is '貸方プロジェクトコード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd3 is '貸方セグメントコード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd3 is '貸方UF1コード３';
comment on column shiwake_pattern_master.kashi_uf2_cd3 is '貸方UF2コード３';
comment on column shiwake_pattern_master.kashi_uf3_cd3 is '貸方UF3コード３';
comment on column shiwake_pattern_master.kashi_uf4_cd3 is '貸方UF4コード３';
comment on column shiwake_pattern_master.kashi_uf5_cd3 is '貸方UF5コード３';
comment on column shiwake_pattern_master.kashi_uf6_cd3 is '貸方UF6コード３';
comment on column shiwake_pattern_master.kashi_uf7_cd3 is '貸方UF7コード３';
comment on column shiwake_pattern_master.kashi_uf8_cd3 is '貸方UF8コード３';
comment on column shiwake_pattern_master.kashi_uf9_cd3 is '貸方UF9コード３';
comment on column shiwake_pattern_master.kashi_uf10_cd3 is '貸方UF10コード３';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd3 is '貸方UF1コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd3 is '貸方UF2コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd3 is '貸方UF3コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd3 is '貸方UF4コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd3 is '貸方UF5コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd3 is '貸方UF6コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd3 is '貸方UF7コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd3 is '貸方UF8コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd3 is '貸方UF9コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd3 is '貸方UF10コード３（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn3 is '貸方課税区分３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd4 is '貸方負担部門コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd4 is '貸方取引先コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd4 is '貸方科目コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd4 is '貸方科目枝番コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd4 is '貸方プロジェクトコード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd4 is '貸方セグメントコード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd4 is '貸方UF1コード４';
comment on column shiwake_pattern_master.kashi_uf2_cd4 is '貸方UF2コード４';
comment on column shiwake_pattern_master.kashi_uf3_cd4 is '貸方UF3コード４';
comment on column shiwake_pattern_master.kashi_uf4_cd4 is '貸方UF4コード４';
comment on column shiwake_pattern_master.kashi_uf5_cd4 is '貸方UF5コード４';
comment on column shiwake_pattern_master.kashi_uf6_cd4 is '貸方UF6コード４';
comment on column shiwake_pattern_master.kashi_uf7_cd4 is '貸方UF7コード４';
comment on column shiwake_pattern_master.kashi_uf8_cd4 is '貸方UF8コード４';
comment on column shiwake_pattern_master.kashi_uf9_cd4 is '貸方UF9コード４';
comment on column shiwake_pattern_master.kashi_uf10_cd4 is '貸方UF10コード４';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd4 is '貸方UF1コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd4 is '貸方UF2コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd4 is '貸方UF3コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd4 is '貸方UF4コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd4 is '貸方UF5コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd4 is '貸方UF6コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd4 is '貸方UF7コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd4 is '貸方UF8コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd4 is '貸方UF9コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd4 is '貸方UF10コード４（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn4 is '貸方課税区分４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd5 is '貸方負担部門コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd5 is '貸方取引先コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd5 is '貸方科目コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd5 is '貸方科目枝番コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd5 is '貸方プロジェクトコード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd5 is '貸方セグメントコード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd5 is '貸方UF1コード５';
comment on column shiwake_pattern_master.kashi_uf2_cd5 is '貸方UF2コード５';
comment on column shiwake_pattern_master.kashi_uf3_cd5 is '貸方UF3コード５';
comment on column shiwake_pattern_master.kashi_uf4_cd5 is '貸方UF4コード５';
comment on column shiwake_pattern_master.kashi_uf5_cd5 is '貸方UF5コード５';
comment on column shiwake_pattern_master.kashi_uf6_cd5 is '貸方UF6コード５';
comment on column shiwake_pattern_master.kashi_uf7_cd5 is '貸方UF7コード５';
comment on column shiwake_pattern_master.kashi_uf8_cd5 is '貸方UF8コード５';
comment on column shiwake_pattern_master.kashi_uf9_cd5 is '貸方UF9コード５';
comment on column shiwake_pattern_master.kashi_uf10_cd5 is '貸方UF10コード５';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd5 is '貸方UF1コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd5 is '貸方UF2コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd5 is '貸方UF3コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd5 is '貸方UF4コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd5 is '貸方UF5コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd5 is '貸方UF6コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd5 is '貸方UF7コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd5 is '貸方UF8コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd5 is '貸方UF9コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd5 is '貸方UF10コード５（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn5 is '貸方課税区分５（仕訳パターン）';
comment on column shiwake_pattern_master.touroku_user_id is '登録ユーザーID';
comment on column shiwake_pattern_master.touroku_time is '登録日時';
comment on column shiwake_pattern_master.koushin_user_id is '更新ユーザーID';
comment on column shiwake_pattern_master.koushin_time is '更新日時';
INSERT INTO shiwake_pattern_master
SELECT
  denpyou_kbn
  , shiwake_edano
  , delete_flg
  , yuukou_kigen_from
  , yuukou_kigen_to
  , bunrui1
  , bunrui2
  , bunrui3
  , torihiki_name
  , tekiyou_flg
  , tekiyou
  , default_hyouji_flg
  , kousaihi_hyouji_flg
  , null -- kousaihi_kijun_gaku
  , '' -- kousaihi_check_houhou
  , '' -- kousaihi_check_result
  , kake_flg
  , hyouji_jun
  , shain_cd_renkei_flg
  , edaban_renkei_flg
  , kari_futan_bumon_cd
  , kari_kamoku_cd
  , kari_kamoku_edaban_cd
  , kari_torihikisaki_cd
  , kari_project_cd
  , kari_segment_cd
  , kari_uf1_cd
  , kari_uf2_cd
  , kari_uf3_cd
  , kari_uf4_cd
  , kari_uf5_cd
  , kari_uf6_cd
  , kari_uf7_cd
  , kari_uf8_cd
  , kari_uf9_cd
  , kari_uf10_cd
  , kari_uf_kotei1_cd
  , kari_uf_kotei2_cd
  , kari_uf_kotei3_cd
  , kari_uf_kotei4_cd
  , kari_uf_kotei5_cd
  , kari_uf_kotei6_cd
  , kari_uf_kotei7_cd
  , kari_uf_kotei8_cd
  , kari_uf_kotei9_cd
  , kari_uf_kotei10_cd
  , kari_kazei_kbn
  , kari_zeiritsu
  , kari_keigen_zeiritsu_kbn
  , kashi_futan_bumon_cd1
  , kashi_kamoku_cd1
  , kashi_kamoku_edaban_cd1
  , kashi_torihikisaki_cd1
  , kashi_project_cd1
  , kashi_segment_cd1
  , kashi_uf1_cd1
  , kashi_uf2_cd1
  , kashi_uf3_cd1
  , kashi_uf4_cd1
  , kashi_uf5_cd1
  , kashi_uf6_cd1
  , kashi_uf7_cd1
  , kashi_uf8_cd1
  , kashi_uf9_cd1
  , kashi_uf10_cd1
  , kashi_uf_kotei1_cd1
  , kashi_uf_kotei2_cd1
  , kashi_uf_kotei3_cd1
  , kashi_uf_kotei4_cd1
  , kashi_uf_kotei5_cd1
  , kashi_uf_kotei6_cd1
  , kashi_uf_kotei7_cd1
  , kashi_uf_kotei8_cd1
  , kashi_uf_kotei9_cd1
  , kashi_uf_kotei10_cd1
  , kashi_kazei_kbn1
  , kashi_futan_bumon_cd2
  , kashi_torihikisaki_cd2
  , kashi_kamoku_cd2
  , kashi_kamoku_edaban_cd2
  , kashi_project_cd2
  , kashi_segment_cd2
  , kashi_uf1_cd2
  , kashi_uf2_cd2
  , kashi_uf3_cd2
  , kashi_uf4_cd2
  , kashi_uf5_cd2
  , kashi_uf6_cd2
  , kashi_uf7_cd2
  , kashi_uf8_cd2
  , kashi_uf9_cd2
  , kashi_uf10_cd2
  , kashi_uf_kotei1_cd2
  , kashi_uf_kotei2_cd2
  , kashi_uf_kotei3_cd2
  , kashi_uf_kotei4_cd2
  , kashi_uf_kotei5_cd2
  , kashi_uf_kotei6_cd2
  , kashi_uf_kotei7_cd2
  , kashi_uf_kotei8_cd2
  , kashi_uf_kotei9_cd2
  , kashi_uf_kotei10_cd2
  , kashi_kazei_kbn2
  , kashi_futan_bumon_cd3
  , kashi_torihikisaki_cd3
  , kashi_kamoku_cd3
  , kashi_kamoku_edaban_cd3
  , kashi_project_cd3
  , kashi_segment_cd3
  , kashi_uf1_cd3
  , kashi_uf2_cd3
  , kashi_uf3_cd3
  , kashi_uf4_cd3
  , kashi_uf5_cd3
  , kashi_uf6_cd3
  , kashi_uf7_cd3
  , kashi_uf8_cd3
  , kashi_uf9_cd3
  , kashi_uf10_cd3
  , kashi_uf_kotei1_cd3
  , kashi_uf_kotei2_cd3
  , kashi_uf_kotei3_cd3
  , kashi_uf_kotei4_cd3
  , kashi_uf_kotei5_cd3
  , kashi_uf_kotei6_cd3
  , kashi_uf_kotei7_cd3
  , kashi_uf_kotei8_cd3
  , kashi_uf_kotei9_cd3
  , kashi_uf_kotei10_cd3
  , kashi_kazei_kbn3
  , kashi_futan_bumon_cd4
  , kashi_torihikisaki_cd4
  , kashi_kamoku_cd4
  , kashi_kamoku_edaban_cd4
  , kashi_project_cd4
  , kashi_segment_cd4
  , kashi_uf1_cd4
  , kashi_uf2_cd4
  , kashi_uf3_cd4
  , kashi_uf4_cd4
  , kashi_uf5_cd4
  , kashi_uf6_cd4
  , kashi_uf7_cd4
  , kashi_uf8_cd4
  , kashi_uf9_cd4
  , kashi_uf10_cd4
  , kashi_uf_kotei1_cd4
  , kashi_uf_kotei2_cd4
  , kashi_uf_kotei3_cd4
  , kashi_uf_kotei4_cd4
  , kashi_uf_kotei5_cd4
  , kashi_uf_kotei6_cd4
  , kashi_uf_kotei7_cd4
  , kashi_uf_kotei8_cd4
  , kashi_uf_kotei9_cd4
  , kashi_uf_kotei10_cd4
  , kashi_kazei_kbn4
  , kashi_futan_bumon_cd5
  , kashi_torihikisaki_cd5
  , kashi_kamoku_cd5
  , kashi_kamoku_edaban_cd5
  , kashi_project_cd5
  , kashi_segment_cd5
  , kashi_uf1_cd5
  , kashi_uf2_cd5
  , kashi_uf3_cd5
  , kashi_uf4_cd5
  , kashi_uf5_cd5
  , kashi_uf6_cd5
  , kashi_uf7_cd5
  , kashi_uf8_cd5
  , kashi_uf9_cd5
  , kashi_uf10_cd5
  , kashi_uf_kotei1_cd5
  , kashi_uf_kotei2_cd5
  , kashi_uf_kotei3_cd5
  , kashi_uf_kotei4_cd5
  , kashi_uf_kotei5_cd5
  , kashi_uf_kotei6_cd5
  , kashi_uf_kotei7_cd5
  , kashi_uf_kotei8_cd5
  , kashi_uf_kotei9_cd5
  , kashi_uf_kotei10_cd5
  , kashi_kazei_kbn5
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
FROM shiwake_pattern_master_old;
DROP TABLE shiwake_pattern_master_old;

-- 交際費表示フラグが設定されている場合はチェック無し、チェック後登録許可に変更
UPDATE shiwake_pattern_master SET kousaihi_kijun_gaku = 0, kousaihi_check_houhou = '0', kousaihi_check_result = '1' WHERE kousaihi_hyouji_flg = '0' OR kousaihi_hyouji_flg = '1';

-- K-140
-- 承認ルート(拠点)
ALTER TABLE shounin_route_kyoten RENAME TO shounin_route_kyoten_tmp;
create table shounin_route_kyoten (
  denpyou_id character varying(19) not null
  , edano integer not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , bumon_cd character varying(8) not null
  , bumon_full_name character varying not null
  , dairi_shounin_user_id character varying(30) not null
  , dairi_shounin_user_full_name character varying(50) not null
  , genzai_flg character varying(1) not null
  , joukyou_edano integer
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , primary key (denpyou_id,edano)
)WITHOUT OIDS;
comment on table shounin_route_kyoten is '承認ルート(拠点)';
comment on column shounin_route_kyoten.denpyou_id is '伝票ID';
comment on column shounin_route_kyoten.edano is '枝番号';
comment on column shounin_route_kyoten.user_id is 'ユーザーID';
comment on column shounin_route_kyoten.user_full_name is 'ユーザーフル名';
comment on column shounin_route_kyoten.bumon_cd is '部門コード';
comment on column shounin_route_kyoten.bumon_full_name is '部門フル名';
comment on column shounin_route_kyoten.dairi_shounin_user_id is '代理承認者ユーザーID';
comment on column shounin_route_kyoten.dairi_shounin_user_full_name is '代理承認者ユーザーフル名';
comment on column shounin_route_kyoten.genzai_flg is '現在フラグ';
comment on column shounin_route_kyoten.joukyou_edano is '承認状況枝番';
comment on column shounin_route_kyoten.touroku_user_id is '登録ユーザーID';
comment on column shounin_route_kyoten.touroku_time is '登録日時';
comment on column shounin_route_kyoten.koushin_user_id is '更新ユーザーID';
comment on column shounin_route_kyoten.koushin_time is '更新日時';
INSERT 
INTO shounin_route_kyoten( 
  denpyou_id
  , edano
  , user_id
  , user_full_name
  , bumon_cd
  , bumon_full_name
  , dairi_shounin_user_id
  , dairi_shounin_user_full_name
  , genzai_flg
  , joukyou_edano
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time
) 
SELECT
  denpyou_id
  , edano
  , user_id
  , user_full_name
  , ( 
    SELECT
      bumon_cd 
    FROM
      shozoku_bumon_wariate AS sbw 
    WHERE
      sbw.user_id = tmp.user_id 
      AND CAST(tmp.touroku_time AS DATE) BETWEEN sbw.yuukou_kigen_from AND sbw.yuukou_kigen_to 
    ORDER BY
      hyouji_jun
	LIMIT
      1 
  ) AS bumon_cd
  , bumon_full_name
  , dairi_shounin_user_id
  , dairi_shounin_user_full_name
  , genzai_flg
  , joukyou_edano
  , touroku_user_id
  , touroku_time
  , koushin_user_id
  , koushin_time 
FROM
  shounin_route_kyoten_tmp AS tmp;
DROP TABLE shounin_route_kyoten_tmp;

--QA59
--仕訳パターンマスター(財務拠点)
UPDATE shiwake_pattern_master_zaimu_kyoten SET shain_cd_renkei_flg = '1' WHERE shain_cd_renkei_flg = '0';

commit;
