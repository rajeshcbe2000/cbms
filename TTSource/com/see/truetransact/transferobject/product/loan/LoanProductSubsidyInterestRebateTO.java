/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanProductSubsidyInterestRebateTO.java
 *
 * Created on July 16, 2012, 4:40 PM
 */
package com.see.truetransact.transferobject.product.loan;

/**
 *
 * @author admin
 */
import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

public class LoanProductSubsidyInterestRebateTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String subsidyAllowed = "";
    private String loanBalance = "";
    private String subsidyReceivedDate = "";
    private String interestRebateAllowed = "";
    private String rebateCalculation = "";
    private String rebatePeriod = "";
    private Double interestRebatePercentage = 0.0;
    private Integer finYearStartingFromDD = 0;
    private Integer finYearStartingFromMM = 0;
    private String penalInterestWaiverAllowed = "";
    private String interestWaiverAllowed = "";
    private String principalWaiverAllowed = "";
    private String noticeWaiveOffAllowed = "";
    //added by risahd 23-04-2015
    private String arcWaiver="";
    private String legalWaiver="";
    private String insurenceWaiver="";
    private String arbitraryWaiver="";
    private String miscellaneousWaiver="";
    private String decreeWaiver="";
    private String advertiseWaiver="";
    private String postageWaiver="";
    private String epWaiver="";
    private String chkRebtSpl ="";
    private String overdueIntWaiver = "";
    private Double loanInteRebatePercent = 1.0; // Added by nithya on 11-01-2020 for KD-1234
    private String recoveryWaiver = "";
    private String measurementWaiver =  "";
    
    private String koleFieldOperationWaiver = "";
    private String koleFieldExpenseWaiver = "";

    public String getChkRebtSpl() {
        return chkRebtSpl;
    }

    public void setChkRebtSpl(String chkRebtSpl) {
        this.chkRebtSpl = chkRebtSpl;
    }
    
    public String getPrincipalWaiverAllowed() {
        return principalWaiverAllowed;
    }

    public void setPrincipalWaiverAllowed(String principalWaiverAllowed) {
        this.principalWaiverAllowed = principalWaiverAllowed;
    }
    private String status = "";

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("principalWaiverAllowed", principalWaiverAllowed));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("noticeWaiveOffAllowed", noticeWaiveOffAllowed));
        strB.append(getTOString("subsidyAllowed", subsidyAllowed));
        strB.append(getTOString("loanBalance", loanBalance));
        strB.append(getTOString("subsidyReceivedDate", subsidyReceivedDate));
        strB.append(getTOString("interestRebateAllowed", interestRebateAllowed));
        strB.append(getTOString("rebateCalculation", rebateCalculation));
        strB.append(getTOString("rebatePeriod", rebatePeriod));
        strB.append(getTOString("interestRebatePercentage", interestRebatePercentage));
        strB.append(getTOString("finYearStartingFromDD", finYearStartingFromDD));
        strB.append(getTOString("finYearStartingFromMM", finYearStartingFromMM));
        strB.append(getTOString("penalInterestWaiverAllowed", penalInterestWaiverAllowed));
        strB.append(getTOString("interestWaiverAllowed", interestWaiverAllowed));
        strB.append(getTOString("arcWaiver", arcWaiver));
        strB.append(getTOString("decreeWaiver",decreeWaiver));
        strB.append(getTOString("epWaiver", epWaiver));
        strB.append(getTOString("miscellaneousWaiver", miscellaneousWaiver));
        strB.append(getTOString("advertiseWaiver", advertiseWaiver));
        strB.append(getTOString("arbitraryWaiver", arbitraryWaiver));
        strB.append(getTOString("postageWaiver",postageWaiver));
        strB.append(getTOString("insurenceWaiver", insurenceWaiver));
        strB.append(getTOString("legalWaiver",legalWaiver));
        strB.append(getTOString("status", status));
        strB.append(getTOString("chkRebtSpl", chkRebtSpl));
        strB.append(getTOString("overdueIntWaiver", overdueIntWaiver));
        strB.append(getTOString("loanInteRebatePercent", loanInteRebatePercent)); // Added by nithya on 11-01-2020 for KD-1234
        strB.append(getTOString("recoveryWaiver", recoveryWaiver));
        strB.append(getTOString("measurementWaiver", measurementWaiver));
        strB.append(getTOString("koleFieldExpenseWaiver", koleFieldExpenseWaiver));
        strB.append(getTOString("koleFieldOperationWaiver", koleFieldOperationWaiver));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("principalWaiverAllowed", principalWaiverAllowed));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("noticeWaiveOffAllowed", noticeWaiveOffAllowed));
        strB.append(getTOXml("subsidyAllowed", subsidyAllowed));
        strB.append(getTOXml("loanBalance", loanBalance));
        strB.append(getTOXml("subsidyReceivedDate", subsidyReceivedDate));
        strB.append(getTOXml("interestRebateAllowed", interestRebateAllowed));
        strB.append(getTOXml("rebateCalculation", rebateCalculation));
        strB.append(getTOXml("rebatePeriod", rebatePeriod));
        strB.append(getTOXml("interestRebatePercentage", interestRebatePercentage));
        strB.append(getTOXml("finYearStartingFromDD", finYearStartingFromDD));
        strB.append(getTOXml("finYearStartingFromMM", finYearStartingFromMM));
        strB.append(getTOXml("penalInterestWaiverAllowed", penalInterestWaiverAllowed));
        strB.append(getTOXml("interestWaiverAllowed", interestWaiverAllowed));
        strB.append(getTOXml("arcWaiver", arcWaiver));
        strB.append(getTOXml("decreeWaiver", decreeWaiver));
        strB.append(getTOXml("epWaiver", epWaiver));
        strB.append(getTOXml("miscellaneousWaiver", miscellaneousWaiver));
        strB.append(getTOXml("advertiseWaiver", advertiseWaiver));
        strB.append(getTOXml("arbitraryWaiver", arbitraryWaiver));
        strB.append(getTOXml("postageWaiver", postageWaiver));
        strB.append(getTOXml("insurenceWaiver", insurenceWaiver));
        strB.append(getTOXml("legalWaiver", legalWaiver));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("chkRebtSpl", chkRebtSpl));
        strB.append(getTOXml("overdueIntWaiver", overdueIntWaiver));
        strB.append(getTOXml("recoveryWaiver", recoveryWaiver));
        strB.append(getTOXml("measurementWaiver", measurementWaiver));
        strB.append(getTOXml("loanInteRebatePercent", loanInteRebatePercent));// Added by nithya on 11-01-2020 for KD-1234
        strB.append(getTOXml("koleFieldExpenseWaiver", koleFieldExpenseWaiver));
        strB.append(getTOXml("koleFieldOperationWaiver", koleFieldOperationWaiver));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getKeyData() {
        setKeyColumns("prodId");
        return prodId;
    }

    public String getAdvertiseWaiver() {
        return advertiseWaiver;
    }

    public void setAdvertiseWaiver(String advertiseWaiver) {
        this.advertiseWaiver = advertiseWaiver;
    }

    public String getArbitraryWaiver() {
        return arbitraryWaiver;
    }

    public void setArbitraryWaiver(String arbitraryWaiver) {
        this.arbitraryWaiver = arbitraryWaiver;
    }

    public String getArcWaiver() {
        return arcWaiver;
    }

    public void setArcWaiver(String arcWaiver) {
        this.arcWaiver = arcWaiver;
    }

    public String getDecreeWaiver() {
        return decreeWaiver;
    }

    public void setDecreeWaiver(String decreeWaiver) {
        this.decreeWaiver = decreeWaiver;
    }

    public String getEpWaiver() {
        return epWaiver;
    }

    public void setEpWaiver(String epWaiver) {
        this.epWaiver = epWaiver;
    }

    public String getInsurenceWaiver() {
        return insurenceWaiver;
    }

    public void setInsurenceWaiver(String insurenceWaiver) {
        this.insurenceWaiver = insurenceWaiver;
    }

    public String getLegalWaiver() {
        return legalWaiver;
    }

    public void setLegalWaiver(String legalWaiver) {
        this.legalWaiver = legalWaiver;
    }

    public String getMiscellaneousWaiver() {
        return miscellaneousWaiver;
    }

    public void setMiscellaneousWaiver(String miscellaneousWaiver) {
        this.miscellaneousWaiver = miscellaneousWaiver;
    }

    public String getPostageWaiver() {
        return postageWaiver;
    }

    public void setPostageWaiver(String postageWaiver) {
        this.postageWaiver = postageWaiver;
    }

    /**
     * Getter for property prodId.
     *
     * @return Value of property prodId.
     */
    
    
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     *
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }

    /**
     * Getter for property loanBalance.
     *
     * @return Value of property loanBalance.
     */
    public java.lang.String getLoanBalance() {
        return loanBalance;
    }

    /**
     * Setter for property loanBalance.
     *
     * @param loanBalance New value of property loanBalance.
     */
    public void setLoanBalance(java.lang.String loanBalance) {
        this.loanBalance = loanBalance;
    }

    /**
     * Getter for property subsidyReceivedDate.
     *
     * @return Value of property subsidyReceivedDate.
     */
    public java.lang.String getSubsidyReceivedDate() {
        return subsidyReceivedDate;
    }

    /**
     * Setter for property subsidyReceivedDate.
     *
     * @param subsidyReceivedDate New value of property subsidyReceivedDate.
     */
    public void setSubsidyReceivedDate(java.lang.String subsidyReceivedDate) {
        this.subsidyReceivedDate = subsidyReceivedDate;
    }

    /**
     * Getter for property interestRebateAllowed.
     *
     * @return Value of property interestRebateAllowed.
     */
    public java.lang.String getInterestRebateAllowed() {
        return interestRebateAllowed;
    }

    /**
     * Setter for property interestRebateAllowed.
     *
     * @param interestRebateAllowed New value of property interestRebateAllowed.
     */
    public void setInterestRebateAllowed(java.lang.String interestRebateAllowed) {
        this.interestRebateAllowed = interestRebateAllowed;
    }

    public Double getInterestRebatePercentage() {
        return interestRebatePercentage;
    }

    public void setInterestRebatePercentage(Double interestRebatePercentage) {
        this.interestRebatePercentage = interestRebatePercentage;
    }

    public Integer getFinYearStartingFromDD() {
        return finYearStartingFromDD;
    }

    public void setFinYearStartingFromDD(Integer finYearStartingFromDD) {
        this.finYearStartingFromDD = finYearStartingFromDD;
    }

    public Integer getFinYearStartingFromMM() {
        return finYearStartingFromMM;
    }

    public void setFinYearStartingFromMM(Integer finYearStartingFromMM) {
        this.finYearStartingFromMM = finYearStartingFromMM;
    }

    

    /**
     * Getter for property penalInterestWaiverAllowed.
     *
     * @return Value of property penalInterestWaiverAllowed.
     */
    public java.lang.String getPenalInterestWaiverAllowed() {
        return penalInterestWaiverAllowed;
    }

    /**
     * Setter for property penalInterestWaiverAllowed.
     *
     * @param penalInterestWaiverAllowed New value of property
     * penalInterestWaiverAllowed.
     */
    public void setPenalInterestWaiverAllowed(java.lang.String penalInterestWaiverAllowed) {
        this.penalInterestWaiverAllowed = penalInterestWaiverAllowed;
    }

    /**
     * Getter for property interestWaiverAllowed.
     *
     * @return Value of property interestWaiverAllowed.
     */
    public java.lang.String getInterestWaiverAllowed() {
        return interestWaiverAllowed;
    }

    /**
     * Setter for property interestWaiverAllowed.
     *
     * @param interestWaiverAllowed New value of property interestWaiverAllowed.
     */
    public void setInterestWaiverAllowed(java.lang.String interestWaiverAllowed) {
        this.interestWaiverAllowed = interestWaiverAllowed;
    }

    /**
     * Getter for property subsidyAllowed.
     *
     * @return Value of property subsidyAllowed.
     */
    public java.lang.String getSubsidyAllowed() {
        return subsidyAllowed;
    }

    /**
     * Setter for property subsidyAllowed.
     *
     * @param subsidyAllowed New value of property subsidyAllowed.
     */
    public void setSubsidyAllowed(java.lang.String subsidyAllowed) {
        this.subsidyAllowed = subsidyAllowed;
    }

    /**
     * Getter for property rebateCalculation.
     *
     * @return Value of property rebateCalculation.
     */
    public java.lang.String getRebateCalculation() {
        return rebateCalculation;
    }

    /**
     * Setter for property rebateCalculation.
     *
     * @param rebateCalculation New value of property rebateCalculation.
     */
    public void setRebateCalculation(java.lang.String rebateCalculation) {
        this.rebateCalculation = rebateCalculation;
    }

    /**
     * Getter for property rebatePeriod.
     *
     * @return Value of property rebatePeriod.
     */
    public java.lang.String getRebatePeriod() {
        return rebatePeriod;
    }

    /**
     * Setter for property rebatePeriod.
     *
     * @param rebatePeriod New value of property rebatePeriod.
     */
    public void setRebatePeriod(java.lang.String rebatePeriod) {
        this.rebatePeriod = rebatePeriod;
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

    public String getNoticeWaiveOffAllowed() {
        return noticeWaiveOffAllowed;
    }

    public void setNoticeWaiveOffAllowed(String noticeWaiveOffAllowed) {
        this.noticeWaiveOffAllowed = noticeWaiveOffAllowed;
    }

    public String getOverdueIntWaiver() {
        return overdueIntWaiver;
    }

    public void setOverdueIntWaiver(String overdueIntWaiver) {
        this.overdueIntWaiver = overdueIntWaiver;
    }

    public Double getLoanInteRebatePercent() { // Added by nithya on 11-01-2020 for KD-1234
        return loanInteRebatePercent;
    }

    public void setLoanInteRebatePercent(Double loanInteRebatePercent) {
        this.loanInteRebatePercent = loanInteRebatePercent;
    }

    public String getMeasurementWaiver() {
        return measurementWaiver;
    }

    public void setMeasurementWaiver(String measurementWaiver) {
        this.measurementWaiver = measurementWaiver;
    }

    public String getRecoveryWaiver() {
        return recoveryWaiver;
    }

    public void setRecoveryWaiver(String recoveryWaiver) {
        this.recoveryWaiver = recoveryWaiver;
    }

    public String getKoleFieldOperationWaiver() {
        return koleFieldOperationWaiver;
    }

    public void setKoleFieldOperationWaiver(String koleFieldOperationWaiver) {
        this.koleFieldOperationWaiver = koleFieldOperationWaiver;
    }

    public String getKoleFieldExpenseWaiver() {
        return koleFieldExpenseWaiver;
    }

    public void setKoleFieldExpenseWaiver(String koleFieldExpenseWaiver) {
        this.koleFieldExpenseWaiver = koleFieldExpenseWaiver;
    }
    
}
