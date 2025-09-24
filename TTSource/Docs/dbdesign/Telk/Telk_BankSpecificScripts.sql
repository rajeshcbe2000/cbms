--Start Version [Script Version - 0.0.16] [ReleaseVersion - 9.3.1.25]--[08-04-2014] -- by Sreekrishnan--For TELK Only
SET DEFINE OFF;
Insert into SCREEN_MASTER
   (SCREEN_ID, MENU_ID, SCREEN_NAME, APP_ID, WF_STATUS, 
    MODULE_ID, SCREEN_CLASS, SL_NO, STATUS, SCREEN_TYPE, 
    RECORD_KEY_COL, SCREEN_DESC)
 Values
   ('SCR09998', '76', 'Cashier Authorization List', 'APP01', 'DONE', 
    '32', NULL, 5, 'CREATED', NULL, 
    NULL, NULL);
COMMIT;

SET DEFINE OFF;
Insert into SCREEN_MASTER
   (SCREEN_ID, MENU_ID, SCREEN_NAME, APP_ID, WF_STATUS, 
    MODULE_ID, SCREEN_CLASS, SL_NO, STATUS, SCREEN_TYPE, 
    RECORD_KEY_COL, SCREEN_DESC)
 Values
   ('SCR09997', '76', 'Manager Authorization List', 'APP01', 'DONE', 
    '32', NULL, 6, 'CREATED', NULL, 
    NULL, NULL);
COMMIT;


SET DEFINE OFF;
Insert into GROUP_SCREENS
   (GROUP_ID, SCREEN_ID, STATUS, NEW_ALLOWED, EDIT_ALLOWED, 
    DELETE_ALLOWED, AUTH_REJ_ALLOWED, EXCEPTION_ALLOWED, PRINT_ALLOWED, INTERBRANCH_ALLOWED)
 Values
   ('GRP01014', 'SCR09998', 'CREATED', 'Y', 'Y', 
    'Y', 'Y', 'Y', 'Y', NULL);
Insert into GROUP_SCREENS
   (GROUP_ID, SCREEN_ID, STATUS, NEW_ALLOWED, EDIT_ALLOWED, 
    DELETE_ALLOWED, AUTH_REJ_ALLOWED, EXCEPTION_ALLOWED, PRINT_ALLOWED, INTERBRANCH_ALLOWED)
 Values
   ('GRP00002', 'SCR09998', 'CREATED', NULL, NULL, 
    NULL, NULL, NULL, NULL, NULL);
Insert into GROUP_SCREENS
   (GROUP_ID, SCREEN_ID, STATUS, NEW_ALLOWED, EDIT_ALLOWED, 
    DELETE_ALLOWED, AUTH_REJ_ALLOWED, EXCEPTION_ALLOWED, PRINT_ALLOWED, INTERBRANCH_ALLOWED)
 Values
   ('GRP00001', 'SCR09998', 'CREATED', 'Y', 'Y', 
    'Y', 'Y', NULL, 'Y', 'Y');
Insert into GROUP_SCREENS
   (GROUP_ID, SCREEN_ID, STATUS, NEW_ALLOWED, EDIT_ALLOWED, 
    DELETE_ALLOWED, AUTH_REJ_ALLOWED, EXCEPTION_ALLOWED, PRINT_ALLOWED, INTERBRANCH_ALLOWED)
 Values
   ('GRP00003', 'SCR09998', 'CREATED', NULL, NULL, 
    NULL, NULL, NULL, NULL, NULL);
Insert into GROUP_SCREENS
   (GROUP_ID, SCREEN_ID, STATUS, NEW_ALLOWED, EDIT_ALLOWED, 
    DELETE_ALLOWED, AUTH_REJ_ALLOWED, EXCEPTION_ALLOWED, PRINT_ALLOWED, INTERBRANCH_ALLOWED)
 Values
   ('GRP00003', 'SCR09997', 'CREATED', NULL, NULL, 
    NULL, NULL, NULL, NULL, NULL);
