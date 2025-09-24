/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositFreezeTO.java
 * 
 * Created on Wed Jun 09 19:25:37 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit.freeze;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_FREEZE.
 */
public class DepositFreezeTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private Integer depositSubNo = 0;
    private String fslNo = "";
    private Double amount = null;
    private Date freezeDt = null;
    private String type = "";
    private String remarks = "";
    private Date unfreezeDt = null;
    private String status = "";
    private String authorizeStatus = null;
    private Date authorizeDt = null;
    private String authorizeBy = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String unfreezeRemarks = "";

    /**
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
    }

    public Integer getDepositSubNo() {
        return depositSubNo;
    }

    public void setDepositSubNo(Integer depositSubNo) {
        this.depositSubNo = depositSubNo;
    }

  

    /**
     * Setter/Getter for FSL_NO - table Field
     */
    public void setFslNo(String fslNo) {
        this.fslNo = fslNo;
    }

    public String getFslNo() {
        return fslNo;
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
     * Setter/Getter for FREEZE_DT - table Field
     */
    public void setFreezeDt(Date freezeDt) {
        this.freezeDt = freezeDt;
    }

    public Date getFreezeDt() {
        return freezeDt;
    }

    /**
     * Setter/Getter for TYPE - table Field
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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
     * Setter/Getter for UNFREEZE_DT - table Field
     */
    public void setUnfreezeDt(Date unfreezeDt) {
        this.unfreezeDt = unfreezeDt;
    }

    public Date getUnfreezeDt() {
        return unfreezeDt;
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
     * Setter/Getter for UNFREEZE_REMARKS - table Field
     */
    public void setUnfreezeRemarks(String unfreezeRemarks) {
        this.unfreezeRemarks = unfreezeRemarks;
    }

    public String getUnfreezeRemarks() {
        return unfreezeRemarks;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("fslNo");
        return fslNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("depositSubNo", depositSubNo));
        strB.append(getTOString("fslNo", fslNo));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("freezeDt", freezeDt));
        strB.append(getTOString("type", type));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("unfreezeDt", unfreezeDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("unfreezeRemarks", unfreezeRemarks));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("depositSubNo", depositSubNo));
        strB.append(getTOXml("fslNo", fslNo));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("freezeDt", freezeDt));
        strB.append(getTOXml("type", type));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("unfreezeDt", unfreezeDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("unfreezeRemarks", unfreezeRemarks));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}