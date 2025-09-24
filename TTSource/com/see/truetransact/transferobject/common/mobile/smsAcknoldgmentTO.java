/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SMSSubscriptionTO.java
 * 
 * Created on Fri May 11 15:53:33 IST 2012
 */
package com.see.truetransact.transferobject.common.mobile;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SMS_SUBSCRIPTION.
 */
public class smsAcknoldgmentTO extends TransferObject implements Serializable {

    private String message = "";
    private String acknoldgment = "";    
    private Date sendDt = null;
    private String phoneNo = "";
    private String smsID = "";
    private String actNum = "";
    private String smsModule = "";
    private String acknowledgementId = "";
    private Integer processedCount;

    public String getAcknowledgementId() {
        return acknowledgementId;
    }

    public void setAcknowledgementId(String acknowledgementId) {
        this.acknowledgementId = acknowledgementId;
    }

    public Integer getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(Integer processedCount) {
        this.processedCount = processedCount;
    }
    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getSmsModule() {
        return smsModule;
    }

    public void setSmsModule(String smsModule) {
        this.smsModule = smsModule;
    }

    public String getSmsID() {
        return smsID;
    }

    public void setSmsID(String smsID) {
        this.smsID = smsID;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAcknoldgment() {
        return acknoldgment;
    }

    public void setAcknoldgment(String acknoldgment) {
        this.acknoldgment = acknoldgment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendDt() {
        return sendDt;
    }

    public void setSendDt(Date sendDt) {
        this.sendDt = sendDt;
    }
    

    /**
     * Setter/Getter for PROD_TYPE - table Field
     */
   

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
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
        strB.append(getTOString("message", message));
        strB.append(getTOString("acknoldgment", acknoldgment));
        strB.append(getTOString("sendDt", sendDt));
        strB.append(getTOString("phoneNo", phoneNo));
        strB.append(getTOString("smsID", smsID));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("smsModule", smsModule));
        strB.append(getTOString("acknowledgementId", acknowledgementId));
        strB.append(getTOString("processedCount", processedCount));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("message", message));
        strB.append(getTOXml("acknoldgment", acknoldgment));
        strB.append(getTOXml("sendDt", sendDt));
        strB.append(getTOXml("phoneNo", phoneNo));
        strB.append(getTOXml("smsID", smsID));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("smsModule", smsModule));
        strB.append(getTOXml("acknowledgementId", acknowledgementId));
        strB.append(getTOXml("processedCount", processedCount));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}