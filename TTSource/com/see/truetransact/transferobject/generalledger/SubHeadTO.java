/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SubHeadTO.java
 * 
 * Created on Mon May 10 11:23:54 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.generalledger;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SUB_AC_HD.
 */
public class SubHeadTO extends TransferObject implements Serializable {

    private String mjrAcHdId = "";
    private String subAcHdId = "";
    private String subAcHdDesc = "";
    private String status = "";
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String finalActType = "";
    private String subActType = "";

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
     * Setter/Getter for SUB_AC_HD_DESC - table Field
     */
    public void setSubAcHdDesc(String subAcHdDesc) {
        this.subAcHdDesc = subAcHdDesc;
    }

    public String getSubAcHdDesc() {
        return subAcHdDesc;
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

    public String getFinalActType() {
        return finalActType;
    }

    public void setFinalActType(String finalActType) {
        this.finalActType = finalActType;
    }

    public String getSubActType() {
        return subActType;
    }

    public void setSubActType(String subActType) {
        this.subActType = subActType;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(mjrAcHdId);
        return mjrAcHdId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("mjrAcHdId", mjrAcHdId));
        strB.append(getTOString("subAcHdId", subAcHdId));
        strB.append(getTOString("subAcHdDesc", subAcHdDesc));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("finalActType", finalActType));
        strB.append(getTOString("subActType", subActType));
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
        strB.append(getTOXml("subAcHdDesc", subAcHdDesc));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("finalActType", finalActType));
        strB.append(getTOXml("subActType", subActType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}