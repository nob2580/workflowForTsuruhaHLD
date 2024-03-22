SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

ALTER TABLE furikae ADD COLUMN IF NOT EXISTS kashi_zeiritsu numeric(3) not null default 0;
comment on column furikae.kashi_zeiritsu is '‘İ•ûÅ—¦';
ALTER TABLE furikae ADD COLUMN IF NOT EXISTS  kashi_keigen_zeiritsu_kbn character varying(1) default 0 not null;
comment on column furikae.kashi_keigen_zeiritsu_kbn is '‘İ•ûŒyŒ¸Å—¦‹æ•ª';
alter table furikae RENAME column zeiritsu to kari_zeiritsu;
comment on column furikae.kari_zeiritsu is 'Ø•ûÅ—¦';
alter table furikae RENAME column keigen_zeiritsu_kbn to kari_keigen_zeiritsu_kbn;
comment on column furikae.kari_keigen_zeiritsu_kbn is 'Ø•ûŒyŒ¸Å—¦‹æ•ª';

commit;
