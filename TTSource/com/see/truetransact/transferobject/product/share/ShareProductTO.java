/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductTO.java
 * 
 * Created on Mon Apr 11 14:45:52 IST 2005
 */
package com.see.truetransact.transferobject.product.share;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_CONF_DETAILS.
 */
public class ShareProductTO extends TransferObject implements Serializable {

    private String shareType = "";
    private Double faceValue = null;
    private Double issuedCapital = null;
    private Double subscribedCapital = null;
    private Double paidupCapital = null;
    private Double withdrawalRestriction = null;
    private Double maximumShare = null;
    private Double admissionFee = null;
    private Double applicationFee = null;
    private Double shareFee = null;
    private String requiredAccountHolder = "";
    private String considerRecovery = "";
    private String dividendCalcFrequency = "";
//        added by nikhil
    private String cboDivCalcFrequencyNew = "";
    private String txtMinDividendAmount = "";
    private String cboDivCalcType = "";
    private String cboDividendRounding = "";
    private String rdoRatification = "";
    private String txtIncomeAccountHead = "";
    private String dividendApplFrequency = "";
    private Double unclaimedDividendPeriod = null;
//         Added by Nikhil
    private String unclaimedDividendPeriodType = "";
//        added by nikhil for Share Subsidy
    private String chkSubsidyForSCST = "";
    private String txtCustomerShare = "";
    private String txtGovernmentShare = "";
    private String txtGovtSubsidyAccountHead = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String branchId = "";
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private Double lockupRefund = null;
    private Double lockupAdditionalShare = null;
    private Double allowedNominee = null;
    private Date lastDividendCalc = null;
    private Date nextDueDate = null;
    private Double percentageDividend = null;
    private Double nominalMemPeriod = null;
    private String shareSuspenseAchd = "";
    private String dividendPaymentAchd = "";
    private String shareAchd = "";
    private String membershipFeeAchd = "";
    private String applicationFeeAchd = "";
    private String minIntialShares = "";
    private String shareFeeAchd = "";
    private String txtDivPayableAcct = "";
    private String txtDivPaidAcct = "";
    private Date lstRunDate = null;
    private Date unclaimedRunDate = null;
    private Date unclaimedTransferDate = null;
    private String unClaimedDivTransferAcHd = "";
    private Double txtAdmissionFeeMin = null;
    private Double txtAdmissionFeeMax = null;
    private String rdoAdmissionFeeType = "";
    private String numPatternPrefix = "";
    private String numPatternSuffix = "";
    private String isOutstandingReq = "";
    private String mandatoryData = "";
    private String mandatoryAddrData = "";
    private Double pensionAge = null;
    private Double shareRunPeriod = null;
    private Double minPension = null;
    private String pensionDebitProdType = "";
    private String pensionDebitProdId = "";
    private String pensionDebitAccount = "";
    private String shareCertificate = "";
    private String rdoFullClosure= "";
    private String drfAllowed = "";

    public String getShareCertificate() {
        return shareCertificate;
    }

    public void setShareCertificate(String shareCertificate) {
        this.shareCertificate = shareCertificate;
    }

    public Double getMinPension() {
        return minPension;
    }

    public void setMinPension(Double minPension) {
        this.minPension = minPension;
    }

    public Double getPensionAge() {
        return pensionAge;
    }

    public void setPensionAge(Double pensionAge) {
        this.pensionAge = pensionAge;
    }

    public String getPensionDebitAccount() {
        return pensionDebitAccount;
    }

    public void setPensionDebitAccount(String pensionDebitAccount) {
        this.pensionDebitAccount = pensionDebitAccount;
    }

    public String getPensionDebitProdId() {
        return pensionDebitProdId;
    }

    public void setPensionDebitProdId(String pensionDebitProdId) {
        this.pensionDebitProdId = pensionDebitProdId;
    }

    public String getPensionDebitProdType() {
        return pensionDebitProdType;
    }

    public void setPensionDebitProdType(String pensionDebitProdType) {
        this.pensionDebitProdType = pensionDebitProdType;
    }

    public Double getShareRunPeriod() {
        return shareRunPeriod;
    }

    public void setShareRunPeriod(Double shareRunPeriod) {
        this.shareRunPeriod = shareRunPeriod;
    }
    
