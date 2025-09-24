--Start Version [Script Version - 0.0.01] [ReleaseVersion - 9.2.1.25]--[02-05-2014] -- by Sreekrishnan
UPDATE PARAMETERS SET CASHIER_AUTH_ALLOWED = 'Y';
--End Version [Script Version - 0.0.01] [ReleaseVersion - 9.2.1.25]--[02-06-2014] -- by Sreekrishnan

-- Start Version [Script Version - 0.0.02] [ReleaseVersion - 9.2.2.16]--[17-sep-2020] -- By Nithya K

CREATE OR REPLACE FUNCTION GET_AGENT_COMM_SLAB_REQUIRED(PRODID VARCHAR) RETURN VARCHAR IS

    SLABREQUIRED VARCHAR2(10);
    PROD_TYPE VARCHAR(10);

BEGIN
 
  SELECT AP.PROD_TYPE INTO PROD_TYPE FROM ALL_PRODUCTS AP WHERE AP.PROD_ID = PRODID;

    IF PROD_TYPE='OA' THEN

        SELECT DP.AGENT_COMM_SLAB_REQUIRED INTO SLABREQUIRED FROM OP_AC_PRODUCT DP WHERE DP.PROD_ID = PRODID;

    ELSIF PROD_TYPE='SA' THEN

        SELECT DP.AGENT_COMM_SLAB_REQUIRED INTO SLABREQUIRED FROM SUSPENSE_PRODUCT DP WHERE DP.PROD_ID = PRODID;

    ELSIF PROD_TYPE='TD' THEN

        SELECT DP.AGENT_COMM_SLAB_REQUIRED INTO SLABREQUIRED FROM DEPOSITS_PROD_SCHEME DP WHERE DP.PROD_ID = PRODID;

    END IF;

RETURN SLABREQUIRED;

END;
/

/* --------------------------------------------------------------------------------- */

CREATE OR REPLACE FUNCTION GET_AGENT_COMMISSION_SLAB (PRODID        VARCHAR2,
                                                      SLABAMOUNT    NUMBER)
   RETURN NUMBER
IS
   PERCENTAGE   NUMBER (16, 2);
BEGIN
   SELECT ACS.COMM_PERCENTAGE INTO PERCENTAGE
     FROM AGENT_COMMISSION_CALC_SLAB ACS  WHERE SLABAMOUNT BETWEEN ACS.FROM_AMT AND ACS.TO_AMT AND ACS.PROD_ID = PRODID AND ACS.STATUS != 'DELETED';
 RETURN PERCENTAGE;
END;
/

/* --------------------------------------------------------------------------------- */

CREATE OR REPLACE FUNCTION GET_DAILY_COL_SLAB_PERCENT (PRODID        VARCHAR2,
                                                      SLABAMOUNT    NUMBER, DEFAULTPERCENT NUMBER)
   RETURN NUMBER
IS
   PERCENTAGE   NUMBER (16, 2);
   SLABREQUIRED VARCHAR2(1);
   
BEGIN
  SELECT NVL(GET_AGENT_COMM_SLAB_REQUIRED(PRODID),'N') INTO SLABREQUIRED FROM DUAL;
  IF SLABREQUIRED = 'Y' THEN
    SELECT NVL(GET_AGENT_COMMISSION_SLAB(PRODID,SLABAMOUNT),0) INTO PERCENTAGE FROM DUAL;
  ELSE
  PERCENTAGE := DEFAULTPERCENT;
  END IF;
 RETURN PERCENTAGE;
END;
/

/* --------------------------------------------------------------------------------- */

DROP VIEW DAILY_COL_DATA;

/* Formatted on 26-06-2020 11:01:36 (QP5 v5.227.12220.39754) */
CREATE OR REPLACE FORCE VIEW PERI.DAILY_COL_DATA
(
   AGENT_ID,
   BILLNO,
   AMT,
   COMM_AMT,
   ACNO,
   STATUS,
   AC_CODE,
   C_DATE,
   C_TIME,
   NARR,
   CBMS_ACTNUM,
   PROD_ID,
   INITIATED_BRANCH,
   BRANCH_ID,
   PROD_TYPE,
   COL_AC_HD_ID,
   COL_PROD_TYPE,
   COL_PROD_ID
)
AS
   SELECT pm.agent_id AS agent_id,
          TO_NUMBER (col8) AS billno,
          ROUND (
             (  CAST (NVL (col4, 0) AS NUMBER (19, 2))
              -   (  CAST (col4 AS NUMBER (19, 2))
                   * GET_DAILY_COL_SLAB_PERCENT (
                        AM.PROD_ID,
                        (CAST (col4 AS NUMBER (19, 2))),
                        am.comm_per_ac_holdr))
                / 100),
             0)
             AS amt,
          ROUND (
               (  CAST (col4 AS NUMBER (19, 2))
                * GET_DAILY_COL_SLAB_PERCENT (
                     AM.PROD_ID,
                     (CAST (col4 AS NUMBER (19, 2))),
                     am.comm_per_ac_holdr))
             / 100,
             0)
             AS comm_amt,
          col3 AS acno,
          col9 AS status,                -- (C - cancelled, Y - Not cancelled)
          col1 AS ac_code,
          TO_DATE (col6, 'DD-MM-YYYY') AS c_date,
          col7 AS c_time,
          col11 AS narr,
             mst.act_num
          || (CASE WHEN am.prod_type = 'TD' THEN '_1' ELSE '' END)
             AS cbms_actnum,
          am.prod_id AS prod_id,
          pm.initiated_branch,
          SUBSTR (col3, 0, 4) AS branch_id,
          am.prod_type AS prod_type,
          CASE
             WHEN amr.dp_act_num IS NULL THEN am.col_ac_hd_id
             ELSE amr.dp_act_num
          END
             AS col_ac_hd_id,
          CASE
             WHEN amr.dp_act_num IS NULL THEN 'GL'
             ELSE amr.dp_prod_type
          END
             AS col_prod_type,
          CASE WHEN amr.dp_act_num IS NULL THEN NULL ELSE amr.dp_prod_id END
             AS col_prod_id
     FROM collection_det pm
          JOIN agent_master amr ON amr.agent_id = pm.agent_id
          JOIN agent_prod_mapping am ON am.prod_exp_code = pm.col1
          JOIN agentmastdet mst
             ON NVL (SUBSTR (mst.sa_acct_num, 1, 13),
                     SUBSTR (mst.act_num, 1, 13)) = pm.col3
    --                   pm.initiated_branch || am.prod_id || SUBSTR (pm.col3, 8, 6)
    WHERE     col9 = 'Y'
          AND mst.prod_id = am.prod_id
          AND AMR.BRANCH_ID = SUBSTR (PM.COL3, 1, 4)
          --      AND amr.branch_id = pm.initiated_branch
          AND AMR.BRANCH_ID = MST.BRANCH_ID
          --AND AMR.AGENT_ID = AM.AGENT_ID----(commented on 30/01/2019)
          AND AM.PROD_ID = MST.PROD_ID;

-- End Version [Script Version - 0.0.02] [ReleaseVersion - 9.2.2.16]--[17-sep-2020] -- By Nithya K