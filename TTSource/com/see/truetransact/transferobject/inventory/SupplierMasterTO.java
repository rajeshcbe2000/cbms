/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SupplierMasterTO.java
 * 
 * Created on Fri Jun 10 17:48:05 IST 2011
 */
package com.see.truetransact.transferobject.inventory;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SUPPPLIER_MASTER.
 */
public class SupplierMasterTO extends TransferObject implements Serializable {

    private String supplierid = "";
    private String suppliername = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String pincode = "";
    private String country = "";
    private String tinno = "";
    private String cst = "";
    private String addrremarks = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorize = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchCode = "";

    /**
     * Setter/Getter for SUPPLIERID - table Field
     */
    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }

    public String getSupplierid() {
        return supplierid;
    }

    /**
     * Setter/Getter for SUPPLIERNAME - table Field
     */
    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public String getSuppliername() {
        return suppliername;
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
     * Setter/Getter for PINCODE - table Field
     */
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPincode() {
        return pincode;
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
     * Setter/Getter for TINNO - table Field
     */
    public void setTinno(String tinno) {
        this.tinno = tinno;
    }

    public String getTinno() {
        return tinno;
    }

    /**
     * Setter/Getter for CST - table Field
     */
    public void setCst(String cst) {
        this.cst = cst;
    }

    public String getCst() {
        return cst;
    }

    /**
     * Setter/Getter for ADDRREMARKS - table Field
     */
    public void setAddrremarks(String addrremarks) {
        this.addrremarks = addrremarks;
    }

    public String getAddrremarks() {
        return addrremarks;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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
     * Setter/Getter for AUTHORIZE - table Field
     */
    public void setAuthorize(String authorize) {
        this.authorize = authorize;
    }

    public String getAuthorize() {
        return authorize;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("supplierid", supplierid));
        strB.append(getTOString("suppliername", suppliername));
        strB.append(getTOString("street", street));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("pincode", pincode));
        strB.append(getTOString("country", country));
        strB.append(getTOString("tinno", tinno));
        strB.append(getTOString("cst", cst));
        strB.append(getTOString("addrremarks", addrremarks));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorize", authorize));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("supplierid", supplierid));
        strB.append(getTOXml("suppliername", suppliername));
        strB.append(getTOXml("street", street));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("pincode", pincode));
        strB.append(getTOXml("country", country));
        strB.append(getTOXml("tinno", tinno));
        strB.append(getTOXml("cst", cst));
        strB.append(getTOXml("addrremarks", addrremarks));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorize", authorize));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}