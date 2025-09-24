/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Revathi
 * Created: 12 sep, 2022
 */

******************************************************************************************************************
CREATE OR REPLACE FUNCTION public.lpad(
	numeric,
	numeric,
	text)
    RETURNS text
    LANGUAGE 'plpgsql'
    COST 100
    STABLE SECURITY DEFINER PARALLEL UNSAFE
AS $BODY$
DECLARE
   RET_TXT  text;
BEGIN
     RET_TXT := lpad($1::text, $2::integer, $3);
   RETURN RET_TXT;
END;
$BODY$;


CREATE OR REPLACE FUNCTION get_future_service(
	custid text)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    STABLE SECURITY DEFINER PARALLEL UNSAFE
AS $BODY$
DECLARE

DT_OFBIRTH timestamp;
DT_OFJOIN timestamp;
RETIREAGE smallint;
RETIREDATE timestamp;
FUTURE_SERVICE varchar(30);
YRS bigint;
MNTHS bigint;
DYS bigint;
CURRDATE timestamp;

BEGIN
SELECT C.DOB,C.JOINING_DATE INTO STRICT DT_OFBIRTH,DT_OFJOIN  FROM CUSTOMER  C WHERE  C.CUST_ID = CUSTID;

SELECT DE.CURR_APPL_DT INTO STRICT CURRDATE FROM DAY_END DE WHERE DE.BRANCH_CODE = '0001';

SELECT P.RETIREMENT_AGE INTO STRICT RETIREAGE  FROM PARAMETERS P;

SELECT GET_RETIRE_DATE(CUSTID) INTO STRICT RETIREDATE;

SELECT trunc(MONTHS_BETWEEN(RETIREDATE, CURRDATE) / 12) YRS,
trunc(MOD(MONTHS_BETWEEN(RETIREDATE, CURRDATE), 12)) MNTHS,
trunc(RETIREDATE - ADD_MONTHS(CURRDATE, TRUNC(MONTHS_BETWEEN(RETIREDATE, CURRDATE)))) DYS INTO STRICT YRS,MNTHS,DYS;

 IF YRS IS NULL THEN
  YRS := 0;
 END IF;
 IF MNTHS IS NULL THEN
 MNTHS := 0;
 END IF;
 IF DYS IS NULL  THEN
 DYS := 0;
 END IF;
 FUTURE_SERVICE := YRS::varchar||'Years'||'  '||MNTHS::varchar||'Months'||'  '||DYS::varchar||'Days';
 RETURN FUTURE_SERVICE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.add_months(
	timestamp with time zone,
	numeric)
    RETURNS timestamp without time zone
    LANGUAGE 'sql'
    COST 100
    IMMUTABLE STRICT PARALLEL UNSAFE
AS $BODY$
 SELECT (pg_catalog.add_months($1::pg_catalog.date, $2::integer) + $1::time)::oracle.date; 
$BODY$;

CREATE OR REPLACE PROCEDURE savpassbook4aperiod_new(
	act_number text,
	frdt timestamp without time zone)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE

/*  
DO $$
DECLARE
  l_act_num text;
BEGIN
  FOR l_act_num IN (SELECT DISTINCT ACT_NUM FROM  ALL_TRANS WHERE TRANS_DT >='2022-12-01'
AND AC_HD_ID IN ('2012010104','2012001001')) LOOP
    BEGIN
      call SAVPASSBOOK4APERIOD(l_act_num, '2022-12-01'::date);
      call SAVDAYEND4APERIOD(l_act_num , '2022-12-01'::date);
    EXCEPTION
      WHEN OTHERS THEN
        RAISE NOTICE 'Error in Creation %', l_act_num;
    END;
  END LOOP;
END $$;
 */
RcFTag bigint;
MxSlNo bigint;
MxPGNo bigint;
RecNo bigint :=30;
Sort_FrmDt timestamp;    -- New variable declaration by Ajith on 27/07/2018 
MXDT timestamp;
BALAMT double precision;
MSTUPDAT  varchar(1):='Y'; -- VARIABLE MSTUPDAT  ='Y'  IS USED FOR UPDATE ACT_MASTER CLEAR_BALANCE ,TOTAL_BALANCE=,AVAILABLE_BALANCE
I integer:=0;
J integer:=0;
ACNO PASS_BOOK.ACT_NUM%TYPE:='A';
BEGIN
    DELETE FROM PASS_BOOK WHERE ACT_NUM =act_number AND TRANS_DT >=FRDT;
      SELECT COUNT(*)
 INTO STRICT RcFTag FROM PASS_BOOK WHERE ACT_NUM =act_number;
    --SELECT MAX(nvl(PAGENO,1)) INTO MxPGNo FROM PASS_BOOK  WHERE ACT_NUM =act_number ; -- Old Code Blocked by Ajith on 27/07/2018
    SELECT MAX(coalesce(PAGENO,1)) INTO STRICT MxPGNo
      FROM PASS_BOOK  
       WHERE ACT_NUM =act_number AND TRANS_DT <FRDT; -- New Code Modified by Ajith on 27/07/2018
    SELECT MAX(TRANS_DT) INTO STRICT Sort_FrmDt
      FROM PASS_BOOK  
       WHERE ACT_NUM =act_number AND PAGENO=MxPGNo;   -- New Code Added by Ajith on 27/07/2018
    SELECT MAX(SLNO) INTO STRICT MxSlNo
      FROM PASS_BOOK 
       WHERE ACT_NUM =act_number AND TRANS_DT =Sort_FrmDt AND coalesce(PAGENO,0) =MxPGNo; -- New Code Modified by Ajith on 27/07/2018
/*    
-- Old Code Blocked by Ajith on 27/07/2018 - Begins --
    AND TRANS_DT =(SELECT MAX(TRANS_DT)  FROM PASS_BOOK WHERE ACT_NUM =act_number AND TRANS_DT <FRDT )
    AND NVL(PAGENO,0) =(SELECT MAX(nvl(PAGENO,0)) FROM PASS_BOOK WHERE ACT_NUM =act_number); -- Old Code Blocked by Ajith on 27/07/2018
-- Old Code Blocked by Ajith on 27/07/2018 - Ends --
*/
    Insert into PASS_BOOK(ACT_NUM, SLNO, TRANS_DT, PARTICULARS, DEBIT,
        CREDIT, PBOOK_FLAG, BALANCE, INSTRUMENT_NO1, INSTRUMENT_NO2, 
        TRANS_ID, BATCH_ID, INST_TYPE, INST_DT, STATUS, 
        AUTHORIZE_STATUS, AUTHORIZE_DT, PAGENO, CREATED_DT, AUTHORIZE_STATUS_2 )
         select X.act_num,((SUM(1) OVER (order by  X.TRANS_DT,X.AUTHORIZE_DT,X.STATUS_DT,X.TRANS_TYPE ,X.rowid )) + coalesce(X.MxSlNo,0))
  -(((CEIL (coalesce(((SUM(1)  OVER (order by  X.TRANS_DT,X.AUTHORIZE_DT,X.STATUS_DT,X.TRANS_TYPE ,X.rowid )) + coalesce(X.MxSlNo,0)),0)/X.RecNo))-1)*X.RecNo)
   AS SLNO , X.trans_dt, x.particulars,x.DEBIT,x.credit, 1::NUMERIC,
   (coalesce((SELECT BALANCE FROM PASS_BOOK WHERE ACT_NUM =x.ACT_NUM AND TRANS_DT =(
        SELECT MAX(TRANS_DT)  FROM PASS_BOOK WHERE ACT_NUM =x.ACT_NUM  AND TRANS_DT <FRDT )
        AND SLNO = coalesce(x.MxSlNo,0) AND PAGENO = MxPGNo),0) +
        SUM( (CASE when x.TRANS_TYPE='CREDIT' THEN AMOUNT ELSE 0 END)-
        (CASE when x.TRANS_TYPE='DEBIT' THEN AMOUNT ELSE 0 END)) OVER (order by TRANS_DT,AUTHORIZE_DT,STATUS_DT,TRANS_TYPE)) 
        AS BALANCE, x.instrument_no1 ,x.instrument_no2 ,
x.trans_id,x.Batch_id,x.inst_type ,x.inst_dt,x.status ,x.authorize_status ,
x.authorize_dt ,(CEIL (coalesce(((SUM(1)  OVER (order by  X.TRANS_DT,X.AUTHORIZE_DT,X.STATUS_DT,X.TRANS_TYPE ,X.rowid )) + coalesce(X.MxSlNo,0)),0)/X.RecNo)) as Pageno, x.status_dt ,x.authorize_status_2 From  
  (SELECT ACT_NUM,AT.trans_dt,AT.TRANS_TYPE,AT.AMOUNT,at.ctid as rowid,MxSlNo,RecNo,PARTICULARS,
(CASE when AT.TRANS_TYPE='CREDIT' THEN AMOUNT ELSE 0 END) AS CREDIT,  
(CASE when AT.TRANS_TYPE='DEBIT' THEN AMOUNT ELSE 0 END) AS DEBIT,
AT.PROD_ID,INSTRUMENT_NO1, INSTRUMENT_NO2, 
        TRANS_ID, null as BATCH_ID, INST_TYPE, INST_DT, AT.STATUS, 
        AT.AUTHORIZE_STATUS, AT.AUTHORIZE_DT,STATUS_DT, AUTHORIZE_STATUS_2 
    from Cash_trans AT join OP_AC_PRODUCT OP  ON AT.AC_HD_ID = OP.AC_HD_ID AND AT.PROD_ID=OP.PROD_ID
    where AT.AUTHORIZE_STATUS='AUTHORIZED' AND AT.STATUS!='DELETED' AND  trans_dt >= FRDT and act_num = act_number   
union all
SELECT ACT_NUM,AT.trans_dt,AT.TRANS_TYPE,AT.AMOUNT,at.ctid as rowid,MxSlNo,RecNo,PARTICULARS,
(CASE when AT.TRANS_TYPE='CREDIT' THEN AMOUNT ELSE 0 END) AS CREDIT,
(CASE when AT.TRANS_TYPE='DEBIT' THEN AMOUNT ELSE 0 END) AS DEBIT,
AT.PROD_ID,INSTRUMENT_NO1, INSTRUMENT_NO2, 
        TRANS_ID, null as BATCH_ID, INST_TYPE, INST_DT, AT.STATUS, 
        AT.AUTHORIZE_STATUS, AT.AUTHORIZE_DT,STATUS_DT, AUTHORIZE_STATUS_2 
    from transfer_trans AT join OP_AC_PRODUCT OP  ON AT.AC_HD_ID = OP.AC_HD_ID AND AT.PROD_ID=OP.PROD_ID
    where AT.AUTHORIZE_STATUS='AUTHORIZED' AND AT.STATUS!='DELETED' AND  trans_dt >= FRDT and act_num = act_number) x
    order by x.act_num,x.trans_dt,x.status_dt,x.AUTHORIZE_DT,x.TRANS_TYPE ,coalesce(x.AMOUNT,0);
/* " AND PAGENO = MxPGNo" part added on 13/02/2019 by Ajith to avoid multiple row exception of MxSlNo in accounts with large number of transactions per day */
 --   COMMIT;
    IF RcFTag =0 THEN
--SLNO AND PAGE NO UPDATION FROM THE BEGINNING 
        DECLARE
            C1 CURSOR FOR
            SELECT ctid AS RID, ACT_NUM, TRANS_DT, DEBIT , CREDIT,  BALANCE, SLNO ,PAGENO,CREATED_DT
            FROM PASS_BOOK   where act_num = act_number
            ORDER BY ACT_NUM, TRANS_DT,CREATED_DT,coalesce(DEBIT,0),coalesce(CREDIT,0);
        BEGIN
            FOR SI IN C1
            LOOP
                IF SI.ACT_NUM=ACNO THEN
                    I:=I+1;
                    BALAMT:=(coalesce(BALAMT,0)+coalesce(SI.CREDIT,0))-coalesce(SI.DEBIT,0);
                    IF I=31 THEN
                        I:=1;
                        J:=J+1;
                    END IF;
                ELSE
                    --BALAMT:=  SI.BALANCE;  -- BALANCE UPDATION USING FIRST ROWS BALANCE
                    BALAMT:= (coalesce(SI.CREDIT,0))-coalesce(SI.DEBIT,0);
                    I:=1;
                    J:=1;
                END IF;
                 UPDATE PASS_BOOK P SET BALANCE= BALAMT ,SLNO=I, PAGENO=J WHERE p.ctid = SI.RID;
                ACNO:=SI.ACT_NUM;
              END LOOP;
        END;
/* New "ELSE" part added by Ajith on 27/07/2018 to correct the page number and serial number update.
   Earlier, the page number used to start with 1 and serial number was also wrong in many places. */
        
-- New code block by Ajith on 27/07/2018 - Begins --   
    ELSE
--SLNO  AND PAGE NO UPDATION FOR PARTIAL RECREATION OF PASSBOOK  
        DECLARE
            C11 CURSOR FOR
            SELECT ctid AS RID, ACT_NUM, TRANS_DT, DEBIT , CREDIT,  BALANCE, SLNO ,PAGENO,CREATED_DT  
            FROM PASS_BOOK 
            WHERE ACT_NUM = act_number AND (TRANS_DT>Sort_FrmDt 
            OR (TRANS_DT=Sort_FrmDt AND PAGENO=MxPGNo AND SLNO>MxSlNo))
            ORDER BY ACT_NUM, TRANS_DT,CREATED_DT,coalesce(DEBIT,0),coalesce(CREDIT,0);
        BEGIN
            SELECT BALANCE INTO STRICT BALAMT
            FROM PASS_BOOK   
            WHERE ACT_NUM = act_number AND PAGENO=MxPGNo AND SLNO=MxSlNo AND TRANS_DT=Sort_FrmDt;
            I:=MxSlNo;
            J:=MxPGNo;
            FOR SII IN C11
            LOOP
                I:=I+1;
                BALAMT:=(coalesce(BALAMT,0)+coalesce(SII.CREDIT,0))-coalesce(SII.DEBIT,0);
                IF I=31 THEN
                    I:=1;
                    J:=J+1;
                END IF;
                UPDATE PASS_BOOK P SET BALANCE= BALAMT ,SLNO=I, PAGENO=J WHERE p.ctid = SII.RID;
             END LOOP;
        END;
-- New code block by Ajith on 27/07/2018 - Ends --        
    END IF;
   -- COMMIT;
    IF coalesce(MSTUPDAT,'Z') = 'Y' THEN
      SELECT distinct max(TRANS_DT) INTO STRICT MXDT FROM PASS_BOOK where act_num = act_number;
      SELECT distinct max(coalesce(PAGENO,0)) INTO STRICT MXPGNO FROM PASS_BOOK where act_num = act_number AND TRANS_DT = MXDT;
      SELECT distinct max(SLNO) INTO STRICT MXSLNO FROM PASS_BOOK where act_num = act_number  AND TRANS_DT = MXDT AND coalesce(PAGENO,0) = MXPGNO;
      SELECT BALANCE INTO STRICT BALAMT FROM PASS_BOOK where act_num = act_number  AND TRANS_DT = MXDT AND coalesce(PAGENO,0) = MXPGNO AND SLNO = MXSLNO;
      UPDATE ACT_MASTER  SET CLEAR_BALANCE =BALAMT,TOTAL_BALANCE=BALAMT,AVAILABLE_BALANCE =BALAMT  WHERE ACT_NUM = act_number;
      --COMMIT;
    END IF;
  CALL SAVDAYEND4APERIOD(act_number::TEXT,FRDT);
END;
$BODY$;

CREATE OR REPLACE FUNCTION pudukkad.deleteaccountinttrailall(todate timestamp without time zone, branch_id text, prodid text)
 RETURNS character varying
 LANGUAGE plpgsql
 SECURITY DEFINER
AS $function$
DECLARE

         curval   varchar(16);-- PRAGMA AUTONOMOUS_TRANSACTION;

BEGIN    
          DELETE FROM ACT_INTEREST_TRIAL WHERE  
          APPL_DT=TODATE AND BRANCH_CODE=BRANCH_ID AND PRODUCT_ID=PRODID;
         --COMMIT;
          DELETE  FROM ODINTR_CAL WHERE  
          TODATE=TODATE AND SUBSTR(ACT_NUM,0,4)=BRANCH_ID AND SUBSTR(ACT_NUM,5,3)=PRODID;
          --COMMIT;
            RETURN curval;
      END;
$function$
;


CREATE OR REPLACE FUNCTION insertloaninteresttmp(
	actnum character varying,
	from_dt timestamp without time zone,
	to_dt timestamp without time zone,
	amt double precision,
	no_of_days bigint,
	tot_product double precision,
	int_amt double precision,
	int_rate double precision,
	prod_id character varying,
	int_calc_dt timestamp without time zone,
	cust_id character varying,
	remarks character varying,
	curr_dt timestamp without time zone,
	branch_code character varying,
	user_id character varying,
	valudate_type character varying)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    --VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
   curval   varchar(16):='Inserted...';
BEGIN
   CALL insertloanintproc(actnum,
                      from_dt,
                      to_dt,
                      amt,
                      no_of_days,
                      tot_product,
                      int_amt,
                      int_rate,
                      prod_id,
                      int_calc_dt,
                      cust_id,
                      remarks,
                      curr_dt,
                      branch_code,
                      user_id,
                      valudate_type
                     );
   RETURN curval;
END;
$BODY$;

CREATE OR REPLACE FUNCTION pudukkad.trigger_fct_dep_unlien()
 RETURNS trigger
 LANGUAGE plpgsql
 SECURITY DEFINER
AS $function$
BEGIN
UPDATE DEPOSIT_SUB_ACINFO  SET STATUS ='CREATED' ,AVAILABLE_BALANCE = CLEAR_BALANCE
WHERE DEPOSIT_NO IN (SELECT DEPOSIT_NO FROM DEPOSIT_LIEN  WHERE LIEN_AC_NO =NEW.ACCT_NUM);
RETURN NEW;
END;
$function$
;

CREATE OR REPLACE PROCEDURE pudukkad.updated_deposit_multipleid(depono text)
 LANGUAGE plpgsql
 SECURITY DEFINER
AS $procedure$
BEGIN
     UPDATE DEPOSIT_ACINFO  set MULTIPLE_DEPOSIT_ID ='' where DEPOSIT_NO = substr(DEPONO,0,13) and MULTIPLE_DEPOSIT_ID is not null;
     UPDATE DEPOSIT_SUB_ACINFO  set MULTIPLE_DEPOSIT_ID = '' where DEPOSIT_NO = substr(DEPONO,0,13) and MULTIPLE_DEPOSIT_ID is not null;
    -- COMMIT;
END;
$procedure$
;

CREATE OR REPLACE PROCEDURE execute_dayend_status(
	branch_code text,
	tname text,
	tstatus text,
	uid text,
	d_dt text)
LANGUAGE 'plpgsql'
    SECURITY DEFINER 
AS $BODY$
DECLARE

    C1 CURSOR FOR SELECT * FROM DAILY_DAYEND_STATUS WHERE BRANCH_ID=BRANCH_CODE AND
        DAYEND_DT=D_DT::timestamp AND TASK_NAME=TNAME;
    I smallint:=0;
BEGIN
    FOR N IN C1 LOOP
        UPDATE DAILY_DAYEND_STATUS SET TASK_STATUS=TSTATUS, DAYEND_BY=UID
            WHERE BRANCH_ID=BRANCH_CODE AND DAYEND_DT=D_DT::timestamp AND TASK_NAME=TNAME;
        I:=I+1;
    END LOOP;
    IF I=0 THEN
        INSERT INTO DAILY_DAYEND_STATUS VALUES (BRANCH_CODE, TNAME, TSTATUS, UID, D_DT::timestamp);
    END IF;
END;
$BODY$;

CREATE OR REPLACE PROCEDURE savdayend4aperiod(
	actnum text,
	frdt timestamp without time zone)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
 /*  
DO $$
DECLARE
  l_act_num text;
BEGIN
  FOR l_act_num IN (SELECT DISTINCT ACT_NUM FROM  ALL_TRANS WHERE TRANS_DT >='2022-12-01'
AND AC_HD_ID IN ('2012010104','2012001001')) LOOP
    BEGIN
      --PERFORM SAVPASSBOOK4APERIOD(l_act_num, '2022-12-01');
      call SAVDAYEND4APERIOD(l_act_num , '2022-12-01'::date);
    EXCEPTION
      WHEN OTHERS THEN
        RAISE NOTICE 'Error in Creation %', l_act_num;
    END;
  END LOOP;
END $$;
 */
BEGIN
 	DELETE FROM ACT_DAYEND_BALANCE WHERE ACT_NUM =ACTNUM AND DAY_END_DT >=FRDT;
	--COMMIT;
	Insert into  ACT_DAYEND_BALANCE(PROD_ID, ACT_NUM, DAY_END_DT, AMT )
	WITH DE AS
	(SELECT ACT_NUM,AMT  FROM ACT_DAYEND_BALANCE WHERE ACT_NUM =ACTNUM AND DAY_END_DT =(
		SELECT MAX(DAY_END_DT)  FROM ACT_DAYEND_BALANCE WHERE ACT_NUM =ACTNUM  AND
		 DAY_END_DT < FRDT )
		   ),
	 TR AS 
	 ( select  ACT_NUM ,TRANS_DT,  
		(SUM( (CASE WHEN AT.TRANS_TYPE='CREDIT' THEN AMOUNT ELSE 0 END)-
		(CASE when AT.TRANS_TYPE='DEBIT' THEN AMOUNT ELSE 0 END) ) ) 
		AS TRBALANCE 
		from all_trans AT where  AT.AUTHORIZE_STATUS='AUTHORIZED' AND AT.STATUS!='DELETED' AND  
		trans_dt >= FRDT
		  and act_num = ACTNUM
		GROUP BY ACT_NUM ,TRANS_DT  )
    select SUBSTR( TR.ACT_NUM,5,3),  TR.ACT_NUM,  TR.TRANS_DT,coalesce((SELECT DE.AMT FROM DE WHERE 
		 DE.ACT_NUM = TR.ACT_NUM ),0) + 
		 SUM(TR.TRBALANCE) OVER (order by  TR.trans_dt) AS BAL 
	FROM TR  order by  TR.trans_dt;
