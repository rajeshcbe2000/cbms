/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OutwardTallyDetailsTO.java
 * 
 * Created on Tue May 04 14:00:46 IST 2004
 */
package com.see.truetransact.transferobject.clearing.outwardtally;

/**
 *
 * @author Hemant
 */
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OUTWARD_TALLY_DETAILS.
 */
public class OutwardTallyDetailsTO extends TransferObject implements Serializable {

    private String scheduleNo = "";
    private String currency = "";
    private Double sysBookedInstruments = null;
    private Double sysBookedAmount = null;
    private Double sysInretInstruments = null;
    private Double sysInretAmount = null;
    private Double servInstruments = null;
    private Double servAmount = null;
    private Double diffInstruments = null;
    private Double diffAmount = null;
    private String status = "";

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
     * Setter/Getter for CURRENCY - table Field
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
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
     * Setter/Getter for SYS_INRET_INSTRUMENTS - table Field
     */
    public void setSysInretInstruments(Double sysInretInstruments) {
        this.sysInretInstruments = sysInretInstruments;
    }

    public Double getSysInretInstruments() {
        return sysInretInstruments;
    }

    /**
     * Setter/Getter for SYS_INRET_AMOUNT - table Field
     */
    public void setSysInretAmount(Double sysInretAmount) {
        this.sysInretAmount = sysInretAmount;
    }

    public Double getSysInretAmount() {
        return sysInretAmount;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    /*
     * public String getKeyData() { setKeyColumns(""); return "";
	}
     */
    /**
     * toString method which returns this TO as a String.
     */
    /*
     * public String toString() { StringBuffer strB = new
     * StringBuffer(getTOStringStart(this.getClass().getName())); //strB.append
     * (getTOStringKey(getKeyData())); strB.append(getTOString("scheduleNo",
     * scheduleNo)); strB.append(getTOString("currency", currency));
     * strB.append(getTOString("sysBookedInstruments", sysBookedInstruments));
     * strB.append(getTOString("sysBookedAmount", sysBookedAmount));
     * strB.append(getTOString("sysInretInstruments", sysInretInstruments));
     * strB.append(getTOString("sysInretAmount", sysInretAmount));
     * strB.append(getTOString("servInstruments", servInstruments));
     * strB.append(getTOString("servAmount", servAmount));
     * strB.append(getTOString("diffInstruments", diffInstruments));
     * strB.append(getTOString("diffAmount", diffAmount));
     * strB.append(getTOString("status", status));
     * strB.append(getTOStringEnd()); return strB.toString();
	}
     */
    /**
     * toXML method which returns this TO as a XML output.
     */
    /*
     * public String toXML() { StringBuffer strB = new
     * StringBuffer(getTOXmlStart(this.getClass().getName())); //strB.append
     * (getTOXmlKey(getKeyData())); strB.append(getTOXml("scheduleNo",
     * scheduleNo)); strB.append(getTOXml("currency", currency));
     * strB.append(getTOXml("sysBookedInstruments", sysBookedInstruments));
     * strB.append(getTOXml("sysBookedAmount", sysBookedAmount));
     * strB.append(getTOXml("sysInretInstruments", sysInretInstruments));
     * strB.append(getTOXml("sysInretAmount", sysInretAmount));
     * strB.append(getTOXml("servInstruments", servInstruments));
     * strB.append(getTOXml("servAmount", servAmount));
     * strB.append(getTOXml("diffInstruments", diffInstruments));
     * strB.append(getTOXml("diffAmount", diffAmount));
     * strB.append(getTOXml("status", status)); strB.append(getTOXmlEnd());
     * return strB.toString();
	}
     */
}