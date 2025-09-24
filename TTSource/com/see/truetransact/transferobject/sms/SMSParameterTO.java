/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SMSParameterTO.java
 * 
 * Created on Fri May 04 13:25:49 IST 2012
 */
package com.see.truetransact.transferobject.sms;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SMS_PARAMETER.
 */
public class SMSParameterTO extends TransferObject implements Serializable {

    private String prodType = "";
    private String prodId = "";
    private String drCash = "";
    private String crCash = "";
    private String drTransfer = "";
    private String crTransfer = "";
    private String drClearing = "";
    private String crClearing = "";
    private Double drCashAmt = null;
    private Double crCashAmt = null;
    private Double drTransferAmt = null;
    private Double crTransferAmt = null;
    private Double drClearingAmt = null;
    private Double crClearingAmt = null;
    private String remarks = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizeStatus = null;;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String smsAlert = "";
    private String reminder ="N"; // Added by nithya
    private String txnAllowed = "N";
    /**
     * Setter/Getter for PROD_TYPE - table Field
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
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
     * Setter/Getter for DR_CASH - table Field
     */
    public void setDrCash(String drCash) {
        this.drCash = drCash;
    }

    public String getDrCash() {
        return drCash;
    }

    /**
     * Setter/Getter for CR_CASH - table Field
     */
    public void setCrCash(String crCash) {
        this.crCash = crCash;
    }

    public String getCrCash() {
        return crCash;
    }

    /**
     * Setter/Getter for DR_TRANSFER - table Field
     */
    public void setDrTransfer(String drTransfer) {
        this.drTransfer = drTransfer;
    }

    public String getDrTransfer() {
        return drTransfer;
    }

    /**
     * Setter/Getter for CR_TRANSFER - table Field
     */
    public void setCrTransfer(String crTransfer) {
        this.crTransfer = crTransfer;
    }

    public String getCrTransfer() {
        return crTransfer;
    }

    /**
     * Setter/Getter for DR_CLEARING - table Field
     */
    public void setDrClearing(String drClearing) {
        this.drClearing = drClearing;
    }

    public String getDrClearing() {
        return drClearing;
    }

    /**
     * Setter/Getter for CR_CLEARING - table Field
     */
    public void setCrClearing(String crClearing) {
        this.crClearing = crClearing;
    }

    public String getCrClearing() {
        return crClearing;
    }

    /**
     * Setter/Getter for DR_CASH_AMT - table Field
     */
    public void setDrCashAmt(Double drCashAmt) {
        this.drCashAmt = drCashAmt;
    }

    public Double getDrCashAmt() {
        return drCashAmt;
    }

    /**
     * Setter/Getter for CR_CASH_AMT - table Field
     */
    public void setCrCashAmt(Double crCashAmt) {
        this.crCashAmt = crCashAmt;
    }

    public Double getCrCashAmt() {
        return crCashAmt;
    }

    /**
     * Setter/Getter for DR_TRANSFER_AMT - table Field
     */
    public void setDrTransferAmt(Double drTransferAmt) {
        this.drTransferAmt = drTransferAmt;
    }

    public Double getDrTransferAmt() {
        return drTransferAmt;
    }

    /**
     * Setter/Getter for CR_TRANSFER_AMT - table Field
     */
    public void setCrTransferAmt(Double crTransferAmt) {
        this.crTransferAmt = crTransferAmt;
    }

    public Double getCrTransferAmt() {
        return crTransferAmt;
    }

    /**
     * Setter/Getter for DR_CLEARING_AMT - table Field
     */
    public void setDrClearingAmt(Double drClearingAmt) {
        this.drClearingAmt = drClearingAmt;
    }

    public Double getDrClearingAmt() {
        return drClearingAmt;
    }

    /**
     * Setter/Getter for CR_CLEARING_AMT - table Field
     */
    public void setCrClearingAmt(Double crClearingAmt) {
        this.crClearingAmt = crClearingAmt;
    }

    public Double getCrClearingAmt() {
        return crClearingAmt;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
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
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("drCash", drCash));
        strB.append(getTOString("crCash", crCash));
        strB.append(getTOString("drTransfer", drTransfer));
        strB.append(getTOString("crTransfer", crTransfer));
        strB.append(getTOString("drClearing", drClearing));
        strB.append(getTOString("crClearing", crClearing));
        strB.append(getTOString("drCashAmt", drCashAmt));
        strB.append(getTOString("crCashAmt", crCashAmt));
        strB.append(getTOString("drTransferAmt", drTransferAmt));
        strB.append(getTOString("crTransferAmt", crTransferAmt));
        strB.append(getTOString("drClearingAmt", drClearingAmt));
        strB.append(getTOString("crClearingAmt", crClearingAmt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("smsAlert", smsAlert));
        strB.append(getTOString("reminder", reminder));
        strB.append(getTOString("txnAllowed", txnAllowed));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("drCash", drCash));
        strB.append(getTOXml("crCash", crCash));
        strB.append(getTOXml("drTransfer", drTransfer));
        strB.append(getTOXml("crTransfer", crTransfer));
        strB.append(getTOXml("drClearing", drClearing));
        strB.append(getTOXml("crClearing", crClearing));
        strB.append(getTOXml("drCashAmt", drCashAmt));
        strB.append(getTOXml("crCashAmt", crCashAmt));
        strB.append(getTOXml("drTransferAmt", drTransferAmt));
        strB.append(getTOXml("crTransferAmt", crTransferAmt));
        strB.append(getTOXml("drClearingAmt", drClearingAmt));
        strB.append(getTOXml("crClearingAmt", crClearingAmt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("smsAlert", smsAlert));
        strB.append(getTOXml("reminder", reminder));
        strB.append(getTOXml("txnAllowed", txnAllowed));
        strB.append(getTOXmlEnd());
        return strB.toString();
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

    public String getSmsAlert() {
        return smsAlert;
    }

    public void setSmsAlert(String smsAlert) {
        this.smsAlert = smsAlert;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }
    
    public String getTxnAllowed() {
        return txnAllowed;
    }

    public void setTxnAllowed(String txnAllowed) {
        this.txnAllowed = txnAllowed;
    }
    
}