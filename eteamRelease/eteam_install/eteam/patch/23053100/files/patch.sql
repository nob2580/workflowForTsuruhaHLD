SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--マスター取り込み一覧関係
DELETE FROM master_torikomi_ichiran_de3 WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
DELETE FROM master_torikomi_ichiran_sias WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
DELETE FROM master_torikomi_ichiran_mk2 WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_mk2 FROM '.\files\csv\master_torikomi_ichiran_mk2_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--マスター取り込み詳細関係
DELETE FROM master_torikomi_shousai_de3 WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
DELETE FROM master_torikomi_shousai_sias WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
DELETE FROM master_torikomi_shousai_mk2 WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_mk2 FROM '.\files\csv\master_torikomi_shousai_mk2_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--マスター管理一覧データ追加
DELETE FROM master_kanri_ichiran WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
\copy master_kanri_ichiran FROM '.\files\csv\master_kanri_ichiran_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- マスター管理版数のデータ追加
DELETE FROM master_kanri_hansuu WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
\copy master_kanri_hansuu FROM '.\files\csv\master_kanri_hansuu_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--内部コード設定
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting WHERE naibu_cd_name NOT IN ('kazei_kbn_kyoten_furikae', 'shiwake_pattern_denpyou_kbn_kyoten', 'fusen_color', 'ebunsho_shubetsu_kyoten');
\copy naibu_cd_setting FROM '..\..\work\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- 支払依頼申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- 請求書払申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

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

-- invoiceカラム追加
-- 負担部門テーブルに仕入区分カラム、入力開始日カラム、入力終了日カラムを追加
ALTER TABLE bumon_master ADD COLUMN IF NOT EXISTS shiire_kbn smallint;
comment on column bumon_master.shiire_kbn is '仕入区分';
ALTER TABLE bumon_master ADD COLUMN IF NOT EXISTS nyuryoku_from_date date;
comment on column bumon_master.nyuryoku_from_date is '入力開始日';
ALTER TABLE bumon_master ADD COLUMN IF NOT EXISTS nyuryoku_to_date date;
comment on column bumon_master.nyuryoku_to_date is '入力終了日';

-- 伝票一覧テーブルに事業者区分カラム、社員番号（起票者）カラム、支払先名(一覧検索用)カラム、税抜明細金額(一覧検索用)カラム、税抜金額カラムを追加
ALTER TABLE denpyou_ichiran ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying[];
comment on column denpyou_ichiran.jigyousha_kbn is '事業者区分';
ALTER TABLE denpyou_ichiran ADD COLUMN IF NOT EXISTS shain_no character varying(15);
comment on column denpyou_ichiran.shain_no is '社員番号（起票者）';
ALTER TABLE denpyou_ichiran ADD COLUMN IF NOT EXISTS shiharai_name character varying[];
comment on column denpyou_ichiran.shiharai_name is '支払先名(一覧検索用)';
ALTER TABLE denpyou_ichiran ADD COLUMN IF NOT EXISTS zeinuki_meisai_kingaku numeric[];
comment on column denpyou_ichiran.zeinuki_meisai_kingaku is '税抜明細金額(一覧検索用)';
ALTER TABLE denpyou_ichiran ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column denpyou_ichiran.zeinuki_kingaku is '税抜金額';

-- 振替テーブルに借方分離区分カラム、貸方分離区分カラム、借方事業者区分カラム、借方仕入区分カラム、借方税額計算方式カラム、貸方事業者区分カラム、貸方仕入区分カラム、貸方税額計算方式カラム、インボイス対応伝票カラムを追加
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kari_bunri_kbn character varying(1);
comment on column furikae.kari_bunri_kbn is '借方分離区分';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kashi_bunri_kbn character varying(1);
comment on column furikae.kashi_bunri_kbn is '貸方分離区分';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kari_jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column furikae.kari_jigyousha_kbn is '借方事業者区分';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column furikae.kari_shiire_kbn is '借方仕入区分';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kari_zeigaku_houshiki character varying(1) NOT NULL default '0';
comment on column furikae.kari_zeigaku_houshiki is '借方税額計算方式';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kashi_jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column furikae.kashi_jigyousha_kbn is '貸方事業者区分';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column furikae.kashi_shiire_kbn is '貸方仕入区分';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kashi_zeigaku_houshiki character varying(1) NOT NULL default '0';
comment on column furikae.kashi_zeigaku_houshiki is '貸方税額計算方式';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column furikae.invoice_denpyou is 'インボイス対応伝票';

-- インボイス開始テーブルの作成
create table if not exists invoice_start (
  invoice_flg character varying(1) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp(6) without time zone not null
  );
comment on table invoice_start is 'インボイス開始';
comment on column invoice_start.invoice_flg is 'インボイス開始フラグ';
comment on column invoice_start.touroku_user_id is '登録ユーザーID';
comment on column invoice_start.touroku_time is '登録日時';

--振替テーブルの摘要カラムの制限を120に変更
ALTER TABLE furikae ALTER COLUMN tekiyou TYPE character varying(120);

-- 自動引落テーブルに入力方式カラム、インボイス対応伝票カラムを追加
ALTER TABLE jidouhikiotoshi ADD COLUMN IF NOT EXISTS nyuryoku_houshiki character varying(1) NOT NULL default '0';
comment on column jidouhikiotoshi.nyuryoku_houshiki is '入力方式';
ALTER TABLE jidouhikiotoshi ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column jidouhikiotoshi.invoice_denpyou is 'インボイス対応伝票';

