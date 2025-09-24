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
package com.see.truetransact.transferobject.deposit.interestmaintenance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_ROI_GROUP_TYPE_RATE.
 */
public class InterestMaintenanceRateTO extends TransferObject implements Serializable {

    private String roiGroupId = "";
    private String rateTypeId = "";
    private Date roiDate = null;
    private Double fromAmount = null;
    private Double toAmount = null;
    private Double fromPeriod = null;
    private Double toPeriod = null;
    private Double roi = null;
    private Double penalInt = null;
    private String status = "";
    private Date roiEndDate = null;
    private Double againstClearingInt = null;
    private Double limitAmount = null;
    private Double intExpiryLimit = null;
    private Double statementPenal = null;
    private Double odIntRate = null;
    private String authorizedStatus = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String statusBy = "";
    private String roiActiveStatus = "";
    private Date createDt = null;
    private String intType = "";
    private String instType = "";
    
    
    /**
     * Setter/Getter for INST_TYPE - table Field
     */
    public String getInstType() {
        return instType;
    }

    public void setInstType(String instType) {
        this.instType = instType;
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
     * Setter/Getter for RATE_TYPE_ID - table Field
     */
    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    public String getRateTypeId() {
        return rateTypeId;
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
     * Setter/Getter for FROM_AMOUNT - table Field
     */
    public void setFromAmount(Double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public Double getFromAmount() {
        return fromAmount;
    }

    /**
     * Setter/Getter for TO_AMOUNT - table Field
     */
    public void setToAmount(Double toAmount) {
        this.toAmount = toAmount;
    }

    public Double getToAmount() {
        return toAmount;
    }

    /**
     * Setter/Getter for FROM_PERIOD - table Field
     */
    public void setFromPeriod(Double fromPeriod) {
        this.fromPeriod = fromPeriod;
    }

    public Double getFromPeriod() {
        return fromPeriod;
    }

    /**
     * Setter/Getter for TO_PERIOD - table Field
     */
    public void setToPeriod(Double toPeriod) {
        this.toPeriod = toPeriod;
    }

    public Double getToPeriod() {
        return toPeriod;
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
     * Setter/Getter for PENAL_INT - table Field
     */
    public void setPenalInt(Double penalInt) {
        this.penalInt = penalInt;
    }

    public Double getPenalInt() {
        return penalInt;
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
     * Setter/Getter for AGAINST_CLEARING_INT - table Field
     */
    public void setAgainstClearingInt(Double againstClearingInt) {
        this.againstClearingInt = againstClearingInt;
    }

    public Double getAgainstClearingInt() {
        return againstClearingInt;
    }

    /**
     * Setter/Getter for LIMIT_AMOUNT - table Field
     */
    public void setLimitAmount(Double limitAmount) {
        this.limitAmount = limitAmount;
    }

    public Double getLimitAmount() {
        return limitAmount;
    }

    /**
     * Setter/Getter for INT_EXPIRY_LIMIT - table Field
     */
    public void setIntExpiryLimit(Double intExpiryLimit) {
        this.intExpiryLimit = intExpiryLimit;
    }

    public Double getIntExpiryLimit() {
        return intExpiryLimit;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("roiGroupId" + KEY_VAL_SEPARATOR + "rateTypeId");
        return roiGroupId + KEY_VAL_SEPARATOR + rateTypeId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("roiGroupId", roiGroupId));
        strB.append(getTOString("rateTypeId", rateTypeId));
        strB.append(getTOString("roiDate", roiDate));
        strB.append(getTOString("fromAmount", fromAmount));
        strB.append(getTOString("toAmount", toAmount));
        strB.append(getTOString("fromPeriod", fromPeriod));
        strB.append(getTOString("toPeriod", toPeriod));
        strB.append(getTOString("roi", roi));
        strB.append(getTOString("penalInt", penalInt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("roiEndDate", roiEndDate));
        strB.append(getTOString("againstClearingInt", againstClearingInt));
        strB.append(getTOString("limitAmount", limitAmount));
        strB.append(getTOString("intExpiryLimit", intExpiryLimit));
        strB.append(getTOString("statementPenal", statementPenal));
        strB.append(getTOString("odIntRate", odIntRate));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("roiActiveStatus", roiActiveStatus));
        strB.append(getTOString("createDt", createDt));
        strB.append(getTOString("intType", intType));
        strB.append(getTOString("instType", instType));
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
        strB.append(getTOXml("rateTypeId", rateTypeId));
        strB.append(getTOXml("roiDate", roiDate));
        strB.append(getTOXml("fromAmount", fromAmount));
        strB.append(getTOXml("toAmount", toAmount));
        strB.append(getTOXml("fromPeriod", fromPeriod));
        strB.append(getTOXml("toPeriod", toPeriod));
        strB.append(getTOXml("roi", roi));
        strB.append(getTOXml("penalInt", penalInt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("roiEndDate", roiEndDate));
        strB.append(getTOXml("againstClearingInt", againstClearingInt));
        strB.append(getTOXml("limitAmount", limitAmount));
        strB.append(getTOXml("intExpiryLimit", intExpiryLimit));
        strB.append(getTOXml("statementPenal", statementPenal));
        strB.append(getTOXml("odIntRate", odIntRate));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("roiActiveStatus", roiActiveStatus));
        strB.append(getTOXml("createDt", createDt));
        strB.append(getTOXml("intType", intType));
        strB.append(getTOXml("instType", instType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property statementPenal.
     *
     * @return Value of property statementPenal.
     */
    public java.lang.Double getStatementPenal() {
        return statementPenal;
    }

    /**
     * Setter for property statementPenal.
     *
     * @param statementPenal New value of property statementPenal.
     */
    public void setStatementPenal(java.lang.Double statementPenal) {
        this.statementPenal = statementPenal;
    }

    /**
     * Getter for property odIntRate.
     *
     * @return Value of property odIntRate.
     */
    public java.lang.Double getOdIntRate() {
        return odIntRate;
    }

    /**
     * Setter for property odIntRate.
     *
     * @param odIntRate New value of property odIntRate.
     */
    public void setOdIntRate(java.lang.Double odIntRate) {
        this.odIntRate = odIntRate;
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
     * Getter for property roiActiveStatus.
     *
     * @return Value of property roiActiveStatus.
     */
    public java.lang.String getRoiActiveStatus() {
        return roiActiveStatus;
    }

    /**
     * Setter for property roiActiveStatus.
     *
     * @param roiActiveStatus New value of property roiActiveStatus.
     */
    public void setRoiActiveStatus(java.lang.String roiActiveStatus) {
        this.roiActiveStatus = roiActiveStatus;
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

    /**
     * Getter for property intType.
     *
     * @return Value of property intType.
     */
    public java.lang.String getIntType() {
        return intType;
    }

    /**
     * Setter for property intType.
     *
     * @param intType New value of property intType.
     */
    public void setIntType(java.lang.String intType) {
        this.intType = intType;
    }
}