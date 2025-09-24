/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Dell
 * Created: 17 Jul, 2021
 */

/* DB Change Scripts By Ajith From 01/07/2021 */
alter table mds_master_maintenance alter column gross_weight type numeric(16,3) using gross_weight::numeric;

alter table mds_master_maintenance alter column net_weight type numeric(16,3) using net_weight::numeric;

ALTER TABLE ACT_MASTER ADD MOBILE_NO VARCHAR(10);

 alter table deposit_lien  alter column deposit_sub_no type numeric(1) USING deposit_sub_no::numeric(1)
  
  alter table MDS_SECURITY_LAND  alter column sub_no type numeric(1) USING sub_no::numeric(1)
  
  alter table MDS_SOCIETY_TYPE  alter column sub_no type numeric(1) USING sub_no::numeric(1)
  
  alter table mds_salary_security_details  alter column sub_no type numeric(1) USING sub_no::numeric(1)
  
  alter table deposit_freeze alter column deposit_sub_no type numeric(1) USING deposit_sub_no::numeric(1)

alter table deposit_acinfo alter column printing_no type numeric(16) USING printing_no::numeric(16)

ALTER TABLE LOANS_FACILITY_DETAILS ADD KOLE_LAND_AREA NUMeric(19,2);

alter table LOAN_CHARGE_DEFINITION alter column FLAT_CHARGE type numeric(16,2) USING FLAT_CHARGE::numeric(16,2)

ALTER TABLE GAHAN_DOCUMENT_DETAILS ADD GAHAN_RELEASE_NO VARCHAR2(30);

ALTER TABLE leave_details ADD leave_details_id VARCHAR(15);
	
Insert into ID_GENERATION
(ID_KEY, CURR_VALUE, PREFIX, LAST_UPDATED, ID_LENGTH,
BRANCH_CODE)
Values
('LEAVE_DETAILS_ID', 0, 'LD', TO_DATE('12/09/2014', 'DD/MM/YYYY'), 6,
'0001');

ALTER TABLE service_tax_details ALTER COLUMN acct_num DROP NOT NULL;

CREATE unique INDEX actdend ON pudukkad.act_dayend_balance USING btree (act_num, day_end_dt)

alter table LOANS_SECURITY_LAND  alter column document_dt type date USING document_dt::date


alter table sms_subscription alter column MOBILE_NO type VARCHAR(10) USING MOBILE_NO::VARCHAR(10)


-- Added by nithya on 20-03-2024

ALTER TABLE MDS_PRODUCT_OTHER_DETAILS ADD SPECIAL_INTEREST VARCHAR(1) DEFAULT 'N';
 
ALTER TABLE MDS_PRODUCT_OTHER_DETAILS ADD SPECIAL_INT_DUE_PERIOD_MONTH NUMERIC(2) default 0;


-- Added by nithya on 18-07-2024

alter table share_conf_details add SHARE_FEE_CONSTANT VARCHAR(1) default 'N';

CREATE OR REPLACE VIEW fixed_deposit_accounts
AS SELECT a.branch_id,
    a.prod_id,
    a.deposit_no,
    a.cust_id,
    a.constitution,
    a.category,
    a.renewal_from_deposit,
    s.status,
    s.acct_status
   FROM deposit_acinfo a
     JOIN fixed_deposit_product fdp ON fdp.prod_id::text = a.prod_id::text
     JOIN deposit_sub_acinfo s ON s.deposit_no::text = a.deposit_no::text
  WHERE nvl(a.authorize_status, 'AUTHORIZED'::character varying)::text = 'AUTHORIZED'::text AND a.status::text <> 'DELETED'::text;

-- Added by nithya on 10-10-2024

CREATE OR REPLACE FUNCTION GET_SELECT_CUST_PAN_NUMBER(custid varchar)
 RETURNS varchar
 LANGUAGE plpgsql
 STABLE SECURITY DEFINER
AS $function$
DECLARE
 PAN_NO varchar(20);
