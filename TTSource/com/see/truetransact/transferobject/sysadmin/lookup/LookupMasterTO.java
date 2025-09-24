/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LookupMasterTO.java
 * 
 * Created on Mon Apr 18 17:37:32 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.lookup;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOOKUP_MASTER.
 */
public class LookupMasterTO extends TransferObject implements Serializable {

    private String lookupId = "";
    private String lookupRefId = "";
    private String lookupDesc = "";
    private String status = "";
    private String editable = "";
    private String authorized = "";

    /**
     * Setter/Getter for LOOKUP_ID - table Field
     */
    public void setLookupId(String lookupId) {
        this.lookupId = lookupId;
    }

    public String getLookupId() {
        return lookupId;
    }

    /**
     * Setter/Getter for LOOKUP_REF_ID - table Field
     */
    public void setLookupRefId(String lookupRefId) {
        this.lookupRefId = lookupRefId;
    }

    public String getLookupRefId() {
        return lookupRefId;
    }

    /**
     * Setter/Getter for LOOKUP_DESC - table Field
     */
    public void setLookupDesc(String lookupDesc) {
        this.lookupDesc = lookupDesc;
    }

    public String getLookupDesc() {
        return lookupDesc;
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
     * Setter/Getter for EDITABLE - table Field
     */
    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getEditable() {
        return editable;
    }

    /**
     * Setter/Getter for AUTHORIZED - table Field
     */
    public void setAuthorized(String authorized) {
        this.authorized = authorized;
    }

    public String getAuthorized() {
        return authorized;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("lookupId");
        return lookupId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("lookupId", lookupId));
        strB.append(getTOString("lookupRefId", lookupRefId));
        strB.append(getTOString("lookupDesc", lookupDesc));
        strB.append(getTOString("status", status));
        strB.append(getTOString("editable", editable));
        strB.append(getTOString("authorized", authorized));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("lookupId", lookupId));
        strB.append(getTOXml("lookupRefId", lookupRefId));
        strB.append(getTOXml("lookupDesc", lookupDesc));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("editable", editable));
        strB.append(getTOXml("authorized", authorized));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}