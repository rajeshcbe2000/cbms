/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanGuarantorTO.java
 * @author shanmugavel
 * Created on Tue May 04 15:43:59 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_GUARANTOR_DETAILS.
 */
public class TermLoanGuarantorTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String prodId = "";
    private String custId = "";
    private String guarantorAcNo = "";
    private String name = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String countryCode = "";
    private String pincode = "";
    private String phone = "";
    private String constitution = "";
    private String guarntConstitution = "";
    private Double guarantorNetWorth = null;
    private Date guarantorNetworthOn = null;
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String acctNum = "";
    private String status = "";
    private Double slno = null;
    private String command = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String guarantorProdType = "";
    private String guarantorProdId = "";
    private Date dob = null;

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
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
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
     * Setter/Getter for GUARANTOR_AC_NO - table Field
     */
    public void setGuarantorAcNo(String guarantorAcNo) {
        this.guarantorAcNo = guarantorAcNo;
    }

    public String getGuarantorAcNo() {
        return guarantorAcNo;
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
     * Setter/Getter for CONSTITUTION - table Field
     */
    public void setConstitution(String constitution) {
        this.constitution = constitution;
    }

    public String getConstitution() {
        return constitution;
    }

    /**
     * Setter/Getter for GUARANTOR_NET_WORTH - table Field
     */
    public void setGuarantorNetWorth(Double guarantorNetWorth) {
        this.guarantorNetWorth = guarantorNetWorth;
    }

    public Double getGuarantorNetWorth() {
        return guarantorNetWorth;
    }

    /**
     * Setter/Getter for GUARANTOR_NETWORTH_ON - table Field
     */
    public void setGuarantorNetworthOn(Date guarantorNetworthOn) {
        this.guarantorNetworthOn = guarantorNetworthOn;
    }

    public Date getGuarantorNetworthOn() {
        return guarantorNetworthOn;
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
     * Setter/Getter for ACCT_NUM - table Field
     */
    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctNum() {
        return acctNum;
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
    public void setSlno(Double slno) {
        this.slno = slno;
    }

    public Double getSlno() {
        return slno;
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
     * Setter/Getter for GUARANTOR_PROD_TYPE - table Field
     */
    public void setGuarantorProdType(String guarantorProdType) {
        this.guarantorProdType = guarantorProdType;
    }

    public String getGuarantorProdType() {
        return guarantorProdType;
    }

    /**
     * Setter/Getter for GUARANTOR_PROD_ID - table Field
     */
    public void setGuarantorProdId(String guarantorProdId) {
        this.guarantorProdId = guarantorProdId;
    }

    public String getGuarantorProdId() {
        return guarantorProdId;
    }

    /**
     * Setter/Getter for DOB - table Field
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDob() {
        return dob;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNum" + KEY_VAL_SEPARATOR + "slno");
        return acctNum + KEY_VAL_SEPARATOR + slno;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("guarantorAcNo", guarantorAcNo));
        strB.append(getTOString("name", name));
        strB.append(getTOString("street", street));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("countryCode", countryCode));
        strB.append(getTOString("pincode", pincode));
        strB.append(getTOString("phone", phone));
        strB.append(getTOString("constitution", constitution));
        strB.append(getTOString("guarntConstitution", guarntConstitution));
        strB.append(getTOString("guarantorNetWorth", guarantorNetWorth));
        strB.append(getTOString("guarantorNetworthOn", guarantorNetworthOn));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("status", status));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("guarantorProdType", guarantorProdType));
        strB.append(getTOString("guarantorProdId", guarantorProdId));
        strB.append(getTOString("dob", dob));
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
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("guarantorAcNo", guarantorAcNo));
        strB.append(getTOXml("name", name));
        strB.append(getTOXml("street", street));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("countryCode", countryCode));
        strB.append(getTOXml("pincode", pincode));
        strB.append(getTOXml("phone", phone));
        strB.append(getTOXml("constitution", constitution));
        strB.append(getTOXml("guarntConstitution", guarntConstitution));
        strB.append(getTOXml("guarantorNetWorth", guarantorNetWorth));
        strB.append(getTOXml("guarantorNetworthOn", guarantorNetworthOn));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("guarantorProdType", guarantorProdType));
        strB.append(getTOXml("guarantorProdId", guarantorProdId));
        strB.append(getTOXml("dob", dob));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property guarntConstitution.
     *
     * @return Value of property guarntConstitution.
     */
    public java.lang.String getGuarntConstitution() {
        return guarntConstitution;
    }

    /**
     * Setter for property guarntConstitution.
     *
     * @param guarntConstitution New value of property guarntConstitution.
     */
    public void setGuarntConstitution(java.lang.String guarntConstitution) {
        this.guarntConstitution = guarntConstitution;
    }
}