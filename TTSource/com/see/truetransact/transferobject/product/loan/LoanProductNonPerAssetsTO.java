/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductNonPerAssetsTO.java
 * 
 * Created on Sat Apr 30 16:23:36 IST 2005
 */
package com.see.truetransact.transferobject.product.loan;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_NONPERASSET.
 */
public class LoanProductNonPerAssetsTO extends TransferObject implements Serializable {

    private String prodId = "";
    private Double minPeriodArrears = null;
    private Double periodTransSubstandard = null;
    private Double provisionSubstandard = null;
    private Double periodTransDoubtful = null;
    private Double provisionDoubtful = null;
    private Double periodTransLoss = null;
    private Double periodTransNoperforming = null;
    private Double provisionStdAssets = null;
    private Double provisionLoseAssets = null;
    private Double periodTransDoubtful2 = null;
    private Double provisionDoubtful2 = null;
    private Double periodTransDoubtful3 = null;
    private Double provisionDoubtful3 = null;

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
     * Setter/Getter for MIN_PERIOD_ARREARS - table Field
     */
    public void setMinPeriodArrears(Double minPeriodArrears) {
        this.minPeriodArrears = minPeriodArrears;
    }

    public Double getMinPeriodArrears() {
        return minPeriodArrears;
    }

    /**
     * Setter/Getter for PERIOD_TRANS_SUBSTANDARD - table Field
     */
    public void setPeriodTransSubstandard(Double periodTransSubstandard) {
        this.periodTransSubstandard = periodTransSubstandard;
    }

    public Double getPeriodTransSubstandard() {
        return periodTransSubstandard;
    }

    /**
     * Setter/Getter for PROVISION_SUBSTANDARD - table Field
     */
    public void setProvisionSubstandard(Double provisionSubstandard) {
        this.provisionSubstandard = provisionSubstandard;
    }

    public Double getProvisionSubstandard() {
        return provisionSubstandard;
    }

    /**
     * Setter/Getter for PERIOD_TRANS_DOUBTFUL - table Field
     */
    public void setPeriodTransDoubtful(Double periodTransDoubtful) {
        this.periodTransDoubtful = periodTransDoubtful;
    }

    public Double getPeriodTransDoubtful() {
        return periodTransDoubtful;
    }

    /**
     * Setter/Getter for PROVISION_DOUBTFUL - table Field
     */
    public void setProvisionDoubtful(Double provisionDoubtful) {
        this.provisionDoubtful = provisionDoubtful;
    }

    public Double getProvisionDoubtful() {
        return provisionDoubtful;
    }

    /**
     * Setter/Getter for PERIOD_TRANS_LOSS - table Field
     */
    public void setPeriodTransLoss(Double periodTransLoss) {
        this.periodTransLoss = periodTransLoss;
    }

    public Double getPeriodTransLoss() {
        return periodTransLoss;
    }

    /**
     * Setter/Getter for PERIOD_TRANS_NOPERFORMING - table Field
     */
    public void setPeriodTransNoperforming(Double periodTransNoperforming) {
        this.periodTransNoperforming = periodTransNoperforming;
    }

    public Double getPeriodTransNoperforming() {
        return periodTransNoperforming;
    }

    /**
     * Setter/Getter for PROVISION_STD_ASSETS - table Field
     */
    public void setProvisionStdAssets(Double provisionStdAssets) {
        this.provisionStdAssets = provisionStdAssets;
    }

    public Double getProvisionStdAssets() {
        return provisionStdAssets;
    }

    /**
     * Setter/Getter for PROVISION_LOSE_ASSETS - table Field
     */
    public void setProvisionLoseAssets(Double provisionLoseAssets) {
        this.provisionLoseAssets = provisionLoseAssets;
    }

    public Double getProvisionLoseAssets() {
        return provisionLoseAssets;
    }

    /**
     * Setter/Getter for PERIOD_TRANS_DOUBTFUL_2 - table Field
     */
    public void setPeriodTransDoubtful2(Double periodTransDoubtful2) {
        this.periodTransDoubtful2 = periodTransDoubtful2;
    }

    public Double getPeriodTransDoubtful2() {
        return periodTransDoubtful2;
    }

    /**
     * Setter/Getter for PROVISION_DOUBTFUL_2 - table Field
     */
    public void setProvisionDoubtful2(Double provisionDoubtful2) {
        this.provisionDoubtful2 = provisionDoubtful2;
    }

    public Double getProvisionDoubtful2() {
        return provisionDoubtful2;
    }

    /**
     * Setter/Getter for PERIOD_TRANS_DOUBTFUL_3 - table Field
     */
    public void setPeriodTransDoubtful3(Double periodTransDoubtful3) {
        this.periodTransDoubtful3 = periodTransDoubtful3;
    }

    public Double getPeriodTransDoubtful3() {
        return periodTransDoubtful3;
    }

    /**
     * Setter/Getter for PROVISION_DOUBTFUL_3 - table Field
     */
    public void setProvisionDoubtful3(Double provisionDoubtful3) {
        this.provisionDoubtful3 = provisionDoubtful3;
    }

    public Double getProvisionDoubtful3() {
        return provisionDoubtful3;
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
        strB.append(getTOString("minPeriodArrears", minPeriodArrears));
        strB.append(getTOString("periodTransSubstandard", periodTransSubstandard));
        strB.append(getTOString("provisionSubstandard", provisionSubstandard));
        strB.append(getTOString("periodTransDoubtful", periodTransDoubtful));
        strB.append(getTOString("provisionDoubtful", provisionDoubtful));
        strB.append(getTOString("periodTransLoss", periodTransLoss));
        strB.append(getTOString("periodTransNoperforming", periodTransNoperforming));
        strB.append(getTOString("provisionStdAssets", provisionStdAssets));
        strB.append(getTOString("provisionLoseAssets", provisionLoseAssets));
        strB.append(getTOString("periodTransDoubtful2", periodTransDoubtful2));
        strB.append(getTOString("provisionDoubtful2", provisionDoubtful2));
        strB.append(getTOString("periodTransDoubtful3", periodTransDoubtful3));
        strB.append(getTOString("provisionDoubtful3", provisionDoubtful3));
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
        strB.append(getTOXml("minPeriodArrears", minPeriodArrears));
        strB.append(getTOXml("periodTransSubstandard", periodTransSubstandard));
        strB.append(getTOXml("provisionSubstandard", provisionSubstandard));
        strB.append(getTOXml("periodTransDoubtful", periodTransDoubtful));
        strB.append(getTOXml("provisionDoubtful", provisionDoubtful));
        strB.append(getTOXml("periodTransLoss", periodTransLoss));
        strB.append(getTOXml("periodTransNoperforming", periodTransNoperforming));
        strB.append(getTOXml("provisionStdAssets", provisionStdAssets));
        strB.append(getTOXml("provisionLoseAssets", provisionLoseAssets));
        strB.append(getTOXml("periodTransDoubtful2", periodTransDoubtful2));
        strB.append(getTOXml("provisionDoubtful2", provisionDoubtful2));
        strB.append(getTOXml("periodTransDoubtful3", periodTransDoubtful3));
        strB.append(getTOXml("provisionDoubtful3", provisionDoubtful3));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}