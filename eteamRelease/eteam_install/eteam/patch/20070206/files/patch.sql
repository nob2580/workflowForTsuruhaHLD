SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

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

--メール設定
ALTER TABLE mail_settei rename to mail_settei_old;
create table mail_settei (
  smtp_server_name character varying not null
  , port_no character varying not null
  , ninshou_houhou character varying(2) not null
  , angouka_houhou character varying not null
  , mail_address character varying(50) not null
  , mail_id character varying not null
  , mail_password character varying not null
);
comment on table mail_settei is 'メール設定';
comment on column mail_settei.smtp_server_name is 'SMTPサーバー名';
comment on column mail_settei.port_no is 'ポート番号';
comment on column mail_settei.ninshou_houhou is '認証方法';
comment on column mail_settei.angouka_houhou is '暗号化方法';
comment on column mail_settei.mail_address is 'メールアドレス';
comment on column mail_settei.mail_id is 'メールID';
comment on column mail_settei.mail_password is 'メールパスワード';
INSERT INTO mail_settei
SELECT
  smtp_server_name 
  , port_no 
  , ninshou_houhou 
  , 'NO'	--angouka_houhou
  , mail_address 
  , mail_id 
  , mail_password 
FROM mail_settei_old;
DROP TABLE mail_settei_old;


commit;
