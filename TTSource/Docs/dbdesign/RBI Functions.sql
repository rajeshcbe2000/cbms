DROP TABLE LOAN_APPLICATION_PARAM_ECS CASCADE CONSTRAINTS;

CREATE TABLE LOAN_APPLICATION_PARAM_ECS
(
  PROD_ID                 VARCHAR2(16 BYTE),
  SALARY_ELIGIB_SURETY    NUMBER(4),
  SALARY_ELIGIB_BORROWER  NUMBER(4),
  COST_OF_VEHICLE         NUMBER(4),
  MAX_ELIGAMT             NUMBER(12,2),
  BORROWER_SHARE          NUMBER(3,2),
  SURETY_SHARE            NUMBER(3,2)
);

SET DEFINE OFF;
Insert into LOAN_APPLICATION_PARAM_ECS
   (PROD_ID, SALARY_ELIGIB_SURETY, SALARY_ELIGIB_BORROWER, COST_OF_VEHICLE, MAX_ELIGAMT, 
    BORROWER_SHARE, SURETY_SHARE)
 Values
   ('101', 5, 4, 0, 300000, 
    0.5, 0.3);
Insert into LOAN_APPLICATION_PARAM_ECS
   (PROD_ID, SALARY_ELIGIB_SURETY, SALARY_ELIGIB_BORROWER, COST_OF_VEHICLE, MAX_ELIGAMT, 
    BORROWER_SHARE, SURETY_SHARE)
 Values
   ('303', 20, 15, 0, NULL, 
    0.5, 0.3);
Insert into LOAN_APPLICATION_PARAM_ECS
   (PROD_ID, SALARY_ELIGIB_SURETY, SALARY_ELIGIB_BORROWER, COST_OF_VEHICLE, MAX_ELIGAMT, 
    BORROWER_SHARE, SURETY_SHARE)
 Values
   ('309', 5, 10, 90, NULL, 
    0.5, 0.3);
COMMIT;



DROP TYPE TAB_TYPE_LOAN_ELIG_BORROWER;

DROP TYPE TYPE_LOAN_ELIG_BORROWER;

CREATE OR REPLACE TYPE TYPE_LOAN_ELIG_BORROWER AS OBJECT (
   IMBP          number(16,2),
   MAXLOANAMTPROD     NUMBER (16, 2),
   OPDATE       Date,
   EXISTINGLOANBAL   Number(16,2),
   SHAREOS         Number(16,2),
   SHARERECOVERABLE Number(16,2),
   MAXSHARELIMIT    NUMBER (16, 2),
   TOBERECOVERED    NUMBER (16, 2),
   CLOSE_BEFORE     NUMBER (10),
   MAXLOANPERIODINMONTHS Number(10),
   VEHICLEAMT number(16,2),   
   Networth       NUMBER(16,2),
   Eligibleamt Number(16,2)
);
/

CREATE OR REPLACE TYPE TAB_TYPE_LOAN_ELIG_BORROWER AS TABLE OF TYPE_LOAN_ELIG_BORROWER;
/

CREATE OR REPLACE FUNCTION GET_EXISTINGLOANBAL(MEMBERNO VARCHAR,PRODID VARCHAR,ASONDT DATE) RETURN NUMBER IS
--DECLARE
--MEMBERNO VARCHAR2(20) := 'A1096';
--PRODID VARCHAR2(10) := '309';
--ASONDT DATE := '12-JAN-2016';
EXISTINGLOANBAL NUMBER(16,2);
BEGIN
BEGIN
SELECT GET_TL_BALANCE(LFD.ACCT_NUM,ASONDT) INTO EXISTINGLOANBAL  FROM LOANS_FACILITY_DETAILS LFD,LOANS_BORROWER LB,SHARE_ACCT SA
        WHERE LFD.BORROW_NO = LB.BORROW_NO
        AND LB.CUST_ID = SA.CUST_ID
        AND SA.SHARE_ACCT_NO = MEMBERNO  AND LFD.ACCT_STATUS = 'NEW' AND LFD.PROD_ID = PRODID;
EXCEPTION WHEN OTHERS THEN
EXISTINGLOANBAL := 0;
END;
--DBMS_OUTPUT.PUT_LINE('EXISTINGLOANBAL  ='||EXISTINGLOANBAL);
IF EXISTINGLOANBAL IS NULL THEN
EXISTINGLOANBAL := 0;
END IF;
--DBMS_OUTPUT.PUT_LINE('EXISTINGLOANBAL  ='||EXISTINGLOANBAL);
RETURN EXISTINGLOANBAL;
END;
/


CREATE OR REPLACE FUNCTION COREDB_RBI.LOAD_LOAN_ELG_BORROWER(MEMBERNO IN VARCHAR2,PRODID IN VARCHAR,TOTALSALARY IN NUMBER,APPLYTYPE IN VARCHAR,APPLIEDAMT IN NUMBER ,COSTOFVEHICLE IN NUMBER ) RETURN TAB_TYPE_LOAN_ELIG_BORROWER PIPELINED IS
PRAGMA AUTONOMOUS_TRANSACTION;
TYPE REF0 IS REF CURSOR;
    TRCURSOR REF0;
    OUTREC TYPE_LOAN_ELIG_BORROWER := TYPE_LOAN_ELIG_BORROWER(0,0,NULL,0,0,0,0,0,0,0,0,0,0);
    APPLIEDTYPE VARCHAR2(20);
    APPLIED_AMT NUMBER(12,2);
