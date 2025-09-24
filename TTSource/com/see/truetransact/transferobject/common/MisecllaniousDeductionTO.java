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
public class MisecllaniousDeductionTO extends TransferObject implements Serializable {

    private Double md_sl_no = null;
    private String md_grade = "";
    private Date md_from_date = null;
    private Date md_to_date = null;
    private String md_deduction_type = "";
    private Double maximum_amt = null;
    private Double percentage = null;
    private String eligible_allowance = "";
    private Double eligible_percentage = null;
    private Double txtMdFixedAmtValue = null;
    private Double txtFromAmount = null;
    private Double txtToAmount = null;
    private String rdoUsingBasic = "";
    private String mdFixedOrPercentage = "";
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
        strB.append(getTOString("md_sl_no", md_sl_no));
        strB.append(getTOString("md_grade", md_grade));
        strB.append(getTOString("md_from_date", md_from_date));
        strB.append(getTOString("md_to_date", md_to_date));
        strB.append(getTOString("md_deduction_type", md_deduction_type));
        strB.append(getTOString("maximum_amt", maximum_amt));
        strB.append(getTOString("percentage", percentage));
        strB.append(getTOString("eligible_allowance", eligible_allowance));
        strB.append(getTOString("eligible_percentage", eligible_percentage));
        strB.append(getTOString("mdFixedOrPercentage", mdFixedOrPercentage));
        strB.append(getTOString("txtMdFixedAmtValue", txtMdFixedAmtValue));
        strB.append(getTOString("txtFromAmount", txtFromAmount));
        strB.append(getTOString("txtToAmount", txtToAmount));
        strB.append(getTOString("rdoUsingBasic", rdoUsingBasic));
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
        strB.append(getTOXml("md_sl_no", md_sl_no));
        strB.append(getTOXml("md_grade", md_grade));
        strB.append(getTOXml("md_from_date", md_from_date));
        strB.append(getTOXml("md_to_date", md_to_date));
        strB.append(getTOXml("md_deduction_type", md_deduction_type));
        strB.append(getTOXml("maximum_amt", maximum_amt));
        strB.append(getTOXml("percentage", percentage));
        strB.append(getTOXml("eligible_allowance", eligible_allowance));
        strB.append(getTOXml("eligible_percentage", eligible_percentage));
        strB.append(getTOXml("mdFixedOrPercentage", mdFixedOrPercentage));
        strB.append(getTOXml("txtMdFixedAmtValue", txtMdFixedAmtValue));
        strB.append(getTOXml("txtFromAmount", txtFromAmount));
        strB.append(getTOXml("txtToAmount", txtToAmount));
        strB.append(getTOXml("rdoUsingBasic", rdoUsingBasic));
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
     * Getter for property md_sl_no.
     *
     * @return Value of property md_sl_no.
     */
    public java.lang.Double getMd_sl_no() {
        return md_sl_no;
    }

    /**
     * Setter for property md_sl_no.
     *
     * @param md_sl_no New value of property md_sl_no.
     */
    public void setMd_sl_no(java.lang.Double md_sl_no) {
        this.md_sl_no = md_sl_no;
    }

    /**
     * Getter for property md_grade.
     *
     * @return Value of property md_grade.
     */
    public java.lang.String getMd_grade() {
        return md_grade;
    }

    /**
     * Setter for property md_grade.
     *
     * @param md_grade New value of property md_grade.
     */
    public void setMd_grade(java.lang.String md_grade) {
        this.md_grade = md_grade;
    }

    /**
     * Getter for property md_from_date.
     *
     * @return Value of property md_from_date.
     */
    public java.util.Date getMd_from_date() {
        return md_from_date;
    }

    /**
     * Setter for property md_from_date.
     *
     * @param md_from_date New value of property md_from_date.
     */
    public void setMd_from_date(java.util.Date md_from_date) {
        this.md_from_date = md_from_date;
    }

    /**
     * Getter for property md_to_date.
     *
     * @return Value of property md_to_date.
     */
    public java.util.Date getMd_to_date() {
        return md_to_date;
    }

    /**
     * Setter for property md_to_date.
     *
     * @param md_to_date New value of property md_to_date.
     */
    public void setMd_to_date(java.util.Date md_to_date) {
        this.md_to_date = md_to_date;
    }

    /**
     * Getter for property md_deduction_type.
     *
     * @return Value of property md_deduction_type.
     */
    public java.lang.String getMd_deduction_type() {
        return md_deduction_type;
    }

