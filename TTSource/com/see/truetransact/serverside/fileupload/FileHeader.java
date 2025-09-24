/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FileTaskHeader.java
 *
 * Created on May 24, 2005, 4:20 PM
 */
package com.see.truetransact.serverside.fileupload;

import java.io.File;

/**
 *
 * @author Sunil
 */
public class FileHeader implements java.io.Serializable {

    private String fileType; // CSV, TXT, XLS
    private File sourceFile; //File object passed from client to server
    private File mappingFile;
    private String encryptionType; //RSA, 
    private String userID;
    private String branchID;
    private String ipAddr;
    private String processType; //DAY BEGIN, DAY END

    /**
     * Creates a new instance of TaskHeader
     */
    public FileHeader() {
    }

    public String toString() {
        StringBuffer strB = new StringBuffer();
        strB.append("encryptionType;" + encryptionType + "\n");
        strB.append("userID;" + userID + "\n");
        strB.append("branchID;" + branchID + "\n");
        strB.append("ipAddr;" + ipAddr + "\n");
        strB.append("processType;" + processType + "\n");
        return strB.toString();
    }

    /**
     * Getter for property fileType.
     *
     * @return Value of property fileType.
     */
    public java.lang.String getFileType() {
        return fileType;
    }

    /**
     * Setter for property fileType.
     *
     * @param fileType New value of property fileType.
     */
    public void setFileType(java.lang.String fileType) {
        this.fileType = fileType;
    }

    /**
     * Getter for property encryptionType.
     *
     * @return Value of property encryptionType.
     */
    public java.lang.String getEncryptionType() {
        return encryptionType;
    }

    /**
     * Setter for property encryptionType.
     *
     * @param encryptionType New value of property encryptionType.
     */
    public void setEncryptionType(java.lang.String encryptionType) {
        this.encryptionType = encryptionType;
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

    /**
     * Getter for property sourceFile.
     *
     * @return Value of property sourceFile.
     */
    public java.io.File getSourceFile() {
        return sourceFile;
    }

    /**
     * Setter for property sourceFile.
     *
     * @param sourceFile New value of property sourceFile.
     */
    public void setSourceFile(String fileName) {
        this.sourceFile = new File(fileName);
    }

    /**
     * Getter for property mappingFile.
     *
     * @return Value of property mappingFile.
     */
    public java.io.File getMappingFile() {
        return mappingFile;
    }

    /**
     * Setter for property mappingFile.
     *
     * @param mappingFile New value of property mappingFile.
     */
    public void setMappingFile(String fileName) {
        this.mappingFile = new File(fileName);
    }
}
