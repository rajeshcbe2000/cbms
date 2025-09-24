/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * StartDayBeginTask.java
 *
 * Created on Mar 26, 2005, 4:40 PM
 */
package com.see.truetransact.serverside.batchprocess.task.authorizechk;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.see.truetransact.serverside.batchprocess.task.gl.glabstract.GlAbstractUpdateTask;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.DailyBalanceUpdateTask;

/**
 *
 * @author Bala
 */
public class StartDayBeginTask extends Task {

    private static SqlMap sqlMap = null;
    private HashMap paramMap;
    private String dayEndType;
    private boolean interBranchOnHoliday = false;
    private List branchList;
    private Date currDt = null;
    private String branch;
    private String taskLable;
    private String process = null;
    private String dayBeginPrev;

    /**
     * Creates a new instance of AuthorizationCheck
     */
    public StartDayBeginTask(TaskHeader header) throws Exception {
        setHeader(header);
        process = header.getProcessType();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        paramMap = getHeader().getTaskParam();
        if (paramMap != null && paramMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(paramMap.get("DAY_END_TYPE"));
        }
        if (paramMap != null && paramMap.containsKey("INTER_BRANCH_ON_HOLIDAY")) {
            interBranchOnHoliday = new Boolean(CommonUtil.convertObjToStr(paramMap.get("INTER_BRANCH_ON_HOLIDAY"))).booleanValue();
        }
        if (paramMap != null && paramMap.containsKey("DAY_BEGIN_PREV")) {
            dayBeginPrev = CommonUtil.convertObjToStr(paramMap.get("DAY_BEGIN_PREV"));
        }
        branch = header.getBranchID();
        currDt = (java.util.Date) paramMap.get("CURR_DATE");
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            if (process.equals(CommonConstants.DAY_BEGIN)) {
//                HashMap newMap = new HashMap();
//                List prevLst = sqlMap.executeQueryForList("getPrevDayEndDate", newMap);
//                if(prevLst!=null && prevLst.size()>0){
//                     newMap = (HashMap) prevLst.get(0);
//                     currDt = (java.util.Date)(newMap.get("PREV_DT"));
////                     currDt = ServerUtil.getCurrentDate(branch);
//                     branch = header.getBranchID();
//                }else{
//                    currDt = ServerUtil.getCurrentDate(branch);
//                }
//                newMap = null;
                if (CommonUtil.convertObjToStr(dayBeginPrev).equals("DAY_BEGIN_PREV")) {
                    if (paramMap.containsKey("BRANCH_LST")) {
                        branchList = (List) paramMap.get("BRANCH_LST");
//              if (branchList!=null && branchList.size()>0) {
//                for (int b=0;b<branchList.size();b++) {
//                    HashMap dateMap = new HashMap();
//                    dateMap = (HashMap)branchList.get(b);
//                    currDt = ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(dateMap.get("BRANCH_CODE")));
////                    presentTasks.append(CommonUtil.convertObjToStr(dateMap.get("BRANCH_CODE")));
//                }
//            }   
                    }
                } else {
                    tempMap.put("NEXT_DATE", currDt);  // Commented, because it should not check for yesterday's date
                    branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEndComp", tempMap);
                }
            } else {
                tempMap.put("NEXT_DATE", currDt);  // Commented, because it should not check for yesterday's date
                branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEndComp", tempMap);
            }
            tempMap = null;
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
        if (paramMap != null && paramMap.containsKey("CHK_CUR_DAYBEGIN_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("CHK_CUR_DAYBEGIN_TASK_LABLE"));
        }
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        TaskHeader header = getHeader();
        TaskHeader holidayHeader = new TaskHeader();
        holidayHeader.setProcessType("HOLIDAY");
        holidayHeader.setBranchID(header.getBranchID());
        DailyBalanceUpdateTask daily = new DailyBalanceUpdateTask(holidayHeader);
        status.setStatus(BatchConstants.STARTED);
        HashMap whereMap = new HashMap();
        //        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))
        //            whereMap.put("BRANCH_CODE", header.getBranchID());
        //        List lst = sqlMap.executeQueryForList("getDayEndStatus", whereMap);
        List lst = null;
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
                    if (CommonUtil.convertObjToStr(dayBeginPrev).equals("DAY_BEGIN_PREV")) {
                        currDt = ServerUtil.getCurrentDate(branch);
                    } else {
                        currDt = (java.util.Date) branchMap.get("DAYEND_DT");
                    }
                } else {
                    currDt = (java.util.Date) paramMap.get("CURR_DATE");
                }
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
                    if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
                        whereMap.put("BRANCH_CODE", getHeader().getBranchID());
                        lst = sqlMap.executeQueryForList("getDayEndStatus", whereMap);
                    } else {
                        whereMap.put("DAYEND_DT", currDt);
                        lst = sqlMap.executeQueryForList("getDayEndStatusForDC", whereMap);
                    }
                    if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
                        if (lst.size() > 0) {
                            status.setStatus(BatchConstants.ERROR);
                        } else {
                            Date AftHolDt = null;
                            try {
                                sqlMap.startTransaction();
                                //                if (branchList!=null && branchList.size()>0) {
                                //                    for (int b=0;b<branchList.size();b++) {
                                //                        HashMap branchMap = (HashMap) branchList.get(b);
                                //                        branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                daily.getHeader().setBranchID(branch);
                                if (interBranchOnHoliday) {
                                    AftHolDt = (java.util.Date) currDt.clone();
                                    AftHolDt = DateUtil.addDaysProperFormat(AftHolDt, 1);
                                } else {
                                    java.util.Date eodDt = (java.util.Date) currDt.clone();
                                    eodDt = DateUtil.addDaysProperFormat(eodDt, 1);
                                    AftHolDt = daily.holiydaychecking(eodDt);
                                }
                                //            whereMap.put("MONTH", String.valueOf(eodDt.getMonth() + 1));
                                //            whereMap.put("YEAR", String.valueOf(eodDt.getYear() + 1900));
                                //            whereMap.put("BRANCH_ID", header.getBranchID());
                                //
                                //            // Getting Holiday List
                                //            java.util.List list = sqlMap.executeQueryForList("getHolidayListForDayEnd", whereMap);
                                //            // Getting Weekly Off List
                                //            java.util.HashMap weeklyOff = (HashMap) sqlMap.executeQueryForObject("getWeeklyOffDayEnd", whereMap);
                                //
                                //            // Skipping Holidays and execute some of the holiday tasks
                                //            eodDt = DateUtil.addDays(eodDt, 1);
                                //            System.out.println("eodDt : " + eodDt);
                                //            boolean isHoliday = false;
                                //            if (list != null) {
                                //                for (int i=0, j=list.size(); i < j; i++) {
                                //                    if (list.contains(String.valueOf(eodDt.getDate()))) {
                                //                        eodDt = DateUtil.addDays(eodDt, 1);
                                //                        executeHolidayTask();
                                //                    } else {
                                //                        // Skipping Weekly off taks and execute holiday tasks
                                //                        if (weeklyOff != null) {
                                //                            String weeklyOff1 = CommonUtil.convertObjToStr(weeklyOff.get("WEEKLY_OFF1"));
                                //                            String weeklyOff2 = CommonUtil.convertObjToStr(weeklyOff.get("WEEKLY_OFF2"));
                                //
                                //                            GregorianCalendar calendar = new GregorianCalendar();
                                //                            calendar.setTime(eodDt);
                                //
                                //                            System.out.println("weeklyoff " + weeklyOff);
                                //
                                //                            if (!weeklyOff1.equals("")
                                //                                && calendar.get(calendar.DAY_OF_WEEK) == CommonUtil.convertObjToInt(weeklyOff1)) {
                                //                                eodDt = DateUtil.addDays(eodDt, 1);
                                //                                executeHolidayTask();
                                //                            }
                                //
                                //                            if (!weeklyOff2.equals("")
                                //                                && calendar.get(calendar.DAY_OF_WEEK) == CommonUtil.convertObjToInt(weeklyOff2)) {
                                //                                eodDt = DateUtil.addDays(eodDt, 1);
                                //                                executeHolidayTask();
                                //                            }
                                //                        }
                                ////                        break;
                                //                    }
                                //                }
                                //            }


                                HashMap eodMap = new HashMap();
                                eodMap.put("BRANCH_CODE", branch);
                                //                eodMap.put("CURR_DT", eodDt);
                                eodMap.put("CURR_DT", AftHolDt);
                                System.out.println("EOD DT : " + eodMap);
                                System.out.println("AftHolDt    " + AftHolDt);
                                sqlMap.executeUpdate("updateDayBegin", eodMap);
                                //                    }
                                //                }
                                sqlMap.commitTransaction();
                                status.setStatus(BatchConstants.COMPLETED);
                            } catch (Exception e) {
                                e.printStackTrace();
                                sqlMap.rollbackTransaction();
                                status.setStatus(BatchConstants.ERROR);
                            }
                        }
                    } else {
                        status.setStatus(BatchConstants.ERROR);
                        Date AftHolDt = null;
                        try {
                            sqlMap.startTransaction();
                            //                if (branchList!=null && branchList.size()>0) {
                            //                    for (int b=0;b<branchList.size();b++) {
                            //                        HashMap branchMap = (HashMap) branchList.get(b);
                            //                        branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                            daily.getHeader().setBranchID(branch);
                            if (interBranchOnHoliday) {
                                AftHolDt = (java.util.Date) currDt.clone();
                                AftHolDt = DateUtil.addDaysProperFormat(AftHolDt, 1);
                            } else {
                                java.util.Date eodDt = (java.util.Date) currDt.clone();
                                eodDt = DateUtil.addDaysProperFormat(eodDt, 1);
                                AftHolDt = daily.holiydaychecking(eodDt);
                            }
                            //            whereMap.put("MONTH", String.valueOf(eodDt.getMonth() + 1));
                            //            whereMap.put("YEAR", String.valueOf(eodDt.getYear() + 1900));
                            //            whereMap.put("BRANCH_ID", header.getBranchID());
                            //
                            //            // Getting Holiday List
                            //            java.util.List list = sqlMap.executeQueryForList("getHolidayListForDayEnd", whereMap);
                            //            // Getting Weekly Off List
                            //            java.util.HashMap weeklyOff = (HashMap) sqlMap.executeQueryForObject("getWeeklyOffDayEnd", whereMap);
                            //
                            //            // Skipping Holidays and execute some of the holiday tasks
                            //            eodDt = DateUtil.addDays(eodDt, 1);
                            //            System.out.println("eodDt : " + eodDt);
                            //            boolean isHoliday = false;
                            //            if (list != null) {
                            //                for (int i=0, j=list.size(); i < j; i++) {
                            //                    if (list.contains(String.valueOf(eodDt.getDate()))) {
                            //                        eodDt = DateUtil.addDays(eodDt, 1);
                            //                        executeHolidayTask();
                            //                    } else {
                            //                        // Skipping Weekly off taks and execute holiday tasks
                            //                        if (weeklyOff != null) {
                            //                            String weeklyOff1 = CommonUtil.convertObjToStr(weeklyOff.get("WEEKLY_OFF1"));
                            //                            String weeklyOff2 = CommonUtil.convertObjToStr(weeklyOff.get("WEEKLY_OFF2"));
                            //
                            //                            GregorianCalendar calendar = new GregorianCalendar();
                            //                            calendar.setTime(eodDt);
                            //
                            //                            System.out.println("weeklyoff " + weeklyOff);
                            //
                            //                            if (!weeklyOff1.equals("")
                            //                                && calendar.get(calendar.DAY_OF_WEEK) == CommonUtil.convertObjToInt(weeklyOff1)) {
                            //                                eodDt = DateUtil.addDays(eodDt, 1);
                            //                                executeHolidayTask();
                            //                            }
                            //
                            //                            if (!weeklyOff2.equals("")
                            //                                && calendar.get(calendar.DAY_OF_WEEK) == CommonUtil.convertObjToInt(weeklyOff2)) {
                            //                                eodDt = DateUtil.addDays(eodDt, 1);
                            //                                executeHolidayTask();
                            //                            }
                            //                        }
                            ////                        break;
                            //                    }
                            //                }
                            //            }


                            HashMap eodMap = new HashMap();
                            eodMap.put("BRANCH_CODE", branch);
                            //                eodMap.put("CURR_DT", eodDt);
                            eodMap.put("CURR_DT", AftHolDt);
                            System.out.println("EOD DT : " + eodMap);
                            System.out.println("AftHolDt    " + AftHolDt);
                            sqlMap.executeUpdate("updateDayBegin", eodMap);
                            //                    }
                            //                }
                            sqlMap.commitTransaction();
                            status.setStatus(BatchConstants.COMPLETED);
                        } catch (Exception e) {
                            e.printStackTrace();
                            sqlMap.rollbackTransaction();
                            status.setStatus(BatchConstants.ERROR);
                        }
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
        return status;
    }

    /*
     * Used to execute the Holiday Task
     */
    private void executeHolidayTask() throws Exception {
//        TaskHeader header = getHeader();
//        header.setProductType("OA");
//        header.setTaskClass("DailyBalanceUpdateTask");
//        DailyBalanceUpdateTask objTask = new DailyBalanceUpdateTask (header);
//        objTask.executeTask();
//        
//        header.setTaskClass("GlAbstractUpdateTask");
//        header.setProcessType(CommonConstants.DAY_BEGIN);
//        GlAbstractUpdateTask objGLTask = new GlAbstractUpdateTask (header);
//        objGLTask.executeTask();
//        
//        header.setProcessType(CommonConstants.DAY_END);
//        objGLTask = new GlAbstractUpdateTask (header);
//        objGLTask.executeTask();
    }

    public static void main(String args[]) {
        try {
            TaskHeader hd = new TaskHeader();
            hd.setBranchID("ABC50001");
            HashMap map = new HashMap();
            map.put("CURR_DATE", ServerUtil.getCurrentDate("ABC50001"));
            hd.setTaskParam(map);
            System.out.println("map : " + map);
            StartDayBeginTask ft = new StartDayBeginTask(hd);
            System.out.println(ft.executeTask().getStatus());
            System.out.println("BatchConstants.ERROR :" + ServerUtil.getCurrentDate("ABC50001"));
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
