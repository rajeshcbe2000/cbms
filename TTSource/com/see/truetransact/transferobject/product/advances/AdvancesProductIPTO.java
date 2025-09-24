/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AdvancesProductIPTO.java
 * 
 * Created on Tue Apr 12 09:59:12 IST 2005
 */
package com.see.truetransact.transferobject.product.advances;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ADVANCES_PROD_INTPAY.
 */
public class AdvancesProductIPTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String crIntPaid = "";
    private Double applCrintRate = null;
    private Double crIntCalcfreq = null;
    private Double crIntApplfreq = null;
    private Date lastIntcalcDtcr = null;
    private Date lastIntapplDtcr = null;
    private String crComp = "";
    private Double crIntCompfreq = null;
    private String crProdRoundoff = "";
    private String crIntRoundoff = "";
    private String calcCriteria = "";
    private Double prodFreq = null;
    private String addiIntStaff = "";
    private Double addiIntStaffPer = null;
    private Double creditMinInterestAmt = null;

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
     * Setter/Getter for CR_INT_PAID - table Field
     */
    public void setCrIntPaid(String crIntPaid) {
        this.crIntPaid = crIntPaid;
    }

    public String getCrIntPaid() {
        return crIntPaid;
    }

    /**
     * Setter/Getter for APPL_CRINT_RATE - table Field
     */
    public void setApplCrintRate(Double applCrintRate) {
        this.applCrintRate = applCrintRate;
    }

    public Double getApplCrintRate() {
        return applCrintRate;
    }

    /**
     * Setter/Getter for CR_INT_CALCFREQ - table Field
     */
    public void setCrIntCalcfreq(Double crIntCalcfreq) {
        this.crIntCalcfreq = crIntCalcfreq;
    }

    public Double getCrIntCalcfreq() {
        return crIntCalcfreq;
    }

    /**
     * Setter/Getter for CR_INT_APPLFREQ - table Field
     */
    public void setCrIntApplfreq(Double crIntApplfreq) {
        this.crIntApplfreq = crIntApplfreq;
    }

    public Double getCrIntApplfreq() {
        return crIntApplfreq;
    }

    /**
     * Setter/Getter for LAST_INTCALC_DTCR - table Field
     */
    public void setLastIntcalcDtcr(Date lastIntcalcDtcr) {
        this.lastIntcalcDtcr = lastIntcalcDtcr;
    }

    public Date getLastIntcalcDtcr() {
        return lastIntcalcDtcr;
    }

    /**
     * Setter/Getter for LAST_INTAPPL_DTCR - table Field
     */
    public void setLastIntapplDtcr(Date lastIntapplDtcr) {
        this.lastIntapplDtcr = lastIntapplDtcr;
    }

    public Date getLastIntapplDtcr() {
        return lastIntapplDtcr;
    }

    /**
     * Setter/Getter for CR_COMP - table Field
     */
    public void setCrComp(String crComp) {
        this.crComp = crComp;
    }

    public String getCrComp() {
        return crComp;
    }

    /**
     * Setter/Getter for CR_INT_COMPFREQ - table Field
     */
    public void setCrIntCompfreq(Double crIntCompfreq) {
        this.crIntCompfreq = crIntCompfreq;
    }

    public Double getCrIntCompfreq() {
        return crIntCompfreq;
    }

    /**
     * Setter/Getter for CR_PROD_ROUNDOFF - table Field
     */
    public void setCrProdRoundoff(String crProdRoundoff) {
        this.crProdRoundoff = crProdRoundoff;
    }

    public String getCrProdRoundoff() {
        return crProdRoundoff;
    }

    /**
     * Setter/Getter for CR_INT_ROUNDOFF - table Field
     */
    public void setCrIntRoundoff(String crIntRoundoff) {
        this.crIntRoundoff = crIntRoundoff;
    }

    public String getCrIntRoundoff() {
        return crIntRoundoff;
    }

    /**
     * Setter/Getter for CALC_CRITERIA - table Field
     */
    public void setCalcCriteria(String calcCriteria) {
        this.calcCriteria = calcCriteria;
    }

    public String getCalcCriteria() {
        return calcCriteria;
    }

    /**
     * Setter/Getter for PROD_FREQ - table Field
     */
    public void setProdFreq(Double prodFreq) {
        this.prodFreq = prodFreq;
    }

    public Double getProdFreq() {
        return prodFreq;
    }

    /**
     * Setter/Getter for ADDI_INT_STAFF - table Field
     */
    public void setAddiIntStaff(String addiIntStaff) {
        this.addiIntStaff = addiIntStaff;
    }

    public String getAddiIntStaff() {
        return addiIntStaff;
    }

    /**
     * Setter/Getter for ADDI_INT_STAFF_PER - table Field
     */
    public void setAddiIntStaffPer(Double addiIntStaffPer) {
        this.addiIntStaffPer = addiIntStaffPer;
    }

    public Double getAddiIntStaffPer() {
        return addiIntStaffPer;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId");
        return prodId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("crIntPaid", crIntPaid));
        strB.append(getTOString("applCrintRate", applCrintRate));
        strB.append(getTOString("crIntCalcfreq", crIntCalcfreq));
        strB.append(getTOString("crIntApplfreq", crIntApplfreq));
        strB.append(getTOString("lastIntcalcDtcr", lastIntcalcDtcr));
        strB.append(getTOString("lastIntapplDtcr", lastIntapplDtcr));
        strB.append(getTOString("crComp", crComp));
        strB.append(getTOString("crIntCompfreq", crIntCompfreq));
        strB.append(getTOString("crProdRoundoff", crProdRoundoff));
        strB.append(getTOString("crIntRoundoff", crIntRoundoff));
        strB.append(getTOString("calcCriteria", calcCriteria));
        strB.append(getTOString("prodFreq", prodFreq));
        strB.append(getTOString("addiIntStaff", addiIntStaff));
        strB.append(getTOString("addiIntStaffPer", addiIntStaffPer));
        strB.append(getTOString("creditMinInterestAmt", creditMinInterestAmt));
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
        strB.append(getTOXml("crIntPaid", crIntPaid));
        strB.append(getTOXml("applCrintRate", applCrintRate));
        strB.append(getTOXml("crIntCalcfreq", crIntCalcfreq));
        strB.append(getTOXml("crIntApplfreq", crIntApplfreq));
        strB.append(getTOXml("lastIntcalcDtcr", lastIntcalcDtcr));
        strB.append(getTOXml("lastIntapplDtcr", lastIntapplDtcr));
        strB.append(getTOXml("crComp", crComp));
        strB.append(getTOXml("crIntCompfreq", crIntCompfreq));
        strB.append(getTOXml("crProdRoundoff", crProdRoundoff));
        strB.append(getTOXml("crIntRoundoff", crIntRoundoff));
        strB.append(getTOXml("calcCriteria", calcCriteria));
        strB.append(getTOXml("prodFreq", prodFreq));
        strB.append(getTOXml("addiIntStaff", addiIntStaff));
        strB.append(getTOXml("addiIntStaffPer", addiIntStaffPer));
        strB.append(getTOXml("creditMinInterestAmt", creditMinInterestAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property creditMinInterestAmt.
     *
     * @return Value of property creditMinInterestAmt.
     */
    public java.lang.Double getCreditMinInterestAmt() {
        return creditMinInterestAmt;
    }

    /**
     * Setter for property creditMinInterestAmt.
     *
     * @param creditMinInterestAmt New value of property creditMinInterestAmt.
     */
    public void setCreditMinInterestAmt(java.lang.Double creditMinInterestAmt) {
        this.creditMinInterestAmt = creditMinInterestAmt;
    }
}