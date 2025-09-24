/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestSubsidyAdjustmentTO.java
 * 
 * Created on Mon Jul 08 14:00:31 IST 2013
 */
package com.see.truetransact.transferobject.termloan.kcctopacs;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SUBSIDY_INT_ADJUSTED.
 */
public class InterestSubsidyAdjustmentTO extends TransferObject implements Serializable {

    private String adjustmentNo = "";
    private String intType = "";
    private String agencyName = "";
    private String subsidyRefNo = "";
    private Date subsidyDate = null;
    private Double subsidyAmountReceived = null;
    private Double subsidyReceivedPer = null;
    private String prodType = "";
    private String prodId = "";
    private String fromAccNo = "";
    private String toAccNo = "";
    private String subsidyType = "";
    private Date fromDt = null;
    private Date toDt = null;
    private String fromReleaseNo = "";
    private String toReleaseNo = "";
    private String otsSanctionNo = "";
    private Date otsSanctionDt = null;
    private String otsSanctionBy = "";
    private Double otsAmount = null;
    private String remarks = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedStatus = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;

    /**
     * Setter/Getter for ADJUSTMENT_NO - table Field
     */
    public void setAdjustmentNo(String adjustmentNo) {
        this.adjustmentNo = adjustmentNo;
    }

    public String getAdjustmentNo() {
        return adjustmentNo;
    }

    /**
     * Setter/Getter for INT_TYPE - table Field
     */
    public void setIntType(String intType) {
        this.intType = intType;
    }

    public String getIntType() {
        return intType;
    }

    /**
     * Setter/Getter for AGENCY_NAME - table Field
     */
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyName() {
        return agencyName;
    }

    /**
     * Setter/Getter for SUBSIDY_REF_NO - table Field
     */
    public void setSubsidyRefNo(String subsidyRefNo) {
        this.subsidyRefNo = subsidyRefNo;
    }

    public String getSubsidyRefNo() {
        return subsidyRefNo;
    }

    /**
     * Setter/Getter for SUBSIDY_DATE - table Field
     */
    public void setSubsidyDate(Date subsidyDate) {
        this.subsidyDate = subsidyDate;
    }

    public Date getSubsidyDate() {
        return subsidyDate;
    }

    /**
     * Setter/Getter for SUBSIDY_AMOUNT_RECEIVED - table Field
     */
    public void setSubsidyAmountReceived(Double subsidyAmountReceived) {
        this.subsidyAmountReceived = subsidyAmountReceived;
    }

    public Double getSubsidyAmountReceived() {
        return subsidyAmountReceived;
    }

    /**
     * Setter/Getter for SUBSIDY_RECEIVED_PER - table Field
     */
    public void setSubsidyReceivedPer(Double subsidyReceivedPer) {
        this.subsidyReceivedPer = subsidyReceivedPer;
    }

    public Double getSubsidyReceivedPer() {
        return subsidyReceivedPer;
    }

    /**
     * Setter/Getter for PROD_TYPE - table Field
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
    }

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for FROM_ACC_NO - table Field
     */
    public void setFromAccNo(String fromAccNo) {
        this.fromAccNo = fromAccNo;
    }

    public String getFromAccNo() {
        return fromAccNo;
    }

    /**
     * Setter/Getter for TO_ACC_NO - table Field
     */
    public void setToAccNo(String toAccNo) {
        this.toAccNo = toAccNo;
    }

    public String getToAccNo() {
        return toAccNo;
    }

    /**
     * Setter/Getter for SUBSIDY_TYPE - table Field
     */
    public void setSubsidyType(String subsidyType) {
        this.subsidyType = subsidyType;
    }

    public String getSubsidyType() {
        return subsidyType;
    }

    /**
     * Setter/Getter for FROM_DT - table Field
     */
    public void setFromDt(Date fromDt) {
        this.fromDt = fromDt;
    }

    public Date getFromDt() {
        return fromDt;
    }

    /**
     * Setter/Getter for TO_DT - table Field
     */
    public void setToDt(Date toDt) {
        this.toDt = toDt;
    }

    public Date getToDt() {
        return toDt;
    }

