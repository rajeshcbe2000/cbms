/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositsProductRenewalTO.java
 *
 * Created on Tue Jul 06 17:44:01 IST 2004
 */
package com.see.truetransact.transferobject.product.deposits;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSITS_PROD_RENEWAL.
 */
public class DepositsProductRenewalTO extends TransferObject implements Serializable {

    private String prodId = "";
    private Double maxPdbkdtRenewal = null;
    private String maxPdInttype = "";
    private Double maxPdMaturitydt = null;
    private String intCriteria = "";
    private String renewalDepositAllowed = "";
    private Double minDaysBkdtDeposits = null;
    private String autoRenewalAllowed = "";
    private Double maxNoAutoRenewal = null;
    private String periodOfFormat = "";
    private String sameNoRenewalAllowed = "";
    private Double maxNoSameNoRenewal = null;
    private String bothRateNotAvail = "";
    private Double renewdDepClosedBefore = null;
    private String oneRateAvail = "";
    private String renewedDepIntPay = "";
    private Double periodOfRenewal = null;
    private String renewedDepositFormat = "";
    private String dailyIntCalc = "";
    private String dateOfRenewal = "";
    private String dateOfMaturity = "";
    private String rdoPartialWithdrawlForDD = "";
    private String intRateAppliedOverdue = "";
    private String renewedDepIntRecovered = "";
    private Double weeklyBasis = null;
    private String cbmMonthlyIntCalcMethod = "";
    private String eligibleTwoRate = "";
    private String beyondOriginal = "";
    private String extensionPenal = "";
    private String  sbRateProdId="";
    private String closureIntYN="";
    private String deathMarkedYN="";

    public String getDeathMarkedYN() {
        return deathMarkedYN;
    }

    public void setDeathMarkedYN(String deathMarkedYN) {
        this.deathMarkedYN = deathMarkedYN;
    }
    
    

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
     * Setter/Getter for MAX_PDBKDT_RENEWAL - table Field
     */
    public void setMaxPdbkdtRenewal(Double maxPdbkdtRenewal) {
        this.maxPdbkdtRenewal = maxPdbkdtRenewal;
    }

    public Double getMaxPdbkdtRenewal() {
        return maxPdbkdtRenewal;
    }

    /**
     * Setter/Getter for MAX_PD_INTTYPE - table Field
     */
    public void setMaxPdInttype(String maxPdInttype) {
        this.maxPdInttype = maxPdInttype;
    }

    public String getMaxPdInttype() {
        return maxPdInttype;
    }

    /**
     * Setter/Getter for MAX_PD_MATURITYDT - table Field
     */
    public void setMaxPdMaturitydt(Double maxPdMaturitydt) {
        this.maxPdMaturitydt = maxPdMaturitydt;
    }

    public Double getMaxPdMaturitydt() {
        return maxPdMaturitydt;
    }

    /**
     * Setter/Getter for INT_CRITERIA - table Field
     */
    public void setIntCriteria(String intCriteria) {
        this.intCriteria = intCriteria;
    }

    public String getIntCriteria() {
        return intCriteria;
    }

    /**
     * Setter/Getter for RENEWAL_DEPOSIT_ALLOWED - table Field
     */
    public void setRenewalDepositAllowed(String renewalDepositAllowed) {
        this.renewalDepositAllowed = renewalDepositAllowed;
    }

    public String getRenewalDepositAllowed() {
        return renewalDepositAllowed;
    }

    /**
     * Setter/Getter for MIN_DAYS_BKDT_DEPOSITS - table Field
     */
    public void setMinDaysBkdtDeposits(Double minDaysBkdtDeposits) {
        this.minDaysBkdtDeposits = minDaysBkdtDeposits;
    }

    public Double getMinDaysBkdtDeposits() {
        return minDaysBkdtDeposits;
    }

    /**
     * Setter/Getter for AUTO_RENEWAL_ALLOWED - table Field
     */
    public void setAutoRenewalAllowed(String autoRenewalAllowed) {
        this.autoRenewalAllowed = autoRenewalAllowed;
    }

    public String getAutoRenewalAllowed() {
        return autoRenewalAllowed;
    }

    /**
     * Setter/Getter for MAX_NO_AUTO_RENEWAL - table Field
     */
    public void setMaxNoAutoRenewal(Double maxNoAutoRenewal) {
        this.maxNoAutoRenewal = maxNoAutoRenewal;
    }

    public Double getMaxNoAutoRenewal() {
        return maxNoAutoRenewal;
    }

    public java.lang.String getBeyondOriginal() {
        return beyondOriginal;
    }

    /**
     * Setter for property beyondOriginal.
     *
     * @param beyondOriginal New value of property beyondOriginal.
     */
    public void setBeyondOriginal(java.lang.String beyondOriginal) {
        this.beyondOriginal = beyondOriginal;
    }

