/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * SupplierTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */
package com.see.truetransact.transferobject.indend.suppliermaster;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.util.*;

/**
 *
 * @author aravind
 */
public class SupplierTO extends TransferObject implements Serializable {

    private String supplierNo = "";
    private String name = "";
    private String tinNo = "";
    private String address = "";
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String status = "";
    private Date createdDt = null;
    // private String currBranName="";
    // private String branCode="";
    private String createdBy = "";
    private HashMap _authorizeMap;
    private Integer result;
    private String suspenseAcHd = "";
    private String branchId = "";

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getSuspenseAcHd() {
        return suspenseAcHd;
    }

    public void setSuspenseAcHd(String suspenseAcHd) {
        this.suspenseAcHd = suspenseAcHd;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("supplierNo", supplierNo));
        // strB.append(getTOString("date", idate));
        //  strB.append(getTOString("details", details));
        strB.append(getTOString("tinNo", tinNo));
        strB.append(getTOString("address", address));
        strB.append(getTOString("name", name));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("suspenseAcHd",suspenseAcHd));
        strB.append(getTOString("branchId",branchId));
        // strB.append(getTOString("ActionTaken",actionTaken));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("supplierNo", supplierNo));
        // strB.append(getTOXml("Date", idate));
        strB.append(getTOXml("tinNo", tinNo));
        strB.append(getTOXml("address", address));
        strB.append(getTOXml("name", name));
        strB.append(getTOXml("statusBy", statusBy));
        //  strB.append(getTOXml("statusBy",statusBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("suspenseAcHd",suspenseAcHd));
        strB.append(getTOXml("branchId",branchId));
        // strB.append(getTOXml("ActionTaken",actionTaken));
        strB.append(getTOXmlEnd());
        return strB.toString();
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

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTinNo() {
        return tinNo;
    }

    public void setTinNo(String tinNo) {
        this.tinNo = tinNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
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

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }
}
