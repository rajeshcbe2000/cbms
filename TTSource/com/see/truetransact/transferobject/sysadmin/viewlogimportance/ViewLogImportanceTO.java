/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewLogImportanceTO.java
 *
 * Created on January 7, 2005, 6:28 PM
 */
package com.see.truetransact.transferobject.sysadmin.viewlogimportance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOG_IMPORTANCE.
 */
/**
 *
 * @author 152713
 */
public class ViewLogImportanceTO extends TransferObject implements Serializable {

    private String impId = "";
    private String module = "";
    private String screen = "";
    private String activity = "";
    private String importance = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

    /**
     * Setter/Getter for IMP_ID - table Field
     */
    public void setImpId(String impId) {
        this.impId = impId;
    }

    public String getImpId() {
        return impId;
    }

    /**
     * Setter/Getter for MODULE - table Field
     */
    public void setModule(String module) {
        this.module = module;
    }

    public String getModule() {
        return module;
    }

    /**
     * Setter/Getter for SCREEN - table Field
     */
    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getScreen() {
        return screen;
    }

    /**
     * Setter/Getter for ACTIVITY - table Field
     */
    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getActivity() {
        return activity;
    }

    /**
     * Setter/Getter for IMPORTANCE - table Field
     */
    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getImportance() {
        return importance;
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
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("impId");
        return impId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("impId", impId));
        strB.append(getTOString("module", module));
        strB.append(getTOString("screen", screen));
        strB.append(getTOString("activity", activity));
        strB.append(getTOString("importance", importance));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("impId", impId));
        strB.append(getTOXml("module", module));
        strB.append(getTOXml("screen", screen));
        strB.append(getTOXml("activity", activity));
        strB.append(getTOXml("importance", importance));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}