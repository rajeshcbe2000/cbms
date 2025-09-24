/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * FlexiTask.java
 *
 * Created on March 5, 2005, 1:18 PM
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

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.transfer.*;
import com.see.truetransact.serverside.common.transaction.TransferTrans;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;


import com.see.truetransact.serverside.operativeaccount.AccountDAO;
import com.see.truetransact.transferobject.operativeaccount.*;

import com.see.truetransact.serverside.deposit.TermDepositDAO;
import com.see.truetransact.transferobject.deposit.*;

import com.see.truetransact.transferobject.common.authorizedsignatory.*;
import com.see.truetransact.transferobject.common.powerofattorney.PowerAttorneyTO;
import com.see.truetransact.transferobject.common.nominee.NomineeTO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.clientproxy.ProxyParameters;
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
public class FlexiTask extends Task {

    private static SqlMap sqlMap = null;
    //    private TransferDAO transferDAO = new TransferDAO();
    private AccountDAO objAccountDAO;
    private String BRANCH_ID = "BRAN";
    private String USER_ID = "";
    private String SCREEN = "TD";
    private String prodID = "";
    private String categoryID = "";
    private String constitution = "";
    private final String YES = "Y";
    private final String NO = "N";
    private double depositAmt = 0;
    private InterestBatchTO interestBatchTO = null;
    private Date currDt;
    private String dayEndType;
    private HashMap paramMap;
    private List branchList;
    private String branch = null;
    private boolean isError = false;
    private String taskLable;

    /**
     * Creates a new instance of FlexiTask
     */
    public FlexiTask(TaskHeader header) throws Exception {
        setHeader(header);
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        currDt = ServerUtil.getCurrentDate(super._branchCode);
        BRANCH_ID = CommonUtil.convertObjToStr(header.getBranchID());
        USER_ID = CommonUtil.convertObjToStr(header.getUserID());
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
        if (paramMap != null && paramMap.containsKey("FLEXI_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("FLEXI_TASK_LABLE"));
        }
    }

    //__ To be Called for the Batch Process...
    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        TermDepositDAO objTermDepositDAO = new TermDepositDAO();
//        currDt = ServerUtil.getCurrentDate(super._branchCode);
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
                    HashMap dataMap = new HashMap();
                    //__ get the all the account(s) for which flexi is applicable...
                    sqlMap.startTransaction();
                    HashMap errorMap = new HashMap();
                    errorMap.put("ERROR_DATE", currDt);
                    errorMap.put("TASK_NAME", taskLable);
                    errorMap.put("BRANCH_ID", branch);
                    sqlMap.executeUpdate("deleteError_showing", errorMap);
                    sqlMap.commitTransaction();

                    ArrayList accountList = getAccountList();

                    int acctListSize = accountList.size();
                    for (int i = 0; i < acctListSize; i++) {
                        dataMap = (HashMap) accountList.get(i);
                        try {
                            sqlMap.startTransaction();
                            HashMap flexiDepMap = new HashMap();
                            flexiDepMap.put("FLEXI_ACT_NUM", dataMap.get("ACT_NUM"));
                            flexiDepMap.put("FLEXI_DATE", currDt);
                            BRANCH_ID = CommonUtil.convertObjToStr(dataMap.get(CommonConstants.BRANCH_ID));
                            //                System.out.println("flexiDepMap Starting : " +flexiDepMap);                                    
                            List lst = sqlMap.executeQueryForList("getSelectflexiTableDetails", flexiDepMap);
                            if (lst != null && lst.size() > 0) {
                                flexiDepMap = (HashMap) lst.get(0);
                                System.out.println("flexiDepMap : " + flexiDepMap);
                            } else {
                                HashMap whereMap = new HashMap();
                                objAccountDAO = new AccountDAO();
                                HashMap depositHeadMap = new HashMap();

                                //__ Det the Value of Deposit Amt...
                                double availBal = Double.parseDouble(CommonUtil.convertObjToStr(dataMap.get("CLEAR_BALANCE")));
                                double minBal = Double.parseDouble(CommonUtil.convertObjToStr(dataMap.get("MIN_BAL1")));
                                depositAmt = availBal - minBal;
                                //__ Set the Value of Prod Id...
                                Date closedDt = null;
                                prodID = CommonUtil.convertObjToStr(dataMap.get("FLEXI_PROD"));
                                HashMap accountHeadMap = new HashMap();
                                accountHeadMap.put("PROD_ID", prodID);
                                List listProdDetails = sqlMap.executeQueryForList("getDepProdDetails", accountHeadMap);
                                if (listProdDetails != null && listProdDetails.size() > 0) {
                                    HashMap depProdDetails = (HashMap) listProdDetails.get(0);
                                    double maxAmt = Double.parseDouble(CommonUtil.convertObjToStr(depProdDetails.get("MAX_DEPOSIT_AMT")));
                                    double minAmt = Double.parseDouble(CommonUtil.convertObjToStr(depProdDetails.get("MIN_DEPOSIT_AMT")));
                                    double multAmt = Double.parseDouble(CommonUtil.convertObjToStr(depProdDetails.get("AMT_MULTIPLES")));
                                    Date schemeDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depProdDetails.get("SCHEME_INTRO_DT")));
                                    closedDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depProdDetails.get("SCHEME_CLOSING_DT")));
                                    Date schemeIntroDt = (Date) currDt.clone();
                                    Date schemeCloseDt = (Date) currDt.clone();
                                    double balance = depositAmt % multAmt;
                                    //                        System.out.println("depositAmt: " +depositAmt);
                                    //                        System.out.println("balance: "+balance +"minAmt :"+minAmt +"closedDt :"+closedDt);
                                    if (depositAmt % multAmt != 0) {
                                        depositAmt = depositAmt - balance;
                                    }
                                    if (depositAmt <= minAmt) {
                                        depositAmt = 0;
                                        //                            System.out.println("depositAmt: " +depositAmt + " if Condtion AccountNum :"+dataMap.get("ACT_NUM"));
                                    } else {
                                        System.out.println("depositAmt: " + depositAmt + " else Condtion AccountNum :" + dataMap.get("ACT_NUM"));
                                    }
                                    depositHeadMap.put("PROD_ID", dataMap.get("FLEXI_PROD"));
                                    depositHeadMap = (HashMap) sqlMap.executeQueryForObject("getAcctHead", depositHeadMap);
                                    dataMap.put("BEHAVES_LIKE", depositHeadMap.get("BEHAVES_LIKE"));
                                    if (dataMap.get("BEHAVES_LIKE").equals("FIXED")) {
                                        dataMap.put("DISCOUNTED_RATE", "Y");
                                        dataMap.put("INTEREST_TYPE", "DATE OF MATURITY");
                                    }
                                }
                                if (depositAmt > 0 && closedDt == null) {
                                    whereMap.put(CommonConstants.MAP_WHERE, CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));

