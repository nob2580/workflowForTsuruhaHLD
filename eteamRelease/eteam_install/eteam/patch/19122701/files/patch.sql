SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- K-157
-- �t�@�C���Y�t�@�\��OFF�Ȃ�t�@�C���Y�t�K�{�t���O��"0"�ɂ���
UPDATE shiwake_pattern_master_zaimu_kyoten set tenpu_file_hissu_flg = '0' 
WHERE '0' = (SELECT kinou_seigyo_kbn FROM kinou_seigyo WHERE kinou_seigyo_cd = 'TP');

commit;