end;
$BODY$;

CREATE OR REPLACE FUNCTION get_tl_total (ACTNUM text, ASONDT timestamp without time zone) RETURNS bigint 
LANGUAGE 'plpgsql'
    COST 100
    STABLE SECURITY DEFINER PARALLEL UNSAFE AS $body$
DECLARE

Balance double precision := 0;

BEGIN
SELECT  sum(a.balance) into STRICT balance
    FROM (
    WITH cte_a AS (                           
                SELECT   t.act_num, 'INSURANCE CHARGES' AS charge_type,
                         SUM(coalesce(t.insurance_charge, 0)) AS amount
                    FROM loan_trans_details t
                   WHERE t.act_num = ACTNUM
                     AND t.trans_dt <=  ASONDT
                     AND t.trans_type = 'CREDIT'
                     AND t.authorize_status = 'AUTHORIZED'
                GROUP BY t.act_num
                
UNION ALL
                
                SELECT   t.act_num, 'ARBITRARY CHARGES' AS charge_type,
                         SUM(coalesce(t.arbitary_charge, 0)) AS amount
                    FROM loan_trans_details t
                   WHERE t.act_num = ACTNUM
                     AND t.trans_dt <=  ASONDT
                     AND t.trans_type = 'CREDIT'
                     AND t.authorize_status = 'AUTHORIZED'
                GROUP BY t.act_num
                
UNION ALL

                SELECT   t.act_num, 'LEGAL CHARGES' AS charge_type,
                         SUM(coalesce(t.legal_charge, 0)) AS amount
                    FROM loan_trans_details t
                   WHERE t.act_num = ACTNUM
                     AND t.trans_dt <=  ASONDT
                     AND t.trans_type = 'CREDIT'
                     AND t.authorize_status = 'AUTHORIZED'
                GROUP BY t.act_num
                
UNION ALL

                SELECT   t.act_num, 'MISCELLANEOUS CHARGES' AS charge_type,
                         SUM(coalesce(t.misc_charges, 0)) AS amount
                    FROM loan_trans_details t
                   WHERE t.act_num = ACTNUM
                     AND t.trans_dt <=  ASONDT
                     AND t.trans_type = 'CREDIT'
                     AND t.authorize_status = 'AUTHORIZED'
                GROUP BY t.act_num
                
UNION ALL

                SELECT   t.act_num, 'EXECUTION DECREE CHARGES' AS charge_type,
                         SUM(coalesce(t.advertise_charge::numeric, 0)) AS amount
                    FROM loan_trans_details t
                   WHERE t.act_num = ACTNUM
                     AND t.trans_dt <=  ASONDT
                     AND t.trans_type = 'CREDIT'
                     AND t.authorize_status = 'AUTHORIZED'
                GROUP BY t.act_num
                
UNION ALL

                SELECT   t.act_num, 'POSTAGE CHARGES' AS charge_type,
                         SUM(coalesce(t.advertise_charge::numeric, 0)) AS amount
                    FROM loan_trans_details t
                   WHERE t.act_num = ACTNUM
                     AND t.trans_dt <=  ASONDT
                     AND t.trans_type = 'CREDIT'
                     AND t.authorize_status = 'AUTHORIZED'
                GROUP BY t.act_num
                
UNION ALL

                SELECT   t.act_num, 'OTHERS' AS charge_type,
                         SUM(coalesce(t.advertise_charge::numeric, 0)) AS amount
                    FROM loan_trans_details t
                   WHERE t.act_num = ACTNUM
                     AND t.trans_dt <=  ASONDT
                     AND t.trans_type = 'CREDIT'
                     AND t.authorize_status = 'AUTHORIZED'
                GROUP BY t.act_num                
                
UNION
 
                   SELECT   t.act_num, t.charge_type  AS charge_type,sum(coalesce(t.PAID_AMT,0)) AS amount
                    FROM loans_acct_charge_details t
                   WHERE t.act_num = ACTNUM
                     AND t.charge_date <= ASONDT
                     --and t.charge_type='EP_COST' 
                     AND t.authorize_status = 'AUTHORIZED'
                     AND t.status != 'DELETED'
                    GROUP BY t.act_num,t.charge_type),
               cte_b AS (SELECT   a.act_num, a.charge_type, SUM(a.amount) AS amount
                    FROM loans_acct_charge_details a
                   WHERE a.act_num = ACTNUM
                     AND a.charge_date <=  ASONDT
                     AND a.authorize_status = 'AUTHORIZED'
                     AND a.status != 'DELETED'
                GROUP BY a.act_num, a.charge_type)
          SELECT b.act_num, a.charge_type, b.amount - a.amount AS balance,
                 NULL AS s_order, 'A' AS mod_type
            FROM cte_b b LEFT JOIN cte_a a
                 ON a.act_num = b.act_num
               AND a.charge_type = coalesce(b.charge_type, 'OTHERS')
          
UNION ALL

          SELECT f.acct_num AS act_num, 'PRINCIPAL' AS charge_type,
                 CASE
                    WHEN p.behaves_like != 'OD'
                       THEN get_tl_balance(f.acct_num,  ASONDT)
                    ELSE get_adv_balance(f.acct_num,  ASONDT)
                 END AS balance,
                 1 AS s_order, 'A' AS mod_type
            FROM loans_facility_details f JOIN loans_product p
                 ON p.prod_id = f.prod_id
                 JOIN loans_sanction_details sd ON sd.borrow_no = f.borrow_no
           WHERE f.acct_num = ACTNUM
          
UNION ALL

          SELECT f.acct_num AS act_num, 'INTEREST' AS charge_type,
                 CASE
                    WHEN p.behaves_like != 'OD'
                       THEN get_tl_interest(f.acct_num,
                                             sd.from_dt,
                                              ASONDT
                                            )
                    ELSE get_adv_interest_application(f.acct_num,  ASONDT)
                 END AS balance,
                 2 AS s_order, 'A' AS mod_type
            FROM loans_facility_details f JOIN loans_product p
                 ON p.prod_id = f.prod_id
                 JOIN loans_sanction_details sd ON sd.borrow_no = f.borrow_no
           WHERE f.acct_num =  ACTNUM
          
UNION ALL

          SELECT f.acct_num AS act_num, 'PENAL INTEREST' AS charge_type,
                 CASE
                    WHEN p.behaves_like != 'OD'
                       THEN get_tl_od_interest(f.acct_num,ASONDT)
                    ELSE get_adv_pen_int_application(f.acct_num,
                                                       ASONDT
                                                     )
                 END AS balance,
                 3 AS s_order, 'A' AS mod_type
            FROM loans_facility_details f JOIN loans_product p
                 ON p.prod_id = f.prod_id
                 JOIN loans_sanction_details sd ON sd.borrow_no = f.borrow_no
           WHERE f.acct_num =  ACTNUM
           ) a;
RETURN balance;
End;
$body$


CREATE OR REPLACE FUNCTION get_curr_bills_id(
	prodid text,
	branchid text)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
id_prefix BRANCH_ACNO_MAINTENANCE.PREFIX%TYPE;
id_currvalue BRANCH_ACNO_MAINTENANCE.LAST_AC_NO%TYPE;
new_id varchar(20);

BEGIN
UPDATE BRANCH_ACNO_MAINTENANCE SET LAST_AC_NO=NEXT_AC_NO,NEXT_AC_NO=NEXT_AC_NO::integer+1::integer WHERE PROD_ID=prodId AND BRANCH_ID = branchid;
SELECT LAST_AC_NO,PREFIX INTO STRICT id_currvalue,id_prefix  FROM BRANCH_ACNO_MAINTENANCE WHERE PROD_ID=prodId 
AND BRANCH_ID=branchid;
new_id:=UPPER(id_prefix)||id_currvalue;
return new_id;
END;
$BODY$;

CREATE OR REPLACE PROCEDURE glupdate(IN frm_date timestamp without time zone, IN brcode text, IN achdid text)
 LANGUAGE plpgsql
 SECURITY DEFINER
AS $procedure$
DECLARE
 /*
  EXECUTE GLUPDATE ('31-MAR-2014','0002','1181001085');
  */
    Start_date timestamp := FRM_date;
    BALTYPE varchar(20);
    End_date timestamp;
    CNT integer;
    ACBALTYPE varchar(20);
    BTPYPE varchar(20);
BEGIN
	DELETE from GL_ABSTRACT WHERE DT > FRM_date  AND BRANCH_CODE=brcode
		AND  AC_HD_ID =ACHDID;
	SELECT CURR_APPL_DT INTO STRICT End_date FROM DAY_END WHERE BRANCH_CODE=brcode;
	SELECT MJR_AC_HD_TYPE ,CASE WHEN MJ.mjr_ac_hd_type='LIABILITY' THEN 'CREDIT' WHEN MJ.mjr_ac_hd_type='INCOME' THEN 'CREDIT'  ELSE 'DEBIT' END   INTO STRICT BALTYPE ,BTPYPE
		FROM MJR_AC_HD MJ WHERE MJ.MJR_AC_HD_ID = (SELECT A.MJR_AC_HD_ID FROM AC_HD A WHERE A.AC_HD_ID =ACHDID);
	SELECT COUNT(*) INTO STRICT CNT from GL_ABSTRACT WHERE DT = Start_date  AND BRANCH_CODE=brcode  AND  AC_HD_ID =ACHDID;
	IF ( (CNT) =0) THEN
		INSERT INTO  GL_ABSTRACT(AC_HD_ID, OPN_BAL, CLOSE_BAL, BRANCH_CODE, DT, BALANCE_TYPE) 
			SELECT ACHDID,0,0,brcode,Start_date ,(CASE WHEN(BALTYPE ='ASSETS' OR BALTYPE ='EXPENDITURE') THEN 'DEBIT' 
			ELSE  'CREDIT' END);
	END IF;
	SELECT G.BALANCE_TYPE INTO STRICT ACBALTYPE FROM  GL_ABSTRACT G WHERE G.DT=Start_date  AND BRANCH_CODE=brcode  AND  AC_HD_ID =ACHDID;
   	WHILE Start_date< End_date
    LOOP   
       Start_date := Start_date + 1 ;
       --(BALTYPE ='INCOME' OR BALTYPE ='LIABILITY')   
       INSERT INTO  GL_ABSTRACT(AC_HD_ID, OPN_BAL, CLOSE_BAL, BRANCH_CODE, DT, BALANCE_TYPE)
       SELECT GLA.AC_HD_ID, GLA.CLOSE_BAL, CASE WHEN ACBALTYPE =BTPYPE THEN
        (CASE WHEN(BALTYPE ='ASSETS' OR BALTYPE ='EXPENDITURE') THEN 
        ((coalesce(GLA.CLOSE_BAL,0)+coalesce(TR.DR,0))-coalesce(TR.CR,0))
        ELSE   ((coalesce(GLA.CLOSE_BAL,0)+coalesce(TR.CR,0))-coalesce(TR.DR,0))  END) ELSE 
        (CASE WHEN ACBALTYPE = 'DEBIT' THEN 
        ((coalesce(GLA.CLOSE_BAL,0)+coalesce(TR.DR,0))-coalesce(TR.CR,0))
        ELSE   ((coalesce(GLA.CLOSE_BAL,0)+coalesce(TR.CR,0))-coalesce(TR.DR,0))  END) END  
       ,GLA.BRANCH_CODE, Start_date  , CASE WHEN ACBALTYPE =BTPYPE THEN (CASE WHEN(BALTYPE ='ASSETS' OR BALTYPE ='EXPENDITURE') THEN 'DEBIT' 
        ELSE  'CREDIT' END) ELSE   ACBALTYPE END 
         AS BALANCE_TYPE
        from GL_ABSTRACT GLA
         LEFT OUTER JOIN(SELECT AC_HD_ID,SUM(coalesce(CR,0)) AS CR ,
            SUM(coalesce(DR,0)) AS DR    
             FROM ALL_GRP_TRN_NEW WHERE TRANS_DT  = Start_date AND BRANCH_ID =brcode AND AC_HD_ID =ACHDID    
               AND  AC_HD_ID NOT IN (SELECT CASH_AC_HD FROM PARAMETERS 
             ) GROUP BY AC_HD_ID) TR ON TR.AC_HD_ID = GLA.AC_HD_ID
        WHERE GLA.DT = Start_date-1 AND GLA.BRANCH_CODE=brcode AND  GLA.AC_HD_ID NOT IN (SELECT CASH_AC_HD FROM PARAMETERS 
        )  AND GLA.AC_HD_ID =ACHDID;
    END LOOP;
	UPDATE  GL_ABSTRACT SET OPN_BAL =ABS(OPN_BAL)  WHERE DT > FRM_date  AND BRANCH_CODE=brcode 
		AND  AC_HD_ID =ACHDID::text AND OPN_BAL < 0;  
	UPDATE  GL_ABSTRACT SET  CLOSE_BAL = ABS(CLOSE_BAL), BALANCE_TYPE ='DEBIT' WHERE DT > FRM_date  
		AND BRANCH_CODE=brcode   AND  AC_HD_ID =ACHDID::text AND CLOSE_BAL < 0 AND BALANCE_TYPE='CREDIT';
	UPDATE  GL_ABSTRACT SET  CLOSE_BAL = ABS(CLOSE_BAL), BALANCE_TYPE ='CREDIT' 
		WHERE DT > FRM_date  AND BRANCH_CODE=brcode   AND  AC_HD_ID =ACHDID::text AND CLOSE_BAL < 0 AND BALANCE_TYPE='DEBIT';
    DELETE FROM GL WHERE BRANCH_CODE = brcode AND AC_HD_ID = ACHDID;
    insert into  GL(ac_hd_id,opn_bal,cur_bal,branch_code,last_trans_dt,authorize_status,balance_type)
		(SELECT ac_hd_id,opn_bal,CLOSE_BAL,branch_code,dt,'AUTHORIZED',balance_type from GL_ABSTRACT
		 WHERE DT =(SELECT CURR_APPL_DT FROM DAY_END WHERE BRANCH_CODE=brcode) and BRANCH_CODE=brcode AND AC_HD_ID = ACHDID  );
	UPDATE GL_ABSTRACT SET  CLOSE_BAL  = NULL WHERE BRANCH_CODE =brcode AND AC_HD_ID =ACHDID AND DT=(
        SELECT CURR_APPL_DT FROM DAY_END WHERE BRANCH_CODE=brcode);
    --- WHOLE BRANCHES VALUE IS UPDATED IN THIS TABLE
  	UPDATE  AC_HD_PARAM  SET 
		BALANCETYPE =(SELECT (CASE WHEN(MJR_AC_HD_TYPE ='ASSETS' OR MJR_AC_HD_TYPE='EXPENDITURE') THEN 'DEBIT'
		ELSE 'CREDIT' END)   from MJR_AC_HD where  MJR_AC_HD_ID = (SELECT A.MJR_AC_HD_ID FROM  AC_HD A WHERE A.AC_HD_ID =AC_HD_PARAM.AC_HD_ID)) ,
		GLBALANCE= ( SELECT SUM(CUR_BAL) FROM GL WHERE  AC_HD_ID = ACHDID)
		where AC_HD_ID =ACHDID;
     UPDATE  AC_HD_PARAM  SET    
     	BALANCETYPE = ( CASE WHEN BALANCETYPE='DEBIT' THEN 'CREDIT' ELSE 'DEBIT'
                         END )  WHERE GLBALANCE <0 AND AC_HD_ID = ACHDID;
 END;
$procedure$
;

CREATE OR REPLACE FUNCTION oracle.nvl(anyelement, anyelement)
 RETURNS anyelement
 LANGUAGE c
 IMMUTABLE
AS '$libdir/orafce', $function$ora_nvl$function$
;

CREATE OR REPLACE FUNCTION get_future_service(
	custid text)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    STABLE SECURITY DEFINER PARALLEL UNSAFE
AS $BODY$
DECLARE

DT_OFBIRTH timestamp;
DT_OFJOIN timestamp;
RETIREAGE smallint;
RETIREDATE timestamp;
FUTURE_SERVICE varchar(30);
YRS bigint;
MNTHS bigint;
DYS bigint;
CURRDATE timestamp;

BEGIN
SELECT C.DOB,C.JOINING_DATE INTO STRICT DT_OFBIRTH,DT_OFJOIN  FROM CUSTOMER  C WHERE  C.CUST_ID = CUSTID;

SELECT DE.CURR_APPL_DT INTO STRICT CURRDATE FROM DAY_END DE WHERE DE.BRANCH_CODE = '0001';

SELECT P.RETIREMENT_AGE INTO STRICT RETIREAGE  FROM PARAMETERS P;

SELECT GET_RETIRE_DATE(CUSTID) INTO STRICT RETIREDATE;

SELECT trunc(MONTHS_BETWEEN(RETIREDATE, CURRDATE) / 12) YRS,
trunc(MOD(MONTHS_BETWEEN(RETIREDATE, CURRDATE), 12)) MNTHS,
trunc(RETIREDATE - ADD_MONTHS(CURRDATE, TRUNC(MONTHS_BETWEEN(RETIREDATE, CURRDATE)))) DYS INTO STRICT YRS,MNTHS,DYS;

 IF YRS IS NULL THEN
  YRS := 0;
 END IF;
 IF MNTHS IS NULL THEN
 MNTHS := 0;
 END IF;
 IF DYS IS NULL  THEN
 DYS := 0;
 END IF;
 FUTURE_SERVICE := YRS::varchar||'Years'||'  '||MNTHS::varchar||'Months'||'  '||DYS::varchar||'Days';
 RETURN FUTURE_SERVICE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.add_months(
	timestamp with time zone,
	numeric)
    RETURNS timestamp without time zone
    LANGUAGE 'sql'
    COST 100
    IMMUTABLE STRICT PARALLEL UNSAFE
AS $BODY$
 SELECT (pg_catalog.add_months($1::pg_catalog.date, $2::integer) + $1::time)::oracle.date; 
$BODY$;

CREATE OR REPLACE FUNCTION pudukkad.get_pfinterest(fromdate timestamp, todate timestamp, empid character varying, paycode character varying, roi numeric)
 RETURNS numeric
 LANGUAGE plpgsql
 STABLE SECURITY DEFINER
AS $function$
DECLARE

   INTEREST double precision :=0;
   EMPNAME varchar(200);
   dedname varchar(200);

