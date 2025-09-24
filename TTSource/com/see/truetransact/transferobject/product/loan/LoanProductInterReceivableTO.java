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
package com.see.truetransact.transferobject.product.loan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_INTREC.
 */
public class LoanProductInterReceivableTO extends TransferObject implements Serializable {

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
    private Double periodFreq = null;
    private String prematurePeriod = "";
    private Double periodIntCalcFreq = null;
    private String prematureIntCalcPeriod = "";
    private String prematureIntCalAmt = "";
    private String billByBill = "";
    private String uptoMaturity = "";
    private String uptoDepositMaturity = "";
    private Double gracePeriodPenal = null;
    private String insuranceApplicable = "";
    private String insuranceSanctionDt = "";
    private Double insuranceRate = null;
    private String interestDueKeptReceivable = "";
    private Integer interestRepaymentFreq = 0;
    // Added by nithya on 22-03-2018 for 0008470: Property Mortgage Loan Ledger modification as per attachment
    private String isOverDueIntTaken = "";
    private String emiPenal = "";
    private String emiPenalCalcMethod = "";
    private String prematureClosureIntCalcRequired = "";// Added by nithya on 08-08-2018 for KD-187 need to change property loan penal calculation (mvnl)
    private String  intCalcFromSanctionDt = "";// Added by nithya on 13-02-2019 for KD-414 0019094: SUVARNA GOLD LOAN INTEREST ISSUE
    private Integer gracePeriodForOverDueInt = null; // Added by nithya on 13-02-2019 for KD-414 0019094: SUVARNA GOLD LOAN INTEREST ISSUE
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

    public Integer getInterestRepaymentFreq() {
        return interestRepaymentFreq;
    }

    public void setInterestRepaymentFreq(Integer interestRepaymentFreq) {
        this.interestRepaymentFreq = interestRepaymentFreq;
    }

   

    public String getEmiPenal() {
        return emiPenal;
    }

    public void setEmiPenal(String emiPenal) {
        this.emiPenal = emiPenal;
    }

    public String getEmiPenalCalcMethod() {
        return emiPenalCalcMethod;
    }

    public void setEmiPenalCalcMethod(String emiPenalCalcMethod) {
        this.emiPenalCalcMethod = emiPenalCalcMethod;
    }

    public String getIsOverDueIntTaken() {
        return isOverDueIntTaken;
    }

    public void setIsOverDueIntTaken(String isOverDueIntTaken) {
        this.isOverDueIntTaken = isOverDueIntTaken;
    }

    // Added by nithya on 08-08-2018 for KD-187 need to change property loan penal calculation (mvnl)
    public String getPrematureClosureIntCalcRequired() {
        return prematureClosureIntCalcRequired;
    }

    public void setPrematureClosureIntCalcRequired(String prematureClosureIntCalcRequired) {
        this.prematureClosureIntCalcRequired = prematureClosureIntCalcRequired;
    }  
    // End
    
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
        strB.append(getTOString("periodFreq", periodFreq));
        strB.append(getTOString("prematurePeriod", prematurePeriod));
        strB.append(getTOString("periodIntCalcFreq", periodIntCalcFreq));
        strB.append(getTOString("prematureIntCalcPeriod", prematureIntCalcPeriod));
        strB.append(getTOString("prematureIntCalAmt", prematureIntCalAmt));
        strB.append(getTOString("uptoMaturity", uptoMaturity));
        strB.append(getTOString("uptoDepositMaturity", uptoDepositMaturity));
        strB.append(getTOString("gracePeriodPenal", gracePeriodPenal));
        strB.append(getTOString("interestDueKeptReceivable", interestDueKeptReceivable));

        strB.append(getTOString("insuranceApplicable", interestDueKeptReceivable));
        strB.append(getTOString("insuranceSanctionDt", interestDueKeptReceivable));
        strB.append(getTOString("insuranceRate", interestDueKeptReceivable));
        strB.append(getTOString("interestRepaymentFreq", interestRepaymentFreq));
        
