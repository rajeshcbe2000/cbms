/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashTransactionTO.java
 * 
 * Created on Thu Aug 12 12:40:27 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.transaction.auditEntry;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CASH_TRANS.
 */
public class AuditEntryTO extends TransferObject implements Serializable {

    private String transId = "";
    private String acHdId = "";
    private String actNum = "";
    private Double inpAmount = null;
    private String inpCurr = "";
    private Double amount = null;
    private Date transDt = null;
    private String transType = "";
    private String instType = "";
    private Date instDt = null;
    private String tokenNo = null;
    private String initTransId = "";
    private String initChannType = "";
    private String particulars = "";
    private String narration = "";
    private String status = "";
    private String instrumentNo1 = "";
    private String instrumentNo2 = "";
    private Double availableBalance = null;
    private String prodId = "";
    private String prodType = "";
    private String authorizeStatus = null;
    private String authorizeStatus_2 = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizeRemarks = "";
    private String statusBy = "";
    private String branchId = "";
    private Date statusDt = null;
    private String linkBatchId = null;
    private String initiatedBranch = "";
    private Date linkBatchDt = null;
    private String panNo = "";
    private String loanHierarchy = "";
    private String shift = "";
    private String screenName = "";

    /**
     * Setter/Getter for TRANS_ID - table Field
     */
    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransId() {
        return transId;
    }

    /**
     * Setter/Getter for AC_HD_ID - table Field
     */
    public void setAcHdId(String acHdId) {
        this.acHdId = acHdId;
    }

    public String getAcHdId() {
        return acHdId;
    }

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for INP_AMOUNT - table Field
     */
    public void setInpAmount(Double inpAmount) {
        this.inpAmount = inpAmount;
    }

    public Double getInpAmount() {
        return inpAmount;
    }

    /**
     * Setter/Getter for INP_CURR - table Field
     */
    public void setInpCurr(String inpCurr) {
        this.inpCurr = inpCurr;
    }

    public String getInpCurr() {
        return inpCurr;
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
     * Setter/Getter for TRANS_DT - table Field
     */
    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }

    public Date getTransDt() {
        return transDt;
    }

    /**
     * Setter/Getter for TRANS_TYPE - table Field
     */
    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransType() {
        return transType;
    }

    /**
     * Setter/Getter for INST_TYPE - table Field
     */
    public void setInstType(String instType) {
        this.instType = instType;
    }

    public String getInstType() {
        return instType;
    }

    /**
     * Setter/Getter for INST_DT - table Field
     */
    public void setInstDt(Date instDt) {
        this.instDt = instDt;
    }

    public Date getInstDt() {
        return instDt;
    }

    /**
     * Setter/Getter for TOKEN_NO - table Field
     */
    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    /**
     * Setter/Getter for INIT_TRANS_ID - table Field
     */
    public void setInitTransId(String initTransId) {
        this.initTransId = initTransId;
    }

    public String getInitTransId() {
        return initTransId;
    }

    /**
     * Setter/Getter for INIT_CHANN_TYPE - table Field
     */
    public void setInitChannType(String initChannType) {
        this.initChannType = initChannType;
    }

    public String getInitChannType() {
        return initChannType;
    }

