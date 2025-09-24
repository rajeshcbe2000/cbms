/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ForexExchangeTO.java
 * 
 * Created on Wed May 05 12:46:50 IST 2004
 */
package com.see.truetransact.transferobject.forex;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is FOREX_EXCHANGE_RATE_DETAIL.
 */
public class ForexExchangeTO extends TransferObject implements Serializable {

    private String exchangeId = "";
    private Date valueDate = null;
    private String transCurrency = "";
    private Double middleRate = null;
    private String status = "";
    private String baseCurrency = "";
    private String valueHrs = "";
    private String valueMins = "";

    /**
     * Setter/Getter for EXCHANGE_ID - table Field
     */
    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    /**
     * Setter/Getter for VALUE_DATE - table Field
     */
    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public Date getValueDate() {
        return valueDate;
    }

    /**
     * Setter/Getter for TRANS_CURRENCY - table Field
     */
    public void setTransCurrency(String transCurrency) {
        this.transCurrency = transCurrency;
    }

    public String getTransCurrency() {
        return transCurrency;
    }

    /**
     * Setter/Getter for MIDDLE_RATE - table Field
     */
    public void setMiddleRate(Double middleRate) {
        this.middleRate = middleRate;
    }

    public Double getMiddleRate() {
        return middleRate;
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
     * Setter/Getter for BASE_CURRENCY - table Field
     */
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    /**
     * Setter/Getter for VALUE_HRS - table Field
     */
    public void setValueHrs(String valueHrs) {
        this.valueHrs = valueHrs;
    }

    public String getValueHrs() {
        return valueHrs;
    }

    /**
     * Setter/Getter for VALUE_MINS - table Field
     */
    public void setValueMins(String valueMins) {
        this.valueMins = valueMins;
    }

    public String getValueMins() {
        return valueMins;
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
        strB.append(getTOString("exchangeId", exchangeId));
        strB.append(getTOString("valueDate", valueDate));
        strB.append(getTOString("transCurrency", transCurrency));
        strB.append(getTOString("middleRate", middleRate));
        strB.append(getTOString("status", status));
        strB.append(getTOString("baseCurrency", baseCurrency));
        strB.append(getTOString("valueHrs", valueHrs));
        strB.append(getTOString("valueMins", valueMins));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("exchangeId", exchangeId));
        strB.append(getTOXml("valueDate", valueDate));
        strB.append(getTOXml("transCurrency", transCurrency));
        strB.append(getTOXml("middleRate", middleRate));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("baseCurrency", baseCurrency));
        strB.append(getTOXml("valueHrs", valueHrs));
        strB.append(getTOXml("valueMins", valueMins));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}