-- 自動引落明細テーブルに事業者区分カラム、分離区分カラム、借方仕入区分カラム、貸方仕入区分カラム、インボイス対応伝票カラムを追加
ALTER TABLE jidouhikiotoshi_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column jidouhikiotoshi_meisai.jigyousha_kbn is '事業者区分';
ALTER TABLE jidouhikiotoshi_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column jidouhikiotoshi_meisai.bunri_kbn is '分離区分';
ALTER TABLE jidouhikiotoshi_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column jidouhikiotoshi_meisai.kari_shiire_kbn is '借方仕入区分';
ALTER TABLE jidouhikiotoshi_meisai ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column jidouhikiotoshi_meisai.kashi_shiire_kbn is '貸方仕入区分';

--自動引落明細テーブルの摘要カラムの制限を120に変更
ALTER TABLE jidouhikiotoshi_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- 海外旅費精算テーブルに分離区分カラム、借方仕入区分カラム、貸方仕入区分カラム、海外分離区分カラム、海外借方仕入区分カラム、海外貸方仕入区分カラム、インボイス対応伝票カラムを追加
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column kaigai_ryohiseisan.bunri_kbn is '分離区分';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan.kari_shiire_kbn is '借方仕入区分';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan.kashi_shiire_kbn is '貸方仕入区分';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS kaigai_bunri_kbn character varying(1);
comment on column kaigai_ryohiseisan.kaigai_bunri_kbn is '海外分離区分';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS kaigai_kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan.kaigai_kari_shiire_kbn is '海外借方仕入区分';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS kaigai_kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan.kaigai_kashi_shiire_kbn is '海外貸方仕入区分';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column kaigai_ryohiseisan.invoice_denpyou is 'インボイス対応伝票';

--海外旅費精算テーブルの摘要・海外摘要カラムの制限を120に変更
ALTER TABLE kaigai_ryohiseisan ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE kaigai_ryohiseisan ALTER COLUMN kaigai_tekiyou TYPE character varying(120);

-- 海外旅費精算経費明細テーブルに支払先名カラム、事業者区分カラム、分離区分カラム、借方仕入区分カラム、貸方仕入区分カラムを追加
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column kaigai_ryohiseisan_keihi_meisai.shiharaisaki_name is '支払先名';
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column kaigai_ryohiseisan_keihi_meisai.jigyousha_kbn is '事業者区分';
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column kaigai_ryohiseisan_keihi_meisai.bunri_kbn is '分離区分';
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_shiire_kbn is '借方仕入区分';
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_shiire_kbn is '貸方仕入区分';

--海外旅費精算経費明細テーブルの摘要・海外摘要カラムの制限を120に変更
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- 海外旅費精算明細テーブルに支払先名カラム、事業者区分カラム、税抜金額カラム、消費税額カラムを追加
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column kaigai_ryohiseisan_meisai.shiharaisaki_name is '支払先名';
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column kaigai_ryohiseisan_meisai.jigyousha_kbn is '事業者区分';
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column kaigai_ryohiseisan_meisai.zeinuki_kingaku is '税抜金額';
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS shouhizeigaku numeric(15) NOT NULL default 0;
comment on column kaigai_ryohiseisan_meisai.shouhizeigaku is '消費税額';

-- 以前より海外旅費精算経費明細テーブルの本体金額、消費税額にnullが格納されているため「0」をセット
UPDATE kaigai_ryohiseisan_keihi_meisai SET hontai_kingaku = 0, shouhizeigaku = 0;

-- 旅費系・仮払系伝票の摘要カラムの制限を120に変更
ALTER TABLE ryohi_karibarai ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE ryohiseisan_keihi_meisai ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE ryohi_karibarai_keihi_meisai ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE kaigai_ryohi_karibarai ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE kaigai_ryohi_karibarai_keihi_meisai ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE karibarai ALTER COLUMN tekiyou TYPE character varying(120);

-- 科目枝番残高テーブルに課税区分カラム、分離区分カラムを追加
ALTER TABLE kamoku_edaban_zandaka ADD COLUMN IF NOT EXISTS kazei_kbn smallint;
comment on column kamoku_edaban_zandaka.kazei_kbn is '課税区分';
ALTER TABLE kamoku_edaban_zandaka ADD COLUMN IF NOT EXISTS bunri_kbn smallint;
comment on column kamoku_edaban_zandaka.bunri_kbn is '分離区分';

-- 経費精算テーブルにインボイス対応伝票カラムを追加
ALTER TABLE keihiseisan ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column keihiseisan.invoice_denpyou is 'インボイス対応伝票';

-- 経費精算明細テーブルに事業者区分カラム、支払先名カラム、分離区分カラム、借方仕入区分カラム、貸方仕入区分カラムを追加
ALTER TABLE keihiseisan_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column keihiseisan_meisai.jigyousha_kbn is '事業者区分';
ALTER TABLE keihiseisan_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column keihiseisan_meisai.shiharaisaki_name is '支払先名';
ALTER TABLE keihiseisan_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column keihiseisan_meisai.bunri_kbn is '分離区分';
ALTER TABLE keihiseisan_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column keihiseisan_meisai.kari_shiire_kbn is '借方仕入区分';
ALTER TABLE keihiseisan_meisai ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column keihiseisan_meisai.kashi_shiire_kbn is '貸方仕入区分';

--経費精算明細テーブルの摘要カラムの制限を120に変更
ALTER TABLE keihiseisan_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- (期別)財務仕訳(ki_shiwake)の摘要カラムの制限を120に変更
ALTER TABLE ki_shiwake ALTER COLUMN rtky TYPE character varying(120);
ALTER TABLE ki_shiwake ALTER COLUMN stky TYPE character varying(120);

