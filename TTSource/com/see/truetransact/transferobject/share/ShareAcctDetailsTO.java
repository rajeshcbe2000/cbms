/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareAcctDetailsTO.java
 * 
 * Created on Mon Apr 18 16:42:47 IST 2005
 */
package com.see.truetransact.transferobject.share;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_ACCT_DETAILS.
 */
public class ShareAcctDetailsTO extends TransferObject implements Serializable {

    private String shareAcctNo = "";
    private Integer noOfShares =0 ;
    private Date shareCertIssueDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorize = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String shareAcctDetNo = "";
    private String shareNoFrom = "";
    private String shareNoTo = "";
    private String resolutionNo = "";
    private String shareStatus = "";
    private Double txtShareValue = 0.0;
    private Double txtTotShareFee = 0.0;
    private Double txtShareApplFee = 0.0;
    private Double txtShareMemFee = 0.0;
    private String txtShareTotAmount = "";
    private String txtNoShares = "";
    private String txtApplicationNo = "";
    private String loanAcctNumber = null;
    private Integer txtFromSL_No=0;
    private Integer txtToSL_No=0;

    public Double getTxtShareValue() {
        return txtShareValue;
    }

    public void setTxtShareValue(Double txtShareValue) {
        this.txtShareValue = txtShareValue;
    }

    public Integer getNoOfShares() {
        return noOfShares;
    }

    public void setNoOfShares(Integer noOfShares) {
        this.noOfShares = noOfShares;
    }

    public Double getTxtTotShareFee() {
        return txtTotShareFee;
    }

    public void setTxtTotShareFee(Double txtTotShareFee) {
        this.txtTotShareFee = txtTotShareFee;
    }

    public String getShareNoTo() {
        return shareNoTo;
    }

    public void setShareNoTo(String shareNoTo) {
        this.shareNoTo = shareNoTo;
    }



    public Integer getTxtFromSL_No() {
        return txtFromSL_No;
    }

    public void setTxtFromSL_No(Integer txtFromSL_No) {
        this.txtFromSL_No = txtFromSL_No;
    }

    public Integer getTxtToSL_No() {
        return txtToSL_No;
    }

