/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChargesTO.java
 * 
 * Created on Thu Dec 23 15:00:55 IST 2004
 */
package com.see.truetransact.transferobject.common.charges;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CHARGES.
 */
public class ChargesTO extends TransferObject implements Serializable {

    private String chargeType = "";
    private String productId = "";
    private String productType = "";
    private Double percentage = null;
    private Double fixedRate = null;
    private Double fromAmt = null;
    private Double toAmt = null;
    private Double forEveryAmt = null;
    private Double forEveryRate = null;
    private String forEveryType = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String authorised = "";
    private String authorisedBy = "";
    private Date authorisedDate = null;
    private Date startDate = null;
    private Date endDate = null;

    /**
     * Setter/Getter for CHARGE_TYPE - table Field
     */
    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getChargeType() {
        return chargeType;
    }

    /**
     * Setter/Getter for PRODUCT_ID - table Field
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    /**
     * Setter/Getter for PRODUCT_TYPE - table Field
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    /**
     * Setter/Getter for PERCENTAGE - table Field
     */
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getPercentage() {
        return percentage;
    }

    /**
     * Setter/Getter for FIXED_RATE - table Field
     */
    public void setFixedRate(Double fixedRate) {
        this.fixedRate = fixedRate;
    }

    public Double getFixedRate() {
        return fixedRate;
    }

    /**
     * Setter/Getter for FROM_AMT - table Field
     */
    public void setFromAmt(Double fromAmt) {
        this.fromAmt = fromAmt;
    }

    public Double getFromAmt() {
        return fromAmt;
    }

    /**
     * Setter/Getter for TO_AMT - table Field
     */
    public void setToAmt(Double toAmt) {
        this.toAmt = toAmt;
    }

    public Double getToAmt() {
        return toAmt;
    }

    /**
     * Setter/Getter for FOR_EVERY_AMT - table Field
     */
    public void setForEveryAmt(Double forEveryAmt) {
        this.forEveryAmt = forEveryAmt;
    }

    public Double getForEveryAmt() {
        return forEveryAmt;
    }

    /**
     * Setter/Getter for FOR_EVERY_RATE - table Field
     */
    public void setForEveryRate(Double forEveryRate) {
        this.forEveryRate = forEveryRate;
    }

    public Double getForEveryRate() {
        return forEveryRate;
    }

    /**
     * Setter/Getter for FOR_EVERY_TYPE - table Field
     */
    public void setForEveryType(String forEveryType) {
        this.forEveryType = forEveryType;
    }

    public String getForEveryType() {
        return forEveryType;
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
     * Setter/Getter for STATUS_DATE - table Field
     */
    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    /**
     * Setter/Getter for AUTHORISED - table Field
     */
    public void setAuthorised(String authorised) {
        this.authorised = authorised;
    }

    public String getAuthorised() {
        return authorised;
    }

    /**
     * Setter/Getter for AUTHORISED_BY - table Field
     */
    public void setAuthorisedBy(String authorisedBy) {
        this.authorisedBy = authorisedBy;
    }

    public String getAuthorisedBy() {
        return authorisedBy;
    }

    /**
     * Setter/Getter for AUTHORISED_DATE - table Field
     */
    public void setAuthorisedDate(Date authorisedDate) {
        this.authorisedDate = authorisedDate;
    }

    public Date getAuthorisedDate() {
        return authorisedDate;
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
        strB.append(getTOString("chargeType", chargeType));
        strB.append(getTOString("productId", productId));
        strB.append(getTOString("productType", productType));
        strB.append(getTOString("percentage", percentage));
        strB.append(getTOString("fixedRate", fixedRate));
        strB.append(getTOString("fromAmt", fromAmt));
        strB.append(getTOString("toAmt", toAmt));
        strB.append(getTOString("forEveryAmt", forEveryAmt));
        strB.append(getTOString("forEveryRate", forEveryRate));
        strB.append(getTOString("forEveryType", forEveryType));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("authorised", authorised));
        strB.append(getTOString("authorisedBy", authorisedBy));
        strB.append(getTOString("authorisedDate", authorisedDate));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("chargeType", chargeType));
        strB.append(getTOXml("productId", productId));
        strB.append(getTOXml("productType", productType));
        strB.append(getTOXml("percentage", percentage));
        strB.append(getTOXml("fixedRate", fixedRate));
        strB.append(getTOXml("fromAmt", fromAmt));
        strB.append(getTOXml("toAmt", toAmt));
        strB.append(getTOXml("forEveryAmt", forEveryAmt));
        strB.append(getTOXml("forEveryRate", forEveryRate));
        strB.append(getTOXml("forEveryType", forEveryType));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("authorised", authorised));
        strB.append(getTOXml("authorisedBy", authorisedBy));
        strB.append(getTOXml("authorisedDate", authorisedDate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property startDate.
     *
     * @return Value of property startDate.
     */
    public java.util.Date getStartDate() {
        return startDate;
    }

    /**
     * Setter for property startDate.
     *
     * @param startDate New value of property startDate.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for property endDate.
     *
     * @return Value of property endDate.
     */
    public java.util.Date getEndDate() {
        return endDate;
    }

    /**
     * Setter for property endDate.
     *
     * @param endDate New value of property endDate.
     */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
}