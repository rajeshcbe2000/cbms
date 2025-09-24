/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TodTask.java
 *
 * Created on March 8, 2005, 4:38 PM
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
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
import com.see.truetransact.serverside.deposit.closing.DepositClosingDAO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.deposit.DepSubNoAccInfoTO;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 *
 * @author Swaroop
 */
public class TodTask extends Task {

    private static SqlMap sqlMap = null;
    private String BRANCH_ID = "BRAN";
    private String ACT_NUM = "";
    private String USER_ID = "";
    private String SCREEN = "TD";
    private InterestBatchTO interestBatchTO = null;
    private Date checkThisCDate = null;
    private Date holidayCDate = null;
    private Date currDt = null;
    private String taskLable;
    private HashMap taskMap;
    private String dayEndType;
    private List branchList;

    /**
     * Creates a new instance of TodTask
     */
    public TodTask(TaskHeader header) throws Exception {
        setHeader(header);
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        BRANCH_ID = CommonUtil.convertObjToStr(header.getBranchID());
        USER_ID = CommonUtil.convertObjToStr(header.getUserID());
        taskMap = header.getTaskParam();
        currDt = ServerUtil.getCurrentDate(BRANCH_ID);
        System.out.println("currDt" + currDt);
        if (taskMap != null && taskMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(taskMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            if (taskMap.containsKey("BRANCH_LST")) {
                branchList = (List) taskMap.get("BRANCH_LST");
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
        if (taskMap != null && taskMap.containsKey("EXEC_TOD_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(taskMap.get("EXEC_TOD_TASK_LABLE"));
        }

    }

    //__ To be Called for the Batch Process...
    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        //        try{
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                BRANCH_ID = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                currDt = ServerUtil.getCurrentDate(BRANCH_ID);
                HashMap compMap = new HashMap();
                compMap.put("TASK_NAME", taskLable);
                compMap.put("DAYEND_DT", currDt);
                compMap.put("BRANCH_ID", BRANCH_ID);
                List compLst = (List) sqlMap.executeQueryForList("getSelectTaskLstDayBegin", compMap);
                compMap = null;
                String compStatus = "";
                if (compLst != null && compLst.size() > 0) {
                    compMap = (HashMap) compLst.get(0);
                    compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                    compMap = null;
                }
                if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                    try {
                        HashMap errorMap = new HashMap();
//                                if(!paramMap.containsKey("CHARGES_PROCESS")){
                        sqlMap.startTransaction();
                        errorMap.put("ERROR_DATE", currDt);
                        errorMap.put("TASK_NAME", taskLable);
                        errorMap.put("BRANCH_ID", BRANCH_ID);
                        sqlMap.executeUpdate("deleteError_showing", errorMap);
                        sqlMap.commitTransaction();
//                                }
                        holidayCDate = ServerUtil.getCurrentDateProperFormat(BRANCH_ID);
                        System.out.println("holidayCDate" + holidayCDate);
                        implementTask(currDt, holidayCDate);
                        int count = holidayProvision(holidayCDate, BRANCH_ID, USER_ID);
                        System.out.println("count" + count);
                        while (count > 0) {
                            holidayCDate = DateUtil.addDays(holidayCDate, -1);
                            implementTask(currDt, holidayCDate);
                            count--;
                        }

                    } catch (Exception e) {
//                        e.printStackTrace();
//                        status.setStatus(BatchConstants.ERROR);
//                        throw new TransRollbackException(e);
//                         if(!paramMap.containsKey("CHARGES_PROCESS")){
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
                                            HashMap errorMap = new HashMap();
                                            errorMap.put("ERROR_DATE", currDt);
                                            errorMap.put("TASK_NAME", taskLable);
                                            errorMap.put("ERROR_MSG", strExc);
                                            errorMap.put("ERROR_CLASS", errClassName);
                                            errorMap.put("ACT_NUM", "");
                                            errorMap.put("BRANCH_ID", BRANCH_ID);
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
                                    errorMap.put("ACT_NUM", "");
                                    errorMap.put("BRANCH_ID", BRANCH_ID);
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
                            errorMap.put("ACT_NUM", "");
                            errorMap.put("BRANCH_ID", BRANCH_ID);
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
//                            }else{
//                                sqlMap.rollbackTransaction();
//                                e.printStackTrace();
////                                e.printStackTrace();
//                            }
                    }
                }
//                 if(!paramMap.containsKey("CHARGES_PROCESS")){
                if (!compStatus.equals("COMPLETED")) {
                    if (status.getStatus() != BatchConstants.ERROR) {
                        sqlMap.startTransaction();
                        if (compStatus.equals("ERROR")) {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", BRANCH_ID);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                            statusMap = null;
                        } else {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", BRANCH_ID);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                            statusMap = null;
                        }
                        sqlMap.commitTransaction();
                    } else {
                        if (compStatus.equals("ERROR")) {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", BRANCH_ID);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "ERROR");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                            statusMap = null;
                        } else {
                            //                            isError = true;
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", BRANCH_ID);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "ERROR");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                            statusMap = null;

                        }
                    }
                }
//                }
            }
        }
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }
        return status;
    }

    //__ Common method to update the available balance in act_master with tod_amount...
    private void implementTask(Date currDt, Date holidayCDate) throws Exception {
        System.out.println("@@INSIDE");
        TaskStatus status = new TaskStatus();
        HashMap dataMap = new HashMap();
        dataMap.put("CURR_DT", currDt);
        dataMap.put("HOLIDAY_DT", holidayCDate);
        dataMap.put("TODAY_DT", currDt);
        System.out.println("@@dataMap" + dataMap);
        ArrayList acctList = (ArrayList) getActList(dataMap);
        System.out.println("acctList: " + acctList.size());
        for (int i = 0; i < acctList.size(); i++) {//__ If the List Contains some Data...
            try {
                HashMap map = (HashMap) acctList.get(i);
                String prod_type = CommonUtil.convertObjToStr(map.get("PROD_TYPE"));
                HashMap map1 = new HashMap();
                double td = CommonUtil.convertObjToDouble(map.get("TOD_AMOUNT")).doubleValue();
                java.math.BigDecimal b = new java.math.BigDecimal(td);
                //map1.put("AMOUNT", String.valueOf(td));
                map1.put("AMOUNT", td);
                map1.put("ACT_NUM", map.get("ACT_NUM"));
                dataMap.put("ACT_NUM", map.get("ACT_NUM"));
                if (prod_type.equals("OA")) {
                    sqlMap.executeUpdate("Tod.updateAvailBalance", map1);
                } else {
                    sqlMap.executeUpdate("Tod.updateAvailBalanceAD", map1);
                }

                List lst = sqlMap.executeQueryForList("getTODUtilizedDetails", dataMap);
                if (lst != null && lst.size() > 0) {
                    HashMap where = new HashMap();
                    where = (HashMap) lst.get(0);
                    System.out.println("Inside List" + where);
                    String tod_util = CommonUtil.convertObjToStr(where.get("TOD_UTILIZED"));
                    if (!tod_util.equals("")) {
                        double tod_utilized = CommonUtil.convertObjToDouble(where.get("TOD_UTILIZED")).doubleValue();
                        dataMap.put("TOD_UTILIZED", new Double(tod_utilized));
                        dataMap.put("TOD", "");
                        System.out.println("Inside IF" + dataMap);
                        sqlMap.executeUpdate("updateTODUtilizedWhileAuth", dataMap);
                    }
                }
                List cbList = new ArrayList();
                HashMap cbMap = new HashMap();
                if (prod_type.equals("OA")) {
                    dataMap.put("OA_CB", "");
                    cbList = sqlMap.executeQueryForList("getCBForTOD", dataMap);
                } else {
                    dataMap.put("AD_CB", "");
                    cbList = sqlMap.executeQueryForList("getCBForTOD", dataMap);
                }
                cbMap = (HashMap) cbList.get(0);
                map.put("CLEAR_BALANCE", cbMap.get("CLEAR_BALANCE"));
                sqlMap.executeUpdate("updateCBForTODDayBegin", map);
            } catch (Exception e) {
//                               e.printStackTrace();
//                              status.setStatus(BatchConstants.ERROR);
//                              throw new TransRollbackException(e);
                sqlMap.rollbackTransaction();
//                if (taskSelected != OBCODE) {
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
                                    HashMap errorMap = new HashMap();
                                    errorMap.put("ERROR_DATE", currDt);
                                    errorMap.put("TASK_NAME", taskLable);
                                    errorMap.put("ERROR_MSG", strExc);
                                    errorMap.put("ERROR_CLASS", errClassName);
                                    errorMap.put("ACT_NUM", "");
                                    errorMap.put("BRANCH_ID", BRANCH_ID);
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
                            errorMap.put("ACT_NUM", "");
                            errorMap.put("BRANCH_ID", BRANCH_ID);
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
                    errorMap.put("ACT_NUM", "");
                    errorMap.put("BRANCH_ID", BRANCH_ID);
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

//                if (!isExceptionCatch) {
//                    //                    throw new TransRollbackException(e);
//                    throw new TTException(e);
//                } else {
//                    status.setStatus(BatchConstants.ERROR);
//                }
            }
        }
        acctList = null;
    }

    //__ To be Called from the Code...
    private List getActList(HashMap dataMap) throws Exception {
        List list = sqlMap.executeQueryForList("getTodDetailsForDayBegin", dataMap);
        return list;
    }

    private int holidayProvision(Date matDt, String BRANCH_ID, String USER_ID) throws Exception {
        int count = 0;
        HashMap weeklyOff = new HashMap();
        HashMap holidayMap = new HashMap();
        weeklyOff.put("NEXT_DATE", DateUtil.addDays(matDt, -1));
        weeklyOff.put("BRANCH_CODE", BRANCH_ID);
        weeklyOff.put("CURR_DATE", matDt);
        List lst = sqlMap.executeQueryForList("checkHolidayProvisionTD", weeklyOff);
        if (lst != null && lst.size() > 0) {
            for (int j = 0; j < lst.size(); j++) {
                count = count + 1;
            }
            holidayMap.put("NEXT_DATE", matDt);
            holidayMap.put("BRANCH_CODE", BRANCH_ID);
            sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
            if (lst != null && lst.size() > 0) {
                count = count + 1;
            }
        } else {
            lst = sqlMap.executeQueryForList("checkWeeklyOffOD", weeklyOff);
            if (lst != null && lst.size() > 0) {
                count = count + 1;
            }
            lst = sqlMap.executeQueryForList("checkHolidayProvisionTD", weeklyOff);
            if (lst != null && lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    count = count + 1;
                }
            }
        }
        return count;
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

            TodTask tsk = new TodTask(header);
            TaskStatus Status = tsk.executeTask();
            System.out.println("Status: " + Status);

            //            tsk.TodTask("OA060897");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