BEGIN 
 SELECT PAN_NUMBER INTO PAN_NO FROM CUSTOMER WHERE CUST_ID = CUSTID;
 IF PAN_NO IS NULL THEN
  select GET_CUST_PROOF_BYTYPE(CUSTID,'PAN') INTO PAN_NO FROM DUAL ;
 END IF; 
 RETURN NVL(PAN_NO,NULL);
      End;
$function$
;


CREATE OR REPLACE PROCEDURE payroll_leave_process(proces_dt timestamp without time zone)
LANGUAGE plpgsql
SECURITY DEFINER
AS $procedure$
DECLARE
    PAYROLL_MNTH_CNT integer;
    BSPAYCODE varchar(10);
    DAPAYCODE varchar(10);
    HRAPAYCODE varchar(10);
    PFPAYCODE varchar(10);
    PENSINPAYCODE varchar(10);
    CALCAMT double precision := 0;

    C1 CURSOR FOR
        SELECT LD.EMP_ID, SUM(LM.LEAVE_DED_PER_DAY) AS TOT_CUTTING_DAYS
        FROM LEAVE_MASTER LM 
        JOIN LEAVE_DETAILS LD ON LM.LEAVE_ID = LD.LEAVE_ID 
        AND LD.LEAVE_DATE BETWEEN PROCES_DT AND PROCES_DT + '1 month'::interval - 1
        GROUP BY LD.EMP_ID;

