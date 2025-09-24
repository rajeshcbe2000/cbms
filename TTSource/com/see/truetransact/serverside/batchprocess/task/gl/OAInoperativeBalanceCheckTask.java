/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OAInoperativeBalanceCheckTask.java
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
import com.ibatis.db.sqlmap.SqlMap;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Sunil (152691)
 */
public class OAInoperativeBalanceCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    private String BRANCH_CODE = "BRANCH_CODE";
    private String GL_SUM = "GL_SUM";
    private String ACT_SUM = "ACT_SUM";

    /**
     * Creates a new instance of OAInoperativeBalanceCheckTask
     */
    public OAInoperativeBalanceCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public TaskStatus executeTask() throws Exception {
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap paramMap = new HashMap();
        HashMap tempMap = null;
        paramMap.put(BRANCH_CODE, branch);
        List outputList = sqlMap.executeQueryForList("getOAInoperativeBalCheck", paramMap);
        for (int i = 0, j = outputList.size(); i < j; i++) {
            tempMap = (HashMap) outputList.get(i);
            if (tempMap.get(GL_SUM) != null
                    && !CommonUtil.convertObjToStr(tempMap.get(GL_SUM)).equals("")) {
                if (CommonUtil.convertObjToDouble(tempMap.get(GL_SUM)).doubleValue()
                        == CommonUtil.convertObjToDouble(tempMap.get(ACT_SUM)).doubleValue()) {
                    outputList.remove(i);
                }
            }
        }
        if (outputList.size() > 0) {
            status.setStatus(BatchConstants.ERROR);
        } else {
            status.setStatus(BatchConstants.COMPLETED);
        }
        System.out.println(status.getStatus());
        tempMap = null;
        paramMap = null;
        return status;
    }

    public static void main(String args[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("OAInoperativeBalanceCheckTask");
            HashMap paramMap = new HashMap();
            header.setBranchID("BRAN");
            header.setTaskParam(paramMap);
            OAInoperativeBalanceCheckTask ft = new OAInoperativeBalanceCheckTask(header);
            System.out.println(ft.executeTask());
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
