/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLTO.java
 * 
 * Created on Tue Apr 19 15:42:03 IST 2005
 */
package com.see.truetransact.transferobject.transaction.common.product.gl;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is GL.
 */
public class GLTO extends TransferObject implements Serializable {

    private String acHdId = "";
    private Double opnBal = null;
    private Double curBal = null;
    private String branchCode = "";
    private Double clearBalance = null;
    private Double unclearBalance = null;
    private Double availableBalance = null;
    private Double shadowCredit = null;
    private Double shadowDebit = null;
    private Date lastTransDt = null;
    private Double totalBalance = null;
    private String implStatus = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String balanceType = "";
    private String initiatedBranch = "";
    private String ibrHierarchy = "";
    //interbranch code

    public String getIbrHierarchy() {
        return ibrHierarchy;
    }

    public void setIbrHierarchy(String ibrHierarchy) {
        this.ibrHierarchy = ibrHierarchy;
    }

    public String getInitiatedBranch() {
        return initiatedBranch;
    }

    public void setInitiatedBranch(String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }
    //end

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
     * Setter/Getter for OPN_BAL - table Field
     */
    public void setOpnBal(Double opnBal) {
        this.opnBal = opnBal;
    }

    public Double getOpnBal() {
        return opnBal;
    }

    /**
     * Setter/Getter for CUR_BAL - table Field
     */
    public void setCurBal(Double curBal) {
        this.curBal = curBal;
    }

    public Double getCurBal() {
        return curBal;
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
     * Setter/Getter for AVAILABLE_BALANCE - table Field
     */
    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Double getAvailableBalance() {
        return availableBalance;
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
     * Setter/Getter for LAST_TRANS_DT - table Field
     */
    public void setLastTransDt(Date lastTransDt) {
        this.lastTransDt = lastTransDt;
    }

    public Date getLastTransDt() {
        return lastTransDt;
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
     * Setter/Getter for IMPL_STATUS - table Field
     */
    public void setImplStatus(String implStatus) {
        this.implStatus = implStatus;
    }

    public String getImplStatus() {
        return implStatus;
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
     * Setter/Getter for BALANCE_TYPE - table Field
     */
    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getBalanceType() {
        return balanceType;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acHdId" + KEY_VAL_SEPARATOR + "branchCode");
        return acHdId + KEY_VAL_SEPARATOR + branchCode;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("opnBal", opnBal));
        strB.append(getTOString("curBal", curBal));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("clearBalance", clearBalance));
        strB.append(getTOString("unclearBalance", unclearBalance));
        strB.append(getTOString("availableBalance", availableBalance));
        strB.append(getTOString("shadowCredit", shadowCredit));
        strB.append(getTOString("shadowDebit", shadowDebit));
        strB.append(getTOString("lastTransDt", lastTransDt));
        strB.append(getTOString("totalBalance", totalBalance));
        strB.append(getTOString("implStatus", implStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("balanceType", balanceType));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("ibrHierarchy", ibrHierarchy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("opnBal", opnBal));
        strB.append(getTOXml("curBal", curBal));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("clearBalance", clearBalance));
        strB.append(getTOXml("unclearBalance", unclearBalance));
        strB.append(getTOXml("availableBalance", availableBalance));
        strB.append(getTOXml("shadowCredit", shadowCredit));
        strB.append(getTOXml("shadowDebit", shadowDebit));
        strB.append(getTOXml("lastTransDt", lastTransDt));
        strB.append(getTOXml("totalBalance", totalBalance));
        strB.append(getTOXml("implStatus", implStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("balanceType", balanceType));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
		strB.append(getTOXml("ibrHierarchy", ibrHierarchy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}