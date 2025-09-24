/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SponsorBankDetailsTO.java
 * 
 * Created on Thu Aug 12 12:40:27 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.transaction.ATMTrans;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SPONSOR_BANK_ACKNOWLEDGEMENT.
 */
public class SponsorBankDetailsTO extends TransferObject implements Serializable {
    private String acctNum = "";
    private String cardAcctNum = null;
    private String transId = "";
    private String sequenceNo = "";
    private Double amount = null;
    private Date transDt = null;
    private String transType = "";
    private String status = "";
    private String statusBy = "";
    private String initiatedBranch = "";
    private Date statusDt = null;
    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public SponsorBankDetailsTO() {
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("transId");
        return transId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("cardAcctNum", cardAcctNum));
        strB.append(getTOString("sequenceNo", sequenceNo));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
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
        strB.append(getTOXml("cardAcctNum", cardAcctNum));
        strB.append(getTOXml("sequenceNo", sequenceNo));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCardAcctNum() {
        return cardAcctNum;
    }

    public void setCardAcctNum(String cardAcctNum) {
        this.cardAcctNum = cardAcctNum;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTransDt() {
        return transDt;
    }

    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getInitiatedBranch() {
        return initiatedBranch;
    }

    public void setInitiatedBranch(String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }
}