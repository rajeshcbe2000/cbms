/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DividendBatchTO.java
 *
 * Created on Thu Jan 20 19:10:16 IST 2005
 */
package com.see.truetransact.transferobject.batchprocess.share;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_DIVIDEND.
 */
public class DividendBatchTO extends TransferObject implements Serializable {

    private String shareType = "";
    private String shareAcctNo = "";
    private Date dividendDt = null;
    private Double shareCount = null;
    private Double dividendPer = null;
    private Double dividendAmt = null;
    private String shareAcctDetailNo = "";
    private Date dividendUpTo = null;
    private String divPayFlag = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String batchID = "";
    private String initBran = "";

    /**
     * Setter/Getter for SHARE_TYPE - table Field
     */
    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getShareType() {
        return shareType;
    }

    /**
     * Setter/Getter for SHARE_ACCT_NO - table Field
     */
    public void setShareAcctNo(String shareAcctNo) {
        this.shareAcctNo = shareAcctNo;
    }

    public String getShareAcctNo() {
        return shareAcctNo;
    }

    /**
     * Setter/Getter for DIVIDEND_DT - table Field
     */
    public void setDividendDt(Date dividendDt) {
        this.dividendDt = dividendDt;
    }

    public Date getDividendDt() {
        return dividendDt;
    }

    /**
     * Setter/Getter for SHARE_COUNT - table Field
     */
    public void setShareCount(Double shareCount) {
        this.shareCount = shareCount;
    }

    public Double getShareCount() {
        return shareCount;
    }

    /**
     * Setter/Getter for DIVIDEND_PER - table Field
     */
    public void setDividendPer(Double dividendPer) {
        this.dividendPer = dividendPer;
    }

    public Double getDividendPer() {
        return dividendPer;
    }

    /**
     * Setter/Getter for DIVIDEND_AMT - table Field
     */
    public void setDividendAmt(Double dividendAmt) {
        this.dividendAmt = dividendAmt;
    }

    public Double getDividendAmt() {
        return dividendAmt;
    }

    /**
     * Setter/Getter for SHARE_ACCT_DETAIL_NO - table Field
     */
    public void setShareAcctDetailNo(String shareAcctDetailNo) {
        this.shareAcctDetailNo = shareAcctDetailNo;
    }

    public String getShareAcctDetailNo() {
        return shareAcctDetailNo;
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
        strB.append(getTOString("shareType", shareType));
        strB.append(getTOString("shareAcctNo", shareAcctNo));
        strB.append(getTOString("dividendDt", dividendDt));
        strB.append(getTOString("shareCount", shareCount));
        strB.append(getTOString("dividendPer", dividendPer));
        strB.append(getTOString("dividendAmt", dividendAmt));
        strB.append(getTOString("shareAcctDetailNo", shareAcctDetailNo));
        strB.append(getTOString("dividendUpTo", dividendUpTo));
        strB.append(getTOString("divPayFlag", divPayFlag));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("batchID", batchID));
        strB.append(getTOString("initBran", initBran));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("shareType", shareType));
        strB.append(getTOXml("shareAcctNo", shareAcctNo));
        strB.append(getTOXml("dividendDt", dividendDt));
        strB.append(getTOXml("shareCount", shareCount));
        strB.append(getTOXml("dividendPer", dividendPer));
        strB.append(getTOXml("dividendAmt", dividendAmt));
        strB.append(getTOXml("shareAcctDetailNo", shareAcctDetailNo));
        strB.append(getTOXml("dividendUpTo", dividendUpTo));
        strB.append(getTOXml("divPayFlag", divPayFlag));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("batchID", batchID));
        strB.append(getTOXml("initBran", initBran));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property dividendUpTo.
     *
     * @return Value of property dividendUpTo.
     */
    public java.util.Date getDividendUpTo() {
        return dividendUpTo;
    }

    /**
     * Setter for property dividendUpTo.
     *
     * @param dividendUpTo New value of property dividendUpTo.
     */
    public void setDividendUpTo(java.util.Date dividendUpTo) {
        this.dividendUpTo = dividendUpTo;
    }

    /**
     * Getter for property divPayFlag.
     *
     * @return Value of property divPayFlag.
     */
    public java.lang.String getDivPayFlag() {
        return divPayFlag;
    }

    /**
     * Setter for property divPayFlag.
     *
     * @param divPayFlag New value of property divPayFlag.
     */
    public void setDivPayFlag(java.lang.String divPayFlag) {
        this.divPayFlag = divPayFlag;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    /**
     * Getter for property batchID.
     *
     * @return Value of property batchID.
     */
    public java.lang.String getBatchID() {
        return batchID;
    }

    /**
     * Setter for property batchID.
     *
     * @param batchID New value of property batchID.
     */
    public void setBatchID(java.lang.String batchID) {
        this.batchID = batchID;
    }

    /**
     * Getter for property initBran.
     *
     * @return Value of property initBran.
     */
    public java.lang.String getInitBran() {
        return initBran;
    }

    /**
     * Setter for property initBran.
     *
     * @param initBran New value of property initBran.
     */
    public void setInitBran(java.lang.String initBran) {
        this.initBran = initBran;
    }
}