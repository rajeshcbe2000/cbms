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
package com.see.truetransact.transferobject.paddyprocurement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SUPPPLIER_MASTER.
 */
public class PaddyLocalityMasterTO extends TransferObject implements Serializable {

    private String txtLocalityName = "";
    private String txtLocalityCode = "";
    private String txtStreet = "";
    private String txtArea = "";
    private String cboCity = "";
    private String cboCountry = "";
    private String cboState = "";
    private String txtPincode = "";
    private String authorizeStatus = null;;
//    added before here
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
        strB.append(getTOString("txtLocalityName", txtLocalityName));
        strB.append(getTOString("txtLocalityCode", txtLocalityCode));
        strB.append(getTOString("txtStreet", txtStreet));
        strB.append(getTOString("txtArea", txtArea));
        strB.append(getTOString("cboCity", cboCity));
        strB.append(getTOString("cboCountry", cboCountry));
        strB.append(getTOString("cboState", cboState));
        strB.append(getTOString("txtPincode", txtPincode));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorize", authorize));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
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
        strB.append(getTOXml("txtLocalityName", txtLocalityName));
        strB.append(getTOXml("txtLocalityCode", txtLocalityCode));
        strB.append(getTOXml("txtStreet", txtStreet));
        strB.append(getTOXml("txtArea", txtArea));
        strB.append(getTOXml("cboCity", cboCity));
        strB.append(getTOXml("cboCountry", cboCountry));
        strB.append(getTOXml("cboState", cboState));
        strB.append(getTOXml("txtPincode", txtPincode));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorize", authorize));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property txtLocalityName.
     *
     * @return Value of property txtLocalityName.
     */
    public java.lang.String getTxtLocalityName() {
        return txtLocalityName;
    }

    /**
     * Setter for property txtLocalityName.
     *
     * @param txtLocalityName New value of property txtLocalityName.
     */
    public void setTxtLocalityName(java.lang.String txtLocalityName) {
        this.txtLocalityName = txtLocalityName;
    }

    /**
     * Getter for property txtLocalityCode.
     *
     * @return Value of property txtLocalityCode.
     */
    public java.lang.String getTxtLocalityCode() {
        return txtLocalityCode;
    }

    /**
     * Setter for property txtLocalityCode.
     *
     * @param txtLocalityCode New value of property txtLocalityCode.
     */
    public void setTxtLocalityCode(java.lang.String txtLocalityCode) {
        this.txtLocalityCode = txtLocalityCode;
    }

    /**
     * Getter for property txtStreet.
     *
     * @return Value of property txtStreet.
     */
    public java.lang.String getTxtStreet() {
        return txtStreet;
    }

    /**
     * Setter for property txtStreet.
     *
     * @param txtStreet New value of property txtStreet.
     */
    public void setTxtStreet(java.lang.String txtStreet) {
        this.txtStreet = txtStreet;
    }

    /**
     * Getter for property txtArea.
     *
     * @return Value of property txtArea.
     */
    public java.lang.String getTxtArea() {
        return txtArea;
    }

    /**
     * Setter for property txtArea.
     *
     * @param txtArea New value of property txtArea.
     */
    public void setTxtArea(java.lang.String txtArea) {
        this.txtArea = txtArea;
    }

    /**
     * Getter for property cboCity.
     *
     * @return Value of property cboCity.
     */
    public java.lang.String getCboCity() {
        return cboCity;
    }

    /**
     * Setter for property cboCity.
     *
     * @param cboCity New value of property cboCity.
     */
    public void setCboCity(java.lang.String cboCity) {
        this.cboCity = cboCity;
    }

    /**
     * Getter for property cboCountry.
     *
     * @return Value of property cboCountry.
     */
    public java.lang.String getCboCountry() {
        return cboCountry;
    }

    /**
     * Setter for property cboCountry.
     *
     * @param cboCountry New value of property cboCountry.
     */
    public void setCboCountry(java.lang.String cboCountry) {
        this.cboCountry = cboCountry;
    }

    /**
     * Getter for property cboState.
     *
     * @return Value of property cboState.
     */
    public java.lang.String getCboState() {
        return cboState;
    }

    /**
     * Setter for property cboState.
     *
     * @param cboState New value of property cboState.
     */
    public void setCboState(java.lang.String cboState) {
        this.cboState = cboState;
    }

    /**
     * Getter for property txtPincode.
     *
     * @return Value of property txtPincode.
     */
    public java.lang.String getTxtPincode() {
        return txtPincode;
    }

    /**
     * Setter for property txtPincode.
     *
     * @param txtPincode New value of property txtPincode.
     */
    public void setTxtPincode(java.lang.String txtPincode) {
        this.txtPincode = txtPincode;
    }

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }
}