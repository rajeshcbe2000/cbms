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
 * Table name for this TO is SUPPPLIER_MASTER.
 */
public class PaddyItemOpeningStockTO extends TransferObject implements Serializable {

    private String txtItemCode = "";
    private String txtItemDesc = "";
    private String txtPurchasePrice = "";
    private String txtSellingPrice = "";
    private String txtQty = "";
    private String txtOrderLevel = "";
    private String txtPurchaseAcHd = "";
    private String txtPurchaseReturnAcHd = "";
    private String txtSalesAcHd = "";
    private String txtTaxAcHd = "";
    private String cboUnit = "";
    private String txtSalesReturnAcHd = "";
    private String authorizeStatus = null;;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorize = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;

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
        strB.append(getTOString("txtItemCode", txtItemCode));
        strB.append(getTOString("txtItemDesc", txtItemDesc));
        strB.append(getTOString("txtPurchasePrice", txtPurchasePrice));
        strB.append(getTOString("txtSellingPrice", txtSellingPrice));
        strB.append(getTOString("txtQty", txtQty));
        strB.append(getTOString("txtOrderLevel", txtOrderLevel));
        strB.append(getTOString("txtPurchaseAcHd", txtPurchaseAcHd));
        strB.append(getTOString("txtPurchaseReturnAcHd", txtPurchaseReturnAcHd));
        strB.append(getTOString("txtSalesAcHd", txtSalesAcHd));
        strB.append(getTOString("txtSalesReturnAcHd", txtSalesReturnAcHd));
        strB.append(getTOString("txtTaxAcHd", txtTaxAcHd));
        strB.append(getTOString("cboUnit", cboUnit));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorize", authorize));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("txtItemCode", txtItemCode));
        strB.append(getTOXml("txtItemDesc", txtItemDesc));
        strB.append(getTOXml("txtPurchasePrice", txtPurchasePrice));
        strB.append(getTOXml("txtSellingPrice", txtSellingPrice));
        strB.append(getTOXml("txtQty", txtQty));
        strB.append(getTOXml("txtOrderLevel", txtOrderLevel));
        strB.append(getTOXml("txtPurchaseAcHd", txtPurchaseAcHd));
        strB.append(getTOXml("txtPurchaseReturnAcHd", txtPurchaseReturnAcHd));
        strB.append(getTOXml("txtSalesAcHd", txtSalesAcHd));
        strB.append(getTOXml("txtSalesReturnAcHd", txtSalesReturnAcHd));
        strB.append(getTOXml("txtTaxAcHd", txtTaxAcHd));
        strB.append(getTOXml("cboUnit", cboUnit));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorize", authorize));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property txtItemCode.
     *
     * @return Value of property txtItemCode.
     */
    public java.lang.String getTxtItemCode() {
        return txtItemCode;
    }

    /**
     * Setter for property txtItemCode.
     *
     * @param txtItemCode New value of property txtItemCode.
     */
    public void setTxtItemCode(java.lang.String txtItemCode) {
        this.txtItemCode = txtItemCode;
    }

    /**
     * Getter for property txtItemDesc.
     *
     * @return Value of property txtItemDesc.
     */
    public java.lang.String getTxtItemDesc() {
        return txtItemDesc;
    }

    /**
     * Setter for property txtItemDesc.
     *
     * @param txtItemDesc New value of property txtItemDesc.
     */
    public void setTxtItemDesc(java.lang.String txtItemDesc) {
        this.txtItemDesc = txtItemDesc;
    }

    /**
     * Getter for property txtPurchasePrice.
     *
     * @return Value of property txtPurchasePrice.
     */
    public java.lang.String getTxtPurchasePrice() {
        return txtPurchasePrice;
    }

    /**
     * Setter for property txtPurchasePrice.
     *
     * @param txtPurchasePrice New value of property txtPurchasePrice.
     */
    public void setTxtPurchasePrice(java.lang.String txtPurchasePrice) {
        this.txtPurchasePrice = txtPurchasePrice;
    }

    /**
     * Getter for property txtSellingPrice.
     *
     * @return Value of property txtSellingPrice.
     */
    public java.lang.String getTxtSellingPrice() {
        return txtSellingPrice;
    }

    /**
     * Setter for property txtSellingPrice.
     *
     * @param txtSellingPrice New value of property txtSellingPrice.
     */
    public void setTxtSellingPrice(java.lang.String txtSellingPrice) {
        this.txtSellingPrice = txtSellingPrice;
    }

