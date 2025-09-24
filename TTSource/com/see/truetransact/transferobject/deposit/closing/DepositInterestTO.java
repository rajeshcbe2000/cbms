/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositInterest.java
 * 
 * Created on Thu Feb 10 10:01:16 IST 2005
 */
package com.see.truetransact.transferobject.deposit.closing;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_INTEREST.
 */
public class DepositInterestTO extends TransferObject implements Serializable {

    private String actNum = "";
    private Date intDt = null;
    private String intType = "";
    private String acHdId = "";
    private Date applDt = null;
    private Double intAmt = null;
    private Double intRate = null;
    private Double principleAmt = null;
    private String productId = "";
    private String productType = "";
    private String transLogId = "";
    private String custId = "";
    private String isTdsApplied = "";

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for INT_DT - table Field
     */
    public void setIntDt(Date intDt) {
        this.intDt = intDt;
    }

    public Date getIntDt() {
        return intDt;
    }

    /**
     * Setter/Getter for INT_TYPE - table Field
     */
    public void setIntType(String intType) {
        this.intType = intType;
    }

    public String getIntType() {
        return intType;
    }

    /**
     * Setter/Getter for AC_HD_ID - table Field
     */
    public void setAcHdId(String acHdId) {
        this.acHdId = acHdId;
    }

    public String getAcHdId() {
        return acHdId;
    }

    /**
     * Setter/Getter for APPL_DT - table Field
     */
    public void setApplDt(Date applDt) {
        this.applDt = applDt;
    }

    public Date getApplDt() {
        return applDt;
    }

    /**
     * Setter/Getter for INT_AMT - table Field
     */
    public void setIntAmt(Double intAmt) {
        this.intAmt = intAmt;
    }

    public Double getIntAmt() {
        return intAmt;
    }

    /**
     * Setter/Getter for INT_RATE - table Field
     */
    public void setIntRate(Double intRate) {
        this.intRate = intRate;
    }

    public Double getIntRate() {
        return intRate;
    }

    /**
     * Setter/Getter for PRINCIPLE_AMT - table Field
     */
    public void setPrincipleAmt(Double principleAmt) {
        this.principleAmt = principleAmt;
    }

    public Double getPrincipleAmt() {
        return principleAmt;
    }

    /**
     * Setter/Getter for PRODUCT_ID - table Field
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    /**
     * Setter/Getter for PRODUCT_TYPE - table Field
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    /**
     * Setter/Getter for TRANS_LOG_ID - table Field
     */
    public void setTransLogId(String transLogId) {
        this.transLogId = transLogId;
    }

    public String getTransLogId() {
        return transLogId;
    }

    /**
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
    }

    /**
     * Setter/Getter for IS_TDS_APPLIED - table Field
     */
    public void setIsTdsApplied(String isTdsApplied) {
        this.isTdsApplied = isTdsApplied;
    }

    public String getIsTdsApplied() {
        return isTdsApplied;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("intDt", intDt));
        strB.append(getTOString("intType", intType));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("applDt", applDt));
        strB.append(getTOString("intAmt", intAmt));
        strB.append(getTOString("intRate", intRate));
        strB.append(getTOString("principleAmt", principleAmt));
        strB.append(getTOString("productId", productId));
        strB.append(getTOString("productType", productType));
        strB.append(getTOString("transLogId", transLogId));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("isTdsApplied", isTdsApplied));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("intDt", intDt));
        strB.append(getTOXml("intType", intType));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("applDt", applDt));
        strB.append(getTOXml("intAmt", intAmt));
        strB.append(getTOXml("intRate", intRate));
        strB.append(getTOXml("principleAmt", principleAmt));
        strB.append(getTOXml("productId", productId));
        strB.append(getTOXml("productType", productType));
        strB.append(getTOXml("transLogId", transLogId));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("isTdsApplied", isTdsApplied));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}