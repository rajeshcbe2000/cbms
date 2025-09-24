/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DormantToInOperativeTask.java
 *
 * Created on May 19, 2004, 12:28 PM
 */
package com.see.truetransact.serverside.batchprocess.task.operativeaccount;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.servicelocator.ServiceLocator;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
//import com.see.truetransact.serverside.common.transaction.TransferTransLog ;

/**
 *
 * @author bala
 */
public class DormantToInOperativeTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    private Date currDt = null;
    private String dayEndType;
    private List branchList;
    private HashMap taskMap;
    private String taskLable;

    /**
     * Creates a new instance of DormantToInOperativeTask
     */
    public DormantToInOperativeTask(TaskHeader header) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        currDt = ServerUtil.getCurrentDate(branch);
        taskMap = header.getTaskParam();
        if (taskMap != null && taskMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(taskMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            if (taskMap.containsKey("BRANCH_LST")) {
                branchList = (List) taskMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
//                HashMap tempMap = new HashMap();
//                tempMap.put("NEXT_DATE", currDt);
//                branchList=(List)sqlMap.executeQueryForList("getAllBranchesFromDayEndComp",tempMap);
//                tempMap = null;
                branchList = null;
            }
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
        if (taskMap != null && taskMap.containsKey("CHK_TRN_OPT_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(taskMap.get("CHK_TRN_OPT_TASK_LABLE"));
        }
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap accountMap = null;
        HashMap transferMap = new HashMap();
        HashMap actData = null;
        HashMap paramMap = new HashMap();
        paramMap.put("TODAY_DT", currDt);
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                currDt = ServerUtil.getCurrentDate(branch);
                HashMap compMap = new HashMap();
                List compLst = null;
                compMap.put("TASK_NAME", taskLable);
                compMap.put("DAYEND_DT", currDt);
                compMap.put("BRANCH_ID", branch);
                compLst = (List) sqlMap.executeQueryForList("getSelectTaskLstDayBegin", compMap);
                compMap = null;
                String compStatus = "";
                if (compLst != null && compLst.size() > 0) {
                    compMap = (HashMap) compLst.get(0);
                    compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                    compMap = null;
                }
                if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                    sqlMap.startTransaction();
                    HashMap errorMap = new HashMap();
                    errorMap.put("ERROR_DATE", currDt);
                    errorMap.put("TASK_NAME", taskLable);
                    errorMap.put("BRANCH_ID", branch);
                    sqlMap.executeUpdate("deleteError_showing", errorMap);
                    sqlMap.commitTransaction();
                    paramMap.put(CommonConstants.BRANCH_ID, branch);
                    try {
                        sqlMap.startTransaction();

                        String actNum = null;
                        double amountToTransfer = 0.0;
                        sqlMap.executeUpdate("OperativeAccount.DormantToInoperativeMovementBatch", paramMap);
                        List accountList = sqlMap.executeQueryForList("OA.getAccountsToBeMadeInoperative", paramMap);
                        for (int i = 0, j = accountList.size(); i < j; i++) {
                            System.out.println("#### i = " + i);
                            accountMap = (HashMap) accountList.get(i);
                            actNum = CommonUtil.convertObjToStr(accountMap.get("ACT_NUM"));
//                            amountToTransfer = CommonUtil.convertObjToDouble(accountMap.get("AVAILABLE_BALANCE")).doubleValue() ;
//                            
//                            actData = new HashMap();
//                            actData.put("DEBIT_HD", accountMap.get("DEBIT_HD"));
//                            actData.put("CREDIT_HD", accountMap.get("CREDIT_HD"));
//                            
//                            System.out.println("#### transferMap before if = "+transferMap);
//                            if(transferMap.containsKey(actData)){
//                                System.out.println("#### inside if = "+transferMap);
//                                transferMap.put(actData, new Double(
//                                CommonUtil.convertObjToDouble(transferMap.get(actData)).doubleValue() + amountToTransfer)) ;
//                                //                    transferMap.put(transferMap.get(actData), new Double(
//                                //                    CommonUtil.convertObjToDouble(((HashMap)transferMap.get(actData))).doubleValue() + amountToTransfer)) ;
//                            }
//                            else{
//                                System.out.println("#### inside else = "+transferMap);
//                                transferMap.put(actData, new Double(amountToTransfer));
//                            }
//                            System.out.println("#### transferMap after if = "+transferMap);
//                            actData = null;
                            paramMap.put("ACT_NUM", actNum);
                            sqlMap.executeUpdate("OA.makeAccountInOperative", paramMap);
                            actData = null;
                        }

                        // Operative to Dormant Task
                        sqlMap.executeUpdate("OperativeAccount.OperationalToDormantMovementBatch", paramMap);
                        sqlMap.executeUpdate("OperativeAccount.OperationalToDormantBatch", paramMap);

                        // New to Operative Task
                        sqlMap.executeUpdate("OperativeAccount.newToOperationalMovementBatch", paramMap);
                        sqlMap.executeUpdate("OperativeAccount.newToOperationalBatch", paramMap);

//                        Iterator iterator = transferMap.keySet().iterator() ;
//                        System.out.println("transferMap " + transferMap);
//                        while(iterator.hasNext()){
//                            actData = (HashMap)iterator.next() ;
//                            System.out.println("actData : " + actData);
//                            doTransfer(actData, CommonUtil.convertObjToDouble(transferMap.get(actData)).doubleValue()) ;
//                        }
                        sqlMap.commitTransaction();
                    } catch (Exception e) {
                        sqlMap.rollbackTransaction();
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
                                            errorMap = new HashMap();
                                            errorMap.put("ERROR_DATE", currDt);
                                            errorMap.put("TASK_NAME", taskLable);
                                            errorMap.put("ERROR_MSG", strExc);
                                            errorMap.put("ERROR_CLASS", errClassName);
                                            errorMap.put("ACT_NUM", "GL");
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
                                    errorMap.put("ACT_NUM", "minor to major");
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
                            errorMap.put("ACT_NUM", "minor to major");
                            errorMap.put("BRANCH_ID", branch);
                            sqlMap.startTransaction();
                            sqlMap.executeUpdate("insertError_showing", errorMap);
                            sqlMap.commitTransaction();
                            errorMap = null;
                        }
                        status.setStatus(BatchConstants.ERROR);
                        tte = null;
                        exceptionMap = null;
                        excMap = null;
                        e.printStackTrace();

                    }
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
                            //                                if(process.equals(CommonConstants.DAY_BEGIN)){
                            sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                            //                                }else{
                            //                                    sqlMap.executeUpdate("updateTskStatus", statusMap);
                            //                                }
                            statusMap = null;
                        } else {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            //                                if(process.equals(CommonConstants.DAY_BEGIN)){
                            sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                            //                                }else{
                            //                                    sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                            //                                }
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
                            //                            if(process.equals(CommonConstants.DAY_BEGIN)){
                            sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                            //                            }else{
                            //                                sqlMap.executeUpdate("updateTskStatus", statusMap);
                            //                            }
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
                            //                            if(process.equals(CommonConstants.DAY_BEGIN)){
                            sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                            //                            }else{
                            //                                sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                            //                            }
                            statusMap = null;

                        }
                    }
                }

//                sqlMap.commitTransaction();
//                status.setStatus(BatchConstants.COMPLETED);
            }
        }
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }
//        status.setStatus(BatchConstants.COMPLETED);
        return status;
//        return status;
    }

    private void doTransfer(HashMap accountMap, double charges) throws Exception {
        System.out.println("Map = " + accountMap);
        HashMap map = new HashMap();
        map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(branch));
        map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(branch));
        map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
        map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(accountMap.get("DEBIT_HD")));
        map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(accountMap.get("CREDIT_HD")));
        map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
        map.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);

        TransferTrans trans = new TransferTrans();
        trans.setInitiatedBranch(branch);
        ArrayList batchList = new ArrayList();
        batchList.add(trans.getDebitTransferTO(map, charges));
        batchList.add(trans.getCreditTransferTO(map, charges));

        trans.doDebitCredit(batchList, CommonUtil.convertObjToStr(branch));
        batchList = null;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("DormantToInOperativeTask");
            HashMap paramMap = new HashMap();
            header.setBranchID("BRAN");
            header.setTaskParam(paramMap);
            DormantToInOperativeTask tsk = new DormantToInOperativeTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
