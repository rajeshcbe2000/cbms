/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * IntPayableTask.java
 *
 * Created on October 27, 2004, 11:17 AM
 * Author : Sunil
 */
package com.see.truetransact.serverside.batchprocess.task.operativeaccount;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.interestcalc.*;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.batchprocess.interest.*;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.transfer.*;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.common.interest.InterestTaskRunner;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.DailyBalanceUpdateTask;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.commonutil.interestcalc.Rounding;

/**
 *
 * @author rahul
 */
public class DebitToCreditCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private TransferDAO transferDAO = new TransferDAO();
    private String branch;
    private int taskSelected = 0;
    private final int BATCHPROCESS = 1;
    private final int OBCODE = 2;
    private final int INSERTDATA = 3;
    private final String SCREEN = "DEBIT_TO_CREDIT_INT";
    private final String MODULE = "OPERATIVE";
    private HashMap paramMap;
    private TaskStatus status = null;
    Date checkThisCDate = new Date();
    Date lstintCr = null;
    private String taskLable;
    private String dayEndType;
    private String actBranch;
    private List branchList;
    private Date currDt;
    private String runningProdId = "";
    private String runningActNum = "";

    /**
     * Creates a new instance of IntPayableTask
     */
    public DebitToCreditCheckTask(TaskHeader header) throws Exception {
        System.out.println("header : " + header);
        setHeader(header);
        branch = header.getBranchID();
        initializeTaskObj(header.getTaskParam());
    }

    //__ used by both the Constructors...
    private void initializeTaskObj(HashMap dataMap) throws Exception {
        paramMap = dataMap;
        paramMap.put("TODAY_DT", ServerUtil.getCurrentDate(branch));
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        if (paramMap != null && paramMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(paramMap.get("DAY_END_TYPE"));
        }
        currDt = ServerUtil.getCurrentDate(branch);
        if (paramMap != null && paramMap.containsKey("DEBIT_INT_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("DEBIT_INT_TASK_LABLE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            if (paramMap.containsKey("BRANCH_LST")) {
                branchList = (List) paramMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
                branchList = null;
            }
        }
    }

    private HashMap getAccountHead(HashMap paramMap, HashMap resultMap) throws Exception {
        String accHead = null;
        //Receivable
        //Get Debit Int Ac Head from OP_AC_ACCOUNT_HD_PARAM
        resultMap.put(TransferTrans.DR_ACT_NUM, (String) paramMap.get("ACT_NUM"));
        resultMap.put(TransferTrans.DR_PROD_ID, (String) paramMap.get("PROD_ID"));
        resultMap.put(TransferTrans.DR_PROD_TYPE, getHeader().getProductType());
        resultMap.put(TransferTrans.DR_BRANCH, actBranch);
        resultMap.put(TransferTrans.CR_AC_HD, (String) paramMap.get("DEBIT_INT"));
        resultMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
        resultMap.put(TransferTrans.CR_BRANCH, actBranch);
        if (!resultMap.containsKey(TransferTrans.PARTICULARS)) {
            resultMap.put(TransferTrans.PARTICULARS, "Interest upto " + CommonUtil.convertObjToStr(resultMap.get("END")));
        }
        return resultMap;
    }

    //__ Method to Calculate the Penal Interest...
    private InterestBatchTO runBatchPenal(HashMap resultMap, HashMap paramMap, LogDAO objLogDAO, LogTO objLogTO, HashMap interestMap) throws Exception {
        InterestBatchTO objInterestBatchTO = null;

        resultMap.put("CALC_MODE", InterestTaskRunner.PENAL_INTEREST);
        resultMap.put(TransferTrans.PARTICULARS, "Penal Int Transaction for TOD ");
        objInterestBatchTO = runBatch(resultMap, paramMap, objLogDAO, objLogTO);
        if (objInterestBatchTO != null) {
            interestMap.put(InterestTaskRunner.PENAL_INTEREST, CommonUtil.convertObjToStr(objInterestBatchTO.getIntAmt()));
        } else {
            interestMap.put(InterestTaskRunner.PENAL_INTEREST, "0.0");
        }
//        resultMap.put("CALC_MODE", InterestTaskRunner.ADDITIONAL_INTEREST);
//        objInterestBatchTO = runBatch(resultMap, paramMap, objLogDAO, objLogTO);
//        if (objInterestBatchTO!=null) {
//            interestMap.put(InterestTaskRunner.ADDITIONAL_INTEREST, CommonUtil.convertObjToStr(objInterestBatchTO.getIntAmt()));
//        } else {
//            interestMap.put(InterestTaskRunner.ADDITIONAL_INTEREST, "0.0");
//        }
        return objInterestBatchTO;
    }

    //__ Method to Calculate the Interest...
    private InterestBatchTO runBatch(HashMap resultMap, HashMap paramMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        InterestBatchTO objInterestBatchTO = null;
        InterestTaskRunner interestTaskRun = new InterestTaskRunner();
        System.out.println("Old REsulsMap: " + resultMap);
        actBranch = CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID));
        objInterestBatchTO = interestTaskRun.getInterestAmountCommon(getHeader(), resultMap);
        objInterestBatchTO.setIntDt((Date) resultMap.get("START"));
        objInterestBatchTO.setApplDt((Date) resultMap.get("END"));
        System.out.println("objInterestBatchTO: " + objInterestBatchTO);
        double interestAmt = objInterestBatchTO.getIntAmt().doubleValue();
        //__ In Case of BatchProcess, DO the following...
        if ((taskSelected == BATCHPROCESS || taskSelected == INSERTDATA) && interestAmt > 0) {
            /**
             * To Enter the Data in the Transaction Table...
             */
            ArrayList batchList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            resultMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(resultMap.get(TransferTrans.PARTICULARS))
                    + " (" + CommonUtil.convertObjToStr(resultMap.get("START")) + " - "
                    + CommonUtil.convertObjToStr(resultMap.get("END")) + ")");
            resultMap = prepareMap(paramMap, resultMap);
            objInterestBatchTO.setIntAmt(new Double(-1 * objInterestBatchTO.getIntAmt().doubleValue()));
            objInterestBatchTO.setPrincipleAmt(new Double(-1 * objInterestBatchTO.getPrincipleAmt().doubleValue()));
            transferTrans.setInitiatedBranch(actBranch);
            transferTrans.setLinkBatchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
            batchList.add(transferTrans.getDebitTransferTO(resultMap, interestAmt));
            batchList.add(transferTrans.getCreditTransferTO(resultMap, interestAmt));
            transferTrans.setForDebitInt(true);  // To avoid error message "Insufficient Balance" even 
            // if the clearbalance is not enough for debiting interest
            transferTrans.doDebitCredit(batchList, actBranch, true);

            //__ To insert the data(Act interest) in the ACT_INTEREST...
            sqlMap.executeUpdate("insertInterestBatchTO", objInterestBatchTO);

            objLogTO.setData(objInterestBatchTO.toString());
            objLogTO.setPrimaryKey(objInterestBatchTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
        }
