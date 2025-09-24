/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SupplierMasterTO.java
 *
 * Created on Fri Jun 10 17:48:05 IST 2011
 */
package com.see.truetransact.transferobject.paddyprocurement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PADDY_SALE_MASTER.
 */
public class PaddySaleMasterTO extends TransferObject implements Serializable {

    private String saleId = "";
    private String saleSlNo = "";
    private Date tdtSaleDate = null;
    private String txtBillNo = "";
    private String txtName = "";
    private String txtProductCode = "";
    private String txtProductDesc = "";
    private String txtRatePerKg = "";
    private String txtKiloGram = "";
    private String txtAmount = "";
    private String txtlAcreage = "";
    private String txtBags = "";
    private Date tdtBillDate = null;
    private String txtAddress = "";
    private String txtTotalAmount = "";
    private String authorizeStatus = null;;
    //    added before here
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorize = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchCode = "";

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
     * Setter/Getter for AUTHORIZE - table Field
     */
    public void setAuthorize(String authorize) {
        this.authorize = authorize;
    }

    public String getAuthorize() {
        return authorize;
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
        strB.append(getTOString("saleId", saleId));
        strB.append(getTOString("saleSlNo", saleSlNo));
        strB.append(getTOString("tdtSaleDate", tdtSaleDate));
        strB.append(getTOString("txtBillNo", txtBillNo));
        strB.append(getTOString("txtName", txtName));
        strB.append(getTOString("txtProductCode", txtProductCode));
        strB.append(getTOString("txtRatePerKg", txtRatePerKg));
        strB.append(getTOString("txtKiloGram", txtKiloGram));
        strB.append(getTOString("txtAmount", txtAmount));
        strB.append(getTOString("txtlAcreage", txtlAcreage));
        strB.append(getTOString("txtBags", txtBags));
        strB.append(getTOString("tdtBillDate", tdtBillDate));
        strB.append(getTOString("txtAddress", txtAddress));
        strB.append(getTOString("txtTotalAmount", txtTotalAmount));
        strB.append(getTOString("txtProductDesc", txtProductDesc));

        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorize", authorize));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("saleId", saleId));
        strB.append(getTOXml("saleSlNo", saleSlNo));
        strB.append(getTOXml("tdtSaleDate", tdtSaleDate));
        strB.append(getTOXml("txtBillNo", txtBillNo));
        strB.append(getTOXml("txtName", txtName));
        strB.append(getTOXml("txtProductCode", txtProductCode));
        strB.append(getTOXml("txtRatePerKg", txtRatePerKg));
        strB.append(getTOXml("txtKiloGram", txtKiloGram));
        strB.append(getTOXml("txtAmount", txtAmount));
        strB.append(getTOXml("txtlAcreage", txtlAcreage));
        strB.append(getTOXml("txtBags", txtBags));
        strB.append(getTOXml("tdtBillDate", tdtBillDate));
        strB.append(getTOXml("txtAddress", txtAddress));
        strB.append(getTOXml("txtTotalAmount", txtTotalAmount));
        strB.append(getTOXml("txtProductDesc", txtProductDesc));


        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorize", authorize));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property txtName.
     *
     * @return Value of property txtName.
     */
    public java.lang.String getTxtName() {
        return txtName;
    }

    /**
     * Setter for property txtName.
     *
     * @param txtName New value of property txtName.
     */
    public void setTxtName(java.lang.String txtName) {
        this.txtName = txtName;
    }

    /**
     * Getter for property txtProductCode.
     *
     * @return Value of property txtProductCode.
     */
    public java.lang.String getTxtProductCode() {
        return txtProductCode;
    }

    /**
     * Setter for property txtProductCode.
     *
     * @param txtProductCode New value of property txtProductCode.
     */
    public void setTxtProductCode(java.lang.String txtProductCode) {
        this.txtProductCode = txtProductCode;
    }

    /**
     * Getter for property txtRatePerKg.
     *
     * @return Value of property txtRatePerKg.
     */
    public java.lang.String getTxtRatePerKg() {
        return txtRatePerKg;
    }

    /**
     * Setter for property txtRatePerKg.
     *
     * @param txtRatePerKg New value of property txtRatePerKg.
     */
    public void setTxtRatePerKg(java.lang.String txtRatePerKg) {
        this.txtRatePerKg = txtRatePerKg;
    }

    /**
     * Getter for property txtKiloGram.
     *
     * @return Value of property txtKiloGram.
     */
    public java.lang.String getTxtKiloGram() {
        return txtKiloGram;
    }

    /**
     * Setter for property txtKiloGram.
     *
     * @param txtKiloGram New value of property txtKiloGram.
     */
    public void setTxtKiloGram(java.lang.String txtKiloGram) {
        this.txtKiloGram = txtKiloGram;
    }

    /**
     * Getter for property txtAmount.
     *
     * @return Value of property txtAmount.
     */
    public java.lang.String getTxtAmount() {
        return txtAmount;
    }

    /**
     * Setter for property txtAmount.
     *
     * @param txtAmount New value of property txtAmount.
     */
    public void setTxtAmount(java.lang.String txtAmount) {
        this.txtAmount = txtAmount;
    }

    /**
     * Getter for property txtlAcreage.
     *
     * @return Value of property txtlAcreage.
     */
    public java.lang.String getTxtlAcreage() {
        return txtlAcreage;
    }

    /**
     * Setter for property txtlAcreage.
     *
     * @param txtlAcreage New value of property txtlAcreage.
     */
    public void setTxtlAcreage(java.lang.String txtlAcreage) {
        this.txtlAcreage = txtlAcreage;
    }

    /**
     * Getter for property txtBags.
     *
     * @return Value of property txtBags.
     */
    public java.lang.String getTxtBags() {
        return txtBags;
    }

    /**
     * Setter for property txtBags.
     *
     * @param txtBags New value of property txtBags.
     */
    public void setTxtBags(java.lang.String txtBags) {
        this.txtBags = txtBags;
    }

    /**
     * Getter for property tdtBillDate.
     *
     * @return Value of property tdtBillDate.
     */
    public java.util.Date getTdtBillDate() {
        return tdtBillDate;
    }

    /**
     * Setter for property tdtBillDate.
     *
     * @param tdtBillDate New value of property tdtBillDate.
     */
    public void setTdtBillDate(java.util.Date tdtBillDate) {
        this.tdtBillDate = tdtBillDate;
    }

    /**
     * Getter for property txtAddress.
     *
     * @return Value of property txtAddress.
     */
    public java.lang.String getTxtAddress() {
        return txtAddress;
    }

    /**
     * Setter for property txtAddress.
     *
     * @param txtAddress New value of property txtAddress.
     */
    public void setTxtAddress(java.lang.String txtAddress) {
        this.txtAddress = txtAddress;
    }

    /**
     * Getter for property txtTotalAmount.
     *
     * @return Value of property txtTotalAmount.
     */
    public java.lang.String getTxtTotalAmount() {
        return txtTotalAmount;
    }

    /**
     * Setter for property txtTotalAmount.
     *
     * @param txtTotalAmount New value of property txtTotalAmount.
     */
    public void setTxtTotalAmount(java.lang.String txtTotalAmount) {
        this.txtTotalAmount = txtTotalAmount;
    }

    /**
     * Getter for property txtProductDesc.
     *
     * @return Value of property txtProductDesc.
     */
    public java.lang.String getTxtProductDesc() {
        return txtProductDesc;
    }

    /**
     * Setter for property txtProductDesc.
     *
     * @param txtProductDesc New value of property txtProductDesc.
     */
    public void setTxtProductDesc(java.lang.String txtProductDesc) {
        this.txtProductDesc = txtProductDesc;
    }

    /**
     * Getter for property txtBillNo.
     *
     * @return Value of property txtBillNo.
     */
    public java.lang.String getTxtBillNo() {
        return txtBillNo;
    }

    /**
     * Setter for property txtBillNo.
     *
     * @param txtBillNo New value of property txtBillNo.
     */
    public void setTxtBillNo(java.lang.String txtBillNo) {
        this.txtBillNo = txtBillNo;
    }

    /**
     * Getter for property tdtSaleDate.
     *
     * @return Value of property tdtSaleDate.
     */
    public java.util.Date getTdtSaleDate() {
        return tdtSaleDate;
    }

    /**
     * Setter for property tdtSaleDate.
     *
     * @param tdtSaleDate New value of property tdtSaleDate.
     */
    public void setTdtSaleDate(java.util.Date tdtSaleDate) {
        this.tdtSaleDate = tdtSaleDate;
    }

    /**
     * Getter for property saleSlNo.
     *
     * @return Value of property saleSlNo.
     */
    public java.lang.String getSaleSlNo() {
        return saleSlNo;
    }

    /**
     * Setter for property saleSlNo.
     *
     * @param saleSlNo New value of property saleSlNo.
     */
    public void setSaleSlNo(java.lang.String saleSlNo) {
        this.saleSlNo = saleSlNo;
    }

    /**
     * Getter for property saleId.
     *
     * @return Value of property saleId.
     */
    public java.lang.String getSaleId() {
        return saleId;
    }

    /**
     * Setter for property saleId.
     *
     * @param saleId New value of property saleId.
     */
    public void setSaleId(java.lang.String saleId) {
        this.saleId = saleId;
    }
}