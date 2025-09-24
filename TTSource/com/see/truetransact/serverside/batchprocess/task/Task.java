/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Task.java
 *
 * Created on May 17, 2004, 4:26 PM
 */
package com.see.truetransact.serverside.batchprocess.task;

/**
 *
 * @author bala
 */
public abstract class Task implements java.io.Serializable {

    boolean taskContinue = true;
    TaskHeader header = null;
    public String _bankCode = null;
    public String _branchCode = null;

    public abstract TaskStatus executeTask() throws Exception;

    /**
     * Getter for property header.
     *
     * @return Value of property header.
     */
    public TaskHeader getHeader() {
        return header;
    }

    /**
     * Setter for property header.
     *
     * @param header New value of property header.
     */
    public void setHeader(TaskHeader header) {
        this._bankCode = header.getBankID();
        this._branchCode = header.getBranchID();
        this.header = header;
    }

    /**
     * Getter for property taksContinue.
     *
     * @return Value of property taksContinue.
     */
    public boolean isTaskContinue() {
        return taskContinue;
    }

    /**
     * Setter for property taksContinue.
     *
     * @param taksContinue New value of property taksContinue.
     */
    public void setTaskContinue(boolean taskContinue) {
        this.taskContinue = taskContinue;
    }
}
