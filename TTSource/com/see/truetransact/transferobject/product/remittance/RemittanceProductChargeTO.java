/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceProductChargeTO.java
 * 
 * Created on Sat Mar 12 16:22:48 IST 2005
 */
package com.see.truetransact.transferobject.product.remittance;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is REMITTANCE_PROD_CHARGES.
 */
public class RemittanceProductChargeTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String chargeType = "";
    private Double amtRangeFrom = null;
    private Double amtRangeTo = null;
    private Double charge = null;
    private String status = "";
    private String category = "";
    private String bankCode = "";
    private String branchCode = "";
    private Double forEveryAmt = null;
    private Double forEveryRate = null;
    private String forEveryType = "";
    private Double percentage = null;
    private Double serviceTax = null;
    private Double minAmt = null;
    private Double maxAmt = null;

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
     * Setter/Getter for CHARGE_TYPE - table Field
     */
    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getChargeType() {
        return chargeType;
    }

    /**
     * Setter/Getter for AMT_RANGE_FROM - table Field
     */
    public void setAmtRangeFrom(Double amtRangeFrom) {
        this.amtRangeFrom = amtRangeFrom;
    }

    public Double getAmtRangeFrom() {
        return amtRangeFrom;
    }

    /**
     * Setter/Getter for AMT_RANGE_TO - table Field
     */
    public void setAmtRangeTo(Double amtRangeTo) {
        this.amtRangeTo = amtRangeTo;
    }

    public Double getAmtRangeTo() {
        return amtRangeTo;
    }

    /**
     * Setter/Getter for CHARGE - table Field
     */
    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public Double getCharge() {
        return charge;
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
     * Setter/Getter for CATEGORY - table Field
     */
    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Setter/Getter for BANK_CODE - table Field
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    /**
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
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
     * Setter/Getter for PERCENTAGE - table Field
     */
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getPercentage() {
        return percentage;
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
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("chargeType", chargeType));
        strB.append(getTOString("amtRangeFrom", amtRangeFrom));
        strB.append(getTOString("amtRangeTo", amtRangeTo));
        strB.append(getTOString("charge", charge));
        strB.append(getTOString("status", status));
        strB.append(getTOString("category", category));
        strB.append(getTOString("bankCode", bankCode));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("forEveryAmt", forEveryAmt));
        strB.append(getTOString("forEveryRate", forEveryRate));
        strB.append(getTOString("forEveryType", forEveryType));
        strB.append(getTOString("percentage", percentage));
        strB.append(getTOString("serviceTax", serviceTax));
        strB.append(getTOString("minAmt", minAmt));
        strB.append(getTOString("maxAmt", maxAmt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("chargeType", chargeType));
        strB.append(getTOXml("amtRangeFrom", amtRangeFrom));
        strB.append(getTOXml("amtRangeTo", amtRangeTo));
        strB.append(getTOXml("charge", charge));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("category", category));
        strB.append(getTOXml("bankCode", bankCode));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("forEveryAmt", forEveryAmt));
        strB.append(getTOXml("forEveryRate", forEveryRate));
        strB.append(getTOXml("forEveryType", forEveryType));
        strB.append(getTOXml("percentage", percentage));
        strB.append(getTOXml("serviceTax", serviceTax));
        strB.append(getTOXml("minAmt", minAmt));
        strB.append(getTOXml("maxAmt", maxAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
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

    public Double getMaxAmt() {
        return maxAmt;
    }

    public void setMaxAmt(Double maxAmt) {
        this.maxAmt = maxAmt;
    }

    public Double getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(Double minAmt) {
        this.minAmt = minAmt;
    }
    
}