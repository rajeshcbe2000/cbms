/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSExemptionTO.java
 * 
 * Created on Thu Feb 03 10:31:19 IST 2005
 */
package com.see.truetransact.transferobject.tds.tdsexemption;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TDS_EXEMPTION.
 */
public class TDSExemptionTO extends TransferObject implements Serializable {

    private String custId = "";
    private Date exemRecDt = null;
    private String certRefNo = "";
    private Date exemStartDt = null;
    private Date exemEndDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String exemId = "";
    private String branchId = "";
    private String panNo = "";
    private String remarks = "";

    /**
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
    }

    /**
     * Setter/Getter for EXEM_REC_DT - table Field
     */
    public void setExemRecDt(Date exemRecDt) {
        this.exemRecDt = exemRecDt;
    }

    public Date getExemRecDt() {
        return exemRecDt;
    }

    /**
     * Setter/Getter for CERT_REF_NO - table Field
     */
    public void setCertRefNo(String certRefNo) {
        this.certRefNo = certRefNo;
    }

    public String getCertRefNo() {
        return certRefNo;
    }

    /**
     * Setter/Getter for EXEM_START_DT - table Field
     */
    public void setExemStartDt(Date exemStartDt) {
        this.exemStartDt = exemStartDt;
    }

    public Date getExemStartDt() {
        return exemStartDt;
    }

    /**
     * Setter/Getter for EXEM_END_DT - table Field
     */
    public void setExemEndDt(Date exemEndDt) {
        this.exemEndDt = exemEndDt;
    }

    public Date getExemEndDt() {
        return exemEndDt;
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
     * Setter/Getter for EXEM_ID - table Field
     */
    public void setExemId(String exemId) {
        this.exemId = exemId;
    }

    public String getExemId() {
        return exemId;
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
     * Setter/Getter for PAN_NO - table Field
     */
    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getPanNo() {
        return panNo;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(exemId);
        return exemId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("exemRecDt", exemRecDt));
        strB.append(getTOString("certRefNo", certRefNo));
        strB.append(getTOString("exemStartDt", exemStartDt));
        strB.append(getTOString("exemEndDt", exemEndDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("exemId", exemId));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("panNo", panNo));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("exemRecDt", exemRecDt));
        strB.append(getTOXml("certRefNo", certRefNo));
        strB.append(getTOXml("exemStartDt", exemStartDt));
        strB.append(getTOXml("exemEndDt", exemEndDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("exemId", exemId));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("panNo", panNo));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}