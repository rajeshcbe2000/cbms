/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashDenominationTO.java
 * 
 * Created on Fri May 13 00:42:06 IST 2005
 */
package com.see.truetransact.transferobject.transaction.cash;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is FOREX_DENOMINATION_TRANS.
 */
public class CashDenominationTO extends TransferObject implements Serializable {

    private String transId = "";
    private String currency = "";
    private String transType = "";
    private Double denominationValue = null;
    private Double denominationCount = null;
    private String status = "";
    private String denominationType = "";

    /**
     * Setter/Getter for TRANS_ID - table Field
     */
    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransId() {
        return transId;
    }

    /**
     * Setter/Getter for CURRENCY - table Field
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
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
     * Setter/Getter for DENOMINATION_VALUE - table Field
     */
    public void setDenominationValue(Double denominationValue) {
        this.denominationValue = denominationValue;
    }

    public Double getDenominationValue() {
        return denominationValue;
    }

    /**
     * Setter/Getter for DENOMINATION_COUNT - table Field
     */
    public void setDenominationCount(Double denominationCount) {
        this.denominationCount = denominationCount;
    }

    public Double getDenominationCount() {
        return denominationCount;
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
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("currency", currency));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("denominationValue", denominationValue));
        strB.append(getTOString("denominationCount", denominationCount));
        strB.append(getTOString("status", status));
        strB.append(getTOString("denominationType", denominationType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("currency", currency));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("denominationValue", denominationValue));
        strB.append(getTOXml("denominationCount", denominationCount));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("denominationType", denominationType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property denominationType.
     *
     * @return Value of property denominationType.
     */
    public java.lang.String getDenominationType() {
        return denominationType;
    }

    /**
     * Setter for property denominationType.
     *
     * @param denominationType New value of property denominationType.
     */
    public void setDenominationType(java.lang.String denominationType) {
        this.denominationType = denominationType;
    }
}