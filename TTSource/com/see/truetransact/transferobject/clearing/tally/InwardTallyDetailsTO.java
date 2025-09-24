/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardTallyDetailsTO.java
 * 
 * Created on Wed May 05 12:36:28 IST 2004
 */
package com.see.truetransact.transferobject.clearing.tally;

/**
 *
 * @author Hemant
 */
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is INWARD_TALLY_DETAILS.
 */
public class InwardTallyDetailsTO extends TransferObject implements Serializable {

    private String tallyId = "";
    private String scheduleNo = "";
    private Double servInstruments = null;
    private Double servAmount = null;
    private Double sysBookedInstruments = null;
    private Double sysBookedAmount = null;
    private Double sysOutretInstruments = null;
    private Double sysOutretAmount = null;
    private Double phyInstruments = null;
    private Double phyAmount = null;
    private Double diffInstruments = null;
    private Double diffAmount = null;
    private String status = "";
    private String currency = "";

    /**
     * Setter/Getter for TALLY_ID - table Field
     */
    public void setTallyId(String tallyId) {
        this.tallyId = tallyId;
    }

    public String getTallyId() {
        return tallyId;
    }

    /**
     * Setter/Getter for SCHEDULE_NO - table Field
     */
    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    /**
     * Setter/Getter for SERV_INSTRUMENTS - table Field
     */
    public void setServInstruments(Double servInstruments) {
        this.servInstruments = servInstruments;
    }

    public Double getServInstruments() {
        return servInstruments;
    }

    /**
     * Setter/Getter for SERV_AMOUNT - table Field
     */
    public void setServAmount(Double servAmount) {
        this.servAmount = servAmount;
    }

    public Double getServAmount() {
        return servAmount;
    }

    /**
     * Setter/Getter for SYS_BOOKED_INSTRUMENTS - table Field
     */
    public void setSysBookedInstruments(Double sysBookedInstruments) {
        this.sysBookedInstruments = sysBookedInstruments;
    }

    public Double getSysBookedInstruments() {
        return sysBookedInstruments;
    }

    /**
     * Setter/Getter for SYS_BOOKED_AMOUNT - table Field
     */
    public void setSysBookedAmount(Double sysBookedAmount) {
        this.sysBookedAmount = sysBookedAmount;
    }

    public Double getSysBookedAmount() {
        return sysBookedAmount;
    }

    /**
     * Setter/Getter for SYS_OUTRET_INSTRUMENTS - table Field
     */
    public void setSysOutretInstruments(Double sysOutretInstruments) {
        this.sysOutretInstruments = sysOutretInstruments;
    }

    public Double getSysOutretInstruments() {
        return sysOutretInstruments;
    }

    /**
     * Setter/Getter for SYS_OUTRET_AMOUNT - table Field
     */
    public void setSysOutretAmount(Double sysOutretAmount) {
        this.sysOutretAmount = sysOutretAmount;
    }

    public Double getSysOutretAmount() {
        return sysOutretAmount;
    }

    /**
     * Setter/Getter for PHY_INSTRUMENTS - table Field
     */
    public void setPhyInstruments(Double phyInstruments) {
        this.phyInstruments = phyInstruments;
    }

    public Double getPhyInstruments() {
        return phyInstruments;
    }

    /**
     * Setter/Getter for PHY_AMOUNT - table Field
     */
    public void setPhyAmount(Double phyAmount) {
        this.phyAmount = phyAmount;
    }

    public Double getPhyAmount() {
        return phyAmount;
    }

    /**
     * Setter/Getter for DIFF_INSTRUMENTS - table Field
     */
    public void setDiffInstruments(Double diffInstruments) {
        this.diffInstruments = diffInstruments;
    }

    public Double getDiffInstruments() {
        return diffInstruments;
    }

    /**
     * Setter/Getter for DIFF_AMOUNT - table Field
     */
    public void setDiffAmount(Double diffAmount) {
        this.diffAmount = diffAmount;
    }

    public Double getDiffAmount() {
        return diffAmount;
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
     * Setter/Getter for CURRENCY - table Field
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
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
        strB.append(getTOString("tallyId", tallyId));
        strB.append(getTOString("scheduleNo", scheduleNo));
        strB.append(getTOString("servInstruments", servInstruments));
        strB.append(getTOString("servAmount", servAmount));
        strB.append(getTOString("sysBookedInstruments", sysBookedInstruments));
        strB.append(getTOString("sysBookedAmount", sysBookedAmount));
        strB.append(getTOString("sysOutretInstruments", sysOutretInstruments));
        strB.append(getTOString("sysOutretAmount", sysOutretAmount));
        strB.append(getTOString("phyInstruments", phyInstruments));
        strB.append(getTOString("phyAmount", phyAmount));
        strB.append(getTOString("diffInstruments", diffInstruments));
        strB.append(getTOString("diffAmount", diffAmount));
        strB.append(getTOString("status", status));
        strB.append(getTOString("currency", currency));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("tallyId", tallyId));
        strB.append(getTOXml("scheduleNo", scheduleNo));
        strB.append(getTOXml("servInstruments", servInstruments));
        strB.append(getTOXml("servAmount", servAmount));
        strB.append(getTOXml("sysBookedInstruments", sysBookedInstruments));
        strB.append(getTOXml("sysBookedAmount", sysBookedAmount));
        strB.append(getTOXml("sysOutretInstruments", sysOutretInstruments));
        strB.append(getTOXml("sysOutretAmount", sysOutretAmount));
        strB.append(getTOXml("phyInstruments", phyInstruments));
        strB.append(getTOXml("phyAmount", phyAmount));
        strB.append(getTOXml("diffInstruments", diffInstruments));
        strB.append(getTOXml("diffAmount", diffAmount));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("currency", currency));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}