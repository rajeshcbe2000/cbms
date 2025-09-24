/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterBranchAuthorizationCheck.java
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
import com.see.truetransact.serverutil.ServerUtil;
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
public class InterBranchAuthorizationCheck extends Task {

    private static SqlMap sqlMap = null;
    private String _branchCode;
    private HashMap paramMap;

    /**
     * Creates a new instance of AuthorizationCheck
     */
    public InterBranchAuthorizationCheck(TaskHeader header) throws Exception {
        setHeader(header);
        _branchCode = header.getBranchID();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        paramMap = header.getTaskParam();
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap map = new HashMap();
        map.put("BRANCH_ID", _branchCode);
        map.put("TODAY_DT", ServerUtil.getCurrentDate(_branchCode));
        if (paramMap != null && paramMap.containsKey("DAY_END_TYPE")
                && CommonUtil.convertObjToStr(paramMap.get("DAY_END_TYPE")).equals("BANK_LEVEL")) {
            map.put("DAY_END_TYPE", paramMap.get("DAY_END_TYPE"));
        }
        if (sqlMap.executeQueryForList("getNonAuthRecInterBranch", map).size() > 0) {
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
            hd.setBranchID("Bran");
            InterBranchAuthorizationCheck ft = new InterBranchAuthorizationCheck(hd);
            ft.executeTask();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
