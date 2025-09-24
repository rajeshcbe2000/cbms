/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ParameterTO.java
 * 
 * Created on Wed Jan 19 15:58:31 IST 2005
 */
package com.see.truetransact.transferobject.clearing;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CLEARING_PARAM.
 */
public class ParameterTO extends TransferObject implements Serializable {

    private String clearingType = "";
    private Double lotsizeMicrClearing = null;
    private String highValAppl = "";
    private String serviceBranchCode = "";
    private Double highValCheque = null;
    private String status = "";
    private String branchId = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private Double clearingPeriod = null;

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
     * Setter/Getter for LOTSIZE_MICR_CLEARING - table Field
     */
    public void setLotsizeMicrClearing(Double lotsizeMicrClearing) {
        this.lotsizeMicrClearing = lotsizeMicrClearing;
    }

    public Double getLotsizeMicrClearing() {
        return lotsizeMicrClearing;
    }

    /**
     * Setter/Getter for HIGH_VAL_APPL - table Field
     */
    public void setHighValAppl(String highValAppl) {
        this.highValAppl = highValAppl;
    }

    public String getHighValAppl() {
        return highValAppl;
    }

    /**
     * Setter/Getter for SERVICE_BRANCH_CODE - table Field
     */
    public void setServiceBranchCode(String serviceBranchCode) {
        this.serviceBranchCode = serviceBranchCode;
    }

    public String getServiceBranchCode() {
        return serviceBranchCode;
    }

    /**
     * Setter/Getter for HIGH_VAL_CHEQUE - table Field
     */
    public void setHighValCheque(Double highValCheque) {
        this.highValCheque = highValCheque;
    }

    public Double getHighValCheque() {
        return highValCheque;
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
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("clearingType", clearingType));
        strB.append(getTOString("lotsizeMicrClearing", lotsizeMicrClearing));
        strB.append(getTOString("highValAppl", highValAppl));
        strB.append(getTOString("serviceBranchCode", serviceBranchCode));
        strB.append(getTOString("highValCheque", highValCheque));
        strB.append(getTOString("status", status));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("clearingType", clearingType));
        strB.append(getTOXml("lotsizeMicrClearing", lotsizeMicrClearing));
        strB.append(getTOXml("highValAppl", highValAppl));
        strB.append(getTOXml("serviceBranchCode", serviceBranchCode));
        strB.append(getTOXml("highValCheque", highValCheque));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property clearingPeriod.
     *
     * @return Value of property clearingPeriod.
     */
    public java.lang.Double getClearingPeriod() {
        return clearingPeriod;
    }

    /**
     * Setter for property clearingPeriod.
     *
     * @param clearingPeriod New value of property clearingPeriod.
     */
    public void setClearingPeriod(java.lang.Double clearingPeriod) {
        this.clearingPeriod = clearingPeriod;
    }
}