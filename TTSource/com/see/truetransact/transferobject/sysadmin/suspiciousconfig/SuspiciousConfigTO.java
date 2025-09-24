/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SuspiciousConfigTO.java
 * 
 * Created on Thu Mar 10 14:23:59 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.suspiciousconfig;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SUSPECIOUS_CONF.
 */
public class SuspiciousConfigTO extends TransferObject implements Serializable {

    private String confKey = "";
    private String confFor = "";
    private String custNo = "";
    private String prodType = "";
    private String prodId = "";
    private String acctNo = "";
    private Double countExceeds = null;
    private Double worthExceeds = null;
    private Double period = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String slNo = "";
    private String crClr = "";
    private String crTrans = "";
    private String crCash = "";
    private String drClr = "";
    private String drTrans = "";
    private String drCash = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;

    /**
     * Setter/Getter for CONF_KEY - table Field
     */
    public void setConfKey(String confKey) {
        this.confKey = confKey;
    }

    public String getConfKey() {
        return confKey;
    }

    /**
     * Setter/Getter for CONF_FOR - table Field
     */
    public void setConfFor(String confFor) {
        this.confFor = confFor;
    }

    public String getConfFor() {
        return confFor;
    }

    /**
     * Setter/Getter for CUST_NO - table Field
     */
    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getCustNo() {
        return custNo;
    }

    /**
     * Setter/Getter for PROD_TYPE - table Field
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
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
     * Setter/Getter for ACCT_NO - table Field
     */
    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctNo() {
        return acctNo;
    }

    /**
     * Setter/Getter for COUNT_EXCEEDS - table Field
     */
    public void setCountExceeds(Double countExceeds) {
        this.countExceeds = countExceeds;
    }

    public Double getCountExceeds() {
        return countExceeds;
    }

    /**
     * Setter/Getter for WORTH_EXCEEDS - table Field
     */
    public void setWorthExceeds(Double worthExceeds) {
        this.worthExceeds = worthExceeds;
    }

    public Double getWorthExceeds() {
        return worthExceeds;
    }

    /**
     * Setter/Getter for PERIOD - table Field
     */
    public void setPeriod(Double period) {
        this.period = period;
    }

    public Double getPeriod() {
        return period;
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
     * Setter/Getter for SL_NO - table Field
     */
    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getSlNo() {
        return slNo;
    }

    /**
     * Setter/Getter for CR_CLR - table Field
     */
    public void setCrClr(String crClr) {
        this.crClr = crClr;
    }

    public String getCrClr() {
        return crClr;
    }

    /**
     * Setter/Getter for CR_TRANS - table Field
     */
    public void setCrTrans(String crTrans) {
        this.crTrans = crTrans;
    }

    public String getCrTrans() {
        return crTrans;
    }

    /**
     * Setter/Getter for CR_CASH - table Field
     */
    public void setCrCash(String crCash) {
        this.crCash = crCash;
    }

    public String getCrCash() {
        return crCash;
    }

    /**
     * Setter/Getter for DR_CLR - table Field
     */
    public void setDrClr(String drClr) {
        this.drClr = drClr;
    }

    public String getDrClr() {
        return drClr;
    }

    /**
     * Setter/Getter for DR_TRANS - table Field
     */
    public void setDrTrans(String drTrans) {
        this.drTrans = drTrans;
    }

    public String getDrTrans() {
        return drTrans;
    }

    /**
     * Setter/Getter for DR_CASH - table Field
     */
    public void setDrCash(String drCash) {
        this.drCash = drCash;
    }

    public String getDrCash() {
        return drCash;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
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
        setKeyColumns("confKey" + KEY_VAL_SEPARATOR + "slNo");
        return (confKey + KEY_VAL_SEPARATOR + slNo);
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("confKey", confKey));
        strB.append(getTOString("confFor", confFor));
        strB.append(getTOString("custNo", custNo));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOString("countExceeds", countExceeds));
        strB.append(getTOString("worthExceeds", worthExceeds));
        strB.append(getTOString("period", period));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("crClr", crClr));
        strB.append(getTOString("crTrans", crTrans));
        strB.append(getTOString("crCash", crCash));
        strB.append(getTOString("drClr", drClr));
        strB.append(getTOString("drTrans", drTrans));
        strB.append(getTOString("drCash", drCash));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("confKey", confKey));
        strB.append(getTOXml("confFor", confFor));
        strB.append(getTOXml("custNo", custNo));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("countExceeds", countExceeds));
        strB.append(getTOXml("worthExceeds", worthExceeds));
        strB.append(getTOXml("period", period));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("crClr", crClr));
        strB.append(getTOXml("crTrans", crTrans));
        strB.append(getTOXml("crCash", crCash));
        strB.append(getTOXml("drClr", drClr));
        strB.append(getTOXml("drTrans", drTrans));
        strB.append(getTOXml("drCash", drCash));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}