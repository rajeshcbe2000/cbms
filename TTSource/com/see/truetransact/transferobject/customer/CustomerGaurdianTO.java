/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerGaurdianTO.java
 * 
 * Created on Mon Mar 07 11:48:01 IST 2005
 */
package com.see.truetransact.transferobject.customer;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 * Table name for this TO is CUST_GUARDIAN.
 */
public class CustomerGaurdianTO extends TransferObject implements Serializable {

    private String custId = "";
    private String guardianName = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String pinCode = "";
    private String areaCode = "";
    private String phNo = "";
    private String relationship = "";
    private String countryCode = "";
    private Date guardianDob = null;
    private Integer guardianAge = 0;

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
     * Setter/Getter for GUARDIAN_NAME - table Field
     */
    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianName() {
        return guardianName;
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
     * Setter/Getter for RELATIONSHIP - table Field
     */
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationship() {
        return relationship;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(custId);
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("guardianName", guardianName));
        strB.append(getTOString("street", street));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("pinCode", pinCode));
        strB.append(getTOString("areaCode", areaCode));
        strB.append(getTOString("phNo", phNo));
        strB.append(getTOString("relationship", relationship));
        strB.append(getTOString("countryCode", countryCode));
        strB.append(getTOString("guardianDob", guardianDob));
        strB.append(getTOString("guardianAge", guardianAge));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("guardianName", guardianName));
        strB.append(getTOXml("street", street));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("pinCode", pinCode));
        strB.append(getTOXml("areaCode", areaCode));
        strB.append(getTOXml("phNo", phNo));
        strB.append(getTOXml("relationship", relationship));
        strB.append(getTOXml("countryCode", countryCode));
        strB.append(getTOXml("guardianDob", guardianDob));
        strB.append(getTOXml("guardianAge", guardianAge));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property guardianDob.
     *
     * @return Value of property guardianDob.
     */
    public java.util.Date getGuardianDob() {
        return guardianDob;
    }

    /**
     * Setter for property guardianDob.
     *
     * @param guardianDob New value of property guardianDob.
     */
    public void setGuardianDob(java.util.Date guardianDob) {
        this.guardianDob = guardianDob;
    }


    public Integer getGuardianAge() {
        return guardianAge;
    }

    public void setGuardianAge(Integer guardianAge) {
        this.guardianAge = guardianAge;
    }

    
}