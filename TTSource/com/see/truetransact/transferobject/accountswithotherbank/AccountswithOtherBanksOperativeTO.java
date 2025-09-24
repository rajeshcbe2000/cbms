/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InvestmentsOperativeTO.java
 * 
 * Created on Tue Feb 21 10:45:22 IST 2012
 */
package com.see.truetransact.transferobject.accountswithotherbank;

import com.see.truetransact.transferobject.investments.*;
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is INVESTMENT_OPERATIVE.
 */
public class AccountswithOtherBanksOperativeTO extends TransferObject implements Serializable {

    private String investmentId = "";
    private String investmentType = "";
    private String investmentProdId = "";
    private String investmentProdDesc = "";
    private String agencyName = "";
    private String investmentRefNo = "";
    private Date investmentIssueDt = null;
    private String operatorDetails = "";
    private String checkAllowed = "";
    private String status = "";
    private String statusBy = "";
    private String branchCode = "";
    private Date statusDt = null;
    private String authorizedStatus = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private Double depositAmt = null;
    private String sanctnNo = "";
    private Double penalRate = null;
    private Double intRate = null;
    private Date last_trans_dt = null;
    private String act_status = "";
    private Double sanctnAmt = null;
    private Date expDt = null;
    private Double amt = null;
    private Double amtC = null;
    private Date closedDt = null;

    public Date getClosedDt() {
        return closedDt;
    }

    public void setClosedDt(Date closedDt) {
        this.closedDt = closedDt;
    }

    public Double getAmtC() {
        return amtC;
    }

    public void setAmtC(Double amtC) {
        this.amtC = amtC;
    }

    public Date getExpDt() {
        return expDt;
    }

    public void setExpDt(Date expDt) {
        this.expDt = expDt;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Double getSanctnAmt() {
        return sanctnAmt;
    }

    public void setSanctnAmt(Double sanctnAmt) {
        this.sanctnAmt = sanctnAmt;
    }

    public String getAct_status() {
        return act_status;
    }

    public void setAct_status(String act_status) {
        this.act_status = act_status;
    }

    public Double getIntRate() {
        return intRate;
    }

    public void setIntRate(Double intRate) {
        this.intRate = intRate;
    }

    public Date getLast_trans_dt() {
        return last_trans_dt;
    }

    public void setLast_trans_dt(Date last_trans_dt) {
        this.last_trans_dt = last_trans_dt;
    }

    public Double getPenalRate() {
        return penalRate;
    }

    public void setPenalRate(Double penalRate) {
        this.penalRate = penalRate;
    }

    public String getSanctnNo() {
        return sanctnNo;
    }

    public void setSanctnNo(String sanctnNo) {
        this.sanctnNo = sanctnNo;
    }

    public Double getDepositAmt() {
        return depositAmt;
    }

    public void setDepositAmt(Double depositAmt) {
        this.depositAmt = depositAmt;
    }

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
     * Setter/Getter for OPERATOR_DETAILS - table Field
     */
    public void setOperatorDetails(String operatorDetails) {
        this.operatorDetails = operatorDetails;
    }

    public String getOperatorDetails() {
        return operatorDetails;
    }

    /**
     * Setter/Getter for CHECK_ALLOWED - table Field
     */
    public void setCheckAllowed(String checkAllowed) {
        this.checkAllowed = checkAllowed;
    }

    public String getCheckAllowed() {
        return checkAllowed;
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

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
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
        strB.append(getTOString("operatorDetails", operatorDetails));
        strB.append(getTOString("checkAllowed", checkAllowed));
        strB.append(getTOString("depositAmt", depositAmt));
        strB.append(getTOString("sanctnNo", sanctnNo));
        strB.append(getTOString("penalRate", penalRate));
        strB.append(getTOString("intRate", intRate));
        strB.append(getTOString("last_trans_dt", last_trans_dt));
        strB.append(getTOString("act_status", act_status));
        strB.append(getTOString("sanctnAmt", sanctnAmt));
        strB.append(getTOString("amt", amt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
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
        strB.append(getTOXml("operatorDetails", operatorDetails));
        strB.append(getTOXml("checkAllowed", checkAllowed));
        strB.append(getTOXml("depositAmt", depositAmt));
        strB.append(getTOXml("sanctnNo", sanctnNo));
        strB.append(getTOXml("penalRate", penalRate));
        strB.append(getTOXml("intRate", intRate));
        strB.append(getTOXml("last_trans_dt", last_trans_dt));
        strB.append(getTOXml("act_status", act_status));
        strB.append(getTOXml("sanctnAmt", sanctnAmt));
        strB.append(getTOXml("amt", amt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}