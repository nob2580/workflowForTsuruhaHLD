SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kashi_zeiritsu numeric(3) not null default 0;
comment on column furikae.kashi_zeiritsu is '�ݕ��ŗ�';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS  kashi_keigen_zeiritsu_kbn character varying(1) default 0 not null;
comment on column furikae.kashi_keigen_zeiritsu_kbn is '�ݕ��y���ŗ��敪';
alter table furikae RENAME column zeiritsu to kari_zeiritsu;
comment on column furikae.kari_zeiritsu is '�ؕ��ŗ�';
alter table furikae RENAME column keigen_zeiritsu_kbn to kari_keigen_zeiritsu_kbn;
comment on column furikae.kari_keigen_zeiritsu_kbn is '�ؕ��y���ŗ��敪';

commit;
