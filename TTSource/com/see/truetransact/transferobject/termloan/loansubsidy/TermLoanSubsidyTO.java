/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TermLoanSubsidyTO.java
 *
 * Created on July 24, 2012, 12:25 PM
 */
package com.see.truetransact.transferobject.termloan.loansubsidy;

/**
 *
 * @author admin
 */
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

public class TermLoanSubsidyTO extends TransferObject implements Serializable {

    private String acctNum = "";
    private String adjustAchd = "";
    private Double subsidyAmt = null;
    private Double transAmt = null;
    private Date subsidyDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDate = null;
    private String subsidyId = "";
    private String branchCode = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNum");
        return acctNum;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("adjustAchd", adjustAchd));
        strB.append(getTOString("subsidyAmt", subsidyAmt));
        strB.append(getTOString("transAmt", transAmt));
        strB.append(getTOString("subsidyDt", subsidyDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("subsidyId", subsidyId));
        strB.append(getTOString("branchCode", branchCode));
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
        strB.append(getTOXml("adjustAchd", adjustAchd));
        strB.append(getTOXml("subsidyAmt", subsidyAmt));
        strB.append(getTOXml("transAmt", transAmt));
        strB.append(getTOXml("subsidyDt", subsidyDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("subsidyId", subsidyId));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property acctNum.
     *
     * @return Value of property acctNum.
     */
    public java.lang.String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter for property acctNum.
     *
     * @param acctNum New value of property acctNum.
     */
    public void setAcctNum(java.lang.String acctNum) {
        this.acctNum = acctNum;
    }

    /**
     * Getter for property adjustAchd.
     *
     * @return Value of property adjustAchd.
     */
    public java.lang.String getAdjustAchd() {
        return adjustAchd;
    }

    /**
     * Setter for property adjustAchd.
     *
     * @param adjustAchd New value of property adjustAchd.
     */
    public void setAdjustAchd(java.lang.String adjustAchd) {
        this.adjustAchd = adjustAchd;
    }

    /**
     * Getter for property subsidyAmt.
     *
     * @return Value of property subsidyAmt.
     */
    public java.lang.Double getSubsidyAmt() {
        return subsidyAmt;
    }

    /**
     * Setter for property subsidyAmt.
     *
     * @param subsidyAmt New value of property subsidyAmt.
     */
    public void setSubsidyAmt(java.lang.Double subsidyAmt) {
        this.subsidyAmt = subsidyAmt;
    }

    /**
     * Getter for property transAmt.
     *
     * @return Value of property transAmt.
     */
    public java.lang.Double getTransAmt() {
        return transAmt;
    }

    /**
     * Setter for property transAmt.
     *
     * @param transAmt New value of property transAmt.
     */
    public void setTransAmt(java.lang.Double transAmt) {
        this.transAmt = transAmt;
    }

    /**
     * Getter for property subsidyDt.
     *
     * @return Value of property subsidyDt.
     */
    public java.util.Date getSubsidyDt() {
        return subsidyDt;
    }

    /**
     * Setter for property subsidyDt.
     *
     * @param subsidyDt New value of property subsidyDt.
     */
    public void setSubsidyDt(java.util.Date subsidyDt) {
        this.subsidyDt = subsidyDt;
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
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property subsidyId.
     *
     * @return Value of property subsidyId.
     */
    public java.lang.String getSubsidyId() {
        return subsidyId;
    }

    /**
     * Setter for property subsidyId.
     *
     * @param subsidyId New value of property subsidyId.
     */
    public void setSubsidyId(java.lang.String subsidyId) {
        this.subsidyId = subsidyId;
    }

    /**
     * Getter for property branchCode.
     *
     * @return Value of property branchCode.
     */
    public java.lang.String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter for property branchCode.
     *
     * @param branchCode New value of property branchCode.
     */
    public void setBranchCode(java.lang.String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * Getter for property authorizeDate.
     *
     * @return Value of property authorizeDate.
     */
    public java.util.Date getAuthorizeDate() {
        return authorizeDate;
    }

    /**
     * Setter for property authorizeDate.
     *
     * @param authorizeDate New value of property authorizeDate.
     */
    public void setAuthorizeDate(java.util.Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }
}
