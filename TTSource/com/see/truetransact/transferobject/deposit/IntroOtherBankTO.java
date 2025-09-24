/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IntroOtherBankTO.java
 *
 * Created on Tue Apr 13 14:50:56 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_INTRO_OTHERBANK.
 */
public class IntroOtherBankTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private String bankName = "";
    private String branchName = "";
    private String otherActNum = "";
    private String otherActName = "";

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
     * Setter/Getter for BANK_NAME - table Field
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    /**
     * Setter/Getter for BRANCH_NAME - table Field
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    /**
     * Setter/Getter for OTHER_ACT_NUM - table Field
     */
    public void setOtherActNum(String otherActNum) {
        this.otherActNum = otherActNum;
    }

    public String getOtherActNum() {
        return otherActNum;
    }

    /**
     * Setter/Getter for OTHER_ACT_NAME - table Field
     */
    public void setOtherActName(String otherActName) {
        this.otherActName = otherActName;
    }

    public String getOtherActName() {
        return otherActName;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("depositNo");
        return depositNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("bankName", bankName));
        strB.append(getTOString("branchName", branchName));
        strB.append(getTOString("otherActNum", otherActNum));
        strB.append(getTOString("otherActName", otherActName));
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
        strB.append(getTOXml("bankName", bankName));
        strB.append(getTOXml("branchName", branchName));
        strB.append(getTOXml("otherActNum", otherActNum));
        strB.append(getTOXml("otherActName", otherActName));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}