BEGIN
   /* Function Modified by Prasanth K G now the Data taking from Payroll because pay pf trans data was wrong.*/

   SELECT EM.EMPLOYEE_NAME INTO STRICT EMPNAME  FROM EMPLOYEE_MASTER EM WHERE EM.EMPLOYEEID =EmpId;
   SELECT P.PAY_DESCRI INTO STRICT dedname FROM PAY_DEDUCTIONS P WHERE P.PAY_CODE = payCode;
 SELECT SUM(INTAMT) into STRICT INTEREST FROM(
SELECT TRANS_DT,coalesce(LEAD(TRANS_DT) OVER (ORDER BY TRANS_DT), toDate) AS NEXTDT,
       SUM(AMOUNT) OVER (ORDER BY TRANS_DT) AS AMOUNT,
         (  SUM(AMOUNT) OVER (ORDER BY TRANS_DT)
          * roi
          * (coalesce(LEAD(TRANS_DT) OVER (ORDER BY TRANS_DT), toDate) - TRANS_DT))
       / 36400.00
          AS INTAMT,
       coalesce(LEAD(TRANS_DT) OVER (ORDER BY TRANS_DT), toDate) - TRANS_DT
          AS DTDIFF From(  
SELECT GET_PAYDED_OPBAL(EMPNAME, dedname, Fromdate) AS AMOUNT,
               TO_DATE(Fromdate)-1 AS TRANS_DT
          

Union ALL

    select SUM(CREDIT -DEBIT ) AS AMOUNT,trans_dt from (
 SELECT EM.EMPLOYEEID,EM.EMPLOYEE_NAME,0 AS WF_OPENING_BALANCE,
 PAY_DEDUCTIONS.PAY_DESCRI,PAY_DEDUCTIONS.PAY_MODULE_TYPE, T.TRANS_ID  AS TRANS_ID,
 T.TRANS_DT,T.TRANS_TYPE,CASE WHEN T.TRANS_TYPE = 'CREDIT' THEN T.AMOUNT ELSE 0 END AS CREDIT,
 CASE WHEN T.TRANS_TYPE = 'DEBIT' THEN T.AMOUNT ELSE 0 END AS DEBIT,T.PARTICULARS,BANK_TITLE(),WF_NUMBER,ATYP.ACTYPE
  FROM CASH_TRANS T 
 JOIN PAY_DEDUCTIONS ON T.AC_HD_ID = PAY_DEDUCTIONS.PAY_ACC_NO
 JOIN (SELECT AC_HD_ID ,(CASE WHEN(MJR_AC_HD_TYPE ='ASSETS' OR MJR_AC_HD_TYPE ='EXPENDITURE' ) THEN 'A' ELSE 'L' END) AS ACTYPE
 FROM  MJR_AC_HD JOIN AC_HD ON MJR_AC_HD.MJR_AC_HD_ID = AC_HD.MJR_AC_HD_ID) ATYP ON ATYP.AC_HD_ID =T.AC_HD_ID 
 JOIN EMPLOYEE_MASTER EM ON T.GL_TRANS_ACT_NUM = EM.EMPLOYEEID
 WHERE EM.EMPLOYEE_NAME =  EmpName AND PAY_DEDUCTIONS.PAY_DESCRI = DedName AND UPPER(PAY_DEDUCTIONS.ACC_TYPE)='CREDIT'
 AND T.TRANS_DT >= FromDate and T.TRANS_DT <= ToDate
 AND T.AUTHORIZE_STATUS = 'AUTHORIZED' AND T.STATUS NOT IN ('DELETED')
 
UNION ALL

 SELECT EM.EMPLOYEEID,EM.EMPLOYEE_NAME,0 AS WF_OPENING_BALANCE,
PAY_DEDUCTIONS.PAY_DESCRI,PAY_DEDUCTIONS.PAY_MODULE_TYPE, T.TRANS_ID  AS TRANS_ID,
 T.TRANS_DT,T.TRANS_TYPE,CASE WHEN T.TRANS_TYPE = 'CREDIT' THEN T.AMOUNT ELSE 0 END AS CREDIT,
 CASE WHEN T.TRANS_TYPE = 'DEBIT' THEN T.AMOUNT ELSE 0 END AS DEBIT,T.PARTICULARS,BANK_TITLE(),WF_NUMBER,ATYP.ACTYPE
  FROM TRANSFER_TRANS T 
 JOIN PAY_DEDUCTIONS ON T.AC_HD_ID = PAY_DEDUCTIONS.PAY_ACC_NO
 JOIN (SELECT AC_HD_ID ,(CASE WHEN(MJR_AC_HD_TYPE ='ASSETS' OR MJR_AC_HD_TYPE ='EXPENDITURE' ) THEN 'A' ELSE 'L' END) AS ACTYPE
 FROM  MJR_AC_HD JOIN AC_HD ON MJR_AC_HD.MJR_AC_HD_ID = AC_HD.MJR_AC_HD_ID) ATYP ON ATYP.AC_HD_ID =T.AC_HD_ID 
 JOIN EMPLOYEE_MASTER EM ON T.GL_TRANS_ACT_NUM = EM.EMPLOYEEID
 WHERE EM.EMPLOYEE_NAME =  EmpName AND PAY_DEDUCTIONS.PAY_DESCRI = DedName AND UPPER(PAY_DEDUCTIONS.ACC_TYPE)='CREDIT'
 AND T.TRANS_DT >= FromDate and T.TRANS_DT <= ToDate
 AND T.AUTHORIZE_STATUS = 'AUTHORIZED' AND T.STATUS NOT IN ('DELETED')) alias29
 Group by trans_dt) a) b;
   IF  INTEREST < 0 THEN
   INTEREST :=0;
   END IF;
   RETURN INTEREST;
   END;
$function$
;

CREATE OR REPLACE FUNCTION public.decode(bigint, integer, text, text)
 RETURNS text
 LANGUAGE c
 IMMUTABLE
AS '$libdir/orafce', $function$ora_decode$function$
;


CREATE OR REPLACE FUNCTION addmonths(deposit_dt timestamp without time zone, pass_dt timestamp without time zone, noofmonths numeric)
 RETURNS date
 LANGUAGE plpgsql
AS $function$
declare
temp_dt date;
begin
temp_dt:=add_months(pass_dt,noofmonths);
if to_number(to_char(deposit_dt,'dd'))>to_number(to_char(last_day(temp_dt),'dd')) then
temp_dt:=last_day(temp_dt);
else
temp_dt:=to_date(to_char(deposit_dt,'dd')||'-'||to_char(temp_dt,'mm')||'-'||to_char(temp_dt,'yyyy'), 'dd-mm-yyyy');
end if;
--dbms_output.put_line('inside else:'||deposit_dt||' / temp_dt'||temp_dt);
return temp_dt;
end
$function$
;

CREATE OR REPLACE FUNCTION check_mds_auth_pending(
	chitalno text,
	scheme text)
    RETURNS bigint
    LANGUAGE 'plpgsql'
    COST 100
    STABLE SECURITY DEFINER PARALLEL UNSAFE
AS $BODY$
DECLARE

   ACT_COUNT   double precision := 0;

BEGIN
   SELECT COALESCE(COUNT(*), 0) into ACT_COUNT
FROM MDS_RECEIPT_ENTRY
WHERE STATUS != 'DELETED'
    AND AUTHORIZE_STATUS IS NULL
    AND SCHEME_NAME =SCHEME
    AND CHITTAL_NO = CHITALNO
GROUP BY CHITTAL_NO
--ORDER BY CHITTAL_NO
UNION ALL
SELECT 0 AS ACT_COUNT
WHERE NOT EXISTS (
    SELECT 1
    FROM MDS_RECEIPT_ENTRY
    WHERE STATUS != 'DELETED'
        AND AUTHORIZE_STATUS IS NULL
        AND SCHEME_NAME = SCHEME
        AND CHITTAL_NO = CHITALNO
);

 
   RETURN ACT_COUNT;
END;
$BODY$;

CREATE OR REPLACE FUNCTION PKGREPORTS.getcustidbyaccountno(
	acctno text)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    STABLE SECURITY DEFINER PARALLEL UNSAFE
AS $BODY$
DECLARE

    Custid varchar(100);

BEGIN
select custid INTO STRICT Custid
from (
SELECT b.CUST_ID custid
  FROM customer a
  JOIN act_master b
    ON a.cust_id = b.cust_id
 WHERE b.act_num = Acctno

union all

  SELECT b.CUST_ID custid
  FROM customer a
  JOIN LOANS_BORROWER b  
    ON a.cust_id = b.CUST_ID
  join loans_facility_details c
  on b.BORROW_NO=c.BORROW_NO
 WHERE c.ACCT_NUM = Acctno
  
UNION ALL

SELECT b.CUST_ID custid
  FROM customer a
  JOIN deposit_acinfo b
    ON a.cust_id = b.CUST_ID
 WHERE b.DEPOSIT_NO = Acctno
 
union all

    SELECT b.SUSPENSE_CUSTOMER_ID custid
  FROM customer a
  JOIN SUSPENSE_ACCOUNT_MASTER b
    ON a.cust_id = b.SUSPENSE_CUSTOMER_ID
 WHERE b.SUSPENSE_ACCT_NUM =Acctno
 ) alias0;
EXCEPTION
      WHEN OTHERS THEN
    RETURN custid;
END;
$BODY$;

CREATE OR REPLACE FUNCTION pkgreports.getcustfullname(custid text, reptype bigint)
 RETURNS character varying
 LANGUAGE plpgsql
 SECURITY DEFINER
AS $function$
DECLARE

    CustName varchar(500);

BEGIN
    BEGIN
         SELECT case when 1=Reptype then CASE WHEN cust_type='INDIVIDUAL' THEN  fname  ELSE comp_name END  ||' ' ||MNAME||' ' ||LNAME  -- full name
                     when 2=Reptype then CASE WHEN cust_type='INDIVIDUAL' THEN  fname  ELSE comp_name END  -- first name
                     when 3=Reptype then MNAME -- middle name
                     when 4=Reptype then LNAME -- last name   
                     when 5=Reptype then CASE WHEN cust_type='INDIVIDUAL' THEN  fname  ELSE comp_name END  ||' ' ||MNAME||' ' ||LNAME ||' '||pkgreports.fngetrelationtype(CUST_ID)||' '||CARE_OF_NAME --- with care of name
                 end as CustName into STRICT CustName
            from customer where CUST_ID=Custid;
    EXCEPTION WHEN OTHERS THEN CustName=NULL;
    END;
    RETURN CustName;
END;

$function$
;

CREATE OR REPLACE FUNCTION GET_TL_LAST_INTERESTDATE(ACTNUM character varying,ASONDT DATE,TRANSAMT numeric )

 RETURNS DATE
    LANGUAGE 'plpgsql'
    COST 100
    STABLE SECURITY DEFINER PARALLEL UNSAFE
AS $BODY$
DECLARE

/* DECLARE

  ---COLINTAMT NUMBER,
 ACTNUM VARCHAR2(15) := '00012640000H4';
 COLINTAMT NUMBER:= 4402;
 ASONDT DATE :='27-FEB-2015';
 */
   
  STARTDATE DATE;
  LSTINTRCALCDATE DATE;
  BALAMT numeric;   
  PAYBLEAMT numeric;
  ROI numeric;
  UNAUTH INT; 
  INTRAMT numeric;    
  BEGIN
    SELECT  MAX(T.TRANS_DT)  INTO STARTDATE   FROM LOAN_TRANS_DETAILS T   WHERE T.ACT_NUM = ACTNUM AND T.TRANS_DT <= ASONDT AND PRINCIPLE>0;
    SELECT   PBAL   INTO BALAMT   FROM LOAN_TRANS_DETAILS    WHERE ACT_NUM = ACTNUM AND  TRANS_DT <= ASONDT
    AND TRANS_SLNO =(SELECT MAX(TRANS_SLNO) FROM LOAN_TRANS_DETAILS    WHERE ACT_NUM = ACTNUM AND  TRANS_DT <= ASONDT);    

    SELECT GET_TL_INTEREST(ACTNUM,STARTDATE::DATE -1::INTEGER,ASONDT ) INTO PAYBLEAMT FROM DUAL;
      
    --DBMS_OUTPUT.PUT_LINE('STARTDATE : ' ||STARTDATE);
       SELECT CASE WHEN LFD.INT_GET_FROM = 'ACT' THEN
                    (SELECT MAX(LIM.INTEREST) FROM LOANS_INT_MAINTENANCE LIM WHERE  
                     LIM.ACCT_NUM = LFD.ACCT_NUM AND LIM.STATUS != 'DELETED'
                     AND FROM_DT =(SELECT MAX(FROM_DT) FROM LOANS_INT_MAINTENANCE LIM WHERE  
                     LIM.ACCT_NUM = LFD.ACCT_NUM AND LIM.STATUS != 'DELETED') )
                WHEN LFD.INT_GET_FROM = 'PROD' THEN
                     GET_TL_ROI(LFD.PROD_ID,LSD.FROM_DT,LSD.LIMIT)
                ELSE 0 END INTO ROI
    FROM LOANS_FACILITY_DETAILS LFD
    JOIN LOANS_SANCTION_DETAILS LSD ON LFD.BORROW_NO = LSD.BORROW_NO   WHERE ACCT_NUM = ACTNUM;
 
  --DBMS_OUTPUT.PUT_LINE ( ' INTEREST : ' || ROI);
  
   IF NVL(BALAMT,0)>0 THEN
      SELECT NVL(SUM(AMOUNT),0) INTO INTRAMT    FROM ALL_TRANS AT 
      JOIN LOANS_PROD_ACHD LPA  ON LPA.PROD_ID = SUBSTR(ACTNUM,5,3)   AND AT.AC_HD_ID = AC_DEBIT_INT  
      WHERE TRANS_DT =ASONDT AND  LINK_BATCH_ID = ACTNUM AND  NVL(AT.AUTHORIZE_STATUS,' ')!='AUTHORIZED' AND NVL(STATUS,' ')!='DELETED';   

    SELECT   ASONDT - (  CEIL(( ((PAYBLEAMT - (INTRAMT+TRANSAMT)) * 36500) / (BALAMT *  ROI)))) INTO LSTINTRCALCDATE   FROM DUAL;
   ELSE
      SELECT   ASONDT   INTO LSTINTRCALCDATE   FROM DUAL;
   END IF ;
     RETURN LSTINTRCALCDATE;
   -- DBMS_OUTPUT.PUT_LINE( ' LSTINTRCALCDATE : ' || LSTINTRCALCDATE);
  
  END;
$BODY$;

CREATE OR REPLACE FUNCTION pkg_app_exec.act_int_validate(
	brcode text,
	prodid text,
	frdt timestamp without time zone,
	todt timestamp without time zone,
	usrnam text)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    STABLE SECURITY DEFINER PARALLEL UNSAFE
AS $BODY$
DECLARE

  --  SELECT PKG_APP_EXEC. pkg_app_exec.act_int_validate('0001','106','01APR2018','28feb2019','admin') FROM DUAL;
  MSG        varchar(500);
  CNT        integer;
  PRNACHD    varchar(15);
  INTACHD    varchar(15);
  STARTDY    integer;
  MININTAMT  integer;
  INTID      varchar(15);
  VALDATE    integer:=1;
  RT         real;
  MINCALAMT  real;
  PRODFREQINTPAY integer;
  INFRDT     timestamp;
  INTODT     timestamp;

 
BEGIN 
 
    Select  OP.AC_HD_ID,CAST(OI.STARTDAY_PROD_CALC AS integer),CAST(OI.MIN_CR_INT_AMT  AS integer),OPP.CREDIT_INT,OI.MIN_BAL_FOR_INT_CALC, OI.PRODUCT_FREQ_INT_PAY
	INTO STRICT PRNACHD,STARTDY,MININTAMT,INTACHD,MINCALAMT ,PRODFREQINTPAY  
    FROM  OP_AC_PRODUCT OP  
    JOIN OP_AC_ACHEAD_PARAM OPP ON OPP.PROD_ID=OP.PROD_ID 
    JOIN OP_AC_INTPAY_PARAM OI ON OI.PROD_ID =OP.PROD_ID
    WHERE OP.PROD_ID=PRODID;

    select   ROI_GROUP_ID INTO STRICT  INTID  from DEPOSIT_ROI_GROUP_PROD WHERE PROD_ID =PRODID;

    SELECT ROI  INTO STRICT  RT FROM (SELECT   ROI,RI.ROI_DATE,MAX(RI.ROI_DATE) OVER (ORDER BY RI.ROI_DATE) AS MAXRDT   FROM  DEPOSIT_ROI_GROUP_TYPE_RATE RI WHERE RI.ROI_GROUP_ID =INTID AND RI.ROI_DATE  <= FRDT)
    RTI WHERE RTI.MAXRDT =RTI.ROI_DATE;

    IF coalesce(RT,0)=0 THEN
      VALDATE :=0;
       MSG :='Please set interest rate for this product '||Chr(10);
    else
      MSG := 'Interest Rate ' ||RT || ' '|| Chr(10);
    END IF;

    SELECT COUNT(*) INTO STRICT CNT  FROM ACT_INTR_CALC_ACT WHERE    DT =FRDT AND   TDT = TODT AND BRANCH_ID = BRCODE AND POSTED =1 AND PROD_ID = PRODID;

    IF CNT >0 THEN 
      VALDATE :=0;
      MSG :=MSG|| trim(both CNT::varchar) ||' Records Already Posted for the Period ';
    END IF;
 
        SELECT COUNT(*) INTO STRICT  CNT  FROM (
     SELECT   DN.LAST_APPL_DT+1 AS FRDT,DN.LAST_APPL_DT + CASE WHEN( CR_INT_APPL_FREQ =30) THEN 1 WHEN( CR_INT_APPL_FREQ =90) THEN 3 WHEN( CR_INT_APPL_FREQ =180) THEN 6 WHEN( CR_INT_APPL_FREQ >360 AND 
     CR_INT_APPL_FREQ <=366) THEN 12 END*'1 month'::interval  AS  TODT,DN.BRANCH_CODE,DN.PROD_ID,DN.LAST_APPL_DT  FROM ( SELECT    DP.BRANCH_CODE,DP.PROD_ID,LAST_APPL_DT, OI.CR_INT_APPL_FREQ ,MAX(LAST_APPL_DT) OVER (PARTITION BY  DP.BRANCH_CODE,DP.PROD_ID
     ORDER BY  DP.BRANCH_CODE,DP.PROD_ID ) AS MXINTAPP  FROM DEPOSIT_PROVISION DP,OP_AC_INTPAY_PARAM OI 
          WHERE DP.PROD_ID =OI.PROD_ID  AND DP.REMARKS ='APPLICATION')DN   WHERE     DN.LAST_APPL_DT = DN.MXINTAPP )PR          
          WHERE    BRANCH_CODE = BRCODE AND PROD_ID =PRODID;
    IF CNT =0 THEN
         VALDATE :=0;
         MSG :=MSG||  'Interest calculation period not set. Check CR_INT_APPL_FREQ (OP_AC_INTPAY_PARAM) And Last Record DEPOSIT_PROVISION '||  Chr(10);
    ELSE
     SELECT PR.FRDT,PR.TODT  INTO STRICT  INFRDT ,INTODT  FROM (
     SELECT   DN.LAST_APPL_DT+1 AS FRDT,DN.LAST_APPL_DT + CASE WHEN( CR_INT_APPL_FREQ =30) THEN 1 WHEN( CR_INT_APPL_FREQ =90) THEN 3 WHEN( CR_INT_APPL_FREQ =180) THEN 6 WHEN( CR_INT_APPL_FREQ >360 AND 
     CR_INT_APPL_FREQ <=366) THEN 12 END*'1 month'::interval  AS  TODT,DN.BRANCH_CODE,DN.PROD_ID,DN.LAST_APPL_DT  FROM ( SELECT    DP.BRANCH_CODE,DP.PROD_ID,LAST_APPL_DT, OI.CR_INT_APPL_FREQ ,MAX(LAST_APPL_DT) OVER (PARTITION BY  DP.BRANCH_CODE,DP.PROD_ID
     ORDER BY  DP.BRANCH_CODE,DP.PROD_ID ) AS MXINTAPP  FROM DEPOSIT_PROVISION DP,OP_AC_INTPAY_PARAM OI 
          WHERE DP.PROD_ID =OI.PROD_ID  AND DP.REMARKS ='APPLICATION')DN   WHERE     DN.LAST_APPL_DT = DN.MXINTAPP )PR          
          WHERE    BRANCH_CODE = BRCODE AND PROD_ID =PRODID;
     IF  INTODT > TODT THEN
        VALDATE :=0;
         MSG :=MSG||  'Invalid Interest calculation period = '|| TO_CHAR(INFRDT ,'DD/MM/YYYY'  )||' - '||TO_CHAR(INTODT  ,'DD/MM/YYYY'   ) || ' Check CR_INT_APPL_FREQ (OP_AC_INTPAY_PARAM) And Last Record DEPOSIT_PROVISION '||  Chr(10);
     ELSE        
        
       MSG :=MSG||  'Interest calculation period =' || TO_CHAR(INFRDT ,'DD/MM/YYYY'  )||' - '||TO_CHAR(INTODT  ,'DD/MM/YYYY'   ) ||     Chr(10);
     END IF;
    END IF;

  --  || TO_CHAR(INFRDT ,'DD/MM/YYYY'  )||', '||TO_CHAR(INTODT  ,'DD/MM/YYYY'   ) ||''
    
    
    SELECT COUNT(*) INTO STRICT CNT  FROM AC_HD WHERE AC_HD_ID =coalesce(PRNACHD,'');

    IF  CNT=0   THEN
      VALDATE :=0;
       MSG :=MSG||  'Please set Valid Princpal A/c head for this product '|| Chr(10);
    END IF;
    SELECT COUNT(*) INTO STRICT CNT  FROM AC_HD WHERE AC_HD_ID =coalesce(INTACHD,'');

    IF CNT=0 THEN
      VALDATE :=0;
       MSG :=MSG||  'Please set Valid Interest A/c head(CREDIT_INT) for this product in "OP_AC_ACHEAD_PARAM" '|| Chr(10);
    END IF;

  
    IF coalesce(STARTDY::integer,0)=0 AND coalesce(PRODFREQINTPAY::integer,0) = 30 THEN
      VALDATE :=0;
       MSG :=MSG||  'Please set Start day(STARTDAY_PROD_CALC) for this product in "OP_AC_INTPAY_PARAM" '|| Chr(10);
    ELSIF coalesce(PRODFREQINTPAY::integer,0) = 30 THEN
       MSG := MSG|| 'Interest Calculation Start Day = '  ||STARTDY ||  Chr(10);
    END IF;

   -- IF NVL(MININTAMT,0)=0 THEN       
       MSG :=MSG||  'Minumum int Amt IS  ' ||coalesce(MININTAMT,0) || Chr(10);
    --END IF;        
     
       MSG :=MSG||  'Minumum Amt For calculation ' || coalesce(MINCALAMT,0 ) ||  Chr(10);
  --- MAX PAGE NO AND SL NO IN PASS_BOOK ARE INVALID
       CNT:=0;
       SELECT COUNT(AR.ACT_NUM) INTO STRICT CNT
            FROM ACT_MASTER AR
            LEFT JOIN  (SELECT 1 AS TAG,ACT_NUM,SLNO,TRANS_DT,PAGENO,PARTICULARS,DEBIT,CREDIT,BALANCE,'' AS TRID FROM ( 
            SELECT PK.ACT_NUM,TRANS_DT,PAGENO,SLNO,PARTICULARS,0 AS DEBIT,BALANCE AS CREDIT,BALANCE ,MAX(TRANS_DT) OVER (PARTITION BY PK.ACT_NUM ) AS MXTRANSDT,MAX(PAGENO) OVER (PARTITION BY PK.ACT_NUM ) AS MXPAGENO,
             MAX((PAGENO*100) + SLNO) OVER (PARTITION BY PK.ACT_NUM ) AS MXSLNO    
           FROM  pass_book PK,ACT_MASTER AM WHERE AM.PROD_ID=PRODID AND  AM.ACT_NUM =PK.ACT_NUM AND AM.STATUS NOT IN ('DELETED') AND (AM.AUTHORIZATION_STATUS  IS NULL OR
            AM.AUTHORIZATION_STATUS  NOT IN ('REJECTED')  )
           AND  PK.AUTHORIZE_STATUS NOT IN ('REJECTED')  AND PK.STATUS NOT IN ('DELETED') ) PB
           WHERE  PB.TRANS_DT= PB.MXTRANSDT AND PB.PAGENO=PB.MXPAGENO AND ((PB.PAGENO*100)+PB.SLNO)= PB.MXSLNO)POK
           ON POK.ACT_NUM =AR.ACT_NUM
           WHERE AR.PROD_ID=PRODID AND AR.STATUS NOT IN ('DELETED') AND (AR.AUTHORIZATION_STATUS  IS NULL OR AR.AUTHORIZATION_STATUS  NOT IN ('REJECTED'))
           AND  POK.ACT_NUM IS NULL AND EXISTS (SELECT * FROM  pass_book PS WHERE    PS.ACT_NUM = AR.ACT_NUM );
   
    IF CNT>0 THEN
      VALDATE :=0;
       MSG :=MSG||  'Invalid PAGENO and SLNO in "PASS_BOOK" table ' || trim(both CNT::varchar) || Chr(10);
    END IF;
 
    IF coalesce(PRODFREQINTPAY,0) not in (1,30) THEN
      VALDATE :=0;
      MSG :=MSG||  'invalid Product int pay Fequency(PRODUCT_FREQ_INT_PAY) for this product in "OP_AC_INTPAY_PARAM" '|| Chr(10);
    ELSIF coalesce(PRODFREQINTPAY,0) = 1 THEN
      MSG :=MSG||  'Interest Calculation Method = Daily Interest ';
    ELSIF coalesce(PRODFREQINTPAY,0) = 30 THEN
      MSG :=MSG||  'Interest Calculation Method = Monthly Minimum Balance  ';
      
    END IF;
    MSG :=VALDATE ||'-'|| MSG;
   EXCEPTION                                         
   WHEN NO_DATA_FOUND
   THEN
     RETURN MSG;
 END;

 

