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
package com.see.truetransact.transferobject.indend.store;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class StoreTO extends TransferObject implements Serializable {

    private String name = "", genTrans = "", stNo = "";
    private String servTax = "", vat = "";
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
        return stNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("stNo", stNo));
        strB.append(getTOString("name", name));
        strB.append(getTOString("genTrans", genTrans));
        strB.append(getTOString("servTax", servTax));
        strB.append(getTOString("vat", vat));
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
        strB.append(getTOXml("stNo ", stNo));
        strB.append(getTOXml("name", name));
        strB.append(getTOXml("genTrans", genTrans));
        strB.append(getTOXml("servTax", servTax));
        strB.append(getTOXml("vat", vat));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDte", authorizeDte));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property stNo.
     *
     * @return Value of property stNo.
     */
    public java.lang.String getStNo() {
        return stNo;
    }

    /**
     * Setter for property stNo.
     *
     * @param stNo New value of property stNo.
     */
    public void setStNo(java.lang.String stNo) {
        this.stNo = stNo;
    }

    /**
     * Getter for property genTrans.
     *
     * @return Value of property genTrans.
     */
    public java.lang.String getGenTrans() {
        return genTrans;
    }

    /**
     * Setter for property genTrans.
     *
     * @param genTrans New value of property genTrans.
     */
    public void setGenTrans(java.lang.String genTrans) {
        this.genTrans = genTrans;
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
     * Getter for property servTax.
     *
     * @return Value of property servTax.
     */
    public String getServTax() {
        return servTax;
    }

    /**
     * Setter for property servTax.
     *
     * @param servTax New value of property servTax.
     */
    public void setServTax(String servTax) {
        this.servTax = servTax;
    }

    /**
     * Getter for property vat.
     *
     * @return Value of property vat.
     */
    public String getVat() {
        return vat;
    }

    /**
     * Setter for property vat.
     *
     * @param vat New value of property vat.
     */
    public void setVat(String vat) {
        this.vat = vat;
    }
    /**
     * Getter for property depoId.
     *
     * @return Value of property depoId.
     */
}