/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingacheadTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */
package com.see.truetransact.transferobject.trading.tradingachead;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 *
 * @author Revathi L
 */
public class TradingacheadTO extends TransferObject implements Serializable {
private String accountHeadPid = "";
private String cashOnHand = "";
private String purchase = "";
private String sales = "";
private String purchaseReturn = "";
private String salesReturn = "";
private String damages = "";
private String purchaseVAT = "";
private String salesVAT = "";
private String saReceivable = "";
private String slPayable = "";
private String value = "";
private String stock = "";
private String period = "";
private String status = "";
private String statusBy = "";
private String statusDt = null;
private String authorizeStatus = null; 
private String authorizeBy = "";
private String authorizeDt = null;

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("cashOnHand", cashOnHand));
        strB.append(getTOString("purchase", purchase));
        strB.append(getTOString("sales", sales));
        strB.append(getTOString("purchaseReturn", purchaseReturn));
        strB.append(getTOString("salesReturn", salesReturn));
        strB.append(getTOString("damages", damages));
        strB.append(getTOString("purchaseVAT", purchaseVAT));
        strB.append(getTOString("salesVAT", salesVAT));
        strB.append(getTOString("saReceivable", saReceivable));
        strB.append(getTOString("slPayable", slPayable));
        strB.append(getTOString("value", value));
        strB.append(getTOString("stock", stock));
        strB.append(getTOString("period", period));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("cashOnHand", cashOnHand));
        strB.append(getTOXml("purchase", purchase));
        strB.append(getTOXml("sales", sales));
        strB.append(getTOXml("purchaseReturn", purchaseReturn));
        strB.append(getTOXml("salesReturn", salesReturn));
        strB.append(getTOXml("damages", damages));
        strB.append(getTOXml("purchaseVAT", purchaseVAT));
        strB.append(getTOXml("salesVAT", salesVAT));
        strB.append(getTOXml("saReceivable", saReceivable));
        strB.append(getTOXml("slPayable", slPayable));
        strB.append(getTOXml("value", value));
        strB.append(getTOXml("stock", stock));
        strB.append(getTOXml("period", period));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getAccountHeadPid() {
        return accountHeadPid;
    }

    public void setAccountHeadPid(String accountHeadPid) {
        this.accountHeadPid = accountHeadPid;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }


    public String getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(String authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }
    public String getKeyData() {
        setKeyColumns(accountHeadPid);
        return accountHeadPid;
    }

    public String getCashOnHand() {
        return cashOnHand;
    }

    public void setCashOnHand(String cashOnHand) {
        this.cashOnHand = cashOnHand;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getPurchaseReturn() {
        return purchaseReturn;
    }

    public void setPurchaseReturn(String purchaseReturn) {
        this.purchaseReturn = purchaseReturn;
    }

    public String getSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(String salesReturn) {
        this.salesReturn = salesReturn;
    }

    public String getDamages() {
        return damages;
    }

    public void setDamages(String damages) {
        this.damages = damages;
    }

    public String getPurchaseVAT() {
        return purchaseVAT;
    }

    public void setPurchaseVAT(String purchaseVAT) {
        this.purchaseVAT = purchaseVAT;
    }

    public String getSalesVAT() {
        return salesVAT;
    }

    public void setSalesVAT(String salesVAT) {
        this.salesVAT = salesVAT;
    }    

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getSaReceivable() {
        return saReceivable;
    }

    public void setSaReceivable(String saReceivable) {
        this.saReceivable = saReceivable;
    }

    public String getSlPayable() {
        return slPayable;
    }

    public void setSlPayable(String slPayable) {
        this.slPayable = slPayable;
    }
    
    
}
