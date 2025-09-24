/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanSanctionFacilityTO.java
 * @author shanmugavel
 * Created on Mon May 10 12:48:39 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_SANCTION_DETAILS.
 */
public class TermLoanSanctionFacilityTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String sanctionNo = "";
    private Double slNo = null;
    private String facilityType = "";
    private Double limit = null;
    private Date fromDt = null;
    private Date toDt = null;
    private String status = "";
//        private String command = "";
    private Double noInstall = null;
    private Double repaymentFrequency = null;
    private String productId = "";
    private Date repaymentDt = null;
    private String moratoriumGiven = "";
    private Double noMoratorium = null;
    private String statusBy = "";
    private Date statusDt = null;
    private Double initialMoneyDeposit = null;
    private String odRenewal = "";
    private String eligibleAmt = "";

    /**
     * Setter/Getter for BORROW_NO - table Field
     */
    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    /**
     * Setter/Getter for SANCTION_NO - table Field
     */
    public void setSanctionNo(String sanctionNo) {
        this.sanctionNo = sanctionNo;
    }

    public String getSanctionNo() {
        return sanctionNo;
    }

    /**
     * Setter/Getter for SL_NO - table Field
     */
    public void setSlNo(Double slNo) {
        this.slNo = slNo;
    }

    public Double getSlNo() {
        return slNo;
    }

    /**
     * Setter/Getter for FACILITY_TYPE - table Field
     */
    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getFacilityType() {
        return facilityType;
    }

    /**
     * Setter/Getter for LIMIT - table Field
     */
    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public Double getLimit() {
        return limit;
    }

    /**
     * Setter/Getter for FROM_DT - table Field
     */
    public void setFromDt(Date fromDt) {
        this.fromDt = fromDt;
    }

    public Date getFromDt() {
        return fromDt;
    }

    /**
     * Setter/Getter for TO_DT - table Field
     */
    public void setToDt(Date toDt) {
        this.toDt = toDt;
    }

    public Date getToDt() {
        return toDt;
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
     * Setter/Getter for NO_INSTALL - table Field
     */
    public void setNoInstall(Double noInstall) {
        this.noInstall = noInstall;
    }

    public Double getNoInstall() {
        return noInstall;
    }

    /**
     * Setter/Getter for REPAYMENT_FREQUENCY - table Field
     */
    public void setRepaymentFrequency(Double repaymentFrequency) {
        this.repaymentFrequency = repaymentFrequency;
    }

    public Double getRepaymentFrequency() {
        return repaymentFrequency;
    }

    /**
     * Setter/Getter for PRODUCT_ID - table Field
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    /**
     * Setter/Getter for REPAYMENT_DT - table Field
     */
    public void setRepaymentDt(Date repaymentDt) {
        this.repaymentDt = repaymentDt;
    }

    public Date getRepaymentDt() {
        return repaymentDt;
    }

    /**
     * Setter/Getter for MORATORIUM_GIVEN - table Field
     */
    public void setMoratoriumGiven(String moratoriumGiven) {
        this.moratoriumGiven = moratoriumGiven;
    }

    public String getMoratoriumGiven() {
        return moratoriumGiven;
    }

    /**
     * Setter/Getter for NO_MORATORIUM - table Field
     */
    public void setNoMoratorium(Double noMoratorium) {
        this.noMoratorium = noMoratorium;
    }

    public Double getNoMoratorium() {
        return noMoratorium;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("borrowNo" + KEY_VAL_SEPARATOR + "sanctionNo" + KEY_VAL_SEPARATOR + "slNo");
        return borrowNo + KEY_VAL_SEPARATOR + sanctionNo + KEY_VAL_SEPARATOR + slNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("sanctionNo", sanctionNo));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("facilityType", facilityType));
        strB.append(getTOString("limit", limit));
        strB.append(getTOString("initialMoneyDeposit", initialMoneyDeposit));
        strB.append(getTOString("fromDt", fromDt));
        strB.append(getTOString("toDt", toDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("noInstall", noInstall));
        strB.append(getTOString("repaymentFrequency", repaymentFrequency));
        strB.append(getTOString("productId", productId));
        strB.append(getTOString("repaymentDt", repaymentDt));
        strB.append(getTOString("moratoriumGiven", moratoriumGiven));
        strB.append(getTOString("noMoratorium", noMoratorium));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("odRenewal", odRenewal));
        strB.append(getTOString("eligibleAmt", eligibleAmt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("sanctionNo", sanctionNo));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("facilityType", facilityType));
        strB.append(getTOXml("limit", limit));
        strB.append(getTOXml("initialMoneyDeposit", initialMoneyDeposit));
        strB.append(getTOXml("fromDt", fromDt));
        strB.append(getTOXml("toDt", toDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("noInstall", noInstall));
        strB.append(getTOXml("repaymentFrequency", repaymentFrequency));
        strB.append(getTOXml("productId", productId));
        strB.append(getTOXml("repaymentDt", repaymentDt));
        strB.append(getTOXml("moratoriumGiven", moratoriumGiven));
        strB.append(getTOXml("noMoratorium", noMoratorium));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("odRenewal", odRenewal));
        strB.append(getTOXml("eligibleAmt", eligibleAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property initialMoneyDeposit.
     *
     * @return Value of property initialMoneyDeposit.
     */
    public java.lang.Double getInitialMoneyDeposit() {
        return initialMoneyDeposit;
    }

    /**
     * Setter for property initialMoneyDeposit.
     *
     * @param initialMoneyDeposit New value of property initialMoneyDeposit.
     */
    public void setInitialMoneyDeposit(java.lang.Double initialMoneyDeposit) {
        this.initialMoneyDeposit = initialMoneyDeposit;
    }

    /**
     * Getter for property odRenewal.
     *
     * @return Value of property odRenewal.
     */
    public java.lang.String getOdRenewal() {
        return odRenewal;
    }

    /**
     * Setter for property odRenewal.
     *
     * @param odRenewal New value of property odRenewal.
     */
    public void setOdRenewal(java.lang.String odRenewal) {
        this.odRenewal = odRenewal;
    }

    /**
     * Getter for property eligibleAmt.
     *
     * @return Value of property eligibleAmt.
     */
    public java.lang.String getEligibleAmt() {
        return eligibleAmt;
    }

    /**
     * Setter for property eligibleAmt.
     *
     * @param eligibleAmt New value of property eligibleAmt.
     */
    public void setEligibleAmt(java.lang.String eligibleAmt) {
        this.eligibleAmt = eligibleAmt;
    }
}