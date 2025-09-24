/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InvestmentsRFTO.java
 * 
 * Created on Tue Feb 21 10:52:38 IST 2012
 */
package com.see.truetransact.transferobject.investments;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is INVESTMENT_RESERVE_FUND.
 */
public class InvestmentsRFTO extends TransferObject implements Serializable {

    private String investmentId = "";
    private String investmentType = "";
    private String investmentProdId = "";
    private String investmentProdDesc = "";
    private String agencyName = "";
    private String investmentRefNo = "";
    private Date investmentIssueDt = null;
    private Double amount = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedStatus = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String txtBranchCode = "";

    /**
     * Setter/Getter for INVESTMENT_ID - table Field
     */
    public void setInvestmentId(String investmentId) {
        this.investmentId = investmentId;
    }

    public String getInvestmentId() {
        return investmentId;
    }

    /**
     * Setter/Getter for INVESTMENT_TYPE - table Field
     */
    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    /**
     * Setter/Getter for INVESTMENT_PROD_ID - table Field
     */
    public void setInvestmentProdId(String investmentProdId) {
        this.investmentProdId = investmentProdId;
    }

    public String getInvestmentProdId() {
        return investmentProdId;
    }

    /**
     * Setter/Getter for INVESTMENT_PROD_DESC - table Field
     */
    public void setInvestmentProdDesc(String investmentProdDesc) {
        this.investmentProdDesc = investmentProdDesc;
    }

    public String getInvestmentProdDesc() {
        return investmentProdDesc;
    }

    /**
     * Setter/Getter for AGENCY_NAME - table Field
     */
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyName() {
        return agencyName;
    }

    /**
     * Setter/Getter for INVESTMENT_REF_NO - table Field
     */
    public void setInvestmentRefNo(String investmentRefNo) {
        this.investmentRefNo = investmentRefNo;
    }

    public String getInvestmentRefNo() {
        return investmentRefNo;
    }

    /**
     * Setter/Getter for INVESTMENT_ISSUE_DT - table Field
     */
    public void setInvestmentIssueDt(Date investmentIssueDt) {
        this.investmentIssueDt = investmentIssueDt;
    }

    public Date getInvestmentIssueDt() {
        return investmentIssueDt;
    }

    /**
     * Setter/Getter for AMOUNT - table Field
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
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

    public String getTxtBranchCode() {
        return txtBranchCode;
    }

    public void setTxtBranchCode(String txtBranchCode) {
        this.txtBranchCode = txtBranchCode;
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
        strB.append(getTOString("investmentId", investmentId));
        strB.append(getTOString("investmentType", investmentType));
        strB.append(getTOString("investmentProdId", investmentProdId));
        strB.append(getTOString("investmentProdDesc", investmentProdDesc));
        strB.append(getTOString("agencyName", agencyName));
        strB.append(getTOString("investmentRefNo", investmentRefNo));
        strB.append(getTOString("investmentIssueDt", investmentIssueDt));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("txtBranchCode", txtBranchCode));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("investmentId", investmentId));
        strB.append(getTOXml("investmentType", investmentType));
        strB.append(getTOXml("investmentProdId", investmentProdId));
        strB.append(getTOXml("investmentProdDesc", investmentProdDesc));
        strB.append(getTOXml("agencyName", agencyName));
        strB.append(getTOXml("investmentRefNo", investmentRefNo));
        strB.append(getTOXml("investmentIssueDt", investmentIssueDt));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("txtBranchCode", txtBranchCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}