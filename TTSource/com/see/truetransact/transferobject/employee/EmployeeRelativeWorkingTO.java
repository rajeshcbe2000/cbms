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
public class EmployeeRelativeWorkingTO extends TransferObject implements Serializable {

    private String staffId = "";
    private String relativeSysId = "";
    private String sysId = "";
    private String status = "";
    private String statusBy = "";
    private String relativeTittle = "";
    private String relativeFirstName = "";
    private String relativeMiddleName = "";
    private String relativeLastName = "";
    private String relativerelationShip = "";
    private String relativeDisgnantion = "";
    private String relativeWorkingBranch = "";
    private Date statusDt = null;

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
        strB.append(getTOString("staffId", staffId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("relativerelationShip", relativerelationShip));
        strB.append(getTOString("relativeSysId", relativeSysId));
        strB.append(getTOString("relativeTittle", relativeTittle));
        strB.append(getTOString("relativeFirstName", relativeFirstName));
        strB.append(getTOString("relativeMiddleName", relativeMiddleName));
        strB.append(getTOString("relativeLastName", relativeLastName));
        strB.append(getTOString("relativeDisgnantion", relativeDisgnantion));
        strB.append(getTOString("sysId", sysId));




        strB.append(getTOString("sysId", sysId));

        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("staffId", staffId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("relativerelationShip", relativerelationShip));
        strB.append(getTOXml("relativeSysId", relativeSysId));
        strB.append(getTOXml("relativeTittle", relativeTittle));
        strB.append(getTOXml("relativeFirstName", relativeFirstName));
        strB.append(getTOXml("relativeMiddleName", relativeMiddleName));
        strB.append(getTOXml("relativeLastName", relativeLastName));
        strB.append(getTOXml("relativeDisgnantion", relativeDisgnantion));
        strB.append(getTOXml("sysId", sysId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property staffId.
     *
     * @return Value of property staffId.
     */
    public java.lang.String getStaffId() {
        return staffId;
    }

    /**
     * Setter for property staffId.
     *
     * @param staffId New value of property staffId.
     */
    public void setStaffId(java.lang.String staffId) {
        this.staffId = staffId;
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
     * Getter for property relativerelationShip.
     *
     * @return Value of property relativerelationShip.
     */
    public java.lang.String getRelativerelationShip() {
        return relativerelationShip;
    }

    /**
     * Setter for property relativerelationShip.
     *
     * @param relativerelationShip New value of property relativerelationShip.
     */
    public void setRelativerelationShip(java.lang.String relativerelationShip) {
        this.relativerelationShip = relativerelationShip;
    }

    /**
     * Getter for property relativeTittle.
     *
     * @return Value of property relativeTittle.
     */
    public java.lang.String getRelativeTittle() {
        return relativeTittle;
    }

    /**
     * Setter for property relativeTittle.
     *
     * @param relativeTittle New value of property relativeTittle.
     */
    public void setRelativeTittle(java.lang.String relativeTittle) {
        this.relativeTittle = relativeTittle;
    }

    /**
     * Getter for property relativeFirstName.
     *
     * @return Value of property relativeFirstName.
     */
    public java.lang.String getRelativeFirstName() {
        return relativeFirstName;
    }

    /**
     * Setter for property relativeFirstName.
     *
     * @param relativeFirstName New value of property relativeFirstName.
     */
    public void setRelativeFirstName(java.lang.String relativeFirstName) {
        this.relativeFirstName = relativeFirstName;
    }

    /**
     * Getter for property relativeMiddleName.
     *
     * @return Value of property relativeMiddleName.
     */
    public java.lang.String getRelativeMiddleName() {
        return relativeMiddleName;
    }

    /**
     * Setter for property relativeMiddleName.
     *
     * @param relativeMiddleName New value of property relativeMiddleName.
     */
    public void setRelativeMiddleName(java.lang.String relativeMiddleName) {
        this.relativeMiddleName = relativeMiddleName;
    }

    /**
     * Getter for property relativeLastName.
     *
     * @return Value of property relativeLastName.
     */
    public java.lang.String getRelativeLastName() {
        return relativeLastName;
    }

    /**
     * Setter for property relativeLastName.
     *
     * @param relativeLastName New value of property relativeLastName.
     */
    public void setRelativeLastName(java.lang.String relativeLastName) {
        this.relativeLastName = relativeLastName;
    }

    /**
     * Getter for property relativeWorkingBranch.
     *
     * @return Value of property relativeWorkingBranch.
     */
    public java.lang.String getRelativeWorkingBranch() {
        return relativeWorkingBranch;
    }

    /**
     * Setter for property relativeWorkingBranch.
     *
     * @param relativeWorkingBranch New value of property relativeWorkingBranch.
     */
    public void setRelativeWorkingBranch(java.lang.String relativeWorkingBranch) {
        this.relativeWorkingBranch = relativeWorkingBranch;
    }

    /**
     * Getter for property relativeSysId.
     *
     * @return Value of property relativeSysId.
     */
    public java.lang.String getRelativeSysId() {
        return relativeSysId;
    }

    /**
     * Setter for property relativeSysId.
     *
     * @param relativeSysId New value of property relativeSysId.
     */
    public void setRelativeSysId(java.lang.String relativeSysId) {
        this.relativeSysId = relativeSysId;
    }

    /**
     * Getter for property relativeDisgnantion.
     *
     * @return Value of property relativeDisgnantion.
     */
    public java.lang.String getRelativeDisgnantion() {
        return relativeDisgnantion;
    }

    /**
     * Setter for property relativeDisgnantion.
     *
     * @param relativeDisgnantion New value of property relativeDisgnantion.
     */
    public void setRelativeDisgnantion(java.lang.String relativeDisgnantion) {
        this.relativeDisgnantion = relativeDisgnantion;
    }
}