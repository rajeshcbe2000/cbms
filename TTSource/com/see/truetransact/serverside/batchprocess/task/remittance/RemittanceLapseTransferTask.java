/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RemittanceLapseTransferTask.java
 *
 * Created on May 11, 2009, 12:26 PM
 */
package com.see.truetransact.serverside.batchprocess.task.remittance;

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
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.transaction.transfer.*;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.commonutil.TTException;


import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class RemittanceLapseTransferTask extends Task {

    private static SqlMap sqlMap = null;
    private String userID = "";
    private final String AC_HD_ID = "AC_HD_ID";
    private final String ACT_NUM = "ACT_NUM";
    private final String BRANCH_CODE = "BRANCH_CODE";
    private final String COMMAND = "COMMAND";
    private final String BASE_CURRENCY = "BASE_CURRENCY";
    private String branchID = "";
    private Date curDate = null;
    private double installment = 0;
    HashMap batch = new HashMap();
    private String dayEndType;
    private List branchList;
    private String actBranch;
    private boolean isError = false;
    private String taskLable;
    private HashMap taskMap;
    LinkedHashMap transactionDetailsMap = new LinkedHashMap();
    TransactionTO transactionTODebit = new TransactionTO();

    /**
     * Creates a new instance of RemittanceLapseTransferTask
     */
    public RemittanceLapseTransferTask(TaskHeader header) throws Exception {
        setHeader(header);
        userID = header.getUserID();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        curDate = ServerUtil.getCurrentDate(header.getBranchID());
        branchID = header.getBranchID();
        taskMap = header.getTaskParam();
        if (taskMap != null && taskMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(taskMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            if (taskMap.containsKey("BRANCH_LST")) {
                branchList = (List) taskMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
                tempMap.put("NEXT_DATE", curDate);
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
        if (taskMap != null && taskMap.containsKey("REMIT_LAPSE_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(taskMap.get("REMIT_LAPSE_TASK_LABLE"));
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    //    public TaskStatus executeTask() throws Exception {
    //        TaskStatus status = new TaskStatus();
    ////        try{
    //            implementTask();
    //            System.out.println("@@@@@@@@@status.getStatus()"+status.getStatus());
    //            if(status.getStatus() != BatchConstants.ERROR)
    //                status.setStatus(BatchConstants.COMPLETED);
    //            System.out.println("@@@@@@@@@status"+status);
    //            return status;
    //
    //        }catch(Exception e){
    //            e.printStackTrace();
    //            status.setStatus(BatchConstants.REJECTED);
    //            throw new TransRollbackException(e);
    //        }
    //        return status;
    //    }
    public TaskStatus executeTask() throws Exception {
        HashMap dataMap = new HashMap();
        TaskStatus status = new TaskStatus();
        ArrayList batchList;
        //           try{
        status.setStatus(BatchConstants.STARTED);
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                actBranch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                curDate = ServerUtil.getCurrentDate(actBranch);
                HashMap compMap = new HashMap();
                compMap.put("TASK_NAME", taskLable);
                compMap.put("DAYEND_DT", curDate);
                compMap.put("BRANCH_ID", actBranch);
                List compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                compMap = null;
                String compStatus = "";
                if (compLst != null && compLst.size() > 0) {
                    compMap = (HashMap) compLst.get(0);
                    compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                    compMap = null;
                }
                if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
//                    try{
                    sqlMap.startTransaction();
                    HashMap errorMap = new HashMap();
                    errorMap.put("ERROR_DATE", curDate);
                    errorMap.put("TASK_NAME", taskLable);
                    errorMap.put("BRANCH_ID", actBranch);
                    sqlMap.executeUpdate("deleteError_showing", errorMap);
                    sqlMap.commitTransaction();
                    dataMap = new HashMap();
                    dataMap.put("CUR_DATE", curDate);
                    dataMap.put("BRANCH_ID", actBranch);
                    List lstData = (List) sqlMap.executeQueryForList("getAllLapsedRemittances", dataMap);
                    dataMap = null;
                    if (lstData.size() > 0 && lstData != null) {
                        for (int i = 0; i < lstData.size(); i++) {
                            try {
                                sqlMap.startTransaction();
                                batchList = new ArrayList();
                                dataMap = (HashMap) lstData.get(i);
                                double amt = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                                String lapseHead = CommonUtil.convertObjToStr(dataMap.get("LAPSED_HD"));
                                String issueHead = CommonUtil.convertObjToStr(dataMap.get("ISSUE_HD"));
                                String inst1 = CommonUtil.convertObjToStr(dataMap.get("INSTRUMENT_NO1"));
                                String inst2 = CommonUtil.convertObjToStr(dataMap.get("INSTRUMENT_NO2"));
                                //                       String variableNo = CommonUtil.convertObjToStr(dataMap.get("VARIABLE_NO"));
                                batchList.add(getDebitTransferTO(issueHead, amt, inst1, inst2, actBranch));
                                batchList.add(getCreditTransferTO(lapseHead, amt, inst1, inst2, actBranch));
                                TransferTrans trans = new TransferTrans();
                                trans.setInitiatedBranch(actBranch);
                                trans.doDebitCredit(batchList, actBranch);
                                //update remit_issue
                                sqlMap.executeUpdate("updateRemitIssueForLapse", dataMap);
                                sqlMap.commitTransaction();
                            } catch (Exception e) {
                                sqlMap.rollbackTransaction();
//                              status.setStatus(BatchConstants.ERROR) ;
//                              errorMap = new HashMap();
//                              errorMap.put("ERROR_DATE",curDate);
//                              errorMap.put("TASK_NAME", taskLable);
//                              errorMap.put("ERROR_MSG",e.getMessage());
//                              errorMap.put("BRANCH_ID", actBranch);
//                              errorMap.put("ACT_NUM",dataMap.get("INSTRUMENT_NO2"));
//                              sqlMap.startTransaction();
//                              sqlMap.executeUpdate("insertError_showing", errorMap);
//                              sqlMap.commitTransaction();
//                              System.out.println("Error thrown for Operative A/c No " + dataMap.get("ACT_NUM"));
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
                                                        strExc = (String) list.get(i);
                                                        System.out.println("#$#$ if TTException part strExc ..." + strExc);
                                                    }
                                                    errorMap = new HashMap();
                                                    errorMap.put("ERROR_DATE", curDate);
                                                    errorMap.put("TASK_NAME", taskLable);
                                                    errorMap.put("ERROR_MSG", strExc);
                                                    errorMap.put("ERROR_CLASS", errClassName);
                                                    errorMap.put("ACT_NUM", dataMap.get("INSTRUMENT_NO2"));
                                                    errorMap.put("BRANCH_ID", actBranch);
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
                                            errorMap.put("ERROR_DATE", curDate);
                                            errorMap.put("TASK_NAME", taskLable);
                                            errorMap.put("ERROR_MSG", errMsg);
                                            errorMap.put("ERROR_CLASS", errClassName);
                                            errorMap.put("ACT_NUM", dataMap.get("INSTRUMENT_NO2"));
                                            errorMap.put("BRANCH_ID", actBranch);
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
                                    errorMap.put("ERROR_DATE", curDate);
                                    errorMap.put("TASK_NAME", taskLable);
                                    errorMap.put("ERROR_MSG", errMsg);
                                    errorMap.put("ERROR_CLASS", errClassName);
                                    errorMap.put("ACT_NUM", dataMap.get("INSTRUMENT_NO2"));
                                    errorMap.put("BRANCH_ID", actBranch);
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
                    }
                }
                if (!compStatus.equals("COMPLETED")) {
                    if (status.getStatus() != BatchConstants.ERROR) {
                        sqlMap.startTransaction();
                        if (compStatus.equals("ERROR")) {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", actBranch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", curDate);
                            sqlMap.executeUpdate("updateTskStatus", statusMap);
                            statusMap = null;
                        } else {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", actBranch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", curDate);
                            sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                            statusMap = null;
                        }
                        sqlMap.commitTransaction();
                    } else {
                        if (compStatus.equals("ERROR")) {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", actBranch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "ERROR");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", curDate);
                            sqlMap.executeUpdate("updateTskStatus", statusMap);
                            statusMap = null;
                        } else {
                            isError = true;
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", actBranch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "ERROR");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", curDate);
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
        if (isError) {
            status.setStatus(BatchConstants.ERROR);
            isError = false;
        }
        //           }catch(Exception e){
        //                sqlMap.rollbackTransaction();
        //                status.setStatus(BatchConstants.ERROR) ;
        //                e.printStackTrace();
        //            }
        //           if(status.getStatus() != BatchConstants.ERROR)
        //               status.setStatus(BatchConstants.COMPLETED);
        return status;

    }

    private TxTransferTO getDebitTransferTO(String acHd, double amt, String inst1, String inst2, String branID) {
        final TxTransferTO objTxTransferTO = new TxTransferTO();
        try {
            objTxTransferTO.setBatchId(null);
            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
            objTxTransferTO.setAcHdId(acHd);
            objTxTransferTO.setBranchId(branID);
            objTxTransferTO.setInpCurr("INR");
            objTxTransferTO.setProdType("GL");
            objTxTransferTO.setInpAmount(new Double(amt));
            objTxTransferTO.setAmount(new Double(amt));
            objTxTransferTO.setTransType(CommonConstants.DEBIT);
            objTxTransferTO.setTransDt(curDate);
            objTxTransferTO.setInstType("VOUCHER");
            objTxTransferTO.setInstrumentNo1("1");
            objTxTransferTO.setInstrumentNo2("2");
            objTxTransferTO.setInstDt(curDate);
            objTxTransferTO.setInitChannType("CASHIER");
            objTxTransferTO.setParticulars("TO Lapse " + inst1 + "" + inst2);
            objTxTransferTO.setTransMode("TRANSFER");
            objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            objTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
            objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(branID);
            System.out.println("######objTxTransferTODDDDD" + objTxTransferTO.getInitiatedBranch());
            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
    }

    private TxTransferTO getCreditTransferTO(String acHd, double amt, String inst1, String inst2, String branID) {
        final TxTransferTO objTxTransferTO = new TxTransferTO();
        try {
            objTxTransferTO.setBatchId("");
            objTxTransferTO.setTransId("");
            HashMap prodID = new HashMap();
            HashMap actNum = new HashMap();
            objTxTransferTO.setAcHdId(acHd);
            objTxTransferTO.setBranchId(branID);
            objTxTransferTO.setInpCurr("INR");
            objTxTransferTO.setInpAmount(new Double(amt));
            objTxTransferTO.setAmount(new Double(amt));
            objTxTransferTO.setProdType("GL");
            objTxTransferTO.setTransType(CommonConstants.CREDIT);
            objTxTransferTO.setTransDt(curDate);
            objTxTransferTO.setInstType(null);
            objTxTransferTO.setInstrumentNo1(null);
            objTxTransferTO.setInstrumentNo2(null);
            objTxTransferTO.setInstDt(null);
            objTxTransferTO.setInitChannType("CASHIER");
            objTxTransferTO.setParticulars("BY Lapse " + inst1 + "" + inst2);
            objTxTransferTO.setTransMode("TRANSFER");
            objTxTransferTO.setStatus(CommonConstants.STATUS_CREATED);
            objTxTransferTO.setStatusBy(CommonConstants.TTSYSTEM);
            objTxTransferTO.setInitTransId(CommonConstants.TTSYSTEM);
            objTxTransferTO.setStatusDt(curDate);
            objTxTransferTO.setInitiatedBranch(branID);
//            objTxTransferTO.setBatchId(_branchCode);
            System.out.println("######objTxTransferTO" + objTxTransferTO.getInitiatedBranch());
            prodID = null;
            actNum = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objTxTransferTO;
    }
}
