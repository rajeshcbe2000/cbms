/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OutwardClearingTO.java
 * 
 * Created on Fri Aug 06 12:36:18 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.transaction.clearing.outward;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OUTWARD_CLEARING.
 */
public class OutwardClearingTO extends TransferObject implements Serializable {

    private String batchId = "";
    private String outwardId = "";
    private Date outwardDt = null;
    private String clearingType = "";
    private String instrumentType = "";
    private Date instrumentDt = null;
    private Double amount = null;
    private String payeeName = "";
    private String drawer = "";
    private String drawerAcctNo = "";
    private String bankCode = "";
    private String branchCode = "";
    private String remarks = "";
    private String status = "";
    private String instrumentNo1 = "";
    private String instrumentNo2 = "";
    private String scheduleNo = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizeStatus = null;
    private String currency = "";
    private String statusBy = "";
    private String branchId = "";
    private Date statusDt = null;
    private String authorizeRemarks = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String initiatedBranch = "";

    /**
     * Setter/Getter for BATCH_ID - table Field
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchId() {
        return batchId;
    }

    /**
     * Setter/Getter for OUTWARD_ID - table Field
     */
    public void setOutwardId(String outwardId) {
        this.outwardId = outwardId;
    }

    public String getOutwardId() {
        return outwardId;
    }

    /**
     * Setter/Getter for OUTWARD_DT - table Field
     */
    public void setOutwardDt(Date outwardDt) {
        this.outwardDt = outwardDt;
    }

    public Date getOutwardDt() {
        return outwardDt;
    }

    /**
     * Setter/Getter for CLEARING_TYPE - table Field
     */
    public void setClearingType(String clearingType) {
        this.clearingType = clearingType;
    }

    public String getClearingType() {
        return clearingType;
    }

    /**
     * Setter/Getter for INSTRUMENT_TYPE - table Field
     */
    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    /**
     * Setter/Getter for INSTRUMENT_DT - table Field
     */
    public void setInstrumentDt(Date instrumentDt) {
        this.instrumentDt = instrumentDt;
    }

    public Date getInstrumentDt() {
        return instrumentDt;
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

    /**
     * Setter/Getter for PAYEE_NAME - table Field
     */
    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    /**
     * Setter/Getter for DRAWER - table Field
     */
    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public String getDrawer() {
        return drawer;
    }

    /**
     * Setter/Getter for DRAWER_ACCT_NO - table Field
     */
    public void setDrawerAcctNo(String drawerAcctNo) {
        this.drawerAcctNo = drawerAcctNo;
    }

    public String getDrawerAcctNo() {
        return drawerAcctNo;
    }

    /**
     * Setter/Getter for BANK_CODE - table Field
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    /**
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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
     * Setter/Getter for INSTRUMENT_NO1 - table Field
     */
    public void setInstrumentNo1(String instrumentNo1) {
        this.instrumentNo1 = instrumentNo1;
    }

    public String getInstrumentNo1() {
        return instrumentNo1;
    }

    /**
     * Setter/Getter for INSTRUMENT_NO2 - table Field
     */
    public void setInstrumentNo2(String instrumentNo2) {
        this.instrumentNo2 = instrumentNo2;
    }

    public String getInstrumentNo2() {
        return instrumentNo2;
    }

    /**
     * Setter/Getter for SCHEDULE_NO - table Field
     */
    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getScheduleNo() {
        return scheduleNo;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for CURRENCY - table Field
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
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

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
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
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("batchId" + KEY_VAL_SEPARATOR + "outwardId");
        return batchId + KEY_VAL_SEPARATOR + outwardId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("outwardId", outwardId));
        strB.append(getTOString("outwardDt", outwardDt));
        strB.append(getTOString("clearingType", clearingType));
        strB.append(getTOString("instrumentType", instrumentType));
        strB.append(getTOString("instrumentDt", instrumentDt));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("payeeName", payeeName));
        strB.append(getTOString("drawer", drawer));
        strB.append(getTOString("drawerAcctNo", drawerAcctNo));
        strB.append(getTOString("bankCode", bankCode));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("instrumentNo1", instrumentNo1));
        strB.append(getTOString("instrumentNo2", instrumentNo2));
        strB.append(getTOString("scheduleNo", scheduleNo));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("currency", currency));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("batchId", batchId));
        strB.append(getTOXml("outwardId", outwardId));
        strB.append(getTOXml("outwardDt", outwardDt));
        strB.append(getTOXml("clearingType", clearingType));
        strB.append(getTOXml("instrumentType", instrumentType));
        strB.append(getTOXml("instrumentDt", instrumentDt));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("payeeName", payeeName));
        strB.append(getTOXml("drawer", drawer));
        strB.append(getTOXml("drawerAcctNo", drawerAcctNo));
        strB.append(getTOXml("bankCode", bankCode));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("instrumentNo1", instrumentNo1));
        strB.append(getTOXml("instrumentNo2", instrumentNo2));
        strB.append(getTOXml("scheduleNo", scheduleNo));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("currency", currency));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

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
}