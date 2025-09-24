/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ReverseFlexiTask.java
 *
 * Created on March 8, 2005, 4:38 PM
 */
package com.see.truetransact.serverside.batchprocess.task.deposit.flexi;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;

import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.deposit.DepSubNoAccInfoTO;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.deposit.closing.DepositClosingDAO;
import com.see.truetransact.commonutil.TTException;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 *
 * @author Sathiya
 */
public class ReverseFlexiTask extends Task {

    private static SqlMap sqlMap = null;
    private String BRANCH_ID = "BRAN";
//    private String ACT_NUM = "";
    private String USER_ID = "";
    private String SCREEN = "TD";
    InterestBatchTO interestBatchTO = null;
    private String dayEndType;
    private HashMap paramMap;
    private Date currDt;
    private List branchList;
    private String branch = null;
    private TransactionDAO transactionDAO = null;
    private String taskLable;

    /**
     * Creates a new instance of ReverseFlexiTask
     */
    public ReverseFlexiTask(TaskHeader header) throws Exception {
        setHeader(header);
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        BRANCH_ID = CommonUtil.convertObjToStr(header.getBranchID());
        USER_ID = CommonUtil.convertObjToStr(header.getUserID());
        currDt = ServerUtil.getCurrentDate(BRANCH_ID);
        paramMap = header.getTaskParam();
        if (paramMap != null && paramMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(paramMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            if (paramMap.containsKey("BRANCH_LST")) {
                branchList = (List) paramMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
                tempMap.put("NEXT_DATE", currDt);
                branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEnd", tempMap);
            }
            tempMap = null;
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
        if (paramMap != null && paramMap.containsKey("REV_FLEXI_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("REV_FLEXI_TASK_LABLE"));
        }
    }

    //__ To be Called for the Batch Process...
//    public TaskStatus executeTask() throws Exception {
//        TaskStatus status = new TaskStatus();
//        status.setStatus(BatchConstants.STARTED);
    //__ Common Method Call..
//        implementTask();
//        if(status.getStatus() != BatchConstants.ERROR)
//            status.setStatus(BatchConstants.COMPLETED);                    
//        return status;
//    }
    //__
//    public void reverseFlexi(String actNum) throws Exception {
//        ACT_NUM = actNum;
    //__ Common Method Call..
//        implementTask();
//    }
    //__ Common method to Transfer the amount from Flexi_deposit to Available Balance...
    public TaskStatus executeTask() throws Exception {
        HashMap dataMap = new HashMap();
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                currDt = ServerUtil.getCurrentDate(branch);
                HashMap compMap = new HashMap();
                compMap.put("TASK_NAME", taskLable);
                compMap.put("DAYEND_DT", currDt);
                compMap.put("BRANCH_ID", branch);
                List compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                compMap = null;
                String compStatus = "";
                if (compLst != null && compLst.size() > 0) {
                    compMap = (HashMap) compLst.get(0);
                    compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                    compMap = null;
                }
                if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
//        currDt = ServerUtil.getCurrentDate(BRANCH_ID);
                    dataMap = new HashMap();
                    dataMap.put("MATURITY_DT", currDt);
                    dataMap.put("UNLIEN_REMARKS", "FLEXI_DEPOSITS");
                    //__ if the Act_num is present...
                    //        dataMap.put("ACT_NUM", ACT_NUM);
                    sqlMap.startTransaction();
                    HashMap errorMap = new HashMap();
                    errorMap.put("ERROR_DATE", currDt);
                    errorMap.put("TASK_NAME", taskLable);
                    errorMap.put("BRANCH_ID", branch);
                    sqlMap.executeUpdate("deleteError_showing", errorMap);
                    sqlMap.commitTransaction();

                    ArrayList acctList = getDepositLienAccounts();
//        System.out.println("acctList Premature : " + acctList.size());
                    for (int i = 0; i < acctList.size(); i++) {//__ If the List Contains some Data...
                        dataMap = (HashMap) acctList.get(i);
                        BRANCH_ID = CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID));
                        try {
                            sqlMap.startTransaction();
//                status.setStatus(BatchConstants.STARTED);                
                            double availBalance = CommonUtil.convertObjToDouble(dataMap.get("AVAILABLE_BALANCE")).doubleValue();
                            System.out.println("dataMap Premature : " + dataMap);//__ To Know the Mode...
                            HashMap periodMap = new HashMap();
                            HashMap inputMap = new HashMap();
                            HashMap cashMap = new HashMap();
                            HashMap txMap = new HashMap();
                            ArrayList transferList = new ArrayList();
                            TransferTrans transferTrans = new TransferTrans();
                            TxTransferTO transferTo = new TxTransferTO();
                            ArrayList transferEachList = new ArrayList();
                            HashMap depositHeadMap = new HashMap();
                            HashMap operativeHeadMap = new HashMap();
                            HashMap operativeAcHeads = new HashMap();
                            HashMap depositMap = new HashMap();
                            HashMap oldMap = new HashMap();
                            HashMap tdsCalcMap = new HashMap();
                            DepSubNoAccInfoTO depSubNoAccInfoTO = new DepSubNoAccInfoTO();
                            double depositAmt = 0.0;
                            double balance = 0.0;
                            double payableAmt = 0.0;
                            double totalLienAmt = 0.0;
                            double prematurerateOfInt = 0.0;
                            double penal = 0.0;
                            double minBal2 = CommonUtil.convertObjToDouble(dataMap.get("MIN_BAL")).doubleValue();
                            operativeHeadMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                            operativeAcHeads = (HashMap) sqlMap.executeQueryForObject("getAccNoProdIdDet", operativeHeadMap);
                            System.out.println("operativeAcHeads Premature : " + operativeAcHeads);
                            depositHeadMap.put("PROD_ID", dataMap.get("FLEXI_PROD"));
                            depositHeadMap = (HashMap) sqlMap.executeQueryForObject("getDepositClosingHeads", depositHeadMap);
                            System.out.println("depositHeadMap : " + depositHeadMap);
                            transferTrans.setInitiatedBranch(BRANCH_ID);
                            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                            transactionDAO.setInitiatedBranch(_branchCode);
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(dataMap.get("FLEXI_ACT_NUM")));
                            String linkBatchId = CommonUtil.convertObjToStr(dataMap.get("FLEXI_ACT_NUM"));
                            String authorizeStatus = CommonConstants.STATUS_AUTHORIZED;
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            cashMap.put("LIEN_AC_NO", dataMap.get("FLEXI_ACT_NUM"));
                            System.out.println("cashMap " + cashMap);
                            List lstLien = sqlMap.executeQueryForList("getSBLienTransferAccountNo", cashMap);
                            if (lstLien != null && lstLien.size() > 0) {
                                for (int j = 0; j < lstLien.size(); j++) {
                                    cashMap = (HashMap) lstLien.get(j);
                                    double lienAmt = CommonUtil.convertObjToDouble(cashMap.get("LIEN_AMOUNT")).doubleValue();
                                    System.out.println("lienAmt : " + lienAmt + "JSIZE :" + j);
                                    HashMap depositLienMap = new HashMap();
                                    depositLienMap.put("DEPOSIT_NO", cashMap.get("DEPOSIT_NO"));
                                    List lstDep = sqlMap.executeQueryForList("getSelectEachLienDetails", depositLienMap);
                                    if (lstDep != null && lstDep.size() > 0) {
                                        depositLienMap = (HashMap) lstDep.get(0);
                                        Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositLienMap.get("DEPOSIT_DT")));
                                        Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositLienMap.get("MATURITY_DT")));
                                        long period = DateUtil.dateDiff(depDt, currDt);
                                        System.out.println("period Premature : " + period);
                                        HashMap productMap = new HashMap();
                                        productMap.put("PRODUCT_TYPE", SCREEN);
                                        productMap.put("PROD_ID", depositLienMap.get("PROD_ID"));
                                        productMap.put("AMOUNT", CommonUtil.convertObjToDouble(cashMap.get("LIEN_AMOUNT")));
                                        productMap.put("CUSTID", dataMap.get("CUST_ID"));
                                        productMap.put("DEPOSITNO", depositLienMap.get("DEPOSITNO"));
                                        productMap.put("DEPOSIT_DT", depDt);
                                        productMap.put("MATURITY_DT", currDt);
                                        productMap.put("PERIOD", period);
                                        productMap.put("CATEGORY_ID", depositLienMap.get("CATEGORY"));
                                        depositAmt = CommonUtil.convertObjToDouble(depositLienMap.get("TOTAL_BALANCE")).doubleValue();
                                        double credit = CommonUtil.convertObjToDouble(depositLienMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                        double drawn = CommonUtil.convertObjToDouble(depositLienMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                        double totIntAmt = CommonUtil.convertObjToDouble(depositLienMap.get("TOT_INT_AMT")).doubleValue();
                                        System.out.println("cashMap " + cashMap + "inputMap" + inputMap);
                                        HashMap flexiMap = new HashMap();
                                        String lienNo = CommonUtil.convertObjToStr(cashMap.get("LIEN_NO"));
                                        flexiMap.put("DEPOSIT_ACT_NUM", cashMap.get("DEPOSIT_NO"));
                                        flexiMap.put("USER_ID", CommonConstants.TTSYSTEM);
                                        flexiMap.put("LIEN_AC_NO", dataMap.get("FLEXI_ACT_NUM"));
                                        flexiMap.put("AUTHORIZE_DATE", currDt);
                                        flexiMap.put("LIENNO", lienNo);
                                        flexiMap.put("SUBNO", CommonUtil.convertObjToInt("1"));
                                        flexiMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                                        flexiMap.put("STATUS", "UNLIENED");
                                        flexiMap.put("LIENAMOUNT", new Double(lienAmt * -1));
                                        flexiMap.put("SHADOWLIEN", new Double(0.0));
                                        flexiMap.put("LIEN_NO", lienNo);
                                        flexiMap.put("LIENNO", lienNo);
                                        flexiMap.put("LIEN_AMOUNT", cashMap.get("LIEN_AMOUNT"));
                                        flexiMap.put("UNLIEN_DT", currDt);
                                        sqlMap.executeUpdate("updateReducingLienAmountDAO", flexiMap);
                                        System.out.println("flexiMap : " + flexiMap);
                                        sqlMap.executeUpdate("updateSubAcInfoBal", flexiMap);
                                        sqlMap.executeUpdate("updateForSBLienMarking", flexiMap);
                                        flexiMap = null;
                                        periodMap.put("PROD_ID", dataMap.get("FLEXI_PROD"));
                                        List lstProd = sqlMap.executeQueryForList("getDepProdDetails", periodMap);
                                        if (lstProd != null && lstProd.size() > 0) {
                                            periodMap = (HashMap) lstProd.get(0);
                                            double prodPeriod = CommonUtil.convertObjToDouble(periodMap.get("MIN_DEPOSIT_PERIOD")).doubleValue();
                                            System.out.println("prodPeriod Premature : " + prodPeriod);
                                            if (period >= prodPeriod) {
                                                inputMap = getRateOfInt(productMap);
                                                System.out.println("Premature : " + inputMap);
                                            }
                                        }
                                        prematurerateOfInt = CommonUtil.convertObjToDouble(depositLienMap.get("ROI")).doubleValue();
                                        penal = CommonUtil.convertObjToDouble(inputMap.get("PENAL_INT")).doubleValue();
                                        if (prematurerateOfInt > 0) {
                                            System.out.println("productMap 3rd Premature : " + productMap);
                                            double amount = lienAmt * period * (prematurerateOfInt - penal) / 36500;
                                            System.out.println("amount Premature : " + amount);
                                            amount = (double) getNearest((long) ((long) amount * 100), 100) / 100;
                                            balance = amount - credit;
                                            payableAmt = credit - drawn;
                                            System.out.println("amount: " + amount + "depositAmt :" + depositAmt + "rateOfInt :" + prematurerateOfInt + "penal :" + penal + "totIntAmt :" + totIntAmt);
                                        }
                                        if (balance > 0) {
                                            txMap = new HashMap();
                                            transferList = new ArrayList();
                                            txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                                            txMap.put(TransferTrans.PARTICULARS, " " + cashMap.get("DEPOSIT_NO") + "_1");
                                            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

                                            txMap.put(TransferTrans.CR_AC_HD, (String) depositHeadMap.get("INT_PAY")); // credited to interest payable account head......
                                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);

                                            transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(balance));
                                            transferList.add(transferTo);
                                            transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(balance));
                                            transferList.add(transferTo);
                                            transferTo.setAuthorizeRemarks("REVERSE_FLEXI");
                                            transferTrans.doDebitCredit(transferList, BRANCH_ID);

                                            txMap = new HashMap();
                                            transferList = new ArrayList();
                                            txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("INT_PAY"));//debiting int payable a/c head
                                            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, " " + dataMap.get("FLEXI_ACT_NUM"));

