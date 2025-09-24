/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FixedWidthTask.java
 *
 * Created on May 24, 2005 4:40 PM
 * This file will not handle any DB connections. 
 * Read the file in server.
 */
package com.see.truetransact.serverside.fileupload;

import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.ibatis.db.sqlmap.SqlMap;

/**
 *
 * @author Sunil (152691)
 */
public class FixedWidthTask extends FileTask {

    private static SqlMap sqlMap = null;

    /**
     * Creates a new instance of AuthorizationCheck
     */
    public FixedWidthTask(FileHeader header) throws Exception {
        setHeader(header);
    }

    public FileTaskStatus executeTask() throws Exception {
        FileTaskStatus status = new FileTaskStatus();
        status.setStatus(BatchConstants.STARTED);
        //Do task here
        // if error set error as below
        status.setStatus(BatchConstants.ERROR);
//            throw new TTException("Records need to be authorised");
        //if no error set status as below
        status.setStatus(BatchConstants.COMPLETED);

        return status;
    }

    public static void main(String args[]) {
        try {
            FileHeader hd = new FileHeader();
            hd.setBranchID("ABC50001");
            FixedWidthTask ft = new FixedWidthTask(hd);
            ft.executeTask();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
