/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingPurchaseReturnTO.java
 */

package com.see.truetransact.transferobject.trading.tradingpurchase;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 *
 * @author Revathi L
 */
public class TradingPurchaseReturnTO extends TransferObject implements Serializable {
    private String purchaseRetNo = "";
    private String supplierName = "";
    private String purchaseNo = "";
    private String billNo = "";
    private String purchaseType = "";
    private String bankAcHead = "";
    private Date createdDt = null;
    private Date puchaseDt = null;
    private Date billDt = null;
    private String purchase = "";
    private String purchReturn = "";
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
        strB.append(getTOString("supplierName", supplierName));
        strB.append(getTOString("purchaseNo", purchaseNo));
        strB.append(getTOString("purchaseType", purchaseType));
        strB.append(getTOString("bankAcHead", bankAcHead));
        strB.append(getTOString("billNo", billNo));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("puchaseDt", puchaseDt));
        strB.append(getTOString("billDt", billDt));
        strB.append(getTOString("purchase", purchase));
        strB.append(getTOString("purchReturn", purchReturn));
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
        strB.append(getTOXml("supplierName", supplierName));
        strB.append(getTOXml("purchaseNo", purchaseNo));
        strB.append(getTOXml("purchaseType", purchaseType));
        strB.append(getTOXml("billNo", billNo));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("puchaseDt", puchaseDt));
        strB.append(getTOXml("billDt", billDt));
        strB.append(getTOXml("bankAcHead", bankAcHead));
        strB.append(getTOXml("purchase", purchase));
        strB.append(getTOXml("purchReturn", purchReturn));
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

    public String getPurchaseRetNo() {
        return purchaseRetNo;
    }

    public void setPurchaseRetNo(String purchaseRetNo) {
        this.purchaseRetNo = purchaseRetNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getPuchaseDt() {
        return puchaseDt;
    }

    public void setPuchaseDt(Date puchaseDt) {
        this.puchaseDt = puchaseDt;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public String getPurchReturn() {
        return purchReturn;
    }

    public void setPurchReturn(String purchReturn) {
        this.purchReturn = purchReturn;
    }
   
}

