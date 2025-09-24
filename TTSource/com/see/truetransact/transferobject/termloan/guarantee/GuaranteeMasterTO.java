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
package com.see.truetransact.transferobject.termloan.guarantee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_PROD_LOANS.
 */
public class GuaranteeMasterTO extends TransferObject implements Serializable {

    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String cboPli = "";
    private String cbopliBranch = "";
    private String cboRepaymentFrequency = "";
    private String cboGuranteeSanctionBy = "";
    private String custId = "";
    private String sanctionNo = "";
    private Date sanctionDt = null;
    private String loanNo = "";
    private Date loanDt = null;
    private String sanctionAmt = "";
    private String holidayPeriod = "";
    private String noOfInst = "";
    private String intRate = "";
    private String guaranteeNo = "";
    private Date guaranteeDt = null;
    private String guaranteeSanctionNo = "";
    private String guranteAmt = "";
    private String guranteFeepayBy = "";
    private String guranteFeePer = "";
    private String guranteFee = "";

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
        strB.append(getTOString("cboPli", cboPli));
        strB.append(getTOString("cbopliBranch", cbopliBranch));
        strB.append(getTOString("cboRepaymentFrequency", cboRepaymentFrequency));
        strB.append(getTOString("cboGuranteeSanctionBy", cboGuranteeSanctionBy));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("sanctionNo", sanctionNo));
        strB.append(getTOString("sanctionDt", sanctionDt));
        strB.append(getTOString("loanNo", loanNo));
        strB.append(getTOString("loanDt", loanDt));
        strB.append(getTOString("sanctionAmt", sanctionAmt));
        strB.append(getTOString("holidayPeriod", holidayPeriod));
        strB.append(getTOString("noOfInst", noOfInst));
        strB.append(getTOString("intRate", intRate));
        strB.append(getTOString("guaranteeNo", guaranteeNo));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("guaranteeDt", guaranteeDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("guaranteeSanctionNo", guaranteeSanctionNo));
        strB.append(getTOString("guranteAmt", guranteAmt));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("guranteFeepayBy", guranteFeepayBy));
        strB.append(getTOString("guranteFeePer", guranteFeePer));
        strB.append(getTOString("guranteFee", guranteFee));

        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("cboPli", cboPli));
        strB.append(getTOXml("cbopliBranch", cbopliBranch));
        strB.append(getTOXml("cboRepaymentFrequency", cboRepaymentFrequency));
        strB.append(getTOXml("cboGuranteeSanctionBy", cboGuranteeSanctionBy));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("sanctionNo", sanctionNo));
        strB.append(getTOXml("sanctionDt", sanctionDt));
        strB.append(getTOXml("loanNo", loanNo));
        strB.append(getTOXml("loanDt", loanDt));
        strB.append(getTOXml("sanctionAmt", sanctionAmt));
        strB.append(getTOXml("holidayPeriod", holidayPeriod));
        strB.append(getTOXml("noOfInst", noOfInst));
        strB.append(getTOXml("intRate", intRate));
        strB.append(getTOXml("guaranteeNo", guaranteeNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("guaranteeDt", guaranteeDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("guaranteeSanctionNo", guaranteeSanctionNo));
        strB.append(getTOXml("guranteAmt", guranteAmt));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("guranteFeepayBy", guranteFeepayBy));
        strB.append(getTOXml("guranteFeePer", guranteFeePer));
        strB.append(getTOXml("guranteFee", guranteFee));
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
     * Getter for property cboPli.
     *
     * @return Value of property cboPli.
     */
    public java.lang.String getCboPli() {
        return cboPli;
    }

    /**
     * Setter for property cboPli.
     *
     * @param cboPli New value of property cboPli.
     */
    public void setCboPli(java.lang.String cboPli) {
        this.cboPli = cboPli;
    }

    /**
     * Getter for property cbopliBranch.
     *
     * @return Value of property cbopliBranch.
     */
    public java.lang.String getCbopliBranch() {
        return cbopliBranch;
    }

    /**
     * Setter for property cbopliBranch.
     *
     * @param cbopliBranch New value of property cbopliBranch.
     */
    public void setCbopliBranch(java.lang.String cbopliBranch) {
        this.cbopliBranch = cbopliBranch;
    }

    /**
     * Getter for property cboRepaymentFrequency.
     *
     * @return Value of property cboRepaymentFrequency.
     */
    public java.lang.String getCboRepaymentFrequency() {
        return cboRepaymentFrequency;
    }

    /**
     * Setter for property cboRepaymentFrequency.
     *
     * @param cboRepaymentFrequency New value of property cboRepaymentFrequency.
     */
    public void setCboRepaymentFrequency(java.lang.String cboRepaymentFrequency) {
        this.cboRepaymentFrequency = cboRepaymentFrequency;
    }

    /**
     * Getter for property cboGuranteeSanctionBy.
     *
     * @return Value of property cboGuranteeSanctionBy.
     */
    public java.lang.String getCboGuranteeSanctionBy() {
        return cboGuranteeSanctionBy;
    }

    /**
     * Setter for property cboGuranteeSanctionBy.
     *
     * @param cboGuranteeSanctionBy New value of property cboGuranteeSanctionBy.
     */
    public void setCboGuranteeSanctionBy(java.lang.String cboGuranteeSanctionBy) {
        this.cboGuranteeSanctionBy = cboGuranteeSanctionBy;
    }

    /**
     * Getter for property custId.
     *
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }

    /**
     * Setter for property custId.
     *
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
    }

    /**
     * Getter for property sanctionNo.
     *
     * @return Value of property sanctionNo.
     */
    public java.lang.String getSanctionNo() {
        return sanctionNo;
    }

    /**
     * Setter for property sanctionNo.
     *
     * @param sanctionNo New value of property sanctionNo.
     */
    public void setSanctionNo(java.lang.String sanctionNo) {
        this.sanctionNo = sanctionNo;
    }

    /**
     * Getter for property sanctionDt.
     *
     * @return Value of property sanctionDt.
     */
    public java.util.Date getSanctionDt() {
        return sanctionDt;
    }

    /**
     * Setter for property sanctionDt.
     *
     * @param sanctionDt New value of property sanctionDt.
     */
    public void setSanctionDt(java.util.Date sanctionDt) {
        this.sanctionDt = sanctionDt;
    }

    /**
     * Getter for property loanNo.
     *
     * @return Value of property loanNo.
     */
    public java.lang.String getLoanNo() {
        return loanNo;
    }

    /**
     * Setter for property loanNo.
     *
     * @param loanNo New value of property loanNo.
     */
    public void setLoanNo(java.lang.String loanNo) {
        this.loanNo = loanNo;
    }

    /**
     * Getter for property loanDt.
     *
     * @return Value of property loanDt.
     */
    public java.util.Date getLoanDt() {
        return loanDt;
    }

    /**
     * Setter for property loanDt.
     *
     * @param loanDt New value of property loanDt.
     */
    public void setLoanDt(java.util.Date loanDt) {
        this.loanDt = loanDt;
    }

    /**
     * Getter for property sanctionAmt.
     *
     * @return Value of property sanctionAmt.
     */
    public java.lang.String getSanctionAmt() {
        return sanctionAmt;
    }

    /**
     * Setter for property sanctionAmt.
     *
     * @param sanctionAmt New value of property sanctionAmt.
     */
    public void setSanctionAmt(java.lang.String sanctionAmt) {
        this.sanctionAmt = sanctionAmt;
    }

    /**
     * Getter for property holidayPeriod.
     *
     * @return Value of property holidayPeriod.
     */
    public java.lang.String getHolidayPeriod() {
        return holidayPeriod;
    }

    /**
     * Setter for property holidayPeriod.
     *
     * @param holidayPeriod New value of property holidayPeriod.
     */
    public void setHolidayPeriod(java.lang.String holidayPeriod) {
        this.holidayPeriod = holidayPeriod;
    }

    /**
     * Getter for property noOfInst.
     *
     * @return Value of property noOfInst.
     */
    public java.lang.String getNoOfInst() {
        return noOfInst;
    }

    /**
     * Setter for property noOfInst.
     *
     * @param noOfInst New value of property noOfInst.
     */
    public void setNoOfInst(java.lang.String noOfInst) {
        this.noOfInst = noOfInst;
    }

    /**
     * Getter for property intRate.
     *
     * @return Value of property intRate.
     */
    public java.lang.String getIntRate() {
        return intRate;
    }

    /**
     * Setter for property intRate.
     *
     * @param intRate New value of property intRate.
     */
    public void setIntRate(java.lang.String intRate) {
        this.intRate = intRate;
    }

    /**
     * Getter for property guaranteeNo.
     *
     * @return Value of property guaranteeNo.
     */
    public java.lang.String getGuaranteeNo() {
        return guaranteeNo;
    }

    /**
     * Setter for property guaranteeNo.
     *
     * @param guaranteeNo New value of property guaranteeNo.
     */
    public void setGuaranteeNo(java.lang.String guaranteeNo) {
        this.guaranteeNo = guaranteeNo;
    }

    /**
     * Getter for property guaranteeDt.
     *
     * @return Value of property guaranteeDt.
     */
    public java.util.Date getGuaranteeDt() {
        return guaranteeDt;
    }

    /**
     * Setter for property guaranteeDt.
     *
     * @param guaranteeDt New value of property guaranteeDt.
     */
    public void setGuaranteeDt(java.util.Date guaranteeDt) {
        this.guaranteeDt = guaranteeDt;
    }

    /**
     * Getter for property guaranteeSanctionNo.
     *
     * @return Value of property guaranteeSanctionNo.
     */
    public java.lang.String getGuaranteeSanctionNo() {
        return guaranteeSanctionNo;
    }

    /**
     * Setter for property guaranteeSanctionNo.
     *
     * @param guaranteeSanctionNo New value of property guaranteeSanctionNo.
     */
    public void setGuaranteeSanctionNo(java.lang.String guaranteeSanctionNo) {
        this.guaranteeSanctionNo = guaranteeSanctionNo;
    }

    /**
     * Getter for property guranteAmt.
     *
     * @return Value of property guranteAmt.
     */
    public java.lang.String getGuranteAmt() {
        return guranteAmt;
    }

    /**
     * Setter for property guranteAmt.
     *
     * @param guranteAmt New value of property guranteAmt.
     */
    public void setGuranteAmt(java.lang.String guranteAmt) {
        this.guranteAmt = guranteAmt;
    }

    /**
     * Getter for property guranteFeepayBy.
     *
     * @return Value of property guranteFeepayBy.
     */
    public java.lang.String getGuranteFeepayBy() {
        return guranteFeepayBy;
    }

    /**
     * Setter for property guranteFeepayBy.
     *
     * @param guranteFeepayBy New value of property guranteFeepayBy.
     */
    public void setGuranteFeepayBy(java.lang.String guranteFeepayBy) {
        this.guranteFeepayBy = guranteFeepayBy;
    }

    /**
     * Getter for property guranteFeePer.
     *
     * @return Value of property guranteFeePer.
     */
    public java.lang.String getGuranteFeePer() {
        return guranteFeePer;
    }

    /**
     * Setter for property guranteFeePer.
     *
     * @param guranteFeePer New value of property guranteFeePer.
     */
    public void setGuranteFeePer(java.lang.String guranteFeePer) {
        this.guranteFeePer = guranteFeePer;
    }

    /**
     * Getter for property guranteFee.
     *
     * @return Value of property guranteFee.
     */
    public java.lang.String getGuranteFee() {
        return guranteFee;
    }

    /**
     * Setter for property guranteFee.
     *
     * @param guranteFee New value of property guranteFee.
     */
    public void setGuranteFee(java.lang.String guranteFee) {
        this.guranteFee = guranteFee;
    }
}