/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingSalesRetDetailsTO.java
 */

package com.see.truetransact.transferobject.trading.tradingsales;

/**
 *
 * @author Revathi.L
 */


import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;


public class TradingSalesRetDetailsTO extends TransferObject implements Serializable {

    private String salesRetNo = "";
    private String slNo = "";
    private String salesNo = "";
    private String prodName = "";
    private String rate = "";
    private String unitType = "";
    private String qty = "";
    private String tax = "";
    private String taxAmt = "";
    private String stockID = "";
    private String salesTot = "";
    private String retTot = "";
    private String retQty = "";
    private String status = "";

    

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getSalesNo() {
        return salesNo;
    }

    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }
    
    

    public String getKeyData() {
        setKeyColumns(salesNo);
        return salesNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("salesRetNo", salesRetNo));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("salesNo", salesNo));
        strB.append(getTOString("prod_name", prodName));
        strB.append(getTOString("rate", rate));
        strB.append(getTOString("bankAcHD", unitType));
        strB.append(getTOString("qty", qty));
        strB.append(getTOString("tax", tax));
        strB.append(getTOString("taxAmt", taxAmt));
        strB.append(getTOString("stockID", stockID));
        strB.append(getTOString("salesTot", salesTot));
        strB.append(getTOString("retQty", retQty));
        strB.append(getTOString("retTot", retTot));
        strB.append(getTOString("status", status));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("salesRetNo", salesRetNo));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("salesNo", salesNo));
        strB.append(getTOXml("prodName", prodName));
        strB.append(getTOXml("rate", rate));
        strB.append(getTOXml("unitType", unitType));
        strB.append(getTOXml("qty", qty));
        strB.append(getTOXml("tax", tax));
        strB.append(getTOXml("taxAmt", taxAmt));
        strB.append(getTOXml("stockID", stockID));
        strB.append(getTOXml("salesTot", salesTot));
        strB.append(getTOXml("retQty", retQty));
        strB.append(getTOXml("retTot", retTot));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getSalesRetNo() {
        return salesRetNo;
    }

    public void setSalesRetNo(String salesRetNo) {
        this.salesRetNo = salesRetNo;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getSalesTot() {
        return salesTot;
    }

    public void setSalesTot(String salesTot) {
        this.salesTot = salesTot;
    }

    public String getRetTot() {
        return retTot;
    }

    public void setRetTot(String retTot) {
        this.retTot = retTot;
    }

    public String getRetQty() {
        return retQty;
    }

    public void setRetQty(String retQty) {
        this.retQty = retQty;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }
    
    

    
}

