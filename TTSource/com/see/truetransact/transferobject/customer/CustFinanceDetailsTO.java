/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustFinanceDetailsTO.java
 * 
 * Created on Sat Dec 25 13:31:23 IST 2004
 */
package com.see.truetransact.transferobject.customer;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CORP_CUST_FINANCE.
 */
public class CustFinanceDetailsTO extends TransferObject implements Serializable {

    private String custId = "";
    private Double authorizeCapital = null;
    private Double issuedCapital = null;
    private Double subscribedCapital = null;
    private Double totalResource = null;
    private Double lastYearPl = null;
    private Double dividendPercent = null;
    private String natureOfBusiness = "";
    private String compRegNo = "";
    private Date establishDt = null;
    private String ceoName = "";
    private Date financialYrEnd = null;
    private Double totalIncome = null;
    private Double totalNonTaxExp = null;
    private Double profitBeforeTax = null;
    private Double liablityTax = null;

    /**
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
    }

    /**
     * Setter/Getter for AUTHORIZE_CAPITAL - table Field
     */
    public void setAuthorizeCapital(Double authorizeCapital) {
        this.authorizeCapital = authorizeCapital;
    }

    public Double getAuthorizeCapital() {
        return authorizeCapital;
    }

    /**
     * Setter/Getter for ISSUED_CAPITAL - table Field
     */
    public void setIssuedCapital(Double issuedCapital) {
        this.issuedCapital = issuedCapital;
    }

    public Double getIssuedCapital() {
        return issuedCapital;
    }

    /**
     * Setter/Getter for SUBSCRIBED_CAPITAL - table Field
     */
    public void setSubscribedCapital(Double subscribedCapital) {
        this.subscribedCapital = subscribedCapital;
    }

    public Double getSubscribedCapital() {
        return subscribedCapital;
    }

    /**
     * Setter/Getter for TOTAL_RESOURCE - table Field
     */
    public void setTotalResource(Double totalResource) {
        this.totalResource = totalResource;
    }

    public Double getTotalResource() {
        return totalResource;
    }

    /**
     * Setter/Getter for LAST_YEAR_PL - table Field
     */
    public void setLastYearPl(Double lastYearPl) {
        this.lastYearPl = lastYearPl;
    }

    public Double getLastYearPl() {
        return lastYearPl;
    }

    /**
     * Setter/Getter for DIVIDEND_PERCENT - table Field
     */
    public void setDividendPercent(Double dividendPercent) {
        this.dividendPercent = dividendPercent;
    }

    public Double getDividendPercent() {
        return dividendPercent;
    }

    /**
     * Setter/Getter for NATURE_OF_BUSINESS - table Field
     */
    public void setNatureOfBusiness(String natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    /**
     * Setter/Getter for COMP_REG_NO - table Field
     */
    public void setCompRegNo(String compRegNo) {
        this.compRegNo = compRegNo;
    }

    public String getCompRegNo() {
        return compRegNo;
    }

    /**
     * Setter/Getter for ESTABLISH_DT - table Field
     */
    public void setEstablishDt(Date establishDt) {
        this.establishDt = establishDt;
    }

    public Date getEstablishDt() {
        return establishDt;
    }

    /**
     * Setter/Getter for CEO_NAME - table Field
     */
    public void setCeoName(String ceoName) {
        this.ceoName = ceoName;
    }

    public String getCeoName() {
        return ceoName;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(custId);
        return custId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("authorizeCapital", authorizeCapital));
        strB.append(getTOString("issuedCapital", issuedCapital));
        strB.append(getTOString("subscribedCapital", subscribedCapital));
        strB.append(getTOString("totalResource", totalResource));
        strB.append(getTOString("lastYearPl", lastYearPl));
        strB.append(getTOString("dividendPercent", dividendPercent));
        strB.append(getTOString("natureOfBusiness", natureOfBusiness));
        strB.append(getTOString("compRegNo", compRegNo));
        strB.append(getTOString("establishDt", establishDt));
        strB.append(getTOString("ceoName", ceoName));
        strB.append(getTOString("financialYrEnd", financialYrEnd));
        strB.append(getTOString("totalIncome", totalIncome));
        strB.append(getTOString("totalNonTaxExp", totalNonTaxExp));
        strB.append(getTOString("profitBeforeTax", profitBeforeTax));
        strB.append(getTOString("liablityTax", liablityTax));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("authorizeCapital", authorizeCapital));
        strB.append(getTOXml("issuedCapital", issuedCapital));
        strB.append(getTOXml("subscribedCapital", subscribedCapital));
        strB.append(getTOXml("totalResource", totalResource));
        strB.append(getTOXml("lastYearPl", lastYearPl));
        strB.append(getTOXml("dividendPercent", dividendPercent));
        strB.append(getTOXml("natureOfBusiness", natureOfBusiness));
        strB.append(getTOXml("compRegNo", compRegNo));
        strB.append(getTOXml("establishDt", establishDt));
        strB.append(getTOXml("ceoName", ceoName));
        strB.append(getTOXml("financialYrEnd", financialYrEnd));
        strB.append(getTOXml("totalIncome", totalIncome));
        strB.append(getTOXml("totalNonTaxExp", totalNonTaxExp));
        strB.append(getTOXml("profitBeforeTax", profitBeforeTax));
        strB.append(getTOXml("liablityTax", liablityTax));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property financialYrEnd.
     *
     * @return Value of property financialYrEnd.
     */
    public java.util.Date getFinancialYrEnd() {
        return financialYrEnd;
    }

    /**
     * Setter for property financialYrEnd.
     *
     * @param financialYrEnd New value of property financialYrEnd.
     */
    public void setFinancialYrEnd(java.util.Date financialYrEnd) {
        this.financialYrEnd = financialYrEnd;
    }

    /**
     * Getter for property totalIncome.
     *
     * @return Value of property totalIncome.
     */
    public java.lang.Double getTotalIncome() {
        return totalIncome;
    }

    /**
     * Setter for property totalIncome.
     *
     * @param totalIncome New value of property totalIncome.
     */
    public void setTotalIncome(java.lang.Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    /**
     * Getter for property totalNonTaxExp.
     *
     * @return Value of property totalNonTaxExp.
     */
    public java.lang.Double getTotalNonTaxExp() {
        return totalNonTaxExp;
    }

    /**
     * Setter for property totalNonTaxExp.
     *
     * @param totalNonTaxExp New value of property totalNonTaxExp.
     */
    public void setTotalNonTaxExp(java.lang.Double totalNonTaxExp) {
        this.totalNonTaxExp = totalNonTaxExp;
    }

    /**
     * Getter for property profitBeforeTax.
     *
     * @return Value of property profitBeforeTax.
     */
    public java.lang.Double getProfitBeforeTax() {
        return profitBeforeTax;
    }

    /**
     * Setter for property profitBeforeTax.
     *
     * @param profitBeforeTax New value of property profitBeforeTax.
     */
    public void setProfitBeforeTax(java.lang.Double profitBeforeTax) {
        this.profitBeforeTax = profitBeforeTax;
    }

    /**
     * Getter for property liablityTax.
     *
     * @return Value of property liablityTax.
     */
    public java.lang.Double getLiablityTax() {
        return liablityTax;
    }

    /**
     * Setter for property liablityTax.
     *
     * @param liablityTax New value of property liablityTax.
     */
    public void setLiablityTax(java.lang.Double liablityTax) {
        this.liablityTax = liablityTax;
    }
}