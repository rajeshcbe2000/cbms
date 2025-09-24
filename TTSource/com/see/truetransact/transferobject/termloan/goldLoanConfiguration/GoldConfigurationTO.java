/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GoldConfigurationMRBTO.java
 * 
 * Created on Tue Jul 12 11:44:31 IST 2005
 */
package com.see.truetransact.transferobject.termloan.goldLoanConfiguration;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is Gold_Configuration.
 */
public class GoldConfigurationTO extends TransferObject implements Serializable {

    private Long referenceNo = null;
    private Long setNo = null;
    private String purityOfGold = "";
    private Double perGramRate = null;
    private Date fromDate = null;
    private Date toDate = null;
    private String branchId = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String defaultItem = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//            strB.append (getTOStringKey(getKeyData()));
        strB.append(getTOString("referenceNo", referenceNo));
        strB.append(getTOString("setNo", setNo));
        strB.append(getTOString("purityOfGold", purityOfGold));
        strB.append(getTOString("perGramRate", perGramRate));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("defaultItem", defaultItem));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//            strB.append (getTOXmlKey(getKeyData()));
        strB.append(getTOXml("referenceNo", referenceNo));
        strB.append(getTOXml("setNo", setNo));
        strB.append(getTOXml("purityOfGold", purityOfGold));
        strB.append(getTOXml("perGramRate", perGramRate));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("defaultItem", defaultItem));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property referenceNo.
     *
     * @return Value of property referenceNo.
     */
    public java.lang.Long getReferenceNo() {
        return referenceNo;
    }

    /**
     * Setter for property referenceNo.
     *
     * @param referenceNo New value of property referenceNo.
     */
    public void setReferenceNo(java.lang.Long referenceNo) {
        this.referenceNo = referenceNo;
    }

    /**
     * Getter for property setNo.
     *
     * @return Value of property setNo.
     */
    public java.lang.Long getSetNo() {
        return setNo;
    }

    /**
     * Setter for property setNo.
     *
     * @param setNo New value of property setNo.
     */
    public void setSetNo(java.lang.Long setNo) {
        this.setNo = setNo;
    }

    /**
     * Getter for property purityOfGold.
     *
     * @return Value of property purityOfGold.
     */
    public java.lang.String getPurityOfGold() {
        return purityOfGold;
    }

    /**
     * Setter for property purityOfGold.
     *
     * @param purityOfGold New value of property purityOfGold.
     */
    public void setPurityOfGold(java.lang.String purityOfGold) {
        this.purityOfGold = purityOfGold;
    }

    /**
     * Getter for property perGramRate.
     *
     * @return Value of property perGramRate.
     */
    public java.lang.Double getPerGramRate() {
        return perGramRate;
    }

    /**
     * Setter for property perGramRate.
     *
     * @param perGramRate New value of property perGramRate.
     */
    public void setPerGramRate(java.lang.Double perGramRate) {
        this.perGramRate = perGramRate;
    }

    /**
     * Getter for property fromDate.
     *
     * @return Value of property fromDate.
     */
    public java.util.Date getFromDate() {
        return fromDate;
    }

    /**
     * Setter for property fromDate.
     *
     * @param fromDate New value of property fromDate.
     */
    public void setFromDate(java.util.Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Getter for property toDate.
     *
     * @return Value of property toDate.
     */
    public java.util.Date getToDate() {
        return toDate;
    }

    /**
     * Setter for property toDate.
     *
     * @param toDate New value of property toDate.
     */
    public void setToDate(java.util.Date toDate) {
        this.toDate = toDate;
    }

    /**
     * Getter for property branchId.
     *
     * @return Value of property branchId.
     */
    public java.lang.String getBranchId() {
        return branchId;
    }

    /**
     * Setter for property branchId.
     *
     * @param branchId New value of property branchId.
     */
    public void setBranchId(java.lang.String branchId) {
        this.branchId = branchId;
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
     * Getter for property authorizedStatus.
     *
     * @return Value of property authorizedStatus.
     */
    public java.lang.String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter for property authorizedStatus.
     *
     * @param authorizedStatus New value of property authorizedStatus.
     */
    public void setAuthorizedStatus(java.lang.String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    /**
     * Getter for property authorizedDt.
     *
     * @return Value of property authorizedDt.
     */
    public java.util.Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter for property authorizedDt.
     *
     * @param authorizedDt New value of property authorizedDt.
     */
    public void setAuthorizedDt(java.util.Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    /**
     * Getter for property authorizedBy.
     *
     * @return Value of property authorizedBy.
     */
    public java.lang.String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter for property authorizedBy.
     *
     * @param authorizedBy New value of property authorizedBy.
     */
    public void setAuthorizedBy(java.lang.String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getDefaultItem() {
        return defaultItem;
    }

    public void setDefaultItem(String defaultItem) {
        this.defaultItem = defaultItem;
    }
    
}