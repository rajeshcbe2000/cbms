/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpSalaryStructureTO.java
 * 
 * Created on Wed Mar 02 16:46:43 GMT+05:30 2011
 */
package com.see.truetransact.transferobject.common;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SALARY_DETAILS.
 */
public class EmpSalaryStructureTO extends TransferObject implements Serializable {

    private String allowanceId = "";
    private Date fromDate = null;
    private Date toDate = null;
    private Date createdDt = null;
    private Date statusDt = null;
    private String statusBy = "";
    private String usingBasic = "";
    private String slNo = "";
    private String fromAmount = "";
    private String toAmount = "";
    private String percentOrFixed = "";
    private String amtOrPerAllowance = "";
    private String earnOrDed = "";
    private String maxAmount = "";
    private String status = "";
    private String allowanceType = "";
    private String authorizedBy = "";
    private String authorizeStatus = null;
    private Date authorizedDt = null;

    /**
     * Setter/Getter for ALLOWANCE_ID - table Field
     */
    public void setAllowanceId(String allowanceId) {
        this.allowanceId = allowanceId;
    }

    public String getAllowanceId() {
        return allowanceId;
    }

    /**
     * Setter/Getter for FROM_DATE - table Field
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    /**
     * Setter/Getter for TO_DATE - table Field
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
    }

    /**
     * Setter/Getter for USING_BASIC - table Field
     */
    public void setUsingBasic(String usingBasic) {
        this.usingBasic = usingBasic;
    }

    public String getUsingBasic() {
        return usingBasic;
    }

    /**
     * Setter/Getter for FROM_AMOUNT - table Field
     */
    public void setFromAmount(String fromAmount) {
        this.fromAmount = fromAmount;
    }

    public String getFromAmount() {
        return fromAmount;
    }

    /**
     * Setter/Getter for TO_AMOUNT - table Field
     */
    public void setToAmount(String toAmount) {
        this.toAmount = toAmount;
    }

    public String getToAmount() {
        return toAmount;
    }

    /**
     * Setter/Getter for PERCENT_OR_FIXED - table Field
     */
    public void setPercentOrFixed(String percentOrFixed) {
        this.percentOrFixed = percentOrFixed;
    }

    public String getPercentOrFixed() {
        return percentOrFixed;
    }

    /**
     * Setter/Getter for AMT_OR_PER_ALLOWANCE - table Field
     */
    public void setAmtOrPerAllowance(String amtOrPerAllowance) {
        this.amtOrPerAllowance = amtOrPerAllowance;
    }

    public String getAmtOrPerAllowance() {
        return amtOrPerAllowance;
    }

    /**
     * Setter/Getter for MAX_AMOUNT - table Field
     */
    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
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
     * Setter/Getter for ALLOWANCE_TYPE - table Field
     */
    public void setAllowanceType(String allowanceType) {
        this.allowanceType = allowanceType;
    }

    public String getAllowanceType() {
        return allowanceType;
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
        strB.append(getTOString("allowanceId", allowanceId));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("usingBasic", usingBasic));
        strB.append(getTOString("fromAmount", fromAmount));
        strB.append(getTOString("toAmount", toAmount));
        strB.append(getTOString("percentOrFixed", percentOrFixed));
        strB.append(getTOString("amtOrPerAllowance", amtOrPerAllowance));
        strB.append(getTOString("maxAmount", maxAmount));
        strB.append(getTOString("status", status));
        strB.append(getTOString("allowanceType", allowanceType));
        strB.append(getTOString("earnOrDed", earnOrDed));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("allowanceId", allowanceId));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("usingBasic", usingBasic));
        strB.append(getTOXml("fromAmount", fromAmount));
        strB.append(getTOXml("toAmount", toAmount));
        strB.append(getTOXml("percentOrFixed", percentOrFixed));
        strB.append(getTOXml("amtOrPerAllowance", amtOrPerAllowance));
        strB.append(getTOXml("maxAmount", maxAmount));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("allowanceType", allowanceType));
        strB.append(getTOXml("earnOrDed", earnOrDed));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property earnOrDed.
     *
     * @return Value of property earnOrDed.
     */
    public java.lang.String getEarnOrDed() {
        return earnOrDed;
    }

    /**
     * Setter for property earnOrDed.
     *
     * @param earnOrDed New value of property earnOrDed.
     */
    public void setEarnOrDed(java.lang.String earnOrDed) {
        this.earnOrDed = earnOrDed;
    }

    /**
     * Getter for property slNo.
     *
     * @return Value of property slNo.
     */
    public java.lang.String getSlNo() {
        return slNo;
    }

    /**
     * Setter for property slNo.
     *
     * @param slNo New value of property slNo.
     */
    public void setSlNo(java.lang.String slNo) {
        this.slNo = slNo;
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
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property createdDt.
     *
     * @param createdDt New value of property createdDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
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
}