$BODY$;

CREATE OR REPLACE FUNCTION get_standing_amt(
	prodid text,
	prodtype text,
	actnum text,
	instno numeric,
	debitamt numeric)
    RETURNS numeric
    LANGUAGE 'plpgsql'
    COST 100
    STABLE SECURITY DEFINER PARALLEL UNSAFE
AS $BODY$
DECLARE

   DEBITAMT1 double precision := 0;
   REPAYMENTTYPE varchar(20);

BEGIN
   IF PRODTYPE= 'TL' THEN
    SELECT P.REPAYMENT_TYPE INTO STRICT REPAYMENTTYPE  FROM LOANS_PRODUCT p WHERE P.prod_id=prodid;
    IF REPAYMENTTYPE='EMI'
    THEN SELECT GET_TL_INST(ACTNUM)*InstNo INTO STRICT DEBITAMT1;
    ELSE DEBITAMT1:=DEBITAMT;
    END IF;
   ELSE DEBITAMT1:=DEBITAMT;
    END IF;
    RETURN DEBITAMT1;
END;
$BODY$;

CREATE OR REPLACE FUNCTION pudukkad.calc_indend_closing_amt(depoid text, closedt timestamp without time zone, branchcode text, stocktype text)
 RETURNS bigint
 LANGUAGE plpgsql
 STABLE SECURITY DEFINER
AS $function$
DECLARE

 CLOSEAMT double precision;
 LASTYEARCLOSEAMT double precision;
 INDEND_PURCHASE_AMT double precision;
 SALE_PURCHASE_RETUTN1 double precision;
 SALE_PURCHASE_RETUTN2 double precision;
 LASTTEAREND_DT timestamp;

BEGIN
    SELECT CLOSEDT + -12*'1 month'::interval INTO STRICT LASTTEAREND_DT;

    SELECT coalesce(CLOSING_AMT,0) INTO STRICT LASTYEARCLOSEAMT FROM INDEND_CLOSE_STOCK CS WHERE CLOSING_DT=LASTTEAREND_DT AND STOCK_TYPE=STOCKTYPE
    AND DEPID=DEPOID AND BRANCH_CODE=BRANCHCODE;

    SELECT SUM(coalesce(AMOUNT,0)) INTO STRICT INDEND_PURCHASE_AMT  FROM INDEND_REGISTER WHERE STATUS!='DELETED' AND AUTHORIZE_STATUS='AUTHORIZED'
    AND TRANS_TYPE IN ('Purchase','Sales Return') AND TRAN_DATE BETWEEN LASTTEAREND_DT+1 AND CLOSEDT AND DEPID=DEPOID AND BRANCH_CODE=BRANCHCODE;

    SELECT SUM(coalesce(TT.AMOUNT,0)) INTO STRICT SALE_PURCHASE_RETUTN1 FROM ALL_TRANS TT WHERE TT.STATUS!='DELETED' 
    AND TT.AUTHORIZE_STATUS='AUTHORIZED' AND AC_HD_ID IN((SELECT DISTINCT PUR_RET_AC_HD_ID FROM DEPO_MASTER WHERE DEPOID=DEPOID)) 
    AND TRANS_DT BETWEEN LASTTEAREND_DT+1 AND CLOSEDT AND TT.BRANCH_ID=BRANCHCODE;
    
    SELECT SUM(coalesce(TT.AMOUNT,0)) INTO STRICT SALE_PURCHASE_RETUTN2 FROM ALL_TRANS TT WHERE TT.STATUS!='DELETED' 
    AND TT.AUTHORIZE_STATUS='AUTHORIZED' AND AC_HD_ID IN((SELECT DISTINCT SALES_AC_HD_ID FROM DEPO_MASTER WHERE DEPOID=DEPOID)) 
    AND TRANS_DT BETWEEN LASTTEAREND_DT+1 AND CLOSEDT AND TT.BRANCH_ID=BRANCHCODE;

    CLOSEAMT:=coalesce(LASTYEARCLOSEAMT,0)+coalesce(INDEND_PURCHASE_AMT,0)-coalesce(SALE_PURCHASE_RETUTN1,0)-coalesce(SALE_PURCHASE_RETUTN2,0);
    IF (CLOSEAMT<=0) THEN
        CLOSEAMT:=0;
    END IF;
   EXCEPTION                                         
   WHEN NO_DATA_FOUND
   THEN

   RETURN CLOSEAMT;
 END;
$function$
;

CREATE OR REPLACE PROCEDURE pudukkad.insertloanintproc(actnum text, from_dt timestamp without time zone, to_dt timestamp without time zone, 
		amt float8, no_of_days bigint, tot_product float8, int_amt float8, int_rate float8, prod_id text, int_calc_dt timestamp without time zone, 
		cust_id text, remarks text, curr_dt timestamp without time zone, branch_code text, user_id text, valudate_type text)
 LANGUAGE plpgsql
 SECURITY DEFINER
AS $procedure$
DECLARE
-- PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
    INSERT INTO loans_closing_int_tmp(act_num, from_dt, TO_DATE, amt, no_of_days, tot_product,
                int_amt, int_rate, prod_id, int_calc_dt, cust_id, remarks,
                curr_dt, branch_code, user_id,VALIDATE_TYPE
               )
        VALUES (actnum, from_dt, to_dt, amt, no_of_days, tot_product,
                int_amt, int_rate, prod_id, int_calc_dt, cust_id, remarks,
                curr_dt, branch_code, user_id,valudate_type
               );
   --COMMIT;
END;
$procedure$
;


CREATE OR REPLACE FUNCTION PKGREPORTS.getcustidbyaccountno(
	acctno text)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    STABLE SECURITY DEFINER PARALLEL UNSAFE
AS $BODY$
DECLARE

    Custid varchar(100);

BEGIN
select custid INTO STRICT Custid
from (
SELECT b.CUST_ID custid
  FROM customer a
  JOIN act_master b
    ON a.cust_id = b.cust_id
 WHERE b.act_num = Acctno

union all

  SELECT b.CUST_ID custid
  FROM customer a
  JOIN LOANS_BORROWER b  
    ON a.cust_id = b.CUST_ID
  join loans_facility_details c
  on b.BORROW_NO=c.BORROW_NO
 WHERE c.ACCT_NUM = Acctno
  
UNION ALL

SELECT b.CUST_ID custid
  FROM customer a
  JOIN deposit_acinfo b
    ON a.cust_id = b.CUST_ID
 WHERE b.DEPOSIT_NO = Acctno
 
union all

    SELECT b.SUSPENSE_CUSTOMER_ID custid
  FROM customer a
  JOIN SUSPENSE_ACCOUNT_MASTER b
    ON a.cust_id = b.SUSPENSE_CUSTOMER_ID
 WHERE b.SUSPENSE_ACCT_NUM =Acctno
 ) alias0;
EXCEPTION
      WHEN OTHERS THEN
    RETURN custid;
END;
$BODY$;

CREATE OR REPLACE FUNCTION pkgreports.getcustfullname(custid text, reptype bigint)
 RETURNS character varying
 LANGUAGE plpgsql
 SECURITY DEFINER
AS $function$
DECLARE

    CustName varchar(500);

BEGIN
    BEGIN
         SELECT case when 1=Reptype then CASE WHEN cust_type='INDIVIDUAL' THEN  fname  ELSE comp_name END  ||' ' ||MNAME||' ' ||LNAME  -- full name
                     when 2=Reptype then CASE WHEN cust_type='INDIVIDUAL' THEN  fname  ELSE comp_name END  -- first name
                     when 3=Reptype then MNAME -- middle name
                     when 4=Reptype then LNAME -- last name   
                     when 5=Reptype then CASE WHEN cust_type='INDIVIDUAL' THEN  fname  ELSE comp_name END  ||' ' ||MNAME||' ' ||LNAME ||' '||pkgreports.fngetrelationtype(CUST_ID)||' '||CARE_OF_NAME --- with care of name
                 end as CustName into STRICT CustName
            from customer where CUST_ID=Custid;
    EXCEPTION WHEN OTHERS THEN CustName=NULL;
    END;
    RETURN CustName;
END;

$function$
;

CREATE OR REPLACE FUNCTION GET_TL_LAST_INTERESTDATE(ACTNUM character varying,ASONDT DATE,TRANSAMT numeric )

 RETURNS DATE
    LANGUAGE 'plpgsql'
    COST 100
    STABLE SECURITY DEFINER PARALLEL UNSAFE
AS $BODY$
DECLARE

/* DECLARE

  ---COLINTAMT NUMBER,
 ACTNUM VARCHAR2(15) := '00012640000H4';
 COLINTAMT NUMBER:= 4402;
 ASONDT DATE :='27-FEB-2015';
 */
   
  STARTDATE DATE;
  LSTINTRCALCDATE DATE;
  BALAMT numeric;   
  PAYBLEAMT numeric;
  ROI numeric;
  UNAUTH INT; 
  INTRAMT numeric;    
  BEGIN
    SELECT  MAX(T.TRANS_DT)  INTO STARTDATE   FROM LOAN_TRANS_DETAILS T   WHERE T.ACT_NUM = ACTNUM AND T.TRANS_DT <= ASONDT AND PRINCIPLE>0;
    SELECT   PBAL   INTO BALAMT   FROM LOAN_TRANS_DETAILS    WHERE ACT_NUM = ACTNUM AND  TRANS_DT <= ASONDT
    AND TRANS_SLNO =(SELECT MAX(TRANS_SLNO) FROM LOAN_TRANS_DETAILS    WHERE ACT_NUM = ACTNUM AND  TRANS_DT <= ASONDT);    

    SELECT GET_TL_INTEREST(ACTNUM,STARTDATE::DATE -1::INTEGER,ASONDT ) INTO PAYBLEAMT FROM DUAL;
      
    --DBMS_OUTPUT.PUT_LINE('STARTDATE : ' ||STARTDATE);
       SELECT CASE WHEN LFD.INT_GET_FROM = 'ACT' THEN
                    (SELECT MAX(LIM.INTEREST) FROM LOANS_INT_MAINTENANCE LIM WHERE  
                     LIM.ACCT_NUM = LFD.ACCT_NUM AND LIM.STATUS != 'DELETED'
                     AND FROM_DT =(SELECT MAX(FROM_DT) FROM LOANS_INT_MAINTENANCE LIM WHERE  
                     LIM.ACCT_NUM = LFD.ACCT_NUM AND LIM.STATUS != 'DELETED') )
                WHEN LFD.INT_GET_FROM = 'PROD' THEN
                     GET_TL_ROI(LFD.PROD_ID,LSD.FROM_DT,LSD.LIMIT)
                ELSE 0 END INTO ROI
    FROM LOANS_FACILITY_DETAILS LFD
    JOIN LOANS_SANCTION_DETAILS LSD ON LFD.BORROW_NO = LSD.BORROW_NO   WHERE ACCT_NUM = ACTNUM;
 
  --DBMS_OUTPUT.PUT_LINE ( ' INTEREST : ' || ROI);
  
   IF NVL(BALAMT,0)>0 THEN
      SELECT NVL(SUM(AMOUNT),0) INTO INTRAMT    FROM ALL_TRANS AT 
      JOIN LOANS_PROD_ACHD LPA  ON LPA.PROD_ID = SUBSTR(ACTNUM,5,3)   AND AT.AC_HD_ID = AC_DEBIT_INT  
      WHERE TRANS_DT =ASONDT AND  LINK_BATCH_ID = ACTNUM AND  NVL(AT.AUTHORIZE_STATUS,' ')!='AUTHORIZED' AND NVL(STATUS,' ')!='DELETED';   

    SELECT   ASONDT - (  CEIL(( ((PAYBLEAMT - (INTRAMT+TRANSAMT)) * 36500) / (BALAMT *  ROI)))) INTO LSTINTRCALCDATE   FROM DUAL;
   ELSE
      SELECT   ASONDT   INTO LSTINTRCALCDATE   FROM DUAL;
   END IF ;
     RETURN LSTINTRCALCDATE;
   -- DBMS_OUTPUT.PUT_LINE( ' LSTINTRCALCDATE : ' || LSTINTRCALCDATE);
  
  END;
$BODY$;


CREATE OR REPLACE FUNCTION public.remainder(val1 numeric, val2 numeric)
RETURNS numeric
LANGUAGE plpgsql
STABLE SECURITY DEFINER
AS $function$
DECLARE
div_amt numeric := val1;
BEGIN
	RAISE NOTICE 'Start div_amt:%', div_amt;
	loop
		div_amt:=div_amt-val2;
		RAISE NOTICE 'inside loop div_amt:%', div_amt;
	 	exit when div_amt<=0;
	end loop;
	RAISE NOTICE '%', div_amt;
	RETURN div_amt;
END;
$function$
;

CREATE OR REPLACE FUNCTION calculate_dd_si_commfunct(actnum text, tdate timestamp without time zone, prodid text, siid text)
 RETURNS bigint
 LANGUAGE plpgsql
 STABLE SECURITY DEFINER
AS $function$
DECLARE

    CNT integer := 0;
    COMM double precision :=0;
    AMT double precision;
    CMPERBNK double precision;
    CMPERACH double precision;

BEGIN
SELECT COMM_PER_AC_HOLDR,COMM_PER_BANK INTO STRICT CMPERACH,CMPERBNK FROM AGENT_PROD_MAPPING WHERE PROD_ID = PRODID;
    SELECT SUM(T.AMOUNT) INTO STRICT AMT
    FROM DAILY_DEPOSIT_TRANS T
    WHERE T.ACC_NUM = ACTNUM  AND T.TRN_DT BETWEEN(SELECT coalesce(LAST_RUN_DT,SI_START_DT) FROM standing_instruction
   WHERE SI_ID = SIID  AND COLLECT_SI_COMM ='Y' )  AND TDATE AND T.AUTHORIZE_STATUS = 'AUTHORIZED' AND T.STATUS != 'DELETED';
            RAISE NOTICE ' AMT %', AMT;
      RAISE NOTICE ' CMPERACH %', CMPERACH;
      RAISE NOTICE ' CMPERBNK %', CMPERBNK;
            COMM :=(AMT*(CMPERACH+CMPERBNK))/100;
            RAISE NOTICE ' COMM %', COMM;
    RETURN COMM;
end;
$function$
;


CREATE OR REPLACE FUNCTION pkg_gn_pbk.gn_passbook_trans_list(
	actnum character varying)
RETURNS SETOF type_pbk_grd
LANGUAGE 'plpgsql'
COST 100
VOLATILE PARALLEL UNSAFE
ROWS 1000
AS $BODY$
DECLARE
-- SELECT * FROM TABLE (GN_PASSBOOK_TRANS_LIST('0003725000035_1'));
GRDLIST TYPE_PBK_GRD;
begin
/*
-- type_pbk_grd definition
-- DROP TYPE type_pbk_grd;
CREATE TYPE pkg_gn_pbk.type_pbk_grd AS (
	act_num varchar(15),
	slno varchar(5),
	next_sl_no varchar(5),
	trans_dt timestamp,
	particulars varchar(500),
	debit float8,
	credit float8,
	balance float8,
	instrument_no varchar(15),
	inst_type varchar(15),
	inst_dt varchar(15),
	pageno varchar(5),
	prev_bal float8,
	tod_limit float8,
	idno varchar(5));
*/
for GRDLIST IN	
SELECT ACT_NUM,SLNO,NEXT_SL_NO,TRANS_DT,PARTICULARS,DEBIT,CREDIT,BALANCE,INSTRUMENT_NO,INST_TYPE,INST_DT::varchar(15),PAGENO,PREV_BAL,TOD_LIMIT,IDNO FROM (
SELECT gk.ACT_NUM, RWID AS SLNO, NULL AS NEXT_SL_NO, TRANS_DT, INITCAP (NARR) AS PARTICULARS,
PRNDR AS DEBIT, PRNCR AS CREDIT, BAL AS BALANCE, INSTR_NO1 AS INSTRUMENT_NO,
NULL AS INST_TYPE, INSTR_DT1 AS INST_DT, PAGENO, LAG (BAL) OVER (ORDER BY IDNO) AS PREV_BAL,
NULL AS TOD_LIMIT, IDNO FROM GN_PASSBOOK GK
LEFT JOIN (SELECT ACT_NUM ,MAX(GP.IDNO) AS MXID FROM GN_PASSBOOK GP WHERE GP.ACT_NUM =ACTNUM AND GP.POST_TAG =1 group by GP.ACT_NUM ) GS
ON GS.ACT_NUM =GK.ACT_NUM
WHERE GK.ACT_NUM = ACTNUM AND GK.IDNO > NVL(GS.MXID,0)-10 ORDER BY IDNO) X
LOOP
RETURN NEXT GRDLIST;
END LOOP;
END;
$BODY$;