                                            txMap.put(TransferTrans.CR_AC_HD, (String) operativeAcHeads.get("AC_HD_ID"));
                                            txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                                            txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("FLEXI_ACT_NUM"));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");

                                            transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(payableAmt + balance));
                                            transferList.add(transferTo);
                                            txMap.put(TransferTrans.PARTICULARS, " " + cashMap.get("DEPOSIT_NO") + "_1");
                                            transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(payableAmt + balance));
                                            transferList.add(transferTo);
                                            transferTo.setAuthorizeRemarks("REVERSE_FLEXI");
                                            transferTrans.doDebitCredit(transferList, BRANCH_ID);
                                        } else if (balance < 0) {
                                            balance = balance * -1;
                                            txMap = new HashMap();
                                            transferList = new ArrayList();
                                            txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("INT_PAY"));//debiting int payable a/c head
                                            txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                            txMap.put(TransferTrans.PARTICULARS, " " + dataMap.get("FLEXI_ACT_NUM"));

                                            txMap.put(TransferTrans.CR_AC_HD, (String) depositHeadMap.get("INT_PROV_ACHD"));//debiting to int paid a/c head
                                            txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);

                                            transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(balance));
                                            transferList.add(transferTo);
                                            txMap.put(TransferTrans.PARTICULARS, " " + cashMap.get("DEPOSIT_NO") + "_1");
                                            transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(balance));
                                            transferList.add(transferTo);
                                            transferTo.setAuthorizeRemarks("REVERSE_FLEXI");
                                            transferTrans.doDebitCredit(transferList, BRANCH_ID);
                                            balance = balance * -1;
                                        }
                                        inputMap.put("DEOSIT_AMT", new Double(lienAmt * -1));
                                        inputMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                                        System.out.println("lienAmt Premature equals j : " + j);
