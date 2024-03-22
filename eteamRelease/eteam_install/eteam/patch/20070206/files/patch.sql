SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--�����R�[�h�ݒ�
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
-- Web���_���͂̓����R�[�h�͐􂢑ւ��ΏۊO
DELETE FROM naibu_cd_setting 
WHERE naibu_cd_name NOT IN ('bunri_kbn','shiire_kbn','kazei_kbn_kyoten_furikae','shiwake_pattern_denpyou_kbn_kyoten','shiwake_pattern_setting_kbn_kyoten','fusen_color');
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- �x���˗��\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- ���������\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

--���[���ݒ�
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
comment on table mail_settei is '���[���ݒ�';
comment on column mail_settei.smtp_server_name is 'SMTP�T�[�o�[��';
comment on column mail_settei.port_no is '�|�[�g�ԍ�';
comment on column mail_settei.ninshou_houhou is '�F�ؕ��@';
comment on column mail_settei.angouka_houhou is '�Í������@';
comment on column mail_settei.mail_address is '���[���A�h���X';
comment on column mail_settei.mail_id is '���[��ID';
comment on column mail_settei.mail_password is '���[���p�X���[�h';
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
