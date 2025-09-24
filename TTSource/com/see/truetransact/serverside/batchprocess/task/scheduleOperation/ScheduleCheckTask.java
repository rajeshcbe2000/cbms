/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ScheduleCheckTask.java
 *
 * Created on JULY, 2016, 13:30 PM
 */
package com.see.truetransact.serverside.batchprocess.task.scheduleOperation;

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
import com.see.truetransact.commonutil.DateUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Rishad This class checks if cash transaction from Vault and teller
 * tally Is to be called as a part of the Day End batch process
 */
public class ScheduleCheckTask extends Task {
    private Date prevDt = null;
    private Date curDate = null;
    private static SqlMap sqlMap = null;
    private String branch = null;
    private String process = null;
    HashMap vaultMap = new HashMap();
    private String taskLable;
    public ScheduleCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        process = header.getProcessType();
        branch = header.getBranchID();
        initializeTaskObj(header.getTaskParam());
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
          taskLable = "ScheduleCheck";
    }

    public TaskStatus executeTask() throws Exception {
        /**
         * Formaula to be employed Vault sum(payment) + Cash sum(receipts) -
         * Cash sum(payments) = Vault sum(receipt)
         *
         */
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        System.out.println(status.getStatus());
        HashMap dataMap = new HashMap();
        HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_CODE", branch);
        paramMap.put("TODAY_DT", ServerUtil.getCurrentDate(super._branchCode));
        curDate = ServerUtil.getCurrentDate(super._branchCode);
        prevDt = DateUtil.addDaysProperFormat(curDate, -1);
        paramMap.put("PREV_DT", prevDt);
        System.out.println("paramMap@@" + paramMap);
        ArrayList finalList = new ArrayList();
        HashMap tempMap = null;
        HashMap hashData = null;
        List outputList = null;
        List lst = null;
        List lstData = null;
        HashMap data = null;
        List crDrList = null;
        double amt = 0;
        double glcurbal = 0;
        double openingCount = 0;
        System.out.println("paramMap@@" + paramMap);
        lst = sqlMap.executeQueryForList("checkSchedule", paramMap);
        //        System.out.println("@@@@@lst"+lst);
        if (lst != null) {
            if (lst.size() > 0) {
                for (int a = 0, b = lst.size(); a < b; a++) {
                    data = (HashMap) lst.get(a);
                    vaultMap.put("TABLE_NAME", data.get("TABLE_NAME"));
                    vaultMap.put("ACT_NUM", data.get("ACT_NUM"));
                    vaultMap.put("OPBAL", data.get("OPBAL"));
                    vaultMap.put("ACT_BAL", data.get("ACT_BAL"));
                    vaultMap.put("CLOSE_BAL", data.get("CLOSE_BAL"));
                    vaultMap.put("DIFF", data.get("DIFF"));

                }
                //                System.out.println("vaultMap"+vaultMap);
                if (vaultMap.size() > 0) {
                    status.setStatus(BatchConstants.ERROR);
                    System.out.println("Completion Status : " + status.getStatus());
                }

            } else {
                status.setStatus(BatchConstants.COMPLETED);
            }
        }
        
        System.out.println("Completion Status : " + status.getStatus());
        return status;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("ScheduleCheckTask");
            HashMap paramMap = new HashMap();
            header.setProcessType(CommonConstants.DAY_END);
            header.setBranchID(CommonConstants.BRANCH_ID);
            header.setTaskParam(paramMap);
            ScheduleCheckTask tsk = new ScheduleCheckTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
