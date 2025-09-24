/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerPhoneTO.java
 *
 * Created on Wed Feb 16 09:38:12 IST 2005
 */
package com.see.truetransact.transferobject.employee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUST_PHONE.
 */
public class EmployeeTermLoanTO extends TransferObject implements Serializable {

    private String sysId = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String cboEmployeeLoanType = "";
    private String cboLoanAvailedBranch = "";
    private String txtLoanSanctionRefNo = "";
    private Date tdtLoanSanctionDate = null;
    private String txtLoanNo = "";
    private String txtLoanAmount = "";
    private String txtLoanRateofInterest = "";
    private String txtLoanNoOfInstallments = "";
    private String txtLoanInstallmentAmount = "";
    private Date tdtLoanRepaymentStartDate = null;
    private Date tdtLoanRepaymentEndDate = null;
    private String rdoLoanPreCloserYesNo = "";
    private Date tdtLoanCloserDate = null;
    private String txtLoanRemarks = "";

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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(sysId);
        return sysId;
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
        strB.append(getTOString("sysId", sysId));
        strB.append(getTOString("cboEmployeeLoanType", cboEmployeeLoanType));
        strB.append(getTOString("cboLoanAvailedBranch", cboLoanAvailedBranch));
        strB.append(getTOString("txtLoanSanctionRefNo", txtLoanSanctionRefNo));
        strB.append(getTOString("tdtLoanSanctionDate", tdtLoanSanctionDate));
        strB.append(getTOString("txtLoanNo", txtLoanNo));
        strB.append(getTOString("txtLoanAmount", txtLoanAmount));
        strB.append(getTOString("txtLoanRateofInterest", txtLoanRateofInterest));
        strB.append(getTOString("txtLoanNoOfInstallments", txtLoanNoOfInstallments));
        strB.append(getTOString("txtLoanInstallmentAmount", txtLoanInstallmentAmount));
        strB.append(getTOString("tdtLoanRepaymentStartDate", tdtLoanRepaymentStartDate));
        strB.append(getTOString("tdtLoanRepaymentEndDate", tdtLoanRepaymentEndDate));
        strB.append(getTOString("rdoLoanPreCloserYesNo", rdoLoanPreCloserYesNo));
        strB.append(getTOString("tdtLoanCloserDate", tdtLoanCloserDate));
        strB.append(getTOString("txtLoanRemarks", txtLoanRemarks));
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
        strB.append(getTOXml("sysId", sysId));
        strB.append(getTOXml("cboEmployeeLoanType", cboEmployeeLoanType));
        strB.append(getTOXml("cboLoanAvailedBranch", cboLoanAvailedBranch));
        strB.append(getTOXml("txtLoanSanctionRefNo", txtLoanSanctionRefNo));
        strB.append(getTOXml("tdtLoanSanctionDate", tdtLoanSanctionDate));
        strB.append(getTOXml("txtLoanNo", txtLoanNo));
        strB.append(getTOXml("txtLoanAmount", txtLoanAmount));
        strB.append(getTOXml("txtLoanRateofInterest", txtLoanRateofInterest));
        strB.append(getTOXml("txtLoanNoOfInstallments", txtLoanNoOfInstallments));
        strB.append(getTOXml("txtLoanInstallmentAmount", txtLoanInstallmentAmount));
        strB.append(getTOXml("tdtLoanRepaymentStartDate", tdtLoanRepaymentStartDate));
        strB.append(getTOXml("tdtLoanRepaymentEndDate", tdtLoanRepaymentEndDate));
        strB.append(getTOXml("rdoLoanPreCloserYesNo", rdoLoanPreCloserYesNo));
        strB.append(getTOXml("tdtLoanCloserDate", tdtLoanCloserDate));
        strB.append(getTOXml("txtLoanRemarks", txtLoanRemarks));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property sysId.
     *
     * @return Value of property sysId.
     */
    public java.lang.String getSysId() {
        return sysId;
    }

    /**
     * Setter for property sysId.
     *
     * @param sysId New value of property sysId.
     */
    public void setSysId(java.lang.String sysId) {
        this.sysId = sysId;
    }

