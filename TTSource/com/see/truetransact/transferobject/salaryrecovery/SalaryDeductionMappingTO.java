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
public class SalaryDeductionMappingTO extends TransferObject implements Serializable {

    private String transId = "";
    private String acHdId = "";
    private String actNum = "";
    private String oldNo = "";
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
    private String exemptionMode = "";
    private String exemptionMode_Temp = "";
    private String prodId_Temp = "";
    private String prodType_Temp = "";
    private String actNum_Temp = "";

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
     * Setter/Getter for AVAILABLE_BALANCE - table Field
     */
    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for PROD_TYPE - table Field
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setTxtOldAccNo(String oldNo) {
        this.oldNo = oldNo;

    }

    String getTxtOldAccNo() {
        return this.oldNo;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

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

    public String getActNum_Temp() {
        return actNum_Temp;
    }

    public void setActNum_Temp(String actNum_Temp) {
        this.actNum_Temp = actNum_Temp;
    }

    public String getExemptionMode_Temp() {
        return exemptionMode_Temp;
    }

    public void setExemptionMode_Temp(String exemptionMode_Temp) {
        this.exemptionMode_Temp = exemptionMode_Temp;
    }

    public String getProdId_Temp() {
        return prodId_Temp;
    }

    public void setProdId_Temp(String prodId_Temp) {
        this.prodId_Temp = prodId_Temp;
    }

    public String getProdType_Temp() {
        return prodType_Temp;
    }

    public void setProdType_Temp(String prodType_Temp) {
        this.prodType_Temp = prodType_Temp;
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
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("status", status));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("deleteFlag", deleteFlag));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("employerRefNo", employerRefNo));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("oldNo", oldNo));
        strB.append(getTOString("exemptionMode_Temp", exemptionMode_Temp));
        strB.append(getTOString("prodId_Temp", prodId_Temp));
        strB.append(getTOString("prodType_Temp", prodType_Temp));
        strB.append(getTOString("actNum_Temp", actNum_Temp));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("employerRefNo", employerRefNo));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("deleteFlag", deleteFlag));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("oldNo", oldNo));
        strB.append(getTOXml("exemptionMode_Temp", exemptionMode_Temp));
        strB.append(getTOXml("prodId_Temp", prodId_Temp));
        strB.append(getTOXml("prodType_Temp", prodType_Temp));
        strB.append(getTOXml("actNum_Temp", actNum_Temp));
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

    /**
     * Getter for property exemptionMode.
     *
     * @return Value of property exemptionMode.
     */
    public String getExemptionMode() {
        return exemptionMode;
    }

    /**
     * Setter for property exemptionMode.
     *
     * @param exemptionMode New value of property exemptionMode.
     */
    public void setExemptionMode(String exemptionMode) {
        this.exemptionMode = exemptionMode;
    }
}