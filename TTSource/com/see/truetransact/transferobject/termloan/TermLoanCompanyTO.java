/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanCompanyTO.java
 * @author shanmugavel
 * Created on Mon May 10 12:12:12 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_COMPANY.
 */
public class TermLoanCompanyTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String coRegNo = "";
    private Date establishDt = null;
    private Date dealBankSince = null;
    private Double netWorth = null;
    private Date netWorthOn = null;
    private String chiefExecName = "";
    private String addrType = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String countryCode = "";
    private String pincode = "";
    private String phone = "";
    private Double riskRate = null;
    private String businessNature = "";
    private String remarks = "";
    private Date crFacilitiesSince = null;
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String status = "";
    private String command = "";
    private String statusBy = "";
    private Date statusDt = null;

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
     * Setter/Getter for CO_REG_NO - table Field
     */
    public void setCoRegNo(String coRegNo) {
        this.coRegNo = coRegNo;
    }

    public String getCoRegNo() {
        return coRegNo;
    }

    /**
     * Setter/Getter for ESTABLISH_DT - table Field
     */
    public void setEstablishDt(Date establishDt) {
        this.establishDt = establishDt;
    }

    public Date getEstablishDt() {
        return establishDt;
    }

    /**
     * Setter/Getter for DEAL_BANK_SINCE - table Field
     */
    public void setDealBankSince(Date dealBankSince) {
        this.dealBankSince = dealBankSince;
    }

    public Date getDealBankSince() {
        return dealBankSince;
    }

    /**
     * Setter/Getter for NET_WORTH - table Field
     */
    public void setNetWorth(Double netWorth) {
        this.netWorth = netWorth;
    }

    public Double getNetWorth() {
        return netWorth;
    }

    /**
     * Setter/Getter for NET_WORTH_ON - table Field
     */
    public void setNetWorthOn(Date netWorthOn) {
        this.netWorthOn = netWorthOn;
    }

    public Date getNetWorthOn() {
        return netWorthOn;
    }

    /**
     * Setter/Getter for CHIEF_EXEC_NAME - table Field
     */
    public void setChiefExecName(String chiefExecName) {
        this.chiefExecName = chiefExecName;
    }

    public String getChiefExecName() {
        return chiefExecName;
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
     * Setter/Getter for RISK_RATE - table Field
     */
    public void setRiskRate(Double riskRate) {
        this.riskRate = riskRate;
    }

    public Double getRiskRate() {
        return riskRate;
    }

    /**
     * Setter/Getter for BUSINESS_NATURE - table Field
     */
    public void setBusinessNature(String businessNature) {
        this.businessNature = businessNature;
    }

    public String getBusinessNature() {
        return businessNature;
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
     * Setter/Getter for CR_FACILITIES_SINCE - table Field
     */
    public void setCrFacilitiesSince(Date crFacilitiesSince) {
        this.crFacilitiesSince = crFacilitiesSince;
    }

    public Date getCrFacilitiesSince() {
        return crFacilitiesSince;
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
        setKeyColumns("borrowNo");
        return borrowNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("coRegNo", coRegNo));
        strB.append(getTOString("establishDt", establishDt));
        strB.append(getTOString("dealBankSince", dealBankSince));
        strB.append(getTOString("netWorth", netWorth));
        strB.append(getTOString("netWorthOn", netWorthOn));
        strB.append(getTOString("chiefExecName", chiefExecName));
        strB.append(getTOString("addrType", addrType));
        strB.append(getTOString("street", street));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("countryCode", countryCode));
        strB.append(getTOString("pincode", pincode));
        strB.append(getTOString("phone", phone));
        strB.append(getTOString("riskRate", riskRate));
        strB.append(getTOString("businessNature", businessNature));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("crFacilitiesSince", crFacilitiesSince));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
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
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("coRegNo", coRegNo));
        strB.append(getTOXml("establishDt", establishDt));
        strB.append(getTOXml("dealBankSince", dealBankSince));
        strB.append(getTOXml("netWorth", netWorth));
        strB.append(getTOXml("netWorthOn", netWorthOn));
        strB.append(getTOXml("chiefExecName", chiefExecName));
        strB.append(getTOXml("addrType", addrType));
        strB.append(getTOXml("street", street));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("countryCode", countryCode));
        strB.append(getTOXml("pincode", pincode));
        strB.append(getTOXml("phone", phone));
        strB.append(getTOXml("riskRate", riskRate));
        strB.append(getTOXml("businessNature", businessNature));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("crFacilitiesSince", crFacilitiesSince));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}