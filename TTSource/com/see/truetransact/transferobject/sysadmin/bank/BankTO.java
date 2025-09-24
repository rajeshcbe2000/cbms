/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BankTO.java
 *
 * Created on Sat Apr 09 17:55:37 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.bank;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author shanmuga
 *
 */
/**
 * Table name for this TO is BANK.
 */
public class BankTO extends TransferObject implements Serializable {

    private String bankCode = "";
    private String bankName = "";
    private String webSiteAddr = "";
    private String webSiteIp = "";
    private String supportEmail = "";
    private String dataCenterIp = "";
    private String dayEndProcessTime = "";
    private String exchRateConv = "";
    private String transPostingMethod = "";
    private Double cashLimit = null;
    private String tellerTransAllowed = "";
    private String branchTransAllowed = "";
    private Date createdDt = null;
    private String createdBy = "";
    private Date lastModifiedDt = null;
    private String lastModifiedBy = "";
    private String baseCurrency = "";
    private Date openingDt = null;
    private String authorizeStatus1 = "";
    private String authorizeStatus2 = "";
    private String authorizeBy1 = "";
    private String authorizeBy2 = "";
    private Date authorizeDt1 = null;
    private Date authorizeDt2 = null;

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
     * Setter/Getter for BANK_NAME - table Field
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    /**
     * Setter/Getter for WEB_SITE_ADDR - table Field
     */
    public void setWebSiteAddr(String webSiteAddr) {
        this.webSiteAddr = webSiteAddr;
    }

    public String getWebSiteAddr() {
        return webSiteAddr;
    }

    /**
     * Setter/Getter for WEB_SITE_IP - table Field
     */
    public void setWebSiteIp(String webSiteIp) {
        this.webSiteIp = webSiteIp;
    }

    public String getWebSiteIp() {
        return webSiteIp;
    }

    /**
     * Setter/Getter for SUPPORT_EMAIL - table Field
     */
    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    /**
     * Setter/Getter for DATA_CENTER_IP - table Field
     */
    public void setDataCenterIp(String dataCenterIp) {
        this.dataCenterIp = dataCenterIp;
    }

    public String getDataCenterIp() {
        return dataCenterIp;
    }

    /**
     * Setter/Getter for DAY_END_PROCESS_TIME - table Field
     */
    public void setDayEndProcessTime(String dayEndProcessTime) {
        this.dayEndProcessTime = dayEndProcessTime;
    }

    public String getDayEndProcessTime() {
        return dayEndProcessTime;
    }

    /**
     * Setter/Getter for EXCH_RATE_CONV - table Field
     */
    public void setExchRateConv(String exchRateConv) {
        this.exchRateConv = exchRateConv;
    }

    public String getExchRateConv() {
        return exchRateConv;
    }

    /**
     * Setter/Getter for TRANS_POSTING_METHOD - table Field
     */
    public void setTransPostingMethod(String transPostingMethod) {
        this.transPostingMethod = transPostingMethod;
    }

    public String getTransPostingMethod() {
        return transPostingMethod;
    }

    /**
     * Setter/Getter for CASH_LIMIT - table Field
     */
    public void setCashLimit(Double cashLimit) {
        this.cashLimit = cashLimit;
    }

    public Double getCashLimit() {
        return cashLimit;
    }

    /**
     * Setter/Getter for TELLER_TRANS_ALLOWED - table Field
     */
    public void setTellerTransAllowed(String tellerTransAllowed) {
        this.tellerTransAllowed = tellerTransAllowed;
    }

    public String getTellerTransAllowed() {
        return tellerTransAllowed;
    }

    /**
     * Setter/Getter for BRANCH_TRANS_ALLOWED - table Field
     */
    public void setBranchTransAllowed(String branchTransAllowed) {
        this.branchTransAllowed = branchTransAllowed;
    }

