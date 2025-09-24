/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DebitIntTask.java
 *
 * Created on March 27, 2005, 11:17 AM
 * Author : Sunil
 * Desc : This class is used to calculate the Interest Receivables
 *        Runs for OA. Is always executed as a batch
 *        and is called in the day end batch process.
 *        Accepted value is OA
 *
 *Map Used : InterestCalculationMap
 */
package com.see.truetransact.serverside.batchprocess.task.operativeaccount.interest;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.DailyBalanceUpdateTask;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.common.interest.InterestTaskRunner;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.operativeaccount.TodAllowedDAO;
import com.see.truetransact.transferobject.operativeaccount.TodAllowedTO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author Sunil
 */
public class DebitIntTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch;
    private String productType;
    private TaskStatus status = null;
    private Date currDt = null;
    private String dayEndType;
    private HashMap paramMap;
    private int taskSelected = 0;
    private final int BATCHPROCESS = 1;
    private final int OBCODE = 2;
    private final int INSERTDATA = 3;
    private final String SCREEN = "DEBIT_INTEREST";
    private final String MODULE = "OPERATIVE";
    Date checkThisCDate = new Date();
    Date lstintCr = null;
    private String taskLable;
    private String actBranch;
    private List branchList;
    private boolean isError = false;
    private String runningProdId = "";
    private String runningActNum = "";

    /**
     * Creates a new instance of DebitIntTask
     */
    public DebitIntTask(TaskHeader header) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        productType = header.getProductType();
        initializeTaskObj(header.getTaskParam());
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        System.out.println("@###" + dataMap);
        paramMap = dataMap;
        paramMap.put("TODAY_DT", ServerUtil.getCurrentDate(branch));
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

    //__ To be Called for the Batch Process...
    public TaskStatus executeTask() throws Exception {
        status = new TaskStatus();
        //        try{
        HashMap nonBatch = new HashMap();
        //__ The selected task is being performed as a Batch Process...
        taskSelected = BATCHPROCESS;
        boolean intflg = false;
        //__ get ProductData...
        if (getHeader().getTaskParam().containsKey(CommonConstants.PRODUCT_ID)) {
            nonBatch.put(InterestTaskRunner.PRODUCT_ID, getHeader().getTaskParam().get(CommonConstants.PRODUCT_ID));
            intflg = true;
        }
        //__ getAccount Data
        if (getHeader().getTaskParam().containsKey(CommonConstants.ACT_NUM)) {
            nonBatch.put(CommonConstants.ACT_NUM, getHeader().getTaskParam().get(CommonConstants.ACT_NUM));
            intflg = true;
        }

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

            if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
                HashMap tempMap = new HashMap();
                tempMap.put("NEXT_DATE", currDt);
                tempMap.put("BRANCH_DAY_END_STATUS", "COMPLETED");
                tempMap.put("DC_DAY_END_STATUS", "COMPLETED");
//                branchList=(List)sqlMap.executeQueryForList("getAllBranchesFromDayEnd",tempMap);
                branchList = (List) sqlMap.executeQueryForList("getSelectBranchCompletedLst", tempMap);
                tempMap = null;
            } else {
                HashMap tempMap = new HashMap();
                tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
                branchList = new ArrayList();
                branchList.add(tempMap);
                tempMap = null;
            }
            if (branchList != null && branchList.size() > 0) {
                for (int b = 0; b < branchList.size(); b++) {
                    HashMap branchMap = (HashMap) branchList.get(b);
                    branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                    currDt = ServerUtil.getCurrentDate(branch);
//                    if (taskLable!=null && taskLable.length()>0) {  //Only from DayEnd
                    HashMap compMap = new HashMap();
                    String compStatus = "";
                    List compLst = null;
                    if (!paramMap.containsKey("CHARGES_PROCESS")) {
                        compMap.put("TASK_NAME", taskLable);
                        compMap.put("DAYEND_DT", currDt);
                        compMap.put("BRANCH_ID", branch);
                        compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                        compMap = null;
                        if (compLst != null && compLst.size() > 0) {
                            compMap = (HashMap) compLst.get(0);
                            compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                            compMap = null;
                        }
                    } else {
                        compStatus = "ERROR";
                        compMap.put("TASK_NAME", "A");
                        compMap.put("DAYEND_DT", currDt);
                        compMap.put("BRANCH_ID", branch);
                        compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                        compMap = null;
                    }
                    if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                        try {
                            HashMap errorMap = new HashMap();
                            if (!paramMap.containsKey("CHARGES_PROCESS")) {
                                sqlMap.startTransaction();
                                errorMap.put("ERROR_DATE", currDt);
                                errorMap.put("TASK_NAME", taskLable);
                                errorMap.put("BRANCH_ID", branch);
                                sqlMap.executeUpdate("deleteError_showing", errorMap);
                                sqlMap.commitTransaction();
                            }
                            nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)

                            paramMap.put(CommonConstants.BRANCH_ID, branch);
                            List ntlst = sqlMap.executeQueryForList("lastDebitIntApplDt", paramMap);
                            if (ntlst != null && ntlst.size() > 0) {
                                HashMap dtMap = (HashMap) ntlst.get(0);
                                lstintCr = (Date) dtMap.get(InterestTaskRunner.LAST_APPL_DT);
                                int fre = CommonUtil.convertObjToInt(((HashMap) productList.get(prod)).get("DEBIT_INT_APPL_FREQ"));

                                lstintCr = DateUtil.addDays(lstintCr, fre);
                                Date dayB = new Date();
                                dayB = ServerUtil.getCurrentDate(branch);
                                System.out.println("lstintCr ===========" + lstintCr);
                                System.out.println("dayB ===========" + dayB);
                                if (intflg == false) {
                                    if (DateUtil.dateDiff(lstintCr, dayB) > 0) {
                                        runDailyBalance();
                                        implementTask(nonBatch, true);
                                    } else {
                                        holiydaychecking(lstintCr);
                                        System.out.println("checkThisCDate--------" + checkThisCDate);
                                        System.out.println("datediff###" + DateUtil.dateDiff(checkThisCDate, dayB));
                                        checkThisCDate = new Date(checkThisCDate.getYear(), checkThisCDate.getMonth(), checkThisCDate.getDate());
                                        if (DateUtil.dateDiff(checkThisCDate, dayB) >= 0) {
                                            runDailyBalance();
                                            implementTask(nonBatch, true);
                                        }
                                    }
                                } else {
                                    lstintCr = ServerUtil.getCurrentDate(branch);
                                    implementTask(nonBatch, true);
                                }
//                                    sqlMap.commitTransaction();
                            } else {
                                System.out.println("No Records In DepositProvision Table for this Product " + paramMap.get("PROD_ID"));
                            }
//                                status.setStatus(BatchConstants.COMPLETED);
                        } catch (Exception e) {
//                                
                        }
                    }
                    if (!paramMap.containsKey("CHARGES_PROCESS")) {
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
            }

        }

        //        sqlMap.commitTransaction();
        System.out.println("status.getStatus() : " + status.getStatus());
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }
//        if(isError){
//            status.setStatus(BatchConstants.ERROR);
//            isError = false;
//        }
        System.out.println("status.getStatus() : " + status.getStatus());
        paramMap = null;
        taskSelected = 0;
        return status;
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
        System.out.println("non Batch = " + nonBatch);
        //To get FromActNo & ToActNo if present in paramMap
        if (paramMap.containsKey("ACT_FROM")) {
            nonBatch.put("ACT_FROM", paramMap.get("ACT_FROM"));
        }
        if (paramMap.containsKey("ACT_TO")) {
            nonBatch.put("ACT_TO", paramMap.get("ACT_TO"));
        }
