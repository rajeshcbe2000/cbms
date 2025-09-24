/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExternalWireTO.java
 *
 * Created on Mon Jul 05 11:02:49 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.product.mds;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_EXTERNAL_WIRE.
 */
public class MDSProductOtherDetailsTO extends TransferObject implements Serializable {

    private String prodId = "";
    private Double commisionRateAmt = 0.0;
    private String commisionRateType = "";
    private Double discountRateAmt = 0.0;
    private String discountRateType = "";
    private Double penalIntAmt = 0.0;
    private String penalIntType = "";
    private Double penalPrizedIntAmt = 0.0;
    private String penalPrizedIntType = "";
    private Double loanIntAmt = 0.0;
    private String loanIntType = "";
    private String bonusGracePeriodDays = "";
    private String bonusGracePeriodMonths = "";
    private String bonusGracePeriodAfter = "";
    private String bonusGracePeriodEnd = "";
    private Double bonusGracePeriod = 0.0;
    private Double bonusPrizedGracePeriod = 0.0;
    private String bonusPrizedGracePeriodDays = "";
    private String bonusPrizedGracePeriodMnth = "";
    private String bonusPrizedGracePeriodAft = "";
    private String bonusPrizedGracePeriodEnd = "";
    private Double penalGracePeriod = 0.0;
    private String penalGracePeriodType = "";
    private Double penalPrizedGracePeriod = 0.0;
    private String penalPrizedGracePeriodType = "";
    private String penalCalc = "";
    private String discountAllowed = "";
    private String disGracePeriodDays = "";
    private String disGracePeriodMonths = "";
    private String disGracePeriodAfter = "";
    private String disGracePeriodEnd = "";
    private Double disGracePeriod = 0.0;
    private Double disPrizedGracePeriod = 0.0;
    private String disPrizedGracePeriodDays = "";
    private String disPrizedGracePeriodMonths = "";
    private String disPrizedGracePeriodAfter = "";
    private String disPrizedGracePeriodEnd = "";
    private String loanAllowed = "";
    private Double loanMargin = 0.0;
    private Double minLoanAmt = 0.0;
    private Double maxLoanAmt = 0.0;
    private String penalIntRateFullInstAmt = "";
    private String penalPrizedIntRateFullInstAmt = "";
    private Double priizedMoneyPaymentAmt = 0.0;
    private String priizedMoneyPaymentType = "";
    private String thalayalBankTransferCategory = "";
    private String munnalBankTransferCategory = "";
    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//    public String getKeyData() {
//        setKeyColumns("");
//        return "";
//    }
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("commisionRateAmt", commisionRateAmt));
        strB.append(getTOString("commisionRateType", commisionRateType));
        strB.append(getTOString("discountRateAmt", discountRateAmt));
        strB.append(getTOString("discountRateType", discountRateType));
        strB.append(getTOString("penalIntAmt", penalIntAmt));
        strB.append(getTOString("penalIntType", penalIntType));
        strB.append(getTOString("penalPrizedIntAmt", penalPrizedIntAmt));
        strB.append(getTOString("penalPrizedIntType", penalPrizedIntType));
        strB.append(getTOString("loanIntAmt", loanIntAmt));
        strB.append(getTOString("loanIntType", loanIntType));
        strB.append(getTOString("bonusGracePeriodDays", bonusGracePeriodDays));
        strB.append(getTOString("bonusGracePeriodMonths", bonusGracePeriodMonths));
        strB.append(getTOString("bonusGracePeriodAfter", bonusGracePeriodAfter));
        strB.append(getTOString("bonusGracePeriodEnd", bonusGracePeriodEnd));
        strB.append(getTOString("bonusGracePeriod", bonusGracePeriod));
        strB.append(getTOString("bonusPrizedGracePeriod", bonusPrizedGracePeriod));
        strB.append(getTOString("bonusPrizedGracePeriodDays", bonusPrizedGracePeriodDays));
        strB.append(getTOString("bonusPrizedGracePeriodMnth", bonusPrizedGracePeriodMnth));
        strB.append(getTOString("bonusPrizedGracePeriodAft", bonusPrizedGracePeriodAft));
        strB.append(getTOString("bonusPrizedGracePeriodEnd", bonusPrizedGracePeriodEnd));
        strB.append(getTOString("penalGracePeriod", penalGracePeriod));
        strB.append(getTOString("penalGracePeriodType", penalGracePeriodType));
        strB.append(getTOString("penalPrizedGracePeriod", penalPrizedGracePeriod));
        strB.append(getTOString("penalPrizedGracePeriodType", penalPrizedGracePeriodType));
        strB.append(getTOString("penalCalc", penalCalc));
        strB.append(getTOString("discountAllowed", discountAllowed));
        strB.append(getTOString("disGracePeriodDays", disGracePeriodDays));
        strB.append(getTOString("disGracePeriodMonths", disGracePeriodMonths));
        strB.append(getTOString("disGracePeriodAfter", disGracePeriodAfter));
        strB.append(getTOString("disGracePeriodEnd", disGracePeriodEnd));
        strB.append(getTOString("disGracePeriod", disGracePeriod));
        strB.append(getTOString("disPrizedGracePeriod", disPrizedGracePeriod));
        strB.append(getTOString("disPrizedGracePeriodDays", disPrizedGracePeriodDays));
        strB.append(getTOString("disPrizedGracePeriodMonths", disPrizedGracePeriodMonths));
        strB.append(getTOString("disPrizedGracePeriodAfter", disPrizedGracePeriodAfter));
        strB.append(getTOString("disPrizedGracePeriodEnd", disPrizedGracePeriodEnd));
        strB.append(getTOString("loanAllowed", loanAllowed));
        strB.append(getTOString("loanMargin", loanMargin));
        strB.append(getTOString("minLoanAmt", minLoanAmt));
        strB.append(getTOString("maxLoanAmt", maxLoanAmt));
        strB.append(getTOString("penalIntRateFullInstAmt", penalIntRateFullInstAmt));
        strB.append(getTOString("penalPrizedIntRateFullInstAmt", penalPrizedIntRateFullInstAmt));
        strB.append(getTOString("priizedMoneyPaymentType", priizedMoneyPaymentType));
        strB.append(getTOString("priizedMoneyPaymentAmt", priizedMoneyPaymentAmt));
        strB.append(getTOString("thalayalBankTransferCategory", thalayalBankTransferCategory));
        strB.append(getTOString("munnalBankTransferCategory", munnalBankTransferCategory));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("commisionRateAmt", commisionRateAmt));
        strB.append(getTOXml("commisionRateType", commisionRateType));
        strB.append(getTOXml("discountRateAmt", discountRateAmt));
        strB.append(getTOXml("discountRateType", discountRateType));
        strB.append(getTOXml("penalIntAmt", penalIntAmt));
        strB.append(getTOXml("penalIntType", penalIntType));
        strB.append(getTOXml("penalPrizedIntAmt", penalPrizedIntAmt));
        strB.append(getTOXml("penalPrizedIntType", penalPrizedIntType));
        strB.append(getTOXml("loanIntAmt", loanIntAmt));
        strB.append(getTOXml("loanIntType", loanIntType));
        strB.append(getTOXml("bonusGracePeriodDays", bonusGracePeriodDays));
        strB.append(getTOXml("bonusGracePeriodMonths", bonusGracePeriodMonths));
        strB.append(getTOXml("bonusGracePeriodAfter", bonusGracePeriodAfter));
        strB.append(getTOXml("bonusGracePeriodEnd", bonusGracePeriodEnd));
        strB.append(getTOXml("bonusGracePeriod", bonusGracePeriod));
        strB.append(getTOXml("bonusPrizedGracePeriod", bonusPrizedGracePeriod));
        strB.append(getTOXml("bonusPrizedGracePeriodDays", bonusPrizedGracePeriodDays));
        strB.append(getTOXml("bonusPrizedGracePeriodMnth", bonusPrizedGracePeriodMnth));
        strB.append(getTOXml("bonusPrizedGracePeriodAft", bonusPrizedGracePeriodAft));
        strB.append(getTOXml("bonusPrizedGracePeriodEnd", bonusPrizedGracePeriodEnd));
        strB.append(getTOXml("penalGracePeriod", penalGracePeriod));
        strB.append(getTOXml("penalGracePeriodType", penalGracePeriodType));
        strB.append(getTOXml("penalPrizedGracePeriod", penalPrizedGracePeriod));
        strB.append(getTOXml("penalPrizedGracePeriodType", penalPrizedGracePeriodType));
        strB.append(getTOXml("penalCalc", penalCalc));
        strB.append(getTOXml("discountAllowed", discountAllowed));
        strB.append(getTOXml("disGracePeriodDays", disGracePeriodDays));
        strB.append(getTOXml("disGracePeriodMonths", disGracePeriodMonths));
        strB.append(getTOXml("disGracePeriodAfter", disGracePeriodAfter));
        strB.append(getTOXml("disGracePeriodEnd", disGracePeriodEnd));
        strB.append(getTOXml("disGracePeriod", disGracePeriod));
        strB.append(getTOXml("disPrizedGracePeriod", disPrizedGracePeriod));
        strB.append(getTOXml("disPrizedGracePeriodDays", disPrizedGracePeriodDays));
        strB.append(getTOXml("disPrizedGracePeriodMonths", disPrizedGracePeriodMonths));
        strB.append(getTOXml("disPrizedGracePeriodAfter", disPrizedGracePeriodAfter));
        strB.append(getTOXml("disPrizedGracePeriodEnd", disPrizedGracePeriodEnd));
        strB.append(getTOXml("loanAllowed", loanAllowed));
        strB.append(getTOXml("loanMargin", loanMargin));
        strB.append(getTOXml("minLoanAmt", minLoanAmt));
        strB.append(getTOXml("maxLoanAmt", maxLoanAmt));
        strB.append(getTOXml("penalIntRateFullInstAmt", penalIntRateFullInstAmt));
        strB.append(getTOXml("penalPrizedIntRateFullInstAmt", penalPrizedIntRateFullInstAmt));
        strB.append(getTOXml("priizedMoneyPaymentType", priizedMoneyPaymentType));
        strB.append(getTOXml("priizedMoneyPaymentAmt", priizedMoneyPaymentAmt));
        strB.append(getTOXml("thalayalBankTransferCategory", thalayalBankTransferCategory));
        strB.append(getTOXml("munnalBankTransferCategory", munnalBankTransferCategory));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property commisionRateAmt.
     *
     * @return Value of property commisionRateAmt.
     */
    public java.lang.Double getCommisionRateAmt() {
        return commisionRateAmt;
    }

    /**
     * Setter for property commisionRateAmt.
     *
     * @param commisionRateAmt New value of property commisionRateAmt.
     */
    public void setCommisionRateAmt(java.lang.Double commisionRateAmt) {
        this.commisionRateAmt = commisionRateAmt;
    }

    /**
     * Getter for property commisionRateType.
     *
     * @return Value of property commisionRateType.
     */
    public java.lang.String getCommisionRateType() {
        return commisionRateType;
    }

    /**
     * Setter for property commisionRateType.
     *
     * @param commisionRateType New value of property commisionRateType.
     */
    public void setCommisionRateType(java.lang.String commisionRateType) {
        this.commisionRateType = commisionRateType;
    }

    /**
     * Getter for property discountRateAmt.
     *
     * @return Value of property discountRateAmt.
     */
    public java.lang.Double getDiscountRateAmt() {
        return discountRateAmt;
    }

    /**
     * Setter for property discountRateAmt.
     *
     * @param discountRateAmt New value of property discountRateAmt.
     */
    public void setDiscountRateAmt(java.lang.Double discountRateAmt) {
        this.discountRateAmt = discountRateAmt;
    }

    /**
     * Getter for property discountRateType.
     *
     * @return Value of property discountRateType.
     */
    public java.lang.String getDiscountRateType() {
        return discountRateType;
    }

    /**
     * Setter for property discountRateType.
     *
     * @param discountRateType New value of property discountRateType.
     */
    public void setDiscountRateType(java.lang.String discountRateType) {
        this.discountRateType = discountRateType;
    }

    /**
     * Getter for property penalIntAmt.
     *
     * @return Value of property penalIntAmt.
     */
    public java.lang.Double getPenalIntAmt() {
        return penalIntAmt;
    }

    /**
     * Setter for property penalIntAmt.
     *
     * @param penalIntAmt New value of property penalIntAmt.
     */
    public void setPenalIntAmt(java.lang.Double penalIntAmt) {
        this.penalIntAmt = penalIntAmt;
    }

    /**
     * Getter for property penalIntType.
     *
     * @return Value of property penalIntType.
     */
    public java.lang.String getPenalIntType() {
        return penalIntType;
    }

    /**
     * Setter for property penalIntType.
     *
     * @param penalIntType New value of property penalIntType.
     */
    public void setPenalIntType(java.lang.String penalIntType) {
        this.penalIntType = penalIntType;
    }

    /**
     * Getter for property penalPrizedIntAmt.
     *
     * @return Value of property penalPrizedIntAmt.
     */
    public java.lang.Double getPenalPrizedIntAmt() {
        return penalPrizedIntAmt;
    }

    /**
     * Setter for property penalPrizedIntAmt.
     *
     * @param penalPrizedIntAmt New value of property penalPrizedIntAmt.
     */
    public void setPenalPrizedIntAmt(java.lang.Double penalPrizedIntAmt) {
        this.penalPrizedIntAmt = penalPrizedIntAmt;
    }

    /**
     * Getter for property penalPrizedIntType.
     *
     * @return Value of property penalPrizedIntType.
     */
    public java.lang.String getPenalPrizedIntType() {
        return penalPrizedIntType;
    }

    /**
     * Setter for property penalPrizedIntType.
     *
     * @param penalPrizedIntType New value of property penalPrizedIntType.
     */
    public void setPenalPrizedIntType(java.lang.String penalPrizedIntType) {
        this.penalPrizedIntType = penalPrizedIntType;
    }

    /**
     * Getter for property loanIntAmt.
     *
     * @return Value of property loanIntAmt.
     */
    public java.lang.Double getLoanIntAmt() {
        return loanIntAmt;
    }

    /**
     * Setter for property loanIntAmt.
     *
     * @param loanIntAmt New value of property loanIntAmt.
     */
    public void setLoanIntAmt(java.lang.Double loanIntAmt) {
        this.loanIntAmt = loanIntAmt;
    }

    /**
     * Getter for property loanIntType.
     *
     * @return Value of property loanIntType.
     */
    public java.lang.String getLoanIntType() {
        return loanIntType;
    }

    /**
     * Setter for property loanIntType.
     *
     * @param loanIntType New value of property loanIntType.
     */
    public void setLoanIntType(java.lang.String loanIntType) {
        this.loanIntType = loanIntType;
    }

    /**
     * Getter for property bonusGracePeriod.
     *
     * @return Value of property bonusGracePeriod.
     */
    public java.lang.Double getBonusGracePeriod() {
        return bonusGracePeriod;
    }

    /**
     * Setter for property bonusGracePeriod.
     *
     * @param bonusGracePeriod New value of property bonusGracePeriod.
     */
    public void setBonusGracePeriod(java.lang.Double bonusGracePeriod) {
        this.bonusGracePeriod = bonusGracePeriod;
    }

    /**
     * Getter for property bonusPrizedGracePeriod.
     *
     * @return Value of property bonusPrizedGracePeriod.
     */
    public java.lang.Double getBonusPrizedGracePeriod() {
        return bonusPrizedGracePeriod;
    }

    /**
     * Setter for property bonusPrizedGracePeriod.
     *
     * @param bonusPrizedGracePeriod New value of property
     * bonusPrizedGracePeriod.
     */
    public void setBonusPrizedGracePeriod(java.lang.Double bonusPrizedGracePeriod) {
        this.bonusPrizedGracePeriod = bonusPrizedGracePeriod;
    }

    /**
     * Getter for property penalGracePeriod.
     *
     * @return Value of property penalGracePeriod.
     */
    public java.lang.Double getPenalGracePeriod() {
        return penalGracePeriod;
    }

    /**
     * Setter for property penalGracePeriod.
     *
     * @param penalGracePeriod New value of property penalGracePeriod.
     */
    public void setPenalGracePeriod(java.lang.Double penalGracePeriod) {
        this.penalGracePeriod = penalGracePeriod;
    }

    /**
     * Getter for property penalGracePeriodType.
     *
     * @return Value of property penalGracePeriodType.
     */
    public java.lang.String getPenalGracePeriodType() {
        return penalGracePeriodType;
    }

    /**
     * Setter for property penalGracePeriodType.
     *
     * @param penalGracePeriodType New value of property penalGracePeriodType.
     */
    public void setPenalGracePeriodType(java.lang.String penalGracePeriodType) {
        this.penalGracePeriodType = penalGracePeriodType;
    }

    /**
     * Getter for property penalPrizedGracePeriod.
     *
     * @return Value of property penalPrizedGracePeriod.
     */
    public java.lang.Double getPenalPrizedGracePeriod() {
        return penalPrizedGracePeriod;
    }

    /**
     * Setter for property penalPrizedGracePeriod.
     *
     * @param penalPrizedGracePeriod New value of property
     * penalPrizedGracePeriod.
     */
    public void setPenalPrizedGracePeriod(java.lang.Double penalPrizedGracePeriod) {
        this.penalPrizedGracePeriod = penalPrizedGracePeriod;
    }

    /**
     * Getter for property penalPrizedGracePeriodType.
     *
     * @return Value of property penalPrizedGracePeriodType.
     */
    public java.lang.String getPenalPrizedGracePeriodType() {
        return penalPrizedGracePeriodType;
    }

    /**
     * Setter for property penalPrizedGracePeriodType.
     *
     * @param penalPrizedGracePeriodType New value of property
     * penalPrizedGracePeriodType.
     */
    public void setPenalPrizedGracePeriodType(java.lang.String penalPrizedGracePeriodType) {
        this.penalPrizedGracePeriodType = penalPrizedGracePeriodType;
    }

    /**
     * Getter for property discountAllowed.
     *
     * @return Value of property discountAllowed.
     */
    public java.lang.String getDiscountAllowed() {
        return discountAllowed;
    }

    /**
     * Setter for property discountAllowed.
     *
     * @param discountAllowed New value of property discountAllowed.
     */
    public void setDiscountAllowed(java.lang.String discountAllowed) {
        this.discountAllowed = discountAllowed;
    }

    /**
     * Getter for property disGracePeriod.
     *
     * @return Value of property disGracePeriod.
     */
    public java.lang.Double getDisGracePeriod() {
        return disGracePeriod;
    }

    /**
     * Setter for property disGracePeriod.
     *
     * @param disGracePeriod New value of property disGracePeriod.
     */
    public void setDisGracePeriod(java.lang.Double disGracePeriod) {
        this.disGracePeriod = disGracePeriod;
    }

    /**
     * Getter for property disPrizedGracePeriod.
     *
     * @return Value of property disPrizedGracePeriod.
     */
    public java.lang.Double getDisPrizedGracePeriod() {
        return disPrizedGracePeriod;
    }

    /**
     * Setter for property disPrizedGracePeriod.
     *
     * @param disPrizedGracePeriod New value of property disPrizedGracePeriod.
     */
    public void setDisPrizedGracePeriod(java.lang.Double disPrizedGracePeriod) {
        this.disPrizedGracePeriod = disPrizedGracePeriod;
    }

    /**
     * Getter for property loanAllowed.
     *
     * @return Value of property loanAllowed.
     */
    public java.lang.String getLoanAllowed() {
        return loanAllowed;
    }

    /**
     * Setter for property loanAllowed.
     *
     * @param loanAllowed New value of property loanAllowed.
     */
    public void setLoanAllowed(java.lang.String loanAllowed) {
        this.loanAllowed = loanAllowed;
    }

    /**
     * Getter for property loanMargin.
     *
     * @return Value of property loanMargin.
     */
    public java.lang.Double getLoanMargin() {
        return loanMargin;
    }

    /**
     * Setter for property loanMargin.
     *
     * @param loanMargin New value of property loanMargin.
     */
    public void setLoanMargin(java.lang.Double loanMargin) {
        this.loanMargin = loanMargin;
    }

    /**
     * Getter for property minLoanAmt.
     *
     * @return Value of property minLoanAmt.
     */
    public java.lang.Double getMinLoanAmt() {
        return minLoanAmt;
    }

    /**
     * Setter for property minLoanAmt.
     *
     * @param minLoanAmt New value of property minLoanAmt.
     */
    public void setMinLoanAmt(java.lang.Double minLoanAmt) {
        this.minLoanAmt = minLoanAmt;
    }

    /**
     * Getter for property maxLoanAmt.
     *
     * @return Value of property maxLoanAmt.
     */
    public java.lang.Double getMaxLoanAmt() {
        return maxLoanAmt;
    }

    /**
     * Setter for property maxLoanAmt.
     *
     * @param maxLoanAmt New value of property maxLoanAmt.
     */
    public void setMaxLoanAmt(java.lang.Double maxLoanAmt) {
        this.maxLoanAmt = maxLoanAmt;
    }

    /**
     * Getter for property penalCalc.
     *
     * @return Value of property penalCalc.
     */
    public java.lang.String getPenalCalc() {
        return penalCalc;
    }

    /**
     * Setter for property penalCalc.
     *
     * @param penalCalc New value of property penalCalc.
     */
    public void setPenalCalc(java.lang.String penalCalc) {
        this.penalCalc = penalCalc;
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
     * Getter for property bonusGracePeriodDays.
     *
     * @return Value of property bonusGracePeriodDays.
     */
    public java.lang.String getBonusGracePeriodDays() {
        return bonusGracePeriodDays;
    }

    /**
     * Setter for property bonusGracePeriodDays.
     *
     * @param bonusGracePeriodDays New value of property bonusGracePeriodDays.
     */
    public void setBonusGracePeriodDays(java.lang.String bonusGracePeriodDays) {
        this.bonusGracePeriodDays = bonusGracePeriodDays;
    }

    /**
     * Getter for property bonusGracePeriodMonths.
     *
     * @return Value of property bonusGracePeriodMonths.
     */
    public java.lang.String getBonusGracePeriodMonths() {
        return bonusGracePeriodMonths;
    }

    /**
     * Setter for property bonusGracePeriodMonths.
     *
     * @param bonusGracePeriodMonths New value of property
     * bonusGracePeriodMonths.
     */
    public void setBonusGracePeriodMonths(java.lang.String bonusGracePeriodMonths) {
        this.bonusGracePeriodMonths = bonusGracePeriodMonths;
    }

    /**
     * Getter for property bonusGracePeriodAfter.
     *
     * @return Value of property bonusGracePeriodAfter.
     */
    public java.lang.String getBonusGracePeriodAfter() {
        return bonusGracePeriodAfter;
    }

    /**
     * Setter for property bonusGracePeriodAfter.
     *
     * @param bonusGracePeriodAfter New value of property bonusGracePeriodAfter.
     */
    public void setBonusGracePeriodAfter(java.lang.String bonusGracePeriodAfter) {
        this.bonusGracePeriodAfter = bonusGracePeriodAfter;
    }

    /**
     * Getter for property bonusGracePeriodEnd.
     *
     * @return Value of property bonusGracePeriodEnd.
     */
    public java.lang.String getBonusGracePeriodEnd() {
        return bonusGracePeriodEnd;
    }

    /**
     * Setter for property bonusGracePeriodEnd.
     *
     * @param bonusGracePeriodEnd New value of property bonusGracePeriodEnd.
     */
    public void setBonusGracePeriodEnd(java.lang.String bonusGracePeriodEnd) {
        this.bonusGracePeriodEnd = bonusGracePeriodEnd;
    }

    /**
     * Getter for property bonusPrizedGracePeriodDays.
     *
     * @return Value of property bonusPrizedGracePeriodDays.
     */
    public java.lang.String getBonusPrizedGracePeriodDays() {
        return bonusPrizedGracePeriodDays;
    }

    /**
     * Setter for property bonusPrizedGracePeriodDays.
     *
     * @param bonusPrizedGracePeriodDays New value of property
     * bonusPrizedGracePeriodDays.
     */
    public void setBonusPrizedGracePeriodDays(java.lang.String bonusPrizedGracePeriodDays) {
        this.bonusPrizedGracePeriodDays = bonusPrizedGracePeriodDays;
    }

    /**
     * Getter for property bonusPrizedGracePeriodMnth.
     *
     * @return Value of property bonusPrizedGracePeriodMnth.
     */
    public java.lang.String getBonusPrizedGracePeriodMnth() {
        return bonusPrizedGracePeriodMnth;
    }

    /**
     * Setter for property bonusPrizedGracePeriodMnth.
     *
     * @param bonusPrizedGracePeriodMnth New value of property
     * bonusPrizedGracePeriodMnth.
     */
    public void setBonusPrizedGracePeriodMnth(java.lang.String bonusPrizedGracePeriodMnth) {
        this.bonusPrizedGracePeriodMnth = bonusPrizedGracePeriodMnth;
    }

    /**
     * Getter for property bonusPrizedGracePeriodAft.
     *
     * @return Value of property bonusPrizedGracePeriodAft.
     */
    public java.lang.String getBonusPrizedGracePeriodAft() {
        return bonusPrizedGracePeriodAft;
    }

    /**
     * Setter for property bonusPrizedGracePeriodAft.
     *
     * @param bonusPrizedGracePeriodAft New value of property
     * bonusPrizedGracePeriodAft.
     */
    public void setBonusPrizedGracePeriodAft(java.lang.String bonusPrizedGracePeriodAft) {
        this.bonusPrizedGracePeriodAft = bonusPrizedGracePeriodAft;
    }

    /**
     * Getter for property disGracePeriodDays.
     *
     * @return Value of property disGracePeriodDays.
     */
    public java.lang.String getDisGracePeriodDays() {
        return disGracePeriodDays;
    }

    /**
     * Setter for property disGracePeriodDays.
     *
     * @param disGracePeriodDays New value of property disGracePeriodDays.
     */
    public void setDisGracePeriodDays(java.lang.String disGracePeriodDays) {
        this.disGracePeriodDays = disGracePeriodDays;
    }

    /**
     * Getter for property disGracePeriodMonths.
     *
     * @return Value of property disGracePeriodMonths.
     */
    public java.lang.String getDisGracePeriodMonths() {
        return disGracePeriodMonths;
    }

    /**
     * Setter for property disGracePeriodMonths.
     *
     * @param disGracePeriodMonths New value of property disGracePeriodMonths.
     */
    public void setDisGracePeriodMonths(java.lang.String disGracePeriodMonths) {
        this.disGracePeriodMonths = disGracePeriodMonths;
    }

    /**
     * Getter for property disGracePeriodAfter.
     *
     * @return Value of property disGracePeriodAfter.
     */
    public java.lang.String getDisGracePeriodAfter() {
        return disGracePeriodAfter;
    }

    /**
     * Setter for property disGracePeriodAfter.
     *
     * @param disGracePeriodAfter New value of property disGracePeriodAfter.
     */
    public void setDisGracePeriodAfter(java.lang.String disGracePeriodAfter) {
        this.disGracePeriodAfter = disGracePeriodAfter;
    }

    /**
     * Getter for property disGracePeriodEnd.
     *
     * @return Value of property disGracePeriodEnd.
     */
    public java.lang.String getDisGracePeriodEnd() {
        return disGracePeriodEnd;
    }

    /**
     * Setter for property disGracePeriodEnd.
     *
     * @param disGracePeriodEnd New value of property disGracePeriodEnd.
     */
    public void setDisGracePeriodEnd(java.lang.String disGracePeriodEnd) {
        this.disGracePeriodEnd = disGracePeriodEnd;
    }

    /**
     * Getter for property disPrizedGracePeriodDays.
     *
     * @return Value of property disPrizedGracePeriodDays.
     */
    public java.lang.String getDisPrizedGracePeriodDays() {
        return disPrizedGracePeriodDays;
    }

    /**
     * Setter for property disPrizedGracePeriodDays.
     *
     * @param disPrizedGracePeriodDays New value of property
     * disPrizedGracePeriodDays.
     */
    public void setDisPrizedGracePeriodDays(java.lang.String disPrizedGracePeriodDays) {
        this.disPrizedGracePeriodDays = disPrizedGracePeriodDays;
    }

    /**
     * Getter for property disPrizedGracePeriodMonths.
     *
     * @return Value of property disPrizedGracePeriodMonths.
     */
    public java.lang.String getDisPrizedGracePeriodMonths() {
        return disPrizedGracePeriodMonths;
    }

    /**
     * Setter for property disPrizedGracePeriodMonths.
     *
     * @param disPrizedGracePeriodMonths New value of property
     * disPrizedGracePeriodMonths.
     */
    public void setDisPrizedGracePeriodMonths(java.lang.String disPrizedGracePeriodMonths) {
        this.disPrizedGracePeriodMonths = disPrizedGracePeriodMonths;
    }

    /**
     * Getter for property disPrizedGracePeriodAfter.
     *
     * @return Value of property disPrizedGracePeriodAfter.
     */
    public java.lang.String getDisPrizedGracePeriodAfter() {
        return disPrizedGracePeriodAfter;
    }

    /**
     * Setter for property disPrizedGracePeriodAfter.
     *
     * @param disPrizedGracePeriodAfter New value of property
     * disPrizedGracePeriodAfter.
     */
    public void setDisPrizedGracePeriodAfter(java.lang.String disPrizedGracePeriodAfter) {
        this.disPrizedGracePeriodAfter = disPrizedGracePeriodAfter;
    }

    /**
     * Getter for property disPrizedGracePeriodEnd.
     *
     * @return Value of property disPrizedGracePeriodEnd.
     */
    public java.lang.String getDisPrizedGracePeriodEnd() {
        return disPrizedGracePeriodEnd;
    }

    /**
     * Setter for property disPrizedGracePeriodEnd.
     *
     * @param disPrizedGracePeriodEnd New value of property
     * disPrizedGracePeriodEnd.
     */
    public void setDisPrizedGracePeriodEnd(java.lang.String disPrizedGracePeriodEnd) {
        this.disPrizedGracePeriodEnd = disPrizedGracePeriodEnd;
    }

    /**
     * Getter for property bonusPrizedGracePeriodEnd.
     *
     * @return Value of property bonusPrizedGracePeriodEnd.
     */
    public java.lang.String getBonusPrizedGracePeriodEnd() {
        return bonusPrizedGracePeriodEnd;
    }

    /**
     * Setter for property bonusPrizedGracePeriodEnd.
     *
     * @param bonusPrizedGracePeriodEnd New value of property
     * bonusPrizedGracePeriodEnd.
     */
    public void setBonusPrizedGracePeriodEnd(java.lang.String bonusPrizedGracePeriodEnd) {
        this.bonusPrizedGracePeriodEnd = bonusPrizedGracePeriodEnd;
    }
    public String getPenalIntRateFullInstAmt() {
        return penalIntRateFullInstAmt;
    }
    public void setPenalIntRateFullInstAmt(String penalIntRateFullInstAmt) {
        this.penalIntRateFullInstAmt = penalIntRateFullInstAmt;
    }
    public String getPenalPrizedIntRateFullInstAmt() {
        return penalPrizedIntRateFullInstAmt;
    }
    public void setPenalPrizedIntRateFullInstAmt(String penalPrizedIntRateFullInstAmt) {
        this.penalPrizedIntRateFullInstAmt = penalPrizedIntRateFullInstAmt;
    }

    public Double getPriizedMoneyPaymentAmt() {
        return priizedMoneyPaymentAmt;
    }

    public void setPriizedMoneyPaymentAmt(Double priizedMoneyPaymentAmt) {
        this.priizedMoneyPaymentAmt = priizedMoneyPaymentAmt;
    }

    public String getPriizedMoneyPaymentType() {
        return priizedMoneyPaymentType;
    }

    public void setPriizedMoneyPaymentType(String priizedMoneyPaymentType) {
        this.priizedMoneyPaymentType = priizedMoneyPaymentType;
    }

    public String getMunnalBankTransferCategory() {
        return munnalBankTransferCategory;
    }

    public void setMunnalBankTransferCategory(String munnalBankTransferCategory) {
        this.munnalBankTransferCategory = munnalBankTransferCategory;
    }

    public String getThalayalBankTransferCategory() {
        return thalayalBankTransferCategory;
    }

    public void setThalayalBankTransferCategory(String thalayalBankTransferCategory) {
        this.thalayalBankTransferCategory = thalayalBankTransferCategory;
    }
    
}