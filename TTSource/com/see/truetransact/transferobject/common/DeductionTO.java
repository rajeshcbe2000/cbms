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
public class DeductionTO extends TransferObject implements Serializable {

    private String dtSlNo = "";
    private String employeeId = "";
    private String employeeName = "";
    private String designation = "";
    private String employeeBranch = "";
    private String fixed = "";
    private String installments = "";
    private String deductionType = "";
    private Double fromMM = null;
    private Double fromYYYY = null;
    private Double toMM = null;
    private Double toYYYY = null;
    private Double amount = null;
    private String accountHead = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDate = null;
    private String branchCode = "";
    private Double tempSlNo = null;
    private String loanAccNo = "";
    private Date loanFromDate = null;
    private Date loanToDate = null;
    private String loanAmount = "";
    private String installmentAmt = "";
    private String noofInstallments = "";
    private String loanAvailedBranch = "";
    private String loanDesc = "";
    private String loanStatus = "";
    private Date loanStoppedDate = null;
    private String remarks = "";
    private String txtInstIntRate = "";
    private String txtIntNetAmount = "";

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
        strB.append(getTOString("dtSlNo", dtSlNo));
        strB.append(getTOString("employeeId", employeeId));
        strB.append(getTOString("employeeName", employeeName));
        strB.append(getTOString("designation", designation));
        strB.append(getTOString("employeeBranch", employeeBranch));
        strB.append(getTOString("fixed", fixed));
        strB.append(getTOString("installments", installments));
        strB.append(getTOString("deductionType", deductionType));
        strB.append(getTOString("fromMM", fromMM));
        strB.append(getTOString("fromYYYY", fromYYYY));
        strB.append(getTOString("toMM", toMM));
        strB.append(getTOString("toYYYY", toYYYY));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("accountHead", accountHead));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("tempSlNo", tempSlNo));
        strB.append(getTOString("loanAccNo", loanAccNo));
        strB.append(getTOString("loanFromDate", loanFromDate));
        strB.append(getTOString("loanToDate", loanToDate));
        strB.append(getTOString("loanAmount", loanAmount));
        strB.append(getTOString("installmentAmt", installmentAmt));
        strB.append(getTOString("noofInstallments", noofInstallments));
        strB.append(getTOString("loanAvailedBranch", loanAvailedBranch));
        strB.append(getTOString("loanDesc", loanDesc));
        strB.append(getTOString("loanStatus", loanStatus));
        strB.append(getTOString("txtInstIntRate", txtInstIntRate));
        strB.append(getTOString("txtIntNetAmount", txtIntNetAmount));
        strB.append(getTOString("loanStoppedDate", loanStoppedDate));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("dtSlNo", dtSlNo));
        strB.append(getTOXml("employeeId", employeeId));
        strB.append(getTOXml("employeeName", employeeName));
        strB.append(getTOXml("designation", designation));
        strB.append(getTOXml("employeeBranch", employeeBranch));
        strB.append(getTOXml("fixed", fixed));
        strB.append(getTOXml("installments", installments));
        strB.append(getTOXml("deductionType", deductionType));
        strB.append(getTOXml("fromMM", fromMM));
        strB.append(getTOXml("fromYYYY", fromYYYY));
        strB.append(getTOXml("toMM", toMM));
        strB.append(getTOXml("toYYYY", toYYYY));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("accountHead", accountHead));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("tempSlNo", tempSlNo));
        strB.append(getTOXml("loanAccNo", loanAccNo));
        strB.append(getTOXml("loanFromDate", loanFromDate));
        strB.append(getTOXml("loanToDate", loanToDate));
        strB.append(getTOXml("loanAmount", loanAmount));
        strB.append(getTOXml("installmentAmt", installmentAmt));
        strB.append(getTOXml("noofInstallments", noofInstallments));
        strB.append(getTOXml("loanAvailedBranch", loanAvailedBranch));
        strB.append(getTOXml("loanDesc", loanDesc));
        strB.append(getTOXml("loanStatus", loanStatus));
        strB.append(getTOXml("txtInstIntRate", txtInstIntRate));
        strB.append(getTOXml("txtIntNetAmount", txtIntNetAmount));
        strB.append(getTOXml("loanStoppedDate", loanStoppedDate));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property employeeName.
     *
     * @return Value of property employeeName.
     */
    public java.lang.String getEmployeeName() {
        return employeeName;
    }

    /**
     * Setter for property employeeName.
     *
     * @param employeeName New value of property employeeName.
     */
    public void setEmployeeName(java.lang.String employeeName) {
        this.employeeName = employeeName;
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
     * Getter for property employeeBranch.
     *
     * @return Value of property employeeBranch.
     */
    public java.lang.String getEmployeeBranch() {
        return employeeBranch;
    }

    /**
     * Setter for property employeeBranch.
     *
     * @param employeeBranch New value of property employeeBranch.
     */
    public void setEmployeeBranch(java.lang.String employeeBranch) {
        this.employeeBranch = employeeBranch;
    }

    /**
     * Getter for property fixed.
     *
     * @return Value of property fixed.
     */
    public java.lang.String getFixed() {
        return fixed;
    }

    /**
     * Setter for property fixed.
     *
     * @param fixed New value of property fixed.
     */
    public void setFixed(java.lang.String fixed) {
        this.fixed = fixed;
    }

    /**
     * Getter for property installments.
     *
     * @return Value of property installments.
     */
    public java.lang.String getInstallments() {
        return installments;
    }

    /**
     * Setter for property installments.
     *
     * @param installments New value of property installments.
     */
    public void setInstallments(java.lang.String installments) {
        this.installments = installments;
    }

    /**
     * Getter for property deductionType.
     *
     * @return Value of property deductionType.
     */
    public java.lang.String getDeductionType() {
        return deductionType;
    }

    /**
     * Setter for property deductionType.
     *
     * @param deductionType New value of property deductionType.
     */
    public void setDeductionType(java.lang.String deductionType) {
        this.deductionType = deductionType;
    }

    /**
     * Getter for property fromMM.
     *
     * @return Value of property fromMM.
     */
    public java.lang.Double getFromMM() {
        return fromMM;
    }

    /**
     * Setter for property fromMM.
     *
     * @param fromMM New value of property fromMM.
     */
    public void setFromMM(java.lang.Double fromMM) {
        this.fromMM = fromMM;
    }

    /**
     * Getter for property fromYYYY.
     *
     * @return Value of property fromYYYY.
     */
    public java.lang.Double getFromYYYY() {
        return fromYYYY;
    }

    /**
     * Setter for property fromYYYY.
     *
     * @param fromYYYY New value of property fromYYYY.
     */
    public void setFromYYYY(java.lang.Double fromYYYY) {
        this.fromYYYY = fromYYYY;
    }

    /**
     * Getter for property toMM.
     *
     * @return Value of property toMM.
     */
    public java.lang.Double getToMM() {
        return toMM;
    }

    /**
     * Setter for property toMM.
     *
     * @param toMM New value of property toMM.
     */
    public void setToMM(java.lang.Double toMM) {
        this.toMM = toMM;
    }

    /**
     * Getter for property toYYYY.
     *
     * @return Value of property toYYYY.
     */
    public java.lang.Double getToYYYY() {
        return toYYYY;
    }

    /**
     * Setter for property toYYYY.
     *
     * @param toYYYY New value of property toYYYY.
     */
    public void setToYYYY(java.lang.Double toYYYY) {
        this.toYYYY = toYYYY;
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
     * Getter for property accountHead.
     *
     * @return Value of property accountHead.
     */
    public java.lang.String getAccountHead() {
        return accountHead;
    }

    /**
     * Setter for property accountHead.
     *
     * @param accountHead New value of property accountHead.
     */
    public void setAccountHead(java.lang.String accountHead) {
        this.accountHead = accountHead;
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
     * Getter for property employeeId.
     *
     * @return Value of property employeeId.
     */
    public java.lang.String getEmployeeId() {
        return employeeId;
    }

    /**
     * Setter for property employeeId.
     *
     * @param employeeId New value of property employeeId.
     */
    public void setEmployeeId(java.lang.String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Getter for property loanAccNo.
     *
     * @return Value of property loanAccNo.
     */
    public java.lang.String getLoanAccNo() {
        return loanAccNo;
    }

    /**
     * Setter for property loanAccNo.
     *
     * @param loanAccNo New value of property loanAccNo.
     */
    public void setLoanAccNo(java.lang.String loanAccNo) {
        this.loanAccNo = loanAccNo;
    }

    /**
     * Getter for property loanAmount.
     *
     * @return Value of property loanAmount.
     */
    public java.lang.String getLoanAmount() {
        return loanAmount;
    }

    /**
     * Setter for property loanAmount.
     *
     * @param loanAmount New value of property loanAmount.
     */
    public void setLoanAmount(java.lang.String loanAmount) {
        this.loanAmount = loanAmount;
    }

    /**
     * Getter for property installmentAmt.
     *
     * @return Value of property installmentAmt.
     */
    public java.lang.String getInstallmentAmt() {
        return installmentAmt;
    }

    /**
     * Setter for property installmentAmt.
     *
     * @param installmentAmt New value of property installmentAmt.
     */
    public void setInstallmentAmt(java.lang.String installmentAmt) {
        this.installmentAmt = installmentAmt;
    }

    /**
     * Getter for property noofInstallments.
     *
     * @return Value of property noofInstallments.
     */
    public java.lang.String getNoofInstallments() {
        return noofInstallments;
    }

    /**
     * Setter for property noofInstallments.
     *
     * @param noofInstallments New value of property noofInstallments.
     */
    public void setNoofInstallments(java.lang.String noofInstallments) {
        this.noofInstallments = noofInstallments;
    }

    /**
     * Getter for property loanAvailedBranch.
     *
     * @return Value of property loanAvailedBranch.
     */
    public java.lang.String getLoanAvailedBranch() {
        return loanAvailedBranch;
    }

    /**
     * Setter for property loanAvailedBranch.
     *
     * @param loanAvailedBranch New value of property loanAvailedBranch.
     */
    public void setLoanAvailedBranch(java.lang.String loanAvailedBranch) {
        this.loanAvailedBranch = loanAvailedBranch;
    }

    /**
     * Getter for property loanDesc.
     *
     * @return Value of property loanDesc.
     */
    public java.lang.String getLoanDesc() {
        return loanDesc;
    }

    /**
     * Setter for property loanDesc.
     *
     * @param loanDesc New value of property loanDesc.
     */
    public void setLoanDesc(java.lang.String loanDesc) {
        this.loanDesc = loanDesc;
    }

    /**
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }

    /**
     * Getter for property loanFromDate.
     *
     * @return Value of property loanFromDate.
     */
    public java.util.Date getLoanFromDate() {
        return loanFromDate;
    }

    /**
     * Setter for property loanFromDate.
     *
     * @param loanFromDate New value of property loanFromDate.
     */
    public void setLoanFromDate(java.util.Date loanFromDate) {
        this.loanFromDate = loanFromDate;
    }

    /**
     * Getter for property loanToDate.
     *
     * @return Value of property loanToDate.
     */
    public java.util.Date getLoanToDate() {
        return loanToDate;
    }

    /**
     * Setter for property loanToDate.
     *
     * @param loanToDate New value of property loanToDate.
     */
    public void setLoanToDate(java.util.Date loanToDate) {
        this.loanToDate = loanToDate;
    }

    /**
     * Getter for property loanStoppedDate.
     *
     * @return Value of property loanStoppedDate.
     */
    public java.util.Date getLoanStoppedDate() {
        return loanStoppedDate;
    }

    /**
     * Setter for property loanStoppedDate.
     *
     * @param loanStoppedDate New value of property loanStoppedDate.
     */
    public void setLoanStoppedDate(java.util.Date loanStoppedDate) {
        this.loanStoppedDate = loanStoppedDate;
    }

    /**
     * Getter for property loanStatus.
     *
     * @return Value of property loanStatus.
     */
    public java.lang.String getLoanStatus() {
        return loanStatus;
    }

    /**
     * Setter for property loanStatus.
     *
     * @param loanStatus New value of property loanStatus.
     */
    public void setLoanStatus(java.lang.String loanStatus) {
        this.loanStatus = loanStatus;
    }

    /**
     * Getter for property dtSlNo.
     *
     * @return Value of property dtSlNo.
     */
    public java.lang.String getDtSlNo() {
        return dtSlNo;
    }

    /**
     * Setter for property dtSlNo.
     *
     * @param dtSlNo New value of property dtSlNo.
     */
    public void setDtSlNo(java.lang.String dtSlNo) {
        this.dtSlNo = dtSlNo;
    }

    /**
     * Getter for property txtInstIntRate.
     *
     * @return Value of property txtInstIntRate.
     */
    public java.lang.String getTxtInstIntRate() {
        return txtInstIntRate;
    }

    /**
     * Setter for property txtInstIntRate.
     *
     * @param txtInstIntRate New value of property txtInstIntRate.
     */
    public void setTxtInstIntRate(java.lang.String txtInstIntRate) {
        this.txtInstIntRate = txtInstIntRate;
    }

    /**
     * Getter for property txtIntNetAmount.
     *
     * @return Value of property txtIntNetAmount.
     */
    public java.lang.String getTxtIntNetAmount() {
        return txtIntNetAmount;
    }

    /**
     * Setter for property txtIntNetAmount.
     *
     * @param txtIntNetAmount New value of property txtIntNetAmount.
     */
    public void setTxtIntNetAmount(java.lang.String txtIntNetAmount) {
        this.txtIntNetAmount = txtIntNetAmount;
    }
}