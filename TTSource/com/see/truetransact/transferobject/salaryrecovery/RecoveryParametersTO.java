/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashTransactionTO.java
 *
 * Created on Thu Aug 12 12:40:27 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.salaryrecovery;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CASH_TRANS.
 */
public class RecoveryParametersTO extends TransferObject implements Serializable {

    private String transId = "";
    private String acHdId = "";
    private String actNum = "";
    private Double inpAmount = null;
    private String inpCurr = "";
    private Double amount = null;
    private String deleteFlag = "";
    private Date transDt = null;
    private String transType = "";
    private String instType = "";
    private Date instDt = null;
    private String tokenNo = null;
    private String initTransId = "";
    private String initChannType = "";
    private String particulars = "";
    private String narration = "";
    private String status = "";
    private String instrumentNo1 = "";
    private String instrumentNo2 = "";
    private Double availableBalance = null;
    private String prodId = "";
    private String prodType = "";
    private String authorizeStatus = null;;
    private String authorizeStatus_2 = "";
    private String firstDay = "";
    private String lastDay = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizeRemarks = "";
    private String statusBy = "";
    private String branchId = "";
    private Date statusDt = null;
    private String linkBatchId = null;
    private String initiatedBranch = "";
    private Date linkBatchDt = null;
    private String panNo = "";
    private String loanHierarchy = "";
    private String employerRefNo = "";
    private String remarks = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String salarySuspenseProdId = "";
    private String mDSSuspenseProdId = "";
    private String rDSuspenseProdId = "";

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for AMOUNT - table Field
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;

    }

    public String getDeleteFlag() {
        return this.deleteFlag;
    }

    /**
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    /**
     * Setter/Getter for AUTHORIZE_REMARKS - table Field
     */
    public void setAuthorizeRemarks(String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    public String getAuthorizeRemarks() {
        return authorizeRemarks;
    }

    /**
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setEmployerRefNo(String employerRefNo) {
        this.employerRefNo = employerRefNo;
    }

    public String getEmployerRefNo() {
        return employerRefNo;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("transId");
        return transId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));

        strB.append(getTOString("firstDay", firstDay));
        strB.append(getTOString("lastDay", lastDay));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));

        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("firstDay", firstDay));
        strB.append(getTOXml("lastDay", lastDay));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property linkBatchId.
     *
     * @return Value of property linkBatchId.
     */
    /**
     * Getter for property initiatedBranch.
     *
     * @return Value of property initiatedBranch.
     */
    public java.lang.String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * Setter for property initiatedBranch.
     *
     * @param initiatedBranch New value of property initiatedBranch.
     */
    public void setInitiatedBranch(java.lang.String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }

    public void setFirstDay(String firstDay) {
        this.firstDay = firstDay;
    }

    public String getFirstDay() {
        return firstDay;
    }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    public String getLastDay() {
        return lastDay;
    }

    /**
     * Getter for property salarySuspenseProdId.
     *
     * @return Value of property salarySuspenseProdId.
     */
    public String getSalarySuspenseProdId() {
        return salarySuspenseProdId;
    }

    /**
     * Setter for property salarySuspenseProdId.
     *
     * @param salarySuspenseProdId New value of property salarySuspenseProdId.
     */
    public void setSalarySuspenseProdId(String salarySuspenseProdId) {
        this.salarySuspenseProdId = salarySuspenseProdId;
    }

    /**
     * Getter for property mDSSuspenseProdId.
     *
     * @return Value of property mDSSuspenseProdId.
     */
    public String getMDSSuspenseProdId() {
        return mDSSuspenseProdId;
    }

    /**
     * Setter for property mDSSuspenseProdId.
     *
     * @param mDSSuspenseProdId New value of property mDSSuspenseProdId.
     */
    public void setMDSSuspenseProdId(String mDSSuspenseProdId) {
        this.mDSSuspenseProdId = mDSSuspenseProdId;
    }

    /**
     * Getter for property rDSuspenseProdId.
     *
     * @return Value of property rDSuspenseProdId.
     */
    public String getRDSuspenseProdId() {
        return rDSuspenseProdId;
    }

    /**
     * Setter for property rDSuspenseProdId.
     *
     * @param rDSuspenseProdId New value of property rDSuspenseProdId.
     */
    public void setRDSuspenseProdId(String rDSuspenseProdId) {
        this.rDSuspenseProdId = rDSuspenseProdId;
    }
}