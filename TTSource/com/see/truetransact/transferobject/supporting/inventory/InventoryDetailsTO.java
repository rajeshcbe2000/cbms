/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryDetailsTO.java
 * 
 * Created on Thu May 05 11:38:45 IST 2005
 */
package com.see.truetransact.transferobject.supporting.inventory;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is INVENTORY_DETAILS.
 */
public class InventoryDetailsTO extends TransferObject implements Serializable {

    private String itemId = "";
    private Date transDt = null;
    private String transType = "";
    private Double bookQuantity = null;
    private Double bookSlnoFrom = null;
    private Double bookSlnoTo = null;
    private Double leavesSlnoFrom = null;
    private Double leavesSlnoTo = null;
    private String prodType = "";
    private String actNum = "";
    private String instPrefix = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String transId = "";
    private String transInId = "";
    private String bookSeriesOver = "";

    /**
     * Setter/Getter for ITEM_ID - table Field
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    /**
     * Setter/Getter for TRANS_DT - table Field
     */
    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }

    public Date getTransDt() {
        return transDt;
    }

    /**
     * Setter/Getter for TRANS_TYPE - table Field
     */
    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransType() {
        return transType;
    }

    /**
     * Setter/Getter for BOOK_QUANTITY - table Field
     */
    public void setBookQuantity(Double bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    public Double getBookQuantity() {
        return bookQuantity;
    }

    /**
     * Setter/Getter for BOOK_SLNO_FROM - table Field
     */
    public void setBookSlnoFrom(Double bookSlnoFrom) {
        this.bookSlnoFrom = bookSlnoFrom;
    }

    public Double getBookSlnoFrom() {
        return bookSlnoFrom;
    }

    /**
     * Setter/Getter for BOOK_SLNO_TO - table Field
     */
    public void setBookSlnoTo(Double bookSlnoTo) {
        this.bookSlnoTo = bookSlnoTo;
    }

    public Double getBookSlnoTo() {
        return bookSlnoTo;
    }

    /**
     * Setter/Getter for LEAVES_SLNO_FROM - table Field
     */
    public void setLeavesSlnoFrom(Double leavesSlnoFrom) {
        this.leavesSlnoFrom = leavesSlnoFrom;
    }

    public Double getLeavesSlnoFrom() {
        return leavesSlnoFrom;
    }

    /**
     * Setter/Getter for LEAVES_SLNO_TO - table Field
     */
    public void setLeavesSlnoTo(Double leavesSlnoTo) {
        this.leavesSlnoTo = leavesSlnoTo;
    }

    public Double getLeavesSlnoTo() {
        return leavesSlnoTo;
    }

    /**
     * Setter/Getter for PROD_TYPE - table Field
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
    }

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
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
     * Setter/Getter for TRANS_ID - table Field
     */
    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransId() {
        return transId;
    }

    /**
     * Setter/Getter for TRANS_IN_ID - table Field
     */
    public void setTransInId(String transInId) {
        this.transInId = transInId;
    }

    public String getTransInId() {
        return transInId;
    }

    /**
     * Setter/Getter for BOOK_SERIES_OVER - table Field
     */
    public void setBookSeriesOver(String bookSeriesOver) {
        this.bookSeriesOver = bookSeriesOver;
    }

    public String getBookSeriesOver() {
        return bookSeriesOver;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("transId");
        return transId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("itemId", itemId));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("bookQuantity", bookQuantity));
        strB.append(getTOString("bookSlnoFrom", bookSlnoFrom));
        strB.append(getTOString("bookSlnoTo", bookSlnoTo));
        strB.append(getTOString("leavesSlnoFrom", leavesSlnoFrom));
        strB.append(getTOString("leavesSlnoTo", leavesSlnoTo));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("instPrefix", instPrefix));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("transInId", transInId));
        strB.append(getTOString("bookSeriesOver", bookSeriesOver));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("itemId", itemId));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("bookQuantity", bookQuantity));
        strB.append(getTOXml("bookSlnoFrom", bookSlnoFrom));
        strB.append(getTOXml("bookSlnoTo", bookSlnoTo));
        strB.append(getTOXml("leavesSlnoFrom", leavesSlnoFrom));
        strB.append(getTOXml("leavesSlnoTo", leavesSlnoTo));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("instPrefix", instPrefix));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("transInId", transInId));
        strB.append(getTOXml("bookSeriesOver", bookSeriesOver));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property instPrefix.
     *
     * @return Value of property instPrefix.
     */
    public java.lang.String getInstPrefix() {
        return instPrefix;
    }

    /**
     * Setter for property instPrefix.
     *
     * @param instPrefix New value of property instPrefix.
     */
    public void setInstPrefix(java.lang.String instPrefix) {
        this.instPrefix = instPrefix;
    }
}