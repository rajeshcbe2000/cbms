/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestMaintenanceRateTO.java
 * 
 * Created on Mon Jan 17 17:18:44 IST 2005
 */
package com.see.truetransact.transferobject.termloan.kcctopacs;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_ROI_GROUP_TYPE_RATE.
 */
public class InterestSubsidyRateMaintenanceTypeTO extends TransferObject implements Serializable {

    private String roiGroupId = "";
    private String instituteName = "";
    private String refno = "";
    private Date roiDate = null;
    private Date roiEndDate = null;
    private Date receivedDate = null;
    private String subsidyORsubvention = "";
    private Double roi = null;
    private Double totalroi = null;
    private Double custroi = null;
    private String remarks = "";
    private String status = "";
    private String authorizedStatus = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private Date createDt = null;
    private Double miscInt = null;

    public Double getMiscInt() {
        return miscInt;
    }

    public void setMiscInt(Double miscInt) {
        this.miscInt = miscInt;
    }

    /**
     * Setter/Getter for CUST_ROI - table Field
     */
    public Double getCustroi() {
        return custroi;
    }

    public void setCustroi(Double custroi) {
        this.custroi = custroi;
    }

    /**
     * Setter/Getter for INSTITUTION_NAME - table Field
     */
    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    /**
     * Setter/Getter for REF_NO - table Field
     */
    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * Setter/Getter for CUST_ROI - table Field
     */
    public Double getTotalroi() {
        return totalroi;
    }

    public void setTotalroi(Double totalroi) {
        this.totalroi = totalroi;
    }

    /**
     * Setter/Getter for ROI_GROUP_ID - table Field
     */
    public void setRoiGroupId(String roiGroupId) {
        this.roiGroupId = roiGroupId;
    }

    public String getRoiGroupId() {
        return roiGroupId;
    }

    /**
     * Setter/Getter for ROI_DATE - table Field
     */
    public void setRoiDate(Date roiDate) {
        this.roiDate = roiDate;
    }

    public Date getRoiDate() {
        return roiDate;
    }

    /**
     * Setter/Getter for ROI - table Field
     */
    public void setRoi(Double roi) {
        this.roi = roi;
    }

    public Double getRoi() {
        return roi;
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
     * Setter/Getter for ROI_END_DATE - table Field
     */
    public void setRoiEndDate(Date roiEndDate) {
        this.roiEndDate = roiEndDate;
    }

    public Date getRoiEndDate() {
        return roiEndDate;
    }

    /**
     * Setter/Getter for RECEIVED_DATE - table Field
     */
    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * Setter/Getter for SUBSIDY_OR_SUBVENTION - table Field
     */
    public String getSubsidyORsubvention() {
        return subsidyORsubvention;
    }

    public void setSubsidyORsubvention(String subsidyORsubvention) {
        this.subsidyORsubvention = subsidyORsubvention;
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
        strB.append(getTOString("roiGroupId", roiGroupId));
        strB.append(getTOString("instituteName", instituteName));
        strB.append(getTOString("refno", refno));
        strB.append(getTOString("roiDate", roiDate));
        strB.append(getTOString("subsidyORsubvention", subsidyORsubvention));
        strB.append(getTOString("totalroi", totalroi));
        strB.append(getTOString("roi", roi));
        strB.append(getTOString("custroi", custroi));
        strB.append(getTOString("status", status));
        strB.append(getTOString("roiEndDate", roiEndDate));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("createDt", createDt));
        strB.append(getTOString("receivedDate", receivedDate));
        strB.append(getTOString("miscInt", miscInt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("roiGroupId", roiGroupId));
        strB.append(getTOXml("instituteName", instituteName));
        strB.append(getTOXml("refno", refno));
        strB.append(getTOXml("roiDate", roiDate));
        strB.append(getTOXml("subsidyORsubvention", subsidyORsubvention));
        strB.append(getTOXml("totalroi", totalroi));
        strB.append(getTOXml("roi", roi));
        strB.append(getTOXml("custroi", custroi));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("roiEndDate", roiEndDate));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("createDt", createDt));
        strB.append(getTOXml("receivedDate", receivedDate));
        strB.append(getTOXml("miscInt", miscInt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property authorizedStatus.
     *
     * @return Value of property authorizedStatus.
     */
    public java.lang.String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter for property authorizedStatus.
     *
     * @param authorizedStatus New value of property authorizedStatus.
     */
    public void setAuthorizedStatus(java.lang.String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    /**
     * Getter for property authorizedBy.
     *
     * @return Value of property authorizedBy.
     */
    public java.lang.String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter for property authorizedBy.
     *
     * @param authorizedBy New value of property authorizedBy.
     */
    public void setAuthorizedBy(java.lang.String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    /**
     * Getter for property authorizedDt.
     *
     * @return Value of property authorizedDt.
     */
    public java.util.Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter for property authorizedDt.
     *
     * @param authorizedDt New value of property authorizedDt.
     */
    public void setAuthorizedDt(java.util.Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    /**
     * Getter for property createDt.
     *
     * @return Value of property createDt.
     */
    public java.util.Date getCreateDt() {
        return createDt;
    }

    /**
     * Setter for property createDt.
     *
     * @param createDt New value of property createDt.
     */
    public void setCreateDt(java.util.Date createDt) {
        this.createDt = createDt;
    }
}