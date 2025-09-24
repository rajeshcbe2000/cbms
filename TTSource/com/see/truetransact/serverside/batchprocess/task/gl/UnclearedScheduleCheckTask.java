/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * UnclearedScheduleCheckTask.java
 *
 * Created on March 11, 2005, 10:40 AM
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
import com.see.truetransact.serverutil.ServerUtil;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Sunil (152691)
 */
public class UnclearedScheduleCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    private String BRANCH_CODE = "BRANCH_CODE";
    private String TODAY_DT = "TODAY_DT";

    /**
     * Creates a new instance of UnclearedScheduleCheckTask
     */
    public UnclearedScheduleCheckTask(TaskHeader header) throws Exception {
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
        paramMap.put(TODAY_DT, ServerUtil.getCurrentDate(branch));
        if (sqlMap.executeQueryForList("getUnclearedClrgSchedule", paramMap).size() > 0) {
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
            header.setTaskClass("UnclearedScheduleCheckTask");
            HashMap paramMap = new HashMap();
            header.setBranchID("BRAN");
            header.setTaskParam(paramMap);
            UnclearedScheduleCheckTask ft = new UnclearedScheduleCheckTask(header);
            System.out.println(ft.executeTask());
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