Insert into GROUP_SCREENS
   (GROUP_ID, SCREEN_ID, STATUS, NEW_ALLOWED, EDIT_ALLOWED, 
    DELETE_ALLOWED, AUTH_REJ_ALLOWED, EXCEPTION_ALLOWED, PRINT_ALLOWED, INTERBRANCH_ALLOWED)
 Values
   ('GRP00001', 'SCR09997', 'CREATED', 'Y', 'Y', 
    'Y', 'Y', NULL, 'Y', 'Y');
Insert into GROUP_SCREENS
   (GROUP_ID, SCREEN_ID, STATUS, NEW_ALLOWED, EDIT_ALLOWED, 
    DELETE_ALLOWED, AUTH_REJ_ALLOWED, EXCEPTION_ALLOWED, PRINT_ALLOWED, INTERBRANCH_ALLOWED)
 Values
   ('GRP00002', 'SCR09997', 'CREATED', NULL, NULL, 
    NULL, NULL, NULL, NULL, NULL);
Insert into GROUP_SCREENS
   (GROUP_ID, SCREEN_ID, STATUS, NEW_ALLOWED, EDIT_ALLOWED, 
    DELETE_ALLOWED, AUTH_REJ_ALLOWED, EXCEPTION_ALLOWED, PRINT_ALLOWED, INTERBRANCH_ALLOWED)
 Values
   ('GRP01014', 'SCR09997', 'CREATED', 'Y', 'Y', 
    'Y', 'Y', 'Y', 'Y', NULL);
COMMIT;

--End Version [Script Version - 0.0.16] [ReleaseVersion - 9.3.1.25]--[08-04-2014] -- by Sreekrishnan--For TELK Only

--Start Version [Script Version - 0.0.17] [ReleaseVersion - 9.2.2.0]--[31-03-2015] -- by Anju Anand--For TELK Only

CREATE OR REPLACE FORCE VIEW all_product_accts_recoveryde (act_num,
                                                                cust_id,
                                                                prod_type,
                                                                prod_id,
                                                                rep_act_num,
                                                                branch_id,
                                                                salary_recovery,
                                                                lock_status,
                                                                acct_status,
                                                                authorization_status,
                                                                emp_refno_new,
                                                                due_status,
                                                                customergroup,
                                                                fname
                                                               )
