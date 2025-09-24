/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GroupMasterTO.java
 * 
 * Created on Thu Apr 29 10:00:50 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.sysadmin.role;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is GROUP_MASTER.
 */
public class GroupMasterTO extends TransferObject implements Serializable {

    private String groupId = "";
    private String groupName = "";
    private String status = "";

    /**
     * Setter/Getter for GROUP_ID - table Field
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    /**
     * Setter/Getter for GROUP_NAME - table Field
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
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
        setKeyColumns("groupId");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("groupId", groupId));
        strB.append(getTOString("groupName", groupName));
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
        strB.append(getTOXml("groupId", groupId));
        strB.append(getTOXml("groupName", groupName));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}