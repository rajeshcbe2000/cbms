/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductAccountParamTO.java
 * 
 * Created on Wed Aug 11 16:05:19 IST 2004
 */
package com.see.truetransact.transferobject.product.loan.agriculturecard;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_ACPARAM.
 */
public class AgriLoanProductAccountParamTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String numberPattern = "";
    private String lastAcNo = "";
    private String limitDefAllowed = "";
    private String staffAcOpened = "";
    private String debitIntClearingappl = "";
    private String numberPatternSuffix = "";

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
     * Setter/Getter for NUMBER_PATTERN - table Field
     */
    public void setNumberPattern(String numberPattern) {
        this.numberPattern = numberPattern;
    }

    public String getNumberPattern() {
        return numberPattern;
    }

    /**
     * Setter/Getter for LAST_AC_NO - table Field
     */
    public void setLastAcNo(String lastAcNo) {
        this.lastAcNo = lastAcNo;
    }

    public String getLastAcNo() {
        return lastAcNo;
    }

    /**
     * Setter/Getter for LIMIT_DEF_ALLOWED - table Field
     */
    public void setLimitDefAllowed(String limitDefAllowed) {
        this.limitDefAllowed = limitDefAllowed;
    }

    public String getLimitDefAllowed() {
        return limitDefAllowed;
    }

    /**
     * Setter/Getter for STAFF_AC_OPENED - table Field
     */
    public void setStaffAcOpened(String staffAcOpened) {
        this.staffAcOpened = staffAcOpened;
    }

    public String getStaffAcOpened() {
        return staffAcOpened;
    }

    /**
     * Setter/Getter for DEBIT_INT_CLEARINGAPPL - table Field
     */
    public void setDebitIntClearingappl(String debitIntClearingappl) {
        this.debitIntClearingappl = debitIntClearingappl;
    }

    public String getDebitIntClearingappl() {
        return debitIntClearingappl;
    }

    /**
     * Setter/Getter for NUMBER_PATTERN_SUFFIX - table Field
     */
    public void setNumberPatternSuffix(String numberPatternSuffix) {
        this.numberPatternSuffix = numberPatternSuffix;
    }

    public String getNumberPatternSuffix() {
        return numberPatternSuffix;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId");
        return prodId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("numberPattern", numberPattern));
        strB.append(getTOString("lastAcNo", lastAcNo));
        strB.append(getTOString("limitDefAllowed", limitDefAllowed));
        strB.append(getTOString("staffAcOpened", staffAcOpened));
        strB.append(getTOString("debitIntClearingappl", debitIntClearingappl));
        strB.append(getTOString("numberPatternSuffix", numberPatternSuffix));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("numberPattern", numberPattern));
        strB.append(getTOXml("lastAcNo", lastAcNo));
        strB.append(getTOXml("limitDefAllowed", limitDefAllowed));
        strB.append(getTOXml("staffAcOpened", staffAcOpened));
        strB.append(getTOXml("debitIntClearingappl", debitIntClearingappl));
        strB.append(getTOXml("numberPatternSuffix", numberPatternSuffix));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}