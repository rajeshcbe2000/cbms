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
public class HaltingAllowanceTO extends TransferObject implements Serializable {

    private Double halting_sl_no = null;
    private String halting_grade = "";
    private Date halting_from_date = null;
    private Date halting_to_date = null;
    private String halting_allowance_type = "";
    private String halting_parameter_based = "";
    private String halting_sub_parameter = "";
    private String halting_fixed_type = "";
    private String halting_percentage_type = "";
    private Double halting_fixed_amt = null;
    private Double halting_maximum_amt = null;
    private Double percentageValue = null;
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
        strB.append(getTOString("halting_sl_no", halting_sl_no));
        strB.append(getTOString("halting_grade", halting_grade));
        strB.append(getTOString("halting_from_date", halting_from_date));
        strB.append(getTOString("halting_to_date", halting_to_date));
        strB.append(getTOString("halting_allowance_type", halting_allowance_type));
        strB.append(getTOString("halting_parameter_based", halting_parameter_based));
        strB.append(getTOString("halting_sub_parameter", halting_sub_parameter));
        strB.append(getTOString("halting_fixed_type", halting_fixed_type));
        strB.append(getTOString("halting_percentage_type", halting_percentage_type));
        strB.append(getTOString("halting_fixed_amt", halting_fixed_amt));
        strB.append(getTOString("halting_maximum_amt", halting_maximum_amt));
        strB.append(getTOString("percentageValue", percentageValue));
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
        strB.append(getTOXml("halting_sl_no", halting_sl_no));
        strB.append(getTOXml("halting_grade", halting_grade));
        strB.append(getTOXml("halting_from_date", halting_from_date));
        strB.append(getTOXml("halting_to_date", halting_to_date));
        strB.append(getTOXml("halting_allowance_type", halting_allowance_type));
        strB.append(getTOXml("halting_parameter_based", halting_parameter_based));
        strB.append(getTOXml("halting_sub_parameter", halting_sub_parameter));
        strB.append(getTOXml("halting_fixed_type", halting_fixed_type));
        strB.append(getTOXml("halting_percentage_type", halting_percentage_type));
        strB.append(getTOXml("halting_fixed_amt", halting_fixed_amt));
        strB.append(getTOXml("halting_maximum_amt", halting_maximum_amt));
        strB.append(getTOXml("percentageValue", percentageValue));
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
     * Getter for property halting_sl_no.
     *
     * @return Value of property halting_sl_no.
     */
    public java.lang.Double getHalting_sl_no() {
        return halting_sl_no;
    }

    /**
     * Setter for property halting_sl_no.
     *
     * @param halting_sl_no New value of property halting_sl_no.
     */
    public void setHalting_sl_no(java.lang.Double halting_sl_no) {
        this.halting_sl_no = halting_sl_no;
    }

    /**
     * Getter for property halting_grade.
     *
     * @return Value of property halting_grade.
     */
    public java.lang.String getHalting_grade() {
        return halting_grade;
    }

    /**
     * Setter for property halting_grade.
     *
     * @param halting_grade New value of property halting_grade.
     */
    public void setHalting_grade(java.lang.String halting_grade) {
        this.halting_grade = halting_grade;
    }

    /**
     * Getter for property halting_from_date.
     *
     * @return Value of property halting_from_date.
     */
    public java.util.Date getHalting_from_date() {
        return halting_from_date;
    }

    /**
     * Setter for property halting_from_date.
     *
     * @param halting_from_date New value of property halting_from_date.
     */
    public void setHalting_from_date(java.util.Date halting_from_date) {
        this.halting_from_date = halting_from_date;
    }

    /**
     * Getter for property halting_to_date.
     *
     * @return Value of property halting_to_date.
     */
    public java.util.Date getHalting_to_date() {
        return halting_to_date;
    }

    /**
     * Setter for property halting_to_date.
     *
     * @param halting_to_date New value of property halting_to_date.
     */
    public void setHalting_to_date(java.util.Date halting_to_date) {
        this.halting_to_date = halting_to_date;
    }

