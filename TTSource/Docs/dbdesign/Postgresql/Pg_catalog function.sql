/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Revathi
 * Created: 16 APR, 2024
 */

CREATE OR REPLACE FUNCTION pg_catalog.add_months(
	day date,
	value integer)
RETURNS date
LANGUAGE 'c'
COST 1
IMMUTABLE STRICT PARALLEL UNSAFE
AS '$libdir/orafce', 'add_months'
;

CREATE OR REPLACE FUNCTION pg_catalog.to_date(text, text)
RETURNS date
LANGUAGE internal
STABLE PARALLEL SAFE STRICT
AS $function$to_date$function$
CREATE OR REPLACE FUNCTION pg_catalog.to_date(str text)
RETURNS timestamp without time zone
LANGUAGE c
STABLE STRICT
AS '$libdir/orafce', $function$ora_to_date$function$

CREATE OR REPLACE FUNCTION pg_catalog.add_months(day date, value integer)
 RETURNS date
 LANGUAGE c
 IMMUTABLE STRICT
AS '$libdir/orafce', $function$add_months$function$;

CREATE OR REPLACE FUNCTION pg_catalog.substr(text, integer, integer)
 RETURNS text
 LANGUAGE sql
AS $function$
	select oracle.substr($1,$2,$3);
$function$;

CREATE OR REPLACE FUNCTION pg_catalog.textcat(text, text)
 RETURNS text
 LANGUAGE sql
AS $function$
	select concat($1,$2);
$function$;

CREATE OPERATOR pg_catalog.+ (
    FUNCTION = text_plus_int,
    LEFTARG = text,
    RIGHTARG = integer,
    COMMUTATOR = +
);

CREATE OR REPLACE FUNCTION oracle.decode(character varying, character varying, text, text)
 RETURNS text
 LANGUAGE c
 IMMUTABLE
AS '$libdir/orafce', $function$ora_decode$function$
;