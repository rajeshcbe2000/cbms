/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitStopPaymentTo.java
 * 
 * Created on Tue Jan 25 10:09:14 IST 2005
 */
package com.see.truetransact.transferobject.supporting.InventoryMovement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MISSING_INSTRUMENTS.
 */
public class InventoryMovementTO extends TransferObject implements Serializable {

    private String id = "";
    private String prodId = "";
    private String ddLeafType = "";
    private String startNo1 = "";
    private String startNo2 = "";
    private String endNo1 = "";
    private String endNo2 = "";
    private String remarks = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String authorizeStatus = null;;
    private Date authorizeDt = null;
    private String authorizeBy = "";
    private String branchId = "";
    private Date missingDt = null;
    private String reason = "";
    private String statusBy = "";
    private Date statusDt = null;

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
     * Setter/Getter for DD_LEAF_TYPE - table Field
     */
    public void setDdLeafType(String ddLeafType) {
        this.ddLeafType = ddLeafType;
    }

    public String getDdLeafType() {
        return ddLeafType;
    }

    /**
     * Setter/Getter for START_DD_NO1 - table Field
     */
    public void setStartNo1(String startNo1) {
        this.startNo1 = startNo1;
    }

    public String getStartNo1() {
        return startNo1;
    }

    /**
     * Setter/Getter for START_DD_NO2 - table Field
     */
    public void setStartNo2(String startNo2) {
        this.startNo2 = startNo2;
    }

    public String getStartNo2() {
        return startNo2;
    }

    /**
     * Setter/Getter for END_DD_NO1 - table Field
     */
    public void setEndNo1(String endNo1) {
        this.endNo1 = endNo1;
    }

    public String getEndNo1() {
        return endNo1;
    }

    /**
     * Setter/Getter for END_DD_NO2 - table Field
     */
    public void setEndNo2(String endNo2) {
        this.endNo2 = endNo2;
    }

    public String getEndNo2() {
        return endNo2;
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
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
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
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
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
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("id");
        return id;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("id", id));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("ddLeafType", ddLeafType));
        strB.append(getTOString("startNo1", startNo1));
        strB.append(getTOString("startNo2", startNo2));
        strB.append(getTOString("endNo1", endNo1));
        strB.append(getTOString("endNo2", endNo2));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("missingDt", missingDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("id", id));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("ddLeafType", ddLeafType));
        strB.append(getTOXml("startNo1", startNo1));
        strB.append(getTOXml("startNo2", startNo2));
        strB.append(getTOXml("endNo1", endNo1));
        strB.append(getTOXml("endNo2", endNo2));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("missingDt", missingDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property missingDt.
     *
     * @return Value of property missingDt.
     */
    public java.util.Date getMissingDt() {
        return missingDt;
    }

    /**
     * Setter for property missingDt.
     *
     * @param missingDt New value of property missingDt.
     */
    public void setMissingDt(java.util.Date missingDt) {
        this.missingDt = missingDt;
    }

    /**
     * Getter for property reason.
     *
     * @return Value of property reason.
     */
    public java.lang.String getReason() {
        return reason;
    }

    /**
     * Setter for property reason.
     *
     * @param reason New value of property reason.
     */
    public void setReason(java.lang.String reason) {
        this.reason = reason;
    }

    /**
     * Getter for property id.
     *
     * @return Value of property id.
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Setter for property id.
     *
     * @param id New value of property id.
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }
}