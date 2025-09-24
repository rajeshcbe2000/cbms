/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountTO.java
 * 
 * Created on Fri Apr 15 10:47:20 IST 2005
 */
package com.see.truetransact.transferobject.operativeaccount;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ACT_MASTER.
 */
public class AccountTO extends TransferObject implements Serializable {
    private static final long serialVersionUID = 1480154587612063809L;
    private String actNum = "";
    private String prodId = "";
    private String custId = "";
    private String branchCode = "";
    private String agentId = "";
    private Date createDt = null;
    private String actStatusId = "";
    private String actCatId = "";
    private String optModeId = "";
    private Double todLimit = null;
    private String groupCodeId = "";
    private String settmtModeId = "";
    private String prevActNum = "";
    private Double clearBalance = null;
    private Double unclearBalance = null;
    private Double floatBalance = null;
    private Double effectiveBalance = null;
    private Double availableBalance = null;
    private String createdBy = "";
    private String authorizedBy = "";
    private String authorizationStatus = "";
    private String closedBy = "";
    private Date closedDt = null;
    private String baseCurr = "";
    private Date lastTransDt = null;
    private Double shadowCredit = null;
    private Double shadowDebit = null;
    private Date authorizedDt = null;
    private Date actStatusDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Double lienAmt = null;
    private Double freezeAmt = null;
    private Double totalBalance = null;
    private String commAddrType = "";
    private Double productAmt = null;
    private String categoryId = "";
    private Double flexiDepositAmt = null;
    private String acctName = "";
    private String remarks = "";
    private String cardActNo = "";
    private Double openingAmount = null;
    private String introducer="";
    private String linkingActNum = "";
    private Double atmCardLimitAmt = null;
    private String primaryAccount = "N";
    private String upiMobileNo = "";

    public Double getAtmCardLimitAmt() {
        return atmCardLimitAmt;
    }

    public void setAtmCardLimitAmt(Double atmCardLimitAmt) {
        this.atmCardLimitAmt = atmCardLimitAmt;
    }
    public String getLinkingActNum() {
        return linkingActNum;
    }

    public void setLinkingActNum(String linkingActNum) {
        this.linkingActNum = linkingActNum;
    }
    /**
     * Setter/Getter for ACT_NUM - table Field
     */    

    public String getIntroducer() {
        return introducer;
    }

    public void setIntroducer(String introducer) {
        this.introducer = introducer;
    }
    
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
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
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
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
     * Setter/Getter for CREATE_DT - table Field
     */
    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getCreateDt() {
        return createDt;
    }

    /**
     * Setter/Getter for ACT_STATUS_ID - table Field
     */
    public void setActStatusId(String actStatusId) {
        this.actStatusId = actStatusId;
    }

    public String getActStatusId() {
        return actStatusId;
    }

    /**
     * Setter/Getter for ACT_CAT_ID - table Field
     */
    public void setActCatId(String actCatId) {
        this.actCatId = actCatId;
    }

    public String getActCatId() {
        return actCatId;
    }

    /**
     * Setter/Getter for OPT_MODE_ID - table Field
     */
    public void setOptModeId(String optModeId) {
        this.optModeId = optModeId;
    }

    public String getOptModeId() {
        return optModeId;
    }

    /**
     * Setter/Getter for TOD_LIMIT - table Field
     */
    public void setTodLimit(Double todLimit) {
        this.todLimit = todLimit;
    }

    public Double getTodLimit() {
        return todLimit;
    }

    /**
     * Setter/Getter for GROUP_CODE_ID - table Field
     */
    public void setGroupCodeId(String groupCodeId) {
        this.groupCodeId = groupCodeId;
    }

    public String getGroupCodeId() {
        return groupCodeId;
    }

    /**
     * Setter/Getter for SETTMT_MODE_ID - table Field
     */
    public void setSettmtModeId(String settmtModeId) {
        this.settmtModeId = settmtModeId;
    }

    public String getSettmtModeId() {
        return settmtModeId;
    }

    /**
     * Setter/Getter for PREV_ACT_NUM - table Field
     */
    public void setPrevActNum(String prevActNum) {
        this.prevActNum = prevActNum;
    }

