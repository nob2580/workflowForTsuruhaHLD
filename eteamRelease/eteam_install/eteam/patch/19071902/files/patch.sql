SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- �A���[No824
-- �C�Ȗڂ��I�����ꂽ���_�Ńf�t�H���g�̉ېŋ敪�E�ŗ��E�����敪���\�������̂͗ǂ��̂ł����A�@�������̂悤�ȏ���őΏۊO�̉Ȗڂ̏ꍇ�́A�ېŋ敪�E�ŗ��E�����敪�̓X�y�[�X��disable�Ƃ��Ă��������B
-- �D����E��������ňȊO�̉ȖڂɐŊz�ΏۉȖڂ͕s�v�Ȃ̂ŁAdisable�ɂȂ��Ă��܂����A�Ŋz�ŋ敪���s�v�Ȃ̂ŁA�X�y�[�X��disable�ɂ��Ă��������B
--�����R�[�h ���I�v�V�����p���R�[�h�̓p�b�`�O�ɂȂ���΍폜
CREATE TABLE naibu_cd_setting_tmp AS SELECT * FROM naibu_cd_setting;
DELETE FROM naibu_cd_setting;
\copy naibu_cd_setting FROM '.\files\csv\naibu_cd_setting.csv' WITH CSV header ENCODING 'SHIFT-JIS';
-- �x���˗��\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A013' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A013')=0;
-- ���������\����VUP�O�ɐݒ肳��Ă��Ȃ�������K�v�Ȃ��̂ō폜
DELETE FROM naibu_cd_setting WHERE naibu_cd='A003' AND (SELECT COUNT(*) FROM naibu_cd_setting_tmp WHERE naibu_cd='A003')=0;
DROP TABLE naibu_cd_setting_tmp;


commit;
