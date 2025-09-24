/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MultiLevelMasterTO.java
 * 
 * Created on Thu Sep 09 15:24:43 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.sysadmin.levelcontrol.multilevel;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LEVEL_MULTI.
 */
public class MultiLevelMasterTO extends TransferObject implements Serializable {

    private String levelMultiId = "";
    private Double amount = null;
    private String condition = "";
    private String cashCredit = "";
    private String cashDebit = "";
    private String transferCredit = "";
    private String transferDebit = "";
    private String clearingCredit = "";
    private String clearingDebit = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizeStatus = null;
    private String postfixFormula = "";

    /**
     * Setter/Getter for LEVEL_MULTI_ID - table Field
     */
    public void setLevelMultiId(String levelMultiId) {
        this.levelMultiId = levelMultiId;
    }

    public String getLevelMultiId() {
        return levelMultiId;
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
     * Setter/Getter for CONDITION - table Field
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    /**
     * Setter/Getter for CASH_CREDIT - table Field
     */
    public void setCashCredit(String cashCredit) {
        this.cashCredit = cashCredit;
    }

    public String getCashCredit() {
        return cashCredit;
    }

    /**
     * Setter/Getter for CASH_DEBIT - table Field
     */
    public void setCashDebit(String cashDebit) {
        this.cashDebit = cashDebit;
    }

    public String getCashDebit() {
        return cashDebit;
    }

    /**
     * Setter/Getter for TRANSFER_CREDIT - table Field
     */
    public void setTransferCredit(String transferCredit) {
        this.transferCredit = transferCredit;
    }

    public String getTransferCredit() {
        return transferCredit;
    }

    /**
     * Setter/Getter for TRANSFER_DEBIT - table Field
     */
    public void setTransferDebit(String transferDebit) {
        this.transferDebit = transferDebit;
    }

    public String getTransferDebit() {
        return transferDebit;
    }

    /**
     * Setter/Getter for CLEARING_CREDIT - table Field
     */
    public void setClearingCredit(String clearingCredit) {
        this.clearingCredit = clearingCredit;
    }

    public String getClearingCredit() {
        return clearingCredit;
    }

    /**
     * Setter/Getter for CLEARING_DEBIT - table Field
     */
    public void setClearingDebit(String clearingDebit) {
        this.clearingDebit = clearingDebit;
    }

    public String getClearingDebit() {
        return clearingDebit;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for POSTFIX_FORMULA - table Field
     */
    public void setPostfixFormula(String postfixFormula) {
        this.postfixFormula = postfixFormula;
    }

    public String getPostfixFormula() {
        return postfixFormula;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("levelMultiId");
        return levelMultiId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("levelMultiId", levelMultiId));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("condition", condition));
        strB.append(getTOString("cashCredit", cashCredit));
        strB.append(getTOString("cashDebit", cashDebit));
        strB.append(getTOString("transferCredit", transferCredit));
        strB.append(getTOString("transferDebit", transferDebit));
        strB.append(getTOString("clearingCredit", clearingCredit));
        strB.append(getTOString("clearingDebit", clearingDebit));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("postfixFormula", postfixFormula));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("levelMultiId", levelMultiId));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("condition", condition));
        strB.append(getTOXml("cashCredit", cashCredit));
        strB.append(getTOXml("cashDebit", cashDebit));
        strB.append(getTOXml("transferCredit", transferCredit));
        strB.append(getTOXml("transferDebit", transferDebit));
        strB.append(getTOXml("clearingCredit", clearingCredit));
        strB.append(getTOXml("clearingDebit", clearingDebit));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("postfixFormula", postfixFormula));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}