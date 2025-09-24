/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FreezeTO.java
 * 
 * Created on Fri Jun 11 17:28:08 IST 2004
 */
package com.see.truetransact.transferobject.operativeaccount;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ACT_FREEZE.
 */
public class FreezeTO extends TransferObject implements Serializable {

    private String actNum = "";
    private String freezeId = "";
    private Double freezeAmt = null;
    private Date freezeDt = null;
    private String freezeType = "";
    private String freezeStatus = "";
    private Date unfreezeDt = null;
    private String remarks = "";
    private String unfreezeRemarks = "";
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private Date createdDt = null;
    private String createdBy = "";

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for FREEZE_ID - table Field
     */
    public void setFreezeId(String freezeId) {
        this.freezeId = freezeId;
    }

    public String getFreezeId() {
        return freezeId;
    }

    /**
     * Setter/Getter for FREEZE_AMT - table Field
     */
    public void setFreezeAmt(Double freezeAmt) {
        this.freezeAmt = freezeAmt;
    }

    public Double getFreezeAmt() {
        return freezeAmt;
    }

    /**
     * Setter/Getter for FREEZE_DT - table Field
     */
    public void setFreezeDt(Date freezeDt) {
        this.freezeDt = freezeDt;
    }

    public Date getFreezeDt() {
        return freezeDt;
    }

    /**
     * Setter/Getter for FREEZE_TYPE - table Field
     */
    public void setFreezeType(String freezeType) {
        this.freezeType = freezeType;
    }

    public String getFreezeType() {
        return freezeType;
    }

    /**
     * Setter/Getter for FREEZE_STATUS - table Field
     */
    public void setFreezeStatus(String freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    public String getFreezeStatus() {
        return freezeStatus;
    }

    /**
     * Setter/Getter for UNFREEZE_DT - table Field
     */
    public void setUnfreezeDt(Date unfreezeDt) {
        this.unfreezeDt = unfreezeDt;
    }

    public Date getUnfreezeDt() {
        return unfreezeDt;
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
     * Setter/Getter for UNFREEZE_REMARKS - table Field
     */
    public void setUnfreezeRemarks(String unfreezeRemarks) {
        this.unfreezeRemarks = unfreezeRemarks;
    }

    public String getUnfreezeRemarks() {
        return unfreezeRemarks;
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
     * Setter/Getter for AUTHORIZE_USER - table Field
     */
    public void setAuthorizeUser(String authorizeUser) {
        this.authorizeUser = authorizeUser;
    }

    public String getAuthorizeUser() {
        return authorizeUser;
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
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
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
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("actNum" + KEY_VAL_SEPARATOR + "freezeId");
        return (actNum + KEY_VAL_SEPARATOR + freezeId);
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("freezeId", freezeId));
        strB.append(getTOString("freezeAmt", freezeAmt));
        strB.append(getTOString("freezeDt", freezeDt));
        strB.append(getTOString("freezeType", freezeType));
        strB.append(getTOString("freezeStatus", freezeStatus));
        strB.append(getTOString("unfreezeDt", unfreezeDt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("unfreezeRemarks", unfreezeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("freezeId", freezeId));
        strB.append(getTOXml("freezeAmt", freezeAmt));
        strB.append(getTOXml("freezeDt", freezeDt));
        strB.append(getTOXml("freezeType", freezeType));
        strB.append(getTOXml("freezeStatus", freezeStatus));
        strB.append(getTOXml("unfreezeDt", unfreezeDt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("unfreezeRemarks", unfreezeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}