AS
   (SELECT da.deposit_no || '_1' AS act_num, da.cust_id, 'TD' AS prod_type,
           prod_id, da.deposit_no || '_1' AS rep_act_num, branch_id,
           NVL (ds.salary_recovery, 'N') AS salary_recovery,
           NVL (ds.lock_status, 'N') AS lock_status, ds.acct_status,
           da.authorize_status, dem.emp_ref_no,
           DECODE (NVL (get_rd_install (da.deposit_no,
                                        de.curr_appl_dt,
                                        da.branch_id
                                       ),
                        0
                       ),
                   0, 'NOT_DUE',
                   'DUE'
                  ) AS due_status,
           c.customergroup, c.fname
      FROM deposit_acinfo da,
           deposit_sub_acinfo ds,
           day_end de,
           share_acct sa,
           deduction_exemption_mapping dem,
           customer c
     WHERE da.authorize_status = 'AUTHORIZED'
       AND da.status != 'DELETED'
       AND ds.acct_status != 'CLOSED'
       AND ds.deposit_no = da.deposit_no
       AND de.branch_code = da.branch_id
       AND da.cust_id = sa.cust_id(+)
       AND da.cust_id = c.cust_id
       AND sa.emp_refno_new = dem.emp_ref_no(+)
       AND EXISTS (
                   SELECT cust_id
                     FROM customer cm
                    WHERE cm.cust_id = c.cust_id
                          AND cm.cust_type_id = 'MEMBER')
    UNION ALL
    SELECT lfd.acct_num AS act_num, lb.cust_id, 'TL' AS prod_type,
           lfd.prod_id, lfd.acct_num || '_1' AS rep_act_num, branch_id,
           NVL (salary_recovery, 'N') AS salary_recovery,
           NVL (lock_status, 'N') AS lock_status, lfd.acct_status,
           lfd.authorize_status_1, dem.emp_ref_no,
           DECODE
              (CASE
                  WHEN lrs.emi_in_simpleintrest = 'Y'
                  AND lrs.install_type = 'UNIFORM_PRINCIPLE_EMI'
                     THEN get_tl_inst_od_emi (lfd.acct_num, de.curr_appl_dt)
                  WHEN lrs.install_type = 'UNIFORM_PRINCIPLE_EMI'
                  AND lrs.emi_in_simpleintrest IS NULL
                     THEN get_tl_inst_od (lfd.acct_num, de.curr_appl_dt)
               END,
               0, 'NOT_DUE',
               'DUE'
              ) AS due_status,
           c.customergroup, c.fname
      FROM loans_facility_details lfd,
           loans_borrower lb,
           day_end de,
           share_acct sa,
           loans_repay_schedule lrs,
           deduction_exemption_mapping dem,
           customer c
     WHERE lfd.authorize_status_1 = 'AUTHORIZED'
       AND lfd.acct_status != 'CLOSED'
       AND lfd.borrow_no = lb.borrow_no
       AND lfd.status != 'DELETED'
       AND NOT EXISTS (SELECT prod_id
                         FROM LOANS_PROD_ACPARAM lp
                        WHERE LP.SALARY_RECOVERY = 'N')
       AND NOT EXISTS (
                   SELECT prod_id
                     FROM loans_product
                    WHERE behaves_like IN ('OD', 'CC')
                      AND prod_id = lfd.prod_id)
       AND de.branch_code = lfd.branch_id
       AND lrs.acct_num = lfd.acct_num
       AND lb.cust_id = sa.cust_id(+)
       AND lb.cust_id = c.cust_id
       AND sa.emp_refno_new = dem.emp_ref_no(+)
       AND EXISTS (
                   SELECT cust_id
                     FROM customer cm
                    WHERE cm.cust_id = c.cust_id
                          AND cm.cust_type_id = 'MEMBER')          
    UNION ALL
    SELECT lfd.acct_num AS act_num, lb.cust_id, 'AD' AS prod_type, prod_id,
           lfd.acct_num || '_1' AS rep_act_num, branch_id,
           NVL (salary_recovery, 'N') AS salary_recovery,
           NVL (lock_status, 'N') AS lock_status, lfd.acct_status,
           lfd.authorize_status_1, dem.emp_ref_no,
           CASE
              WHEN (SELECT lsd.to_dt
                      FROM loans_sanction_details lsd
                     WHERE lsd.borrow_no = lfd.borrow_no) >
                                                   de.curr_appl_dt
                 THEN 'NOT_DUE'
              ELSE 'DUE'
           END due_status,
           c.customergroup, c.fname
      FROM loans_facility_details lfd,
           loans_borrower lb,
           day_end de,
           share_acct sa,
           deduction_exemption_mapping dem,
           customer c
     WHERE lfd.authorize_status_1 = 'AUTHORIZED'
       AND lfd.borrow_no = lb.borrow_no
       AND lfd.acct_status != 'CLOSED'
       AND lfd.status != 'DELETED'
       AND NOT EXISTS (SELECT prod_id
                         FROM LOANS_PROD_ACPARAM lp
                        WHERE LP.SALARY_RECOVERY = 'N')
       AND EXISTS (
                   SELECT prod_id
                     FROM loans_product
                    WHERE behaves_like IN ('OD', 'CC')
                      AND prod_id = lfd.prod_id)
       AND de.branch_code = lfd.branch_id
       AND lb.cust_id = sa.cust_id(+)
       AND lb.cust_id = c.cust_id
       AND sa.emp_refno_new = dem.emp_ref_no(+)
       AND EXISTS (
                   SELECT cust_id
                     FROM customer cm
                    WHERE cm.cust_id = c.cust_id
                          AND cm.cust_type_id = 'MEMBER')               
    UNION ALL
    SELECT lfd.acct_num AS act_num, lb.cust_id, 'ATL' AS prod_type, prod_id,
           lfd.acct_num || '_1' AS rep_act_num, branch_id,
           'N' AS salary_recovery, 'N' AS lock_status, lfd.acct_status,
           lfd.authorize_status_1, NULL AS emp_refno_new, NULL AS due_status,
           NULL AS customergroup, NULL AS fname
      FROM agri_loans_facility_details lfd, loans_borrower lb
     WHERE lfd.authorize_status_1 = 'AUTHORIZED'
       AND lfd.borrow_no = lb.borrow_no
       AND lfd.status != 'DELETED'
       AND NOT EXISTS (
                 SELECT prod_id
                   FROM agriloans_product
                  WHERE behaves_like IN ('AOD', 'ACC')
                    AND prod_id = lfd.prod_id)
    UNION ALL
    SELECT lfd.acct_num AS act_num, lb.cust_id, 'AAD' AS prod_type, prod_id,
           lfd.acct_num || '_1' AS rep_act_num, branch_id,
           'N' AS salary_recovery, 'N' AS lock_status, lfd.acct_status,
           lfd.authorize_status_1, NULL AS emp_refno_new, NULL AS due_status,
           NULL AS customergroup, NULL AS fname
      FROM agri_loans_facility_details lfd, loans_borrower lb
     WHERE lfd.authorize_status_1 = 'AUTHORIZED'
       AND lfd.borrow_no = lb.borrow_no
       AND lfd.status != 'DELETED'
       AND EXISTS (
                 SELECT prod_id
                   FROM agriloans_product
                  WHERE behaves_like IN ('AOD', 'ACC')
                    AND prod_id = lfd.prod_id)
    UNION ALL
    SELECT am.suspense_acct_num AS act_num, am.suspense_customer_id,
           'SA' AS prod_type, am.suspense_prod_id AS prod_id,
           am.suspense_prefix || am.suspense_ref_no AS rep_act_num,
           am.branch_code AS branch_id, 'N' AS salary_recovery,
           'N' AS lock_status, 'NEW' AS acct_status, am.authorize_status,
           NULL AS emp_refno_new, NULL AS due_status, c.customergroup,
           c.fname
      FROM suspense_account_master am, customer c
     WHERE c.cust_id = am.suspense_customer_id
       AND am.authorize_status = 'AUTHORIZED'
       AND am.status != 'DELETED'
       AND EXISTS (
              SELECT cust_id
                FROM customer cm
               WHERE cm.cust_id = c.cust_id
                 AND cm.cust_type_id IN ('MEMBER', 'CANTEEN'))
    UNION ALL
    SELECT am.chittal_no AS act_num, sa.cust_id, 'MDS' AS prod_type,
           am.scheme_name AS prod_id,
           am.chittal_no || '_' || am.sub_no AS rep_act_num,
           am.branch_code AS branch_id,
           NVL (mm.salary_recovery, 'N') AS salary_recovery,
           NVL (mm.lock_status, 'N') AS lock_status,
           msd.status AS acct_status, am.authorize_status, dem.emp_ref_no,
           DECODE (get_mds_instdue (mm.scheme_name,
                                    am.chittal_no,
                                    am.division_no,
                                    de.curr_appl_dt
                                   ),
                   0, 'NOT_DUE',
                   'DUE'
                  ) AS due_status,
           c.customergroup, c.fname
      FROM mds_application am,
           mds_master_maintenance mm,
           mds_scheme_details msd,
           share_acct sa,
           day_end de,
           deduction_exemption_mapping dem,
           customer c
     WHERE am.authorize_status = 'AUTHORIZED'
       AND am.member_no = sa.share_acct_no(+)
       AND am.status != 'DELETED'
       AND mm.scheme_name = msd.scheme_name
       AND am.chittal_no = mm.chittal_no
       AND am.sub_no = mm.sub_no
       AND am.member_no = sa.share_acct_no
       AND sa.cust_id = c.cust_id
       AND de.branch_code = am.branch_code
       AND sa.emp_refno_new = dem.emp_ref_no(+)
       AND sa.status != 'CLOSED'
       AND msd.status != 'CLOSED'
       AND EXISTS (
                   SELECT cust_id
                     FROM customer cm
                    WHERE cm.cust_id = c.cust_id
                          AND cm.cust_type_id = 'MEMBER'));

--End Version [Script Version - 0.0.17] [ReleaseVersion - 9.2.2.0]--[31-03-2015] -- by Anju Anand--For TELK Only