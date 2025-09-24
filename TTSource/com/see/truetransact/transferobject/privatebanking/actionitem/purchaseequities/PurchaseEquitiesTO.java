/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PurchaseEquitiesTO.java
 * 
 * Created on Thu Jul 08 12:52:16 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.privatebanking.actionitem.purchaseequities;

/**
 *
 * @author Ashok
 */
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_PURCHASE_EQUITY.
 */
public class PurchaseEquitiesTO extends TransferObject implements Serializable {

    private String purchaseId = "";
    private String memberId = "";
    private String entitleGrp = "";
    private String portfolioLoc = "";
    private String pfAssetSubClass = "";
    private String pfAcct = "";
    private String setAssetSubClass = "";
    private String setAcct = "";
    private Date execDt = null;
    private Date settlementDt = null;
    private String edtsEligible = "";
    private String smiInfo = "";
    private String orderType = "";
    private String orderSubType = "";
    private String phoneOrder = "";
    private String currency = "";
    private String dealerName = "";
    private Double lotSize = null;
    private Double units = null;
    private Double price = null;
    private String commType = "";
    private Double commission = null;
    private String commCurrency = "";
    private Date goodTillDt = null;
    private String processEdts = "";
    private String exchange = "";
    private Double lodgementFee = null;
    private String lodgementCurrency = "";
    private Double commRate = null;
    private Double approxAmount = null;
    private Double minCommAmount = null;
    private String minCommCurrency = "";
    private String bankInst = "";
    private String creditNotes = "";
    private String traderInst = "";
    private String clientAdvices = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizeStatus = null;;

    /**
     * Setter/Getter for PURCHASE_ID - table Field
     */
    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    /**
     * Setter/Getter for MEMBER_ID - table Field
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }

    /**
     * Setter/Getter for ENTITLE_GRP - table Field
     */
    public void setEntitleGrp(String entitleGrp) {
        this.entitleGrp = entitleGrp;
    }

