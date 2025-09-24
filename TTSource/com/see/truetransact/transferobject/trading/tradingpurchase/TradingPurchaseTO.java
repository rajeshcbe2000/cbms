/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingPurchaseTO.java
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
public class TradingPurchaseTO extends TransferObject implements Serializable {
private String purchaseNo = "";
private String supplier = "";
private Date voucherDt = null;
private String voucherNo = "";
private String billNo = "";
private Date billDt = null;
private String productName = "";
private String unitType = "";
private String qty = "";
private String qtyUnit = "";
private String purchaseTot = "";
private String tax = "";
private String discount = "";
private String transCharges = "";
private String shrinkage = "";
private String shrinkageQty = "";
private String purchasePrice = "";
private String mrp = "";
private String salesPrice = "";
private Date expiryDt = null;
private String particulars = "";
private String place = "";
private String purchaseType = "";
private String bankAcHead = "";
private String indentNo = "";
private String branchCode = "";
private String purchaseReturn = "";
private String purchase = "";
private String taxTot = "";
private String tpTot = "";
private String discTot = "";
private String total = "";
private String purchaseAmt = "";
private String salesAmt = "";
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
        strB.append(getTOString("purchaseNo", purchaseNo));
        strB.append(getTOString("supplier", supplier));
        strB.append(getTOString("voucherDt", voucherDt));
        strB.append(getTOString("voucherNo", voucherNo));
        strB.append(getTOString("billNo", billNo));
        strB.append(getTOString("billDt", billDt));
        strB.append(getTOString("productName", productName));
        strB.append(getTOString("unitType", unitType));
        strB.append(getTOString("qty", qty));
        strB.append(getTOString("purchaseTot", purchaseTot));
        strB.append(getTOString("tax", tax));
        strB.append(getTOString("discount", discount));
        strB.append(getTOString("transCharges", transCharges));
        strB.append(getTOString("shrinkage", shrinkage));
        strB.append(getTOString("shrinkageQty", shrinkageQty));
        strB.append(getTOString("purchasePrice", purchasePrice));
        strB.append(getTOString("mrp", mrp));
        strB.append(getTOString("salesPrice", salesPrice));
        strB.append(getTOString("expiryDt", expiryDt));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("place", place));
        strB.append(getTOString("purchaseType", purchaseType));
        strB.append(getTOString("bankAcHead", bankAcHead));
        strB.append(getTOString("indentNo", indentNo));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("purchaseReturn", purchaseReturn));
         strB.append(getTOString("purchase", purchase));
        strB.append(getTOString("taxTot", taxTot));
        strB.append(getTOString("tpTot", tpTot));
        strB.append(getTOString("discTot", discTot));
        strB.append(getTOString("total", total));
        strB.append(getTOString("purchaseAmt", purchaseAmt));
        strB.append(getTOString("salesAmt", salesAmt));
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
        strB.append(getTOXml("purchaseNo", purchaseNo));
        strB.append(getTOXml("supplier", supplier));
        strB.append(getTOXml("voucherDt", voucherDt));
        strB.append(getTOXml("voucherNo", voucherNo));
        strB.append(getTOXml("billNo", billNo));
        strB.append(getTOXml("billDt", billDt));
        strB.append(getTOXml("productName", productName));
        strB.append(getTOXml("unitType", unitType));
        strB.append(getTOXml("qty", qty));
        strB.append(getTOXml("purchaseTot", purchaseTot));
        strB.append(getTOXml("tax", tax));
        strB.append(getTOXml("discount", discount));
        strB.append(getTOXml("transCharges", transCharges));
        strB.append(getTOXml("shrinkage", shrinkage));
        strB.append(getTOXml("shrinkageQty", shrinkageQty));
        strB.append(getTOXml("purchasePrice", purchasePrice));
        strB.append(getTOXml("mrp", mrp));
        strB.append(getTOXml("salesPrice", salesPrice));
        strB.append(getTOXml("expiryDt", expiryDt));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("place", place));
        strB.append(getTOXml("purchaseType", purchaseType));
        strB.append(getTOXml("bankAcHead", bankAcHead));
        strB.append(getTOXml("indentNo", indentNo));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("purchaseReturn", purchaseReturn));
        strB.append(getTOXml("purchase", purchase));
        strB.append(getTOXml("taxTot", taxTot));
        strB.append(getTOXml("tpTot", tpTot));
        strB.append(getTOXml("discTot", discTot));
        strB.append(getTOXml("total", total));
        strB.append(getTOXml("purchaseAmt", purchaseAmt));
        strB.append(getTOXml("salesAmt", salesAmt));
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

    public String getPurchaseReturn() {
        return purchaseReturn;
    }

    public void setPurchaseReturn(String purchaseReturn) {
        this.purchaseReturn = purchaseReturn;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
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
        setKeyColumns(purchaseNo);
        return purchaseNo;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Date getVoucherDt() {
        return voucherDt;
    }

    public void setVoucherDt(Date voucherDt) {
        this.voucherDt = voucherDt;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getPurchaseTot() {
        return purchaseTot;
    }

    public void setPurchaseTot(String purchaseTot) {
        this.purchaseTot = purchaseTot;
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

    public String getTransCharges() {
        return transCharges;
    }

    public void setTransCharges(String transCharges) {
        this.transCharges = transCharges;
    }

    public String getShrinkage() {
        return shrinkage;
    }

    public void setShrinkage(String shrinkage) {
        this.shrinkage = shrinkage;
    }

    public String getShrinkageQty() {
        return shrinkageQty;
    }

    public void setShrinkageQty(String shrinkageQty) {
        this.shrinkageQty = shrinkageQty;
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

    public Date getExpiryDt() {
        return expiryDt;
    }

    public void setExpiryDt(Date expiryDt) {
        this.expiryDt = expiryDt;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getBankAcHead() {
        return bankAcHead;
    }

    public void setBankAcHead(String bankAcHead) {
        this.bankAcHead = bankAcHead;
    }

    public String getIndentNo() {
        return indentNo;
    }

    public void setIndentNo(String indentNo) {
        this.indentNo = indentNo;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getTaxTot() {
        return taxTot;
    }

    public void setTaxTot(String taxTot) {
        this.taxTot = taxTot;
    }

    public String getTpTot() {
        return tpTot;
    }

    public void setTpTot(String tpTot) {
        this.tpTot = tpTot;
    }

    public String getDiscTot() {
        return discTot;
    }

    public void setDiscTot(String discTot) {
        this.discTot = discTot;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPurchaseAmt() {
        return purchaseAmt;
    }

    public void setPurchaseAmt(String purchaseAmt) {
        this.purchaseAmt = purchaseAmt;
    }

    public String getSalesAmt() {
        return salesAmt;
    }

    public void setSalesAmt(String salesAmt) {
        this.salesAmt = salesAmt;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    
   
}