        strB.append(getTOString("isOverDueIntTaken", isOverDueIntTaken));
        strB.append(getTOString("emiPenal", emiPenal));
        strB.append(getTOString("emiPenalCalcMethod", emiPenalCalcMethod));        
        strB.append(getTOString("prmatureClosureIntCalcRequired", prematureClosureIntCalcRequired));
        strB.append(getTOString("intCalcFromSanctionDt", intCalcFromSanctionDt));
        strB.append(getTOString("GracePeriodForOverDueInt", gracePeriodForOverDueInt));
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
        strB.append(getTOXml("periodFreq", periodFreq));
        strB.append(getTOXml("prematurePeriod", prematurePeriod));
        strB.append(getTOXml("periodIntCalcFreq", periodIntCalcFreq));
        strB.append(getTOXml("prematureIntCalcPeriod", prematureIntCalcPeriod));
        strB.append(getTOXml("prematureIntCalAmt", prematureIntCalAmt));
        strB.append(getTOXml("uptoMaturity", uptoMaturity));
        strB.append(getTOXml("uptoDepositMaturity", uptoDepositMaturity));
        strB.append(getTOXml("gracePeriodPenal", gracePeriodPenal));
        strB.append(getTOXml("interestDueKeptReceivable", interestDueKeptReceivable));
        strB.append(getTOXml("insuranceApplicable", interestDueKeptReceivable));
        strB.append(getTOXml("insuranceSanctionDt", interestDueKeptReceivable));
        strB.append(getTOXml("insuranceRate", interestDueKeptReceivable));
        strB.append(getTOXml("interestRepaymentFreq", interestRepaymentFreq));
        
        strB.append(getTOXml("isOverDueIntTaken", isOverDueIntTaken));
        strB.append(getTOXml("emiPenal", emiPenal));
        strB.append(getTOXml("emiPenalCalcMethod", emiPenalCalcMethod));
        strB.append(getTOXml("prmatureClosureIntCalcRequired", prematureClosureIntCalcRequired));
        strB.append(getTOXml("intCalcFromSanctionDt", intCalcFromSanctionDt));
        strB.append(getTOXml("GracePeriodForOverDueInt", gracePeriodForOverDueInt));
     
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
     * Getter for property periodFreq.
     *
     * @return Value of property periodFreq.
     */
    public java.lang.Double getPeriodFreq() {
        return periodFreq;
    }

    /**
     * Setter for property periodFreq.
     *
     * @param periodFreq New value of property periodFreq.
     */
    public void setPeriodFreq(java.lang.Double periodFreq) {
        this.periodFreq = periodFreq;
    }

    /**
     * Getter for property prematureIntCalAmt.
     *
     * @return Value of property prematureIntCalAmt.
     */
    public java.lang.String getPrematureIntCalAmt() {
        return prematureIntCalAmt;
    }

    /**
     * Setter for property prematureIntCalAmt.
     *
     * @param prematureIntCalAmt New value of property prematureIntCalAmt.
     */
    public void setPrematureIntCalAmt(java.lang.String prematureIntCalAmt) {
        this.prematureIntCalAmt = prematureIntCalAmt;
    }

    /**
     * Getter for property billByBill.
     *
     * @return Value of property billByBill.
     */
    public java.lang.String getBillByBill() {
        return billByBill;
    }

    /**
     * Setter for property billByBill.
     *
     * @param billByBill New value of property billByBill.
     */
    public void setBillByBill(java.lang.String billByBill) {
        this.billByBill = billByBill;
    }

    /**
     * Getter for property prematurePeriod.
     *
     * @return Value of property prematurePeriod.
     */
    public java.lang.String getPrematurePeriod() {
        return prematurePeriod;
    }

    /**
     * Setter for property prematurePeriod.
     *
     * @param prematurePeriod New value of property prematurePeriod.
     */
    public void setPrematurePeriod(java.lang.String prematurePeriod) {
        this.prematurePeriod = prematurePeriod;
    }

    /**
     * Getter for property periodIntCalcFreq.
     *
     * @return Value of property periodIntCalcFreq.
     */
    public java.lang.Double getPeriodIntCalcFreq() {
        return periodIntCalcFreq;
    }

    /**
     * Setter for property periodIntCalcFreq.
     *
     * @param periodIntCalcFreq New value of property periodIntCalcFreq.
     */
    public void setPeriodIntCalcFreq(java.lang.Double periodIntCalcFreq) {
        this.periodIntCalcFreq = periodIntCalcFreq;
    }

    /**
     * Getter for property prematureIntCalcPeriod.
     *
     * @return Value of property prematureIntCalcPeriod.
     */
    public java.lang.String getPrematureIntCalcPeriod() {
        return prematureIntCalcPeriod;
    }

