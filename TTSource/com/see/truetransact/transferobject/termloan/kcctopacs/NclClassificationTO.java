/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NclClassificationTO.java
 * 
 * Created on Thu Mar 07 18:21:22 IST 2013
 */
package com.see.truetransact.transferobject.termloan.kcctopacs;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;

/**
 * Table name for this TO is NCL_CLASSIFICATION.
 */
public class NclClassificationTO extends TransferObject implements Serializable {

    private String nclSanctionNo = "";
    private Double totMembers = null;
//    private Double amt = null;
    private String category = "";
    private String subcategory = "";
    private Double countMembers = null;
    private String slNo = "";
    private String amt = null;

    /**
     * Setter/Getter for NCL_SANCTION_NO - table Field
     */
    public void setNclSanctionNo(String nclSanctionNo) {
        this.nclSanctionNo = nclSanctionNo;
    }

    public String getNclSanctionNo() {
        return nclSanctionNo;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public Double getTotMembers() {
        return totMembers;
    }

    public void setTotMembers(Double totMembers) {
        this.totMembers = totMembers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public Double getCountMembers() {
        return countMembers;
    }

    public void setCountMembers(Double countMembers) {
        this.countMembers = countMembers;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
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
        StringBuilder strB = new StringBuilder(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("nclSanctionNo", nclSanctionNo));
        strB.append(getTOString("totMembers", totMembers));
        strB.append(getTOString("amt", amt));
        strB.append(getTOString("category", category));
        strB.append(getTOString("subcategory", subcategory));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuilder strB = new StringBuilder(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("nclSanctionNo", nclSanctionNo));
        strB.append(getTOXml("totMembers", totMembers));
        strB.append(getTOXml("amt", amt));
        strB.append(getTOXml("category", category));
        strB.append(getTOXml("subcategory", subcategory));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}