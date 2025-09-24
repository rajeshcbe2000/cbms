/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountCreationTO.java
 * 
 * Created on Mon Mar 21 14:52:10 IST 2005
 */
package com.see.truetransact.transferobject.generalledger;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is AC_HD.
 */
public class AccountCreationTO extends TransferObject implements Serializable {

    private String mjrAcHdId = "";
    private String subAcHdId = "";
    private String acHdCode = "";
    private String acHdId = "";
    private String acHdDesc = "";
    private String status = "";
    private Date createdDt = null;
    private Date deletedDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String recDayBook = "";
    private String payDayBook = "";
    private Double acHeadOrder = 0.0;

    /**
     * Setter/Getter for MJR_AC_HD_ID - table Field
     */
    public void setMjrAcHdId(String mjrAcHdId) {
        this.mjrAcHdId = mjrAcHdId;
    }

    public String getMjrAcHdId() {
        return mjrAcHdId;
    }

    /**
     * Setter/Getter for SUB_AC_HD_ID - table Field
     */
    public void setSubAcHdId(String subAcHdId) {
        this.subAcHdId = subAcHdId;
    }

    public String getSubAcHdId() {
        return subAcHdId;
    }

    /**
     * Setter/Getter for AC_HD_CODE - table Field
     */
    public void setAcHdCode(String acHdCode) {
        this.acHdCode = acHdCode;
    }

    public String getAcHdCode() {
        return acHdCode;
    }

    /**
     * Setter/Getter for AC_HD_ID - table Field
     */
    public void setAcHdId(String acHdId) {
        this.acHdId = acHdId;
    }

    public String getAcHdId() {
        return acHdId;
    }

    /**
     * Setter/Getter for AC_HD_DESC - table Field
     */
    public void setAcHdDesc(String acHdDesc) {
        this.acHdDesc = acHdDesc;
    }

    public String getAcHdDesc() {
        return acHdDesc;
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
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter/Getter for DELETED_DT - table Field
     */
    public void setDeletedDt(Date deletedDt) {
        this.deletedDt = deletedDt;
    }

    public Date getDeletedDt() {
        return deletedDt;
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
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
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
        setKeyColumns("acHdId");
        return acHdId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("mjrAcHdId", mjrAcHdId));
        strB.append(getTOString("subAcHdId", subAcHdId));
        strB.append(getTOString("acHdCode", acHdCode));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("acHdDesc", acHdDesc));
        strB.append(getTOString("status", status));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("deletedDt", deletedDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
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
        strB.append(getTOXml("mjrAcHdId", mjrAcHdId));
        strB.append(getTOXml("subAcHdId", subAcHdId));
        strB.append(getTOXml("acHdCode", acHdCode));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("acHdDesc", acHdDesc));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("deletedDt", deletedDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property recDayBook.
     *
     * @return Value of property recDayBook.
     */
    public java.lang.String getRecDayBook() {
        return recDayBook;
    }

    /**
     * Setter for property recDayBook.
     *
     * @param recDayBook New value of property recDayBook.
     */
    public void setRecDayBook(java.lang.String recDayBook) {
        this.recDayBook = recDayBook;
    }

    /**
     * Getter for property payDayBook.
     *
     * @return Value of property payDayBook.
     */
    public java.lang.String getPayDayBook() {
        return payDayBook;
    }

    /**
     * Setter for property payDayBook.
     *
     * @param payDayBook New value of property payDayBook.
     */
    public void setPayDayBook(java.lang.String payDayBook) {
        this.payDayBook = payDayBook;
    }

    public Double getAcHeadOrder() {
        return acHeadOrder;
    }

    public void setAcHeadOrder(Double acHeadOrder) {
        this.acHeadOrder = acHeadOrder;
    }

    
}