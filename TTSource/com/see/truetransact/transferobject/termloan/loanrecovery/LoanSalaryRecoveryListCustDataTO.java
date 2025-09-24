/*
 * Copyright 2013 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanSalaryRecoveryListMasterTO.java
 * 
 * Created on Fri Jan 22 16:21:35 IST 2019
 */
package com.see.truetransact.transferobject.termloan.loanrecovery;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is SALARY_RECOVERY_LIST_MASTER.
 */
public class LoanSalaryRecoveryListCustDataTO extends TransferObject implements Serializable {

    private Date intCalcUptoDt = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String recoveryId="";
    private String status ="";
    private String instId="";
    private String custId = "";
    private Double totalRecoveryAmt = 0.0;
    /**
     * Setter/Getter for INT_CALC_UPTO_DT - table Field
     */
    public void setIntCalcUptoDt(Date intCalcUptoDt) {
        this.intCalcUptoDt = intCalcUptoDt;
    }

    public Date getIntCalcUptoDt() {
        return intCalcUptoDt;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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

    public String getRecoveryId() {
        return recoveryId;
    }

    public void setRecoveryId(String recoveryId) {
        this.recoveryId = recoveryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Double getTotalRecoveryAmt() {
        return totalRecoveryAmt;
    }

    public void setTotalRecoveryAmt(Double totalRecoveryAmt) {
        this.totalRecoveryAmt = totalRecoveryAmt;
    }
    
    
    
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("intCalcUptoDt", intCalcUptoDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("recoveryId", recoveryId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("instId", instId));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("totalRecoveryAmt", totalRecoveryAmt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("intCalcUptoDt", intCalcUptoDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("recoveryId", recoveryId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("instId", instId));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("totalRecoveryAmt", totalRecoveryAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}