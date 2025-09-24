/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GlAbstractUpdateTask.java
 *
 * Created on February 28, 2005, 12:23 PM
 */
package com.see.truetransact.serverside.batchprocess.task.gl.glabstract;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.TTException;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import com.see.truetransact.transferobject.batchprocess.glpackage.GlAbstractTO;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.DailyBalanceUpdateTask;

/**
 *
 * @author 152691
 * @modified Bala This class inserts into the GL_ABSTRACT table with the balance
 * as on day end. Balance is retrieved from the GL Table Is to be called as a
 * part of the Day Begin and Day End batch process
 */
public class GlAbstractUpdateTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    private String process = null;
    private HashMap paramMap = null;
    private Date checkThisCDate = null;
    private Date holidayCDate = null;
    private String dayEndType;
    private Date currDt;
    private String taskLable;
    private List branchList;
    private boolean isHoliday = false;

    /**
     * Creates a new instance of GlAbstractUpdateTask
     */
    public GlAbstractUpdateTask(TaskHeader header) throws Exception {
        setHeader(header);
        process = header.getProcessType();
        branch = header.getBranchID();
        currDt = ServerUtil.getCurrentDate(branch);
        initializeTaskObj(header.getTaskParam());
        //        branch = header.getBranchID();
        //        currDt = ServerUtil.getCurrentDate(branch);
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        paramMap = dataMap;
        if (paramMap != null && paramMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(paramMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            if (paramMap.containsKey("BRANCH_LST")) {
                branchList = (List) paramMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
                HashMap tempMap = new HashMap();
                tempMap.put("NEXT_DATE", currDt);
                if (process.equals(CommonConstants.DAY_BEGIN)) {
                    branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEndComp", tempMap);
                } else {
                    branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEnd", tempMap);
                }
                tempMap = null;
            }
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
        if (paramMap != null && paramMap.containsKey("GL_ABS_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("GL_ABS_TASK_LABLE"));
        }
        if (paramMap != null && paramMap.containsKey("CREATE_GL_ABS_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("CREATE_GL_ABS_TASK_LABLE"));
        }

    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        System.out.println(status.getStatus());
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
                if (process.equals(CommonConstants.DAY_BEGIN)) {
                    compLst = (List) sqlMap.executeQueryForList("getSelectTaskLstDayBegin", compMap);
                } else {
                    compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                }
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
                    try {
                        sqlMap.startTransaction();
                        holidayCDate = currDt;
                        System.out.println("holidayCDate" + holidayCDate);
                        updateGLAbstract();
                        if (process.equals(CommonConstants.DAY_BEGIN)) {
                            holidayCDate = DateUtil.addDays(holidayCDate, -1);
                            holiydaychecking(holidayCDate);
                        }
                        sqlMap.commitTransaction();
                        status.setStatus(BatchConstants.COMPLETED);
                    } catch (Exception e) {
                        //                    sqlMap.rollbackTransaction();
                        //                    e.printStackTrace();
                        //                    throw new TransRollbackException(e);
                        sqlMap.rollbackTransaction();
                        //                              status.setStatus(BatchConstants.ERROR) ;
                        //                              errorMap = new HashMap();
                        //                              errorMap.put("ERROR_DATE",currDt);
                        //                              errorMap.put("TASK_NAME", taskLable);
                        //                              errorMap.put("ERROR_MSG",e.getMessage());
                        //                              errorMap.put("BRANCH_ID", branch);
                        //                              errorMap.put("ACT_NUM","GL");
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
                            errorMap.put("ACT_NUM", "GL");
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
                            if (process.equals(CommonConstants.DAY_BEGIN)) {
                                sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                            } else {
                                sqlMap.executeUpdate("updateTskStatus", statusMap);
                            }
                            statusMap = null;
                        } else {
                            HashMap statusMap = new HashMap();
                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                            statusMap.put("BRANCH_CODE", branch);
                            statusMap.put("TASK_NAME", taskLable);
                            statusMap.put("TASK_STATUS", "COMPLETED");
                            statusMap.put("USER_ID", getHeader().getUserID());
                            statusMap.put("DAYEND_DT", currDt);
                            if (process.equals(CommonConstants.DAY_BEGIN)) {
                                sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                            } else {
                                sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                            }
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
                            if (process.equals(CommonConstants.DAY_BEGIN)) {
                                sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                            } else {
                                sqlMap.executeUpdate("updateTskStatus", statusMap);
                            }
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
                            if (process.equals(CommonConstants.DAY_BEGIN)) {
                                sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                            } else {
                                sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                            }
                            statusMap = null;

                        }
                    }
                }
            }
        }
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }

        System.out.println(status.getStatus());
        return status;
    }

    private void updateGLAbstract() throws Exception {
        //        GlAbstractTO inputGl = new GlAbstractTO();
        GlAbstractTO outputGl = new GlAbstractTO();
        //        inputGl.setDt(ServerUtil.getCurrentDate(super._branchCode));
        //        inputGl.setBranchCode(branch);
        HashMap inputGl = new HashMap();
        inputGl.put("CURR_DT", holidayCDate);
        //        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))
        inputGl.put(CommonConstants.BRANCH_ID, branch);
        List glDataList = null;

            outputGl.setDt(holidayCDate);
            outputGl.setBranchCode(branch);

            // Day Begin Insertion
            if (process.equals(CommonConstants.DAY_BEGIN)) {
                if (isHoliday) {
                    outputGl.setCloseBal(outputGl.getOpnBal());
//                    HashMap dataMap = new HashMap();
//                    dataMap.put("DAY_END_DATE", holidayCDate);
//                    runHolDailyBalance(dataMap);
                }
                sqlMap.executeUpdate("insertGlAbstractDB", outputGl);
            } else { // Day End
                    /* In Day end, if the opening balance is not null then,
                 * it is a new entry which doesn't have day begin balance
                 * (Check selectGLDayEnd query)
                 */
                sqlMap.executeUpdate("insertGlAbstract", outputGl);
            }
        if (process.equals(CommonConstants.DAY_BEGIN)) {
            if (isHoliday) {
                HashMap dataMap = new HashMap();
                dataMap.put("DAY_END_DATE", holidayCDate);
                runHolDailyBalance(dataMap);
            }
        }
        inputGl = null;
        outputGl = null;
        //        }
        //        }
    }

    private void runHolDailyBalance(HashMap holidayCDate) throws Exception {
        TaskHeader tskHeader = new TaskHeader();
        tskHeader.setBranchID(branch);
        tskHeader.setUserID(getHeader().getUserID());
        tskHeader.setIpAddr(getHeader().getIpAddr());
//        tskHeader.setProductType("OA");
        tskHeader.setTaskParam(getHeader().getTaskParam());
        DailyBalanceUpdateTask daily = new DailyBalanceUpdateTask(tskHeader);
        daily.doAllHolProductsUpdate(holidayCDate);
        tskHeader = null;
    }

    public Date holiydaychecking(Date lstintCr) {
        System.out.println("inside HolidyChecking");
        try {
            HashMap MonthEnd = new HashMap(); //traansferto holidaychecking method;
            boolean checkHoliday = true;
            System.out.println("lstintCr   " + lstintCr);
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
                    String processType = "";
                    if (getHeader().getProcessType() != null && getHeader().getProcessType().equals("HOLIDAY")) {
                        processType = getHeader().getProcessType();
                    }
                    this.isHoliday = true;
                    if (processType.equals("")) {
                        updateGLAbstract();
                    }
                    MonthEnd = dateAdd(MonthEnd);
                    checkHoliday = true;
                } else {
                    checkHoliday = false;
                    this.isHoliday = false;
                }
            }
            return (Date) MonthEnd.get("NEXT_DATE");
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    private HashMap dateAdd(HashMap dateMap) {
        //        String day=CommonUtil.convertObjToStr(dateMap.get("NEXT_DATE"));
        //        holidayCDate=DateUtil.getDateMMDDYYYY(day);
        holidayCDate = (Date) dateMap.get("NEXT_DATE");
        System.out.println("checkThisCDate" + holidayCDate);
        holidayCDate = DateUtil.addDaysProperFormat(holidayCDate, -1);
        System.out.println("checkThisCDate" + checkThisCDate);

        dateMap.put("NEXT_DATE", holidayCDate);
        dateMap.put("BRANCH_CODE", getHeader().getBranchID());
        return dateMap;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("GlAbstractUpdateTask");
            HashMap paramMap = new HashMap();

            //            header.setProcessType(CommonConstants.DAY_BEGIN);
            header.setProcessType(CommonConstants.DAY_END);
            header.setBranchID("Bran");
            header.setTaskParam(paramMap);
            GlAbstractUpdateTask tsk = new GlAbstractUpdateTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
