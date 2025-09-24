/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TradingProductTO.java
 * 
 * Created on Thu Feb 10 15:07:29 IST 2005
 */
/**
 *
 * @author Revathi.L
 */
package com.see.truetransact.transferobject.trading.tradingproduct;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TDS_CONFIG.
 */
public class TradingProductTO extends TransferObject implements Serializable {

    private String productID = "";
    private String productName = "";
    private String productDesc = "";
    private String unitType = "";
    private String groupName = "";
    private String subGroupName = "";
    private String newProd = "";
    private Date date = null;
    private String tax = "";
    private String taxPer = "";
    private String reOrderLevel = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSubGroupName() {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName) {
        this.subGroupName = subGroupName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTaxPer() {
        return taxPer;
    }

    public void setTaxPer(String taxPer) {
        this.taxPer = taxPer;
    }

    public String getReOrderLevel() {
        return reOrderLevel;
    }

    public void setReOrderLevel(String reOrderLevel) {
        this.reOrderLevel = reOrderLevel;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
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

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getNewProd() {
        return newProd;
    }

    public void setNewProd(String newProd) {
        this.newProd = newProd;
    }
    
    

    public String getKeyData() {
        setKeyColumns(productID);
        return productID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("productID", productID));
        strB.append(getTOString("productName", productName));
        strB.append(getTOString("productDesc", productDesc));
        strB.append(getTOString("unitType", unitType));
        strB.append(getTOString("groupName", groupName));
        strB.append(getTOString("subGroupName", subGroupName));
        strB.append(getTOString("newProd", newProd));
        strB.append(getTOString("date", date));
        strB.append(getTOString("tax", tax));
        strB.append(getTOString("taxPer", taxPer));
        strB.append(getTOString("reOrderLevel", reOrderLevel));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("productID", productID));
        strB.append(getTOXml("productName", productName));
        strB.append(getTOXml("productDesc", productDesc));
        strB.append(getTOXml("unitType", unitType));
        strB.append(getTOXml("groupName", groupName));
        strB.append(getTOXml("subGroupName", subGroupName));
        strB.append(getTOXml("newProd", newProd));
        strB.append(getTOXml("date", date));
        strB.append(getTOXml("tax", tax));
        strB.append(getTOXml("taxPer", taxPer));
        strB.append(getTOXml("reOrderLevel", reOrderLevel));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));

        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}