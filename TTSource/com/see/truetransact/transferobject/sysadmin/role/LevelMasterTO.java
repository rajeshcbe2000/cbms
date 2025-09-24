/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LevelMasterTO.java
 * 
 * Created on Thu Apr 29 09:56:04 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.sysadmin.role;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LEVEL_MASTER.
 */
public class LevelMasterTO extends TransferObject implements Serializable {

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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("levelId");
        return "";
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
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}