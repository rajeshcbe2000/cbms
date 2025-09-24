/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenLossTO.java
 * 
 * Created on Thu Jan 27 10:30:27 IST 2005
 */
package com.see.truetransact.transferobject.transaction.token.tokenloss;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_LOST.
 */
public class TokenLossTO extends TransferObject implements Serializable {

    private String tokenType = "";
    private Date lostDt = null;
    private String seriesNo = "";
    private String tokenNo = "";
    private String remarks = "";
    private String tokenStatus = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private Date recoverDt = null;
    private String tokenLostId = "";
    private String branchId = "";

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
     * Setter/Getter for LOST_DT - table Field
     */
    public void setLostDt(Date lostDt) {
        this.lostDt = lostDt;
    }

    public Date getLostDt() {
        return lostDt;
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
     * Setter/Getter for TOKEN_NO - table Field
     */
    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getTokenNo() {
        return tokenNo;
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
     * Setter/Getter for RECOVER_DT - table Field
     */
    public void setRecoverDt(Date recoverDt) {
        this.recoverDt = recoverDt;
    }

    public Date getRecoverDt() {
        return recoverDt;
    }

    /**
     * Setter/Getter for TOKEN_LOST_ID - table Field
     */
    public void setTokenLostId(String tokenLostId) {
        this.tokenLostId = tokenLostId;
    }

    public String getTokenLostId() {
        return tokenLostId;
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
        setKeyColumns(tokenLostId);
        return tokenLostId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("tokenType", tokenType));
        strB.append(getTOString("lostDt", lostDt));
        strB.append(getTOString("seriesNo", seriesNo));
        strB.append(getTOString("tokenNo", tokenNo));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("tokenStatus", tokenStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("recoverDt", recoverDt));
        strB.append(getTOString("tokenLostId", tokenLostId));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("tokenType", tokenType));
        strB.append(getTOXml("lostDt", lostDt));
        strB.append(getTOXml("seriesNo", seriesNo));
        strB.append(getTOXml("tokenNo", tokenNo));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("tokenStatus", tokenStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("recoverDt", recoverDt));
        strB.append(getTOXml("tokenLostId", tokenLostId));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}