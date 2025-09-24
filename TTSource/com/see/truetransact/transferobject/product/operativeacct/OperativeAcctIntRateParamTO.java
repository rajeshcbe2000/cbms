/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctIntRateParamTO.java
 * 
 * Created on Thu Jul 22 10:40:17 IST 2004
 */
package com.see.truetransact.transferobject.product.operativeacct;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OP_AC_INTRATEMAINT_PARAM.
 */
public class OperativeAcctIntRateParamTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String intCatId = "";
    private Date intDate = null;
    private Double intRate = null;
    private String acHdId = "";

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for INT_CAT_ID - table Field
     */
    public void setIntCatId(String intCatId) {
        this.intCatId = intCatId;
    }

    public String getIntCatId() {
        return intCatId;
    }

    /**
     * Setter/Getter for INT_DATE - table Field
     */
    public void setIntDate(Date intDate) {
        this.intDate = intDate;
    }

    public Date getIntDate() {
        return intDate;
    }

    /**
     * Setter/Getter for INT_RATE - table Field
     */
    public void setIntRate(Double intRate) {
        this.intRate = intRate;
    }

    public Double getIntRate() {
        return intRate;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId");
        return prodId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("intCatId", intCatId));
        strB.append(getTOString("intDate", intDate));
        strB.append(getTOString("intRate", intRate));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("intCatId", intCatId));
        strB.append(getTOXml("intDate", intDate));
        strB.append(getTOXml("intRate", intRate));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}