BEGIN
    DELETE FROM PAYROLL_LEAVE WHERE MONTH_YEAR = PROCES_DT;

    INSERT INTO PAYROLL_LEAVE(EMPLOYEEID, MONTH_YEAR, PAY_TYPE, SCALE_ID, VERSION_NO,
        SRL_NO, TRANS_DATE, TRANS_ID, BATCH_ID, PAY_CODE, 
        PAY_SRL_NO, PAY_DESCRI, AMOUNT, PROD_TYPE, PROD_ID, 
        ACC_NO, PRINCIPAL, INTEREST, PENALINTEREST, CALC_UPTO, 
        FROM_DATE, TO_DATE, REMARK, STATUS_BY, CREATED_BY, 
        CREATED_DATE, AUTHORIZED_BY, AUTHORIZE_STATUS, STATUS, PAYROLLID)
    SELECT EMPLOYEEID, MONTH_YEAR, PAY_TYPE, SCALE_ID, VERSION_NO, 
        SRL_NO, TRANS_DATE, TRANS_ID, BATCH_ID, PAY_CODE, 
        PAY_SRL_NO, PAY_DESCRI, AMOUNT, PROD_TYPE, PROD_ID, 
        ACC_NO, PRINCIPAL, INTEREST, PENALINTEREST, CALC_UPTO, 
        FROM_DATE, TO_DATE, REMARK, STATUS_BY, CREATED_BY, 
        CREATED_DATE, AUTHORIZED_BY, AUTHORIZE_STATUS, STATUS, PAYROLLID 
    FROM PAYROLL 
    WHERE EMPLOYEEID IN (
        SELECT LD.EMP_ID  
        FROM LEAVE_MASTER LM 
        JOIN LEAVE_DETAILS LD ON LM.LEAVE_ID = LD.LEAVE_ID 
        AND LD.LEAVE_DATE BETWEEN PROCES_DT AND PROCES_DT + '1 month'::interval - 1
    ) 
    AND MONTH_YEAR = PROCES_DT;

    -- Explicitly assign values to avoid ambiguity
    SELECT parameters.PAYROLL_MNTH_CNT INTO PAYROLL_MNTH_CNT FROM PARAMETERS;
    SELECT PM.PAY_CODE INTO BSPAYCODE FROM PAYCODES_MASTER PM 
    WHERE PM.PAY_EARNDEDU = 'EARNINGS' AND UPPER(PM.PAY_MODULE_TYPE) = 'BASICPAY';
    SELECT PM.PAY_CODE INTO DAPAYCODE FROM PAYCODES_MASTER PM 
    WHERE PM.PAY_EARNDEDU = 'EARNINGS' AND UPPER(PM.PAY_MODULE_TYPE) = 'DEARNESS ALLOWANCE';
    SELECT PM.PAY_CODE INTO HRAPAYCODE FROM PAYCODES_MASTER PM 
    WHERE PM.PAY_EARNDEDU = 'EARNINGS' AND UPPER(PM.PAY_MODULE_TYPE) = 'HOUSE RENT ALLOWANCE';
    SELECT PM.PAY_CODE INTO PFPAYCODE FROM PAYCODES_MASTER PM 
    WHERE PM.PAY_EARNDEDU = 'DEDUCTIONS' AND UPPER(PM.PAY_MODULE_TYPE) = 'PF';
    SELECT PM.PAY_CODE INTO PENSINPAYCODE FROM PAYCODES_MASTER PM 
    WHERE PM.PAY_EARNDEDU = 'CONTRA' AND UPPER(PM.PAY_MODULE_TYPE) = 'PF';

    FOR S1 IN C1 LOOP
        -- BASIC
        UPDATE PAYROLL
        SET AMOUNT = AMOUNT - ROUND(((AMOUNT / PAYROLL_MNTH_CNT) * S1.TOT_CUTTING_DAYS), 0)
        WHERE PAY_CODE = BSPAYCODE AND EMPLOYEEID = S1.EMP_ID AND MONTH_YEAR = PROCES_DT 
        AND UPPER(STATUS) != 'POSTED';

        -- DA     
        SELECT SUM(AMOUNT) INTO CALCAMT 
        FROM PAYROLL
        WHERE PAY_CODE IN (
            SELECT CALC_PAY_CODE  
            FROM PAY_CALC_SET 
            WHERE PAY_CODE = DAPAYCODE AND TO_DATE IS NULL
        )
        AND EMPLOYEEID = S1.EMP_ID AND MONTH_YEAR = PROCES_DT AND UPPER(STATUS) != 'POSTED';

        UPDATE PAYROLL
        SET AMOUNT = ROUND(((CALCAMT * (
            SELECT PAY_PERCENT  
            FROM PAY_SETTINGS 
            WHERE PAY_CODE = DAPAYCODE AND TO_DATE IS NULL
        )) / 100), 0)
        WHERE PAY_CODE = DAPAYCODE AND EMPLOYEEID = S1.EMP_ID AND MONTH_YEAR = PROCES_DT 
        AND UPPER(STATUS) != 'POSTED';

        -- HRA
        CALCAMT := 0;
        SELECT SUM(AMOUNT) INTO CALCAMT 
        FROM PAYROLL
        WHERE PAY_CODE IN (
            SELECT CALC_PAY_CODE  
            FROM PAY_CALC_SET 
            WHERE PAY_CODE = HRAPAYCODE AND TO_DATE IS NULL
        )
        AND EMPLOYEEID = S1.EMP_ID AND MONTH_YEAR = PROCES_DT AND UPPER(STATUS) != 'POSTED';

        UPDATE PAYROLL
        SET AMOUNT = CASE 
            WHEN (
                SELECT PAY_MAX_AMT  
                FROM PAY_SETTINGS 
                WHERE PAY_CODE = HRAPAYCODE AND TO_DATE IS NULL
            ) < ROUND(((CALCAMT * (
                SELECT PAY_PERCENT  
                FROM PAY_SETTINGS 
                WHERE PAY_CODE = HRAPAYCODE AND TO_DATE IS NULL
            )) / 100), 0) 
            THEN (SELECT PAY_MAX_AMT  FROM PAY_SETTINGS WHERE PAY_CODE = HRAPAYCODE AND TO_DATE IS NULL) 
            ELSE ROUND(((CALCAMT * (
                SELECT PAY_PERCENT  
                FROM PAY_SETTINGS 
                WHERE PAY_CODE = HRAPAYCODE AND TO_DATE IS NULL
            )) / 100), 0) 
        END 
        WHERE PAY_CODE = HRAPAYCODE AND EMPLOYEEID = S1.EMP_ID AND MONTH_YEAR = PROCES_DT 
        AND UPPER(STATUS) != 'POSTED';

        -- Provident Fund
        CALCAMT := 0;
        SELECT SUM(AMOUNT) INTO CALCAMT 
        FROM PAYROLL
        WHERE PAY_CODE IN (
            SELECT CALC_PAY_CODE  
            FROM PAY_CALC_SET 
            WHERE PAY_CODE = PFPAYCODE AND TO_DATE IS NULL
        )
        AND EMPLOYEEID = S1.EMP_ID AND MONTH_YEAR = PROCES_DT AND UPPER(STATUS) != 'POSTED';

        UPDATE PAYROLL
        SET AMOUNT = ROUND(((CALCAMT * (
            SELECT PAY_PERCENT  
            FROM PAY_SETTINGS 
            WHERE PAY_CODE = PFPAYCODE AND TO_DATE IS NULL
        )) / 100), 0)
        WHERE PAY_CODE = PFPAYCODE AND EMPLOYEEID = S1.EMP_ID AND MONTH_YEAR = PROCES_DT 
        AND UPPER(STATUS) != 'POSTED';

        -- Pension Fund
        CALCAMT := 0;
        SELECT SUM(AMOUNT) INTO CALCAMT 
        FROM PAYROLL
        WHERE PAY_CODE IN (
            SELECT CALC_PAY_CODE  
            FROM PAY_CALC_SET 
            WHERE PAY_CODE = PENSINPAYCODE AND TO_DATE IS NULL
        )
        AND EMPLOYEEID = S1.EMP_ID AND MONTH_YEAR = PROCES_DT AND UPPER(STATUS) != 'POSTED';

        UPDATE PAYROLL
        SET AMOUNT = ROUND(((CALCAMT * (
            SELECT PAY_PERCENT  
            FROM PAY_SETTINGS 
            WHERE PAY_CODE = PENSINPAYCODE AND TO_DATE IS NULL
        )) / 100), 0)
        WHERE PAY_CODE = PENSINPAYCODE AND EMPLOYEEID = S1.EMP_ID AND MONTH_YEAR = PROCES_DT 
        AND UPPER(STATUS) != 'POSTED';

    END LOOP;

EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'An error occurred: %', SQLERRM;
END;
$procedure$;


 alter table investment_deposit add RENEWAL_DT DATE;  
 
 alter table investment_deposit_renewal  add renewal_dt date;

--Added by nithya on 18-03-2025

alter table group_screens add REPORT_EXPORT_ALLOWED VARCHAR(1) default 'N';

INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('EXCEL_REPORT_EXPORT_PATH', 'E:/ExportedReports/', NULL);   

--Added by Nithya on 16 Apr 2025


INSERT INTO screen_master
(screen_id, menu_id, screen_name, app_id, wf_status, module_id, screen_class, sl_no, status, screen_type, record_key_col, screen_desc)
VALUES('SCR01380', '131', 'Loan Auction', 'APP01', 'DONE', '5', NULL, 5, 'CREATED', NULL, NULL, 'Loan Auction');


alter table loans_facility_details add is_auction varchar(1) default 'N';
			
alter table loans_facility_details add auction_dt date default null;


-- DROP FUNCTION chndata.get_tl_interest(text, timestamp, timestamp, numeric);

CREATE OR REPLACE FUNCTION get_tl_interest(actnum text, loandate timestamp without time zone, asondt timestamp without time zone, virtual_roi numeric DEFAULT 0)
 RETURNS numeric
 LANGUAGE plpgsql
 STABLE SECURITY DEFINER
AS $function$
DECLARE
/*
 VIRTUAL_ROI : THIS FIELD IS USED TO CALCULATE THE INTEREST OF AN ACCOUNT WITH A NEW ROI OTHER THAN THE ACTUAL ONE. WHEN THIS PARAMETER IS
              PASSED THE TOTAL INTEREST RECEIVED WILL BE IGNORED. THIS IS PARAMETER IS USED ONLY IN INTEREST SUBVENTION REPORT
*/
--DECLARE
--    ACTNUM VARCHAR2(13) := '0005264000033';
--    LOANDATE DATE := '01-JAN-2019';
--    ASONDT DATE := '31-MAR-2019';
--    VIRTUAL_ROI NUMBER := 3;
    INTRECEIVABLE double precision := 0;
    INTRECEIVED double precision := 0;
    ROI double precision := 0;
    TRNDATE timestamp;
    PRNBAL double precision := 0;
    LASTINTCALCDATE timestamp;
    STARTDATE timestamp;
    ENDDATE timestamp := ASONDT;
    LOANBAL double precision := 0;
    FIRST_PAY_DATE timestamp;
    CLS_DATE timestamp;
    PRODUCTID varchar(8);
    INT_RECOVED_PARM varchar(1);
