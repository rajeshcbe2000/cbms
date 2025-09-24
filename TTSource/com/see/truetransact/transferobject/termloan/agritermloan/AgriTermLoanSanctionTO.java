/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanSanctionTO.java
 * @author shanmugavel
 * Created on Mon May 10 12:55:41 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan.agritermloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_SANCTION.
 */
public class AgriTermLoanSanctionTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String sanctionNo = "";
    private String sanctionAuthority = "";
    private Date sanctionDt = null;
    private String sanctionMode = "";
    private String remarks = "";
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String status = "";
    private String command = "";
    private Double slNo = null;
    private String statusBy = "";
    private Date statusDt = null;

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
     * Setter/Getter for SANCTION_NO - table Field
     */
    public void setSanctionNo(String sanctionNo) {
        this.sanctionNo = sanctionNo;
    }

    public String getSanctionNo() {
        return sanctionNo;
    }

    /**
     * Setter/Getter for SANCTION_AUTHORITY - table Field
     */
    public void setSanctionAuthority(String sanctionAuthority) {
        this.sanctionAuthority = sanctionAuthority;
    }

    public String getSanctionAuthority() {
        return sanctionAuthority;
    }

    /**
     * Setter/Getter for SANCTION_DT - table Field
     */
    public void setSanctionDt(Date sanctionDt) {
        this.sanctionDt = sanctionDt;
    }

    public Date getSanctionDt() {
        return sanctionDt;
    }

    /**
     * Setter/Getter for SANCTION_MODE - table Field
     */
    public void setSanctionMode(String sanctionMode) {
        this.sanctionMode = sanctionMode;
    }

    public String getSanctionMode() {
        return sanctionMode;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
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
        setKeyColumns("borrowNo" + KEY_VAL_SEPARATOR + "sanctionNo");
        return borrowNo + KEY_VAL_SEPARATOR + sanctionNo;
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
        strB.append(getTOString("sanctionAuthority", sanctionAuthority));
        strB.append(getTOString("sanctionDt", sanctionDt));
        strB.append(getTOString("sanctionMode", sanctionMode));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
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
        strB.append(getTOXml("sanctionAuthority", sanctionAuthority));
        strB.append(getTOXml("sanctionDt", sanctionDt));
        strB.append(getTOXml("sanctionMode", sanctionMode));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}