/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * StandingInstructionTaskTO.java
 * 
 * Created on Wed Aug 04 15:38:52 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.batchprocess.supporting.standinginstruction;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is STANDING_INSTRUCTION_BATCH.
 */
public class StandingInstructionTaskTO extends TransferObject implements Serializable {

    private Date execDt = null;
    private String status = "";
    private String siId = "";
    private Double installment = null;
    private String batchID = "";

    /**
     * Setter/Getter for EXEC_DT - table Field
     */
    public void setExecDt(Date execDt) {
        this.execDt = execDt;
    }

    public Date getExecDt() {
        return execDt;
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
     * Setter/Getter for SI_ID - table Field
     */
    public void setSiId(String siId) {
        this.siId = siId;
    }

    public String getSiId() {
        return siId;
    }

    /**
     * Setter/Getter for INSTALLMENT - table Field
     */
    public void setInstallment(Double installment) {
        this.installment = installment;
    }

    public Double getInstallment() {
        return installment;
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
        strB.append(getTOString("execDt", execDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("siId", siId));
        strB.append(getTOString("installment", installment));
        strB.append(getTOString("batchID", batchID));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("execDt", execDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("siId", siId));
        strB.append(getTOXml("installment", installment));
        strB.append(getTOXml("batchID", batchID));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property batchID.
     *
     * @return Value of property batchID.
     */
    public java.lang.String getBatchID() {
        return batchID;
    }

    /**
     * Setter for property batchID.
     *
     * @param batchID New value of property batchID.
     */
    public void setBatchID(java.lang.String batchID) {
        this.batchID = batchID;
    }
}