-- （期別）部門テーブルに仕入区分カラム、入力開始日カラム、入力終了日カラムを追加
ALTER TABLE ki_bumon ADD COLUMN IF NOT EXISTS shiire_kbn smallint;
comment on column ki_bumon.shiire_kbn is '仕入区分';
ALTER TABLE ki_bumon ADD COLUMN IF NOT EXISTS nyuryoku_from_date date;
comment on column ki_bumon.nyuryoku_from_date is '入力開始日';
ALTER TABLE ki_bumon ADD COLUMN IF NOT EXISTS nyuryoku_to_date date;
comment on column ki_bumon.nyuryoku_to_date is '入力終了日';

-- （期別）消費税設定テーブルの作成
-- 拠点未使用の場合は新規テーブルになる
-- 従来のカラムのみ先出し（後で追加されるのでクローンコード回避）
create table if not exists ki_shouhizei_setting (
  kesn smallint not null,
  shiire_zeigaku_anbun_flg smallint not null,
  shouhizei_kbn smallint not null,
  hasuu_shori_flg smallint not null,
  zeigaku_keisan_flg smallint not null,
  shouhizeitaishou_minyuryoku_flg smallint not null,
  constraint ki_shouhizei_setting_pkey PRIMARY KEY (kesn)
);

-- コメント更新はカラム追加とは別に行われるので従来分のみ先出し
comment on table ki_shouhizei_setting is '（期別）消費税設定';
comment on column ki_shouhizei_setting.kesn is '内部決算期';
comment on column ki_shouhizei_setting.shiire_zeigaku_anbun_flg is '仕入税額按分フラグ';
comment on column ki_shouhizei_setting.shouhizei_kbn is '消費税区分';
comment on column ki_shouhizei_setting.hasuu_shori_flg is '端数処理フラグ';
comment on column ki_shouhizei_setting.zeigaku_keisan_flg is '税額計算フラグ';
comment on column ki_shouhizei_setting.shouhizeitaishou_minyuryoku_flg is '消費税対象科目未入力フラグ';

-- （期別）消費税設定テーブルに資産カラム、売上カラム、仕入カラム、経費カラム、部門別処理カラム、特定仕入取引等カラム、0円消費税作成カラム、仮払・仮受消費税　部門カラム、取引先カラム、枝番カラム、プロジェクトカラム、セグメントカラム、ユニバーサル１カラム、ユニバーサル２カラム、ユニバーサル３カラム、工事カラム、工種カラム、仮受消費税　売上カラム、資産カラム、仮払消費税　仕入カラム、経費カラム、資産カラム、売上税額計算方式カラム、仕入税額計算方式カラム、仕入税額控除経過措置適用カラムを追加
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shisan smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shisan is '資産';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS uriage smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.uriage is '売上';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shiire smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shiire is '仕入';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS keihi smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.keihi is '経費';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS bumonbetsu_shori smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.bumonbetsu_shori is '部門別処理';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS tokuteishiire smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.tokuteishiire is '特定仕入取引等';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS zero_shouhizei smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.zero_shouhizei is '0円消費税作成';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_bumon smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_bumon is '仮払・仮受消費税　部門';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_torihikisaki smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_torihikisaki is '取引先';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_edaban smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_edaban is '枝番';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_project smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_project is 'プロジェクト';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_segment smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_segment is 'セグメント';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_uf1 smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_uf1 is 'ユニバーサル１';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_uf2 smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_uf2 is 'ユニバーサル２';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_uf3 smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_uf3 is 'ユニバーサル３';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_kouji smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_kouji is '工事';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_koushu smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_koushu is '工種';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS ukeshouhizei_uriage character varying(15) NOT NULL default '';
comment on column ki_shouhizei_setting.ukeshouhizei_uriage is '仮受消費税　売上';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS ukeshouhizei_shisan character varying(15) NOT NULL default '';
comment on column ki_shouhizei_setting.ukeshouhizei_shisan is '資産';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS haraishouhizei_shiire character varying(15) NOT NULL default '';
comment on column ki_shouhizei_setting.haraishouhizei_shiire is '仮払消費税　仕入';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS haraishouhizei_keihi character varying(15) NOT NULL default '';
comment on column ki_shouhizei_setting.haraishouhizei_keihi is '経費';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS haraisyouhizei_shisan character varying(15) NOT NULL default '';
comment on column ki_shouhizei_setting.haraisyouhizei_shisan is '資産';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS uriagezeigaku_keisan smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.uriagezeigaku_keisan is '売上税額計算方式';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shiirezeigaku_keisan smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shiirezeigaku_keisan is '仕入税額計算方式';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shiirezeigaku_keikasothi smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shiirezeigaku_keikasothi is '仕入税額控除経過措置適用';

-- 交通費精算テーブルに分離区分カラム、借方仕入区分カラム、貸方仕入区分カラム、インボイス対応伝票カラムを追加
ALTER TABLE koutsuuhiseisan ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column koutsuuhiseisan.bunri_kbn is '分離区分';
ALTER TABLE koutsuuhiseisan ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column koutsuuhiseisan.kari_shiire_kbn is '借方仕入区分';
ALTER TABLE koutsuuhiseisan ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column koutsuuhiseisan.kashi_shiire_kbn is '貸方仕入区分';
ALTER TABLE koutsuuhiseisan ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column koutsuuhiseisan.invoice_denpyou is 'インボイス対応伝票';

--交通費精算テーブルの摘要カラムの制限を120に変更
ALTER TABLE koutsuuhiseisan ALTER COLUMN tekiyou TYPE character varying(120);

-- 交通費精算明細テーブルに支払先名カラム、事業者区分カラム、税抜金額カラム、消費税額カラムを追加
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column koutsuuhiseisan_meisai.shiharaisaki_name is '支払先名';
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column koutsuuhiseisan_meisai.jigyousha_kbn is '事業者区分';
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column koutsuuhiseisan_meisai.zeinuki_kingaku is '税抜金額';
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS shouhizeigaku numeric(15) NOT NULL default 0;
comment on column koutsuuhiseisan_meisai.shouhizeigaku is '消費税額';

