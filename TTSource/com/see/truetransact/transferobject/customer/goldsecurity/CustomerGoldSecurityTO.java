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
package com.see.truetransact.transferobject.customer.goldsecurity;

import com.see.truetransact.transferobject.customer.security.*;
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
public class CustomerGoldSecurityTO extends TransferObject implements Serializable {
    private String custId = "";
    private Double securityNo = null;
    private String securityCategory = "";
    private String purity = "";
    private Double grossWeight = null;
    private Double netWeight = null;  
    private Double securityValue = null;
    private String marketRate = "";
    private String particulars = "";
    private Double pledgeAmt = null;
    private String appraiserId = "";
    private Date asondt = null;    
    private Double availableSecurityValue = null;    
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchCode = "";
    private String goldSecurityId = "";

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
     * Setter/Getter for PARTICULARS - table Field
     */
    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getParticulars() {
        return particulars;
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
        strB.append(getTOString("purity", purity));
        strB.append(getTOString("grossWeight", grossWeight));
        strB.append(getTOString("netWeight", netWeight));
        strB.append(getTOString("securityValue", securityValue));
        strB.append(getTOString("marketRate", marketRate));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("pledgeAmt", pledgeAmt));
        strB.append(getTOString("appraiserId", appraiserId));
        strB.append(getTOString("asondt", asondt));
        strB.append(getTOString("availableSecurityValue", availableSecurityValue));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("goldSecurityId", goldSecurityId));
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
        strB.append(getTOXml("purity", purity));
        strB.append(getTOXml("grossWeight", grossWeight));
        strB.append(getTOXml("netWeight", netWeight));
        strB.append(getTOXml("securityValue", securityValue));
        strB.append(getTOXml("marketRate", marketRate));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("pledgeAmt", pledgeAmt));
        strB.append(getTOXml("appraiserId", appraiserId));
        strB.append(getTOXml("asondt", asondt));
        strB.append(getTOXml("availableSecurityValue", availableSecurityValue));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchCode", branchCode)); 
        strB.append(getTOXml("goldSecurityId", goldSecurityId));    
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

    public String getAppraiserId() {
        return appraiserId;
    }

    public void setAppraiserId(String appraiserId) {
        this.appraiserId = appraiserId;
    }

    public String getMarketRate() {
        return marketRate;
    }

    public void setMarketRate(String marketRate) {
        this.marketRate = marketRate;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Double getPledgeAmt() {
        return pledgeAmt;
    }

    public void setPledgeAmt(Double pledgeAmt) {
        this.pledgeAmt = pledgeAmt;
    }

    public String getPurity() {
        return purity;
    }

    public void setPurity(String purity) {
        this.purity = purity;
    }

    public Date getAsondt() {
        return asondt;
    }

    public void setAsondt(Date asondt) {
        this.asondt = asondt;
    }

    public String getGoldSecurityId() {
        return goldSecurityId;
    }

    public void setGoldSecurityId(String goldSecurityId) {
        this.goldSecurityId = goldSecurityId;
    }
    
    
}