    public String getIsOutstandingReq() {
        return isOutstandingReq;
    }

    public void setIsOutstandingReq(String fieldval) {
        this.isOutstandingReq = fieldval;
    }

    public String getMandatoryAddrData() {
        return mandatoryAddrData;
    }

    public void setMandatoryAddrData(String mandatoryAddrData) {
        this.mandatoryAddrData = mandatoryAddrData;
    }

    /**
     * Setter/Getter for SHARE_TYPE - table Field
     */
    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getShareType() {
        return shareType;
    }

    /**
     * Setter/Getter for FACE_VALUE - table Field
     */
    public void setFaceValue(Double faceValue) {
        this.faceValue = faceValue;
    }

    public Double getFaceValue() {
        return faceValue;
    }

    public String getMandatoryData() {
        return mandatoryData;
    }

    public void setMandatoryData(String mandatoryData) {
        this.mandatoryData = mandatoryData;
    }

  

   

    /**
     * Setter/Getter for ISSUED_CAPITAL - table Field
     */
    public void setIssuedCapital(Double issuedCapital) {
        this.issuedCapital = issuedCapital;
    }

    public Double getIssuedCapital() {
        return issuedCapital;
    }

    /**
     * Setter/Getter for SUBSCRIBED_CAPITAL - table Field
     */
    public void setSubscribedCapital(Double subscribedCapital) {
        this.subscribedCapital = subscribedCapital;
    }

    public Double getSubscribedCapital() {
        return subscribedCapital;
    }

    /**
     * Setter/Getter for PAIDUP_CAPITAL - table Field
     */
    public void setPaidupCapital(Double paidupCapital) {
        this.paidupCapital = paidupCapital;
    }

    public Double getPaidupCapital() {
        return paidupCapital;
    }

    /**
     * Setter/Getter for WITHDRAWAL_RESTRICTION - table Field
     */
    public void setWithdrawalRestriction(Double withdrawalRestriction) {
        this.withdrawalRestriction = withdrawalRestriction;
    }

    public Double getWithdrawalRestriction() {
        return withdrawalRestriction;
    }

    /**
     * Setter/Getter for MAXIMUM_SHARE - table Field
     */
    public void setMaximumShare(Double maximumShare) {
        this.maximumShare = maximumShare;
    }

    public Double getMaximumShare() {
        return maximumShare;
    }

    /**
     * Setter/Getter for ADMISSION_FEE - table Field
     */
    public void setAdmissionFee(Double admissionFee) {
        this.admissionFee = admissionFee;
    }

    public Double getAdmissionFee() {
        return admissionFee;
    }

    /**
     * Setter/Getter for APPLICATION_FEE - table Field
     */
    public void setApplicationFee(Double applicationFee) {
        this.applicationFee = applicationFee;
    }

    public Double getApplicationFee() {
        return applicationFee;
    }

    /**
     * Setter/Getter for SHARE_FEE - table Field
     */
    public void setShareFee(Double shareFee) {
        this.shareFee = shareFee;
    }

    public Double getShareFee() {
        return shareFee;
    }

    /**
     * Setter/Getter for REQUIRED_ACCOUNT_HOLDER - table Field
     */
    public void setRequiredAccountHolder(String requiredAccountHolder) {
        this.requiredAccountHolder = requiredAccountHolder;
    }

    public String getRequiredAccountHolder() {
        return requiredAccountHolder;
    }

    /**
     * Setter/Getter for DIVIDEND_CALC_FREQUENCY - table Field
     */
    public void setDividendCalcFrequency(String dividendCalcFrequency) {
        this.dividendCalcFrequency = dividendCalcFrequency;
    }

    public String getDividendCalcFrequency() {
        return dividendCalcFrequency;
    }

    /**
     * Setter/Getter for DIVIDEND_APPL_FREQUENCY - table Field
     */
    public void setDividendApplFrequency(String dividendApplFrequency) {
        this.dividendApplFrequency = dividendApplFrequency;
    }

    public String getDividendApplFrequency() {
        return dividendApplFrequency;
    }

