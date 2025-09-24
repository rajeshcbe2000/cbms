/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PooledThread.java
 *
 * Created on May 25, 2004, 12:56 PM
 */

package com.see.truetransact.ui.batchprocess;

import java.util.HashMap;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;

/**
 *
 * @author  Dr. Harvinder Singh
 */

public class PooledThread extends PooledObject implements Runnable {
    private boolean status = false;
    private TaskStatus taskStatus;
    private TaskHeader taskHeader;
    private HashMap map, obj;
    private String name;
    /**
     * PooledThread's constructor.
     *
     * @param name          String
     * @param taskHeader    TaskHeader
     */
    public PooledThread(String name, TaskHeader taskHeader) {
        this.name = name;
        this.taskHeader = taskHeader;
        
        //if (taskHeader == null) throw new Exception ("Task Header is not found");
        
        obj = new HashMap();
        obj.put(BatchConstants.TASK_HEADER, taskHeader);

        setTaskHeader(taskHeader);
        
        // Connecting to JNDI
        map = new HashMap();
        map.put(CommonConstants.JNDI, "BatchProcessJNDI");
        map.put(CommonConstants.HOME, "batchprocess.ejb.BatchProcessHome");
        map.put(CommonConstants.REMOTE, "batchprocess.ejb.BatchProcess");
    }
    
    /**
     * Tasks are executed PooledThread
     */
    public void run() {
        try {
            status = false;
            setTaskStatus ((TaskStatus) proxyFactory.executeQuery(obj, map).get("STATUS"));
            status = true;
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void setTaskHeader(TaskHeader taskHeader) {
        this.taskHeader = taskHeader;
    }
    
    public TaskHeader getTaskHeader () {
        return this.taskHeader;
    }
    
    public void setTaskStatus(TaskStatus tskStatus) {
        this.taskStatus = tskStatus;
    }
    
    public TaskStatus getTaskStatus () {
        return this.taskStatus;
    }
    
    /**
     * Returns Status 
     */
    public boolean getStatus() {
        return status;
    }
    
    public static void main (String arr[]) {
        TaskHeader thead = new TaskHeader();
        thead.setTaskClass("NewToOperativeTask");
        PooledThread thr = new PooledThread("test", thead);
        thr.run();
    }
}