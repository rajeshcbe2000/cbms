/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CardAccountTO.java
 * 
 * Created on Tue Dec 1 01:30:20 IST 2015
 * Created By Suresh R
 */
package com.see.truetransact.transferobject.operativeaccount;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CARD_ACCT_STATUS.
 */
public class CardAccountTO extends TransferObject implements Serializable {

    private String cardActNum = "";
    private String slNo = "";
    private String action = "";
    private Date actionDt = null;
    private String remarks = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String authorizedStatus = null;

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("cardActNum", cardActNum));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("action", action));
        strB.append(getTOString("actionDt", actionDt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("cardActNum", cardActNum));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("action", action));
        strB.append(getTOXml("actionDt", actionDt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getCardActNum() {
        return cardActNum;
    }

    public void setCardActNum(String cardActNum) {
        this.cardActNum = cardActNum;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getActionDt() {
        return actionDt;
    }

    public void setActionDt(Date actionDt) {
        this.actionDt = actionDt;
    }
    
}