/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInstallMultIntTO.java
 * 
 * Created on Fri Feb 25 17:21:31 IST 2005
 */
package com.see.truetransact.transferobject.termloan.agritermloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_INSTALLMENT_MULTIRATE.
 */
public class AgriTermLoanInstallMultIntTO extends TransferObject implements Serializable {

    private String acctNum = "";
    private Double scheduleId = null;
    private Double installmentSlno = null;
    private Double interestRate = null;
    private Date fromDt = null;
    private Date toDt = null;

    /**
     * Setter/Getter for ACCT_NUM - table Field
     */
    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter/Getter for SCHEDULE_ID - table Field
     */
    public void setScheduleId(Double scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Double getScheduleId() {
        return scheduleId;
    }

    /**
     * Setter/Getter for INSTALLMENT_SLNO - table Field
     */
    public void setInstallmentSlno(Double installmentSlno) {
        this.installmentSlno = installmentSlno;
    }

    public Double getInstallmentSlno() {
        return installmentSlno;
    }

    /**
     * Setter/Getter for INTEREST_RATE - table Field
     */
    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    /**
     * Setter/Getter for FROM_DT - table Field
     */
    public void setFromDt(Date fromDt) {
        this.fromDt = fromDt;
    }

    public Date getFromDt() {
        return fromDt;
    }

    /**
     * Setter/Getter for TO_DT - table Field
     */
    public void setToDt(Date toDt) {
        this.toDt = toDt;
    }

    public Date getToDt() {
        return toDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNum" + KEY_VAL_SEPARATOR + "scheduleId" + KEY_VAL_SEPARATOR + "installmentSlno");
        return acctNum + KEY_VAL_SEPARATOR + scheduleId + KEY_VAL_SEPARATOR + installmentSlno;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("scheduleId", scheduleId));
        strB.append(getTOString("installmentSlno", installmentSlno));
        strB.append(getTOString("interestRate", interestRate));
        strB.append(getTOString("fromDt", fromDt));
        strB.append(getTOString("toDt", toDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("scheduleId", scheduleId));
        strB.append(getTOXml("installmentSlno", installmentSlno));
        strB.append(getTOXml("interestRate", interestRate));
        strB.append(getTOXml("fromDt", fromDt));
        strB.append(getTOXml("toDt", toDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}