/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductAccHeadTO.java
 *
 * Created on Fri Aug 13 11:23:46 IST 2004
 */
package com.see.truetransact.transferobject.product.loan;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_ACHD.
 */
public class LoanProductAccHeadTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String acClosingChrg = "";
    private String miscServChrg = "";
    private String statChrg = "";
    private String acDebitInt = "";
    private String penalInt = "";
    private String acCreditInt = "";
    private String expiryInt = "";
    private String chqRetChrgOutward = "";
    private String chqRetChrgInward = "";
    private String folioChrgAc = "";
    private String commitmentChrg = "";
    private String procChrg = "";
    private String postageCharges = "";
    private String noticeCharges = "";
    private String intPayableAcHd = "";
    private String legalCharges = "";
//    private String miscCharges="";
    private String arbitraryCharges = "";
    private String insuranceCharges = "";
    private String penalWaiveOff = "";
    private String noticeWaiveOff = "";
    private String principleWaveOff = "";
    private String executionDecreeCharges = "";
    private String arcCost = "";
    private String arcExpense = "";
    private String eaCost = "";
    private String eaExpense = "";
    private String epCost = "";
    private String epExpense = "";
    private String debitIntDiscountAchd = "";
    private String rebateInterest = "";
    private String stampAdvancesHead = "";
    private String noticeAdvancesHead = "";
    private String advertisementHead = "";
    private String arcEpSuspenceHead = "";
    private String othrChrgsHead = "";
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
    private String overDueWaiver = "";
    private String overDueIntHead = "";
    private String recoveryCharges = "";
    private String recoveryWaiver = "";
    private String measurementCharges = "";
    private String measurementWaiver = "";
    
    private String koleFieldOperation = "";
    private String koleFieldOperationWaiver = "";
    private String koleFieldexpense = "";
    private String koleFieldExpenseWaiver = "";
    

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
   
   

    public String getOthrChrgsHead() {
        return othrChrgsHead;
    }

    public void setOthrChrgsHead(String othrChrgsHead) {
        this.othrChrgsHead = othrChrgsHead;
    }

   

   
    public String getPenalWaiveOff() {
        return penalWaiveOff;
    }

    public void setPenalWaiveOff(String penalWaiveOff) {
        this.penalWaiveOff = penalWaiveOff;
    }

    public String getPrincipleWaveOff() {
        return principleWaveOff;
    }

    public void setPrincipleWaveOff(String principleWaveOff) {
        this.principleWaveOff = principleWaveOff;
    }

    public String getNoticeWaiveOff() {
        return noticeWaiveOff;
    }

    public void setNoticeWaiveOff(String noticeWaiveOff) {
        this.noticeWaiveOff = noticeWaiveOff;
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
     * Setter/Getter for AC_CLOSING_CHRG - table Field
     */
    public void setAcClosingChrg(String acClosingChrg) {
        this.acClosingChrg = acClosingChrg;
    }

    public String getAcClosingChrg() {
        return acClosingChrg;
    }

    /**
     * Setter/Getter for MISC_SERV_CHRG - table Field
     */
    public void setMiscServChrg(String miscServChrg) {
        this.miscServChrg = miscServChrg;
    }

    public String getMiscServChrg() {
        return miscServChrg;
    }

    /**
     * Setter/Getter for STAT_CHRG - table Field
     */
    public void setStatChrg(String statChrg) {
        this.statChrg = statChrg;
    }

    public String getStatChrg() {
        return statChrg;
    }

    /**
     * Setter/Getter for AC_DEBIT_INT - table Field
     */
    public void setAcDebitInt(String acDebitInt) {
        this.acDebitInt = acDebitInt;
    }

    public String getAcDebitInt() {
        return acDebitInt;
    }

    /**
     * Setter/Getter for PENAL_INT - table Field
     */
    public void setPenalInt(String penalInt) {
        this.penalInt = penalInt;
    }

    public String getPenalInt() {
        return penalInt;
    }

    /**
     * Setter/Getter for AC_CREDIT_INT - table Field
     */
    public void setAcCreditInt(String acCreditInt) {
        this.acCreditInt = acCreditInt;
    }

    public String getAcCreditInt() {
        return acCreditInt;
    }

    /**
     * Setter/Getter for EXPIRY_INT - table Field
     */
    public void setExpiryInt(String expiryInt) {
        this.expiryInt = expiryInt;
    }

    public String getExpiryInt() {
        return expiryInt;
    }

    /**
     * Setter/Getter for CHQ_RET_CHRG_OUTWARD - table Field
     */
    public void setChqRetChrgOutward(String chqRetChrgOutward) {
        this.chqRetChrgOutward = chqRetChrgOutward;
    }

    public String getChqRetChrgOutward() {
        return chqRetChrgOutward;
    }

    /**
     * Setter/Getter for CHQ_RET_CHRG_INWARD - table Field
     */
    public void setChqRetChrgInward(String chqRetChrgInward) {
        this.chqRetChrgInward = chqRetChrgInward;
    }

    public String getChqRetChrgInward() {
        return chqRetChrgInward;
    }

    /**
     * Setter/Getter for FOLIO_CHRG_AC - table Field
     */
    public void setFolioChrgAc(String folioChrgAc) {
        this.folioChrgAc = folioChrgAc;
    }

    public String getFolioChrgAc() {
        return folioChrgAc;
    }

    /**
     * Setter/Getter for COMMITMENT_CHRG - table Field
     */
    public void setCommitmentChrg(String commitmentChrg) {
        this.commitmentChrg = commitmentChrg;
    }

    public String getCommitmentChrg() {
        return commitmentChrg;
    }

    /**
     * Setter/Getter for PROC_CHRG - table Field
     */
    public void setProcChrg(String procChrg) {
        this.procChrg = procChrg;
    }

    public String getProcChrg() {
        return procChrg;
    }

    public String getOverDueIntHead() {
        return overDueIntHead;
    }

    public void setOverDueIntHead(String overDueIntHead) {
        this.overDueIntHead = overDueIntHead;
    }

    public String getOverDueWaiver() {
        return overDueWaiver;
    }

    public void setOverDueWaiver(String overDueWaiver) {
        this.overDueWaiver = overDueWaiver;
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
        strB.append(getTOString("acClosingChrg", acClosingChrg));
        strB.append(getTOString("miscServChrg", miscServChrg));
        strB.append(getTOString("statChrg", statChrg));
        strB.append(getTOString("acDebitInt", acDebitInt));
        strB.append(getTOString("penalInt", penalInt));
        strB.append(getTOString("acCreditInt", acCreditInt));
        strB.append(getTOString("expiryInt", expiryInt));
        strB.append(getTOString("chqRetChrgOutward", chqRetChrgOutward));
        strB.append(getTOString("chqRetChrgInward", chqRetChrgInward));
        strB.append(getTOString("folioChrgAc", folioChrgAc));
        strB.append(getTOString("commitmentChrg", commitmentChrg));
        strB.append(getTOString("procChrg", procChrg));
        strB.append(getTOString("postageCharges", postageCharges));
        strB.append(getTOString("penalWaiveOff", penalWaiveOff));
        strB.append(getTOString("principleWaveOff", principleWaveOff));
        strB.append(getTOString("noticeWaiveOff", noticeWaiveOff));
        System.out.println("strB" + strB + principleWaveOff);
        strB.append(getTOString("noticeCharges", noticeCharges));
        strB.append(getTOString("intPayableAcHd", intPayableAcHd));
        strB.append(getTOString("legalCharges", legalCharges));
//        strB.append(getTOString("miscCharges",miscCharges));
        strB.append(getTOString("arbitraryCharges", arbitraryCharges));
        strB.append(getTOString("insuranceCharges", insuranceCharges));
        strB.append(getTOString("executionDecreeCharges", executionDecreeCharges));
        strB.append(getTOString("arcCost", arcCost));
        strB.append(getTOString("arcExpense", arcExpense));
        strB.append(getTOString("eaCost", eaCost));
        strB.append(getTOString("eaExpense", eaExpense));
        strB.append(getTOString("epCost", epCost));
        strB.append(getTOString("epExpense", epExpense));
        strB.append(getTOString("debitIntDiscountAchd", debitIntDiscountAchd));
        strB.append(getTOString("rebateInterest", rebateInterest));
        strB.append(getTOString("stampAdvancesHead", stampAdvancesHead));
        strB.append(getTOString("advertisementHead", advertisementHead));
        strB.append(getTOString("arcEpSuspenceHead", arcEpSuspenceHead));
        strB.append(getTOString("arcWaiver", arcWaiver));
        strB.append(getTOString("decreeWaiver", decreeWaiver));
        strB.append(getTOString("epWaiver", epWaiver));
        strB.append(getTOString("miscellaneousWaiver", miscellaneousWaiver));
        strB.append(getTOString("advertiseWaiver", advertiseWaiver));
        strB.append(getTOString("arbitraryWaiver", arbitraryWaiver));
        strB.append(getTOString("postageWaiver", postageWaiver));
        strB.append(getTOString("insurenceWaiver", insurenceWaiver));
        strB.append(getTOString("legalWaiver", legalWaiver));
        strB.append(getTOString("overDueWaiver", overDueWaiver));
        strB.append(getTOString("overDueIntHead", overDueIntHead));
        strB.append(getTOString("recoveryCharges", recoveryCharges));
        strB.append(getTOString("recoveryWaiver", recoveryWaiver));
        strB.append(getTOString("measurementCharges", measurementCharges));
        strB.append(getTOString("measurementWaiver", measurementWaiver));
        strB.append(getTOString("koleFieldExpenseWaiver", koleFieldExpenseWaiver));
        strB.append(getTOString("koleFieldOperation", koleFieldOperation));
        strB.append(getTOString("koleFieldOperationWaiver", koleFieldOperationWaiver));
        strB.append(getTOString("koleFieldexpense", koleFieldexpense));
       
        
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
        strB.append(getTOXml("acClosingChrg", acClosingChrg));
        strB.append(getTOXml("miscServChrg", miscServChrg));
        strB.append(getTOXml("statChrg", statChrg));
        strB.append(getTOXml("acDebitInt", acDebitInt));
        strB.append(getTOXml("penalInt", penalInt));
        strB.append(getTOXml("acCreditInt", acCreditInt));
        strB.append(getTOXml("expiryInt", expiryInt));
        strB.append(getTOXml("chqRetChrgOutward", chqRetChrgOutward));
        strB.append(getTOXml("chqRetChrgInward", chqRetChrgInward));
        strB.append(getTOXml("folioChrgAc", folioChrgAc));
        strB.append(getTOXml("commitmentChrg", commitmentChrg));
        strB.append(getTOXml("procChrg", procChrg));
        strB.append(getTOXml("postageCharges", postageCharges));
        strB.append(getTOXml("penalWaiveOff", penalWaiveOff));
        strB.append(getTOXml("principleWaveOff", principleWaveOff));
        strB.append(getTOXml("noticeWaiveOff", noticeWaiveOff));
        strB.append(getTOXml("noticeCharges", noticeCharges));
        strB.append(getTOXml("intPayableAcHd", intPayableAcHd));
        strB.append(getTOXml("legalCharges", legalCharges));
//        strB.append(getTOXml("miscCharges",miscCharges));
        strB.append(getTOXml("arbitraryCharges", arbitraryCharges));
        strB.append(getTOXml("insuranceCharges", insuranceCharges));
        strB.append(getTOXml("executionDecreeCharges", executionDecreeCharges));
        strB.append(getTOXml("arcCost", arcCost));
        strB.append(getTOXml("arcExpense", arcExpense));
        strB.append(getTOXml("eaCost", eaCost));
        strB.append(getTOXml("eaExpense", eaExpense));
        strB.append(getTOXml("epCost", epCost));
        strB.append(getTOXml("epExpense", epExpense));
        strB.append(getTOXml("debitIntDiscountAchd", debitIntDiscountAchd));
        strB.append(getTOXml("rebateInterest", rebateInterest));
        strB.append(getTOXml("stampAdvancesHead", stampAdvancesHead));
        strB.append(getTOXml("advertisementHead", advertisementHead));
        strB.append(getTOXml("arcEpSuspenceHead", arcEpSuspenceHead));
        strB.append(getTOXml("arcWaiver", arcWaiver));
        strB.append(getTOXml("decreeWaiver", decreeWaiver));
        strB.append(getTOXml("epWaiver", epWaiver));
        strB.append(getTOXml("miscellaneousWaiver", miscellaneousWaiver));
        strB.append(getTOXml("advertiseWaiver", advertiseWaiver));
        strB.append(getTOXml("arbitraryWaiver", arbitraryWaiver));
        strB.append(getTOXml("postageWaiver", postageWaiver));
        strB.append(getTOXml("insurenceWaiver", insurenceWaiver));
        strB.append(getTOXml("legalWaiver", legalWaiver));
        strB.append(getTOXml("overDueWaiver", overDueWaiver));
        strB.append(getTOXml("overDueIntHead", overDueIntHead));
        strB.append(getTOXml("recoveryCharges", recoveryCharges));
        strB.append(getTOXml("recoveryWaiver", recoveryWaiver));
        strB.append(getTOXml("measurementCharges", measurementCharges));
        strB.append(getTOXml("measurementWaiver", measurementWaiver));
        
        strB.append(getTOXml("koleFieldExpenseWaiver", koleFieldExpenseWaiver));
        strB.append(getTOXml("koleFieldOperation", koleFieldOperation));
        strB.append(getTOXml("koleFieldOperationWaiver", koleFieldOperationWaiver));
        strB.append(getTOXml("koleFieldexpense", koleFieldexpense));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property postageCharges.
     *
     * @return Value of property postageCharges.
     */
    public java.lang.String getPostageCharges() {
        return postageCharges;
    }

    /**
     * Setter for property postageCharges.
     *
     * @param postageCharges New value of property postageCharges.
     */
    public void setPostageCharges(java.lang.String postageCharges) {
        this.postageCharges = postageCharges;
    }

    /**
     * Getter for property noticeCharges.
     *
     * @return Value of property noticeCharges.
     */
    public java.lang.String getNoticeCharges() {
        return noticeCharges;
    }

    /**
     * Setter for property noticeCharges.
     *
     * @param noticeCharges New value of property noticeCharges.
     */
    public void setNoticeCharges(java.lang.String noticeCharges) {
        this.noticeCharges = noticeCharges;
    }

    /**
     * Getter for property intPayableAcHd.
     *
     * @return Value of property intPayableAcHd.
     */
    public java.lang.String getIntPayableAcHd() {
        return intPayableAcHd;
    }

    /**
     * Setter for property intPayableAcHd.
     *
     * @param intPayableAcHd New value of property intPayableAcHd.
     */
    public void setIntPayableAcHd(java.lang.String intPayableAcHd) {
        this.intPayableAcHd = intPayableAcHd;
    }

    /**
     * Getter for property legalCharges.
     *
     * @return Value of property legalCharges.
     */
    public java.lang.String getLegalCharges() {
        return legalCharges;
    }

    /**
     * Setter for property legalCharges.
     *
     * @param legalCharges New value of property legalCharges.
     */
    public void setLegalCharges(java.lang.String legalCharges) {
        this.legalCharges = legalCharges;
    }

    /**
     * Getter for property miscCharges.
     *
     * @return Value of property miscCharges.
     */
//    public java.lang.String getMiscCharges() {
//        return miscCharges;
//    }
    /**
     * Setter for property miscCharges.
     *
     * @param miscCharges New value of property miscCharges.
     */
//    public void setMiscCharges(java.lang.String miscCharges) {
//        this.miscCharges = miscCharges;
//    }
    /**
     * Getter for property arbitraryCharges.
     *
     * @return Value of property arbitraryCharges.
     */
    public java.lang.String getArbitraryCharges() {
        return arbitraryCharges;
    }

    /**
     * Setter for property arbitraryCharges.
     *
     * @param arbitraryCharges New value of property arbitraryCharges.
     */
    public void setArbitraryCharges(java.lang.String arbitraryCharges) {
        this.arbitraryCharges = arbitraryCharges;
    }

    /**
     * Getter for property insuranceCharges.
     *
     * @return Value of property insuranceCharges.
     */
    public java.lang.String getInsuranceCharges() {
        return insuranceCharges;
    }

    /**
     * Setter for property insuranceCharges.
     *
     * @param insuranceCharges New value of property insuranceCharges.
     */
    public void setInsuranceCharges(java.lang.String insuranceCharges) {
        this.insuranceCharges = insuranceCharges;
    }

    /**
     * Getter for property executionDecreeCharges.
     *
     * @return Value of property executionDecreeCharges.
     */
    public java.lang.String getExecutionDecreeCharges() {
        return executionDecreeCharges;
    }

    /**
     * Setter for property executionDecreeCharges.
     *
     * @param executionDecreeCharges New value of property
     * executionDecreeCharges.
     */
    public void setExecutionDecreeCharges(java.lang.String executionDecreeCharges) {
        this.executionDecreeCharges = executionDecreeCharges;
    }

    /**
     * Getter for property arcCost.
     *
     * @return Value of property arcCost.
     */
    public java.lang.String getArcCost() {
        return arcCost;
    }

    /**
     * Setter for property arcCost.
     *
     * @param arcCost New value of property arcCost.
     */
    public void setArcCost(java.lang.String arcCost) {
        this.arcCost = arcCost;
    }

    /**
     * Getter for property arcExpense.
     *
     * @return Value of property arcExpense.
     */
    public java.lang.String getArcExpense() {
        return arcExpense;
    }

    /**
     * Setter for property arcExpense.
     *
     * @param arcExpense New value of property arcExpense.
     */
    public void setArcExpense(java.lang.String arcExpense) {
        this.arcExpense = arcExpense;
    }

    /**
     * Getter for property eaCost.
     *
     * @return Value of property eaCost.
     */
    public java.lang.String getEaCost() {
        return eaCost;
    }

    /**
     * Setter for property eaCost.
     *
     * @param eaCost New value of property eaCost.
     */
    public void setEaCost(java.lang.String eaCost) {
        this.eaCost = eaCost;
    }

    /**
     * Getter for property eaExpense.
     *
     * @return Value of property eaExpense.
     */
    public java.lang.String getEaExpense() {
        return eaExpense;
    }

    /**
     * Setter for property eaExpense.
     *
     * @param eaExpense New value of property eaExpense.
     */
    public void setEaExpense(java.lang.String eaExpense) {
        this.eaExpense = eaExpense;
    }

    /**
     * Getter for property epCost.
     *
     * @return Value of property epCost.
     */
    public java.lang.String getEpCost() {
        return epCost;
    }

    /**
     * Setter for property epCost.
     *
     * @param epCost New value of property epCost.
     */
    public void setEpCost(java.lang.String epCost) {
        this.epCost = epCost;
    }

    /**
     * Getter for property epExpense.
     *
     * @return Value of property epExpense.
     */
    public java.lang.String getEpExpense() {
        return epExpense;
    }

    /**
     * Setter for property epExpense.
     *
     * @param epExpense New value of property epExpense.
     */
    public void setEpExpense(java.lang.String epExpense) {
        this.epExpense = epExpense;
    }

    /**
     * Getter for property debitIntDiscountAchd.
     *
     * @return Value of property debitIntDiscountAchd.
     */
    public java.lang.String getDebitIntDiscountAchd() {
        return debitIntDiscountAchd;
    }

    /**
     * Setter for property debitIntDiscountAchd.
     *
     * @param debitIntDiscountAchd New value of property debitIntDiscountAchd.
     */
    public void setDebitIntDiscountAchd(java.lang.String debitIntDiscountAchd) {
        this.debitIntDiscountAchd = debitIntDiscountAchd;
    }

    /**
     * Getter for property rebateInterest.
     *
     * @return Value of property rebateInterest.
     */
    public java.lang.String getRebateInterest() {
        return rebateInterest;
    }

    /**
     * Setter for property rebateInterest.
     *
     * @param rebateInterest New value of property rebateInterest.
     */
    public void setRebateInterest(java.lang.String rebateInterest) {
        this.rebateInterest = rebateInterest;
    }

    /**
     * Getter for property stampAdvancesHead.
     *
     * @return Value of property stampAdvancesHead.
     */
    public java.lang.String getStampAdvancesHead() {
        return stampAdvancesHead;
    }

    /**
     * Setter for property stampAdvancesHead.
     *
     * @param stampAdvancesHead New value of property stampAdvancesHead.
     */
    public void setStampAdvancesHead(java.lang.String stampAdvancesHead) {
        this.stampAdvancesHead = stampAdvancesHead;
    }

    /**
     * Getter for property advertisementHead.
     *
     * @return Value of property advertisementHead.
     */
    public java.lang.String getAdvertisementHead() {
        return advertisementHead;
    }

    /**
     * Setter for property advertisementHead.
     *
     * @param advertisementHead New value of property advertisementHead.
     */
    public void setAdvertisementHead(java.lang.String advertisementHead) {
        this.advertisementHead = advertisementHead;
    }

    /**
     * Getter for property arcEpSuspenceHead.
     *
     * @return Value of property arcEpSuspenceHead.
     */
    public java.lang.String getArcEpSuspenceHead() {
        return arcEpSuspenceHead;
    }

    /**
     * Setter for property arcEpSuspenceHead.
     *
     * @param arcEpSuspenceHead New value of property arcEpSuspenceHead.
     */
    public void setArcEpSuspenceHead(java.lang.String arcEpSuspenceHead) {
        this.arcEpSuspenceHead = arcEpSuspenceHead;
    }

    public String getNoticeAdvancesHead() {
        return noticeAdvancesHead;
    }

    public void setNoticeAdvancesHead(String noticeAdvancesHead) {
        this.noticeAdvancesHead = noticeAdvancesHead;
    }

    public String getMeasurementCharges() {
        return measurementCharges;
    }

    public void setMeasurementCharges(String measurementCharges) {
        this.measurementCharges = measurementCharges;
    }

    public String getMeasurementWaiver() {
        return measurementWaiver;
    }

    public void setMeasurementWaiver(String measurementWaiver) {
        this.measurementWaiver = measurementWaiver;
    }

    public String getRecoveryCharges() {
        return recoveryCharges;
    }

    public void setRecoveryCharges(String recoveryCharges) {
        this.recoveryCharges = recoveryCharges;
    }

    public String getRecoveryWaiver() {
        return recoveryWaiver;
    }

    public void setRecoveryWaiver(String recoveryWaiver) {
        this.recoveryWaiver = recoveryWaiver;
    }

    public String getKoleFieldOperation() {
        return koleFieldOperation;
    }

    public void setKoleFieldOperation(String koleFieldOperation) {
        this.koleFieldOperation = koleFieldOperation;
    }

    public String getKoleFieldOperationWaiver() {
        return koleFieldOperationWaiver;
    }

    public void setKoleFieldOperationWaiver(String koleFieldOperationWaiver) {
        this.koleFieldOperationWaiver = koleFieldOperationWaiver;
    }

    public String getKoleFieldexpense() {
        return koleFieldexpense;
    }

    public void setKoleFieldexpense(String koleFieldexpense) {
        this.koleFieldexpense = koleFieldexpense;
    }

    public String getKoleFieldExpenseWaiver() {
        return koleFieldExpenseWaiver;
    }

    public void setKoleFieldExpenseWaiver(String koleFieldExpenseWaiver) {
        this.koleFieldExpenseWaiver = koleFieldExpenseWaiver;
    }
    
}