//        if(taskSelected == BATCHPROCESS || taskSelected == INSERTDATA)
//            sqlMap.executeUpdate("updateTODIntCalcDt", objInterestBatchTO);
        return objInterestBatchTO;
    }

    private HashMap prepareMap(HashMap paramMap, HashMap resultMap) throws Exception {
        resultMap = getAccountHead(paramMap, resultMap);
        resultMap.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);

        //Remove keys which were over written with TransferTrans's keys
        resultMap.remove("ACT_NUM");
        resultMap.remove("PROD_ID");

        return resultMap;
    }

    //__ To get the Data regarding the product...
    private ArrayList getProductList(HashMap nonBatch) throws Exception {
        ArrayList productList = new ArrayList();

        if (nonBatch.containsKey(InterestTaskRunner.PRODUCT_ID)) {
            productList = (ArrayList) sqlMap.executeQueryForList("icm.getProducts", nonBatch);
        } else {
            productList = (ArrayList) sqlMap.executeQueryForList("icm.getProducts", null);
        }
        return productList;
    }

    //__ To get the Data regarding the Account Number...
    private ArrayList getAccountList(HashMap nonBatch) throws Exception {
        ArrayList accountList = new ArrayList();
        nonBatch.put("CURR_DT", currDt);
        System.out.println("non Batch = " + nonBatch);
        //For each product, get the account no
        accountList = (ArrayList) sqlMap.executeQueryForList("getDebitToCreditAccounts", nonBatch);

        return accountList;
    }

    //__ Common Method Call...
    private HashMap implementTask(HashMap nonBatch, boolean isExceptionCatch) throws Exception {
        InterestBatchTO objInterestBatchTO = null;
        HashMap interestMap = new HashMap();
        ArrayList accountList = new ArrayList();
        System.out.println("!!!!! Inside implementTask...");
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId(getHeader().getUserID());
        objLogTO.setBranchId(getHeader().getBranchID());
        objLogTO.setIpAddr(getHeader().getIpAddr());
        objLogTO.setModule(MODULE);
        objLogTO.setScreen(SCREEN);
        System.out.println("!!!!! Logged...");
        //__ Get All the Acount Nos...
        System.out.println("!!!!! nonBatch : " + nonBatch);
        accountList = getAccountList(nonBatch);
        System.out.println("!!!!! accountList : " + accountList);
        int accountListSize = accountList.size();
        String runningProdId = "", runningActNum = "";
        for (int acc = 0; acc < accountListSize; acc++) {
            try {
                sqlMap.startTransaction();

                paramMap.put("ACT_NUM", ((HashMap) accountList.get(acc)).get("ACT_NUM")); //Interest is calculated for this Ac No

                ArrayList resultList = new ArrayList();
                resultList = (ArrayList) sqlMap.executeQueryForList("OperativeAccount.getInterestReceivable", paramMap);

                System.out.println("resultList: " + resultList);
                if (resultList.size() > 0) {
                    for (int i = 0, size = resultList.size(); i < size; i++) {
                        //run the interest calculator for each account.
                        HashMap resultMap = (HashMap) resultList.get(i);
                        if (resultMap.get("START") == null) {
                            continue;
                        }
                        resultMap.put("END", (Date) currDt.clone());
                        resultMap.put("BRANCH_ID", nonBatch.get("BRANCH"));
                        System.out.println("resultMaprunbatch" + resultMap);
                        resultMap.put("CALC_MODE", "INTEREST");
                        resultMap.put(TransferTrans.PARTICULARS, "Interest Transaction for TOD ");
                        objInterestBatchTO = runBatch(resultMap, paramMap, objLogDAO, objLogTO);
                        if (objInterestBatchTO != null) {
                            interestMap.put(CommonUtil.convertObjToStr(objInterestBatchTO.getActNum()), CommonUtil.convertObjToStr(objInterestBatchTO.getIntAmt()));
                        } else {
                            interestMap.put(paramMap.get("ACT_NUM"), "0.0");
                        }
                        resultMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
                        resultMap.put("PROD_ID", paramMap.get("PROD_ID"));
                        runBatchPenal(resultMap, paramMap, objLogDAO, objLogTO, interestMap);
                        if (taskSelected == BATCHPROCESS || taskSelected == INSERTDATA) {
                            sqlMap.executeUpdate("updateTODIntCalcDt", objInterestBatchTO);
                        }
                    }
                } else {
                    interestMap.put(paramMap.get("ACT_NUM"), "0.0");
                    System.out.println("implementTask " + paramMap.get("ACT_NUM"));

                }

                sqlMap.commitTransaction();
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();

                if (!isExceptionCatch) {
                    //                    throw new TransRollbackException(e);
                    throw new TTException(e);
                } else {
                    status.setStatus(BatchConstants.ERROR);
                }
            }
        }

        return interestMap;
    }

    //__ To be Called for the Batch Process...
    public TaskStatus executeTask() throws Exception {
        status = new TaskStatus();
        try {
            HashMap nonBatch = new HashMap();
            //__ The selected task is being performed as a Batch Process...
            taskSelected = BATCHPROCESS;

            status.setStatus(BatchConstants.STARTED);
            //        sqlMap.startTransaction();

            ArrayList productList = getProductList(nonBatch);
            int prodListSize = productList.size();

            for (int prod = 0; prod < prodListSize; prod++) {
                //Store all product level parameters
                paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(prod)).get("AC_HD_ID")); //Ac Head
                paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(prod)).get("DEBIT_INT")); //Debit Int Head
                paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(prod)).get("CREDIT_INT")); //Credit Int Head
                paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id
                paramMap.put("TAX_APPLICABLE", ((HashMap) productList.get(prod)).get("TAX_INT_APPLICABLE"));
                paramMap.put("NRO_TAX_AMT", ((HashMap) productList.get(prod)).get("NRO_TAX_AMT"));
                paramMap.put("TAX_HEAD", ((HashMap) productList.get(prod)).get("TAX_HEAD"));

                paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner
                paramMap.put(InterestTaskRunner.PRODUCT_TYPE, getHeader().getProductType()); //Product Type; for Transaction update

                System.out.println("Param Map : " + paramMap);

                nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)

                paramMap.put(CommonConstants.BRANCH_ID, branch);
                nonBatch.put(CommonConstants.BRANCH_ID, branch);
                implementTask(nonBatch, true);
            }

            //        sqlMap.commitTransaction();
            System.out.println("status.getStatus() : " + status.getStatus());
            if (status.getStatus() != BatchConstants.ERROR) {
                status.setStatus(BatchConstants.COMPLETED);
            }
            System.out.println("status.getStatus() : " + status.getStatus());
            paramMap = null;
            taskSelected = 0;
        } catch (Exception e) {
            status.setStatus(BatchConstants.ERROR);
            //            sqlMap.rollbackTransaction();
            e.printStackTrace();
        }
        System.out.println("status in InterestTask : " + status.getStatus());
        return status;
    }

    //__ To be called from the OB, Calculates the Interest and inserts the data into the DataBase...
    public void insertData() throws Exception {
        //__ The selected task is being called from OB of Some Code...
        taskSelected = INSERTDATA;
        final String prodId = CommonUtil.convertObjToStr(paramMap.get(CommonConstants.PRODUCT_ID));
        paramMap.put(InterestTaskRunner.PRODUCT_ID, prodId);

        ArrayList productList = getProductList(paramMap);
        setProdIdData(productList);
        System.out.println("paramMap2: " + paramMap);
        lstintCr = ServerUtil.getCurrentDate(branch);
        implementTask(paramMap, false);
        taskSelected = 0;
    }

    //__ To be called from the OB to get the Interest...
    public HashMap getInterestData() throws Exception {
        //__ The selected task is being called from OB of Some Code...
        taskSelected = OBCODE;
        HashMap dataMap = new HashMap();
        final String prodId = CommonUtil.convertObjToStr(paramMap.get(CommonConstants.PRODUCT_ID));
        paramMap.put(InterestTaskRunner.PRODUCT_ID, prodId);

        ArrayList productList = getProductList(paramMap);
        System.out.println("!!!!!!!! productList.size() : " + productList.size());
        if (productList.size() != 0 && productList != null) { //added by Rajesh
            setProdIdData(productList);
            System.out.println("!!!!!!!! paramMap : " + paramMap);
            lstintCr = ServerUtil.getCurrentDate(branch);
            dataMap = implementTask(paramMap, false);
            taskSelected = 0;
            return dataMap;
        }
        dataMap.put(paramMap.get("ACT_NUM"), "0.0");
        return dataMap;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
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

    //__ to set the Data in the Param Map regarding the Product Data...
    private void setProdIdData(ArrayList productList) throws Exception {
        paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(0)).get("AC_HD_ID")); //Ac Head
        paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(0)).get("DEBIT_INT")); //Debit Int Head
        paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(0)).get("CREDIT_INT")); //Credit Int Head
        paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(0)).get("PROD_ID")); //Product Id

        if (getHeader().getTransactionType().equalsIgnoreCase(CommonConstants.PAYABLE)) {
            paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(0)).get("LAST_APPL_DT_CR")); //Last Interest Applied Dt Payale
            paramMap.put("TAX_APPLICABLE", ((HashMap) productList.get(0)).get("TAX_INT_APPLICABLE"));
            paramMap.put("NRO_TAX_AMT", ((HashMap) productList.get(0)).get("NRO_TAX_AMT"));
            paramMap.put("TAX_HEAD", ((HashMap) productList.get(0)).get("TAX_HEAD"));

        } else {
            paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(0)).get("LAST_APPL_DT_DR")); //Last Interest Applied Dt Recievable
        }

        paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner
        paramMap.put(InterestTaskRunner.PRODUCT_TYPE, getHeader().getProductType()); //Product Type; for Transaction update
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("InterestTask");
            //            header.setTransactionType(CommonConstants.PAYABLE);
            header.setTransactionType(CommonConstants.RECEIVABLE);
            header.setProductType(TransactionFactory.OPERATIVE);

            header.setBranchID("Bran");
            header.setIpAddr("172.19.147.86");
            header.setUserID("Psupro");

            ArrayList acctList = new ArrayList();
            acctList.add("OA060922");//, OA060910
            //            acctList.add("OA060887");

            HashMap taskParam = new HashMap();
            taskParam.put(CommonConstants.PRODUCT_ID, "SBGEN");
            //            taskParam.put(CommonConstants.ACT_NUM, "OA060892");
            taskParam.put(CommonConstants.ACT_NUM, acctList);
            taskParam.put(CommonConstants.BRANCH, "Bran");
            //            taskParam.put("DATE_FROM", DateUtil.getDate(1,10,2003));
            //            taskParam.put("DATE_TO", DateUtil.getDate(31,03,2004));

            header.setTaskParam(taskParam);