    /**
     * Setter/Getter for UNCLAIMED_DIVIDEND_PERIOD - table Field
     */
    public void setUnclaimedDividendPeriod(Double unclaimedDividendPeriod) {
        this.unclaimedDividendPeriod = unclaimedDividendPeriod;
    }

    public Double getUnclaimedDividendPeriod() {
        return unclaimedDividendPeriod;
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
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
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

    /**
     * Setter/Getter for LOCKUP_REFUND - table Field
     */
    public void setLockupRefund(Double lockupRefund) {
        this.lockupRefund = lockupRefund;
    }

    public Double getLockupRefund() {
        return lockupRefund;
    }

    /**
     * Setter/Getter for LOCKUP_ADDITIONAL_SHARE - table Field
     */
    public void setLockupAdditionalShare(Double lockupAdditionalShare) {
        this.lockupAdditionalShare = lockupAdditionalShare;
    }

    public Double getLockupAdditionalShare() {
        return lockupAdditionalShare;
    }

    /**
     * Setter/Getter for ALLOWED_NOMINEE - table Field
     */
    public void setAllowedNominee(Double allowedNominee) {
        this.allowedNominee = allowedNominee;
    }

    public Double getAllowedNominee() {
        return allowedNominee;
    }

    /**
     * Setter/Getter for LAST_DIVIDEND_CALC - table Field
     */
    public void setLastDividendCalc(Date lastDividendCalc) {
        this.lastDividendCalc = lastDividendCalc;
    }

    public Date getLastDividendCalc() {
        return lastDividendCalc;
    }

    /**
     * Setter/Getter for NEXT_DUE_DATE - table Field
     */
    public void setNextDueDate(Date nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public Date getNextDueDate() {
        return nextDueDate;
    }

    /**
     * Setter/Getter for PERCENTAGE_DIVIDEND - table Field
     */
    public void setPercentageDividend(Double percentageDividend) {
        this.percentageDividend = percentageDividend;
    }

    public Double getPercentageDividend() {
        return percentageDividend;
    }

    /**
     * Setter/Getter for NOMINAL_MEM_PERIOD - table Field
     */
    public void setNominalMemPeriod(Double nominalMemPeriod) {
        this.nominalMemPeriod = nominalMemPeriod;
    }

    public Double getNominalMemPeriod() {
        return nominalMemPeriod;
    }

    /**
     * Setter/Getter for SHARE_SUSPENSE_ACHD - table Field
     */
    public void setShareSuspenseAchd(String shareSuspenseAchd) {
        this.shareSuspenseAchd = shareSuspenseAchd;
    }

    public String getShareSuspenseAchd() {
        return shareSuspenseAchd;
    }

    /**
     * Setter/Getter for DIVIDEND_PAYMENT_ACHD - table Field
     */
    public void setDividendPaymentAchd(String dividendPaymentAchd) {
        this.dividendPaymentAchd = dividendPaymentAchd;
    }

    public String getDividendPaymentAchd() {
        return dividendPaymentAchd;
    }

    /**
     * Setter/Getter for SHARE_ACHD - table Field
     */
    public void setShareAchd(String shareAchd) {
        this.shareAchd = shareAchd;
    }

    public String getShareAchd() {
        return shareAchd;
    }

    /**
     * Setter/Getter for MEMBERSHIP_FEE_ACHD - table Field
     */
    public void setMembershipFeeAchd(String membershipFeeAchd) {
        this.membershipFeeAchd = membershipFeeAchd;
    }

    public String getMembershipFeeAchd() {
        return membershipFeeAchd;
    }

    /**
     * Setter/Getter for APPLICATION_FEE_ACHD - table Field
     */
    public void setApplicationFeeAchd(String applicationFeeAchd) {
        this.applicationFeeAchd = applicationFeeAchd;
    }

    public String getApplicationFeeAchd() {
        return applicationFeeAchd;
    }

    public void setMinIntialShares(String minIntialShares) {
        this.minIntialShares = minIntialShares;
    }

    public String getMinIntialShares() {
        return minIntialShares;
    }

    /**
     * Setter/Getter for SHARE_FEE_ACHD - table Field
     */
    public void setShareFeeAchd(String shareFeeAchd) {
        this.shareFeeAchd = shareFeeAchd;
    }

    public String getShareFeeAchd() {
        return shareFeeAchd;
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
        strB.append(getTOString("shareType", shareType));
        strB.append(getTOString("faceValue", faceValue));
        strB.append(getTOString("issuedCapital", issuedCapital));
        strB.append(getTOString("subscribedCapital", subscribedCapital));
        strB.append(getTOString("paidupCapital", paidupCapital));
        strB.append(getTOString("withdrawalRestriction", withdrawalRestriction));
        strB.append(getTOString("maximumShare", maximumShare));
        strB.append(getTOString("admissionFee", admissionFee));
        strB.append(getTOString("txtAdmissionFeeMin", txtAdmissionFeeMin));
        strB.append(getTOString("txtAdmissionFeeMax", txtAdmissionFeeMax));
        strB.append(getTOString("rdoAdmissionFeeType", rdoAdmissionFeeType));
        strB.append(getTOString("applicationFee", applicationFee));
        strB.append(getTOString("shareFee", shareFee));
        strB.append(getTOString("requiredAccountHolder", requiredAccountHolder));
        strB.append(getTOString("considerRecovery", considerRecovery));
        strB.append(getTOString("dividendCalcFrequency", dividendCalcFrequency));
//                changed by nikhil
        strB.append(getTOString("cboDivCalcFrequencyNew", cboDivCalcFrequencyNew));
        strB.append(getTOString("txtMinDividendAmount", txtMinDividendAmount));
        strB.append(getTOString("cboDivCalcType", cboDivCalcType));
        strB.append(getTOString("cboDividendRounding", cboDividendRounding));
        strB.append(getTOString("dividendApplFrequency", dividendApplFrequency));
        strB.append(getTOString("unclaimedDividendPeriod", unclaimedDividendPeriod));
        strB.append(getTOString("unclaimedDividendPeriodType", unclaimedDividendPeriodType));
        strB.append(getTOString("chkSubsidyForSCST", chkSubsidyForSCST));
        strB.append(getTOString("txtCustomerShare", txtCustomerShare));
        strB.append(getTOString("txtGovernmentShare", txtGovernmentShare));
        strB.append(getTOString("txtGovtSubsidyAccountHead", txtGovtSubsidyAccountHead));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("lockupRefund", lockupRefund));
        strB.append(getTOString("lockupAdditionalShare", lockupAdditionalShare));
        strB.append(getTOString("allowedNominee", allowedNominee));
        strB.append(getTOString("lastDividendCalc", lastDividendCalc));
        strB.append(getTOString("nextDueDate", nextDueDate));
        strB.append(getTOString("percentageDividend", percentageDividend));
        strB.append(getTOString("nominalMemPeriod", nominalMemPeriod));
        strB.append(getTOString("shareSuspenseAchd", shareSuspenseAchd));
        strB.append(getTOString("dividendPaymentAchd", dividendPaymentAchd));
        strB.append(getTOString("shareAchd", shareAchd));
        strB.append(getTOString("membershipFeeAchd", membershipFeeAchd));
        strB.append(getTOString("applicationFeeAchd", applicationFeeAchd));
        strB.append(getTOString("minIntialShares", minIntialShares));
        strB.append(getTOString("shareFeeAchd", shareFeeAchd));
        strB.append(getTOString("txtDivPayableAcct", txtDivPayableAcct));
        strB.append(getTOString("txtDivPaidAcct", txtDivPaidAcct));
        strB.append(getTOString("unClaimedDivTransferAcHd", unClaimedDivTransferAcHd));
        strB.append(getTOString("rdoRatification", rdoRatification));
        strB.append(getTOString("txtIncomeAccountHead", txtIncomeAccountHead));
        strB.append(getTOString("numPatternPrefix", numPatternPrefix));
        strB.append(getTOString("numPatternSuffix", numPatternSuffix));
        strB.append(getTOString("mandatoryData", mandatoryData));
        strB.append(getTOString("mandatoryAddrData", mandatoryAddrData));
        strB.append(getTOString("pensionAge", pensionAge));
        strB.append(getTOString("shareRunPeriod", shareRunPeriod));
        strB.append(getTOString("minPension", minPension));
        strB.append(getTOString("pensionDebitProdType", pensionDebitProdType));
        strB.append(getTOString("pensionDebitProdId", pensionDebitProdId));
        strB.append(getTOString("pensionDebitAccount", pensionDebitAccount));
        strB.append(getTOString("shareCertificate", shareCertificate));
        strB.append(getTOString("rdoFullClosure", rdoFullClosure));
        strB.append(getTOString("drfAllowed", drfAllowed));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("shareType", shareType));
        strB.append(getTOXml("faceValue", faceValue));
        strB.append(getTOXml("issuedCapital", issuedCapital));
        strB.append(getTOXml("subscribedCapital", subscribedCapital));
        strB.append(getTOXml("paidupCapital", paidupCapital));
        strB.append(getTOXml("withdrawalRestriction", withdrawalRestriction));
        strB.append(getTOXml("maximumShare", maximumShare));
        strB.append(getTOXml("admissionFee", admissionFee));
        strB.append(getTOXml("txtAdmissionFeeMin", txtAdmissionFeeMin));
        strB.append(getTOXml("txtAdmissionFeeMax", txtAdmissionFeeMax));
        strB.append(getTOXml("rdoAdmissionFeeType", rdoAdmissionFeeType));
        strB.append(getTOXml("applicationFee", applicationFee));
        strB.append(getTOXml("shareFee", shareFee));
        strB.append(getTOXml("requiredAccountHolder", requiredAccountHolder));
        strB.append(getTOXml("considerRecovery", considerRecovery));
        strB.append(getTOXml("dividendCalcFrequency", dividendCalcFrequency));
//                changed by nikhil
        strB.append(getTOXml("cboDivCalcFrequencyNew", cboDivCalcFrequencyNew));
        strB.append(getTOXml("txtMinDividendAmount", txtMinDividendAmount));
        strB.append(getTOXml("cboDivCalcType", cboDivCalcType));
        strB.append(getTOXml("cboDividendRounding", cboDividendRounding));
        strB.append(getTOXml("dividendApplFrequency", dividendApplFrequency));
        strB.append(getTOXml("unclaimedDividendPeriod", unclaimedDividendPeriod));
        strB.append(getTOXml("unclaimedDividendPeriodType", unclaimedDividendPeriodType));
        strB.append(getTOXml("chkSubsidyForSCST", chkSubsidyForSCST));
        strB.append(getTOXml("txtCustomerShare", txtCustomerShare));
        strB.append(getTOXml("txtGovernmentShare", txtGovernmentShare));
        strB.append(getTOXml("txtGovtSubsidyAccountHead", txtGovtSubsidyAccountHead));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("lockupRefund", lockupRefund));
        strB.append(getTOXml("lockupAdditionalShare", lockupAdditionalShare));
        strB.append(getTOXml("allowedNominee", allowedNominee));
        strB.append(getTOXml("lastDividendCalc", lastDividendCalc));
        strB.append(getTOXml("nextDueDate", nextDueDate));
        strB.append(getTOXml("percentageDividend", percentageDividend));
        strB.append(getTOXml("nominalMemPeriod", nominalMemPeriod));
        strB.append(getTOXml("shareSuspenseAchd", shareSuspenseAchd));
        strB.append(getTOXml("dividendPaymentAchd", dividendPaymentAchd));
        strB.append(getTOXml("shareAchd", shareAchd));
        strB.append(getTOXml("membershipFeeAchd", membershipFeeAchd));
        strB.append(getTOXml("applicationFeeAchd", applicationFeeAchd));
        strB.append(getTOXml("minIntialShares", minIntialShares));
        strB.append(getTOXml("shareFeeAchd", shareFeeAchd));
        strB.append(getTOXml("txtDivPayableAcct", txtDivPayableAcct));
        strB.append(getTOXml("txtDivPaidAcct", txtDivPaidAcct));
        strB.append(getTOXml("unClaimedDivTransferAcHd", unClaimedDivTransferAcHd));
        strB.append(getTOXml("rdoRatification", rdoRatification));
        strB.append(getTOXml("txtIncomeAccountHead", txtIncomeAccountHead));
        strB.append(getTOXml("numPatternPrefix", numPatternPrefix));
        strB.append(getTOXml("numPatternSuffix", numPatternSuffix));
        strB.append(getTOXml("mandatoryData", mandatoryData));
        strB.append(getTOXml("mandatoryAddrData", mandatoryAddrData));
        strB.append(getTOXml("pensionAge", pensionAge));
        strB.append(getTOXml("shareRunPeriod", shareRunPeriod));
        strB.append(getTOXml("minPension", minPension));
        strB.append(getTOXml("pensionDebitProdType", pensionDebitProdType));
        strB.append(getTOXml("pensionDebitProdId", pensionDebitProdId));
        strB.append(getTOXml("pensionDebitAccount", pensionDebitAccount));
        strB.append(getTOXml("shareCertificate", shareCertificate));
        strB.append(getTOXml("rdoFullClosure", rdoFullClosure));
        strB.append(getTOXml("drfAllowed", drfAllowed));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property txtDivPayableAcct.
     *
     * @return Value of property txtDivPayableAcct.
     */
    public java.lang.String getTxtDivPayableAcct() {
        return txtDivPayableAcct;
    }

