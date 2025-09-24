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
package com.see.truetransact.transferobject.product.loan.agriculturecard;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_ACHD.
 */
public class AgriLoanProductAccHeadTO extends TransferObject implements Serializable {

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
    private String executionDecreeCharges = "";
    private String insurancePremiumDebit = "";

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
        strB.append(getTOString("noticeCharges", noticeCharges));
        strB.append(getTOString("intPayableAcHd", intPayableAcHd));
        strB.append(getTOString("legalCharges", legalCharges));
//        strB.append(getTOString("miscCharges",miscCharges));
        strB.append(getTOString("arbitraryCharges", arbitraryCharges));
        strB.append(getTOString("insuranceCharges", insuranceCharges));
        strB.append(getTOString("executionDecreeCharges", executionDecreeCharges));
        strB.append(getTOString("insurancePremiumDebit", insurancePremiumDebit));
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
        strB.append(getTOXml("noticeCharges", noticeCharges));
        strB.append(getTOXml("intPayableAcHd", intPayableAcHd));
        strB.append(getTOXml("legalCharges", legalCharges));
//        strB.append(getTOXml("miscCharges",miscCharges));
        strB.append(getTOXml("arbitraryCharges", arbitraryCharges));
        strB.append(getTOXml("insuranceCharges", insuranceCharges));
        strB.append(getTOXml("executionDecreeCharges", executionDecreeCharges));
        strB.append(getTOXml("insurancePremiumDebit", insurancePremiumDebit));
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
     * Getter for property insurancePremiumDebit.
     *
     * @return Value of property insurancePremiumDebit.
     */
    public java.lang.String getInsurancePremiumDebit() {
        return insurancePremiumDebit;
    }

    /**
     * Setter for property insurancePremiumDebit.
     *
     * @param insurancePremiumDebit New value of property insurancePremiumDebit.
     */
    public void setInsurancePremiumDebit(java.lang.String insurancePremiumDebit) {
        this.insurancePremiumDebit = insurancePremiumDebit;
    }
}