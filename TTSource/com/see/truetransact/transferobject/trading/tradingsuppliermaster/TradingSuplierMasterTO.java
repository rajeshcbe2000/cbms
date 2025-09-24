/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingSuplierMasterTO.java
 */

package com.see.truetransact.transferobject.trading.tradingsuppliermaster;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 *
 * @author Revathi L
 */
public class TradingSuplierMasterTO extends TransferObject implements Serializable {

    private String supplierPID = "";
    private String supplierID = "";
    private String txtName = "";
    private String txtAddress = "";
    private String supplierPlace = "";
    private String supplierPost = "";
    private String txtSundryCreditors = "";
    private String txtPurchase = "";
    private String customerID = "";
    private String supplierActnum = "";
    private Date tdtDate = null;
    private String txtPhone = "";
    private String txtCSTNO = "";
    private String txtKGSTNO = "";
    private String txtTinNo = "";
    private String tin = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchCode = "";
    private String active = "";

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
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

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public Date getTdtDate() {
        return tdtDate;
    }

    public void setTdtDate(Date tdtDate) {
        this.tdtDate = tdtDate;
    }

    public String getTxtAddress() {
        return txtAddress;
    }

    public void setTxtAddress(String txtAddress) {
        this.txtAddress = txtAddress;
    }

    public String getTxtCSTNO() {
        return txtCSTNO;
    }

    public void setTxtCSTNO(String txtCSTNO) {
        this.txtCSTNO = txtCSTNO;
    }

    public String getTxtKGSTNO() {
        return txtKGSTNO;
    }

    public void setTxtKGSTNO(String txtKGSTNO) {
        this.txtKGSTNO = txtKGSTNO;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getTxtPhone() {
        return txtPhone;
    }

    public void setTxtPhone(String txtPhone) {
        this.txtPhone = txtPhone;
    }

    public String getTxtPurchase() {
        return txtPurchase;
    }

    public void setTxtPurchase(String txtPurchase) {
        this.txtPurchase = txtPurchase;
    }

    public String getTxtSundryCreditors() {
        return txtSundryCreditors;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getSupplierActnum() {
        return supplierActnum;
    }

    public void setSupplierActnum(String supplierActnum) {
        this.supplierActnum = supplierActnum;
    }

    public String getSupplierPID() {
        return supplierPID;
    }

    public void setSupplierPID(String supplierPID) {
        this.supplierPID = supplierPID;
    }

    public String getSupplierPlace() {
        return supplierPlace;
    }

    public void setSupplierPlace(String supplierPlace) {
        this.supplierPlace = supplierPlace;
    }

    public String getSupplierPost() {
        return supplierPost;
    }

    public void setSupplierPost(String supplierPost) {
        this.supplierPost = supplierPost;
    }

    public void setTxtSundryCreditors(String txtSundryCreditors) {
        this.txtSundryCreditors = txtSundryCreditors;
    }

    public String getTxtTinNo() {
        return txtTinNo;
    }

    public void setTxtTinNo(String txtTinNo) {
        this.txtTinNo = txtTinNo;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("supplierPID", supplierPID));
        strB.append(getTOString("supplierID", supplierID));
        strB.append(getTOString("txtName", txtName));
        strB.append(getTOString("txtAddress", txtAddress));
        strB.append(getTOString("txtSundryCreditors", txtSundryCreditors));
        strB.append(getTOString("txtPurchase", txtPurchase));
        strB.append(getTOString("tdtDate", tdtDate));
        strB.append(getTOString("txtPhone", txtPhone));
        strB.append(getTOString("txtCSTNO", txtCSTNO));
        strB.append(getTOString("txtKGSTNO", txtKGSTNO));
        strB.append(getTOString("txtTinNo", txtTinNo));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("supplierPlace", supplierPlace));
        strB.append(getTOString("supplierPost", supplierPost));
        strB.append(getTOString("customerID", customerID));
        strB.append(getTOString("supplierActnum", supplierActnum));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("active", active));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOString("supplierPID", supplierPID));
        strB.append(getTOXml("supplierID", supplierID));
        strB.append(getTOXml("txtName", txtName));
        strB.append(getTOXml("txtAddress", txtAddress));
        strB.append(getTOXml("txtSundryCreditors", txtSundryCreditors));
        strB.append(getTOXml("txtPurchase", txtPurchase));
        strB.append(getTOXml("tdtDate", tdtDate));
        strB.append(getTOXml("txtPhone", txtPhone));
        strB.append(getTOXml("txtCSTNO", txtCSTNO));
        strB.append(getTOXml("txtKGSTNO", txtKGSTNO));
        strB.append(getTOXml("txtTinNo", txtTinNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("supplierPlace", supplierPlace));
        strB.append(getTOXml("supplierPost", supplierPost));
        strB.append(getTOXml("customerID", customerID));
        strB.append(getTOXml("supplierActnum", supplierActnum));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("active", active));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getKeyData() {
        setKeyColumns(supplierID);
        return supplierID;
    }
}