    /**
     * Setter for property txtDivPayableAcct.
     *
     * @param txtDivPayableAcct New value of property txtDivPayableAcct.
     */
    public void setTxtDivPayableAcct(java.lang.String txtDivPayableAcct) {
        this.txtDivPayableAcct = txtDivPayableAcct;
    }

    /**
     * Getter for property txtDivPaidAcct.
     *
     * @return Value of property txtDivPaidAcct.
     */
    public java.lang.String getTxtDivPaidAcct() {
        return txtDivPaidAcct;
    }

    /**
     * Setter for property txtDivPaidAcct.
     *
     * @param txtDivPaidAcct New value of property txtDivPaidAcct.
     */
    public void setTxtDivPaidAcct(java.lang.String txtDivPaidAcct) {
        this.txtDivPaidAcct = txtDivPaidAcct;
    }

    /**
     * Getter for property lstRunDate.
     *
     * @return Value of property lstRunDate.
     */
    public java.util.Date getLstRunDate() {
        return lstRunDate;
    }

    /**
     * Setter for property lstRunDate.
     *
     * @param lstRunDate New value of property lstRunDate.
     */
    public void setLstRunDate(java.util.Date lstRunDate) {
        this.lstRunDate = lstRunDate;
    }

    /**
     * Getter for property unclaimedRunDate.
     *
     * @return Value of property unclaimedRunDate.
     */
    public java.util.Date getUnclaimedRunDate() {
        return unclaimedRunDate;
    }

