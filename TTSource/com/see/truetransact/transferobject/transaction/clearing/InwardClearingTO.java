/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingTO.java
 * 
 * Created on Thu Jul 14 11:26:37 IST 2005
 */
package com.see.truetransact.transferobject.transaction.clearing;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is INWARD_CLEARING.
 */
public class InwardClearingTO extends TransferObject implements Serializable {

    private String inwardId = "";
    private String prodId = "";
    private String acctNo = "";
    private String clearingType = "";
    private Date clearingDt = null;
    private String scheduleNo = "";
    private Date instrumentDt = null;
    private Double amount = null;
    private String payeeName = "";
    private String bankCode = "";
    private String branchCode = "";
    private String status = "";
    private String instrumentNo1 = "";
    private String instrumentNo2 = "";
    private String suserId = "";
    private Date sDate = null;
    private String sRemarks = "";
    private String instrumentType = "";
    private Date inwardDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizeRemarks = "";
    private String inCurrency = "";
    private Double outputAmount = null;
    private String branchId = "";
    private String acHdId = "";
    private String prodType = "";
    private String createdBy = "";
    private String initiatedBranch = "";

    /**
     * Setter/Getter for INWARD_ID - table Field
     */
    public void setInwardId(String inwardId) {
        this.inwardId = inwardId;
    }

    public String getInwardId() {
        return inwardId;
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
     * Setter/Getter for ACCT_NO - table Field
     */
    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctNo() {
        return acctNo;
    }

    /**
     * Setter/Getter for CLEARING_TYPE - table Field
     */
    public void setClearingType(String clearingType) {
        this.clearingType = clearingType;
    }

    public String getClearingType() {
        return clearingType;
    }

    /**
     * Setter/Getter for CLEARING_DT - table Field
     */
    public void setClearingDt(Date clearingDt) {
        this.clearingDt = clearingDt;
    }

    public Date getClearingDt() {
        return clearingDt;
    }

    /**
     * Setter/Getter for SCHEDULE_NO - table Field
     */
    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    /**
     * Setter/Getter for INSTRUMENT_DT - table Field
     */
    public void setInstrumentDt(Date instrumentDt) {
        this.instrumentDt = instrumentDt;
    }

    public Date getInstrumentDt() {
        return instrumentDt;
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
     * Setter/Getter for PAYEE_NAME - table Field
     */
    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    /**
     * Setter/Getter for BANK_CODE - table Field
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
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
     * Setter/Getter for SUSER_ID - table Field
     */
    public void setSuserId(String suserId) {
        this.suserId = suserId;
    }

    public String getSuserId() {
        return suserId;
    }

    /**
     * Setter/Getter for S_DATE - table Field
     */
    public void setSDate(Date sDate) {
        this.sDate = sDate;
    }

    public Date getSDate() {
        return sDate;
    }

    /**
     * Setter/Getter for S_REMARKS - table Field
     */
    public void setSRemarks(String sRemarks) {
        this.sRemarks = sRemarks;
    }

    public String getSRemarks() {
        return sRemarks;
    }

    /**
     * Setter/Getter for INSTRUMENT_TYPE - table Field
     */
    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    /**
     * Setter/Getter for INWARD_DT - table Field
     */
    public void setInwardDt(Date inwardDt) {
        this.inwardDt = inwardDt;
    }

    public Date getInwardDt() {
        return inwardDt;
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
     * Setter/Getter for IN_CURRENCY - table Field
     */
    public void setInCurrency(String inCurrency) {
        this.inCurrency = inCurrency;
    }

    public String getInCurrency() {
        return inCurrency;
    }

    /**
     * Setter/Getter for OUTPUT_AMOUNT - table Field
     */
    public void setOutputAmount(Double outputAmount) {
        this.outputAmount = outputAmount;
    }

    public Double getOutputAmount() {
        return outputAmount;
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
     * Setter/Getter for AC_HD_ID - table Field
     */
    public void setAcHdId(String acHdId) {
        this.acHdId = acHdId;
    }

    public String getAcHdId() {
        return acHdId;
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
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter/Getter for INITIATED_BRANCH - table Field
     */
    public void setInitiatedBranch(String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }

    public String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("inwardId");
        return inwardId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("inwardId", inwardId));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOString("clearingType", clearingType));
        strB.append(getTOString("clearingDt", clearingDt));
        strB.append(getTOString("scheduleNo", scheduleNo));
        strB.append(getTOString("instrumentDt", instrumentDt));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("payeeName", payeeName));
        strB.append(getTOString("bankCode", bankCode));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("instrumentNo1", instrumentNo1));
        strB.append(getTOString("instrumentNo2", instrumentNo2));
        strB.append(getTOString("suserId", suserId));
        strB.append(getTOString("sDate", sDate));
        strB.append(getTOString("sRemarks", sRemarks));
        strB.append(getTOString("instrumentType", instrumentType));
        strB.append(getTOString("inwardDt", inwardDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("inCurrency", inCurrency));
        strB.append(getTOString("outputAmount", outputAmount));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("inwardId", inwardId));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("clearingType", clearingType));
        strB.append(getTOXml("clearingDt", clearingDt));
        strB.append(getTOXml("scheduleNo", scheduleNo));
        strB.append(getTOXml("instrumentDt", instrumentDt));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("payeeName", payeeName));
        strB.append(getTOXml("bankCode", bankCode));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("instrumentNo1", instrumentNo1));
        strB.append(getTOXml("instrumentNo2", instrumentNo2));
        strB.append(getTOXml("suserId", suserId));
        strB.append(getTOXml("sDate", sDate));
        strB.append(getTOXml("sRemarks", sRemarks));
        strB.append(getTOXml("instrumentType", instrumentType));
        strB.append(getTOXml("inwardDt", inwardDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("inCurrency", inCurrency));
        strB.append(getTOXml("outputAmount", outputAmount));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}