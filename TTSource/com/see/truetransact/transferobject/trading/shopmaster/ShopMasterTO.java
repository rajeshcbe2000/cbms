/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigTO.java
 * 
 * Created on Thu Feb 10 15:07:29 IST 2005
 */
/**
 *
 * @author Revathi L
 */
package com.see.truetransact.transferobject.trading.shopmaster;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TDS_CONFIG.
 */
public class ShopMasterTO extends TransferObject implements Serializable {

    private static final long serialVersionUID = 1480154587612063809L;
    //Added By Revathi
    private String pID = "";
    private String txtStoreID = "";
    private String txtName = "";
    private String txtPlace = "";
    private String cboBranchID = "";
    private String cboCashACHead = "";
    private String cboTransACHead = "";
    private String cboKVATACHead = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String interestRate = "";
    private String gracePreviousDays = "";
    private String tin = "";
    private String cst = "";
    private String mode = "";
    private Date opDt = null;

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
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

    public String getCboBranchID() {
        return cboBranchID;
    }

    public void setCboBranchID(String cboBranchID) {
        this.cboBranchID = cboBranchID;
    }

    public String getCboCashACHead() {
        return cboCashACHead;
    }

    public void setCboCashACHead(String cboCashACHead) {
        this.cboCashACHead = cboCashACHead;
    }

    public String getCboKVATACHead() {
        return cboKVATACHead;
    }

    public void setCboKVATACHead(String cboKVATACHead) {
        this.cboKVATACHead = cboKVATACHead;
    }

    public String getCboTransACHead() {
        return cboTransACHead;
    }

    public void setCboTransACHead(String cboTransACHead) {
        this.cboTransACHead = cboTransACHead;
    }

    public String getCst() {
        return cst;
    }

    public void setCst(String cst) {
        this.cst = cst;
    }

    public String getGracePreviousDays() {
        return gracePreviousDays;
    }

    public void setGracePreviousDays(String gracePreviousDays) {
        this.gracePreviousDays = gracePreviousDays;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Date getOpDt() {
        return opDt;
    }

    public void setOpDt(Date opDt) {
        this.opDt = opDt;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
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

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getTxtPlace() {
        return txtPlace;
    }

    public void setTxtPlace(String txtPlace) {
        this.txtPlace = txtPlace;
    }

    public String getTxtStoreID() {
        return txtStoreID;
    }

    public void setTxtStoreID(String txtStoreID) {
        this.txtStoreID = txtStoreID;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(txtStoreID);
        return txtStoreID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("txtStoreID", txtStoreID));
        strB.append(getTOString("pID", pID));
        strB.append(getTOString("txtName", txtName));
        strB.append(getTOString("txtPlace", txtPlace));
        strB.append(getTOString("cboBranchID", cboBranchID));
        strB.append(getTOString("cboCashACHead", cboCashACHead));
        strB.append(getTOString("cboTransACHead", cboTransACHead));
        strB.append(getTOString("cboKVATACHead", cboKVATACHead));
        strB.append(getTOString("interestRate", interestRate));
        strB.append(getTOString("gracePreviousDays", gracePreviousDays));
        strB.append(getTOString("tin", tin));
        strB.append(getTOString("cst", cst));
        strB.append(getTOString("mode", mode));
        strB.append(getTOString("opDt", opDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("txtStoreID", txtStoreID));
        strB.append(getTOXml("pID", pID));
        strB.append(getTOXml("txtName", txtName));
        strB.append(getTOXml("txtPlace", txtPlace));
        strB.append(getTOXml("cboBranchID", cboBranchID));
        strB.append(getTOXml("cboCashACHead", cboCashACHead));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("cboTransACHead", cboTransACHead));
        strB.append(getTOXml("cboKVATACHead", cboKVATACHead));
        strB.append(getTOXml("interestRate", interestRate));
        strB.append(getTOXml("gracePreviousDays", gracePreviousDays));
        strB.append(getTOXml("tin", tin));
        strB.append(getTOXml("cst", cst));
        strB.append(getTOXml("mode", mode));
        strB.append(getTOXml("opDt", opDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}