BEGIN
APPLIEDTYPE := APPLYTYPE;
APPLIED_AMT := APPLIEDAMT;
OPEN TRCURSOR FOR
        WITH IMBP AS (
        SELECT IMBP.MAX_LOAN_AMOUNT AS IMBP
          FROM IMBP_SETTINGS IMBP
          WHERE IMBP.EFFECT_FROM = (SELECT MAX(IM.EFFECT_FROM) FROM IMBP_SETTINGS IM,IMBP_SETTINGS_PROD ISP
          WHERE ISP.IMBP_ID = IM.IMBP_ID
          AND ISP.PROD_ID = PRODID)
        ),
        MAXLOANAMTPROD AS 
        (
        SELECT NVL ((SELECT NVL (LPI.MAX_AMT_LOAN, 0)
           FROM LOANS_PROD_INTCALC LPI
          WHERE LPI.PROD_ID = PRODID),
        (SELECT NVL (L.MAX_ELIGAMT, 0)
           FROM LOAN_APPLICATION_PARAM_ECS L
          WHERE L.PROD_ID = PRODID)
       ) AS MAXLOANAMTPROD
        FROM DUAL
        ),
        CURRENTAPPLICATIONDATE AS 
        (
        SELECT DE.CURR_APPL_DT AS OPDATE
          FROM DAY_END DE
        WHERE DE.BRANCH_CODE = '0001' AND DE.END_DAY_STATUS IS NULL
        ),
        EXISTINGLOANBAL AS 
        (SELECT GET_EXISTINGLOANBAL(MEMBERNO,PRODID,CAD.OPDATE) AS EXISTINGLOANBAL FROM CURRENTAPPLICATIONDATE CAD  
        ),
        SHAREOS AS (
        SELECT SUM(CASE WHEN SAD.SHARE_NO_FROM = 'ADD' THEN  1 ELSE -1 END * SAD.SHARE_VALUE) AS SHAREOS   FROM SHARE_ACCT SA,SHARE_ACCT_DETAILS SAD
        WHERE SA.SHARE_ACCT_NO = SAD.SHARE_ACCT_NO
        AND SA.SHARE_ACCT_NO = MEMBERNO
        ),
        SHARERECOVERABLE AS 
        (/*
        SELECT  CASE WHEN APPLIED_AMT = 0 THEN (SELECT (NVL(SALARY_ELIGIB_BORROWER,0) * TOTALSALARY)  * BORROWER_SHARE FROM LOAN_APPLICATION_PARAM_ECS LPE
        WHERE LPE.PROD_ID = PRODID) ELSE APPLIED_AMT END /100 AS SHARERECOVERABLE FROM LOAN_APPLICATION_PARAM_ECS LAPE
        WHERE LAPE.PROD_ID = PRODID
        */
        SELECT (APPLIEDAMT  * BORROWER_SHARE)/100 AS SHARERECOVERABLE FROM LOAN_APPLICATION_PARAM_ECS LAPE
        WHERE LAPE.PROD_ID = PRODID
        ),
        MAXSHARELIMIT AS 
        (SELECT SCD.FACE_VALUE * SCD.MAXIMUM_SHARE AS MAXSHARELIMIT FROM SHARE_CONF_DETAILS SCD 
        WHERE SCD.SHARE_TYPE = SUBSTR(MEMBERNO,1,1)
        ),
        SHARETOBERECOVERED AS 
        (
        SELECT  CASE WHEN SHAREOS >= MAXSHARELIMIT THEN  0 WHEN SHARERECOVERABLE > SHAREOS THEN  CASE WHEN SHARERECOVERABLE - SHAREOS > MAXSHARELIMIT THEN MAXSHARELIMIT ELSE SHARERECOVERABLE - SHAREOS END END TOBERECOVERED
        FROM MAXSHARELIMIT MSL,SHARERECOVERABLE SR,SHAREOS SOS
        ),
        CLOSEBEFORE AS 
        (SELECT PSC.CLOSE_BEFORE FROM PERSONAL_SURETY_CONFIGURATION PSC WHERE PSC.PERSONAL_SURETY_ID = (SELECT MAX(PS.PERSONAL_SURETY_ID) FROM PERSONAL_SURETY_CONFIGURATION PS 
        WHERE PS.PERSONAL_SURETY_ID = PSC.PERSONAL_SURETY_ID)), 
        MAXLOANPERIODBORROWER AS 
        (SELECT FLOOR(MONTHS_BETWEEN(ADD_MONTHS(C.RETIREMENT_DT,CB.CLOSE_BEFORE*(-1)),CAD.OPDATE)) AS MAXLOANPERIODINMONTHS FROM SHARE_ACCT SA,CUSTOMER C,CLOSEBEFORE CB,CURRENTAPPLICATIONDATE CAD WHERE C.CUST_ID = SA.CUST_ID
        AND SA.SHARE_ACCT_NO = MEMBERNO),
        ELGLOANAMT_AGAINSTSAL AS
        (SELECT NVL(SALARY_ELIGIB_BORROWER,0) * TOTALSALARY  AS NETWORTH,NVL(COSTOFVEHICLE * (COST_OF_VEHICLE/100),0) AS VEHICLEAMT    FROM LOAN_APPLICATION_PARAM_ECS  WHERE PROD_ID = PRODID)
        SELECT I.IMBP,ML.MAXLOANAMTPROD,CAD.OPDATE,ELB.EXISTINGLOANBAL,SO.SHAREOS,SR.SHARERECOVERABLE,MSL.MAXSHARELIMIT,STB.TOBERECOVERED,CB.CLOSE_BEFORE,MLPB.MAXLOANPERIODINMONTHS,ELAS.NETWORTH,ELAS.VEHICLEAMT,
        CASE WHEN APPLIED_AMT = 0 THEN
                CASE WHEN APPLIEDTYPE = 'NEW' THEN 
                             CASE WHEN ELAS.NETWORTH > I.IMBP THEN
                                  CASE WHEN APPLIED_AMT > ML.MAXLOANAMTPROD THEN  ML.MAXLOANAMTPROD
                                      ELSE APPLIED_AMT
                                  END
                             ELSE
                                  CASE WHEN APPLIED_AMT > ML.MAXLOANAMTPROD THEN  ML.MAXLOANAMTPROD
                                      ELSE APPLIED_AMT
                                  END
                             END                        
                ELSE
                    CASE WHEN MLPB.MAXLOANPERIODINMONTHS <= 0 THEN 0
                    ELSE 
                        CASE WHEN ELAS.NETWORTH > I.IMBP THEN
                             CASE WHEN APPLIEDAMT > ML.MAXLOANAMTPROD THEN  (ML.MAXLOANAMTPROD - (APPLIEDAMT - ELB.EXISTINGLOANBAL))
                             ELSE (ML.MAXLOANAMTPROD - ELB.EXISTINGLOANBAL) END 
                        ELSE 
                             CASE WHEN APPLIEDAMT > ML.MAXLOANAMTPROD THEN  (ML.MAXLOANAMTPROD - (APPLIEDAMT - ELB.EXISTINGLOANBAL))
                             ELSE (ML.MAXLOANAMTPROD - ELB.EXISTINGLOANBAL) END
                        END 
                    END
                END 
        ELSE 
                CASE WHEN APPLIEDTYPE = 'NEW' THEN 
                        CASE WHEN ELB.EXISTINGLOANBAL > 0 THEN 0
                        ELSE
                             CASE WHEN ELAS.NETWORTH > I.IMBP THEN
                                  CASE WHEN APPLIEDAMT > ML.MAXLOANAMTPROD THEN  ML.MAXLOANAMTPROD
                                      ELSE APPLIEDAMT
                                  END
                             ELSE
                                  CASE WHEN APPLIEDAMT > ML.MAXLOANAMTPROD THEN  ML.MAXLOANAMTPROD
                                      ELSE APPLIEDAMT
                                  END
                             END
                        END
                ELSE
                    CASE WHEN MLPB.MAXLOANPERIODINMONTHS <= 0 THEN 0
                    ELSE 
                        CASE WHEN ELAS.NETWORTH > I.IMBP THEN
                             CASE WHEN APPLIEDAMT > ML.MAXLOANAMTPROD THEN  (ML.MAXLOANAMTPROD - (APPLIEDAMT - ELB.EXISTINGLOANBAL))
                             ELSE (ML.MAXLOANAMTPROD - ELB.EXISTINGLOANBAL) END 
                        ELSE 
                             CASE WHEN APPLIEDAMT > ML.MAXLOANAMTPROD THEN  (ML.MAXLOANAMTPROD - (APPLIEDAMT - ELB.EXISTINGLOANBAL))
                             ELSE (ELAS.NETWORTH - ELB.EXISTINGLOANBAL) + ELB.EXISTINGLOANBAL END
                        END 
                    END
                END
        END   AS ELIGIBLEAMT
        FROM IMBP I,MAXLOANAMTPROD ML,CURRENTAPPLICATIONDATE CAD,EXISTINGLOANBAL ELB,SHAREOS SO,SHARERECOVERABLE SR,MAXSHARELIMIT MSL,
        SHARETOBERECOVERED STB,CLOSEBEFORE CB,MAXLOANPERIODBORROWER MLPB,ELGLOANAMT_AGAINSTSAL ELAS;
        LOOP
            FETCH TRCURSOR INTO OUTREC.IMBP,OUTREC.MAXLOANAMTPROD,OUTREC.OPDATE,OUTREC.EXISTINGLOANBAL,
                                OUTREC.SHAREOS,OUTREC.SHARERECOVERABLE,OUTREC.MAXSHARELIMIT,OUTREC.TOBERECOVERED,OUTREC.CLOSE_BEFORE,
                                OUTREC.MAXLOANPERIODINMONTHS,OUTREC.NETWORTH,OUTREC.VEHICLEAMT,OUTREC.ELIGIBLEAMT;
            EXIT WHEN TRCURSOR%NOTFOUND;
            PIPE ROW(OUTREC);                               
        END LOOP;
CLOSE TRCURSOR;
    RETURN;
END;
/

select * from table (LOAD_LOAN_ELG_BORROWER('A1096','309',100000,'NEW',1000000 ,700000))


