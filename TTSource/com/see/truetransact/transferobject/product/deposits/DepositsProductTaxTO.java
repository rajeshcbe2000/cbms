/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositsProductTaxTO.java
 *
 * Created on Tue Jul 06 17:41:49 IST 2004
 */
package com.see.truetransact.transferobject.product.deposits;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSITS_PROD_TAX.
 */
public class DepositsProductTaxTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String tdsGlAchd = "";
    private String recalcMaturityValtds = "";

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
     * Setter/Getter for TDS_GL_ACHD - table Field
     */
    public void setTdsGlAchd(String tdsGlAchd) {
        this.tdsGlAchd = tdsGlAchd;
    }

    public String getTdsGlAchd() {
        return tdsGlAchd;
    }

    /**
     * Setter/Getter for RECALC_MATURITY_VALTDS - table Field
     */
    public void setRecalcMaturityValtds(String recalcMaturityValtds) {
        this.recalcMaturityValtds = recalcMaturityValtds;
    }

    public String getRecalcMaturityValtds() {
        return recalcMaturityValtds;
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
        strB.append(getTOString("tdsGlAchd", tdsGlAchd));
        strB.append(getTOString("recalcMaturityValtds", recalcMaturityValtds));
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
        strB.append(getTOXml("tdsGlAchd", tdsGlAchd));
        strB.append(getTOXml("recalcMaturityValtds", recalcMaturityValtds));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}