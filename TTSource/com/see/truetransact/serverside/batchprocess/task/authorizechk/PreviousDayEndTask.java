/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PreviousDayEndTask.java
 *
 * Created on Mar 26, 2005, 4:40 PM
 */
package com.see.truetransact.serverside.batchprocess.task.authorizechk;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

/**
 *
 * @author Bala
 */
public class PreviousDayEndTask extends Task {

    private static SqlMap sqlMap = null;
    private HashMap paramMap;
    private String dayEndType;
    private List branchList;
    private Date currDt = null;
    private String branch = null;
    private String taskLable;
    private String dayBeginPrev;

    /**
     * Creates a new instance of AuthorizationCheck
     */
    public PreviousDayEndTask(TaskHeader header) throws Exception {
        setHeader(header);
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        paramMap = getHeader().getTaskParam();
        if (paramMap != null && paramMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(paramMap.get("DAY_END_TYPE"));
        }
        if (paramMap != null && paramMap.containsKey("CHK_PREV_DAYEND_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("CHK_PREV_DAYEND_TASK_LABLE"));
        }
        if (paramMap != null && paramMap.containsKey("DAY_BEGIN_PREV")) {
            dayBeginPrev = CommonUtil.convertObjToStr(paramMap.get("DAY_BEGIN_PREV"));
        }
        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            branch = header.getBranchID();
            currDt = ServerUtil.getCurrentDate(branch);
        } else {
//            HashMap newMap = new HashMap();
//            List prevLst = sqlMap.executeQueryForList("getPrevDayEndDate", newMap);
//            if(prevLst!=null && prevLst.size()>0){
//                 newMap = (HashMap) prevLst.get(0);
//                 currDt = (java.util.Date)(newMap.get("PREV_DT"));
//                 branch = header.getBranchID();
////                 currDt = ServerUtil.getCurrentDate(branch);
//            }else{
//                branch = header.getBranchID();
//                currDt = ServerUtil.getCurrentDate(branch);
//            }
//            newMap = null;
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
                branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEndComp", null);
            }
        }


    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap whereMap = new HashMap();
        List lst = null;
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                System.out.println("branchList%^^^^%^" + branchList);
                System.out.println("dayBeginPrev%^^^^%^" + dayBeginPrev);
                HashMap branchMap = (HashMap) branchList.get(b);
                branch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
//                currDt = ServerUtil.getCurrentDate(branch);
                if (CommonUtil.convertObjToStr(dayBeginPrev).equals("DAY_BEGIN_PREV")) {
                    currDt = ServerUtil.getCurrentDate(branch);
                } else {
                    currDt = (java.util.Date) branchMap.get("DAYEND_DT");
                }
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
                    if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
                        whereMap.put("BRANCH_CODE", getHeader().getBranchID());
                        lst = sqlMap.executeQueryForList("getDayEndStatus", whereMap);
                    } else {
                        //                        List prevLst = sqlMap.executeQueryForList("getPrevDayEndDate", whereMap);
                        //                       if(prevLst!=null && prevLst.size()>0){
                        //                           whereMap = (HashMap) prevLst.get(0);
                        whereMap.put("DAYEND_DT", currDt);
                        //                       }
                        //                        whereMap.put("DAYEND_DT", currDt);
                        lst = sqlMap.executeQueryForList("getDayEndStatusForDC", whereMap);
                    }

                    if (lst.size() > 0) {
                        status.setStatus(BatchConstants.ERROR);
                    } else {
                        status.setStatus(BatchConstants.COMPLETED);
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
            }
        }
        if (status.getStatus() != BatchConstants.ERROR) {
            status.setStatus(BatchConstants.COMPLETED);
        }
        return status;
    }

    public static void main(String args[]) {
        try {
            TaskHeader hd = new TaskHeader();
            hd.setBranchID("Bran");
            PreviousDayEndTask ft = new PreviousDayEndTask(hd);
            System.out.println(ft.executeTask().getStatus());
            System.out.println("BatchConstants.ERROR :" + BatchConstants.ERROR);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