CREATE OR REPLACE PROCEDURE account_head_creation(descr text, modle text)
LANGUAGE plpgsql
SECURITY DEFINER
AS $procedure$
BEGIN
-- EXECUTE ACCOUNT_HEAD_CREATION ('MDSH 2/2015' ,'M');
Insert into AC_HD(MJR_AC_HD_ID, SUB_AC_HD_ID, AC_HD_CODE, AC_HD_ID, AC_HD_DESC,
STATUS, CREATED_DT, DELETED_DT, AUTHORIZE_STATUS, AUTHORIZE_BY,
AUTHORIZE_DT, STATUS_BY, STATUS_DT, REC_DETAILED_IN_DAYBOOK, PAY_DETAILED_IN_DAYBOOK,
AC_HD_ORDER)
SELECT MJR_AC_HD_ID,SUB_AC_HD_ID,NACHD,NACHD,DESCR,
'CREATED', NULL, NULL, 'AUTHORIZED', 'sysadmin',
NULL, 'admin', NULL, 'Y', 'Y',
NACHD FROM (SELECT MJR_AC_HD_ID,SUB_AC_HD_ID,MAX(AC_HD_ID)::numeric+1 AS NACHD FROM AC_HD AH WHERE is_number(AC_HD_ID)=1 and
EXISTS (SELECT * FROM NEW_ACC_HEAD WHERE MODULE =MODLE AND MJRHD = AH.MJR_AC_HD_ID )
GROUP BY MJR_AC_HD_ID,SUB_AC_HD_ID
) alias4;
COMMIT;
UPDATE AC_HD AH SET AC_HD_DESC=(SELECT MJR_AC_HD_DESC||' - '||AH.AC_HD_DESC FROM MJR_AC_HD WHERE MJR_AC_HD_ID =AH.MJR_AC_HD_ID )
WHERE AC_HD_DESC =DESCR AND EXISTS (SELECT * FROM NEW_ACC_HEAD WHERE MODULE = MODLE AND MJRHD = AH.MJR_AC_HD_ID);
COMMIT;
INSERT INTO AC_HD_PARAM(AC_HD_ID, FLOAT_ACT, CONTRA_ACT, CR_CLR, CR_TRANS,
CR_CASH, DR_CLR, DR_TRANS, DR_CASH, RECONS,
F_TRANS_DT, L_TRANS_DT, TRANSPOST, POSTMODE, BALANCETYPE,
GLBALANCE, AC_OPEN_DT, AC_CLOSE_DT, STATUS, AUTHORIZE_STATUS,
AUTHORIZE_BY, AUTHORIZE_DT, STATUS_BY, STATUS_DT, NEGATIVE_ALLOWED,
RECONS_AC_HD_ID, HO_ACCT, DAY_END_ZERO_CHECK)
SELECT AC_HD_ID AS AC_HD_ID,'Y' AS FLOAT_ACT, NULL AS CONTRA_ACT, 'Y' AS CR_CLR,
'Y' AS CR_TRANS, 'Y' AS CR_CASH, 'Y' AS DR_CLR, 'Y' AS DR_TRANS,'Y' AS DR_CASH,
'N' AS RECONS,NULL AS F_TRANS_DT, NULL AS L_TRANS_DT, 'BOTH' AS TRANSPOST,
'INDIVIDUAL' AS POSTMODE, CASE WHEN SUBSTR(MJR_AC_HD_ID,1,1)='1' THEN 'DEBIT' WHEN SUBSTR(MJR_AC_HD_ID,1,1)='2' THEN 'CREDIT' WHEN SUBSTR(MJR_AC_HD_ID,1,1)='4' THEN 'CREDIT' WHEN SUBSTR(MJR_AC_HD_ID,1,1)='5' THEN 'DEBIT' END AS BALANCETYPE,0 AS GLBALANCE, NULL AS
AC_OPEN_DT, NULL AS AC_CLOSE_DT, 'CREATED' AS STATUS, 'AUTHORIZED' AS
AUTHORIZE_STATUS, 'sysadmin' AS AUTHORIZE_BY, NULL AS AUTHORIZE_DT, 'admin' AS
STATUS_BY, NULL AS STATUS_DT, 'N' AS NEGATIVE_ALLOWED, NULL AS RECONS_AC_HD_ID,
'N' AS HO_ACCT, 'N' AS DAY_END_ZERO_CHECK FROM AC_HD WHERE
AC_HD_ID NOT IN (SELECT AC_HD_ID FROM AC_HD_PARAM);
COMMIT;
INSERT INTO BRANCH_GL(AC_HD_ID, STATUS, GROUP_ID)
SELECT AC_HD_ID,'CREATED','BG000001' FROM AC_HD WHERE
AC_HD_ID NOT IN (SELECT AC_HD_ID FROM BRANCH_GL);
COMMIT;
END;
$procedure$
;
	CREATE OR REPLACE FUNCTION get_product_behaveslike(prodid text)
RETURNS character varying
LANGUAGE plpgsql
STABLE SECURITY DEFINER
AS $function$
DECLARE
BEHAVESLIKE varchar(20);
BEGIN
select BEHAVES_LIKE INTO STRICT BEHAVESLIKE from ALL_PRODUCTS where PROD_ID = PRODID;
RETURN BEHAVESLIKE;
END;
$function$
;


CREATE OR REPLACE FUNCTION load_fin_acct(branch_id text, as_on_date timestamp without time zone, fin_act_type text)
RETURNS SETOF type_fin_acct
LANGUAGE plpgsql
STABLE SECURITY DEFINER
AS $function$
DECLARE
trCursor REFCURSOR;
outRec TYPE_FIN_ACCT ;--:= TYPE_FIN_ACCT(NULL,NULL,NULL,NULL,NULL,0,0,0,NULL);
BEGIN
IF FIN_ACT_TYPE = 'TRADING' THEN
OPEN trCursor FOR
WITH BAL AS (
SELECT MAX(m.mjr_ac_hd_id) as mjr_ac_hd_id, MAX(m.mjr_ac_hd_type) as mjr_ac_hd_type, MAX(m.mjr_ac_hd_desc) as mjr_ac_hd_desc,
MAX(a.ac_hd_id) as ac_hd_id, MAX(FORMATACHD_NAME(coalesce(a.ac_hd_order::varchar,a.ac_hd_id::varchar),coalesce(m.mjr_ac_order::varchar,m.mjr_ac_hd_id::varchar))) ac_hd_desc,
MAX(a.ac_hd_order) ac_hd_order, MAX(m.mjr_ac_order) mjr_ac_order,
ABS(SUM(CASE
WHEN g.balance_type = 'DEBIT'
THEN -1
ELSE 1
END * g.close_bal
)) close_bal,CASE
WHEN(SUM( CASE
WHEN g.balance_type = 'DEBIT'
THEN -1
ELSE 1
END
* g.close_bal
)
) < 0
THEN 'DEBIT'
ELSE 'CREDIT'
END balance_type
FROM ac_hd a JOIN mjr_ac_hd m ON a.mjr_ac_hd_id = m.mjr_ac_hd_id
JOIN gl_abstract g ON g.ac_hd_id = a.ac_hd_id
WHERE m.final_account_type = FIN_ACT_TYPE AND g.dt = AS_ON_DATE AND g.branch_code = coalesce(BRANCH_ID,G.branch_code)
GROUP BY a.ac_hd_id
),
BAL_UPD AS (
SELECT coalesce(M.MJR_AC_HD_ID,0::varchar) MJR_AC_HD_ID,
coalesce(M.MJR_AC_HD_TYPE,CASE WHEN FIN_ACT_TYPE='BALANCE SHEET' THEN CASE WHEN B.BALANCE_TYPE='CREDIT' THEN 'LIABILITY' ELSE 'ASSETS' END ELSE CASE WHEN B.BALANCE_TYPE='CREDIT' THEN 'INCOME' ELSE 'EXPENDITURE' END END ) MJR_AC_HD_TYPE,
coalesce(M.MJR_AC_HD_DESC,B.ACCOUNT_HEAD_DESC) MJR_AC_HD_DESC,
coalesce(B.ACCOUNT_HEAD_ID,0::varchar) AC_HD_ID,coalesce(FORMATACHD_NAME(coalesce(A.AC_HD_ORDER::varchar,A.AC_HD_ID::varchar),coalesce(M.MJR_AC_ORDER::varchar,M.MJR_AC_HD_ID::varchar)),B.ACCOUNT_HEAD_DESC) AC_HD_DESC,
coalesce(A.AC_HD_ORDER,0) AC_HD_ORDER,coalesce(M.MJR_AC_ORDER,0) MJR_AC_ORDER,B.AMOUNT CLOSE_BAL,B.BALANCE_TYPE BALANCE_TYPE
FROM BALANCESHEET_BALANCEUPDATE B
LEFT JOIN AC_HD A ON B.ACCOUNT_HEAD_ID = A.AC_HD_ID
LEFT JOIN MJR_AC_HD M ON M.MJR_AC_HD_ID=A.MJR_AC_HD_ID
WHERE B.FINAL_ACCOUNT_TYPE=FIN_ACT_TYPE AND B.TO_DT=AS_ON_DATE AND (CASE WHEN BRANCH_ID IS NULL THEN '1' ELSE BRANCH_ID END)=coalesce(B.BRANCH_CODE,'1')
AND B.AUTHORIZATION_STATUS='AUTHORIZED' AND B.STATUS NOT IN ('DELETED','REJECTED')
)
SELECT * FROM BAL WHERE CLOSE_BAL<>0 AND ac_hd_id
NOT IN (SELECT ac_hd_id FROM BAL_UPD WHERE CLOSE_BAL<>0)
UNION ALL
SELECT * FROM BAL_UPD WHERE CLOSE_BAL<>0;
ELSIF FIN_ACT_TYPE = 'PROFIT AND LOSS' THEN
OPEN trCursor FOR
WITH BAL AS (
SELECT MAX(m.mjr_ac_hd_id) as mjr_ac_hd_id, MAX(m.mjr_ac_hd_type) as mjr_ac_hd_type, MAX(m.mjr_ac_hd_desc) as mjr_ac_hd_desc,
MAX(a.ac_hd_id) as ac_hd_id, MAX(FORMATACHD_NAME(coalesce(a.ac_hd_order::varchar,a.ac_hd_id::varchar),coalesce(m.mjr_ac_order::varchar,m.mjr_ac_hd_id::varchar))) ac_hd_desc,
MAX(a.ac_hd_order) ac_hd_order, MAX(m.mjr_ac_order) mjr_ac_order,
ABS(SUM(CASE
WHEN g.balance_type = 'DEBIT'
THEN -1
ELSE 1
END * g.close_bal
)) close_bal,
CASE
WHEN(SUM( CASE
WHEN g.balance_type = 'DEBIT'
THEN -1
ELSE 1
END
* g.close_bal
)
) < 0
THEN 'DEBIT'
ELSE 'CREDIT'
END balance_type
FROM ac_hd a JOIN mjr_ac_hd m ON a.mjr_ac_hd_id = m.mjr_ac_hd_id
JOIN gl_abstract g ON g.ac_hd_id = a.ac_hd_id
WHERE m.final_account_type = FIN_ACT_TYPE AND g.dt = AS_ON_DATE AND g.branch_code = coalesce(BRANCH_ID,G.branch_code)
GROUP BY a.ac_hd_id
),
BAL_UPD AS (
SELECT coalesce(M.MJR_AC_HD_ID,0::varchar) MJR_AC_HD_ID,
coalesce(M.MJR_AC_HD_TYPE,CASE WHEN FIN_ACT_TYPE='BALANCE SHEET' THEN CASE WHEN B.BALANCE_TYPE='CREDIT' THEN 'LIABILITY' ELSE 'ASSETS' END ELSE CASE WHEN B.BALANCE_TYPE='CREDIT' THEN 'INCOME' ELSE 'EXPENDITURE' END END ) MJR_AC_HD_TYPE,
coalesce(M.MJR_AC_HD_DESC,B.ACCOUNT_HEAD_DESC) MJR_AC_HD_DESC,
coalesce(B.ACCOUNT_HEAD_ID,0::varchar) AC_HD_ID,coalesce(FORMATACHD_NAME(coalesce(A.AC_HD_ORDER::varchar,A.AC_HD_ID::varchar),coalesce(M.MJR_AC_ORDER::varchar,M.MJR_AC_HD_ID::varchar)),B.ACCOUNT_HEAD_DESC) AC_HD_DESC,
coalesce(A.AC_HD_ORDER,0) AC_HD_ORDER,coalesce(M.MJR_AC_ORDER,0) MJR_AC_ORDER,B.AMOUNT CLOSE_BAL,B.BALANCE_TYPE BALANCE_TYPE
FROM BALANCESHEET_BALANCEUPDATE B
LEFT JOIN AC_HD A ON B.ACCOUNT_HEAD_ID = A.AC_HD_ID
LEFT JOIN MJR_AC_HD M ON M.MJR_AC_HD_ID=A.MJR_AC_HD_ID
WHERE B.FINAL_ACCOUNT_TYPE=FIN_ACT_TYPE AND B.TO_DT=AS_ON_DATE AND (CASE WHEN BRANCH_ID IS NULL THEN '1' ELSE BRANCH_ID END)=coalesce(B.BRANCH_CODE,'1') AND
B.AUTHORIZATION_STATUS='AUTHORIZED' AND
B.STATUS NOT IN ('DELETED','REJECTED')
),
TRADING AS (
SELECT '0' AS MJR_AC_HD_ID,CASE WHEN BB.BALANCE_TYPE='DEBIT' THEN 'INCOME' ELSE 'EXPENDITURE' END MJR_AC_HD_TYPE,
BB.ACCOUNT_HEAD_DESC MJR_AC_HD_DESC,'0' AS AC_HD_ID,BB.ACCOUNT_HEAD_DESC AC_HD_DESC,
0 AS AC_HD_ORDER,0 AS MJR_AC_ORDER,BB.AMOUNT CLOSE_BAL,CASE WHEN BB.BALANCE_TYPE='DEBIT' THEN 'CREDIT' ELSE 'DEBIT' END BALANCE_TYPE
FROM BALANCESHEET_BALANCEFINAL BB WHERE BB.FINAL_ACCOUNT_TYPE='TRADING' AND BB.ACCOUNT_HEAD_DESC IN ('GROSS LOSS','GROSS PROFIT')
AND (CASE WHEN BRANCH_ID IS NULL THEN '1' ELSE BRANCH_ID END)=coalesce(BB.BRANCH_CODE,'1')
AND BB.AUTHORIZATION_STATUS='AUTHORIZED' AND BB.TO_DT=AS_ON_DATE AND BB.STATUS NOT IN ('DELETED','REJECTED')
)
SELECT * FROM BAL WHERE CLOSE_BAL<>0 AND ac_hd_id
NOT IN (SELECT ac_hd_id FROM BAL_UPD WHERE CLOSE_BAL<>0) AND ac_hd_id
NOT IN (SELECT ac_hd_id FROM TRADING WHERE CLOSE_BAL<>0)
UNION ALL
SELECT * FROM BAL_UPD WHERE CLOSE_BAL<>0
UNION ALL
SELECT * FROM TRADING WHERE CLOSE_BAL<>0;
ELSE
OPEN trCursor FOR
WITH BAL AS (
SELECT MAX(m.mjr_ac_hd_id) as mjr_ac_hd_id, MAX(m.mjr_ac_hd_type) as mjr_ac_hd_type, MAX(m.mjr_ac_hd_desc) as mjr_ac_hd_desc,
MAX(a.ac_hd_id) as ac_hd_id, MAX(FORMATACHD_NAME(coalesce(a.ac_hd_order::varchar,a.ac_hd_id::varchar),coalesce(m.mjr_ac_order::varchar,m.mjr_ac_hd_id::varchar))) ac_hd_desc,
MAX(a.ac_hd_order) ac_hd_order, MAX(m.mjr_ac_order) mjr_ac_order,
ABS(SUM(CASE
WHEN g.balance_type = 'DEBIT'
THEN -1
ELSE 1
END * g.close_bal
)) close_bal,
CASE
WHEN(SUM( CASE
WHEN g.balance_type = 'DEBIT'
THEN -1
ELSE 1
END
* g.close_bal
)
) < 0
THEN 'DEBIT'
ELSE 'CREDIT'
END balance_type
FROM ac_hd a JOIN mjr_ac_hd m ON a.mjr_ac_hd_id = m.mjr_ac_hd_id
JOIN gl_abstract g ON g.ac_hd_id = a.ac_hd_id
WHERE m.final_account_type = FIN_ACT_TYPE AND g.dt = AS_ON_DATE AND g.branch_code = coalesce(BRANCH_ID,G.branch_code)
GROUP BY a.ac_hd_id
),
BAL_UPD AS (
SELECT coalesce(M.MJR_AC_HD_ID,0::varchar) MJR_AC_HD_ID,
coalesce(M.MJR_AC_HD_TYPE,CASE WHEN FIN_ACT_TYPE='BALANCE SHEET' THEN CASE WHEN B.BALANCE_TYPE='CREDIT' THEN 'LIABILITY' ELSE 'ASSETS' END ELSE CASE WHEN B.BALANCE_TYPE='CREDIT' THEN 'INCOME' ELSE 'EXPENDITURE' END END ) MJR_AC_HD_TYPE,
coalesce(M.MJR_AC_HD_DESC,B.ACCOUNT_HEAD_DESC) MJR_AC_HD_DESC,
coalesce(B.ACCOUNT_HEAD_ID,0::varchar) AC_HD_ID,coalesce(FORMATACHD_NAME(coalesce(A.AC_HD_ORDER::varchar,A.AC_HD_ID::varchar),coalesce(M.MJR_AC_ORDER::varchar,M.MJR_AC_HD_ID::varchar)),B.ACCOUNT_HEAD_DESC) AC_HD_DESC,
coalesce(A.AC_HD_ORDER,0) AC_HD_ORDER,coalesce(M.MJR_AC_ORDER,0) MJR_AC_ORDER,B.AMOUNT CLOSE_BAL,B.BALANCE_TYPE BALANCE_TYPE
FROM BALANCESHEET_BALANCEUPDATE B
LEFT JOIN AC_HD A ON B.ACCOUNT_HEAD_ID = A.AC_HD_ID
LEFT JOIN MJR_AC_HD M ON M.MJR_AC_HD_ID=A.MJR_AC_HD_ID
WHERE B.FINAL_ACCOUNT_TYPE=FIN_ACT_TYPE AND B.TO_DT=AS_ON_DATE AND (CASE WHEN BRANCH_ID IS NULL THEN '1' ELSE BRANCH_ID END)=coalesce(B.BRANCH_CODE,'1') AND
B.AUTHORIZATION_STATUS='AUTHORIZED' AND B.STATUS NOT IN ('DELETED','REJECTED')
),
STOCK AS (
SELECT MAH.MJR_AC_HD_ID AS MJR_AC_HD_ID,'ASSETS' MJR_AC_HD_TYPE,
MAH.MJR_AC_HD_DESC MJR_AC_HD_DESC,AH.AC_HD_ID AS AC_HD_ID,AH.AC_HD_DESC AC_HD_DESC,
1 AS AC_HD_ORDER,1 AS MJR_AC_ORDER,BB.AMOUNT CLOSE_BAL,'DEBIT' BALANCE_TYPE FROM MJR_AC_HD MAH
JOIN AC_HD AH ON AH.MJR_AC_HD_ID=MAH.MJR_AC_HD_ID
JOIN BALANCESHEET_BALANCEFINAL BB ON BB.ACCOUNT_HEAD_ID = AH.AC_HD_ID
WHERE (CASE WHEN BRANCH_ID IS NULL THEN '1' ELSE BRANCH_ID END)=coalesce(BB.BRANCH_CODE,'1') AND
MAH.SUB_ACCOUNT_TYPE='CLOSING STOCK' AND BB.TO_DT=AS_ON_DATE AND BB.AUTHORIZATION_STATUS='AUTHORIZED'
AND BB.STATUS NOT IN ('DELETED','REJECTED')),
PANDL AS (
SELECT '0' AS MJR_AC_HD_ID,CASE WHEN BB.BALANCE_TYPE='CREDIT' THEN 'ASSETS' ELSE 'LIABILITY' END MJR_AC_HD_TYPE,
BB.ACCOUNT_HEAD_DESC MJR_AC_HD_DESC,'0' AS AC_HD_ID,BB.ACCOUNT_HEAD_DESC AC_HD_DESC,
0 AS AC_HD_ORDER,0 AS MJR_AC_ORDER,BB.AMOUNT CLOSE_BAL,CASE WHEN BB.ACCOUNT_HEAD_DESC='NET PROFIT' THEN 'CREDIT' ELSE 'DEBIT' END BALANCE_TYPE
FROM BALANCESHEET_BALANCEFINAL BB WHERE BB.FINAL_ACCOUNT_TYPE='PROFIT AND LOSS' AND BB.ACCOUNT_HEAD_DESC IN ('NET LOSS','NET PROFIT')
AND (CASE WHEN BRANCH_ID IS NULL THEN '1' ELSE BRANCH_ID END)=coalesce(BB.BRANCH_CODE,'1') AND
BB.AUTHORIZATION_STATUS='AUTHORIZED' AND BB.TO_DT=AS_ON_DATE AND BB.STATUS NOT IN ('DELETED','REJECTED')
)
SELECT * FROM BAL WHERE CLOSE_BAL<>0 AND ac_hd_id
NOT IN (SELECT ac_hd_id FROM BAL_UPD WHERE CLOSE_BAL<>0) AND ac_hd_id
NOT IN (SELECT ac_hd_id FROM STOCK WHERE CLOSE_BAL<>0)
UNION ALL
SELECT * FROM BAL_UPD WHERE CLOSE_BAL<>0
UNION ALL
SELECT * FROM STOCK WHERE CLOSE_BAL<>0
UNION ALL
SELECT * FROM PANDL WHERE CLOSE_BAL<>0;
END IF;
LOOP
FETCH trCursor INTO
outRec.MJR_AC_HD_ID, outRec.MJR_AC_HD_TYPE, outRec.MJR_AC_HD_DESC, outRec.AC_HD_ID, outRec.AC_HD_DESC, outRec.AC_HD_ORDER,
outRec.MJR_AC_ORDER,outRec.CLOSE_BAL, outRec.BALANCE_TYPE;
EXIT WHEN NOT FOUND; /* apply on trCursor */
RETURN NEXT outRec;
END LOOP;
CLOSE trCursor;
RETURN;
END;
$function$
;

CREATE OR REPLACE FUNCTION deleteaccountinttrailall(todtt timestamp without time zone, branch_id text, prodid text)
RETURNS character varying
LANGUAGE plpgsql
SECURITY DEFINER
AS $function$
DECLARE
curval varchar(16);-- PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
DELETE FROM ACT_INTEREST_TRIAL WHERE
APPL_DT=todtt AND BRANCH_CODE=BRANCH_ID AND PRODUCT_ID=PRODID;
--COMMIT;
DELETE FROM ODINTR_CAL WHERE
TODATE=todtt AND SUBSTR(ACT_NUM,0,4)=BRANCH_ID AND SUBSTR(ACT_NUM,5,3)=PRODID;
--COMMIT;
RETURN curval;
END;
$function$
;




CREATE OR REPLACE FUNCTION public.dt_minus_dt(dt1 timestamp without time zone, dt2 timestamp without time zone)
RETURNS INTEGER language plpgsql AS $BODY$
DECLARE
days INTEGER;
BEGIN
RAISE NOTICE 'hi';
-- SELECT DATE_PART('day', dt1::timestamp - dt2::timestamp)::integer INTO days;
	SELECT dt1::date - dt2::date INTO days;
RETURN days;
END;
$BODY$;

