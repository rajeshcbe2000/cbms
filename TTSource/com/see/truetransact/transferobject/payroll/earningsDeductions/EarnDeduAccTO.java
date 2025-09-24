/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EarnDeduAccTO.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.transferobject.payroll.earningsDeductions;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;

/**
 * Table name for this TO is PAY_ACCOUNT.
 */
public class EarnDeduAccTO extends TransferObject implements Serializable {

    private String payCode_Id = "";
    private String accHd = "";
    private String accType = "";

    public String getAccHd() {
        return accHd;
    }

    public void setAccHd(String accHd) {
        this.accHd = accHd;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getPayCode_Id() {
        return payCode_Id;
    }

    public void setPayCode_Id(String payCode_Id) {
        this.payCode_Id = payCode_Id;
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
        strB.append(getTOString("payCode_Id", payCode_Id));
        strB.append(getTOString("accHd", accHd));
        strB.append(getTOString("accType", accType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("payCode_Id", payCode_Id));
        strB.append(getTOXml("accHd", accHd));
        strB.append(getTOXml("accType", accType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}