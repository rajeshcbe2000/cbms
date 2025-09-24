/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FileTask.java
 *
 * Created on May 24, 2005, 4:20 PM
 */
package com.see.truetransact.serverside.fileupload;

/**
 *
 * @author Sunil
 */
public abstract class FileTask implements java.io.Serializable {

    boolean taskContinue = true;
    FileHeader header = null;
    public String _branchCode = null;

    public abstract FileTaskStatus executeTask() throws Exception;

    /**
     * Getter for property header.
     *
     * @return Value of property header.
     */
    public FileHeader getHeader() {
        return header;
    }

    /**
     * Setter for property header.
     *
     * @param header New value of property header.
     */
    public void setHeader(FileHeader header) {
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
