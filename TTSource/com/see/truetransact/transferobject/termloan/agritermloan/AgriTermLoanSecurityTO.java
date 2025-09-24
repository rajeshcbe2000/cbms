/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanSecurityTO.java
 * 
 * Created on Wed Mar 16 16:19:00 IST 2005
 */
package com.see.truetransact.transferobject.termloan.agritermloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_SECURITY_DETAILS.
 */
public class AgriTermLoanSecurityTO extends TransferObject implements Serializable {

    private String acctNum = "";
    private String custId = "";
    private Double securityNo = null;
    private Date fromDt = null;
    private Date toDt = null;
    private Double securityAmt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String borrowNo = "";
    private Double slno = null;
    private Double margin = null;
    private Double eligibleLoanAmt = null;

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
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
    }

    /**
     * Setter/Getter for SECURITY_NO - table Field
     */
    public void setSecurityNo(Double securityNo) {
        this.securityNo = securityNo;
    }

    public Double getSecurityNo() {
        return securityNo;
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
     * Setter/Getter for SECURITY_AMT - table Field
     */
    public void setSecurityAmt(Double securityAmt) {
        this.securityAmt = securityAmt;
    }

    public Double getSecurityAmt() {
        return securityAmt;
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
     * Setter/Getter for BORROW_NO - table Field
     */
    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getBorrowNo() {
        return borrowNo;
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
     * Setter/Getter for MARGIN - table Field
     */
    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public Double getMargin() {
        return margin;
    }

    /**
     * Setter/Getter for ELIGIBLE_LOAN_AMT - table Field
     */
    public void setEligibleLoanAmt(Double eligibleLoanAmt) {
        this.eligibleLoanAmt = eligibleLoanAmt;
    }

    public Double getEligibleLoanAmt() {
        return eligibleLoanAmt;
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
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("securityNo", securityNo));
        strB.append(getTOString("fromDt", fromDt));
        strB.append(getTOString("toDt", toDt));
        strB.append(getTOString("securityAmt", securityAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("margin", margin));
        strB.append(getTOString("eligibleLoanAmt", eligibleLoanAmt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("securityNo", securityNo));
        strB.append(getTOXml("fromDt", fromDt));
        strB.append(getTOXml("toDt", toDt));
        strB.append(getTOXml("securityAmt", securityAmt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("margin", margin));
        strB.append(getTOXml("eligibleLoanAmt", eligibleLoanAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}