    /**
     * Setter for property md_deduction_type.
     *
     * @param md_deduction_type New value of property md_deduction_type.
     */
    public void setMd_deduction_type(java.lang.String md_deduction_type) {
        this.md_deduction_type = md_deduction_type;
    }

    /**
     * Getter for property maximum_amt.
     *
     * @return Value of property maximum_amt.
     */
    public java.lang.Double getMaximum_amt() {
        return maximum_amt;
    }

    /**
     * Setter for property maximum_amt.
     *
     * @param maximum_amt New value of property maximum_amt.
     */
    public void setMaximum_amt(java.lang.Double maximum_amt) {
        this.maximum_amt = maximum_amt;
    }

    /**
     * Getter for property percentage.
     *
     * @return Value of property percentage.
     */
    public java.lang.Double getPercentage() {
        return percentage;
    }

    /**
     * Setter for property percentage.
     *
     * @param percentage New value of property percentage.
     */
    public void setPercentage(java.lang.Double percentage) {
        this.percentage = percentage;
    }

    /**
     * Getter for property eligible_allowance.
     *
     * @return Value of property eligible_allowance.
     */
    public java.lang.String getEligible_allowance() {
        return eligible_allowance;
    }

    /**
     * Setter for property eligible_allowance.
     *
     * @param eligible_allowance New value of property eligible_allowance.
     */
    public void setEligible_allowance(java.lang.String eligible_allowance) {
        this.eligible_allowance = eligible_allowance;
    }

    /**
     * Getter for property eligible_percentage.
     *
     * @return Value of property eligible_percentage.
     */
    public java.lang.Double getEligible_percentage() {
        return eligible_percentage;
    }

    /**
     * Setter for property eligible_percentage.
     *
     * @param eligible_percentage New value of property eligible_percentage.
     */
    public void setEligible_percentage(java.lang.Double eligible_percentage) {
        this.eligible_percentage = eligible_percentage;
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
     * Getter for property txtMdFixedAmtValue.
     *
     * @return Value of property txtMdFixedAmtValue.
     */
    public java.lang.Double getTxtMdFixedAmtValue() {
        return txtMdFixedAmtValue;
    }

    /**
     * Setter for property txtMdFixedAmtValue.
     *
     * @param txtMdFixedAmtValue New value of property txtMdFixedAmtValue.
     */
    public void setTxtMdFixedAmtValue(java.lang.Double txtMdFixedAmtValue) {
        this.txtMdFixedAmtValue = txtMdFixedAmtValue;
    }

    /**
     * Getter for property mdFixedOrPercentage.
     *
     * @return Value of property mdFixedOrPercentage.
     */
    public java.lang.String getMdFixedOrPercentage() {
        return mdFixedOrPercentage;
    }

    /**
     * Setter for property mdFixedOrPercentage.
     *
     * @param mdFixedOrPercentage New value of property mdFixedOrPercentage.
     */
    public void setMdFixedOrPercentage(java.lang.String mdFixedOrPercentage) {
        this.mdFixedOrPercentage = mdFixedOrPercentage;
    }

    /**
     * Getter for property txtFromAmount.
     *
     * @return Value of property txtFromAmount.
     */
    public java.lang.Double getTxtFromAmount() {
        return txtFromAmount;
    }

    /**
     * Setter for property txtFromAmount.
     *
     * @param txtFromAmount New value of property txtFromAmount.
     */
    public void setTxtFromAmount(java.lang.Double txtFromAmount) {
        this.txtFromAmount = txtFromAmount;
    }

    /**
     * Getter for property txtToAmount.
     *
     * @return Value of property txtToAmount.
     */
    public java.lang.Double getTxtToAmount() {
        return txtToAmount;
    }

    /**
     * Setter for property txtToAmount.
     *
     * @param txtToAmount New value of property txtToAmount.
     */
    public void setTxtToAmount(java.lang.Double txtToAmount) {
        this.txtToAmount = txtToAmount;
    }

    /**
     * Getter for property rdoUsingBasic.
     *
     * @return Value of property rdoUsingBasic.
     */
    public java.lang.String getRdoUsingBasic() {
        return rdoUsingBasic;
    }

    /**
     * Setter for property rdoUsingBasic.
     *
     * @param rdoUsingBasic New value of property rdoUsingBasic.
     */
    public void setRdoUsingBasic(java.lang.String rdoUsingBasic) {
        this.rdoUsingBasic = rdoUsingBasic;
    }
}