Create or Replace Function Get_OD_ExpDate(MemberNo Varchar,opDate Date) return Date is
RetireDt Date;
ExpiryDate Date;
Begin
begin
Select C.RETIREMENT_DT into RetireDt from Customer c where C.MEMBERSHIP_NO = MemberNo;
Exception when others then
dbms_output.put_line('No Data');
End;
Select add_months(to_date(opDate),12) into ExpiryDate from dual;
if ExpiryDate > RetireDt then
ExpiryDate := RetireDt;
End if;
Return ExpiryDate;
End;

  
Select Get_OD_ExpDate('A0366','05-may-2016') from dual;


======================================


Create or Replace Function get_borrower_no(acctnum varchar) return varchar is
custid varchar2(20);
MemberNo varchar2(20);
Begin
Select Cust_id into custid from 
(select cust_id  from LOANS_borrower lb where LB.APPLICATION_NO = acctnum
Union
Select cust_id from LOANS_BORROWER lb,LOANS_FACILITY_DETAILS lfd
where  LB.BORROW_NO = LFD.BORROW_NO and  LFD.ACCT_NUM = acctnum);
Select SA.SHARE_ACCT_NO into MemberNo from Share_acct sa where SA.CUST_ID = custid;
Return MemberNo;
End;

===================================================================



CREATE OR REPLACE FUNCTION GET_SURETY_EXISISTING_BAL(MEMBERNO VARCHAR,ASONDT
DATE) RETURN NUMBER IS
--DECLARE 
--MEMBERNO VARCHAR2(20) := 'A1096';
--ASONDT DATE := '05-FEB-2016';
SURETYSTANDBALOS NUMBER(12,2) := 0;
BEGIN
BEGIN
SELECT   SUM (CASE
                 WHEN LP.BEHAVES_LIKE = 'OD'
                    THEN GET_ADV_BALANCE (LFD.ACCT_NUM, ASONDT)
                 ELSE GET_TL_BALANCE (LFD.ACCT_NUM, ASONDT)
              END
             )
    INTO SURETYSTANDBALOS
    FROM LOANS_SECURITY_MEMBER LSM,
         LOANS_FACILITY_DETAILS LFD,      LOANS_BORROWER LB,
         SHARE_ACCT SA,
         LOANS_PRODUCT LP
   WHERE LP.PROD_ID = LFD.PROD_ID
     AND LSM.ACCT_NUM = LB.APPLICATION_NO
     AND LB.BORROW_NO = LFD.BORROW_NO
     AND SA.SHARE_ACCT_NO = LSM.MEMBER_NO
     AND LSM.MEMBER_NO = MEMBERNO
     AND LFD.ACCT_STATUS = 'NEW'
     AND LP.AUTHORIZE_REMARK = 'OTHER_LOAN'
     AND LP.BEHAVES_LIKE IN ('OD', 'SI_BEARING')GROUP BY LSM.MEMBER_NO;
EXCEPTION WHEN OTHERS THEN
SURETYSTANDBALOS := 0;
END;
IF SURETYSTANDBALOS IS NULL THEN
SURETYSTANDBALOS := 0;
END IF;
RETURN SURETYSTANDBALOS ;
END;
==================================================================
CREATE OR REPLACE FUNCTION GET_EXISISTING_SURETYCOUNT(BORROWMEMNO VARCHAR,SURETYMEMBERNO VARCHAR) RETURN NUMBER IS
EXISTING_SRTYCOUNT_OFSAMEPROD NUMBER(12,2);
BEGIN
BEGIN
SELECT SUM(COUNTR)  INTO EXISTING_SRTYCOUNT_OFSAMEPROD FROM(
        SELECT 1 AS COUNTR,LSM.ACCT_NUM, GET_BORROWER_NO(LSM.ACCT_NUM) AS BORROWER FROM LOANS_SECURITY_MEMBER LSM WHERE LSM.MEMBER_NO = SURETYMEMBERNO
        AND LSM.ACCT_NUM IN (SELECT LFD.ACCT_NUM FROM LOANS_FACILITY_DETAILS LFD,LOANS_BORROWER LB 
        WHERE LFD.BORROW_NO = LB.BORROW_NO
        AND LFD.ACCT_STATUS = 'NEW')
        ) A,SHARE_ACCT SA WHERE A.BORROWER = SA.SHARE_ACCT_NO AND SA.SHARE_ACCT_NO= BORROWMEMNO
        GROUP BY  A.ACCT_NUM, A.BORROWER;
EXCEPTION WHEN OTHERS THEN
EXISTING_SRTYCOUNT_OFSAMEPROD := 0;
END;
IF EXISTING_SRTYCOUNT_OFSAMEPROD IS NULL THEN
EXISTING_SRTYCOUNT_OFSAMEPROD := 0;
END IF;
RETURN EXISTING_SRTYCOUNT_OFSAMEPROD;
END;

==========================================================================


DROP TYPE TAB_TYPE_LOAN_ELIG_SURETY;

DROP TYPE TYPE_LOAN_ELIG_SURETY;

CREATE OR REPLACE TYPE TYPE_LOAN_ELIG_SURETY AS OBJECT (
   IMBP          number(16,2),
   MAXLOANAMTPROD     NUMBER (16, 2),
   OPDATE       Date,
   SURETYSTANDBALOS   Number(16,2),
   EXISTING_SRTYCOUNT_OFSAMEPROD NUMBER(10),
   SHAREOS         Number(16,2),
   SHARERECOVERABLE Number(16,2),
   MAXSHARELIMIT    NUMBER (16, 2),
   TOBERECOVERED    NUMBER (16, 2),
   CLOSE_BEFORE     NUMBER (10),
   MAXLOANPERIODINMONTHS Number(10),
   Networth       NUMBER(16,2),
   Eligibleamt Number(16,2)
);
/

CREATE OR REPLACE TYPE TAB_TYPE_LOAN_ELIG_SURETY AS TABLE OF TYPE_LOAN_ELIG_SURETY;
/


CREATE OR REPLACE FUNCTION COREDB_RBI.LOAD_LOAN_ELG_SURETY(SURETYMEMBERNO IN VARCHAR2, BORROWMEMNO IN VARCHAR2, PRODID IN VARCHAR,TOTALSALARY IN NUMBER,APPLYTYPE IN VARCHAR,APPLIEDAMT IN NUMBER) RETURN TAB_TYPE_LOAN_ELIG_SURETY PIPELINED IS
PRAGMA AUTONOMOUS_TRANSACTION;
TYPE REF0 IS REF CURSOR;
    TRCURSOR REF0;
    OUTREC TYPE_LOAN_ELIG_SURETY := TYPE_LOAN_ELIG_SURETY(0,0,NULL,0,0,0,0,0,0,0,0,0,0);
    APPLIEDTYPE VARCHAR2(20);
    APPLIED_AMT NUMBER(12,2);
