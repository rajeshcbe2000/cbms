/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanFacilityTO.java
 * 
 * Created on Wed Apr 13 17:21:29 IST 2005
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_FACILITY_DETAILS.
 */
public class TermLoanFacilityTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String sanctionNo = "";
    private Double slNo = null;
    private String prodId = "";
    private String securityType = "";
    private String securityDetails = "";
    private String accountType = "";
    private String interestNature = "";
    private String interestType = "";
    private String accountLimit = "";
    private String riskWeight = "";
    private Date demandPromDt = null;
    private Date demandPromExpdt = null;
    private String multiDisburse = "";
    private Date aodDt = null;
    private String purposeDesc = "";
    private String groupDesc = "";
    private String interest = "";
    private String contactPerson = "";
    private String dealerID = "";//Added By Revathi.L
    private String contactPhone = "";
    private String remarks = "";
    private String authorizeStatus1 = null;
    private String authorizeRemarks1 = "";
    private String status = "";
    private String acctNum = "";
    private String authorizeBy1 = "";
    private Date authorizeDt1 = null;
    private Date createDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private Double availableBalance = null;
    private Double clearBalance = null;
    private Double unclearBalance = null;
    private Double shadowDebit = null;
    private Double shadowCredit = null;
    private Double totalBalance = null;
    private Date lastTransDt = null;
    private String branchId = "";
    private String acctStatus = null;
    private String intGetFrom = "";
    private Double loanBalancePrincipal = null;
    private Double loanPaidInt = null;
    private Double loanPaidPenalint = null;
    private Double excessAmt = null;
    private Date lastRepayDt = null;
    private String authorizeStatus2 = null;
    private String authorizeRemarks2 = "";
    private String authorizeBy2 = "";
    private Date authorizeDt2 = null;
    private String createdBy = "";
    private String acctName = "";
    private String recommendedBy = "";
    private String recommendedBy2 = "";
    private Date accOpenDt = null;
    private Date accCloseDt = null;
    private String dpYesNo = "";
    private String docDetails = "";
    private String authorizedSignatory = "";
    private String pofAttorney = "";
    private Date lastIntCalcDt = null;
    private String acctTransfer = "";
    private String isMobileBanking = "";
    private String salaryRecovery = "";
    private String lockStatus = "";
    private String subsidyAllowed = "";
    private Double subsidyAmt = null;
    private String subsidyAdjustAchd = "";
    private Date subsidyDate = null;
    private Double subsidyAdjustAmt = null;
    private String rebateAllowed = "";
    private Double rebateAmt = null;
    private Date rebateDate = null;
    private String renewalAcctNo = null;
    private String txtJewelleryDetails = "";
    private String txtGrossWeight = "";
    private String txtNetWeight = "";
    private Double txtValueOfGold;
    private String txtGoldRemarks = "";
    private String ots = "";
    private Double koleLandArea = 0.0;

    /**
     * Setter/Getter for BORROW_NO - table Field
     */
    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    /**
     * Setter/Getter for SANCTION_NO - table Field
     */
    public void setSanctionNo(String sanctionNo) {
        this.sanctionNo = sanctionNo;
    }

    public String getSanctionNo() {
        return sanctionNo;
    }

    /**
     * Setter/Getter for SL_NO - table Field
     */
    public void setSlNo(Double slNo) {
        this.slNo = slNo;
    }

    public Double getSlNo() {
        return slNo;
    }

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
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
     * Setter/Getter for SECURITY_DETAILS - table Field
     */
    public void setSecurityDetails(String securityDetails) {
        this.securityDetails = securityDetails;
    }

    public String getSecurityDetails() {
        return securityDetails;
    }

    /**
     * Setter/Getter for ACCOUNT_TYPE - table Field
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    /**
     * Setter/Getter for INTEREST_NATURE - table Field
     */
    public void setInterestNature(String interestNature) {
        this.interestNature = interestNature;
    }

    public String getInterestNature() {
        return interestNature;
    }

    /**
     * Setter/Getter for INTEREST_TYPE - table Field
     */
    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getInterestType() {
        return interestType;
    }

    /**
     * Setter/Getter for ACCOUNT_LIMIT - table Field
     */
    public void setAccountLimit(String accountLimit) {
        this.accountLimit = accountLimit;
    }

    public String getAccountLimit() {
        return accountLimit;
    }

    /**
     * Setter/Getter for RISK_WEIGHT - table Field
     */
    public void setRiskWeight(String riskWeight) {
        this.riskWeight = riskWeight;
    }

    public String getRiskWeight() {
        return riskWeight;
    }

    /**
     * Setter/Getter for DEMAND_PROM_DT - table Field
     */
    public void setDemandPromDt(Date demandPromDt) {
        this.demandPromDt = demandPromDt;
    }

    public Date getDemandPromDt() {
        return demandPromDt;
    }

    /**
     * Setter/Getter for DEMAND_PROM_EXPDT - table Field
     */
    public void setDemandPromExpdt(Date demandPromExpdt) {
        this.demandPromExpdt = demandPromExpdt;
    }

    public Date getDemandPromExpdt() {
        return demandPromExpdt;
    }

    /**
     * Setter/Getter for MULTI_DISBURSE - table Field
     */
    public void setMultiDisburse(String multiDisburse) {
        this.multiDisburse = multiDisburse;
    }

    public String getMultiDisburse() {
        return multiDisburse;
    }

    /**
     * Setter/Getter for AOD_DT - table Field
     */
    public void setAodDt(Date aodDt) {
        this.aodDt = aodDt;
    }

    public Date getAodDt() {
        return aodDt;
    }

    /**
     * Setter/Getter for PURPOSE_DESC - table Field
     */
    public void setPurposeDesc(String purposeDesc) {
        this.purposeDesc = purposeDesc;
    }

    public String getPurposeDesc() {
        return purposeDesc;
    }

    /**
     * Setter/Getter for GROUP_DESC - table Field
     */
    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    /**
     * Setter/Getter for INTEREST - table Field
     */
    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInterest() {
        return interest;
    }

    /**
     * Setter/Getter for CONTACT_PERSON - table Field
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * Setter/Getter for CONTACT_PHONE - table Field
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
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
     * Setter/Getter for AUTHORIZE_STATUS_1 - table Field
     */
    public void setAuthorizeStatus1(String authorizeStatus1) {
        this.authorizeStatus1 = authorizeStatus1;
    }

    public String getAuthorizeStatus1() {
        return authorizeStatus1;
    }

    /**
     * Setter/Getter for AUTHORIZE_REMARKS_1 - table Field
     */
    public void setAuthorizeRemarks1(String authorizeRemarks1) {
        this.authorizeRemarks1 = authorizeRemarks1;
    }

    public String getAuthorizeRemarks1() {
        return authorizeRemarks1;
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
     * Setter/Getter for ACCT_NUM - table Field
     */
    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY_1 - table Field
     */
    public void setAuthorizeBy1(String authorizeBy1) {
        this.authorizeBy1 = authorizeBy1;
    }

    public String getAuthorizeBy1() {
        return authorizeBy1;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT_1 - table Field
     */
    public void setAuthorizeDt1(Date authorizeDt1) {
        this.authorizeDt1 = authorizeDt1;
    }

    public Date getAuthorizeDt1() {
        return authorizeDt1;
    }

    /**
     * Setter/Getter for CREATE_DT - table Field
     */
    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getCreateDt() {
        return createDt;
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
     * Setter/Getter for AVAILABLE_BALANCE - table Field
     */
    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    /**
     * Setter/Getter for CLEAR_BALANCE - table Field
     */
    public void setClearBalance(Double clearBalance) {
        this.clearBalance = clearBalance;
    }

    public Double getClearBalance() {
        return clearBalance;
    }

    /**
     * Setter/Getter for UNCLEAR_BALANCE - table Field
     */
    public void setUnclearBalance(Double unclearBalance) {
        this.unclearBalance = unclearBalance;
    }

    public Double getUnclearBalance() {
        return unclearBalance;
    }

    /**
     * Setter/Getter for SHADOW_DEBIT - table Field
     */
    public void setShadowDebit(Double shadowDebit) {
        this.shadowDebit = shadowDebit;
    }

    public Double getShadowDebit() {
        return shadowDebit;
    }

    /**
     * Setter/Getter for SHADOW_CREDIT - table Field
     */
    public void setShadowCredit(Double shadowCredit) {
        this.shadowCredit = shadowCredit;
    }

    public Double getShadowCredit() {
        return shadowCredit;
    }

    /**
     * Setter/Getter for TOTAL_BALANCE - table Field
     */
    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    /**
     * Setter/Getter for LAST_TRANS_DT - table Field
     */
    public void setLastTransDt(Date lastTransDt) {
        this.lastTransDt = lastTransDt;
    }

    public Date getLastTransDt() {
        return lastTransDt;
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
     * Setter/Getter for ACCT_STATUS - table Field
     */
    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
    }

    public String getAcctStatus() {
        return acctStatus;
    }

    /**
     * Setter/Getter for INT_GET_FROM - table Field
     */
    public void setIntGetFrom(String intGetFrom) {
        this.intGetFrom = intGetFrom;
    }

    public String getIntGetFrom() {
        return intGetFrom;
    }

    /**
     * Setter/Getter for LOAN_BALANCE_PRINCIPAL - table Field
     */
    public void setLoanBalancePrincipal(Double loanBalancePrincipal) {
        this.loanBalancePrincipal = loanBalancePrincipal;
    }

    public Double getLoanBalancePrincipal() {
        return loanBalancePrincipal;
    }

    /**
     * Setter/Getter for LOAN_PAID_INT - table Field
     */
    public void setLoanPaidInt(Double loanPaidInt) {
        this.loanPaidInt = loanPaidInt;
    }

    public Double getLoanPaidInt() {
        return loanPaidInt;
    }

    /**
     * Setter/Getter for LOAN_PAID_PENALINT - table Field
     */
    public void setLoanPaidPenalint(Double loanPaidPenalint) {
        this.loanPaidPenalint = loanPaidPenalint;
    }

    public Double getLoanPaidPenalint() {
        return loanPaidPenalint;
    }

    /**
     * Setter/Getter for EXCESS_AMT - table Field
     */
    public void setExcessAmt(Double excessAmt) {
        this.excessAmt = excessAmt;
    }

    public Double getExcessAmt() {
        return excessAmt;
    }

    /**
     * Setter/Getter for LAST_REPAY_DT - table Field
     */
    public void setLastRepayDt(Date lastRepayDt) {
        this.lastRepayDt = lastRepayDt;
    }

    public Date getLastRepayDt() {
        return lastRepayDt;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS_2 - table Field
     */
    public void setAuthorizeStatus2(String authorizeStatus2) {
        this.authorizeStatus2 = authorizeStatus2;
    }

    public String getAuthorizeStatus2() {
        return authorizeStatus2;
    }

    /**
     * Setter/Getter for AUTHORIZE_REMARKS_2 - table Field
     */
    public void setAuthorizeRemarks2(String authorizeRemarks2) {
        this.authorizeRemarks2 = authorizeRemarks2;
    }

    public String getAuthorizeRemarks2() {
        return authorizeRemarks2;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY_2 - table Field
     */
    public void setAuthorizeBy2(String authorizeBy2) {
        this.authorizeBy2 = authorizeBy2;
    }

    public String getAuthorizeBy2() {
        return authorizeBy2;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT_2 - table Field
     */
    public void setAuthorizeDt2(Date authorizeDt2) {
        this.authorizeDt2 = authorizeDt2;
    }

    public Date getAuthorizeDt2() {
        return authorizeDt2;
    }

    /**
     * Setter/Getter for ACCT_NAME - table Field
     */
    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getAcctName() {
        return acctName;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("borrowNo" + KEY_VAL_SEPARATOR + "sanctionNo" + KEY_VAL_SEPARATOR + "slNo");
        return borrowNo + KEY_VAL_SEPARATOR + sanctionNo + KEY_VAL_SEPARATOR + slNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("sanctionNo", sanctionNo));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("securityType", securityType));
        strB.append(getTOString("securityDetails", securityDetails));
        strB.append(getTOString("accountType", accountType));
        strB.append(getTOString("interestNature", interestNature));
        strB.append(getTOString("interestType", interestType));
        strB.append(getTOString("accountLimit", accountLimit));
        strB.append(getTOString("riskWeight", riskWeight));
        strB.append(getTOString("demandPromDt", demandPromDt));
        strB.append(getTOString("demandPromExpdt", demandPromExpdt));
        strB.append(getTOString("multiDisburse", multiDisburse));
        strB.append(getTOString("aodDt", aodDt));
        strB.append(getTOString("purposeDesc", purposeDesc));
        strB.append(getTOString("groupDesc", groupDesc));
        strB.append(getTOString("interest", interest));
        strB.append(getTOString("contactPerson", contactPerson));
        strB.append(getTOString("dealerID", dealerID));
        strB.append(getTOString("contactPhone", contactPhone));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("authorizeStatus1", authorizeStatus1));
        strB.append(getTOString("authorizeRemarks1", authorizeRemarks1));
        strB.append(getTOString("status", status));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("authorizeBy1", authorizeBy1));
        strB.append(getTOString("authorizeDt1", authorizeDt1));
        strB.append(getTOString("createDt", createDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("availableBalance", availableBalance));
        strB.append(getTOString("clearBalance", clearBalance));
        strB.append(getTOString("unclearBalance", unclearBalance));
        strB.append(getTOString("shadowDebit", shadowDebit));
        strB.append(getTOString("shadowCredit", shadowCredit));
        strB.append(getTOString("totalBalance", totalBalance));
        strB.append(getTOString("lastTransDt", lastTransDt));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("acctStatus", acctStatus));
        strB.append(getTOString("intGetFrom", intGetFrom));
        strB.append(getTOString("loanBalancePrincipal", loanBalancePrincipal));
        strB.append(getTOString("loanPaidInt", loanPaidInt));
        strB.append(getTOString("loanPaidPenalint", loanPaidPenalint));
        strB.append(getTOString("excessAmt", excessAmt));
        strB.append(getTOString("lastRepayDt", lastRepayDt));
        strB.append(getTOString("authorizeStatus2", authorizeStatus2));
        strB.append(getTOString("authorizeRemarks2", authorizeRemarks2));
        strB.append(getTOString("authorizeBy2", authorizeBy2));
        strB.append(getTOString("authorizeDt2", authorizeDt2));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("acctName", acctName));
        strB.append(getTOString("dpYesNo", dpYesNo));
        strB.append(getTOString("docDetails", docDetails));
        strB.append(getTOString("authorizedSignatory", authorizedSignatory));
        strB.append(getTOString("pofAttorney", pofAttorney));
        strB.append(getTOString("accOpenDt", accOpenDt));
        strB.append(getTOString("lastIntCalcDt", lastIntCalcDt));
        strB.append(getTOString("accCloseDt", accCloseDt));
        strB.append(getTOString("acctTransfer", acctTransfer));
        strB.append(getTOString("isMobileBanking", isMobileBanking));
        strB.append(getTOString("salaryRecovery", salaryRecovery));
        strB.append(getTOString("lockStatus", lockStatus));
        strB.append(getTOString("subsidyAdjustAmt", subsidyAdjustAmt));
        strB.append(getTOString("renewalAcctNo", renewalAcctNo));
        strB.append(getTOString("txtJewelleryDetails", txtJewelleryDetails));
        strB.append(getTOString("txtGrossWeight", txtGrossWeight));
        strB.append(getTOString("txtNetWeight", txtNetWeight));
        strB.append(getTOString("txtValueOfGold", txtValueOfGold));
        strB.append(getTOString("txtGoldRemarks", txtGoldRemarks));
        strB.append(getTOString("recommendedBy2", recommendedBy2));
        strB.append(getTOString("koleLandArea", koleLandArea));
        strB.append(getTOString("ots", ots));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("sanctionNo", sanctionNo));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("securityType", securityType));
        strB.append(getTOXml("securityDetails", securityDetails));
        strB.append(getTOXml("accountType", accountType));
        strB.append(getTOXml("interestNature", interestNature));
        strB.append(getTOXml("interestType", interestType));
        strB.append(getTOXml("accountLimit", accountLimit));
        strB.append(getTOXml("riskWeight", riskWeight));
        strB.append(getTOXml("demandPromDt", demandPromDt));
        strB.append(getTOXml("demandPromExpdt", demandPromExpdt));
        strB.append(getTOXml("multiDisburse", multiDisburse));
        strB.append(getTOXml("aodDt", aodDt));
        strB.append(getTOXml("purposeDesc", purposeDesc));
        strB.append(getTOXml("groupDesc", groupDesc));
        strB.append(getTOXml("interest", interest));
        strB.append(getTOXml("contactPerson", contactPerson));
        strB.append(getTOXml("dealerID", dealerID));
        strB.append(getTOXml("contactPhone", contactPhone));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("authorizeStatus1", authorizeStatus1));
        strB.append(getTOXml("authorizeRemarks1", authorizeRemarks1));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("authorizeBy1", authorizeBy1));
        strB.append(getTOXml("authorizeDt1", authorizeDt1));
        strB.append(getTOXml("createDt", createDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("availableBalance", availableBalance));
        strB.append(getTOXml("clearBalance", clearBalance));
        strB.append(getTOXml("unclearBalance", unclearBalance));
        strB.append(getTOXml("shadowDebit", shadowDebit));
        strB.append(getTOXml("shadowCredit", shadowCredit));
        strB.append(getTOXml("totalBalance", totalBalance));
        strB.append(getTOXml("lastTransDt", lastTransDt));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("acctStatus", acctStatus));
        strB.append(getTOXml("intGetFrom", intGetFrom));
        strB.append(getTOXml("loanBalancePrincipal", loanBalancePrincipal));
        strB.append(getTOXml("loanPaidInt", loanPaidInt));
        strB.append(getTOXml("loanPaidPenalint", loanPaidPenalint));
        strB.append(getTOXml("excessAmt", excessAmt));
        strB.append(getTOXml("lastRepayDt", lastRepayDt));
        strB.append(getTOXml("authorizeStatus2", authorizeStatus2));
        strB.append(getTOXml("authorizeRemarks2", authorizeRemarks2));
        strB.append(getTOXml("authorizeBy2", authorizeBy2));
        strB.append(getTOXml("authorizeDt2", authorizeDt2));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("acctName", acctName));
        strB.append(getTOXml("dpYesNo", dpYesNo));
        strB.append(getTOXml("docDetails", docDetails));
        strB.append(getTOXml("authorizedSignatory", authorizedSignatory));
        strB.append(getTOXml("pofAttorney", pofAttorney));
        strB.append(getTOXml("accOpenDt", accOpenDt));
        strB.append(getTOXml("lastIntCalcDt", lastIntCalcDt));
        strB.append(getTOXml("accCloseDt", accCloseDt));
        strB.append(getTOXml("acctTransfer", acctTransfer));
        strB.append(getTOXml("isMobileBanking", isMobileBanking));
        strB.append(getTOXml("salaryRecovery", salaryRecovery));
        strB.append(getTOXml("lockStatus", lockStatus));
        strB.append(getTOXml("subsidyAdjustAmt", subsidyAdjustAmt));
        strB.append(getTOXml("renewalAcctNo", renewalAcctNo));
        strB.append(getTOXml("txtJewelleryDetails", txtJewelleryDetails));
        strB.append(getTOXml("txtGrossWeight", txtGrossWeight));
        strB.append(getTOXml("txtNetWeight", txtNetWeight));
        strB.append(getTOXml("txtValueOfGold", txtValueOfGold));
        strB.append(getTOXml("txtGoldRemarks", txtGoldRemarks));
        strB.append(getTOXml("recommendedBy2", recommendedBy2));
        strB.append(getTOXml("koleLandArea", koleLandArea));
        strB.append(getTOXml("ots", ots));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property recommendedBy.
     *
     * @return Value of property recommendedBy.
     */
    public java.lang.String getRecommendedBy() {
        return recommendedBy;
    }

    /**
     * Setter for property recommendedBy.
     *
     * @param recommendedBy New value of property recommendedBy.
     */
    public void setRecommendedBy(java.lang.String recommendedBy) {
        this.recommendedBy = recommendedBy;
    }

    /**
     * Getter for property accOpenDt.
     *
     * @return Value of property accOpenDt.
     */
    public java.util.Date getAccOpenDt() {
        return accOpenDt;
    }

    /**
     * Setter for property accOpenDt.
     *
     * @param accOpenDt New value of property accOpenDt.
     */
    public void setAccOpenDt(java.util.Date accOpenDt) {
        this.accOpenDt = accOpenDt;
    }

    /**
     * Getter for property accCloseDt.
     *
     * @return Value of property accCloseDt.
     */
    public java.util.Date getAccCloseDt() {
        return accCloseDt;
    }

    /**
     * Setter for property accCloseDt.
     *
     * @param accCloseDt New value of property accCloseDt.
     */
    public void setAccCloseDt(java.util.Date accCloseDt) {
        this.accCloseDt = accCloseDt;
    }

    /**
     * Getter for property dpYesNo.
     *
     * @return Value of property dpYesNo.
     */
    public java.lang.String getDpYesNo() {
        return dpYesNo;
    }

    /**
     * Setter for property dpYesNo.
     *
     * @param dpYesNo New value of property dpYesNo.
     */
    public void setDpYesNo(java.lang.String dpYesNo) {
        this.dpYesNo = dpYesNo;
    }

    /**
     * Getter for property docDetails.
     *
     * @return Value of property docDetails.
     */
    public java.lang.String getDocDetails() {
        return docDetails;
    }

    /**
     * Setter for property docDetails.
     *
     * @param docDetails New value of property docDetails.
     */
    public void setDocDetails(java.lang.String docDetails) {
        this.docDetails = docDetails;
    }

    /**
     * Getter for property authorizedSignatory.
     *
     * @return Value of property authorizedSignatory.
     */
    public java.lang.String getAuthorizedSignatory() {
        return authorizedSignatory;
    }

    /**
     * Setter for property authorizedSignatory.
     *
     * @param authorizedSignatory New value of property authorizedSignatory.
     */
    public void setAuthorizedSignatory(java.lang.String authorizedSignatory) {
        this.authorizedSignatory = authorizedSignatory;
    }

    /**
     * Getter for property pofAttorney.
     *
     * @return Value of property pofAttorney.
     */
    public java.lang.String getPofAttorney() {
        return pofAttorney;
    }

    /**
     * Setter for property pofAttorney.
     *
     * @param pofAttorney New value of property pofAttorney.
     */
    public void setPofAttorney(java.lang.String pofAttorney) {
        this.pofAttorney = pofAttorney;
    }

    /**
     * Getter for property lastIntCalcDt.
     *
     * @return Value of property lastIntCalcDt.
     */
    public java.util.Date getLastIntCalcDt() {
        return lastIntCalcDt;
    }

    /**
     * Setter for property lastIntCalcDt.
     *
     * @param lastIntCalcDt New value of property lastIntCalcDt.
     */
    public void setLastIntCalcDt(java.util.Date lastIntCalcDt) {
        this.lastIntCalcDt = lastIntCalcDt;
    }

    /**
     * Getter for property acctTransfer.
     *
     * @return Value of property acctTransfer.
     */
    public java.lang.String getAcctTransfer() {
        return acctTransfer;
    }

    /**
     * Setter for property acctTransfer.
     *
     * @param acctTransfer New value of property acctTransfer.
     */
    public void setAcctTransfer(java.lang.String acctTransfer) {
        this.acctTransfer = acctTransfer;
    }

    /**
     * Getter for property isMobileBanking.
     *
     * @return Value of property isMobileBanking.
     */
    public java.lang.String getIsMobileBanking() {
        return isMobileBanking;
    }

    /**
     * Setter for property isMobileBanking.
     *
     * @param isMobileBanking New value of property isMobileBanking.
     */
    public void setIsMobileBanking(java.lang.String isMobileBanking) {
        this.isMobileBanking = isMobileBanking;
    }

    /**
     * Getter for property salaryRecovery.
     *
     * @return Value of property salaryRecovery.
     */
    public java.lang.String getSalaryRecovery() {
        return salaryRecovery;
    }

    /**
     * Setter for property salaryRecovery.
     *
     * @param salaryRecovery New value of property salaryRecovery.
     */
    public void setSalaryRecovery(java.lang.String salaryRecovery) {
        this.salaryRecovery = salaryRecovery;
    }

    /**
     * Getter for property lockStatus.
     *
     * @return Value of property lockStatus.
     */
    public java.lang.String getLockStatus() {
        return lockStatus;
    }

    /**
     * Setter for property lockStatus.
     *
     * @param lockStatus New value of property lockStatus.
     */
    public void setLockStatus(java.lang.String lockStatus) {
        this.lockStatus = lockStatus;
    }

    /*
     * Getter for property rebateDate. @return Value of property rebateDate.
     */
    public java.util.Date getRebateDate() {
        return rebateDate;
    }

    /**
     * Setter for property rebateDate.
     *
     * @param rebateDate New value of property rebateDate.
     */
    public void setRebateDate(java.util.Date rebateDate) {
        this.rebateDate = rebateDate;
    }

    /**
     * Getter for property rebateAmt.
     *
     * @return Value of property rebateAmt.
     */
    public java.lang.Double getRebateAmt() {
        return rebateAmt;
    }

    /**
     * Setter for property rebateAmt.
     *
     * @param rebateAmt New value of property rebateAmt.
     */
    public void setRebateAmt(java.lang.Double rebateAmt) {
        this.rebateAmt = rebateAmt;
    }

    /**
     * Getter for property rebateAllowed.
     *
     * @return Value of property rebateAllowed.
     */
    public java.lang.String getRebateAllowed() {
        return rebateAllowed;
    }

    /**
     * Setter for property rebateAllowed.
     *
     * @param rebateAllowed New value of property rebateAllowed.
     */
    public void setRebateAllowed(java.lang.String rebateAllowed) {
        this.rebateAllowed = rebateAllowed;
    }

    /**
     * Getter for property subsidyDate.
     *
     * @return Value of property subsidyDate.
     */
    public java.util.Date getSubsidyDate() {
        return subsidyDate;
    }

    /**
     * Setter for property subsidyDate.
     *
     * @param subsidyDate New value of property subsidyDate.
     */
    public void setSubsidyDate(java.util.Date subsidyDate) {
        this.subsidyDate = subsidyDate;
    }

    /**
     * Getter for property subsidyAdjustAchd.
     *
     * @return Value of property subsidyAdjustAchd.
     */
    public java.lang.String getSubsidyAdjustAchd() {
        return subsidyAdjustAchd;
    }

    /**
     * Setter for property subsidyAdjustAchd.
     *
     * @param subsidyAdjustAchd New value of property subsidyAdjustAchd.
     */
    public void setSubsidyAdjustAchd(java.lang.String subsidyAdjustAchd) {
        this.subsidyAdjustAchd = subsidyAdjustAchd;
    }

    /**
     * Getter for property subsidyAmt.
     *
     * @return Value of property subsidyAmt.
     */
    public java.lang.Double getSubsidyAmt() {
        return subsidyAmt;
    }

    /**
     * Setter for property subsidyAmt.
     *
     * @param subsidyAmt New value of property subsidyAmt.
     */
    public void setSubsidyAmt(java.lang.Double subsidyAmt) {
        this.subsidyAmt = subsidyAmt;
    }

    /**
     * Getter for property subsidyAllowed.
     *
     * @return Value of property subsidyAllowed.
     */
    public java.lang.String getSubsidyAllowed() {
        return subsidyAllowed;
    }

    /**
     * Setter for property subsidyAllowed.
     *
     * @param subsidyAllowed New value of property subsidyAllowed.
     */
    public void setSubsidyAllowed(java.lang.String subsidyAllowed) {
        this.subsidyAllowed = subsidyAllowed;
    }

    /**
     * Getter for property subsidyAdjustAmt.
     *
     * @return Value of property subsidyAdjustAmt.
     */
    public java.lang.Double getSubsidyAdjustAmt() {
        return subsidyAdjustAmt;
    }

    /**
     * Setter for property subsidyAdjustAmt.
     *
     * @param subsidyAdjustAmt New value of property subsidyAdjustAmt.
     */
    public void setSubsidyAdjustAmt(java.lang.Double subsidyAdjustAmt) {
        this.subsidyAdjustAmt = subsidyAdjustAmt;
    }

    /**
     * Getter for property renewalAcctNo.
     *
     * @return Value of property renewalAcctNo.
     */
    public java.lang.String getRenewalAcctNo() {
        return renewalAcctNo;
    }

    /**
     * Setter for property renewalAcctNo.
     *
     * @param renewalAcctNo New value of property renewalAcctNo.
     */
    public void setRenewalAcctNo(java.lang.String renewalAcctNo) {
        this.renewalAcctNo = renewalAcctNo;
    }

    /**
     * Getter for property txtJewelleryDetails.
     *
     * @return Value of property txtJewelleryDetails.
     */
    public java.lang.String getTxtJewelleryDetails() {
        return txtJewelleryDetails;
    }

    /**
     * Setter for property txtJewelleryDetails.
     *
     * @param txtJewelleryDetails New value of property txtJewelleryDetails.
     */
    public void setTxtJewelleryDetails(java.lang.String txtJewelleryDetails) {
        this.txtJewelleryDetails = txtJewelleryDetails;
    }

    /**
     * Getter for property txtGrossWeight.
     *
     * @return Value of property txtGrossWeight.
     */
    public java.lang.String getTxtGrossWeight() {
        return txtGrossWeight;
    }

    /**
     * Setter for property txtGrossWeight.
     *
     * @param txtGrossWeight New value of property txtGrossWeight.
     */
    public void setTxtGrossWeight(java.lang.String txtGrossWeight) {
        this.txtGrossWeight = txtGrossWeight;
    }

    /**
     * Getter for property txtNetWeight.
     *
     * @return Value of property txtNetWeight.
     */
    public java.lang.String getTxtNetWeight() {
        return txtNetWeight;
    }

    /**
     * Setter for property txtNetWeight.
     *
     * @param txtNetWeight New value of property txtNetWeight.
     */
    public void setTxtNetWeight(java.lang.String txtNetWeight) {
        this.txtNetWeight = txtNetWeight;
    }

    /**
     * Getter for property txtValueOfGold.
     *
     * @return Value of property txtValueOfGold.
     */
    public java.lang.Double getTxtValueOfGold() {
        return txtValueOfGold;
    }

    /**
     * Setter for property txtValueOfGold.
     *
     * @param txtValueOfGold New value of property txtValueOfGold.
     */
    public void setTxtValueOfGold(java.lang.Double txtValueOfGold) {
        this.txtValueOfGold = txtValueOfGold;
    }

    /**
     * Getter for property txtGoldRemarks.
     *
     * @return Value of property txtGoldRemarks.
     */
    public java.lang.String getTxtGoldRemarks() {
        return txtGoldRemarks;
    }

    /**
     * Setter for property txtGoldRemarks.
     *
     * @param txtGoldRemarks New value of property txtGoldRemarks.
     */
    public void setTxtGoldRemarks(java.lang.String txtGoldRemarks) {
        this.txtGoldRemarks = txtGoldRemarks;
    }

    /**
     * Getter for property ots.
     *
     * @return Value of property ots.
     */
    public java.lang.String getOts() {
        return ots;
    }

    /**
     * Setter for property ots.
     *
     * @param ots New value of property ots.
     */
    public void setOts(java.lang.String ots) {
        this.ots = ots;
    }

    public String getDealerID() {
        return dealerID;
    }

    public void setDealerID(String dealerID) {
        this.dealerID = dealerID;
    }

    public String getRecommendedBy2() {
        return recommendedBy2;
    }

    public void setRecommendedBy2(String recommendedBy2) {
        this.recommendedBy2 = recommendedBy2;
    }    

    public Double getKoleLandArea() {
        return koleLandArea;
    }

    public void setKoleLandArea(Double koleLandArea) {
        this.koleLandArea = koleLandArea;
    }
    
    
}
