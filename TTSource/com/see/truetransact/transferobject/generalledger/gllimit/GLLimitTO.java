/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLLimitTO.java
 * 
 * Created on Mon Aug 22 17:10:25 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.generalledger.gllimit;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is GL_LIMIT.
 */
public class GLLimitTO extends TransferObject implements Serializable {

    private String branchGroup = "";
    private String acHdId = "";
    private Double limitAmt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private Double annualLimitAmt = null;
    private Double overDrawPer = null;
    private String interBranchAllowed = "";
    private Date frmPeriod = null;
    private Date toPeriod = null;
    private Integer slNo = 0;

    /**
     * Setter/Getter for BRANCH_GROUP - table Field
     */
    public void setBranchGroup(String branchGroup) {
        this.branchGroup = branchGroup;
    }

    public String getBranchGroup() {
        return branchGroup;
    }

    /**
     * Setter/Getter for AC_HD_ID - table Field
     */
    public void setAcHdId(String acHdId) {
        this.acHdId = acHdId;
    }

    public String getAcHdId() {
        return acHdId;
    }

    /**
     * Setter/Getter for LIMIT_AMT - table Field
     */
    public void setLimitAmt(Double limitAmt) {
        this.limitAmt = limitAmt;
    }

    public Double getLimitAmt() {
        return limitAmt;
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
     * Setter/Getter for ANNUAL_LIMIT_AMT - table Field
     */
    public void setAnnualLimitAmt(Double annualLimitAmt) {
        this.annualLimitAmt = annualLimitAmt;
    }

    public Double getAnnualLimitAmt() {
        return annualLimitAmt;
    }

    /**
     * Setter/Getter for OVER_DRAW_PER - table Field
     */
    public void setOverDrawPer(Double overDrawPer) {
        this.overDrawPer = overDrawPer;
    }

    public Double getOverDrawPer() {
        return overDrawPer;
    }

    /**
     * Setter/Getter for INTER_BRANCH_ALLOWED - table Field
     */
    public void setInterBranchAllowed(String interBranchAllowed) {
        this.interBranchAllowed = interBranchAllowed;
    }

    public String getInterBranchAllowed() {
        return interBranchAllowed;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(acHdId);
        return acHdId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("branchGroup", branchGroup));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("limitAmt", limitAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("annualLimitAmt", annualLimitAmt));
        strB.append(getTOString("overDrawPer", overDrawPer));
        strB.append(getTOString("interBranchAllowed", interBranchAllowed));
        strB.append(getTOString("frmPeriod", frmPeriod));
        strB.append(getTOString("toPeriod", toPeriod));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("branchGroup", branchGroup));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("limitAmt", limitAmt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("annualLimitAmt", annualLimitAmt));
        strB.append(getTOXml("overDrawPer", overDrawPer));
        strB.append(getTOXml("interBranchAllowed", interBranchAllowed));
        strB.append(getTOXml("frmPeriod", frmPeriod));
        strB.append(getTOXml("toPeriod", toPeriod));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property frmPeriod.
     *
     * @return Value of property frmPeriod.
     */
    public java.util.Date getFrmPeriod() {
        return frmPeriod;
    }

    /**
     * Setter for property frmPeriod.
     *
     * @param frmPeriod New value of property frmPeriod.
     */
    public void setFrmPeriod(java.util.Date frmPeriod) {
        this.frmPeriod = frmPeriod;
    }

    /**
     * Getter for property toPeriod.
     *
     * @return Value of property toPeriod.
     */
    public java.util.Date getToPeriod() {
        return toPeriod;
    }

    /**
     * Setter for property toPeriod.
     *
     * @param toPeriod New value of property toPeriod.
     */
    public void setToPeriod(java.util.Date toPeriod) {
        this.toPeriod = toPeriod;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }

  
}