    /**
     * Setter for property prematureIntCalcPeriod.
     *
     * @param prematureIntCalcPeriod New value of property
     * prematureIntCalcPeriod.
     */
    public void setPrematureIntCalcPeriod(java.lang.String prematureIntCalcPeriod) {
        this.prematureIntCalcPeriod = prematureIntCalcPeriod;
    }

    /**
     * Getter for property uptoMaturity.
     *
     * @return Value of property uptoMaturity.
     */
    public java.lang.String getUptoMaturity() {
        return uptoMaturity;
    }

    /**
     * Setter for property uptoMaturity.
     *
     * @param uptoMaturity New value of property uptoMaturity.
     */
    public void setUptoMaturity(java.lang.String uptoMaturity) {
        this.uptoMaturity = uptoMaturity;
    }

    /**
     * Getter for property uptoDepositMaturity.
     *
     * @return Value of property uptoDepositMaturity.
     */
    public java.lang.String getUptoDepositMaturity() {
        return uptoDepositMaturity;
    }

    /**
     * Setter for property uptoDepositMaturity.
     *
     * @param uptoDepositMaturity New value of property uptoDepositMaturity.
     */
    public void setUptoDepositMaturity(java.lang.String uptoDepositMaturity) {
        this.uptoDepositMaturity = uptoDepositMaturity;
    }

    /**
     * Getter for property gracePeriodPenal.
     *
     * @return Value of property gracePeriodPenal.
     */
    public java.lang.Double getGracePeriodPenal() {
        return gracePeriodPenal;
    }

    /**
     * Setter for property gracePeriodPenal.
     *
     * @param gracePeriodPenal New value of property gracePeriodPenal.
     */
    public void setGracePeriodPenal(java.lang.Double gracePeriodPenal) {
        this.gracePeriodPenal = gracePeriodPenal;
    }

    /**
     * Getter for property interestDueKeptReceivable.
     *
     * @return Value of property interestDueKeptReceivable.
     */
    public java.lang.String getInterestDueKeptReceivable() {
        return interestDueKeptReceivable;
    }

    /**
     * Setter for property interestDueKeptReceivable.
     *
     * @param interestDueKeptReceivable New value of property
     * interestDueKeptReceivable.
     */
    public void setInterestDueKeptReceivable(java.lang.String interestDueKeptReceivable) {
        this.interestDueKeptReceivable = interestDueKeptReceivable;
    }

    /**
     * Getter for property insuranceApplicable.
     *
     * @return Value of property insuranceApplicable.
     */
    public java.lang.String getInsuranceApplicable() {
        return insuranceApplicable;
    }

    /**
     * Setter for property insuranceApplicable.
     *
     * @param insuranceApplicable New value of property insuranceApplicable.
     */
    public void setInsuranceApplicable(java.lang.String insuranceApplicable) {
        this.insuranceApplicable = insuranceApplicable;
    }

    /**
     * Getter for property insuranceSanctionDt.
     *
     * @return Value of property insuranceSanctionDt.
     */
    public java.lang.String getInsuranceSanctionDt() {
        return insuranceSanctionDt;
    }

    /**
     * Setter for property insuranceSanctionDt.
     *
     * @param insuranceSanctionDt New value of property insuranceSanctionDt.
     */
    public void setInsuranceSanctionDt(java.lang.String insuranceSanctionDt) {
        this.insuranceSanctionDt = insuranceSanctionDt;
    }

    /**
     * Getter for property insuranceRate.
     *
     * @return Value of property insuranceRate.
     */
    public java.lang.Double getInsuranceRate() {
        return insuranceRate;
    }

    /**
     * Setter for property insuranceRate.
     *
     * @param insuranceRate New value of property insuranceRate.
     */
    public void setInsuranceRate(java.lang.Double insuranceRate) {
        this.insuranceRate = insuranceRate;
    }

    public Integer getGracePeriodForOverDueInt() {
        return gracePeriodForOverDueInt;
    }

    public void setGracePeriodForOverDueInt(Integer gracePeriodForOverDueInt) {
        this.gracePeriodForOverDueInt = gracePeriodForOverDueInt;
    }

    public String getIntCalcFromSanctionDt() {
        return intCalcFromSanctionDt;
    }

    public void setIntCalcFromSanctionDt(String intCalcFromSanctionDt) {
        this.intCalcFromSanctionDt = intCalcFromSanctionDt;
    }
    
}
