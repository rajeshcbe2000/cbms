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
package com.see.truetransact.transferobject.remittance.remitstoppayment;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DD_STOP_PAYMENT.
 */
public class RemitStopPaymentTO extends TransferObject implements Serializable {

    private String stopPaymentId = "";
    private String prodId = "";
    private String ddLeafType = "";
    private String startDdNo1 = "";
    private String startDdNo2 = "";
    private String endDdNo1 = "";
    private String endDdNo2 = "";
    private String startVariableNo = "";
    private String endVariableNo = "";
    private String remarks = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private Date authorizeDt = null;
    private String authorizeBy = "";
    private String stopStatus = "";
    private String branchId = "";
    private String revokeRemark = "";
    private Date stopStatusDt = null;
    private Date revokeStatusDt = null;

    /**
     * Setter/Getter for STOP_PAYMENT_ID - table Field
     */
    public void setStopPaymentId(String stopPaymentId) {
        this.stopPaymentId = stopPaymentId;
    }

    public String getStopPaymentId() {
        return stopPaymentId;
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
    public void setStartDdNo1(String startDdNo1) {
        this.startDdNo1 = startDdNo1;
    }

    public String getStartDdNo1() {
        return startDdNo1;
    }

    /**
     * Setter/Getter for START_DD_NO2 - table Field
     */
    public void setStartDdNo2(String startDdNo2) {
        this.startDdNo2 = startDdNo2;
    }

    public String getStartDdNo2() {
        return startDdNo2;
    }

    /**
     * Setter/Getter for END_DD_NO1 - table Field
     */
    public void setEndDdNo1(String endDdNo1) {
        this.endDdNo1 = endDdNo1;
    }

    public String getEndDdNo1() {
        return endDdNo1;
    }

    /**
     * Setter/Getter for END_DD_NO2 - table Field
     */
    public void setEndDdNo2(String endDdNo2) {
        this.endDdNo2 = endDdNo2;
    }

    public String getEndDdNo2() {
        return endDdNo2;
    }

    /**
     * Setter/Getter for START_VARIABLE_NO - table Field
     */
    public void setStartVariableNo(String startVariableNo) {
        this.startVariableNo = startVariableNo;
    }

    public String getStartVariableNo() {
        return startVariableNo;
    }

    /**
     * Setter/Getter for END_VARIABLE_NO - table Field
     */
    public void setEndVariableNo(String endVariableNo) {
        this.endVariableNo = endVariableNo;
    }

    public String getEndVariableNo() {
        return endVariableNo;
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
     * Setter/Getter for STOP_STATUS - table Field
     */
    public void setStopStatus(String stopStatus) {
        this.stopStatus = stopStatus;
    }

    public String getStopStatus() {
        return stopStatus;
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
        setKeyColumns("stopPaymentId");
        return stopPaymentId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("stopPaymentId", stopPaymentId));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("ddLeafType", ddLeafType));
        strB.append(getTOString("startDdNo1", startDdNo1));
        strB.append(getTOString("startDdNo2", startDdNo2));
        strB.append(getTOString("endDdNo1", endDdNo1));
        strB.append(getTOString("endDdNo2", endDdNo2));
        strB.append(getTOString("startVariableNo", startVariableNo));
        strB.append(getTOString("endVariableNo", endVariableNo));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("stopStatus", stopStatus));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("revokeRemark", revokeRemark));
        strB.append(getTOString("stopStatusDt", revokeRemark));
        strB.append(getTOString("revokeStatusDt", revokeRemark));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("stopPaymentId", stopPaymentId));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("ddLeafType", ddLeafType));
        strB.append(getTOXml("startDdNo1", startDdNo1));
        strB.append(getTOXml("startDdNo2", startDdNo2));
        strB.append(getTOXml("endDdNo1", endDdNo1));
        strB.append(getTOXml("endDdNo2", endDdNo2));
        strB.append(getTOXml("startVariableNo", startVariableNo));
        strB.append(getTOXml("endVariableNo", endVariableNo));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("stopStatus", stopStatus));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("revokeRemark", revokeRemark));
        strB.append(getTOXml("stopStatusDt", revokeRemark));
        strB.append(getTOXml("revokeStatusDt", revokeRemark));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property revokeRemark.
     *
     * @return Value of property revokeRemark.
     */
    public java.lang.String getRevokeRemark() {
        return revokeRemark;
    }

    /**
     * Setter for property revokeRemark.
     *
     * @param revokeRemark New value of property revokeRemark.
     */
    public void setRevokeRemark(java.lang.String revokeRemark) {
        this.revokeRemark = revokeRemark;
    }

    /**
     * Getter for property stopStatusDt.
     *
     * @return Value of property stopStatusDt.
     */
    public java.util.Date getStopStatusDt() {
        return stopStatusDt;
    }

    /**
     * Setter for property stopStatusDt.
     *
     * @param stopStatusDt New value of property stopStatusDt.
     */
    public void setStopStatusDt(java.util.Date stopStatusDt) {
        this.stopStatusDt = stopStatusDt;
    }

    /**
     * Getter for property revokeStatusDt.
     *
     * @return Value of property revokeStatusDt.
     */
    public java.util.Date getRevokeStatusDt() {
        return revokeStatusDt;
    }

    /**
     * Setter for property revokeStatusDt.
     *
     * @param revokeStatusDt New value of property revokeStatusDt.
     */
    public void setRevokeStatusDt(java.util.Date revokeStatusDt) {
        this.revokeStatusDt = revokeStatusDt;
    }
}