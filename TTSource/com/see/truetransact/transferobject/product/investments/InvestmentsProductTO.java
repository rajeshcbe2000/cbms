/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductLoanTO.java
 *
 * Created on Tue Mar 15 14:08:12 IST 2005
 */
package com.see.truetransact.transferobject.product.investments;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_PROD_LOANS.
 */
public class InvestmentsProductTO extends TransferObject implements Serializable {

    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String cboInvestmentBehaves = "";
    private String txtProductID = "";
    private String txtDesc = "";
    private String txtInvestmentAcHead = "";
    private String txtIntReceivedAcHead = "";
    private String txtIntPaidAcHead = "";
    private String txtChargeAcHead = "";
    private String txtInterestReceivableAcHead = "";
    private String txtPremiumPaidAcHead = "";
    private String txtPremiumReceivedAcHead = "";
    private String txtPremiumDepreciationAcHead = "";
    private String txtBrokerCommissionAcHead = "";
    private String txtDividentReceivedAcHead = "";
    private String txtServiceTaxAcHead = "";
    private String txtDividentPaidAcHead = "";
    private String commond = "";
    private String authorizeStatus;
    private String authorizeBy;
    private Date authorizeDt;
    private String txtTDSAcHead = "";
    private String renewalWithoutTransaction = "N";

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
        strB.append(getTOString("cboInvestmentBehaves", cboInvestmentBehaves));
        strB.append(getTOString("txtProductID", txtProductID));
        strB.append(getTOString("renewalWithoutTransaction", renewalWithoutTransaction));
        strB.append(getTOString("txtDesc", txtDesc));
        strB.append(getTOString("txtInvestmentAcHead", txtInvestmentAcHead));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("txtIntReceivedAcHead", txtIntReceivedAcHead));
        strB.append(getTOString("txtPremiumDepreciationAcHead", txtPremiumDepreciationAcHead));
        strB.append(getTOString("txtPremiumReceivedAcHead", txtPremiumReceivedAcHead));
        strB.append(getTOString("txtPremiumPaidAcHead", txtPremiumPaidAcHead));
        strB.append(getTOString("txtBrokerCommissionAcHead", txtBrokerCommissionAcHead));
        strB.append(getTOString("txtDividentReceivedAcHead", txtDividentReceivedAcHead));
        strB.append(getTOString("txtServiceTaxAcHead", txtServiceTaxAcHead));
        strB.append(getTOString("txtDividentPaidAcHead", txtDividentPaidAcHead));
        strB.append(getTOString("txtChargeAcHead", txtChargeAcHead));
        strB.append(getTOString("txtInterestReceivableAcHead", txtInterestReceivableAcHead));
        strB.append(getTOString("commond", commond));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("txtTDSAcHead", txtTDSAcHead));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("cboInvestmentBehaves", cboInvestmentBehaves));
        strB.append(getTOXml("txtProductID", txtProductID));
        strB.append(getTOXml("renewalWithoutTransaction", renewalWithoutTransaction));
        strB.append(getTOXml("txtDesc", txtDesc));
        strB.append(getTOXml("txtInvestmentAcHead", txtInvestmentAcHead));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("txtIntReceivedAcHead", txtIntReceivedAcHead));
        strB.append(getTOXml("txtPremiumDepreciationAcHead", txtPremiumDepreciationAcHead));
        strB.append(getTOXml("txtPremiumReceivedAcHead", txtPremiumReceivedAcHead));
        strB.append(getTOXml("txtPremiumPaidAcHead", txtPremiumPaidAcHead));
        strB.append(getTOXml("txtBrokerCommissionAcHead", txtBrokerCommissionAcHead));
        strB.append(getTOXml("txtDividentReceivedAcHead", txtDividentReceivedAcHead));
        strB.append(getTOXml("txtServiceTaxAcHead", txtServiceTaxAcHead));
        strB.append(getTOXml("txtDividentPaidAcHead", txtDividentPaidAcHead));
        strB.append(getTOXml("txtChargeAcHead", txtChargeAcHead));
        strB.append(getTOXml("txtInterestReceivableAcHead", txtInterestReceivableAcHead));
        strB.append(getTOXml("commond", commond));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("txtTDSAcHead", txtTDSAcHead));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property cboInvestmentBehaves.
     *
     * @return Value of property cboInvestmentBehaves.
     */
    public java.lang.String getCboInvestmentBehaves() {
        return cboInvestmentBehaves;
    }

    /**
     * Setter for property cboInvestmentBehaves.
     *
     * @param cboInvestmentBehaves New value of property cboInvestmentBehaves.
     */
    public void setCboInvestmentBehaves(java.lang.String cboInvestmentBehaves) {
        this.cboInvestmentBehaves = cboInvestmentBehaves;
    }

    /**
     * Getter for property txtProductID.
     *
     * @return Value of property txtProductID.
     */
    public java.lang.String getTxtProductID() {
        return txtProductID;
    }

    /**
     * Setter for property txtProductID.
     *
     * @param txtProductID New value of property txtProductID.
     */
    public void setTxtProductID(java.lang.String txtProductID) {
        this.txtProductID = txtProductID;
    }

    /**
     * Getter for property txtDesc.
     *
     * @return Value of property txtDesc.
     */
    public java.lang.String getTxtDesc() {
        return txtDesc;
    }

    /**
     * Setter for property txtDesc.
     *
     * @param txtDesc New value of property txtDesc.
     */
    public void setTxtDesc(java.lang.String txtDesc) {
        this.txtDesc = txtDesc;
    }

    /**
     * Getter for property txtInvestmentAcHead.
     *
     * @return Value of property txtInvestmentAcHead.
     */
    public java.lang.String getTxtInvestmentAcHead() {
        return txtInvestmentAcHead;
    }

    /**
     * Setter for property txtInvestmentAcHead.
     *
     * @param txtInvestmentAcHead New value of property txtInvestmentAcHead.
     */
    public void setTxtInvestmentAcHead(java.lang.String txtInvestmentAcHead) {
        this.txtInvestmentAcHead = txtInvestmentAcHead;
    }

    /**
     * Getter for property txtIntReceivedAcHead.
     *
     * @return Value of property txtIntReceivedAcHead.
     */
    public java.lang.String getTxtIntReceivedAcHead() {
        return txtIntReceivedAcHead;
    }

    /**
     * Setter for property txtIntReceivedAcHead.
     *
     * @param txtIntReceivedAcHead New value of property txtIntReceivedAcHead.
     */
    public void setTxtIntReceivedAcHead(java.lang.String txtIntReceivedAcHead) {
        this.txtIntReceivedAcHead = txtIntReceivedAcHead;
    }

    /**
     * Getter for property txtIntPaidAcHead.
     *
     * @return Value of property txtIntPaidAcHead.
     */
    public java.lang.String getTxtIntPaidAcHead() {
        return txtIntPaidAcHead;
    }

    /**
     * Setter for property txtIntPaidAcHead.
     *
     * @param txtIntPaidAcHead New value of property txtIntPaidAcHead.
     */
    public void setTxtIntPaidAcHead(java.lang.String txtIntPaidAcHead) {
        this.txtIntPaidAcHead = txtIntPaidAcHead;
    }

    /**
     * Getter for property txtPremiumPaidAcHead.
     *
     * @return Value of property txtPremiumPaidAcHead.
     */
    public java.lang.String getTxtPremiumPaidAcHead() {
        return txtPremiumPaidAcHead;
    }

    /**
     * Setter for property txtPremiumPaidAcHead.
     *
     * @param txtPremiumPaidAcHead New value of property txtPremiumPaidAcHead.
     */
    public void setTxtPremiumPaidAcHead(java.lang.String txtPremiumPaidAcHead) {
        this.txtPremiumPaidAcHead = txtPremiumPaidAcHead;
    }

    /**
     * Getter for property txtPremiumDepreciationAcHead.
     *
     * @return Value of property txtPremiumDepreciationAcHead.
     */
    public java.lang.String getTxtPremiumDepreciationAcHead() {
        return txtPremiumDepreciationAcHead;
    }

    /**
     * Setter for property txtPremiumDepreciationAcHead.
     *
     * @param txtPremiumDepreciationAcHead New value of property
     * txtPremiumDepreciationAcHead.
     */
    public void setTxtPremiumDepreciationAcHead(java.lang.String txtPremiumDepreciationAcHead) {
        this.txtPremiumDepreciationAcHead = txtPremiumDepreciationAcHead;
    }

    /**
     * Getter for property txtBrokerCommissionAcHead.
     *
     * @return Value of property txtBrokerCommissionAcHead.
     */
    public java.lang.String getTxtBrokerCommissionAcHead() {
        return txtBrokerCommissionAcHead;
    }

    /**
     * Setter for property txtBrokerCommissionAcHead.
     *
     * @param txtBrokerCommissionAcHead New value of property
     * txtBrokerCommissionAcHead.
     */
    public void setTxtBrokerCommissionAcHead(java.lang.String txtBrokerCommissionAcHead) {
        this.txtBrokerCommissionAcHead = txtBrokerCommissionAcHead;
    }

    /**
     * Getter for property txtDividentReceivedAcHead.
     *
     * @return Value of property txtDividentReceivedAcHead.
     */
    public java.lang.String getTxtDividentReceivedAcHead() {
        return txtDividentReceivedAcHead;
    }

    /**
     * Setter for property txtDividentReceivedAcHead.
     *
     * @param txtDividentReceivedAcHead New value of property
     * txtDividentReceivedAcHead.
     */
    public void setTxtDividentReceivedAcHead(java.lang.String txtDividentReceivedAcHead) {
        this.txtDividentReceivedAcHead = txtDividentReceivedAcHead;
    }

    /**
     * Getter for property txtServiceTaxAcHead.
     *
     * @return Value of property txtServiceTaxAcHead.
     */
    public java.lang.String getTxtServiceTaxAcHead() {
        return txtServiceTaxAcHead;
    }

    /**
     * Setter for property txtServiceTaxAcHead.
     *
     * @param txtServiceTaxAcHead New value of property txtServiceTaxAcHead.
     */
    public void setTxtServiceTaxAcHead(java.lang.String txtServiceTaxAcHead) {
        this.txtServiceTaxAcHead = txtServiceTaxAcHead;
    }

    /**
     * Getter for property commond.
     *
     * @return Value of property commond.
     */
    public java.lang.String getCommond() {
        return commond;
    }

    /**
     * Setter for property commond.
     *
     * @param commond New value of property commond.
     */
    public void setCommond(java.lang.String commond) {
        this.commond = commond;
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
     * Getter for property txtDividentPaidAcHead.
     *
     * @return Value of property txtDividentPaidAcHead.
     */
    public java.lang.String getTxtDividentPaidAcHead() {
        return txtDividentPaidAcHead;
    }

    /**
     * Setter for property txtDividentPaidAcHead.
     *
     * @param txtDividentPaidAcHead New value of property txtDividentPaidAcHead.
     */
    public void setTxtDividentPaidAcHead(java.lang.String txtDividentPaidAcHead) {
        this.txtDividentPaidAcHead = txtDividentPaidAcHead;
    }

    /**
     * Getter for property txtPremiumReceivedAcHead.
     *
     * @return Value of property txtPremiumReceivedAcHead.
     */
    public java.lang.String getTxtPremiumReceivedAcHead() {
        return txtPremiumReceivedAcHead;
    }

    /**
     * Setter for property txtPremiumReceivedAcHead.
     *
     * @param txtPremiumReceivedAcHead New value of property
     * txtPremiumReceivedAcHead.
     */
    public void setTxtPremiumReceivedAcHead(java.lang.String txtPremiumReceivedAcHead) {
        this.txtPremiumReceivedAcHead = txtPremiumReceivedAcHead;
    }

    /**
     * Getter for property txtChargeAcHead.
     *
     * @return Value of property txtChargeAcHead.
     */
    public java.lang.String getTxtChargeAcHead() {
        return txtChargeAcHead;
    }

    /**
     * Setter for property txtChargeAcHead.
     *
     * @param txtChargeAcHead New value of property txtChargeAcHead.
     */
    public void setTxtChargeAcHead(java.lang.String txtChargeAcHead) {
        this.txtChargeAcHead = txtChargeAcHead;
    }

    /**
     * Getter for property txtInterestReceivableAcHead.
     *
     * @return Value of property txtInterestReceivableAcHead.
     */
    public java.lang.String getTxtInterestReceivableAcHead() {
        return txtInterestReceivableAcHead;
    }

    /**
     * Setter for property txtInterestReceivableAcHead.
     *
     * @param txtInterestReceivableAcHead New value of property
     * txtInterestReceivableAcHead.
     */
    public void setTxtInterestReceivableAcHead(java.lang.String txtInterestReceivableAcHead) {
        this.txtInterestReceivableAcHead = txtInterestReceivableAcHead;
    }

    public String getTxtTDSAcHead() {
        return txtTDSAcHead;
    }

    public void setTxtTDSAcHead(String txtTDSAcHead) {
        this.txtTDSAcHead = txtTDSAcHead;
    }

    public String getRenewalWithoutTransaction() {
        return renewalWithoutTransaction;
    }

    public void setRenewalWithoutTransaction(String renewalWithoutTransaction) {
        this.renewalWithoutTransaction = renewalWithoutTransaction;
    }
    
}