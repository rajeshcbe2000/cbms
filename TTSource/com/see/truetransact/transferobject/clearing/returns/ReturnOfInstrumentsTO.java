/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReturnOfInstrumentsTO.java
 * 
 * Created on Fri Jul 29 15:02:25 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.clearing.returns;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OUTWARD_RETURN.
 */
public class ReturnOfInstrumentsTO extends TransferObject implements Serializable {

    private String returnType = "";
    private String presentAgain = "";
    private String clearingType = "";
    private Date clearingDate = null;
    private String status = "";
    private String batchId = "";
    private String returnId = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizeStatus = null;
    private String authorizeRemarks = "";
    private String branchId = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String instrumentNo1 = "";
    private String instrumentNo2 = "";
    private String scheduleNo = "";
    private Double amount = null;
    private String initiatedBranch = "";
    private Double charge = null;
    private String acctNo = "";

    /**
     * Setter/Getter for RETURN_TYPE - table Field
     */
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnType() {
        return returnType;
    }

    /**
     * Setter/Getter for PRESENT_AGAIN - table Field
     */
    public void setPresentAgain(String presentAgain) {
        this.presentAgain = presentAgain;
    }

    public String getPresentAgain() {
        return presentAgain;
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
     * Setter/Getter for CLEARING_DATE - table Field
     */
    public void setClearingDate(Date clearingDate) {
        this.clearingDate = clearingDate;
    }

    public Date getClearingDate() {
        return clearingDate;
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
     * Setter/Getter for BATCH_ID - table Field
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchId() {
        return batchId;
    }

    /**
     * Setter/Getter for RETURN_ID - table Field
     */
    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public String getReturnId() {
        return returnId;
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
     * Setter/Getter for AUTHORIZE_REMARKS - table Field
     */
    public void setAuthorizeRemarks(String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    public String getAuthorizeRemarks() {
        return authorizeRemarks;
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
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
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
     * Setter/Getter for AMOUNT - table Field
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    /**
     * Setter/Getter for INITIATED_BRANCH - table Field
     */
    public void setInitiatedBranch(String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }

    public String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(returnId);
        return returnId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("returnType", returnType));
        strB.append(getTOString("presentAgain", presentAgain));
        strB.append(getTOString("clearingType", clearingType));
        strB.append(getTOString("clearingDate", clearingDate));
        strB.append(getTOString("status", status));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("returnId", returnId));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("instrumentNo1", instrumentNo1));
        strB.append(getTOString("instrumentNo2", instrumentNo2));
        strB.append(getTOString("scheduleNo", scheduleNo));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("charge", charge));
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("returnType", returnType));
        strB.append(getTOXml("presentAgain", presentAgain));
        strB.append(getTOXml("clearingType", clearingType));
        strB.append(getTOXml("clearingDate", clearingDate));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("batchId", batchId));
        strB.append(getTOXml("returnId", returnId));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("instrumentNo1", instrumentNo1));
        strB.append(getTOXml("instrumentNo2", instrumentNo2));
        strB.append(getTOXml("scheduleNo", scheduleNo));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("charge", charge));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property charge.
     *
     * @return Value of property charge.
     */
    public java.lang.Double getCharge() {
        return charge;
    }

    /**
     * Setter for property charge.
     *
     * @param charge New value of property charge.
     */
    public void setCharge(java.lang.Double charge) {
        this.charge = charge;
    }

    /**
     * Getter for property acctNo.
     *
     * @return Value of property acctNo.
     */
    public java.lang.String getAcctNo() {
        return acctNo;
    }

    /**
     * Setter for property acctNo.
     *
     * @param acctNo New value of property acctNo.
     */
    public void setAcctNo(java.lang.String acctNo) {
        this.acctNo = acctNo;
    }
}