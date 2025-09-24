/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OutwardRegisterTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */
package com.see.truetransact.transferobject.outwardregister;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.util.*;

/**
 *
 * @author aravind
 */
public class OutwardRegisterTO extends TransferObject implements Serializable {

    private String address = "";
    private String cbmmessenger = "";
    private String outwardNo = "";
    private Date odate = null;
    private String branchId = ""; // Added by nithya on 14-07-2016 for 4796
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCbmmessenger() {
        return cbmmessenger;
    }

    public void setCbmmessenger(String cbmmessenger) {
        this.cbmmessenger = cbmmessenger;
    }
    private String details = "";
    private String remarks = "";
    //private String submittedBy= "";
    private String referenceNo = "";
    //private String actionTaken="";
    //private String statusBy = "";
    //private Date statusDt=null;
    private String status = "";
    //private Date createdDt=null;
    // private String currBranName="";
    // private String branCode="";
    //private String createdBy = "";
    private HashMap _authorizeMap;
    private Integer result;

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("inwardNo", outwardNo));
        strB.append(getTOString("date", odate));
        strB.append(getTOString("details", details));
        //  strB.append(getTOString("nameAddress", nameAddress));
        strB.append(getTOString("Remarks", remarks));
        //strB.append(getTOString("SubmittedBy",submittedBy));
        //strB.append(getTOString("statusBy",statusBy));
        strB.append(getTOString("status", status));
        //strB.append(getTOString("createdDt",createdDt));
        strB.append(getTOString("ReferenceNo", referenceNo));
        strB.append(getTOString("cbmmessenger", cbmmessenger));
        strB.append(getTOString("address", address));
        strB.append(getTOString("branchId", branchId));
        //strB.append(getTOString("ActionTaken",actionTaken));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("OutwardNo", outwardNo));
        strB.append(getTOXml("Date", odate));
        strB.append(getTOXml("Details", details));
        strB.append(getTOXml("Remarks", remarks));
        //strB.append(getTOXml("SubmittedBy",submittedBy));
        //strB.append(getTOXml("statusBy",statusBy));
        //strB.append(getTOXml("statusBy",statusBy));
        strB.append(getTOXml("status", status));
        //strB.append(getTOXml("createdDt",createdDt));
        strB.append(getTOXml("ReferenceNo", referenceNo));
        strB.append(getTOXml("cbmmessenger", cbmmessenger));
        strB.append(getTOXml("address", address));
        strB.append(getTOXml("branchId", branchId));

        //strB.append(getTOXml("ActionTaken",actionTaken));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property outwardNo.
     *
     * @return Value of property outwardNo.
     */
    public java.lang.String getOutwardNo() {
        return outwardNo;
    }

    /**
     * Setter for property outwardNo.
     *
     * @param outwardNo New value of property outwardNo.
     */
    public void setOutwardNo(java.lang.String outwardNo) {
        this.outwardNo = outwardNo;
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
     * Getter for property odate.
     *
     * @return Value of property odate.
     */
    public java.util.Date getOdate() {
        return odate;
    }

    /**
     * Setter for property odate.
     *
     * @param odate New value of property odate.
     */
    public void setOdate(java.util.Date odate) {
        this.odate = odate;
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

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    
    
    
}