                                    //__ To get the Data regarding Account Details from AccountDAO...
                                    whereMap = objAccountDAO.executeQuery(whereMap);
                                    //                        System.out.println("whereMap: " + whereMap);

                                    //__ Objects...
                                    AccountTO objAccountTO = (AccountTO) ((List) whereMap.get("AccountTO")).get(0);
                                    ArrayList nomineeList = (ArrayList) whereMap.get("AccountNomineeList");
                                    ArrayList poaList = (ArrayList) whereMap.get("PowerAttorneyTO");
                                    ArrayList authSignList = (ArrayList) whereMap.get("AuthorizedSignatoryTO");
                                    ArrayList autInstList = (ArrayList) whereMap.get("AuthorizedSignatoryInstructionTO");

                                    HashMap map = new HashMap();
                                    AccInfoTO objAccInfoTO = setAccInfoData(objAccountTO);

                                    map.put("DepSubNoAccInfoTO", setDepSubNoAccInfoData(dataMap));
                                    if (CommonUtil.convertObjToStr(objAccountTO.getActCatId()).equalsIgnoreCase("JOINT")) {
                                        map.put("JointAccntTO", setJointAccntData((List) whereMap.get("JointAcctDetails")));
                                    }
                                    //__ Data for the Authorized Signatory...
                                    int authLen = authSignList.size();
                                    HashMap authSignMap = new HashMap();
                                    if (authLen > 0) {
                                        AuthorizedSignatoryTO objAuthSign;
                                        for (int j = 0; j < authLen; j++) {
                                            objAuthSign = (AuthorizedSignatoryTO) authSignList.get(j);
                                            objAuthSign.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            objAuthSign.setStatusBy(CommonConstants.TTSYSTEM);
                                            objAuthSign.setStatusDt(currDt);
                                            objAuthSign.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                                            authSignMap.put(String.valueOf(j), objAuthSign);
                                        }
                                    } else {
                                        objAccInfoTO.setAuthorizedSignatory(NO);
                                    }
                                    //                        System.out.println("authSignMap: " + authSignMap);
                                    map.put("AuthorizedSignatoryTO", authSignMap);
                                    authSignMap = null;