-- 旅費精算テーブルに分離区分カラム、借方仕入区分カラム、貸方仕入区分カラム、インボイス対応伝票カラムを追加
ALTER TABLE ryohiseisan ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column ryohiseisan.bunri_kbn is '分離区分';
ALTER TABLE ryohiseisan ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column ryohiseisan.kari_shiire_kbn is '借方仕入区分';
ALTER TABLE ryohiseisan ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column ryohiseisan.kashi_shiire_kbn is '貸方仕入区分';
ALTER TABLE ryohiseisan ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column ryohiseisan.invoice_denpyou is 'インボイス対応伝票';

--旅費精算テーブルの摘要カラムの制限を120に変更
ALTER TABLE ryohiseisan ALTER COLUMN tekiyou TYPE character varying(120);

-- 旅費精算経費明細テーブルに支払先名カラム、事業者区分カラム、分離区分カラム、借方仕入区分カラム、貸方仕入区分カラムを追加
ALTER TABLE ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column ryohiseisan_keihi_meisai.shiharaisaki_name is '支払先名';
ALTER TABLE ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column ryohiseisan_keihi_meisai.jigyousha_kbn is '事業者区分';
ALTER TABLE ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column ryohiseisan_keihi_meisai.bunri_kbn is '分離区分';
ALTER TABLE ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column ryohiseisan_keihi_meisai.kari_shiire_kbn is '借方仕入区分';
ALTER TABLE ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column ryohiseisan_keihi_meisai.kashi_shiire_kbn is '貸方仕入区分';

-- 旅費精算明細テーブルに支払先名カラム、事業者区分カラム、税抜金額カラム、消費税額カラムを追加
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column ryohiseisan_meisai.shiharaisaki_name is '支払先名';
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column ryohiseisan_meisai.jigyousha_kbn is '事業者区分';
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column ryohiseisan_meisai.zeinuki_kingaku is '税抜金額';
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS shouhizeigaku numeric(15) NOT NULL default 0;
comment on column ryohiseisan_meisai.shouhizeigaku is '消費税額';

-- 請求書払いテーブルに入力方式カラム、インボイス対応伝票カラムを追加
ALTER TABLE seikyuushobarai ADD COLUMN IF NOT EXISTS nyuryoku_houshiki character varying(1) NOT NULL default '0';
comment on column seikyuushobarai.nyuryoku_houshiki is '入力方式';
ALTER TABLE seikyuushobarai ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column seikyuushobarai.invoice_denpyou is 'インボイス対応伝票';

-- 請求書払い明細テーブルに事業者区分カラム、分離区分カラム、借方仕入区分カラム、貸方仕入区分カラムを追加
ALTER TABLE seikyuushobarai_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column seikyuushobarai_meisai.jigyousha_kbn is '事業者区分';
ALTER TABLE seikyuushobarai_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column seikyuushobarai_meisai.bunri_kbn is '分離区分';
ALTER TABLE seikyuushobarai_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column seikyuushobarai_meisai.kari_shiire_kbn is '借方仕入区分';
ALTER TABLE seikyuushobarai_meisai ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column seikyuushobarai_meisai.kashi_shiire_kbn is '貸方仕入区分';

--請求書払い明細テーブルの摘要カラムの制限を120に変更
ALTER TABLE seikyuushobarai_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- 支払依頼テーブルに入力方式カラム、事業者区分カラム、事業者登録番号カラム、インボイス対応伝票カラムを追加
ALTER TABLE shiharai_irai ADD COLUMN IF NOT EXISTS nyuryoku_houshiki character varying(1) NOT NULL default '0';
comment on column shiharai_irai.nyuryoku_houshiki is '入力方式';
ALTER TABLE shiharai_irai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(15) NOT NULL default '0';
comment on column shiharai_irai.jigyousha_kbn is '事業者区分';
ALTER TABLE shiharai_irai ADD COLUMN IF NOT EXISTS jigyousha_no character varying(15) NOT NULL default '';
comment on column shiharai_irai.jigyousha_no is '事業者登録番号';
ALTER TABLE shiharai_irai ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column shiharai_irai.invoice_denpyou is 'インボイス対応伝票';

-- 支払依頼明細テーブルに分離区分カラム、借方仕入区分カラム、税抜金額カラム、消費税額カラムを追加
ALTER TABLE shiharai_irai_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column shiharai_irai_meisai.bunri_kbn is '分離区分';
ALTER TABLE shiharai_irai_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column shiharai_irai_meisai.kari_shiire_kbn is '借方仕入区分';
ALTER TABLE shiharai_irai_meisai ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column shiharai_irai_meisai.zeinuki_kingaku is '税抜金額';
ALTER TABLE shiharai_irai_meisai ADD COLUMN IF NOT EXISTS shouhizeigaku numeric(15) NOT NULL default 0;
comment on column shiharai_irai_meisai.shouhizeigaku is '消費税額';

