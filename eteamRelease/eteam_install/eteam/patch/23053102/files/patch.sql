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

-- �ݒ���f�[�^����uWF���݂Ȃ����s���̏W�v���@�v�𕜊�
UPDATE setting_info SET editable_flg = 1, hissu_flg = 1 WHERE setting_name = 'minashi_shukei_houhou';

commit;