                                    int authInsLen = autInstList.size();
                                    HashMap authInsMap = new HashMap();
                                    //                        System.out.println("authInsLen: " + authInsLen);
                                    if (authInsLen > 0) {
                                        AuthorizedSignatoryInstructionTO objAuthInst;
                                        for (int j = 0; j < authInsLen; j++) {
                                            objAuthInst = (AuthorizedSignatoryInstructionTO) autInstList.get(j);
                                            objAuthInst.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            objAuthInst.setStatusBy(CommonConstants.TTSYSTEM);
                                            objAuthInst.setStatusDt(currDt);
                                            authInsMap.put(String.valueOf(j), objAuthInst);
                                        }
                                    }
                                    map.put("AuthorizedSignatoryInstructionTO", authInsMap);
                                    //                        System.out.println("authInsMap: " + authInsMap);
                                    authInsMap = null;

                                    //__ Power of Autorney Details...
                                    int poaLen = poaList.size();
                                    HashMap poaMap = new HashMap();
                                    //                        System.out.println("poaLen: " + poaLen);
                                    if (poaLen > 0) {
                                        PowerAttorneyTO objPOATO;
                                        for (int j = 0; j < poaLen; j++) {
                                            objPOATO = (PowerAttorneyTO) poaList.get(j);
                                            objPOATO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            objPOATO.setStatusBy(CommonConstants.TTSYSTEM);
                                            objPOATO.setStatusDt(currDt);
                                            objPOATO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                                            poaMap.put(String.valueOf(j), objPOATO);
                                        }
                                    } else {
                                        objAccInfoTO.setPoa(NO);
                                    }
                                    map.put("PoATO", poaMap);
                                    //                        System.out.println("poaMap: " + poaMap);
                                    poaMap = null;

                                    //__ Nominee Details...
                                    int nomineeLen = nomineeList.size();
                                    ArrayList nomineeDataList = new ArrayList();
                                    if (nomineeLen > 0) {
                                        NomineeTO objNomineeTO;
                                        for (int j = 0; j < nomineeLen; j++) {
                                            objNomineeTO = (NomineeTO) nomineeList.get(j);
                                            objNomineeTO.setStatusBy(CommonConstants.TTSYSTEM);
                                            objNomineeTO.setStatus(CommonConstants.STATUS_CREATED);
                                            nomineeDataList.add(objNomineeTO);
                                        }
                                    } else {
                                        objAccInfoTO.setNomineeDetails(NO);
                                    }
                                    map.put("AccountNomineeTO", nomineeDataList);
                                    nomineeDataList = null;

                                    map.put("TERMDEPOSIT", setAccInfoData(objAccountTO));

                                    map.put(CommonConstants.SCREEN, SCREEN);
                                    map.put(CommonConstants.USER_ID, USER_ID);
                                    map.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                                    map.put(CommonConstants.IP_ADDR, "");
                                    map.put(CommonConstants.MODULE, "Deposits");
                                    map.put("UI_PRODUCT_TYPE", "TD");