--支払依頼明細テーブルの摘要カラムの制限を120に変更
ALTER TABLE shiharai_irai_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- 仕訳パターンマスターテーブルに旧借方課税区分（仕訳パターン）カラム、旧貸方課税区分１（仕訳パターン）カラム、旧貸方課税区分２（仕訳パターン）カラム、旧貸方課税区分３（仕訳パターン）カラム、旧貸方課税区分４（仕訳パターン）カラム、旧貸方課税区分５（仕訳パターン）カラム、借方分離区分（仕訳パターン）カラム、貸方分離区分１（仕訳パターン）カラム、貸方分離区分２（仕訳パターン）カラム、貸方分離区分３（仕訳パターン）カラム、貸方分離区分４（仕訳パターン）カラム、貸方分離区分５（仕訳パターン）カラム、借方仕入区分（仕訳パターン）カラム、貸方仕入区分１（仕訳パターン）カラム、貸方仕入区分２（仕訳パターン）カラム、貸方仕入区分３（仕訳パターン）カラム、貸方仕入区分４（仕訳パターン）カラム、貸方仕入区分５（仕訳パターン）カラムを追加
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kari_kazei_kbn character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kari_kazei_kbn is '旧借方課税区分（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kashi_kazei_kbn1 character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn1 is '旧貸方課税区分１（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kashi_kazei_kbn2 character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn2 is '旧貸方課税区分２（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kashi_kazei_kbn3 character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn3 is '旧貸方課税区分３（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kashi_kazei_kbn4 character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn4 is '旧貸方課税区分４（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kashi_kazei_kbn5 character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn5 is '旧貸方課税区分５（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kari_bunri_kbn character varying(1);
comment on column shiwake_pattern_master.kari_bunri_kbn is '借方分離区分（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_bunri_kbn1 character varying(1);
comment on column shiwake_pattern_master.kashi_bunri_kbn1 is '貸方分離区分１（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_bunri_kbn2 character varying(1);
comment on column shiwake_pattern_master.kashi_bunri_kbn2 is '貸方分離区分２（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_bunri_kbn3 character varying(1);
comment on column shiwake_pattern_master.kashi_bunri_kbn3 is '貸方分離区分３（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_bunri_kbn4 character varying(1);
comment on column shiwake_pattern_master.kashi_bunri_kbn4 is '貸方分離区分４（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_bunri_kbn5 character varying(1);
comment on column shiwake_pattern_master.kashi_bunri_kbn5 is '貸方分離区分５（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1);
comment on column shiwake_pattern_master.kari_shiire_kbn is '借方仕入区分（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_shiire_kbn1 character varying(1);
comment on column shiwake_pattern_master.kashi_shiire_kbn1 is '貸方仕入区分１（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_shiire_kbn2 character varying(1);
comment on column shiwake_pattern_master.kashi_shiire_kbn2 is '貸方仕入区分２（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_shiire_kbn3 character varying(1);
comment on column shiwake_pattern_master.kashi_shiire_kbn3 is '貸方仕入区分３（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_shiire_kbn4 character varying(1);
comment on column shiwake_pattern_master.kashi_shiire_kbn4 is '貸方仕入区分４（仕訳パターン）';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_shiire_kbn5 character varying(1);
comment on column shiwake_pattern_master.kashi_shiire_kbn5 is '貸方仕入区分５（仕訳パターン）';

-- 仕訳パターンマスターテーブルの旧課税区分（借方・貸方１～５）に現在の登録内容をコピー
UPDATE shiwake_pattern_master SET old_kari_kazei_kbn = kari_kazei_kbn;
UPDATE shiwake_pattern_master SET old_kashi_kazei_kbn1 = kashi_kazei_kbn1;
UPDATE shiwake_pattern_master SET old_kashi_kazei_kbn2 = kashi_kazei_kbn2;
UPDATE shiwake_pattern_master SET old_kashi_kazei_kbn3 = kashi_kazei_kbn3;
UPDATE shiwake_pattern_master SET old_kashi_kazei_kbn4 = kashi_kazei_kbn4;
UPDATE shiwake_pattern_master SET old_kashi_kazei_kbn5 = kashi_kazei_kbn5;

--仕訳パターンマスターテーブルの摘要カラムの制限を60に変更
ALTER TABLE shiwake_pattern_master ALTER COLUMN tekiyou TYPE character varying(60);

-- 取引先マスターテーブルに入力許可開始年月日カラム、入力許可終了年月日カラム、適格請求書発行事業者登録番号カラム、免税事業者等フラグカラムを追加
ALTER TABLE torihikisaki_master ADD COLUMN IF NOT EXISTS nyuryoku_from_date date;
comment on column torihikisaki_master.nyuryoku_from_date is '入力許可開始年月日';
ALTER TABLE torihikisaki_master ADD COLUMN IF NOT EXISTS nyuryoku_to_date date;
comment on column torihikisaki_master.nyuryoku_to_date is '入力許可終了年月日';
ALTER TABLE torihikisaki_master ADD COLUMN IF NOT EXISTS tekikaku_no character varying(14);
comment on column torihikisaki_master.tekikaku_no is '適格請求書発行事業者登録番号';
ALTER TABLE torihikisaki_master ADD COLUMN IF NOT EXISTS menzei_jigyousha_flg character varying(1) NOT NULL default '0';
comment on column torihikisaki_master.menzei_jigyousha_flg is '免税事業者等フラグ';

-- 付替テーブルに付替元事業者区分カラム、付替元分離区分カラム、付替元仕入区分カラム、付替元税額計算方式カラム、インボイス対応伝票カラムを追加
ALTER TABLE tsukekae ADD COLUMN IF NOT EXISTS moto_jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column tsukekae.moto_jigyousha_kbn is '付替元事業者区分';
ALTER TABLE tsukekae ADD COLUMN IF NOT EXISTS moto_bunri_kbn character varying(1);
comment on column tsukekae.moto_bunri_kbn is '付替元分離区分';
ALTER TABLE tsukekae ADD COLUMN IF NOT EXISTS moto_shiire_kbn character varying(1) NOT NULL default '';
comment on column tsukekae.moto_shiire_kbn is '付替元仕入区分';
ALTER TABLE tsukekae ADD COLUMN IF NOT EXISTS moto_zeigaku_houshiki character varying(1) NOT NULL default '0';
comment on column tsukekae.moto_zeigaku_houshiki is '付替元税額計算方式';
ALTER TABLE tsukekae ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column tsukekae.invoice_denpyou is 'インボイス対応伝票';

