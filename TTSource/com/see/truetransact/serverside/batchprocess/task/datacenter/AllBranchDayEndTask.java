/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AllBranchDayEndTask.java
 *
 * Created on February 24, 2006, 9:23 PM
 */
package com.see.truetransact.serverside.batchprocess.task.datacenter;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Bala
 */
public class AllBranchDayEndTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    private String process = null;
    private String dayEndType;
    private String dayEndPrev;
    private HashMap paramMap;
    private Date currDt;
    private List branchList;
    private StringBuffer presentTasks = new StringBuffer();

    /**
     * Creates a new instance of BalanceCheckTask
     */
    public AllBranchDayEndTask(TaskHeader header) throws Exception {
        setHeader(header);
        process = header.getProcessType();
        branch = header.getBranchID();
        initializeTaskObj(header.getTaskParam());

    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        currDt = ServerUtil.getCurrentDate(branch);
        paramMap = dataMap;
        if (paramMap != null && paramMap.containsKey("DAY_END_PREV")) {
            dayEndPrev = CommonUtil.convertObjToStr(paramMap.get("DAY_END_PREV"));
        }
//        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
        if (CommonUtil.convertObjToStr(dayEndPrev).equals("DAY_END_PREV")) {
            if (paramMap.containsKey("BRANCH_LST")) {
                branchList = (List) paramMap.get("BRANCH_LST");
                if (branchList != null && branchList.size() > 0) {
                    for (int b = 0; b < branchList.size(); b++) {
                        HashMap dateMap = new HashMap();
                        dateMap = (HashMap) branchList.get(b);
                        currDt = ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(dateMap.get("BRANCH_CODE")));
//                    presentTasks.append(CommonUtil.convertObjToStr(dateMap.get("BRANCH_CODE")));
                        presentTasks.append("'" + CommonUtil.convertObjToStr(dateMap.get("BRANCH_CODE")) + "'");
                        if ((branchList.size() > 1) && (branchList.size() - 1 != b)) {
                            presentTasks.append(",");
                        }
                    }
                }
            }
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put("NEXT_DATE", currDt);

            branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEndChk", tempMap);
            if (branchList != null && branchList.size() > 0) {
                for (int b = 0; b < branchList.size(); b++) {
                    HashMap dateMap = new HashMap();
                    dateMap = (HashMap) branchList.get(0);
                    currDt = (java.util.Date) dateMap.get("CURR_APPL_DT");
                }
            }
            tempMap = null;
        }
//        } else {
//            HashMap tempMap = new HashMap();
//            tempMap.put(CommonConstants.BRANCH_ID,getHeader().getBranchID());
//            branchList = new ArrayList();
//            branchList.add(tempMap);
//            tempMap = null;
//        }        
    }

    public TaskStatus executeTask() throws Exception {
        /**
         * What this method does :
         */
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);

//        LinkedHashMap outputList = (LinkedHashMap) sqlMap.executeQueryForObject("checkAllBranchDayEnd", null);
        HashMap passMap = new HashMap();
        passMap.put("CURR_DT", currDt);
        if (CommonUtil.convertObjToStr(dayEndPrev).equals("DAY_END_PREV")) {
            passMap.put("BRANCH_ID", presentTasks);

        }
        List dayEndBranchList = sqlMap.executeQueryForList("checkAllBranchDayEndStartedOrNot", passMap);
        LinkedHashMap dayEndBranchMap = null;
        if (dayEndBranchList != null && dayEndBranchList.size() > 0) {
            System.out.println("#$#$# dayEndBranchList : " + dayEndBranchList);
        } else {
            System.out.println("#$#$# dayEndBranchList is null");
        }
        ArrayList outputList = new ArrayList();

        String branchFromList = "";
        if (branchList != null && branchList.size() > 0) {
            for (int b = 0; b < branchList.size(); b++) {
                HashMap branchMap = (HashMap) branchList.get(b);
                branchFromList = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                currDt = ServerUtil.getCurrentDate(branchFromList);
                int existCount = 0;
                if (dayEndBranchList != null && dayEndBranchList.size() > 0) {
                    for (int i = 0; i < dayEndBranchList.size(); i++) {
                        dayEndBranchMap = (LinkedHashMap) dayEndBranchList.get(i);
                        String dayEndBranch = CommonUtil.convertObjToStr(dayEndBranchMap.get("BRANCH_ID"));
                        int cnt = CommonUtil.convertObjToInt(dayEndBranchMap.get("CNT"));
                        if (branchFromList.equals(dayEndBranch) && cnt > 0) {
                            existCount++;
                            HashMap mapData = new HashMap();
                            mapData.put("DAYEND_DT", currDt);
                            mapData.put("BRANCH_ID", branchFromList);
//                            getSelectBranchCompletedLstPrevDay
                            List lstData = null;
                            if (CommonUtil.convertObjToStr(dayEndPrev).equals("DAY_END_PREV")) {
                                mapData.put("CURR_APPL_DT", currDt);
                                lstData = ((List) sqlMap.executeQueryForList("getSelectBranchCompletedLstPrevDay", mapData));
                            } else {
                                lstData = ((List) sqlMap.executeQueryForList("getSelectBranchCompletedLst", mapData));
                            }
                            mapData = null;
                            if (lstData != null && lstData.size() > 0) {
                                for (int j = 0; j < lstData.size(); j++) {
                                    mapData = (HashMap) lstData.get(j);
//                                LinkedHashMap outputMap = new LinkedHashMap();
//                                outputMap.put("BRANCH_ID", branchFromList);
//                                outputMap.put("TASK_NAME", "DayEnd not completed...");
//                                outputMap.put("TASK_STATUS", "ERROR");
//                                outputList.add(outputMap);
                                }
                            } else {
//                                LinkedHashMap outputMap = new LinkedHashMap();
//                                outputMap.put("BRANCH_ID", branchFromList);
//                                outputMap.put("TASK_NAME", "Branch DayEnd not completed...");
//                                outputMap.put("TASK_STATUS", "ERROR");
                                outputList.add(branchFromList);
                            }
                        }
                    }
                }
                if (existCount == 0) {
                    outputList.add(branchFromList);
                }
            }
        }

        System.out.println("#$#$# outputList : " + outputList);
        if (outputList.size() > 0) {
            status.setStatus(BatchConstants.ERROR);
        } else {
            status.setStatus(BatchConstants.COMPLETED);
        }

        System.out.println("Status " + status + ":" + BatchConstants.COMPLETED);
        return status;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("AllBranchDayEndTask");
            HashMap paramMap = new HashMap();
            //header.setProcessType(CommonConstants.DAY_BEGIN);
            header.setProcessType(CommonConstants.DAY_END);
            header.setBranchID("ABC50002");
            header.setTaskParam(paramMap);
            AllBranchDayEndTask tsk = new AllBranchDayEndTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
