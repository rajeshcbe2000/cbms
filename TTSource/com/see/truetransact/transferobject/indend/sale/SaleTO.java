/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigTO.java
 * 
 * Created on Thu Jan 20 16:44:08 IST 2005
 */
package com.see.truetransact.transferobject.indend.sale;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class SaleTO extends TransferObject implements Serializable {

    private String name = "", address = "", smId = "",
            remarks = "";
    Double sec_amt;
    private String authorizeStatus = null;
    private String authorizeBy = null;
    private Date authorizeDte = null;
    private String status = null;

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        //setKeyColumns(borrowingNo);
        return smId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("smId", smId));
        strB.append(getTOString("name", name));
        strB.append(getTOString("address", address));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("sec_amt", sec_amt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOString("status", status));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("smId ", smId));
        strB.append(getTOXml("name", name));
        strB.append(getTOXml("address", address));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("sec_amt", sec_amt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDte", authorizeDte));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Getter for property address.
     *
     * @return Value of property address.
     */
    public java.lang.String getAddress() {
        return address;
    }

    /**
     * Setter for property address.
     *
     * @param address New value of property address.
     */
    public void setAddress(java.lang.String address) {
        this.address = address;
    }

    /**
     * Getter for property smId.
     *
     * @return Value of property smId.
     */
    public java.lang.String getSmId() {
        return smId;
    }

    /**
     * Setter for property smId.
     *
     * @param smId New value of property smId.
     */
    public void setSmId(java.lang.String smId) {
        this.smId = smId;
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
     * Getter for property sec_amt.
     *
     * @return Value of property sec_amt.
     */
    public java.lang.Double getSec_amt() {
        return sec_amt;
    }

    /**
     * Setter for property sec_amt.
     *
     * @param sec_amt New value of property sec_amt.
     */
    public void setSec_amt(java.lang.Double sec_amt) {
        this.sec_amt = sec_amt;
    }

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDte.
     *
     * @return Value of property authorizeDte.
     */
    public java.util.Date getAuthorizeDte() {
        return authorizeDte;
    }

    /**
     * Setter for property authorizeDte.
     *
     * @param authorizeDte New value of property authorizeDte.
     */
    public void setAuthorizeDte(java.util.Date authorizeDte) {
        this.authorizeDte = authorizeDte;
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
     * Getter for property depoId.
     *
     * @return Value of property depoId.
     */
}