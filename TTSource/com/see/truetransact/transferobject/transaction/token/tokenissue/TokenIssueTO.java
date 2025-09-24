/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenIssueTO.java
 * 
 * Created on Fri Jan 21 17:01:30 IST 2005
 */
package com.see.truetransact.transferobject.transaction.token.tokenissue;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_ISSUE.
 */
public class TokenIssueTO extends TransferObject implements Serializable {

    private String issueId = "";
    private String tokenType = "";
    private Date issueDt = null;
    private Double noOfTokens = null;
    private String receivedBy = "";
    private String seriesNo = "";
    private Double tokenStartNo = null;
    private Double tokenEndNo = null;
    private String branchId = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String tokenStatus = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;

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
     * Setter/Getter for TOKEN_TYPE - table Field
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenType() {
        return tokenType;
    }

    /**
     * Setter/Getter for ISSUE_DT - table Field
     */
    public void setIssueDt(Date issueDt) {
        this.issueDt = issueDt;
    }

    public Date getIssueDt() {
        return issueDt;
    }

    /**
     * Setter/Getter for NO_OF_TOKENS - table Field
     */
    public void setNoOfTokens(Double noOfTokens) {
        this.noOfTokens = noOfTokens;
    }

    public Double getNoOfTokens() {
        return noOfTokens;
    }

    /**
     * Setter/Getter for RECEIVED_BY - table Field
     */
    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    /**
     * Setter/Getter for SERIES_NO - table Field
     */
    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    /**
     * Setter/Getter for TOKEN_START_NO - table Field
     */
    public void setTokenStartNo(Double tokenStartNo) {
        this.tokenStartNo = tokenStartNo;
    }

    public Double getTokenStartNo() {
        return tokenStartNo;
    }

    /**
     * Setter/Getter for TOKEN_END_NO - table Field
     */
    public void setTokenEndNo(Double tokenEndNo) {
        this.tokenEndNo = tokenEndNo;
    }

    public Double getTokenEndNo() {
        return tokenEndNo;
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
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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
     * Setter/Getter for TOKEN_STATUS - table Field
     */
    public void setTokenStatus(String tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public String getTokenStatus() {
        return tokenStatus;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(issueId);
        return issueId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("issueId", issueId));
        strB.append(getTOString("tokenType", tokenType));
        strB.append(getTOString("issueDt", issueDt));
        strB.append(getTOString("noOfTokens", noOfTokens));
        strB.append(getTOString("receivedBy", receivedBy));
        strB.append(getTOString("seriesNo", seriesNo));
        strB.append(getTOString("tokenStartNo", tokenStartNo));
        strB.append(getTOString("tokenEndNo", tokenEndNo));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("tokenStatus", tokenStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("issueId", issueId));
        strB.append(getTOXml("tokenType", tokenType));
        strB.append(getTOXml("issueDt", issueDt));
        strB.append(getTOXml("noOfTokens", noOfTokens));
        strB.append(getTOXml("receivedBy", receivedBy));
        strB.append(getTOXml("seriesNo", seriesNo));
        strB.append(getTOXml("tokenStartNo", tokenStartNo));
        strB.append(getTOXml("tokenEndNo", tokenEndNo));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("tokenStatus", tokenStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}