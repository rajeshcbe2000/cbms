/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanEligibilityTO.java
 *
 * Created on 4 February, 2013, 11:24 AM
 */
package com.see.truetransact.transferobject.product.loan.loaneligibilitymaintenance;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class LoanEligibilityTO extends TransferObject implements Serializable {

    String cropType = "";
    Double eligibleAmt = null;
    Date fromDate = null;
    Date toDate = null;
    String status = "";
    String statusBy = "";
    Date statusDate = null;
    String authStatus = "";
    String authBy = "";
    Date authDate = null;
    String slno = "";

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("cropType", cropType));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("status", status));
        strB.append(getTOString("eligibleAmt", eligibleAmt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("authStatus", authStatus));
        strB.append(getTOString("authBy", authBy));
        strB.append(getTOString("authDate", authDate));
        strB.append(getTOString("slno", slno));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("cropType", cropType));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("eligibleAmt", eligibleAmt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("authStatus", authStatus));
        strB.append(getTOXml("authBy", authBy));
        strB.append(getTOXml("authDate", authDate));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property cropType.
     *
     * @return Va;lue of property cropType.
     */
    public java.lang.String getCropType() {
        return cropType;
    }

    /**
     * Setter for property cropType.
     *
     * @param cropType New value of property cropType.
     */
    public void setCropType(java.lang.String cropType) {
        this.cropType = cropType;
    }

    /**
     * Getter for property fromDate.
     *
     * @return Value of property fromDate.
     */
    public java.util.Date getFromDate() {
        return fromDate;
    }

    /**
     * Setter for property fromDate.
     *
     * @param fromDate New value of property fromDate.
     */
    public void setFromDate(java.util.Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Getter for property toDate.
     *
     * @return Value of property toDate.
     */
    public java.util.Date getToDate() {
        return toDate;
    }

    /**
     * Setter for property toDate.
     *
     * @param toDate New value of property toDate.
     */
    public void setToDate(java.util.Date toDate) {
        this.toDate = toDate;
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
     * Getter for property eligibleAmt.
     *
     * @return Value of property eligibleAmt.
     */
    public Double getEligibleAmt() {
        return eligibleAmt;
    }

    /**
     * Setter for property eligibleAmt.
     *
     * @param eligibleAmt New value of property eligibleAmt.
     */
    public void setEligibleAmt(Double eligibleAmt) {
        this.eligibleAmt = eligibleAmt;
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
     * Getter for property statusDate.
     *
     * @return Value of property statusDate.
     */
    public java.util.Date getStatusDate() {
        return statusDate;
    }

    /**
     * Setter for property statusDate.
     *
     * @param statusDate New value of property statusDate.
     */
    public void setStatusDate(java.util.Date statusDate) {
        this.statusDate = statusDate;
    }

    /**
     * Getter for property authStatus.
     *
     * @return Value of property authStatus.
     */
    public java.lang.String getAuthStatus() {
        return authStatus;
    }

    /**
     * Setter for property authStatus.
     *
     * @param authStatus New value of property authStatus.
     */
    public void setAuthStatus(java.lang.String authStatus) {
        this.authStatus = authStatus;
    }

    /**
     * Getter for property authBy.
     *
     * @return Value of property authBy.
     */
    public java.lang.String getAuthBy() {
        return authBy;
    }

    /**
     * Setter for property authBy.
     *
     * @param authBy New value of property authBy.
     */
    public void setAuthBy(java.lang.String authBy) {
        this.authBy = authBy;
    }

    /**
     * Getter for property authDate.
     *
     * @return Value of property authDate.
     */
    public java.util.Date getAuthDate() {
        return authDate;
    }

    /**
     * Setter for property authDate.
     *
     * @param authDate New value of property authDate.
     */
    public void setAuthDate(java.util.Date authDate) {
        this.authDate = authDate;
    }

    /**
     * Getter for property slno.
     *
     * @return Value of property slno.
     */
    public java.lang.String getSlno() {
        return slno;
    }

    /**
     * Setter for property slno.
     *
     * @param slno New value of property slno.
     */
    public void setSlno(java.lang.String slno) {
        this.slno = slno;
    }
}
