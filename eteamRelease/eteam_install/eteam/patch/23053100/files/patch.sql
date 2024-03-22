SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

--�}�X�^�[��荞�݈ꗗ�֌W
DELETE FROM master_torikomi_ichiran_de3 WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
DELETE FROM master_torikomi_ichiran_sias WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
DELETE FROM master_torikomi_ichiran_mk2 WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
\copy master_torikomi_ichiran_de3 FROM '.\files\csv\master_torikomi_ichiran_de3_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_sias FROM '.\files\csv\master_torikomi_ichiran_sias_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_ichiran_mk2 FROM '.\files\csv\master_torikomi_ichiran_mk2_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--�}�X�^�[��荞�ݏڍ׊֌W
DELETE FROM master_torikomi_shousai_de3 WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
DELETE FROM master_torikomi_shousai_sias WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
DELETE FROM master_torikomi_shousai_mk2 WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
\copy master_torikomi_shousai_de3 FROM '.\files\csv\master_torikomi_shousai_de3_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_sias FROM '.\files\csv\master_torikomi_shousai_sias_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy master_torikomi_shousai_mk2 FROM '.\files\csv\master_torikomi_shousai_mk2_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--�}�X�^�[�Ǘ��ꗗ�f�[�^�ǉ�
DELETE FROM master_kanri_ichiran WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
\copy master_kanri_ichiran FROM '.\files\csv\master_kanri_ichiran_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

-- �}�X�^�[�Ǘ��Ő��̃f�[�^�ǉ�
DELETE FROM master_kanri_hansuu WHERE master_id = 'kamoku_edaban_zandaka' OR master_id = 'torihikisaki_master' OR master_id = 'bumon_master' OR master_id = 'ki_shouhizei_setting';
\copy master_kanri_hansuu FROM '.\files\csv\master_kanri_hansuu_patch.csv' WITH CSV header ENCODING 'SHIFT-JIS';

--�����R�[�h�ݒ�
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting WHERE naibu_cd_name NOT IN ('kazei_kbn_kyoten_furikae', 'shiwake_pattern_denpyou_kbn_kyoten', 'fusen_color', 'ebunsho_shubetsu_kyoten');
\copy naibu_cd_setting FROM '..\..\work\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- �x���˗��\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- ���������\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;

-- �ݒ���f�[�^�ύX ��hyouji_jun900�ȍ~�̓J�X�^�}�C�Y�̈�A1000�Ԉȍ~�͋��_���͌����̈�
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

-- invoice�J�����ǉ�
-- ���S����e�[�u���Ɏd���敪�J�����A���͊J�n���J�����A���͏I�����J������ǉ�
ALTER TABLE bumon_master ADD COLUMN IF NOT EXISTS shiire_kbn smallint;
comment on column bumon_master.shiire_kbn is '�d���敪';
ALTER TABLE bumon_master ADD COLUMN IF NOT EXISTS nyuryoku_from_date date;
comment on column bumon_master.nyuryoku_from_date is '���͊J�n��';
ALTER TABLE bumon_master ADD COLUMN IF NOT EXISTS nyuryoku_to_date date;
comment on column bumon_master.nyuryoku_to_date is '���͏I����';

-- �`�[�ꗗ�e�[�u���Ɏ��Ǝҋ敪�J�����A�Ј��ԍ��i�N�[�ҁj�J�����A�x���於(�ꗗ�����p)�J�����A�Ŕ����׋��z(�ꗗ�����p)�J�����A�Ŕ����z�J������ǉ�
ALTER TABLE denpyou_ichiran ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying[];
comment on column denpyou_ichiran.jigyousha_kbn is '���Ǝҋ敪';
ALTER TABLE denpyou_ichiran ADD COLUMN IF NOT EXISTS shain_no character varying(15);
comment on column denpyou_ichiran.shain_no is '�Ј��ԍ��i�N�[�ҁj';
ALTER TABLE denpyou_ichiran ADD COLUMN IF NOT EXISTS shiharai_name character varying[];
comment on column denpyou_ichiran.shiharai_name is '�x���於(�ꗗ�����p)';
ALTER TABLE denpyou_ichiran ADD COLUMN IF NOT EXISTS zeinuki_meisai_kingaku numeric[];
comment on column denpyou_ichiran.zeinuki_meisai_kingaku is '�Ŕ����׋��z(�ꗗ�����p)';
ALTER TABLE denpyou_ichiran ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column denpyou_ichiran.zeinuki_kingaku is '�Ŕ����z';

-- �U�փe�[�u���Ɏؕ������敪�J�����A�ݕ������敪�J�����A�ؕ����Ǝҋ敪�J�����A�ؕ��d���敪�J�����A�ؕ��Ŋz�v�Z�����J�����A�ݕ����Ǝҋ敪�J�����A�ݕ��d���敪�J�����A�ݕ��Ŋz�v�Z�����J�����A�C���{�C�X�Ή��`�[�J������ǉ�
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kari_bunri_kbn character varying(1);
comment on column furikae.kari_bunri_kbn is '�ؕ������敪';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kashi_bunri_kbn character varying(1);
comment on column furikae.kashi_bunri_kbn is '�ݕ������敪';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kari_jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column furikae.kari_jigyousha_kbn is '�ؕ����Ǝҋ敪';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column furikae.kari_shiire_kbn is '�ؕ��d���敪';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kari_zeigaku_houshiki character varying(1) NOT NULL default '0';
comment on column furikae.kari_zeigaku_houshiki is '�ؕ��Ŋz�v�Z����';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kashi_jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column furikae.kashi_jigyousha_kbn is '�ݕ����Ǝҋ敪';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column furikae.kashi_shiire_kbn is '�ݕ��d���敪';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kashi_zeigaku_houshiki character varying(1) NOT NULL default '0';
comment on column furikae.kashi_zeigaku_houshiki is '�ݕ��Ŋz�v�Z����';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column furikae.invoice_denpyou is '�C���{�C�X�Ή��`�[';

-- �C���{�C�X�J�n�e�[�u���̍쐬
create table if not exists invoice_start (
  invoice_flg character varying(1) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp(6) without time zone not null
  );
comment on table invoice_start is '�C���{�C�X�J�n';
comment on column invoice_start.invoice_flg is '�C���{�C�X�J�n�t���O';
comment on column invoice_start.touroku_user_id is '�o�^���[�U�[ID';
comment on column invoice_start.touroku_time is '�o�^����';

--�U�փe�[�u���̓E�v�J�����̐�����120�ɕύX
ALTER TABLE furikae ALTER COLUMN tekiyou TYPE character varying(120);

-- ���������e�[�u���ɓ��͕����J�����A�C���{�C�X�Ή��`�[�J������ǉ�
ALTER TABLE jidouhikiotoshi ADD COLUMN IF NOT EXISTS nyuryoku_houshiki character varying(1) NOT NULL default '0';
comment on column jidouhikiotoshi.nyuryoku_houshiki is '���͕���';
ALTER TABLE jidouhikiotoshi ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column jidouhikiotoshi.invoice_denpyou is '�C���{�C�X�Ή��`�[';

-- �����������׃e�[�u���Ɏ��Ǝҋ敪�J�����A�����敪�J�����A�ؕ��d���敪�J�����A�ݕ��d���敪�J�����A�C���{�C�X�Ή��`�[�J������ǉ�
ALTER TABLE jidouhikiotoshi_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column jidouhikiotoshi_meisai.jigyousha_kbn is '���Ǝҋ敪';
ALTER TABLE jidouhikiotoshi_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column jidouhikiotoshi_meisai.bunri_kbn is '�����敪';
ALTER TABLE jidouhikiotoshi_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column jidouhikiotoshi_meisai.kari_shiire_kbn is '�ؕ��d���敪';
ALTER TABLE jidouhikiotoshi_meisai ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column jidouhikiotoshi_meisai.kashi_shiire_kbn is '�ݕ��d���敪';

--�����������׃e�[�u���̓E�v�J�����̐�����120�ɕύX
ALTER TABLE jidouhikiotoshi_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- �C�O����Z�e�[�u���ɕ����敪�J�����A�ؕ��d���敪�J�����A�ݕ��d���敪�J�����A�C�O�����敪�J�����A�C�O�ؕ��d���敪�J�����A�C�O�ݕ��d���敪�J�����A�C���{�C�X�Ή��`�[�J������ǉ�
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column kaigai_ryohiseisan.bunri_kbn is '�����敪';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan.kari_shiire_kbn is '�ؕ��d���敪';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan.kashi_shiire_kbn is '�ݕ��d���敪';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS kaigai_bunri_kbn character varying(1);
comment on column kaigai_ryohiseisan.kaigai_bunri_kbn is '�C�O�����敪';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS kaigai_kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan.kaigai_kari_shiire_kbn is '�C�O�ؕ��d���敪';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS kaigai_kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan.kaigai_kashi_shiire_kbn is '�C�O�ݕ��d���敪';
ALTER TABLE kaigai_ryohiseisan ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column kaigai_ryohiseisan.invoice_denpyou is '�C���{�C�X�Ή��`�[';

--�C�O����Z�e�[�u���̓E�v�E�C�O�E�v�J�����̐�����120�ɕύX
ALTER TABLE kaigai_ryohiseisan ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE kaigai_ryohiseisan ALTER COLUMN kaigai_tekiyou TYPE character varying(120);

-- �C�O����Z�o��׃e�[�u���Ɏx���於�J�����A���Ǝҋ敪�J�����A�����敪�J�����A�ؕ��d���敪�J�����A�ݕ��d���敪�J������ǉ�
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column kaigai_ryohiseisan_keihi_meisai.shiharaisaki_name is '�x���於';
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column kaigai_ryohiseisan_keihi_meisai.jigyousha_kbn is '���Ǝҋ敪';
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column kaigai_ryohiseisan_keihi_meisai.bunri_kbn is '�����敪';
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_shiire_kbn is '�ؕ��d���敪';
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_shiire_kbn is '�ݕ��d���敪';

--�C�O����Z�o��׃e�[�u���̓E�v�E�C�O�E�v�J�����̐�����120�ɕύX
ALTER TABLE kaigai_ryohiseisan_keihi_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- �C�O����Z���׃e�[�u���Ɏx���於�J�����A���Ǝҋ敪�J�����A�Ŕ����z�J�����A����Ŋz�J������ǉ�
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column kaigai_ryohiseisan_meisai.shiharaisaki_name is '�x���於';
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column kaigai_ryohiseisan_meisai.jigyousha_kbn is '���Ǝҋ敪';
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column kaigai_ryohiseisan_meisai.zeinuki_kingaku is '�Ŕ����z';
ALTER TABLE kaigai_ryohiseisan_meisai ADD COLUMN IF NOT EXISTS shouhizeigaku numeric(15) NOT NULL default 0;
comment on column kaigai_ryohiseisan_meisai.shouhizeigaku is '����Ŋz';

-- �ȑO���C�O����Z�o��׃e�[�u���̖{�̋��z�A����Ŋz��null���i�[����Ă��邽�߁u0�v���Z�b�g
UPDATE kaigai_ryohiseisan_keihi_meisai SET hontai_kingaku = 0, shouhizeigaku = 0;

-- ����n�E�����n�`�[�̓E�v�J�����̐�����120�ɕύX
ALTER TABLE ryohi_karibarai ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE ryohiseisan_keihi_meisai ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE ryohi_karibarai_keihi_meisai ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE kaigai_ryohi_karibarai ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE kaigai_ryohi_karibarai_keihi_meisai ALTER COLUMN tekiyou TYPE character varying(120);
ALTER TABLE karibarai ALTER COLUMN tekiyou TYPE character varying(120);

-- �Ȗڎ}�Ԏc���e�[�u���ɉېŋ敪�J�����A�����敪�J������ǉ�
ALTER TABLE kamoku_edaban_zandaka ADD COLUMN IF NOT EXISTS kazei_kbn smallint;
comment on column kamoku_edaban_zandaka.kazei_kbn is '�ېŋ敪';
ALTER TABLE kamoku_edaban_zandaka ADD COLUMN IF NOT EXISTS bunri_kbn smallint;
comment on column kamoku_edaban_zandaka.bunri_kbn is '�����敪';

-- �o��Z�e�[�u���ɃC���{�C�X�Ή��`�[�J������ǉ�
ALTER TABLE keihiseisan ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column keihiseisan.invoice_denpyou is '�C���{�C�X�Ή��`�[';

-- �o��Z���׃e�[�u���Ɏ��Ǝҋ敪�J�����A�x���於�J�����A�����敪�J�����A�ؕ��d���敪�J�����A�ݕ��d���敪�J������ǉ�
ALTER TABLE keihiseisan_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column keihiseisan_meisai.jigyousha_kbn is '���Ǝҋ敪';
ALTER TABLE keihiseisan_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column keihiseisan_meisai.shiharaisaki_name is '�x���於';
ALTER TABLE keihiseisan_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column keihiseisan_meisai.bunri_kbn is '�����敪';
ALTER TABLE keihiseisan_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column keihiseisan_meisai.kari_shiire_kbn is '�ؕ��d���敪';
ALTER TABLE keihiseisan_meisai ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column keihiseisan_meisai.kashi_shiire_kbn is '�ݕ��d���敪';

--�o��Z���׃e�[�u���̓E�v�J�����̐�����120�ɕύX
ALTER TABLE keihiseisan_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- (����)�����d��(ki_shiwake)�̓E�v�J�����̐�����120�ɕύX
ALTER TABLE ki_shiwake ALTER COLUMN rtky TYPE character varying(120);
ALTER TABLE ki_shiwake ALTER COLUMN stky TYPE character varying(120);

-- �i���ʁj����e�[�u���Ɏd���敪�J�����A���͊J�n���J�����A���͏I�����J������ǉ�
ALTER TABLE ki_bumon ADD COLUMN IF NOT EXISTS shiire_kbn smallint;
comment on column ki_bumon.shiire_kbn is '�d���敪';
ALTER TABLE ki_bumon ADD COLUMN IF NOT EXISTS nyuryoku_from_date date;
comment on column ki_bumon.nyuryoku_from_date is '���͊J�n��';
ALTER TABLE ki_bumon ADD COLUMN IF NOT EXISTS nyuryoku_to_date date;
comment on column ki_bumon.nyuryoku_to_date is '���͏I����';

-- �i���ʁj����Őݒ�e�[�u���̍쐬
-- ���_���g�p�̏ꍇ�͐V�K�e�[�u���ɂȂ�
-- �]���̃J�����̂ݐ�o���i��Œǉ������̂ŃN���[���R�[�h����j
create table if not exists ki_shouhizei_setting (
  kesn smallint not null,
  shiire_zeigaku_anbun_flg smallint not null,
  shouhizei_kbn smallint not null,
  hasuu_shori_flg smallint not null,
  zeigaku_keisan_flg smallint not null,
  shouhizeitaishou_minyuryoku_flg smallint not null,
  constraint ki_shouhizei_setting_pkey PRIMARY KEY (kesn)
);

-- �R�����g�X�V�̓J�����ǉ��Ƃ͕ʂɍs����̂ŏ]�����̂ݐ�o��
comment on table ki_shouhizei_setting is '�i���ʁj����Őݒ�';
comment on column ki_shouhizei_setting.kesn is '�������Z��';
comment on column ki_shouhizei_setting.shiire_zeigaku_anbun_flg is '�d���Ŋz���t���O';
comment on column ki_shouhizei_setting.shouhizei_kbn is '����ŋ敪';
comment on column ki_shouhizei_setting.hasuu_shori_flg is '�[�������t���O';
comment on column ki_shouhizei_setting.zeigaku_keisan_flg is '�Ŋz�v�Z�t���O';
comment on column ki_shouhizei_setting.shouhizeitaishou_minyuryoku_flg is '����őΏۉȖږ����̓t���O';

-- �i���ʁj����Őݒ�e�[�u���Ɏ��Y�J�����A����J�����A�d���J�����A�o��J�����A����ʏ����J�����A����d��������J�����A0�~����ō쐬�J�����A�����E�������Ł@����J�����A�����J�����A�}�ԃJ�����A�v���W�F�N�g�J�����A�Z�O�����g�J�����A���j�o�[�T���P�J�����A���j�o�[�T���Q�J�����A���j�o�[�T���R�J�����A�H���J�����A�H��J�����A�������Ł@����J�����A���Y�J�����A��������Ł@�d���J�����A�o��J�����A���Y�J�����A����Ŋz�v�Z�����J�����A�d���Ŋz�v�Z�����J�����A�d���Ŋz�T���o�ߑ[�u�K�p�J������ǉ�
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shisan smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shisan is '���Y';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS uriage smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.uriage is '����';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shiire smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shiire is '�d��';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS keihi smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.keihi is '�o��';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS bumonbetsu_shori smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.bumonbetsu_shori is '����ʏ���';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS tokuteishiire smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.tokuteishiire is '����d�������';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS zero_shouhizei smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.zero_shouhizei is '0�~����ō쐬';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_bumon smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_bumon is '�����E�������Ł@����';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_torihikisaki smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_torihikisaki is '�����';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_edaban smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_edaban is '�}��';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_project smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_project is '�v���W�F�N�g';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_segment smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_segment is '�Z�O�����g';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_uf1 smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_uf1 is '���j�o�[�T���P';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_uf2 smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_uf2 is '���j�o�[�T���Q';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_uf3 smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_uf3 is '���j�o�[�T���R';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_kouji smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_kouji is '�H��';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shouhizei_koushu smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shouhizei_koushu is '�H��';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS ukeshouhizei_uriage character varying(15) NOT NULL default '';
comment on column ki_shouhizei_setting.ukeshouhizei_uriage is '�������Ł@����';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS ukeshouhizei_shisan character varying(15) NOT NULL default '';
comment on column ki_shouhizei_setting.ukeshouhizei_shisan is '���Y';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS haraishouhizei_shiire character varying(15) NOT NULL default '';
comment on column ki_shouhizei_setting.haraishouhizei_shiire is '��������Ł@�d��';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS haraishouhizei_keihi character varying(15) NOT NULL default '';
comment on column ki_shouhizei_setting.haraishouhizei_keihi is '�o��';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS haraisyouhizei_shisan character varying(15) NOT NULL default '';
comment on column ki_shouhizei_setting.haraisyouhizei_shisan is '���Y';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS uriagezeigaku_keisan smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.uriagezeigaku_keisan is '����Ŋz�v�Z����';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shiirezeigaku_keisan smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shiirezeigaku_keisan is '�d���Ŋz�v�Z����';
ALTER TABLE ki_shouhizei_setting ADD COLUMN IF NOT EXISTS shiirezeigaku_keikasothi smallint NOT NULL default 0;
comment on column ki_shouhizei_setting.shiirezeigaku_keikasothi is '�d���Ŋz�T���o�ߑ[�u�K�p';

-- ��ʔ�Z�e�[�u���ɕ����敪�J�����A�ؕ��d���敪�J�����A�ݕ��d���敪�J�����A�C���{�C�X�Ή��`�[�J������ǉ�
ALTER TABLE koutsuuhiseisan ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column koutsuuhiseisan.bunri_kbn is '�����敪';
ALTER TABLE koutsuuhiseisan ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column koutsuuhiseisan.kari_shiire_kbn is '�ؕ��d���敪';
ALTER TABLE koutsuuhiseisan ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column koutsuuhiseisan.kashi_shiire_kbn is '�ݕ��d���敪';
ALTER TABLE koutsuuhiseisan ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column koutsuuhiseisan.invoice_denpyou is '�C���{�C�X�Ή��`�[';

--��ʔ�Z�e�[�u���̓E�v�J�����̐�����120�ɕύX
ALTER TABLE koutsuuhiseisan ALTER COLUMN tekiyou TYPE character varying(120);

-- ��ʔ�Z���׃e�[�u���Ɏx���於�J�����A���Ǝҋ敪�J�����A�Ŕ����z�J�����A����Ŋz�J������ǉ�
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column koutsuuhiseisan_meisai.shiharaisaki_name is '�x���於';
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column koutsuuhiseisan_meisai.jigyousha_kbn is '���Ǝҋ敪';
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column koutsuuhiseisan_meisai.zeinuki_kingaku is '�Ŕ����z';
ALTER TABLE koutsuuhiseisan_meisai ADD COLUMN IF NOT EXISTS shouhizeigaku numeric(15) NOT NULL default 0;
comment on column koutsuuhiseisan_meisai.shouhizeigaku is '����Ŋz';

-- ����Z�e�[�u���ɕ����敪�J�����A�ؕ��d���敪�J�����A�ݕ��d���敪�J�����A�C���{�C�X�Ή��`�[�J������ǉ�
ALTER TABLE ryohiseisan ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column ryohiseisan.bunri_kbn is '�����敪';
ALTER TABLE ryohiseisan ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column ryohiseisan.kari_shiire_kbn is '�ؕ��d���敪';
ALTER TABLE ryohiseisan ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column ryohiseisan.kashi_shiire_kbn is '�ݕ��d���敪';
ALTER TABLE ryohiseisan ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column ryohiseisan.invoice_denpyou is '�C���{�C�X�Ή��`�[';

--����Z�e�[�u���̓E�v�J�����̐�����120�ɕύX
ALTER TABLE ryohiseisan ALTER COLUMN tekiyou TYPE character varying(120);

-- ����Z�o��׃e�[�u���Ɏx���於�J�����A���Ǝҋ敪�J�����A�����敪�J�����A�ؕ��d���敪�J�����A�ݕ��d���敪�J������ǉ�
ALTER TABLE ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column ryohiseisan_keihi_meisai.shiharaisaki_name is '�x���於';
ALTER TABLE ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column ryohiseisan_keihi_meisai.jigyousha_kbn is '���Ǝҋ敪';
ALTER TABLE ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column ryohiseisan_keihi_meisai.bunri_kbn is '�����敪';
ALTER TABLE ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column ryohiseisan_keihi_meisai.kari_shiire_kbn is '�ؕ��d���敪';
ALTER TABLE ryohiseisan_keihi_meisai ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column ryohiseisan_keihi_meisai.kashi_shiire_kbn is '�ݕ��d���敪';

-- ����Z���׃e�[�u���Ɏx���於�J�����A���Ǝҋ敪�J�����A�Ŕ����z�J�����A����Ŋz�J������ǉ�
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column ryohiseisan_meisai.shiharaisaki_name is '�x���於';
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column ryohiseisan_meisai.jigyousha_kbn is '���Ǝҋ敪';
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column ryohiseisan_meisai.zeinuki_kingaku is '�Ŕ����z';
ALTER TABLE ryohiseisan_meisai ADD COLUMN IF NOT EXISTS shouhizeigaku numeric(15) NOT NULL default 0;
comment on column ryohiseisan_meisai.shouhizeigaku is '����Ŋz';

-- �����������e�[�u���ɓ��͕����J�����A�C���{�C�X�Ή��`�[�J������ǉ�
ALTER TABLE seikyuushobarai ADD COLUMN IF NOT EXISTS nyuryoku_houshiki character varying(1) NOT NULL default '0';
comment on column seikyuushobarai.nyuryoku_houshiki is '���͕���';
ALTER TABLE seikyuushobarai ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column seikyuushobarai.invoice_denpyou is '�C���{�C�X�Ή��`�[';

-- �������������׃e�[�u���Ɏ��Ǝҋ敪�J�����A�����敪�J�����A�ؕ��d���敪�J�����A�ݕ��d���敪�J������ǉ�
ALTER TABLE seikyuushobarai_meisai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column seikyuushobarai_meisai.jigyousha_kbn is '���Ǝҋ敪';
ALTER TABLE seikyuushobarai_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column seikyuushobarai_meisai.bunri_kbn is '�����敪';
ALTER TABLE seikyuushobarai_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column seikyuushobarai_meisai.kari_shiire_kbn is '�ؕ��d���敪';
ALTER TABLE seikyuushobarai_meisai ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column seikyuushobarai_meisai.kashi_shiire_kbn is '�ݕ��d���敪';

--�������������׃e�[�u���̓E�v�J�����̐�����120�ɕύX
ALTER TABLE seikyuushobarai_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- �x���˗��e�[�u���ɓ��͕����J�����A���Ǝҋ敪�J�����A���Ǝғo�^�ԍ��J�����A�C���{�C�X�Ή��`�[�J������ǉ�
ALTER TABLE shiharai_irai ADD COLUMN IF NOT EXISTS nyuryoku_houshiki character varying(1) NOT NULL default '0';
comment on column shiharai_irai.nyuryoku_houshiki is '���͕���';
ALTER TABLE shiharai_irai ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(15) NOT NULL default '0';
comment on column shiharai_irai.jigyousha_kbn is '���Ǝҋ敪';
ALTER TABLE shiharai_irai ADD COLUMN IF NOT EXISTS jigyousha_no character varying(15) NOT NULL default '';
comment on column shiharai_irai.jigyousha_no is '���Ǝғo�^�ԍ�';
ALTER TABLE shiharai_irai ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column shiharai_irai.invoice_denpyou is '�C���{�C�X�Ή��`�[';

-- �x���˗����׃e�[�u���ɕ����敪�J�����A�ؕ��d���敪�J�����A�Ŕ����z�J�����A����Ŋz�J������ǉ�
ALTER TABLE shiharai_irai_meisai ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column shiharai_irai_meisai.bunri_kbn is '�����敪';
ALTER TABLE shiharai_irai_meisai ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column shiharai_irai_meisai.kari_shiire_kbn is '�ؕ��d���敪';
ALTER TABLE shiharai_irai_meisai ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column shiharai_irai_meisai.zeinuki_kingaku is '�Ŕ����z';
ALTER TABLE shiharai_irai_meisai ADD COLUMN IF NOT EXISTS shouhizeigaku numeric(15) NOT NULL default 0;
comment on column shiharai_irai_meisai.shouhizeigaku is '����Ŋz';

--�x���˗����׃e�[�u���̓E�v�J�����̐�����120�ɕύX
ALTER TABLE shiharai_irai_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- �d��p�^�[���}�X�^�[�e�[�u���ɋ��ؕ��ېŋ敪�i�d��p�^�[���j�J�����A���ݕ��ېŋ敪�P�i�d��p�^�[���j�J�����A���ݕ��ېŋ敪�Q�i�d��p�^�[���j�J�����A���ݕ��ېŋ敪�R�i�d��p�^�[���j�J�����A���ݕ��ېŋ敪�S�i�d��p�^�[���j�J�����A���ݕ��ېŋ敪�T�i�d��p�^�[���j�J�����A�ؕ������敪�i�d��p�^�[���j�J�����A�ݕ������敪�P�i�d��p�^�[���j�J�����A�ݕ������敪�Q�i�d��p�^�[���j�J�����A�ݕ������敪�R�i�d��p�^�[���j�J�����A�ݕ������敪�S�i�d��p�^�[���j�J�����A�ݕ������敪�T�i�d��p�^�[���j�J�����A�ؕ��d���敪�i�d��p�^�[���j�J�����A�ݕ��d���敪�P�i�d��p�^�[���j�J�����A�ݕ��d���敪�Q�i�d��p�^�[���j�J�����A�ݕ��d���敪�R�i�d��p�^�[���j�J�����A�ݕ��d���敪�S�i�d��p�^�[���j�J�����A�ݕ��d���敪�T�i�d��p�^�[���j�J������ǉ�
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kari_kazei_kbn character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kari_kazei_kbn is '���ؕ��ېŋ敪�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kashi_kazei_kbn1 character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn1 is '���ݕ��ېŋ敪�P�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kashi_kazei_kbn2 character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn2 is '���ݕ��ېŋ敪�Q�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kashi_kazei_kbn3 character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn3 is '���ݕ��ېŋ敪�R�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kashi_kazei_kbn4 character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn4 is '���ݕ��ېŋ敪�S�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS old_kashi_kazei_kbn5 character varying NOT NULL default '';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn5 is '���ݕ��ېŋ敪�T�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kari_bunri_kbn character varying(1);
comment on column shiwake_pattern_master.kari_bunri_kbn is '�ؕ������敪�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_bunri_kbn1 character varying(1);
comment on column shiwake_pattern_master.kashi_bunri_kbn1 is '�ݕ������敪�P�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_bunri_kbn2 character varying(1);
comment on column shiwake_pattern_master.kashi_bunri_kbn2 is '�ݕ������敪�Q�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_bunri_kbn3 character varying(1);
comment on column shiwake_pattern_master.kashi_bunri_kbn3 is '�ݕ������敪�R�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_bunri_kbn4 character varying(1);
comment on column shiwake_pattern_master.kashi_bunri_kbn4 is '�ݕ������敪�S�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_bunri_kbn5 character varying(1);
comment on column shiwake_pattern_master.kashi_bunri_kbn5 is '�ݕ������敪�T�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1);
comment on column shiwake_pattern_master.kari_shiire_kbn is '�ؕ��d���敪�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_shiire_kbn1 character varying(1);
comment on column shiwake_pattern_master.kashi_shiire_kbn1 is '�ݕ��d���敪�P�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_shiire_kbn2 character varying(1);
comment on column shiwake_pattern_master.kashi_shiire_kbn2 is '�ݕ��d���敪�Q�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_shiire_kbn3 character varying(1);
comment on column shiwake_pattern_master.kashi_shiire_kbn3 is '�ݕ��d���敪�R�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_shiire_kbn4 character varying(1);
comment on column shiwake_pattern_master.kashi_shiire_kbn4 is '�ݕ��d���敪�S�i�d��p�^�[���j';
ALTER TABLE shiwake_pattern_master ADD COLUMN IF NOT EXISTS kashi_shiire_kbn5 character varying(1);
comment on column shiwake_pattern_master.kashi_shiire_kbn5 is '�ݕ��d���敪�T�i�d��p�^�[���j';

-- �d��p�^�[���}�X�^�[�e�[�u���̋��ېŋ敪�i�ؕ��E�ݕ��P�`�T�j�Ɍ��݂̓o�^���e���R�s�[
UPDATE shiwake_pattern_master SET old_kari_kazei_kbn = kari_kazei_kbn;
UPDATE shiwake_pattern_master SET old_kashi_kazei_kbn1 = kashi_kazei_kbn1;
UPDATE shiwake_pattern_master SET old_kashi_kazei_kbn2 = kashi_kazei_kbn2;
UPDATE shiwake_pattern_master SET old_kashi_kazei_kbn3 = kashi_kazei_kbn3;
UPDATE shiwake_pattern_master SET old_kashi_kazei_kbn4 = kashi_kazei_kbn4;
UPDATE shiwake_pattern_master SET old_kashi_kazei_kbn5 = kashi_kazei_kbn5;

--�d��p�^�[���}�X�^�[�e�[�u���̓E�v�J�����̐�����60�ɕύX
ALTER TABLE shiwake_pattern_master ALTER COLUMN tekiyou TYPE character varying(60);

-- �����}�X�^�[�e�[�u���ɓ��͋��J�n�N�����J�����A���͋��I���N�����J�����A�K�i���������s���Ǝғo�^�ԍ��J�����A�ƐŎ��Ǝғ��t���O�J������ǉ�
ALTER TABLE torihikisaki_master ADD COLUMN IF NOT EXISTS nyuryoku_from_date date;
comment on column torihikisaki_master.nyuryoku_from_date is '���͋��J�n�N����';
ALTER TABLE torihikisaki_master ADD COLUMN IF NOT EXISTS nyuryoku_to_date date;
comment on column torihikisaki_master.nyuryoku_to_date is '���͋��I���N����';
ALTER TABLE torihikisaki_master ADD COLUMN IF NOT EXISTS tekikaku_no character varying(14);
comment on column torihikisaki_master.tekikaku_no is '�K�i���������s���Ǝғo�^�ԍ�';
ALTER TABLE torihikisaki_master ADD COLUMN IF NOT EXISTS menzei_jigyousha_flg character varying(1) NOT NULL default '0';
comment on column torihikisaki_master.menzei_jigyousha_flg is '�ƐŎ��Ǝғ��t���O';

-- �t�փe�[�u���ɕt�֌����Ǝҋ敪�J�����A�t�֌������敪�J�����A�t�֌��d���敪�J�����A�t�֌��Ŋz�v�Z�����J�����A�C���{�C�X�Ή��`�[�J������ǉ�
ALTER TABLE tsukekae ADD COLUMN IF NOT EXISTS moto_jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column tsukekae.moto_jigyousha_kbn is '�t�֌����Ǝҋ敪';
ALTER TABLE tsukekae ADD COLUMN IF NOT EXISTS moto_bunri_kbn character varying(1);
comment on column tsukekae.moto_bunri_kbn is '�t�֌������敪';
ALTER TABLE tsukekae ADD COLUMN IF NOT EXISTS moto_shiire_kbn character varying(1) NOT NULL default '';
comment on column tsukekae.moto_shiire_kbn is '�t�֌��d���敪';
ALTER TABLE tsukekae ADD COLUMN IF NOT EXISTS moto_zeigaku_houshiki character varying(1) NOT NULL default '0';
comment on column tsukekae.moto_zeigaku_houshiki is '�t�֌��Ŋz�v�Z����';
ALTER TABLE tsukekae ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column tsukekae.invoice_denpyou is '�C���{�C�X�Ή��`�[';

-- �t�֖��׃e�[�u���ɕt�֐掖�Ǝҋ敪�J�����A�t�֐敪���敪�J�����A�t�֐�d���敪�J�����A�t�֐�Ŋz�v�Z�����J������ǉ�
ALTER TABLE tsukekae_meisai ADD COLUMN IF NOT EXISTS saki_jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column tsukekae_meisai.saki_jigyousha_kbn is '�t�֐掖�Ǝҋ敪';
ALTER TABLE tsukekae_meisai ADD COLUMN IF NOT EXISTS saki_bunri_kbn character varying(1);
comment on column tsukekae_meisai.saki_bunri_kbn is '�t�֐敪���敪';
ALTER TABLE tsukekae_meisai ADD COLUMN IF NOT EXISTS saki_shiire_kbn character varying(1) NOT NULL default '';
comment on column tsukekae_meisai.saki_shiire_kbn is '�t�֐�d���敪';
ALTER TABLE tsukekae_meisai ADD COLUMN IF NOT EXISTS saki_zeigaku_houshiki character varying(1) NOT NULL default '0';
comment on column tsukekae_meisai.saki_zeigaku_houshiki is '�t�֐�Ŋz�v�Z����';

-- �t�֖��ׂ̓E�v�J�����̐�����120�ɕύX
ALTER TABLE tsukekae_meisai ALTER COLUMN tekiyou TYPE character varying(120);

-- �ʋΒ���e�[�u���ɐŔ����z�J�����A����Ŋz�J�����A���͕����J�����A�x���於�J�����A���Ǝҋ敪�J�����A�����敪�J�����A�ؕ��d���敪�J�����A�ݕ��d���敪�J�����A�C���{�C�X�Ή��`�[�J������ǉ�
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS zeinuki_kingaku numeric(15) NOT NULL default 0;
comment on column tsuukinteiki.zeinuki_kingaku is '�Ŕ����z';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS shouhizeigaku numeric(15) NOT NULL default 0;
comment on column tsuukinteiki.shouhizeigaku is '����Ŋz';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS shiharaisaki_name character varying(60);
comment on column tsuukinteiki.shiharaisaki_name is '�x���於';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS jigyousha_kbn character varying(1) NOT NULL default '0';
comment on column tsuukinteiki.jigyousha_kbn is '���Ǝҋ敪';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS bunri_kbn character varying(1);
comment on column tsuukinteiki.bunri_kbn is '�����敪';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS kari_shiire_kbn character varying(1) NOT NULL default '';
comment on column tsuukinteiki.kari_shiire_kbn is '�ؕ��d���敪';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS kashi_shiire_kbn character varying(1) NOT NULL default '';
comment on column tsuukinteiki.kashi_shiire_kbn is '�ݕ��d���敪';
ALTER TABLE tsuukinteiki ADD COLUMN IF NOT EXISTS invoice_denpyou character varying(1) NOT NULL default '1';
comment on column tsuukinteiki.invoice_denpyou is '�C���{�C�X�Ή��`�[';

--�ʋΒ���e�[�u���̓E�v�J�����̐�����120�ɕύX
ALTER TABLE tsuukinteiki ALTER COLUMN tekiyou TYPE character varying(120);

-- ����ŏڍׂ̉�ʌ��������ǉ��i1���݂̂Ȃ̂Œ���INSERT�j
INSERT INTO gamen_kengen_seigyo(gamen_id, gamen_name, bumon_shozoku_riyoukanou_flg, system_kanri_riyoukanou_flg, workflow_riyoukanou_flg, kaishasettei_riyoukanou_flg, keirishori_riyoukanou_flg, kinou_seigyo_cd)
VALUES ('ShouhizeiShousai','����ŏڍ�','1','1','1','1','1','') ,
 ('InvoiceSeidoKaishiSettei','�C���{�C�X���x�J�n�ݒ�','0','1','0','1','0','') ,
 ('ShouhizeiKbnSentaku','����ŋ敪�I��','1','1','1','1','1','') ON CONFLICT DO NOTHING;

-- �d�󒊏o(SIAS)�e�[�u����(�ؕ�/�ݕ�/�őΏۉȖ�)���p����Ŋz�v�Z�����J�����A�d���Ŋz�T���o�ߑ[�u�����J�����A��ǉ�
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS rurizeikeisan character varying(1) default 0;
comment on column shiwake_sias.rurizeikeisan is '�i�I�[�v���Q�P�j�ؕ��@���p����Ŋz�v�Z����';
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS surizeikeisan character varying(1) default 0;
comment on column shiwake_sias.surizeikeisan is '�i�I�[�v���Q�P�j�ݕ��@���p����Ŋz�v�Z����';
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS zurizeikeisan character varying(1) default 0;
comment on column shiwake_sias.zurizeikeisan is '�i�I�[�v���Q�P�j�őΏۉȖځ@���p����Ŋz�v�Z����';
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS rmenzeikeika character varying(1) default 0;
comment on column shiwake_sias.rmenzeikeika is '�i�I�[�v���Q�P�j�ؕ��@�d���Ŋz�T���o�ߑ[�u����';
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS smenzeikeika character varying(1) default 0;
comment on column shiwake_sias.smenzeikeika is '�i�I�[�v���Q�P�j�ݕ��@�d���Ŋz�T���o�ߑ[�u����';
ALTER TABLE shiwake_sias ADD COLUMN IF NOT EXISTS zmenzeikeika character varying(1) default 0;
comment on column shiwake_sias.zmenzeikeika is '�i�I�[�v���Q�P�j�őΏۉȖځ@�d���Ŋz�T���o�ߑ[�u����';

-- �d�󒊏o(SIAS)(shiwake_sias)�̓E�v�J�����̐�����120�ɕύX
ALTER TABLE shiwake_sias ALTER COLUMN rtky TYPE character varying(120);
ALTER TABLE shiwake_sias ALTER COLUMN stky TYPE character varying(120);

-- �d�󒊏o(de3)�e�[�u����(�ؕ�/�ݕ�/�őΏۉȖ�)���p����Ŋz�v�Z�����J�����A�d���Ŋz�T���o�ߑ[�u�����J�����A��ǉ�
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS rurizeikeisan character varying(1) default 0;
comment on column shiwake_de3.rurizeikeisan is '�i�I�[�v���Q�P�j�ؕ��@���p����Ŋz�v�Z����';
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS surizeikeisan character varying(1) default 0;
comment on column shiwake_de3.surizeikeisan is '�i�I�[�v���Q�P�j�ݕ��@���p����Ŋz�v�Z����';
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS zurizeikeisan character varying(1) default 0;
comment on column shiwake_de3.zurizeikeisan is '�i�I�[�v���Q�P�j�őΏۉȖځ@���p����Ŋz�v�Z����';
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS rmenzeikeika character varying(1) default 0;
comment on column shiwake_de3.rmenzeikeika is '�i�I�[�v���Q�P�j�ؕ��@�d���Ŋz�T���o�ߑ[�u����';
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS smenzeikeika character varying(1) default 0;
comment on column shiwake_de3.smenzeikeika is '�i�I�[�v���Q�P�j�ݕ��@�d���Ŋz�T���o�ߑ[�u����';
ALTER TABLE shiwake_de3 ADD COLUMN IF NOT EXISTS zmenzeikeika character varying(1) default 0;
comment on column shiwake_de3.zmenzeikeika is '�i�I�[�v���Q�P�j�őΏۉȖځ@�d���Ŋz�T���o�ߑ[�u����';

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

-- shiwake_pattern_setting�ւ̕����E�d���敪�ǉ�
-- ��U�ېŋ敪�Ƃ��낦�Ă���
-- �􂢑ւ�����̂��߁A�@�B�I��INSERT ON CONFLICT DO NOTHING���Ƃ��ď���
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
