/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Revathi
 * Created: 12 mar, 2024
 */

 ******************************************************************************************************************

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


