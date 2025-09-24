/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BankClearingParameterTO.java
 * 
 * Created on Wed Aug 24 15:07:39 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.clearing.banklevel;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CLEARING_BANK_PARAM.
 */
public class BankClearingParameterTO extends TransferObject implements Serializable {

    private String clearingType = "";
    private String clearingHd = "";
    private Double outwardReturnChrg = null;
    private Double inwardReturnChrg = null;
    private String clearingSuspenseHd = "";
    private String outwardReturnHd = "";
    private String inwardReturnHd = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String shortClaimHd = "";
    private String excessClaimHd = "";
    private String dayendWithoutCo = "";
    private Double ocInstumentCharges = null;
    private String outwardChargesHd = "";
    private String instrumentChargesCheck = "";

    public String getInstrumentChargesCheck() {
        return instrumentChargesCheck;
    }

    public void setInstrumentChargesCheck(String instrumentChargesCheck) {
        this.instrumentChargesCheck = instrumentChargesCheck;
    }

    public String getOutwardChargesHd() {
        return outwardChargesHd;
    }

    public void setOutwardChargesHd(String outwardChargesHd) {
        this.outwardChargesHd = outwardChargesHd;
    }

    public Double getOcInstumentCharges() {
        return ocInstumentCharges;
    }

    public void setOcInstumentCharges(Double ocInstumentCharges) {
        this.ocInstumentCharges = ocInstumentCharges;
    }

    /**
     * Setter/Getter for CLEARING_TYPE - table Field
     */
    public void setClearingType(String clearingType) {
        this.clearingType = clearingType;
    }

    public String getClearingType() {
        return clearingType;
    }

    /**
     * Setter/Getter for CLEARING_HD - table Field
     */
    public void setClearingHd(String clearingHd) {
        this.clearingHd = clearingHd;
    }

    public String getClearingHd() {
        return clearingHd;
    }

    /**
     * Setter/Getter for OUTWARD_RETURN_CHRG - table Field
     */
    public void setOutwardReturnChrg(Double outwardReturnChrg) {
        this.outwardReturnChrg = outwardReturnChrg;
    }

    public Double getOutwardReturnChrg() {
        return outwardReturnChrg;
    }

    /**
     * Setter/Getter for INWARD_RETURN_CHRG - table Field
     */
    public void setInwardReturnChrg(Double inwardReturnChrg) {
        this.inwardReturnChrg = inwardReturnChrg;
    }

    public Double getInwardReturnChrg() {
        return inwardReturnChrg;
    }

    /**
     * Setter/Getter for CLEARING_SUSPENSE_HD - table Field
     */
    public void setClearingSuspenseHd(String clearingSuspenseHd) {
        this.clearingSuspenseHd = clearingSuspenseHd;
    }

    public String getClearingSuspenseHd() {
        return clearingSuspenseHd;
    }

    /**
     * Setter/Getter for OUTWARD_RETURN_HD - table Field
     */
    public void setOutwardReturnHd(String outwardReturnHd) {
        this.outwardReturnHd = outwardReturnHd;
    }

    public String getOutwardReturnHd() {
        return outwardReturnHd;
    }

    /**
     * Setter/Getter for INWARD_RETURN_HD - table Field
     */
    public void setInwardReturnHd(String inwardReturnHd) {
        this.inwardReturnHd = inwardReturnHd;
    }

    public String getInwardReturnHd() {
        return inwardReturnHd;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter/Getter for SHORT_CLAIM_HD - table Field
     */
    public void setShortClaimHd(String shortClaimHd) {
        this.shortClaimHd = shortClaimHd;
    }

    public String getShortClaimHd() {
        return shortClaimHd;
    }

    /**
     * Setter/Getter for EXCESS_CLAIM_HD - table Field
     */
    public void setExcessClaimHd(String excessClaimHd) {
        this.excessClaimHd = excessClaimHd;
    }

    public String getExcessClaimHd() {
        return excessClaimHd;
    }

    /**
     * Setter/Getter for DAYEND_WITHOUT_CO - table Field
     */
    public void setDayendWithoutCo(String dayendWithoutCo) {
        this.dayendWithoutCo = dayendWithoutCo;
    }

    public String getDayendWithoutCo() {
        return dayendWithoutCo;
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
        strB.append(getTOString("clearingType", clearingType));
        strB.append(getTOString("clearingHd", clearingHd));
        strB.append(getTOString("outwardReturnChrg", outwardReturnChrg));
        strB.append(getTOString("inwardReturnChrg", inwardReturnChrg));
        strB.append(getTOString("clearingSuspenseHd", clearingSuspenseHd));
        strB.append(getTOString("outwardReturnHd", outwardReturnHd));
        strB.append(getTOString("inwardReturnHd", inwardReturnHd));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("shortClaimHd", shortClaimHd));
        strB.append(getTOString("excessClaimHd", excessClaimHd));
        strB.append(getTOString("dayendWithoutCo", dayendWithoutCo));
        strB.append(getTOString("ocInstumentCharges", ocInstumentCharges));
        strB.append(getTOString("outwardChargesHd", outwardChargesHd));
        strB.append(getTOString("instrumentChargesCheck", instrumentChargesCheck));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("clearingType", clearingType));
        strB.append(getTOXml("clearingHd", clearingHd));
        strB.append(getTOXml("outwardReturnChrg", outwardReturnChrg));
        strB.append(getTOXml("inwardReturnChrg", inwardReturnChrg));
        strB.append(getTOXml("clearingSuspenseHd", clearingSuspenseHd));
        strB.append(getTOXml("outwardReturnHd", outwardReturnHd));
        strB.append(getTOXml("inwardReturnHd", inwardReturnHd));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("shortClaimHd", shortClaimHd));
        strB.append(getTOXml("excessClaimHd", excessClaimHd));
        strB.append(getTOXml("dayendWithoutCo", dayendWithoutCo));
        strB.append(getTOXml("ocInstumentCharges", ocInstumentCharges));
        strB.append(getTOXml("outwardChargesHd", outwardChargesHd));
        strB.append(getTOXml("instrumentChargesCheck", instrumentChargesCheck));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}