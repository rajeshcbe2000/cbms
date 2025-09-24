/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryMasterTO.java
 * 
 * Created on Fri Oct 15 09:57:09 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.supporting.inventory;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is INVENTORY_MASTER.
 */
public class InventoryMasterTO extends TransferObject implements Serializable {

    private String itemId = "";
    private String itemType = "";
    private String itemSubType = "";
    private Double availableBooks = null;
    private Double leavesPerBook = null;
    private Double booksReorderLevel = null;
    private Double booksDangerLevel = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchId = "";
    private String instrumentPrefix = "";
    private String remarks = "";

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
     * Setter/Getter for ITEM_TYPE - table Field
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemType() {
        return itemType;
    }

    /**
     * Setter/Getter for ITEM_SUB_TYPE - table Field
     */
    public void setItemSubType(String itemSubType) {
        this.itemSubType = itemSubType;
    }

    public String getItemSubType() {
        return itemSubType;
    }

    /**
     * Setter/Getter for AVAILABLE_BOOKS - table Field
     */
    public void setAvailableBooks(Double availableBooks) {
        this.availableBooks = availableBooks;
    }

    public Double getAvailableBooks() {
        return availableBooks;
    }

    /**
     * Setter/Getter for LEAVES_PER_BOOK - table Field
     */
    public void setLeavesPerBook(Double leavesPerBook) {
        this.leavesPerBook = leavesPerBook;
    }

    public Double getLeavesPerBook() {
        return leavesPerBook;
    }

    /**
     * Setter/Getter for BOOKS_REORDER_LEVEL - table Field
     */
    public void setBooksReorderLevel(Double booksReorderLevel) {
        this.booksReorderLevel = booksReorderLevel;
    }

    public Double getBooksReorderLevel() {
        return booksReorderLevel;
    }

    /**
     * Setter/Getter for BOOKS_DANGER_LEVEL - table Field
     */
    public void setBooksDangerLevel(Double booksDangerLevel) {
        this.booksDangerLevel = booksDangerLevel;
    }

    public Double getBooksDangerLevel() {
        return booksDangerLevel;
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
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    /**
     * Setter/Getter for INSTRUMENT_PREFIX - table Field
     */
    public void setInstrumentPrefix(String instrumentPrefix) {
        this.instrumentPrefix = instrumentPrefix;
    }

    public String getInstrumentPrefix() {
        return instrumentPrefix;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("itemId");
        return itemId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("itemId", itemId));
        strB.append(getTOString("itemType", itemType));
        strB.append(getTOString("itemSubType", itemSubType));
        strB.append(getTOString("availableBooks", availableBooks));
        strB.append(getTOString("leavesPerBook", leavesPerBook));
        strB.append(getTOString("booksReorderLevel", booksReorderLevel));
        strB.append(getTOString("booksDangerLevel", booksDangerLevel));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("instrumentPrefix", instrumentPrefix));
        strB.append(getTOString("remarks", remarks));
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
        strB.append(getTOXml("itemType", itemType));
        strB.append(getTOXml("itemSubType", itemSubType));
        strB.append(getTOXml("availableBooks", availableBooks));
        strB.append(getTOXml("leavesPerBook", leavesPerBook));
        strB.append(getTOXml("booksReorderLevel", booksReorderLevel));
        strB.append(getTOXml("booksDangerLevel", booksDangerLevel));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("instrumentPrefix", instrumentPrefix));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}