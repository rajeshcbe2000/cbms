/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductLoanTO.java
 *
 * Created on Tue Mar 15 14:08:12 IST 2005
 */
package com.see.truetransact.transferobject.investments;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_PROD_LOANS.
 */
public class CallMoneyTO extends TransferObject implements Serializable {

    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String batchID = "";
    private Date transDT = null;
    private Date callMoneydate = null;
    private String callMoneyInstituation = "";
    private String callMoneyCommunication = "";
    private Double noOfDays = null;
    private Double interestRate = null;
    private Double callMoneyAmount = null;
    private Double interestAmt = null;
    private String transMode = "";
    private String transType = "";
    private String particulars = "";
    private String reconcileStatus = "";
    private String reconcileBatchId = "";
    private String callMoneyType = "";
    private Double noOfDayExtension = null;
    private Double extensioninterestRate = null;
    private String initBran = "";
    private String txtCallMoneyInstId = "";

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
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("batchID", batchID));
        strB.append(getTOString("transDT", transDT));
        strB.append(getTOString("callMoneydate", callMoneydate));
        strB.append(getTOString("callMoneyInstituation", callMoneyInstituation));
        strB.append(getTOString("callMoneyCommunication", callMoneyCommunication));
        strB.append(getTOString("noOfDays", noOfDays));
        strB.append(getTOString("interestRate", interestRate));
        strB.append(getTOString("callMoneyAmount", callMoneyAmount));
        strB.append(getTOString("interestAmt", interestAmt));
        strB.append(getTOString("transMode", transMode));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("ReconcileStatus", reconcileStatus));
        strB.append(getTOString("ReconcileBatchId", reconcileBatchId));
        strB.append(getTOString("callMoneyType", callMoneyType));
        strB.append(getTOString("noOfDayExtension", noOfDayExtension));
        strB.append(getTOString("extensioninterestRate", extensioninterestRate));
        strB.append(getTOString("initBran", initBran));
        strB.append(getTOString("txtCallMoneyInstId", txtCallMoneyInstId));

        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("batchID", batchID));
        strB.append(getTOXml("transDT", transDT));
        strB.append(getTOXml("callMoneydate", callMoneydate));
        strB.append(getTOXml("callMoneyInstituation", callMoneyInstituation));
        strB.append(getTOXml("callMoneyCommunication", callMoneyCommunication));
        strB.append(getTOXml("noOfDays", noOfDays));
        strB.append(getTOXml("interestRate", interestRate));
        strB.append(getTOXml("callMoneyAmount", callMoneyAmount));
        strB.append(getTOXml("interestAmt", interestAmt));
        strB.append(getTOXml("transMode", transMode));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("ReconcileStatus", reconcileStatus));
        strB.append(getTOXml("ReconcileBatchId", reconcileBatchId));
        strB.append(getTOXml("callMoneyType", callMoneyType));
        strB.append(getTOXml("noOfDayExtension", noOfDayExtension));
        strB.append(getTOXml("extensioninterestRate", extensioninterestRate));
        strB.append(getTOXml("initBran", initBran));
        strB.append(getTOXml("txtCallMoneyInstId", txtCallMoneyInstId));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property transDT.
     *
     * @return Value of property transDT.
     */
    public java.util.Date getTransDT() {
        return transDT;
    }

    /**
     * Setter for property transDT.
     *
     * @param transDT New value of property transDT.
     */
    public void setTransDT(java.util.Date transDT) {
        this.transDT = transDT;
    }

    /**
     * Getter for property callMoneydate.
     *
     * @return Value of property callMoneydate.
     */
    public java.util.Date getCallMoneydate() {
        return callMoneydate;
    }

    /**
     * Setter for property callMoneydate.
     *
     * @param callMoneydate New value of property callMoneydate.
     */
    public void setCallMoneydate(java.util.Date callMoneydate) {
        this.callMoneydate = callMoneydate;
    }

    /**
     * Getter for property callMoneyInstituation.
     *
     * @return Value of property callMoneyInstituation.
     */
    public java.lang.String getCallMoneyInstituation() {
        return callMoneyInstituation;
    }

    /**
     * Setter for property callMoneyInstituation.
     *
     * @param callMoneyInstituation New value of property callMoneyInstituation.
     */
    public void setCallMoneyInstituation(java.lang.String callMoneyInstituation) {
        this.callMoneyInstituation = callMoneyInstituation;
    }

    /**
     * Getter for property callMoneyCommunication.
     *
     * @return Value of property callMoneyCommunication.
     */
    public java.lang.String getCallMoneyCommunication() {
        return callMoneyCommunication;
    }

    /**
     * Setter for property callMoneyCommunication.
     *
     * @param callMoneyCommunication New value of property
     * callMoneyCommunication.
     */
    public void setCallMoneyCommunication(java.lang.String callMoneyCommunication) {
        this.callMoneyCommunication = callMoneyCommunication;
    }

    /**
     * Getter for property noOfDays.
     *
     * @return Value of property noOfDays.
     */
    public java.lang.Double getNoOfDays() {
        return noOfDays;
    }

    /**
     * Setter for property noOfDays.
     *
     * @param noOfDays New value of property noOfDays.
     */
    public void setNoOfDays(java.lang.Double noOfDays) {
        this.noOfDays = noOfDays;
    }

    /**
     * Getter for property interestRate.
     *
     * @return Value of property interestRate.
     */
    public java.lang.Double getInterestRate() {
        return interestRate;
    }

    /**
     * Setter for property interestRate.
     *
     * @param interestRate New value of property interestRate.
     */
    public void setInterestRate(java.lang.Double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Getter for property callMoneyAmount.
     *
     * @return Value of property callMoneyAmount.
     */
    public java.lang.Double getCallMoneyAmount() {
        return callMoneyAmount;
    }

    /**
     * Setter for property callMoneyAmount.
     *
     * @param callMoneyAmount New value of property callMoneyAmount.
     */
    public void setCallMoneyAmount(java.lang.Double callMoneyAmount) {
        this.callMoneyAmount = callMoneyAmount;
    }

    /**
     * Getter for property interestAmt.
     *
     * @return Value of property interestAmt.
     */
    public java.lang.Double getInterestAmt() {
        return interestAmt;
    }

    /**
     * Setter for property interestAmt.
     *
     * @param interestAmt New value of property interestAmt.
     */
    public void setInterestAmt(java.lang.Double interestAmt) {
        this.interestAmt = interestAmt;
    }

    /**
     * Getter for property transMode.
     *
     * @return Value of property transMode.
     */
    public java.lang.String getTransMode() {
        return transMode;
    }

    /**
     * Setter for property transMode.
     *
     * @param transMode New value of property transMode.
     */
    public void setTransMode(java.lang.String transMode) {
        this.transMode = transMode;
    }

    /**
     * Getter for property transType.
     *
     * @return Value of property transType.
     */
    public java.lang.String getTransType() {
        return transType;
    }

    /**
     * Setter for property transType.
     *
     * @param transType New value of property transType.
     */
    public void setTransType(java.lang.String transType) {
        this.transType = transType;
    }

    /**
     * Getter for property particulars.
     *
     * @return Value of property particulars.
     */
    public java.lang.String getParticulars() {
        return particulars;
    }

    /**
     * Setter for property particulars.
     *
     * @param particulars New value of property particulars.
     */
    public void setParticulars(java.lang.String particulars) {
        this.particulars = particulars;
    }

    /**
     * Getter for property reconcileStatus.
     *
     * @return Value of property reconcileStatus.
     */
    public java.lang.String getReconcileStatus() {
        return reconcileStatus;
    }

    /**
     * Setter for property reconcileStatus.
     *
     * @param reconcileStatus New value of property reconcileStatus.
     */
    public void setReconcileStatus(java.lang.String reconcileStatus) {
        this.reconcileStatus = reconcileStatus;
    }

    /**
     * Getter for property reconcileBatchId.
     *
     * @return Value of property reconcileBatchId.
     */
    public java.lang.String getReconcileBatchId() {
        return reconcileBatchId;
    }

    /**
     * Setter for property reconcileBatchId.
     *
     * @param reconcileBatchId New value of property reconcileBatchId.
     */
    public void setReconcileBatchId(java.lang.String reconcileBatchId) {
        this.reconcileBatchId = reconcileBatchId;
    }

    /**
     * Getter for property callMoneyType.
     *
     * @return Value of property callMoneyType.
     */
    public java.lang.String getCallMoneyType() {
        return callMoneyType;
    }

    /**
     * Setter for property callMoneyType.
     *
     * @param callMoneyType New value of property callMoneyType.
     */
    public void setCallMoneyType(java.lang.String callMoneyType) {
        this.callMoneyType = callMoneyType;
    }

    /**
     * Getter for property noOfDayExtension.
     *
     * @return Value of property noOfDayExtension.
     */
    public java.lang.Double getNoOfDayExtension() {
        return noOfDayExtension;
    }

    /**
     * Setter for property noOfDayExtension.
     *
     * @param noOfDayExtension New value of property noOfDayExtension.
     */
    public void setNoOfDayExtension(java.lang.Double noOfDayExtension) {
        this.noOfDayExtension = noOfDayExtension;
    }

    /**
     * Getter for property extensioninterestRate.
     *
     * @return Value of property extensioninterestRate.
     */
    public java.lang.Double getExtensioninterestRate() {
        return extensioninterestRate;
    }

    /**
     * Setter for property extensioninterestRate.
     *
     * @param extensioninterestRate New value of property extensioninterestRate.
     */
    public void setExtensioninterestRate(java.lang.Double extensioninterestRate) {
        this.extensioninterestRate = extensioninterestRate;
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

    /**
     * Getter for property txtCallMoneyInstId.
     *
     * @return Value of property txtCallMoneyInstId.
     */
    public java.lang.String getTxtCallMoneyInstId() {
        return txtCallMoneyInstId;
    }

    /**
     * Setter for property txtCallMoneyInstId.
     *
     * @param txtCallMoneyInstId New value of property txtCallMoneyInstId.
     */
    public void setTxtCallMoneyInstId(java.lang.String txtCallMoneyInstId) {
        this.txtCallMoneyInstId = txtCallMoneyInstId;
    }
}