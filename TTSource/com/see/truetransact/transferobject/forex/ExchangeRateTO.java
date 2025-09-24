/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExchangeRateTO.java
 * 
 * Created on Tue May 04 18:11:20 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.forex;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is FOREX_EXCHANGE_RATE.
 */
public class ExchangeRateTO extends TransferObject implements Serializable {

    private String exchangeId = "";
    private Date valueDate = null;
    private String transCurrency = "";
    private String conversionCurrency = "";
    private String customerType = "";
    private Double middleRate = null;
    private Double sellingPer = null;
    private Double sellingPrice = null;
    private Double buyingPer = null;
    private Double buyingPrice = null;
    private String preferred = "";
    private Double commission = null;
    private String remarks = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String status = "";
    private String authorizeStatus = null;
    private String rateType = "";
    private Double billBuying = null;
    private Double billSelling = null;
    private Double ddBuying = null;
    private Double ddSelling = null;
    private Double tcBuying = null;
    private Double tcSelling = null;
    private Double notionalRate = null;
    private Double ttBuying = null;
    private Double ttSelling = null;
    private Double commBillSlab = null;
    private Double commBillPer = null;
    private Double commDdSlab = null;
    private Double commDdPer = null;
    private Double commTcSlab = null;
    private Double commTcPer = null;
    private Double commTtSlab = null;
    private Double commTtPer = null;
    private Double commCurrPer = null;

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
     * Setter/Getter for CONVERSION_CURRENCY - table Field
     */
    public void setConversionCurrency(String conversionCurrency) {
        this.conversionCurrency = conversionCurrency;
    }

    public String getConversionCurrency() {
        return conversionCurrency;
    }

