/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GroupScreensTO.java
 * 
 * Created on Tue Jul 19 16:28:39 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.group;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is GROUP_SCREENS.
 */
public class GroupScreensTO extends TransferObject implements Serializable {

    private String groupId = "";
    private String screenId = "";
    private String status = "";
    private String newAllowed = "";
    private String editAllowed = "";
    private String deleteAllowed = "";
    private String authRejAllowed = "";
    private String exceptionAllowed = "";
    private String printAllowed = "";
    private String interbranchAllowed = "";

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
     * Setter/Getter for SCREEN_ID - table Field
     */
    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getScreenId() {
        return screenId;
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
     * Setter/Getter for NEW_ALLOWED - table Field
     */
    public void setNewAllowed(String newAllowed) {
        this.newAllowed = newAllowed;
    }

    public String getNewAllowed() {
        return newAllowed;
    }

    /**
     * Setter/Getter for EDIT_ALLOWED - table Field
     */
    public void setEditAllowed(String editAllowed) {
        this.editAllowed = editAllowed;
    }

    public String getEditAllowed() {
        return editAllowed;
    }

    /**
     * Setter/Getter for DELETE_ALLOWED - table Field
     */
    public void setDeleteAllowed(String deleteAllowed) {
        this.deleteAllowed = deleteAllowed;
    }

    public String getDeleteAllowed() {
        return deleteAllowed;
    }

    /**
     * Setter/Getter for AUTH_REJ_ALLOWED - table Field
     */
    public void setAuthRejAllowed(String authRejAllowed) {
        this.authRejAllowed = authRejAllowed;
    }

    public String getAuthRejAllowed() {
        return authRejAllowed;
    }

    /**
     * Setter/Getter for EXCEPTION_ALLOWED - table Field
     */
    public void setExceptionAllowed(String exceptionAllowed) {
        this.exceptionAllowed = exceptionAllowed;
    }

    public String getExceptionAllowed() {
        return exceptionAllowed;
    }

    /**
     * Setter/Getter for PRINT_ALLOWED - table Field
     */
    public void setPrintAllowed(String printAllowed) {
        this.printAllowed = printAllowed;
    }

    public String getPrintAllowed() {
        return printAllowed;
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
        strB.append(getTOString("groupId", groupId));
        strB.append(getTOString("screenId", screenId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("newAllowed", newAllowed));
        strB.append(getTOString("editAllowed", editAllowed));
        strB.append(getTOString("deleteAllowed", deleteAllowed));
        strB.append(getTOString("authRejAllowed", authRejAllowed));
        strB.append(getTOString("exceptionAllowed", exceptionAllowed));
        strB.append(getTOString("printAllowed", printAllowed));
        strB.append(getTOString("interbranchAllowed", interbranchAllowed));
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
        strB.append(getTOXml("screenId", screenId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("newAllowed", newAllowed));
        strB.append(getTOXml("editAllowed", editAllowed));
        strB.append(getTOXml("deleteAllowed", deleteAllowed));
        strB.append(getTOXml("authRejAllowed", authRejAllowed));
        strB.append(getTOXml("exceptionAllowed", exceptionAllowed));
        strB.append(getTOXml("printAllowed", printAllowed));
        strB.append(getTOXml("interbranchAllowed", interbranchAllowed));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property interbranchAllowed.
     *
     * @return Value of property interbranchAllowed.
     */
    public java.lang.String getInterbranchAllowed() {
        return interbranchAllowed;
    }

    /**
     * Setter for property interbranchAllowed.
     *
     * @param interbranchAllowed New value of property interbranchAllowed.
     */
    public void setInterbranchAllowed(java.lang.String interbranchAllowed) {
        this.interbranchAllowed = interbranchAllowed;
    }
}