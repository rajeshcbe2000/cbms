/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TdsDeductionTO.java
 *
 * Created on Tue Apr 13 17:43:18 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit.tds;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_TDS_DEDUCTION.
 */
public class TdsDeductionTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String depositNo = "";
    private Date depositDt = null;
    private Date maturityDt = null;
    private Double depositAmt = null;
    private String debitProdId = "";
    private String debitProdType = "";
    private String debitAcctNo = "";
    private String remarks = "";
    private String status = "";
    private String statusBy = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String tdsId = "";
    private String modified = "0";

    public String getKeyData() {
        setKeyColumns("prodId");
        return prodId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("depositDt", depositDt));
        strB.append(getTOString("maturityDt", maturityDt));
        strB.append(getTOString("depositAmt", depositAmt));
        strB.append(getTOString("debitAcctNo", debitAcctNo));
        strB.append(getTOString("debitProdId", debitProdId));
        strB.append(getTOString("debitProdType", debitProdType));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));

        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("modified", modified));


        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("depositDt", depositDt));
        strB.append(getTOXml("maturityDt", maturityDt));
        strB.append(getTOXml("depositAmt", depositAmt));
        strB.append(getTOXml("debitAcctNo", debitAcctNo));
        strB.append(getTOXml("debitProdId", debitProdId));
        strB.append(getTOXml("debitProdType", debitProdType));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("modified", modified));

        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property depositDt.
     *
     * @return Value of property depositDt.
     */
    public java.util.Date getDepositDt() {
        return depositDt;
    }

    /**
     * Setter for property depositDt.
     *
     * @param depositDt New value of property depositDt.
     */
    public void setDepositDt(java.util.Date depositDt) {
        this.depositDt = depositDt;
    }

    /**
     * Getter for property maturityDt.
     *
     * @return Value of property maturityDt.
     */
    public java.util.Date getMaturityDt() {
        return maturityDt;
    }

    /**
     * Setter for property maturityDt.
     *
     * @param maturityDt New value of property maturityDt.
     */
    public void setMaturityDt(java.util.Date maturityDt) {
        this.maturityDt = maturityDt;
    }

    /**
     * Getter for property depositAmt.
     *
     * @return Value of property depositAmt.
     */
    public java.lang.Double getDepositAmt() {
        return depositAmt;
    }

    /**
     * Setter for property depositAmt.
     *
     * @param depositAmt New value of property depositAmt.
     */
    public void setDepositAmt(java.lang.Double depositAmt) {
        this.depositAmt = depositAmt;
    }

    /**
     * Getter for property debitProdId.
     *
     * @return Value of property debitProdId.
     */
    public java.lang.String getDebitProdId() {
        return debitProdId;
    }

    /**
     * Setter for property debitProdId.
     *
     * @param debitProdId New value of property debitProdId.
     */
    public void setDebitProdId(java.lang.String debitProdId) {
        this.debitProdId = debitProdId;
    }

    /**
     * Getter for property debitProdType.
     *
     * @return Value of property debitProdType.
     */
    public java.lang.String getDebitProdType() {
        return debitProdType;
    }

    /**
     * Setter for property debitProdType.
     *
     * @param debitProdType New value of property debitProdType.
     */
    public void setDebitProdType(java.lang.String debitProdType) {
        this.debitProdType = debitProdType;
    }

    /**
     * Getter for property debitAcctNo.
     *
     * @return Value of property debitAcctNo.
     */
    public java.lang.String getDebitAcctNo() {
        return debitAcctNo;
    }

    /**
     * Setter for property debitAcctNo.
     *
     * @param debitAcctNo New value of property debitAcctNo.
     */
    public void setDebitAcctNo(java.lang.String debitAcctNo) {
        this.debitAcctNo = debitAcctNo;
    }

    /**
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
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
     * Getter for property tdsId.
     *
     * @return Value of property tdsId.
     *
     * /**
     * Getter for property statusBy.
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
     * Getter for property prodId.
     *
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     *
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }

    /**
     * Getter for property depositNo.
     *
     * @return Value of property depositNo.
     */
    public java.lang.String getDepositNo() {
        return depositNo;
    }

    /**
     * Setter for property depositNo.
     *
     * @param depositNo New value of property depositNo.
     */
    public void setDepositNo(java.lang.String depositNo) {
        this.depositNo = depositNo;
    }

    /**
     * Getter for property tdsId.
     *
     * @return Value of property tdsId.
     */
    public java.lang.String getTdsId() {
        return tdsId;
    }

    /**
     * Setter for property tdsId.
     *
     * @param tdsId New value of property tdsId.
     */
    public void setTdsId(java.lang.String tdsId) {
        this.tdsId = tdsId;
    }

    /**
     * Getter for property modified.
     *
     * @return Value of property modified.
     */
    public java.lang.String getModified() {
        return modified;
    }

    /**
     * Setter for property modified.
     *
     * @param modified New value of property modified.
     */
    public void setModified(java.lang.String modified) {
        this.modified = modified;
    }
}