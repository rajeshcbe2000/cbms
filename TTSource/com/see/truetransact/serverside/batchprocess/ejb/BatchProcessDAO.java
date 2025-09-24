/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BatchProcessDAO.java
 *
 * Created on Mon May 17 11:06:22 PST 2004
 */
package com.see.truetransact.serverside.batchprocess.ejb;

import java.util.HashMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskFactory;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;

/**
 * BatchProcessDAO. This is used for Parameter Data Access.
 *
 * @author bala
 *
 */
public class BatchProcessDAO extends TTDAO {

    /**
     * Creates a new instance of ParameterDAO
     */
    public BatchProcessDAO() {
    }

    // To insert or update or delete the data in the database
    public HashMap execute(HashMap map) throws Exception {
        return null;
    }

    // To retrive data from the database
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        HashMap resultMap = new HashMap();
        TaskHeader header = (TaskHeader) obj.get(BatchConstants.TASK_HEADER);
        header.setBranchID(_branchCode);
        System.out.println("First resultMap@@@@@ executeQuery resultMap: " + resultMap);
        Task tsk = TaskFactory.createTask(header);
        TaskStatus status = tsk.executeTask();
        resultMap.put("STATUS", status);
        System.out.println("@@@@@ executeQuery resultMap: " + resultMap);
        return resultMap;
    }

    public static void main(String str[]) {
        try {
            BatchProcessDAO dao = new BatchProcessDAO();
            HashMap obj = new HashMap();
            TaskHeader header = new TaskHeader();
            header.setTaskClass("InOperativeToDormantTask");
            obj.put(BatchConstants.TASK_HEADER, header);
            System.out.println(dao.executeQuery(obj));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
