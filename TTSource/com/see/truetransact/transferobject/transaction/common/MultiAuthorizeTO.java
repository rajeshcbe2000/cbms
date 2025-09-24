/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MultiAuthorize.java
 * 
 * Created on Thu Sep 02 15:09:14 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.transaction.common;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 * Table name for this TO is AUTHORIZE_EXCEPTION.
 */
public class MultiAuthorizeTO extends TransferObject implements Serializable {

    private String transId = "";
    private Double transType = null;
    private String authUserId = "";
    private String status = "";
    private Date transDt = null;
    private String initiatedBranch = "";

    /**
     * Setter/Getter for TRANS_ID - table Field
     */
    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransId() {
        return transId;
    }

    /**
     * Setter/Getter for TRANS_TYPE - table Field
     */
    public void setTransType(Double transType) {
        this.transType = transType;
    }

    public Double getTransType() {
        return transType;
    }

    /**
     * Setter/Getter for AUTH_USER_ID - table Field
     */
    public void setAuthUserId(String authUserId) {
        this.authUserId = authUserId;
    }

    public String getAuthUserId() {
        return authUserId;
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
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("authUserId", authUserId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("authUserId", authUserId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property transDt.
     *
     * @return Value of property transDt.
     */
    public java.util.Date getTransDt() {
        return transDt;
    }

    /**
     * Setter for property transDt.
     *
     * @param transDt New value of property transDt.
     */
    public void setTransDt(java.util.Date transDt) {
        this.transDt = transDt;
    }

    /**
     * Getter for property initiatedBranch.
     *
     * @return Value of property initiatedBranch.
     */
    public java.lang.String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * Setter for property initiatedBranch.
     *
     * @param initiatedBranch New value of property initiatedBranch.
     */
    public void setInitiatedBranch(java.lang.String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }
}
