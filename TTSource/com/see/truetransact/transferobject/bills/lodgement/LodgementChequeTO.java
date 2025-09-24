/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementChequeTO.java
 * 
 * Created on Mon Feb 07 12:35:36 IST 2005
 */
package com.see.truetransact.transferobject.bills.lodgement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BILLS_LODGEMENT_CHEQUE.
 */
public class LodgementChequeTO extends TransferObject implements Serializable {

    private String lodgementId = "";
    private String draweeBankName = "";
    private String draweeBranchName = "";
    private String instrumentNo = "";
    private String instrPrefix = "";

    public String getInstrPrefix() {
        return instrPrefix;
    }

    public void setInstrPrefix(String instrPrefix) {
        this.instrPrefix = instrPrefix;
    }
    private Date instrumentDt = null;
    private Double instrumentAmount = null;
    private String micr = "";
    private String payeeName = "";
    private String remarks = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

    /**
     * Setter/Getter for LODGEMENT_ID - table Field
     */
    public void setLodgementId(String lodgementId) {
        this.lodgementId = lodgementId;
    }

    public String getLodgementId() {
        return lodgementId;
    }

    /**
     * Setter/Getter for DRAWEE_BANK_NAME - table Field
     */
    public void setDraweeBankName(String draweeBankName) {
        this.draweeBankName = draweeBankName;
    }

    public String getDraweeBankName() {
        return draweeBankName;
    }

    /**
     * Setter/Getter for DRAWEE_BRANCH_NAME - table Field
     */
    public void setDraweeBranchName(String draweeBranchName) {
        this.draweeBranchName = draweeBranchName;
    }

    public String getDraweeBranchName() {
        return draweeBranchName;
    }

    /**
     * Setter/Getter for INSTRUMENT_NO - table Field
     */
    public void setInstrumentNo(String instrumentNo) {
        this.instrumentNo = instrumentNo;
    }

    public String getInstrumentNo() {
        return instrumentNo;
    }

    /**
     * Setter/Getter for INSTRUMENT_DT - table Field
     */
    public void setInstrumentDt(Date instrumentDt) {
        this.instrumentDt = instrumentDt;
    }

    public Date getInstrumentDt() {
        return instrumentDt;
    }

    /**
     * Setter/Getter for INSTRUMENT_AMOUNT - table Field
     */
    public void setInstrumentAmount(Double instrumentAmount) {
        this.instrumentAmount = instrumentAmount;
    }

    public Double getInstrumentAmount() {
        return instrumentAmount;
    }

    /**
     * Setter/Getter for MICR - table Field
     */
    public void setMicr(String micr) {
        this.micr = micr;
    }

    public String getMicr() {
        return micr;
    }

    /**
     * Setter/Getter for PAYEE_NAME - table Field
     */
    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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
        setKeyColumns(lodgementId);
        return lodgementId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("lodgementId", lodgementId));
        strB.append(getTOString("draweeBankName", draweeBankName));
        strB.append(getTOString("draweeBranchName", draweeBranchName));
        strB.append(getTOString("instrumentNo", instrumentNo));
        strB.append(getTOString("instrPrefix", instrPrefix));
        strB.append(getTOString("instrumentDt", instrumentDt));
        strB.append(getTOString("instrumentAmount", instrumentAmount));
        strB.append(getTOString("micr", micr));
        strB.append(getTOString("payeeName", payeeName));
        strB.append(getTOString("remarks", remarks));
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
        strB.append(getTOXml("lodgementId", lodgementId));
        strB.append(getTOXml("draweeBankName", draweeBankName));
        strB.append(getTOXml("draweeBranchName", draweeBranchName));
        strB.append(getTOXml("instrumentNo", instrumentNo));
        strB.append(getTOXml("instrPrefix", instrPrefix));
        strB.append(getTOXml("instrumentDt", instrumentDt));
        strB.append(getTOXml("instrumentAmount", instrumentAmount));
        strB.append(getTOXml("micr", micr));
        strB.append(getTOXml("payeeName", payeeName));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}