/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanDemandCreationTask.java
 *
 * Created on May 22, 2008, 6:45 PM
 */
package com.see.truetransact.serverside.batchprocess.task.loan;

import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class LoanDemandCreationTask extends Task {

    private static SqlMap sqlMap = null;
    private TaskStatus status = null;
    private Date currDt = null;
    private String dayEndType;
    private List branchList;
    private HashMap paramMap;
    private String branch;
    private String process = null;
    private String taskLable;

    /**
     * Creates a new instance of LoanDemandCreationTask
     */
    public LoanDemandCreationTask(TaskHeader header) throws Exception {
        setHeader(header);
        process = header.getProcessType();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        branch = header.getBranchID();
        currDt = ServerUtil.getCurrentDate(branch);
        paramMap = header.getTaskParam();
        if (paramMap != null && paramMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(paramMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            tempMap.put("NEXT_DATE", currDt);
            if (process.equals(CommonConstants.DAY_BEGIN)) {
                if (paramMap.containsKey("BRANCH_LST")) {
                    branchList = (List) paramMap.get("BRANCH_LST");
                    System.out.println("branchList*****" + branchList);
                } else {
//                    branchList=(List)sqlMap.executeQueryForList("getAllBranchesFromDayEndComp",tempMap);
                    branchList = null;
                }
            } else {
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
        if (paramMap != null && paramMap.containsKey("CHK_TL_START_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("CHK_TL_START_TASK_LABLE"));
        }
    }

    public TaskStatus executeTask() throws Exception {
//        try{
        status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap executeMap = new HashMap();
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
                //                if(process.equals(CommonConstants.DAY_BEGIN)){
                compLst = (List) sqlMap.executeQueryForList("getSelectTaskLstDayBegin", compMap);
                //                }else{
                //                    compLst = (List)sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                //                }
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
                    Date curr_dt = (Date) currDt.clone();
                    executeMap.put("CURR_DATE", curr_dt);
                    executeMap.put("BRANCH_ID", branch);
                    List accountlst = sqlMap.executeQueryForList("getDueDetails", executeMap);
                    demandTally(executeMap, accountlst);
                    curr_dt = DateUtil.addDaysProperFormat(curr_dt, 1);
                    holiyDayChecking(curr_dt, accountlst);
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
            }
        }
//        }
//        catch(Exception e){
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            status.setStatus(BatchConstants.ERROR);
//            
//        }
        status.setStatus(BatchConstants.COMPLETED);
        return status;
    }

    public void demandTally(HashMap map, List accountLst) throws Exception {
        if (accountLst != null) {
            for (int i = 0; i < accountLst.size(); i++) {
                HashMap accountMap = new HashMap();
                try {
                    accountMap = (HashMap) accountLst.get(i);
                    accountMap.put("ACT_NUM", accountMap.get("ACCT_NUM"));
                    List balanceLst = sqlMap.executeQueryForList("getIntDetails", accountMap);
                    if (balanceLst != null) {
                        HashMap balanceMap = (HashMap) balanceLst.get(0);
                        double excessAmt = CommonUtil.convertObjToDouble(balanceMap.get("EXCESS_AMT")).doubleValue();
                        if (excessAmt > 0) {
                            balanceMap.put("PROD_TYPE", "LOANS");
                            balanceMap.put("ACCOUNTNO", accountMap.get("ACT_NUM"));
                            balanceMap.put("ACT_NUM", accountMap.get("ACT_NUM"));
                            InterestCalculationTask intCalTask = new InterestCalculationTask(getHeader());
                            intCalTask.insertExcesscredit(balanceMap, balanceMap);

                        }
                    }
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
                                        HashMap errorMap = new HashMap();
                                        errorMap.put("ERROR_DATE", currDt);
                                        errorMap.put("TASK_NAME", taskLable);
                                        errorMap.put("ERROR_MSG", strExc);
                                        errorMap.put("ERROR_CLASS", errClassName);
                                        errorMap.put("ACT_NUM", accountMap.get("ACT_NUM"));
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
                                errorMap.put("ACT_NUM", accountMap.get("ACT_NUM"));
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
                        errorMap.put("ACT_NUM", accountMap.get("ACT_NUM"));
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
        }

    }

    private HashMap dateMinus(HashMap dateMap) {
        Date lastDay = (Date) dateMap.get("NEXT_DATE");
        lastDay = DateUtil.addDaysProperFormat(lastDay, -1);
        dateMap.put("NEXT_DATE", lastDay);
        dateMap.put("BRANCH_CODE", branch);
        System.out.println("dateMap#####" + dateMap);
        return dateMap;
    }

    public void holiyDayChecking(Date dt, List accountlst) {
        HashMap MonthEnd = new HashMap(); //traansferto holidaychecking method;
        HashMap chekLastDay = new HashMap();
        try {
            boolean checkHoliday = true;
            Date date = (Date) dt.clone();
            MonthEnd.put("NEXT_DATE", date);
            MonthEnd.put("BRANCH_CODE", branch);
            while (checkHoliday) {
                boolean tholiday = false;
                System.out.println("enterytothecheckholiday" + checkHoliday);
                List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", MonthEnd);
                List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", MonthEnd);
                boolean isHoliday = Holiday.size() > 0 ? true : false;
                boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                if (isHoliday || isWeekOff) {
                    demandTally(MonthEnd, accountlst);
                    MonthEnd = dateMinus(MonthEnd);
                    checkHoliday = true;
                } else {
                    checkHoliday = false;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
