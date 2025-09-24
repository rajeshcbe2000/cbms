/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ThriftBenCalculationTO.java
 * 
 * Created on Fri Aug 05 15:08:19 GMT+05:30 2011
 */
package com.see.truetransact.transferobject.deposit.interestprocessing;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is THRIFTBEN_INT_CALC_MASTER
 */
public class ThriftBenCalculationTO extends TransferObject implements Serializable {

    private String status = "";
    private Date statusDate = null;
    private String statusBy = "";
    private String authorizeBy = "";
    private String authorizeStatus = null;
    private Date authorizeDate = null;
    private Date createdDt = null;
    private String createdBy = "";
    private String branchCode = "";
    private String thriftBenID = "";
    private String txtDebitGl = "";
    private String txtPayableGl = "";
    private Double txtTotalAmount = 0.0;
    private Double txtIntPercent = 0.0;
    private Double txtReservefundPercent = 0.0;
    private String txtResolutionNo = "";
    private String txtRemarks = "";
    private Date tdtResolutionDate = null;
    private Date tdtFromPeriod = null;
    private Date tdtToPeriod = null;
    private String prodId="";

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
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
        strB.append(getTOString("thriftBenID", thriftBenID));
        strB.append(getTOString("txtDebitGl", txtDebitGl));
        strB.append(getTOString("txtPayableGl", txtPayableGl));
        strB.append(getTOString("txtTotalAmount", txtTotalAmount));
        strB.append(getTOString("tdtFromPeriod", tdtFromPeriod));
        strB.append(getTOString("tdtToPeriod", tdtToPeriod));
        strB.append(getTOString("txtIntPercent", txtIntPercent));
        strB.append(getTOString("txtReservefundPercent", txtReservefundPercent));
        strB.append(getTOString("txtResolutionNo", txtResolutionNo));
        strB.append(getTOString("tdtResolutionDate", tdtResolutionDate));
        strB.append(getTOString("txtRemarks", txtRemarks));
        strB.append(getTOString("prodId", prodId));
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
        strB.append(getTOXml("thriftBenID", thriftBenID));
        strB.append(getTOXml("txtDebitGl", txtDebitGl));
        strB.append(getTOXml("txtPayableGl", txtPayableGl));
        strB.append(getTOXml("txtTotalAmount", txtTotalAmount));
        strB.append(getTOXml("tdtFromPeriod", tdtFromPeriod));
        strB.append(getTOXml("tdtToPeriod", tdtToPeriod));
        strB.append(getTOXml("txtIntPercent", txtIntPercent));
        strB.append(getTOXml("txtReservefundPercent", txtReservefundPercent));
        strB.append(getTOXml("txtResolutionNo", txtResolutionNo));
        strB.append(getTOXml("tdtResolutionDate", tdtResolutionDate));
        strB.append(getTOXml("txtRemarks", txtRemarks));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getThriftBenID() {
        return thriftBenID;
    }

    public void setThriftBenID(String thriftBenID) {
        this.thriftBenID = thriftBenID;
    }

    public Double getTxtIntPercent() {
        return txtIntPercent;
    }

    public void setTxtIntPercent(Double txtIntPercent) {
        this.txtIntPercent = txtIntPercent;
    }

    public Double getTxtReservefundPercent() {
        return txtReservefundPercent;
    }

    public void setTxtReservefundPercent(Double txtReservefundPercent) {
        this.txtReservefundPercent = txtReservefundPercent;
    }

    public Double getTxtTotalAmount() {
        return txtTotalAmount;
    }

    public void setTxtTotalAmount(Double txtTotalAmount) {
        this.txtTotalAmount = txtTotalAmount;
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