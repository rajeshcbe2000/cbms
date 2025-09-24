

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingTransferTO.java
 */

package com.see.truetransact.transferobject.trading.tradingtransfer;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Revathi L
 */
public class TradingTransferTO extends TransferObject implements Serializable {

    private String transferID = "";
    private String slNo = "";
    private String prodID = "";
    private String prod_Name = "";
    private String stockID = "";
    private String unitType = "";
    private String salesPrice = "";
    private String purchasePrice = "";
    private String availQty = "";
    private String transferQty = "";
    private String fromBranch = "";
    private String toBranch = "";
    private String achd = "";
    private Date transferDt = null;
    private String totAmt = "";
    private String branchID = "";
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
        strB.append(getTOString("transferID", transferID));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("prodID", prodID));
        strB.append(getTOString("prod_Name", prod_Name));
        strB.append(getTOString("stockID", stockID));
        strB.append(getTOString("unitType", unitType));
        strB.append(getTOString("salesPrice", salesPrice));
        strB.append(getTOString("purchasePrice", purchasePrice));
        strB.append(getTOString("availQty", availQty));
        strB.append(getTOString("transferQty", transferQty));
        strB.append(getTOString("totAmt", totAmt));
        strB.append(getTOString("fromBranch", fromBranch));
        strB.append(getTOString("toBranch", toBranch));
        strB.append(getTOString("achd", achd));
        strB.append(getTOString("transferDt", transferDt));
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
        strB.append(getTOXml("transferID", transferID));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("prodID", prodID));
        strB.append(getTOXml("stockID", stockID));
        strB.append(getTOXml("prod_Name", prod_Name));
        strB.append(getTOXml("unitType", unitType));
        strB.append(getTOXml("salesPrice", salesPrice));
        strB.append(getTOXml("purchasePrice", purchasePrice));
        strB.append(getTOXml("availQty", availQty));
        strB.append(getTOXml("transferQty", transferQty));
        strB.append(getTOXml("totAmt", totAmt));
        strB.append(getTOXml("fromBranch", fromBranch));
        strB.append(getTOXml("toBranch", toBranch));
        strB.append(getTOXml("achd", achd));
        strB.append(getTOXml("transferDt", transferDt));
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

    public String getTransferID() {
        return transferID;
    }

    public void setTransferID(String transferID) {
        this.transferID = transferID;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getProd_Name() {
        return prod_Name;
    }

    public void setProd_Name(String prod_Name) {
        this.prod_Name = prod_Name;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getAvailQty() {
        return availQty;
    }

    public void setAvailQty(String availQty) {
        this.availQty = availQty;
    }

    public String getTransferQty() {
        return transferQty;
    }

    public void setTransferQty(String transferQty) {
        this.transferQty = transferQty;
    }

    public String getFromBranch() {
        return fromBranch;
    }

    public void setFromBranch(String fromBranch) {
        this.fromBranch = fromBranch;
    }

    public String getToBranch() {
        return toBranch;
    }

    public void setToBranch(String toBranch) {
        this.toBranch = toBranch;
    }

    public String getAchd() {
        return achd;
    }

    public void setAchd(String achd) {
        this.achd = achd;
    }

    public Date getTransferDt() {
        return transferDt;
    }

    public void setTransferDt(Date transferDt) {
        this.transferDt = transferDt;
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

    
}