                                    HashMap newDepositMap = objTermDepositDAO.execute(map, false);//this is creating new term deposit.
                                    //                        System.out.println("******newDepositMap : "+newDepositMap);
                                    if (depositAmt > 0) {
                                        HashMap operativeHeadMap = new HashMap();
                                        operativeHeadMap.put("ACT_NUM", dataMap.get("ACT_NUM"));
                                        HashMap operativeAcHeads = (HashMap) sqlMap.executeQueryForObject("getAccNoProdIdDet", operativeHeadMap);
                                        TransferTrans transferTrans = new TransferTrans();
                                        TxTransferTO transferTo = new TxTransferTO();
                                        HashMap txMap = new HashMap();
                                        ArrayList transferList = new ArrayList();
                                        transferTrans.setInitiatedBranch(BRANCH_ID);
                                        txMap.put(TransferTrans.DR_AC_HD, (String) operativeAcHeads.get("AC_HD_ID"));
                                        txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("PROD_ID"));
                                        txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("ACT_NUM"));
                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                        txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                                        txMap.put(TransferTrans.PARTICULARS, "Crediting New Flexi Deposit" + " " + newDepositMap.get("DEPOSIT NO") + "_1");

                                        txMap.put(TransferTrans.CR_AC_HD, (String) depositHeadMap.get("ACCT_HEAD"));
                                        txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("FLEXI_PROD"));
                                        txMap.put(TransferTrans.CR_ACT_NUM, newDepositMap.get("DEPOSIT NO") + "_1");
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                        txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                                        //                            System.out.println("****** txMap 1st sbIntAmt : "+txMap);
                                        transferTo = transferTrans.getDebitTransferTO(txMap, Math.abs(depositAmt));
                                        transferList.add(transferTo);
                                        txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
                                        transferTo = transferTrans.getCreditTransferTO(txMap, Math.abs(depositAmt));
                                        transferList.add(transferTo);
                                        transferTrans.doDebitCredit(transferList, BRANCH_ID);
                                        HashMap tdsCalcMap = new HashMap();
                                        tdsCalcMap.put("CUST_ID", dataMap.get("CUST_ID"));
                                        tdsCalcMap.put("PROD_ID", dataMap.get("FLEXI_PROD"));
                                        tdsCalcMap.put("DEPOSIT_NO", newDepositMap.get("DEPOSIT NO"));
                                        calcuateTDS(tdsCalcMap);
                                        transferList = null;
                                        operativeHeadMap = null;
                                        operativeAcHeads = null;
                                        depositHeadMap = null;
                                        txMap = null;
                                        transferTrans = null;
                                        transferTo = null;
                                    }
                                    //__ To Update the Available balance in Act_Master...
                                    map = new HashMap();
                                    double balance = 0.0;
                                    double minBal2 = CommonUtil.convertObjToDouble(dataMap.get("MIN_BAL2")).doubleValue();
                                    double flexiDepositAmt = CommonUtil.convertObjToDouble(dataMap.get("FLEXI_DEPOSIT_AMT")).doubleValue();
                                    if (flexiDepositAmt == 0) {
                                        balance = depositAmt - minBal2;
                                    } else {
                                        balance = depositAmt;
                                    }
                                    map.put("ACT_NUM", dataMap.get("ACT_NUM"));
                                    map.put("DEOSIT_AMT", new Double(depositAmt));
                                    map.put("FLEXI_DEOSIT_AMT", new Double(balance));
                                    updateAvailBalance(map);
                                    HashMap flexiMap = new HashMap();
                                    flexiMap.put("FLEXI_ACT_NUM", dataMap.get("ACT_NUM"));
                                    flexiMap.put("DEPOSIT_NO", newDepositMap.get("DEPOSIT NO"));
                                    flexiMap.put("FLEXI_DEPOSIT_AMT", new Double(depositAmt));
                                    flexiMap.put("FLEXI_BALANCE", new Double(balance));
                                    flexiMap.put("CREATED_BY", USER_ID);
                                    flexiMap.put("BRANCH_CODE", BRANCH_ID);
                                    flexiMap.put("FLEXI_DATE", currDt);
                                    flexiMap.put("FLEXI_FLAG", "Y");
                                    //                        System.out.println("******flexiMap : "+flexiMap);
                                    sqlMap.executeUpdate("insertFlexiTable", flexiMap);
                                    map = null;
                                    whereMap = null;
                                    objAccountTO = null;
                                }
                            }
                            sqlMap.commitTransaction();
                        } catch (Exception e) {
                            sqlMap.rollbackTransaction();
//                        status.setStatus(BatchConstants.ERROR) ;
//                        errorMap = new HashMap();
//                        errorMap.put("ERROR_DATE",currDt);
//                        errorMap.put("TASK_NAME", "FlexiTask");
//                        errorMap.put("ERROR_MSG",e.getMessage());
//                        errorMap.put("ACT_NUM",dataMap.get("ACT_NUM"));
//                        errorMap.put("BRANCH_ID", branch);
//                        sqlMap.startTransaction();
//                        sqlMap.executeUpdate("insertError_showing", errorMap);
//                        sqlMap.commitTransaction();
//                        System.out.println("Error thrown for Operative A/c No " + dataMap.get("ACT_NUM"));
//                         if (taskSelected != OBCODE) {
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
                                                errorMap.put("ACT_NUM", dataMap.get("ACT_NUM"));
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
//                                System.out.println("@@@@ggggggg"+dataMap.get("FLEXI_ACT_NUM"));
                                        errorMap.put("ACT_NUM", dataMap.get("ACT_NUM"));
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
                                errorMap.put("ACT_NUM", dataMap.get("ACT_NUM"));
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
//                }
                            e.printStackTrace();
                        }
                    }
                    dataMap = null;
                    accountList = null;
                }