//            InterestTask tsk = new InterestTask(header);
            //            TaskStatus status = tsk.executeTask();
//            HashMap dataList = tsk.getInterestData();
//            System.out.println("dataList: " + dataList);

            //            tsk.insertData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void holiydaychecking(Date lstintCr) {
        try {
            HashMap MonthEnd = new HashMap(); //traansferto holidaychecking method;
            boolean checkHoliday = true;
            MonthEnd.put("NEXT_DATE", lstintCr);
            MonthEnd.put("BRANCH_CODE", getHeader().getBranchID());
            //                try{
//            sqlMap.startTransaction();
            lstintCr = lstintCr;
            while (checkHoliday) {
                boolean tholiday = false;
                System.out.println("enterytothecheckholiday" + checkHoliday);
                List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", MonthEnd);
                List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", MonthEnd);
                boolean isHoliday = Holiday.size() > 0 ? true : false;
                boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                if (isHoliday || isWeekOff) {
                    MonthEnd = dateMinus(MonthEnd);
                    checkHoliday = true;
                } else {
                    checkHoliday = doNoHoliday(CommonUtil.convertObjToStr(MonthEnd.get("NEXT_DATE")));;

                }
                //                if(! isWeekOff)
                //                    if(isHoliday) {
                //                        MonthEnd = dateMinus(MonthEnd);
                //                        checkHoliday=true;
                //                    }
                //                    else  {
                //                        checkHoliday=doNoHoliday(CommonUtil.convertObjToStr(MonthEnd.get("NEXT_DATE")));;
                //                    }
                //                if(!isHoliday)
                //                    if(isWeekOff) {
                //                        HashMap week=(HashMap)weeklyOf.get(0);
                //                        int datenum=Lday.getDay();
                //                        int ofyes=CommonUtil.convertObjToInt(week.get("WEEKLY_OFF1"));
                //                        if(datenum==ofyes){
                //                            MonthEnd = dateMinus(MonthEnd);
                //                            checkHoliday=true;
                //                        }
                //                    }
                //                    else {
                //                        checkHoliday=doNoHoliday(CommonUtil.convertObjToStr(MonthEnd.get("NEXT_DATE")));;
                //                    }

            }
//            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap dateMinus(HashMap dateMap) {
        String day = CommonUtil.convertObjToStr(dateMap.get("NEXT_DATE"));
        Date lastDay = (Date) dateMap.get("NEXT_DATE");
        int days = lastDay.getDate();
        days--;
        lastDay.setDate(days);
        dateMap.put("NEXT_DATE", lastDay);
        dateMap.put("BRANCH_CODE", getHeader().getBranchID());
        return dateMap;
    }

    private boolean doNoHoliday(String nonHoliday) {
        checkThisCDate = DateUtil.getDateMMDDYYYY(nonHoliday);
        System.out.println("nonHoliday" + nonHoliday);
        return false;
    }
}
