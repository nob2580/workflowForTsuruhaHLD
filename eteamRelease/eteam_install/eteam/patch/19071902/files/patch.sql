SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- 連絡票No824
-- ④科目が選択された時点でデフォルトの課税区分・税率・分離区分が表示されるのは良いのですが、　未払金のような消費税対象外の科目の場合は、課税区分・税率・分離区分はスペースでdisableとしてください。
-- ⑤仮受・仮払消費税以外の科目に税額対象科目は不要なので、disableになっていますが、税額税区分も不要なので、スペースでdisableにしてください。
--内部コード ※オプション用レコードはパッチ前になければ削除
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- 支払依頼申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- 請求書払申請がVUP前に設定されていなかったら必要ないので削除
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;


commit;
