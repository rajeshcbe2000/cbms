/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceIssueTO.java
 *
 * Created on Fri Sep 10 12:33:13 PDT 2004
 */
package com.see.truetransact.transferobject.remittance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is REMIT_ISSUE.
 */
/**
 *
 * @author Prasath.T
 */
public class RemittanceIssueTO extends TransferObject implements Serializable {

    private String batchId = "";
    private Date batchDt = null;
    private String issueId = "";
    private String prodId = "";
    private String city = "";
    private String draweeBank = "";
    private String draweeBranchCode = "";
    private String favouring = "";
    private String remitType = "";
    private Double amount = null;
    private String panGirNo = "";
    private String category = "";
    private Double exchange = null;
    private Double postage = null;
    private Double otherCharges = null;
    private Double totalAmt = null;
    private String remarks = "";
    private String payeeAcctHead = "";
    private String payeeProdId = "";
    private String payeeProdType = "";
    private String crossing = "";
    private String payeeAcctNo = "";
    private String status = "";
    private String instrumentNo1 = "";
    private String instrumentNo2 = "";
    private String variableNo = "";
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;
    private String authorizeRemark = "";
    private Date revalidateDt = null;
    private Double revalidateCharge = null;
    private Date revalidateExpiryDt = null;
    private String revalidateRemarks = "";
    private Date duplicateDt = null;
    private Double duplicateCharge = null;
    private String duplicateRemarks = "";
    private Date cancelDt = null;
    private Double cancelCharge = null;
    private String cancelRemarks = "";
    private String paidStatus = "";
    private String branchId = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String revalidateTrans = "";
    private String duplicateTrans = "";
    private Double dupServTax = null;
    private Double revServTax = null;
    private Double exgCal = null;
    private String remitForFlag = "";

    /**
     * Setter/Getter for BATCH_ID - table Field
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchId() {
        return batchId;
    }

    /**
     * Setter/Getter for BATCH_DT - table Field
     */
    public void setBatchDt(Date batchDt) {
        this.batchDt = batchDt;
    }

    public Date getBatchDt() {
        return batchDt;
    }

