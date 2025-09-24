/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ForexDenominationMasterTO.java
 * 
 * Created on Thu Jan 27 12:18:39 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.denomination;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is FOREX_DENOMINATION_MASTER.
 */
public class ForexDenominationMasterTO extends TransferObject implements Serializable {

    private String currency = "";
    private String denominationName = "";
    private Double denominationValue = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String status = "";
    private Double denominationCount = null;
    private String denominationType = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;

    /**
     * Setter/Getter for CURRENCY - table Field
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    /**
     * Setter/Getter for DENOMINATION_NAME - table Field
     */
    public void setDenominationName(String denominationName) {
        this.denominationName = denominationName;
    }

    public String getDenominationName() {
        return denominationName;
    }

    /**
     * Setter/Getter for DENOMINATION_VALUE - table Field
     */
    public void setDenominationValue(Double denominationValue) {
        this.denominationValue = denominationValue;
    }

    public Double getDenominationValue() {
        return denominationValue;
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
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
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
     * Setter/Getter for DENOMINATION_COUNT - table Field
     */
    public void setDenominationCount(Double denominationCount) {
        this.denominationCount = denominationCount;
    }

    public Double getDenominationCount() {
        return denominationCount;
    }

    /**
     * Setter/Getter for DENOMINATION_TYPE - table Field
     */
    public void setDenominationType(String denominationType) {
        this.denominationType = denominationType;
    }

    public String getDenominationType() {
        return denominationType;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("currency" + KEY_VAL_SEPARATOR + "denominationName" + KEY_VAL_SEPARATOR + "denominationValue" + KEY_VAL_SEPARATOR + "denominationType");
        return currency + KEY_VAL_SEPARATOR + denominationName + KEY_VAL_SEPARATOR + denominationValue + KEY_VAL_SEPARATOR + denominationType;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("currency", currency));
        strB.append(getTOString("denominationName", denominationName));
        strB.append(getTOString("denominationValue", denominationValue));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("denominationCount", denominationCount));
        strB.append(getTOString("denominationType", denominationType));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("currency", currency));
        strB.append(getTOXml("denominationName", denominationName));
        strB.append(getTOXml("denominationValue", denominationValue));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("denominationCount", denominationCount));
        strB.append(getTOXml("denominationType", denominationType));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}