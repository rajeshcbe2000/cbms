/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanBorrowerTO.java
 * 
 * Created on Wed Apr 13 18:02:42 IST 2005
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_BORROWER.
 */
public class TermLoanPenalWaiveOffTO extends TransferObject implements Serializable {

    private String acctNum = "";
    private Date waiveDt = null;
    private Double interestAmt = null;
    private Double penalAmt = null;
    private String authorizeStatus = null;
    private String status = "";
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String waiveOffId = "";
    //added by rishad 15/03/2014 
    private Double waiveAmt=null;
    private String remarks=null;
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
        strB.append(getTOString("waiveDt", waiveDt));
        strB.append(getTOString("interestAmt", interestAmt));
        strB.append(getTOString("penalAmt", penalAmt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("waiveOffId", waiveOffId));
        strB.append(getTOString("waiveAmt",waiveAmt));
        strB.append(getTOString("remarks",remarks));


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
        strB.append(getTOXml("waiveDt", waiveDt));
        strB.append(getTOXml("interestAmt", interestAmt));
        strB.append(getTOString("penalAmt", penalAmt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("waiveOffId", waiveOffId));
        strB.append(getTOXml("waiveAmt", waiveAmt));
        strB.append(getTOXml("remarks",remarks));
        
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
     * Getter for property authorizedBy.
     *
     * @return Value of property authorizedBy.
     */
    public java.lang.String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter for property authorizedBy.
     *
     * @param authorizedBy New value of property authorizedBy.
     */
    public void setAuthorizedBy(java.lang.String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    /**
     * Getter for property authorizedDt.
     *
     * @return Value of property authorizedDt.
     */
    public java.util.Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter for property authorizedDt.
     *
     * @param authorizedDt New value of property authorizedDt.
     */
    public void setAuthorizedDt(java.util.Date authorizedDt) {
        this.authorizedDt = authorizedDt;
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
     * Getter for property waiveDt.
     *
     * @return Value of property waiveDt.
     */
    public java.util.Date getWaiveDt() {
        return waiveDt;
    }

    /**
     * Setter for property waiveDt.
     *
     * @param waiveDt New value of property waiveDt.
     */
    public void setWaiveDt(java.util.Date waiveDt) {
        this.waiveDt = waiveDt;
    }

    /**
     * Getter for property interestAmt.
     *
     * @return Value of property interestAmt.
     */
    public java.lang.Double getInterestAmt() {
        return interestAmt;
    }

    /**
     * Setter for property interestAmt.
     *
     * @param interestAmt New value of property interestAmt.
     */
    
    public void setInterestAmt(java.lang.Double interestAmt) {
        this.interestAmt = interestAmt;
    }

    /**
     * Getter for property penalAmt.
     *
     * @return Value of property penalAmt.
     */
    public java.lang.Double getPenalAmt() {
        return penalAmt;
    }

    /**
     * Setter for property penalAmt.
     *
     * @param penalAmt New value of property penalAmt.
     */
    public void setPenalAmt(java.lang.Double penalAmt) {
        this.penalAmt = penalAmt;
    }

    /**
     * Getter for property waiveOffId.
     *
     * @return Value of property waiveOffId.
     */
    public java.lang.String getWaiveOffId() {
        return waiveOffId;
    }

    /**
     * Setter for property waiveOffId.
     *
     * @param waiveOffId New value of property waiveOffId.
     */
    public void setWaiveOffId(java.lang.String waiveOffId) {
        this.waiveOffId = waiveOffId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Double getWaiveAmt() {
        return waiveAmt;
    }

    public void setWaiveAmt(Double waiveAmt) {
        this.waiveAmt = waiveAmt;
    }       
}