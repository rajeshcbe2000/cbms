/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductLoanTO.java
 *
 * Created on Tue Mar 15 14:08:12 IST 2005
 */
package com.see.truetransact.transferobject.investments;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_PROD_LOANS.
 */
public class InvestmentsAmortizationCalculationTO extends TransferObject implements Serializable {

    private Date uptoDate = null;
    private String investmentBehaves = "";
    private String investmentID = "";
    private String investmentName = "";
    private Date transDate = null;
    private Double amortizationAmount = null;
    private Double premium = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String batchID = "";

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
        strB.append(getTOString("amortizationAmount", amortizationAmount));
        strB.append(getTOString("uptoDate", uptoDate));
        strB.append(getTOString("transDate", transDate));
        strB.append(getTOString("premium", premium));
        strB.append(getTOString("investmentBehaves", investmentBehaves));
        strB.append(getTOString("investmentID", investmentID));
        strB.append(getTOString("investmentName", investmentName));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("batchID", batchID));

        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("investmentBehaves", investmentBehaves));
        strB.append(getTOXml("investmentID", investmentID));
        strB.append(getTOXml("investmentName", investmentName));
        strB.append(getTOXml("amortizationAmount", amortizationAmount));
        strB.append(getTOXml("uptoDate", uptoDate));
        strB.append(getTOXml("transDate", transDate));
        strB.append(getTOXml("premium", premium));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("batchID", batchID));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    /**
     * Getter for property investmentBehaves.
     *
     * @return Value of property investmentBehaves.
     */
    public java.lang.String getInvestmentBehaves() {
        return investmentBehaves;
    }

    /**
     * Setter for property investmentBehaves.
     *
     * @param investmentBehaves New value of property investmentBehaves.
     */
    public void setInvestmentBehaves(java.lang.String investmentBehaves) {
        this.investmentBehaves = investmentBehaves;
    }

    /**
     * Getter for property investmentID.
     *
     * @return Value of property investmentID.
     */
    public java.lang.String getInvestmentID() {
        return investmentID;
    }

    /**
     * Setter for property investmentID.
     *
     * @param investmentID New value of property investmentID.
     */
    public void setInvestmentID(java.lang.String investmentID) {
        this.investmentID = investmentID;
    }

    /**
     * Getter for property investmentName.
     *
     * @return Value of property investmentName.
     */
    public java.lang.String getInvestmentName() {
        return investmentName;
    }

    /**
     * Setter for property investmentName.
     *
     * @param investmentName New value of property investmentName.
     */
    public void setInvestmentName(java.lang.String investmentName) {
        this.investmentName = investmentName;
    }

    /**
     * Getter for property uptoDate.
     *
     * @return Value of property uptoDate.
     */
    public java.util.Date getUptoDate() {
        return uptoDate;
    }

    /**
     * Setter for property uptoDate.
     *
     * @param uptoDate New value of property uptoDate.
     */
    public void setUptoDate(java.util.Date uptoDate) {
        this.uptoDate = uptoDate;
    }

    /**
     * Getter for property transDate.
     *
     * @return Value of property transDate.
     */
    public java.util.Date getTransDate() {
        return transDate;
    }

    /**
     * Setter for property transDate.
     *
     * @param transDate New value of property transDate.
     */
    public void setTransDate(java.util.Date transDate) {
        this.transDate = transDate;
    }

    /**
     * Getter for property amortizationAmount.
     *
     * @return Value of property amortizationAmount.
     */
    public java.lang.Double getAmortizationAmount() {
        return amortizationAmount;
    }

    /**
     * Setter for property amortizationAmount.
     *
     * @param amortizationAmount New value of property amortizationAmount.
     */
    public void setAmortizationAmount(java.lang.Double amortizationAmount) {
        this.amortizationAmount = amortizationAmount;
    }

    /**
     * Getter for property premium.
     *
     * @return Value of property premium.
     */
    public java.lang.Double getPremium() {
        return premium;
    }

    /**
     * Setter for property premium.
     *
     * @param premium New value of property premium.
     */
    public void setPremium(java.lang.Double premium) {
        this.premium = premium;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * Getter for property batchID.
     *
     * @return Value of property batchID.
     */
    public java.lang.String getBatchID() {
        return batchID;
    }

    /**
     * Setter for property batchID.
     *
     * @param batchID New value of property batchID.
     */
    public void setBatchID(java.lang.String batchID) {
        this.batchID = batchID;
    }
}