/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSDepositTypeTO .java
 * 
 * Created on Sat Jun 18 10:34:33 IST 2011
 */
package com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is MDS_DEPOSIT_TYPE.
 */
public class MDSDepositTypeTO extends TransferObject implements Serializable {

    private String chittalNo = null; //AJITH
    private Integer subNo = 0;   //AJITH
    private String depositNo = null; //AJITH
    private String prodId = null; //AJITH
    private Date depositDt = null;
    private Double amount = 0.0;   //AJITH
    private Double intRate = 0.0;  //AJITH
    private Double maturityValue = 0.0;
    private Date maturityDt = null;
    private String branchCode = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null; //AJITH
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String prodType = "";
    private String sameChittal = "";

    /**
     * Setter/Getter for CHITTAL_NO - table Field
     */
    public void setChittalNo(String chittalNo) {
        this.chittalNo = chittalNo;
    }

    public String getChittalNo() {
        return chittalNo;
    }

    /**
     * Getter for property sameChittal.
     *
     * @return Value of property sameChittal.
     */
    public String getSameChittal() {
        return sameChittal;
    }

    /**
     * Setter for property sameChittal.
     *
     * @param sameChittal New value of property sameChittal.
     */
    public void setSameChittal(String sameChittal) {
        this.sameChittal = sameChittal;
    }

    /**
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
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
     * Setter/Getter for DEPOSIT_DT - table Field
     */
    public void setDepositDt(Date depositDt) {
        this.depositDt = depositDt;
    }

    public Date getDepositDt() {
        return depositDt;
    }

    /**
     * Setter/Getter for MATURITY_DT - table Field
     */
    public void setMaturityDt(Date maturityDt) {
        this.maturityDt = maturityDt;
    }

    public Date getMaturityDt() {
        return maturityDt;
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
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
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
        strB.append(getTOString("chittalNo", chittalNo));
        strB.append(getTOString("subNo", subNo));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("depositDt", depositDt));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("intRate", intRate));
        strB.append(getTOString("maturityValue", maturityValue));
        strB.append(getTOString("maturityDt", maturityDt));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("chittalNo", chittalNo));
        strB.append(getTOXml("subNo", subNo));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("depositDt", depositDt));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("intRate", intRate));
        strB.append(getTOXml("maturityValue", maturityValue));
        strB.append(getTOXml("maturityDt", maturityDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getIntRate() {
        return intRate;
    }

    public void setIntRate(Double intRate) {
        this.intRate = intRate;
    }

    public Double getMaturityValue() {
        return maturityValue;
    }

    public void setMaturityValue(Double maturityValue) {
        this.maturityValue = maturityValue;
    }

    

    public Integer getSubNo() {
        return subNo;
    }

    public void setSubNo(Integer subNo) {
        this.subNo = subNo;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }
}