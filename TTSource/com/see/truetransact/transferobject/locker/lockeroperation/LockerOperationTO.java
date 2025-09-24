/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigTO.java
 * 
 * Created on Thu Jan 20 16:44:08 IST 2005
 */
package com.see.truetransact.transferobject.locker.lockeroperation;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class LockerOperationTO extends TransferObject implements Serializable {

    private String optId = "";
    private String locNum = "";
    private Date optDt = null;
    private String custId = "";
    private String branchID = "";
    private String optMode = "";
    private String createdBy = "";
    private String lockerOutBy = "";
    private Date lockerOutDt = null;
    private String authorizeBy = "";
    private String authorizeStatus = null;;
    private Date authorizeDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String acctName = "";
    private String remarks = "";
    private String lblConstitutionVal = "";

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
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(optId);
        return optId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("optId", optId));
        strB.append(getTOString("locNum", locNum));
        strB.append(getTOString("optDt", optDt));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("branchID", branchID));
        strB.append(getTOString("optMode", optMode));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("lockerOutBy", lockerOutBy));
        strB.append(getTOString("lockerOutDt", lockerOutDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("acctName", acctName));
        strB.append(getTOString("lblConstitutionVal", lblConstitutionVal));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("optId", optId));
        strB.append(getTOXml("locNum", locNum));
        strB.append(getTOXml("optDt", optDt));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("branchID", branchID));
        strB.append(getTOXml("optMode", optMode));
        strB.append(getTOXml("lockerOutBy", lockerOutBy));
        strB.append(getTOXml("lockerOutDt", lockerOutDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("acctName", acctName));
        strB.append(getTOXml("lblConstitutionVal", lblConstitutionVal));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property optId.
     *
     * @return Value of property optId.
     */
    public java.lang.String getOptId() {
        return optId;
    }

    /**
     * Setter for property optId.
     *
     * @param optId New value of property optId.
     */
    public void setOptId(java.lang.String optId) {
        this.optId = optId;
    }

    /**
     * Getter for property locNum.
     *
     * @return Value of property locNum.
     */
    public java.lang.String getLocNum() {
        return locNum;
    }

    /**
     * Setter for property locNum.
     *
     * @param locNum New value of property locNum.
     */
    public void setLocNum(java.lang.String locNum) {
        this.locNum = locNum;
    }

    /**
     * Getter for property optDt.
     *
     * @return Value of property optDt.
     */
    public Date getOptDt() {
        return optDt;
    }

    /**
     * Setter for property optDt.
     *
     * @param optDt New value of property optDt.
     */
    public void setOptDt(Date optDt) {
        this.optDt = optDt;
    }

    /**
     * Getter for property custId.
     *
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }

    /**
     * Setter for property custId.
     *
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
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
     * Getter for property optMode.
     *
     * @return Value of property optMode.
     */
    public java.lang.String getOptMode() {
        return optMode;
    }

    /**
     * Setter for property optMode.
     *
     * @param optMode New value of property optMode.
     */
    public void setOptMode(java.lang.String optMode) {
        this.optMode = optMode;
    }

    /**
     * Getter for property lockerOutBy.
     *
     * @return Value of property lockerOutBy.
     */
    public java.lang.String getLockerOutBy() {
        return lockerOutBy;
    }

    /**
     * Setter for property lockerOutBy.
     *
     * @param lockerOutBy New value of property lockerOutBy.
     */
    public void setLockerOutBy(java.lang.String lockerOutBy) {
        this.lockerOutBy = lockerOutBy;
    }

    public Date getLockerOutDt() {
        return lockerOutDt;
    }

    /**
     * Setter for property optDt.
     *
     * @param optDt New value of property optDt.
     */
    public void setLockerOutDt(Date lockerOutDt) {
        this.lockerOutDt = lockerOutDt;
    }

    /**
     * Getter for property lockerOutDt.
     *
     * @return Value of property lockerOutDt.
     */
//        public java.util.Date getLockerOutDt() {
//            return lockerOutDt;
//        }
//        
//        /**
//         * Setter for property lockerOutDt.
//         * @param lockerOutDt New value of property lockerOutDt.
//         */
//        public void setLockerOutDt(java.util.Date lockerOutDt) {
//            this.lockerOutDt = lockerOutDt;
//        }
    /**
     * Getter for property acctName.
     *
     * @return Value of property acctName.
     */
    public java.lang.String getAcctName() {
        return acctName;
    }

    /**
     * Setter for property acctName.
     *
     * @param acctName New value of property acctName.
     */
    public void setAcctName(java.lang.String acctName) {
        this.acctName = acctName;
    }

    public void setLblConstitutionVal(java.lang.String lblConstitutionVal) {
        this.lblConstitutionVal = lblConstitutionVal;
    }

    public String getLblConstitutionVal() {
        return lblConstitutionVal;
    }

    /**
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }
}