/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGLTO.java
 * 
 * Created on Wed Jan 12 17:49:02 IST 2005
 */
package com.see.truetransact.transferobject.generalledger.branchgl;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BRANCH_GL.
 */
public class BranchGLTO extends TransferObject implements Serializable {

    private String acHdId = "";
    private String status = "";
    private String groupId = "";

    /**
     * Setter/Getter for AC_HD_ID - table Field
     */
    public void setAcHdId(String acHdId) {
        this.acHdId = acHdId;
    }

    public String getAcHdId() {
        return acHdId;
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
     * Setter/Getter for GROUP_ID - table Field
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

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
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("groupId", groupId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("groupId", groupId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}