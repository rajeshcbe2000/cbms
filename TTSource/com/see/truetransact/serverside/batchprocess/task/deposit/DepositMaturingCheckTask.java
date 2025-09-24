/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositMaturingCheckTask.java
 *
 * Created on May 23, 2012, 04:52 PM
 * Author : Rajesh
 * Desc : The class is used to check for Matured Deposits on run date
 *        Is always run as a Batch, in DAY END process
 *        header should contain User id and Branch Id
 *
 *Map Used : DepositMap
 *
 *
 */
package com.see.truetransact.serverside.batchprocess.task.deposit;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.common.transaction.TransferTrans;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;

/**
 *
 * @author Rajesh
 */
public class DepositMaturingCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch;
    private TaskStatus status = null;
    private String dayEndType;
    private List branchList;
    private HashMap taskMap;
    private Date currDt;

    /**
     * Creates a new instance of DepositMaturingCheckTask
     */
    public DepositMaturingCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        currDt = ServerUtil.getCurrentDate(branch);
        initializeTaskObj(header.getTaskParam());
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        taskMap = dataMap;
        if (taskMap != null && taskMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(taskMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            tempMap.put("NEXT_DATE", currDt);
            branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEnd", tempMap);
            tempMap = null;
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
    }

    public TaskStatus executeTask() throws Exception {
        status = new TaskStatus();
        HashMap whereMap = new HashMap();
        try {
            if (branchList != null && branchList.size() > 0) {
                if (branchList.size() == 1) {
                    whereMap = (HashMap) branchList.get(0);
                }
                whereMap.put("TODAY_DT", currDt);
                whereMap.put("USER_ID", getHeader().getUserID());
                System.out.println("#$#$ whereMap INSIDE DepositMaturingCheckTask:" + whereMap);
                ArrayList accountList = (ArrayList) sqlMap.executeQueryForList("getLTDMaturedDepositAccounts", whereMap);
                if (accountList != null && accountList.size() > 0) {
                    status.setStatus(BatchConstants.ERROR);
                    accountList.clear();
                } else {
                    status.setStatus(BatchConstants.COMPLETED);
                }
                accountList = null;
                whereMap.clear();
                whereMap = null;
            }
        } catch (Exception e) {
            status.setStatus(BatchConstants.ERROR);
            e.printStackTrace();
        }
        return status;

    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("DepositMaturingCheckTask");
            header.setBranchID("0001");
            header.setUserID("sysadmin");
            DepositMaturingCheckTask tsk = new DepositMaturingCheckTask(header);
            TaskStatus status = tsk.executeTask();
            System.out.println("status : " + status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