BEGIN
APPLIEDTYPE := APPLYTYPE;
APPLIED_AMT := APPLIEDAMT;
OPEN TRCURSOR FOR
       WITH IMBP AS (        
        SELECT IMBP.MAX_LOAN_AMOUNT AS IMBP
          FROM IMBP_SETTINGS IMBP
          WHERE IMBP.EFFECT_FROM = (SELECT MAX(IM.EFFECT_FROM) FROM IMBP_SETTINGS IM,IMBP_SETTINGS_PROD ISP
          WHERE ISP.IMBP_ID = IM.IMBP_ID
          AND ISP.PROD_ID = PRODID)),
        MAXLOANAMTPROD AS 
        (
        SELECT NVL ((SELECT NVL (LPI.MAX_AMT_LOAN, 0)
           FROM LOANS_PROD_INTCALC LPI
          WHERE LPI.PROD_ID = PRODID),
        (SELECT NVL (L.MAX_ELIGAMT, 0)
           FROM LOAN_APPLICATION_PARAM_ECS L
          WHERE L.PROD_ID = PRODID)
       ) AS MAXLOANAMTPROD
       FROM DUAL
        ),
        CURRENTAPPLICATIONDATE AS 
        (
        SELECT DE.CURR_APPL_DT AS OPDATE
          FROM DAY_END DE
        WHERE DE.BRANCH_CODE = '0001' AND DE.END_DAY_STATUS IS NULL
        ),
        EXISTINGLOANBAL AS 
        (SELECT GET_SURETY_EXISISTING_BAL(SURETYMEMBERNO,MAX(CAD.OPDATE) ) AS SURETYSTANDBALOS FROM CURRENTAPPLICATIONDATE CAD
        ),
        EXISTINGSURETY AS 
        (
        SELECT GET_EXISISTING_SURETYCOUNT(BORROWMEMNO,SURETYMEMBERNO) AS EXISTING_SRTYCOUNT_OFSAMEPROD FROM DUAL
        ),
        SHAREOS AS
        (
        SELECT SUM(CASE WHEN SAD.SHARE_NO_FROM = 'ADD' THEN  1 ELSE -1 END * SAD.SHARE_VALUE) AS SHAREOS   FROM SHARE_ACCT SA,SHARE_ACCT_DETAILS SAD
        WHERE SA.SHARE_ACCT_NO = SAD.SHARE_ACCT_NO
        AND SA.SHARE_ACCT_NO = SURETYMEMBERNO
        ),
        SHARERECOVERABLE AS 
        (
        /*
        SELECT  CASE WHEN APPLIED_AMT = 0 THEN (SELECT (NVL(SALARY_ELIGIB_BORROWER,0) * TOTALSALARY)  * BORROWER_SHARE FROM LOAN_APPLICATION_PARAM_ECS LPE
        WHERE LPE.PROD_ID = PRODID) ELSE APPLIED_AMT END /100 AS SHARERECOVERABLE FROM LOAN_APPLICATION_PARAM_ECS LAPE
        WHERE LAPE.PROD_ID = PRODID
        */
        SELECT (APPLIEDAMT  * SURETY_SHARE)/100 AS SHARERECOVERABLE FROM LOAN_APPLICATION_PARAM_ECS LAPE
        WHERE LAPE.PROD_ID = PRODID
        ),
        MAXSHARELIMIT AS 
        (SELECT SCD.FACE_VALUE * SCD.MAXIMUM_SHARE AS MAXSHARELIMIT FROM SHARE_CONF_DETAILS SCD 
        WHERE SCD.SHARE_TYPE = SUBSTR(SURETYMEMBERNO,1,1)
        ),
        SHARETOBERECOVERED AS 
        (
        SELECT  CASE WHEN SHAREOS >= MAXSHARELIMIT THEN  0 WHEN SHARERECOVERABLE > SHAREOS THEN  CASE WHEN SHARERECOVERABLE - SHAREOS > MAXSHARELIMIT THEN MAXSHARELIMIT ELSE SHARERECOVERABLE - SHAREOS END END TOBERECOVERED
        FROM MAXSHARELIMIT MSL,SHARERECOVERABLE SR,SHAREOS SOS
        ),
        CLOSEBEFORE AS 
        (SELECT PSC.CLOSE_BEFORE FROM PERSONAL_SURETY_CONFIGURATION PSC WHERE PSC.PERSONAL_SURETY_ID = (SELECT MAX(PS.PERSONAL_SURETY_ID) FROM PERSONAL_SURETY_CONFIGURATION PS 
        WHERE PS.PERSONAL_SURETY_ID = PSC.PERSONAL_SURETY_ID)), 
        MAXLOANPERIODSURETY AS 
        (SELECT FLOOR(MONTHS_BETWEEN(ADD_MONTHS(C.RETIREMENT_DT,CB.CLOSE_BEFORE*(-1)),CAD.OPDATE)) AS MAXLOANPERIODINMONTHS FROM SHARE_ACCT SA,CUSTOMER C,CLOSEBEFORE CB,CURRENTAPPLICATIONDATE CAD WHERE C.CUST_ID = SA.CUST_ID
        AND SA.SHARE_ACCT_NO = SURETYMEMBERNO),
        ELGLOANAMT_AGAINSTSAL AS
        (SELECT NVL(SALARY_ELIGIB_SURETY,0) * TOTALSALARY  AS NETWORTH FROM LOAN_APPLICATION_PARAM_ECS WHERE PROD_ID = PRODID)        
        SELECT IMBP,MAXLOANAMTPROD,OPDATE,SURETYSTANDBALOS,EXISTING_SRTYCOUNT_OFSAMEPROD,SHAREOS,SHARERECOVERABLE,MAXSHARELIMIT,
        TOBERECOVERED,CLOSE_BEFORE,MAXLOANPERIODINMONTHS,NETWORTH,
        CASE WHEN MAXLOANPERIODINMONTHS <= 0 THEN 0
        ELSE 
            CASE WHEN NETWORTH > IMBP THEN
                 CASE WHEN APPLIEDAMT > NETWORTH - SURETYSTANDBALOS THEN
                  (NETWORTH - SURETYSTANDBALOS) ELSE MAXLOANAMTPROD
                 END ELSE (NETWORTH - SURETYSTANDBALOS)  
            END 
        END AS ELIGIBLEAMT
        FROM IMBP,MAXLOANAMTPROD,CURRENTAPPLICATIONDATE,EXISTINGLOANBAL,EXISTINGSURETY,SHAREOS,SHARERECOVERABLE,MAXSHARELIMIT,SHARETOBERECOVERED,CLOSEBEFORE,MAXLOANPERIODSURETY,ELGLOANAMT_AGAINSTSAL;
                LOOP
            FETCH TRCURSOR INTO OUTREC.IMBP,OUTREC.MAXLOANAMTPROD,OUTREC.OPDATE,OUTREC.SURETYSTANDBALOS,OUTREC.EXISTING_SRTYCOUNT_OFSAMEPROD,
                                OUTREC.SHAREOS,OUTREC.SHARERECOVERABLE,OUTREC.MAXSHARELIMIT,OUTREC.TOBERECOVERED,OUTREC.CLOSE_BEFORE,
                                OUTREC.MAXLOANPERIODINMONTHS, OUTREC.NETWORTH,OUTREC.ELIGIBLEAMT;
            EXIT WHEN TRCURSOR%NOTFOUND;
            PIPE ROW(OUTREC);                               
        END LOOP;
    CLOSE TRCURSOR;
    RETURN;
END;
/


SELECT * FROM TABLE(LOAD_LOAN_ELG_SURETY('A1097','A1096','309',100000,'NEW',500000))

===========================================================================================
13-JUN-2016
===================================
Drop  type "COREDB_RBI"."TAB_TYPE_INTR_PROCESS"

Drop  type "COREDB_RBI"."TYPE_INTR_PROCESS"


CREATE OR REPLACE TYPE "COREDB_RBI"."TYPE_INTR_PROCESS" AS OBJECT (
   ACTNUM                          VARCHAR2(13),
   CREDITINT                      NUMBER (16, 2),
   DEBITINT                       NUMBER (16, 2)
);
/


CREATE OR REPLACE TYPE "COREDB_RBI"."TAB_TYPE_INTR_PROCESS" AS TABLE OF TYPE_INTR_PROCESS;
/


================

CREATE OR REPLACE FUNCTION COREDB_RBI.GET_OA_ROI_DRINT(ACTNUM VARCHAR )
RETURN NUMBER IS
--DECLARE 
--ACTNUM VARCHAR2(13) := '0001101001785';
    INTEREST NUMBER(16,2) := 0;
    TMP_INT_GET_FROM VARCHAR2(10);
    PRODID VARCHAR2(4);
    LOANDATE DATE;
    LOANAMOUNT NUMBER(16,2); 
    INTGETFROM VARCHAR2(4):= NULL;
    CATG varchar2(32);
    CUSTID VARCHAR2(20);
    LOANDUEDT date;
    RETIREDATE DATE;
    CREATDT DATE;
