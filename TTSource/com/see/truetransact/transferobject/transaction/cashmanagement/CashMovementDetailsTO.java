/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashMovementDetailsTO.java
 * 
 * Created on Thu Sep 01 11:47:08 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.transaction.cashmanagement;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 * Table name for this TO is CASH_MOVEMENT_DETAILS.
 */
public class CashMovementDetailsTO extends TransferObject implements Serializable {

    private String cashMovementId = "";
    private String denominationName = "";
    private Double denominationCount = null;
    private Double denominationTotal = null;
    private String status = "";
    private String denominationType = "";
    private String denomType = "";
    private java.util.Date movementDate = null;
    private Double totalValue;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchId = "";

    /**
     * Setter/Getter for CASH_MOVEMENT_ID - table Field
     */
    public void setCashMovementId(String cashMovementId) {
        this.cashMovementId = cashMovementId;
    }

    public String getCashMovementId() {
        return cashMovementId;
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
     * Setter/Getter for DENOMINATION_COUNT - table Field
     */
    public void setDenominationCount(Double denominationCount) {
        this.denominationCount = denominationCount;
    }

    public Double getDenominationCount() {
        return denominationCount;
    }

    /**
     * Setter/Getter for DENOMINATION_TOTAL - table Field
     */
    public void setDenominationTotal(Double denominationTotal) {
        this.denominationTotal = denominationTotal;
    }

    public Double getDenominationTotal() {
        return denominationTotal;
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
     * Setter/Getter for DENOMINATION_TYPE - table Field
     */
    public void setDenominationType(String denominationType) {
        this.denominationType = denominationType;
    }

    public String getDenominationType() {
        return denominationType;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(cashMovementId);
        return cashMovementId;
    }

    public void setMovementDate(java.util.Date movementDate) {
        this.movementDate = movementDate;
    }

    public java.util.Date getMovementDate() {
        return movementDate;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Double getTotalValue() {
        return totalValue;
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

    public java.lang.String getBranchId() {
        return branchId;
    }

    /**
     * Setter for property branchId.
     *
     * @param branchId New value of property branchId.
     */
    public void setBranchId(java.lang.String branchId) {
        this.branchId = branchId;
    }

    public java.lang.String getDenomType() {
        return this.denomType;
    }

    /**
     * Setter for property denominationType.
     *
     * @param denominationType New value of property denominationType.
     */
    public void setDenomType(java.lang.String denomType) {
        this.denomType = denomType;

    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("cashMovementId", cashMovementId));
        strB.append(getTOString("denominationName", denominationName));
        strB.append(getTOString("denominationCount", denominationCount));
        strB.append(getTOString("denominationTotal", denominationTotal));
        strB.append(getTOString("status", status));
        strB.append(getTOString("denominationType", denominationType));
        strB.append(getTOString("totalValue", totalValue));
        strB.append(getTOString("movementDate", movementDate));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("denomType", denomType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("cashMovementId", cashMovementId));
        strB.append(getTOXml("denominationName", denominationName));
        strB.append(getTOXml("denominationCount", denominationCount));
        strB.append(getTOXml("denominationTotal", denominationTotal));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("denominationType", denominationType));
        strB.append(getTOXml("totalValue", totalValue));
        strB.append(getTOXml("movementDate", movementDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("denomType", denomType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}