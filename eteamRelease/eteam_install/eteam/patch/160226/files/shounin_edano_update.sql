SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-------------------------------------
-- �ꎞ�R�t���f�[�^�i�[�p�e�[�u��
-------------------------------------
CREATE TABLE shounin_himoduke_tmp (
  denpyou_id character varying(19) not null
  , edano integer not null
  , route_edano integer not null
  , user_id character varying(19) not null
  , gyoumu_role_id character varying(19) not null
  , joukyou character varying not null
  , comment character varying not null
);

-------------------------------------
-- route_edano���ڃf�[�^�i�[
-------------------------------------
INSERT INTO shounin_himoduke_tmp
SELECT
  denpyou_id
  , edano
  , ROW_NUMBER() OVER (PARTITION BY denpyou_id ORDER BY edano ASC) AS route_edano
  , user_id
  , gyoumu_role_id
  , joukyou
  , comment
FROM
  shounin_joukyou AS tmp_j1
WHERE
  edano >= (SELECT max(edano) FROM shounin_joukyou AS tmp_j2 WHERE tmp_j2.denpyou_id=tmp_j1.denpyou_id AND (tmp_j2.joukyou like '%�\��%' OR tmp_j2.joukyou like '%�扺%'))
  AND
    (joukyou like '%�\��%' 
  OR joukyou like '%�扺%' 
  OR joukyou like '%���F%' 
  OR joukyou like '%�۔F%'
  OR comment = '��ʐ挈���F��'
  OR comment = '��ʐ挈�۔F��')
  AND NOT(joukyou like '%���F���[�g%');

-------------------------------------
-- ���X�g�����߂̏ꍇ�͌��݃t���O�ȍ~�̃f�[�^�폜
-------------------------------------
DELETE FROM shounin_himoduke_tmp tmp_j1
WHERE
  route_edano >= ( COALESCE( (SELECT edano FROM shounin_route AS tmp_j2 WHERE tmp_j1.denpyou_id=tmp_j2.denpyou_id AND tmp_j2.genzai_flg = '1'), 0 ) )
  AND denpyou_id IN (SELECT tmp_j3.denpyou_id FROM shounin_joukyou tmp_j3 WHERE tmp_j3.edano = (SELECT max(edano) FROM shounin_himoduke_tmp AS tmp_j4 WHERE tmp_j4.denpyou_id=tmp_j3.denpyou_id) AND (tmp_j3.joukyou = '���߂�' OR tmp_j3.joukyou = '��߂�') );

-------------------------------------
-- ���F�󋵕s��v�f�[�^�ێ��p�e�[�u���쐬
-------------------------------------
CREATE TABLE shounin_fuicchi_tmp (
  denpyou_id character varying(19) NOT NULL
);

-------------------------------------
-- �A�b�v�f�[�g���s�f�[�^���X�g�ێ��p�e�[�u���쐬
-------------------------------------
CREATE TABLE update_fail_tmp (
  denpyou_id character varying(19) NOT NULL
);
-------------------------------------
-- ���F�󋵕s��v�f�[�^�o�^ ��shounin_himoduke_tmp�͍쐬�ς݂̑O��
-------------------------------------
INSERT INTO shounin_fuicchi_tmp 
SELECT DISTINCT r.denpyou_id 
FROM shounin_route r 
FULL OUTER JOIN shounin_himoduke_tmp j ON (r.denpyou_id,r.edano) = (j.denpyou_id,j.route_edano)
WHERE (r.edano <> j.route_edano)
  OR (r.user_id <> '' AND r.user_id <> j.user_id)
  OR (r.gyoumu_role_id <> '' AND r.gyoumu_role_id <> j.gyoumu_role_id)
  OR (j.route_edano > ( COALESCE( (SELECT edano FROM shounin_route AS tmp_j2 WHERE r.denpyou_id=tmp_j2.denpyou_id AND tmp_j2.genzai_flg = '1'), 32767 ) ));


-------------------------------------
-- shounin_route�f�[�^�o�^ ���s��v��������
-------------------------------------
\qecho '���F�󋵁E���F���[�g��v�f�[�^�ꗗ'
\qecho '----------------------------------'
\COPY (SELECT DISTINCT denpyou_id FROM shounin_himoduke_tmp WHERE denpyou_id NOT IN(SELECT denpyou_id FROM shounin_fuicchi_tmp) ORDER BY denpyou_id) TO stdout;

UPDATE shounin_route r SET 
joukyou_edano = (SELECT edano FROM shounin_himoduke_tmp j WHERE j.denpyou_id=r.denpyou_id AND j.route_edano=r.edano 
AND denpyou_id NOT IN(SELECT denpyou_id FROM shounin_fuicchi_tmp) 
);

\qecho '���F�󋵁E���F���[�g�s��v�f�[�^�ꗗ'
\qecho '----------------------------------'
\COPY (SELECT * FROM shounin_fuicchi_tmp ORDER BY denpyou_id) TO stdout;

-------------------------------------
-- ���F���[�g���O��v�f�[�^�A�b�v�f�[�gFunction(�P��w��)
-------------------------------------
DROP FUNCTION if exists check_update_icchi_edano(VARCHAR, INTEGER, INTEGER);
CREATE FUNCTION check_update_icchi_edano(dpid VARCHAR, rted INTEGER ,jkst INTEGER) RETURNS INTEGER as $$
DECLARE
    recrowrt RECORD;
    recrowjk RECORD;
BEGIN
    SELECT * INTO recrowrt FROM shounin_route WHERE denpyou_id = dpid AND edano = rted;
    FOR cnt IN REVERSE jkst..1 LOOP
        SELECT * INTO recrowjk FROM shounin_himoduke_tmp WHERE denpyou_id = dpid AND route_edano = cnt;
        IF recrowrt.gyoumu_role_id <> '' AND recrowrt.gyoumu_role_id = recrowjk.gyoumu_role_id THEN
            UPDATE shounin_route SET joukyou_edano = recrowjk.edano WHERE denpyou_id = dpid AND edano = rted;
            RETURN recrowjk.route_edano;
        ELSEIF recrowrt.gyoumu_role_id = '' AND recrowrt.user_id <> '' AND recrowrt.user_id = recrowjk.user_id THEN
            UPDATE shounin_route SET joukyou_edano = recrowjk.edano WHERE denpyou_id = dpid AND edano = rted;
            RETURN recrowjk.route_edano;
        END IF;
    END LOOP;
    RETURN -1;
END;
$$ language plpgsql VOLATILE
;

-------------------------------------
-- ���F���[�g���O��v�f�[�^�A�b�v�f�[�gFunction(�S��)
-------------------------------------
DROP FUNCTION IF EXISTS update_shounin_edano();
CREATE FUNCTION update_shounin_edano() RETURNS void as $$
DECLARE
    recrow RECORD;
    rectmp RECORD;
    selsql TEXT := 'SELECT * FROM shounin_fuicchi_tmp';
    routeedamax INTEGER;
    himodukecureda INTEGER;
    retno INTEGER;
BEGIN
    FOR recrow IN EXECUTE selsql LOOP
       
       RAISE INFO '���F�󋵁E���F���[�g�s��v�f�[�^�X�V [%]',recrow.denpyou_id;
       
       SELECT edano INTO rectmp FROM shounin_route WHERE denpyou_id = recrow.denpyou_id AND genzai_flg = '1';
       IF NOT FOUND THEN
          SELECT count(*) as rtcnt INTO rectmp FROM shounin_route WHERE denpyou_id = recrow.denpyou_id;
          routeedamax := rectmp.rtcnt;
       ELSE
          routeedamax := rectmp.edano - 1;
       END IF;
       SELECT count(*) as hdcnt INTO rectmp FROM shounin_himoduke_tmp WHERE denpyou_id = recrow.denpyou_id;
       himodukecureda := rectmp.hdcnt;
       
       retno := 0;
       FOR cnt IN REVERSE routeedamax..1 LOOP
           IF himodukecureda > 0 THEN
              retno := check_update_icchi_edano(recrow.denpyou_id, cnt, himodukecureda);
           END IF;
           IF retno > 0 THEN
               himodukecureda := retno - 1;
           END IF;
           IF retno = -1 AND cnt = 1 THEN
               --�A�b�v�f�[�g���s���O�o�^
               RAISE INFO '���F�󋵃A�b�v�f�[�g���s [%]',recrow.denpyou_id;
               INSERT INTO update_fail_tmp(denpyou_id) SELECT ( recrow.denpyou_id ) WHERE NOT EXISTS(SELECT 1 FROM update_fail_tmp WHERE update_fail_tmp.denpyou_id = recrow.denpyou_id);
           END IF;
       END LOOP;
       
    END LOOP;
END;
$$ language plpgsql VOLATILE
;

-------------------------------------
-- �v���V�[�W�����s
-------------------------------------
SELECT update_shounin_edano();

-------------------------------------
-- �ꎞ�e�[�u���E�v���V�[�W���폜
-------------------------------------
DROP TABLE shounin_himoduke_tmp;
DROP TABLE shounin_fuicchi_tmp;
DROP FUNCTION IF EXISTS update_shounin_edano();
DROP FUNCTION IF EXISTS check_update_icchi_edano(VARCHAR, INTEGER, INTEGER);

commit;