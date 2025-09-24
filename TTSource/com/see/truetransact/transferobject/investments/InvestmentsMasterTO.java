/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductLoanTO.java
 *
 * Created on Tue Mar 15 14:08:12 IST 2005
 */
package com.see.truetransact.transferobject.investments;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_PROD_LOANS.
 */
public class InvestmentsMasterTO extends TransferObject implements Serializable {

    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus;
    private String authorizeBy;
    private Date authorizeDt;
    private String cboInvestmentBehaves = "";
    private String cboIntPayFreq = "";
    private String investmentID = "";
    private String investmentName = "";
    private Date issueDt = null;
    private Double years = null;
    private Double months = null;
    private Double days = null;
    private Date maturityDate = null;
    private Double faceValue = null;
    private Double couponRate = null;
    private String sLR = "";
    private String callOption = "";
    private String putOption = "";
    private String setUpOption = "";
    private Double availableNoOfUnits = null;
    private Date lastIntPaidDate = null;
    private Double totalPremiumPaid = null;
    private Double totalPremiumCollected = null;
    private Double totalInterestPaid = null;
    private Double totalInterestCollected = null;
    private String classification = "";
    private Date initiatedDate = null;
    private Double outstandingAmount = null;
    private Double maturityAmount = null;
    private Double putOptionNoofYears = null;
    private Double callOptionNoofYears = null;
    private Double setUpOptionNoofYears = null;
    private String txtBranchCode = "";
    private String txtBankCode = "";
    private String rdoSecurityType = "";
    private String cboSecurityTypeCode = "";
    private String txtRemarks = "";
    private String txtOtherName = "";
    private String preCloserRate = "";
    private String closerType = "";
    private String amortizationAmt = "";
    private Date closerDate = null;
    private String investmentStatus = "";

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
        strB.append(getTOString("cboInvestmentBehaves", cboInvestmentBehaves));
        strB.append(getTOString("investmentID", investmentID));
        strB.append(getTOString("investmentName", investmentName));
        strB.append(getTOString("issueDt", issueDt));
        strB.append(getTOString("years", years));
        strB.append(getTOString("months", months));
        strB.append(getTOString("days", days));
        strB.append(getTOString("maturityDate", maturityDate));
        strB.append(getTOString("faceValue", faceValue));
        strB.append(getTOString("couponRate", couponRate));
        strB.append(getTOString("sLR", sLR));
        strB.append(getTOString("callOption", callOption));
        strB.append(getTOString("putOption", putOption));
        strB.append(getTOString("setUpOption", setUpOption));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("totalPremiumPaid", totalPremiumPaid));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("availableNoOfUnits", availableNoOfUnits));
        strB.append(getTOString("lastIntPaidDate", lastIntPaidDate));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("totalPremiumCollected", totalPremiumCollected));
        strB.append(getTOString("totalInterestPaid", totalInterestPaid));
        strB.append(getTOString("totalInterestCollected", totalInterestCollected));
        strB.append(getTOString("classification", classification));
        strB.append(getTOString("initiatedDate", initiatedDate));
        strB.append(getTOString("txtBranchCode", txtBranchCode));
        strB.append(getTOString("txtBankCode", txtBankCode));
        strB.append(getTOString("rdoSecurityType", rdoSecurityType));
        strB.append(getTOString("cboSecurityTypeCode", cboSecurityTypeCode));
        strB.append(getTOString("txtRemarks", txtRemarks));
        strB.append(getTOString("txtOtherName", txtOtherName));
        strB.append(getTOString("preCloserRate", preCloserRate));
        strB.append(getTOString("closerType", closerType));
        strB.append(getTOString("amortizationAmt", amortizationAmt));
        strB.append(getTOString("closerDate", closerDate));
        strB.append(getTOString("investmentStatus", investmentStatus));

        strB.append(getTOStringEnd());
        return strB.toString();


    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("cboInvestmentBehaves", cboInvestmentBehaves));
        strB.append(getTOXml("investmentID", investmentID));
        strB.append(getTOXml("investmentName", investmentName));
        strB.append(getTOXml("issueDt", issueDt));
        strB.append(getTOXml("years", years));
        strB.append(getTOXml("months", months));
        strB.append(getTOXml("days", days));
        strB.append(getTOXml("maturityDate", maturityDate));
        strB.append(getTOXml("faceValue", faceValue));
        strB.append(getTOXml("couponRate", couponRate));
        strB.append(getTOXml("sLR", sLR));
        strB.append(getTOXml("callOption", callOption));
        strB.append(getTOXml("putOption", putOption));
        strB.append(getTOXml("setUpOption", setUpOption));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("availableNoOfUnits", availableNoOfUnits));
        strB.append(getTOXml("lastIntPaidDate", lastIntPaidDate));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("totalPremiumCollected", totalPremiumCollected));
        strB.append(getTOXml("totalInterestPaid", totalInterestPaid));
        strB.append(getTOXml("totalInterestCollected", totalInterestCollected));
        strB.append(getTOXml("classification", classification));
        strB.append(getTOXml("initiatedDate", initiatedDate));
        strB.append(getTOXml("txtBranchCode", txtBranchCode));
        strB.append(getTOXml("txtBankCode", txtBankCode));
        strB.append(getTOXml("rdoSecurityType", rdoSecurityType));
        strB.append(getTOXml("cboSecurityTypeCode", cboSecurityTypeCode));
        strB.append(getTOXml("txtRemarks", txtRemarks));
        strB.append(getTOXml("txtOtherName", txtOtherName));
        strB.append(getTOXml("preCloserRate", preCloserRate));
        strB.append(getTOXml("closerType", closerType));
        strB.append(getTOXml("amortizationAmt", amortizationAmt));
        strB.append(getTOXml("closerDate", closerDate));
        strB.append(getTOXml("investmentStatus", investmentStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
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
     * Getter for property cboInvestmentBehaves.
     *
     * @return Value of property cboInvestmentBehaves.
     */
    public java.lang.String getCboInvestmentBehaves() {
        return cboInvestmentBehaves;
    }

    /**
     * Setter for property cboInvestmentBehaves.
     *
     * @param cboInvestmentBehaves New value of property cboInvestmentBehaves.
     */
    public void setCboInvestmentBehaves(java.lang.String cboInvestmentBehaves) {
        this.cboInvestmentBehaves = cboInvestmentBehaves;
    }

    /**
     * Getter for property cboIntPayFreq.
     *
     * @return Value of property cboIntPayFreq.
     */
    public java.lang.String getCboIntPayFreq() {
        return cboIntPayFreq;
    }

    /**
     * Setter for property cboIntPayFreq.
     *
     * @param cboIntPayFreq New value of property cboIntPayFreq.
     */
    public void setCboIntPayFreq(java.lang.String cboIntPayFreq) {
        this.cboIntPayFreq = cboIntPayFreq;
    }

    /**
     * Getter for property investmentID.
     *
     * @return Value of property investmentID.
     */
    public java.lang.String getInvestmentID() {
        return investmentID;
    }

    /**
     * Setter for property investmentID.
     *
     * @param investmentID New value of property investmentID.
     */
    public void setInvestmentID(java.lang.String investmentID) {
        this.investmentID = investmentID;
    }

    /**
     * Getter for property investmentName.
     *
     * @return Value of property investmentName.
     */
    public java.lang.String getInvestmentName() {
        return investmentName;
    }

    /**
     * Setter for property investmentName.
     *
     * @param investmentName New value of property investmentName.
     */
    public void setInvestmentName(java.lang.String investmentName) {
        this.investmentName = investmentName;
    }

    /**
     * Getter for property issueDt.
     *
     * @return Value of property issueDt.
     */
    public java.util.Date getIssueDt() {
        return issueDt;
    }

    /**
     * Setter for property issueDt.
     *
     * @param issueDt New value of property issueDt.
     */
    public void setIssueDt(java.util.Date issueDt) {
        this.issueDt = issueDt;
    }

    /**
     *
     *
     *
     *
     * /**
     * Getter for property maturityDate.
     *
     * @return Value of property maturityDate.
     */
    public java.util.Date getMaturityDate() {
        return maturityDate;
    }

    /**
     * Setter for property maturityDate.
     *
     * @param maturityDate New value of property maturityDate.
     */
    public void setMaturityDate(java.util.Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    /**
     * Getter for property sLR.
     *
     * @return Value of property sLR.
     */
    public java.lang.String getSLR() {
        return sLR;
    }

    /**
     * Setter for property sLR.
     *
     * @param sLR New value of property sLR.
     */
    public void setSLR(java.lang.String sLR) {
        this.sLR = sLR;
    }

    /**
     * Getter for property callOption.
     *
     * @return Value of property callOption.
     */
    public java.lang.String getCallOption() {
        return callOption;
    }

    /**
     * Setter for property callOption.
     *
     * @param callOption New value of property callOption.
     */
    public void setCallOption(java.lang.String callOption) {
        this.callOption = callOption;
    }

    /**
     * Getter for property putOption.
     *
     * @return Value of property putOption.
     */
    public java.lang.String getPutOption() {
        return putOption;
    }

    /**
     * Setter for property putOption.
     *
     * @param putOption New value of property putOption.
     */
    public void setPutOption(java.lang.String putOption) {
        this.putOption = putOption;
    }

    /**
     * Getter for property setUpOption.
     *
     * @return Value of property setUpOption.
     */
    public java.lang.String getSetUpOption() {
        return setUpOption;
    }

    /**
     * Setter for property setUpOption.
     *
     * @param setUpOption New value of property setUpOption.
     */
    public void setSetUpOption(java.lang.String setUpOption) {
        this.setUpOption = setUpOption;
    }

    /**
     * Getter for property years.
     *
     * @return Value of property years.
     */
    public java.lang.Double getYears() {
        return years;
    }

    /**
     * Setter for property years.
     *
     * @param years New value of property years.
     */
    public void setYears(java.lang.Double years) {
        this.years = years;
    }

    /**
     * Getter for property months.
     *
     * @return Value of property months.
     */
    public java.lang.Double getMonths() {
        return months;
    }

    /**
     * Setter for property months.
     *
     * @param months New value of property months.
     */
    public void setMonths(java.lang.Double months) {
        this.months = months;
    }

    /**
     * Getter for property days.
     *
     * @return Value of property days.
     */
    public java.lang.Double getDays() {
        return days;
    }

    /**
     * Setter for property days.
     *
     * @param days New value of property days.
     */
    public void setDays(java.lang.Double days) {
        this.days = days;
    }

    /**
     * Getter for property faceValue.
     *
     * @return Value of property faceValue.
     */
    public java.lang.Double getFaceValue() {
        return faceValue;
    }

    /**
     * Setter for property faceValue.
     *
     * @param faceValue New value of property faceValue.
     */
    public void setFaceValue(java.lang.Double faceValue) {
        this.faceValue = faceValue;
    }

    /**
     * Getter for property couponRate.
     *
     * @return Value of property couponRate.
     */
    public java.lang.Double getCouponRate() {
        return couponRate;
    }

    /**
     * Setter for property couponRate.
     *
     * @param couponRate New value of property couponRate.
     */
    public void setCouponRate(java.lang.Double couponRate) {
        this.couponRate = couponRate;
    }

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    /**
     * Getter for property availableNoOfUnits.
     *
     * @return Value of property availableNoOfUnits.
     */
    public java.lang.Double getAvailableNoOfUnits() {
        return availableNoOfUnits;
    }

    /**
     * Setter for property availableNoOfUnits.
     *
     * @param availableNoOfUnits New value of property availableNoOfUnits.
     */
    public void setAvailableNoOfUnits(java.lang.Double availableNoOfUnits) {
        this.availableNoOfUnits = availableNoOfUnits;
    }

    /**
     * Getter for property lastIntPaidDate.
     *
     * @return Value of property lastIntPaidDate.
     */
    public java.util.Date getLastIntPaidDate() {
        return lastIntPaidDate;
    }

    /**
     * Setter for property lastIntPaidDate.
     *
     * @param lastIntPaidDate New value of property lastIntPaidDate.
     */
    public void setLastIntPaidDate(java.util.Date lastIntPaidDate) {
        this.lastIntPaidDate = lastIntPaidDate;
    }

    /**
     * Getter for property totalPremiumPaid.
     *
     * @return Value of property totalPremiumPaid.
     */
    public java.lang.Double getTotalPremiumPaid() {
        return totalPremiumPaid;
    }

    /**
     * Setter for property totalPremiumPaid.
     *
     * @param totalPremiumPaid New value of property totalPremiumPaid.
     */
    public void setTotalPremiumPaid(java.lang.Double totalPremiumPaid) {
        this.totalPremiumPaid = totalPremiumPaid;
    }

    /**
     * Getter for property totalPremiumCollected.
     *
     * @return Value of property totalPremiumCollected.
     */
    public java.lang.Double getTotalPremiumCollected() {
        return totalPremiumCollected;
    }

    /**
     * Setter for property totalPremiumCollected.
     *
     * @param totalPremiumCollected New value of property totalPremiumCollected.
     */
    public void setTotalPremiumCollected(java.lang.Double totalPremiumCollected) {
        this.totalPremiumCollected = totalPremiumCollected;
    }

    /**
     * Getter for property totalInterestPaid.
     *
     * @return Value of property totalInterestPaid.
     */
    public java.lang.Double getTotalInterestPaid() {
        return totalInterestPaid;
    }

    /**
     * Setter for property totalInterestPaid.
     *
     * @param totalInterestPaid New value of property totalInterestPaid.
     */
    public void setTotalInterestPaid(java.lang.Double totalInterestPaid) {
        this.totalInterestPaid = totalInterestPaid;
    }

    /**
     * Getter for property tTotalInterestCollected.
     *
     * @return Value of property tTotalInterestCollected.
     */
    public java.lang.Double getTotalInterestCollected() {
        return totalInterestCollected;
    }

    /**
     * Setter for property tTotalInterestCollected.
     *
     * @param tTotalInterestCollected New value of property
     * tTotalInterestCollected.
     */
    public void setTotalInterestCollected(java.lang.Double tTotalInterestCollected) {
        this.totalInterestCollected = tTotalInterestCollected;
    }

    /**
     * Getter for property classification.
     *
     * @return Value of property classification.
     */
    public java.lang.String getClassification() {
        return classification;
    }

    /**
     * Setter for property classification.
     *
     * @param classification New value of property classification.
     */
    public void setClassification(java.lang.String classification) {
        this.classification = classification;
    }

    /**
     * Getter for property initiatedDate.
     *
     * @return Value of property initiatedDate.
     */
    public java.util.Date getInitiatedDate() {
        return initiatedDate;
    }

    /**
     * Setter for property initiatedDate.
     *
     * @param initiatedDate New value of property initiatedDate.
     */
    public void setInitiatedDate(java.util.Date initiatedDate) {
        this.initiatedDate = initiatedDate;
    }

    /**
     * Getter for property outstandingAmount.
     *
     * @return Value of property outstandingAmount.
     */
    public java.lang.Double getOutstandingAmount() {
        return outstandingAmount;
    }

    /**
     * Setter for property outstandingAmount.
     *
     * @param outstandingAmount New value of property outstandingAmount.
     */
    public void setOutstandingAmount(java.lang.Double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    /**
     * Getter for property maturityAmount.
     *
     * @return Value of property maturityAmount.
     */
    public java.lang.Double getMaturityAmount() {
        return maturityAmount;
    }

    /**
     * Setter for property maturityAmount.
     *
     * @param maturityAmount New value of property maturityAmount.
     */
    public void setMaturityAmount(java.lang.Double maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    /**
     * Getter for property putOptionNoofYears.
     *
     * @return Value of property putOptionNoofYears.
     */
    public java.lang.Double getPutOptionNoofYears() {
        return putOptionNoofYears;
    }

    /**
     * Setter for property putOptionNoofYears.
     *
     * @param putOptionNoofYears New value of property putOptionNoofYears.
     */
    public void setPutOptionNoofYears(java.lang.Double putOptionNoofYears) {
        this.putOptionNoofYears = putOptionNoofYears;
    }

    /**
     * Getter for property callOptionNoofYears.
     *
     * @return Value of property callOptionNoofYears.
     */
    public java.lang.Double getCallOptionNoofYears() {
        return callOptionNoofYears;
    }

    /**
     * Setter for property callOptionNoofYears.
     *
     * @param callOptionNoofYears New value of property callOptionNoofYears.
     */
    public void setCallOptionNoofYears(java.lang.Double callOptionNoofYears) {
        this.callOptionNoofYears = callOptionNoofYears;
    }

    /**
     * Getter for property setUpOptionNoofYears.
     *
     * @return Value of property setUpOptionNoofYears.
     */
    public java.lang.Double getSetUpOptionNoofYears() {
        return setUpOptionNoofYears;
    }

    /**
     * Setter for property setUpOptionNoofYears.
     *
     * @param setUpOptionNoofYears New value of property setUpOptionNoofYears.
     */
    public void setSetUpOptionNoofYears(java.lang.Double setUpOptionNoofYears) {
        this.setUpOptionNoofYears = setUpOptionNoofYears;
    }

    /**
     * Getter for property txtBranchCode.
     *
     * @return Value of property txtBranchCode.
     */
    public java.lang.String getTxtBranchCode() {
        return txtBranchCode;
    }

    /**
     * Setter for property txtBranchCode.
     *
     * @param txtBranchCode New value of property txtBranchCode.
     */
    public void setTxtBranchCode(java.lang.String txtBranchCode) {
        this.txtBranchCode = txtBranchCode;
    }

    /**
     * Getter for property txtBankCode.
     *
     * @return Value of property txtBankCode.
     */
    public java.lang.String getTxtBankCode() {
        return txtBankCode;
    }

    /**
     * Setter for property txtBankCode.
     *
     * @param txtBankCode New value of property txtBankCode.
     */
    public void setTxtBankCode(java.lang.String txtBankCode) {
        this.txtBankCode = txtBankCode;
    }

    /**
     * Getter for property rdoSecurityType.
     *
     * @return Value of property rdoSecurityType.
     */
    public java.lang.String getRdoSecurityType() {
        return rdoSecurityType;
    }

    /**
     * Setter for property rdoSecurityType.
     *
     * @param rdoSecurityType New value of property rdoSecurityType.
     */
    public void setRdoSecurityType(java.lang.String rdoSecurityType) {
        this.rdoSecurityType = rdoSecurityType;
    }

    /**
     * Getter for property rdoSecurityTypeCode.
     *
     * @return Value of property rdoSecurityTypeCode.
     */
    /**
     * Getter for property txtRemarks.
     *
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }

    /**
     * Setter for property txtRemarks.
     *
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    /**
     * Getter for property cboSecurityTypeCode.
     *
     * @return Value of property cboSecurityTypeCode.
     */
    public java.lang.String getCboSecurityTypeCode() {
        return cboSecurityTypeCode;
    }

    /**
     * Setter for property cboSecurityTypeCode.
     *
     * @param cboSecurityTypeCode New value of property cboSecurityTypeCode.
     */
    public void setCboSecurityTypeCode(java.lang.String cboSecurityTypeCode) {
        this.cboSecurityTypeCode = cboSecurityTypeCode;
    }

    /**
     * Getter for property txtOtherName.
     *
     * @return Value of property txtOtherName.
     */
    public java.lang.String getTxtOtherName() {
        return txtOtherName;
    }

    /**
     * Setter for property txtOtherName.
     *
     * @param txtOtherName New value of property txtOtherName.
     */
    public void setTxtOtherName(java.lang.String txtOtherName) {
        this.txtOtherName = txtOtherName;
    }

    /**
     * Getter for property preCloserRate.
     *
     * @return Value of property preCloserRate.
     */
    public java.lang.String getPreCloserRate() {
        return preCloserRate;
    }

    /**
     * Setter for property preCloserRate.
     *
     * @param preCloserRate New value of property preCloserRate.
     */
    public void setPreCloserRate(java.lang.String preCloserRate) {
        this.preCloserRate = preCloserRate;
    }

    /**
     * Getter for property closerType.
     *
     * @return Value of property closerType.
     */
    public java.lang.String getCloserType() {
        return closerType;
    }

    /**
     * Setter for property closerType.
     *
     * @param closerType New value of property closerType.
     */
    public void setCloserType(java.lang.String closerType) {
        this.closerType = closerType;
    }

    /**
     * Getter for property amortizationAmt.
     *
     * @return Value of property amortizationAmt.
     */
    public java.lang.String getAmortizationAmt() {
        return amortizationAmt;
    }

    /**
     * Setter for property amortizationAmt.
     *
     * @param amortizationAmt New value of property amortizationAmt.
     */
    public void setAmortizationAmt(java.lang.String amortizationAmt) {
        this.amortizationAmt = amortizationAmt;
    }

    /**
     * Getter for property closerDate.
     *
     * @return Value of property closerDate.
     */
    public java.util.Date getCloserDate() {
        return closerDate;
    }

    /**
     * Setter for property closerDate.
     *
     * @param closerDate New value of property closerDate.
     */
    public void setCloserDate(java.util.Date closerDate) {
        this.closerDate = closerDate;
    }

    /**
     * Getter for property investmentStatus.
     *
     * @return Value of property investmentStatus.
     */
    public java.lang.String getInvestmentStatus() {
        return investmentStatus;
    }

    /**
     * Setter for property investmentStatus.
     *
     * @param investmentStatus New value of property investmentStatus.
     */
    public void setInvestmentStatus(java.lang.String investmentStatus) {
        this.investmentStatus = investmentStatus;
    }
}