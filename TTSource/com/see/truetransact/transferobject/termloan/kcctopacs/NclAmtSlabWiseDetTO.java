/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NclAmtSlabWiseDetTO.java
 * 
 * Created on Thu Mar 07 18:23:45 IST 2013
 */
package com.see.truetransact.transferobject.termloan.kcctopacs;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;

/**
 * Table name for this TO is NCL_AMT_SLABWISE_DET.
 */
public class NclAmtSlabWiseDetTO extends TransferObject implements Serializable {

    private String nclSanctionNo = "";
//    private Double fromAmt = null;
//    private Double toAmt = null;
    private Double slabNoOfMembers = null;
    private String fromAmt = null;
    private String toAmt = null;

    /**
     * Setter/Getter for NCL_SANCTION_NO - table Field
     */
    public void setNclSanctionNo(String nclSanctionNo) {
        this.nclSanctionNo = nclSanctionNo;
    }

    public String getNclSanctionNo() {
        return nclSanctionNo;
    }

    public String getFromAmt() {
        return fromAmt;
    }

    public void setFromAmt(String fromAmt) {
        this.fromAmt = fromAmt;
    }

    public String getToAmt() {
        return toAmt;
    }

    public void setToAmt(String toAmt) {
        this.toAmt = toAmt;
    }
//    /**
//     * Setter/Getter for FROM_AMT - table Field
//     */
//    public void setFromAmt(Double fromAmt) {
//        this.fromAmt = fromAmt;
//    }
//
//    public Double getFromAmt() {
//        return fromAmt;
//    }
//
//    /**
//     * Setter/Getter for TO_AMT - table Field
//     */
//    public void setToAmt(Double toAmt) {
//        this.toAmt = toAmt;
//    }
//
//    public Double getToAmt() {
//        return toAmt;
//    }

    public Double getSlabNoOfMembers() {
        return slabNoOfMembers;
    }

    public void setSlabNoOfMembers(Double slabNoOfMembers) {
        this.slabNoOfMembers = slabNoOfMembers;
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
        strB.append(getTOString("fromAmt", fromAmt));
        strB.append(getTOString("toAmt", toAmt));
        strB.append(getTOString("slabNoOfMembers", slabNoOfMembers));
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
        strB.append(getTOXml("fromAmt", fromAmt));
        strB.append(getTOXml("toAmt", toAmt));
        strB.append(getTOXml("slabNoOfMembers", slabNoOfMembers));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}