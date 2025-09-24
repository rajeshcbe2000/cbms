/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DrawingPowerMaintenanceDetailsTO.java
 * 
 * Created on Fri Jul 16 16:47:32 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan.drawingpower;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_DRAWING_POWER_DETAILS.
 */
public class DrawingPowerMaintenanceDetailsTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String prodId = "";
    private String acctNo = "";
    private String securityNo = "";
    private String slNo = "";
    private Double presentStockValue = null;
    private Double margin = null;
    private Double lastStockValue = null;
    private Double calcDrawingPower = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Double marginAmt = null;

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
     * Setter/Getter for SECURITY_NO - table Field
     */
    public void setSecurityNo(String securityNo) {
        this.securityNo = securityNo;
    }

    public String getSecurityNo() {
        return securityNo;
    }

    /**
     * Setter/Getter for SL_NO - table Field
     */
    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getSlNo() {
        return slNo;
    }

    /**
     * Setter/Getter for PRESENT_STOCK_VALUE - table Field
     */
    public void setPresentStockValue(Double presentStockValue) {
        this.presentStockValue = presentStockValue;
    }

    public Double getPresentStockValue() {
        return presentStockValue;
    }

    /**
     * Setter/Getter for MARGIN - table Field
     */
    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public Double getMargin() {
        return margin;
    }

    /**
     * Setter/Getter for LAST_STOCK_VALUE - table Field
     */
    public void setLastStockValue(Double lastStockValue) {
        this.lastStockValue = lastStockValue;
    }

    public Double getLastStockValue() {
        return lastStockValue;
    }

    /**
     * Setter/Getter for CALC_DRAWING_POWER - table Field
     */
    public void setCalcDrawingPower(Double calcDrawingPower) {
        this.calcDrawingPower = calcDrawingPower;
    }

    public Double getCalcDrawingPower() {
        return calcDrawingPower;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("borrowNo" + KEY_VAL_SEPARATOR + "securityNo" + KEY_VAL_SEPARATOR + "slNo");
        return borrowNo + KEY_VAL_SEPARATOR + securityNo + KEY_VAL_SEPARATOR + slNo;
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
        strB.append(getTOString("securityNo", securityNo));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("presentStockValue", presentStockValue));
        strB.append(getTOString("margin", margin));
        strB.append(getTOString("marginAmt", marginAmt));
        strB.append(getTOString("lastStockValue", lastStockValue));
        strB.append(getTOString("calcDrawingPower", calcDrawingPower));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
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
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("securityNo", securityNo));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("presentStockValue", presentStockValue));
        strB.append(getTOXml("margin", margin));
        strB.append(getTOXml("marginAmt", marginAmt));
        strB.append(getTOXml("lastStockValue", lastStockValue));
        strB.append(getTOXml("calcDrawingPower", calcDrawingPower));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property marginAmt.
     *
     * @return Value of property marginAmt.
     */
    public java.lang.Double getMarginAmt() {
        return marginAmt;
    }

    /**
     * Setter for property marginAmt.
     *
     * @param marginAmt New value of property marginAmt.
     */
    public void setMarginAmt(java.lang.Double marginAmt) {
        this.marginAmt = marginAmt;
    }
}