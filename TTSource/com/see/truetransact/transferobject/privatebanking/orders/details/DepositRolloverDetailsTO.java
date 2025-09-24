/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositRolloverDetailsTO.java
 * 
 * Created on Wed Jul 07 17:44:20 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.privatebanking.orders.details;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_ORDER_DETAILS.
 */
public class DepositRolloverDetailsTO extends TransferObject implements Serializable {

    private String ordId = "";
    private String portfolioLoc = "";
    private String assetSubClass = "";
    private String account = "";
    private Double principal = null;
    private String rollover = "";
    private Double intEarned = null;
    private Double cspMemoBalance = null;
    private Date startDt = null;
    private Date maturityDate = null;
    private Double rolloverAmount = null;
    private String phoneOrder = "";
    private String tenorPeriodType = "";
    private Double tenorDays = null;
    private String rolloverType = "";
    private String currency = "";
    private Double spread = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;

    /**
     * Setter/Getter for ORD_ID - table Field
     */
    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public String getOrdId() {
        return ordId;
    }

    /**
     * Setter/Getter for PORTFOLIO_LOC - table Field
     */
    public void setPortfolioLoc(String portfolioLoc) {
        this.portfolioLoc = portfolioLoc;
    }

    public String getPortfolioLoc() {
        return portfolioLoc;
    }

    /**
     * Setter/Getter for ASSET_SUB_CLASS - table Field
     */
    public void setAssetSubClass(String assetSubClass) {
        this.assetSubClass = assetSubClass;
    }

    public String getAssetSubClass() {
        return assetSubClass;
    }

    /**
     * Setter/Getter for ACCOUNT - table Field
     */
    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    /**
     * Setter/Getter for PRINCIPAL - table Field
     */
    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    public Double getPrincipal() {
        return principal;
    }

    /**
     * Setter/Getter for ROLLOVER - table Field
     */
    public void setRollover(String rollover) {
        this.rollover = rollover;
    }

    public String getRollover() {
        return rollover;
    }

    /**
     * Setter/Getter for INT_EARNED - table Field
     */
    public void setIntEarned(Double intEarned) {
        this.intEarned = intEarned;
    }

    public Double getIntEarned() {
        return intEarned;
    }

    /**
     * Setter/Getter for CSP_MEMO_BALANCE - table Field
     */
    public void setCspMemoBalance(Double cspMemoBalance) {
        this.cspMemoBalance = cspMemoBalance;
    }

    public Double getCspMemoBalance() {
        return cspMemoBalance;
    }

    /**
     * Setter/Getter for START_DT - table Field
     */
    public void setStartDt(Date startDt) {
        this.startDt = startDt;
    }

    public Date getStartDt() {
        return startDt;
    }

    /**
     * Setter/Getter for MATURITY_DATE - table Field
     */
    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    /**
     * Setter/Getter for ROLLOVER_AMOUNT - table Field
     */
    public void setRolloverAmount(Double rolloverAmount) {
        this.rolloverAmount = rolloverAmount;
    }

    public Double getRolloverAmount() {
        return rolloverAmount;
    }

    /**
     * Setter/Getter for PHONE_ORDER - table Field
     */
    public void setPhoneOrder(String phoneOrder) {
        this.phoneOrder = phoneOrder;
    }

    public String getPhoneOrder() {
        return phoneOrder;
    }

    /**
     * Setter/Getter for TENOR_PERIOD_TYPE - table Field
     */
    public void setTenorPeriodType(String tenorPeriodType) {
        this.tenorPeriodType = tenorPeriodType;
    }

    public String getTenorPeriodType() {
        return tenorPeriodType;
    }

    /**
     * Setter/Getter for TENOR_DAYS - table Field
     */
    public void setTenorDays(Double tenorDays) {
        this.tenorDays = tenorDays;
    }

    public Double getTenorDays() {
        return tenorDays;
    }

    /**
     * Setter/Getter for ROLLOVER_TYPE - table Field
     */
    public void setRolloverType(String rolloverType) {
        this.rolloverType = rolloverType;
    }

    public String getRolloverType() {
        return rolloverType;
    }

    /**
     * Setter/Getter for CURRENCY - table Field
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    /**
     * Setter/Getter for SPREAD - table Field
     */
    public void setSpread(Double spread) {
        this.spread = spread;
    }

    public Double getSpread() {
        return spread;
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
        setKeyColumns("ordId");
        return ordId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("ordId", ordId));
        strB.append(getTOString("portfolioLoc", portfolioLoc));
        strB.append(getTOString("assetSubClass", assetSubClass));
        strB.append(getTOString("account", account));
        strB.append(getTOString("principal", principal));
        strB.append(getTOString("rollover", rollover));
        strB.append(getTOString("intEarned", intEarned));
        strB.append(getTOString("cspMemoBalance", cspMemoBalance));
        strB.append(getTOString("startDt", startDt));
        strB.append(getTOString("maturityDate", maturityDate));
        strB.append(getTOString("rolloverAmount", rolloverAmount));
        strB.append(getTOString("phoneOrder", phoneOrder));
        strB.append(getTOString("tenorPeriodType", tenorPeriodType));
        strB.append(getTOString("tenorDays", tenorDays));
        strB.append(getTOString("rolloverType", rolloverType));
        strB.append(getTOString("currency", currency));
        strB.append(getTOString("spread", spread));
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
        strB.append(getTOXml("ordId", ordId));
        strB.append(getTOXml("portfolioLoc", portfolioLoc));
        strB.append(getTOXml("assetSubClass", assetSubClass));
        strB.append(getTOXml("account", account));
        strB.append(getTOXml("principal", principal));
        strB.append(getTOXml("rollover", rollover));
        strB.append(getTOXml("intEarned", intEarned));
        strB.append(getTOXml("cspMemoBalance", cspMemoBalance));
        strB.append(getTOXml("startDt", startDt));
        strB.append(getTOXml("maturityDate", maturityDate));
        strB.append(getTOXml("rolloverAmount", rolloverAmount));
        strB.append(getTOXml("phoneOrder", phoneOrder));
        strB.append(getTOXml("tenorPeriodType", tenorPeriodType));
        strB.append(getTOXml("tenorDays", tenorDays));
        strB.append(getTOXml("rolloverType", rolloverType));
        strB.append(getTOXml("currency", currency));
        strB.append(getTOXml("spread", spread));
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