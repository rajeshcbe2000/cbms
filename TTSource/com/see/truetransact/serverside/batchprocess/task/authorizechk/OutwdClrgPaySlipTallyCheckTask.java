/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OutwdClrgPaySlipTallyCheckTask.java
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

/**
 *
 * @author Sunil (152691)
 */
public class OutwdClrgPaySlipTallyCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;

    /**
     * Creates a new instance of OutwdClrgPaySlipTallyCheckTask
     */
    public OutwdClrgPaySlipTallyCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_CODE", branch);
        paramMap.put("TODAY_DT", ServerUtil.getCurrentDate(super._branchCode));
        if (sqlMap.executeQueryForList("getNonTallyOutClr_PS", paramMap).size() > 0) {
            status.setStatus(BatchConstants.ERROR);
//            throw new TTException("Non tallied records exist in outward clearing and pay in slip");
        } else {
            status.setStatus(BatchConstants.COMPLETED);
        }
        paramMap = null;
        return status;
    }

    public static void main(String args[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("OutwdClrgPaySlipTallyCheckTask");
            HashMap paramMap = new HashMap();
            header.setBranchID("BRAN");
            header.setTaskParam(paramMap);
            OutwdClrgPaySlipTallyCheckTask ft = new OutwdClrgPaySlipTallyCheckTask(header);
            ft.executeTask();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
