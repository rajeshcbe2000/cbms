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
package com.see.truetransact.transferobject.payroll.arrear;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is ARREAR_CALCULATION_MASTER.
 */

public class ArrearTO extends TransferObject implements Serializable {

    private String employeeId = "";
    private String payCode = "";  
    private String payDesc = "";  
    private Double oldAmount = 0.0; 
    private Double newAmount = 0.0;
    private Double difference = 0.0;
    private Date fromDt = null;
    private Date toDt = null;
    private String baseOn = "";
    private Double newDaPer = 0.0;
    private Double oldDaPer = 0.0;
    private String status = "";
    private Date transDt = null;

    public Date getTransDt() {
        return transDt;
    }

    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }
    
    
    public Double getDifference() {
        return difference;
    }

    public void setDifference(Double difference) {
        this.difference = difference;
    }  

    public String getBaseOn() {
        return baseOn;
    }

    public void setBaseOn(String baseOn) {
        this.baseOn = baseOn;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Date getFromDt() {
        return fromDt;
    }

    public void setFromDt(Date fromDt) {
        this.fromDt = fromDt;
    }

    public Double getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(Double newAmount) {
        this.newAmount = newAmount;
    }

    public Double getNewDaPer() {
        return newDaPer;
    }

    public void setNewDaPer(Double newDaPer) {
        this.newDaPer = newDaPer;
    }

    public Double getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(Double oldAmount) {
        this.oldAmount = oldAmount;
    }

    public Double getOldDaPer() {
        return oldDaPer;
    }

    public void setOldDaPer(Double oldDaPer) {
        this.oldDaPer = oldDaPer;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayDesc() {
        return payDesc;
    }

    public void setPayDesc(String payDesc) {
        this.payDesc = payDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getToDt() {
        return toDt;
    }

    public void setToDt(Date toDt) {
        this.toDt = toDt;
    }
    
   
    /**
     * toString method which returns this TO as a String.
     */
    
    public String getKeyData() {
        setKeyColumns("");
        return "";
    }
    
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("employeeId", employeeId));
        strB.append(getTOString("payCode", payCode));
        strB.append(getTOString("payDesc", payDesc));
        strB.append(getTOString("oldAmount", oldAmount));
        strB.append(getTOString("newAmount", newAmount));
        strB.append(getTOString("fromDt", fromDt));
        strB.append(getTOString("toDt", toDt));
        strB.append(getTOString("baseOn", baseOn));
        strB.append(getTOString("newDaPer", newDaPer));
        strB.append(getTOString("oldDaPer", oldDaPer));
        strB.append(getTOString("status", status));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("employeeId", employeeId));
        strB.append(getTOXml("payCode", payCode));
        strB.append(getTOXml("payDesc", payDesc));
        strB.append(getTOXml("oldAmount", oldAmount));
        strB.append(getTOXml("newAmount", newAmount));
        strB.append(getTOXml("fromDt", fromDt));
        strB.append(getTOXml("toDt", toDt));
        strB.append(getTOXml("baseOn", baseOn));
        strB.append(getTOXml("newDaPer", newDaPer));
        strB.append(getTOXml("oldDaPer", oldDaPer));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}