CREATE OR REPLACE FUNCTION pkg_app_exec.act_int_validate(brcode text, prodid text, frdt timestamp without time zone, todt timestamp without time zone, usrnam text)
RETURNS character varying
LANGUAGE plpgsql
STABLE SECURITY DEFINER
AS $function$
DECLARE
-- SELECT PKG_APP_EXEC. pkg_app_exec.act_int_validate('0001','106','01APR2018','28feb2019','admin') FROM DUAL;
MSG varchar(500);
CNT integer;
PRNACHD varchar(15);
INTACHD varchar(15);
STARTDY integer;
MININTAMT integer;
INTID varchar(15);
VALDATE integer:=1;
RT real;
MINCALAMT real;
PRODFREQINTPAY integer;
INFRDT timestamp;
INTODT timestamp;
BEGIN
begin
Select OP.AC_HD_ID,CAST(OI.STARTDAY_PROD_CALC AS integer),CAST(OI.MIN_CR_INT_AMT AS integer),OPP.CREDIT_INT,OI.MIN_BAL_FOR_INT_CALC, OI.PRODUCT_FREQ_INT_PAY
	INTO STRICT PRNACHD,STARTDY,MININTAMT,INTACHD,MINCALAMT ,PRODFREQINTPAY
FROM OP_AC_PRODUCT OP
JOIN OP_AC_ACHEAD_PARAM OPP ON OPP.PROD_ID=OP.PROD_ID
JOIN OP_AC_INTPAY_PARAM OI ON OI.PROD_ID =OP.PROD_ID
WHERE OP.PROD_ID=PRODID;
select ROI_GROUP_ID INTO STRICT INTID from DEPOSIT_ROI_GROUP_PROD WHERE PROD_ID =PRODID;
-- SELECT ROI INTO STRICT RT FROM (SELECT ROI,RI.ROI_DATE,MAX(RI.ROI_DATE) OVER (ORDER BY RI.ROI_DATE) AS MAXRDT FROM
-- DEPOSIT_ROI_GROUP_TYPE_RATE RI WHERE RI.ROI_GROUP_ID =INTID AND RI.ROI_DATE <= FRDT)
--RTI WHERE RTI.MAXRDT =RTI.ROI_DATE;-->
SELECT ROI INTO STRICT RT
FROM (SELECT ROI,RI.ROI_DATE,MAX(RI.ROI_DATE) OVER (ORDER BY RI.ROI_DATE) AS MAXRDT FROM DEPOSIT_ROI_GROUP_TYPE_RATE RI
WHERE RI.ROI_GROUP_ID ='SB' AND (FRDT BETWEEN ROI_DATE AND ROI_END_DATE
OR ROI_DATE <= FRDT AND ROI_END_DATE IS NULL)) RTI WHERE RTI.MAXRDT =RTI.ROI_DATE;
IF coalesce(RT,0)=0 THEN
VALDATE :=0;
MSG :='Please set interest rate for this product '||Chr(10);
else
MSG := 'Interest Rate ' ||RT || ' '|| Chr(10);
END IF;
SELECT COUNT(*) INTO STRICT CNT FROM ACT_INTR_CALC_ACT WHERE DT =FRDT AND TDT = TODT AND BRANCH_ID = BRCODE AND POSTED =1 AND PROD_ID = PRODID;
IF CNT >0 THEN
VALDATE :=0;
MSG :=MSG|| trim(both CNT::varchar) ||' Records Already Posted for the Period ';
END IF;
SELECT COUNT(*) INTO STRICT CNT FROM (
SELECT DN.LAST_APPL_DT+1 AS FRDT,DN.LAST_APPL_DT + CASE WHEN( CR_INT_APPL_FREQ =30) THEN 1 WHEN( CR_INT_APPL_FREQ =90) THEN 3 WHEN( CR_INT_APPL_FREQ =180) THEN 6 WHEN( CR_INT_APPL_FREQ >360 AND
CR_INT_APPL_FREQ <=366) THEN 12 END*'1 month'::interval AS TODT,DN.BRANCH_CODE,DN.PROD_ID,DN.LAST_APPL_DT FROM ( SELECT DP.BRANCH_CODE,DP.PROD_ID,LAST_APPL_DT, OI.CR_INT_APPL_FREQ ,MAX(LAST_APPL_DT) OVER (PARTITION BY DP.BRANCH_CODE,DP.PROD_ID
ORDER BY DP.BRANCH_CODE,DP.PROD_ID ) AS MXINTAPP FROM DEPOSIT_PROVISION DP,OP_AC_INTPAY_PARAM OI
WHERE DP.PROD_ID =OI.PROD_ID AND DP.REMARKS ='APPLICATION')DN WHERE DN.LAST_APPL_DT = DN.MXINTAPP )PR
WHERE BRANCH_CODE = BRCODE AND PROD_ID =PRODID;
IF CNT =0 THEN
VALDATE :=0;
MSG :=MSG|| 'Interest calculation period not set. Check CR_INT_APPL_FREQ (OP_AC_INTPAY_PARAM) And Last Record DEPOSIT_PROVISION '|| Chr(10);
ELSE
SELECT PR.FRDT,PR.TODT INTO STRICT INFRDT ,INTODT FROM (
SELECT DN.LAST_APPL_DT+1 AS FRDT,DN.LAST_APPL_DT + CASE WHEN( CR_INT_APPL_FREQ =30) THEN 1 WHEN( CR_INT_APPL_FREQ =90) THEN 3 WHEN( CR_INT_APPL_FREQ =180) THEN 6 WHEN( CR_INT_APPL_FREQ >360 AND
CR_INT_APPL_FREQ <=366) THEN 12 END*'1 month'::interval AS TODT,DN.BRANCH_CODE,DN.PROD_ID,DN.LAST_APPL_DT FROM ( SELECT DP.BRANCH_CODE,DP.PROD_ID,LAST_APPL_DT, OI.CR_INT_APPL_FREQ ,MAX(LAST_APPL_DT) OVER (PARTITION BY DP.BRANCH_CODE,DP.PROD_ID
ORDER BY DP.BRANCH_CODE,DP.PROD_ID ) AS MXINTAPP FROM DEPOSIT_PROVISION DP,OP_AC_INTPAY_PARAM OI
WHERE DP.PROD_ID =OI.PROD_ID AND DP.REMARKS ='APPLICATION')DN WHERE DN.LAST_APPL_DT = DN.MXINTAPP )PR
WHERE BRANCH_CODE = BRCODE AND PROD_ID =PRODID;
IF INTODT > TODT THEN
VALDATE :=0;
MSG :=MSG|| 'Invalid Interest calculation period = '|| TO_CHAR(INFRDT ,'DD/MM/YYYY' )||' - '||TO_CHAR(INTODT ,'DD/MM/YYYY' ) || ' Check CR_INT_APPL_FREQ (OP_AC_INTPAY_PARAM) And Last Record DEPOSIT_PROVISION '|| Chr(10);
ELSE
MSG :=MSG|| 'Interest calculation period =' || TO_CHAR(INFRDT ,'DD/MM/YYYY' )||' - '||TO_CHAR(INTODT ,'DD/MM/YYYY' ) || Chr(10);
END IF;
END IF;
-- || TO_CHAR(INFRDT ,'DD/MM/YYYY' )||', '||TO_CHAR(INTODT ,'DD/MM/YYYY' ) ||''
SELECT COUNT(*) INTO STRICT CNT FROM AC_HD WHERE AC_HD_ID =coalesce(PRNACHD,'');
IF CNT=0 THEN
VALDATE :=0;
MSG :=MSG|| 'Please set Valid Princpal A/c head for this product '|| Chr(10);
END IF;
SELECT COUNT(*) INTO STRICT CNT FROM AC_HD WHERE AC_HD_ID =coalesce(INTACHD,'');
IF CNT=0 THEN
VALDATE :=0;
MSG :=MSG|| 'Please set Valid Interest A/c head(CREDIT_INT) for this product in "OP_AC_ACHEAD_PARAM" '|| Chr(10);
END IF;
IF coalesce(STARTDY::integer,0)=0 AND coalesce(PRODFREQINTPAY::integer,0) = 30 THEN
VALDATE :=0;
MSG :=MSG|| 'Please set Start day(STARTDAY_PROD_CALC) for this product in "OP_AC_INTPAY_PARAM" '|| Chr(10);
ELSIF coalesce(PRODFREQINTPAY::integer,0) = 30 THEN
MSG := MSG|| 'Interest Calculation Start Day = ' ||STARTDY || Chr(10);
END IF;
-- IF NVL(MININTAMT,0)=0 THEN
MSG :=MSG|| 'Minumum int Amt IS ' ||coalesce(MININTAMT,0) || Chr(10);
--END IF;
MSG :=MSG|| 'Minumum Amt For calculation ' || coalesce(MINCALAMT,0 ) || Chr(10);
--- MAX PAGE NO AND SL NO IN PASS_BOOK ARE INVALID
CNT:=0;
SELECT COUNT(AR.ACT_NUM) INTO STRICT CNT
FROM ACT_MASTER AR
LEFT JOIN (SELECT 1 AS TAG,ACT_NUM,SLNO,TRANS_DT,PAGENO,PARTICULARS,DEBIT,CREDIT,BALANCE,'' AS TRID FROM (
SELECT PK.ACT_NUM,TRANS_DT,PAGENO,SLNO,PARTICULARS,0 AS DEBIT,BALANCE AS CREDIT,BALANCE ,MAX(TRANS_DT) OVER (PARTITION BY PK.ACT_NUM ) AS MXTRANSDT,MAX(PAGENO) OVER (PARTITION BY PK.ACT_NUM ) AS MXPAGENO,
MAX((PAGENO*100) + SLNO) OVER (PARTITION BY PK.ACT_NUM ) AS MXSLNO
FROM pass_book PK,ACT_MASTER AM WHERE AM.PROD_ID=PRODID AND AM.ACT_NUM =PK.ACT_NUM AND AM.STATUS NOT IN ('DELETED') AND (AM.AUTHORIZATION_STATUS IS NULL OR
AM.AUTHORIZATION_STATUS NOT IN ('REJECTED') )
AND PK.AUTHORIZE_STATUS NOT IN ('REJECTED') AND PK.STATUS NOT IN ('DELETED') ) PB
WHERE PB.TRANS_DT= PB.MXTRANSDT AND PB.PAGENO=PB.MXPAGENO AND ((PB.PAGENO*100)+PB.SLNO)= PB.MXSLNO)POK
ON POK.ACT_NUM =AR.ACT_NUM
WHERE AR.PROD_ID=PRODID AND AR.STATUS NOT IN ('DELETED') AND (AR.AUTHORIZATION_STATUS IS NULL OR AR.AUTHORIZATION_STATUS NOT IN ('REJECTED'))
AND POK.ACT_NUM IS NULL AND EXISTS (SELECT * FROM pass_book PS WHERE PS.ACT_NUM = AR.ACT_NUM );
IF CNT>0 THEN
VALDATE :=0;
MSG :=MSG|| 'Invalid PAGENO and SLNO in "PASS_BOOK" table ' || trim(both CNT::varchar) || Chr(10);
END IF;
IF coalesce(PRODFREQINTPAY,0) not in (1,30) THEN
VALDATE :=0;
MSG :=MSG|| 'invalid Product int pay Fequency(PRODUCT_FREQ_INT_PAY) for this product in "OP_AC_INTPAY_PARAM" '|| Chr(10);
ELSIF coalesce(PRODFREQINTPAY,0) = 1 THEN
MSG :=MSG|| 'Interest Calculation Method = Daily Interest ';
ELSIF coalesce(PRODFREQINTPAY,0) = 30 THEN
MSG :=MSG|| 'Interest Calculation Method = Monthly Minimum Balance ';
END IF;
MSG :=VALDATE ||'-'|| MSG;
	RAISE NOTICE 'MESSAGE :%', MSG;
EXCEPTION WHEN OTHERS THEN MSG := NULL;
end;
RETURN MSG;
END;
$function$
;

CREATE OR REPLACE FUNCTION schedule_product(br_code text, curdate timestamp without time zone, prevdate timestamp without time zone DEFAULT NULL::timestamp without time zone)
RETURNS SETOF type_schedule
LANGUAGE plpgsql
SECURITY DEFINER
AS $function$
DECLARE
-- PRAGMA AUTONOMOUS_TRANSACTION;
TRCURSOR REFCURSOR;
OUTREC TYPE_SCHEDULE;
	status varchar(100);