    public String getBranchTransAllowed() {
        return branchTransAllowed;
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
     * Setter/Getter for LAST_MODIFIED_DT - table Field
     */
    public void setLastModifiedDt(Date lastModifiedDt) {
        this.lastModifiedDt = lastModifiedDt;
    }

    public Date getLastModifiedDt() {
        return lastModifiedDt;
    }

    /**
     * Setter/Getter for LAST_MODIFIED_BY - table Field
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Setter/Getter for BASE_CURRENCY - table Field
     */
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    /**
     * Setter/Getter for OPENING_DT - table Field
     */
    public void setOpeningDt(Date openingDt) {
        this.openingDt = openingDt;
    }

    public Date getOpeningDt() {
        return openingDt;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS_1 - table Field
     */
    public void setAuthorizeStatus1(String authorizeStatus1) {
        this.authorizeStatus1 = authorizeStatus1;
    }

    public String getAuthorizeStatus1() {
        return authorizeStatus1;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS_2 - table Field
     */
    public void setAuthorizeStatus2(String authorizeStatus2) {
        this.authorizeStatus2 = authorizeStatus2;
    }

    public String getAuthorizeStatus2() {
        return authorizeStatus2;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY_1 - table Field
     */
    public void setAuthorizeBy1(String authorizeBy1) {
        this.authorizeBy1 = authorizeBy1;
    }

    public String getAuthorizeBy1() {
        return authorizeBy1;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY_2 - table Field
     */
    public void setAuthorizeBy2(String authorizeBy2) {
        this.authorizeBy2 = authorizeBy2;
    }

    public String getAuthorizeBy2() {
        return authorizeBy2;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT_1 - table Field
     */
    public void setAuthorizeDt1(Date authorizeDt1) {
        this.authorizeDt1 = authorizeDt1;
    }

    public Date getAuthorizeDt1() {
        return authorizeDt1;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT_2 - table Field
     */
    public void setAuthorizeDt2(Date authorizeDt2) {
        this.authorizeDt2 = authorizeDt2;
    }

    public Date getAuthorizeDt2() {
        return authorizeDt2;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("bankCode");
        return bankCode;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("bankCode", bankCode));
        strB.append(getTOString("bankName", bankName));
        strB.append(getTOString("webSiteAddr", webSiteAddr));
        strB.append(getTOString("webSiteIp", webSiteIp));
        strB.append(getTOString("supportEmail", supportEmail));
        strB.append(getTOString("dataCenterIp", dataCenterIp));
        strB.append(getTOString("dayEndProcessTime", dayEndProcessTime));
        strB.append(getTOString("exchRateConv", exchRateConv));
        strB.append(getTOString("transPostingMethod", transPostingMethod));
        strB.append(getTOString("cashLimit", cashLimit));
        strB.append(getTOString("tellerTransAllowed", tellerTransAllowed));
        strB.append(getTOString("branchTransAllowed", branchTransAllowed));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("lastModifiedDt", lastModifiedDt));
        strB.append(getTOString("lastModifiedBy", lastModifiedBy));
        strB.append(getTOString("baseCurrency", baseCurrency));
        strB.append(getTOString("openingDt", openingDt));
        strB.append(getTOString("authorizeStatus1", authorizeStatus1));
        strB.append(getTOString("authorizeStatus2", authorizeStatus2));
        strB.append(getTOString("authorizeBy1", authorizeBy1));
        strB.append(getTOString("authorizeBy2", authorizeBy2));
        strB.append(getTOString("authorizeDt1", authorizeDt1));
        strB.append(getTOString("authorizeDt2", authorizeDt2));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("bankCode", bankCode));
        strB.append(getTOXml("bankName", bankName));
        strB.append(getTOXml("webSiteAddr", webSiteAddr));
        strB.append(getTOXml("webSiteIp", webSiteIp));
        strB.append(getTOXml("supportEmail", supportEmail));
        strB.append(getTOXml("dataCenterIp", dataCenterIp));
        strB.append(getTOXml("dayEndProcessTime", dayEndProcessTime));
        strB.append(getTOXml("exchRateConv", exchRateConv));
        strB.append(getTOXml("transPostingMethod", transPostingMethod));
        strB.append(getTOXml("cashLimit", cashLimit));
        strB.append(getTOXml("tellerTransAllowed", tellerTransAllowed));
        strB.append(getTOXml("branchTransAllowed", branchTransAllowed));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("lastModifiedDt", lastModifiedDt));
        strB.append(getTOXml("lastModifiedBy", lastModifiedBy));
        strB.append(getTOXml("baseCurrency", baseCurrency));
        strB.append(getTOXml("openingDt", openingDt));
        strB.append(getTOXml("authorizeStatus1", authorizeStatus1));
        strB.append(getTOXml("authorizeStatus2", authorizeStatus2));
        strB.append(getTOXml("authorizeBy1", authorizeBy1));
        strB.append(getTOXml("authorizeBy2", authorizeBy2));
        strB.append(getTOXml("authorizeDt1", authorizeDt1));
        strB.append(getTOXml("authorizeDt2", authorizeDt2));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}