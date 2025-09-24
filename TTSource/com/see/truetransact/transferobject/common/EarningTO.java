/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeductionTO.java
 * 
 * Created on Wed Jun 09 18:50:50 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.common;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is Deduction.
 */
public class EarningTO extends TransferObject implements Serializable {

    private Double edSlNo = null;
    private String empId = "";
    private String empName = "";
    private String empDesignation = "";
    private String empBranch = "";
    private Double presentBasicPay = null;
    private Date lastIncDate = null;
    private Date nextIncDate = null;
    private Date fromDate = null;
    private Date toDate = null;
    private String parameter = "";
    private String subParameter = "";
    private Double amount = null;
    private String txtNoOfDaysLOP = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDate = null;
    private String branchCode = "";
    private Double tempSlNo = null;
    private String cboCreditTypeValue = "";
    private String cboCreditDesigValue = "";
    private String earningID = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//    public String getKeyData() {
//        setKeyColumns("lienNo");
//        return slNo;
//    }
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("edSlNo", edSlNo));
        strB.append(getTOString("empId", empId));
        strB.append(getTOString("empName", empName));
        strB.append(getTOString("empDesignation", empDesignation));
        strB.append(getTOString("empBranch", empBranch));
        strB.append(getTOString("presentBasicPay", presentBasicPay));
        strB.append(getTOString("lastIncDate", lastIncDate));
        strB.append(getTOString("nextIncDate", nextIncDate));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("parameter", parameter));
        strB.append(getTOString("subParameter", subParameter));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("tempSlNo", tempSlNo));
        strB.append(getTOString("cboCreditTypeValue", cboCreditTypeValue));
        strB.append(getTOString("cboCreditDesigValue", cboCreditDesigValue));
        strB.append(getTOString("earningID", earningID));
        strB.append(getTOString("txtNoOfDaysLOP", txtNoOfDaysLOP));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("edSlNo", edSlNo));
        strB.append(getTOXml("empId", empId));
        strB.append(getTOXml("empName", empName));
        strB.append(getTOXml("empDesignation", empDesignation));
        strB.append(getTOXml("empBranch", empBranch));
        strB.append(getTOXml("presentBasicPay", presentBasicPay));
        strB.append(getTOXml("lastIncDate", lastIncDate));
        strB.append(getTOXml("nextIncDate", nextIncDate));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("parameter", parameter));
        strB.append(getTOXml("subParameter", subParameter));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("tempSlNo", tempSlNo));
        strB.append(getTOXml("cboCreditTypeValue", cboCreditTypeValue));
        strB.append(getTOXml("cboCreditDesigValue", cboCreditDesigValue));
        strB.append(getTOXml("earningID", earningID));
        strB.append(getTOXml("txtNoOfDaysLOP", txtNoOfDaysLOP));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property edSlNo.
     *
     * @return Value of property edSlNo.
     */
    public java.lang.Double getEdSlNo() {
        return edSlNo;
    }

    /**
     * Setter for property edSlNo.
     *
     * @param edSlNo New value of property edSlNo.
     */
    public void setEdSlNo(java.lang.Double edSlNo) {
        this.edSlNo = edSlNo;
    }

    /**
     * Getter for property empId.
     *
     * @return Value of property empId.
     */
    public java.lang.String getEmpId() {
        return empId;
    }

    /**
     * Setter for property empId.
     *
     * @param empId New value of property empId.
     */
    public void setEmpId(java.lang.String empId) {
        this.empId = empId;
    }

    /**
     * Getter for property empName.
     *
     * @return Value of property empName.
     */
    public java.lang.String getEmpName() {
        return empName;
    }

    /**
     * Setter for property empName.
     *
     * @param empName New value of property empName.
     */
    public void setEmpName(java.lang.String empName) {
        this.empName = empName;
    }

    /**
     * Getter for property empDesignation.
     *
     * @return Value of property empDesignation.
     */
    public java.lang.String getEmpDesignation() {
        return empDesignation;
    }

    /**
     * Setter for property empDesignation.
     *
     * @param empDesignation New value of property empDesignation.
     */
    public void setEmpDesignation(java.lang.String empDesignation) {
        this.empDesignation = empDesignation;
    }

    /**
     * Getter for property empBranch.
     *
     * @return Value of property empBranch.
     */
    public java.lang.String getEmpBranch() {
        return empBranch;
    }

    /**
     * Setter for property empBranch.
     *
     * @param empBranch New value of property empBranch.
     */
    public void setEmpBranch(java.lang.String empBranch) {
        this.empBranch = empBranch;
    }

    /**
     * Getter for property presentBasicPay.
     *
     * @return Value of property presentBasicPay.
     */
    public java.lang.Double getPresentBasicPay() {
        return presentBasicPay;
    }

    /**
     * Setter for property presentBasicPay.
     *
     * @param presentBasicPay New value of property presentBasicPay.
     */
    public void setPresentBasicPay(java.lang.Double presentBasicPay) {
        this.presentBasicPay = presentBasicPay;
    }

    /**
     * Getter for property lastIncDate.
     *
     * @return Value of property lastIncDate.
     */
    public java.util.Date getLastIncDate() {
        return lastIncDate;
    }

    /**
     * Setter for property lastIncDate.
     *
     * @param lastIncDate New value of property lastIncDate.
     */
    public void setLastIncDate(java.util.Date lastIncDate) {
        this.lastIncDate = lastIncDate;
    }

    /**
     * Getter for property nextIncDate.
     *
     * @return Value of property nextIncDate.
     */
    public java.util.Date getNextIncDate() {
        return nextIncDate;
    }

    /**
     * Setter for property nextIncDate.
     *
     * @param nextIncDate New value of property nextIncDate.
     */
    public void setNextIncDate(java.util.Date nextIncDate) {
        this.nextIncDate = nextIncDate;
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
     * Getter for property parameter.
     *
     * @return Value of property parameter.
     */
    public java.lang.String getParameter() {
        return parameter;
    }

    /**
     * Setter for property parameter.
     *
     * @param parameter New value of property parameter.
     */
    public void setParameter(java.lang.String parameter) {
        this.parameter = parameter;
    }

    /**
     * Getter for property subParameter.
     *
     * @return Value of property subParameter.
     */
    public java.lang.String getSubParameter() {
        return subParameter;
    }

    /**
     * Setter for property subParameter.
     *
     * @param subParameter New value of property subParameter.
     */
    public void setSubParameter(java.lang.String subParameter) {
        this.subParameter = subParameter;
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     */
    public java.lang.Double getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     */
    public void setAmount(java.lang.Double amount) {
        this.amount = amount;
    }

//    /**
//     * Getter for property statusDate.
//     * @return Value of property statusDate.
//     */
//    public java.util.Date getStatusDate() {
//        return statusDate;
//    }
//    
//    /**
//     * Setter for property statusDate.
//     * @param statusDate New value of property statusDate.
//     */
//    public void setStatusDate(java.util.Date statusDate) {
//        this.statusDate = statusDate;
//    }
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
     * Getter for property authorizeDate.
     *
     * @return Value of property authorizeDate.
     */
    public java.util.Date getAuthorizeDate() {
        return authorizeDate;
    }

    /**
     * Setter for property authorizeDate.
     *
     * @param authorizeDate New value of property authorizeDate.
     */
    public void setAuthorizeDate(java.util.Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    /**
     * Getter for property branchCode.
     *
     * @return Value of property branchCode.
     */
    public java.lang.String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter for property branchCode.
     *
     * @param branchCode New value of property branchCode.
     */
    public void setBranchCode(java.lang.String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * Getter for property tempSlNo.
     *
     * @return Value of property tempSlNo.
     */
    public java.lang.Double getTempSlNo() {
        return tempSlNo;
    }

    /**
     * Setter for property tempSlNo.
     *
     * @param tempSlNo New value of property tempSlNo.
     */
    public void setTempSlNo(java.lang.Double tempSlNo) {
        this.tempSlNo = tempSlNo;
    }

    /**
     * Getter for property cboCreditTypeValue.
     *
     * @return Value of property cboCreditTypeValue.
     */
    public java.lang.String getCboCreditTypeValue() {
        return cboCreditTypeValue;
    }

    /**
     * Setter for property cboCreditTypeValue.
     *
     * @param cboCreditTypeValue New value of property cboCreditTypeValue.
     */
    public void setCboCreditTypeValue(java.lang.String cboCreditTypeValue) {
        this.cboCreditTypeValue = cboCreditTypeValue;
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
     * Getter for property earningID.
     *
     * @return Value of property earningID.
     */
    public java.lang.String getEarningID() {
        return earningID;
    }

    /**
     * Setter for property earningID.
     *
     * @param earningID New value of property earningID.
     */
    public void setEarningID(java.lang.String earningID) {
        this.earningID = earningID;
    }

    /**
     * Getter for property cboCreditDesigValue.
     *
     * @return Value of property cboCreditDesigValue.
     */
    public java.lang.String getCboCreditDesigValue() {
        return cboCreditDesigValue;
    }

    /**
     * Setter for property cboCreditDesigValue.
     *
     * @param cboCreditDesigValue New value of property cboCreditDesigValue.
     */
    public void setCboCreditDesigValue(java.lang.String cboCreditDesigValue) {
        this.cboCreditDesigValue = cboCreditDesigValue;
    }

    /**
     * Getter for property txtNoOfDaysLOP.
     *
     * @return Value of property txtNoOfDaysLOP.
     */
    public java.lang.String getTxtNoOfDaysLOP() {
        return txtNoOfDaysLOP;
    }

    /**
     * Setter for property txtNoOfDaysLOP.
     *
     * @param txtNoOfDaysLOP New value of property txtNoOfDaysLOP.
     */
    public void setTxtNoOfDaysLOP(java.lang.String txtNoOfDaysLOP) {
        this.txtNoOfDaysLOP = txtNoOfDaysLOP;
    }
}