//        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))
        nonBatch.put(CommonConstants.BRANCH_ID, branch);
//        else
        nonBatch.put("NEXT_DATE", currDt);

//          if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))
//            nonBatch.put(CommonConstants.BRANCH_ID, branch);
//        else
//            nonBatch.put("NEXT_DATE", currDt);

        //For each product, get the account no
        // If running for a single Ac/No, take Ac/No from Map
        if (nonBatch.containsKey(CommonConstants.ACT_NUM)) {
            accountList.add(nonBatch);
        } else {
            accountList = (ArrayList) sqlMap.executeQueryForList("icm.getODAccountsOA", nonBatch);
        }

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
        if (taskSelected == OBCODE) {
            accountList = (ArrayList) nonBatch.get(CommonConstants.ACT_NUM);

        } else if (taskSelected == INSERTDATA) {
            accountList = (ArrayList) nonBatch.get(CommonConstants.ACT_NUM);

        } else {
            accountList = getAccountList(nonBatch);
        }
        System.out.println("!!!!! accountList : " + accountList);
        int accountListSize = accountList.size();
        for (int acc = 0; acc < accountListSize; acc++) {
            try {
                //                sqlMap.startTransaction();

                if (taskSelected == OBCODE || taskSelected == INSERTDATA) {
                    paramMap.put("ACT_NUM", accountList.get(acc));

                } else {
                    paramMap.put("ACT_NUM", ((HashMap) accountList.get(acc)).get("ACT_NUM")); //Interest is calculated for this Ac No
                }

                ArrayList resultList = new ArrayList();
                resultList = (ArrayList) sqlMap.executeQueryForList("OperativeAccount.getInterestReceivable", paramMap);

                System.out.println("resultList: " + resultList);
                if (resultList.size() > 0) {
                    for (int i = 0, size = resultList.size(); i < size; i++) {
                        //run the interest calculator for each account.
                        sqlMap.startTransaction();
                        HashMap resultMap = (HashMap) resultList.get(i);
                        if (resultMap.get("START") == null) {
                            continue;
                        }
                        if (paramMap.containsKey("DATE_TO")) {
                            resultMap.put("END", paramMap.get("DATE_TO"));
                        } else {
                            resultMap.put("END", (Date) currDt.clone());
                        }
                        resultMap.put("BRANCH_ID", nonBatch.get("BRANCH"));
                        System.out.println("resultMaprunbatch" + resultMap);
                        resultMap.put("CALC_MODE", "INTEREST");
                        resultMap.put(TransferTrans.PARTICULARS, "Interest Transaction for TOD ");
                        runningActNum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                        runningProdId = CommonUtil.convertObjToStr(paramMap.get("PROD_ID"));
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
                        System.out.println("resultMaprunbatch" + resultMap);
                        if (objInterestBatchTO != null) {
                            interestMap.put(CommonUtil.convertObjToStr(objInterestBatchTO.getActNum()), CommonUtil.convertObjToStr(objInterestBatchTO.getIntAmt()));
                        } else {
                            interestMap.put(paramMap.get("ACT_NUM"), "0.0");
                        }
                        sqlMap.commitTransaction();
                    }
                } else {
                    interestMap.put(paramMap.get("ACT_NUM"), "0.0");
                    System.out.println("implementTask " + paramMap.get("ACT_NUM"));

                }

            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                if (taskSelected != OBCODE) {
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
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i) instanceof HashMap) {
                                            excMap = (HashMap) list.get(i);
                                            System.out.println("#$#$ if TTException part excMap ..." + excMap);
                                            strExc = CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_MESSAGE))
                                                    + " (" + CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_DATA)) + ")";
                                        } else {
                                            strExc = (String) list.get(i);
                                            System.out.println("#$#$ if TTException part strExc ..." + strExc);
                                        }
                                        HashMap errorMap = new HashMap();
                                        errorMap.put("ERROR_DATE", currDt);
                                        errorMap.put("TASK_NAME", taskLable);
                                        errorMap.put("ERROR_MSG", strExc);
                                        errorMap.put("ERROR_CLASS", errClassName);
                                        errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
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
                                HashMap errorMap = new HashMap();
                                errorMap.put("ERROR_DATE", currDt);
                                errorMap.put("TASK_NAME", taskLable);
                                errorMap.put("ERROR_MSG", errMsg);
                                errorMap.put("ERROR_CLASS", errClassName);
                                errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
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
                        HashMap errorMap = new HashMap();
                        errorMap.put("ERROR_DATE", currDt);
                        errorMap.put("TASK_NAME", taskLable);
                        errorMap.put("ERROR_MSG", errMsg);
                        errorMap.put("ERROR_CLASS", errClassName);
                        errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
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
                }
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
            // For creating TOD if debit interest amount is greater than the available balance
            HashMap inputMap = new HashMap();
            inputMap.put("ACCOUNTNO", runningActNum);
            List list = (List) sqlMap.executeQueryForList("getOABalance", inputMap);
            inputMap = (HashMap) list.get(0);
            double availableBalance = CommonUtil.convertObjToDouble(inputMap.get("AVAILABLE_BALANCE")).doubleValue();
            double todAmount = 0;
            if (availableBalance < 0) {
                todAmount = interestAmt;
            } else if (availableBalance < interestAmt) {
                todAmount = interestAmt - availableBalance;
            }
            if (todAmount > 0) {
                TodAllowedTO todAllowedTO = null;
                TodAllowedDAO todAllowedDAO = null;
                todAllowedTO = new TodAllowedTO();
                todAllowedDAO = new TodAllowedDAO();
                todAllowedTO.setTrans_id("");
                todAllowedTO.setAccountNumber(runningActNum);
//                todAllowedTO.setAcctName(CommonUtil.convertObjToStr(drawingPowerMap.get("CUST_NAME")));
                todAllowedTO.setFromDate(currDt);
                todAllowedTO.setToDate(currDt);
                todAllowedTO.setPermittedDt(currDt);
                todAllowedTO.setStatusBy(getHeader().getUserID());
                todAllowedTO.setRemarks("From Debit Interest for TOD");
                todAllowedTO.setProductType(getHeader().getProductType());
                todAllowedTO.setProductId(runningProdId);
//                todAllowedTO.setPermitedBy(getHeader().getUserID());
                todAllowedTO.setTodAllowed(String.valueOf(todAmount));
                todAllowedTO.setBranchCode(_branchCode);
                todAllowedTO.setCommand("INSERT");
                todAllowedTO.setStatus(CommonConstants.STATUS_CREATED);
                todAllowedTO.setStatusBy(getHeader().getUserID());
                todAllowedTO.setStatusDt(currDt);
                todAllowedTO.setTypeOfTOD("SINGLE");
                todAllowedTO.setRepayPeriod(new Double(1));
                todAllowedTO.setRepayPeriodDDMMYY("DAYS");
                todAllowedTO.setRepayDt(currDt);
                HashMap todMap = new HashMap();
                todMap.put("TodAllowed", todAllowedTO);
                todMap.put("MODE", "INSERT");
                todMap.put("BRANCH_CODE", _branchCode);
                todMap.put("USER_ID", getHeader().getUserID());
//                todMap.put("AUTHORIZEMAP", null);
                todAllowedDAO.execute(todMap, true, true);
            }

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

    void holiydaychecking(Date lstintCr) {
        try {
            HashMap MonthEnd = new HashMap();
            boolean checkHoliday = true;
            MonthEnd.put("NEXT_DATE", lstintCr);
            MonthEnd.put("BRANCH_CODE", getHeader().getBranchID());
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
            }
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

    private void runDailyBalance() throws Exception {
        TaskHeader tskHeader = new TaskHeader();
        tskHeader.setBranchID(branch);
        tskHeader.setUserID(getHeader().getUserID());
        tskHeader.setIpAddr(getHeader().getIpAddr());
//                        tskHeader.setTaskClass(CommonUtil.convertObjToStr(observable.getTaskMap().get(chkDailyBalance.getText())));
        tskHeader.setProductType("OA");
        tskHeader.setTaskParam(getHeader().getTaskParam());
        DailyBalanceUpdateTask daily = new DailyBalanceUpdateTask(tskHeader);
        daily.executeTask();
        tskHeader = null;
    }

//    public TaskStatus executeTask() throws Exception {
//        status = new TaskStatus();
//        try{
//            ArrayList productList = (ArrayList)sqlMap.executeQueryForList("icm.getProd" + productType , null);
//            System.out.println("productList : " + productList);
//            HashMap products ;
//            for(int i = 0, j = productList.size() ; i < j ; i++){
//                products = (HashMap)productList.get(i) ;
//                getAccountInfo(products);
//            }
//            
//            if (status.getStatus() != BatchConstants.ERROR)
//                status.setStatus(BatchConstants.COMPLETED);
//        }catch(Exception e){
//            status.setStatus(BatchConstants.ERROR);
//            e.printStackTrace();
//        }
//        return status;
//    }
//    
//    private void getAccountInfo(HashMap products) throws Exception{
//        HashMap accounts ;
//        
//        double debitIntRate = CommonUtil.convertObjToDouble(products.get("DEBIT_INT")).doubleValue();
//        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))
//            products.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());        
//        else
//            products.put("NEXT_DATE", currDt);
//        
//        ArrayList accountList = (ArrayList)sqlMap.executeQueryForList("icm.getODAccounts" + productType, products);
//        System.out.println("accountList : " + accountList);
//        for(int i = 0, j = accountList.size() ; i < j ; i++){
//            accounts = (HashMap)accountList.get(i) ;
//            getIntAmount(products, accounts, debitIntRate) ;
//            accounts = null;
//        }
//    }
//    
//    private void getIntAmount(HashMap products, HashMap accounts, double debitIntRate) throws Exception{
//        accounts.put("TODAY_DT", currDt);
//        ArrayList accountAmtList = (ArrayList)sqlMap.executeQueryForList("icm.getODAccountAmount" + productType, accounts);
//        HashMap accountsAmt = null;
//        double amountPenal = 0 ;
//        double amountNormal = 0 ;
//        double debitPenalRate = 0 ;
//        double interestPenal = 0 ;
//        
//        
//        System.out.println("accountAmtList : " + accountAmtList);
//        for(int i = 0, j = accountAmtList.size() ; i < j ; i++){
//            accountsAmt = (HashMap)accountAmtList.get(i) ;
//            branch = CommonUtil.convertObjToStr(accountsAmt.get(CommonConstants.BRANCH_ID));
//            debitPenalRate = CommonUtil.convertObjToDouble(accountsAmt.get("PENAL_INT")).doubleValue() ;
//            if(productType.equals(TransactionFactory.OPERATIVE) && i > CommonUtil.convertObjToInt(accountsAmt.get("PENAL_START_DAY"))){
//                //Penal Int to be applied on this amount
//                amountPenal+=CommonUtil.convertObjToDouble(accounts.get("AMT")).doubleValue();
//            }
//            else{
//                //Normal Int to be applied on this amount
//                amountNormal+=CommonUtil.convertObjToDouble(accounts.get("AMT")).doubleValue();
//            }
//        }
//        
//        //if AD, get interest rate and overwrite the debitIntRate
//        if(productType.equals(TransactionFactory.ADVANCES)){
//            boolean noError = true ;
//            InterestTaskRunner itr = new InterestTaskRunner();
//            HashMap itrParamMap = new HashMap();
//            itrParamMap.put("AMOUNT", new Double(amountNormal));
//            itrParamMap.put("PROD_ID", products.get("PROD_ID"));
//            itrParamMap.put("PRODUCT_TYPE", productType);
//            itrParamMap.put("CATEGORY_ID", accounts.get("CATEGORY_ID"));
//            itrParamMap.put("DEPOSIT_DT", accounts.get("DEPOSIT_DT")) ;
//            HashMap outputMap ;
//            try{
//                outputMap = itr.getInterestRates(itrParamMap); ;
//                if(outputMap != null){
//                    debitIntRate = CommonUtil.convertObjToDouble(outputMap.get("ROI")).doubleValue();
//                }
//                
//            }catch(Exception error){
//                System.out.println("error : " + error.getMessage());
//                noError = false ;
//            }
//            outputMap = null ;
//            itrParamMap = null ;
//            itr = null ;
//            
//            if(noError == false) //If an error occurred, dont apply int to it
//                return ;
//        }
//        
//        double interestNormal  = (amountNormal * debitIntRate) / 36500 ;
//        System.out.println("interestNormal : " + interestNormal + " FOR ACCT NUM " + accounts);
//        
//        if(productType.equals(TransactionFactory.OPERATIVE)){
//            debitPenalRate += debitIntRate ;
//            interestPenal  = (amountPenal * debitPenalRate) / 36500 ;
//            System.out.println("interestPenal : " + interestPenal + " FOR ACCT NUM " + accounts);
//        }
//        
//        
//        
//        if(accountsAmt != null && (interestPenal + interestNormal) > 0){
//            HashMap resultMap = getDebitMap(accountsAmt) ;
//            ArrayList batchList = new ArrayList();
//            TransferTrans transferTrans = new TransferTrans();
//            batchList.add(transferTrans.getDebitTransferTO(resultMap, interestPenal + interestNormal));
//            batchList.add(transferTrans.getCreditTransferTO(resultMap, interestPenal + interestNormal));
//            try{
//                sqlMap.startTransaction();
//                transferTrans.doDebitCredit(batchList,branch ) ;
//                
//                //update last int applied date in the respective tables
//                HashMap applDtMap = new HashMap();
//                applDtMap.put("ACT_NUM", accountsAmt.get("ACT_NUM"));
//                applDtMap.put("TODAY_DT", currDt);
//                sqlMap.executeUpdate("updateLastIntAppl" + productType, applDtMap);
//                
//                applDtMap = null ;
//                sqlMap.commitTransaction();
//            }catch(Exception e){
//                System.out.println("Interest not applied for Account No : " + accountsAmt.get("ACT_NUM"));
//                e.printStackTrace();
//                sqlMap.rollbackTransaction();
//            }
//        }
//    }
//    private HashMap getDebitMap(HashMap accountsAmt){
//        HashMap resultMap = new HashMap();
//        resultMap.put(TransferTrans.DR_AC_HD, accountsAmt.get("PRODUCT_HEAD"));
//        resultMap.put(TransferTrans.DR_PROD_ID, accountsAmt.get("PROD_ID"));
//        resultMap.put(TransferTrans.DR_ACT_NUM, accountsAmt.get("ACT_NUM"));
//        resultMap.put(TransferTrans.DR_INST_TYPE,"VOUCHER");
//        resultMap.put(TransferTrans.DR_PROD_TYPE,"OA");
//        resultMap.put(TransferTrans.CR_AC_HD, accountsAmt.get("DEBIT_AC_HEAD"));
//        resultMap.put(TransferTrans.CR_BRANCH, accountsAmt.get("BRANCH_CODE"));
//        resultMap.put(TransferTrans.CR_PROD_TYPE,"GL");
//        return resultMap ;
//    }
    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("DebitIntTask");
            header.setBranchID("Bran");
            header.setProductType("AD"); // OA or AD only
            DebitIntTask tsk = new DebitIntTask(header);
            TaskStatus status = tsk.executeTask();
            System.out.println("status : " + status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
