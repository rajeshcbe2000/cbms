/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerPassportTO.java
 * 
 * Created on Wed Feb 16 09:38:12 IST 2005 swaroop
 */
package com.see.truetransact.transferobject.customer;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUSTOMER_SUSPENSION.
 */
public class CustomerSuspensionTO extends TransferObject implements Serializable {

    private String custId = "";
    private String status = "";
    private Date suspendedFromDate = null;
    private String suspendedBy = "";
    private String revokedBy = "";
    private Date statusDate = null;
    private String remarks = "";
    private Date revokedDate = null;

    public String getKeyData() {
        setKeyColumns(custId);
        return custId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));

        strB.append(getTOString("custId", custId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("suspendedFromDate", suspendedFromDate));
        strB.append(getTOString("suspendedBy", suspendedBy));
        strB.append(getTOString("revokedBy", revokedBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("revokedDate", revokedDate));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));

        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("suspendedFromDate", suspendedFromDate));
        strB.append(getTOXml("suspendedBy", suspendedBy));
        strB.append(getTOXml("revokedBy", revokedBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("revokedDate", revokedDate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property custId.
     *
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }

    /**
     * Setter for property custId.
     *
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
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
     * Getter for property suspendedFromDate.
     *
     * @return Value of property suspendedFromDate.
     */
    public java.util.Date getSuspendedFromDate() {
        return suspendedFromDate;
    }

    /**
     * Setter for property suspendedFromDate.
     *
     * @param suspendedFromDate New value of property suspendedFromDate.
     */
    public void setSuspendedFromDate(java.util.Date suspendedFromDate) {
        this.suspendedFromDate = suspendedFromDate;
    }

    /**
     * Getter for property suspendedBy.
     *
     * @return Value of property suspendedBy.
     */
    public java.lang.String getSuspendedBy() {
        return suspendedBy;
    }

    /**
     * Setter for property suspendedBy.
     *
     * @param suspendedBy New value of property suspendedBy.
     */
    public void setSuspendedBy(java.lang.String suspendedBy) {
        this.suspendedBy = suspendedBy;
    }

    /**
     * Getter for property revokedBy.
     *
     * @return Value of property revokedBy.
     */
    public java.lang.String getRevokedBy() {
        return revokedBy;
    }

    /**
     * Setter for property revokedBy.
     *
     * @param revokedBy New value of property revokedBy.
     */
    public void setRevokedBy(java.lang.String revokedBy) {
        this.revokedBy = revokedBy;
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
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }

    /**
     * Getter for property revokedDate.
     *
     * @return Value of property revokedDate.
     */
    public java.util.Date getRevokedDate() {
        return revokedDate;
    }

    /**
     * Setter for property revokedDate.
     *
     * @param revokedDate New value of property revokedDate.
     */
    public void setRevokedDate(java.util.Date revokedDate) {
        this.revokedDate = revokedDate;
    }
}