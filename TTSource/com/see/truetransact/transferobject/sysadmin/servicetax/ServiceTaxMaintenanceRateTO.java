/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ServiceTaxMaintenanceRateTO.java
 * 
 * Created on Mon Jan 17 17:18:44 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.servicetax;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_ROI_GROUP_TYPE_RATE.
 */
public class ServiceTaxMaintenanceRateTO extends TransferObject implements Serializable {

    private String roiGroupId = "";
    private String rateTypeId = "";
    private Date roiDate = null;
    private Double fromAmount = null;
    private Double toAmount = null;
    private Double fromPeriod = null;
    private Double toPeriod = null;
    private Double serviceTax = null;
    private Double cess1Tax = null;
    private String status = "";
    private Date roiEndDate = null;
    private Double againstClearingInt = null;
    private Double limitAmount = null;
    private Double intExpiryLimit = null;
    private Double totTax = null;
    private Double cess2Tax = null;
    private String authorizedStatus = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String statusBy = "";
//        private String txtServiceTax = "";
//        private String txtCess1Tax= "";
//        private String txtCess2Tax= "";
//        private String txtTotTax = "";

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
        strB.append(getTOString("serviceTax", serviceTax));
        strB.append(getTOString("cess1Tax", cess1Tax));
        strB.append(getTOString("status", status));
        strB.append(getTOString("roiEndDate", roiEndDate));
        strB.append(getTOString("againstClearingInt", againstClearingInt));
        strB.append(getTOString("limitAmount", limitAmount));
        strB.append(getTOString("intExpiryLimit", intExpiryLimit));
        strB.append(getTOString("totTax", totTax));
        strB.append(getTOString("cess2Tax", cess2Tax));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("statusBy", statusBy));
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
        strB.append(getTOXml("serviceTax", serviceTax));
        strB.append(getTOXml("cess1Tax", cess1Tax));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("roiEndDate", roiEndDate));
        strB.append(getTOXml("againstClearingInt", againstClearingInt));
        strB.append(getTOXml("limitAmount", limitAmount));
        strB.append(getTOXml("intExpiryLimit", intExpiryLimit));
        strB.append(getTOXml("totTax", totTax));
        strB.append(getTOXml("cess2Tax", cess2Tax));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property txtTotTax.
     *
     * @return Value of property txtTotTax.
     */
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

    /**
     * Getter for property cess1Tax.
     *
     * @return Value of property cess1Tax.
     */
    public java.lang.Double getCess1Tax() {
        return cess1Tax;
    }

    /**
     * Setter for property cess1Tax.
     *
     * @param cess1Tax New value of property cess1Tax.
     */
    public void setCess1Tax(java.lang.Double cess1Tax) {
        this.cess1Tax = cess1Tax;
    }

    /**
     * Getter for property totTax.
     *
     * @return Value of property totTax.
     */
    public java.lang.Double getTotTax() {
        return totTax;
    }

    /**
     * Setter for property totTax.
     *
     * @param totTax New value of property totTax.
     */
    public void setTotTax(java.lang.Double totTax) {
        this.totTax = totTax;
    }

    /**
     * Getter for property cess2Tax.
     *
     * @return Value of property cess2Tax.
     */
    public java.lang.Double getCess2Tax() {
        return cess2Tax;
    }

    /**
     * Setter for property cess2Tax.
     *
     * @param cess2Tax New value of property cess2Tax.
     */
    public void setCess2Tax(java.lang.Double cess2Tax) {
        this.cess2Tax = cess2Tax;
    }
}