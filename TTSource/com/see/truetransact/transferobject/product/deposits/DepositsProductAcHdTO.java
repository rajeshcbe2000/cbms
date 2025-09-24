/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositsProductAcHdTO.java
 *
 * Created on Wed May 12 11:37:37 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.product.deposits;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSITS_PROD_ACHD.
 */
public class DepositsProductAcHdTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String intProvAchd = "";
    private String intPay = "";
    private String intDebit = "";
    private String maturityDeposit = "";
    private String intMaturedDeposit = "";
    private String intProvMatured = "";
    private String achdFloatAc = "";
    private String fixedDepositAchd = "";
    private String commisionAchd = "";
    private String delayedAchd = "";
    private String trasferOutAchd = "";
    private String postageAcHd = "";
    private String interestRecoveryAcHd = "";
    private String benovelentIntReserveHd = ""; // Added by nithya for adding field for interest reserve head for benevolent deposits

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
     * Setter/Getter for INT_PROV_ACHD - table Field
     */
    public void setIntProvAchd(String intProvAchd) {
        this.intProvAchd = intProvAchd;
    }

    public String getIntProvAchd() {
        return intProvAchd;
    }

    /**
     * Setter/Getter for INT_PAY - table Field
     */
    public void setIntPay(String intPay) {
        this.intPay = intPay;
    }

    public String getIntPay() {
        return intPay;
    }

    /**
     * Setter/Getter for INT_DEBIT - table Field
     */
    public void setIntDebit(String intDebit) {
        this.intDebit = intDebit;
    }

    public String getIntDebit() {
        return intDebit;
    }

    /**
     * Setter/Getter for MATURITY_DEPOSIT - table Field
     */
    public void setMaturityDeposit(String maturityDeposit) {
        this.maturityDeposit = maturityDeposit;
    }

    public String getMaturityDeposit() {
        return maturityDeposit;
    }

    /**
     * Setter/Getter for INT_MATURED_DEPOSIT - table Field
     */
    public void setIntMaturedDeposit(String intMaturedDeposit) {
        this.intMaturedDeposit = intMaturedDeposit;
    }

    public String getIntMaturedDeposit() {
        return intMaturedDeposit;
    }

    /**
     * Setter/Getter for INT_PROV_MATURED - table Field
     */
    public void setIntProvMatured(String intProvMatured) {
        this.intProvMatured = intProvMatured;
    }

    public String getIntProvMatured() {
        return intProvMatured;
    }

    /**
     * Setter/Getter for ACHD_FLOAT_AC - table Field
     */
    public void setAchdFloatAc(String achdFloatAc) {
        this.achdFloatAc = achdFloatAc;
    }

    public String getAchdFloatAc() {
        return achdFloatAc;
    }

    /**
     * Setter/Getter for FIXED_DEPOSIT_ACHD - table Field
     */
    public void setFixedDepositAchd(String fixedDepositAchd) {
        this.fixedDepositAchd = fixedDepositAchd;
    }

    public String getFixedDepositAchd() {
        return fixedDepositAchd;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId" + KEY_VAL_SEPARATOR + "intProvAchd");
        return prodId + KEY_VAL_SEPARATOR + intProvAchd;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("intProvAchd", intProvAchd));
        strB.append(getTOString("intPay", intPay));
        strB.append(getTOString("intDebit", intDebit));
        strB.append(getTOString("maturityDeposit", maturityDeposit));
        strB.append(getTOString("intMaturedDeposit", intMaturedDeposit));
        strB.append(getTOString("intProvMatured", intProvMatured));
        strB.append(getTOString("achdFloatAc", achdFloatAc));
        strB.append(getTOString("fixedDepositAchd", fixedDepositAchd));
        strB.append(getTOString("commisionAchd", commisionAchd));
        strB.append(getTOString("delayedAchd", delayedAchd));
        strB.append(getTOString("trasferOutAchd", trasferOutAchd));
        strB.append(getTOString("postageAcHd", postageAcHd));
        strB.append(getTOString("benovelentIntReserveHd", benovelentIntReserveHd));
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
        strB.append(getTOXml("intProvAchd", intProvAchd));
        strB.append(getTOXml("intPay", intPay));
        strB.append(getTOXml("intDebit", intDebit));
        strB.append(getTOXml("maturityDeposit", maturityDeposit));
        strB.append(getTOXml("intMaturedDeposit", intMaturedDeposit));
        strB.append(getTOXml("intProvMatured", intProvMatured));
        strB.append(getTOXml("achdFloatAc", achdFloatAc));
        strB.append(getTOXml("fixedDepositAchd", fixedDepositAchd));
        strB.append(getTOXml("commisionAchd", commisionAchd));
        strB.append(getTOXml("delayedAchd", delayedAchd));
        strB.append(getTOXml("trasferOutAchd", trasferOutAchd));
        strB.append(getTOXml("postageAcHd", postageAcHd));
        strB.append(getTOXml("benovelentIntReserveHd", benovelentIntReserveHd));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property commisionAchd.
     *
     * @return Value of property commisionAchd.
     */
    public java.lang.String getCommisionAchd() {
        return commisionAchd;
    }

    /**
     * Setter for property commisionAchd.
     *
     * @param commisionAchd New value of property commisionAchd.
     */
    public void setCommisionAchd(java.lang.String commisionAchd) {
        this.commisionAchd = commisionAchd;
    }

    /**
     * Getter for property delayedAchd.
     *
     * @return Value of property delayedAchd.
     */
    public java.lang.String getDelayedAchd() {
        return delayedAchd;
    }

    /**
     * Setter for property delayedAchd.
     *
     * @param delayedAchd New value of property delayedAchd.
     */
    public void setDelayedAchd(java.lang.String delayedAchd) {
        this.delayedAchd = delayedAchd;
    }

    /**
     * Getter for property trasferOutAchd.
     *
     * @return Value of property trasferOutAchd.
     */
    public java.lang.String getTrasferOutAchd() {
        return trasferOutAchd;
    }

    /**
     * Setter for property trasferOutAchd.
     *
     * @param trasferOutAchd New value of property trasferOutAchd.
     */
    public void setTrasferOutAchd(java.lang.String trasferOutAchd) {
        this.trasferOutAchd = trasferOutAchd;
    }

    /**
     * Getter for property postageAcHd.
     *
     * @return Value of property postageAcHd.
     */
    public java.lang.String getPostageAcHd() {
        return postageAcHd;
    }

    /**
     * Setter for property postageAcHd.
     *
     * @param postageAcHd New value of property postageAcHd.
     */
    public void setPostageAcHd(java.lang.String postageAcHd) {
        this.postageAcHd = postageAcHd;
    }

    public String getInterestRecoveryAcHd() {
        return interestRecoveryAcHd;
    }

    public void setInterestRecoveryAcHd(String interestRecoveryAcHd) {
        this.interestRecoveryAcHd = interestRecoveryAcHd;
    }

    public String getBenovelentIntReserveHd() {
        return benovelentIntReserveHd;
    }

    public void setBenovelentIntReserveHd(String benovelentIntReserveHd) {
        this.benovelentIntReserveHd = benovelentIntReserveHd;
    }
    
    
}