-- 付替明細テーブルに付替先事業者区分カラム、付替先分離区分カラム、付替先仕入区分カラム、付替先税額計算方式カラムを追加
ALTER TABLE tsukekae_meisai ADD COLUMN IF NOT EXISTS saki_jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column tsukekae_meisai.saki_jigyousha_kbn is '付替先事業者区分';
ALTER TABLE tsukekae_meisai ADD COLUMN IF NOT EXISTS saki_bunri_kbn character varying(1);
comment on column tsukekae_meisai.saki_bunri_kbn is '付替先分離区分';
ALTER TABLE tsukekae_meisai ADD COLUMN IF NOT EXISTS saki_shiire_kbn character varying(1) NOT NULL default '';
comment on column tsukekae_meisai.saki_shiire_kbn is '付替先仕入区分';
ALTER TABLE tsukekae_meisai ADD COLUMN IF NOT EXISTS saki_zeigaku_houshiki character varying(1) NOT NULL default '0';
comment on column tsukekae_meisai.saki_zeigaku_houshiki is '付替先税額計算方式';

-- 付替明細の摘要カラムの制限を120に変更
ALTER TABLE tsukekae_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- 通勤定期テーブルに税抜金額カラム、消費税額カラム、入力方式カラム、支払先名カラム、事業者区分カラム、分離区分カラム、借方仕入区分カラム、貸方仕入区分カラム、インボイス対応伝票カラムを追加
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column tsuukinteiki.zeinuki_kingaku is '税抜金額';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS shouhizeigaku numeric(15) NOT NULL default 0;
comment on column tsuukinteiki.shouhizeigaku is '消費税額';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column tsuukinteiki.shiharaisaki_name is '支払先名';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column tsuukinteiki.jigyousha_kbn is '事業者区分';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column tsuukinteiki.bunri_kbn is '分離区分';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column tsuukinteiki.kari_shiire_kbn is '借方仕入区分';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column tsuukinteiki.kashi_shiire_kbn is '貸方仕入区分';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column tsuukinteiki.invoice_denpyou is 'インボイス対応伝票';

--通勤定期テーブルの摘要カラムの制限を120に変更
ALTER TABLE tsuukinteiki ALTER COLUMN tekiyou TYPE character varying(120);

-- 消費税詳細の画面権限制御を追加（1件のみなので直接INSERT）
INSERT INTO gamen_kengen_seigyo(gamen_id, gamen_name, bumon_shozoku_riyoukanou_flg, system_kanri_riyoukanou_flg, workflow_riyoukanou_flg, kaishasettei_riyoukanou_flg, keirishori_riyoukanou_flg, kinou_seigyo_cd)
VALUES ('ShouhizeiShousai','消費税詳細','1','1','1','1','1','') ,
 ('InvoiceSeidoKaishiSettei','インボイス制度開始設定','0','1','0','1','0','') ,
 ('ShouhizeiKbnSentaku','消費税区分選択','1','1','1','1','1','') ON CONFLICT DO NOTHING;

-- 仕訳抽出(SIAS)テーブルに(借方/貸方/税対象科目)併用売上税額計算方式カラム、仕入税額控除経過措置割合カラム、を追加
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS rurizeikeisan character varying(1) default 0;
comment on column shiwake_sias.rurizeikeisan is '（オープン２１）借方　併用売上税額計算方式';
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS surizeikeisan character varying(1) default 0;
comment on column shiwake_sias.surizeikeisan is '（オープン２１）貸方　併用売上税額計算方式';
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS zurizeikeisan character varying(1) default 0;
comment on column shiwake_sias.zurizeikeisan is '（オープン２１）税対象科目　併用売上税額計算方式';
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS rmenzeikeika character varying(1) default 0;
comment on column shiwake_sias.rmenzeikeika is '（オープン２１）借方　仕入税額控除経過措置割合';
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS smenzeikeika character varying(1) default 0;
comment on column shiwake_sias.smenzeikeika is '（オープン２１）貸方　仕入税額控除経過措置割合';
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS zmenzeikeika character varying(1) default 0;
comment on column shiwake_sias.zmenzeikeika is '（オープン２１）税対象科目　仕入税額控除経過措置割合';

-- 仕訳抽出(SIAS)(shiwake_sias)の摘要カラムの制限を120に変更
ALTER TABLE shiwake_sias ALTER COLUMN rtky TYPE character varying(120);
ALTER TABLE shiwake_sias ALTER COLUMN stky TYPE character varying(120);

-- 仕訳抽出(de3)テーブルに(借方/貸方/税対象科目)併用売上税額計算方式カラム、仕入税額控除経過措置割合カラム、を追加
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS rurizeikeisan character varying(1) default 0;
comment on column shiwake_de3.rurizeikeisan is '（オープン２１）借方　併用売上税額計算方式';
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS surizeikeisan character varying(1) default 0;
comment on column shiwake_de3.surizeikeisan is '（オープン２１）貸方　併用売上税額計算方式';
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS zurizeikeisan character varying(1) default 0;
comment on column shiwake_de3.zurizeikeisan is '（オープン２１）税対象科目　併用売上税額計算方式';
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS rmenzeikeika character varying(1) default 0;
comment on column shiwake_de3.rmenzeikeika is '（オープン２１）借方　仕入税額控除経過措置割合';
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS smenzeikeika character varying(1) default 0;
comment on column shiwake_de3.smenzeikeika is '（オープン２１）貸方　仕入税額控除経過措置割合';
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS zmenzeikeika character varying(1) default 0;
comment on column shiwake_de3.zmenzeikeika is '（オープン２１）税対象科目　仕入税額控除経過措置割合';