    /**
     * Setter/Getter for PARTICULARS - table Field
     */
    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getParticulars() {
        return particulars;
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
     * Setter/Getter for INSTRUMENT_NO1 - table Field
     */
    public void setInstrumentNo1(String instrumentNo1) {
        this.instrumentNo1 = instrumentNo1;
    }

    public String getInstrumentNo1() {
        return instrumentNo1;
    }

    /**
     * Setter/Getter for INSTRUMENT_NO2 - table Field
     */
    public void setInstrumentNo2(String instrumentNo2) {
        this.instrumentNo2 = instrumentNo2;
    }

    public String getInstrumentNo2() {
        return instrumentNo2;
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
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for PROD_TYPE - table Field
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
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
     * Setter/Getter for AUTHORIZE_REMARKS - table Field
     */
    public void setAuthorizeRemarks(String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    public String getAuthorizeRemarks() {
        return authorizeRemarks;
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
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
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
        setKeyColumns("transId");
        return transId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("inpAmount", inpAmount));
        strB.append(getTOString("inpCurr", inpCurr));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("instType", instType));
        strB.append(getTOString("instDt", instDt));
        strB.append(getTOString("tokenNo", tokenNo));
        strB.append(getTOString("initTransId", initTransId));
        strB.append(getTOString("initChannType", initChannType));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("narration", narration));
        strB.append(getTOString("status", status));
        strB.append(getTOString("instrumentNo1", instrumentNo1));
        strB.append(getTOString("instrumentNo2", instrumentNo2));
        strB.append(getTOString("availableBalance", availableBalance));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("linkBatchId", linkBatchId));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("linkBatchDt", linkBatchDt));
        strB.append(getTOString("panNo", panNo));
        strB.append(getTOString("loanHierarchy", loanHierarchy));
        strB.append(getTOString("authorizeStatus_2", authorizeStatus_2));
        strB.append(getTOString("shift", shift));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("inpAmount", inpAmount));
        strB.append(getTOXml("inpCurr", inpCurr));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("instType", instType));
        strB.append(getTOXml("instDt", instDt));
        strB.append(getTOXml("tokenNo", tokenNo));
        strB.append(getTOXml("initTransId", initTransId));
        strB.append(getTOXml("initChannType", initChannType));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("narration", narration));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("instrumentNo1", instrumentNo1));
        strB.append(getTOXml("instrumentNo2", instrumentNo2));
        strB.append(getTOXml("availableBalance", availableBalance));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("linkBatchId", linkBatchId));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("linkBatchDt", linkBatchDt));
        strB.append(getTOXml("panNo", panNo));
        strB.append(getTOXml("loanHierarchy", loanHierarchy));
        strB.append(getTOXml("authorizeStatus_2", authorizeStatus_2));
        strB.append(getTOXml("shift", shift));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property linkBatchId.
     *
     * @return Value of property linkBatchId.
     */
    public java.lang.String getLinkBatchId() {
        return linkBatchId;
    }

    /**
     * Setter for property linkBatchId.
     *
     * @param linkBatchId New value of property linkBatchId.
     */
    public void setLinkBatchId(java.lang.String linkBatchId) {
        this.linkBatchId = linkBatchId;
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
     * Getter for property linkBatchDt.
     *
     * @return Value of property linkBatchDt.
     */
    public java.util.Date getLinkBatchDt() {
        return linkBatchDt;
    }

    /**
     * Setter for property linkBatchDt.
     *
     * @param linkBatchDt New value of property linkBatchDt.
     */
    public void setLinkBatchDt(java.util.Date linkBatchDt) {
        this.linkBatchDt = linkBatchDt;
    }

    /**
     * Getter for property panNo.
     *
     * @return Value of property panNo.
     */
    public java.lang.String getPanNo() {
        return panNo;
    }

    /**
     * Setter for property panNo.
     *
     * @param panNo New value of property panNo.
     */
    public void setPanNo(java.lang.String panNo) {
        this.panNo = panNo;
    }

    /**
     * Getter for property loanHierarchy.
     *
     * @return Value of property loanHierarchy.
     */
    public java.lang.String getLoanHierarchy() {
        return loanHierarchy;
    }

    /**
     * Setter for property loanHierarchy.
     *
     * @param loanHierarchy New value of property loanHierarchy.
     */
    public void setLoanHierarchy(java.lang.String loanHierarchy) {
        this.loanHierarchy = loanHierarchy;
    }

    /**
     * Getter for property authorizeStatus_2.
     *
     * @return Value of property authorizeStatus_2.
     */
    public java.lang.String getAuthorizeStatus_2() {
        return authorizeStatus_2;
    }

    /**
     * Setter for property authorizeStatus_2.
     *
     * @param authorizeStatus_2 New value of property authorizeStatus_2.
     */
    public void setAuthorizeStatus_2(java.lang.String authorizeStatus_2) {
        this.authorizeStatus_2 = authorizeStatus_2;
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
     * Getter for property shift.
     *
     * @return Value of property shift.
     */
    public java.lang.String getShift() {
        return shift;
    }

    /**
     * Setter for property shift.
     *
     * @param shift New value of property shift.
     */
    public void setShift(java.lang.String shift) {
        this.shift = shift;
    }

    /**
     * Getter for property screenName.
     *
     * @return Value of property screenName.
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Setter for property screenName.
     *
     * @param screenName New value of property screenName.
     */
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}