/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LienMarkingTO.java
 * 
 * Created on Tue Jun 08 17:01:14 IST 2004
 */
package com.see.truetransact.transferobject.operativeaccount;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ACT_LIEN.
 */
public class LienMarkingTO extends TransferObject implements Serializable {

    private String actNum = "";
    private String lienId = "";
    private Double lienAmt = null;
    private Date lienDt = null;
    private String lienActNum = "";
    private String lienStatus = "";
    private Date unlienDt = null;
    private String remarks = "";
    private String lienAcHd = "";
    private String lienType = "";
    private String branchId = "";
    private String bankId = "";
    private String unlienRemarks = "";
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;
    private String prodId = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private Date createdDt = null;
    private String createdBy = "";

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
     * Setter/Getter for LIEN_ID - table Field
     */
    public void setLienId(String lienId) {
        this.lienId = lienId;
    }

    public String getLienId() {
        return lienId;
    }

    /**
     * Setter/Getter for LIEN_AMT - table Field
     */
    public void setLienAmt(Double lienAmt) {
        this.lienAmt = lienAmt;
    }

    public Double getLienAmt() {
        return lienAmt;
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
     * Setter/Getter for LIEN_ACT_NUM - table Field
     */
    public void setLienActNum(String lienActNum) {
        this.lienActNum = lienActNum;
    }

    public String getLienActNum() {
        return lienActNum;
    }

    /**
     * Setter/Getter for LIEN_STATUS - table Field
     */
    public void setLienStatus(String lienStatus) {
        this.lienStatus = lienStatus;
    }

    public String getLienStatus() {
        return lienStatus;
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
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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
     * Setter/Getter for LIEN_TYPE - table Field
     */
    public void setLienType(String lienType) {
        this.lienType = lienType;
    }

    public String getLienType() {
        return lienType;
    }

    /**
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    /**
     * Setter/Getter for BANK_ID - table Field
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankId() {
        return bankId;
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
     * Setter/Getter for AUTHORIZE_USER - table Field
     */
    public void setAuthorizeUser(String authorizeUser) {
        this.authorizeUser = authorizeUser;
    }

    public String getAuthorizeUser() {
        return authorizeUser;
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
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("actNum" + KEY_VAL_SEPARATOR + "lienId");
        return (actNum + KEY_VAL_SEPARATOR + lienId);
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("lienId", lienId));
        strB.append(getTOString("lienAmt", lienAmt));
        strB.append(getTOString("lienDt", lienDt));
        strB.append(getTOString("lienActNum", lienActNum));
        strB.append(getTOString("lienStatus", lienStatus));
        strB.append(getTOString("unlienDt", unlienDt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("lienAcHd", lienAcHd));
        strB.append(getTOString("lienType", lienType));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("bankId", bankId));
        strB.append(getTOString("unlienRemarks", unlienRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
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
        strB.append(getTOXml("lienId", lienId));
        strB.append(getTOXml("lienAmt", lienAmt));
        strB.append(getTOXml("lienDt", lienDt));
        strB.append(getTOXml("lienActNum", lienActNum));
        strB.append(getTOXml("lienStatus", lienStatus));
        strB.append(getTOXml("unlienDt", unlienDt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("lienAcHd", lienAcHd));
        strB.append(getTOXml("lienType", lienType));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("bankId", bankId));
        strB.append(getTOXml("unlienRemarks", unlienRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}