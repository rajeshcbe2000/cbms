/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * StandInstrDebitTO.java
 *
 * Created on Mon Aug 23 17:10:26 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_STANDING_INSTRU.
 */
public class StandInstrDebitTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private String debitProdId = "";
    private String debitAchd = "";
    private String depositSubNo = "";
    private Double amount = null;
    private String particulars = "";

    /**
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
    }

    /**
     * Setter/Getter for DEBIT_PROD_ID - table Field
     */
    public void setDebitProdId(String debitProdId) {
        this.debitProdId = debitProdId;
    }

    public String getDebitProdId() {
        return debitProdId;
    }

    /**
     * Setter/Getter for DEBIT_ACHD - table Field
     */
    public void setDebitAchd(String debitAchd) {
        this.debitAchd = debitAchd;
    }

    public String getDebitAchd() {
        return debitAchd;
    }

    /**
     * Setter/Getter for DEPOSIT_SUB_NO - table Field
     */
    public void setDepositSubNo(String depositSubNo) {
        this.depositSubNo = depositSubNo;
    }

    public String getDepositSubNo() {
        return depositSubNo;
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
     * Setter/Getter for PARTICULARS - table Field
     */
    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getParticulars() {
        return particulars;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("depositNo" + KEY_VAL_SEPARATOR + "depositSubNo");
        return depositNo + KEY_VAL_SEPARATOR + depositSubNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("debitProdId", debitProdId));
        strB.append(getTOString("debitAchd", debitAchd));
        strB.append(getTOString("depositSubNo", depositSubNo));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("particulars", particulars));
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
        strB.append(getTOXml("debitProdId", debitProdId));
        strB.append(getTOXml("debitAchd", debitAchd));
        strB.append(getTOXml("depositSubNo", depositSubNo));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}