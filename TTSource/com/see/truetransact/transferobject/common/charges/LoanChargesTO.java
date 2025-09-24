/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanChargesTO.java
 * 
 * Created on Mon Aug 29 16:02:00 IST 2011
 */
package com.see.truetransact.transferobject.common.charges;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOAN_CHARGE_DEFINITION.
 */
public class LoanChargesTO extends TransferObject implements Serializable {

    private String schemeId = "";
    private String chargeId = "";
    private String chargeDesc = "";
    private String accHead = "";
    private String mandatory = "";
    private String deductionAccu = "";
    private String chargeBase = "";
    private String fromSlabAmt = "";
    private String toSlabAmt = "";
    private Double flatCharge = 0.0;
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
    private String editable = "";
    private String isDepositOrLoan = "";
    private String dayWiseCalc = "";
    private Double prematureRate = 0.0;

    public Double getPrematureRate() {
        return prematureRate;
    }

    public void setPrematureRate(Double prematureRate) {
        this.prematureRate = prematureRate;
    }

    

    public String getDayWiseCalc() {
        return dayWiseCalc;
    }

    public void setDayWiseCalc(String dayWiseCalc) {
        this.dayWiseCalc = dayWiseCalc;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    /**
     * Setter/Getter for SCHEME_ID - table Field
     */
    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getSchemeId() {
        return schemeId;
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
     * Setter/Getter for CHARGE_DESC - table Field
     */
    public void setChargeDesc(String chargeDesc) {
        this.chargeDesc = chargeDesc;
    }

    public String getChargeDesc() {
        return chargeDesc;
    }

    /**
     * Setter/Getter for ACC_HEAD - table Field
     */
    public void setAccHead(String accHead) {
        this.accHead = accHead;
    }

    public String getAccHead() {
        return accHead;
    }

    /**
     * Setter/Getter for MANDATORY - table Field
     */
    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getMandatory() {
        return mandatory;
    }

    /**
     * Setter/Getter for DEDUCTION_ACCU - table Field
     */
    public void setDeductionAccu(String deductionAccu) {
        this.deductionAccu = deductionAccu;
    }

    public String getDeductionAccu() {
        return deductionAccu;
    }

    /**
     * Setter/Getter for CHARGE_BASE - table Field
     */
    public void setChargeBase(String chargeBase) {
        this.chargeBase = chargeBase;
    }

    public String getChargeBase() {
        return chargeBase;
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
     * Setter/Getter for FLAT_CHARGE - table Field
     */
    public Double getFlatCharge() {
        return flatCharge;
    }

    public void setFlatCharge(Double flatCharge) {
        this.flatCharge = flatCharge;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
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
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }
    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public String getIsDepositOrLoan() {
        return isDepositOrLoan;
    }

    public void setIsDepositOrLoan(String isDepositOrLoan) {
        this.isDepositOrLoan = isDepositOrLoan;
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
        strB.append(getTOString("schemeId", schemeId));
        strB.append(getTOString("chargeId", chargeId));
        strB.append(getTOString("chargeDesc", chargeDesc));
        strB.append(getTOString("accHead", accHead));
        strB.append(getTOString("mandatory", mandatory));
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
        strB.append(getTOString("editable", editable));
        strB.append(getTOString("isDepositOrLoan", isDepositOrLoan));
        strB.append(getTOString("dayWiseCalc", dayWiseCalc));
        strB.append(getTOString("prematureRate", prematureRate));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("schemeId", schemeId));
        strB.append(getTOXml("chargeId", chargeId));
        strB.append(getTOXml("chargeDesc", chargeDesc));
        strB.append(getTOXml("accHead", accHead));
        strB.append(getTOXml("mandatory", mandatory));
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
        strB.append(getTOXml("editable", editable));
        strB.append(getTOXml("isDepositOrLoan", isDepositOrLoan));
        strB.append(getTOXml("dayWiseCalc", dayWiseCalc));
        strB.append(getTOXml("prematureRate", prematureRate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}