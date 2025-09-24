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
public class DrfRecoveryTO extends TransferObject implements Serializable {

    private String txtDrfTransMemberNo = "";
    private String txtDrfTransName = "";
    private String txtDrfTransAmount = "";
    private String cboDrfTransProdID = "";
    private String status = "";
    private String recover_status = "";
    private Date statusDate = null;
    private String statusBy = "";
    private String authorizeBy = "";
    private String authorizeStatus = null;;
    private Date authorizeDate = null;
    private Date drfPaidDate = null;
    //private String rdoDrfTransaction =  "";
    //private String chkDueAmtPayment = "";
    private String drfTransID = "";
    private String drfProdPaymentAmt = "";
    private String drfProductAmount = "";
    private String lblDrfTransAddressCont = "";
    private String linkid = "";

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
    public java.lang.String getTxtDrfTransAmount() {
        return txtDrfTransAmount;
    }

    /**
     * Setter for property txtDrfTransAmount.
     *
     * @param txtDrfTransAmount New value of property txtDrfTransAmount.
     */
    public void setTxtDrfTransAmount(java.lang.String txtDrfTransAmount) {
        this.txtDrfTransAmount = txtDrfTransAmount;
    }

    /**
     * Getter for property rdoDrfTransaction.
     *
     * @return Value of property rdoDrfTransaction.
     */
    /**
     * Setter for property rdoDrfTransaction.
     *
     * @param rdoDrfTransaction New value of property rdoDrfTransaction.
     */
    public void setRdoDrfTransaction(java.lang.String rdoDrfTransaction) {
        //            this.rdoDrfTransaction = rdoDrfTransaction;
    }

    /**
     * Getter for property chkDueAmtPayment.
     *
     * @return Value of property chkDueAmtPayment.
     */
    /**
     * Setter for property chkDueAmtPayment.
     *
     * @param chkDueAmtPayment New value of property chkDueAmtPayment.
     */
    public void setChkDueAmtPayment(java.lang.String chkDueAmtPayment) {
        //            this.chkDueAmtPayment = chkDueAmtPayment;
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
     * Getter for property drfPaidDate.
     *
     * @return Value of property drfPaidDate.
     */
    public java.util.Date getDrfPaidDate() {
        return drfPaidDate;
    }

    /**
     * Setter for property drfPaidDate.
     *
     * @param drfPaidDate New value of property drfPaidDate.
     */
    public void setDrfPaidDate(java.util.Date drfPaidDate) {
        this.drfPaidDate = drfPaidDate;
    }

    /**
     * Getter for property recover_status.
     *
     * @return Value of property recover_status.
     */
    public java.lang.String getRecover_status() {
        return recover_status;
    }

    /**
     * Setter for property recover_status.
     *
     * @param recover_status New value of property recover_status.
     */
    public void setRecover_status(java.lang.String recover_status) {
        this.recover_status = recover_status;
    }

    /**
     * Getter for property lblDrfTransAddressCont.
     *
     * @return Value of property lblDrfTransAddressCont.
     */
    public java.lang.String getLblDrfTransAddressCont() {
        return lblDrfTransAddressCont;
    }

    /**
     * Setter for property lblDrfTransAddressCont.
     *
     * @param lblDrfTransAddressCont New value of property
     * lblDrfTransAddressCont.
     */
    public void setLblDrfTransAddressCont(java.lang.String lblDrfTransAddressCont) {
        this.lblDrfTransAddressCont = lblDrfTransAddressCont;
    }

    /**
     * Getter for property linkid.
     *
     * @return Value of property linkid.
     */
    public String getLinkid() {
        return linkid;
    }

    /**
     * Setter for property linkid.
     *
     * @param linkid New value of property linkid.
     */
    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }
}