    public void setTxtToSL_No(Integer txtToSL_No) {
        this.txtToSL_No = txtToSL_No;
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

    public void setTxtApplicationNo(String txtApplicationNo) {
        this.txtApplicationNo = txtApplicationNo;
    }

    public String getTxtApplicationNo() {
        return txtApplicationNo;
    }

    /**
     * Setter/Getter for SHARE_STATUS - table Field
     */
    public void setShareStatus(String shareStatus) {
        this.shareStatus = shareStatus;
    }

    public String getShareStatus() {
        return shareStatus;
    }

    /**
     * Setter/Getter for SHARE_CERT_ISSUE_DT - table Field
     */
    public void setShareCertIssueDt(Date shareCertIssueDt) {
        this.shareCertIssueDt = shareCertIssueDt;
    }

    public Date getShareCertIssueDt() {
        return shareCertIssueDt;
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
     * Setter/Getter for AUTHORIZE - table Field
     */
    public void setAuthorize(String authorize) {
        this.authorize = authorize;
    }

    public String getAuthorize() {
        return authorize;
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
     * Setter/Getter for SHARE_ACCT_DET_NO - table Field
     */
    public void setShareAcctDetNo(String shareAcctDetNo) {
        this.shareAcctDetNo = shareAcctDetNo;
    }

    public String getShareAcctDetNo() {
        return shareAcctDetNo;
    }
    
    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("shareAcctNo");
        return shareAcctNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("shareAcctNo", shareAcctNo));
        strB.append(getTOString("noOfShares", noOfShares));
        strB.append(getTOString("shareCertIssueDt", shareCertIssueDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorize", authorize));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("shareAcctDetNo", shareAcctDetNo));
        strB.append(getTOString("shareNoFrom", shareNoFrom));
        strB.append(getTOString("shareNoTo", shareNoTo));
        strB.append(getTOString("resolutionNo", resolutionNo));
        strB.append(getTOString("shareStatus", shareStatus));
        strB.append(getTOString("txtShareValue", txtShareValue));
        strB.append(getTOString("txtTotShareFee", txtTotShareFee));
        strB.append(getTOString("txtShareApplFee", txtShareApplFee));
        strB.append(getTOString("txtShareMemFee", txtShareMemFee));
        strB.append(getTOString("txtShareTotAmount", txtShareTotAmount));
        strB.append(getTOString("txtNoShares", txtNoShares));
        strB.append(getTOString("txtApplicationNo", txtApplicationNo));
        strB.append(getTOString("loanAcctNumber", loanAcctNumber));
        strB.append(getTOString("txtFromSL_No", txtFromSL_No));
        strB.append(getTOString("txtToSL_No", txtToSL_No));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("shareAcctNo", shareAcctNo));
        strB.append(getTOXml("noOfShares", noOfShares));
        strB.append(getTOXml("shareCertIssueDt", shareCertIssueDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorize", authorize));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("shareAcctDetNo", shareAcctDetNo));
        strB.append(getTOXml("shareNoFrom", shareNoFrom));
        strB.append(getTOXml("shareNoTo", shareNoTo));
        strB.append(getTOXml("resolutionNo", resolutionNo));
        strB.append(getTOXml("shareStatus", shareStatus));
        strB.append(getTOXml("txtShareValue", txtShareValue));
        strB.append(getTOXml("txtTotShareFee", txtTotShareFee));
        strB.append(getTOXml("txtShareApplFee", txtShareApplFee));
        strB.append(getTOXml("txtShareMemFee", txtShareMemFee));
        strB.append(getTOXml("txtShareTotAmount", txtShareTotAmount));
        strB.append(getTOXml("txtNoShares", txtNoShares));
        strB.append(getTOXml("txtApplicationNo", txtApplicationNo));
        strB.append(getTOXml("loanAcctNumber", loanAcctNumber));
        strB.append(getTOXml("txtFromSL_No", txtFromSL_No));
        strB.append(getTOXml("txtToSL_No", txtToSL_No));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property resolutionNo.
     *
     * @return Value of property resolutionNo.
     */
    public java.lang.String getResolutionNo() {
        return resolutionNo;
    }

    /**
     * Setter for property resolutionNo.
     *
     * @param resolutionNo New value of property resolutionNo.
     */
    public void setResolutionNo(java.lang.String resolutionNo) {
        this.resolutionNo = resolutionNo;
    }
    /**
     * Getter for property txtShareTotAmount.
     *
     * @return Value of property txtShareTotAmount.
     */
    public java.lang.String getTxtShareTotAmount() {
        return txtShareTotAmount;
    }

    /**
     * Setter for property txtShareTotAmount.
     *
     * @param txtShareTotAmount New value of property txtShareTotAmount.
     */
    public void setTxtShareTotAmount(java.lang.String txtShareTotAmount) {
        this.txtShareTotAmount = txtShareTotAmount;
    }

    /**
     * Getter for property txtNoShares.
     *
     * @return Value of property txtNoShares.
     */
    public java.lang.String getTxtNoShares() {
        return txtNoShares;
    }

    /**
     * Setter for property txtNoShares.
     *
     * @param txtNoShares New value of property txtNoShares.
     */
    public void setTxtNoShares(java.lang.String txtNoShares) {
        this.txtNoShares = txtNoShares;
    }
    
    /**
     * Getter for property loanAcctNumber.
     *
     * @return Value of property loanAcctNumber.
     */
    public java.lang.String getLoanAcctNumber() {
        return loanAcctNumber;
    }

    /**
     * Setter for property loanAcctNumber.
     *
     * @param loanAcctNumber New value of property loanAcctNumber.
     */
    public void setLoanAcctNumber(java.lang.String loanAcctNumber) {
        this.loanAcctNumber = loanAcctNumber;
    }

    public Double getTxtShareMemFee() {
        return txtShareMemFee;
    }

    public void setTxtShareMemFee(Double txtShareMemFee) {
        this.txtShareMemFee = txtShareMemFee;
    }

    public Double getTxtShareApplFee() {
        return txtShareApplFee;
    }


    public void setTxtShareApplFee(Double txtShareApplFee) {
        this.txtShareApplFee = txtShareApplFee;
    }

    public String getShareNoFrom() {
        return shareNoFrom;
    }

    public void setShareNoFrom(String shareNoFrom) {
        this.shareNoFrom = shareNoFrom;
    }
    
    
    
    
}