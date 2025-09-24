/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanBorrowerTO.java
 * 
 * Created on Wed Apr 13 18:02:42 IST 2005
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_BORROWER.
 */
public class TermLoanOTSInstallmentTO extends TransferObject implements Serializable {

    private String acctNum = "";
    private String instllmentNo = "";
    private Date installmentDate = null;
    private Double installmentAmt = null;
    private String status = "";
    private String slno = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNum");
        return acctNum;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("instllmentNo", instllmentNo));
        strB.append(getTOString("installmentDate", installmentDate));
        strB.append(getTOString("installmentAmt", installmentAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("slno", slno));
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
        strB.append(getTOXml("instllmentNo", instllmentNo));
        strB.append(getTOXml("installmentDate", installmentDate));
        strB.append(getTOXml("installmentAmt", installmentAmt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property acctNum.
     *
     * @return Value of property acctNum.
     */
    public java.lang.String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter for property acctNum.
     *
     * @param acctNum New value of property acctNum.
     */
    public void setAcctNum(java.lang.String acctNum) {
        this.acctNum = acctNum;
    }

    /**
     * Getter for property instllmentNo.
     *
     * @return Value of property instllmentNo.
     */
    public java.lang.String getInstllmentNo() {
        return instllmentNo;
    }

    /**
     * Setter for property instllmentNo.
     *
     * @param instllmentNo New value of property instllmentNo.
     */
    public void setInstllmentNo(java.lang.String instllmentNo) {
        this.instllmentNo = instllmentNo;
    }

    /**
     * Getter for property installmentDate.
     *
     * @return Value of property installmentDate.
     */
    public java.util.Date getInstallmentDate() {
        return installmentDate;
    }

    /**
     * Setter for property installmentDate.
     *
     * @param installmentDate New value of property installmentDate.
     */
    public void setInstallmentDate(java.util.Date installmentDate) {
        this.installmentDate = installmentDate;
    }

    /**
     * Getter for property installmentAmt.
     *
     * @return Value of property installmentAmt.
     */
    public java.lang.Double getInstallmentAmt() {
        return installmentAmt;
    }

    /**
     * Setter for property installmentAmt.
     *
     * @param installmentAmt New value of property installmentAmt.
     */
    public void setInstallmentAmt(java.lang.Double installmentAmt) {
        this.installmentAmt = installmentAmt;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property slno.
     *
     * @return Value of property slno.
     */
    public java.lang.String getSlno() {
        return slno;
    }

    /**
     * Setter for property slno.
     *
     * @param slno New value of property slno.
     */
    public void setSlno(java.lang.String slno) {
        this.slno = slno;
    }
}