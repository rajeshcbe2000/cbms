/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * StandInstrCreditTO.java
 *
 * Created on Wed Jul 21 18:32:11 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_STANDING_INSTRU_CR.
 */
public class StandInstrCreditTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private String depositSubNo = "";
    private String crProdId = "";
    private String crAchd = "";
    private String crAcNo = "";
    private String crDepositNo = "";
    private Double crAmount = null;
    private String crParticulars = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

    /**
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
    }

    /**
     * Setter/Getter for DEPOSIT_SUB_NO - table Field
     */
    public void setDepositSubNo(String depositSubNo) {
        this.depositSubNo = depositSubNo;
    }

    public String getDepositSubNo() {
        return depositSubNo;
    }

    /**
     * Setter/Getter for CR_PROD_ID - table Field
     */
    public void setCrProdId(String crProdId) {
        this.crProdId = crProdId;
    }

    public String getCrProdId() {
        return crProdId;
    }

    /**
     * Setter/Getter for CR_ACHD - table Field
     */
    public void setCrAchd(String crAchd) {
        this.crAchd = crAchd;
    }

    public String getCrAchd() {
        return crAchd;
    }

    /**
     * Setter/Getter for CR_AC_NO - table Field
     */
    public void setCrAcNo(String crAcNo) {
        this.crAcNo = crAcNo;
    }

    public String getCrAcNo() {
        return crAcNo;
    }

    /**
     * Setter/Getter for CR_DEPOSIT_NO - table Field
     */
    public void setCrDepositNo(String crDepositNo) {
        this.crDepositNo = crDepositNo;
    }

    public String getCrDepositNo() {
        return crDepositNo;
    }

    /**
     * Setter/Getter for CR_AMOUNT - table Field
     */
    public void setCrAmount(Double crAmount) {
        this.crAmount = crAmount;
    }

    public Double getCrAmount() {
        return crAmount;
    }

    /**
     * Setter/Getter for CR_PARTICULARS - table Field
     */
    public void setCrParticulars(String crParticulars) {
        this.crParticulars = crParticulars;
    }

    public String getCrParticulars() {
        return crParticulars;
    }

    /**
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("depositNo");
        return depositNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("depositSubNo", depositSubNo));
        strB.append(getTOString("crProdId", crProdId));
        strB.append(getTOString("crAchd", crAchd));
        strB.append(getTOString("crAcNo", crAcNo));
        strB.append(getTOString("crDepositNo", crDepositNo));
        strB.append(getTOString("crAmount", crAmount));
        strB.append(getTOString("crParticulars", crParticulars));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("depositSubNo", depositSubNo));
        strB.append(getTOXml("crProdId", crProdId));
        strB.append(getTOXml("crAchd", crAchd));
        strB.append(getTOXml("crAcNo", crAcNo));
        strB.append(getTOXml("crDepositNo", crDepositNo));
        strB.append(getTOXml("crAmount", crAmount));
        strB.append(getTOXml("crParticulars", crParticulars));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}