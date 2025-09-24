/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankTO.java
 * 
 * Created on20-05-2009 IST 
 */
package com.see.truetransact.transferobject.sysadmin.noticereportparameters;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

public class NoticePeriodTO extends TransferObject implements Serializable {

    private String reportCode = "";
    private String reportName = "";
    private String status = "";
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branCode = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String dataEntered = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String lang = "";
    private String grDetails = "";
    private String repHeading = "";

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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(reportCode);
        return reportCode;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("reportCode", reportCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branCode", branCode));
        strB.append(getTOString("reportCode", reportCode));
        strB.append(getTOString("reportName", reportName));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("dataEntered", dataEntered));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("lang", lang));
        strB.append(getTOString("grDetails", grDetails));
        strB.append(getTOString("repHeading", repHeading));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("reportCode", reportCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branCode", branCode));
        strB.append(getTOXml("reportCode", reportCode));
        strB.append(getTOXml("reportName", reportName));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("dataEntered", dataEntered));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("lang", lang));
        strB.append(getTOXml("grDetails", grDetails));
        strB.append(getTOXml("repHeading", repHeading));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property branCode.
     *
     * @return Value of property branCode.
     */
    public java.lang.String getBranCode() {
        return branCode;
    }

    /**
     * Setter for property branCode.
     *
     * @param branCode New value of property branCode.
     */
    public void setBranCode(java.lang.String branCode) {
        this.branCode = branCode;
    }

    /**
     * Getter for property reportCode.
     *
     * @return Value of property reportCode.
     */
    public java.lang.String getReportCode() {
        return reportCode;
    }

    /**
     * Setter for property reportCode.
     *
     * @param reportCode New value of property reportCode.
     */
    public void setReportCode(java.lang.String reportCode) {
        this.reportCode = reportCode;
    }

    /**
     * Getter for property reportName.
     *
     * @return Value of property reportName.
     */
    public java.lang.String getReportName() {
        return reportName;
    }

    /**
     * Setter for property reportName.
     *
     * @param reportName New value of property reportName.
     */
    public void setReportName(java.lang.String reportName) {
        this.reportName = reportName;
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
     * Getter for property dataEntered.
     *
     * @return Value of property dataEntered.
     */
    public java.lang.String getDataEntered() {
        return dataEntered;
    }

    /**
     * Setter for property dataEntered.
     *
     * @param dataEntered New value of property dataEntered.
     */
    public void setDataEntered(java.lang.String dataEntered) {
        this.dataEntered = dataEntered;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property createdDt.
     *
     * @param createdDt New value of property createdDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property lang.
     *
     * @return Value of property lang.
     */
    public java.lang.String getLang() {
        return lang;
    }

    /**
     * Setter for property lang.
     *
     * @param lang New value of property lang.
     */
    public void setLang(java.lang.String lang) {
        this.lang = lang;
    }

    /**
     * Getter for property grDetails.
     *
     * @return Value of property grDetails.
     */
    public java.lang.String getGrDetails() {
        return grDetails;
    }

    /**
     * Setter for property grDetails.
     *
     * @param grDetails New value of property grDetails.
     */
    public void setGrDetails(java.lang.String grDetails) {
        this.grDetails = grDetails;
    }

    /**
     * Getter for property repHeading.
     *
     * @return Value of property repHeading.
     */
    public java.lang.String getRepHeading() {
        return repHeading;
    }

    /**
     * Setter for property repHeading.
     *
     * @param repHeading New value of property repHeading.
     */
    public void setRepHeading(java.lang.String repHeading) {
        this.repHeading = repHeading;
    }
}