/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TaskHeader.java
 *
 * Created on May 17, 2004, 4:20 PM
 */
package com.see.truetransact.serverside.batchprocess.task;

import java.util.HashMap;

/**
 *
 * @author bala
 */
public class TaskHeader implements java.io.Serializable {

    private String taskClass;
    private HashMap taskParam;
    private String transactionType; // This can be Charge or Interest Type
    private String productType; // Product Type like OA, TD, TL...
    private String userID;
    private String bankID;
    private String branchID;
    private String ipAddr;
    private String processType;
    private String DB_DRIVER_NAME;

    /**
     * Creates a new instance of TaskHeader
     */
    public TaskHeader() {
    }

    /**
     * Getter for property taskClass.
     *
     * @return Value of property taskClass.
     */
    public java.lang.String getTaskClass() {
        return taskClass;
    }

    /**
     * Setter for property taskClass.
     *
     * @param taskClass New value of property taskClass.
     */
    public void setTaskClass(java.lang.String taskClass) {
        this.taskClass = taskClass;
    }

    /**
     * Getter for property taskParam.
     *
     * @return Value of property taskParam.
     */
    public HashMap getTaskParam() {
        return taskParam;
    }

    /**
     * Setter for property taskParam.
     *
     * @param taskParam New value of property taskParam.
     */
    public void setTaskParam(HashMap taskParam) {
        this.taskParam = taskParam;
    }

    /**
     * Getter for property transactionType.
     *
     * @return Value of property transactionType.
     */
    public java.lang.String getTransactionType() {
        return transactionType;
    }

    /**
     * Setter for property transactionType.
     *
     * @param transactionType New value of property transactionType.
     */
    public void setTransactionType(java.lang.String transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * Getter for property productType.
     *
     * @return Value of property productType.
     */
    public java.lang.String getProductType() {
        return productType;
    }

    /**
     * Setter for property productType.
     *
     * @param productType New value of property productType.
     */
    public void setProductType(java.lang.String productType) {
        this.productType = productType;
    }

    /**
     * Getter for property branchID.
     *
     * @return Value of property branchID.
     */
    public java.lang.String getBranchID() {
        return branchID;
    }

    /**
     * Setter for property branchID.
     *
     * @param branchID New value of property branchID.
     */
    public void setBranchID(java.lang.String branchID) {
        this.branchID = branchID;
    }

    /**
     * Getter for property ipAddr.
     *
     * @return Value of property ipAddr.
     */
    public java.lang.String getIpAddr() {
        return ipAddr;
    }

    /**
     * Setter for property ipAddr.
     *
     * @param ipAddr New value of property ipAddr.
     */
    public void setIpAddr(java.lang.String ipAddr) {
        this.ipAddr = ipAddr;
    }

    /**
     * Getter for property userID.
     *
     * @return Value of property userID.
     */
    public java.lang.String getUserID() {
        return userID;
    }

    /**
     * Setter for property userID.
     *
     * @param userID New value of property userID.
     */
    public void setUserID(java.lang.String userID) {
        this.userID = userID;
    }

    /**
     * Getter for property processType.
     *
     * @return Value of property processType.
     */
    public java.lang.String getProcessType() {
        return processType;
    }

    /**
     * Setter for property processType.
     *
     * @param processType New value of property processType.
     */
    public void setProcessType(java.lang.String processType) {
        this.processType = processType;
    }

    public String toString() {
        StringBuffer strB = new StringBuffer();
        strB.append("taskClass;" + taskClass + "\n");
        strB.append("taskParam;" + taskParam + "\n");
        strB.append("transactionType;" + transactionType + "\n");
        strB.append("productType;" + productType + "\n");
        strB.append("userID;" + userID + "\n");
        strB.append("bankID;" + bankID + "\n");
        strB.append("branchID;" + branchID + "\n");
        strB.append("ipAddr;" + ipAddr + "\n");
        strB.append("processType;" + processType + "\n");
        strB.append("DB_DRIVER_NAME;" + DB_DRIVER_NAME + "\n");
        return strB.toString();
    }

    /**
     * Getter for property bankID.
     *
     * @return Value of property bankID.
     */
    public java.lang.String getBankID() {
        return bankID;
    }

    /**
     * Setter for property bankID.
     *
     * @param bankID New value of property bankID.
     */
    public void setBankID(java.lang.String bankID) {
        this.bankID = bankID;
    }
    
    public String getDB_DRIVER_NAME() {
        return DB_DRIVER_NAME;
    }

    public void setDB_DRIVER_NAME(String DB_DRIVER_NAME) {
        this.DB_DRIVER_NAME = DB_DRIVER_NAME;
    }
    
}
