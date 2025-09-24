/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductInterCalcTO.java
 * 
 * Created on Fri May 14 16:03:50 IST 2004
 */
package com.see.truetransact.transferobject.product.loan;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_INTCALC.
 */
public class LoanProductInterCalcTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String chkFixed = "";
    private Double fixedMargin = null;
    private String calcType = "";
    private Double minPeriod = null;
    private Double maxPeriod = null;
    private Double minAmtLoan = null;
    private Double maxAmtLoan = null;
    private Double minIntDebit = null;
    private String subsidy = "";
    private Double loanPeriodsMultiples = null;
    private Double applInterest = null;
    private Double reviewPeriod = null;
    private Double eligibleDepForLoanAmt = null;
    private String depositRoundOff = "";
    private String maxPeriodChar = "";
    private Double depAmtMaturing = null;
    private Double depAmtMaturingPeriod = null;

    public Double getDepAmtMaturingPeriod() {
        return depAmtMaturingPeriod;
    }

    public void setDepAmtMaturingPeriod(Double depAmtMaturingPeriod) {
        this.depAmtMaturingPeriod = depAmtMaturingPeriod;
    }

    public Double getDepAmtMaturing() {
        return depAmtMaturing;
    }

    public void setDepAmtMaturing(Double depAmtMaturing) {
        this.depAmtMaturing = depAmtMaturing;
    }

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
     * Setter/Getter for CALC_TYPE - table Field
     */
    public void setCalcType(String calcType) {
        this.calcType = calcType;
    }

    public String getCalcType() {
        return calcType;
    }

    /**
     * Setter/Getter for MIN_PERIOD - table Field
     */
    public void setMinPeriod(Double minPeriod) {
        this.minPeriod = minPeriod;
    }

    public Double getMinPeriod() {
        return minPeriod;
    }

    /**
     * Setter/Getter for MAX_PERIOD - table Field
     */
    public void setMaxPeriod(Double maxPeriod) {
        this.maxPeriod = maxPeriod;
    }

    public Double getMaxPeriod() {
        return maxPeriod;
    }

    /**
     * Setter/Getter for MIN_AMT_LOAN - table Field
     */
    public void setMinAmtLoan(Double minAmtLoan) {
        this.minAmtLoan = minAmtLoan;
    }

    public Double getMinAmtLoan() {
        return minAmtLoan;
    }

    /**
     * Setter/Getter for MAX_AMT_LOAN - table Field
     */
    public void setMaxAmtLoan(Double maxAmtLoan) {
        this.maxAmtLoan = maxAmtLoan;
    }

    public Double getMaxAmtLoan() {
        return maxAmtLoan;
    }

    /**
     * Setter/Getter for MIN_INT_DEBIT - table Field
     */
    public void setMinIntDebit(Double minIntDebit) {
        this.minIntDebit = minIntDebit;
    }

    public Double getMinIntDebit() {
        return minIntDebit;
    }

    /**
     * Setter/Getter for SUBSIDY - table Field
     */
    public void setSubsidy(String subsidy) {
        this.subsidy = subsidy;
    }

    public String getSubsidy() {
        return subsidy;
    }

    /**
     * Setter/Getter for LOAN_PERIODS_MULTIPLES - table Field
     */
    public void setLoanPeriodsMultiples(Double loanPeriodsMultiples) {
        this.loanPeriodsMultiples = loanPeriodsMultiples;
    }

    public Double getLoanPeriodsMultiples() {
        return loanPeriodsMultiples;
    }

    /**
     * Setter/Getter for APPL_INTEREST - table Field
     */
    public void setApplInterest(Double applInterest) {
        this.applInterest = applInterest;
    }

    public Double getApplInterest() {
        return applInterest;
    }

    public String getChkFixed() {
        return chkFixed;
    }

    public void setChkFixed(String chkFixed) {
        this.chkFixed = chkFixed;
    }

    public Double getFixedMargin() {
        return fixedMargin;
    }

    public void setFixedMargin(Double fixedMargin) {
        this.fixedMargin = fixedMargin;
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
        strB.append(getTOString("fixedMargin", fixedMargin));
        strB.append(getTOString("chkFixed", chkFixed));
        strB.append(getTOString("calcType", calcType));
        strB.append(getTOString("minPeriod", minPeriod));
        strB.append(getTOString("maxPeriod", maxPeriod));
        strB.append(getTOString("minAmtLoan", minAmtLoan));
        strB.append(getTOString("maxAmtLoan", maxAmtLoan));
        strB.append(getTOString("minIntDebit", minIntDebit));
        strB.append(getTOString("subsidy", subsidy));
        strB.append(getTOString("loanPeriodsMultiples", loanPeriodsMultiples));
        strB.append(getTOString("applInterest", applInterest));
        strB.append(getTOString("reviewPeriod", reviewPeriod));
        strB.append(getTOString("eligibleDepForLoanAmt", eligibleDepForLoanAmt));
        strB.append(getTOString("depositRoundOff", depositRoundOff));
        strB.append(getTOString("maxPeriodChar", maxPeriodChar));
        strB.append(getTOString("depAmtMaturing", depAmtMaturing));
        strB.append(getTOString("depAmtMaturingPeriod", depAmtMaturingPeriod));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("fixedMargin", fixedMargin));
        strB.append(getTOXml("chkFixed", chkFixed));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("calcType", calcType));
        strB.append(getTOXml("minPeriod", minPeriod));
        strB.append(getTOXml("maxPeriod", maxPeriod));
        strB.append(getTOXml("minAmtLoan", minAmtLoan));
        strB.append(getTOXml("maxAmtLoan", maxAmtLoan));
        strB.append(getTOXml("minIntDebit", minIntDebit));
        strB.append(getTOXml("subsidy", subsidy));
        strB.append(getTOXml("loanPeriodsMultiples", loanPeriodsMultiples));
        strB.append(getTOXml("applInterest", applInterest));
        strB.append(getTOXml("reviewPeriod", reviewPeriod));
        strB.append(getTOXml("eligibleDepForLoanAmt", eligibleDepForLoanAmt));
        strB.append(getTOXml("depositRoundOff", depositRoundOff));
        strB.append(getTOXml("maxPeriodChar", maxPeriodChar));
        strB.append(getTOXml("depAmtMaturing", depAmtMaturing));
        strB.append(getTOString("depAmtMaturingPeriod", depAmtMaturingPeriod));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property reviewPeriod.
     *
     * @return Value of property reviewPeriod.
     */
    public java.lang.Double getReviewPeriod() {
        return reviewPeriod;
    }

    /**
     * Setter for property reviewPeriod.
     *
     * @param reviewPeriod New value of property reviewPeriod.
     */
    public void setReviewPeriod(java.lang.Double reviewPeriod) {
        this.reviewPeriod = reviewPeriod;
    }

    /**
     * Getter for property eligibleDepForLoanAmt.
     *
     * @return Value of property eligibleDepForLoanAmt.
     */
    public java.lang.Double getEligibleDepForLoanAmt() {
        return eligibleDepForLoanAmt;
    }

    /**
     * Setter for property eligibleDepForLoanAmt.
     *
     * @param eligibleDepForLoanAmt New value of property eligibleDepForLoanAmt.
     */
    public void setEligibleDepForLoanAmt(java.lang.Double eligibleDepForLoanAmt) {
        this.eligibleDepForLoanAmt = eligibleDepForLoanAmt;
    }

    /**
     * Getter for property depositRoundOff.
     *
     * @return Value of property depositRoundOff.
     */
    public java.lang.String getDepositRoundOff() {
        return depositRoundOff;
    }

    /**
     * Setter for property depositRoundOff.
     *
     * @param depositRoundOff New value of property depositRoundOff.
     */
    public void setDepositRoundOff(java.lang.String depositRoundOff) {
        this.depositRoundOff = depositRoundOff;
    }

    /**
     * Getter for property maxPeriodChar.
     *
     * @return Value of property maxPeriodChar.
     */
    public java.lang.String getMaxPeriodChar() {
        return maxPeriodChar;
    }

    /**
     * Setter for property maxPeriodChar.
     *
     * @param maxPeriodChar New value of property maxPeriodChar.
     */
    public void setMaxPeriodChar(java.lang.String maxPeriodChar) {
        this.maxPeriodChar = maxPeriodChar;
    }
}