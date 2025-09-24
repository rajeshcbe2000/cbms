/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositLienTO.java
 *
 * Created on Wed Jun 09 18:50:50 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit.lien;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_LIEN.
 */
public class DepositLienTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private Integer depositSubNo = 0;
    private String lienNo = "";
    private Date lienDt = null;
    private String lienAcHd = "";
    private String lienAcNo = "";
    private String remarks = "";
    private String creditLienAcct = "";
    private String status = "";
    private String authorizeStatus = null;
    private Date authorizeDt = null;
    private String authorizeBy = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Double lienAmount = null;
    private Date unlienDt = null;
    private String lienProdId = "";
    private String unlienRemarks = "";
    private String losLienLoanType = "";
    private String losLienAcNo;
    private String losLienCustName;
    private Double losLienAmount;
    private Date losLienDt = null;
    private String losLienRemarks;
    private String chkLos = "";

    /**
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
    }

    public Integer getDepositSubNo() {
        return depositSubNo;
    }

    public void setDepositSubNo(Integer depositSubNo) {
        this.depositSubNo = depositSubNo;
    }

    

    /**
     * Setter/Getter for LIEN_NO - table Field
     */
    public void setLienNo(String lienNo) {
        this.lienNo = lienNo;
    }

    public String getLienNo() {
        return lienNo;
    }

    /**
     * Setter/Getter for LIEN_DT - table Field
     */
    public void setLienDt(Date lienDt) {
        this.lienDt = lienDt;
    }

    public Date getLienDt() {
        return lienDt;
    }

    /**
     * Setter/Getter for LIEN_AC_HD - table Field
     */
    public void setLienAcHd(String lienAcHd) {
        this.lienAcHd = lienAcHd;
    }

    public String getLienAcHd() {
        return lienAcHd;
    }

    /**
     * Setter/Getter for LIEN_AC_NO - table Field
     */
    public void setLienAcNo(String lienAcNo) {
        this.lienAcNo = lienAcNo;
    }

    public String getLienAcNo() {
        return lienAcNo;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     * Setter/Getter for CREDIT_LIEN_ACCT - table Field
     */
    public void setCreditLienAcct(String creditLienAcct) {
        this.creditLienAcct = creditLienAcct;
    }

    public String getCreditLienAcct() {
        return creditLienAcct;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
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
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter/Getter for LIEN_AMOUNT - table Field
     */
    public void setLienAmount(Double lienAmount) {
        this.lienAmount = lienAmount;
    }

    public Double getLienAmount() {
        return lienAmount;
    }

    /**
     * Setter/Getter for UNLIEN_DT - table Field
     */
    public void setUnlienDt(Date unlienDt) {
        this.unlienDt = unlienDt;
    }

    public Date getUnlienDt() {
        return unlienDt;
    }

    /**
     * Setter/Getter for LIEN_PROD_ID - table Field
     */
    public void setLienProdId(String lienProdId) {
        this.lienProdId = lienProdId;
    }

    public String getLienProdId() {
        return lienProdId;
    }

    public void setLosLienLoanType(String losLienLoanType) {
        this.losLienLoanType = losLienLoanType;
    }

    public String getLosLienLoanType() {
        return losLienLoanType;
    }

    /**
     * Setter/Getter for UNLIEN_REMARKS - table Field
     */
    public void setUnlienRemarks(String unlienRemarks) {
        this.unlienRemarks = unlienRemarks;
    }

    public String getUnlienRemarks() {
        return unlienRemarks;
    }

    public void setLosLienCustName(String losLienCustName) {
        this.losLienCustName = losLienCustName;
    }

    public String getLosLienCustName() {
        return losLienCustName;
    }

    public void setLosLienAmount(Double losLienAmount) {
        this.losLienAmount = losLienAmount;
    }

    public Double getLosLienAmount() {
        return losLienAmount;
    }

    public void setLosLienRemarks(String losLienRemarks) {
        this.losLienRemarks = losLienRemarks;
    }

    public String getLosLienRemarks() {
        return losLienRemarks;
    }

    public void setChkLos(String chkLos) {
        this.chkLos = chkLos;
    }

    public String getChkLos() {
        return chkLos;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("lienNo");
        return lienNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("depositSubNo", depositSubNo));
        strB.append(getTOString("lienNo", lienNo));
        strB.append(getTOString("lienDt", lienDt));
        strB.append(getTOString("lienAcHd", lienAcHd));
        strB.append(getTOString("lienAcNo", lienAcNo));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("creditLienAcct", creditLienAcct));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("lienAmount", lienAmount));
        strB.append(getTOString("unlienDt", unlienDt));
        strB.append(getTOString("lienProdId", lienProdId));
        strB.append(getTOString("unlienRemarks", unlienRemarks));

        strB.append(getTOString("losLienAcNo", losLienAcNo));
        strB.append(getTOString("losLienLoanType", losLienLoanType));
        strB.append(getTOString("losLienCustName", losLienCustName));
        strB.append(getTOString("losLienAmount", losLienAmount));
        strB.append(getTOString("losLienDt", losLienDt));
        strB.append(getTOString("losLienRemarks", losLienRemarks));
        strB.append(getTOString("chlLos", chkLos));

        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("depositSubNo", depositSubNo));
        strB.append(getTOXml("lienNo", lienNo));
        strB.append(getTOXml("lienDt", lienDt));
        strB.append(getTOXml("lienAcHd", lienAcHd));
        strB.append(getTOXml("lienAcNo", lienAcNo));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("creditLienAcct", creditLienAcct));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("lienAmount", lienAmount));
        strB.append(getTOXml("unlienDt", unlienDt));
        strB.append(getTOXml("lienProdId", lienProdId));
        strB.append(getTOXml("unlienRemarks", unlienRemarks));

        strB.append(getTOXml("losLienAcNo", losLienAcNo));
        strB.append(getTOXml("losLienLoanType", losLienLoanType));
        strB.append(getTOXml("losLienCustName", losLienCustName));
        strB.append(getTOXml("losLienAmount", losLienAmount));
        strB.append(getTOXml("losLienDt", losLienDt));
        strB.append(getTOXml("losLienRemarks", losLienRemarks));
        strB.append(getTOXml("chkLos", chkLos));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property losLienDt.
     *
     * @return Value of property losLienDt.
     */
    public java.util.Date getLosLienDt() {
        return losLienDt;
    }

    /**
     * Setter for property losLienDt.
     *
     * @param losLienDt New value of property losLienDt.
     */
    public void setLosLienDt(java.util.Date losLienDt) {
        this.losLienDt = losLienDt;
    }

    /**
     * Getter for property losLienAcNo.
     *
     * @return Value of property losLienAcNo.
     */
    public java.lang.String getLosLienAcNo() {
        return losLienAcNo;
    }

    /**
     * Setter for property losLienAcNo.
     *
     * @param losLienAcNo New value of property losLienAcNo.
     */
    public void setLosLienAcNo(java.lang.String losLienAcNo) {
        this.losLienAcNo = losLienAcNo;
    }
}