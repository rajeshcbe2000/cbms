/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerPhoneTO.java
 * 
 * Created on Wed Feb 16 09:38:12 IST 2005
 */
package com.see.truetransact.transferobject.customer.customeridchange;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUST_PHONE.
 */
public class CustomerIdChangeTO extends TransferObject implements Serializable {

    private String prodType = "";
    private String prodId = "";
    private String actNum = "";
    private String actName = "";
    private String oldCustId = "";
    private String newCustId = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String createdBy = "";
    private Date createdDate = null;
    private String authBy = "";
    private Date authDate = null;
    private String branchCode = "";
    private String status = "";
    private String authstatus = "";
    private String newActName = "";
	private String multipleBatchId = "";
    private String actStatus = "";

    public String getActStatus() {
        return actStatus;
    }

    public void setActStatus(String actStatus) {
        this.actStatus = actStatus;
    }
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("oldCustId", oldCustId));
        strB.append(getTOString("newCustId", newCustId));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDate", createdDate));
        strB.append(getTOString("authDate", authDate));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authstatus", authstatus));
        strB.append(getTOString("newActName", newActName));
		strB.append(getTOString("multipleBatchId", multipleBatchId));
        strB.append(getTOString("actStatus", actStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("oldCustId", oldCustId));
        strB.append(getTOXml("newCustId", newCustId));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDate", createdDate));
        strB.append(getTOXml("authDate", authDate));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authstatus", authstatus));
        strB.append(getTOXml("newActName", newActName));
		strB.append(getTOXml("multipleBatchId", multipleBatchId));
        strB.append(getTOXml("actStatus", actStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }

    /**
     * Getter for property prodId.
     *
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     *
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }

    /**
     * Getter for property actNum.
     *
     * @return Value of property actNum.
     */
    public java.lang.String getActNum() {
        return actNum;
    }

    /**
     * Setter for property actNum.
     *
     * @param actNum New value of property actNum.
     */
    public void setActNum(java.lang.String actNum) {
        this.actNum = actNum;
    }

    /**
     * Getter for property actName.
     *
     * @return Value of property actName.
     */
    public java.lang.String getActName() {
        return actName;
    }

    /**
     * Setter for property actName.
     *
     * @param actName New value of property actName.
     */
    public void setActName(java.lang.String actName) {
        this.actName = actName;
    }

    /**
     * Getter for property oldCustId.
     *
     * @return Value of property oldCustId.
     */
    public java.lang.String getOldCustId() {
        return oldCustId;
    }

    /**
     * Setter for property oldCustId.
     *
     * @param oldCustId New value of property oldCustId.
     */
    public void setOldCustId(java.lang.String oldCustId) {
        this.oldCustId = oldCustId;
    }

    /**
     * Getter for property newCustId.
     *
     * @return Value of property newCustId.
     */
    public java.lang.String getNewCustId() {
        return newCustId;
    }

    /**
     * Setter for property newCustId.
     *
     * @param newCustId New value of property newCustId.
     */
    public void setNewCustId(java.lang.String newCustId) {
        this.newCustId = newCustId;
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
     * Getter for property branchCode.
     *
     * @return Value of property branchCode.
     */
    public java.lang.String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter for property branchCode.
     *
     * @param branchCode New value of property branchCode.
     */
    public void setBranchCode(java.lang.String branchCode) {
        this.branchCode = branchCode;
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
     * Getter for property authstatus.
     *
     * @return Value of property authstatus.
     */
    public java.lang.String getAuthstatus() {
        return authstatus;
    }

    /**
     * Setter for property authstatus.
     *
     * @param authstatus New value of property authstatus.
     */
    public void setAuthstatus(java.lang.String authstatus) {
        this.authstatus = authstatus;
    }

    /**
     * Getter for property createdDate.
     *
     * @return Value of property createdDate.
     */
    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Setter for property createdDate.
     *
     * @param createdDate New value of property createdDate.
     */
    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Getter for property newActName.
     *
     * @return Value of property newActName.
     */
    public java.lang.String getNewActName() {
        return newActName;
    }

    /**
     * Setter for property newActName.
     *
     * @param newActName New value of property newActName.
     */
    public void setNewActName(java.lang.String newActName) {
        this.newActName = newActName;
    }

	public String getMultipleBatchId() {
        return multipleBatchId;
    }

    public void setMultipleBatchId(String multipleBatchId) {
        this.multipleBatchId = multipleBatchId;
    }
}