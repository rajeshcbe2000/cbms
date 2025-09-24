/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NomineeTO.java
 * 
 * Created on Fri Feb 25 14:39:11 IST 2005
 */
package com.see.truetransact.transferobject.common.nominee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ACT_NOMINEE_DETAIL.
 */
public class NomineeTO extends TransferObject implements Serializable {

    private String actNum = "";
    private String nomineeId = "";
    private String nomineeName = "";
    private String relationship = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String pinCode = "";
    private String areaCode = "";
    private String phNo = "";
    private Double sharePer = null;
    private String nomineeStatus = "";
    private Date nomineeDob = null;
    private String guardianName = "";
    private String gStreet = "";
    private String gArea = "";
    private String gCity = "";
    private String gState = "";
    private String gPinCode = "";
    private String gAreaCode = "";
    private String gPhNo = "";
    private String gRelationship = "";
    private String countryCode = "";
    private String gCountryCode = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String custId = "";
    private String nomineeCurrStatus = "";

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for NOMINEE_ID - table Field
     */
    public void setNomineeId(String nomineeId) {
        this.nomineeId = nomineeId;
    }

    public String getNomineeId() {
        return nomineeId;
    }

    /**
     * Setter/Getter for NOMINEE_NAME - table Field
     */
    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getNomineeName() {
        return nomineeName;
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
     * Setter/Getter for SHARE_PER - table Field
     */
    public void setSharePer(Double sharePer) {
        this.sharePer = sharePer;
    }

    public Double getSharePer() {
        return sharePer;
    }

    /**
     * Setter/Getter for NOMINEE_STATUS - table Field
     */
    public void setNomineeStatus(String nomineeStatus) {
        this.nomineeStatus = nomineeStatus;
    }

    public String getNomineeStatus() {
        return nomineeStatus;
    }

    /**
     * Setter/Getter for NOMINEE_DOB - table Field
     */
    public void setNomineeDob(Date nomineeDob) {
        this.nomineeDob = nomineeDob;
    }

    public Date getNomineeDob() {
        return nomineeDob;
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
     * Setter/Getter for G_STREET - table Field
     */
    public void setGStreet(String gStreet) {
        this.gStreet = gStreet;
    }

    public String getGStreet() {
        return gStreet;
    }

    /**
     * Setter/Getter for G_AREA - table Field
     */
    public void setGArea(String gArea) {
        this.gArea = gArea;
    }

    public String getGArea() {
        return gArea;
    }

    /**
     * Setter/Getter for G_CITY - table Field
     */
    public void setGCity(String gCity) {
        this.gCity = gCity;
    }

    public String getGCity() {
        return gCity;
    }

    /**
     * Setter/Getter for G_STATE - table Field
     */
    public void setGState(String gState) {
        this.gState = gState;
    }

    public String getGState() {
        return gState;
    }

    /**
     * Setter/Getter for G_PIN_CODE - table Field
     */
    public void setGPinCode(String gPinCode) {
        this.gPinCode = gPinCode;
    }

    public String getGPinCode() {
        return gPinCode;
    }

    /**
     * Setter/Getter for G_AREA_CODE - table Field
     */
    public void setGAreaCode(String gAreaCode) {
        this.gAreaCode = gAreaCode;
    }

    public String getGAreaCode() {
        return gAreaCode;
    }

    /**
     * Setter/Getter for G_PH_NO - table Field
     */
    public void setGPhNo(String gPhNo) {
        this.gPhNo = gPhNo;
    }

    public String getGPhNo() {
        return gPhNo;
    }

    /**
     * Setter/Getter for G_RELATIONSHIP - table Field
     */
    public void setGRelationship(String gRelationship) {
        this.gRelationship = gRelationship;
    }

    public String getGRelationship() {
        return gRelationship;
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
     * Setter/Getter for G_COUNTRY_CODE - table Field
     */
    public void setGCountryCode(String gCountryCode) {
        this.gCountryCode = gCountryCode;
    }

    public String getGCountryCode() {
        return gCountryCode;
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

    public String getNomineeCurrStatus() {
        return nomineeCurrStatus;
    }

    public void setNomineeCurrStatus(String nomineeCurrStatus) {
        this.nomineeCurrStatus = nomineeCurrStatus;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("actNum");
        return actNum;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("nomineeId", nomineeId));
        strB.append(getTOString("nomineeName", nomineeName));
        strB.append(getTOString("relationship", relationship));
        strB.append(getTOString("street", street));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("pinCode", pinCode));
        strB.append(getTOString("areaCode", areaCode));
        strB.append(getTOString("phNo", phNo));
        strB.append(getTOString("sharePer", sharePer));
        strB.append(getTOString("nomineeStatus", nomineeStatus));
        strB.append(getTOString("nomineeDob", nomineeDob));
        strB.append(getTOString("guardianName", guardianName));
        strB.append(getTOString("gStreet", gStreet));
        strB.append(getTOString("gArea", gArea));
        strB.append(getTOString("gCity", gCity));
        strB.append(getTOString("gState", gState));
        strB.append(getTOString("gPinCode", gPinCode));
        strB.append(getTOString("gAreaCode", gAreaCode));
        strB.append(getTOString("gPhNo", gPhNo));
        strB.append(getTOString("gRelationship", gRelationship));
        strB.append(getTOString("countryCode", countryCode));
        strB.append(getTOString("gCountryCode", gCountryCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("nomineeCurrStatus", nomineeCurrStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("nomineeId", nomineeId));
        strB.append(getTOXml("nomineeName", nomineeName));
        strB.append(getTOXml("relationship", relationship));
        strB.append(getTOXml("street", street));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("pinCode", pinCode));
        strB.append(getTOXml("areaCode", areaCode));
        strB.append(getTOXml("phNo", phNo));
        strB.append(getTOXml("sharePer", sharePer));
        strB.append(getTOXml("nomineeStatus", nomineeStatus));
        strB.append(getTOXml("nomineeDob", nomineeDob));
        strB.append(getTOXml("guardianName", guardianName));
        strB.append(getTOXml("gStreet", gStreet));
        strB.append(getTOXml("gArea", gArea));
        strB.append(getTOXml("gCity", gCity));
        strB.append(getTOXml("gState", gState));
        strB.append(getTOXml("gPinCode", gPinCode));
        strB.append(getTOXml("gAreaCode", gAreaCode));
        strB.append(getTOXml("gPhNo", gPhNo));
        strB.append(getTOXml("gRelationship", gRelationship));
        strB.append(getTOXml("countryCode", countryCode));
        strB.append(getTOXml("gCountryCode", gCountryCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("nomineeCurrStatus", nomineeCurrStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}