    /**
     * Getter for property cboEmployeeLoanType.
     *
     * @return Value of property cboEmployeeLoanType.
     */
    public java.lang.String getCboEmployeeLoanType() {
        return cboEmployeeLoanType;
    }

    /**
     * Setter for property cboEmployeeLoanType.
     *
     * @param cboEmployeeLoanType New value of property cboEmployeeLoanType.
     */
    public void setCboEmployeeLoanType(java.lang.String cboEmployeeLoanType) {
        this.cboEmployeeLoanType = cboEmployeeLoanType;
    }

    /**
     * Getter for property cboLoanAvailedBranch.
     *
     * @return Value of property cboLoanAvailedBranch.
     */
    public java.lang.String getCboLoanAvailedBranch() {
        return cboLoanAvailedBranch;
    }

    /**
     * Setter for property cboLoanAvailedBranch.
     *
     * @param cboLoanAvailedBranch New value of property cboLoanAvailedBranch.
     */
    public void setCboLoanAvailedBranch(java.lang.String cboLoanAvailedBranch) {
        this.cboLoanAvailedBranch = cboLoanAvailedBranch;
    }

    /**
     * Getter for property txtLoanSanctionRefNo.
     *
     * @return Value of property txtLoanSanctionRefNo.
     */
    public java.lang.String getTxtLoanSanctionRefNo() {
        return txtLoanSanctionRefNo;
    }

    /**
     * Setter for property txtLoanSanctionRefNo.
     *
     * @param txtLoanSanctionRefNo New value of property txtLoanSanctionRefNo.
     */
    public void setTxtLoanSanctionRefNo(java.lang.String txtLoanSanctionRefNo) {
        this.txtLoanSanctionRefNo = txtLoanSanctionRefNo;
    }

    /**
     * Getter for property tdtLoanSanctionDate.
     *
     * @return Value of property tdtLoanSanctionDate.
     */
    public java.util.Date getTdtLoanSanctionDate() {
        return tdtLoanSanctionDate;
    }

    /**
     * Setter for property tdtLoanSanctionDate.
     *
     * @param tdtLoanSanctionDate New value of property tdtLoanSanctionDate.
     */
    public void setTdtLoanSanctionDate(java.util.Date tdtLoanSanctionDate) {
        this.tdtLoanSanctionDate = tdtLoanSanctionDate;
    }

    /**
     * Getter for property txtLoanNo.
     *
     * @return Value of property txtLoanNo.
     */
    public java.lang.String getTxtLoanNo() {
        return txtLoanNo;
    }

    /**
     * Setter for property txtLoanNo.
     *
     * @param txtLoanNo New value of property txtLoanNo.
     */
    public void setTxtLoanNo(java.lang.String txtLoanNo) {
        this.txtLoanNo = txtLoanNo;
    }

    /**
     * Getter for property txtLoanAmount.
     *
     * @return Value of property txtLoanAmount.
     */
    public java.lang.String getTxtLoanAmount() {
        return txtLoanAmount;
    }

    /**
     * Setter for property txtLoanAmount.
     *
     * @param txtLoanAmount New value of property txtLoanAmount.
     */
    public void setTxtLoanAmount(java.lang.String txtLoanAmount) {
        this.txtLoanAmount = txtLoanAmount;
    }

    /**
     * Getter for property txtLoanRateofInterest.
     *
     * @return Value of property txtLoanRateofInterest.
     */
    public java.lang.String getTxtLoanRateofInterest() {
        return txtLoanRateofInterest;
    }

    /**
     * Setter for property txtLoanRateofInterest.
     *
     * @param txtLoanRateofInterest New value of property txtLoanRateofInterest.
     */
    public void setTxtLoanRateofInterest(java.lang.String txtLoanRateofInterest) {
        this.txtLoanRateofInterest = txtLoanRateofInterest;
    }

    /**
     * Getter for property txtLoanNoOfInstallments.
     *
     * @return Value of property txtLoanNoOfInstallments.
     */
    public java.lang.String getTxtLoanNoOfInstallments() {
        return txtLoanNoOfInstallments;
    }

