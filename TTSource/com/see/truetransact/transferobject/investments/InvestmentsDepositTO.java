/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InvestmentsDepositTO.java
 * 
 * Created on Tue Feb 21 10:49:20 IST 2012
 */
package com.see.truetransact.transferobject.investments;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is INVESTMENT_DEPOSIT.
 */
public class InvestmentsDepositTO extends TransferObject implements Serializable {

    private String investmentId = "";
    private String investmentType = "";
    private String investmentProdId = "";
    private String investmentProdDesc = "";
    private String agencyName = "";
    private String investmentRefNo = "";
    private Date investmentIssueDt = null;
    private Date investmentEffectiveDt = null;
    private Double principalAmount = null;
    private Double investmentPeriodYy = null;
    private Double investmentPeriodMm = null;
    private Double investmentPeriodDd = null;
    private Double rateOfInterest = null;
    private Date maturityDt = null;
    private Double maturityAmount = null;
    private Double intpayFreq = null;
    private Double interestReceivable = null;
    private Double interestReceived = null;
    private Double periodicIntrest = null;
    private Date intRecTillDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedStatus = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String withPrincipal = "";
    private String renewal = "";
    private String renewalSameNo = "";
    private String renewalNewNo = "";
    private String renewalDiffProdNo = "";
    private String renewalWithInterest = "";
    private String renewalWithOutInterest = "";
    private String renewalPartialInterest = "";
    private Double renewalPartialAmt = null;
    private String withInterest = "";
    private String txtBranchCode = "";
    private Date renewalDt = null;

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
     * Setter/Getter for INVESTMENT_EFFECTIVE_DT - table Field
     */
    public void setInvestmentEffectiveDt(Date investmentEffectiveDt) {
        this.investmentEffectiveDt = investmentEffectiveDt;
    }

    public Date getInvestmentEffectiveDt() {
        return investmentEffectiveDt;
    }

    /**
     * Setter/Getter for PRINCIPAL_AMOUNT - table Field
     */
    public void setPrincipalAmount(Double principalAmount) {
        this.principalAmount = principalAmount;
    }

    public Double getPrincipalAmount() {
        return principalAmount;
    }

    /**
     * Setter/Getter for INVESTMENT_PERIOD_YY - table Field
     */
    public void setInvestmentPeriodYy(Double investmentPeriodYy) {
        this.investmentPeriodYy = investmentPeriodYy;
    }

    public Double getInvestmentPeriodYy() {
        return investmentPeriodYy;
    }

    /**
     * Setter/Getter for INVESTMENT_PERIOD_MM - table Field
     */
    public void setInvestmentPeriodMm(Double investmentPeriodMm) {
        this.investmentPeriodMm = investmentPeriodMm;
    }

    public Double getInvestmentPeriodMm() {
        return investmentPeriodMm;
    }

    /**
     * Setter/Getter for INVESTMENT_PERIOD_DD - table Field
     */
    public void setInvestmentPeriodDd(Double investmentPeriodDd) {
        this.investmentPeriodDd = investmentPeriodDd;
    }

    public Double getInvestmentPeriodDd() {
        return investmentPeriodDd;
    }

