/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureTO.java
 * 
 * Created on Wed Jun 09 18:50:50 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.common;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SalaryStructure.
 */
public class SalaryStructureTO extends TransferObject implements Serializable {

    private String slNo = "";
    private String grade = "";
    private Date fromDate = null;
    private Date toDate = null;
    private String startingScaleAmt = "";
    private String incrementAmt = "";
    private String noOfIncrement = "";
    private String totalAmount = "";
    private String singleRowTotAmt = "";
    private String stagnationInc = "";
    private String totNoOfStagnation = "";
    private String stagnationIncAmt = "";
    private String noofStagnation = "";
    private String stagnationOnceIn = "";
    private String stagnationValues = "";
    private String salaryType = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDate = null;
    private String branchCode = "";
    private Double tempSlNo = null;

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
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("grade", grade));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("startingScaleAmt", startingScaleAmt));
        strB.append(getTOString("incrementAmt", incrementAmt));
        strB.append(getTOString("noOfIncrement", noOfIncrement));
        strB.append(getTOString("totalAmount", totalAmount));
        strB.append(getTOString("stagnationInc", stagnationInc));
        strB.append(getTOString("totNoOfStagnation", totNoOfStagnation));
        strB.append(getTOString("stagnationIncAmt", stagnationIncAmt));
        strB.append(getTOString("noofStagnation", noofStagnation));
        strB.append(getTOString("stagnationOnceIn", stagnationOnceIn));
        strB.append(getTOString("stagnationValues", stagnationValues));
        strB.append(getTOString("salaryType", salaryType));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("tempSlNo", tempSlNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("grade", grade));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("startingScaleAmt", startingScaleAmt));
        strB.append(getTOXml("incrementAmt", incrementAmt));
        strB.append(getTOXml("noOfIncrement", noOfIncrement));
        strB.append(getTOXml("totalAmount", totalAmount));
        strB.append(getTOXml("stagnationInc", stagnationInc));
        strB.append(getTOXml("totNoOfStagnation", totNoOfStagnation));
        strB.append(getTOXml("stagnationIncAmt", stagnationIncAmt));
        strB.append(getTOXml("noofStagnation", noofStagnation));
        strB.append(getTOXml("stagnationOnceIn", stagnationOnceIn));
        strB.append(getTOXml("stagnationValues", stagnationValues));
        strB.append(getTOXml("salaryType", salaryType));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("tempSlNo", tempSlNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property grade.
     *
     * @return Value of property grade.
     */
    public java.lang.String getGrade() {
        return grade;
    }

    /**
     * Setter for property grade.
     *
     * @param grade New value of property grade.
     */
    public void setGrade(java.lang.String grade) {
        this.grade = grade;
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
     * Getter for property startingScaleAmt.
     *
     * @return Value of property startingScaleAmt.
     */
    public java.lang.String getStartingScaleAmt() {
        return startingScaleAmt;
    }

    /**
     * Setter for property startingScaleAmt.
     *
     * @param startingScaleAmt New value of property startingScaleAmt.
     */
    public void setStartingScaleAmt(java.lang.String startingScaleAmt) {
        this.startingScaleAmt = startingScaleAmt;
    }

    /**
     * Getter for property incrementAmt.
     *
     * @return Value of property incrementAmt.
     */
    public java.lang.String getIncrementAmt() {
        return incrementAmt;
    }

    /**
     * Setter for property incrementAmt.
     *
     * @param incrementAmt New value of property incrementAmt.
     */
    public void setIncrementAmt(java.lang.String incrementAmt) {
        this.incrementAmt = incrementAmt;
    }

    /**
     * Getter for property noOfIncrement.
     *
     * @return Value of property noOfIncrement.
     */
    public java.lang.String getNoOfIncrement() {
        return noOfIncrement;
    }

    /**
     * Setter for property noOfIncrement.
     *
     * @param noOfIncrement New value of property noOfIncrement.
     */
    public void setNoOfIncrement(java.lang.String noOfIncrement) {
        this.noOfIncrement = noOfIncrement;
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
     * Getter for property statusDate.
     *
     * @return Value of property statusDate.
     */
    public java.util.Date getStatusDate() {
        return statusDate;
    }

    /**
     * Setter for property statusDate.
     *
     * @param statusDate New value of property statusDate.
     */
    public void setStatusDate(java.util.Date statusDate) {
        this.statusDate = statusDate;
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
     * Getter for property totalAmount.
     *
     * @return Value of property totalAmount.
     */
    public java.lang.String getTotalAmount() {
        return totalAmount;
    }

    /**
     * Setter for property totalAmount.
     *
     * @param totalAmount New value of property totalAmount.
     */
    public void setTotalAmount(java.lang.String totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Getter for property singleRowTotAmt.
     *
     * @return Value of property singleRowTotAmt.
     */
    public java.lang.String getSingleRowTotAmt() {
        return singleRowTotAmt;
    }

    /**
     * Setter for property singleRowTotAmt.
     *
     * @param singleRowTotAmt New value of property singleRowTotAmt.
     */
    public void setSingleRowTotAmt(java.lang.String singleRowTotAmt) {
        this.singleRowTotAmt = singleRowTotAmt;
    }

    /**
     * Getter for property stagnationInc.
     *
     * @return Value of property stagnationInc.
     */
    public java.lang.String getStagnationInc() {
        return stagnationInc;
    }

    /**
     * Setter for property stagnationInc.
     *
     * @param stagnationInc New value of property stagnationInc.
     */
    public void setStagnationInc(java.lang.String stagnationInc) {
        this.stagnationInc = stagnationInc;
    }

    /**
     * Getter for property stagnationValues.
     *
     * @return Value of property stagnationValues.
     */
    public java.lang.String getStagnationValues() {
        return stagnationValues;
    }

    /**
     * Setter for property stagnationValues.
     *
     * @param stagnationValues New value of property stagnationValues.
     */
    public void setStagnationValues(java.lang.String stagnationValues) {
        this.stagnationValues = stagnationValues;
    }

    /**
     * Getter for property salaryType.
     *
     * @return Value of property salaryType.
     */
    public java.lang.String getSalaryType() {
        return salaryType;
    }

    /**
     * Setter for property salaryType.
     *
     * @param salaryType New value of property salaryType.
     */
    public void setSalaryType(java.lang.String salaryType) {
        this.salaryType = salaryType;
    }

    /**
     * Getter for property stagnationOnceIn.
     *
     * @return Value of property stagnationOnceIn.
     */
    public java.lang.String getStagnationOnceIn() {
        return stagnationOnceIn;
    }

    /**
     * Setter for property stagnationOnceIn.
     *
     * @param stagnationOnceIn New value of property stagnationOnceIn.
     */
    public void setStagnationOnceIn(java.lang.String stagnationOnceIn) {
        this.stagnationOnceIn = stagnationOnceIn;
    }

    /**
     * Getter for property noofStagnation.
     *
     * @return Value of property noofStagnation.
     */
    public java.lang.String getNoofStagnation() {
        return noofStagnation;
    }

    /**
     * Setter for property noofStagnation.
     *
     * @param noofStagnation New value of property noofStagnation.
     */
    public void setNoofStagnation(java.lang.String noofStagnation) {
        this.noofStagnation = noofStagnation;
    }

    /**
     * Getter for property stagnationIncAmt.
     *
     * @return Value of property stagnationIncAmt.
     */
    public java.lang.String getStagnationIncAmt() {
        return stagnationIncAmt;
    }

    /**
     * Setter for property stagnationIncAmt.
     *
     * @param stagnationIncAmt New value of property stagnationIncAmt.
     */
    public void setStagnationIncAmt(java.lang.String stagnationIncAmt) {
        this.stagnationIncAmt = stagnationIncAmt;
    }

    /**
     * Getter for property totNoOfStagnation.
     *
     * @return Value of property totNoOfStagnation.
     */
    public java.lang.String getTotNoOfStagnation() {
        return totNoOfStagnation;
    }

    /**
     * Setter for property totNoOfStagnation.
     *
     * @param totNoOfStagnation New value of property totNoOfStagnation.
     */
    public void setTotNoOfStagnation(java.lang.String totNoOfStagnation) {
        this.totNoOfStagnation = totNoOfStagnation;
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
}