//                dataMap = null;
//                accountList = null;
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
                            isError = true;
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

    //__ To get the Data regarding the product...
    private ArrayList getAccountList() throws Exception {
        ArrayList accountList = new ArrayList();
        HashMap tempMap = new HashMap();
//        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))
        tempMap.put(CommonConstants.BRANCH_ID, branch);
//        else
        tempMap.put("NEXT_DATE", currDt);
        accountList = (ArrayList) sqlMap.executeQueryForList("Flexi.getAccountData", tempMap);
        System.out.println("accountList: " + accountList);
        tempMap = null;
        return accountList;
    }

    //__ To set Account Info data in the Transfer Object
    private AccInfoTO setAccInfoData(AccountTO objAccountTo) throws Exception {
        AccInfoTO objAccInfoTO = new AccInfoTO();
        try {
            objAccInfoTO.setAuthorizedSignatory(YES);
            objAccInfoTO.setCommAddress(CommonUtil.convertObjToStr(objAccountTo.getCommAddrType()));

            objAccInfoTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objAccInfoTO.setCustId(objAccountTo.getCustId());
            objAccInfoTO.setDepositNo(null);
            //__ Check from Bala...
            objAccInfoTO.setFifteenhDeclare(NO);
            objAccInfoTO.setNomineeDetails(YES);
            objAccInfoTO.setOpeningMode("Normal");

            objAccInfoTO.setPanNumber("");
            objAccInfoTO.setPoa(YES);
            objAccInfoTO.setAgentId("");
            objAccInfoTO.setProdId(prodID);
            objAccInfoTO.setDepositStatus("NEW");
            //            objAccInfoTO.setRemarks(this.getTxtRemarks());
            objAccInfoTO.setSettlementMode(CommonUtil.convertObjToStr(objAccountTo.getSettmtModeId()));
            objAccInfoTO.setConstitution(CommonUtil.convertObjToStr(objAccountTo.getActCatId()));
            //__ Set the Valve of the Category...
            categoryID = CommonUtil.convertObjToStr(objAccountTo.getCategoryId());
            objAccInfoTO.setCategory(CommonUtil.convertObjToStr(objAccountTo.getCategoryId()));
            objAccInfoTO.setBranchId(BRANCH_ID);
            objAccInfoTO.setCreatedBy(CommonConstants.TTSYSTEM);
            objAccInfoTO.setCreatedDt(currDt);
            objAccInfoTO.setStatusBy(CommonConstants.TTSYSTEM);
            objAccInfoTO.setStatus(CommonConstants.STATUS_CREATED);
            objAccInfoTO.setStatusDt(currDt);
            objAccInfoTO.setTaxDeductions(YES);
            objAccInfoTO.setFlexiActNum(CommonUtil.convertObjToStr(objAccountTo.getActNum()));
            objAccInfoTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            objAccInfoTO.setAuthorizedBy(CommonConstants.TTSYSTEM);
            objAccInfoTO.setAuthorizedDt(currDt);
            objAccInfoTO.setDeathClaim("N");
            objAccInfoTO.setAutoRenewal("N");
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID", prodID);
            List resList = (List) sqlMap.executeQueryForList("getBehavesLike", prodMap);
            prodMap.put("BEHAVES_LIKE", ((HashMap) resList.get(0)).get("BEHAVES_LIKE"));
            if (prodMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                objAccInfoTO.setRenewWithInt("Y");
            } else if (prodMap.get("BEHAVES_LIKE").equals("FIXED")) {
                objAccInfoTO.setRenewWithInt("Y");
            }
            objAccInfoTO.setMatAlertRep("Y");
            objAccInfoTO.setMember("N");
            objAccInfoTO.setTransOut("N");

        } catch (Exception e) {
            System.out.println("Error in setAccInfoData()");
            e.printStackTrace();
        }
        return objAccInfoTO;
    }

    private HashMap calcuateTDS(HashMap tdsCalcMap) throws Exception {
//        System.out.println("####### doDepositClose tdsCalcMap " + tdsCalcMap);
        double intTrfAmt = 0.0;
        String Prod_type = "TD";
        interestBatchTO = new InterestBatchTO();
        TdsCalc tdsCalculator = new TdsCalc(super._branchCode);
        String CustId = CommonUtil.convertObjToStr(tdsCalcMap.get("CUST_ID"));
        String prod_id = CommonUtil.convertObjToStr(tdsCalcMap.get("PROD_ID"));
        String accnum = CommonUtil.convertObjToStr(tdsCalcMap.get("DEPOSIT_NO"));
        //        intTrfAmt = CommonUtil.convertObjToDouble(tdsCalcMap.get("TDS_AMOUNT")).doubleValue();
        HashMap tdsMap = new HashMap();
        HashMap closeMap = new HashMap();
        closeMap.put("DEPOSIT_NO", tdsCalcMap.get("DEPOSIT_NO"));
        closeMap.put("RATE_OF_INT", tdsCalcMap.get("RATE_OF_INT"));
        closeMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));
        tdsMap.put("INT_DATE", currDt);
        //        tdsMap.put("INT_DATE", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(ServerUtil.getCurrentDate(_branchCode))));
        tdsMap.put("CUSTID", tdsCalcMap.get("CUST_ID"));

        List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
        if (exceptionList == null || exceptionList.size() <= 0) {
            tdsMap = new HashMap();
            tdsMap = tdsCalculator.tdsCalcforInt(CustId, intTrfAmt, accnum, Prod_type, prod_id, closeMap);
//            System.out.println("####### doDepositClose tdsMap " + tdsMap);
            if (tdsMap != null) {
                interestBatchTO.setIsTdsApplied("Y");
                interestBatchTO.setTdsAmt(CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")));
            }
        }
        return tdsMap;
    }

    //__ To set Joint Account data in the Transfer Object
    public HashMap setJointAccntData(List jntAcctAll) throws Exception {
        HashMap singleRecordJntAcct;
        LinkedHashMap jntAcctTOMap = new LinkedHashMap();
        try {
            JointAccntTO objJointAccntTO;
            int jntAcctSize = jntAcctAll.size();
            for (int i = 0; i < jntAcctSize; i++) {
                singleRecordJntAcct = (HashMap) jntAcctAll.get(i);
                objJointAccntTO = new JointAccntTO();
                objJointAccntTO.setCustId(CommonUtil.convertObjToStr(singleRecordJntAcct.get("CUST_ID")));
                objJointAccntTO.setDepositNo("");
                objJointAccntTO.setStatus(CommonConstants.STATUS_CREATED);
                objJointAccntTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                jntAcctTOMap.put(String.valueOf(i), objJointAccntTO);
                objJointAccntTO = null;
                singleRecordJntAcct = null;
            }
        } catch (Exception e) {
            System.out.println("Error in setJointAccntData()");
            e.printStackTrace();
        }
        return jntAcctTOMap;
    }

    //__ To set Depsosit Sub No Account Info data in the Transfer Object
    public HashMap setDepSubNoAccInfoData(HashMap subAcinfoMap) throws Exception {
        int period = CommonUtil.convertObjToInt(subAcinfoMap.get(("FLEXI_PD")));
        HashMap depSubNoAccInfoSingleRec;
        DepSubNoAccInfoTO objDepSubNoAccInfoTO = new DepSubNoAccInfoTO();
        LinkedHashMap depSubNoTOMap = new LinkedHashMap();
        int years = 0;
        int months = 0;
        int days = 0;
        try {
            HashMap dataMap = new HashMap();
            dataMap.put("PRODUCT_TYPE", SCREEN);
            dataMap.put("PROD_ID", prodID);
            dataMap.put("AMOUNT", depositAmt);
            dataMap.put("DEPOSIT_DT", currDt);
            dataMap.put("MATURITY_DT", DateUtil.addDays((Date) currDt.clone(), period));
            dataMap.put("PERIOD", period);
            dataMap.put("CATEGORY_ID", categoryID);

            //__ To put the data regarding the Rate Of Interest in the DataMap...
            dataMap.putAll(getProdDetails(dataMap));

            //__ To put the data regarding the Rate Of Interest in the DataMap...
            dataMap.putAll(getRateOfInt(dataMap));

            InterestCalc objInterestCalc = new InterestCalc();
            HashMap resultMap = new HashMap();
            resultMap = objInterestCalc.calcAmtDetails(dataMap);

            objDepSubNoAccInfoTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objDepSubNoAccInfoTO.setDepositAmt(new Double(depositAmt));
            objDepSubNoAccInfoTO.setDepositDt(currDt);

            //            if(period > 0){
            //                years = period/365;
            //                months = (period%365)/30;
            //                days = ((period%365)%30);
            //            }
            objDepSubNoAccInfoTO.setDepositPeriodDd(new Double(period));
            objDepSubNoAccInfoTO.setDepositPeriodMm(new Double(0.0));
            objDepSubNoAccInfoTO.setDepositPeriodYy(new Double(0.0));

            objDepSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt("1"));
            objDepSubNoAccInfoTO.setIntpayFreq(new Double(0));
            objDepSubNoAccInfoTO.setIntpayMode("TRANSFER");
            objDepSubNoAccInfoTO.setInstallType("FIXED");

            objDepSubNoAccInfoTO.setPaymentType("");
            objDepSubNoAccInfoTO.setPaymentDay(null);

            objDepSubNoAccInfoTO.setMaturityAmt(new Double(depositAmt));
            objDepSubNoAccInfoTO.setMaturityDt(DateUtil.addDays((Date) currDt.clone(), period));
            objDepSubNoAccInfoTO.setPeriodicIntAmt(new Double(0));
            objDepSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(dataMap.get("ROI")));
            objDepSubNoAccInfoTO.setStatus(CommonConstants.STATUS_CREATED);
            objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(resultMap.get("INTEREST")));
            objDepSubNoAccInfoTO.setCreateBy(CommonConstants.TTSYSTEM);
            objDepSubNoAccInfoTO.setLastTransDt(currDt);
            objDepSubNoAccInfoTO.setAcctStatus("NEW");
            objDepSubNoAccInfoTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            objDepSubNoAccInfoTO.setAuthorizeBy(CommonConstants.TTSYSTEM);
            objDepSubNoAccInfoTO.setAuthorizeDt(currDt);
            objDepSubNoAccInfoTO.setIntPayProdType("OA");
            objDepSubNoAccInfoTO.setIntPayProdId(CommonUtil.convertObjToStr(subAcinfoMap.get("PROD_ID")));
            objDepSubNoAccInfoTO.setIntPayAcNo(CommonUtil.convertObjToStr(subAcinfoMap.get("ACT_NUM")));
            objDepSubNoAccInfoTO.setCalender_day(new Double(0));
            objDepSubNoAccInfoTO.setFlexi_status("Y");
            depSubNoTOMap.put("0", objDepSubNoAccInfoTO);
            objDepSubNoAccInfoTO = null;
            depSubNoAccInfoSingleRec = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            System.out.println("Error in setDepSubNoAccInfoData()");
            e.printStackTrace();
        }
        return depSubNoTOMap;
    }

    //__ To get the Data related to Product...
    private HashMap getProdDetails(HashMap inputMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("getDepProdIntPay", inputMap);
        return (HashMap) list.get(0);
    }

    //__ To get the Rate OF Interest...
    private HashMap getRateOfInt(HashMap inputMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("icm.getInterestRates", inputMap);
        if (list != null && list.size() > 0) {
            return (HashMap) list.get(0);
        } else {
            throw new TTException("Rate of interest is not set for this period...");
        }
    }

    //__ To Update the Available Balance in Act_master...
    private void updateAvailBalance(HashMap inputMap) throws Exception {
        sqlMap.executeUpdate("Flexi.updateFlexiBalance", inputMap);
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


            //            TaskStatus status = tsk.executeTask();
            //            System.out.println("status: " + status);

            int period = 364;
            //            System.out.println("Years: " + period/365);
            //            System.out.println("Months: " + (period%365)/30);
            //            System.out.println("Days: " + ((period%365)%30));
            FlexiTask ft = new FlexiTask(header);
            TaskStatus status = ft.executeTask();
            System.out.println("status: " + status);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
