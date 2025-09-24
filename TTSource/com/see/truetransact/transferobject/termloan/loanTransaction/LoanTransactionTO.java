/*
 * Copyright 2010 Fincuro Solutions(P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions (P) Ltd..
 * 
 *
 * LoanTransaction.java
 * 
 * Created on Mon jan 10 11:10:46 GMT+05:30 2019
 */
package com.see.truetransact.transferobject.termloan.loanTransaction;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LoanTransaction.
 */
public class LoanTransactionTO extends TransferObject implements Serializable {

    private String actNum = "";
    private Double intPayable = null;
    private Double payableBal = null;
    private String authorizeStatus = null;
    private Date authorizeDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String prodId = "";
    private Double rebateInterest = null;
    private String drAccHead = null;
    private Double penalInt = null;
    private Date intCalcUptoDt = null;
    private String prodType ="";
    private Double totalWaiveAmount=null;
    private String transactionId="";

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getIntCalcUptoDt() {
        return intCalcUptoDt;
    }

    public void setIntCalcUptoDt(Date intCalcUptoDt) {
        this.intCalcUptoDt = intCalcUptoDt;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }
    
    public Double getPenalInt() {
        return penalInt;
    }

    public void setPenalInt(Double penalInt) {
        this.penalInt = penalInt;
    }
    
    
    
    public String getDrAccHead() {
        return drAccHead;
    }

    public void setDrAccHead(String drAccHead) {
        this.drAccHead = drAccHead;
    }
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
     * Setter/Getter for INT_PAYABLE - table Field
     */
    public void setIntPayable(Double intPayable) {
        this.intPayable = intPayable;
    }

    public Double getIntPayable() {
        return intPayable;
    }

    /**
     * Setter/Getter for PAYABLE_BAL - table Field
     */
    public void setPayableBal(Double payableBal) {
        this.payableBal = payableBal;
    }

    public Double getPayableBal() {
        return payableBal;
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

    public Double getTotalWaiveAmount() {
        return totalWaiveAmount;
    }

    public void setTotalWaiveAmount(Double totalWaiveAmount) {
        this.totalWaiveAmount = totalWaiveAmount;
    }
    
   

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("actNum");
        return "actNum";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("intPayable", intPayable));
        strB.append(getTOString("payableBal", payableBal));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("rebateInterest", rebateInterest));
        strB.append(getTOString("drAccHead", drAccHead));
        strB.append(getTOString("penalInt", penalInt));
        strB.append(getTOString("intCalcUptoDt", intCalcUptoDt));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("totalWaiveAmount", totalWaiveAmount));
        strB.append(getTOString("transactionId", transactionId));
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
        strB.append(getTOXml("intPayable", intPayable));
        strB.append(getTOXml("payableBal", payableBal));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("rebateInterest", rebateInterest));
        strB.append(getTOXml("drAccHead", drAccHead));
        strB.append(getTOXml("penalInt", penalInt));
        strB.append(getTOXml("intCalcUptoDt", intCalcUptoDt));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("transactionId", transactionId));
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
     * Getter for property prodId.
     *
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     *
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }
    
    /**
     * Getter for property rebateInterest.
     *
     * @return Value of property rebateInterest.
     */
    public java.lang.Double getRebateInterest() {
        return rebateInterest;
    }

    /**
     * Setter for property rebateInterest.
     *
     * @param rebateInterest New value of property rebateInterest.
     */
    public void setRebateInterest(java.lang.Double rebateInterest) {
        this.rebateInterest = rebateInterest;
    }
}