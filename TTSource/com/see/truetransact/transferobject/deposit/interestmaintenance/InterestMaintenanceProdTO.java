/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestMaintenanceProdTO.java
 * 
 * Created on Tue May 25 10:47:22 IST 2004
 */
package com.see.truetransact.transferobject.deposit.interestmaintenance;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_ROI_GROUP_PROD.
 */
public class InterestMaintenanceProdTO extends TransferObject implements Serializable {

    private String roiGroupId = "";
    private String prodId = "";
    private String status = "";

    /**
     * Setter/Getter for ROI_GROUP_ID - table Field
     */
    public void setRoiGroupId(String roiGroupId) {
        this.roiGroupId = roiGroupId;
    }

    public String getRoiGroupId() {
        return roiGroupId;
    }

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("roiGroupId" + KEY_VAL_SEPARATOR + "prodId");
        return roiGroupId + KEY_VAL_SEPARATOR + prodId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("roiGroupId", roiGroupId));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("status", status));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("roiGroupId", roiGroupId));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}