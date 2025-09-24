/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterTO.java
 * 
 * Created on Fri Aug 05 15:08:19 GMT+05:30 2011
 */
package com.see.truetransact.transferobject.share;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DRF_PRODUCT.
 */
public class ShareDividendCalculationTO extends TransferObject implements Serializable {

    private String txtDrfTransAmount = "";
    private String cboDrfTransProdID = "";
    private String status = "";
    private Date statusDate = null;
    private String statusBy = "";
    private String authorizeBy = "";
    private String authorizeStatus = null;;
    private Date authorizeDate = null;
    private Date createdDt = null;
    private String createdBy = "";
    private String branchCode = "";
    private String dividendID = "";
    private String cboShareClass = "";
    private String txtDebitGl = "";
    private String txtPayableGl = "";
    private String txtTotalAmount = "";
    private String txtDividendPercent = "";
    private String txtResolutionNo = "";
    private String txtRemarks = "";
    private Date tdtResolutionDate = null;
    private Date tdtFromPeriod = null;
    private Date tdtToPeriod = null;
    private String closedFreq= "";
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
     * Setter/Getter for STATUS_DATE - table Field
     */
    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public Date getStatusDate() {
        return statusDate;
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
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
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
     * Setter/Getter for AUTHORIZE_DATE - table Field
     */
    public void setAuthorizeDate(Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    public Date getAuthorizeDate() {
        return authorizeDate;
    }

    public String getClosedFreq() {
        return closedFreq;
    }

    public void setClosedFreq(String closedFreq) {
        this.closedFreq = closedFreq;
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
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("dividendID", dividendID));
        strB.append(getTOString("cboShareClass", cboShareClass));
        strB.append(getTOString("txtDebitGl", txtDebitGl));
        strB.append(getTOString("txtPayableGl", txtPayableGl));
        strB.append(getTOString("txtTotalAmount", txtTotalAmount));
        strB.append(getTOString("tdtFromPeriod", tdtFromPeriod));
        strB.append(getTOString("tdtToPeriod", tdtToPeriod));
        strB.append(getTOString("txtDividendPercent", txtDividendPercent));
        strB.append(getTOString("txtResolutionNo", txtResolutionNo));
        strB.append(getTOString("tdtResolutionDate", tdtResolutionDate));
        strB.append(getTOString("txtRemarks", txtRemarks));
        strB.append(getTOString("closedFreq", closedFreq));
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
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("dividendID", dividendID));
        strB.append(getTOXml("cboShareClass", cboShareClass));
        strB.append(getTOXml("txtDebitGl", txtDebitGl));
        strB.append(getTOXml("txtPayableGl", txtPayableGl));
        strB.append(getTOXml("txtTotalAmount", txtTotalAmount));
        strB.append(getTOXml("tdtFromPeriod", tdtFromPeriod));
        strB.append(getTOXml("tdtToPeriod", tdtToPeriod));
        strB.append(getTOXml("txtDividendPercent", txtDividendPercent));
        strB.append(getTOXml("txtResolutionNo", txtResolutionNo));
        strB.append(getTOXml("tdtResolutionDate", tdtResolutionDate));
        strB.append(getTOXml("txtRemarks", txtRemarks));
        strB.append(getTOXml("closedFreq", closedFreq));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property txtDrfTransAmount.
     *
     * @return Value of property txtDrfTransAmount.
     */
    public java.lang.String getTxtDrfTransAmount() {
        return txtDrfTransAmount;
    }

    /**
     * Setter for property txtDrfTransAmount.
     *
     * @param txtDrfTransAmount New value of property txtDrfTransAmount.
     */
    public void setTxtDrfTransAmount(java.lang.String txtDrfTransAmount) {
        this.txtDrfTransAmount = txtDrfTransAmount;
    }

    /**
     * Getter for property cboDrfTransProdID.
     *
     * @return Value of property cboDrfTransProdID.
     */
    public java.lang.String getCboDrfTransProdID() {
        return cboDrfTransProdID;
    }

    /**
     * Setter for property cboDrfTransProdID.
     *
     * @param cboDrfTransProdID New value of property cboDrfTransProdID.
     */
    public void setCboDrfTransProdID(java.lang.String cboDrfTransProdID) {
        this.cboDrfTransProdID = cboDrfTransProdID;
    }

    /**
     * Getter for property dividendID.
     *
     * @return Value of property dividendID.
     */
    public java.lang.String getDividendID() {
        return dividendID;
    }

    /**
     * Setter for property dividendID.
     *
     * @param dividendID New value of property dividendID.
     */
    public void setDividendID(java.lang.String dividendID) {
        this.dividendID = dividendID;
    }

    /**
     * Getter for property cboShareClass.
     *
     * @return Value of property cboShareClass.
     */
    public java.lang.String getCboShareClass() {
        return cboShareClass;
    }

    /**
     * Setter for property cboShareClass.
     *
     * @param cboShareClass New value of property cboShareClass.
     */
    public void setCboShareClass(java.lang.String cboShareClass) {
        this.cboShareClass = cboShareClass;
    }

    /**
     * Getter for property txtDebitGl.
     *
     * @return Value of property txtDebitGl.
     */
    public java.lang.String getTxtDebitGl() {
        return txtDebitGl;
    }

    /**
     * Setter for property txtDebitGl.
     *
     * @param txtDebitGl New value of property txtDebitGl.
     */
    public void setTxtDebitGl(java.lang.String txtDebitGl) {
        this.txtDebitGl = txtDebitGl;
    }

    /**
     * Getter for property txtPayableGl.
     *
     * @return Value of property txtPayableGl.
     */
    public java.lang.String getTxtPayableGl() {
        return txtPayableGl;
    }

    /**
     * Setter for property txtPayableGl.
     *
     * @param txtPayableGl New value of property txtPayableGl.
     */
    public void setTxtPayableGl(java.lang.String txtPayableGl) {
        this.txtPayableGl = txtPayableGl;
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
     * Getter for property txtDividendPercent.
     *
     * @return Value of property txtDividendPercent.
     */
    public java.lang.String getTxtDividendPercent() {
        return txtDividendPercent;
    }

    /**
     * Setter for property txtDividendPercent.
     *
     * @param txtDividendPercent New value of property txtDividendPercent.
     */
    public void setTxtDividendPercent(java.lang.String txtDividendPercent) {
        this.txtDividendPercent = txtDividendPercent;
    }

    /**
     * Getter for property txtResolutionNo.
     *
     * @return Value of property txtResolutionNo.
     */
    public java.lang.String getTxtResolutionNo() {
        return txtResolutionNo;
    }

    /**
     * Setter for property txtResolutionNo.
     *
     * @param txtResolutionNo New value of property txtResolutionNo.
     */
    public void setTxtResolutionNo(java.lang.String txtResolutionNo) {
        this.txtResolutionNo = txtResolutionNo;
    }

    /**
     * Getter for property txtRemarks.
     *
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }

    /**
     * Setter for property txtRemarks.
     *
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    /**
     * Getter for property tdtResolutionDate.
     *
     * @return Value of property tdtResolutionDate.
     */
    public java.util.Date getTdtResolutionDate() {
        return tdtResolutionDate;
    }

    /**
     * Setter for property tdtResolutionDate.
     *
     * @param tdtResolutionDate New value of property tdtResolutionDate.
     */
    public void setTdtResolutionDate(java.util.Date tdtResolutionDate) {
        this.tdtResolutionDate = tdtResolutionDate;
    }

    /**
     * Getter for property tdtFromPeriod.
     *
     * @return Value of property tdtFromPeriod.
     */
    public java.util.Date getTdtFromPeriod() {
        return tdtFromPeriod;
    }

    /**
     * Setter for property tdtFromPeriod.
     *
     * @param tdtFromPeriod New value of property tdtFromPeriod.
     */
    public void setTdtFromPeriod(java.util.Date tdtFromPeriod) {
        this.tdtFromPeriod = tdtFromPeriod;
    }

    /**
     * Getter for property tdtToPeriod.
     *
     * @return Value of property tdtToPeriod.
     */
    public java.util.Date getTdtToPeriod() {
        return tdtToPeriod;
    }

    /**
     * Setter for property tdtToPeriod.
     *
     * @param tdtToPeriod New value of property tdtToPeriod.
     */
    public void setTdtToPeriod(java.util.Date tdtToPeriod) {
        this.tdtToPeriod = tdtToPeriod;
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
}