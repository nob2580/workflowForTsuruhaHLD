SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

start transaction;

\copy user_info FROM '.\files\csv\user_info.csv' WITH CSV header ENCODING 'SHIFT-JIS';
\copy shozoku_bumon_wariate FROM '.\files\csv\shozoku_bumon_wariate.csv' WITH CSV header ENCODING 'SHIFT-JIS';
INSERT INTO password
SELECT
	user_id,
	'\xc30d04070302525d61028bf42ab064d23901dad49d3f09b57a08513fc73c24d1a9d463a250171b60e3d6d53c7a0921fb1a0edaabeb2be38e69ab82d8ebf88953f183edc8882690199765'
FROM
	user_info u
WHERE
	u.user_id NOT IN (SELECT user_id FROM password);

commit;
