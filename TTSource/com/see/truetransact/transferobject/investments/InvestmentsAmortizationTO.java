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
public class InvestmentsAmortizationTO extends TransferObject implements Serializable {

    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String investmentBehaves = "";
    private String investmentID = "";
    private String investmentName = "";
    private String batchID = "";
    private Date shiftingDate = null;
    private Double valuationRate = null;
    private String oldClassfication = "";
    private String newClassfication = "";
    private Date transDT = null;
    private String initiatedBranch = "";

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
        strB.append(getTOString("investmentBehaves", investmentBehaves));
        strB.append(getTOString("investmentID", investmentID));
        strB.append(getTOString("investmentName", investmentName));
        strB.append(getTOString("batchID", batchID));
        strB.append(getTOString("shiftingDate", shiftingDate));
        strB.append(getTOString("oldClassfication", oldClassfication));
        strB.append(getTOString("newClassfication", newClassfication));
        strB.append(getTOString("transDT", transDT));
        strB.append(getTOString("initiatedBranch", initiatedBranch));

        strB.append(getTOString("valuationRate", valuationRate));
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
        strB.append(getTOXml("investmentBehaves", investmentBehaves));
        strB.append(getTOXml("investmentID", investmentID));
        strB.append(getTOXml("investmentName", investmentName));
        strB.append(getTOXml("batchID", batchID));
        strB.append(getTOXml("valuationRate", valuationRate));
        strB.append(getTOXml("shiftingDate", shiftingDate));
        strB.append(getTOXml("oldClassfication", oldClassfication));
        strB.append(getTOXml("newClassfication", newClassfication));
        strB.append(getTOXml("transDT", transDT));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
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
     * Getter for property investmentBehaves.
     *
     * @return Value of property investmentBehaves.
     */
    public java.lang.String getInvestmentBehaves() {
        return investmentBehaves;
    }

    /**
     * Setter for property investmentBehaves.
     *
     * @param investmentBehaves New value of property investmentBehaves.
     */
    public void setInvestmentBehaves(java.lang.String investmentBehaves) {
        this.investmentBehaves = investmentBehaves;
    }

    /**
     * Getter for property investmentID.
     *
     * @return Value of property investmentID.
     */
    public java.lang.String getInvestmentID() {
        return investmentID;
    }

    /**
     * Setter for property investmentID.
     *
     * @param investmentID New value of property investmentID.
     */
    public void setInvestmentID(java.lang.String investmentID) {
        this.investmentID = investmentID;
    }

    /**
     * Getter for property investmentName.
     *
     * @return Value of property investmentName.
     */
    public java.lang.String getInvestmentName() {
        return investmentName;
    }

    /**
     * Setter for property investmentName.
     *
     * @param investmentName New value of property investmentName.
     */
    public void setInvestmentName(java.lang.String investmentName) {
        this.investmentName = investmentName;
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
     * Getter for property valuationRate.
     *
     * @return Value of property valuationRate.
     */
    public java.lang.Double getValuationRate() {
        return valuationRate;
    }

    /**
     * Setter for property valuationRate.
     *
     * @param valuationRate New value of property valuationRate.
     */
    public void setValuationRate(java.lang.Double valuationRate) {
        this.valuationRate = valuationRate;
    }

    /**
     * Getter for property shiftingDate.
     *
     * @return Value of property shiftingDate.
     */
    public java.util.Date getShiftingDate() {
        return shiftingDate;
    }

    /**
     * Setter for property shiftingDate.
     *
     * @param shiftingDate New value of property shiftingDate.
     */
    public void setShiftingDate(java.util.Date shiftingDate) {
        this.shiftingDate = shiftingDate;
    }

    /**
     * Getter for property oldClassfication.
     *
     * @return Value of property oldClassfication.
     */
    public java.lang.String getOldClassfication() {
        return oldClassfication;
    }

    /**
     * Setter for property oldClassfication.
     *
     * @param oldClassfication New value of property oldClassfication.
     */
    public void setOldClassfication(java.lang.String oldClassfication) {
        this.oldClassfication = oldClassfication;
    }

    /**
     * Getter for property newClassfication.
     *
     * @return Value of property newClassfication.
     */
    public java.lang.String getNewClassfication() {
        return newClassfication;
    }

    /**
     * Setter for property newClassfication.
     *
     * @param newClassfication New value of property newClassfication.
     */
    public void setNewClassfication(java.lang.String newClassfication) {
        this.newClassfication = newClassfication;
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
     * Getter for property initiatedBranch.
     *
     * @return Value of property initiatedBranch.
     */
    public java.lang.String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * Setter for property initiatedBranch.
     *
     * @param initiatedBranch New value of property initiatedBranch.
     */
    public void setInitiatedBranch(java.lang.String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }
}