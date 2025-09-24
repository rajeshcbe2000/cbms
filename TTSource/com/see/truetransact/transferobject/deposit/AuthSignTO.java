/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AuthSignTO.java
 *
 * Created on Wed Jul 21 18:19:18 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_AUTHORIZED.
 */
public class AuthSignTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private String authorizedNo = "";
    private String name = "";
    private String commAddr = "";
    private String designation = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String pinCode = "";
    private String countryCode = "";
    private String areaCode = "";
    private String homePhone = "";
    private String homeFax = "";
    private String pager = "";
    private String emailid = "";
    private String businessPhone = "";
    private String businessFax = "";
    private String mobile = "";
    private Double limits = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

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
     * Setter/Getter for AUTHORIZED_NO - table Field
     */
    public void setAuthorizedNo(String authorizedNo) {
        this.authorizedNo = authorizedNo;
    }

    public String getAuthorizedNo() {
        return authorizedNo;
    }

    /**
     * Setter/Getter for NAME - table Field
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Setter/Getter for COMM_ADDR - table Field
     */
    public void setCommAddr(String commAddr) {
        this.commAddr = commAddr;
    }

    public String getCommAddr() {
        return commAddr;
    }

    /**
     * Setter/Getter for DESIGNATION - table Field
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignation() {
        return designation;
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
     * Setter/Getter for HOME_PHONE - table Field
     */
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    /**
     * Setter/Getter for HOME_FAX - table Field
     */
    public void setHomeFax(String homeFax) {
        this.homeFax = homeFax;
    }

    public String getHomeFax() {
        return homeFax;
    }

    /**
     * Setter/Getter for PAGER - table Field
     */
    public void setPager(String pager) {
        this.pager = pager;
    }

    public String getPager() {
        return pager;
    }

    /**
     * Setter/Getter for EMAILID - table Field
     */
    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getEmailid() {
        return emailid;
    }

    /**
     * Setter/Getter for BUSINESS_PHONE - table Field
     */
    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    /**
     * Setter/Getter for BUSINESS_FAX - table Field
     */
    public void setBusinessFax(String businessFax) {
        this.businessFax = businessFax;
    }

    public String getBusinessFax() {
        return businessFax;
    }

    /**
     * Setter/Getter for MOBILE - table Field
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    /**
     * Setter/Getter for LIMITS - table Field
     */
    public void setLimits(Double limits) {
        this.limits = limits;
    }

    public Double getLimits() {
        return limits;
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
        setKeyColumns("depositNo" + KEY_VAL_SEPARATOR + "authorizedNo");
        return depositNo + KEY_VAL_SEPARATOR + authorizedNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("authorizedNo", authorizedNo));
        strB.append(getTOString("name", name));
        strB.append(getTOString("commAddr", commAddr));
        strB.append(getTOString("designation", designation));
        strB.append(getTOString("street", street));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("pinCode", pinCode));
        strB.append(getTOString("countryCode", countryCode));
        strB.append(getTOString("areaCode", areaCode));
        strB.append(getTOString("homePhone", homePhone));
        strB.append(getTOString("homeFax", homeFax));
        strB.append(getTOString("pager", pager));
        strB.append(getTOString("emailid", emailid));
        strB.append(getTOString("businessPhone", businessPhone));
        strB.append(getTOString("businessFax", businessFax));
        strB.append(getTOString("mobile", mobile));
        strB.append(getTOString("limits", limits));
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
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("authorizedNo", authorizedNo));
        strB.append(getTOXml("name", name));
        strB.append(getTOXml("commAddr", commAddr));
        strB.append(getTOXml("designation", designation));
        strB.append(getTOXml("street", street));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("pinCode", pinCode));
        strB.append(getTOXml("countryCode", countryCode));
        strB.append(getTOXml("areaCode", areaCode));
        strB.append(getTOXml("homePhone", homePhone));
        strB.append(getTOXml("homeFax", homeFax));
        strB.append(getTOXml("pager", pager));
        strB.append(getTOXml("emailid", emailid));
        strB.append(getTOXml("businessPhone", businessPhone));
        strB.append(getTOXml("businessFax", businessFax));
        strB.append(getTOXml("mobile", mobile));
        strB.append(getTOXml("limits", limits));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}