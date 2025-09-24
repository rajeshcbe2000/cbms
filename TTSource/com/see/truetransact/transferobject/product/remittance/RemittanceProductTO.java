/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceProductTO.java
 *
 * Created on Mon May 17 15:47:24 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.product.remittance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is REMITTANCE_PRODUCT.
 */
public class RemittanceProductTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String prodDesc = "";
    private String issueHd = "";
    private String exchangeHd = "";
    private String telegramChrgHd = "";
    private String revalChrgHd = "";
    private String otherChrgHd = "";
    private String lapsedHd = "";
    private String eftProduct = "";
    private Double cashLimit = null;
    private Double maximumAmount = null;
    private Double minimumAmount = null;
    private String printServices = "";
    private String payHd = "";
    private String postageHd = "";
    private String duplChrgHd = "";
    private String cancellChrgHd = "";
    private String lapseAppl = "";
    private Double lapsePeriod = null;
    private String payIssueBranch = "";
    private String numberPattern = "";
    private String seriesMaintained = "";
    private String remarks = "";
    private String status = "";
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;
    private String baseCurrency = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeRemark = "";
    private String numberPatternSuffix = "";
    private String behavesLike = "";
    private String newProcedure = "";
    private Date newProcStartDt = null;
    private String newProcIssueAcHd = "";
    private String rtgsSuspenseAchd="";
    private String rtgsNeftGLType = "";
    private String rtgsNeftProductType = "";
    private String rtgsNeftProdId = "";
    private String rtgsNeftActNum = "";

    public String getRtgsNeftGLType() {
        return rtgsNeftGLType;
    }

    public void setRtgsNeftGLType(String rtgsNeftGLType) {
        this.rtgsNeftGLType = rtgsNeftGLType;
    }

    public String getRtgsNeftProductType() {
        return rtgsNeftProductType;
    }

    public void setRtgsNeftProductType(String rtgsNeftProductType) {
        this.rtgsNeftProductType = rtgsNeftProductType;
    }

    public String getRtgsNeftProdId() {
        return rtgsNeftProdId;
    }

    public void setRtgsNeftProdId(String rtgsNeftProdId) {
        this.rtgsNeftProdId = rtgsNeftProdId;
    }

    public String getRtgsNeftActNum() {
        return rtgsNeftActNum;
    }

    public void setRtgsNeftActNum(String rtgsNeftActNum) {
        this.rtgsNeftActNum = rtgsNeftActNum;
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
     * Setter/Getter for PROD_DESC - table Field
     */
    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    /**
     * Setter/Getter for ISSUE_HD - table Field
     */
    public void setIssueHd(String issueHd) {
        this.issueHd = issueHd;
    }

    public String getIssueHd() {
        return issueHd;
    }

    /**
     * Setter/Getter for EXCHANGE_HD - table Field
     */
    public void setExchangeHd(String exchangeHd) {
        this.exchangeHd = exchangeHd;
    }

    public String getExchangeHd() {
        return exchangeHd;
    }

    /**
     * Setter/Getter for TELEGRAM_CHRG_HD - table Field
     */
    public void setTelegramChrgHd(String telegramChrgHd) {
        this.telegramChrgHd = telegramChrgHd;
    }

    public String getTelegramChrgHd() {
        return telegramChrgHd;
    }

    /**
     * Setter/Getter for REVAL_CHRG_HD - table Field
     */
    public void setRevalChrgHd(String revalChrgHd) {
        this.revalChrgHd = revalChrgHd;
    }

    public String getRevalChrgHd() {
        return revalChrgHd;
    }

    /**
     * Setter/Getter for OTHER_CHRG_HD - table Field
     */
    public void setOtherChrgHd(String otherChrgHd) {
        this.otherChrgHd = otherChrgHd;
    }

    public String getOtherChrgHd() {
        return otherChrgHd;
    }

    /**
     * Setter/Getter for LAPSED_HD - table Field
     */
    public void setLapsedHd(String lapsedHd) {
        this.lapsedHd = lapsedHd;
    }

    public String getLapsedHd() {
        return lapsedHd;
    }

    /**
     * Setter/Getter for EFT_PRODUCT - table Field
     */
    public void setEftProduct(String eftProduct) {
        this.eftProduct = eftProduct;
    }

    public String getEftProduct() {
        return eftProduct;
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
     * Setter/Getter for PRINT_SERVICES - table Field
     */
    public void setPrintServices(String printServices) {
        this.printServices = printServices;
    }

    public String getPrintServices() {
        return printServices;
    }

    /**
     * Setter/Getter for PAY_HD - table Field
     */
    public void setPayHd(String payHd) {
        this.payHd = payHd;
    }

    public String getPayHd() {
        return payHd;
    }

    /**
     * Setter/Getter for POSTAGE_HD - table Field
     */
    public void setPostageHd(String postageHd) {
        this.postageHd = postageHd;
    }

    public String getPostageHd() {
        return postageHd;
    }

    /**
     * Setter/Getter for DUPL_CHRG_HD - table Field
     */
    public void setDuplChrgHd(String duplChrgHd) {
        this.duplChrgHd = duplChrgHd;
    }

    public String getDuplChrgHd() {
        return duplChrgHd;
    }

    /**
     * Setter/Getter for CANCELL_CHRG_HD - table Field
     */
    public void setCancellChrgHd(String cancellChrgHd) {
        this.cancellChrgHd = cancellChrgHd;
    }

    public String getCancellChrgHd() {
        return cancellChrgHd;
    }

    /**
     * Setter/Getter for LAPSE_APPL - table Field
     */
    public void setLapseAppl(String lapseAppl) {
        this.lapseAppl = lapseAppl;
    }

    public String getLapseAppl() {
        return lapseAppl;
    }

    /**
     * Setter/Getter for LAPSE_PERIOD - table Field
     */
    public void setLapsePeriod(Double lapsePeriod) {
        this.lapsePeriod = lapsePeriod;
    }

    public Double getLapsePeriod() {
        return lapsePeriod;
    }

    /**
     * Setter/Getter for PAY_ISSUE_BRANCH - table Field
     */
    public void setPayIssueBranch(String payIssueBranch) {
        this.payIssueBranch = payIssueBranch;
    }

    public String getPayIssueBranch() {
        return payIssueBranch;
    }

    /**
     * Setter/Getter for NUMBER_PATTERN - table Field
     */
    public void setNumberPattern(String numberPattern) {
        this.numberPattern = numberPattern;
    }

    public String getNumberPattern() {
        return numberPattern;
    }

    /**
     * Setter/Getter for SERIES_MAINTAINED - table Field
     */
    public void setSeriesMaintained(String seriesMaintained) {
        this.seriesMaintained = seriesMaintained;
    }

    public String getSeriesMaintained() {
        return seriesMaintained;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZE_USER - table Field
     */
    public void setAuthorizeUser(String authorizeUser) {
        this.authorizeUser = authorizeUser;
    }

    public String getAuthorizeUser() {
        return authorizeUser;
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
     * Setter/Getter for BASE_CURRENCY - table Field
     */
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
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
     * Setter/Getter for AUTHORIZE_REMARK - table Field
     */
    public void setAuthorizeRemark(String authorizeRemark) {
        this.authorizeRemark = authorizeRemark;
    }

    public String getAuthorizeRemark() {
        return authorizeRemark;
    }

    /**
     * Setter/Getter for NUMBER_PATTERN_SUFFIX - table Field
     */
    public void setNumberPatternSuffix(String numberPatternSuffix) {
        this.numberPatternSuffix = numberPatternSuffix;
    }

    public String getNumberPatternSuffix() {
        return numberPatternSuffix;
    }

    /**
     * Setter/Getter for BEHAVES_LIKE - table Field
     */
    public void setBehavesLike(String behavesLike) {
        this.behavesLike = behavesLike;
    }

    public String getBehavesLike() {
        return behavesLike;
    }

    public String getNewProcedure() {
        return newProcedure;
    }

    public void setNewProcedure(String newProcedure) {
        this.newProcedure = newProcedure;
    }

    public Date getNewProcStartDt() {
        return newProcStartDt;
    }

    public void setNewProcStartDt(Date newProcStartDt) {
        this.newProcStartDt = newProcStartDt;
    }
    
    public String getNewProcIssueAcHd() {
        return newProcIssueAcHd;
    }

    public void setNewProcIssueAcHd(String newProcIssueAcHd) {
        this.newProcIssueAcHd = newProcIssueAcHd;
    }

    public Double getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(Double maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public Double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(Double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }
    
    
    public String getRtgsSuspenseAchd() {
        return rtgsSuspenseAchd;
    }

    public void setRtgsSuspenseAchd(String rtgsSuspenseAchd) {
        this.rtgsSuspenseAchd = rtgsSuspenseAchd;
    }

    
    /** getKeyData returns the Primary Key Columns for this TO
     *  User needs to add the Key columns as a setter
     *  Example :
     *            setKeyColumns("col1" + KEY_VAL_SEPARATOR + "col2");
     *            return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId");
        return prodId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("prodDesc", prodDesc));
        strB.append(getTOString("issueHd", issueHd));
        strB.append(getTOString("exchangeHd", exchangeHd));
        strB.append(getTOString("telegramChrgHd", telegramChrgHd));
        strB.append(getTOString("revalChrgHd", revalChrgHd));
        strB.append(getTOString("otherChrgHd", otherChrgHd));
        strB.append(getTOString("lapsedHd", lapsedHd));
        strB.append(getTOString("eftProduct", eftProduct));
        strB.append(getTOString("cashLimit", cashLimit));
        strB.append(getTOString("maximumAmount", maximumAmount));
        strB.append(getTOString("minimumAmount", minimumAmount));
        strB.append(getTOString("printServices", printServices));
        strB.append(getTOString("payHd", payHd));
        strB.append(getTOString("postageHd", postageHd));
        strB.append(getTOString("duplChrgHd", duplChrgHd));
        strB.append(getTOString("cancellChrgHd", cancellChrgHd));
        strB.append(getTOString("lapseAppl", lapseAppl));
        strB.append(getTOString("lapsePeriod", lapsePeriod));
        strB.append(getTOString("payIssueBranch", payIssueBranch));
        strB.append(getTOString("numberPattern", numberPattern));
        strB.append(getTOString("seriesMaintained", seriesMaintained));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("baseCurrency", baseCurrency));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeRemark", authorizeRemark));
        strB.append(getTOString("numberPatternSuffix", numberPatternSuffix));
        strB.append(getTOString("behavesLike", behavesLike));
        strB.append(getTOString("newProcedure", newProcedure));
        strB.append(getTOString("newProcStartDt", newProcStartDt));
        strB.append(getTOString("newProcIssueAcHd", newProcIssueAcHd));
        strB.append(getTOString("rtgsSuspenseAchd", rtgsSuspenseAchd));
        strB.append(getTOString("rtgsNeftGLType",rtgsNeftGLType));
        strB.append(getTOString("rtgsNeftProductType",rtgsNeftProductType));
        strB.append(getTOString("rtgsNeftProdId",rtgsNeftProdId));
        strB.append(getTOString("rtgsNeftActNum",rtgsNeftActNum));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("prodDesc", prodDesc));
        strB.append(getTOXml("issueHd", issueHd));
        strB.append(getTOXml("exchangeHd", exchangeHd));
        strB.append(getTOXml("telegramChrgHd", telegramChrgHd));
        strB.append(getTOXml("revalChrgHd", revalChrgHd));
        strB.append(getTOXml("otherChrgHd", otherChrgHd));
        strB.append(getTOXml("lapsedHd", lapsedHd));
        strB.append(getTOXml("eftProduct", eftProduct));
        strB.append(getTOXml("cashLimit", cashLimit));
        strB.append(getTOXml("maximumAmount", maximumAmount));
        strB.append(getTOXml("minimumAmount", minimumAmount));
        strB.append(getTOXml("printServices", printServices));
        strB.append(getTOXml("payHd", payHd));
        strB.append(getTOXml("postageHd", postageHd));
        strB.append(getTOXml("duplChrgHd", duplChrgHd));
        strB.append(getTOXml("cancellChrgHd", cancellChrgHd));
        strB.append(getTOXml("lapseAppl", lapseAppl));
        strB.append(getTOXml("lapsePeriod", lapsePeriod));
        strB.append(getTOXml("payIssueBranch", payIssueBranch));
        strB.append(getTOXml("numberPattern", numberPattern));
        strB.append(getTOXml("seriesMaintained", seriesMaintained));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("baseCurrency", baseCurrency));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeRemark", authorizeRemark));
        strB.append(getTOXml("numberPatternSuffix", numberPatternSuffix));
        strB.append(getTOXml("behavesLike", behavesLike));
        strB.append(getTOXml("newProcedure", newProcedure));
        strB.append(getTOXml("newProcStartDt", newProcStartDt));
        strB.append(getTOXml("newProcIssueAcHd", newProcIssueAcHd));
        strB.append(getTOXml("rtgsSuspenseAchd", rtgsSuspenseAchd));
        strB.append(getTOXml("rtgsNeftGLType",rtgsNeftGLType));
        strB.append(getTOXml("rtgsNeftProductType",rtgsNeftProductType));
        strB.append(getTOXml("rtgsNeftProdId",rtgsNeftProdId));
        strB.append(getTOXml("rtgsNeftActNum",rtgsNeftActNum));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
    
}