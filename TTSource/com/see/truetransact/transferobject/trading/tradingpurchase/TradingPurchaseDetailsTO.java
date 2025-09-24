/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingPurchaseDetailsTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */
package com.see.truetransact.transferobject.trading.tradingpurchase;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 *
 * @author Revathi L
 */
public class TradingPurchaseDetailsTO extends TransferObject implements Serializable {
private String purchaseNo = "";
private String prodId = "";
private String prodName = "";
private String unitType = "";
private String voucherNo = "";
private String billNo = "";
private Date billDt = null;
private String qty = "";
private String qtyUnit = "";
private String tax = "";
private String discount = "";
private String purchasePrice = "";
private String mrp = "";
private String salesPrice = "";
private String stkQty = "";
private String totQty = "";
private String total = "";
private String mfgBatchID = "";
private Date expiry_Dt = null;
private String particulars = "";
private String slNo = "";
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
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("purchaseNo", purchaseNo));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("prodName", prodName));
        strB.append(getTOString("unitType", unitType));
        strB.append(getTOString("voucherNo", voucherNo));
        strB.append(getTOString("billNo", billNo));
        strB.append(getTOString("billDt", billDt));
        strB.append(getTOString("qty", qty));
        strB.append(getTOString("qtyUnit", qtyUnit));
        strB.append(getTOString("tax", tax));
        strB.append(getTOString("discount", discount));
        strB.append(getTOString("purchasePrice", purchasePrice));
        strB.append(getTOString("mrp", mrp));
        strB.append(getTOString("salesPrice", salesPrice));
        strB.append(getTOString("total", total));
        strB.append(getTOString("mfgBatchID", mfgBatchID));
        strB.append(getTOString("stkQty", stkQty));
        strB.append(getTOString("totQty", totQty));
        strB.append(getTOString("expiry_Dt", expiry_Dt));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("slNo", slNo));
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
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("purchaseNo", purchaseNo));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("unitType", unitType));
        strB.append(getTOXml("voucherNo", voucherNo));
        strB.append(getTOXml("billNo", billNo));
        strB.append(getTOXml("billDt", billDt));
        strB.append(getTOXml("qty", qty));
        strB.append(getTOXml("qtyUnit", qtyUnit));
        strB.append(getTOXml("prodName", prodName));
        strB.append(getTOXml("tax", tax));
        strB.append(getTOXml("discount", discount));
        strB.append(getTOXml("purchasePrice", purchasePrice));
        strB.append(getTOXml("mrp", mrp));
        strB.append(getTOXml("salesPrice", salesPrice));
        strB.append(getTOXml("total", total));
        strB.append(getTOXml("expiry_Dt", expiry_Dt));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("stkQty", stkQty));
        strB.append(getTOXml("totQty", totQty));
        strB.append(getTOXml("mfgBatchID", mfgBatchID));
        strB.append(getTOXml("slNo", slNo));
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    
    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getStkQty() {
        return stkQty;
    }

    public void setStkQty(String stkQty) {
        this.stkQty = stkQty;
    }

    public String getTotQty() {
        return totQty;
    }

    public void setTotQty(String totQty) {
        this.totQty = totQty;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public Date getExpiry_Dt() {
        return expiry_Dt;
    }

    public void setExpiry_Dt(Date expiry_Dt) {
        this.expiry_Dt = expiry_Dt;
    }

    

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Date getBillDt() {
        return billDt;
    }

    public void setBillDt(Date billDt) {
        this.billDt = billDt;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getMfgBatchID() {
        return mfgBatchID;
    }

    public void setMfgBatchID(String mfgBatchID) {
        this.mfgBatchID = mfgBatchID;
    }

    
    
   
}
