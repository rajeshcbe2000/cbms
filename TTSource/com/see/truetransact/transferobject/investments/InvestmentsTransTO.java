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
public class InvestmentsTransTO extends TransferObject implements Serializable {

    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String investmentBehaves = "";
    private String investmentID = "";
    private String investmentName = "";
    private Double prematureROI = null;
    private Double prematureInt = null;
    private String batchID = null;
    private Date transDT = null;
    private String transType = "";
    private String trnCode = "";
    private String investment_internal_Id = "";
    private String investment_Ref_No = "";
    private String cboInvestmentType = "";
    private String txtInvestmentIDTransSBorCA = "";
    private String txtInvestmentRefNoTrans = "";
    private String txtInvestmentInternalNoTrans = "";
    private String txtChequeNo = "";
    private String rdoSBorCA = "";
    private Double amount = null;
    private Date purchaseDt = null;
    private String purchaseMode = "";
    private String purchaseThrough = "";
    private String brokerName = "";
    private Double purchaseRate = null;
    private Double noOfUnits = null;
    private Double investmentAmount = null;
    private Double discountAmount = null;
    private Double premiumAmount = null;
    private Double brokerCommession = null;
    private Double brokenPeriodInterest = null;
    private Double dividendAmount = null;
    private Date lastIntPaidDate = null;
    private String purchaseSaleBy = "";
    private String initiatedBranch = "";
    private String narration = "";
    private String crParticulars = "";
    private Double investTDS=0.0;
    private String principalEntryWithoutTransaction = "";

    public Double getInvestTDS() {
        return investTDS;
    }

    public void setInvestTDS(Double investTDS) {
        this.investTDS = investTDS;
    }

    public String getCrParticulars() {
        return crParticulars;
    }

    public void setCrParticulars(String crParticulars) {
        this.crParticulars = crParticulars;
    }

    public String getDrParticulars() {
        return drParticulars;
    }

