/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FTInternalTO.java
 * 
 * Created on Tue Jun 22 16:25:32 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.privatebanking.actionitem.ftinternal;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_AI_FTINTERNAL.
 */
public class FTInternalTO extends TransferObject implements Serializable {

    private String refNo = "";
    private String ordId = "";
    private String drEntitleGrp = "";
    private String drPortfolioLoc = "";
    private String drAssetSubclass = "";
    private String drAcct = "";
    private String crEntitleGrp = "";
    private String crPortfolioLoc = "";
    private String crAssetSubclass = "";
    private String crAcct = "";
    private Date ordExecDt = null;
    private Date ordValueDt = null;
    private Double drAmount = null;
    private Double crAmount = null;
    private String bankOfficeInstruct = "";
    private String traderInstruct = "";
    private String clientAdvcies = "";
    private String creditNotes = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizeRemarks = "";
    private String memberId = "";

    /**
     * Setter/Getter for REF_NO - table Field
     */
    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefNo() {
        return refNo;
    }

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
     * Setter/Getter for DR_ENTITLE_GRP - table Field
     */
    public void setDrEntitleGrp(String drEntitleGrp) {
        this.drEntitleGrp = drEntitleGrp;
    }

    public String getDrEntitleGrp() {
        return drEntitleGrp;
    }

    /**
     * Setter/Getter for DR_PORTFOLIO_LOC - table Field
     */
    public void setDrPortfolioLoc(String drPortfolioLoc) {
        this.drPortfolioLoc = drPortfolioLoc;
    }

    public String getDrPortfolioLoc() {
        return drPortfolioLoc;
    }

    /**
     * Setter/Getter for DR_ASSET_SUBCLASS - table Field
     */
    public void setDrAssetSubclass(String drAssetSubclass) {
        this.drAssetSubclass = drAssetSubclass;
    }

    public String getDrAssetSubclass() {
        return drAssetSubclass;
    }

    /**
     * Setter/Getter for DR_ACCT - table Field
     */
    public void setDrAcct(String drAcct) {
        this.drAcct = drAcct;
    }

    public String getDrAcct() {
        return drAcct;
    }

    /**
     * Setter/Getter for CR_ENTITLE_GRP - table Field
     */
    public void setCrEntitleGrp(String crEntitleGrp) {
        this.crEntitleGrp = crEntitleGrp;
    }

    public String getCrEntitleGrp() {
        return crEntitleGrp;
    }

    /**
     * Setter/Getter for CR_PORTFOLIO_LOC - table Field
     */
    public void setCrPortfolioLoc(String crPortfolioLoc) {
        this.crPortfolioLoc = crPortfolioLoc;
    }

    public String getCrPortfolioLoc() {
        return crPortfolioLoc;
    }

    /**
     * Setter/Getter for CR_ASSET_SUBCLASS - table Field
     */
    public void setCrAssetSubclass(String crAssetSubclass) {
        this.crAssetSubclass = crAssetSubclass;
    }

    public String getCrAssetSubclass() {
        return crAssetSubclass;
    }

    /**
     * Setter/Getter for CR_ACCT - table Field
     */
    public void setCrAcct(String crAcct) {
        this.crAcct = crAcct;
    }

    public String getCrAcct() {
        return crAcct;
    }

    /**
     * Setter/Getter for ORD_EXEC_DT - table Field
     */
    public void setOrdExecDt(Date ordExecDt) {
        this.ordExecDt = ordExecDt;
    }

    public Date getOrdExecDt() {
        return ordExecDt;
    }

    /**
     * Setter/Getter for ORD_VALUE_DT - table Field
     */
    public void setOrdValueDt(Date ordValueDt) {
        this.ordValueDt = ordValueDt;
    }

    public Date getOrdValueDt() {
        return ordValueDt;
    }

    /**
     * Setter/Getter for DR_AMOUNT - table Field
     */
    public void setDrAmount(Double drAmount) {
        this.drAmount = drAmount;
    }

    public Double getDrAmount() {
        return drAmount;
    }

    /**
     * Setter/Getter for CR_AMOUNT - table Field
     */
    public void setCrAmount(Double crAmount) {
        this.crAmount = crAmount;
    }

    public Double getCrAmount() {
        return crAmount;
    }

    /**
     * Setter/Getter for BANK_OFFICE_INSTRUCT - table Field
     */
    public void setBankOfficeInstruct(String bankOfficeInstruct) {
        this.bankOfficeInstruct = bankOfficeInstruct;
    }

    public String getBankOfficeInstruct() {
        return bankOfficeInstruct;
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
     * Setter/Getter for CLIENT_ADVCIES - table Field
     */
    public void setClientAdvcies(String clientAdvcies) {
        this.clientAdvcies = clientAdvcies;
    }

    public String getClientAdvcies() {
        return clientAdvcies;
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
     * Setter/Getter for AUTHORIZE_REMARKS - table Field
     */
    public void setAuthorizeRemarks(String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    public String getAuthorizeRemarks() {
        return authorizeRemarks;
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
        strB.append(getTOString("refNo", refNo));
        strB.append(getTOString("ordId", ordId));
        strB.append(getTOString("drEntitleGrp", drEntitleGrp));
        strB.append(getTOString("drPortfolioLoc", drPortfolioLoc));
        strB.append(getTOString("drAssetSubclass", drAssetSubclass));
        strB.append(getTOString("drAcct", drAcct));
        strB.append(getTOString("crEntitleGrp", crEntitleGrp));
        strB.append(getTOString("crPortfolioLoc", crPortfolioLoc));
        strB.append(getTOString("crAssetSubclass", crAssetSubclass));
        strB.append(getTOString("crAcct", crAcct));
        strB.append(getTOString("ordExecDt", ordExecDt));
        strB.append(getTOString("ordValueDt", ordValueDt));
        strB.append(getTOString("drAmount", drAmount));
        strB.append(getTOString("crAmount", crAmount));
        strB.append(getTOString("bankOfficeInstruct", bankOfficeInstruct));
        strB.append(getTOString("traderInstruct", traderInstruct));
        strB.append(getTOString("clientAdvcies", clientAdvcies));
        strB.append(getTOString("creditNotes", creditNotes));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("memberId", memberId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("refNo", refNo));
        strB.append(getTOXml("ordId", ordId));
        strB.append(getTOXml("drEntitleGrp", drEntitleGrp));
        strB.append(getTOXml("drPortfolioLoc", drPortfolioLoc));
        strB.append(getTOXml("drAssetSubclass", drAssetSubclass));
        strB.append(getTOXml("drAcct", drAcct));
        strB.append(getTOXml("crEntitleGrp", crEntitleGrp));
        strB.append(getTOXml("crPortfolioLoc", crPortfolioLoc));
        strB.append(getTOXml("crAssetSubclass", crAssetSubclass));
        strB.append(getTOXml("crAcct", crAcct));
        strB.append(getTOXml("ordExecDt", ordExecDt));
        strB.append(getTOXml("ordValueDt", ordValueDt));
        strB.append(getTOXml("drAmount", drAmount));
        strB.append(getTOXml("crAmount", crAmount));
        strB.append(getTOXml("bankOfficeInstruct", bankOfficeInstruct));
        strB.append(getTOXml("traderInstruct", traderInstruct));
        strB.append(getTOXml("clientAdvcies", clientAdvcies));
        strB.append(getTOXml("creditNotes", creditNotes));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("memberId", memberId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}