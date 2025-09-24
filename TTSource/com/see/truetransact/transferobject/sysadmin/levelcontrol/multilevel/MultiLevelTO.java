/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MultiLevelTO.java
 * 
 * Created on Thu Sep 09 15:20:52 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.sysadmin.levelcontrol.multilevel;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LEVEL_MULTI_DETAILS.
 */
public class MultiLevelTO extends TransferObject implements Serializable {

    private String levelMultiId = "";
    private String levelOrder = "";
    private String levelId = "";
    private Double noOfPersons = null;
    private String levelCondition = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

    /**
     * Setter/Getter for LEVEL_MULTI_ID - table Field
     */
    public void setLevelMultiId(String levelMultiId) {
        this.levelMultiId = levelMultiId;
    }

    public String getLevelMultiId() {
        return levelMultiId;
    }

    /**
     * Setter/Getter for LEVEL_ORDER - table Field
     */
    public void setLevelOrder(String levelOrder) {
        this.levelOrder = levelOrder;
    }

    public String getLevelOrder() {
        return levelOrder;
    }

    /**
     * Setter/Getter for LEVEL_ID - table Field
     */
    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelId() {
        return levelId;
    }

    /**
     * Setter/Getter for NO_OF_PERSONS - table Field
     */
    public void setNoOfPersons(Double noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public Double getNoOfPersons() {
        return noOfPersons;
    }

    /**
     * Setter/Getter for LEVEL_CONDITION - table Field
     */
    public void setLevelCondition(String levelCondition) {
        this.levelCondition = levelCondition;
    }

    public String getLevelCondition() {
        return levelCondition;
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
        setKeyColumns("levelMultiId" + KEY_VAL_SEPARATOR + "levelOrder");
        return levelMultiId + KEY_VAL_SEPARATOR + levelOrder;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("levelMultiId", levelMultiId));
        strB.append(getTOString("levelOrder", levelOrder));
        strB.append(getTOString("levelId", levelId));
        strB.append(getTOString("noOfPersons", noOfPersons));
        strB.append(getTOString("levelCondition", levelCondition));
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
        strB.append(getTOXml("levelMultiId", levelMultiId));
        strB.append(getTOXml("levelOrder", levelOrder));
        strB.append(getTOXml("levelId", levelId));
        strB.append(getTOXml("noOfPersons", noOfPersons));
        strB.append(getTOXml("levelCondition", levelCondition));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}