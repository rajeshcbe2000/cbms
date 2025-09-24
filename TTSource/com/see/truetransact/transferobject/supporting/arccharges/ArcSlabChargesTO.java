/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 *
 * ArcSlabChargesTO.java
 *
 * Created on November 20, 2012, 9:25 AM
 */
package com.see.truetransact.transferobject.supporting.arccharges;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author admin
 */
/**
 * Table name for this TO is LOAN_SLAB_AMOUNT_DETAILS.
 */
public class ArcSlabChargesTO extends TransferObject implements Serializable {

    private String slNo = "";
    private String chargeId = "";
    private String fromSlabAmt = "";
    private String toSlabAmt = "";
    private String chargeRate = "";
    private String divisibleBy = "";
    private String roundOffType = "";
    private String minChargeAmount = "";
    private String maxChargeAmount = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";

    /**
     * Setter/Getter for SL_NO - table Field
     */
    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getSlNo() {
        return slNo;
    }

    /**
     * Setter/Getter for CHARGE_ID - table Field
     */
    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeId() {
        return chargeId;
    }

    /**
     * Setter/Getter for FROM_SLAB_AMT - table Field
     */
    public void setFromSlabAmt(String fromSlabAmt) {
        this.fromSlabAmt = fromSlabAmt;
    }

    public String getFromSlabAmt() {
        return fromSlabAmt;
    }

    /**
     * Setter/Getter for TO_SLAB_AMT - table Field
     */
    public void setToSlabAmt(String toSlabAmt) {
        this.toSlabAmt = toSlabAmt;
    }

    public String getToSlabAmt() {
        return toSlabAmt;
    }

    /**
     * Setter/Getter for CHARGE_RATE - table Field
     */
    public void setChargeRate(String chargeRate) {
        this.chargeRate = chargeRate;
    }

    public String getChargeRate() {
        return chargeRate;
    }

    /**
     * Setter/Getter for DIVISIBLE_BY - table Field
     */
    public void setDivisibleBy(String divisibleBy) {
        this.divisibleBy = divisibleBy;
    }

    public String getDivisibleBy() {
        return divisibleBy;
    }

    /**
     * Setter/Getter for ROUND_OFF_TYPE - table Field
     */
    public void setRoundOffType(String roundOffType) {
        this.roundOffType = roundOffType;
    }

    public String getRoundOffType() {
        return roundOffType;
    }

    /**
     * Setter/Getter for MIN_CHARGE_AMOUNT - table Field
     */
    public void setMinChargeAmount(String minChargeAmount) {
        this.minChargeAmount = minChargeAmount;
    }

    public String getMinChargeAmount() {
        return minChargeAmount;
    }

    /**
     * Setter/Getter for MAX_CHARGE_AMOUNT - table Field
     */
    public void setMaxChargeAmount(String maxChargeAmount) {
        this.maxChargeAmount = maxChargeAmount;
    }

    public String getMaxChargeAmount() {
        return maxChargeAmount;
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
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
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
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("chargeId", chargeId));
        strB.append(getTOString("fromSlabAmt", fromSlabAmt));
        strB.append(getTOString("toSlabAmt", toSlabAmt));
        strB.append(getTOString("chargeRate", chargeRate));
        strB.append(getTOString("divisibleBy", divisibleBy));
        strB.append(getTOString("roundOffType", roundOffType));
        strB.append(getTOString("minChargeAmount", minChargeAmount));
        strB.append(getTOString("maxChargeAmount", maxChargeAmount));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
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
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("chargeId", chargeId));
        strB.append(getTOXml("fromSlabAmt", fromSlabAmt));
        strB.append(getTOXml("toSlabAmt", toSlabAmt));
        strB.append(getTOXml("chargeRate", chargeRate));
        strB.append(getTOXml("divisibleBy", divisibleBy));
        strB.append(getTOXml("roundOffType", roundOffType));
        strB.append(getTOXml("minChargeAmount", minChargeAmount));
        strB.append(getTOXml("maxChargeAmount", maxChargeAmount));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}
