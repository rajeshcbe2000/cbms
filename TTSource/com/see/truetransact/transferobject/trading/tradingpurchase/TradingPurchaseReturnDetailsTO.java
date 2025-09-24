/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingPurchaseReturnDetailsTO.java
 */

package com.see.truetransact.transferobject.trading.tradingpurchase;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 *
 * @author Revathi L
 */
public class TradingPurchaseReturnDetailsTO extends TransferObject implements Serializable {
    private String purchaseRetNo = "";
    private String slNo = "";
    private String purchaseNo = "";
    private String prodName = "";
    private String unitType = "";
    private String tax = "";
    private String purchasePrice = null;
    private String salesPrice = null;
    private String purchQty = null;
    private String mrp = "";
    private Date expiry_Dt = null;
    private String availQty = "";
    private String returnQty = "";
    private String purchTotal = "";
    private String returnTotal = "";
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
        strB.append(getTOString("purchaseRetNo", purchaseRetNo));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("purchaseNo", purchaseNo));
        strB.append(getTOString("prodName", prodName));
        strB.append(getTOString("unitType", unitType));
        strB.append(getTOString("tax", tax));
        strB.append(getTOString("mrp", mrp));
        strB.append(getTOString("expiry_Dt", expiry_Dt));
        strB.append(getTOString("purchasePrice", purchasePrice));
        strB.append(getTOString("salesPrice", salesPrice));
        strB.append(getTOString("purchQty", purchQty));
        strB.append(getTOString("availQty", availQty));
        strB.append(getTOString("returnQty", returnQty));
        strB.append(getTOString("purchTotal", purchTotal));
        strB.append(getTOString("returnTotal", returnTotal));
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
        strB.append(getTOXml("purchaseRetNo", purchaseRetNo));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("purchaseNo", purchaseNo));
        strB.append(getTOXml("prodName", prodName));
        strB.append(getTOXml("unitType", unitType));
        strB.append(getTOXml("tax", tax));
        strB.append(getTOXml("mrp", mrp));
        strB.append(getTOXml("expiry_Dt", expiry_Dt));
        strB.append(getTOXml("purchasePrice", purchasePrice));
        strB.append(getTOXml("salesPrice", salesPrice));
        strB.append(getTOXml("purchQty", purchQty));
        strB.append(getTOXml("availQty", availQty));
        strB.append(getTOXml("returnQty", returnQty));
        strB.append(getTOXml("purchTotal", purchTotal));
        strB.append(getTOXml("returnTotal", returnTotal));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
        setKeyColumns(purchaseNo);
        return purchaseNo;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }


    public String getPurchaseRetNo() {
        return purchaseRetNo;
    }

    public void setPurchaseRetNo(String purchaseRetNo) {
        this.purchaseRetNo = purchaseRetNo;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getPurchQty() {
        return purchQty;
    }

    public void setPurchQty(String purchQty) {
        this.purchQty = purchQty;
    }

    public String getAvailQty() {
        return availQty;
    }

    public void setAvailQty(String availQty) {
        this.availQty = availQty;
    }

    public String getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(String returnQty) {
        this.returnQty = returnQty;
    }

    public String getPurchTotal() {
        return purchTotal;
    }

    public void setPurchTotal(String purchTotal) {
        this.purchTotal = purchTotal;
    }

    public String getReturnTotal() {
        return returnTotal;
    }

    public void setReturnTotal(String returnTotal) {
        this.returnTotal = returnTotal;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public Date getExpiry_Dt() {
        return expiry_Dt;
    }

    public void setExpiry_Dt(Date expiry_Dt) {
        this.expiry_Dt = expiry_Dt;
    }
   
    

}