    public String getPrevActNum() {
        return prevActNum;
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
     * Setter/Getter for FLOAT_BALANCE - table Field
     */
    public void setFloatBalance(Double floatBalance) {
        this.floatBalance = floatBalance;
    }

    public Double getFloatBalance() {
        return floatBalance;
    }

    /**
     * Setter/Getter for EFFECTIVE_BALANCE - table Field
     */
    public void setEffectiveBalance(Double effectiveBalance) {
        this.effectiveBalance = effectiveBalance;
    }

    public Double getEffectiveBalance() {
        return effectiveBalance;
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
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
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
     * Setter/Getter for AUTHORIZATION_STATUS - table Field
     */
    public void setAuthorizationStatus(String authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }

    public String getAuthorizationStatus() {
        return authorizationStatus;
    }

    /**
     * Setter/Getter for CLOSED_BY - table Field
     */
    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public String getClosedBy() {
        return closedBy;
    }

    /**
     * Setter/Getter for CLOSED_DT - table Field
     */
    public void setClosedDt(Date closedDt) {
        this.closedDt = closedDt;
    }

    public Date getClosedDt() {
        return closedDt;
    }

    /**
     * Setter/Getter for BASE_CURR - table Field
     */
    public void setBaseCurr(String baseCurr) {
        this.baseCurr = baseCurr;
    }

    public String getBaseCurr() {
        return baseCurr;
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
     * Setter/Getter for SHADOW_CREDIT - table Field
     */
    public void setShadowCredit(Double shadowCredit) {
        this.shadowCredit = shadowCredit;
    }

    public Double getShadowCredit() {
        return shadowCredit;
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
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter/Getter for ACT_STATUS_DT - table Field
     */
    public void setActStatusDt(Date actStatusDt) {
        this.actStatusDt = actStatusDt;
    }

    public Date getActStatusDt() {
        return actStatusDt;
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
     * Setter/Getter for LIEN_AMT - table Field
     */
    public void setLienAmt(Double lienAmt) {
        this.lienAmt = lienAmt;
    }

    public Double getLienAmt() {
        return lienAmt;
    }

    /**
     * Setter/Getter for FREEZE_AMT - table Field
     */
    public void setFreezeAmt(Double freezeAmt) {
        this.freezeAmt = freezeAmt;
    }

    public Double getFreezeAmt() {
        return freezeAmt;
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
     * Setter/Getter for COMM_ADDR_TYPE - table Field
     */
    public void setCommAddrType(String commAddrType) {
        this.commAddrType = commAddrType;
    }

    public String getCommAddrType() {
        return commAddrType;
    }

    /**
     * Setter/Getter for PRODUCT_AMT - table Field
     */
    public void setProductAmt(Double productAmt) {
        this.productAmt = productAmt;
    }

    public Double getProductAmt() {
        return productAmt;
    }

    /**
     * Setter/Getter for CATEGORY_ID - table Field
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    /**
     * Setter/Getter for FLEXI_DEPOSIT_AMT - table Field
     */
    public void setFlexiDepositAmt(Double flexiDepositAmt) {
        this.flexiDepositAmt = flexiDepositAmt;
    }

    public Double getFlexiDepositAmt() {
        return flexiDepositAmt;
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
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("actNum");
        return actNum;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("createDt", createDt));
        strB.append(getTOString("actStatusId", actStatusId));
        strB.append(getTOString("actCatId", actCatId));
        strB.append(getTOString("optModeId", optModeId));
        strB.append(getTOString("todLimit", todLimit));
        strB.append(getTOString("groupCodeId", groupCodeId));
        strB.append(getTOString("settmtModeId", settmtModeId));
        strB.append(getTOString("prevActNum", prevActNum));
        strB.append(getTOString("clearBalance", clearBalance));
        strB.append(getTOString("unclearBalance", unclearBalance));
        strB.append(getTOString("floatBalance", floatBalance));
        strB.append(getTOString("effectiveBalance", effectiveBalance));
        strB.append(getTOString("availableBalance", availableBalance));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizationStatus", authorizationStatus));
        strB.append(getTOString("closedBy", closedBy));
        strB.append(getTOString("closedDt", closedDt));
        strB.append(getTOString("baseCurr", baseCurr));
        strB.append(getTOString("lastTransDt", lastTransDt));
        strB.append(getTOString("shadowCredit", shadowCredit));
        strB.append(getTOString("shadowDebit", shadowDebit));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("actStatusDt", actStatusDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("lienAmt", lienAmt));
        strB.append(getTOString("freezeAmt", freezeAmt));
        strB.append(getTOString("totalBalance", totalBalance));
        strB.append(getTOString("commAddrType", commAddrType));
        strB.append(getTOString("productAmt", productAmt));
        strB.append(getTOString("categoryId", categoryId));
        strB.append(getTOString("flexiDepositAmt", flexiDepositAmt));
        strB.append(getTOString("acctName", acctName));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("cardActNo", cardActNo));
        strB.append(getTOString("openingAmount", openingAmount));
        strB.append(getTOString("introducer", introducer));
        strB.append(getTOString("linkingActNum", linkingActNum));
        strB.append(getTOString("atmCardLimitAmt", atmCardLimitAmt));
        strB.append(getTOString("primaryAccount", primaryAccount));
        strB.append(getTOString("upiMobileNo", upiMobileNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("createDt", createDt));
        strB.append(getTOXml("actStatusId", actStatusId));
        strB.append(getTOXml("actCatId", actCatId));
        strB.append(getTOXml("optModeId", optModeId));
        strB.append(getTOXml("todLimit", todLimit));
        strB.append(getTOXml("groupCodeId", groupCodeId));
        strB.append(getTOXml("settmtModeId", settmtModeId));
        strB.append(getTOXml("prevActNum", prevActNum));
        strB.append(getTOXml("clearBalance", clearBalance));
        strB.append(getTOXml("unclearBalance", unclearBalance));
        strB.append(getTOXml("floatBalance", floatBalance));
        strB.append(getTOXml("effectiveBalance", effectiveBalance));
        strB.append(getTOXml("availableBalance", availableBalance));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizationStatus", authorizationStatus));
        strB.append(getTOXml("closedBy", closedBy));
        strB.append(getTOXml("closedDt", closedDt));
        strB.append(getTOXml("baseCurr", baseCurr));
        strB.append(getTOXml("lastTransDt", lastTransDt));
        strB.append(getTOXml("shadowCredit", shadowCredit));
        strB.append(getTOXml("shadowDebit", shadowDebit));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("actStatusDt", actStatusDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("lienAmt", lienAmt));
        strB.append(getTOXml("freezeAmt", freezeAmt));
        strB.append(getTOXml("totalBalance", totalBalance));
        strB.append(getTOXml("commAddrType", commAddrType));
        strB.append(getTOXml("productAmt", productAmt));
        strB.append(getTOXml("categoryId", categoryId));
        strB.append(getTOXml("flexiDepositAmt", flexiDepositAmt));
        strB.append(getTOXml("acctName", acctName));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("cardActNo", cardActNo));
        strB.append(getTOXml("openingAmount", openingAmount));
        strB.append(getTOXml("introducer", introducer));
        strB.append(getTOXml("linkingActNum", linkingActNum));
        strB.append(getTOXml("atmCardLimitAmt", atmCardLimitAmt));
        strB.append(getTOXml("primaryAccount", primaryAccount));
        strB.append(getTOXml("upiMobileNo", upiMobileNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Double getOpeningAmount() {
        return openingAmount;
    }

    public void setOpeningAmount(Double openingAmount) {
        this.openingAmount = openingAmount;
    }

    

    public String getCardActNo() {
        return cardActNo;
    }

    public void setCardActNo(String cardActNo) {
        this.cardActNo = cardActNo;
    }

    public String getPrimaryAccount() {
        return primaryAccount;
    }

    public void setPrimaryAccount(String primaryAccount) {
        this.primaryAccount = primaryAccount;
    }    

    public String getUpiMobileNo() {
        return upiMobileNo;
    }

    public void setUpiMobileNo(String upiMobileNo) {
        this.upiMobileNo = upiMobileNo;
    }
    
    
    
}