/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestSubventionAdjustmentTO.java
 * 
 * Created on Thu Jun 13 17:02:12 IST 2013
 */
package com.see.truetransact.transferobject.termloan.kcctopacs;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SUBVENTION_INT_ADJUSTED.
 */
public class InterestSubventionAdjustmentTO extends TransferObject implements Serializable {

    private String adjustmentNo = "";
    private String agencyName = "";
    private String custId = "";
    private String releaseRefNo = "";
    private Date releaseDate = null;
    private Double startFinYear = null;
    private Double endFinYear = null;
    private Double claimedAmount = null;
    private Double receivedAmount = null;
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
     * Setter/Getter for AGENCY_NAME - table Field
     */
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyName() {
        return agencyName;
    }

    /**
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
    }

    /**
     * Setter/Getter for RELEASE_REF_NO - table Field
     */
    public void setReleaseRefNo(String releaseRefNo) {
        this.releaseRefNo = releaseRefNo;
    }

    public String getReleaseRefNo() {
        return releaseRefNo;
    }

    /**
     * Setter/Getter for RELEASE_DATE - table Field
     */
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * Setter/Getter for START_FIN_YEAR - table Field
     */
    public void setStartFinYear(Double startFinYear) {
        this.startFinYear = startFinYear;
    }

    public Double getStartFinYear() {
        return startFinYear;
    }

    /**
     * Setter/Getter for END_FIN_YEAR - table Field
     */
    public void setEndFinYear(Double endFinYear) {
        this.endFinYear = endFinYear;
    }

    public Double getEndFinYear() {
        return endFinYear;
    }

    /**
     * Setter/Getter for CLAIMED_AMOUNT - table Field
     */
    public void setClaimedAmount(Double claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    public Double getClaimedAmount() {
        return claimedAmount;
    }

    /**
     * Setter/Getter for RECEIVED_AMOUNT - table Field
     */
    public void setReceivedAmount(Double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public Double getReceivedAmount() {
        return receivedAmount;
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
        strB.append(getTOString("agencyName", agencyName));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("releaseRefNo", releaseRefNo));
        strB.append(getTOString("releaseDate", releaseDate));
        strB.append(getTOString("startFinYear", startFinYear));
        strB.append(getTOString("endFinYear", endFinYear));
        strB.append(getTOString("claimedAmount", claimedAmount));
        strB.append(getTOString("receivedAmount", receivedAmount));
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
        strB.append(getTOXml("agencyName", agencyName));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("releaseRefNo", releaseRefNo));
        strB.append(getTOXml("releaseDate", releaseDate));
        strB.append(getTOXml("startFinYear", startFinYear));
        strB.append(getTOXml("endFinYear", endFinYear));
        strB.append(getTOXml("claimedAmount", claimedAmount));
        strB.append(getTOXml("receivedAmount", receivedAmount));
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