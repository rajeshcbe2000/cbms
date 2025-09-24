/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositWithDrawalTO.java
 * 
 * Created on Fri Jun 18 17:04:05 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit.closing;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_WITHDRAW.
 */
public class DepositWithDrawalTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private Date withdrawDt = null;
    private Double withdrawAmt = null;
    private Double noOfUnits = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Integer depositSubNo = 0;
    private String withdrawNo = "";

    /**
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
    }

    /**
     * Setter/Getter for WITHDRAW_DT - table Field
     */
    public void setWithdrawDt(Date withdrawDt) {
        this.withdrawDt = withdrawDt;
    }

    public Date getWithdrawDt() {
        return withdrawDt;
    }

    /**
     * Setter/Getter for WITHDRAW_AMT - table Field
     */
    public void setWithdrawAmt(Double withdrawAmt) {
        this.withdrawAmt = withdrawAmt;
    }

    public Double getWithdrawAmt() {
        return withdrawAmt;
    }

    /**
     * Setter/Getter for NO_OF_UNITS - table Field
     */
    public void setNoOfUnits(Double noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    public Double getNoOfUnits() {
        return noOfUnits;
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
     * Setter/Getter for DEPOSIT_SUB_NO - table Field
     */
    

    /**
     * Setter/Getter for WITHDRAW_NO - table Field
     */
    public void setWithdrawNo(String withdrawNo) {
        this.withdrawNo = withdrawNo;
    }

    public Integer getDepositSubNo() {
        return depositSubNo;
    }

    public void setDepositSubNo(Integer depositSubNo) {
        this.depositSubNo = depositSubNo;
    }

    public String getWithdrawNo() {
        return withdrawNo;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("withdrawNo");
        return withdrawNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("withdrawDt", withdrawDt));
        strB.append(getTOString("withdrawAmt", withdrawAmt));
        strB.append(getTOString("noOfUnits", noOfUnits));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("depositSubNo", depositSubNo));
        strB.append(getTOString("withdrawNo", withdrawNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("withdrawDt", withdrawDt));
        strB.append(getTOXml("withdrawAmt", withdrawAmt));
        strB.append(getTOXml("noOfUnits", noOfUnits));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("depositSubNo", depositSubNo));
        strB.append(getTOXml("withdrawNo", withdrawNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}