/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerPhoneTO.java
 *
 * Created on Wed Feb 16 09:38:12 IST 2005
 */
package com.see.truetransact.transferobject.employee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUST_PHONE.
 */
public class EmployeeRelativeDirectorTO extends TransferObject implements Serializable {

    private String sysId = "";
    private String status = "";
    private String statusBy = "";
    private String directorTittle = "";
    private String directorFirstName = "";
    private String directorMiddleName = "";
    private String directorLastName = "";
    private String directorRelationShip = "";
    private Date statusDt = null;
    private String directorID = "";

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
        setKeyColumns(sysId);
        return sysId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));

        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("directorRelationShip", directorRelationShip));
        strB.append(getTOString("directorTittle", directorTittle));
        strB.append(getTOString("directorFirstName", directorFirstName));
        strB.append(getTOString("directorMiddleName", directorMiddleName));
        strB.append(getTOString("directorLastName", directorLastName));
        strB.append(getTOString("sysId", sysId));
        strB.append(getTOString("directorID", directorID));
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
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("directorRelationShip", directorRelationShip));
        strB.append(getTOXml("directorTittle", directorTittle));
        strB.append(getTOXml("directorFirstName", directorFirstName));
        strB.append(getTOXml("directorMiddleName", directorMiddleName));
        strB.append(getTOXml("directorLastName", directorLastName));
        strB.append(getTOXml("sysId", sysId));
        strB.append(getTOXml("directorID", directorID));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property sysId.
     *
     * @return Value of property sysId.
     */
    public java.lang.String getSysId() {
        return sysId;
    }

    /**
     * Setter for property sysId.
     *
     * @param sysId New value of property sysId.
     */
    public void setSysId(java.lang.String sysId) {
        this.sysId = sysId;
    }

    /**
     * Getter for property directorTittle.
     *
     * @return Value of property directorTittle.
     */
    public java.lang.String getDirectorTittle() {
        return directorTittle;
    }

    /**
     * Setter for property directorTittle.
     *
     * @param directorTittle New value of property directorTittle.
     */
    public void setDirectorTittle(java.lang.String directorTittle) {
        this.directorTittle = directorTittle;
    }

    /**
     * Getter for property directorFirstName.
     *
     * @return Value of property directorFirstName.
     */
    public java.lang.String getDirectorFirstName() {
        return directorFirstName;
    }

    /**
     * Setter for property directorFirstName.
     *
     * @param directorFirstName New value of property directorFirstName.
     */
    public void setDirectorFirstName(java.lang.String directorFirstName) {
        this.directorFirstName = directorFirstName;
    }

    /**
     * Getter for property directorMiddleName.
     *
     * @return Value of property directorMiddleName.
     */
    public java.lang.String getDirectorMiddleName() {
        return directorMiddleName;
    }

    /**
     * Setter for property directorMiddleName.
     *
     * @param directorMiddleName New value of property directorMiddleName.
     */
    public void setDirectorMiddleName(java.lang.String directorMiddleName) {
        this.directorMiddleName = directorMiddleName;
    }

    /**
     * Getter for property directorLastName.
     *
     * @return Value of property directorLastName.
     */
    public java.lang.String getDirectorLastName() {
        return directorLastName;
    }

    /**
     * Setter for property directorLastName.
     *
     * @param directorLastName New value of property directorLastName.
     */
    public void setDirectorLastName(java.lang.String directorLastName) {
        this.directorLastName = directorLastName;
    }

    /**
     * Getter for property directorRelationShip.
     *
     * @return Value of property directorRelationShip.
     */
    public java.lang.String getDirectorRelationShip() {
        return directorRelationShip;
    }

    /**
     * Setter for property directorRelationShip.
     *
     * @param directorRelationShip New value of property directorRelationShip.
     */
    public void setDirectorRelationShip(java.lang.String directorRelationShip) {
        this.directorRelationShip = directorRelationShip;
    }

    /**
     * Getter for property directorID.
     *
     * @return Value of property directorID.
     */
    public java.lang.String getDirectorID() {
        return directorID;
    }

    /**
     * Setter for property directorID.
     *
     * @param directorID New value of property directorID.
     */
    public void setDirectorID(java.lang.String directorID) {
        this.directorID = directorID;
    }
}