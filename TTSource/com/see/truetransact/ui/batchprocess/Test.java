/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Test.java
 *
 * Created on May 26, 2004, 6:00 PM
 */

package com.see.truetransact.ui.batchprocess;

/**
 *
 * @author  bala
 */

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.HashMap;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;

public class Test {
    
    /** Creates a new instance of Test */
    public Test() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
       // ProxyParameters.USER_ID = "admin";
        //ProxyParameters.BRANCH_ID = "bran";
        ProxyFactory proxyFactory = ProxyFactory.createProxy();
        
        HashMap map = new HashMap();
        map.put(CommonConstants.JNDI, "BatchProcessJNDI");
        map.put(CommonConstants.HOME, "batchprocess.ejb.BatchProcessHome");
        map.put(CommonConstants.REMOTE, "batchprocess.ejb.BatchProcess");

        HashMap obj = new HashMap();
        TaskHeader header = new TaskHeader();
        header.setTaskClass("StandingInstructionTask");
        obj.put(BatchConstants.TASK_HEADER, header);
        System.out.println (proxyFactory.executeQuery(obj, map));
        
    }
    
}
