/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterTO.java
 * 
 * Created on Fri Aug 05 15:08:19 GMT+05:30 2011
 */
package com.see.truetransact.transferobject.share;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DRF_PRODUCT.
 */
public class DrfTransactionTO extends TransferObject implements Serializable {

    private String txtDrfTransMemberNo = "";
    private String txtDrfTransName = "";
    private Double txtDrfTransAmount = 0.0;
    private String cboDrfTransProdID = "";
    private String status = "";
    private Date statusDate = null;
    private String statusBy = "";
    private String authorizeBy = "";
    private String authorizeStatus = null;
    private Date authorizeDate = null;
    private String rdoDrfTransaction = "";
    private String chkDueAmtPayment = "";
    private String drfTransID = "";
    private String drfProdPaymentAmt = "";
    private String drfProductAmount = "";
    private String drfInterestTransID = "";
    private String cboDrfProdId = "";
    private String drfInterestId = "";
    private String interestMemberNo = "";
    private String interestBalanceAmount = "";
    private String interest = "";
    private String resolutionNo = "";
    private Date resolutionDate = null;
    //private Date statusDate = null;

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
     * Setter/Getter for STATUS_DATE - table Field
     */
    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public Date getStatusDate() {
        return statusDate;
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
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
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
     * Setter/Getter for AUTHORIZE_DATE - table Field
     */
    public void setAuthorizeDate(Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    public Date getAuthorizeDate() {
        return authorizeDate;
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
        strB.append(getTOString("txtDrfTransMemberNo", txtDrfTransMemberNo));
        strB.append(getTOString("txtDrfTransName", txtDrfTransName));
        strB.append(getTOString("txtDrfTransAmount", txtDrfTransAmount));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("rdoDrfTransaction", rdoDrfTransaction));
        strB.append(getTOString("chkDueAmtPayment", chkDueAmtPayment));
        strB.append(getTOString("cboDrfTransProdID", cboDrfTransProdID));
        strB.append(getTOString("drfProdPaymentAmt", drfProdPaymentAmt));
        strB.append(getTOString("drfProductAmount", drfProductAmount));
        strB.append(getTOString("drfTransID", drfTransID));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("txtDrfTransMemberNo", txtDrfTransMemberNo));
        strB.append(getTOXml("txtDrfTransName", txtDrfTransName));
        strB.append(getTOXml("txtDrfTransAmount", txtDrfTransAmount));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("rdoDrfTransaction", rdoDrfTransaction));
        strB.append(getTOXml("chkDueAmtPayment", chkDueAmtPayment));
        strB.append(getTOXml("cboDrfTransProdID", cboDrfTransProdID));
        strB.append(getTOXml("drfProdPaymentAmt", drfProdPaymentAmt));
        strB.append(getTOXml("drfProductAmount", drfProductAmount));
        strB.append(getTOXml("drfTransID", drfTransID));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property txtDrfTransMemberNo.
     *
     * @return Value of property txtDrfTransMemberNo.
     */
    public java.lang.String getTxtDrfTransMemberNo() {
        return txtDrfTransMemberNo;
    }

    /**
     * Setter for property txtDrfTransMemberNo.
     *
     * @param txtDrfTransMemberNo New value of property txtDrfTransMemberNo.
     */
    public void setTxtDrfTransMemberNo(java.lang.String txtDrfTransMemberNo) {
        this.txtDrfTransMemberNo = txtDrfTransMemberNo;
    }

    /**
     * Getter for property txtDrfTransName.
     *
     * @return Value of property txtDrfTransName.
     */
    public java.lang.String getTxtDrfTransName() {
        return txtDrfTransName;
    }

    /**
     * Setter for property txtDrfTransName.
     *
     * @param txtDrfTransName New value of property txtDrfTransName.
     */
    public void setTxtDrfTransName(java.lang.String txtDrfTransName) {
        this.txtDrfTransName = txtDrfTransName;
    }

    /**
     * Getter for property txtDrfTransAmount.
     *
     * @return Value of property txtDrfTransAmount.
     */


    /**
     * Getter for property rdoDrfTransaction.
     *
     * @return Value of property rdoDrfTransaction.
     */
    public java.lang.String getRdoDrfTransaction() {
        return rdoDrfTransaction;
    }

    public Double getTxtDrfTransAmount() {
        return txtDrfTransAmount;
    }

    public void setTxtDrfTransAmount(Double txtDrfTransAmount) {
        this.txtDrfTransAmount = txtDrfTransAmount;
    }
    
    

    /**
     * Setter for property rdoDrfTransaction.
     *
     * @param rdoDrfTransaction New value of property rdoDrfTransaction.
     */
    public void setRdoDrfTransaction(java.lang.String rdoDrfTransaction) {
        this.rdoDrfTransaction = rdoDrfTransaction;
    }

    /**
     * Getter for property chkDueAmtPayment.
     *
     * @return Value of property chkDueAmtPayment.
     */
    public java.lang.String getChkDueAmtPayment() {
        return chkDueAmtPayment;
    }

