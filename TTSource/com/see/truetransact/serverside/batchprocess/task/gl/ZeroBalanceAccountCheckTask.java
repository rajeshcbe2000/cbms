/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ZeroBalanceAccountCheckTask.java
 *
 * Created on March 10, 2005, 10:40 AM
 */
package com.see.truetransact.serverside.batchprocess.task.gl;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Sunil (152691) Checks if Account Heads marked as Zero Balance are
 * zero at the day end time Runs part of the Day End Process
 */
public class ZeroBalanceAccountCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    private String BRANCH_CODE = "BRANCH_CODE";

    /**
     * Creates a new instance of ZeroBalanceAccountCheckTask
     */
    public ZeroBalanceAccountCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap paramMap = new HashMap();
        paramMap.put(BRANCH_CODE, branch);
        if (sqlMap.executeQueryForList("getZeroBalAccountHeads", paramMap).size() > 0) {
            status.setStatus(BatchConstants.ERROR);
        } else {
            status.setStatus(BatchConstants.COMPLETED);
        }
        System.out.println(status.getStatus());
        paramMap = null;
        return status;
    }

    public static void main(String args[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("ZeroBalanceAccountCheckTask");
            HashMap paramMap = new HashMap();
            header.setBranchID("BRAN");
            header.setTaskParam(paramMap);
            ZeroBalanceAccountCheckTask ft = new ZeroBalanceAccountCheckTask(header);
            System.out.println(ft.executeTask());
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
