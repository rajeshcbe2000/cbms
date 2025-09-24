/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositsProductSchemeTO.java
 *
 * Created on Tue Jul 06 11:11:23 IST 2004
 */
package com.see.truetransact.transferobject.product.deposits;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSITS_PROD_SCHEME.
 */
public class DepositsProductSchemeTO extends TransferObject implements Serializable {

    private String depositUnlien = "";
    private String cummCertPrint = "";
    private String excludeLienStanding = "";
    private String excludeLienIntrstAppl = "";
    //private String prematClosInSI="";
    private String prodId = "";
    private String calculateTds = "";
    private String payintDepMaturity = "";
    private String maturityAmtRound = "";
    private String roundoffCriteria = "";
    private String intpaidAftermaturity = "";
    private String aftermaturityInttype = "";
    private String aftermaturityIntrate = "";
    private Double depositPerUnit = null;
    private Double minDepositPeriod = null;
    private Double maxDepositPeriod = null;
    private Double periodMultiples = null;
    private Double minDepositAmt = null;
    private Double maxDepositAmt = null;
    private Double amtMultiples = null;
    private String calcMaturityValue = "";
    private String transMaturedDep = "";
    private Double afterDays = null;
    private Double advMaturityGenpd = null;
    private String provIntmaturedDep = "";
    private Double intMaturedDep = null;
    private String partialWithdrawal = "";
    private Double noPartialWithdrawal = null;
    private Double maxAmtWithdrawal = null;
    private Double maxNoWithdrawalYr = null;
    private Double minAmtWithdrawal = null;
    private Double withdrawalMulti = null;
    private Double serviceChargePer = null;
    private String withdrawalInterest = "";
    private String serviceCharge = "";
    private String extDepMaturity = "";
    private String adjMaturityLoan = "";
    private String adjIntLien = "";
    private String adjPrinLoanLien = "";
    private Double prematureWithdrawal = null;
    private String flexiSbCa = "";
    private String tdSecurityOd = "";
    private String acnumPattern = "";
    private String alphaSuffixTdrec = "";
    private Double maxAmtCash = null;
    private Double minAmtPan = null;
    private String introRequired = "";
    private Date schemeIntroDt = null;
    private String certificatePrint = "";
    private Double limitBulkDeposit = null;
    private Double afterNoDays = null;
    private String discounted = "";
    private Date schemeClosingDt = null;
    private String typesOfDep = "";
    private String staffAccount = "";
    private String rdoWithPeriod = "";
    private String rdoDoublingScheme = "";
    private String fdRenewalSameNoTranPrincAmt = "";
    private String canZeroBalActOpeng = "";
    private Integer doubligCount;
    private String agentcommSlabRequired ="N";
    private String agentCommCalcMethod = "";

    public Integer getDoubligCount() {
        return doubligCount;
    }

    public void setDoubligCount(Integer doubligCount) {
        this.doubligCount = doubligCount;
    }

    
    public String getCanZeroBalActOpeng() {
        return canZeroBalActOpeng;
    }

