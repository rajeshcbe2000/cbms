/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * PhysicalVerificationTO.java
 */

package com.see.truetransact.transferobject.trading.tradingstock;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Revathi L
 */
public class PhysicalVerificationTO extends TransferObject implements Serializable {
private String phy_ID = "";
private String stockID = "";
private String sl_No = "";
private String stock_Type = "";
private String stock_Sales_Price = "";
private String stock_Purchase_Price = "";
private String product_ID = "";
private String stock_Quant = "";
private String stock_Diff = "";
private String prod_Name = "";
private String phy_Qty = "";
private String remarks = "";
private String totAmt = "";
private String amount = "";
private String tranID = "";
private Date phy_Dt = null;
private String stock_MRP = "";
private String status = "";
private String statusBy = "";
private Date statusDt = null;
private String authorizeStatus = null; 
private String authorizeBy = "";
private String authorizeDt = null;
private String branchID = "";

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("phy_ID", phy_ID));
        strB.append(getTOString("stockID", stockID));
        strB.append(getTOString("sl_No", sl_No));
        strB.append(getTOString("stock_Type", stock_Type));
        strB.append(getTOString("stock_Sales_Price", stock_Sales_Price));
        strB.append(getTOString("stock_Purchase_Price", stock_Purchase_Price));
        strB.append(getTOString("product_ID", product_ID));
        strB.append(getTOString("stock_Quant", stock_Quant));
        strB.append(getTOString("prod_Name", prod_Name));
        strB.append(getTOString("phy_Qty", phy_Qty));
        strB.append(getTOString("phy_Dt", phy_Dt));
        strB.append(getTOString("stock_MRP", stock_MRP));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("stock_Diff", stock_Diff));
        strB.append(getTOString("totAmt", totAmt));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("tranID", tranID));
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
        strB.append(getTOXml("phy_ID", phy_ID));
        strB.append(getTOXml("stockID", stockID));
        strB.append(getTOXml("sl_No", sl_No));
        strB.append(getTOXml("stock_Type", stock_Type));
        strB.append(getTOXml("stock_Sales_Price", stock_Sales_Price));
        strB.append(getTOXml("stock_Purchase_Price", stock_Purchase_Price));
        strB.append(getTOXml("product_ID", product_ID));
        strB.append(getTOXml("stock_Quant", stock_Quant));
        strB.append(getTOXml("prod_Name", prod_Name));
        strB.append(getTOXml("phy_Qty", phy_Qty));
        strB.append(getTOXml("phy_Dt", phy_Dt));
        strB.append(getTOXml("stock_MRP", stock_MRP));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("stock_Diff", stock_Diff));
        strB.append(getTOXml("totAmt", totAmt));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("tranID", tranID));
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
    public String getKeyData() {
        setKeyColumns(stockID);
        return stockID;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public String getStock_Type() {
        return stock_Type;
    }

    public void setStock_Type(String stock_Type) {
        this.stock_Type = stock_Type;
    }

    public String getStock_Sales_Price() {
        return stock_Sales_Price;
    }

    public void setStock_Sales_Price(String stock_Sales_Price) {
        this.stock_Sales_Price = stock_Sales_Price;
    }

    public String getStock_Purchase_Price() {
        return stock_Purchase_Price;
    }

    public void setStock_Purchase_Price(String stock_Purchase_Price) {
        this.stock_Purchase_Price = stock_Purchase_Price;
    }

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    public String getStock_Quant() {
        return stock_Quant;
    }

    public void setStock_Quant(String stock_Quant) {
        this.stock_Quant = stock_Quant;
    }


    public String getProd_Name() {
        return prod_Name;
    }

    public void setProd_Name(String prod_Name) {
        this.prod_Name = prod_Name;
    }


    public String getStock_MRP() {
        return stock_MRP;
    }

    public void setStock_MRP(String stock_MRP) {
        this.stock_MRP = stock_MRP;
    }

    public String getPhy_ID() {
        return phy_ID;
    }

    public void setPhy_ID(String phy_ID) {
        this.phy_ID = phy_ID;
    }

    public String getStock_Diff() {
        return stock_Diff;
    }

    public void setStock_Diff(String stock_Diff) {
        this.stock_Diff = stock_Diff;
    }

    public String getPhy_Qty() {
        return phy_Qty;
    }

    public void setPhy_Qty(String phy_Qty) {
        this.phy_Qty = phy_Qty;
    }

    public Date getPhy_Dt() {
        return phy_Dt;
    }

    public void setPhy_Dt(Date phy_Dt) {
        this.phy_Dt = phy_Dt;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getSl_No() {
        return sl_No;
    }

    public void setSl_No(String sl_No) {
        this.sl_No = sl_No;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTotAmt() {
        return totAmt;
    }

    public void setTotAmt(String totAmt) {
        this.totAmt = totAmt;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTranID() {
        return tranID;
    }

    public void setTranID(String tranID) {
        this.tranID = tranID;
    }

    
    
}
