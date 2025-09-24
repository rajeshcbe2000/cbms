/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ScreenMasterTO.java
 * 
 * Created on Wed Apr 28 16:06:58 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.sysadmin.group;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SCREEN_MASTER.
 */
public class ScreenMasterTO extends TransferObject implements Serializable {

    private String screenId = "";
    private String menuId = "";
    private String screenName = "";
    private String appId = "";
    private String wfStatus = "";
    private String moduleId = "";
    private String screenClass = "";
    private Double slNo = null;
    private String status = "";

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
     * Setter/Getter for MENU_ID - table Field
     */
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuId() {
        return menuId;
    }

    /**
     * Setter/Getter for SCREEN_NAME - table Field
     */
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getScreenName() {
        return screenName;
    }

    /**
     * Setter/Getter for APP_ID - table Field
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    /**
     * Setter/Getter for WF_STATUS - table Field
     */
    public void setWfStatus(String wfStatus) {
        this.wfStatus = wfStatus;
    }

    public String getWfStatus() {
        return wfStatus;
    }

    /**
     * Setter/Getter for MODULE_ID - table Field
     */
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleId() {
        return moduleId;
    }

    /**
     * Setter/Getter for SCREEN_CLASS - table Field
     */
    public void setScreenClass(String screenClass) {
        this.screenClass = screenClass;
    }

    public String getScreenClass() {
        return screenClass;
    }

    /**
     * Setter/Getter for SL_NO - table Field
     */
    public void setSlNo(Double slNo) {
        this.slNo = slNo;
    }

    public Double getSlNo() {
        return slNo;
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
        setKeyColumns("screenId");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("screenId", screenId));
        strB.append(getTOString("menuId", menuId));
        strB.append(getTOString("screenName", screenName));
        strB.append(getTOString("appId", appId));
        strB.append(getTOString("wfStatus", wfStatus));
        strB.append(getTOString("moduleId", moduleId));
        strB.append(getTOString("screenClass", screenClass));
        strB.append(getTOString("slNo", slNo));
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
        strB.append(getTOXml("screenId", screenId));
        strB.append(getTOXml("menuId", menuId));
        strB.append(getTOXml("screenName", screenName));
        strB.append(getTOXml("appId", appId));
        strB.append(getTOXml("wfStatus", wfStatus));
        strB.append(getTOXml("moduleId", moduleId));
        strB.append(getTOXml("screenClass", screenClass));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}