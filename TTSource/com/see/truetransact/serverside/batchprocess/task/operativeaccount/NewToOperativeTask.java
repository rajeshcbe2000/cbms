/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OperativeToInOperativeTask.java
 *
 * Created on May 19, 2004, 12:28 PM
 */
package com.see.truetransact.serverside.batchprocess.task.operativeaccount;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverutil.ServerUtil;
import java.util.HashMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;

import com.ibatis.db.sqlmap.SqlMap;

/**
 *
 * @author bala
 */
public class NewToOperativeTask extends Task {

    private static SqlMap sqlMap = null;

    /**
     * Creates a new instance of OperativeToInOperativeTask
     */
    public NewToOperativeTask(TaskHeader header) throws Exception {
        setHeader(header);
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);

        sqlMap.startTransaction();
        HashMap paramMap = new HashMap();
        paramMap.put("TODAY_DT", ServerUtil.getCurrentDate(super._branchCode));
        sqlMap.executeUpdate("OperativeAccount.newToOperationalBatch", paramMap);
        paramMap = null;
        sqlMap.commitTransaction();
        status.setStatus(BatchConstants.COMPLETED);

        return status;
    }
}
