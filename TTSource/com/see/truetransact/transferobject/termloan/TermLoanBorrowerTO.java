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
public class TermLoanBorrowerTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String custId = "";
    private String constitution = "";
    private String category = "";
    private String references = "";
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String status = "";
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String branchCode = "";
    private String shgID = "";
    private String applicationNo = "";

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
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
    }

    /**
     * Setter/Getter for CONSTITUTION - table Field
     */
    public void setConstitution(String constitution) {
        this.constitution = constitution;
    }

    public String getConstitution() {
        return constitution;
    }

    /**
     * Setter/Getter for CATEGORY - table Field
     */
    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Setter/Getter for REFERENCES - table Field
     */
    public void setReferences(String references) {
        this.references = references;
    }

    public String getReferences() {
        return references;
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
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
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
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("borrowNo");
        return borrowNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("constitution", constitution));
        strB.append(getTOString("shgID", shgID));
        strB.append(getTOString("applicationNo", applicationNo));
        strB.append(getTOString("category", category));
        strB.append(getTOString("references", references));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
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
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("constitution", constitution));
        strB.append(getTOString("shgID", shgID));
        strB.append(getTOString("applicationNo", applicationNo));
        strB.append(getTOXml("category", category));
        strB.append(getTOXml("references", references));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property shgID.
     *
     * @return Value of property shgID.
     */
    public java.lang.String getShgID() {
        return shgID;
    }

    /**
     * Setter for property shgID.
     *
     * @param shgID New value of property shgID.
     */
    public void setShgID(java.lang.String shgID) {
        this.shgID = shgID;
    }

    /**
     * Getter for property applicationNo.
     *
     * @return Value of property applicationNo.
     */
    public java.lang.String getApplicationNo() {
        return applicationNo;
    }

    /**
     * Setter for property applicationNo.
     *
     * @param applicationNo New value of property applicationNo.
     */
    public void setApplicationNo(java.lang.String applicationNo) {
        this.applicationNo = applicationNo;
    }
}