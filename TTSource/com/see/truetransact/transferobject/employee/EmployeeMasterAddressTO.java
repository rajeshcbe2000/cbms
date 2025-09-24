/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerAddressTO.java
 *
 * Created on Fri Feb 11 12:55:29 IST 2005
 */
package com.see.truetransact.transferobject.employee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUST_ADDR.
 */
public class EmployeeMasterAddressTO extends TransferObject implements Serializable {

    private String txtEmpId = "";
    private String cboEmpAddressType = "";
    private String txtEmpStreet = "";
    private String txtEmpArea = "";
    private String cboEmpCity = "";
    private String cboEmpState = "";
    private String cboEmpCountry = "";
    private String txtEmpPinNo = "";
    private String txtEmpPhoneNoCountryCode = "";
    private String txtEmpPhoneNoAreaCode = "";
    private String txtEmpPhoneNo = "";
    private String txtEmpMobileNoCountryCode = "";
    private String txtEmpMobileNoAreaCode = "";
    private String txtEmpMobileNo = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

    public String getKeyData() {
        setKeyColumns(txtEmpId);
        return txtEmpId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("txtEmpId", txtEmpId));
        strB.append(getTOString("txtEmpId", txtEmpId));
        strB.append(getTOString("cboEmpAddressType", cboEmpAddressType));
        strB.append(getTOString("txtEmpStreet", txtEmpStreet));
        strB.append(getTOString("txtEmpArea", txtEmpArea));
        strB.append(getTOString("cboEmpCity", cboEmpCity));
        strB.append(getTOString("cboEmpState", cboEmpState));
        strB.append(getTOString("cboEmpCountry", cboEmpCountry));
        strB.append(getTOString("txtEmpPinNo", txtEmpPinNo));
        strB.append(getTOString("txtEmpPhoneNoCountryCode", txtEmpPhoneNoCountryCode));
        strB.append(getTOString("txtEmpPhoneNoAreaCode", txtEmpPhoneNoAreaCode));
        strB.append(getTOString("txtEmpPhoneNo", txtEmpPhoneNo));
        strB.append(getTOString("txtEmpMobileNoCountryCode", txtEmpMobileNoCountryCode));
        strB.append(getTOString("txtEmpMobileNoAreaCode", txtEmpMobileNoAreaCode));
        strB.append(getTOString("txtEmpMobileNo", txtEmpMobileNo));
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
        strB.append(getTOXml("txtEmpId", txtEmpId));
        strB.append(getTOXml("txtEmpId", txtEmpId));
        strB.append(getTOXml("cboEmpAddressType", cboEmpAddressType));
        strB.append(getTOXml("txtEmpStreet", txtEmpStreet));
        strB.append(getTOXml("txtEmpArea", txtEmpArea));
        strB.append(getTOXml("cboEmpCity", cboEmpCity));
        strB.append(getTOXml("cboEmpState", cboEmpState));
        strB.append(getTOXml("cboEmpCountry", cboEmpCountry));
        strB.append(getTOXml("txtEmpPinNo", txtEmpPinNo));
        strB.append(getTOXml("txtEmpPhoneNoCountryCode", txtEmpPhoneNoCountryCode));
        strB.append(getTOXml("txtEmpPhoneNoAreaCode", txtEmpPhoneNoAreaCode));
        strB.append(getTOXml("txtEmpPhoneNo", txtEmpPhoneNo));
        strB.append(getTOXml("txtEmpMobileNoCountryCode", txtEmpMobileNoCountryCode));
        strB.append(getTOXml("txtEmpMobileNoAreaCode", txtEmpMobileNoAreaCode));
        strB.append(getTOXml("txtEmpMobileNo", txtEmpMobileNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));

        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property txtEmpId.
     *
     * @return Value of property txtEmpId.
     */
    public java.lang.String getTxtEmpId() {
        return txtEmpId;
    }

    /**
     * Setter for property txtEmpId.
     *
     * @param txtEmpId New value of property txtEmpId.
     */
    public void setTxtEmpId(java.lang.String txtEmpId) {
        this.txtEmpId = txtEmpId;
    }

    /**
     * Getter for property cboEmpAddressType.
     *
     * @return Value of property cboEmpAddressType.
     */
    public java.lang.String getCboEmpAddressType() {
        return cboEmpAddressType;
    }

    /**
     * Setter for property cboEmpAddressType.
     *
     * @param cboEmpAddressType New value of property cboEmpAddressType.
     */
    public void setCboEmpAddressType(java.lang.String cboEmpAddressType) {
        this.cboEmpAddressType = cboEmpAddressType;
    }

    /**
     * Getter for property txtEmpStreet.
     *
     * @return Value of property txtEmpStreet.
     */
    public java.lang.String getTxtEmpStreet() {
        return txtEmpStreet;
    }

    /**
     * Setter for property txtEmpStreet.
     *
     * @param txtEmpStreet New value of property txtEmpStreet.
     */
    public void setTxtEmpStreet(java.lang.String txtEmpStreet) {
        this.txtEmpStreet = txtEmpStreet;
    }

    /**
     * Getter for property txtEmpArea.
     *
     * @return Value of property txtEmpArea.
     */
    public java.lang.String getTxtEmpArea() {
        return txtEmpArea;
    }

    /**
     * Setter for property txtEmpArea.
     *
     * @param txtEmpArea New value of property txtEmpArea.
     */
    public void setTxtEmpArea(java.lang.String txtEmpArea) {
        this.txtEmpArea = txtEmpArea;
    }

