/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AuthorizedSignatoryInstructionTO.java
 * 
 * Created on Tue Mar 01 17:23:52 IST 2005
 */
package com.see.truetransact.transferobject.common.authorizedsignatory;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_AUTHORIZE_INSTRUCTION.
 */
public class AuthorizedSignatoryInstructionTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String acctNum = "";
    private Integer slNo = null;
    private Double fromAmt = null;
    private Double toAmt = null;
    private String instruction = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

    /**
     * Setter/Getter for BORROW_NO - table Field
     */
    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    /**
     * Setter/Getter for SL_NO - table Field
     */
    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }

    
    

    /**
     * Setter/Getter for FROM_AMT - table Field
     */
    public void setFromAmt(Double fromAmt) {
        this.fromAmt = fromAmt;
    }

    public Double getFromAmt() {
        return fromAmt;
    }

    /**
     * Setter/Getter for TO_AMT - table Field
     */
    public void setToAmt(Double toAmt) {
        this.toAmt = toAmt;
    }

    public Double getToAmt() {
        return toAmt;
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
        setKeyColumns("borrowNo" + KEY_VAL_SEPARATOR + "slNo");
        return borrowNo + KEY_VAL_SEPARATOR + slNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("fromAmt", fromAmt));
        strB.append(getTOString("toAmt", toAmt));
        strB.append(getTOString("instruction", instruction));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("fromAmt", fromAmt));
        strB.append(getTOXml("toAmt", toAmt));
        strB.append(getTOXml("instruction", instruction));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
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
}