    /**
     * Setter for property unclaimedRunDate.
     *
     * @param unclaimedRunDate New value of property unclaimedRunDate.
     */
    public void setUnclaimedRunDate(java.util.Date unclaimedRunDate) {
        this.unclaimedRunDate = unclaimedRunDate;
    }

    /**
     * Getter for property unclaimedTransferDate.
     *
     * @return Value of property unclaimedTransferDate.
     */
    public java.util.Date getUnclaimedTransferDate() {
        return unclaimedTransferDate;
    }

    /**
     * Setter for property unclaimedTransferDate.
     *
     * @param unclaimedTransferDate New value of property unclaimedTransferDate.
     */
    public void setUnclaimedTransferDate(java.util.Date unclaimedTransferDate) {
        this.unclaimedTransferDate = unclaimedTransferDate;
    }

    /**
     * Getter for property unClaimedDivTransferAcHd.
     *
     * @return Value of property unClaimedDivTransferAcHd.
     */
    public java.lang.String getUnClaimedDivTransferAcHd() {
        return unClaimedDivTransferAcHd;
    }

    /**
     * Setter for property unClaimedDivTransferAcHd.
     *
     * @param unClaimedDivTransferAcHd New value of property
     * unClaimedDivTransferAcHd.
     */
    public void setUnClaimedDivTransferAcHd(java.lang.String unClaimedDivTransferAcHd) {
        this.unClaimedDivTransferAcHd = unClaimedDivTransferAcHd;
    }

