/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanAuthorizedSignatoryTO.java
 * @author shanmugavel
 * Created on Mon May 10 11:22:43 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan.agritermloan;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_AUTHORIZE.
 */
public class AgriTermLoanAuthorizedSignatoryTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String authrizeNo = "";
    private String custId = "";
    private String custName = "";
    private String commAddr = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String countryCode = "";
    private String pincode = "";
    private Double limits = null;
    private String designation = "";
    private String homePhone = "";
    private String homeFax = "";
    private String pager = "";
    private String emailId = "";
    private String businessPhone = "";
    private String businessFax = "";
    private String mobile = "";
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String status = "";
    private String slNo = "";
    private String command = "";

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
     * Setter/Getter for AUTHRIZE_NO - table Field
     */
    public void setAuthrizeNo(String authrizeNo) {
        this.authrizeNo = authrizeNo;
    }

    public String getAuthrizeNo() {
        return authrizeNo;
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
     * Setter/Getter for CUST_NAME - table Field
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustName() {
        return custName;
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
     * Setter/Getter for LIMITS - table Field
     */
    public void setLimits(Double limits) {
        this.limits = limits;
    }

    public Double getLimits() {
        return limits;
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
     * Setter/Getter for EMAIL_ID - table Field
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
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
     * Setter/Getter for SLNO - table Field
     */
    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getSlNo() {
        return slNo;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("borrowNo" + KEY_VAL_SEPARATOR + "slNo");
        return borrowNo + KEY_VAL_SEPARATOR + slNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("authrizeNo", authrizeNo));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("custName", custName));
        strB.append(getTOString("commAddr", commAddr));
        strB.append(getTOString("street", street));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("countryCode", countryCode));
        strB.append(getTOString("pincode", pincode));
        strB.append(getTOString("limits", limits));
        strB.append(getTOString("designation", designation));
        strB.append(getTOString("homePhone", homePhone));
        strB.append(getTOString("homeFax", homeFax));
        strB.append(getTOString("pager", pager));
        strB.append(getTOString("emailId", emailId));
        strB.append(getTOString("businessPhone", businessPhone));
        strB.append(getTOString("businessFax", businessFax));
        strB.append(getTOString("mobile", mobile));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("slNo", slNo));
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
        strB.append(getTOXml("authrizeNo", authrizeNo));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("custName", custName));
        strB.append(getTOXml("commAddr", commAddr));
        strB.append(getTOXml("street", street));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("countryCode", countryCode));
        strB.append(getTOXml("pincode", pincode));
        strB.append(getTOXml("limits", limits));
        strB.append(getTOXml("designation", designation));
        strB.append(getTOXml("homePhone", homePhone));
        strB.append(getTOXml("homeFax", homeFax));
        strB.append(getTOXml("pager", pager));
        strB.append(getTOXml("emailId", emailId));
        strB.append(getTOXml("businessPhone", businessPhone));
        strB.append(getTOXml("businessFax", businessFax));
        strB.append(getTOXml("mobile", mobile));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}