BEGIN
    SELECT GET_TL_BALANCE(ACTNUM,ASONDT) INTO STRICT LOANBAL;
    IF LOANBAL <= 0 THEN
        INTRECEIVABLE := 0;
        RETURN INTRECEIVABLE;
    END IF;
    SELECT F.PROD_ID ,F.LAST_INT_CALC_DT,TO_DATE(TO_CHAR(F.ACCT_CLOSE_DT,'dd-MM-yyyy'),'dd-MM-yyyy') INTO STRICT PRODUCTID, LASTINTCALCDATE,CLS_DATE
    FROM LOANS_FACILITY_DETAILS F
    WHERE F.ACCT_NUM = ACTNUM;
    SELECT coalesce(IS_INTEREST_PAID_FIRST,'N') INTO STRICT  INT_RECOVED_PARM FROM LOANS_PROD_ACPARAM WHERE PROD_ID = PRODUCTID;
    IF LASTINTCALCDATE = CLS_DATE THEN
        LASTINTCALCDATE := LASTINTCALCDATE - 1;
    END IF;
    --DBMS_OUTPUT.PUT_LINE(' LASTINTCALCDATE ' || LASTINTCALCDATE || ' CLS_DATE ' || CLS_DATE);
    IF CLS_DATE IS NOT NULL AND LASTINTCALCDATE + 1 = CLS_DATE THEN
        LASTINTCALCDATE := LOANDATE - 1;
    END IF;
    IF LASTINTCALCDATE < ASONDT THEN
        STARTDATE := LASTINTCALCDATE + 1;
    ELSE
        SELECT coalesce(MAX(T.TRANS_DT),LOANDATE) INTO STRICT STARTDATE 
        FROM LOAN_TRANS_DETAILS T
        WHERE T.ACT_NUM = ACTNUM AND T.TRANS_DT <= ASONDT;-- AND UPTO_DT_INT = 'Y';
    END IF;
    --DBMS_OUTPUT.PUT_LINE( ' STARTDATE ' || STARTDATE || ' CLS_DATE ' || CLS_DATE);
    IF VIRTUAL_ROI = 0 THEN
        IF INT_RECOVED_PARM ='Y' THEN
        SELECT coalesce(SUM(LT.INTEREST),0) INTO STRICT INTRECEIVED
        FROM LOAN_TRANS_DETAILS LT
        WHERE LT.ACT_NUM = ACTNUM AND LT.TRANS_DT = LOANDATE;
        ELSE
        SELECT coalesce(SUM(LT.INTEREST),0) INTO STRICT INTRECEIVED 
        FROM LOAN_TRANS_DETAILS LT
        WHERE LT.ACT_NUM = ACTNUM AND LT.TRANS_DT > STARTDATE AND LT.TRANS_DT <= ASONDT;
        END IF;
    END IF;
    SELECT CASE WHEN VIRTUAL_ROI <> 0 THEN VIRTUAL_ROI
                WHEN LFD.INT_GET_FROM = 'ACT' THEN (SELECT MAX(LIM.INTEREST) FROM LOANS_INT_MAINTENANCE LIM WHERE LIM.ACCT_NUM = LFD.ACCT_NUM AND LIM.STATUS != 'DELETED' )
                WHEN LFD.INT_GET_FROM = 'PROD' THEN
                     GET_TL_ROI(LFD.PROD_ID,LSD.FROM_DT,LSD.LIMIT)
                ELSE 0 END INTO STRICT ROI
    FROM LOANS_FACILITY_DETAILS LFD
    JOIN LOANS_SANCTION_DETAILS LSD ON LFD.BORROW_NO = LSD.BORROW_NO
    WHERE LFD.ACCT_NUM = ACTNUM;
    SELECT MIN(T.TRANS_DT) INTO STRICT FIRST_PAY_DATE
    FROM LOAN_TRANS_DETAILS T
    WHERE T.ACT_NUM = ACTNUM AND T.TRANS_TYPE = 'DEBIT';
    IF FIRST_PAY_DATE IS NOT NULL AND FIRST_PAY_DATE > (LASTINTCALCDATE + 1) THEN
        BEGIN
            SELECT (coalesce(D.AMT,0) * -1) INTO STRICT PRNBAL
            FROM LOANS_DAYEND_BALANCE D 
            WHERE D.ACT_NUM = ACTNUM AND D.DAY_END_DT = (LASTINTCALCDATE + 1);
        EXCEPTION WHEN OTHERS THEN
            SELECT GET_TL_BALANCE(ACTNUM,LASTINTCALCDATE+1) INTO STRICT PRNBAL;
        END;
         INTRECEIVABLE := ((PRNBAL * ((FIRST_PAY_DATE - LASTINTCALCDATE) - 1) * ROI)/36500);
         --DBMS_OUTPUT.PUT_LINE(' PRNBAL ' || PRNBAL || ' INTRECEIVABLE ' || INTRECEIVABLE || ' ROI ' || ROI);
         PRNBAL := 0;
    END IF;
    IF VIRTUAL_ROI <> 0 THEN
        ENDDATE := ENDDATE + 1;
    END IF;
    DECLARE LOANTRANS CURSOR FOR
    SELECT T.TRANS_DT,T.PRINCIPLE,T.TRN_CODE
    FROM LOAN_TRANS_DETAILS T
    WHERE T.ACT_NUM = ACTNUM AND T.TRANS_DT <= ENDDATE AND T.TRANS_DT >= STARTDATE AND T.PRINCIPLE > 0 
    UNION
    SELECT ENDDATE AS TRANS_DT,0 AS PRINCIPLE,'DP' AS TRN_CODE
        ORDER BY TRANS_DT ASC;
    TRANSDT LOAN_TRANS_DETAILS.TRANS_DT%TYPE;
    I double precision := 0;
    BEGIN
        FOR TRN IN LOANTRANS LOOP
            SELECT coalesce(SUM(CASE WHEN T.TRN_CODE = 'DP' /*T.TRANS_TYPE='DEBIT'*/ THEN 1 ELSE -1 END * T.PRINCIPLE),0) INTO STRICT PRNBAL
            FROM LOAN_TRANS_DETAILS T
            WHERE T.ACT_NUM = ACTNUM AND T.TRANS_DT < TRN.TRANS_DT;
            TRNDATE := TRN.TRANS_DT;
            INTRECEIVABLE := INTRECEIVABLE +  (PRNBAL * (TRN.TRANS_DT::date - STARTDATE::date) * ROI/ 36500) ;
            --DBMS_OUTPUT.PUT_LINE( ' INTRECEIVABLE ' || INTRECEIVABLE);
            STARTDATE := TRN.TRANS_DT;
        END LOOP;
    END;
    INTRECEIVABLE := INTRECEIVABLE - coalesce(INTRECEIVED,0);
    IF INTRECEIVABLE < 0 THEN
        INTRECEIVABLE := 0;
    END IF;
    INTRECEIVABLE := ROUND(INTRECEIVABLE);
    --DBMS_OUTPUT.PUT_LINE(' PRNBAL ' || PRNBAL || ' INTRECEIVABLE ' || INTRECEIVABLE || ' ROI ' || ROI);    
    RETURN INTRECEIVABLE;
