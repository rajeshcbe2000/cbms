/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductAccountParamTO.java
 * 
 * Created on Wed Aug 11 16:05:19 IST 2004
 */
package com.see.truetransact.transferobject.product.loan;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_ACPARAM.
 */
public class LoanProductAccountParamTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String chkEMISimpleInt = "";
    private String numberPattern = "";
    private String lastAcNo = "";
    private String limitDefAllowed = "";
    private String staffAcOpened = "";
    private String debitIntClearingappl = "";
    private String numberPatternSuffix = "";
    private String auctionAmt = "";
    private String gldRenewOldAmt = "";
    private String gldRenewCash = "";
    private String gldRenewMarketRate = "";
    private String gldRnwNwIntRate = "";
    private String interestOnMaturity = "";
    private String isCreditAllowedToPricipal = "";
    private String isDisbAftMoraPerd = "";
    private String isInterestFirst = "";
    private Double maxCashPayment = 0.0;
    private String chkAuctionAllowed = "";
    private String suspenseCreditAchd = "";
    private String suspenseDebitAchd = "";   
    private Integer freqInDays = 0;
    private String isSalaryRecovery = "";
    private String isIntCalcPeriodWise = "";// Added by nithya on 15-11-2017 for 7867
    /*
     this variable for 
     */
    private String isInterestLoanDue="";

    public String getIsInterestLoanDue() {
        return isInterestLoanDue;
    }

    public void setIsInterestLoanDue(String isInterestLoanDue) {
        this.isInterestLoanDue = isInterestLoanDue;
    }

    public String getIsSalaryRecovery() {
        return isSalaryRecovery;
    }

    public void setIsSalaryRecovery(String isSalaryRecovery) {
        this.isSalaryRecovery = isSalaryRecovery;
    }

    public Integer getFreqInDays() {
        return freqInDays;
    }

    public void setFreqInDays(Integer freqInDays) {
        this.freqInDays = freqInDays;
    }

    public String getIsInterestFirst() {
        return isInterestFirst;
    }

    public void setIsInterestFirst(String isInterestFirst) {
        this.isInterestFirst = isInterestFirst;
    }
    
    public String getIsDisbAftMoraPerd() {
        return isDisbAftMoraPerd;
    }

    public String getChkEMISimpleInt() {
        return chkEMISimpleInt;
    }

    public void setChkEMISimpleInt(String chkEMISimpleInt) {
        this.chkEMISimpleInt = chkEMISimpleInt;
    }
 	public String getChkAuctionAllowed() {
        return chkAuctionAllowed;
    }

    public void setChkAuctionAllowed(String chkAuctionAllowed) {
        this.chkAuctionAllowed = chkAuctionAllowed;
    }
    public void setIsDisbAftMoraPerd(String isDisbAftMoraPerd) {
        this.isDisbAftMoraPerd = isDisbAftMoraPerd;
    }

    public String getInterestOnMaturity() {
        return interestOnMaturity;
    }

    public void setInterestOnMaturity(String InterestOnMaturity) {
        this.interestOnMaturity = InterestOnMaturity;
    }
    public String getGldRnwNwIntRate() {
        return gldRnwNwIntRate;
    }

    public void setGldRnwNwIntRate(String gldRnwNwIntRate) {
        this.gldRnwNwIntRate = gldRnwNwIntRate;
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
     * Setter/Getter for NUMBER_PATTERN - table Field
     */
    public void setNumberPattern(String numberPattern) {
        this.numberPattern = numberPattern;
    }

    public String getNumberPattern() {
        return numberPattern;
    }

    public Double getMaxCashPayment() {
        return maxCashPayment;
    }

    public void setMaxCashPayment(Double maxCashPayment) {
        this.maxCashPayment = maxCashPayment;
    }

    

    /**
     * Setter/Getter for LAST_AC_NO - table Field
     */
    public void setLastAcNo(String lastAcNo) {
        this.lastAcNo = lastAcNo;
    }

    public String getLastAcNo() {
        return lastAcNo;
    }

    /**
     * Setter/Getter for LIMIT_DEF_ALLOWED - table Field
     */
    public void setLimitDefAllowed(String limitDefAllowed) {
        this.limitDefAllowed = limitDefAllowed;
    }

    public String getLimitDefAllowed() {
        return limitDefAllowed;
    }

    /**
     * Setter/Getter for STAFF_AC_OPENED - table Field
     */
    public void setStaffAcOpened(String staffAcOpened) {
        this.staffAcOpened = staffAcOpened;
    }

    public String getStaffAcOpened() {
        return staffAcOpened;
    }

    /**
     * Setter/Getter for DEBIT_INT_CLEARINGAPPL - table Field
     */
    public void setDebitIntClearingappl(String debitIntClearingappl) {
        this.debitIntClearingappl = debitIntClearingappl;
    }

    public String getDebitIntClearingappl() {
        return debitIntClearingappl;
    }

    /**
     * Setter/Getter for NUMBER_PATTERN_SUFFIX - table Field
     */
    public void setNumberPatternSuffix(String numberPatternSuffix) {
        this.numberPatternSuffix = numberPatternSuffix;
    }

    public String getNumberPatternSuffix() {
        return numberPatternSuffix;
    }

    public String getIsCreditAllowedToPricipal() {
        return isCreditAllowedToPricipal;
    }

    public void setIsCreditAllowedToPricipal(String IsCreditAllowedToPricipal) {
        this.isCreditAllowedToPricipal = IsCreditAllowedToPricipal;
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
        strB.append(getTOString("numberPattern", numberPattern));
        strB.append(getTOString("lastAcNo", lastAcNo));
        strB.append(getTOString("limitDefAllowed", limitDefAllowed));
        strB.append(getTOString("staffAcOpened", staffAcOpened));
        strB.append(getTOString("debitIntClearingappl", debitIntClearingappl));
        strB.append(getTOString("numberPatternSuffix", numberPatternSuffix));
        strB.append(getTOString("auctionAmt", auctionAmt));
        strB.append(getTOString("gldRenewOldAmt", gldRenewOldAmt));
        strB.append(getTOString("gldRenewCash", gldRenewCash));
        strB.append(getTOString("gldRenewMarketRate", gldRenewMarketRate));
        strB.append(getTOString("isCreditAllowedToPricipal", isCreditAllowedToPricipal));
        strB.append(getTOString("interestOnMaturity", interestOnMaturity));
        strB.append(getTOString("isDisbAftMoraPerd", isDisbAftMoraPerd));
        strB.append(getTOString("maxCashPayment", maxCashPayment));
        strB.append(getTOString("chkEMISimpleInt", chkEMISimpleInt));
        strB.append(getTOString("chkAuctionAllowed", chkAuctionAllowed));
        strB.append(getTOString("suspenseCreditAchd", suspenseCreditAchd));
        strB.append(getTOString("suspenseDebitAchd", suspenseDebitAchd));
        strB.append(getTOString("isInterestFirst", isInterestFirst));
        strB.append(getTOString("freqInDays", freqInDays));
        strB.append(getTOString("isSalaryRecovery", isSalaryRecovery));
        strB.append(getTOString("isInterestLoanDue", isInterestLoanDue));
        strB.append(getTOString("isIntCalcPeriodWise", isIntCalcPeriodWise));
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
        strB.append(getTOXml("numberPattern", numberPattern));
        strB.append(getTOXml("lastAcNo", lastAcNo));
        strB.append(getTOXml("limitDefAllowed", limitDefAllowed));
        strB.append(getTOXml("staffAcOpened", staffAcOpened));
        strB.append(getTOXml("debitIntClearingappl", debitIntClearingappl));
        strB.append(getTOXml("numberPatternSuffix", numberPatternSuffix));
        strB.append(getTOXml("auctionAmt", auctionAmt));
        strB.append(getTOXml("gldRenewOldAmt", gldRenewOldAmt));
        strB.append(getTOXml("gldRenewCash", gldRenewCash));
        strB.append(getTOXml("gldRenewMarketRate", gldRenewMarketRate));
        strB.append(getTOXml("isCreditAllowedToPricipal", isCreditAllowedToPricipal));
        strB.append(getTOXml("interestOnMaturity", interestOnMaturity));
        strB.append(getTOXml("isDisbAftMoraPerd", isDisbAftMoraPerd));
        strB.append(getTOXml("maxCashPayment", maxCashPayment));
        strB.append(getTOXml("chkEMISimpleInt", chkEMISimpleInt));
        strB.append(getTOXml("chkAuctionAllowed", chkAuctionAllowed));
        strB.append(getTOXml("suspenseCreditAchd", suspenseCreditAchd));
        strB.append(getTOXml("suspenseDebitAchd", suspenseDebitAchd));
        strB.append(getTOXml("isInterestFirst", isInterestFirst));
        strB.append(getTOXml("freqInDays", freqInDays));
        strB.append(getTOXml("isSalaryRecovery", isSalaryRecovery));
        strB.append(getTOXml("isInterestLoanDue", isInterestLoanDue));
        strB.append(getTOXml("isIntCalcPeriodWise", isIntCalcPeriodWise));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property auctionAmt.
     *
     * @return Value of property auctionAmt.
     */
    public String getAuctionAmt() {
        return auctionAmt;
    }

    public String getSuspenseCreditAchd() {
        return suspenseCreditAchd;
    }

    public void setSuspenseCreditAchd(String suspenseCreditAchd) {
        this.suspenseCreditAchd = suspenseCreditAchd;
    }

    public String getSuspenseDebitAchd() {
        return suspenseDebitAchd;
    }

    public void setSuspenseDebitAchd(String suspenseDebitAchd) {
        this.suspenseDebitAchd = suspenseDebitAchd;
    }

    /**
     * Setter for property auctionAmt.
     *
     * @param auctionAmt New value of property auctionAmt.
     */
    public void setAuctionAmt(String auctionAmt) {
        this.auctionAmt = auctionAmt;
    }

    public String getGldRenewOldAmt() {
        return gldRenewOldAmt;
    }

    public void setGldRenewOldAmt(String gldRenewOldAmt) {
        this.gldRenewOldAmt = gldRenewOldAmt;
    }

    public String getGldRenewCash() {
        return gldRenewCash;
    }

    public void setGldRenewCash(String gldRenewCash) {
        this.gldRenewCash = gldRenewCash;
    }

    public String getGldRenewMarketRate() {
        return gldRenewMarketRate;
    }

    public void setGldRenewMarketRate(String gldRenewMarketRate) {
        this.gldRenewMarketRate = gldRenewMarketRate;
    }

    public String getIsIntCalcPeriodWise() {
        return isIntCalcPeriodWise;
    }

    public void setIsIntCalcPeriodWise(String isIntCalcPeriodWise) {
        this.isIntCalcPeriodWise = isIntCalcPeriodWise;
    }   
    
}