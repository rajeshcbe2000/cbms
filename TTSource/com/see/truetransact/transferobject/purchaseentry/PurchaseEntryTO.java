/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigTO.java
 * 
 * Created on Thu Jan 20 16:44:08 IST 2005
 */
package com.see.truetransact.transferobject.purchaseentry;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class PurchaseEntryTO extends TransferObject implements Serializable {

    private Double purAmount = 0.0, purComm = 0.0, purchaseRet = 0.0, sundry = 0.0, investAcHead = 0.0, balAmt = 0.0;
    private String status_By = "";
    private String transType = "";
    private String supplier = "";
    private String isSundry = "";
    private String isInvestment = "";
    private String prodId = "";
    private String actnum = "";
    private String chequeNo = "";
    private String narration = "";
    private String tradeNarration = "";
    private Double cashAmount = 0.0;
    private String trans_mode = "";
    private String transMode = "";
    private String supActnum = "";
    private Date fromDate;
    private Double fromWeight=0.0;
    private Double toWeight=0.0;
    private Double amount=0.0;
    private Double grandTot=0.0;
    private String authorizeStatus = null;
    private String authorizeBy = null;
    private Date authorizeDte = null;
    private String status = null;
    private String purchaseType = "";
    private Date statusDt = null;

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }
    public String getTradeNarration() {
        return tradeNarration;
    }

    public void setTradeNarration(String tradeNarration) {
        this.tradeNarration = tradeNarration;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }
   
  

    public String getSupActnum() {
        return supActnum;
    }

    public void setSupActnum(String supActnum) {
        this.supActnum = supActnum;
    }

    public String getTrans_mode() {
        return trans_mode;
    }

    public void setTrans_mode(String trans_mode) {
        this.trans_mode = trans_mode;
    }

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getActnum() {
        return actnum;
    }

    public void setActnum(String actnum) {
        this.actnum = actnum;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getIsInvestment() {
        return isInvestment;
    }

    public void setIsInvestment(String isInvestment) {
        this.isInvestment = isInvestment;
    }

    public String getIsSundry() {
        return isSundry;
    }

    public void setIsSundry(String isSundry) {
        this.isSundry = isSundry;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getStatus_By() {
        return status_By;
    }

    public void setStatus_By(String status_By) {
        this.status_By = status_By;
    }

    public Double getBalAmt() {
        return balAmt;
    }

    public void setBalAmt(Double balAmt) {
        this.balAmt = balAmt;
    }
    private String purId = "";
    private String tradeId = "";

    public String getPurId() {
        return purId;
    }

    public void setPurId(String purId) {
        this.purId = purId;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public Double getInvestAcHead() {
        return investAcHead;
    }

    public void setInvestAcHead(Double investAcHead) {
        this.investAcHead = investAcHead;
    }

    public Double getPurAmount() {
        return purAmount;
    }

    public void setPurAmount(Double purAmount) {
        this.purAmount = purAmount;
    }

    public Double getPurComm() {
        return purComm;
    }

    public void setPurComm(Double purComm) {
        this.purComm = purComm;
    }

    public Double getPurchaseRet() {
        return purchaseRet;
    }

    public void setPurchaseRet(Double purchaseRet) {
        this.purchaseRet = purchaseRet;
    }

    public Double getSundry() {
        return sundry;
    }

    public void setSundry(Double sundry) {
        this.sundry = sundry;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public Double  getAmount() {
        return amount;
    }

    public void setAmount(Double  amount) {
        this.amount = amount;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Double  getFromWeight() {
        return fromWeight;
    }

    public void setFromWeight(Double  fromWeight) {
        this.fromWeight = fromWeight;
    }

    public Double  getToWeight() {
        return toWeight;
    }

    public void setToWeight(Double  toWeight) {
        this.toWeight = toWeight;
    }

    public Double  getGrandTot() {
        return grandTot;
    }

    public void setGrandTot(Double  grandTot) {
        this.grandTot = grandTot;
    }

    public String getKeyData() {

        return purId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("purId", purId));
        strB.append(getTOString("tradeId", tradeId));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("actnum", actnum));
        strB.append(getTOString("purAmount", purAmount));
        strB.append(getTOString("purComm", purComm));
        strB.append(getTOString("purchaseRet", purchaseRet));
        strB.append(getTOString("sundry", sundry));
        strB.append(getTOString("investAcHead", investAcHead));
        strB.append(getTOString("balAmt", balAmt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOString("status", status));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("transMode", transMode));
        strB.append(getTOString("supplier", supplier));
        strB.append(getTOString("isSundry", isSundry));
        strB.append(getTOString("isInvestment", isInvestment));
        strB.append(getTOString("cashAmount", cashAmount));
        strB.append(getTOString("chequeNo", chequeNo));
        strB.append(getTOString("narration", narration));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("fromWeight", fromWeight));
        strB.append(getTOString("toWeight", toWeight));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("grandTot", grandTot));
        strB.append(getTOString("purchaseType", purchaseType));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("purId ", purId));
        strB.append(getTOXml("tradeId ", tradeId));
        strB.append(getTOXml("prodId ", prodId));
        strB.append(getTOXml("actnum ", actnum));
        strB.append(getTOXml("purAmount", purAmount));
        strB.append(getTOXml("purComm", purComm));
        strB.append(getTOXml("purchaseRet", purchaseRet));
        strB.append(getTOXml("sundry", sundry));
        strB.append(getTOXml("investAcHead", investAcHead));
        strB.append(getTOXml("balAmt", balAmt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDte", authorizeDte));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("transMode", transMode));
        strB.append(getTOXml("supplier", supplier));
        strB.append(getTOXml("isSundry", isSundry));
        strB.append(getTOXml("isInvestment", isInvestment));
        strB.append(getTOXml("cashAmount", cashAmount));
        strB.append(getTOXml("chequeNo", chequeNo));
        strB.append(getTOXml("narration", narration));
        strB.append(getTOXml("toWeight", toWeight));
        strB.append(getTOXml("fromWeight", fromWeight));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("grandTot", grandTot));
        strB.append(getTOXml("purchaseType", purchaseType));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

   
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

   
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    
    public java.util.Date getAuthorizeDte() {
        return authorizeDte;
    }

    
    public void setAuthorizeDte(java.util.Date authorizeDte) {
        this.authorizeDte = authorizeDte;
    }

    
    public java.lang.String getStatus() {
        return status;
    }

    
    public void setStatus(java.lang.String status) {
        this.status = status;
    }
}