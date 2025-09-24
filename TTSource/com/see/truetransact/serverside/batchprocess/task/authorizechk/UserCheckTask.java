/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * UserCheckTask.java
 *
 * Created on Mar 26, 2005, 4:40 PM
 */
package com.see.truetransact.serverside.batchprocess.task.authorizechk;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Bala
 */
public class UserCheckTask extends Task {

    private static SqlMap sqlMap = null;

    /**
     * Creates a new instance of AuthorizationCheck
     */
    public UserCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap whereMap = new HashMap();
        whereMap.put("BRANCH_CODE", getHeader().getBranchID());
        whereMap.put("USER_ID", getHeader().getUserID());
        List lst = sqlMap.executeQueryForList("getUserLogoutStatus", whereMap);

        if (lst.size() > 0) {
            System.out.println("in error part .........");
            status.setStatus(BatchConstants.ERROR);
        } else {
            status.setStatus(BatchConstants.COMPLETED);
        }

        return status;
    }

    public static void main(String args[]) {
        try {
            TaskHeader hd = new TaskHeader();
            hd.setBranchID("Bran");
            hd.setUserID("sysadmin");
            UserCheckTask ft = new UserCheckTask(hd);
            System.out.println(ft.executeTask().getStatus());
            System.out.println("BatchConstants.ERROR :" + BatchConstants.ERROR);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
