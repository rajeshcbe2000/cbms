/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingStockTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */
package com.see.truetransact.transferobject.trading.tradingstock;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 *
 * @author Revathi L
 */
public class TradingStockTO extends TransferObject implements Serializable {
private String stockID = "";
private String stock_Type = "";
private String stock_Sales_Price = "";
private String stock_Purchase_Price = "";
private String product_ID = "";
private String stock_Quant = "";
private String sales_Tax = "";
private String prod_Name = "";
private String loose_Qty = "";
private Date expiry_Dt = null;
private String trans_Charge = "";
private String stock_MRP = "";
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
        strB.append(getTOString("stockID", stockID));
        strB.append(getTOString("stock_Type", stock_Type));
        strB.append(getTOString("stock_Sales_Price", stock_Sales_Price));
        strB.append(getTOString("stock_Purchase_Price", stock_Purchase_Price));
        strB.append(getTOString("product_ID", product_ID));
        strB.append(getTOString("stock_Quant", stock_Quant));
        strB.append(getTOString("sales_Tax", sales_Tax));
        strB.append(getTOString("prod_Name", prod_Name));
        strB.append(getTOString("loose_Qty", loose_Qty));
        strB.append(getTOString("expiry_Dt", expiry_Dt));
        strB.append(getTOString("trans_Charge", trans_Charge));
        strB.append(getTOString("stock_MRP", stock_MRP));
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
        strB.append(getTOXml("stockID", stockID));
        strB.append(getTOXml("stock_Type", stock_Type));
        strB.append(getTOXml("stock_Sales_Price", stock_Sales_Price));
        strB.append(getTOXml("stock_Purchase_Price", stock_Purchase_Price));
        strB.append(getTOXml("product_ID", product_ID));
        strB.append(getTOXml("stock_Quant", stock_Quant));
        strB.append(getTOXml("sales_Tax", sales_Tax));
        strB.append(getTOXml("prod_Name", prod_Name));
        strB.append(getTOXml("loose_Qty", loose_Qty));
        strB.append(getTOXml("expiry_Dt", expiry_Dt));
        strB.append(getTOXml("trans_Charge", trans_Charge));
        strB.append(getTOXml("stock_MRP", stock_MRP));
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

    public String getSales_Tax() {
        return sales_Tax;
    }

    public void setSales_Tax(String sales_Tax) {
        this.sales_Tax = sales_Tax;
    }

    public String getProd_Name() {
        return prod_Name;
    }

    public void setProd_Name(String prod_Name) {
        this.prod_Name = prod_Name;
    }

    public String getLoose_Qty() {
        return loose_Qty;
    }

    public void setLoose_Qty(String loose_Qty) {
        this.loose_Qty = loose_Qty;
    }

    public Date getExpiry_Dt() {
        return expiry_Dt;
    }

    public void setExpiry_Dt(Date expiry_Dt) {
        this.expiry_Dt = expiry_Dt;
    }

    public String getTrans_Charge() {
        return trans_Charge;
    }

    public void setTrans_Charge(String trans_Charge) {
        this.trans_Charge = trans_Charge;
    }

    public String getStock_MRP() {
        return stock_MRP;
    }

    public void setStock_MRP(String stock_MRP) {
        this.stock_MRP = stock_MRP;
    }
    
    
}