END;
$function$
;

-- DROP FUNCTION chndata.get_tl_od_interest(text, timestamp);

CREATE OR REPLACE FUNCTION get_tl_od_interest(acnum text, asondate timestamp without time zone)
 RETURNS bigint
 LANGUAGE plpgsql
 STABLE SECURITY DEFINER
AS $function$
DECLARE
 
    PENROI numeric(4,2) := 0;
    PENAMT numeric(16,2) := 0;
    LSTINTDATE timestamp;
    INSTODDATE timestamp;
    ODAMT numeric(16,2) := 0;
    PENINT_RECEIVED numeric(16,2) := 0;
	BEGIN
    BEGIN
        SELECT GET_TL_PENROI(F.PROD_ID,F.ACCT_NUM,SD.FROM_DT,SD.LIMIT,F.INT_GET_FROM) INTO PENROI
        FROM LOANS_FACILITY_DETAILS F
        JOIN LOANS_SANCTION_DETAILS SD ON SD.BORROW_NO = F.BORROW_NO
        WHERE F.ACCT_NUM = ACNUM;
        IF PENROI IS NULL OR PENROI = 0 THEN
            RETURN PENAMT;
        END IF;
        IF GET_TL_BALANCE(ACNUM,ASONDATE) < 0 THEN
            RETURN PENAMT;
            --DBMS_OUTPUT.PUT_LINE(' NO BALANCE');
        END IF;
        SELECT (F.LAST_INT_CALC_DT + 1),GET_TL_INST_ODDATE(F.ACCT_NUM,ASONDATE) INTO LSTINTDATE,INSTODDATE
        FROM LOANS_FACILITY_DETAILS F
        WHERE F.ACCT_NUM = ACNUM;
        --DBMS_OUTPUT.PUT_LINE(' LSTINTDATE : ' || LSTINTDATE || '    INSTODDATE : ' || INSTODDATE);
        IF INSTODDATE IS NULL THEN
            SELECT GET_TL_INST_OD(ACNUM,ASONDATE) INTO ODAMT
            FROM DUAL;
            PENAMT := (ODAMT * (ASONDATE::date - LSTINTDATE::date) * PENROI/36500);
        ELSE
            SELECT COALESCE(SUM(b.ODAMT * b.NOD * PENROI/36500),0) INTO PENAMT
            FROM
                (
                    SELECT (ASONDATE::date - INST::date) AS NOD,PEN_AMT_OD AS ODAMT,INST
                    FROM
                        (        
                            SELECT CASE WHEN (I.INSTALLMENT_DT) < LSTINTDATE THEN LSTINTDATE ELSE I.INSTALLMENT_DT END AS INST,
                                   I.PRINCIPAL_AMT,
                                   CASE WHEN I.INSTALLMENT_DT <= INSTODDATE THEN 
                                             GET_TL_INST_OD_PENINT(I.ACCT_NUM,I.INSTALLMENT_DT,ASONDATE) 
                                        ELSE I.PRINCIPAL_AMT 
                                   END AS PEN_AMT_OD
                            FROM LOANS_INSTALLMENT I
                            WHERE I.ACCT_NUM = ACNUM AND I.INSTALLMENT_DT >= INSTODDATE AND I.INSTALLMENT_DT <= ASONDATE AND I.STATUS != 'DELETED'  
                        ) a
                ) b;
        END IF;
        SELECT SUM(COALESCE(T.PENAL,0)) INTO PENINT_RECEIVED
        FROM LOAN_TRANS_DETAILS T
        WHERE T.ACT_NUM = ACNUM AND T.TRANS_DT > LSTINTDATE AND T.TRANS_DT <= ASONDATE AND T.AUTHORIZE_STATUS = 'AUTHORIZED';
        PENINT_RECEIVED := COALESCE(PENINT_RECEIVED,0);
        --DBMS_OUTPUT.PUT_LINE(' PENAL AMOUNT ' || PENAMT);    
  --  EXCEPTION WHEN OTHERS THEN
       -- RETURN 0;
    END;
    PENAMT := PENAMT - PENINT_RECEIVED;
    IF PENAMT < 0 THEN 
        PENAMT := 0; 
    END IF;
    PENAMT := ROUND(PENAMT);
    --DBMS_OUTPUT.PUT_LINE(' PENAL AMOUNT ' || PENAMT);
    RETURN(PENAMT);  
