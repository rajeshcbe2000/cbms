/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFTransferTO.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.transferobject.passbookDataEntry;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is OTHER_BANK_PASSBOOK.
 */
public class PassbookDataEntryTO extends TransferObject implements Serializable {

    private String bankCode = "";
    private String branchCode = "";
    private String transId = "";
    private String transType = "";
    private Date transDate = null;
    private String instType = "";
    private Date instDate = null;
    private String instNo1 = "";
    private String instNo2 = "";
    private String particulars = "";
    private Double amount = 0.0;
    private Double balance = 0.0;
    private Integer srlNo = 0;
    private String accType = "";
    

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }
        

    public Integer getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(Integer srlNo) {
        this.srlNo = srlNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public Date getInstDate() {
        return instDate;
    }

    public void setInstDate(Date instDate) {
        this.instDate = instDate;
    }

    public String getInstNo1() {
        return instNo1;
    }

    public void setInstNo1(String instNo1) {
        this.instNo1 = instNo1;
    }

    public String getInstNo2() {
        return instNo2;
    }

    public void setInstNo2(String instNo2) {
        this.instNo2 = instNo2;
    }

    public String getInstType() {
        return instType;
    }

    public void setInstType(String instType) {
        this.instType = instType;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
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
        strB.append(getTOString("bankCode", bankCode));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("transDate", transDate));
        strB.append(getTOString("instType", instType));
        strB.append(getTOString("instDate", instDate));
        strB.append(getTOString("instNo1", instNo1));
        strB.append(getTOString("instNo2", instNo2));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("balance", balance));
        strB.append(getTOString("srlNo", srlNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("bankCode", bankCode));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("transDate", transDate));
        strB.append(getTOXml("instType", instType));
        strB.append(getTOXml("instDate", instDate));
        strB.append(getTOXml("instNo1", instNo1));
        strB.append(getTOXml("instNo2", instNo2));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("balance", balance));
        strB.append(getTOXml("srlNo", srlNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}