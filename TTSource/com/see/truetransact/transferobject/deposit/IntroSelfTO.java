/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IntroSelfTO.java
 *
 * Created on Tue Apr 13 14:52:36 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_INTRO_SELF.
 */
public class IntroSelfTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private String acctNo = "";
    private String introCustId = "";

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
     * Setter/Getter for ACCT_NO - table Field
     */
    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctNo() {
        return acctNo;
    }

    /**
     * Setter/Getter for INTRO_CUST_ID - table Field
     */
    public void setIntroCustId(String introCustId) {
        this.introCustId = introCustId;
    }

    public String getIntroCustId() {
        return introCustId;
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
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOString("introCustId", introCustId));
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
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("introCustId", introCustId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}