/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingSubGroupTO.java
 *
 * @author Revathi L
 */
package com.see.truetransact.transferobject.trading.tradinggroup;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TDS_CONFIG.
 */
public class TradingSubGroupTO extends TransferObject implements Serializable {

    private String groupID = "";
    private String subGroupID = "";
    private String subGroupName = "";
    private String status = "";
    private String slNo = "";
    private String authorize_Status = null;
    private Date cr_Dt = null;
    private String active = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getSubGroupID() {
        return subGroupID;
    }

    public void setSubGroupID(String subGroupID) {
        this.subGroupID = subGroupID;
    }

    public String getSubGroupName() {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName) {
        this.subGroupName = subGroupName;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getAuthorize_Status() {
        return authorize_Status;
    }

    public void setAuthorize_Status(String authorize_Status) {
        this.authorize_Status = authorize_Status;
    }

    public Date getCr_Dt() {
        return cr_Dt;
    }

    public void setCr_Dt(Date cr_Dt) {
        this.cr_Dt = cr_Dt;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
//		setKeyColumns(txtStoreID);
        return groupID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("groupID", groupID));
        strB.append(getTOString("subGroupID", subGroupID));
        strB.append(getTOString("subGroupName", subGroupName));
        strB.append(getTOString("status", status));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("authorize_Status", authorize_Status));
        strB.append(getTOString("cr_Dt", cr_Dt));
        strB.append(getTOString("active", active));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("authorize_Status", authorize_Status));
        strB.append(getTOXml("cr_Dt", cr_Dt));
        strB.append(getTOXml("active", active));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}