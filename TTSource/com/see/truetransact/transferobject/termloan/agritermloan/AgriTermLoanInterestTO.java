/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInterestTO.java
 * @author shanmugavel
 * Created on Wed May 05 15:47:53 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan.agritermloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_INT_MAINTENANCE.
 */
public class AgriTermLoanInterestTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String prodId = "";
    private Date fromDt = null;
    private Date toDt = null;
    private Double fromAmt = null;
    private Double toAmt = null;
    private Double interest = null;
    private Double penalInterest = null;
    private Double againstClearingInt = null;
    private Double statementPenal = null;
    private Double interestExpiryLimit = null;
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String acctNum = "";
    private String status = "";
    private Double slno = null;
    private String command = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String activeStatus = "";
    private Date retCreateDt = null;

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

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
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
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
     * Setter/Getter for FROM_AMT - table Field
     */
    public void setFromAmt(Double fromAmt) {
        this.fromAmt = fromAmt;
    }

    public Double getFromAmt() {
        return fromAmt;
    }

    /**
     * Setter/Getter for TO_AMT - table Field
     */
    public void setToAmt(Double toAmt) {
        this.toAmt = toAmt;
    }

    public Double getToAmt() {
        return toAmt;
    }

    /**
     * Setter/Getter for INTEREST - table Field
     */
    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Double getInterest() {
        return interest;
    }

    /**
     * Setter/Getter for PENAL_INTEREST - table Field
     */
    public void setPenalInterest(Double penalInterest) {
        this.penalInterest = penalInterest;
    }

    public Double getPenalInterest() {
        return penalInterest;
    }

    /**
     * Setter/Getter for AGAINST_CLEARING_INT - table Field
     */
    public void setAgainstClearingInt(Double againstClearingInt) {
        this.againstClearingInt = againstClearingInt;
    }

    public Double getAgainstClearingInt() {
        return againstClearingInt;
    }

    /**
     * Setter/Getter for STATEMENT_PENAL - table Field
     */
    public void setStatementPenal(Double statementPenal) {
        this.statementPenal = statementPenal;
    }

    public Double getStatementPenal() {
        return statementPenal;
    }

    /**
     * Setter/Getter for INTEREST_EXPIRY_LIMIT - table Field
     */
    public void setInterestExpiryLimit(Double interestExpiryLimit) {
        this.interestExpiryLimit = interestExpiryLimit;
    }

    public Double getInterestExpiryLimit() {
        return interestExpiryLimit;
    }

    /**
     * Setter/Getter for AUTHORIZE_REMARKS - table Field
     */
    public void setAuthorizeRemarks(String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    public String getAuthorizeRemarks() {
        return authorizeRemarks;
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
     * Setter/Getter for ACCT_NUM - table Field
     */
    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctNum() {
        return acctNum;
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
     * Setter/Getter for SLNO - table Field
     */
    public void setSlno(Double slno) {
        this.slno = slno;
    }

    public Double getSlno() {
        return slno;
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
        setKeyColumns("acctNum" + KEY_VAL_SEPARATOR + "slno");
        return acctNum + KEY_VAL_SEPARATOR + slno;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("fromDt", fromDt));
        strB.append(getTOString("toDt", toDt));
        strB.append(getTOString("fromAmt", fromAmt));
        strB.append(getTOString("toAmt", toAmt));
        strB.append(getTOString("interest", interest));
        strB.append(getTOString("penalInterest", penalInterest));
        strB.append(getTOString("againstClearingInt", againstClearingInt));
        strB.append(getTOString("statementPenal", statementPenal));
        strB.append(getTOString("interestExpiryLimit", interestExpiryLimit));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("status", status));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("activeStatus", activeStatus));
        strB.append(getTOString("retCreateDt", retCreateDt));
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
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("fromDt", fromDt));
        strB.append(getTOXml("toDt", toDt));
        strB.append(getTOXml("fromAmt", fromAmt));
        strB.append(getTOXml("toAmt", toAmt));
        strB.append(getTOXml("interest", interest));
        strB.append(getTOXml("penalInterest", penalInterest));
        strB.append(getTOXml("againstClearingInt", againstClearingInt));
        strB.append(getTOXml("statementPenal", statementPenal));
        strB.append(getTOXml("interestExpiryLimit", interestExpiryLimit));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("activeStatus", activeStatus));
        strB.append(getTOXml("retCreateDt", retCreateDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property activeStatus.
     *
     * @return Value of property activeStatus.
     */
    public java.lang.String getActiveStatus() {
        return activeStatus;
    }

    /**
     * Setter for property activeStatus.
     *
     * @param activeStatus New value of property activeStatus.
     */
    public void setActiveStatus(java.lang.String activeStatus) {
        this.activeStatus = activeStatus;
    }

    /**
     * Getter for property retCreateDt.
     *
     * @return Value of property retCreateDt.
     */
    public java.util.Date getRetCreateDt() {
        return retCreateDt;
    }

    /**
     * Setter for property retCreateDt.
     *
     * @param retCreateDt New value of property retCreateDt.
     */
    public void setRetCreateDt(java.util.Date retCreateDt) {
        this.retCreateDt = retCreateDt;
    }
}