    /**
     * Setter for property chkDueAmtPayment.
     *
     * @param chkDueAmtPayment New value of property chkDueAmtPayment.
     */
    public void setChkDueAmtPayment(java.lang.String chkDueAmtPayment) {
        this.chkDueAmtPayment = chkDueAmtPayment;
    }

    /**
     * Getter for property cboDrfTransProdID.
     *
     * @return Value of property cboDrfTransProdID.
     */
    public java.lang.String getCboDrfTransProdID() {
        return cboDrfTransProdID;
    }

    /**
     * Setter for property cboDrfTransProdID.
     *
     * @param cboDrfTransProdID New value of property cboDrfTransProdID.
     */
    public void setCboDrfTransProdID(java.lang.String cboDrfTransProdID) {
        this.cboDrfTransProdID = cboDrfTransProdID;
    }

    /**
     * Getter for property drfTransID.
     *
     * @return Value of property drfTransID.
     */
    public java.lang.String getDrfTransID() {
        return drfTransID;
    }

    /**
     * Setter for property drfTransID.
     *
     * @param drfTransID New value of property drfTransID.
     */
    public void setDrfTransID(java.lang.String drfTransID) {
        this.drfTransID = drfTransID;
    }

    /**
     * Getter for property drfProdPaymentAmt.
     *
     * @return Value of property drfProdPaymentAmt.
     */
    public java.lang.String getDrfProdPaymentAmt() {
        return drfProdPaymentAmt;
    }

    /**
     * Setter for property drfProdPaymentAmt.
     *
     * @param drfProdPaymentAmt New value of property drfProdPaymentAmt.
     */
    public void setDrfProdPaymentAmt(java.lang.String drfProdPaymentAmt) {
        this.drfProdPaymentAmt = drfProdPaymentAmt;
    }

    /**
     * Getter for property drfProductAmount.
     *
     * @return Value of property drfProductAmount.
     */
    public java.lang.String getDrfProductAmount() {
        return drfProductAmount;
    }

    /**
     * Setter for property drfProductAmount.
     *
     * @param drfProductAmount New value of property drfProductAmount.
     */
    public void setDrfProductAmount(java.lang.String drfProductAmount) {
        this.drfProductAmount = drfProductAmount;
    }

    /**
     * Getter for property drfInterestTransID.
     *
     * @return Value of property drfInterestTransID.
     */
    public java.lang.String getDrfInterestTransID() {
        return drfInterestTransID;
    }

    /**
     * Setter for property drfInterestTransID.
     *
     * @param drfInterestTransID New value of property drfInterestTransID.
     */
    public void setDrfInterestTransID(java.lang.String drfInterestTransID) {
        this.drfInterestTransID = drfInterestTransID;
    }

    /**
     * Getter for property cboDrfProdId.
     *
     * @return Value of property cboDrfProdId.
     */
    public java.lang.String getCboDrfProdId() {
        return cboDrfProdId;
    }

    /**
     * Setter for property cboDrfProdId.
     *
     * @param cboDrfProdId New value of property cboDrfProdId.
     */
    public void setCboDrfProdId(java.lang.String cboDrfProdId) {
        this.cboDrfProdId = cboDrfProdId;
    }

    /**
     * Getter for property drfInterestId.
     *
     * @return Value of property drfInterestId.
     */
    public java.lang.String getDrfInterestId() {
        return drfInterestId;
    }

    /**
     * Setter for property drfInterestId.
     *
     * @param drfInterestId New value of property drfInterestId.
     */
    public void setDrfInterestId(java.lang.String drfInterestId) {
        this.drfInterestId = drfInterestId;
    }

    /**
     * Getter for property interestMemberNo.
     *
     * @return Value of property interestMemberNo.
     */
    public java.lang.String getInterestMemberNo() {
        return interestMemberNo;
    }

    /**
     * Setter for property interestMemberNo.
     *
     * @param interestMemberNo New value of property interestMemberNo.
     */
    public void setInterestMemberNo(java.lang.String interestMemberNo) {
        this.interestMemberNo = interestMemberNo;
    }

    /**
     * Getter for property interestBalanceAmount.
     *
     * @return Value of property interestBalanceAmount.
     */
    public java.lang.String getInterestBalanceAmount() {
        return interestBalanceAmount;
    }

    /**
     * Setter for property interestBalanceAmount.
     *
     * @param interestBalanceAmount New value of property interestBalanceAmount.
     */
    public void setInterestBalanceAmount(java.lang.String interestBalanceAmount) {
        this.interestBalanceAmount = interestBalanceAmount;
    }

    /**
     * Getter for property interest.
     *
     * @return Value of property interest.
     */
    public java.lang.String getInterest() {
        return interest;
    }

    /**
     * Setter for property interest.
     *
     * @param interest New value of property interest.
     */
    public void setInterest(java.lang.String interest) {
        this.interest = interest;
    }

    public String getResolutionNo() {
        return resolutionNo;
    }

    public void setResolutionNo(String resolutionNo) {
        this.resolutionNo = resolutionNo;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }
}