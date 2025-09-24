/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductAccountTO.java
 * 
 * Created on Fri May 14 16:02:10 IST 2004
 */
package com.see.truetransact.transferobject.product.loan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PRODUCT.
 */
public class LoanProductAccountTO extends TransferObject implements Serializable {

    private String excludeTOD = "";
    private String groupLoan = "";
    private String shareLink = "";
    private String excludeScSt = "";
    private String prodId = "";
    private String prodDesc = "";
    private String acctHead = "";
    private String behavesLike = "";
    private String remarks = "";
    private String status = "";
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;
    private String baseCurrency = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeRemark = "";
    private String default_Prod = "N";
    
    
    // Added by nithya
    private String repaymentType = null;
    private Double repaymentFreq = null;   
    // End

    private String goldStockPhotoStored = "N"; // Added by nithya on 29-10-2019 for KD-763 Need Gold ornaments photo saving option
    private String blockIntPostIfLimitExceeds = "N"; // Added by nithya on 16-11-2019 for KD-729
    
    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    public String getGroupLoan() {
        return groupLoan;
    }

    public void setGroupLoan(String groupLoan) {
        this.groupLoan = groupLoan;
    }

    /**
     * Setter/Getter for PROD_DESC - table Field
     */
    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    /**
     * Setter/Getter for ACCT_HEAD - table Field
     */
    public void setAcctHead(String acctHead) {
        this.acctHead = acctHead;
    }

    public String getAcctHead() {
        return acctHead;
    }

    /**
     * Setter/Getter for BEHAVES_LIKE - table Field
     */
    public void setBehavesLike(String behavesLike) {
        this.behavesLike = behavesLike;
    }

    public String getBehavesLike() {
        return behavesLike;
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
     * Setter/Getter for BASE_CURRENCY - table Field
     */
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
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
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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
     * Setter/Getter for AUTHORIZE_REMARK - table Field
     */
    public void setAuthorizeRemark(String authorizeRemark) {
        this.authorizeRemark = authorizeRemark;
    }

    public String getAuthorizeRemark() {
        return authorizeRemark;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public String getExcludeScSt() {
        return excludeScSt;
    }

    public void setExcludeScSt(String excludeScSt) {
        this.excludeScSt = excludeScSt;
    }

    public String getExcludeTOD() {
        return excludeTOD;
    }

    public void setExcludeTOD(String excludeTOD) {
        this.excludeTOD = excludeTOD;
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

    public String getDefault_Prod() {
        return default_Prod;
    }

    public void setDefault_Prod(String default_Prod) {
        this.default_Prod = default_Prod;
    }

    // -- Added by nithya
    public Double getRepaymentFreq() {
        return repaymentFreq;
    }

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentFreq(Double repaymentFreq) {
        this.repaymentFreq = repaymentFreq;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }
    // End

    public String getGoldStockPhotoStored() {
        return goldStockPhotoStored;
    }

    public void setGoldStockPhotoStored(String goldStockPhotoStored) {
        this.goldStockPhotoStored = goldStockPhotoStored;
    }

    public String getBlockIntPostIfLimitExceeds() {
        return blockIntPostIfLimitExceeds;
    }

    public void setBlockIntPostIfLimitExceeds(String blockIntPostIfLimitExceeds) {
        this.blockIntPostIfLimitExceeds = blockIntPostIfLimitExceeds;
    }
    
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("groupLoan", groupLoan));
        strB.append(getTOString("prodDesc", prodDesc));
        strB.append(getTOString("acctHead", acctHead));
        strB.append(getTOString("behavesLike", behavesLike));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("baseCurrency", baseCurrency));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeRemark", authorizeRemark));
        strB.append(getTOString("default_Prod", default_Prod));
        strB.append(getTOString("goldStockPhotoStored", goldStockPhotoStored));
        strB.append(getTOString("blockIntPostIfLimitExceeds", blockIntPostIfLimitExceeds));
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
        strB.append(getTOXml("groupLoan", groupLoan));
        strB.append(getTOXml("prodDesc", prodDesc));
        strB.append(getTOXml("acctHead", acctHead));
        strB.append(getTOXml("behavesLike", behavesLike));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("baseCurrency", baseCurrency));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeRemark", authorizeRemark));
        strB.append(getTOXml("default_Prod", default_Prod));
        strB.append(getTOXml("goldStockPhotoStored", goldStockPhotoStored));
        strB.append(getTOXml("blockIntPostIfLimitExceeds", blockIntPostIfLimitExceeds));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}