/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChequeBookStopPaymentTO.java
 * 
 * Created on Fri Jan 21 11:18:56 IST 2005
 */
package com.see.truetransact.transferobject.supporting.chequebook;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CHEQUE_STOP_PAYMENT.
 */
public class ChequeBookStopPaymentTO extends TransferObject implements Serializable {

    private String chqStopId = "";
    private Date chqStopDt = null;
    private String prodId = "";
    private String acctNo = "";
    private String startChqNo1 = "";
    private String startChqNo2 = "";
    private String endChqNo1 = "";
    private String endChqNo2 = "";
    private Date chqDt = null;
    private String leaf = "";
    private String payeeName = "";
    private Double chqAmt = null;
    private Double stopPayChrg = null;
    private String stopPayReason = "";
    private String stopStatus = "";
    private String status = "";
    private String authorizeStatus = null;
    private Date authorizeDt = null;
    private String authorizeBy = "";
    private String authorizeRemarks = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String prodType = "";
    private String branchId = "";
    private Date chqRevokeDt = null;

    /**
     * Setter/Getter for CHQ_STOP_ID - table Field
     */
    public void setChqStopId(String chqStopId) {
        this.chqStopId = chqStopId;
    }

    public String getChqStopId() {
        return chqStopId;
    }

    /**
     * Setter/Getter for CHQ_STOP_DT - table Field
     */
    public void setChqStopDt(Date chqStopDt) {
        this.chqStopDt = chqStopDt;
    }

    public Date getChqStopDt() {
        return chqStopDt;
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
     * Setter/Getter for START_CHQ_NO1 - table Field
     */
    public void setStartChqNo1(String startChqNo1) {
        this.startChqNo1 = startChqNo1;
    }

    public String getStartChqNo1() {
        return startChqNo1;
    }

    /**
     * Setter/Getter for START_CHQ_NO2 - table Field
     */
    public void setStartChqNo2(String startChqNo2) {
        this.startChqNo2 = startChqNo2;
    }

    public String getStartChqNo2() {
        return startChqNo2;
    }

    /**
     * Setter/Getter for END_CHQ_NO1 - table Field
     */
    public void setEndChqNo1(String endChqNo1) {
        this.endChqNo1 = endChqNo1;
    }

    public String getEndChqNo1() {
        return endChqNo1;
    }

    /**
     * Setter/Getter for END_CHQ_NO2 - table Field
     */
    public void setEndChqNo2(String endChqNo2) {
        this.endChqNo2 = endChqNo2;
    }

    public String getEndChqNo2() {
        return endChqNo2;
    }

    /**
     * Setter/Getter for CHQ_DT - table Field
     */
    public void setChqDt(Date chqDt) {
        this.chqDt = chqDt;
    }

    public Date getChqDt() {
        return chqDt;
    }

    /**
     * Setter/Getter for LEAF - table Field
     */
    public void setLeaf(String leaf) {
        this.leaf = leaf;
    }

    public String getLeaf() {
        return leaf;
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
     * Setter/Getter for CHQ_AMT - table Field
     */
    public void setChqAmt(Double chqAmt) {
        this.chqAmt = chqAmt;
    }

    public Double getChqAmt() {
        return chqAmt;
    }

    /**
     * Setter/Getter for STOP_PAY_CHRG - table Field
     */
    public void setStopPayChrg(Double stopPayChrg) {
        this.stopPayChrg = stopPayChrg;
    }

    public Double getStopPayChrg() {
        return stopPayChrg;
    }

    /**
     * Setter/Getter for STOP_PAY_REASON - table Field
     */
    public void setStopPayReason(String stopPayReason) {
        this.stopPayReason = stopPayReason;
    }

    public String getStopPayReason() {
        return stopPayReason;
    }

    /**
     * Setter/Getter for STOP_STATUS - table Field
     */
    public void setStopStatus(String stopStatus) {
        this.stopStatus = stopStatus;
    }

    public String getStopStatus() {
        return stopStatus;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
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
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
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
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
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
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("chqStopId");
        return chqStopId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("chqStopId", chqStopId));
        strB.append(getTOString("chqStopDt", chqStopDt));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOString("startChqNo1", startChqNo1));
        strB.append(getTOString("startChqNo2", startChqNo2));
        strB.append(getTOString("endChqNo1", endChqNo1));
        strB.append(getTOString("endChqNo2", endChqNo2));
        strB.append(getTOString("chqDt", chqDt));
        strB.append(getTOString("leaf", leaf));
        strB.append(getTOString("payeeName", payeeName));
        strB.append(getTOString("chqAmt", chqAmt));
        strB.append(getTOString("stopPayChrg", stopPayChrg));
        strB.append(getTOString("stopPayReason", stopPayReason));
        strB.append(getTOString("stopStatus", stopStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("chqRevokeDt", chqRevokeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("chqStopId", chqStopId));
        strB.append(getTOXml("chqStopDt", chqStopDt));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("startChqNo1", startChqNo1));
        strB.append(getTOXml("startChqNo2", startChqNo2));
        strB.append(getTOXml("endChqNo1", endChqNo1));
        strB.append(getTOXml("endChqNo2", endChqNo2));
        strB.append(getTOXml("chqDt", chqDt));
        strB.append(getTOXml("leaf", leaf));
        strB.append(getTOXml("payeeName", payeeName));
        strB.append(getTOXml("chqAmt", chqAmt));
        strB.append(getTOXml("stopPayChrg", stopPayChrg));
        strB.append(getTOXml("stopPayReason", stopPayReason));
        strB.append(getTOXml("stopStatus", stopStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("chqRevokeDt", chqRevokeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property chqRevokeDt.
     *
     * @return Value of property chqRevokeDt.
     */
    public java.util.Date getChqRevokeDt() {
        return chqRevokeDt;
    }

    /**
     * Setter for property chqRevokeDt.
     *
     * @param chqRevokeDt New value of property chqRevokeDt.
     */
    public void setChqRevokeDt(java.util.Date chqRevokeDt) {
        this.chqRevokeDt = chqRevokeDt;
    }
}