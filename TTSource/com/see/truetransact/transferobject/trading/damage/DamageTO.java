/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DamageTO.java
 *
 * Created on 03 Feb, 2016, 2:24 PM
 */
package com.see.truetransact.transferobject.trading.damage;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 *
 * @author Revathi L
 */
public class DamageTO extends TransferObject implements Serializable {

    private String damageID = "";
    private String slNo = "";
    private String prodID = "";
    private String prodName = "";
    private String stockID = "";
    private String unitType = "";
    private String availQty = "";
    private String damageQty = "";
    private String totAmt = "";
    private String totdamageQty = "";
    private String purchasePrice = "";
    private String salesPrice = "";
    private Date damageDt = null;
    private String branchID = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private String authorizeDt = null;

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("damageID", damageID));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("prodID", prodID));
        strB.append(getTOString("prodName", prodName));
        strB.append(getTOString("unitType", unitType));
        strB.append(getTOString("availQty", availQty));
        strB.append(getTOString("damageQty", damageQty));
        strB.append(getTOString("totdamageQty", totdamageQty));
        strB.append(getTOString("totAmt", totAmt));
        strB.append(getTOString("damageDt", damageDt));
        strB.append(getTOString("stockID", stockID));
        strB.append(getTOString("purchasePrice", purchasePrice));
        strB.append(getTOString("salesPrice", salesPrice));
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
        strB.append(getTOXml("damageID", damageID));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("prodID", prodID));
        strB.append(getTOXml("prodName", prodName));
        strB.append(getTOXml("stockID", stockID));
        strB.append(getTOXml("unitType", unitType));
        strB.append(getTOXml("availQty", availQty));
        strB.append(getTOXml("damageQty", damageQty));
        strB.append(getTOXml("totdamageQty", totdamageQty));
        strB.append(getTOXml("totAmt", totAmt));
        strB.append(getTOXml("damageDt", damageDt));
        strB.append(getTOXml("purchasePrice", purchasePrice));
        strB.append(getTOXml("salesPrice", salesPrice));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getDamageID() {
        return damageID;
    }

    public void setDamageID(String damageID) {
        this.damageID = damageID;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getAvailQty() {
        return availQty;
    }

    public void setAvailQty(String availQty) {
        this.availQty = availQty;
    }

    public String getDamageQty() {
        return damageQty;
    }

    public void setDamageQty(String damageQty) {
        this.damageQty = damageQty;
    }

    public String getTotdamageQty() {
        return totdamageQty;
    }

    public void setTotdamageQty(String totdamageQty) {
        this.totdamageQty = totdamageQty;
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

    public Date getDamageDt() {
        return damageDt;
    }

    public void setDamageDt(Date damageDt) {
        this.damageDt = damageDt;
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

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
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

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getTotAmt() {
        return totAmt;
    }

    public void setTotAmt(String totAmt) {
        this.totAmt = totAmt;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }
    
    
    
    

    public String getKeyData() {
        setKeyColumns(damageID);
        return damageID;
    }
}