    /**
     * Setter/Getter for FROM_RELEASE_NO - table Field
     */
    public void setFromReleaseNo(String fromReleaseNo) {
        this.fromReleaseNo = fromReleaseNo;
    }

    public String getFromReleaseNo() {
        return fromReleaseNo;
    }

    /**
     * Setter/Getter for TO_RELEASE_NO - table Field
     */
    public void setToReleaseNo(String toReleaseNo) {
        this.toReleaseNo = toReleaseNo;
    }

    public String getToReleaseNo() {
        return toReleaseNo;
    }

    /**
     * Setter/Getter for OTS_SANCTION_NO - table Field
     */
    public void setOtsSanctionNo(String otsSanctionNo) {
        this.otsSanctionNo = otsSanctionNo;
    }

    public String getOtsSanctionNo() {
        return otsSanctionNo;
    }

    /**
     * Setter/Getter for OTS_SANCTION_DT - table Field
     */
    public void setOtsSanctionDt(Date otsSanctionDt) {
        this.otsSanctionDt = otsSanctionDt;
    }

    public Date getOtsSanctionDt() {
        return otsSanctionDt;
    }

    /**
     * Setter/Getter for OTS_SANCTION_BY - table Field
     */
    public void setOtsSanctionBy(String otsSanctionBy) {
        this.otsSanctionBy = otsSanctionBy;
    }

    public String getOtsSanctionBy() {
        return otsSanctionBy;
    }

    /**
     * Setter/Getter for OTS_AMOUNT - table Field
     */
    public void setOtsAmount(Double otsAmount) {
        this.otsAmount = otsAmount;
    }

    public Double getOtsAmount() {
        return otsAmount;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
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
        strB.append(getTOString("adjustmentNo", adjustmentNo));
        strB.append(getTOString("intType", intType));
        strB.append(getTOString("agencyName", agencyName));
        strB.append(getTOString("subsidyRefNo", subsidyRefNo));
        strB.append(getTOString("subsidyDate", subsidyDate));
        strB.append(getTOString("subsidyAmountReceived", subsidyAmountReceived));
        strB.append(getTOString("subsidyReceivedPer", subsidyReceivedPer));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("fromAccNo", fromAccNo));
        strB.append(getTOString("toAccNo", toAccNo));
        strB.append(getTOString("subsidyType", subsidyType));
        strB.append(getTOString("fromDt", fromDt));
        strB.append(getTOString("toDt", toDt));
        strB.append(getTOString("fromReleaseNo", fromReleaseNo));
        strB.append(getTOString("toReleaseNo", toReleaseNo));
        strB.append(getTOString("otsSanctionNo", otsSanctionNo));
        strB.append(getTOString("otsSanctionDt", otsSanctionDt));
        strB.append(getTOString("otsSanctionBy", otsSanctionBy));
        strB.append(getTOString("otsAmount", otsAmount));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("adjustmentNo", adjustmentNo));
        strB.append(getTOXml("intType", intType));
        strB.append(getTOXml("agencyName", agencyName));
        strB.append(getTOXml("subsidyRefNo", subsidyRefNo));
        strB.append(getTOXml("subsidyDate", subsidyDate));
        strB.append(getTOXml("subsidyAmountReceived", subsidyAmountReceived));
        strB.append(getTOXml("subsidyReceivedPer", subsidyReceivedPer));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("fromAccNo", fromAccNo));
        strB.append(getTOXml("toAccNo", toAccNo));
        strB.append(getTOXml("subsidyType", subsidyType));
        strB.append(getTOXml("fromDt", fromDt));
        strB.append(getTOXml("toDt", toDt));
        strB.append(getTOXml("fromReleaseNo", fromReleaseNo));
        strB.append(getTOXml("toReleaseNo", toReleaseNo));
        strB.append(getTOXml("otsSanctionNo", otsSanctionNo));
        strB.append(getTOXml("otsSanctionDt", otsSanctionDt));
        strB.append(getTOXml("otsSanctionBy", otsSanctionBy));
        strB.append(getTOXml("otsAmount", otsAmount));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}