    /**
     * Setter for property txtLoanNoOfInstallments.
     *
     * @param txtLoanNoOfInstallments New value of property
     * txtLoanNoOfInstallments.
     */
    public void setTxtLoanNoOfInstallments(java.lang.String txtLoanNoOfInstallments) {
        this.txtLoanNoOfInstallments = txtLoanNoOfInstallments;
    }

    /**
     * Getter for property txtLoanInstallmentAmount.
     *
     * @return Value of property txtLoanInstallmentAmount.
     */
    public java.lang.String getTxtLoanInstallmentAmount() {
        return txtLoanInstallmentAmount;
    }

    /**
     * Setter for property txtLoanInstallmentAmount.
     *
     * @param txtLoanInstallmentAmount New value of property
     * txtLoanInstallmentAmount.
     */
    public void setTxtLoanInstallmentAmount(java.lang.String txtLoanInstallmentAmount) {
        this.txtLoanInstallmentAmount = txtLoanInstallmentAmount;
    }

    /**
     * Getter for property tdtLoanRepaymentStartDate.
     *
     * @return Value of property tdtLoanRepaymentStartDate.
     */
    public java.util.Date getTdtLoanRepaymentStartDate() {
        return tdtLoanRepaymentStartDate;
    }

    /**
     * Setter for property tdtLoanRepaymentStartDate.
     *
     * @param tdtLoanRepaymentStartDate New value of property
     * tdtLoanRepaymentStartDate.
     */
    public void setTdtLoanRepaymentStartDate(java.util.Date tdtLoanRepaymentStartDate) {
        this.tdtLoanRepaymentStartDate = tdtLoanRepaymentStartDate;
    }

    /**
     * Getter for property tdtLoanRepaymentEndDate.
     *
     * @return Value of property tdtLoanRepaymentEndDate.
     */
    public java.util.Date getTdtLoanRepaymentEndDate() {
        return tdtLoanRepaymentEndDate;
    }

    /**
     * Setter for property tdtLoanRepaymentEndDate.
     *
     * @param tdtLoanRepaymentEndDate New value of property
     * tdtLoanRepaymentEndDate.
     */
    public void setTdtLoanRepaymentEndDate(java.util.Date tdtLoanRepaymentEndDate) {
        this.tdtLoanRepaymentEndDate = tdtLoanRepaymentEndDate;
    }

    /**
     * Getter for property rdoLoanPreCloserYesNo.
     *
     * @return Value of property rdoLoanPreCloserYesNo.
     */
    public java.lang.String getRdoLoanPreCloserYesNo() {
        return rdoLoanPreCloserYesNo;
    }

    /**
     * Setter for property rdoLoanPreCloserYesNo.
     *
     * @param rdoLoanPreCloserYesNo New value of property rdoLoanPreCloserYesNo.
     */
    public void setRdoLoanPreCloserYesNo(java.lang.String rdoLoanPreCloserYesNo) {
        this.rdoLoanPreCloserYesNo = rdoLoanPreCloserYesNo;
    }

    /**
     * Getter for property tdtLoanCloserDate.
     *
     * @return Value of property tdtLoanCloserDate.
     */
    public java.util.Date getTdtLoanCloserDate() {
        return tdtLoanCloserDate;
    }

    /**
     * Setter for property tdtLoanCloserDate.
     *
     * @param tdtLoanCloserDate New value of property tdtLoanCloserDate.
     */
    public void setTdtLoanCloserDate(java.util.Date tdtLoanCloserDate) {
        this.tdtLoanCloserDate = tdtLoanCloserDate;
    }

    /**
     * Getter for property txtLoanRemarks.
     *
     * @return Value of property txtLoanRemarks.
     */
    public java.lang.String getTxtLoanRemarks() {
        return txtLoanRemarks;
    }

    /**
     * Setter for property txtLoanRemarks.
     *
     * @param txtLoanRemarks New value of property txtLoanRemarks.
     */
    public void setTxtLoanRemarks(java.lang.String txtLoanRemarks) {
        this.txtLoanRemarks = txtLoanRemarks;
    }
}