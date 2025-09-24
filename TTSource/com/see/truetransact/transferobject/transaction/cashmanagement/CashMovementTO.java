/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashMovementTO.java
 * 
 * Created on Fri Jan 28 14:27:26 IST 2005
 */
package com.see.truetransact.transferobject.transaction.cashmanagement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CASH_MOVEMENT.
 */
public class CashMovementTO extends TransferObject implements Serializable {

    private String cashMovementId = "";
    private Date cashDt = null;
    private String receivedCashierId = "";
    private String vaultCash = "";
    private String transType = "";
    private Double cashBoxBalance = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String issuedCashierId = "";
    private String branchId = "";

    /**
     * Setter/Getter for CASH_MOVEMENT_ID - table Field
     */
    public void setCashMovementId(String cashMovementId) {
        this.cashMovementId = cashMovementId;
    }

    public String getCashMovementId() {
        return cashMovementId;
    }

    /**
     * Setter/Getter for CASH_DT - table Field
     */
    public void setCashDt(Date cashDt) {
        this.cashDt = cashDt;
    }

    public Date getCashDt() {
        return cashDt;
    }

    /**
     * Setter/Getter for RECEIVED_CASHIER_ID - table Field
     */
    public void setReceivedCashierId(String receivedCashierId) {
        this.receivedCashierId = receivedCashierId;
    }

    public String getReceivedCashierId() {
        return receivedCashierId;
    }

    /**
     * Setter/Getter for VAULT_CASH - table Field
     */
    public void setVaultCash(String vaultCash) {
        this.vaultCash = vaultCash;
    }

    public String getVaultCash() {
        return vaultCash;
    }

    /**
     * Setter/Getter for TRANS_TYPE - table Field
     */
    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransType() {
        return transType;
    }

    /**
     * Setter/Getter for CASH_BOX_BALANCE - table Field
     */
    public void setCashBoxBalance(Double cashBoxBalance) {
        this.cashBoxBalance = cashBoxBalance;
    }

    public Double getCashBoxBalance() {
        return cashBoxBalance;
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
     * Setter/Getter for ISSUED_CASHIER_ID - table Field
     */
    public void setIssuedCashierId(String issuedCashierId) {
        this.issuedCashierId = issuedCashierId;
    }

    public String getIssuedCashierId() {
        return issuedCashierId;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("cashMovementId");
        return cashMovementId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("cashMovementId", cashMovementId));
        strB.append(getTOString("cashDt", cashDt));
        strB.append(getTOString("receivedCashierId", receivedCashierId));
        strB.append(getTOString("vaultCash", vaultCash));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("cashBoxBalance", cashBoxBalance));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("issuedCashierId", issuedCashierId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("cashMovementId", cashMovementId));
        strB.append(getTOXml("cashDt", cashDt));
        strB.append(getTOXml("receivedCashierId", receivedCashierId));
        strB.append(getTOXml("vaultCash", vaultCash));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("cashBoxBalance", cashBoxBalance));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("issuedCashierId", issuedCashierId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property branchId.
     *
     * @return Value of property branchId.
     */
    public java.lang.String getBranchId() {
        return branchId;
    }

    /**
     * Setter for property branchId.
     *
     * @param branchId New value of property branchId.
     */
    public void setBranchId(java.lang.String branchId) {
        this.branchId = branchId;
    }
}