    /**
     * Setter/Getter for ISSUE_ID - table Field
     */
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getIssueId() {
        return issueId;
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
     * Setter/Getter for CITY - table Field
     */
    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    /**
     * Setter/Getter for DRAWEE_BANK - table Field
     */
    public void setDraweeBank(String draweeBank) {
        this.draweeBank = draweeBank;
    }

    public String getDraweeBank() {
        return draweeBank;
    }

    /**
     * Setter/Getter for DRAWEE_BRANCH_CODE - table Field
     */
    public void setDraweeBranchCode(String draweeBranchCode) {
        this.draweeBranchCode = draweeBranchCode;
    }

    public String getDraweeBranchCode() {
        return draweeBranchCode;
    }

    /**
     * Setter/Getter for FAVOURING - table Field
     */
    public void setFavouring(String favouring) {
        this.favouring = favouring;
    }

    public String getFavouring() {
        return favouring;
    }

    /**
     * Setter/Getter for REMIT_TYPE - table Field
     */
    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    public String getRemitType() {
        return remitType;
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
     * Setter/Getter for PAN_GIR_NO - table Field
     */
    public void setPanGirNo(String panGirNo) {
        this.panGirNo = panGirNo;
    }

    public String getPanGirNo() {
        return panGirNo;
    }

    /**
     * Setter/Getter for CATEGORY - table Field
     */
    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Setter/Getter for EXCHANGE - table Field
     */
    public void setExchange(Double exchange) {
        this.exchange = exchange;
    }

    public Double getExchange() {
        return exchange;
    }

    /**
     * Setter/Getter for POSTAGE - table Field
     */
    public void setPostage(Double postage) {
        this.postage = postage;
    }

    public Double getPostage() {
        return postage;
    }

    /**
     * Setter/Getter for OTHER_CHARGES - table Field
     */
    public void setOtherCharges(Double otherCharges) {
        this.otherCharges = otherCharges;
    }

    public Double getOtherCharges() {
        return otherCharges;
    }

    /**
     * Setter/Getter for TOTAL_AMT - table Field
     */
    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Double getTotalAmt() {
        return totalAmt;
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
     * Setter/Getter for PAYEE_ACCT_HEAD - table Field
     */
    public void setPayeeAcctHead(String payeeAcctHead) {
        this.payeeAcctHead = payeeAcctHead;
    }

    public String getPayeeAcctHead() {
        return payeeAcctHead;
    }

    /**
     * Setter/Getter for CROSSING - table Field
     */
    public void setCrossing(String crossing) {
        this.crossing = crossing;
    }

    public String getCrossing() {
        return crossing;
    }

    /**
     * Setter/Getter for PAYEE_ACCT_NO - table Field
     */
    public void setPayeeAcctNo(String payeeAcctNo) {
        this.payeeAcctNo = payeeAcctNo;
    }

    public String getPayeeAcctNo() {
        return payeeAcctNo;
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
     * Setter/Getter for VARIABLE_NO - table Field
     */
    public void setVariableNo(String variableNo) {
        this.variableNo = variableNo;
    }

    public String getVariableNo() {
        return variableNo;
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
     * Setter/Getter for AUTHORIZE_USER - table Field
     */
    public void setAuthorizeUser(String authorizeUser) {
        this.authorizeUser = authorizeUser;
    }

    public String getAuthorizeUser() {
        return authorizeUser;
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
     * Setter/Getter for AUTHORIZE_REMARK - table Field
     */
    public void setAuthorizeRemark(String authorizeRemark) {
        this.authorizeRemark = authorizeRemark;
    }

    public String getAuthorizeRemark() {
        return authorizeRemark;
    }

    /**
     * Setter/Getter for REVALIDATE_DT - table Field
     */
    public void setRevalidateDt(Date revalidateDt) {
        this.revalidateDt = revalidateDt;
    }

    public Date getRevalidateDt() {
        return revalidateDt;
    }

    /**
     * Setter/Getter for REVALIDATE_CHARGE - table Field
     */
    public void setRevalidateCharge(Double revalidateCharge) {
        this.revalidateCharge = revalidateCharge;
    }

    public Double getRevalidateCharge() {
        return revalidateCharge;
    }

    /**
     * Setter/Getter for REVALIDATE_EXPIRY_DT - table Field
     */
    public void setRevalidateExpiryDt(Date revalidateExpiryDt) {
        this.revalidateExpiryDt = revalidateExpiryDt;
    }

    public Date getRevalidateExpiryDt() {
        return revalidateExpiryDt;
    }

    /**
     * Setter/Getter for REVALIDATE_REMARKS - table Field
     */
    public void setRevalidateRemarks(String revalidateRemarks) {
        this.revalidateRemarks = revalidateRemarks;
    }

    public String getRevalidateRemarks() {
        return revalidateRemarks;
    }

    /**
     * Setter/Getter for DUPLICATE_DT - table Field
     */
    public void setDuplicateDt(Date duplicateDt) {
        this.duplicateDt = duplicateDt;
    }

    public Date getDuplicateDt() {
        return duplicateDt;
    }

    /**
     * Setter/Getter for DUPLICATE_CHARGE - table Field
     */
    public void setDuplicateCharge(Double duplicateCharge) {
        this.duplicateCharge = duplicateCharge;
    }

    public Double getDuplicateCharge() {
        return duplicateCharge;
    }

    /**
     * Setter/Getter for DUPLICATE_REMARKS - table Field
     */
    public void setDuplicateRemarks(String duplicateRemarks) {
        this.duplicateRemarks = duplicateRemarks;
    }

    public String getDuplicateRemarks() {
        return duplicateRemarks;
    }

    /**
     * Setter/Getter for CANCEL_DT - table Field
     */
    public void setCancelDt(Date cancelDt) {
        this.cancelDt = cancelDt;
    }

    public Date getCancelDt() {
        return cancelDt;
    }

    /**
     * Setter/Getter for CANCEL_CHARGE - table Field
     */
    public void setCancelCharge(Double cancelCharge) {
        this.cancelCharge = cancelCharge;
    }

    public Double getCancelCharge() {
        return cancelCharge;
    }

    /**
     * Setter/Getter for CANCEL_REMARKS - table Field
     */
    public void setCancelRemarks(String cancelRemarks) {
        this.cancelRemarks = cancelRemarks;
    }

    public String getCancelRemarks() {
        return cancelRemarks;
    }

    /**
     * Setter/Getter for PAID_STATUS - table Field
     */
    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getPaidStatus() {
        return paidStatus;
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
     * Setter/Getter for REVALIDATE_TRANS - table Field
     */
    public void setRevalidateTrans(String revalidateTrans) {
        this.revalidateTrans = revalidateTrans;
    }

    public String getRevalidateTrans() {
        return revalidateTrans;
    }

    /**
     * Setter/Getter for DUPLICATE_TRANS - table Field
     */
    public void setDuplicateTrans(String duplicateTrans) {
        this.duplicateTrans = duplicateTrans;
    }

    public String getDuplicateTrans() {
        return duplicateTrans;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("batchId" + KEY_VAL_SEPARATOR + "issueId");
        return batchId + KEY_VAL_SEPARATOR + issueId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("batchDt", batchDt));
        strB.append(getTOString("issueId", issueId));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("city", city));
        strB.append(getTOString("draweeBank", draweeBank));
        strB.append(getTOString("draweeBranchCode", draweeBranchCode));
        strB.append(getTOString("favouring", favouring));
        strB.append(getTOString("remitType", remitType));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("panGirNo", panGirNo));
        strB.append(getTOString("category", category));
        strB.append(getTOString("exchange", exchange));
        strB.append(getTOString("postage", postage));
        strB.append(getTOString("otherCharges", otherCharges));
        strB.append(getTOString("totalAmt", totalAmt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("payeeAcctHead", payeeAcctHead));
        strB.append(getTOString("crossing", crossing));
        strB.append(getTOString("payeeAcctNo", payeeAcctNo));
        strB.append(getTOString("payeeProdType", payeeProdType));
        strB.append(getTOString("payeeProdId", payeeProdId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("instrumentNo1", instrumentNo1));
        strB.append(getTOString("instrumentNo2", instrumentNo2));
        strB.append(getTOString("variableNo", variableNo));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeRemark", authorizeRemark));
        strB.append(getTOString("revalidateDt", revalidateDt));
        strB.append(getTOString("revalidateCharge", revalidateCharge));
        strB.append(getTOString("revalidateExpiryDt", revalidateExpiryDt));
        strB.append(getTOString("revalidateRemarks", revalidateRemarks));
        strB.append(getTOString("duplicateDt", duplicateDt));
        strB.append(getTOString("duplicateCharge", duplicateCharge));
        strB.append(getTOString("duplicateRemarks", duplicateRemarks));
        strB.append(getTOString("cancelDt", cancelDt));
        strB.append(getTOString("cancelCharge", cancelCharge));
        strB.append(getTOString("cancelRemarks", cancelRemarks));
        strB.append(getTOString("paidStatus", paidStatus));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("revalidateTrans", revalidateTrans));
        strB.append(getTOString("duplicateTrans", duplicateTrans));
        strB.append(getTOString("dupServTax", dupServTax));
        strB.append(getTOString("revServTax", revServTax));
        strB.append(getTOString("exgCal", exgCal));
        strB.append(getTOString("remitForFlag", remitForFlag));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("batchId", batchId));
        strB.append(getTOXml("batchDt", batchDt));
        strB.append(getTOXml("issueId", issueId));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("draweeBank", draweeBank));
        strB.append(getTOXml("draweeBranchCode", draweeBranchCode));
        strB.append(getTOXml("favouring", favouring));
        strB.append(getTOXml("remitType", remitType));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("panGirNo", panGirNo));
        strB.append(getTOXml("category", category));
        strB.append(getTOXml("exchange", exchange));
        strB.append(getTOXml("postage", postage));
        strB.append(getTOXml("otherCharges", otherCharges));
        strB.append(getTOXml("totalAmt", totalAmt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("payeeAcctHead", payeeAcctHead));
        strB.append(getTOXml("payeeProdType", payeeProdType));
        strB.append(getTOXml("payeeProdId", payeeProdId));
        strB.append(getTOXml("crossing", crossing));
        strB.append(getTOXml("payeeAcctNo", payeeAcctNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("instrumentNo1", instrumentNo1));
        strB.append(getTOXml("instrumentNo2", instrumentNo2));
        strB.append(getTOXml("variableNo", variableNo));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeRemark", authorizeRemark));
        strB.append(getTOXml("revalidateDt", revalidateDt));
        strB.append(getTOXml("revalidateCharge", revalidateCharge));
        strB.append(getTOXml("revalidateExpiryDt", revalidateExpiryDt));
        strB.append(getTOXml("revalidateRemarks", revalidateRemarks));
        strB.append(getTOXml("duplicateDt", duplicateDt));
        strB.append(getTOXml("duplicateCharge", duplicateCharge));
        strB.append(getTOXml("duplicateRemarks", duplicateRemarks));
        strB.append(getTOXml("cancelDt", cancelDt));
        strB.append(getTOXml("cancelCharge", cancelCharge));
        strB.append(getTOXml("cancelRemarks", cancelRemarks));
        strB.append(getTOXml("paidStatus", paidStatus));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("revalidateTrans", revalidateTrans));
        strB.append(getTOXml("duplicateTrans", duplicateTrans));
        strB.append(getTOXml("dupServTax", dupServTax));
        strB.append(getTOXml("revServTax", revServTax));
        strB.append(getTOXml("exgCal", exgCal));
        strB.append(getTOXml("remitForFlag", remitForFlag));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property payeeProdId.
     *
     * @return Value of property payeeProdId.
     */
    public java.lang.String getPayeeProdId() {
        return payeeProdId;
    }

    /**
     * Setter for property payeeProdId.
     *
     * @param payeeProdId New value of property payeeProdId.
     */
    public void setPayeeProdId(java.lang.String payeeProdId) {
        this.payeeProdId = payeeProdId;
    }

    /**
     * Getter for property payeeProdType.
     *
     * @return Value of property payeeProdType.
     */
    public java.lang.String getPayeeProdType() {
        return payeeProdType;
    }

    /**
     * Setter for property payeeProdType.
     *
     * @param payeeProdType New value of property payeeProdType.
     */
    public void setPayeeProdType(java.lang.String payeeProdType) {
        this.payeeProdType = payeeProdType;
    }

    /**
     * Getter for property dupServTax.
     *
     * @return Value of property dupServTax.
     */
    public java.lang.Double getDupServTax() {
        return dupServTax;
    }

    /**
     * Setter for property dupServTax.
     *
     * @param dupServTax New value of property dupServTax.
     */
    public void setDupServTax(java.lang.Double dupServTax) {
        this.dupServTax = dupServTax;
    }

    /**
     * Getter for property revServTax.
     *
     * @return Value of property revServTax.
     */
    public java.lang.Double getRevServTax() {
        return revServTax;
    }

    /**
     * Setter for property revServTax.
     *
     * @param revServTax New value of property revServTax.
     */
    public void setRevServTax(java.lang.Double revServTax) {
        this.revServTax = revServTax;
    }

    /**
     * Getter for property exgCal.
     *
     * @return Value of property exgCal.
     */
    public java.lang.Double getExgCal() {
        return exgCal;
    }

    /**
     * Setter for property exgCal.
     *
     * @param exgCal New value of property exgCal.
     */
    public void setExgCal(java.lang.Double exgCal) {
        this.exgCal = exgCal;
    }

    /**
     * Getter for property remitForFlag.
     *
     * @return Value of property remitForFlag.
     */
    public java.lang.String getRemitForFlag() {
        return remitForFlag;
    }

    /**
     * Setter for property remitForFlag.
     *
     * @param remitForFlag New value of property remitForFlag.
     */
    public void setRemitForFlag(java.lang.String remitForFlag) {
        this.remitForFlag = remitForFlag;
    }
}