/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanPowerAttorneyTO.java
 * @author shanmugavel
 * Created on Mon May 10 12:31:59 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan.agritermloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_POA.
 */
public class AgriTermLoanPowerAttorneyTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private Double poaNo = null;
    private String poaHolderName = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String countryCode = "";
    private String pincode = "";
    private String phone = "";
    private Date periodFrom = null;
    private Date periodTo = null;
    private String remarks = "";
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String status = "";
    private String command = "";
    private String toWhom = "";
    private String addrType = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String custId = "";

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    /**
     * Setter/Getter for BORROW_NO - table Field
     */
    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    /**
     * Setter/Getter for POA_NO - table Field
     */
    public void setPoaNo(Double poaNo) {
        this.poaNo = poaNo;
    }

    public Double getPoaNo() {
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
     * Setter/Getter for COUNTRY_CODE - table Field
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Setter/Getter for PINCODE - table Field
     */
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPincode() {
        return pincode;
    }

    /**
     * Setter/Getter for PHONE - table Field
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
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
     * Setter/Getter for AUTHORIZE_REMARKS - table Field
     */
    public void setAuthorizeRemarks(String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    public String getAuthorizeRemarks() {
        return authorizeRemarks;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
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
     * Setter/Getter for TO_WHOM - table Field
     */
    public void setToWhom(String toWhom) {
        this.toWhom = toWhom;
    }

    public String getToWhom() {
        return toWhom;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("borrowNo" + KEY_VAL_SEPARATOR + "poaNo");
        return borrowNo + KEY_VAL_SEPARATOR + poaNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("poaNo", poaNo));
        strB.append(getTOString("poaHolderName", poaHolderName));
        strB.append(getTOString("street", street));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("countryCode", countryCode));
        strB.append(getTOString("pincode", pincode));
        strB.append(getTOString("phone", phone));
        strB.append(getTOString("periodFrom", periodFrom));
        strB.append(getTOString("periodTo", periodTo));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("toWhom", toWhom));
        strB.append(getTOString("addrType", addrType));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("custId", custId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("poaNo", poaNo));
        strB.append(getTOXml("poaHolderName", poaHolderName));
        strB.append(getTOXml("street", street));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("countryCode", countryCode));
        strB.append(getTOXml("pincode", pincode));
        strB.append(getTOXml("phone", phone));
        strB.append(getTOXml("periodFrom", periodFrom));
        strB.append(getTOXml("periodTo", periodTo));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("toWhom", toWhom));
        strB.append(getTOXml("addrType", addrType));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}