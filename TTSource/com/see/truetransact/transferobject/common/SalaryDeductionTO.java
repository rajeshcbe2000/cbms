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
public class SalaryDeductionTO extends TransferObject implements Serializable {

    private Double sdSlNo = null;
    private String empId = "";
    private String empName = "";
    private String empCustId = "";
    private String designation = "";
    private String empBranch = "";
    private Double basic = null;
    private String typeOfDeduction = "";
    private Double amount = null;
    private Double salaryMonth = null;
    private Double salaryYear = null;
    private String salaryStatus = "";
    private Date salaryDate = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDate = null;
    private String branchCode = "";
    private Double tempSlNo = null;
    private Date lastEffectiveDt = null;

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
        strB.append(getTOString("sdSlNo", sdSlNo));
        strB.append(getTOString("empId", empId));
        strB.append(getTOString("empCustId", empCustId));
        strB.append(getTOString("empName", empName));
        strB.append(getTOString("designation", designation));
        strB.append(getTOString("empBranch", empBranch));
        strB.append(getTOString("basic", basic));
        strB.append(getTOString("typeOfDeduction", typeOfDeduction));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("salaryMonth", salaryMonth));
        strB.append(getTOString("salaryYear", salaryYear));
        strB.append(getTOString("salaryStatus", salaryStatus));
        strB.append(getTOString("salaryDate", salaryDate));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("tempSlNo", tempSlNo));
        strB.append(getTOString("lastEffectiveDt", lastEffectiveDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("sdSlNo", sdSlNo));
        strB.append(getTOXml("empId", empId));
        strB.append(getTOXml("empCustId", empCustId));
        strB.append(getTOXml("empName", empName));
        strB.append(getTOXml("designation", designation));
        strB.append(getTOXml("empBranch", empBranch));
        strB.append(getTOXml("basic", basic));
        strB.append(getTOXml("typeOfDeduction", typeOfDeduction));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("salaryMonth", salaryMonth));
        strB.append(getTOXml("salaryYear", salaryYear));
        strB.append(getTOXml("salaryStatus", salaryStatus));
        strB.append(getTOXml("salaryDate", salaryDate));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("tempSlNo", tempSlNo));
        strB.append(getTOXml("lastEffectiveDt", lastEffectiveDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property sdSlNo.
     *
     * @return Value of property sdSlNo.
     */
    public java.lang.Double getSdSlNo() {
        return sdSlNo;
    }

    /**
     * Setter for property sdSlNo.
     *
     * @param sdSlNo New value of property sdSlNo.
     */
    public void setSdSlNo(java.lang.Double sdSlNo) {
        this.sdSlNo = sdSlNo;
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
     * Getter for property designation.
     *
     * @return Value of property designation.
     */
    public java.lang.String getDesignation() {
        return designation;
    }

    /**
     * Setter for property designation.
     *
     * @param designation New value of property designation.
     */
    public void setDesignation(java.lang.String designation) {
        this.designation = designation;
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
     * Getter for property basic.
     *
     * @return Value of property basic.
     */
    public java.lang.Double getBasic() {
        return basic;
    }

    /**
     * Setter for property basic.
     *
     * @param basic New value of property basic.
     */
    public void setBasic(java.lang.Double basic) {
        this.basic = basic;
    }

    /**
     * Getter for property salaryStatus.
     *
     * @return Value of property salaryStatus.
     */
    public java.lang.String getSalaryStatus() {
        return salaryStatus;
    }

    /**
     * Setter for property salaryStatus.
     *
     * @param salaryStatus New value of property salaryStatus.
     */
    public void setSalaryStatus(java.lang.String salaryStatus) {
        this.salaryStatus = salaryStatus;
    }

    /**
     * Getter for property salaryDate.
     *
     * @return Value of property salaryDate.
     */
    public java.util.Date getSalaryDate() {
        return salaryDate;
    }

    /**
     * Setter for property salaryDate.
     *
     * @param salaryDate New value of property salaryDate.
     */
    public void setSalaryDate(java.util.Date salaryDate) {
        this.salaryDate = salaryDate;
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
     * Getter for property typeOfDeduction.
     *
     * @return Value of property typeOfDeduction.
     */
    public java.lang.String getTypeOfDeduction() {
        return typeOfDeduction;
    }

    /**
     * Setter for property typeOfDeduction.
     *
     * @param typeOfDeduction New value of property typeOfDeduction.
     */
    public void setTypeOfDeduction(java.lang.String typeOfDeduction) {
        this.typeOfDeduction = typeOfDeduction;
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

    /**
     * Getter for property empCustId.
     *
     * @return Value of property empCustId.
     */
    public java.lang.String getEmpCustId() {
        return empCustId;
    }

    /**
     * Setter for property empCustId.
     *
     * @param empCustId New value of property empCustId.
     */
    public void setEmpCustId(java.lang.String empCustId) {
        this.empCustId = empCustId;
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
     * Getter for property salaryMonth.
     *
     * @return Value of property salaryMonth.
     */
    public java.lang.Double getSalaryMonth() {
        return salaryMonth;
    }

    /**
     * Setter for property salaryMonth.
     *
     * @param salaryMonth New value of property salaryMonth.
     */
    public void setSalaryMonth(java.lang.Double salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    /**
     * Getter for property salaryYear.
     *
     * @return Value of property salaryYear.
     */
    public java.lang.Double getSalaryYear() {
        return salaryYear;
    }

    /**
     * Setter for property salaryYear.
     *
     * @param salaryYear New value of property salaryYear.
     */
    public void setSalaryYear(java.lang.Double salaryYear) {
        this.salaryYear = salaryYear;
    }

    /**
     * Getter for property lastEffectiveDt.
     *
     * @return Value of property lastEffectiveDt.
     */
    public java.util.Date getLastEffectiveDt() {
        return lastEffectiveDt;
    }

    /**
     * Setter for property lastEffectiveDt.
     *
     * @param lastEffectiveDt New value of property lastEffectiveDt.
     */
    public void setLastEffectiveDt(java.util.Date lastEffectiveDt) {
        this.lastEffectiveDt = lastEffectiveDt;
    }
}