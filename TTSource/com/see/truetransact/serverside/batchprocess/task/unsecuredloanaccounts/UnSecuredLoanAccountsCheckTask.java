/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * UnSecuredLoanAccountsCheckTask.java
 *
 * Created on February 28, 2005, 12:23 PM
 */
package com.see.truetransact.serverside.batchprocess.task.unsecuredloanaccounts;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author 152691 This class checks if cash transaction from Vault and teller
 * tally Is to be called as a part of the Day End batch process
 */
public class UnSecuredLoanAccountsCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    private String process = null;
    HashMap vaultMap = new HashMap();

    public UnSecuredLoanAccountsCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        process = header.getProcessType();
        branch = header.getBranchID();
        initializeTaskObj(header.getTaskParam());
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();

    }

    public TaskStatus executeTask() throws Exception {
        /**
         * Formaula to be employed Vault sum(payment) + Cash sum(receipts) -
         * Cash sum(payments) = Vault sum(receipt)
         *
         */
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        System.out.println(status.getStatus());
        HashMap dataMap = new HashMap();
        HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_CODE", branch);
        System.out.println("paramMap@@" + paramMap);
        ArrayList finalList = new ArrayList();
        HashMap tempMap = null;
        HashMap hashData = null;
        List outputList = null;
        List lst = null;
        List lstData = null;
        HashMap data = null;
        List crDrList = null;
        double amt = 0;
        double glcurbal = 0;
        double openingCount = 0;
        System.out.println("paramMap@@" + paramMap);
        lst = sqlMap.executeQueryForList("getUnSecuredLoanAccounts", paramMap);
        //        System.out.println("@@@@@lst"+lst);
        if (lst != null) {
            if (lst.size() > 0) {
                for (int a = 0, b = lst.size(); a < b; a++) {
                    data = (HashMap) lst.get(a);
                    vaultMap.put("ACT_NUM", data.get("ACCT_NUM"));
                    vaultMap.put("PROD_ID", data.get("PROD_ID"));
                    vaultMap.put("NAME", data.get("NAME"));
                    vaultMap.put("CUST_ID", data.get("CUST_ID"));
                    vaultMap.put("AMOUNT", data.get("LIMIT"));
                    vaultMap.put("MEMBER_NO", data.get("MEMBER_NO"));
                }
                if (vaultMap.size() > 0) {
                    status.setStatus(BatchConstants.ERROR);
                    System.out.println("Completion Status : " + status.getStatus());
                }

            } else {
                status.setStatus(BatchConstants.COMPLETED);
            }
        }
        System.out.println("Completion Status : " + status.getStatus());
        return status;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("UnSecuredLoanAccountsCheckTask");
            HashMap paramMap = new HashMap();
            header.setProcessType(CommonConstants.DAY_END);
            header.setBranchID(CommonConstants.BRANCH_ID);
            header.setTaskParam(paramMap);
            UnSecuredLoanAccountsCheckTask tsk = new UnSecuredLoanAccountsCheckTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
