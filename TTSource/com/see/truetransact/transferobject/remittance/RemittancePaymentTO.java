/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittancePaymentTO.java
 * 
 * Created on Thu Sep 02 11:39:55 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.remittance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is REMIT_PAYMENT.
 */
public class RemittancePaymentTO extends TransferObject implements Serializable {

    private String remitPayId = "";
    private Date remitPayDt = null;
    private String instrumentType = "";
    private String serialNo = "";
    private String address = "";
    private Double payAmt = null;
    private Double charges = null;
    private Double serviceTax = null;
    private String remarks = "";
    private String status = "";
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;
    private String branchId = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String instrumentNo1 = "";
    private String instrumentNo2 = "";
    private String payStatus = "";
    private String issueDt = "";
    private String favouring = "";
    private String draweeBank = "";
    private String draweeBranch = "";

    /**
     * Setter/Getter for REMIT_PAY_ID - table Field
     */
    public void setRemitPayId(String remitPayId) {
        this.remitPayId = remitPayId;
    }

    public String getRemitPayId() {
        return remitPayId;
    }

    /**
     * Setter/Getter for REMIT_PAY_DT - table Field
     */
    public void setRemitPayDt(Date remitPayDt) {
        this.remitPayDt = remitPayDt;
    }

    public Date getRemitPayDt() {
        return remitPayDt;
    }

    /**
     * Setter/Getter for INSTRUMENT_TYPE - table Field
     */
    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    /**
     * Setter/Getter for SERIAL_NO - table Field
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    /**
     * Setter/Getter for ADDRESS - table Field
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    /**
     * Setter/Getter for PAY_AMT - table Field
     */
    public void setPayAmt(Double payAmt) {
        this.payAmt = payAmt;
    }

    public Double getPayAmt() {
        return payAmt;
    }

    /**
     * Setter/Getter for CHARGES - table Field
     */
    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public Double getCharges() {
        return charges;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZE_USER - table Field
     */
    public void setAuthorizeUser(String authorizeUser) {
        this.authorizeUser = authorizeUser;
    }

    public String getAuthorizeUser() {
        return authorizeUser;
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
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
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
     * Setter/Getter for INSTRUMENT_NO1 - table Field
     */
    public void setInstrumentNo1(String instrumentNo1) {
        this.instrumentNo1 = instrumentNo1;
    }

    public String getInstrumentNo1() {
        return instrumentNo1;
    }

    /**
     * Setter/Getter for INSTRUMENT_NO2 - table Field
     */
    public void setInstrumentNo2(String instrumentNo2) {
        this.instrumentNo2 = instrumentNo2;
    }

    public String getInstrumentNo2() {
        return instrumentNo2;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(remitPayId);
        return remitPayId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("remitPayId", remitPayId));
        strB.append(getTOString("remitPayDt", remitPayDt));
        strB.append(getTOString("instrumentType", instrumentType));
        strB.append(getTOString("serialNo", serialNo));
        strB.append(getTOString("address", address));
        strB.append(getTOString("payAmt", payAmt));
        strB.append(getTOString("charges", charges));
        strB.append(getTOString("serviceTax", serviceTax));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("instrumentNo1", instrumentNo1));
        strB.append(getTOString("instrumentNo2", instrumentNo2));
        strB.append(getTOString("payStatus", payStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("remitPayId", remitPayId));
        strB.append(getTOXml("remitPayDt", remitPayDt));
        strB.append(getTOXml("instrumentType", instrumentType));
        strB.append(getTOXml("serialNo", serialNo));
        strB.append(getTOXml("address", address));
        strB.append(getTOXml("payAmt", payAmt));
        strB.append(getTOXml("charges", charges));
        strB.append(getTOXml("serviceTax", serviceTax));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("instrumentNo1", instrumentNo1));
        strB.append(getTOXml("instrumentNo2", instrumentNo2));
        strB.append(getTOXml("payStatus", payStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property payStatus.
     *
     * @return Value of property payStatus.
     */
    public java.lang.String getPayStatus() {
        return payStatus;
    }

    /**
     * Setter for property payStatus.
     *
     * @param payStatus New value of property payStatus.
     */
    public void setPayStatus(java.lang.String payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * Getter for property draweeBranch.
     *
     * @return Value of property draweeBranch.
     */
    public java.lang.String getDraweeBranch() {
        return draweeBranch;
    }

    /**
     * Setter for property draweeBranch.
     *
     * @param draweeBranch New value of property draweeBranch.
     */
    public void setDraweeBranch(java.lang.String draweeBranch) {
        this.draweeBranch = draweeBranch;
    }

    /**
     * Getter for property issueDt.
     *
     * @return Value of property issueDt.
     */
    public java.lang.String getIssueDt() {
        return issueDt;
    }

    /**
     * Setter for property issueDt.
     *
     * @param issueDt New value of property issueDt.
     */
    public void setIssueDt(java.lang.String issueDt) {
        this.issueDt = issueDt;
    }

    /**
     * Getter for property favouring.
     *
     * @return Value of property favouring.
     */
    public java.lang.String getFavouring() {
        return favouring;
    }

    /**
     * Setter for property favouring.
     *
     * @param favouring New value of property favouring.
     */
    public void setFavouring(java.lang.String favouring) {
        this.favouring = favouring;
    }

    /**
     * Getter for property draweeBank.
     *
     * @return Value of property draweeBank.
     */
    public java.lang.String getDraweeBank() {
        return draweeBank;
    }

    /**
     * Setter for property draweeBank.
     *
     * @param draweeBank New value of property draweeBank.
     */
    public void setDraweeBank(java.lang.String draweeBank) {
        this.draweeBank = draweeBank;
    }

    /**
     * Getter for property serviceTax.
     *
     * @return Value of property serviceTax.
     */
    public java.lang.Double getServiceTax() {
        return serviceTax;
    }

    /**
     * Setter for property serviceTax.
     *
     * @param serviceTax New value of property serviceTax.
     */
    public void setServiceTax(java.lang.Double serviceTax) {
        this.serviceTax = serviceTax;
    }
}