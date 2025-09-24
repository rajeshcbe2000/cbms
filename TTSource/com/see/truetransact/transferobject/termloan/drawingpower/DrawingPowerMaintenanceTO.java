/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DrawingPowerMaintenanceTO.java
 * 
 * Created on Fri Jul 16 16:44:07 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan.drawingpower;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_DRAWING_POWER.
 */
public class DrawingPowerMaintenanceTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String prodId = "";
    private String acctNo = "";
    private Double stockStatFreq = null;
    private Date prevDpvalueCalcdt = null;
    private String prevDpMonth = "";
    private Double prevDpValue = null;
    private String currDpMonth = "";
    private Date dueDt = null;
    private Date nextDueDt = null;
    private Date stockSubmitDt = null;
    private String goodsParticulars = "";
    private Date inspectionDt = null;
    private Double openingStockValue = null;
    private Double purchase = null;
    private Double closingStockValue = null;
    private Double sales = null;
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String securityNo = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchCode = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String stockStatDay = "";
    private Double serialNo = null;

    /**
     * Setter/Getter for BORROW_NO - table Field
     */
    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for ACCT_NO - table Field
     */
    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctNo() {
        return acctNo;
    }

    /**
     * Setter/Getter for STOCK_STAT_FREQ - table Field
     */
    public void setStockStatFreq(Double stockStatFreq) {
        this.stockStatFreq = stockStatFreq;
    }

    public Double getStockStatFreq() {
        return stockStatFreq;
    }

    /**
     * Setter/Getter for PREV_DPVALUE_CALCDT - table Field
     */
    public void setPrevDpvalueCalcdt(Date prevDpvalueCalcdt) {
        this.prevDpvalueCalcdt = prevDpvalueCalcdt;
    }

    public Date getPrevDpvalueCalcdt() {
        return prevDpvalueCalcdt;
    }

    /**
     * Setter/Getter for PREV_DP_MONTH - table Field
     */
    public void setPrevDpMonth(String prevDpMonth) {
        this.prevDpMonth = prevDpMonth;
    }

    public String getPrevDpMonth() {
        return prevDpMonth;
    }

    /**
     * Setter/Getter for PREV_DP_VALUE - table Field
     */
    public void setPrevDpValue(Double prevDpValue) {
        this.prevDpValue = prevDpValue;
    }

    public Double getPrevDpValue() {
        return prevDpValue;
    }

    /**
     * Setter/Getter for CURR_DP_MONTH - table Field
     */
    public void setCurrDpMonth(String currDpMonth) {
        this.currDpMonth = currDpMonth;
    }

    public String getCurrDpMonth() {
        return currDpMonth;
    }

    /**
     * Setter/Getter for DUE_DT - table Field
     */
    public void setDueDt(Date dueDt) {
        this.dueDt = dueDt;
    }

    public Date getDueDt() {
        return dueDt;
    }

    /**
     * Setter/Getter for STOCK_SUBMIT_DT - table Field
     */
    public void setStockSubmitDt(Date stockSubmitDt) {
        this.stockSubmitDt = stockSubmitDt;
    }

    public Date getStockSubmitDt() {
        return stockSubmitDt;
    }

    /**
     * Setter/Getter for GOODS_PARTICULARS - table Field
     */
    public void setGoodsParticulars(String goodsParticulars) {
        this.goodsParticulars = goodsParticulars;
    }

    public String getGoodsParticulars() {
        return goodsParticulars;
    }

    /**
     * Setter/Getter for INSPECTION_DT - table Field
     */
    public void setInspectionDt(Date inspectionDt) {
        this.inspectionDt = inspectionDt;
    }

    public Date getInspectionDt() {
        return inspectionDt;
    }

    /**
     * Setter/Getter for OPENING_STOCK_VALUE - table Field
     */
    public void setOpeningStockValue(Double openingStockValue) {
        this.openingStockValue = openingStockValue;
    }

    public Double getOpeningStockValue() {
        return openingStockValue;
    }

    /**
     * Setter/Getter for PURCHASE - table Field
     */
    public void setPurchase(Double purchase) {
        this.purchase = purchase;
    }

    public Double getPurchase() {
        return purchase;
    }

    /**
     * Setter/Getter for CLOSING_STOCK_VALUE - table Field
     */
    public void setClosingStockValue(Double closingStockValue) {
        this.closingStockValue = closingStockValue;
    }

    public Double getClosingStockValue() {
        return closingStockValue;
    }

    /**
     * Setter/Getter for SALES - table Field
     */
    public void setSales(Double sales) {
        this.sales = sales;
    }

    public Double getSales() {
        return sales;
    }

    /**
     * Setter/Getter for AUTHORIZE_REMARKS - table Field
     */
    public void setAuthorizeRemarks(String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    public String getAuthorizeRemarks() {
        return authorizeRemarks;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for SECURITY_NO - table Field
     */
    public void setSecurityNo(String securityNo) {
        this.securityNo = securityNo;
    }

    public String getSecurityNo() {
        return securityNo;
    }

    /**
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("borrowNo" + KEY_VAL_SEPARATOR + "securityNo");
        return borrowNo + KEY_VAL_SEPARATOR + securityNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOString("stockStatFreq", stockStatFreq));
        strB.append(getTOString("prevDpvalueCalcdt", prevDpvalueCalcdt));
        strB.append(getTOString("prevDpMonth", prevDpMonth));
        strB.append(getTOString("prevDpValue", prevDpValue));
        strB.append(getTOString("currDpMonth", currDpMonth));
        strB.append(getTOString("dueDt", dueDt));
        strB.append(getTOString("stockSubmitDt", stockSubmitDt));
        strB.append(getTOString("goodsParticulars", goodsParticulars));
        strB.append(getTOString("inspectionDt", inspectionDt));
        strB.append(getTOString("openingStockValue", openingStockValue));
        strB.append(getTOString("purchase", purchase));
        strB.append(getTOString("closingStockValue", closingStockValue));
        strB.append(getTOString("sales", sales));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("securityNo", securityNo));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("stockStatDay", stockStatDay));
        strB.append(getTOString("serialNo", serialNo));
        strB.append(getTOString("nextDueDt", nextDueDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("stockStatFreq", stockStatFreq));
        strB.append(getTOXml("prevDpvalueCalcdt", prevDpvalueCalcdt));
        strB.append(getTOXml("prevDpMonth", prevDpMonth));
        strB.append(getTOXml("prevDpValue", prevDpValue));
        strB.append(getTOXml("currDpMonth", currDpMonth));
        strB.append(getTOXml("dueDt", dueDt));
        strB.append(getTOXml("stockSubmitDt", stockSubmitDt));
        strB.append(getTOXml("goodsParticulars", goodsParticulars));
        strB.append(getTOXml("inspectionDt", inspectionDt));
        strB.append(getTOXml("openingStockValue", openingStockValue));
        strB.append(getTOXml("purchase", purchase));
        strB.append(getTOXml("closingStockValue", closingStockValue));
        strB.append(getTOXml("sales", sales));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("securityNo", securityNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("stockStatDay", stockStatDay));
        strB.append(getTOXml("serialNo", serialNo));
        strB.append(getTOXml("nextDueDt", nextDueDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property stockStatDay.
     *
     * @return Value of property stockStatDay.
     */
    public java.lang.String getStockStatDay() {
        return stockStatDay;
    }

    /**
     * Setter for property stockStatDay.
     *
     * @param stockStatDay New value of property stockStatDay.
     */
    public void setStockStatDay(java.lang.String stockStatDay) {
        this.stockStatDay = stockStatDay;
    }

    /**
     * Getter for property serialNo.
     *
     * @return Value of property serialNo.
     */
    public java.lang.Double getSerialNo() {
        return serialNo;
    }

    /**
     * Setter for property serialNo.
     *
     * @param serialNo New value of property serialNo.
     */
    public void setSerialNo(java.lang.Double serialNo) {
        this.serialNo = serialNo;
    }

    /**
     * Getter for property nextDueDt.
     *
     * @return Value of property nextDueDt.
     */
    public java.util.Date getNextDueDt() {
        return nextDueDt;
    }

    /**
     * Setter for property nextDueDt.
     *
     * @param nextDueDt New value of property nextDueDt.
     */
    public void setNextDueDt(java.util.Date nextDueDt) {
        this.nextDueDt = nextDueDt;
    }
}