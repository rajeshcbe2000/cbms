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
public class EmployeeMasterPhoneTO extends TransferObject implements Serializable {

    private String sysId = "";
    private String phoneTypeId = "";
    private String areaCode = "";
    private String phoneNumber = "";
    private String addrType = "";
    private Double phoneId = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

    /**
     * Setter/Getter for PHONE_TYPE_ID - table Field
     */
    public void setPhoneTypeId(String phoneTypeId) {
        this.phoneTypeId = phoneTypeId;
    }

    public String getPhoneTypeId() {
        return phoneTypeId;
    }

    /**
     * Setter/Getter for AREA_CODE - table Field
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    /**
     * Setter/Getter for PHONE_NUMBER - table Field
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Setter/Getter for ADDR_TYPE - table Field
     */
    public void setAddrType(String addrType) {
        this.addrType = addrType;
    }

    public String getAddrType() {
        return addrType;
    }

    /**
     * Setter/Getter for PHONE_ID - table Field
     */
    public void setPhoneId(Double phoneId) {
        this.phoneId = phoneId;
    }

    public Double getPhoneId() {
        return phoneId;
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
        setKeyColumns(sysId);
        return sysId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("sysId", sysId));
        strB.append(getTOString("phoneTypeId", phoneTypeId));
        strB.append(getTOString("areaCode", areaCode));
        strB.append(getTOString("phoneNumber", phoneNumber));
        strB.append(getTOString("addrType", addrType));
        strB.append(getTOString("phoneId", phoneId));
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
        strB.append(getTOXml("sysId", sysId));
        strB.append(getTOXml("phoneTypeId", phoneTypeId));
        strB.append(getTOXml("areaCode", areaCode));
        strB.append(getTOXml("phoneNumber", phoneNumber));
        strB.append(getTOXml("addrType", addrType));
        strB.append(getTOXml("phoneId", phoneId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
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
}