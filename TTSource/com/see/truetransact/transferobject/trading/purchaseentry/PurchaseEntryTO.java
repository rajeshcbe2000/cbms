/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PurchaseEntryTO.java
 * 
 * Created on Thu Jan 20 16:44:08 IST 2005
 */

/**
 *
 * @author Revathi L
 */
package com.see.truetransact.transferobject.trading.purchaseentry;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class PurchaseEntryTO extends TransferObject implements Serializable {
    private String purchaseEntryID = "";
    private String supplierID = "";
    private String transMode = "";
    private String transType = "";
    private String creditFrom = "";
    private String prodType = "";
    private String prodID = "";
    private String acHd = "";
    private String balanceAmt = "";
    private String cash = "";
    private String purchaseComm = "";
    private String purchaseReturn = "";
    private String sundryAmt = "";
    private String chequeAmt = "";
    private String chequeNo = "";
    private String totalAmt = "";
    private String narration = "";
    private String status = "";
    private String statusBy = "";
    private String statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private String authorizeDt = null;
    private String branch_code = "";
    private String purchaseRetID = "";
     

    public String getKeyData() {

        return purchaseEntryID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("purchaseEntryID", purchaseEntryID));
        strB.append(getTOString("supplierID", supplierID));
        strB.append(getTOString("transMode", transMode));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("creditFrom", creditFrom));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodID", prodID));
        strB.append(getTOString("acHd", acHd));
        strB.append(getTOString("balanceAmt", balanceAmt));
        strB.append(getTOString("cash", cash));
        strB.append(getTOString("purchaseComm", purchaseComm));
        strB.append(getTOString("purchaseReturn", purchaseReturn));
        strB.append(getTOString("sundryAmt", sundryAmt));
        strB.append(getTOString("chequeNo", chequeNo));
        strB.append(getTOString("narration", narration));
        strB.append(getTOString("chequeAmt", chequeAmt));
        strB.append(getTOString("totalAmt", totalAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("purchaseRetID", purchaseRetID));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("purchaseEntryID ", purchaseEntryID));
        strB.append(getTOXml("supplierID ", supplierID));
        strB.append(getTOXml("transMode ", transMode));
        strB.append(getTOXml("transType ", transType));
        strB.append(getTOXml("creditFrom", creditFrom));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodID", prodID));
        strB.append(getTOXml("acHd", acHd));
        strB.append(getTOXml("balanceAmt", balanceAmt));
        strB.append(getTOXml("cash", cash));
        strB.append(getTOXml("purchaseComm", purchaseComm));
        strB.append(getTOXml("purchaseReturn", purchaseReturn));
        strB.append(getTOXml("sundryAmt", sundryAmt));
        strB.append(getTOXml("chequeNo", chequeNo));
        strB.append(getTOXml("narration", narration));
        strB.append(getTOXml("chequeAmt", chequeAmt));
        strB.append(getTOXml("totalAmt", totalAmt));
         strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("purchaseRetID", purchaseRetID));
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

    
    public java.lang.String getStatus() {
        return status;
    }

    
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public String getPurchaseEntryID() {
        return purchaseEntryID;
    }

    public void setPurchaseEntryID(String purchaseEntryID) {
        this.purchaseEntryID = purchaseEntryID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getCreditFrom() {
        return creditFrom;
    }

    public void setCreditFrom(String creditFrom) {
        this.creditFrom = creditFrom;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getAcHd() {
        return acHd;
    }

    public void setAcHd(String acHd) {
        this.acHd = acHd;
    }

    public String getBalanceAmt() {
        return balanceAmt;
    }

    public void setBalanceAmt(String balanceAmt) {
        this.balanceAmt = balanceAmt;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getPurchaseComm() {
        return purchaseComm;
    }

    public void setPurchaseComm(String purchaseComm) {
        this.purchaseComm = purchaseComm;
    }

    public String getPurchaseReturn() {
        return purchaseReturn;
    }

    public void setPurchaseReturn(String purchaseReturn) {
        this.purchaseReturn = purchaseReturn;
    }

    public String getSundryAmt() {
        return sundryAmt;
    }

    public void setSundryAmt(String sundryAmt) {
        this.sundryAmt = sundryAmt;
    }

    public String getChequeAmt() {
        return chequeAmt;
    }

    public void setChequeAmt(String chequeAmt) {
        this.chequeAmt = chequeAmt;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(String statusDt) {
        this.statusDt = statusDt;
    }

    public String getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(String authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getPurchaseRetID() {
        return purchaseRetID;
    }

    public void setPurchaseRetID(String purchaseRetID) {
        this.purchaseRetID = purchaseRetID;
    }
    
    
}