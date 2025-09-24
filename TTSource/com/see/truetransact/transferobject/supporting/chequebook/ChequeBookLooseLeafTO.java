/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChequeBookLooseLeafTO.java
 * 
 * Created on Fri Jan 21 11:09:07 IST 2005
 */
package com.see.truetransact.transferobject.supporting.chequebook;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CHEQUE_LOOSE_LEAF.
 */
public class ChequeBookLooseLeafTO extends TransferObject implements Serializable {

    private String chqLeafId = "";
    private String prodId = "";
    private String acctNo = "";
    private String remarks = "";
    private String status = "";
    private String leafNo1 = "";
    private String leafNo2 = "";
    private String authorizeStatus = null;
    private Date authorizeDt = null;
    private String authorizeBy = "";
    private String authorizeRemarks = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String branchId = "";
    private String prodType = "";

    /**
     * Setter/Getter for CHQ_LEAF_ID - table Field
     */
    public void setChqLeafId(String chqLeafId) {
        this.chqLeafId = chqLeafId;
    }

    public String getChqLeafId() {
        return chqLeafId;
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
     * Setter/Getter for LEAF_NO1 - table Field
     */
    public void setLeafNo1(String leafNo1) {
        this.leafNo1 = leafNo1;
    }

    public String getLeafNo1() {
        return leafNo1;
    }

    /**
     * Setter/Getter for LEAF_NO2 - table Field
     */
    public void setLeafNo2(String leafNo2) {
        this.leafNo2 = leafNo2;
    }

    public String getLeafNo2() {
        return leafNo2;
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
     * Setter/Getter for AUTHORIZE_REMARKS - table Field
     */
    public void setAuthorizeRemarks(String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    public String getAuthorizeRemarks() {
        return authorizeRemarks;
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
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("chqLeafId");
        return chqLeafId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("chqLeafId", chqLeafId));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("leafNo1", leafNo1));
        strB.append(getTOString("leafNo2", leafNo2));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("chqLeafId", chqLeafId));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("leafNo1", leafNo1));
        strB.append(getTOXml("leafNo2", leafNo2));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}