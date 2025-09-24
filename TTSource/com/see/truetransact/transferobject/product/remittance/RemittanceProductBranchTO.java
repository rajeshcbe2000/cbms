/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceProductBranchTO.java
 *
 * Created on Tue May 18 12:35:59 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.product.remittance;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is REMITTANCE_PROD_BRANCH.
 */
public class RemittanceProductBranchTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String branchCode = "";
    private String branchName = "";
    private String remittanceType = "";
    private Double remittanceLimit = null;
    private String inwardVariableNo = "";
    private String outwardVariableNo = "";
    private Double minAmt = null;
    private Double maxAmt = null;
    private String status = "";
    private String bankCode = "";

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
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter/Getter for BRANCH_NAME - table Field
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    /**
     * Setter/Getter for REMITTANCE_TYPE - table Field
     */
    public void setRemittanceType(String remittanceType) {
        this.remittanceType = remittanceType;
    }

    public String getRemittanceType() {
        return remittanceType;
    }

    /**
     * Setter/Getter for REMITTANCE_LIMIT - table Field
     */
    public void setRemittanceLimit(Double remittanceLimit) {
        this.remittanceLimit = remittanceLimit;
    }

    public Double getRemittanceLimit() {
        return remittanceLimit;
    }

    /**
     * Setter/Getter for INWARD_VARIABLE_NO - table Field
     */
    public void setInwardVariableNo(String inwardVariableNo) {
        this.inwardVariableNo = inwardVariableNo;
    }

    public String getInwardVariableNo() {
        return inwardVariableNo;
    }

    /**
     * Setter/Getter for OUTWARD_VARIABLE_NO - table Field
     */
    public void setOutwardVariableNo(String outwardVariableNo) {
        this.outwardVariableNo = outwardVariableNo;
    }

    public String getOutwardVariableNo() {
        return outwardVariableNo;
    }

    /**
     * Setter/Getter for MIN_AMT - table Field
     */
    public void setMinAmt(Double minAmt) {
        this.minAmt = minAmt;
    }

    public Double getMinAmt() {
        return minAmt;
    }

    /**
     * Setter/Getter for MAX_AMT - table Field
     */
    public void setMaxAmt(Double maxAmt) {
        this.maxAmt = maxAmt;
    }

    public Double getMaxAmt() {
        return maxAmt;
    }

    /**
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for BANK_CODE - table Field
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId" + KEY_VAL_SEPARATOR + "branchCode");
        return prodId + KEY_VAL_SEPARATOR + branchCode;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("branchName", branchName));
        strB.append(getTOString("remittanceType", remittanceType));
        strB.append(getTOString("remittanceLimit", remittanceLimit));
        strB.append(getTOString("inwardVariableNo", inwardVariableNo));
        strB.append(getTOString("outwardVariableNo", outwardVariableNo));
        strB.append(getTOString("minAmt", minAmt));
        strB.append(getTOString("maxAmt", maxAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("bankCode", bankCode));
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
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("branchName", branchName));
        strB.append(getTOXml("remittanceType", remittanceType));
        strB.append(getTOXml("remittanceLimit", remittanceLimit));
        strB.append(getTOXml("inwardVariableNo", inwardVariableNo));
        strB.append(getTOXml("outwardVariableNo", outwardVariableNo));
        strB.append(getTOXml("minAmt", minAmt));
        strB.append(getTOXml("maxAmt", maxAmt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("bankCode", bankCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}