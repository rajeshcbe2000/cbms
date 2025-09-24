/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BlockedListTo.java
 * 
 * Created on Wed Feb 09 15:29:39 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.blockedlist;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BLOCK_LIST.
 */
public class BlockedListTO extends TransferObject implements Serializable {

    private String blockId = "";
    private String blockedName = "";
    private String businessAddr = "";
    private String customerType = "";
    private String fraudStatus = "";
    private String fraudClassification = "";
    private String remarks = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;

    /**
     * Setter/Getter for BLOCK_ID - table Field
     */
    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getBlockId() {
        return blockId;
    }

    /**
     * Setter/Getter for BLOCKED_NAME - table Field
     */
    public void setBlockedName(String blockedName) {
        this.blockedName = blockedName;
    }

    public String getBlockedName() {
        return blockedName;
    }

    /**
     * Setter/Getter for BUSINESS_ADDR - table Field
     */
    public void setBusinessAddr(String businessAddr) {
        this.businessAddr = businessAddr;
    }

    public String getBusinessAddr() {
        return businessAddr;
    }

    /**
     * Setter/Getter for CUSTOMER_TYPE - table Field
     */
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerType() {
        return customerType;
    }

    /**
     * Setter/Getter for FRAUD_STATUS - table Field
     */
    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    /**
     * Setter/Getter for FRAUD_CLASSIFICATION - table Field
     */
    public void setFraudClassification(String fraudClassification) {
        this.fraudClassification = fraudClassification;
    }

    public String getFraudClassification() {
        return fraudClassification;
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
        setKeyColumns(blockId);
        return blockId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("blockId", blockId));
        strB.append(getTOString("blockedName", blockedName));
        strB.append(getTOString("businessAddr", businessAddr));
        strB.append(getTOString("customerType", customerType));
        strB.append(getTOString("fraudStatus", fraudStatus));
        strB.append(getTOString("fraudClassification", fraudClassification));
        strB.append(getTOString("remarks", remarks));
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
        strB.append(getTOXml("blockId", blockId));
        strB.append(getTOXml("blockedName", blockedName));
        strB.append(getTOXml("businessAddr", businessAddr));
        strB.append(getTOXml("customerType", customerType));
        strB.append(getTOXml("fraudStatus", fraudStatus));
        strB.append(getTOXml("fraudClassification", fraudClassification));
        strB.append(getTOXml("remarks", remarks));
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