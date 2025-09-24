/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanPowerAttorneyTO.java
 * @author shanmugavel
 * Created on Mon May 10 12:31:59 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan.accounttransfer;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_POA.
 */
public class ActTransTO extends TransferObject implements Serializable {

    private String bankName = "";
    private String branchName = "";
    private String command = "";
    private String toRefNo = "";
    private Double toAmt = null;
    private String secDocRec = "";
    private String poddNo = "";
    private Date poddDt = null;
    private Double poddAmt = null;
    private String remarks = "";
    private String status = "";
    private String acctNum = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("bankName" + KEY_VAL_SEPARATOR + "branchName");
        return bankName + KEY_VAL_SEPARATOR + branchName;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("bankName", bankName));
        strB.append(getTOString("branchName", branchName));
        strB.append(getTOString("toRefNo", toRefNo));
        strB.append(getTOString("toAmt", toAmt));
        strB.append(getTOString("secDocRec", secDocRec));
        strB.append(getTOString("poddNo", poddNo));
        strB.append(getTOString("poddDt", poddDt));
        strB.append(getTOString("poddAmt", poddAmt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("bankName", bankName));
        strB.append(getTOXml("branchName", branchName));
        strB.append(getTOXml("toRefNo", toRefNo));
        strB.append(getTOXml("toAmt", toAmt));
        strB.append(getTOXml("secDocRec", secDocRec));
        strB.append(getTOXml("poddNo", poddNo));
        strB.append(getTOXml("poddDt", poddDt));
        strB.append(getTOXml("poddAmt", poddAmt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property bankName.
     *
     * @return Value of property bankName.
     */
    public java.lang.String getBankName() {
        return bankName;
    }

    /**
     * Setter for property bankName.
     *
     * @param bankName New value of property bankName.
     */
    public void setBankName(java.lang.String bankName) {
        this.bankName = bankName;
    }

    /**
     * Getter for property branchName.
     *
     * @return Value of property branchName.
     */
    public java.lang.String getBranchName() {
        return branchName;
    }

    /**
     * Setter for property branchName.
     *
     * @param branchName New value of property branchName.
     */
    public void setBranchName(java.lang.String branchName) {
        this.branchName = branchName;
    }

    /**
     * Getter for property command.
     *
     * @return Value of property command.
     */
    public java.lang.String getCommand() {
        return command;
    }

    /**
     * Setter for property command.
     *
     * @param command New value of property command.
     */
    public void setCommand(java.lang.String command) {
        this.command = command;
    }

    /**
     * Getter for property toRefNo.
     *
     * @return Value of property toRefNo.
     */
    public java.lang.String getToRefNo() {
        return toRefNo;
    }

    /**
     * Setter for property toRefNo.
     *
     * @param toRefNo New value of property toRefNo.
     */
    public void setToRefNo(java.lang.String toRefNo) {
        this.toRefNo = toRefNo;
    }

    /**
     * Getter for property toAmt.
     *
     * @return Value of property toAmt.
     */
    public java.lang.Double getToAmt() {
        return toAmt;
    }

    /**
     * Setter for property toAmt.
     *
     * @param toAmt New value of property toAmt.
     */
    public void setToAmt(java.lang.Double toAmt) {
        this.toAmt = toAmt;
    }

    /**
     * Getter for property secDocRec.
     *
     * @return Value of property secDocRec.
     */
    public java.lang.String getSecDocRec() {
        return secDocRec;
    }

    /**
     * Setter for property secDocRec.
     *
     * @param secDocRec New value of property secDocRec.
     */
    public void setSecDocRec(java.lang.String secDocRec) {
        this.secDocRec = secDocRec;
    }

    /**
     * Getter for property poddNo.
     *
     * @return Value of property poddNo.
     */
    public java.lang.String getPoddNo() {
        return poddNo;
    }

    /**
     * Setter for property poddNo.
     *
     * @param poddNo New value of property poddNo.
     */
    public void setPoddNo(java.lang.String poddNo) {
        this.poddNo = poddNo;
    }

    /**
     * Getter for property poddDt.
     *
     * @return Value of property poddDt.
     */
    public java.util.Date getPoddDt() {
        return poddDt;
    }

    /**
     * Setter for property poddDt.
     *
     * @param poddDt New value of property poddDt.
     */
    public void setPoddDt(java.util.Date poddDt) {
        this.poddDt = poddDt;
    }

    /**
     * Getter for property poddAmt.
     *
     * @return Value of property poddAmt.
     */
    public java.lang.Double getPoddAmt() {
        return poddAmt;
    }

    /**
     * Setter for property poddAmt.
     *
     * @param poddAmt New value of property poddAmt.
     */
    public void setPoddAmt(java.lang.Double poddAmt) {
        this.poddAmt = poddAmt;
    }

    /**
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
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
}