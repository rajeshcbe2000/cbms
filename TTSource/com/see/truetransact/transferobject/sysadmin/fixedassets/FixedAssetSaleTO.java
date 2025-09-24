/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetsMovementTO.java
 * 
 * Created on Tue Jan 18 17:22:43 IST 2011
 */
package com.see.truetransact.transferobject.sysadmin.fixedassets;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is FIXED_ASSET_MOVEMENT.
 */
public class FixedAssetSaleTO extends TransferObject implements Serializable {

    private String saleBatchId = "";
    private String assetIdSale = "";
    private String faceValueSale = "";
    private String currentValueSale = "";
    private String saleAmount = "";
    private Date saleDate = null;
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String assetId = "";

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
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("saleBatchId", saleBatchId));
        strB.append(getTOString("assetIdSale", assetIdSale));
        strB.append(getTOString("assetId", assetId));
        strB.append(getTOString("faceValueSale", faceValueSale));
        strB.append(getTOString("currentValueSale", currentValueSale));
        strB.append(getTOString("saleAmount", saleAmount));
        strB.append(getTOString("saleDate", saleDate));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("saleBatchId", saleBatchId));
        strB.append(getTOXml("assetIdSale", assetIdSale));
        strB.append(getTOXml("assetId", assetId));
        strB.append(getTOXml("faceValueSale", faceValueSale));
        strB.append(getTOXml("currentValueSale", currentValueSale));
        strB.append(getTOXml("saleAmount", saleAmount));
        strB.append(getTOXml("saleDate", saleDate));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property faceValueSale.
     *
     * @return Value of property faceValueSale.
     */
    public java.lang.String getFaceValueSale() {
        return faceValueSale;
    }

    /**
     * Setter for property faceValueSale.
     *
     * @param faceValueSale New value of property faceValueSale.
     */
    public void setFaceValueSale(java.lang.String faceValueSale) {
        this.faceValueSale = faceValueSale;
    }

    /**
     * Getter for property currentValueSale.
     *
     * @return Value of property currentValueSale.
     */
    public java.lang.String getCurrentValueSale() {
        return currentValueSale;
    }

    /**
     * Setter for property currentValueSale.
     *
     * @param currentValueSale New value of property currentValueSale.
     */
    public void setCurrentValueSale(java.lang.String currentValueSale) {
        this.currentValueSale = currentValueSale;
    }

    /**
     * Getter for property assetIdSale.
     *
     * @return Value of property assetIdSale.
     */
    public java.lang.String getAssetIdSale() {
        return assetIdSale;
    }

    /**
     * Setter for property assetIdSale.
     *
     * @param assetIdSale New value of property assetIdSale.
     */
    public void setAssetIdSale(java.lang.String assetIdSale) {
        this.assetIdSale = assetIdSale;
    }

    /**
     * Getter for property saleAmount.
     *
     * @return Value of property saleAmount.
     */
    public java.lang.String getSaleAmount() {
        return saleAmount;
    }

    /**
     * Setter for property saleAmount.
     *
     * @param saleAmount New value of property saleAmount.
     */
    public void setSaleAmount(java.lang.String saleAmount) {
        this.saleAmount = saleAmount;
    }

    /**
     * Getter for property saleDate.
     *
     * @return Value of property saleDate.
     */
    public java.util.Date getSaleDate() {
        return saleDate;
    }

    /**
     * Setter for property saleDate.
     *
     * @param saleDate New value of property saleDate.
     */
    public void setSaleDate(java.util.Date saleDate) {
        this.saleDate = saleDate;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property createdDt.
     *
     * @param createdDt New value of property createdDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property saleBatchId.
     *
     * @return Value of property saleBatchId.
     */
    public java.lang.String getSaleBatchId() {
        return saleBatchId;
    }

    /**
     * Setter for property saleBatchId.
     *
     * @param saleBatchId New value of property saleBatchId.
     */
    public void setSaleBatchId(java.lang.String saleBatchId) {
        this.saleBatchId = saleBatchId;
    }

    /**
     * Getter for property assetId.
     *
     * @return Value of property assetId.
     */
    public java.lang.String getAssetId() {
        return assetId;
    }

    /**
     * Setter for property assetId.
     *
     * @param assetId New value of property assetId.
     */
    public void setAssetId(java.lang.String assetId) {
        this.assetId = assetId;
    }
}