/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanSecurityTO.java
 *
 * Created on Wed Mar 16 16:19:00 IST 2005
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_SECURITY_DETAILS.
 */
public class TermLoanDepositTypeTO extends TransferObject implements Serializable {

    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String borrowNo = "";
    private String branchCode = "";
    private String prodType = "";
    private String txtDepNo = "";
    private String prodId = "";
    private Date tdtDepDt = null;
    private Double txtDepAmount;
    private Date txtMaturityDt = null;
    private Double txtMaturityValue;
    private String txtRateOfInterest = "";

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
     * Setter/Getter for BORROW_NO - table Field
     */
    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNum" + KEY_VAL_SEPARATOR + "slno");
        return KEY_VAL_SEPARATOR;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));

        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("borrowNo", borrowNo));

        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("txtDepNo", txtDepNo));
        strB.append(getTOString("tdtDepDt", tdtDepDt));
        strB.append(getTOString("txtDepAmount", txtDepAmount));
        strB.append(getTOString("txtMaturityDt", txtMaturityDt));
        strB.append(getTOString("txtMaturityValue", txtMaturityValue));
        strB.append(getTOString("txtRateOfInterest", txtRateOfInterest));

        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));

        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("borrowNo", borrowNo));

        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("txtDepNo", txtDepNo));
        strB.append(getTOXml("tdtDepDt", tdtDepDt));
        strB.append(getTOXml("txtDepAmount", txtDepAmount));
        strB.append(getTOXml("txtMaturityDt", txtMaturityDt));
        strB.append(getTOXml("txtMaturityValue", txtMaturityValue));
        strB.append(getTOXml("txtRateOfInterest", txtRateOfInterest));


        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property txtJewelleryDetails.
     *
     * @return Value of property txtJewelleryDetails.
     */
    /**
     * Getter for property branchCode.
     *
     * @return Value of property branchCode.
     */
    public java.lang.String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter for property branchCode.
     *
     * @param branchCode New value of property branchCode.
     */
    public void setBranchCode(java.lang.String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * Getter for property ProdType.
     *
     * @return Value of property ProdType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property ProdType.
     *
     * @param ProdType New value of property ProdType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }

    /**
     * Getter for property txtDepNo.
     *
     * @return Value of property txtDepNo.
     */
    public java.lang.String getTxtDepNo() {
        return txtDepNo;
    }

    /**
     * Setter for property txtDepNo.
     *
     * @param txtDepNo New value of property txtDepNo.
     */
    public void setTxtDepNo(java.lang.String txtDepNo) {
        this.txtDepNo = txtDepNo;
    }

    /**
     * Getter for property ProdId.
     *
     * @return Value of property ProdId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property ProdId.
     *
     * @param ProdId New value of property ProdId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }

    /**
     * Getter for property tdtDepDt.
     *
     * @return Value of property tdtDepDt.
     */
    public java.util.Date getTdtDepDt() {
        return tdtDepDt;
    }

    /**
     * Setter for property tdtDepDt.
     *
     * @param tdtDepDt New value of property tdtDepDt.
     */
    public void setTdtDepDt(java.util.Date tdtDepDt) {
        this.tdtDepDt = tdtDepDt;
    }

    /**
     * Getter for property txtDepAmount.
     *
     * @return Value of property txtDepAmount.
     */
    public java.lang.Double getTxtDepAmount() {
        return txtDepAmount;
    }

    /**
     * Setter for property txtDepAmount.
     *
     * @param txtDepAmount New value of property txtDepAmount.
     */
    public void setTxtDepAmount(java.lang.Double txtDepAmount) {
        this.txtDepAmount = txtDepAmount;
    }

    /**
     * Getter for property txtMaturityDt.
     *
     * @return Value of property txtMaturityDt.
     */
    public java.util.Date getTxtMaturityDt() {
        return txtMaturityDt;
    }

    /**
     * Setter for property txtMaturityDt.
     *
     * @param txtMaturityDt New value of property txtMaturityDt.
     */
    public void setTxtMaturityDt(java.util.Date txtMaturityDt) {
        this.txtMaturityDt = txtMaturityDt;
    }

    /**
     * Getter for property txtMaturityValue.
     *
     * @return Value of property txtMaturityValue.
     */
    public java.lang.Double getTxtMaturityValue() {
        return txtMaturityValue;
    }

    /**
     * Setter for property txtMaturityValue.
     *
     * @param txtMaturityValue New value of property txtMaturityValue.
     */
    public void setTxtMaturityValue(java.lang.Double txtMaturityValue) {
        this.txtMaturityValue = txtMaturityValue;
    }

    /**
     * Getter for property txtRateOfInterest.
     *
     * @return Value of property txtRateOfInterest.
     */
    public java.lang.String getTxtRateOfInterest() {
        return txtRateOfInterest;
    }

    /**
     * Setter for property txtRateOfInterest.
     *
     * @param txtRateOfInterest New value of property txtRateOfInterest.
     */
    public void setTxtRateOfInterest(java.lang.String txtRateOfInterest) {
        this.txtRateOfInterest = txtRateOfInterest;
    }
}