    public String getEntitleGrp() {
        return entitleGrp;
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
     * Setter/Getter for PF_ASSET_SUB_CLASS - table Field
     */
    public void setPfAssetSubClass(String pfAssetSubClass) {
        this.pfAssetSubClass = pfAssetSubClass;
    }

    public String getPfAssetSubClass() {
        return pfAssetSubClass;
    }

    /**
     * Setter/Getter for PF_ACCT - table Field
     */
    public void setPfAcct(String pfAcct) {
        this.pfAcct = pfAcct;
    }

    public String getPfAcct() {
        return pfAcct;
    }

    /**
     * Setter/Getter for SET_ASSET_SUB_CLASS - table Field
     */
    public void setSetAssetSubClass(String setAssetSubClass) {
        this.setAssetSubClass = setAssetSubClass;
    }

    public String getSetAssetSubClass() {
        return setAssetSubClass;
    }

    /**
     * Setter/Getter for SET_ACCT - table Field
     */
    public void setSetAcct(String setAcct) {
        this.setAcct = setAcct;
    }

    public String getSetAcct() {
        return setAcct;
    }

    /**
     * Setter/Getter for EXEC_DT - table Field
     */
    public void setExecDt(Date execDt) {
        this.execDt = execDt;
    }

    public Date getExecDt() {
        return execDt;
    }

    /**
     * Setter/Getter for SETTLEMENT_DT - table Field
     */
    public void setSettlementDt(Date settlementDt) {
        this.settlementDt = settlementDt;
    }

    public Date getSettlementDt() {
        return settlementDt;
    }

    /**
     * Setter/Getter for EDTS_ELIGIBLE - table Field
     */
    public void setEdtsEligible(String edtsEligible) {
        this.edtsEligible = edtsEligible;
    }

    public String getEdtsEligible() {
        return edtsEligible;
    }

    /**
     * Setter/Getter for SMI_INFO - table Field
     */
    public void setSmiInfo(String smiInfo) {
        this.smiInfo = smiInfo;
    }

    public String getSmiInfo() {
        return smiInfo;
    }

    /**
     * Setter/Getter for ORDER_TYPE - table Field
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderType() {
        return orderType;
    }

    /**
     * Setter/Getter for ORDER_SUB_TYPE - table Field
     */
    public void setOrderSubType(String orderSubType) {
        this.orderSubType = orderSubType;
    }

    public String getOrderSubType() {
        return orderSubType;
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
     * Setter/Getter for CURRENCY - table Field
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    /**
     * Setter/Getter for DEALER_NAME - table Field
     */
    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerName() {
        return dealerName;
    }

    /**
     * Setter/Getter for LOT_SIZE - table Field
     */
    public void setLotSize(Double lotSize) {
        this.lotSize = lotSize;
    }

    public Double getLotSize() {
        return lotSize;
    }

    /**
     * Setter/Getter for UNITS - table Field
     */
    public void setUnits(Double units) {
        this.units = units;
    }

    public Double getUnits() {
        return units;
    }

    /**
     * Setter/Getter for PRICE - table Field
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    /**
     * Setter/Getter for COMM_TYPE - table Field
     */
    public void setCommType(String commType) {
        this.commType = commType;
    }

    public String getCommType() {
        return commType;
    }

    /**
     * Setter/Getter for COMMISSION - table Field
     */
    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getCommission() {
        return commission;
    }

    /**
     * Setter/Getter for COMM_CURRENCY - table Field
     */
    public void setCommCurrency(String commCurrency) {
        this.commCurrency = commCurrency;
    }

    public String getCommCurrency() {
        return commCurrency;
    }

    /**
     * Setter/Getter for GOOD_TILL_DT - table Field
     */
    public void setGoodTillDt(Date goodTillDt) {
        this.goodTillDt = goodTillDt;
    }

    public Date getGoodTillDt() {
        return goodTillDt;
    }

    /**
     * Setter/Getter for PROCESS_EDTS - table Field
     */
    public void setProcessEdts(String processEdts) {
        this.processEdts = processEdts;
    }

    public String getProcessEdts() {
        return processEdts;
    }

    /**
     * Setter/Getter for EXCHANGE - table Field
     */
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getExchange() {
        return exchange;
    }

    /**
     * Setter/Getter for LODGEMENT_FEE - table Field
     */
    public void setLodgementFee(Double lodgementFee) {
        this.lodgementFee = lodgementFee;
    }

    public Double getLodgementFee() {
        return lodgementFee;
    }

    /**
     * Setter/Getter for LODGEMENT_CURRENCY - table Field
     */
    public void setLodgementCurrency(String lodgementCurrency) {
        this.lodgementCurrency = lodgementCurrency;
    }

    public String getLodgementCurrency() {
        return lodgementCurrency;
    }

    /**
     * Setter/Getter for COMM_RATE - table Field
     */
    public void setCommRate(Double commRate) {
        this.commRate = commRate;
    }

    public Double getCommRate() {
        return commRate;
    }

    /**
     * Setter/Getter for APPROX_AMOUNT - table Field
     */
    public void setApproxAmount(Double approxAmount) {
        this.approxAmount = approxAmount;
    }

    public Double getApproxAmount() {
        return approxAmount;
    }

    /**
     * Setter/Getter for MIN_COMM_AMOUNT - table Field
     */
    public void setMinCommAmount(Double minCommAmount) {
        this.minCommAmount = minCommAmount;
    }

    public Double getMinCommAmount() {
        return minCommAmount;
    }

    /**
     * Setter/Getter for MIN_COMM_CURRENCY - table Field
     */
    public void setMinCommCurrency(String minCommCurrency) {
        this.minCommCurrency = minCommCurrency;
    }

    public String getMinCommCurrency() {
        return minCommCurrency;
    }

    /**
     * Setter/Getter for BANK_INST - table Field
     */
    public void setBankInst(String bankInst) {
        this.bankInst = bankInst;
    }

    public String getBankInst() {
        return bankInst;
    }

    /**
     * Setter/Getter for CREDIT_NOTES - table Field
     */
    public void setCreditNotes(String creditNotes) {
        this.creditNotes = creditNotes;
    }

    public String getCreditNotes() {
        return creditNotes;
    }

    /**
     * Setter/Getter for TRADER_INST - table Field
     */
    public void setTraderInst(String traderInst) {
        this.traderInst = traderInst;
    }

    public String getTraderInst() {
        return traderInst;
    }

    /**
     * Setter/Getter for CLIENT_ADVICES - table Field
     */
    public void setClientAdvices(String clientAdvices) {
        this.clientAdvices = clientAdvices;
    }

    public String getClientAdvices() {
        return clientAdvices;
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
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
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
        strB.append(getTOString("purchaseId", purchaseId));
        strB.append(getTOString("memberId", memberId));
        strB.append(getTOString("entitleGrp", entitleGrp));
        strB.append(getTOString("portfolioLoc", portfolioLoc));
        strB.append(getTOString("pfAssetSubClass", pfAssetSubClass));
        strB.append(getTOString("pfAcct", pfAcct));
        strB.append(getTOString("setAssetSubClass", setAssetSubClass));
        strB.append(getTOString("setAcct", setAcct));
        strB.append(getTOString("execDt", execDt));
        strB.append(getTOString("settlementDt", settlementDt));
        strB.append(getTOString("edtsEligible", edtsEligible));
        strB.append(getTOString("smiInfo", smiInfo));
        strB.append(getTOString("orderType", orderType));
        strB.append(getTOString("orderSubType", orderSubType));
        strB.append(getTOString("phoneOrder", phoneOrder));
        strB.append(getTOString("currency", currency));
        strB.append(getTOString("dealerName", dealerName));
        strB.append(getTOString("lotSize", lotSize));
        strB.append(getTOString("units", units));
        strB.append(getTOString("price", price));
        strB.append(getTOString("commType", commType));
        strB.append(getTOString("commission", commission));
        strB.append(getTOString("commCurrency", commCurrency));
        strB.append(getTOString("goodTillDt", goodTillDt));
        strB.append(getTOString("processEdts", processEdts));
        strB.append(getTOString("exchange", exchange));
        strB.append(getTOString("lodgementFee", lodgementFee));
        strB.append(getTOString("lodgementCurrency", lodgementCurrency));
        strB.append(getTOString("commRate", commRate));
        strB.append(getTOString("approxAmount", approxAmount));
        strB.append(getTOString("minCommAmount", minCommAmount));
        strB.append(getTOString("minCommCurrency", minCommCurrency));
        strB.append(getTOString("bankInst", bankInst));
        strB.append(getTOString("creditNotes", creditNotes));
        strB.append(getTOString("traderInst", traderInst));
        strB.append(getTOString("clientAdvices", clientAdvices));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("purchaseId", purchaseId));
        strB.append(getTOXml("memberId", memberId));
        strB.append(getTOXml("entitleGrp", entitleGrp));
        strB.append(getTOXml("portfolioLoc", portfolioLoc));
        strB.append(getTOXml("pfAssetSubClass", pfAssetSubClass));
        strB.append(getTOXml("pfAcct", pfAcct));
        strB.append(getTOXml("setAssetSubClass", setAssetSubClass));
        strB.append(getTOXml("setAcct", setAcct));
        strB.append(getTOXml("execDt", execDt));
        strB.append(getTOXml("settlementDt", settlementDt));
        strB.append(getTOXml("edtsEligible", edtsEligible));
        strB.append(getTOXml("smiInfo", smiInfo));
        strB.append(getTOXml("orderType", orderType));
        strB.append(getTOXml("orderSubType", orderSubType));
        strB.append(getTOXml("phoneOrder", phoneOrder));
        strB.append(getTOXml("currency", currency));
        strB.append(getTOXml("dealerName", dealerName));
        strB.append(getTOXml("lotSize", lotSize));
        strB.append(getTOXml("units", units));
        strB.append(getTOXml("price", price));
        strB.append(getTOXml("commType", commType));
        strB.append(getTOXml("commission", commission));
        strB.append(getTOXml("commCurrency", commCurrency));
        strB.append(getTOXml("goodTillDt", goodTillDt));
        strB.append(getTOXml("processEdts", processEdts));
        strB.append(getTOXml("exchange", exchange));
        strB.append(getTOXml("lodgementFee", lodgementFee));
        strB.append(getTOXml("lodgementCurrency", lodgementCurrency));
        strB.append(getTOXml("commRate", commRate));
        strB.append(getTOXml("approxAmount", approxAmount));
        strB.append(getTOXml("minCommAmount", minCommAmount));
        strB.append(getTOXml("minCommCurrency", minCommCurrency));
        strB.append(getTOXml("bankInst", bankInst));
        strB.append(getTOXml("creditNotes", creditNotes));
        strB.append(getTOXml("traderInst", traderInst));
        strB.append(getTOXml("clientAdvices", clientAdvices));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}