BEGIN
SELECT AM.PROD_ID, CATEGORY_ID,AM.CUST_ID,AM.CREATE_DT
  INTO PRODID, CATG, CUSTID,CREATDT
  FROM ACT_MASTER AM, OP_AC_PRODUCT OA
WHERE AM.PROD_ID = OA.PROD_ID
AND AM.ACT_NUM = ACTNUM ;
SELECT GET_RETIRE_DATE(CUSTID) INTO RETIREDATE FROM DUAL;
if RETIREDATE is null then
RETIREDATE := add_months(CREATDT,1);
End if;
--DBMS_OUTPUT.PUT_LINE('PRODID  '||PRODID||'  '||CATG||'   '||CUSTID||'  '||CREATDT);
               BEGIN
                        SELECT DRGTR.ROI INTO INTEREST
                      FROM DEPOSIT_ROI_GROUP DRG,
                           DEPOSIT_ROI_GROUP_CAT DRGC,
                           DEPOSIT_ROI_GROUP_PROD DRGP,
                           DEPOSIT_ROI_GROUP_TYPE_RATE DRGTR
                     WHERE DRG.ROI_GROUP_ID = DRGC.ROI_GROUP_ID
                       AND DRG.ROI_GROUP_ID = DRGP.ROI_GROUP_ID
                       AND DRG.ROI_GROUP_ID = DRGTR.ROI_GROUP_ID
                       AND DRG.PRODUCT_TYPE IN ('OA')
                       AND DRGP.PROD_ID = PRODID
                       AND DRGC.CATEGORY_ID = CATG
                       AND 1 BETWEEN 1 AND 9999999999
                       AND (   (to_date(CREATDT) >= ROI_DATE AND to_date(CREATDT) <= ROI_END_DATE)
                            OR (ROI_DATE <= to_date(CREATDT) AND ROI_END_DATE IS NULL)
                           )
                       AND NVL(( to_date(RETIREDATE)- to_date(CREATDT)),0)
                              BETWEEN FROM_PERIOD
                                  AND TO_PERIOD AND DRGTR.INT_TYPE = 'D';              
               EXCEPTION WHEN OTHERS THEN
                 DBMS_OUTPUT.PUT_LINE('ERROR');
               END;
if Interest is null then
RETURN 0;
Else
return Interest;
End if; 
--DBMS_OUTPUT.PUT_LINE('PRODID  '||PRODID||'  '||CATG||'   '||CUSTID||'  '||CREATDT||' '||INTEREST );
END;
/


============================================================================================


CREATE OR REPLACE FUNCTION COREDB_RBI.GET_OA_ROI_CRINT(ACTNUM VARCHAR )
RETURN NUMBER IS
--DECLARE 
--ACTNUM VARCHAR2(13) := '0001101001785';
    INTEREST NUMBER(16,2) := 0;
    TMP_INT_GET_FROM VARCHAR2(10);
    PRODID VARCHAR2(4);
    LOANDATE DATE;
    LOANAMOUNT NUMBER(16,2); 
    INTGETFROM VARCHAR2(4):= NULL;
    CATG varchar2(32);
    CUSTID VARCHAR2(20);
    LOANDUEDT date;
    RETIREDATE DATE;
    CREATDT DATE;
BEGIN
SELECT AM.PROD_ID, CATEGORY_ID,AM.CUST_ID,AM.CREATE_DT
  INTO PRODID, CATG, CUSTID,CREATDT
  FROM ACT_MASTER AM, OP_AC_PRODUCT OA
WHERE AM.PROD_ID = OA.PROD_ID
AND AM.ACT_NUM = ACTNUM ;
SELECT GET_RETIRE_DATE(CUSTID) INTO RETIREDATE FROM DUAL;
if RETIREDATE is null then
RETIREDATE := add_months(CREATDT,1);
End if;
--DBMS_OUTPUT.PUT_LINE('PRODID  '||PRODID||'  '||CATG||'   '||CUSTID||'  '||CREATDT);
               BEGIN
                        SELECT DRGTR.ROI INTO INTEREST
                      FROM DEPOSIT_ROI_GROUP DRG,
                           DEPOSIT_ROI_GROUP_CAT DRGC,
                           DEPOSIT_ROI_GROUP_PROD DRGP,
                           DEPOSIT_ROI_GROUP_TYPE_RATE DRGTR
                     WHERE DRG.ROI_GROUP_ID = DRGC.ROI_GROUP_ID
                       AND DRG.ROI_GROUP_ID = DRGP.ROI_GROUP_ID
                       AND DRG.ROI_GROUP_ID = DRGTR.ROI_GROUP_ID
                       AND DRG.PRODUCT_TYPE IN ('OA')
                       AND DRGP.PROD_ID = PRODID
                       AND DRGC.CATEGORY_ID = CATG
                       AND 1 BETWEEN 1 AND 9999999999
                       AND (   (to_date(CREATDT) >= ROI_DATE AND to_date(CREATDT) <= ROI_END_DATE)
                            OR (ROI_DATE <= to_date(CREATDT) AND ROI_END_DATE IS NULL)
                           )
                       AND NVL(( to_date(RETIREDATE)- to_date(CREATDT)),0)
                              BETWEEN FROM_PERIOD
                                  AND TO_PERIOD AND DRGTR.INT_TYPE = 'C';              
               EXCEPTION WHEN OTHERS THEN
                 DBMS_OUTPUT.PUT_LINE('ERROR');
               END;
if Interest is null then
RETURN 0;
Else
return Interest;
End if; 
--DBMS_OUTPUT.PUT_LINE('PRODID  '||PRODID||'  '||CATG||'   '||CUSTID||'  '||CREATDT||' '||INTEREST );
END;
/

================================================================


--SELECT * FROM TABLE (GET_SBDAILY_INTEREST('0001101000276','01-jan-2016','31-jan-2016'))


