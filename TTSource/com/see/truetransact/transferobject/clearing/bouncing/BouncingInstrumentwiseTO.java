/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BouncingInstrumentwiseTO.java
 * 
 * Created on Wed Mar 16 13:26:51 IST 2005
 */
package com.see.truetransact.transferobject.clearing.bouncing;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is INWARD_BOUNCING.
 */
public class BouncingInstrumentwiseTO extends TransferObject implements Serializable {

    private String bouncingId = "";
    private String bouncingType = "";
    private String inwardId = "";
    private String inwardScheduleNo = "";
    private String bouncingReason = "";
    private String presentAgain = "";
    private String clearingType = "";
    private Date clearingDate = null;
    private String status = "";
    private Date createdDt = null;
    private String branchId = "";
    private String createdBy = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private Double charge = null;
    private String scheduleNo = "";
    private Double amount = null;

    /**
     * Setter/Getter for BOUNCING_ID - table Field
     */
    public void setBouncingId(String bouncingId) {
        this.bouncingId = bouncingId;
    }

    public String getBouncingId() {
        return bouncingId;
    }

    /**
     * Setter/Getter for BOUNCING_TYPE - table Field
     */
    public void setBouncingType(String bouncingType) {
        this.bouncingType = bouncingType;
    }

    public String getBouncingType() {
        return bouncingType;
    }

    /**
     * Setter/Getter for INWARD_ID - table Field
     */
    public void setInwardId(String inwardId) {
        this.inwardId = inwardId;
    }

    public String getInwardId() {
        return inwardId;
    }

    /**
     * Setter/Getter for INWARD_SCHEDULE_NO - table Field
     */
    public void setInwardScheduleNo(String inwardScheduleNo) {
        this.inwardScheduleNo = inwardScheduleNo;
    }

    public String getInwardScheduleNo() {
        return inwardScheduleNo;
    }

    /**
     * Setter/Getter for BOUNCING_REASON - table Field
     */
    public void setBouncingReason(String bouncingReason) {
        this.bouncingReason = bouncingReason;
    }

    public String getBouncingReason() {
        return bouncingReason;
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
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
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
        setKeyColumns("bouncingId");
        return bouncingId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("bouncingId", bouncingId));
        strB.append(getTOString("bouncingType", bouncingType));
        strB.append(getTOString("inwardId", inwardId));
        strB.append(getTOString("inwardScheduleNo", inwardScheduleNo));
        strB.append(getTOString("bouncingReason", bouncingReason));
        strB.append(getTOString("presentAgain", presentAgain));
        strB.append(getTOString("clearingType", clearingType));
        strB.append(getTOString("clearingDate", clearingDate));
        strB.append(getTOString("status", status));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("charge", charge));
        strB.append(getTOString("scheduleNo", scheduleNo));
        strB.append(getTOString("amount", amount));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("bouncingId", bouncingId));
        strB.append(getTOXml("bouncingType", bouncingType));
        strB.append(getTOXml("inwardId", inwardId));
        strB.append(getTOXml("inwardScheduleNo", inwardScheduleNo));
        strB.append(getTOXml("bouncingReason", bouncingReason));
        strB.append(getTOXml("presentAgain", presentAgain));
        strB.append(getTOXml("clearingType", clearingType));
        strB.append(getTOXml("clearingDate", clearingDate));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("charge", charge));
        strB.append(getTOXml("scheduleNo", scheduleNo));
        strB.append(getTOXml("amount", amount));
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
     * Getter for property scheduleNo.
     *
     * @return Value of property scheduleNo.
     */
    public java.lang.String getScheduleNo() {
        return scheduleNo;
    }

    /**
     * Setter for property scheduleNo.
     *
     * @param scheduleNo New value of property scheduleNo.
     */
    public void setScheduleNo(java.lang.String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     */
    public java.lang.Double getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     */
    public void setAmount(java.lang.Double amount) {
        this.amount = amount;
    }
}