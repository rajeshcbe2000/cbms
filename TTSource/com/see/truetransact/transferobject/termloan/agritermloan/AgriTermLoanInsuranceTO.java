/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInsuranceTO.java
 *
 * @author shanmugavel
 * Created on Fri Apr 30 17:04:14 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan.agritermloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_INSURANCE_DETAILS.
 */
public class AgriTermLoanInsuranceTO extends TransferObject implements Serializable {

    private String phoneNo = "";
    private String address = "";
    private String securityNo = "";
    private String insuranceCompany = "";
    private String insurnaceUnderScheme = "";
    private String insuranceDetails = "";
    private String policyNo = "";
    private Double policyAmt = null;
    private Date policyDt = null;
    private Double premiumAmt = null;
    private Date expiryDt = null;
    private Date fromAppliedDt = null;
    private String riskNature = "";
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String acctNum = "";
    private String status = "";
    private String slno = "";
    private String command = "";
    private String remarks = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String debitProdType = "";
    private String debitProdId = "";
    private String debitAcctNum = "";

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
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
     * Setter/Getter for ACCT_NUM - table Field
     */
    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctNum() {
        return acctNum;
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
    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getSlno() {
        return slno;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNum" + KEY_VAL_SEPARATOR + "slno");
        return acctNum + KEY_VAL_SEPARATOR + slno;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("address", address));
        strB.append(getTOString("phoneNo", phoneNo));
        strB.append(getTOString("insuranceDetails", insuranceDetails));
        strB.append(getTOString("insuranceCompany", insuranceCompany));
        strB.append(getTOString("insurnaceUnderScheme", insurnaceUnderScheme));
        strB.append(getTOString("policyNo", policyNo));
        strB.append(getTOString("policyAmt", policyAmt));
        strB.append(getTOString("policyDt", policyDt));
        strB.append(getTOString("premiumAmt", premiumAmt));
        strB.append(getTOString("expiryDt", expiryDt));
        strB.append(getTOString("riskNature", riskNature));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("debitProdType", debitProdType));
        strB.append(getTOString("debitProdId", debitProdId));
        strB.append(getTOString("debitAcctNum", debitAcctNum));
        strB.append(getTOString("securityNo", securityNo));
        strB.append(getTOString("status", status));
        strB.append(getTOString("remarks", remarks));
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
        strB.append(getTOXml("address", address));
        strB.append(getTOXml("phoneNo", phoneNo));
        strB.append(getTOXml("insuranceDetails", insuranceDetails));
        strB.append(getTOXml("insuranceCompany", insuranceCompany));
        strB.append(getTOXml("insurnaceUnderScheme", insurnaceUnderScheme));
        strB.append(getTOXml("policyNo", policyNo));
        strB.append(getTOXml("policyAmt", policyAmt));
        strB.append(getTOXml("policyDt", policyDt));
        strB.append(getTOXml("fromAppliedDt", fromAppliedDt));
        strB.append(getTOXml("premiumAmt", premiumAmt));
        strB.append(getTOXml("expiryDt", expiryDt));
        strB.append(getTOXml("riskNature", riskNature));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("debitProdType", debitProdType));
        strB.append(getTOXml("debitProdId", debitProdId));
        strB.append(getTOXml("debitAcctNum", debitAcctNum));
        strB.append(getTOXml("securityNo", securityNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));

        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property insurnaceUnderScheme.
     *
     * @return Value of property insurnaceUnderScheme.
     */
    public java.lang.String getInsurnaceUnderScheme() {
        return insurnaceUnderScheme;
    }

    /**
     * Setter for property insurnaceUnderScheme.
     *
     * @param insurnaceUnderScheme New value of property insurnaceUnderScheme.
     */
    public void setInsurnaceUnderScheme(java.lang.String insurnaceUnderScheme) {
        this.insurnaceUnderScheme = insurnaceUnderScheme;
    }

    /**
     * Getter for property fromAppliedDt.
     *
     * @return Value of property fromAppliedDt.
     */
    public java.util.Date getFromAppliedDt() {
        return fromAppliedDt;
    }

    /**
     * Setter for property fromAppliedDt.
     *
     * @param fromAppliedDt New value of property fromAppliedDt.
     */
    public void setFromAppliedDt(java.util.Date fromAppliedDt) {
        this.fromAppliedDt = fromAppliedDt;
    }

    /**
     * Getter for property debitProdType.
     *
     * @return Value of property debitProdType.
     */
    public java.lang.String getDebitProdType() {
        return debitProdType;
    }

    /**
     * Setter for property debitProdType.
     *
     * @param debitProdType New value of property debitProdType.
     */
    public void setDebitProdType(java.lang.String debitProdType) {
        this.debitProdType = debitProdType;
    }

    /**
     * Getter for property debitProdId.
     *
     * @return Value of property debitProdId.
     */
    public java.lang.String getDebitProdId() {
        return debitProdId;
    }

    /**
     * Setter for property debitProdId.
     *
     * @param debitProdId New value of property debitProdId.
     */
    public void setDebitProdId(java.lang.String debitProdId) {
        this.debitProdId = debitProdId;
    }

    /**
     * Getter for property insuranceDetails.
     *
     * @return Value of property insuranceDetails.
     */
    public java.lang.String getInsuranceDetails() {
        return insuranceDetails;
    }

    /**
     * Setter for property insuranceDetails.
     *
     * @param insuranceDetails New value of property insuranceDetails.
     */
    public void setInsuranceDetails(java.lang.String insuranceDetails) {
        this.insuranceDetails = insuranceDetails;
    }

    /**
     * Getter for property address.
     *
     * @return Value of property address.
     */
    public java.lang.String getAddress() {
        return address;
    }

    /**
     * Setter for property address.
     *
     * @param address New value of property address.
     */
    public void setAddress(java.lang.String address) {
        this.address = address;
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

    /**
     * Getter for property debitAcctNum.
     *
     * @return Value of property debitAcctNum.
     */
    public java.lang.String getDebitAcctNum() {
        return debitAcctNum;
    }

    /**
     * Setter for property debitAcctNum.
     *
     * @param debitAcctNum New value of property debitAcctNum.
     */
    public void setDebitAcctNum(java.lang.String debitAcctNum) {
        this.debitAcctNum = debitAcctNum;
    }
}