//                            if(lienAmt == depositAmt && j == 0){
//                                lienAmt = CommonUtil.convertObjToDouble(cashMap.get("LIEN_AMOUNT")).doubleValue();
//                                System.out.println("lienAmt Premature equals : " + lienAmt);
//                                double lien = lienAmt - minBal2;
//                                System.out.println("lienAmt Premature equals : " + lienAmt);
//                                inputMap.put("FLEXI_DEOSIT_AMT", new Double(lien * -1));
//                            }else{
                                        double balAmt = CommonUtil.convertObjToDouble(cashMap.get("LIEN_AMOUNT")).doubleValue();
                                        System.out.println("lienAmt 1ST Premature else : " + lienAmt);
                                        inputMap.put("FLEXI_DEOSIT_AMT", new Double(balAmt * -1));
//                            }                
                                        System.out.println("lienAmt : " + lienAmt + "inputMap :" + inputMap);
                                        updateAvailBalance(inputMap);                //__ To Update the Avail Balance and Flexi Deposit Amount
                                        tdsCalcMap.put("CUST_ID", dataMap.get("CUST_ID"));
                                        tdsCalcMap.put("PROD_ID", dataMap.get("FLEXI_PROD"));
                                        tdsCalcMap.put("DEPOSIT_NO", cashMap.get("DEPOSIT_NO"));
                                        tdsCalcMap.put("RATE_OF_INT", depositLienMap.get("RATE_OF_INT"));
                                        tdsCalcMap.put("INT_PAY", depositHeadMap.get("INT_PAY"));
                                        tdsCalcMap.put("INT_PROV_ACHD", depositHeadMap.get("INT_PROV_ACHD"));
                                        System.out.println("******tdsCalcMap Premature : " + tdsCalcMap);
                                        if (balance != 0) {
                                            updatingDepositInterestTable(tdsCalcMap, balance, depositAmt, totIntAmt, payableAmt, depDt);
                                        }
                                        System.out.println("******tdsCalcMap Premature : " + tdsCalcMap);
                                        if (prematurerateOfInt > 0) {
                                            period = DateUtil.dateDiff(depDt, matDt);
                                            depositAmt = depositAmt - lienAmt;
                                            double balanceIntAmt = depositAmt * period * prematurerateOfInt / 36500;
                                            balanceIntAmt = (double) getNearest((long) ((long) balanceIntAmt * 100), 100) / 100;
                                            HashMap totIntMap = new HashMap();
                                            totIntMap.put("DEPOSIT_NO", cashMap.get("DEPOSIT_NO"));
                                            totIntMap.put("TOT_INT_AMT", new Double(balanceIntAmt));
                                            System.out.println("totIntMap Premature : " + totIntMap);
                                            sqlMap.executeUpdate("updateTotalInterstAmt", totIntMap);
                                        } else {
                                            period = DateUtil.dateDiff(depDt, matDt);
                                            depositAmt = depositAmt - lienAmt;
                                            double rateOfInt = CommonUtil.convertObjToDouble(depositLienMap.get("RATE_OF_INT")).doubleValue();
                                            System.out.println("rateOfInt Premature : " + rateOfInt + "depositAmt :" + depositAmt + "period :" + period);
                                            double balanceIntAmt = depositAmt * period * rateOfInt / 36500;
                                            balanceIntAmt = (double) getNearest((long) ((long) balanceIntAmt * 100), 100) / 100;
                                            System.out.println("rateOfInt : " + rateOfInt + "balanceIntAmt :" + balanceIntAmt + "period :" + period);
                                            HashMap totIntMap = new HashMap();
                                            totIntMap.put("DEPOSIT_NO", cashMap.get("DEPOSIT_NO"));
                                            totIntMap.put("TOT_INT_AMT", new Double(balanceIntAmt));
                                            System.out.println("totIntMap : " + totIntMap);
                                            sqlMap.executeUpdate("updateTotalInterstAmt", totIntMap);
                                        }
                                        depositAmt = CommonUtil.convertObjToDouble(depositLienMap.get("TOTAL_BALANCE")).doubleValue();
                                        calcuateTDS(tdsCalcMap, balance, depositAmt, totIntAmt);                //calcualting TDS...                
                                        txMap = new HashMap();
                                        transferTo = new TxTransferTO();
                                        txMap.put(TransferTrans.DR_AC_HD, (String) depositHeadMap.get("FIXED_DEPOSIT_ACHD"));
                                        txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("FLEXI_PROD"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, cashMap.get("DEPOSIT_NO") + "_1");
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put(TransferTrans.PARTICULARS, "Premature Closing Flexi Deposit " + " " + cashMap.get("DEPOSIT_NO") + "_1");
                                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                        System.out.println("txMap : " + txMap + "lienAmt :" + lienAmt);
                                        transferTo = transactionDAO.addTransferDebitLocal(txMap, lienAmt);
                                        transferTo.setAuthorizeRemarks("REVERSE_FLEXI");
                                        transferEachList.add(transferTo);
                                        totalLienAmt += lienAmt;
                                        System.out.println("totalLienAmt : " + totalLienAmt + "lienAmt :" + lienAmt);
                                        if (lienAmt == depositAmt) {
                                            depositMap.put("DEPOSIT_NO", cashMap.get("DEPOSIT_NO"));
                                            depositMap.put("DEPOSIT_DT", dataMap.get("DEPOSIT_DT"));
                                            depositMap.put("TOTAL_INT_CREDIT", new Double(balance));
                                            depositMap.put("TOTAL_INT_DRAWN", new Double(payableAmt + balance));
                                            depositMap.put("CURR_RATE_OF_INT", depositLienMap.get("RATE_OF_INT"));
                                            depositMap.put("INTEREST_AMT", new Double(balance));
                                            depositMap.put("DEPOSIT_STATUS", CommonConstants.CLOSED);
                                            depositMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                                            depositMap.put("CLOSE_DT", currDt);
                                            System.out.println("Closing depositMap Premature : " + depositMap);
                                            sqlMap.executeUpdate("updateFlexiClosedDetails", depositMap);
                                            sqlMap.executeUpdate("updateFlexiClosedSubDetails", depositMap);
                                        }
