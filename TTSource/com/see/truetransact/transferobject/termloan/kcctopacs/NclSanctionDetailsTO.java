/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NclSanctionDetailsTO.java
 * 
 * Created on Fri Mar 08 11:28:25 IST 2013
 */
package com.see.truetransact.transferobject.termloan.kcctopacs;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is NCL_SANCTION_DETAILS.
 */
public class NclSanctionDetailsTO extends TransferObject implements Serializable {

    private String custId = "";
    private String nclSanctionNo = "";
    private Date sanctionDt = null;
    private Date expiryDt = null;
    private Double sanctionAmt = null;
    private String remarks = "";
    private String caProdId = "";
    private String caActNum = "";
    private String kccProdId = "";
    private String kccActNum = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String caprodtype = "";
    private String branchId = "";

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCaprodtype() {
        return caprodtype;
    }

    public void setCaprodtype(String caprodtype) {
        this.caprodtype = caprodtype;
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
     * Setter/Getter for NCL_SANCTION_NO - table Field
     */
    public void setNclSanctionNo(String nclSanctionNo) {
        this.nclSanctionNo = nclSanctionNo;
    }

    public String getNclSanctionNo() {
        return nclSanctionNo;
    }

    /**
     * Setter/Getter for SANCTION_DT - table Field
     */
    public void setSanctionDt(Date sanctionDt) {
        this.sanctionDt = sanctionDt;
    }

    public Date getSanctionDt() {
        return sanctionDt;
    }

    /**
     * Setter/Getter for EXPIRY_DT - table Field
     */
    public void setExpiryDt(Date expiryDt) {
        this.expiryDt = expiryDt;
    }

    public Date getExpiryDt() {
        return expiryDt;
    }

    /**
     * Setter/Getter for SANCTION_AMT - table Field
     */
    public void setSanctionAmt(Double sanctionAmt) {
        this.sanctionAmt = sanctionAmt;
    }

    public Double getSanctionAmt() {
        return sanctionAmt;
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
     * Setter/Getter for CA_PROD_ID - table Field
     */
    public void setCaProdId(String caProdId) {
        this.caProdId = caProdId;
    }

    public String getCaProdId() {
        return caProdId;
    }

    /**
     * Setter/Getter for CA_ACT_NUM - table Field
     */
    public void setCaActNum(String caActNum) {
        this.caActNum = caActNum;
    }

    public String getCaActNum() {
        return caActNum;
    }

    /**
     * Setter/Getter for KCC_PROD_ID - table Field
     */
    public void setKccProdId(String kccProdId) {
        this.kccProdId = kccProdId;
    }

    public String getKccProdId() {
        return kccProdId;
    }

    /**
     * Setter/Getter for KCC_ACT_NUM - table Field
     */
    public void setKccActNum(String kccActNum) {
        this.kccActNum = kccActNum;
    }

    public String getKccActNum() {
        return kccActNum;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
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
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
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
        StringBuilder strB = new StringBuilder(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("nclSanctionNo", nclSanctionNo));
        strB.append(getTOString("sanctionDt", sanctionDt));
        strB.append(getTOString("expiryDt", expiryDt));
        strB.append(getTOString("sanctionAmt", sanctionAmt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("caProdId", caProdId));
        strB.append(getTOString("caActNum", caActNum));
        strB.append(getTOString("kccProdId", kccProdId));
        strB.append(getTOString("kccActNum", kccActNum));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuilder strB = new StringBuilder(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("nclSanctionNo", nclSanctionNo));
        strB.append(getTOXml("sanctionDt", sanctionDt));
        strB.append(getTOXml("expiryDt", expiryDt));
        strB.append(getTOXml("sanctionAmt", sanctionAmt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("caProdId", caProdId));
        strB.append(getTOXml("caActNum", caActNum));
        strB.append(getTOXml("kccProdId", kccProdId));
        strB.append(getTOXml("kccActNum", kccActNum));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}