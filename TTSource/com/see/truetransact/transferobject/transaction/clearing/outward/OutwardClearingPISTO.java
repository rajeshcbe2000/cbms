/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OutwardClearingPISTO.java
 * 
 * Created on Fri Aug 06 12:38:28 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.transaction.clearing.outward;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PAY_IN_SLIP.
 */
public class OutwardClearingPISTO extends TransferObject implements Serializable {

    private String batchId = "";
    private String payInSlipId = "";
    private Date payInSlipDt = null;
    private String prodId = "";
    private String acctNo = null;
    private Double amount = null;
    private String remarks = "";
    private String status = "";
    private Double convertAmt = null;
    private String acHdId = "";
    private String statusBy = "";
    private String branchId = "";
    private Date statusDt = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizeStatus = null;
    private String authorizeRemarks = "";
    private String prodType = "";

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
     * Setter/Getter for PAY_IN_SLIP_ID - table Field
     */
    public void setPayInSlipId(String payInSlipId) {
        this.payInSlipId = payInSlipId;
    }

    public String getPayInSlipId() {
        return payInSlipId;
    }

    /**
     * Setter/Getter for PAY_IN_SLIP_DT - table Field
     */
    public void setPayInSlipDt(Date payInSlipDt) {
        this.payInSlipDt = payInSlipDt;
    }

    public Date getPayInSlipDt() {
        return payInSlipDt;
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
     * Setter/Getter for AMOUNT - table Field
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
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
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for CONVERT_AMT - table Field
     */
    public void setConvertAmt(Double convertAmt) {
        this.convertAmt = convertAmt;
    }

    public Double getConvertAmt() {
        return convertAmt;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
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
     * Setter/Getter for PROD_TYPE - table Field
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("batchId" + KEY_VAL_SEPARATOR + "payInSlipId");
        return batchId + KEY_VAL_SEPARATOR + payInSlipId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("payInSlipId", payInSlipId));
        strB.append(getTOString("payInSlipDt", payInSlipDt));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("convertAmt", convertAmt));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("prodType", prodType));
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
        strB.append(getTOXml("payInSlipId", payInSlipId));
        strB.append(getTOXml("payInSlipDt", payInSlipDt));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("convertAmt", convertAmt));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}