CREATE OR REPLACE FUNCTION GET_SBDAILY_INTEREST(ACTNUM VARCHAR, FROMDT DATE,TODAT DATE)  RETURN TAB_TYPE_INTR_PROCESS PIPELINED IS 
 PRAGMA AUTONOMOUS_TRANSACTION;
  TYPE REF0 IS REF CURSOR;
    TRCURSOR REF0;
    OUTREC TYPE_INTR_PROCESS := TYPE_INTR_PROCESS(NULL,0,0);
    CRINTRATE NUMBER(16,2):=0;
    DEBITINTEREST NUMBER(16,2):=0;
    CREDITINTEREST NUMBER(16,2):=0;
    INTEREST VARCHAR2(50);
    INTGET VARCHAR2(10);
    CATG VARCHAR2(30);
    STARTDAT DATE;
    ENDDT DATE;
    LIMT NUMBER(16);
    PRODID NUMBER(10);  
    PENALAMT NUMBER(16,2):=0;
    PENALREQUIRED  VARCHAR(1):='Y';
    CALFORLIMTEXEED  VARCHAR(1):='N';
    INTR VARCHAR2(50);    
    ACTNUMBER VARCHAR2(13);
   BEGIN    
   DELETE FROM ODINTR_CAL WHERE ACT_NUM = ACTNUM AND FRMDATE = FROMDT AND TODATE =TODAT;
             COMMIT;
                   INSERT INTO ODINTR_CAL (ACT_NUM,DT, PRINCIPAL,PRD,RT,INTRAMT,FRMDATE,TODATE )
             SELECT ACT_NUM, TRANS_DT, SUM(PRINCIPAL) OVER (PARTITION BY ACT_NUM   ORDER BY ACT_NUM,TRANS_DT) AS BAL
             ,(TO_DATE( NVL(LEAD(TRANS_DT, 1)  OVER (ORDER BY TRANS_DT) ,TODAT)) -TRANS_DT) AS PRD, CASE WHEN SUM(PRINCIPAL) OVER (PARTITION BY ACT_NUM   ORDER BY ACT_NUM,TRANS_DT) > 0 THEN GET_OA_ROI_CRINT(ACTNUM) ELSE
             GET_OA_ROI_DRINT(ACTNUM) END AS RT,   
             ROUND((SUM(PRINCIPAL) OVER (PARTITION BY ACT_NUM   ORDER BY ACT_NUM,TRANS_DT)) *((TO_DATE( NVL(LEAD(TRANS_DT, 1)  OVER (ORDER BY TRANS_DT) ,TODAT)) -TRANS_DT)) * (CASE WHEN SUM(PRINCIPAL) OVER (PARTITION BY ACT_NUM   ORDER BY ACT_NUM,TRANS_DT) > 0 THEN GET_OA_ROI_CRINT(ACTNUM) ELSE
             GET_OA_ROI_DRINT(ACTNUM) END)/(36500),0)   AS INTRAMT,FROMDT AS FROMDATE,TODAT TODATE
             FROM(
             SELECT ACT_NUM,TO_DATE(FROMDT)/*-1*/ AS TRANS_DT,(AMT) AS PRINCIPAL FROM ACT_DAYEND_BALANCE
             WHERE  ACT_NUM = ACTNUM AND DAY_END_DT =
             (SELECT MAX(DAY_END_DT) FROM  ACT_DAYEND_BALANCE WHERE  ACT_NUM = ACTNUM AND DAY_END_DT  
             <=FROMDT) /* COMENT  =  IF U PASS FROMDATE  01 */
             UNION ALL 
             SELECT ACT_NUM,TRANS_DT,SUM(CREDIT) - SUM(DEBIT) PRINCIPAL 
               FROM PASS_BOOK  WHERE ACT_NUM = ACTNUM
             AND TRANS_DT BETWEEN TO_DATE(FROMDT) +1/*COMENT +1 IF U PASS FROMDATE  01 */ AND TODAT 
             GROUP BY ACT_NUM,TRANS_DT
             ORDER BY TRANS_DT
               )TR   ORDER BY ACT_NUM, TRANS_DT ;
            COMMIT;
   OPEN TRCURSOR FOR
       SELECT   ACT_NUM, ROUND (SUM (CASE
                                        WHEN PRINCIPAL > 0
                                           THEN NVL (INTRAMT, 0)
                                        ELSE 0
                                     END), 0),
                         ABS(ROUND (SUM (CASE
                                        WHEN PRINCIPAL > 0
                                           THEN 0
                                        ELSE NVL (INTRAMT, 0)
                                     END), 0))
                    INTO ACTNUMBER,DEBITINTEREST,
                         CREDITINTEREST
                    FROM ODINTR_CAL
                   WHERE ACT_NUM = ACTNUM AND FRMDATE = FROMDT AND TODATE = TODAT
                GROUP BY ACT_NUM;
            LOOP
            FETCH TRCURSOR INTO
                OUTREC.ACTNUM, OUTREC.CREDITINT, OUTREC.DEBITINT;            
            EXIT WHEN TRCURSOR%NOTFOUND;
            PIPE ROW(OUTREC);        
            END LOOP;
   CLOSE TRCURSOR;
   RETURN;
END;
/

CREATE OR REPLACE function get_retire_date(custid varchar) return date is
      retireage number(3);
      retiredate date;
      begin
      select nvl(P.RETIREMENT_AGE,0) into retireage from parameters p;
      Select add_months(C.DOB,retireage * 12 ) into retiredate from customer c where C.CUST_ID = custid  ;
      return retiredate;
      end;
/

DROP TYPE TYPE_RecoveryDueList;

CREATE OR REPLACE TYPE TYPE_RecoveryDueList AS 
            OBJECT (emp_refno VARCHAR2 (32 Byte),
                    MEM_NAME  VARCHAR2 (128 Byte),
                    PROD_DESC VARCHAR2 (128 Byte),
                    ACT_NUM VARCHAR2 (16 Byte),
                    PROD_TYPE VARCHAR2 (8 Byte),
                    PROD_ID VARCHAR2 (8 Byte),
                    AMOUNT NUMBER (16,2),
                    SALARY_RECOVERY VARCHAR2(1),
                    CUSTOMERGROUP VARCHAR2(1),
                    DE_STATUS VARCHAR2(20),
                    DUE_STATUS VARCHAR2(20))
/

DROP TYPE TAB_TYPE_RecoveryDueList

CREATE OR REPLACE TYPE TAB_TYPE_RecoveryDueList AS TABLE OF TYPE_RecoveryDueList

