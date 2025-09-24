/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SecurityTO.java
 *
 * Created on Wed May 25 12:11:50 IST 2005
 */
package com.see.truetransact.transferobject.customer.security;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author 152713
 */
/**
 * Table name for this TO is CUST_SECURITY_DETAILS.
 */
public class SecurityTO extends TransferObject implements Serializable {

    private String custId = "";
    private Double securityNo = null;
    private String securityCategory = "";
    private Double securityValue = null;
    private Date securityValueOn = null;
    private String particulars = "";
    private String securityType = "";
    private Date fromDt = null;
    private Date toDt = null;
    private Double availableSecurityValue = null;
    private String chargeNature = "";
    private Date chargeDt = null;
    private String selectiveCommodity = "";
    private String industrialUsers = "";
    private Date inspectionDt = null;
    private Double stockStatFreq = null;
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchCode = "";
    private Double weight = null;
    private Double grossWeight = null;
    private Double insuranceNo = null;

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
     * Setter/Getter for SECURITY_NO - table Field
     */
    public void setSecurityNo(Double securityNo) {
        this.securityNo = securityNo;
    }

    public Double getSecurityNo() {
        return securityNo;
    }

    /**
     * Setter/Getter for SECURITY_CATEGORY - table Field
     */
    public void setSecurityCategory(String securityCategory) {
        this.securityCategory = securityCategory;
    }

    public String getSecurityCategory() {
        return securityCategory;
    }

    /**
     * Setter/Getter for SECURITY_VALUE - table Field
     */
    public void setSecurityValue(Double securityValue) {
        this.securityValue = securityValue;
    }

    public Double getSecurityValue() {
        return securityValue;
    }

    /**
     * Setter/Getter for SECURITY_VALUE_ON - table Field
     */
    public void setSecurityValueOn(Date securityValueOn) {
        this.securityValueOn = securityValueOn;
    }

    public Date getSecurityValueOn() {
        return securityValueOn;
    }

    /**
     * Setter/Getter for PARTICULARS - table Field
     */
    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getParticulars() {
        return particulars;
    }

    /**
     * Setter/Getter for SECURITY_TYPE - table Field
     */
    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getSecurityType() {
        return securityType;
    }

    /**
     * Setter/Getter for FROM_DT - table Field
     */
    public void setFromDt(Date fromDt) {
        this.fromDt = fromDt;
    }

    public Date getFromDt() {
        return fromDt;
    }

    /**
     * Setter/Getter for TO_DT - table Field
     */
    public void setToDt(Date toDt) {
        this.toDt = toDt;
    }

    public Date getToDt() {
        return toDt;
    }

    /**
     * Setter/Getter for AVAILABLE_SECURITY_VALUE - table Field
     */
    public void setAvailableSecurityValue(Double availableSecurityValue) {
        this.availableSecurityValue = availableSecurityValue;
    }

    public Double getAvailableSecurityValue() {
        return availableSecurityValue;
    }

    /**
     * Setter/Getter for CHARGE_NATURE - table Field
     */
    public void setChargeNature(String chargeNature) {
        this.chargeNature = chargeNature;
    }

    public String getChargeNature() {
        return chargeNature;
    }

    /**
     * Setter/Getter for CHARGE_DT - table Field
     */
    public void setChargeDt(Date chargeDt) {
        this.chargeDt = chargeDt;
    }

    public Date getChargeDt() {
        return chargeDt;
    }

    /**
     * Setter/Getter for SELECTIVE_COMMODITY - table Field
     */
    public void setSelectiveCommodity(String selectiveCommodity) {
        this.selectiveCommodity = selectiveCommodity;
    }

    public String getSelectiveCommodity() {
        return selectiveCommodity;
    }

    /**
     * Setter/Getter for INDUSTRIAL_USERS - table Field
     */
    public void setIndustrialUsers(String industrialUsers) {
        this.industrialUsers = industrialUsers;
    }

    public String getIndustrialUsers() {
        return industrialUsers;
    }

    /**
     * Setter/Getter for INSPECTION_DT - table Field
     */
    public void setInspectionDt(Date inspectionDt) {
        this.inspectionDt = inspectionDt;
    }

    public Date getInspectionDt() {
        return inspectionDt;
    }

    /**
     * Setter/Getter for STOCK_STAT_FREQ - table Field
     */
    public void setStockStatFreq(Double stockStatFreq) {
        this.stockStatFreq = stockStatFreq;
    }

    public Double getStockStatFreq() {
        return stockStatFreq;
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
     * Setter/Getter for WEIGHT - table Field
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWeight() {
        return weight;
    }

    /**
     * Setter/Getter for INSURANCE_NO - table Field
     */
    public void setInsuranceNo(Double insuranceNo) {
        this.insuranceNo = insuranceNo;
    }

    public Double getInsuranceNo() {
        return insuranceNo;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("custId" + KEY_VAL_SEPARATOR + "securityNo");
        return custId + KEY_VAL_SEPARATOR + securityNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("securityNo", securityNo));
        strB.append(getTOString("securityCategory", securityCategory));
        strB.append(getTOString("securityValue", securityValue));
        strB.append(getTOString("securityValueOn", securityValueOn));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("securityType", securityType));
        strB.append(getTOString("fromDt", fromDt));
        strB.append(getTOString("toDt", toDt));
        strB.append(getTOString("availableSecurityValue", availableSecurityValue));
        strB.append(getTOString("chargeNature", chargeNature));
        strB.append(getTOString("chargeDt", chargeDt));
        strB.append(getTOString("selectiveCommodity", selectiveCommodity));
        strB.append(getTOString("industrialUsers", industrialUsers));
        strB.append(getTOString("inspectionDt", inspectionDt));
        strB.append(getTOString("stockStatFreq", stockStatFreq));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("weight", weight));
        strB.append(getTOString("grossWeight", grossWeight));
        strB.append(getTOString("insuranceNo", insuranceNo));
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
        strB.append(getTOXml("securityNo", securityNo));
        strB.append(getTOXml("securityCategory", securityCategory));
        strB.append(getTOXml("securityValue", securityValue));
        strB.append(getTOXml("securityValueOn", securityValueOn));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("securityType", securityType));
        strB.append(getTOXml("fromDt", fromDt));
        strB.append(getTOXml("toDt", toDt));
        strB.append(getTOXml("availableSecurityValue", availableSecurityValue));
        strB.append(getTOXml("chargeNature", chargeNature));
        strB.append(getTOXml("chargeDt", chargeDt));
        strB.append(getTOXml("selectiveCommodity", selectiveCommodity));
        strB.append(getTOXml("industrialUsers", industrialUsers));
        strB.append(getTOXml("inspectionDt", inspectionDt));
        strB.append(getTOXml("stockStatFreq", stockStatFreq));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("weight", weight));
        strB.append(getTOXml("grossWeight", grossWeight));
        strB.append(getTOXml("insuranceNo", insuranceNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property grossWeight.
     *
     * @return Value of property grossWeight.
     */
    public java.lang.Double getGrossWeight() {
        return grossWeight;
    }

    /**
     * Setter for property grossWeight.
     *
     * @param grossWeight New value of property grossWeight.
     */
    public void setGrossWeight(java.lang.Double grossWeight) {
        this.grossWeight = grossWeight;
    }
}