/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChargesBatchTO.java
 * 
 * Created on Thu Aug 19 12:03:48 IST 2004
 */
package com.see.truetransact.transferobject.batchprocess.charges;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ACT_CHARGES.
 */
public class ChargesBatchTO extends TransferObject implements Serializable {

    private String actNum = "";
    private Date chrgDt = null;
    private String chrgType = "";
    private Double chrgAmt = null;
    private String acHdId = "";

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for CHRG_DT - table Field
     */
    public void setChrgDt(Date chrgDt) {
        this.chrgDt = chrgDt;
    }

    public Date getChrgDt() {
        return chrgDt;
    }

    /**
     * Setter/Getter for CHRG_TYPE - table Field
     */
    public void setChrgType(String chrgType) {
        this.chrgType = chrgType;
    }

    public String getChrgType() {
        return chrgType;
    }

    /**
     * Setter/Getter for CHRG_AMT - table Field
     */
    public void setChrgAmt(Double chrgAmt) {
        this.chrgAmt = chrgAmt;
    }

    public Double getChrgAmt() {
        return chrgAmt;
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
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("chrgDt", chrgDt));
        strB.append(getTOString("chrgType", chrgType));
        strB.append(getTOString("chrgAmt", chrgAmt));
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
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("chrgDt", chrgDt));
        strB.append(getTOXml("chrgType", chrgType));
        strB.append(getTOXml("chrgAmt", chrgAmt));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}