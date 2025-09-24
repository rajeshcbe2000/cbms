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
package com.see.truetransact.transferobject.termloan.agritermloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_FACILITY_DETAILS.
 */
public class AgriTermLoanFacilityTO extends TransferObject implements Serializable {

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
    private String contactPhone = "";
    private String remarks = "";
    private String authorizeStatus1 = "";
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
    private String acctStatus = "";
    private String intGetFrom = "";
    private Double loanBalancePrincipal = null;
    private Double loanPaidInt = null;
    private Double loanPaidPenalint = null;
    private Double excessAmt = null;
    private Date lastRepayDt = null;
    private String authorizeStatus2 = "";
    private String authorizeRemarks2 = "";
    private String authorizeBy2 = "";
    private Date authorizeDt2 = null;
    private String createdBy = "";
    private String acctName = "";
    private String recommendedBy = "";
    private Date accOpenDt = null;
    private Date accCloseDt = null;
    private String docDetails = "";
    private String authorizedSignatory = "";
    private String pofAttorney = "";
    private String cardType = "";
    private Date reviewDate = null;
    private Double cardLimit = null;
    private Double cardPeriod = null;
    private String inspection = "";
    private Date lastIntCalcDt = null;

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
        strB.append(getTOString("docDetails", docDetails));
        strB.append(getTOString("authorizedSignatory", authorizedSignatory));
        strB.append(getTOString("pofAttorney", pofAttorney));
        strB.append(getTOString("cardType", cardType));
        strB.append(getTOString("reviewDate", reviewDate));
        strB.append(getTOString("cardLimit", cardLimit));
        strB.append(getTOString("cardPeriod", cardPeriod));
        strB.append(getTOString("inspection", inspection));
        strB.append(getTOString("lastIntCalcDt", lastIntCalcDt));
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
        strB.append(getTOXml("authorizedSignatory", authorizedSignatory));
        strB.append(getTOXml("docDetails", docDetails));
        strB.append(getTOXml("pofAttorney", pofAttorney));
        strB.append(getTOXml("cardType", cardType));
        strB.append(getTOXml("reviewDate", reviewDate));
        strB.append(getTOXml("cardLimit", cardLimit));
        strB.append(getTOXml("cardPeriod", cardPeriod));
        strB.append(getTOXml("inspection", inspection));
        strB.append(getTOXml("lastIntCalcDt", lastIntCalcDt));
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
     * Getter for property cardType.
     *
     * @return Value of property cardType.
     */
    public java.lang.String getCardType() {
        return cardType;
    }

    /**
     * Setter for property cardType.
     *
     * @param cardType New value of property cardType.
     */
    public void setCardType(java.lang.String cardType) {
        this.cardType = cardType;
    }

    /**
     * Getter for property cardLimit.
     *
     * @return Value of property cardLimit.
     */
    public java.lang.Double getCardLimit() {
        return cardLimit;
    }

    /**
     * Setter for property cardLimit.
     *
     * @param cardLimit New value of property cardLimit.
     */
    public void setCardLimit(java.lang.Double cardLimit) {
        this.cardLimit = cardLimit;
    }

    /**
     * Getter for property cardPeriod.
     *
     * @return Value of property cardPeriod.
     */
    public java.lang.Double getCardPeriod() {
        return cardPeriod;
    }

    /**
     * Setter for property cardPeriod.
     *
     * @param cardPeriod New value of property cardPeriod.
     */
    public void setCardPeriod(java.lang.Double cardPeriod) {
        this.cardPeriod = cardPeriod;
    }

    /**
     * Getter for property reviewDate.
     *
     * @return Value of property reviewDate.
     */
    public java.util.Date getReviewDate() {
        return reviewDate;
    }

    /**
     * Setter for property reviewDate.
     *
     * @param reviewDate New value of property reviewDate.
     */
    public void setReviewDate(java.util.Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    /**
     * Getter for property inspection.
     *
     * @return Value of property inspection.
     */
    public java.lang.String getInspection() {
        return inspection;
    }

    /**
     * Setter for property inspection.
     *
     * @param inspection New value of property inspection.
     */
    public void setInspection(java.lang.String inspection) {
        this.inspection = inspection;
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
}