-- 画面項目制御
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
-- ▼▼K-133 ここから
-- アップデート前に項目値がデフォルトから未変更の項目は、項目名をデフォルト項目名と同じにする
UPDATE gamen_koumoku_seigyo new SET
  koumoku_name = default_koumoku_name
WHERE
  (new.denpyou_kbn,new.koumoku_id) IN (SELECT denpyou_kbn,koumoku_id FROM gamen_koumoku_seigyo_tmp WHERE default_koumoku_name != '' AND default_koumoku_name = koumoku_name AND denpyou_kbn = 'Z001');
-- ▲▲ここまで
DROP TABLE gamen_koumoku_seigyo_tmp;

-- shiwake_pattern_settingへの分離・仕入区分追加
-- 一旦課税区分とそろえている
-- 洗い替え回避のため、機械的にINSERT ON CONFLICT DO NOTHING文として処理
INSERT INTO shiwake_pattern_setting
(denpyou_kbn, setting_kbn, setting_item, default_value, hyouji_flg, shiwake_pattern_var1, shiwake_pattern_var2, shiwake_pattern_var3, shiwake_pattern_var4, shiwake_pattern_var5)
VALUES
 ('A001','2','BUNRIKBN','001','1','','','','',''),
 ('A001','2','KOBETSUKBN','001','1','','','','',''),
 ('A001','3','BUNRIKBN','','1','','','','',''),
 ('A001','3','KOBETSUKBN','','1','','','','',''),
 ('A001','4','BUNRIKBN','','1','','','','',''),
 ('A001','4','KOBETSUKBN','','1','','','','',''),
 ('A001','5','BUNRIKBN','','1','','','','',''),
 ('A001','5','KOBETSUKBN','','1','','','','',''),
 ('A001','6','BUNRIKBN','','1','','','','',''),
 ('A001','6','KOBETSUKBN','','1','','','','',''),
 ('A001','7','BUNRIKBN','','1','','','','',''),
 ('A001','7','KOBETSUKBN','','1','','','','',''),
 ('A002','2','BUNRIKBN','','1','','','','',''),
 ('A002','2','KOBETSUKBN','','1','','','','',''),
 ('A002','3','BUNRIKBN','','1','','','','',''),
 ('A002','3','KOBETSUKBN','','1','','','','',''),
 ('A002','4','BUNRIKBN','','1','','','','',''),
 ('A002','4','KOBETSUKBN','','1','','','','',''),
 ('A002','5','BUNRIKBN','','0','','','','',''),
 ('A002','5','KOBETSUKBN','','0','','','','',''),
 ('A002','6','BUNRIKBN','','0','','','','',''),
 ('A002','6','KOBETSUKBN','','0','','','','',''),
 ('A002','7','BUNRIKBN','','0','','','','',''),
 ('A002','7','KOBETSUKBN','','0','','','','',''),
 ('A003','2','BUNRIKBN','001','1','','','','',''),
 ('A003','2','KOBETSUKBN','001','1','','','','',''),
 ('A003','3','BUNRIKBN','','1','','','','',''),
 ('A003','3','KOBETSUKBN','','1','','','','',''),
 ('A003','4','BUNRIKBN','','1','','','','',''),
 ('A003','4','KOBETSUKBN','','1','','','','',''),
 ('A003','5','BUNRIKBN','','0','','','','',''),
 ('A003','5','KOBETSUKBN','','0','','','','',''),
 ('A003','6','BUNRIKBN','','0','','','','',''),
 ('A003','6','KOBETSUKBN','','0','','','','',''),
 ('A003','7','BUNRIKBN','','0','','','','',''),
 ('A003','7','KOBETSUKBN','','0','','','','',''),
 ('A004','2','BUNRIKBN','001','1','','','','',''),
 ('A004','2','KOBETSUKBN','001','1','','','','',''),
 ('A004','3','BUNRIKBN','','1','','','','',''),
 ('A004','3','KOBETSUKBN','','1','','','','',''),
 ('A004','4','BUNRIKBN','','1','','','','',''),
 ('A004','4','KOBETSUKBN','','1','','','','',''),
 ('A004','5','BUNRIKBN','','1','','','','',''),
 ('A004','5','KOBETSUKBN','','1','','','','',''),
 ('A004','6','BUNRIKBN','','1','','','','',''),
 ('A004','6','KOBETSUKBN','','1','','','','',''),
 ('A004','7','BUNRIKBN','','1','','','','',''),
 ('A004','7','KOBETSUKBN','','1','','','','',''),
 ('A005','2','BUNRIKBN','','1','','','','',''),
 ('A005','2','KOBETSUKBN','','1','','','','',''),
 ('A005','3','BUNRIKBN','','1','','','','',''),
 ('A005','3','KOBETSUKBN','','1','','','','',''),
 ('A005','4','BUNRIKBN','','1','','','','',''),
 ('A005','4','KOBETSUKBN','','1','','','','',''),
 ('A005','5','BUNRIKBN','','0','','','','',''),
 ('A005','5','KOBETSUKBN','','0','','','','',''),
 ('A005','6','BUNRIKBN','','0','','','','',''),
 ('A005','6','KOBETSUKBN','','0','','','','',''),
 ('A005','7','BUNRIKBN','','0','','','','',''),
 ('A005','7','KOBETSUKBN','','0','','','','',''),
 ('A006','2','BUNRIKBN','001','1','','','','',''),
 ('A006','2','KOBETSUKBN','001','1','','','','',''),
 ('A006','3','BUNRIKBN','','1','','','','',''),
 ('A006','3','KOBETSUKBN','','1','','','','',''),
 ('A006','4','BUNRIKBN','','0','','','','',''),
 ('A006','4','KOBETSUKBN','','0','','','','',''),
 ('A006','5','BUNRIKBN','','0','','','','',''),
 ('A006','5','KOBETSUKBN','','0','','','','',''),
 ('A006','6','BUNRIKBN','','0','','','','',''),
 ('A006','6','KOBETSUKBN','','0','','','','',''),
 ('A006','7','BUNRIKBN','','0','','','','',''),
 ('A006','7','KOBETSUKBN','','0','','','','',''),
 ('A009','2','BUNRIKBN','001','1','','','','',''),
 ('A009','2','KOBETSUKBN','001','1','','','','',''),
 ('A009','3','BUNRIKBN','','1','','','','',''),
 ('A009','3','KOBETSUKBN','','1','','','','',''),
 ('A009','4','BUNRIKBN','','1','','','','',''),
 ('A009','4','KOBETSUKBN','','1','','','','',''),
 ('A009','5','BUNRIKBN','','0','','','','',''),
 ('A009','5','KOBETSUKBN','','0','','','','',''),
 ('A009','6','BUNRIKBN','','0','','','','',''),
 ('A009','6','KOBETSUKBN','','0','','','','',''),
 ('A009','7','BUNRIKBN','','0','','','','',''),
 ('A009','7','KOBETSUKBN','','0','','','','',''),
 ('A010','2','BUNRIKBN','001','1','','','','',''),
 ('A010','2','KOBETSUKBN','001','1','','','','',''),
 ('A010','3','BUNRIKBN','','1','','','','',''),
 ('A010','3','KOBETSUKBN','','1','','','','',''),
 ('A010','4','BUNRIKBN','','1','','','','',''),
 ('A010','4','KOBETSUKBN','','1','','','','',''),
 ('A010','5','BUNRIKBN','','1','','','','',''),
 ('A010','5','KOBETSUKBN','','1','','','','',''),
 ('A010','6','BUNRIKBN','','1','','','','',''),
 ('A010','6','KOBETSUKBN','','1','','','','',''),
 ('A010','7','BUNRIKBN','','1','','','','',''),
 ('A010','7','KOBETSUKBN','','1','','','','',''),
 ('A011','2','BUNRIKBN','001','1','','','','',''),
 ('A011','2','KOBETSUKBN','001','1','','','','',''),
 ('A011','3','BUNRIKBN','','1','','','','',''),
 ('A011','3','KOBETSUKBN','','1','','','','',''),
 ('A011','4','BUNRIKBN','','1','','','','',''),
 ('A011','4','KOBETSUKBN','','1','','','','',''),
 ('A011','5','BUNRIKBN','','1','','','','',''),
 ('A011','5','KOBETSUKBN','','1','','','','',''),
 ('A011','6','BUNRIKBN','','1','','','','',''),
 ('A011','6','KOBETSUKBN','','1','','','','',''),
 ('A011','7','BUNRIKBN','','1','','','','',''),
 ('A011','7','KOBETSUKBN','','1','','','','',''),
 ('A012','2','BUNRIKBN','','1','','','','',''),
 ('A012','2','KOBETSUKBN','','1','','','','',''),
 ('A012','3','BUNRIKBN','','1','','','','',''),
 ('A012','3','KOBETSUKBN','','1','','','','',''),
 ('A012','4','BUNRIKBN','','1','','','','',''),
 ('A012','4','KOBETSUKBN','','1','','','','',''),
 ('A012','5','BUNRIKBN','','0','','','','',''),
 ('A012','5','KOBETSUKBN','','0','','','','',''),
 ('A012','6','BUNRIKBN','','0','','','','',''),
 ('A012','6','KOBETSUKBN','','0','','','','',''),
 ('A012','7','BUNRIKBN','','0','','','','',''),
 ('A012','7','KOBETSUKBN','','0','','','','',''),
 ('A901','2','BUNRIKBN','001','1','','','','',''),
 ('A901','2','KOBETSUKBN','001','1','','','','',''),
 ('A901','3','BUNRIKBN','','1','','','','',''),
 ('A901','3','KOBETSUKBN','','1','','','','',''),
 ('A901','4','BUNRIKBN','','1','','','','',''),
 ('A901','4','KOBETSUKBN','','1','','','','',''),
 ('A901','5','BUNRIKBN','','1','','','','',''),
 ('A901','5','KOBETSUKBN','','1','','','','',''),
 ('A901','6','BUNRIKBN','','1','','','','',''),
 ('A901','6','KOBETSUKBN','','1','','','','',''),
 ('A901','7','BUNRIKBN','','1','','','','',''),
 ('A901','7','KOBETSUKBN','','1','','','','',''),
 ('A013','2','BUNRIKBN','001','1','','','','',''),
 ('A013','2','KOBETSUKBN','001','1','','','','',''),
 ('A013','3','BUNRIKBN','','1','','','','',''),
 ('A013','3','KOBETSUKBN','','1','','','','',''),
 ('A013','4','BUNRIKBN','','1','','','','',''),
 ('A013','4','KOBETSUKBN','','1','','','','',''),
 ('A013','5','BUNRIKBN','','1','','','','',''),
 ('A013','5','KOBETSUKBN','','1','','','','',''),
 ('A013','6','BUNRIKBN','','0','','','','',''),
 ('A013','6','KOBETSUKBN','','0','','','','',''),
 ('A013','7','BUNRIKBN','','0','','','','',''),
 ('A013','7','KOBETSUKBN','','0','','','','','')
ON CONFLICT DO NOTHING;

commit;
