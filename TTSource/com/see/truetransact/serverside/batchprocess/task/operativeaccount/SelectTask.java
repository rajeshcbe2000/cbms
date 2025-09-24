/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SelectTask.java
 *
 * Created on May 19, 2004, 12:28 PM
 */
package com.see.truetransact.serverside.batchprocess.task.operativeaccount;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonUtil;

import com.see.truetransact.servicelocator.ServiceLocator;

import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;

/**
 *
 * @author bala
 */
public class SelectTask extends Task {

    private static SqlMap sqlMap = null;

    /**
     * Creates a new instance of OperativeToInOperativeTask
     */
    public SelectTask(TaskHeader header) throws Exception {
        setHeader(header);
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);

        String mapName = getHeader().getTaskClass().substring(6);
        HashMap map = (HashMap) sqlMap.executeQueryForObject(mapName, null);
        status.setExecutionCount(CommonUtil.convertObjToDouble(map.get("RESULT")).intValue());
        System.out.println(map);
        status.setStatus(BatchConstants.COMPLETED);

        return status;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("SelectCACheck");
            SelectTask tsk = new SelectTask(header);
            TaskStatus status = tsk.executeTask();
            System.out.println("SelectTask Status: " + status.getStatus());
            System.out.println("SelectTask Count: " + status.getExecutionCount());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