    public void setCanZeroBalActOpeng(String canZeroBalActOpeng) {
        this.canZeroBalActOpeng = canZeroBalActOpeng;
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
     * Setter/Getter for CALCULATE_TDS - table Field
     */
    public void setCalculateTds(String calculateTds) {
        this.calculateTds = calculateTds;
    }

    public String getCalculateTds() {
        return calculateTds;
    }

    /**
     * Setter/Getter for PAYINT_DEP_MATURITY - table Field
     */
    public void setPayintDepMaturity(String payintDepMaturity) {
        this.payintDepMaturity = payintDepMaturity;
    }

    public String getPayintDepMaturity() {
        return payintDepMaturity;
    }

    /**
     * Setter/Getter for MATURITY_AMT_ROUND - table Field
     */
    public void setMaturityAmtRound(String maturityAmtRound) {
        this.maturityAmtRound = maturityAmtRound;
    }

    public String getMaturityAmtRound() {
        return maturityAmtRound;
    }

    /**
     * Setter/Getter for ROUNDOFF_CRITERIA - table Field
     */
    public void setRoundoffCriteria(String roundoffCriteria) {
        this.roundoffCriteria = roundoffCriteria;
    }

    public String getRoundoffCriteria() {
        return roundoffCriteria;
    }

    /**
     * Setter/Getter for INTPAID_AFTERMATURITY - table Field
     */
    public void setIntpaidAftermaturity(String intpaidAftermaturity) {
        this.intpaidAftermaturity = intpaidAftermaturity;
    }

    public String getIntpaidAftermaturity() {
        return intpaidAftermaturity;
    }

    /**
     * Setter/Getter for AFTERMATURITY_INTTYPE - table Field
     */
    public void setAftermaturityInttype(String aftermaturityInttype) {
        this.aftermaturityInttype = aftermaturityInttype;
    }

    public String getAftermaturityInttype() {
        return aftermaturityInttype;
    }

    /**
     * Setter/Getter for AFTERMATURITY_INTRATE - table Field
     */
    public void setAftermaturityIntrate(String aftermaturityIntrate) {
        this.aftermaturityIntrate = aftermaturityIntrate;
    }

    public String getAftermaturityIntrate() {
        return aftermaturityIntrate;
    }

    /**
     * Setter/Getter for DEPOSIT_PER_UNIT - table Field
     */
    public void setDepositPerUnit(Double depositPerUnit) {
        this.depositPerUnit = depositPerUnit;
    }

    public Double getDepositPerUnit() {
        return depositPerUnit;
    }

    /**
     * Setter/Getter for MIN_DEPOSIT_PERIOD - table Field
     */
    public void setMinDepositPeriod(Double minDepositPeriod) {
        this.minDepositPeriod = minDepositPeriod;
    }

    public Double getMinDepositPeriod() {
        return minDepositPeriod;
    }

    /**
     * Setter/Getter for MAX_DEPOSIT_PERIOD - table Field
     */
    public void setMaxDepositPeriod(Double maxDepositPeriod) {
        this.maxDepositPeriod = maxDepositPeriod;
    }

    public Double getMaxDepositPeriod() {
        return maxDepositPeriod;
    }

    /**
     * Setter/Getter for PERIOD_MULTIPLES - table Field
     */
    public void setPeriodMultiples(Double periodMultiples) {
        this.periodMultiples = periodMultiples;
    }

    public Double getPeriodMultiples() {
        return periodMultiples;
    }

    /**
     * Setter/Getter for MIN_DEPOSIT_AMT - table Field
     */
    public void setMinDepositAmt(Double minDepositAmt) {
        this.minDepositAmt = minDepositAmt;
    }

    public Double getMinDepositAmt() {
        return minDepositAmt;
    }

    /**
     * Setter/Getter for MAX_DEPOSIT_AMT - table Field
     */
    public void setMaxDepositAmt(Double maxDepositAmt) {
        this.maxDepositAmt = maxDepositAmt;
    }

    public Double getMaxDepositAmt() {
        return maxDepositAmt;
    }

    /**
     * Setter/Getter for AMT_MULTIPLES - table Field
     */
    public void setAmtMultiples(Double amtMultiples) {
        this.amtMultiples = amtMultiples;
    }

    public Double getAmtMultiples() {
        return amtMultiples;
    }

    /**
     * Setter/Getter for CALC_MATURITY_VALUE - table Field
     */
    public void setCalcMaturityValue(String calcMaturityValue) {
        this.calcMaturityValue = calcMaturityValue;
    }

    public String getCalcMaturityValue() {
        return calcMaturityValue;
    }

    /**
     * Setter/Getter for TRANS_MATURED_DEP - table Field
     */
    public void setTransMaturedDep(String transMaturedDep) {
        this.transMaturedDep = transMaturedDep;
    }

    public String getTransMaturedDep() {
        return transMaturedDep;
    }

    /**
     * Setter/Getter for AFTER_DAYS - table Field
     */
    public void setAfterDays(Double afterDays) {
        this.afterDays = afterDays;
    }

    public Double getAfterDays() {
        return afterDays;
    }

    /**
     * Setter/Getter for ADV_MATURITY_GENPD - table Field
     */
    public void setAdvMaturityGenpd(Double advMaturityGenpd) {
        this.advMaturityGenpd = advMaturityGenpd;
    }

    public Double getAdvMaturityGenpd() {
        return advMaturityGenpd;
    }

    /**
     * Setter/Getter for PROV_INTMATURED_DEP - table Field
     */
    public void setProvIntmaturedDep(String provIntmaturedDep) {
        this.provIntmaturedDep = provIntmaturedDep;
    }

    public String getProvIntmaturedDep() {
        return provIntmaturedDep;
    }

    /**
     * Setter/Getter for INT_MATURED_DEP - table Field
     */
    public void setIntMaturedDep(Double intMaturedDep) {
        this.intMaturedDep = intMaturedDep;
    }

    public Double getIntMaturedDep() {
        return intMaturedDep;
    }

    /**
     * Setter/Getter for PARTIAL_WITHDRAWAL - table Field
     */
    public void setPartialWithdrawal(String partialWithdrawal) {
        this.partialWithdrawal = partialWithdrawal;
    }

    public String getPartialWithdrawal() {
        return partialWithdrawal;
    }

    /**
     * Setter/Getter for NO_PARTIAL_WITHDRAWAL - table Field
     */
    public void setNoPartialWithdrawal(Double noPartialWithdrawal) {
        this.noPartialWithdrawal = noPartialWithdrawal;
    }

    public Double getNoPartialWithdrawal() {
        return noPartialWithdrawal;
    }

    /**
     * Setter/Getter for MAX_AMT_WITHDRAWAL - table Field
     */
    public void setMaxAmtWithdrawal(Double maxAmtWithdrawal) {
        this.maxAmtWithdrawal = maxAmtWithdrawal;
    }

    public Double getMaxAmtWithdrawal() {
        return maxAmtWithdrawal;
    }

    /**
     * Setter/Getter for MAX_NO_WITHDRAWAL_YR - table Field
     */
    public void setMaxNoWithdrawalYr(Double maxNoWithdrawalYr) {
        this.maxNoWithdrawalYr = maxNoWithdrawalYr;
    }

    public Double getMaxNoWithdrawalYr() {
        return maxNoWithdrawalYr;
    }

    /**
     * Setter/Getter for MIN_AMT_WITHDRAWAL - table Field
     */
    public void setMinAmtWithdrawal(Double minAmtWithdrawal) {
        this.minAmtWithdrawal = minAmtWithdrawal;
    }

    public Double getMinAmtWithdrawal() {
        return minAmtWithdrawal;
    }

    /**
     * Setter/Getter for WITHDRAWAL_MULTI - table Field
     */
    public void setWithdrawalMulti(Double withdrawalMulti) {
        this.withdrawalMulti = withdrawalMulti;
    }

    public Double getWithdrawalMulti() {
        return withdrawalMulti;
    }

    /**
     * Setter/Getter for WITHDRAWAL_INTEREST - table Field
     */
    public void setWithdrawalInterest(String withdrawalInterest) {
        this.withdrawalInterest = withdrawalInterest;
    }

    public String getWithdrawalInterest() {
        return withdrawalInterest;
    }

    /**
     * Setter/Getter for EXT_DEP_MATURITY - table Field
     */
    public void setExtDepMaturity(String extDepMaturity) {
        this.extDepMaturity = extDepMaturity;
    }

    public String getExtDepMaturity() {
        return extDepMaturity;
    }

    /**
     * Setter/Getter for ADJ_MATURITY_LOAN - table Field
     */
    public void setAdjMaturityLoan(String adjMaturityLoan) {
        this.adjMaturityLoan = adjMaturityLoan;
    }

    public String getAdjMaturityLoan() {
        return adjMaturityLoan;
    }

    /**
     * Setter/Getter for ADJ_INT_LIEN - table Field
     */
    public void setAdjIntLien(String adjIntLien) {
        this.adjIntLien = adjIntLien;
    }

    public String getAdjIntLien() {
        return adjIntLien;
    }

    /**
     * Setter/Getter for ADJ_PRIN_LOAN_LIEN - table Field
     */
    public void setAdjPrinLoanLien(String adjPrinLoanLien) {
        this.adjPrinLoanLien = adjPrinLoanLien;
    }

    public String getAdjPrinLoanLien() {
        return adjPrinLoanLien;
    }

    /**
     * Setter/Getter for PREMATURE_WITHDRAWAL - table Field
     */
    public void setPrematureWithdrawal(Double prematureWithdrawal) {
        this.prematureWithdrawal = prematureWithdrawal;
    }

    public Double getPrematureWithdrawal() {
        return prematureWithdrawal;
    }

    /**
     * Setter/Getter for FLEXI_SB_CA - table Field
     */
    public void setFlexiSbCa(String flexiSbCa) {
        this.flexiSbCa = flexiSbCa;
    }

    public String getFlexiSbCa() {
        return flexiSbCa;
    }

    /**
     * Setter/Getter for TD_SECURITY_OD - table Field
     */
    public void setTdSecurityOd(String tdSecurityOd) {
        this.tdSecurityOd = tdSecurityOd;
    }

    public String getTdSecurityOd() {
        return tdSecurityOd;
    }

    /**
     * Setter/Getter for ACNUM_PATTERN - table Field
     */
    public void setAcnumPattern(String acnumPattern) {
        this.acnumPattern = acnumPattern;
    }

    public String getAcnumPattern() {
        return acnumPattern;
    }

    /**
     * Setter/Getter for ALPHA_SUFFIX_TDREC - table Field
     */
    public void setAlphaSuffixTdrec(String alphaSuffixTdrec) {
        this.alphaSuffixTdrec = alphaSuffixTdrec;
    }

    public String getAlphaSuffixTdrec() {
        return alphaSuffixTdrec;
    }

    /**
     * Setter/Getter for MAX_AMT_CASH - table Field
     */
    public void setMaxAmtCash(Double maxAmtCash) {
        this.maxAmtCash = maxAmtCash;
    }

    public Double getMaxAmtCash() {
        return maxAmtCash;
    }

    /**
     * Setter/Getter for MIN_AMT_PAN - table Field
     */
    public void setMinAmtPan(Double minAmtPan) {
        this.minAmtPan = minAmtPan;
    }

    public Double getMinAmtPan() {
        return minAmtPan;
    }

    /**
     * Setter/Getter for INTRO_REQUIRED - table Field
     */
    public void setIntroRequired(String introRequired) {
        this.introRequired = introRequired;
    }

    public String getIntroRequired() {
        return introRequired;
    }

    /**
     * Setter/Getter for SCHEME_INTRO_DT - table Field
     */
    public void setSchemeIntroDt(Date schemeIntroDt) {
        this.schemeIntroDt = schemeIntroDt;
    }

    public Date getSchemeIntroDt() {
        return schemeIntroDt;
    }

    /**
     * Setter/Getter for LIMIT_BULK_DEPOSIT - table Field
     */
    public void setLimitBulkDeposit(Double limitBulkDeposit) {
        this.limitBulkDeposit = limitBulkDeposit;
    }

    public Double getLimitBulkDeposit() {
        return limitBulkDeposit;
    }

    /**
     * Setter/Getter for AFTER_NO_DAYS - table Field
     */
    public void setAfterNoDays(Double afterNoDays) {
        this.afterNoDays = afterNoDays;
    }

    public Double getAfterNoDays() {
        return afterNoDays;
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
        strB.append(getTOString("calculateTds", calculateTds));
        strB.append(getTOString("payintDepMaturity", payintDepMaturity));
        strB.append(getTOString("maturityAmtRound", maturityAmtRound));
        strB.append(getTOString("roundoffCriteria", roundoffCriteria));
        strB.append(getTOString("intpaidAftermaturity", intpaidAftermaturity));
        strB.append(getTOString("aftermaturityInttype", aftermaturityInttype));
        strB.append(getTOString("aftermaturityIntrate", aftermaturityIntrate));
        strB.append(getTOString("depositPerUnit", depositPerUnit));
        strB.append(getTOString("minDepositPeriod", minDepositPeriod));
        strB.append(getTOString("maxDepositPeriod", maxDepositPeriod));
        strB.append(getTOString("periodMultiples", periodMultiples));
        strB.append(getTOString("minDepositAmt", minDepositAmt));
        strB.append(getTOString("maxDepositAmt", maxDepositAmt));
        strB.append(getTOString("amtMultiples", amtMultiples));
        strB.append(getTOString("calcMaturityValue", calcMaturityValue));
        strB.append(getTOString("transMaturedDep", transMaturedDep));
        strB.append(getTOString("afterDays", afterDays));
        strB.append(getTOString("advMaturityGenpd", advMaturityGenpd));
        strB.append(getTOString("provIntmaturedDep", provIntmaturedDep));
        strB.append(getTOString("intMaturedDep", intMaturedDep));
        strB.append(getTOString("partialWithdrawal", partialWithdrawal));
        strB.append(getTOString("noPartialWithdrawal", noPartialWithdrawal));
        strB.append(getTOString("maxAmtWithdrawal", maxAmtWithdrawal));
        strB.append(getTOString("maxNoWithdrawalYr", maxNoWithdrawalYr));
        strB.append(getTOString("minAmtWithdrawal", minAmtWithdrawal));
        strB.append(getTOString("withdrawalMulti", withdrawalMulti));
        strB.append(getTOString("withdrawalInterest", withdrawalInterest));
        strB.append(getTOString("serviceCharge", serviceCharge));
        strB.append(getTOString("serviceChargePer", serviceChargePer));
        strB.append(getTOString("extDepMaturity", extDepMaturity));
        strB.append(getTOString("adjMaturityLoan", adjMaturityLoan));
        strB.append(getTOString("adjIntLien", adjIntLien));
        strB.append(getTOString("adjPrinLoanLien", adjPrinLoanLien));
        strB.append(getTOString("prematureWithdrawal", prematureWithdrawal));
        strB.append(getTOString("flexiSbCa", flexiSbCa));
        strB.append(getTOString("tdSecurityOd", tdSecurityOd));
        strB.append(getTOString("acnumPattern", acnumPattern));
        strB.append(getTOString("alphaSuffixTdrec", alphaSuffixTdrec));
        strB.append(getTOString("maxAmtCash", maxAmtCash));
        strB.append(getTOString("minAmtPan", minAmtPan));
        strB.append(getTOString("introRequired", introRequired));
        strB.append(getTOString("schemeIntroDt", schemeIntroDt));
        strB.append(getTOString("certificatePrint", certificatePrint));
        strB.append(getTOString("limitBulkDeposit", limitBulkDeposit));
        strB.append(getTOString("afterNoDays", afterNoDays));
        strB.append(getTOString("discounted", discounted));
        strB.append(getTOString("schemeClosingDt", schemeClosingDt));
        strB.append(getTOString("typesOfDep", typesOfDep));
        strB.append(getTOString("staffAccount", staffAccount));
        strB.append(getTOString("rdoWithPeriod", rdoWithPeriod));
        strB.append(getTOString("rdoDoublingScheme", rdoDoublingScheme));
        strB.append(getTOString("fdRenewalSameNoTranPrincAmt", fdRenewalSameNoTranPrincAmt));
        strB.append(getTOString("doubligCount", doubligCount));
         strB.append(getTOString("agentcommSlabRequired", agentcommSlabRequired));
        strB.append(getTOString("agentCommCalcMethod", agentCommCalcMethod));
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
        strB.append(getTOXml("calculateTds", calculateTds));
        strB.append(getTOXml("payintDepMaturity", payintDepMaturity));
        strB.append(getTOXml("maturityAmtRound", maturityAmtRound));
        strB.append(getTOXml("roundoffCriteria", roundoffCriteria));
        strB.append(getTOXml("intpaidAftermaturity", intpaidAftermaturity));
        strB.append(getTOXml("aftermaturityInttype", aftermaturityInttype));
        strB.append(getTOXml("aftermaturityIntrate", aftermaturityIntrate));
        strB.append(getTOXml("depositPerUnit", depositPerUnit));
        strB.append(getTOXml("minDepositPeriod", minDepositPeriod));
        strB.append(getTOXml("maxDepositPeriod", maxDepositPeriod));
        strB.append(getTOXml("periodMultiples", periodMultiples));
        strB.append(getTOXml("minDepositAmt", minDepositAmt));
        strB.append(getTOXml("maxDepositAmt", maxDepositAmt));
        strB.append(getTOXml("amtMultiples", amtMultiples));
        strB.append(getTOXml("calcMaturityValue", calcMaturityValue));
        strB.append(getTOXml("transMaturedDep", transMaturedDep));
        strB.append(getTOXml("afterDays", afterDays));
        strB.append(getTOXml("advMaturityGenpd", advMaturityGenpd));
        strB.append(getTOXml("provIntmaturedDep", provIntmaturedDep));
        strB.append(getTOXml("intMaturedDep", intMaturedDep));
        strB.append(getTOXml("partialWithdrawal", partialWithdrawal));
        strB.append(getTOXml("noPartialWithdrawal", noPartialWithdrawal));
        strB.append(getTOXml("maxAmtWithdrawal", maxAmtWithdrawal));
        strB.append(getTOXml("maxNoWithdrawalYr", maxNoWithdrawalYr));
        strB.append(getTOXml("minAmtWithdrawal", minAmtWithdrawal));
        strB.append(getTOXml("withdrawalMulti", withdrawalMulti));
        strB.append(getTOXml("withdrawalInterest", withdrawalInterest));
        strB.append(getTOXml("serviceCharge", serviceCharge));
        strB.append(getTOXml("serviceChargePer", serviceChargePer));
        strB.append(getTOXml("extDepMaturity", extDepMaturity));
        strB.append(getTOXml("adjMaturityLoan", adjMaturityLoan));
        strB.append(getTOXml("adjIntLien", adjIntLien));
        strB.append(getTOXml("adjPrinLoanLien", adjPrinLoanLien));
        strB.append(getTOXml("prematureWithdrawal", prematureWithdrawal));
        strB.append(getTOXml("flexiSbCa", flexiSbCa));
        strB.append(getTOXml("tdSecurityOd", tdSecurityOd));
        strB.append(getTOXml("acnumPattern", acnumPattern));
        strB.append(getTOXml("alphaSuffixTdrec", alphaSuffixTdrec));
        strB.append(getTOXml("maxAmtCash", maxAmtCash));
        strB.append(getTOXml("minAmtPan", minAmtPan));
        strB.append(getTOXml("introRequired", introRequired));
        strB.append(getTOXml("schemeIntroDt", schemeIntroDt));
        strB.append(getTOXml("certificatePrint", certificatePrint));
        strB.append(getTOXml("limitBulkDeposit", limitBulkDeposit));
        strB.append(getTOXml("afterNoDays", afterNoDays));
        strB.append(getTOXml("discounted", discounted));
        strB.append(getTOXml("schemeClosingDt", schemeClosingDt));
        strB.append(getTOXml("typesOfDep", typesOfDep));
        strB.append(getTOXml("staffAccount", staffAccount));
        strB.append(getTOXml("rdoWithPeriod", rdoWithPeriod));
        strB.append(getTOXml("rdoDoublingScheme", rdoDoublingScheme));
        strB.append(getTOXml("fdRenewalSameNoTranPrincAmt", fdRenewalSameNoTranPrincAmt));
        strB.append(getTOXml("doubligCount", doubligCount));
        strB.append(getTOXml("agentcommSlabRequired", agentcommSlabRequired));
        strB.append(getTOXml("agentCommCalcMethod", agentCommCalcMethod));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property discounted.
     *
     * @return Value of property discounted.
     */
    public java.lang.String getDiscounted() {
        return discounted;
    }

    /**
     * Setter for property discounted.
     *
     * @param discounted New value of property discounted.
     */
    public void setDiscounted(java.lang.String discounted) {
        this.discounted = discounted;
    }

    /**
     * Getter for property schemeClosingDt.
     *
     * @return Value of property schemeClosingDt.
     */
    public java.util.Date getSchemeClosingDt() {
        return schemeClosingDt;
    }

    /**
     * Setter for property schemeClosingDt.
     *
     * @param schemeClosingDt New value of property schemeClosingDt.
     */
    public void setSchemeClosingDt(java.util.Date schemeClosingDt) {
        this.schemeClosingDt = schemeClosingDt;
    }

    /**
     * Getter for property typesOfDep.
     *
     * @return Value of property typesOfDep.
     */
    public java.lang.String getTypesOfDep() {
        return typesOfDep;
    }

    /**
     * Setter for property typesOfDep.
     *
     * @param typesOfDep New value of property typesOfDep.
     */
    public void setTypesOfDep(java.lang.String typesOfDep) {
        this.typesOfDep = typesOfDep;
    }

    /**
     * Getter for property staffAccount.
     *
     * @return Value of property staffAccount.
     */
    public java.lang.String getStaffAccount() {
        return staffAccount;
    }

    /**
     * Setter for property staffAccount.
     *
     * @param staffAccount New value of property staffAccount.
     */
    public void setStaffAccount(java.lang.String staffAccount) {
        this.staffAccount = staffAccount;
    }

    /**
     * Getter for property rdoWithPeriod.
     *
     * @return Value of property rdoWithPeriod.
     */
    public java.lang.String getRdoWithPeriod() {
        return rdoWithPeriod;
    }

    /**
     * Setter for property rdoWithPeriod.
     *
     * @param rdoWithPeriod New value of property rdoWithPeriod.
     */
    public void setRdoWithPeriod(java.lang.String rdoWithPeriod) {
        this.rdoWithPeriod = rdoWithPeriod;
    }

    /**
     * Getter for property rdoDoublingScheme.
     *
     * @return Value of property rdoDoublingScheme.
     */
    public java.lang.String getRdoDoublingScheme() {
        return rdoDoublingScheme;
    }

    /**
     * Setter for property rdoDoublingScheme.
     *
     * @param rdoDoublingScheme New value of property rdoDoublingScheme.
     */
    public void setRdoDoublingScheme(java.lang.String rdoDoublingScheme) {
        this.rdoDoublingScheme = rdoDoublingScheme;
    }

    /**
     * Getter for property certificatePrint.
     *
     * @return Value of property certificatePrint.
     */
    public java.lang.String getCertificatePrint() {
        return certificatePrint;
    }

    /**
     * Setter for property certificatePrint.
     *
     * @param certificatePrint New value of property certificatePrint.
     */
    public void setCertificatePrint(java.lang.String certificatePrint) {
        this.certificatePrint = certificatePrint;
    }

    /**
     * Getter for property serviceCharge.
     *
     * @return Value of property serviceCharge.
     */
    public java.lang.String getServiceCharge() {
        return serviceCharge;
    }

    /**
     * Setter for property serviceCharge.
     *
     * @param serviceCharge New value of property serviceCharge.
     */
    public void setServiceCharge(java.lang.String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    /**
     * Getter for property serviceChargePer.
     *
     * @return Value of property serviceChargePer.
     */
    public java.lang.Double getServiceChargePer() {
        return serviceChargePer;
    }

    /**
     * Setter for property serviceChargePer.
     *
     * @param serviceChargePer New value of property serviceChargePer.
     */
    public void setServiceChargePer(java.lang.Double serviceChargePer) {
        this.serviceChargePer = serviceChargePer;
    }

    /**
     * Getter for property fdRenewalSameNoTranPrincAmt.
     *
     * @return Value of property fdRenewalSameNoTranPrincAmt.
     */
    public java.lang.String getFdRenewalSameNoTranPrincAmt() {
        return fdRenewalSameNoTranPrincAmt;
    }

    /**
     * Setter for property fdRenewalSameNoTranPrincAmt.
     *
     * @param fdRenewalSameNoTranPrincAmt New value of property
     * fdRenewalSameNoTranPrincAmt.
     */
    public void setFdRenewalSameNoTranPrincAmt(java.lang.String fdRenewalSameNoTranPrincAmt) {
        this.fdRenewalSameNoTranPrincAmt = fdRenewalSameNoTranPrincAmt;
    }

    public String getExcludeLienStanding() {
        return excludeLienStanding;
    }

    public void setExcludeLienStanding(String excludeLienStanding) {
        this.excludeLienStanding = excludeLienStanding;
    }

    public String getExcludeLienIntrstAppl() {
        return excludeLienIntrstAppl;
    }

    public void setExcludeLienIntrstAppl(String excludeLienIntrstAppl) {
        this.excludeLienIntrstAppl = excludeLienIntrstAppl;
    }

    public String getCummCertPrint() {
        return cummCertPrint;
    }

    public void setCummCertPrint(String cummCertPrint) {
        this.cummCertPrint = cummCertPrint;
    }

    public String getDepositUnlien() {
        return depositUnlien;
    }

    public void setDepositUnlien(String depositUnlien) {
        this.depositUnlien = depositUnlien;
    }

    public String getAgentCommCalcMethod() {
        return agentCommCalcMethod;
    }

    public void setAgentCommCalcMethod(String agentCommCalcMethod) {
        this.agentCommCalcMethod = agentCommCalcMethod;
    }

    public String getAgentcommSlabRequired() {
        return agentcommSlabRequired;
    }

    public void setAgentcommSlabRequired(String agentcommSlabRequired) {
        this.agentcommSlabRequired = agentcommSlabRequired;
    }
    
    
}