//                            HashMap alreadyReducedMap = new HashMap();
//                            alreadyReducedMap.put("LIEN_NO",lienNo);
//                            sqlMap.executeUpdate("updateAlreadyReduced",alreadyReducedMap);
//                            alreadyReducedMap = null;
                                        oldMap.put("DEPOSIT NO", cashMap.get("DEPOSIT_NO"));
                                        depSubNoAccInfoTO = new DepSubNoAccInfoTO();
                                        List lst = (List) sqlMap.executeQueryForList("getSelectDepSubNoAccInfoTO", oldMap);
                                        if (lst != null && lst.size() > 0) {
                                            oldMap = (HashMap) lst.get(0);
                                            depSubNoAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(oldMap.get("DEPOSIT_NO")));
                                            depSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt(oldMap.get("DEPOSIT_SUB_NO")));
                                            depSubNoAccInfoTO.setDepositDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("DEPOSIT_DT"))));
                                            depSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(oldMap.get("DEPOSIT_PERIOD_DD")));
                                            depSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(oldMap.get("DEPOSIT_PERIOD_MM")));
                                            depSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(oldMap.get("DEPOSIT_PERIOD_YY")));
                                            depSubNoAccInfoTO.setDepositAmt(new Double(lienAmt));
                                            depSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(oldMap.get("INTPAY_MODE")));
                                            depSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(oldMap.get("INTPAY_FREQ")));
                                            depSubNoAccInfoTO.setMaturityDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("MATURITY_DT"))));
                                            depSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(oldMap.get("RATE_OF_INT")));
                                            depSubNoAccInfoTO.setMaturityAmt(new Double(lienAmt));
                                            depSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(oldMap.get("TOT_INT_AMT")));
                                            depSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(oldMap.get("PERIODIC_INT_AMT")));
                                            depSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(oldMap.get("STATUS")));
                                            depSubNoAccInfoTO.setClearBalance(new Double(0.0));
                                            depSubNoAccInfoTO.setAvailableBalance(new Double(0.0));
                                            depSubNoAccInfoTO.setCreateBy(CommonUtil.convertObjToStr(oldMap.get("CREATE_BY")));
                                            depSubNoAccInfoTO.setCloseDt(currDt);
                                            depSubNoAccInfoTO.setCloseBy(CommonUtil.convertObjToStr(CommonConstants.TTSYSTEM));
                                            depSubNoAccInfoTO.setAuthorizeStatus(CommonUtil.convertObjToStr(oldMap.get("AUTHORIZE_STATUS")));
                                            depSubNoAccInfoTO.setAuthorizeDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("AUTHORIZE_DT"))));
                                            depSubNoAccInfoTO.setAuthorizeBy(CommonUtil.convertObjToStr(oldMap.get("AUTHORIZE_BY")));
                                            depSubNoAccInfoTO.setAcctStatus(CommonConstants.CLOSED);
                                            depSubNoAccInfoTO.setLastIntApplDt((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("LAST_INT_APPL_DT"))));
                                            depSubNoAccInfoTO.setTotalIntCredit(new Double(balance));
                                            depSubNoAccInfoTO.setTotalIntDrawn(new Double(payableAmt + balance));
                                            depSubNoAccInfoTO.setTotalBalance(new Double(0.0));
                                            depSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(oldMap.get("INSTALL_TYPE")));
                                            depSubNoAccInfoTO.setPaymentDay((Date) DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr("")));
                                            depSubNoAccInfoTO.setCalender_day(new Double(0));
                                            depSubNoAccInfoTO.setFlexi_status("Y");
                                            depSubNoAccInfoTO.setRenewedDt(getProperDateFormat(oldMap.get("RENEWED_DT")));
                                            sqlMap.executeUpdate("insertDepSubNoAccInfoTOSameNo", depSubNoAccInfoTO);
                                            HashMap intMap = new HashMap();
                                            intMap.put("DEPOSIT_NO", oldMap.get("DEPOSIT_NO"));
                                            lst = (List) sqlMap.executeQueryForList("getSelectDepSubNoIntDetails", intMap);
                                            if (lst != null && lst.size() > 0) {
                                                intMap = (HashMap) lst.get(0);
                                                intMap.put("ACT_NUM", intMap.get("DEPOSIT_NO"));
                                                intMap.put("ACCT_STATUS", CommonConstants.CLOSED);
                                                intMap.put("CURR_RATE_OF_INT", prematurerateOfInt);
                                                intMap.put("SB_INT_AMT", CommonUtil.convertObjToDouble(CommonUtil.convertObjToDouble(intMap.get("SB_INT_AMT")).doubleValue()));
                                                //intMap.put("SB_PERIOD_RUN", String.valueOf(CommonUtil.convertObjToDouble(intMap.get("SB_PERIOD_RUN")).doubleValue()));
                                                intMap.put("SB_PERIOD_RUN", CommonUtil.convertObjToDouble(intMap.get("SB_PERIOD_RUN")).doubleValue());
                                                intMap.put("BAL_INT_AMT", CommonUtil.convertObjToDouble(balance));
                                                intMap.put("INT", CommonUtil.convertObjToDouble(penal));
                                                sqlMap.executeUpdate("updateSbInterestAmountSameNo", intMap);//sameno table itwill store.
                                                intMap = null;
                                            }
                                        }
                                        depSubNoAccInfoTO = null;
                                        lst = null;
                                        HashMap renewalMap = new HashMap();
                                        renewalMap.put("DEPOSIT_NO", cashMap.get("DEPOSIT_NO"));
                                        lst = sqlMap.executeQueryForList("getSelectMaxSLNo", renewalMap);
                                        if (lst != null && lst.size() > 0) {
                                            renewalMap = (HashMap) lst.get(0);
                                            double maxNo = CommonUtil.convertObjToDouble(renewalMap.get("MAX_NO")).doubleValue();
                                            renewalMap.put("DEPOSIT_NO", cashMap.get("DEPOSIT_NO"));
                                            renewalMap.put("COUNT", new Double(maxNo + 1));
                                            sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
                                        } else {
                                            renewalMap.put("DEPOSIT_NO", cashMap.get("DEPOSIT_NO"));
                                            renewalMap.put("COUNT", new Double(1));
                                            sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
                                        }
                                        renewalMap = null;
                                        productMap = null;
                                    }
                                }
                                System.out.println("totalLienAmt :" + totalLienAmt);
                                txMap = new HashMap();
                                txMap.put(TransferTrans.CR_AC_HD, (String) operativeAcHeads.get("AC_HD_ID"));
                                txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                                txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("FLEXI_ACT_NUM"));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                txMap.put(TransferTrans.PARTICULARS, "Premature Closing Flexi Deposit " + " " + cashMap.get("DEPOSIT_NO") + "_1");
                                System.out.println("txMap : " + txMap + "totalLienAmt :" + totalLienAmt);
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, totalLienAmt);
                                transferTo.setAuthorizeRemarks("REVERSE_FLEXI");
                                transferEachList.add(transferTo);
                                transactionDAO.doTransferLocal(transferEachList, BRANCH_ID);
                                HashMap cashAuthMap = new HashMap();
                                cashAuthMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                                cashAuthMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
                                transactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, cashAuthMap);
                                HashMap flexiAmtMap = new HashMap();
                                flexiAmtMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                                List lstFlexi = sqlMap.executeQueryForList("getSelectFlexiAmtDetails", flexiAmtMap);
                                if (lstFlexi != null && lstFlexi.size() > 0) {
                                    flexiAmtMap = (HashMap) lstFlexi.get(0);
                                    double flexiAmt = CommonUtil.convertObjToDouble(flexiAmtMap.get("FLEXI_DEPOSIT_AMT")).doubleValue();
                                    if (flexiAmt == 0) {
                                        sqlMap.executeUpdate("updateFlexiAmtDetails", flexiAmtMap);
                                    }
                                }
                                flexiAmtMap = null;
                                lstFlexi = null;
                                cashMap = null;
                                oldMap = null;
                                inputMap = null;
                                txMap = null;
                                transferList = null;
                                transferTrans = null;
                                transferTo = null;
                                depositHeadMap = null;
                                operativeHeadMap = null;
                                depositMap = null;
                                tdsCalcMap = null;
                                dataMap = null;
                                periodMap = null;
                                transferEachList = null;
                                operativeAcHeads = null;
                            }