BEGIN
DELETE FROM DAILY_SCHEDULE_INFO D WHERE D.TRANS_DT = CURDATE AND D.BRANCH_CODE = BR_CODE;
--COMMIT;
SELECT PKGDAYEND.INS_ALL_DAYEND(CURDATE,BR_CODE) into STRICT status;
OPEN TRCURSOR FOR
WITH MSTR
AS (
SELECT TABLE_NAME,
PROD_DESC,
SUBSTR(ACT_NUM, 1, 13) AS ACT_NUM,
PROD_ID,
BRANCH_ID,
SUM(CREDIT) AS CREDIT,
SUM(DEBIT) AS DEBIT,
BEHAVIOR
FROM (
SELECT PROD.TABLE_NAME,
PROD.PROD_DESC,
T.ACT_NUM,
T.PROD_ID,
CASE WHEN T.TRANS_TYPE='CREDIT' THEN T.AMOUNT ELSE 0 END
AS CREDIT,
CASE WHEN T.TRANS_TYPE='DEBIT' THEN T.AMOUNT ELSE 0 END AS DEBIT,T.BRANCH_ID,
PROD.BEHAVIOR
FROM CASH_TRANS T
JOIN(SELECT OA.AC_HD_ID,
OA.PROD_ID,
OA.PROD_DESC,
'OA' AS BEHAVIOR,
'ACT_MASTER' AS TABLE_NAME
FROM OP_AC_PRODUCT oa
UNION ALL
SELECT OA.ACCT_HEAD,
OA.PROD_ID,
OA.PROD_DESC,
'TD' AS BEHAVIOR,
'DEPOSIT_ACINFO' AS TABLE_NAME
FROM DEPOSITS_PRODUCT oa
UNION ALL
SELECT OA.ACCT_HEAD,
OA.PROD_ID,
OA.PROD_DESC,
CASE WHEN OA.BEHAVES_LIKE='OD' THEN 'OD' ELSE 'TL' END AS BEHAVIOR ,
'LOANS_FACILITY_DETAILS' AS TABLE_NAME
FROM LOANS_PRODUCT oa
UNION ALL
SELECT OA.AC_HD_ID,
OA.PROD_ID,
OA.PROD_DESC,
'SA' AS BEHAVIOR,
'SUSPENSE_ACCOUNT_MASTER'
FROM SUSPENSE_PRODUCT oa) PROD ON PROD.PROD_ID = T.PROD_ID
LEFT JOIN(SELECT ACC_NUM,TRANS_ID FROM DAILY_DEPOSIT_TRANS DT
WHERE DT.TRANS_TYPE = 'CREDIT' AND DT.TRN_DT = CURDATE AND DT.AUTHORIZE_STATUS = 'AUTHORIZED'
AND DT.STATUS NOT IN ('DELETED'))DL on DL.TRANS_ID::varchar = T.TRANS_ID AND DL.ACC_NUM = T.ACT_NUM
WHERE T.AUTHORIZE_STATUS = 'AUTHORIZED'
AND T.STATUS NOT IN ('DELETED')
AND T.TRANS_DT = CURDATE
AND T.ACT_NUM IS NOT NULL
and T.BRANCH_ID = BR_CODE
AND DL.ACC_NUM IS NULL
UNION ALL
SELECT PROD.TABLE_NAME,
PROD.PROD_DESC,
T.ACT_NUM,
T.PROD_ID,
CASE WHEN T.TRANS_TYPE='CREDIT' THEN T.AMOUNT ELSE 0 END
AS CREDIT,
CASE WHEN T.TRANS_TYPE='DEBIT' THEN T.AMOUNT ELSE 0 END AS DEBIT,T.BRANCH_ID,
PROD.BEHAVIOR
FROM TRANSFER_TRANS T
JOIN(SELECT OA.AC_HD_ID,
OA.PROD_ID,
OA.PROD_DESC,
'OA' AS BEHAVIOR,
'ACT_MASTER' AS TABLE_NAME
FROM OP_AC_PRODUCT oa
UNION ALL
SELECT OA.ACCT_HEAD,
OA.PROD_ID,
OA.PROD_DESC,
'TD' AS BEHAVIOR,
'DEPOSIT_ACINFO' AS TABLE_NAME
FROM DEPOSITS_PRODUCT oa
UNION ALL
SELECT OA.ACCT_HEAD,
OA.PROD_ID,
OA.PROD_DESC,
CASE WHEN OA.BEHAVES_LIKE='OD' THEN 'OD' ELSE 'TL' END AS BEHAVIOR ,
'LOANS_FACILITY_DETAILS' AS TABLE_NAME
FROM LOANS_PRODUCT oa
UNION ALL
SELECT OA.AC_HD_ID,
OA.PROD_ID,
OA.PROD_DESC,
'SA' AS BEHAVIOR,
'SUSPENSE_ACCOUNT_MASTER'
FROM SUSPENSE_PRODUCT oa) PROD ON PROD.PROD_ID = T.PROD_ID
LEFT JOIN(SELECT ACC_NUM,TRANS_ID FROM DAILY_DEPOSIT_TRANS DT
WHERE DT.TRANS_TYPE = 'CREDIT' AND DT.TRN_DT = CURDATE AND DT.AUTHORIZE_STATUS = 'AUTHORIZED'
AND DT.STATUS NOT IN ('DELETED') )DL on DL.TRANS_ID::varchar = T.TRANS_ID AND DL.ACC_NUM = T.ACT_NUM
WHERE T.AUTHORIZE_STATUS = 'AUTHORIZED'
AND T.STATUS NOT IN ('DELETED')
AND T.TRANS_DT = CURDATE
AND T.ACT_NUM IS NOT NULL
and T.BRANCH_ID = BR_CODE
AND DL.ACC_NUM IS NULL) alias11
GROUP BY ACT_NUM,
PROD_ID,
TABLE_NAME,
PROD_DESC,
BRANCH_ID,
BEHAVIOR ),
DAILYTRANS AS (SELECT 'DEPOSIT_ACINFO' AS TABLE_NAME, DT.TRN_DT,DT.ACC_NUM,DT.BATCH_ID,DT.TRANS_ID,DT.AMOUNT AS RECEIPT,DT.AGENT_NO FROM DAILY_DEPOSIT_TRANS DT
LEFT JOIN TRANSFER_TRANS MSTR ON MSTR.ACT_NUM =DT.ACC_NUM AND MSTR.TRANS_DT= DT.TRN_DT AND MSTR.BATCH_ID =DT.BATCH_ID
WHERE DT.ACC_NUM IS NULL AND DT.TRANS_TYPE = 'CREDIT' AND DT.AUTHORIZE_STATUS = 'AUTHORIZED' AND DT.STATUS NOT IN ('DELETED')
) ,
OPENING AS (SELECT ACT_NUM, DAY_END_DT, AMT AS OPENING
FROM (SELECT ADB.ACT_NUM,
DAY_END_DT,
MAX(DAY_END_DT) OVER (PARTITION BY ADB.ACT_NUM)
AS LSTDAY_DT,
AMT
FROM ACT_DAYEND_BALANCE ADB
JOIN MSTR ON MSTR.ACT_NUM = ADB.ACT_NUM
WHERE MSTR.BEHAVIOR = 'OA' AND ADB.PROD_ID = MSTR.PROD_ID
AND DAY_END_DT <= PREVDATE) LD
WHERE LD.LSTDAY_DT = LD.DAY_END_DT AND LD.ACT_NUM = LD.ACT_NUM
UNION ALL
SELECT ACT_NUM, DAY_END_DT, AMT AS OPENING
FROM (SELECT ADB.ACT_NUM,
DAY_END_DT,
MAX(DAY_END_DT) OVER (PARTITION BY ADB.ACT_NUM)
AS LSTDAY_DT,
AMT
FROM LOANS_DAYEND_BALANCE ADB
JOIN MSTR ON MSTR.ACT_NUM = ADB.ACT_NUM
WHERE MSTR.BEHAVIOR ='TL' AND ADB.PROD_ID = MSTR.PROD_ID
AND DAY_END_DT <= PREVDATE) LD
WHERE LD.LSTDAY_DT = LD.DAY_END_DT AND LD.ACT_NUM = LD.ACT_NUM
UNION ALL
SELECT ACT_NUM, DAY_END_DT, AMT AS OPENING
FROM (SELECT ADB.ACT_NUM,
DAY_END_DT,
MAX(DAY_END_DT) OVER (PARTITION BY ADB.ACT_NUM)
AS LSTDAY_DT,
AMT
FROM ADVANCES_DAYEND_BALANCE ADB
JOIN MSTR ON MSTR.ACT_NUM = ADB.ACT_NUM
WHERE MSTR.BEHAVIOR = 'OD' AND ADB.PROD_ID = MSTR.PROD_ID
AND DAY_END_DT <= PREVDATE) LD
WHERE LD.LSTDAY_DT = LD.DAY_END_DT AND LD.ACT_NUM = LD.ACT_NUM
UNION ALL
SELECT ACT_NUM, DAY_END_DT, AMT AS OPENING
FROM (SELECT ADB.ACT_NUM,
DAY_END_DT,
MAX(DAY_END_DT) OVER (PARTITION BY ADB.ACT_NUM)
AS LSTDAY_DT,
AMT
FROM DEPOSIT_DAYEND_BALANCE ADB
JOIN MSTR ON MSTR.ACT_NUM = ADB.ACT_NUM
WHERE MSTR.BEHAVIOR = 'TD' AND ADB.PROD_ID = MSTR.PROD_ID
AND DAY_END_DT <= PREVDATE) LD
WHERE LD.LSTDAY_DT = LD.DAY_END_DT AND LD.ACT_NUM = LD.ACT_NUM
UNION ALL
SELECT ACT_NUM, DAY_END_DT, AMT AS OPENING
FROM (SELECT ADB.ACT_NUM,
DAY_END_DT,
MAX(DAY_END_DT) OVER (PARTITION BY ADB.ACT_NUM)
AS LSTDAY_DT,
AMT
FROM SUSPENSE_DAYEND_BALANCE ADB
JOIN MSTR ON MSTR.ACT_NUM = ADB.ACT_NUM
WHERE MSTR.BEHAVIOR = 'SA' AND ADB.PROD_ID = MSTR.PROD_ID
AND DAY_END_DT <= PREVDATE) LD
WHERE LD.LSTDAY_DT = LD.DAY_END_DT AND LD.ACT_NUM = LD.ACT_NUM),
CLOSING
AS (SELECT ACT_NUM, DAY_END_DT, AMT AS CLOSING
FROM (SELECT ADB.ACT_NUM,
DAY_END_DT,
MAX(DAY_END_DT) OVER (PARTITION BY ADB.ACT_NUM)
AS LSTDAY_DT,
AMT
FROM ACT_DAYEND_BALANCE ADB
JOIN MSTR ON MSTR.ACT_NUM = ADB.ACT_NUM
WHERE MSTR.BEHAVIOR = 'OA' AND ADB.PROD_ID = MSTR.PROD_ID
AND DAY_END_DT = CURDATE) LD
WHERE LD.LSTDAY_DT = LD.DAY_END_DT AND LD.ACT_NUM = LD.ACT_NUM
UNION ALL
SELECT ACT_NUM, DAY_END_DT, AMT AS CLOSING
FROM (SELECT ADB.ACT_NUM,
DAY_END_DT,
MAX(DAY_END_DT) OVER (PARTITION BY ADB.ACT_NUM)
AS LSTDAY_DT,
AMT
FROM LOANS_DAYEND_BALANCE ADB
JOIN MSTR ON MSTR.ACT_NUM = ADB.ACT_NUM
WHERE MSTR.BEHAVIOR = 'TL' AND ADB.PROD_ID = MSTR.PROD_ID
AND DAY_END_DT = CURDATE) LD
WHERE LD.LSTDAY_DT = LD.DAY_END_DT AND LD.ACT_NUM = LD.ACT_NUM
UNION ALL
SELECT ACT_NUM, DAY_END_DT, AMT AS CLOSING
FROM (SELECT ADB.ACT_NUM,
DAY_END_DT,
MAX(DAY_END_DT) OVER (PARTITION BY ADB.ACT_NUM)
AS LSTDAY_DT,
AMT
FROM ADVANCES_DAYEND_BALANCE ADB
JOIN MSTR ON MSTR.ACT_NUM = ADB.ACT_NUM
WHERE MSTR.BEHAVIOR = 'OD' AND ADB.PROD_ID = MSTR.PROD_ID
AND DAY_END_DT = CURDATE) LD
WHERE LD.LSTDAY_DT = LD.DAY_END_DT AND LD.ACT_NUM = LD.ACT_NUM
UNION ALL
SELECT ACT_NUM, DAY_END_DT, AMT AS CLOSING
FROM (SELECT ADB.ACT_NUM,
DAY_END_DT,
MAX(DAY_END_DT) OVER (PARTITION BY ADB.ACT_NUM)
AS LSTDAY_DT,
AMT
FROM DEPOSIT_DAYEND_BALANCE ADB
JOIN MSTR ON MSTR.ACT_NUM = ADB.ACT_NUM
WHERE MSTR.BEHAVIOR = 'TD' AND ADB.PROD_ID = MSTR.PROD_ID
AND DAY_END_DT = CURDATE) LD
WHERE LD.LSTDAY_DT = LD.DAY_END_DT AND LD.ACT_NUM = LD.ACT_NUM
UNION ALL
SELECT ACT_NUM, DAY_END_DT, AMT AS CLOSING
FROM (SELECT ADB.ACT_NUM,
DAY_END_DT,
MAX(DAY_END_DT) OVER (PARTITION BY ADB.ACT_NUM)
AS LSTDAY_DT,
AMT
FROM SUSPENSE_DAYEND_BALANCE ADB
JOIN MSTR ON MSTR.ACT_NUM = ADB.ACT_NUM
WHERE MSTR.BEHAVIOR = 'SA' AND ADB.PROD_ID = MSTR.PROD_ID
AND DAY_END_DT = CURDATE) LD
WHERE LD.LSTDAY_DT = LD.DAY_END_DT AND LD.ACT_NUM = LD.ACT_NUM)
SELECT M.TABLE_NAME,
M.ACT_NUM,
coalesce(OPENING, 0) AS OPBAL,
coalesce(OPENING, 0) + coalesce(CREDIT, 0) - coalesce(DEBIT, 0) AS ARRIVED_BAL,
coalesce(CLOSING, 0) AS CLOSE_BAL,
((coalesce(OPENING, 0) + coalesce(CREDIT, 0) - coalesce(DEBIT, 0)) -(coalesce(CLOSING, 0))) AS DIFF ,
M.BRANCH_ID AS BRANCH_CODE,
M.PROD_DESC
FROM MSTR M
LEFT JOIN OPENING O ON M.ACT_NUM = O.ACT_NUM
LEFT JOIN CLOSING C ON M.ACT_NUM = C.ACT_NUM
LEFT JOIN DAILYTRANS DT ON DT.TRN_DT = CURDATE
WHERE coalesce(OPENING, 0) + coalesce(CREDIT, 0) - coalesce(DEBIT, 0) <> coalesce(CLOSING, 0)
/* Commented by Prasanth K G taking time to get result */
/*SELECT A.TABLE_NAME,
ACT_NUM,
OPBAL,
OPBAL - DR_AMT + CR_AMT ACT_BAL,
CLOSEBAL,
(OPBAL - DR_AMT + CR_AMT) - CLOSEBAL DIFF,
BRANCH_ID,
A.PROD_DESC FROM
(
SELECT ACT_NUM,
OPBAL,
DR_AMT,
CR_AMT,
CLOSEBAL,
PROD_ID,
BRANCH_ID FROM(
WITH MST
AS ( SELECT ACT_NUM,
0 AS OPBAL,
SUM (DECODE (TRANS_TYPE, 'DEBIT', NVL (AMOUNT, 0), 0)) DR_AMT,
SUM (DECODE (TRANS_TYPE, 'CREDIT', NVL (AMOUNT, 0), 0))
CR_AMT,
0 AS CLOSEBAL,
ALL_PRODUCT_ACCTS1.PROD_ID,
ALL_PRODUCT_ACCTS1.BRANCH_ID
FROM ALL_PRODUCT_ACCTS1, REP_SUB_DAY_BOOK_IGT REP_SUB_DAY_BOOK
WHERE ( REP_SUB_DAY_BOOK.ACC_NO = ALL_PRODUCT_ACCTS1.ACT_NUM
OR REP_SUB_DAY_BOOK.ACC_NO =
ALL_PRODUCT_ACCTS1.REP_ACT_NUM)
AND TRANS_DT = CURDATE
AND ALL_PRODUCT_ACCTS1.BRANCH_ID = BR_CODE
AND ALL_PRODUCT_ACCTS1.PROD_TYPE NOT IN ('MDS', 'AB')
AND NOT EXISTS
(SELECT DDT.ACC_NUM
FROM DAILY_DEPOSIT_TRANS DDT
WHERE REPLACE (DDT.ACC_NUM, '_1', NULL) =
ACT_NUM
AND DDT.TRN_DT = CURDATE
AND REP_SUB_DAY_BOOK.TRANS_TYPE =
DDT.TRANS_TYPE
AND DDT.TRANS_TYPE = 'CREDIT')
GROUP BY ACT_NUM,
ALL_PRODUCT_ACCTS1.PROD_ID,
ALL_PRODUCT_ACCTS1.BRANCH_ID
UNION ALL
SELECT ACT_NUM,
0 AS OPBAL,
0 DR_AMT,
SUM (NVL (AMOUNT, 0)) CR_AMT,
0 AS CLOSEBAL,
ALL_PRODUCT_ACCTS1.PROD_ID,
ALL_PRODUCT_ACCTS1.BRANCH_ID
FROM ALL_PRODUCT_ACCTS1, DAILY_DEPOSIT_TRANS
WHERE ALL_PRODUCT_ACCTS1.REP_ACT_NUM(+) =
DAILY_DEPOSIT_TRANS.ACC_NUM
AND ALL_PRODUCT_ACCTS1.BRANCH_ID = BR_CODE
AND TRN_DT(+) = CURDATE
AND ALL_PRODUCT_ACCTS1.PROD_TYPE NOT IN ('MDS', 'AB')
GROUP BY ACT_NUM,
ALL_PRODUCT_ACCTS1.PROD_ID,
ALL_PRODUCT_ACCTS1.BRANCH_ID)
SELECT ACT_NUM,
NVL (PKGREPORTS.FNGETBAL (ACT_NUM, PREVDATE), 0) OPBAL,
SUM (DR_AMT) AS DR_AMT,
SUM (CR_AMT) AS CR_AMT,
NVL (PKGREPORTS.FNGETBAL (ACT_NUM, CURDATE), 0) CLOSEBAL,
PROD_ID,
BRANCH_ID
FROM MST
GROUP BY ACT_NUM, PROD_ID, BRANCH_ID)
WHERE (OPBAL - DR_AMT + CR_AMT) <> CLOSEBAL),
TABLE (GET_TAB_TABLE_NAME (PROD_ID)) A
WHERE A.TABLE_NAME IS NOT NULL AND A.TABLE_NAME NOT IN('')*/
/* End of Commented by Prasanth K G taking time to get result */
/*UNION ALL
SELECT TABLE_NAME,ACT_NUM,OPBAL,ACT_BAL,CLOSEBAL,DIFF,BRANCH_ID,PROD_DESC FROM (
WITH GLHEADS
AS (SELECT GL.AC_HD_ID,
DECODE (BALANCE_TYPE,
'DEBIT', -1 * NVL (CUR_BAL, 0),
NVL (CUR_BAL, 0))
CLOSE_BAL,
BRANCH_CODE,
BALANCE_TYPE,
AC.AC_HD_DESC
FROM GL, AC_HD AC
WHERE AC.AC_HD_ID = GL.AC_HD_ID
AND AC.AUTHORIZE_STATUS = 'AUTHORIZED'
AND AC.STATUS NOT IN ('DELETED')
AND BRANCH_CODE = BR_CODE --AND NOT EXISTS(SELECT V.AC_HD_ID FROM VIEW_OMITTED_GLHEADS V WHERE V.AC_HD_ID = A.AC_HD_ID)
),
GL_OPENINGS
AS (SELECT A.AC_HD_ID,
GLHEADS.AC_HD_DESC,
DECODE (A.BALANCE_TYPE,
'DEBIT', -1 * NVL (A.CLOSE_BAL, 0),
NVL (A.CLOSE_BAL, 0))
AS OPN_BAL,
DT,
A.BRANCH_CODE,
A.BALANCE_TYPE
FROM GL_ABSTRACT A, GLHEADS
WHERE A.AC_HD_ID = GLHEADS.AC_HD_ID
AND A.BRANCH_CODE = GLHEADS.BRANCH_CODE
AND DT IN
(SELECT MAX (G.DT)
FROM GL_ABSTRACT G
WHERE G.AC_HD_ID = A.AC_HD_ID
AND A.BRANCH_CODE = G.BRANCH_CODE
AND G.DT <= PREVDATE)
AND A.BRANCH_CODE = BR_CODE --AND NOT EXISTS(SELECT V.AC_HD_ID FROM VIEW_OMITTED_GLHEADS V WHERE V.AC_HD_ID = A.AC_HD_ID)
),
TRNS
AS ( SELECT REP_DAY_BOOK_FINAL.AC_HD_ID,
REP_DAY_BOOK_FINAL.AC_HD_DESC,
REP_DAY_BOOK_FINAL.TRANS_DT,
REP_DAY_BOOK_FINAL.BRANCH_CODE,
NVL (
SUM (
REP_DAY_BOOK_FINAL.DEBIT_CASH
+ REP_DAY_BOOK_FINAL.DEBIT_TRANSFER
+ REP_DAY_BOOK_FINAL.DEBIT_CLEARING),
0)
DEBIT,
NVL (
SUM (
REP_DAY_BOOK_FINAL.CREDIT_CASH
+ REP_DAY_BOOK_FINAL.CREDIT_TRANSFER
+ REP_DAY_BOOK_FINAL.CREDIT_CLEARING),
0)
CREDIT
FROM REP_DAY_BOOK_FINAL_IGT REP_DAY_BOOK_FINAL, GLHEADS G
WHERE REP_DAY_BOOK_FINAL.AC_HD_ID = G.AC_HD_ID
AND REP_DAY_BOOK_FINAL.BRANCH_CODE = G.BRANCH_CODE
AND TRANS_DT = CURDATE
AND REP_DAY_BOOK_FINAL.BRANCH_CODE = BR_CODE
--AND NOT EXISTS(SELECT V.AC_HD_ID FROM VIEW_OMITTED_GLHEADS V WHERE V.AC_HD_ID = REP_DAY_BOOK_FINAL.AC_HD_ID)
GROUP BY REP_DAY_BOOK_FINAL.AC_HD_ID,
REP_DAY_BOOK_FINAL.AC_HD_DESC,
REP_DAY_BOOK_FINAL.TRANS_DT,
REP_DAY_BOOK_FINAL.BRANCH_CODE
UNION ALL
SELECT TRANS_REF_GL.AC_HD_ID,
GLHEADS.AC_HD_DESC,
TRANS_DT,
BRANCH_ID AS BRANCH_CODE,
SUM (DECODE (TRANS_TYPE, 'DEBIT', AMOUNT, 0)) DEBIT,
SUM (DECODE (TRANS_TYPE, 'CREDIT', AMOUNT, 0)) CREDIT
FROM TRANS_REF_GL, GLHEADS
WHERE TRANS_REF_GL.AC_HD_ID = GLHEADS.AC_HD_ID
AND TRANS_REF_GL.BRANCH_ID = GLHEADS.BRANCH_CODE
AND TRANS_DT = CURDATE
AND TRANS_MODE != 'CLEARING'
AND BRANCH_ID = BR_CODE
--AND NOT EXISTS(SELECT V.AC_HD_ID FROM VIEW_OMITTED_GLHEADS V WHERE V.AC_HD_ID = TRANS_REF_GL.AC_HD_ID)
GROUP BY TRANS_REF_GL.AC_HD_ID,
TRANS_DT,
TRANS_REF_GL.BRANCH_ID,
GLHEADS.AC_HD_DESC),
OUTER_1
AS (SELECT G.BRANCH_CODE,
G.AC_HD_ID,
0 AS OPN_BAL,
0 AS CREDIT,
0 AS DEBIT,
CLOSE_BAL,
G.AC_HD_DESC
FROM GLHEADS G
UNION ALL
SELECT A.BRANCH_CODE,
A.AC_HD_ID,
OPN_BAL,
0 AS CREDIT,
0 AS DEBIT,
0 AS CLOSE_BAL,
AC_HD_DESC
FROM GL_OPENINGS A
UNION ALL
SELECT T.BRANCH_CODE,
T.AC_HD_ID,
0 AS OPN_BAL,
CREDIT,
DEBIT,
0 AS CLOSE_BAL,
AC_HD_DESC
FROM TRNS T),
OUTER_2
AS ( SELECT BRANCH_CODE,
AC_HD_ID,
AC_HD_DESC,
SUM (OPN_BAL) AS OPN_BAL,
SUM (CREDIT) AS CREDIT,
SUM (DEBIT) AS DEBIT,
SUM (CLOSE_BAL) AS CLOSE_BAL
FROM OUTER_1
GROUP BY BRANCH_CODE, AC_HD_ID, AC_HD_DESC),
OUTER_3
AS (SELECT BRANCH_CODE,
AC_HD_ID,
AC_HD_DESC,
OPN_BAL,
NVL (DEBIT, 0) DEBIT,
NVL (CREDIT, 0) CREDIT,
OPN_BAL - NVL (DEBIT, 0) + NVL (CREDIT, 0) CALC_CLOSE_BAL,
CLOSE_BAL
FROM OUTER_2)
SELECT 'GL' AS TABLE_NAME,
AC_HD_ID AS ACT_NUM,
AC_HD_DESC AS PROD_DESC,
OPN_BAL AS OPBAL,
CALC_CLOSE_BAL AS ACT_BAL,
CLOSE_BAL AS CLOSEBAL,
CALC_CLOSE_BAL - CLOSE_BAL AS DIFF,
NULL AS PROD_ID,
BRANCH_CODE AS BRANCH_ID
FROM OUTER_3
WHERE AC_HD_ID IS NOT NULL AND CALC_CLOSE_BAL <> CLOSE_BAL)*/
;
LOOP
FETCH TRCURSOR
INTO OUTREC.TABLE_NAME, OUTREC.ACT_NUM, OUTREC.OPBAL,
OUTREC.ARRIVED_BAL, OUTREC.CLOSE_BAL, OUTREC.DIFF,
OUTREC.BRANCH_CODE, OUTREC.PROD_DESC;
IF OUTREC.TABLE_NAME IS NOT NULL THEN
INSERT INTO DAILY_SCHEDULE_INFO(TABLE_NAME, ACT_NUM,OPBAL,ARRIVED_BAL,CLOSE_BAL,DIFF,BRANCH_CODE,TRANS_DT)
VALUES ( OUTREC.TABLE_NAME, OUTREC.ACT_NUM, OUTREC.OPBAL,
OUTREC.ARRIVED_BAL, OUTREC.CLOSE_BAL, OUTREC.DIFF,
OUTREC.BRANCH_CODE,CURDATE );
--COMMIT;
END IF;
/*
MERGE INTO DAILY_SCHEDULE_INFO A
USING (SELECT OUTREC.TABLE_NAME AS TABLE_NAME, OUTREC.ACT_NUM AS ACT_NUM,OUTREC.OPBAL AS OPBAL,OUTREC.ARRIVED_BAL AS ARRIVED_BAL
, OUTREC.CLOSE_BAL AS CLOSE_BAL ,OUTREC.DIFF AS DIFF,OUTREC.BRANCH_CODE AS BRANCH_CODE,CURDATE AS CURRDATE FROM DUAL) B
ON (A.ACT_NUM = B.ACT_NUM AND A.BRANCH_CODE=B.BRANCH_CODE AND A.TRANS_DT=B.CURRDATE)
WHEN MATCHED THEN
UPDATE
SET A.OPBAL = B.OPBAL, A.ARRIVED_BAL = B.ARRIVED_BAL,A.CLOSE_BAL = B.CLOSE_BAL,A.DIFF = B.DIFF
WHEN NOT MATCHED THEN
INSERT (TABLE_NAME, ACT_NUM,OPBAL,ARRIVED_BAL,CLOSE_BAL,DIFF,BRANCH_CODE,TRANS_DT)
VALUES( B.TABLE_NAME, B.ACT_NUM, B.OPBAL,B.ARRIVED_BAL,B.CLOSE_BAL,B.DIFF,B.BRANCH_CODE,CURDATE );
COMMIT;
*/
EXIT WHEN NOT FOUND; /* apply on TRCURSOR */
RETURN NEXT OUTREC;
END LOOP;
CLOSE TRCURSOR;
RETURN;
END;
$function$
;


-- pudukkad.all_account_balance source
CREATE OR REPLACE VIEW all_account_balance
AS SELECT act_master.act_num,
act_master.clear_balance AS account_balance,
act_master.prod_id,
act_master.branch_code AS branch_id,
act_master.create_dt,
act_master.closed_dt
FROM act_master
WHERE act_master.status::text <> 'DELETED'::text AND (act_master.authorization_status IS NULL OR act_master.authorization_status::text <> 'REJECTED'::text)
UNION ALL
SELECT da.deposit_no AS act_num,
dsa.clear_balance AS account_balance,
da.prod_id,
da.branch_id,
da.created_dt AS create_dt,
dsa.close_dt AS closed_dt
FROM deposit_sub_acinfo dsa,
deposit_acinfo da
WHERE dsa.deposit_no::text = da.deposit_no::text AND da.status::text <> 'DELETED'::text AND (da.authorize_status IS NULL OR da.authorize_status::text <> 'REJECTED'::text)
UNION ALL
SELECT loans_facility_details.acct_num AS act_num,
loans_facility_details.clear_balance AS account_balance,
loans_facility_details.prod_id,
loans_facility_details.branch_id,
loans_facility_details.acct_open_dt AS create_dt,
loans_facility_details.acct_close_dt AS closed_dt
FROM loans_facility_details
WHERE loans_facility_details.status::text <> 'DELETED'::text AND (loans_facility_details.authorize_status_1 IS NULL OR loans_facility_details.authorize_status_1::text <> 'REJECTED'::text)
UNION ALL
SELECT suspense_account_master.suspense_acct_num AS act_num,
suspense_account_master.clear_balance AS account_balance,
suspense_account_master.suspense_prod_id AS prod_id,
suspense_account_master.branch_code AS branch_id,
suspense_account_master.suspense_open__date AS create_dt,
NULL::timestamp without time zone AS closed_dt
FROM suspense_account_master
WHERE suspense_account_master.status::text <> 'DELETED'::text AND (suspense_account_master.authorize_status IS NULL OR suspense_account_master.authorize_status::text <> 'REJECTED'::text);