    /**
     * Setter/Getter for CUSTOMER_TYPE - table Field
     */
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerType() {
        return customerType;
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
     * Setter/Getter for SELLING_PER - table Field
     */
    public void setSellingPer(Double sellingPer) {
        this.sellingPer = sellingPer;
    }

    public Double getSellingPer() {
        return sellingPer;
    }

    /**
     * Setter/Getter for SELLING_PRICE - table Field
     */
    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    /**
     * Setter/Getter for BUYING_PER - table Field
     */
    public void setBuyingPer(Double buyingPer) {
        this.buyingPer = buyingPer;
    }

    public Double getBuyingPer() {
        return buyingPer;
    }

    /**
     * Setter/Getter for BUYING_PRICE - table Field
     */
    public void setBuyingPrice(Double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    /**
     * Setter/Getter for PREFERRED - table Field
     */
    public void setPreferred(String preferred) {
        this.preferred = preferred;
    }

    public String getPreferred() {
        return preferred;
    }

    /**
     * Setter/Getter for COMMISSION - table Field
     */
    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getCommission() {
        return commission;
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
     * Setter/Getter for RATE_TYPE - table Field
     */
    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getRateType() {
        return rateType;
    }

    /**
     * Setter/Getter for BILL_BUYING - table Field
     */
    public void setBillBuying(Double billBuying) {
        this.billBuying = billBuying;
    }

    public Double getBillBuying() {
        return billBuying;
    }

    /**
     * Setter/Getter for BILL_SELLING - table Field
     */
    public void setBillSelling(Double billSelling) {
        this.billSelling = billSelling;
    }

    public Double getBillSelling() {
        return billSelling;
    }

    /**
     * Setter/Getter for DD_BUYING - table Field
     */
    public void setDdBuying(Double ddBuying) {
        this.ddBuying = ddBuying;
    }

    public Double getDdBuying() {
        return ddBuying;
    }

    /**
     * Setter/Getter for DD_SELLING - table Field
     */
    public void setDdSelling(Double ddSelling) {
        this.ddSelling = ddSelling;
    }

    public Double getDdSelling() {
        return ddSelling;
    }

    /**
     * Setter/Getter for TC_BUYING - table Field
     */
    public void setTcBuying(Double tcBuying) {
        this.tcBuying = tcBuying;
    }

    public Double getTcBuying() {
        return tcBuying;
    }

    /**
     * Setter/Getter for TC_SELLING - table Field
     */
    public void setTcSelling(Double tcSelling) {
        this.tcSelling = tcSelling;
    }

    public Double getTcSelling() {
        return tcSelling;
    }

    /**
     * Setter/Getter for NOTIONAL_RATE - table Field
     */
    public void setNotionalRate(Double notionalRate) {
        this.notionalRate = notionalRate;
    }

    public Double getNotionalRate() {
        return notionalRate;
    }

    /**
     * Setter/Getter for TT_BUYING - table Field
     */
    public void setTtBuying(Double ttBuying) {
        this.ttBuying = ttBuying;
    }

    public Double getTtBuying() {
        return ttBuying;
    }

    /**
     * Setter/Getter for TT_SELLING - table Field
     */
    public void setTtSelling(Double ttSelling) {
        this.ttSelling = ttSelling;
    }

    public Double getTtSelling() {
        return ttSelling;
    }

    /**
     * Setter/Getter for COMM_BILL_SLAB - table Field
     */
    public void setCommBillSlab(Double commBillSlab) {
        this.commBillSlab = commBillSlab;
    }

    public Double getCommBillSlab() {
        return commBillSlab;
    }

    /**
     * Setter/Getter for COMM_BILL_PER - table Field
     */
    public void setCommBillPer(Double commBillPer) {
        this.commBillPer = commBillPer;
    }

    public Double getCommBillPer() {
        return commBillPer;
    }

    /**
     * Setter/Getter for COMM_DD_SLAB - table Field
     */
    public void setCommDdSlab(Double commDdSlab) {
        this.commDdSlab = commDdSlab;
    }

    public Double getCommDdSlab() {
        return commDdSlab;
    }

    /**
     * Setter/Getter for COMM_DD_PER - table Field
     */
    public void setCommDdPer(Double commDdPer) {
        this.commDdPer = commDdPer;
    }

    public Double getCommDdPer() {
        return commDdPer;
    }

    /**
     * Setter/Getter for COMM_TC_SLAB - table Field
     */
    public void setCommTcSlab(Double commTcSlab) {
        this.commTcSlab = commTcSlab;
    }

    public Double getCommTcSlab() {
        return commTcSlab;
    }

    /**
     * Setter/Getter for COMM_TC_PER - table Field
     */
    public void setCommTcPer(Double commTcPer) {
        this.commTcPer = commTcPer;
    }

    public Double getCommTcPer() {
        return commTcPer;
    }

    /**
     * Setter/Getter for COMM_TT_SLAB - table Field
     */
    public void setCommTtSlab(Double commTtSlab) {
        this.commTtSlab = commTtSlab;
    }

    public Double getCommTtSlab() {
        return commTtSlab;
    }

    /**
     * Setter/Getter for COMM_TT_PER - table Field
     */
    public void setCommTtPer(Double commTtPer) {
        this.commTtPer = commTtPer;
    }

    public Double getCommTtPer() {
        return commTtPer;
    }

    /**
     * Setter/Getter for COMM_CURR_PER - table Field
     */
    public void setCommCurrPer(Double commCurrPer) {
        this.commCurrPer = commCurrPer;
    }

    public Double getCommCurrPer() {
        return commCurrPer;
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
        strB.append(getTOString("conversionCurrency", conversionCurrency));
        strB.append(getTOString("customerType", customerType));
        strB.append(getTOString("middleRate", middleRate));
        strB.append(getTOString("sellingPer", sellingPer));
        strB.append(getTOString("sellingPrice", sellingPrice));
        strB.append(getTOString("buyingPer", buyingPer));
        strB.append(getTOString("buyingPrice", buyingPrice));
        strB.append(getTOString("preferred", preferred));
        strB.append(getTOString("commission", commission));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("rateType", rateType));
        strB.append(getTOString("billBuying", billBuying));
        strB.append(getTOString("billSelling", billSelling));
        strB.append(getTOString("ddBuying", ddBuying));
        strB.append(getTOString("ddSelling", ddSelling));
        strB.append(getTOString("tcBuying", tcBuying));
        strB.append(getTOString("tcSelling", tcSelling));
        strB.append(getTOString("notionalRate", notionalRate));
        strB.append(getTOString("ttBuying", ttBuying));
        strB.append(getTOString("ttSelling", ttSelling));
        strB.append(getTOString("commBillSlab", commBillSlab));
        strB.append(getTOString("commBillPer", commBillPer));
        strB.append(getTOString("commDdSlab", commDdSlab));
        strB.append(getTOString("commDdPer", commDdPer));
        strB.append(getTOString("commTcSlab", commTcSlab));
        strB.append(getTOString("commTcPer", commTcPer));
        strB.append(getTOString("commTtSlab", commTtSlab));
        strB.append(getTOString("commTtPer", commTtPer));
        strB.append(getTOString("commCurrPer", commCurrPer));
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
        strB.append(getTOXml("conversionCurrency", conversionCurrency));
        strB.append(getTOXml("customerType", customerType));
        strB.append(getTOXml("middleRate", middleRate));
        strB.append(getTOXml("sellingPer", sellingPer));
        strB.append(getTOXml("sellingPrice", sellingPrice));
        strB.append(getTOXml("buyingPer", buyingPer));
        strB.append(getTOXml("buyingPrice", buyingPrice));
        strB.append(getTOXml("preferred", preferred));
        strB.append(getTOXml("commission", commission));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("rateType", rateType));
        strB.append(getTOXml("billBuying", billBuying));
        strB.append(getTOXml("billSelling", billSelling));
        strB.append(getTOXml("ddBuying", ddBuying));
        strB.append(getTOXml("ddSelling", ddSelling));
        strB.append(getTOXml("tcBuying", tcBuying));
        strB.append(getTOXml("tcSelling", tcSelling));
        strB.append(getTOXml("notionalRate", notionalRate));
        strB.append(getTOXml("ttBuying", ttBuying));
        strB.append(getTOXml("ttSelling", ttSelling));
        strB.append(getTOXml("commBillSlab", commBillSlab));
        strB.append(getTOXml("commBillPer", commBillPer));
        strB.append(getTOXml("commDdSlab", commDdSlab));
        strB.append(getTOXml("commDdPer", commDdPer));
        strB.append(getTOXml("commTcSlab", commTcSlab));
        strB.append(getTOXml("commTcPer", commTcPer));
        strB.append(getTOXml("commTtSlab", commTtSlab));
        strB.append(getTOXml("commTtPer", commTtPer));
        strB.append(getTOXml("commCurrPer", commCurrPer));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}