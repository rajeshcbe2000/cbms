/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareJointTO.java
 * 
 * Created on Wed Dec 29 10:54:12 IST 2004
 */
package com.see.truetransact.transferobject.share;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 * Table name for this TO is SHARE_JOINT.
 */
public class PensionSchemeTO extends TransferObject implements Serializable {

    private String shareAcctNo = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String status = "";
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizedStatus = null;
    private String transType = "";
    private Double transAmount = 0.0;
    private Double shareRunPeriod = 0.0;
    private Double custAge = 0.0;
    private String transId = "";
    private Date transDt = null;
    private String branchCode = "";

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public Double getCustAge() {
        return custAge;
    }

    public void setCustAge(Double custAge) {
        this.custAge = custAge;
    }

    public String getShareAcctNo() {
        return shareAcctNo;
    }

    public void setShareAcctNo(String shareAcctNo) {
        this.shareAcctNo = shareAcctNo;
    }

    public Double getShareRunPeriod() {
        return shareRunPeriod;
    }

    public void setShareRunPeriod(Double shareRunPeriod) {
        this.shareRunPeriod = shareRunPeriod;
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

    public Double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Double transAmount) {
        this.transAmount = transAmount;
    }

    public Date getTransDt() {
        return transDt;
    }

    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
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
        strB.append(getTOString("shareAcctNo", shareAcctNo));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("transAmount", transAmount));
        strB.append(getTOString("shareRunPeriod", shareRunPeriod));
        strB.append(getTOString("custAge", custAge));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("custAge", custAge));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("shareAcctNo", shareAcctNo));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("transAmount", transAmount));
        strB.append(getTOXml("shareRunPeriod", shareRunPeriod));
        strB.append(getTOXml("custAge", custAge));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("custAge", custAge));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}