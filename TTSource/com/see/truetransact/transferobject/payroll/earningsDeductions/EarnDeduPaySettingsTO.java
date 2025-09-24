/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EarnDeduPaySettingsTO.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.transferobject.payroll.earningsDeductions;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is PAY_SETTINGS.
 */
public class EarnDeduPaySettingsTO extends TransferObject implements Serializable {

    private String paycode_Id = "";
    private Integer srlNo = 0;
    private String payCode = "";
    private String payCalcOn = "";
    private Double payMinAmt = 0.0;
    private Double payMaxAmt = 0.0;
    private Double payFixAmt = 0.0;
    private Double payPercent = 0.0;
    private Date fromDate = null;
    private Date toDate = null;

    public String getPaycode_Id() {
        return paycode_Id;
    }

    public void setPaycode_Id(String paycode_Id) {
        this.paycode_Id = paycode_Id;
    }

    public Integer getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(Integer srlNo) {
        this.srlNo = srlNo;
    }

    public Double getPayFixAmt() {
        return payFixAmt;
    }

    public void setPayFixAmt(Double payFixAmt) {
        this.payFixAmt = payFixAmt;
    }

    public Double getPayMaxAmt() {
        return payMaxAmt;
    }

    public void setPayMaxAmt(Double payMaxAmt) {
        this.payMaxAmt = payMaxAmt;
    }

    public Double getPayMinAmt() {
        return payMinAmt;
    }

    public void setPayMinAmt(Double payMinAmt) {
        this.payMinAmt = payMinAmt;
    }

    public Double getPayPercent() {
        return payPercent;
    }

    public void setPayPercent(Double payPercent) {
        this.payPercent = payPercent;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getPayCalcOn() {
        return payCalcOn;
    }

    public void setPayCalcOn(String payCalcOn) {
        this.payCalcOn = payCalcOn;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
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
        strB.append(getTOString("paycode_Id", paycode_Id));
        strB.append(getTOString("srlNo", srlNo));
        strB.append(getTOString("payCode", payCode));
        strB.append(getTOString("payCalcOn", payCalcOn));
        strB.append(getTOString("payMinAmt", payMinAmt));
        strB.append(getTOString("payMaxAmt", payMaxAmt));
        strB.append(getTOString("payFixAmt", payFixAmt));
        strB.append(getTOString("payPercent", payPercent));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("paycode_Id", paycode_Id));
        strB.append(getTOXml("srlNo", srlNo));
        strB.append(getTOXml("payCode", payCode));
        strB.append(getTOXml("payCalcOn", payCalcOn));
        strB.append(getTOXml("payMinAmt", payMinAmt));
        strB.append(getTOXml("payMaxAmt", payMaxAmt));
        strB.append(getTOXml("payFixAmt", payFixAmt));
        strB.append(getTOXml("payPercent", payPercent));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}