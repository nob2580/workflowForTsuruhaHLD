SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

-- íËä˙ãÊä‘ÇÃï∂éöêîêßå¿äOÇ∑
ALTER TABLE teiki_jouhou					ALTER COLUMN web_teiki_kukan	TYPE VARCHAR;
ALTER TABLE tsuukinteiki					ALTER COLUMN jyousha_kukan		TYPE VARCHAR;
ALTER TABLE kaigai_ryohiseisan_meisai		ALTER COLUMN bikou				TYPE VARCHAR;
ALTER TABLE kaigai_ryohi_karibarai_meisai	ALTER COLUMN bikou				TYPE VARCHAR;
ALTER TABLE koutsuuhiseisan_meisai			ALTER COLUMN bikou				TYPE VARCHAR;
ALTER TABLE ryohiseisan_meisai				ALTER COLUMN bikou				TYPE VARCHAR;
ALTER TABLE ryohi_karibarai_meisai			ALTER COLUMN bikou				TYPE VARCHAR;

commit;