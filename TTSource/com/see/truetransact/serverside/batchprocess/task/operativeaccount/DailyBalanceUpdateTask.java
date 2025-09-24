/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DailyBalanceUpdateTask.java
 *
 * Created on February 28, 2005, 12:23 PM
 */
package com.see.truetransact.serverside.batchprocess.task.operativeaccount;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

/**
 *
 * @author 152691 This class inserts into the ACT_DAYEND_BALANCE table with the
 * balance as on day end. Balance is retrieved from the
 * ACT_MASTER.AVAILABLE_BALANCE Is to be called as a part of the Day end batch
 * process
 */
public class DailyBalanceUpdateTask extends Task {

    private static SqlMap sqlMap = null;
    private String productType;
    private Date checkThisCDate = null;
    private Date holidayCDate = null;
    private HashMap loanInterestMap = null;
    private String dayEndType;
    private String taskLable;
    private List branchList;
    private Date curDate = null;
    private String branch = null;
    private String tod_updation = "";

    /**
     * Creates a new instance of DailyBalanceUpdateTask
     */
    public DailyBalanceUpdateTask(TaskHeader header) throws Exception {
        setHeader(header);
        productType = header.getProductType();
        curDate = ServerUtil.getCurrentDate(header.getBranchID());
        branch = header.getBranchID();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        if (header.getTaskParam() != null && header.getTaskParam().size() > 0) {
            loanInterestMap = new HashMap();
            loanInterestMap.putAll(header.getTaskParam());
         //   System.out.println("loanInterestMap**DDB***" + loanInterestMap);
            if (loanInterestMap != null && loanInterestMap.containsKey("DAY_END_TYPE")) {
                dayEndType = CommonUtil.convertObjToStr(loanInterestMap.get("DAY_END_TYPE"));
            }
            if (loanInterestMap != null && loanInterestMap.containsKey("TOD_UPDATION")) {
                tod_updation = CommonUtil.convertObjToStr(loanInterestMap.get("TOD_UPDATION"));
            }
            if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
                if (loanInterestMap.containsKey("BRANCH_LST")) {
                    branchList = (List) loanInterestMap.get("BRANCH_LST");
                  //  System.out.println("branchList*****" + branchList);
                } else {
                    HashMap tempMap = new HashMap();
                    tempMap.put("NEXT_DATE", curDate);
                    branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEnd", tempMap);
                    tempMap = null;
                }
            } else {

                HashMap tempMap = new HashMap();
                tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
                branchList = new ArrayList();
                branchList.add(tempMap);
               // System.out.println("branchList**DDB***" + branchList);
                tempMap = null;
            }
            if (loanInterestMap != null && loanInterestMap.containsKey("DAILY_BAL_UPDATE_TASK_LABLE")) {
                taskLable = CommonUtil.convertObjToStr(loanInterestMap.get("DAILY_BAL_UPDATE_TASK_LABLE"));
            }

        }
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
//        try{
        status.setStatus(BatchConstants.STARTED);
  //      System.out.println("taskLable**DDB***" + taskLable);
        if (!CommonUtil.convertObjToStr(taskLable).equals("")) {
            if (branchList != null && branchList.size() > 0) {
                for (int b = 0; b < branchList.size(); b++) {
                    HashMap branchMap = (HashMap) branchList.get(b);
              //      System.out.println("branchMap*****" + branchMap);
                    branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                    curDate = ServerUtil.getCurrentDate(branch);
                  //  System.out.println("branch*****" + branch);
                    HashMap compMap = new HashMap();
                    compMap.put("TASK_NAME", taskLable);
                    compMap.put("DAYEND_DT", curDate);
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
                        sqlMap.startTransaction();
                        HashMap errorMap = new HashMap();
                        errorMap.put("ERROR_DATE", curDate);
                        errorMap.put("TASK_NAME", taskLable);
                        errorMap.put("BRANCH_ID", branch);
                        sqlMap.executeUpdate("deleteError_showing", errorMap);
                        sqlMap.commitTransaction();
                        try {
                            sqlMap.startTransaction();
                        //    System.out.println("Before   Daily");
                           // System.out.println("productType&***8Daily" + productType);
                            if (productType != null && productType.length() > 0) {
                                holidayCDate = ServerUtil.getCurrentDateProperFormat(branch);
                             //   System.out.println("holidayCDate" + holidayCDate);
                                updateDailyBalance();
                                if (productType.equals("TL") || productType.equals("AD") || productType.equals("AAD")) {
                                 //   System.out.println("Before HolidayChecking  Daily");
                                    holidayCDate = DateUtil.addDaysProperFormat(holidayCDate, 1);
                                //    System.out.println("holidayCDate+   " + holidayCDate);
                                //    System.out.println("Befor HolidayChecking Completed Daily");
                                    holiydaychecking(holidayCDate);
                                   // System.out.println("After HolidayChecking Completed Daily");
                                }
                            } else {
                                doAllProductsUpdate();
                            }
                            sqlMap.commitTransaction();
                            status.setStatus(BatchConstants.COMPLETED);
                        } catch (Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw new TransRollbackException(e);
                            sqlMap.rollbackTransaction();
//                              status.setStatus(BatchConstants.ERROR) ;
//                              errorMap = new HashMap();
//                              errorMap.put("ERROR_DATE",curDate);
//                              errorMap.put("TASK_NAME", taskLable);
//                              errorMap.put("ERROR_MSG",e.getMessage());
//                              errorMap.put("BRANCH_ID", branch);
//                              errorMap.put("ACT_NUM","bal");
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
                                                errorMap.put("ERROR_DATE", curDate);
                                                errorMap.put("TASK_NAME", taskLable);
                                                errorMap.put("ERROR_MSG", strExc);
                                                errorMap.put("ERROR_CLASS", errClassName);
                                                errorMap.put("ACT_NUM", "bal");
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
                                        errorMap.put("ERROR_DATE", curDate);
                                        errorMap.put("TASK_NAME", taskLable);
                                        errorMap.put("ERROR_MSG", errMsg);
                                        errorMap.put("ERROR_CLASS", errClassName);
                                        errorMap.put("ACT_NUM", "bal");
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
                                errorMap.put("ERROR_DATE", curDate);
                                errorMap.put("TASK_NAME", taskLable);
                                errorMap.put("ERROR_MSG", errMsg);
                                errorMap.put("ERROR_CLASS", errClassName);
                                errorMap.put("ACT_NUM", "bal");
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
                                statusMap.put("DAYEND_DT", curDate);
                                sqlMap.executeUpdate("updateTskStatus", statusMap);
                                statusMap = null;
                            } else {
                                HashMap statusMap = new HashMap();
                                //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                statusMap.put("BRANCH_CODE", branch);
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
                                statusMap.put("BRANCH_CODE", branch);
                                statusMap.put("TASK_NAME", taskLable);
                                statusMap.put("TASK_STATUS", "ERROR");
                                statusMap.put("USER_ID", getHeader().getUserID());
                                statusMap.put("DAYEND_DT", curDate);
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
                                statusMap.put("DAYEND_DT", curDate);
                                sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                                statusMap = null;

                            }
                        }
                    }
                }
            }
        } else {
            if (branchList != null && branchList.size() > 0) {
                for (int b = 0; b < branchList.size(); b++) {
                    HashMap branchMap = (HashMap) branchList.get(b);
                 //   System.out.println("branchMap*****" + branchMap);
                    branch = CommonUtil.convertObjToStr(branchMap.get("BRANCH_CODE"));
                 //   System.out.println("branch*****" + branch);
//                HashMap compMap = new HashMap();
//                compMap.put("TASK_NAME", taskLable);
//                compMap.put("DAYEND_DT", curDate);
//                compMap.put("BRANCH_ID", branch);
//                List compLst = (List)sqlMap.executeQueryForList("getSelectTaskLst", compMap);
//                compMap = null;
//                String compStatus = "";
//                if(compLst != null && compLst.size() > 0){
//                    compMap = (HashMap)compLst.get(0);
//                    compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
//                    compMap = null;
//                }
//               if(compLst.size() <= 0 || compStatus.equals("ERROR")){
//                    sqlMap.startTransaction();
//                        HashMap errorMap = new HashMap();
//                        errorMap.put("ERROR_DATE",curDate);
//                        errorMap.put("TASK_NAME",taskLable);
//                        errorMap.put("BRANCH_ID",branch);
//                        sqlMap.executeUpdate("deleteError_showing", errorMap);                
//                        sqlMap.commitTransaction();
                    try {
                        sqlMap.startTransaction();
                      //  System.out.println("Before ily");
                       // System.out.println("productType&***8" + productType);
                        if (productType != null && productType.length() > 0) {
                            holidayCDate = ServerUtil.getCurrentDateProperFormat(branch);
                           // System.out.println("holidayCDate" + holidayCDate);
                            updateDailyBalance();
//                if (productType.equals("TL")||productType.equals("AD") ||productType.equals("AAD")) {
                         //   System.out.println("Before HolidayChecking  Daily");
                            holidayCDate = DateUtil.addDaysProperFormat(holidayCDate, 1);
                           // System.out.println("holidayCDate+   " + holidayCDate);
                           // System.out.println("Befor HolidayChecking Completed Daily");
                            holiydaychecking(holidayCDate);
                         //   System.out.println("After HolidayChecking Completed Daily");
//                }
                        } else {
                            doAllProductsUpdate();
                        }
                        sqlMap.commitTransaction();
                        status.setStatus(BatchConstants.COMPLETED);
                    } catch (Exception e) {
                        sqlMap.rollbackTransaction();
                        e.printStackTrace();
                        throw new TransRollbackException(e);
//            sqlMap.rollbackTransaction();
//                              status.setStatus(BatchConstants.ERROR) ;
//                              errorMap = new HashMap();
//                              errorMap.put("ERROR_DATE",curDate);
//                              errorMap.put("TASK_NAME", taskLable);
//                              errorMap.put("ERROR_MSG",e.getMessage());
//                              errorMap.put("BRANCH_ID", branch);
//                              errorMap.put("ACT_NUM","bal");
//                              sqlMap.startTransaction();
//                              sqlMap.executeUpdate("insertError_showing", errorMap);
//                              sqlMap.commitTransaction();
////                              System.out.println("Error thrown for Operative A/c No " + dataMap.get("ACT_NUM"));
//                              e.printStackTrace();

                    }
//    }
//     if(!compStatus.equals("COMPLETED")){
//                 if (status.getStatus() != BatchConstants.ERROR){
//                            sqlMap.startTransaction();
//                            if(compStatus.equals("ERROR")){
//                                HashMap statusMap = new HashMap();
//                                //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
//                                statusMap.put("BRANCH_CODE", branch);
//                                statusMap.put("TASK_NAME", taskLable);
//                                statusMap.put("TASK_STATUS", "COMPLETED");
//                                statusMap.put("USER_ID", getHeader().getUserID());
//                                statusMap.put("DAYEND_DT", curDate);
//                                sqlMap.executeUpdate("updateTskStatus", statusMap);
//                                statusMap = null;
//                            }else{
//                                HashMap statusMap = new HashMap();
//                                //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
//                                statusMap.put("BRANCH_CODE", branch);
//                                statusMap.put("TASK_NAME", taskLable);
//                                statusMap.put("TASK_STATUS", "COMPLETED");
//                                statusMap.put("USER_ID", getHeader().getUserID());
//                                statusMap.put("DAYEND_DT", curDate);
//                                sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
//                                statusMap = null;
//                            }
//                            sqlMap.commitTransaction();
//                        }else{
//                            if(compStatus.equals("ERROR")){
//                            HashMap statusMap = new HashMap();
//                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
//                            statusMap.put("BRANCH_CODE", branch);
//                            statusMap.put("TASK_NAME", taskLable);
//                            statusMap.put("TASK_STATUS", "ERROR");
//                            statusMap.put("USER_ID", getHeader().getUserID());
//                            statusMap.put("DAYEND_DT", curDate);
//                            sqlMap.executeUpdate("updateTskStatus", statusMap);
//                            statusMap = null;
//                        }else{
////                            isError = true;
//                            HashMap statusMap = new HashMap();
//                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
//                            statusMap.put("BRANCH_CODE", branch);
//                            statusMap.put("TASK_NAME", taskLable);
//                            statusMap.put("TASK_STATUS", "ERROR");
//                            statusMap.put("USER_ID", getHeader().getUserID());
//                            statusMap.put("DAYEND_DT", curDate);
//                            sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
//                            statusMap = null;
//                            
//                        }
//                        }
//            }           
                }
            }
        }
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }
        return status;
    }

    private void doAllProductsUpdate() throws Exception {
        holidayCDate = ServerUtil.getCurrentDateProperFormat(branch);
        updateDailyBalance();
        holidayCDate=DateUtil.addDaysProperFormat(holidayCDate, 1);
        holiydaychecking(holidayCDate);
        productType = "";
    }

    public void doAllHolProductsUpdate(HashMap holCDate) throws Exception {
        String prodList[] = {"OA", "AD", "TL", "TD", "BILLS", "ATL", "AAD", "SA"};
        for (int i = 0; i < prodList.length; i++) {
            productType = prodList[i];
            System.out.println("DDDDDDD" + holCDate);
            holidayCDate = (Date) holCDate.get("DAY_END_DATE");
            //updateDailyBalance();
//            holidayCDate=DateUtil.addDaysProperFormat(holidayCDate, 1);
//            holiydaychecking(holidayCDate);
        }
        updateDailyBalance(); // Added ouside the loop on 11-Feb-2025 [KD-3913]since, slowness reported in Maneed - Ref By Prasanth,Giby & Seby
        productType = "";
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("DailyBalanceUpdateTask");
            header.setProductType("AD"); //Can be OA / AD
            DailyBalanceUpdateTask tsk = new DailyBalanceUpdateTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Date holiydaychecking(Date lstintCr) {
      //  System.out.println("inside HolidyChecking");
        try {
            HashMap MonthEnd = new HashMap(); //traansferto holidaychecking method;
            boolean checkHoliday = true;
          //  System.out.println("lstintCr   " + lstintCr);
            MonthEnd.put("NEXT_DATE", lstintCr);
            MonthEnd.put("BRANCH_CODE", getHeader().getBranchID());
            //                try{
            //            sqlMap.startTransaction();
            lstintCr = lstintCr;
            while (checkHoliday) {
                boolean tholiday = false;
                //System.out.println("enterytothecheckholiday" + checkHoliday);
                List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", MonthEnd);
                List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", MonthEnd);
                boolean isHoliday = Holiday.size() > 0 ? true : false;
                boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                if (isHoliday || isWeekOff) {
                    String processType = "";
                    if (getHeader().getProcessType() != null && getHeader().getProcessType().equals("HOLIDAY")) {
                        processType = getHeader().getProcessType();
                    }
                    if (processType.equals("")) {
                        updateDailyBalance();
                    }
                    MonthEnd = dateAdd(MonthEnd);
                    checkHoliday = true;
                } else {
                    checkHoliday = false;


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
       // System.out.println("checkThisCDate" + holidayCDate);
        holidayCDate = DateUtil.addDaysProperFormat(holidayCDate, 1);
     //   System.out.println("checkThisCDate" + checkThisCDate);

        dateMap.put("NEXT_DATE", holidayCDate);
        dateMap.put("BRANCH_CODE", getHeader().getBranchID());
        return dateMap;
    }

    private void updateDailyBalance() throws Exception {
        HashMap map = new HashMap();
        if (loanInterestMap != null && loanInterestMap.size() > 0) {
            map.putAll(loanInterestMap);
        }
        //        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))
        map.put(CommonConstants.BRANCH_ID, branch);
        Date dayEndDt = (Date) holidayCDate;
        map.put("DAY_END_DT", dayEndDt);
        List dayEndList = sqlMap.executeQueryForList("getSelectinsertDailyBalance",map);
        if (dayEndList != null && dayEndList.size() > 0) {
            HashMap dayendMap = (HashMap) dayEndList.get(0);
            System.out.println("Dayend Status : " + dayendMap.get("DAYEND_STATUS"));
        }       
    }

    private void updateDailyBalanceForCorpLoan() throws Exception {
        HashMap map = new HashMap();
        if (loanInterestMap != null && loanInterestMap.size() > 0) {
            map.putAll(loanInterestMap);
        }
        map.put(CommonConstants.BRANCH_ID, branch);
        Date dayEndDt = (Date) holidayCDate;
        map.put("DAY_END_DT", dayEndDt);
        System.out.println("mapdailybalanceupdatetask###0" + map);
        List accountList = sqlMap.executeQueryForList("getDailyAvailableBalanceCorpTL", map);
        System.out.println("@#@# " + productType + " Count : " + accountList.size());
        if (accountList != null && accountList.size() > 0) {
            for (int i = 0, count = accountList.size(); i < count; i++) {
                //check if exists
                map = new HashMap();
                map.put("prodId", ((HashMap) accountList.get(i)).get("PROD_ID"));
                map.put("actNum", ((HashMap) accountList.get(i)).get("ACT_NUM"));
                map.put("slNo", ((HashMap) accountList.get(i)).get("SLNO"));
                map.put("amt", ((HashMap) accountList.get(i)).get("TOTAL_BALANCE"));
                map.put("AVAILABLE_BALANCE", ((HashMap) accountList.get(i)).get("AVAILABLE_BALANCE"));
                map.put("CLEAR_BALANCE", ((HashMap) accountList.get(i)).get("CLEAR_BALANCE"));
                map.put("dayeEndDt", dayEndDt);
                map.put(CommonConstants.BRANCH_ID, branch);
                if (productType.equals("ATL") || productType.equals("TL") || productType.equals("AD") || productType.equals("AAD") || productType.equals("BILLS")) {
                    map.put("principal", ((HashMap) accountList.get(i)).get("LOAN_BALANCE_PRINCIPAL"));
                    //FOR FLATE RATE INTEREST CALCULATION ONE MORE COLUMN
                    if (productType.equals("ATL") || productType.equals("TL") || productType.equals("AAD") || productType.equals("AD")) {
                        map.put("flatPrincipal", ((HashMap) accountList.get(i)).get("LIMIT"));
                    }
                }

//                 System.out.println("insertDailyBalance"+map);

                Integer exists = (Integer) sqlMap.executeQueryForObject("checkDailyAvailableBalanceExistsCorpTL", map);
                if (exists.intValue() == 0) {
                    sqlMap.executeUpdate("insertDailyBalanceCorpTL", map);
                } else {
                    sqlMap.executeUpdate("updateDailyBalanceCorpTL", map);
                }
                map = null;
                exists = null;
            }
        }
        System.out.println("@#@# Coporate Loans Completed...");
    }
}
