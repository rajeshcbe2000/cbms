/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PoATO.java
 *
 * Created on Wed Aug 18 11:02:21 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_POA.
 */
public class PoATO extends TransferObject implements Serializable {

    private String depositNo = "";
    private String poaNo = "";
    private String poaHolderName = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String pinCode = "";
    private String countryCode = "";
    private String areaCode = "";
    private String phNo = "";
    private Date periodFrom = null;
    private Date periodTo = null;
    private String remarks = "";
    private String addrType = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String custId = "";
    private String onBehalfOf = "";

    /**
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
    }

    /**
     * Setter/Getter for POA_NO - table Field
     */
    public void setPoaNo(String poaNo) {
        this.poaNo = poaNo;
    }

    public String getPoaNo() {
        return poaNo;
    }

    /**
     * Setter/Getter for POA_HOLDER_NAME - table Field
     */
    public void setPoaHolderName(String poaHolderName) {
        this.poaHolderName = poaHolderName;
    }

    public String getPoaHolderName() {
        return poaHolderName;
    }

    /**
     * Setter/Getter for STREET - table Field
     */
    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    /**
     * Setter/Getter for AREA - table Field
     */
    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    /**
     * Setter/Getter for CITY - table Field
     */
    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    /**
     * Setter/Getter for STATE - table Field
     */
    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    /**
     * Setter/Getter for PIN_CODE - table Field
     */
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    /**
     * Setter/Getter for COUNTRY_CODE - table Field
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
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
     * Setter/Getter for PH_NO - table Field
     */
    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getPhNo() {
        return phNo;
    }

    /**
     * Setter/Getter for PERIOD_FROM - table Field
     */
    public void setPeriodFrom(Date periodFrom) {
        this.periodFrom = periodFrom;
    }

    public Date getPeriodFrom() {
        return periodFrom;
    }

    /**
     * Setter/Getter for PERIOD_TO - table Field
     */
    public void setPeriodTo(Date periodTo) {
        this.periodTo = periodTo;
    }

    public Date getPeriodTo() {
        return periodTo;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
    }

    /**
     * Setter/Getter for ON_BEHALF_OF - table Field
     */
    public void setOnBehalfOf(String onBehalfOf) {
        this.onBehalfOf = onBehalfOf;
    }

    public String getOnBehalfOf() {
        return onBehalfOf;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("depositNo" + KEY_VAL_SEPARATOR + "poaNo");
        return depositNo + KEY_VAL_SEPARATOR + poaNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("poaNo", poaNo));
        strB.append(getTOString("poaHolderName", poaHolderName));
        strB.append(getTOString("street", street));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("pinCode", pinCode));
        strB.append(getTOString("countryCode", countryCode));
        strB.append(getTOString("areaCode", areaCode));
        strB.append(getTOString("phNo", phNo));
        strB.append(getTOString("periodFrom", periodFrom));
        strB.append(getTOString("periodTo", periodTo));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("addrType", addrType));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("onBehalfOf", onBehalfOf));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("poaNo", poaNo));
        strB.append(getTOXml("poaHolderName", poaHolderName));
        strB.append(getTOXml("street", street));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("pinCode", pinCode));
        strB.append(getTOXml("countryCode", countryCode));
        strB.append(getTOXml("areaCode", areaCode));
        strB.append(getTOXml("phNo", phNo));
        strB.append(getTOXml("periodFrom", periodFrom));
        strB.append(getTOXml("periodTo", periodTo));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("addrType", addrType));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("onBehalfOf", onBehalfOf));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}