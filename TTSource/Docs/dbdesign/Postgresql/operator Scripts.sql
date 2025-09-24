/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Revathi
 * Created: 16 APR, 2024
 */

CREATE OR REPLACE FUNCTION public.int_gt_text(
	integer,
	text)
    RETURNS boolean
    LANGUAGE 'sql'
AS $BODY$
	select $1>$2::integer;
$BODY$;

CREATE OPERATOR public.> (
    FUNCTION = int_gt_text,
    LEFTARG = integer,
    RIGHTARG = text,
    COMMUTATOR = <,
    NEGATOR = <=,
    RESTRICT = scalargtsel,
    JOIN = scalargtjoinsel
);

CREATE OR REPLACE FUNCTION public.int_lt_text(
	integer,
	text)
    RETURNS boolean
    LANGUAGE 'sql'
AS $BODY$
	select $1<$2::integer;
$BODY$;

CREATE OPERATOR public.< (
    FUNCTION = int_lt_text,
    LEFTARG = integer,
    RIGHTARG = text,
    COMMUTATOR = >,
    NEGATOR = >=,
    RESTRICT = scalarltsel,
    JOIN = scalarltjoinsel
);

CREATE OR REPLACE FUNCTION public.text_plus_int(
	text,
	integer)
    RETURNS integer
    LANGUAGE 'sql'
AS $BODY$
	select $1::integer+$2;
$BODY$;

delete from pg_operator where oid=1328::oid

CREATE OR REPLACE FUNCTION public.dt_minus_dt(dt1 timestamp without time zone, dt2 timestamp without time zone)
RETURNS INTEGER language plpgsql AS $BODY$
DECLARE
    days INTEGER;
BEGIN
    RAISE NOTICE 'hi';
--     SELECT DATE_PART('day', dt1::timestamp - dt2::timestamp)::integer INTO days;
	SELECT dt1::date - dt2::date INTO days;
    RETURN days;
END;
$BODY$;

CREATE OPERATOR - (
  PROCEDURE = public.dt_minus_dt,
  LEFTARG = timestamp without time zone,
  RIGHTARG = timestamp without time zone
);

delete from pg_operator where oid=2067::oid

CREATE OR REPLACE FUNCTION public.varchar_eq_text(
	varchar,
	text)
    RETURNS boolean
AS 'select $1::text = $2;'
LANGUAGE SQL IMMUTABLE;

-- Operator: =;

-- DROP OPERATOR IF EXISTS public.= (varchar , text);

CREATE OPERATOR public.= (
    FUNCTION = varchar_eq_text,
    LEFTARG = varchar,
    RIGHTARG = text,
--     COMMUTATOR = =,
--     NEGATOR = <>,
    RESTRICT = eqsel,
    JOIN = eqjoinsel,
    HASHES, MERGES
);

CREATE OR REPLACE FUNCTION public.text_eq_varchar(
	text,
	varchar)
    RETURNS boolean
AS 'select $1 = $2::text;'
LANGUAGE SQL IMMUTABLE;

-- Operator: =;

-- DROP OPERATOR IF EXISTS public.= (text, varchar);

CREATE OPERATOR public.= (
    FUNCTION = public.text_eq_varchar,
    LEFTARG = text,
    RIGHTARG = varchar,
--     COMMUTATOR = =,
--     NEGATOR = <>,
    RESTRICT = eqsel,
    JOIN = eqjoinsel,
    HASHES, MERGES
);

CREATE OR REPLACE FUNCTION public.num_eq_text(
	numeric,
	text)
    RETURNS boolean
AS 'select case when $2 ~ ''^[0-9\.]+$'' then $1 operator(pg_catalog.=) cast($2 as numeric) else $1::text = $2 end;'
LANGUAGE SQL IMMUTABLE;

-- Operator: =;

-- DROP OPERATOR IF EXISTS public.= (numeric , text);

CREATE OPERATOR public.= (
    FUNCTION = num_eq_text,
    LEFTARG = numeric,
    RIGHTARG = text,
--     COMMUTATOR = =,
--     NEGATOR = <>,
    RESTRICT = eqsel,
    JOIN = eqjoinsel,
    HASHES, MERGES
);

CREATE OR REPLACE FUNCTION public.text_eq_num(
	text, numeric)
    RETURNS boolean
AS 'select case when $1 ~ ''^[0-9\.]+$'' then cast($1 as numeric) operator(pg_catalog.=) $2 else $1 = $2::text end;'
LANGUAGE SQL IMMUTABLE;

-- Operator: =;

-- DROP OPERATOR IF EXISTS public.= (text, numeric);

CREATE OPERATOR public.= (
    FUNCTION = text_eq_num,
    LEFTARG = text,
    RIGHTARG = numeric,
--     COMMUTATOR = =,
--     NEGATOR = <>,
    RESTRICT = eqsel,
    JOIN = eqjoinsel,
    HASHES, MERGES
);

CREATE OR REPLACE FUNCTION public.num_eq_varchar(
	numeric,
	varchar)
    RETURNS boolean
AS 'select case when $2 ~ ''^[0-9\.]+$'' then $1 operator(pg_catalog.=) cast($2 as numeric) else $1::varchar = $2 end;'
LANGUAGE SQL IMMUTABLE;

-- Operator: =;

-- DROP OPERATOR IF EXISTS public.= (numeric , varchar);

CREATE OPERATOR public.= (
    FUNCTION = num_eq_varchar,
    LEFTARG = numeric,
    RIGHTARG = varchar,
--     COMMUTATOR = =,
--     NEGATOR = <>,
    RESTRICT = eqsel,
    JOIN = eqjoinsel,
    HASHES, MERGES
);

CREATE OR REPLACE FUNCTION public.varchar_eq_num(
	varchar, numeric)
    RETURNS boolean
AS 'select case when $1 ~ ''^[0-9\.]+$'' then cast($1 as numeric) operator(pg_catalog.=) $2 else $1 = $2::varchar end;'
LANGUAGE SQL IMMUTABLE;

-- Operator: =;

-- DROP OPERATOR IF EXISTS public.= (varchar, numeric);

CREATE OPERATOR public.= (
    FUNCTION = varchar_eq_num,
    LEFTARG = varchar,
    RIGHTARG = numeric,
--     COMMUTATOR = =,
--     NEGATOR = <>,
    RESTRICT = eqsel,
    JOIN = eqjoinsel,
    HASHES, MERGES
);

drop FUNCTION public.varchar_eq_varchar

CREATE OR REPLACE FUNCTION public.varchar_eq_varchar(
	varchar,
	varchar)
    RETURNS boolean
AS 'select $1::text = $2::text;'
LANGUAGE SQL IMMUTABLE;

-- Operator: =;

-- DROP OPERATOR IF EXISTS public.= (varchar, varchar);

CREATE OPERATOR public.= (
    FUNCTION = public.varchar_eq_varchar,
    LEFTARG = varchar,
    RIGHTARG = varchar,
--     COMMUTATOR = =,
--     NEGATOR = <>,
    RESTRICT = eqsel,
    JOIN = eqjoinsel,
    HASHES, MERGES
);