    /**
     * Setter/Getter for RATE_OF_INTEREST - table Field
     */
    public void setRateOfInterest(Double rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    public Double getRateOfInterest() {
        return rateOfInterest;
    }

    /**
     * Setter/Getter for MATURITY_DT - table Field
     */
    public void setMaturityDt(Date maturityDt) {
        this.maturityDt = maturityDt;
    }

    public Date getMaturityDt() {
        return maturityDt;
    }

    /**
     * Setter/Getter for MATURITY_AMOUNT - table Field
     */
    public void setMaturityAmount(Double maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    public Double getMaturityAmount() {
        return maturityAmount;
    }

    /**
     * Setter/Getter for INTPAY_FREQ - table Field
     */
    public void setIntpayFreq(Double intpayFreq) {
        this.intpayFreq = intpayFreq;
    }

    public Double getIntpayFreq() {
        return intpayFreq;
    }

    /**
     * Setter/Getter for INTEREST_RECEIVABLE - table Field
     */
    public void setInterestReceivable(Double interestReceivable) {
        this.interestReceivable = interestReceivable;
    }

    public Double getInterestReceivable() {
        return interestReceivable;
    }

    /**
     * Setter/Getter for INTEREST_RECEIVED - table Field
     */
    public void setInterestReceived(Double interestReceived) {
        this.interestReceived = interestReceived;
    }

    public Double getInterestReceived() {
        return interestReceived;
    }

    /**
     * Setter/Getter for INT_REC_TILL_DT - table Field
     */
    public void setIntRecTillDt(Date intRecTillDt) {
        this.intRecTillDt = intRecTillDt;
    }

    public Date getIntRecTillDt() {
        return intRecTillDt;
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
        strB.append(getTOString("investmentEffectiveDt", investmentEffectiveDt));
        strB.append(getTOString("principalAmount", principalAmount));
        strB.append(getTOString("investmentPeriodYy", investmentPeriodYy));
        strB.append(getTOString("investmentPeriodMm", investmentPeriodMm));
        strB.append(getTOString("investmentPeriodDd", investmentPeriodDd));
        strB.append(getTOString("rateOfInterest", rateOfInterest));
        strB.append(getTOString("maturityDt", maturityDt));
        strB.append(getTOString("maturityAmount", maturityAmount));
        strB.append(getTOString("intpayFreq", intpayFreq));
        strB.append(getTOString("interestReceivable", interestReceivable));
        strB.append(getTOString("interestReceived", interestReceived));
        strB.append(getTOString("periodicIntrest", periodicIntrest));
        strB.append(getTOString("intRecTillDt", intRecTillDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("withPrincipal", withPrincipal));
        strB.append(getTOString("renewal", renewal));
        strB.append(getTOString("renewalSameNo", renewalSameNo));
        strB.append(getTOString("renewalWithInterest", renewalWithInterest));
        strB.append(getTOString("renewalWithOutInterest", renewalWithOutInterest));
        strB.append(getTOString("renewalPartialInterest", renewalPartialInterest));
        strB.append(getTOString("renewalPartialAmt", renewalPartialAmt));
        strB.append(getTOString("renewalNewNo", renewalNewNo));
        strB.append(getTOString("renewalDiffProdNo", renewalDiffProdNo));
        strB.append(getTOString("withInterest", withInterest));
        strB.append(getTOString("txtBranchCode", txtBranchCode));
        strB.append(getTOString("renewalDt", renewalDt));
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
        strB.append(getTOXml("investmentEffectiveDt", investmentEffectiveDt));
        strB.append(getTOXml("principalAmount", principalAmount));
        strB.append(getTOXml("investmentPeriodYy", investmentPeriodYy));
        strB.append(getTOXml("investmentPeriodMm", investmentPeriodMm));
        strB.append(getTOXml("investmentPeriodDd", investmentPeriodDd));
        strB.append(getTOXml("rateOfInterest", rateOfInterest));
        strB.append(getTOXml("maturityDt", maturityDt));
        strB.append(getTOXml("maturityAmount", maturityAmount));
        strB.append(getTOXml("intpayFreq", intpayFreq));
        strB.append(getTOXml("interestReceivable", interestReceivable));
        strB.append(getTOXml("interestReceived", interestReceived));
        strB.append(getTOXml("periodicIntrest", periodicIntrest));
        strB.append(getTOXml("intRecTillDt", intRecTillDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("withPrincipal", withPrincipal));
        strB.append(getTOXml("renewal", renewal));
        strB.append(getTOXml("renewalSameNo", renewalSameNo));
        strB.append(getTOXml("renewalNewNo", renewalNewNo));
        strB.append(getTOXml("renewalDiffProdNo", renewalDiffProdNo));
        strB.append(getTOXml("renewalWithInterest", renewalWithInterest));
        strB.append(getTOXml("renewalWithOutInterest", renewalWithOutInterest));
        strB.append(getTOXml("renewalPartialInterest", renewalPartialInterest));
        strB.append(getTOXml("renewalPartialAmt", renewalPartialAmt));
        strB.append(getTOXml("withInterest", withInterest));
        strB.append(getTOXml("txtBranchCode", txtBranchCode));
        strB.append(getTOXml("renewalDt", renewalDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property withPrincipal.
     *
     * @return Value of property withPrincipal.
     */
    public java.lang.String getWithPrincipal() {
        return withPrincipal;
    }

    /**
     * Setter for property withPrincipal.
     *
     * @param withPrincipal New value of property withPrincipal.
     */
    public void setWithPrincipal(java.lang.String withPrincipal) {
        this.withPrincipal = withPrincipal;
    }

    /**
     * Getter for property renewal.
     *
     * @return Value of property renewal.
     */
    public java.lang.String getRenewal() {
        return renewal;
    }

    /**
     * Setter for property renewal.
     *
     * @param renewal New value of property renewal.
     */
    public void setRenewal(java.lang.String renewal) {
        this.renewal = renewal;
    }

    /**
     * Getter for property withInterest.
     *
     * @return Value of property withInterest.
     */
    public java.lang.String getWithInterest() {
        return withInterest;
    }

    /**
     * Setter for property withInterest.
     *
     * @param withInterest New value of property withInterest.
     */
    public void setWithInterest(java.lang.String withInterest) {
        this.withInterest = withInterest;
    }

    /**
     * Getter for property periodicIntrest.
     *
     * @return Value of property periodicIntrest.
     */
    public java.lang.Double getPeriodicIntrest() {
        return periodicIntrest;
    }

    /**
     * Setter for property periodicIntrest.
     *
     * @param periodicIntrest New value of property periodicIntrest.
     */
    public void setPeriodicIntrest(java.lang.Double periodicIntrest) {
        this.periodicIntrest = periodicIntrest;
    }

    /**
     * Getter for property renewalSameNo.
     *
     * @return Value of property renewalSameNo.
     */
    public java.lang.String getRenewalSameNo() {
        return renewalSameNo;
    }

    /**
     * Setter for property renewalSameNo.
     *
     * @param renewalSameNo New value of property renewalSameNo.
     */
    public void setRenewalSameNo(java.lang.String renewalSameNo) {
        this.renewalSameNo = renewalSameNo;
    }

    /**
     * Getter for property renewalNewNo.
     *
     * @return Value of property renewalNewNo.
     */
    public java.lang.String getRenewalNewNo() {
        return renewalNewNo;
    }

    /**
     * Setter for property renewalNewNo.
     *
     * @param renewalNewNo New value of property renewalNewNo.
     */
    public void setRenewalNewNo(java.lang.String renewalNewNo) {
        this.renewalNewNo = renewalNewNo;
    }

    /**
     * Getter for property renewalDiffProdNo.
     *
     * @return Value of property renewalDiffProdNo.
     */
    public java.lang.String getRenewalDiffProdNo() {
        return renewalDiffProdNo;
    }

    /**
     * Setter for property renewalDiffProdNo.
     *
     * @param renewalDiffProdNo New value of property renewalDiffProdNo.
     */
    public void setRenewalDiffProdNo(java.lang.String renewalDiffProdNo) {
        this.renewalDiffProdNo = renewalDiffProdNo;
    }

    /**
     * Getter for property renewalWithInterest.
     *
     * @return Value of property renewalWithInterest.
     */
    public java.lang.String getRenewalWithInterest() {
        return renewalWithInterest;
    }

    /**
     * Setter for property renewalWithInterest.
     *
     * @param renewalWithInterest New value of property renewalWithInterest.
     */
    public void setRenewalWithInterest(java.lang.String renewalWithInterest) {
        this.renewalWithInterest = renewalWithInterest;
    }

    /**
     * Getter for property renewalWithOutInterest.
     *
     * @return Value of property renewalWithOutInterest.
     */
    public java.lang.String getRenewalWithOutInterest() {
        return renewalWithOutInterest;
    }

    /**
     * Setter for property renewalWithOutInterest.
     *
     * @param renewalWithOutInterest New value of property
     * renewalWithOutInterest.
     */
    public void setRenewalWithOutInterest(java.lang.String renewalWithOutInterest) {
        this.renewalWithOutInterest = renewalWithOutInterest;
    }

    /**
     * Getter for property renewalPartialInterest.
     *
     * @return Value of property renewalPartialInterest.
     */
    public java.lang.String getRenewalPartialInterest() {
        return renewalPartialInterest;
    }

    /**
     * Setter for property renewalPartialInterest.
     *
     * @param renewalPartialInterest New value of property
     * renewalPartialInterest.
     */
    public void setRenewalPartialInterest(java.lang.String renewalPartialInterest) {
        this.renewalPartialInterest = renewalPartialInterest;
    }

    /**
     * Getter for property renewalPartialAmt.
     *
     * @return Value of property renewalPartialAmt.
     */
    public java.lang.Double getRenewalPartialAmt() {
        return renewalPartialAmt;
    }

    /**
     * Setter for property renewalPartialAmt.
     *
     * @param renewalPartialAmt New value of property renewalPartialAmt.
     */
    public void setRenewalPartialAmt(java.lang.Double renewalPartialAmt) {
        this.renewalPartialAmt = renewalPartialAmt;
    }

    public Date getRenewalDt() {
        return renewalDt;
    }

    public void setRenewalDt(Date renewalDt) {
        this.renewalDt = renewalDt;
    }
    
}