    /**
     * Getter for property txtAdmissionFeeMin.
     *
     * @return Value of property txtAdmissionFeeMin.
     */
    public java.lang.Double getTxtAdmissionFeeMin() {
        return txtAdmissionFeeMin;
    }

    /**
     * Setter for property txtAdmissionFeeMin.
     *
     * @param txtAdmissionFeeMin New value of property txtAdmissionFeeMin.
     */
    public void setTxtAdmissionFeeMin(java.lang.Double txtAdmissionFeeMin) {
        this.txtAdmissionFeeMin = txtAdmissionFeeMin;
    }

    /**
     * Getter for property txtAdmissionFeeMax.
     *
     * @return Value of property txtAdmissionFeeMax.
     */
    public java.lang.Double getTxtAdmissionFeeMax() {
        return txtAdmissionFeeMax;
    }

    /**
     * Setter for property txtAdmissionFeeMax.
     *
     * @param txtAdmissionFeeMax New value of property txtAdmissionFeeMax.
     */
    public void setTxtAdmissionFeeMax(java.lang.Double txtAdmissionFeeMax) {
        this.txtAdmissionFeeMax = txtAdmissionFeeMax;
    }

    /**
     * Getter for property rdoAdmissionFeeType.
     *
     * @return Value of property rdoAdmissionFeeType.
     */
    public java.lang.String getRdoAdmissionFeeType() {
        return rdoAdmissionFeeType;
    }

