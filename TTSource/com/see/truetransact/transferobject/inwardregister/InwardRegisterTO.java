/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * InwardRegisterTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */
package com.see.truetransact.transferobject.inwardregister;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.util.*;

/**
 *
 * @author aravind
 */
public class InwardRegisterTO extends TransferObject implements Serializable {

    private String inwardNo = "";
    private Date idate = null;
    private String details = "";
    private String remarks = "";
    private String submittedBy = "";
    private String referenceNo = "";
    private String actionTaken = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String status = "";
    private Date createdDt = null;
    // private String currBranName="";
    // private String branCode="";
    private String createdBy = "";
    private HashMap _authorizeMap;
    private Integer result;
    private String branchId = "";  // Added by nithya on 14-07-2016 for 4796

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("inwardNo", inwardNo));
        strB.append(getTOString("date", idate));
        strB.append(getTOString("details", details));
        //  strB.append(getTOString("nameAddress", nameAddress));
        strB.append(getTOString("Remarks", remarks));
        strB.append(getTOString("SubmittedBy", submittedBy));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("ReferenceNo", referenceNo));
        strB.append(getTOString("ActionTaken", actionTaken));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("InwardNo", inwardNo));
        strB.append(getTOXml("Date", idate));
        strB.append(getTOXml("Details", details));
        strB.append(getTOXml("Remarks", remarks));
        strB.append(getTOXml("SubmittedBy", submittedBy));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("ReferenceNo", referenceNo));
        strB.append(getTOXml("ActionTaken", actionTaken));
        strB.append(getTOXml("ActionTaken", actionTaken));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property InwardNo.
     *
     * @return Value of property InwardNo.
     */
    /**
     * Getter for property Date.
     *
     * @return Value of property Date.
     */
    /**
     * Getter for property Details.
     *
     * @return Value of property Details.
     *
     *
     * /**
     * Getter for property Remarks.
     * @return Value of property Remarks.
     *
     * Setter for property SubmittedBy.
     * @param SubmittedBy New value of property SubmittedBy.
     */
    /**
     * Setter for property ActionTaken.
     *
     * @param ActionTaken New value of property ActionTaken.
     *
     * /**
     * Setter for property statusBy.
     * @param statusBy New value of property statusBy.
     */
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
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property createdDt.
     *
     * @param createdDt New value of property createdDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public Integer getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     * Getter for property inwardNo.
     *
     * @return Value of property inwardNo.
     */
    public java.lang.String getInwardNo() {
        return inwardNo;
    }

    /**
     * Setter for property inwardNo.
     *
     * @param inwardNo New value of property inwardNo.
     */
    public void setInwardNo(java.lang.String inwardNo) {
        this.inwardNo = inwardNo;
    }

    /**
     * Getter for property details.
     *
     * @return Value of property details.
     */
    public java.lang.String getDetails() {
        return details;
    }

    /**
     * Setter for property details.
     *
     * @param details New value of property details.
     */
    public void setDetails(java.lang.String details) {
        this.details = details;
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
     * Getter for property submittedBy.
     *
     * @return Value of property submittedBy.
     */
    public java.lang.String getSubmittedBy() {
        return submittedBy;
    }

    /**
     * Getter for property referenceNo.
     *
     * @return Value of property referenceNo.
     */
    public java.lang.String getReferenceNo() {
        return referenceNo;
    }

    /**
     * Setter for property referenceNo.
     *
     * @param referenceNo New value of property referenceNo.
     */
    public void setReferenceNo(java.lang.String referenceNo) {
        this.referenceNo = referenceNo;
    }

    /**
     * Setter for property actionTaken.
     *
     * @param actionTaken New value of property actionTaken.
     */
    /**
     * Setter for property submittedBy.
     *
     * @param submittedBy New value of property submittedBy.
     */
    /**
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Getter for property idate.
     *
     * @return Value of property idate.
     */
    public java.util.Date getIdate() {
        return idate;
    }

    /**
     * Setter for property idate.
     *
     * @param idate New value of property idate.
     */
    public void setIdate(java.util.Date idate) {
        this.idate = idate;
    }

    /**
     * Getter for property actionTaken.
     *
     * @return Value of property actionTaken.
     */
    public String getActionTaken() {
        return actionTaken;
    }

    /**
     * Setter for property actionTaken.
     *
     * @param actionTaken New value of property actionTaken.
     */
    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    /**
     * Setter for property submittedBy.
     *
     * @param submittedBy New value of property submittedBy.
     */
    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    
    
}