    /**
     * Getter for property txtQty.
     *
     * @return Value of property txtQty.
     */
    public java.lang.String getTxtQty() {
        return txtQty;
    }

    /**
     * Setter for property txtQty.
     *
     * @param txtQty New value of property txtQty.
     */
    public void setTxtQty(java.lang.String txtQty) {
        this.txtQty = txtQty;
    }

    /**
     * Getter for property txtOrderLevel.
     *
     * @return Value of property txtOrderLevel.
     */
    public java.lang.String getTxtOrderLevel() {
        return txtOrderLevel;
    }

    /**
     * Setter for property txtOrderLevel.
     *
     * @param txtOrderLevel New value of property txtOrderLevel.
     */
    public void setTxtOrderLevel(java.lang.String txtOrderLevel) {
        this.txtOrderLevel = txtOrderLevel;
    }

    /**
     * Getter for property txtPurchaseAcHd.
     *
     * @return Value of property txtPurchaseAcHd.
     */
    public java.lang.String getTxtPurchaseAcHd() {
        return txtPurchaseAcHd;
    }

    /**
     * Setter for property txtPurchaseAcHd.
     *
     * @param txtPurchaseAcHd New value of property txtPurchaseAcHd.
     */
    public void setTxtPurchaseAcHd(java.lang.String txtPurchaseAcHd) {
        this.txtPurchaseAcHd = txtPurchaseAcHd;
    }

    /**
     * Getter for property txtPurchaseReturnAcHd.
     *
     * @return Value of property txtPurchaseReturnAcHd.
     */
    public java.lang.String getTxtPurchaseReturnAcHd() {
        return txtPurchaseReturnAcHd;
    }

    /**
     * Setter for property txtPurchaseReturnAcHd.
     *
     * @param txtPurchaseReturnAcHd New value of property txtPurchaseReturnAcHd.
     */
    public void setTxtPurchaseReturnAcHd(java.lang.String txtPurchaseReturnAcHd) {
        this.txtPurchaseReturnAcHd = txtPurchaseReturnAcHd;
    }

    /**
     * Getter for property txtSalesAcHd.
     *
     * @return Value of property txtSalesAcHd.
     */
    public java.lang.String getTxtSalesAcHd() {
        return txtSalesAcHd;
    }

    /**
     * Setter for property txtSalesAcHd.
     *
     * @param txtSalesAcHd New value of property txtSalesAcHd.
     */
    public void setTxtSalesAcHd(java.lang.String txtSalesAcHd) {
        this.txtSalesAcHd = txtSalesAcHd;
    }

    /**
     * Getter for property txtTaxAcHd.
     *
     * @return Value of property txtTaxAcHd.
     */
    public java.lang.String getTxtTaxAcHd() {
        return txtTaxAcHd;
    }

    /**
     * Setter for property txtTaxAcHd.
     *
     * @param txtTaxAcHd New value of property txtTaxAcHd.
     */
    public void setTxtTaxAcHd(java.lang.String txtTaxAcHd) {
        this.txtTaxAcHd = txtTaxAcHd;
    }

    /**
     * Getter for property cboUnit.
     *
     * @return Value of property cboUnit.
     */
    public java.lang.String getCboUnit() {
        return cboUnit;
    }

    /**
     * Setter for property cboUnit.
     *
     * @param cboUnit New value of property cboUnit.
     */
    public void setCboUnit(java.lang.String cboUnit) {
        this.cboUnit = cboUnit;
    }

    /**
     * Getter for property txtSalesReturnAcHd.
     *
     * @return Value of property txtSalesReturnAcHd.
     */
    public java.lang.String getTxtSalesReturnAcHd() {
        return txtSalesReturnAcHd;
    }

    /**
     * Setter for property txtSalesReturnAcHd.
     *
     * @param txtSalesReturnAcHd New value of property txtSalesReturnAcHd.
     */
    public void setTxtSalesReturnAcHd(java.lang.String txtSalesReturnAcHd) {
        this.txtSalesReturnAcHd = txtSalesReturnAcHd;
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
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * Getter for property authorize.
     *
     * @return Value of property authorize.
     */
    public java.lang.String getAuthorize() {
        return authorize;
    }

    /**
     * Setter for property authorize.
     *
     * @param authorize New value of property authorize.
     */
    public void setAuthorize(java.lang.String authorize) {
        this.authorize = authorize;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }
}