/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NclSubLimitTO.java
 * 
 * Created on Thu Mar 07 18:17:45 IST 2013
 */
package com.see.truetransact.transferobject.termloan.kcctopacs;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;

/**
 * Table name for this TO is NCL_SUB_LIMIT.
 */
public class NclSubLimitTO extends TransferObject implements Serializable {

    private String nclSanctionNo = "";
    private Double startFinYear = null;
    private Double endFinYear = null;
//    private Double limitAmt = null;
    private String limitAmt = null;

    public String getLimitAmt() {
        return limitAmt;
    }

    public void setLimitAmt(String limitAmt) {
        this.limitAmt = limitAmt;
    }

    /**
     * Setter/Getter for NCL_SANCTION_NO - table Field
     */
    public void setNclSanctionNo(String nclSanctionNo) {
        this.nclSanctionNo = nclSanctionNo;
    }

    public String getNclSanctionNo() {
        return nclSanctionNo;
    }

    /**
     * Setter/Getter for START_FIN_YEAR - table Field
     */
    public void setStartFinYear(Double startFinYear) {
        this.startFinYear = startFinYear;
    }

    public Double getStartFinYear() {
        return startFinYear;
    }

    /**
     * Setter/Getter for END_FIN_YEAR - table Field
     */
    public void setEndFinYear(Double endFinYear) {
        this.endFinYear = endFinYear;
    }

    public Double getEndFinYear() {
        return endFinYear;
    }

//    /**
//     * Setter/Getter for LIMIT_AMT - table Field
//     */
//    public void setLimitAmt(Double limitAmt) {
//        this.limitAmt = limitAmt;
//    }
//
//    public Double getLimitAmt() {
//        return limitAmt;
//    }
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
        strB.append(getTOString("startFinYear", startFinYear));
        strB.append(getTOString("endFinYear", endFinYear));
        strB.append(getTOString("limitAmt", limitAmt));
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
        strB.append(getTOXml("startFinYear", startFinYear));
        strB.append(getTOXml("endFinYear", endFinYear));
        strB.append(getTOXml("limitAmt", limitAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}