    /**
     * Getter for property cboEmpCity.
     *
     * @return Value of property cboEmpCity.
     */
    public java.lang.String getCboEmpCity() {
        return cboEmpCity;
    }

    /**
     * Setter for property cboEmpCity.
     *
     * @param cboEmpCity New value of property cboEmpCity.
     */
    public void setCboEmpCity(java.lang.String cboEmpCity) {
        this.cboEmpCity = cboEmpCity;
    }

    /**
     * Getter for property cboEmpState.
     *
     * @return Value of property cboEmpState.
     */
    public java.lang.String getCboEmpState() {
        return cboEmpState;
    }

    /**
     * Setter for property cboEmpState.
     *
     * @param cboEmpState New value of property cboEmpState.
     */
    public void setCboEmpState(java.lang.String cboEmpState) {
        this.cboEmpState = cboEmpState;
    }

    /**
     * Getter for property cboEmpCountry.
     *
     * @return Value of property cboEmpCountry.
     */
    public java.lang.String getCboEmpCountry() {
        return cboEmpCountry;
    }

    /**
     * Setter for property cboEmpCountry.
     *
     * @param cboEmpCountry New value of property cboEmpCountry.
     */
    public void setCboEmpCountry(java.lang.String cboEmpCountry) {
        this.cboEmpCountry = cboEmpCountry;
    }

    /**
     * Getter for property txtEmpPinNo.
     *
     * @return Value of property txtEmpPinNo.
     */
    public java.lang.String getTxtEmpPinNo() {
        return txtEmpPinNo;
    }

    /**
     * Setter for property txtEmpPinNo.
     *
     * @param txtEmpPinNo New value of property txtEmpPinNo.
     */
    public void setTxtEmpPinNo(java.lang.String txtEmpPinNo) {
        this.txtEmpPinNo = txtEmpPinNo;
    }

    /**
     * Getter for property txtEmpPhoneNoCountryCode.
     *
     * @return Value of property txtEmpPhoneNoCountryCode.
     */
    public java.lang.String getTxtEmpPhoneNoCountryCode() {
        return txtEmpPhoneNoCountryCode;
    }

    /**
     * Setter for property txtEmpPhoneNoCountryCode.
     *
     * @param txtEmpPhoneNoCountryCode New value of property
     * txtEmpPhoneNoCountryCode.
     */
    public void setTxtEmpPhoneNoCountryCode(java.lang.String txtEmpPhoneNoCountryCode) {
        this.txtEmpPhoneNoCountryCode = txtEmpPhoneNoCountryCode;
    }

    /**
     * Getter for property txtEmpPhoneNoAreaCode.
     *
     * @return Value of property txtEmpPhoneNoAreaCode.
     */
    public java.lang.String getTxtEmpPhoneNoAreaCode() {
        return txtEmpPhoneNoAreaCode;
    }

    /**
     * Setter for property txtEmpPhoneNoAreaCode.
     *
     * @param txtEmpPhoneNoAreaCode New value of property txtEmpPhoneNoAreaCode.
     */
    public void setTxtEmpPhoneNoAreaCode(java.lang.String txtEmpPhoneNoAreaCode) {
        this.txtEmpPhoneNoAreaCode = txtEmpPhoneNoAreaCode;
    }

    /**
     * Getter for property txtEmpPhoneNo.
     *
     * @return Value of property txtEmpPhoneNo.
     */
    public java.lang.String getTxtEmpPhoneNo() {
        return txtEmpPhoneNo;
    }

    /**
     * Setter for property txtEmpPhoneNo.
     *
     * @param txtEmpPhoneNo New value of property txtEmpPhoneNo.
     */
    public void setTxtEmpPhoneNo(java.lang.String txtEmpPhoneNo) {
        this.txtEmpPhoneNo = txtEmpPhoneNo;
    }

    /**
     * Getter for property txtEmpMobileNoCountryCode.
     *
     * @return Value of property txtEmpMobileNoCountryCode.
     */
    public java.lang.String getTxtEmpMobileNoCountryCode() {
        return txtEmpMobileNoCountryCode;
    }

    /**
     * Setter for property txtEmpMobileNoCountryCode.
     *
     * @param txtEmpMobileNoCountryCode New value of property
     * txtEmpMobileNoCountryCode.
     */
    public void setTxtEmpMobileNoCountryCode(java.lang.String txtEmpMobileNoCountryCode) {
        this.txtEmpMobileNoCountryCode = txtEmpMobileNoCountryCode;
    }

    /**
     * Getter for property txtEmpMobileNoAreaCode.
     *
     * @return Value of property txtEmpMobileNoAreaCode.
     */
    public java.lang.String getTxtEmpMobileNoAreaCode() {
        return txtEmpMobileNoAreaCode;
    }

    /**
     * Setter for property txtEmpMobileNoAreaCode.
     *
     * @param txtEmpMobileNoAreaCode New value of property
     * txtEmpMobileNoAreaCode.
     */
    public void setTxtEmpMobileNoAreaCode(java.lang.String txtEmpMobileNoAreaCode) {
        this.txtEmpMobileNoAreaCode = txtEmpMobileNoAreaCode;
    }

    /**
     * Getter for property txtEmpMobileNo.
     *
     * @return Value of property txtEmpMobileNo.
     */
    public java.lang.String getTxtEmpMobileNo() {
        return txtEmpMobileNo;
    }

    /**
     * Setter for property txtEmpMobileNo.
     *
     * @param txtEmpMobileNo New value of property txtEmpMobileNo.
     */
    public void setTxtEmpMobileNo(java.lang.String txtEmpMobileNo) {
        this.txtEmpMobileNo = txtEmpMobileNo;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }
}