    /**
     * Setter for property rdoAdmissionFeeType.
     *
     * @param rdoAdmissionFeeType New value of property rdoAdmissionFeeType.
     */
    public void setRdoAdmissionFeeType(java.lang.String rdoAdmissionFeeType) {
        this.rdoAdmissionFeeType = rdoAdmissionFeeType;
    }

    public java.lang.String getTxtIncomeAccountHead() {
        return txtIncomeAccountHead;
    }

    /**
     * Setter for property rdoAdmissionFeeType.
     *
     * @param rdoAdmissionFeeType New value of property rdoAdmissionFeeType.
     */
    public void setTxtIncomeAccountHead(java.lang.String txtIncomeAccountHead) {
        this.txtIncomeAccountHead = txtIncomeAccountHead;
    }

    /**
     * Getter for property cboDivCalcFrequencyNew.
     *
     * @return Value of property cboDivCalcFrequencyNew.
     */
    public java.lang.String getCboDivCalcFrequencyNew() {
        return cboDivCalcFrequencyNew;
    }

    /**
     * Setter for property cboDivCalcFrequencyNew.
     *
     * @param cboDivCalcFrequencyNew New value of property
     * cboDivCalcFrequencyNew.
     */
    public void setCboDivCalcFrequencyNew(java.lang.String cboDivCalcFrequencyNew) {
        this.cboDivCalcFrequencyNew = cboDivCalcFrequencyNew;
    }

    /**
     * Getter for property txtMinDividendAmount.
     *
     * @return Value of property txtMinDividendAmount.
     */
    public java.lang.String getTxtMinDividendAmount() {
        return txtMinDividendAmount;
    }

    /**
     * Setter for property txtMinDividendAmount.
     *
     * @param txtMinDividendAmount New value of property txtMinDividendAmount.
     */
    public void setTxtMinDividendAmount(java.lang.String txtMinDividendAmount) {
        this.txtMinDividendAmount = txtMinDividendAmount;
    }

    /**
     * Getter for property cboDivCalcType.
     *
     * @return Value of property cboDivCalcType.
     */
    public java.lang.String getCboDivCalcType() {
        return cboDivCalcType;
    }

    /**
     * Setter for property cboDivCalcType.
     *
     * @param cboDivCalcType New value of property cboDivCalcType.
     */
    public void setCboDivCalcType(java.lang.String cboDivCalcType) {
        this.cboDivCalcType = cboDivCalcType;
    }

    /**
     * Getter for property cboDividendRounding.
     *
     * @return Value of property cboDividendRounding.
     */
    public java.lang.String getCboDividendRounding() {
        return cboDividendRounding;
    }

    /**
     * Setter for property cboDividendRounding.
     *
     * @param cboDividendRounding New value of property cboDividendRounding.
     */
    public void setCboDividendRounding(java.lang.String cboDividendRounding) {
        this.cboDividendRounding = cboDividendRounding;
    }

    public java.lang.String isRdoRatification() {
        return rdoRatification;
    }