    public java.lang.String getPeriodOfFormat() {
        return periodOfFormat;
    }

    /**
     * Setter for property periodOfFormat.
     *
     * @param periodOfFormat New value of property periodOfFormat.
     */
    public void setPeriodOfFormat(java.lang.String periodOfFormat) {
        this.periodOfFormat = periodOfFormat;
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
        strB.append(getTOString("maxPdbkdtRenewal", maxPdbkdtRenewal));
        strB.append(getTOString("maxPdInttype", maxPdInttype));
        strB.append(getTOString("maxPdMaturitydt", maxPdMaturitydt));
        strB.append(getTOString("intCriteria", intCriteria));
        strB.append(getTOString("renewalDepositAllowed", renewalDepositAllowed));
        strB.append(getTOString("minDaysBkdtDeposits", minDaysBkdtDeposits));
        strB.append(getTOString("autoRenewalAllowed", autoRenewalAllowed));
        strB.append(getTOString("maxNoAutoRenewal", maxNoAutoRenewal));
        strB.append(getTOString("sameNoRenewalAllowed", sameNoRenewalAllowed));
        strB.append(getTOString("maxNoSameNoRenewal", maxNoSameNoRenewal));
        strB.append(getTOString("dailyIntCalc", dailyIntCalc));
        strB.append(getTOString("rdoPartialWithdrawlForDD", rdoPartialWithdrawlForDD));
        strB.append(getTOString("weeklyBasis", weeklyBasis));
        strB.append(getTOString("cbmMonthlyIntCalcMethod", cbmMonthlyIntCalcMethod));
        strB.append(getTOString("renewedDepIntRecovered", renewedDepIntRecovered));
        strB.append(getTOString("intRateAppliedOverdue", intRateAppliedOverdue));
        strB.append(getTOString("dateOfMaturity", dateOfMaturity));
        strB.append(getTOString("eligibleTwoRate", eligibleTwoRate));
        strB.append(getTOString("oneRateAvail", oneRateAvail));
        strB.append(getTOString("bothRateNotAvail", bothRateNotAvail));
        strB.append(getTOString("periodOfFormat", periodOfFormat));
        strB.append(getTOString("renewedDepositFormat", renewedDepositFormat));
        strB.append(getTOString("renewdDepClosedBefore", renewdDepClosedBefore));
        strB.append(getTOString("periodOfRenewal", periodOfRenewal));
        strB.append(getTOString("renewedDepIntPay", renewedDepIntPay));
        strB.append(getTOString("beyondOriginal", beyondOriginal));
        strB.append(getTOString("extensionPenal", extensionPenal));
        strB.append(getTOString("sbRateProdId", sbRateProdId));
        strB.append(getTOString("closureIntYN", closureIntYN));
        strB.append(getTOString("deathMarkedYN", deathMarkedYN));
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
        strB.append(getTOXml("maxPdbkdtRenewal", maxPdbkdtRenewal));
        strB.append(getTOXml("maxPdInttype", maxPdInttype));
        strB.append(getTOXml("maxPdMaturitydt", maxPdMaturitydt));
        strB.append(getTOXml("intCriteria", intCriteria));
        strB.append(getTOXml("renewalDepositAllowed", renewalDepositAllowed));
        strB.append(getTOXml("minDaysBkdtDeposits", minDaysBkdtDeposits));
        strB.append(getTOXml("autoRenewalAllowed", autoRenewalAllowed));
        strB.append(getTOXml("maxNoAutoRenewal", maxNoAutoRenewal));
        strB.append(getTOXml("sameNoRenewalAllowed", sameNoRenewalAllowed));
        strB.append(getTOXml("maxNoSameNoRenewal", maxNoSameNoRenewal));
        strB.append(getTOXml("dailyIntCalc", dailyIntCalc));
        strB.append(getTOXml("rdoPartialWithdrawlForDD", rdoPartialWithdrawlForDD));
        strB.append(getTOXml("weeklyBasis", weeklyBasis));
        strB.append(getTOXml("cbmMonthlyIntCalcMethod", cbmMonthlyIntCalcMethod));
        strB.append(getTOXml("renewedDepIntRecovered", renewedDepIntRecovered));
        strB.append(getTOXml("intRateAppliedOverdue", intRateAppliedOverdue));
        strB.append(getTOXml("eligibleTwoRate", eligibleTwoRate));
        strB.append(getTOXml("dateOfMaturity", dateOfMaturity));
        strB.append(getTOXml("periodOfRenewal", periodOfRenewal));
        strB.append(getTOXml("periodOfFormat", periodOfFormat));
        strB.append(getTOXml("oneRateAvail", oneRateAvail));
        strB.append(getTOXml("renewdDepClosedBefore", renewdDepClosedBefore));
        strB.append(getTOXml("renewedDepositFormat", renewedDepositFormat));
        strB.append(getTOXml("bothRateNotAvail", bothRateNotAvail));
        strB.append(getTOXml("renewedDepIntPay", renewedDepIntPay));
        strB.append(getTOXml("beyondOriginal", beyondOriginal));
        strB.append(getTOXml("extensionPenal", extensionPenal));
        strB.append(getTOXml("sbRateProdId", sbRateProdId));
        strB.append(getTOXml("closureIntYN", closureIntYN));
        strB.append(getTOXml("deathMarkedYN", deathMarkedYN));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property sameNoRenewalAllowed.
     *
     * @return Value of property sameNoRenewalAllowed.
     */
    public java.lang.Double getPeriodOfRenewal() {
        return periodOfRenewal;
    }

    /**
     * Setter for property periodOfRenewal.
     *
     * @param periodOfRenewal New value of property periodOfRenewal.
     */
    public void setPeriodOfRenewal(java.lang.Double periodOfRenewal) {
        this.periodOfRenewal = periodOfRenewal;
    }

    public java.lang.Double getRenewdDepClosedBefore() {
        return renewdDepClosedBefore;
    }

    /**
     * Setter for property renewdDepClosedBefore.
     *
     * @param renewdDepClosedBefore New value of property renewdDepClosedBefore.
     */
    public void setRenewdDepClosedBefore(java.lang.Double renewdDepClosedBefore) {
        this.renewdDepClosedBefore = renewdDepClosedBefore;
    }

    public java.lang.String getSameNoRenewalAllowed() {
        return sameNoRenewalAllowed;
    }

    /**
     * Setter for property sameNoRenewalAllowed.
     *
     * @param sameNoRenewalAllowed New value of property sameNoRenewalAllowed.
     */
    public void setSameNoRenewalAllowed(java.lang.String sameNoRenewalAllowed) {
        this.sameNoRenewalAllowed = sameNoRenewalAllowed;
    }

    /**
     * Getter for property maxNoSameNoRenewal.
     *
     * @return Value of property maxNoSameNoRenewal.
     */
    public java.lang.Double getMaxNoSameNoRenewal() {
        return maxNoSameNoRenewal;
    }

    /**
     * Setter for property maxNoSameNoRenewal.
     *
     * @param maxNoSameNoRenewal New value of property maxNoSameNoRenewal.
     */
    public void setMaxNoSameNoRenewal(java.lang.Double maxNoSameNoRenewal) {
        this.maxNoSameNoRenewal = maxNoSameNoRenewal;
    }

    public java.lang.String getOneRateAvail() {
        return oneRateAvail;
    }

    public java.lang.String getRenewedDepositFormat() {
        return renewedDepositFormat;
    }

    /**
     * Setter for property renewedDepositFormat.
     *
     * @param renewedDepositFormat New value of property renewedDepositFormat.
     */
    public void setRenewedDepositFormat(java.lang.String renewedDepositFormat) {
        this.renewedDepositFormat = renewedDepositFormat;
    }

    public java.lang.String getRenewedDepIntRecovered() {
        return renewedDepIntRecovered;
    }

    public java.lang.String getRenewedDepIntPay() {
        return renewedDepIntPay;
    }

    /**
     * Setter for property renewedDepIntPay.
     *
     * @param renewedDepIntPay New value of property renewedDepIntPay.
     */
    public void setRenewedDepIntPay(java.lang.String renewedDepIntPay) {
        this.renewedDepIntPay = renewedDepIntPay;
    }

    /**
     * Setter for property renewedDepIntRecovered.
     *
     * @param renewedDepIntRecovered New value of property
     * renewedDepIntRecovered.
     */
    public void setRenewedDepIntRecovered(java.lang.String renewedDepIntRecovered) {
        this.renewedDepIntRecovered = renewedDepIntRecovered;
    }

    /**
     * Getter for property renewdDepClosedBefore.
     *
     * @return Value of property renewdDepClosedBefore.
     */
    /**
     * Setter for property oneRateAvail.
     *
     * @param oneRateAvail New value of property oneRateAvail.
     */
    public void setOneRateAvail(java.lang.String oneRateAvail) {
        this.oneRateAvail = oneRateAvail;
    }

    /**
     * Getter for property dailyIntCalc.
     *
     * @return Value of property dailyIntCalc.
     */
    public java.lang.String getDailyIntCalc() {
        return dailyIntCalc;
    }

    /**
     * Setter for property dailyIntCalc.
     *
     * @param dailyIntCalc New value of property dailyIntCalc.
     */
    public void setDailyIntCalc(java.lang.String dailyIntCalc) {
        this.dailyIntCalc = dailyIntCalc;
    }

    public java.lang.String getBothRateNotAvail() {
        return bothRateNotAvail;
    }

    /**
     * Setter for property bothRateNotAvail.
     *
     * @param bothRateNotAvail New value of property bothRateNotAvail.
     */
    public void setBothRateNotAvail(java.lang.String bothRateNotAvail) {
        this.bothRateNotAvail = bothRateNotAvail;
    }

    /**
     * Getter for property weeklyBasis.
     *
     * @return Value of property weeklyBasis.
     */
    public java.lang.Double getWeeklyBasis() {
        return weeklyBasis;
    }

    /**
     * Setter for property weeklyBasis.
     *
     * @param weeklyBasis New value of property weeklyBasis.
     */
    public void setWeeklyBasis(java.lang.Double weeklyBasis) {
        this.weeklyBasis = weeklyBasis;
    }

    /**
     * Getter for property cbmMonthlyIntCalcMethod.
     *
     * @return Value of property cbmMonthlyIntCalcMethod.
     */
    public java.lang.String getCbmMonthlyIntCalcMethod() {
        return cbmMonthlyIntCalcMethod;
    }

    /**
     * Setter for property cbmMonthlyIntCalcMethod.
     *
     * @param cbmMonthlyIntCalcMethod New value of property
     * cbmMonthlyIntCalcMethod.
     */
    public void setCbmMonthlyIntCalcMethod(java.lang.String cbmMonthlyIntCalcMethod) {
        this.cbmMonthlyIntCalcMethod = cbmMonthlyIntCalcMethod;
    }

    public java.lang.String getEligibleTwoRate() {
        return eligibleTwoRate;
    }

    /**
     * Setter for property eligibleTwoRate.
     *
     * @param eligibleTwoRate New value of property eligibleTwoRate.
     */
    public void setEligibleTwoRate(java.lang.String eligibleTwoRate) {
        this.eligibleTwoRate = eligibleTwoRate;
    }

    /**
     * Getter for property rdoPartialWithdrawlForDD.
     *
     * @return Value of property rdoPartialWithdrawlForDD.
     */
    public java.lang.String getRdoPartialWithdrawlForDD() {
        return rdoPartialWithdrawlForDD;
    }

    /**
     * Setter for property rdoPartialWithdrawlForDD.
     *
     * @param rdoPartialWithdrawlForDD New value of property
     * rdoPartialWithdrawlForDD.
     */
    public void setRdoPartialWithdrawlForDD(java.lang.String rdoPartialWithdrawlForDD) {
        this.rdoPartialWithdrawlForDD = rdoPartialWithdrawlForDD;
    }

    public java.lang.String getDateOfRenewal() {
        return dateOfRenewal;
    }

    /**
     * Setter for property dateOfRenewal.
     *
     * @param dateOfRenewal New value of property dateOfRenewal.
     */
    public void setDateOfRenewal(java.lang.String dateOfRenewal) {
        this.dateOfRenewal = dateOfRenewal;
    }

    /**
     * Getter for property dateOfMaturity.
     *
     * @return Value of property dateOfMaturity.
     */
    public java.lang.String getDateOfMaturity() {
        return dateOfMaturity;
    }

    /**
     * Setter for property dateOfMaturity.
     *
     * @param dateOfMaturity New value of property dateOfMaturity.
     */
    public void setDateOfMaturity(java.lang.String dateOfMaturity) {
        this.dateOfMaturity = dateOfMaturity;
    }

    public java.lang.String getExtensionPenal() {
        return extensionPenal;
    }

    /**
     * Setter for property extensionPenal.
     *
     * @param extensionPenal New value of property extensionPenal.
     */
    public void setExtensionPenal(java.lang.String extensionPenal) {
        this.extensionPenal = extensionPenal;
    }

    public java.lang.String getIntRateAppliedOverdue() {
        return intRateAppliedOverdue;
    }

    /**
     * Setter for property intRateAppliedOverdue.
     *
     * @param intRateAppliedOverdue New value of property intRateAppliedOverdue.
     */
    public void setIntRateAppliedOverdue(java.lang.String intRateAppliedOverdue) {
        this.intRateAppliedOverdue = intRateAppliedOverdue;
    }

    public String getSbRateProdId() {
        return sbRateProdId;
    }

    public void setSbRateProdId(String sbRateProdId) {
        this.sbRateProdId = sbRateProdId;
    }

    public String getClosureIntYN() {
        return closureIntYN;
    }

    public void setClosureIntYN(String closureIntYN) {
        this.closureIntYN = closureIntYN;
    }

    
    /**
     * Setter for property renewedDepIntRecovered.
     *
     * @param renewedDepIntRecovered New value of property
     * renewedDepIntRecovered.
     */
    
}