CREATE OR REPLACE FUNCTION Load_getDueForRecoveryList(IntCalcDate Date) RETURN TAB_TYPE_RecoveryDueList AS RecoveryDueList TAB_TYPE_RecoveryDueList;
BEGIN
SELECT TYPE_RecoveryDueList(emp_refno,MEM_NAME,PROD_DESC,ACT_NUM,PROD_TYPE,PROD_ID,AMOUNT,SALARY_RECOVERY,CUSTOMERGROUP,DE_STATUS,DUE_STATUS)
BULK COLLECT INTO RecoveryDueList FROM(
SELECT   *
    FROM (SELECT DISTINCT emp_refno_new AS emp_refno,
                             c.fname
                          || ' '
                          || c.mname
                          || ' '
                          || c.lname AS "MEM_NAME",
                          ap.prod_desc, c.act_num, apa.prod_type, apa.prod_id,
                          NULL AS amount, salary_recovery,
                          DECODE (cu.customergroup,
                                  'Canteen', '0',
                                  '1'
                                 ) AS customergroup,
                          '' AS de_status, 'NOT_DUE' AS due_status
                     FROM share_acct sa,
                          all_products ap,
                          all_product_accts apa,
                          all_customer c,
                          customer cu
                    WHERE sa.emp_refno_new IS NOT NULL
                      AND sa.cust_id = c.cust_id
                      AND (   c.act_num = apa.act_num
                           OR c.act_num = apa.rep_act_num
                          )
                      AND apa.prod_id = ap.prod_id
                      AND apa.acct_status Not in ('CLOSED')
                      AND cu.cust_id = sa.cust_id
                      AND NOT EXISTS (SELECT sp.prod_id
                                        FROM suspense_product sp
                                       WHERE apa.prod_id = sp.prod_id)
                      AND cu.customer_status = 'PRESENT'
                      AND cu.cust_type_id IN ('MEMBER')
                      AND NOT EXISTS (
                             SELECT DISTINCT dem.emp_ref_no
                                        FROM deduction_exemption_mapping dem
                                       WHERE sa.emp_refno_new = dem.emp_ref_no
                                         AND dem.status not in  ('DELETED'))
                      AND cu.cust_type_id IN ('MEMBER')
                      AND apa.prod_id NOT IN ('310', '302', '308')
          UNION
          SELECT emp_ref_no AS emp_refno,
                 cu.fname || ' ' || cu.mname || ' ' || cu.lname AS "MEM_NAME",
                 prod_desc, c.act_num, sdm.map_prod_type AS prod_type,
                 sdm.map_prod_id AS prod_id, amount, NULL AS salary_recovery,
                 DECODE (cu.customergroup,
                         'Canteen', '0',
                         '1'
                        ) AS customergroup,
                 '' AS de_status, 'NOT_DUE' AS due_status
            FROM salary_deduction_mapping sdm,
                 all_products ap,
                 all_customer c,
                 share_acct sa,
                 customer cu
           WHERE c.act_num = sdm.map_act_num
             AND cu.cust_type_id IN ('MEMBER')
             AND sdm.map_prod_id = ap.prod_id
             AND sa.emp_refno_new = sdm.emp_ref_no
             AND cu.cust_id = sa.cust_id
             AND sdm.map_prod_type != 'GL'
             AND sdm.status != 'DELETED'
             AND c.acct_status Not in ('CLOSED')
             AND cu.customer_status = 'PRESENT'
             AND NOT EXISTS (
                    SELECT DISTINCT dem.emp_ref_no
                               FROM deduction_exemption_mapping dem
                              WHERE sa.emp_refno_new = dem.emp_ref_no
                                AND dem.status not in  ('DELETED'))
          UNION
          SELECT emp_ref_no AS emp_refno,
                 cu.fname || ' ' || mname || ' ' || lname AS "MEM_NAME",
                 ac_hd_desc AS prod_desc, ac_hd_id AS act_num,
                 sdm.map_prod_type AS prod_type, sdm.map_prod_id AS prod_id,
                 amount, NULL AS salary_recovery,
                 DECODE (cu.customergroup,
                         'Canteen', '0',
                         '1'
                        ) AS customergroup,
                 '' AS de_status, 'NOT_DUE' AS due_status
            FROM salary_deduction_mapping sdm,
                 ac_hd c,
                 share_acct sa,
                 customer cu
           WHERE c.ac_hd_id = sdm.map_act_num
             AND sdm.status != 'DELETED'
             AND sdm.map_prod_type = 'GL'
             AND sa.emp_refno_new = sdm.emp_ref_no
             AND cu.cust_id = sa.cust_id
             AND cu.cust_type_id IN ('MEMBER')
             AND cu.customer_status = 'PRESENT'
             AND NOT EXISTS (
                    SELECT DISTINCT dem.emp_ref_no
                               FROM deduction_exemption_mapping dem
                              WHERE sa.emp_refno_new = dem.emp_ref_no
                                AND dem.status not in  ('DELETED'))
          UNION
          SELECT DISTINCT a.emp_refno, a.mem_name, a.prod_desc, a.act_num,
                          a.prod_type, a.prod_id, NULL AS amount,
                          NULL AS salary_recovery, a.customergroup,
                          '' AS de_status, 'NOT_DUE' AS due_status
                     FROM (SELECT DISTINCT (c.act_num), clock_no AS emp_refno,
                                              c.fname
                                           || ' '
                                           || c.mname
                                           || ' '
                                           || c.lname AS "MEM_NAME",
                                           prod_desc, apa.prod_type,
                                           apa.prod_id,
                                           DECODE
                                              (cu.customergroup,
                                               'Canteen', '0',
                                               '1'
                                              ) AS customergroup,
                                           '' AS de_status,
                                           'NOT_DUE' AS due_status
                                      FROM suspense_installment si,
                                           all_products ap,
                                           all_product_accts apa,
                                           all_customer c,
                                           suspense_account_master sam,
                                           customer cu,
                                           share_acct sa
                                     WHERE c.cust_id = sa.cust_id
                                       AND si.acct_num = apa.act_num
                                       AND sam.suspense_acct_num = si.acct_num
                                       AND cu.cust_type_id IN
                                                        ('MEMBER', 'CANTEEN')
                                       AND apa.act_num = c.act_num
                                       AND apa.prod_id = ap.prod_id
                                       AND apa.acct_status Not in ('CLOSED')
                                       AND cu.cust_id = c.cust_id
                                       AND NOT EXISTS (
                                              SELECT DISTINCT dem.emp_ref_no
                                                         FROM deduction_exemption_mapping dem
                                                        WHERE sa.emp_refno_new =
                                                                 dem.emp_ref_no
                                                          AND dem.status Not in ('DELETED'))) a
          UNION
          SELECT sa.emp_refno_new AS emp_refno, suspense_name AS mem_name,
                 suspense_prod_desc AS prod_desc,
                 suspense_acct_num AS act_num, 'SA' AS prod_type,
                 suspense_prod_id AS prod_id,
                 NVL (ABS (clear_balance), 0) AS amount, salary_recovery,
                 DECODE (c.customergroup,
                         'Canteen', '0',
                         '1'
                        ) AS customergroup,
                 '' AS de_status, 'NOT_DUE' AS due_status
            FROM suspense_account_master JOIN customer c
                 ON suspense_account_master.suspense_customer_id = c.cust_id
                 JOIN share_acct sa ON sa.cust_id = c.cust_id
           WHERE clear_balance < 0
             AND c.cust_type_id IN ('MEMBER')
             AND c.customer_status = 'PRESENT'
             AND NOT EXISTS (
                    SELECT DISTINCT dem.emp_ref_no
                               FROM deduction_exemption_mapping dem
                              WHERE sa.emp_refno_new = dem.emp_ref_no
                                AND dem.status not in  ('DELETED'))
          UNION
          SELECT apr.emp_refno_new, apr.fname, ap.prod_desc, apr.act_num,
                 apr.prod_type, apr.prod_id, 0 AS amount, apr.salary_recovery,
                 DECODE (apr.customergroup,
                         'Canteen', '0',
                         '1'
                        ) AS customergroup,
                 'DE' AS de_status, apr.due_status
            FROM all_product_accts_recoveryde apr, all_products ap
           WHERE ap.prod_id = apr.prod_id
             AND apr.emp_refno_new IS NOT NULL
             AND apr.emp_refno_new = getrecoverydueemp (apr.emp_refno_new)) a
ORDER BY customergroup, REGEXP_REPLACE (emp_refno, '[AZaz]', ''));
Return RecoveryDueList;
End;

DROP TYPE TYPE_GETDETAILSFORRECOVERYLIST;

CREATE OR REPLACE TYPE TYPE_getDetailsForRecoveryList AS 
            OBJECT (emp_refno VARCHAR2 (32 Byte),
                    MEM_NAME  VARCHAR2 (128 Byte),
                    PROD_DESC VARCHAR2 (128 Byte),
                    ACT_NUM VARCHAR2 (16 Byte),
                    PROD_TYPE VARCHAR2 (8 Byte),
                    PROD_ID VARCHAR2 (8 Byte),
                    AMOUNT NUMBER (16,2),
                    SALARY_RECOVERY VARCHAR2(1),
                    CUSTOMERGROUP VARCHAR2(1),
                    DE_STATUS VARCHAR2(20),
                    DUE_STATUS VARCHAR2(20))
/


DROP TYPE TAB_TYPE_RECOVERYLIST;

CREATE OR REPLACE TYPE TAB_TYPE_RecoveryList AS TABLE OF TYPE_getDetailsForRecoveryList
/

==============================================================================================================


