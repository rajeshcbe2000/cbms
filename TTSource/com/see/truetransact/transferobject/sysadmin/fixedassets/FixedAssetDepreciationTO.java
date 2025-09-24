/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetDepreciationTO.java
 * 
 * Created on Tue Jan 25 11:15:13 IST 2011
 */
package com.see.truetransact.transferobject.sysadmin.fixedassets;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is FIXED_ASSET_DEPRECIATION.
 */
public class FixedAssetDepreciationTO extends TransferObject implements Serializable {

    private String faId = "";
    private Date depreciationDate = null;
    private String depreciationValue = "";
    private String newCurrentValue = "";
    private String deprBatchId = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String branchCode = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizedStatus = null;
    private String status = "";

    /**
     * Setter/Getter for FA_ID - table Field
     */
    public void setFaId(String faId) {
        this.faId = faId;
    }

    public String getFaId() {
        return faId;
    }

    /**
     * Setter/Getter for DEPRECIATION_DATE - table Field
     */
    public void setDepreciationDate(Date depreciationDate) {
        this.depreciationDate = depreciationDate;
    }

    public Date getDepreciationDate() {
        return depreciationDate;
    }

    /**
     * Setter/Getter for DEPRECIATION_VALUE - table Field
     */
    public void setDepreciationValue(String depreciationValue) {
        this.depreciationValue = depreciationValue;
    }

    public String getDepreciationValue() {
        return depreciationValue;
    }

    /**
     * Setter/Getter for NEW_CURRENT_VALUE - table Field
     */
    public void setNewCurrentValue(String newCurrentValue) {
        this.newCurrentValue = newCurrentValue;
    }

    public String getNewCurrentValue() {
        return newCurrentValue;
    }

    /**
     * Setter/Getter for DEPR_BATCH_ID - table Field
     */
    public void setDeprBatchId(String deprBatchId) {
        this.deprBatchId = deprBatchId;
    }

    public String getDeprBatchId() {
        return deprBatchId;
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
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
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
        strB.append(getTOString("faId", faId));
        strB.append(getTOString("depreciationDate", depreciationDate));
        strB.append(getTOString("depreciationValue", depreciationValue));
        strB.append(getTOString("newCurrentValue", newCurrentValue));
        strB.append(getTOString("deprBatchId", deprBatchId));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("faId", faId));
        strB.append(getTOXml("depreciationDate", depreciationDate));
        strB.append(getTOXml("depreciationValue", depreciationValue));
        strB.append(getTOXml("newCurrentValue", newCurrentValue));
        strB.append(getTOXml("deprBatchId", deprBatchId));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    /**
     * Getter for property authorizedStatus.
     *
     * @return Value of property authorizedStatus.
     */
    public java.lang.String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter for property authorizedStatus.
     *
     * @param authorizedStatus New value of property authorizedStatus.
     */
    public void setAuthorizedStatus(java.lang.String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }
}