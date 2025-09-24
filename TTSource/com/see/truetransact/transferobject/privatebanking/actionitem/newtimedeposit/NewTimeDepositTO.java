/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NewTimeDepositTO.java
 * 
 * Created on Tue Jul 13 12:27:38 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.privatebanking.actionitem.newtimedeposit;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_TIME_DEPOSIT.
 */
public class NewTimeDepositTO extends TransferObject implements Serializable {

    private String refId = "";
    private String memberId = "";
    private String safeEntitleGrp = "";
    private String portfolioLoc = "";
    private String safeAssetSubclass = "";
    private String safeAcct = "";
    private String intAssetSubclass1 = "";
    private String intAssetSubclass2 = "";
    private String intAcct1 = "";
    private String intAcct2 = "";
    private String prinAssetSubclass = "";
    private String prinAcct = "";
    private Date execDt = null;
    private Date startDt = null;
    private Double amt = null;
    private Double spread = null;
    private String prodType = "";
    private String settlementType = "";
    private String tenor1 = "";
    private String tenor2 = "";
    private Date maturityDt = null;
    private String autoroll = "";
    private String phoneOrder = "";
    private String clientRate = "";
    private String bankInstruct = "";
    private String traderInstruct = "";
    private String creditNotes = "";
    private String clientAdvices = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;

