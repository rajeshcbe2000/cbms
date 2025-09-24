/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 *
 * 
 * InsuranceTO.java
 * 
 * Created on Thu Jan 13 15:58:37 IST 2005
 */
package com.see.truetransact.transferobject.customer.security;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/*
 * @author 152713
 */
/**
 * Table name for this TO is CUST_INSURANCE_DETAILS.
 */
public class InsuranceTO extends TransferObject implements Serializable {

    private String custId = "";
    private String insuranceCompany = "";
    private String policyNo = "";
    private Double policyAmt = null;
    private Date policyDt = null;
    private Double premiumAmt = null;
    private Date expiryDt = null;
    private String riskNature = "";
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String status = "";
    private Double slno = null;
    private Double securityNo = null;
    private String remarks = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchCode = "";

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
     * Setter/Getter for INSURANCE_COMPANY - table Field
     */
    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    /**
     * Setter/Getter for POLICY_NO - table Field
     */
    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    /**
     * Setter/Getter for POLICY_AMT - table Field
     */
    public void setPolicyAmt(Double policyAmt) {
        this.policyAmt = policyAmt;
    }

    public Double getPolicyAmt() {
        return policyAmt;
    }

    /**
     * Setter/Getter for POLICY_DT - table Field
     */
    public void setPolicyDt(Date policyDt) {
        this.policyDt = policyDt;
    }

    public Date getPolicyDt() {
        return policyDt;
    }

    /**
     * Setter/Getter for PREMIUM_AMT - table Field
     */
    public void setPremiumAmt(Double premiumAmt) {
        this.premiumAmt = premiumAmt;
    }

    public Double getPremiumAmt() {
        return premiumAmt;
    }

    /**
     * Setter/Getter for EXPIRY_DT - table Field
     */
    public void setExpiryDt(Date expiryDt) {
        this.expiryDt = expiryDt;
    }

    public Date getExpiryDt() {
        return expiryDt;
    }

    /**
     * Setter/Getter for RISK_NATURE - table Field
     */
    public void setRiskNature(String riskNature) {
        this.riskNature = riskNature;
    }

    public String getRiskNature() {
        return riskNature;
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
     * Setter/Getter for SLNO - table Field
     */
    public void setSlno(Double slno) {
        this.slno = slno;
    }

    public Double getSlno() {
        return slno;
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
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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
        setKeyColumns("custId" + KEY_VAL_SEPARATOR + "slno");
        return custId + KEY_VAL_SEPARATOR + slno;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("insuranceCompany", insuranceCompany));
        strB.append(getTOString("policyNo", policyNo));
        strB.append(getTOString("policyAmt", policyAmt));
        strB.append(getTOString("policyDt", policyDt));
        strB.append(getTOString("premiumAmt", premiumAmt));
        strB.append(getTOString("expiryDt", expiryDt));
        strB.append(getTOString("riskNature", riskNature));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("securityNo", securityNo));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
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
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("insuranceCompany", insuranceCompany));
        strB.append(getTOXml("policyNo", policyNo));
        strB.append(getTOXml("policyAmt", policyAmt));
        strB.append(getTOXml("policyDt", policyDt));
        strB.append(getTOXml("premiumAmt", premiumAmt));
        strB.append(getTOXml("expiryDt", expiryDt));
        strB.append(getTOXml("riskNature", riskNature));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("securityNo", securityNo));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}