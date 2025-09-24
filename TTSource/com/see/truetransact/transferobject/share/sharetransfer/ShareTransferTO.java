/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareTransferTO.java
 * 
 * Created on Thu Feb 03 16:15:13 IST 2005
 */
package com.see.truetransact.transferobject.share.sharetransfer;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_TRANSFER.
 */
public class ShareTransferTO extends TransferObject implements Serializable {

    private String shareTransId = "";
    private String transferFrom = "";
    private String transferTo = "";
    private String shareNoFrom = "";
    private String shareNoTo = "";
    private String remarks = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;

    /**
     * Setter/Getter for SHARE_TRANS_ID - table Field
     */
    public void setShareTransId(String shareTransId) {
        this.shareTransId = shareTransId;
    }

    public String getShareTransId() {
        return shareTransId;
    }

    /**
     * Setter/Getter for TRANSFER_FROM - table Field
     */
    public void setTransferFrom(String transferFrom) {
        this.transferFrom = transferFrom;
    }

    public String getTransferFrom() {
        return transferFrom;
    }

    /**
     * Setter/Getter for TRANSFER_TO - table Field
     */
    public void setTransferTo(String transferTo) {
        this.transferTo = transferTo;
    }

    public String getTransferTo() {
        return transferTo;
    }

    /**
     * Setter/Getter for SHARE_NO_FROM - table Field
     */
    public void setShareNoFrom(String shareNoFrom) {
        this.shareNoFrom = shareNoFrom;
    }

    public String getShareNoFrom() {
        return shareNoFrom;
    }

    /**
     * Setter/Getter for SHARE_NO_TO - table Field
     */
    public void setShareNoTo(String shareNoTo) {
        this.shareNoTo = shareNoTo;
    }

    public String getShareNoTo() {
        return shareNoTo;
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
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
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
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("shareTransId");
        return shareTransId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("shareTransId", shareTransId));
        strB.append(getTOString("transferFrom", transferFrom));
        strB.append(getTOString("transferTo", transferTo));
        strB.append(getTOString("shareNoFrom", shareNoFrom));
        strB.append(getTOString("shareNoTo", shareNoTo));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("shareTransId", shareTransId));
        strB.append(getTOXml("transferFrom", transferFrom));
        strB.append(getTOXml("transferTo", transferTo));
        strB.append(getTOXml("shareNoFrom", shareNoFrom));
        strB.append(getTOXml("shareNoTo", shareNoTo));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}