END;
$function$
;


CREATE OR REPLACE FUNCTION data_check(param_value text, field text, where_field text, tbl text)
 RETURNS character varying
 LANGUAGE plpgsql
 STABLE SECURITY DEFINER
AS $function$
DECLARE

MSG varchar(350):='';
CNT bigint:=0;
sql_stmt varchar(200);

BEGIN
    sql_stmt:='SELECT COUNT(*) FROM '||TBL||' WHERE '||WHERE_FIELD||'= $1';
    EXECUTE sql_stmt INTO STRICT CNT USING PARAM_VALUE;
    IF CNT>0 THEN
        sql_stmt:='SELECT COUNT(*) FROM '||TBL||' WHERE '||FIELD||' IS NULL AND '||WHERE_FIELD||'= $1';
        EXECUTE sql_stmt INTO STRICT CNT USING PARAM_VALUE;
        IF CNT>0 THEN
            MSG:=INITCAP(FIELD)||' needs proper value';
        ELSE
            MSG:= null;
        END IF;
    ELSE
        MSG:=INITCAP(FIELD)||' needs proper value';
    END IF;
    RETURN MSG;
END;
$function$
;

-- UPI and QR Code generation added by nithya on 04 Jul 2025

CREATE TABLE UPI_QR_DETAILS
(
  QR_ACT_NUM   VARCHAR(16),
  QR_ACT_NAME  VARCHAR(100),
  QR_UPI_ID    VARCHAR(100),
  QR_BANK      VARCHAR(16),
  QR_FILE      bytea,
  STATUS       VARCHAR(16),
  STATUS_BY    VARCHAR(16),
  STATUS_DT    timestamp
);
    