    /**
     * Setter/Getter for REF_ID - table Field
     */
    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getRefId() {
        return refId;
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
     * Setter/Getter for SAFE_ENTITLE_GRP - table Field
     */
    public void setSafeEntitleGrp(String safeEntitleGrp) {
        this.safeEntitleGrp = safeEntitleGrp;
    }

    public String getSafeEntitleGrp() {
        return safeEntitleGrp;
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
     * Setter/Getter for SAFE_ASSET_SUBCLASS - table Field
     */
    public void setSafeAssetSubclass(String safeAssetSubclass) {
        this.safeAssetSubclass = safeAssetSubclass;
    }

    public String getSafeAssetSubclass() {
        return safeAssetSubclass;
    }

    /**
     * Setter/Getter for SAFE_ACCT - table Field
     */
    public void setSafeAcct(String safeAcct) {
        this.safeAcct = safeAcct;
    }

    public String getSafeAcct() {
        return safeAcct;
    }

    /**
     * Setter/Getter for INT_ASSET_SUBCLASS1 - table Field
     */
    public void setIntAssetSubclass1(String intAssetSubclass1) {
        this.intAssetSubclass1 = intAssetSubclass1;
    }

    public String getIntAssetSubclass1() {
        return intAssetSubclass1;
    }

    /**
     * Setter/Getter for INT_ASSET_SUBCLASS2 - table Field
     */
    public void setIntAssetSubclass2(String intAssetSubclass2) {
        this.intAssetSubclass2 = intAssetSubclass2;
    }

    public String getIntAssetSubclass2() {
        return intAssetSubclass2;
    }

    /**
     * Setter/Getter for INT_ACCT1 - table Field
     */
    public void setIntAcct1(String intAcct1) {
        this.intAcct1 = intAcct1;
    }

    public String getIntAcct1() {
        return intAcct1;
    }

    /**
     * Setter/Getter for INT_ACCT2 - table Field
     */
    public void setIntAcct2(String intAcct2) {
        this.intAcct2 = intAcct2;
    }

    public String getIntAcct2() {
        return intAcct2;
    }

    /**
     * Setter/Getter for PRIN_ASSET_SUBCLASS - table Field
     */
    public void setPrinAssetSubclass(String prinAssetSubclass) {
        this.prinAssetSubclass = prinAssetSubclass;
    }

    public String getPrinAssetSubclass() {
        return prinAssetSubclass;
    }

    /**
     * Setter/Getter for PRIN_ACCT - table Field
     */
    public void setPrinAcct(String prinAcct) {
        this.prinAcct = prinAcct;
    }

    public String getPrinAcct() {
        return prinAcct;
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
     * Setter/Getter for START_DT - table Field
     */
    public void setStartDt(Date startDt) {
        this.startDt = startDt;
    }

    public Date getStartDt() {
        return startDt;
    }

    /**
     * Setter/Getter for AMT - table Field
     */
    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Double getAmt() {
        return amt;
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
     * Setter/Getter for PROD_TYPE - table Field
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
    }

    /**
     * Setter/Getter for SETTLEMENT_TYPE - table Field
     */
    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType;
    }

    public String getSettlementType() {
        return settlementType;
    }

    /**
     * Setter/Getter for TENOR1 - table Field
     */
    public void setTenor1(String tenor1) {
        this.tenor1 = tenor1;
    }

    public String getTenor1() {
        return tenor1;
    }

    /**
     * Setter/Getter for TENOR2 - table Field
     */
    public void setTenor2(String tenor2) {
        this.tenor2 = tenor2;
    }

    public String getTenor2() {
        return tenor2;
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
     * Setter/Getter for AUTOROLL - table Field
     */
    public void setAutoroll(String autoroll) {
        this.autoroll = autoroll;
    }

    public String getAutoroll() {
        return autoroll;
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
     * Setter/Getter for CLIENT_RATE - table Field
     */
    public void setClientRate(String clientRate) {
        this.clientRate = clientRate;
    }

    public String getClientRate() {
        return clientRate;
    }

    /**
     * Setter/Getter for BANK_INSTRUCT - table Field
     */
    public void setBankInstruct(String bankInstruct) {
        this.bankInstruct = bankInstruct;
    }

    public String getBankInstruct() {
        return bankInstruct;
    }

    /**
     * Setter/Getter for TRADER_INSTRUCT - table Field
     */
    public void setTraderInstruct(String traderInstruct) {
        this.traderInstruct = traderInstruct;
    }

    public String getTraderInstruct() {
        return traderInstruct;
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
     * Setter/Getter for CLIENT_ADVICES - table Field
     */
    public void setClientAdvices(String clientAdvices) {
        this.clientAdvices = clientAdvices;
    }

    public String getClientAdvices() {
        return clientAdvices;
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
        strB.append(getTOString("refId", refId));
        strB.append(getTOString("memberId", memberId));
        strB.append(getTOString("safeEntitleGrp", safeEntitleGrp));
        strB.append(getTOString("portfolioLoc", portfolioLoc));
        strB.append(getTOString("safeAssetSubclass", safeAssetSubclass));
        strB.append(getTOString("safeAcct", safeAcct));
        strB.append(getTOString("intAssetSubclass1", intAssetSubclass1));
        strB.append(getTOString("intAssetSubclass2", intAssetSubclass2));
        strB.append(getTOString("intAcct1", intAcct1));
        strB.append(getTOString("intAcct2", intAcct2));
        strB.append(getTOString("prinAssetSubclass", prinAssetSubclass));
        strB.append(getTOString("prinAcct", prinAcct));
        strB.append(getTOString("execDt", execDt));
        strB.append(getTOString("startDt", startDt));
        strB.append(getTOString("amt", amt));
        strB.append(getTOString("spread", spread));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("settlementType", settlementType));
        strB.append(getTOString("tenor1", tenor1));
        strB.append(getTOString("tenor2", tenor2));
        strB.append(getTOString("maturityDt", maturityDt));
        strB.append(getTOString("autoroll", autoroll));
        strB.append(getTOString("phoneOrder", phoneOrder));
        strB.append(getTOString("bankInstruct", bankInstruct));
        strB.append(getTOString("traderInstruct", traderInstruct));
        strB.append(getTOString("creditNotes", creditNotes));
        strB.append(getTOString("clientAdvices", clientAdvices));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
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
        strB.append(getTOXml("refId", refId));
        strB.append(getTOXml("memberId", memberId));
        strB.append(getTOXml("safeEntitleGrp", safeEntitleGrp));
        strB.append(getTOXml("portfolioLoc", portfolioLoc));
        strB.append(getTOXml("safeAssetSubclass", safeAssetSubclass));
        strB.append(getTOXml("safeAcct", safeAcct));
        strB.append(getTOXml("intAssetSubclass1", intAssetSubclass1));
        strB.append(getTOXml("intAssetSubclass2", intAssetSubclass2));
        strB.append(getTOXml("intAcct1", intAcct1));
        strB.append(getTOXml("intAcct2", intAcct2));
        strB.append(getTOXml("prinAssetSubclass", prinAssetSubclass));
        strB.append(getTOXml("prinAcct", prinAcct));
        strB.append(getTOXml("execDt", execDt));
        strB.append(getTOXml("startDt", startDt));
        strB.append(getTOXml("amt", amt));
        strB.append(getTOXml("spread", spread));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("settlementType", settlementType));
        strB.append(getTOXml("tenor1", tenor1));
        strB.append(getTOXml("tenor2", tenor2));
        strB.append(getTOXml("maturityDt", maturityDt));
        strB.append(getTOXml("autoroll", autoroll));
        strB.append(getTOXml("phoneOrder", phoneOrder));
        strB.append(getTOXml("bankInstruct", bankInstruct));
        strB.append(getTOXml("traderInstruct", traderInstruct));
        strB.append(getTOXml("creditNotes", creditNotes));
        strB.append(getTOXml("clientAdvices", clientAdvices));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}