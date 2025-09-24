/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankBranchTO.java
 * 
 * Created on Thu Dec 30 17:56:12 IST 2004
 */
package com.see.truetransact.transferobject.sysadmin.otherbank;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OTHER_BANK_BRANCH.
 */
public class OtherBankBranchTO extends TransferObject implements Serializable {

    private String bankCode = "";
    private String branchCode = "";
    private String branchShortName = "";
    private String branchName = "";
    private String status = "";
    private String city = "";
    private String address = "";
    private String micr = "";
    private String accountHead = "";
    private String prodType = "";
    private String prodId = "";
    private String state = "";
    private String country = "";
    private String pincode = "";
    private String phoneNo = "";
//        private String bankType="";
    private String cRadio_HVC_Yes = "";
    private String cRadio_DB_Yes = "";

//        private boolean cRadio_HVC_No=false;
    /**
     * Setter/Getter for BANK_CODE - table Field
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    /**
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter/Getter for BRANCH_SHORT_NAME - table Field
     */
    public void setBranchShortName(String branchShortName) {
        this.branchShortName = branchShortName;
    }

    public String getBranchShortName() {
        return branchShortName;
    }

    /**
     * Setter/Getter for BRANCH_NAME - table Field
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
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
     * Setter/Getter for CITY - table Field
     */
    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    /**
     * Setter/Getter for ADDRESS - table Field
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
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
     * Setter/Getter for COUNTRY - table Field
     */
    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("bankCode" + KEY_VAL_SEPARATOR + "branchCode");
        return bankCode + branchCode;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("bankCode", bankCode));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("branchShortName", branchShortName));
        strB.append(getTOString("branchName", branchName));
        strB.append(getTOString("status", status));
        strB.append(getTOString("city", city));
        strB.append(getTOString("address", address));
        strB.append(getTOString("state", state));
        strB.append(getTOString("country", country));
        strB.append(getTOString("pincode", pincode));
        strB.append(getTOString("phoneNo", phoneNo));
//                strB.append(getTOString("bankType",bankType));
        strB.append(getTOString("cRadio_HVC_Yes", cRadio_HVC_Yes));
        strB.append(getTOString("cRadio_DB_Yes", cRadio_DB_Yes));
        strB.append(getTOString("micr", micr));
        strB.append(getTOString("accountHead", accountHead));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("bankCode", bankCode));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("branchShortName", branchShortName));
        strB.append(getTOXml("branchName", branchName));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("address", address));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("country", country));
        strB.append(getTOXml("pincode", pincode));
        strB.append(getTOXml("phoneNo", phoneNo));
//                strB.append(getTOXml("bankType",bankType));
        strB.append(getTOXml("cRadio_HVC_Yes", cRadio_HVC_Yes));
        strB.append(getTOXml("cRadio_DB_Yes", cRadio_DB_Yes));
        strB.append(getTOXml("micr", micr));
        strB.append(getTOXml("accountHead", accountHead));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property phoneNo.
     *
     * @return Value of property phoneNo.
     */
    public java.lang.String getPhoneNo() {
        return phoneNo;
    }

    /**
     * Setter for property phoneNo.
     *
     * @param phoneNo New value of property phoneNo.
     */
    public void setPhoneNo(java.lang.String phoneNo) {
        this.phoneNo = phoneNo;
    }

//        /**
//         * Getter for property bankType.
//         * @return Value of property bankType.
//         */
//        public java.lang.String getBankType() {
//            return bankType;
//        }
//        
//        /**
//         * Setter for property bankType.
//         * @param bankType New value of property bankType.
//         */
//        public void setBankType(java.lang.String bankType) {
//            this.bankType = bankType;
//        }
    /**
     * Getter for property cRadio_HVC_Yes.
     *
     * @return Value of property cRadio_HVC_Yes.
     */
    public java.lang.String getCRadio_HVC_Yes() {
        return cRadio_HVC_Yes;
    }

    /**
     * Setter for property cRadio_HVC_Yes.
     *
     * @param cRadio_HVC_Yes New value of property cRadio_HVC_Yes.
     */
    public void setCRadio_HVC_Yes(java.lang.String cRadio_HVC_Yes) {
        this.cRadio_HVC_Yes = cRadio_HVC_Yes;
    }

    /**
     * Getter for property cRadio_DB_Yes.
     *
     * @return Value of property cRadio_DB_Yes.
     */
    public java.lang.String getCRadio_DB_Yes() {
        return cRadio_DB_Yes;
    }

    /**
     * Setter for property cRadio_DB_Yes.
     *
     * @param cRadio_DB_Yes New value of property cRadio_DB_Yes.
     */
    public void setCRadio_DB_Yes(java.lang.String cRadio_DB_Yes) {
        this.cRadio_DB_Yes = cRadio_DB_Yes;
    }

    /**
     * Getter for property micr.
     *
     * @return Value of property micr.
     */
    public java.lang.String getMicr() {
        return micr;
    }

    /**
     * Setter for property micr.
     *
     * @param micr New value of property micr.
     */
    public void setMicr(java.lang.String micr) {
        this.micr = micr;
    }

    /**
     * Getter for property accountHead.
     *
     * @return Value of property accountHead.
     */
    public java.lang.String getAccountHead() {
        return accountHead;
    }

    /**
     * Setter for property accountHead.
     *
     * @param accountHead New value of property accountHead.
     */
    public void setAccountHead(java.lang.String accountHead) {
        this.accountHead = accountHead;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }

    /**
     * Getter for property prodId.
     *
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     *
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }
}