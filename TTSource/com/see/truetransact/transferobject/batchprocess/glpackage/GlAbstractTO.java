/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GlAbstract.java
 * 
 * Created on Fri Mar 04 14:34:23 IST 2005
 */
package com.see.truetransact.transferobject.batchprocess.glpackage;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is GL_ABSTRACT.
 */
public class GlAbstractTO extends TransferObject implements Serializable {

    private String acHdId = "";
    private Double opnBal = null;
    private Double closeBal = null;
    private String branchCode = "";
    private Date dt = null;
    private String balanceType = "";

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
     * Setter/Getter for OPN_BAL - table Field
     */
    public void setOpnBal(Double opnBal) {
        this.opnBal = opnBal;
    }

    public Double getOpnBal() {
        return opnBal;
    }

    /**
     * Setter/Getter for CLOSE_BAL - table Field
     */
    public void setCloseBal(Double closeBal) {
        this.closeBal = closeBal;
    }

    public Double getCloseBal() {
        return closeBal;
    }

    /**
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter/Getter for DT - table Field
     */
    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Date getDt() {
        return dt;
    }

    /**
     * Setter/Getter for Balance_Type - table Field
     */
    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getBalanceType() {
        return balanceType;
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
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("opnBal", opnBal));
        strB.append(getTOString("closeBal", closeBal));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("dt", dt));
        strB.append(getTOString("balanceType", balanceType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("opnBal", opnBal));
        strB.append(getTOXml("closeBal", closeBal));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("dt", dt));
        strB.append(getTOXml("balanceType", balanceType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}