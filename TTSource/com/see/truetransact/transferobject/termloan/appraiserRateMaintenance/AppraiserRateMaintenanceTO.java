/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGroupTO.java
 * 
 * Created on Thu Aug 25 12:46:29 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.termloan.appraiserRateMaintenance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BRANCH_GROUP.
 */
public class AppraiserRateMaintenanceTO extends TransferObject implements Serializable {

    private String groupId = "";
    private Date fromDate = null;
    private Date toDate = null;
    private String fromAmt = "";
    private String toAmt = "";
    private String amount = "";
    private String percentage = "";
    private String serviceTax = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//	 public String getKeyData() {
//		setKeyColumns(branchGroupId);
//		return branchGroupId;
//	}
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//		strB.append (getTOStringKey(getKeyData()));
        strB.append(getTOString("groupId", groupId));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("fromAmt", fromAmt));
        strB.append(getTOString("toAmt", toAmt));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("percentage", percentage));
        strB.append(getTOString("serviceTax", serviceTax));
        strB.append(getTOString("status", status));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
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
//		strB.append (getTOXmlKey(getKeyData()));
        strB.append(getTOXml("groupId", groupId));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("fromAmt", fromAmt));
        strB.append(getTOXml("toAmt", toAmt));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("percentage", percentage));
        strB.append(getTOXml("serviceTax", serviceTax));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property fromDate.
     *
     * @return Value of property fromDate.
     */
    public java.util.Date getFromDate() {
        return fromDate;
    }

    /**
     * Setter for property fromDate.
     *
     * @param fromDate New value of property fromDate.
     */
    public void setFromDate(java.util.Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Getter for property toDate.
     *
     * @return Value of property toDate.
     */
    public java.util.Date getToDate() {
        return toDate;
    }

    /**
     * Setter for property toDate.
     *
     * @param toDate New value of property toDate.
     */
    public void setToDate(java.util.Date toDate) {
        this.toDate = toDate;
    }

    /**
     * Getter for property fromAmt.
     *
     * @return Value of property fromAmt.
     */
    public java.lang.String getFromAmt() {
        return fromAmt;
    }

    /**
     * Setter for property fromAmt.
     *
     * @param fromAmt New value of property fromAmt.
     */
    public void setFromAmt(java.lang.String fromAmt) {
        this.fromAmt = fromAmt;
    }

    /**
     * Getter for property toAmt.
     *
     * @return Value of property toAmt.
     */
    public java.lang.String getToAmt() {
        return toAmt;
    }

    /**
     * Setter for property toAmt.
     *
     * @param toAmt New value of property toAmt.
     */
    public void setToAmt(java.lang.String toAmt) {
        this.toAmt = toAmt;
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     */
    public java.lang.String getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     */
    public void setAmount(java.lang.String amount) {
        this.amount = amount;
    }

    /**
     * Getter for property percentage.
     *
     * @return Value of property percentage.
     */
    public java.lang.String getPercentage() {
        return percentage;
    }

    /**
     * Setter for property percentage.
     *
     * @param percentage New value of property percentage.
     */
    public void setPercentage(java.lang.String percentage) {
        this.percentage = percentage;
    }

    /**
     * Getter for property serviceTax.
     *
     * @return Value of property serviceTax.
     */
    public java.lang.String getServiceTax() {
        return serviceTax;
    }

    /**
     * Setter for property serviceTax.
     *
     * @param serviceTax New value of property serviceTax.
     */
    public void setServiceTax(java.lang.String serviceTax) {
        this.serviceTax = serviceTax;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property createdDt.
     *
     * @param createdDt New value of property createdDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
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

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
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
     * Getter for property groupId.
     *
     * @return Value of property groupId.
     */
    public java.lang.String getGroupId() {
        return groupId;
    }

    /**
     * Setter for property groupId.
     *
     * @param groupId New value of property groupId.
     */
    public void setGroupId(java.lang.String groupId) {
        this.groupId = groupId;
    }
}