//                System.out.println("****** txMap 1st lienAmt Premature : "+txMap);
//                transferTo =  transferTrans.getDebitTransferTO(txMap, Math.abs(lienAmt)) ;
//                transferList.add(transferTo);
//                txMap.put(TransferTrans.PARTICULARS,dataMap.get("FLEXI_ACT_NUM"));
//                transferTo =  transferTrans.getCreditTransferTO(txMap, Math.abs(lienAmt)) ;
//                transferList.add(transferTo);
//                transferTo.setAuthorizeRemarks("REVERSE_FLEXI");
//                transferTrans.doDebitCredit(transferList, BRANCH_ID);
//                }
//                HashMap tdsCalcMap = new HashMap();
//                tdsCalcMap.put("CUST_ID",dataMap.get("CUST_ID"));
//                tdsCalcMap.put("PROD_ID",dataMap.get("FLEXI_PROD"));
//                tdsCalcMap.put("DEPOSIT_NO",dataMap.get("DEPOSITNO"));
//                tdsCalcMap.put("RATE_OF_INT", dataMap.get("RATE_OF_INT"));
//                tdsCalcMap.put("INT_PAY",depositHeadMap.get("INT_PAY"));
//                tdsCalcMap.put("INT_PROV_ACHD",depositHeadMap.get("INT_PROV_ACHD"));
//                System.out.println("******tdsCalcMap Premature : "+tdsCalcMap);
//                if(balance != 0)
//                    updatingDepositInterestTable(tdsCalcMap,balance,depositAmt,totIntAmt,payableAmt,depDt);
//                System.out.println("******tdsCalcMap Premature : "+tdsCalcMap);
//                HashMap depositMap = new HashMap();
//                if(lienAmt == depositAmt){
//                    depositMap.put("DEPOSIT_NO",dataMap.get("DEPOSITNO"));
//                    depositMap.put("DEPOSIT_DT",dataMap.get("DEPOSIT_DT"));
//                    depositMap.put("TOTAL_INT_CREDIT",new Double(balance));
//                    depositMap.put("TOTAL_INT_DRAWN",new Double(payableAmt+balance));
//                    depositMap.put("CURR_RATE_OF_INT", dataMap.get("RATE_OF_INT"));
//                    depositMap.put("INTEREST_AMT", new Double(balance));
//                    depositMap.put("DEPOSIT_STATUS",CommonConstants.CLOSED);
//                    depositMap.put("ACCT_STATUS",CommonConstants.CLOSED);
//                    depositMap.put("CLOSE_DT",currDt);
//                    System.out.println("******depositMap Premature : "+depositMap);
//                    sqlMap.executeUpdate("updateFlexiClosedDetails", depositMap);
//                    sqlMap.executeUpdate("updateFlexiClosedSubDetails", depositMap);
//                } 
//                if(prematurerateOfInt >0){
//                    period = DateUtil.dateDiff(depDt,matDt);
//                    depositAmt = depositAmt - lienAmt;
//                    double balanceIntAmt = depositAmt * period * prematurerateOfInt/36500;
//                    balanceIntAmt = (double)getNearest((long)((long)balanceIntAmt *100),100)/100;
//                    HashMap totIntMap = new HashMap();
//                    totIntMap.put("DEPOSIT_NO",dataMap.get("DEPOSITNO"));
//                    totIntMap.put("TOT_INT_AMT",new Double(balanceIntAmt));
//                    System.out.println("totIntMap Premature : " + totIntMap);
//                    sqlMap.executeUpdate("updateTotalInterstAmt",totIntMap);
//                }else{
//                    period = DateUtil.dateDiff(depDt,matDt);
//                    depositAmt = depositAmt - lienAmt;
//                    double rateOfInt = CommonUtil.convertObjToDouble(dataMap.get("RATE_OF_INT")).doubleValue();
//                    System.out.println("rateOfInt Premature : " + rateOfInt+"depositAmt :"+depositAmt+"period :"+period);
//                    double balanceIntAmt = depositAmt * period * rateOfInt/36500;
//                    balanceIntAmt = (double)getNearest((long)((long)balanceIntAmt *100),100)/100;
//                    System.out.println("rateOfInt : " + rateOfInt+"balanceIntAmt :"+balanceIntAmt+"period :"+period);
//                    HashMap totIntMap = new HashMap();
//                    totIntMap.put("DEPOSIT_NO",dataMap.get("DEPOSITNO"));
//                    totIntMap.put("TOT_INT_AMT",new Double(balanceIntAmt));
//                    System.out.println("totIntMap : " + totIntMap);
//                    sqlMap.executeUpdate("updateTotalInterstAmt",totIntMap);                    
//                }
//                HashMap lienMap = new HashMap();
//                lienMap.put("LIEN_AC_NO", dataMap.get("FLEXI_ACT_NUM"));
//                lienMap.put("UNLIEN_DT", currDt);
//                sqlMap.executeUpdate("update.FlexiDepositUnLien", lienMap);
//                HashMap depositBalanceMap = new HashMap();
//                depositMap.put("DEPOSIT_NO",dataMap.get("DEPOSITNO"));
//                if(lienAmt == depositAmt)
//                    depositMap.put("BALANCE", new Double(0));
//                else if(lienAmt != depositAmt)
//                    depositMap.put("BALANCE", new Double(lienAmt));
//                                    
//                sqlMap.executeUpdate("update.FlexiDepositBalanceLien", depositMap);                
//                DepSubNoAccInfoTO depSubNoAccInfoTO = new DepSubNoAccInfoTO();
//                HashMap oldMap = new HashMap();
//                oldMap.put("DEPOSIT NO",dataMap.get("DEPOSITNO"));
//                List lst = (List)sqlMap.executeQueryForList("getSelectDepSubNoAccInfoTO",oldMap);
//                if(lst !=null && lst.size()>0){
//                    oldMap=(HashMap)lst.get(0);
//                    depSubNoAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(oldMap.get("DEPOSIT_NO")));
//                    depSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToStr(oldMap.get("DEPOSIT_SUB_NO")));
//                    depSubNoAccInfoTO.setDepositDt((Date)DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("DEPOSIT_DT"))));
//                    depSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(oldMap.get("DEPOSIT_PERIOD_DD")));
//                    depSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(oldMap.get("DEPOSIT_PERIOD_MM")));
//                    depSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(oldMap.get("DEPOSIT_PERIOD_YY")));
//                    depSubNoAccInfoTO.setDepositAmt(new Double(lienAmt));
//                    depSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(oldMap.get("INTPAY_MODE")));
//                    depSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(oldMap.get("INTPAY_FREQ")));
//                    depSubNoAccInfoTO.setMaturityDt((Date)DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("MATURITY_DT"))));
//                    depSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(oldMap.get("RATE_OF_INT")));
//                    depSubNoAccInfoTO.setMaturityAmt(new Double(lienAmt));
//                    depSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(oldMap.get("TOT_INT_AMT")));
//                    depSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(oldMap.get("PERIODIC_INT_AMT")));
//                    depSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(oldMap.get("STATUS")));
//                    depSubNoAccInfoTO.setClearBalance(new Double(0.0));
//                    depSubNoAccInfoTO.setAvailableBalance(new Double(0.0));
//                    depSubNoAccInfoTO.setCreateBy(CommonUtil.convertObjToStr(oldMap.get("CREATE_BY")));
//                    depSubNoAccInfoTO.setCloseDt(currDt);
//                    depSubNoAccInfoTO.setCloseBy(CommonUtil.convertObjToStr(CommonConstants.TTSYSTEM));
//                    depSubNoAccInfoTO.setAuthorizeStatus(CommonUtil.convertObjToStr(oldMap.get("AUTHORIZE_STATUS")));
//                    depSubNoAccInfoTO.setAuthorizeDt((Date)DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("AUTHORIZE_DT"))));
//                    depSubNoAccInfoTO.setAuthorizeBy(CommonUtil.convertObjToStr(oldMap.get("AUTHORIZE_BY")));
//                    depSubNoAccInfoTO.setAcctStatus(CommonConstants.CLOSED);
//                    depSubNoAccInfoTO.setLastIntApplDt((Date)DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(oldMap.get("LAST_INT_APPL_DT"))));
//                    depSubNoAccInfoTO.setTotalIntCredit(new Double(balance));
//                    depSubNoAccInfoTO.setTotalIntDrawn(new Double(payableAmt+balance));
//                    depSubNoAccInfoTO.setTotalBalance(new Double(0.0));
//                    depSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(oldMap.get("INSTALL_TYPE")));
//                    depSubNoAccInfoTO.setPaymentDay((Date)DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr("")));
//                    sqlMap.executeUpdate("insertDepSubNoAccInfoTOSameNo", depSubNoAccInfoTO);
//                    HashMap intMap= new HashMap();
//                    intMap.put("DEPOSIT_NO",oldMap.get("DEPOSIT_NO"));
//                    lst = (List)sqlMap.executeQueryForList("getSelectDepSubNoIntDetails",intMap);
//                    if(lst!=null && lst.size()>0){
//                        intMap = (HashMap)lst.get(0);
//                        intMap.put("ACT_NUM",intMap.get("DEPOSIT_NO"));
//                        if(prematurerateOfInt == 0)
//                            prematurerateOfInt = 0.0;
//                        if(penal == 0)
//                            penal = 0.0;
//                        if(balance == 0)
//                            balance = 0.0;
//                        intMap.put("ACCT_STATUS",CommonConstants.CLOSED);
//                        intMap.put("CURR_RATE_OF_INT",String.valueOf(prematurerateOfInt));
//                        intMap.put("SB_INT_AMT",CommonUtil.convertObjToDouble(intMap.get("SB_INT_AMT")));
//                        intMap.put("SB_PERIOD_RUN",CommonUtil.convertObjToDouble(intMap.get("SB_PERIOD_RUN")));
//                        intMap.put("BAL_INT_AMT",String.valueOf(balance));
//                        intMap.put("INT",String.valueOf(penal));
//                        sqlMap.executeUpdate("updateSbInterestAmountSameNo", intMap);//sameno table itwill store.
//                        intMap = null;
//                    }
//                }
//                lst = null;
//                HashMap renewalMap = new HashMap();
//                renewalMap.put("DEPOSIT_NO", dataMap.get("DEPOSITNO"));
//                lst = sqlMap.executeQueryForList("getSelectMaxSLNo",renewalMap);
//                if(lst!=null && lst.size()>0){
//                    renewalMap = (HashMap)lst.get(0);
//                    double maxNo = CommonUtil.convertObjToDouble(renewalMap.get("MAX_NO")).doubleValue();
//                    renewalMap.put("DEPOSIT_NO", dataMap.get("DEPOSITNO"));
//                    renewalMap.put("COUNT", new Double(maxNo+1));
//                    sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
//                }else{
//                    renewalMap.put("DEPOSIT_NO", dataMap.get("DEPOSITNO"));
//                    renewalMap.put("COUNT", new Double(1));
//                    sqlMap.executeUpdate("updateMaxSLNo", renewalMap);
//                }
//                renewalMap = null;                
//                calcuateTDS(tdsCalcMap,balance,depositAmt,totIntAmt);                //calcualting TDS...                
//                depSubNoAccInfoTO = null;
//                oldMap = null;
//                inputMap = null;
//                txMap = null;
//                transferList = null;
//                transferTrans = null;
//                transferTo = null;
//                depositHeadMap = null;
//                operativeHeadMap = null;
//                depositMap = null;
//                tdsCalcMap = null;
//                dataMap = null;
//                lst = null;
                            sqlMap.commitTransaction();
                        } catch (Exception e) {
                            sqlMap.rollbackTransaction();
//                status.setStatus(BatchConstants.ERROR) ;
//                errorMap = new HashMap();
//                errorMap.put("ERROR_DATE",currDt);
//                errorMap.put("TASK_NAME", "ReverseFlexiTask");
//                errorMap.put("ERROR_MSG",e.getMessage());
//                errorMap.put("ACT_NUM",dataMap.get("FLEXI_ACT_NUM"));
//                errorMap.put("BRANCH_ID", branch);
//                sqlMap.startTransaction();
//                sqlMap.executeUpdate("insertError_showing", errorMap);
//                sqlMap.commitTransaction();
//                System.out.println("Error thrown for Depsoit No " + dataMap.get("DEPOSITNO"));
                            String errMsg = "";
                            TTException tte = null;
                            HashMap exceptionMap = null;
                            HashMap excMap = null;
                            String strExc = null;
                            String errClassName = "";
                            if (e instanceof TTException) {
                                System.out.println("#$#$ if TTException part..." + e);
                                tte = (TTException) e;
                                if (tte != null) {
                                    exceptionMap = tte.getExceptionHashMap();
                                    System.out.println("#$#$ if TTException part exceptionMap ..." + exceptionMap);
                                    if (exceptionMap != null) {
                                        ArrayList list = (ArrayList) exceptionMap.get(CommonConstants.EXCEPTION_LIST);
                                        errClassName = CommonUtil.convertObjToStr(exceptionMap.get(CommonConstants.CONSTANT_CLASS));
                                        System.out.println("#$#$ if TTException part EXCEPTION_LIST ..." + list);
                                        if (list != null && list.size() > 0) {
                                            for (int a = 0; a < list.size(); a++) {
                                                if (list.get(a) instanceof HashMap) {
                                                    excMap = (HashMap) list.get(a);
                                                    System.out.println("#$#$ if TTException part excMap ..." + excMap);
                                                    strExc = CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_MESSAGE))
                                                            + " (" + CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_DATA)) + ")";
                                                } else {
                                                    strExc = (String) list.get(a);
                                                    System.out.println("#$#$ if TTException part strExc ..." + strExc);
                                                }
                                                errorMap = new HashMap();
                                                errorMap.put("ERROR_DATE", currDt);
                                                errorMap.put("TASK_NAME", taskLable);
                                                errorMap.put("ERROR_MSG", strExc);
                                                errorMap.put("ERROR_CLASS", errClassName);
                                                errorMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                                                errorMap.put("BRANCH_ID", branch);
                                                sqlMap.startTransaction();
                                                sqlMap.executeUpdate("insertError_showing", errorMap);
                                                sqlMap.commitTransaction();
                                                errorMap = null;
                                            }
                                        }
                                    } else {
                                        System.out.println("#$#$ if not TTException part..." + e);
                                        errMsg = e.getMessage();
                                        errorMap = new HashMap();
                                        errorMap.put("ERROR_DATE", currDt);
                                        errorMap.put("TASK_NAME", taskLable);
                                        errorMap.put("ERROR_MSG", errMsg);
                                        errorMap.put("ERROR_CLASS", errClassName);
                                        errorMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                                        errorMap.put("BRANCH_ID", branch);
                                        sqlMap.startTransaction();
                                        sqlMap.executeUpdate("insertError_showing", errorMap);
                                        sqlMap.commitTransaction();
                                        errorMap = null;
                                    }
                                }
                            } else {
                                System.out.println("#$#$ if not TTException part..." + e);
                                errMsg = e.getMessage();
                                errorMap = new HashMap();
                                errorMap.put("ERROR_DATE", currDt);
                                errorMap.put("TASK_NAME", taskLable);
                                errorMap.put("ERROR_MSG", errMsg);
                                errorMap.put("ERROR_CLASS", errClassName);
                                errorMap.put("ACT_NUM", dataMap.get("FLEXI_ACT_NUM"));
                                errorMap.put("BRANCH_ID", branch);
                                sqlMap.startTransaction();
                                sqlMap.executeUpdate("insertError_showing", errorMap);
                                sqlMap.commitTransaction();
                                errorMap = null;
                            }
                            status.setStatus(BatchConstants.ERROR);
                            //                        System.out.println("Error thrown for Operative A/c No " + dataMap.get("ACT_NUM"));
                            //                e.printStackTrace();
                            tte = null;
                            exceptionMap = null;
                            excMap = null;


                            e.printStackTrace();
                        }
                    }
                    dataMap = null;
                    acctList = null;

                }
                if (!compStatus.equals("COMPLETED")) {
                    if (status.getStatus() != BatchConstants.ERROR) {
                        sqlMap.startTransaction();
                        if (compStatus.equals("ERROR")) {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("updateTskStatus", statusMap);
                            statusMap = null;
                        } else {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                            statusMap = null;
                        }
                        sqlMap.commitTransaction();
                    } else {
                        if (compStatus.equals("ERROR")) {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "ERROR");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("updateTskStatus", statusMap);
                            statusMap = null;
                        } else {
//                            isError = true;
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "ERROR");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                            statusMap = null;

                        }
                    }
                }
            }
        }
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }

        return status;
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    private void updatingDepositInterestTable(HashMap interestMap, double balance, double depositAmt,
            double totIntAmt, double payableAmt, Date depDt) throws Exception {
        System.out.println("interestMap :" + interestMap + "balance :" + balance + "depositAmt :" + depositAmt + "totIntAmt :" + totIntAmt);
        interestBatchTO = new InterestBatchTO();
        Date depositDt = (Date) currDt.clone();
        if (depDt != null) {
            depositDt.setDate(depDt.getDate());
            depositDt.setMonth(depDt.getMonth());
            depositDt.setYear(depDt.getYear());
        }
        interestBatchTO.setIntDt(depositDt);
        interestBatchTO.setApplDt(currDt);
        interestBatchTO.setActNum(interestMap.get("DEPOSIT_NO") + "_1");
        interestBatchTO.setProductId(CommonUtil.convertObjToStr(interestMap.get("PROD_ID")));
        interestBatchTO.setPrincipleAmt(new Double(depositAmt));
        interestBatchTO.setCustId(CommonUtil.convertObjToStr(interestMap.get("CUST_ID")));
        interestBatchTO.setIntRate(CommonUtil.convertObjToDouble(interestMap.get("RATE_OF_INT")));
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_ID", interestMap.get("PROD_ID"));
        List resList = (List) sqlMap.executeQueryForList("getBehavesLike", prodMap);
        prodMap.put("BEHAVES_LIKE", ((HashMap) resList.get(0)).get("BEHAVES_LIKE"));
        if (prodMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            interestBatchTO.setIntType("COMPOUND");
        } else if (prodMap.get("BEHAVES_LIKE").equals("FIXED")) {
            interestBatchTO.setIntType("SIMPLE");
        }
        interestBatchTO.setProductType("TD");
        interestBatchTO.setTrnDt(currDt);
        HashMap depMap = new HashMap();
        if (balance > 0) {
            interestBatchTO.setDrCr("CREDIT");
            interestBatchTO.setTransLogId("Payable");
            interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(interestMap.get("INT_PROV_ACHD")));
            interestBatchTO.setIntAmt(new Double(balance));
            interestBatchTO.setTot_int_amt(new Double(balance));
            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
            interestBatchTO.setDrCr("DEBIT");
            interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(interestMap.get("INT_PAY")));
            interestBatchTO.setTransLogId("Payable");
            interestBatchTO.setIntAmt(new Double(balance + payableAmt));
            interestBatchTO.setTot_int_amt(new Double(0.0));
            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);
        } else if (balance < 0) {
            interestBatchTO.setDrCr("DEBIT");
            interestBatchTO.setAcHdId(CommonUtil.convertObjToStr(interestMap.get("INT_PAY")));
            interestBatchTO.setTransLogId("Payable");
            interestBatchTO.setIntAmt(new Double(balance * -1));
            interestBatchTO.setTot_int_amt(new Double(0.0));
            sqlMap.executeUpdate("insertDepositInterestTO", interestBatchTO);

        }
        balance = 0;
        depositAmt = 0;
        totIntAmt = 0;
        interestBatchTO = null;
    }

    private HashMap calcuateTDS(HashMap tdsCalcMap, double balance, double depositAmt, double totIntAmt) throws Exception {
        System.out.println("####### doDepositClose tdsCalcMap " + tdsCalcMap);
        double intTrfAmt = 0.0;
        String Prod_type = "TD";
        interestBatchTO = new InterestBatchTO();
        TdsCalc tdsCalculator = new TdsCalc(BRANCH_ID);
        String CustId = CommonUtil.convertObjToStr(tdsCalcMap.get("CUST_ID"));
        String prod_id = CommonUtil.convertObjToStr(tdsCalcMap.get("PROD_ID"));
        String accnum = CommonUtil.convertObjToStr(tdsCalcMap.get("DEPOSIT_NO"));
        intTrfAmt = balance;
        HashMap tdsMap = new HashMap();
        HashMap closeMap = new HashMap();
        closeMap.put("DEPOSIT_NO", tdsCalcMap.get("DEPOSIT_NO"));
        closeMap.put("RATE_OF_INT", tdsCalcMap.get("RATE_OF_INT"));
        closeMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));
        tdsMap.put("INT_DATE", currDt);
        tdsMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));
        List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
        if (exceptionList == null || exceptionList.size() <= 0) {
            tdsMap = new HashMap();
            tdsMap = tdsCalculator.tdsCalcforInt(CustId, intTrfAmt, accnum, Prod_type, prod_id, closeMap);
            System.out.println("####### doDepositClose tdsMap " + tdsMap);
            if (tdsMap != null) {
                interestBatchTO.setIsTdsApplied("Y");
                interestBatchTO.setTdsAmt(CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")));
            }
        }
        closeMap = null;
        return tdsMap;
    }
    
    public Date getProperDateFormat(Object obj) {
        Date currDate1 = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate1 = (Date) currDt.clone();
            currDate1.setDate(tempDt.getDate());
            currDate1.setMonth(tempDt.getMonth());
            currDate1.setYear(tempDt.getYear());
        }
        return currDate1;
    }

    private ArrayList getDepositLienAccounts() throws Exception {//__ To be Called from the Code...
        ArrayList accountList = new ArrayList();
        HashMap tempMap = new HashMap();
//        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))
        tempMap.put(CommonConstants.BRANCH_ID, branch);
//        else
        tempMap.put("NEXT_DATE", currDt);
        accountList = (ArrayList) sqlMap.executeQueryForList("ReverseFlexiPremature.getDepositData", tempMap);
        System.out.println("accountList: " + accountList);
        tempMap = null;
        return accountList;
    }

    //__ To Update the Avail Balance and Flexi Deposit Amount
    private void updateAvailBalance(HashMap dataMap) throws Exception {
        sqlMap.executeUpdate("Flexi.updateFlexiBalance", dataMap);
    }

    private HashMap getProdDetails(HashMap inputMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("getDepProdIntPay", inputMap);
        return (HashMap) list.get(0);
    }

    //__ To get the Rate OF Interest...
    private HashMap getRateOfInt(HashMap inputMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("icm.getInterestRates", inputMap);
        System.out.println("list : " + list);
        if (list != null && list.size() > 0) {
            inputMap.putAll((HashMap) list.get(0));
            System.out.println("if inputMap : " + inputMap);
        } else {
            inputMap.put("ROI", "0.0");
            inputMap.put("PENAL_INT", "0.0");
            System.out.println("else inputMap : " + inputMap);
        }
        return inputMap;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            TaskHeader header = new TaskHeader();

            header.setBranchID("Bran");
            header.setUserID(CommonConstants.TTSYSTEM);
            header.setIpAddr("172.19.147.86");
            header.setUserID("sysadmin");

            ReverseFlexiTask tsk = new ReverseFlexiTask(header);
            //            TaskStatus Status = tsk.executeTask();
            //            System.out.println("Status: " + Status);

//            tsk.reverseFlexi("OA060897");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
