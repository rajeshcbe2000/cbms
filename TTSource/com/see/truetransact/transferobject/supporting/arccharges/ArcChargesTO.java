/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ArcChargesTO.java
 *
 * Created on November 20, 2012, 5:41 AM
 */
package com.see.truetransact.transferobject.supporting.arccharges;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author admin
 */
public class ArcChargesTO extends TransferObject implements Serializable {

    private String chargeId = "";
    private String chargeType = "";
    private String deductionAccu = "";
    private String chargeBase = "";
    private String fromSlabAmt = "";
    private String toSlabAmt = "";
    private String flatCharge = "";
    private String chargeRate = "";
    private String divisibleBy = "";
    private String roundOffType = "";
    private String minChargeAmount = "";
    private String maxChargeAmount = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("chargeId", chargeId));
        strB.append(getTOString("chargeType", chargeType));
        strB.append(getTOString("deductionAccu", deductionAccu));
        strB.append(getTOString("chargeBase", chargeBase));
        strB.append(getTOString("fromSlabAmt", fromSlabAmt));
        strB.append(getTOString("toSlabAmt", toSlabAmt));
        strB.append(getTOString("flatCharge", flatCharge));
        strB.append(getTOString("chargeRate", chargeRate));
        strB.append(getTOString("divisibleBy", divisibleBy));
        strB.append(getTOString("roundOffType", roundOffType));
        strB.append(getTOString("minChargeAmount", minChargeAmount));
        strB.append(getTOString("maxChargeAmount", maxChargeAmount));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("chargeId", chargeId));
        strB.append(getTOXml("chargeType", chargeType));
        strB.append(getTOXml("deductionAccu", deductionAccu));
        strB.append(getTOXml("chargeBase", chargeBase));
        strB.append(getTOXml("fromSlabAmt", fromSlabAmt));
        strB.append(getTOXml("toSlabAmt", toSlabAmt));
        strB.append(getTOXml("flatCharge", flatCharge));
        strB.append(getTOXml("chargeRate", chargeRate));
        strB.append(getTOXml("divisibleBy", divisibleBy));
        strB.append(getTOXml("roundOffType", roundOffType));
        strB.append(getTOXml("minChargeAmount", minChargeAmount));
        strB.append(getTOXml("maxChargeAmount", maxChargeAmount));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property chargeId.
     *
     * @return Value of property chargeId.
     */
    public java.lang.String getChargeId() {
        return chargeId;
    }

    /**
     * Setter for property chargeId.
     *
     * @param chargeId New value of property chargeId.
     */
    public void setChargeId(java.lang.String chargeId) {
        this.chargeId = chargeId;
    }

    /**
     * Getter for property deductionAccu.
     *
     * @return Value of property deductionAccu.
     */
    public java.lang.String getDeductionAccu() {
        return deductionAccu;
    }

    /**
     * Setter for property deductionAccu.
     *
     * @param deductionAccu New value of property deductionAccu.
     */
    public void setDeductionAccu(java.lang.String deductionAccu) {
        this.deductionAccu = deductionAccu;
    }

    /**
     * Getter for property chargeBase.
     *
     * @return Value of property chargeBase.
     */
    public java.lang.String getChargeBase() {
        return chargeBase;
    }

    /**
     * Setter for property chargeBase.
     *
     * @param chargeBase New value of property chargeBase.
     */
    public void setChargeBase(java.lang.String chargeBase) {
        this.chargeBase = chargeBase;
    }

    /**
     * Getter for property fromSlabAmt.
     *
     * @return Value of property fromSlabAmt.
     */
    public java.lang.String getFromSlabAmt() {
        return fromSlabAmt;
    }

    /**
     * Setter for property fromSlabAmt.
     *
     * @param fromSlabAmt New value of property fromSlabAmt.
     */
    public void setFromSlabAmt(java.lang.String fromSlabAmt) {
        this.fromSlabAmt = fromSlabAmt;
    }

    /**
     * Getter for property toSlabAmt.
     *
     * @return Value of property toSlabAmt.
     */
    public java.lang.String getToSlabAmt() {
        return toSlabAmt;
    }

    /**
     * Setter for property toSlabAmt.
     *
     * @param toSlabAmt New value of property toSlabAmt.
     */
    public void setToSlabAmt(java.lang.String toSlabAmt) {
        this.toSlabAmt = toSlabAmt;
    }

    /**
     * Getter for property flatCharge.
     *
     * @return Value of property flatCharge.
     */
    public java.lang.String getFlatCharge() {
        return flatCharge;
    }

    /**
     * Setter for property flatCharge.
     *
     * @param flatCharge New value of property flatCharge.
     */
    public void setFlatCharge(java.lang.String flatCharge) {
        this.flatCharge = flatCharge;
    }

    /**
     * Getter for property chargeRate.
     *
     * @return Value of property chargeRate.
     */
    public java.lang.String getChargeRate() {
        return chargeRate;
    }

    /**
     * Setter for property chargeRate.
     *
     * @param chargeRate New value of property chargeRate.
     */
    public void setChargeRate(java.lang.String chargeRate) {
        this.chargeRate = chargeRate;
    }

    /**
     * Getter for property divisibleBy.
     *
     * @return Value of property divisibleBy.
     */
    public java.lang.String getDivisibleBy() {
        return divisibleBy;
    }

    /**
     * Setter for property divisibleBy.
     *
     * @param divisibleBy New value of property divisibleBy.
     */
    public void setDivisibleBy(java.lang.String divisibleBy) {
        this.divisibleBy = divisibleBy;
    }

    /**
     * Getter for property roundOffType.
     *
     * @return Value of property roundOffType.
     */
    public java.lang.String getRoundOffType() {
        return roundOffType;
    }

    /**
     * Setter for property roundOffType.
     *
     * @param roundOffType New value of property roundOffType.
     */
    public void setRoundOffType(java.lang.String roundOffType) {
        this.roundOffType = roundOffType;
    }

    /**
     * Getter for property minChargeAmount.
     *
     * @return Value of property minChargeAmount.
     */
    public java.lang.String getMinChargeAmount() {
        return minChargeAmount;
    }

    /**
     * Setter for property minChargeAmount.
     *
     * @param minChargeAmount New value of property minChargeAmount.
     */
    public void setMinChargeAmount(java.lang.String minChargeAmount) {
        this.minChargeAmount = minChargeAmount;
    }

    /**
     * Getter for property maxChargeAmount.
     *
     * @return Value of property maxChargeAmount.
     */
    public java.lang.String getMaxChargeAmount() {
        return maxChargeAmount;
    }

    /**
     * Setter for property maxChargeAmount.
     *
     * @param maxChargeAmount New value of property maxChargeAmount.
     */
    public void setMaxChargeAmount(java.lang.String maxChargeAmount) {
        this.maxChargeAmount = maxChargeAmount;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
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
     * Getter for property chargeType.
     *
     * @return Value of property chargeType.
     */
    public java.lang.String getChargeType() {
        return chargeType;
    }

    /**
     * Setter for property chargeType.
     *
     * @param chargeType New value of property chargeType.
     */
    public void setChargeType(java.lang.String chargeType) {
        this.chargeType = chargeType;
    }
}