    /**
     * Setter for property cboDividendRounding.
     *
     * @param cboDividendRounding New value of property cboDividendRounding.
     */
    public void setRdoRatification(java.lang.String rdoRatification) {
        this.rdoRatification = rdoRatification;
    }
     public java.lang.String isRdoFullClosure() {
        return rdoFullClosure;
    }

    public void setRdoFullClosure(java.lang.String rdoFullClosure) {
        this.rdoFullClosure = rdoFullClosure;
    }

    /**
     * Getter for property unclaimedDividendPeriodType.
     *
     * @return Value of property unclaimedDividendPeriodType.
     */
    public java.lang.String getUnclaimedDividendPeriodType() {
        return unclaimedDividendPeriodType;
    }

    /**
     * Setter for property unclaimedDividendPeriodType.
     *
     * @param unclaimedDividendPeriodType New value of property
     * unclaimedDividendPeriodType.
     */
    public void setUnclaimedDividendPeriodType(java.lang.String unclaimedDividendPeriodType) {
        this.unclaimedDividendPeriodType = unclaimedDividendPeriodType;
    }

    public void setNumPatternPrefix(String numPatternPrefix) {
        this.numPatternPrefix = numPatternPrefix;
    }

    public String getNumPatternPrefix() {
        return numPatternPrefix;
    }

    public void setNumPatternSuffix(String numPatternSuffix) {
        this.numPatternSuffix = numPatternSuffix;
    }

    public String getNumPatternSuffix() {
        return numPatternSuffix;
    }

    /**
     * Getter for property considerRecovery.
     *
     * @return Value of property considerRecovery.
     */
    public java.lang.String getConsiderRecovery() {
        return considerRecovery;
    }

    /**
     * Setter for property considerRecovery.
     *
     * @param considerRecovery New value of property considerRecovery.
     */
    public void setConsiderRecovery(java.lang.String considerRecovery) {
        this.considerRecovery = considerRecovery;
    }

    /**
     * Getter for property chkSubsidyForSCST.
     *
     * @return Value of property chkSubsidyForSCST.
     */
    public java.lang.String getChkSubsidyForSCST() {
        return chkSubsidyForSCST;
    }

    /**
     * Setter for property chkSubsidyForSCST.
     *
     * @param chkSubsidyForSCST New value of property chkSubsidyForSCST.
     */
    public void setChkSubsidyForSCST(java.lang.String chkSubsidyForSCST) {
        this.chkSubsidyForSCST = chkSubsidyForSCST;
    }

    /**
     * Getter for property txtCustomerShare.
     *
     * @return Value of property txtCustomerShare.
     */
    public java.lang.String getTxtCustomerShare() {
        return txtCustomerShare;
    }

    /**
     * Setter for property txtCustomerShare.
     *
     * @param txtCustomerShare New value of property txtCustomerShare.
     */
    public void setTxtCustomerShare(java.lang.String txtCustomerShare) {
        this.txtCustomerShare = txtCustomerShare;
    }

    /**
     * Getter for property txtGovernmentShare.
     *
     * @return Value of property txtGovernmentShare.
     */
    public java.lang.String getTxtGovernmentShare() {
        return txtGovernmentShare;
    }

    /**
     * Setter for property txtGovernmentShare.
     *
     * @param txtGovernmentShare New value of property txtGovernmentShare.
     */
    public void setTxtGovernmentShare(java.lang.String txtGovernmentShare) {
        this.txtGovernmentShare = txtGovernmentShare;
    }

    /**
     * Getter for property txtGovtSubsidyAccountHead.
     *
     * @return Value of property txtGovtSubsidyAccountHead.
     */
    public java.lang.String getTxtGovtSubsidyAccountHead() {
        return txtGovtSubsidyAccountHead;
    }

    /**
     * Setter for property txtGovtSubsidyAccountHead.
     *
     * @param txtGovtSubsidyAccountHead New value of property
     * txtGovtSubsidyAccountHead.
     */
    public void setTxtGovtSubsidyAccountHead(java.lang.String txtGovtSubsidyAccountHead) {
        this.txtGovtSubsidyAccountHead = txtGovtSubsidyAccountHead;
    }

    public String getDrfAllowed() {
        return drfAllowed;
    }

    public void setDrfAllowed(String drfAllowed) {
        this.drfAllowed = drfAllowed;
    }
    
}