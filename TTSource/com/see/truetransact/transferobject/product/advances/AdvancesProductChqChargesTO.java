/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AdvancesProductChqChargesTO.java
 * 
 * Created on Mon Apr 11 18:03:16 IST 2005
 */
package com.see.truetransact.transferobject.product.advances;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ADVANCES_PROD_CHQCHRG.
 */
public class AdvancesProductChqChargesTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String chqRetChrgtype = "";
    private String chqRetChrgamt = "";
    private Double chqRetChrgrate = null;

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
     * Setter/Getter for CHQ_RET_CHRGTYPE - table Field
     */
    public void setChqRetChrgtype(String chqRetChrgtype) {
        this.chqRetChrgtype = chqRetChrgtype;
    }

    public String getChqRetChrgtype() {
        return chqRetChrgtype;
    }

    /**
     * Setter/Getter for CHQ_RET_CHRGAMT - table Field
     */
    public void setChqRetChrgamt(String chqRetChrgamt) {
        this.chqRetChrgamt = chqRetChrgamt;
    }

    public String getChqRetChrgamt() {
        return chqRetChrgamt;
    }

    /**
     * Setter/Getter for CHQ_RET_CHRGRATE - table Field
     */
    public void setChqRetChrgrate(Double chqRetChrgrate) {
        this.chqRetChrgrate = chqRetChrgrate;
    }

    public Double getChqRetChrgrate() {
        return chqRetChrgrate;
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
        strB.append(getTOString("chqRetChrgtype", chqRetChrgtype));
        strB.append(getTOString("chqRetChrgamt", chqRetChrgamt));
        strB.append(getTOString("chqRetChrgrate", chqRetChrgrate));
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
        strB.append(getTOXml("chqRetChrgtype", chqRetChrgtype));
        strB.append(getTOXml("chqRetChrgamt", chqRetChrgamt));
        strB.append(getTOXml("chqRetChrgrate", chqRetChrgrate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}