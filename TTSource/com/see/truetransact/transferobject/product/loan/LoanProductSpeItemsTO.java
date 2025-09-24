/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductSpeItemsTO.java
 * 
 * Created on Fri May 14 16:05:43 IST 2004
 */
package com.see.truetransact.transferobject.product.loan;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_SPECIALITEM.
 */
public class LoanProductSpeItemsTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String atmCardIssued = "";
    private String crCardIssued = "";
    private String mobileBankClient = "";
    private String branchBankingAllowed = "";

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
     * Setter/Getter for ATM_CARD_ISSUED - table Field
     */
    public void setAtmCardIssued(String atmCardIssued) {
        this.atmCardIssued = atmCardIssued;
    }

    public String getAtmCardIssued() {
        return atmCardIssued;
    }

    /**
     * Setter/Getter for CR_CARD_ISSUED - table Field
     */
    public void setCrCardIssued(String crCardIssued) {
        this.crCardIssued = crCardIssued;
    }

    public String getCrCardIssued() {
        return crCardIssued;
    }

    /**
     * Setter/Getter for MOBILE_BANK_CLIENT - table Field
     */
    public void setMobileBankClient(String mobileBankClient) {
        this.mobileBankClient = mobileBankClient;
    }

    public String getMobileBankClient() {
        return mobileBankClient;
    }

    /**
     * Setter/Getter for BRANCH_BANKING_ALLOWED - table Field
     */
    public void setBranchBankingAllowed(String branchBankingAllowed) {
        this.branchBankingAllowed = branchBankingAllowed;
    }

    public String getBranchBankingAllowed() {
        return branchBankingAllowed;
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
        strB.append(getTOString("atmCardIssued", atmCardIssued));
        strB.append(getTOString("crCardIssued", crCardIssued));
        strB.append(getTOString("mobileBankClient", mobileBankClient));
        strB.append(getTOString("branchBankingAllowed", branchBankingAllowed));
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
        strB.append(getTOXml("atmCardIssued", atmCardIssued));
        strB.append(getTOXml("crCardIssued", crCardIssued));
        strB.append(getTOXml("mobileBankClient", mobileBankClient));
        strB.append(getTOXml("branchBankingAllowed", branchBankingAllowed));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}