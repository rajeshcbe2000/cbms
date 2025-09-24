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
public class GratuityTO extends TransferObject implements Serializable {

    private Double gratuity_sl_no = null;
    private String gratuity_grade = "";
    private Date gratuity_from_date = null;
    private Date gratuity_to_date = null;
    private Double upto_year = null;
    private Double upto_months_pay = null;
    private Double maximum_months = null;
    private Double beyond_year = null;
    private Double beyond_months_pay = null;
    private Double maximum_amount = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String authorizeBy = "";
    private String authorizeStatus = null;
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
        strB.append(getTOString("gratuity_sl_no", gratuity_sl_no));
        strB.append(getTOString("gratuity_grade", gratuity_grade));
        strB.append(getTOString("gratuity_from_date", gratuity_from_date));
        strB.append(getTOString("gratuity_to_date", gratuity_to_date));
        strB.append(getTOString("upto_year", upto_year));
        strB.append(getTOString("upto_months_pay", upto_months_pay));
        strB.append(getTOString("maximum_months", maximum_months));
        strB.append(getTOString("beyond_year", beyond_year));
        strB.append(getTOString("beyond_months_pay", beyond_months_pay));
        strB.append(getTOString("maximum_amount", maximum_amount));
        strB.append(getTOString("status", status));
        strB.append(getTOString("status_By", statusBy));
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
        strB.append(getTOXml("gratuity_sl_no", gratuity_sl_no));
        strB.append(getTOXml("gratuity_grade", gratuity_grade));
        strB.append(getTOXml("gratuity_from_date", gratuity_from_date));
        strB.append(getTOXml("gratuity_to_date", gratuity_to_date));
        strB.append(getTOXml("upto_year", upto_year));
        strB.append(getTOXml("upto_months_pay", upto_months_pay));
        strB.append(getTOXml("maximum_months", maximum_months));
        strB.append(getTOXml("beyond_year", beyond_year));
        strB.append(getTOXml("beyond_months_pay", beyond_months_pay));
        strB.append(getTOXml("maximum_amount", maximum_amount));
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
     * Getter for property gratuity_sl_no.
     *
     * @return Value of property gratuity_sl_no.
     */
    public java.lang.Double getGratuity_sl_no() {
        return gratuity_sl_no;
    }

    /**
     * Setter for property gratuity_sl_no.
     *
     * @param gratuity_sl_no New value of property gratuity_sl_no.
     */
    public void setGratuity_sl_no(java.lang.Double gratuity_sl_no) {
        this.gratuity_sl_no = gratuity_sl_no;
    }

    /**
     * Getter for property gratuity_grade.
     *
     * @return Value of property gratuity_grade.
     */
    public java.lang.String getGratuity_grade() {
        return gratuity_grade;
    }

    /**
     * Setter for property gratuity_grade.
     *
     * @param gratuity_grade New value of property gratuity_grade.
     */
    public void setGratuity_grade(java.lang.String gratuity_grade) {
        this.gratuity_grade = gratuity_grade;
    }

    /**
     * Getter for property gratuity_from_date.
     *
     * @return Value of property gratuity_from_date.
     */
    public java.util.Date getGratuity_from_date() {
        return gratuity_from_date;
    }

    /**
     * Setter for property gratuity_from_date.
     *
     * @param gratuity_from_date New value of property gratuity_from_date.
     */
    public void setGratuity_from_date(java.util.Date gratuity_from_date) {
        this.gratuity_from_date = gratuity_from_date;
    }

    /**
     * Getter for property gratuity_to_date.
     *
     * @return Value of property gratuity_to_date.
     */
    public java.util.Date getGratuity_to_date() {
        return gratuity_to_date;
    }

    /**
     * Setter for property gratuity_to_date.
     *
     * @param gratuity_to_date New value of property gratuity_to_date.
     */
    public void setGratuity_to_date(java.util.Date gratuity_to_date) {
        this.gratuity_to_date = gratuity_to_date;
    }

    /**
     * Getter for property upto_year.
     *
     * @return Value of property upto_year.
     */
    public java.lang.Double getUpto_year() {
        return upto_year;
    }

    /**
     * Setter for property upto_year.
     *
     * @param upto_year New value of property upto_year.
     */
    public void setUpto_year(java.lang.Double upto_year) {
        this.upto_year = upto_year;
    }

    /**
     * Getter for property upto_months_pay.
     *
     * @return Value of property upto_months_pay.
     */
    public java.lang.Double getUpto_months_pay() {
        return upto_months_pay;
    }

    /**
     * Setter for property upto_months_pay.
     *
     * @param upto_months_pay New value of property upto_months_pay.
     */
    public void setUpto_months_pay(java.lang.Double upto_months_pay) {
        this.upto_months_pay = upto_months_pay;
    }

    /**
     * Getter for property maximum_months.
     *
     * @return Value of property maximum_months.
     */
    public java.lang.Double getMaximum_months() {
        return maximum_months;
    }

    /**
     * Setter for property maximum_months.
     *
     * @param maximum_months New value of property maximum_months.
     */
    public void setMaximum_months(java.lang.Double maximum_months) {
        this.maximum_months = maximum_months;
    }

    /**
     * Getter for property beyond_year.
     *
     * @return Value of property beyond_year.
     */
    public java.lang.Double getBeyond_year() {
        return beyond_year;
    }

    /**
     * Setter for property beyond_year.
     *
     * @param beyond_year New value of property beyond_year.
     */
    public void setBeyond_year(java.lang.Double beyond_year) {
        this.beyond_year = beyond_year;
    }

    /**
     * Getter for property beyond_months_pay.
     *
     * @return Value of property beyond_months_pay.
     */
    public java.lang.Double getBeyond_months_pay() {
        return beyond_months_pay;
    }

    /**
     * Setter for property beyond_months_pay.
     *
     * @param beyond_months_pay New value of property beyond_months_pay.
     */
    public void setBeyond_months_pay(java.lang.Double beyond_months_pay) {
        this.beyond_months_pay = beyond_months_pay;
    }

    /**
     * Getter for property maximum_amount.
     *
     * @return Value of property maximum_amount.
     */
    public java.lang.Double getMaximum_amount() {
        return maximum_amount;
    }

    /**
     * Setter for property maximum_amount.
     *
     * @param maximum_amount New value of property maximum_amount.
     */
    public void setMaximum_amount(java.lang.Double maximum_amount) {
        this.maximum_amount = maximum_amount;
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
}