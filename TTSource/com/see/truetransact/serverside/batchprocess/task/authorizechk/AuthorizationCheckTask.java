/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AuthorizationCheck.java
 *
 * Created on July 9, 2004, 4:40 PM
 */
package com.see.truetransact.serverside.batchprocess.task.authorizechk;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Sunil (152691)
 */
public class AuthorizationCheckTask extends Task {

    private static SqlMap sqlMap = null;

    /**
     * Creates a new instance of AuthorizationCheck
     */
    public AuthorizationCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap map = new HashMap();
        map.put("BRANCH_ID", _branchCode);
        if (sqlMap.executeQueryForList("getViewData", map).size() > 0) {
            System.out.println("in error part .........");
            status.setStatus(BatchConstants.ERROR);
//            throw new TTException("Records need to be authorised");
        } else {
            status.setStatus(BatchConstants.COMPLETED);
        }

        return status;
    }

    public static void main(String args[]) {
        try {
            TaskHeader hd = new TaskHeader();
            hd.setBranchID("ABC50001");
            AuthorizationCheckTask ft = new AuthorizationCheckTask(hd);
            ft.executeTask();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
