/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReleaseDetailsTO.java
 * 
 * Created on Fri Apr 19 11:22:39 IST 2013
 */
package com.see.truetransact.transferobject.termloan.kcctopacs;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is NCL_RELEASE_DETAILS.
 */
public class ReleaseDetailsTO extends TransferObject implements Serializable {

    private String releaseNo = "";
    private String nclSanctionNo = "";
    private Double amountRequested = null;
    private Date requestedDate = null;
    private Double amountReleased = null;
    private Date releaseDate = null;
    private Double repaymentPeriod = null;
    private String repaymentPeriodType = "";
    private Date dueDate = null;
    private Double noOfInst = null;
    private String principalFreqType = "";
    private String intFreqType = "";
    private Double roi = null;
    private Double penalInt = null;
    private String loanCategory = "";
    private String subCategory = "";
    private String crop = "";
    private Double totalIntPayable = null;
    private Double totalAmtPayable = null;
    private String remarks = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;

    /**
     * Setter/Getter for RELEASE_NO - table Field
     */
    public void setReleaseNo(String releaseNo) {
        this.releaseNo = releaseNo;
    }

    public String getReleaseNo() {
        return releaseNo;
    }

    /**
     * Setter/Getter for NCL_SANCTION_NO - table Field
     */
    public void setNclSanctionNo(String nclSanctionNo) {
        this.nclSanctionNo = nclSanctionNo;
    }

    public String getNclSanctionNo() {
        return nclSanctionNo;
    }

    /**
     * Setter/Getter for AMOUNT_REQUESTED - table Field
     */
    public void setAmountRequested(Double amountRequested) {
        this.amountRequested = amountRequested;
    }

    public Double getAmountRequested() {
        return amountRequested;
    }

    /**
     * Setter/Getter for REQUESTED_DATE - table Field
     */
    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    /**
     * Setter/Getter for AMOUNT_RELEASED - table Field
     */
    public void setAmountReleased(Double amountReleased) {
        this.amountReleased = amountReleased;
    }

    public Double getAmountReleased() {
        return amountReleased;
    }

    /**
     * Setter/Getter for RELEASE_DATE - table Field
     */
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * Setter/Getter for REPAYMENT_PERIOD - table Field
     */
    public void setRepaymentPeriod(Double repaymentPeriod) {
        this.repaymentPeriod = repaymentPeriod;
    }

    public Double getRepaymentPeriod() {
        return repaymentPeriod;
    }

    /**
     * Setter/Getter for REPAYMENT_PERIOD_TYPE - table Field
     */
    public void setRepaymentPeriodType(String repaymentPeriodType) {
        this.repaymentPeriodType = repaymentPeriodType;
    }

    public String getRepaymentPeriodType() {
        return repaymentPeriodType;
    }

    /**
     * Setter/Getter for DUE_DATE - table Field
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Setter/Getter for NO_OF_INST - table Field
     */
    public void setNoOfInst(Double noOfInst) {
        this.noOfInst = noOfInst;
    }

    public Double getNoOfInst() {
        return noOfInst;
    }

    /**
     * Setter/Getter for PRINCIPAL_FREQ_TYPE - table Field
     */
    public void setPrincipalFreqType(String principalFreqType) {
        this.principalFreqType = principalFreqType;
    }

    public String getPrincipalFreqType() {
        return principalFreqType;
    }

    /**
     * Setter/Getter for INT_FREQ_TYPE - table Field
     */
    public void setIntFreqType(String intFreqType) {
        this.intFreqType = intFreqType;
    }

    public String getIntFreqType() {
        return intFreqType;
    }

    /**
     * Setter/Getter for ROI - table Field
     */
    public void setRoi(Double roi) {
        this.roi = roi;
    }

    public Double getRoi() {
        return roi;
    }

    /**
     * Setter/Getter for PENAL_INT - table Field
     */
    public void setPenalInt(Double penalInt) {
        this.penalInt = penalInt;
    }

    public Double getPenalInt() {
        return penalInt;
    }

    /**
     * Setter/Getter for LOAN_CATEGORY - table Field
     */
    public void setLoanCategory(String loanCategory) {
        this.loanCategory = loanCategory;
    }

    public String getLoanCategory() {
        return loanCategory;
    }

    /**
     * Setter/Getter for SUB_CATEGORY - table Field
     */
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    /**
     * Setter/Getter for CROP - table Field
     */
    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getCrop() {
        return crop;
    }

    /**
     * Setter/Getter for TOTAL_INT_PAYABLE - table Field
     */
    public void setTotalIntPayable(Double totalIntPayable) {
        this.totalIntPayable = totalIntPayable;
    }

    public Double getTotalIntPayable() {
        return totalIntPayable;
    }

    /**
     * Setter/Getter for TOTAL_AMT_PAYABLE - table Field
     */
    public void setTotalAmtPayable(Double totalAmtPayable) {
        this.totalAmtPayable = totalAmtPayable;
    }

    public Double getTotalAmtPayable() {
        return totalAmtPayable;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
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
        strB.append(getTOString("releaseNo", releaseNo));
        strB.append(getTOString("nclSanctionNo", nclSanctionNo));
        strB.append(getTOString("amountRequested", amountRequested));
        strB.append(getTOString("requestedDate", requestedDate));
        strB.append(getTOString("amountReleased", amountReleased));
        strB.append(getTOString("releaseDate", releaseDate));
        strB.append(getTOString("repaymentPeriod", repaymentPeriod));
        strB.append(getTOString("repaymentPeriodType", repaymentPeriodType));
        strB.append(getTOString("dueDate", dueDate));
        strB.append(getTOString("noOfInst", noOfInst));
        strB.append(getTOString("principalFreqType", principalFreqType));
        strB.append(getTOString("intFreqType", intFreqType));
        strB.append(getTOString("roi", roi));
        strB.append(getTOString("penalInt", penalInt));
        strB.append(getTOString("loanCategory", loanCategory));
        strB.append(getTOString("subCategory", subCategory));
        strB.append(getTOString("crop", crop));
        strB.append(getTOString("totalIntPayable", totalIntPayable));
        strB.append(getTOString("totalAmtPayable", totalAmtPayable));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("releaseNo", releaseNo));
        strB.append(getTOXml("nclSanctionNo", nclSanctionNo));
        strB.append(getTOXml("amountRequested", amountRequested));
        strB.append(getTOXml("requestedDate", requestedDate));
        strB.append(getTOXml("amountReleased", amountReleased));
        strB.append(getTOXml("releaseDate", releaseDate));
        strB.append(getTOXml("repaymentPeriod", repaymentPeriod));
        strB.append(getTOXml("repaymentPeriodType", repaymentPeriodType));
        strB.append(getTOXml("dueDate", dueDate));
        strB.append(getTOXml("noOfInst", noOfInst));
        strB.append(getTOXml("principalFreqType", principalFreqType));
        strB.append(getTOXml("intFreqType", intFreqType));
        strB.append(getTOXml("roi", roi));
        strB.append(getTOXml("penalInt", penalInt));
        strB.append(getTOXml("loanCategory", loanCategory));
        strB.append(getTOXml("subCategory", subCategory));
        strB.append(getTOXml("crop", crop));
        strB.append(getTOXml("totalIntPayable", totalIntPayable));
        strB.append(getTOXml("totalAmtPayable", totalAmtPayable));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}