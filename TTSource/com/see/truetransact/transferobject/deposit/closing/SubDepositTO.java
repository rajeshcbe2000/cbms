/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SubDepositTO.java
 * 
 * Created on Wed Jun 16 15:58:44 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit.closing;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_SUB_ACINFO.
 */
public class SubDepositTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private Integer depositSubNo = 0;
    private Date depositDt = null;
    private Double depositPeriodYy = null;
    private Double depositPeriodMm = null;
    private Double depositPeriodDd = null;
    private Double depositAmt = null;
    private String intpayMode = "";
    private Double intpayFreq = null;
    private Date maturityDt = null;
    private Double rateOfInt = null;
    private Double maturityAmt = null;
    private Double totIntAmt = null;
    private Double periodicIntAmt = null;
    private String status = "";
    private Double clearBalance = null;
    private Double unclearBalance = null;
    private Double availableBalance = null;
    private Date closeDt = null;
    private String closeBy = "";
    private String createBy = "";
    private Date authorizeDt = null;
    private String authorizeBy = "";
    private String authorizeStatus = null;
    private Double shadowLien = null;
    private Double shadowFreeze = null;
    private String substatusBy = "";
    private Date substatusDt = null;
    private String acctStatus = "";
    private Date lastIntApplDt = null;
    private Double totalIntCredit = null;
    private Double totalIntDrawn = null;
    private Double totalIntDebit = null;
    private Double totalInstallments = null;
    private Double totalInstallPaid = null;
    private Double totalBalance = null;

    /**
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
    }

    public Integer getDepositSubNo() {
        return depositSubNo;
    }

    public void setDepositSubNo(Integer depositSubNo) {
        this.depositSubNo = depositSubNo;
    }

    

    /**
     * Setter/Getter for DEPOSIT_DT - table Field
     */
    public void setDepositDt(Date depositDt) {
        this.depositDt = depositDt;
    }

    public Date getDepositDt() {
        return depositDt;
    }

    /**
     * Setter/Getter for DEPOSIT_PERIOD_YY - table Field
     */
    public void setDepositPeriodYy(Double depositPeriodYy) {
        this.depositPeriodYy = depositPeriodYy;
    }

    public Double getDepositPeriodYy() {
        return depositPeriodYy;
    }

    /**
     * Setter/Getter for DEPOSIT_PERIOD_MM - table Field
     */
    public void setDepositPeriodMm(Double depositPeriodMm) {
        this.depositPeriodMm = depositPeriodMm;
    }

    public Double getDepositPeriodMm() {
        return depositPeriodMm;
    }

    /**
     * Setter/Getter for DEPOSIT_PERIOD_DD - table Field
     */
    public void setDepositPeriodDd(Double depositPeriodDd) {
        this.depositPeriodDd = depositPeriodDd;
    }

    public Double getDepositPeriodDd() {
        return depositPeriodDd;
    }

    /**
     * Setter/Getter for DEPOSIT_AMT - table Field
     */
    public void setDepositAmt(Double depositAmt) {
        this.depositAmt = depositAmt;
    }

    public Double getDepositAmt() {
        return depositAmt;
    }

    /**
     * Setter/Getter for INTPAY_MODE - table Field
     */
    public void setIntpayMode(String intpayMode) {
        this.intpayMode = intpayMode;
    }

    public String getIntpayMode() {
        return intpayMode;
    }

    /**
     * Setter/Getter for INTPAY_FREQ - table Field
     */
    public void setIntpayFreq(Double intpayFreq) {
        this.intpayFreq = intpayFreq;
    }

    public Double getIntpayFreq() {
        return intpayFreq;
    }

    /**
     * Setter/Getter for MATURITY_DT - table Field
     */
    public void setMaturityDt(Date maturityDt) {
        this.maturityDt = maturityDt;
    }

    public Date getMaturityDt() {
        return maturityDt;
    }

    /**
     * Setter/Getter for RATE_OF_INT - table Field
     */
    public void setRateOfInt(Double rateOfInt) {
        this.rateOfInt = rateOfInt;
    }

    public Double getRateOfInt() {
        return rateOfInt;
    }

    /**
     * Setter/Getter for MATURITY_AMT - table Field
     */
    public void setMaturityAmt(Double maturityAmt) {
        this.maturityAmt = maturityAmt;
    }

    public Double getMaturityAmt() {
        return maturityAmt;
    }

    /**
     * Setter/Getter for TOT_INT_AMT - table Field
     */
    public void setTotIntAmt(Double totIntAmt) {
        this.totIntAmt = totIntAmt;
    }

    public Double getTotIntAmt() {
        return totIntAmt;
    }

    /**
     * Setter/Getter for PERIODIC_INT_AMT - table Field
     */
    public void setPeriodicIntAmt(Double periodicIntAmt) {
        this.periodicIntAmt = periodicIntAmt;
    }

    public Double getPeriodicIntAmt() {
        return periodicIntAmt;
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
     * Setter/Getter for CLOSE_DT - table Field
     */
    public void setCloseDt(Date closeDt) {
        this.closeDt = closeDt;
    }

    public Date getCloseDt() {
        return closeDt;
    }

    /**
     * Setter/Getter for CLOSE_BY - table Field
     */
    public void setCloseBy(String closeBy) {
        this.closeBy = closeBy;
    }

    public String getCloseBy() {
        return closeBy;
    }

    /**
     * Setter/Getter for CREATE_BY - table Field
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateBy() {
        return createBy;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for SHADOW_LIEN - table Field
     */
    public void setShadowLien(Double shadowLien) {
        this.shadowLien = shadowLien;
    }

    public Double getShadowLien() {
        return shadowLien;
    }

    /**
     * Setter/Getter for SHADOW_FREEZE - table Field
     */
    public void setShadowFreeze(Double shadowFreeze) {
        this.shadowFreeze = shadowFreeze;
    }

    public Double getShadowFreeze() {
        return shadowFreeze;
    }

    /**
     * Setter/Getter for SUBSTATUS_BY - table Field
     */
    public void setSubstatusBy(String substatusBy) {
        this.substatusBy = substatusBy;
    }

    public String getSubstatusBy() {
        return substatusBy;
    }

    /**
     * Setter/Getter for SUBSTATUS_DT - table Field
     */
    public void setSubstatusDt(Date substatusDt) {
        this.substatusDt = substatusDt;
    }

    public Date getSubstatusDt() {
        return substatusDt;
    }

    /**
     * Setter/Getter for ACCT_STATUS - table Field
     */
    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
    }

    public String getAcctStatus() {
        return acctStatus;
    }

    /**
     * Setter/Getter for LAST_INT_APPL_DT - table Field
     */
    public void setLastIntApplDt(Date lastIntApplDt) {
        this.lastIntApplDt = lastIntApplDt;
    }

    public Date getLastIntApplDt() {
        return lastIntApplDt;
    }

    /**
     * Setter/Getter for TOTAL_INT_CREDIT - table Field
     */
    public void setTotalIntCredit(Double totalIntCredit) {
        this.totalIntCredit = totalIntCredit;
    }

    public Double getTotalIntCredit() {
        return totalIntCredit;
    }

    /**
     * Setter/Getter for TOTAL_INT_DRAWN - table Field
     */
    public void setTotalIntDrawn(Double totalIntDrawn) {
        this.totalIntDrawn = totalIntDrawn;
    }

    public Double getTotalIntDrawn() {
        return totalIntDrawn;
    }

    /**
     * Setter/Getter for TOTAL_INT_DEBIT - table Field
     */
    public void setTotalIntDebit(Double totalIntDebit) {
        this.totalIntDebit = totalIntDebit;
    }

    public Double getTotalIntDebit() {
        return totalIntDebit;
    }

    /**
     * Setter/Getter for TOTAL_INSTALLMENTS - table Field
     */
    public void setTotalInstallments(Double totalInstallments) {
        this.totalInstallments = totalInstallments;
    }

    public Double getTotalInstallments() {
        return totalInstallments;
    }

    /**
     * Setter/Getter for TOTAL_INSTALL_PAID - table Field
     */
    public void setTotalInstallPaid(Double totalInstallPaid) {
        this.totalInstallPaid = totalInstallPaid;
    }

    public Double getTotalInstallPaid() {
        return totalInstallPaid;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("depositSubNo", depositSubNo));
        strB.append(getTOString("depositDt", depositDt));
        strB.append(getTOString("depositPeriodYy", depositPeriodYy));
        strB.append(getTOString("depositPeriodMm", depositPeriodMm));
        strB.append(getTOString("depositPeriodDd", depositPeriodDd));
        strB.append(getTOString("depositAmt", depositAmt));
        strB.append(getTOString("intpayMode", intpayMode));
        strB.append(getTOString("intpayFreq", intpayFreq));
        strB.append(getTOString("maturityDt", maturityDt));
        strB.append(getTOString("rateOfInt", rateOfInt));
        strB.append(getTOString("maturityAmt", maturityAmt));
        strB.append(getTOString("totIntAmt", totIntAmt));
        strB.append(getTOString("periodicIntAmt", periodicIntAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("clearBalance", clearBalance));
        strB.append(getTOString("unclearBalance", unclearBalance));
        strB.append(getTOString("availableBalance", availableBalance));
        strB.append(getTOString("closeDt", closeDt));
        strB.append(getTOString("closeBy", closeBy));
        strB.append(getTOString("createBy", createBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("shadowLien", shadowLien));
        strB.append(getTOString("shadowFreeze", shadowFreeze));
        strB.append(getTOString("substatusBy", substatusBy));
        strB.append(getTOString("substatusDt", substatusDt));
        strB.append(getTOString("acctStatus", acctStatus));
        strB.append(getTOString("lastIntApplDt", lastIntApplDt));
        strB.append(getTOString("totalIntCredit", totalIntCredit));
        strB.append(getTOString("totalIntDrawn", totalIntDrawn));
        strB.append(getTOString("totalIntDebit", totalIntDebit));
        strB.append(getTOString("totalInstallments", totalInstallments));
        strB.append(getTOString("totalInstallPaid", totalInstallPaid));
        strB.append(getTOString("totalBalance", totalBalance));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("depositSubNo", depositSubNo));
        strB.append(getTOXml("depositDt", depositDt));
        strB.append(getTOXml("depositPeriodYy", depositPeriodYy));
        strB.append(getTOXml("depositPeriodMm", depositPeriodMm));
        strB.append(getTOXml("depositPeriodDd", depositPeriodDd));
        strB.append(getTOXml("depositAmt", depositAmt));
        strB.append(getTOXml("intpayMode", intpayMode));
        strB.append(getTOXml("intpayFreq", intpayFreq));
        strB.append(getTOXml("maturityDt", maturityDt));
        strB.append(getTOXml("rateOfInt", rateOfInt));
        strB.append(getTOXml("maturityAmt", maturityAmt));
        strB.append(getTOXml("totIntAmt", totIntAmt));
        strB.append(getTOXml("periodicIntAmt", periodicIntAmt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("clearBalance", clearBalance));
        strB.append(getTOXml("unclearBalance", unclearBalance));
        strB.append(getTOXml("availableBalance", availableBalance));
        strB.append(getTOXml("closeDt", closeDt));
        strB.append(getTOXml("closeBy", closeBy));
        strB.append(getTOXml("createBy", createBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("shadowLien", shadowLien));
        strB.append(getTOXml("shadowFreeze", shadowFreeze));
        strB.append(getTOXml("substatusBy", substatusBy));
        strB.append(getTOXml("substatusDt", substatusDt));
        strB.append(getTOXml("acctStatus", acctStatus));
        strB.append(getTOXml("lastIntApplDt", lastIntApplDt));
        strB.append(getTOXml("totalIntCredit", totalIntCredit));
        strB.append(getTOXml("totalIntDrawn", totalIntDrawn));
        strB.append(getTOXml("totalIntDebit", totalIntDebit));
        strB.append(getTOXml("totalInstallments", totalInstallments));
        strB.append(getTOXml("totalInstallPaid", totalInstallPaid));
        strB.append(getTOXml("totalBalance", totalBalance));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property totalBalance.
     *
     * @return Value of property totalBalance.
     */
    public java.lang.Double getTotalBalance() {
        return totalBalance;
    }

    /**
     * Setter for property totalBalance.
     *
     * @param totalBalance New value of property totalBalance.
     */
    public void setTotalBalance(java.lang.Double totalBalance) {
        this.totalBalance = totalBalance;
    }
}