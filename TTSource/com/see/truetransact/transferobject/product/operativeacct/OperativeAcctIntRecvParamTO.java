/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctIntRecvParamTO.java
 * 
 * Created on Tue Jul 20 11:11:34 IST 2004
 */
package com.see.truetransact.transferobject.product.operativeacct;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OP_AC_INTRECV_PARAM.
 */
public class OperativeAcctIntRecvParamTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String debitIntCharged = "";
    private Double minDebitIntRate = null;
    private Double maxDebitIntRate = null;
    private Double applDebitIntRate = null;
    private Double minDebitIntAmt = null;
    private Double maxDebitIntAmt = null;
    private Double debitIntCalcFreq = null;
    private Double debitIntApplFreq = null;
    private String debitCompound = "";
    private Double debitCompintCalcFreq = null;
    private String debitProductRoundoff = "";
    private String debitIntRoundoff = "";
    private Date lastIntCalcdtDebit = null;
    private Date lastIntAppldtDebit = null;
    private Double productFreq = null;
    private Double penalIntDebitBalacct = null;
    private Double penalIntChargeStday = null;
    private Double startdayIntCalc = null;
    private Double enddayIntCalc = null;
    private String startmonIntCalc = "";
    private String endmonIntCalc = "";
    private Double startdayProdCalc = null;
    private Double enddayProdCalc = null;
    private String startmonProdCalc = "";
    private String endmonProdCalc = "";

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
     * Setter/Getter for MIN_DEBIT_INT_RATE - table Field
     */
    public void setMinDebitIntRate(Double minDebitIntRate) {
        this.minDebitIntRate = minDebitIntRate;
    }

    public Double getMinDebitIntRate() {
        return minDebitIntRate;
    }

    /**
     * Setter/Getter for MAX_DEBIT_INT_RATE - table Field
     */
    public void setMaxDebitIntRate(Double maxDebitIntRate) {
        this.maxDebitIntRate = maxDebitIntRate;
    }

    public Double getMaxDebitIntRate() {
        return maxDebitIntRate;
    }

    /**
     * Setter/Getter for APPL_DEBIT_INT_RATE - table Field
     */
    public void setApplDebitIntRate(Double applDebitIntRate) {
        this.applDebitIntRate = applDebitIntRate;
    }

    public Double getApplDebitIntRate() {
        return applDebitIntRate;
    }

    /**
     * Setter/Getter for MIN_DEBIT_INT_AMT - table Field
     */
    public void setMinDebitIntAmt(Double minDebitIntAmt) {
        this.minDebitIntAmt = minDebitIntAmt;
    }

    public Double getMinDebitIntAmt() {
        return minDebitIntAmt;
    }

    /**
     * Setter/Getter for MAX_DEBIT_INT_AMT - table Field
     */
    public void setMaxDebitIntAmt(Double maxDebitIntAmt) {
        this.maxDebitIntAmt = maxDebitIntAmt;
    }

    public Double getMaxDebitIntAmt() {
        return maxDebitIntAmt;
    }

    /**
     * Setter/Getter for DEBIT_INT_CALC_FREQ - table Field
     */
    public void setDebitIntCalcFreq(Double debitIntCalcFreq) {
        this.debitIntCalcFreq = debitIntCalcFreq;
    }

    public Double getDebitIntCalcFreq() {
        return debitIntCalcFreq;
    }

    /**
     * Setter/Getter for DEBIT_INT_APPL_FREQ - table Field
     */
    public void setDebitIntApplFreq(Double debitIntApplFreq) {
        this.debitIntApplFreq = debitIntApplFreq;
    }

    public Double getDebitIntApplFreq() {
        return debitIntApplFreq;
    }

    /**
     * Setter/Getter for DEBIT_COMPOUND - table Field
     */
    public void setDebitCompound(String debitCompound) {
        this.debitCompound = debitCompound;
    }

    public String getDebitCompound() {
        return debitCompound;
    }

    /**
     * Setter/Getter for DEBIT_COMPINT_CALC_FREQ - table Field
     */
    public void setDebitCompintCalcFreq(Double debitCompintCalcFreq) {
        this.debitCompintCalcFreq = debitCompintCalcFreq;
    }

    public Double getDebitCompintCalcFreq() {
        return debitCompintCalcFreq;
    }

    /**
     * Setter/Getter for DEBIT_PRODUCT_ROUNDOFF - table Field
     */
    public void setDebitProductRoundoff(String debitProductRoundoff) {
        this.debitProductRoundoff = debitProductRoundoff;
    }

    public String getDebitProductRoundoff() {
        return debitProductRoundoff;
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
     * Setter/Getter for LAST_INT_CALCDT_DEBIT - table Field
     */
    public void setLastIntCalcdtDebit(Date lastIntCalcdtDebit) {
        this.lastIntCalcdtDebit = lastIntCalcdtDebit;
    }

    public Date getLastIntCalcdtDebit() {
        return lastIntCalcdtDebit;
    }

    /**
     * Setter/Getter for LAST_INT_APPLDT_DEBIT - table Field
     */
    public void setLastIntAppldtDebit(Date lastIntAppldtDebit) {
        this.lastIntAppldtDebit = lastIntAppldtDebit;
    }

    public Date getLastIntAppldtDebit() {
        return lastIntAppldtDebit;
    }

    /**
     * Setter/Getter for PRODUCT_FREQ - table Field
     */
    public void setProductFreq(Double productFreq) {
        this.productFreq = productFreq;
    }

    public Double getProductFreq() {
        return productFreq;
    }

    /**
     * Setter/Getter for PENAL_INT_DEBIT_BALACCT - table Field
     */
    public void setPenalIntDebitBalacct(Double penalIntDebitBalacct) {
        this.penalIntDebitBalacct = penalIntDebitBalacct;
    }

    public Double getPenalIntDebitBalacct() {
        return penalIntDebitBalacct;
    }

    /**
     * Setter/Getter for PENAL_INT_CHARGE_STDAY - table Field
     */
    public void setPenalIntChargeStday(Double penalIntChargeStday) {
        this.penalIntChargeStday = penalIntChargeStday;
    }

    public Double getPenalIntChargeStday() {
        return penalIntChargeStday;
    }

    /**
     * Setter/Getter for STARTDAY_INT_CALC - table Field
     */
    public void setStartdayIntCalc(Double startdayIntCalc) {
        this.startdayIntCalc = startdayIntCalc;
    }

    public Double getStartdayIntCalc() {
        return startdayIntCalc;
    }

    /**
     * Setter/Getter for ENDDAY_INT_CALC - table Field
     */
    public void setEnddayIntCalc(Double enddayIntCalc) {
        this.enddayIntCalc = enddayIntCalc;
    }

    public Double getEnddayIntCalc() {
        return enddayIntCalc;
    }

    /**
     * Setter/Getter for STARTMON_INT_CALC - table Field
     */
    public void setStartmonIntCalc(String startmonIntCalc) {
        this.startmonIntCalc = startmonIntCalc;
    }

    public String getStartmonIntCalc() {
        return startmonIntCalc;
    }

    /**
     * Setter/Getter for ENDMON_INT_CALC - table Field
     */
    public void setEndmonIntCalc(String endmonIntCalc) {
        this.endmonIntCalc = endmonIntCalc;
    }

    public String getEndmonIntCalc() {
        return endmonIntCalc;
    }

    /**
     * Setter/Getter for STARTDAY_PROD_CALC - table Field
     */
    public void setStartdayProdCalc(Double startdayProdCalc) {
        this.startdayProdCalc = startdayProdCalc;
    }

    public Double getStartdayProdCalc() {
        return startdayProdCalc;
    }

    /**
     * Setter/Getter for ENDDAY_PROD_CALC - table Field
     */
    public void setEnddayProdCalc(Double enddayProdCalc) {
        this.enddayProdCalc = enddayProdCalc;
    }

    public Double getEnddayProdCalc() {
        return enddayProdCalc;
    }

    /**
     * Setter/Getter for STARTMON_PROD_CALC - table Field
     */
    public void setStartmonProdCalc(String startmonProdCalc) {
        this.startmonProdCalc = startmonProdCalc;
    }

    public String getStartmonProdCalc() {
        return startmonProdCalc;
    }

    /**
     * Setter/Getter for ENDMON_PROD_CALC - table Field
     */
    public void setEndmonProdCalc(String endmonProdCalc) {
        this.endmonProdCalc = endmonProdCalc;
    }

    public String getEndmonProdCalc() {
        return endmonProdCalc;
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
        strB.append(getTOString("minDebitIntRate", minDebitIntRate));
        strB.append(getTOString("maxDebitIntRate", maxDebitIntRate));
        strB.append(getTOString("applDebitIntRate", applDebitIntRate));
        strB.append(getTOString("minDebitIntAmt", minDebitIntAmt));
        strB.append(getTOString("maxDebitIntAmt", maxDebitIntAmt));
        strB.append(getTOString("debitIntCalcFreq", debitIntCalcFreq));
        strB.append(getTOString("debitIntApplFreq", debitIntApplFreq));
        strB.append(getTOString("debitCompound", debitCompound));
        strB.append(getTOString("debitCompintCalcFreq", debitCompintCalcFreq));
        strB.append(getTOString("debitProductRoundoff", debitProductRoundoff));
        strB.append(getTOString("debitIntRoundoff", debitIntRoundoff));
        strB.append(getTOString("lastIntCalcdtDebit", lastIntCalcdtDebit));
        strB.append(getTOString("lastIntAppldtDebit", lastIntAppldtDebit));
        strB.append(getTOString("productFreq", productFreq));
        strB.append(getTOString("penalIntDebitBalacct", penalIntDebitBalacct));
        strB.append(getTOString("penalIntChargeStday", penalIntChargeStday));
        strB.append(getTOString("startdayIntCalc", startdayIntCalc));
        strB.append(getTOString("enddayIntCalc", enddayIntCalc));
        strB.append(getTOString("startmonIntCalc", startmonIntCalc));
        strB.append(getTOString("endmonIntCalc", endmonIntCalc));
        strB.append(getTOString("startdayProdCalc", startdayProdCalc));
        strB.append(getTOString("enddayProdCalc", enddayProdCalc));
        strB.append(getTOString("startmonProdCalc", startmonProdCalc));
        strB.append(getTOString("endmonProdCalc", endmonProdCalc));
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
        strB.append(getTOXml("minDebitIntRate", minDebitIntRate));
        strB.append(getTOXml("maxDebitIntRate", maxDebitIntRate));
        strB.append(getTOXml("applDebitIntRate", applDebitIntRate));
        strB.append(getTOXml("minDebitIntAmt", minDebitIntAmt));
        strB.append(getTOXml("maxDebitIntAmt", maxDebitIntAmt));
        strB.append(getTOXml("debitIntCalcFreq", debitIntCalcFreq));
        strB.append(getTOXml("debitIntApplFreq", debitIntApplFreq));
        strB.append(getTOXml("debitCompound", debitCompound));
        strB.append(getTOXml("debitCompintCalcFreq", debitCompintCalcFreq));
        strB.append(getTOXml("debitProductRoundoff", debitProductRoundoff));
        strB.append(getTOXml("debitIntRoundoff", debitIntRoundoff));
        strB.append(getTOXml("lastIntCalcdtDebit", lastIntCalcdtDebit));
        strB.append(getTOXml("lastIntAppldtDebit", lastIntAppldtDebit));
        strB.append(getTOXml("productFreq", productFreq));
        strB.append(getTOXml("penalIntDebitBalacct", penalIntDebitBalacct));
        strB.append(getTOXml("penalIntChargeStday", penalIntChargeStday));
        strB.append(getTOXml("startdayIntCalc", startdayIntCalc));
        strB.append(getTOXml("enddayIntCalc", enddayIntCalc));
        strB.append(getTOXml("startmonIntCalc", startmonIntCalc));
        strB.append(getTOXml("endmonIntCalc", endmonIntCalc));
        strB.append(getTOXml("startdayProdCalc", startdayProdCalc));
        strB.append(getTOXml("enddayProdCalc", enddayProdCalc));
        strB.append(getTOXml("startmonProdCalc", startmonProdCalc));
        strB.append(getTOXml("endmonProdCalc", endmonProdCalc));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}