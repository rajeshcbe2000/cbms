/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInstallmentTO.java
 * 
 * Created on Thu Jul 28 17:13:06 IST 2005
 */
package com.see.truetransact.transferobject.termloan.agritermloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_INSTALLMENT.
 */
public class AgriTermLoanInstallmentTO extends TransferObject implements Serializable {

    private String acctNum = "";
    private Double scheduleId = null;
    private Double installmentSlno = null;
    private Date installmentDt = null;
    private Double principalAmt = null;
    private Double interestAmt = null;
    private Double totalAmt = null;
    private Double balanceAmt = null;
    private String status = "";
    private Double interestRate = null;
    private String installmentPaid = "";

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
     * Setter/Getter for INSTALLMENT_DT - table Field
     */
    public void setInstallmentDt(Date installmentDt) {
        this.installmentDt = installmentDt;
    }

    public Date getInstallmentDt() {
        return installmentDt;
    }

    /**
     * Setter/Getter for PRINCIPAL_AMT - table Field
     */
    public void setPrincipalAmt(Double principalAmt) {
        this.principalAmt = principalAmt;
    }

    public Double getPrincipalAmt() {
        return principalAmt;
    }

    /**
     * Setter/Getter for INTEREST_AMT - table Field
     */
    public void setInterestAmt(Double interestAmt) {
        this.interestAmt = interestAmt;
    }

    public Double getInterestAmt() {
        return interestAmt;
    }

    /**
     * Setter/Getter for TOTAL_AMT - table Field
     */
    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    /**
     * Setter/Getter for BALANCE_AMT - table Field
     */
    public void setBalanceAmt(Double balanceAmt) {
        this.balanceAmt = balanceAmt;
    }

    public Double getBalanceAmt() {
        return balanceAmt;
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
     * Setter/Getter for INTEREST_RATE - table Field
     */
    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    /**
     * Setter/Getter for INSTALLMENT_PAID - table Field
     */
    public void setInstallmentPaid(String installmentPaid) {
        this.installmentPaid = installmentPaid;
    }

    public String getInstallmentPaid() {
        return installmentPaid;
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
        strB.append(getTOString("installmentDt", installmentDt));
        strB.append(getTOString("principalAmt", principalAmt));
        strB.append(getTOString("interestAmt", interestAmt));
        strB.append(getTOString("totalAmt", totalAmt));
        strB.append(getTOString("balanceAmt", balanceAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("interestRate", interestRate));
        strB.append(getTOString("installmentPaid", installmentPaid));
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
        strB.append(getTOXml("installmentDt", installmentDt));
        strB.append(getTOXml("principalAmt", principalAmt));
        strB.append(getTOXml("interestAmt", interestAmt));
        strB.append(getTOXml("totalAmt", totalAmt));
        strB.append(getTOXml("balanceAmt", balanceAmt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("interestRate", interestRate));
        strB.append(getTOXml("installmentPaid", installmentPaid));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}