CREATE OR REPLACE FUNCTION pudukkad.data_checking(
	currdate date,
	br_code character varying)
    RETURNS SETOF type_data_checking 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

DECLARE
	I     TYPE_DATA_CHECKING;
	L_TAB     TYPE_DATA_CHECKING[];
	AMOUNT1   numeric (16, 2) := 0;
	AMOUNT2   numeric (16, 2) := 0;
	COUNT1    numeric := 0;
	BLIKE     VARCHAR2 (20) := NULL;
	C CURSOR for SELECT case when BATCH_ID is null or BATCH_ID =  '' then  TRANS_ID else  BATCH_ID end  AS BATCH_ID, TRANS_ID AS TRANS_ID,
		case when ACT_NUM is null or ACT_NUM = '' then LINK_BATCH_ID else  ACT_NUM end  AS LINK_BATCH_ID, TRANS_MOD_TYPE, PROD_ID, PROD_TYPE,
		TRANS_MODE, BRANCH_ID,SCREEN_NAME FROM ALL_TRANS_FUNCT (BR_CODE, CURRDATE) T WHERE T.INSTRUMENT_NO2  
		NOT LIKE '%WAIVE%' AND T.INSTRUMENT_NO2 NOT LIKE '%REBATE%';
BEGIN
	FOR I IN C LOOP
		AMOUNT1 := 0;
		AMOUNT2 := 0;
		COUNT1 := 0;
		IF (I.TRANS_MOD_TYPE = 'OA') THEN
			IF (I.TRANS_MODE = 'TRANSFER') THEN
				SELECT SUM (NVL (AMOUNT, 0)) INTO AMOUNT1 FROM ALL_TRANS A WHERE A.TRANS_DT = CURRDATE AND 
					A.TRANS_ID = I.TRANS_ID AND A.TRANS_MOD_TYPE = I.TRANS_MOD_TYPE AND A.BRANCH_ID = I.BRANCH_ID;
				SELECT COUNT (*) INTO COUNT1 FROM PASS_BOOK P WHERE P.TRANS_ID = I.TRANS_ID
					AND P.TRANS_DT = CURRDATE AND P.ACT_NUM = I.LINK_BATCH_ID;
				IF (COUNT1 = 0) THEN
					L_TAB[nvl(array_length(L_TAB,1),0)] := ('PASS_BOOK','TRANSFER_TRANS',I.LINK_BATCH_ID,'NO RECORD IN PASS BOOK');
				ELSIF (COUNT1 = 1) THEN
					SELECT  case when coalesce(DEBIT, 0) =  0 then coalesce (CREDIT, 0) else coalesce(DEBIT, 0) end  INTO AMOUNT2
						FROM PASS_BOOK p WHERE P.TRANS_ID = I.TRANS_ID AND TRANS_DT = CURRDATE
						AND ACT_NUM = I.LINK_BATCH_ID;
					IF (AMOUNT1 <> AMOUNT2) THEN
						L_TAB[nvl(array_length(L_TAB,1),0)] := ('PASS_BOOK','TRANSFER_TRANS',I.LINK_BATCH_ID,'VALUE MISMATCH');
					END IF;
				END IF;
			ELSIF (I.TRANS_MODE = 'CASH') THEN
				SELECT SUM (NVL (AMOUNT, 0)) INTO AMOUNT1 FROM ALL_TRANS A WHERE A.TRANS_DT = CURRDATE
					AND A.TRANS_ID = I.TRANS_ID AND A.TRANS_MOD_TYPE = I.TRANS_MOD_TYPE AND A.BRANCH_ID = I.BRANCH_ID;
				SELECT COUNT (*) INTO COUNT1 FROM PASS_BOOK P WHERE P.TRANS_ID = I.TRANS_ID AND TRANS_DT = CURRDATE
					AND ACT_NUM = I.LINK_BATCH_ID;
				IF (COUNT1 = 0) THEN
					L_TAB[nvl(array_length(L_TAB,1),0)] := ('PASS_BOOK','CASH_TRANS',I.LINK_BATCH_ID,'NO RECORD IN PASS BOOK');
				ELSIF (COUNT1 = 1) THEN
					SELECT case when coalesce(DEBIT, 0) =  0 then coalesce (CREDIT, 0) else coalesce(DEBIT, 0) end INTO AMOUNT2
						FROM PASS_BOOK P WHERE P.TRANS_ID = I.TRANS_ID AND TRANS_DT = CURRDATE AND ACT_NUM = I.LINK_BATCH_ID;
					IF (AMOUNT1 <> AMOUNT2) THEN
						L_TAB[nvl(array_length(L_TAB,1),0)] := ('PASS_BOOK','CASH_TRANS',I.LINK_BATCH_ID,'VALUE MISMATCH');
					END IF;
				END IF;
			END IF;                                             --transfer end
		ELSIF (I.TRANS_MOD_TYPE = 'TL' AND I.SCREEN_NAME not in ('LOAN_NOTICE','LOAN_CHARGE','LOAN_ARBITRATION')) THEN
			IF (I.TRANS_MODE = 'TRANSFER') THEN
				SELECT NVL (SUM (AMOUNT), 0) INTO AMOUNT1 FROM ALL_TRANS A WHERE A.TRANS_DT = CURRDATE
					AND A.BATCH_ID = I.BATCH_ID AND A.TRANS_MOD_TYPE = I.TRANS_MOD_TYPE AND A.BRANCH_ID = I.BRANCH_ID
					AND A.TRANS_TYPE = 'CREDIT' AND case when A.ACT_NUM is null or A.ACT_NUM = '' then A.LINK_BATCH_ID else  A.ACT_NUM End =I.LINK_BATCH_ID;
				SELECT COUNT (*) INTO COUNT1 FROM LOAN_TRANS_DETAILS L WHERE SUBSTR (L.TRANS_ID, 1, INSTR (L.TRANS_ID, '_') ) =
					I.BATCH_ID AND TRANS_DT = CURRDATE AND ACT_NUM = I.LINK_BATCH_ID and INSTR (L.TRANS_ID, '_')>0;
				IF (COUNT1 = 0) THEN
					L_TAB[nvl(array_length(L_TAB,1),0)] := ('LOAN_TRANS_DETAILS','TRANSFER_TRANS',I.LINK_BATCH_ID,'NO RECORD IN LOAN_TRANS_DETAILS');
				ELSE
					SELECT SUM ((NVL (PRINCIPLE, 0) + NVL (INTEREST, 0) /+ NVL (IBAl, 0)/ + NVL (PENAL, 0) /+ NVL (PIBAL, 0)/ 
						+ NVL (EXPENSE, 0) /+ NVL (EBAL, 0)/ + NVL (EXCESS_AMT, 0) + NVL (POSTAGE_CHARGE, 0) + NVL (ARBITARY_CHARGE, 0)
						+ NVL (LEGAL_CHARGE, 0) + NVL (INSURANCE_CHARGE, 0) + NVL (MISC_CHARGES, 0) + NVL (EXE_DEGREE, 0)
						+ NVL (ADVERTISE_CHARGE, 0) + NVL (EXE_DEGREE, 0) + NVL (NOTICE_CHARGES, 0))) AS AMOUNT INTO AMOUNT2
						FROM LOAN_TRANS_DETAILS L WHERE L.TRANS_DT = CURRDATE AND SUBSTR (l.TRANS_ID, 1, INSTR (l.TRANS_ID, '_') ) =
						I.BATCH_ID AND L.ACT_NUM = I.LINK_BATCH_ID and INSTR (L.TRANS_ID, '_')>0;
					IF (AMOUNT1 <> AMOUNT2) THEN
						L_TAB[nvl(array_length(L_TAB,1),0)] := ('LOAN_TRANS_DETAILS','TRANSFER_TRANS',I.LINK_BATCH_ID,'VALUE MISMATCH');
					END IF;
				END IF;
			ELSIF (I.TRANS_MODE = 'CASH') THEN
				SELECT NVL (SUM (AMOUNT), 0) INTO AMOUNT1 FROM ALL_TRANS A WHERE A.TRANS_DT = CURRDATE
					AND A.TRANS_ID = I.BATCH_ID AND TRANS_MOD_TYPE = I.TRANS_MOD_TYPE
					AND case when A.ACT_NUM is  null or A.ACT_NUM  = ''  then A.LINK_BATCH_ID else  A.ACT_NUM end =I.LINK_BATCH_ID;
				SELECT COUNT (*) INTO COUNT1 FROM LOAN_TRANS_DETAILS L WHERE L.TRANS_ID = I.BATCH_ID
					AND L.TRANS_DT = CURRDATE AND L.ACT_NUM = I.LINK_BATCH_ID;
				IF (COUNT1 = 0) THEN
					L_TAB[nvl(array_length(L_TAB,1),0)] := ('LOAN_TRANS_DETAILS','CASH_TRANS',I.LINK_BATCH_ID,'NO RECORD IN LOAN_TRANS_DETAILS');
				ELSE
					SELECT SUM ((  NVL (PRINCIPLE, 0) + NVL (INTEREST, 0) + NVL (IBAl, 0) + NVL (PENAL, 0) + NVL (PIBAL, 0)
						+ NVL (EXPENSE, 0) + NVL (EBAL, 0) + NVL (EXCESS_AMT, 0) + NVL (POSTAGE_CHARGE, 0)
						+ NVL (ARBITARY_CHARGE, 0) + NVL (LEGAL_CHARGE, 0) + NVL (INSURANCE_CHARGE, 0) + NVL (MISC_CHARGES, 0)
						+ NVL (EXE_DEGREE, 0) + NVL (ADVERTISE_CHARGE, 0) + NVL (NOTICE_CHARGES, 0))) AS AMOUNT INTO AMOUNT2
						FROM LOAN_TRANS_DETAILS L WHERE L.TRANS_DT = CURRDATE AND L.TRANS_ID = I.BATCH_ID AND L.ACT_NUM = I.LINK_BATCH_ID;
					IF (AMOUNT1 <> AMOUNT2) THEN
						L_TAB[nvl(array_length(L_TAB,1),0)] := ('LOAN_TRANS_DETAILS','CASH_TRANS',I.LINK_BATCH_ID,'VALUE MISMATCH');
					END IF;
				END IF;
			END IF;                                            ---transfer end
		ELSIF (I.TRANS_MOD_TYPE = 'AD') THEN
			IF (I.TRANS_MODE = 'TRANSFER') THEN
				SELECT NVL (SUM (AMOUNT), 0) INTO AMOUNT1 FROM ALL_TRANS A WHERE A.TRANS_DT = CURRDATE AND A.BATCH_ID = I.BATCH_ID 
					AND A.TRANS_MOD_TYPE = I.TRANS_MOD_TYPE AND case when A.ACT_NUM is NULL or   A.ACT_NUM = '' then  A.LINK_BATCH_ID else  A.ACT_NUM End=I.LINK_BATCH_ID
					AND A.BRANCH_ID = I.BRANCH_ID;
				SELECT COUNT (*) INTO COUNT1 FROM ADV_TRANS_DETAILS A WHERE SUBSTR (A.TRANS_ID, 1, INSTR (A.TRANS_ID, '_') - 1) =
					I.BATCH_ID AND A.TRANS_DT = CURRDATE AND A.ACT_NUM = I.LINK_BATCH_ID;
				IF (COUNT1 = 0) THEN
					L_TAB[nvl(array_length(L_TAB,1),0)] := ('ADV_TRANS_DETAILS','TRANSFER_TRANS',I.LINK_BATCH_ID,'NO RECORD IN ADV_TRANS_DETAILS');
				ELSE
					SELECT SUM ((  NVL (PRINCIPLE, 0) + NVL (INTEREST, 0) + NVL (IBAl, 0) + NVL (PENAL, 0) + NVL (PIBAL, 0)
						+ NVL (EXPENSE, 0) + NVL (EBAL, 0) + NVL (EXCESS_AMT, 0) + NVL (POSTAGE_CHARGE, 0) + NVL (ARBITARY_CHARGE, 0)
						+ NVL (LEGAL_CHARGE, 0) + NVL (INSURANCE_CHARGE, 0) + NVL (MISC_CHARGES, 0) + NVL (EXE_DEGREE, 0)))
						AS AMOUNT INTO AMOUNT2 FROM ADV_TRANS_DETAILS A WHERE A.TRANS_DT = CURRDATE AND SUBSTR (A.TRANS_ID,1,INSTR 
						(A.TRANS_ID, '_') - 1) = I.BATCH_ID AND A.ACT_NUM = I.LINK_BATCH_ID;
					IF (AMOUNT1 <> AMOUNT2) THEN
						L_TAB[nvl(array_length(L_TAB,1),0)] := ('ADV_TRANS_DETAILS','TRANSFER_TRANS',I.LINK_BATCH_ID,'VALUE MISMATCH');
					END IF;
				END IF;
			ELSIF (I.TRANS_MODE = 'CASH') THEN
				SELECT NVL (SUM (AMOUNT), 0) INTO AMOUNT1 FROM ALL_TRANS A WHERE A.TRANS_DT = CURRDATE AND A.TRANS_ID = I.BATCH_ID
					AND A.TRANS_MOD_TYPE = I.TRANS_MOD_TYPE AND case when A.ACT_NUM is NULL or   A.ACT_NUM = '' then  A.LINK_BATCH_ID else  A.ACT_NUM End=I.LINK_BATCH_ID
					AND A.BRANCH_ID = I.BRANCH_ID;
				SELECT COUNT (*) INTO COUNT1 FROM ADV_TRANS_DETAILS A WHERE A.TRANS_ID = I.BATCH_ID AND A.TRANS_DT = CURRDATE
					AND A.ACT_NUM = I.LINK_BATCH_ID;
				IF (COUNT1 = 0) THEN
					L_TAB[nvl(array_length(L_TAB,1),0)] := ('ADV_TRANS_DETAILS','CASH_TRANS',I.LINK_BATCH_ID,'NO RECORD IN ADV_TRANS_DETAILS');
				ELSE
					SELECT SUM ((  NVL (PRINCIPLE, 0) + NVL (INTEREST, 0) + NVL (IBAl, 0) + NVL (PENAL, 0) + NVL (PIBAL, 0)
						+ NVL (EXPENSE, 0) + NVL (EBAL, 0) + NVL (EXCESS_AMT, 0) + NVL (POSTAGE_CHARGE, 0) + NVL (ARBITARY_CHARGE, 0)
						+ NVL (LEGAL_CHARGE, 0) + NVL (INSURANCE_CHARGE, 0) + NVL (MISC_CHARGES, 0) + NVL (EXE_DEGREE, 0)))
						AS AMOUNT INTO AMOUNT2 FROM ADV_TRANS_DETAILS L WHERE L.TRANS_DT = CURRDATE AND L.TRANS_ID = I.BATCH_ID;
					IF (AMOUNT1 <> AMOUNT2) THEN
						L_TAB[nvl(array_length(L_TAB,1),0)] := ('ADV_TRANS_DETAILS','CASH_TRANS',I.LINK_BATCH_ID,'VALUE MISMATCH');
					END IF;
				END IF;  
			END IF;  
			--                                                       --transfer end
		ELSIF (I.TRANS_MOD_TYPE = 'TD') THEN
			IF (I.PROD_TYPE = 'TD') THEN
				IF (I.TRANS_MODE = 'TRANSFER') THEN
					SELECT a.BEHAVES_LIKE INTO BLIKE FROM ALL_PRODUCTS A WHERE A.PROD_ID = I.PROD_ID;
					IF ((BLIKE = 'RECURRING' AND I.SCREEN_NAME not in ('Deposit Closing/Transfer to Matured Deposit'))) THEN
						SELECT SUM (AMOUNT) INTO AMOUNT1 FROM ALL_TRANS A,DEPOSITS_PRODUCT DP WHERE A.TRANS_DT = CURRDATE
							AND DP.ACCT_HEAD = A.AC_HD_ID AND DP.PROD_ID = A.PROD_ID AND A.TRANS_ID = I.TRANS_ID
							AND A.TRANS_MOD_TYPE = I.TRANS_MOD_TYPE AND A.BRANCH_ID = I.BRANCH_ID;
						SELECT COUNT (*) INTO COUNT1 FROM DEPOSIT_RECURRING D WHERE D.DEPOSIT_NO_SUB = I.LINK_BATCH_ID
							OR D.DEPOSIT_NO_SUB = I.LINK_BATCH_ID || '_1' AND D.TRANS_DT = CURRDATE;
						IF (COUNT1 = 0) THEN
							L_TAB[nvl(array_length(L_TAB,1),0)] := ('DEPOSIT_RECURRING','TRANSFER_TRANS',I.LINK_BATCH_ID,'NO RECORD IN DEPOSIT_RECURRING');
						ELSE
							SELECT NVL (SUM (AMOUNT), 0) INTO AMOUNT2 FROM DEPOSIT_RECURRING D WHERE (D.DEPOSIT_NO_SUB = I.LINK_BATCH_ID
								OR D.DEPOSIT_NO_SUB = I.LINK_BATCH_ID||'_1') AND D.TRANS_DT = CURRDATE;
							IF (AMOUNT1 <> AMOUNT2) THEN
								L_TAB[nvl(array_length(L_TAB,1),0)] := ('DEPOSIT_RECURRING','TRANSFER_TRANS',I.LINK_BATCH_ID,'VALUE MISMATCH');
							END IF;
						END IF;
					END IF;
				ELSIF (I.TRANS_MODE = 'CASH') THEN
					SELECT A.BEHAVES_LIKE INTO BLIKE FROM ALL_PRODUCTS A WHERE A.PROD_ID = I.PROD_ID;
					IF ((BLIKE = 'RECURRING' AND I.SCREEN_NAME not in ('Deposit Account Closing'))) THEN
						SELECT SUM (AMOUNT) INTO AMOUNT1 FROM ALL_TRANS A,DEPOSITS_PRODUCT DP WHERE A.TRANS_DT = CURRDATE
							AND A.AC_HD_ID = DP.ACCT_HEAD AND A.PROD_ID = DP.PROD_ID AND A.TRANS_ID = I.BATCH_ID AND 
							A.TRANS_MOD_TYPE = I.TRANS_MOD_TYPE AND A.BRANCH_ID = I.BRANCH_ID;
						SELECT COUNT (*) INTO COUNT1 FROM DEPOSIT_RECURRING D WHERE ( D.DEPOSIT_NO_SUB = I.LINK_BATCH_ID
							OR D.DEPOSIT_NO_SUB = I.LINK_BATCH_ID||'_1') AND D.TRANS_DT = CURRDATE;
						IF (COUNT1 = 0) THEN
							L_TAB[nvl(array_length(L_TAB,1),0)] := ('DEPOSIT_RECURRING','CAH_TRANS',I.LINK_BATCH_ID,'NO RECORD IN DEPOSIT_RECURRING');
						ELSE
							SELECT NVL (SUM (AMOUNT), 0) INTO AMOUNT2 FROM DEPOSIT_RECURRING D WHERE ( D.DEPOSIT_NO_SUB = I.LINK_BATCH_ID
								OR D.DEPOSIT_NO_SUB = I.LINK_BATCH_ID||'_1') AND D.TRANS_DT = CURRDATE;
							IF (AMOUNT1 <> AMOUNT2) THEN
								L_TAB[nvl(array_length(L_TAB,1),0)] := ('DEPOSIT_RECURRING','TRANSFER_TRANS',I.LINK_BATCH_ID,'VALUE MISMATCH');
							END IF;
						END IF;
					END IF;
				END IF;
			END IF;
		END IF;
	END LOOP;
	IF L_TAB IS NOT NULL THEN
	FOREACH I IN ARRAY L_TAB LOOP
		--         DBMS_OUTPUT.put_line (
		--               'last loop'
		--            || i.TABLE_NAME
		--            || AMOUNT2
		--            || i.MASTER_TABLE
		--            || i.ACT_NUM);
		INSERT INTO DATA_CHECKING_INFO  (TABLE_NAME, MASTER_TABLE, ACT_NUM,MESSAGE, BRANCH_CODE,TRANS_DT)
			(SELECT I.TABLE_NAME AS TABLE_NAME, I.MASTER_TABLE AS MASTER_TABLE,	I.ACT_NUM AS ACT_NUM,
			I.MESSAGE AS MESSAGE, BR_CODE AS BRCODE, CURRDATE AS CURDATE FROM DUAL)
		ON CONFLICT (ACT_NUM,TABLE_NAME,MASTER_TABLE,TRANS_DT,BRANCH_CODE)
		DO UPDATE SET
			MESSAGE=EXCLUDED.MESSAGE;	
		RETURN NEXT I;
	END LOOP;
	END IF;
END;
$BODY$;

CREATE UNIQUE INDEX data_checking_info_idx ON chndata.data_checking_info USING btree (act_num, table_name, master_table, trans_dt, branch_code);


alter table LOAN_TRANS_DETAILS alter column ADVERTISE_CHARGE type numeric(16,2) USING ADVERTISE_CHARGE::numeric(16,2);

alter table LOAN_TRANS_DETAILS alter column ADVERTISE_CHARGE_BAL type numeric(16,2) USING ADVERTISE_CHARGE_BAL::numeric(16,2);