Insert into SCREEN_MASTER
   (SCREEN_ID, MENU_ID, SCREEN_NAME, APP_ID, WF_STATUS, 
    MODULE_ID, SCREEN_CLASS, SL_NO, STATUS, SCREEN_TYPE, 
    RECORD_KEY_COL, SCREEN_DESC)
 Values
   ('SCR12251', '2248', 'UPI QRCode Generation', 'APP01', 'DONE', 
    '7', null, 7, 'CREATED', NULL, 
    NULL, 'UPI QRCode Generation');    

call insert_report_group('SCR12251');

Insert into CBMS_PARAMETERS
   (CBMS_KEY, CBMS_VALUE, DESCRIPTION)
Values
   ('SBI_UPI_BANK_CODE', 'kpdacb', NULL);
   
Insert into CBMS_PARAMETERS
   (CBMS_KEY, CBMS_VALUE, DESCRIPTION)
Values
   ('SBI_UPI_FORMAT', 'sbivan.kpdacb####@sbi', NULL);
   
Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
Values
   ('UPI_BANK', 'SBI', 'State Bank Of India', 'CREATED', NULL, 
    'Y');
        
Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
Values
   ('UPI_BANK', 'ICICI', 'ICICI', 'CREATED', NULL, 
    'Y');   
    
Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
 Values
   ('UPI_PRODUCT_TYPE', 'OA', 'Operative Account', 'CREATED', 'N', 
    'Y');    

-- Added by nithya for limit checking

INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('OD_CASH_PAYMENT_LIMIT', '999999', NULL);


INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('OD_CASH_RECEIPT_LIMIT', '999999', NULL);


INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('GL_CASH_PAYMENT_LIMIT', '999999', NULL);


INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('GL_CASH_RECEIPT_LIMIT', '999999', NULL);


INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('DL_CASH_PAYMENT_LIMIT', '999999', NULL);


INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('DL_CASH_RECEIPT_LIMIT', '999999', NULL);


INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('TL_CASH_PAYMENT_LIMIT', '999999', NULL);


INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('TL_CASH_RECEIPT_LIMIT', '999999', NULL);


INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('MSS_CASH_PAYMENT_LIMIT', '999999', NULL);


INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('MSS_CASH_RECEIPT_LIMIT', '999999', NULL);


INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('SA_CASH_PAYMENT_LIMIT', '999999', NULL);


INSERT INTO cbms_parameters
(cbms_key, cbms_value, description)
VALUES('SA_CASH_RECEIPT_LIMIT', '999999', NULL);