/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareResolutionTO.java
 * 
 * Created on Thu Apr 28 12:55:24 IST 2005
 */
package com.see.truetransact.transferobject.share;

import com.see.truetransact.transferobject.share.shareresolution.*;
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_RESOLUTION.
 */
public class NmfMaintenanceTO extends TransferObject implements Serializable {

    private String nominalMemNo = "";
    private Date openingDt = null;
    private String custId = "";
    private Double nominalMemFee = null;
    private String statusBy = "";
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String status = "";
    private String createdBy = "";
    private Date createdDt = null;
    private Date statusDt = null;
    private String nomineeReq = "";
    private String branchCode = "";
    private String customerName = "";

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String isNomineeReq() {
        return nomineeReq;
    }

    public void setNomineeReq(String nomineeReq) {
        this.nomineeReq = nomineeReq;
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

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Double getNominalMemFee() {
        return nominalMemFee;
    }

    public void setNominalMemFee(Double nominalMemFee) {
        this.nominalMemFee = nominalMemFee;
    }

    public String getNominalMemNo() {
        return nominalMemNo;
    }

    public void setNominalMemNo(String nominalMemNo) {
        this.nominalMemNo = nominalMemNo;
    }

    public Date getOpeningDt() {
        return openingDt;
    }

    public void setOpeningDt(Date openingDt) {
        this.openingDt = openingDt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("nominalMemNo", nominalMemNo));
        strB.append(getTOString("openingDt", openingDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("nominalMemFee", nominalMemFee));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("nomineeReq", nomineeReq));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("customerName", customerName));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("nominalMemNo", nominalMemNo));
        strB.append(getTOXml("openingDt", openingDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("nominalMemFee", nominalMemFee));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("nomineeReq", nomineeReq));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("customerName", customerName));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}