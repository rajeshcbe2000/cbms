/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChequeBookTO.java
 * 
 * Created on Mon Feb 28 12:44:05 IST 2005
 */
package com.see.truetransact.transferobject.supporting.chequebook;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CHEQUE_ISSUE.
 */
public class ChequeBookTO extends TransferObject implements Serializable {

    private String chqIssueId = "";
    private Date chqIssueDt = null;
    private String prodId = "";
    private String acctNo = "";
    private Double unpaidChq = null;
    private Double chqReturned = null;
    private Double chqIssue = null;
    private String chqDelivery = "";
    private Double noLeaves = null;
    private Double noChqBooks = null;
    private String startChqNo1 = "";
    private String startChqNo2 = "";
    private String endChqNo1 = "";
    private String endChqNo2 = "";
    private Double chqBkSeriesFrom = null;
    private Double chqBkSeriesTo = null;
    private Double chargesCollected = null;
    private String remarks = "";
    private String status = "";
    private String acctNames = "";
    private String authorizeStatus = null;
    private Date authorizeDt = null;
    private String authorizeBy = "";
    private String authorizeRemarks = "";
    private String chequeSubType = "";
    private String prodType = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String branchId = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String transOutId = "";
    private String stopStatus = "";

    /**
     * Setter/Getter for CHQ_ISSUE_ID - table Field
     */
    public void setChqIssueId(String chqIssueId) {
        this.chqIssueId = chqIssueId;
    }

    public String getChqIssueId() {
        return chqIssueId;
    }

    /**
     * Setter/Getter for CHQ_ISSUE_DT - table Field
     */
    public void setChqIssueDt(Date chqIssueDt) {
        this.chqIssueDt = chqIssueDt;
    }

    public Date getChqIssueDt() {
        return chqIssueDt;
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
     * Setter/Getter for UNPAID_CHQ - table Field
     */
    public void setUnpaidChq(Double unpaidChq) {
        this.unpaidChq = unpaidChq;
    }

    public Double getUnpaidChq() {
        return unpaidChq;
    }

    /**
     * Setter/Getter for CHQ_RETURNED - table Field
     */
    public void setChqReturned(Double chqReturned) {
        this.chqReturned = chqReturned;
    }

    public Double getChqReturned() {
        return chqReturned;
    }

    /**
     * Setter/Getter for CHQ_ISSUE - table Field
     */
    public void setChqIssue(Double chqIssue) {
        this.chqIssue = chqIssue;
    }

    public Double getChqIssue() {
        return chqIssue;
    }

    /**
     * Setter/Getter for CHQ_DELIVERY - table Field
     */
    public void setChqDelivery(String chqDelivery) {
        this.chqDelivery = chqDelivery;
    }

    public String getChqDelivery() {
        return chqDelivery;
    }

    /**
     * Setter/Getter for NO_LEAVES - table Field
     */
    public void setNoLeaves(Double noLeaves) {
        this.noLeaves = noLeaves;
    }

    public Double getNoLeaves() {
        return noLeaves;
    }

    /**
     * Setter/Getter for NO_CHQ_BOOKS - table Field
     */
    public void setNoChqBooks(Double noChqBooks) {
        this.noChqBooks = noChqBooks;
    }

    public Double getNoChqBooks() {
        return noChqBooks;
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
     * Setter/Getter for CHQ_BK_SERIES_FROM - table Field
     */
    public void setChqBkSeriesFrom(Double chqBkSeriesFrom) {
        this.chqBkSeriesFrom = chqBkSeriesFrom;
    }

    public Double getChqBkSeriesFrom() {
        return chqBkSeriesFrom;
    }

    /**
     * Setter/Getter for CHQ_BK_SERIES_TO - table Field
     */
    public void setChqBkSeriesTo(Double chqBkSeriesTo) {
        this.chqBkSeriesTo = chqBkSeriesTo;
    }

    public Double getChqBkSeriesTo() {
        return chqBkSeriesTo;
    }

    /**
     * Setter/Getter for CHARGES_COLLECTED - table Field
     */
    public void setChargesCollected(Double chargesCollected) {
        this.chargesCollected = chargesCollected;
    }

    public Double getChargesCollected() {
        return chargesCollected;
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
     * Setter/Getter for ACCT_NAMES - table Field
     */
    public void setAcctNames(String acctNames) {
        this.acctNames = acctNames;
    }

    public String getAcctNames() {
        return acctNames;
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
     * Setter/Getter for CHEQUE_SUB_TYPE - table Field
     */
    public void setChequeSubType(String chequeSubType) {
        this.chequeSubType = chequeSubType;
    }

    public String getChequeSubType() {
        return chequeSubType;
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
     * Setter/Getter for TRANS_OUT_ID - table Field
     */
    public void setTransOutId(String transOutId) {
        this.transOutId = transOutId;
    }

    public String getTransOutId() {
        return transOutId;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("chqIssueId");
        return chqIssueId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("chqIssueId", chqIssueId));
        strB.append(getTOString("chqIssueDt", chqIssueDt));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOString("unpaidChq", unpaidChq));
        strB.append(getTOString("chqReturned", chqReturned));
        strB.append(getTOString("chqIssue", chqIssue));
        strB.append(getTOString("chqDelivery", chqDelivery));
        strB.append(getTOString("noLeaves", noLeaves));
        strB.append(getTOString("noChqBooks", noChqBooks));
        strB.append(getTOString("startChqNo1", startChqNo1));
        strB.append(getTOString("startChqNo2", startChqNo2));
        strB.append(getTOString("endChqNo1", endChqNo1));
        strB.append(getTOString("endChqNo2", endChqNo2));
        strB.append(getTOString("chqBkSeriesFrom", chqBkSeriesFrom));
        strB.append(getTOString("chqBkSeriesTo", chqBkSeriesTo));
        strB.append(getTOString("chargesCollected", chargesCollected));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("acctNames", acctNames));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("chequeSubType", chequeSubType));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("transOutId", transOutId));
        strB.append(getTOString("stopStatus", stopStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("chqIssueId", chqIssueId));
        strB.append(getTOXml("chqIssueDt", chqIssueDt));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("unpaidChq", unpaidChq));
        strB.append(getTOXml("chqReturned", chqReturned));
        strB.append(getTOXml("chqIssue", chqIssue));
        strB.append(getTOXml("chqDelivery", chqDelivery));
        strB.append(getTOXml("noLeaves", noLeaves));
        strB.append(getTOXml("noChqBooks", noChqBooks));
        strB.append(getTOXml("startChqNo1", startChqNo1));
        strB.append(getTOXml("startChqNo2", startChqNo2));
        strB.append(getTOXml("endChqNo1", endChqNo1));
        strB.append(getTOXml("endChqNo2", endChqNo2));
        strB.append(getTOXml("chqBkSeriesFrom", chqBkSeriesFrom));
        strB.append(getTOXml("chqBkSeriesTo", chqBkSeriesTo));
        strB.append(getTOXml("chargesCollected", chargesCollected));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("acctNames", acctNames));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("chequeSubType", chequeSubType));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("transOutId", transOutId));
        strB.append(getTOXml("stopStatus", stopStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property stopStatus.
     *
     * @return Value of property stopStatus.
     */
    public java.lang.String getStopStatus() {
        return stopStatus;
    }

    /**
     * Setter for property stopStatus.
     *
     * @param stopStatus New value of property stopStatus.
     */
    public void setStopStatus(java.lang.String stopStatus) {
        this.stopStatus = stopStatus;
    }
}