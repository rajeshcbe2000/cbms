/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductInterReceivableTO.java
 * 
 * Created on Fri May 14 16:04:38 IST 2004
 */
package com.see.truetransact.transferobject.product.loan.agriculturecard;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_INTREC.
 */
public class AgriLoanProductInterReceivableTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String debitIntCharged = "";
    private Double minDebitintRate = null;
    private Double maxDebitintRate = null;
    private Double minDebitintAmt = null;
    private Double maxDebitintAmt = null;
    private Double debitintCalcFreq = null;
    private Double debitintApplFreq = null;
    private Double debitintCompFreq = null;
    private String debitProdRoundoff = "";
    private String debitIntRoundoff = "";
    private Date lastIntcalcDtdebit = null;
    private Date lastIntapplDtdebit = null;
    private Double prodFreq = null;
    private String plrRateAppl = "";
    private Double plrRate = null;
    private Date plrApplFrom = null;
    private String plrApplNewac = "";
    private String plrApplExistac = "";
    private Date plrApplSancfrom = null;
    private String penalAppl = "";
    private String limitExpiryInt = "";
    private Double penalIntRate = null;
    private Double expoLmtPrudentialamt = null;
    private Double expoLmtPolicyamt = null;
    private Double expoLmtPrudentialper = null;
    private Double expoLmtPolicyper = null;
    private String calendarFrequency_Yes = "";
    private String asAndWhenCustomer_Yes = "";
    private String principalDue = "";
    private String interestDue = "";
    private Double penalExceptionAmt = null;

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
     * Setter/Getter for DEBIT_INT_CHARGED - table Field
     */
    public void setDebitIntCharged(String debitIntCharged) {
        this.debitIntCharged = debitIntCharged;
    }

    public String getDebitIntCharged() {
        return debitIntCharged;
    }

    /**
     * Setter/Getter for MIN_DEBITINT_RATE - table Field
     */
    public void setMinDebitintRate(Double minDebitintRate) {
        this.minDebitintRate = minDebitintRate;
    }

    public Double getMinDebitintRate() {
        return minDebitintRate;
    }

    /**
     * Setter/Getter for MAX_DEBITINT_RATE - table Field
     */
    public void setMaxDebitintRate(Double maxDebitintRate) {
        this.maxDebitintRate = maxDebitintRate;
    }

    public Double getMaxDebitintRate() {
        return maxDebitintRate;
    }

    /**
     * Setter/Getter for MIN_DEBITINT_AMT - table Field
     */
    public void setMinDebitintAmt(Double minDebitintAmt) {
        this.minDebitintAmt = minDebitintAmt;
    }

    public Double getMinDebitintAmt() {
        return minDebitintAmt;
    }

    /**
     * Setter/Getter for MAX_DEBITINT_AMT - table Field
     */
    public void setMaxDebitintAmt(Double maxDebitintAmt) {
        this.maxDebitintAmt = maxDebitintAmt;
    }

    public Double getMaxDebitintAmt() {
        return maxDebitintAmt;
    }

    /**
     * Setter/Getter for DEBITINT_CALC_FREQ - table Field
     */
    public void setDebitintCalcFreq(Double debitintCalcFreq) {
        this.debitintCalcFreq = debitintCalcFreq;
    }

    public Double getDebitintCalcFreq() {
        return debitintCalcFreq;
    }

    /**
     * Setter/Getter for DEBITINT_APPL_FREQ - table Field
     */
    public void setDebitintApplFreq(Double debitintApplFreq) {
        this.debitintApplFreq = debitintApplFreq;
    }

    public Double getDebitintApplFreq() {
        return debitintApplFreq;
    }

    /**
     * Setter/Getter for DEBITINT_COMP_FREQ - table Field
     */
    public void setDebitintCompFreq(Double debitintCompFreq) {
        this.debitintCompFreq = debitintCompFreq;
    }

    public Double getDebitintCompFreq() {
        return debitintCompFreq;
    }

    /**
     * Setter/Getter for DEBIT_PROD_ROUNDOFF - table Field
     */
    public void setDebitProdRoundoff(String debitProdRoundoff) {
        this.debitProdRoundoff = debitProdRoundoff;
    }

    public String getDebitProdRoundoff() {
        return debitProdRoundoff;
    }

    /**
     * Setter/Getter for DEBIT_INT_ROUNDOFF - table Field
     */
    public void setDebitIntRoundoff(String debitIntRoundoff) {
        this.debitIntRoundoff = debitIntRoundoff;
    }

    public String getDebitIntRoundoff() {
        return debitIntRoundoff;
    }

    /**
     * Setter/Getter for LAST_INTCALC_DTDEBIT - table Field
     */
    public void setLastIntcalcDtdebit(Date lastIntcalcDtdebit) {
        this.lastIntcalcDtdebit = lastIntcalcDtdebit;
    }

    public Date getLastIntcalcDtdebit() {
        return lastIntcalcDtdebit;
    }

    /**
     * Setter/Getter for LAST_INTAPPL_DTDEBIT - table Field
     */
    public void setLastIntapplDtdebit(Date lastIntapplDtdebit) {
        this.lastIntapplDtdebit = lastIntapplDtdebit;
    }

    public Date getLastIntapplDtdebit() {
        return lastIntapplDtdebit;
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
     * Setter/Getter for PLR_RATE_APPL - table Field
     */
    public void setPlrRateAppl(String plrRateAppl) {
        this.plrRateAppl = plrRateAppl;
    }

    public String getPlrRateAppl() {
        return plrRateAppl;
    }

    /**
     * Setter/Getter for PLR_RATE - table Field
     */
    public void setPlrRate(Double plrRate) {
        this.plrRate = plrRate;
    }

    public Double getPlrRate() {
        return plrRate;
    }

    /**
     * Setter/Getter for PLR_APPL_FROM - table Field
     */
    public void setPlrApplFrom(Date plrApplFrom) {
        this.plrApplFrom = plrApplFrom;
    }

    public Date getPlrApplFrom() {
        return plrApplFrom;
    }

    /**
     * Setter/Getter for PLR_APPL_NEWAC - table Field
     */
    public void setPlrApplNewac(String plrApplNewac) {
        this.plrApplNewac = plrApplNewac;
    }

    public String getPlrApplNewac() {
        return plrApplNewac;
    }

    /**
     * Setter/Getter for PLR_APPL_EXISTAC - table Field
     */
    public void setPlrApplExistac(String plrApplExistac) {
        this.plrApplExistac = plrApplExistac;
    }

    public String getPlrApplExistac() {
        return plrApplExistac;
    }

    /**
     * Setter/Getter for PLR_APPL_SANCFROM - table Field
     */
    public void setPlrApplSancfrom(Date plrApplSancfrom) {
        this.plrApplSancfrom = plrApplSancfrom;
    }

    public Date getPlrApplSancfrom() {
        return plrApplSancfrom;
    }

    /**
     * Setter/Getter for PENAL_APPL - table Field
     */
    public void setPenalAppl(String penalAppl) {
        this.penalAppl = penalAppl;
    }

    public String getPenalAppl() {
        return penalAppl;
    }

    /**
     * Setter/Getter for LIMIT_EXPIRY_INT - table Field
     */
    public void setLimitExpiryInt(String limitExpiryInt) {
        this.limitExpiryInt = limitExpiryInt;
    }

    public String getLimitExpiryInt() {
        return limitExpiryInt;
    }

    /**
     * Setter/Getter for PENAL_INT_RATE - table Field
     */
    public void setPenalIntRate(Double penalIntRate) {
        this.penalIntRate = penalIntRate;
    }

    public Double getPenalIntRate() {
        return penalIntRate;
    }

    /**
     * Setter/Getter for EXPO_LMT_PRUDENTIALAMT - table Field
     */
    public void setExpoLmtPrudentialamt(Double expoLmtPrudentialamt) {
        this.expoLmtPrudentialamt = expoLmtPrudentialamt;
    }

    public Double getExpoLmtPrudentialamt() {
        return expoLmtPrudentialamt;
    }

    /**
     * Setter/Getter for EXPO_LMT_POLICYAMT - table Field
     */
    public void setExpoLmtPolicyamt(Double expoLmtPolicyamt) {
        this.expoLmtPolicyamt = expoLmtPolicyamt;
    }

    public Double getExpoLmtPolicyamt() {
        return expoLmtPolicyamt;
    }

    /**
     * Setter/Getter for EXPO_LMT_PRUDENTIALPER - table Field
     */
    public void setExpoLmtPrudentialper(Double expoLmtPrudentialper) {
        this.expoLmtPrudentialper = expoLmtPrudentialper;
    }

    public Double getExpoLmtPrudentialper() {
        return expoLmtPrudentialper;
    }

    /**
     * Setter/Getter for EXPO_LMT_POLICYPER - table Field
     */
    public void setExpoLmtPolicyper(Double expoLmtPolicyper) {
        this.expoLmtPolicyper = expoLmtPolicyper;
    }

    public Double getExpoLmtPolicyper() {
        return expoLmtPolicyper;
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
        strB.append(getTOString("debitIntCharged", debitIntCharged));
        strB.append(getTOString("minDebitintRate", minDebitintRate));
        strB.append(getTOString("maxDebitintRate", maxDebitintRate));
        strB.append(getTOString("minDebitintAmt", minDebitintAmt));
        strB.append(getTOString("maxDebitintAmt", maxDebitintAmt));
        strB.append(getTOString("debitintCalcFreq", debitintCalcFreq));
        strB.append(getTOString("debitintApplFreq", debitintApplFreq));
        strB.append(getTOString("debitintCompFreq", debitintCompFreq));
        strB.append(getTOString("debitProdRoundoff", debitProdRoundoff));
        strB.append(getTOString("debitIntRoundoff", debitIntRoundoff));
        strB.append(getTOString("lastIntcalcDtdebit", lastIntcalcDtdebit));
        strB.append(getTOString("lastIntapplDtdebit", lastIntapplDtdebit));
        strB.append(getTOString("prodFreq", prodFreq));
        strB.append(getTOString("plrRateAppl", plrRateAppl));
        strB.append(getTOString("plrRate", plrRate));
        strB.append(getTOString("plrApplFrom", plrApplFrom));
        strB.append(getTOString("plrApplNewac", plrApplNewac));
        strB.append(getTOString("plrApplExistac", plrApplExistac));
        strB.append(getTOString("plrApplSancfrom", plrApplSancfrom));
        strB.append(getTOString("penalAppl", penalAppl));
        strB.append(getTOString("limitExpiryInt", limitExpiryInt));
        strB.append(getTOString("penalIntRate", penalIntRate));
        strB.append(getTOString("expoLmtPrudentialamt", expoLmtPrudentialamt));
        strB.append(getTOString("expoLmtPolicyamt", expoLmtPolicyamt));
        strB.append(getTOString("expoLmtPrudentialper", expoLmtPrudentialper));
        strB.append(getTOString("expoLmtPolicyper", expoLmtPolicyper));
        strB.append(getTOString("calendarFrequency_Yes", calendarFrequency_Yes));
        strB.append(getTOString("asAndWhenCustomer_Yes", asAndWhenCustomer_Yes));
        strB.append(getTOString("principalDue", principalDue));
        strB.append(getTOString("interestDue", interestDue));
        strB.append(getTOString("penalExceptionAmt", penalExceptionAmt));
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
        strB.append(getTOXml("debitIntCharged", debitIntCharged));
        strB.append(getTOXml("minDebitintRate", minDebitintRate));
        strB.append(getTOXml("maxDebitintRate", maxDebitintRate));
        strB.append(getTOXml("minDebitintAmt", minDebitintAmt));
        strB.append(getTOXml("maxDebitintAmt", maxDebitintAmt));
        strB.append(getTOXml("debitintCalcFreq", debitintCalcFreq));
        strB.append(getTOXml("debitintApplFreq", debitintApplFreq));
        strB.append(getTOXml("debitintCompFreq", debitintCompFreq));
        strB.append(getTOXml("debitProdRoundoff", debitProdRoundoff));
        strB.append(getTOXml("debitIntRoundoff", debitIntRoundoff));
        strB.append(getTOXml("lastIntcalcDtdebit", lastIntcalcDtdebit));
        strB.append(getTOXml("lastIntapplDtdebit", lastIntapplDtdebit));
        strB.append(getTOXml("prodFreq", prodFreq));
        strB.append(getTOXml("plrRateAppl", plrRateAppl));
        strB.append(getTOXml("plrRate", plrRate));
        strB.append(getTOXml("plrApplFrom", plrApplFrom));
        strB.append(getTOXml("plrApplNewac", plrApplNewac));
        strB.append(getTOXml("plrApplExistac", plrApplExistac));
        strB.append(getTOXml("plrApplSancfrom", plrApplSancfrom));
        strB.append(getTOXml("penalAppl", penalAppl));
        strB.append(getTOXml("limitExpiryInt", limitExpiryInt));
        strB.append(getTOXml("penalIntRate", penalIntRate));
        strB.append(getTOXml("expoLmtPrudentialamt", expoLmtPrudentialamt));
        strB.append(getTOXml("expoLmtPolicyamt", expoLmtPolicyamt));
        strB.append(getTOXml("expoLmtPrudentialper", expoLmtPrudentialper));
        strB.append(getTOXml("expoLmtPolicyper", expoLmtPolicyper));
        strB.append(getTOXml("calendarFrequency_Yes", calendarFrequency_Yes));
        strB.append(getTOXml("asAndWhenCustomer_Yes", asAndWhenCustomer_Yes));
        strB.append(getTOXml("principalDue", principalDue));
        strB.append(getTOXml("interestDue", interestDue));
        strB.append(getTOXml("penalExceptionAmt", penalExceptionAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property calendarFrequency_Yes.
     *
     * @return Value of property calendarFrequency_Yes.
     */
    public java.lang.String getCalendarFrequency_Yes() {
        return calendarFrequency_Yes;
    }

    /**
     * Setter for property calendarFrequency_Yes.
     *
     * @param calendarFrequency_Yes New value of property calendarFrequency_Yes.
     */
    public void setCalendarFrequency_Yes(java.lang.String calendarFrequency_Yes) {
        this.calendarFrequency_Yes = calendarFrequency_Yes;
    }

    /**
     * Getter for property asAndWhenCustomer_Yes.
     *
     * @return Value of property asAndWhenCustomer_Yes.
     */
    public java.lang.String getAsAndWhenCustomer_Yes() {
        return asAndWhenCustomer_Yes;
    }

    /**
     * Setter for property asAndWhenCustomer_Yes.
     *
     * @param asAndWhenCustomer_Yes New value of property asAndWhenCustomer_Yes.
     */
    public void setAsAndWhenCustomer_Yes(java.lang.String asAndWhenCustomer_Yes) {
        this.asAndWhenCustomer_Yes = asAndWhenCustomer_Yes;
    }

    /**
     * Getter for property principalDue.
     *
     * @return Value of property principalDue.
     */
    public java.lang.String getPrincipalDue() {
        return principalDue;
    }

    /**
     * Setter for property principalDue.
     *
     * @param principalDue New value of property principalDue.
     */
    public void setPrincipalDue(java.lang.String principalDue) {
        this.principalDue = principalDue;
    }

    /**
     * Getter for property interestDue.
     *
     * @return Value of property interestDue.
     */
    public java.lang.String getInterestDue() {
        return interestDue;
    }

    /**
     * Setter for property interestDue.
     *
     * @param interestDue New value of property interestDue.
     */
    public void setInterestDue(java.lang.String interestDue) {
        this.interestDue = interestDue;
    }

    /**
     * Getter for property penalExceptionAmt.
     *
     * @return Value of property penalExceptionAmt.
     */
    public java.lang.Double getPenalExceptionAmt() {
        return penalExceptionAmt;
    }

    /**
     * Setter for property penalExceptionAmt.
     *
     * @param penalExceptionAmt New value of property penalExceptionAmt.
     */
    public void setPenalExceptionAmt(java.lang.Double penalExceptionAmt) {
        this.penalExceptionAmt = penalExceptionAmt;
    }
}