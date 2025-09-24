/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MaturedDepositTask.java
 *
 * Created on May 04, 2005, 04:52 PM
 * Author : Sunil
 * Desc : The class is used to transfer Matured Deposits on run date
 *        Is always run as a Batch, in DAY BEGIN process
 *        header should contain User id and Branch Id
 *
 *Map Used : DepositMap
 *
 *Code Status : As on 05-May-2005, 
 Separate handling of Interest Amt needs to be done.
 *              Currently only the Deposit Amt is transferred.
 *
 */
package com.see.truetransact.serverside.batchprocess.task.deposit;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.common.transaction.TransferTrans;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;

/**
 *
 * @author Sunil
 */
public class MaturedDepositTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch;
    private TaskStatus status = null;
    private HashMap updateMap = new HashMap();
    private String dayEndType;
    private List branchList;
    private HashMap taskMap;
    private Date currDt;

    /**
     * Creates a new instance of MaturedDepositTask
     */
    public MaturedDepositTask(TaskHeader header) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        currDt = ServerUtil.getCurrentDate(branch);
        initializeTaskObj(header.getTaskParam());
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        taskMap = dataMap;
        if (taskMap != null && taskMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(taskMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            tempMap.put("NEXT_DATE", currDt);
            branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEnd", tempMap);
            tempMap = null;
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
    }

    public TaskStatus executeTask() throws Exception {
        status = new TaskStatus();
        try {
            sqlMap.startTransaction();
            getProductList();
            sqlMap.commitTransaction();
            status.setStatus(BatchConstants.COMPLETED);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            status.setStatus(BatchConstants.ERROR);
            e.printStackTrace();
        }
        return status;

    }

    private void getProductList() throws Exception {
        ArrayList productList = (ArrayList) sqlMap.executeQueryForList("getMaturedDepositProducts", null);
        HashMap products;
        for (int i = 0, j = productList.size(); i < j; i++) {
            products = (HashMap) productList.get(i);
            if (branchList != null && branchList.size() > 0) {
                for (int b = 0; b < branchList.size(); b++) {
                    HashMap branchMap = (HashMap) branchList.get(b);
                    branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                    products.put("TODAY_DT", currDt);
                    updateMap.put("BRANCH_ID", branch);
                    updateMap.put("USER_ID", getHeader().getUserID());
                    updateMap.put("TODAY_DT", branch);
                    getAccountInfo(products);
                }
            }
            products = null;
        }
    }

    private void getAccountInfo(HashMap products) throws Exception {
        HashMap accounts;
        System.out.println("Starting######getAccountInfoaccounts : " + products);
        ArrayList accountList = (ArrayList) sqlMap.executeQueryForList("getMaturedDepositAccounts", products);
        for (int i = 0, j = accountList.size(); i < j; i++) {
            accounts = (HashMap) accountList.get(i);
            System.out.println("Before######getAccountInfoupdateMaturedDepositsaccounts : " + accounts);
            System.out.println("Before###getAccountInfoupdateMaturedDepositsproducts : " + products);
            if (CommonUtil.convertObjToDouble(accounts.get("DEPOSIT_AMT")).doubleValue() > 0) {
                products.put("BRANCH_ID", accounts.get("BRANCH_ID"));
                updateMaturedDeposits(accounts, products);
            }
            accounts = null;
        }
    }

    private void updateMaturedDeposits(HashMap accounts, HashMap products) throws Exception {
        double depositAmt = CommonUtil.convertObjToDouble(accounts.get("DEPOSIT_AMT")).doubleValue();
        HashMap resultMap = getDebitMap(accounts, products);
        System.out.println("resultMapupdateMaturedDeposits" + resultMap);
        ArrayList batchList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        transferTrans.setInitiatedBranch(CommonUtil.convertObjToStr((resultMap.get("CR_BRAN"))));
        resultMap.put("BRANCH_ID", branch);
        System.out.println("updateMaturedDepositstransferTrans" + transferTrans);
        batchList.add(transferTrans.getDebitTransferTO(resultMap, depositAmt));
        batchList.add(transferTrans.getCreditTransferTO(resultMap, depositAmt));
        System.out.println("updateMaturedDeposits batchList : " + batchList);
        transferTrans.doDebitCredit(batchList, branch);
        updateMap.put("PROD_ID", products.get("PROD_ID"));
        sqlMap.executeUpdate("updateMaturedDepositAccounts", updateMap);
        sqlMap.executeUpdate("updateMaturedDepositSubAccounts", updateMap);
    }

    private HashMap getDebitMap(HashMap accounts, HashMap products) {
        HashMap resultMap = new HashMap();
        resultMap.put(TransferTrans.DR_AC_HD, products.get("ACCT_HEAD"));
        resultMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
        resultMap.put(TransferTrans.DR_PROD_TYPE, "GL");
        resultMap.put(TransferTrans.DR_BRANCH, accounts.get("BRANCH_ID"));
        resultMap.put(TransferTrans.CR_AC_HD, products.get("MATURITY_DEPOSIT"));
        resultMap.put(TransferTrans.CR_BRANCH, accounts.get("BRANCH_ID"));
        resultMap.put(TransferTrans.CR_PROD_TYPE, "GL");
        return resultMap;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("MaturedDepositTask");
            header.setBranchID("Bran");
            header.setUserID("sysadmin");
            MaturedDepositTask tsk = new MaturedDepositTask(header);
            TaskStatus status = tsk.executeTask();
            System.out.println("status : " + status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
