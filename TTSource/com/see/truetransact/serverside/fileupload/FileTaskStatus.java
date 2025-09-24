/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FileTaskStatus.java
 *
 * Created on May 24, 2005, 4:20 PM
 */
package com.see.truetransact.serverside.fileupload;

/**
 *
 * @author Sunil
 */
public class FileTaskStatus implements java.io.Serializable {

    int status;
    private int executionCount;
    private int actualCount;

    /**
     * Creates a new instance of TaskStatus
     */
    public FileTaskStatus() {
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return "Task Status is " + String.valueOf(status);
    }

    /**
     * Getter for property executionCount.
     *
     * @return Value of property executionCount.
     */
    public int getExecutionCount() {
        return executionCount;
    }

    /**
     * Setter for property executionCount.
     *
     * @param executionCount New value of property executionCount.
     */
    public void setExecutionCount(int executionCount) {
        this.executionCount = executionCount;
    }

    /**
     * Getter for property actualCount.
     *
     * @return Value of property actualCount.
     */
    public int getActualCount() {
        return actualCount;
    }

    /**
     * Setter for property actualCount.
     *
     * @param actualCount New value of property actualCount.
     */
    public void setActualCount(int actualCount) {
        this.actualCount = actualCount;
    }
}
