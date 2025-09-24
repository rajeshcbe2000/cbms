/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * IndendClosingTO.java
 */

package com.see.truetransact.transferobject.indend.closing;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Suresh R
 */

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUST_PHONE.
 */
public class IndendClosingTO extends TransferObject implements Serializable {
    private String depoCloseID = "";
    private String depoID = "";
    private Date closingDt = null;
    private String closingAmount = "";
    private String closingPerLessAmt = "";
    private String stockType = "";
    private String closingStockType = "";
    private String status = "";
    private String statusBy = "";
    
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("depoCloseID", depoCloseID));
        strB.append(getTOString("depoID", depoID));
        strB.append(getTOString("closingDt", closingDt));
        strB.append(getTOString("closingAmount", closingAmount));
        strB.append(getTOString("closingPerLessAmt", closingPerLessAmt));
        strB.append(getTOString("stockType", stockType));
        strB.append(getTOString("closingStockType", closingStockType));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("depoCloseID", depoCloseID));
        strB.append(getTOXml("depoID", depoID));
        strB.append(getTOXml("closingDt", closingDt));
        strB.append(getTOXml("closingAmount", closingAmount));
        strB.append(getTOXml("closingPerLessAmt", closingPerLessAmt));
        strB.append(getTOXml("stockType", stockType));
        strB.append(getTOXml("closingStockType", closingStockType));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getDepoID() {
        return depoID;
    }

    public void setDepoID(String depoID) {
        this.depoID = depoID;
    }

    public String getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(String closingAmount) {
        this.closingAmount = closingAmount;
    }

    public String getClosingPerLessAmt() {
        return closingPerLessAmt;
    }

    public void setClosingPerLessAmt(String closingPerLessAmt) {
        this.closingPerLessAmt = closingPerLessAmt;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getClosingStockType() {
        return closingStockType;
    }

    public void setClosingStockType(String closingStockType) {
        this.closingStockType = closingStockType;
    }

    public Date getClosingDt() {
        return closingDt;
    }

    public void setClosingDt(Date closingDt) {
        this.closingDt = closingDt;
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

    public String getDepoCloseID() {
        return depoCloseID;
    }

    public void setDepoCloseID(String depoCloseID) {
        this.depoCloseID = depoCloseID;
    }
    
}
