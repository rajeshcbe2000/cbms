/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LodgementInstrTO.java
 */

package com.see.truetransact.transferobject.bills.lodgement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BILLS_LODGEMENT_INSTRUCTION.
 */
public class LodgementInstrTO extends TransferObject implements Serializable {

    private String instLodgementId = "";
    private String instSlNo = "";
    private String instInstruction = "";
    private String instParticulars = "";
    private String instStatus = "";
    private String instStatusBy = "";
    private Date instStatusDt = null;

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(instLodgementId);
        return instLodgementId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("instLodgementId", instLodgementId));
        strB.append(getTOString("instSlNo", instSlNo));
        strB.append(getTOString("instInstruction", instInstruction));
        strB.append(getTOString("instParticulars", instParticulars));
        strB.append(getTOString("instStatus", instStatus));
        strB.append(getTOString("instStatusBy", instStatusBy));
        strB.append(getTOString("instStatusDt", instStatusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("instLodgementId", instLodgementId));
        strB.append(getTOXml("instSlNo", instSlNo));
        strB.append(getTOXml("instInstruction", instInstruction));
        strB.append(getTOXml("instParticulars", instParticulars));
        strB.append(getTOXml("instStatus", instStatus));
        strB.append(getTOXml("instStatusBy", instStatusBy));
        strB.append(getTOXml("instStatusDt", instStatusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property instLodgementId.
     *
     * @return Value of property instLodgementId.
     */
    public java.lang.String getInstLodgementId() {
        return instLodgementId;
    }

    /**
     * Setter for property instLodgementId.
     *
     * @param instLodgementId New value of property instLodgementId.
     */
    public void setInstLodgementId(java.lang.String instLodgementId) {
        this.instLodgementId = instLodgementId;
    }

    /**
     * Getter for property instSlNo.
     *
     * @return Value of property instSlNo.
     */
    public java.lang.String getInstSlNo() {
        return instSlNo;
    }

    /**
     * Setter for property instSlNo.
     *
     * @param instSlNo New value of property instSlNo.
     */
    public void setInstSlNo(java.lang.String instSlNo) {
        this.instSlNo = instSlNo;
    }

    /**
     * Getter for property instInstruction.
     *
     * @return Value of property instInstruction.
     */
    public java.lang.String getInstInstruction() {
        return instInstruction;
    }

    /**
     * Setter for property instInstruction.
     *
     * @param instInstruction New value of property instInstruction.
     */
    public void setInstInstruction(java.lang.String instInstruction) {
        this.instInstruction = instInstruction;
    }

    /**
     * Getter for property instStatus.
     *
     * @return Value of property instStatus.
     */
    public java.lang.String getInstStatus() {
        return instStatus;
    }

    /**
     * Setter for property instStatus.
     *
     * @param instStatus New value of property instStatus.
     */
    public void setInstStatus(java.lang.String instStatus) {
        this.instStatus = instStatus;
    }

    /**
     * Getter for property instStatusBy.
     *
     * @return Value of property instStatusBy.
     */
    public java.lang.String getInstStatusBy() {
        return instStatusBy;
    }

    /**
     * Setter for property instStatusBy.
     *
     * @param instStatusBy New value of property instStatusBy.
     */
    public void setInstStatusBy(java.lang.String instStatusBy) {
        this.instStatusBy = instStatusBy;
    }

    /**
     * Getter for property instStatusDt.
     *
     * @return Value of property instStatusDt.
     */
    public java.util.Date getInstStatusDt() {
        return instStatusDt;
    }

    /**
     * Setter for property instStatusDt.
     *
     * @param instStatusDt New value of property instStatusDt.
     */
    public void setInstStatusDt(java.util.Date instStatusDt) {
        this.instStatusDt = instStatusDt;
    }

    /**
     * Getter for property instParticulars.
     *
     * @return Value of property instParticulars.
     */
    public java.lang.String getInstParticulars() {
        return instParticulars;
    }

    /**
     * Setter for property instParticulars.
     *
     * @param instParticulars New value of property instParticulars.
     */
    public void setInstParticulars(java.lang.String instParticulars) {
        this.instParticulars = instParticulars;
    }
}