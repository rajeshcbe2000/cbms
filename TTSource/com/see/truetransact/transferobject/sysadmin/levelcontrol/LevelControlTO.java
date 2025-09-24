/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LevelControlTO.java
 * 
 * Created on Wed May 04 12:16:36 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.levelcontrol;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LEVEL_MASTER.
 */
public class LevelControlTO extends TransferObject implements Serializable {

    private String levelId = "";
    private String levelDesc = "";
    private Double cashCredit = null;
    private Double cashDebit = null;
    private Double transCredit = null;
    private Double transDebit = null;
    private Double clearingCredit = null;
    private Double clearingDebit = null;
    private String status = "";
    private String levelName = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchCode = "";
    private String singleWindow = "";
    private String createdBy = "";
    private Date createdDt = null;

    /**
     * Setter/Getter for LEVEL_ID - table Field
     */
    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelId() {
        return levelId;
    }

    /**
     * Setter/Getter for LEVEL_DESC - table Field
     */
    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    /**
     * Setter/Getter for CASH_CREDIT - table Field
     */
    public void setCashCredit(Double cashCredit) {
        this.cashCredit = cashCredit;
    }

    public Double getCashCredit() {
        return cashCredit;
    }

    /**
     * Setter/Getter for CASH_DEBIT - table Field
     */
    public void setCashDebit(Double cashDebit) {
        this.cashDebit = cashDebit;
    }

    public Double getCashDebit() {
        return cashDebit;
    }

    /**
     * Setter/Getter for TRANS_CREDIT - table Field
     */
    public void setTransCredit(Double transCredit) {
        this.transCredit = transCredit;
    }

    public Double getTransCredit() {
        return transCredit;
    }

    /**
     * Setter/Getter for TRANS_DEBIT - table Field
     */
    public void setTransDebit(Double transDebit) {
        this.transDebit = transDebit;
    }

    public Double getTransDebit() {
        return transDebit;
    }

    /**
     * Setter/Getter for CLEARING_CREDIT - table Field
     */
    public void setClearingCredit(Double clearingCredit) {
        this.clearingCredit = clearingCredit;
    }

    public Double getClearingCredit() {
        return clearingCredit;
    }

    /**
     * Setter/Getter for CLEARING_DEBIT - table Field
     */
    public void setClearingDebit(Double clearingDebit) {
        this.clearingDebit = clearingDebit;
    }

    public Double getClearingDebit() {
        return clearingDebit;
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
     * Setter/Getter for LEVEL_NAME - table Field
     */
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelName() {
        return levelName;
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
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter/Getter for SINGLE_WINDOW - table Field
     */
    public void setSingleWindow(String singleWindow) {
        this.singleWindow = singleWindow;
    }

    public String getSingleWindow() {
        return singleWindow;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("levelId");
        return levelId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("levelId", levelId));
        strB.append(getTOString("levelDesc", levelDesc));
        strB.append(getTOString("cashCredit", cashCredit));
        strB.append(getTOString("cashDebit", cashDebit));
        strB.append(getTOString("transCredit", transCredit));
        strB.append(getTOString("transDebit", transDebit));
        strB.append(getTOString("clearingCredit", clearingCredit));
        strB.append(getTOString("clearingDebit", clearingDebit));
        strB.append(getTOString("status", status));
        strB.append(getTOString("levelName", levelName));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("singleWindow", singleWindow));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("levelId", levelId));
        strB.append(getTOXml("levelDesc", levelDesc));
        strB.append(getTOXml("cashCredit", cashCredit));
        strB.append(getTOXml("cashDebit", cashDebit));
        strB.append(getTOXml("transCredit", transCredit));
        strB.append(getTOXml("transDebit", transDebit));
        strB.append(getTOXml("clearingCredit", clearingCredit));
        strB.append(getTOXml("clearingDebit", clearingDebit));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("levelName", levelName));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("singleWindow", singleWindow));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}