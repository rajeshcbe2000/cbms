/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctSpclitemParamTO.java
 * 
 * Created on Thu Jul 22 10:41:58 IST 2004
 */
package com.see.truetransact.transferobject.product.operativeacct;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OP_AC_SPCLITEM_PARAM.
 */
public class OperativeAcctSpclitemParamTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String linkedFlexiAcct = "";
    private Double minBal1Flexideposit = null;
    private Double minBal2Flexideposit = null;
    private String flexiHappen = "";
    private String atmCardIssued = "";
    private Double minBalAtm = null;
    private String crCardIssued = "";
    private Double minBalCrCard = null;
    private String drCardIssued = "";
    private Double minBalDrCard = null;
    private String ivrsProvided = "";
    private Double minBalIvrs = null;
    private String mobileBanking = "";
    private Double minBalMobile = null;
    private String anyBranchBanking = "";
    private Double minBalAbb = null;
    private String flexiProdId = "";
    private Double impsLimit = null;

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
     * Setter/Getter for LINKED_FLEXI_ACCT - table Field
     */
    public void setLinkedFlexiAcct(String linkedFlexiAcct) {
        this.linkedFlexiAcct = linkedFlexiAcct;
    }

    public String getLinkedFlexiAcct() {
        return linkedFlexiAcct;
    }

    /**
     * Setter/Getter for MIN_BAL1_FLEXIDEPOSIT - table Field
     */
    public void setMinBal1Flexideposit(Double minBal1Flexideposit) {
        this.minBal1Flexideposit = minBal1Flexideposit;
    }

    public Double getMinBal1Flexideposit() {
        return minBal1Flexideposit;
    }

    /**
     * Setter/Getter for MIN_BAL2_FLEXIDEPOSIT - table Field
     */
    public void setMinBal2Flexideposit(Double minBal2Flexideposit) {
        this.minBal2Flexideposit = minBal2Flexideposit;
    }

    public Double getMinBal2Flexideposit() {
        return minBal2Flexideposit;
    }

    /**
     * Setter/Getter for FLEXI_HAPPEN - table Field
     */
    public void setFlexiHappen(String flexiHappen) {
        this.flexiHappen = flexiHappen;
    }

    public String getFlexiHappen() {
        return flexiHappen;
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
     * Setter/Getter for MIN_BAL_ATM - table Field
     */
    public void setMinBalAtm(Double minBalAtm) {
        this.minBalAtm = minBalAtm;
    }

    public Double getMinBalAtm() {
        return minBalAtm;
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
     * Setter/Getter for MIN_BAL_CR_CARD - table Field
     */
    public void setMinBalCrCard(Double minBalCrCard) {
        this.minBalCrCard = minBalCrCard;
    }

    public Double getMinBalCrCard() {
        return minBalCrCard;
    }

    /**
     * Setter/Getter for DR_CARD_ISSUED - table Field
     */
    public void setDrCardIssued(String drCardIssued) {
        this.drCardIssued = drCardIssued;
    }

    public String getDrCardIssued() {
        return drCardIssued;
    }

    /**
     * Setter/Getter for MIN_BAL_DR_CARD - table Field
     */
    public void setMinBalDrCard(Double minBalDrCard) {
        this.minBalDrCard = minBalDrCard;
    }

    public Double getMinBalDrCard() {
        return minBalDrCard;
    }

    /**
     * Setter/Getter for IVRS_PROVIDED - table Field
     */
    public void setIvrsProvided(String ivrsProvided) {
        this.ivrsProvided = ivrsProvided;
    }

    public String getIvrsProvided() {
        return ivrsProvided;
    }

    /**
     * Setter/Getter for MIN_BAL_IVRS - table Field
     */
    public void setMinBalIvrs(Double minBalIvrs) {
        this.minBalIvrs = minBalIvrs;
    }

    public Double getMinBalIvrs() {
        return minBalIvrs;
    }

    /**
     * Setter/Getter for MOBILE_BANKING - table Field
     */
    public void setMobileBanking(String mobileBanking) {
        this.mobileBanking = mobileBanking;
    }

    public String getMobileBanking() {
        return mobileBanking;
    }

    /**
     * Setter/Getter for MIN_BAL_MOBILE - table Field
     */
    public void setMinBalMobile(Double minBalMobile) {
        this.minBalMobile = minBalMobile;
    }

    public Double getMinBalMobile() {
        return minBalMobile;
    }

    /**
     * Setter/Getter for ANY_BRANCH_BANKING - table Field
     */
    public void setAnyBranchBanking(String anyBranchBanking) {
        this.anyBranchBanking = anyBranchBanking;
    }

    public String getAnyBranchBanking() {
        return anyBranchBanking;
    }

    /**
     * Setter/Getter for MIN_BAL_ABB - table Field
     */
    public void setMinBalAbb(Double minBalAbb) {
        this.minBalAbb = minBalAbb;
    }

    public Double getMinBalAbb() {
        return minBalAbb;
    }

    /**
     * Setter/Getter for FLEXI_PROD_ID - table Field
     */
    public void setFlexiProdId(String flexiProdId) {
        this.flexiProdId = flexiProdId;
    }

    public String getFlexiProdId() {
        return flexiProdId;
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

    public Double getImpsLimit() {
        return impsLimit;
    }

    public void setImpsLimit(Double impsLimit) {
        this.impsLimit = impsLimit;
    }


    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("linkedFlexiAcct", linkedFlexiAcct));
        strB.append(getTOString("minBal1Flexideposit", minBal1Flexideposit));
        strB.append(getTOString("minBal2Flexideposit", minBal2Flexideposit));
        strB.append(getTOString("flexiHappen", flexiHappen));
        strB.append(getTOString("atmCardIssued", atmCardIssued));
        strB.append(getTOString("minBalAtm", minBalAtm));
        strB.append(getTOString("crCardIssued", crCardIssued));
        strB.append(getTOString("minBalCrCard", minBalCrCard));
        strB.append(getTOString("drCardIssued", drCardIssued));
        strB.append(getTOString("minBalDrCard", minBalDrCard));
        strB.append(getTOString("ivrsProvided", ivrsProvided));
        strB.append(getTOString("minBalIvrs", minBalIvrs));
        strB.append(getTOString("mobileBanking", mobileBanking));
        strB.append(getTOString("minBalMobile", minBalMobile));
        strB.append(getTOString("anyBranchBanking", anyBranchBanking));
        strB.append(getTOString("minBalAbb", minBalAbb));
        strB.append(getTOString("flexiProdId", flexiProdId));
        strB.append(getTOString("IMPSLimit", impsLimit));
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
        strB.append(getTOXml("linkedFlexiAcct", linkedFlexiAcct));
        strB.append(getTOXml("minBal1Flexideposit", minBal1Flexideposit));
        strB.append(getTOXml("minBal2Flexideposit", minBal2Flexideposit));
        strB.append(getTOXml("flexiHappen", flexiHappen));
        strB.append(getTOXml("atmCardIssued", atmCardIssued));
        strB.append(getTOXml("minBalAtm", minBalAtm));
        strB.append(getTOXml("crCardIssued", crCardIssued));
        strB.append(getTOXml("minBalCrCard", minBalCrCard));
        strB.append(getTOXml("drCardIssued", drCardIssued));
        strB.append(getTOXml("minBalDrCard", minBalDrCard));
        strB.append(getTOXml("ivrsProvided", ivrsProvided));
        strB.append(getTOXml("minBalIvrs", minBalIvrs));
        strB.append(getTOXml("mobileBanking", mobileBanking));
        strB.append(getTOXml("minBalMobile", minBalMobile));
        strB.append(getTOXml("anyBranchBanking", anyBranchBanking));
        strB.append(getTOXml("minBalAbb", minBalAbb));
        strB.append(getTOXml("flexiProdId", flexiProdId));
        strB.append(getTOXml("IMPSLimit", impsLimit));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}