    /**
     * Getter for property halting_allowance_type.
     *
     * @return Value of property halting_allowance_type.
     */
    public java.lang.String getHalting_allowance_type() {
        return halting_allowance_type;
    }

    /**
     * Setter for property halting_allowance_type.
     *
     * @param halting_allowance_type New value of property
     * halting_allowance_type.
     */
    public void setHalting_allowance_type(java.lang.String halting_allowance_type) {
        this.halting_allowance_type = halting_allowance_type;
    }

    /**
     * Getter for property halting_parameter_based.
     *
     * @return Value of property halting_parameter_based.
     */
    public java.lang.String getHalting_parameter_based() {
        return halting_parameter_based;
    }

    /**
     * Setter for property halting_parameter_based.
     *
     * @param halting_parameter_based New value of property
     * halting_parameter_based.
     */
    public void setHalting_parameter_based(java.lang.String halting_parameter_based) {
        this.halting_parameter_based = halting_parameter_based;
    }

    /**
     * Getter for property halting_sub_parameter.
     *
     * @return Value of property halting_sub_parameter.
     */
    public java.lang.String getHalting_sub_parameter() {
        return halting_sub_parameter;
    }

    /**
     * Setter for property halting_sub_parameter.
     *
     * @param halting_sub_parameter New value of property halting_sub_parameter.
     */
    public void setHalting_sub_parameter(java.lang.String halting_sub_parameter) {
        this.halting_sub_parameter = halting_sub_parameter;
    }

    /**
     * Getter for property halting_fixed_type.
     *
     * @return Value of property halting_fixed_type.
     */
    public java.lang.String getHalting_fixed_type() {
        return halting_fixed_type;
    }

    /**
     * Setter for property halting_fixed_type.
     *
     * @param halting_fixed_type New value of property halting_fixed_type.
     */
    public void setHalting_fixed_type(java.lang.String halting_fixed_type) {
        this.halting_fixed_type = halting_fixed_type;
    }

    /**
     * Getter for property halting_percentage_type.
     *
     * @return Value of property halting_percentage_type.
     */
    public java.lang.String getHalting_percentage_type() {
        return halting_percentage_type;
    }

    /**
     * Setter for property halting_percentage_type.
     *
     * @param halting_percentage_type New value of property
     * halting_percentage_type.
     */
    public void setHalting_percentage_type(java.lang.String halting_percentage_type) {
        this.halting_percentage_type = halting_percentage_type;
    }

    /**
     * Getter for property halting_fixed_amt.
     *
     * @return Value of property halting_fixed_amt.
     */
    public java.lang.Double getHalting_fixed_amt() {
        return halting_fixed_amt;
    }

    /**
     * Setter for property halting_fixed_amt.
     *
     * @param halting_fixed_amt New value of property halting_fixed_amt.
     */
    public void setHalting_fixed_amt(java.lang.Double halting_fixed_amt) {
        this.halting_fixed_amt = halting_fixed_amt;
    }

    /**
     * Getter for property halting_maximum_amt.
     *
     * @return Value of property halting_maximum_amt.
     */
    public java.lang.Double getHalting_maximum_amt() {
        return halting_maximum_amt;
    }

    /**
     * Setter for property halting_maximum_amt.
     *
     * @param halting_maximum_amt New value of property halting_maximum_amt.
     */
    public void setHalting_maximum_amt(java.lang.Double halting_maximum_amt) {
        this.halting_maximum_amt = halting_maximum_amt;
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
     * Getter for property percentageValue.
     *
     * @return Value of property percentageValue.
     */
    public java.lang.Double getPercentageValue() {
        return percentageValue;
    }

    /**
     * Setter for property percentageValue.
     *
     * @param percentageValue New value of property percentageValue.
     */
    public void setPercentageValue(java.lang.Double percentageValue) {
        this.percentageValue = percentageValue;
    }
}