CREATE OR REPLACE FUNCTION Load_getDetailsForRecoveryList(INTCALCDATE DATE) RETURN TAB_TYPE_RecoveryList AS RecoveryList TAB_TYPE_RecoveryList;
BEGIN
SELECT TYPE_getDetailsForRecoveryList(emp_refno,MEM_NAME,PROD_DESC,ACT_NUM,PROD_TYPE,PROD_ID,AMOUNT,SALARY_RECOVERY,CUSTOMERGROUP,DE_STATUS,DUE_STATUS)
BULK COLLECT INTO RecoveryList FROM
(
SELECT   emp_refno,MEM_NAME,PROD_DESC,ACT_NUM,PROD_TYPE,PROD_ID,AMOUNT,SALARY_RECOVERY,CUSTOMERGROUP,DE_STATUS,DUE_STATUS
    FROM (SELECT DISTINCT emp_refno_new AS emp_refno,
                             c.fname
                          || ' '
                          || c.mname
                          || ' '
                          || c.lname AS "MEM_NAME",
                          ap.prod_desc, c.act_num, apa.prod_type, apa.prod_id,
                          NULL AS amount, salary_recovery,
                          DECODE (cu.customergroup,
                                  'Canteen', '0',
                                  '1'
                                 ) AS customergroup,
                          '' AS de_status, 'NOT_DUE' AS due_status
                     FROM share_acct sa,
                          all_products ap,
                          all_product_accts apa,
                          all_customer c,
                          customer cu
                    WHERE sa.emp_refno_new IS NOT NULL
                      AND sa.cust_id = c.cust_id
                      AND (   c.act_num = apa.act_num
                           OR c.act_num = apa.rep_act_num
                          )
                      AND apa.prod_id = ap.prod_id
                      AND apa.acct_status != 'CLOSED'
                      AND cu.cust_id = sa.cust_id
                      AND NOT EXISTS (SELECT sp.prod_id
                                        FROM suspense_product sp
                                       WHERE apa.prod_id = sp.prod_id)
                      AND cu.customer_status = 'PRESENT'
                      AND cu.cust_type_id IN ('MEMBER')
                      AND NOT EXISTS (
                             SELECT DISTINCT dem.emp_ref_no
                                        FROM deduction_exemption_mapping dem
                                       WHERE sa.emp_refno_new = dem.emp_ref_no
                                         AND dem.status = 'CREATED')
                      AND cu.cust_type_id IN ('MEMBER')
                      AND apa.prod_id NOT IN ('310', '302', '308')
          UNION
          SELECT emp_ref_no AS emp_refno,
                 cu.fname || ' ' || cu.mname || ' ' || cu.lname AS "MEM_NAME",
                 prod_desc, c.act_num, sdm.map_prod_type AS prod_type,
                 sdm.map_prod_id AS prod_id, amount, NULL AS salary_recovery,
                 DECODE (cu.customergroup,
                         'Canteen', '0',
                         '1'
                        ) AS customergroup,
                 '' AS de_status, 'NOT_DUE' AS due_status
            FROM salary_deduction_mapping sdm,
                 all_products ap,
                 all_customer c,
                 share_acct sa,
                 customer cu
           WHERE c.act_num = sdm.map_act_num
             AND cu.cust_type_id IN ('MEMBER')
             AND sdm.map_prod_id = ap.prod_id
             AND sa.emp_refno_new = sdm.emp_ref_no
             AND cu.cust_id = sa.cust_id
             AND sdm.map_prod_type != 'GL'
             AND sdm.status != 'DELETED'
             AND c.acct_status != 'CLOSED'
             AND cu.customer_status = 'PRESENT'
             AND NOT EXISTS (
                    SELECT DISTINCT dem.emp_ref_no
                               FROM deduction_exemption_mapping dem
                              WHERE sa.emp_refno_new = dem.emp_ref_no
                                AND dem.status = 'CREATED')
          UNION
          SELECT emp_ref_no AS emp_refno,
                 cu.fname || ' ' || mname || ' ' || lname AS "MEM_NAME",
                 ac_hd_desc AS prod_desc, ac_hd_id AS act_num,
                 sdm.map_prod_type AS prod_type, sdm.map_prod_id AS prod_id,
                 amount, NULL AS salary_recovery,
                 DECODE (cu.customergroup,
                         'Canteen', '0',
                         '1'
                        ) AS customergroup,
                 '' AS de_status, 'NOT_DUE' AS due_status
            FROM salary_deduction_mapping sdm,
                 ac_hd c,
                 share_acct sa,
                 customer cu
           WHERE c.ac_hd_id = sdm.map_act_num
             AND sdm.status != 'DELETED'
             AND sdm.map_prod_type = 'GL'
             AND sa.emp_refno_new = sdm.emp_ref_no
             AND cu.cust_id = sa.cust_id
             AND cu.cust_type_id IN ('MEMBER')
             AND cu.customer_status = 'PRESENT'
             AND NOT EXISTS (
                    SELECT DISTINCT dem.emp_ref_no
                               FROM deduction_exemption_mapping dem
                              WHERE sa.emp_refno_new = dem.emp_ref_no
                                AND dem.status = 'CREATED')
          UNION
          SELECT DISTINCT a.emp_refno, a.mem_name, a.prod_desc, a.act_num,
                          a.prod_type, a.prod_id, NULL AS amount,
                          NULL AS salary_recovery, a.customergroup,
                          '' AS de_status, 'NOT_DUE' AS due_status
                     FROM (SELECT DISTINCT (c.act_num), clock_no AS emp_refno,
                                              c.fname
                                           || ' '
                                           || c.mname
                                           || ' '
                                           || c.lname AS "MEM_NAME",
                                           prod_desc, apa.prod_type,
                                           apa.prod_id,
                                           DECODE
                                              (cu.customergroup,
                                               'Canteen', '0',
                                               '1'
                                              ) AS customergroup,
                                           '' AS de_status,
                                           'NOT_DUE' AS due_status
                                      FROM suspense_installment si,
                                           all_products ap,
                                           all_product_accts apa,
                                           all_customer c,
                                           suspense_account_master sam,
                                           customer cu,
                                           share_acct sa
                                     WHERE c.cust_id = sa.cust_id
                                       AND si.acct_num = apa.act_num
                                       AND sam.suspense_acct_num = si.acct_num
                                       AND cu.cust_type_id IN
                                                        ('MEMBER', 'CANTEEN')
                                       AND apa.act_num = c.act_num
                                       AND apa.prod_id = ap.prod_id
                                       AND apa.acct_status != 'CLOSED'
                                       AND cu.cust_id = c.cust_id
                                       AND NOT EXISTS (
                                              SELECT DISTINCT dem.emp_ref_no
                                                         FROM deduction_exemption_mapping dem
                                                        WHERE sa.emp_refno_new =
                                                                 dem.emp_ref_no
                                                          AND dem.status =
                                                                     'CREATED')) a
          UNION
          SELECT sa.emp_refno_new AS emp_refno, suspense_name AS mem_name,
                 suspense_prod_desc AS prod_desc,
                 suspense_acct_num AS act_num, 'SA' AS prod_type,
                 suspense_prod_id AS prod_id,
                 NVL (ABS (clear_balance), 0) AS amount, salary_recovery,
                 DECODE (c.customergroup,
                         'Canteen', '0',
                         '1'
                        ) AS customergroup,
                 '' AS de_status, 'NOT_DUE' AS due_status
            FROM suspense_account_master JOIN customer c
                 ON suspense_account_master.suspense_customer_id = c.cust_id
                 JOIN share_acct sa ON sa.cust_id = c.cust_id
           WHERE clear_balance < 0
             AND c.cust_type_id IN ('MEMBER')
             AND c.customer_status = 'PRESENT'
             AND NOT EXISTS (
                    SELECT DISTINCT dem.emp_ref_no
                               FROM deduction_exemption_mapping dem
                              WHERE sa.emp_refno_new = dem.emp_ref_no
                                AND dem.status = 'CREATED')) a
ORDER BY customergroup, REGEXP_REPLACE (emp_refno, '[AZaz]', ''));
RETURN RecoveryList; 
END;

-- Start Version [Script Version - 0.0.2] [ReleaseVersion - 9.2.2.9]--[20-06-2018] -- By Nithya

ALTER TABLE SALARY_RECO_OMIT_PRINC_DETAILS ADD OMIT_PRINCIPLE VARCHAR2(1);

ALTER TABLE SALARY_RECO_OMIT_PRINC_DETAILS ADD OMIT_INTEREST VARCHAR2(1);

CREATE OR REPLACE FORCE VIEW thrift_deposit_accounts (branch_id,
                                                          prod_id,
                                                          deposit_no,
                                                          cust_id,
                                                          constitution,
                                                          CATEGORY,
                                                          renewal_from_deposit
                                                         )
AS
   SELECT a.branch_id, a.prod_id, a.deposit_no, a.cust_id, a.constitution,
          a.CATEGORY, a.renewal_from_deposit
     FROM deposit_acinfo a JOIN thrift_deposit_product rdp
          ON rdp.prod_id = a.prod_id
    WHERE a.authorize_status = 'AUTHORIZED' AND a.status != 'DELETED';


CREATE OR REPLACE FORCE VIEW thrift_deposit_product (prod_id,
                                                         prod_desc,
                                                         behaves_like,
                                                         acct_head
                                                        )
AS
   SELECT dp.prod_id, dp.prod_desc, dp.behaves_like, dp.acct_head
     FROM deposits_product dp
    WHERE dp.behaves_like = 'THRIFT'
      AND dp.authorize_status = 'AUTHORIZED'
      AND dp.status != 'DELETED';

-- Start Version [Script Version - 0.0.2] [ReleaseVersion - 9.2.2.9]--[20-06-2018] -- By Nithya