    public void setDrParticulars(String drParticulars) {
        this.drParticulars = drParticulars;
    }
    private String drParticulars = "";
    private Double invAvailableBal = null;
    private String closingType = "";
    private String interestType = "";

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
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("investmentBehaves", investmentBehaves));
        strB.append(getTOString("investmentID", investmentID));
        strB.append(getTOString("investmentName", investmentName));
        strB.append(getTOString("prematureROI", prematureROI));
        strB.append(getTOString("prematureInt", prematureInt));
        strB.append(getTOString("batchID", batchID));
        strB.append(getTOString("transDT", transDT));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("trnCode", trnCode));
        strB.append(getTOString("investment_internal_Id", investment_internal_Id));
        strB.append(getTOString("cboInvestmentType", cboInvestmentType));
        strB.append(getTOString("txtInvestmentIDTransSBorCA", txtInvestmentIDTransSBorCA));
        strB.append(getTOString("txtInvestmentRefNoTrans", txtInvestmentRefNoTrans));
        strB.append(getTOString("txtInvestmentInternalNoTrans", txtInvestmentInternalNoTrans));
        strB.append(getTOString("txtChequeNo", txtChequeNo));
        strB.append(getTOString("narration", narration));
        strB.append(getTOString("rdoSBorCA", rdoSBorCA));
        strB.append(getTOString("investment_Ref_No", investment_Ref_No));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("purchaseDt", purchaseDt));
        strB.append(getTOString("purchaseMode", purchaseMode));
        strB.append(getTOString("purchaseThrough", purchaseThrough));
        strB.append(getTOString("brokerName", brokerName));
        strB.append(getTOString("purchaseRate", purchaseRate));
        strB.append(getTOString("noOfUnits", noOfUnits));
        strB.append(getTOString("investmentAmount", investmentAmount));
        strB.append(getTOString("discountAmount", discountAmount));
        strB.append(getTOString("premiumAmount", premiumAmount));
        strB.append(getTOString("brokerCommession", brokerCommession));
        strB.append(getTOString("brokenPeriodInterest", brokenPeriodInterest));
        strB.append(getTOString("dividendAmount", dividendAmount));
        strB.append(getTOString("lastIntPaidDate", lastIntPaidDate));
        strB.append(getTOString("purchaseSaleBy", purchaseSaleBy));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("invAvailableBal", invAvailableBal));
        strB.append(getTOString("closingType", closingType));
        strB.append(getTOString("interestType", interestType));
        strB.append(getTOString("investTDS", investTDS));
        strB.append(getTOString("principalEntryWithoutTransaction", principalEntryWithoutTransaction));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("investmentBehaves", investmentBehaves));
        strB.append(getTOXml("investmentID", investmentID));
        strB.append(getTOXml("investmentName", investmentName));
        strB.append(getTOXml("prematureROI", prematureROI));
        strB.append(getTOXml("prematureInt", prematureInt));
        strB.append(getTOXml("batchID", batchID));
        strB.append(getTOXml("transDT", transDT));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("trnCode", trnCode));
        strB.append(getTOXml("investment_internal_Id", investment_internal_Id));
        strB.append(getTOXml("cboInvestmentType", cboInvestmentType));
        strB.append(getTOXml("txtInvestmentIDTransSBorCA", txtInvestmentIDTransSBorCA));
        strB.append(getTOXml("txtInvestmentRefNoTrans", txtInvestmentRefNoTrans));
        strB.append(getTOXml("txtInvestmentInternalNoTrans", txtInvestmentInternalNoTrans));
        strB.append(getTOXml("txtChequeNo", txtChequeNo));
        strB.append(getTOXml("narration", narration));
        strB.append(getTOXml("rdoSBorCA", rdoSBorCA));
        strB.append(getTOXml("investment_Ref_No", investment_Ref_No));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("purchaseDt", purchaseDt));
        strB.append(getTOXml("purchaseMode", purchaseMode));
        strB.append(getTOXml("purchaseThrough", purchaseThrough));
        strB.append(getTOXml("brokerName", brokerName));
        strB.append(getTOXml("purchaseRate", purchaseRate));
        strB.append(getTOXml("noOfUnits", noOfUnits));
        strB.append(getTOXml("investmentAmount", investmentAmount));
        strB.append(getTOXml("discountAmount", discountAmount));
        strB.append(getTOXml("premiumAmount", premiumAmount));
        strB.append(getTOXml("brokerCommession", brokerCommession));
        strB.append(getTOXml("brokenPeriodInterest", brokenPeriodInterest));
        strB.append(getTOXml("dividendAmount", dividendAmount));
        strB.append(getTOXml("lastIntPaidDate", lastIntPaidDate));
        strB.append(getTOXml("purchaseSaleBy", purchaseSaleBy));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("invAvailableBal", invAvailableBal));
        strB.append(getTOXml("closingType", closingType));
        strB.append(getTOXml("interestType", interestType));
        strB.append(getTOXml("investTDS", investTDS));
        strB.append(getTOXml("principalEntryWithoutTransaction", principalEntryWithoutTransaction));
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
     * Getter for property investmentBehaves.
     *
     * @return Value of property investmentBehaves.
     */
    public java.lang.String getInvestmentBehaves() {
        return investmentBehaves;
    }

    /**
     * Setter for property investmentBehaves.
     *
     * @param investmentBehaves New value of property investmentBehaves.
     */
    public void setInvestmentBehaves(java.lang.String investmentBehaves) {
        this.investmentBehaves = investmentBehaves;
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
     * Getter for property batchID.
     *
     * @return Value of property batchID.
     */
    public java.lang.String getBatchID() {
        return batchID;
    }

    /**
     * Setter for property batchID.
     *
     * @param batchID New value of property batchID.
     */
    public void setBatchID(java.lang.String batchID) {
        this.batchID = batchID;
    }

    /**
     * Getter for property transDT.
     *
     * @return Value of property transDT.
     */
    public java.util.Date getTransDT() {
        return transDT;
    }

    /**
     * Setter for property transDT.
     *
     * @param transDT New value of property transDT.
     */
    public void setTransDT(java.util.Date transDT) {
        this.transDT = transDT;
    }

    /**
     * Getter for property transType.
     *
     * @return Value of property transType.
     */
    public java.lang.String getTransType() {
        return transType;
    }

    /**
     * Setter for property transType.
     *
     * @param transType New value of property transType.
     */
    public void setTransType(java.lang.String transType) {
        this.transType = transType;
    }

    /**
     * Getter for property TrnCode.
     *
     * @return Value of property TrnCode.
     */
    public java.lang.String getTrnCode() {
        return trnCode;
    }

    /**
     * Setter for property TrnCode.
     *
     * @param TrnCode New value of property TrnCode.
     */
    public void setTrnCode(java.lang.String trnCode) {
        this.trnCode = trnCode;
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     */
    public java.lang.Double getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     */
    public void setAmount(java.lang.Double amount) {
        this.amount = amount;
    }

    //    /**
    //     * Getter for property purchaseDt.
    //     * @return Value of property purchaseDt.
    //     */
    //    public java.lang.Double getPurchaseDt() {
    //        return purchaseDt;
    //    }
    //
    //    /**
    //     * Setter for property purchaseDt.
    //     * @param purchaseDt New value of property purchaseDt.
    //     */
    //    public void setPurchaseDt(java.lang.Double purchaseDt) {
    //        this.purchaseDt = purchaseDt;
    //    }
    /**
     * Getter for property purchaseMode.
     *
     * @return Value of property purchaseMode.
     */
    public java.lang.String getPurchaseMode() {
        return purchaseMode;
    }

    /**
     * Setter for property purchaseMode.
     *
     * @param purchaseMode New value of property purchaseMode.
     */
    public void setPurchaseMode(java.lang.String purchaseMode) {
        this.purchaseMode = purchaseMode;
    }

    /**
     * Getter for property purchaseThrough.
     *
     * @return Value of property purchaseThrough.
     */
    public java.lang.String getPurchaseThrough() {
        return purchaseThrough;
    }

    /**
     * Setter for property purchaseThrough.
     *
     * @param purchaseThrough New value of property purchaseThrough.
     */
    public void setPurchaseThrough(java.lang.String purchaseThrough) {
        this.purchaseThrough = purchaseThrough;
    }

    /**
     * Getter for property brokerName.
     *
     * @return Value of property brokerName.
     */
    public java.lang.String getBrokerName() {
        return brokerName;
    }

    /**
     * Setter for property brokerName.
     *
     * @param brokerName New value of property brokerName.
     */
    public void setBrokerName(java.lang.String brokerName) {
        this.brokerName = brokerName;
    }

    /**
     * Getter for property purchaseRate.
     *
     * @return Value of property purchaseRate.
     */
    public java.lang.Double getPurchaseRate() {
        return purchaseRate;
    }

    /**
     * Setter for property purchaseRate.
     *
     * @param purchaseRate New value of property purchaseRate.
     */
    public void setPurchaseRate(java.lang.Double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    /**
     * Getter for property noOfUnits.
     *
     * @return Value of property noOfUnits.
     */
    public java.lang.Double getNoOfUnits() {
        return noOfUnits;
    }

    /**
     * Setter for property noOfUnits.
     *
     * @param noOfUnits New value of property noOfUnits.
     */
    public void setNoOfUnits(java.lang.Double noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    /**
     * Getter for property investmentAmount.
     *
     * @return Value of property investmentAmount.
     */
    public java.lang.Double getInvestmentAmount() {
        return investmentAmount;
    }

    /**
     * Setter for property investmentAmount.
     *
     * @param investmentAmount New value of property investmentAmount.
     */
    public void setInvestmentAmount(java.lang.Double investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    /**
     * Getter for property discountAmount.
     *
     * @return Value of property discountAmount.
     */
    public java.lang.Double getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Setter for property discountAmount.
     *
     * @param discountAmount New value of property discountAmount.
     */
    public void setDiscountAmount(java.lang.Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     * Getter for property premiumAmount.
     *
     * @return Value of property premiumAmount.
     */
    public java.lang.Double getPremiumAmount() {
        return premiumAmount;
    }

    /**
     * Setter for property premiumAmount.
     *
     * @param premiumAmount New value of property premiumAmount.
     */
    public void setPremiumAmount(java.lang.Double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    /**
     * Getter for property brokerCommession.
     *
     * @return Value of property brokerCommession.
     */
    public java.lang.Double getBrokerCommession() {
        return brokerCommession;
    }

    /**
     * Setter for property brokerCommession.
     *
     * @param brokerCommession New value of property brokerCommession.
     */
    public void setBrokerCommession(java.lang.Double brokerCommession) {
        this.brokerCommession = brokerCommession;
    }

    /**
     * Getter for property brokenPeriodInterest.
     *
     * @return Value of property brokenPeriodInterest.
     */
    public java.lang.Double getBrokenPeriodInterest() {
        return brokenPeriodInterest;
    }

    /**
     * Setter for property brokenPeriodInterest.
     *
     * @param brokenPeriodInterest New value of property brokenPeriodInterest.
     */
    public void setBrokenPeriodInterest(java.lang.Double brokenPeriodInterest) {
        this.brokenPeriodInterest = brokenPeriodInterest;
    }

    /**
     * Getter for property dividendAmount.
     *
     * @return Value of property dividendAmount.
     */
    public java.lang.Double getDividendAmount() {
        return dividendAmount;
    }

    /**
     * Setter for property dividendAmount.
     *
     * @param dividendAmount New value of property dividendAmount.
     */
    public void setDividendAmount(java.lang.Double dividendAmount) {
        this.dividendAmount = dividendAmount;
    }

    /**
     * Getter for property purchaseDt.
     *
     * @return Value of property purchaseDt.
     */
    public java.util.Date getPurchaseDt() {
        return purchaseDt;
    }

    /**
     * Setter for property purchaseDt.
     *
     * @param purchaseDt New value of property purchaseDt.
     */
    public void setPurchaseDt(java.util.Date purchaseDt) {
        this.purchaseDt = purchaseDt;
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
     * Getter for property purchaseSaleBy.
     *
     * @return Value of property purchaseSaleBy.
     */
    public java.lang.String getPurchaseSaleBy() {
        return purchaseSaleBy;
    }

    /**
     * Setter for property purchaseSaleBy.
     *
     * @param purchaseSaleBy New value of property purchaseSaleBy.
     */
    public void setPurchaseSaleBy(java.lang.String purchaseSaleBy) {
        this.purchaseSaleBy = purchaseSaleBy;
    }

    /**
     * Getter for property initiatedBranch.
     *
     * @return Value of property initiatedBranch.
     */
    public java.lang.String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * Setter for property initiatedBranch.
     *
     * @param initiatedBranch New value of property initiatedBranch.
     */
    public void setInitiatedBranch(java.lang.String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }

    /**
     * Getter for property investment_Ref_No.
     *
     * @return Value of property investment_Ref_No.
     */
    public java.lang.String getInvestment_Ref_No() {
        return investment_Ref_No;
    }

    /**
     * Setter for property investment_Ref_No.
     *
     * @param investment_Ref_No New value of property investment_Ref_No.
     */
    public void setInvestment_Ref_No(java.lang.String investment_Ref_No) {
        this.investment_Ref_No = investment_Ref_No;
    }

    /**
     * Getter for property investment_internal_Id.
     *
     * @return Value of property investment_internal_Id.
     */
    public java.lang.String getInvestment_internal_Id() {
        return investment_internal_Id;
    }

    /**
     * Setter for property investment_internal_Id.
     *
     * @param investment_internal_Id New value of property
     * investment_internal_Id.
     */
    public void setInvestment_internal_Id(java.lang.String investment_internal_Id) {
        this.investment_internal_Id = investment_internal_Id;
    }

    /**
     * Getter for property cboInvestmentType.
     *
     * @return Value of property cboInvestmentType.
     */
    public java.lang.String getCboInvestmentType() {
        return cboInvestmentType;
    }

    /**
     * Setter for property cboInvestmentType.
     *
     * @param cboInvestmentType New value of property cboInvestmentType.
     */
    public void setCboInvestmentType(java.lang.String cboInvestmentType) {
        this.cboInvestmentType = cboInvestmentType;
    }

    /**
     * Getter for property txtInvestmentIDTransSBorCA.
     *
     * @return Value of property txtInvestmentIDTransSBorCA.
     */
    public java.lang.String getTxtInvestmentIDTransSBorCA() {
        return txtInvestmentIDTransSBorCA;
    }

    /**
     * Setter for property txtInvestmentIDTransSBorCA.
     *
     * @param txtInvestmentIDTransSBorCA New value of property
     * txtInvestmentIDTransSBorCA.
     */
    public void setTxtInvestmentIDTransSBorCA(java.lang.String txtInvestmentIDTransSBorCA) {
        this.txtInvestmentIDTransSBorCA = txtInvestmentIDTransSBorCA;
    }

    /**
     * Getter for property txtInvestmentRefNoTrans.
     *
     * @return Value of property txtInvestmentRefNoTrans.
     */
    public java.lang.String getTxtInvestmentRefNoTrans() {
        return txtInvestmentRefNoTrans;
    }

    /**
     * Setter for property txtInvestmentRefNoTrans.
     *
     * @param txtInvestmentRefNoTrans New value of property
     * txtInvestmentRefNoTrans.
     */
    public void setTxtInvestmentRefNoTrans(java.lang.String txtInvestmentRefNoTrans) {
        this.txtInvestmentRefNoTrans = txtInvestmentRefNoTrans;
    }

    /**
     * Getter for property txtInvestmentInternalNoTrans.
     *
     * @return Value of property txtInvestmentInternalNoTrans.
     */
    public java.lang.String getTxtInvestmentInternalNoTrans() {
        return txtInvestmentInternalNoTrans;
    }

    /**
     * Setter for property txtInvestmentInternalNoTrans.
     *
     * @param txtInvestmentInternalNoTrans New value of property
     * txtInvestmentInternalNoTrans.
     */
    public void setTxtInvestmentInternalNoTrans(java.lang.String txtInvestmentInternalNoTrans) {
        this.txtInvestmentInternalNoTrans = txtInvestmentInternalNoTrans;
    }

    /**
     * Getter for property rdoSBorCA.
     *
     * @return Value of property rdoSBorCA.
     */
    public java.lang.String getRdoSBorCA() {
        return rdoSBorCA;
    }

    /**
     * Setter for property rdoSBorCA.
     *
     * @param rdoSBorCA New value of property rdoSBorCA.
     */
    public void setRdoSBorCA(java.lang.String rdoSBorCA) {
        this.rdoSBorCA = rdoSBorCA;
    }

    /**
     * Getter for property txtChequeNo.
     *
     * @return Value of property txtChequeNo.
     */
    public java.lang.String getTxtChequeNo() {
        return txtChequeNo;
    }

    /**
     * Setter for property txtChequeNo.
     *
     * @param txtChequeNo New value of property txtChequeNo.
     */
    public void setTxtChequeNo(java.lang.String txtChequeNo) {
        this.txtChequeNo = txtChequeNo;
    }

    /**
     * Getter for property narration.
     *
     * @return Value of property narration.
     */
    public java.lang.String getNarration() {
        return narration;
    }

    /**
     * Setter for property narration.
     *
     * @param narration New value of property narration.
     */
    public void setNarration(java.lang.String narration) {
        this.narration = narration;
    }

    /**
     * Getter for property invAvailableBal.
     *
     * @return Value of property invAvailableBal.
     */
    public java.lang.Double getInvAvailableBal() {
        return invAvailableBal;
    }

    /**
     * Setter for property invAvailableBal.
     *
     * @param invAvailableBal New value of property invAvailableBal.
     */
    public void setInvAvailableBal(java.lang.Double invAvailableBal) {
        this.invAvailableBal = invAvailableBal;
    }

    public Double getPrematureROI() {
        return prematureROI;
    }

    public void setPrematureROI(Double prematureROI) {
        this.prematureROI = prematureROI;
    }

    public String getClosingType() {
        return closingType;
    }

    public void setClosingType(String closingType) {
        this.closingType = closingType;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public Double getPrematureInt() {
        return prematureInt;
    }

    public void setPrematureInt(Double prematureInt) {
        this.prematureInt = prematureInt;
    }
    
    public String getPrincipalEntryWithoutTransaction() {
        return principalEntryWithoutTransaction;
    }

    public void setPrincipalEntryWithoutTransaction(String principalEntryWithoutTransaction) {
        this.principalEntryWithoutTransaction = principalEntryWithoutTransaction;
    }
}