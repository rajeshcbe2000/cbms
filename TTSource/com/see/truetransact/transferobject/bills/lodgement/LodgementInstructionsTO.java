/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementInstructionsTO.java
 * 
 * Created on Mon Feb 07 12:40:07 IST 2005
 */
package com.see.truetransact.transferobject.bills.lodgement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BILLS_LODGEMENT_INSTRUCTION.
 */
public class LodgementInstructionsTO extends TransferObject implements Serializable {

    private String lodgementId = "";
    private String slNo = "";
    private String instruction = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Double amount = null;
    private Double serviceTx = null;

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
     * Setter/Getter for SL_NO - table Field
     */
    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getSlNo() {
        return slNo;
    }

    /**
     * Setter/Getter for INSTRUCTION - table Field
     */
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
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
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("instruction", instruction));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("serviceTx", serviceTx));
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
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("instruction", instruction));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("serviceTx", serviceTx));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     */
    public java.lang.Double getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     */
    public void setAmount(java.lang.Double amount) {
        this.amount = amount;
    }

    /**
     * Getter for property serviceTx.
     *
     * @return Value of property serviceTx.
     */
    public java.lang.Double getServiceTx() {
        return serviceTx;
    }

    /**
     * Setter for property serviceTx.
     *
     * @param serviceTx New value of property serviceTx.
     */
    public void setServiceTx(java.lang.Double serviceTx) {
        this.serviceTx = serviceTx;
    }
}